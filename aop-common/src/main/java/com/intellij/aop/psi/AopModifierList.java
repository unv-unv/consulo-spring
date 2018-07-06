/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */

package com.intellij.aop.psi;

import javax.annotation.Nonnull;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiModifierListOwner;

/**
 * @author peter
 */
public class AopModifierList extends AopElementBase {

  public AopModifierList(@Nonnull final ASTNode node) {
    super(node);
  }

  public String toString() {
    return "AopModifierList";
  }

  public boolean accepts(PsiModifierListOwner owner) {
    for (final ASTNode node : getNode().getChildren(null)) {
      if (node.getElementType() == AopElementTypes.AOP_MODIFIER &&  !owner.hasModifierProperty(node.getText())) return false;
    }

    for (final AopNotExpression expression : findChildrenByClass(AopNotExpression.class)) {
      if (owner.hasModifierProperty(expression.getLastChild().getNode().getText())) return false;
    }

    return true;
  }
}