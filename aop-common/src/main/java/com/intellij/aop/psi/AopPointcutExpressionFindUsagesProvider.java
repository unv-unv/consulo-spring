package com.intellij.aop.psi;

import consulo.annotation.component.ExtensionImpl;
import consulo.language.Language;
import consulo.language.findUsage.FindUsagesProvider;
import consulo.language.psi.PsiElement;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

@ExtensionImpl
public class AopPointcutExpressionFindUsagesProvider implements FindUsagesProvider {
  @Override
  public boolean canFindUsagesFor(@Nonnull final PsiElement psiElement) {
    return false;
  }

  @Override
  @Nullable
  public String getHelpId(@Nonnull final PsiElement psiElement) {
    return null;
  }

  @Override
  @Nonnull
  public String getType(@Nonnull final PsiElement element) {
    throw new UnsupportedOperationException("Method getType is not yet implemented in " + getClass().getName());
  }

  @Override
  @Nonnull
  public String getDescriptiveName(@Nonnull final PsiElement element) {
    throw new UnsupportedOperationException("Method getDescriptiveName is not yet implemented in " + getClass().getName() + "; element=" + element + " of class=" + element
      .getClass());
  }

  @Override
  @Nonnull
  public String getNodeText(@Nonnull final PsiElement element, final boolean useFullName) {
    throw new UnsupportedOperationException("Method getNodeText is not yet implemented in " + getClass().getName());
  }

  @Nonnull
  @Override
  public Language getLanguage() {
    return AopPointcutExpressionLanguage.getInstance();
  }
}