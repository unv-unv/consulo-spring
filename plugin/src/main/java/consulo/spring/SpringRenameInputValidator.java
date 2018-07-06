package consulo.spring;

import com.intellij.patterns.DomPatterns;
import com.intellij.patterns.ElementPattern;
import com.intellij.psi.PsiElement;
import com.intellij.refactoring.rename.RenameInputValidator;
import com.intellij.spring.model.xml.beans.SpringBean;
import com.intellij.util.ProcessingContext;

/**
 * @author VISTALL
 * @since 14-Jan-17
 */
public class SpringRenameInputValidator implements RenameInputValidator {
  @Override
  public ElementPattern<? extends PsiElement> getPattern() {
    return DomPatterns.domTargetElement(DomPatterns.domElement(SpringBean.class));
  }

  @Override
  public boolean isInputValid(String s, PsiElement psiElement, ProcessingContext processingContext) {
    return true;
  }
}
