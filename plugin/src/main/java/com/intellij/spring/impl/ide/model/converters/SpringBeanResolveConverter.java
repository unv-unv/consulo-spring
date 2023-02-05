/*
 * Copyright (c) 2000-2006 JetBrains s.r.o. All Rights Reserved.
 */

/*
 * Created by IntelliJ IDEA.
 * User: Sergey.Vasiliev
 * Date: Nov 10, 2006
 * Time: 4:25:45 PM
 */
package com.intellij.spring.impl.ide.model.converters;

import com.intellij.java.language.psi.*;
import com.intellij.java.language.psi.util.PsiUtil;
import com.intellij.spring.impl.ide.SpringBundle;
import com.intellij.spring.impl.ide.SpringManager;
import com.intellij.spring.impl.ide.SpringModel;
import com.intellij.spring.impl.ide.model.SpringUtils;
import com.intellij.spring.impl.ide.model.xml.CommonSpringBean;
import com.intellij.spring.impl.ide.model.xml.beans.*;
import com.intellij.spring.impl.ide.model.xml.custom.ParseCustomBeanIntention;
import consulo.codeEditor.Editor;
import consulo.language.editor.completion.lookup.LookupElement;
import consulo.language.editor.completion.lookup.LookupElementBuilder;
import consulo.language.editor.inspection.LocalQuickFix;
import consulo.language.editor.inspection.ProblemDescriptor;
import consulo.language.editor.intention.IntentionAction;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.language.util.IncorrectOperationException;
import consulo.project.Project;
import consulo.util.collection.ContainerUtil;
import consulo.util.collection.SmartList;
import consulo.util.lang.StringUtil;
import consulo.xml.psi.xml.XmlTag;
import consulo.xml.util.xml.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class SpringBeanResolveConverter extends ResolvingConverter<SpringBeanPointer> {

  public SpringBeanPointer fromString(final @Nullable String s, final ConvertContext context) {
    if (s == null) return null;
    final SpringModel model = getSpringModel(context);
    return model == null ? null : SpringUtils.getBeanPointer(model, s);
  }

  public String toString(final @Nullable SpringBeanPointer springBeanPointer, final ConvertContext context) {
    return springBeanPointer == null ? null : springBeanPointer.getName();
  }

  @Nonnull
  public Collection<SpringBeanPointer> getVariants(final ConvertContext context) {
    return getVariants(context, false);
  }

  @Override
  public LookupElement createLookupElement(SpringBeanPointer springBeanPointer) {
    return createCompletionVariant(springBeanPointer);
  }

  protected Collection<SpringBeanPointer> getVariants(final ConvertContext context, boolean parentBeans) {
    final SpringModel model = getSpringModel(context);
    if (model == null) return Collections.emptyList();

    final List<SpringBeanPointer> variants = new ArrayList<SpringBeanPointer>();
    final CommonSpringBean currentBean = SpringConverterUtil.getCurrentBeanCustomAware(context);
    processBeans(model, variants, parentBeans ? model.getAllParentBeans() : model.getAllCommonBeans(), parentBeans, currentBean);
    return variants;
  }

  public Collection<SpringBeanPointer> getSmartVariants(final ConvertContext context) {
    final SpringModel model = getSpringModel(context);
    if (model == null) return Collections.emptyList();

    final List<SpringBeanPointer> variants = new ArrayList<SpringBeanPointer>();

    final CommonSpringBean currentBean = SpringConverterUtil.getCurrentBeanCustomAware(context);
    final List<PsiClassType> requiredClasses = getRequiredClasses(context);
    if (requiredClasses != null) {
      for (final PsiClassType requiredClass : requiredClasses) {
        final PsiClass psiClass = PsiUtil.resolveClassInType(requiredClass);
        if (psiClass != null) {
          processBeans(model, variants, model.findBeansByEffectivePsiClassWithInheritance(psiClass), false, currentBean);
        }
        final PsiClass componentClass = PsiUtil.resolveClassInType(PsiUtil.extractIterableTypeParameter(requiredClass, false));
        if (componentClass != null) {
          processBeans(model, variants, model.findBeansByEffectivePsiClassWithInheritance(componentClass), false, currentBean);
        }
      }
    }
    return variants;
  }

  private static void processBeans(SpringModel model,
                                   List<SpringBeanPointer> variants,
                                   Collection<? extends SpringBaseBeanPointer> pointers,
                                   boolean acceptAbstract,
                                   CommonSpringBean currentBean) {
    for (SpringBaseBeanPointer bean : pointers) {
      if (!acceptAbstract && bean.isAbstract()) continue;
      if (bean.isReferenceTo(currentBean)) continue;

      final String beanName = bean.getName();
      if (beanName != null) {
        for (String string : model.getAllBeanNames(beanName)) {
          if (StringUtil.isNotEmpty(string)) {
            variants.add(bean.derive(string));
          }
        }
      }
    }
  }


  private final CreateElementQuickFixProvider<SpringBeanPointer> myQuickFixProvider =
    new CreateElementQuickFixProvider<SpringBeanPointer>(SpringBundle.message("model.bean.quickfix.family")) {
      @Override
      public LocalQuickFix[] getQuickFixes(GenericDomValue<SpringBeanPointer> value) {
        List<LocalQuickFix> result = new SmartList<LocalQuickFix>();
        ContainerUtil.addIfNotNull(result, getQuickFix(value));

        final String id = value.getStringValue();
        if (StringUtil.isNotEmpty(id)) {
          final List<SpringModel> models = SpringUtils.getNonEmptySpringModelsByFile(DomUtil.getFile(value));
          final Collection<XmlTag> tags = ContainerUtil.concat(models, model -> model.getCustomBeanCandidates(id));
          if (!tags.isEmpty()) {
            result.add(new TryParsingCustomBeansFix(tags));
          }
        }

        return result.toArray(new LocalQuickFix[result.size()]);
      }

      protected void apply(final String elementName, final GenericDomValue<SpringBeanPointer> genericDomValue) {
        Beans beans = getBeans(genericDomValue);
        if (beans != null) {
          final SpringBean springBean = beans.addBean();
          springBean.setName(elementName);
          // setting correct class...
          final DomElement parent = genericDomValue.getParent();
          if (parent instanceof SpringInjection) {
            final PsiClassType classType = SpringBeanUtil.getRequiredClass((SpringInjection)parent);
            if (classType != null) {
              springBean.getClazz().setStringValue(classType.getCanonicalText());
            }
          }
        }
      }

      @Nonnull
      protected String getFixName(String elementName) {
        return SpringBundle.message("model.bean.quickfix.message", elementName);
      }
    };

  @Nullable
  protected Beans getBeans(final GenericDomValue<SpringBeanPointer> genericDomValue) {
    return genericDomValue.getParentOfType(Beans.class, false);
  }

  public String getErrorMessage(final String s, final ConvertContext context) {
    return SpringBundle.message("model.bean.error.message", s);
  }

  @Nullable
  protected SpringModel getSpringModel(final ConvertContext context) {
    return SpringManager.getInstance(context.getFile().getProject()).getSpringModelByFile(context.getFile());
  }

  /**
   * Used to filter variants by bean class.
   *
   * @param context conversion context.
   * @return null if no requirement applied; empty list to suppress completion.
   */
  @Nullable
  public List<PsiClassType> getRequiredClasses(ConvertContext context) {
    return null;
  }

  public PsiElement getPsiElement(@Nullable SpringBeanPointer resolvedValue) {
    if (resolvedValue == null) return null;

    return resolvedValue.getPsiElement();
  }

  public LocalQuickFix[] getQuickFixes(final ConvertContext context) {
    return myQuickFixProvider.getQuickFixes((GenericDomValue)context.getInvocationElement());
  }

  @Nullable
  private static List<PsiClassType> getValueClasses(ConvertContext context) {
    final TypeHolder valueHolder = context.getInvocationElement().getParentOfType(TypeHolder.class, false);
    assert valueHolder != null;
    if (valueHolder instanceof ConstructorArg) {
      return getClassesForArg((ConstructorArg)valueHolder);
    }
    else {
      final PsiClassType classType = SpringBeanUtil.getRequiredClass(valueHolder);
      return classType == null ? null : Collections.singletonList(classType);
    }
  }

  @Nullable
  private static List<PsiClassType> getClassesForArg(final ConstructorArg arg) {
    PsiType type = arg.getType().getValue();
    if (type == null) {
      Integer integer = arg.getIndex().getValue();
      int index = -1;
      if (integer != null) {
        index = integer.intValue();
      }
      final ArrayList<PsiClassType> classes = new ArrayList<PsiClassType>();
      final SpringBean springBean = (SpringBean)arg.getParent();
      final List<PsiMethod> psiMethods = SpringBeanUtil.getInstantiationMethods(springBean);
      for (PsiMethod method : psiMethods) {
        final PsiParameterList parameterList = method.getParameterList();
        if (parameterList.getParametersCount() > 0) {
          if (index >= 0) {
            if (index < parameterList.getParametersCount()) {
              type = parameterList.getParameters()[index].getType();
              if (type instanceof PsiClassType) {
                classes.add((PsiClassType)type);
              }
            }
          }
          else {
            for (PsiParameter parameter : parameterList.getParameters()) {
              type = parameter.getType();
              if (type instanceof PsiClassType) {
                classes.add((PsiClassType)type);
              }
            }
          }
        }
      }
      return classes;
    }
    else {
      if (type instanceof PsiClassType) {
        return Collections.singletonList((PsiClassType)type);
      }
      else {
        return Collections.emptyList();
      }
    }
  }

  public static LookupElementBuilder createCompletionVariant(SpringBeanPointer variant) {
    String name = variant.getName();
    PsiClass beanClass = variant.getBeanClass();
    if (name != null) {
      LookupElementBuilder element = LookupElementBuilder.create(variant, name);
      element.withIcon(variant.getBeanIcon());
      if (beanClass != null) {
        element.withTypeText(beanClass.getName());
      }
      return element;
    }
    return null;
  }

  public static class Local extends SpringBeanResolveConverter {

    @Nullable
    protected SpringModel getSpringModel(final ConvertContext context) {
      return SpringManager.getInstance(context.getFile().getProject()).getLocalSpringModel(context.getFile());
    }

  }

  public static class PropertyBean extends SpringBeanResolveConverter {

    @Nullable
    public List<PsiClassType> getRequiredClasses(ConvertContext context) {
      return getValueClasses(context);
    }
  }

  public static class PropertyLocal extends Local {

    @Nullable
    public List<PsiClassType> getRequiredClasses(ConvertContext context) {
      return getValueClasses(context);
    }


    public void bindReference(final GenericDomValue<SpringBeanPointer> genericValue,
                              final ConvertContext context,
                              final PsiElement newTarget) {
      if (newTarget.getContainingFile() != context.getFile()) {
        final RefBase ref = (RefBase)genericValue.getParent();
        assert ref != null;
        ref.getBean().setStringValue(genericValue.getStringValue());
        genericValue.undefine();
      }
    }
  }

  public static class Parent extends SpringBeanResolveConverter {
  }

  public static class Key extends SpringBeanResolveConverter {

    @Nullable
    public List<PsiClassType> getRequiredClasses(ConvertContext context) {
      SpringEntry entry = (SpringEntry)context.getInvocationElement().getParent();
      assert entry != null;
      final PsiClass keyClass = entry.getRequiredKeyClass();
      return keyClass == null
        ? null
        : Collections.singletonList(JavaPsiFacade.getInstance(keyClass.getProject()).getElementFactory().createType(keyClass));
    }
  }

  private static class TryParsingCustomBeansFix implements LocalQuickFix, IntentionAction {
    private final Collection<XmlTag> myTags;

    public TryParsingCustomBeansFix(Collection<XmlTag> tags) {
      myTags = tags;
    }

    @Nonnull
    public String getText() {
      return getName();
    }

    public boolean isAvailable(@Nonnull Project project, Editor editor, PsiFile file) {
      return true;
    }

    public void invoke(@Nonnull Project project, Editor editor, PsiFile file) throws IncorrectOperationException {
      ParseCustomBeanIntention.invokeCustomBeanParsers(project, myTags);
    }

    public boolean startInWriteAction() {
      return false;
    }

    @Nonnull
    public String getName() {
      return SpringBundle.message("try.parsing.custom.beans");
    }

    @Nonnull
    public String getFamilyName() {
      return SpringBundle.message("model.bean.quickfix.family");
    }

    public void applyFix(@Nonnull Project project, @Nonnull ProblemDescriptor descriptor) {
      throw new UnsupportedOperationException("Method applyFix is not yet implemented in " + getClass().getName());
    }
  }
}
