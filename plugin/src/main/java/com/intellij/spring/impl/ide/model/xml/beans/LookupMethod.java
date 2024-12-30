// Generated on Thu Nov 09 17:15:14 MSK 2006
// DTD/Schema  :    http://www.springframework.org/schema/beans

package com.intellij.spring.impl.ide.model.xml.beans;

import com.intellij.java.language.psi.PsiMethod;
import com.intellij.spring.impl.ide.model.converters.LookupMethodBeanConverter;
import com.intellij.spring.impl.ide.model.converters.SpringBeanLookupMethodConverter;
import consulo.xml.util.xml.Convert;
import consulo.xml.util.xml.DomElement;
import consulo.xml.util.xml.GenericAttributeValue;

import jakarta.annotation.Nonnull;

/**
 * http://www.springframework.org/schema/beans:lookup-methodElemType interface.
 */
public interface LookupMethod extends DomElement {

	/**
	 * Returns the value of the name child.
	 * <pre>
	 * <h3>Attribute null:name documentation</h3>
	 * 	The name of the lookup method. This method must take no arguments.
	 * 							
	 * </pre>
	 * @return the value of the name child.
	 */
	@Nonnull
        @Convert(value = SpringBeanLookupMethodConverter.class)  
        GenericAttributeValue<PsiMethod> getName();


	/**
	 * Returns the value of the bean child.
	 * <pre>
	 * <h3>Attribute null:bean documentation</h3>
	 * 	The name of the bean in the current or ancestor factories that
	 * 	the lookup method should resolve to. Often this bean will be a
	 * 	prototype, in which case the lookup method will return a distinct
	 * 	instance on every invocation. This is useful for single-threaded objects.
	 * 							
	 * </pre>
	 * @return the value of the bean child.
	 */
	@Nonnull
        @Convert(value = LookupMethodBeanConverter.class)  
        GenericAttributeValue<SpringBeanPointer> getBean();
}
