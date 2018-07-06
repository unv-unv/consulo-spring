package com.intellij.spring.impl.model.util;

import com.intellij.psi.PsiType;
import com.intellij.spring.impl.model.DomSpringBeanImpl;
import com.intellij.spring.impl.model.beans.TypedCollectionImpl;
import com.intellij.spring.model.xml.util.UtilList;
import javax.annotation.Nonnull;

import java.util.List;

public abstract class UtilListImpl extends DomSpringBeanImpl implements UtilList {

  @Nonnull
  public String getClassName() {
    return "org.springframework.beans.factory.config.ListFactoryBean";
  }

  @Nonnull
  public List<? extends PsiType> getRequiredTypes() {
    return TypedCollectionImpl.getRequiredTypes(this);
  }
}
