/*******************************************************************************
 * Copyright (c) 2015 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package org.eclipse.rap.loadtest.demo;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.internal.widgets.WidgetTreeVisitor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Widget;


public class WidgetCounter {

  private final Map<String, Integer> widgetsMap = new HashMap<>();

  public WidgetCounter countWidgets( Widget parent ) {
    WidgetTreeVisitor.accept( parent, new WidgetTreeVisitor() {

      @Override
      public boolean visit( Widget widget ) {
        count( widget );
        return true;
      }

      @Override
      public boolean visit( Composite composite ) {
        count( composite );
        return true;
      }

    } );
    return this;
  }

  public void printStats() {
    int total = 0;
    for( String name : widgetsMap.keySet() ) {
      System.out.format( "%d\t%s\n", widgetsMap.get( name ), name );
      total += widgetsMap.get( name ).intValue();
    }
    System.out.format( "\n%d\tTotal\n", Integer.valueOf( total ) );
  }

  private void count( Widget widget ) {
    String name = widget.getClass().getSimpleName();
    Integer count = widgetsMap.get( name );
    widgetsMap.put( name, Integer.valueOf( count == null ? 1 : count.intValue() + 1 ) );
  }

}
