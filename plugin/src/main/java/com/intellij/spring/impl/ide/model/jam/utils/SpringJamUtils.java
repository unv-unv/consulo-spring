package com.intellij.spring.impl.ide.model.jam.utils;

import com.intellij.java.language.codeInsight.AnnotationUtil;
import com.intellij.java.language.psi.*;
import com.intellij.spring.impl.ide.SpringModel;
import com.intellij.spring.impl.ide.constants.SpringAnnotationsConstants;
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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class SpringJamUtils {
  private SpringJamUtils() {
  }

  @Nonnull
  public static List<SpringStereotypeElement> getAllStereotypeJavaBeans(final SpringModel springModel) {
    List<SpringStereotypeElement> allStereotypes = new ArrayList<>();
    final consulo.module.Module module = springModel.getModule();
    if (module != null) {
      List<? extends ComponentScan> scanBeans = springModel.getComponentScans();
      if (scanBeans.size() > 0) {
        List<PsiJavaPackage> psiPackages = getScannedPackages(scanBeans);
        if (psiPackages.isEmpty()) {
          return Collections.emptyList();
        }
        SpringJamModel javaModel = SpringJamModel.getModel(module);

        List<SpringStereotypeElement> components = getAllStereotypeComponents(javaModel);
        return filterStereotypeComponents(components, psiPackages);
      }
    }

    return allStereotypes;
  }

  @Nonnull
  public static List<SpringJamElement> getJavaConfigurations(final SpringModel springModel) {
    List<SpringJamElement> javaConfigurations = new ArrayList<>();
    final consulo.module.Module module = springModel.getModule();
    if (module == null) {
      return List.of();
    }
    
    List<? extends ComponentScan> scanBeans = springModel.getComponentScans();
    if (scanBeans.size() > 0) {
      List<PsiJavaPackage> psiPackages = getScannedPackages(scanBeans);
      if (psiPackages.isEmpty()) {
        return Collections.emptyList();
      }

      List<SpringJamElement> components = SpringJamModel.getModel(module).getConfigurations();

      List<SpringJamElement> filteredConfigurations = filterJavaConfigurations(components, psiPackages);
      for (SpringJamElement filteredConfiguration : filteredConfigurations) {
        javaConfigurations.add(filteredConfiguration);
        // TODO process imports
      }
    }

    return javaConfigurations;
  }

  public static void processImport(SpringJamElement filteredConfiguration) {
    PsiClass psiClass = filteredConfiguration.getPsiClass();

    if (psiClass != null) {
      PsiAnnotation annotation = AnnotationUtil.findAnnotation(psiClass, SpringAnnotationsConstants.IMPORT_ANNOTATION);
      if (annotation != null) {
        PsiAnnotationParameterList parameterList = annotation.getParameterList();
        for (PsiNameValuePair attribute : parameterList.getAttributes()) {
          PsiAnnotationMemberValue value = attribute.getValue();
          System.out.println();
        }
      }
    }

  }


  private static List<SpringStereotypeElement> filterStereotypeComponents(final List<SpringStereotypeElement> components,
                                                                          final List<PsiJavaPackage> psiPackages) {
    List<SpringStereotypeElement> filtered = new ArrayList<>();
    for (SpringStereotypeElement component : components) {
      final PsiClass psiClass = component.getBeanClass();
      if (isInPackage(psiPackages, psiClass)) {
        filtered.add(component);
      }
    }

    return filtered;
  }

  private static List<SpringJamElement> filterJavaConfigurations(final List<SpringJamElement> javaConfigurations,
                                                                 final List<PsiJavaPackage> psiPackages) {
    List<SpringJamElement> filtered = new ArrayList<>();
    for (SpringJamElement component : javaConfigurations) {
      final PsiClass psiClass = component.getPsiClass();
      if (isInPackage(psiPackages, psiClass)) {
        filtered.add(component);
      }
    }

    return filtered;
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

  private static List<SpringStereotypeElement> getAllStereotypeComponents(final SpringJamModel javaModel) {
    List<SpringStereotypeElement> elements = new ArrayList<>();

    elements.addAll(javaModel.getAllStereotypeComponents());

    return elements;
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
