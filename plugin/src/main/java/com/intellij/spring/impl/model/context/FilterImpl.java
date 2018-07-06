package com.intellij.spring.impl.model.context;

import com.intellij.spring.impl.model.DomSpringBeanImpl;
import com.intellij.spring.model.xml.context.Filter;
import javax.annotation.Nullable;

public abstract class FilterImpl extends DomSpringBeanImpl implements Filter {

  @Nullable
  public String getClassName() {
    return null; //todo
  }
}
