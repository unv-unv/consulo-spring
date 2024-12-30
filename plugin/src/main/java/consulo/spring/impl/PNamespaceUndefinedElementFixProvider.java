package consulo.spring.impl;

import com.intellij.java.impl.psi.impl.beanProperties.CreateBeanPropertyFix;
import com.intellij.java.language.psi.JavaPsiFacade;
import com.intellij.java.language.psi.PsiClass;
import com.intellij.java.language.psi.PsiType;
import com.intellij.spring.impl.ide.PNamespaceDescriptor;
import com.intellij.spring.impl.ide.SpringManager;
import com.intellij.spring.impl.ide.SpringModel;
import com.intellij.spring.impl.ide.model.SpringUtils;
import com.intellij.spring.impl.ide.model.xml.beans.SpringBeanPointer;
import com.intellij.xml.XmlUndefinedElementFixProvider;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.editor.intention.IntentionAction;
import consulo.project.Project;
import consulo.xml.psi.xml.XmlAttribute;
import consulo.xml.psi.xml.XmlFile;
import consulo.xml.psi.xml.XmlTag;
import org.jetbrains.annotations.NonNls;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * @author VISTALL
 * @since 14-Jan-17
 */
@ExtensionImpl
public class PNamespaceUndefinedElementFixProvider extends XmlUndefinedElementFixProvider {
  @Nullable
  @Override
  public IntentionAction[] createFixes(@Nonnull XmlAttribute element) {
    final PsiClass psiClass = PNamespaceDescriptor.getClass((XmlTag) element.getParent());
    if (psiClass != null) {
      PsiType type = null;
      @NonNls final String localName = ((XmlAttribute) element).getLocalName();
      final Project project = element.getProject();
      if (localName.endsWith("-ref")) {
        final SpringModel model = SpringManager.getInstance(project).getSpringModelByFile((XmlFile) element.getContainingFile());
        final SpringBeanPointer pointer = SpringUtils.getBeanPointer(model, ((XmlAttribute) element).getDisplayValue());
        if (pointer != null && pointer.getEffectiveBeanType().length > 0) {
          type = JavaPsiFacade.getInstance(project).getElementFactory().createType(pointer.getEffectiveBeanType()[0]);
        }
      }
      @NonNls String name = ((XmlAttribute) element).getLocalName();
      if (name.endsWith("-ref")) {
        name = name.substring(0, name.length() - "-ref".length());
      }
      return CreateBeanPropertyFix.createActions(name, psiClass, type, true);
    }
    return IntentionAction.EMPTY_ARRAY;
  }
}
