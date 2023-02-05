/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */

package com.intellij.spring.impl.ide;

import com.intellij.spring.impl.ide.facet.SpringFileSet;
import consulo.annotation.access.RequiredReadAction;
import consulo.annotation.component.ComponentScope;
import consulo.annotation.component.ExtensionAPI;
import consulo.component.extension.ExtensionPointName;
import consulo.spring.impl.module.extension.SpringModuleExtension;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * @author Dmitry Avdeev
 */
@ExtensionAPI(ComponentScope.APPLICATION)
public interface SpringModelProvider {

  ExtensionPointName<SpringModelProvider> EP_NAME = ExtensionPointName.create(SpringModelProvider.class);

  @Nonnull
  @RequiredReadAction
  List<SpringFileSet> getFilesets(@Nonnull SpringModuleExtension extension);
}
