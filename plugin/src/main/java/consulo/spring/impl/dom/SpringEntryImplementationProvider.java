package consulo.spring.impl.dom;

import com.intellij.spring.impl.model.beans.SpringEntryImpl;
import com.intellij.spring.impl.ide.model.xml.beans.SpringEntry;
import consulo.annotation.component.ExtensionImpl;
import consulo.xml.dom.DomElementImplementationProvider;

import javax.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 04/02/2023
 */
@ExtensionImpl
public class SpringEntryImplementationProvider implements DomElementImplementationProvider<SpringEntry, SpringEntryImpl> {
  @Nonnull
  @Override
  public Class<SpringEntry> getInterfaceClass() {
    return SpringEntry.class;
  }

  @Nonnull
  @Override
  public Class<SpringEntryImpl> getImplementationClass() {
    return SpringEntryImpl.class;
  }
}
