package consulo.spring.boot;

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
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author VISTALL
 * @since 15-Jan-17
 */
public class AnnotationSpringModel implements SpringModel {
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
    return null;
  }

  @Nullable
  @Override
  public SpringBeanPointer findParentBean(@NonNls @NotNull String beanName) {
    return null;
  }

  @NotNull
  @Override
  public Collection<SpringBaseBeanPointer> getAllDomBeans() {
    return Collections.emptyList();
  }

  @NotNull
  @Override
  public Collection<SpringBaseBeanPointer> getAllDomBeans(boolean withDepenedencies) {
    return Collections.emptyList();
  }

  @NotNull
  @Override
  public Set<String> getAllBeanNames(@NotNull String beanName) {
    return Collections.emptySet();
  }

  @Override
  public boolean isNameDuplicated(@NotNull String beanName) {
    return false;
  }

  @NotNull
  @Override
  public Collection<? extends SpringBaseBeanPointer> getAllCommonBeans(boolean withDepenedencies) {
    return Collections.emptyList();
  }

  @NotNull
  @Override
  public Collection<? extends SpringBaseBeanPointer> getAllCommonBeans() {
    return Collections.emptyList();
  }

  @NotNull
  @Override
  public Collection<? extends SpringBaseBeanPointer> getAllParentBeans() {
    return Collections.emptyList();
  }

  @NotNull
  @Override
  public List<SpringBaseBeanPointer> findBeansByPsiClass(@NotNull PsiClass psiClass) {
    return Collections.emptyList();
  }

  @NotNull
  @Override
  public List<SpringBaseBeanPointer> findBeansByPsiClassWithInheritance(@NotNull PsiClass psiClass) {
    return Collections.emptyList();
  }

  @NotNull
  @Override
  public List<SpringBaseBeanPointer> findBeansByEffectivePsiClassWithInheritance(@NotNull PsiClass psiClass) {
    return Collections.emptyList();
  }

  @NotNull
  @Override
  public List<SpringBaseBeanPointer> getChildren(@NotNull SpringBeanPointer parent) {
    return Collections.emptyList();
  }

  @NotNull
  @Override
  public List<SpringBaseBeanPointer> getDescendants(@NotNull CommonSpringBean context) {
    return Collections.emptyList();
  }

  @Nullable
  @Override
  public Module getModule() {
    return null;
  }

  @Override
  public Collection<SpringBaseBeanPointer> getOwnBeans() {
    return null;
  }

  @Override
  public List<SpringBaseBeanPointer> findQualifiedBeans(@NotNull SpringQualifier qualifier) {
    return null;
  }

  @Override
  public Collection<XmlTag> getCustomBeanCandidates(String id) {
    return null;
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
