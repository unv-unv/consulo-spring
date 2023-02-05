package com.intellij.spring.impl.ide.model.jam.javaConfig;

import com.intellij.java.language.psi.PsiAnnotation;
import com.intellij.java.language.psi.PsiModifier;
import com.intellij.spring.impl.ide.model.jam.JamPsiMethodSpringBean;
import consulo.language.psi.PsiNamedElement;

public abstract class SpringJavaBean extends JamPsiMethodSpringBean {

  public PsiNamedElement getIdentifyingPsiElement() {
    return getPsiElement();
  }

  public abstract PsiAnnotation getPsiAnnotation();

  public boolean isPublic() {
    return getPsiElement().getModifierList().hasModifierProperty(PsiModifier.PUBLIC);
  }
}
