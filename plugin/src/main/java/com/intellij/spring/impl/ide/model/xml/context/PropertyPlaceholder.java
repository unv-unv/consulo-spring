// Generated on Wed Oct 17 15:28:10 MSD 2007
// DTD/Schema  :    http://www.springframework.org/schema/context

package com.intellij.spring.impl.ide.model.xml.context;

import com.intellij.spring.impl.ide.model.values.converters.ResourceValueConverter;
import com.intellij.spring.impl.ide.model.xml.DomSpringBean;
import consulo.xml.dom.GenericAttributeValue;
import consulo.xml.dom.Referencing;
import consulo.xml.dom.Required;
import jakarta.annotation.Nonnull;

/**
 * http://www.springframework.org/schema/context:property-placeholderElemType interface.
 */
public interface PropertyPlaceholder extends DomSpringBean, SpringContextElement {


    @Nonnull
    @Required
    @Referencing(value = ResourceValueConverter.class)  
    GenericAttributeValue<String> getLocation();
}
