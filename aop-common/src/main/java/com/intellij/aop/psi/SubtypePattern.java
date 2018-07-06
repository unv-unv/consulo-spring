/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */
package com.intellij.aop.psi;

import javax.annotation.Nonnull;

import com.intellij.psi.*;
import com.intellij.util.Processor;

/**
 * @author peter
 */
public class SubtypePattern extends AopPsiTypePattern {
  private final AopPsiTypePattern myBoundPattern;

  public SubtypePattern(final AopPsiTypePattern boundPattern) {
    myBoundPattern = boundPattern;
  }

  public boolean accepts(@Nonnull PsiType type) {
    return canBeAssignableFrom(type) == PointcutMatchDegree.TRUE;
  }

  public AopPsiTypePattern getBoundPattern() {
    return myBoundPattern;
  }

  public boolean processPackages(final PsiManager manager, final Processor<PsiJavaPackage> processor) {
    return TRUE.processPackages(manager, processor);
  }

  @Nonnull
  public final PointcutMatchDegree canBeAssignableFrom(@Nonnull final PsiType type) {
    if (type instanceof PsiWildcardType && !(myBoundPattern instanceof WildcardPattern)) {
      if (myBoundPattern instanceof PsiClassTypePattern) {
        final PsiClassTypePattern pattern = (PsiClassTypePattern)myBoundPattern;
        if (!CommonClassNames.JAVA_LANG_OBJECT.equals(pattern.getText())) {
          return PointcutMatchDegree.FALSE;
        }
      } else {
        return PointcutMatchDegree.FALSE;
      }
    }

    return myBoundPattern.canBeAssignableFrom(type);
  }
}
