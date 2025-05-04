package com.intellij.spring.impl.ide.model.highlighting;

import com.intellij.java.analysis.impl.codeInspection.BaseJavaLocalInspectionTool;
import com.intellij.java.language.psi.PsiClass;
import com.intellij.java.language.psi.PsiIdentifier;
import com.intellij.java.language.psi.PsiMethod;
import com.intellij.java.language.psi.util.PropertyUtil;
import com.intellij.spring.impl.ide.SpringBundle;
import com.intellij.spring.impl.ide.constants.SpringAnnotationsConstants;
import com.intellij.spring.impl.ide.java.SpringJavaClassInfo;
import com.intellij.spring.impl.ide.model.xml.DomSpringBean;
import com.intellij.spring.impl.ide.model.xml.beans.DomSpringBeanPointer;
import com.intellij.spring.impl.ide.model.xml.beans.SpringBean;
import com.intellij.spring.impl.ide.model.xml.beans.SpringPropertyDefinition;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.editor.inspection.LocalQuickFix;
import consulo.language.editor.inspection.ProblemDescriptor;
import consulo.language.editor.inspection.ProblemHighlightType;
import consulo.language.editor.inspection.scheme.InspectionManager;
import consulo.language.editor.rawHighlight.HighlightDisplayLevel;
import consulo.language.psi.PsiFile;
import consulo.project.Project;
import consulo.virtualFileSystem.ReadonlyStatusHandler;
import consulo.virtualFileSystem.VirtualFile;
import jakarta.annotation.Nonnull;

import java.util.*;

/**
 * @author Dmitry Avdeev
 */
@ExtensionImpl
public class SpringRequiredAnnotationInspection extends BaseJavaLocalInspectionTool<Object> {

  @Override
  public ProblemDescriptor[] checkMethod(@Nonnull final PsiMethod method, @Nonnull final InspectionManager manager,
                                         final boolean isOnTheFly, Object state) {

    if (PropertyUtil.isSimplePropertySetter(method)) {
      PsiClass containingClass = method.getContainingClass();
      if (containingClass == null) {
        return null;
      }

      final SpringJavaClassInfo info = SpringJavaClassInfo.getSpringJavaClassInfo(containingClass);
      if (info.isMapped()) {
        if (method.getModifierList().findAnnotation(SpringAnnotationsConstants.REQUIRED_ANNOTATION) != null) {
          final String property = PropertyUtil.getPropertyNameBySetter(method);
          final Collection<SpringPropertyDefinition> mappedProperties = info.getMappedProperties(property);
          if (mappedProperties.isEmpty()) {
            final List<DomSpringBeanPointer> list = info.getMappedBeans();
            final List<SpringBean> beans = new ArrayList<>(list.size());
            for (DomSpringBeanPointer pointer : list) {
              final DomSpringBean springBean = pointer.getSpringBean();
              if (springBean instanceof SpringBean && !((SpringBean)springBean).isAbstract()) {
                beans.add((SpringBean)springBean);
              }
            }
            if (beans.isEmpty()) {
              return null;
            }

            final LocalQuickFix fix = new LocalQuickFix() {
              @Override
              @Nonnull
              public String getName() {
                return SpringBundle.message("create.missing.mappings", property);
              }

              @Override
              @Nonnull
              public String getFamilyName() {
                return getName();
              }

              @Override
              public void applyFix(@Nonnull final Project project, @Nonnull final ProblemDescriptor descriptor) {
                final Set<VirtualFile> files = new HashSet<>();
                for (SpringBean bean : beans) {
                  final PsiFile psiFile = bean.getContainingFile();
                  if (psiFile != null) {
                    files.add(psiFile.getVirtualFile());
                  }
                }
                if (!ReadonlyStatusHandler.getInstance(project).ensureFilesWritable(files).hasReadonlyFiles()) {
                  for (SpringBean bean : beans) {
                    bean.addProperty().getName().setStringValue(property);
                  }
                }
              }
            };
            final PsiIdentifier psiIdentifier = method.getNameIdentifier();
            assert psiIdentifier != null;
            final ProblemDescriptor descriptor = manager
              .createProblemDescriptor(psiIdentifier, SpringBundle.message("required.property.not.mapped", property), fix,
                                       ProblemHighlightType.GENERIC_ERROR_OR_WARNING);
            return new ProblemDescriptor[]{descriptor};
          }
        }
      }
    }

    return null;
  }

  @Override
  @Nonnull
  public String getGroupDisplayName() {
    return SpringBundle.message("model.inspection.group.name");
  }

  @Override
  @Nonnull
  public String getDisplayName() {
    return SpringBundle.message("required.properties.inspection");
  }

  @Override
  @Nonnull
  public String getShortName() {
    return "SpringRequiredAnnotationInspection";
  }

  @Override
  public boolean isEnabledByDefault() {
    return true;
  }

  @Override
  @Nonnull
  public HighlightDisplayLevel getDefaultLevel() {
    return HighlightDisplayLevel.ERROR;
  }
}
