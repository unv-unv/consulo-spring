/*
 * Copyright (c) 2000-2006 JetBrains s.r.o. All Rights Reserved.
 */

package com.intellij.spring.impl.ide.model.properties;

import com.intellij.java.impl.psi.impl.beanProperties.BeanProperty;
import com.intellij.java.impl.refactoring.rename.BeanPropertyRenameHandler;
import consulo.annotation.component.ExtensionImpl;
import consulo.dataContext.DataContext;
import consulo.localize.LocalizeValue;
import jakarta.annotation.Nonnull;

import javax.annotation.Nullable;

/**
 * @author Dmitry Avdeev
 */
@ExtensionImpl
public class SpringPropertyRenameHandler extends BeanPropertyRenameHandler {
    @Override
    @Nullable
    protected BeanProperty getProperty(DataContext context) {
        return SpringPropertiesUtil.getBeanProperty(context);
    }

    @Nonnull
    @Override
    public LocalizeValue getActionTitleValue() {
        return LocalizeValue.localizeTODO("Spring Property Rename...");
    }
}
