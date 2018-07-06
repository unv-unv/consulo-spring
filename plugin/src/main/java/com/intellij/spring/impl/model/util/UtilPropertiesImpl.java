package com.intellij.spring.impl.model.util;

import com.intellij.spring.impl.model.DomSpringBeanImpl;
import com.intellij.spring.model.xml.util.UtilProperties;
import javax.annotation.Nonnull;

public abstract class UtilPropertiesImpl extends DomSpringBeanImpl implements UtilProperties {
  @Nonnull
  public String getClassName() {
    return BEAN_CLASS_NAME;
  }
}
