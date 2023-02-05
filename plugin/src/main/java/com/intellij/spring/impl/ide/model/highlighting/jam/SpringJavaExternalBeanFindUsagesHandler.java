package com.intellij.spring.impl.ide.model.highlighting.jam;

import com.intellij.java.language.psi.PsiMethod;
import com.intellij.spring.impl.ide.model.jam.utils.SpringJamUtils;
import com.intellij.spring.impl.ide.model.xml.beans.SpringBaseBeanPointer;
import com.intellij.spring.impl.ide.model.xml.beans.SpringBeanPointer;
import consulo.find.FindUsagesHandler;
import consulo.language.psi.PsiElement;
import consulo.util.collection.ContainerUtil;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SpringJavaExternalBeanFindUsagesHandler extends FindUsagesHandler {
  private final PsiMethod myExternalBean;

  public SpringJavaExternalBeanFindUsagesHandler(final PsiMethod externalBean) {
    super(externalBean);
    myExternalBean = externalBean;
  }

  @Nonnull
  public PsiElement[] getSecondaryElements() {
    final List<SpringBaseBeanPointer> list = SpringJamUtils.findExternalBeans(myExternalBean);

    Set<PsiElement> psiElements = new HashSet<PsiElement>();

    for (SpringBeanPointer springBean : list) {
      ContainerUtil.addIfNotNull(psiElements, springBean.getPsiElement());
    }
    return psiElements.toArray(new PsiElement[psiElements.size()]);
  }
}
