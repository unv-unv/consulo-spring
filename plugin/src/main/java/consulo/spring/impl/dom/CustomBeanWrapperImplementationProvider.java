package consulo.spring.impl.dom;

import com.intellij.spring.impl.model.CustomBeanWrapperImpl;
import com.intellij.spring.impl.ide.model.xml.CustomBeanWrapper;
import consulo.annotation.component.ExtensionImpl;
import consulo.xml.dom.DomElementImplementationProvider;

import jakarta.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 05/02/2023
 */
@ExtensionImpl
public class CustomBeanWrapperImplementationProvider implements DomElementImplementationProvider<CustomBeanWrapper, CustomBeanWrapperImpl> {
  @Nonnull
  @Override
  public Class<CustomBeanWrapper> getInterfaceClass() {
    return CustomBeanWrapper.class;
  }

  @Nonnull
  @Override
  public Class<CustomBeanWrapperImpl> getImplementationClass() {
    return CustomBeanWrapperImpl.class;
  }
}
