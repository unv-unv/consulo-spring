package consulo.spring.impl.boot.domOverAnnotation;

import com.intellij.java.language.psi.PsiJavaPackage;
import com.intellij.spring.impl.ide.model.context.ComponentScan;
import com.intellij.spring.impl.model.AbstractDomSpringBean;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.language.psi.PsiManager;
import consulo.module.Module;
import consulo.xml.psi.xml.XmlTag;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;

/**
 * @author VISTALL
 * @since 2024-04-13
 */
public class AnnotatationComponentScan extends AbstractDomSpringBean implements ComponentScan {

  private final List<PsiJavaPackage> basePackages;

  public AnnotatationComponentScan(List<PsiJavaPackage> basePackages) {
    this.basePackages = basePackages;
  }

  @Override
  public boolean isValid() {
    return true;
  }

  @Override
  public XmlTag getXmlTag() {
    return null;
  }

  @Nullable
  @Override
  public String getClassName() {
    return null;
  }

  @Override
  public PsiManager getPsiManager() {
    return null;
  }

  @Nullable
  @Override
  public Module getModule() {
    return null;
  }

  @jakarta.annotation.Nullable
  @Override
  public PsiElement getIdentifyingPsiElement() {
    return null;
  }

  @Nullable
  @Override
  public PsiFile getContainingFile() {
    return null;
  }

  @Nullable
  @Override
  public String getBeanName() {
    return null;
  }

  @Nonnull
  @Override
  public String[] getAliases() {
    return new String[0];
  }

  @Override
  public Collection<PsiJavaPackage> getBasePackages() {
    return basePackages;
  }

  @Override
  public String toString() {
    return basePackages.toString();
  }
}
