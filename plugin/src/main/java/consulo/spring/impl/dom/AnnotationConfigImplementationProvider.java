package consulo.spring.impl.dom;

import com.intellij.spring.impl.model.context.AnnotationConfigImpl;
import com.intellij.spring.impl.ide.model.xml.context.AnnotationConfig;
import consulo.annotation.component.ExtensionImpl;
import consulo.xml.dom.DomElementImplementationProvider;

import jakarta.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 05/02/2023
 */
@ExtensionImpl
public class AnnotationConfigImplementationProvider implements DomElementImplementationProvider<AnnotationConfig, AnnotationConfigImpl> {
  @Nonnull
  @Override
  public Class<AnnotationConfig> getInterfaceClass() {
    return AnnotationConfig.class;
  }

  @Nonnull
  @Override
  public Class<AnnotationConfigImpl> getImplementationClass() {
    return AnnotationConfigImpl.class;
  }
}
