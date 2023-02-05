package consulo.spring.impl.module.extension;

import consulo.annotation.component.ExtensionImpl;
import consulo.localize.LocalizeValue;
import consulo.module.content.layer.ModuleExtensionProvider;
import consulo.module.content.layer.ModuleRootLayer;
import consulo.module.extension.MutableModuleExtension;
import consulo.spring.impl.icon.SpringImplIconGroup;
import consulo.ui.image.Image;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author VISTALL
 * @since 05/02/2023
 */
@ExtensionImpl
public class SpringModuleExtensionProviderImpl implements ModuleExtensionProvider<SpringModuleExtension> {
  @Nonnull
  @Override
  public String getId() {
    return "spring";
  }

  @Nullable
  @Override
  public String getParentId() {
    return "java";
  }

  @Nonnull
  @Override
  public LocalizeValue getName() {
    return LocalizeValue.localizeTODO("Spring");
  }

  @Nonnull
  @Override
  public Image getIcon() {
    return SpringImplIconGroup.spring();
  }

  @Nonnull
  @Override
  public SpringModuleExtension createImmutableExtension(@Nonnull ModuleRootLayer moduleRootLayer) {
    return new SpringModuleExtensionImpl(getId(), moduleRootLayer);
  }

  @Nonnull
  @Override
  public MutableModuleExtension<SpringModuleExtension> createMutableExtension(@Nonnull ModuleRootLayer moduleRootLayer) {
    return new SpringMutableModuleExtensionImpl(getId(), moduleRootLayer);
  }
}
