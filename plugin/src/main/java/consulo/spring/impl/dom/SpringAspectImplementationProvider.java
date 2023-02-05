package consulo.spring.impl.dom;

import com.intellij.spring.impl.model.aop.SpringAspectImpl;
import com.intellij.spring.impl.ide.model.xml.aop.SpringAspect;
import consulo.annotation.component.ExtensionImpl;
import consulo.xml.dom.DomElementImplementationProvider;

import javax.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 05/02/2023
 */
@ExtensionImpl
public class SpringAspectImplementationProvider implements DomElementImplementationProvider<SpringAspect, SpringAspectImpl> {
  @Nonnull
  @Override
  public Class<SpringAspect> getInterfaceClass() {
    return SpringAspect.class;
  }

  @Nonnull
  @Override
  public Class<SpringAspectImpl> getImplementationClass() {
    return SpringAspectImpl.class;
  }
}
