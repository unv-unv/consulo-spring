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

package consulo.spring.spel.language.impl;

import consulo.annotation.component.ExtensionImpl;
import consulo.spring.spel.language.SpELFileType;
import consulo.virtualFileSystem.fileType.FileTypeConsumer;
import consulo.virtualFileSystem.fileType.FileTypeFactory;

@ExtensionImpl
public class SpELFileTypeFactory extends FileTypeFactory {
    @Override
    public void createFileTypes(FileTypeConsumer consumer) {
        consumer.consume(SpELFileType.INSTANCE, SpELFileType.INSTANCE.getDefaultExtension());
    }
}
