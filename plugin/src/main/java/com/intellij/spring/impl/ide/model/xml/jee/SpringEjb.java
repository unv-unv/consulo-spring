// Generated on Thu Nov 09 17:15:14 MSK 2006
// DTD/Schema  :    http://www.springframework.org/schema/jee

package com.intellij.spring.impl.ide.model.xml.jee;

import com.intellij.java.language.psi.PsiClass;
import consulo.xml.util.xml.DomElement;
import consulo.xml.util.xml.GenericAttributeValue;
import consulo.xml.util.xml.GenericDomValue;
import consulo.xml.util.xml.Required;

import jakarta.annotation.Nonnull;

/**
 * http://www.springframework.org/schema/jee:ejbType interface.
 */
public interface SpringEjb extends DomElement, JndiLocated {

  /**
   * Returns the value of the lookup-home-on-startup child.
   * <pre>
   * <h3>Attribute null:lookup-home-on-startup documentation</h3>
   * 	Controls whether the lookup of the EJB home object is performed
   * 	immediately on startup (if true, the default), or on first access
   * 	(if false).
   *
   * </pre>
   *
   * @return the value of the lookup-home-on-startup child.
   */
  @Nonnull
  GenericAttributeValue<Boolean> getLookupHomeOnStartup();


  /**
   * Returns the value of the cache-home child.
   * <pre>
   * <h3>Attribute null:cache-home documentation</h3>
   * 	Controls whether the EJB home object is cached once it has been located.
   *
   * </pre>
   *
   * @return the value of the cache-home child.
   */
  @Nonnull
  GenericAttributeValue<Boolean> getCacheHome();


  /**
   * Returns the value of the business-interface child.
   * <pre>
   * <h3>Attribute null:business-interface documentation</h3>
   * 	The business interface of the EJB being proxied.
   *
   * </pre>
   *
   * @return the value of the business-interface child.
   */
  @Nonnull
  @Required
  GenericAttributeValue<PsiClass> getBusinessInterface();


  /**
   * Returns the value of the jndi-name child.
   * <pre>
   * <h3>Attribute null:jndi-name documentation</h3>
   * 	The JNDI name to look up.
   *
   * </pre>
   *
   * @return the value of the jndi-name child.
   */
  @Nonnull
  @Required
  GenericAttributeValue<String> getJndiName();


  /**
   * Returns the value of the resource-ref child.
   * <pre>
   * <h3>Attribute null:resource-ref documentation</h3>
   * 	Controls whether the lookup occurs in a J2EE container, i.e. if the
   * 	prefix "java:comp/env/" needs to be added if the JNDI name doesn't
   * 	already contain it.
   *
   * </pre>
   *
   * @return the value of the resource-ref child.
   */
  @Nonnull
  GenericAttributeValue<Boolean> getResourceRef();


  /**
   * Returns the value of the environment child.
   * <pre>
   * <h3>Element http://www.springframework.org/schema/jee:environment documentation</h3>
   * 	The newline-separated, key-value pairs for the JNDI environment
   * 	(in standard Properties format, namely 'key=value' pairs)
   *
   * </pre>
   *
   * @return the value of the environment child.
   */
  @Nonnull
  GenericDomValue<String> getEnvironment();


}
