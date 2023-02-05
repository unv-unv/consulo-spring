package com.intellij.spring.impl.ide.model.actions.patterns.frameworks.util;

import consulo.util.xml.serializer.annotation.Tag;
import consulo.util.xml.serializer.annotation.AbstractCollection;

public class LibrariesConfigurationInfo {

  @Tag("libraries")
  @AbstractCollection(surroundWithTag = false)
  public LibraryConfigurationInfo[] myInfos;

  public LibraryConfigurationInfo[] getLibraryConfigurationInfos() {
    return myInfos;
  }
}
