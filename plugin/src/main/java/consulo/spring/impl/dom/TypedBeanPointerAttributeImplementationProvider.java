package consulo.spring.impl.dom;

import com.intellij.spring.impl.model.beans.TypedBeanPointerAttributeImpl;
import com.intellij.spring.impl.ide.model.xml.beans.TypedBeanPointerAttribute;
import consulo.annotation.component.ExtensionImpl;
import consulo.xml.dom.DomElementImplementationProvider;

import jakarta.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 05/02/2023
 */
@ExtensionImpl
public class TypedBeanPointerAttributeImplementationProvider implements DomElementImplementationProvider<TypedBeanPointerAttribute,
  TypedBeanPointerAttributeImpl> {
  @Nonnull
  @Override
  public Class<TypedBeanPointerAttribute> getInterfaceClass() {
    return TypedBeanPointerAttribute.class;
  }

  @Nonnull
  @Override
  public Class<TypedBeanPointerAttributeImpl> getImplementationClass() {
    return TypedBeanPointerAttributeImpl.class;
  }
}
