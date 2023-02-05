package com.intellij.spring.impl.ide.model.actions.patterns.frameworks;

import javax.annotation.Nullable;
import com.intellij.spring.impl.ide.SpringBundle;
import com.intellij.spring.impl.ide.model.actions.patterns.AbstarctSpringConfigActionGroup;
import com.intellij.spring.impl.ide.model.actions.patterns.PatternIcons;
import consulo.ui.image.Image;

public class FrameworkIntegrationActionGroup extends AbstarctSpringConfigActionGroup {

  public FrameworkIntegrationActionGroup() {
    super();
    add(new AddHibernateAction());
    add(new AddJdoAction());
    add(new AddToplinkAction());
    add(new AddIbatisAction());

    addSeparator();

    add(new AddWebflowAction());

    addSeparator();

    add(new AddOpenSymphonyTimerAction());
    add(new AddJdkTimerAction());
  }

  protected String getDescription() {
    return SpringBundle.message("spring.patterns");
  }

  @Nullable
  protected Image getIcon() {
    return PatternIcons.SPRING_PATTERNS_ICON;
  }
}