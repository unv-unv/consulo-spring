/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */
package com.intellij.spring.impl.ide.model.xml.aop;

import com.intellij.aop.AopPointcut;
import com.intellij.aop.jam.AopModuleService;
import consulo.language.util.ModuleUtilCore;
import consulo.util.collection.ContainerUtil;
import consulo.util.lang.function.Condition;
import consulo.xml.psi.xml.XmlElement;
import consulo.xml.util.xml.ConvertContext;
import consulo.xml.util.xml.ResolvingConverter;
import org.jetbrains.annotations.NonNls;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;

/**
 * @author peter
 */
public class AopPointcutConverter extends ResolvingConverter<AopPointcut> {
  @Nonnull
  public Collection<? extends AopPointcut> getVariants(final ConvertContext context) {
    final XmlElement element = context.getXmlElement();
    if (element == null) return Collections.emptyList();

    return AopModuleService.getAopModel(ModuleUtilCore.findModuleForPsiElement(element)).getPointcuts();
  }

  public AopPointcut fromString(@Nullable @NonNls final String s, final ConvertContext context) {
    return s == null ? null : ContainerUtil.find(getVariants(context), new Condition<AopPointcut>() {
      public boolean value(final AopPointcut o) {
        return s.equals(o.getQualifiedName().getStringValue());
      }
    });
  }

  public String toString(@Nullable AopPointcut aopPointcut, final ConvertContext context) {
    return aopPointcut == null ? null : aopPointcut.getQualifiedName().getStringValue();
  }
}
