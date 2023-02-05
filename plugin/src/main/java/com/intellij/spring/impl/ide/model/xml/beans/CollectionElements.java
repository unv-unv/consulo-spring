// Generated on Thu Nov 09 17:15:14 MSK 2006
// DTD/Schema  :    http://www.springframework.org/schema/beans

package com.intellij.spring.impl.ide.model.xml.beans;

import com.intellij.spring.impl.ide.model.values.ListOrSetValueConverter;
import com.intellij.spring.impl.ide.model.xml.CustomBeanWrapper;
import consulo.xml.util.xml.Convert;
import consulo.xml.util.xml.CustomChildren;
import consulo.xml.util.xml.DomElement;
import consulo.xml.util.xml.SubTagList;
import javax.annotation.Nonnull;

import java.util.List;

/**
 * http://www.springframework.org/schema/beans:collectionElements model group interface.
 */
public interface CollectionElements extends DomElement {

  @Nonnull
  List<SpringBean> getBeans();

  SpringBean addBean();

  @CustomChildren
  List<CustomBeanWrapper> getCustomBeans();


  @Nonnull
  List<SpringRef> getRefs();

  SpringRef addRef();


  @Nonnull
  List<Idref> getIdrefs();

  Idref addIdref();


  /**
   * Returns the list of value children.
   * <pre>
   * <h3>Element http://www.springframework.org/schema/beans:value documentation</h3>
   * 	Contains a string representation of a property value.
   * 	The property may be a string, or may be converted to the required
   * 	type using the JavaBeans PropertyEditor machinery. This makes it
   * 	possible for application developers to write custom PropertyEditor
   * 	implementations that can convert strings to arbitrary target objects.
   * 	Note that this is recommended for simple objects only. Configure
   * 	more complex objects by populating JavaBean properties with
   * 	references to other beans.
   * <p/>
   * </pre>
   *
   * @return the list of value children.
   */
  @Convert(ListOrSetValueConverter.class)
  @Nonnull
  List<SpringValue> getValues();

  SpringValue addValue();


  @Nonnull
  SpringNull getNull();


  @Nonnull
  List<ListOrSet> getLists();

  ListOrSet addList();


  @Nonnull
  List<ListOrSet> getSets();

  ListOrSet addSet();


  @Nonnull
  List<SpringMap> getMaps();

  SpringMap addMap();


  @Nonnull
  @SubTagList("props")
  List<Props> getPropses();

  @SubTagList("props")
  Props addProps();


}
