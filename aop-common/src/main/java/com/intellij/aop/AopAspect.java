/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */
package com.intellij.aop;

import com.intellij.java.language.psi.PsiClass;
import consulo.language.psi.PsiElement;

import javax.annotation.Nullable;
import java.util.List;

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
