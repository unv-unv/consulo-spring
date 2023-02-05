// Generated on Thu Nov 09 17:15:14 MSK 2006
// DTD/Schema  :    http://www.springframework.org/schema/beans

package com.intellij.spring.impl.ide.model.xml.beans;

import consulo.xml.util.xml.DomElement;
import consulo.xml.util.xml.Namespace;
import com.intellij.spring.impl.ide.constants.SpringConstants;
import javax.annotation.Nonnull;

import java.util.List;

/**
 * http://www.springframework.org/schema/beans:propsType interface.
 */
@Namespace(SpringConstants.BEANS_NAMESPACE_KEY)
public interface Props extends DomElement, BaseCollection {

	/**
	 * Returns the list of prop children.
	 * <pre>
	 * <h3>Element http://www.springframework.org/schema/beans:prop documentation</h3>
	 * 	The string value of the property. Note that whitespace is trimmed
	 * 	off to avoid unwanted whitespace caused by typical XML formatting.
	 * 			
	 * </pre>
	 * @return the list of prop children.
	 */
	@Nonnull
	List<Prop> getProps();
	/**
	 * Adds new child to the list of prop children.
	 * @return created child
	 */
	Prop addProp();
}
