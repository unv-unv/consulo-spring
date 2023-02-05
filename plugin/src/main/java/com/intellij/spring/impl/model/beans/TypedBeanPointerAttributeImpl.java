/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */

package com.intellij.spring.impl.model.beans;

import com.intellij.java.language.psi.PsiClassType;
import com.intellij.java.language.psi.PsiType;
import com.intellij.spring.impl.ide.model.converters.SpringBeanResolveConverter;
import com.intellij.spring.impl.ide.model.xml.beans.SpringBeanPointer;
import com.intellij.spring.impl.ide.model.xml.beans.TypedBeanPointerAttribute;
import consulo.xml.util.xml.AbstractConvertContext;
import consulo.xml.util.xml.DomElement;
import consulo.xml.util.xml.GenericDomValue;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

/**
 * @author peter
 */
public abstract class TypedBeanPointerAttributeImpl implements TypedBeanPointerAttribute {

  @Nonnull
  public List<? extends PsiType> getRequiredTypes() {
    final List<PsiClassType> list = ((SpringBeanResolveConverter)getConverter()).getRequiredClasses(new AbstractConvertContext() {
      @Nonnull
      public DomElement getInvocationElement() {
        return TypedBeanPointerAttributeImpl.this;
      }
    });
    return list == null ? Collections.<PsiType>emptyList() : list;
  }

  @Nonnull
  public GenericDomValue<SpringBeanPointer> getRefElement() {
    return this;
  }

  public GenericDomValue<?> getValueElement() {
    return null;
  }
}