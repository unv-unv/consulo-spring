package consulo.spring.impl.dom;

import com.intellij.spring.impl.model.aop.AfterReturningAdviceImpl;
import com.intellij.spring.impl.ide.model.xml.aop.AfterReturningAdvice;
import consulo.annotation.component.ExtensionImpl;
import consulo.xml.dom.DomElementImplementationProvider;

import jakarta.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 05/02/2023
 */
@ExtensionImpl
public class AfterReturningAdviceImplementationProvider implements DomElementImplementationProvider<AfterReturningAdvice,
  AfterReturningAdviceImpl> {
  @Nonnull
  @Override
  public Class<AfterReturningAdvice> getInterfaceClass() {
    return AfterReturningAdvice.class;
  }

  @Nonnull
  @Override
  public Class<AfterReturningAdviceImpl> getImplementationClass() {
    return AfterReturningAdviceImpl.class;
  }
}
