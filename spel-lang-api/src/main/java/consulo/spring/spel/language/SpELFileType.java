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

import consulo.language.file.LanguageFileType;
import consulo.localize.LocalizeValue;
import consulo.ui.image.Image;
import consulo.ui.image.ImageEffects;

public class SpELFileType extends LanguageFileType {
    public static final SpELFileType INSTANCE = new SpELFileType();

    private SpELFileType() {
        super(SpELLanguage.INSTANCE);
    }

    @Override
    public String getId() {
        return "SpEL";
    }

    @Override
    public LocalizeValue getDescription() {
        return LocalizeValue.localizeTODO("Spring Expression Language");
    }

    @Override
    public String getDefaultExtension() {
        return "spel";
    }

    @Override
    public Image getIcon() {
        return ImageEffects.empty(16);
    }
}
