/*
 * ReportingReqBaseWindow.java
 *
 * Created on July 15, 2004, 10:45 AM
 * @author   bijosht
 */

/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.award.gui;

import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusInternalFrame;
import edu.mit.coeus.gui.menu.CoeusFileMenu;
import edu.mit.coeus.gui.menu.CoeusMenu;
import edu.mit.coeus.gui.menu.CoeusMenuItem;
import edu.mit.coeus.gui.toolbar.CoeusToolBarButton;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.TypeConstants;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.event.InternalFrameEvent;


public class ReportingReqBaseWindow extends CoeusInternalFrame {
    private CoeusAppletMDIForm mdiForm;
    
    public CoeusToolBarButton btnAdd, btnEdit, btnSelectAll, btnCustomizeView,
    btnSave, btnClose;
    
    /*
     * to indicate horizondal seperator in menu items
     */
    private final String SEPERATOR="seperator";
    
    //File Menu Items
    public CoeusMenuItem mnuItmInbox, mnuItmPrintSetup,mnuItmClose, mnuItmSave,
    mnuItmChangePassword,
    /*Added for Case#3682 - Enhancements related to Delegations - Start*/
    mnuItmDelegations,
    /*Added for Case#3682 - Enhancements related to Delegations - End*/ 
    mnuItmPreferences, mnuItmExit,
    /*Case 2110 Start*/mnuItmCurrentLocks/*Case 2110 End*/;
    
    //Edit Menu Items
    public CoeusMenuItem mnuItmSelectAll, mnuItmAdd, mnuItmModify;
    
    private CoeusMenu mnuFile, mnuEdit;
    
    private char functionType;
    
    /** Creates a new instance of ReportingReqBaseWindow */
    public ReportingReqBaseWindow(String title, char functionType,CoeusAppletMDIForm mdiForm) {
        super(title, mdiForm);
        this.functionType = functionType;
        this.mdiForm = mdiForm;
        initComponents();
    }
    
    private void initComponents() {
        setFrameToolBar(getReportingReqBaseWindowToolBar());
        prepareMenus();
    }
    
    public void saveActiveSheet() {
    }
    
    public void saveAsActiveSheet() {
    }
    
    private JToolBar getReportingReqBaseWindowToolBar() {
        JToolBar toolBar = new JToolBar();
        
        btnAdd = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.ADD_ICON)),
        null, "Add new reporting requirements");
        
        btnEdit = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.EDIT_ICON)),
        null, "Edit Reporting Requirements");
        
        btnSelectAll = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.SELECT_ALL_ICON)),
        null, "Select All");
        
        btnCustomizeView = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.CUSTOMIZE_VIEW_ICON)),
        null, "Customize View");
        
        btnSave = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.SAVE_ICON)),
        null, "Save");
        
        btnClose = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.CLOSE_ICON)),
        null, "Close");
        if (functionType==TypeConstants.DISPLAY_MODE) {
            btnAdd.setEnabled(false);
            btnEdit.setEnabled(false);
            btnSelectAll.setEnabled(false);
            btnSave.setEnabled(false);
        }
        toolBar.add(btnAdd);
        toolBar.add(btnEdit);
        toolBar.add(btnSelectAll);
        toolBar.addSeparator();
        toolBar.add(btnCustomizeView);
        toolBar.add(btnSave);
        toolBar.addSeparator();
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
        
        mnuItmSave = new CoeusMenuItem("Save", null, true, true);
        mnuItmSave.setMnemonic('S');
        mnuItmSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        
        mnuItmChangePassword = new CoeusMenuItem("Change Password", null, true, true);
        mnuItmChangePassword.setMnemonic('h');
        
        //Added for Case#3682 - Enhancements related to Delegations - Start 
        mnuItmDelegations = new CoeusMenuItem("Delegations", null, true, true);
        mnuItmDelegations.setMnemonic('D');
        //Added for Case#3682 - Enhancements related to Delegations - End
        
        mnuItmPreferences = new CoeusMenuItem("Preferences", null, true, true);
        mnuItmPreferences.setMnemonic('P');
        
        mnuItmExit = new CoeusMenuItem("Exit", null, true, true);
        mnuItmExit.setMnemonic('x');
          
        //Build Edit Menu
        mnuItmSelectAll = new CoeusMenuItem("Select All", null, true, true);
        mnuItmSelectAll.setMnemonic('l');
        mnuItmSelectAll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
        
        mnuItmAdd = new CoeusMenuItem("Add", null, true, true);
        mnuItmAdd.setMnemonic('A');
        
        mnuItmModify = new CoeusMenuItem("Modify", null, true, true);
        mnuItmModify.setMnemonic('M');
        if (functionType==TypeConstants.DISPLAY_MODE) {
            mnuItmAdd.setEnabled(false);
            mnuItmModify.setEnabled(false);
            mnuItmSelectAll.setEnabled(false);
            mnuItmSave.setEnabled(false);
        }
        //Case 2110 Start
        mnuItmCurrentLocks = new CoeusMenuItem("Current Locks",null,true,true);
        mnuItmCurrentLocks.setMnemonic('L');
        //Case 2110 End
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
        vecFile.add(mnuItmDelegations);
        vecFile.add(SEPERATOR);
        //Added for Case#3682 - Enhancements related to Delegations - end
        vecFile.add(mnuItmPreferences);
        vecFile.add(SEPERATOR);
        vecFile.add(mnuItmExit);
        
        Vector vecEdit = new Vector();
        vecEdit.add(mnuItmSelectAll);
        vecEdit.add(SEPERATOR);
        vecEdit.add(mnuItmAdd);
        vecEdit.add(mnuItmModify);
        
        mnuFile = new CoeusMenu("File", null, vecFile, true, true);
        mnuFile.setMnemonic('F');
        
        mnuEdit = new CoeusMenu("Edit", null, vecEdit, true, true);
        mnuEdit.setMnemonic('E');
    }
    
    private void loadMenus() {
        mdiForm.getCoeusMenuBar().remove(0);
        mdiForm.getCoeusMenuBar().add(mnuFile, 0);
        mdiForm.getCoeusMenuBar().add(mnuEdit, 1);
    }
    
    private void unloadMenus() {
        mdiForm.getCoeusMenuBar().remove(mnuFile);
        mdiForm.getCoeusMenuBar().add(new CoeusFileMenu(mdiForm).getMenu(), 0);
        mdiForm.getCoeusMenuBar().remove(mnuEdit);
    }
    
   public void internalFrameActivated(InternalFrameEvent e) {
        super.internalFrameActivated(e);
        loadMenus();
    }
    
    public void internalFrameDeactivated(InternalFrameEvent e) {
        unloadMenus();
        super.internalFrameDeactivated(e);
    }    
}
