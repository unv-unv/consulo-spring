// Generated on Thu Nov 09 17:15:14 MSK 2006
// DTD/Schema  :    http://www.springframework.org/schema/tool

package com.intellij.spring.impl.ide.model.xml.tool;

import jakarta.annotation.Nonnull;

import consulo.xml.util.xml.GenericAttributeValue;

/**
 * http://www.springframework.org/schema/tool:annotationElemType interface.
 */
public interface SpringAnnotation extends SpringToolElement {

	/**
	 * Returns the value of the kind child.
	 * @return the value of the kind child.
	 */
	@Nonnull
	GenericAttributeValue<SpringAnnotationKind> getKind();


	/**
	 * Returns the value of the expected-type child.
	 * @return the value of the expected-type child.
	 */
	@Nonnull
	TypedParameter getExpectedType();


	/**
	 * Returns the value of the assignable-to child.
	 * @return the value of the assignable-to child.
	 */
	@Nonnull
	TypedParameter getAssignableTo();


	/**
	 * Returns the value of the exports child.
	 * @return the value of the exports child.
	 */
	@Nonnull
	Exports getExports();


}
