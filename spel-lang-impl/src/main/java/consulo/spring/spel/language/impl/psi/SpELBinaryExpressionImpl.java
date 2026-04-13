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
import org.jspecify.annotations.Nullable;

public class SpELBinaryExpressionImpl extends SpELElementImpl implements PsiExpression {
    public SpELBinaryExpressionImpl(ASTNode node) {
        super(node);
    }

    public @Nullable PsiExpression getLeftOperand() {
        for (PsiElement child = getFirstChild(); child != null; child = child.getNextSibling()) {
            if (child instanceof PsiExpression) {
                return (PsiExpression) child;
            }
        }
        return null;
    }

    public @Nullable PsiExpression getRightOperand() {
        boolean foundFirst = false;
        for (PsiElement child = getFirstChild(); child != null; child = child.getNextSibling()) {
            if (child instanceof PsiExpression) {
                if (foundFirst) {
                    return (PsiExpression) child;
                }
                foundFirst = true;
            }
        }
        return null;
    }

    @Override
    public @Nullable PsiType getType() {
        // TODO: needs TypeConversionUtil which is complex
        return null;
    }
}
