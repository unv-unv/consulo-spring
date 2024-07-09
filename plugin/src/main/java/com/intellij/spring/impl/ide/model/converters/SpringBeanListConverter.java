/*
 * Copyright (c) 2000-2006 JetBrains s.r.o. All Rights Reserved.
 */

/*
 * Created by IntelliJ IDEA.
 * User: Sergey.Vasiliev
 * Date: Nov 13, 2006
 * Time: 4:35:08 PM
 */
package com.intellij.spring.impl.ide.model.converters;

import com.intellij.java.language.psi.PsiClass;
import com.intellij.spring.impl.ide.SpringBundle;
import com.intellij.spring.impl.ide.SpringModel;
import com.intellij.spring.impl.ide.model.SpringUtils;
import com.intellij.spring.impl.ide.model.xml.CommonSpringBean;
import com.intellij.spring.impl.ide.model.xml.DomSpringBean;
import com.intellij.spring.impl.ide.model.xml.beans.Beans;
import com.intellij.spring.impl.ide.model.xml.beans.SpringBaseBeanPointer;
import com.intellij.spring.impl.ide.model.xml.beans.SpringBean;
import com.intellij.spring.impl.ide.model.xml.beans.SpringBeanPointer;
import consulo.document.util.TextRange;
import consulo.language.editor.completion.lookup.LookupElement;
import consulo.language.editor.completion.lookup.LookupElementBuilder;
import consulo.language.editor.inspection.LocalQuickFix;
import consulo.language.editor.inspection.LocalQuickFixProvider;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiReference;
import consulo.localize.LocalizeValue;
import consulo.util.lang.StringUtil;
import consulo.xml.util.xml.ConvertContext;
import consulo.xml.util.xml.GenericDomValue;
import consulo.xml.util.xml.converters.DelimitedListConverter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class SpringBeanListConverter extends DelimitedListConverter<SpringBeanPointer> {

  public SpringBeanListConverter() {
    this(SpringUtils.SPRING_DELIMITERS);
  }

  protected SpringBeanListConverter(String delimiters) {
    super(delimiters);
  }

  @Override
  @Nullable
  protected SpringBeanPointer convertString(final @Nullable String s, final ConvertContext context) {
    if (s == null) return null;

    final SpringModel model = SpringConverterUtil.getSpringModel(context);
    if (model == null) return null;

    return SpringUtils.findBean(model, s);
  }

  @Override
  @Nullable
  protected String toString(@Nullable final SpringBeanPointer springBeanPointer) {
    return springBeanPointer == null ? null : springBeanPointer.getName();
  }

  @Override
  @Nullable
  protected PsiElement resolveReference(final SpringBeanPointer springBeanPointer, final ConvertContext context) {
    return springBeanPointer == null ? null : springBeanPointer.getPsiElement();
  }

  @Nonnull
  @Override
  public LocalizeValue buildUnresolvedMessageInner(@Nullable String value) {
    return LocalizeValue.localizeTODO(SpringBundle.message("model.bean.error.message", value));
  }

  @Override
  protected Object[] getReferenceVariants(final ConvertContext context, GenericDomValue<List<SpringBeanPointer>> genericDomValue) {
    final SpringModel model = SpringConverterUtil.getSpringModel(context);
    if (model == null) return EMPTY_ARRAY;
    
    List<SpringBeanPointer> variants = new ArrayList<SpringBeanPointer>();

    final DomSpringBean currentBean = SpringConverterUtil.getCurrentBean(context);

    final Collection<? extends SpringBeanPointer> allBeans = getVariantBeans(model);
    for (SpringBeanPointer pointer : allBeans) {
      if (pointer.isReferenceTo(currentBean)) continue;

      final String beanName = pointer.getName();
      if (beanName != null) {
        for (String string : model.getAllBeanNames(beanName)) {
          if (StringUtil.isNotEmpty(string)) {
            variants.add(pointer.derive(string));
          }
        }
      }
    }

    final List<SpringBeanPointer> list = genericDomValue.getValue();
    if (list != null) {
      for (Iterator<SpringBeanPointer> i = variants.iterator(); i.hasNext();) {
        final CommonSpringBean variant = i.next().getSpringBean();
        for (SpringBeanPointer existing: list) {
          if (existing.isReferenceTo(variant)) {
            i.remove();
            break;
          }
        }
      }
    }

    List<LookupElement> result = new ArrayList<LookupElement>();
    for (final SpringBeanPointer pointer : variants) {
      final String beanName = pointer.getName();
      if (beanName != null) {
        final LookupElementBuilder element = LookupElementBuilder.create(pointer, beanName).withIcon(pointer.getBeanIcon());
        final PsiClass psiClass = pointer.getBeanClass();
        if (psiClass != null) {
          element.withTypeText(psiClass.getName());
        }
        result.add(element);
      }
    }

    return result.toArray();
  }

  protected Collection<? extends SpringBaseBeanPointer> getVariantBeans(@Nonnull SpringModel model) {
    return model.getAllCommonBeans(true);
  }

  @Override
  @Nonnull
  protected PsiReference createPsiReference(final PsiElement element, final int start, final int end, final ConvertContext context,
											final GenericDomValue<List<SpringBeanPointer>> genericDomValue, final boolean delimitersOnly) {
    
    return new MyFixableReference(element, new TextRange(start, end), context, genericDomValue, delimitersOnly);
  }

  protected class MyFixableReference extends MyPsiReference implements LocalQuickFixProvider {

    public MyFixableReference(final PsiElement element, final TextRange range, final ConvertContext context, final GenericDomValue<List<SpringBeanPointer>> genericDomValue,
							  final boolean delimitersOnly) {
      super(element, range, context, genericDomValue, delimitersOnly);
    }

    private final CreateElementQuickFixProvider<List<SpringBeanPointer>> myQuickFixProvider =

      new CreateElementQuickFixProvider<List<SpringBeanPointer>>(SpringBundle.message("model.bean.quickfix.family")) {

        @Override
        protected String getElementName(@Nonnull final GenericDomValue<List<SpringBeanPointer>> genericDomValue) {
          return getValue().trim();
        }

        @Override
        protected void apply(final String elementName, final GenericDomValue<List<SpringBeanPointer>> genericDomValue) {
          Beans beans = genericDomValue.getParentOfType(Beans.class, false);
          final SpringBean springBean = beans.addBean();
          springBean.setName(elementName);
        }

        @Override
        @Nonnull
        protected String getFixName(String elementName) {
          return SpringBundle.message("model.bean.quickfix.message", elementName);
        }
      };

    @Override
    public LocalQuickFix[] getQuickFixes() {
      return myQuickFixProvider.getQuickFixes(myGenericDomValue);
    }
  }
}
