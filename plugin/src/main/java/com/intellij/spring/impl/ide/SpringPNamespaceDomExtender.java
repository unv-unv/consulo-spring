/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */

package com.intellij.spring.impl.ide;

import com.intellij.java.language.psi.PsiClass;
import com.intellij.java.language.psi.util.PropertyUtil;
import com.intellij.spring.impl.ide.constants.SpringConstants;
import com.intellij.spring.impl.ide.model.xml.beans.PNamespaceRefValue;
import com.intellij.spring.impl.ide.model.xml.beans.PNamespaceValue;
import com.intellij.spring.impl.ide.model.xml.beans.SpringBean;
import consulo.annotation.component.ExtensionImpl;
import consulo.xml.psi.xml.XmlTag;
import consulo.xml.util.xml.XmlName;
import consulo.xml.util.xml.reflect.DomExtender;
import consulo.xml.util.xml.reflect.DomExtensionsRegistrar;

import jakarta.annotation.Nonnull;

/**
 * @author peter
 */
@ExtensionImpl
public class SpringPNamespaceDomExtender extends DomExtender<SpringBean> {

  @Nonnull
  @Override
  public Class<SpringBean> getElementClass() {
    return SpringBean.class;
  }

  public void registerExtensions(@Nonnull final SpringBean bean, @Nonnull final DomExtensionsRegistrar registrar) {
    final XmlTag tag = bean.getXmlTag();
    if (tag == null || tag.getPrefixByNamespace(SpringConstants.P_NAMESPACE) == null) {
      return;
    }
    final PsiClass beanClass = bean.getBeanClass();
    if (beanClass != null) {
      for (final String name : PropertyUtil.getAllProperties(beanClass, true, false).keySet()) {
        registrar.registerAttributeChildExtension(new XmlName(name, SpringConstants.P_NAMESPACE_KEY), PNamespaceValue.class);
        registrar.registerAttributeChildExtension(new XmlName(name + "-ref", SpringConstants.P_NAMESPACE_KEY), PNamespaceRefValue.class);
      }
    }
  }

}