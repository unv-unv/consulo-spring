/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */

package com.intellij.spring.impl.ide.model.xml.beans;

import jakarta.annotation.Nonnull;

import com.intellij.spring.impl.ide.constants.SpringConstants;
import com.intellij.spring.impl.ide.model.converters.SpringBeanResolveConverter;
import consulo.xml.util.xml.Convert;
import consulo.xml.util.xml.DomElement;
import consulo.xml.util.xml.GenericAttributeValue;
import consulo.xml.util.xml.Namespace;

/**
 * @author Dmitry Avdeev
 */
@Namespace(SpringConstants.BEANS_NAMESPACE_KEY)
public interface RefBase extends DomElement {
  /**
	 * Returns the value of the bean child.
   * <pre>
   * <h3>Attribute null:bean documentation</h3>
   * 	The name of the referenced bean.
   *
   * </pre>
   * @return the value of the bean child.
   */
  @Nonnull
  @Convert(value = SpringBeanResolveConverter.PropertyBean.class)
  GenericAttributeValue<SpringBeanPointer> getBean();

  /**
	 * Returns the value of the local child.
   * <pre>
   * <h3>Attribute null:local documentation</h3>
   * 	The name of the referenced bean. The value must be a bean ID,
   * 	and thus can be checked by the XML parser, thus should be preferred
   * 	for references within the same bean factory XML file.
   *
   * </pre>
   * @return the value of the local child.
   */
  @Nonnull
  @Convert(value = SpringBeanResolveConverter.PropertyLocal.class)
  GenericAttributeValue<SpringBeanPointer> getLocal();
}
