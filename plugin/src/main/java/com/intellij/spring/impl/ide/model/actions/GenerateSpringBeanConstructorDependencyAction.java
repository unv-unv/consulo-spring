package com.intellij.spring.impl.ide.model.actions;

import consulo.annotation.component.ActionImpl;
import consulo.spring.localize.SpringLocalize;

@ActionImpl(id = "Spring.Beans.Generate.Constructor.Dependency.Action")
public class GenerateSpringBeanConstructorDependencyAction extends GenerateSpringBeanDependencyAction {
    public GenerateSpringBeanConstructorDependencyAction() {
        super(
            new GenerateSpringBeanDependenciesActionHandler(false),
            SpringLocalize.actionSpringBeansGenerateConstructorDependencyActionText()
        );
    }
}
