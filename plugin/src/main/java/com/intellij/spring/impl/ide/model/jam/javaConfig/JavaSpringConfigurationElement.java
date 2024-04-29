/*
 * Copyright 2000-2009 JetBrains s.r.o.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.intellij.spring.impl.ide.model.jam.javaConfig;

import com.intellij.jam.reflect.JamAnnotationMeta;
import com.intellij.jam.reflect.JamChildrenQuery;
import com.intellij.jam.reflect.JamClassMeta;
import com.intellij.jam.reflect.JamMethodMeta;
import com.intellij.java.language.codeInsight.AnnotationUtil;
import com.intellij.java.language.psi.*;
import com.intellij.spring.impl.ide.constants.SpringAnnotationsConstants;
import consulo.application.util.CachedValueProvider;
import consulo.application.util.CachedValuesManager;
import consulo.language.pom.PomTarget;
import consulo.language.psi.PsiElementRef;
import consulo.language.psi.PsiModificationTracker;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public abstract class JavaSpringConfigurationElement extends SpringJamElement {
  public static final JamMethodMeta<JavaSpringJavaBean> BEANS_METHOD_META =
    new JamMethodMeta<>(JavaSpringJavaBean.class).addAnnotation(JavaSpringJavaBean.META);

  static {
    BEANS_METHOD_META.addPomTargetProducer((javaSpringJavaBean, pomTargetConsumer) -> {
      for (PomTarget pomTarget : javaSpringJavaBean.getPomTargets()) {
        pomTargetConsumer.accept(pomTarget);
      }
    });
  }

  protected static final JamChildrenQuery<JavaSpringJavaBean> BEANS_QUERY =
    JamChildrenQuery.annotatedMethods(JavaSpringJavaBean.META, BEANS_METHOD_META);

  private static final JamAnnotationMeta ANNOTATION_META =
    new JamAnnotationMeta(SpringAnnotationsConstants.SPRING_CONFIGURATION_ANNOTATION);

  public static final JamClassMeta<JavaSpringConfigurationElement> META =
    new JamClassMeta<>(JavaSpringConfigurationElement.class).addChildrenQuery(BEANS_QUERY);

  public JavaSpringConfigurationElement() {
    super(ANNOTATION_META);
  }

  protected JavaSpringConfigurationElement(@Nonnull JamAnnotationMeta annotationMeta) {
    super(annotationMeta);
  }

  @Override
  public List<? extends SpringJavaBean> getBeans() {
    PsiClass psiElement = getPsiElement();
    return CachedValuesManager.getManager(psiElement.getProject()).getCachedValue(psiElement, () -> {
      return CachedValueProvider.Result.create(calcBeans(), PsiModificationTracker.MODIFICATION_COUNT);
    });
  }

  @Nonnull
  public List<PsiClass> getImportedClasses() {
    PsiClass psiElement = getPsiElement();
    return CachedValuesManager.getManager(psiElement.getProject()).getCachedValue(psiElement, () -> {
      return CachedValueProvider.Result.create(getImportedClassesImpl(), PsiModificationTracker.MODIFICATION_COUNT);
    });
  }

  @Nonnull
  private List<PsiClass> getImportedClassesImpl() {
    PsiClass psiClass = getPsiClass();

    PsiAnnotation annotation = AnnotationUtil.findAnnotation(psiClass, SpringAnnotationsConstants.IMPORT_ANNOTATION);
    if (annotation != null) {
      PsiAnnotationMemberValue value = annotation.findAttributeValue(PsiAnnotation.DEFAULT_REFERENCED_METHOD_NAME);

      return resolveClasses(value);
    }

    return List.of();
  }

  private List<? extends SpringJavaBean> calcBeans() {
    PsiClass psiClass = getPsiClass();
    List<JavaSpringJavaBean> children = BEANS_QUERY.findChildren(PsiElementRef.real(psiClass));
    List<PsiClass> importedClasses = getImportedClasses();
    if (!importedClasses.isEmpty()) {
      children = new ArrayList<>(children);

      for (PsiClass aClass : importedClasses) {
        ImportConfigurationElement element = new ImportConfigurationElement(aClass);

        List otherBeans = element.getBeans();
        children.addAll(otherBeans);
      }
    }

    return children;
  }

  private List<PsiClass> resolveClasses(PsiAnnotationMemberValue value) {
    if (value == null) {
      return List.of();
    }

    if (value instanceof PsiClassObjectAccessExpression expression) {
      PsiType type = expression.getOperand().getType();

      if (type instanceof PsiClassType psiClassType) {
        PsiClass resolved = ((PsiClassType)type).resolve();
        if (resolved != null) {
          return List.of(resolved);
        }
      }
    }
    else if (value instanceof PsiArrayInitializerMemberValue memberValue) {
      PsiAnnotationMemberValue[] initializers = memberValue.getInitializers();

      List<PsiClass> classes = new ArrayList<>(initializers.length);
      for (PsiAnnotationMemberValue initializer : initializers) {
        classes.addAll(resolveClasses(initializer));
      }

      return classes;
    }

    return List.of();
  }
}