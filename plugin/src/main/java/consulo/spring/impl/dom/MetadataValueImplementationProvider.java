package consulo.spring.impl.dom;

import com.intellij.spring.impl.model.beans.MetadataValueImpl;
import com.intellij.spring.impl.ide.model.xml.beans.MetadataValue;
import consulo.annotation.component.ExtensionImpl;
import consulo.xml.dom.DomElementImplementationProvider;

import jakarta.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 05/02/2023
 */
@ExtensionImpl
public class MetadataValueImplementationProvider implements DomElementImplementationProvider<MetadataValue, MetadataValueImpl> {
  @Nonnull
  @Override
  public Class<MetadataValue> getInterfaceClass() {
    return MetadataValue.class;
  }

  @Nonnull
  @Override
  public Class<MetadataValueImpl> getImplementationClass() {
    return MetadataValueImpl.class;
  }
}
