/*
 * Copyright (c) 2000-2005 by JetBrains s.r.o. All Rights Reserved.
 * Use is subject to license terms.
 */
package com.intellij.spring.impl.ide;

import com.intellij.java.impl.psi.impl.beanProperties.BeanProperty;
import com.intellij.java.impl.psi.impl.beanProperties.BeanPropertyFindUsagesHandler;
import com.intellij.java.language.psi.PsiMethod;
import com.intellij.spring.impl.ide.model.converters.SpringBeanUtil;
import com.intellij.spring.impl.ide.model.highlighting.jam.SpringJavaBeanReferencesFindUsagesHandler;
import com.intellij.spring.impl.ide.model.highlighting.jam.SpringJavaExternalBeanFindUsagesHandler;
import com.intellij.spring.impl.ide.model.jam.utils.SpringJamUtils;
import com.intellij.spring.impl.ide.model.properties.SpringPropertiesUtil;
import com.intellij.spring.impl.ide.model.xml.DomSpringBean;
import consulo.annotation.component.ExtensionImpl;
import consulo.find.FindUsagesHandler;
import consulo.find.FindUsagesHandlerFactory;
import consulo.language.psi.PsiElement;

/**
 * @author peter
 */
@ExtensionImpl
public class SpringFindUsagesHandlerFactory extends FindUsagesHandlerFactory {
  public boolean canFindUsages(final PsiElement psiElement) {
    final BeanProperty property = SpringPropertiesUtil.getBeanProperty(psiElement);
    if (property != null) return true;

    if (psiElement instanceof PsiMethod && SpringJamUtils.isExternalBean((PsiMethod)psiElement)) return true;

    return SpringBeanUtil.getTargetSpringBean(psiElement) != null;
  }

  public FindUsagesHandler createFindUsagesHandler(final PsiElement element, final boolean forHighlightUsages) {
    final BeanProperty property = SpringPropertiesUtil.getBeanProperty(element);
    if (property != null) {
      return new BeanPropertyFindUsagesHandler(property);
    }
    if (element instanceof PsiMethod && SpringJamUtils.isExternalBean((PsiMethod)element)) {
      return new SpringJavaExternalBeanFindUsagesHandler((PsiMethod)element);
    }
    final DomSpringBean bean = SpringBeanUtil.getTargetSpringBean(element);
    if (bean != null) {
      return new SpringJavaBeanReferencesFindUsagesHandler(bean);
    }
    return null;
  }
}
