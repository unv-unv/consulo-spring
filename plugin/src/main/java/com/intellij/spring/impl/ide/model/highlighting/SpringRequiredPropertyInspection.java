/*
 * Copyright (c) 2000-2006 JetBrains s.r.o. All Rights Reserved.
 */
package com.intellij.spring.impl.ide.model.highlighting;

import com.intellij.java.language.codeInsight.AnnotationUtil;
import com.intellij.java.language.psi.PsiClass;
import com.intellij.java.language.psi.PsiMethod;
import com.intellij.java.language.psi.util.PropertyUtil;
import com.intellij.spring.impl.ide.SpringModel;
import com.intellij.spring.impl.ide.constants.SpringAnnotationsConstants;
import com.intellij.spring.impl.ide.model.actions.generate.SpringPropertiesGenerateProvider;
import com.intellij.spring.impl.ide.model.actions.generate.SpringTemplateBuilder;
import com.intellij.spring.impl.ide.model.xml.beans.Beans;
import com.intellij.spring.impl.ide.model.xml.beans.SpringBean;
import com.intellij.spring.impl.ide.model.xml.beans.SpringPropertyDefinition;
import consulo.annotation.component.ExtensionImpl;
import consulo.codeEditor.Editor;
import consulo.language.editor.annotation.HighlightSeverity;
import consulo.language.editor.inspection.LocalQuickFix;
import consulo.language.editor.inspection.ProblemDescriptor;
import consulo.localize.LocalizeValue;
import consulo.project.Project;
import consulo.spring.localize.SpringLocalize;
import consulo.util.lang.StringUtil;
import consulo.virtualFileSystem.ReadonlyStatusHandler;
import consulo.xml.util.xml.DomElement;
import consulo.xml.util.xml.DomUtil;
import consulo.xml.util.xml.highlighting.DomElementAnnotationHolder;
import jakarta.annotation.Nonnull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ExtensionImpl
public class SpringRequiredPropertyInspection extends SpringBeanInspectionBase {
    @Nonnull
    @Override
    public LocalizeValue getDisplayName() {
        return SpringLocalize.springRequiredPropertyInspection();
    }

    @Nonnull
    @Override
    public String getShortName() {
        return "SpringRequiredPropertyInspection";
    }

    protected void checkBean(
        final SpringBean springBean,
        final Beans beans,
        final DomElementAnnotationHolder holder,
        final SpringModel springModel, Object state
    ) {
        if (springBean.isAbstract()) {
            return;
        }
        final PsiClass psiClass = springBean.getBeanClass();
        if (psiClass != null) {
            final Map<String, PsiMethod> properties = PropertyUtil.getAllProperties(psiClass, true, false);
            final List<SpringPropertyDefinition> list = springBean.getAllProperties();
            final List<String> missing = new ArrayList<String>();
            final List<PsiMethod> missingMethods = new ArrayList<PsiMethod>();
            for (Map.Entry<String, PsiMethod> entry : properties.entrySet()) {
                if (AnnotationUtil.findAnnotation(entry.getValue(), SpringAnnotationsConstants.REQUIRED_ANNOTATION) != null) {
                    if (!isDefined(list, entry.getKey())) {
                        missing.add(entry.getKey());
                        missingMethods.add(entry.getValue());
                    }
                }
            }
            if (!missing.isEmpty()) {
                final DomElement element = DomUtil.hasXml(springBean.getClazz()) ? springBean.getClazz() : springBean;
                holder.createProblem(
                    element,
                    HighlightSeverity.ERROR,
                    SpringLocalize.requiredPropertiesMissed(StringUtil.join(missing, ",")).get(),
                    new LocalQuickFix() {
                        @Nonnull
                        @Override
                        public LocalizeValue getName() {
                            return SpringLocalize.createMissingProperties();
                        }

                        public void applyFix(@Nonnull final Project project, @Nonnull final ProblemDescriptor descriptor) {
                            if (!ReadonlyStatusHandler.getInstance(project)
                                .ensureFilesWritable(descriptor.getPsiElement().getContainingFile().getVirtualFile())
                                .hasReadonlyFiles()) {
                                final Editor editor = SpringTemplateBuilder.getEditor(descriptor);
                                SpringPropertiesGenerateProvider.doGenerate(editor, springBean, project,
                                    missingMethods.toArray(new PsiMethod[missingMethods.size()])
                                );
                            }
                        }
                    }
                );
            }
        }
    }

    private static boolean isDefined(final List<SpringPropertyDefinition> list, final String property) {
        for (SpringPropertyDefinition definition : list) {
            final String name = definition.getPropertyName();
            if (name != null && name.equals(property)) {
                return true;
            }
        }
        return false;
    }
}