package consulo.spring.impl.dom;

import com.intellij.spring.impl.model.tx.TxAdviceImpl;
import com.intellij.spring.impl.ide.model.xml.tx.Advice;
import consulo.annotation.component.ExtensionImpl;
import consulo.xml.dom.DomElementImplementationProvider;

import javax.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 05/02/2023
 */
@ExtensionImpl
public class AdviceImplementationProvider implements DomElementImplementationProvider<Advice, TxAdviceImpl> {
  @Nonnull
  @Override
  public Class<Advice> getInterfaceClass() {
    return Advice.class;
  }

  @Nonnull
  @Override
  public Class<TxAdviceImpl> getImplementationClass() {
    return TxAdviceImpl.class;
  }
}
