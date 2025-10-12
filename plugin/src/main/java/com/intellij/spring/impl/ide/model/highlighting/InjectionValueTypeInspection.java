/*
 * Copyright (c) 2000-2006 JetBrains s.r.o. All Rights Reserved.
 */
package com.intellij.spring.impl.ide.model.highlighting;

import com.intellij.java.language.psi.*;
import com.intellij.spring.impl.ide.SpringBundle;
import com.intellij.spring.impl.ide.SpringModel;
import com.intellij.spring.impl.ide.model.SpringBeanEffectiveTypeProvider;
import com.intellij.spring.impl.ide.model.SpringUtils;
import com.intellij.spring.impl.ide.model.converters.SpringBeanResolveConverter;
import com.intellij.spring.impl.ide.model.converters.SpringBeanUtil;
import com.intellij.spring.impl.ide.model.converters.SpringConverterUtil;
import com.intellij.spring.impl.ide.model.jam.javaConfig.SpringJavaBean;
import com.intellij.spring.impl.ide.model.xml.CommonSpringBean;
import com.intellij.spring.impl.ide.model.xml.CustomBean;
import com.intellij.spring.impl.ide.model.xml.DomSpringBean;
import com.intellij.spring.impl.ide.model.xml.beans.*;
import consulo.annotation.component.ExtensionImpl;
import consulo.localize.LocalizeValue;
import consulo.project.Project;
import consulo.spring.localize.SpringLocalize;
import consulo.util.lang.StringUtil;
import consulo.xml.util.xml.Converter;
import consulo.xml.util.xml.DomElement;
import consulo.xml.util.xml.GenericDomValue;
import consulo.xml.util.xml.highlighting.DomElementAnnotationHolder;
import consulo.xml.util.xml.impl.ConvertContextImpl;
import org.jetbrains.annotations.NonNls;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

@ExtensionImpl
public class InjectionValueTypeInspection extends DomSpringBeanInspectionBase<Object> {
    @Nonnull
    @Override
    public LocalizeValue getDisplayName() {
        return SpringLocalize.modelInspectionBeanPropertyValue();
    }

    @Nonnull
    @Override
    public String getShortName() {
        return "InjectionValueTypeInspection";
    }

    private static void checkIdRef(
        final SpringElementsHolder elementsHolder,
        final DomElementAnnotationHolder holder,
        @Nonnull final PsiType injectionType
    ) {
        if (elementsHolder.getIdref().getXmlElement() != null) {
            checkPropertyTypeByClass(elementsHolder, String.class, holder, injectionType, elementsHolder.getIdref());
        }
    }

    private static void checkSpringPropertyValueType(
        final SpringElementsHolder elementsHolder,
        final DomElementAnnotationHolder holder,
        @Nonnull final PsiType propertyType
    ) {
        final PsiType type = elementsHolder.getValue().getType().getValue();

        if (type == null) {
            return;
        }

        if (!propertyType.isAssignableFrom(type)) {
            LocalizeValue message = SpringLocalize.beanBadPropertyType(propertyType.getCanonicalText(), type.getCanonicalText());

            holder.createProblem(elementsHolder.getValue().getType(), message.get());
        }
    }


    private void checkSpringPropertyListAndSet(
        final SpringElementsHolder elementsHolder,
        final DomElementAnnotationHolder holder,
        @Nullable final PsiType propertyType
    ) {
        final ListOrSet set = elementsHolder.getSet();
        final ListOrSet list = elementsHolder.getList();

        if (set.getXmlElement() != null) {
            checkSpringPropertyCollection(set, holder);
            if (propertyType != null) {
                checkPropertyTypeByClass(elementsHolder, Set.class, holder, propertyType, set);
            }
        }
        if (list.getXmlElement() != null) {
            checkSpringPropertyCollection(list, holder);
            if (propertyType != null) {
                checkPropertyTypeByClass(elementsHolder, List.class, holder, propertyType, list);
            }
        }
    }

    private static void checkSpringPropertyMap(
        final SpringElementsHolder elementsHolder,
        final DomElementAnnotationHolder holder,
        @Nonnull final PsiType propertyType
    ) {
        final SpringMap map = elementsHolder.getMap();
        if (map.getXmlElement() != null) {
            checkPropertyTypeByClass(elementsHolder, Map.class, holder, propertyType, map);
        }
    }

    private static void checkSpringPropertyProps(
        final SpringElementsHolder property,
        final DomElementAnnotationHolder holder,
        @Nonnull final PsiType propertyType
    ) {
        final Props props = property.getProps();
        if (props.getXmlElement() != null) {
            checkPropertyTypeByClass(property, Properties.class, holder, propertyType, props);
        }
    }


    public void checkSpringPropertyCollection(final ListOrSet collection, final DomElementAnnotationHolder holder) {
        if (collection.getXmlElement() == null) {
            return;
        }
        final PsiType psiClass = SpringBeanUtil.getRequiredType(collection);
        if (psiClass != null) {
            checkCollectionElementsType(psiClass, collection, holder);
        }
        for (ListOrSet listOrSet : collection.getLists()) {
            checkSpringPropertyCollection(listOrSet, holder);
        }
    }

    private void checkCollectionElementsType(
        @Nonnull final PsiType type,
        final CollectionElements collection,
        final DomElementAnnotationHolder holder
    ) {
        for (SpringRef ref : collection.getRefs()) {
            checkSpringRefType(ref, type, holder);
        }
        for (Idref idref : collection.getIdrefs()) {
            if (!CommonClassNames.JAVA_LANG_STRING.equals(type.getCanonicalText())) {
                holder.createProblem(idref, SpringBundle.message("idref.cannot.be.added.in.collection", type.getCanonicalText()));
            }
        }
        if (type instanceof PsiClassType) {
            final PsiClass psiClass = ((PsiClassType) type).resolve();
            if (psiClass != null) {
                for (SpringBean springBean : collection.getBeans()) {
                    checkBeanClass(springBean, psiClass, springBean, holder);
                }
            }
        }
    }

    private void checkSpringInjectionRefAttr(
        final DomElementAnnotationHolder holder,
        @Nullable final PsiType propertyType,
        @Nullable final GenericDomValue<SpringBeanPointer> refAttr
    ) {
        if (propertyType == null || refAttr == null) {
            return;
        }

        final SpringBeanPointer beanPointer = refAttr.getValue();
        if (beanPointer != null) {
            checkBeanClass(beanPointer, propertyType, refAttr, holder);
            checkJavaBeanVisibility(beanPointer, refAttr, holder);
        }
    }

    private void checkSpringRefType(final SpringRef ref, final PsiType psiType, final DomElementAnnotationHolder holder) {
        if (ref.getXmlElement() == null) {
            return;
        }

        checkBeanClass(ref.getBean().getValue(), psiType, ref.getBean(), holder);
        checkBeanClass(ref.getLocal().getValue(), psiType, ref.getLocal(), holder);
        checkBeanClass(ref.getParentAttr().getValue(), psiType, ref.getParentAttr(), holder);
        checkJavaBeanVisibility(ref.getBean().getValue(), ref.getBean(), holder);
    }

    private static void checkJavaBeanVisibility(
        final SpringBeanPointer beanPointer,
        DomElement annotatedElement,
        final DomElementAnnotationHolder holder
    ) {
        if (beanPointer == null) {
            return;
        }
        final CommonSpringBean springBean = beanPointer.getSpringBean();
        if (springBean instanceof SpringJavaBean) {
            if (!((SpringJavaBean) springBean).isPublic()) {
                final String message = SpringBundle.message("bean.must.be.public");
                holder.createProblem(annotatedElement, message);
            }
        }
    }

    private void checkSpringPropertyInnerBean(
        final SpringElementsHolder elementsHolder,
        final DomElementAnnotationHolder holder,
        @Nonnull final PsiType injectionType
    ) {
        final List<CommonSpringBean> beans = SpringUtils.getChildBeans(elementsHolder, false);
        if (!beans.isEmpty()) {
            final CommonSpringBean bean = beans.get(0);
            checkBeanClass(
                bean,
                injectionType,
                bean instanceof CustomBean ? ((CustomBean) bean).getWrapper() : (DomSpringBean) bean,
                holder
            );
        }
    }

    private void checkBeanClass(
        @Nullable final CommonSpringBean springBean,
        @Nonnull final PsiClass clazz,
        final DomElement annotatedElement,
        @Nonnull final DomElementAnnotationHolder holder
    ) {
        if (springBean != null) {
            final PsiClassType classType = JavaPsiFacade.getInstance(clazz.getProject()).getElementFactory().createType(clazz);

            checkBeanClass(springBean, classType, annotatedElement, holder);
        }
    }

    private void checkBeanClass(
        @Nullable final SpringBeanPointer springBeanPointer,
        @Nonnull final PsiType psiType,
        final DomElement annotatedElement,
        @Nonnull final DomElementAnnotationHolder holder
    ) {
        if (springBeanPointer != null) {
            if (springBeanPointer.getBeanClass() == null) {
                return;
            }

            checkBeanClass(springBeanPointer.getSpringBean(), psiType, annotatedElement, holder);
        }
    }

    protected void checkBeanClass(
        @Nonnull final CommonSpringBean springBean,
        @Nonnull PsiType psiType,
        final DomElement annotatedElement,
        @Nonnull final DomElementAnnotationHolder holder
    ) {

        if (psiType instanceof PsiArrayType) {
            psiType = ((PsiArrayType) psiType).getComponentType();
        }
        final PsiClass beanClass = springBean.getBeanClass();
        if (beanClass == null) {
            return;
        }

        final Project project = annotatedElement.getManager().getProject();

        if (!SpringUtils.isEffectiveClassType(psiType, springBean)) {
            if (tryCreatingCustomProblem(springBean, psiType, annotatedElement, holder)) {
                return;
            }

            if (SpringUtils.isCollectionType(psiType, project)) {
                final PsiType genericType = SpringUtils.getGenericCollectonType(psiType);
                if (genericType != null && !genericType.isAssignableFrom(JavaPsiFacade.getInstance(project)
                    .getElementFactory()
                    .createType(beanClass))) {
                    final String message =
                        SpringBundle.message("bean.must.be.of.types", psiType.getCanonicalText(), genericType.getCanonicalText());
                    holder.createProblem(annotatedElement, message);
                }
            }
            else {
                final String message = SpringBundle.message("bean.must.be.of.type", psiType.getCanonicalText());
                holder.createProblem(annotatedElement, message);
            }
        }
        else if (annotatedElement instanceof GenericDomValue) {
            GenericDomValue genericDomValue = (GenericDomValue) annotatedElement;
            Converter converter = genericDomValue.getConverter();
            if (converter instanceof SpringBeanResolveConverter) {
                List<PsiClassType> list =
                    ((SpringBeanResolveConverter) converter).getRequiredClasses(new ConvertContextImpl(genericDomValue));
                if (list != null && list.size() > 0) {
                    boolean isAssignable = false;
                    for (PsiClassType type : list) {
                        if (SpringUtils.isEffectiveClassType(type, springBean)) {
                            isAssignable = true;
                            break;
                        }
                    }
                    if (!isAssignable) {
                        if (tryCreatingCustomProblem(springBean, psiType, annotatedElement, holder)) {
                            return;
                        }

                        if (list.size() == 1) {
                            final String message = SpringBundle.message("bean.must.be.of.type", psiType.getCanonicalText());
                            holder.createProblem(annotatedElement, message);
                        }
                        else {
                            String classNames = StringUtil.join(list, psiClassType -> psiClassType.getCanonicalText(), ",");
                            final String message = SpringBundle.message("bean.must.be.one.of.this.types", classNames);
                            holder.createProblem(annotatedElement, message);
                        }
                    }
                }
            }
        }
    }

    private static boolean tryCreatingCustomProblem(
        final CommonSpringBean springBean,
        final PsiType psiType,
        final DomElement annotatedElement,
        final DomElementAnnotationHolder holder
    ) {
        for (final SpringBeanEffectiveTypeProvider provider : SpringBeanEffectiveTypeProvider.EP_NAME.getExtensions()) {
            if (provider.createCustomProblem(springBean, psiType, holder, annotatedElement)) {
                return true;
            }
        }
        return false;
    }

    private static void checkPropertyTypeByClass(
        final SpringElementsHolder injection,
        final Class requiredClass,
        final DomElementAnnotationHolder holder,
        @Nonnull final PsiType propertyType,
        DomElement value
    ) {


        if (requiredClass.getName().equals(propertyType.getCanonicalText())) {
            return;
        }

        final Project project = injection.getManager().getProject();
        final PsiType requiredType = SpringConverterUtil.findType(requiredClass, project);
        if (requiredType != null && !requiredType.isAssignableFrom(propertyType) &&
            !SpringConverterUtil.isConvertable(requiredType, propertyType, project)) {
            final String message =
                SpringBundle.message("bean.bad.property.type", propertyType.getCanonicalText(), requiredClass.getCanonicalName());
            holder.createProblem(value, message);
        }
    }

    protected void checkBean(
        DomSpringBean springBean,
        final Beans beans,
        final DomElementAnnotationHolder holder,
        final SpringModel springModel
    ) {
        for (SpringValueHolderDefinition definition : SpringUtils.getValueHolders(springBean)) {
            if (definition instanceof ConstructorArg) {
                continue;
            }

            final PsiType propertyType = SpringBeanUtil.getRequiredType(definition);
            checkSpringInjectionRefAttr(holder, propertyType, definition.getRefElement());
            if (definition instanceof SpringElementsHolder) {
                checkElementsHolder((SpringElementsHolder) definition, propertyType, holder);
            }
        }
    }

    private void checkElementsHolder(
        final SpringElementsHolder elementsHolder,
        @Nullable final PsiType requiredType,
        final DomElementAnnotationHolder holder
    ) {
        if (requiredType != null) {
            checkSpringRefType(elementsHolder.getRef(), requiredType, holder);
            checkSpringPropertyValueType(elementsHolder, holder, requiredType);
            checkIdRef(elementsHolder, holder, requiredType);
            checkSpringPropertyProps(elementsHolder, holder, requiredType);
            checkSpringPropertyMap(elementsHolder, holder, requiredType);
            checkSpringPropertyInnerBean(elementsHolder, holder, requiredType);
        }
        checkSpringPropertyListAndSet(elementsHolder, holder, requiredType);
    }
}
