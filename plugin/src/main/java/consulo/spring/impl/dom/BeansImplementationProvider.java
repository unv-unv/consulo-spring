package consulo.spring.impl.dom;

import com.intellij.spring.impl.ide.model.xml.beans.Beans;
import consulo.annotation.component.ExtensionImpl;
import consulo.xml.dom.DomElementImplementationProvider;

import jakarta.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 04/02/2023
 */
@ExtensionImpl
public class BeansImplementationProvider implements DomElementImplementationProvider<Beans, BeansImpl> {
  @Nonnull
  @Override
  public Class<Beans> getInterfaceClass() {
    return Beans.class;
  }

  @Nonnull
  @Override
  public Class<BeansImpl> getImplementationClass() {
    return BeansImpl.class;
  }
}
