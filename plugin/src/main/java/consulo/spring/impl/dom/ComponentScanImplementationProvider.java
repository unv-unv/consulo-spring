package consulo.spring.impl.dom;

import com.intellij.spring.impl.model.context.ComponentScanImpl;
import com.intellij.spring.impl.ide.model.xml.context.ComponentScan;
import consulo.annotation.component.ExtensionImpl;
import consulo.xml.dom.DomElementImplementationProvider;

import javax.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 05/02/2023
 */
@ExtensionImpl
public class ComponentScanImplementationProvider implements DomElementImplementationProvider<ComponentScan, ComponentScanImpl> {
  @Nonnull
  @Override
  public Class<ComponentScan> getInterfaceClass() {
    return ComponentScan.class;
  }

  @Nonnull
  @Override
  public Class<ComponentScanImpl> getImplementationClass() {
    return ComponentScanImpl.class;
  }
}
