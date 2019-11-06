/*
 * InstituteProposalBaseWindow.java
 *
 * Created on April 26, 2004, 11:14 AM
 */

package edu.mit.coeus.instprop.gui;

import java.awt.event.*;
import java.util.Vector;
import javax.swing.*;
import javax.swing.event.*;

import edu.mit.coeus.gui.*;
import edu.mit.coeus.gui.menu.*;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.gui.toolbar.*;

/** /**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
* @author chandru
*/
public class InstituteProposalBaseWindow extends CoeusInternalFrame{
    
      private CoeusAppletMDIForm mdiForm;
      
      public  CoeusToolBarButton btnNext, btnPrevious,btnPrint, btnNotepad, btnMedusa,
                btnSave, btnClose, btnSendEmail;//Added for Case#2214 email enhancement
      // Menu items for the File Menu
      public  CoeusMenuItem mnuItmInbox/*, mnuItmPrintSetup*/,mnuItmNext, mnuItmPrevious,
               mnuItmCurrentPendingreport, mnuItmUnlockProposal,mnuItmPrintProposalNotice,
               mnuItmClose, mnuItmSave, mnuItmChangePassword, mnuItmPreferences,
              /*Added for Case#3682 - Enhancements related to Delegations -Start*/
              mnuItmDelegations,
              /*Added for Case#3682 - Enhancements related to Delegations -End*/
               mnuItmExit,/*Case 2110 Start*/mnuItmCurrentLocks/*Case 2110 End*/;
      // Menu items for the Edit menu.
      public  CoeusMenuItem mnuItmNegotiation, mnuItmNotepad, mnuItmMedusa;
      //Added for COEUSQA-1525 : Attachments for Institute Proposal - Start
      public CoeusMenuItem mnuItmAttachments;
      public CoeusToolBarButton btnAttachments;
      //COEUSQA-1525 : End
      public  CoeusMenu mnuFile, mnuEdit;
      private char functionType;
      /*
     * to indicate horizondal seperator in menu items
     */
    private final String SEPERATOR="seperator";
      
    
    /** Creates a new instance of InstituteProposalBaseWindow */
    public InstituteProposalBaseWindow(String title, CoeusAppletMDIForm mdiForm) {
        super(title, mdiForm);
        this.mdiForm = mdiForm;
        initComponents();
    }
    
    private void initComponents() {
        setFrameToolBar(getProposalBaseWindowToolBar());
        prepareMenus();
    }
    
    private JToolBar getProposalBaseWindowToolBar(){
         JToolBar toolBar = new JToolBar();
        
        btnNext = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.ERIGHT_ARROW_ICON)),
        null, "Next Proposal");
        
        btnPrevious = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.ELEFT_ARROW_ICON)),
        null, "Previous Proposal");
        
        btnPrint = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.PRINT_ICON)),
        null, "Print Proposal Notice");
        
        btnMedusa = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.MEDUSA_ICON)),
        null, "Medusa");
        
        btnNotepad = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.NOTEPAD_ICON)),
        null, "Notepad");
        
        //Added for COEUSQA-1525 : Attachments for Institute Proposal - Start
        btnAttachments = new CoeusToolBarButton(new ImageIcon(
                getClass().getClassLoader().getResource(CoeusGuiConstants.NARRATIVE_ICON)),
                null, "Attachments");
        //COEUSQA-1525 : End
        
        btnSave = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.SAVE_ICON)),
        null, "Save");
        
        btnClose = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.CLOSE_ICON)),
        null, "Close");
        
        //Added for Case#2214 email enhancement
        btnSendEmail = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.EMAIL_ICON)), null, "Send Mail Notification");
        
        toolBar.add(btnNext);
        toolBar.add(btnPrevious);
        toolBar.addSeparator();
        toolBar.add(btnPrint);
        toolBar.add(btnNotepad);
        toolBar.addSeparator();
        //Added for COEUSQA-1525 : Attachments for Institute Proposal - Start
        toolBar.add(btnAttachments);
        toolBar.addSeparator();
        //COEUSQA-1525 : End
        toolBar.add(btnMedusa);
        toolBar.add(btnSendEmail);//Added for Case#2214 email enhancement
        toolBar.addSeparator();
        toolBar.add(btnSave);
        toolBar.addSeparator();
        toolBar.add(btnClose);
        return toolBar;
    }
    
    private void prepareMenus(){
        
        if(mnuFile == null){
             // holds the data for the File Menu
            Vector vecFileMenu = new Vector();
            // Build the File Menu...
            mnuItmInbox = new CoeusMenuItem("Inbox", null, true, true);
            mnuItmInbox.setMnemonic('I');

            ////Commented since we are not using it in Coeus 4.0
            //mnuItmPrintSetup = new CoeusMenuItem("Print Setup..", null, true, true);
            //mnuItmPrintSetup.setMnemonic('u');

            mnuItmNext = new CoeusMenuItem("Next", null, true, true);
            mnuItmNext.setMnemonic('t');

            mnuItmPrevious = new CoeusMenuItem("Previous", null, true, true);
            mnuItmPrevious.setMnemonic('P');

            mnuItmCurrentPendingreport= new CoeusMenuItem("Current And Pending Report...", null, true, true);
            mnuItmCurrentPendingreport.setMnemonic('R');

            mnuItmUnlockProposal= new CoeusMenuItem("Unlock Proposal", null, true, true);
            mnuItmUnlockProposal.setMnemonic('U');
            mnuItmUnlockProposal.setEnabled(false);

            mnuItmPrintProposalNotice= new CoeusMenuItem("Print Proposal Notice", null, true, true);
            mnuItmPrintProposalNotice.setMnemonic('N');

            mnuItmSave= new CoeusMenuItem("Save", null, true, true);
            mnuItmSave.setMnemonic('S');

            mnuItmClose= new CoeusMenuItem("Close", null, true, true);
            mnuItmClose.setMnemonic('C');

            mnuItmChangePassword= new CoeusMenuItem("Change Password", null, true, true);
            mnuItmChangePassword.setMnemonic('h');

            //Added for Case#3682 - Enhancements related to Delegations - Start
            mnuItmDelegations= new CoeusMenuItem("Delegations...", null, true, true);
            mnuItmDelegations.setMnemonic('D');
            //Added for Case#3682 - Enhancements related to Delegations - End
            
            mnuItmPreferences= new CoeusMenuItem("Preferences...", null, true, true);
            mnuItmPreferences.setMnemonic('f');

            mnuItmExit= new CoeusMenuItem("Exit", null, true, true);
            mnuItmExit.setMnemonic('x');
            
            //Case 2110 Start
            mnuItmCurrentLocks = new CoeusMenuItem("Current Locks",null,true,true);
            mnuItmCurrentLocks.setMnemonic('L');
            //Case 2110 End

            vecFileMenu.add(mnuItmInbox);
            vecFileMenu.add(SEPERATOR);
            
            //Commented since we are not using it in Coeus 4.0
            //vecFileMenu.add(mnuItmPrintSetup);
            //vecFileMenu.add(SEPERATOR);
            
            vecFileMenu.add(mnuItmNext);
            vecFileMenu.add(mnuItmPrevious);
            vecFileMenu.add(SEPERATOR);
            vecFileMenu.add(mnuItmCurrentPendingreport);          
            vecFileMenu.add(SEPERATOR);
            vecFileMenu.add(mnuItmUnlockProposal);
            vecFileMenu.add(mnuItmPrintProposalNotice);
            vecFileMenu.add(SEPERATOR);
            vecFileMenu.add(mnuItmClose);
            vecFileMenu.add(mnuItmSave);
            vecFileMenu.add(SEPERATOR);
            vecFileMenu.add(mnuItmChangePassword);
            //Case 2110 Start
            vecFileMenu.add(SEPERATOR);
            vecFileMenu.add(mnuItmCurrentLocks);
            vecFileMenu.add(SEPERATOR);
            //Case 2110 End
            //Added for Case#3682 - Enhancements related to Delegations - Start
            vecFileMenu.add(mnuItmDelegations);
            vecFileMenu.add(SEPERATOR);
            ////Added for Case#3682 - Enhancements related to Delegations -End
            vecFileMenu.add(mnuItmPreferences);
            vecFileMenu.add(SEPERATOR);
            vecFileMenu.add(mnuItmExit);
            mnuFile = new CoeusMenu("File", null, vecFileMenu, true, true);
            mnuFile.setMnemonic('F');
        }
        if(mnuEdit == null){
             // Holds the data for the Edit Menu.
             Vector vecEditMenu = new Vector();
             // Build the menu item for the Edit Menu
        
            mnuItmNegotiation= new CoeusMenuItem("Negotiation", null, true, true);
            mnuItmNegotiation.setMnemonic('g');

            mnuItmNotepad= new CoeusMenuItem("Notepad...", null, true, true);
            mnuItmNotepad.setMnemonic('t');
            
            //Added for COEUSQA-1525 : Attachments for Institute Proposal - Start
            mnuItmAttachments = new CoeusMenuItem("Attachments", null, true, true);
            mnuItmAttachments.setMnemonic('A');
            //COEUSQA-1525 : End
            
            mnuItmMedusa= new CoeusMenuItem("Medusa", null, true, true);
            mnuItmMedusa.setMnemonic('M');

            mnuItmMedusa.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M,KeyEvent.CTRL_MASK));

            vecEditMenu.add(mnuItmNegotiation);
            vecEditMenu.add(mnuItmNotepad);
            //Added for COEUSQA-1525 : Attachments for Institute Proposal - Start
            vecEditMenu.add(mnuItmAttachments);
            //COEUSQA-1525 : End
            vecEditMenu.add(SEPERATOR);
            vecEditMenu.add(mnuItmMedusa);
            mnuEdit = new CoeusMenu("Details", null, vecEditMenu, true, true);
            mnuEdit.setMnemonic('E');
        }
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
    
    public void saveActiveSheet() {
    }
    
    public void saveAsActiveSheet() {
    }
    
    /** Getter for property functionType.
     * @return Value of property functionType.
     *
     */
    public char getFunctionType() {
        return functionType;
    }
    
    /** Setter for property functionType.
     * @param functionType New value of property functionType.
     *
     */
    public void setFunctionType(char functionType) {
        this.functionType = functionType;
    }
    
}
