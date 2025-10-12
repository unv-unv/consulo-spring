/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */
package com.intellij.spring.impl.ide.model.highlighting;

import com.intellij.spring.impl.ide.model.xml.beans.Beans;
import com.intellij.spring.impl.ide.model.xml.beans.SpringBeanScope;
import consulo.annotation.component.ExtensionImpl;
import consulo.localize.LocalizeValue;
import consulo.spring.localize.SpringLocalize;
import consulo.xml.util.xml.DomElement;
import consulo.xml.util.xml.DomUtil;
import consulo.xml.util.xml.GenericDomValue;
import consulo.xml.util.xml.highlighting.BasicDomElementsInspection;
import consulo.xml.util.xml.highlighting.DomElementAnnotationHolder;
import consulo.xml.util.xml.highlighting.DomHighlightingHelper;
import jakarta.annotation.Nonnull;

/**
 * @author peter
 */
@ExtensionImpl
public class SpringScopesInspection extends BasicDomElementsInspection<Beans, Object> {
    protected void checkDomElement(final DomElement element, final DomElementAnnotationHolder holder, final DomHighlightingHelper helper) {
        if (element instanceof GenericDomValue) {
            GenericDomValue value = (GenericDomValue) element;
            if (SpringBeanScope.class.equals(DomUtil.getGenericValueParameter(value.getDomElementType()))) {
                helper.checkResolveProblems(value, holder);
            }
        }
    }

    public SpringScopesInspection() {
        super(Beans.class);
    }

    @Nonnull
    @Override
    public LocalizeValue getGroupDisplayName() {
        return SpringLocalize.modelInspectionGroupName();
    }

    @Nonnull
    @Override
    public LocalizeValue getDisplayName() {
        return SpringLocalize.scopesInspectionDisplayName();
    }

    @Nonnull
    @Override
    public String getShortName() {
        return "SpringScopesInspection";
    }
}
