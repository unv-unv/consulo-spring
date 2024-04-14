package com.intellij.spring.impl.ide.model;

import com.intellij.java.language.psi.JavaPsiFacade;
import com.intellij.java.language.psi.PsiClass;
import com.intellij.spring.impl.ide.SpringModel;
import com.intellij.spring.impl.ide.model.xml.CommonSpringBean;
import com.intellij.spring.impl.ide.model.xml.beans.SpringBaseBeanPointer;
import com.intellij.spring.impl.ide.model.xml.context.AnnotationConfig;
import com.intellij.spring.impl.ide.model.xml.context.DomComponentScan;
import consulo.language.psi.scope.GlobalSearchScope;
import consulo.module.Module;
import org.jetbrains.annotations.NonNls;

import javax.annotation.Nonnull;

/**
 * User: Sergey.Vasiliev
 */
public class SpringAnnotationConfigUtils {

  @NonNls public static String AUTOWIRED_ANNOTATION_BPP =
      "org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor"; // BPP - BeanPostProcessor :)
  @NonNls public static String REQUIRED_ANNOTATION_BPP = "org.springframework.beans.factory.annotation.RequiredAnnotationBeanPostProcessor";
  @NonNls public static String COMMON_ANNOTATION_BPP = "org.springframework.context.annotation.CommonAnnotationBeanPostProcessor";
  @NonNls public static String PERSISTENCE_ANNOTATION_BPP = "org.springframework.orm.jpa.support.PersistenceAnnotationBeanPostProcessor";

  private SpringAnnotationConfigUtils() {
  }

  // <context:annotation-config/> : implicitly registered post-processors include AutowiredAnnotationBeanPostProcessor, CommonAnnotationBeanPostProcessor, PersistenceAnnotationBeanPostProcessor, RequiredAnnotationBeanPostProcessor.
  public static boolean containsAnnotationConfigBean(@Nonnull final SpringModel springModel) {
    for (SpringBaseBeanPointer beanPointer : springModel.getAllDomBeans()) {
      final CommonSpringBean domSpringBean = beanPointer.getSpringBean();
      if (domSpringBean instanceof AnnotationConfig) {
        return true;
      }
    }
    return false;
  }

  // <context:annotation-config/> : implicitly registered post-processors include AutowiredAnnotationBeanPostProcessor, CommonAnnotationBeanPostProcessor, PersistenceAnnotationBeanPostProcessor, RequiredAnnotationBeanPostProcessor.
  public static boolean containsComponentScanBean(@Nonnull final SpringModel springModel) {
    for (SpringBaseBeanPointer beanPointer : springModel.getAllDomBeans()) {
      final CommonSpringBean domSpringBean = beanPointer.getSpringBean();
      if (domSpringBean instanceof DomComponentScan) {
        return true;
      }
    }
    return false;
  }

  public static boolean containsAutowiredAnnotationBeanPostProcessor(@Nonnull final SpringModel springModel) {
    return isBeanExists(springModel, AUTOWIRED_ANNOTATION_BPP) || containsAnnotationConfigBean(springModel) || containsComponentScanBean(springModel);
  }

  public static boolean containsRequiredAnnotationBeanPostProcessor(@Nonnull final SpringModel springModel) {
    return isBeanExists(springModel, REQUIRED_ANNOTATION_BPP) || containsAnnotationConfigBean(springModel);
  }

  public static boolean containsCommonAnnotationBeanPostProcessor(@Nonnull final SpringModel springModel) {
    return isBeanExists(springModel, COMMON_ANNOTATION_BPP) || containsAnnotationConfigBean(springModel) || containsComponentScanBean(springModel);
  }

  public static boolean containsPersistenceAnnotationBeanPostProcessor(@Nonnull final SpringModel springModel) {
    return isBeanExists(springModel, PERSISTENCE_ANNOTATION_BPP) || containsAnnotationConfigBean(springModel);
  }

  private static boolean isBeanExists(final SpringModel springModel, final String qualifiedName) {
    final Module module = springModel.getModule();
    if (module != null) {
      final PsiClass psiClass = JavaPsiFacade.getInstance(module.getProject())
                                             .findClass(qualifiedName, GlobalSearchScope.moduleWithDependenciesAndLibrariesScope(module, false));
      if (psiClass != null) {
        return springModel.findBeansByPsiClass(psiClass).size() > 0;
      }
    }
    return false;
  }
}
