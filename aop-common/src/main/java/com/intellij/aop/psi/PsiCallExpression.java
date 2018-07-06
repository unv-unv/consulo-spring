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
public class PsiCallExpression extends MethodPatternPointcut{
  public PsiCallExpression(@Nonnull final ASTNode node) {
    super(node);
  }

  @Nonnull
  public PointcutMatchDegree acceptsSubject(final PointcutContext context, final PsiMember member) {
    return PointcutMatchDegree.FALSE;
  }

  @Override
  public String toString() {
    return "PsiCallExpression";
  }
}
