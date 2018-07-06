/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */

package com.intellij.aop.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMember;
import com.intellij.util.Function;
import com.intellij.util.containers.ContainerUtil;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.Collection;
import java.util.Collections;

/**
 * @author peter
 */
public class AopNotExpression extends AopElementBase implements PsiPointcutExpression, AopTypeExpression, AopAnnotationPattern{
  public AopNotExpression(@Nonnull final ASTNode node) {
    super(node);
  }

  public String toString() {
    return "AopNotExpression";
  }

  @Nullable
  public AopPatternContainer getInnerExpression() {
    return findChildByClass(AopPatternContainer.class);
  }

  @Nonnull
  public PsiElement getNotToken() {
    return findChildByType(AopElementTypes.AOP_NOT);
  }

  @Nonnull
  public PointcutMatchDegree acceptsSubject(final PointcutContext context, final PsiMember member) {
    final AopPatternContainer expression = getInnerExpression();
    return expression instanceof PsiPointcutExpression ? PointcutMatchDegree.not(((PsiPointcutExpression)expression).acceptsSubject(context,
                                                                                                                                    member)) : PointcutMatchDegree.FALSE;
  }

  @Nonnull
  public Collection<AopPsiTypePattern> getInnerPatterns() {
    final AopPatternContainer typeExpression = getInnerExpression();
    if (typeExpression != null) return typeExpression.getPatterns();
    return Collections.emptyList();
  }

  @Nonnull
  public Collection<AopPsiTypePattern> getPatterns() {
    return ContainerUtil.map2List(getInnerPatterns(), new Function<AopPsiTypePattern, AopPsiTypePattern>() {
      public AopPsiTypePattern fun(final AopPsiTypePattern aopPsiTypePattern) {
        return new NotPattern(aopPsiTypePattern);
      }
    });
  }

  public String getTypePattern() {
    final AopPatternContainer expression = getInnerExpression();
    if (expression instanceof AopTypeExpression) {
      final String pattern = ((AopTypeExpression)expression).getTypePattern();
      if (pattern == null) return null;

      return "'_:[!is(\"" + pattern + "\")]";
    }
    return null;
  }
}