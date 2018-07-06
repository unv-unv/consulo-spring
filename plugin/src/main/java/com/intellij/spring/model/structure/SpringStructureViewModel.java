package com.intellij.spring.model.structure;

import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.structureView.impl.xml.XmlStructureViewTreeModel;
import com.intellij.ide.util.treeView.smartTree.Sorter;
import com.intellij.openapi.Disposable;
import com.intellij.psi.xml.XmlFile;
import com.intellij.util.xml.DomElementNavigationProvider;
import com.intellij.util.xml.DomElementsNavigationManager;
import javax.annotation.Nonnull;

public class SpringStructureViewModel extends XmlStructureViewTreeModel implements Disposable {

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
