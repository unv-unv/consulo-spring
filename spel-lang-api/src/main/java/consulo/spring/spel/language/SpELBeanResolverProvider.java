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

package consulo.spring.spel.language;

import com.intellij.java.language.psi.PsiClass;
import consulo.annotation.component.ComponentScope;
import consulo.annotation.component.ExtensionAPI;
import consulo.component.extension.ExtensionPointName;
import consulo.language.psi.PsiElement;
import org.jspecify.annotations.Nullable;

/**
 * Extension point for resolving Spring bean references from SpEL expressions.
 * Implemented by the Spring plugin module which has access to SpringManager/SpringModel.
 */
@ExtensionAPI(ComponentScope.APPLICATION)
public interface SpELBeanResolverProvider {
    ExtensionPointName<SpELBeanResolverProvider> EP_NAME = ExtensionPointName.create(SpELBeanResolverProvider.class);

    /**
     * Resolve a Spring bean by name and return its declaring PsiElement
     * (the @Component class, @Bean method, or XML bean tag).
     */
    @Nullable
    PsiElement resolveBean(String beanName, PsiElement context);

    /**
     * Resolve the PsiClass type of a Spring bean by name.
     */
    @Nullable
    PsiClass resolveBeanClass(String beanName, PsiElement context);
}
