/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */
package com.intellij.spring.impl.ide.aop;

import com.intellij.aop.AopAdvice;
import com.intellij.aop.AopIntroduction;
import com.intellij.aop.jam.AopJavaAnnotator;
import com.intellij.java.language.psi.PsiClass;
import com.intellij.spring.impl.ide.model.xml.DomSpringBean;
import com.intellij.spring.impl.ide.model.xml.aop.DeclareParents;
import com.intellij.spring.impl.ide.model.xml.aop.SpringAopAdvice;
import com.intellij.spring.impl.ide.model.xml.beans.Beans;
import com.intellij.xml.util.XmlTagUtil;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.Language;
import consulo.language.editor.gutter.LineMarkerInfo;
import consulo.language.editor.gutter.LineMarkerProvider;
import consulo.language.editor.ui.navigation.NavigationGutterIconBuilder;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.xml.lang.xml.XMLLanguage;
import consulo.xml.psi.xml.XmlFile;
import consulo.xml.psi.xml.XmlTag;
import consulo.xml.psi.xml.XmlToken;
import consulo.xml.util.xml.DomElement;
import consulo.xml.util.xml.DomFileElement;
import consulo.xml.util.xml.DomManager;

import jakarta.annotation.Nonnull;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author peter
 */
@ExtensionImpl
public class SpringAopAnnotator implements LineMarkerProvider {

  public LineMarkerInfo getLineMarkerInfo(final PsiElement element) {
    return null;
  }

  public void collectSlowLineMarkers(final List<PsiElement> elements, final Collection<LineMarkerInfo> result) {
    if (elements.isEmpty()) return;

    final PsiFile file = elements.get(0).getContainingFile();
    if (!(file instanceof XmlFile)) return;

    final XmlFile xmlFile = (XmlFile)file;
    final DomFileElement<Beans> fileElement = DomManager.getDomManager(xmlFile.getProject()).getFileElement(xmlFile, Beans.class);
    if (fileElement == null) return;

    for (final PsiElement element : elements) {
      annotate(element, result);
    }
  }

  private static void annotate(PsiElement psiElement, final Collection<LineMarkerInfo> result) {
    if (psiElement instanceof XmlToken && psiElement.getParent() instanceof XmlTag) {
      final XmlTag tag = (XmlTag)psiElement.getParent();
      if (XmlTagUtil.getStartTagNameElement(tag) == psiElement) {
        final DomElement element = DomManager.getDomManager(psiElement.getProject()).getDomElement(tag);
        if (element instanceof SpringAopAdvice) {
          final SpringAopAdvice advice = (SpringAopAdvice)element;
          result.add(AopJavaAnnotator.addNavigationToInterceptedMethods(advice, advice.getSearcher()).createLineMarkerInfo(psiElement));
        }
        else if (element instanceof DeclareParents) {
          final NavigationGutterIconBuilder<PsiElement> builder =
            AopJavaAnnotator.addNavigationToIntroducedClasses((DeclareParents)element);
          if (builder != null) {
            result.add(builder.createLineMarkerInfo(psiElement));
          }
        }
        else if (element instanceof DomSpringBean) {
          final PsiClass psiClass = ((DomSpringBean)element).getBeanClass();
          if (psiClass != null) {
            final Map<AopAdvice, Integer> advices = AopJavaAnnotator.getBoundAdvices(psiClass);
            if (!advices.isEmpty()) {
              result.add(AopJavaAnnotator.addNavigationToBoundAdvices(advices).createLineMarkerInfo(psiElement));
            }
            final List<AopIntroduction> introductions = AopJavaAnnotator.getBoundIntroductions(psiClass);
            if (!introductions.isEmpty()) {
              result.add(AopJavaAnnotator.addNavigationToBoundIntroductions(introductions).createLineMarkerInfo(psiElement));
            }
          }
        }
      }
    }
  }

  @Nonnull
  @Override
  public Language getLanguage() {
    return XMLLanguage.INSTANCE;
  }
}
