/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */
package com.intellij.aop.psi;

import com.intellij.psi.PsiElement;
import javax.annotation.Nonnull;

import java.util.Collection;

/**
 * @author peter
 */
public interface AopPatternContainer extends PsiElement {
  @Nonnull
  Collection<AopPsiTypePattern> getPatterns();
}
