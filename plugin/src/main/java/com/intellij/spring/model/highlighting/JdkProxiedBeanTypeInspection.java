/*
 * Copyright 2000-2009 JetBrains s.r.o.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.intellij.spring.model.highlighting;

import com.intellij.aop.AopAdvisedElementsSearcher;
import com.intellij.aop.AopProvider;
import com.intellij.aop.jam.AopJavaAnnotator;
import com.intellij.aop.jam.AopLanguageInjector;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.jam.model.util.JamCommonUtil;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.progress.ProcessCanceledException;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.util.AbstractProgressIndicatorBase;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiClassType;
import com.intellij.psi.PsiType;
import com.intellij.psi.util.CachedValue;
import com.intellij.psi.util.CachedValueProvider;
import com.intellij.psi.util.CachedValuesManager;
import com.intellij.psi.util.PsiModificationTracker;
import com.intellij.spring.SpringBundle;
import com.intellij.spring.aop.SpringAdvisedElementsSearcher;
import com.intellij.spring.model.xml.CommonSpringBean;
import com.intellij.spring.model.xml.aop.AopConfig;
import com.intellij.spring.model.xml.aop.AspectjAutoproxy;
import com.intellij.spring.model.xml.beans.Beans;
import com.intellij.util.Function;
import com.intellij.util.Processor;
import com.intellij.util.containers.ArrayListSet;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.DomUtil;
import com.intellij.util.xml.highlighting.DomElementAnnotationHolder;
import consulo.util.dataholder.Key;
import org.jetbrains.annotations.Nls;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * @author Dmitry Avdeev
 */
public class JdkProxiedBeanTypeInspection extends InjectionValueTypeInspection {
  private static final Key<CachedValue<Set<PsiClass>>> REPLACE_CLASS = Key.create("ReplaceClassWithInterfaces");

  @Override
  protected void checkBeanClass(@Nonnull CommonSpringBean springBean,
                                @Nonnull PsiType psiType,
                                final DomElement annotatedElement,
                                @Nonnull DomElementAnnotationHolder holder) {


    final PsiClass psiClass = psiType instanceof PsiClassType ? ((PsiClassType)psiType).resolve() : null;
    if (psiClass == null || psiClass.isInterface()) {
      return;
    }
    final Set<PsiClass> interfaces = new HashSet<PsiClass>();

    final long startTime = System.currentTimeMillis();
    try {
      ProgressManager.getInstance().runProcess(new Runnable() {
        public void run() {
          ProgressManager.getInstance().checkCanceled();
          interfaces.addAll(getInterfacesToReplaceClassWith(psiClass));
        }
      }, new AbstractProgressIndicatorBase() {

        long myCount;
        @Override
        public void checkCanceled() throws ProcessCanceledException {

          if (!isCanceled() && ((++myCount % 1000 == 0) && (System.currentTimeMillis() - startTime > 200))) {
            throw new TimeoutException();
          }
          super.checkCanceled();
        }
      });
    }
    catch (TimeoutException e) {
      holder.createProblem(annotatedElement, ProblemHighlightType.INFO, "Could not determine JDK-proxied bean type", null, createFixes(annotatedElement));
      return;
    }

    if (!interfaces.isEmpty()) {
      String s = StringUtil.join(ContainerUtil.map(interfaces, new Function<PsiClass, String>() {
        public String fun(PsiClass psiClass) {
          return psiClass.getQualifiedName();
        }
      }), ", ");
      holder.createProblem(annotatedElement, HighlightSeverity.GENERIC_SERVER_ERROR_OR_WARNING,
                           SpringBundle.message("jdk.proxy.intercepts.class", psiType.getCanonicalText(), s),
                           createFixes(annotatedElement));
    }
  }

  private static LocalQuickFix[] createFixes(DomElement annotatedElement) {
    return EnableAspectJQuickFix.isSchemaStyle(DomUtil.getFileElement(annotatedElement).getRootElement().getXmlTag()) ?
                                  new LocalQuickFix[]{new SwitchToCglibProxyingFix(annotatedElement)} : new LocalQuickFix[0];
  }

  private static Set<PsiClass> getInterfacesToReplaceClassWith(final PsiClass psiClass) {
    CachedValue<Set<PsiClass>> classes = psiClass.getUserData(REPLACE_CLASS);
    if (classes == null) {
      psiClass.putUserData(REPLACE_CLASS, classes = CachedValuesManager.getManager(psiClass.getProject()).createCachedValue(new CachedValueProvider<Set<PsiClass>>() {
        public Result<Set<PsiClass>> compute() {
          ProgressManager.getInstance().checkCanceled();
          for (final AopProvider provider : AopLanguageInjector.getAopProviders(psiClass)) {
            final AopAdvisedElementsSearcher elementsSearcher = provider.getAdvisedElementsSearcher(psiClass);
            if (elementsSearcher instanceof SpringAdvisedElementsSearcher) {
              final SpringAdvisedElementsSearcher searcher = (SpringAdvisedElementsSearcher)elementsSearcher;
              if (searcher.isJdkProxyType() && isAdvised(psiClass)) {
                final Set<PsiClass> interfaces = new HashSet<PsiClass>();
                JamCommonUtil.processSuperClassList(psiClass, new ArrayListSet<PsiClass>(), new Processor<PsiClass>() {
                  public boolean process(final PsiClass psiClass) {
                    interfaces.addAll(Arrays.asList(psiClass.getInterfaces()));
                    return true;
                  }
                });
                return Result.create(interfaces, PsiModificationTracker.OUT_OF_CODE_BLOCK_MODIFICATION_COUNT);
              }
            }
          }
          return Result.create(Collections.<PsiClass>emptySet(), PsiModificationTracker.OUT_OF_CODE_BLOCK_MODIFICATION_COUNT);
        }
      }, false));
    }
    return classes.getValue();
  }


  @Nls
  @Nonnull
  @Override
  public String getDisplayName() {
    return "JDK-proxied beans type checking";
  }

  @Nonnull
  @Override
  public String getShortName() {
    return "JdkProxiedBeanTypeInspection";
  }

  private static boolean isAdvised(final PsiClass psiClass) {
    return !AopJavaAnnotator.getBoundAdvices(psiClass).isEmpty() ||
           !AopJavaAnnotator.getBoundIntroductions(psiClass).isEmpty();
  }

  private static class SwitchToCglibProxyingFix implements LocalQuickFix {
    private final DomElement myElement;

    public SwitchToCglibProxyingFix(final DomElement element) {
      myElement = element;
    }

    @Nonnull
    public String getName() {
      return SpringBundle.message("use.cglib.proxying");
    }

    @Nonnull
    public String getFamilyName() {
      return getName();
    }

    public void applyFix(@Nonnull final Project project, @Nonnull final ProblemDescriptor descriptor) {
      Beans beans = (Beans)DomUtil.getFileElement(myElement).getRootElement();
       final List<AopConfig> configs = DomUtil.getDefinedChildrenOfType(beans, AopConfig.class);
      if (!configs.isEmpty()) {
        configs.get(0).getProxyTargetClass().setValue(Boolean.TRUE);
        return;
      }

      List<AspectjAutoproxy> autoproxyList = DomUtil.getDefinedChildrenOfType(beans, AspectjAutoproxy.class);
      if (autoproxyList.isEmpty()) {
        EnableAspectJQuickFix.addAspectjAutoproxy(beans.getXmlTag());
        autoproxyList = DomUtil.getDefinedChildrenOfType(beans, AspectjAutoproxy.class);
      }
      autoproxyList.get(0).getProxyTargetClass().setValue(Boolean.TRUE);
    }
  }

  private static class TimeoutException extends RuntimeException {}
}
