package consulo.spring.impl.model;

import com.intellij.java.language.psi.PsiClass;
import com.intellij.spring.impl.ide.SpringModel;
import com.intellij.spring.impl.ide.facet.SpringFileSet;
import com.intellij.spring.impl.ide.model.context.ComponentScan;
import com.intellij.spring.impl.ide.model.xml.CommonSpringBean;
import com.intellij.spring.impl.ide.model.xml.SpringQualifier;
import com.intellij.spring.impl.ide.model.xml.beans.Beans;
import com.intellij.spring.impl.ide.model.xml.beans.SpringBaseBeanPointer;
import com.intellij.spring.impl.ide.model.xml.beans.SpringBeanPointer;
import consulo.module.Module;
import consulo.xml.psi.xml.XmlFile;
import consulo.xml.psi.xml.XmlTag;
import consulo.xml.util.xml.DomFileElement;
import org.jetbrains.annotations.NonNls;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.util.*;

/**
 * @author VISTALL
 * @since 15-Jan-17
 */
public class CompositeSpringModel implements SpringModel {
  private Module myModule;
  private List<SpringModel> myModels;

  public CompositeSpringModel(@Nonnull Module module, @Nonnull List<SpringModel> models) {
    myModule = module;
    myModels = models;
  }

  @Nonnull
  @Override
  public String getId() {
    return null;
  }

  @Nonnull
  @Override
  public SpringModel[] getDependencies() {
    return new SpringModel[0];
  }

  @Override
  public SpringFileSet getFileSet() {
    return null;
  }

  @Nullable
  @Override
  public SpringBeanPointer findBean(@NonNls @Nonnull String beanName) {
    for (SpringModel model : myModels) {
      SpringBeanPointer bean = model.findBean(beanName);
      if (bean != null) {
        return bean;
      }
    }
    return null;
  }

  @Nullable
  @Override
  public SpringBeanPointer findParentBean(@NonNls @Nonnull String beanName) {
    for (SpringModel model : myModels) {
      SpringBeanPointer bean = model.findParentBean(beanName);
      if (bean != null) {
        return bean;
      }
    }
    return null;
  }

  @Nonnull
  @Override
  public Collection<SpringBaseBeanPointer> getAllDomBeans() {
    List<SpringBaseBeanPointer> list = new ArrayList<>();
    for (SpringModel model : myModels) {
      list.addAll(model.getAllDomBeans());
    }
    return list;
  }

  @Nonnull
  @Override
  public Collection<SpringBaseBeanPointer> getAllDomBeans(boolean withDepenedencies) {
    List<SpringBaseBeanPointer> list = new ArrayList<>();
    for (SpringModel model : myModels) {
      list.addAll(model.getAllDomBeans(withDepenedencies));
    }
    return list;
  }

  @Nonnull
  @Override
  public Set<String> getAllBeanNames(@Nonnull String beanName) {
    Set<String> list = new HashSet<>();
    for (SpringModel model : myModels) {
      list.addAll(model.getAllBeanNames(beanName));
    }
    return list;
  }

  @Override
  public boolean isNameDuplicated(@Nonnull String beanName) {
    return false;
  }

  @Nonnull
  @Override
  public Collection<? extends SpringBaseBeanPointer> getAllCommonBeans(boolean withDepenedencies) {
    Collection<SpringBaseBeanPointer> list = new ArrayList<>();
    for (SpringModel model : myModels) {
      list.addAll(model.getAllCommonBeans(withDepenedencies));
    }
    return list;
  }

  @Nonnull
  @Override
  public Collection<? extends SpringBaseBeanPointer> getAllParentBeans() {
    Collection<SpringBaseBeanPointer> list = new ArrayList<>();
    for (SpringModel model : myModels) {
      list.addAll(model.getAllParentBeans());
    }
    return list;
  }

  @Nonnull
  @Override
  public List<SpringBaseBeanPointer> findBeansByPsiClass(@Nonnull PsiClass psiClass) {
    List<SpringBaseBeanPointer> list = new ArrayList<>();
    for (SpringModel model : myModels) {
      list.addAll(model.findBeansByPsiClass(psiClass));
    }
    return list;
  }

  @Nonnull
  @Override
  public List<SpringBaseBeanPointer> findBeansByPsiClassWithInheritance(@Nonnull PsiClass psiClass) {
    List<SpringBaseBeanPointer> list = new ArrayList<>();
    for (SpringModel model : myModels) {
      list.addAll(model.findBeansByPsiClassWithInheritance(psiClass));
    }
    return list;
  }

  @Nonnull
  @Override
  public List<SpringBaseBeanPointer> findBeansByEffectivePsiClassWithInheritance(@Nonnull PsiClass psiClass) {
    List<SpringBaseBeanPointer> list = new ArrayList<>();
    for (SpringModel model : myModels) {
      list.addAll(model.findBeansByEffectivePsiClassWithInheritance(psiClass));
    }
    return list;
  }

  @Nonnull
  @Override
  public List<SpringBaseBeanPointer> getChildren(@Nonnull SpringBeanPointer parent) {
    List<SpringBaseBeanPointer> list = new ArrayList<>();
    for (SpringModel model : myModels) {
      list.addAll(model.getChildren(parent));
    }
    return list;
  }

  @Nonnull
  @Override
  public List<SpringBaseBeanPointer> getDescendants(@Nonnull CommonSpringBean context) {
    List<SpringBaseBeanPointer> list = new ArrayList<>();
    for (SpringModel model : myModels) {
      list.addAll(model.getDescendants(context));
    }
    return list;
  }

  @Nullable
  @Override
  public consulo.module.Module getModule() {
    return myModule;
  }

  @Nonnull
  @Override
  public Collection<SpringBaseBeanPointer> getOwnBeans() {
    List<SpringBaseBeanPointer> list = new ArrayList<>();
    for (SpringModel model : myModels) {
      list.addAll(model.getOwnBeans());
    }
    return list;
  }

  @Nonnull
  @Override
  public List<SpringBaseBeanPointer> findQualifiedBeans(@Nonnull SpringQualifier qualifier) {
    List<SpringBaseBeanPointer> list = new ArrayList<>();
    for (SpringModel model : myModels) {
      list.addAll(model.findQualifiedBeans(qualifier));
    }
    return list;
  }

  @Nonnull
  @Override
  public Collection<XmlTag> getCustomBeanCandidates(String id) {
    List<XmlTag> list = new ArrayList<>();
    for (SpringModel model : myModels) {
      list.addAll(model.getCustomBeanCandidates(id));
    }
    return list;
  }

  @Override
  public List<? extends ComponentScan> getComponentScans() {
    List componentScans = new ArrayList<>();
    for (SpringModel model : myModels) {
      componentScans.addAll(model.getComponentScans());
    }
    return componentScans;
  }

  @Nonnull
  @Override
  public Set<XmlFile> getConfigFiles() {
    return Collections.emptySet();
  }

  @Nonnull
  @Override
  public List<DomFileElement<Beans>> getRoots() {
    return Collections.emptyList();
  }
}
