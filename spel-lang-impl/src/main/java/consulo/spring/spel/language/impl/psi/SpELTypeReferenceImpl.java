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
import consulo.spring.spel.language.SpELElementTypes;
import org.jspecify.annotations.Nullable;

public class SpELTypeReferenceImpl extends SpELElementImpl implements PsiExpression, PsiReference {
    public SpELTypeReferenceImpl(ASTNode node) {
        super(node);
    }

    @Override
    public PsiElement getElement() {
        return this;
    }

    @Override
    public TextRange getRangeInElement() {
        ASTNode qualifiedNode = getNode().findChildByType(SpELElementTypes.QUALIFIED_NAME);
        if (qualifiedNode != null) {
            int startOffset = qualifiedNode.getStartOffset() - getNode().getStartOffset();
            return new TextRange(startOffset, startOffset + qualifiedNode.getTextLength());
        }
        return TextRange.from(0, getTextLength());
    }

    @Override
    public String getCanonicalText() {
        return getQualifiedName();
    }

    @Override
    public @Nullable PsiElement resolve() {
        String qualifiedName = getQualifiedName();
        if (qualifiedName == null || qualifiedName.isEmpty()) {
            return null;
        }
        return JavaPsiFacade.getInstance(getProject()).findClass(qualifiedName, getResolveScope());
    }

    @Override
    public @Nullable PsiType getType() {
        PsiElement resolved = resolve();
        if (resolved instanceof PsiClass) {
            return PsiType.getJavaLangClass(getManager(), getResolveScope());
        }
        return null;
    }

    @Override
    public PsiReference getReference() {
        return this;
    }

    @Override
    public PsiReference[] getReferences() {
        return new PsiReference[]{this};
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

    private @Nullable String getQualifiedName() {
        ASTNode qualifiedNode = getNode().findChildByType(SpELElementTypes.QUALIFIED_NAME);
        return qualifiedNode != null ? qualifiedNode.getText() : null;
    }
}
