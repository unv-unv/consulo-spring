/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */

package com.intellij.spring.impl.ide.model.values.converters;

import com.intellij.java.language.psi.PsiArrayType;
import com.intellij.java.language.psi.PsiType;
import com.intellij.spring.impl.ide.model.converters.ResourceResolverUtils;
import consulo.language.psi.ElementManipulators;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFileSystemItem;
import consulo.language.psi.PsiReference;
import consulo.util.lang.Pair;
import consulo.util.lang.StringUtil;
import consulo.util.lang.function.Condition;
import consulo.util.lang.function.PairProcessor;
import consulo.xml.util.xml.ConvertContext;
import consulo.xml.util.xml.Converter;
import consulo.xml.util.xml.CustomReferenceConverter;
import consulo.xml.util.xml.GenericDomValue;
import org.jetbrains.annotations.NonNls;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

public class ResourceValueConverter extends Converter<Object> implements CustomReferenceConverter {

  public Object fromString(@Nullable @NonNls String s, final ConvertContext context) {
    final GenericDomValue domValue = (GenericDomValue)context.getInvocationElement();
    return StringUtil.isEmpty(s)
           ? Collections.emptySet()
           : ResourceResolverUtils.addResourceFilesFrom(domValue, s, new HashSet<PsiFileSystemItem>(), Condition.TRUE);
  }

  public String toString(@Nullable Object o, final ConvertContext context) {
    return null;
  }


  @Nonnull
  public PsiReference[] createReferences(final GenericDomValue genericDomValue, final PsiElement element, final ConvertContext context) {
    final ArrayList<PsiReference> result = new ArrayList<PsiReference>();
    final int startInElement = ElementManipulators.getOffsetInElement(element);
    ResourceResolverUtils.processSeparatedString(genericDomValue.getStringValue(), ",", new PairProcessor<String, Integer>() {
      public boolean process(final String s, final Integer offset) {
        result.addAll(Arrays.asList(ResourceResolverUtils.getReferences(element, s, true, false, offset + startInElement, true)));
        return true;
      }
    });
    return result.isEmpty() ? PsiReference.EMPTY_ARRAY : result.toArray(new PsiReference[result.size()]);
  }

  public static class ResourceValueConverterCondition implements Condition<Pair<PsiType, GenericDomValue>> {
    public boolean value(Pair<PsiType, GenericDomValue> pair) {
      PsiType psiType = pair.getFirst();
      if (psiType instanceof PsiArrayType) {
        psiType = ((PsiArrayType)psiType).getComponentType();
      }
      return psiType != null && "org.springframework.core.io.Resource".equals(psiType.getCanonicalText());
    }
  }
}