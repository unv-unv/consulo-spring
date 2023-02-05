package com.intellij.spring.impl.ide.model.actions.generate;

import consulo.codeEditor.Editor;
import com.intellij.spring.impl.ide.SpringBundle;
import com.intellij.spring.impl.ide.model.xml.beans.SpringImport;
import consulo.xml.util.xml.DomElement;
import javax.annotation.Nullable;

public class SpringImportGenerateProvider extends BasicSpringDomGenerateProvider<SpringImport> {
  public SpringImportGenerateProvider() {
    super(SpringBundle.message("spring.import"), SpringImport.class);
  }

  public SpringImport generate(@Nullable final DomElement parent, final Editor editor) {
    final SpringImport springImport = super.generate(parent, editor);

    if (springImport != null) {
      springImport.getResource().ensureXmlElementExists();
    }

    return springImport;
  }

  protected DomElement getElementToNavigate(final SpringImport springImport) {
    return springImport.getResource();
  }
}
