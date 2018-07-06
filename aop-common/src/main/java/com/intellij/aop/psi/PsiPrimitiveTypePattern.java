/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */
package com.intellij.aop.psi;

import com.intellij.psi.PsiType;
import javax.annotation.Nonnull;

/**
 * @author peter
 */
public class PsiPrimitiveTypePattern extends AopPsiTypePattern {
  private final PsiType myType;

  public PsiPrimitiveTypePattern(@Nonnull PsiType type) {
    myType = type;
  }

  public boolean accepts(@Nonnull final PsiType type) {
    return type.equals(myType);
  }

  @Nonnull
  public PsiType getType() {
    return myType;
  }

}
