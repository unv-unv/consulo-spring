package consulo.spring.module.extension;

import com.intellij.openapi.vfs.pointers.VirtualFilePointer;
import com.intellij.spring.facet.SpringFileSet;
import consulo.annotations.RequiredReadAction;
import consulo.module.extension.impl.ModuleExtensionImpl;
import consulo.roots.ModuleRootLayer;
import org.jdom.Element;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * @author VISTALL
 * @since 14-Jan-17
 */
public class SpringModuleExtensionImpl extends ModuleExtensionImpl<SpringModuleExtension> implements SpringModuleExtension {
  @NonNls
  private static final String FILESET = "fileset";
  @NonNls
  private static final String SET_ID = "id";
  @NonNls
  private static final String SET_NAME = "name";
  @NonNls
  private static final String SET_REMOVED = "removed";
  @NonNls
  private static final String FILE = "file";
  @NonNls
  private static final String DEPENDENCY = "dependency";

  protected Set<SpringFileSet> myFileSets = new LinkedHashSet<>();

  public SpringModuleExtensionImpl(@NotNull String id, @NotNull ModuleRootLayer moduleRootLayer) {
    super(id, moduleRootLayer);
  }

  @NotNull
  @Override
  public Set<SpringFileSet> getFileSets() {
    return myFileSets;
  }

  @Override
  public void dispose() {
  }

  @RequiredReadAction
  @Override
  public void commit(@NotNull SpringModuleExtension mutableModuleExtension) {
    super.commit(mutableModuleExtension);
    myFileSets = new LinkedHashSet<>(mutableModuleExtension.getFileSets());
  }

  @RequiredReadAction
  @Override
  protected void loadStateImpl(@NotNull Element element) {
    super.loadStateImpl(element);

    for (Element setElement : element.getChildren(FILESET)) {
      final String setName = setElement.getAttributeValue(SET_NAME);
      final String setId = setElement.getAttributeValue(SET_ID);
      final String removed = setElement.getAttributeValue(SET_REMOVED);
      if (setName != null && setId != null) {
        final SpringFileSet fileSet = new SpringFileSet(setId, setName, this);
        final List<Element> deps = setElement.getChildren(DEPENDENCY);
        for (Element dep : deps) {
          fileSet.addDependency(dep.getText());
        }
        final List<Element> files = setElement.getChildren(FILE);
        for (Element fileElement : files) {
          final String text = fileElement.getText();
          fileSet.addFile(text);
        }
        fileSet.setRemoved(Boolean.valueOf(removed));
        myFileSets.add(fileSet);
      }
    }
  }

  @Override
  protected void getStateImpl(@NotNull Element element) {
    super.getStateImpl(element);

    for (SpringFileSet fileSet : myFileSets) {
      final Element setElement = new Element(FILESET);
      setElement.setAttribute(SET_ID, fileSet.getId());
      setElement.setAttribute(SET_NAME, fileSet.getName());
      setElement.setAttribute(SET_REMOVED, Boolean.toString(fileSet.isRemoved()));
      element.addContent(setElement);
      for (String dep : fileSet.getDependencies()) {
        final Element depElement = new Element(DEPENDENCY);
        depElement.setText(dep);
        setElement.addContent(depElement);
      }
      for (VirtualFilePointer fileName : fileSet.getFiles()) {
        final Element fileElement = new Element(FILE);
        fileElement.setText(fileName.getUrl());
        setElement.addContent(fileElement);
      }
    }
  }
}
