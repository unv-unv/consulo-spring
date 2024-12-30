/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */

package com.intellij.spring.impl.ide.factories.resolvers;

import com.intellij.java.language.psi.JavaPsiFacade;
import com.intellij.java.language.psi.PsiClass;
import com.intellij.java.language.psi.PsiClassType;
import com.intellij.spring.impl.ide.SpringModel;
import com.intellij.spring.impl.ide.factories.ObjectTypeResolver;
import com.intellij.spring.impl.ide.factories.SpringFactoryBeansManager;
import com.intellij.spring.impl.ide.model.SpringUtils;
import com.intellij.spring.impl.ide.model.xml.CommonSpringBean;
import com.intellij.spring.impl.ide.model.xml.beans.SpringBean;
import com.intellij.spring.impl.ide.model.xml.beans.SpringBeanPointer;
import com.intellij.spring.impl.ide.model.xml.beans.SpringProperty;
import com.intellij.spring.impl.ide.model.xml.beans.SpringPropertyDefinition;
import consulo.language.psi.PsiManager;
import consulo.util.lang.StringUtil;
import consulo.xml.util.xml.DomUtil;
import consulo.xml.util.xml.converters.values.BooleanValueConverter;
import org.jetbrains.annotations.NonNls;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Serega Vasiliev, Taras Tielkes
 */
public abstract class AbstractTypeResolver implements ObjectTypeResolver {
  @NonNls private static final String CLASS_ARRAY_EDITOR_SEPARATOR = ",";

  @Nullable
  protected static String getPropertyValue(@Nonnull final CommonSpringBean bean, @Nonnull final String propertyName) {
    if (bean instanceof SpringBean) {
      final SpringPropertyDefinition property = SpringUtils.findPropertyByName((SpringBean)bean, propertyName);
      if (property != null) {
        final String value = SpringUtils.getStringPropertyValue(property);
        if (value != null) return value;
      }
    }
    return null;
  }

  @Nonnull
  protected static Set<String> getListOrSetValues(@Nonnull final SpringBean bean, @Nonnull String propertyName) {
    final SpringPropertyDefinition property = SpringUtils.findPropertyByName(bean, propertyName);
    if (property != null) {
      return SpringUtils.getListOrSetValues(property);
    }
    return Collections.emptySet();
  }

  // @see org.springframework.beans.propertyeditors.ClassArrayEditor.setAsText(String text)
  @Nonnull
  protected static Set<String> getTypesFromClassArrayProperty(@Nonnull final SpringBean context, final String propertyName) {
    final SpringPropertyDefinition property = SpringUtils.findPropertyByName(context, propertyName);
    if (property != null) {
      final String stringValue = SpringUtils.getStringPropertyValue(property);
      if (stringValue != null) {
        return splitAndTrim(stringValue, CLASS_ARRAY_EDITOR_SEPARATOR);
      } else {
        return SpringUtils.getListOrSetValues(property);
      }
    }
    return Collections.emptySet();
  }

  @Nonnull
  private static Set<String> splitAndTrim(@Nonnull String value, @Nonnull String separator) {
    final List<String> parts = StringUtil.split(value, separator);
    final Set<String> trimmedParts = new HashSet<String>(parts.size());
    for (String part : parts) {
      trimmedParts.add(part.trim());
    }
    return trimmedParts;
  }

  protected static boolean isBooleanProperySetAndTrue(@Nonnull final SpringBean context, @Nonnull final String propertyName) {
    final String value = getPropertyValue(context, propertyName);
    return value != null && BooleanValueConverter.getInstance(true).isTrue(value);
  }

  protected static boolean isBooleanProperySetAndFalse(@Nonnull final SpringBean context, @Nonnull final String propertyName) {
    final String value = getPropertyValue(context, propertyName);
    return value != null && !BooleanValueConverter.getInstance(true).isTrue(value);
  }

  @Nullable
  protected static PsiClassType getTypeFromProperty(@Nonnull final SpringBean context, @Nonnull final String propertyName) {
    final SpringPropertyDefinition targetProperty = SpringUtils.findPropertyByName(context, propertyName);

    if (targetProperty != null) {
      if (targetProperty instanceof SpringProperty) {
        // support chained FactoryBean resolving only for inner beans
        final SpringProperty property = (SpringProperty)targetProperty;
        final SpringBean bean = property.getBean();
        if (DomUtil.hasXml(bean)) {
          final PsiClass[] classes = SpringUtils.getEffectiveBeanTypes(bean);
          final PsiManager psiManager = bean.getPsiManager();
          if (classes.length > 0 && psiManager != null) {
            return JavaPsiFacade.getInstance(psiManager.getProject()).getElementFactory().createType(classes[0]);
          }
        }
      }
      return getTypeFromNonFactoryBean(SpringUtils.getReferencedSpringBean(targetProperty));
    }
    return null;
  }

  @Nullable
  protected static PsiClassType getTypeFromBeanName(@Nonnull SpringBean context, @Nonnull String beanName) {
    final SpringModel model = SpringUtils.getSpringModel(context);
    return getTypeFromNonFactoryBean(model.findBean(beanName));
  }

  @Nullable
  private static PsiClassType getTypeFromNonFactoryBean(@Nullable final SpringBeanPointer bean) {
    // chained FactoryBean resolving is not supported for top-level beans (to avoid circularity handling)
    if (bean != null) {
      final PsiClass targetBeanClass = bean.getBeanClass();
      if (targetBeanClass != null && !SpringFactoryBeansManager.isBeanFactory(targetBeanClass)) {
        final PsiManager psiManager = bean.getPsiManager();
        if (psiManager != null) {
          return JavaPsiFacade.getInstance(psiManager.getProject()).getElementFactory().createType(targetBeanClass);
        }
      }
    }
    return null;
  }
}
