package consulo.spring.impl.boot;

import com.intellij.spring.impl.ide.facet.SpringFileSet;
import com.intellij.spring.impl.ide.facet.SpringFileSetFactory;
import consulo.disposer.Disposable;
import consulo.spring.impl.icon.SpringImplIconGroup;
import consulo.ui.image.Image;

import javax.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 15-Jan-17
 */
public class SpringBootFileSet extends SpringFileSet {
  public SpringBootFileSet(@Nonnull String id, @Nonnull String name, @Nonnull Disposable parent) {
    super(id, name, parent);
    setAutodetected(true);
  }

  public SpringBootFileSet(SpringFileSet original) {
    super(original);
    setAutodetected(true);
  }

  @Nonnull
  @Override
  public String getType() {
    return SpringFileSetFactory.BOOT;
  }

  @Override
  public Image getIcon() {
    return SpringImplIconGroup.springjavaconfig();
  }
}
