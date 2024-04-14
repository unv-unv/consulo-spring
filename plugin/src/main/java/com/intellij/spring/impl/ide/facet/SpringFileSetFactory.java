package com.intellij.spring.impl.ide.facet;

import consulo.disposer.Disposable;
import consulo.spring.impl.boot.SpringBootFileSet;

/**
 * @author VISTALL
 * @since 2024-04-14
 */
public class SpringFileSetFactory {
  public static final String XML = "xml";
  public static final String BOOT = "boot";

  public static SpringFileSet create(String type, String id, String name, Disposable parent) {
    if (type == null) {
      return new XmlSpringFileSet(id, name, parent);
    }
    
    return switch (type) {
      case XML -> new XmlSpringFileSet(id, name, parent);
      case BOOT -> new SpringBootFileSet(id, name, parent);
      default -> throw new IllegalArgumentException(type);
    };
  }
}
