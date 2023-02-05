package com.intellij.spring.impl.ide.model.jam.javaConfig;

import com.intellij.jam.reflect.JamAnnotationMeta;
import com.intellij.spring.impl.ide.constants.SpringAnnotationsConstants;
import com.intellij.spring.impl.ide.model.jam.JamPsiMethodSpringBean;

public abstract class SpringJavaExternalBean extends JamPsiMethodSpringBean {

  public static JamAnnotationMeta META = new JamAnnotationMeta(SpringAnnotationsConstants.JAVA_CONFIG_EXTERNAL_BEAN_ANNOTATION);

}