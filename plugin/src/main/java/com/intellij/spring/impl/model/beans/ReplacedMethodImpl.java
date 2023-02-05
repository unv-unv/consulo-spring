package com.intellij.spring.impl.model.beans;

import com.intellij.spring.impl.ide.model.xml.beans.ReplacedMethod;
import consulo.util.lang.ComparatorUtil;

/**
 * @author Dmitry Avdeev
 */
@SuppressWarnings({"AbstractClassNeverImplemented"})
public abstract class ReplacedMethodImpl implements ReplacedMethod {

  public int hashCode() {
    final String value = getName().getStringValue();
    return value == null ? 0 : value.hashCode();
  }

  public boolean equals(final Object obj) {
    return obj instanceof ReplacedMethod &&
           ComparatorUtil.equalsNullable(getName().getStringValue(), ((ReplacedMethod)obj).getName().getStringValue());
  }
}
