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

import consulo.language.ast.ASTNode;
import consulo.language.ast.IElementType;
import consulo.language.psi.PsiElement;
import consulo.spring.spel.language.SpELElementTypes;

public final class SpELElementFactory {
    public static PsiElement createElement(ASTNode node) {
        IElementType type = node.getElementType();

        if (type == SpELElementTypes.LITERAL_EXPRESSION) {
            return new SpELLiteralExpressionImpl(node);
        }
        if (type == SpELElementTypes.REFERENCE_EXPRESSION) {
            return new SpELReferenceExpressionImpl(node);
        }
        if (type == SpELElementTypes.METHOD_CALL_EXPRESSION) {
            return new SpELMethodCallExpressionImpl(node);
        }
        if (type == SpELElementTypes.BINARY_EXPRESSION) {
            return new SpELBinaryExpressionImpl(node);
        }
        if (type == SpELElementTypes.PREFIX_EXPRESSION) {
            return new SpELPrefixExpressionImpl(node);
        }
        if (type == SpELElementTypes.CONDITIONAL_EXPRESSION) {
            return new SpELConditionalExpressionImpl(node);
        }
        if (type == SpELElementTypes.NEW_EXPRESSION) {
            return new SpELNewExpressionImpl(node);
        }
        if (type == SpELElementTypes.PARENTHESIZED_EXPRESSION) {
            return new SpELParenthesizedExpressionImpl(node);
        }
        if (type == SpELElementTypes.EXPRESSION_LIST) {
            return new SpELExpressionListImpl(node);
        }
        if (type == SpELElementTypes.ASSIGNMENT_EXPRESSION) {
            return new SpELAssignmentExpressionImpl(node);
        }
        if (type == SpELElementTypes.INDEXER_EXPRESSION) {
            return new SpELIndexerExpressionImpl(node);
        }
        if (type == SpELElementTypes.BEAN_REFERENCE) {
            return new SpELBeanReferenceImpl(node);
        }
        if (type == SpELElementTypes.VARIABLE_REFERENCE) {
            return new SpELVariableReferenceImpl(node);
        }
        if (type == SpELElementTypes.TYPE_REFERENCE) {
            return new SpELTypeReferenceImpl(node);
        }
        if (type == SpELElementTypes.QUALIFIED_NAME) {
            return new SpELQualifiedNameImpl(node);
        }
        if (type == SpELElementTypes.ELVIS_EXPRESSION) {
            return new SpELElvisExpressionImpl(node);
        }
        if (type == SpELElementTypes.SAFE_NAV_EXPRESSION) {
            return new SpELSafeNavExpressionImpl(node);
        }
        if (type == SpELElementTypes.PROJECTION_EXPRESSION) {
            return new SpELProjectionExpressionImpl(node);
        }
        if (type == SpELElementTypes.SELECTION_EXPRESSION) {
            return new SpELSelectionExpressionImpl(node);
        }
        if (type == SpELElementTypes.SELECT_LAST_EXPRESSION) {
            return new SpELSelectLastExpressionImpl(node);
        }
        if (type == SpELElementTypes.SELECT_FIRST_EXPRESSION) {
            return new SpELSelectFirstExpressionImpl(node);
        }
        if (type == SpELElementTypes.PROPERTY_PLACEHOLDER) {
            return new SpELPropertyPlaceholderImpl(node);
        }
        if (type == SpELElementTypes.PLACEHOLDER_KEY) {
            return new SpELPlaceholderKeyImpl(node);
        }
        if (type == SpELElementTypes.PLACEHOLDER_DEFAULT_VALUE) {
            return new SpELPlaceholderDefaultValueImpl(node);
        }
        if (type == SpELElementTypes.INLINE_LIST) {
            return new SpELInlineListImpl(node);
        }
        if (type == SpELElementTypes.INLINE_MAP) {
            return new SpELInlineMapImpl(node);
        }
        if (type == SpELElementTypes.MAP_ENTRY) {
            return new SpELMapEntryImpl(node);
        }

        return new SpELElementImpl(node);
    }

    private SpELElementFactory() {
    }
}
