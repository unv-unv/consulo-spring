package consulo.spring.impl.dom;

import com.intellij.spring.impl.model.beans.SpringKeyImpl;
import com.intellij.spring.impl.ide.model.xml.beans.SpringKey;
import consulo.annotation.component.ExtensionImpl;
import consulo.xml.dom.DomElementImplementationProvider;

import javax.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 04/02/2023
 */
@ExtensionImpl
public class SpringKeyImplementationProvider implements DomElementImplementationProvider<SpringKey, SpringKeyImpl> {
  @Nonnull
  @Override
  public Class<SpringKey> getInterfaceClass() {
    return SpringKey.class;
  }

  @Nonnull
  @Override
  public Class<SpringKeyImpl> getImplementationClass() {
    return SpringKeyImpl.class;
  }
}
