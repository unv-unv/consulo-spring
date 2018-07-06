/*
 * Copyright (c) 2000-2005 by JetBrains s.r.o. All Rights Reserved.
 * Use is subject to license terms.
 */
package com.intellij.aop.psi;

import java.util.Set;

import javax.annotation.Nonnull;

import org.jetbrains.annotations.NonNls;
import com.intellij.aop.AopAdvisedElementsSearcher;
import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionResult;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.spring.aop.SpringAdvisedElementsSearcher;
import com.intellij.spring.model.SpringUtils;
import com.intellij.util.Consumer;
import com.intellij.util.ProcessingContext;
import consulo.codeInsight.completion.CompletionProvider;

/**
 * @author peter
 */
public class SpringAopCompletionContributor extends CompletionContributor{
  public static final @NonNls String[] SPRING20_AOP_POINTCUTS = {"execution", "target", "this", "within", "@target", "@within", "@annotation", "args", "@args"};

  public SpringAopCompletionContributor() {
    extend(CompletionType.BASIC, AopCompletionData.POINTCUT_PATTERN, new CompletionProvider() {
      public void addCompletions(@Nonnull final CompletionParameters parameters, final ProcessingContext context, @Nonnull final CompletionResultSet result) {
        final AopPointcutExpressionFile file = (AopPointcutExpressionFile)parameters.getPosition().getContainingFile();
        final AopAdvisedElementsSearcher searcher = file.getAopModel().getAdvisedElementsSearcher();
        final boolean isSpring = searcher instanceof SpringAdvisedElementsSearcher;
        if (isSpring) {
          for (final String pointcut : SPRING20_AOP_POINTCUTS) {
            result.addElement(AopCompletionData.createPointcutDesignatorElement(pointcut));
          }

          final Module module = ModuleUtil.findModuleForPsiElement(parameters.getPosition());
          if (module != null && SpringUtils.isSpring25(module)) {
            result.addElement(AopCompletionData.createPointcutDesignatorElement("bean"));
          }
        }

        final Set<String> designators = AopCompletionData.getAllPointcutDesignators();
        result.runRemainingContributors(parameters, new Consumer<CompletionResult>() {
          public void consume(final CompletionResult r) {
            LookupElement lookupElement = r.getLookupElement();
            if (!isSpring || !designators.contains(lookupElement.getLookupString())) {
              result.addElement(lookupElement);
            }
          }
        });
      }
    });
  }

}
