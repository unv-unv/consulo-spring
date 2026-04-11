package com.intellij.spring.impl.ide.model.xml.beans;

import com.intellij.java.language.psi.PsiClass;
import com.intellij.spring.impl.ide.constants.SpringConstants;
import com.intellij.spring.impl.ide.model.xml.SpringModelElement;
import com.intellij.spring.impl.ide.model.xml.SpringQualifier;
import consulo.xml.dom.Convert;
import consulo.xml.dom.GenericAttributeValue;
import consulo.xml.dom.NameValue;
import consulo.xml.dom.Namespace;

import jakarta.annotation.Nonnull;
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