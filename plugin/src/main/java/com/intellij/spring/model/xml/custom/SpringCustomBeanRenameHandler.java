package com.intellij.spring.model.xml.custom;

import javax.annotation.Nonnull;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.refactoring.rename.PsiElementRenameHandler;
import com.intellij.refactoring.rename.RenameHandler;
import com.intellij.spring.SpringBundle;

public class SpringCustomBeanRenameHandler implements RenameHandler {

  public boolean isAvailableOnDataContext(DataContext dataContext) {
    return false;
  }

  public boolean isRenaming(DataContext dataContext) {
    PsiElement element = dataContext.getData(LangDataKeys.PSI_ELEMENT);
    return element instanceof CustomBeanFakePsiElement;
  }

  public void invoke(@Nonnull Project project, Editor editor, PsiFile file, DataContext dataContext) {
    final PsiElement element = dataContext.getData(LangDataKeys.PSI_ELEMENT);
    doInvoke(project, editor, element);
  }

  private static void doInvoke(final Project project, final Editor editor, final PsiElement element) {
    final XmlAttribute idAttribute = ((CustomBeanFakePsiElement)element).getBean().getIdAttribute();
    if (idAttribute == null) {
      final int i = Messages
          .showOkCancelDialog(project, SpringBundle.message("custom.bean.no.id"), SpringBundle.message("custom.bean.no.id.title"), Messages.getWarningIcon());
      if (i != 0) return;
    }

    PsiElementRenameHandler.invoke(element, project, element, editor);
  }

  public void invoke(@Nonnull Project project, @Nonnull PsiElement[] elements, DataContext dataContext) {
  }

}