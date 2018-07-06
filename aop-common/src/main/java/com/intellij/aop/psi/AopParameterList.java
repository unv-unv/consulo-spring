/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */
package com.intellij.aop.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.PsiParameterList;
import com.intellij.psi.PsiType;
import com.intellij.util.PairFunction;
import javax.annotation.Nonnull;

/**
 * @author peter
 */
public class AopParameterList extends AopAbstractList<PsiParameter> {

  public AopParameterList(@Nonnull final ASTNode node) {
    super(node);
  }

  protected PsiType getPsiType(@Nonnull final PsiParameter psiParameter) {
    return psiParameter.getType();
  }

  public String toString() {
    return "AopParameterList";
  }

  public PointcutMatchDegree matches(final PointcutContext context, PsiParameterList list, final PairFunction<PsiType, AopReferenceTarget, PointcutMatchDegree> matcher) {
    return accepts(context, list.getParameters(), matcher);
  }

}
