// Generated on Thu Nov 09 17:15:14 MSK 2006
// DTD/Schema  :    http://www.springframework.org/schema/jee

package com.intellij.spring.model.xml.jee;

import javax.annotation.Nonnull;

import com.intellij.psi.PsiClass;
import com.intellij.spring.model.xml.beans.Identified;
import com.intellij.util.xml.GenericAttributeValue;

/**
 * http://www.springframework.org/schema/jee:entityManagerFactoryType interface.
 */
public interface EntityManagerFactory extends SpringJeeElement, Identified {

	/**
	 * Returns the value of the persistence-unit-name child.
	 * @return the value of the persistence-unit-name child.
	 */
	@Nonnull
	GenericAttributeValue<String> getPersistenceUnitName();


	/**
	 * Returns the value of the inject child.
	 * @return the value of the inject child.
	 */
	@Nonnull
	GenericAttributeValue<Boolean> getInject();


	/**
	 * Returns the value of the translate child.
	 * @return the value of the translate child.
	 */
	@Nonnull
	GenericAttributeValue<Boolean> getTranslate();


	/**
	 * Returns the value of the provider child.
	 * @return the value of the provider child.
	 */
	@Nonnull
	GenericAttributeValue<JeeProvider> getProvider();


	/**
	 * Returns the value of the provider-class child.
	 * @return the value of the provider-class child.
	 */
	@Nonnull
	GenericAttributeValue<PsiClass> getProviderClass();


	/**
	 * Returns the value of the data-source-ref child.
	 * @return the value of the data-source-ref child.
	 */
	@Nonnull
	GenericAttributeValue<String> getDataSourceRef();


	/**
	 * Returns the value of the show-sql child.
	 * @return the value of the show-sql child.
	 */
	@Nonnull
	GenericAttributeValue<Boolean> getShowSql();


	/**
	 * Returns the value of the generate-ddl child.
	 * @return the value of the generate-ddl child.
	 */
	@Nonnull
	GenericAttributeValue<Boolean> getGenerateDdl();


}
