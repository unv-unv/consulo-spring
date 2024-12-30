package consulo.spring.impl.dom;

import com.intellij.spring.impl.model.aop.SpringPointcutImpl;
import com.intellij.spring.impl.ide.model.xml.aop.SpringPointcut;
import consulo.annotation.component.ExtensionImpl;
import consulo.xml.dom.DomElementImplementationProvider;

import jakarta.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 05/02/2023
 */
@ExtensionImpl
public class SpringPointcutImplementationProvider implements DomElementImplementationProvider<SpringPointcut, SpringPointcutImpl> {
  @Nonnull
  @Override
  public Class<SpringPointcut> getInterfaceClass() {
    return SpringPointcut.class;
  }

  @Nonnull
  @Override
  public Class<SpringPointcutImpl> getImplementationClass() {
    return SpringPointcutImpl.class;
  }
}
