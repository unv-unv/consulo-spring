package com.intellij.spring.model.jam.stereotype;

import javax.annotation.Nonnull;

import com.intellij.psi.PsiClass;
import com.intellij.spring.constants.SpringAnnotationsConstants;
import com.intellij.jam.reflect.JamClassMeta;

public abstract class SpringRepository extends SpringStereotypeElement {
  public static final JamClassMeta<SpringRepository> META = new JamClassMeta<SpringRepository>(SpringRepository.class);

  public SpringRepository(@Nonnull PsiClass psiClass) {
    super(SpringAnnotationsConstants.REPOSITORY_ANNOTATION, psiClass);
  }
}
