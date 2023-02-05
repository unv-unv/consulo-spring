package consulo.spring.impl;

import consulo.annotation.component.ExtensionImpl;
import consulo.language.editor.template.DefaultLiveTemplatesProvider;
import consulo.util.collection.ArrayUtil;
import org.jetbrains.annotations.NonNls;

import javax.annotation.Nullable;

/**
 * @author VISTALL
 * @since 2018-08-23
 */
@ExtensionImpl
public class SpringDefaultLiveTemplatesProvider implements DefaultLiveTemplatesProvider {
  @NonNls
  private static final String[] LIVE_TEMPLATES_FILES =
    {
      "/liveTemplates/spring.xml",
      "/liveTemplates/aop.xml",
      "/liveTemplates/dataAccess.xml",
      "/liveTemplates/scheduling.xml",
      "/liveTemplates/integration.xml",
      "/liveTemplates/commonBeans.xml",
      "/liveTemplates/webflow.xml",
      "/liveTemplates/osgi.xml"
    };


  @Override
  public String[] getDefaultLiveTemplateFiles() {
    return ArrayUtil.EMPTY_STRING_ARRAY;
  }

  @Nullable
  @Override
  public String[] getHiddenLiveTemplateFiles() {
    return LIVE_TEMPLATES_FILES;
  }
}
