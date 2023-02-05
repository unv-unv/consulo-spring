package com.intellij.spring.impl.ide;

import com.intellij.spring.impl.ide.constants.SpringConstants;
import com.intellij.spring.impl.ide.model.xml.aop.AopConfig;
import com.intellij.spring.impl.ide.model.xml.aop.AspectjAutoproxy;
import com.intellij.spring.impl.ide.model.xml.beans.Beans;
import consulo.annotation.component.ExtensionImpl;
import consulo.xml.util.xml.XmlName;
import consulo.xml.util.xml.reflect.DomExtender;
import consulo.xml.util.xml.reflect.DomExtensionsRegistrar;

import javax.annotation.Nonnull;

@ExtensionImpl
public class BeansExtender extends DomExtender<Beans> {
  @Nonnull
  @Override
  public Class<Beans> getElementClass() {
    return Beans.class;
  }

  public void registerExtensions(@Nonnull final Beans element, @Nonnull final DomExtensionsRegistrar registrar) {
    SpringDefaultDomExtender.registerDefaultBeanExtensions(registrar);

    registrar.registerCollectionChildrenExtension(new XmlName("config", SpringConstants.AOP_NAMESPACE_KEY), AopConfig.class);
    registrar.registerFixedNumberChildExtension(new XmlName(SpringConstants.ASPECTJ_AUTOPROXY, SpringConstants.AOP_NAMESPACE_KEY),
                                                AspectjAutoproxy.class);
  }
}
