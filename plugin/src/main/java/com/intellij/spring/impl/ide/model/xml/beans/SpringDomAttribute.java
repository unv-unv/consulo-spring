package com.intellij.spring.impl.ide.model.xml.beans;

import consulo.xml.util.xml.Namespace;
import consulo.xml.util.xml.GenericDomValue;
import com.intellij.spring.impl.ide.constants.SpringConstants;
import com.intellij.spring.impl.ide.model.xml.QualifierAttribute;
import com.intellij.spring.impl.ide.model.xml.SpringModelElement;

/**
 * @author Dmitry Avdeev
 */
@Namespace(SpringConstants.BEANS_NAMESPACE_KEY)
public interface SpringDomAttribute extends QualifierAttribute, SpringModelElement {

  GenericDomValue<String> getKey();

  GenericDomValue<String> getValue();
}
