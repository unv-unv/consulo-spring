package com.intellij.spring.impl.ide.refactoring;

import com.intellij.spring.impl.ide.SpringBundle;
import com.intellij.spring.impl.ide.constants.SpringConstants;
import com.intellij.spring.impl.ide.model.xml.beans.SpringBean;
import com.intellij.spring.impl.ide.model.xml.beans.SpringProperty;
import com.intellij.xml.XmlNamespaceHelper;
import com.intellij.xml.util.XmlUtil;
import consulo.annotation.component.ExtensionImpl;
import consulo.codeEditor.Editor;
import consulo.language.editor.intention.IntentionAction;
import consulo.language.editor.intention.IntentionMetaData;
import consulo.language.psi.PsiFile;
import consulo.language.util.IncorrectOperationException;
import consulo.project.Project;
import consulo.xml.psi.XmlElementFactory;
import consulo.xml.psi.xml.XmlAttribute;
import consulo.xml.psi.xml.XmlFile;
import consulo.xml.psi.xml.XmlTag;
import consulo.xml.util.xml.DomUtil;

import jakarta.annotation.Nonnull;
import java.util.Collections;

/**
 * @author Dmitry Avdeev
 */
@ExtensionImpl
@IntentionMetaData(ignoreId = "spring.use.pname", fileExtensions = "xml", categories = {"XML", "Spring"})
public class UsePNamespaceIntention implements IntentionAction {
  @Nonnull
  public String getText() {
    return SpringBundle.message("use.p.namespace");
  }

  @Nonnull
  public String getFamilyName() {
    return getText();
  }

  public boolean isAvailable(@Nonnull final Project project, final Editor editor, final PsiFile file) {
    if (!(file instanceof XmlFile)) return false;

    final SpringProperty property = DomUtil.getContextElement(editor, SpringProperty.class);
    return property != null &&
      property.getParent() instanceof SpringBean &&
      property.getName().getStringValue() != null &&
      (DomUtil.hasXml(property.getValueElement()) || DomUtil.hasXml(property.getRefElement()));
  }

  public void invoke(@Nonnull final Project project, final Editor editor, final PsiFile file) throws IncorrectOperationException {
    if (SpringUpdateSchemaIntention.requestSchemaUpdate((XmlFile)file)) {
      final SpringProperty property = DomUtil.getContextElement(editor, SpringProperty.class);
      assert property != null;
      if (property.getXmlTag().getNamespaceByPrefix("p").equals(XmlUtil.EMPTY_URI)) {
        XmlNamespaceHelper.getHelper((XmlFile)file).insertNamespaceDeclaration((XmlFile)file,
                                                                               editor,
                                                                               Collections.<String>singleton(SpringConstants.P_NAMESPACE),
                                                                               "p",
                                                                               null);
      }
      final SpringBean bean = (SpringBean)property.getParent();
      assert bean != null;
      final String name = property.getName().getStringValue();
      final String value = property.getValueElement().getStringValue();
      final XmlTag tag = bean.getXmlTag();
      if (value != null) {
        property.undefine();
        final XmlAttribute attribute = XmlElementFactory.getInstance(project).createXmlAttribute("p:" + name, value);
        tag.add(attribute);
        tag.collapseIfEmpty();
        return;
      }
      final String ref = property.getRefElement().getStringValue();
      if (ref != null) {
        property.undefine();
        final XmlAttribute attribute = XmlElementFactory.getInstance(project).createXmlAttribute("p:" + name + "-ref", ref);
        tag.add(attribute);
        tag.collapseIfEmpty();
      }
    }
  }

  public boolean startInWriteAction() {
    return true;
  }
}
