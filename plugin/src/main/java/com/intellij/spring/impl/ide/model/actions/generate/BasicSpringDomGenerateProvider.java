package com.intellij.spring.impl.ide.model.actions.generate;

import com.intellij.spring.impl.ide.SpringManager;
import com.intellij.spring.impl.ide.SpringModel;
import consulo.codeEditor.Editor;
import consulo.language.psi.PsiFile;
import consulo.project.Project;
import consulo.spring.impl.DomSpringModel;
import consulo.xml.psi.xml.XmlFile;
import consulo.xml.util.xml.DomElement;
import consulo.xml.util.xml.actions.generate.AbstractDomGenerateProvider;

/**
 * User: Sergey.Vasiliev
 */
public abstract class BasicSpringDomGenerateProvider<T extends DomElement> extends AbstractDomGenerateProvider<T> {

  protected BasicSpringDomGenerateProvider(final String description, final Class<T> tClass) {
    super(description, tClass);
  }

  protected BasicSpringDomGenerateProvider(final String description, final Class<T> tClass, final String mappingId) {
    super(description, tClass, mappingId);
  }

  protected DomElement getParentDomElement(final Project project, final Editor editor, final PsiFile file) {
    final SpringModel springModel = SpringManager.getInstance(project).getLocalSpringModel((XmlFile)file);
    if(springModel instanceof DomSpringModel) {
      return ((DomSpringModel) springModel).getMergedModel();
    }
    return null;
  }
}
