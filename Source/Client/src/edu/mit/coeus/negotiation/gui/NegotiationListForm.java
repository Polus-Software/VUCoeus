/*
 * NegotiationListController.java
 *
 * Created on July 16, 2004, 11:30 AM
 */

package edu.mit.coeus.negotiation.gui;

/** 
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
* @author nadhgj
*/


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





public class NegotiationListForm extends CoeusInternalFrame {
    
    public JTable tblResults;
    private JScrollPane scrPnResults;
    private CoeusAppletMDIForm mdiForm;
    private final String SEPERATOR="seperator";
    
    // Declaring the toolbar buttons
    public CoeusToolBarButton btnNewNegotiation, btnCorrectNegotiation,btnDisplayNegotiation,
        btnMedusa,btnPrintAllNegotiation,btnSearchNegotiation,btnClose;
    //Added for Case#2805 - Save As functionality on Negotiation Search results - no icon on tool bar - Start
    public CoeusToolBarButton btnSaveas;
    //End - Case#2805
    public CoeusToolBarButton btnSort;//Added by nadh - 19/01/2005 for Sort ToolBar Button
    
    // Declaring the menu items for the File Menu
    public CoeusMenuItem mnuItmClose,mnuItmSaveas,mnuItmSort,mnuItmPrintAll,
        mnuItmChangePassword, 
        /*Added for Case#3682 - Enhancements related to Delegations - Start*/
        mnuItmDelegations,
        /*Added for Case#3682 - Enhancements related to Delegations - End*/
        mnuItmPreferences,mnuItmExit,/*Case 2110 Start */mnuCurrentLocks/*Case 2110 End*/;
    
// Declaring the menu items for the Edit menu
    public CoeusMenuItem mnuItmNewNegotiation,mnuItmDisplayNegotiation,
            mnuItmCorrectNegotiation, mnuItmMedusa;
    
    // Declaring the menu items for the Tools menu
    public CoeusMenuItem mnuItmSearch;
    
    private CoeusMenu mnuFile, mnuEdit, mnuTools;
    
    /** Creates a new instance of NegotiationBaseWindowForm */
    public NegotiationListForm(String title, CoeusAppletMDIForm mdiForm){
        super(title,mdiForm);
        this.mdiForm = mdiForm;
    }
    
     public void initComponents(JTable tblEmptyResults) {
        
        setFrameToolBar(getNegotiationToolBar());
        prepareMenus();
        
        tblResults = tblEmptyResults;
        
        scrPnResults = new JScrollPane(tblResults);
        scrPnResults.getViewport().setBackground(Color.white);
        getContentPane().add(scrPnResults);
        setColumnSizes();
    }
     
     private void setColumnSizes() {
        int colSize[] = {140, 100, 100, 100, 100, 100, 150, 150, 200, 120, 200};
        for(int columnIndex = 0; columnIndex < colSize.length; columnIndex++) {
            tblResults.getColumnModel().getColumn(columnIndex).setPreferredWidth(colSize[columnIndex]);
            tblResults.getColumnModel().getColumn(columnIndex).setMinWidth(colSize[columnIndex]);
        }
    }
     
     private JToolBar getNegotiationToolBar(){
          JToolBar negotiationToolBar = new JToolBar();
          
          btnNewNegotiation = new CoeusToolBarButton(new ImageIcon(
            getClass().getClassLoader().getResource(CoeusGuiConstants.ADD_ICON)),
            null, "Create a new Negotiation");
          btnCorrectNegotiation = new CoeusToolBarButton(new ImageIcon(
            getClass().getClassLoader().getResource(CoeusGuiConstants.EDIT_ICON)),
            null, "Correct Negotiation");
          btnDisplayNegotiation = new CoeusToolBarButton(new ImageIcon(
            getClass().getClassLoader().getResource(CoeusGuiConstants.DISPLAY_ICON)),
            null, "Display Negotiation");
          btnMedusa = new CoeusToolBarButton(new ImageIcon(
            getClass().getClassLoader().getResource(CoeusGuiConstants.MEDUSA_ICON)),
            null, "Medusa");
          btnSearchNegotiation = new CoeusToolBarButton(new ImageIcon(
            getClass().getClassLoader().getResource(CoeusGuiConstants.SEARCH_ICON)),
            null, "Search for Negotiation");
          btnPrintAllNegotiation = new CoeusToolBarButton(new ImageIcon(
            getClass().getClassLoader().getResource(CoeusGuiConstants.PRINT_ICON)),
            null, "Print Negotiation");
          //Added for Case#2805 - Save As functionality on Negotiation Search results - no icon on tool bar - Start
          btnSaveas = new CoeusToolBarButton(new ImageIcon(
            getClass().getClassLoader().getResource(CoeusGuiConstants.SAVEAS_ICON)),
            null, "Save As");
          //End - Case#2805
          btnClose = new CoeusToolBarButton(new ImageIcon(
            getClass().getClassLoader().getResource(CoeusGuiConstants.CLOSE_ICON)),
            null, "Close");
          
          //Added by nadh - 19/01/2005 for Sort ToolBar Button - Start
        btnSort = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.SORT_ICON)),null,
        "Sort Negotiations");
        //Added by Nadh - End
          
          negotiationToolBar.add(btnNewNegotiation);
          negotiationToolBar.add(btnCorrectNegotiation);
          negotiationToolBar.add(btnDisplayNegotiation);
          negotiationToolBar.addSeparator();
          negotiationToolBar.add(btnMedusa);
          negotiationToolBar.addSeparator();
          negotiationToolBar.add(btnPrintAllNegotiation);
          negotiationToolBar.add(btnSort);//Added by nadh - 19/01/2005 for Sort ToolBar Button
          negotiationToolBar.add(btnSearchNegotiation);
          //Added for Case#2805 - Save As functionality on Negotiation Search results - no icon on tool bar - Start
          negotiationToolBar.addSeparator();
          negotiationToolBar.add(btnSaveas);
          //End - Case#2805
          negotiationToolBar.addSeparator();
          negotiationToolBar.add(btnClose);
          
          return negotiationToolBar;
     }
     
     public void prepareMenus(){
          if(mnuFile== null){
         // Holds the File Menu details
             Vector vecFileMenu = new Vector();
             // Holds the Edit Menu details

              mnuItmClose = new CoeusMenuItem("Close", null, true, true);
              mnuItmClose.setMnemonic('C');

              mnuItmSaveas = new CoeusMenuItem("SaveAs...", null, true, true);
              mnuItmSaveas.setMnemonic('A');

              mnuItmPrintAll = new CoeusMenuItem("Print All", null, true, true);
              mnuItmPrintAll.setMnemonic('P');
              mnuItmPrintAll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));

              mnuItmChangePassword = new CoeusMenuItem("Change Password", null, true, true);
              mnuItmChangePassword.setMnemonic('h');
              
              //Added for Case#3682 - Enhancements related to Delegations - Start             
             mnuItmDelegations= new CoeusMenuItem("Delegations...", null, true, true);
             mnuItmDelegations.setMnemonic('g');
             //Added for Case#3682 - Enhancements related to Delegations - End 
              mnuItmPreferences = new CoeusMenuItem("Preferences...", null, true, true);
              mnuItmPreferences.setMnemonic('P');
              
              //Start of bug fix 1651
              mnuItmExit = new CoeusMenuItem("Exit", null, true, true);
              mnuItmExit.setMnemonic('x');
              // End of bug fix 1651
              
              //Case 2110 Start
              mnuCurrentLocks = new CoeusMenuItem("Current Locks",null,true,true);
              mnuCurrentLocks.setMnemonic('L');
              //Case 2110 End

              mnuItmSort = new CoeusMenuItem("Sort...",null,true,true);
              mnuItmSort.setMnemonic('o');
              
              vecFileMenu.add(mnuItmClose);
              vecFileMenu.add(SEPERATOR);
              vecFileMenu.add(mnuItmSaveas);
              vecFileMenu.add(SEPERATOR);
              vecFileMenu.add(mnuItmSort);
              vecFileMenu.add(SEPERATOR);
              vecFileMenu.add(mnuItmPrintAll);
              vecFileMenu.add(SEPERATOR);
              vecFileMenu.add(mnuItmChangePassword);
              vecFileMenu.add(SEPERATOR);
              vecFileMenu.add(mnuCurrentLocks);
              vecFileMenu.add(SEPERATOR);
              //Added for Case#3682 - Enhancements related to Delegations - Start 
              vecFileMenu.add(mnuItmDelegations);
              vecFileMenu.add(SEPERATOR);
              //Added for Case#3682 - Enhancements related to Delegations - End 
              vecFileMenu.add(mnuItmPreferences);
              vecFileMenu.add(mnuItmExit);//bug fix id 1651
              mnuFile = new CoeusMenu("File", null, vecFileMenu, true, true);
              mnuFile.setMnemonic('F');
          }
          if(mnuEdit == null){
             Vector vecEditMenu = new Vector();
              mnuItmNewNegotiation = new CoeusMenuItem("Add Negotiation", null, true, true);
              mnuItmNewNegotiation.setMnemonic('N');

              mnuItmDisplayNegotiation = new CoeusMenuItem("Display Negotiation", null, true, true);
              mnuItmDisplayNegotiation.setMnemonic('D');

              mnuItmCorrectNegotiation = new CoeusMenuItem("Correct Negotiation", null, true, true);
              mnuItmCorrectNegotiation.setMnemonic('C');

              mnuItmMedusa= new CoeusMenuItem("Medusa...", null, true, true);
              mnuItmMedusa.setMnemonic('M');
              mnuItmMedusa.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, ActionEvent.CTRL_MASK));


              vecEditMenu.add(mnuItmNewNegotiation);
              vecEditMenu.add(mnuItmCorrectNegotiation);
              vecEditMenu.add(mnuItmDisplayNegotiation);
              vecEditMenu.add(SEPERATOR);
              vecEditMenu.add(mnuItmMedusa);
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
    public void saveActiveSheet() {
    }
    
    public void saveAsActiveSheet() {
    }
    
}
