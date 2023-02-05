/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */
package com.intellij.aop;

import com.intellij.java.language.psi.PsiParameter;
import consulo.xml.util.xml.GenericValue;

/**
 * @author peter
 */
public interface AopAfterReturningAdvice extends AopAdvice{
  GenericValue<PsiParameter> getReturning();
}
