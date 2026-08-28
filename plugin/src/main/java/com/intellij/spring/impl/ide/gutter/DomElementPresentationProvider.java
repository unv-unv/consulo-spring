/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */

package com.intellij.spring.impl.ide.gutter;

import consulo.annotation.access.RequiredReadAction;
import consulo.application.Application;
import consulo.language.editor.ui.navigation.PsiTargetPresentationFactory;
import consulo.language.editor.ui.navigation.TargetPresentationProvider;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.localize.LocalizeValue;
import consulo.navigation.TargetPresentation;
import consulo.navigation.TargetPresentationBuilder;
import consulo.ui.image.Image;
import consulo.xml.dom.DomElement;
import consulo.xml.dom.DomManager;
import consulo.xml.language.psi.XmlTag;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * @author Dmitry Avdeev
 */
public class DomElementPresentationProvider implements TargetPresentationProvider<PsiElement> {
    protected final LocalizeValue myUnknown;

    public DomElementPresentationProvider(@Nonnull LocalizeValue unknownElementText) {
        myUnknown = unknownElementText;
    }

    @Override
    @RequiredReadAction
    public TargetPresentation getPresentation(PsiElement element) {
        TargetPresentationBuilder builder = Application.get()
            .getInstance(PsiTargetPresentationFactory.class)
            .presentationBuilder(element)
            .withContainerText(getContainerText(element));

        if (element instanceof XmlTag tag) {
            builder = builder.withPresentableText(getElementText(tag));

            Image icon = getIcon(tag);
            if (icon != null) {
                builder = builder.withIcon(icon);
            }
        }

        return builder.build();
    }

    @Nonnull
    @RequiredReadAction
    public LocalizeValue getElementText(XmlTag element) {
        DomElement domElement = getDomElement(element);
        if (domElement == null) {
            return LocalizeValue.of(element.getName());
        }

        String elementName = domElement.getPresentation().getElementName();
        return elementName == null ? myUnknown : LocalizeValue.of(elementName);
    }

    @Nonnull
    @RequiredReadAction
    public static LocalizeValue getContainerText(PsiElement element) {
        PsiFile file = element.getContainingFile();
        return file == null ? LocalizeValue.empty() : LocalizeValue.of(file.getName());
    }

    @Nullable
    @RequiredReadAction
    public Image getIcon(XmlTag element) {
        DomElement domElement = getDomElement(element);
        return domElement == null ? null : domElement.getPresentation().getIcon();
    }

    @Nullable
    protected static DomElement getDomElement(XmlTag tag) {
        return DomManager.getDomManager(tag.getProject()).getDomElement(tag);
    }
}
