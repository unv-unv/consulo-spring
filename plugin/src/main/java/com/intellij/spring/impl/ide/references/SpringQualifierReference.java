package com.intellij.spring.impl.ide.references;

import com.intellij.java.language.psi.*;
import com.intellij.spring.impl.ide.SpringManager;
import com.intellij.spring.impl.ide.SpringModel;
import com.intellij.spring.impl.ide.model.highlighting.SpringAutowireUtil;
import com.intellij.spring.impl.ide.model.jam.qualifiers.SpringJamQualifier;
import com.intellij.spring.impl.ide.model.xml.CommonSpringBean;
import com.intellij.spring.impl.ide.model.xml.SpringQualifier;
import com.intellij.spring.impl.ide.model.xml.beans.SpringBaseBeanPointer;
import com.intellij.spring.impl.ide.model.xml.beans.SpringBeanPointer;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiElementResolveResult;
import consulo.language.psi.PsiReferenceBase;
import consulo.language.psi.ResolveResult;
import consulo.language.psi.scope.GlobalSearchScope;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.language.util.ModuleUtilCore;
import consulo.module.Module;
import consulo.util.collection.ArrayUtil;
import consulo.util.lang.Comparing;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SpringQualifierReference extends PsiReferenceBase.Poly<PsiLiteralExpression> {
  
  public SpringQualifierReference(final PsiLiteralExpression element) {
    super(element);
  }

  public boolean isSoft() {
    return false;
  }

  @Nullable
  private SpringModel getSpringModel() {
    final Module module = ModuleUtilCore.findModuleForPsiElement(myElement);
    if (module == null) return null;

    return SpringManager.getInstance(module.getProject()).getCombinedModel(module);
  }

  public Object[] getVariants() {
    final PsiClass psiAnnoClass = findAnnoPsiClass(myElement);
    final PsiVariable variable = PsiTreeUtil.getParentOfType(myElement, PsiVariable.class);
    if (psiAnnoClass != null && variable != null) {
      final PsiType type = variable.getType();

      final SpringModel model = getSpringModel();
      if (model != null) {
        final List<SpringBaseBeanPointer> pointers = SpringAutowireUtil.autowireByType(model, type);

        final Set<String> variants = new HashSet<String>();
        for (SpringBeanPointer beanPointer : pointers) {
          final CommonSpringBean bean = beanPointer.getSpringBean();
          final SpringQualifier qualifier = bean.getSpringQualifier();
          if (qualifier != null) {
            final String value = qualifier.getQualifierValue();
            if (value != null && Comparing.equal(qualifier.getQualifierType(), psiAnnoClass)) {
              variants.add(value);
            }
          } else {
            variants.add(bean.getBeanName());
          }
        }
        return ArrayUtil.toStringArray(variants);
      }
    }
    return EMPTY_ARRAY;
  }

  @Nonnull
  public ResolveResult[] multiResolve(final boolean incompleteCode) {
    final PsiAnnotation annotation = PsiTreeUtil.getParentOfType(myElement, PsiAnnotation.class);
    final PsiMember member = PsiTreeUtil.getParentOfType(annotation, PsiMember.class);
    if (member == null) {
      return ResolveResult.EMPTY_ARRAY;
    }
    final SpringJamQualifier jamQualifier = new SpringJamQualifier(annotation, null, null);
    

    final Object value = myElement.getValue();
    if (value instanceof String) {
      final PsiClass psiAnnoClass = findAnnoPsiClass(myElement);

      if (psiAnnoClass != null) {
        final SpringModel model = getSpringModel();
        if (model != null) {
          List<ResolveResult> results = new ArrayList<ResolveResult>();
          final List<SpringBaseBeanPointer> qualifiedBeans = model.findQualifiedBeans(jamQualifier);

          for (SpringBaseBeanPointer beanPointer : qualifiedBeans) {
            final CommonSpringBean bean = beanPointer.getSpringBean();
            final SpringQualifier qualifier = bean.getSpringQualifier();
            assert qualifier != null;
            results.add(new PsiElementResolveResult(qualifier.getIdentifyingPsiElement()));
          }
          final String qualifierValue = jamQualifier.getQualifierValue();
          if (qualifierValue != null) {
            final SpringBeanPointer springBeanPointer = model.findBean(qualifierValue);
            if (springBeanPointer != null) {
              final PsiElement psiElement = springBeanPointer.getPsiElement();
              if (psiElement != null) {
                results.add(new PsiElementResolveResult(psiElement));
              }
            }
          }
          return results.toArray(new ResolveResult[results.size()]);
        }
      }
    }
    return ResolveResult.EMPTY_ARRAY;
  }

  @Nullable
  private static PsiClass findAnnoPsiClass(PsiLiteralExpression expression) {
    final PsiAnnotation annotation = PsiTreeUtil.getParentOfType(expression, PsiAnnotation.class);
    final consulo.module.Module module = ModuleUtilCore.findModuleForPsiElement(expression);

    if (annotation != null && module != null) {
      final String qualifiedName = annotation.getQualifiedName();

      if (qualifiedName != null) {
        return JavaPsiFacade.getInstance(module.getProject()).findClass(qualifiedName, GlobalSearchScope.moduleWithDependenciesAndLibrariesScope(module, false));
      }
    }
    return null;
  }

}
