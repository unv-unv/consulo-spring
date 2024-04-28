package consulo.spring.impl.context;

import consulo.annotation.component.ComponentScope;
import consulo.annotation.component.ServiceAPI;
import consulo.annotation.component.ServiceImpl;
import consulo.component.persist.PersistentStateComponent;
import consulo.component.persist.State;
import consulo.component.persist.Storage;
import consulo.component.persist.StoragePathMacros;
import consulo.util.xml.serializer.XmlSerializerUtil;
import jakarta.annotation.Nullable;
import jakarta.inject.Singleton;

import java.util.LinkedHashMap;

/**
 * @author VISTALL
 * @since 2024-04-28
 */
@Singleton
@State(name = "SpringContextSetting", storages = @Storage(StoragePathMacros.WORKSPACE_FILE))
@ServiceAPI(ComponentScope.PROJECT)
@ServiceImpl
public class SpringContextSetting implements PersistentStateComponent<SpringContextSetting.State> {
  public static class SpringSet {
    public String type;
    public String value;
  }

  public static class State {
    public LinkedHashMap<String, SpringSet> data = new LinkedHashMap<>();
  }

  private State myState = new State();

  @Nullable
  @Override
  public State getState() {
    return myState;
  }

  @Override
  public void loadState(State state) {
    XmlSerializerUtil.copyBean(state, myState);
  }
}
