package com.intellij.spring.impl.ide;

import com.intellij.spring.impl.ide.model.xml.beans.ListOrSet;
import consulo.annotation.component.ExtensionImpl;
import consulo.xml.util.xml.reflect.DomExtender;
import consulo.xml.util.xml.reflect.DomExtensionsRegistrar;

import javax.annotation.Nonnull;

@ExtensionImpl
public class ListOrSetExtender extends DomExtender<ListOrSet> {
  @Nonnull
  @Override
  public Class<ListOrSet> getElementClass() {
    return ListOrSet.class;
  }

  public void registerExtensions(@Nonnull final ListOrSet element, @Nonnull final DomExtensionsRegistrar registrar) {
    SpringDefaultDomExtender.registerDefaultBeanExtensions(registrar);
  }
}
