/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */
package com.intellij.spring.impl.ide.aop;

import com.intellij.aop.AopAdvice;
import com.intellij.aop.AopAdviceType;
import com.intellij.aop.ArgNamesManipulator;
import com.intellij.aop.jam.AopConstants;
import com.intellij.xml.util.XmlTagUtil;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiReference;
import consulo.language.util.IncorrectOperationException;
import consulo.util.lang.ObjectUtil;
import consulo.xml.psi.xml.XmlAttribute;
import consulo.xml.psi.xml.XmlAttributeValue;
import consulo.xml.psi.xml.XmlTag;
import consulo.xml.util.xml.DomElement;
import consulo.xml.util.xml.DomManager;
import org.jetbrains.annotations.NonNls;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * @author peter
 */
public class SpringArgNamesManipulator extends ArgNamesManipulator {
  private final XmlTag myTag;
  @NonNls private static final String ARG_NAMES = "arg-names";

  public SpringArgNamesManipulator(final XmlTag tag) {
    myTag = tag;
  }

  @Nullable
  public String getArgNames() {
    return myTag.getAttributeValue(ARG_NAMES);
  }

  public void setArgNames(@Nullable final String argNames) throws IncorrectOperationException
  {
    myTag.setAttribute(ARG_NAMES, argNames);
  }

  @Nonnull
  public PsiElement getArgNamesProblemElement() {
    final XmlAttribute attribute = myTag.getAttribute(ARG_NAMES);
    if (attribute != null) {
      final XmlAttributeValue xmlAttributeValue = attribute.getValueElement();
      if (xmlAttributeValue != null && !xmlAttributeValue.getValueTextRange().isEmpty()) return xmlAttributeValue;
      return attribute.getFirstChild();
    }
    return getCommonProblemElement();
  }

  @Nonnull
  public PsiElement getCommonProblemElement() {
    return ObjectUtil.assertNotNull(XmlTagUtil.getStartTagNameElement(myTag));
  }

  @Nonnull
  @NonNls
  public String getArgNamesAttributeName() {
    return ARG_NAMES;
  }

  @Nullable
  public PsiReference getReturningReference() {
    return getParameterReference(AopConstants.RETURNING_PARAM, myTag);
  }

  @Nullable
  public PsiReference getThrowingReference() {
    return getParameterReference(AopConstants.THROWING_PARAM, myTag);
  }

  public AopAdviceType getAdviceType() {
    final DomElement element = DomManager.getDomManager(myTag.getProject()).getDomElement(myTag);
    return element instanceof AopAdvice ? ((AopAdvice)element).getAdviceType() : null;
  }

  private static PsiReference getParameterReference(final String qname, final XmlTag tag) {
    final XmlAttribute attribute = tag.getAttribute(qname);
    if (attribute != null) {
      final XmlAttributeValue element = attribute.getValueElement();
      if (element != null) {
        return element.getReference();
      }
    }
    return null;
  }


}
