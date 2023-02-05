/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */

package com.intellij.spring.impl.ide.factories;

import consulo.util.xml.serializer.annotation.AbstractCollection;
import consulo.util.xml.serializer.annotation.Tag;

public class FactoriesBean {
  @Tag("factories")
  @AbstractCollection(surroundWithTag = false)
  public FactoryBeanInfo[] myFactories;


  public FactoryBeanInfo[] getFactories() {
    return myFactories;
  }
}