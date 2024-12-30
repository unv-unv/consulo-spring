package com.intellij.spring.impl.ide.model.converters;

import com.intellij.java.impl.psi.impl.source.resolve.reference.impl.providers.PackageReferenceSet;
import com.intellij.java.impl.psi.impl.source.resolve.reference.impl.providers.PsiPackageReference;
import com.intellij.java.language.psi.PsiJavaPackage;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiReference;
import consulo.util.lang.PatternUtil;
import consulo.xml.psi.xml.XmlAttributeValue;
import consulo.xml.util.xml.*;
import consulo.xml.util.xml.converters.DelimitedListProcessor;
import org.jetbrains.annotations.NonNls;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.regex.Pattern;

/**
 * @author Dmitry Avdeev
 */
public class PackageListConverter extends Converter<Collection<PsiJavaPackage>> implements CustomReferenceConverter {

  public Collection<PsiJavaPackage> fromString(@Nullable @NonNls final String s, final ConvertContext context) {
    if (s == null) {
      return Collections.emptyList();
    }
    final XmlAttributeValue xmlAttributeValue = ((GenericAttributeValue)context.getInvocationElement()).getXmlAttributeValue();

    if (xmlAttributeValue == null) {
      return Collections.emptyList();
    }
    final PsiReference[] psiReferences = xmlAttributeValue.getReferences();
    final Collection<PsiJavaPackage> list = new HashSet<PsiJavaPackage>();
    for (PsiReference psiReference : psiReferences) {
      if (psiReference instanceof PsiPackageReference) {
        list.addAll(((PsiPackageReference)psiReference).getReferenceSet().resolvePackage());
      }
    }
    return list;
  }

  public String toString(@Nullable final Collection<PsiJavaPackage> psiPackages, final ConvertContext context) {
    return null;
  }

  @Nonnull
  public PsiReference[] createReferences(final GenericDomValue genericDomValue, final PsiElement element, final ConvertContext context) {
    final String text = genericDomValue.getStringValue();
    if (text == null) {
      return PsiReference.EMPTY_ARRAY;
    }
    final ArrayList<PsiReference> list = new ArrayList<PsiReference>();
    new DelimitedListProcessor(",") {
      protected void processToken(final int start, final int end, final boolean delimitersOnly) {
        final PackageReferenceSet referenceSet = new PackageReferenceSet(text.substring(start, end), element, 1 + start) {
          @Override
          public Collection<PsiJavaPackage> resolvePackageName(final PsiJavaPackage context, final String packageName) {
            if (packageName.contains("*")) {
              final Pattern pattern = PatternUtil.fromMask(packageName);
              final PsiJavaPackage[] psiPackages = context.getSubPackages();
              final ArrayList<PsiJavaPackage> packages = new ArrayList<PsiJavaPackage>(psiPackages.length);
              for (PsiJavaPackage aPackage : psiPackages) {
                if (pattern.matcher(aPackage.getName()).matches()) {
                  packages.add(aPackage);
                }
              }
              return packages;
            } else {
              return super.resolvePackageName(context, packageName);
            }
          }
        };
        list.addAll(referenceSet.getReferences());
      }
    }.processText(text);
    return list.toArray(new PsiReference[list.size()]);
  }
}
