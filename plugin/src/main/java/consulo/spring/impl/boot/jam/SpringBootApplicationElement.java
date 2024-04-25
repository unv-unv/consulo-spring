package consulo.spring.impl.boot.jam;

import com.intellij.jam.reflect.JamAnnotationMeta;
import com.intellij.jam.reflect.JamClassMeta;
import com.intellij.spring.impl.ide.constants.SpringAnnotationsConstants;
import com.intellij.spring.impl.ide.model.jam.javaConfig.SpringJamElement;

/**
 * @author VISTALL
 * @since 15-Jan-17
 */
public abstract class SpringBootApplicationElement extends SpringJamElement {
  public static final JamClassMeta<SpringBootApplicationElement> META = new JamClassMeta<>(SpringBootApplicationElement.class);

  private static final JamAnnotationMeta ANNOTATION_META =
    new JamAnnotationMeta(SpringAnnotationsConstants.SPRING_BOOT_APPLICATION_ANNOTATION);

  public SpringBootApplicationElement() {
    super(ANNOTATION_META);
  }
}
