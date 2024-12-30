/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */
package com.intellij.aop.psi;

import consulo.language.psi.PsiElement;

import jakarta.annotation.Nonnull;

import java.util.Collection;

/**
 * @author peter
 */
public interface AopPatternContainer extends PsiElement
{
  @Nonnull
  Collection<AopPsiTypePattern> getPatterns();
}
