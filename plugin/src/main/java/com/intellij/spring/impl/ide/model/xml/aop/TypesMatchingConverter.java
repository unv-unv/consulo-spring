/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */

package com.intellij.spring.impl.ide.model.xml.aop;

import com.intellij.aop.psi.AopReferenceHolder;
import com.intellij.aop.jam.AopIntroductionImpl;
import consulo.xml.util.xml.Converter;
import consulo.xml.util.xml.ConvertContext;
import consulo.xml.util.xml.GenericAttributeValue;

import jakarta.annotation.Nullable;
import org.jetbrains.annotations.NonNls;

/**
 * @author peter
 */
public class TypesMatchingConverter extends Converter<AopReferenceHolder> {
  public AopReferenceHolder fromString(@Nullable @NonNls String s, final ConvertContext context) {
    return AopIntroductionImpl.getTypesMatchingPattern(((GenericAttributeValue)context.getInvocationElement()).getXmlAttributeValue());
  }

  public String getErrorMessage(@Nullable final String s, final ConvertContext context) {
    return null;
  }

  public String toString(@Nullable AopReferenceHolder psiPointcutExpression, final ConvertContext context) {
    throw new UnsupportedOperationException("Method toString is not yet implemented in " + getClass().getName());
  }
}