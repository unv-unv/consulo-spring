package com.intellij.spring.impl.model.beans;

import com.intellij.java.language.psi.PsiClass;
import com.intellij.spring.impl.ide.model.xml.CommonSpringBean;
import com.intellij.spring.impl.ide.model.xml.QualifierAttribute;
import com.intellij.spring.impl.ide.model.xml.beans.SpringBean;
import com.intellij.spring.impl.ide.model.xml.beans.SpringDomQualifier;
import consulo.language.psi.PsiElement;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * @author Dmitry Avdeev
 */
@SuppressWarnings({"AbstractClassNeverImplemented"})
public abstract class SpringDomQualifierImpl implements SpringDomQualifier {

  public PsiClass getQualifierType() {
    return getType().getValue();
  }

  public String getQualifierValue() {
    return getValue().getValue();
  }

  @Nonnull
  public List<? extends QualifierAttribute> getQualifierAttributes() {
    return getAttributes();
  }

  @Nullable
  public CommonSpringBean getQualifiedBean() {
    return getParentOfType(SpringBean.class, false);
  }

  @Nonnull
  public PsiElement getIdentifyingPsiElement() {
    return getXmlTag();
  }
}
