package com.intellij.spring.impl.ide.model.actions.patterns.frameworks.ui;

import consulo.java.ex.facet.LibraryInfo;
import consulo.module.Module;

public class LibrariesInfo {
    private LibraryInfo[] myLibs;
  private Module myModule;
  private String myName;

  public LibrariesInfo(final LibraryInfo[] libs, final Module module, final String name) {
    myLibs = libs;
    myModule = module;
    myName = name;
  }

  public LibraryInfo[] getLibs() {
    return myLibs;
  }

  public void setLibs(final LibraryInfo[] libs) {
    myLibs = libs;
  }

  public consulo.module.Module getModule() {
    return myModule;
  }

  public void setModule(final consulo.module.Module module) {
    myModule = module;
  }

  public String getName() {
    return myName;
  }

  public void setName(final String name) {
    myName = name;
  }
}
