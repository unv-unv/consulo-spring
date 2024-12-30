/*
 * Copyright (c) 2000-2006 JetBrains s.r.o. All Rights Reserved.
 */

package com.intellij.spring.impl.ide.model.converters;

import com.intellij.java.language.psi.*;
import com.intellij.java.language.psi.util.PsiFormatUtil;
import com.intellij.spring.impl.ide.SpringBundle;
import consulo.language.editor.completion.lookup.LookupElementBuilder;
import consulo.language.editor.inspection.LocalQuickFix;
import consulo.language.editor.inspection.LocalQuickFixProvider;
import consulo.language.icon.IconDescriptorUpdaters;
import consulo.language.psi.EmptyResolveMessageProvider;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiReference;
import consulo.language.psi.PsiReferenceBase;
import consulo.language.psi.resolve.ResolveState;
import consulo.language.util.IncorrectOperationException;
import consulo.localize.LocalizeValue;
import consulo.util.collection.ArrayUtil;
import consulo.xml.util.xml.ConvertContext;
import consulo.xml.util.xml.Converter;
import consulo.xml.util.xml.CustomReferenceConverter;
import consulo.xml.util.xml.GenericDomValue;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.util.ArrayList;

/**
 * @author Dmitry Avdeev
 */
public abstract class PsiMethodConverter extends Converter<PsiMethod> implements CustomReferenceConverter<PsiMethod> {

  protected final static Object[] EMPTY_ARRAY = ArrayUtil.EMPTY_OBJECT_ARRAY;

  private final MethodAccepter myMethodAccepter;

  public PsiMethodConverter(MethodAccepter accepter) {
    myMethodAccepter = accepter;
  }

  public PsiMethodConverter() {
    this(new MethodAccepter());
  }

  protected static class MethodAccepter {
    public boolean accept(PsiMethod method) {
      return !method.isConstructor() &&
        method.hasModifierProperty(PsiModifier.PUBLIC) &&
        !method.hasModifierProperty(PsiModifier.STATIC);
    }
  }

  public PsiMethod fromString(@Nullable final String methodName, final ConvertContext context) {
    if (methodName == null || methodName.length() == 0) {
      return null;
    }
    final PsiClass psiClass = getPsiClass(context);
    if (psiClass != null) {
      final PsiMethod[] psiMethods = psiClass.findMethodsByName(methodName, true);
      if (psiMethods.length == 0) {
        return null;
      }
      final MethodAccepter accepter = getMethodAccepter(context, false);
      for (PsiMethod method : psiMethods) {
        if (accepter.accept(method)) {
          return method;
        }
      }
      return psiMethods[0];
    }
    else {
      return null;
    }
  }

  public String toString(@Nullable final PsiMethod psiMethods, final ConvertContext context) {
    return null;
  }

  @Nullable
  protected abstract PsiClass getPsiClass(final ConvertContext context);

  protected MethodAccepter getMethodAccepter(ConvertContext context, final boolean forCompletion) {
    return myMethodAccepter;
  }

  private Object[] getVariants(ConvertContext context) {
    final PsiClass psiClass = getPsiClass(context);
    if (psiClass == null) {
      return EMPTY_ARRAY;
    }
    final ArrayList<Object> result = new ArrayList<Object>();
    final MethodAccepter methodAccepter = getMethodAccepter(context, true);
    PsiMethod[] methods;
    if (psiClass.isEnum()) {
      MethodResolveProcessor processor = new MethodResolveProcessor();
      psiClass.processDeclarations(processor, ResolveState.initial(), null, psiClass);
      methods = processor.getMethods();
    }
    else {
      methods = psiClass.getAllMethods();
    }
    for (PsiMethod method : methods) {
      if (methodAccepter.accept(method)) {
        String tail = PsiFormatUtil.formatMethod(method,
                                                 PsiSubstitutor.EMPTY,
                                                 PsiFormatUtil.SHOW_PARAMETERS,
                                                 PsiFormatUtil.SHOW_NAME | PsiFormatUtil.SHOW_TYPE);

        final PsiType returnType = method.getReturnType();
        LookupElementBuilder value = LookupElementBuilder.create(method.getName())
                                                         .withIcon(IconDescriptorUpdaters.getIcon(method, 0))
                                                         .withTailText(tail)
                                                         .withTypeText(returnType == null ? null : returnType.getPresentableText());
        result.add(value);
      }
    }
    return ArrayUtil.toObjectArray(result);
  }

  @Nonnull
  public PsiReference[] createReferences(final GenericDomValue<PsiMethod> genericDomValue,
                                         final PsiElement element,
                                         final ConvertContext context) {

    return new PsiReference[]{new MyReference(element, genericDomValue, context)};
  }

  protected class MyReference extends PsiReferenceBase<PsiElement> implements EmptyResolveMessageProvider, LocalQuickFixProvider {
    private final GenericDomValue<PsiMethod> myGenericDomValue;
    private final ConvertContext myContext;

    public MyReference(final PsiElement element,
                       final GenericDomValue<PsiMethod> genericDomValue,
                       ConvertContext context) {
      super(element);
      myGenericDomValue = genericDomValue;
      myContext = context;
    }

    public Object[] getVariants() {
      return PsiMethodConverter.this.getVariants(myContext);
    }

    @Nullable
    public PsiElement resolve() {
      return myGenericDomValue.getValue();
    }

    public boolean isSoft() {
      return true;
    }

    public PsiElement bindToElement(@Nonnull final PsiElement element) throws IncorrectOperationException {
      assert element instanceof PsiMethod : "PsiMethod expected";
      final PsiMethod psiMethod = (PsiMethod)element;
      myGenericDomValue.setStringValue(psiMethod.getName());
      return psiMethod;
    }

    public LocalQuickFix[] getQuickFixes() {
      return PsiMethodConverter.this.getQuickFixes(myContext);
    }

    @Nonnull
    @Override
    public LocalizeValue buildUnresolvedMessage(@Nonnull String s) {
      return LocalizeValue.localizeTODO(SpringBundle.message("cannot.resolve.method", myGenericDomValue.getStringValue()));
    }
  }

  public LocalQuickFix[] getQuickFixes(final ConvertContext context) {
    return new LocalQuickFix[0];
  }
}
