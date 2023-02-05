/*
 * Copyright (c) 2000-2006 JetBrains s.r.o. All Rights Reserved.
 */

package com.intellij.spring.impl.ide.model.highlighting;

import com.intellij.java.language.psi.JavaPsiFacade;
import com.intellij.java.language.psi.PsiNameHelper;
import com.intellij.spring.impl.ide.SpringBundle;
import com.intellij.spring.impl.ide.SpringModel;
import com.intellij.spring.impl.ide.model.values.converters.FieldRetrievingFactoryBeanConverter;
import com.intellij.spring.impl.ide.model.xml.beans.Alias;
import com.intellij.spring.impl.ide.model.xml.beans.Beans;
import com.intellij.spring.impl.ide.model.xml.beans.SpringBean;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.editor.rawHighlight.HighlightDisplayLevel;
import consulo.util.lang.StringUtil;
import consulo.xml.util.xml.DomElement;
import consulo.xml.util.xml.highlighting.DomElementAnnotationHolder;
import org.jetbrains.annotations.NonNls;

import javax.annotation.Nonnull;

@ExtensionImpl
public class SpringBeanNameConventionInspection extends SpringBeanInspectionBase {

  @Nonnull
  public String getDisplayName() {
    return SpringBundle.message("model.inspection.display.bean.name.convention");
  }

  @Nonnull
  @NonNls
  public String getShortName() {
    return "SpringBeanNameConventionInspection";
  }

  protected void checkBean(SpringBean springBean, final Beans beans, final DomElementAnnotationHolder holder, final SpringModel springModel) {
    final String beanId = springBean.getId().getStringValue();

    if (acceptBean(springBean, beanId)) {
      checkName(springBean.getId(), beanId, holder);
    }
  }

  private static boolean acceptBean(final SpringBean springBean, final String beanId) {
    return !StringUtil.isEmpty(beanId) && (!FieldRetrievingFactoryBeanConverter.isFieldRetrivingFactoryBean(springBean) ||
                                           !FieldRetrievingFactoryBeanConverter.isResolved(springBean.getManager().getProject(), beanId));

  }

  @Nonnull
  public HighlightDisplayLevel getDefaultLevel() {
    return HighlightDisplayLevel.WARNING;
  }

  private static void checkAlias(final Alias alias, final DomElementAnnotationHolder holder) {
    final String aliasName = alias.getAlias().getStringValue();

    checkName(alias.getAlias(), aliasName, holder);
  }

  private static void checkName(final DomElement domElement, @Nonnull final String name, final DomElementAnnotationHolder holder) {
    PsiNameHelper psiNameHelper = JavaPsiFacade.getInstance(domElement.getManager().getProject()).getNameHelper();
    final boolean identifier = psiNameHelper.isIdentifier(name);
    if (!identifier) {
      boolean keyword = psiNameHelper.isKeyword(name); // IDEADEV-15506
      if(!keyword) {
        holder.createProblem(domElement, SpringBundle.message("model.inspection.invalid.identifier.message", name));
      }
    }

    if (Character.isUpperCase(name.charAt(0))) {
      holder.createProblem(domElement, SpringBundle.message("model.inspection.invalid.lowercase.name.message", name));
    }
  }
}