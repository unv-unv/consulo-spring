// Generated on Thu Nov 09 17:15:14 MSK 2006
// DTD/Schema  :    http://www.springframework.org/schema/util

package com.intellij.spring.impl.ide.model.xml.util;

import jakarta.annotation.Nonnull;

import com.intellij.spring.impl.ide.model.values.converters.FieldRetrievingFactoryBeanConverter;
import com.intellij.spring.impl.ide.model.xml.DomSpringBean;
import consulo.xml.util.xml.GenericAttributeValue;
import consulo.xml.util.xml.Referencing;
import consulo.xml.util.xml.Required;

/**
 * http://www.springframework.org/schema/util:constantElemType interface.
 */
public interface SpringConstant extends SpringUtilElement, DomSpringBean {

	/**
	 * Returns the value of the static-field child.
	 * @return the value of the static-field child.
	 */
	@Nonnull
	@Required
        @Referencing(value = FieldRetrievingFactoryBeanConverter.class) 
        GenericAttributeValue<String> getStaticField();


}
