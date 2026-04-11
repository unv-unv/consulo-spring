// Generated on Thu Nov 09 17:15:14 MSK 2006
// DTD/Schema  :    http://www.springframework.org/schema/tool

package com.intellij.spring.impl.ide.model.xml.tool;

import jakarta.annotation.Nonnull;

import consulo.xml.dom.GenericAttributeValue;
import consulo.xml.dom.Required;

/**
 * http://www.springframework.org/schema/tool:typedParameterType interface.
 */
public interface TypedParameter extends SpringToolElement {

	/**
	 * Returns the value of the type child.
	 * @return the value of the type child.
	 */
	@Nonnull
	@Required
	GenericAttributeValue<String> getType();


}
