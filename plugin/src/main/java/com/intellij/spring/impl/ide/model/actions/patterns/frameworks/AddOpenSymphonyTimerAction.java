package com.intellij.spring.impl.ide.model.actions.patterns.frameworks;

import com.intellij.spring.impl.ide.SpringBundle;
import com.intellij.spring.impl.ide.model.actions.patterns.PatternIcons;
import com.intellij.spring.impl.ide.model.actions.patterns.frameworks.ui.LibrariesInfo;
import com.intellij.spring.impl.ide.model.actions.patterns.frameworks.ui.TemplateInfo;
import com.intellij.spring.impl.ide.model.actions.patterns.frameworks.util.LibrariesConfigurationManager;
import consulo.java.ex.facet.LibraryInfo;
import consulo.language.editor.template.TemplateSettings;
import consulo.module.Module;
import consulo.ui.image.Image;
import org.jetbrains.annotations.NonNls;

import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.List;

public class AddOpenSymphonyTimerAction extends AbstractFrameworkIntegrationAction {
  @NonNls private static final String QUARTZ_ID = "quartz";

  protected String[] getBeansClassNames() {
    return new String[]{"org.springframework.scheduling.quartz.JobDetailBean"};
  }

   protected LibrariesInfo getLibrariesInfo(final Module module) {
    final LibraryInfo[] libraryInfos = LibrariesConfigurationManager.getInstance(module.getProject()).getLibraryInfos("quartz");

    return new LibrariesInfo(libraryInfos, module, QUARTZ_ID);
  }

  protected List<TemplateInfo> getTemplateInfos(final consulo.module.Module module) {
    List<TemplateInfo> infos = new LinkedList<TemplateInfo>();

    final TemplateSettings settings = TemplateSettings.getInstance();

    final TemplateInfo job = new TemplateInfo(module, settings.getTemplateById("quartz-job-detail"),
                                                     SpringBundle.message("spring.patterns.integration.opensymphony.job.detail.bean"), null);

    final TemplateInfo simpleTrigger = new TemplateInfo(module, settings.getTemplateById("quartz-simple-trigger"),
                                                     SpringBundle.message("spring.patterns.integration.opensymphony.simple.trigger"), null);

    final TemplateInfo cronTrigger = new TemplateInfo(module, settings.getTemplateById("quartz-cron-trigger"),
                                                     SpringBundle.message("spring.patterns.integration.opensymphony.cron.trigger"), null, false);

    final TemplateInfo sf = new TemplateInfo(module, settings.getTemplateById("quartz-scheduler-factory"),
                                                         SpringBundle.message("spring.patterns.integration.opensymphony.scheduler"),
                                                         null);

    infos.add(job);
    infos.add(simpleTrigger);
    infos.add(cronTrigger);
    infos.add(sf);

    return infos;
  }

  protected String getDescription() {
    return SpringBundle.message("spring.patterns.quartz.scheduler");
  }

  @Nullable
  protected Image getIcon() {
    return PatternIcons.SCHEDULER_ICON;
  }
}
