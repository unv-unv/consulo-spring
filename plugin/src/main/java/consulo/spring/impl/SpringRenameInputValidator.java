package consulo.spring.impl;

import com.intellij.spring.impl.ide.model.xml.beans.SpringBean;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.editor.refactoring.rename.RenameInputValidator;
import consulo.language.pattern.ElementPattern;
import consulo.language.psi.PsiElement;
import consulo.language.util.ProcessingContext;
import consulo.xml.patterns.DomPatterns;

/**
 * @author VISTALL
 * @since 14-Jan-17
 */
@ExtensionImpl
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
