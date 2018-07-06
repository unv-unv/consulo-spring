package com.intellij.aop.psi;

import javax.annotation.Nonnull;

import com.intellij.lang.findUsages.FindUsagesProvider;
import com.intellij.lang.cacheBuilder.WordsScanner;
import com.intellij.psi.PsiElement;
import javax.annotation.Nullable;

public class AopPointcutExpressionFindUsagesProvider implements FindUsagesProvider {
  @Nullable
  public WordsScanner getWordsScanner() {
    return null;
  }

  public boolean canFindUsagesFor(@Nonnull final PsiElement psiElement) {
    return false;
  }

  @Nullable
  public String getHelpId(@Nonnull final PsiElement psiElement) {
    return null;
  }

  @Nonnull
  public String getType(@Nonnull final PsiElement element) {
    throw new UnsupportedOperationException("Method getType is not yet implemented in " + getClass().getName());
  }

  @Nonnull
  public String getDescriptiveName(@Nonnull final PsiElement element) {
    throw new UnsupportedOperationException("Method getDescriptiveName is not yet implemented in " + getClass().getName() + "; element=" + element + " of class=" + element.getClass());
  }

  @Nonnull
  public String getNodeText(@Nonnull final PsiElement element, final boolean useFullName) {
    throw new UnsupportedOperationException("Method getNodeText is not yet implemented in " + getClass().getName());
  }
}