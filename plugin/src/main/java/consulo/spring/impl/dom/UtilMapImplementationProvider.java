package consulo.spring.impl.dom;

import com.intellij.spring.impl.model.util.UtilMapImpl;
import com.intellij.spring.impl.ide.model.xml.util.UtilMap;
import consulo.annotation.component.ExtensionImpl;
import consulo.xml.dom.DomElementImplementationProvider;

import javax.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 05/02/2023
 */
@ExtensionImpl
public class UtilMapImplementationProvider implements DomElementImplementationProvider<UtilMap, UtilMapImpl> {
  @Nonnull
  @Override
  public Class<UtilMap> getInterfaceClass() {
    return UtilMap.class;
  }

  @Nonnull
  @Override
  public Class<UtilMapImpl> getImplementationClass() {
    return UtilMapImpl.class;
  }
}
