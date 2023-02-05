package consulo.spring.impl.dom;

import com.intellij.spring.impl.model.tx.TxAnnotationDrivenImpl;
import com.intellij.spring.impl.ide.model.xml.tx.AnnotationDriven;
import consulo.annotation.component.ExtensionImpl;
import consulo.xml.dom.DomElementImplementationProvider;

import javax.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 05/02/2023
 */
@ExtensionImpl
public class AnnotationDrivenImplementationProvider implements DomElementImplementationProvider<AnnotationDriven, TxAnnotationDrivenImpl> {
  @Nonnull
  @Override
  public Class<AnnotationDriven> getInterfaceClass() {
    return AnnotationDriven.class;
  }

  @Nonnull
  @Override
  public Class<TxAnnotationDrivenImpl> getImplementationClass() {
    return TxAnnotationDrivenImpl.class;
  }
}
