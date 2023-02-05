package com.intellij.spring.impl.ide.model.actions.patterns.webflow;

import consulo.ui.ex.action.DefaultActionGroup;
import com.intellij.spring.impl.ide.SpringBundle;
import com.intellij.spring.impl.ide.SpringIcons;
import com.intellij.spring.impl.ide.model.actions.GenerateSpringDomElementAction;
import com.intellij.spring.impl.ide.model.actions.generate.SpringBeanGenerateProvider;
import com.intellij.spring.impl.ide.model.actions.patterns.PatternIcons;
import consulo.ui.ex.action.AnActionEvent;

public class GenerateWebflowPatternsGroup extends DefaultActionGroup {

  public GenerateWebflowPatternsGroup() {
    add(new GenerateSpringDomElementAction(new SpringBeanGenerateProvider(SpringBundle.message("spring.patterns.webflow.registry"), "flow-registry"), SpringIcons.SPRING_BEAN_ICON)) ;
    add(new GenerateSpringDomElementAction(new SpringBeanGenerateProvider(SpringBundle.message("spring.patterns.webflow.executor"), "flow-executor"), SpringIcons.SPRING_BEAN_ICON)) ;
    add(new GenerateSpringDomElementAction(new SpringBeanGenerateProvider(SpringBundle.message("spring.patterns.webflow.builder.services"), "flow-builder-serices"), SpringIcons.SPRING_BEAN_ICON)) ;
    addSeparator();
    add(new GenerateSpringDomElementAction(new SpringBeanGenerateProvider(SpringBundle.message("spring.patterns.webflow.execution.listener"), "flow-execution-listener"), PatternIcons.FACTORY_BEAN_ICON)) ;
    addSeparator();
    add(new GenerateSpringDomElementAction(new SpringBeanGenerateProvider(SpringBundle.message("spring.patterns.webflow.services.conversion.service"), "conversation-service"), PatternIcons.FACTORY_BEAN_ICON)) ;
    add(new GenerateSpringDomElementAction(new SpringBeanGenerateProvider(SpringBundle.message("spring.patterns.webflow.services.expression.parser"), "expression-parser"), PatternIcons.FACTORY_BEAN_ICON)) ;
    add(new GenerateSpringDomElementAction(new SpringBeanGenerateProvider(SpringBundle.message("spring.patterns.webflow.services.view.factory.creator"), "factory-creator"), PatternIcons.FACTORY_BEAN_ICON)) ;
    add(new GenerateSpringDomElementAction(new SpringBeanGenerateProvider(SpringBundle.message("spring.patterns.webflow.services.view.formatter.registry"), "formatter-registry"), PatternIcons.FACTORY_BEAN_ICON)) ;

    setPopup(true);
  }

  public void update(final AnActionEvent e) {
    super.update(e);
    e.getPresentation().setText(SpringBundle.message("spring.patterns.webflow.group.name"));
    e.getPresentation().setIcon(PatternIcons.FACTORY_BEAN_ICON);
  }

}
