package consulo.spring;

import consulo.annotation.DeprecationInfo;
import consulo.spring.icon.SpringImplIconGroup;
import consulo.ui.image.Image;

@Deprecated
@DeprecationInfo("Use SpringImplIconGroup")
public interface SpringIcons
{
	interface Patterns
	{
		Image Beans = SpringImplIconGroup.patternsBeans();
		Image DataAccess = SpringImplIconGroup.patternsDataAccess();
		Image Datasource = SpringImplIconGroup.patternsDatasource();
		Image Ejb = SpringImplIconGroup.patternsEjb();
		Image FactoryBean = SpringImplIconGroup.patternsFactoryBean();
		Image Hibernate = SpringImplIconGroup.patternsHibernate();
		Image Ibatis = SpringImplIconGroup.patternsIbatis();
		Image Integration = SpringImplIconGroup.patternsIntegration();
		Image Jdk = SpringImplIconGroup.patternsJdk();
		Image Jdo = SpringImplIconGroup.patternsJdo();
		Image Jpa = SpringImplIconGroup.patternsJpa();
		Image Patterns = SpringImplIconGroup.patternsPatterns();
		Image Scheduler = SpringImplIconGroup.patternsScheduler();
		Image Toplink = SpringImplIconGroup.patternsToplink();
		Image TransactionManager = SpringImplIconGroup.patternsTransactionManager();
	}

	Image AbstractBean = SpringImplIconGroup.AbstractBean();
	Image BeanAlias = SpringImplIconGroup.BeanAlias();
	Image Beans = SpringImplIconGroup.beans();
	Image ChildBeanGutter = SpringImplIconGroup.childBeanGutter();
	Image Dependency = SpringImplIconGroup.dependency();
	Image FactoryMethodBean = SpringImplIconGroup.factoryMethodBean();
	Image FactoryMethodBean2 = SpringImplIconGroup.factoryMethodBean2();
	Image FileSet = SpringImplIconGroup.fileSet();
	Image GroupBeans = SpringImplIconGroup.groupBeans();
	Image IncludedFile = SpringImplIconGroup.IncludedFile();
	Image ParentBeanGutter = SpringImplIconGroup.parentBeanGutter();
	Image ShowAutowiredDependencies = SpringImplIconGroup.showAutowiredDependencies();
	Image Spring = SpringImplIconGroup.spring();
	Image SpringBean = SpringImplIconGroup.springBean();
	Image SpringBeanScope = SpringImplIconGroup.springBeanScope();
	Image SpringConfig = SpringImplIconGroup.springConfig();
	Image SpringJavaBean = SpringImplIconGroup.springJavaBean();
	Image SpringJavaConfig = SpringImplIconGroup.springJavaConfig();
	Image SpringProperty = SpringImplIconGroup.springProperty();
}