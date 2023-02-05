package com.intellij.spring.impl.ide.refactoring;

import com.intellij.spring.impl.model.beans.SpringBeanImpl;
import com.intellij.spring.impl.ide.model.SpringUtils;
import com.intellij.spring.impl.ide.model.xml.DomSpringBean;
import com.intellij.spring.impl.ide.model.xml.beans.CollectionElements;
import com.intellij.spring.impl.ide.model.xml.beans.SpringBean;
import com.intellij.spring.impl.ide.model.xml.beans.SpringElementsHolder;
import consulo.annotation.component.ExtensionImpl;
import consulo.codeEditor.Editor;
import consulo.language.Language;
import consulo.language.codeStyle.CodeStyleManager;
import consulo.language.editor.refactoring.inline.InlineHandler;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiReference;
import consulo.language.util.IncorrectOperationException;
import consulo.logging.Logger;
import consulo.project.Project;
import consulo.usage.UsageInfo;
import consulo.util.collection.MultiMap;
import consulo.xml.lang.xml.XMLLanguage;
import consulo.xml.psi.xml.XmlAttribute;
import consulo.xml.psi.xml.XmlAttributeValue;
import consulo.xml.psi.xml.XmlTag;
import consulo.xml.util.xml.DomElement;
import consulo.xml.util.xml.DomManager;
import consulo.xml.util.xml.GenericAttributeValue;
import consulo.xml.util.xml.GenericDomValue;
import consulo.xml.util.xml.reflect.DomCollectionChildDescription;
import consulo.xml.util.xml.reflect.DomFixedChildDescription;
import consulo.xml.util.xml.reflect.DomGenericInfo;
import org.jetbrains.annotations.NonNls;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Set;
import java.util.function.Function;

/**
 * @author Dmitry Avdeev
 */
@ExtensionImpl
public class SpringInlineHandler implements InlineHandler {

  private final static Logger LOG = Logger.getInstance(SpringInlineHandler.class);
  @NonNls
  private static final String PARENT_ATTR = "parent";

  public Settings prepareInlineElement(final PsiElement element, final Editor editor, final boolean invokedOnReference) {
    return new Settings() {
      public boolean isOnlyOneReferenceToInline() {
        return false;
      }
    };
  }

  public void removeDefinition(final PsiElement element, Settings settings) {
    final DomElement domElement;
    if (element instanceof XmlTag) {
      domElement = DomManager.getDomManager(element.getProject()).getDomElement((XmlTag)element);
      if (domElement != null) {
        domElement.undefine();
      }
    }
  }

  public Inliner createInliner(final PsiElement element, Settings settings) {
    if (!(element instanceof XmlTag)) {
      return null;
    }
    return new Inliner() {
      public MultiMap<PsiElement, String> getConflicts(final PsiReference reference, final PsiElement referenced) {
        return null;
      }

      public void inlineUsage(final UsageInfo usage, final PsiElement referenced) {
        if (!(referenced instanceof XmlTag)) {
          return;
        }
        final Project project = referenced.getProject();
        final DomManager domManager = DomManager.getDomManager(project);
        final DomSpringBean bean = (DomSpringBean)domManager.getDomElement((XmlTag)referenced);

        PsiElement psiElement = usage.getElement();
        if (psiElement instanceof XmlAttributeValue) {
          final XmlAttribute attribute = (XmlAttribute)psiElement.getParent();
          final GenericAttributeValue value = domManager.getDomElement(attribute);
          assert value != null;
          final DomElement parent = value.getParent();
          assert parent != null;
          if (parent instanceof SpringBean) {
            final String attrName = attribute.getName();
            if (attrName.equals(PARENT_ATTR)) {
              SpringBean thisBean = (SpringBean)parent;
              mergeValue(thisBean, thisBean.getScope());
              mergeValue(thisBean, thisBean.getAbstract());
              mergeValue(thisBean, thisBean.getLazyInit());

              mergeValue(thisBean, thisBean.getAutowireCandidate());
              mergeValue(thisBean, thisBean.getAutowire());
              mergeValue(thisBean, thisBean.getDependencyCheck());
              mergeValue(thisBean, thisBean.getDependsOn());

              mergeValue(thisBean, thisBean.getFactoryBean());
              mergeValue(thisBean, thisBean.getFactoryMethod());
              mergeValue(thisBean, thisBean.getInitMethod());
              mergeValue(thisBean, thisBean.getDestroyMethod());

              mergeValue(thisBean, thisBean.getDescription());

              mergeList(thisBean, SpringBeanImpl.CTOR_ARGS_GETTER, springBean -> springBean.addConstructorArg());
              mergeList(thisBean, SpringBeanImpl.PROPERTIES_GETTER, springBean -> springBean.addProperty());
              mergeList(thisBean, springBean -> springBean.getReplacedMethods(), springBean -> springBean.addReplacedMethod());
              value.undefine();
              reformat(parent);
            }
          }
          else if (parent instanceof SpringElementsHolder) {

            copyBean(bean, parent);
            value.undefine();
            reformat(parent);
          }
          else {
            final DomElement grandParent = parent.getParent();
            if (grandParent instanceof SpringElementsHolder) {
              copyBean(bean, grandParent);
              parent.undefine();
              reformat(grandParent);
            }
            else if (grandParent instanceof CollectionElements) {
              copyBean(bean, grandParent);
              parent.undefine();
              reformat(grandParent);
            }
            else {
              LOG.error("Cannot inline " + attribute);
            }
          }
        }
      }
    };
  }

  private static <T extends GenericDomValue<?>> void mergeValue(SpringBean springBean, T value) {
    final T mergedValue = SpringUtils.getMergedValue(springBean, value);
    if (mergedValue != value) {
      value.setStringValue(mergedValue.getStringValue());
    }
  }

  public static <T extends DomElement> void mergeList(final SpringBean springBean,
                                                      final Function<SpringBean, Collection<T>> getter,
                                                      final Function<SpringBean, T> adder) {
    final Set<T> merged = SpringUtils.getMergedSet(springBean, getter);
    final Collection<T> existing = getter.apply(springBean);
    for (T t : existing) {
      if (!merged.contains(t)) {
        t.undefine();
      }
      else {
        merged.remove(t);
      }
    }
    for (T t : merged) {
      final T newElement = adder.apply(springBean);
      newElement.copyFrom(t);
    }
  }

  private static void reformat(final DomElement domElement) {
    try {
      CodeStyleManager.getInstance(domElement.getManager().getProject()).reformat(domElement.getXmlTag());
    }
    catch (IncorrectOperationException e) {
      LOG.error(e);
    }
  }

  private static void copyBean(final DomSpringBean from, final DomElement parent) {
    final DomCollectionChildDescription description = (DomCollectionChildDescription)from.getChildDescription();
    final DomGenericInfo info = parent.getGenericInfo();
    final String name = description.getXmlElementName();
    final String namespaceKey = description.getXmlName().getNamespaceKey();
    final DomSpringBean to;
    DomCollectionChildDescription targetDescription = info.getCollectionChildDescription(name, namespaceKey);
    if (targetDescription != null) {
      to = (DomSpringBean)targetDescription.addValue(parent);
    }
    else {
      final DomFixedChildDescription fixedDescr = info.getFixedChildDescription(name, namespaceKey);
      assert fixedDescr != null;
      to = (DomSpringBean)fixedDescr.getValues(parent).get(0);
    }

    to.copyFrom(from);
    to.getId().undefine();
    if (to instanceof SpringBean) {
      ((SpringBean)to).getName().undefine();
    }
  }

  @Nonnull
  @Override
  public Language getLanguage() {
    return XMLLanguage.INSTANCE;
  }
}
