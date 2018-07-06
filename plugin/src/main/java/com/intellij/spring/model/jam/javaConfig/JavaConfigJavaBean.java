package com.intellij.spring.model.jam.javaConfig;

import com.intellij.jam.reflect.JamAnnotationMeta;
import com.intellij.jam.reflect.JamAttributeMeta;
import com.intellij.jam.reflect.JamStringAttributeMeta;
import com.intellij.psi.PsiAnnotation;
import com.intellij.spring.constants.SpringAnnotationsConstants;
import javax.annotation.Nonnull;

import java.util.List;

public abstract class JavaConfigJavaBean extends SpringJavaBean {
  private static final JamStringAttributeMeta.Collection<String> ALIASES_META = JamAttributeMeta.collectionString("aliases");

  public static JamAnnotationMeta META =
    new JamAnnotationMeta(SpringAnnotationsConstants.JAVA_CONFIG_BEAN_ANNOTATION).addAttribute(ALIASES_META);

  public PsiAnnotation getPsiAnnotation() {
    return META.getAnnotation(getPsiElement());
  }

  @Nonnull
  public String[] getAliases() {
    List<String> aliases = getStringNames(META.getAttribute(getPsiElement(), ALIASES_META));

    return aliases.toArray(new String[aliases.size()]);
  }
}