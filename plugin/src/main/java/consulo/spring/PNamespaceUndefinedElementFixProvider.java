package consulo.spring;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.jetbrains.annotations.NonNls;
import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiType;
import com.intellij.psi.impl.beanProperties.CreateBeanPropertyFix;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import com.intellij.spring.PNamespaceDescriptor;
import com.intellij.spring.SpringManager;
import com.intellij.spring.SpringModel;
import com.intellij.spring.model.SpringUtils;
import com.intellij.spring.model.xml.beans.SpringBeanPointer;
import com.intellij.xml.XmlUndefinedElementFixProvider;

/**
 * @author VISTALL
 * @since 14-Jan-17
 */
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
