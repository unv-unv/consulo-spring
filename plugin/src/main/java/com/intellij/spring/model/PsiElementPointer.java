package com.intellij.spring.model;

import javax.annotation.Nullable;

import com.intellij.psi.PsiElement;

/**
 * @author Dmitry Avdeev
 */
public interface PsiElementPointer {
  @Nullable
  PsiElement getPsiElement();
}
