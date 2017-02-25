package consulo.spring.boot;

import com.intellij.jam.JamService;
import com.intellij.jam.reflect.JamClassMeta;
import com.intellij.openapi.module.Module;
import com.intellij.psi.PsiClass;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.spring.SpringModelProvider;
import com.intellij.spring.constants.SpringAnnotationsConstants;
import com.intellij.spring.facet.SpringFileSet;
import consulo.annotations.RequiredReadAction;
import consulo.spring.boot.jam.SpringBootApplication;
import consulo.spring.module.extension.SpringModuleExtension;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author VISTALL
 * @since 15-Jan-17
 */
public class SpringBootModelProvider implements SpringModelProvider {
  public static final JamClassMeta<SpringBootApplication> META = new JamClassMeta<>(SpringBootApplication.class);

  @RequiredReadAction
  @NotNull
  @Override
  public List<SpringFileSet> getFilesets(@NotNull SpringModuleExtension extension) {
    Module module = extension.getModule();

    final JamService service = JamService.getJamService(module.getProject());
    final GlobalSearchScope scope = GlobalSearchScope.moduleScope(module);

    List<SpringBootApplication> configurations = service.getJamClassElements(META, SpringAnnotationsConstants.SPRING_BOOT_APPLICATION, scope);

    if (configurations.isEmpty()) {
      return Collections.emptyList();
    }
    List<SpringFileSet> list = new ArrayList<>(configurations.size());
    for (SpringBootApplication configuration : configurations) {
      PsiClass psiClass = configuration.getPsiClass();

      SpringBootFileSet springFileSet = new SpringBootFileSet(SpringFileSet.getUniqueId(extension.getFileSets()), psiClass.getQualifiedName(), extension);
      list.add(springFileSet);
    }
    return list;
  }
}
