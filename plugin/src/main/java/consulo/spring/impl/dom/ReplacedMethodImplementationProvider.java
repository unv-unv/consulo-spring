package consulo.spring.impl.dom;

import com.intellij.spring.impl.model.beans.ReplacedMethodImpl;
import com.intellij.spring.impl.ide.model.xml.beans.ReplacedMethod;
import consulo.annotation.component.ExtensionImpl;
import consulo.xml.dom.DomElementImplementationProvider;

import javax.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 04/02/2023
 */
@ExtensionImpl
public class ReplacedMethodImplementationProvider implements DomElementImplementationProvider<ReplacedMethod, ReplacedMethodImpl> {
  @Nonnull
  @Override
  public Class<ReplacedMethod> getInterfaceClass() {
    return ReplacedMethod.class;
  }

  @Nonnull
  @Override
  public Class<ReplacedMethodImpl> getImplementationClass() {
    return ReplacedMethodImpl.class;
  }
}
