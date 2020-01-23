/*
 * CostElementListForm.java
 *
 * Created on Dec 1, 2004, 6:05 PM
 */

package edu.mit.coeus.admin.gui;

import javax.swing.*;
import javax.swing.event.InternalFrameEvent;
import java.util.Vector;
import java.awt.event.*;
import java.awt.*;



import edu.mit.coeus.gui.*;
import edu.mit.coeus.gui.toolbar.*;
import edu.mit.coeus.gui.menu.*;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;


/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * @author jinu
 */
public class CostElementListForm extends CoeusInternalFrame {
    
    public JTable tblCostElements;
    private JScrollPane scrPnCostElements;
    private CoeusAppletMDIForm mdiForm;
    private final String SEPERATOR="seperator";
    
    // Declaring the toolbar buttons
    public CoeusToolBarButton btnAddCostElement, btnModifyCostElement,btnCategory,
    btnOnCampus, btnOffCampus, btnSave, btnSaveas, btnClose;
    
    // Declaring the menu items for the File Menu
    public CoeusMenuItem mnuItmInbox, mnuItmClose,mnuItmSave,mnuItmSaveas,
    mnuItmSortCostElement, mnuItmChangePassword, mnuItmPreferences, mnuItmExit,
     /*Added for Case#3682 - Enhancements related to Delegations - Start*/
       mnuItmDelegations,
     /*Added for Case#3682 - Enhancements related to Delegations - End*/
    /*Csae 2110 Start*/mnuItmCurrentLocks/*Case 2110*/;
    
    // Declaring the menu items for the Edit menu
    public CoeusMenuItem mnuItmAddCostElement,mnuItmModifyCostElement,
    mnuItmCategory, mnuItmOnCampus, mnuItmOffCampus;
    
    private CoeusMenu mnuFile, mnuEdit, mnuTools;
    
    /** Creates a new instance of InstituteProposalBaseWindowForm */
    public CostElementListForm(String title, CoeusAppletMDIForm mdiForm){
        super(title,mdiForm);
        this.mdiForm = mdiForm;
    }
    
    public void initComponents() {
        
        setFrameToolBar(getCostElementToolBar());
        prepareMenus();
        
        tblCostElements = new javax.swing.JTable();
        
        scrPnCostElements = new JScrollPane(tblCostElements);
        scrPnCostElements.getViewport().setBackground(Color.white);
        getContentPane().add(scrPnCostElements);
    }
    
    private JToolBar getCostElementToolBar(){
        JToolBar costElementToolBar = new JToolBar();
        btnAddCostElement = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.ADD_ICON)),
        null, "Add CostElement");
        btnModifyCostElement = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.EDIT_ICON)),
        null, "Modify CostElement");
        btnCategory = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.CATEGORY_ICON)),
        null, "Modify Category");
        btnOnCampus = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.PROP_ACTIVE_ROLE_ICON)),
        null, "On Campus");
        btnOffCampus = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.PROP_INACTIVE_ROLE_ICON)),
        null, "Off Campus");
        btnSave = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.SAVE_ICON)),
        null, "Save");
        btnSaveas = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.SAVEAS_ICON)),
        null, "Save As");
        btnClose = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.CLOSE_ICON)),
        null, "Close");
        
        costElementToolBar.add(btnAddCostElement);
        costElementToolBar.add(btnModifyCostElement);
        costElementToolBar.add(btnCategory);
        costElementToolBar.add(btnOnCampus);
        costElementToolBar.add(btnOffCampus);
        costElementToolBar.addSeparator();
        costElementToolBar.add(btnSave);
        costElementToolBar.add(btnSaveas);
        costElementToolBar.addSeparator();
        costElementToolBar.add(btnClose);
        
        return costElementToolBar;
    }
    
    public void prepareMenus(){
        if(mnuFile== null){
            // Holds the File Menu details
            Vector vecFileMenu = new Vector();
            mnuItmInbox = new CoeusMenuItem("Inbox", null, true, true);
            mnuItmInbox.setMnemonic('I');
            
            mnuItmClose = new CoeusMenuItem("Close", null, true, true);
            mnuItmClose.setMnemonic('C');
            
            mnuItmSave = new CoeusMenuItem("Save...", null, true, true);
            mnuItmSave.setMnemonic('S');
            
            mnuItmSaveas = new CoeusMenuItem("SaveAs...", null, true, true);
            mnuItmSaveas.setMnemonic('A');
            
            mnuItmSortCostElement = new CoeusMenuItem("Sort...", null, true, true);
            mnuItmSortCostElement.setMnemonic('o');
            
            mnuItmChangePassword = new CoeusMenuItem("Change Password", null, true, true);
            mnuItmChangePassword.setMnemonic('h');
            
            //Added for Case#3682 - Enhancements related to Delegations - Start
            mnuItmDelegations = new CoeusMenuItem("Delegations...", null, true, true);
            mnuItmDelegations.setMnemonic('D');            
            //Added for Case#3682 - Enhancements related to Delegations - End
            
            mnuItmPreferences = new CoeusMenuItem("Preferences...", null, true, true);
            mnuItmPreferences.setMnemonic('P');
            
            mnuItmExit = new CoeusMenuItem("Exit", null, true, true);
            
            //Case 2110 Start
            mnuItmCurrentLocks = new CoeusMenuItem("Current Locks",null,true,true);
            mnuItmCurrentLocks.setMnemonic('L');
            //Case 2110 End
            mnuItmExit.setMnemonic('x');
            
            vecFileMenu.add(mnuItmInbox);
            vecFileMenu.add(SEPERATOR);
            vecFileMenu.add(mnuItmClose);
            vecFileMenu.add(SEPERATOR);
            vecFileMenu.add(mnuItmSave);
            vecFileMenu.add(mnuItmSaveas);
            //vecFileMenu.add(mnuItmSortCostElement);
            vecFileMenu.add(mnuItmChangePassword);
            //Case 2110 Start
            vecFileMenu.add(SEPERATOR);
            vecFileMenu.add(mnuItmCurrentLocks);
            vecFileMenu.add(SEPERATOR);
            //Case 2110 End
            //Added for Case#3682 - Enhancements related to Delegations - Start            
            vecFileMenu.add(mnuItmDelegations);
            vecFileMenu.add(SEPERATOR);            
            //Added for Case#3682 - Enhancements related to Delegations - End
            vecFileMenu.add(mnuItmPreferences);
            vecFileMenu.add(SEPERATOR);
            vecFileMenu.add(mnuItmExit);
            mnuFile = new CoeusMenu("File", null, vecFileMenu, true, true);
            mnuFile.setMnemonic('F');
        }
        if(mnuEdit == null){
            
            Vector vecEditMenu = new Vector();
            mnuItmAddCostElement = new CoeusMenuItem("Add CostElement", null, true, true);
            mnuItmAddCostElement.setMnemonic('A');
            
            mnuItmModifyCostElement = new CoeusMenuItem("Modify CostElement", null, true, true);
            mnuItmModifyCostElement.setMnemonic('M');
            
            mnuItmCategory = new CoeusMenuItem("Modify Category", null, true, true);
            mnuItmCategory.setMnemonic('C');
            
            mnuItmOnCampus = new CoeusMenuItem("On Campus", null, true, true);
            mnuItmOnCampus.setMnemonic('O');
            
            mnuItmOffCampus= new CoeusMenuItem("Off Campus", null, true, true);
            mnuItmOffCampus.setMnemonic('F');
            
            vecEditMenu.add(mnuItmAddCostElement);
            vecEditMenu.add(SEPERATOR);
            vecEditMenu.add(mnuItmModifyCostElement);
            vecEditMenu.add(mnuItmCategory);
            vecEditMenu.add(mnuItmOnCampus);
            vecEditMenu.add(mnuItmOffCampus);
            mnuEdit = new CoeusMenu("Edit", null, vecEditMenu, true, true);
            mnuEdit.setMnemonic('E');
        }
        
        //         if(mnuTools== null){
        //             Vector vecTools = new Vector();
        //
        //             mnuItmSearch = new CoeusMenuItem("Search", null, true, true);
        //             mnuItmSearch.setMnemonic('S');
        //             vecTools.add(mnuItmSearch);
        //             mnuTools = new CoeusMenu("Tools",null,vecTools,true,true);
        //             mnuTools.setMnemonic('T');
        //         }
    }
    
    private void loadMenus() {
        mdiForm.getCoeusMenuBar().remove(0);
        mdiForm.getCoeusMenuBar().add(mnuFile, 0);
        mdiForm.getCoeusMenuBar().add(mnuEdit, 1);
        //        mdiForm.getCoeusMenuBar().add(mnuTools, 6);
        mdiForm.getCoeusMenuBar().validate();
    }
    
    private void unloadMenus() {
        mdiForm.getCoeusMenuBar().remove(mnuFile);
        mdiForm.getCoeusMenuBar().add(new CoeusFileMenu(mdiForm).getMenu(), 0);
        mdiForm.getCoeusMenuBar().remove(mnuEdit);
        //        mdiForm.getCoeusMenuBar().remove(mnuTools);
        mdiForm.getCoeusMenuBar().validate();
    }
    
    public void display() {//JTable results
//        if(results == null) return ;
//        tblCostElements = results;
        scrPnCostElements.setViewportView(tblCostElements);
//        setColumnSizes();
        this.revalidate();
    }
    
    private void setColumnSizes() {
        int colSize[] = {75, 300, 250, 50};
        for(int columnIndex = 0; columnIndex < colSize.length; columnIndex++) {
            tblCostElements.getColumnModel().getColumn(columnIndex).setPreferredWidth(colSize[columnIndex]);
            tblCostElements.getColumnModel().getColumn(columnIndex).setMinWidth(colSize[columnIndex]);
        }
    }
    
    public void internalFrameActivated(InternalFrameEvent internalFrameEvent) {
        super.internalFrameActivated(internalFrameEvent);
        loadMenus();
    }
    
    public void internalFrameDeactivated(InternalFrameEvent internalFrameEvent) {
        unloadMenus();
        super.internalFrameDeactivated(internalFrameEvent);
    }

    public void saveActiveSheet() {
    }
    
    public void saveAsActiveSheet() {
    }
    
}
