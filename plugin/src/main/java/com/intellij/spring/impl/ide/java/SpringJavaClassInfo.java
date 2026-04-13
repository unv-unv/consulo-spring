package com.intellij.spring.impl.ide.java;

import com.intellij.java.language.psi.PsiClass;
import com.intellij.spring.impl.ide.SpringManager;
import com.intellij.spring.impl.ide.SpringModel;
import com.intellij.spring.impl.ide.model.xml.DomSpringBean;
import com.intellij.spring.impl.ide.model.xml.beans.*;
import consulo.application.util.CachedValue;
import consulo.application.util.CachedValueProvider;
import consulo.application.util.CachedValuesManager;
import consulo.application.util.function.Processor;
import consulo.language.util.ModuleUtilCore;
import consulo.module.Module;
import consulo.project.Project;
import consulo.util.collection.MultiMap;
import consulo.util.dataholder.Key;
import consulo.xml.dom.DomManager;

import jakarta.annotation.Nonnull;
import java.util.*;

/**
 * Caches Spring mappings for given PsiClass.
 *
 * @author Dmitry Avdeev
 */
public class SpringJavaClassInfo {

  private static final Key<SpringJavaClassInfo> KEY = new Key<SpringJavaClassInfo>("Spring Java Class Info");

  private final PsiClass myPsiClass;
  private final CachedValue<List<SpringBaseBeanPointer>> myBeans;
  private final CachedValue<MultiMap<String, SpringPropertyDefinition>> myProperties;

  private SpringJavaClassInfo(final PsiClass psiClass) {

    myPsiClass = psiClass;
    final Project project = psiClass.getProject();

    myBeans = CachedValuesManager.getManager(project).createCachedValue(new CachedValueProvider<List<SpringBaseBeanPointer>>() {
      public Result<List<SpringBaseBeanPointer>> compute() {
        final consulo.module.Module module = ModuleUtilCore.findModuleForPsiElement(myPsiClass);
        if (module == null) {
          return null;
        }
        final List<SpringBaseBeanPointer> result = new ArrayList<>();
        ModuleUtilCore.visitMeAndDependentModules(module, new Processor<Module>() {
          public boolean process(final Module module) {
            final SpringModel model = SpringManager.getInstance(project).getCombinedModel(module);
            if (model != null) {
              result.addAll(model.findBeansByEffectivePsiClassWithInheritance(myPsiClass));
              return true;
            }
            return true;
          }
        });
        return new Result<>(result, DomManager.getDomManager(project));
      }
    }, false);

    myProperties = CachedValuesManager.getManager(project).createCachedValue(new CachedValueProvider<MultiMap<String, SpringPropertyDefinition>>() {
      public Result<MultiMap<String, SpringPropertyDefinition>> compute() {
        final List<SpringBaseBeanPointer> list = getMappedBeans();
        final MultiMap<String, SpringPropertyDefinition> map = MultiMap.createConcurrent();
        for (SpringBaseBeanPointer beanPointer : list) {
          if (beanPointer instanceof DomSpringBeanPointer domPointer) {
            final DomSpringBean bean = domPointer.getSpringBean();
            if (bean instanceof SpringBean) {
              final List<SpringPropertyDefinition> properties = ((SpringBean)bean).getAllProperties();
              for (SpringPropertyDefinition property : properties) {
                final String propertyName = property.getPropertyName();
                if (propertyName != null) {
                  map.putValue(propertyName, property);
                }
              }
            }
          }
        }
        return new Result<>(map, DomManager.getDomManager(project));
      }
    }, false);
  }

  @Nonnull
  public static SpringJavaClassInfo getSpringJavaClassInfo(final @Nonnull PsiClass psiClass) {
    SpringJavaClassInfo info = psiClass.getUserData(KEY);
    if (info == null) {
      info = new SpringJavaClassInfo(psiClass);
      psiClass.putUserData(KEY, info);
    }
    return info;
  }

  public boolean isMapped() {
    return getMappedBeans().size() > 0;
  }

  @Nonnull
  public List<SpringBaseBeanPointer> getMappedBeans() {
    final List<SpringBaseBeanPointer> list = myBeans.getValue();
    return list == null ? Collections.emptyList() : list;
  }

  @Nonnull
  public Collection<SpringPropertyDefinition> getMappedProperties(String propertyName) {
    final MultiMap<String, SpringPropertyDefinition> value = myProperties.getValue();
    if (value == null) {
      return Collections.emptyList();
    }
    return value.get(propertyName);
  }
}
