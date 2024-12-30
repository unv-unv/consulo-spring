package com.intellij.spring.impl.ide.model.converters;

import consulo.language.psi.PsiFile;
import consulo.language.psi.PsiReference;
import consulo.language.psi.PsiElement;
import consulo.xml.util.xml.*;
import com.intellij.spring.impl.ide.model.values.PlaceholderUtils;
import jakarta.annotation.Nullable;
import jakarta.annotation.Nonnull;

public class SpringImportResourceConverter extends Converter<PsiFile> implements CustomReferenceConverter {

  public PsiFile fromString(final @Nullable String s, final ConvertContext context) {
    if (s != null) {
      final GenericAttributeValue<PsiFile> element = (GenericAttributeValue<PsiFile>)context.getInvocationElement();

      if (!s.contains(PlaceholderUtils.DEFAULT_PLACEHOLDER_PREFIX)) {
        final PsiReference[] references = createReferences(element, element.getXmlAttributeValue(), context);
        if (references.length > 0) {
          PsiElement result = references[references.length - 1].resolve();
          if (result instanceof PsiFile) {
            return (PsiFile)result;
          }
        }
      }
    }
    return null;
  }

  public String toString(final @Nullable PsiFile psiFile, final ConvertContext context) {
    return null;
  }

  @Nonnull
  public PsiReference[] createReferences(final GenericDomValue genericDomValue, final PsiElement element, final ConvertContext context) {
    final String s = genericDomValue.getStringValue();
    if (s == null || element == null) {
      return PsiReference.EMPTY_ARRAY;
    }

    if (s.contains(PlaceholderUtils.DEFAULT_PLACEHOLDER_PREFIX)) {
      return PlaceholderUtils.createPlaceholderPropertiesReferences(genericDomValue);
    }

    return ResourceResolverUtils.getReferences(element, s, s.startsWith("/"), false);
  }

}
