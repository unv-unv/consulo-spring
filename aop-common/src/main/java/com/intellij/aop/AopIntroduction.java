/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */
package com.intellij.aop;

import com.intellij.aop.psi.AopReferenceHolder;
import com.intellij.jam.model.common.CommonModelElement;
import com.intellij.psi.PsiClass;
import com.intellij.util.xml.GenericValue;
import javax.annotation.Nonnull;

/**
 * @author peter
 */
public interface AopIntroduction extends CommonModelElement {

  @Nonnull
  GenericValue<AopReferenceHolder> getTypesMatching();


  @Nonnull
  GenericValue<PsiClass> getImplementInterface();


  @Nonnull
  GenericValue<PsiClass> getDefaultImpl();
}
