package consulo.spring.module.extension;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.spring.facet.SpringFileSet;
import consulo.annotations.RequiredReadAction;
import consulo.module.extension.ModuleExtension;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

/**
 * @author VISTALL
 * @since 14-Jan-17
 */
public interface SpringModuleExtension extends ModuleExtension<SpringModuleExtension>, Disposable{
  @Nullable
  @RequiredReadAction
  static SpringModuleExtension getInstance(@NotNull Module module) {
    return ModuleUtilCore.getExtension(module, SpringModuleExtension.class);
  }

  @NotNull
  Set<SpringFileSet> getFileSets();
}
