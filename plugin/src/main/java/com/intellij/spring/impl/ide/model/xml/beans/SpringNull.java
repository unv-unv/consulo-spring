/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */
package com.intellij.spring.impl.ide.model.xml.beans;

import consulo.xml.util.xml.GenericDomValue;
import consulo.xml.util.xml.Namespace;
import com.intellij.spring.impl.ide.constants.SpringConstants;

/**
 * @author peter
 */
@Namespace(SpringConstants.BEANS_NAMESPACE_KEY)
public interface SpringNull extends GenericDomValue<String>{
}
