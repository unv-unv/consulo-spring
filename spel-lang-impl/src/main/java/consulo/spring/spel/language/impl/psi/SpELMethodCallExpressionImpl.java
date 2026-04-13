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
import com.intellij.java.language.psi.PsiMethod;
import com.intellij.java.language.psi.PsiType;
import consulo.language.ast.ASTNode;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiReference;
import consulo.spring.spel.language.SpELElementTypes;
import org.jspecify.annotations.Nullable;

public class SpELMethodCallExpressionImpl extends SpELElementImpl implements PsiExpression {
    public SpELMethodCallExpressionImpl(ASTNode node) {
        super(node);
    }

    public @Nullable PsiExpression getMethodExpression() {
        for (ASTNode child = getNode().getFirstChildNode(); child != null; child = child.getTreeNext()) {
            if (child.getElementType() == SpELElementTypes.REFERENCE_EXPRESSION
                    || child.getElementType() == SpELElementTypes.SAFE_NAV_EXPRESSION) {
                PsiElement psi = child.getPsi();
                if (psi instanceof PsiExpression) {
                    return (PsiExpression) psi;
                }
            }
        }
        return null;
    }

    public @Nullable PsiElement getArgumentList() {
        ASTNode listNode = getNode().findChildByType(SpELElementTypes.EXPRESSION_LIST);
        return listNode != null ? listNode.getPsi() : null;
    }

    @Override
    public @Nullable PsiType getType() {
        PsiExpression methodExpr = getMethodExpression();
        if (methodExpr instanceof SpELReferenceExpressionImpl) {
            PsiElement resolved = ((SpELReferenceExpressionImpl) methodExpr).resolve();
            if (resolved instanceof PsiMethod) {
                return ((PsiMethod) resolved).getReturnType();
            }
        }
        return null;
    }

    @Override
    public PsiReference getReference() {
        PsiExpression methodExpr = getMethodExpression();
        return methodExpr != null ? methodExpr.getReference() : null;
    }

    @Override
    public PsiReference[] getReferences() {
        PsiExpression methodExpr = getMethodExpression();
        PsiReference[] ownRefs = methodExpr != null ? methodExpr.getReferences() : PsiReference.EMPTY_ARRAY;
        return mergeWithContributed(ownRefs);
    }
}
