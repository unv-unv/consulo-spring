/*
 * Copyright (c) 2000-2005 by JetBrains s.r.o. All Rights Reserved.
 * Use is subject to license terms.
 */
package com.intellij.spring.impl.ide.model.xml.aop;

import jakarta.annotation.Nonnull;

import com.intellij.aop.AopAdvice;
import consulo.xml.dom.GenericAttributeValue;
import consulo.xml.dom.DomElement;
import com.intellij.spring.impl.ide.aop.SpringAdvisedElementsSearcher;

/**
 * @author peter
 */
public interface SpringAopAdvice extends AopAdvice, DomElement {

  @Nonnull
  SpringAdvisedElementsSearcher getSearcher();

  GenericAttributeValue<Integer> getOrder();
}
