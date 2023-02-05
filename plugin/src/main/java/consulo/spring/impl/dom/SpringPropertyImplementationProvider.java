package consulo.spring.impl.dom;

import com.intellij.spring.impl.model.beans.SpringPropertyImpl;
import com.intellij.spring.impl.ide.model.xml.beans.SpringProperty;
import consulo.annotation.component.ExtensionImpl;
import consulo.xml.dom.DomElementImplementationProvider;

import javax.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 04/02/2023
 */
@ExtensionImpl
public class SpringPropertyImplementationProvider implements DomElementImplementationProvider<SpringProperty, SpringPropertyImpl> {
  @Nonnull
  @Override
  public Class<SpringProperty> getInterfaceClass() {
    return SpringProperty.class;
  }

  @Nonnull
  @Override
  public Class<SpringPropertyImpl> getImplementationClass() {
    return SpringPropertyImpl.class;
  }
}
