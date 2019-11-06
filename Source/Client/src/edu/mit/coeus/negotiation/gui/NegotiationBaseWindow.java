/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.negotiation.gui;

import java.util.Vector;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import javax.swing.JToolBar;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;
import javax.swing.event.InternalFrameEvent;

import edu.mit.coeus.gui.CoeusInternalFrame;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.menu.CoeusFileMenu;
import edu.mit.coeus.gui.menu.CoeusMaintainMenu;
import edu.mit.coeus.gui.menu.CoeusMenuItem;
import edu.mit.coeus.gui.menu.CoeusMenu;
import edu.mit.coeus.gui.toolbar.CoeusToolBarButton;
import edu.mit.coeus.utils.CoeusGuiConstants;

/**
 * NegotiationBaseWindow.java
 * Created on July 13, 2004, 3:18 PM
 * @author  Vyjayanthi
 */
public class NegotiationBaseWindow extends CoeusInternalFrame {
    
    private CoeusAppletMDIForm mdiForm;
    
    public CoeusToolBarButton btnNext, btnPrevious, btnSave, btnMedusa, btnClose;
    
    private final String SEPERATOR = "seperator";
    
    //File Menu Items
    public CoeusMenuItem mnuItmClose, mnuItmSave/*, mnuItmPrintSetup*/ , mnuItmNext,
        mnuItmPrevious,
        /*Added for Case#3682 - Enhancements related to Delegations - Start*/
        mnuItmDelegations,
        /*Added for Case#3682 - Enhancements related to Delegations - End*/
        mnuItmPreferences,mnuItmExit,/*Case 2110 Start1*/mnuItmCurrentLocks/*Case 2110 End1*/;
    
    //Edit Menu Items
    public CoeusMenuItem mnuItmCustomData;
    
    //View Menu Items
    public CoeusMenuItem mnuItmMedusa;
    
    //Menus
    public CoeusMenu mnuFile, mnuEdit, mnuView;
    
    private char functionType;
    
    /** Creates a new instance of NegotiationBaseWindow */
    public NegotiationBaseWindow(String title, CoeusAppletMDIForm mdiForm) {
        super(title, mdiForm);
        this.mdiForm = mdiForm;
        initComponents();
    }
    
    /** Set the toolbar and menu items
     */
    private void initComponents() {
        setFrameToolBar(getNegotiationBaseWindowToolBar());
        prepareMenus();
    }
    
    /** Set the toolbar buttons
     * @return toolBar the prepared toolbar
     */
    private JToolBar getNegotiationBaseWindowToolBar() {
        JToolBar toolBar = new JToolBar();
        
        btnNext = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.ERIGHT_ARROW_ICON)),
        null, "Next");
        
        btnNext.setDisabledIcon(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.DRIGHT_ARROW_ICON)));
        
        btnPrevious = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.ELEFT_ARROW_ICON)),
        null, "Previous");
        
        btnPrevious.setDisabledIcon(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.DLEFT_ARROW_ICON)));
        
        btnSave = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.SAVE_ICON)),
        null, "Save");
        
        btnSave.setDisabledIcon(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.DSAVE_ICON)));
        
        btnMedusa = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.MEDUSA_ICON)),
        null, "Medusa");
        
        btnClose = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.CLOSE_ICON)),
        null, "Close");
        
        toolBar.add(btnNext);
        toolBar.add(btnPrevious);
        toolBar.add(btnSave);
        toolBar.addSeparator();
        toolBar.add(btnMedusa);
        toolBar.addSeparator();
        toolBar.add(btnClose);       
        
        return toolBar;
    }
    
    /** Set the menus
     */
    private void prepareMenus() {
        //Build File Menu
        Vector vecFile = new Vector();
        
        mnuItmClose = new CoeusMenuItem("Close", null, true, true);
        mnuItmClose.setMnemonic('C');

        mnuItmSave = new CoeusMenuItem("Save", null, true, true);
        mnuItmSave.setMnemonic('S');
        mnuItmSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        
        //Commented since we are not using it in Coeus 4.0
        /*mnuItmPrintSetup = new CoeusMenuItem("Print Setup...", null, true, true);
        mnuItmPrintSetup.setMnemonic('u');
         */

        mnuItmNext = new CoeusMenuItem("Next", null, true, true);
        mnuItmNext.setMnemonic('N');
        
        mnuItmPrevious = new CoeusMenuItem("Previous", null, true, true);
        mnuItmPrevious.setMnemonic('P');
        
         //Added for Case#3682 - Enhancements related to Delegations - Start             
         mnuItmDelegations= new CoeusMenuItem("Delegations...", null, true, true);
         mnuItmDelegations.setMnemonic('D');
         //Added for Case#3682 - Enhancements related to Delegations - End
        
        mnuItmPreferences = new CoeusMenuItem("Preferences", null, true, true);
        mnuItmPreferences.setMnemonic('P');
        
        //Start of bug fix 1651
        mnuItmExit = new CoeusMenuItem("Exit", null, true, true);
        mnuItmExit.setMnemonic('x');
        // End of bug fix 1651
        
        //Case 2110 Start2
        mnuItmCurrentLocks = new CoeusMenuItem("Current Locks",null,true,true);
        mnuItmCurrentLocks.setMnemonic('L');
        //Case 2110 End2
        
        vecFile.add(mnuItmClose);
        vecFile.add(SEPERATOR);
        vecFile.add(mnuItmSave);
        vecFile.add(SEPERATOR);
        
        //Commented since we are not using it in Coeus 4.0
        //vecFile.add(mnuItmPrintSetup);
        //vecFile.add(SEPERATOR);
        
        vecFile.add(mnuItmNext);
        vecFile.add(SEPERATOR);
        vecFile.add(mnuItmCurrentLocks);
        vecFile.add(SEPERATOR);
        vecFile.add(mnuItmPrevious);
        //Added for Case#3682 - Enhancements related to Delegations - Start
        vecFile.add(SEPERATOR);
        vecFile.add(mnuItmDelegations);
        vecFile.add(SEPERATOR);
        //Added for Case#3682 - Enhancements related to Delegations - End
        vecFile.add(mnuItmPreferences);
        vecFile.add(mnuItmExit);
        
        //Build Edit Menu
        Vector vecEdit = new Vector();
        
        mnuItmCustomData = new CoeusMenuItem("Custom Data", null, true, true);
        mnuItmCustomData.setMnemonic('o');
        mnuItmCustomData.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, ActionEvent.CTRL_MASK));
               
        vecEdit.add(mnuItmCustomData);
        
        //Build View Menu
        Vector vecView = new Vector();
        
        mnuItmMedusa = new CoeusMenuItem("Medusa...", null, true, true);
        mnuItmMedusa.setMnemonic('M');
        mnuItmMedusa.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, ActionEvent.CTRL_MASK));
        
        vecView.add(mnuItmMedusa);
        
        //Set the menu items to the respective menus
        mnuFile = new CoeusMenu("File", null, vecFile, true, true);
        mnuFile.setMnemonic('F');
        
        mnuEdit = new CoeusMenu("Edit", null, vecEdit, true, true);
        mnuEdit.setMnemonic('E');
        
        mnuView = new CoeusMenu("View", null, vecView, true, true);
        mnuView.setMnemonic('V');
    }
    
    private void loadMenus() {
        mdiForm.getCoeusMenuBar().remove(0);
        mdiForm.getCoeusMenuBar().add(mnuFile, 0);
        /*modified for the Bug Fix:1734 start
        //mdiForm.getCoeusMenuBar().remove(1);
        /*Bug Fix:1734 End*/ 
        mdiForm.getCoeusMenuBar().add(mnuEdit, 1);
        mdiForm.getCoeusMenuBar().add(mnuView, 2);
    }
    
    private void unloadMenus() {
        mdiForm.getCoeusMenuBar().remove(mnuFile);
        mdiForm.getCoeusMenuBar().add(new CoeusFileMenu(mdiForm).getMenu(), 0);
        mdiForm.getCoeusMenuBar().remove(mnuEdit);
        mdiForm.getCoeusMenuBar().add(new CoeusMaintainMenu(mdiForm).getMenu(), 1);
        mdiForm.getCoeusMenuBar().remove(mnuView);
    }
    
    public void saveActiveSheet() {
    }
    
    public void saveAsActiveSheet() {
    }
    
    public void internalFrameActivated(InternalFrameEvent e) {
        super.internalFrameActivated(e);
        loadMenus();
        mdiForm.getCoeusMenuBar().validate();
    }
    
    
    public void internalFrameDeactivated(InternalFrameEvent e) {
        unloadMenus();
        super.internalFrameDeactivated(e);
        mdiForm.getCoeusMenuBar().revalidate();
    }
    
    /**
     * Getter for property functionType.
     * @return Value of property functionType.
     */
    public char getFunctionType() {
        return functionType;
    }
    
    /**
     * Setter for property functionType.
     * @param functionType New value of property functionType.
     */
    public void setFunctionType(char functionType) {
        this.functionType = functionType;
    }
    
}
