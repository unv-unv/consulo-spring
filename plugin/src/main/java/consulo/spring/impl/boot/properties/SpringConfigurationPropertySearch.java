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

package consulo.spring.impl.boot.properties;

import com.intellij.lang.properties.IProperty;
import com.intellij.lang.properties.psi.PropertiesFile;
import consulo.annotation.component.ComponentScope;
import consulo.annotation.component.ServiceAPI;
import consulo.annotation.component.ServiceImpl;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.language.psi.PsiManager;
import consulo.module.Module;
import consulo.module.content.ModuleRootManager;
import consulo.project.Project;
import consulo.virtualFileSystem.VirtualFile;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Project-scoped service for resolving Spring Boot configuration properties.
 * Searches application.properties / application.yml in resource roots,
 * and spring-configuration-metadata.json in library JARs.
 */
@ServiceAPI(ComponentScope.PROJECT)
@ServiceImpl
@Singleton
public class SpringConfigurationPropertySearch {
    private static final String[] CONFIG_FILENAMES = {
        "application.properties",
        "application.yml",
        "application.yaml"
    };

    private static final String[] CONFIG_DIRS = {"", "config/"};

    private final Project myProject;

    @Inject
    public SpringConfigurationPropertySearch(Project project) {
        myProject = project;
    }

    public static SpringConfigurationPropertySearch getInstance(Project project) {
        return project.getInstance(SpringConfigurationPropertySearch.class);
    }

    /**
     * Resolve a property key to its definition(s) in config files.
     * Returns IProperty elements from .properties files and YAMLKeyValue from .yml files.
     */
    public List<PsiElement> resolvePropertyKey(String key, @Nullable Module module) {
        if (module == null) {
            return List.of();
        }

        List<PsiElement> results = new ArrayList<>();
        PsiManager psiManager = PsiManager.getInstance(myProject);

        for (VirtualFile configFile : findConfigFiles(module)) {
            PsiFile psiFile = psiManager.findFile(configFile);
            if (psiFile instanceof PropertiesFile propertiesFile) {
                List<? extends IProperty> properties = propertiesFile.findPropertiesByKey(key);
                for (IProperty property : properties) {
                    results.add(property.getPsiElement());
                }
            }
            else {
                // try YAML
                resolveYamlKey(psiFile, key, results);
            }
        }

        return results;
    }

    /**
     * Get all property keys from config files + metadata for completion.
     */
    public List<String> getAllPropertyKeys(@Nullable Module module) {
        if (module == null) {
            return List.of();
        }

        List<String> keys = new ArrayList<>();
        PsiManager psiManager = PsiManager.getInstance(myProject);

        for (VirtualFile configFile : findConfigFiles(module)) {
            PsiFile psiFile = psiManager.findFile(configFile);
            if (psiFile instanceof PropertiesFile propertiesFile) {
                for (IProperty property : propertiesFile.getProperties()) {
                    String propKey = property.getKey();
                    if (propKey != null && !propKey.isEmpty()) {
                        keys.add(propKey);
                    }
                }
            }
            else {
                collectYamlKeys(psiFile, keys);
            }
        }

        // also add keys from spring-configuration-metadata.json in library JARs
        for (SpringMetadataPropertyLoader.MetadataProperty metaProp : SpringMetadataPropertyLoader.loadFromModule(module)) {
            keys.add(metaProp.name());
        }

        return keys;
    }

    private List<VirtualFile> findConfigFiles(Module module) {
        List<VirtualFile> files = new ArrayList<>();
        VirtualFile[] sourceRoots = ModuleRootManager.getInstance(module).getSourceRoots();

        for (VirtualFile root : sourceRoots) {
            for (String dir : CONFIG_DIRS) {
                VirtualFile configDir = dir.isEmpty() ? root : root.findChild("config");
                if (configDir == null) {
                    continue;
                }

                for (String filename : CONFIG_FILENAMES) {
                    VirtualFile file = configDir.findChild(filename);
                    if (file != null) {
                        files.add(file);
                    }
                }

                // profile-specific files
                for (VirtualFile child : configDir.getChildren()) {
                    String name = child.getName();
                    if (name.startsWith("application-") &&
                        (name.endsWith(".properties") || name.endsWith(".yml") || name.endsWith(".yaml"))) {
                        files.add(child);
                    }
                }
            }
        }

        return files;
    }

    private void resolveYamlKey(PsiFile psiFile, String key, List<PsiElement> results) {
        try {
            Class<?> yamlFileClass = Class.forName("org.jetbrains.yaml.psi.YAMLFile");
            Class<?> yamlUtilClass = Class.forName("org.jetbrains.yaml.YAMLUtil");

            if (!yamlFileClass.isInstance(psiFile)) {
                return;
            }

            // YAMLUtil.getQualifiedKeyInFile(yamlFile, key.split("\\."))
            java.lang.reflect.Method method = yamlUtilClass.getMethod("getQualifiedKeyInFile",
                yamlFileClass, String[].class);
            Object result = method.invoke(null, psiFile, key.split("\\."));
            if (result instanceof PsiElement element) {
                results.add(element);
            }
        }
        catch (Exception ignored) {
            // YAML plugin not available
        }
    }

    private void collectYamlKeys(PsiFile psiFile, List<String> keys) {
        try {
            Class<?> yamlFileClass = Class.forName("org.jetbrains.yaml.psi.YAMLFile");
            if (!yamlFileClass.isInstance(psiFile)) {
                return;
            }

            collectYamlKeysRecursive(psiFile, keys);
        }
        catch (Exception ignored) {
            // YAML plugin not available
        }
    }

    private void collectYamlKeysRecursive(PsiElement element, List<String> keys) {
        try {
            Class<?> yamlKeyValueClass = Class.forName("org.jetbrains.yaml.psi.YAMLKeyValue");
            Class<?> yamlUtilClass = Class.forName("org.jetbrains.yaml.YAMLUtil");
            Class<?> yamlPsiElementClass = Class.forName("org.jetbrains.yaml.psi.YAMLPsiElement");

            if (yamlKeyValueClass.isInstance(element)) {
                // check if this is a leaf (scalar value, not a mapping)
                java.lang.reflect.Method getValueMethod = yamlKeyValueClass.getMethod("getValue");
                Object value = getValueMethod.invoke(element);
                Class<?> yamlScalarClass = Class.forName("org.jetbrains.yaml.psi.YAMLScalar");
                if (value != null && yamlScalarClass.isInstance(value)) {
                    java.lang.reflect.Method getConfigFullName = yamlUtilClass.getMethod("getConfigFullName", yamlPsiElementClass);
                    Object fullName = getConfigFullName.invoke(null, element);
                    if (fullName instanceof String key && !key.isEmpty()) {
                        keys.add(key);
                    }
                }
            }

            for (PsiElement child = element.getFirstChild(); child != null; child = child.getNextSibling()) {
                collectYamlKeysRecursive(child, keys);
            }
        }
        catch (Exception ignored) {
            // YAML plugin not available
        }
    }
}
