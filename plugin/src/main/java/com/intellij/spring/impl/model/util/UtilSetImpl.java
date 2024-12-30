package com.intellij.spring.impl.model.util;

import com.intellij.java.language.psi.PsiType;
import com.intellij.spring.impl.model.DomSpringBeanImpl;
import com.intellij.spring.impl.model.beans.TypedCollectionImpl;
import com.intellij.spring.impl.ide.model.xml.util.UtilSet;

import jakarta.annotation.Nonnull;
import java.util.List;

public abstract class UtilSetImpl extends DomSpringBeanImpl implements UtilSet {
  @Nonnull
  public String getClassName() {
    return "org.springframework.beans.factory.config.SetFactoryBean";
  }

   @Nonnull
   public List<? extends PsiType> getRequiredTypes() {
     return TypedCollectionImpl.getRequiredTypes(this);
  }
}
