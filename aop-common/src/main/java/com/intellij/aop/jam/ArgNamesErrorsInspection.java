/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */
package com.intellij.aop.jam;

import com.intellij.aop.ArgNamesManipulator;
import com.intellij.aop.LocalAopModel;
import com.intellij.aop.psi.AopPointcutExpressionFile;
import com.intellij.java.language.psi.*;
import consulo.annotation.access.RequiredReadAction;
import consulo.annotation.component.ExtensionImpl;
import consulo.aop.localize.AopLocalize;
import consulo.application.util.function.Processor;
import consulo.document.util.TextRange;
import consulo.language.editor.inspection.ProblemHighlightType;
import consulo.language.editor.inspection.ProblemsHolder;
import consulo.language.editor.inspection.scheme.InspectionManager;
import consulo.language.editor.rawHighlight.HighlightDisplayLevel;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiManager;
import consulo.language.psi.PsiReference;
import consulo.language.psi.scope.GlobalSearchScope;
import consulo.language.psi.search.ReferencesSearch;
import consulo.localize.LocalizeValue;
import consulo.xml.psi.xml.XmlElement;
import jakarta.annotation.Nonnull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author peter
 */
@ExtensionImpl
public class ArgNamesErrorsInspection extends AbstractArgNamesInspection {
    @Nonnull
    public HighlightDisplayLevel getDefaultLevel() {
        return HighlightDisplayLevel.ERROR;
    }

    protected void checkAnnotation(
        final PsiParameter[] parameters, final ProblemsHolder holder,
        final ArgNamesManipulator manipulator, final PsiMethod method
    ) {
        final String names = manipulator.getArgNames();
        if (names != null) {
            final String[] strings = names.trim().split(",");
            for (int i = 0; i < strings.length; i++) {
                strings[i] = strings[i].trim();
            }
            final List<String> actualNames = getGeneralArgumentNames(parameters);
            if (!actualNames.equals(Arrays.asList(strings))) {
                holder.newProblem(AopLocalize.errorArgnamesShouldMatch(manipulator.getArgNamesAttributeName()))
                    .range(manipulator.getArgNamesProblemElement())
                    .withFix(new SetArgNamesQuickFix(
                        AopLocalize.quickfixNameArgNamesCorrect(manipulator.getArgNamesAttributeName()),
                        true,
                        manipulator,
                        method
                    ))
                    .withFix(new SetArgNamesQuickFix(
                        AopLocalize.quickfixNameArgNamesRemove(manipulator.getArgNamesAttributeName()),
                        false,
                        manipulator,
                        method
                    ))
                    .create();
                return;
            }
        }
        final PsiReference returningReference = manipulator.getReturningReference();
        if (returningReference != null) {
            if (method != null && returningReference.resolve() == null) {
                addAnnoReferenceProblem(holder, returningReference);
                return;
            }
        }
        final PsiReference throwingReference = manipulator.getThrowingReference();
        if (throwingReference != null) {
            final PsiElement psiElement = throwingReference.resolve();
            if (method != null && psiElement == null) {
                addAnnoReferenceProblem(holder, throwingReference);
                return;
            }
            else if (psiElement instanceof PsiParameter) {
                final PsiManager psiManager = psiElement.getManager();
                final PsiClass throwableClass = JavaPsiFacade.getInstance(psiManager.getProject())
                    .findClass(CommonClassNames.JAVA_LANG_THROWABLE, GlobalSearchScope.allScope(psiElement.getProject()));
                if (throwableClass != null &&
                    !JavaPsiFacade.getInstance(psiManager.getProject())
                        .getElementFactory()
                        .createType(throwableClass)
                        .isAssignableFrom(((PsiParameter) psiElement).getType())) {
                    holder.newProblem(AopLocalize.errorThrowableExpected())
                        .range(throwingReference.getElement())
                        .create();
                    return;
                }
            }
        }

        for (final PsiParameter parameter : parameters) {
            if (!LocalAopModel.isJoinPointParamer(parameter)) {
                boolean hasRef = !ReferencesSearch.search(parameter).forEach(new Processor<PsiReference>() {
                    public boolean process(final PsiReference reference) {
                        return !(reference.getElement().getContainingFile() instanceof AopPointcutExpressionFile);
                    }
                });
                if (!hasRef && !parameter.equals(manipulator.getReturningParameter()) && !parameter.equals(manipulator.getThrowingParameter())) {
                    holder.newProblem(AopLocalize.unboundPointcutParameter(parameter.getName()))
                        .range(manipulator.getArgNamesProblemElement())
                        .create();
                }
            }
        }
    }

    @RequiredReadAction
    private static void addAnnoReferenceProblem(final ProblemsHolder holder, final PsiReference returningReference) {
        final PsiElement element = returningReference.getElement();
        TextRange range = returningReference.getRangeInElement();
        final boolean emptyRange = range.isEmpty();
        if (emptyRange) {
            range = TextRange.from(range.getStartOffset(), 1);
        }
        LocalizeValue message = ProblemsHolder.unresolvedReferenceMessage(returningReference);
        final ProblemHighlightType highlightType = emptyRange || !(element instanceof PsiLiteralExpression || element instanceof XmlElement)
            ? ProblemHighlightType.GENERIC_ERROR_OR_WARNING : ProblemHighlightType.LIKE_UNKNOWN_SYMBOL;
        holder.registerProblem(InspectionManager.getInstance(element.getProject()).createProblemDescriptor(element, range, message.get(),
            highlightType
        ));
    }

    public static List<String> getGeneralArgumentNames(final PsiParameter[] parameters) {
        final List<String> actualNames = new ArrayList<String>();
        for (PsiParameter parameter : parameters) {
            actualNames.add(parameter.getName());
        }
        return actualNames;
    }

    @Nonnull
    @Override
    public LocalizeValue getDisplayName() {
        return AopLocalize.inspectionDisplayNameArgnamesErrors();
    }

    @Nonnull
    @Override
    public String getShortName() {
        return "ArgNamesErrorsInspection";
    }
}
