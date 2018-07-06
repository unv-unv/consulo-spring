package com.intellij.spring.model.xml.beans;

import com.intellij.psi.PsiClass;
import com.intellij.spring.constants.SpringConstants;
import com.intellij.spring.model.xml.SpringModelElement;
import com.intellij.spring.model.xml.SpringQualifier;
import com.intellij.util.xml.GenericAttributeValue;
import com.intellij.util.xml.NameValue;
import com.intellij.util.xml.Namespace;
import com.intellij.util.xml.Convert;
import javax.annotation.Nonnull;

import java.util.List;

/**
 * User: Sergey.Vasiliev
 */
@Namespace(SpringConstants.BEANS_NAMESPACE_KEY)
public interface SpringDomQualifier extends SpringModelElement, SpringQualifier {

  @Nonnull
  @NameValue
  GenericAttributeValue<String> getValue();

  @Convert(SpringQualifierTypeConverter.class)
  @Nonnull
  GenericAttributeValue<PsiClass> getType();

  @Nonnull
  List<SpringDomAttribute> getAttributes();
}