package consulo.spring.impl.dom;

import com.intellij.spring.impl.model.beans.PNamespaceValueImpl;
import com.intellij.spring.impl.ide.model.xml.beans.PNamespaceValue;
import consulo.annotation.component.ExtensionImpl;
import consulo.xml.dom.DomElementImplementationProvider;

import jakarta.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 05/02/2023
 */
@ExtensionImpl
public class PNamespaceValueImplementationProvider implements DomElementImplementationProvider<PNamespaceValue, PNamespaceValueImpl> {
  @Nonnull
  @Override
  public Class<PNamespaceValue> getInterfaceClass() {
    return PNamespaceValue.class;
  }

  @Nonnull
  @Override
  public Class<PNamespaceValueImpl> getImplementationClass() {
    return PNamespaceValueImpl.class;
  }
}
