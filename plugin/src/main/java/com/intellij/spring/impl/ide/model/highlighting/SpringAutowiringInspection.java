/*
 * Copyright (c) 2000-2006 JetBrains s.r.o. All Rights Reserved.
 */

package com.intellij.spring.impl.ide.model.highlighting;

import com.intellij.java.language.psi.*;
import com.intellij.java.language.psi.util.PropertyUtil;
import com.intellij.java.language.psi.util.PsiFormatUtil;
import com.intellij.spring.impl.ide.SpringModel;
import com.intellij.spring.impl.ide.model.ResolvedConstructorArgs;
import com.intellij.spring.impl.ide.model.xml.beans.*;
import consulo.annotation.component.ExtensionImpl;
import consulo.localize.LocalizeValue;
import consulo.spring.localize.SpringLocalize;
import consulo.util.lang.StringUtil;
import consulo.xml.util.xml.DomElement;
import consulo.xml.util.xml.DomUtil;
import consulo.xml.util.xml.highlighting.DomElementAnnotationHolder;
import jakarta.annotation.Nonnull;

import java.util.*;

@ExtensionImpl
public class SpringAutowiringInspection extends SpringBeanInspectionBase {
    @Nonnull
    @Override
    public LocalizeValue getDisplayName() {
        return SpringLocalize.modelInspectionBeanAutowiring();
    }

    @Nonnull
    @Override
    public String getShortName() {
        return "SpringBeanAutowiringInspection";
    }

    private static void checkAutowiring(
        @Nonnull final SpringBean springBean,
        @Nonnull final SpringModel springModel,
        final DomElementAnnotationHolder holder
    ) {

        if (springBean.getBeanClass() == null) {
            return;
        }
        final Autowire autowire = springBean.getBeanAutowire();

        if (autowire.equals(Autowire.BY_TYPE)) {
            checkByTypeAutowire(springBean, springModel, holder);
        }
        else if (autowire.equals(Autowire.CONSTRUCTOR)) {
            checkByConstructorAutowire(springBean, holder);
        }
    }

    private static void checkByConstructorAutowire(@Nonnull final SpringBean springBean, @Nonnull final DomElementAnnotationHolder holder) {
        final ResolvedConstructorArgs resolvedArgs = springBean.getResolvedConstructorArgs();
        final List<PsiMethod> methods = resolvedArgs.getCheckedMethods();
        if (resolvedArgs.isResolved() || methods == null) {
            return;
        }
        for (final PsiMethod checkedMethod : methods) {
            final Map<PsiParameter, Collection<SpringBaseBeanPointer>> autowiredParams = resolvedArgs.getAutowiredParams(checkedMethod);
            if (autowiredParams != null && autowiredParams.size() > 0) {
                final Set<Map.Entry<PsiParameter, Collection<SpringBaseBeanPointer>>> entries = autowiredParams.entrySet();
                for (Map.Entry<PsiParameter, Collection<SpringBaseBeanPointer>> entry : entries) {
                    checkAutowire(springBean, holder, checkedMethod, entry.getKey(), entry.getValue());
                }
            }
        }
    }

    private static void checkAutowire(
        final SpringBean springBean,
        final DomElementAnnotationHolder holder,
        final PsiMethod checkedMethod,
        final PsiParameter psiParameter,
        final Collection<SpringBaseBeanPointer> springBeans
    ) {
        if (springBeans != null && springBeans.size() > 1) {
            List<String> beanNames = new ArrayList<String>();
            for (SpringBaseBeanPointer bean : springBeans) {
                String beanName = bean.getName();
                if (StringUtil.isEmpty(beanName)) {
                    beanName = "unknown";
                }
                beanNames.add(beanName);
            }

            String methodName = PsiFormatUtil
                .formatMethod(checkedMethod, PsiSubstitutor.EMPTY, PsiFormatUtil.SHOW_NAME | PsiFormatUtil.SHOW_PARAMETERS,
                    PsiFormatUtil.SHOW_TYPE
                );

            LocalizeValue message = SpringLocalize.beanAutowiringByType(
                psiParameter.getType().getPresentableText(),
                StringUtil.join(beanNames, ","),
                methodName
            );
            final DomElement problemElement;
            if (DomUtil.hasXml(springBean.getClazz())) {
                problemElement = springBean.getClazz();
            }
            else if (DomUtil.hasXml(springBean.getFactoryMethod())) {
                problemElement = springBean.getFactoryMethod();
            }
            else {
                problemElement = springBean;
            }
            holder.createProblem(problemElement, message.get());
        }
    }

    private static void checkByTypeAutowire(
        @Nonnull final SpringBean springBean,
        @Nonnull final SpringModel springModel,
        @Nonnull final DomElementAnnotationHolder holder
    ) {
        final PsiClass beanClass = springBean.getBeanClass();
        if (beanClass == null) {
            return;
        }

        Map<PsiType, List<PsiMethod>> propertyTypes = new HashMap<PsiType, List<PsiMethod>>();
        for (PsiMethod psiMethod : beanClass.getAllMethods()) {
            if (PropertyUtil.isSimplePropertySetter(psiMethod)) {
                final PsiParameter parameter = psiMethod.getParameterList().getParameters()[0];
                final PsiType type = parameter.getType();
                if (propertyTypes.get(type) == null) {
                    propertyTypes.put(type, new ArrayList<PsiMethod>());
                }

                propertyTypes.get(type).add(psiMethod);
            }
        }

        for (PsiType psiType : propertyTypes.keySet()) {
            final Collection<SpringBaseBeanPointer> beans = SpringAutowireUtil.autowireByType(springModel, psiType);
            if (beans.size() > 1) {
                List<String> properties = new ArrayList<String>();
                for (PsiMethod psiMethod : propertyTypes.get(psiType)) {
                    boolean isPropertyDefined = false;
                    final String propertyName = PropertyUtil.getPropertyNameBySetter(psiMethod);
                    for (SpringPropertyDefinition springProperty : springBean.getAllProperties()) {
                        if (propertyName.equals(springProperty.getPropertyName())) {
                            isPropertyDefined = true;
                            break;
                        }
                    }
                    if (!isPropertyDefined) {
                        properties.add(propertyName);
                    }
                }

                if (properties.size() > 0) {
                    List<String> beanNames = new ArrayList<String>();
                    for (SpringBaseBeanPointer bean : beans) {
                        String beanName = bean.getName();
                        if (StringUtil.isEmpty(beanName)) {
                            beanName = "unknown";
                        }
                        beanNames.add(beanName);
                    }

                    LocalizeValue message = SpringLocalize.beanAutowiringByType(
                        psiType.getPresentableText(),
                        StringUtil.join(beanNames, ","),
                        StringUtil.join(properties, ",")
                    );

                    holder.createProblem(springBean, message.get());
                }
            }
        }
    }

    protected void checkBean(
        SpringBean springBean,
        final Beans beans,
        final DomElementAnnotationHolder holder,
        final SpringModel model,
        Object state
    ) {
        final Boolean autoWireCandidate = springBean.getAutowireCandidate().getValue();
        if (autoWireCandidate != null && !autoWireCandidate.booleanValue()) {
            return;
        }
        checkAutowiring(springBean, model, holder);
    }
}
