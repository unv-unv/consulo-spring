package com.intellij.spring.impl.ide.model.actions;

import com.intellij.java.impl.codeInsight.generation.actions.BaseGenerateAction;
import com.intellij.java.language.psi.PsiClass;
import com.intellij.spring.impl.ide.model.actions.generate.GenerateSpringBeanDependenciesUtil;
import consulo.annotation.access.RequiredReadAction;
import consulo.codeEditor.Editor;
import consulo.language.psi.PsiFile;
import consulo.localize.LocalizeValue;
import consulo.module.Module;
import consulo.project.Project;
import consulo.spring.impl.icon.SpringImplIconGroup;
import consulo.ui.ex.action.AnActionEvent;
import jakarta.annotation.Nonnull;

public abstract class GenerateSpringBeanDependencyAction extends BaseGenerateAction {
    public GenerateSpringBeanDependencyAction(GenerateSpringBeanDependenciesActionHandler handler, @Nonnull LocalizeValue text) {
        super(handler, text);
        getTemplatePresentation().setIcon(SpringImplIconGroup.spring());
    }

    @Override
    @RequiredReadAction
    protected boolean isValidForFile(@Nonnull Project project, @Nonnull Editor editor, @Nonnull PsiFile file) {
        if (!super.isValidForFile(project, editor, file)) {
            return false;
        }
        PsiClass psiClass = getTargetClass(editor, file);

        if (psiClass == null || psiClass.isInterface() || psiClass.isEnum()) {
            return false;
        }
        Module module = GenerateSpringBeanDependenciesUtil.getSpringModule(psiClass);

        return module != null && GenerateSpringBeanDependenciesUtil.acceptPsiClass(psiClass, isSetterDependency());
    }

    private boolean isSetterDependency() {
        return ((GenerateSpringBeanDependenciesActionHandler) getHandler()).isSetterDependency();
    }

    @Override
    public void update(@Nonnull AnActionEvent event) {
        super.update(event);
    }
}
