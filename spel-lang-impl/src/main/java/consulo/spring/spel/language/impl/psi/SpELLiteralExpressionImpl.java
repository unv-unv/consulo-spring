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
import consulo.language.ast.IElementType;
import consulo.spring.spel.language.SpELTokenTypes;
import org.jspecify.annotations.Nullable;

public class SpELLiteralExpressionImpl extends SpELElementImpl implements PsiExpression {
    public SpELLiteralExpressionImpl(ASTNode node) {
        super(node);
    }

    @Override
    public @Nullable PsiType getType() {
        ASTNode firstChild = getNode().getFirstChildNode();
        if (firstChild == null) {
            return null;
        }

        IElementType tokenType = firstChild.getElementType();
        String text = firstChild.getText();

        if (tokenType == SpELTokenTypes.INTEGER_LITERAL) {
            if (text.endsWith("L") || text.endsWith("l")) {
                return PsiType.LONG;
            }
            return PsiType.INT;
        }

        if (tokenType == SpELTokenTypes.REAL_LITERAL) {
            if (text.endsWith("F") || text.endsWith("f")) {
                return PsiType.FLOAT;
            }
            return PsiType.DOUBLE;
        }

        if (tokenType == SpELTokenTypes.STRING_LITERAL) {
            return JavaPsiFacade.getInstance(getProject())
                    .getElementFactory()
                    .createTypeByFQClassName("java.lang.String", getResolveScope());
        }

        if (tokenType == SpELTokenTypes.TRUE || tokenType == SpELTokenTypes.FALSE) {
            return PsiType.BOOLEAN;
        }

        if (tokenType == SpELTokenTypes.NULL) {
            return PsiType.NULL;
        }

        return null;
    }
}
