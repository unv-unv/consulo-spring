package consulo.spring.impl.dom;

import com.intellij.spring.impl.model.context.PropertyPlaceholderImpl;
import com.intellij.spring.impl.ide.model.xml.context.PropertyPlaceholder;
import consulo.annotation.component.ExtensionImpl;
import consulo.xml.dom.DomElementImplementationProvider;

import javax.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 05/02/2023
 */
@ExtensionImpl
public class PropertyPlaceholderImplementationProvider implements DomElementImplementationProvider<PropertyPlaceholder,
  PropertyPlaceholderImpl> {
  @Nonnull
  @Override
  public Class<PropertyPlaceholder> getInterfaceClass() {
    return PropertyPlaceholder.class;
  }

  @Nonnull
  @Override
  public Class<PropertyPlaceholderImpl> getImplementationClass() {
    return PropertyPlaceholderImpl.class;
  }
}
