/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */
package com.intellij.aop.jam;

import org.jetbrains.annotations.NonNls;
import com.intellij.openapi.util.IconLoader;
import consulo.ui.image.Image;

/**
 * @author peter
 */
public interface AopConstants
{
	String ASPECT_ANNO = "org.aspectj.lang.annotation.Aspect";
	String POINTCUT_ANNO = "org.aspectj.lang.annotation.Pointcut";

	String BEFORE_ANNO = "org.aspectj.lang.annotation.Before";
	String AFTER_ANNO = "org.aspectj.lang.annotation.After";
	String AFTER_RETURNING_ANNO = "org.aspectj.lang.annotation.AfterReturning";
	String AFTER_THROWING_ANNO = "org.aspectj.lang.annotation.AfterThrowing";
	String AROUND_ANNO = "org.aspectj.lang.annotation.Around";

	String DECLARE_PARENTS_ANNO = "org.aspectj.lang.annotation.DeclareParents";

	@NonNls
	String ARG_NAMES_PARAM = "argNames";
	@NonNls
	String RETURNING_PARAM = "returning";
	@NonNls
	String THROWING_PARAM = "throwing";
	@NonNls
	String POINTCUT_PARAM = "pointcut";

	@NonNls
	String DEFAULT_IMPL_PARAM = "defaultImpl";

	String JOIN_POINT = "org.aspectj.lang.JoinPoint";
	String JOIN_POINT_STATIC_PART = "org.aspectj.lang.JoinPoint.StaticPart";
	String PROCEEDING_JOIN_POINT = "org.aspectj.lang.ProceedingJoinPoint";

	Image POINTCUT_ICON = IconLoader.getIcon("/icons/pointcut.png");
	Image TO_POINTCUT_ICON = IconLoader.getIcon("/icons/to_pointcut.png");
	Image FROM_POINTCUT_ICON = IconLoader.getIcon("/icons/from_pointcut.png");

	Image FROM_ICON = IconLoader.getIcon("/icons/from_arrow.png");
	Image TO_ICON = IconLoader.getIcon("/icons/to_arrow.png");

	Image ABSTRACT_ADVICE_ICON = IconLoader.getIcon("/icons/abstract_advice.png");
	Image BEFORE_ADVICE_ICON = IconLoader.getIcon("/icons/before_advice.png");
	Image AFTER_ADVICE_ICON = IconLoader.getIcon("/icons/after_advice.png");
	Image AFTER_THROWING_ADVICE_ICON = IconLoader.getIcon("/icons/after_throwing_advice.png");
	Image AFTER_RETURNING_ADVICE_ICON = IconLoader.getIcon("/icons/after_returning_advice.png");
	Image AROUND_ADVICE_ICON = IconLoader.getIcon("/icons/around_advice.png");
	Image INTRODUCTION_ICON = IconLoader.getIcon("/icons/introduction.png");
}
