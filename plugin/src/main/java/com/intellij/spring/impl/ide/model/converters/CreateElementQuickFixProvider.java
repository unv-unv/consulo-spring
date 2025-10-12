/*
 * Copyright (c) 2000-2006 JetBrains s.r.o. All Rights Reserved.
 */
package com.intellij.spring.impl.ide.model.converters;

import consulo.language.editor.inspection.LocalQuickFix;
import consulo.language.editor.inspection.ProblemDescriptor;
import consulo.localize.LocalizeValue;
import consulo.project.Project;
import consulo.xml.util.xml.GenericDomValue;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * @author Dmitry Avdeev
 */
public abstract class CreateElementQuickFixProvider<T> {
    public LocalQuickFix[] getQuickFixes(final GenericDomValue<T> value) {
        final LocalQuickFix fix = getQuickFix(value);
        return fix == null ? LocalQuickFix.EMPTY_ARRAY : new LocalQuickFix[]{fix};
    }

    @Nullable
    public LocalQuickFix getQuickFix(final GenericDomValue<T> value) {
        final String elementName = getElementName(value);
        if (!isAvailable(elementName, value)) {
            return null;
        }
        final GenericDomValue<T> copy = value.createStableCopy();

        return new LocalQuickFix() {
            @Nonnull
            public LocalizeValue getName() {
                return getFixName(elementName);
            }

            public void applyFix(@Nonnull final Project project, @Nonnull final ProblemDescriptor descriptor) {
                apply(elementName, copy);
            }
        };
    }

    protected boolean isAvailable(String elementName, GenericDomValue<T> value) {
        return elementName != null && elementName.trim().length() > 0;
    }

    protected abstract void apply(String elementName, GenericDomValue<T> value);

    @Nonnull
    protected abstract LocalizeValue getFixName(String elementName);

    @Nullable
    protected String getElementName(@Nonnull final GenericDomValue<T> value) {
        return value.getStringValue();
    }
}
