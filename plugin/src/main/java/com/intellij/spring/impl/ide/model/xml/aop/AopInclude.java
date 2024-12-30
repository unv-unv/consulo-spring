// Generated on Thu Nov 09 17:15:14 MSK 2006
// DTD/Schema  :    http://www.springframework.org/schema/aop

package com.intellij.spring.impl.ide.model.xml.aop;

import consulo.xml.util.xml.GenericAttributeValue;
import jakarta.annotation.Nonnull;

/**
 * http://www.springframework.org/schema/aop:includeType interface.
 */
public interface AopInclude extends SpringAopElement {

	/**
	 * Returns the value of the name child.
	 * <pre>
	 * <h3>Attribute null:name documentation</h3>
	 * 	The regular expression defining which beans are to be included in the
	 * 	list of @AspectJ beans; beans with names matched by the pattern will
	 * 	be included.
	 * 				
	 * </pre>
	 * @return the value of the name child.
	 */
	@Nonnull
	GenericAttributeValue<String> getName();


}
