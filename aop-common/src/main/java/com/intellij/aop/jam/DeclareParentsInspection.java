/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */

package com.intellij.aop.jam;

import com.intellij.aop.AopBundle;
import com.intellij.aop.AopIntroduction;
import com.intellij.aop.IntroductionManipulator;
import com.intellij.aop.psi.AopPointcutExpressionFile;
import com.intellij.java.language.psi.PsiClass;
import com.intellij.java.language.psi.PsiLiteralExpression;
import com.intellij.java.language.psi.PsiMethod;
import com.intellij.java.language.psi.PsiModifier;
import consulo.annotation.component.ExtensionImpl;
import consulo.document.util.TextRange;
import consulo.language.editor.inspection.LocalQuickFix;
import consulo.language.editor.inspection.ProblemDescriptor;
import consulo.language.editor.inspection.ProblemHighlightType;
import consulo.language.editor.inspection.ProblemsHolder;
import consulo.language.editor.rawHighlight.HighlightDisplayLevel;
import consulo.language.inject.InjectedLanguageManager;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiElementVisitor;
import consulo.language.psi.PsiFile;
import consulo.language.util.IncorrectOperationException;
import consulo.logging.Logger;
import consulo.project.Project;
import consulo.util.collection.ContainerUtil;
import consulo.util.lang.function.Condition;
import consulo.virtualFileSystem.ReadonlyStatusHandler;
import consulo.xml.codeInspection.XmlSuppressableInspectionTool;
import consulo.xml.psi.xml.XmlAttributeValue;
import consulo.xml.psi.xml.XmlElement;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;

import javax.annotation.Nonnull;

/**
 * @author peter
 */
@ExtensionImpl
public class DeclareParentsInspection extends XmlSuppressableInspectionTool {
  private static final Logger LOG = Logger.getInstance(DeclareParentsInspection.class);

  public boolean isEnabledByDefault() {
    return true;
  }

  @Nonnull
  public HighlightDisplayLevel getDefaultLevel() {
    return HighlightDisplayLevel.ERROR;
  }

  @Nls
  @Nonnull
  public String getDisplayName() {
    return AopBundle.message("inspection.display.name.declareParents");
  }

  @NonNls
  @Nonnull
  public String getShortName() {
    return "DeclareParentsInspection";
  }

  @Nonnull
  public PsiElementVisitor buildVisitor(@Nonnull final ProblemsHolder holder, final boolean isOnTheFly) {
    return new PsiElementVisitor() {
      @Override
      public void visitElement(final PsiElement element) {
        if (element instanceof PsiLiteralExpression || element instanceof XmlAttributeValue) {
          PsiElement injectedElement =
            InjectedLanguageManager.getInstance(holder.getProject()).findElementAtNoCommit(element.getContainingFile(),
                                                                                           element.getTextRange().getStartOffset() + 1);

          final PsiFile file = injectedElement == null ? null : injectedElement.getContainingFile();
          if (file instanceof AopPointcutExpressionFile) {
            final IntroductionManipulator manipulator = ((AopPointcutExpressionFile)file).getAopModel().getIntroductionManipulator();
            if (manipulator == null) return;
            final AopIntroduction introduction = manipulator.getIntroduction();
            if (introduction == null) return;

            final PsiClass intf = introduction.getImplementInterface().getValue();
            if (intf == null && introduction.getImplementInterface().getStringValue() != null || intf != null && !intf.isInterface()) {
              registerProblem(manipulator.getInterfaceElement(), AopBundle.message("error.interface.expected"), holder);
              return;
            }
            if (intf == null) return;

            final PsiClass defaultImpl = introduction.getDefaultImpl().getValue();
            if (defaultImpl == null) {
              if (!(element instanceof XmlElement) && !ContainerUtil.findAll(intf.getAllMethods(), new Condition<PsiMethod>() {
                public boolean value(final PsiMethod method) {
                  return method.hasModifierProperty(PsiModifier.ABSTRACT);
                }
              }).isEmpty()) {
                holder.registerProblem(manipulator.getCommonProblemElement(),
                                       AopBundle.message("error.default.implementation.class.should.be.specified"),
                                       new LocalQuickFix() {
                                         @Nonnull
                                         public String getName() {
                                           return AopBundle.message("quickfix.name.define.attribute",
                                                                    manipulator.getDefaultImplAttributeName());
                                         }

                                         @Nonnull
                                         public String getFamilyName() {
                                           return getName();
                                         }

                                         public void applyFix(@Nonnull final Project project, @Nonnull final ProblemDescriptor descriptor) {
                                           try {
                                             if (ReadonlyStatusHandler.getInstance(project)
                                                                      .ensureFilesWritable(descriptor.getPsiElement()
                                                                                                     .getContainingFile()
                                                                                                     .getVirtualFile())
                                                                      .hasReadonlyFiles()) return;
                                             manipulator.defineDefaultImpl(project, descriptor);
                                           }
                                           catch (IncorrectOperationException e) {
                                             LOG.error(e);
                                           }
                                         }
                                       });

              }
              return;
            }
            if (defaultImpl.hasModifierProperty(PsiModifier.ABSTRACT) || !defaultImpl.isInheritor(intf, true)) {
              final PsiElement defaultImplElement = manipulator.getDefaultImplElement();
              assert defaultImplElement != null;
              registerProblem(defaultImplElement,
                              AopBundle.message("error.non.abstract.class.implemention.0.expected", intf.getQualifiedName()),
                              holder);
            }
          }
        }
      }
    };
  }

  private static void registerProblem(final PsiElement element, final String descriptionTemplate, final ProblemsHolder holder) {
    final int startOffset = element.getTextRange().getStartOffset();
    int quotes = element.getText().startsWith("\"") ? 1 : 0;
    final TextRange range = TextRange.from(quotes, Math.max(element.getTextLength() - 2 * quotes, 1));
    holder.registerProblem(holder.getManager().createProblemDescriptor(element, range,
                                                                       descriptionTemplate, ProblemHighlightType.GENERIC_ERROR_OR_WARNING));
  }

  @Nls
  @Nonnull
  public String getGroupDisplayName() {
    return AopBundle.message("inspection.group.display.name.aop");
  }
}