/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */
package com.intellij.spring.model.xml;

import javax.annotation.Nonnull;

import com.intellij.psi.PsiMethod;
import com.intellij.psi.xml.XmlTag;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.spring.model.xml.beans.SpringBeanPointer;
import com.intellij.util.xml.GenericValue;
import javax.annotation.Nullable;

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
