/*
 * Copyright (c) 2000-2006 JetBrains s.r.o. All Rights Reserved.
 */

package com.intellij.spring.impl.ide.model.properties;

import com.intellij.java.impl.psi.impl.beanProperties.BeanProperty;
import com.intellij.java.impl.refactoring.rename.BeanPropertyRenameHandler;
import consulo.annotation.component.ExtensionImpl;
import consulo.dataContext.DataContext;

import javax.annotation.Nullable;

/**
 * @author Dmitry Avdeev
 */
@ExtensionImpl
public class SpringPropertyRenameHandler extends BeanPropertyRenameHandler {
  @Nullable
  protected BeanProperty getProperty(DataContext context) {
    return SpringPropertiesUtil.getBeanProperty(context);
  }
}
