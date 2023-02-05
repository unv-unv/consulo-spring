/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */

package com.intellij.spring.impl.ide.model.xml.beans;

import consulo.xml.util.xml.Convert;
import consulo.xml.util.xml.GenericAttributeValue;

/**
 * @author peter
 */
@Convert(MetadataPropertyValueConverter.class)
public interface MetadataValue extends GenericAttributeValue<Object>, SpringValueHolderDefinition {
}