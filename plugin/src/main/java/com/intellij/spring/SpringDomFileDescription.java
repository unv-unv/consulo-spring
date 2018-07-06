/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */
package com.intellij.spring;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.intellij.jam.model.common.BaseRootImpl;
import com.intellij.openapi.extensions.Extensions;
import com.intellij.openapi.util.Iconable;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import com.intellij.spring.constants.SpringConstants;
import com.intellij.spring.impl.model.CustomBeanWrapperImpl;
import com.intellij.spring.impl.model.aop.AdvisorImpl;
import com.intellij.spring.impl.model.aop.AfterReturningAdviceImpl;
import com.intellij.spring.impl.model.aop.AspectjAutoproxyImpl;
import com.intellij.spring.impl.model.aop.BasicAdviceImpl;
import com.intellij.spring.impl.model.aop.DeclareParentsImpl;
import com.intellij.spring.impl.model.aop.SpringAspectImpl;
import com.intellij.spring.impl.model.aop.SpringPointcutImpl;
import com.intellij.spring.impl.model.beans.*;
import com.intellij.spring.impl.model.context.AnnotationConfigImpl;
import com.intellij.spring.impl.model.context.ComponentScanImpl;
import com.intellij.spring.impl.model.context.FilterImpl;
import com.intellij.spring.impl.model.context.LoadTimeWeaverImpl;
import com.intellij.spring.impl.model.context.PropertyPlaceholderImpl;
import com.intellij.spring.impl.model.jee.JndiLookupImpl;
import com.intellij.spring.impl.model.jee.LocalSlsbImpl;
import com.intellij.spring.impl.model.jee.RemoteSlsbImpl;
import com.intellij.spring.impl.model.lang.LangBeanImpl;
import com.intellij.spring.impl.model.tx.TxAdviceImpl;
import com.intellij.spring.impl.model.tx.TxAnnotationDrivenImpl;
import com.intellij.spring.impl.model.util.UtilConstantImpl;
import com.intellij.spring.impl.model.util.UtilListImpl;
import com.intellij.spring.impl.model.util.UtilMapImpl;
import com.intellij.spring.impl.model.util.UtilPropertiesImpl;
import com.intellij.spring.impl.model.util.UtilPropertyPathImpl;
import com.intellij.spring.impl.model.util.UtilSetImpl;
import com.intellij.spring.model.xml.CustomBeanWrapper;
import com.intellij.spring.model.xml.aop.Advisor;
import com.intellij.spring.model.xml.aop.AfterReturningAdvice;
import com.intellij.spring.model.xml.aop.AspectjAutoproxy;
import com.intellij.spring.model.xml.aop.BasicAdvice;
import com.intellij.spring.model.xml.aop.DeclareParents;
import com.intellij.spring.model.xml.aop.SpringAspect;
import com.intellij.spring.model.xml.aop.SpringPointcut;
import com.intellij.spring.model.xml.beans.*;
import com.intellij.spring.model.xml.context.AnnotationConfig;
import com.intellij.spring.model.xml.context.ComponentScan;
import com.intellij.spring.model.xml.context.Filter;
import com.intellij.spring.model.xml.context.LoadTimeWeaver;
import com.intellij.spring.model.xml.context.PropertyPlaceholder;
import com.intellij.spring.model.xml.jee.JndiLookup;
import com.intellij.spring.model.xml.jee.LocalSlsb;
import com.intellij.spring.model.xml.jee.RemoteSlsb;
import com.intellij.spring.model.xml.lang.LangBean;
import com.intellij.spring.model.xml.tx.Advice;
import com.intellij.spring.model.xml.tx.AnnotationDriven;
import com.intellij.spring.model.xml.util.PropertyPath;
import com.intellij.spring.model.xml.util.SpringConstant;
import com.intellij.spring.model.xml.util.UtilList;
import com.intellij.spring.model.xml.util.UtilMap;
import com.intellij.spring.model.xml.util.UtilProperties;
import com.intellij.spring.model.xml.util.UtilSet;
import com.intellij.util.NotNullFunction;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.util.xml.DomFileDescription;
import consulo.spring.SpringIcons;
import consulo.ui.image.Image;

/**
 * @author peter
*/
public class SpringDomFileDescription extends DomFileDescription<Beans> {
  
  private static final List<String> SPRING_NAMESPACES = Arrays.asList(SpringConstants.BEANS_DTD_1, SpringConstants.BEANS_DTD_2, SpringConstants.BEANS_XSD);

  public SpringDomFileDescription() {
    super(Beans.class, "beans");
  }

  @Nullable
  @Override
  public Image getFileIcon(@Iconable.IconFlags int flags) {
    return SpringIcons.SpringConfig;
  }

  @Override
  public boolean isMyFile(@Nonnull XmlFile file) {
    return true;
  }

  protected void initializeFileDescription() {

    registerImplementation(Beans.class, (Class)BaseRootImpl.class);
    registerImplementation(SpringBean.class, SpringBeanImpl.class);
    registerImplementation(SpringProperty.class, SpringPropertyImpl.class);
    registerImplementation(ConstructorArg.class, ConstructorArgImpl.class);
    registerImplementation(ReplacedMethod.class, ReplacedMethodImpl.class);
    registerImplementation(SpringValue.class, SpringValueImpl.class);
    registerImplementation(SpringEntry.class, SpringEntryImpl.class);
    registerImplementation(SpringKey.class, SpringKeyImpl.class);
    registerImplementation(ListOrSet.class, ListOrSetImpl.class);
    registerImplementation(UtilProperties.class, UtilPropertiesImpl.class);
    registerImplementation(UtilList.class, UtilListImpl.class);
    registerImplementation(UtilMap.class, UtilMapImpl.class);
    registerImplementation(UtilSet.class, UtilSetImpl.class);
    registerImplementation(SpringConstant.class, UtilConstantImpl.class);
    registerImplementation(PropertyPath.class, UtilPropertyPathImpl.class);

    registerImplementation(PropertyPlaceholder.class, PropertyPlaceholderImpl.class);
    registerImplementation(ComponentScan.class, ComponentScanImpl.class);
    registerImplementation(LoadTimeWeaver.class, LoadTimeWeaverImpl.class);
    registerImplementation(Filter.class, FilterImpl.class);
    registerImplementation(AnnotationConfig.class, AnnotationConfigImpl.class);

    registerImplementation(JndiLookup.class, JndiLookupImpl.class);
    registerImplementation(RemoteSlsb.class, RemoteSlsbImpl.class);
    registerImplementation(LocalSlsb.class, LocalSlsbImpl.class);

    registerImplementation(SpringPointcut.class, SpringPointcutImpl.class);
    registerImplementation(SpringAspect.class, SpringAspectImpl.class);
    registerImplementation(BasicAdvice.class, BasicAdviceImpl.class);
    registerImplementation(DeclareParents.class, DeclareParentsImpl.class);
    registerImplementation(AfterReturningAdvice.class, AfterReturningAdviceImpl.class);
    registerImplementation(Advisor.class, AdvisorImpl.class);
    registerImplementation(AspectjAutoproxy.class, AspectjAutoproxyImpl.class);

    registerImplementation(Advice.class, TxAdviceImpl.class);
    registerImplementation(AnnotationDriven.class, TxAnnotationDrivenImpl.class);

    registerImplementation(PNamespaceValue.class, PNamespaceValueImpl.class);
    registerImplementation(PNamespaceRefValue.class, PNamespaceRefValueImpl.class);
    registerImplementation(CustomBeanWrapper.class, CustomBeanWrapperImpl.class);

    registerImplementation(MetadataValue.class, MetadataValueImpl.class);
    registerImplementation(TypedBeanPointerAttribute.class, TypedBeanPointerAttributeImpl.class);
    
    registerImplementation(LangBean.class, LangBeanImpl.class);

    registerImplementation(SpringDomQualifier.class, SpringDomQualifierImpl.class);
    registerImplementation(SpringDomAttribute.class, SpringDomAttributeImpl.class);

    registerNamespacePolicy(SpringConstants.BEANS_NAMESPACE_KEY, new NotNullFunction<XmlTag, List<String>>() {
      @Nonnull
      public List<String> fun(final XmlTag tag) {
        return SPRING_NAMESPACES;
      }
    });

    registerNamespacePolicy(SpringConstants.AOP_NAMESPACE_KEY, SpringConstants.AOP_NAMESPACE);
    registerNamespacePolicy(SpringConstants.JEE_NAMESPACE_KEY, SpringConstants.JEE_NAMESPACE);
    registerNamespacePolicy(SpringConstants.LANG_NAMESPACE_KEY, SpringConstants.LANG_NAMESPACE);
    registerNamespacePolicy(SpringConstants.TOOL_NAMESPACE_KEY, SpringConstants.TOOL_NAMESPACE);
    registerNamespacePolicy(SpringConstants.TX_NAMESPACE_KEY, SpringConstants.TX_NAMESPACE);
    registerNamespacePolicy(SpringConstants.UTIL_NAMESPACE_KEY, SpringConstants.UTIL_NAMESPACE);
    registerNamespacePolicy(SpringConstants.JMS_NAMESPACE_KEY, SpringConstants.JMS_NAMESPACE);
    registerNamespacePolicy(SpringConstants.CONTEXT_NAMESPACE_KEY, SpringConstants.CONTEXT_NAMESPACE);
    registerNamespacePolicy(SpringConstants.P_NAMESPACE_KEY, SpringConstants.P_NAMESPACE);
  }

  public static SpringDomFileDescription getInstance() {
    return ContainerUtil.findInstance(Extensions.getExtensions(EP_NAME), SpringDomFileDescription.class);
  }
}
