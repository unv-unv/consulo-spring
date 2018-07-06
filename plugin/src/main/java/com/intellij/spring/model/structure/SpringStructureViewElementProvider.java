package com.intellij.spring.model.structure;

import javax.annotation.Nonnull;

import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.structureView.xml.XmlStructureViewElementProvider;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import com.intellij.spring.SpringManager;
import com.intellij.spring.model.xml.beans.Beans;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.DomManager;

import javax.annotation.Nullable;

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
