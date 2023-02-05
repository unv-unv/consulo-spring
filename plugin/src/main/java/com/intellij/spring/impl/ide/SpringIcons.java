/*
 * Copyright (c) 2000-2006 JetBrains s.r.o. All Rights Reserved.
 */

package com.intellij.spring.impl.ide;

import consulo.ui.image.Image;

@Deprecated
public interface SpringIcons {
  Image SPRING_ICON = consulo.spring.impl.SpringIcons.Spring;

  Image SPRING_BEAN_ICON = consulo.spring.impl.SpringIcons.SpringBean;
  Image SPRING_JAVA_BEAN_ICON = consulo.spring.impl.SpringIcons.SpringJavaBean;
  Image SPRING_ALIAS_ICON = consulo.spring.impl.SpringIcons.Spring;
  Image SPRING_BEANS_ICON = consulo.spring.impl.SpringIcons.Beans;
  Image SPRING_BEAN_SCOPE_ICON = consulo.spring.impl.SpringIcons.SpringBeanScope;
  Image SPRING_BEAN_PROPERTY_ICON = consulo.spring.impl.SpringIcons.SpringProperty;

  Image PARENT_GUTTER = consulo.spring.impl.SpringIcons.ParentBeanGutter;
  Image CHILD_GUTTER = consulo.spring.impl.SpringIcons.ChildBeanGutter;

  Image CONFIG_FILE = consulo.spring.impl.SpringIcons.SpringConfig;
  Image JAVA_CONFIG_FILE = consulo.spring.impl.SpringIcons.SpringJavaConfig;

  Image FILESET = consulo.spring.impl.SpringIcons.FileSet;
  Image DEPENDENCY = consulo.spring.impl.SpringIcons.Dependency;

  Image SPRING_BEAN_INSTANTIATED_BY_FACORY = consulo.spring.impl.SpringIcons.FactoryMethodBean;
  Image SPRING_BEAN_INSTANTIATED_BY_FACORY_METHOD = consulo.spring.impl.SpringIcons.FactoryMethodBean;
  Image SPRING_DEPENDENCIES_GRAPH_GROUP_BEANS = consulo.spring.impl.SpringIcons.GroupBeans;
  Image SPRING_DEPENDENCIES_GRAPH_SHOW_AUTOWIRED = consulo.spring.impl.SpringIcons.ShowAutowiredDependencies;
}