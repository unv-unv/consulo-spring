package com.intellij.spring.impl.ide.model.jam.stereotype;

import com.intellij.jam.reflect.JamAnnotationMeta;
import com.intellij.java.language.psi.PsiClass;

import jakarta.annotation.Nonnull;

public class CustomSpringComponent extends SpringStereotypeElement {
  private PsiClass myPsiClass;

  public CustomSpringComponent(@Nonnull JamAnnotationMeta annotationMeta, @Nonnull PsiClass psiClass) {
    super(annotationMeta, psiClass);

    myPsiClass = psiClass;
  }

  @Override
  @Nonnull
  public PsiClass getPsiElement() {
    return myPsiClass;
  }
}