package com.intellij.spring.impl.ide.constants;

/**
 * User: Sergey.Vasiliev
 */
public interface SpringAnnotationsConstants {
  // java config
  String JAVA_CONFIG_CONFIGURATION_ANNOTATION = "org.springframework.config.java.annotation.Configuration";
  String JAVA_CONFIG_BEAN_ANNOTATION = "org.springframework.config.java.annotation.Bean";
  String JAVA_CONFIG_EXTERNAL_BEAN_ANNOTATION = "org.springframework.config.java.annotation.ExternalBean";
  String JAVA_CONFIG_SCOPED_PROXY_ANNOTATION = "org.springframework.config.java.annotation.aop.ScopedProxy";

  // java config in spring 3.0
  String JAVA_SPRING_CONFIGURATION_ANNOTATION = "org.springframework.context.annotation.Configuration";
  String JAVA_SPRING_BEAN_ANNOTATION = "org.springframework.context.annotation.Bean";

  // stereotypes
  String COMPONENT_ANNOTATION = "org.springframework.stereotype.Component";
  String CONTROLLER_ANNOTATION = "org.springframework.stereotype.Controller";
  String REPOSITORY_ANNOTATION = "org.springframework.stereotype.Repository";
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

  String CUSTOM_AUTOWIRE_CONFIGURER_CLASS = "org.springframework.beans.factory.annotation.CustomAutowireConfigurer";

  String SPRING_BOOT_APPLICATION_ANNOTATION = "org.springframework.boot.autoconfigure.SpringBootApplication";
  String SPRING_BOOT_CONFIGURATION_ANNOTATION = "org.springframework.boot.SpringBootConfiguration";

  // predefined component annotations
  String[] SPRING_COMPONENT_ANNOTATIONS =
    {SpringAnnotationsConstants.COMPONENT_ANNOTATION, SpringAnnotationsConstants.CONTROLLER_ANNOTATION,
      SpringAnnotationsConstants.REPOSITORY_ANNOTATION, SpringAnnotationsConstants.SERVICE_ANNOTATION,
      SpringAnnotationsConstants.JAVA_CONFIG_CONFIGURATION_ANNOTATION, SpringAnnotationsConstants.JAVA_SPRING_CONFIGURATION_ANNOTATION};

}
