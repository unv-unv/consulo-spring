/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */

package com.intellij.aop.psi;

import javax.annotation.Nonnull;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiType;

/**
 * @author peter
 */
public class AopTypeParameterList extends AopAbstractList<PsiType> {

  public AopTypeParameterList(@Nonnull final ASTNode node) {
    super(node);
  }

  protected PsiType getPsiType(@Nonnull final PsiType psiType) {
    return psiType;
  }

  public String toString() {
    return "AopTypeParameterList";
  }

}