/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */

package com.intellij.spring.impl.ide.schemas;

import com.intellij.java.language.psi.JavaPsiFacade;
import com.intellij.java.language.psi.PsiJavaPackage;
import com.intellij.spring.impl.ide.SpringManager;
import com.intellij.spring.impl.ide.constants.SpringConstants;
import com.intellij.xml.DefaultXmlExtension;
import com.intellij.xml.XmlSchemaProvider;
import com.intellij.xml.impl.schema.XmlNSDescriptorImpl;
import consulo.annotation.component.ExtensionImpl;
import consulo.application.dumb.DumbAware;
import consulo.application.util.CachedValue;
import consulo.application.util.CachedValueProvider;
import consulo.application.util.CachedValuesManager;
import consulo.language.psi.*;
import consulo.language.psi.meta.PsiMetaData;
import consulo.language.psi.scope.GlobalSearchScope;
import consulo.language.util.ModuleUtilCore;
import consulo.logging.Logger;
import consulo.module.Module;
import consulo.module.content.ProjectRootManager;
import consulo.project.Project;
import consulo.util.dataholder.Key;
import consulo.virtualFileSystem.VirtualFile;
import consulo.virtualFileSystem.VirtualFileManager;
import consulo.xml.psi.xml.XmlDocument;
import consulo.xml.psi.xml.XmlFile;
import org.jetbrains.annotations.NonNls;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * @author Dmitry Avdeev
 */
@ExtensionImpl
public class SpringSchemaProvider extends XmlSchemaProvider implements DumbAware {
  private static final Logger LOG = Logger.getInstance(SpringSchemaProvider.class);

  private static final Map<String, String> FALLBACK_SCHEMALOCATIONS = new HashMap<String, String>(2);
  private static final Key<CachedValue<Map<String, VirtualFile>>> SCHEMAS_BUNDLE_KEY = Key.create("spring schemas");
  private static final CachedValueProvider.Result<Map<String, VirtualFile>> EMPTY_MAP_RESULT =
    new CachedValueProvider.Result<Map<String, VirtualFile>>(
      Collections.<String, VirtualFile>emptyMap(), PsiModificationTracker.MODIFICATION_COUNT);

  static {
    FALLBACK_SCHEMALOCATIONS.put(SpringConstants.BEANS_XSD, SpringConstants.BEANS_SCHEMALOCATION_FALLBACK);
    FALLBACK_SCHEMALOCATIONS.put(SpringConstants.TOOL_NAMESPACE, SpringConstants.TOOL_SCHEMALOCATION_FALLBACK);
  }

  @Nullable
  public XmlFile getSchema(@Nonnull @NonNls final String url, @Nullable consulo.module.Module module, @Nonnull final PsiFile baseFile) {
    final String schemaLocation = FALLBACK_SCHEMALOCATIONS.get(url);
    if (schemaLocation != null) {
      return getSchema(schemaLocation, module, baseFile);
    }
    if (module == null) {
      final PsiDirectory directory = baseFile.getParent();
      if (directory != null) {
        module = ModuleUtilCore.findModuleForPsiElement(directory);
      }
    }
    if (module == null) {
      return null;
    }
    final Map<String, VirtualFile> schemas = getSchemas(module);
    final Project project = module.getProject();
    final VirtualFile file = schemas.get(url);
    if (file == null) return null;
    final PsiFile psiFile = PsiManager.getInstance(project).findFile(file);
    if (!(psiFile instanceof XmlFile)) return null;
    return (XmlFile)psiFile;
  }

  public boolean isAvailable(final @Nonnull XmlFile file) {
    final boolean isSpring = SpringManager.getInstance(file.getProject()).isSpringBeans(file);
    if (isSpring) {
      return true;
    }
    final VirtualFile virtualFile = file.getVirtualFile();
    if (virtualFile == null) {
      return false;
    }
    final String extension = virtualFile.getExtension();
    return extension != null && extension.equals("xsd");
  }

  @Nonnull
  public Set<String> getAvailableNamespaces(@Nonnull final XmlFile file, final String tagName) {
    final consulo.module.Module module = ModuleUtilCore.findModuleForPsiElement(file);
    if (module == null) {
      return Collections.emptySet();
    }
    final Map<String, VirtualFile> map = getSchemas(module);
    final HashSet<String> strings = new HashSet<String>(map.size());
    for (VirtualFile virtualFile : map.values()) {
      final String namespace = getNamespace(virtualFile, file.getProject());
      if (namespace != null) {
        strings.add(namespace);
      }
    }
    return DefaultXmlExtension.filterNamespaces(strings, tagName, file);
  }

  @Nullable
  private static String getNamespace(final VirtualFile virtualFile, final Project project) {
    final PsiFile psiFile = PsiManager.getInstance(project).findFile(virtualFile);
    if (psiFile instanceof XmlFile) {
      final XmlDocument document = ((XmlFile)psiFile).getDocument();
      if (document != null) {
        final PsiMetaData metaData = document.getMetaData();
        if (metaData instanceof XmlNSDescriptorImpl) {
          return ((XmlNSDescriptorImpl)metaData).getDefaultNamespace();
        }
      }
    }
    return null;
  }

  public String getDefaultPrefix(@Nonnull @NonNls final String namespace, @Nonnull final XmlFile context) {
    if (!SpringManager.getInstance(context.getProject()).isSpringBeans(context))
      return null;
    final String[] strings = namespace.split("/");
    return strings[strings.length - 1];
  }

  public Set<String> getLocations(@Nonnull @NonNls final String namespace, @Nonnull final XmlFile context) {
    final consulo.module.Module module = ModuleUtilCore.findModuleForPsiElement(context);
    if (module == null) {
      return null;
    }
    final Map<String, VirtualFile> schemas = getSchemas(module);
    for (Map.Entry<String, VirtualFile> entry : schemas.entrySet()) {
      final String s = getNamespace(entry.getValue(), context.getProject());
      if (s != null && s.equals(namespace)) {
        return Collections.singleton(entry.getKey());
      }
    }
    return null;
  }

  @Nonnull
  public static Map<String, VirtualFile> getSchemas(@Nonnull final Module module) {
    final Project project = module.getProject();
    final CachedValuesManager manager = CachedValuesManager.getManager(project);
    final Map<String, VirtualFile> bundle =
      manager.getCachedValue(module, SCHEMAS_BUNDLE_KEY, new CachedValueProvider<Map<String, VirtualFile>>() {
        public Result<Map<String, VirtualFile>> compute() {
          return computeSchemas(module);
        }
      }, false);
    return bundle == null ? Collections.<String, VirtualFile>emptyMap() : bundle;
  }

  @Nonnull
  public static Map<String, String> getHandlers(@Nonnull final consulo.module.Module module) {
    return computeHandlers(module);
  }

  @Nonnull
  private static CachedValueProvider.Result<Map<String, VirtualFile>> computeSchemas(@Nonnull final consulo.module.Module module) {
    final PsiJavaPackage psiPackage =
      JavaPsiFacade.getInstance(module.getProject()).findPackage("META-INF");
    if (psiPackage != null) {
      final PsiDirectory[] directories =
        psiPackage.getDirectories(GlobalSearchScope.moduleWithDependenciesAndLibrariesScope(module, false));
      Map<String, VirtualFile> map = new HashMap<String, VirtualFile>();
      ArrayList<Object> dependencies = new ArrayList<Object>();
      dependencies.add(ProjectRootManager.getInstance(module.getProject()));
      for (PsiDirectory directory : directories) {
        final PsiFile psiFile = directory.findFile("spring.schemas");
        if (psiFile != null) {
          final VirtualFile schemasFile = psiFile.getVirtualFile();
          assert schemasFile != null;
          dependencies.add(psiFile);
          final PsiDirectory parent = directory.getParent();
          assert parent != null;
          String root = parent.getVirtualFile().getUrl();
          if (!root.endsWith("/")) {
            root += "/";
          }
          InputStream inputStream = null;
          try {
            inputStream = schemasFile.getInputStream();
            final PropertyResourceBundle bundle = new PropertyResourceBundle(inputStream);
            final Enumeration<String> keys = bundle.getKeys();
            while (keys.hasMoreElements()) {
              final String key = keys.nextElement();
              final String location = (String)bundle.handleGetObject(key);
              final String schemaUrl = root + location;
              final VirtualFile file = VirtualFileManager.getInstance().findFileByUrl(schemaUrl);
              if (file != null) {
                map.put(key, file);
              }
            }
          }
          catch (IOException e) {
            LOG.error(e);
            return EMPTY_MAP_RESULT;
          }
          finally {
            if (inputStream != null) {
              try {
                inputStream.close();
              }
              catch (IOException e) {
                LOG.error(e);
              }
            }
          }
        }
      }
      return new CachedValueProvider.Result<Map<String, VirtualFile>>(map, dependencies.toArray());
    }
    return EMPTY_MAP_RESULT;
  }

  @Nonnull
  private static Map<String, String> computeHandlers(@Nonnull final consulo.module.Module module) {
    final Project project = module.getProject();
    final PsiManager psiManager = PsiManager.getInstance(project);
    final PsiPackage psiPackage = JavaPsiFacade.getInstance(psiManager.getProject()).findPackage("META-INF");
    if (psiPackage != null) {
      final GlobalSearchScope scope = GlobalSearchScope.moduleWithDependenciesAndLibrariesScope(module, false);
      final PsiDirectory[] directories = psiPackage.getDirectories(scope);
      Map<String, String> map = new HashMap<String, String>();
      for (PsiDirectory directory : directories) {
        final PsiFile psiFile = directory.findFile("spring.handlers");
        if (psiFile != null) {
          final VirtualFile handlersFile = psiFile.getVirtualFile();
          assert handlersFile != null;
          final PsiDirectory parent = directory.getParent();
          assert parent != null;
          String root = parent.getVirtualFile().getUrl();
          if (!root.endsWith("/")) {
            root += "/";
          }
          InputStream inputStream = null;
          try {
            inputStream = handlersFile.getInputStream();
            final PropertyResourceBundle bundle = new PropertyResourceBundle(inputStream);
            final Enumeration<String> keys = bundle.getKeys();
            while (keys.hasMoreElements()) {
              final String key = keys.nextElement();
              map.put(key, (String)bundle.handleGetObject(key));
            }
          }
          catch (IOException e) {
            LOG.error(e);
            return Collections.emptyMap();
          }
          finally {
            if (inputStream != null) {
              try {
                inputStream.close();
              }
              catch (IOException e) {
                LOG.error(e);
              }
            }
          }
        }
      }
      return map;
    }
    return Collections.emptyMap();
  }
}
