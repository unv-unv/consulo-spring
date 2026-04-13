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

package consulo.spring.spel.language.impl.parser;

import consulo.language.ast.ASTNode;
import consulo.language.ast.IElementType;
import consulo.language.parser.PsiBuilder;
import consulo.language.parser.PsiParser;
import consulo.language.version.LanguageVersion;
import consulo.spring.spel.language.SpELElementTypes;
import consulo.spring.spel.language.SpELTokenTypes;

public class SpELParser implements PsiParser {
    @Override
    public ASTNode parse(IElementType root, PsiBuilder builder, LanguageVersion languageVersion) {
        PsiBuilder.Marker rootMarker = builder.mark();
        if (!builder.eof()) {
            parseExpression(builder);
        }
        while (!builder.eof()) {
            builder.error("Unexpected token");
            builder.advanceLexer();
        }
        rootMarker.done(root);
        return builder.getTreeBuilt();
    }

    private void parseExpression(PsiBuilder builder) {
        parseAssignment(builder);
    }

    private void parseAssignment(PsiBuilder builder) {
        PsiBuilder.Marker marker = builder.mark();
        parseTernary(builder);
        if (builder.getTokenType() == SpELTokenTypes.ASSIGN) {
            builder.advanceLexer();
            parseTernary(builder);
            marker.done(SpELElementTypes.ASSIGNMENT_EXPRESSION);
        }
        else {
            marker.drop();
        }
    }

    private void parseTernary(PsiBuilder builder) {
        PsiBuilder.Marker marker = builder.mark();
        parseElvis(builder);
        if (builder.getTokenType() == SpELTokenTypes.QMARK) {
            builder.advanceLexer();
            parseExpression(builder);
            expect(builder, SpELTokenTypes.COLON, "':' expected");
            parseExpression(builder);
            marker.done(SpELElementTypes.CONDITIONAL_EXPRESSION);
        }
        else {
            marker.drop();
        }
    }

    private void parseElvis(PsiBuilder builder) {
        PsiBuilder.Marker marker = builder.mark();
        parseOr(builder);
        if (builder.getTokenType() == SpELTokenTypes.ELVIS) {
            builder.advanceLexer();
            parseOr(builder);
            marker.done(SpELElementTypes.ELVIS_EXPRESSION);
        }
        else {
            marker.drop();
        }
    }

    private void parseOr(PsiBuilder builder) {
        PsiBuilder.Marker marker = builder.mark();
        parseAnd(builder);
        while (builder.getTokenType() == SpELTokenTypes.OR_OR || builder.getTokenType() == SpELTokenTypes.OR) {
            builder.advanceLexer();
            parseAnd(builder);
            marker.done(SpELElementTypes.BINARY_EXPRESSION);
            marker = marker.precede();
        }
        marker.drop();
    }

    private void parseAnd(PsiBuilder builder) {
        PsiBuilder.Marker marker = builder.mark();
        parseEquality(builder);
        while (builder.getTokenType() == SpELTokenTypes.AND_AND || builder.getTokenType() == SpELTokenTypes.AND) {
            builder.advanceLexer();
            parseEquality(builder);
            marker.done(SpELElementTypes.BINARY_EXPRESSION);
            marker = marker.precede();
        }
        marker.drop();
    }

    private void parseEquality(PsiBuilder builder) {
        PsiBuilder.Marker marker = builder.mark();
        parseRelational(builder);
        while (builder.getTokenType() == SpELTokenTypes.EQ || builder.getTokenType() == SpELTokenTypes.NE) {
            builder.advanceLexer();
            parseRelational(builder);
            marker.done(SpELElementTypes.BINARY_EXPRESSION);
            marker = marker.precede();
        }
        marker.drop();
    }

    private void parseRelational(PsiBuilder builder) {
        PsiBuilder.Marker marker = builder.mark();
        parseAdditive(builder);
        IElementType token = builder.getTokenType();
        if (token == SpELTokenTypes.LT || token == SpELTokenTypes.GT
            || token == SpELTokenTypes.LE || token == SpELTokenTypes.GE
            || token == SpELTokenTypes.INSTANCEOF || token == SpELTokenTypes.MATCHES) {
            builder.advanceLexer();
            parseAdditive(builder);
            marker.done(SpELElementTypes.BINARY_EXPRESSION);
        }
        else {
            marker.drop();
        }
    }

    private void parseAdditive(PsiBuilder builder) {
        PsiBuilder.Marker marker = builder.mark();
        parseMultiplicative(builder);
        while (builder.getTokenType() == SpELTokenTypes.PLUS || builder.getTokenType() == SpELTokenTypes.MINUS) {
            builder.advanceLexer();
            parseMultiplicative(builder);
            marker.done(SpELElementTypes.BINARY_EXPRESSION);
            marker = marker.precede();
        }
        marker.drop();
    }

    private void parseMultiplicative(PsiBuilder builder) {
        PsiBuilder.Marker marker = builder.mark();
        parsePower(builder);
        while (builder.getTokenType() == SpELTokenTypes.STAR
            || builder.getTokenType() == SpELTokenTypes.DIV
            || builder.getTokenType() == SpELTokenTypes.MOD) {
            builder.advanceLexer();
            parsePower(builder);
            marker.done(SpELElementTypes.BINARY_EXPRESSION);
            marker = marker.precede();
        }
        marker.drop();
    }

    private void parsePower(PsiBuilder builder) {
        PsiBuilder.Marker marker = builder.mark();
        parseUnary(builder);
        if (builder.getTokenType() == SpELTokenTypes.POWER) {
            builder.advanceLexer();
            parseUnary(builder);
            marker.done(SpELElementTypes.BINARY_EXPRESSION);
        }
        else {
            marker.drop();
        }
    }

    private void parseUnary(PsiBuilder builder) {
        IElementType token = builder.getTokenType();
        if (token == SpELTokenTypes.MINUS || token == SpELTokenTypes.BANG || token == SpELTokenTypes.NOT) {
            PsiBuilder.Marker marker = builder.mark();
            builder.advanceLexer();
            parseUnary(builder);
            marker.done(SpELElementTypes.PREFIX_EXPRESSION);
        }
        else {
            parsePostfix(builder);
        }
    }

    // Postfix parsing with proper qualifier wrapping using precede()
    // For "foo.bar": REFERENCE_EXPRESSION[REFERENCE_EXPRESSION[IDENTIFIER "foo"] DOT IDENTIFIER "bar"]
    // For "foo.bar()": METHOD_CALL_EXPRESSION[REFERENCE_EXPRESSION[REFERENCE_EXPRESSION[IDENTIFIER "foo"] DOT IDENTIFIER "bar"] EXPRESSION_LIST]
    private void parsePostfix(PsiBuilder builder) {
        // expr wraps everything from primary onwards - used for dot-chaining
        PsiBuilder.Marker expr = builder.mark();
        parsePrimary(builder);

        while (!builder.eof()) {
            IElementType token = builder.getTokenType();
            if (token == SpELTokenTypes.DOT || token == SpELTokenTypes.SAFE_NAV) {
                builder.advanceLexer(); // consume DOT or ?.

                if (builder.getTokenType() == SpELTokenTypes.IDENTIFIER) {
                    // lookahead: method call or property?
                    PsiBuilder.Marker lookahead = builder.mark();
                    builder.advanceLexer(); // consume identifier
                    boolean isMethodCall = builder.getTokenType() == SpELTokenTypes.LPAREN;
                    lookahead.rollbackTo();

                    if (isMethodCall) {
                        // wrap qualifier + dot + methodName as REFERENCE_EXPRESSION
                        builder.advanceLexer(); // re-consume identifier
                        IElementType refType = token == SpELTokenTypes.SAFE_NAV
                            ? SpELElementTypes.SAFE_NAV_EXPRESSION
                            : SpELElementTypes.REFERENCE_EXPRESSION;
                        expr.done(refType);
                        // wrap reference + argList as METHOD_CALL_EXPRESSION
                        expr = expr.precede();
                        parseArgumentList(builder);
                        expr.done(SpELElementTypes.METHOD_CALL_EXPRESSION);
                        expr = expr.precede();
                    }
                    else {
                        // property access: wrap qualifier + dot + identifier as REFERENCE_EXPRESSION
                        builder.advanceLexer(); // consume identifier
                        IElementType refType = token == SpELTokenTypes.SAFE_NAV
                            ? SpELElementTypes.SAFE_NAV_EXPRESSION
                            : SpELElementTypes.REFERENCE_EXPRESSION;
                        expr.done(refType);
                        expr = expr.precede();
                    }
                }
                else {
                    builder.error("Identifier expected");
                    expr.done(SpELElementTypes.REFERENCE_EXPRESSION);
                    expr = expr.precede();
                }
            }
            else if (token == SpELTokenTypes.LPAREN) {
                // bare method call: foo(args) - the primary was already parsed as REFERENCE_EXPRESSION
                parseArgumentList(builder);
                expr.done(SpELElementTypes.METHOD_CALL_EXPRESSION);
                expr = expr.precede();
            }
            else if (token == SpELTokenTypes.LBRACKET) {
                builder.advanceLexer();
                parseExpression(builder);
                expect(builder, SpELTokenTypes.RBRACKET, "']' expected");
                expr.done(SpELElementTypes.INDEXER_EXPRESSION);
                expr = expr.precede();
            }
            else if (token == SpELTokenTypes.PROJECTION) {
                builder.advanceLexer();
                parseExpression(builder);
                expect(builder, SpELTokenTypes.RBRACKET, "']' expected");
                expr.done(SpELElementTypes.PROJECTION_EXPRESSION);
                expr = expr.precede();
            }
            else if (token == SpELTokenTypes.SELECTION) {
                builder.advanceLexer();
                parseExpression(builder);
                expect(builder, SpELTokenTypes.RBRACKET, "']' expected");
                expr.done(SpELElementTypes.SELECTION_EXPRESSION);
                expr = expr.precede();
            }
            else if (token == SpELTokenTypes.SELECT_LAST) {
                builder.advanceLexer();
                parseExpression(builder);
                expect(builder, SpELTokenTypes.RBRACKET, "']' expected");
                expr.done(SpELElementTypes.SELECT_LAST_EXPRESSION);
                expr = expr.precede();
            }
            else if (token == SpELTokenTypes.SELECT_FIRST) {
                builder.advanceLexer();
                parseExpression(builder);
                expect(builder, SpELTokenTypes.RBRACKET, "']' expected");
                expr.done(SpELElementTypes.SELECT_FIRST_EXPRESSION);
                expr = expr.precede();
            }
            else {
                break;
            }
        }
        expr.drop();
    }

    private void parsePrimary(PsiBuilder builder) {
        IElementType token = builder.getTokenType();
        if (token == null) {
            builder.error("Expression expected");
            return;
        }

        // literals
        if (token == SpELTokenTypes.INTEGER_LITERAL || token == SpELTokenTypes.REAL_LITERAL
            || token == SpELTokenTypes.STRING_LITERAL
            || token == SpELTokenTypes.TRUE || token == SpELTokenTypes.FALSE
            || token == SpELTokenTypes.NULL) {
            PsiBuilder.Marker marker = builder.mark();
            builder.advanceLexer();
            marker.done(SpELElementTypes.LITERAL_EXPRESSION);
        }
        // parenthesized expression
        else if (token == SpELTokenTypes.LPAREN) {
            PsiBuilder.Marker marker = builder.mark();
            builder.advanceLexer();
            parseExpression(builder);
            expect(builder, SpELTokenTypes.RPAREN, "')' expected");
            marker.done(SpELElementTypes.PARENTHESIZED_EXPRESSION);
        }
        // variable reference: #identifier
        else if (token == SpELTokenTypes.HASH) {
            PsiBuilder.Marker marker = builder.mark();
            builder.advanceLexer();
            expect(builder, SpELTokenTypes.IDENTIFIER, "Identifier expected after '#'");
            marker.done(SpELElementTypes.VARIABLE_REFERENCE);
        }
        // bean reference: @beanName
        else if (token == SpELTokenTypes.AT) {
            PsiBuilder.Marker marker = builder.mark();
            builder.advanceLexer();
            expect(builder, SpELTokenTypes.IDENTIFIER, "Bean name expected after '@'");
            marker.done(SpELElementTypes.BEAN_REFERENCE);
        }
        // type reference: T(qualified.Name)
        else if (token == SpELTokenTypes.T_KEYWORD) {
            PsiBuilder.Marker marker = builder.mark();
            builder.advanceLexer();
            expect(builder, SpELTokenTypes.LPAREN, "'(' expected after 'T'");
            parseQualifiedName(builder);
            expect(builder, SpELTokenTypes.RPAREN, "')' expected");
            marker.done(SpELElementTypes.TYPE_REFERENCE);
        }
        // constructor: new Type(args)
        else if (token == SpELTokenTypes.NEW) {
            PsiBuilder.Marker marker = builder.mark();
            builder.advanceLexer();
            parseQualifiedName(builder);
            parseArgumentList(builder);
            marker.done(SpELElementTypes.NEW_EXPRESSION);
        }
        // inline list/map
        else if (token == SpELTokenTypes.LBRACE) {
            parseInlineListOrMap(builder);
        }
        // property placeholder: ${key} or ${key:defaultValue}
        else if (token == SpELTokenTypes.DOLLAR_LBRACE) {
            PsiBuilder.Marker marker = builder.mark();
            builder.advanceLexer(); // consume ${

            // parse key: IDENTIFIER (DOT IDENTIFIER)*
            if (builder.getTokenType() == SpELTokenTypes.IDENTIFIER) {
                PsiBuilder.Marker keyMarker = builder.mark();
                builder.advanceLexer();
                while (builder.getTokenType() == SpELTokenTypes.DOT) {
                    builder.advanceLexer();
                    if (builder.getTokenType() == SpELTokenTypes.IDENTIFIER) {
                        builder.advanceLexer();
                    }
                }
                keyMarker.done(SpELElementTypes.PLACEHOLDER_KEY);
            }
            else if (builder.getTokenType() == SpELTokenTypes.PLACEHOLDER_CONTENT) {
                // fallback for non-standard key content
                PsiBuilder.Marker keyMarker = builder.mark();
                builder.advanceLexer();
                keyMarker.done(SpELElementTypes.PLACEHOLDER_KEY);
            }

            // parse optional :defaultValue
            if (builder.getTokenType() == SpELTokenTypes.COLON) {
                builder.advanceLexer(); // consume :
                if (builder.getTokenType() == SpELTokenTypes.PLACEHOLDER_CONTENT) {
                    PsiBuilder.Marker defaultMarker = builder.mark();
                    builder.advanceLexer();
                    defaultMarker.done(SpELElementTypes.PLACEHOLDER_DEFAULT_VALUE);
                }
            }

            expect(builder, SpELTokenTypes.RBRACE, "'}' expected");
            marker.done(SpELElementTypes.PROPERTY_PLACEHOLDER);
        }
        // identifier - always wraps as REFERENCE_EXPRESSION, method call handled by parsePostfix
        else if (token == SpELTokenTypes.IDENTIFIER) {
            PsiBuilder.Marker marker = builder.mark();
            builder.advanceLexer();
            marker.done(SpELElementTypes.REFERENCE_EXPRESSION);
        }
        else {
            builder.error("Expression expected");
            builder.advanceLexer();
        }
    }

    private void parseQualifiedName(PsiBuilder builder) {
        PsiBuilder.Marker marker = builder.mark();
        if (!expect(builder, SpELTokenTypes.IDENTIFIER, "Identifier expected")) {
            marker.drop();
            return;
        }
        while (builder.getTokenType() == SpELTokenTypes.DOT) {
            builder.advanceLexer();
            expect(builder, SpELTokenTypes.IDENTIFIER, "Identifier expected after '.'");
        }
        marker.done(SpELElementTypes.QUALIFIED_NAME);
    }

    private void parseArgumentList(PsiBuilder builder) {
        PsiBuilder.Marker marker = builder.mark();
        expect(builder, SpELTokenTypes.LPAREN, "'(' expected");
        if (builder.getTokenType() != SpELTokenTypes.RPAREN) {
            parseExpression(builder);
            while (builder.getTokenType() == SpELTokenTypes.COMMA) {
                builder.advanceLexer();
                parseExpression(builder);
            }
        }
        expect(builder, SpELTokenTypes.RPAREN, "')' expected");
        marker.done(SpELElementTypes.EXPRESSION_LIST);
    }

    private void parseInlineListOrMap(PsiBuilder builder) {
        PsiBuilder.Marker marker = builder.mark();
        builder.advanceLexer(); // consume '{'

        if (builder.getTokenType() == SpELTokenTypes.RBRACE) {
            builder.advanceLexer();
            marker.done(SpELElementTypes.INLINE_LIST);
            return;
        }

        parseExpression(builder);

        if (builder.getTokenType() == SpELTokenTypes.COLON) {
            builder.advanceLexer();
            parseExpression(builder);
            while (builder.getTokenType() == SpELTokenTypes.COMMA) {
                builder.advanceLexer();
                parseExpression(builder);
                expect(builder, SpELTokenTypes.COLON, "':' expected in map entry");
                parseExpression(builder);
            }
            expect(builder, SpELTokenTypes.RBRACE, "'}' expected");
            marker.done(SpELElementTypes.INLINE_MAP);
        }
        else {
            while (builder.getTokenType() == SpELTokenTypes.COMMA) {
                builder.advanceLexer();
                parseExpression(builder);
            }
            expect(builder, SpELTokenTypes.RBRACE, "'}' expected");
            marker.done(SpELElementTypes.INLINE_LIST);
        }
    }

    private boolean expect(PsiBuilder builder, IElementType expectedType, String errorMessage) {
        if (builder.getTokenType() == expectedType) {
            builder.advanceLexer();
            return true;
        }
        builder.error(errorMessage);
        return false;
    }
}
