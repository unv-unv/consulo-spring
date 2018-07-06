package com.intellij.spring.model.xml;

import com.intellij.spring.model.converters.SpringBeanIdConverter;
import com.intellij.spring.model.xml.beans.Identified;
import com.intellij.util.xml.GenericAttributeValue;
import com.intellij.util.xml.Referencing;
import com.intellij.util.xml.Required;
import javax.annotation.Nonnull;

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
