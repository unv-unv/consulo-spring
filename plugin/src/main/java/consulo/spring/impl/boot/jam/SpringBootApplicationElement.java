package consulo.spring.impl.boot.jam;

import com.intellij.jam.reflect.JamClassMeta;
import com.intellij.spring.impl.ide.constants.SpringAnnotationsConstants;
import com.intellij.spring.impl.ide.model.jam.javaConfig.SpingJamElement;

/**
 * @author VISTALL
 * @since 15-Jan-17
 */
public abstract class SpringBootApplicationElement extends SpingJamElement {
  public static final JamClassMeta<SpringBootApplicationElement> META = new JamClassMeta<>(SpringBootApplicationElement.class);

  public SpringBootApplicationElement() {
    super(SpringAnnotationsConstants.SPRING_BOOT_APPLICATION_ANNOTATION);
  }
}
