/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */
package com.intellij.spring.impl.ide.model.xml;

import com.intellij.spring.impl.ide.model.xml.beans.SpringProperty;
import jakarta.annotation.Nonnull;

import java.util.List;

/**
 * @author peter
 */
public interface CustomBeanWrapper extends DomSpringBean {

  @Nonnull
  List<CustomBean> getCustomBeans();

  /**
   * @return whether no result was obtained from custom namespace handler, or the handler itself hasn't been found
   */
  boolean isDummy();

  boolean isParsed();

  List<SpringProperty> getProperties();

  //List<ConstructorArg> getConstructorArgs();
}
