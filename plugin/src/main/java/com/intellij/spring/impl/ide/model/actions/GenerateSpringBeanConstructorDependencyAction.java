package com.intellij.spring.impl.ide.model.actions;

import com.intellij.spring.impl.ide.SpringBundle;

public class GenerateSpringBeanConstructorDependencyAction extends GenerateSpringBeanDependencyAction {

  public GenerateSpringBeanConstructorDependencyAction() {
    super(new GenerateSpringBeanDependenciesActionHandler(false),
          SpringBundle.message("action.Spring.Beans.Generate.Constructor.Dependency.Action.text"));
  }
}
