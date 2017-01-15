package consulo.spring;

import com.intellij.spring.SpringModel;
import com.intellij.spring.model.xml.beans.Beans;
import com.intellij.util.xml.model.DomModel;

/**
 * @author VISTALL
 * @since 15-Jan-17
 */
public interface DomSpringModel extends SpringModel, DomModel<Beans> {
}
