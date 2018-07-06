/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */
package com.intellij.aop;

import java.util.Collections;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.intellij.openapi.extensions.ExtensionPointName;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.util.Pair;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;

/**
 * @author peter
 */
public abstract class AopProvider {
  public static final ExtensionPointName<AopProvider> EXTENSION_POINT_NAME = ExtensionPointName.create("com.intellij.spring.aop.provider");

  @Nonnull
  public Set<? extends AopAspect> getAdditionalAspects(@Nonnull Module module) {
    return Collections.emptySet();
  }

  @Nullable
  public abstract AopAdvisedElementsSearcher getAdvisedElementsSearcher(@Nonnull PsiClass aClass);

  @Nullable
  public Pair<? extends ArgNamesManipulator, PsiMethod> getCustomArgNamesManipulator(@Nonnull PsiElement element) {
    return null;
  }

  @Nullable
  public Integer getAdviceOrder(AopAdvice advice) {
    return null;
  }

}
