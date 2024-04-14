package consulo.spring.impl.boot.jam;

import com.intellij.jam.JamElement;
import com.intellij.jam.reflect.JamClassMeta;
import com.intellij.java.language.psi.PsiClass;

/**
 * @author VISTALL
 * @since 15-Jan-17
 */
public class SpringBootApplicationElement implements JamElement {
  public static final JamClassMeta<SpringBootApplicationElement> META = new JamClassMeta<>(SpringBootApplicationElement.class);

  private final PsiClass myPsiClass;

  protected SpringBootApplicationElement(PsiClass psiClass) {
    myPsiClass = psiClass;
  }

  public PsiClass getPsiClass() {
    return myPsiClass;
  }
}
