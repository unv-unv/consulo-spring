package com.intellij.spring.impl.model.util;

import com.intellij.spring.impl.model.DomSpringBeanImpl;
import com.intellij.spring.impl.ide.model.xml.util.PropertyPath;
import javax.annotation.Nonnull;

public abstract class UtilPropertyPathImpl extends DomSpringBeanImpl implements PropertyPath {
  @Nonnull
  public String getClassName() {
    return "org.springframework.beans.factory.config.PropertyPathFactoryBean";
  }
}
