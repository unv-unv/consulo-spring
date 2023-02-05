package consulo.spring.impl.dom;

import com.intellij.spring.impl.model.jee.JndiLookupImpl;
import com.intellij.spring.impl.ide.model.xml.jee.JndiLookup;
import consulo.annotation.component.ExtensionImpl;
import consulo.xml.dom.DomElementImplementationProvider;

import javax.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 05/02/2023
 */
@ExtensionImpl
public class JndiLookupImplementationProvider implements DomElementImplementationProvider<JndiLookup, JndiLookupImpl> {
  @Nonnull
  @Override
  public Class<JndiLookup> getInterfaceClass() {
    return JndiLookup.class;
  }

  @Nonnull
  @Override
  public Class<JndiLookupImpl> getImplementationClass() {
    return JndiLookupImpl.class;
  }
}
