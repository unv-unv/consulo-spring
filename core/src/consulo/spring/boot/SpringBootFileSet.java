package consulo.spring.boot;

import com.intellij.openapi.Disposable;
import com.intellij.spring.facet.SpringFileSet;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * @author VISTALL
 * @since 15-Jan-17
 */
public class SpringBootFileSet extends SpringFileSet {
  public SpringBootFileSet(@NonNls @NotNull String id, @NotNull String name, @NotNull Disposable parent) {
    super(id, name, parent);
    setAutodetected(true);
  }

  public SpringBootFileSet(SpringFileSet original) {
    super(original);
    setAutodetected(true);
  }
}
