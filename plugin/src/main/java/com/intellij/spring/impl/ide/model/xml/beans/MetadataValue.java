/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */

package com.intellij.spring.impl.ide.model.xml.beans;

import consulo.xml.dom.Convert;
import consulo.xml.dom.GenericAttributeValue;

/**
 * @author peter
 */
@Convert(MetadataPropertyValueConverter.class)
public interface MetadataValue extends GenericAttributeValue<Object>, SpringValueHolderDefinition {
}