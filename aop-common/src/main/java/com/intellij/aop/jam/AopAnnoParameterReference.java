/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */
package com.intellij.aop.jam;

import javax.annotation.Nonnull;

import com.intellij.aop.AopBundle;
import com.intellij.aop.LocalAopModel;
import com.intellij.codeInsight.daemon.EmptyResolveMessageProvider;
import com.intellij.jam.model.util.JamCommonUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ArrayUtil;

/**
 * @author peter
*/
public class AopAnnoParameterReference extends PsiReferenceBase<PsiAnnotationMemberValue> implements EmptyResolveMessageProvider {
  private final PsiMethod myMethod;

  public AopAnnoParameterReference(final PsiLiteral element) {
    super(element);
    myMethod = PsiTreeUtil.getParentOfType(element, PsiMethod.class);
  }

  public final PsiElement resolve() {
    return AopAdviceImpl.findParameter(myMethod, getCanonicalText());
  }

  public Object[] getVariants() {
    final PsiParameter[] parameters = myMethod.getParameterList().getParameters();
    return parameters.length > 0 && LocalAopModel.isJoinPointParamer(parameters[0]) ? ArrayUtil.remove(parameters, 0) : parameters;
  }

  @Nonnull
  public final String getCanonicalText() {
    return StringUtil.notNullize(JamCommonUtil.getObjectValue(getElement(), String.class));
  }

  public String getUnresolvedMessagePattern() {
    return AopBundle.message("error.cannot.resolve.parameter", getCanonicalText());
  }
}
