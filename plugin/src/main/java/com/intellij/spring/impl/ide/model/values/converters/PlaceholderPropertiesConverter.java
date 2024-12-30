package com.intellij.spring.impl.ide.model.values.converters;

import com.intellij.java.language.psi.PsiType;
import com.intellij.spring.impl.ide.model.values.PlaceholderUtils;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiReference;
import consulo.util.lang.Pair;
import consulo.util.lang.function.Condition;
import consulo.xml.util.xml.ConvertContext;
import consulo.xml.util.xml.Converter;
import consulo.xml.util.xml.CustomReferenceConverter;
import consulo.xml.util.xml.GenericDomValue;
import org.jetbrains.annotations.NonNls;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public class PlaceholderPropertiesConverter extends Converter<String> implements CustomReferenceConverter {

  public String fromString(@Nullable @NonNls String s, final ConvertContext context) {
    return s;
  }

  public String toString(@Nullable String s, final ConvertContext context) {
    return s;
  }

  @Nonnull
  public PsiReference[] createReferences(final GenericDomValue genericDomValue, final PsiElement element, final ConvertContext context) {
    return PlaceholderUtils.createPlaceholderPropertiesReferences(genericDomValue);
  }

  public static class PlaceholderPropertiesCondition implements Condition<Pair<PsiType, GenericDomValue>> {
    public boolean value(final Pair<PsiType, GenericDomValue> pair) {
      return PlaceholderUtils.isPlaceholder(pair.getSecond());
    }
  }

}
