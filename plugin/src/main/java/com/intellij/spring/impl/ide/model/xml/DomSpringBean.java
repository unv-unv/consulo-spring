package com.intellij.spring.impl.ide.model.xml;

import com.intellij.spring.impl.ide.model.converters.SpringBeanIdConverter;
import com.intellij.spring.impl.ide.model.xml.beans.Identified;
import consulo.xml.util.xml.GenericAttributeValue;
import consulo.xml.util.xml.Referencing;
import consulo.xml.util.xml.Required;

import jakarta.annotation.Nonnull;

public interface DomSpringBean extends CommonSpringBean, Identified {
  DomSpringBean[] EMPTY_ARRAY = new DomSpringBean[0];

  /**
   * Returns the value of the id child.
   *
   * @return the value of the id child.
   */

  @Referencing(value = SpringBeanIdConverter.class, soft = true)
  @Nonnull
  @Required(value = false, nonEmpty = true)
  GenericAttributeValue<String> getId();

  void setName(@Nonnull String newName);
}
