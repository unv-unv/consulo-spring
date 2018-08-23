/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */

package com.intellij.spring;

import com.intellij.ide.fileTemplates.FileTemplateDescriptor;
import com.intellij.ide.fileTemplates.FileTemplateGroupDescriptor;
import com.intellij.ide.fileTemplates.FileTemplateGroupDescriptorFactory;
import com.intellij.spring.facet.SpringSchemaVersion;

public class SpringBeansTemplatesFactory implements FileTemplateGroupDescriptorFactory {

  @Override
  public FileTemplateGroupDescriptor getFileTemplatesDescriptor() {
    final FileTemplateGroupDescriptor group = new FileTemplateGroupDescriptor(SpringBundle.message("spring"), SpringIcons.FILESET);

    group.addTemplate(new FileTemplateDescriptor(SpringSchemaVersion.Spring_1_DTD.getTemplateName(), SpringIcons.CONFIG_FILE));
    group.addTemplate(new FileTemplateDescriptor(SpringSchemaVersion.Spring_2_DTD.getTemplateName(), SpringIcons.CONFIG_FILE));
    group.addTemplate(new FileTemplateDescriptor(SpringSchemaVersion.Spring_2_Schema.getTemplateName(), SpringIcons.CONFIG_FILE));
    return group;
  }
}
