/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */

package com.intellij.spring.impl.model.beans;

import com.intellij.java.language.psi.*;
import com.intellij.spring.impl.ide.factories.SpringFactoryBeansManager;
import com.intellij.spring.impl.ide.model.ResolvedConstructorArgs;
import com.intellij.spring.impl.ide.model.SpringUtils;
import com.intellij.spring.impl.ide.model.converters.ConstructorArgIndexConverter;
import com.intellij.spring.impl.ide.model.converters.SpringConverterUtil;
import com.intellij.spring.impl.ide.model.xml.beans.ConstructorArg;
import com.intellij.spring.impl.ide.model.xml.beans.SpringBean;
import com.intellij.spring.impl.ide.model.xml.beans.SpringBeanPointer;
import consulo.util.lang.ComparatorUtil;
import consulo.xml.util.xml.GenericAttributeValue;

import jakarta.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

/**
 * @author Dmitry Avdeev
 */
@SuppressWarnings({"AbstractClassNeverImplemented"})
public abstract class ConstructorArgImpl extends SpringInjectionImpl implements ConstructorArg {

  @Nonnull
  public List<? extends PsiType> getRequiredTypes() {
    final PsiType type = getType().getValue();
    if (type != null) {
      return Collections.singletonList(type);
    }
    final SpringBean springBean = (SpringBean)getParent();
    assert springBean != null;
    final GenericAttributeValue<Integer> index = getIndex();
    if (index.getValue() != null) {
      final PsiParameter parameter = ConstructorArgIndexConverter.resolve(index, springBean);
      return parameter == null ? Collections.<PsiType>emptyList() : Collections.singletonList(parameter.getType());
    }
    final ResolvedConstructorArgs resolvedArgs = springBean.getResolvedConstructorArgs();
    final PsiMethod resolvedMethod = resolvedArgs.getResolvedMethod();
    if (resolvedMethod != null) {
      final PsiParameter parameter = resolvedArgs.getResolvedArgs(resolvedMethod).get(this);
      if (parameter != null) {
        return Collections.singletonList(parameter.getType());
      }
    }
    return Collections.emptyList();
  }

  public boolean isAssignable(final @Nonnull PsiType to) {
    PsiType[] types = getTypesByValue();
    if (types == null) {
      return true;
    }
    for (PsiType typeByValue : types) {
      if (to instanceof PsiClassType && typeByValue instanceof PsiClassType) {
        final PsiClass psiClass = ((PsiClassType)typeByValue).resolve();
        if (psiClass != null && SpringFactoryBeansManager.isBeanFactory(psiClass)) {
          final SpringBean springBean = (SpringBean)getParent();
          assert springBean != null;
          final PsiClass requiredClass = ((PsiClassType)to).resolve();
          final SpringBeanPointer factoryBean = SpringUtils.getReferencedSpringBean(this);
          if (requiredClass != null && factoryBean != null) {
            return SpringFactoryBeansManager.getInstance().canProduce(psiClass, requiredClass, factoryBean.getSpringBean());
          }
        }
      }
      if (
             typeByValue.equals(SpringConverterUtil.findType(String.class, getManager().getProject())) ||
             SpringConverterUtil.isConvertable(typeByValue, to, getManager().getProject())) {
        return true;
      }

    }
    return false;
  }

  public int hashCode() {
    final Integer value = getIndex().getValue();
    return value == null ? 0 : value.hashCode();
  }

  public boolean equals(final Object obj) {
    if (!(obj instanceof ConstructorArg)) return false;

    final ConstructorArg that = (ConstructorArg)obj;
    if (getXmlTag().equals(that.getXmlTag())) return true;
    
    final Integer indec = getIndex().getValue();
    return indec != null && ComparatorUtil.equalsNullable(indec, that.getIndex().getValue());
  }
}
