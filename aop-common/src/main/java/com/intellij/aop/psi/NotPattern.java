/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */
package com.intellij.aop.psi;

import com.intellij.java.language.psi.PsiJavaPackage;
import com.intellij.java.language.psi.PsiType;
import consulo.application.util.function.Processor;
import consulo.language.psi.PsiManager;

import jakarta.annotation.Nonnull;

/**
 * @author peter
 */
public class NotPattern extends AopPsiTypePattern{
  private final AopPsiTypePattern myInnerPattern;

  public NotPattern(final AopPsiTypePattern innerPattern) {
    myInnerPattern = innerPattern;
  }

  public AopPsiTypePattern getInnerPattern() {
    return myInnerPattern;
  }

  public boolean accepts(@Nonnull PsiType type) {
    return !myInnerPattern.accepts(type);
  }

  public boolean accepts(@Nonnull final String qualifiedName) {
    return !myInnerPattern.accepts(qualifiedName);
  }

  public boolean processPackages(final PsiManager manager, final Processor<PsiJavaPackage> processor) {
    return TRUE.processPackages(manager, processor);
  }
}
