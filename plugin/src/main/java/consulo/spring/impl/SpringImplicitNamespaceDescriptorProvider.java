package consulo.spring.impl;

import com.intellij.spring.impl.ide.PNamespaceDescriptor;
import com.intellij.spring.impl.ide.constants.SpringConstants;
import com.intellij.xml.XmlNSDescriptor;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.psi.PsiFile;
import consulo.module.Module;
import consulo.xml.javaee.ImplicitNamespaceDescriptorProvider;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * @author VISTALL
 * @since 14-Jan-17
 */
@ExtensionImpl
public class SpringImplicitNamespaceDescriptorProvider implements ImplicitNamespaceDescriptorProvider {
  @Nullable
  @Override
  public XmlNSDescriptor getNamespaceDescriptor(@Nullable Module module, @Nonnull String s, @Nullable PsiFile psiFile) {
    if(SpringConstants.P_NAMESPACE.equals(s)) {
      return new PNamespaceDescriptor();
    }
    return null;
  }
}
