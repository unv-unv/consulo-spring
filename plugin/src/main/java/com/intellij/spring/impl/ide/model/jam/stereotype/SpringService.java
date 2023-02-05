package com.intellij.spring.impl.ide.model.jam.stereotype;

import com.intellij.jam.reflect.JamClassMeta;
import com.intellij.java.language.psi.PsiClass;
import com.intellij.spring.impl.ide.constants.SpringAnnotationsConstants;

import javax.annotation.Nonnull;

public abstract class SpringService extends SpringStereotypeElement {
  public static final JamClassMeta<SpringService> META = new JamClassMeta<SpringService>(SpringService.class);

  public SpringService(@Nonnull PsiClass psiClass) {
    super(SpringAnnotationsConstants.SERVICE_ANNOTATION, psiClass);
  }
}
