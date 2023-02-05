/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */
package com.intellij.spring.impl.ide.model.xml;

import consulo.module.Module;
import consulo.xml.util.xml.DomElement;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.List;

/**
 * @author peter
 */
public interface AdditionalBeanProvider<T extends DomElement> {
  @Nullable
  List<CommonSpringBean> getAdditionalBeans(@Nonnull final T t, @Nonnull Module module);
}
