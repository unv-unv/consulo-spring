package com.intellij.spring.impl.ide.model.jam.javaConfig;

import com.intellij.jam.JamElement;
import com.intellij.jam.annotations.JamPsiConnector;
import com.intellij.jam.reflect.JamAnnotationMeta;
import com.intellij.java.language.psi.PsiAnnotation;
import com.intellij.java.language.psi.PsiClass;

import jakarta.annotation.Nonnull;
import java.util.List;

public abstract class SpringJamElement implements JamElement {

  private final JamAnnotationMeta myMeta;

  protected SpringJamElement(@Nonnull JamAnnotationMeta annotationMeta) {
    myMeta = annotationMeta;
  }

  public PsiAnnotation getAnnotation() {
    return myMeta.getAnnotation(getPsiElement());
  }

  @Nonnull
  @JamPsiConnector
  public abstract PsiClass getPsiElement();

  public abstract List<? extends SpringJavaBean> getBeans();

  public PsiClass getPsiClass() {
    return getPsiElement();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }

    if (obj.getClass() == getClass()) {
      SpringJamElement other = (SpringJamElement)obj;

      PsiClass p1 = other.getPsiElement();
      PsiClass p2 = getPsiElement();
      return p1.isEquivalentTo(p2);
    }

    return false;
  }

  @Override
  public String toString() {
    return String.valueOf(getPsiElement());
  }
}
