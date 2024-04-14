package com.intellij.spring.impl.ide.constants;

import org.jetbrains.annotations.NonNls;

/**
 * User: Sergey.Vasiliev
 */
public interface SpringAnnotationsConstants {
  // java config
  @NonNls
  String JAVA_CONFIG_CONFIGURATION_ANNOTATION = "org.springframework.config.java.annotation.Configuration";
  @NonNls
  String JAVA_CONFIG_BEAN_ANNOTATION = "org.springframework.config.java.annotation.Bean";
  @NonNls
  String JAVA_CONFIG_EXTERNAL_BEAN_ANNOTATION = "org.springframework.config.java.annotation.ExternalBean";
  @NonNls
  String JAVA_CONFIG_SCOPED_PROXY_ANNOTATION = "org.springframework.config.java.annotation.aop.ScopedProxy";

  // java config in spring 3.0
  @NonNls
  String JAVA_SPRING_CONFIGURATION_ANNOTATION = "org.springframework.context.annotation.Configuration";
  @NonNls
  String JAVA_SPRING_BEAN_ANNOTATION = "org.springframework.context.annotation.Bean";

  // stereotypes
  @NonNls
  String COMPONENT_ANNOTATION = "org.springframework.stereotype.Component";
  @NonNls
  String CONTROLLER_ANNOTATION = "org.springframework.stereotype.Controller";
  @NonNls
  String REPOSITORY_ANNOTATION = "org.springframework.stereotype.Repository";
  @NonNls
  String SERVICE_ANNOTATION = "org.springframework.stereotype.Service";

  String AUTOWIRED_ANNOTATION = "org.springframework.beans.factory.annotation.Autowired";
  String CONFIGURABLE_ANNOTATION = "org.springframework.beans.factory.annotation.Configurable";
  String QUALIFIER_ANNOTATION = "org.springframework.beans.factory.annotation.Qualifier";
  String REQUIRED_ANNOTATION = "org.springframework.beans.factory.annotation.Required";
  String COMPONENT_SCAN_ANNOTATION = "org.springframework.context.annotation.ComponentScan";

  // javax annotations
  String JAVAX_RESOURCE_ANNOTATION = "javax.annotation.Resource";
  String JAVAX_PRE_DESTROY_ANNOTATION = "javax.annotation.PreDestroy";
  String JAVAX_POST_CONSTRUCT_ANNOTATION = "javax.annotation.PostConstruct";

  // jakarta annotations
  String JAKARTA_RESOURCE_ANNOTATION = "jakarta.annotation.Resource";
  String JAKARTA_PRE_DESTROY_ANNOTATION = "jakarta.annotation.PreDestroy";
  String JAKARTA_POST_CONSTRUCT_ANNOTATION = "jakarta.annotation.PostConstruct";

  @NonNls
  String CUSTOM_AUTOWIRE_CONFIGURER_CLASS = "org.springframework.beans.factory.annotation.CustomAutowireConfigurer";

  String SPRING_BOOT_APPLICATION = "org.springframework.boot.autoconfigure.SpringBootApplication";

  // predefined component annotations
  @NonNls
  String[] SPRING_COMPONENT_ANNOTATIONS =
      {SpringAnnotationsConstants.COMPONENT_ANNOTATION, SpringAnnotationsConstants.CONTROLLER_ANNOTATION,
          SpringAnnotationsConstants.REPOSITORY_ANNOTATION, SpringAnnotationsConstants.SERVICE_ANNOTATION,
          SpringAnnotationsConstants.JAVA_CONFIG_CONFIGURATION_ANNOTATION, SpringAnnotationsConstants.JAVA_SPRING_CONFIGURATION_ANNOTATION};

}
