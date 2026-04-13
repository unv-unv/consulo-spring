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

import consulo.language.ast.ASTNode;
import consulo.language.impl.psi.ASTWrapperPsiElement;
import consulo.language.psi.ContributedReferenceHost;
import consulo.language.psi.PsiReference;
import consulo.language.psi.PsiReferenceService;
import consulo.util.collection.ArrayUtil;

public class SpELElementImpl extends ASTWrapperPsiElement implements ContributedReferenceHost {
    public SpELElementImpl(ASTNode node) {
        super(node);
    }

    @Override
    public PsiReference[] getReferences() {
        return PsiReferenceService.getService().getContributedReferences(this);
    }

    /**
     * Merges own references with contributed references from PsiReferenceContributors.
     * Subclasses that provide their own PsiReference should call this instead of returning just their own refs.
     */
    protected PsiReference[] mergeWithContributed(PsiReference... ownRefs) {
        PsiReference[] contributed = PsiReferenceService.getService().getContributedReferences(this);
        if (contributed.length == 0) {
            return ownRefs;
        }
        if (ownRefs.length == 0) {
            return contributed;
        }
        return ArrayUtil.mergeArrays(ownRefs, contributed);
    }
}
