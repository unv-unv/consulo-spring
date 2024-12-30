package consulo.spring.impl.toolWindow.tree.node;

import consulo.annotation.access.RequiredReadAction;
import consulo.module.Module;
import consulo.platform.base.icon.PlatformIconGroup;
import consulo.project.ui.view.tree.AbstractTreeNode;
import consulo.ui.ex.SimpleTextAttributes;
import consulo.ui.ex.tree.PresentationData;
import jakarta.annotation.Nonnull;

import java.util.Collection;
import java.util.List;

/**
 * @author VISTALL
 * @since 2024-12-13
 */
public class SpringContextModuleNode extends AbstractTreeNode<Module> {
    public SpringContextModuleNode(@Nonnull Module value) {
        super(value.getProject(), value);
    }

    @RequiredReadAction
    @Nonnull
    @Override
    public Collection<? extends AbstractTreeNode> getChildren() {
        return List.of();
    }

    @Override
    protected void update(PresentationData presentation) {
        presentation.setIcon(PlatformIconGroup.nodesModule());
        presentation.addText(getValue().getName(), SimpleTextAttributes.REGULAR_BOLD_ATTRIBUTES);
    }
}
