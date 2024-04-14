package com.intellij.spring.impl.ide.model.jam.stereotype;

import com.intellij.jam.reflect.JamClassMeta;
import com.intellij.java.language.psi.PsiClass;
import com.intellij.spring.impl.ide.constants.SpringAnnotationsConstants;
import jakarta.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 2024-04-13
 */
public abstract class  SpringComponentScan extends SpringStereotypeElement {
  public static final JamClassMeta<SpringComponentScan> META = new JamClassMeta<>(SpringComponentScan.class);

  public SpringComponentScan(@Nonnull PsiClass psiClass) {
    super(SpringAnnotationsConstants.COMPONENT_SCAN_ANNOTATION, psiClass);
  }
}
