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

module com.intellij.spring.spel.language.impl {
    requires consulo.language.api;
    requires consulo.language.impl;
    requires consulo.language.editor.api;
    requires consulo.java.language.api;

    requires com.intellij.spring.spel.language.api;

    exports consulo.spring.spel.language.impl;
    exports consulo.spring.spel.language.impl.lexer;
    exports consulo.spring.spel.language.impl.parser;
    exports consulo.spring.spel.language.impl.psi;
    exports consulo.spring.spel.language.impl.highlight;
}
