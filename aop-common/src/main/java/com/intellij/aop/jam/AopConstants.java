/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */
package com.intellij.aop.jam;

import consulo.aop.icon.AopIconGroup;
import consulo.ui.image.Image;

/**
 * @author peter
 */
public interface AopConstants {
  String ASPECT_ANNO = "org.aspectj.lang.annotation.Aspect";
  String POINTCUT_ANNO = "org.aspectj.lang.annotation.Pointcut";

  String BEFORE_ANNO = "org.aspectj.lang.annotation.Before";
  String AFTER_ANNO = "org.aspectj.lang.annotation.After";
  String AFTER_RETURNING_ANNO = "org.aspectj.lang.annotation.AfterReturning";
  String AFTER_THROWING_ANNO = "org.aspectj.lang.annotation.AfterThrowing";
  String AROUND_ANNO = "org.aspectj.lang.annotation.Around";

  String DECLARE_PARENTS_ANNO = "org.aspectj.lang.annotation.DeclareParents";

  String ARG_NAMES_PARAM = "argNames";
  String RETURNING_PARAM = "returning";
  String THROWING_PARAM = "throwing";
  String POINTCUT_PARAM = "pointcut";

  String DEFAULT_IMPL_PARAM = "defaultImpl";

  String JOIN_POINT = "org.aspectj.lang.JoinPoint";
  String JOIN_POINT_STATIC_PART = "org.aspectj.lang.JoinPoint.StaticPart";
  String PROCEEDING_JOIN_POINT = "org.aspectj.lang.ProceedingJoinPoint";

  Image POINTCUT_ICON = AopIconGroup.pointcut();

  Image FROM_ICON = AopIconGroup.from_arrow();
  Image TO_ICON = AopIconGroup.to_arrow();

  Image ABSTRACT_ADVICE_ICON = AopIconGroup.abstract_advice();
  Image BEFORE_ADVICE_ICON = AopIconGroup.before_advice();
  Image AFTER_ADVICE_ICON = AopIconGroup.after_advice();
  Image AFTER_THROWING_ADVICE_ICON = AopIconGroup.after_throwing_advice();
  Image AFTER_RETURNING_ADVICE_ICON = AopIconGroup.after_returning_advice();
  Image AROUND_ADVICE_ICON = AopIconGroup.around_advice();
}
