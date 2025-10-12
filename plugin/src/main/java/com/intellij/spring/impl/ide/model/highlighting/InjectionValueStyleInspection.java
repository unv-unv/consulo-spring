/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */

package com.intellij.spring.impl.ide.model.highlighting;

import com.intellij.java.language.psi.PsiType;
import com.intellij.spring.impl.ide.SpringModel;
import com.intellij.spring.impl.ide.model.SpringModelVisitor;
import com.intellij.spring.impl.ide.model.SpringUtils;
import com.intellij.spring.impl.ide.model.converters.SpringBeanUtil;
import com.intellij.spring.impl.ide.model.xml.beans.*;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.editor.CodeInsightUtilCore;
import consulo.language.editor.annotation.HighlightSeverity;
import consulo.language.editor.inspection.LocalQuickFix;
import consulo.language.editor.inspection.ProblemDescriptor;
import consulo.language.editor.rawHighlight.HighlightDisplayLevel;
import consulo.localize.LocalizeValue;
import consulo.project.Project;
import consulo.spring.localize.SpringLocalize;
import consulo.xml.psi.xml.XmlElement;
import consulo.xml.psi.xml.XmlTag;
import consulo.xml.util.xml.DomUtil;
import consulo.xml.util.xml.GenericAttributeValue;
import consulo.xml.util.xml.GenericDomValue;
import consulo.xml.util.xml.highlighting.DomElementAnnotationHolder;
import jakarta.annotation.Nonnull;

import java.util.Properties;

/**
 * @author Dmitry Avdeev
 */
@ExtensionImpl
public class InjectionValueStyleInspection extends SpringBeanInspectionBase {
    private static final String KEY = "key";
    private static final String VALUE = "value";
    private static final String VALUE_REF = "value-ref";
    private static final String KEY_REF = "key-ref";
    private static final String REF = "ref";

    protected SpringModelVisitor createVisitor(
        final DomElementAnnotationHolder holder,
        final Beans beans,
        final SpringModel model,
        Object state
    ) {
        return new SpringModelVisitor() {
            protected boolean visitValueHolder(final SpringValueHolder valueHolder) {
                checkValueHolder(holder, valueHolder);
                return true;
            }
        };
    }

    protected void checkBean(
        final SpringBean springBean,
        final Beans beans,
        final DomElementAnnotationHolder holder,
        final SpringModel springModel, Object state
    ) {
        for (final SpringValueHolderDefinition property : SpringUtils.getValueHolders(springBean)) {
            checkValueHolder(holder, property);
        }
    }

    private static void checkValueHolder(final DomElementAnnotationHolder holder, final SpringValueHolderDefinition valueHolder) {
        checkValue(valueHolder, holder);
        checkRefBean(valueHolder, holder);
        if (valueHolder instanceof SpringEntry) {
            checkValue(((SpringEntry) valueHolder).getKey(), holder);
            checkRefBean(((SpringEntry) valueHolder).getKey(), holder);
        }
    }

    private static void checkValue(final SpringValueHolderDefinition valueHolder, final DomElementAnnotationHolder holder) {
        final PsiType type = SpringBeanUtil.getRequiredType(valueHolder);
        if (type != null && type.getCanonicalText().equals(Properties.class.getName())) {
            return; // IDEADEV-18731
        }

        final GenericDomValue<?> value = valueHolder.getValueElement();
        if (value != null && !(value instanceof GenericAttributeValue)) {
            final String s = value.getStringValue();
            if (s != null && !isMultiline(s) && (!(value instanceof SpringValue) || !DomUtil.hasXml(((SpringValue) value).getType()))) {
                final LocalQuickFix fix = new ValueQuickFix(valueHolder);
                holder.createProblem(
                    value,
                    HighlightSeverity.ERROR,
                    SpringLocalize.modelInspectionInjectionValueStyleMessage().get(),
                    fix
                ).highlightWholeElement();
            }
        }
    }

    private static boolean isMultiline(String s) {
        return s.trim().indexOf('\n') >= 0;
    }

    private static void checkRefBean(final SpringValueHolderDefinition valueHolder, final DomElementAnnotationHolder holder) {
        if (valueHolder instanceof SpringValueHolder) {
            final SpringRef ref = ((SpringValueHolder) valueHolder).getRef();
            final GenericAttributeValue<SpringBeanPointer> bean = ref.getBean();
            if (DomUtil.hasXml(bean)) {
                final LocalQuickFix fix = new RefQuickFix((SpringValueHolder) valueHolder);
                holder.createProblem(ref, HighlightSeverity.ERROR, SpringLocalize.modelInspectionInjectionValueStyleRefMessage().get(), fix)
                    .highlightWholeElement();
            }
        }
    }

    @Nonnull
    @Override
    public LocalizeValue getDisplayName() {
        return SpringLocalize.modelInspectionInjectionValueStyle();
    }

    @Nonnull
    @Override
    public String getShortName() {
        return "SpringInjectionValueStyleInspection";
    }

    @Nonnull
    public HighlightDisplayLevel getDefaultLevel() {
        return HighlightDisplayLevel.WARNING;
    }

    private static class ValueQuickFix implements LocalQuickFix {
        private final SpringValueHolderDefinition myValueHolder;

        public ValueQuickFix(final SpringValueHolderDefinition valueHolder) {
            myValueHolder = valueHolder.createStableCopy();
        }

        @Nonnull
        @Override
        public LocalizeValue getName() {
            return SpringLocalize.modelInspectionInjectionValueStyleValueFix(myValueHolder instanceof SpringKey ? KEY : VALUE);
        }

        public void applyFix(@Nonnull final Project project, @Nonnull final ProblemDescriptor descriptor) {
            final XmlElement xmlElement = myValueHolder.getXmlElement();
            if (xmlElement == null || !CodeInsightUtilCore.getInstance().preparePsiElementForWrite(xmlElement)) {
                return;
            }
            final GenericDomValue<?> valueElement = myValueHolder.getValueElement();
            assert valueElement != null;
            final String val = valueElement.getStringValue();
            if (myValueHolder instanceof SpringKey) {
                final SpringEntry entry = (SpringEntry) myValueHolder.getParent();
                assert entry != null;
                entry.getKeyAttr().setStringValue(val);
                myValueHolder.undefine();
                final XmlTag tag = entry.getXmlTag();
                assert tag != null;
                tag.collapseIfEmpty();
            }
            else {
                if (myValueHolder instanceof SpringValueHolder) {
                    final SpringValueHolder holder = (SpringValueHolder) myValueHolder;
                    holder.getValueAttr().undefine();
                    holder.getValue().undefine();
                }
                final GenericDomValue<?> element = myValueHolder.getValueElement();
                assert element != null;
                element.setStringValue(val);
                final XmlTag tag = myValueHolder.getXmlTag();
                assert tag != null;
                tag.collapseIfEmpty();
            }
        }
    }

    private static class RefQuickFix implements LocalQuickFix {
        private final SpringValueHolder myValueHolder;

        public RefQuickFix(final SpringValueHolder valueHolder) {
            myValueHolder = valueHolder.createStableCopy();
        }

        @Nonnull
        @Override
        public LocalizeValue getName() {
            final String attr;
            if (myValueHolder instanceof SpringKey) {
                attr = KEY_REF;
            }
            else if (myValueHolder instanceof SpringEntry) {
                attr = VALUE_REF;
            }
            else {
                attr = REF;
            }
            return SpringLocalize.modelInspectionInjectionValueStyleRefFix(attr);
        }

        public void applyFix(@Nonnull final Project project, @Nonnull final ProblemDescriptor descriptor) {
            final XmlElement element = myValueHolder.getXmlElement();
            if (element == null || !CodeInsightUtilCore.getInstance().preparePsiElementForWrite(element)) {
                return;
            }
            final String val = myValueHolder.getRef().getBean().getStringValue();
            if (myValueHolder instanceof SpringKey) {
                final SpringEntry entry = (SpringEntry) myValueHolder.getParent();
                assert entry != null;
                entry.getKeyRef().setStringValue(val);
                myValueHolder.undefine();
                final XmlTag tag = entry.getXmlTag();
                assert tag != null;
                tag.collapseIfEmpty();
            }
            else {
                myValueHolder.getRefAttr().setStringValue(val);
                myValueHolder.getRef().undefine();
                final XmlTag tag = myValueHolder.getXmlTag();
                assert tag != null;
                tag.collapseIfEmpty();
            }
        }
    }
}
