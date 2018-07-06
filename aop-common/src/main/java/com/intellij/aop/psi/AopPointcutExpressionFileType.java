/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */
package com.intellij.aop.psi;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.intellij.openapi.fileTypes.LanguageFileType;
import consulo.ui.image.Image;
import org.jetbrains.annotations.NonNls;

/**
 * @author peter
 */
public class AopPointcutExpressionFileType extends LanguageFileType {
  public static final AopPointcutExpressionFileType INSTANCE = new AopPointcutExpressionFileType();
  
  private AopPointcutExpressionFileType() {
    super(new AopPointcutExpressionLanguage());
  }

  @Nonnull
  @NonNls
  public String getName() {
    return "Pointcut Expression";
  }

  @Nonnull
  public String getDescription() {
    return getName();
  }

  @Nonnull
  @NonNls
  public String getDefaultExtension() {
    return "pointcutExpression";
  }

  @Nullable
  public Image getIcon() {
    return null;
  }
}
