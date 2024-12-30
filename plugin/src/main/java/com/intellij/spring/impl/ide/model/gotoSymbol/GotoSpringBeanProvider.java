package com.intellij.spring.impl.ide.model.gotoSymbol;

import com.intellij.spring.impl.ide.SpringManager;
import com.intellij.spring.impl.ide.SpringModel;
import com.intellij.spring.impl.ide.model.xml.beans.SpringBaseBeanPointer;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.psi.PsiElement;
import consulo.module.Module;
import consulo.navigation.NavigationItem;
import consulo.spring.impl.module.extension.SpringModuleExtension;
import consulo.util.lang.StringUtil;
import consulo.xml.util.xml.model.gotosymbol.GoToSymbolProvider;

import jakarta.annotation.Nonnull;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * User: Sergey.Vasiliev
 */
@ExtensionImpl
public class GotoSpringBeanProvider extends GoToSymbolProvider {

  protected void addNames(@Nonnull final Module module, final Set<String> result) {
    SpringModel springModel = SpringManager.getInstance(module.getProject()).getCombinedModel(module);
    if(springModel != null) {
      for (SpringBaseBeanPointer pointer : springModel.getAllCommonBeans()) {
        result.addAll(getNames(pointer));
      }
    }
  }

  private void addNonNull(Set<String> result, String aliase) {
    if (!StringUtil.isEmptyOrSpaces(aliase)) {
      result.add(aliase);
    }
  }

  protected void addItems(@Nonnull final Module module, final String name, final List<NavigationItem> result) {
    SpringModel springModel = SpringManager.getInstance(module.getProject()).getCombinedModel(module);
    if(springModel != null) {
      for (SpringBaseBeanPointer pointer : springModel.getAllCommonBeans()) {
        final Set<String> beanNames = getNames(pointer);
        if (beanNames.contains(name)) {
          final PsiElement element = pointer.getSpringBean().getIdentifyingPsiElement();

          if (element != null) {
            result.add(createNavigationItem(element, name, pointer.getBeanIcon()));
          }
        }
      }
    }
  }

  private Set<String> getNames(SpringBaseBeanPointer pointer) {
    Set<String> names = new HashSet<String>();
    addNonNull(names, pointer.getName());

    for (String aliase : pointer.getAliases()) {
      addNonNull(names, aliase);
    }
    return names;
  }

  protected boolean acceptModule(final Module module) {
    return SpringModuleExtension.getInstance(module) != null;
  }
}

