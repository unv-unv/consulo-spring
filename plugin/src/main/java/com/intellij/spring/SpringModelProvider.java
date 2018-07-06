/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */

package com.intellij.spring;

import java.util.List;

import javax.annotation.Nonnull;

import com.intellij.openapi.extensions.ExtensionPointName;
import com.intellij.spring.facet.SpringFileSet;
import consulo.annotations.RequiredReadAction;
import consulo.spring.module.extension.SpringModuleExtension;

/**
 * @author Dmitry Avdeev
 */
public interface SpringModelProvider {

  ExtensionPointName<SpringModelProvider> EP_NAME = new ExtensionPointName<>("com.intellij.spring.modelProvider");

  @Nonnull
  @RequiredReadAction
  List<SpringFileSet> getFilesets(@Nonnull SpringModuleExtension extension);
}
