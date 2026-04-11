package com.intellij.spring.impl.ide.model.xml.context;

import com.intellij.spring.impl.ide.constants.SpringConstants;
import consulo.xml.dom.DomElement;
import consulo.xml.dom.Namespace;

@Namespace(SpringConstants.CONTEXT_NAMESPACE_KEY)
public interface SpringContextElement extends DomElement {
}
