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

import com.intellij.java.language.psi.JavaPsiFacade;
import com.intellij.java.language.psi.PsiClass;
import com.intellij.java.language.psi.PsiExpression;
import com.intellij.java.language.psi.PsiType;
import consulo.document.util.TextRange;
import consulo.language.ast.ASTNode;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiReference;
import consulo.language.util.IncorrectOperationException;
import consulo.spring.spel.language.SpELBeanResolverProvider;
import consulo.spring.spel.language.SpELTokenTypes;
import org.jspecify.annotations.Nullable;

public class SpELBeanReferenceImpl extends SpELElementImpl implements PsiExpression, PsiReference {
    public SpELBeanReferenceImpl(ASTNode node) {
        super(node);
    }

    public @Nullable String getBeanName() {
        ASTNode idNode = getNode().findChildByType(SpELTokenTypes.IDENTIFIER);
        return idNode != null ? idNode.getText() : null;
    }

    @Override
    public PsiElement getElement() {
        return this;
    }

    @Override
    public TextRange getRangeInElement() {
        ASTNode idNode = getNode().findChildByType(SpELTokenTypes.IDENTIFIER);
        if (idNode != null) {
            int startOffset = idNode.getStartOffset() - getNode().getStartOffset();
            return new TextRange(startOffset, startOffset + idNode.getTextLength());
        }
        return TextRange.from(0, getTextLength());
    }

    @Override
    public String getCanonicalText() {
        String name = getBeanName();
        return name != null ? name : "";
    }

    @Override
    public @Nullable PsiElement resolve() {
        String beanName = getBeanName();
        if (beanName == null) {
            return null;
        }
        for (SpELBeanResolverProvider provider : SpELBeanResolverProvider.EP_NAME.getExtensionList()) {
            PsiElement result = provider.resolveBean(beanName, this);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    @Override
    public @Nullable PsiType getType() {
        String beanName = getBeanName();
        if (beanName == null) {
            return null;
        }
        for (SpELBeanResolverProvider provider : SpELBeanResolverProvider.EP_NAME.getExtensionList()) {
            PsiClass beanClass = provider.resolveBeanClass(beanName, this);
            if (beanClass != null) {
                return JavaPsiFacade.getInstance(getProject())
                    .getElementFactory()
                    .createType(beanClass);
            }
        }
        return null;
    }

    @Override
    public PsiReference getReference() {
        return this;
    }

    @Override
    public PsiReference[] getReferences() {
        return mergeWithContributed(this);
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
}
