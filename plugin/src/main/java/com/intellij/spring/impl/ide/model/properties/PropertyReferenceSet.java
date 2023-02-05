/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */

package com.intellij.spring.impl.ide.model.properties;

import com.intellij.java.impl.psi.impl.beanProperties.BeanProperty;
import com.intellij.java.language.psi.PsiClass;
import com.intellij.spring.impl.ide.model.xml.CommonSpringBean;
import consulo.document.util.TextRange;
import consulo.language.psi.PsiElement;
import consulo.language.psi.ReferenceSetBase;
import consulo.xml.util.xml.ConvertContext;
import consulo.xml.util.xml.GenericDomValue;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * @author Dmitry Avdeev
 */
public class PropertyReferenceSet extends ReferenceSetBase<PropertyReference>
{

  @Nullable private final PsiClass myBeanClass;

  private final GenericDomValue<List<BeanProperty>> myGenericDomValue;
  private final ConvertContext myContext;
  private final CommonSpringBean myBean;

  public PropertyReferenceSet(@Nonnull PsiElement element,
                              @Nullable PsiClass beanClass,
                              @Nonnull GenericDomValue<List<BeanProperty>> genericDomValue,
                              ConvertContext context,
                              CommonSpringBean bean) {
    super(element);
    myBeanClass = beanClass;
    myGenericDomValue = genericDomValue;
    myContext = context;
    myBean = bean;
  }

  @Nonnull
  protected PropertyReference createReference(final TextRange range, final int index) {
    return new PropertyReference(this, range, index);
  }

  public PropertyReference[] getPsiReferences() {
    return getReferences().toArray(new PropertyReference[getReferences().size()]);
  }

  public GenericDomValue<List<BeanProperty>> getGenericDomValue() {
    return myGenericDomValue;
  }

  @Nullable
  public PsiClass getBeanClass() {
    return myBeanClass;
  }


  public ConvertContext getContext() {
    return myContext;
  }

  public CommonSpringBean getBean() {
    return myBean;
  }
}
