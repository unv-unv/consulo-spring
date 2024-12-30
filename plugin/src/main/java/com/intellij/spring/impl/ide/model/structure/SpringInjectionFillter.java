package com.intellij.spring.impl.ide.model.structure;

import com.intellij.spring.impl.ide.SpringBundle;
import com.intellij.spring.impl.ide.SpringIcons;
import consulo.fileEditor.structureView.tree.ActionPresentation;
import consulo.fileEditor.structureView.tree.ActionPresentationData;
import consulo.fileEditor.structureView.tree.Filter;
import consulo.fileEditor.structureView.tree.TreeElement;
import org.jetbrains.annotations.NonNls;

import jakarta.annotation.Nonnull;

public class SpringInjectionFillter implements Filter {
  @NonNls
  public static final String ID = "SHOW_PROPERTIES";

  public boolean isVisible(TreeElement treeNode) {
    return !(treeNode instanceof SpringInjectionTreeElement);
  }

  @Nonnull
  public ActionPresentation getPresentation() {
    return new ActionPresentationData(SpringBundle.message("show.properties.and.constructor.args"),
                                      null,
                                      SpringIcons.SPRING_BEAN_PROPERTY_ICON);
  }

  @Nonnull
  public String getName() {
    return ID;
  }

  public boolean isReverted() {
    return true;
  }
}
