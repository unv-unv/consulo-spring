// Generated on Thu Nov 09 17:15:14 MSK 2006
// DTD/Schema  :    http://www.springframework.org/schema/util

package com.intellij.spring.impl.ide.model.xml.util;

import com.intellij.spring.impl.ide.model.xml.DomSpringBean;
import consulo.xml.util.xml.GenericAttributeValue;
import consulo.xml.util.xml.Required;
import jakarta.annotation.Nonnull;

/**
 * http://www.springframework.org/schema/util:property-pathElemType interface.
 */
public interface PropertyPath extends SpringUtilElement, DomSpringBean {

	/**
	 * Returns the value of the path child.
	 * @return the value of the path child.
	 */
	@Nonnull
	@Required
	GenericAttributeValue<String> getPath();
}
