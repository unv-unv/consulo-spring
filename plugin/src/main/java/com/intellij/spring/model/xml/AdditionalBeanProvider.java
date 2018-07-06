/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */
package com.intellij.spring.model.xml;

import com.intellij.openapi.module.Module;
import com.intellij.util.xml.DomElement;
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
