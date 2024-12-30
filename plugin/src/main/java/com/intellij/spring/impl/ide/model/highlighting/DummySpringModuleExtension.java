package com.intellij.spring.impl.ide.model.highlighting;

import com.intellij.spring.impl.ide.facet.SpringFileSet;
import consulo.annotation.access.RequiredReadAction;
import consulo.module.Module;
import consulo.project.Project;
import consulo.spring.impl.module.extension.SpringModuleExtension;
import org.jdom.Element;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author VISTALL
 * @since 14-Jan-17
 */
class DummySpringModuleExtension implements SpringModuleExtension {
  private consulo.module.Module myModule;

  public DummySpringModuleExtension(@Nonnull consulo.module.Module module) {
    myModule = module;
  }

  @Nonnull
  @Override
  public Set<SpringFileSet> getFileSets() {
    return new LinkedHashSet<>();
  }

  @Override
  public void dispose() {

  }

  @Nonnull
  @Override
  public String getId() {
    return "spring";
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  @Nonnull
  @Override
  public Module getModule() {
    return myModule;
  }

  @Nonnull
  @Override
  public Project getProject() {
    return myModule.getProject();
  }

  @RequiredReadAction
  @Override
  public void commit(@Nonnull SpringModuleExtension springModuleExtension) {

  }

  @Nullable
  @Override
  public Element getState() {
    return null;
  }

  @Override
  public void loadState(Element element) {

  }
}
