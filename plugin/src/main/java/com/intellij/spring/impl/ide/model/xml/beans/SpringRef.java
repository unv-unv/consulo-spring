// Generated on Thu Nov 09 17:15:14 MSK 2006
// DTD/Schema  :    http://www.springframework.org/schema/beans

package com.intellij.spring.impl.ide.model.xml.beans;

import jakarta.annotation.Nonnull;

import com.intellij.spring.impl.ide.model.converters.ParentRefConverter;
import consulo.xml.util.xml.Attribute;
import consulo.xml.util.xml.Convert;
import consulo.xml.util.xml.GenericAttributeValue;

/**
 * http://www.springframework.org/schema/beans:refElemType interface.
 */
public interface SpringRef extends RefBase {


  /**
   * Returns the value of the parent child.
   * <pre>
   * <h3>Attribute null:parent documentation</h3>
   * 	The name of the referenced bean *in a parent factory*.
   * <p/>
   * </pre>
   *
   * @return the value of the parent child.
   */
  @Nonnull
  @Attribute("parent")
  @Convert(value = ParentRefConverter.class)
  GenericAttributeValue<SpringBeanPointer> getParentAttr();


}
