package com.intellij.spring.impl.ide.model.actions;

import consulo.annotation.component.ActionImpl;
import consulo.spring.localize.SpringLocalize;

@ActionImpl(id = "Spring.Beans.Generate.Setter.Dependency.Action")
public class GenerateSpringBeanSetterDependencyAction extends GenerateSpringBeanDependencyAction {
    public GenerateSpringBeanSetterDependencyAction() {
        super(
            new GenerateSpringBeanDependenciesActionHandler(true),
            SpringLocalize.actionSpringBeansGenerateSetterDependencyActionText()
        );
    }
}
