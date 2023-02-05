package com.intellij.spring.impl.ide.model.xml.beans;

import com.intellij.java.language.psi.PsiClass;
import com.intellij.spring.impl.ide.constants.SpringConstants;
import com.intellij.spring.impl.ide.model.xml.SpringModelElement;
import com.intellij.spring.impl.ide.model.xml.SpringQualifier;
import consulo.xml.util.xml.Convert;
import consulo.xml.util.xml.GenericAttributeValue;
import consulo.xml.util.xml.NameValue;
import consulo.xml.util.xml.Namespace;

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