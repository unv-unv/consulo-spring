package consulo.spring.module.extension;

import consulo.roots.ModuleRootLayer;
import org.jetbrains.annotations.NotNull;

/**
 * @author VISTALL
 * @since 14-Jan-17
 */
public class SpringMutableModuleExtensionImpl extends SpringModuleExtensionImpl implements SpringMutableModuleExtension {
  public SpringMutableModuleExtensionImpl(@NotNull String id, @NotNull ModuleRootLayer moduleRootLayer) {
    super(id, moduleRootLayer);
  }

  @Override
  public void setEnabled(boolean b) {
    myIsEnabled = b;
  }

  @Override
  public boolean isModified(@NotNull SpringModuleExtension springModuleExtension) {
    return myIsEnabled != springModuleExtension.isEnabled();
  }
}
