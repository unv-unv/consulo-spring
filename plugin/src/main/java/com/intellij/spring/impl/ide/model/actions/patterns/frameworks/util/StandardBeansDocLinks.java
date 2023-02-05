package com.intellij.spring.impl.ide.model.actions.patterns.frameworks.util;

import consulo.util.xml.serializer.annotation.AbstractCollection;
import consulo.util.xml.serializer.annotation.Tag;

public class StandardBeansDocLinks {

  @Tag("links")
  @AbstractCollection(surroundWithTag = false)
  public StandardBeansDocLink[] myLinks;

  public StandardBeansDocLink[] getDocLinks() {
    return myLinks;
  }
}
