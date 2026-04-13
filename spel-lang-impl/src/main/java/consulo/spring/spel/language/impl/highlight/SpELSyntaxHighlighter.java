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

import consulo.annotation.component.ExtensionImpl;
import consulo.codeEditor.DefaultLanguageHighlighterColors;
import consulo.colorScheme.TextAttributesKey;
import consulo.language.Language;
import consulo.language.ast.IElementType;
import consulo.language.ast.TokenType;
import consulo.language.editor.highlight.SyntaxHighlighter;
import consulo.language.editor.highlight.SyntaxHighlighterBase;
import consulo.language.editor.highlight.SyntaxHighlighterFactory;
import consulo.language.lexer.Lexer;
import consulo.project.Project;
import consulo.spring.spel.language.SpELLanguage;
import consulo.spring.spel.language.SpELTokenSets;
import consulo.spring.spel.language.impl.lexer.SpELLexer;
import consulo.spring.spel.language.SpELTokenTypes;
import consulo.virtualFileSystem.VirtualFile;
import org.jspecify.annotations.Nullable;

@ExtensionImpl
public class SpELSyntaxHighlighter extends SyntaxHighlighterFactory {
    private static final SpELHighlighterImpl HIGHLIGHTER = new SpELHighlighterImpl();

    @Override
    public Language getLanguage() {
        return SpELLanguage.INSTANCE;
    }

    @Override
    public SyntaxHighlighter getSyntaxHighlighter(@Nullable Project project, @Nullable VirtualFile virtualFile) {
        return HIGHLIGHTER;
    }

    private static class SpELHighlighterImpl extends SyntaxHighlighterBase {
        private static final TextAttributesKey KEYWORD = TextAttributesKey.createTextAttributesKey(
            "SPEL_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD
        );
        private static final TextAttributesKey NUMBER = TextAttributesKey.createTextAttributesKey(
            "SPEL_NUMBER", DefaultLanguageHighlighterColors.NUMBER
        );
        private static final TextAttributesKey STRING = TextAttributesKey.createTextAttributesKey(
            "SPEL_STRING", DefaultLanguageHighlighterColors.STRING
        );
        private static final TextAttributesKey OPERATION = TextAttributesKey.createTextAttributesKey(
            "SPEL_OPERATION", DefaultLanguageHighlighterColors.OPERATION_SIGN
        );
        private static final TextAttributesKey PARENS = TextAttributesKey.createTextAttributesKey(
            "SPEL_PARENS", DefaultLanguageHighlighterColors.PARENTHESES
        );
        private static final TextAttributesKey BRACKETS = TextAttributesKey.createTextAttributesKey(
            "SPEL_BRACKETS", DefaultLanguageHighlighterColors.BRACKETS
        );
        private static final TextAttributesKey BRACES = TextAttributesKey.createTextAttributesKey(
            "SPEL_BRACES", DefaultLanguageHighlighterColors.BRACES
        );
        private static final TextAttributesKey METADATA = TextAttributesKey.createTextAttributesKey(
            "SPEL_METADATA", DefaultLanguageHighlighterColors.METADATA
        );
        private static final TextAttributesKey BAD_CHARACTER_KEY = TextAttributesKey.createTextAttributesKey(
            "SPEL_BAD_CHARACTER", DefaultLanguageHighlighterColors.INVALID_STRING_ESCAPE
        );

        @Override
        public Lexer getHighlightingLexer() {
            return new SpELLexer();
        }

        @Override
        public TextAttributesKey[] getTokenHighlights(IElementType tokenType) {
            if (SpELTokenSets.KEYWORDS.contains(tokenType)) {
                return pack(KEYWORD);
            }
            if (SpELTokenSets.NUMERIC_LITERALS.contains(tokenType)) {
                return pack(NUMBER);
            }
            if (tokenType == SpELTokenTypes.STRING_LITERAL) {
                return pack(STRING);
            }
            if (SpELTokenSets.OPERATORS.contains(tokenType)) {
                return pack(OPERATION);
            }
            if (tokenType == SpELTokenTypes.LPAREN || tokenType == SpELTokenTypes.RPAREN) {
                return pack(PARENS);
            }
            if (tokenType == SpELTokenTypes.LBRACKET || tokenType == SpELTokenTypes.RBRACKET) {
                return pack(BRACKETS);
            }
            if (tokenType == SpELTokenTypes.LBRACE || tokenType == SpELTokenTypes.RBRACE) {
                return pack(BRACES);
            }
            if (tokenType == SpELTokenTypes.HASH || tokenType == SpELTokenTypes.AT) {
                return pack(METADATA);
            }
            if (tokenType == TokenType.BAD_CHARACTER) {
                return pack(BAD_CHARACTER_KEY);
            }
            return TextAttributesKey.EMPTY_ARRAY;
        }
    }
}
