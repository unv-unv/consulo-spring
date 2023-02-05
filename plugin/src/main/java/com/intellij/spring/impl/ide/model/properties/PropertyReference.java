/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */

package com.intellij.spring.impl.ide.model.properties;

import com.intellij.java.impl.psi.impl.beanProperties.CreateBeanPropertyFix;
import com.intellij.java.language.psi.*;
import com.intellij.java.language.psi.util.InheritanceUtil;
import com.intellij.java.language.psi.util.PropertyUtil;
import com.intellij.spring.impl.ide.SpringBundle;
import com.intellij.spring.impl.ide.SpringModel;
import com.intellij.spring.impl.ide.model.converters.SpringConverterUtil;
import com.intellij.spring.impl.ide.model.xml.beans.SpringBaseBeanPointer;
import com.intellij.spring.impl.ide.model.xml.beans.SpringBeanPointer;
import com.intellij.spring.impl.ide.model.xml.beans.SpringPropertyDefinition;
import consulo.document.util.TextRange;
import consulo.language.editor.completion.lookup.LookupValueFactory;
import consulo.language.editor.inspection.LocalQuickFix;
import consulo.language.editor.inspection.LocalQuickFixProvider;
import consulo.language.psi.*;
import consulo.language.util.IncorrectOperationException;
import consulo.spring.impl.SpringIcons;
import consulo.util.collection.ContainerUtil;
import consulo.util.lang.StringUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Function;

/**
 * @author Dmitry Avdeev
 */
public class PropertyReference extends PsiReferenceBase<PsiElement> implements PsiPolyVariantReference, EmptyResolveMessageProvider,
		LocalQuickFixProvider
{

  private final PropertyReferenceSet myReferenceSet;
  private final int myIndex;

  public PropertyReference(PropertyReferenceSet set,
                           TextRange range,
                           int index) {

    super(set.getElement(), range, true);
    myReferenceSet = set;
    myIndex = index;
  }

  @Nullable
  private PsiClass getPsiClass() {
    if (myIndex == 0) {
      return myReferenceSet.getBeanClass();
    } else {
      final ResolveResult[] results = myReferenceSet.getReference(myIndex - 1).multiResolve(false);
      if (results.length > 0) {
        PsiMethod method = chooseMethod(ContainerUtil.map2List(results, resolveResult -> (PsiMethod)resolveResult.getElement()));
        final PsiType type = method.getReturnType();
        if (type instanceof PsiClassType) {
          return ((PsiClassType)type).resolve();
        }
      }
    }
    return null;
  }

  private Set<PsiMethod> getSharedProperties(@Nonnull final Collection<SpringBaseBeanPointer> descendants, final boolean forCompletion) {
    final Set<PsiClass> beanClasses = getUniqueBeanClasses(descendants);
    boolean acceptSetters = forCompletion || isLast();
    boolean acceptGetters = !isLast();

    final Set<PsiMethod> maps = new HashSet<PsiMethod>();
    final String propertyName = getValue();
    for (PsiClass beanClass : beanClasses) {
      if (acceptSetters) {
        maps.addAll(PropertyUtil.getSetters(beanClass, propertyName));
      }
      if (acceptGetters) {
        maps.addAll(PropertyUtil.getGetters(beanClass, propertyName));
      }
    }
    return maps;
  }

  private static PsiMethod chooseMethod(List<PsiMethod> methods) {
    switch (methods.size()) {
      case 1:
        return methods.get(0);
      default:
        PsiMethod chosenMethod = methods.get(0);
        if (methods.size() > 1) {
          for (int i = 1, methodsSize = methods.size(); i < methodsSize; i++) {
            PsiMethod method = methods.get(i);
            if (InheritanceUtil.isInheritorOrSelf(chosenMethod.getContainingClass(), method.getContainingClass(), true)) {
              chosenMethod = method;
            }
          }
        }
        return chosenMethod;
    }
  }

  private Map<String, Set<PsiMethod>> getAllSharedProperties(@Nonnull final Collection<SpringBaseBeanPointer> descendants, final boolean forCompletion) {
    final Set<PsiClass> beanClasses = getUniqueBeanClasses(descendants);
    boolean acceptSetters = forCompletion || isLast();
    boolean acceptGetters = !isLast();

    final List<Map<String, PsiMethod>> maps = new ArrayList<Map<String, PsiMethod>>();
    for (PsiClass beanClass : beanClasses) {
      maps.add(PropertyUtil.getAllProperties(beanClass, acceptSetters, acceptGetters));
    }
    return reduce(maps);
  }


  @Nullable
  public PsiMethod resolve() {
    final ResolveResult[] resolveResults = multiResolve(false);
    return (PsiMethod) (resolveResults.length == 1 ? resolveResults[0].getElement() : null);
  }

  @Nonnull
  public ResolveResult[] multiResolve(final boolean incompleteCode) {

    String propertyName = getValue();
    if (isFirst()) {
      final SpringModel model = SpringConverterUtil.getSpringModel(myReferenceSet.getContext());
      if (model == null) return ResolveResult.EMPTY_ARRAY;

      final Collection<SpringBaseBeanPointer> descendants = model.getDescendants(myReferenceSet.getBean());
      if (!descendants.isEmpty()) {
        final Set<PsiMethod> methods = getSharedProperties(descendants, false);
        return (ResolveResult[]) ContainerUtil.map2Array(methods, new ResolveResult[0],
                                                         (Function<PsiMethod, Object>)PsiElementResolveResult::new);
      }
    } else {
      PsiClass psiClass = getPsiClass();
      if (psiClass != null) {
        final PsiMethod method = resolve(psiClass, propertyName);
        if (method != null) {
          return new ResolveResult[]{new PsiElementResolveResult(method)};
        }
      }
    }

    return ResolveResult.EMPTY_ARRAY;
  }

  @Nullable
  private PsiMethod resolve(final PsiClass psiClass, final String propertyName) {
    final boolean isLast = isLast();
    final PsiMethod method = isLast ? PropertyUtil.findPropertySetter(psiClass, propertyName, false, true) :
                             PropertyUtil.findPropertyGetter(psiClass, propertyName, false, true);
    return method == null || !method.hasModifierProperty(PsiModifier.PUBLIC) ? null : method;
  }

  private boolean isLast() {
    return myReferenceSet.getReferences().size() - 1 == myIndex;
  }

  private boolean isFirst() {
    return myIndex == 0;
  }

  public Object[] getVariants() {
    final SpringModel model = SpringConverterUtil.getSpringModel(myReferenceSet.getContext());
    if (model == null) return EMPTY_ARRAY;

    final Map<String, PsiMethod> properties;
    final Collection<SpringBaseBeanPointer> descendants = model.getDescendants(myReferenceSet.getBean());

    if (!descendants.isEmpty() && isFirst()) {
      final Map<String, Set<PsiMethod>> sharedProperties = getAllSharedProperties(descendants, true);

      properties = new HashMap<String, PsiMethod>();
      for (Map.Entry<String,Set<PsiMethod>> entry : sharedProperties.entrySet()) {
        final String propertyName = entry.getKey();
        final PsiMethod firstMethod = entry.getValue().iterator().next();
        properties.put(propertyName, firstMethod);
      }
    } else {
      final PsiClass psiClass = getPsiClass();
      if (psiClass == null) return EMPTY_ARRAY;
      properties = PropertyUtil.getAllProperties(psiClass, true, !isLast());
    }
    final Object[] variants = new Object[properties.size()];
    int i = 0;
    for (Map.Entry<String, PsiMethod> entry : properties.entrySet()) {
      final String propertyName = entry.getKey();
      final PsiType propertyType = PropertyUtil.getPropertyType(entry.getValue());
      assert propertyType != null;
      variants[i++] = LookupValueFactory.createLookupValueWithHint(propertyName, SpringIcons.SpringProperty, propertyType.getPresentableText());
    }
    return variants;
  }

  public PsiElement handleElementRename(final String newElementName) throws IncorrectOperationException {
    final String name = PropertyUtil.getPropertyName(newElementName);
    return super.handleElementRename(name == null ? newElementName : name);
  }

  public PsiElement bindToElement(@Nonnull final PsiElement element) throws IncorrectOperationException {
    if (element instanceof PsiMethod) {
      final String propertyName = PropertyUtil.getPropertyName((PsiMember)element);
      if (propertyName != null) {
        return super.handleElementRename(propertyName);
      }
    }
    return getElement();
  }

  @Nonnull
  private static Set<PsiClass> getUniqueBeanClasses(@Nonnull final Collection<SpringBaseBeanPointer> beans) {
    if (beans.isEmpty()) return Collections.emptySet();
    final Set<PsiClass> classes = new HashSet<PsiClass>();
    for (SpringBeanPointer bean : beans) {
      final PsiClass psiClass = bean.getBeanClass();
      if (psiClass != null) {
        classes.add(psiClass);
      }
    }
    return classes;
  }

  @Nonnull
  private static <K,V> Map<K,Set<V>> reduce(@Nonnull final Collection<Map<K,V>> maps) {
    final Map<K, Set<V>> intersection = new HashMap<K, Set<V>>();
    final Iterator<Map<K, V>> i = maps.iterator();
    if (i.hasNext()) {
      final Map<K, V> first = i.next();
      for (Map.Entry<K, V> entry : first.entrySet()) {
        final Set<V> values = new HashSet<V>();
        values.add(entry.getValue());
        intersection.put(entry.getKey(), values);
      }

      while (i.hasNext()) {
        final Map<K, V> map = i.next();
        intersection.keySet().retainAll(map.keySet());
        for (Map.Entry<K, Set<V>> entry : intersection.entrySet()) {
          entry.getValue().add(map.get(entry.getKey()));
        }
      }
    }
    return intersection;
  }

  public String getUnresolvedMessagePattern() {
    return SpringBundle.message("model.property.error.message", getValue());
  }

  public LocalQuickFix[] getQuickFixes() {
    final String value = getValue();
    if (StringUtil.isNotEmpty(value)) {
      final PsiClass psiClass = getPsiClass();
      if (psiClass != null) {
        final SpringPropertyDefinition definition = (SpringPropertyDefinition)myReferenceSet.getGenericDomValue().getParent();
        assert definition != null;
        PsiType[] types = definition.getTypesByValue();
        return CreateBeanPropertyFix.createFixes(value, psiClass, types == null || types.length == 0 ? null : types[0], true);
      }
    }
    return LocalQuickFix.EMPTY_ARRAY;
  }
}
