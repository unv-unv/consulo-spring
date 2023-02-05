/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */
package com.intellij.aop.psi;

import com.intellij.aop.AopBundle;
import consulo.language.InjectableLanguage;
import consulo.language.Language;
import consulo.language.file.LanguageFileType;

/**
 * @author peter
 */
public class AopPointcutExpressionLanguage extends Language implements InjectableLanguage {
  public static final AopPointcutExpressionLanguage INSTANCE = new AopPointcutExpressionLanguage();

  protected AopPointcutExpressionLanguage() {
    super("PointcutExpression");
  }

  @Override
  public LanguageFileType getAssociatedFileType() {
    return AopPointcutExpressionFileType.INSTANCE;
  }

  @Deprecated
  public static AopPointcutExpressionLanguage getInstance() {
    return INSTANCE;
  }

  @Override
  public String getDisplayName() {
    return AopBundle.message("inspection.group.display.name.aop");
  }
}
