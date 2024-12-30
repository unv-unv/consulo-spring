package com.intellij.spring.impl.ide.model.structure;

import com.intellij.spring.impl.ide.SpringManager;
import com.intellij.spring.impl.ide.SpringModel;
import consulo.annotation.component.ExtensionImpl;
import consulo.codeEditor.Editor;
import consulo.fileEditor.FileEditor;
import consulo.fileEditor.structureView.StructureView;
import consulo.fileEditor.structureView.StructureViewBuilder;
import consulo.fileEditor.structureView.StructureViewModel;
import consulo.fileEditor.structureView.TreeBasedStructureViewBuilder;
import consulo.fileEditor.structureView.tree.Sorter;
import consulo.project.Project;
import consulo.xml.ide.structureView.xml.XmlStructureViewBuilderProvider;
import consulo.xml.psi.xml.XmlFile;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

@ExtensionImpl
public class SpringXmlStructureViewBuilderProvider implements XmlStructureViewBuilderProvider {
  @Nullable
  public StructureViewBuilder createStructureViewBuilder(@Nonnull final XmlFile file) {
    final SpringManager springManager = SpringManager.getInstance(file.getProject());
    if (springManager.isSpringBeans(file)) {
      final SpringModel localSpringModel = springManager.getLocalSpringModel(file);
      if (localSpringModel != null) {
        return new TreeBasedStructureViewBuilder() {
          @Nonnull
          public StructureViewModel createStructureViewModel() {
            return new SpringStructureViewModel(file) {
              @Nonnull
              public Sorter[] getSorters() {
                final SpringManager springManager = SpringManager.getInstance(file.getProject());
                return springManager.isSpringBeans(file) ? new Sorter[]{Sorter.ALPHA_SORTER} : Sorter.EMPTY_ARRAY;
              }
            };
          }

          public boolean isRootNodeShown() {
            return true;
          }

          @Nonnull
          @Override
          public StructureViewModel createStructureViewModel(@Nullable Editor editor) {
            throw new UnsupportedOperationException();
          }

          @Nonnull
          public StructureView createStructureView(final FileEditor fileEditor, final Project project) {
            return new SpringStructureViewComponent(fileEditor, createStructureViewModel(), project);

          }
        };
      }
    }
    return null;
  }


}
