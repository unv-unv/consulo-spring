/*
 * Copyright (c) 2000-2006 JetBrains s.r.o. All Rights Reserved.
 */

/*
 * Created by IntelliJ IDEA.
 * User: Sergey.Vasiliev
 * Date: Nov 15, 2006
 * Time: 1:06:27 PM
 */
package com.intellij.spring.impl.ide.model.highlighting;

import com.intellij.java.impl.util.xml.impl.ExtendsClassChecker;
import com.intellij.spring.impl.ide.SpringBundle;
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
import org.jetbrains.annotations.NonNls;

import jakarta.annotation.Nonnull;

@ExtensionImpl
public class SpringModelInspection extends BasicDomElementsInspection<Beans, Object> {
    protected boolean shouldCheckResolveProblems(final GenericDomValue value) {
        return !SpringBeanScope.class.equals(DomUtil.getGenericValueParameter(value.getDomElementType()))
            && super.shouldCheckResolveProblems(value);

    }

    public SpringModelInspection() {
        super(Beans.class);
    }

    protected void checkDomElement(
        final DomElement element,
        final DomElementAnnotationHolder holder,
        final DomHighlightingHelper helper,
        Object state
    ) {
        final int oldSize = holder.getSize();
        super.checkDomElement(element, holder, helper, state);

        if (oldSize == holder.getSize() && element instanceof GenericDomValue) {
            ExtendsClassChecker.checkExtendsClassInReferences((GenericDomValue) element, holder);
        }
    }

    @Nonnull
    @Override
    public LocalizeValue getGroupDisplayName() {
        return SpringLocalize.modelInspectionGroupName();
    }

    @Nonnull
    @Override
    public LocalizeValue getDisplayName() {
        return SpringLocalize.modelInspectionDisplayName();
    }

    @Nonnull
    @Override
    public String getShortName() {
        return "SpringModelInspection";
    }
}