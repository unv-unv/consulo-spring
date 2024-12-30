package consulo.spring.impl.dom;

import com.intellij.spring.impl.model.util.UtilSetImpl;
import com.intellij.spring.impl.ide.model.xml.util.UtilSet;
import consulo.annotation.component.ExtensionImpl;
import consulo.xml.dom.DomElementImplementationProvider;

import jakarta.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 05/02/2023
 */
@ExtensionImpl
public class UtilSetImplementationProvider implements DomElementImplementationProvider<UtilSet, UtilSetImpl> {
  @Nonnull
  @Override
  public Class<UtilSet> getInterfaceClass() {
    return UtilSet.class;
  }

  @Nonnull
  @Override
  public Class<UtilSetImpl> getImplementationClass() {
    return UtilSetImpl.class;
  }
}
