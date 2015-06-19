package org.eclipse.rap.loadtest.demo;

import org.eclipse.rap.rwt.application.AbstractEntryPoint;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
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
    parent.setLayout( new GridLayout( 10, false ) );
    for( int i = 0; i < 10; i++ ) {
      createForm( new Composite( parent, SWT.NONE ) );
    }
    for( int i = 0; i < 3; i++ ) {
      createTable( new Composite( parent, SWT.NONE ) );
    }
  }

  private static void createForm( Composite parent ) {
    parent.setLayout( new GridLayout( 3, false ) );
    Listener listener = fakeListener();
    for( int i = 0; i < 5; i++ ) {
      new Label( parent, SWT.NONE ).setText( "Label" );
      Text text = new Text( parent, SWT.BORDER );
      text.addListener( SWT.Modify, listener );
      Button button = new Button( parent, SWT.PUSH );
      button.setText( "x" );
      button.addListener( SWT.Selection, listener );
    }
  }

  private static void createTable( Composite parent ) {
    parent.setLayout( new FillLayout() );
    Table table = new Table( parent, SWT.MULTI );
    for( int i = 0; i < 100; i++ ) {
      TableItem item = new TableItem( table, SWT.NONE );
      item.setText( "Item " + i );
    }
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

  private static Listener fakeListener() {
    return new Listener() {
      @Override
      public void handleEvent( Event event ) {
      }
    };
  }

}
