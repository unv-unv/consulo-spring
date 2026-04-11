// Generated on Thu Nov 09 17:15:14 MSK 2006
// DTD/Schema  :    http://www.springframework.org/schema/lang

package com.intellij.spring.impl.ide.model.xml.lang;

import com.intellij.java.impl.util.xml.converters.values.ClassArrayConverter;
import consulo.xml.dom.DomElement;
import consulo.xml.dom.GenericAttributeValue;
import consulo.xml.dom.Referencing;
import consulo.xml.dom.Required;

import jakarta.annotation.Nonnull;

/**
 * http://www.springframework.org/schema/lang:dynamicScriptType interface.
 */
public interface DynamicScript extends DomElement, SimpleScript {

  /**
   * Returns the value of the script-interfaces child.
   * <pre>
   * <h3>Attribute null:script-interfaces documentation</h3>
   * 	The Java interfaces that the dynamic language-backed object is to expose; comma-delimited.
   *
   * </pre>
   *
   * @return the value of the script-interfaces child.
   */
  @Nonnull
  @Required
  @Referencing(value = ClassArrayConverter.class)
  GenericAttributeValue<String> getScriptInterfaces();
}
