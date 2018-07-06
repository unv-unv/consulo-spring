/*
 * Copyright (c) 2000-2005 by JetBrains s.r.o. All Rights Reserved.
 * Use is subject to license terms.
 */
package com.intellij.aop.jam;

import com.intellij.aop.AopBundle;
import com.intellij.aop.LocalAopModel;
import com.intellij.aop.psi.AopPointcutExpressionFile;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.codeInspection.XmlSuppressableInspectionTool;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.tree.injected.InjectedLanguageUtil;
import com.intellij.psi.xml.XmlAttributeValue;
import org.jetbrains.annotations.Nls;
import javax.annotation.Nonnull;

import java.util.List;

/**
 * @author peter
 */
public abstract class AbstractAopInspection extends XmlSuppressableInspectionTool {
  public boolean isEnabledByDefault() {
    return true;
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
    InjectedLanguageUtil.enumerate(element, new PsiLanguageInjectionHost.InjectedPsiVisitor() {
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
    return AopBundle.message("inspection.group.display.name.aop");
  }
}
