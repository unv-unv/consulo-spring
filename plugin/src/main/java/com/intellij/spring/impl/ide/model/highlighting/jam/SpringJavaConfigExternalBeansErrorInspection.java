package com.intellij.spring.impl.ide.model.highlighting.jam;

import com.intellij.java.language.psi.*;
import com.intellij.spring.impl.ide.SpringBundle;
import com.intellij.spring.impl.ide.model.jam.javaConfig.JavaConfigConfiguration;
import com.intellij.spring.impl.ide.model.jam.javaConfig.SpingJamElement;
import com.intellij.spring.impl.ide.model.jam.javaConfig.SpringJavaExternalBean;
import com.intellij.spring.impl.ide.model.jam.utils.SpringJamUtils;
import com.intellij.spring.impl.ide.model.xml.beans.SpringBaseBeanPointer;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.editor.inspection.ProblemsHolder;
import consulo.language.editor.rawHighlight.HighlightDisplayLevel;
import consulo.module.Module;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;

import javax.annotation.Nonnull;
import java.util.List;

@ExtensionImpl
public class SpringJavaConfigExternalBeansErrorInspection extends SpringJavaConfigInspectionBase {
  protected void checkJavaConfiguration(final SpingJamElement javaConfiguration, final Module module, final ProblemsHolder holder) {
    if (javaConfiguration instanceof JavaConfigConfiguration) {
      checkExternalBean((JavaConfigConfiguration)javaConfiguration, holder);
    }
  }

  private static void checkExternalBean(final JavaConfigConfiguration configuration, final ProblemsHolder holder) {
    final List<? extends SpringJavaExternalBean> list = configuration.getExternalBeans();
    if (list.size() > 0) {
      for (SpringJavaExternalBean externalBean : list) {
        checkExternalBeanExist(externalBean, holder);
        checkExternalBeanType(externalBean, holder);
      }
    }
  }

  private static void checkExternalBeanType(final SpringJavaExternalBean externalBean, final ProblemsHolder holder) {
    final List<SpringBaseBeanPointer> beans = SpringJamUtils.findExternalBeans(externalBean.getPsiElement());
    if (beans.size() == 1) {
      SpringBaseBeanPointer springBean = beans.get(0);
      final PsiMethod psiMethod = externalBean.getPsiElement();
      if (psiMethod != null) {
        final PsiType returnType = psiMethod.getReturnType();

        if (returnType instanceof PsiClassType) {

          final PsiClass beanClass = springBean.getBeanClass();
          if (beanClass != null) {
            final PsiClassType beanClassType = JavaPsiFacade.getInstance(beanClass.getProject()).getElementFactory().createType(beanClass);
            final PsiClassType externalBeanClassType = (PsiClassType)returnType;
            if (!externalBeanClassType.isAssignableFrom(beanClassType)) {
              holder.registerProblem(psiMethod.getReturnTypeElement(),
                                     SpringBundle.message("spring.java.configuration.inspection.incorrect.return.type.of.external.bean",
                                                          beanClassType.getClassName()));
            }
          }
        }
        else {
          holder.registerProblem(psiMethod.getReturnTypeElement(),
                                 SpringBundle.message("spring.java.configuration.inspection.class.type.expected"));
        }
      }
    }
  }

  private static void checkExternalBeanExist(final SpringJavaExternalBean externalBean, final ProblemsHolder holder) {
    final PsiMethod member = externalBean.getPsiElement();
    if (member != null) {
      if (SpringJamUtils.findExternalBeans(externalBean.getPsiElement()).size() == 0) {
        final PsiIdentifier psiIdentifier = member.getNameIdentifier();
        holder.registerProblem(psiIdentifier, SpringBundle.message("spring.java.configuration.inspection.cannot.find.external.bean"));
      }
    }
  }

  @Nls
  @Nonnull
  public String getDisplayName() {
    return SpringBundle.message("spring.java.configuration.inspection.name");
  }

  @NonNls
  @Nonnull
  public String getShortName() {
    return "SpringJavaConfigExternalBeansErrorInspection";
  }

  @Nonnull
  public HighlightDisplayLevel getDefaultLevel() {
    return HighlightDisplayLevel.WARNING;
  }
}
