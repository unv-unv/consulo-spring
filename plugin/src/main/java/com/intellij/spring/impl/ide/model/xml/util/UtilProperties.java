// Generated on Thu Nov 09 17:15:14 MSK 2006
// DTD/Schema  :    http://www.springframework.org/schema/util

package com.intellij.spring.impl.ide.model.xml.util;

import com.intellij.spring.impl.ide.model.values.converters.ResourceValueConverter;
import com.intellij.spring.impl.ide.model.xml.DomSpringBean;
import com.intellij.spring.impl.ide.model.xml.beans.ScopedElement;
import consulo.xml.util.xml.GenericAttributeValue;
import consulo.xml.util.xml.Referencing;
import consulo.xml.util.xml.Required;
import jakarta.annotation.Nonnull;

/**
 * http://www.springframework.org/schema/util:propertiesElemType interface.
 */
public interface UtilProperties extends SpringUtilElement, DomSpringBean, ScopedElement {
  String BEAN_CLASS_NAME = "org.springframework.beans.factory.config.PropertiesFactoryBean";

  /**
   * Returns the value of the location child.
   *
   * @return the value of the location child.
   */
  @Nonnull
  @Required
  @Referencing(value = ResourceValueConverter.class)
  GenericAttributeValue<String> getLocation();
}
