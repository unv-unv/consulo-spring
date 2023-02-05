package consulo.spring.impl.dom;

import com.intellij.spring.impl.model.context.FilterImpl;
import com.intellij.spring.impl.ide.model.xml.context.Filter;
import consulo.annotation.component.ExtensionImpl;
import consulo.xml.dom.DomElementImplementationProvider;

import javax.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 05/02/2023
 */
@ExtensionImpl
public class FilterImplementationProvider implements DomElementImplementationProvider<Filter, FilterImpl> {
  @Nonnull
  @Override
  public Class<Filter> getInterfaceClass() {
    return Filter.class;
  }

  @Nonnull
  @Override
  public Class<FilterImpl> getImplementationClass() {
    return FilterImpl.class;
  }
}
