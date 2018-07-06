package com.intellij.spring.model.actions;

import com.intellij.spring.SpringIcons;
import com.intellij.util.xml.ui.actions.generate.GenerateDomElementProvider;
import consulo.ui.image.Image;

public class GenerateSpringBeanBodyAction extends GenerateSpringDomElementAction {
  public GenerateSpringBeanBodyAction(final GenerateDomElementProvider provider) {
    this(provider, SpringIcons.SPRING_BEAN_PROPERTY_ICON);
  }

  public GenerateSpringBeanBodyAction(final GenerateDomElementProvider provider, Image icon) {
    super(provider, icon);
  }
}
