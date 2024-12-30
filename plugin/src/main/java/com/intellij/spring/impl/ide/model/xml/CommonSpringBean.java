package com.intellij.spring.impl.ide.model.xml;

import com.intellij.jam.model.common.CommonModelElement;
import com.intellij.java.language.psi.PsiClass;
import com.intellij.spring.impl.ide.SpringModel;
import consulo.xml.util.xml.NameValue;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public interface CommonSpringBean extends CommonModelElement {
  @Nullable
  @NameValue
  String getBeanName();

  /**
   * Return aliases defined in the bean definition, not including the bean name.
   * @return bean aliases
   * @see SpringModel#getAllBeanNames(String)
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
