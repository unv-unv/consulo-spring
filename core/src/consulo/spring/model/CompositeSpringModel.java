package consulo.spring.model;

import com.intellij.openapi.module.Module;
import com.intellij.psi.PsiClass;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import com.intellij.spring.SpringModel;
import com.intellij.spring.facet.SpringFileSet;
import com.intellij.spring.model.xml.CommonSpringBean;
import com.intellij.spring.model.xml.SpringQualifier;
import com.intellij.spring.model.xml.beans.Beans;
import com.intellij.spring.model.xml.beans.SpringBaseBeanPointer;
import com.intellij.spring.model.xml.beans.SpringBeanPointer;
import com.intellij.util.xml.DomFileElement;
import gnu.trove.THashSet;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * @author VISTALL
 * @since 15-Jan-17
 */
public class CompositeSpringModel implements SpringModel {
  private Module myModule;
  private List<SpringModel> myModels;

  public CompositeSpringModel(@NotNull Module module, @NotNull List<SpringModel> models) {
    myModule = module;
    myModels = models;
  }

  @NotNull
  @Override
  public String getId() {
    return null;
  }

  @NotNull
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
  public SpringBeanPointer findBean(@NonNls @NotNull String beanName) {
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
  public SpringBeanPointer findParentBean(@NonNls @NotNull String beanName) {
    for (SpringModel model : myModels) {
      SpringBeanPointer bean = model.findParentBean(beanName);
      if (bean != null) {
        return bean;
      }
    }
    return null;
  }

  @NotNull
  @Override
  public Collection<SpringBaseBeanPointer> getAllDomBeans() {
    List<SpringBaseBeanPointer> list = new ArrayList<>();
    for (SpringModel model : myModels) {
      list.addAll(model.getAllDomBeans());
    }
    return list;
  }

  @NotNull
  @Override
  public Collection<SpringBaseBeanPointer> getAllDomBeans(boolean withDepenedencies) {
    List<SpringBaseBeanPointer> list = new ArrayList<>();
    for (SpringModel model : myModels) {
      list.addAll(model.getAllDomBeans(withDepenedencies));
    }
    return list;
  }

  @NotNull
  @Override
  public Set<String> getAllBeanNames(@NotNull String beanName) {
    Set<String> list = new THashSet<>();
    for (SpringModel model : myModels) {
      list.addAll(model.getAllBeanNames(beanName));
    }
    return list;
  }

  @Override
  public boolean isNameDuplicated(@NotNull String beanName) {
    return false;
  }

  @NotNull
  @Override
  public Collection<? extends SpringBaseBeanPointer> getAllCommonBeans(boolean withDepenedencies) {
    Collection<SpringBaseBeanPointer> list = new ArrayList<>();
    for (SpringModel model : myModels) {
      list.addAll(model.getAllCommonBeans(withDepenedencies));
    }
    return list;
  }

  @NotNull
  @Override
  public Collection<? extends SpringBaseBeanPointer> getAllCommonBeans() {
    Collection<SpringBaseBeanPointer> list = new ArrayList<>();
    for (SpringModel model : myModels) {
      list.addAll(model.getAllCommonBeans());
    }
    return list;
  }

  @NotNull
  @Override
  public Collection<? extends SpringBaseBeanPointer> getAllParentBeans() {
    Collection<SpringBaseBeanPointer> list = new ArrayList<>();
    for (SpringModel model : myModels) {
      list.addAll(model.getAllParentBeans());
    }
    return list;
  }

  @NotNull
  @Override
  public List<SpringBaseBeanPointer> findBeansByPsiClass(@NotNull PsiClass psiClass) {
    List<SpringBaseBeanPointer> list = new ArrayList<>();
    for (SpringModel model : myModels) {
      list.addAll(model.findBeansByPsiClass(psiClass));
    }
    return list;
  }

  @NotNull
  @Override
  public List<SpringBaseBeanPointer> findBeansByPsiClassWithInheritance(@NotNull PsiClass psiClass) {
    List<SpringBaseBeanPointer> list = new ArrayList<>();
    for (SpringModel model : myModels) {
      list.addAll(model.findBeansByPsiClassWithInheritance(psiClass));
    }
    return list;
  }

  @NotNull
  @Override
  public List<SpringBaseBeanPointer> findBeansByEffectivePsiClassWithInheritance(@NotNull PsiClass psiClass) {
    List<SpringBaseBeanPointer> list = new ArrayList<>();
    for (SpringModel model : myModels) {
      list.addAll(model.findBeansByEffectivePsiClassWithInheritance(psiClass));
    }
    return list;
  }

  @NotNull
  @Override
  public List<SpringBaseBeanPointer> getChildren(@NotNull SpringBeanPointer parent) {
    List<SpringBaseBeanPointer> list = new ArrayList<>();
    for (SpringModel model : myModels) {
      list.addAll(model.getChildren(parent));
    }
    return list;
  }

  @NotNull
  @Override
  public List<SpringBaseBeanPointer> getDescendants(@NotNull CommonSpringBean context) {
    List<SpringBaseBeanPointer> list = new ArrayList<>();
    for (SpringModel model : myModels) {
      list.addAll(model.getDescendants(context));
    }
    return list;
  }

  @Nullable
  @Override
  public Module getModule() {
    return myModule;
  }

  @NotNull
  @Override
  public Collection<SpringBaseBeanPointer> getOwnBeans() {
    List<SpringBaseBeanPointer> list = new ArrayList<>();
    for (SpringModel model : myModels) {
      list.addAll(model.getOwnBeans());
    }
    return list;
  }

  @NotNull
  @Override
  public List<SpringBaseBeanPointer> findQualifiedBeans(@NotNull SpringQualifier qualifier) {
    List<SpringBaseBeanPointer> list = new ArrayList<>();
    for (SpringModel model : myModels) {
      list.addAll(model.findQualifiedBeans(qualifier));
    }
    return list;
  }

  @NotNull
  @Override
  public Collection<XmlTag> getCustomBeanCandidates(String id) {
    List<XmlTag> list = new ArrayList<>();
    for (SpringModel model : myModels) {
      list.addAll(model.getCustomBeanCandidates(id));
    }
    return list;
  }

  @NotNull
  @Override
  public Set<XmlFile> getConfigFiles() {
    return Collections.emptySet();
  }

  @NotNull
  @Override
  public List<DomFileElement<Beans>> getRoots() {
    return Collections.emptyList();
  }
}
