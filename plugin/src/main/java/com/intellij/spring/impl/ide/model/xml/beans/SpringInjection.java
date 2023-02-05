/*
 * Copyright (c) 2000-2006 JetBrains s.r.o. All Rights Reserved.
 */

package com.intellij.spring.impl.ide.model.xml.beans;

import com.intellij.spring.impl.ide.model.xml.util.SpringConstant;
import consulo.xml.util.xml.GenericDomValue;
import consulo.xml.util.xml.SubTag;

import javax.annotation.Nonnull;

/**
 * @author Dmitry Avdeev
 */
public interface SpringInjection extends SpringValueHolder, SpringValueHolderDefinition {

  /**
   * Returns the value of the meta child.
   * <pre>
   * <h3>Element http://www.springframework.org/schema/beans:meta documentation</h3>
   * 	Arbitrary metadata attached to a bean definition.
   * <p/>
   * </pre>
   *
   * @return the value of the meta child.
   */
  @Nonnull
  Meta getMeta();

  /**
   * Returns the value of the description child.
   * <pre>
   * <h3>Element http://www.springframework.org/schema/beans:description documentation</h3>
   * 	Contains informative text describing the purpose of the enclosing
   * 	element.
   * 	Used primarily for user documentation of XML bean definition documents.
   * <p/>
   * </pre>
   *
   * @return the value of the description child.
   */
  @Nonnull
  GenericDomValue<String> getDescription();

  @SubTag("constant")
  SpringConstant getConstant();
}
