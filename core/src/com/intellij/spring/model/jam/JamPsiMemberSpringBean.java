package com.intellij.spring.model.jam;

import com.intellij.jam.JamElement;
import com.intellij.jam.JamStringAttributeElement;
import com.intellij.jam.annotations.JamPsiConnector;
import com.intellij.jam.model.common.CommonModelElement;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMember;
import com.intellij.spring.model.xml.CommonSpringBean;
import com.intellij.spring.model.xml.SpringQualifier;
import com.intellij.util.ArrayUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class JamPsiMemberSpringBean<T extends PsiMember> extends CommonModelElement.PsiBase
  implements JamElement, CommonSpringBean {

  @NotNull
  @JamPsiConnector
  public abstract T getPsiElement();

  @Nullable
  public PsiClass getBeanClass(final boolean considerFactories) {
    return getBeanClass();
  }

  @NotNull
  public String[] getAliases() {
    return ArrayUtil.EMPTY_STRING_ARRAY;
  }

  protected List<String> getStringNames(List<JamStringAttributeElement<String>> elements) {
    List<String> aliases = new ArrayList<String>();
    for (JamStringAttributeElement<String> element : elements) {
      String aliasName = element.getStringValue();
      if (!StringUtil.isEmptyOrSpaces(aliasName)) {
        aliases.add(aliasName);
      }
    }
    return aliases;
  }

  public SpringQualifier getSpringQualifier() {
    return null;
  }
}
