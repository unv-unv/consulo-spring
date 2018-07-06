/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */
package com.intellij.aop;

import javax.annotation.Nonnull;

import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NonNls;

import javax.annotation.Nullable;

/**
 * @author peter
 */
public interface IntroductionManipulator {
  @Nonnull
  PsiElement getCommonProblemElement();

  @Nullable
  AopIntroduction getIntroduction();

  void defineDefaultImpl(final Project project, final ProblemDescriptor descriptor) throws IncorrectOperationException;

  @NonNls
  String getDefaultImplAttributeName();  

  @Nonnull
  PsiElement getInterfaceElement();

  @Nullable
  PsiElement getDefaultImplElement();

}
