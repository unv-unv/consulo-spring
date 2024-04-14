package com.intellij.spring.impl.ide.model.highlighting.jam;

import com.intellij.java.language.codeInsight.AnnotationUtil;
import com.intellij.java.language.psi.*;
import com.intellij.spring.impl.ide.SpringBundle;
import com.intellij.spring.impl.ide.constants.SpringAnnotationsConstants;
import com.intellij.spring.impl.ide.model.jam.javaConfig.SpringJavaBean;
import com.intellij.spring.impl.ide.model.jam.javaConfig.SpingJamElement;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.editor.inspection.ProblemsHolder;
import consulo.language.editor.rawHighlight.HighlightDisplayLevel;
import consulo.module.Module;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;

import javax.annotation.Nonnull;

@ExtensionImpl
public class SpringJavaConfigInconsistencyInspection extends SpringJavaConfigInspectionBase {

  @Override
  protected void checkClass(PsiClass aClass, ProblemsHolder holder, @Nonnull Module module) {
    SpingJamElement configuration = getJavaConfiguration(aClass, module);

    if (configuration != null) {
      checkJavaConfiguration(configuration, module, holder);
    }
    else {
      for (PsiMethod psiMethod : aClass.getMethods()) {
        PsiAnnotation beanAnnotation = AnnotationUtil.findAnnotation(psiMethod, SpringAnnotationsConstants.JAVA_CONFIG_BEAN_ANNOTATION,
                                                                     SpringAnnotationsConstants.JAVA_SPRING_BEAN_ANNOTATION);
        if (beanAnnotation != null) {
          holder.registerProblem(beanAnnotation, SpringBundle.message("java.config.bean.must.be.declared.inside.configuration"));
        }
      }
    }
  }

  protected void checkJavaConfiguration(final SpingJamElement javaConfiguration, final Module module, final ProblemsHolder holder) {
    checkJavaConfigurationClass(javaConfiguration, holder);

    for (SpringJavaBean springJavaBean : javaConfiguration.getBeans()) {
      checkJavaBeanInconsistency(springJavaBean, holder);
    }
  }

  private static void checkJavaConfigurationClass(final SpingJamElement configuration, final ProblemsHolder holder) {
    PsiClass psiClass = configuration.getPsiElement();

    checkConstructor(psiClass, configuration, holder);
    checkNonFinal(configuration, holder, psiClass);
  }

  private static void checkNonFinal(SpingJamElement configuration, ProblemsHolder holder, PsiClass psiClass) {
    if (psiClass.getModifierList().hasModifierProperty(PsiModifier.FINAL)) {
      holder.registerProblem(configuration.getAnnotation(), SpringBundle.message("java.configuration.cannot.be.final"));
    }
  }

  private static void checkConstructor(PsiClass psiClass, SpingJamElement configuration, ProblemsHolder holder) {
    PsiMethod[] constructors = psiClass.getConstructors();

    if (constructors.length != 0 && !hasDefaultConstructor(constructors)) {
      holder.registerProblem(configuration.getAnnotation(), SpringBundle.message("java.configuration.must.have.default.constructor"));
    }
    for (PsiMethod constructor : constructors) {
      if (AnnotationUtil.isAnnotated(constructor, SpringAnnotationsConstants.AUTOWIRED_ANNOTATION, false)) {
        holder.registerProblem(constructor.getNameIdentifier(), SpringBundle.message("java.configuration.autowired.constructor.param"));
      }

    }
  }

  private static boolean hasDefaultConstructor(PsiMethod[] constructors) {
    for (PsiMethod constructor : constructors) {
      if (constructor.hasModifierProperty(PsiModifier.PUBLIC) && constructor.getParameterList().getParametersCount() == 0) {
        return true;
      }
    }
    return false;
  }

  private static void checkJavaBeanInconsistency(SpringJavaBean springJavaBean, ProblemsHolder holder) {
    checkReturnType(springJavaBean, holder);
    checkNonFinal(springJavaBean, holder);
    checkNonPrivate(springJavaBean, holder);
    checkNoArguments(springJavaBean, holder);
  }

  private static void checkNoArguments(SpringJavaBean springJavaBean, ProblemsHolder holder) {
    if (springJavaBean.getPsiElement().getParameterList().getParametersCount() > 0) {
      holder.registerProblem(springJavaBean.getPsiAnnotation(), SpringBundle.message("java.config.bean.method.cannot.has.arguments"));
    }
  }

  private static void checkNonPrivate(SpringJavaBean springJavaBean, ProblemsHolder holder) {
    if (springJavaBean.getPsiElement().getModifierList().hasExplicitModifier(PsiModifier.PRIVATE)) {
      holder.registerProblem(springJavaBean.getPsiAnnotation(), SpringBundle.message("java.config.bean.method.cannot.be.private"));
    }
  }

  private static void checkNonFinal(SpringJavaBean springJavaBean, ProblemsHolder holder) {
    if (springJavaBean.getPsiElement().getModifierList().hasExplicitModifier(PsiModifier.FINAL)) {
      holder.registerProblem(springJavaBean.getPsiAnnotation(), SpringBundle.message("java.config.bean.method.cannot.be.final"));
    }
  }

  private static void checkReturnType(SpringJavaBean springJavaBean, ProblemsHolder holder) {
    if (springJavaBean.getPsiElement().getReturnType().equals(PsiType.VOID)) {
      holder.registerProblem(springJavaBean.getPsiAnnotation(), SpringBundle.message("java.config.bean.method.cannot.return.void"));
    }
  }

  @Nls
  @Nonnull
  public String getDisplayName() {
    return SpringBundle.message("spring.java.configuration.inconsistency.inspection.name");
  }

  @NonNls
  @Nonnull
  public String getShortName() {
    return "SpringJavaConfigInconsistencyInspection";
  }

  @Nonnull
  public HighlightDisplayLevel getDefaultLevel() {
    return HighlightDisplayLevel.ERROR;
  }
}