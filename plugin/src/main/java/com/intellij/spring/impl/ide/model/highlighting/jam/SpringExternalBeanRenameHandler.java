package com.intellij.spring.impl.ide.model.highlighting.jam;

import com.intellij.java.impl.refactoring.openapi.impl.JavaRenameRefactoringImpl;
import com.intellij.java.language.psi.PsiMethod;
import com.intellij.spring.impl.ide.model.jam.javaConfig.SpringJavaExternalBean;
import com.intellij.spring.impl.ide.model.jam.utils.SpringJamUtils;
import com.intellij.spring.impl.ide.model.xml.beans.SpringBaseBeanPointer;
import consulo.annotation.component.ExtensionImpl;
import consulo.codeEditor.Editor;
import consulo.dataContext.DataContext;
import consulo.language.editor.LangDataKeys;
import consulo.language.editor.refactoring.RenameRefactoring;
import consulo.language.editor.refactoring.rename.RenameDialog;
import consulo.language.editor.refactoring.rename.RenameHandler;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.project.Project;
import consulo.ui.ex.awt.DialogWrapper;
import consulo.xml.psi.xml.XmlTag;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

@ExtensionImpl
public class SpringExternalBeanRenameHandler implements RenameHandler {

  public boolean isAvailableOnDataContext(DataContext dataContext) {
    return false;
  }

  public boolean isRenaming(DataContext dataContext) {
    return getExternalBean(dataContext) != null;
  }

  @Nullable
  private SpringJavaExternalBean getExternalBean(final DataContext dataContext) {
    PsiElement element = dataContext.getData(LangDataKeys.PSI_ELEMENT);

    return element instanceof PsiMethod ? SpringJamUtils.getExternalBean((PsiMethod)element) : null;
  }

  public void invoke(@Nonnull Project project, Editor editor, PsiFile file, DataContext dataContext) {
    final SpringJavaExternalBean externalBean = getExternalBean(dataContext);

    if (externalBean != null) {
      final ExternalBeanRenameDialog dialog = new ExternalBeanRenameDialog(externalBean, editor);
      dialog.show();
    }
  }

  public void invoke(@Nonnull Project project, @Nonnull PsiElement[] elements, DataContext dataContext) {
  }

  public static void doRename(final SpringJavaExternalBean externalBean, final String newName, final boolean searchInComments) {
    final PsiMethod psiElement = externalBean.getPsiElement();

    if (psiElement == null) return;

    final RenameRefactoring rename = new JavaRenameRefactoringImpl(psiElement.getProject(), psiElement, newName, searchInComments, false);
    Set<PsiElement> psiElements = new HashSet<PsiElement>();
    for (SpringBaseBeanPointer springBean : SpringJamUtils.findExternalBeans(psiElement)) {
      final XmlTag tag = springBean.getSpringBean().getXmlTag();
      if (tag != null) {
        psiElements.add(tag);
        for (SpringJavaExternalBean javaExternalBean : SpringJamUtils.findExternalBeanReferences(springBean.getSpringBean())) {
          if (javaExternalBean.equals(externalBean)) continue;
          final PsiMethod psiMethod = javaExternalBean.getPsiElement();
          if (psiMethod != null) {
            psiElements.add(psiMethod);
          }
        }
      }
    }
    for (PsiElement element : psiElements) {
      rename.addElement(element, newName);
    }
    rename.run();
  }

  private static class ExternalBeanRenameDialog extends RenameDialog {

    private final SpringJavaExternalBean myExternalBean;

    protected ExternalBeanRenameDialog(SpringJavaExternalBean externalBean, final Editor editor) {
      super(externalBean.getPsiElement().getProject(), externalBean.getPsiElement(), null, editor);
      myExternalBean = externalBean;
    }

    protected void doAction() {
      final String newName = getNewName();
      final boolean searchInComments = isSearchInComments();
      doRename(myExternalBean, newName, searchInComments);
      close(DialogWrapper.OK_EXIT_CODE);
    }

  }
}
