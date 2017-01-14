package consulo.java.jam;

import com.intellij.jam.JamClassGenerator;
import com.intellij.psi.PsiElementRef;
import com.intellij.util.NotNullFunction;
import org.jetbrains.annotations.NotNull;

/**
 * @author VISTALL
 * @since 14-Jan-17
 */
public class JamClassGeneratorImpl extends JamClassGenerator {
  @Override
  public <T> NotNullFunction<PsiElementRef, T> generateJamElementFactory(Class<T> aClass) {
    return new NotNullFunction<PsiElementRef, T>() {
      @NotNull
      @Override
      public T fun(PsiElementRef psiElementRef) {
        return null;
      }
    };
  }
}
