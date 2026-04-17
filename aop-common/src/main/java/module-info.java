/**
 * @author VISTALL
 * @since 05/02/2023
 */
module com.intellij.aop {
  requires transitive consulo.ide.api;

  requires transitive consulo.language.api;
  requires transitive consulo.language.impl;
  requires transitive consulo.language.editor.api;
  requires transitive consulo.language.editor.ui.api;

  requires transitive consulo.code.editor.api;
  requires transitive consulo.color.scheme.api;

  requires transitive consulo.java;
  requires transitive consulo.java.jam.api;
  requires transitive consulo.java.language.api;
  requires transitive consulo.java.language.impl;

  requires transitive com.intellij.xml;
  requires transitive com.intellij.xml.api;
  requires transitive com.intellij.xml.dom.api;
  requires transitive com.intellij.xml.editor.api;

  exports com.intellij.aop;
  exports com.intellij.aop.jam;
  exports com.intellij.aop.lexer;
  exports com.intellij.aop.psi;
  exports consulo.aop.icon;
  exports consulo.aop.localize;
}
