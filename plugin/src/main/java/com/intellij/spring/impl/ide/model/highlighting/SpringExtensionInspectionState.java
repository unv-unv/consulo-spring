package com.intellij.spring.impl.ide.model.highlighting;

import consulo.configurable.ConfigurableBuilder;
import consulo.configurable.ConfigurableBuilderState;
import consulo.configurable.UnnamedConfigurable;
import consulo.language.editor.inspection.InspectionToolState;
import consulo.localize.LocalizeValue;
import consulo.util.xml.serializer.XmlSerializerUtil;

import jakarta.annotation.Nullable;

/**
 * @author VISTALL
 * @since 01/04/2023
 */
public class SpringExtensionInspectionState implements InspectionToolState<SpringExtensionInspectionState> {
  public boolean checkTestFiles = false;

  @Nullable
  @Override
  public UnnamedConfigurable createConfigurable() {
    ConfigurableBuilder<ConfigurableBuilderState> builder = ConfigurableBuilder.newBuilder();
    builder.checkBox(LocalizeValue.localizeTODO("Check test files"), () -> checkTestFiles, b -> checkTestFiles = b);
    return builder.buildUnnamed();
  }

  @Nullable
  @Override
  public SpringExtensionInspectionState getState() {
    return this;
  }

  @Override
  public void loadState(SpringExtensionInspectionState state) {
    XmlSerializerUtil.copyBean(state, this);
  }
}
