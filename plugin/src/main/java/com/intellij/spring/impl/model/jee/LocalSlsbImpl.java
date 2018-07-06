package com.intellij.spring.impl.model.jee;

import javax.annotation.Nonnull;

import com.intellij.spring.impl.model.DomSpringBeanImpl;
import com.intellij.spring.model.xml.jee.LocalSlsb;

public abstract class LocalSlsbImpl extends DomSpringBeanImpl implements LocalSlsb {

  @Nonnull
  public String getClassName() {
    return "org.springframework.ejb.access.LocalStatelessSessionProxyFactoryBean";
  }
}