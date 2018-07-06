package com.intellij.spring.model.structure;

import javax.annotation.Nonnull;

import org.jetbrains.annotations.NonNls;
import com.intellij.ide.util.treeView.smartTree.ActionPresentation;
import com.intellij.ide.util.treeView.smartTree.ActionPresentationData;
import com.intellij.ide.util.treeView.smartTree.Filter;
import com.intellij.ide.util.treeView.smartTree.TreeElement;
import com.intellij.spring.SpringBundle;
import com.intellij.spring.SpringIcons;
import consulo.awt.TargetAWT;

public class SpringInjectionFillter implements Filter {
  @NonNls public static final String ID = "SHOW_PROPERTIES";

  public boolean isVisible(TreeElement treeNode) {
    return !(treeNode instanceof SpringInjectionTreeElement);
  }

  @Nonnull
  public ActionPresentation getPresentation() {
    return new ActionPresentationData(SpringBundle.message("show.properties.and.constructor.args"), null, TargetAWT.to(SpringIcons.SPRING_BEAN_PROPERTY_ICON));
  }

  @Nonnull
  public String getName() {
    return ID;
  }

  public boolean isReverted() {
    return true;
  }
}
