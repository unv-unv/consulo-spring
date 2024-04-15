package consulo.spring.impl;

import consulo.annotation.DeprecationInfo;
import consulo.spring.impl.icon.SpringImplIconGroup;
import consulo.ui.image.Image;

@Deprecated
@DeprecationInfo("Use SpringImplIconGroup")
public interface SpringIcons {
  Image FactoryMethodBean = SpringImplIconGroup.factorymethodbean();
  Image FileSet = SpringImplIconGroup.fileset();
  Image ShowAutowiredDependencies = SpringImplIconGroup.showautowireddependencies();
  Image Spring = SpringImplIconGroup.spring();
  Image SpringBean = SpringImplIconGroup.springbean();
  Image SpringConfig = SpringImplIconGroup.springconfig();
  Image SpringJavaBean = SpringImplIconGroup.springjavabean();
  Image SpringJavaConfig = SpringImplIconGroup.springjavaconfig();
  Image SpringProperty = SpringImplIconGroup.springproperty();
}