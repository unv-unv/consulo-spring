/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */
package com.intellij.aop.psi;

import com.intellij.psi.PsiJavaPackage;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiType;
import com.intellij.util.Processor;
import org.jetbrains.annotations.NotNull;

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

  public boolean accepts(@NotNull PsiType type) {
    return !myInnerPattern.accepts(type);
  }

  public boolean accepts(@NotNull final String qualifiedName) {
    return !myInnerPattern.accepts(qualifiedName);
  }

  public boolean processPackages(final PsiManager manager, final Processor<PsiJavaPackage> processor) {
    return TRUE.processPackages(manager, processor);
  }
}
