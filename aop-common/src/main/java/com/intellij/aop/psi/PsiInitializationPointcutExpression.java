/*
 * Copyright (c) 2000-2005 by JetBrains s.r.o. All Rights Reserved.
 * Use is subject to license terms.
 */
package com.intellij.aop.psi;

import javax.annotation.Nonnull;

import com.intellij.psi.PsiMember;
import com.intellij.lang.ASTNode;

/**
 * @author peter
 */
public class PsiInitializationPointcutExpression extends MethodPatternPointcut{
  private final boolean myPre;

  public PsiInitializationPointcutExpression(@Nonnull final ASTNode node, boolean isPre) {
    super(node);
    myPre = isPre;
  }

  @Nonnull
  public PointcutMatchDegree acceptsSubject(final PointcutContext context, final PsiMember member) {
    return PointcutMatchDegree.FALSE;
  }

  @Override
  public String toString() {
    return "PsiInitializationPointcutExpression";
  }
}