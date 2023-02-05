// Generated on Wed Oct 17 15:28:10 MSD 2007
// DTD/Schema  :    http://www.springframework.org/schema/context

package com.intellij.spring.impl.ide.model.xml.context;

import com.intellij.spring.impl.ide.model.values.converters.ResourceValueConverter;
import com.intellij.spring.impl.ide.model.xml.DomSpringBean;
import consulo.xml.util.xml.GenericAttributeValue;
import consulo.xml.util.xml.Referencing;
import consulo.xml.util.xml.Required;
import javax.annotation.Nonnull;

/**
 * http://www.springframework.org/schema/context:property-placeholderElemType interface.
 */
public interface PropertyPlaceholder extends DomSpringBean, SpringContextElement {


    @Nonnull
    @Required
    @Referencing(value = ResourceValueConverter.class)  
    GenericAttributeValue<String> getLocation();
}
