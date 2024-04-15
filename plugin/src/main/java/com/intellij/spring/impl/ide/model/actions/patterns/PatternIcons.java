package com.intellij.spring.impl.ide.model.actions.patterns;

import consulo.annotation.DeprecationInfo;
import consulo.spring.impl.icon.SpringImplIconGroup;
import consulo.ui.image.Image;

@Deprecated
@DeprecationInfo("Use SpringImplIconGroup")
public interface PatternIcons {
  Image SPRING_BEANS_ICON = Image.empty();

  Image SPRING_PATTERNS_ICON = Image.empty();

  Image HIBERNATE_ICON = Image.empty();

  Image DATASOURCE_ICON = Image.empty();

  Image JDO_ICON = Image.empty();

  Image TOPLINK_ICON = Image.empty();

  Image IBATIS_ICON = Image.empty();

  Image JPA_ICON = Image.empty();

  Image TRANSACTION_MANAGER_ICON = Image.empty();

  Image DATA_ACCESS_GROUP_ICON = Image.empty();

  Image INTEGRATION_GROUP_ICON = Image.empty();

  Image FACTORY_BEAN_ICON = Image.empty();

  Image SCHEDULER_ICON = Image.empty();

  Image EJB_ICON = Image.empty();

  Image JDK_ICON = Image.empty();

  Image SPRING_WEBFLOW_ICON = SpringImplIconGroup.springbean();
}
