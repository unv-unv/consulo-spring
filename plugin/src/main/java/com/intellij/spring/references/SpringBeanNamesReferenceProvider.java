package com.intellij.spring.references;

import javax.annotation.Nonnull;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiLiteralExpression;
import com.intellij.psi.PsiReference;
import com.intellij.psi.impl.source.resolve.reference.PsiReferenceProviderBase;
import com.intellij.util.ProcessingContext;

public class SpringBeanNamesReferenceProvider extends PsiReferenceProviderBase {
  final public static String[] METHODS = new String[] {"containsBean", "getBean", "isSingleton", "getType", "getAliases"};

  @Nonnull
  public PsiReference[] getReferencesByElement(@Nonnull PsiElement element, @Nonnull final ProcessingContext context) {
    if (element instanceof PsiLiteralExpression) {
      final PsiLiteralExpression literalExpression = (PsiLiteralExpression)element;
      if (literalExpression.getValue() instanceof String) {
        return new PsiReference[] {
          new SpringBeanReference(literalExpression)
        };
      }
    }
    return PsiReference.EMPTY_ARRAY;
  }

}
