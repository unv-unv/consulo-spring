/*
 * Copyright (c) 2000-2006 JetBrains s.r.o. All Rights Reserved.
 */

package com.intellij.spring.impl.ide.model.highlighting;

import com.intellij.spring.impl.ide.SpringBundle;
import com.intellij.spring.impl.ide.SpringManager;
import com.intellij.spring.impl.ide.SpringModel;
import com.intellij.spring.impl.ide.model.SpringModelVisitor;
import com.intellij.spring.impl.ide.model.xml.CommonSpringBean;
import com.intellij.spring.impl.ide.model.xml.beans.Beans;
import com.intellij.spring.impl.ide.model.xml.beans.SpringBean;
import consulo.xml.psi.xml.XmlFile;
import consulo.xml.util.xml.DomFileElement;
import consulo.xml.util.xml.highlighting.DomElementAnnotationHolder;
import consulo.xml.util.xml.highlighting.DomElementsInspection;

import javax.annotation.Nonnull;

/**
 * @author Dmitry Avdeev
 */
public abstract class SpringBeanInspectionBase extends DomElementsInspection<Beans> {

  public SpringBeanInspectionBase() {
    super(Beans.class);
  }

  @Nonnull
  public String getGroupDisplayName() {
    return SpringBundle.message("model.inspection.group.name");
  }

  public void checkFileElement(final DomFileElement<Beans> domFileElement, final DomElementAnnotationHolder holder) {
    final XmlFile xmlFile = domFileElement.getFile();
    final Beans beans = domFileElement.getRootElement();
    final SpringModel model = SpringManager.getInstance(xmlFile.getProject()).getSpringModelByFile(xmlFile);
    final SpringModelVisitor visitor = createVisitor(holder, beans, model);
    SpringModelVisitor.visitBeans(visitor, beans);
  }

  protected SpringModelVisitor createVisitor(final DomElementAnnotationHolder holder, final Beans beans, final SpringModel model) {
    return new SpringModelVisitor() {

      protected boolean visitBean(CommonSpringBean bean) {
        if (bean instanceof SpringBean) {
          checkBean((SpringBean)bean, beans, holder, model);
        }
        return true;
      }
    };
  }

  protected void checkBean(SpringBean springBean, final Beans beans, final DomElementAnnotationHolder holder, final SpringModel springModel) {}
}
