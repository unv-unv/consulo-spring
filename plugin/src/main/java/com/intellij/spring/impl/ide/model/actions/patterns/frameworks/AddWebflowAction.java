package com.intellij.spring.impl.ide.model.actions.patterns.frameworks;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.Nullable;

import consulo.language.editor.template.TemplateSettings;
import org.jetbrains.annotations.NonNls;
import consulo.java.ex.facet.LibraryInfo;
import consulo.module.Module;
import com.intellij.spring.impl.ide.SpringBundle;
import com.intellij.spring.impl.ide.SpringIcons;
import com.intellij.spring.impl.ide.model.actions.patterns.frameworks.ui.LibrariesInfo;
import com.intellij.spring.impl.ide.model.actions.patterns.frameworks.ui.TemplateInfo;
import com.intellij.spring.impl.ide.model.actions.patterns.frameworks.util.LibrariesConfigurationManager;
import consulo.ui.image.Image;

public class AddWebflowAction extends AbstractFrameworkIntegrationAction {
  @NonNls private static final String WEBFLOW_STRING_ID = "webflow";

  @NonNls
  protected String[] getBeansClassNames() {
    return new String[]{"org.springframework.webflow.definition.registry.FlowDefinitionRegistry"};
  }

  protected LibrariesInfo getLibrariesInfo(final Module module) {
    final LibraryInfo[] libraryInfos = LibrariesConfigurationManager.getInstance(module.getProject()).getLibraryInfos(WEBFLOW_STRING_ID);

    return new LibrariesInfo(libraryInfos, module, WEBFLOW_STRING_ID);
  }

  protected List<TemplateInfo> getTemplateInfos(final Module module) {
    List<TemplateInfo> infos = new LinkedList<TemplateInfo>();

    final TemplateSettings settings = TemplateSettings.getInstance();

    final TemplateInfo flowRegistry = new TemplateInfo(module, settings.getTemplateById("flow-registry"),
                                                     SpringBundle.message("spring.patterns.webflow.registry"), null);

    final TemplateInfo flowExecutor = new TemplateInfo(module, settings.getTemplateById("flow-executor"),
                                                     SpringBundle.message("spring.patterns.webflow.executor"), null);

    final TemplateInfo flowBuilderServices = new TemplateInfo(module, settings.getTemplateById("flow-builder-serices"),
                                                     SpringBundle.message("spring.patterns.webflow.builder.services"), null);


    final TemplateInfo conversationService = new TemplateInfo(module, settings.getTemplateById("conversation-service"),
                                                         SpringBundle.message("spring.patterns.webflow.services.conversion.service"), null, false);

    final TemplateInfo expresiionParser = new TemplateInfo(module, settings.getTemplateById("expression-parser"),
                                                         SpringBundle.message("spring.patterns.webflow.services.expression.parser"), null, false);

    final TemplateInfo factoryCreator = new TemplateInfo(module, settings.getTemplateById("factory-creator"),
                                                         SpringBundle.message("spring.patterns.webflow.services.view.factory.creator"), null, false);

    final TemplateInfo formatterRegistry = new TemplateInfo(module, settings.getTemplateById("formatter-registry"),
                                                         SpringBundle.message("spring.patterns.webflow.services.view.formatter.registry"), null, false);

    final TemplateInfo exeListener = new TemplateInfo(module, settings.getTemplateById("flow-execution-listener"),
                                                         SpringBundle.message("spring.patterns.webflow.execution.listener"), null, false);


    infos.add(flowBuilderServices);
    infos.add(flowRegistry);
    infos.add(exeListener);
    infos.add(flowExecutor);

    infos.add(conversationService);
    infos.add(expresiionParser);
    infos.add(factoryCreator);

    return infos;
  }

  protected String getDescription() {
    return SpringBundle.message("spring.patterns.webflow.group.name");
  }

  @Nullable
  protected Image getIcon() {
    return SpringIcons.SPRING_ICON;
  }
}