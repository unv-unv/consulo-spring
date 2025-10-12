/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */

package com.intellij.spring.impl.ide.model.highlighting;

import com.intellij.spring.impl.ide.SpringManager;
import com.intellij.spring.impl.ide.SpringModel;
import com.intellij.spring.impl.ide.model.xml.DomSpringBean;
import com.intellij.spring.impl.ide.model.xml.beans.Beans;
import consulo.localize.LocalizeValue;
import consulo.spring.localize.SpringLocalize;
import consulo.xml.psi.xml.XmlFile;
import consulo.xml.util.xml.DomElement;
import consulo.xml.util.xml.DomFileElement;
import consulo.xml.util.xml.DomUtil;
import consulo.xml.util.xml.GenericDomValue;
import consulo.xml.util.xml.highlighting.DomElementAnnotationHolder;
import consulo.xml.util.xml.highlighting.DomElementsInspection;
import jakarta.annotation.Nonnull;

import java.util.function.Consumer;

/**
 * @author Dmitry Avdeev
 */
public abstract class DomSpringBeanInspectionBase<State> extends DomElementsInspection<Beans, State> {
    public DomSpringBeanInspectionBase() {
        super(Beans.class);
    }

    @Nonnull
    @Override
    public LocalizeValue getGroupDisplayName() {
        return SpringLocalize.modelInspectionGroupName();
    }

    @Override
    public void checkFileElement(final DomFileElement<Beans> domFileElement, final DomElementAnnotationHolder holder, State state) {
        final XmlFile xmlFile = domFileElement.getFile();
        final Beans beans = domFileElement.getRootElement();
        final SpringModel model = SpringManager.getInstance(xmlFile.getProject()).getSpringModelByFile(xmlFile);
        final Consumer<DomElement> consumer = new Consumer<DomElement>() {
            public void accept(final DomElement element) {
                if (element instanceof DomSpringBean) {
                    checkBean((DomSpringBean) element, beans, holder, model);
                }
                else if (!(element instanceof GenericDomValue) && DomUtil.hasXml(element)) {
                    checkChildren(element, this);
                }
            }
        };
        consumer.accept(domFileElement.getRootElement());
    }

    protected void checkBean(
        DomSpringBean springBean,
        final Beans beans,
        final DomElementAnnotationHolder holder,
        final SpringModel springModel
    ) {
    }
}