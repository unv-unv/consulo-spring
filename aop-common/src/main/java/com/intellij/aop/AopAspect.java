/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */
package com.intellij.aop;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;

import java.util.List;

import javax.annotation.Nullable;

/**
 * @author peter
 */
public interface AopAspect {

  @Nullable
  PsiElement getIdentifyingPsiElement();

  @Nullable
  PsiClass getPsiClass();

  List<? extends AopAdvice> getAdvices();

  List<? extends AopIntroduction> getIntroductions();

}
