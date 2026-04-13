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

import consulo.language.ast.IElementType;

public final class SpELElementTypes {
    // Java-compatible expression types
    public static final IElementType REFERENCE_EXPRESSION = new SpELElementType("REFERENCE_EXPRESSION");
    public static final IElementType METHOD_CALL_EXPRESSION = new SpELElementType("METHOD_CALL_EXPRESSION");
    public static final IElementType LITERAL_EXPRESSION = new SpELElementType("LITERAL_EXPRESSION");
    public static final IElementType BINARY_EXPRESSION = new SpELElementType("BINARY_EXPRESSION");
    public static final IElementType PREFIX_EXPRESSION = new SpELElementType("PREFIX_EXPRESSION");
    public static final IElementType CONDITIONAL_EXPRESSION = new SpELElementType("CONDITIONAL_EXPRESSION");
    public static final IElementType NEW_EXPRESSION = new SpELElementType("NEW_EXPRESSION");
    public static final IElementType PARENTHESIZED_EXPRESSION = new SpELElementType("PARENTHESIZED_EXPRESSION");
    public static final IElementType EXPRESSION_LIST = new SpELElementType("EXPRESSION_LIST");
    public static final IElementType INDEXER_EXPRESSION = new SpELElementType("INDEXER_EXPRESSION");
    public static final IElementType ASSIGNMENT_EXPRESSION = new SpELElementType("ASSIGNMENT_EXPRESSION");

    // SpEL-specific expression types
    public static final IElementType BEAN_REFERENCE = new SpELElementType("BEAN_REFERENCE");
    public static final IElementType VARIABLE_REFERENCE = new SpELElementType("VARIABLE_REFERENCE");
    public static final IElementType TYPE_REFERENCE = new SpELElementType("TYPE_REFERENCE");
    public static final IElementType QUALIFIED_NAME = new SpELElementType("QUALIFIED_NAME");
    public static final IElementType ELVIS_EXPRESSION = new SpELElementType("ELVIS_EXPRESSION");
    public static final IElementType SAFE_NAV_EXPRESSION = new SpELElementType("SAFE_NAV_EXPRESSION");
    public static final IElementType PROJECTION_EXPRESSION = new SpELElementType("PROJECTION_EXPRESSION");
    public static final IElementType SELECTION_EXPRESSION = new SpELElementType("SELECTION_EXPRESSION");
    public static final IElementType SELECT_LAST_EXPRESSION = new SpELElementType("SELECT_LAST_EXPRESSION");
    public static final IElementType SELECT_FIRST_EXPRESSION = new SpELElementType("SELECT_FIRST_EXPRESSION");
    public static final IElementType PROPERTY_PLACEHOLDER = new SpELElementType("PROPERTY_PLACEHOLDER");
    public static final IElementType INLINE_LIST = new SpELElementType("INLINE_LIST");
    public static final IElementType INLINE_MAP = new SpELElementType("INLINE_MAP");
    public static final IElementType MAP_ENTRY = new SpELElementType("MAP_ENTRY");

    private SpELElementTypes() {
    }
}
