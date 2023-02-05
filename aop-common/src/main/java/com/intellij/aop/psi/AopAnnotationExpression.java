/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */

package com.intellij.aop.psi;

import consulo.language.ast.ASTNode;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.Collection;
import java.util.Collections;

/**
 * @author peter
 */
public class AopAnnotationExpression extends AopElementBase implements AopAnnotationPattern{
  public AopAnnotationExpression(@Nonnull final ASTNode node) {
    super(node);
  }

  public String toString() {
    return "AopAnnotationExpression";
  }

  @Nullable
  public AopReferenceHolder getAnnotationPattern() {
    return findChildByClass(AopReferenceHolder.class);
  }

  public final Collection<AopPsiTypePattern> getPatterns() {
    final AopReferenceHolder holder = getAnnotationPattern();
    return holder == null ? Collections.<AopPsiTypePattern>emptyList() : holder.getPatterns();
  }


}