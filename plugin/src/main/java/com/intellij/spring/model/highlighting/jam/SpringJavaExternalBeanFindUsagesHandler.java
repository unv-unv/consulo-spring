package com.intellij.spring.model.highlighting.jam;

import com.intellij.find.findUsages.FindUsagesHandler;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.spring.model.jam.utils.SpringJamUtils;
import com.intellij.spring.model.xml.beans.SpringBaseBeanPointer;
import com.intellij.spring.model.xml.beans.SpringBeanPointer;
import com.intellij.util.containers.ContainerUtil;
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
      ContainerUtil.addIfNotNull(springBean.getPsiElement(), psiElements);
    }
    return psiElements.toArray(new PsiElement[psiElements.size()]);
  }
}
