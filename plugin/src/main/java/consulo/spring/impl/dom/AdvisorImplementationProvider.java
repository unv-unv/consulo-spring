package consulo.spring.impl.dom;

import com.intellij.spring.impl.model.aop.AdvisorImpl;
import com.intellij.spring.impl.ide.model.xml.aop.Advisor;
import consulo.annotation.component.ExtensionImpl;
import consulo.xml.dom.DomElementImplementationProvider;

import jakarta.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 05/02/2023
 */
@ExtensionImpl
public class AdvisorImplementationProvider implements DomElementImplementationProvider<Advisor, AdvisorImpl> {
  @Nonnull
  @Override
  public Class<Advisor> getInterfaceClass() {
    return Advisor.class;
  }

  @Nonnull
  @Override
  public Class<AdvisorImpl> getImplementationClass() {
    return AdvisorImpl.class;
  }
}
