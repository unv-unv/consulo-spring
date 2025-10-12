/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */

package com.intellij.spring.impl.ide.model.highlighting;

import com.intellij.java.impl.codeInsight.daemon.impl.quickfix.ExtendsListFix;
import com.intellij.java.language.psi.JavaPsiFacade;
import com.intellij.java.language.psi.PsiClass;
import com.intellij.spring.impl.ide.SpringModel;
import com.intellij.spring.impl.ide.model.converters.ReplacedMethodBeanConverter;
import com.intellij.spring.impl.ide.model.xml.beans.Beans;
import com.intellij.spring.impl.ide.model.xml.beans.ReplacedMethod;
import com.intellij.spring.impl.ide.model.xml.beans.SpringBean;
import com.intellij.spring.impl.ide.model.xml.beans.SpringBeanPointer;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.editor.annotation.HighlightSeverity;
import consulo.language.psi.scope.GlobalSearchScope;
import consulo.localize.LocalizeValue;
import consulo.project.Project;
import consulo.spring.localize.SpringLocalize;
import consulo.xml.util.xml.highlighting.DomElementAnnotationHolder;
import jakarta.annotation.Nonnull;

@ExtensionImpl
public class ReplacedMethodsInspection extends SpringBeanInspectionBase {
    protected void checkBean(
        SpringBean springBean,
        final Beans beans,
        final DomElementAnnotationHolder holder,
        final SpringModel springModel, Object state
    ) {
        for (ReplacedMethod replacedMethod : springBean.getReplacedMethods()) {
            checkReplacedMethod(springBean, replacedMethod, holder);
        }
    }

    private static void checkReplacedMethod(
        final SpringBean springBean,
        final ReplacedMethod replacedMethod,
        final DomElementAnnotationHolder holder
    ) {

        final SpringBeanPointer beanPointer = replacedMethod.getReplacer().getValue();
        if (beanPointer != null) {
            final PsiClass beanClass = beanPointer.getBeanClass();

            if (beanClass != null) {
                final Project project = springBean.getManager().getProject();
                final PsiClass replacerClass = JavaPsiFacade.getInstance(project)
                    .findClass(
                        ReplacedMethodBeanConverter.METHOD_REPLACER_CLASS,
                        GlobalSearchScope.allScope(project)
                    );
                if (replacerClass != null && !beanClass.isInheritor(replacerClass, true)) {
                    holder.createProblem(
                        replacedMethod.getReplacer(),
                        HighlightSeverity.ERROR,
                        SpringLocalize.springBeanReplacedMethodMustImplementMethodreplacer().get(),
                        new ExtendsListFix(beanClass, replacerClass, true)
                    );
                }
            }
        }
    }

    @Nonnull
    @Override
    public LocalizeValue getDisplayName() {
        return SpringLocalize.springBeanReplaceMethodsInspection();
    }

    @Nonnull
    @Override
    public String getShortName() {
        return "SpringReplacedMethodsInspection";
    }
}
