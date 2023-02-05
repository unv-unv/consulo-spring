/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */
package com.intellij.spring.impl.ide.model.xml.beans;

import com.intellij.java.language.psi.PsiType;
import com.intellij.spring.impl.ide.model.values.PropertyValueConverter;

import javax.annotation.Nonnull;

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
