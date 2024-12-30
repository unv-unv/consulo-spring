package consulo.spring.impl.dom;

import com.intellij.spring.impl.model.aop.DeclareParentsImpl;
import com.intellij.spring.impl.ide.model.xml.aop.DeclareParents;
import consulo.annotation.component.ExtensionImpl;
import consulo.xml.dom.DomElementImplementationProvider;

import jakarta.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 05/02/2023
 */
@ExtensionImpl
public class DeclareParentsImplementationProvider implements DomElementImplementationProvider<DeclareParents, DeclareParentsImpl> {
  @Nonnull
  @Override
  public Class<DeclareParents> getInterfaceClass() {
    return DeclareParents.class;
  }

  @Nonnull
  @Override
  public Class<DeclareParentsImpl> getImplementationClass() {
    return DeclareParentsImpl.class;
  }
}
