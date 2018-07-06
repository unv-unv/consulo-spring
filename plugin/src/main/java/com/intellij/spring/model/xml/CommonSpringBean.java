package com.intellij.spring.model.xml;

import javax.annotation.Nonnull;

import com.intellij.jam.model.common.CommonModelElement;
import com.intellij.psi.PsiClass;
import com.intellij.util.xml.NameValue;

import javax.annotation.Nullable;

public interface CommonSpringBean extends CommonModelElement {
  @Nullable
  @NameValue    
  String getBeanName();

  /**
   * Return aliases defined in the bean definition, not including the bean name.
   * @return bean aliases
   * @see com.intellij.spring.SpringModel#getAllBeanNames(String)
   */
  @Nonnull
  String[] getAliases();

  @Nullable
  PsiClass getBeanClass(boolean considerFactories);

  @Nullable
  PsiClass getBeanClass();

  @Nullable
  SpringQualifier getSpringQualifier();
}
