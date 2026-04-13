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

import com.intellij.spring.impl.ide.constants.SpringAnnotationsConstants;
import com.intellij.java.language.patterns.PsiJavaPatterns;
import com.intellij.java.language.psi.PsiLiteralExpression;
import consulo.annotation.access.RequiredReadAction;
import consulo.annotation.component.ExtensionImpl;
import consulo.document.util.TextRange;
import consulo.language.inject.ConcatenationAwareInjector;
import consulo.language.inject.MultiHostRegistrar;
import consulo.language.pattern.ElementPattern;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiLanguageInjectionHost;
import consulo.spring.spel.language.SpELLanguage;

import static consulo.language.pattern.StandardPatterns.string;

@ExtensionImpl
public class SpELValueInjector implements ConcatenationAwareInjector {
    private static final ElementPattern<PsiLiteralExpression> VALUE_PATTERN =
        PsiJavaPatterns.literalExpression()
            .withText(string().contains("#{"))
            .insideAnnotationParam(string().oneOf(SpringAnnotationsConstants.VALUE_ANNOTATION), "value");

    @Override
    @RequiredReadAction
    public void inject(MultiHostRegistrar registrar, PsiElement... operands) {
        PsiElement host = operands[0];
        if (!VALUE_PATTERN.accepts(host)) {
            return;
        }

        String text = host.getText();
        if (text.length() < 2) {
            return;
        }

        // remove surrounding quotes from the literal
        String content = text.substring(1, text.length() - 1);
        boolean hasInjection = false;

        int pos = 0;
        while (pos < content.length()) {
            int spelStart = content.indexOf("#{", pos);
            if (spelStart < 0) {
                break;
            }

            int depth = 1;
            int i = spelStart + 2;
            while (i < content.length() && depth > 0) {
                char c = content.charAt(i);
                if (c == '{') {
                    depth++;
                }
                else if (c == '}') {
                    depth--;
                }
                i++;
            }

            if (depth == 0) {
                // +1 for the opening quote of the string literal
                int rangeStart = spelStart + 2 + 1;
                int rangeEnd = i - 1 + 1;

                if (rangeStart < rangeEnd) {
                    if (!hasInjection) {
                        registrar.startInjecting(SpELLanguage.INSTANCE);
                        hasInjection = true;
                    }
                    registrar.addPlace(null, null, (PsiLanguageInjectionHost) host, new TextRange(rangeStart, rangeEnd));
                }
            }

            pos = i;
        }

        if (hasInjection) {
            registrar.doneInjecting();
        }
    }
}
