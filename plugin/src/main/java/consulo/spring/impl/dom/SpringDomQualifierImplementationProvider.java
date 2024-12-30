package consulo.spring.impl.dom;

import com.intellij.spring.impl.model.beans.SpringDomQualifierImpl;
import com.intellij.spring.impl.ide.model.xml.beans.SpringDomQualifier;
import consulo.annotation.component.ExtensionImpl;
import consulo.xml.dom.DomElementImplementationProvider;

import jakarta.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 04/02/2023
 */
@ExtensionImpl
public class SpringDomQualifierImplementationProvider implements DomElementImplementationProvider<SpringDomQualifier, SpringDomQualifierImpl> {
  @Nonnull
  @Override
  public Class<SpringDomQualifier> getInterfaceClass() {
    return SpringDomQualifier.class;
  }

  @Nonnull
  @Override
  public Class<SpringDomQualifierImpl> getImplementationClass() {
    return SpringDomQualifierImpl.class;
  }
}
