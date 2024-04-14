package com.intellij.spring.impl.ide.model.jam;

import com.intellij.jam.JamService;
import com.intellij.jam.reflect.JamMemberMeta;
import com.intellij.java.language.codeInsight.AnnotationUtil;
import com.intellij.java.language.patterns.PsiClassPattern;
import com.intellij.java.language.psi.PsiClass;
import com.intellij.spring.impl.ide.constants.SpringAnnotationsConstants;
import com.intellij.spring.impl.ide.model.jam.javaConfig.JavaSpringConfigurationElement;
import com.intellij.spring.impl.ide.model.jam.stereotype.*;
import com.intellij.spring.impl.ide.model.jam.utils.JamAnnotationTypeUtil;
import consulo.annotation.access.RequiredReadAction;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.psi.PsiElementRef;
import consulo.language.sem.SemContributor;
import consulo.language.sem.SemKey;
import consulo.language.sem.SemRegistrar;
import consulo.language.sem.SemService;
import consulo.module.Module;
import consulo.spring.impl.boot.jam.SpringBootApplicationElement;
import consulo.spring.impl.boot.jam.SpringBootConfigurationElement;
import jakarta.inject.Inject;

import java.util.List;

import static com.intellij.java.language.patterns.PsiJavaPatterns.psiClass;

@ExtensionImpl
public class SpringSemContributor extends SemContributor {
  private static final SemKey<JamMemberMeta<PsiClass, CustomSpringComponent>> CUSTOM_COMPONENT_META_KEY =
    JamService.MEMBER_META_KEY.subKey("CustomSpringComponentMeta");
  public static final SemKey<CustomSpringComponent> CUSTOM_COMPONENT_JAM_KEY = JamService.JAM_ELEMENT_KEY.subKey("CustomSpringComponent");

  private final SemService mySemService;

  @Inject
  public SpringSemContributor(SemService semService) {
    mySemService = semService;
  }

  @Override
  public void registerSemProviders(SemRegistrar registrar) {
    PsiClassPattern psiClassPattern = psiClass().nonAnnotationType();

    JavaSpringConfigurationElement.META.register(registrar,
                                                 psiClassPattern.withAnnotation(SpringAnnotationsConstants.SPRING_CONFIGURATION_ANNOTATION));

    SpringBootConfigurationElement.META.register(registrar,
                                                 psiClassPattern.withAnnotation(SpringAnnotationsConstants.SPRING_BOOT_CONFIGURATION_ANNOTATION));

    //JavaSpringConfiguration.BEANS_METHOD_META.register(registrar, PsiJavaPatterns.psiMethod().withAnnotation(SpringAnnotationsConstants.JAVA_SPRING_BEAN_ANNOTATION));

    SpringComponent.META.register(registrar, psiClassPattern.withAnnotation(SpringAnnotationsConstants.COMPONENT_ANNOTATION));
    SpringController.META.register(registrar, psiClassPattern.withAnnotation(SpringAnnotationsConstants.CONTROLLER_ANNOTATION));
    SpringService.META.register(registrar, psiClassPattern.withAnnotation(SpringAnnotationsConstants.SERVICE_ANNOTATION));
    SpringRepository.META.register(registrar, psiClassPattern.withAnnotation(SpringAnnotationsConstants.REPOSITORY_ANNOTATION));
    SpringComponentScan.META.register(registrar, psiClassPattern.withAnnotation(SpringAnnotationsConstants.COMPONENT_SCAN_ANNOTATION));
    SpringBootApplicationElement.META.register(registrar,
                                               psiClassPattern.withAnnotation(SpringAnnotationsConstants.SPRING_BOOT_APPLICATION_ANNOTATION));


    // register custom components

    registrar.registerSemElementProvider(CUSTOM_COMPONENT_META_KEY, psiClassPattern, SpringSemContributor::calcNamedWebBeanMeta);

    registrar.registerSemElementProvider(CUSTOM_COMPONENT_JAM_KEY, psiClassPattern, member -> {
      final JamMemberMeta<PsiClass, CustomSpringComponent> memberMeta =
        mySemService.getSemElement(CUSTOM_COMPONENT_META_KEY, member);
      return memberMeta != null ? memberMeta.createJamElement(PsiElementRef.real(member)) : null;
    });
  }


  @RequiredReadAction
  private static JamMemberMeta<PsiClass, CustomSpringComponent> calcNamedWebBeanMeta(PsiClass psiClass) {
    if (psiClass.isAnnotationType()) return null;

    final Module module = psiClass.getModule();
    if (module != null) {
      List<String> customComponentAnnotations = JamAnnotationTypeUtil.getUserDefinedCustomComponentAnnotations(module);

      for (String anno : customComponentAnnotations) {
        if (AnnotationUtil.isAnnotated(psiClass, anno, true)) {
          return createCustomSpringComponentJamMemberMeta(anno);
        }
      }
    }

    return null;
  }

  private static JamMemberMeta<PsiClass, CustomSpringComponent> createCustomSpringComponentJamMemberMeta(final String annotationFQN) {
    return new JamMemberMeta<>(null, CustomSpringComponent.class, CUSTOM_COMPONENT_JAM_KEY) {
      @Override
      public CustomSpringComponent createJamElement(PsiElementRef<PsiClass> psiMemberPsiRef) {
        return new CustomSpringComponent(annotationFQN, psiMemberPsiRef.getPsiElement());
      }
    };
  }
}