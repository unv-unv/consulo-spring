package consulo.spring.module.extension;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.JComponent;

import com.intellij.spring.facet.SpringConfigurationTab;
import consulo.annotations.RequiredDispatchThread;
import consulo.roots.ModuleRootLayer;

/**
 * @author VISTALL
 * @since 14-Jan-17
 */
public class SpringMutableModuleExtensionImpl extends SpringModuleExtensionImpl implements SpringMutableModuleExtension {
  public SpringMutableModuleExtensionImpl(@Nonnull String id, @Nonnull ModuleRootLayer moduleRootLayer) {
    super(id, moduleRootLayer);
  }

  @RequiredDispatchThread
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
