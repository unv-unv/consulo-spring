package com.intellij.spring.impl.ide.model.converters;

import consulo.project.Project;
import com.intellij.spring.impl.ide.SpringManager;
import consulo.xml.util.xml.Converter;
import consulo.xml.util.xml.GenericDomValue;
import consulo.xml.util.xml.WrappingConverter;
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
