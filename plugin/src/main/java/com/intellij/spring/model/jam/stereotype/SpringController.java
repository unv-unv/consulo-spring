package com.intellij.spring.model.jam.stereotype;

import com.intellij.psi.PsiClass;
import com.intellij.spring.constants.SpringAnnotationsConstants;
import com.intellij.jam.reflect.JamClassMeta;
import javax.annotation.Nonnull;

public abstract class SpringController extends SpringStereotypeElement {
  public static final JamClassMeta<SpringController> META = new JamClassMeta<SpringController>(SpringController.class);

  public SpringController(@Nonnull PsiClass psiClass) {
    super(SpringAnnotationsConstants.CONTROLLER_ANNOTATION, psiClass);
  }
}
