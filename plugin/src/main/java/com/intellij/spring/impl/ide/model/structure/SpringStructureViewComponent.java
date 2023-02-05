package com.intellij.spring.impl.ide.model.structure;

import com.intellij.spring.impl.ide.model.xml.DomSpringBean;
import com.intellij.spring.impl.ide.model.xml.beans.Beans;
import consulo.fileEditor.FileEditor;
import consulo.fileEditor.structureView.StructureViewModel;
import consulo.ide.impl.idea.ide.structureView.newStructureView.StructureViewComponent;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.project.Project;
import consulo.project.ui.view.tree.AbstractTreeNode;
import consulo.util.concurrent.Promise;
import consulo.xml.psi.xml.XmlElement;
import consulo.xml.psi.xml.XmlTag;
import consulo.xml.util.xml.DomElement;
import consulo.xml.util.xml.DomManager;

import javax.annotation.Nonnull;

public class SpringStructureViewComponent extends StructureViewComponent {

  public SpringStructureViewComponent(FileEditor editor, @Nonnull StructureViewModel structureViewModel, @Nonnull Project project) {
    super(editor, structureViewModel, project, true);
  }

  @Override
  public Promise<AbstractTreeNode> expandPathToElement(Object element) {
    if (element instanceof XmlElement) {
      final XmlElement xmlElement = (XmlElement) element;
      XmlTag tag = PsiTreeUtil.getParentOfType(xmlElement, XmlTag.class);

      while (tag != null) {
        final DomElement domElement = DomManager.getDomManager(xmlElement.getProject()).getDomElement(tag);
        if (domElement != null) {
          final DomSpringBean springBean = domElement.getParentOfType(DomSpringBean.class, false);
          if (springBean != null) {
            if (springBean.getParent() instanceof Beans) {
              return super.expandPathToElement(springBean.getXmlElement());
            }
          }
        }

        tag = PsiTreeUtil.getParentOfType(tag, XmlTag.class, true);
      }

    }
    return super.expandPathToElement(element);
  }
}
