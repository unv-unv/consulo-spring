package com.intellij.spring.impl.ide.model.highlighting;

import com.intellij.aop.psi.AopPointcutExpressionLanguage;
import consulo.language.Language;
import consulo.language.editor.inspection.LocalInspectionTool;
import consulo.language.editor.rawHighlight.HighlightDisplayLevel;

import consulo.localize.LocalizeValue;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * @author VISTALL
 * @since 2023-02-04
 */
public abstract class AOPLocalInspectionTool extends LocalInspectionTool {
    @Nullable
    @Override
    public Language getLanguage() {
        return AopPointcutExpressionLanguage.INSTANCE;
    }

    @Nonnull
    @Override
    public LocalizeValue getGroupDisplayName() {
        return LocalizeValue.empty();
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
