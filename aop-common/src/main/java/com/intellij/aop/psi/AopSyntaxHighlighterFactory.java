package com.intellij.aop.psi;

import consulo.annotation.component.ExtensionImpl;
import consulo.language.Language;
import consulo.language.editor.highlight.SingleLazyInstanceSyntaxHighlighterFactory;
import consulo.language.editor.highlight.SyntaxHighlighter;

import jakarta.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 2019-02-25
 */
@ExtensionImpl
public class AopSyntaxHighlighterFactory extends SingleLazyInstanceSyntaxHighlighterFactory {
  @Nonnull
  @Override
  protected SyntaxHighlighter createHighlighter() {
    return new AopSyntaxHighlighter();
  }

  @Nonnull
  @Override
  public Language getLanguage() {
    return AopPointcutExpressionLanguage.INSTANCE;
  }
}
