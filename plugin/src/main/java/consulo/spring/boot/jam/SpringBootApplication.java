package consulo.spring.boot.jam;

import javax.annotation.Nonnull;

import com.intellij.jam.JamElement;
import com.intellij.jam.annotations.JamPsiConnector;
import com.intellij.jam.reflect.JamAnnotationMeta;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.spring.constants.SpringAnnotationsConstants;

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
