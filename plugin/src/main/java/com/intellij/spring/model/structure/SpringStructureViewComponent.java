package com.intellij.spring.model.structure;

import com.intellij.ide.structureView.StructureViewModel;
import com.intellij.ide.structureView.newStructureView.StructureViewComponent;
import com.intellij.ide.util.treeView.AbstractTreeNode;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.xml.XmlElement;
import com.intellij.psi.xml.XmlTag;
import com.intellij.spring.model.xml.DomSpringBean;
import com.intellij.spring.model.xml.beans.Beans;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.DomManager;
import org.jetbrains.concurrency.Promise;

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
