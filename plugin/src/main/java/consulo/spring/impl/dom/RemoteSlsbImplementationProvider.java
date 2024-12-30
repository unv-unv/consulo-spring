package consulo.spring.impl.dom;

import com.intellij.spring.impl.model.jee.RemoteSlsbImpl;
import com.intellij.spring.impl.ide.model.xml.jee.RemoteSlsb;
import consulo.annotation.component.ExtensionImpl;
import consulo.xml.dom.DomElementImplementationProvider;

import jakarta.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 05/02/2023
 */
@ExtensionImpl
public class RemoteSlsbImplementationProvider implements DomElementImplementationProvider<RemoteSlsb, RemoteSlsbImpl> {
  @Nonnull
  @Override
  public Class<RemoteSlsb> getInterfaceClass() {
    return RemoteSlsb.class;
  }

  @Nonnull
  @Override
  public Class<RemoteSlsbImpl> getImplementationClass() {
    return RemoteSlsbImpl.class;
  }
}
