/*
 * Copyright (c) 2000-2006 JetBrains s.r.o. All Rights Reserved.
 */

package com.intellij.spring;

import consulo.ui.image.Image;

import javax.swing.*;

@Deprecated
public interface SpringIcons {
  Icon SPRING_ICON = consulo.spring.SpringIcons.Spring;
  
  Icon SPRING_BEAN_ICON = consulo.spring.SpringIcons.SpringBean;
  Icon SPRING_JAVA_BEAN_ICON = consulo.spring.SpringIcons.SpringJavaBean;
  Icon SPRING_ALIAS_ICON = consulo.spring.SpringIcons.Spring;
  Icon SPRING_BEANS_ICON = consulo.spring.SpringIcons.Beans;
  Icon SPRING_BEAN_SCOPE_ICON = consulo.spring.SpringIcons.SpringBeanScope;
  Icon SPRING_BEAN_PROPERTY_ICON = consulo.spring.SpringIcons.SpringProperty;

  Icon PARENT_GUTTER = consulo.spring.SpringIcons.ParentBeanGutter;
  Icon CHILD_GUTTER = consulo.spring.SpringIcons.ChildBeanGutter;

  Image CONFIG_FILE = consulo.spring.SpringIcons.SpringConfig;
  Icon JAVA_CONFIG_FILE = consulo.spring.SpringIcons.SpringJavaConfig;

  Image FILESET = consulo.spring.SpringIcons.FileSet;
  Icon DEPENDENCY = consulo.spring.SpringIcons.Dependency;

  Icon SPRING_BEAN_INSTANTIATED_BY_FACORY = consulo.spring.SpringIcons.FactoryMethodBean;
  Icon SPRING_BEAN_INSTANTIATED_BY_FACORY_METHOD = consulo.spring.SpringIcons.FactoryMethodBean;
  Icon SPRING_DEPENDENCIES_GRAPH_GROUP_BEANS = consulo.spring.SpringIcons.GroupBeans;
  Icon SPRING_DEPENDENCIES_GRAPH_SHOW_AUTOWIRED = consulo.spring.SpringIcons.ShowAutowiredDependencies;
}