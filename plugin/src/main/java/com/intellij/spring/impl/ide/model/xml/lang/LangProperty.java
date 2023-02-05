/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */
package com.intellij.spring.impl.ide.model.xml.lang;

import com.intellij.java.impl.psi.impl.beanProperties.BeanProperty;
import com.intellij.spring.impl.ide.constants.SpringConstants;
import com.intellij.spring.impl.ide.model.converters.BeanPropertyConverter;
import com.intellij.spring.impl.ide.model.xml.beans.SpringProperty;
import consulo.xml.util.xml.Convert;
import consulo.xml.util.xml.GenericAttributeValue;
import consulo.xml.util.xml.Namespace;

import java.util.List;

/**
 * @author peter
 */
@Namespace(SpringConstants.LANG_NAMESPACE_KEY)
public interface LangProperty extends SpringProperty {

  @Convert(value = BeanPropertyConverter.class, soft = true)
  GenericAttributeValue<List<BeanProperty>> getName();
}
