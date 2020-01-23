/*
 * @(#)CoeusWindowMenu.java 1.0 10/18/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.gui.menu;

import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import java.awt.*;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Collections;
import java.util.Comparator;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.DesktopUtils;
import edu.mit.coeus.gui.CoeusMessageResources;

/**
 * This class creates CentralWindowMenu for the coeus application.
 *
 * @version :1.0 October 18, 2002, 3:11 PM
 * @author Guptha
 */

public class CoeusWindowMenu extends JMenu implements ActionListener{

    /*
     * Windows menu items
     */
    CoeusMenuItem cascade,tileHorizontal,tileVertical,layer,/*toolbars,*/
                  minimizeAllWindows,undo;

    private static int nextX; // Next X position
    private static int nextY;
    private static final int DEFAULT_OFFSETX = 24;
    private static final int DEFAULT_OFFSETY = 24;
    private static int offsetX = DEFAULT_OFFSETX;
    private static int offsetY = DEFAULT_OFFSETY;
    /* constant which specifies the starting index of internal frame reference
     * displayed in window menu.
     */
    private static final int INTERNAL_FRAME_START_INDEX = 10;
    /*
     * to indicate horizondal seperator in menu items
     */
    private final String SEPERATOR="seperator";

    private CoeusMenu coeusMenu;

    private CoeusAppletMDIForm mdiForm;
    
    //holds the desk top panel
    private JDesktopPane desktop = null;
    
    //holds the dynamically active Window entry list
    private Hashtable activeModuleWindow = null;
   
    private boolean isStateUpdated = false;
    
    //holds CoeusMessageResources instance used for reading message Properties.
    private CoeusMessageResources coeusMessageResources;
    
    private JCheckBoxMenuItem moduleMenuItem;
    
    MenuItemComparator menuComparator;
    
    /** Default constructor which constructs the window menu for coeus application.
     * @param mdiForm  CoeusAppletMDIForm
     */
    public CoeusWindowMenu(CoeusAppletMDIForm mdiForm){
        super();
        this.mdiForm = mdiForm;
        coeusMessageResources = CoeusMessageResources.getInstance();
        desktop = mdiForm.getDeskTopPane();
        createMenu();
        activeModuleWindow = new Hashtable();      
        menuComparator = new MenuItemComparator();
    }

    /**
     * This method is used to get the window menu
     *
     * @return JMenu coeus window menu
     */
    public JMenu getMenu(){
        return coeusMenu;
    }

    /** This method is used to get the CoeusWindowMenu
     * @return CoeusWindowMenu 
     */
    public CoeusWindowMenu getCoeusWindowMenu(){
        return this;
    }
    
    /**
     * This method is used to create window menu for coeus application.
     */
    private void createMenu(){
        java.util.Vector fileChildren = new java.util.Vector();
        cascade = new CoeusMenuItem("Cascade",null,true,true);
        cascade.setMnemonic('C');
        tileHorizontal = new CoeusMenuItem("Tile Horizontal",null,true,true);
        tileHorizontal.setMnemonic('H');
        tileVertical = new CoeusMenuItem("Tile Vertical",null,true,true);
        tileVertical.setMnemonic('V');
        layer= new CoeusMenuItem("Layer",null,true,true);
        layer.setMnemonic('L');
        //Bug Fix: Not implementing Toolbars
        //toolbars = new CoeusMenuItem("Toolbrs...",null,true,true);
        //toolbars.setMnemonic('B');
        
        minimizeAllWindows = new CoeusMenuItem("Minimize All Windows",null,
                                                                    true,true);
        minimizeAllWindows.setMnemonic('M');
        undo = new CoeusMenuItem("Undo Minimize",null,true,true);
        undo.setMnemonic('U');

        fileChildren.add(cascade);
        fileChildren.add(tileHorizontal);
        fileChildren.add(tileVertical);
        fileChildren.add(layer);
        
        //Bug Fix: Not implementing Toolbars
        //fileChildren.add(SEPERATOR);
        //fileChildren.add(toolbars);
        
        fileChildren.add(SEPERATOR);
        
        fileChildren.add(minimizeAllWindows);
        fileChildren.add(undo);

        coeusMenu = new CoeusMenu("Window",null,fileChildren,true,true);
        coeusMenu.setMnemonic('W');

        //add listener
        cascade.addActionListener(this);
        tileHorizontal.addActionListener(this);
        tileVertical.addActionListener(this);
        layer.addActionListener(this);
        
        //Bug Fix: Not implementing Toolbars
        //toolbars.addActionListener(this);
        
        minimizeAllWindows.addActionListener(this);
        undo.addActionListener(this);

    }

    /** This Method is used to add new coeus menu item for newly opened
     * module like AreaOfResearch, Protocol, Committee.
     * @param newModuleWinItem Name of Menu Item newly introduced.
     * @param frame JInternalFrame to listen to the actions of this menu item.
     * @return  CoeusWindowMenu
     */
    public CoeusWindowMenu addNewMenuItem( String newModuleWinItem,
                                                   final JInternalFrame frame ){        
        if( coeusMenu != null && newModuleWinItem!= null &&
                           ! activeModuleWindow.containsKey(newModuleWinItem )){
                               
            int curOpenedWindows = activeModuleWindow.size();
            Vector children = coeusMenu.getChildren();       
            
            //This is for adding the separator at the begging
            if( curOpenedWindows == 0 ){                         
             children.add( SEPERATOR );     
            }
            
            moduleMenuItem = new JCheckBoxMenuItem(
                                               newModuleWinItem,null, true );            
            children.add( moduleMenuItem );    
            if(children.size() > INTERNAL_FRAME_START_INDEX){
                Collections.sort(children.subList(INTERNAL_FRAME_START_INDEX,
                    children.size()),menuComparator);
            }
            coeusMenu.setChildren( children );                                    
            activeModuleWindow.put( newModuleWinItem, moduleMenuItem );

            moduleMenuItem.addActionListener( new ActionListener (){
                public void actionPerformed( ActionEvent actEvnt ){
                    try{
                        if( frame.isIcon() ){                                                       
                            frame.setIcon(false);                                                                
                        }
                        frame.setSelected( true );                        
                        if(activeModuleWindow.size() == 1 ){
                            ((JCheckBoxMenuItem)actEvnt.getSource()).setSelected(true);
                        }
                    }catch (java.beans.PropertyVetoException pve) {                         
                        CoeusOptionPane.showErrorDialog( pve.getMessage() );
                    }catch( Exception err ){                        
                        CoeusOptionPane.showErrorDialog( err.getMessage() );
                    }
                }});
        }        
        return this;
    }
    
    /** This method is used to remove coeus menu item for closed
     * module like AreaOfResearch, Protocol, Committee.
     * @param closedModuleWinItem Name of menu item for which module
     * window will be closed.
     * @return  CoeusWindowMenu
     */
    public CoeusWindowMenu removeMenuItem( String closedModuleWinItem ){
        int curOpenedWindows = activeModuleWindow.size();
        if( coeusMenu != null && closedModuleWinItem!= null && 
               curOpenedWindows > 0 ){
               boolean isExists = activeModuleWindow.containsKey(
                                                        closedModuleWinItem );               
               if( isExists ){
                    Vector existingWinMenuItems = coeusMenu.getChildren();
                    existingWinMenuItems.remove( activeModuleWindow.remove( 
                                                         closedModuleWinItem ));
                    //This is for removing the separator at the end
                    if( activeModuleWindow.size() == 0 ){
                     existingWinMenuItems.remove( 
                                             existingWinMenuItems.size() - 1 );
                    }
                    coeusMenu.setChildren( existingWinMenuItems );  
               }
        }
        return this;
    }
    
    /** This method is used to handle the action event for the window menu items
     * @param ae  ActionEvent
     */
    public void actionPerformed(ActionEvent ae){
        Object srcAction = ae.getSource();
        
        if( srcAction.equals( cascade ) ){            
            DesktopUtils.cascadeAll( desktop );
        }else if( srcAction.equals( tileHorizontal ) ){
            DesktopUtils.tileHorizontal( desktop );
        }else if( srcAction.equals( tileVertical ) ){
            DesktopUtils.tileVertical( desktop );
        }else if( srcAction.equals( minimizeAllWindows ) ){
            DesktopUtils.minimizeAll( desktop );
        }else if( srcAction.equals( layer ) ){             
            DesktopUtils.layer( desktop );             
        }else if( srcAction.equals( undo ) ){
            DesktopUtils.maximizeAll( desktop );
        }else{
            log(coeusMessageResources.parseMessageKey(
                                            "funcNotImpl_exceptionCode.1100"));
        }
    }

    /**
     * display alert message
     *
     * @param mesg the message to be displayed
     */
    private void log(String mesg) {
        CoeusOptionPane.showInfoDialog(mesg);
    }
    
    /**
     * This method is used to select specific menu item for which
     * JInternalFrame or CoeusInternalFrame is activated.
     * @param menuItemName String represent the name of menu item
     * @param state boolean value of menu item selected / deselected
     */
    public void updateCheckBoxMenuItemState( String menuItemName, 
                                                                boolean state){
     
       if( activeModuleWindow.containsKey( menuItemName ) ){            
            JCheckBoxMenuItem selectedMenuItem = ( JCheckBoxMenuItem )
                                        activeModuleWindow.get( menuItemName );                                       
            if( state != selectedMenuItem.getState() ){
                selectedMenuItem.setSelected( state );              
            }
        }
    }
    class MenuItemComparator implements Comparator {
        JMenuItem menuItem1, menuItem2;
        public int compare ( Object first, Object second ) {
            if ( ( first instanceof JMenuItem ) 
                    && ( second instanceof JMenuItem ) ) {
                menuItem1 = (JMenuItem)first;
                menuItem2 = (JMenuItem)second;
              
                return menuItem1.getText().compareTo(menuItem2.getText());
            }
            /* if any or both the arguments are instance(s) of JMenuItem return
               -1 which specifies to take the items in the order they were sent.
             */
            return -1; 
        }
    }
} 