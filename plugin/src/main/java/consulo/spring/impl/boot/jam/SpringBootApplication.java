package consulo.spring.impl.boot.jam;

import com.intellij.jam.JamElement;
import com.intellij.jam.annotations.JamPsiConnector;
import com.intellij.jam.reflect.JamAnnotationMeta;
import com.intellij.java.language.psi.PsiAnnotation;
import com.intellij.java.language.psi.PsiClass;
import com.intellij.spring.impl.ide.constants.SpringAnnotationsConstants;

import javax.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 15-Jan-17
 */
public abstract class SpringBootApplication implements JamElement {
  private final JamAnnotationMeta myMeta;

  protected SpringBootApplication() {
    myMeta = new JamAnnotationMeta(SpringAnnotationsConstants.SPRING_BOOT_APPLICATION);
  }

  public PsiAnnotation getAnnotation() {
    return myMeta.getAnnotation(getPsiElement());
  }

  @Nonnull
  @JamPsiConnector
  public abstract PsiClass getPsiElement();

  public PsiClass getPsiClass() {
    return getPsiElement();
  }
}
