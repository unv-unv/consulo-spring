/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */

package com.intellij.spring.impl.ide.model.values;

import com.intellij.java.impl.util.xml.converters.values.GenericDomValueConvertersRegistry;
import com.intellij.java.language.psi.PsiType;
import com.intellij.spring.impl.ide.SpringManager;
import com.intellij.spring.impl.ide.model.xml.beans.TypeHolder;
import consulo.language.inject.InjectedLanguageManager;
import consulo.language.psi.PsiLanguageInjectionHost;
import consulo.project.Project;
import consulo.xml.psi.xml.XmlAttribute;
import consulo.xml.psi.xml.XmlElement;
import consulo.xml.util.xml.Converter;
import consulo.xml.util.xml.DomElement;
import consulo.xml.util.xml.GenericDomValue;
import consulo.xml.util.xml.WrappingConverter;

import jakarta.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Dmitry Avdeev
 */
public class PropertyValueConverter extends WrappingConverter {

  @Nonnull
  public List<? extends PsiType> getValueTypes(final GenericDomValue element) {
    if (element instanceof TypeHolder) {
      final List<? extends PsiType> psiTypes = ((TypeHolder)element).getRequiredTypes();
      if (!psiTypes.isEmpty())
      return psiTypes;
    }
    final DomElement parent = element.getParent();
    return parent instanceof TypeHolder ? ((TypeHolder)parent).getRequiredTypes() : Collections.<PsiType>emptyList();
  }

  @Nonnull
  public List<Converter> getConverters(@Nonnull final GenericDomValue element) {
    
    XmlElement xmlElement = element.getXmlElement();
    if (xmlElement instanceof XmlAttribute) {
      PsiLanguageInjectionHost host = (PsiLanguageInjectionHost)((XmlAttribute)xmlElement).getValueElement();
      if (host == null || InjectedLanguageManager.getInstance(xmlElement.getProject()).getInjectedPsiFiles(xmlElement) != null) {
        return Collections.emptyList();
      }
    }
    Project project = element.getManager().getProject();
    final GenericDomValueConvertersRegistry registry = SpringManager.getInstance(project).getValueProvidersRegistry();
    final List<? extends PsiType> types = getValueTypes(element);
    final ArrayList<Converter> list = new ArrayList<Converter>(types.size());
    if (types.isEmpty()) {
      final Converter converter = registry.getConverter(element, null);
      if (converter != null) {
        list.add(converter);
      }
    }
    for (PsiType type : types) {
      final Converter converter = registry.getConverter(element, type);
      if (converter != null) {
        list.add(converter);
      } else {
        return Collections.emptyList();
      }
    }
    return list;
  }

  public Converter getConverter(@Nonnull final GenericDomValue domElement) {
    final List<Converter> converters = getConverters(domElement);
    return converters.isEmpty() ? null : converters.get(0);
  }
}
