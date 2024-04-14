package consulo.spring.impl.dom;

import com.intellij.spring.impl.DomSpringModelImpl2;
import com.intellij.spring.impl.ide.SpringModel;
import com.intellij.spring.impl.ide.facet.SpringFileSet;
import com.intellij.spring.impl.ide.model.xml.beans.Beans;
import consulo.annotation.access.RequiredReadAction;
import consulo.language.psi.PsiFile;
import consulo.language.psi.PsiManager;
import consulo.module.Module;
import consulo.virtualFileSystem.VirtualFile;
import consulo.virtualFileSystem.pointer.VirtualFilePointer;
import consulo.xml.psi.xml.XmlFile;
import consulo.xml.util.xml.DomElement;
import consulo.xml.util.xml.DomFileElement;
import consulo.xml.util.xml.DomManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author VISTALL
 * @since 15-Jan-17
 */
public class SpringDomUtil {
  @Nonnull
  @RequiredReadAction
  public static List<SpringModel> createModels(final SpringFileSet set, final Module module) {
    final PsiManager psiManager = PsiManager.getInstance(module.getProject());
    List<SpringModel> list = new ArrayList<>();
        for (VirtualFilePointer filePointer : set.getFiles()) {
      final VirtualFile file = filePointer.getFile();
      if (file == null) {
        continue;
      }
      final PsiFile psiFile = psiManager.findFile(file);
      if (psiFile instanceof XmlFile) {
        final DomFileElement<Beans> dom = getDomRoot((XmlFile) psiFile, Beans.class);
        if (dom != null) {
          list.add(new DomSpringModelImpl2(dom, Collections.singleton((XmlFile) psiFile), module, set));
          //addIncludes(files, dom);
        }
      }
    }
    return list;
  }

  @Nullable
  public static <T extends DomElement> T getDom(@Nonnull XmlFile configFile, @Nonnull Class<T> clazz) {
    final DomFileElement<T> element = getDomRoot(configFile, clazz);
    return element == null ? null : element.getRootElement();
  }

  @Nullable
  public static <T extends DomElement> DomFileElement<T> getDomRoot(@Nonnull XmlFile configFile, @Nonnull Class<T> clazz) {
    return DomManager.getDomManager(configFile.getProject()).getFileElement(configFile, clazz);
  }
}
