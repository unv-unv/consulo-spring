/*
 * Copyright (c) 2000-2006 JetBrains s.r.o. All Rights Reserved.
 */

package com.intellij.spring.impl.ide.model.highlighting;

import com.intellij.java.language.psi.PsiMethod;
import com.intellij.spring.impl.ide.SpringBundle;
import com.intellij.spring.impl.ide.SpringModel;
import com.intellij.spring.impl.ide.model.converters.SpringBeanFactoryMethodConverter;
import com.intellij.spring.impl.ide.model.xml.beans.Beans;
import com.intellij.spring.impl.ide.model.xml.beans.SpringBean;
import com.intellij.spring.impl.ide.model.xml.beans.SpringBeanPointer;
import consulo.annotation.component.ExtensionImpl;
import consulo.xml.util.xml.highlighting.DomElementAnnotationHolder;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;

import jakarta.annotation.Nonnull;

/**
 * @author Dmitry Avdeev
 */
@ExtensionImpl
public class FactoryMethodInspection extends SpringBeanInspectionBase {

  @Nls
  @Nonnull
  public String getDisplayName() {
    return SpringBundle.message("model.inspection.bean.factory.method");
  }

  @NonNls
  @Nonnull
  public String getShortName() {
    return "SpringFactoryMethodInspection";
  }

  protected void checkBean(SpringBean springBean,
                           final Beans beans,
                           final DomElementAnnotationHolder holder,
                           final SpringModel model,
                           Object state) {
    final PsiMethod factoryMethod = springBean.getFactoryMethod().getValue();
    if (factoryMethod != null) {
      if (!SpringBeanFactoryMethodConverter.isPublic(factoryMethod)) {
        holder.createProblem(springBean.getFactoryMethod(), SpringBundle.message("method.must.be.public", factoryMethod.getName()));
      }
      final boolean isStatic = SpringBeanFactoryMethodConverter.isStatic(factoryMethod);
      final SpringBeanPointer factoryBean = springBean.getFactoryBean().getValue();
      if (!isStatic && factoryBean == null) {
        holder.createProblem(springBean.getFactoryMethod(), SpringBundle.message("method.must.be.static", factoryMethod.getName()));
      } else if (isStatic && factoryBean != null) {
        holder.createProblem(springBean.getFactoryMethod(), SpringBundle.message("method.must.not.be.static", factoryMethod.getName()));
      }
      if (!SpringBeanFactoryMethodConverter.isProperReturnType(factoryMethod)) {
        holder.createProblem(springBean.getFactoryMethod(), SpringBundle.message("method.cannot.instantiate.bean", factoryMethod.getName()));
      }
    }
  }
}
