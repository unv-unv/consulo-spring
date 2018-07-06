/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */
package com.intellij.spring.impl.model.tx;

import com.intellij.spring.impl.model.DomSpringBeanImpl;
import com.intellij.spring.model.xml.tx.Advice;
import javax.annotation.Nonnull;

/**
 * @author peter
 */
public abstract class TxAdviceImpl extends DomSpringBeanImpl implements Advice {

  @Nonnull
  public String getClassName() {
    return "org.springframework.transaction.interceptor.TransactionInterceptor";
  }

}
