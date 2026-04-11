package com.intellij.spring.impl.ide.model.xml;

import com.intellij.spring.impl.ide.model.converters.SpringBeanIdConverter;
import com.intellij.spring.impl.ide.model.xml.beans.Identified;
import consulo.xml.dom.GenericAttributeValue;
import consulo.xml.dom.Referencing;
import consulo.xml.dom.Required;

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
