package com.intellij.spring.impl.ide.refactoring;

import com.intellij.spring.impl.ide.SpringBundle;
import com.intellij.spring.impl.ide.SpringManager;
import com.intellij.spring.impl.ide.constants.SpringConstants;
import com.intellij.spring.impl.ide.model.SpringModelVisitor;
import com.intellij.spring.impl.ide.model.xml.CommonSpringBean;
import com.intellij.spring.impl.ide.model.xml.beans.Beans;
import com.intellij.spring.impl.ide.model.xml.beans.SpringBean;
import com.intellij.spring.impl.ide.model.xml.beans.SpringBeanScope;
import consulo.annotation.component.ExtensionImpl;
import consulo.application.ApplicationManager;
import consulo.codeEditor.Editor;
import consulo.document.Document;
import consulo.document.util.TextRange;
import consulo.language.ast.ASTNode;
import consulo.language.codeStyle.CodeStyleManager;
import consulo.language.editor.intention.IntentionAction;
import consulo.language.editor.intention.IntentionMetaData;
import consulo.language.psi.PsiDocumentManager;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.language.util.IncorrectOperationException;
import consulo.project.Project;
import consulo.ui.ex.awt.Messages;
import consulo.xml.psi.xml.*;
import consulo.xml.util.xml.DomFileElement;
import consulo.xml.util.xml.DomManager;
import org.jetbrains.annotations.NonNls;

import jakarta.annotation.Nonnull;

/**
 * @author Dmitry Avdeev
 */
@ExtensionImpl
@IntentionMetaData(ignoreId = "spring.update.scheme", fileExtensions = "xml", categories = {"XML", "Spring"})
public class SpringUpdateSchemaIntention implements IntentionAction {
  @NonNls private static final String BEANS = "beans xmlns=\"http://www.springframework.org/schema/beans\"\n" +
                                                                  "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                                                                  "xsi:schemaLocation=\"http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-2.0.xsd\"";

  @Nonnull
  public String getText() {
    return SpringBundle.message("update.schema.intention");
  }

  @Nonnull
  public String getFamilyName() {
    return getText();
  }

  public boolean isAvailable(@Nonnull final Project project, final Editor editor, final PsiFile file) {
    if (file instanceof XmlFile && SpringManager.getInstance(project).isSpringBeans((XmlFile)file)) {
      final int offset = editor.getCaretModel().getOffset();
      final PsiElement psiElement = file.findElementAt(offset);
      if (PsiTreeUtil.getParentOfType(psiElement, XmlDoctype.class) != null) {
        return true;
      }
      final XmlTag tag = PsiTreeUtil.getParentOfType(psiElement, XmlTag.class);
      if (tag != null && tag.getParentTag() == null && isUpdateNeeded((XmlFile)file)) {
        return true;
      }
    }
    return false;
  }

  public void invoke(@Nonnull final Project project, final Editor editor, final PsiFile file) throws IncorrectOperationException {
    updateSchema((XmlFile)file);
  }

  public static boolean requestSchemaUpdate(final @Nonnull XmlFile file) throws IncorrectOperationException {
    if (!isUpdateNeeded(file)) {
      return true;
    }
    if (!ApplicationManager.getApplication().isUnitTestMode() &&
        Messages.showYesNoDialog(SpringBundle.message("xml.schema.will.be.updated"),
                                 SpringBundle.message("xml.schema.update.is.required"), Messages.getQuestionIcon()) != 0) {
      return false;
    }
    updateSchema(file);
    return true;
  }

  public static void updateSchema(final @Nonnull XmlFile file) throws IncorrectOperationException {

    final Project project = file.getProject();
    final XmlDocument document = file.getDocument();
    assert document != null;
    final XmlProlog prolog = document.getProlog();
    final PsiDocumentManager documentManager = PsiDocumentManager.getInstance(project);
    final Document doc = documentManager.getDocument(file);
    assert doc != null;
    if (prolog != null) {
      final XmlDoctype doctype = prolog.getDoctype();
      if (doctype != null) {
        doctype.delete();
        documentManager.doPostponedOperationsAndUnblockDocument(doc);
      }
    }
    final DomFileElement<Beans> element = DomManager.getDomManager(project).getFileElement(file, Beans.class);
    assert element != null;
    final XmlTag tag = element.getRootTag();
    assert tag != null;
    final ASTNode node = tag.getNode();
    assert node != null;
    final ASTNode child = XmlChildRole.START_TAG_NAME_FINDER.findChild(node);
    assert child != null;
    final TextRange range = child.getTextRange();
    doc.replaceString(range.getStartOffset(), range.getEndOffset(), BEANS);
    documentManager.commitDocument(doc);
    CodeStyleManager.getInstance(project).reformatRange(tag, 1, BEANS.length());

    SpringModelVisitor.visitBeans(new SpringModelVisitor() {
      protected boolean visitBean(final CommonSpringBean bean) {
        if (bean instanceof SpringBean) {
          final Boolean value = ((SpringBean)bean).getSingleton().getValue();
          if (value != null) {
            ((SpringBean)bean).getSingleton().undefine();
            ((SpringBean)bean).getScope().setValue(value.booleanValue() ? SpringBeanScope.SINGLETON_SCOPE : SpringBeanScope.PROROTYPE_SCOPE);
          }
        }
        return true;
      }
    }, element.getRootElement());
  }

  public boolean startInWriteAction() {
    return true;
  }

  public static boolean isUpdateNeeded(@Nonnull final XmlFile config) {
    final XmlDocument document = config.getDocument();
    assert document != null;
    final XmlTag tag = document.getRootTag();
    assert tag != null;
    return !tag.getNamespace().equals(SpringConstants.BEANS_XSD);
  }
}
