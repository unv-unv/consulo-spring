package com.intellij.spring.model.xml;

import com.intellij.jam.model.common.CommonModelElement;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.List;

/**
 * @author Dmitry Avdeev
 */
public interface SpringQualifier extends CommonModelElement {

  @Nonnull
  PsiElement getIdentifyingPsiElement();

  @Nullable
  PsiClass getQualifierType();

  @Nullable
  String getQualifierValue();

  @Nonnull
  List<? extends QualifierAttribute> getQualifierAttributes();

  @Nullable
  CommonSpringBean getQualifiedBean();
}
