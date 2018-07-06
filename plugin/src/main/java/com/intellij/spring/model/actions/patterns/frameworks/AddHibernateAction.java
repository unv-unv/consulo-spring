package com.intellij.spring.model.actions.patterns.frameworks;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.Nullable;

import org.jetbrains.annotations.NonNls;
import com.intellij.codeInsight.template.impl.TemplateSettings;
import com.intellij.facet.ui.libraries.LibraryInfo;
import com.intellij.openapi.module.Module;
import com.intellij.spring.SpringBundle;
import com.intellij.spring.model.actions.patterns.PatternIcons;
import com.intellij.spring.model.actions.patterns.frameworks.ui.LibrariesInfo;
import com.intellij.spring.model.actions.patterns.frameworks.ui.TemplateInfo;
import com.intellij.spring.model.actions.patterns.frameworks.util.LibrariesConfigurationManager;
import consulo.ui.image.Image;

public class AddHibernateAction extends AbstractFrameworkIntegrationAction {
  @NonNls private static final String HIBERNATE_STRING_ID = "hibernate";
  @NonNls private static final String HIBERNATE_FACET_NAME = "Hibernate";

  protected String[] getBeansClassNames() {
    return new String[]{"org.springframework.orm.hibernate3.LocalSessionFactoryBean",
      "org.springframework.orm.hibernate3.annotation.AnnotationSessionFactoryBean"};
  }

  protected LibrariesInfo getLibrariesInfo(final Module module) {
    final LibraryInfo[] libraryInfos = LibrariesConfigurationManager.getInstance(module.getProject()).getLibraryInfos("hibernate");

    return new LibrariesInfo(libraryInfos, module, HIBERNATE_STRING_ID);
  }

  protected List<TemplateInfo> getTemplateInfos(final Module module) {
    List<TemplateInfo> infos = new LinkedList<TemplateInfo>();

    final TemplateSettings settings = TemplateSettings.getInstance();

    final TemplateInfo datasource = new TemplateInfo(module, settings.getTemplateById("datasource"),
                                                     SpringBundle.message("spring.patterns.data.access.data.source"), null);
    final TemplateInfo jndiDatasource = new TemplateInfo(module, settings.getTemplateById("jndi-datasource"),
                                                     SpringBundle.message("spring.patterns.data.access.jndi.data.source"), null);
    jndiDatasource.setAccepted(false);

    final TemplateInfo sessionFactory = new TemplateInfo(module, settings.getTemplateById("hibernatefactory"),
                                                         SpringBundle.message("spring.patterns.data.access.hibernate.session.factory"),
                                                         null);
    final TemplateInfo transctionManager = new TemplateInfo(module, settings.getTemplateById("hibernate-tm"), SpringBundle.message(
      "spring.patterns.data.access.hibernate.transaction.manager"), null);

    infos.add(datasource);
    infos.add(jndiDatasource);
    infos.add(sessionFactory);
    infos.add(transctionManager);

    return infos;
  }

  @Nullable
  protected String getFacetId() {
    return HIBERNATE_STRING_ID;
  }

  protected String getDescription() {
    return SpringBundle.message("spring.patterns.hibernate");
  }

  @Nullable
  protected Image getIcon() {
    return PatternIcons.HIBERNATE_ICON;
  }
}
