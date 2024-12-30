package consulo.spring.impl.dom;

import com.intellij.spring.impl.model.util.UtilListImpl;
import com.intellij.spring.impl.ide.model.xml.util.UtilList;
import consulo.annotation.component.ExtensionImpl;
import consulo.xml.dom.DomElementImplementationProvider;

import jakarta.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 04/02/2023
 */
@ExtensionImpl
public class UtilListImplementationProvider implements DomElementImplementationProvider<UtilList, UtilListImpl> {
  @Nonnull
  @Override
  public Class<UtilList> getInterfaceClass() {
    return UtilList.class;
  }

  @Nonnull
  @Override
  public Class<UtilListImpl> getImplementationClass() {
    return UtilListImpl.class;
  }
}
