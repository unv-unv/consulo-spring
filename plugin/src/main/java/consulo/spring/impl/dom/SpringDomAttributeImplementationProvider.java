package consulo.spring.impl.dom;

import com.intellij.spring.impl.model.beans.SpringDomAttributeImpl;
import com.intellij.spring.impl.ide.model.xml.beans.SpringDomAttribute;
import consulo.annotation.component.ExtensionImpl;
import consulo.xml.dom.DomElementImplementationProvider;

import jakarta.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 04/02/2023
 */
@ExtensionImpl
public class SpringDomAttributeImplementationProvider implements DomElementImplementationProvider<SpringDomAttribute,SpringDomAttributeImpl> {
  @Nonnull
  @Override
  public Class<SpringDomAttribute> getInterfaceClass() {
    return SpringDomAttribute.class;
  }

  @Nonnull
  @Override
  public Class<SpringDomAttributeImpl> getImplementationClass() {
    return SpringDomAttributeImpl.class;
  }
}
