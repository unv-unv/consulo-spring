package com.intellij.spring.impl.ide.model.actions.patterns.frameworks.ui;

import consulo.language.editor.template.Template;
import consulo.module.Module;
import com.intellij.spring.impl.ide.model.actions.patterns.frameworks.util.StandardBeansDocLinksManager;

public class TemplateInfo {
  private Template myTemplate;
  private String myName;
  private String myReferenceLink;
  private String myApiLink;
  private String myDescription;
  private boolean myAccepted;

  public TemplateInfo(final Module module, final Template template, String name, String description) {
     this(module,  template, name, description, true);
  }
  public TemplateInfo(final Module module, final Template template, String name, String description, boolean isAccepted) {
    myTemplate = template;
    myName = name;
    final StandardBeansDocLinksManager linksManager = StandardBeansDocLinksManager.getInstance(module.getProject());
    myReferenceLink = linksManager.getReferenceLink(template.getId());
    myApiLink = linksManager.getApiLink(template.getId());
    myDescription = description;
    myAccepted = isAccepted;
  }

  public Template getTemplate() {
    return myTemplate;
  }

  public boolean isAccepted() {
    return myAccepted;
  }

  public void setAccepted(final boolean accepted) {
    myAccepted = accepted;
  }

  public String getName() {
    return myName;
  }

  public String getDescription() {
    return myDescription == null ? "" : myDescription;
  }

  public String getReferenceLink() {
    return myReferenceLink;
  }

  public void setReferenceLink(final String referenceLink) {
    myReferenceLink = referenceLink;
  }

  public String getApiLink() {
    return myApiLink;
  }

  public void setApiLink(final String apiLink) {
    myApiLink = apiLink;
  }
}
