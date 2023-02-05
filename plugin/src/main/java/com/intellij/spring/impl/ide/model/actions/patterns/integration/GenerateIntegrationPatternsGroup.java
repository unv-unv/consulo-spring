package com.intellij.spring.impl.ide.model.actions.patterns.integration;

import consulo.ui.ex.action.AnActionEvent;
import consulo.ui.ex.action.DefaultActionGroup;
import com.intellij.spring.impl.ide.SpringBundle;
import com.intellij.spring.impl.ide.SpringIcons;
import com.intellij.spring.impl.ide.model.actions.GenerateSpringDomElementAction;
import com.intellij.spring.impl.ide.model.actions.generate.SpringBeanGenerateProvider;
import com.intellij.spring.impl.ide.model.actions.patterns.PatternIcons;

public class GenerateIntegrationPatternsGroup  extends DefaultActionGroup {

  public GenerateIntegrationPatternsGroup() {
    add(new GenerateSpringDomElementAction(new SpringBeanGenerateProvider(SpringBundle.message("spring.patterns.integration.ejb.local.stateles.session.bean"), "ejb-lssb"), PatternIcons.EJB_ICON)) ;
    add(new GenerateSpringDomElementAction(new SpringBeanGenerateProvider(SpringBundle.message("spring.patterns.integration.ejb.remote.stateles.session.bean"), "ejb-rssb"), PatternIcons.EJB_ICON)) ;
    add(new GenerateSpringDomElementAction(new SpringBeanGenerateProvider(SpringBundle.message("spring.patterns.integration.free.marker.factory.bean"), "free-marker"), SpringIcons.SPRING_BEAN_ICON)) ;

    setPopup(true);
  }

  public void update(final AnActionEvent e) {
    super.update(e);
    e.getPresentation().setText(SpringBundle.message("spring.patterns.integration.group.name"));
    e.getPresentation().setIcon(PatternIcons.INTEGRATION_GROUP_ICON);
  }

}