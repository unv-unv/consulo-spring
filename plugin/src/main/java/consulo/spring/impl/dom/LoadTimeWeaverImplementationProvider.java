package consulo.spring.impl.dom;

import com.intellij.spring.impl.model.context.LoadTimeWeaverImpl;
import com.intellij.spring.impl.ide.model.xml.context.LoadTimeWeaver;
import consulo.annotation.component.ExtensionImpl;
import consulo.xml.dom.DomElementImplementationProvider;

import jakarta.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 05/02/2023
 */
@ExtensionImpl
public class LoadTimeWeaverImplementationProvider implements DomElementImplementationProvider<LoadTimeWeaver, LoadTimeWeaverImpl> {
  @Nonnull
  @Override
  public Class<LoadTimeWeaver> getInterfaceClass() {
    return LoadTimeWeaver.class;
  }

  @Nonnull
  @Override
  public Class<LoadTimeWeaverImpl> getImplementationClass() {
    return LoadTimeWeaverImpl.class;
  }
}
