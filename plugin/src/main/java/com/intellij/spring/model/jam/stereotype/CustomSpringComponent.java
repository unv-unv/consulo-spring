package com.intellij.spring.model.jam.stereotype;

import javax.annotation.Nonnull;

import com.intellij.psi.PsiClass;

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