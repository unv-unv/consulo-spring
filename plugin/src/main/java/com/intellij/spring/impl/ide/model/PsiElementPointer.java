package com.intellij.spring.impl.ide.model;

import consulo.language.psi.PsiElement;

import jakarta.annotation.Nullable;

/**
 * @author Dmitry Avdeev
 */
public interface PsiElementPointer {
  @Nullable
  PsiElement getPsiElement();
}
