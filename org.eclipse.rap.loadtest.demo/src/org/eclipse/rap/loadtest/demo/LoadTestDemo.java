package org.eclipse.rap.loadtest.demo;

import org.eclipse.rap.rwt.application.Application;
import org.eclipse.rap.rwt.application.ApplicationConfiguration;


public class LoadTestDemo implements ApplicationConfiguration {

  @Override
  public void configure( Application application ) {
    application.addEntryPoint( "/", LoadTestEntryPoint.class, null );
  }

}
