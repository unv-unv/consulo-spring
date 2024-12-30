package com.intellij.spring.impl.ide.model.jam.stereotype;

import com.intellij.jam.reflect.JamAnnotationMeta;
import com.intellij.jam.reflect.JamClassMeta;
import com.intellij.java.language.psi.PsiClass;
import com.intellij.spring.impl.ide.constants.SpringAnnotationsConstants;

import jakarta.annotation.Nonnull;

public abstract class SpringRepository extends SpringStereotypeElement {
  public static final JamClassMeta<SpringRepository> META = new JamClassMeta<SpringRepository>(SpringRepository.class);

  private static final JamAnnotationMeta ANNOTATION_META = new JamAnnotationMeta(SpringAnnotationsConstants.REPOSITORY_ANNOTATION);

  public SpringRepository(@Nonnull PsiClass psiClass) {
    super(ANNOTATION_META, psiClass);
  }
}
