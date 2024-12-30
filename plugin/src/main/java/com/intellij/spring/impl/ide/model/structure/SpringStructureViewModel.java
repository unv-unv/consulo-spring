package com.intellij.spring.impl.ide.model.structure;

import consulo.disposer.Disposable;
import consulo.fileEditor.structureView.StructureViewTreeElement;
import consulo.fileEditor.structureView.tree.Sorter;
import consulo.xml.ide.structureView.impl.xml.XmlStructureViewTreeModel;
import consulo.xml.psi.xml.XmlFile;
import consulo.xml.util.xml.DomElementNavigationProvider;
import consulo.xml.util.xml.DomElementsNavigationManager;

import jakarta.annotation.Nonnull;

public class SpringStructureViewModel extends XmlStructureViewTreeModel implements Disposable
{

  private final SpringModelTreeElement myRoot;

  public SpringStructureViewModel(@Nonnull XmlFile xmlFile) {
     this(xmlFile, DomElementsNavigationManager.getManager(xmlFile.getProject()).getDomElementsNavigateProvider(DomElementsNavigationManager.DEFAULT_PROVIDER_NAME),
          false);
  }

  public SpringStructureViewModel(@Nonnull XmlFile xmlFile, final DomElementNavigationProvider navigationProvider, boolean showBeanStructure) {

    super(xmlFile, null);
    myRoot = new SpringModelTreeElement(getPsiFile(), navigationProvider, showBeanStructure);
  }

  @Nonnull
  public StructureViewTreeElement getRoot() {
    return myRoot;
  }

  @Nonnull
  public Sorter[] getSorters() {
     return new Sorter[]{Sorter.ALPHA_SORTER};
  }
}
