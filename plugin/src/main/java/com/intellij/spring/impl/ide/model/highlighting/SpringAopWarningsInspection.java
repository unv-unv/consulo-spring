/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */

/*
 * Created by IntelliJ IDEA.
 * User: Sergey.Vasiliev
 * Date: Nov 15, 2006
 * Time: 1:06:27 PM
 */
package com.intellij.spring.impl.ide.model.highlighting;

import com.intellij.aop.AopBundle;
import com.intellij.aop.jam.AopJavaAnnotator;
import com.intellij.java.language.psi.PsiClass;
import com.intellij.spring.impl.ide.SpringBundle;
import com.intellij.spring.impl.ide.model.xml.aop.SpringAspect;
import com.intellij.spring.impl.ide.model.xml.aop.SpringPointcut;
import com.intellij.spring.impl.ide.model.xml.beans.Beans;
import com.intellij.spring.impl.ide.model.xml.beans.SpringBeanPointer;
import consulo.annotation.component.ExtensionImpl;
import consulo.ide.IdeBundle;
import consulo.language.editor.annotation.HighlightSeverity;
import consulo.language.editor.rawHighlight.HighlightDisplayLevel;
import consulo.xml.util.xml.DomElement;
import consulo.xml.util.xml.GenericAttributeValue;
import consulo.xml.util.xml.highlighting.BasicDomElementsInspection;
import consulo.xml.util.xml.highlighting.DomElementAnnotationHolder;
import consulo.xml.util.xml.highlighting.DomHighlightingHelper;
import org.jetbrains.annotations.NonNls;

import javax.annotation.Nonnull;

@ExtensionImpl
public class SpringAopWarningsInspection extends BasicDomElementsInspection<Beans, Object> {

  public SpringAopWarningsInspection() {
    super(Beans.class);
  }

  @Nonnull
  public HighlightDisplayLevel getDefaultLevel() {
    return HighlightDisplayLevel.WARNING;
  }

  protected void checkDomElement(final DomElement element, final DomElementAnnotationHolder holder, final DomHighlightingHelper helper) {
    if (element.getXmlTag() == null) return;

    if (element instanceof SpringPointcut && ((SpringPointcut)element).getId().getXmlAttribute() == null) {
      holder.createProblem(element, HighlightSeverity.WARNING,
                           SpringBundle.message("warning.pointcut.should.have.id"), new DefineAttributeQuickFix("id"));
    }
    else if (element instanceof SpringAspect) {
      final GenericAttributeValue<SpringBeanPointer> ref = ((SpringAspect)element).getRef();
      if (ref.getXmlAttribute() == null) {
        holder.createProblem(element, HighlightSeverity.WARNING,
                             IdeBundle.message("attribute.0.should.be.defined", "ref"), new DefineAttributeQuickFix("ref"));
        return;
      }

      final SpringBeanPointer pointer = ref.getValue();
      if (pointer != null) {
        final PsiClass beanClass = pointer.getBeanClass();
        if (beanClass != null &&
          (!AopJavaAnnotator.getBoundAdvices(beanClass).isEmpty() || !AopJavaAnnotator.getBoundIntroductions(beanClass).isEmpty())) {
          holder.createProblem(ref, HighlightSeverity.WARNING, SpringBundle.message("aop.advice.matching.aspect.bean"));
        }
      }
    }

  }

  @Nonnull
  public String getGroupDisplayName() {
    return AopBundle.message("inspection.group.display.name.aop");
  }

  @Nonnull
  public String getDisplayName() {
    return SpringBundle.message("aop.warnings.inspection.display.name");
  }

  @Nonnull
  @NonNls
  public String getShortName() {
    return "SpringAopWarningsInspection";
  }

}
