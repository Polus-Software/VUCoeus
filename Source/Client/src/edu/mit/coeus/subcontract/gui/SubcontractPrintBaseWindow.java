/*
 * SubcontractPrintBaseWindow.java
 *
 * Created on December 22, 2004, 11:26 AM
 */

package edu.mit.coeus.subcontract.gui;

import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusInternalFrame;
import edu.mit.coeus.gui.menu.CoeusEditMenu;
import edu.mit.coeus.gui.menu.CoeusFileMenu;
import edu.mit.coeus.gui.menu.CoeusMenu;
import edu.mit.coeus.gui.menu.CoeusMenuItem;
import edu.mit.coeus.gui.toolbar.CoeusToolBarButton;
import edu.mit.coeus.utils.CoeusGuiConstants;
import java.awt.event.KeyEvent;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.event.InternalFrameEvent;

/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * @author  jobinelias
 */
public class SubcontractPrintBaseWindow extends CoeusInternalFrame {
	
	private CoeusAppletMDIForm mdiForm;
      
    public  CoeusToolBarButton btnPrint,btnClose;
    // Menu items for the File Menu
    public  CoeusMenuItem mnuItmInbox,mnuItmPrint,mnuItmPrintPreview,mnuItmClose,
			mnuItmChangePassword, mnuItmPreferences,mnuItmExit, mnuItmUnDo,
                        /*Added for Case#3682 - Enhancements related to Delegations - Start*/
                        mnuItmDelegations,
                        /*Added for Case#3682 - Enhancements related to Delegations - End*/
			mnuItmCut,mnuItmCopy,mnuItmPaste;
       
    public  CoeusMenu mnuFile,mnuEdit;
    
    /*
     * to indicate horizondal seperator in menu items
     */
    private final String SEPERATOR = "seperator";
	
	/** Creates a new instance of SubcontractPrintBaseWindow */
	public SubcontractPrintBaseWindow(String title,CoeusAppletMDIForm mdiForm) {
		super(title, mdiForm);
		this.mdiForm = mdiForm;
		initComponents();
	}
	
	private void initComponents() {
        setFrameToolBar(getSubcontractBaseWindowToolBar());
        prepareMenus();
		mnuItmUnDo.setEnabled(false);
    }
    
    private JToolBar getSubcontractBaseWindowToolBar(){
         JToolBar toolBar = new JToolBar();
        
        btnPrint = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.PRINT_ICON)),
        null, "Print...");
        
        btnClose = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.CLOSE_ICON)),
        null, "Close");
        
        toolBar.add(btnPrint);
        toolBar.addSeparator();
        toolBar.add(btnClose);
        return toolBar;
    }
    
    private void prepareMenus(){
        
        if(mnuFile == null) {
             // holds the data for the File Menu
            Vector vecFileMenu = new Vector();
            // Build the File Menu...
            mnuItmInbox = new CoeusMenuItem("Inbox", null, true, true);
            mnuItmInbox.setMnemonic('I');
            
            mnuItmPrint= new CoeusMenuItem("Print...", null, true, true);
            mnuItmPrint.setMnemonic('P');
            mnuItmPrint.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,KeyEvent.CTRL_MASK));

            mnuItmPrintPreview = new CoeusMenuItem("Print Preview", null, true, true);
            mnuItmPrintPreview.setMnemonic('V');
            			
			mnuItmClose= new CoeusMenuItem("Close", null, true, true);
            mnuItmClose.setMnemonic('C');

            mnuItmChangePassword= new CoeusMenuItem("Change Password", null, true, true);
            mnuItmChangePassword.setMnemonic('h');

            //Added for Case#3682 - Enhancements related to Delegations - Start             
            mnuItmDelegations= new CoeusMenuItem("Delegations...", null, true, true);
            mnuItmDelegations.setMnemonic('g');
            //Added for Case#3682 - Enhancements related to Delegations - End
            
            mnuItmPreferences= new CoeusMenuItem("Preferences...", null, true, true);
            mnuItmPreferences.setMnemonic('r');

            mnuItmExit= new CoeusMenuItem("Exit", null, true, true);
            mnuItmExit.setMnemonic('x');

            vecFileMenu.add(mnuItmInbox);
            vecFileMenu.add(SEPERATOR);
            vecFileMenu.add(mnuItmClose);
            vecFileMenu.add(SEPERATOR);
            vecFileMenu.add(mnuItmPrint);
			vecFileMenu.add(mnuItmPrintPreview);
            vecFileMenu.add(SEPERATOR);
			vecFileMenu.add(mnuItmChangePassword);
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
		if (mnuEdit == null) {
			 // holds the data for the File Menu
            Vector vecEditMenu = new Vector();
            // Build the Edit Menu...
            mnuItmUnDo = new CoeusMenuItem("Undo", null, true, true);
            mnuItmUnDo.setMnemonic('U');
			mnuItmUnDo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z,KeyEvent.CTRL_MASK));
			
			mnuItmCut = new CoeusMenuItem("Cut", null, true, true);
            mnuItmCut.setMnemonic('t');
			
			mnuItmCopy = new CoeusMenuItem("Copy", null, true, true);
            mnuItmCopy.setMnemonic('C');
			
			mnuItmPaste = new CoeusMenuItem("Paste", null, true, true);
            mnuItmPaste.setMnemonic('P');
			
			
			vecEditMenu.add(mnuItmUnDo);
            vecEditMenu.add(SEPERATOR);
            vecEditMenu.add(mnuItmCut);
            vecEditMenu.add(mnuItmCopy);
            vecEditMenu.add(mnuItmPaste);
			
            			
			mnuEdit = new CoeusMenu("Edit", null, vecEditMenu, true, true);
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
		mdiForm.getCoeusMenuBar().remove(mnuEdit);
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
	
	public void saveActiveSheet() {
	}
	
	public void saveAsActiveSheet() {
	}
	
}
