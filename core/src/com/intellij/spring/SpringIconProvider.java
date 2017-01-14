/*
 * Copyright (c) 2000-2005 by JetBrains s.r.o. All Rights Reserved.
 * Use is subject to license terms.
 */
package com.intellij.spring;

import com.intellij.psi.PsiElement;
import com.intellij.psi.xml.XmlFile;
import consulo.annotations.RequiredReadAction;
import consulo.ide.IconDescriptor;
import consulo.ide.IconDescriptorUpdater;
import org.jetbrains.annotations.NotNull;

/**
 * @author peter
 */
public class SpringIconProvider implements IconDescriptorUpdater {
  @RequiredReadAction
  @Override
  public void updateIcon(@NotNull IconDescriptor iconDescriptor, @NotNull PsiElement element, int i) {
    if (element instanceof XmlFile && SpringManager.getInstance(element.getProject()).isSpringBeans((XmlFile) element)) {
      iconDescriptor.setMainIcon(SpringIcons.CONFIG_FILE);
    }
  }
}
