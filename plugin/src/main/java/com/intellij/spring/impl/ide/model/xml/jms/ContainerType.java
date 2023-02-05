package com.intellij.spring.impl.ide.model.xml.jms;

import consulo.xml.util.xml.NamedEnum;

public enum ContainerType implements NamedEnum {
  DEFAULT("default"),
  DEFAULT102("default102"),
  SIMPLE("simple"),
  SIMPLE102("simple102");

  private final String value;

  private ContainerType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
