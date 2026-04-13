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

package consulo.spring.spel.language.impl.highlight;

import com.intellij.java.language.psi.PsiField;
import com.intellij.java.language.psi.PsiMethod;
import consulo.annotation.access.RequiredReadAction;
import consulo.codeEditor.DefaultLanguageHighlighterColors;
import consulo.colorScheme.TextAttributesKey;
import consulo.language.ast.ASTNode;
import consulo.language.editor.annotation.AnnotationHolder;
import consulo.language.editor.annotation.Annotator;
import consulo.language.editor.annotation.HighlightSeverity;
import consulo.language.psi.PsiElement;
import consulo.spring.spel.language.SpELElementTypes;
import consulo.spring.spel.language.SpELTokenTypes;
import consulo.spring.spel.language.impl.psi.*;

public class SpELSemanticHighlighter implements Annotator {
    private static final TextAttributesKey PROPERTY_ACCESS = TextAttributesKey.createTextAttributesKey(
        "SPEL_PROPERTY_ACCESS", DefaultLanguageHighlighterColors.INSTANCE_FIELD
    );
    private static final TextAttributesKey METHOD_CALL = TextAttributesKey.createTextAttributesKey(
        "SPEL_METHOD_CALL", DefaultLanguageHighlighterColors.FUNCTION_CALL
    );
    private static final TextAttributesKey BEAN_REFERENCE = TextAttributesKey.createTextAttributesKey(
        "SPEL_BEAN_REFERENCE", DefaultLanguageHighlighterColors.INSTANCE_FIELD
    );
    private static final TextAttributesKey VARIABLE_REFERENCE = TextAttributesKey.createTextAttributesKey(
        "SPEL_VARIABLE_REFERENCE", DefaultLanguageHighlighterColors.LOCAL_VARIABLE
    );
    private static final TextAttributesKey TYPE_REFERENCE = TextAttributesKey.createTextAttributesKey(
        "SPEL_TYPE_REFERENCE", DefaultLanguageHighlighterColors.CLASS_NAME
    );
    private static final TextAttributesKey PLACEHOLDER_KEY_ATTR = TextAttributesKey.createTextAttributesKey(
        "SPEL_PLACEHOLDER_KEY", DefaultLanguageHighlighterColors.INSTANCE_FIELD
    );
    private static final TextAttributesKey STATIC_METHOD_CALL = TextAttributesKey.createTextAttributesKey(
        "SPEL_STATIC_METHOD_CALL", DefaultLanguageHighlighterColors.STATIC_METHOD
    );
    private static final TextAttributesKey STATIC_FIELD_ACCESS = TextAttributesKey.createTextAttributesKey(
        "SPEL_STATIC_FIELD_ACCESS", DefaultLanguageHighlighterColors.STATIC_FIELD
    );

    @Override
    @RequiredReadAction
    public void annotate(PsiElement element, AnnotationHolder holder) {
        if (element instanceof SpELReferenceExpressionImpl refExpr) {
            highlightReferenceExpression(refExpr, holder);
        }
        else if (element instanceof SpELMethodCallExpressionImpl methodCall) {
            highlightMethodCall(methodCall, holder);
        }
        else if (element instanceof SpELBeanReferenceImpl beanRef) {
            highlightBeanReference(beanRef, holder);
        }
        else if (element instanceof SpELVariableReferenceImpl varRef) {
            highlightVariableReference(varRef, holder);
        }
        else if (element instanceof SpELTypeReferenceImpl typeRef) {
            highlightTypeReference(typeRef, holder);
        }
        else if (element instanceof SpELPlaceholderKeyImpl placeholderKey) {
            highlightPlaceholderKey(placeholderKey, holder);
        }
    }

    @RequiredReadAction
    private void highlightReferenceExpression(SpELReferenceExpressionImpl refExpr, AnnotationHolder holder) {
        ASTNode idNode = findLastIdentifier(refExpr);
        if (idNode == null) {
            return;
        }

        PsiElement resolved = refExpr.resolve();
        if (resolved instanceof PsiMethod method) {
            TextAttributesKey key = method.hasModifierProperty("static") ? STATIC_METHOD_CALL : METHOD_CALL;
            holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                .range(idNode)
                .textAttributes(key)
                .create();
        }
        else if (resolved instanceof PsiField field) {
            TextAttributesKey key = field.hasModifierProperty("static") ? STATIC_FIELD_ACCESS : PROPERTY_ACCESS;
            holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                .range(idNode)
                .textAttributes(key)
                .create();
        }
        else if (refExpr.getQualifier() == null) {
            // unqualified reference = bean name, highlight like instance field
            holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                .range(idNode)
                .textAttributes(BEAN_REFERENCE)
                .create();
        }
        else {
            // qualified but unresolved - still highlight as property
            holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                .range(idNode)
                .textAttributes(PROPERTY_ACCESS)
                .create();
        }
    }

    @RequiredReadAction
    private void highlightMethodCall(SpELMethodCallExpressionImpl methodCall, AnnotationHolder holder) {
        // the method name is inside the REFERENCE_EXPRESSION child - handled there
    }

    @RequiredReadAction
    private void highlightBeanReference(SpELBeanReferenceImpl beanRef, AnnotationHolder holder) {
        ASTNode idNode = beanRef.getNode().findChildByType(SpELTokenTypes.IDENTIFIER);
        if (idNode != null) {
            holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                .range(idNode)
                .textAttributes(BEAN_REFERENCE)
                .create();
        }
    }

    @RequiredReadAction
    private void highlightVariableReference(SpELVariableReferenceImpl varRef, AnnotationHolder holder) {
        ASTNode idNode = varRef.getNode().findChildByType(SpELTokenTypes.IDENTIFIER);
        if (idNode != null) {
            holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                .range(idNode)
                .textAttributes(VARIABLE_REFERENCE)
                .create();
        }
    }

    @RequiredReadAction
    private void highlightTypeReference(SpELTypeReferenceImpl typeRef, AnnotationHolder holder) {
        ASTNode qualifiedName = typeRef.getNode().findChildByType(SpELElementTypes.QUALIFIED_NAME);
        if (qualifiedName != null) {
            // highlight each identifier segment in the qualified name as class name
            for (ASTNode child = qualifiedName.getFirstChildNode(); child != null; child = child.getTreeNext()) {
                if (child.getElementType() == SpELTokenTypes.IDENTIFIER) {
                    holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                        .range(child)
                        .textAttributes(TYPE_REFERENCE)
                        .create();
                }
            }
        }
    }

    @RequiredReadAction
    private void highlightPlaceholderKey(SpELPlaceholderKeyImpl placeholderKey, AnnotationHolder holder) {
        holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
            .range(placeholderKey)
            .textAttributes(PLACEHOLDER_KEY_ATTR)
            .create();
    }

    private ASTNode findLastIdentifier(SpELReferenceExpressionImpl refExpr) {
        ASTNode lastId = null;
        for (ASTNode child = refExpr.getNode().getFirstChildNode(); child != null; child = child.getTreeNext()) {
            if (child.getElementType() == SpELTokenTypes.IDENTIFIER) {
                lastId = child;
            }
        }
        return lastId;
    }
}
