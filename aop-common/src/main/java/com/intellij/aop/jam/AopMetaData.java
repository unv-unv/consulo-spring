/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */
package com.intellij.aop.jam;

import com.intellij.java.language.psi.PsiAnnotation;
import com.intellij.java.language.psi.PsiMethod;
import consulo.language.psi.PsiElement;
import consulo.language.psi.meta.PsiMetaData;
import org.jetbrains.annotations.NonNls;

/**
 * @author peter
 */
public class AopMetaData implements PsiMetaData {
  private PsiAnnotation myAnnotation;

  public PsiElement getDeclaration() {
    return myAnnotation;
  }

  @NonNls
  public String getName(PsiElement context) {
    return getName();
  }

  @NonNls
  public final String getName() {
    return ((PsiMethod)myAnnotation.getParent().getParent()).getName();
  }

  public void init(PsiElement element) {
    myAnnotation = (PsiAnnotation) element;
  }

  public Object[] getDependences() {
    return new Object[]{myAnnotation};
  }
}
