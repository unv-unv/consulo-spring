/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */

package com.intellij.spring.impl.ide.gutter;

import consulo.language.editor.ui.PsiElementListCellRenderer;
import consulo.language.psi.PsiElement;
import consulo.ui.image.Image;
import consulo.xml.psi.xml.XmlTag;
import consulo.xml.util.xml.DomElement;
import consulo.xml.util.xml.DomManager;
import org.jetbrains.annotations.Nls;

import jakarta.annotation.Nullable;

/**
 * @author Dmitry Avdeev
 */
public class DomElementListCellRenderer extends PsiElementListCellRenderer<XmlTag> {

  protected final String myUnknown;

  public DomElementListCellRenderer(@Nls String unknownElementText) {
    myUnknown = unknownElementText;
  }

  public String getElementText(final XmlTag element) {
    final DomElement domElement = getDomElement(element);
    if (domElement == null) return element.getName();

    final String elementName = domElement.getPresentation().getElementName();
    return elementName == null ? myUnknown : elementName;
  }

  protected String getContainerText(final XmlTag element, final String name) {
    return getContainerText(element);
  }

  public static String getContainerText(final PsiElement element) {
    return " (" + element.getContainingFile().getName() + ")";
  }

  protected int getIconFlags() {
    return 0;
  }

  protected Image getIcon(final PsiElement element) {
    final DomElement domElement = getDomElement((XmlTag)element);
    if (domElement != null && domElement.getPresentation().getIcon() != null) {
      return domElement.getPresentation().getIcon();
    }

    return super.getIcon(element);
  }

  @Nullable
  protected static DomElement getDomElement(XmlTag tag) {
    return DomManager.getDomManager(tag.getProject()).getDomElement(tag);
  }
}
