package com.intellij.spring.impl.ide.model.actions.generate;

import com.intellij.spring.impl.ide.model.xml.beans.SpringBean;
import consulo.xml.util.xml.DomElement;
import org.jetbrains.annotations.NonNls;
import javax.annotation.Nullable;

public class SpringBeanGenerateProvider extends BasicSpringDomGenerateProvider<SpringBean> {

  public SpringBeanGenerateProvider(final String description, @NonNls String template) {
    super(description, SpringBean.class, template);
  }

  @Nullable
  protected DomElement getElementToNavigate(final SpringBean springBean) {
    return null;
  }
}
