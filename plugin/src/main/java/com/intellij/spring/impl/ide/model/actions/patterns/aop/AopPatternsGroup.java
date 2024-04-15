package com.intellij.spring.impl.ide.model.actions.patterns.aop;

import com.intellij.spring.impl.ide.SpringBundle;
import com.intellij.spring.impl.ide.model.actions.GenerateSpringDomElementAction;
import com.intellij.spring.impl.ide.model.actions.generate.SpringBeanGenerateProvider;
import consulo.aop.icon.AopIconGroup;
import consulo.ui.ex.action.AnActionEvent;
import consulo.ui.ex.action.DefaultActionGroup;

public class AopPatternsGroup extends DefaultActionGroup {

  public AopPatternsGroup() {
    add(new GenerateSpringDomElementAction(new SpringBeanGenerateProvider(SpringBundle.message("spring.patterns.aop.auto.proxy.creator"),
                                                                          "aop-auto-proxy-creator"),
                                           AopIconGroup.gutterIntroduction()));
    add(new GenerateSpringDomElementAction(new SpringBeanGenerateProvider(SpringBundle.message("spring.patterns.aop.auto.proxy"),
                                                                          "aop-auto-proxy"),
                                           AopIconGroup.gutterIntroduction()));

    setPopup(true);
  }

  public void update(final AnActionEvent e) {
    super.update(e);
    e.getPresentation().setText(SpringBundle.message("spring.patterns.aop.group.name"));
    e.getPresentation().setIcon(AopIconGroup.gutterIntroduction());
  }

}