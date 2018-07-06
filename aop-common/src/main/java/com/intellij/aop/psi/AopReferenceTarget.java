/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */
package com.intellij.aop.psi;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiType;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author peter
 */
public interface AopReferenceTarget {
  @Nonnull
  String getQualifiedName();

  PointcutMatchDegree canBeInstance(final PsiClass psiClass, final boolean allowPatterns);

  PointcutMatchDegree accepts(final PsiType actualType);

  @Nullable
  String getTypePattern();

  @Nullable
  PsiClass findClass();

  boolean isAssignableFrom(PsiType type);
}
