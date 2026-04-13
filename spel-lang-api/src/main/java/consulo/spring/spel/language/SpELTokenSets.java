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

import consulo.language.ast.TokenSet;
import static consulo.spring.spel.language.SpELTokenTypes.*;

public final class SpELTokenSets {
    public static final TokenSet COMMENTS = TokenSet.EMPTY;

    public static final TokenSet STRING_LITERALS = TokenSet.create(SpELTokenTypes.STRING_LITERAL);

    public static final TokenSet KEYWORDS = TokenSet.create(
        SpELTokenTypes.TRUE,
        SpELTokenTypes.FALSE,
        SpELTokenTypes.NULL,
        SpELTokenTypes.NEW,
        SpELTokenTypes.INSTANCEOF,
        SpELTokenTypes.MATCHES,
        SpELTokenTypes.AND,
        SpELTokenTypes.OR,
        SpELTokenTypes.NOT,
        SpELTokenTypes.T_KEYWORD
    );

    public static final TokenSet NUMERIC_LITERALS = TokenSet.create(
        SpELTokenTypes.INTEGER_LITERAL,
        SpELTokenTypes.REAL_LITERAL
    );

    public static final TokenSet OPERATORS = TokenSet.create(
        SpELTokenTypes.PLUS,
        SpELTokenTypes.MINUS,
        SpELTokenTypes.STAR,
        SpELTokenTypes.DIV,
        SpELTokenTypes.MOD,
        SpELTokenTypes.POWER,
        SpELTokenTypes.EQ,
        SpELTokenTypes.NE,
        SpELTokenTypes.LT,
        SpELTokenTypes.GT,
        SpELTokenTypes.LE,
        SpELTokenTypes.GE,
        SpELTokenTypes.ASSIGN,
        SpELTokenTypes.QMARK,
        SpELTokenTypes.ELVIS,
        SpELTokenTypes.SAFE_NAV,
        SpELTokenTypes.BANG,
        SpELTokenTypes.AND_AND,
        SpELTokenTypes.OR_OR
    );

    private SpELTokenSets() {
    }
}
