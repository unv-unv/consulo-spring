/*
 * Copyright (c) 2000-2006 JetBrains s.r.o. All Rights Reserved.
 */

package com.intellij.spring.impl.ide.model.highlighting;

import com.intellij.spring.impl.ide.SpringBundle;
import com.intellij.spring.impl.ide.SpringIcons;
import com.intellij.spring.impl.ide.SpringModel;
import com.intellij.spring.impl.ide.gutter.DomElementListCellRenderer;
import com.intellij.spring.impl.ide.model.SpringUtils;
import com.intellij.spring.impl.ide.model.xml.DomSpringBean;
import com.intellij.spring.impl.ide.model.xml.beans.SpringBaseBeanPointer;
import com.intellij.spring.impl.ide.model.xml.beans.SpringBeanPointer;
import consulo.language.editor.annotation.AnnotationHolder;
import consulo.language.editor.annotation.Annotator;
import consulo.language.editor.ui.navigation.NavigationGutterIconBuilder;
import consulo.language.psi.PsiElement;
import consulo.util.collection.ContainerUtil;
import consulo.xml.psi.xml.XmlTag;
import consulo.xml.util.xml.DomElement;
import consulo.xml.util.xml.DomManager;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public class SpringDomAnnotator implements Annotator {

  private static final DomElementListCellRenderer RENDERER = new DomElementListCellRenderer(SpringBundle.message("spring.bean.with.unknown.name"));

  private static final Function<SpringBaseBeanPointer, Collection<? extends PsiElement>> CONVERTER =
    new Function<SpringBaseBeanPointer, Collection<? extends PsiElement>>() {

      @Nonnull
      public Collection<? extends PsiElement> apply(final SpringBaseBeanPointer pointer) {
        return ContainerUtil.createMaybeSingletonList(pointer.getPsiElement());
      }
    };

  private static void annotateBean(DomSpringBean bean, AnnotationHolder holder) {
    final XmlTag tag = bean.getXmlTag();
    if (tag == null) return;

    final SpringModel model = SpringUtils.getSpringModel(bean);
    final List<SpringBaseBeanPointer> children = model.getChildren(SpringBeanPointer.createSpringBeanPointer(bean));
    if (children.size() > 0) {

      final NavigationGutterIconBuilder<SpringBaseBeanPointer> iconBuilder =
        NavigationGutterIconBuilder.create(SpringIcons.PARENT_GUTTER, CONVERTER);
      iconBuilder.
                   setTargets(children).
                   setPopupTitle(SpringBundle.message("spring.bean.class.navigate.choose.class.title")).
                   setCellRenderer(RENDERER).
                   setTooltipText(SpringBundle.message("spring.parent.bean.tooltip")).
                   install(holder, bean.getXmlTag());
    }
  }

  public void annotate(PsiElement psiElement, AnnotationHolder holder) {
    if (psiElement instanceof XmlTag) {
      final DomElement element = DomManager.getDomManager(psiElement.getProject()).getDomElement((XmlTag)psiElement);
      if (element instanceof DomSpringBean) {
        annotateBean((DomSpringBean)element, holder);
      }
    }
  }
}
