/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */

package com.intellij.spring.impl.ide.model.properties;

import com.intellij.java.impl.psi.impl.beanProperties.BeanProperty;
import com.intellij.java.language.psi.PsiMethod;
import com.intellij.spring.impl.ide.model.jam.javaConfig.SpringJavaBean;
import com.intellij.spring.impl.ide.model.xml.beans.SpringBeanPointer;
import com.intellij.spring.impl.ide.model.xml.beans.SpringPropertyDefinition;
import consulo.codeEditor.Editor;
import consulo.dataContext.DataContext;
import consulo.language.editor.LangDataKeys;
import consulo.language.editor.PlatformDataKeys;
import consulo.language.editor.TargetElementUtil;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.language.psi.PsiReference;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.xml.psi.xml.XmlAttribute;
import consulo.xml.psi.xml.XmlFile;
import consulo.xml.util.xml.DomElement;
import consulo.xml.util.xml.DomManager;
import consulo.xml.util.xml.DomUtil;
import consulo.xml.util.xml.GenericAttributeValue;

import jakarta.annotation.Nullable;

/**
 * @author Dmitry Avdeev
 */
public class SpringPropertiesUtil {
  private SpringPropertiesUtil() {
  }

  @Nullable
  public static BeanProperty getBeanProperty(DataContext dataContext) {
    final Editor editor = dataContext.getData(PlatformDataKeys.EDITOR);
    final PsiFile file = dataContext.getData(LangDataKeys.PSI_FILE);

    return getBeanProperty(editor, file);
  }

  @Nullable
  public static BeanProperty getBeanProperty(final Editor editor, final PsiFile file) {
    if (editor != null && file instanceof XmlFile) {
      final int offset = editor.getCaretModel().getOffset();
      final DomElement value = DomUtil.getContextElement(editor);
      final SpringPropertyDefinition property = DomUtil.getParentOfType(value, SpringPropertyDefinition.class, false);

      if (property == null || isJavaBeanReference(file, offset)) return null;

      final PsiReference reference = TargetElementUtil.findReference(editor, offset);
      if (reference != null) {
        final PsiElement psiElement = reference.resolve();
        if (psiElement instanceof PsiMethod) {
          return BeanProperty.createBeanProperty((PsiMethod)psiElement);
        }
      }
    }
    return null;
  }

  @Nullable
  public static BeanProperty getBeanProperty(PsiElement element) {
    PsiFile file = element.getContainingFile();
    if (file instanceof XmlFile) {
      final DomElement value = DomUtil.getDomElement(element);
      final SpringPropertyDefinition property = DomUtil.getParentOfType(value, SpringPropertyDefinition.class, false);

      if (property == null || isJavaBeanReference(file, element)) return null;

      final PsiReference reference = file.findReferenceAt(element.getTextOffset());
      if (reference != null) {
        final PsiElement psiElement = reference.resolve();
        if (psiElement instanceof PsiMethod) {
          return BeanProperty.createBeanProperty((PsiMethod)psiElement);
        }
      }
    }
    return null;
  }

  static boolean isJavaBeanReference(final PsiFile file, PsiElement element) {
    final XmlAttribute xmlAttribute = PsiTreeUtil.getParentOfType(element, XmlAttribute.class);

    if (xmlAttribute != null) {
      final DomElement value = DomManager.getDomManager(file.getProject()).getDomElement(xmlAttribute);
      if (value instanceof GenericAttributeValue) {
        final Object attributeValue = ((GenericAttributeValue) value).getValue();
        if (attributeValue instanceof SpringBeanPointer) {
          return ((SpringBeanPointer) attributeValue).getSpringBean() instanceof SpringJavaBean;
        }
      }
    }

    return false;
  }

  static boolean isJavaBeanReference(final PsiFile file, final int offset) {
    final XmlAttribute xmlAttribute = PsiTreeUtil.getParentOfType(file.findElementAt(offset), XmlAttribute.class);

    if (xmlAttribute != null) {
      final DomElement value = DomManager.getDomManager(file.getProject()).getDomElement(xmlAttribute);
      if (value instanceof GenericAttributeValue) {
        final Object attributeValue = ((GenericAttributeValue)value).getValue();
        if (attributeValue instanceof SpringBeanPointer) {
          return ((SpringBeanPointer)attributeValue).getSpringBean() instanceof SpringJavaBean;
        }
      }
    }

    return false;
  }
}
