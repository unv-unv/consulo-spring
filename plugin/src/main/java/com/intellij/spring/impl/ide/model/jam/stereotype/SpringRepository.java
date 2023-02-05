package com.intellij.spring.impl.ide.model.jam.stereotype;

import com.intellij.jam.reflect.JamClassMeta;
import com.intellij.java.language.psi.PsiClass;
import com.intellij.spring.impl.ide.constants.SpringAnnotationsConstants;

import javax.annotation.Nonnull;

public abstract class SpringRepository extends SpringStereotypeElement {
  public static final JamClassMeta<SpringRepository> META = new JamClassMeta<SpringRepository>(SpringRepository.class);

  public SpringRepository(@Nonnull PsiClass psiClass) {
    super(SpringAnnotationsConstants.REPOSITORY_ANNOTATION, psiClass);
  }
}
