/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */
package com.intellij.aop;

import com.intellij.java.language.psi.PsiClass;
import com.intellij.java.language.psi.PsiMethod;
import consulo.annotation.component.ComponentScope;
import consulo.annotation.component.ExtensionAPI;
import consulo.component.extension.ExtensionPointName;
import consulo.language.psi.PsiElement;
import consulo.module.Module;
import consulo.util.lang.Pair;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.util.Collections;
import java.util.Set;

/**
 * @author peter
 */
@ExtensionAPI(ComponentScope.APPLICATION)
public abstract class AopProvider {
  public static final ExtensionPointName<AopProvider> EXTENSION_POINT_NAME = ExtensionPointName.create(AopProvider.class);

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
