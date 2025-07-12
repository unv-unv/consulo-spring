/**
 * @author VISTALL
 * @since 05/02/2023
 */
module com.intellij.aop {
  requires transitive consulo.ide.api;
  requires transitive consulo.java.jam.api;
  requires transitive consulo.java.language.api;
  requires transitive consulo.java.language.impl;
  requires transitive consulo.java;
  requires transitive com.intellij.xml;

  exports com.intellij.aop;
  exports com.intellij.aop.jam;
  exports com.intellij.aop.lexer;
  exports com.intellij.aop.psi;
  exports consulo.aop.icon;
  exports consulo.aop.localize;
}