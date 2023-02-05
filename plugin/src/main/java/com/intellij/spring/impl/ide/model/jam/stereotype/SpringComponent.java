package com.intellij.spring.impl.ide.model.jam.stereotype;

import com.intellij.jam.reflect.JamClassMeta;
import com.intellij.java.language.psi.PsiClass;
import com.intellij.spring.impl.ide.constants.SpringAnnotationsConstants;

import javax.annotation.Nonnull;

public abstract class SpringComponent extends SpringStereotypeElement {
  public static final JamClassMeta<SpringComponent> META = new JamClassMeta<SpringComponent>(SpringComponent.class);

  public SpringComponent(@Nonnull PsiClass psiClass) {
    super(SpringAnnotationsConstants.COMPONENT_ANNOTATION, psiClass);
  }
}
