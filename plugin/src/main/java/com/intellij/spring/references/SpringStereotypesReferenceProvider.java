package com.intellij.spring.references;

import javax.annotation.Nonnull;

import com.intellij.psi.impl.source.resolve.reference.PsiReferenceProviderBase;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ProcessingContext;

/**
 * User: Sergey.Vasiliev
 */
public class SpringStereotypesReferenceProvider extends PsiReferenceProviderBase {

  @Nonnull
  public PsiReference[] getReferencesByElement(@Nonnull PsiElement element, @Nonnull final ProcessingContext context) {
    if (element instanceof PsiLiteralExpression) {
      final PsiLiteralExpression literalExpression = (PsiLiteralExpression)element;
      if (literalExpression.getValue() instanceof String) {

        final PsiAnnotation psiAnnotation = PsiTreeUtil.getParentOfType(element, PsiAnnotation.class);

        if (psiAnnotation != null) {
          return new PsiReference[]{PsiReferenceBase.createSelfReference(element, psiAnnotation)};
        }
      }
    }
    return PsiReference.EMPTY_ARRAY;
  }
}
