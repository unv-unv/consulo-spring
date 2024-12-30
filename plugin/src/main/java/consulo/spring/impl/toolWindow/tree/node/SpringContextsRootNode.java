package consulo.spring.impl.toolWindow.tree.node;

import com.intellij.spring.impl.ide.SpringManager;
import com.intellij.spring.impl.ide.SpringModel;
import consulo.annotation.access.RequiredReadAction;
import consulo.module.Module;
import consulo.module.ModuleManager;
import consulo.project.Project;
import consulo.project.ui.view.tree.AbstractTreeNode;
import consulo.ui.ex.tree.PresentationData;
import jakarta.annotation.Nonnull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author VISTALL
 * @since 2024-12-13
 */
public class SpringContextsRootNode extends AbstractTreeNode<Object> {
    public SpringContextsRootNode(Project project) {
        super(project, "root");
    }

    @RequiredReadAction
    @Nonnull
    @Override
    public Collection<? extends AbstractTreeNode> getChildren() {
        ModuleManager moduleManager = ModuleManager.getInstance(myProject);

        List<SpringContextModuleNode> moduleNodes = new ArrayList<>();

        for (Module module : moduleManager.getModules()) {
            SpringModel model = SpringManager.getInstance(myProject).getModel(module);
            if (model == null) {
                continue;
            }

            moduleNodes.add(new SpringContextModuleNode(module));
        }
        return moduleNodes;
    }

    @Override
    protected void update(PresentationData presentationData) {

    }
}
