// Generated on Thu Nov 09 17:15:14 MSK 2006
// DTD/Schema  :    http://www.springframework.org/schema/util

package com.intellij.spring.impl.ide.model.xml.util;

import com.intellij.java.language.psi.PsiClass;
import com.intellij.spring.impl.ide.model.xml.DomSpringBean;
import com.intellij.spring.impl.ide.model.xml.beans.ListOrSet;
import com.intellij.spring.impl.ide.model.xml.beans.ScopedElement;
import com.intellij.spring.impl.ide.model.xml.beans.TypeHolder;
import consulo.xml.util.xml.GenericAttributeValue;

import jakarta.annotation.Nonnull;

/**
 * http://www.springframework.org/schema/util:listElemType interface.
 */
public interface UtilList extends DomSpringBean, SpringUtilElement, ListOrSet, ScopedElement, TypeHolder {
    /**
     * Returns the value of the list-class child.
     * @return the value of the list-class child.
     */
    @Nonnull
    GenericAttributeValue<PsiClass> getListClass();
}
