/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */
package com.intellij.aop.psi;

import com.intellij.aop.AopAdvisedElementsSearcher;
import com.intellij.aop.jam.AopConstants;
import com.intellij.java.language.psi.JavaPsiFacade;
import com.intellij.java.language.psi.PsiClass;
import com.intellij.java.language.psi.PsiJavaPackage;
import consulo.application.util.function.Processor;
import consulo.language.psi.PsiManager;
import consulo.language.psi.scope.GlobalSearchScope;
import consulo.logging.Logger;
import consulo.util.collection.ContainerUtil;
import consulo.util.lang.StringUtil;
import org.jetbrains.annotations.TestOnly;

import java.util.ArrayList;
import java.util.List;

/**
 * @author peter
 */
public class AllAdvisedElementsSearcher extends AopAdvisedElementsSearcher {
  private static final Logger LOG = Logger.getInstance(AllAdvisedElementsSearcher.class);
  private GlobalSearchScope myScope;

  @TestOnly
  public AllAdvisedElementsSearcher(final PsiManager manager) {
    this(manager, GlobalSearchScope.allScope(manager.getProject()));

  }

  public AllAdvisedElementsSearcher(final PsiManager manager, final GlobalSearchScope scope) {
    super(manager);
    myScope = scope;
  }

  public boolean process(final Processor<PsiClass> processor) {
    final PsiJavaPackage psiPackage = JavaPsiFacade.getInstance(getManager().getProject()).findPackage("");
    return psiPackage == null || processPackage(processor, psiPackage, new ArrayList<PsiJavaPackage>());

  }

  @Override
  public boolean isAcceptable(final PsiClass psiClass) {
    return true;
  }

  private boolean processPackage(final Processor<PsiClass> processor, final PsiJavaPackage psiPackage, final List<PsiJavaPackage> visited) {
    if (visited.contains(psiPackage)) {
      LOG.error("Circular package structure:\n" + StringUtil.join(visited,
                                                                  psiPackage1 -> psiPackage1.getQualifiedName() + " === " + StringUtil.join(
                                                                    psiPackage1.getDirectories(),
                                                                    psiDirectory -> psiDirectory.getVirtualFile().getPath(), "; "), "\n"));
    }

    visited.add(psiPackage);
    if (!ContainerUtil.process(psiPackage.getClasses(myScope), new Processor<PsiClass>() {
      public boolean process(final PsiClass psiClass) {
        return psiClass.getModifierList().findAnnotation(AopConstants.ASPECT_ANNO) != null || processor.process(psiClass);
      }
    })) {
      return false;
    }
    for (final PsiJavaPackage aPackage : psiPackage.getSubPackages(myScope)) {
      if (!processPackage(processor, aPackage, new ArrayList<PsiJavaPackage>(visited))) return false;
    }
    return true;
  }

}
