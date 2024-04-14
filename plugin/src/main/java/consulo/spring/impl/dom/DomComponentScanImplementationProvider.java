package consulo.spring.impl.dom;

import com.intellij.spring.impl.model.context.DomComponentScanImpl;
import com.intellij.spring.impl.ide.model.xml.context.DomComponentScan;
import consulo.annotation.component.ExtensionImpl;
import consulo.xml.dom.DomElementImplementationProvider;

import javax.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 05/02/2023
 */
@ExtensionImpl
public class DomComponentScanImplementationProvider implements DomElementImplementationProvider<DomComponentScan, DomComponentScanImpl> {
  @Nonnull
  @Override
  public Class<DomComponentScan> getInterfaceClass() {
    return DomComponentScan.class;
  }

  @Nonnull
  @Override
  public Class<DomComponentScanImpl> getImplementationClass() {
    return DomComponentScanImpl.class;
  }
}
