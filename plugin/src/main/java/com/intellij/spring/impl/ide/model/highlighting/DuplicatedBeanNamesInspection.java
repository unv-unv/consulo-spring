/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */
package com.intellij.spring.impl.ide.model.highlighting;

import com.intellij.spring.impl.ide.SpringManager;
import com.intellij.spring.impl.ide.SpringModel;
import com.intellij.spring.impl.ide.model.SpringUtils;
import com.intellij.spring.impl.ide.model.xml.CommonSpringBean;
import com.intellij.spring.impl.ide.model.xml.DomSpringBean;
import com.intellij.spring.impl.ide.model.xml.beans.Alias;
import com.intellij.spring.impl.ide.model.xml.beans.Beans;
import com.intellij.spring.impl.ide.model.xml.beans.SpringBean;
import consulo.annotation.component.ExtensionImpl;
import consulo.document.util.TextRange;
import consulo.language.editor.annotation.HighlightSeverity;
import consulo.localize.LocalizeValue;
import consulo.spring.localize.SpringLocalize;
import consulo.util.lang.text.StringTokenizer;
import consulo.xml.psi.xml.XmlFile;
import consulo.xml.util.xml.DomFileElement;
import consulo.xml.util.xml.GenericAttributeValue;
import consulo.xml.util.xml.highlighting.DomElementAnnotationHolder;
import jakarta.annotation.Nonnull;

import java.util.List;

@ExtensionImpl
public class DuplicatedBeanNamesInspection extends SpringBeanInspectionBase<Object> {
    @Override
    public void checkFileElement(final DomFileElement<Beans> domFileElement, final DomElementAnnotationHolder holder, Object state) {
        final XmlFile xmlFile = domFileElement.getFile();
        final SpringModel model = SpringManager.getInstance(xmlFile.getProject()).getSpringModelByFile(xmlFile);
        final Beans beans = domFileElement.getRootElement();
        for (CommonSpringBean bean : SpringUtils.getChildBeans(beans, false)) {
            if (bean instanceof DomSpringBean) {
                checkBean((DomSpringBean) bean, holder, model);
            }
        }
        for (Alias alias : beans.getAliases()) {
            checkAlias(alias, holder, model);
        }
    }

    private static void checkBean(final DomSpringBean bean, final DomElementAnnotationHolder holder, final SpringModel springModel) {
        final String id = bean.getId().getStringValue();
        if (id != null && springModel.isNameDuplicated(id)) {
            holder.createProblem(bean.getId(), HighlightSeverity.ERROR, SpringLocalize.springBeanDublicateBeanName().get());
        }

        if (bean instanceof SpringBean) {
            final SpringBean springBean = (SpringBean) bean;
            final GenericAttributeValue<List<String>> name = springBean.getName();
            final String value = name.getStringValue();
            if (value != null) {
                final StringTokenizer tokenizer = new StringTokenizer(value, SpringUtils.SPRING_DELIMITERS);
                while (tokenizer.hasMoreTokens()) {
                    final String s = tokenizer.nextToken();
                    if (springModel.isNameDuplicated(s)) {
                        holder.createProblem(
                            name,
                            HighlightSeverity.ERROR,
                            SpringLocalize.springBeanDublicateBeanName().get(),
                            TextRange.from(tokenizer.getCurrentPosition() - s.length() + 1, s.length())
                        );
                    }
                }
            }
        }
    }

    private static void checkAlias(final Alias alias, final DomElementAnnotationHolder holder, final SpringModel model) {
        final GenericAttributeValue<String> value = alias.getAlias();
        final String aliasName = value.getStringValue();
        if (aliasName != null && model.isNameDuplicated(aliasName)) {
            holder.createProblem(value, HighlightSeverity.ERROR, SpringLocalize.springBeanDublicateBeanName().get());
        }
    }

    @Nonnull
    @Override
    public LocalizeValue getDisplayName() {
        return SpringLocalize.springBeanDuplicatedBeanNameInspection();
    }

    @Nonnull
    @Override
    public String getShortName() {
        return "DuplicatedBeanNamesInspection";
    }
}