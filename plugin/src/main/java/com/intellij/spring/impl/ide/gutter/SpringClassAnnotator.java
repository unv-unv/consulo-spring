/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */

package com.intellij.spring.impl.ide.gutter;

import com.intellij.jam.JamService;
import com.intellij.java.language.codeInsight.AnnotationUtil;
import com.intellij.java.language.psi.*;
import com.intellij.java.language.psi.util.PropertyUtil;
import com.intellij.spring.impl.ide.SpringIcons;
import com.intellij.spring.impl.ide.SpringManager;
import com.intellij.spring.impl.ide.SpringModel;
import com.intellij.spring.impl.ide.constants.SpringAnnotationsConstants;
import com.intellij.spring.impl.ide.java.SpringJavaClassInfo;
import com.intellij.spring.impl.ide.model.highlighting.SpringAutowireUtil;
import com.intellij.spring.impl.ide.model.highlighting.SpringJavaAutowiringInspection;
import com.intellij.spring.impl.ide.model.jam.JamPsiMemberSpringBean;
import com.intellij.spring.impl.ide.model.xml.CommonSpringBean;
import com.intellij.spring.impl.ide.model.xml.DomSpringBean;
import com.intellij.spring.impl.ide.model.xml.beans.*;
import consulo.annotation.access.RequiredReadAction;
import consulo.application.Application;
import consulo.application.util.NotNullLazyValue;
import consulo.language.editor.annotation.AnnotationHolder;
import consulo.language.editor.annotation.Annotator;
import consulo.language.editor.ui.navigation.PsiTargetPresentationFactory;
import consulo.language.editor.ui.navigation.TargetPresentationProvider;
import consulo.language.editor.ui.navigation.NavigationGutterIconBuilder;
import consulo.language.psi.PsiElement;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.language.util.ModuleUtilCore;
import consulo.localize.LocalizeValue;
import consulo.module.Module;
import consulo.navigation.TargetPresentationBuilder;
import consulo.spring.impl.icon.SpringImplIconGroup;
import consulo.spring.localize.SpringLocalize;
import consulo.util.lang.lazy.LazyValue;
import consulo.xml.codeInsight.navigation.DomNavigationGutterIconBuilder;
import consulo.xml.language.psi.XmlTag;
import consulo.xml.dom.DomElement;
import consulo.xml.dom.DomUtil;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class SpringClassAnnotator implements Annotator {

  private static final LocalizeValue UNKNOWN = SpringLocalize.springBeanWithUnknownName();
  private static final DomElementPresentationProvider DOM_PRESENTATION = new DomElementPresentationProvider(UNKNOWN);

  @Nullable
  private static SpringBean getSpringBean(PsiElement element) {
    DomElement domElement = DomUtil.getDomElement(element);
    return domElement == null ? null : domElement.getParentOfType(SpringBean.class, false);
  }

  private static final TargetPresentationProvider<PsiElement> BEAN_PRESENTATION = element -> {
    if (element instanceof XmlTag tag) {
      return DOM_PRESENTATION.getPresentation(tag);
    }

    TargetPresentationBuilder builder =
      Application.get().getInstance(PsiTargetPresentationFactory.class).presentationBuilder(element);

    if (element instanceof PsiAnnotation) {
      CommonSpringBean springBean = findJamSpringBean(element);
      if (springBean != null) {
        String beanName = springBean.getBeanName();
        builder = builder.withPresentableText(beanName == null ? UNKNOWN : LocalizeValue.of(beanName))
                         .withIcon(consulo.spring.impl.SpringIcons.SpringJavaBean);
      }

      PsiClass psiClass = PsiTreeUtil.getParentOfType(element, PsiClass.class);
      if (psiClass != null && psiClass.getName() != null) {
        builder = builder.withContainerText(LocalizeValue.of(psiClass.getName()));
      }
    }

    return builder.build();
  };

  @Nullable
  @RequiredReadAction
  private static CommonSpringBean findJamSpringBean(PsiElement element) {
    PsiMember member = PsiTreeUtil.getParentOfType(element, PsiMember.class);
    return member == null
      ? null
      : JamService.getJamService(element.getProject()).getJamElement(JamPsiMemberSpringBean.class, member);
  }

  private static final Function<SpringBaseBeanPointer, Collection<? extends PsiElement>> BEAN_POINTER_CONVERTOR = new Function<>() {
    @Override
    @Nonnull
    public Collection<? extends PsiElement> apply(SpringBaseBeanPointer pointer) {
      return Collections.singleton(pointer.getPsiElement());
    }
  };

  @Override
  @RequiredReadAction
  public void annotate(PsiElement psiElement, AnnotationHolder holder) {
    if (psiElement instanceof PsiIdentifier) {
      PsiElement parent = psiElement.getParent();
      if (parent instanceof PsiClass) {
        final PsiClass psiClass = (PsiClass)parent;
        SpringJavaClassInfo info = SpringJavaClassInfo.getSpringJavaClassInfo(psiClass);
        if (info.isMapped()) {
          addSpringBeanGutterIcon(holder,
                                  psiClass.getNameIdentifier(),
                                  new NotNullLazyValue<>() {
                                    @Override
                                    @Nonnull
                                    protected Collection<? extends SpringBaseBeanPointer> compute() {
                                      SpringJavaClassInfo info = SpringJavaClassInfo.getSpringJavaClassInfo(psiClass);
                                      return info.getMappedBeans();
                                    }
                                  });
        }
      }
      else if (parent instanceof PsiMethod) {
        annotateMethod((PsiMethod)parent, holder);
      }
      else if (parent instanceof PsiField) {
        PsiField field = (PsiField)parent;
        if (SpringAutowireUtil.isAutowiredByAnnotation(field)) {
          consulo.module.Module module = ModuleUtilCore.findModuleForPsiElement(field);
          SpringModel model = SpringManager.getInstance(field.getProject()).getCombinedModel(module);
          if (model != null) {
            boolean required = SpringAutowireUtil.isRequired(field);
            processVariable(field, holder, model, psiElement, required, field.getType());
          }
        }
      }
    }
  }

  @RequiredReadAction
  private static void annotateMethod(PsiMethod method, AnnotationHolder holder) {
    boolean autowired = false;
    if (PropertyUtil.isSimplePropertySetter(method)) {
      PsiClass psiClass = method.getContainingClass();
      if (psiClass != null) {
        SpringJavaClassInfo info = SpringJavaClassInfo.getSpringJavaClassInfo(psiClass);
        String propertyName = PropertyUtil.getPropertyNameBySetter(method);
        Collection<SpringPropertyDefinition> list = info.getMappedProperties(propertyName);
        if (list.size() > 0) {
          addPropertiesGutterIcon(holder, method);
        }
        List<SpringBaseBeanPointer> pointers = info.getMappedBeans();
        for (SpringBaseBeanPointer pointer : pointers) {
          if (pointer instanceof DomSpringBeanPointer domPointer) {
            DomSpringBean springBean = domPointer.getSpringBean();
            if (springBean instanceof SpringBean) {
              Autowire autowire = ((SpringBean)springBean).getBeanAutowire();
              if (autowire.isAutowired()) {
                autowired = true;
                break;
              }
            }
          }
        }
        if (autowired) {
          Module module = method.getModule();
          SpringModel model = SpringManager.getInstance(method.getProject()).getCombinedModel(module);
          if (model != null) {
            PsiType type = PropertyUtil.getPropertyType(method);
            if (type != null) {
              processVariable(method, holder, model, method.getNameIdentifier(), false, type);
            }
          }
        }
      }
    }
    else if (AnnotationUtil.isAnnotated(method, SpringAnnotationsConstants.SPRING_BEAN_ANNOTATION, 0)) {
      Module module = method.getModule();
      SpringModel model = SpringManager.getInstance(method.getProject()).getCombinedModel(module);

      if (model != null) {
        SpringBeanPointer bean = model.findBean(method.getName());
        if (bean != null) {
          NavigationGutterIconBuilder.create(SpringImplIconGroup.gutterSpringbean(), BEAN_POINTER_CONVERTOR).
                                     setPopupTitle(SpringLocalize.springBeanClassNavigateChooseClassTitle()).
                                     setPresentationProvider(DOM_PRESENTATION).
                                     setTargets(LazyValue.notNull(List::of)).
                                     setTooltipText(SpringLocalize.springBeanClassTooltipNavigateDeclaration()).
                                     install(holder, method.getNameIdentifier());
        }
      }
    }

    if (!autowired) {
      processAnnotatedMethod(method, holder);
    }
  }

  @RequiredReadAction
  private static void processAnnotatedMethod(PsiMethod method, AnnotationHolder holder) {
    boolean isAutowired = SpringAutowireUtil.isAutowiredByAnnotation(method);

    // implicit autowiring: single constructor of a Spring bean doesn't need @Autowired
    if (!isAutowired && method.isConstructor()) {
      PsiClass containingClass = method.getContainingClass();
      if (containingClass != null) {
        PsiMethod[] constructors = containingClass.getConstructors();
        if (constructors.length == 1) {
          SpringJavaClassInfo info = SpringJavaClassInfo.getSpringJavaClassInfo(containingClass);
          isAutowired = info.isMapped();
        }
      }
    }

    if (isAutowired) {
      Module module = method.getModule();
      SpringModel model = SpringManager.getInstance(method.getProject()).getCombinedModel(module);
      if (model != null) {
        boolean required = SpringAutowireUtil.isRequired(method);
        for (PsiVariable variable : method.getParameterList().getParameters()) {
          processVariable(variable, holder, model, variable, required, variable.getType());
        }
      }
    }
  }

  private static void processVariable(PsiModifierListOwner variable, AnnotationHolder holder,
                                      @Nonnull SpringModel model,
                                      PsiElement element, boolean required, @Nonnull PsiType type) {
    Collection<SpringBaseBeanPointer> list =
      SpringJavaAutowiringInspection.checkAutowiredPsiMember(variable, type, null, model, required);
    if (list != null && !list.isEmpty()) {
      NavigationGutterIconBuilder.create(SpringImplIconGroup.gutterShowautowireddependencies(), BEAN_POINTER_CONVERTOR).
                                 setPopupTitle(SpringLocalize.springBeanClassNavigateChooseClassTitle()).
                                 setPresentationProvider(BEAN_PRESENTATION).
                                 setTooltipText(SpringLocalize.navigateToAutowiredDependencies()).
                                 setTargets(list).install(holder, element);
    }
  }

  private static void addPropertiesGutterIcon(AnnotationHolder holder,
                                              final PsiMethod psiMethod) {


    NavigationGutterIconBuilder.create(SpringIcons.SPRING_BEAN_PROPERTY_ICON, DomNavigationGutterIconBuilder.DEFAULT_DOM_CONVERTOR).
                               setTargets(new NotNullLazyValue<>() {
                                 @Override
                                 @Nonnull
                                 protected Collection<? extends DomElement> compute() {
                                   String propertyName = PropertyUtil.getPropertyNameBySetter(psiMethod);
                                   SpringJavaClassInfo info =
                                     SpringJavaClassInfo.getSpringJavaClassInfo((PsiClass)psiMethod.getParent());
                                   return info.getMappedProperties(propertyName);
                                 }
                               }).setPopupTitle(SpringLocalize.springBeanPropertyNavigateChooseClassTitle()).
                               setPresentationProvider(element -> {
                                 SpringBean springBean = getSpringBean(element);
                                 assert springBean != null;
                                 String elementName = springBean.getBeanName();
                                 assert elementName != null;

                                 return Application.get().getInstance(PsiTargetPresentationFactory.class)
                                                   .presentationBuilder(element)
                                                   .withPresentableText(LocalizeValue.of(elementName))
                                                   .withContainerText(DomElementPresentationProvider.getContainerText(element))
                                                   .withIcon(consulo.spring.impl.SpringIcons.SpringBean)
                                                   .build();
                               }).
                               setTooltipText(SpringLocalize.springBeanPropertyTooltipNavigateDeclaration()).
                               install(holder, psiMethod.getNameIdentifier());
  }

  private static void addSpringBeanGutterIcon(AnnotationHolder holder,
                                              PsiIdentifier psiIdentifier,
                                              NotNullLazyValue<Collection<? extends SpringBaseBeanPointer>> targets) {

    LocalizeValue tooltip = SpringLocalize.springBeanClassTooltipNavigateDeclaration();
    Collection<? extends SpringBaseBeanPointer> resolvedTargets = targets.getValue();
    if (!resolvedTargets.isEmpty()) {
      SpringBaseBeanPointer first = resolvedTargets.iterator().next();
      String beanName = first.getName();
      if (beanName != null && !beanName.isEmpty()) {
        tooltip = LocalizeValue.localizeTODO("Spring Bean: '" + beanName + "'");
      }
    }

    NavigationGutterIconBuilder.create(SpringIcons.SPRING_BEAN_ICON, BEAN_POINTER_CONVERTOR).
                               setTargets(targets).
                               setPopupTitle(SpringLocalize.springBeanClassNavigateChooseClassTitle()).
                               setPresentationProvider(BEAN_PRESENTATION).
                               setTooltipText(tooltip).
                               install(holder, psiIdentifier);
  }
}
