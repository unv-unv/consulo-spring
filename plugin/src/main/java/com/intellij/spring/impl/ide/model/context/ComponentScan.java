package com.intellij.spring.impl.ide.model.context;

import com.intellij.java.language.psi.PsiJavaPackage;
import com.intellij.spring.impl.ide.model.xml.CommonSpringBean;

import java.util.Collection;

/**
 * @author VISTALL
 * @since 2024-04-13
 */
public interface ComponentScan extends CommonSpringBean {
  Collection<PsiJavaPackage> getBasePackages();
}
