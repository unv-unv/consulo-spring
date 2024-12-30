package consulo.spring.impl.dom;

import com.intellij.spring.impl.model.util.UtilPropertyPathImpl;
import com.intellij.spring.impl.ide.model.xml.util.PropertyPath;
import consulo.annotation.component.ExtensionImpl;
import consulo.xml.dom.DomElementImplementationProvider;

import jakarta.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 05/02/2023
 */
@ExtensionImpl
public class PropertyPathImplementationProvider implements DomElementImplementationProvider<PropertyPath, UtilPropertyPathImpl> {
  @Nonnull
  @Override
  public Class<PropertyPath> getInterfaceClass() {
    return PropertyPath.class;
  }

  @Nonnull
  @Override
  public Class<UtilPropertyPathImpl> getImplementationClass() {
    return UtilPropertyPathImpl.class;
  }
}
