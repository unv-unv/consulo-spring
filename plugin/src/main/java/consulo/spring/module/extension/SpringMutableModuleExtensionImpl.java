package consulo.spring.module.extension;

import com.intellij.spring.facet.SpringConfigurationTab;
import consulo.roots.ModuleRootLayer;
import consulo.ui.annotation.RequiredUIAccess;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.*;

/**
 * @author VISTALL
 * @since 14-Jan-17
 */
public class SpringMutableModuleExtensionImpl extends SpringModuleExtensionImpl implements SpringMutableModuleExtension {
  public SpringMutableModuleExtensionImpl(@Nonnull String id, @Nonnull ModuleRootLayer moduleRootLayer) {
    super(id, moduleRootLayer);
  }

  @RequiredUIAccess
  @Nullable
  @Override
  public JComponent createConfigurablePanel(@Nonnull Runnable updateOnCheck) {
    SpringConfigurationTab tab = new SpringConfigurationTab(this);
    return tab.createComponent();
  }

  @Override
  public void setEnabled(boolean b) {
    myIsEnabled = b;
  }

  @Override
  public boolean isModified(@Nonnull SpringModuleExtension springModuleExtension) {
    return myIsEnabled != springModuleExtension.isEnabled() || !myFileSets.equals(springModuleExtension.getFileSets());
  }
}
