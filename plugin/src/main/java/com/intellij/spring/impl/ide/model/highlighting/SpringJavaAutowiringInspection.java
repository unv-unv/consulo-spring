package com.intellij.spring.impl.ide.model.highlighting;

import com.intellij.java.analysis.impl.codeInspection.BaseJavaLocalInspectionTool;
import com.intellij.java.language.codeInsight.AnnotationUtil;
import com.intellij.java.language.psi.*;
import com.intellij.java.language.psi.util.InheritanceUtil;
import com.intellij.java.language.psi.util.PropertyUtil;
import com.intellij.java.language.psi.util.PsiUtil;
import com.intellij.spring.impl.ide.SpringBundle;
import com.intellij.spring.impl.ide.SpringManager;
import com.intellij.spring.impl.ide.SpringModel;
import com.intellij.spring.impl.ide.java.SpringJavaClassInfo;
import com.intellij.spring.impl.ide.model.jam.utils.JamAnnotationTypeUtil;
import com.intellij.spring.impl.ide.model.xml.beans.DomSpringBeanPointer;
import com.intellij.spring.impl.ide.model.xml.beans.SpringBaseBeanPointer;
import com.intellij.spring.impl.ide.model.xml.beans.SpringBeanPointer;
import com.intellij.spring.impl.ide.references.SpringBeanReference;
import com.intellij.spring.impl.ide.references.SpringQualifierReference;
import consulo.annotation.component.ExtensionImpl;
import consulo.java.impl.model.annotations.AnnotationModelUtil;
import consulo.language.editor.inspection.ProblemDescriptor;
import consulo.language.editor.inspection.ProblemHighlightType;
import consulo.language.editor.inspection.ProblemsHolder;
import consulo.language.editor.inspection.scheme.InspectionManager;
import consulo.language.editor.rawHighlight.HighlightDisplayLevel;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiNameIdentifierOwner;
import consulo.language.psi.PsiReference;
import consulo.language.util.ModuleUtilCore;
import consulo.module.Module;
import consulo.util.lang.StringUtil;
import org.jetbrains.annotations.NonNls;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * User: Sergey.Vasiliev
 */
@ExtensionImpl
public class SpringJavaAutowiringInspection extends BaseJavaLocalInspectionTool {

  @Nullable
  private static SpringModel getModelForBean(final PsiClass aClass) {
    final SpringJavaClassInfo info = SpringJavaClassInfo.getSpringJavaClassInfo(aClass);
    final List<DomSpringBeanPointer> beans = info.getMappedBeans();
    final Module module = ModuleUtilCore.findModuleForPsiElement(aClass);
    if (module == null) {
      return null;
    }

    if (beans.isEmpty()) {
      if (!AnnotationUtil.isAnnotated(aClass, JamAnnotationTypeUtil.getCustomComponentAnnotations(module))) {
        return null;
      }
    }

    return SpringManager.getInstance(module.getProject()).getCombinedModel(module);
  }

  @Override
  public ProblemDescriptor[] checkMethod(@Nonnull PsiMethod psiMethod, @Nonnull InspectionManager manager, boolean isOnTheFly) {
    if (SpringAutowireUtil.isAutowiredByAnnotation(psiMethod)) {
      SpringModel springModel = getModelForBean(psiMethod.getContainingClass());
      if (springModel != null) {
        final ProblemsHolder holder = new ProblemsHolder(manager, psiMethod.getContainingFile(), isOnTheFly);
        final boolean required = SpringAutowireUtil.isRequired(psiMethod);
        checkAutowiredMethod(psiMethod, holder, springModel, required);
        return holder.getResultsArray();
      }
    }
    return null;
  }

  @Override
  public ProblemDescriptor[] checkField(@Nonnull PsiField psiField, @Nonnull InspectionManager manager, boolean isOnTheFly) {
    if (SpringAutowireUtil.isAutowiredByAnnotation(psiField)) {
      SpringModel springModel = getModelForBean(psiField.getContainingClass());
      if (springModel != null) {
        final ProblemsHolder holder = new ProblemsHolder(manager, psiField.getContainingFile(), isOnTheFly);
        final boolean required = SpringAutowireUtil.isRequired(psiField);
        checkAutowiredPsiMember(psiField, psiField.getType(), holder, springModel, required);
        return holder.getResultsArray();
      }
    }
    return null;
  }

  private static void checkAutowiredMethod(final PsiMethod psiMethod, @Nullable final ProblemsHolder holder, final SpringModel springModel,
                                           final boolean required) {
    final PsiAnnotation resourceAnnotation = SpringAutowireUtil.getResourceAnnotation(psiMethod);
    if (resourceAnnotation != null) {
      final PsiType type = PropertyUtil.getPropertyType(psiMethod);
      if (type != null) {
        checkAutowiredPsiMember(psiMethod, type, holder, springModel, required);
      }
    }
    else {
      for (PsiParameter parameter : psiMethod.getParameterList().getParameters()) {
        checkAutowiredPsiMember(parameter, parameter.getType(), holder, springModel, required);
      }
    }
  }

  @Nullable
  public static Collection<SpringBaseBeanPointer> checkAutowiredPsiMember(final PsiModifierListOwner modifierListOwner,
                                                                          @Nonnull final PsiType psiType,
                                                                          @Nullable final ProblemsHolder holder,
                                                                          @Nonnull final SpringModel springModel,
                                                                          final boolean required) {

    PsiType beanType =
      psiType instanceof PsiArrayType ? ((PsiArrayType)psiType).getComponentType() : PsiUtil.extractIterableTypeParameter(psiType, true);
    final boolean isIterable = beanType != null;
    if (beanType == null) {
      beanType = psiType;
    }
    final PsiAnnotation resourceAnnotation = SpringAutowireUtil.getResourceAnnotation(modifierListOwner);
    if (resourceAnnotation != null && modifierListOwner instanceof PsiMember) {
      final PsiAnnotationMemberValue attributeValue = resourceAnnotation.findDeclaredAttributeValue("name");
      if (attributeValue != null) {
        return checkByNameAutowiring(attributeValue, beanType, holder, springModel);
      }
      String name = null;
      if (modifierListOwner instanceof PsiMethod) {
        name = PropertyUtil.getPropertyNameBySetter((PsiMethod)modifierListOwner);
      }
      else if (modifierListOwner instanceof PsiField) {
        name = ((PsiField)modifierListOwner).getName();
      }
      if (name != null) {
        final SpringBeanPointer bean = springModel.findBean(name);
        if (bean != null) {
          return Collections.singleton(bean.getBasePointer());
        }
      }
      return checkByTypeAutowire(((PsiNameIdentifierOwner)modifierListOwner).getNameIdentifier(),
                                 beanType,
                                 holder,
                                 springModel,
                                 isIterable,
                                 required);
    }
    final PsiAnnotation qualifiedAnnotation = SpringAutowireUtil.getQualifiedAnnotation(modifierListOwner);

    if (qualifiedAnnotation == null) {
      return checkByTypeAutowire(((PsiNameIdentifierOwner)modifierListOwner).getNameIdentifier(),
                                 beanType,
                                 holder,
                                 springModel,
                                 isIterable,
                                 required);
    }
    else {
      return checkQualifiedAutowiring(beanType, qualifiedAnnotation, holder, springModel);
    }
  }

  @Nullable
  private static Collection<SpringBaseBeanPointer> checkByNameAutowiring(final PsiAnnotationMemberValue annotationMemberValue,
                                                                         final PsiType psiType,
                                                                         @Nullable final ProblemsHolder holder,
                                                                         @Nonnull final SpringModel model) {
    SpringBeanReference ref = null;
    for (PsiReference reference : annotationMemberValue.getReferences()) {
      if (reference instanceof SpringBeanReference) {
        ref = (SpringBeanReference)reference;
      }
    }
    if (ref != null) {
      final SpringBeanPointer bean = model.findBean(ref.getValue());
      if (bean != null) {
        final PsiClass beanClass = bean.getBeanClass();
        if (psiType instanceof PsiClassType) {
          final PsiClass psiClass = ((PsiClassType)psiType).resolve();
          if (psiClass != null && InheritanceUtil.isInheritorOrSelf(beanClass, psiClass, true)) {
            return Collections.singleton(bean.getBasePointer());
          }
        }
        if (holder != null) {
          holder.registerProblem(ref, SpringBundle.message("cannot.autowire.bean.of.type", psiType.getCanonicalText()), ProblemHighlightType
            .GENERIC_ERROR_OR_WARNING);
        }
      }
      else {
        if (holder != null) {
          holder.registerProblem(ref);
        }
      }
    }
    return null;
  }

  @Nullable
  private static List<SpringBaseBeanPointer> checkQualifiedAutowiring(final PsiType type,
                                                                      final PsiAnnotation qualifiedAnnotation,
                                                                      @Nullable final ProblemsHolder holder,
                                                                      @Nonnull final SpringModel springModel) {

    final PsiAnnotationMemberValue attributeValue = qualifiedAnnotation.findDeclaredAttributeValue("value");
    PsiReference qreference = null;
    final String name = attributeValue == null ? null : AnnotationModelUtil.getStringValue(qualifiedAnnotation, "value", "").getValue();
    final PsiReference[] references;
    if (attributeValue != null) {
      references = attributeValue.getReferences();
      for (PsiReference reference : references) {
        if (reference instanceof SpringQualifierReference) {
          qreference = reference;
          if (((SpringQualifierReference)reference).multiResolve(false).length == 0) {
            if (holder != null && reference.getElement().isPhysical()) {
              holder.registerProblem(reference,
                                     SpringBundle.message("bean.class.unknown.qualifier.bean", name),
                                     ProblemHighlightType.LIKE_UNKNOWN_SYMBOL);
            }
            return null;
          }
        }
      }
    }
    if (type instanceof PsiClassType) {
      final PsiClass aClass = ((PsiClassType)type).resolve();
      if (aClass != null) {
        List<SpringBaseBeanPointer> candidates = SpringAutowireUtil.getQualifiedBeans(qualifiedAnnotation, springModel);
        if (name != null) {
          final SpringBeanPointer pointer = springModel.findBean(name);
          if (pointer != null) {
            candidates = new ArrayList<SpringBaseBeanPointer>(candidates);
            candidates.add(pointer.getBasePointer());
          }
        }
        final List<SpringBaseBeanPointer> beanPointers = SpringAutowireUtil.excludeAutowireCandidatesForCommonBeans(candidates);

        if (beanPointers.size() == 0) {
          if (holder != null) {
            if (attributeValue != null) {
              reportProblem(holder, qreference, attributeValue,
                            SpringBundle.message("bean.class.unknown.qualifier.bean", name));
            }
            else {
              final String qualifiedName = qualifiedAnnotation.getQualifiedName();
              assert qualifiedName != null;
              reportProblem(holder, qreference, qualifiedAnnotation,
                            SpringBundle.message("cannot.find.bean.qualified.by", "@" + StringUtil.getShortName(qualifiedName)));
            }
          }
          return null;
        }
        else if (beanPointers.size() == 1) {
          boolean isAssignable = false;
          for (SpringBaseBeanPointer bean : beanPointers) {
            @NonNls PsiClass[] psiClasses = bean.getEffectiveBeanType();
            for (PsiClass psiClass : psiClasses) {
              if (InheritanceUtil.isInheritorOrSelf(psiClass, aClass, true)) {
                isAssignable = true;
                break;
              }
            }
          }

          if (!isAssignable) {
            if (holder != null) {
              final String message = SpringBundle.message("bean.class.autowired.incorrect.qualifier.type", type.getPresentableText());
              reportProblem(holder, qreference, attributeValue == null ? qualifiedAnnotation : attributeValue, message);
            }
            return null;
          }
        }
        return beanPointers;
      }
    }
    return null;
  }

  private static void reportProblem(@Nonnull final ProblemsHolder holder,
                                    final PsiReference qreference,
                                    @Nonnull final PsiAnnotationMemberValue attributeValue,
                                    final String text) {
    if (qreference == null) {
      holder.registerProblem(attributeValue, text);
    }
    else {
      holder.registerProblem(qreference, text, ProblemHighlightType.GENERIC_ERROR_OR_WARNING);
    }
  }

  @Nullable
  private static Collection<SpringBaseBeanPointer> checkByTypeAutowire(final PsiElement psiElement,
                                                                       @Nonnull final PsiType type,
                                                                       @Nullable final ProblemsHolder holder,
                                                                       @Nonnull final SpringModel springModel,
                                                                       final boolean iterable, final boolean required) {

    final Collection<SpringBaseBeanPointer> beanPointers = SpringAutowireUtil.autowireByType(springModel, type);

    if (beanPointers.isEmpty() && required) {
      if (holder != null && !SpringAutowireUtil.isAutowiredByDefault(type)) {
        holder.registerProblem(psiElement, SpringBundle.message("bean.autowiring.by.type.none", type.getPresentableText()));
      }
      return null;
    }
    else if (beanPointers.size() > 1 && !iterable) {
      if (holder != null) {
        final List<String> beanNames = new ArrayList<String>();
        for (SpringBaseBeanPointer bean : beanPointers) {
          @NonNls String beanName = bean.getName();
          if (StringUtil.isEmptyOrSpaces(beanName)) beanName = "Unknown";
          beanNames.add(beanName);
        }
        Collections.sort(beanNames);
        final String message =
          SpringBundle.message("bean.class.autowired.by.type", type.getPresentableText(), StringUtil.join(beanNames, ","));
        holder.registerProblem(psiElement, message);
      }
      return beanPointers;
    }
    else {
      return beanPointers;
    }
  }

  @Nonnull
  public String getGroupDisplayName() {
    return SpringBundle.message("model.inspection.group.name");
  }

  @Nonnull
  public String getDisplayName() {
    return SpringBundle.message("model.qualifiers.in.class.inspection.display.name");
  }

  @Nonnull
  @NonNls
  public String getShortName() {
    return "SpringJavaAutowiringInspection";
  }

  public boolean isEnabledByDefault() {
    return true;
  }

  @Nonnull
  public HighlightDisplayLevel getDefaultLevel() {
    return HighlightDisplayLevel.ERROR;
  }
}

