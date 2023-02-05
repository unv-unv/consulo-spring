package com.intellij.spring.impl.model.util;

import com.intellij.spring.impl.model.DomSpringBeanImpl;
import com.intellij.spring.impl.ide.model.xml.util.UtilMap;
import javax.annotation.Nonnull;

public abstract class UtilMapImpl extends DomSpringBeanImpl implements UtilMap {

  @Nonnull
  public String getClassName() {
    return "org.springframework.beans.factory.config.MapFactoryBean";
  }
}