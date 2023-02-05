package com.intellij.spring.impl.ide.model.xml;

import com.intellij.jam.model.common.CommonModelElement;
import com.intellij.java.language.psi.PsiClass;
import consulo.language.psi.PsiElement;

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
