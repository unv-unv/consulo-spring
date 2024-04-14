/*
 * Copyright (c) 2000-2006 JetBrains s.r.o. All Rights Reserved.
 */
package com.intellij.spring.impl;

import com.intellij.java.language.psi.PsiClass;
import com.intellij.spring.impl.ide.model.xml.beans.SpringBaseBeanPointer;

import java.util.*;

public abstract class Class2BeansMap {
  private final Map<String, List<SpringBaseBeanPointer>> myMap = new HashMap<>();

  public List<SpringBaseBeanPointer> get(PsiClass psiClass) {
    String fqn = psiClass.getQualifiedName();
    if (fqn == null) {
      return Collections.emptyList();
    }
    List<SpringBaseBeanPointer> pointers = myMap.get(fqn);
    if (pointers == null) {
      pointers = new ArrayList<>();
      compute(psiClass, pointers);
      myMap.put(fqn, pointers);
    }
    return pointers;

  }

  protected abstract void compute(PsiClass psiClass, List<SpringBaseBeanPointer> pointers);
}
