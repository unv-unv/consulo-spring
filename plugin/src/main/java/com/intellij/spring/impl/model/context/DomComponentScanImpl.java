package com.intellij.spring.impl.model.context;

import com.intellij.java.language.psi.PsiJavaPackage;
import com.intellij.spring.impl.ide.model.xml.context.DomComponentScan;
import com.intellij.spring.impl.model.DomSpringBeanImpl;

import javax.annotation.Nullable;
import java.util.Collection;

@SuppressWarnings({"AbstractClassNeverImplemented"})
public abstract class DomComponentScanImpl extends DomSpringBeanImpl implements DomComponentScan {
  @Override
  public String getBeanName() {
    return "context:component-scan";
  }

  @Override
  public Collection<PsiJavaPackage> getBasePackages() {
    return getBasePackage().getValue();
  }

  @Override
  @Nullable
  public String getClassName() {
    return null;
  }
}
