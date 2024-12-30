package com.intellij.spring.impl.model.jee;

import jakarta.annotation.Nonnull;

import com.intellij.spring.impl.model.DomSpringBeanImpl;
import com.intellij.spring.impl.ide.model.xml.jee.LocalSlsb;

public abstract class LocalSlsbImpl extends DomSpringBeanImpl implements LocalSlsb {

  @Nonnull
  public String getClassName() {
    return "org.springframework.ejb.access.LocalStatelessSessionProxyFactoryBean";
  }
}