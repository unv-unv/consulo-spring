package com.intellij.spring.impl.model.jee;

import javax.annotation.Nonnull;

import com.intellij.spring.impl.model.DomSpringBeanImpl;
import com.intellij.spring.impl.ide.model.xml.jee.RemoteSlsb;

public abstract class RemoteSlsbImpl extends DomSpringBeanImpl implements RemoteSlsb {

  @Nonnull
  public String getClassName() {
    return "org.springframework.ejb.access.SimpleRemoteStatelessSessionProxyFactoryBean";
  }
}