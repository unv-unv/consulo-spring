/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */

package com.intellij.spring.impl.ide.model.highlighting;

import com.intellij.java.impl.util.xml.DomJavaUtil;
import com.intellij.java.language.psi.PsiClass;
import com.intellij.java.language.psi.util.InheritanceUtil;
import com.intellij.spring.impl.ide.SpringBundle;
import com.intellij.spring.impl.ide.model.xml.aop.RequiredBeanType;
import com.intellij.spring.impl.ide.model.xml.beans.Beans;
import com.intellij.spring.impl.ide.model.xml.beans.SpringBeanPointer;
import consulo.annotation.component.ExtensionImpl;
import consulo.xml.util.xml.DomElement;
import consulo.xml.util.xml.GenericAttributeValue;
import consulo.xml.util.xml.highlighting.DomElementAnnotationHolder;
import consulo.xml.util.xml.highlighting.DomElementsInspection;
import consulo.xml.util.xml.highlighting.DomHighlightingHelper;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;

import javax.annotation.Nonnull;

@ExtensionImpl
public class RequiredBeanTypeInspection extends DomElementsInspection<Beans> {

  public RequiredBeanTypeInspection() {
    super(Beans.class);
  }

  @Override
  protected void checkDomElement(DomElement element, DomElementAnnotationHolder holder, DomHighlightingHelper helper) {
    if (element instanceof GenericAttributeValue) {
      Object value = ((GenericAttributeValue)element).getValue();

      if (value instanceof SpringBeanPointer) {
        final RequiredBeanType type = element.getAnnotation(RequiredBeanType.class);
        if (type != null) {
          SpringBeanPointer springBeanPointer = (SpringBeanPointer)value;

          PsiClass[] classes = springBeanPointer.getEffectiveBeanType();
          final PsiClass requiredClass = DomJavaUtil.findClass(type.value(), element);
          if (requiredClass != null) {
            boolean isAssignable = false;
            for (PsiClass psiClass : classes) {
              if (InheritanceUtil.isInheritorOrSelf(psiClass, requiredClass, true)) {
                isAssignable = true;
                break;
              }
            }
            if (!isAssignable) {
              if (holder != null) {
                final String message = SpringBundle.message("bean.must.be.of.type", requiredClass.getQualifiedName());
                holder.createProblem(element, message);
              }
            }
          }
        }
      }
    }
  }

  @Nls
  @Nonnull
  @Override
  public String getGroupDisplayName() {
    return SpringBundle.message("model.inspection.group.name");
  }

  @Nls
  @Nonnull
  public String getDisplayName() {
    return SpringBundle.message("required.spring.bean.type.inspection");
  }

  @NonNls
  @Nonnull
  public String getShortName() {
    return "RequiredBeanTypeInspection";
  }
}