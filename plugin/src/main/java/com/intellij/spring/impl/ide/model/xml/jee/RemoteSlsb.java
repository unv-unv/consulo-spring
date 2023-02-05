// Generated on Thu Nov 09 17:15:14 MSK 2006
// DTD/Schema  :    http://www.springframework.org/schema/jee

package com.intellij.spring.impl.ide.model.xml.jee;

import com.intellij.java.language.psi.PsiClass;
import com.intellij.spring.impl.ide.model.xml.DomSpringBean;
import consulo.xml.util.xml.GenericAttributeValue;

import javax.annotation.Nonnull;

/**
 * http://www.springframework.org/schema/jee:remote-slsbElemType interface.
 */
public interface RemoteSlsb extends DomSpringBean, SpringEjb {

	/**
	 * Returns the value of the home-interface child.
	 * <pre>
	 * <h3>Attribute null:home-interface documentation</h3>
	 * 	The home interface that will be narrowed to before performing
	 * 	the parameterless SLSB create() call that returns the actual
	 * 	SLSB proxy.
	 * 							
	 * </pre>
	 * @return the value of the home-interface child.
	 */
	@Nonnull
	GenericAttributeValue<PsiClass> getHomeInterface();

	/**
	 * Returns the value of the refresh-home-on-connect-failure child.
	 * <pre>
	 * <h3>Attribute null:refresh-home-on-connect-failure documentation</h3>
	 * 	Controls whether to refresh the EJB home on connect failure.
	 * 	
	 * 	Can be turned on to allow for hot restart of the EJB server.
	 * 	If a cached EJB home throws an RMI exception that indicates a
	 * 	remote connect failure, a fresh home will be fetched and the
	 * 	invocation will be retried.
	 * 							
	 * </pre>
	 * @return the value of the refresh-home-on-connect-failure child.
	 */
	@Nonnull
	GenericAttributeValue<Boolean> getRefreshHomeOnConnectFailure();
}
