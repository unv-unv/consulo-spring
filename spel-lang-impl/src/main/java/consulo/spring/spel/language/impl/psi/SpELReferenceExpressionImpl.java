/*
 * Copyright 2013-2026 consulo.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package consulo.spring.spel.language.impl.psi;

import com.intellij.java.language.psi.*;
import com.intellij.java.language.psi.util.PropertyUtil;
import consulo.document.util.TextRange;
import consulo.language.ast.ASTNode;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiReference;
import consulo.language.util.IncorrectOperationException;
import consulo.spring.spel.language.SpELBeanResolverProvider;
import consulo.spring.spel.language.SpELTokenTypes;
import org.jspecify.annotations.Nullable;

public class SpELReferenceExpressionImpl extends SpELElementImpl implements PsiExpression, PsiReference {
    public SpELReferenceExpressionImpl(ASTNode node) {
        super(node);
    }

    @Override
    public PsiElement getElement() {
        return this;
    }

    @Override
    public TextRange getRangeInElement() {
        ASTNode idNode = findIdentifierNode();
        if (idNode != null) {
            int startOffset = idNode.getStartOffset() - getNode().getStartOffset();
            return new TextRange(startOffset, startOffset + idNode.getTextLength());
        }
        return TextRange.from(0, getTextLength());
    }

    @Override
    public String getCanonicalText() {
        ASTNode idNode = findIdentifierNode();
        return idNode != null ? idNode.getText() : getText();
    }

    public @Nullable String getReferenceName() {
        ASTNode idNode = findIdentifierNode();
        return idNode != null ? idNode.getText() : null;
    }

    @Override
    public @Nullable PsiElement resolve() {
        ASTNode idNode = findIdentifierNode();
        if (idNode == null) {
            return null;
        }
        String name = idNode.getText();

        PsiExpression qualifier = getQualifier();

        // no qualifier: unqualified identifier at root level
        // in SpEL, root identifiers are bean names (like greetingService)
        if (qualifier == null) {
            return resolveAsBean(name);
        }

        // qualified: resolve member on qualifier type
        PsiClass targetClass = resolveQualifierClass();
        if (targetClass == null) {
            return null;
        }

        boolean isStatic = isStaticContext();

        // try property getter: getName() / isName()
        PsiMethod getter = PropertyUtil.findPropertyGetter(targetClass, name, isStatic, true);
        if (getter != null) {
            return getter;
        }

        // try field
        PsiField field = targetClass.findFieldByName(name, true);
        if (field != null) {
            return field;
        }

        // try method by name (for method call references)
        PsiMethod[] methods = targetClass.findMethodsByName(name, true);
        if (methods.length > 0) {
            return methods[0];
        }

        return null;
    }

    @Override
    public @Nullable PsiType getType() {
        PsiExpression qualifier = getQualifier();

        // unqualified: resolve as bean, return bean class type
        if (qualifier == null) {
            String name = getReferenceName();
            if (name != null) {
                PsiClass beanClass = resolveBeanClass(name);
                if (beanClass != null) {
                    return JavaPsiFacade.getInstance(getProject())
                        .getElementFactory()
                        .createType(beanClass);
                }
            }
            return null;
        }

        // qualified: type from resolved member
        PsiElement resolved = resolve();
        if (resolved instanceof PsiMethod method) {
            return method.getReturnType();
        }
        if (resolved instanceof PsiField field) {
            return field.getType();
        }
        return null;
    }

    @Override
    public PsiReference[] getReferences() {
        return new PsiReference[]{this};
    }

    @Override
    public @Nullable PsiReference getReference() {
        return this;
    }

    @Override
    public boolean isSoft() {
        return true;
    }

    @Override
    public PsiElement handleElementRename(String newElementName) throws IncorrectOperationException {
        throw new IncorrectOperationException();
    }

    @Override
    public PsiElement bindToElement(PsiElement element) throws IncorrectOperationException {
        throw new IncorrectOperationException();
    }

    @Override
    public boolean isReferenceTo(PsiElement element) {
        PsiElement resolved = resolve();
        return resolved != null && resolved.getManager().areElementsEquivalent(resolved, element);
    }

    @Override
    public Object[] getVariants() {
        return PsiReference.EMPTY_ARRAY;
    }

    private @Nullable ASTNode findIdentifierNode() {
        ASTNode lastId = null;
        for (ASTNode child = getNode().getFirstChildNode(); child != null; child = child.getTreeNext()) {
            if (child.getElementType() == SpELTokenTypes.IDENTIFIER) {
                lastId = child;
            }
        }
        return lastId;
    }

    public @Nullable PsiExpression getQualifier() {
        for (ASTNode child = getNode().getFirstChildNode(); child != null; child = child.getTreeNext()) {
            PsiElement psi = child.getPsi();
            if (psi instanceof PsiExpression && child.getElementType() != SpELTokenTypes.IDENTIFIER) {
                return (PsiExpression) psi;
            }
        }
        return null;
    }

    // Resolve unqualified identifier as a Spring bean name
    private @Nullable PsiElement resolveAsBean(String name) {
        for (SpELBeanResolverProvider provider : SpELBeanResolverProvider.EP_NAME.getExtensionList()) {
            PsiElement result = provider.resolveBean(name, this);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    // Get PsiClass for bean name
    private @Nullable PsiClass resolveBeanClass(String name) {
        for (SpELBeanResolverProvider provider : SpELBeanResolverProvider.EP_NAME.getExtensionList()) {
            PsiClass result = provider.resolveBeanClass(name, this);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    // Resolves the qualifier to a PsiClass. Handles T(type) specially for static access.
    private @Nullable PsiClass resolveQualifierClass() {
        PsiExpression qualifier = getQualifier();
        if (qualifier == null) {
            return null;
        }

        // T(java.lang.Math).random() -> qualifier is TYPE_REFERENCE -> resolve directly to PsiClass
        if (qualifier instanceof SpELTypeReferenceImpl typeRef) {
            PsiElement resolved = typeRef.resolve();
            if (resolved instanceof PsiClass psiClass) {
                return psiClass;
            }
            return null;
        }

        // For other qualifiers, get their type and resolve to PsiClass
        PsiType qualifierType = qualifier.getType();
        if (qualifierType instanceof PsiClassType classType) {
            return classType.resolve();
        }
        return null;
    }

    // T(type).method -> static context
    private boolean isStaticContext() {
        PsiExpression qualifier = getQualifier();
        return qualifier instanceof SpELTypeReferenceImpl;
    }
}
