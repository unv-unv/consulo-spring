// Generated on Thu Nov 09 17:15:14 MSK 2006
// DTD/Schema  :    http://www.springframework.org/schema/beans

package com.intellij.spring.impl.ide.model.xml.beans;

import jakarta.annotation.Nonnull;

import com.intellij.spring.impl.ide.model.converters.PropertyKeyConverter;
import com.intellij.spring.impl.ide.model.values.PropsValueConverter;
import consulo.xml.dom.Convert;
import consulo.xml.dom.GenericAttributeValue;
import consulo.xml.dom.GenericDomValue;
import consulo.xml.dom.Required;

@Convert(PropsValueConverter.class)
public interface Prop extends GenericDomValue<Object> {

  @Nonnull
  @Required
  @Convert(PropertyKeyConverter.class)
  GenericAttributeValue<String> getKey();
}
