package consulo.spring.impl.dom;

import com.intellij.spring.impl.model.aop.BasicAdviceImpl;
import com.intellij.spring.impl.ide.model.xml.aop.BasicAdvice;
import consulo.annotation.component.ExtensionImpl;
import consulo.xml.dom.DomElementImplementationProvider;

import jakarta.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 05/02/2023
 */
@ExtensionImpl
public class BasicAdviceImplementationProvider implements DomElementImplementationProvider<BasicAdvice, BasicAdviceImpl> {
  @Nonnull
  @Override
  public Class<BasicAdvice> getInterfaceClass() {
    return BasicAdvice.class;
  }

  @Nonnull
  @Override
  public Class<BasicAdviceImpl> getImplementationClass() {
    return BasicAdviceImpl.class;
  }
}
