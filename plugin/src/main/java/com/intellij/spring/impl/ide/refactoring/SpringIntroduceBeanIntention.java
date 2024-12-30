package com.intellij.spring.impl.ide.refactoring;

import com.intellij.spring.impl.ide.SpringBundle;
import com.intellij.spring.impl.ide.SpringManager;
import com.intellij.spring.impl.ide.model.SpringUtils;
import com.intellij.spring.impl.ide.model.xml.beans.SpringBean;
import com.intellij.spring.impl.ide.model.xml.beans.SpringValueHolder;
import consulo.annotation.component.ExtensionImpl;
import consulo.codeEditor.Editor;
import consulo.document.Document;
import consulo.language.editor.completion.lookup.LookupElement;
import consulo.language.editor.completion.lookup.LookupElementBuilder;
import consulo.language.editor.intention.IntentionAction;
import consulo.language.editor.intention.IntentionMetaData;
import consulo.language.editor.template.*;
import consulo.language.psi.PsiDocumentManager;
import consulo.language.psi.PsiFile;
import consulo.language.util.IncorrectOperationException;
import consulo.logging.Logger;
import consulo.project.Project;
import consulo.util.collection.ContainerUtil;
import consulo.xml.psi.XmlElementFactory;
import consulo.xml.psi.xml.XmlAttribute;
import consulo.xml.psi.xml.XmlFile;
import consulo.xml.psi.xml.XmlTag;
import consulo.xml.util.xml.DomUtil;

import jakarta.annotation.Nonnull;
import java.util.function.Function;

/**
 * @author Dmitry Avdeev
 */
@ExtensionImpl
@IntentionMetaData(ignoreId = "spring.introduce.bean", fileExtensions = "xml", categories = {"XML", "Spring"})
public class SpringIntroduceBeanIntention implements IntentionAction {

  private final static Logger LOG = Logger.getInstance(SpringIntroduceBeanIntention.class);

  @Nonnull
  public String getText() {
    return SpringBundle.message("introduce.bean.intention");
  }

  @Nonnull
  public String getFamilyName() {
    return getText();
  }

  public boolean isAvailable(@Nonnull final Project project, final Editor editor, final PsiFile file) {
    if(!(file instanceof XmlFile) || !SpringManager.getInstance(project).isSpringBeans((XmlFile)file)) return false;
    
    final SpringBean springBean = SpringUtils.getSpringBeanForCurrentCaretPosition(editor, file);
    return springBean != null && springBean.getParent() instanceof SpringValueHolder;
  }

  public void invoke(@Nonnull final Project project, final Editor editor, final PsiFile file) throws IncorrectOperationException {
    final SpringBean springBean = SpringUtils.getSpringBeanForCurrentCaretPosition(editor, file);
    moveToTheTopLevel(project, editor, springBean);
  }

  public static void moveToTheTopLevel(final Project project, final Editor editor, final SpringBean springBean) {
    if (springBean == null) return;
    final SpringBean topLevelBean = SpringUtils.getTopLevelBean(springBean);

    final SpringBean newBean = DomUtil.addElementAfter(topLevelBean);
    newBean.copyFrom(springBean);

    final String id = newBean.getId().getValue();
    if (id == null) {
      try {
        final XmlAttribute attribute = XmlElementFactory.getInstance(project).createXmlAttribute("id", "");
        final XmlTag tag = newBean.getXmlTag();
        final XmlAttribute[] attributes = tag.getAttributes();
        if (attributes.length > 0) {
          tag.addBefore(attribute, attributes[0]);
        } else {
          tag.add(attribute);
        }        
      }
      catch (IncorrectOperationException e) {
        LOG.error(e);
      }
    }

    final SpringValueHolder holder = (SpringValueHolder)springBean.getParent();
    assert holder != null;
    holder.getRefAttr().setStringValue(id == null ? "" : id);

    springBean.undefine();

    final XmlTag tag = holder.getXmlTag();
    tag.collapseIfEmpty();

    if (id != null) {
      return;
    }

    final SpringBean topLevelBeanCopy = topLevelBean.createStableCopy();
    final SpringBean newBeanCopy = newBean.createStableCopy();
    final SpringValueHolder holderCopy = holder.createStableCopy();

    final Document document = editor.getDocument();
    PsiDocumentManager.getInstance(project).doPostponedOperationsAndUnblockDocument(document);

    final int start = topLevelBeanCopy.getXmlTag().getTextOffset();
    final int end = newBeanCopy.getXmlTag().getTextRange().getEndOffset();

    final TemplateManager templateManager = TemplateManager.getInstance(project);
    final Template template = templateManager.createTemplate("", "");
    template.setToReformat(true);

    final String text = document.getText();
    final int refOffset = holderCopy.getRefAttr().getXmlAttributeValue().getTextOffset();
    final int idOffset = newBeanCopy.getId().getXmlAttributeValue().getTextOffset();

    template.addTextSegment(text.substring(start, refOffset));

    final String[] names = SpringUtils.suggestBeanNames(newBean);
    final Expression node = new Expression() {
      public Result calculateResult(final ExpressionContext context) {
        return null;
      }

      public Result calculateQuickResult(final ExpressionContext context) {
        return null;
      }

      public LookupElement[] calculateLookupItems(final ExpressionContext context) {
        return ContainerUtil.map2Array(names, LookupElement.class, (Function<String, LookupElement>) LookupElementBuilder::create);
      }
    };
    template.addVariable("id", node, node, true);
    template.addTextSegment(text.substring(refOffset, idOffset));
    template.addVariableSegment("id");
    template.addTextSegment(text.substring(idOffset, end));

    document.deleteString(start, end);

    templateManager.startTemplate(editor, template);
  }

  public boolean startInWriteAction() {
    return true;
  }
}
