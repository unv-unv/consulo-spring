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

public final class SpELTokenTypes {
    // literals
    public static final IElementType INTEGER_LITERAL = new SpELTokenType("INTEGER_LITERAL");
    public static final IElementType REAL_LITERAL = new SpELTokenType("REAL_LITERAL");
    public static final IElementType STRING_LITERAL = new SpELTokenType("STRING_LITERAL");

    // keywords
    public static final IElementType TRUE = new SpELTokenType("TRUE");
    public static final IElementType FALSE = new SpELTokenType("FALSE");
    public static final IElementType NULL = new SpELTokenType("NULL");
    public static final IElementType NEW = new SpELTokenType("NEW");
    public static final IElementType INSTANCEOF = new SpELTokenType("INSTANCEOF");
    public static final IElementType MATCHES = new SpELTokenType("MATCHES");
    public static final IElementType AND = new SpELTokenType("AND");
    public static final IElementType OR = new SpELTokenType("OR");
    public static final IElementType NOT = new SpELTokenType("NOT");
    public static final IElementType T_KEYWORD = new SpELTokenType("T");

    // operators
    public static final IElementType PLUS = new SpELTokenType("+");
    public static final IElementType MINUS = new SpELTokenType("-");
    public static final IElementType STAR = new SpELTokenType("*");
    public static final IElementType DIV = new SpELTokenType("/");
    public static final IElementType MOD = new SpELTokenType("%");
    public static final IElementType POWER = new SpELTokenType("^");
    public static final IElementType EQ = new SpELTokenType("==");
    public static final IElementType NE = new SpELTokenType("!=");
    public static final IElementType LT = new SpELTokenType("<");
    public static final IElementType GT = new SpELTokenType(">");
    public static final IElementType LE = new SpELTokenType("<=");
    public static final IElementType GE = new SpELTokenType(">=");
    public static final IElementType ASSIGN = new SpELTokenType("=");
    public static final IElementType QMARK = new SpELTokenType("?");
    public static final IElementType COLON = new SpELTokenType(":");
    public static final IElementType ELVIS = new SpELTokenType("?:");
    public static final IElementType SAFE_NAV = new SpELTokenType("?.");
    public static final IElementType BANG = new SpELTokenType("!");
    public static final IElementType AND_AND = new SpELTokenType("&&");
    public static final IElementType OR_OR = new SpELTokenType("||");

    // delimiters
    public static final IElementType LPAREN = new SpELTokenType("(");
    public static final IElementType RPAREN = new SpELTokenType(")");
    public static final IElementType LBRACKET = new SpELTokenType("[");
    public static final IElementType RBRACKET = new SpELTokenType("]");
    public static final IElementType LBRACE = new SpELTokenType("{");
    public static final IElementType RBRACE = new SpELTokenType("}");
    public static final IElementType DOT = new SpELTokenType(".");
    public static final IElementType COMMA = new SpELTokenType(",");
    public static final IElementType HASH = new SpELTokenType("#");
    public static final IElementType AT = new SpELTokenType("@");

    // special composite tokens
    public static final IElementType PROJECTION = new SpELTokenType(".![");
    public static final IElementType SELECTION = new SpELTokenType(".?[");
    public static final IElementType SELECT_LAST = new SpELTokenType(".$[");
    public static final IElementType SELECT_FIRST = new SpELTokenType(".^[");

    // identifiers
    public static final IElementType IDENTIFIER = new SpELTokenType("IDENTIFIER");

    // property placeholder
    public static final IElementType DOLLAR_LBRACE = new SpELTokenType("${");
    public static final IElementType PLACEHOLDER_CONTENT = new SpELTokenType("PLACEHOLDER_CONTENT");

    private SpELTokenTypes() {
    }
}
