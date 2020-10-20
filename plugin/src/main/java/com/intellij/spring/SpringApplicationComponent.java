/*
 * Copyright (c) 2000-2006 JetBrains s.r.o. All Rights Reserved.
 */

package com.intellij.spring;

import static com.intellij.aop.psi.AopElementTypes.AOP_LEFT_PAR;
import static com.intellij.aop.psi.AopElementTypes.AOP_RIGHT_PAR;
import static com.intellij.aop.psi.AopPrattParser.parsePatternPart;

import javax.annotation.Nullable;
import jakarta.inject.Singleton;

import com.intellij.aop.AopBundle;
import com.intellij.aop.psi.AopPointcutTypes;
import com.intellij.aop.psi.PointcutDescriptor;
import com.intellij.aop.psi.PsiBeanPointcutExpression;
import com.intellij.aop.psi.PsiPointcutExpression;
import com.intellij.lang.ASTNode;
import com.intellij.lang.pratt.PrattBuilder;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.filters.ElementFilter;
import com.intellij.psi.impl.beanProperties.BeanProperty;
import com.intellij.psi.meta.MetaDataRegistrar;
import com.intellij.psi.xml.XmlTag;
import com.intellij.spring.metadata.SpringBeanMetaData;
import com.intellij.spring.metadata.SpringStereotypeQualifierMetaData;
import com.intellij.spring.model.jam.utils.JamAnnotationTypeUtil;
import com.intellij.spring.model.xml.CommonSpringBean;
import com.intellij.spring.model.xml.CustomBeanWrapper;
import com.intellij.spring.model.xml.DomSpringBean;
import com.intellij.spring.model.xml.beans.SpringBean;
import com.intellij.spring.model.xml.beans.SpringBeanPointer;
import com.intellij.spring.model.xml.beans.SpringImport;
import com.intellij.util.Function;
import com.intellij.util.NullableFunction;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.DomManager;
import com.intellij.util.xml.ElementPresentationManager;

@Singleton
public class SpringApplicationComponent
{
	static
	{
		AopPointcutTypes.registerPointcut(new PointcutDescriptor("bean")
		{
			public void parseToken(final PrattBuilder builder)
			{
				if(builder.assertToken(AOP_LEFT_PAR, AopBundle.message("error.0.expected", "(")))
				{
					parsePatternPart(builder);
					builder.assertToken(AOP_RIGHT_PAR, AopBundle.message("error.0.expected", ")"));
				}
			}

			public PsiPointcutExpression createPsi(final ASTNode node)
			{
				return new PsiBeanPointcutExpression(node);
			}
		});
	}

	public SpringApplicationComponent()
	{
		registerPresentations();

		registerMetaData();
	}

	private static void registerMetaData()
	{
		MetaDataRegistrar.getInstance().registerMetaData(new ElementFilter()
		{
			public boolean isAcceptable(Object element, PsiElement context)
			{
				if(element instanceof XmlTag)
				{
					final XmlTag tag = (XmlTag) element;
					final DomElement domElement = DomManager.getDomManager(tag.getProject()).getDomElement(tag);
					if(!(domElement instanceof DomSpringBean))
					{
						return false;
					}

					if(!(domElement instanceof CustomBeanWrapper))
					{
						return true;
					}
					if(!((CustomBeanWrapper) domElement).isParsed())
					{
						return true;
					}
				}
				return false;
			}

			public boolean isClassAcceptable(Class hintClass)
			{
				return XmlTag.class.isAssignableFrom(hintClass);
			}
		}, SpringBeanMetaData.class);

		MetaDataRegistrar.getInstance().registerMetaData(new ElementFilter()
		{
			public boolean isAcceptable(Object element, PsiElement context)
			{
				if(element instanceof PsiAnnotation)
				{
					Module module = ModuleUtil.findModuleForPsiElement(context);
					if(module != null)
					{
						for(PsiClass psiClass : JamAnnotationTypeUtil.getQualifierAnnotationTypesWithChildren(module))
						{
							PsiAnnotation annotation = (PsiAnnotation) element;
							if(annotation.getQualifiedName().equals(psiClass.getQualifiedName()))
							{
								return true;
							}
						}
					}
				}
				return false;
			}

			public boolean isClassAcceptable(Class hintClass)
			{
				return PsiAnnotation.class.isAssignableFrom(hintClass);
			}
		}, SpringStereotypeQualifierMetaData.class);
	}

	private static void registerPresentations()
	{
		ElementPresentationManager.registerNameProvider(new Function<Object, String>()
		{

			@Nullable
			public String fun(final Object s)
			{

				if(s instanceof CommonSpringBean)
				{
					final CommonSpringBean springBean = (CommonSpringBean) s;
					final String beanName = springBean.getBeanName();
					if(beanName != null)
					{
						return beanName;
					}
					final PsiClass beanClass = springBean.getBeanClass();
					if(beanClass != null)
					{
						return beanClass.getName();
					}

					return SpringBundle.message("spring.bean.with.unknown.name");
				}
				else if(s instanceof BeanProperty)
				{
					return ((BeanProperty) s).getName();
				}
				else if(s instanceof SpringBeanPointer)
				{
					return ((SpringBeanPointer) s).getName();
				}
				else if(s instanceof SpringImport)
				{
					return ((SpringImport) s).getResource().getStringValue();
				}
				return null;
			}
		});

		ElementPresentationManager.registerDocumentationProvider(new NullableFunction<Object, String>()
		{
			public String fun(final Object o)
			{
				if(o instanceof SpringBean)
				{
					return ((SpringBean) o).getDescription().getStringValue();
				}
				return null;
			}
		});
	}
}
