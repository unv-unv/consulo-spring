package consulo.spring.impl.dom;

import com.intellij.spring.impl.model.jee.LocalSlsbImpl;
import com.intellij.spring.impl.ide.model.xml.jee.LocalSlsb;
import consulo.annotation.component.ExtensionImpl;
import consulo.xml.dom.DomElementImplementationProvider;

import javax.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 04/02/2023
 */
@ExtensionImpl
public class LocalSlsbImplementationProvider implements DomElementImplementationProvider<LocalSlsb, LocalSlsbImpl> {
  @Nonnull
  @Override
  public Class<LocalSlsb> getInterfaceClass() {
    return LocalSlsb.class;
  }

  @Nonnull
  @Override
  public Class<LocalSlsbImpl> getImplementationClass() {
    return LocalSlsbImpl.class;
  }
}
