package com.intellij.spring.impl.ide.model.highlighting;

import com.intellij.spring.impl.ide.SpringBundle;
import com.intellij.spring.impl.ide.SpringManager;
import com.intellij.spring.impl.ide.facet.FileSetEditor;
import com.intellij.spring.impl.ide.facet.SpringFileSet;
import com.intellij.spring.impl.ide.facet.XmlSpringFileSet;
import com.intellij.spring.impl.ide.model.xml.beans.Beans;
import consulo.annotation.component.ExtensionImpl;
import consulo.application.WriteAction;
import consulo.application.util.function.Processor;
import consulo.codeEditor.Editor;
import consulo.dataContext.DataManager;
import consulo.language.editor.DaemonCodeAnalyzer;
import consulo.language.editor.annotation.HighlightSeverity;
import consulo.language.editor.inspection.InspectionToolState;
import consulo.language.editor.inspection.LocalQuickFix;
import consulo.language.editor.inspection.ProblemDescriptor;
import consulo.language.editor.intention.SyntheticIntentionAction;
import consulo.language.editor.rawHighlight.HighlightDisplayLevel;
import consulo.language.psi.PsiFile;
import consulo.language.util.IncorrectOperationException;
import consulo.language.util.ModuleUtilCore;
import consulo.module.Module;
import consulo.module.content.ModuleRootManager;
import consulo.module.content.ProjectFileIndex;
import consulo.module.content.ProjectRootManager;
import consulo.module.content.layer.ModifiableRootModel;
import consulo.project.Project;
import consulo.spring.impl.module.extension.SpringModuleExtension;
import consulo.spring.impl.module.extension.SpringMutableModuleExtension;
import consulo.ui.annotation.RequiredUIAccess;
import consulo.ui.ex.popup.BaseListPopupStep;
import consulo.ui.ex.popup.JBPopupFactory;
import consulo.ui.ex.popup.PopupStep;
import consulo.util.lang.ref.Ref;
import consulo.virtualFileSystem.VirtualFile;
import consulo.xml.util.xml.DomFileElement;
import consulo.xml.util.xml.highlighting.DomElementAnnotationHolder;
import consulo.xml.util.xml.highlighting.DomElementAnnotationsManager;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;

import jakarta.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Set;
import java.util.function.Consumer;

/**
 * @author Dmitry Avdeev
 */
@ExtensionImpl
public class SpringExtensionInspection extends SpringBeanInspectionBase<SpringExtensionInspectionState> {
  @Nonnull
  @Override
  public InspectionToolState<?> createStateProvider() {
    return new SpringExtensionInspectionState();
  }

  @Override
  public void checkFileElement(final DomFileElement<Beans> domFileElement,
                               final DomElementAnnotationHolder holder,
                               SpringExtensionInspectionState state) {
    final consulo.module.Module module = domFileElement.getModule();
    if (module == null) {
      return;
    }
    final VirtualFile virtualFile = domFileElement.getFile().getVirtualFile();
    if (virtualFile == null) {
      return;
    }
    final ProjectFileIndex projectFileIndex = ProjectRootManager.getInstance(module.getProject()).getFileIndex();
    if (!projectFileIndex.isInSourceContent(virtualFile) ||
      (!state.checkTestFiles && projectFileIndex.isInTestSourceContent(virtualFile))) {
      return;
    }
    final Ref<SpringModuleExtension> moduleExtensionRef = new Ref<SpringModuleExtension>();
    final boolean notFound = ModuleUtilCore.visitMeAndDependentModules(module, new Processor<Module>() {
      @Override
      public boolean process(final consulo.module.Module module) {
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

  @Override
  @Nonnull
  public HighlightDisplayLevel getDefaultLevel() {
    return HighlightDisplayLevel.WARNING;
  }

  @Override
  @Nls
  @Nonnull
  public String getDisplayName() {
    return SpringBundle.message("spring.facet.inspection");
  }

  @Override
  @NonNls
  @Nonnull
  public String getShortName() {
    return "SpringFacetInspection";
  }

  private static class ConfigureFileSetFix extends EnableExtensionFix {

    protected ConfigureFileSetFix(@Nonnull consulo.module.Module module, PsiFile file) {
      super(module, file);
    }

    @Override
    @Nonnull
    public String getName() {
      return SpringBundle.message("configure.file.set.for.file");
    }

    @Override
    @RequiredUIAccess
    protected void doFix(final Project project) {
      final SpringModuleExtension extension = SpringModuleExtension.getInstance(myModule);
      if (extension != null) {
        final Set<SpringFileSet> sets = extension.getFileSets();
        if (sets.size() == 0) {
          addNewSet(myModule, sets);
        }
        else {
          final ArrayList<SpringFileSet> list = new ArrayList<SpringFileSet>(sets);
          final SpringFileSet newSet = new XmlSpringFileSet(SpringFileSet.getUniqueId(sets),
                                                            SpringBundle.message("fileset.new"), extension) {
            @Override
            public boolean isNew() {
              return true;
            }
          };
          list.add(newSet);
          final BaseListPopupStep<SpringFileSet> step =
            new BaseListPopupStep<SpringFileSet>(SpringBundle.message("choose.file.set"), list) {
              @Override
              @RequiredUIAccess
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

  private static class EnableExtensionFix implements LocalQuickFix, SyntheticIntentionAction {
    protected final Module myModule;
    protected final VirtualFile myVirtualFile;

    protected EnableExtensionFix(@Nonnull consulo.module.Module module, PsiFile file) {
      myModule = module;
      myVirtualFile = file.getVirtualFile();
    }

    @Override
    @Nonnull
    public String getName() {
      return SpringBundle.message("add.spring.facet", myModule.getName());
    }

    @Override
    @Nonnull
    public String getText() {
      return getName();
    }

    @Override
    @Nonnull
    public String getFamilyName() {
      return SpringBundle.message("model.bean.quickfix.family");
    }

    @Override
    public boolean isAvailable(@Nonnull final Project project, final Editor editor, final PsiFile file) {
      return true;
    }

    @Override
    public void invoke(@Nonnull final Project project, final Editor editor, final PsiFile file) throws IncorrectOperationException {
      doFix(project);
      DomElementAnnotationsManager.getInstance(project).dropAnnotationsCache();
      DaemonCodeAnalyzer.getInstance(project).restart();
    }

    @Override
    public boolean startInWriteAction() {
      return false;
    }

    @Override
    public void applyFix(@Nonnull final Project project, @Nonnull final ProblemDescriptor descriptor) {
      doFix(project);
      DomElementAnnotationsManager.getInstance(project).dropAnnotationsCache();
      DaemonCodeAnalyzer.getInstance(project).restart();
    }

    @RequiredUIAccess
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

    @RequiredUIAccess
    protected void addNewSet(final consulo.module.Module module, final Set<SpringFileSet> sets) {
      final SpringFileSet set = new XmlSpringFileSet(SpringFileSet.getUniqueId(sets),
                                                     SpringFileSet.getUniqueName(SpringBundle.message("default.fileset.name"), sets),
                                                     module) {
        @Override
        public boolean isNew() {
          return true;
        }
      };
      editSet(module, sets, set);
    }

    @RequiredUIAccess
    protected void editSet(final Module module, final Set<SpringFileSet> sets, final SpringFileSet set) {
      set.addFile(myVirtualFile);
      final FileSetEditor editor = new FileSetEditor(myModule, set, sets);
      editor.show();
      if (editor.isOK()) {
        modifyExtensionOnce(module, it -> it.getFileSets().add(editor.getEditedFileSet().cloneTo(it)));
      }
    }

    @RequiredUIAccess
    @Nonnull
    public static SpringModuleExtension modifyExtensionOnce(consulo.module.Module module, Consumer<SpringMutableModuleExtension> consumer) {
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
