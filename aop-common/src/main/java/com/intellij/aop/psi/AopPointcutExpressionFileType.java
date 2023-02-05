/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */
package com.intellij.aop.psi;

import consulo.language.file.LanguageFileType;
import consulo.localize.LocalizeValue;
import consulo.ui.image.Image;
import org.jetbrains.annotations.NonNls;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author peter
 */
public class AopPointcutExpressionFileType extends LanguageFileType {
  public static final AopPointcutExpressionFileType INSTANCE = new AopPointcutExpressionFileType();
  
  private AopPointcutExpressionFileType() {
    super(AopPointcutExpressionLanguage.INSTANCE);
  }

  @Nonnull
  @NonNls
  public String getId() {
    return "Pointcut Expression";
  }

  @Nonnull
  public LocalizeValue getDescription() {
    return LocalizeValue.localizeTODO("Pointcut Expression");
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
