package consulo.spring;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.intellij.javaee.ImplicitNamespaceDescriptorProvider;
import com.intellij.openapi.module.Module;
import com.intellij.psi.PsiFile;
import com.intellij.spring.PNamespaceDescriptor;
import com.intellij.spring.constants.SpringConstants;
import com.intellij.xml.XmlNSDescriptor;

/**
 * @author VISTALL
 * @since 14-Jan-17
 */
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
