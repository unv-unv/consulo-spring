package com.intellij.spring.impl.ide.model.intentions;

import com.intellij.java.language.psi.*;
import com.intellij.java.language.psi.codeStyle.JavaCodeStyleManager;
import com.intellij.java.language.psi.codeStyle.VariableKind;
import com.intellij.lang.properties.psi.PropertiesFile;
import com.intellij.spring.impl.ide.model.ResolvedConstructorArgs;
import com.intellij.spring.impl.ide.model.converters.ConstructorArgIndexConverter;
import com.intellij.spring.impl.ide.model.values.PlaceholderUtils;
import com.intellij.spring.impl.ide.model.xml.CommonSpringBean;
import com.intellij.spring.impl.ide.model.xml.DomSpringBean;
import com.intellij.spring.impl.ide.model.xml.beans.*;
import consulo.annotation.component.ExtensionImpl;
import consulo.codeEditor.Editor;
import consulo.java.properties.impl.i18n.JavaCreatePropertyFix;
import consulo.language.editor.intention.IntentionMetaData;
import consulo.language.editor.refactoring.rename.SuggestedNameInfo;
import consulo.language.psi.PsiCompiledElement;
import consulo.language.psi.PsiFile;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.localize.LocalizeValue;
import consulo.project.Project;
import consulo.spring.localize.SpringLocalize;
import consulo.util.lang.Pair;
import consulo.util.lang.StringUtil;
import consulo.xml.psi.xml.XmlElement;
import consulo.xml.psi.xml.XmlFile;
import consulo.xml.util.xml.*;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.*;

@ExtensionImpl
@IntentionMetaData(ignoreId = "spring.hardcode.string2placeholder.move", fileExtensions = "xml", categories = {"XML", "Spring"})
public class HardCodedStringToPlaceholderMoveIntention extends JavaCreatePropertyFix {
    private static final Map<String, List<String>> myExcludedProperties = new HashMap<String, List<String>>();
    private static final String[] myEscapes = new String[]{":", "_", "/", "\\", "#", "$", "{", "}"};

    public HardCodedStringToPlaceholderMoveIntention() {
        addExcludedProperties(PlaceholderUtils.PLACEHOLDER_CONFIGURER_CLASS, PlaceholderUtils.PLACEHOLDER_PREFIX_PROPERTY_NAME,
            PlaceholderUtils.PLACEHOLDER_SUFFIX_PROPERTY_NAME
        );
    }

    public boolean isAvailable(@Nonnull Project project, Editor editor, PsiFile file) {
        if (!(file instanceof XmlFile) || DomManager.getDomManager(project).getFileElement((XmlFile) file, Beans.class) == null) {
            return false;
        }
        GenericDomValue<?> genericDomValue = getValueElement(editor, file);

        return genericDomValue != null &&
            isAvailable(genericDomValue, genericDomValue.getParentOfType(SpringBean.class, false));
    }

    @Nullable
    private static GenericDomValue<?> getValueElement(final Editor editor, final PsiFile file) {
        DomElement domElement = DomUtil.getDomElement(editor, file);
        return domElement instanceof GenericDomValue ? (GenericDomValue) domElement : null;
    }

    public void invoke(@Nonnull final Project project, final Editor editor, @Nonnull final PsiFile file) {
        GenericDomValue<?> domElement = getValueElement(editor, file);

        if (domElement == null) {
            return;
        }

        final List<SpringBaseBeanPointer> placeholderConfigurerBeans = PlaceholderUtils.getPlaceholderConfigurerBeans(domElement);
        if (placeholderConfigurerBeans.size() > 0) {
            final String suggestedKey = suggestKey(domElement);

            final List<PropertiesFile> propertiesFiles = new ArrayList<PropertiesFile>();
            for (SpringBaseBeanPointer placeholder : placeholderConfigurerBeans) {
                propertiesFiles.addAll(PlaceholderUtils.getResources(placeholder.getSpringBean()));
            }

            XmlElement element = domElement.getXmlElement();
            if (element == null) {
                return;
            }

            Pair<String, String> property =
                invokeAction(project, file, element, suggestedKey, domElement.getStringValue(), propertiesFiles);

            if (property == null) {
                return;
            }

            String createdKey = property.getFirst();
            Pair<String, String> pair = getPrefixAndSuffix(placeholderConfigurerBeans, propertiesFiles);

            domElement.setStringValue(pair.first + createdKey + pair.second);
        }
    }

    @Nonnull
    @Override
    public LocalizeValue getText() {
        return SpringLocalize.modelIntentionStringConstantMoveToPlaceholder();
    }

    private static boolean isAvailable(final GenericDomValue<?> valueElement, @Nullable final SpringBean springBean) {
        if (valueElement == null || springBean == null) {
            return false;
        }
        final String s = valueElement.getStringValue();
        if (StringUtil.isEmptyOrSpaces(s) || isMultiline(s)) {
            return false;
        }

        final List<SpringBaseBeanPointer> placeholderConfigurerBeans = PlaceholderUtils.getPlaceholderConfigurerBeans(valueElement);
        return placeholderConfigurerBeans.size() > 0 &&
            (!(valueElement instanceof SpringValue) || !DomUtil.hasXml(((SpringValue) valueElement).getType())) &&
            !PlaceholderUtils.isPlaceholder(s, placeholderConfigurerBeans) &&
            !isExcludedProperties(springBean, valueElement);
    }

    private static boolean isExcludedProperties(@Nullable final SpringBean springBean, final GenericDomValue valueHolder) {
        if (springBean == null) {
            return false;
        }

        final PsiClass beanClass = springBean.getBeanClass();

        if (beanClass != null && myExcludedProperties.get(beanClass.getQualifiedName()) != null) {
            final SpringProperty springProperty = valueHolder.getParentOfType(SpringProperty.class, false);
            if (springProperty != null) {
                return myExcludedProperties.get(beanClass.getQualifiedName()).contains(springProperty.getName().getStringValue());
            }
        }
        return false;
    }

    @Nullable
    private static String suggestKey(final GenericDomValue<?> domElement) {

        if (domElement instanceof Prop) {
            final String key = ((Prop) domElement).getKey().getStringValue();
            if (key != null) {
                return key;
            }
        }
        final LinkedList<String> keyFragments = new LinkedList<String>();

        // iterate injections up to top-level bean
        SpringInjection current = domElement.getParentOfType(SpringInjection.class, false);
        while (current != null) {
            keyFragments.addFirst(createKeyFragment(current));
            current = current.getParentOfType(SpringInjection.class, true);
        }

        // start key fragment from top-level bean name (fall back to class if anonymous)
        final DomSpringBean topLevelBean = findTopLevelBean(domElement);
        keyFragments.addFirst(createPrefixFromBean(topLevelBean));
        final String key = StringUtil.join(keyFragments, ".");

        return StringUtil.isEmptyOrSpaces(key) ? suggestValue(domElement.getStringValue()) : key;
    }

    @Nullable
    private static String createPrefixFromBean(@Nullable final DomSpringBean bean) {
        if (bean != null) {
            final String beanName = bean.getBeanName();
            if (beanName != null) {
                return beanName;
            }
            else {
                final PsiClass beanClass = bean.getBeanClass();
                if (beanClass != null) {
                    final Project project = bean.getManager().getProject();
                    final JavaCodeStyleManager styleManager = JavaCodeStyleManager.getInstance(project);
                    final PsiClassType classType = JavaPsiFacade.getInstance(project).getElementFactory().createType(beanClass);
                    final SuggestedNameInfo nameInfo = styleManager.suggestVariableName(VariableKind.LOCAL_VARIABLE, null, null, classType);
                    if (nameInfo.names.length > 0) {
                        return nameInfo.names[0];
                    }
                }
            }
        }
        return null;
    }

    @Nullable
    private static DomSpringBean findTopLevelBean(@Nonnull final DomElement domElement) {
        DomSpringBean current = domElement.getParentOfType(DomSpringBean.class, false);
        while (current != null) {
            if (current.getParent() instanceof Beans) {
                return current;
            }
            current = current.getParentOfType(DomSpringBean.class, true);
        }
        return null;
    }

    @Nullable
    private static String createKeyFragment(@Nonnull final SpringValueHolder holder) {
        if (holder instanceof SpringProperty) {
            return ((SpringProperty) holder).getName().getStringValue();
        }
        if (holder instanceof ConstructorArg) {
            final SpringBean bean = (SpringBean) holder.getParent();
            if (bean != null) {
                final ConstructorArg constructorArg = (ConstructorArg) holder;

                final GenericAttributeValue<Integer> index = constructorArg.getIndex();
                PsiParameter parameter = null;

                if (index.getValue() != null) {
                    parameter = ConstructorArgIndexConverter.resolve(index, bean);
                }
                else {
                    final ResolvedConstructorArgs resolvedArgs = bean.getResolvedConstructorArgs();
                    final PsiMethod resolvedMethod = resolvedArgs.getResolvedMethod();
                    if (resolvedMethod != null) {
                        parameter = resolvedArgs.getResolvedArgs(resolvedMethod).get(constructorArg);
                    }
                }

                if (parameter != null) {
                    final PsiMethod method = PsiTreeUtil.getParentOfType(parameter, PsiMethod.class);
                    if (method instanceof PsiCompiledElement) {
                        final JavaCodeStyleManager styleManager = JavaCodeStyleManager.getInstance(holder.getManager().getProject());
                        final SuggestedNameInfo nameInfo =
                            styleManager.suggestVariableName(VariableKind.LOCAL_VARIABLE, null, null, parameter.getType());
                        return nameInfo.names.length > 0 ? nameInfo.names[0] : null;
                    }
                    else {
                        return parameter.getName();
                    }
                }
            }
        }
        if (holder instanceof SpringEntry) {
            final SpringEntry entry = (SpringEntry) holder;
            final String keyValue = entry.getKeyAttr().getStringValue();
            if (keyValue != null) {
                return suggestValue(keyValue);
            }
            final String value = entry.getKey().getValue().getStringValue();
            if (value != null) {
                return suggestValue(value);
            }
        }
        return null;
    }

    private static String suggestValue(String value) {
        if (value == null) {
            return "";
        }

        for (String myEscape : myEscapes) {
            value = value.replace(myEscape, ".");
        }
        while (value.contains("..")) {
            value = value.replace("..", ".");
        }
        value = value.trim();
        if (value.startsWith(".")) {
            value = value.substring(1, value.length());
        }
        if (value.endsWith(".")) {
            value = value.substring(0, value.length() - 1);
        }

        return value;
    }

    private static Pair<String, String> getPrefixAndSuffix(
        final List<SpringBaseBeanPointer> springBeans,
        final Collection<PropertiesFile> propertiesFiles
    ) {
        for (PropertiesFile propertiesFile : propertiesFiles) {
            if (propertiesFile != null) {
                for (SpringBaseBeanPointer springBean : springBeans) {
                    final CommonSpringBean bean = springBean.getSpringBean();
                    if (PlaceholderUtils.getResources(bean).contains(propertiesFile) && bean instanceof DomSpringBean) {
                        return PlaceholderUtils.getPlaceholderPrefixAndSuffix((DomSpringBean) bean);
                    }
                }
            }
        }
        return new Pair<String, String>(PlaceholderUtils.DEFAULT_PLACEHOLDER_PREFIX, PlaceholderUtils.DEFAULT_PLACEHOLDER_SUFFIX);
    }

    private static boolean isMultiline(String s) {
        return s.trim().indexOf('\n') >= 0;
    }

    private static void addExcludedProperties(final String baseClassName, final String... properties) {
        myExcludedProperties.put(baseClassName, Arrays.asList(properties));
    }

    public boolean startInWriteAction() {
        return true;
    }
}
