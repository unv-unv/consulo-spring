package consulo.spring.impl.boot.jam;

import com.intellij.jam.reflect.JamClassMeta;
import com.intellij.spring.impl.ide.constants.SpringAnnotationsConstants;
import com.intellij.spring.impl.ide.model.jam.javaConfig.JavaSpringConfigurationElement;

/**
 * @author VISTALL
 * @since 2024-04-14
 */
public abstract class SpringBootConfigurationElement extends JavaSpringConfigurationElement {
  public static final JamClassMeta<SpringBootConfigurationElement> META = new JamClassMeta<>(SpringBootConfigurationElement.class);

  static {
    META.addChildrenQuery(BEANS_QUERY);
  }

  public SpringBootConfigurationElement() {
    super(SpringAnnotationsConstants.SPRING_BOOT_CONFIGURATION_ANNOTATION);
  }
}

