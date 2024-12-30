package consulo.spring.impl.dom;

import com.intellij.spring.impl.model.util.UtilPropertiesImpl;
import com.intellij.spring.impl.ide.model.xml.util.UtilProperties;
import consulo.annotation.component.ExtensionImpl;
import consulo.xml.dom.DomElementImplementationProvider;

import jakarta.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 04/02/2023
 */
@ExtensionImpl
public class UtilPropertiesImplementationProvider implements DomElementImplementationProvider<UtilProperties, UtilPropertiesImpl> {
  @Nonnull
  @Override
  public Class<UtilProperties> getInterfaceClass() {
    return UtilProperties.class;
  }

  @Nonnull
  @Override
  public Class<UtilPropertiesImpl> getImplementationClass() {
    return UtilPropertiesImpl.class;
  }
}
