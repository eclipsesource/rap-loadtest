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

import java.util.regex.Pattern;

import org.eclipse.rap.rwt.application.AbstractEntryPoint;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;


public class LoadTestEntryPoint extends AbstractEntryPoint {

  @Override
  protected void createContents( Composite parent ) {
    createMenu( getShell() );
    parent.setLayout( new GridLayout( 5, true ) );
    for( int i = 0; i < 10; i++ ) {
      createForm( new Composite( parent, SWT.NONE ) );
    }
    for( int i = 0; i < 5; i++ ) {
      createTable( new Composite( parent, SWT.NONE ) );
    }
    // Uncomment to print widget stats to sysout
    // new WidgetCounter().countWidgets( getShell() ).printStats();
  }

  private static void createMenu( Shell parent ) {
    Menu menu = new Menu( parent, SWT.BAR );
    parent.setMenuBar( menu );
    for( int i = 0; i < 7; i++ ) {
      MenuItem menuItem = new MenuItem( menu, SWT.CASCADE );
      Menu subMenu = new Menu( menu );
      menuItem.setText( "Item " + i );
      menuItem.setMenu( subMenu );
      for( int j = 0; j < 10; j++ ) {
        MenuItem subItem = new MenuItem( subMenu, SWT.PUSH );
        subItem.setText( "Item " + i + "-" + j );
      }
    }
  }

  private static void createForm( Composite parent ) {
    parent.setLayout( new GridLayout( 3, false ) );
    parent.setLayoutData( new GridData( SWT.FILL, SWT.FILL, false, false ) );
    final Pattern pattern = Pattern.compile("^[A-Za-z]*$");
    final Color red = parent.getDisplay().getSystemColor( SWT.COLOR_RED );
    for( int i = 0; i < 5; i++ ) {
      new Label( parent, SWT.NONE ).setText( "Label" );
      final Text text = new Text( parent, SWT.BORDER );
      text.setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, true ) );
      text.addListener( SWT.Modify, new Listener() {
        @Override
        public void handleEvent( Event event ) {
          text.setBackground( pattern.matcher( text.getText() ).matches() ? null : red );
        }
      } );
      Button button = new Button( parent, SWT.PUSH );
      button.setText( "x" );
      button.addListener( SWT.Selection, new Listener() {
        @Override
        public void handleEvent( Event event ) {
          text.setText( "" );
        }
      } );
    }
  }

  private static void createTable( Composite parent ) {
    parent.setBackground( parent.getDisplay().getSystemColor( SWT.COLOR_GREEN ) );
    parent.setLayout( new FillLayout() );
    parent.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true ) );
    Table table = new Table( parent, SWT.MULTI );
    for( int i = 0; i < 10; i++ ) {
      TableItem item = new TableItem( table, SWT.NONE );
      item.setText( "Item " + i );
    }
  }

}
