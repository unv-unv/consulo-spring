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

import consulo.annotation.component.ExtensionImpl;
import consulo.document.util.TextRange;
import consulo.language.Language;
import consulo.language.inject.InjectedLanguageManager;
import consulo.language.psi.*;
import consulo.language.util.ModuleUtilCore;
import consulo.language.util.ProcessingContext;
import consulo.module.Module;
import consulo.spring.impl.boot.properties.SpringConfigurationPropertySearch;
import consulo.spring.spel.language.SpELLanguage;
import consulo.spring.spel.language.impl.psi.SpELPlaceholderKeyImpl;

import java.util.ArrayList;
import java.util.List;

import static consulo.language.pattern.PlatformPatterns.psiElement;

@ExtensionImpl
public class SpELPropertyReferenceContributor extends PsiReferenceContributor {
    @Override
    public void registerReferenceProviders(PsiReferenceRegistrar registrar) {
        registrar.registerReferenceProvider(
            psiElement(SpELPlaceholderKeyImpl.class),
            new PsiReferenceProvider() {
                @Override
                public PsiReference[] getReferencesByElement(PsiElement element, ProcessingContext context) {
                    if (!(element instanceof SpELPlaceholderKeyImpl keyElement)) {
                        return PsiReference.EMPTY_ARRAY;
                    }

                    String key = keyElement.getText();
                    if (key == null || key.isEmpty()) {
                        return PsiReference.EMPTY_ARRAY;
                    }

                    TextRange range = TextRange.from(0, key.length());
                    return new PsiReference[]{new SpELPropertyReference(element, range, key)};
                }
            }
        );
    }

    @Override
    public Language getLanguage() {
        return SpELLanguage.INSTANCE;
    }

    private static class SpELPropertyReference extends PsiReferenceBase<PsiElement> implements PsiPolyVariantReference {
        private final String myKey;

        SpELPropertyReference(PsiElement element, TextRange range, String key) {
            super(element, range, true);
            myKey = key;
        }

        @Override
        public ResolveResult[] multiResolve(boolean incompleteCode) {
            Module module = findModule();
            if (module == null) {
                return ResolveResult.EMPTY_ARRAY;
            }

            SpringConfigurationPropertySearch search = SpringConfigurationPropertySearch.getInstance(myElement.getProject());
            List<PsiElement> resolved = search.resolvePropertyKey(myKey, module);

            List<ResolveResult> results = new ArrayList<>(resolved.size());
            for (PsiElement element : resolved) {
                results.add(new PsiElementResolveResult(element));
            }
            return results.toArray(ResolveResult.EMPTY_ARRAY);
        }

        @Override
        public PsiElement resolve() {
            ResolveResult[] results = multiResolve(false);
            return results.length >= 1 ? results[0].getElement() : null;
        }

        @Override
        public Object[] getVariants() {
            Module module = findModule();
            if (module == null) {
                return EMPTY_ARRAY;
            }

            SpringConfigurationPropertySearch search = SpringConfigurationPropertySearch.getInstance(myElement.getProject());
            List<String> keys = search.getAllPropertyKeys(module);
            return keys.toArray();
        }

        private Module findModule() {
            Module module = ModuleUtilCore.findModuleForPsiElement(myElement);
            if (module != null) {
                return module;
            }
            PsiLanguageInjectionHost host = InjectedLanguageManager.getInstance(myElement.getProject())
                .getInjectionHost(myElement);
            if (host != null) {
                return ModuleUtilCore.findModuleForPsiElement(host);
            }
            return null;
        }
    }
}
