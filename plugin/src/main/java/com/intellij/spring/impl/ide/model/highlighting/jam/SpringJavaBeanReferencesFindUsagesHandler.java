package com.intellij.spring.impl.ide.model.highlighting.jam;

import com.intellij.java.language.psi.PsiMethod;
import com.intellij.spring.impl.ide.model.jam.javaConfig.SpringJavaExternalBean;
import com.intellij.spring.impl.ide.model.jam.utils.SpringJamUtils;
import com.intellij.spring.impl.ide.model.xml.DomSpringBean;
import consulo.find.FindUsagesHandler;
import consulo.language.psi.PsiElement;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SpringJavaBeanReferencesFindUsagesHandler extends FindUsagesHandler {
  private final DomSpringBean mySpringBean;

  public SpringJavaBeanReferencesFindUsagesHandler(final DomSpringBean springBean) {
    super(springBean.getIdentifyingPsiElement());
    mySpringBean = springBean;
  }

  @Nonnull
  public PsiElement[] getSecondaryElements() {
    final List<SpringJavaExternalBean> list = SpringJamUtils.findExternalBeanReferences(mySpringBean);

    Set<PsiElement> psiElements = new HashSet<PsiElement>();

    for (SpringJavaExternalBean externalBean : list) {
      final PsiMethod method = externalBean.getPsiElement();
      if (method != null) {
         psiElements.add(method);
      }
    }
    return psiElements.toArray(new PsiElement[psiElements.size()]);
  }
}
