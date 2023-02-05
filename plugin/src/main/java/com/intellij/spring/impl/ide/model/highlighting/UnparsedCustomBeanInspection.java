/*
 * Copyright (c) 2000-2006 JetBrains s.r.o. All Rights Reserved.
 */

package com.intellij.spring.impl.ide.model.highlighting;

import com.intellij.spring.impl.ide.SpringBundle;
import com.intellij.spring.impl.ide.SpringModel;
import com.intellij.spring.impl.ide.model.SpringModelVisitor;
import com.intellij.spring.impl.ide.model.xml.CommonSpringBean;
import com.intellij.spring.impl.ide.model.xml.CustomBeanWrapper;
import com.intellij.spring.impl.ide.model.xml.beans.Beans;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.editor.annotation.HighlightSeverity;
import consulo.language.editor.rawHighlight.HighlightDisplayLevel;
import consulo.xml.util.xml.highlighting.DomElementAnnotationHolder;

import javax.annotation.Nonnull;

/**
 * @author Dmitry Avdeev
 */
@ExtensionImpl
public class UnparsedCustomBeanInspection extends SpringBeanInspectionBase {

  @Nonnull
  public String getDisplayName() {
    return SpringBundle.message("unparsed.custom.bean.inspection");
  }

  @Nonnull
  @Override
  public HighlightDisplayLevel getDefaultLevel() {
    return HighlightDisplayLevel.WARNING;
  }

  @Nonnull
  public String getShortName() {
    return "UnparsedCustomBeanInspection";
  }

  protected SpringModelVisitor createVisitor(final DomElementAnnotationHolder holder, final Beans beans, final SpringModel model) {
    return new SpringModelVisitor() {

      protected boolean visitBean(CommonSpringBean bean) {
        if (bean instanceof CustomBeanWrapper) {
          final CustomBeanWrapper wrapper = (CustomBeanWrapper)bean;
          if (!wrapper.isParsed()) {
            holder.createProblem(wrapper,
                                 HighlightSeverity.GENERIC_SERVER_ERROR_OR_WARNING,
                                 SpringBundle.message("unparsed.custom.bean.message"));
          }
        }
        return true;
      }
    };
  }


}