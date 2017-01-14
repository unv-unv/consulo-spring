package com.intellij.javaee.model.annotations;

import com.intellij.psi.*;
import consulo.annotations.RequiredReadAction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author VISTALL
 * @since 14-Jan-17
 */
public class AnnotationModelUtil {
  private static class AnnotationGenericValueImpl<T> implements AnnotationGenericValue<T> {

    private T myValue;
    private String myStringValue;

    private AnnotationGenericValueImpl(T myValue, String myStringValue) {
      this.myValue = myValue;
      this.myStringValue = myStringValue;
    }

    @Nullable
    @Override
    public String getStringValue() {
      return myStringValue;
    }

    @Nullable
    @Override
    public T getValue() {
      return myValue;
    }
  }

  @NotNull
  public static AnnotationGenericValue<String> getStringValue(PsiAnnotation annotation, String value, String defaultValue) {
    throw new UnsupportedOperationException();
  }

  @RequiredReadAction
  @SuppressWarnings("unchecked")
  public static <T> List<AnnotationGenericValue<T>> getEnumArrayValue(PsiAnnotation annotation, String name, Class<T> c) {
    List<AnnotationGenericValue<T>> values = new ArrayList<>();

    PsiAnnotationMemberValue attributeValue = annotation.findAttributeValue(name);
    if(attributeValue instanceof PsiArrayInitializerMemberValue) {
      PsiAnnotationMemberValue[] initializers = ((PsiArrayInitializerMemberValue) attributeValue).getInitializers();
      for (PsiAnnotationMemberValue initializer : initializers) {
        if(initializer instanceof PsiReferenceExpression) {
          PsiElement resolve = ((PsiReferenceExpression) initializer).resolve();
          if(resolve instanceof PsiEnumConstant) {
            try {
              String constantName = ((PsiEnumConstant) resolve).getName();
              if(constantName == null) {
                continue;
              }
              Field declaredField = c.getDeclaredField(constantName);
              values.add(new AnnotationGenericValueImpl<>((T) declaredField.get(null), constantName));

            }
            catch (NoSuchFieldException | IllegalAccessException ignored) {
            }
          }
        }
      }
    }
    else {
      throw new UnsupportedOperationException();
    }

    return values;
  }

  public static AnnotationGenericValue<Boolean> getBooleanValue(PsiAnnotation annotation, String required, boolean defaultValue) {
    throw new UnsupportedOperationException();
  }
}
