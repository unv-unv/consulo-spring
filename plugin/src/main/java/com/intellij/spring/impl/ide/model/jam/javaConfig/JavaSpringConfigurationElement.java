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
import com.intellij.spring.impl.ide.constants.SpringAnnotationsConstants;
import consulo.language.pom.PomTarget;
import consulo.language.psi.PsiElementRef;

import javax.annotation.Nonnull;
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
    return BEANS_QUERY.findChildren(PsiElementRef.real(getPsiElement()));
  }
}