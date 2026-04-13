/*
 * Copyright 2013-2026 consulo.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.intellij.spring.impl.ide.gutter;

import com.intellij.spring.impl.ide.SpringManager;
import com.intellij.spring.impl.ide.SpringModel;
import com.intellij.spring.impl.ide.model.xml.beans.SpringBeanPointer;
import consulo.annotation.access.RequiredReadAction;
import consulo.language.editor.annotation.AnnotationHolder;
import consulo.language.editor.annotation.Annotator;
import consulo.language.editor.ui.navigation.NavigationGutterIconBuilder;
import consulo.language.psi.PsiElement;
import consulo.module.Module;
import consulo.spring.impl.icon.SpringImplIconGroup;
import consulo.spring.spel.language.impl.psi.SpELBeanReferenceImpl;

public class SpELAnnotator implements Annotator {
    @Override
    @RequiredReadAction
    public void annotate(PsiElement element, AnnotationHolder holder) {
        if (element instanceof SpELBeanReferenceImpl beanRef) {
            annotateBeanReference(beanRef, holder);
        }
    }

    @RequiredReadAction
    private void annotateBeanReference(SpELBeanReferenceImpl beanRef, AnnotationHolder holder) {
        String beanName = beanRef.getBeanName();
        if (beanName == null || beanName.isEmpty()) {
            return;
        }

        Module module = beanRef.getModule();
        if (module == null) {
            return;
        }

        SpringModel model = SpringManager.getInstance(beanRef.getProject()).getModel(module);
        if (model == null) {
            return;
        }

        SpringBeanPointer beanPointer = model.findBean(beanName);
        if (beanPointer == null) {
            return;
        }

        PsiElement beanElement = beanPointer.getPsiElement();
        if (beanElement == null) {
            return;
        }

        NavigationGutterIconBuilder.create(SpringImplIconGroup.springbean())
            .setTarget(beanElement)
            .setTooltipText("Navigate to bean '" + beanName + "'")
            .install(holder, beanRef);
    }
}
