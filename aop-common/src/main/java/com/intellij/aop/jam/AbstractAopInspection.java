/*
 * Copyright (c) 2000-2005 by JetBrains s.r.o. All Rights Reserved.
 * Use is subject to license terms.
 */
package com.intellij.aop.jam;

import com.intellij.aop.LocalAopModel;
import com.intellij.aop.psi.AopPointcutExpressionFile;
import com.intellij.aop.psi.AopPointcutExpressionLanguage;
import com.intellij.java.language.psi.PsiLiteralExpression;
import com.intellij.java.language.psi.PsiMethod;
import consulo.language.Language;
import consulo.language.editor.inspection.ProblemsHolder;
import consulo.language.editor.rawHighlight.HighlightDisplayLevel;
import consulo.language.inject.InjectedLanguageManager;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiElementVisitor;
import consulo.language.psi.PsiFile;
import consulo.language.psi.PsiLanguageInjectionHost;
import consulo.xml.codeInspection.XmlSuppressableInspectionTool;
import consulo.xml.psi.xml.XmlAttributeValue;
import org.jetbrains.annotations.Nls;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.util.List;

/**
 * @author peter
 */
public abstract class AbstractAopInspection extends XmlSuppressableInspectionTool {
  public boolean isEnabledByDefault() {
    return true;
  }

  @Nonnull
  @Override
  public HighlightDisplayLevel getDefaultLevel() {
    return HighlightDisplayLevel.WARNING;
  }

  @Nullable
  @Override
  public Language getLanguage() {
    return AopPointcutExpressionLanguage.getInstance();
  }

  @Nonnull
  public PsiElementVisitor buildVisitor(@Nonnull final ProblemsHolder holder, final boolean isOnTheFly) {
    return new PsiElementVisitor() {
      @Override public void visitElement(final PsiElement element) {
        if (element instanceof PsiLiteralExpression || element instanceof XmlAttributeValue) {
          checkElement(element, holder);
        }
      }
    };
  }

  protected void checkElement(final PsiElement element, final ProblemsHolder holder) {
    InjectedLanguageManager.getInstance(element.getProject()).enumerate(element, new PsiLanguageInjectionHost.InjectedPsiVisitor() {
      public void visit(@Nonnull PsiFile file, @Nonnull List<PsiLanguageInjectionHost.Shred> places) {
        if (file instanceof AopPointcutExpressionFile && file.getContext() == element) {
          final AopPointcutExpressionFile aopFile = (AopPointcutExpressionFile)file;
          final LocalAopModel model = aopFile.getAopModel();
          final PsiMethod method = model.getPointcutMethod();
          if (method != null) {
            checkAopMethod(method, model, holder, aopFile);
          }
        }
      }
    });
  }

  protected abstract void checkAopMethod(final PsiMethod pointcutMethod, final LocalAopModel model, final ProblemsHolder holder,
                                         final AopPointcutExpressionFile aopFile);

  @Nls
  @Nonnull
  public String getGroupDisplayName() {
    return "";
  }
}
