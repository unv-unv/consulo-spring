/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */
package com.intellij.spring.impl.ide.model.highlighting;

import com.intellij.spring.impl.ide.SpringBundle;
import consulo.language.editor.inspection.LocalQuickFix;
import consulo.language.editor.inspection.ProblemDescriptor;
import consulo.language.util.IncorrectOperationException;
import consulo.logging.Logger;
import consulo.navigation.OpenFileDescriptorFactory;
import consulo.project.Project;
import consulo.virtualFileSystem.ReadonlyStatusHandler;
import consulo.xml.psi.xml.XmlAttribute;
import consulo.xml.psi.xml.XmlTag;
import org.jetbrains.annotations.NonNls;

import jakarta.annotation.Nonnull;

/**
 * @author peter
 */
public class DefineAttributeQuickFix implements LocalQuickFix {
  private static final Logger LOG = Logger.getInstance(DefineAttributeQuickFix.class);
  private final String myAttrName;

  public DefineAttributeQuickFix(@NonNls final String attrName) {
    myAttrName = attrName;
  }

  @Nonnull
  public String getName() {
    return getFamilyName();
  }

  @Nonnull
  public String getFamilyName() {
    return SpringBundle.message("aop.quickfix.define.0.attr", myAttrName);
  }

  public void applyFix(@Nonnull final Project project, @Nonnull final ProblemDescriptor descriptor) {
    try {
      final XmlTag tag = (XmlTag)descriptor.getPsiElement();
      if (ReadonlyStatusHandler.getInstance(project)
                               .ensureFilesWritable(descriptor.getPsiElement().getContainingFile().getVirtualFile())
                               .hasReadonlyFiles()) return;
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
