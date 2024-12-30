package com.intellij.spring.impl.ide.model.actions.patterns;

import com.intellij.spring.impl.ide.SpringBundle;
import com.intellij.spring.impl.ide.model.actions.patterns.aop.AopPatternsGroup;
import com.intellij.spring.impl.ide.model.actions.patterns.dataAccess.GenerateDataAccessPatternsGroup;
import com.intellij.spring.impl.ide.model.actions.patterns.factoryBeans.GenerateCommonBeansPatternsGroup;
import com.intellij.spring.impl.ide.model.actions.patterns.integration.GenerateIntegrationPatternsGroup;
import com.intellij.spring.impl.ide.model.actions.patterns.integration.GenerateSchedulersPatternsGroup;
import com.intellij.spring.impl.ide.model.actions.patterns.osgi.GenerateOsgiPatternsGroup;
import com.intellij.spring.impl.ide.model.actions.patterns.webflow.GenerateWebflowPatternsGroup;
import consulo.ui.image.Image;

import jakarta.annotation.Nullable;

public class GenerateTemplateBeansGroup extends AbstarctSpringConfigActionGroup {

  public GenerateTemplateBeansGroup() {
    super();
    add(new GenerateCommonBeansPatternsGroup());
    add(new GenerateDataAccessPatternsGroup());
    add(new GenerateIntegrationPatternsGroup());
    add(new GenerateSchedulersPatternsGroup());
    add(new AopPatternsGroup());
    add(new GenerateWebflowPatternsGroup());
    add(new GenerateOsgiPatternsGroup());
  }

  protected String getDescription() {
    return SpringBundle.message("spring.template.beans");
  }

  @Nullable
  protected Image getIcon() {
    return PatternIcons.SPRING_BEANS_ICON;
  }
}