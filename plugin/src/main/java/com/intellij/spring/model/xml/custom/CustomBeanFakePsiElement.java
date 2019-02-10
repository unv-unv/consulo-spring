/*
 * Copyright (c) 2000-2005 by JetBrains s.r.o. All Rights Reserved.
 * Use is subject to license terms.
 */
package com.intellij.spring.model.xml.custom;

import javax.annotation.Nonnull;

import org.jetbrains.annotations.NonNls;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.RenameableFakePsiElement;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlTag;
import com.intellij.spring.SpringBundle;
import com.intellij.spring.SpringIcons;
import com.intellij.util.IncorrectOperationException;
import consulo.ui.image.Image;

/**
 * @author peter
*/
public class CustomBeanFakePsiElement extends RenameableFakePsiElement {
  private final CustomNamespaceSpringBean myBean;

  public CustomBeanFakePsiElement(CustomNamespaceSpringBean bean) {
    super(bean.getContainingFile());
    myBean = bean;
  }

  public XmlTag getParent() {
    return myBean.getXmlTag();
  }

  @Override
  public String getName() {
    return myBean.getBeanName();
  }

  public CustomNamespaceSpringBean getBean() {
    return myBean;
  }

  @Override
  public PsiElement getNavigationElement() {
    return getParent();
  }

  public String getTypeName() {
    return SpringBundle.message("spring.bean");
  }

  @Override
    public PsiElement setName(@NonNls @Nonnull final String name) throws IncorrectOperationException {
    final XmlAttribute idAttribute = myBean.getIdAttribute();
    if (idAttribute != null) {
      idAttribute.setValue(name);
    }
    return super.setName(name);
  }

  @Override
  public boolean isEquivalentTo(PsiElement another) {
    if (another instanceof CustomBeanFakePsiElement) {
      final CustomBeanFakePsiElement element = (CustomBeanFakePsiElement)another;
      return element.getBean().equals(getBean());
    }
    return false;
  }

  public Image getIcon() {
    return SpringIcons.SPRING_BEAN_ICON;
  }
}
