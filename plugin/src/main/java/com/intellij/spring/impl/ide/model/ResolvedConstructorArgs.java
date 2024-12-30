/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */

package com.intellij.spring.impl.ide.model;

import com.intellij.java.language.psi.PsiMethod;
import com.intellij.java.language.psi.PsiParameter;
import com.intellij.spring.impl.ide.model.xml.beans.ConstructorArg;
import com.intellij.spring.impl.ide.model.xml.beans.SpringBaseBeanPointer;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author Dmitry Avdeev
 */
public interface ResolvedConstructorArgs {

  boolean isResolved();

  @Nullable
  PsiMethod getResolvedMethod();

  @Nullable
  List<PsiMethod> getCheckedMethods();

  /**
   * @return null if {@link #getResolvedMethod()} returns null.
   */
  @Nullable
  Map<ConstructorArg, PsiParameter> getResolvedArgs();

  Map<ConstructorArg, PsiParameter> getResolvedArgs(@Nonnull PsiMethod method);

  Map<PsiParameter, Collection<SpringBaseBeanPointer>> getAutowiredParams(@Nonnull PsiMethod method);
}
