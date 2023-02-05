/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */

package com.intellij.spring.impl.ide.model.xml.beans;

import com.intellij.java.language.psi.PsiClass;
import com.intellij.spring.impl.ide.model.xml.CommonSpringBean;
import com.intellij.spring.impl.ide.model.xml.DomSpringBean;
import consulo.application.progress.ProgressManager;
import consulo.application.util.NotNullLazyValue;
import consulo.application.util.NullableLazyValue;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.language.psi.PsiManager;
import consulo.util.lang.Comparing;
import consulo.util.lang.ref.PatchedWeakReference;
import consulo.xml.psi.xml.XmlTag;
import consulo.xml.util.xml.DomAnchor;
import consulo.xml.util.xml.impl.DomAnchorImpl;

import javax.annotation.Nonnull;
import java.lang.ref.WeakReference;

/**
 * @author peter
 */
public class DomSpringBeanPointer extends SpringBaseBeanPointer {
  @Nonnull
  private final DomAnchor<DomSpringBean> myPointer;
  private final NotNullLazyValue<Boolean> myAbstract = new NotNullLazyValue<Boolean>() {
    @Nonnull
    protected Boolean compute() {
      final CommonSpringBean bean = getSpringBean();
      if (bean instanceof SpringBean) {
        final Boolean value = ((SpringBean)bean).getAbstract().getValue();
        return value != null && value.booleanValue();
      }
      return false;
    }
  };
  private final NullableLazyValue<SpringBeanPointer> myParent = new NullableLazyValue<SpringBeanPointer>() {
    protected SpringBeanPointer compute() {
      final CommonSpringBean parent = getSpringBean();
      return parent instanceof SpringBean ? ((SpringBean)parent).getParentBean().getValue() : null;
    }
  };
  private final NullableLazyValue<PsiClass> myBeanClass = new NullableLazyValue<PsiClass>() {
    protected PsiClass compute() {
      return getSpringBean().getBeanClass();
    }
  };

  private WeakReference<DomSpringBean> myCachedValue;

  private DomSpringBeanPointer(@Nonnull final DomSpringBean springBean) {
    super(springBean.getBeanName());
    ProgressManager.getInstance().checkCanceled();
    myCachedValue = new PatchedWeakReference<>(springBean);
    final XmlTag tag = springBean.getXmlTag();
    assert tag != null;
    myPointer = DomAnchorImpl.createAnchor(springBean);
  }

  @Nonnull
  public DomSpringBean getSpringBean() {
    DomSpringBean bean = myCachedValue.get();
    if (bean != null) return bean;

    bean = myPointer.retrieveDomElement();
    assert bean != null : "No bean at pointer";
    myCachedValue = new PatchedWeakReference<DomSpringBean>(bean);
    return bean;
  }

  public boolean isValid() {
    DomSpringBean bean = myCachedValue.get();
    if (bean != null) return bean.isValid();

    bean = myPointer.retrieveDomElement();
    if (bean != null && bean.isValid()) {
      myCachedValue = new PatchedWeakReference<DomSpringBean>(bean);
      return true;
    }
    return false;
  }

  public PsiManager getPsiManager() {
    return PsiManager.getInstance(getContainingFile().getProject());
  }

  public static DomSpringBeanPointer createDomSpringBeanPointer(final @Nonnull DomSpringBean bean) {
    return new DomSpringBeanPointer(bean);
  }

  public synchronized boolean isAbstract() {
    return myAbstract.getValue().booleanValue();
  }

  public synchronized SpringBeanPointer getParentPointer() {
    return myParent.getValue();
  }

  public PsiElement getPsiElement() {
    return getSpringBean().getXmlElement();
  }

  public SpringBeanPointer derive(@Nonnull final String name) {
    return Comparing.equal(name, getName()) ? this : new DerivedSpringBeanPointer(this, name);
  }

  public synchronized PsiClass getBeanClass() {
    return myBeanClass.getValue();
  }

  public PsiFile getContainingFile() {
    return myPointer.getContainingFile();
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (!(o instanceof DomSpringBeanPointer)) return false;

    return myPointer.equals(((DomSpringBeanPointer)o).myPointer);
  }

  @Override
  public int hashCode() {
    return myPointer.hashCode();
  }
}
