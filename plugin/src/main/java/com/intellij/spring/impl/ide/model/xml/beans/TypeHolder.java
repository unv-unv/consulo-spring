/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */

package com.intellij.spring.impl.ide.model.xml.beans;

import com.intellij.java.language.psi.PsiType;
import consulo.xml.util.xml.DomElement;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * @author Dmitry Avdeev
 */
public interface TypeHolder extends DomElement {
  @Nonnull
  List<? extends PsiType> getRequiredTypes();
}
