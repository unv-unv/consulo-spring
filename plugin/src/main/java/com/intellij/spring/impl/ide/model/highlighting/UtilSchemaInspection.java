package com.intellij.spring.impl.ide.model.highlighting;

import com.intellij.java.language.psi.JavaPsiFacade;
import com.intellij.java.language.psi.PsiClass;
import com.intellij.java.language.psi.PsiClassType;
import com.intellij.java.language.psi.PsiType;
import com.intellij.spring.impl.ide.SpringBundle;
import com.intellij.spring.impl.ide.model.converters.SpringConverterUtil;
import com.intellij.spring.impl.ide.model.xml.beans.Beans;
import com.intellij.spring.impl.ide.model.xml.beans.ListOrSet;
import com.intellij.spring.impl.ide.model.xml.util.UtilList;
import com.intellij.spring.impl.ide.model.xml.util.UtilMap;
import com.intellij.spring.impl.ide.model.xml.util.UtilSet;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.editor.rawHighlight.HighlightDisplayLevel;
import consulo.project.Project;
import consulo.xml.util.xml.DomFileElement;
import consulo.xml.util.xml.DomUtil;
import consulo.xml.util.xml.GenericAttributeValue;
import consulo.xml.util.xml.highlighting.DomElementAnnotationHolder;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;

import jakarta.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.Set;

@ExtensionImpl
public class UtilSchemaInspection extends InjectionValueTypeInspection {

  @Override
  public void checkFileElement(final DomFileElement<Beans> domFileElement, final DomElementAnnotationHolder holder, Object state) {
    final Beans beans = domFileElement.getRootElement();

    for (UtilSet springSet : DomUtil.getDefinedChildrenOfType(beans, UtilSet.class)) {
      checkSetBean(springSet, holder);
      checkElementsHolder(springSet, holder);
    }
    for (UtilList list : DomUtil.getDefinedChildrenOfType(beans, UtilList.class)) {
      checkListBean(list, holder);
      checkElementsHolder(list, holder);
    }
    for (UtilMap map : DomUtil.getDefinedChildrenOfType(beans, UtilMap.class)) {
      checkMapBean(map, holder);
    }
  }

  private void checkElementsHolder(final ListOrSet springSet, final DomElementAnnotationHolder holder) {
    checkSpringPropertyCollection(springSet, holder);
  }

  private static void checkMapBean(final UtilMap map, final DomElementAnnotationHolder holder) {
    checkProperClass(map.getMapClass(), Map.class, holder);
  }

  private static void checkListBean(final UtilList list, final DomElementAnnotationHolder holder) {
    checkProperClass(list.getListClass(), List.class, holder);
  }

  private static void checkSetBean(final UtilSet set, final DomElementAnnotationHolder holder) {
    checkProperClass(set.getSetClass(), Set.class, holder);
  }

  private static void checkProperClass(final GenericAttributeValue<PsiClass> attrClass,
                                       final Class aClass,
                                       final DomElementAnnotationHolder holder) {
    final PsiClass psiClass = attrClass.getValue();
    if (psiClass != null) {
      if (!isAssignable(psiClass, aClass)) {
        final String s = SpringBundle.message("util.requred.class.message", aClass.getName());
        holder.createProblem(attrClass, s);
      }
    }
  }

  private static boolean isAssignable(final PsiClass psiClass, final Class fromClass) {
    final Project project = psiClass.getProject();
    final PsiType fromType = SpringConverterUtil.findType(fromClass, project);
    final PsiClassType classType = JavaPsiFacade.getInstance(project).getElementFactory().createType(psiClass);

    return fromType != null && fromType.isAssignableFrom(classType);
  }

  @Nls
  @Nonnull
  public String getDisplayName() {
    return SpringBundle.message("util.schema.inspection.name");
  }

  @NonNls
  @Nonnull
  public String getShortName() {
    return "UtilSchemaInspection";
  }

  @Nonnull
  public HighlightDisplayLevel getDefaultLevel() {
    return HighlightDisplayLevel.ERROR;
  }
}

