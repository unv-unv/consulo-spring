package com.intellij.spring.impl.ide.model.actions.patterns;

import consulo.annotation.DeprecationInfo;
import consulo.spring.impl.icon.SpringImplIconGroup;
import consulo.ui.image.Image;

@Deprecated
@DeprecationInfo("Use SpringImplIconGroup")
public interface PatternIcons {
  Image SPRING_BEANS_ICON = SpringImplIconGroup.patternsBeans();

  Image SPRING_PATTERNS_ICON = SpringImplIconGroup.patternsPatterns();

  Image HIBERNATE_ICON = SpringImplIconGroup.patternsHibernate();

  Image DATASOURCE_ICON = SpringImplIconGroup.patternsDatasource();

  Image JDO_ICON = SpringImplIconGroup.patternsJdo();

  Image TOPLINK_ICON = SpringImplIconGroup.patternsToplink();

  Image IBATIS_ICON = SpringImplIconGroup.patternsIbatis();

  Image JPA_ICON = SpringImplIconGroup.patternsJpa();

  Image TRANSACTION_MANAGER_ICON = SpringImplIconGroup.patternsTransactionmanager();

  Image DATA_ACCESS_GROUP_ICON = SpringImplIconGroup.patternsDataaccess();

  Image INTEGRATION_GROUP_ICON = SpringImplIconGroup.patternsIntegration();

  Image FACTORY_BEAN_ICON = SpringImplIconGroup.patternsFactorybean();

  Image SCHEDULER_ICON = SpringImplIconGroup.patternsScheduler();

  Image EJB_ICON = SpringImplIconGroup.patternsEjb();

  Image JDK_ICON = SpringImplIconGroup.patternsJdk();

  Image SPRING_WEBFLOW_ICON = SpringImplIconGroup.springbean();
}
