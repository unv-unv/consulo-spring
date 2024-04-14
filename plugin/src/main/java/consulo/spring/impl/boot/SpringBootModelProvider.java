package consulo.spring.impl.boot;

import com.intellij.jam.JamService;
import com.intellij.java.language.psi.PsiClass;
import com.intellij.spring.impl.ide.SpringModelProvider;
import com.intellij.spring.impl.ide.constants.SpringAnnotationsConstants;
import com.intellij.spring.impl.ide.facet.SpringFileSet;
import consulo.annotation.access.RequiredReadAction;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.psi.scope.GlobalSearchScope;
import consulo.module.Module;
import consulo.spring.impl.boot.jam.SpringBootApplicationElement;
import consulo.spring.impl.module.extension.SpringModuleExtension;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author VISTALL
 * @since 15-Jan-17
 */
@ExtensionImpl
public class SpringBootModelProvider implements SpringModelProvider {
  @RequiredReadAction
  @Nonnull
  @Override
  public List<SpringFileSet> getFilesets(@Nonnull SpringModuleExtension extension) {
    Module module = extension.getModule();

    final JamService service = JamService.getJamService(module.getProject());
    final GlobalSearchScope scope = GlobalSearchScope.moduleScope(module);

    List<SpringBootApplicationElement> configurations =
      service.getJamClassElements(SpringBootApplicationElement.META, SpringAnnotationsConstants.SPRING_BOOT_APPLICATION, scope);

    if (configurations.isEmpty()) {
      return Collections.emptyList();
    }
    List<SpringFileSet> list = new ArrayList<>(configurations.size());
    for (SpringBootApplicationElement configuration : configurations) {
      PsiClass psiClass = configuration.getPsiClass();

      SpringBootFileSet springFileSet =
        new SpringBootFileSet(SpringFileSet.getUniqueId(extension.getFileSets()), psiClass.getQualifiedName(), extension);
      list.add(springFileSet);
    }
    return list;
  }
}
