package consulo.spring.dom;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.pointers.VirtualFilePointer;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.xml.XmlFile;
import com.intellij.spring.SpringModel;
import com.intellij.spring.facet.SpringFileSet;
import com.intellij.spring.impl.DomSpringModelImpl;
import com.intellij.spring.model.xml.beans.Beans;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.DomFileElement;
import com.intellij.util.xml.DomManager;
import consulo.annotations.RequiredReadAction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author VISTALL
 * @since 15-Jan-17
 */
public class SpringDomUtil {
  @NotNull
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
          list.add(new DomSpringModelImpl(dom, Collections.singleton((XmlFile) psiFile), module, set));
          //addIncludes(files, dom);
        }
      }
    }
    return list;
  }

  @Nullable
  public static <T extends DomElement> T getDom(@NotNull XmlFile configFile, @NotNull Class<T> clazz) {
    final DomFileElement<T> element = getDomRoot(configFile, clazz);
    return element == null ? null : element.getRootElement();
  }

  @Nullable
  public static <T extends DomElement> DomFileElement<T> getDomRoot(@NotNull XmlFile configFile, @NotNull Class<T> clazz) {
    return DomManager.getDomManager(configFile.getProject()).getFileElement(configFile, clazz);
  }
}
