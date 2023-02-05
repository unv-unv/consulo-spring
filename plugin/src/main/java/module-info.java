/**
 * @author VISTALL
 * @since 05/02/2023
 */
open module com.intellij.spring {
  requires consulo.ide.api;
  requires com.intellij.aop;
  requires com.intellij.spring.java.ex.impl;
  requires consulo.java.properties.impl;
  requires com.intellij.properties;

  // TODO remove in future
  requires java.desktop;
  requires forms.rt;
  requires consulo.ide.impl;
}