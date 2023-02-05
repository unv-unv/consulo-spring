/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */
package com.intellij.aop;

import com.intellij.aop.psi.PointcutMatchDegree;
import com.intellij.aop.psi.PsiPointcutExpression;
import com.intellij.jam.model.common.CommonModelElement;
import com.intellij.java.language.psi.PsiMethod;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author peter
 */
public interface AopAdvice extends CommonModelElement {

  @Nullable
  PsiPointcutExpression getPointcutExpression();

  @Nonnull
  AopAdviceType getAdviceType();

  /**
   * use com.intellij.aop.AopAdviceUtil#accepts(AopAdvice, com.intellij.psi.PsiMethod) 
   */
  PointcutMatchDegree accepts(PsiMethod method);

  @Nullable
  AopAdvisedElementsSearcher getSearcher();
}
