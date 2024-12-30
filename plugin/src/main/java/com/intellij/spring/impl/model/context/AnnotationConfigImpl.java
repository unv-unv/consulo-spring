package com.intellij.spring.impl.model.context;

import com.intellij.spring.impl.model.DomSpringBeanImpl;
import com.intellij.spring.impl.ide.model.xml.context.AnnotationConfig;
import consulo.xml.psi.xml.XmlTag;
import org.jetbrains.annotations.NonNls;

import jakarta.annotation.Nullable;

/**
 * User: Sergey.Vasiliev
 */
@SuppressWarnings({"AbstractClassNeverImplemented"})
public abstract class AnnotationConfigImpl extends DomSpringBeanImpl implements AnnotationConfig {
  @NonNls
  public String getBeanName() {
    final XmlTag tag = getXmlTag();
    return tag == null ? "context:annotation-config" : tag.getName();
  }

  @Nullable
  public String getClassName() {
    return null;
  }
}