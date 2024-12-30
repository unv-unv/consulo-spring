/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */

package com.intellij.spring.impl.ide;

import com.intellij.java.impl.util.xml.DomJavaUtil;
import com.intellij.java.impl.util.xml.ExtendClassImpl;
import com.intellij.java.language.psi.CommonClassNames;
import com.intellij.java.language.psi.JavaPsiFacade;
import com.intellij.java.language.psi.PsiClass;
import com.intellij.java.language.psi.PsiClassType;
import com.intellij.spring.impl.ide.constants.SpringConstants;
import com.intellij.spring.impl.ide.model.converters.SpringBeanResolveConverter;
import com.intellij.spring.impl.ide.model.xml.CustomBeanWrapper;
import com.intellij.spring.impl.ide.model.xml.beans.MetadataPropertyValueConverter;
import com.intellij.spring.impl.ide.model.xml.beans.MetadataRefValue;
import com.intellij.spring.impl.ide.model.xml.beans.MetadataValue;
import com.intellij.xml.XmlAttributeDescriptor;
import com.intellij.xml.impl.schema.XmlAttributeDescriptorImpl;
import com.intellij.xml.util.XmlUtil;
import consulo.annotation.component.ExtensionImpl;
import consulo.component.bind.ParameterizedTypeImpl;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiReference;
import consulo.xml.psi.xml.XmlAttribute;
import consulo.xml.psi.xml.XmlAttributeValue;
import consulo.xml.psi.xml.XmlFile;
import consulo.xml.psi.xml.XmlTag;
import consulo.xml.util.xml.ConvertContext;
import consulo.xml.util.xml.DomUtil;
import consulo.xml.util.xml.GenericAttributeValue;
import consulo.xml.util.xml.XmlName;
import consulo.xml.util.xml.reflect.DomExtender;
import consulo.xml.util.xml.reflect.DomExtension;
import consulo.xml.util.xml.reflect.DomExtensionsRegistrar;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

/**
 * @author peter
 */
@ExtensionImpl
public class SpringToolDomExtender extends DomExtender<CustomBeanWrapper> {

  @Nullable
  public static XmlTag getToolAnnotationTag(@Nullable PsiElement declaration, boolean allowRecursion) {
    if (declaration instanceof XmlTag) {
      final XmlTag xmlTag = (XmlTag)declaration;
      final XmlTag[] tags = xmlTag.findSubTags("annotation", XmlUtil.XML_SCHEMA_URI);
      if (tags.length > 0) {
        final XmlTag[] tags1 = tags[0].findSubTags("appinfo", XmlUtil.XML_SCHEMA_URI);
        if (tags1.length > 0) {
          final XmlTag[] tags2 = tags1[0].findSubTags("annotation", SpringConstants.TOOL_NAMESPACE);
          if (tags2.length > 0) {
            return tags2[0];
          }
        }
      }
      final XmlAttribute attribute = xmlTag.getAttribute("type");
      if (allowRecursion && attribute != null) {
        final XmlAttributeValue value = attribute.getValueElement();
        if (value != null) {
          for (final PsiReference reference : value.getReferences()) {
            final PsiElement element = reference.resolve();
            if (element instanceof XmlTag) {
              final XmlTag annotationTag = getToolAnnotationTag(element, false);
              if (annotationTag != null) {
                return annotationTag;
              }
            }
          }
        }
      }
    }
    return null;
  }

  @Nonnull
  @Override
  public Class<CustomBeanWrapper> getElementClass() {
    return CustomBeanWrapper.class;
  }

  public void registerExtensions(@Nonnull final CustomBeanWrapper element, @Nonnull final DomExtensionsRegistrar registrar) {
    final XmlTag tag = element.getXmlTag();
    assert tag != null;
    for (final XmlAttribute attribute : tag.getAttributes()) {
      final XmlAttributeDescriptor descriptor = attribute.getDescriptor();
      if (descriptor instanceof XmlAttributeDescriptorImpl) {
        final XmlTag annotationTag = getToolAnnotationTag(descriptor.getDeclaration(), true);
        if (annotationTag != null) {
          boolean ref = "ref".equals(annotationTag.getAttributeValue("kind"));
          final PsiClass expectedTypeClass = getExpectedTypeClass(element, annotationTag);
          if (expectedTypeClass != null) {
            final PsiClassType
              expectedType = JavaPsiFacade.getInstance(expectedTypeClass.getProject()).getElementFactory().createType(expectedTypeClass);
            final XmlName xmlName = new XmlName(attribute.getName());
            if (ref) {
              registrar.registerAttributeChildExtension(xmlName, MetadataRefValue.class).setConverter(new SpringBeanResolveConverter() {
                @Nullable
                public List<PsiClassType> getRequiredClasses(final ConvertContext context) {
                  return Arrays.asList(expectedType);
                }
              });
            }
            else {
              if (CommonClassNames.JAVA_LANG_CLASS.equals(expectedType.getCanonicalText())) {
                final DomExtension extension = registrar.registerAttributeChildExtension(xmlName, new ParameterizedTypeImpl
                  (GenericAttributeValue.class, PsiClass.class));
                final XmlTag[] tags1 = annotationTag.findSubTags("assignable-to", SpringConstants.TOOL_NAMESPACE);
                if (tags1.length > 0) {
                  final String assignableFrom = tags1[0].getAttributeValue("type");
                  if (assignableFrom != null) {
                    extension.addCustomAnnotation(new ExtendClassImpl() {
                      public String value() {
                        return assignableFrom;
                      }
                    });
                  }
                }
              }
              else {
                registrar.registerAttributeChildExtension(xmlName, MetadataValue.class)
                         .setConverter(new MetadataPropertyValueConverter(expectedType));
              }
            }
          }
        }
      }
    }
  }

  private static PsiClass getExpectedTypeClass(final CustomBeanWrapper element, final XmlTag annotationTag) {
    final XmlFile file = DomUtil.getFile(element);
    final XmlTag[] expectedTypeTags = annotationTag.findSubTags("expected-type", SpringConstants.TOOL_NAMESPACE);
    final String value = expectedTypeTags.length > 0 ? expectedTypeTags[0].getAttributeValue("type") : null;
    return DomJavaUtil.findClass(value != null ? value : CommonClassNames.JAVA_LANG_OBJECT, file, element.getModule(), null);
  }


}