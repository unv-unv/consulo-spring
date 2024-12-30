package com.intellij.spring.impl.ide.model.actions.generate;

import jakarta.annotation.Nullable;

import consulo.codeEditor.Editor;
import com.intellij.spring.impl.ide.model.xml.beans.Alias;
import consulo.xml.util.xml.DomElement;
import consulo.xml.util.xml.DomElementNavigationProvider;

public class SpringAliasGenerateProvider extends BasicSpringDomGenerateProvider<Alias> {
  public SpringAliasGenerateProvider() {
    super(getDescription(Alias.class), Alias.class);
  }

  public Alias generate(@Nullable final DomElement parent, final Editor editor) {
    final Alias alias = super.generate(parent, editor);

    if (alias != null) {
      alias.getAlias().ensureXmlElementExists();
      alias.getAliasedBean().ensureXmlElementExists();
    }

    return alias;
  }

  protected DomElement getElementToNavigate(final Alias alias) {
    return alias.getAliasedBean();
  }

  protected void doNavigate(final DomElementNavigationProvider navigateProvider, final DomElement element) {
    navigateProvider.navigate(((Alias)element).getAliasedBean(), true);
  }
}
