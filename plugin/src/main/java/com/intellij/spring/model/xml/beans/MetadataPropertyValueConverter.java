/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */
package com.intellij.spring.model.xml.beans;

import javax.annotation.Nonnull;

import com.intellij.psi.PsiType;
import com.intellij.spring.model.values.PropertyValueConverter;

/**
 * @author peter
 */
public class MetadataPropertyValueConverter extends PropertyValueConverter {
  private final PsiType myRequiredClass;

  public MetadataPropertyValueConverter(final PsiType requiredClass) {
    myRequiredClass = requiredClass;
  }

  @Nonnull
  public PsiType getRequiredType() {
    return myRequiredClass;
  }
}
