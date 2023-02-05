package com.intellij.spring.impl.ide.model.actions;

import com.intellij.spring.impl.ide.SpringBundle;

public class GenerateSpringBeanSetterDependencyAction extends GenerateSpringBeanDependencyAction {
  public GenerateSpringBeanSetterDependencyAction() {
    super(new GenerateSpringBeanDependenciesActionHandler(true),
          SpringBundle.message("action.Spring.Beans.Generate.Setter.Dependency.Action.text"));
  }
}
