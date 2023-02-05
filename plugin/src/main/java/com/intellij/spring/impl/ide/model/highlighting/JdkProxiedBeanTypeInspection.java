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

package com.intellij.spring.impl.ide.model.highlighting;

import com.intellij.aop.AopAdvisedElementsSearcher;
import com.intellij.aop.AopProvider;
import com.intellij.aop.jam.AopJavaAnnotator;
import com.intellij.aop.jam.AopLanguageInjector;
import com.intellij.jam.model.util.JamCommonUtil;
import com.intellij.java.language.psi.PsiClass;
import com.intellij.java.language.psi.PsiClassType;
import com.intellij.java.language.psi.PsiType;
import com.intellij.spring.impl.ide.SpringBundle;
import com.intellij.spring.impl.ide.aop.SpringAdvisedElementsSearcher;
import com.intellij.spring.impl.ide.model.xml.CommonSpringBean;
import com.intellij.spring.impl.ide.model.xml.aop.AopConfig;
import com.intellij.spring.impl.ide.model.xml.aop.AspectjAutoproxy;
import com.intellij.spring.impl.ide.model.xml.beans.Beans;
import consulo.annotation.component.ExtensionImpl;
import consulo.application.progress.ProgressManager;
import consulo.application.util.CachedValue;
import consulo.application.util.CachedValueProvider;
import consulo.application.util.CachedValuesManager;
import consulo.application.util.function.Processor;
import consulo.language.editor.annotation.HighlightSeverity;
import consulo.language.editor.inspection.LocalQuickFix;
import consulo.language.editor.inspection.ProblemDescriptor;
import consulo.language.psi.PsiModificationTracker;
import consulo.project.Project;
import consulo.util.collection.ContainerUtil;
import consulo.util.dataholder.Key;
import consulo.util.lang.StringUtil;
import consulo.xml.util.xml.DomElement;
import consulo.xml.util.xml.DomUtil;
import consulo.xml.util.xml.highlighting.DomElementAnnotationHolder;
import org.jetbrains.annotations.Nls;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * @author Dmitry Avdeev
 */
@ExtensionImpl
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

    interfaces.addAll(getInterfacesToReplaceClassWith(psiClass));

    if (!interfaces.isEmpty()) {
      String s = StringUtil.join(ContainerUtil.map(interfaces, PsiClass::getQualifiedName), ", ");
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
      psiClass.putUserData(REPLACE_CLASS,
                           classes = CachedValuesManager.getManager(psiClass.getProject())
                                                        .createCachedValue(new CachedValueProvider<Set<PsiClass>>() {
                                                          public Result<Set<PsiClass>> compute() {
                                                            ProgressManager.getInstance().checkCanceled();
                                                            for (final AopProvider provider : AopLanguageInjector.getAopProviders(psiClass)) {
                                                              final AopAdvisedElementsSearcher elementsSearcher =
                                                                provider.getAdvisedElementsSearcher(psiClass);
                                                              if (elementsSearcher instanceof SpringAdvisedElementsSearcher) {
                                                                final SpringAdvisedElementsSearcher searcher =
                                                                  (SpringAdvisedElementsSearcher)elementsSearcher;
                                                                if (searcher.isJdkProxyType() && isAdvised(psiClass)) {
                                                                  final Set<PsiClass> interfaces = new HashSet<PsiClass>();
                                                                  JamCommonUtil.processSuperClassList(psiClass,
                                                                                                      new consulo.ide.impl.idea.util.containers.ArrayListSet<PsiClass>(),
                                                                                                      new Processor<PsiClass>() {
                                                                                                        public boolean process(final PsiClass psiClass) {
                                                                                                          interfaces.addAll(Arrays.asList(
                                                                                                            psiClass.getInterfaces()));
                                                                                                          return true;
                                                                                                        }
                                                                                                      });
                                                                  return Result.create(interfaces,
                                                                                       PsiModificationTracker.OUT_OF_CODE_BLOCK_MODIFICATION_COUNT);
                                                                }
                                                              }
                                                            }
                                                            return Result.create(Collections.<PsiClass>emptySet(),
                                                                                 PsiModificationTracker.OUT_OF_CODE_BLOCK_MODIFICATION_COUNT);
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

  private static class TimeoutException extends RuntimeException {
  }
}
