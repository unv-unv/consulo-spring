/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */

package com.intellij.aop.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiPrimitiveType;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.Collection;
import java.util.Collections;

/**
 * @author peter
 */
public class AopPrimitiveTypeExpression extends AopElementBase implements AopTypeExpression{
  public AopPrimitiveTypeExpression(@Nonnull final ASTNode node) {
    super(node);
  }

  public String toString() {
    return "AopPrimitiveTypeExpression";
  }

  @Nonnull
  public Collection<AopPsiTypePattern> getPatterns() {
    final PsiPrimitiveType psiType = getPsiType();
    return psiType == null ? Collections.<AopPsiTypePattern>emptyList() : Collections.singletonList((AopPsiTypePattern) new PsiPrimitiveTypePattern(psiType)); 

  }

  public String getTypePattern() {
    return getText();
  }

  @Nullable
  public PsiPrimitiveType getPsiType() {
    return JavaPsiFacade.getInstance(getProject()).getElementFactory().createPrimitiveType(getText());
  }

}