/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */
package com.intellij.spring.impl.ide.model.highlighting;

import consulo.language.editor.inspection.LocalQuickFix;
import consulo.language.editor.inspection.ProblemDescriptor;
import consulo.language.util.IncorrectOperationException;
import consulo.localize.LocalizeValue;
import consulo.logging.Logger;
import consulo.navigation.OpenFileDescriptorFactory;
import consulo.project.Project;
import consulo.spring.localize.SpringLocalize;
import consulo.virtualFileSystem.ReadonlyStatusHandler;
import consulo.xml.psi.xml.XmlAttribute;
import consulo.xml.psi.xml.XmlTag;
import jakarta.annotation.Nonnull;
import org.jetbrains.annotations.NonNls;

/**
 * @author peter
 */
public class DefineAttributeQuickFix implements LocalQuickFix {
    private static final Logger LOG = Logger.getInstance(DefineAttributeQuickFix.class);
    private final String myAttrName;

    public DefineAttributeQuickFix(final String attrName) {
        myAttrName = attrName;
    }

    @Nonnull
    @Override
    public LocalizeValue getName() {
        return SpringLocalize.aopQuickfixDefine0Attr(myAttrName);
    }

    public void applyFix(@Nonnull final Project project, @Nonnull final ProblemDescriptor descriptor) {
        try {
            final XmlTag tag = (XmlTag) descriptor.getPsiElement();
            if (ReadonlyStatusHandler.getInstance(project)
                .ensureFilesWritable(descriptor.getPsiElement().getContainingFile().getVirtualFile())
                .hasReadonlyFiles()) {
                return;
            }
            final XmlAttribute attribute = tag.setAttribute(myAttrName, "", "");

            OpenFileDescriptorFactory.getInstance(project)
                .builder(tag.getContainingFile().getVirtualFile())
                .offset(attribute.getValueElement().getTextRange().getStartOffset() + 1)
                .build()
                .navigate(true);
        }
        catch (IncorrectOperationException e) {
            LOG.error(e);
        }
    }
}
