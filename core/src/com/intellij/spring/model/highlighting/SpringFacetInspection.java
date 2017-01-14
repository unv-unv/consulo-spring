package com.intellij.spring.model.highlighting;

import com.intellij.codeHighlighting.HighlightDisplayLevel;
import com.intellij.codeInsight.daemon.DaemonCodeAnalyzer;
import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.ide.DataManager;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.application.WriteAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.PopupStep;
import com.intellij.openapi.ui.popup.util.BaseListPopupStep;
import com.intellij.openapi.util.Ref;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.spring.SpringBundle;
import com.intellij.spring.SpringManager;
import com.intellij.spring.facet.FileSetEditor;
import com.intellij.spring.facet.SpringFileSet;
import com.intellij.spring.model.xml.beans.Beans;
import com.intellij.util.IncorrectOperationException;
import com.intellij.util.Processor;
import com.intellij.util.xml.DomFileElement;
import com.intellij.util.xml.highlighting.DomElementAnnotationHolder;
import com.intellij.util.xml.highlighting.DomElementAnnotationsManager;
import consulo.annotations.RequiredDispatchThread;
import consulo.spring.module.extension.SpringModuleExtension;
import consulo.spring.module.extension.SpringMutableModuleExtension;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Set;
import java.util.function.Consumer;

/**
 * @author Dmitry Avdeev
 */
public class SpringFacetInspection extends SpringBeanInspectionBase {

  private JPanel myOptionsPanel;
  private JCheckBox myCheckBox;

  public boolean checkTestFiles = false;

  public SpringFacetInspection() {
    myCheckBox.addActionListener(new ActionListener() {
      public void actionPerformed(final ActionEvent e) {
        checkTestFiles = myCheckBox.isSelected();
      }
    });
  }

  public void checkFileElement(final DomFileElement<Beans> domFileElement, final DomElementAnnotationHolder holder) {
    final Module module = domFileElement.getModule();
    if (module == null) {
      return;
    }
    final VirtualFile virtualFile = domFileElement.getFile().getVirtualFile();
    if (virtualFile == null) {
      return;
    }
    final ProjectFileIndex projectFileIndex = ProjectRootManager.getInstance(module.getProject()).getFileIndex();
    if (!projectFileIndex.isInSourceContent(virtualFile) ||
        (!checkTestFiles && projectFileIndex.isInTestSourceContent(virtualFile))) {
      return;
    }
    final Ref<SpringModuleExtension> moduleExtensionRef = new Ref<SpringModuleExtension>();
    final boolean notFound = ModuleUtil.visitMeAndDependentModules(module, new Processor<Module>() {
      public boolean process(final Module module) {
        final SpringModuleExtension facet = SpringModuleExtension.getInstance(module);
        if (facet != null) {
          moduleExtensionRef.set(facet);
          final Set<SpringFileSet> sets = SpringManager.getInstance(module.getProject()).getAllSets(facet);
          for (SpringFileSet fileSet : sets) {
            if (fileSet.hasFile(virtualFile)) {
              return false;
            }
          }

        }
        return true;
      }
    });
    if (!notFound) {
      return;
    }
    final SpringModuleExtension moduleExtension = moduleExtensionRef.get();
    if (moduleExtension == null) {
      holder.createProblem(domFileElement, HighlightSeverity.WARNING,
          SpringBundle.message("spring.facet.not.configured.for.module", module.getName()),
          new EnableExtensionFix(module, domFileElement.getFile()));
    }
    else {
      holder.createProblem(domFileElement, HighlightSeverity.WARNING,
          SpringBundle.message("file.set.not.configured.for.file"),
          new ConfigureFileSetFix(moduleExtension.getModule(), domFileElement.getFile()));

    }
  }

  @NotNull
  public HighlightDisplayLevel getDefaultLevel() {
    return HighlightDisplayLevel.WARNING;
  }

  @Nls
  @NotNull
  public String getDisplayName() {
    return SpringBundle.message("spring.facet.inspection");
  }

  @Override
  public JComponent createOptionsPanel() {
    myCheckBox.setSelected(checkTestFiles);
    return myOptionsPanel;
  }

  @NonNls
  @NotNull
  public String getShortName() {
    return "SpringFacetInspection";
  }

  private static class ConfigureFileSetFix extends EnableExtensionFix {

    protected ConfigureFileSetFix(@NotNull Module module, PsiFile file) {
      super(module, file);
    }

    @NotNull
    public String getName() {
      return SpringBundle.message("configure.file.set.for.file");
    }

    @Override
    @RequiredDispatchThread
    protected void doFix(final Project project) {
      final SpringModuleExtension extension = SpringModuleExtension.getInstance(myModule);
      if (extension != null) {
        final Set<SpringFileSet> sets = extension.getFileSets();
        if (sets.size() == 0) {
          addNewSet(myModule, sets);
        }
        else {
          final ArrayList<SpringFileSet> list = new ArrayList<SpringFileSet>(sets);
          final SpringFileSet newSet = new SpringFileSet(SpringFileSet.getUniqueId(sets),
              SpringBundle.message("fileset.new"), extension) {
            public boolean isNew() {
              return true;
            }
          };
          list.add(newSet);
          final BaseListPopupStep<SpringFileSet> step =
              new BaseListPopupStep<SpringFileSet>(SpringBundle.message("choose.file.set"), list) {
                @RequiredDispatchThread
                public PopupStep onChosen(final SpringFileSet selectedValue, final boolean finalChoice) {
                  if (selectedValue == newSet) {
                    final String name = SpringFileSet.getUniqueName(SpringBundle.message("default.fileset.name"), sets);
                    newSet.setName(name);


                    editSet(myModule, sets, newSet);
                  }
                  else {
                    modifyExtensionOnce(myModule, springMutableModuleExtension -> {
                      selectedValue.addFile(myVirtualFile);
                    });
                  }
                  return super.onChosen(selectedValue, finalChoice);
                }
              };
          JBPopupFactory.getInstance().createListPopup(step).showInBestPositionFor(DataManager.getInstance().getDataContext());
        }
      }
    }
  }

  private static class EnableExtensionFix implements LocalQuickFix, IntentionAction {
    protected final Module myModule;
    protected final VirtualFile myVirtualFile;

    protected EnableExtensionFix(@NotNull Module module, PsiFile file) {
      myModule = module;
      myVirtualFile = file.getVirtualFile();
    }

    @NotNull
    public String getName() {
      return SpringBundle.message("add.spring.facet", myModule.getName());
    }

    @NotNull
    public String getText() {
      return getName();
    }

    @NotNull
    public String getFamilyName() {
      return SpringBundle.message("model.bean.quickfix.family");
    }

    public boolean isAvailable(@NotNull final Project project, final Editor editor, final PsiFile file) {
      return true;
    }

    public void invoke(@NotNull final Project project, final Editor editor, final PsiFile file) throws IncorrectOperationException {
      doFix(project);
      DomElementAnnotationsManager.getInstance(project).dropAnnotationsCache();
      DaemonCodeAnalyzer.getInstance(project).restart();
    }

    public boolean startInWriteAction() {
      return false;
    }

    public void applyFix(@NotNull final Project project, @NotNull final ProblemDescriptor descriptor) {
      doFix(project);
      DomElementAnnotationsManager.getInstance(project).dropAnnotationsCache();
      DaemonCodeAnalyzer.getInstance(project).restart();
    }

    @RequiredDispatchThread
    protected void doFix(final Project project) {
      SpringModuleExtension extension = new DummySpringModuleExtension(myModule);

      final Set<SpringFileSet> sets = SpringManager.getInstance(project).getAllSets(extension);
      for (SpringFileSet fileSet : sets) {
        if (fileSet.hasFile(myVirtualFile)) {
          return;
        }
      }
      addNewSet(myModule, sets);
    }

    @RequiredDispatchThread
    protected void addNewSet(final Module module, final Set<SpringFileSet> sets) {
      final SpringFileSet set = new SpringFileSet(SpringFileSet.getUniqueId(sets), SpringFileSet.getUniqueName(SpringBundle.message("default.fileset.name"), sets), module) {
        public boolean isNew() {
          return true;
        }
      };
      editSet(module, sets, set);
    }

    @RequiredDispatchThread
    protected void editSet(final Module module, final Set<SpringFileSet> sets, final SpringFileSet set) {
      set.addFile(myVirtualFile);
      final FileSetEditor editor = new FileSetEditor(myModule, set, sets);
      editor.show();
      if (editor.isOK()) {
        modifyExtensionOnce(module, it -> it.getFileSets().add(editor.getEditedFileSet().cloneTo(it)));
      }
    }

    @RequiredDispatchThread
    @NotNull
    public static SpringModuleExtension modifyExtensionOnce(Module module, Consumer<SpringMutableModuleExtension> consumer) {
      WriteAction.run(() -> {
        ModifiableRootModel modifiableModel = ModuleRootManager.getInstance(module).getModifiableModel();
        SpringMutableModuleExtension springExtension = modifiableModel.getExtensionWithoutCheck(SpringMutableModuleExtension.class);
        assert springExtension != null;
        springExtension.setEnabled(true);
        consumer.accept(springExtension);
        modifiableModel.commit();
      });
      return ModuleUtilCore.getExtension(module, SpringModuleExtension.class);
    }
  }
}
