/*
 * SubcontractListForm.java
 *
 * Created on September 4, 2004, 11:32 AM
 */

package edu.mit.coeus.subcontract.gui;

import javax.swing.*;
import javax.swing.event.InternalFrameEvent;
import java.util.Vector;
import java.awt.event.*;
import java.awt.*;


import edu.mit.coeus.gui.CoeusInternalFrame;
import edu.mit.coeus.gui.*;
import edu.mit.coeus.gui.toolbar.*;
import edu.mit.coeus.gui.menu.*;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;

/**Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * @author  nadhgj
 */
public class SubcontractListForm extends CoeusInternalFrame {
    
    public JTable tblResults;
    private JScrollPane scrPnResults;
    private CoeusAppletMDIForm mdiForm;
    private final String SEPERATOR="seperator";
    
    // Declaring the toolbar buttons
    public CoeusToolBarButton btnNewSubcontract, btnCorrectSubcontract,btnDisplaySubcontract,
        btnNewEntry,btnSortSubcontracts,btnSaveAs,btnSearchSubcontract,btnClose;
    
    // Declaring the menu items for the File Menu
    public CoeusMenuItem mnuItmClose,mnuItmSaveas,mnuItmInbox,mnuItmSort,
        /*Added for Case#3682 - Enhancements related to Delegations - Start*/
        mnuItmDelegations,
        /*Added for Case#3682 - Enhancements related to Delegations - End*/
        mnuItmChangePassword, mnuItmPreferences,mnuItmExit;
    
    // Declaring the menu items for the Edit menu
    public CoeusMenuItem mnuItmNewSubcontract,mnuItmDisplaySubcontract,
            mnuItmCorrectSubcontract, mnuItmNewEntry,/*Case 2110 Start*/mnuItmCurrentLocks/*Case 2110 End*/;
    
    // Declaring the menu items for the Tools menu
    public CoeusMenuItem mnuItmSearch;
    
    private CoeusMenu mnuFile, mnuEdit, mnuTools;
    
    
    /** Creates a new instance of SubcontractListForm */
    public SubcontractListForm(String title, CoeusAppletMDIForm mdiForm) {
        super(title,mdiForm);
        this.mdiForm = mdiForm;
    }
    
    public void saveActiveSheet() {
    }
    
    public void saveAsActiveSheet() {
    }
    
     public void initComponents(JTable tblEmptyResults) {
        
        setFrameToolBar(getSubcontractToolBar());
        prepareMenus();
        
        tblResults = tblEmptyResults;
        
        scrPnResults = new JScrollPane(tblResults);
        scrPnResults.getViewport().setBackground(Color.white);
        getContentPane().add(scrPnResults);
        setColumnSizes();
    }
    
    private void setColumnSizes() {
        int colSize[] = {120, 100, 120, 100, 100, 100, 120, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 105, 100, 100};
        for(int columnIndex = 0; columnIndex < colSize.length; columnIndex++) {
            tblResults.getColumnModel().getColumn(columnIndex).setPreferredWidth(colSize[columnIndex]);
            tblResults.getColumnModel().getColumn(columnIndex).setMinWidth(colSize[columnIndex]);
        }
    }
    
    private JToolBar getSubcontractToolBar(){
          JToolBar subcontractToolBar = new JToolBar();
          
          btnNewSubcontract = new CoeusToolBarButton(new ImageIcon(
            getClass().getClassLoader().getResource(CoeusGuiConstants.NEW_ICON)),
            null, "Create a new Subcontract");
          btnCorrectSubcontract = new CoeusToolBarButton(new ImageIcon(
            getClass().getClassLoader().getResource(CoeusGuiConstants.EDIT_ICON)),
            null, "Correct Subcontract");
          btnDisplaySubcontract = new CoeusToolBarButton(new ImageIcon(
            getClass().getClassLoader().getResource(CoeusGuiConstants.DISPLAY_ICON)),
            null, "Display Subcontract");
          btnNewEntry = new CoeusToolBarButton(new ImageIcon(
            getClass().getClassLoader().getResource(CoeusGuiConstants.AWARD_NEW_ENTRY_ICON)),
            null, "Subcontract New Entry");
          btnSearchSubcontract = new CoeusToolBarButton(new ImageIcon(
            getClass().getClassLoader().getResource(CoeusGuiConstants.SEARCH_ICON)),
            null, "Search for Subcontract");
          btnSaveAs = new CoeusToolBarButton(new ImageIcon(
            getClass().getClassLoader().getResource(CoeusGuiConstants.SAVEAS_ICON)),
            null, "Save As");
          btnSortSubcontracts = new CoeusToolBarButton(new ImageIcon(
            getClass().getClassLoader().getResource(CoeusGuiConstants.SORT_ICON)),
            null, "Sort Subcontracts");
          btnClose = new CoeusToolBarButton(new ImageIcon(
            getClass().getClassLoader().getResource(CoeusGuiConstants.CLOSE_ICON)),
            null, "Close");
          
          subcontractToolBar.add(btnNewSubcontract);
          subcontractToolBar.add(btnCorrectSubcontract);
          subcontractToolBar.add(btnDisplaySubcontract);
          subcontractToolBar.add(btnNewEntry);
          subcontractToolBar.addSeparator();
          subcontractToolBar.add(btnSortSubcontracts);
          subcontractToolBar.add(btnSearchSubcontract);
          subcontractToolBar.add(btnSaveAs);
          subcontractToolBar.addSeparator();
          subcontractToolBar.add(btnClose);
          
          return subcontractToolBar;
     }
    
    public void prepareMenus(){
          if(mnuFile== null){
         // Holds the File Menu details
             Vector vecFileMenu = new Vector();
             // Holds the Edit Menu details
             
              mnuItmInbox = new CoeusMenuItem("Inbox", null, true, true);
              mnuItmInbox.setMnemonic('I');
              
              mnuItmSort = new CoeusMenuItem("Sort...", null, true, true);
              mnuItmSort.setMnemonic('o');

              mnuItmClose = new CoeusMenuItem("Close", null, true, true);
              mnuItmClose.setMnemonic('C');

              mnuItmSaveas = new CoeusMenuItem("SaveAs...", null, true, true);
              mnuItmSaveas.setMnemonic('A');

              mnuItmChangePassword = new CoeusMenuItem("Change Password", null, true, true);
              mnuItmChangePassword.setMnemonic('h');
              
              //Case 2110 Start 
              mnuItmCurrentLocks = new CoeusMenuItem("Current Locks",null,true,true);
              mnuItmCurrentLocks.setMnemonic('L');              
              //Case 2110 End
             
             //Added for Case#3682 - Enhancements related to Delegations - Start
             mnuItmDelegations= new CoeusMenuItem("Delegations...", null, true, true);
             mnuItmDelegations.setMnemonic('g');
             //Added for Case#3682 - Enhancements related to Delegations - End
             
              mnuItmPreferences = new CoeusMenuItem("Preferences...", null, true, true);
              mnuItmPreferences.setMnemonic('r');
              
              mnuItmExit = new CoeusMenuItem("Exit", null, true, true);
              mnuItmExit.setMnemonic('x');

              
              vecFileMenu.add(mnuItmInbox);
              vecFileMenu.add(SEPERATOR);
              vecFileMenu.add(mnuItmSaveas);
              vecFileMenu.add(SEPERATOR);
              vecFileMenu.add(mnuItmSort);
              vecFileMenu.add(mnuItmClose);
              vecFileMenu.add(mnuItmChangePassword);
              vecFileMenu.add(SEPERATOR);
              vecFileMenu.add(mnuItmCurrentLocks);
              vecFileMenu.add(SEPERATOR);
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
              mnuItmNewSubcontract = new CoeusMenuItem("New Subcontract", null, true, true);
              mnuItmNewSubcontract.setMnemonic('N');

              mnuItmDisplaySubcontract = new CoeusMenuItem("Display Subcontract", null, true, true);
              mnuItmDisplaySubcontract.setMnemonic('D');

              mnuItmCorrectSubcontract = new CoeusMenuItem("Correct Subcontract", null, true, true);
              mnuItmCorrectSubcontract.setMnemonic('C');

              mnuItmNewEntry= new CoeusMenuItem("New Entry", null, true, true);
              mnuItmNewEntry.setMnemonic('E');

              vecEditMenu.add(mnuItmNewSubcontract);
              vecEditMenu.add(mnuItmCorrectSubcontract);
              vecEditMenu.add(mnuItmDisplaySubcontract);
              vecEditMenu.add(mnuItmNewEntry);
              mnuEdit = new CoeusMenu("Edit", null, vecEditMenu, true, true);
              mnuEdit.setMnemonic('E');
          }
          
          if(mnuTools== null){
               Vector vecTools = new Vector();
              
              mnuItmSearch = new CoeusMenuItem("Search", null, true, true);
              mnuItmSearch.setMnemonic('S');
              vecTools.add(mnuItmSearch);
              mnuTools = new CoeusMenu("Tools",null,vecTools,true,true);
              mnuTools.setMnemonic('T');
          }
     }
    
     private void loadMenus() {
        mdiForm.getCoeusMenuBar().remove(0);
        mdiForm.getCoeusMenuBar().add(mnuFile, 0);
        mdiForm.getCoeusMenuBar().add(mnuEdit, 1);
        mdiForm.getCoeusMenuBar().add(mnuTools, 6);
        mdiForm.getCoeusMenuBar().validate();
    }
    
    private void unloadMenus() {
        mdiForm.getCoeusMenuBar().remove(mnuFile);
        mdiForm.getCoeusMenuBar().add(new CoeusFileMenu(mdiForm).getMenu(), 0);
        mdiForm.getCoeusMenuBar().remove(mnuEdit);
        mdiForm.getCoeusMenuBar().remove(mnuTools);
        
        mdiForm.getCoeusMenuBar().validate();
    }
    
    public void displayResults(JTable results) {
        if(results == null) return ;
        tblResults = results;
        scrPnResults.setViewportView(tblResults);
        setColumnSizes();
        this.revalidate();
    }
    
    public void internalFrameActivated(InternalFrameEvent internalFrameEvent) {
        super.internalFrameActivated(internalFrameEvent);
        loadMenus();
    }
    
    public void internalFrameDeactivated(InternalFrameEvent internalFrameEvent) {
        unloadMenus();
        super.internalFrameDeactivated(internalFrameEvent);
        
    }
     
}


