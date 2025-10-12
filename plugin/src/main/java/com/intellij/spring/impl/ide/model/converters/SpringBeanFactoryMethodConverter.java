/*
 * Copyright (c) 2000-2006 JetBrains s.r.o. All Rights Reserved.
 */

package com.intellij.spring.impl.ide.model.converters;

import com.intellij.java.language.psi.*;
import com.intellij.spring.impl.ide.SpringBundle;
import com.intellij.spring.impl.ide.SpringModel;
import com.intellij.spring.impl.ide.model.SpringUtils;
import com.intellij.spring.impl.ide.model.highlighting.SpringAutowireUtil;
import com.intellij.spring.impl.ide.model.highlighting.SpringConstructorArgResolveUtil;
import com.intellij.spring.impl.ide.model.xml.CommonSpringBean;
import com.intellij.spring.impl.ide.model.xml.beans.ConstructorArg;
import com.intellij.spring.impl.ide.model.xml.beans.SpringBean;
import com.intellij.spring.impl.ide.model.xml.beans.SpringBeanPointer;
import consulo.language.editor.inspection.LocalQuickFix;
import consulo.language.editor.inspection.ProblemDescriptor;
import consulo.language.psi.resolve.ResolveState;
import consulo.language.util.IncorrectOperationException;
import consulo.localize.LocalizeValue;
import consulo.project.Project;
import consulo.spring.localize.SpringLocalize;
import consulo.xml.util.xml.ConvertContext;
import consulo.xml.util.xml.GenericDomValue;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.jetbrains.annotations.NonNls;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class SpringBeanFactoryMethodConverter extends SpringBeanMethodConverter {
    @Nullable
    protected PsiClass getPsiClass(final ConvertContext context) {
        final SpringBean springBean = (SpringBean) SpringConverterUtil.getCurrentBean(context);
        return getFactoryClass(springBean);
    }

    protected MethodAccepter getMethodAccepter(ConvertContext context, final boolean forCompletion) {
        final SpringBean springBean = (SpringBean) SpringConverterUtil.getCurrentBean(context);
        assert springBean != null;
        final SpringModel model = SpringUtils.getSpringModel(springBean);
        final boolean fromFactoryBean = springBean.getFactoryBean().getValue() != null;
        final boolean autowire = SpringAutowireUtil.isConstructorAutowire(springBean);
        final Set<ConstructorArg> args = springBean.getAllConstructorArgs();

        return new MethodAccepter() {
            public boolean accept(PsiMethod psiMethod) {
                if (psiMethod.isConstructor() || psiMethod.getReturnType() == null) {
                    return false;
                }

                final String containingClass = psiMethod.getContainingClass().getQualifiedName();
                return (!forCompletion || containingClass == null || !containingClass.equals(CommonClassNames.JAVA_LANG_OBJECT)) &&
                    isValidFactoryMethod(psiMethod, fromFactoryBean) &&
                    (forCompletion || SpringConstructorArgResolveUtil.acceptMethodByAutowire(
                        autowire,
                        args,
                        psiMethod.getParameterList().getParameters()
                    ));
            }
        };
    }

    // gets methods matching the method name
    @Nonnull
    public static List<PsiMethod> getFactoryMethodCandidates(@Nonnull final SpringBean springBean, @Nonnull final String methodName) {
        final PsiClass factoryClass = getFactoryClass(springBean);
        if (factoryClass != null) {
            final PsiMethod[] methods;
            if (factoryClass.isEnum()) {
                MethodResolveProcessor processor = new MethodResolveProcessor(methodName);
                factoryClass.processDeclarations(processor, ResolveState.initial(), null, factoryClass);
                methods = processor.getMethods();
            }
            else {
                methods = factoryClass.findMethodsByName(methodName, true);
            }
            if (methods.length > 0) {
                final ArrayList<PsiMethod> result = new ArrayList<PsiMethod>(methods.length);
                final boolean fromFactoryBean = springBean.getFactoryBean().getValue() != null;
                for (PsiMethod method : methods) {
                    if (isValidFactoryMethod(method, fromFactoryBean)) {
                        result.add(method);
                    }
                }
                return result;
            }
        }
        return Collections.emptyList();
    }

    public static boolean isValidFactoryMethod(final PsiMethod psiMethod, final boolean fromFactoryBean) {
        if (psiMethod.isConstructor() || psiMethod.getReturnType() == null) {
            return false;
        }

        final boolean isStatic = isStatic(psiMethod);
        return isPublic(psiMethod) &&
            (fromFactoryBean && !isStatic || !fromFactoryBean && isStatic) &&
            isProperReturnType(psiMethod);
    }

    @Nullable
    public static PsiClass getFactoryClass(final SpringBean springBean) {
        final SpringBeanPointer factoryBeanPointer = springBean.getFactoryBean().getValue();
        if (factoryBeanPointer == null) {
            return springBean.getBeanClass(false);
        }
        else {
            final CommonSpringBean factoryBean = factoryBeanPointer.getSpringBean();
            return factoryBean.equals(springBean) ? null : factoryBean.getBeanClass(false);
        }
    }

    public static boolean isPublic(final PsiMethod psiMethod) {
        return psiMethod.hasModifierProperty(PsiModifier.PUBLIC);
    }

    public static boolean isStatic(final PsiMethod psiMethod) {
        return psiMethod.hasModifierProperty(PsiModifier.STATIC);
    }

    public static boolean isProperReturnType(final PsiMethod psiMethod) {
        final PsiType returnType = psiMethod.getReturnType();
        return returnType instanceof PsiClassType;
    }

    public LocalQuickFix[] getQuickFixes(final ConvertContext context) {
        List<LocalQuickFix> fixes = new ArrayList<LocalQuickFix>();
        final SpringBean springBean = (SpringBean) SpringConverterUtil.getCurrentBean(context);
        final GenericDomValue element = (GenericDomValue) context.getInvocationElement();

        final String elementName = element.getStringValue();
        if (elementName != null && elementName.length() > 0) {
            PsiClass psiClass = getFactoryMethodClass(springBean);
            if (psiClass != null) {
                fixes.add(getCreateNewMethodQuickFix(springBean, psiClass, elementName));
            }

            return fixes.toArray(new LocalQuickFix[fixes.size()]);
        }

        return LocalQuickFix.EMPTY_ARRAY;
    }

    @Nullable
    private static PsiClass getFactoryMethodClass(final SpringBean springBean) {
        final SpringBeanPointer factoryBeanPointer = springBean.getFactoryBean().getValue();

        return factoryBeanPointer != null ? factoryBeanPointer.getSpringBean().getBeanClass(false) : springBean.getBeanClass(false);
    }

    private static LocalQuickFix getCreateNewMethodQuickFix(
        final SpringBean springBean,
        final PsiClass beanClass,
        final String elementName
    ) {
        return new LocalQuickFix() {
            @Nonnull
            public LocalizeValue getName() {
                return SpringLocalize.modelCreateFactoryMethodQuickfixMessage(getSignature(springBean, elementName));
            }

            public void applyFix(@Nonnull final Project project, @Nonnull final ProblemDescriptor descriptor) {
                try {
                    assert beanClass != null;
                    final PsiElementFactory elementFactory = JavaPsiFacade.getInstance(beanClass.getProject()).getElementFactory();

                    final String signature = getSignature(springBean, elementName) + "{ return null; }";

                    final PsiMethod method = elementFactory.createMethodFromText(signature, null);

                    beanClass.add(method);
                }
                catch (IncorrectOperationException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }

    @NonNls
    private static String getSignature(@Nonnull final SpringBean springBean, @Nonnull final String elementName) {
        boolean isStatic = springBean.getFactoryBean().getValue() == null;

        String params = SpringConstructorArgResolveUtil.suggestParamsForConstructorArgsAsString(springBean);
        final PsiClass psiClass = springBean.getBeanClass();
        String returnType = psiClass == null ? "java.lang.String" : psiClass.getQualifiedName();

        StringBuilder signature = new StringBuilder();
        signature.append(PsiModifier.PUBLIC);
        signature.append(" ");
        signature.append(isStatic ? PsiModifier.STATIC : "");
        signature.append(" ");
        signature.append(returnType);
        signature.append(" ");
        signature.append(elementName);
        signature.append(" (");
        signature.append(params);
        signature.append(")");

        return signature.toString();
    }

}