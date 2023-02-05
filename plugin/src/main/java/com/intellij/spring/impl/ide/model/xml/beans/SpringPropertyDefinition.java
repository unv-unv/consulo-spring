/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */
package com.intellij.spring.impl.ide.model.xml.beans;

import com.intellij.java.language.psi.PsiType;
import org.jetbrains.annotations.NonNls;

import javax.annotation.Nullable;

/**
 * @author peter
 */
public interface SpringPropertyDefinition extends SpringValueHolderDefinition {
  @Nullable
  @NonNls
  String getPropertyName();

  @Nullable
  PsiType[] getTypesByValue();
}
