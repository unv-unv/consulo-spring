// Generated on Thu Nov 09 17:15:14 MSK 2006
// DTD/Schema  :    http://www.springframework.org/schema/beans

package com.intellij.spring.impl.ide.model.xml.beans;

import consulo.xml.util.xml.DomElement;
import consulo.xml.util.xml.GenericAttributeValue;
import consulo.xml.util.xml.Required;
import javax.annotation.Nonnull;

/**
 * http://www.springframework.org/schema/beans:arg-typeElemType interface.
 */
public interface ArgType extends DomElement {

	/**
	 * Returns the value of the simple content.
	 * @return the value of the simple content.
	 */
	@Nonnull
	@Required
	String getValue();
	/**
	 * Sets the value of the simple content.
	 * @param value the new value to set
	 */
	void setValue(@Nonnull String value);


	/**
	 * Returns the value of the match child.
	 * <pre>
	 * <h3>Attribute null:match documentation</h3>
	 * 	Specification of the type of an overloaded method argument as a String.
	 * 	For convenience, this may be a substring of the FQN. E.g. all the
	 * 	following would match "java.lang.String":
	 * 	- java.lang.String
	 * 	- String
	 * 	- Str
	 * 	As the number of arguments will be checked also, this convenience
	 * 	can often be used to save typing.
	 * 					
	 * </pre>
	 * @return the value of the match child.
	 */
	@Nonnull
	GenericAttributeValue<String> getMatch();


}
