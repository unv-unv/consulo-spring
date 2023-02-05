/**
 * @author VISTALL
 * @since 05/02/2023
 */
module com.intellij.spring.java.ex.impl {
  requires transitive consulo.java;

  exports consulo.java.ex.facet;
  exports consulo.java.ex.jsp;

  // TODO remove
  requires java.desktop;
}