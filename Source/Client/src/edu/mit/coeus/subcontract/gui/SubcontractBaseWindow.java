/*
 * SubcontractBaseWindow.java
 *
 * Created on September 3, 2004, 12:23 PM
 */

package edu.mit.coeus.subcontract.gui;

import java.awt.event.*;
import java.util.Vector;
import javax.swing.*;
import javax.swing.event.*;

import edu.mit.coeus.gui.*;
import edu.mit.coeus.gui.menu.*;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.gui.toolbar.*;

/**Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * @author  nadhgj
 */
public class SubcontractBaseWindow extends CoeusInternalFrame {
    
    private CoeusAppletMDIForm mdiForm;
      
      public  CoeusToolBarButton btnNext, btnPrevious,btnPrint,btnSave, btnClose;
      // Menu items for the File Menu
      public  CoeusMenuItem mnuItmInbox, mnuItmNext, mnuItmPrevious,
               mnuItmPrint,mnuItmGenerateAgreeModifaction,  mnuItmSelectSubcontractor,mnuItmSelectRequisitioner,
               mnuItmClose, mnuItmSave, mnuItmChangePassword, mnuItmPreferences,
              /*Added for Case#3682 - Enhancements related to Delegations - Start*/
              mnuItmDelegations,
              /*Added for Case#3682 - Enhancements related to Delegations - End*/
               mnuItmExit,/*Case 2110 Start*/mnuItmCurrentLocks/*Case 2110 End*/;
       
      public  CoeusMenu mnuFile;
      private char functionType;
      /*
     * to indicate horizondal seperator in menu items
     */
    private final String SEPERATOR="seperator";
    
    /** Creates a new instance of SubcontractBaseWindow */
    public SubcontractBaseWindow(String title, CoeusAppletMDIForm mdiForm) {
        super(title, mdiForm);
        this.mdiForm = mdiForm;
        initComponents();
    }
    
    public void saveActiveSheet() {
    }
    
    public void saveAsActiveSheet() {
    }
    
    private void initComponents() {
        setFrameToolBar(getSubcontractBaseWindowToolBar());
        prepareMenus();
    }
    
    private JToolBar getSubcontractBaseWindowToolBar(){
         JToolBar toolBar = new JToolBar();
        
        btnNext = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.ERIGHT_ARROW_ICON)),
        null, "Next Subcontract");
        
        btnPrevious = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.ELEFT_ARROW_ICON)),
        null, "Previous Subcontract");
        
        btnPrint = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.PRINT_ICON)),
        null, "Print...");
        
        btnSave = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.SAVE_ICON)),
        null, "Save");
        
        btnClose = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.CLOSE_ICON)),
        null, "Close");
        
        toolBar.add(btnNext);
        toolBar.add(btnPrevious);
        toolBar.addSeparator();
        toolBar.add(btnPrint);
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

            

            mnuItmNext = new CoeusMenuItem("Next", null, true, true);
            mnuItmNext.setMnemonic('e');
            mnuItmPrevious = new CoeusMenuItem("Previous", null, true, true);
            mnuItmPrevious.setMnemonic('v');
            mnuItmSelectSubcontractor= new CoeusMenuItem("Select Subcontractor", null, true, true);
            mnuItmSelectSubcontractor.setMnemonic('l');

            mnuItmSelectRequisitioner= new CoeusMenuItem("Select Requisitioner", null, true, true);
            mnuItmSelectRequisitioner.setMnemonic('R');
            
            mnuItmPrint= new CoeusMenuItem("Print...", null, true, true);
            mnuItmPrint.setMnemonic('P');
            mnuItmPrint.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,KeyEvent.CTRL_MASK));
            
            // Added for COEUSQA-1412 Subcontract Module changes - Start
            mnuItmGenerateAgreeModifaction= new CoeusMenuItem("Generate Agreement / Modification", null, true, true);
            mnuItmGenerateAgreeModifaction.setMnemonic('P');
            //mnuItmGenerateAgreeModifaction.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,KeyEvent.CTRL_MASK));
            // Added for COEUSQA-1412 Subcontract Module changes - End
                                
            mnuItmSave= new CoeusMenuItem("Save", null, true, true);
            mnuItmSave.setMnemonic('S');
            mnuItmSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,KeyEvent.CTRL_MASK));

            mnuItmClose= new CoeusMenuItem("Close", null, true, true);
            mnuItmClose.setMnemonic('C');

            mnuItmChangePassword= new CoeusMenuItem("Change Password", null, true, true);
            mnuItmChangePassword.setMnemonic('h');

            //Added for Case#3682 - Enhancements related to Delegations - Start
             mnuItmDelegations= new CoeusMenuItem("Delegations...", null, true, true);
             mnuItmDelegations.setMnemonic('g');
             //Added for Case#3682 - Enhancements related to Delegations - End
            mnuItmPreferences= new CoeusMenuItem("Preferences...", null, true, true);
            mnuItmPreferences.setMnemonic('n');

            mnuItmExit= new CoeusMenuItem("Exit", null, true, true);
            mnuItmExit.setMnemonic('x');
            
            //Case 2110 Start            
            mnuItmCurrentLocks = new CoeusMenuItem("Current Locks",null,true,true);
            mnuItmCurrentLocks.setMnemonic('o');
            //Case 2110 End

            vecFileMenu.add(mnuItmInbox);
            vecFileMenu.add(SEPERATOR);
            vecFileMenu.add(mnuItmClose);
            vecFileMenu.add(SEPERATOR);
            vecFileMenu.add(mnuItmSave);
            vecFileMenu.add(SEPERATOR);
            vecFileMenu.add(mnuItmPrint);
            // Added for COEUSQA-1412 Subcontract Module changes - Start
            vecFileMenu.add(mnuItmGenerateAgreeModifaction);
            // Added for COEUSQA-1412 Subcontract Module changes - End
            vecFileMenu.add(SEPERATOR);           
            vecFileMenu.add(mnuItmNext);
            vecFileMenu.add(mnuItmPrevious);
            vecFileMenu.add(mnuItmSelectSubcontractor);
            vecFileMenu.add(mnuItmSelectRequisitioner);
            vecFileMenu.add(mnuItmChangePassword);
             //Case 2110 Start
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
    }
    
    private void loadMenus() {
        mdiForm.getCoeusMenuBar().remove(0);
        mdiForm.getCoeusMenuBar().add(mnuFile, 0);
    }
    
    private void unloadMenus() {
        mdiForm.getCoeusMenuBar().remove(mnuFile);
        mdiForm.getCoeusMenuBar().add(new CoeusFileMenu(mdiForm).getMenu(), 0);
    }
    
    public void internalFrameActivated(InternalFrameEvent e) {
        super.internalFrameActivated(e);
        loadMenus();
    }
    
    public void internalFrameDeactivated(InternalFrameEvent e) {
        unloadMenus();
        super.internalFrameDeactivated(e);
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

