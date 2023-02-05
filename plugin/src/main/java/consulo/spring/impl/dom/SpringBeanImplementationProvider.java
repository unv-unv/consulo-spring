package consulo.spring.impl.dom;

import com.intellij.spring.impl.model.beans.SpringBeanImpl;
import com.intellij.spring.impl.ide.model.xml.beans.SpringBean;
import consulo.annotation.component.ExtensionImpl;
import consulo.xml.dom.DomElementImplementationProvider;

import javax.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 04/02/2023
 */
@ExtensionImpl
public class SpringBeanImplementationProvider implements DomElementImplementationProvider<SpringBean, SpringBeanImpl> {
  @Nonnull
  @Override
  public Class<SpringBean> getInterfaceClass() {
    return SpringBean.class;
  }

  @Nonnull
  @Override
  public Class<SpringBeanImpl> getImplementationClass() {
    return SpringBeanImpl.class;
  }
}
