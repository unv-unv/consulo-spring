/*
 * Copyright (c) 2000-2006 JetBrains s.r.o. All Rights Reserved.
 */

/*
 * Created by IntelliJ IDEA.
 * User: Sergey.Vasiliev
 * Date: Nov 10, 2006
 * Time: 3:23:47 PM
 */
package com.intellij.spring.impl.ide.model.converters;

import com.intellij.java.impl.codeInsight.daemon.impl.quickfix.CreateMethodQuickFix;
import com.intellij.java.language.psi.CommonClassNames;
import com.intellij.java.language.psi.PsiClass;
import com.intellij.java.language.psi.PsiMethod;
import com.intellij.java.language.psi.PsiModifier;
import com.intellij.spring.impl.ide.model.xml.DomSpringBean;
import com.intellij.spring.impl.ide.model.xml.beans.SpringBean;
import consulo.language.editor.inspection.LocalQuickFix;
import consulo.xml.util.xml.ConvertContext;
import consulo.xml.util.xml.GenericDomValue;
import org.jetbrains.annotations.NonNls;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public class SpringBeanMethodConverter extends PsiMethodConverter {

  protected MethodAccepter getMethodAccepter(final ConvertContext context, final boolean forCompletion) {
    return new MethodAccepter() {

      public boolean accept(PsiMethod method) {
        final String containing = method.getContainingClass().getQualifiedName();
        return checkParameterList(method) && checkModifiers(method) && checkReturnType(context, method, forCompletion) &&
               !method.isConstructor() && containing != null && !containing.equals(CommonClassNames.JAVA_LANG_OBJECT);
      }
    };
  }

  protected boolean checkModifiers(final PsiMethod method) {
    return method.hasModifierProperty(PsiModifier.PUBLIC) && !method.hasModifierProperty(PsiModifier.ABSTRACT);
  }

  protected boolean checkParameterList(final PsiMethod method) {
    return method.getParameterList().getParametersCount() == 0;
  }

  protected boolean checkReturnType(final ConvertContext context, final PsiMethod method, final boolean forCompletion) {
    return true;
  }

  @Nullable
  protected PsiClass getPsiClass(final ConvertContext context) {
    final SpringBean springBean = context.getInvocationElement().getParentOfType(SpringBean.class, false);
    return springBean != null ? springBean.getBeanClass() : null;
  }

  public LocalQuickFix[] getQuickFixes(final ConvertContext context) {
    final DomSpringBean springBean = SpringConverterUtil.getCurrentBean(context);
    if (springBean != null) {
      final GenericDomValue element = (GenericDomValue)context.getInvocationElement();

      final String elementName = element.getStringValue();
      final PsiClass beanClass = springBean.getBeanClass();
      if (elementName != null && elementName.length() > 0 && beanClass != null) {
        LocalQuickFix fix = createNewMethodQuickFix(beanClass, elementName, context);

        return fix == null ? LocalQuickFix.EMPTY_ARRAY : new LocalQuickFix[] {fix};
      }
    }

    return LocalQuickFix.EMPTY_ARRAY;
  }

  @Nullable
  private static LocalQuickFix createNewMethodQuickFix(@Nonnull final PsiClass beanClass,
																						  final String elementName,
																						  final ConvertContext context) {
    return CreateMethodQuickFix.createFix(beanClass, getNewMethodSignature(elementName, context), getNewMethodBody(elementName, context));
  }

  @NonNls
  protected static String getNewMethodSignature(final String elementName, final ConvertContext context) {
    return "public void " + elementName + "()";
  }

  @NonNls
  protected static String getNewMethodBody(final String elementName, final ConvertContext context) {
    return "";
  }

}