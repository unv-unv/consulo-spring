package consulo.spring;

import javax.annotation.Nullable;

import org.jetbrains.annotations.NonNls;
import com.intellij.codeInsight.template.impl.DefaultLiveTemplatesProvider;
import com.intellij.util.ArrayUtil;

/**
 * @author VISTALL
 * @since 2018-08-23
 */
public class SpringDefaultLiveTemplatesProvider implements DefaultLiveTemplatesProvider
{
	@NonNls
	private static final String[] LIVE_TEMPLATES_FILES =
			{
					"/liveTemplates/spring",
					"/liveTemplates/aop",
					"/liveTemplates/dataAccess",
					"/liveTemplates/scheduling",
					"/liveTemplates/integration",
					"/liveTemplates/commonBeans",
					"/liveTemplates/webflow",
					"/liveTemplates/osgi"
			};


	@Override
	public String[] getDefaultLiveTemplateFiles()
	{
		return ArrayUtil.EMPTY_STRING_ARRAY;
	}

	@Nullable
	@Override
	public String[] getHiddenLiveTemplateFiles()
	{
		return LIVE_TEMPLATES_FILES;
	}
}
