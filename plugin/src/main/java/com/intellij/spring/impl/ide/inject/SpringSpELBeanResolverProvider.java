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

package com.intellij.spring.impl.ide.inject;

import com.intellij.java.language.psi.PsiClass;
import com.intellij.spring.impl.ide.SpringManager;
import com.intellij.spring.impl.ide.SpringModel;
import com.intellij.spring.impl.ide.model.xml.beans.SpringBeanPointer;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.inject.InjectedLanguageManager;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiLanguageInjectionHost;
import consulo.language.util.ModuleUtilCore;
import consulo.module.Module;
import consulo.spring.spel.language.SpELBeanResolverProvider;
import org.jspecify.annotations.Nullable;

@ExtensionImpl
public class SpringSpELBeanResolverProvider implements SpELBeanResolverProvider {
    @Override
    public @Nullable PsiElement resolveBean(String beanName, PsiElement context) {
        SpringBeanPointer pointer = findBeanPointer(beanName, context);
        return pointer != null ? pointer.getPsiElement() : null;
    }

    @Override
    public @Nullable PsiClass resolveBeanClass(String beanName, PsiElement context) {
        SpringBeanPointer pointer = findBeanPointer(beanName, context);
        return pointer != null ? pointer.getBeanClass() : null;
    }

    private @Nullable SpringBeanPointer findBeanPointer(String beanName, PsiElement context) {
        Module module = findModule(context);
        if (module == null) {
            return null;
        }

        SpringModel model = SpringManager.getInstance(context.getProject()).getModel(module);
        if (model == null) {
            return null;
        }

        return model.findBean(beanName);
    }

    private @Nullable Module findModule(PsiElement element) {
        Module module = ModuleUtilCore.findModuleForPsiElement(element);
        if (module != null) {
            return module;
        }

        PsiLanguageInjectionHost host = InjectedLanguageManager.getInstance(element.getProject())
            .getInjectionHost(element);
        if (host != null) {
            return ModuleUtilCore.findModuleForPsiElement(host);
        }

        return null;
    }
}
