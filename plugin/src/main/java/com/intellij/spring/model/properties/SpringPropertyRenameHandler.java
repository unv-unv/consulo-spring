/*
 * Copyright (c) 2000-2006 JetBrains s.r.o. All Rights Reserved.
 */

package com.intellij.spring.model.properties;

import javax.annotation.Nullable;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.psi.impl.beanProperties.BeanProperty;
import com.intellij.refactoring.rename.BeanPropertyRenameHandler;

/**
 * @author Dmitry Avdeev
 */
public class SpringPropertyRenameHandler extends BeanPropertyRenameHandler {
  @Nullable
  protected BeanProperty getProperty(DataContext context) {
    return SpringPropertiesUtil.getBeanProperty(context);
  }
}
