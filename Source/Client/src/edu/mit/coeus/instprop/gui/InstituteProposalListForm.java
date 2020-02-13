/*
 * InstituteProposalBaseWindowForm.java
 *
 * Created on April 23, 2004, 3:05 PM
 */

package edu.mit.coeus.instprop.gui;

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
* @author chandru
*/
public class InstituteProposalListForm extends CoeusInternalFrame {
    
    public JTable tblResults;
    private JScrollPane scrPnResults;
    private CoeusAppletMDIForm mdiForm;
    private final String SEPERATOR="seperator";
    
    // Declaring the toolbar buttons
    public CoeusToolBarButton btnNewProposal, btnCorrectProposal,btnDisplayProposal,
        btnNewEntryProposal, btnIpReview, btnNegotiation, btnNotepad, btnMedusa,
        btnSortProposal, btnSearchProposal, btnSaveas, btnClose;
    
    // Declaring the menu items for the File Menu
    public CoeusMenuItem mnuItmInbox, mnuItmClose,mnuItmSaveas,/*mnuItmPrintSetUp,*/
        mnuItmSortProposal, mnuItmChangePassword, mnuItmPreferences, mnuItmExit,mnuItmSearch,
        /*Added for Case#3682 - Enhancements related to Delegations - Start*/
        mnuItmDelegations,
        /*Added for Case#3682 - Enhancements related to Delegations -End */
        /*Case 2110 Start*/mnuItmCurrentLocks/*Case 2110 End*/;
    //Added for COEUSQA-1525 : Attachments for Institute Proposal - Start
    public CoeusMenuItem mnuItmAttachments;
    public CoeusToolBarButton btnAttachments;
    //COEUSQA-1525 : End
    // Declaring the menu items for the Edit menu
    public CoeusMenuItem mnuItmNewProposal,mnuItmDisplayProposal,
            mnuItmCorrectProposal, mnuItmNewEntry, mnuItmIPReview,
            mnuItmNegotiation,mnuItmNotepad,mnuItmMedusa;
    
    private CoeusMenu mnuFile, mnuEdit, mnuTools;
    
    /** Creates a new instance of InstituteProposalBaseWindowForm */
    public InstituteProposalListForm(String title, CoeusAppletMDIForm mdiForm){
        super(title,mdiForm);
        this.mdiForm = mdiForm;
    }
    
     public void initComponents(JTable tblEmptyResults) {
        
        setFrameToolBar(getInstProposalToolBar());
        prepareMenus();
        
        tblResults = tblEmptyResults;
        
        scrPnResults = new JScrollPane(tblResults);
        scrPnResults.getViewport().setBackground(Color.white);
        getContentPane().add(scrPnResults);
        // Bug Fix - To avoid setting the table column sizes. 
        // The sizes are set in CoeusSearch.XML - Start
        //setColumnSizes();
        // End
    }
     // Bug Fix - To avoid setting the table column sizes. 
        // The sizes are set in CoeusSearch.XML - Start
//     private void setColumnSizes() {
//        int colSize[] = {150, 100, 150, 100, 100, 75, 200, 100, 200, 200, 0};
//        for(int columnIndex = 0; columnIndex < colSize.length; columnIndex++) {
//            tblResults.getColumnModel().getColumn(columnIndex).setPreferredWidth(colSize[columnIndex]);
//            tblResults.getColumnModel().getColumn(columnIndex).setMinWidth(colSize[columnIndex]);
//        }
//    }// end
     
     private JToolBar getInstProposalToolBar(){
          JToolBar instPropToolBar = new JToolBar();
          
          btnNewProposal = new CoeusToolBarButton(new ImageIcon(
            getClass().getClassLoader().getResource(CoeusGuiConstants.ADD_ICON)),
            null, "Create a new proposal");
          btnCorrectProposal = new CoeusToolBarButton(new ImageIcon(
            getClass().getClassLoader().getResource(CoeusGuiConstants.EDIT_ICON)),
            null, "Correct Proposal");
          btnDisplayProposal = new CoeusToolBarButton(new ImageIcon(
            getClass().getClassLoader().getResource(CoeusGuiConstants.DISPLAY_ICON)),
            null, "Display Proposal");
          btnNewEntryProposal = new CoeusToolBarButton(new ImageIcon(
            getClass().getClassLoader().getResource(CoeusGuiConstants.AWARD_NEW_ENTRY_ICON)),
            null, "Proposal New Entry");
          btnIpReview = new CoeusToolBarButton(new ImageIcon(
            getClass().getClassLoader().getResource(CoeusGuiConstants.IP_REVIEW_ICON)),
            null, "IP Review");
          btnNegotiation = new CoeusToolBarButton(new ImageIcon(
            getClass().getClassLoader().getResource(CoeusGuiConstants.MEDUSA_NEGOTIATION_ICON)),
            null, "Negotiation");
          btnNotepad = new CoeusToolBarButton(new ImageIcon(
            getClass().getClassLoader().getResource(CoeusGuiConstants.NOTEPAD_ICON)),
            null, "Notepad");
          
          //Added for COEUSQA-1525 : Attachments for Institute Proposal - Start
          btnAttachments = new CoeusToolBarButton(new ImageIcon(
                  getClass().getClassLoader().getResource(CoeusGuiConstants.NARRATIVE_ICON)),
                  null, "Attachments");
          //COEUSQA-1525 : End
          
          btnMedusa = new CoeusToolBarButton(new ImageIcon(
            getClass().getClassLoader().getResource(CoeusGuiConstants.MEDUSA_ICON)),
            null, "Medusa");
          btnSortProposal = new CoeusToolBarButton(new ImageIcon(
            getClass().getClassLoader().getResource(CoeusGuiConstants.SORT_ICON)),
            null, "Sort proposals");
          btnSearchProposal = new CoeusToolBarButton(new ImageIcon(
            getClass().getClassLoader().getResource(CoeusGuiConstants.SEARCH_ICON)),
            null, "Search for Proposal");
          btnSaveas = new CoeusToolBarButton(new ImageIcon(
            getClass().getClassLoader().getResource(CoeusGuiConstants.SAVEAS_ICON)),
            null, "Save As");
          
          btnClose = new CoeusToolBarButton(new ImageIcon(
            getClass().getClassLoader().getResource(CoeusGuiConstants.CLOSE_ICON)),
            null, "Close");
          
          instPropToolBar.add(btnNewProposal);
          instPropToolBar.add(btnCorrectProposal);
          instPropToolBar.add(btnDisplayProposal);
          instPropToolBar.add(btnNewEntryProposal);
          instPropToolBar.add(btnIpReview);
          instPropToolBar.add(btnNegotiation);
          instPropToolBar.add(btnNotepad);
          instPropToolBar.addSeparator();
          //Added for COEUSQA-1525 : Attachments for Institute Proposal - Start
          instPropToolBar.add(btnAttachments);
          instPropToolBar.addSeparator();
          //COEUSQA-1525 : End
          instPropToolBar.add(btnMedusa);
          instPropToolBar.addSeparator();
          instPropToolBar.add(btnSortProposal);
          instPropToolBar.add(btnSearchProposal);
          instPropToolBar.add(btnSaveas);
          instPropToolBar.addSeparator();
          instPropToolBar.add(btnClose);
          
          return instPropToolBar;
     }
     
     public void prepareMenus(){
          if(mnuFile== null){
         // Holds the File Menu details
             Vector vecFileMenu = new Vector();
             // Holds the Edit Menu details

              mnuItmInbox = new CoeusMenuItem("Inbox", null, true, true);
              mnuItmInbox.setMnemonic('I');

              mnuItmClose = new CoeusMenuItem("Close", null, true, true);
              mnuItmClose.setMnemonic('C');

              mnuItmSaveas = new CoeusMenuItem("SaveAs...", null, true, true);
              mnuItmSaveas.setMnemonic('A');

              //Commented since we are not using it in Coeus 4.0
              //mnuItmPrintSetUp = new CoeusMenuItem("Print Setup...", null, true, true);
              //mnuItmPrintSetUp.setMnemonic('u');

              mnuItmSortProposal = new CoeusMenuItem("Sort...", null, true, true);
              mnuItmSortProposal.setMnemonic('o');

              mnuItmChangePassword = new CoeusMenuItem("Change Password", null, true, true);
              mnuItmChangePassword.setMnemonic('h');

              //Added for Case#3682 - Enhancements related to Delegations - Start
              mnuItmDelegations= new CoeusMenuItem("Delegations...", null, true, true);
              mnuItmDelegations.setMnemonic('l');
              //Added for Case#3682 - Enhancements related to Delegations -End
              
              mnuItmPreferences = new CoeusMenuItem("Preferences...", null, true, true);
              mnuItmPreferences.setMnemonic('P');

              mnuItmExit = new CoeusMenuItem("Exit", null, true, true);
              mnuItmExit.setMnemonic('x');
              
              //Case 2110 Start
              mnuItmCurrentLocks = new CoeusMenuItem("Current Locks",null,true,true);
              mnuItmCurrentLocks.setMnemonic('L');
              //Case 2110 End

              vecFileMenu.add(mnuItmInbox);
              vecFileMenu.add(SEPERATOR);
              vecFileMenu.add(mnuItmClose);
              vecFileMenu.add(SEPERATOR);
              vecFileMenu.add(mnuItmSaveas);
              
              //Commented since we are not using it in Coeus 4.0
              //vecFileMenu.add(mnuItmPrintSetUp);
              //vecFileMenu.add(SEPERATOR);
              
              vecFileMenu.add(mnuItmSortProposal);
              vecFileMenu.add(mnuItmChangePassword);
              //Case 2110 Start
              vecFileMenu.add(SEPERATOR);
              vecFileMenu.add(mnuItmCurrentLocks);
              vecFileMenu.add(SEPERATOR);
              //Case 2110 End
              //Added for Case#3682 - Enhancements related to Delegations - Start
              vecFileMenu.add(mnuItmDelegations);
              vecFileMenu.add(SEPERATOR);
              //Added for Case#3682 - Enhancements related to Delegations -End
              vecFileMenu.add(mnuItmPreferences);
              vecFileMenu.add(SEPERATOR);
              vecFileMenu.add(mnuItmExit);              
              mnuFile = new CoeusMenu("File", null, vecFileMenu, true, true);
              mnuFile.setMnemonic('F');
          }
          if(mnuEdit == null){
             Vector vecEditMenu = new Vector();
              mnuItmNewProposal = new CoeusMenuItem("New Proposal", null, true, true);
              mnuItmNewProposal.setMnemonic('N');

              mnuItmDisplayProposal = new CoeusMenuItem("Display Proposal", null, true, true);
              mnuItmDisplayProposal.setMnemonic('D');

              mnuItmCorrectProposal = new CoeusMenuItem("Correct Proposal", null, true, true);
              mnuItmCorrectProposal.setMnemonic('C');

              mnuItmNewEntry = new CoeusMenuItem("New Entry", null, true, true);
              mnuItmNewEntry.setMnemonic('e');

              mnuItmIPReview= new CoeusMenuItem("IP Review", null, true, true);
              mnuItmIPReview.setMnemonic('R');

              mnuItmNegotiation= new CoeusMenuItem("Negotiation", null, true, true);
              mnuItmNegotiation.setMnemonic('g');

              mnuItmNotepad= new CoeusMenuItem("Notepad", null, true, true);
              mnuItmNotepad.setMnemonic('t');
              
              //Added for COEUSQA-1525 : Attachments for Institute Proposal - Start
              mnuItmAttachments = new CoeusMenuItem("Attachments", null, true, true);
              mnuItmAttachments.setMnemonic('A');
              //COEUSQA-1525 : End
              
              mnuItmMedusa= new CoeusMenuItem("Medusa", null, true, true);
              mnuItmMedusa.setMnemonic('M');
              mnuItmMedusa.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M,KeyEvent.CTRL_MASK));


              vecEditMenu.add(mnuItmNewProposal);
              vecEditMenu.add(mnuItmDisplayProposal);
              vecEditMenu.add(mnuItmCorrectProposal);
              vecEditMenu.add(SEPERATOR);
              vecEditMenu.add(mnuItmNewEntry);
              vecEditMenu.add(SEPERATOR);
              vecEditMenu.add(mnuItmIPReview);
              vecEditMenu.add(mnuItmNegotiation);
              vecEditMenu.add(mnuItmNotepad);
              //Added for COEUSQA-1525 : Attachments for Institute Proposal - Start
              vecEditMenu.add(mnuItmAttachments);
              //COEUSQA-1525 : End
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
       // Bug Fix - To avoid setting the table column sizes. 
        // The sizes are set in CoeusSearch.XML - Start
        //setColumnSizes();
        // End
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
