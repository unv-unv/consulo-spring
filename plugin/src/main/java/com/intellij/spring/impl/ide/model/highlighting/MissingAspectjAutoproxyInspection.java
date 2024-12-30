/*
 * Copyright (c) 2000-2005 by JetBrains s.r.o. All Rights Reserved.
 * Use is subject to license terms.
 */
package com.intellij.spring.impl.ide.model.highlighting;

import com.intellij.aop.AopAdvisedElementsSearcher;
import com.intellij.aop.AopBundle;
import com.intellij.aop.AopProvider;
import com.intellij.aop.jam.AopConstants;
import com.intellij.java.language.psi.PsiAnnotation;
import com.intellij.java.language.psi.PsiClass;
import com.intellij.java.language.psi.PsiJavaFile;
import com.intellij.java.language.psi.PsiModifierList;
import com.intellij.spring.impl.ide.SpringBundle;
import com.intellij.spring.impl.ide.SpringModel;
import com.intellij.spring.impl.ide.aop.SpringAdvisedElementsSearcher;
import com.intellij.spring.impl.ide.aop.SpringAopProvider;
import com.intellij.spring.impl.ide.constants.SpringConstants;
import com.intellij.spring.impl.ide.model.SpringUtils;
import com.intellij.spring.impl.ide.model.xml.CommonSpringBean;
import com.intellij.spring.impl.ide.model.xml.beans.Beans;
import consulo.annotation.component.ExtensionImpl;
import consulo.application.util.NotNullLazyValue;
import consulo.language.editor.inspection.LocalQuickFix;
import consulo.language.editor.inspection.ProblemDescriptor;
import consulo.language.editor.inspection.ProblemHighlightType;
import consulo.language.editor.inspection.scheme.InspectionManager;
import consulo.language.psi.PsiFile;
import consulo.language.util.ModuleUtilCore;
import consulo.module.Module;
import consulo.xml.util.xml.DomFileElement;
import org.jetbrains.annotations.Nls;

import jakarta.annotation.Nonnull;
import java.util.List;

/**
 * @author peter
 */
@ExtensionImpl
public class MissingAspectjAutoproxyInspection extends AOPLocalInspectionTool {
  private final NotNullLazyValue<SpringAopProvider> mySpringAopProvider = new NotNullLazyValue<SpringAopProvider>() {
    @Nonnull
    protected SpringAopProvider compute() {
      for (final AopProvider provider : AopProvider.EXTENSION_POINT_NAME.getExtensionList()) {
        if (provider instanceof SpringAopProvider) {
          return (SpringAopProvider)provider;
        }
      }
      throw new AssertionError();
    }
  };

  @Override
  public boolean isEnabledByDefault() {
    return true;
  }

  public ProblemDescriptor[] checkFile(@Nonnull final PsiFile file, @Nonnull final InspectionManager manager, final boolean isOnTheFly) {
    if (file instanceof PsiJavaFile) {
      final Module module = ModuleUtilCore.findModuleForPsiElement(file);
      if (module == null || SpringUtils.isSpring25(module)) {
        return null;
      }

      for (final PsiClass aClass : ((PsiJavaFile)file).getClasses()) {
        final PsiModifierList modifierList = aClass.getModifierList();
        if (modifierList != null) {
          final PsiAnnotation annotation = modifierList.findAnnotation(AopConstants.ASPECT_ANNO);
          if (annotation != null) {
            final AopAdvisedElementsSearcher searcher = mySpringAopProvider.getValue().getAdvisedElementsSearcher(aClass);
            if (searcher instanceof SpringAdvisedElementsSearcher) {
              final List<SpringModel> models = ((SpringAdvisedElementsSearcher)searcher).getSpringModels();
              if (!models.isEmpty() && !isAspectJSupportEnabled(models)) {
                final LocalQuickFix[] fixes = models.isEmpty() ? LocalQuickFix.EMPTY_ARRAY : new LocalQuickFix[]{new EnableAspectJQuickFix(
                  models.get(0))};
                return new ProblemDescriptor[]{
                  manager.createProblemDescriptor(annotation.getNameReferenceElement(),
                                                  SpringBundle.message("aop.warning.aspectj.isnt.enabled"),
                                                  fixes,
                                                  ProblemHighlightType.GENERIC_ERROR_OR_WARNING)};
              }
            }
          }
        }
      }
    }

    return super.checkFile(file, manager, isOnTheFly);
  }

  @Nls
  @Nonnull
  public String getDisplayName() {
    return SpringBundle.message("missing.aspectj.autoproxy.inspection.display.name");
  }

  @Nonnull
  public String getShortName() {
    return "MissingAspectjAutoproxyInspection";
  }

  public static boolean isAspectJSupportEnabled(List<SpringModel> models) {
    for (final SpringModel model : models) {
      for (final DomFileElement<Beans> fileElement : model.getRoots()) {
        for (final CommonSpringBean springBean : SpringUtils.getChildBeans(fileElement.getRootElement(), false)) {
          final PsiClass beanClass = springBean.getBeanClass();
          if (beanClass != null && SpringConstants.ASPECTJ_AUTOPROXY_BEAN_CLASS.equals(beanClass.getQualifiedName())) {
            return true;
          }
        }
      }
    }
    return false;
  }
}
