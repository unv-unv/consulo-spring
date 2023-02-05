package consulo.spring.impl;

import consulo.annotation.DeprecationInfo;
import consulo.spring.impl.icon.SpringImplIconGroup;
import consulo.ui.image.Image;

@Deprecated
@DeprecationInfo("Use SpringImplIconGroup")
public interface SpringIcons {
  interface Patterns {
    Image Beans = SpringImplIconGroup.patternsBeans();
    Image DataAccess = SpringImplIconGroup.patternsDataaccess();
    Image Datasource = SpringImplIconGroup.patternsDatasource();
    Image Ejb = SpringImplIconGroup.patternsEjb();
    Image FactoryBean = SpringImplIconGroup.patternsFactorybean();
    Image Hibernate = SpringImplIconGroup.patternsHibernate();
    Image Ibatis = SpringImplIconGroup.patternsIbatis();
    Image Integration = SpringImplIconGroup.patternsIntegration();
    Image Jdk = SpringImplIconGroup.patternsJdk();
    Image Jdo = SpringImplIconGroup.patternsJdo();
    Image Jpa = SpringImplIconGroup.patternsJpa();
    Image Patterns = SpringImplIconGroup.patternsPatterns();
    Image Scheduler = SpringImplIconGroup.patternsScheduler();
    Image Toplink = SpringImplIconGroup.patternsToplink();
    Image TransactionManager = SpringImplIconGroup.patternsTransactionmanager();
  }

  Image AbstractBean = SpringImplIconGroup.abstractbean();
  Image BeanAlias = SpringImplIconGroup.beanalias();
  Image Beans = SpringImplIconGroup.beans();
  Image ChildBeanGutter = SpringImplIconGroup.childbeangutter();
  Image Dependency = SpringImplIconGroup.dependency();
  Image FactoryMethodBean = SpringImplIconGroup.factorymethodbean();
  Image FactoryMethodBean2 = SpringImplIconGroup.factorymethodbean2();
  Image FileSet = SpringImplIconGroup.fileset();
  Image GroupBeans = SpringImplIconGroup.groupbeans();
  Image IncludedFile = SpringImplIconGroup.includedfile();
  Image ParentBeanGutter = SpringImplIconGroup.parentbeangutter();
  Image ShowAutowiredDependencies = SpringImplIconGroup.showautowireddependencies();
  Image Spring = SpringImplIconGroup.spring();
  Image SpringBean = SpringImplIconGroup.springbean();
  Image SpringBeanScope = SpringImplIconGroup.springbeanscope();
  Image SpringConfig = SpringImplIconGroup.springconfig();
  Image SpringJavaBean = SpringImplIconGroup.springjavabean();
  Image SpringJavaConfig = SpringImplIconGroup.springjavaconfig();
  Image SpringProperty = SpringImplIconGroup.springproperty();
}