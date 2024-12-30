package com.intellij.spring.impl.model;

import jakarta.annotation.Nonnull;

import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiManager;
import com.intellij.spring.impl.ide.model.xml.DomSpringBean;
import consulo.xml.util.xml.DomUtil;
import consulo.language.psi.PsiFile;
import consulo.util.collection.ArrayUtil;

import jakarta.annotation.Nullable;

@SuppressWarnings({"AbstractClassNeverImplemented"})
public abstract class DomSpringBeanImpl extends AbstractDomSpringBean implements DomSpringBean {

  @Nullable
  public String getBeanName() {
    return getId().getStringValue();
  }

  public void setName(@Nonnull String newName) {
    if (getBeanName() != null) {
      getId().setStringValue(newName);
    }
  }

  @Nullable
  public PsiFile getContainingFile() {
    return DomUtil.getFile(this);
  }

  @Nullable
  public PsiElement getIdentifyingPsiElement() {
    return getXmlElement();
  }

  @Nonnull
  public String[] getAliases() {
    return ArrayUtil.EMPTY_STRING_ARRAY;
  }

  public PsiManager getPsiManager() {
    return PsiManager.getInstance(getManager().getProject());
  }

}
