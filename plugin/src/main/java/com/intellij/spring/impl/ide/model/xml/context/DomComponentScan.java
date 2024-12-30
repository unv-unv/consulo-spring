// Generated on Wed Oct 17 15:28:10 MSD 2007
// DTD/Schema  :    http://www.springframework.org/schema/context

package com.intellij.spring.impl.ide.model.xml.context;

import com.intellij.java.language.psi.PsiJavaPackage;
import com.intellij.spring.impl.ide.model.context.ComponentScan;
import com.intellij.spring.impl.ide.model.converters.PackageListConverter;
import com.intellij.spring.impl.ide.model.xml.DomSpringBean;
import consulo.xml.util.xml.Convert;
import consulo.xml.util.xml.GenericAttributeValue;
import consulo.xml.util.xml.Required;

import jakarta.annotation.Nonnull;
import java.util.Collection;
import java.util.List;

/**
 * http://www.springframework.org/schema/context:component-scanElemType interface.
 */
public interface DomComponentScan extends DomSpringBean, SpringContextElement, ComponentScan {

  /**
   * Returns the value of the base-package child.
   *
   * @return the value of the base-package child.
   */
  @Nonnull
  @Required
  @Convert(PackageListConverter.class)
  GenericAttributeValue<Collection<PsiJavaPackage>> getBasePackage();

  /**
   * Returns the value of the resource-pattern child.
   *
   * @return the value of the resource-pattern child.
   */
  @Nonnull
  GenericAttributeValue<String> getResourcePattern();


  /**
   * Returns the value of the use-default-filters child.
   *
   * @return the value of the use-default-filters child.
   */
  @Nonnull
  GenericAttributeValue<Boolean> getUseDefaultFilters();


  /**
   * Returns the value of the annotation-config child.
   *
   * @return the value of the annotation-config child.
   */
  @Nonnull
  GenericAttributeValue<Boolean> getAnnotationConfig();


  /**
   * Returns the value of the name-generator child.
   *
   * @return the value of the name-generator child.
   */
  @Nonnull
  GenericAttributeValue<String> getNameGenerator();


  /**
   * Returns the value of the scope-resolver child.
   *
   * @return the value of the scope-resolver child.
   */
  @Nonnull
  GenericAttributeValue<String> getScopeResolver();


  /**
   * Returns the value of the scoped-proxy child.
   *
   * @return the value of the scoped-proxy child.
   */
  @Nonnull
  GenericAttributeValue<ScopedProxy> getScopedProxy();


  /**
   * Returns the list of include-filter children.
   *
   * @return the list of include-filter children.
   */
  @Nonnull
  List<Filter> getIncludeFilters();

  /**
   * Adds new child to the list of include-filter children.
   *
   * @return created child
   */
  Filter addIncludeFilter();


  /**
   * Returns the list of exclude-filter children.
   *
   * @return the list of exclude-filter children.
   */
  @Nonnull
  List<Filter> getExcludeFilters();

  /**
   * Adds new child to the list of exclude-filter children.
   *
   * @return created child
   */
  Filter addExcludeFilter();


}
