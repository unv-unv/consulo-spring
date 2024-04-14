package com.intellij.spring.impl.ide.facet;

import consulo.disposer.Disposable;
import consulo.spring.impl.icon.SpringImplIconGroup;
import consulo.ui.image.Image;

import javax.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 2024-04-14
 */
public class XmlSpringFileSet extends SpringFileSet {
  public XmlSpringFileSet(@Nonnull String id, @Nonnull String name, @Nonnull Disposable parent) {
    super(id, name, parent);
  }

  public XmlSpringFileSet(SpringFileSet original) {
    super(original);
  }

  @Nonnull
  @Override
  public String getType() {
    return SpringFileSetFactory.XML;
  }

  @Override
  public Image getIcon() {
    return SpringImplIconGroup.springconfig();
  }
}
