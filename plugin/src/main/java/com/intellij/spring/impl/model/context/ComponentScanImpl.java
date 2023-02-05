package com.intellij.spring.impl.model.context;

import com.intellij.spring.impl.model.DomSpringBeanImpl;
import com.intellij.spring.impl.ide.model.xml.context.ComponentScan;
import javax.annotation.Nullable;
import org.jetbrains.annotations.NonNls;

@SuppressWarnings({"AbstractClassNeverImplemented"})
public abstract class ComponentScanImpl extends DomSpringBeanImpl implements ComponentScan {
  @NonNls
  public String getBeanName() {
    return "context:component-scan";
  }

  @Nullable
  public String getClassName() {
    return null;
  }
}
