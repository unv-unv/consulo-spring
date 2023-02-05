package consulo.spring.impl.dom;

import com.intellij.spring.impl.model.util.UtilConstantImpl;
import com.intellij.spring.impl.ide.model.xml.util.SpringConstant;
import consulo.annotation.component.ExtensionImpl;
import consulo.xml.dom.DomElementImplementationProvider;

import javax.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 05/02/2023
 */
@ExtensionImpl
public class SpringConstantImplementationProvider implements DomElementImplementationProvider<SpringConstant, UtilConstantImpl> {
  @Nonnull
  @Override
  public Class<SpringConstant> getInterfaceClass() {
    return SpringConstant.class;
  }

  @Nonnull
  @Override
  public Class<UtilConstantImpl> getImplementationClass() {
    return UtilConstantImpl.class;
  }
}
