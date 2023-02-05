package com.intellij.spring.impl.ide.model.converters;

import consulo.util.lang.function.Condition;
import consulo.util.lang.Pair;
import consulo.xml.util.xml.Converter;
import consulo.xml.util.xml.GenericDomValue;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class CustomConverterRegistry {
  private final Map<Class, Pair<Condition<GenericDomValue>, Converter>> myCustomConverters =
    new HashMap<Class, Pair<Condition<GenericDomValue>, Converter>>();

  public void registryConverter(Class aClass, Pair<Condition<GenericDomValue>, Converter> pair) {
    myCustomConverters.put(aClass, pair);
  }

  @Nullable
  public Converter getCustomConverter(Class aClass, GenericDomValue context) {
    final Pair<Condition<GenericDomValue>, Converter> pair = myCustomConverters.get(aClass);
    if (pair != null && pair.first.value(context)) {
      return pair.second;
    }
    return null;
  }

}
