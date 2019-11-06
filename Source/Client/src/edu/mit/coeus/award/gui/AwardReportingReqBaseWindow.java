


/*
 * AwardReportingReqBaseWindow.java
 *
 * Created on July 29, 2004, 7:09 PM
 * @author   bijosht
 */

/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.award.gui;

import edu.mit.coeus.gui.toolbar.CoeusToolBarButton;
import edu.mit.coeus.gui.menu.*;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.gui.CoeusInternalFrame;

import javax.swing.JCheckBoxMenuItem;
import java.util.Vector;
import javax.swing.JToolBar;
import javax.swing.ImageIcon;
import javax.swing.event.InternalFrameEvent;


public class AwardReportingReqBaseWindow extends CoeusInternalFrame {
    
    public CoeusToolBarButton btnPrintAll, btnPrintSelected, btnSaveAs,
    btnSearch,btnView1,btnView2,btnView3,btnCustomizeView,btnClose;
    
    // To indicate horizondal seperator in menu items
     
    private final String SEPERATOR="seperator";
    
    //File Menu Items
    public CoeusMenuItem mnuItmInbox, mnuItmPrintSetup,mnuItmClose, mnuItmSave,
    mnuItmChangePassword,
    /*Added for Case#3682 - Enhancements related to Delegations- Start*/
    mnuItmDelegations,
    /*Added for Case#3682 - Enhancements related to Delegations - End*/ 
    mnuItmPreferences, mnuItmExit,/*Case 2110 Start*/mnuItmCurrentLocks/*Case 2110 End*/;
    
    //View Menu Items
    public CoeusMenuItem mnuItmCustomize, mnuItmDisplayAward,mnuItmReportingReqAward;
    
    //Custom Views Menu items
    public JCheckBoxMenuItem mnuItmView1,  mnuItmView2,mnuItmView3;
    
    private CoeusMenu mnuFile, mnuView,mnuCustomViews;
    
    CoeusAppletMDIForm mdiForm;
    char functionType;
    
    /** Creates a new instance of AwardReportingReqBaseWindow */
    public AwardReportingReqBaseWindow(String title,CoeusAppletMDIForm mdiForm) {
        super(title, mdiForm);
        //this.functionType = functionType;
        this.mdiForm = mdiForm;
        initComponents();
    }
    
    public void initComponents() {
        setFrameToolBar(getReportingReqBaseWindowToolBar());
        prepareMenus();
    }
/*
 * Method which adds tool bar buttons
 */
    private JToolBar getReportingReqBaseWindowToolBar() {
        JToolBar toolBar = new JToolBar();
        
        btnPrintAll = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.PRINT_ICON)),
        null, "Print All");
        
        btnPrintSelected = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.PRINT_SELECTED_ICON)),
        null, "Print selected group");
        
        btnSaveAs = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.SAVEAS_ICON)),
        null, "Save As");
        
        btnSearch = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.SEARCH_ICON)),
        null, "Search");
        
        btnCustomizeView =new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.CUSTOMIZE_VIEW_ICON)),
        null, "Customize View");
        
        btnView1 = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.VIEW1_ICON)),
        null, "View 1");
        
        btnView2 = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.VIEW2_ICON)),
        null, "View 2");
        
        btnView3 = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.VIEW3_ICON)),
        null, "View 3");
        
        btnClose = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.CLOSE_ICON)),
        null, "Close");
        
        toolBar.add(btnPrintAll);
        toolBar.add(btnPrintSelected);
        toolBar.add(btnSaveAs);
        //toolBar.addSeparator();
        toolBar.add(btnSearch);
        toolBar.add(btnView1);
        toolBar.add(btnView2);
        toolBar.add(btnView3);
        toolBar.add(btnCustomizeView);
        toolBar.add(btnClose);
        return toolBar;
    }
    
    /**
     * Method to create the menu items
     */
    private void prepareMenus() {
        //build File Menu
        Vector vecFile = new Vector();
        
        mnuItmInbox = new CoeusMenuItem("Inbox", null, true, true);
        mnuItmInbox.setMnemonic('I');
        
        mnuItmPrintSetup = new CoeusMenuItem("PrintSetup", null, true, true);
        mnuItmPrintSetup.setMnemonic('u');
        
        mnuItmClose = new CoeusMenuItem("Close", null, true, true);
        mnuItmClose.setMnemonic('C');
        
        mnuItmSave = new CoeusMenuItem("Save As", null, true, true);
        mnuItmSave.setMnemonic('S');
        //mnuItmSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        
        mnuItmChangePassword = new CoeusMenuItem("Change Password", null, true, true);
        mnuItmChangePassword.setMnemonic('h');
        
        //Added for Case#3682 - Enhancements related to Delegations - Start  
        mnuItmDelegations = new CoeusMenuItem("Delegations", null, true, true);
        mnuItmDelegations.setMnemonic('g');
        //Added for Case#3682 - Enhancements related to Delegations - End
        
        mnuItmPreferences = new CoeusMenuItem("Preferences", null, true, true);
        mnuItmPreferences.setMnemonic('P');
        
        mnuItmExit = new CoeusMenuItem("Exit", null, true, true);
        mnuItmExit.setMnemonic('x');
        
        //Case 2110 Start
        mnuItmCurrentLocks = new CoeusMenuItem("Current Locks",null,true,true);
        mnuItmCurrentLocks.setMnemonic('L');
        //Case 2110 End
        
        //Build View Menu
        mnuItmCustomize = new CoeusMenuItem("Customize", null, true, true);
        mnuItmCustomize.setMnemonic('C');
        mnuItmView1 = new JCheckBoxMenuItem("View 1",true);
        mnuItmView2 = new JCheckBoxMenuItem("View 2",false);
        mnuItmView3 = new JCheckBoxMenuItem("View 3",false);
        
        Vector vecCustomViews = new Vector();
        
        vecCustomViews.add(mnuItmView1);
        vecCustomViews.add(mnuItmView2);
        vecCustomViews.add(mnuItmView3);
        
        mnuCustomViews = new CoeusMenu("Custom Views", null, vecCustomViews,true, true);
        
        mnuItmDisplayAward = new CoeusMenuItem("Display Award", null, true, true);
        mnuItmDisplayAward.setMnemonic('D');
        
        mnuItmReportingReqAward = new CoeusMenuItem("Reporting Requirements for Award", null, true, true);
        mnuItmReportingReqAward.setMnemonic('R');
        
        vecFile.add(mnuItmInbox);
        vecFile.add(SEPERATOR);
        vecFile.add(mnuItmClose);
        vecFile.add(mnuItmSave);
        vecFile.add(SEPERATOR);
        vecFile.add(mnuItmPrintSetup);
        vecFile.add(SEPERATOR);
        vecFile.add(mnuItmChangePassword);
        //Case 2110 Start
        vecFile.add(SEPERATOR);
        vecFile.add(mnuItmCurrentLocks);
        vecFile.add(SEPERATOR);
        //Case 2110 End
        //Added for Case#3682 - Enhancements related to Delegations - Start  
        vecFile.add(SEPERATOR);
        vecFile.add(mnuItmDelegations);
        vecFile.add(SEPERATOR);
        //Added for Case#3682 - Enhancements related to Delegations - End
        vecFile.add(mnuItmPreferences);
        vecFile.add(SEPERATOR);
        vecFile.add(mnuItmExit);
        
        Vector vecView = new Vector();
        vecView.add(mnuItmCustomize);
        vecView.add(mnuCustomViews);
        
        vecView.add(SEPERATOR);
        vecView.add(mnuItmDisplayAward);
        vecView.add(mnuItmReportingReqAward);
        
        mnuFile = new CoeusMenu("File", null, vecFile, true, true);
        mnuFile.setMnemonic('F');
        
        mnuView = new CoeusMenu("View", null, vecView, true, true);
        mnuView.setMnemonic('V');
    }
    
    private void loadMenus() {
        mdiForm.getCoeusMenuBar().remove(0);
        mdiForm.getCoeusMenuBar().add(mnuFile, 0);
        mdiForm.getCoeusMenuBar().add(mnuView, 1);
    }
    public void display() {
        
    }
    private void unloadMenus() {
        mdiForm.getCoeusMenuBar().remove(mnuFile);
        mdiForm.getCoeusMenuBar().add(new CoeusFileMenu(mdiForm).getMenu(), 0);
        mdiForm.getCoeusMenuBar().remove(mnuView);
    }
    
    public void internalFrameActivated(InternalFrameEvent e) {
         super.internalFrameActivated(e);
        loadMenus();
        mdiForm.getCoeusMenuBar().revalidate();
    }
    
    public void internalFrameDeactivated(InternalFrameEvent e) {
        unloadMenus();
        super.internalFrameDeactivated(e);
        mdiForm.getCoeusMenuBar().revalidate();
    }
    
    public void saveActiveSheet() {
    }
    
    public void saveAsActiveSheet() {
    }
    
}
