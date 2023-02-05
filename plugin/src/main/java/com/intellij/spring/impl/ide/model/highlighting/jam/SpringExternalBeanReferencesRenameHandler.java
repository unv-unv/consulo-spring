package com.intellij.spring.impl.ide.model.highlighting.jam;

import com.intellij.java.impl.refactoring.openapi.impl.JavaRenameRefactoringImpl;
import com.intellij.java.language.psi.PsiMethod;
import com.intellij.spring.impl.ide.model.converters.SpringBeanUtil;
import com.intellij.spring.impl.ide.model.jam.javaConfig.SpringJavaExternalBean;
import com.intellij.spring.impl.ide.model.jam.utils.SpringJamUtils;
import com.intellij.spring.impl.ide.model.xml.DomSpringBean;
import com.intellij.spring.impl.ide.model.xml.beans.SpringBaseBeanPointer;
import consulo.annotation.component.ExtensionImpl;
import consulo.codeEditor.Editor;
import consulo.dataContext.DataContext;
import consulo.language.editor.CommonDataKeys;
import consulo.language.editor.refactoring.RenameRefactoring;
import consulo.language.editor.refactoring.rename.RenameDialog;
import consulo.language.editor.refactoring.rename.RenameHandler;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.project.Project;
import consulo.ui.ex.awt.DialogWrapper;
import consulo.util.collection.ContainerUtil;
import consulo.xml.psi.xml.XmlTag;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ExtensionImpl
public class SpringExternalBeanReferencesRenameHandler implements RenameHandler {

  public boolean isAvailableOnDataContext(DataContext dataContext) {
    return false;
  }

  public boolean isRenaming(DataContext dataContext) {
    final DomSpringBean springBean = SpringBeanUtil.getTargetSpringBean(dataContext.getData(CommonDataKeys.EDITOR));
    if (springBean == null) return false;

    final List<SpringJavaExternalBean> list = SpringJamUtils.findExternalBeanReferences(springBean);

    return list.size() > 0;
  }


  public void invoke(@Nonnull Project project, Editor editor, PsiFile file, DataContext dataContext) {
    final DomSpringBean springBean = SpringBeanUtil.getTargetSpringBean(editor);
    if (springBean != null) {
      final ExternalBeanRenameDialog dialog = new ExternalBeanRenameDialog(springBean, editor);
      dialog.show();
    }
  }

  public void invoke(@Nonnull Project project, @Nonnull PsiElement[] elements, DataContext dataContext) {
  }

  public static void doRename(final DomSpringBean externalBean, final String newName, final boolean searchInComments) {
    final XmlTag psiElement = externalBean.getXmlTag();

    if (psiElement == null) return;

    final RenameRefactoring rename = new JavaRenameRefactoringImpl(psiElement.getProject(), psiElement, newName, searchInComments, false);

    Set<PsiElement> psiElements = new HashSet<PsiElement>();

    final List<SpringJavaExternalBean> list = SpringJamUtils.findExternalBeanReferences(externalBean);
    for (SpringJavaExternalBean springBean : list) {
      final PsiMethod psiMethod = springBean.getPsiElement();
      if (psiMethod != null) {
        psiElements.add(psiMethod);
        for (SpringBaseBeanPointer pointer : SpringJamUtils.findExternalBeans(psiMethod)) {
          if (!pointer.isReferenceTo(externalBean)) {
            ContainerUtil.addIfNotNull(psiElements, pointer.getPsiElement());
          }
        }
      }
      for (PsiElement element : psiElements) {
        rename.addElement(element, newName);
      }
    }

    rename.run();
  }

  private static class ExternalBeanRenameDialog extends RenameDialog {

    private final DomSpringBean myExternalBean;

    protected ExternalBeanRenameDialog(DomSpringBean externalBean, final Editor editor) {
      super(editor.getProject(), externalBean.getXmlTag(), null, editor);
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
