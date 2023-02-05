// Generated on Thu Nov 09 17:15:14 MSK 2006
// DTD/Schema  :    http://www.springframework.org/schema/tx

package com.intellij.spring.impl.ide.model.xml.tx;

import consulo.xml.util.xml.Required;
import javax.annotation.Nonnull;

import java.util.List;

/**
 * http://www.springframework.org/schema/tx:attributesType interface.
 */
public interface Attributes extends SpringTxElement {

	/**
	 * Returns the list of method children.
	 * @return the list of method children.
	 */
	@Nonnull
	@Required
	List<SpringMethod> getMethods();
	/**
	 * Adds new child to the list of method children.
	 * @return created child
	 */
	SpringMethod addMethod();


}
