/*
 * Copyright (c) 2000-2006 JetBrains s.r.o. All Rights Reserved.
 */

package com.intellij.spring;

import consulo.ui.image.Image;

@Deprecated
public interface SpringIcons
{
	Image SPRING_ICON = consulo.spring.SpringIcons.Spring;

	Image SPRING_BEAN_ICON = consulo.spring.SpringIcons.SpringBean;
	Image SPRING_JAVA_BEAN_ICON = consulo.spring.SpringIcons.SpringJavaBean;
	Image SPRING_ALIAS_ICON = consulo.spring.SpringIcons.Spring;
	Image SPRING_BEANS_ICON = consulo.spring.SpringIcons.Beans;
	Image SPRING_BEAN_SCOPE_ICON = consulo.spring.SpringIcons.SpringBeanScope;
	Image SPRING_BEAN_PROPERTY_ICON = consulo.spring.SpringIcons.SpringProperty;

	Image PARENT_GUTTER = consulo.spring.SpringIcons.ParentBeanGutter;
	Image CHILD_GUTTER = consulo.spring.SpringIcons.ChildBeanGutter;

	Image CONFIG_FILE = consulo.spring.SpringIcons.SpringConfig;
	Image JAVA_CONFIG_FILE = consulo.spring.SpringIcons.SpringJavaConfig;

	Image FILESET = consulo.spring.SpringIcons.FileSet;
	Image DEPENDENCY = consulo.spring.SpringIcons.Dependency;

	Image SPRING_BEAN_INSTANTIATED_BY_FACORY = consulo.spring.SpringIcons.FactoryMethodBean;
	Image SPRING_BEAN_INSTANTIATED_BY_FACORY_METHOD = consulo.spring.SpringIcons.FactoryMethodBean;
	Image SPRING_DEPENDENCIES_GRAPH_GROUP_BEANS = consulo.spring.SpringIcons.GroupBeans;
	Image SPRING_DEPENDENCIES_GRAPH_SHOW_AUTOWIRED = consulo.spring.SpringIcons.ShowAutowiredDependencies;
}