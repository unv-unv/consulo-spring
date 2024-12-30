package com.intellij.spring.impl.ide.facet;

import com.intellij.spring.impl.ide.model.xml.beans.Beans;
import consulo.language.psi.PsiFile;
import consulo.language.psi.scope.GlobalSearchScope;
import consulo.language.util.ModuleUtilCore;
import consulo.module.Module;
import consulo.util.collection.MultiMap;
import consulo.virtualFileSystem.VirtualFile;
import consulo.virtualFileSystem.archive.ArchiveVfsUtil;
import consulo.xml.psi.xml.XmlFile;
import consulo.xml.util.xml.DomFileElement;
import consulo.xml.util.xml.DomService;

import jakarta.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Dmitry Avdeev
 */
public class SpringConfigsSearcher {

  private final MultiMap<Module, PsiFile> myFiles = new MultiMap<consulo.module.Module, PsiFile>();
  private final MultiMap<VirtualFile, PsiFile> myJars = new MultiMap<VirtualFile, PsiFile>();
  private final List<VirtualFile> myVirtualFiles = new ArrayList<VirtualFile>();
  private final @Nonnull
  Module myModule;

  public SpringConfigsSearcher(@Nonnull Module module) {
    myModule = module;
  }

  public void search() {
    myFiles.clear();
    myJars.clear();

    List<DomFileElement<Beans>> elements = DomService.getInstance()
      .getFileElements(Beans.class, myModule.getProject(), GlobalSearchScope.moduleWithDependenciesAndLibrariesScope(myModule));
    for (DomFileElement<Beans> element : elements) {
      XmlFile file = element.getFile();
      VirtualFile jar = ArchiveVfsUtil.getVirtualFileForJar(file.getVirtualFile());
      if (jar != null) {
        myJars.putValue(jar, file);
      } else {
        consulo.module.Module module = ModuleUtilCore.findModuleForPsiElement(file);
        if (module != null) {
          myFiles.putValue(module, file);
        }
      }
    }
  }

  public MultiMap<consulo.module.Module, PsiFile> getFilesByModules() {
    return myFiles;
  }

  public MultiMap<VirtualFile, PsiFile> getJars() {
    return myJars;
  }

  public List<VirtualFile> getVirtualFiles() {
    return myVirtualFiles;
  }
}
