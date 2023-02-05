/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */

package com.intellij.spring.impl.ide.model.xml.aop;

import com.intellij.java.impl.util.xml.DomJavaUtil;
import com.intellij.java.language.psi.JavaPsiFacade;
import com.intellij.java.language.psi.PsiClass;
import com.intellij.java.language.psi.PsiClassType;
import com.intellij.spring.impl.ide.model.converters.SpringBeanResolveConverter;
import consulo.xml.util.xml.ConvertContext;
import consulo.xml.util.xml.DomElement;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public class TypedBeanResolveConverter extends SpringBeanResolveConverter {

  @Nullable
  public List<PsiClassType> getRequiredClasses(final ConvertContext context) {
    final DomElement element = context.getInvocationElement();
    final RequiredBeanType type = element.getAnnotation(RequiredBeanType.class);
    assert type != null;
    final PsiClass aClass = DomJavaUtil.findClass(type.value(), context.getFile(), context.getModule(), null);
    return aClass == null ? null : Arrays.asList(JavaPsiFacade.getInstance(context.getFile().getProject()).getElementFactory().createType(aClass));
  }
}
