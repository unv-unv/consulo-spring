package consulo.spring.impl.model;

import com.intellij.java.language.psi.PsiClass;
import com.intellij.java.language.psi.util.InheritanceUtil;
import com.intellij.spring.impl.BeanNamesMapper;
import com.intellij.spring.impl.Class2BeansMap;
import com.intellij.spring.impl.ide.SpringModel;
import com.intellij.spring.impl.ide.facet.SpringFileSet;
import com.intellij.spring.impl.ide.model.SpringModelVisitor;
import com.intellij.spring.impl.ide.model.SpringUtils;
import com.intellij.spring.impl.ide.model.jam.javaConfig.SpringJamElement;
import com.intellij.spring.impl.ide.model.jam.javaConfig.SpringJavaBean;
import com.intellij.spring.impl.ide.model.jam.utils.SpringJamUtils;
import com.intellij.spring.impl.ide.model.xml.CommonSpringBean;
import com.intellij.spring.impl.ide.model.xml.CustomBeanWrapper;
import com.intellij.spring.impl.ide.model.xml.SpringQualifier;
import com.intellij.spring.impl.ide.model.xml.beans.Alias;
import com.intellij.spring.impl.ide.model.xml.beans.Beans;
import com.intellij.spring.impl.ide.model.xml.beans.SpringBaseBeanPointer;
import com.intellij.spring.impl.ide.model.xml.beans.SpringBeanPointer;
import consulo.application.progress.ProgressIndicatorProvider;
import consulo.application.util.AtomicNotNullLazyValue;
import consulo.application.util.ConcurrentFactoryMap;
import consulo.module.Module;
import consulo.util.collection.MultiMap;
import consulo.util.collection.SmartList;
import consulo.xml.psi.xml.XmlAttribute;
import consulo.xml.psi.xml.XmlTag;
import consulo.xml.util.xml.DomFileElement;
import consulo.xml.util.xml.DomUtil;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.jetbrains.annotations.NonNls;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author VISTALL
 * @since 2024-04-13
 */
public abstract class BaseSpringModel implements SpringModel {
  private final Map<SpringQualifier, List<SpringBaseBeanPointer>> myBeansByQualifier =
    ConcurrentFactoryMap.createMap(this::computeBeansByQualifier);

  private final Map<PsiClass, List<SpringBaseBeanPointer>> myBeansByClass = ConcurrentFactoryMap.createMap(this::computeBeansByPsiClass);

  private final Class2BeansMap myBeansByEffectiveClassWithInheritance = new Class2BeansMap() {
    @Override
    protected void compute(PsiClass psiClass, List<SpringBaseBeanPointer> pointers) {
      Collection<? extends SpringBaseBeanPointer> beans = getAllCommonBeans();
      for (final SpringBaseBeanPointer bean : beans) {
        for (PsiClass beanClass : bean.getEffectiveBeanType()) {
          if (InheritanceUtil.isInheritorOrSelf(beanClass, psiClass, true)) {
            pointers.add(bean);
          }
        }
      }
    }
  };

  private final AtomicNotNullLazyValue<MultiMap<PsiClass, SpringBaseBeanPointer>> myBeansByClassWithInheritance =
    new AtomicNotNullLazyValue<>() {
      @Nonnull
      @Override
      protected MultiMap<PsiClass, SpringBaseBeanPointer> compute() {
        return computeBeansByPsiClassWithInheritance();
      }
    };

  private final AtomicNotNullLazyValue<MultiMap<String, XmlTag>> myCustomBeanIdCandidates = new AtomicNotNullLazyValue<>() {
    @Nonnull
    @Override
    protected MultiMap<String, XmlTag> compute() {
      final MultiMap<String, XmlTag> map = new MultiMap<>();
      for (final DomFileElement<Beans> element : getRoots()) {
        for (CustomBeanWrapper bean : DomUtil.getDefinedChildrenOfType(element.getRootElement(), CustomBeanWrapper.class)) {
          if (!bean.isParsed()) {
            final XmlTag tag = bean.getXmlTag();
            for (XmlAttribute attribute : tag.getAttributes()) {
              map.putValue(attribute.getDisplayValue(), tag);
            }
          }
        }
      }

      return map;
    }
  };

  private BeanNamesMapper myBeanNamesMapper;

  @Nullable
  private final Module myModule;

  private SpringModel[] myDependencies = EMPTY_ARRAY;

  private final SpringFileSet myFileSet;

  private Collection<? extends SpringBaseBeanPointer> myBeansWithoutDependencies;

  private Collection<? extends SpringBaseBeanPointer> myBeans;

  private final AtomicNotNullLazyValue<Collection<SpringBaseBeanPointer>> myOwnBeans =
    new AtomicNotNullLazyValue<>() {
      @Override
      @Nonnull
      protected Collection<SpringBaseBeanPointer> compute() {
        Collection<SpringBaseBeanPointer> beans = null;
        for (final DomFileElement<Beans> element : getRoots()) {
          final List<CommonSpringBean> springBeanList = SpringUtils.getChildBeans(element.getRootElement(), false);
          if (beans == null) {
            beans = new ArrayList<>(springBeanList.size());
          }
          for (CommonSpringBean bean : springBeanList) {
            beans.add(SpringBeanPointer.createSpringBeanPointer(bean));
          }
        }
        return beans == null ? Collections.<SpringBaseBeanPointer>emptySet() : beans;
      }
    };

  private final AtomicNotNullLazyValue<MultiMap<SpringBaseBeanPointer, SpringBaseBeanPointer>> myDirectInheritorsMap =
    new AtomicNotNullLazyValue<>() {
      @Override
      @Nonnull
      protected MultiMap<SpringBaseBeanPointer, SpringBaseBeanPointer> compute() {
        final MultiMap<SpringBaseBeanPointer, SpringBaseBeanPointer> map = MultiMap.createConcurrent();
        for (final SpringBaseBeanPointer pointer : getAllDomBeans()) {
          final SpringBeanPointer parentPointer = pointer.getParentPointer();
          if (parentPointer != null) {
            map.putValue(parentPointer.getBasePointer(), pointer);
          }
        }
        return map;
      }
    };

  private interface ModelVisitor {


    /**
     * @param model
     * @return false to stop traversing
     */
    boolean visit(SpringModel model);
  }

  public BaseSpringModel(final Module module, final SpringFileSet fileSet) {
    myFileSet = fileSet;
    myModule = module;
  }

  private boolean visitDependencies(final ModelVisitor visitor) {
    for (SpringModel dependency : myDependencies) {
      if (!visitor.visit(dependency)) {
        return false;
      }
      if (dependency instanceof BaseSpringModel baseSpringModel) {
        baseSpringModel.visitDependencies(visitor);
      }
    }
    return true;
  }

  public List<Alias> getAliases(boolean withDeps) {
    final ArrayList<Alias> list = new ArrayList<>();
    final ModelVisitor modelVisitor = model -> {
      for (DomFileElement<Beans> fileElement : model.getRoots()) {
        list.addAll(fileElement.getRootElement().getAliases());
      }
      return true;
    };
    modelVisitor.visit(this);
    if (withDeps) {
      visitDependencies(modelVisitor);
    }
    return list;
  }

  @Override
  @Nonnull
  public String getId() {
    return myFileSet.getId();
  }

  @Override
  public SpringFileSet getFileSet() {
    return myFileSet;
  }

  @Override
  @Nonnull
  public SpringModel[] getDependencies() {
    return myDependencies == null ? EMPTY_ARRAY : myDependencies;
  }

  @Override
  @Nonnull
  public Collection<XmlTag> getCustomBeanCandidates(String id) {
    return myCustomBeanIdCandidates.getValue().get(id);
  }

  public void setDependencies(@Nonnull final SpringModel[] dependencies) {
    myDependencies = dependencies;
  }

  @Override
  @Nullable
  public SpringBeanPointer findBean(@NonNls @Nonnull String beanName) {
    return getBeanNamesMapper().getBean(beanName);
  }

  @Override
  @Nullable
  public SpringBeanPointer findParentBean(@NonNls @Nonnull final String beanName) {
    for (SpringModel dependency : myDependencies) {
      final SpringBeanPointer springBean = dependency.findBean(beanName);
      if (springBean != null) {
        return springBean;
      }
    }
    return null;
  }

  private BeanNamesMapper getBeanNamesMapper() {
    if (myBeanNamesMapper == null) {
      myBeanNamesMapper = new BeanNamesMapper(this);
    }
    return myBeanNamesMapper;
  }

  @Override
  @Nonnull
  public Collection<SpringBaseBeanPointer> getAllDomBeans() {
    return getAllDomBeans(true);
  }

  @Override
  @Nonnull
  public Collection<SpringBaseBeanPointer> getOwnBeans() {
    return myOwnBeans.getValue();
  }

  @Override
  @Nonnull
  public Collection<SpringBaseBeanPointer> getAllDomBeans(boolean withDependencies) {

    final Collection<SpringBaseBeanPointer> ownBeans = getOwnBeans();
    if (withDependencies) {
      final List<SpringBaseBeanPointer> allBeans = new ArrayList<>(ownBeans);
      visitDependencies(model -> {
        allBeans.addAll(model.getOwnBeans());
        return true;
      });
      return allBeans;
    }
    else {
      return ownBeans;
    }
  }

  @Override
  @Nonnull
  public Set<String> getAllBeanNames(@Nonnull final String beanName) {
    return getBeanNamesMapper().getAllBeanNames(beanName);
  }

  @Override
  public boolean isNameDuplicated(@Nonnull final String beanName) {
    return getBeanNamesMapper().isNameDuplicated(beanName);
  }

  @Override
  @Nonnull
  public synchronized Collection<? extends SpringBaseBeanPointer> getAllCommonBeans(final boolean withDepenedencies) {
    if (!withDepenedencies || myDependencies.length == 0) {
      return myBeansWithoutDependencies == null
        ? myBeansWithoutDependencies = calculateBeans(withDepenedencies)
        : myBeansWithoutDependencies;
    }
    else {
      return myBeans == null ? myBeans = calculateBeans(withDepenedencies) : myBeans;
    }
  }

  private Collection<SpringBaseBeanPointer> calculateBeans(final boolean withDepenedencies) {
    Collection<SpringBaseBeanPointer> domBeans = getAllDomBeans(withDepenedencies);
    final Collection<SpringBaseBeanPointer> allBeans = new ArrayList<>(domBeans);

    processNonDomBeans(bean -> {
      ProgressIndicatorProvider.checkCanceled();

      allBeans.add(SpringBeanPointer.createSpringBeanPointer(bean));
    });

    return allBeans;
  }

  private void processNonDomBeans(Consumer<CommonSpringBean> consumer) {
    Consumer<SpringJamElement> elementConsumer = conf -> {
      List<? extends SpringJavaBean> beans = conf.getBeans();
      for (SpringJavaBean javaBean : beans) {
        if (javaBean.isPublic()) {
          consumer.accept(javaBean);
        }
      }
    };

    processBeans(elementConsumer);

    SpringJamUtils.processAllStereotypeJavaBeans(this, consumer);
  }

  protected void processBeans(Consumer<SpringJamElement> consumer) {
    SpringJamUtils.processConfigurations(this, consumer);
  }

  @Override
  @Nonnull
  public Collection<? extends SpringBaseBeanPointer> getAllParentBeans() {
    final Collection<SpringBaseBeanPointer> allBeans = new ArrayList<>();

    visitDependencies(model -> {
      allBeans.addAll(model.getAllCommonBeans());
      return true;
    });

    return allBeans;
  }

  @Override
  @Nonnull
  public List<SpringBaseBeanPointer> findQualifiedBeans(@Nonnull final SpringQualifier qualifier) {
    final List<SpringBaseBeanPointer> pointers = new ArrayList<>(myBeansByQualifier.get(qualifier));
    visitDependencies(model -> {
      pointers.addAll(((BaseSpringModel)model).myBeansByQualifier.get(qualifier));
      return true;
    });
    return pointers;
  }

  private List<SpringBaseBeanPointer> computeBeansByQualifier(final SpringQualifier pair) {
    final List<SpringBaseBeanPointer> beans = new ArrayList<>();
    final Collection<? extends SpringBaseBeanPointer> pointers = getAllCommonBeans(true);
    for (SpringBaseBeanPointer beanPointer : pointers) {
      final CommonSpringBean bean = beanPointer.getSpringBean();
      final SpringQualifier qualifier = bean.getSpringQualifier();
      if (qualifier != null) {
        if (SpringUtils.compareQualifiers(qualifier, pair)) {
          beans.add(beanPointer);
        }
      }
    }
    return beans;
  }


  @Override
  @Nonnull
  public List<SpringBaseBeanPointer> findBeansByPsiClass(@Nonnull final PsiClass psiClass) {
    final List<SpringBaseBeanPointer> pointers = new ArrayList<>(myBeansByClass.get(psiClass));
    visitDependencies(model -> {
      pointers.addAll(((BaseSpringModel)model).myBeansByClass.get(psiClass));
      return true;
    });
    return pointers;
  }

  @Override
  @Nonnull
  public List<SpringBaseBeanPointer> findBeansByPsiClassWithInheritance(@Nonnull final PsiClass psiClass) {
    final ArrayList<SpringBaseBeanPointer> pointers =
      new ArrayList<>(myBeansByClassWithInheritance.getValue().get(psiClass));
    visitDependencies(model -> {
      pointers.addAll(((BaseSpringModel)model).myBeansByClassWithInheritance.getValue().get(psiClass));
      return true;
    });

    return pointers;
  }

  @Override
  @Nonnull
  public List<SpringBaseBeanPointer> findBeansByEffectivePsiClassWithInheritance(@Nonnull final PsiClass psiClass) {
    return collectBeans(psiClass, springModel -> springModel.myBeansByEffectiveClassWithInheritance);
  }

  private List<SpringBaseBeanPointer> collectBeans(final PsiClass psiClass, final Function<BaseSpringModel, Class2BeansMap> getter) {
    final ArrayList<SpringBaseBeanPointer> pointers = new ArrayList<>(getter.apply(this).get(psiClass));
    visitDependencies(model -> {
      pointers.addAll(getter.apply((BaseSpringModel)model).get(psiClass));
      return true;
    });

    return pointers;
  }

  @Override
  @Nonnull
  public List<SpringBaseBeanPointer> getChildren(@Nonnull SpringBeanPointer parent) {
    final SpringBaseBeanPointer baseParent = parent.getBasePointer();
    final ArrayList<SpringBaseBeanPointer> list = new ArrayList<>();
    for (SpringBaseBeanPointer bean : getAllDomBeans()) {
      final SpringBeanPointer pointer = bean.getParentPointer();
      if (pointer != null && pointer.getBasePointer().equals(baseParent)) {
        list.add(bean);
      }
    }
    return list;
  }

  private static void addDescendants(MultiMap<SpringBaseBeanPointer, SpringBaseBeanPointer> map,
                                     SpringBaseBeanPointer current,
                                     Set<SpringBaseBeanPointer> result) {
    final Collection<SpringBaseBeanPointer> pointers = map.get(current);
    for (final SpringBaseBeanPointer pointer : pointers) {
      if (result.add(pointer)) {
        addDescendants(map, pointer, result);
      }
    }
  }

  @Override
  @Nonnull
  public List<SpringBaseBeanPointer> getDescendants(final @Nonnull CommonSpringBean context) {
    final Set<SpringBaseBeanPointer> visited = new HashSet<>();
    final SpringBaseBeanPointer pointer = SpringBeanPointer.createSpringBeanPointer(context);
    visited.add(pointer);
    final MultiMap<SpringBaseBeanPointer, SpringBaseBeanPointer> map = myDirectInheritorsMap.getValue();
    addDescendants(map, pointer, visited);
    return new SmartList<>(visited);
  }

  private List<SpringBaseBeanPointer> computeBeansByPsiClass(@Nonnull final PsiClass psiClass) {
    final List<SpringBaseBeanPointer> beans = new ArrayList<>();
    final Consumer<CommonSpringBean> consumer = bean -> {
      final PsiClass beanClass = bean.getBeanClass();
      if (beanClass != null && beanClass.equals(psiClass)) {
        beans.add(SpringBeanPointer.createSpringBeanPointer(bean));
      }
    };

    processAllBeans(consumer);

    return beans;
  }

  private void processAllBeans(final Consumer<CommonSpringBean> consumer) {
    final SpringModelVisitor visitor = new SpringModelVisitor() {
      @Override
      public boolean visitBean(final CommonSpringBean bean) {
        consumer.accept(bean);
        return true;
      }
    };
    for (final DomFileElement<Beans> element : getRoots()) {
      SpringModelVisitor.visitBeans(visitor, element.getRootElement());
    }
    processNonDomBeans(consumer);
  }

  private MultiMap<PsiClass, SpringBaseBeanPointer> computeBeansByPsiClassWithInheritance() {
    final MultiMap<PsiClass, SpringBaseBeanPointer> result = new MultiMap<>();
    final Consumer<CommonSpringBean> consumer = bean -> {
      final PsiClass beanClass = bean.getBeanClass();
      if (beanClass == null) {
        return;
      }

      final SpringBaseBeanPointer pointer = SpringBeanPointer.createSpringBeanPointer(bean);
      InheritanceUtil.processSupers(beanClass, true, psiClass -> {
        result.putValue(psiClass, pointer);
        return true;
      });
    };

    processAllBeans(consumer);

    return result;
  }

  @Override
  @Nullable
  public Module getModule() {
    return myModule;
  }

  public String toString() {
    return getId();
  }
}
