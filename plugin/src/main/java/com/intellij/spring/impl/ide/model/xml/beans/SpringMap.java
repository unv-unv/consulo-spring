// Generated on Thu Nov 09 17:15:14 MSK 2006
// DTD/Schema  :    http://www.springframework.org/schema/beans

package com.intellij.spring.impl.ide.model.xml.beans;

import com.intellij.java.language.psi.PsiClass;
import com.intellij.spring.impl.ide.constants.SpringConstants;
import consulo.xml.util.xml.DomElement;
import consulo.xml.util.xml.GenericAttributeValue;
import consulo.xml.util.xml.Namespace;

import jakarta.annotation.Nonnull;
import java.util.List;

/**
 * http://www.springframework.org/schema/beans:mapType interface.
 */
@Namespace(SpringConstants.BEANS_NAMESPACE_KEY)
public interface SpringMap extends DomElement, TypedCollection {

  /**
   * Returns the value of the key-type child.
   * <pre>
   * <h3>Attribute null:key-type documentation</h3>
   * 	The default Java type for nested entry keys. Must be a fully qualified
   * 	class name.
   *
   * </pre>
   * @return the value of the key-type child.
   */
  @Nonnull
  GenericAttributeValue<PsiClass> getKeyType();

  /**
   * Returns the list of entry children.
   * <pre>
   * <h3>Element http://www.springframework.org/schema/beans:entry documentation</h3>
   * 	A map entry can be an inner bean, ref, value, or collection.
   * 	The key of the entry is given by the "key" attribute or child element.
   *
   * </pre>
   * @return the list of entry children.
   */
  @Nonnull
  List<SpringEntry> getEntries();
  /**
   * Adds new child to the list of entry children.
   * @return created child
   */
  @SuppressWarnings({"UnusedReturnValue"})
  SpringEntry addEntry();


}
