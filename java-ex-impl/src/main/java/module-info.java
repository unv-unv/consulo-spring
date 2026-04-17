/**
 * @author VISTALL
 * @since 05/02/2023
 */
module com.intellij.spring.java.ex.impl {
  requires transitive consulo.logging.api;
  requires transitive consulo.module.api;
  requires transitive consulo.module.content.api;
  requires transitive consulo.virtual.file.system.api;
  requires transitive consulo.util.lang;

  exports consulo.java.ex.facet;
  exports consulo.java.ex.jsp;

  // TODO remove
  requires java.desktop;
}
