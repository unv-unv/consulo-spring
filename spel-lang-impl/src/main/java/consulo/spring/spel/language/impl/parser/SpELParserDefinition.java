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

import consulo.annotation.component.ExtensionImpl;
import consulo.language.Language;
import consulo.language.ast.ASTNode;
import consulo.language.ast.IFileElementType;
import consulo.language.ast.TokenSet;
import consulo.language.file.FileViewProvider;
import consulo.language.lexer.Lexer;
import consulo.language.parser.ParserDefinition;
import consulo.language.parser.PsiParser;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.language.version.LanguageVersion;
import consulo.spring.spel.language.SpELFileElementType;
import consulo.spring.spel.language.SpELLanguage;
import consulo.spring.spel.language.SpELTokenSets;
import consulo.spring.spel.language.impl.lexer.SpELLexer;
import consulo.spring.spel.language.impl.psi.SpELFileImpl;
import consulo.spring.spel.language.impl.psi.SpELElementFactory;
import org.jspecify.annotations.Nullable;

@ExtensionImpl
public class SpELParserDefinition implements ParserDefinition {
    @Override
    public Language getLanguage() {
        return SpELLanguage.INSTANCE;
    }

    @Override
    public Lexer createLexer(LanguageVersion languageVersion) {
        return new SpELLexer();
    }

    @Override
    public PsiParser createParser(LanguageVersion languageVersion) {
        return new SpELParser();
    }

    @Override
    public IFileElementType getFileNodeType() {
        return SpELFileElementType.FILE;
    }

    @Override
    public TokenSet getWhitespaceTokens(LanguageVersion languageVersion) {
        return TokenSet.WHITE_SPACE;
    }

    @Override
    public TokenSet getCommentTokens(LanguageVersion languageVersion) {
        return SpELTokenSets.COMMENTS;
    }

    @Override
    public TokenSet getStringLiteralElements(LanguageVersion languageVersion) {
        return SpELTokenSets.STRING_LITERALS;
    }

    @Override
    public PsiElement createElement(ASTNode node) {
        return SpELElementFactory.createElement(node);
    }

    @Override
    public PsiFile createFile(FileViewProvider viewProvider) {
        return new SpELFileImpl(viewProvider);
    }

    @Override
    public SpaceRequirements spaceExistenceTypeBetweenTokens(@Nullable ASTNode left, @Nullable ASTNode right) {
        return SpaceRequirements.MAY;
    }
}
