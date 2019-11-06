/*
 * @(#)CoeusInternalFrame.java 1.0 07/25/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.gui;

import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.birt.BirtReport;
import edu.mit.coeus.utils.birt.BirtReportMenuBuilder;
import edu.mit.coeus.utils.birt.CodeRunner;
import java.awt.event.ActionEvent;
import javax.swing.*;
import javax.swing.JMenu;
import javax.swing.event.InternalFrameListener;
import javax.swing.event.InternalFrameEvent;
import java.awt.*;
import java.util.Vector;
import java.util.HashMap;

import edu.mit.coeus.gui.menu.CoeusWindowMenu;
import edu.mit.coeus.gui.menu.CoeusMenu;
import edu.mit.coeus.gui.menu.CoeusFileMenu;
import java.awt.event.ActionListener;

/**
 * An internalframe to be shown on click of menu items. This internal frame holds
 * menus and toolbars to be displayed into the application. The menus and toolbars
 * can be activated and deactivated through internal frame's activated and
 * deactivated methods recpectivly.
 *
 * @version 1.0 July 25, 2002, 09:30 AM
 * @author Guptha K
 */
public abstract class CoeusInternalFrame extends JInternalFrame implements InternalFrameListener {
    /*
     * to hold number of frames opened
     */
    int openFrameCount = 0;
    /*
     * to set the frame offset
     */
    static final int XOFFSET = 30, YOFFSET = 30;
    /*
     * height of the frame
     */
    static final int FRAME_HEIGHT = 645;
    /*
     * width of the frame
     */
    static final int FRAME_WIDTH = 1020;

    /**
     * MDIForm object
     */
    CoeusAppletMDIForm desktop;
    /*
     * menu associated with this frame
     */
    CoeusMenu frameMenu,toolsMenu, actionMenu; //prps added actionMenu
    /*
     * toolbar associated with this frame
     */
    JToolBar toolBar;
    
    /** To Hold Menus specific for this Internal Frame
     */
    private HashMap menus = null;
    

    /*
     * set search Result Table Component to identify the selecte Row.
     */
    private JTable searchResultsTable;

    /*
     * set search Result Table Component to identify the selecte Row.
     */
    private Vector resultData;

    private String frame;
    
    /** begin: fixed bug with id #147  */
    
        
    //holds CoeusMessageResources instance used for reading message Properties.
    private CoeusMessageResources coeusMessageResources;
    /** end: fixed bug with id #147  */
    public static final String DETAILS_MODE = "DETAILS";
    public static final String LIST_MODE = "LIST";
    public static final String OTHER_MODE = "OTHERS";
    private String mode = DETAILS_MODE;
    public CoeusInternalFrame(String title, CoeusAppletMDIForm desktop,String mode) {
        this(title,desktop);
        this.mode = mode;
    }
    /**
     * Creates new CoeusInternalFrame.
     *
     * @param title String  representing the title of the frame.
     * @param desktop reference of CoeusAppletMDIForm.
     */
    public CoeusInternalFrame(String title, CoeusAppletMDIForm desktop) {
        super(title,
                true, //resizable
                true, //closable
                true, //maximizable
                true);//iconifiab-le

        this.desktop = desktop;

        /** begin: fixed bug with id #147  */
        /*coeusMessageResources = CoeusMessageResources.getInstance();*/
        /** end: fixed bug with id #147  */

        addInternalFrameListener(this);
        // added by ravi to get the internal frame in maximized mode.
        //setSize(desktop.getSize()); commented by sharath.
        //setSize(FRAME_WIDTH,FRAME_HEIGHT);
        //setMaximumSize(new Dimension(FRAME_WIDTH,FRAME_HEIGHT));
        
        //added by sharath to set the size same as desktop free space - START
        Dimension size = desktop.getContentPane().getSize();
        int width, height;
        width = (int)size.getWidth();
        JToolBar defaultToolBar = desktop.getDefaultToolBar();
        if( defaultToolBar != null ){
            int defaultToolbarHeight = defaultToolBar.getHeight();
            height = ((int)size.getHeight() - (2*defaultToolbarHeight));
        }else{
            height = (int)size.getHeight() - desktop.getToolBarPanel().getHeight();
        }
        
        setSize(width, height);
        //added by sharath to set the size same as desktop free space - END
        
        setBackground(Color.white);
        //Set the window's location.
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        setLocation(XOFFSET * openFrameCount, YOFFSET * openFrameCount);
        
        //added by to set the Icon
        setFrameIcon(desktop.getCoeusIcon());
        
        try {
            setSelected(true);
        } catch (java.beans.PropertyVetoException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is used to set the name of the frame.
     * @param frame  String representing the frame name. 
     */    
    public void setFrame(String frame) {
        this.frame = frame;
    }

    /**
     * This method is used to get the name of the frame.
     *
     * @return frame  String representing the frame name.  
     */    
    public String getFrame() {
        return frame;
    }

    /**
     * This method is used to set the custom menu for this internal frame.
     * @param frameMenu  CoeusMenu specific to this internal frame. 
     */    
    public void setFrameMenu(CoeusMenu frameMenu) {
        this.frameMenu = frameMenu;
    }

    /** 
     * This method is used to get the custom menu specific to this internal frame.
     * @return frameMenu CoeusMenu specific to this internal frame. 
     */    
    public CoeusMenu getFrameMenu() {
        return frameMenu;
    }

    /**
     * This method is used to set the custom tools menu for this frame.
     * @param toolsMenu  CoeusMenu specific to this internal frame.
     */    
    public void setToolsMenu(CoeusMenu toolsMenu) {
        this.toolsMenu = toolsMenu;
    }

    /**
     * This method is used to get the custom tools menu specific to this 
     * internal frame.
     * @return  toolsMenu CoeusMenu specific to this internal frame.
     */    
    public CoeusMenu getToolsMenu() {
        return toolsMenu;
    }

    // prps start
    
    /**
     * This method is used to set the custom actions menu for this frame.
     * @param actionMenu CoeusMenu specific to this internal frame.
     */    
    public void setActionsMenu(CoeusMenu actionMenu) {
        this.actionMenu = actionMenu;
    }

    /**
     * This method is used to get the custom actions menu specific to this 
     * internal frame.
     * @return  actionMenu CoeusMenu specific to this internal frame.
     */    
    public CoeusMenu getActionsMenu() {
        return actionMenu;
    }

    //prps end
    
    
    
    /**
     * This method is used to set the custom tool bar specific to this internal
     * frame.
     * @param toolBar  reference to JToolBar with custom tools specific to this
     * internal frame.
     */    
    public void setFrameToolBar(JToolBar toolBar) {
        this.toolBar = toolBar;
    }

    /**
     * This method is used to get the custom tool bar specific to this internal
     * frame.
     * @return toolBar  reference to JToolBar with custom tools specific to this
     * internal frame.
     */    
    public JToolBar getFrameToolBar() {
        return toolBar;
    }
    
    /**
     * This method is used to perform some operation when the internal frame is
     * opened.
     * @param e  InternalFrameEvent which delegates the event. */    
    public void internalFrameOpened(InternalFrameEvent e) {
        String frameTitle = getTitle();
    }

    /**
     * This method is used to perform some operation when the internal frame is
     * closing.
     * @param e  InternalFrameEvent which delegates the event. */    
    public void internalFrameClosing(InternalFrameEvent e) {
    }

    /**
     * This method is used to perform some operation when the internal frame is
     * closed.
     * @param e  InternalFrameEvent which delegates the event. */    
    public void internalFrameClosed(InternalFrameEvent e) {
        //update to handle removing the open window menu item from Window Menu
        CoeusWindowMenu windowMenu = desktop.getWindowMenu();
        if( windowMenu != null ){
            windowMenu = windowMenu.removeMenuItem( this.getTitle() );
        }

        desktop.removeMenu(getFrameMenu());
        desktop.removeMenu(getToolsMenu());
        //prps start
        desktop.removeMenu(getActionsMenu());
        //prps end
        desktop.removeToolBar(getFrameToolBar());
                
        if (getFrame()!=null){
            desktop.removeFrame(getFrame());
        }
        //Commented By Sharath.K on 16 July 2003 as 
        //it is working without Recreating the whole Menu Bar.
        //Since Recreating whole MenuBar is a memory intense operation.
        
        //desktop.refreshWindowMenu(windowMenu);
        
        unloadMenus();
        BirtReportMenuBuilder birtReportMenuBuilder = BirtReportMenuBuilder.getInstance(desktop);
        birtReportMenuBuilder.removeReportMenu(this);
        /*setStatusMessage(coeusMessageResources.parseMessageKey("general_readyCode.2276"));*/
    }

    /**
     * This method is used to perform some operation when the internal frame is
     * iconified (minimized).
     * @param e  InternalFrameEvent which delegates the event. */    
    public void internalFrameIconified(InternalFrameEvent e) {
    }

    /**
     * This method is used to perform some operation when the internal frame is
     * deiconified.
     * @param e  InternalFrameEvent which delegates the event. */    
    public void internalFrameDeiconified(InternalFrameEvent e) {
    }

    /**
     * This method is used to add custom tool bar and menu bar to the application
     *  when the internal frame is activated.
     * @param e  InternalFrameEvent which delegates the event. */    
    public void internalFrameActivated(InternalFrameEvent e) {
        // recreate the menu bar
        Vector menus = new Vector();
        menus = desktop.getDefaultMenus();
        Object obj = menus.elementAt(0);
        if( obj instanceof CoeusFileMenu ){
            ((CoeusFileMenu)obj).formatMenuItems(mode);
        }
        menus.insertElementAt(getFrameMenu(), 1);
        
        //prps start
        if (getActionsMenu() != null)
        {
            menus.insertElementAt(getActionsMenu(), 2);
            menus.insertElementAt(getToolsMenu(),7);
        
        }    
        //prps end
        
        menus.insertElementAt(getToolsMenu(),6);
        
        
        //update to handle new window menu item to the existing Window Menu.
        CoeusWindowMenu windowMenu = desktop.getWindowMenu();
        if( windowMenu != null ){
            windowMenu = windowMenu.addNewMenuItem( this.getTitle(), this );
            windowMenu.updateCheckBoxMenuItemState( this.getTitle(), true );
        }

        BirtReportMenuBuilder birtReportMenuBuilder = BirtReportMenuBuilder.getInstance(desktop);
        birtReportMenuBuilder.setReportMenu(this);
        loadMenus(menus);
        
        desktop.refreshMenu(menus);
        desktop.refreshWindowMenu(windowMenu);

        //create all toolbars
        Vector allToolBars = new Vector();
        allToolBars.addElement(desktop.getDefaultToolBar());
        allToolBars.addElement(getFrameToolBar());
        
        desktop.refreshToolBars(allToolBars);
        /*setStatusMessage(coeusMessageResources.parseMessageKey("general_readyCode.2276"));*/
        
        //Bug Fix : 1034 - START
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                requestFocusInWindow();
            }
        });
        //Bug Fix : 1034 - END

    }

    /**
     * This method is used to remove the internal frame from the active windows
     * list in Window menu item.
     * @param e  InternalFrameEvent which delegates the event.
     */    
    public void internalFrameDeactivated(InternalFrameEvent e) {
        //de select the specific window Menu item in Window Menu.
        CoeusWindowMenu windowMenu = desktop.getWindowMenu();
        if( windowMenu != null ){
          windowMenu.updateCheckBoxMenuItemState( this.getTitle(), false );
        }
        
        BirtReportMenuBuilder birtReportMenuBuilder = BirtReportMenuBuilder.getInstance(desktop);
        birtReportMenuBuilder.hideReportMenu(this);
       
    }

    /**
     * This method is used to set the table to be shown in base windows which is
     * obtained from the search results.
     * @param searchResultsTable  instance of JTable obtained from search result.
     */    
    public void setSearchResultsTable(JTable searchResultsTable){
        this.searchResultsTable=searchResultsTable;
    }
    /**
     * This method is used to get the table to be shown in base windows which is
     * obtained from the search results.
     * @return searchResultsTable  instance of JTable obtained from search result.
     *   
     */    
    public JTable getSearchResultsTable(){
        return searchResultsTable;
    }

    /**
     * This method is used to set the data obtained from the search result.
     * @param results  Vector which consists of all the data obtained from
     * search.
     */    
    public void setResultData(Vector results){
        this.resultData = results;
    }

    /**
     * This method is used to get the data obtained from the search result.
     * @return results  Vector which consists of all the data obtained from
     * search.
     */    
    public Vector getResultData(){
        return this.resultData;
    }
    
    public final void setMenu(CoeusMenu coeusMenu, int position)
    {
        if(menus == null) menus = new HashMap();
        menus.put(new Integer(position),coeusMenu);
        desktop.addMenu(coeusMenu, position);
    }
    
    private void loadMenus(Vector menus)
    {
        if(this.menus == null) return ;
        int position;
        for(int count=0; count < this.menus.size(); count++)
        {
            position = Integer.parseInt((this.menus.keySet().toArray()[0]).toString());
            menus.insertElementAt((CoeusMenu)this.menus.get(new Integer(position)),position);
        }
    }
    
    private void unloadMenus()
    {
        if(this.menus == null) return ;
        int position;
        for(int count=0; count < menus.size(); count++)
        {
            position = Integer.parseInt((menus.keySet().toArray()[0]).toString());
            //desktop.getCoeusMenuBar().remove(position);
            desktop.removeMenu((JMenu)menus.get(new Integer(position)));
        }
        
    }
    
    /**
     * This abstract method must be implemented by all classes which inherits this class.
     * Used for saving the current activesheet when clicked Save from File menu.
     */
    public abstract void saveActiveSheet();
    
    /**
     * This abstract method must be implemented by all the classes which in
     */
    public abstract void saveAsActiveSheet();

   /** begin: bug id: #147  */
   /* provided public method to set the message to be displayed 
     in status bar. bug ID: #147 */
    /** 
     * Method used to set message in status bar 
     * @param message String representing the message to be displayed in status bar.
     */
   /*  
   public void setStatusMessage(String message){
       desktop.setStatusMessage(message);
   }*/
   /** end: bug id: #147  */
    
    public void setIcon(boolean icon)throws java.beans.PropertyVetoException{
        super.setIcon(icon);
        if(!icon){
            //Bring to focus
            if(this.isIcon()){
                this.setMaximum(true);
            }
        }
    }
    
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        //Bring to focus
        try{
            if(this.isIcon()){
                this.setMaximum(true);
            }
        }catch (java.beans.PropertyVetoException propertyVetoException) {
            propertyVetoException.printStackTrace();
        }
    }

    /**
     * @return the desktop
     */
     /**
     * MDIForm object
     */
    public CoeusAppletMDIForm getDesktop() {
        return desktop;
    }

    /**
     * @param desktop the desktop to set
     */
    public void setDesktop(CoeusAppletMDIForm desktop) {
        this.desktop = desktop;
    }

    /*public void actionPerformed(ActionEvent e) {
        try {
            CoeusGuiConstants.getMDIForm().setCursor(new Cursor(Cursor.WAIT_CURSOR));
            //blockEvents(true);
            JMenuItem menuItem = (JMenuItem) e.getSource();
            if (menuItem != null) {
                BirtReport birtReport = new BirtReport();
                birtReport.displayReport(menuItem.getActionCommand());
            }
        } finally {
            CoeusGuiConstants.getMDIForm().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }*/


}

