package com.intellij.spring.impl.ide.metadata;

import com.intellij.spring.impl.ide.model.xml.DomSpringBean;
import com.intellij.spring.impl.ide.model.xml.beans.SpringBean;
import com.intellij.spring.impl.ide.SpringBundle;
import consulo.xml.util.xml.DomMetaData;
import consulo.xml.util.xml.GenericDomValue;
import consulo.xml.util.xml.GenericAttributeValue;
import consulo.xml.util.xml.DomUtil;
import consulo.language.util.IncorrectOperationException;

import javax.annotation.Nullable;

import java.util.List;

/**
 * User: Sergey.Vasiliev
*/
public class SpringBeanMetaData extends DomMetaData<DomSpringBean> {

  @Nullable
  protected GenericDomValue getNameElement(final DomSpringBean element) {
    final GenericAttributeValue<String> id = element.getId();
    if (DomUtil.hasXml(id)) {
      return id;
    }
    if (element instanceof SpringBean) {
      final GenericAttributeValue<List<String>> name = ((SpringBean)element).getName();
      if (DomUtil.hasXml(name)) {
        return name;
      }
    }
    return null;
  }

  public void setName(final String name) throws IncorrectOperationException
  {
    getElement().setName(name);
  }

  public String getTypeName() {
    return SpringBundle.message("spring.bean");
  }
}
