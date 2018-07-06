/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */

package com.intellij.spring.impl.model.beans;

import com.intellij.psi.PsiClassType;
import com.intellij.psi.PsiType;
import com.intellij.spring.model.converters.SpringBeanResolveConverter;
import com.intellij.spring.model.xml.beans.SpringBeanPointer;
import com.intellij.spring.model.xml.beans.TypedBeanPointerAttribute;
import com.intellij.util.xml.AbstractConvertContext;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericDomValue;
import javax.annotation.Nonnull;

import java.util.List;
import java.util.Collections;

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