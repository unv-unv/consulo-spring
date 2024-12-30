/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */
package com.intellij.aop.psi;

import consulo.language.ast.IElementType;
import org.jetbrains.annotations.NonNls;
import jakarta.annotation.Nonnull;

/**
 * @author peter
 */
public class AopElementType extends IElementType
{
  public AopElementType(@Nonnull @NonNls String debugName) {
    super(debugName, AopPointcutExpressionLanguage.getInstance());
  }
}
