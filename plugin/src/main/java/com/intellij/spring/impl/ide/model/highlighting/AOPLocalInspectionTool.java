package com.intellij.spring.impl.ide.model.highlighting;

import com.intellij.aop.psi.AopPointcutExpressionLanguage;
import consulo.language.Language;
import consulo.language.editor.inspection.LocalInspectionTool;
import consulo.language.editor.rawHighlight.HighlightDisplayLevel;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * @author VISTALL
 * @since 04/02/2023
 */
public abstract class AOPLocalInspectionTool extends LocalInspectionTool {
  @Nullable
  @Override
  public Language getLanguage() {
    return AopPointcutExpressionLanguage.INSTANCE;
  }

  @Nonnull
  @Override
  public String getGroupDisplayName() {
    return "";
  }

  @Override
  public boolean isEnabledByDefault() {
    return true;
  }

  @Nonnull
  @Override
  public HighlightDisplayLevel getDefaultLevel() {
    return HighlightDisplayLevel.WARNING;
  }
}
