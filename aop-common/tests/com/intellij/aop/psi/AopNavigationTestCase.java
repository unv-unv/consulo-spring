/*
 * Copyright (c) 2000-2005 by JetBrains s.r.o. All Rights Reserved.
 * Use is subject to license terms.
 */
package com.intellij.aop.psi;

import com.intellij.aop.AopLiteFixture;
import consulo.codeEditor.markup.GutterIconRenderer;
import consulo.language.editor.gutter.LineMarkerInfo;
import com.intellij.psi.PsiClass;
import consulo.language.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.xml.XmlTag;
import com.intellij.testFramework.UsefulTestCase;
import com.intellij.testFramework.fixtures.JavaCodeInsightFixtureTestCase;
import java.util.function.Consumer;
import java.util.function.Function;
import consulo.util.collection.ContainerUtil;
import consulo.language.editor.ui.navigation.NavigationGutterIconRenderer;
import consulo.util.lang.StringUtil;
import gnu.trove.THashSet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author peter
 */
public abstract class AopNavigationTestCase extends JavaCodeInsightFixtureTestCase {

  protected void setUp() throws Exception {
    super.setUp();

    AopLiteFixture.addAopAnnotations(myFixture);
  }

  public void checkNavigation(final String filePath, final boolean orderMatters, String... expected) throws Throwable {
    List<Collection<String>> targets = new ArrayList<Collection<String>>();
    for (final GutterIconRenderer renderer : myFixture.findAllGutters(filePath)) {
      processGutterIcon(orderMatters, targets, renderer);
      if (renderer instanceof LineMarkerInfo.LineMarkerGutterIconRenderer) {
        final LineMarkerInfo.LineMarkerGutterIconRenderer iconRenderer = (LineMarkerInfo.LineMarkerGutterIconRenderer)renderer;
        processGutterIcon(orderMatters, targets, iconRenderer.getLineMarkerInfo().getNavigationHandler());
      }
    }
    Consumer<Collection<String>>[] checkers = ContainerUtil.map2Array(expected, Consumer.class, new Function<String, Consumer>() {
      public Consumer fun(final String s) {
        return new Consumer<Collection<String>>() {
          public void consume(final Collection<String> o) {
            final String[] navItems = StringUtil.isEmpty(s) ? new String[0] : s.split("\n");
            if (orderMatters) {
              UsefulTestCase.assertOrderedEquals(o, navItems);
            } else {
              UsefulTestCase.assertSameElements(o, navItems);
            }
          }
        };
      }
    });

    UsefulTestCase.assertOrderedCollection(targets, checkers);
  }

  private static void processGutterIcon(final boolean orderMatters, final List<Collection<String>> targets, final Object renderer) {
    if (renderer instanceof NavigationGutterIconRenderer) {
      final NavigationGutterIconRenderer navRenderer = (NavigationGutterIconRenderer)renderer;
      final List<PsiElement> elements = navRenderer.getTargetElements();
      Collection<String> toStrings = ContainerUtil.map(elements, new Function<PsiElement, String>() {
        public String fun(final PsiElement element) {
          if (element instanceof PsiClass) {
            return ((PsiClass)element).getQualifiedName();
          }
          if (element instanceof PsiMethod) {
            final PsiMethod method = (PsiMethod)element;
            return method.getContainingClass().getQualifiedName() + "#" + method.getName() + method.getParameterList().getText();
          }
          if (element instanceof XmlTag) {
            return ((XmlTag)element).getName();
          }
          throw new AssertionError(element);
        }
      });
      if (!orderMatters) {
        toStrings = new THashSet<String>(toStrings);
      }
      targets.add(toStrings);
    }
  }
}
