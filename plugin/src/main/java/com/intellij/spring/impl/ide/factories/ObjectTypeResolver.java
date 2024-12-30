/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */

package com.intellij.spring.impl.ide.factories;

import com.intellij.spring.impl.ide.model.xml.CommonSpringBean;
import jakarta.annotation.Nonnull;

import java.util.Set;

public interface ObjectTypeResolver {

  /**
   * @param context factory bean in container
   * @return class name of the object managed by this factory, or implemented interfaces. Like as {@link org.springframework.beans.factory.FactoryBean#getObjectType()}.
   */
  @Nonnull
  Set<String> getObjectType(@Nonnull CommonSpringBean context);

  boolean accept(@Nonnull String factoryClassName);
}