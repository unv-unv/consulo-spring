package consulo.spring.impl;

import com.intellij.spring.impl.ide.SpringModel;
import com.intellij.spring.impl.ide.model.xml.beans.Beans;
import consulo.xml.util.xml.model.DomModel;

/**
 * @author VISTALL
 * @since 15-Jan-17
 */
public interface DomSpringModel extends SpringModel, DomModel<Beans> {
}
