package com.intellij.spring.impl.ide.model.jam.stereotype;

import com.intellij.jam.reflect.JamClassMeta;
import com.intellij.java.language.psi.PsiClass;
import com.intellij.spring.impl.ide.constants.SpringAnnotationsConstants;

import javax.annotation.Nonnull;

public abstract class SpringController extends SpringStereotypeElement {
  public static final JamClassMeta<SpringController> META = new JamClassMeta<SpringController>(SpringController.class);

  public SpringController(@Nonnull PsiClass psiClass) {
    super(SpringAnnotationsConstants.CONTROLLER_ANNOTATION, psiClass);
  }
}
