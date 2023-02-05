/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */

package com.intellij.aop.psi;

import com.intellij.java.language.psi.PsiMember;
import consulo.language.ast.ASTNode;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Collections;

/**
 * @author peter
 */
public class PsiIfPointcutExpression extends AopElementBase implements PsiPointcutExpression{
  public PsiIfPointcutExpression(@Nonnull final ASTNode node) {
    super(node);
  }

  public String toString() {
    return "PsiIfPointcutExpression";
  }

  @Nonnull
  public PointcutMatchDegree acceptsSubject(final PointcutContext context, final PsiMember member) {
    return PointcutMatchDegree.FALSE;
  }

  @Nonnull
  public Collection<AopPsiTypePattern> getPatterns() {
    return Collections.emptyList();
  }

}