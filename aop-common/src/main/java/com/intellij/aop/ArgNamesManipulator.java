/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */
package com.intellij.aop;

import com.intellij.java.language.psi.PsiParameter;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiReference;
import consulo.language.util.IncorrectOperationException;
import org.jetbrains.annotations.NonNls;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author peter
 */
public abstract class ArgNamesManipulator {
  @Nullable
  public abstract String getArgNames();
  public abstract void setArgNames(@Nullable String argNames) throws IncorrectOperationException;

  @Nonnull
  public abstract PsiElement getArgNamesProblemElement();

  @Nonnull
  public abstract PsiElement getCommonProblemElement();

  @Nonnull
  @NonNls
  public abstract String getArgNamesAttributeName();

  @Nullable
  public abstract PsiReference getReturningReference();

  @Nullable
  public PsiParameter getReturningParameter() {
    final PsiReference psiReference = getReturningReference();
    return psiReference == null ? null : (PsiParameter)psiReference.resolve();
  }

  @Nullable
  public abstract PsiReference getThrowingReference();

  @Nullable
  public PsiParameter getThrowingParameter() {
    final PsiReference psiReference = getThrowingReference();
    return psiReference == null ? null : (PsiParameter)psiReference.resolve();
  }

  @Nullable
  public abstract AopAdviceType getAdviceType();
}
