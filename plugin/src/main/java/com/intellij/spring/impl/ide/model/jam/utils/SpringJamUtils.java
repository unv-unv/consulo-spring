package com.intellij.spring.impl.ide.model.jam.utils;

import com.intellij.java.language.psi.PsiClass;
import com.intellij.java.language.psi.PsiJavaPackage;
import com.intellij.java.language.psi.PsiMethod;
import com.intellij.spring.impl.ide.SpringModel;
import com.intellij.spring.impl.ide.model.SpringUtils;
import com.intellij.spring.impl.ide.model.context.ComponentScan;
import com.intellij.spring.impl.ide.model.jam.SpringJamModel;
import com.intellij.spring.impl.ide.model.jam.javaConfig.JavaSpringConfigurationElement;
import com.intellij.spring.impl.ide.model.jam.javaConfig.SpringJamElement;
import com.intellij.spring.impl.ide.model.jam.javaConfig.SpringJavaBean;
import com.intellij.spring.impl.ide.model.jam.stereotype.SpringStereotypeElement;
import com.intellij.spring.impl.ide.model.xml.CommonSpringBean;
import consulo.annotation.access.RequiredReadAction;
import consulo.language.util.ModuleUtilCore;
import consulo.module.Module;
import consulo.util.lang.StringUtil;
import consulo.xml.psi.xml.XmlTag;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class SpringJamUtils {
  private SpringJamUtils() {
  }

  public static void processAllStereotypeJavaBeans(final SpringModel springModel, Consumer<CommonSpringBean> consumer) {
    final consulo.module.Module module = springModel.getModule();
    if (module != null) {
      List<? extends ComponentScan> scanBeans = springModel.getComponentScans();
      if (scanBeans.size() > 0) {
        List<PsiJavaPackage> psiPackages = getScannedPackages(scanBeans);
        if (psiPackages.isEmpty()) {
          return;
        }
        SpringJamModel javaModel = SpringJamModel.getModel(module);

        filterStereotypeComponents(javaModel.getAllStereotypeComponents(), psiPackages, consumer);
      }
    }
  }

  public static void processConfigurations(final SpringModel springModel, Consumer<SpringJamElement> consumer) {
    final consulo.module.Module module = springModel.getModule();
    if (module == null) {
      return;
    }

    List<? extends ComponentScan> scanBeans = springModel.getComponentScans();
    if (scanBeans.size() > 0) {
      List<PsiJavaPackage> psiPackages = getScannedPackages(scanBeans);
      if (psiPackages.isEmpty()) {
        return;
      }

      List<SpringJamElement> components = SpringJamModel.getModel(module).getConfigurations();

      filterJavaConfigurations(components, psiPackages, consumer);
    }
  }

  private static void filterStereotypeComponents(List<? extends SpringStereotypeElement> components,
                                                 List<PsiJavaPackage> psiPackages,
                                                 Consumer<CommonSpringBean> consumer) {
    for (SpringStereotypeElement component : components) {
      final PsiClass psiClass = component.getBeanClass();

      if (isInPackage(psiPackages, psiClass)) {
        consumer.accept(component);
      }
    }
  }

  private static void filterJavaConfigurations(List<SpringJamElement> javaConfigurations,
                                               List<PsiJavaPackage> psiPackages,
                                               Consumer<SpringJamElement> consumer) {
    for (SpringJamElement component : javaConfigurations) {
      final PsiClass psiClass = component.getPsiClass();
      if (isInPackage(psiPackages, psiClass)) {
        consumer.accept(component);
      }
    }
  }

  private static boolean isInPackage(List<PsiJavaPackage> psiPackages, @Nullable PsiClass psiClass) {
    if (psiClass != null) {
      final String qualifiedName = psiClass.getQualifiedName();
      if (qualifiedName != null) {
        for (PsiJavaPackage psiPackage : psiPackages) {
          if (StringUtil.startsWithConcatenation(qualifiedName, psiPackage.getQualifiedName(), ".")) {
            return true;
          }
        }
      }
    }
    return false;
  }

  private static List<PsiJavaPackage> getScannedPackages(final List<? extends ComponentScan> scanBeans) {
    final ArrayList<PsiJavaPackage> list = new ArrayList<>(scanBeans.size());
    for (ComponentScan scanBean : scanBeans) {
      list.addAll(scanBean.getBasePackages());
    }
    return list;
  }

  @Nonnull
  public static List<SpringJavaBean> findBeanReferences(final CommonSpringBean springBean) {
    List<SpringJavaBean> extBeans = new ArrayList<>();
    final Set<String> strings = SpringUtils.getAllBeanNames(springBean);

    if (strings.size() > 0) {
      final XmlTag element = springBean.getXmlTag();
      if (element != null) {
        final Module module = ModuleUtilCore.findModuleForPsiElement(element);

        if (module != null) {
          for (SpringJamElement javaConfiguration : SpringJamModel.getModel(module).getConfigurations()) {
            if (javaConfiguration instanceof JavaSpringConfigurationElement) {
              for (SpringJavaBean externalBean : javaConfiguration.getBeans()) {
                final PsiMethod psiMethod = externalBean.getPsiElement();
                if (psiMethod != null && strings.contains(psiMethod.getName())) {
                  extBeans.add(externalBean);
                }
              }
            }
          }
        }
      }
    }
    return extBeans;
  }

  public static boolean isBean(final PsiMethod psiMethod) {
    return getBeanByMethod(psiMethod) != null;
  }

  @Nullable
  @RequiredReadAction
  public static SpringJavaBean getBeanByMethod(final PsiMethod psiMethod) {
    final consulo.module.Module module = psiMethod.getModule();
    if (module != null) {
      for (SpringJamElement javaConfiguration : SpringJamModel.getModel(module).getConfigurations()) {
        if (javaConfiguration instanceof JavaSpringConfigurationElement) {
          if (psiMethod.getContainingFile().equals(javaConfiguration.getPsiClass().getContainingFile())) {
            for (SpringJavaBean externalBean : javaConfiguration.getBeans()) {
              if (psiMethod.equals(externalBean.getPsiElement())) {
                return externalBean;
              }
            }
          }
        }
      }
    }
    return null;
  }
}
