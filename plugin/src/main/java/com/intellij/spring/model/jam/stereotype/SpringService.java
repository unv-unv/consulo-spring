package com.intellij.spring.model.jam.stereotype;

import com.intellij.psi.PsiClass;
import com.intellij.spring.constants.SpringAnnotationsConstants;
import com.intellij.jam.reflect.JamClassMeta;
import javax.annotation.Nonnull;

public abstract class SpringService extends SpringStereotypeElement {
  public static final JamClassMeta<SpringService> META = new JamClassMeta<SpringService>(SpringService.class);

  public SpringService(@Nonnull PsiClass psiClass) {
    super(SpringAnnotationsConstants.SERVICE_ANNOTATION, psiClass);
  }
}
