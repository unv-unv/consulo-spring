package com.intellij.spring.impl.ide.model.jam.stereotype;

import com.intellij.java.language.psi.PsiClass;

import javax.annotation.Nonnull;

public class CustomSpringComponent extends SpringStereotypeElement {
  private PsiClass myPsiClass;

  public CustomSpringComponent(@Nonnull String anno, @Nonnull PsiClass psiClass) {
    super(anno, psiClass);

    myPsiClass = psiClass;
  }

  @Nonnull
  public PsiClass getPsiElement() {
    return myPsiClass;
  }
}