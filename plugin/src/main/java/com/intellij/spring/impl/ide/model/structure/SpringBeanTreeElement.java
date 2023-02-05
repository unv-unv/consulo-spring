package com.intellij.spring.impl.ide.model.structure;

import com.intellij.java.language.psi.PsiClass;
import com.intellij.spring.impl.ide.SpringIcons;
import com.intellij.spring.impl.ide.factories.SpringFactoryBeansManager;
import com.intellij.spring.impl.ide.model.SpringUtils;
import com.intellij.spring.impl.ide.model.xml.CustomBeanWrapper;
import com.intellij.spring.impl.ide.model.xml.DomSpringBean;
import com.intellij.spring.impl.ide.model.xml.beans.ConstructorArg;
import com.intellij.spring.impl.ide.model.xml.beans.SpringPropertyDefinition;
import consulo.fileEditor.structureView.StructureViewTreeElement;
import consulo.fileEditor.structureView.tree.TreeElement;
import consulo.navigation.ItemPresentation;
import consulo.ui.image.Image;
import consulo.util.lang.StringUtil;
import consulo.xml.ide.structureView.impl.xml.XmlTagTreeElement;
import consulo.xml.util.xml.DomElementNavigationProvider;
import org.jetbrains.annotations.NonNls;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SpringBeanTreeElement implements StructureViewTreeElement, ItemPresentation {

  private final DomSpringBean myBean;
  private final DomElementNavigationProvider myNavigationProvider;
  private final boolean myShowBeanStructure;
  @NonNls
  private static final String UNKNOWN_BEAN_NAME = "Bean";

  public SpringBeanTreeElement(final DomSpringBean springBean,
                               final DomElementNavigationProvider navigationProvider,
                               final boolean showBeanStructure) {
    myBean = springBean;
    myNavigationProvider = navigationProvider;
    myShowBeanStructure = showBeanStructure;
  }


  public Object getValue() {
    return myBean.isValid() ? myBean.getXmlElement() : null;
  }

  public ItemPresentation getPresentation() {
    return this;
  }

  public String getPresentableText() {
    if (!myBean.isValid()) return "";

    String name = myBean.getBeanName();
    final PsiClass[] psiClass = getPsiClasses();
    String psiClassName = StringUtil.join(Arrays.asList(psiClass), it -> {
      if (it == null) return "";
      final String s = it.getName();
      return s == null ? "" : s;
    }, ",");
    if (StringUtil.isEmptyOrSpaces(name)) {
      name = psiClassName.length() > 0 ? psiClassName : UNKNOWN_BEAN_NAME;
    }
    else {
      if (psiClassName.length() > 0) {
        name += ": " + psiClassName;
      }
    }
    return name;
  }

  private PsiClass[] getPsiClasses() {
    final PsiClass beanClass = myBean.getBeanClass();
    if (beanClass != null && SpringFactoryBeansManager.isBeanFactory(beanClass)) {
      final PsiClass[] classes = SpringFactoryBeansManager.getInstance().getProductTypes(beanClass, myBean);
      if (classes.length > 0) {
        return classes;
      }
    }
    return new PsiClass[]{beanClass};
  }

  public String getLocationString() {
    return null;
  }

  public Image getIcon() {
    final Image icon = myBean.getPresentation().getIcon();
    return icon == null ? SpringIcons.SPRING_BEAN_ICON : icon;
  }

  public TreeElement[] getChildren() {
    if (myBean instanceof CustomBeanWrapper && SpringUtils.getConstructorArgs(myBean).isEmpty() &&
      SpringUtils.getProperties(myBean).isEmpty() && ((CustomBeanWrapper)myBean).getCustomBeans().isEmpty()) {
      return new XmlTagTreeElement(myBean.getXmlTag()).getChildren();
    }

    if (!myShowBeanStructure) return new TreeElement[0];

    List<SpringInjectionTreeElement> children = new ArrayList<SpringInjectionTreeElement>();
    for (ConstructorArg arg : SpringUtils.getConstructorArgs(myBean)) {
      children.add(new SpringInjectionTreeElement(arg));
    }
    for (SpringPropertyDefinition property : SpringUtils.getProperties(myBean)) {
      children.add(new SpringInjectionTreeElement(property));
    }

    return children.toArray(new SpringInjectionTreeElement[children.size()]);
  }

  public void navigate(final boolean requestFocus) {
    if (myNavigationProvider != null) {
      myNavigationProvider.navigate(myBean, requestFocus);
    }
  }

  public boolean canNavigate() {
    return myBean.isValid() && myNavigationProvider != null && myNavigationProvider.canNavigate(myBean);
  }

  public boolean canNavigateToSource() {
    return myBean.isValid() && myNavigationProvider != null && myNavigationProvider.canNavigate(myBean);
  }

  public DomSpringBean getBean() {
    return myBean;
  }
}
