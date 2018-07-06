package com.intellij.spring.model.converters;

import com.intellij.openapi.project.Project;
import com.intellij.spring.SpringManager;
import com.intellij.util.xml.Converter;
import com.intellij.util.xml.GenericDomValue;
import com.intellij.util.xml.WrappingConverter;
import javax.annotation.Nullable;
import javax.annotation.Nonnull;

public class PropertyKeyConverter extends WrappingConverter {

  @Nullable
  public Converter getConverter(@Nonnull final GenericDomValue domValue) {
    final CustomConverterRegistry registry = getRegistry(domValue.getManager().getProject());

    return registry.getCustomConverter(getClass(), domValue);
  }

  private static CustomConverterRegistry getRegistry(final Project project) {
    return SpringManager.getInstance(project).getCustomConverterRegistry();
  }


}
