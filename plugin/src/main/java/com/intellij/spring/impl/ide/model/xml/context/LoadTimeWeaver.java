// Generated on Wed Oct 17 15:28:10 MSD 2007
// DTD/Schema  :    http://www.springframework.org/schema/context

package com.intellij.spring.impl.ide.model.xml.context;

import com.intellij.spring.impl.ide.model.xml.DomSpringBean;
import consulo.xml.util.xml.GenericAttributeValue;
import jakarta.annotation.Nonnull;

/**
 * http://www.springframework.org/schema/context:load-time-weaverElemType interface.
 */
public interface LoadTimeWeaver extends DomSpringBean, SpringContextElement {

  /**
   * Returns the value of the weaver-class child.
   * <pre>
   * <h3>Attribute null:weaver-class documentation</h3>
   * 	The fully-qualified classname of the LoadTimeWeaver that is to be activated.
   * <p/>
   * </pre>
   *
   * @return the value of the weaver-class child.
   */
  @Nonnull
  GenericAttributeValue<String> getWeaverClass();


  /**
   * Returns the value of the aspectj-weaving child.
   *
   * @return the value of the aspectj-weaving child.
   */
  @Nonnull
  GenericAttributeValue<AspectjWeaving> getAspectjWeaving();


}
