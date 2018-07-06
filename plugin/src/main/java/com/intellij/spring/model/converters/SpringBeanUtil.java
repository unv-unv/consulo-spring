/*
 * Copyright (c) 2000-2006 JetBrains s.r.o. All Rights Reserved.
 */

package com.intellij.spring.model.converters;

import com.intellij.openapi.editor.Editor;
import com.intellij.psi.*;
import com.intellij.psi.xml.XmlTag;
import com.intellij.spring.model.xml.DomSpringBean;
import com.intellij.spring.model.xml.beans.SpringBean;
import com.intellij.spring.model.xml.beans.TypeHolder;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.DomManager;
import consulo.codeInsight.TargetElementUtil;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Dmitry Avdeev
 */
public class SpringBeanUtil {

  private SpringBeanUtil() {
  }

  @Nullable
  public static PsiClass getInstantiationClass(SpringBean bean) {
    final PsiMethod factoryMethod = bean.getFactoryMethod().getValue();
    if (factoryMethod != null) {
      return SpringBeanFactoryMethodConverter.getFactoryClass(bean);
    }
    return bean.getBeanClass();
  }

  public static boolean isInstantiatedByFactory(SpringBean bean) {
    return bean.getFactoryMethod().getValue() != null;
  }

  @Nonnull
  public static List<PsiMethod> getInstantiationMethods(final SpringBean springBean) {
    final String factoryMethod = springBean.getFactoryMethod().getStringValue();
    if (factoryMethod != null) {
      return SpringBeanFactoryMethodConverter.getFactoryMethodCandidates(springBean, factoryMethod);
    }
    final PsiClass beanClass = springBean.getBeanClass();
    if (beanClass != null) {
      return Arrays.asList(beanClass.getConstructors()); //private constructor is accepted
    }
    return Collections.emptyList();
  }

  @Nullable
  public static PsiType getRequiredType(@Nonnull TypeHolder valueHolder) {
    final List<? extends PsiType> psiTypes = valueHolder.getRequiredTypes();
    return psiTypes.isEmpty() ? null : psiTypes.get(0);
  }

  @Nullable
  public static PsiClassType getRequiredClass(@Nonnull TypeHolder valueHolder) {
    final PsiType injectionType = getRequiredType(valueHolder);
    if (injectionType instanceof PsiClassType) {
      return (PsiClassType)injectionType;
    }
    return null;
  }

  @Nullable
  public static DomSpringBean getTargetSpringBean(Editor editor) {
    if (editor != null) {
      final PsiElement targetPsiElement = TargetElementUtil.findTargetElement(editor, TargetElementUtil.getReferenceSearchFlags());
      if (targetPsiElement instanceof XmlTag) {
        final DomElement value = DomManager.getDomManager(targetPsiElement.getProject()).getDomElement((XmlTag)targetPsiElement);
        if (value instanceof DomSpringBean) {
          return (DomSpringBean)value;
        }
      }
    }
    return null;
  }

  public static DomSpringBean getTargetSpringBean(final PsiElement element) {

    if (element instanceof XmlTag) {
      final DomElement value = DomManager.getDomManager(element.getProject()).getDomElement((XmlTag)element);
      if (value instanceof DomSpringBean) {
        return (DomSpringBean)value;
      }
    }
    return null;
  }

}
