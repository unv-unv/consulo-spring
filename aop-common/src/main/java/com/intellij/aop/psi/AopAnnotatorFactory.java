package com.intellij.aop.psi;

import consulo.annotation.component.ExtensionImpl;
import consulo.language.Language;
import consulo.language.editor.annotation.Annotator;
import consulo.language.editor.annotation.AnnotatorFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author VISTALL
 * @since 05/02/2023
 */
@ExtensionImpl
public class AopAnnotatorFactory implements AnnotatorFactory {
  @Nullable
  @Override
  public Annotator createAnnotator() {
    return new AopAnnotator();
  }

  @Nonnull
  @Override
  public Language getLanguage() {
    return AopPointcutExpressionLanguage.INSTANCE;
  }
}
