/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */

package com.intellij.spring.impl.ide.model.xml.beans;

import consulo.xml.util.xml.GenericAttributeValue;
import consulo.xml.util.xml.Convert;
import com.intellij.spring.impl.ide.model.converters.SpringBeanResolveConverter;

/**
 * @author peter
 */
@Convert(value = SpringBeanResolveConverter.PropertyBean.class)
public interface PNamespaceRefValue extends GenericAttributeValue<SpringBeanPointer>, SpringPropertyDefinition {
}