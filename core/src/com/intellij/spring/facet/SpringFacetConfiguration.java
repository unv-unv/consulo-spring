/*
 * Copyright (c) 2000-2006 JetBrains s.r.o. All Rights Reserved.
 */

package com.intellij.spring.facet;

import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.WriteExternalException;
import com.intellij.openapi.vfs.pointers.VirtualFilePointer;
import consulo.spring.module.extension.SpringModuleExtension;
import org.jdom.Element;
import org.jetbrains.annotations.NonNls;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Dmitry Avdeev
 */
@Deprecated
public class SpringFacetConfiguration {

  @NonNls private static final String FILESET = "fileset";
  @NonNls private static final String SET_ID = "id";
  @NonNls private static final String SET_NAME = "name";
  @NonNls private static final String SET_REMOVED = "removed";
  @NonNls private static final String FILE = "file";
  @NonNls private static final String DEPENDENCY = "dependency";

  private final Set<SpringFileSet> myFileSets = new LinkedHashSet<SpringFileSet>();
  private long myModificationCount;


  public void readExternal(SpringModuleExtension springModuleExtension, Element element) throws InvalidDataException {
    for (Object setElement: element.getChildren(FILESET)) {
      final String setName = ((Element)setElement).getAttributeValue(SET_NAME);
      final String setId = ((Element)setElement).getAttributeValue(SET_ID);
      final String removed = ((Element)setElement).getAttributeValue(SET_REMOVED);
      if (setName != null && setId != null) {
        final SpringFileSet fileSet = new SpringFileSet(setId, setName, springModuleExtension);
        final List deps = ((Element)setElement).getChildren(DEPENDENCY);
        for (Object dep : deps) {
          fileSet.addDependency(((Element)dep).getText());
        }
        final List files = ((Element)setElement).getChildren(FILE);
        for (Object fileElement: files) {
          final String text = ((Element)fileElement).getText();
          fileSet.addFile(text);
        }
        fileSet.setRemoved(Boolean.valueOf(removed).booleanValue());
        myFileSets.add(fileSet);
      }
    }
  }

  public void writeExternal(Element element) throws WriteExternalException {
    for (SpringFileSet fileSet: myFileSets) {
      final Element setElement = new Element(FILESET);
      setElement.setAttribute(SET_ID, fileSet.getId());
      setElement.setAttribute(SET_NAME, fileSet.getName());
      setElement.setAttribute(SET_REMOVED, Boolean.toString(fileSet.isRemoved()));
      element.addContent(setElement);
      for (String dep: fileSet.getDependencies()) {
        final Element depElement = new Element(DEPENDENCY);
        depElement.setText(dep);
        setElement.addContent(depElement);
      }
      for (VirtualFilePointer fileName: fileSet.getFiles()) {
        final Element fileElement = new Element(FILE);
        fileElement.setText(fileName.getUrl());
        setElement.addContent(fileElement);
      }
    }
  }

  public long getModificationCount() {
    return myModificationCount;
  }

  public void setModified() {
    myModificationCount++;
  }

  public void dispose() {

  }
}
