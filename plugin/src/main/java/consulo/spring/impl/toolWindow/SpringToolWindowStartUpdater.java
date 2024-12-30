package consulo.spring.impl.toolWindow;

import consulo.annotation.component.ExtensionImpl;
import consulo.application.dumb.DumbAware;
import consulo.module.extension.ModuleExtensionHelper;
import consulo.project.Project;
import consulo.project.startup.PostStartupActivity;
import consulo.spring.impl.module.extension.SpringModuleExtension;
import consulo.ui.UIAccess;
import jakarta.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 2024-12-13
 */
@ExtensionImpl
public class SpringToolWindowStartUpdater implements PostStartupActivity, DumbAware {
    @Override
    public void runActivity(@Nonnull Project project, @Nonnull UIAccess uiAccess) {
        if (ModuleExtensionHelper.getInstance(project).hasModuleExtension(SpringModuleExtension.class)) {
            uiAccess.give(() -> {
                project.getInstance(SpringToolWindowPanel.class).invalidate();
            });
        }
    }
}
