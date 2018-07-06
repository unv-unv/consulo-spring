package consulo.spring.module.extension;

import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.spring.facet.SpringFileSet;
import consulo.annotations.RequiredReadAction;
import consulo.module.extension.ModuleExtension;

/**
 * @author VISTALL
 * @since 14-Jan-17
 */
public interface SpringModuleExtension extends ModuleExtension<SpringModuleExtension>, Disposable{
  @Nullable
  @RequiredReadAction
  static SpringModuleExtension getInstance(@Nonnull Module module) {
    return ModuleUtilCore.getExtension(module, SpringModuleExtension.class);
  }

  @Nonnull
  Set<SpringFileSet> getFileSets();
}
