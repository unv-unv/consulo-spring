package consulo.spring.module.extension;

import com.intellij.spring.facet.SpringConfigurationTab;
import consulo.annotations.RequiredDispatchThread;
import consulo.roots.ModuleRootLayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * @author VISTALL
 * @since 14-Jan-17
 */
public class SpringMutableModuleExtensionImpl extends SpringModuleExtensionImpl implements SpringMutableModuleExtension {
  public SpringMutableModuleExtensionImpl(@NotNull String id, @NotNull ModuleRootLayer moduleRootLayer) {
    super(id, moduleRootLayer);
  }

  @RequiredDispatchThread
  @Nullable
  @Override
  public JComponent createConfigurablePanel(@NotNull Runnable updateOnCheck) {
    SpringConfigurationTab tab = new SpringConfigurationTab(this);
    return tab.createComponent();
  }

  @Override
  public void setEnabled(boolean b) {
    myIsEnabled = b;
  }

  @Override
  public boolean isModified(@NotNull SpringModuleExtension springModuleExtension) {
    return myIsEnabled != springModuleExtension.isEnabled() || !myFileSets.equals(springModuleExtension.getFileSets());
  }
}
