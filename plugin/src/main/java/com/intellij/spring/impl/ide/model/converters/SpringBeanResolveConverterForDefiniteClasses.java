package com.intellij.spring.impl.ide.model.converters;

import com.intellij.java.language.psi.JavaPsiFacade;
import com.intellij.java.language.psi.PsiClass;
import com.intellij.java.language.psi.PsiClassType;
import consulo.language.psi.PsiManager;
import consulo.language.psi.scope.GlobalSearchScope;
import consulo.xml.util.xml.ConvertContext;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public abstract class SpringBeanResolveConverterForDefiniteClasses extends SpringBeanResolveConverter {

  @Nullable
  protected abstract String[] getClassNames(final ConvertContext context) ;

  @Nullable
  public List<PsiClassType> getRequiredClasses(final ConvertContext context) {
    final List<PsiClassType> required = new ArrayList<PsiClassType>();
    final PsiManager psiManager = context.getPsiManager();
    final String[] strings = getClassNames(context);

    if (strings == null || strings.length == 0) return null;

    for (String className : strings) {
      final PsiClass psiClass = JavaPsiFacade.getInstance(psiManager.getProject())
                                             .findClass(className, GlobalSearchScope.allScope(psiManager.getProject()));

      if (psiClass != null) {
        required.add(JavaPsiFacade.getInstance(psiClass.getProject()).getElementFactory().createType(psiClass));
      }
    }


    return required;
  }
}
