// Generated on Thu Nov 09 17:15:14 MSK 2006
// DTD/Schema  :    http://www.springframework.org/schema/util

package com.intellij.spring.impl.ide.model.xml.util;

import com.intellij.java.language.psi.PsiClass;
import com.intellij.spring.impl.ide.model.xml.DomSpringBean;
import com.intellij.spring.impl.ide.model.xml.beans.ScopedElement;
import com.intellij.spring.impl.ide.model.xml.beans.SpringMap;
import consulo.xml.util.xml.GenericAttributeValue;

import javax.annotation.Nonnull;

/**
 * http://www.springframework.org/schema/util:mapElemType interface.
 */
public interface UtilMap extends DomSpringBean, SpringUtilElement, ScopedElement, SpringMap {

	/**
	 * Returns the value of the map-class child.
	 * @return the value of the map-class child.
	 */
	@Nonnull
	GenericAttributeValue<PsiClass> getMapClass();
}
