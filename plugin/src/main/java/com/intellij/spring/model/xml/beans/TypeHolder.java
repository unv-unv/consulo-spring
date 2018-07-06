/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */

package com.intellij.spring.model.xml.beans;

import com.intellij.psi.PsiType;
import com.intellij.util.xml.DomElement;
import javax.annotation.Nonnull;

import java.util.List;

/**
 * @author Dmitry Avdeev
 */
public interface TypeHolder extends DomElement {
  @Nonnull
  List<? extends PsiType> getRequiredTypes();
}
