/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */
package com.intellij.aop.jam;

import com.intellij.aop.AopAspect;
import com.intellij.aop.AopModel;
import com.intellij.aop.AopPointcut;
import com.intellij.jam.JamService;
import consulo.disposer.Disposable;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleServiceManager;
import com.intellij.openapi.util.NotNullLazyValue;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.Collections;
import java.util.List;

/**
 * @author peter
 */
public class AopModuleService implements Disposable {
  private final NotNullLazyValue<AopModelImpl> myModel;
  private static final AopModel EMPTY_AOP_MODEL = new AopModel() {
    public List<? extends AopAspect> getAspects() {
      return Collections.emptyList();
    }

    public List<? extends AopPointcut> getPointcuts() {
      return Collections.emptyList();
    }
  };

  @Nonnull
  public static AopModuleService getService(@Nonnull Module module) {
    return ModuleServiceManager.getService(module, AopModuleService.class);
  }

  @Nonnull
  public static AopModel getAopModel(@Nullable Module module) {
    return module == null ? EMPTY_AOP_MODEL : getService(module).getModel();
  }

  public AopModuleService(final @Nonnull Module module) {
    myModel = new NotNullLazyValue<AopModelImpl>() {
      @Nonnull
      protected AopModelImpl compute() {
        return new AopModelImpl(module);
      }
    };
  }

  @Nullable
  public static AopAspectImpl getAspect(@Nonnull PsiClass cls) {
    return JamService.getJamService(cls.getProject()).getJamElement(AopAspectImpl.class, cls);
  }

  @Nullable
  public static AopAdviceImpl getAdvice(@Nonnull PsiMethod method) {
    return JamService.getJamService(method.getProject()).getJamElement(AopAdviceImpl.class, method);
  }
  @Nullable
  public static AopPointcutImpl getPointcut(@Nonnull PsiMethod method) {
    return JamService.getJamService(method.getProject()).getJamElement(AopPointcutImpl.class, method);
  }
  @Nullable
  public static AopIntroductionImpl getIntroduction(@Nonnull PsiField field) {
    return JamService.getJamService(field.getProject()).getJamElement(AopIntroductionImpl.class, field);
  }

  @Nonnull
  public synchronized AopModelImpl getModel() {
    return myModel.getValue();
  }

  public void dispose() {
  }
}
