/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */
package com.intellij.spring.impl.ide.model.xml;

import com.intellij.java.language.psi.PsiMethod;
import com.intellij.spring.impl.ide.model.xml.beans.SpringBeanPointer;
import consulo.xml.psi.xml.XmlAttribute;
import consulo.xml.psi.xml.XmlTag;
import consulo.xml.util.xml.GenericValue;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * @author peter
 */
public interface CustomBean extends CommonSpringBean {

  @Nonnull
  XmlTag getXmlTag();

  @Nullable
  String getClassName();

  @Nullable
  GenericValue<PsiMethod> getFactoryMethod();

  @Nullable GenericValue<SpringBeanPointer> getFactoryBean();

  @Nonnull
  CustomBeanWrapper getWrapper();

  @Nullable
  XmlAttribute getIdAttribute();
}
