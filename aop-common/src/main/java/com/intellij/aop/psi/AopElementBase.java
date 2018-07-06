/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */
package com.intellij.aop.psi;

import javax.annotation.Nonnull;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;

/**
 * @author peter
 */
public class AopElementBase extends ASTWrapperPsiElement {
  public AopElementBase(@Nonnull final ASTNode node) {
    super(node);
  }

  @Nonnull
  public AopPointcutExpressionFile getContainingFile() {
    return (AopPointcutExpressionFile)super.getContainingFile();
  }
}
