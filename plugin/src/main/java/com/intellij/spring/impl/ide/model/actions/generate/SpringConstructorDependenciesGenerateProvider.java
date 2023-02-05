package com.intellij.spring.impl.ide.model.actions.generate;

import consulo.codeEditor.Editor;
import consulo.language.psi.PsiFile;
import consulo.project.Project;
import consulo.util.lang.Pair;
import com.intellij.spring.impl.ide.SpringBundle;
import com.intellij.spring.impl.ide.model.SpringUtils;
import com.intellij.spring.impl.ide.model.xml.beans.ConstructorArg;
import com.intellij.spring.impl.ide.model.xml.beans.SpringBean;
import com.intellij.spring.impl.ide.model.xml.beans.SpringInjection;
import consulo.xml.util.xml.DomElement;
import consulo.xml.util.xml.DomElementNavigationProvider;
import consulo.xml.util.xml.actions.generate.AbstractDomGenerateProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.List;

public class SpringConstructorDependenciesGenerateProvider extends AbstractDomGenerateProvider<ConstructorArg> {
  public SpringConstructorDependenciesGenerateProvider() {
    super(SpringBundle.message("spring.generate.constructor.dependencies"), ConstructorArg.class);
  }

  protected DomElement getParentDomElement(final Project project, final Editor editor, final PsiFile file) {
    return SpringUtils.getSpringBeanForCurrentCaretPosition(editor, file);
  }

  public ConstructorArg generate(@Nullable final DomElement parent, final Editor editor) {
    if ( !(parent instanceof SpringBean)) return null;
    final List<Pair<SpringInjection,SpringGenerateTemplatesHolder>> list = GenerateSpringBeanDependenciesUtil.generateDependenciesFor((SpringBean)parent, false);

    for (Pair<SpringInjection, SpringGenerateTemplatesHolder> pair : list) {
      pair.getSecond().runTemplates();
    }
    return  list.size() > 0 ? (ConstructorArg)list.get(0).getFirst() : null;
  }

  protected void doNavigate(final DomElementNavigationProvider navigateProvider, final DomElement element) {
   //super.doNavigate(navigateProvider, element);
  }
  
  public boolean isAvailableForElement(@Nonnull final DomElement contextElement) {
    return contextElement instanceof SpringBean && GenerateSpringBeanDependenciesUtil.acceptBean((SpringBean)contextElement, false);
  }

}
