// Generated on Thu Nov 09 17:15:14 MSK 2006
// DTD/Schema  :    http://www.springframework.org/schema/tool

package com.intellij.spring.impl.ide.model.xml.tool;

import javax.annotation.Nonnull;

import consulo.xml.util.xml.GenericAttributeValue;
import consulo.xml.util.xml.Required;

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
