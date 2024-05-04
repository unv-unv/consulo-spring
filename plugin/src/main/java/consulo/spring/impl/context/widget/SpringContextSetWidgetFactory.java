package consulo.spring.impl.context.widget;

import consulo.annotation.component.ExtensionImpl;
import consulo.disposer.Disposer;
import consulo.fileEditor.statusBar.StatusBarEditorBasedWidgetFactory;
import consulo.module.extension.ModuleExtensionHelper;
import consulo.project.Project;
import consulo.project.ui.wm.StatusBarWidget;
import consulo.spring.impl.module.extension.SpringModuleExtension;
import jakarta.annotation.Nonnull;
import org.jetbrains.annotations.Nls;

/**
 * @author VISTALL
 * @since 2024-04-28
 */
@ExtensionImpl(id = SpringContextSetWidgetFactory.ID)
public class SpringContextSetWidgetFactory extends StatusBarEditorBasedWidgetFactory {
  public static final String ID = "springContextSetWidget";
  @Nonnull
  @Override
  public String getId() {
    return ID;
  }

  @Nls
  @Nonnull
  @Override
  public String getDisplayName() {
    return "Spring Context";
  }

  @Override
  public boolean isAvailable(@Nonnull Project project) {
    return ModuleExtensionHelper.getInstance(project).hasModuleExtension(SpringModuleExtension.class);
  }

  @Nonnull
  @Override
  public StatusBarWidget createWidget(@Nonnull Project project) {
    return new SpringContextSetWidget(project, this);
  }

  @Override
  public void disposeWidget(@Nonnull StatusBarWidget widget) {
    Disposer.dispose(widget);
  }
}
