/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */

package com.intellij.spring.impl.ide.model.values;

import com.intellij.java.language.psi.PsiType;
import com.intellij.spring.impl.ide.model.xml.beans.ListOrSet;
import com.intellij.spring.impl.ide.model.xml.beans.SpringValue;
import consulo.xml.util.xml.GenericDomValue;

import jakarta.annotation.Nonnull;
import java.util.List;

/**
 * @author Dmitry Avdeev
 */
public class ListOrSetValueConverter extends PropertyValueConverter {

  @Nonnull
  public List<? extends PsiType> getValueTypes(final GenericDomValue element) {
    if (element instanceof SpringValue) {
      final List<? extends PsiType> psiTypes = ((SpringValue)element).getRequiredTypes();
      if (!psiTypes.isEmpty())
      return psiTypes;
    }
    final ListOrSet parent = (ListOrSet)element.getParent();
    assert parent != null;
    return parent.getRequiredTypes();
  }
}
