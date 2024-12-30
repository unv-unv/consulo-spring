/*
 * Copyright (c) 2000-2006 JetBrains s.r.o. All Rights Reserved.
 */

package com.intellij.spring.impl.ide.model.converters;

import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.language.psi.PsiReference;
import consulo.xml.util.xml.*;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public class SpringResourceConverter extends Converter<PsiFile> implements CustomReferenceConverter<PsiFile> {

  public PsiFile fromString(final @Nullable String s, final ConvertContext context) {
    if (s != null) {
      final GenericAttributeValue<PsiFile> element = (GenericAttributeValue<PsiFile>)context.getInvocationElement();
      final PsiReference[] references = createReferences(element, element.getXmlAttributeValue(), context);
      if (references.length > 0) {
        PsiElement result = references[references.length - 1].resolve();
        if (result instanceof PsiFile) {
          return (PsiFile)result;
        }
      }
    }
    return null;
  }

  public String toString(final @Nullable PsiFile psiFile, final ConvertContext context) {
      return null;
  }

  @Nonnull
  public PsiReference[] createReferences(final GenericDomValue<PsiFile> genericDomValue, final PsiElement element, final ConvertContext context) {
    final String s = genericDomValue.getStringValue();
    if (s == null || element == null) {
      return PsiReference.EMPTY_ARRAY;
    }

    return ResourceResolverUtils.getReferences(element, s, s.startsWith("/"), false);
  }
}
