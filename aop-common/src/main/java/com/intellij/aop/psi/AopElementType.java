/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */
package com.intellij.aop.psi;

import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NonNls;
import javax.annotation.Nonnull;

/**
 * @author peter
 */
public class AopElementType extends IElementType {
  public AopElementType(@Nonnull @NonNls String debugName) {
    super(debugName, AopPointcutExpressionLanguage.getInstance());
  }
}
