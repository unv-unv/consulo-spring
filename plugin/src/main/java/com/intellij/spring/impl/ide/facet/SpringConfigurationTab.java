/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */

package com.intellij.spring.impl.ide.facet;

import com.intellij.spring.impl.ide.SpringBundle;
import com.intellij.spring.impl.ide.SpringIcons;
import com.intellij.spring.impl.ide.SpringManager;
import consulo.disposer.Disposer;
import consulo.language.psi.PsiFile;
import consulo.language.psi.PsiManager;
import consulo.module.Module;
import consulo.project.Project;
import consulo.spring.impl.module.extension.SpringModuleExtension;
import consulo.ui.ex.SimpleTextAttributes;
import consulo.ui.ex.awt.DialogWrapper;
import consulo.ui.ex.awt.Messages;
import consulo.ui.ex.awt.tree.SimpleNode;
import consulo.ui.ex.awt.tree.SimpleTree;
import consulo.ui.ex.awt.tree.SimpleTreeBuilder;
import consulo.ui.ex.awt.tree.SimpleTreeStructure;
import consulo.virtualFileSystem.VirtualFile;
import consulo.virtualFileSystem.pointer.VirtualFilePointer;
import consulo.xml.psi.xml.XmlFile;

import javax.annotation.Nullable;
import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

/**
 * @author Dmitry Avdeev
 */
public class SpringConfigurationTab {

  private JPanel myMainPanel;
  private SimpleTree myTree;

  private JButton myAddSetButton;
  private JButton myRemoveButton;
  private JButton myEditButton;

  private final SpringModuleExtension myModuleExtension;
  private final Set<SpringFileSet> myBuffer = new HashSet<SpringFileSet>();
  private final SimpleTreeBuilder myBuilder;
  private final SpringConfigsSearcher mySearcher;

  private final SimpleNode myRoot = new SimpleNode() {
    public SimpleNode[] getChildren() {
      List<SimpleNode> nodes = new ArrayList<SimpleNode>(myBuffer.size());
      for (SpringFileSet entry : myBuffer) {
        if (!entry.isRemoved()) {
          final FileSetNode setNode = new FileSetNode(entry);
          nodes.add(setNode);
        }
      }
      return nodes.toArray(new SimpleNode[nodes.size()]);
    }

    public boolean isAutoExpandNode() {
      return true;
    }

  };

  public SpringConfigurationTab(final SpringModuleExtension configuration) {

    myModuleExtension = configuration;
    mySearcher = new SpringConfigsSearcher(configuration.getModule());

    final SimpleTreeStructure structure = new SimpleTreeStructure() {
      public Object getRootElement() {
        return myRoot;
      }
    };
    myTree.setRootVisible(false);
    myBuilder = new SimpleTreeBuilder(myTree, (DefaultTreeModel) myTree.getModel(), structure, null);
    myBuilder.initRoot();

    myTree.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {
      public void valueChanged(TreeSelectionEvent e) {
        final SpringFileSet fileSet = getCurrentFileSet();
        myEditButton.setEnabled(fileSet != null);
        myRemoveButton.setEnabled(fileSet != null);
      }
    });

    myAddSetButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        final SpringFileSet fileSet = new SpringFileSet(SpringFileSet.getUniqueId(myBuffer),
            SpringFileSet.getUniqueName(SpringBundle.message("default.fileset.name"), myBuffer),
            myModuleExtension) {
          public boolean isNew() {
            return true;
          }
        };

        final FileSetEditor editor = new FileSetEditor(myMainPanel, fileSet, myBuffer, mySearcher, myModuleExtension.getProject());
        editor.show();
        if (editor.getExitCode() == DialogWrapper.OK_EXIT_CODE) {
          SpringFileSet editedFileSet = editor.getEditedFileSet();
          Disposer.register(configuration, editedFileSet);
          myBuffer.add(editedFileSet);
          myBuilder.updateFromRoot();
          selectFileSet(fileSet);
          apply();
        }
        myTree.requestFocus();
      }
    });

    myRemoveButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        remove();
      }
    });

    myEditButton.addActionListener(new ActionListener() {
      public void actionPerformed(final ActionEvent e) {
        final SpringFileSet fileSet = getCurrentFileSet();
        if (fileSet != null) {
          final FileSetEditor editor = new FileSetEditor(myMainPanel, fileSet, myBuffer, mySearcher, myModuleExtension.getProject());
          editor.show();
          if (editor.getExitCode() == DialogWrapper.OK_EXIT_CODE) {
            myBuffer.remove(fileSet);
            final SpringFileSet edited = editor.getEditedFileSet();
            Disposer.register(configuration, edited);
            myBuffer.add(edited);
            edited.setAutodetected(false);
            edited.setIcon(consulo.spring.impl.SpringIcons.FileSet);
            myBuilder.updateFromRoot();
            selectFileSet(edited);
            apply();
          }
          myTree.requestFocus();
        }
      }
    });

    reset();
  }

  private void remove() {
    final SimpleNode[] nodes = myTree.getSelectedNodesIfUniform();
    for (SimpleNode node : nodes) {

      if (node instanceof DependencyNode) {
        final SpringFileSet fileSet = ((FileSetNode) node.getParent()).mySet;
        fileSet.removeDependency(((DependencyNode) node).myFileSet.getId());
      }
      else if (node instanceof FileSetNode) {
        final SpringFileSet fileSet = ((FileSetNode) node).mySet;
        final int result = Messages.showYesNoDialog(myMainPanel,
            SpringBundle.message("config.remove.button.message", fileSet.getName()),
            SpringBundle.message("config.remove.button.title"),
            Messages.getQuestionIcon());
        if (result == 0) {
          if (fileSet.isAutodetected()) {
            fileSet.setRemoved(true);
            myBuffer.add(fileSet);
          }
          else {
            myBuffer.remove(fileSet);
          }
          for (SpringFileSet set : myBuffer) {
            set.removeDependency(fileSet.getId());
          }
        }
      }
      else if (node instanceof ConfigFileNode) {
        final VirtualFilePointer filePointer = ((ConfigFileNode) node).myFilePointer;
        final SpringFileSet fileSet = ((FileSetNode) node.getParent()).mySet;
        fileSet.removeFile(filePointer);
      }
    }

    myBuilder.updateFromRoot();
    myTree.requestFocus();
    apply();
  }

  @Nullable
  private SpringFileSet getCurrentFileSet() {
    final FileSetNode currentFileSetNode = getCurrentFileSetNode();
    return currentFileSetNode == null ? null : currentFileSetNode.mySet;
  }

  @Nullable
  private FileSetNode getCurrentFileSetNode() {
    final SimpleNode selectedNode = myTree.getSelectedNode();
    if (selectedNode == null) {
      return null;
    }
    if (selectedNode instanceof FileSetNode) {
      return (FileSetNode) selectedNode;
    }
    else if (selectedNode.getParent() instanceof FileSetNode) {
      return (FileSetNode) selectedNode.getParent();
    }
    else {
      final SimpleNode parent = selectedNode.getParent();
      if (parent != null && parent.getParent() instanceof FileSetNode) {
        return (FileSetNode) selectedNode.getParent().getParent();
      }
    }
    return null;
  }

  public JComponent createComponent() {
    return myMainPanel;
  }

  public void apply() {
    final Set<SpringFileSet> fileSets = myModuleExtension.getFileSets();
    fileSets.clear();
    for (SpringFileSet fileSet : myBuffer) {
      if (!fileSet.isAutodetected() || fileSet.isRemoved()) {
        fileSets.add(fileSet);
      }
    }
  }

  public void reset() {
    myBuffer.clear();
    final Module module = myModuleExtension.getModule();
    final SpringModuleExtension springFacet = myModuleExtension;
    final Set<SpringFileSet> sets = SpringManager.getInstance(module.getProject()).getAllSets(springFacet);
    for (SpringFileSet fileSet : sets) {
      myBuffer.add(new SpringFileSet(fileSet));
    }

    myBuilder.updateFromRoot();

    myTree.setSelectionRow(0);
  }

  public void disposeUIResources() {
    Disposer.dispose(myBuilder);
  }

  private void selectFileSet(final SpringFileSet fileSet) {
    myTree.select(myBuilder, new Predicate<SimpleNode>() {
      public boolean test(final SimpleNode simpleNode) {
        if (simpleNode instanceof FileSetNode) {
          if (((FileSetNode) simpleNode).mySet.equals(fileSet)) {
            return true;
          }
        }
        return false;
      }
    }, false);
  }

  private class FileSetNode extends SimpleNode {

    protected final SpringFileSet mySet;

    FileSetNode(SpringFileSet fileSet) {
      mySet = fileSet;

      final String name = mySet.getName();
      setPlainText(mySet.isAutodetected() ? name + " " + SpringBundle.message("config.fileset.autodetected") : name);
      setIcon(fileSet.getIcon());
    }

    public SimpleNode[] getChildren() {
      final ArrayList<SimpleNode> nodes = new ArrayList<SimpleNode>();
      deps:
      for (String dep : mySet.getDependencies()) {
        final consulo.module.Module module = myModuleExtension.getModule();
        for (SpringFileSet fileSet : myBuffer) {
          if (fileSet.getId().equals(dep)) {
            nodes.add(new DependencyNode(fileSet, this));
            continue deps;
          }
        }
        final SpringModuleExtension springFacet = SpringModuleExtension.getInstance(module);
        assert springFacet != null;
        final List<SpringFileSet> models = SpringManager.getInstance(module.getProject()).getProvidedModels(springFacet);
        for (SpringFileSet fileSet : models) {
          if (fileSet.getId().equals(dep)) {
            nodes.add(new DependencyNode(fileSet, this));
            continue deps;
          }
        }
      }
      for (VirtualFilePointer file : mySet.getFiles()) {
        nodes.add(new ConfigFileNode(file, this));
      }
      return nodes.toArray(new SimpleNode[nodes.size()]);
    }

    public boolean isAutoExpandNode() {
      return true;
    }

    public Object[] getEqualityObjects() {
      return new Object[]{mySet, mySet.getName(), mySet.getFiles(), mySet.getDependencies()};
    }
  }

  private static class DependencyNode extends SimpleNode {
    private final SpringFileSet myFileSet;

    DependencyNode(SpringFileSet fileSet, SimpleNode parent) {
      super(parent);
      myFileSet = fileSet;
      setPlainText(fileSet.getName());
      setIcon(SpringIcons.DEPENDENCY);
    }

    public SimpleNode[] getChildren() {
      return NO_CHILDREN;
    }
  }

  private class ConfigFileNode extends SimpleNode {
    private final VirtualFilePointer myFilePointer;

    ConfigFileNode(VirtualFilePointer name, SimpleNode parent) {
      super(parent);
      myFilePointer = name;
      setIcon(consulo.spring.impl.SpringIcons.SpringConfig);
    }

    protected void doUpdate() {
      final VirtualFile file = myFilePointer.getFile();
      if (file != null) {
        final Project project = myModuleExtension.getProject();
        final PsiFile psiFile = PsiManager.getInstance(project).findFile(file);
        if (!(psiFile instanceof XmlFile) || !SpringManager.getInstance(project).isSpringBeans((XmlFile) psiFile)) {
          renderFile(SimpleTextAttributes.ERROR_ATTRIBUTES,
              SimpleTextAttributes.ERROR_ATTRIBUTES,
              SpringBundle.message("config.file.is.not.spring"));
          return;
        }
        renderFile(SimpleTextAttributes.REGULAR_ATTRIBUTES,
            SimpleTextAttributes.GRAYED_ATTRIBUTES,
            null);
      }
      else {
        renderFile(SimpleTextAttributes.ERROR_ATTRIBUTES,
            SimpleTextAttributes.ERROR_ATTRIBUTES,
            SpringBundle.message("config.file.not.found"));
      }
    }

    private void renderFile(SimpleTextAttributes main, SimpleTextAttributes full, @Nullable String toolTip) {
      addColoredFragment(myFilePointer.getFileName(), toolTip, main);
      addColoredFragment(" (" + myFilePointer.getPresentableUrl() + ")", toolTip, full);
    }

    public SimpleNode[] getChildren() {
      return NO_CHILDREN;
    }
  }
}
