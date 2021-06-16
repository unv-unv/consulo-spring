package com.intellij.spring.aop;

import com.intellij.aop.AopIntroduction;
import com.intellij.aop.jam.AopJavaAnnotator;
import com.intellij.psi.PsiClass;
import com.intellij.spring.model.SpringBeanEffectiveTypeProvider;
import com.intellij.spring.model.xml.CommonSpringBean;
import com.intellij.util.containers.ContainerUtil;

import javax.annotation.Nonnull;
import java.util.*;

public class SpringAopEffectiveTypesProvider extends SpringBeanEffectiveTypeProvider {


  public void processEffectiveTypes(@Nonnull final CommonSpringBean bean, final Collection<PsiClass> result) {
    final Set<PsiClass> toAdd = new HashSet<PsiClass>();
    List<PsiClass> toRemove = new ArrayList<>();
    for (final PsiClass psiClass : result) {
      for (final AopIntroduction introduction : AopJavaAnnotator.getBoundIntroductions(psiClass)) {
        ContainerUtil.addIfNotNull(introduction.getImplementInterface().getValue(), toAdd);
      }
   }
    result.addAll(toAdd);
    result.removeAll(toRemove);
  }


  
}