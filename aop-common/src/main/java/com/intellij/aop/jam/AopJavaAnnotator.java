/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */
package com.intellij.aop.jam;

import com.intellij.aop.*;
import com.intellij.aop.psi.AopReferenceHolder;
import com.intellij.aop.psi.PointcutMatchDegree;
import com.intellij.aop.psi.PsiPointcutExpression;
import com.intellij.jam.model.util.JamCommonUtil;
import com.intellij.java.language.JavaLanguage;
import com.intellij.java.language.psi.*;
import consulo.annotation.component.ExtensionImpl;
import consulo.application.progress.ProgressManager;
import consulo.application.util.CachedValue;
import consulo.application.util.CachedValueProvider;
import consulo.application.util.CachedValuesManager;
import consulo.application.util.NotNullLazyValue;
import consulo.application.util.function.Processor;
import consulo.codeEditor.markup.GutterIconRenderer;
import consulo.language.Language;
import consulo.language.editor.gutter.LineMarkerInfo;
import consulo.language.editor.gutter.LineMarkerProvider;
import consulo.language.editor.ui.DefaultPsiElementCellRenderer;
import consulo.language.editor.ui.navigation.NavigationGutterIconBuilder;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.language.psi.PsiModificationTracker;
import consulo.language.util.ModuleUtilCore;
import consulo.module.Module;
import consulo.ui.image.Image;
import consulo.ui.image.ImageEffects;
import consulo.util.collection.HashingStrategy;
import consulo.util.collection.Sets;
import consulo.util.dataholder.Key;
import consulo.util.lang.StringUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author peter
 */
@ExtensionImpl
public class AopJavaAnnotator implements LineMarkerProvider {
  private static final Key<CachedValue<Set<AopAspect>>> ASPECTS_CACHE = Key.create("ASPECTS_CACHE");
  private static final Key<CachedValue<List<AopIntroduction>>> BOUND_INTROS_KEY =
    Key.create("ClassBoundIntroductions");
  private static final Key<CachedValue<Map<AopAdvice, Integer>>> BOUND_ADVICES_KEY = Key.create("ClassBoundAdvices");
  private static final HashingStrategy<AopAspect> ASPECT_HASHING_STRATEGY = new HashingStrategy<AopAspect>() {
    public int hashCode(final AopAspect object) {
      final PsiElement element = object.getIdentifyingPsiElement();
      return element == null ? 0 : element.hashCode();
    }

    public boolean equals(final AopAspect o1, final AopAspect o2) {
      return o1.getIdentifyingPsiElement() == o2.getIdentifyingPsiElement();
    }
  };

  private static Image createFromIcon(@Nonnull Image icon) {
    return ImageEffects.layered(icon, AopConstants.FROM_ICON);
  }

  public static Image createToIcon(@Nonnull Image icon) {
    return ImageEffects.layered(icon, AopConstants.TO_ICON);
  }

  public LineMarkerInfo getLineMarkerInfo(final PsiElement element) {
    return null;
  }

  @Nonnull
  @Override
  public Language getLanguage() {
    return JavaLanguage.INSTANCE;
  }

  public void collectSlowLineMarkers(final List<PsiElement> elements, final Collection<LineMarkerInfo> result) {
    for (final PsiElement element : elements) {
      annotate(element, result);
    }
  }

  private static void annotate(PsiElement psiElement,
                               final Collection<LineMarkerInfo> result) {
    if (psiElement instanceof PsiIdentifier) {
      final PsiElement parent = psiElement.getParent();
      if (parent instanceof PsiMethod) {
        final PsiMethod method = (PsiMethod)parent;
        if (method.isConstructor()) return;

        final List<AopProvider> providers = AopLanguageInjector.getAopProviders(psiElement);
        if (providers.isEmpty()) return;

        final PsiIdentifier nameIdentifier = method.getNameIdentifier();
        if (nameIdentifier != psiElement) return;

        final PsiClass psiClass = method.getContainingClass();
        final consulo.module.Module module = ModuleUtilCore.findModuleForPsiElement(method);
        if (module != null && psiClass != null) {
          if (isAcceptableAdviceMethod(psiClass, providers)) {
            final AopAdviceImpl advice = AopModuleService.getAdvice(method);
            if (advice != null) {
              final PsiPointcutExpression expression = advice.getPointcutExpression();
              if (expression != null) {
                result.add(addNavigationToInterceptedMethods(advice,
                                                             expression.getContainingFile()
                                                                       .getAopModel()
                                                                       .getAdvisedElementsSearcher()).createLineMarkerInfo(nameIdentifier));
                return;
              }
            }
          }

          final Map<AopAdvice, Integer> boundAdvices = addBoundAdvices(method, getAspects(providers, module), providers);
          if (!boundAdvices.isEmpty()) {
            result.add(addNavigationToBoundAdvices(boundAdvices).createLineMarkerInfo(nameIdentifier));
          }
        }
      }
      else if (parent instanceof PsiClass) {
        final PsiClass psiClass = (PsiClass)parent;
        if (psiClass.hasModifierProperty(PsiModifier.ABSTRACT)) return;

        final PsiIdentifier nameIdentifier = psiClass.getNameIdentifier();
        if (nameIdentifier != psiElement) return;

        List<AopIntroduction> boundIntros = getBoundIntroductions(psiClass);
        if (!boundIntros.isEmpty()) {
          result.add(addNavigationToBoundIntroductions(boundIntros).createLineMarkerInfo(nameIdentifier));
        }
      }
      else if (parent instanceof PsiField) {
        final PsiField field = (PsiField)parent;
        final List<AopProvider> providers = AopLanguageInjector.getAopProviders(psiElement);
        if (providers.isEmpty()) return;

        final PsiIdentifier nameIdentifier = field.getNameIdentifier();
        if (nameIdentifier != psiElement) return;

        final PsiClass psiClass = field.getContainingClass();
        final consulo.module.Module module = ModuleUtilCore.findModuleForPsiElement(field);
        if (module != null && psiClass != null) {
          final AopIntroductionImpl introduction = AopModuleService.getIntroduction(field);
          if (introduction != null) {
            final NavigationGutterIconBuilder<PsiElement> builder = addNavigationToIntroducedClasses(introduction);
            if (builder != null) {
              result.add(builder.createLineMarkerInfo(psiElement));
            }
          }
        }
      }
    }
  }

  public static NavigationGutterIconBuilder<PsiElement> addNavigationToBoundIntroductions(
    final List<AopIntroduction> boundIntros) {
    return NavigationGutterIconBuilder.create(createToIcon(AopConstants.INTRODUCTION_ICON)).
      setTargets(boundIntros.stream().map(it -> it.getIdentifyingPsiElement()).collect(Collectors.toList())).
                                        setTooltipText(AopBundle.message("tooltip.text.navigate.to.introductions")).
                                        setPopupTitle(AopBundle.message("tooltip.text.navigate.to.introductions")).
                                        setAlignment(GutterIconRenderer.Alignment.LEFT);
  }

  public static NavigationGutterIconBuilder<AopAdvice> addNavigationToBoundAdvices(final Map<AopAdvice, Integer> boundAdvices) {
    final ArrayList<AopAdvice> adviceList = new ArrayList<AopAdvice>(boundAdvices.keySet());
    Collections.sort(adviceList, new Comparator<AopAdvice>() {
      public int compare(final AopAdvice o1, final AopAdvice o2) {
        final boolean onTheWayIn = o1.getAdviceType().isOnTheWayIn();
        if (onTheWayIn != o2.getAdviceType().isOnTheWayIn()) {
          return onTheWayIn ? -1 : 1;
        }

        final int i1 = boundAdvices.get(o1).intValue();
        final int i2 = boundAdvices.get(o2).intValue();
        final int diff = i2 - i1;
        return onTheWayIn ? -diff : diff;
      }
    });

    final Map<PsiElement, AopAdvice> psi2Advice = new HashMap<PsiElement, AopAdvice>();
    return NavigationGutterIconBuilder
      .<AopAdvice>create(createToIcon(AopConstants.ABSTRACT_ADVICE_ICON), new Function<>() {
        @Nonnull
        public Collection<? extends PsiElement> apply(final AopAdvice advice) {
          final PsiElement[] psiElements = JamCommonUtil.getTargetPsiElements(advice);
          for (final PsiElement element : psiElements) {
            psi2Advice.put(element, advice);
          }
          return Arrays.asList(psiElements);
        }
      }).
          setTargets(adviceList).
          setTooltipText(AopBundle.message("tooltip.text.navigate.to.advices")).
          setPopupTitle(AopBundle.message("tooltip.text.navigate.to.advices")).
          setAlignment(GutterIconRenderer.Alignment.LEFT).
          setCellRenderer(new DefaultPsiElementCellRenderer() {
            @Override
            public String getElementText(final PsiElement element) {
              final String superText = super.getElementText(element);
              final AopAdvice advice = psi2Advice.get(element);
              if (advice != null && advice.isValid()) {
                final Integer integer = boundAdvices.get(advice);
                if (integer != null && integer.intValue() < Integer.MAX_VALUE) {
                  return superText + " (order=" + integer + ")";
                }
              }
              return superText;
            }

            @Override
            public String getContainerText(final PsiElement element, final String name) {
              final String superText = super.getContainerText(element, name);
              if (StringUtil.isEmpty(superText)) {
                final PsiFile file = element.getContainingFile();
                if (file != null) {
                  return "(in " + file.getName() + ")";
                }
              }
              return superText;
            }

            @Override
            protected int getIconFlags() {
              return 0;
            }
          });
  }

  public static List<AopIntroduction> getBoundIntroductions(final PsiClass psiClass) {
    CachedValue<List<AopIntroduction>> value = psiClass.getUserData(BOUND_INTROS_KEY);
    if (value == null) {
      psiClass.putUserData(BOUND_INTROS_KEY, value = CachedValuesManager.getManager(psiClass.getProject()).createCachedValue(() -> {
        final List<AopProvider> providers =
          AopLanguageInjector.getAopProviders(psiClass);
        if (!providers.isEmpty()) {
          final Module module =
            ModuleUtilCore.findModuleForPsiElement(
              psiClass);
          if (module != null) {
            List<AopIntroduction> boundIntros =
              new ArrayList<AopIntroduction>();
            final PsiClassType type = createPsiType(psiClass);
            for (final AopAspect aspect : getAspects(providers,
                                                     module)) {
              for (final AopIntroduction introduction : aspect.getIntroductions()) {
                final AopReferenceHolder value1 =
                  introduction.getTypesMatching().getValue();
                if (value1 != null && isAcceptable(type,
                                                   value1)) {
                  boundIntros.add(introduction);
                }
              }
            }
            return CachedValueProvider.Result.create(boundIntros,
                                                     PsiModificationTracker.OUT_OF_CODE_BLOCK_MODIFICATION_COUNT);
          }
        }
        return CachedValueProvider.Result.create(Collections.<AopIntroduction>emptyList(),
                                                 PsiModificationTracker.OUT_OF_CODE_BLOCK_MODIFICATION_COUNT);
      }, false));
    }
    return value.getValue();
  }

  public static Set<AopAspect> getAspects(final List<AopProvider> providers, final consulo.module.Module module) {
    CachedValue<Set<AopAspect>> aspects = module.getUserData(ASPECTS_CACHE);
    if (aspects == null) {
      module.putUserData(ASPECTS_CACHE, aspects = CachedValuesManager.getManager(module.getProject()).createCachedValue(() -> {
        Set<AopAspect> set =
          Sets.newHashSet(ASPECT_HASHING_STRATEGY);
        collectAspects(providers, module, set);
        for (final Module module1 : ModuleUtilCore.getAllDependentModules(
          module)) {
          collectAspects(providers, module1, set);
        }
        return CachedValueProvider.Result.create(set,
                                                 PsiModificationTracker.OUT_OF_CODE_BLOCK_MODIFICATION_COUNT);
      }, false));
    }

    return aspects.getValue();
  }

  private static void collectAspects(final List<AopProvider> providers, final Module module, final Set<AopAspect> aspects) {
    aspects.addAll(AopModuleService.getService(module).getModel().getAspects());
    for (final AopProvider provider : providers) {
      aspects.addAll(provider.getAdditionalAspects(module));
    }
  }

  private static int getAdviceOrder(AopAdvice advice, final List<AopProvider> providers) {
    for (final AopProvider provider : providers) {
      final Integer order = provider.getAdviceOrder(advice);
      if (order != null) return order.intValue();
    }
    return Integer.MAX_VALUE;
  }

  private static boolean isAcceptableAdviceMethod(final PsiClass psiClass, final List<AopProvider> providers) {
    for (final AopProvider provider : providers) {
      if (provider.getAdvisedElementsSearcher(psiClass) != null) return true;
    }
    return false;
  }

  public static Map<AopAdvice, Integer> addBoundAdvices(final PsiMethod method,
                                                        final Collection<? extends AopAspect> aspects,
                                                        final List<AopProvider> providers) {
    final Map<AopAdvice, Integer> boundAdvices = new LinkedHashMap<AopAdvice, Integer>();
    for (final AopAspect aspect : aspects) {
      for (final AopAdvice advice : aspect.getAdvices()) {
        ProgressManager.getInstance().checkCanceled();
        if (AopAdviceUtil.accepts(advice, method) == PointcutMatchDegree.TRUE) {
          boundAdvices.put(advice, getAdviceOrder(advice, providers));
        }
      }
    }
    return boundAdvices;
  }

  private static PsiClassType createPsiType(final PsiClass psiClass) {
    return JavaPsiFacade.getInstance(psiClass.getProject()).getElementFactory().createType(psiClass);
  }

  private static boolean isAcceptable(final PsiClassType type, final AopReferenceHolder value) {
    final PsiClass psiClass = type.resolve();
    if (psiClass != null && psiClass.hasModifierProperty(PsiModifier.ABSTRACT)) return false;

    return value.accepts(type) == PointcutMatchDegree.TRUE && value.getContainingFile()
                                                                   .getAopModel()
                                                                   .getAdvisedElementsSearcher()
                                                                   .isAcceptable(
                                                                     psiClass);
  }


  public static NavigationGutterIconBuilder<PsiElement> addNavigationToInterceptedMethods(final AopAdvice advice,
                                                                                          final AopAdvisedElementsSearcher searcher) {
    final NavigationGutterIconBuilder<PsiElement> builder =
      NavigationGutterIconBuilder.create(createFromIcon(advice.getAdviceType().getAdviceIcon()))
                                 .setTargets(new NotNullLazyValue<Collection<? extends PsiElement>>() {
                                   @Nonnull
                                   public Collection<? extends PsiElement> compute() {
                                     if (!advice.isValid()) return Collections.emptyList();

                                     final Set<PsiMethod> result = new HashSet<PsiMethod>();
                                     searcher.process(new Processor<PsiClass>() {
                                       public boolean process(final PsiClass psiClass) {
                                         if (advice.isValid()) {
                                           for (final PsiMethod psiMethod : psiClass.getMethods()) {
                                             if (AopAdviceUtil.accepts(advice,
                                                                       psiMethod) == PointcutMatchDegree.TRUE) {
                                               result.add(psiMethod);
                                             }
                                           }
                                         }
                                         return true;
                                       }
                                     });
                                     return result;
                                   }
                                 })
                                 .setPopupTitle(AopBundle.message("tooltip.text.navigate.to.methods"))
                                 .setTooltipText(AopBundle.message("tooltip.text.navigate.to.methods"))
                                 .setEmptyPopupText(AopBundle.message(
                                   "empty.popup.text.navigate.to.methods"));
    return builder;
  }

  @Nullable
  public static NavigationGutterIconBuilder<PsiElement> addNavigationToIntroducedClasses(
    final AopIntroduction introduction) {
    final AopReferenceHolder expression = introduction.getTypesMatching().getValue();
    if (expression == null) return null;

    final AopAdvisedElementsSearcher searcher = expression.getContainingFile().getAopModel().getAdvisedElementsSearcher();

    final NotNullLazyValue<Collection<? extends PsiElement>> targets =
      new NotNullLazyValue<Collection<? extends PsiElement>>() {
        @Nonnull
        public Collection<? extends PsiElement> compute() {
          final Set<PsiClass> result = new HashSet<PsiClass>();
          searcher.process(new Processor<PsiClass>() {
            public boolean process(final PsiClass psiClass) {
              if (isAcceptable(createPsiType(psiClass), expression)) {
                result.add(psiClass);
              }
              return true;
            }
          });
          return result;
        }
      };
    return NavigationGutterIconBuilder.create(createFromIcon(AopConstants.INTRODUCTION_ICON)).
      setTargets(targets).
                                        setPopupTitle(AopBundle.message("tooltip.text.navigate.to.classes")).
                                        setTooltipText(AopBundle.message("tooltip.text.navigate.to.classes")).
                                        setEmptyPopupText(AopBundle.message("empty.popup.text.navigate.to.classes"));
  }

  public static Map<AopAdvice, Integer> getBoundAdvices(final PsiClass psiClass) {
    CachedValue<Map<AopAdvice, Integer>> value = psiClass.getUserData(BOUND_ADVICES_KEY);
    if (value == null) {
      psiClass.putUserData(BOUND_ADVICES_KEY, value = CachedValuesManager.getManager(psiClass.getProject()).createCachedValue(() -> {
        final Module module =
          ModuleUtilCore.findModuleForPsiElement(psiClass);
        if (module == null)
          return CachedValueProvider.Result.create(Collections.<AopAdvice, Integer>emptyMap(),
                                                   PsiModificationTracker.OUT_OF_CODE_BLOCK_MODIFICATION_COUNT);

        Map<AopAdvice, Integer> result =
          new HashMap<AopAdvice, Integer>();
        final List<AopProvider> providers =
          AopLanguageInjector.getAopProviders(psiClass);
        final Set<AopAspect> aspects = getAspects(providers, module);
        for (final PsiMethod method : psiClass.getMethods()) {
          result.putAll(addBoundAdvices(method, aspects, providers));
        }
        return CachedValueProvider.Result.create(result, PsiModificationTracker.OUT_OF_CODE_BLOCK_MODIFICATION_COUNT);
      }, false));
    }
    return value.getValue();
  }
}
