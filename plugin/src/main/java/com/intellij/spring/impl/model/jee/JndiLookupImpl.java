package com.intellij.spring.impl.model.jee;

import javax.annotation.Nonnull;

import com.intellij.spring.impl.model.DomSpringBeanImpl;
import com.intellij.spring.model.xml.jee.JndiLookup;

public abstract class JndiLookupImpl extends DomSpringBeanImpl implements JndiLookup {

  @Nonnull
  public String getClassName() {
    return "org.springframework.jndi.JndiObjectFactoryBean";
  }
}