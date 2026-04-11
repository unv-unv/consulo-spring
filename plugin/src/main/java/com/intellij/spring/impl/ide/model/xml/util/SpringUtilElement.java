/*
 * Copyright (c) 2000-2005 by JetBrains s.r.o. All Rights Reserved.
 * Use is subject to license terms.
 */
package com.intellij.spring.impl.ide.model.xml.util;

import com.intellij.spring.impl.ide.constants.SpringConstants;
import consulo.xml.dom.DomElement;
import consulo.xml.dom.Namespace;

/**
 * @author peter
 */
@Namespace(SpringConstants.UTIL_NAMESPACE_KEY)
public interface SpringUtilElement extends DomElement {
}
