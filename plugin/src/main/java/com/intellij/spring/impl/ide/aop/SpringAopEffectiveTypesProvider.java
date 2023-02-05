package com.intellij.spring.impl.ide.aop;

import com.intellij.aop.AopIntroduction;
import com.intellij.aop.jam.AopJavaAnnotator;
import com.intellij.java.language.psi.PsiClass;
import com.intellij.spring.impl.ide.model.SpringBeanEffectiveTypeProvider;
import com.intellij.spring.impl.ide.model.xml.CommonSpringBean;
import consulo.annotation.component.ExtensionImpl;
import consulo.util.collection.ContainerUtil;

import javax.annotation.Nonnull;
import java.util.*;

@ExtensionImpl
public class SpringAopEffectiveTypesProvider extends SpringBeanEffectiveTypeProvider {


  public void processEffectiveTypes(@Nonnull final CommonSpringBean bean, final Collection<PsiClass> result) {
    final Set<PsiClass> toAdd = new HashSet<PsiClass>();
    List<PsiClass> toRemove = new ArrayList<>();
    for (final PsiClass psiClass : result) {
      for (final AopIntroduction introduction : AopJavaAnnotator.getBoundIntroductions(psiClass)) {
        ContainerUtil.addIfNotNull(toAdd, introduction.getImplementInterface().getValue());
      }
   }
    result.addAll(toAdd);
    result.removeAll(toRemove);
  }


  
}