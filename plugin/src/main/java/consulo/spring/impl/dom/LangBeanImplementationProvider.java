package consulo.spring.impl.dom;

import com.intellij.spring.impl.model.lang.LangBeanImpl;
import com.intellij.spring.impl.ide.model.xml.lang.LangBean;
import consulo.annotation.component.ExtensionImpl;
import consulo.xml.dom.DomElementImplementationProvider;

import jakarta.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 04/02/2023
 */
@ExtensionImpl
public class LangBeanImplementationProvider implements DomElementImplementationProvider<LangBean, LangBeanImpl> {
  @Nonnull
  @Override
  public Class<LangBean> getInterfaceClass() {
    return LangBean.class;
  }

  @Nonnull
  @Override
  public Class<LangBeanImpl> getImplementationClass() {
    return LangBeanImpl.class;
  }
}
