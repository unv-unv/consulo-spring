package com.intellij.spring.references;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import consulo.util.dataholder.Key;
import com.intellij.patterns.PatternCondition;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.patterns.PsiJavaPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReferenceContributor;
import com.intellij.psi.PsiReferenceRegistrar;
import com.intellij.spring.constants.SpringAnnotationsConstants;
import com.intellij.spring.model.jam.utils.JamAnnotationTypeUtil;
import com.intellij.util.ProcessingContext;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Sergey.Vasiliev
 */
public class SpringStereotypesNameContributor extends PsiReferenceContributor {
  private static final Key<PsiElement> PSI_ELEMENT_KEY = Key.create("PSI_ELEMENT_KEY");

  public void registerReferenceProviders(final PsiReferenceRegistrar registrar) {
    registrar.registerReferenceProvider(PsiJavaPatterns.literalExpression().save(PSI_ELEMENT_KEY).annotationParam(
        PlatformPatterns.string().with(new PatternCondition<String>("stereotypeAnnos") {
          public boolean accepts(@Nonnull final String s, final ProcessingContext context) {
            final PsiElement element = context.get(PSI_ELEMENT_KEY);

            final Module module = ModuleUtil.findModuleForPsiElement(element);

            return getAnnotaionNames(module).contains(s);
          }
        }), "value"), new SpringStereotypesReferenceProvider());
  }

  private static List<String> getAnnotaionNames(@Nullable final Module module) {
    List<String> annos = new ArrayList<String>();

    if (module == null) {
      annos.add(SpringAnnotationsConstants.COMPONENT_ANNOTATION);
      annos.add(SpringAnnotationsConstants.CONTROLLER_ANNOTATION);
      annos.add(SpringAnnotationsConstants.SERVICE_ANNOTATION);
      annos.add(SpringAnnotationsConstants.REPOSITORY_ANNOTATION);
    }
    else {
      annos.addAll(JamAnnotationTypeUtil.getCustomComponentAnnotations(module));
    }

    return annos;
  }
}
