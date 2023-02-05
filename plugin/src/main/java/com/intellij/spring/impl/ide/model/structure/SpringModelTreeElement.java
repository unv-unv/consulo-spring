package com.intellij.spring.impl.ide.model.structure;

import com.intellij.spring.impl.ide.SpringBundle;
import com.intellij.spring.impl.ide.SpringIcons;
import com.intellij.spring.impl.ide.SpringManager;
import com.intellij.spring.impl.ide.SpringModel;
import com.intellij.spring.impl.ide.model.xml.CommonSpringBean;
import com.intellij.spring.impl.ide.model.xml.CustomBeanWrapper;
import com.intellij.spring.impl.ide.model.xml.DomSpringBean;
import com.intellij.spring.impl.ide.model.xml.beans.SpringBaseBeanPointer;
import consulo.fileEditor.structureView.StructureViewTreeElement;
import consulo.fileEditor.structureView.tree.TreeElement;
import consulo.navigation.ItemPresentation;
import consulo.ui.image.Image;
import consulo.xml.ide.structureView.impl.xml.XmlTagTreeElement;
import consulo.xml.psi.xml.XmlDocument;
import consulo.xml.psi.xml.XmlFile;
import consulo.xml.psi.xml.XmlTag;
import consulo.xml.util.xml.DomElement;
import consulo.xml.util.xml.DomElementNavigationProvider;
import consulo.xml.util.xml.DomElementsNavigationManager;
import consulo.xml.util.xml.DomManager;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class SpringModelTreeElement implements StructureViewTreeElement, ItemPresentation {

  private XmlFile myXmlFile;
  private DomElementNavigationProvider myNavigationProvider;
  private boolean myShowBeanStructure;

  public SpringModelTreeElement(XmlFile xmlFile, final boolean showBeanStructure) {
    this(xmlFile, DomElementsNavigationManager.getManager(xmlFile.getProject()).getDomElementsNavigateProvider(
      DomElementsNavigationManager.DEFAULT_PROVIDER_NAME), showBeanStructure);
  }

  public SpringModelTreeElement(XmlFile xmlFile, final DomElementNavigationProvider navigationProvider, final boolean showBeanStructure) {
    myXmlFile = xmlFile;
    myNavigationProvider = navigationProvider;
    myShowBeanStructure = showBeanStructure;
  }

  public Object getValue() {
    return myXmlFile;
  }

  public ItemPresentation getPresentation() {
    return this;
  }

  public TreeElement[] getChildren() {
    List<StructureViewTreeElement> treeElements = new ArrayList<StructureViewTreeElement>();
    SpringModel springModel = getSpringModel();
    if (springModel != null) {
      for (SpringBaseBeanPointer pointer : springModel.getAllDomBeans()) {
        final CommonSpringBean springBean = pointer.getSpringBean();
        if (pointer.isValid() && springBean.isValid() && springBean instanceof DomSpringBean) {
          treeElements
            .add(new SpringBeanTreeElement((DomSpringBean)springBean, myNavigationProvider, myShowBeanStructure));
        }
      }
    }
    final DomManager manager = DomManager.getDomManager(myXmlFile.getProject());
    final XmlDocument document = myXmlFile.getDocument();
    if (document != null) {
      final XmlTag rootTag = document.getRootTag();
      if (rootTag != null) {
        for (final XmlTag tag : rootTag.getSubTags()) {
          final DomElement element = manager.getDomElement(tag);
          if (element instanceof CustomBeanWrapper && ((CustomBeanWrapper)element).isDummy()) {
            treeElements.add(new XmlTagTreeElement(tag));
          }
        }
      }
    }

    return treeElements.toArray(new TreeElement[treeElements.size()]);
  }

  @Nullable
  private SpringModel getSpringModel() {
    return SpringManager.getInstance(myXmlFile.getProject()).getLocalSpringModel(myXmlFile);
  }

  public void navigate(final boolean requestFocus) {
  }

  public boolean canNavigate() {
    return false;
  }

  public boolean canNavigateToSource() {
    return false;
  }

  public String getPresentableText() {
    return SpringBundle.message("spring.beans");
  }

  public String getLocationString() {
    return null;
  }

  public Image getIcon() {
    return SpringIcons.SPRING_BEANS_ICON;
  }
}
