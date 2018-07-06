/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */

package com.intellij.spring.impl.model.aop;

import javax.annotation.Nullable;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.spring.model.xml.aop.DeclareParents;
import com.intellij.util.xml.DomUtil;

/**
 * @author peter
 */
public abstract class DeclareParentsImpl implements DeclareParents {

  @Nullable
  public PsiFile getContainingFile() {
    return DomUtil.getFile(this);
  }

  @Nullable
  public PsiElement getIdentifyingPsiElement() {
    return getXmlTag();
  }

  public PsiManager getPsiManager() {
    return PsiManager.getInstance(getManager().getProject());
  }
}