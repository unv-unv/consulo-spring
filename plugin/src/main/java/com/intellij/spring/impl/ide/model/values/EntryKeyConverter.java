/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */

package com.intellij.spring.impl.ide.model.values;

import com.intellij.java.language.psi.JavaPsiFacade;
import com.intellij.java.language.psi.PsiClass;
import com.intellij.java.language.psi.PsiType;
import com.intellij.spring.impl.ide.model.xml.beans.SpringEntry;
import consulo.xml.util.xml.GenericDomValue;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

/**
 * @author Dmitry Avdeev
 */
public class EntryKeyConverter extends PropertyValueConverter {

  @Nonnull
  public List<? extends PsiType> getValueTypes(final GenericDomValue domValue) {
    SpringEntry entry = (SpringEntry)domValue.getParent();
    assert entry != null;
    final PsiClass psiClass = entry.getRequiredKeyClass();
    return psiClass == null ? Collections.<PsiType>emptyList() : Collections.singletonList(JavaPsiFacade.getInstance(psiClass.getProject()).getElementFactory().createType(psiClass));
  }
}
