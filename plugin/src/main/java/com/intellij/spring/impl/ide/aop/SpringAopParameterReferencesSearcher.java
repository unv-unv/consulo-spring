/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */
package com.intellij.spring.impl.ide.aop;

import com.intellij.java.language.psi.PsiParameter;
import com.intellij.spring.impl.ide.SpringModel;
import com.intellij.spring.impl.ide.model.SpringUtils;
import consulo.annotation.component.ExtensionImpl;
import consulo.application.ApplicationManager;
import consulo.application.util.function.Computable;
import consulo.application.util.function.Processor;
import consulo.content.scope.SearchScope;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiReference;
import consulo.language.psi.scope.LocalSearchScope;
import consulo.language.psi.search.ReferencesSearch;
import consulo.language.psi.search.ReferencesSearchQueryExecutor;
import consulo.language.util.ModuleUtilCore;
import consulo.module.Module;
import consulo.xml.psi.xml.XmlFile;

import java.util.HashSet;
import java.util.Set;

/**
 * @author peter
 */
@ExtensionImpl
public class SpringAopParameterReferencesSearcher implements ReferencesSearchQueryExecutor {
  public boolean execute(final ReferencesSearch.SearchParameters queryParameters, final Processor<? super PsiReference> consumer) {
    //noinspection AutoUnboxing
    return ApplicationManager.getApplication().runReadAction(new Computable<Boolean>() {
      public Boolean compute() {
        final SearchScope scope = queryParameters.getScope();
        if (scope instanceof MyLocalSearchScope) {
          return true; //recursive call
        }

        final PsiElement element = queryParameters.getElementToSearch();
        if (element instanceof PsiParameter) {
          final Module module = ModuleUtilCore.findModuleForPsiElement(element);
          if (module != null) {
            final Set<XmlFile> visited = new HashSet<XmlFile>();
            for (final SpringModel model : SpringUtils.getNonEmptySpringModels(module)) {
              for (final XmlFile xmlFile : model.getConfigFiles()) {
                if (!visited.contains(xmlFile)) {
                  visited.add(xmlFile);
                  final LocalSearchScope localScope = (LocalSearchScope)new LocalSearchScope(xmlFile).intersectWith(scope);
                  if (localScope.getScope().length > 0) {
                    if (!ReferencesSearch.search(element, new MyLocalSearchScope(localScope), true).forEach(consumer)) return false;
                  }
                }
              }
            }
          }

        }
        return true;
      }
    });
  }

  private static class MyLocalSearchScope extends LocalSearchScope {
    public MyLocalSearchScope(final LocalSearchScope scope) {
      super(scope.getScope(), scope.getDisplayName(), scope.isIgnoreInjectedPsi());
    }
  }
}
