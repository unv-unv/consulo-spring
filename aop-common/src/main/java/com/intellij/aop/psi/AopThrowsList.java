/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */

package com.intellij.aop.psi;

import com.intellij.java.language.psi.PsiClassType;
import com.intellij.java.language.psi.PsiReferenceList;
import consulo.language.ast.ASTNode;
import consulo.util.lang.StringUtil;

import javax.annotation.Nonnull;

/**
 * @author peter
 */
public class AopThrowsList extends AopElementBase {
  public AopThrowsList(@Nonnull final ASTNode node) {
    super(node);
  }

  public String toString() {
    return "AopThrowsList";
  }

  public AopReferenceHolder[] getExceptionPatterns() {
    return findChildrenByClass(AopReferenceHolder.class);
  }

  public boolean matches(PsiReferenceList list) {
    if (StringUtil.isEmpty(list.getText())) return false;

    final PsiClassType[] referencedTypes = list.getReferencedTypes();
    for (final AopReferenceHolder pattern : getExceptionPatterns()) {
      boolean matchAll = pattern.getFirstChild() instanceof AopNotExpression;
      PointcutMatchDegree result = PointcutMatchDegree.valueOf(matchAll);
      for (final PsiClassType type : referencedTypes) {
        final PointcutMatchDegree degree = pattern.accepts(type);
        result = matchAll ? PointcutMatchDegree.and(result, degree) : PointcutMatchDegree.or(result, degree);
      }
      if (result == PointcutMatchDegree.FALSE) return false;
    }
    return true;
  }
}