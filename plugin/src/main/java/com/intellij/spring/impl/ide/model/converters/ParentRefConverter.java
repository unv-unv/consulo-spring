/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */

package com.intellij.spring.impl.ide.model.converters;

import com.intellij.spring.impl.ide.SpringModel;
import com.intellij.spring.impl.ide.model.xml.beans.SpringBeanPointer;
import consulo.xml.util.xml.ConvertContext;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.Collection;

/**
 * @author Dmitry Avdeev
*/
public class ParentRefConverter extends SpringBeanResolveConverter.PropertyBean {

  public SpringBeanPointer fromString(final @Nullable String s, final ConvertContext context) {
    if (s == null) return null;
    final SpringModel model = getSpringModel(context);
    return model == null ? null : model.findParentBean(s);
  }

  @Nonnull
  public Collection<SpringBeanPointer> getVariants(final ConvertContext context) {
    return getVariants(context, true);
  }
}
