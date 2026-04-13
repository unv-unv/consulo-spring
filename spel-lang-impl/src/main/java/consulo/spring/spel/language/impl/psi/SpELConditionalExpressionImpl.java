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

import com.intellij.java.language.psi.PsiExpression;
import com.intellij.java.language.psi.PsiType;
import consulo.language.ast.ASTNode;
import consulo.language.psi.PsiElement;
import consulo.spring.spel.language.SpELTokenTypes;
import org.jspecify.annotations.Nullable;

public class SpELConditionalExpressionImpl extends SpELElementImpl implements PsiExpression {
    public SpELConditionalExpressionImpl(ASTNode node) {
        super(node);
    }

    public @Nullable PsiExpression getCondition() {
        for (PsiElement child = getFirstChild(); child != null; child = child.getNextSibling()) {
            if (child instanceof PsiExpression) {
                return (PsiExpression) child;
            }
        }
        return null;
    }

    public @Nullable PsiExpression getThenExpression() {
        boolean pastQmark = false;
        for (PsiElement child = getFirstChild(); child != null; child = child.getNextSibling()) {
            if (child.getNode().getElementType() == SpELTokenTypes.QMARK) {
                pastQmark = true;
                continue;
            }
            if (pastQmark && child instanceof PsiExpression) {
                return (PsiExpression) child;
            }
        }
        return null;
    }

    public @Nullable PsiExpression getElseExpression() {
        boolean pastColon = false;
        for (PsiElement child = getFirstChild(); child != null; child = child.getNextSibling()) {
            if (child.getNode().getElementType() == SpELTokenTypes.COLON) {
                pastColon = true;
                continue;
            }
            if (pastColon && child instanceof PsiExpression) {
                return (PsiExpression) child;
            }
        }
        return null;
    }

    @Override
    public @Nullable PsiType getType() {
        PsiExpression thenExpr = getThenExpression();
        return thenExpr != null ? thenExpr.getType() : null;
    }
}
