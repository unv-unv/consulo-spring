package com.intellij.spring.impl.ide.refactoring;

import com.intellij.spring.impl.ide.SpringBundle;
import com.intellij.spring.impl.ide.model.SpringModelVisitor;
import com.intellij.spring.impl.ide.model.xml.beans.*;
import consulo.language.editor.WriteCommandAction;
import consulo.language.editor.refactoring.ui.RefactoringDialog;
import consulo.language.editor.ui.awt.EditorComboBox;
import consulo.language.psi.PsiFile;
import consulo.language.psi.PsiManager;
import consulo.language.psi.PsiReference;
import consulo.language.psi.search.ReferencesSearch;
import consulo.language.util.IncorrectOperationException;
import consulo.logging.Logger;
import consulo.project.Project;
import consulo.ui.ex.RecentsManager;
import consulo.ui.ex.awt.ComboboxWithBrowseButton;
import consulo.util.collection.ArrayUtil;
import consulo.virtualFileSystem.VirtualFile;
import consulo.virtualFileSystem.VirtualFileManager;
import consulo.virtualFileSystem.util.VirtualFileUtil;
import consulo.xml.psi.xml.XmlFile;
import consulo.xml.util.xml.DomFileElement;
import consulo.xml.util.xml.DomManager;
import org.jetbrains.annotations.NonNls;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Dmitry Avdeev
 */
public class SpringBeanMoveDialog extends RefactoringDialog {

  private final SpringBean mySpringBean;
  private JPanel myPanel;
  private ComboboxWithBrowseButton myFileCombo;
  private JLabel myMessage;

  @NonNls
  private static final String SPRING_CONFIG_FILE_RECENTS = "spring.config.file.recents";
  public static final Logger LOG = Logger.getInstance("#com.intellij.spring.refactoring.SpringBeanMoveDialog");

  public SpringBeanMoveDialog(@Nonnull final Project project, SpringBean springBean) {
    super(project, true);
    setTitle(SpringBundle.message("move.bean"));
    myMessage.setText(SpringBundle.message("move.bean.name", springBean.getBeanName()));
    mySpringBean = springBean;

    init();

    final PsiFile psiFile = springBean.getContainingFile();
    final List<String> list = RecentsManager.getInstance(project).getRecentEntries(SPRING_CONFIG_FILE_RECENTS);
    if (list != null) {
      final List<String> recentEntries = new ArrayList<String>(list);
      if (psiFile != null) {
        final VirtualFile virtualFile = psiFile.getVirtualFile();
        if (virtualFile != null) {
          recentEntries.remove(virtualFile.getPath());
        }
      }
      ((EditorComboBox)myFileCombo.getComboBox()).setHistory(ArrayUtil.toStringArray(recentEntries));
    }
    myFileCombo.addActionListener(new ActionListener() {
      public void actionPerformed(final ActionEvent e) {
        final ConfigFileChooser chooser = new ConfigFileChooser(project, psiFile);
        chooser.show();
        final XmlFile selectedFile = chooser.getSelectedFile();
        if (selectedFile != null) {
          final VirtualFile virtualFile = selectedFile.getVirtualFile();
          assert virtualFile != null;
          final String path = virtualFile.getPath();
          final DefaultComboBoxModel model = (DefaultComboBoxModel)myFileCombo.getComboBox().getModel();
          if (model.getIndexOf(path) < 0) {
            model.addElement(path);
          }
          model.setSelectedItem(path);
          pack();
        }
      }
    });
  }

  @Nullable
  private XmlFile getTargetFile() {
    final String path = (String)myFileCombo.getComboBox().getSelectedItem();
    if (path != null) {
      final VirtualFile virtualFile = VirtualFileManager.getInstance().findFileByUrl(VirtualFileUtil.pathToUrl(path.trim()));
      if (virtualFile != null) {
        final PsiFile psiFile = PsiManager.getInstance(myProject).findFile(virtualFile);
        return psiFile instanceof XmlFile ? (XmlFile)psiFile : null;
      }
    }
    return null;
  }

  protected boolean areButtonsValid() {
    return getTargetFile() != null;
  }

  protected Action[] createActions() {
    return new Action[]{getRefactorAction(), getCancelAction()};
  }

  protected void doAction() {
    final XmlFile file = getTargetFile();
    if (file != null) {
      doMove(file, mySpringBean, myProject);
      final VirtualFile virtualFile = file.getVirtualFile();
      assert virtualFile != null;
      RecentsManager.getInstance(myProject).registerRecentEntry(SPRING_CONFIG_FILE_RECENTS, virtualFile.getPath());
    }
    close(OK_EXIT_CODE);
  }

  public static void doMove(final XmlFile file, final SpringBean springBean, final Project project) {
    final PsiFile psiFile = springBean.getXmlTag().getContainingFile();
    new WriteCommandAction.Simple(project, SpringBundle.message("move.bean"), psiFile, file) {
      protected void run() throws Throwable {

        final DomFileElement<Beans> fileElement = DomManager.getDomManager(project).getFileElement(file, Beans.class);
        assert fileElement != null;
        final Beans beans = fileElement.getRootElement();
        final SpringBean bean = beans.addBean();

        SpringModelVisitor.visitBean(new SpringModelVisitor() {
          protected boolean visitRef(final SpringRef ref) {
            visitRefBase(ref);
            return super.visitRef(ref);
          }

          protected boolean visitIdref(final Idref idref) {
            visitRefBase(idref);
            return super.visitIdref(idref);
          }

          private void visitRefBase(RefBase refBase) {
            final String local = refBase.getLocal().getStringValue();
            if (local != null) {
              refBase.getBean().setStringValue(local);
              refBase.getLocal().undefine();
            }
          }
        }, springBean);

        bean.copyFrom(springBean);

        for (PsiReference psiReference : ReferencesSearch.search(springBean.getXmlTag())) {
          try {
            psiReference.bindToElement(bean.getXmlTag());
          }
          catch (IncorrectOperationException e) {
            LOG.error("Can't bind " + psiReference + " to " + bean, e);
          }
        }

        springBean.undefine();
      }
    }.execute();
  }

  protected JComponent createCenterPanel() {
    return myPanel;
  }

  private void createUIComponents() {
    myFileCombo = new ComboboxWithBrowseButton(new EditorComboBox(""));
  }
}
