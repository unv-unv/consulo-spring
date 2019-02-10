/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */
package com.intellij.spring;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.jetbrains.annotations.NonNls;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiClassType;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.meta.PsiPresentableMetaData;
import com.intellij.psi.util.PropertyUtil;
import com.intellij.psi.xml.XmlDocument;
import com.intellij.psi.xml.XmlElement;
import com.intellij.psi.xml.XmlTag;
import com.intellij.spring.constants.SpringConstants;
import com.intellij.spring.model.xml.beans.SpringBean;
import com.intellij.util.ArrayUtil;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.DomManager;
import com.intellij.xml.XmlAttributeDescriptor;
import com.intellij.xml.XmlElementDescriptor;
import com.intellij.xml.impl.XmlAttributeDescriptorEx;
import com.intellij.xml.impl.schema.XmlNSDescriptorImpl;
import consulo.ui.image.Image;

/**
 * @author peter
 */
public class PNamespaceDescriptor extends XmlNSDescriptorImpl {

  public XmlAttributeDescriptor getAttribute(final String localName, final String namespace, final XmlTag context) {
    // TODO: this is not efficient!
    if (SpringConstants.P_NAMESPACE.equals(namespace)) {
      for(XmlAttributeDescriptor a:getAttributeDescriptors(context)) {
        if (a.getName().equals(localName)) return a;
      }
    }
    return super.getAttribute(localName, namespace, context);
  }

  @Nullable
  public static PsiClass getClass(@Nonnull final XmlTag tag) {
    final DomElement element = DomManager.getDomManager(tag.getProject()).getDomElement(tag);

    if (element instanceof SpringBean) {
      final SpringBean bean = (SpringBean)element;
      return bean.getBeanClass();
    }
    return null;
  }

  private static XmlAttributeDescriptor[] getAttributeDescriptors(@Nonnull final XmlTag tag) {
    final PsiClass psiClass = getClass(tag);
    if (psiClass == null) {
      return XmlAttributeDescriptor.EMPTY;
    }
    final List<XmlAttributeDescriptor> result = new ArrayList<XmlAttributeDescriptor>();
    final Map<String,PsiMethod> properties = PropertyUtil.getAllProperties(psiClass, true, false);

    for (final String propertyName : properties.keySet()) {
      final PsiMethod method = properties.get(propertyName);
      result.add(new PAttributeDescriptor(propertyName, "", method));

      if (method.getParameterList().getParameters()[0].getType() instanceof PsiClassType) {
        result.add(new PAttributeDescriptor(propertyName, "-ref", method));
      }
    }
    return result.toArray(new XmlAttributeDescriptor[result.size()]);
  }

  @Nonnull
  public XmlElementDescriptor[] getRootElementsDescriptors(@Nullable final XmlDocument doc) {
    return XmlElementDescriptor.EMPTY_ARRAY;
  }

  public XmlAttributeDescriptor[] getRootAttributeDescriptors(final XmlTag context) {
    return getAttributeDescriptors(context);
  }


  private static class PAttributeDescriptor implements XmlAttributeDescriptorEx, PsiPresentableMetaData {
    private final String myPropertyName;
    private final String mySuffix;
    private final PsiMethod myMethod;

    public PAttributeDescriptor(@NonNls final String propertyName, @NonNls String suffix, final PsiMethod method) {
      myPropertyName = propertyName;
      mySuffix = suffix;
      myMethod = method;
    }

    public String getName() {
      return myPropertyName + mySuffix;
    }

    public void init(PsiElement element) {
      throw new UnsupportedOperationException("Method init is not yet implemented in " + getClass().getName());
    }

    public Object[] getDependences() {
      return ArrayUtil.EMPTY_OBJECT_ARRAY;
    }

    public PsiElement getDeclaration() {
      return myMethod;
    }

    @NonNls
    public String getName(PsiElement context) {
      String name = getName();
      final String prefix = ((XmlTag)context).getPrefixByNamespace(SpringConstants.P_NAMESPACE);
      name = (StringUtil.isNotEmpty(prefix) ? prefix + ":" : "") + name;
      return name;
    }

    public boolean isRequired() {
      return false;
    }

    public boolean isFixed() {
      return false;
    }

    public boolean hasIdType() {
      return false;
    }

    public boolean hasIdRefType() {
      return false;
    }

    @Nullable
    public String getDefaultValue() {
      return null;
    }

    public boolean isEnumerated() {
      return false;
    }

    public String[] getEnumeratedValues() {
      return ArrayUtil.EMPTY_STRING_ARRAY;
    }

    @Nullable
    public String validateValue(XmlElement context, String value) {
      return null;
    }

    public String getTypeName() {
      throw new UnsupportedOperationException("Method getTypeName is not yet implemented in " + getClass().getName());
    }

    @Nullable
    public Image getIcon() {
      return SpringIcons.SPRING_BEAN_PROPERTY_ICON;
    }

    @NonNls
    public String handleTargetRename(@Nonnull @NonNls final String newTargetName) {
      final String propertyName = PropertyUtil.getPropertyName(newTargetName);
      return propertyName == null ? null : propertyName + mySuffix;

    }
  }
}
