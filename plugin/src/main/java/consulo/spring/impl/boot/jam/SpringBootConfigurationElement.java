package consulo.spring.impl.boot.jam;

import com.intellij.jam.reflect.JamAnnotationMeta;
import com.intellij.jam.reflect.JamClassMeta;
import com.intellij.spring.impl.ide.constants.SpringAnnotationsConstants;
import com.intellij.spring.impl.ide.model.jam.javaConfig.JavaSpringConfigurationElement;

/**
 * @author VISTALL
 * @since 2024-04-14
 */
public abstract class SpringBootConfigurationElement extends JavaSpringConfigurationElement {
  public static final JamClassMeta<SpringBootConfigurationElement> META = new JamClassMeta<>(SpringBootConfigurationElement.class).addChildrenQuery(
    BEANS_QUERY);

  private static final JamAnnotationMeta ANNOTATION_META =
    new JamAnnotationMeta(SpringAnnotationsConstants.SPRING_BOOT_CONFIGURATION_ANNOTATION);

  public SpringBootConfigurationElement() {
    super(ANNOTATION_META);
  }
}

