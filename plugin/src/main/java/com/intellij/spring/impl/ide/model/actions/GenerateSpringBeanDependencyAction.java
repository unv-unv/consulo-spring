package com.intellij.spring.impl.ide.model.actions;

import com.intellij.java.impl.codeInsight.generation.actions.BaseGenerateAction;
import com.intellij.java.language.psi.PsiClass;
import com.intellij.spring.impl.ide.SpringIcons;
import com.intellij.spring.impl.ide.model.actions.generate.GenerateSpringBeanDependenciesUtil;
import consulo.codeEditor.Editor;
import consulo.language.psi.PsiFile;
import consulo.module.Module;
import consulo.project.Project;
import consulo.ui.ex.action.AnActionEvent;

public abstract class GenerateSpringBeanDependencyAction extends BaseGenerateAction {

  public GenerateSpringBeanDependencyAction(final GenerateSpringBeanDependenciesActionHandler handler, String text) {
    super(handler);
    getTemplatePresentation().setText(text);
    getTemplatePresentation().setIcon(SpringIcons.SPRING_ICON);
  }

  protected boolean isValidForFile(Project project, Editor editor, PsiFile file) {
    if (!super.isValidForFile(project, editor, file)) return false;
    final PsiClass psiClass = getTargetClass(editor, file);

    if (psiClass == null || psiClass.isInterface() || psiClass.isEnum()) return false;
    Module module = GenerateSpringBeanDependenciesUtil.getSpringModule(psiClass);

    return  module != null &&  GenerateSpringBeanDependenciesUtil.acceptPsiClass(psiClass, isSetterDependency());
  }

  private boolean isSetterDependency() {
    return ((GenerateSpringBeanDependenciesActionHandler)getHandler()).isSetterDependency();  
  }

  public void update(final AnActionEvent event) {
    super.update(event);
  }
}
