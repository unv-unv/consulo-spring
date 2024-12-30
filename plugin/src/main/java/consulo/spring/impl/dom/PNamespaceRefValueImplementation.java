package consulo.spring.impl.dom;

import com.intellij.spring.impl.model.beans.PNamespaceRefValueImpl;
import com.intellij.spring.impl.ide.model.xml.beans.PNamespaceRefValue;
import consulo.annotation.component.ExtensionImpl;
import consulo.xml.dom.DomElementImplementationProvider;

import jakarta.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 05/02/2023
 */
@ExtensionImpl
public class PNamespaceRefValueImplementation implements DomElementImplementationProvider<PNamespaceRefValue, PNamespaceRefValueImpl> {
  @Nonnull
  @Override
  public Class<PNamespaceRefValue> getInterfaceClass() {
    return PNamespaceRefValue.class;
  }

  @Nonnull
  @Override
  public Class<PNamespaceRefValueImpl> getImplementationClass() {
    return PNamespaceRefValueImpl.class;
  }
}
