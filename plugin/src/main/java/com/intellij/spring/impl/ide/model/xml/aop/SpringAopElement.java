/*
 * Copyright (c) 2000-2005 by JetBrains s.r.o. All Rights Reserved.
 * Use is subject to license terms.
 */
package com.intellij.spring.impl.ide.model.xml.aop;

import consulo.xml.util.xml.DomElement;
import consulo.xml.util.xml.Namespace;
import com.intellij.spring.impl.ide.constants.SpringConstants;

/**
 * @author peter
 */
@Namespace(SpringConstants.AOP_NAMESPACE_KEY)
public interface SpringAopElement extends DomElement {
}
