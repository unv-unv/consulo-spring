package consulo.spring.impl.dom;

import com.intellij.spring.impl.model.beans.SpringValueImpl;
import com.intellij.spring.impl.ide.model.xml.beans.SpringValue;
import consulo.annotation.component.ExtensionImpl;
import consulo.xml.dom.DomElementImplementationProvider;

import javax.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 04/02/2023
 */
@ExtensionImpl
public class SpringValueImplementationProvider implements DomElementImplementationProvider<SpringValue, SpringValueImpl> {
  @Nonnull
  @Override
  public Class<SpringValue> getInterfaceClass() {
    return SpringValue.class;
  }

  @Nonnull
  @Override
  public Class<SpringValueImpl> getImplementationClass() {
    return SpringValueImpl.class;
  }
}
