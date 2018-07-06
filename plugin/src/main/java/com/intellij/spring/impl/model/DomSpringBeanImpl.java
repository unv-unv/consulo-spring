package com.intellij.spring.impl.model;

import javax.annotation.Nonnull;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.spring.model.xml.DomSpringBean;
import com.intellij.util.ArrayUtil;
import com.intellij.util.xml.DomUtil;

import javax.annotation.Nullable;

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
