package consulo.spring;

import com.intellij.codeInspection.InspectionToolProvider;
import com.intellij.spring.model.highlighting.*;
import com.intellij.spring.model.highlighting.jam.SpringJavaConfigExternalBeansErrorInspection;
import com.intellij.spring.model.highlighting.jam.SpringJavaConfigInconsistencyInspection;

/**
 * @author VISTALL
 * @since 2018-08-23
 */
public class SpringInspectionToolProvider implements InspectionToolProvider
{
	@Override
	public Class[] getInspectionClasses()
	{
		return new Class[]{
				SpringModelInspection.class,
				SpringScopesInspection.class,
				SpringBeanNameConventionInspection.class,
				InjectionValueTypeInspection.class,
				SpringAutowiringInspection.class,
				SpringConstructorArgInspection.class,
				FactoryMethodInspection.class,
				SpringDependencyCheckInspection.class,
				LookupMethodInspection.class,
				InjectionValueStyleInspection.class,
				ReplacedMethodsInspection.class,
				InjectionValueConsistencyInspection.class,
				AbstractBeanReferencesInspection.class,
				AutowiredDependenciesInspection.class,
				DuplicatedBeanNamesInspection.class,
				UtilSchemaInspection.class,
				SpringBeanInstantiationInspection.class,
				SpringJavaConfigExternalBeansErrorInspection.class,
				SpringAopErrorsInspection.class,
				SpringAopWarningsInspection.class,
				SpringExtensionInspection.class,
				MissingAspectjAutoproxyInspection.class,
				SpringJavaAutowiringInspection.class,
				SpringRequiredAnnotationInspection.class,
				SpringRequiredPropertyInspection.class,
				UnparsedCustomBeanInspection.class,
				SpringJavaConfigInconsistencyInspection.class,
				JdkProxiedBeanTypeInspection.class,
				RequiredBeanTypeInspection.class
		};
	}
}
