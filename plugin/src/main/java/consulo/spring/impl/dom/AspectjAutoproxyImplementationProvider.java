package consulo.spring.impl.dom;

import com.intellij.spring.impl.model.aop.AspectjAutoproxyImpl;
import com.intellij.spring.impl.ide.model.xml.aop.AspectjAutoproxy;
import consulo.annotation.component.ExtensionImpl;
import consulo.xml.dom.DomElementImplementationProvider;

import javax.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 05/02/2023
 */
@ExtensionImpl
public class AspectjAutoproxyImplementationProvider implements DomElementImplementationProvider<AspectjAutoproxy, AspectjAutoproxyImpl> {
  @Nonnull
  @Override
  public Class<AspectjAutoproxy> getInterfaceClass() {
    return AspectjAutoproxy.class;
  }

  @Nonnull
  @Override
  public Class<AspectjAutoproxyImpl> getImplementationClass() {
    return AspectjAutoproxyImpl.class;
  }
}
