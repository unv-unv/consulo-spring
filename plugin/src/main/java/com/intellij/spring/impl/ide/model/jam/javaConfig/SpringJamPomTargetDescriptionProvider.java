package com.intellij.spring.impl.ide.model.jam.javaConfig;

import com.intellij.jam.JamElement;
import com.intellij.jam.JamPomTarget;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.pom.PomDescriptionProvider;
import consulo.language.pom.PomTarget;
import consulo.usage.UsageViewNodeTextLocation;
import consulo.usage.UsageViewTypeLocation;
import com.intellij.spring.impl.ide.SpringBundle;
import consulo.language.psi.ElementDescriptionLocation;

import jakarta.annotation.Nonnull;

@ExtensionImpl
public class SpringJamPomTargetDescriptionProvider extends PomDescriptionProvider {

  public String getElementDescription(@Nonnull PomTarget element, @Nonnull ElementDescriptionLocation location) {
    if (element instanceof JamPomTarget) {
      JamPomTarget target = (JamPomTarget)element;
      JamElement jamElement = target.getJamElement();
      if (jamElement instanceof JavaSpringJavaBean) {
        if (location == UsageViewTypeLocation.INSTANCE) {
          return SpringBundle.message("spring.bean");
        }
        if (location == UsageViewNodeTextLocation.INSTANCE) {
          return SpringBundle.message("spring.bean") +" "+ target.getName();
        }
      }
    }
    return null;
  }
}
