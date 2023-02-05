package com.intellij.spring.impl.ide.model.highlighting;

import com.intellij.java.analysis.impl.codeInspection.BaseJavaLocalInspectionTool;
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
import org.jetbrains.annotations.NonNls;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * @author Dmitry Avdeev
 */
@ExtensionImpl
public class SpringRequiredAnnotationInspection extends BaseJavaLocalInspectionTool {

  @Override
  public ProblemDescriptor[] checkMethod(@Nonnull final PsiMethod method, @Nonnull final InspectionManager manager,
                                         final boolean isOnTheFly) {

    if (PropertyUtil.isSimplePropertySetter(method)) {
      final SpringJavaClassInfo info = SpringJavaClassInfo.getSpringJavaClassInfo(method.getContainingClass());
      if (info.isMapped()) {
        if (method.getModifierList().findAnnotation(SpringAnnotationsConstants.REQUIRED_ANNOTATION) != null) {
          final String property = PropertyUtil.getPropertyNameBySetter(method);
          final Collection<SpringPropertyDefinition> mappedProperties = info.getMappedProperties(property);
          if (mappedProperties.isEmpty()) {
            final List<DomSpringBeanPointer> list = info.getMappedBeans();
            final List<SpringBean> beans = new ArrayList<SpringBean>(list.size());
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
              @Nonnull
              public String getName() {
                return SpringBundle.message("create.missing.mappings", property);
              }

              @Nonnull
              public String getFamilyName() {
                return getName();
              }

              public void applyFix(@Nonnull final Project project, @Nonnull final ProblemDescriptor descriptor) {
                final Set<VirtualFile> files = new HashSet<VirtualFile>();
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

  @Nonnull
  public String getGroupDisplayName() {
    return SpringBundle.message("model.inspection.group.name");
  }

  @Nonnull
  public String getDisplayName() {
    return SpringBundle.message("required.properties.inspection");
  }

  @Nonnull
  @NonNls
  public String getShortName() {
    return "SpringRequiredAnnotationInspection";
  }

  public boolean isEnabledByDefault() {
    return true;
  }

  @Nonnull
  public HighlightDisplayLevel getDefaultLevel() {
    return HighlightDisplayLevel.ERROR;
  }
}
