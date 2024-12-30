// Generated on Thu Nov 09 17:15:14 MSK 2006
// DTD/Schema  :    http://www.springframework.org/schema/beans

package com.intellij.spring.impl.ide.model.xml.beans;

import jakarta.annotation.Nonnull;

import consulo.xml.util.xml.DomElement;
import consulo.xml.util.xml.GenericAttributeValue;
import consulo.xml.util.xml.Required;

/**
 * http://www.springframework.org/schema/beans:metaType interface.
 */
public interface Meta extends DomElement {

	/**
	 * Returns the value of the key child.
	 * <pre>
	 * <h3>Attribute null:key documentation</h3>
	 * 	The key name of the metadata parameter being defined.
	 * 				
	 * </pre>
	 * @return the value of the key child.
	 */
	@Nonnull
	@Required
	GenericAttributeValue<String> getKey();


	/**
	 * Returns the value of the value child.
	 * <pre>
	 * <h3>Attribute null:value documentation</h3>
	 * 	The value of the metadata parameter being defined (as a simple String).
	 * 				
	 * </pre>
	 * @return the value of the value child.
	 */
	@Nonnull
	@Required
	GenericAttributeValue<String> getValue();


}
