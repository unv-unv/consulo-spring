package com.intellij.spring.impl.ide.model.structure;

import com.intellij.spring.impl.ide.SpringManager;
import com.intellij.spring.impl.ide.model.xml.beans.Beans;
import consulo.annotation.component.ExtensionImpl;
import consulo.fileEditor.structureView.StructureViewTreeElement;
import consulo.language.psi.PsiFile;
import consulo.project.Project;
import consulo.xml.ide.structureView.xml.XmlStructureViewElementProvider;
import consulo.xml.psi.xml.XmlFile;
import consulo.xml.psi.xml.XmlTag;
import consulo.xml.util.xml.DomElement;
import consulo.xml.util.xml.DomManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@ExtensionImpl
public class SpringStructureViewElementProvider implements XmlStructureViewElementProvider {

  @Nullable
  public StructureViewTreeElement createCustomXmlTagTreeElement(@Nonnull final XmlTag tag) {
    final PsiFile psiFile = tag.getContainingFile();
    final Project project = tag.getProject();
    if (psiFile instanceof XmlFile && SpringManager.getInstance(project).isSpringBeans((XmlFile)psiFile)) {
      final DomElement domElement = DomManager.getDomManager(project).getDomElement(tag);
      if (domElement instanceof Beans) {
        return new SpringModelTreeElement((XmlFile)psiFile, false);
      }
    }

    return null;
  }
}
