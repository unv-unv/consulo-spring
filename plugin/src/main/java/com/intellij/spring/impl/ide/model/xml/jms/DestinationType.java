package com.intellij.spring.impl.ide.model.xml.jms;

import consulo.xml.util.xml.NamedEnum;

public enum DestinationType implements NamedEnum {
  DURABLE_TOPIC("durableTopic"),
  QUEUE("queue"),
  TOPIC("topic");

  private final String value;

  private DestinationType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
