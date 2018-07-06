package com.intellij.spring.model.values;

import com.intellij.psi.PsiType;
import com.intellij.spring.model.converters.SpringConverterUtil;
import com.intellij.util.xml.GenericDomValue;
import javax.annotation.Nonnull;

import java.util.Collections;
import java.util.List;

public class PropsValueConverter extends PropertyValueConverter {
  @Nonnull
  public List<? extends PsiType> getValueTypes(final GenericDomValue domValue) {
    final PsiType type = SpringConverterUtil.findType(String.class, domValue.getManager().getProject());
    return type == null ? Collections.<PsiType>emptyList() : Collections.singletonList(type);
  }
}
