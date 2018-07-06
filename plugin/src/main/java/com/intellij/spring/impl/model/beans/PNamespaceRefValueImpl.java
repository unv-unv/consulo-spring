/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */

package com.intellij.spring.impl.model.beans;

import com.intellij.psi.PsiType;
import com.intellij.spring.model.xml.beans.PNamespaceRefValue;
import com.intellij.spring.model.xml.beans.SpringBeanPointer;
import com.intellij.util.xml.GenericDomValue;
import javax.annotation.Nullable;
import javax.annotation.Nonnull;
import org.jetbrains.annotations.NonNls;

import java.util.List;

/**
 * @author peter
 */
public abstract class PNamespaceRefValueImpl implements PNamespaceRefValue {

  @Nonnull
  public List<? extends PsiType> getRequiredTypes() {
    return PNamespaceValueImpl.getPropertyType(this, getPropertyName());
  }

  @Nonnull
  @NonNls
  public String getPropertyName() {
    final String name = getXmlElementName();
    return name.substring(0, name.length() - "-ref".length());
  }

  @Nullable
  public PsiType[] getTypesByValue() {
    return null;
  }

  @Nonnull
  public GenericDomValue<SpringBeanPointer> getRefElement() {
    return this;
  }

  @Nonnull
  public GenericDomValue<?> getValueElement() {
    return getParent().getGenericInfo().getAttributeChildDescription(getPropertyName()).getDomAttributeValue(getParent());
  }
}