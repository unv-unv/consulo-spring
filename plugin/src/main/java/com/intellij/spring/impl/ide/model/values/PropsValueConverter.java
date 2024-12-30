package com.intellij.spring.impl.ide.model.values;

import com.intellij.java.language.psi.PsiType;
import com.intellij.spring.impl.ide.model.converters.SpringConverterUtil;
import consulo.xml.util.xml.GenericDomValue;

import jakarta.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

public class PropsValueConverter extends PropertyValueConverter {
  @Nonnull
  public List<? extends PsiType> getValueTypes(final GenericDomValue domValue) {
    final PsiType type = SpringConverterUtil.findType(String.class, domValue.getManager().getProject());
    return type == null ? Collections.<PsiType>emptyList() : Collections.singletonList(type);
  }
}
