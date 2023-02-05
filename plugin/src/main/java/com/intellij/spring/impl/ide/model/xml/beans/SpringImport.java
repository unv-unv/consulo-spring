// Generated on Thu Nov 09 17:15:14 MSK 2006
// DTD/Schema  :    http://www.springframework.org/schema/beans

package com.intellij.spring.impl.ide.model.xml.beans;

import javax.annotation.Nonnull;

import com.intellij.spring.impl.ide.model.converters.SpringImportResourceConverter;
import consulo.xml.util.xml.Convert;
import consulo.xml.util.xml.DomElement;
import consulo.xml.util.xml.GenericAttributeValue;
import consulo.xml.util.xml.Required;
import consulo.language.psi.PsiFile;

/**
 * http://www.springframework.org/schema/beans:importElemType interface.
 */
public interface SpringImport extends DomElement {

  /**
   * Returns the value of the resource child.
   * <pre>
   * <h3>Attribute null:resource documentation</h3>
   * 	The relative resource location of the XML (bean definition) file to import,
   * 	for example "myImport.xml" or "includes/myImport.xml" or "../myImport.xml".
   * <p/>
   * </pre>
   *
   * @return the value of the resource child.
   */
  @Nonnull
  @Required
  @Convert(SpringImportResourceConverter.class)
  GenericAttributeValue<PsiFile> getResource();
}
