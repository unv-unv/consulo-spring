/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */

package com.intellij.spring.impl.ide.model.xml.beans;

import com.intellij.spring.impl.ide.model.converters.SpringBeanResolveConverter;
import com.intellij.spring.impl.ide.model.values.PropertyValueConverter;
import consulo.xml.util.xml.Attribute;
import consulo.xml.util.xml.Convert;
import consulo.xml.util.xml.GenericAttributeValue;
import jakarta.annotation.Nonnull;

/**
 * @author Dmitry Avdeev
 */
public interface SpringValueHolder extends SpringElementsHolder, SpringValueHolderDefinition {

  /**
   * Returns the value of the ref child.
   * <pre>
   * <h3>Attribute null:ref documentation</h3>
   * 	A short-cut alternative to a nested "<ref bean='...'/>".
   * <p/>
   * </pre>
   *
   * @return the value of the ref child.
   */
  @Nonnull
  @Attribute(value = "ref")
  @Convert(value = SpringBeanResolveConverter.PropertyBean.class)
  GenericAttributeValue<SpringBeanPointer> getRefAttr();

  /**
   * Returns the value of the value child.
   * <pre>
   * <h3>Attribute null:value documentation</h3>
   * 	A short-cut alternative to a nested "<value>...</value>"
   * 	element.
   * <p/>
   * </pre>
   *
   * @return the value of the value child.
   */
  @Nonnull
  @Attribute(value = "value")
  @Convert(PropertyValueConverter.class)
  GenericAttributeValue<String> getValueAttr();

}
