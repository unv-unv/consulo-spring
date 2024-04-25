package com.intellij.spring.impl.ide.model.jam.stereotype;

import com.intellij.jam.JamStringAttributeElement;
import com.intellij.jam.reflect.JamAnnotationMeta;
import com.intellij.jam.reflect.JamAttributeMeta;
import com.intellij.jam.reflect.JamClassMeta;
import com.intellij.jam.reflect.JamStringAttributeMeta;
import com.intellij.java.language.psi.PsiClass;
import com.intellij.spring.impl.ide.constants.SpringAnnotationsConstants;
import jakarta.annotation.Nonnull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author VISTALL
 * @since 2024-04-13
 */
public abstract class SpringComponentScan extends SpringStereotypeElement {
  public static final JamClassMeta<SpringComponentScan> META = new JamClassMeta<>(SpringComponentScan.class);

  private static final JamStringAttributeMeta.Collection<String> VALUE_ATTRIBUTE_META = JamAttributeMeta.collectionString("value");

  private static final JamStringAttributeMeta.Collection<String> BASE_PACKAGES_ATTRIBUTE_META =
    JamAttributeMeta.collectionString("basePackages");

  private static final JamAnnotationMeta ANNOTATION_META = new JamAnnotationMeta(SpringAnnotationsConstants.COMPONENT_SCAN_ANNOTATION);

  public SpringComponentScan(@Nonnull PsiClass psiClass) {
    super(ANNOTATION_META, psiClass);
  }

  public Set<String> getBasePackages() {
    Set<String> values = new HashSet<>();
    List<JamStringAttributeElement<String>> attribute = myMeta.getAttribute(myPsiClass, VALUE_ATTRIBUTE_META);
    for (JamStringAttributeElement<String> attributeElement : attribute) {
      values.add(attributeElement.getValue());
    }
    attribute = myMeta.getAttribute(myPsiClass, BASE_PACKAGES_ATTRIBUTE_META);
    for (JamStringAttributeElement<String> attributeElement : attribute) {
      values.add(attributeElement.getValue());
    }
    return values;
  }
}
