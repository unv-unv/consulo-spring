package com.intellij.spring.impl.ide.model.xml.custom;

import com.intellij.spring.impl.ide.CustomBeanInfo;
import com.intellij.spring.impl.ide.CustomBeanRegistry;
import com.intellij.spring.impl.ide.SpringBundle;
import com.intellij.spring.impl.ide.model.xml.CustomBeanWrapper;
import com.intellij.spring.impl.ide.model.xml.DomSpringBean;
import consulo.annotation.component.ExtensionImpl;
import consulo.application.ApplicationManager;
import consulo.application.CommonBundle;
import consulo.application.progress.ProgressManager;
import consulo.codeEditor.Editor;
import consulo.execution.unscramble.UnscrambleService;
import consulo.language.editor.DaemonCodeAnalyzer;
import consulo.language.editor.intention.IntentionAction;
import consulo.language.editor.intention.IntentionMetaData;
import consulo.language.psi.PsiFile;
import consulo.language.psi.PsiModificationTracker;
import consulo.language.util.IncorrectOperationException;
import consulo.project.Project;
import consulo.ui.ex.action.ActionsBundle;
import consulo.ui.ex.awt.Messages;
import consulo.util.lang.StringUtil;
import consulo.util.lang.ref.Ref;
import consulo.xml.psi.xml.XmlTag;
import consulo.xml.util.xml.DomUtil;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author peter
 */
@ExtensionImpl
@IntentionMetaData(ignoreId = "spring.parse.custom.bean", fileExtensions = "xml", categories = {"XML", "Spring"})
public class ParseCustomBeanIntention implements IntentionAction {

  @Nonnull
  public String getText() {
    return SpringBundle.message("parse.custom.bean.intention");
  }

  public boolean isAvailable(@Nonnull final Project project, final Editor editor, final PsiFile file) {
    return DomUtil.findDomElement(file.findElementAt(editor.getCaretModel().getOffset()), DomSpringBean.class) instanceof CustomBeanWrapper;
  }

  public void invoke(@Nonnull final Project project, final Editor editor, final PsiFile file) throws IncorrectOperationException {
    final CustomBeanWrapper wrapper =
      DomUtil.findDomElement(file.findElementAt(editor.getCaretModel().getOffset()), CustomBeanWrapper.class);
    assert wrapper != null;
    invokeCustomBeanParsers(project, Arrays.asList(wrapper.getXmlTag()));
  }

  public static void invokeCustomBeanParsers(final Project project, final Collection<XmlTag> tags) {
    final Ref<CustomBeanRegistry.ParseResult> ref = Ref.create(null);
    ProgressManager.getInstance().runProcessWithProgressSynchronously(new Runnable() {
      public void run() {
        ProgressManager.getInstance().getProgressIndicator().setIndeterminate(true);
        ApplicationManager.getApplication().runReadAction(new Runnable() {
          public void run() {
            ref.set(CustomBeanRegistry.getInstance(project).parseBeans(tags));
          }
        });
      }
    }, SpringBundle.message("parsing.custom.bean"), false, project);

    ApplicationManager.getApplication().runWriteAction(new Runnable() {
      public void run() {
        PsiModificationTracker.getInstance(project).incCounter();
      }
    });
    DaemonCodeAnalyzer.getInstance(project).restart();

    if (ApplicationManager.getApplication().isUnitTestMode()) return; //the rest deals only with pretty feedback to user 

    final CustomBeanRegistry.ParseResult result = ref.get();

    final String message = result.getErrorMessage();
    if (message != null) {
      Messages.showErrorDialog(project, message, SpringBundle.message("parse.custom.bean.error"));
      return;
    }

    final String trace = result.getStackTrace();
    if (trace != null) {
      String shortTrace = trace;
      int i = 0;
      for (int j = 0; j < 10; j++) {
        if (i >= 0) {
          i = trace.indexOf('\n', i + 1);
        }
      }
      if (i >= 0) {
        shortTrace = trace.substring(0, i) + "\n\t...";
      }

      final int exitCode = Messages.showDialog(project, shortTrace, SpringBundle.message("parse.custom.bean.error"),
                                               new String[]{CommonBundle.getOkButtonText(),
                                                 ActionsBundle.message("action.Unscramble.text")}, 0, Messages.getErrorIcon());
      if (exitCode == 1) {
        UnscrambleService unscrambleService = project.getInstance(UnscrambleService.class);
        unscrambleService.showAsync(trace);
      }
      return;
    }

    final List<CustomBeanInfo> infos = result.getBeans();
    assert infos != null;
    final String beansText = StringUtil.join(infos, it -> "  id = " + it.beanName + "; class = " + it.beanClassName, "\n");

    if (infos.size() == 1 && tags.size() == 1) {
      final String idAttr = infos.get(0).idAttribute;
      if (idAttr != null) {
        final XmlTag tag = tags.iterator().next();
        final String ns = tag.getNamespace();
        final String localName = tag.getLocalName();
        final String inductMessage = SpringBundle.message("parse.these.beans.induct", beansText, ns, localName, idAttr);
        if (Messages.showDialog(project,
                                inductMessage,
                                SpringBundle.message("parse.custom.bean.success"),
                                new String[]{CommonBundle.getYesButtonText(), SpringBundle.message("parse.these.beans.induct.only.this")},
                                1,
                                Messages.getInformationIcon()) == 0) {
          final CustomBeanInfo beanInfo = new CustomBeanInfo(infos.get(0));
          beanInfo.beanName = null;
          CustomBeanRegistry.getInstance(project).addBeanPolicy(ns, localName, beanInfo);
        }
        return;
      }
    }

    final String parsedMessage = !infos.isEmpty()
      ? SpringBundle.message("parse.these.beans", beansText)
      : result.hasInfrastructureBeans()
      ? SpringBundle.message("parse.only.infrastructure.beans")
      : SpringBundle.message("parse.no.custom.beans");
    Messages.showInfoMessage(project, parsedMessage, SpringBundle.message("parse.custom.bean.success"));
  }

  public boolean startInWriteAction() {
    return false;
  }
}