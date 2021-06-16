/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */
package com.intellij.aop.psi;

import com.intellij.openapi.util.Ref;
import com.intellij.psi.PsiJavaPackage;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiType;
import com.intellij.util.CommonProcessors;
import com.intellij.util.Processor;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;

/**
 * @author peter
*/
public class AndPsiTypePattern extends AopPsiTypePattern {
  private final AopPsiTypePattern[] myPatterns;

  public AndPsiTypePattern(final AopPsiTypePattern... patterns) {
    myPatterns = patterns;
  }

  public AopPsiTypePattern[] getPatterns() {
    return myPatterns;
  }

  public boolean accepts(@Nonnull final PsiType type) {
    for (final AopPsiTypePattern typePattern : myPatterns) {
      if (!typePattern.accepts(type)) return false;
    }
    return true;
  }

  public boolean processPackages(final PsiManager manager, final Processor<PsiJavaPackage> processor) {
    final Ref<Set<PsiJavaPackage>> set = Ref.create(new HashSet<PsiJavaPackage>());
    myPatterns[0].processPackages(manager, new CommonProcessors.CollectProcessor<PsiJavaPackage>(set.get()));
    for (int i = 1; i < myPatterns.length; i++) {
      AopPsiTypePattern pattern = myPatterns[i];
      final Set<PsiJavaPackage> all = set.get();
      set.set(new HashSet<PsiJavaPackage>());
      pattern.processPackages(manager, new Processor<PsiJavaPackage>() {
        public boolean process(final PsiJavaPackage psiPackage) {
          if (all.contains(psiPackage)) {
            set.get().add(psiPackage);
          }
          return true;
        }
      });
    }
    for (final PsiJavaPackage psiPackage : set.get()) {
      if (!processor.process(psiPackage)) return false;
    }
    return true;
  }
}
