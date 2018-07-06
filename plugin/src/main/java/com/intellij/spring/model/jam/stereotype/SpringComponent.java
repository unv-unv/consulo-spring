package com.intellij.spring.model.jam.stereotype;

import javax.annotation.Nonnull;

import com.intellij.jam.reflect.JamClassMeta;
import com.intellij.psi.PsiClass;
import com.intellij.spring.constants.SpringAnnotationsConstants;

public abstract class SpringComponent extends SpringStereotypeElement {
  public static final JamClassMeta<SpringComponent> META = new JamClassMeta<SpringComponent>(SpringComponent.class);

  public SpringComponent(@Nonnull PsiClass psiClass) {
    super(SpringAnnotationsConstants.COMPONENT_ANNOTATION, psiClass);
  }
}
