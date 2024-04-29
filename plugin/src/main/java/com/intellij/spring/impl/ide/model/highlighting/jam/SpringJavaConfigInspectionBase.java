package com.intellij.spring.impl.ide.model.highlighting.jam;

import com.intellij.jam.JamService;
import com.intellij.jam.model.util.JamCommonUtil;
import com.intellij.java.analysis.impl.codeInspection.BaseJavaLocalInspectionTool;
import com.intellij.java.language.psi.PsiClass;
import com.intellij.java.language.psi.PsiJavaFile;
import com.intellij.spring.impl.ide.SpringBundle;
import com.intellij.spring.impl.ide.model.jam.javaConfig.JavaSpringConfigurationElement;
import com.intellij.spring.impl.ide.model.jam.javaConfig.SpringJamElement;
import consulo.annotation.access.RequiredReadAction;
import consulo.language.editor.inspection.ProblemDescriptor;
import consulo.language.editor.inspection.ProblemsHolder;
import consulo.language.editor.inspection.scheme.InspectionManager;
import consulo.language.psi.PsiFile;
import consulo.language.sem.SemKey;
import consulo.logging.Logger;
import consulo.module.Module;
import consulo.spring.impl.boot.jam.SpringBootConfigurationElement;
import consulo.spring.impl.module.extension.SpringModuleExtension;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public abstract class SpringJavaConfigInspectionBase extends BaseJavaLocalInspectionTool<Object> {
  private static final Logger LOG = Logger.getInstance(SpringJavaConfigInspectionBase.class);

  @Override
  @Nonnull
  public String getGroupDisplayName() {
    return SpringBundle.message("model.inspection.group.name");
  }

  @Override
  public boolean isEnabledByDefault() {
    return true;
  }

  @Override
  @Nullable
  @RequiredReadAction
  public ProblemDescriptor[] checkFile(@Nonnull PsiFile file, @Nonnull InspectionManager manager, boolean isOnTheFly, Object state) {
    if (JamCommonUtil.isPlainJavaFile(file)) {
      final consulo.module.Module module = file.getModule();
      if (module != null && SpringModuleExtension.getInstance(module) != null) {

        final ProblemsHolder holder = new ProblemsHolder(manager, file, isOnTheFly);

        checkJavaFile((PsiJavaFile)file, holder, isOnTheFly, module);

        final List<ProblemDescriptor> problemDescriptors = holder.getResults();
        if (problemDescriptors != null) return problemDescriptors.toArray(new ProblemDescriptor[problemDescriptors.size()]);
      }
    }
    return null;
  }

  protected void checkJavaFile(@Nonnull final PsiJavaFile javaFile,
                               @Nonnull final ProblemsHolder holder,
                               final boolean isOnTheFly,
                               @Nonnull consulo.module.Module module) {
    for (PsiClass psiClass : javaFile.getClasses()) {
      checkClassInternal(psiClass, holder, module);
    }
  }

  private void checkClassInternal(final PsiClass aClass, final ProblemsHolder holder, @Nonnull Module module) {
    checkClass(aClass, holder, module);
    for (PsiClass psiClass : aClass.getInnerClasses()) {
      checkClass(psiClass, holder, module);
    }
  }

  protected void checkClass(final PsiClass aClass, final ProblemsHolder holder, @Nonnull Module module) {
    SpringJamElement configuration = getJavaConfiguration(aClass, module);

    if (configuration != null) {
      checkJavaConfiguration(configuration, module, holder);
    }
  }

  @Nullable
  protected SpringJamElement getJavaConfiguration(PsiClass aClass, consulo.module.Module module) {
    SpringJamElement javaSpringConfiguration = getJavaConfiguration(aClass, module, JavaSpringConfigurationElement.META.getJamKey());
    if (javaSpringConfiguration != null) {
      return javaSpringConfiguration;
    }

    javaSpringConfiguration = getJavaConfiguration(aClass, module, SpringBootConfigurationElement.META.getJamKey());
    if (javaSpringConfiguration != null) {
      return javaSpringConfiguration;
    }

    return null;
  }

  private <T extends SpringJamElement> T getJavaConfiguration(PsiClass aClass, consulo.module.Module module, SemKey<T> jamKey) {
    return JamService.getJamService(module.getProject()).getJamElement(jamKey, aClass);
  }

  protected abstract void checkJavaConfiguration(final SpringJamElement javaConfiguration,
                                                 final Module module,
                                                 final ProblemsHolder holder);
}

