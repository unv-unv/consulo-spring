package com.intellij.javaee.model.annotations;

import com.intellij.psi.PsiAnnotation;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author VISTALL
 * @since 14-Jan-17
 */
public class AnnotationModelUtil {

  @NotNull
  public static AnnotationGenericValue<String> getStringValue(PsiAnnotation annotation, String value, String defaultValue) {
    throw new UnsupportedOperationException();
  }

  public static <T> List<AnnotationGenericValue<T>> getEnumArrayValue(PsiAnnotation psiAnnotation, String value, Class<T> c) {
    throw new UnsupportedOperationException();
  }

  public static AnnotationGenericValue<Boolean> getBooleanValue(PsiAnnotation autowiredAnnotation, String required, boolean defaultValue) {
    throw new UnsupportedOperationException();
  }
}
