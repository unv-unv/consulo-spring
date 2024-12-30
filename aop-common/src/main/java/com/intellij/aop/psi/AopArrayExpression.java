/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */
package com.intellij.aop.psi;

import consulo.language.ast.ASTNode;
import consulo.util.collection.ContainerUtil;

import jakarta.annotation.Nonnull;
import java.util.Collection;

/**
 * @author peter
 */
public class AopArrayExpression extends AopElementBase implements AopTypeExpression {
  public AopArrayExpression(@Nonnull final ASTNode node) {
    super(node);
  }

  public String toString() {
    return "AopArrayExpression";
  }

  @Nonnull
  public AopTypeExpression getTypeReference() {
    return findNotNullChildByClass(AopTypeExpression.class);
  }

  @Nonnull
  public Collection<AopPsiTypePattern> getPatterns() {
    final boolean varargs = isVarargs();
    return ContainerUtil.map2List(getTypeReference().getPatterns(), aopPsiTypePattern -> new ArrayPattern(aopPsiTypePattern, varargs));
  }

  public String getTypePattern() {
    final String pattern = getTypeReference().getTypePattern();
    return pattern == null ? null : isVarargs() ? pattern + "..." : pattern + "[]";
  }

  public boolean isVarargs() {
    return findChildByType(AopElementTypes.AOP_VARARGS) != null;
  }
}
