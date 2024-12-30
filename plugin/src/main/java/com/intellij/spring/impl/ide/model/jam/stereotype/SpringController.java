package com.intellij.spring.impl.ide.model.jam.stereotype;

import com.intellij.jam.reflect.JamAnnotationMeta;
import com.intellij.jam.reflect.JamClassMeta;
import com.intellij.java.language.psi.PsiClass;
import com.intellij.spring.impl.ide.constants.SpringAnnotationsConstants;

import jakarta.annotation.Nonnull;

public abstract class SpringController extends SpringStereotypeElement {
  public static final JamClassMeta<SpringController> META = new JamClassMeta<SpringController>(SpringController.class);

  private static final JamAnnotationMeta ANNOTATION_META = new JamAnnotationMeta(SpringAnnotationsConstants.CONTROLLER_ANNOTATION);

  public SpringController(@Nonnull PsiClass psiClass) {
    super(ANNOTATION_META, psiClass);
  }
}
