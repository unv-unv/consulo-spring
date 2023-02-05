/*
 * Copyright (c) 2000-2006 JetBrains s.r.o. All Rights Reserved.
 */

package com.intellij.spring.impl.ide;

@Deprecated
public class SpringApplicationComponent {

  public SpringApplicationComponent() {

  }

  private static void registerPresentations() {
    // TODO
//    ElementPresentationManager.registerNameProvider(new Function<Object, String>() {
//
//      @Nullable
//      public String fun(final Object s) {
//
//        if (s instanceof CommonSpringBean) {
//          final CommonSpringBean springBean = (CommonSpringBean)s;
//          final String beanName = springBean.getBeanName();
//          if (beanName != null) {
//            return beanName;
//          }
//          final PsiClass beanClass = springBean.getBeanClass();
//          if (beanClass != null) {
//            return beanClass.getName();
//          }
//
//          return SpringBundle.message("spring.bean.with.unknown.name");
//        }
//        else if (s instanceof BeanProperty) {
//          return ((BeanProperty)s).getName();
//        }
//        else if (s instanceof SpringBeanPointer) {
//          return ((SpringBeanPointer)s).getName();
//        }
//        else if (s instanceof SpringImport) {
//          return ((SpringImport)s).getResource().getStringValue();
//        }
//        return null;
//      }
//    });
//
//    ElementPresentationManager.registerDocumentationProvider(new NullableFunction<Object, String>() {
//      public String fun(final Object o) {
//        if (o instanceof SpringBean) {
//          return ((SpringBean)o).getDescription().getStringValue();
//        }
//        return null;
//      }
//    });
  }
}
