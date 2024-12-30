// Generated on Thu Nov 09 17:15:14 MSK 2006
// DTD/Schema  :    http://www.springframework.org/schema/beans

package com.intellij.spring.impl.ide.model.xml.beans;

import com.intellij.java.language.psi.PsiClass;
import consulo.xml.util.xml.DomElement;
import consulo.xml.util.xml.GenericAttributeValue;

import jakarta.annotation.Nonnull;

/**
 * http://www.springframework.org/schema/beans:typedCollectionType interface.
 */
public interface TypedCollection extends DomElement, BaseCollection {

	/**
	 * Returns the value of the value-type child.
	 * <pre>
	 * <h3>Attribute null:value-type documentation</h3>
	 * 	The default Java type for nested values. Must be a fully qualified
	 * 	class name.
	 * 						
	 * </pre>
	 * @return the value of the value-type child.
	 */
	@Nonnull
	GenericAttributeValue<PsiClass> getValueType();

}
