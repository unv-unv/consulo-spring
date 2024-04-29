package com.intellij.spring.impl.ide.model.jam.javaConfig;

import com.intellij.java.language.psi.PsiClass;

import javax.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 30.04.2024
 */
public class ImportConfigurationElement extends JavaSpringConfigurationElement {
  private final PsiClass myImportClass;

  public ImportConfigurationElement(PsiClass importClass) {
    myImportClass = importClass;
  }

  @Nonnull
  @Override
  public PsiClass getPsiElement() {
    return myImportClass;
  }
}
