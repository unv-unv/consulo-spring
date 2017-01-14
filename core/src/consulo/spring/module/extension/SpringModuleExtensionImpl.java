package consulo.spring.module.extension;

import com.intellij.spring.facet.SpringFileSet;
import consulo.module.extension.impl.ModuleExtensionImpl;
import consulo.roots.ModuleRootLayer;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author VISTALL
 * @since 14-Jan-17
 */
public class SpringModuleExtensionImpl extends ModuleExtensionImpl<SpringModuleExtension> implements SpringModuleExtension {
  public SpringModuleExtensionImpl(@NotNull String id, @NotNull ModuleRootLayer moduleRootLayer) {
    super(id, moduleRootLayer);
  }

  @NotNull
  @Override
  public Set<SpringFileSet> getFileSets() {
    return new LinkedHashSet<>();
  }

  @Override
  public void dispose() {

  }
}
