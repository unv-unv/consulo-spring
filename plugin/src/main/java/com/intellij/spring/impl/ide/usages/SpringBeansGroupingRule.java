package com.intellij.spring.impl.ide.usages;

import com.intellij.spring.impl.ide.SpringBundle;
import com.intellij.spring.impl.ide.SpringManager;
import com.intellij.spring.impl.ide.model.SpringUtils;
import com.intellij.spring.impl.ide.model.xml.DomSpringBean;
import consulo.dataContext.DataSink;
import consulo.dataContext.TypeSafeDataProvider;
import consulo.language.editor.LangDataKeys;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.logging.Logger;
import consulo.project.Project;
import consulo.spring.impl.SpringIcons;
import consulo.ui.image.Image;
import consulo.usage.Usage;
import consulo.usage.UsageGroup;
import consulo.usage.UsageInfo;
import consulo.usage.UsageView;
import consulo.usage.rule.PsiElementUsage;
import consulo.usage.rule.UsageGroupingRule;
import consulo.util.dataholder.Key;
import consulo.virtualFileSystem.status.FileStatus;
import consulo.virtualFileSystem.status.FileStatusManager;
import consulo.xml.psi.xml.XmlElement;
import consulo.xml.psi.xml.XmlFile;
import consulo.xml.util.xml.DomElement;
import consulo.xml.util.xml.DomUtil;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public class SpringBeansGroupingRule implements UsageGroupingRule {
  private static final Logger LOG = Logger.getInstance(SpringBeansGroupingRule.class);

  public UsageGroup groupUsage(Usage usage) {
    if (usage instanceof PsiElementUsage) {
      PsiElement psiElement = ((PsiElementUsage)usage).getElement();

      final PsiFile psiFile = psiElement.getContainingFile();
      final Project project = psiElement.getProject();
      if (psiFile instanceof XmlFile && SpringManager.getInstance(project).isSpringBeans((XmlFile)psiFile)) {
        final DomElement domElement = DomUtil.getDomElement(psiElement);
        if (domElement != null) {
          final DomSpringBean springBean = domElement.getParentOfType(DomSpringBean.class, false);
          if (springBean != null) {
            return new SpringBeansUsageGroup(springBean);
          }
        }
      }
    }
    return null;
  }

  private static class SpringBeansUsageGroup implements UsageGroup, TypeSafeDataProvider {
    private final String myName;
    private final DomSpringBean myBean;

    public SpringBeansUsageGroup(@Nonnull DomSpringBean bean) {
      myBean = bean;
      final String beanName = bean.getPresentation().getElementName();
      myName = beanName == null ? SpringBundle.message("spring.bean.with.unknown.name") : beanName;

      update();
    }

    public void update() {
    }

    public boolean equals(final Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      final SpringBeansUsageGroup that = (SpringBeansUsageGroup)o;

      if (!myBean.equals(that.myBean)) return false;
      if (!myName.equals(that.myName)) return false;

      return true;
    }

    public int hashCode() {
      int result;
      result = myName.hashCode();
      result = 31 * result + myBean.hashCode();
      return result;
    }

    public Image getIcon() {
      return SpringIcons.SpringBean;
    }

    @Nonnull
    public String getText(UsageView view) {
      return myName;
    }

    @Nonnull
    public DomSpringBean getBean() {
      return myBean;
    }

    public FileStatus getFileStatus() {
      return isValid() ? FileStatusManager.getInstance(myBean.getPsiManager().getProject())
                                          .getStatus(DomUtil.getFile(getBean()).getVirtualFile()) : null;
    }

    public boolean isValid() {
      return getBean().isValid();
    }

    public void navigate(boolean focus) throws UnsupportedOperationException {
      if (canNavigate()) {
        SpringUtils.navigate(myBean);
      }
    }

    public boolean canNavigate() {
      return isValid();
    }

    public boolean canNavigateToSource() {
      return canNavigate();
    }

    public int compareTo(UsageGroup usageGroup) {
      if (!(usageGroup instanceof SpringBeansUsageGroup)) {
        LOG.error("MethodUsageGroup expected but " + usageGroup.getClass() + " found");
      }

      return myName.compareTo(((SpringBeansUsageGroup)usageGroup).myName);
    }

    public void calcData(final Key<?> key, final DataSink sink) {
      if (!isValid()) return;
      if (LangDataKeys.PSI_ELEMENT == key) {
        final XmlElement element = getPsiElement();
        if (element != null && element.isValid()) {
          sink.put(LangDataKeys.PSI_ELEMENT, element);
        }
      }
      if (UsageView.USAGE_INFO_KEY == key) {
        PsiElement element = getPsiElement();
        if (element != null && element.isValid()) {
          sink.put(UsageView.USAGE_INFO_KEY, new UsageInfo(element));
        }
      }
    }

    @Nullable
    private XmlElement getPsiElement() {
      return getBean().getXmlElement();
    }
  }
}
