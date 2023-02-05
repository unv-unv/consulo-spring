/*
 * Copyright (c) 2000-2005 by JetBrains s.r.o. All Rights Reserved.
 * Use is subject to license terms.
 */
package com.intellij.aop.jam;

import com.intellij.jam.JamStringAttributeElement;
import com.intellij.java.language.psi.PsiAnnotation;

/**
 * @author peter
 */
public interface PointcutContainer {

  PsiAnnotation getAnnotation();

  JamStringAttributeElement<String> getArgNames();
}
