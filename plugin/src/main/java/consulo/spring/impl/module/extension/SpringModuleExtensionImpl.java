package consulo.spring.impl.module.extension;

import com.intellij.spring.impl.ide.facet.SpringFileSet;
import com.intellij.spring.impl.ide.facet.SpringFileSetFactory;
import consulo.annotation.access.RequiredReadAction;
import consulo.module.content.layer.ModuleRootLayer;
import consulo.module.content.layer.extension.ModuleExtensionBase;
import consulo.spring.impl.boot.SpringBootFileSet;
import consulo.virtualFileSystem.pointer.VirtualFilePointer;
import org.jdom.Element;

import javax.annotation.Nonnull;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author VISTALL
 * @since 14-Jan-17
 */
public class SpringModuleExtensionImpl extends ModuleExtensionBase<SpringModuleExtension> implements SpringModuleExtension {
  private static final String FILESET = "fileset";
  private static final String SET_ID = "id";
  private static final String SET_NAME = "name";
  private static final String SET_REMOVED = "removed";
  private static final String FILE = "file";
  private static final String DEPENDENCY = "dependency";
  private static final String TYPE = "type";

  protected Set<SpringFileSet> myFileSets = new LinkedHashSet<>();

  public SpringModuleExtensionImpl(@Nonnull String id, @Nonnull ModuleRootLayer moduleRootLayer) {
    super(id, moduleRootLayer);
  }

  @Nonnull
  @Override
  public Set<SpringFileSet> getFileSets() {
    return myFileSets;
  }

  @Override
  public void dispose() {
  }

  @RequiredReadAction
  @Override
  public void commit(@Nonnull SpringModuleExtension mutableModuleExtension) {
    super.commit(mutableModuleExtension);
    myFileSets =
      mutableModuleExtension.getFileSets().stream().map(it -> it.cloneTo(this)).collect(Collectors.toCollection(LinkedHashSet::new));
  }

  @RequiredReadAction
  @Override
  protected void loadStateImpl(@Nonnull Element element) {
    super.loadStateImpl(element);

    for (Element setElement : element.getChildren(FILESET)) {
      final String setName = setElement.getAttributeValue(SET_NAME);
      final String setId = setElement.getAttributeValue(SET_ID);
      final String removed = setElement.getAttributeValue(SET_REMOVED);
      final String type = setElement.getAttributeValue(TYPE);
      if (setName != null && setId != null) {
        final SpringFileSet fileSet = SpringFileSetFactory.create(type, setId, setName, this);
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
  protected void getStateImpl(@Nonnull Element element) {
    super.getStateImpl(element);

    for (SpringFileSet fileSet : myFileSets) {
      if (fileSet instanceof SpringBootFileSet) {
        continue;
      }

      final Element setElement = new Element(FILESET);
      setElement.setAttribute(SET_ID, fileSet.getId());
      setElement.setAttribute(SET_NAME, fileSet.getName());
      setElement.setAttribute(SET_REMOVED, Boolean.toString(fileSet.isRemoved()));
      setElement.setAttribute(TYPE, fileSet.getType());
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
