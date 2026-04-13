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

package consulo.spring.spel.language.impl.psi;

import com.intellij.java.language.psi.JavaPsiFacade;
import com.intellij.java.language.psi.PsiExpression;
import com.intellij.java.language.psi.PsiType;
import consulo.language.ast.ASTNode;
import consulo.spring.spel.language.SpELElementTypes;
import org.jspecify.annotations.Nullable;

public class SpELPropertyPlaceholderImpl extends SpELElementImpl implements PsiExpression {
    public SpELPropertyPlaceholderImpl(ASTNode node) {
        super(node);
    }

    /**
     * Returns the property key, e.g. "app.feature.enabled" from ${app.feature.enabled:false}
     */
    public @Nullable String getPropertyKey() {
        ASTNode keyNode = getNode().findChildByType(SpELElementTypes.PLACEHOLDER_KEY);
        return keyNode != null ? keyNode.getText() : null;
    }

    /**
     * Returns the PLACEHOLDER_KEY PSI element for reference targeting.
     */
    public @Nullable SpELPlaceholderKeyImpl getKeyElement() {
        ASTNode keyNode = getNode().findChildByType(SpELElementTypes.PLACEHOLDER_KEY);
        return keyNode != null ? (SpELPlaceholderKeyImpl) keyNode.getPsi() : null;
    }

    @Override
    public @Nullable PsiType getType() {
        // property placeholders resolve to String at runtime
        return JavaPsiFacade.getInstance(getProject())
            .getElementFactory()
            .createTypeByFQClassName("java.lang.String", getResolveScope());
    }
}
