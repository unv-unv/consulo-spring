package com.intellij.spring.model.highlighting;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.spring.facet.SpringFileSet;
import consulo.annotations.RequiredReadAction;
import consulo.roots.ModuleRootLayer;
import consulo.spring.module.extension.SpringModuleExtension;
import org.jdom.Element;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author VISTALL
 * @since 14-Jan-17
 */
class DummySpringModuleExtension implements SpringModuleExtension {
  private Module myModule;

  public DummySpringModuleExtension(@Nonnull Module module) {
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
  public ModuleRootLayer getModuleRootLayer() {
    throw new UnsupportedOperationException();
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
