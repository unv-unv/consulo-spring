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
import com.intellij.aop.psi.*;
import com.intellij.spring.impl.ide.SpringBundle;
import com.intellij.spring.impl.ide.model.xml.aop.Advisor;
import com.intellij.spring.impl.ide.model.xml.aop.BasicAdvice;
import com.intellij.spring.impl.ide.model.xml.beans.Beans;
import com.intellij.spring.impl.model.aop.psi.SpringAopCompletionContributor;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.editor.annotation.HighlightSeverity;
import consulo.language.editor.inspection.LocalQuickFix;
import consulo.language.editor.inspection.ProblemDescriptor;
import consulo.language.editor.inspection.ProblemHighlightType;
import consulo.language.editor.inspection.scheme.InspectionManager;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.language.psi.PsiRecursiveElementVisitor;
import consulo.util.collection.SmartList;
import consulo.util.lang.StringUtil;
import consulo.xml.psi.xml.XmlTag;
import consulo.xml.util.xml.DomElement;
import consulo.xml.util.xml.highlighting.BasicDomElementsInspection;
import consulo.xml.util.xml.highlighting.DomElementAnnotationHolder;
import consulo.xml.util.xml.highlighting.DomHighlightingHelper;
import org.jetbrains.annotations.NonNls;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

@ExtensionImpl
public class SpringAopErrorsInspection extends BasicDomElementsInspection<Beans, Object> {

  public SpringAopErrorsInspection() {
    super(Beans.class);
  }

  protected void checkDomElement(final DomElement element, final DomElementAnnotationHolder holder, final DomHighlightingHelper helper) {
    final XmlTag tag = element.getXmlTag();
    if (element instanceof BasicAdvice && tag != null) {
      final BasicAdvice advice = (BasicAdvice)element;
      if (advice.getPointcut().getXmlAttribute() == null && advice.getPointcutRef().getXmlAttribute() == null) {
        createPointcutProblem(element, holder);
      }
    }
    else if (element instanceof Advisor && tag != null) {
      final Advisor advisor = (Advisor)element;
      if (advisor.getPointcut().getXmlAttribute() == null && advisor.getPointcutRef().getXmlAttribute() == null) {
        createPointcutProblem(element, holder);
      }
    }
  }

  public ProblemDescriptor[] checkFile(@Nonnull final PsiFile file, @Nonnull final InspectionManager manager, final boolean isOnTheFly) {
    if (file instanceof AopPointcutExpressionFile) {
      final List<ProblemDescriptor> result = new SmartList<ProblemDescriptor>();
      final PsiPointcutExpression expression = ((AopPointcutExpressionFile)file).getPointcutExpression();
      if (expression != null) {
        expression.accept(new PsiRecursiveElementVisitor() {
          @Override
          public void visitElement(final PsiElement element) {
            if (!(element instanceof PsiPointcutExpression)) return;
            super.visitElement(element);
            final PsiElement firstChild = element.getFirstChild();
            if (element instanceof PsiPointcutReferenceExpression || element instanceof AopNotExpression ||
              element instanceof AopBinaryExpression || element instanceof AopParenthesizedExpression) return;
            if (firstChild == null || firstChild.getFirstChild() != null) return;

            @NonNls final String text = firstChild.getText();
            if (StringUtil.isEmptyOrSpaces(text)) return;

            if (!Arrays.asList(SpringAopCompletionContributor.SPRING20_AOP_POINTCUTS).contains(text) && !"bean".equals(text)) {
              result.add(manager.createProblemDescriptor(firstChild,
                                                         SpringBundle.message("this.pointcut.designator.isn.t.supported.by.spring", text),
                                                         LocalQuickFix.EMPTY_ARRAY, ProblemHighlightType.GENERIC_ERROR_OR_WARNING));
            }
          }
        });
      }
      return result.toArray(new ProblemDescriptor[result.size()]);
    }

    return super.checkFile(file, manager, isOnTheFly);
  }

  private static void createPointcutProblem(final DomElement element, final DomElementAnnotationHolder holder) {
    holder.createProblem(element, HighlightSeverity.ERROR, SpringBundle.message("error.pointcut.or.pointcut.ref.should.be.defined"),
                         new DefineAttributeQuickFix("pointcut"), new DefineAttributeQuickFix("pointcut-ref"));
  }

  @Nonnull
  public String getGroupDisplayName() {
    return AopBundle.message("inspection.group.display.name.aop");
  }

  @Nonnull
  public String getDisplayName() {
    return SpringBundle.message("aop.errors.inspection.display.name");
  }

  @Nonnull
  @NonNls
  public String getShortName() {
    return "SpringAopErrorsInspection";
  }

}