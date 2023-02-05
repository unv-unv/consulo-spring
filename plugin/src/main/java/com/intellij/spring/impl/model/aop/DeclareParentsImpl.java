/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */

package com.intellij.spring.impl.model.aop;

import javax.annotation.Nullable;

import consulo.language.psi.PsiFile;
import com.intellij.spring.impl.ide.model.xml.aop.DeclareParents;
import consulo.xml.util.xml.DomUtil;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiManager;

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