/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */

package com.intellij.spring.factories.resolvers;

import com.intellij.spring.factories.ObjectTypeResolver;
import com.intellij.spring.model.xml.CommonSpringBean;
import com.intellij.openapi.util.text.StringUtil;
import javax.annotation.Nonnull;

import java.util.HashSet;
import java.util.Set;

public class SingleObjectTypeResolver implements ObjectTypeResolver {

  private final Set<String> myTypes;

  public SingleObjectTypeResolver(final String type) {
    myTypes = new HashSet<String>(StringUtil.split(type, ","));
  }

  @Nonnull
  public Set<String> getObjectType(@Nonnull final CommonSpringBean bean) {
    return myTypes;
  }

  public boolean accept(@Nonnull final String factoryClassName) {
    return true;
  }
}