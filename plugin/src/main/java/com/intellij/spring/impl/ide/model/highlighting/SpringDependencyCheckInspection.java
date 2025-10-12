/*
 * Copyright (c) 2000-2006 JetBrains s.r.o. All Rights Reserved.
 */

package com.intellij.spring.impl.ide.model.highlighting;

import com.intellij.java.language.psi.*;
import com.intellij.java.language.psi.util.PropertyUtil;
import com.intellij.spring.impl.ide.SpringBundle;
import com.intellij.spring.impl.ide.SpringModel;
import com.intellij.spring.impl.ide.model.xml.beans.*;
import consulo.annotation.component.ExtensionImpl;
import consulo.localize.LocalizeValue;
import consulo.spring.localize.SpringLocalize;
import consulo.util.lang.StringUtil;
import consulo.xml.util.xml.DomFileElement;
import consulo.xml.util.xml.highlighting.DomElementAnnotationHolder;
import org.jetbrains.annotations.NonNls;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * @see org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory#checkDependencies()
 */
@ExtensionImpl
public class SpringDependencyCheckInspection extends SpringBeanInspectionBase {
    private static final List<String> myWrapperClasses = new ArrayList<String>();

    static {
        myWrapperClasses.add("java.lang.Boolean");
        myWrapperClasses.add("java.lang.Byte");
        myWrapperClasses.add("java.lang.Character");
        myWrapperClasses.add("java.lang.Short");
        myWrapperClasses.add("java.lang.Integer");
        myWrapperClasses.add("java.lang.Long");
        myWrapperClasses.add("java.lang.Float");
        myWrapperClasses.add("java.lang.Double");
    }

    @Nonnull
    @Override
    public LocalizeValue getDisplayName() {
        return SpringLocalize.modelInspectionBeanDependencyCheck();
    }

    @Nonnull
    @Override
    public String getShortName() {
        return "SpringBeanDepedencyCheckInspection";
    }

    private static boolean isObjectsDependencyCheck(final DependencyCheck dependencyCheckMode) {
        return dependencyCheckMode.equals(DependencyCheck.OBJECTS) || dependencyCheckMode.equals(DependencyCheck.ALL);
    }

    private static boolean isSimpleDependencyCheck(final DependencyCheck dependencyCheckMode) {
        return dependencyCheckMode.equals(DependencyCheck.SIMPLE) || dependencyCheckMode.equals(DependencyCheck.ALL);
    }

    private static void checkSimpleAndObjectsDependencies(
        @Nonnull final SpringBean springBean,
        @Nonnull final SpringModel springModel,
        @Nonnull final DomElementAnnotationHolder holder,
        final DependencyCheck dependencyCheck
    ) {

        final PsiClass beanClass = springBean.getBeanClass();
        if (beanClass == null) {
            return;
        }
        List<PsiMethod> setters = new ArrayList<PsiMethod>();

        for (PsiMethod psiMethod : beanClass.getAllMethods()) {
            if (PropertyUtil.isSimplePropertySetter(psiMethod) && psiMethod.hasModifierProperty(PsiModifier.PUBLIC)) {
                setters.add(psiMethod);
            }
        }

        List<String> nonInjectedProperties = new ArrayList<String>();
        for (PsiMethod psiMethod : setters) {
            boolean accepted = acceptPropertyForCheck(psiMethod, dependencyCheck);
            if (accepted && !isPropertyInjected(springBean, psiMethod)) {
                if (isObjectsDependencyCheck(dependencyCheck) && isAutowired(springBean, springModel, psiMethod)) {
                    continue;
                }

                nonInjectedProperties.add(PropertyUtil.getPropertyNameBySetter(psiMethod));
            }
        }

        if (nonInjectedProperties.size() > 0) {
            LocalizeValue message = SpringLocalize.beanDependencyCheckMessage(StringUtil.join(nonInjectedProperties, ","));
            holder.createProblem(springBean, message.get());
        }
    }

    private static boolean acceptPropertyForCheck(final PsiMethod psiMethod, final DependencyCheck dependencyCheck) {
        final PsiType psiType = psiMethod.getParameterList().getParameters()[0].getType();
        final boolean simpleProperty = isSimpleProperty(psiType);

        return (simpleProperty && isSimpleDependencyCheck(dependencyCheck)) || (!simpleProperty && isObjectsDependencyCheck(dependencyCheck));
    }

    private static boolean isAutowired(final SpringBean springBean, final SpringModel springModel, final PsiMethod psiMethod) {
        final Autowire autowire = springBean.getBeanAutowire();
        switch (autowire) {
            case BY_TYPE:
                final PsiType psiType = psiMethod.getParameterList().getParameters()[0].getType();
                if (psiType instanceof PsiClassType) {
                    final PsiClass psiClass = ((PsiClassType) psiType).resolve();
                    if (psiClass != null) {
                        final List<SpringBaseBeanPointer> springBeans = springModel.findBeansByEffectivePsiClassWithInheritance(psiClass);
                        if (springBeans != null && springBeans.size() > 0) {
                            return true;
                        }
                    }
                }
                return false;
            case BY_NAME:
                final String propertyName = PropertyUtil.getPropertyNameBySetter(psiMethod);

                final SpringBeanPointer bean = springModel.findBean(propertyName);

                return bean != null && !bean.isReferenceTo(springBean);
        }
        return false;
    }

    private static boolean isPropertyInjected(final SpringBean springBean, final PsiMethod psiMethod) {
        final String beanClassPropertyName = PropertyUtil.getPropertyNameBySetter(psiMethod);
        if (beanClassPropertyName != null) {
            for (SpringPropertyDefinition springProperty : springBean.getAllProperties()) {
                if (beanClassPropertyName.equals(springProperty.getPropertyName())) {
                    return true;
                }
            }
        }
        return false;
    }

    protected void checkBean(
        SpringBean springBean,
        final Beans beans,
        final DomElementAnnotationHolder holder,
        final SpringModel model,
        Object state
    ) {

        if (springBean.getBeanClass() != null) {
            DependencyCheck dependencyCheck = springBean.getDependencyCheck().getValue();
            if (dependencyCheck == null) {
                dependencyCheck = getDefaultDependencyCheck(model.getRoots());
            }


            if (dependencyCheck != null && !dependencyCheck.equals(DependencyCheck.NONE) &&
                !dependencyCheck.equals(DependencyCheck.DEFAULT)) {

                checkSimpleAndObjectsDependencies(springBean, model, holder, dependencyCheck);
            }
        }
    }

    @Nullable
    private static DependencyCheck getDefaultDependencyCheck(final List<DomFileElement<Beans>> roots) {
        for (final DomFileElement<Beans> element : roots) {
            final DependencyCheck value = DependencyCheck.fromDefault(element.getRootElement().getDefaultDependencyCheck().getValue());
            if (value != null) {
                return value;
            }
        }
        return null;
    }


    //@see org.springframework.beans.BeanUtils.BeanUtils#isSimpleProperty
    private static boolean isSimpleProperty(final PsiType psiType) {
        final boolean isArrayType = psiType instanceof PsiArrayType;

        return isArrayType ? isSimpleSpringType(((PsiArrayType) psiType).getComponentType()) : isSimpleSpringType(psiType);
    }

    //@see org.springframework.beans.BeanUtils.BeanUtils#isSimpleProperty
    private static boolean isSimpleSpringType(final PsiType psiType) {
        if (psiType instanceof PsiPrimitiveType) {
            return true;
        }

        final String typeName = psiType.getCanonicalText();
        if (myWrapperClasses.contains(typeName)) {
            return true;
        }
        if (String.class.getName().equals(typeName)) {
            return true;
        }
        if (Class.class.getName().equals(typeName)) {
            return true;
        }

        return false;
    }
}
