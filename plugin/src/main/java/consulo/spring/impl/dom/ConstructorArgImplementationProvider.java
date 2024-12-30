package consulo.spring.impl.dom;

import com.intellij.spring.impl.model.beans.ConstructorArgImpl;
import com.intellij.spring.impl.ide.model.xml.beans.ConstructorArg;
import consulo.annotation.component.ExtensionImpl;
import consulo.xml.dom.DomElementImplementationProvider;

import jakarta.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 04/02/2023
 */
@ExtensionImpl
public class ConstructorArgImplementationProvider implements DomElementImplementationProvider<ConstructorArg, ConstructorArgImpl> {
  @Nonnull
  @Override
  public Class<ConstructorArg> getInterfaceClass() {
    return ConstructorArg.class;
  }

  @Nonnull
  @Override
  public Class<ConstructorArgImpl> getImplementationClass() {
    return ConstructorArgImpl.class;
  }
}
