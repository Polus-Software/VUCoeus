/*
 * MaintainSponsorHierarchyBaseWindow.java
 *
 * Created on November 19, 2004, 12:51 PM
 */

package edu.mit.coeus.sponsorhierarchy.gui;


import edu.mit.coeus.gui.CoeusInternalFrame;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.toolbar.CoeusToolBarButton;
import edu.mit.coeus.gui.menu.CoeusMenu;
import edu.mit.coeus.gui.menu.CoeusMenuItem;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.gui.menu.*;

import javax.swing.JToolBar;
import javax.swing.event.*;
import javax.swing.ImageIcon;
import java.util.Vector;
import java.awt.event.*;

/*
 *Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * @author  nadhgj
 */
public class MaintainSponsorHierarchyBaseWindow extends CoeusInternalFrame{
    
    private CoeusAppletMDIForm mdiForm;
    
    public  CoeusToolBarButton btnCreateNewGroup, btnChangeGroupName,btnDelete,btnMoveUp, btnMoveDown,
            btnDetails,btnPanel,btnClose,btnSave,btnSearchSponser;
    
//    public javax.swing.JToggleButton btnPanel;
    
      // Menu items for the Edit Menu
    public  CoeusMenuItem mnuItmCreateNewGroup, mnuItmChangeGroupName, mnuItmDelete,
               mnuItmMoveUp, mnuItmMoveDown
            //Added for Case#2445 - proposal development print forms linked to indiv sponsor, should link to sponsor hierarchy - Start
            ,mnuItmSponsorForm;//Case#2445 - End
    
    // Declaring the menu items for the File Menu
    public CoeusMenuItem mnuItmClose,mnuItmSave,mnuItmInbox,mnuItmChangePassword, mnuItmPreferences,mnuItmExit,
     /*Added for Case#3682 - Enhancements related to Delegations - Start*/
     mnuItmDelegations,
    /*Added for Case#3682 - Enhancements related to Delegations - End*/
    /*Case 2110 Start*/mnuItmCurrentLocks;/* Case 2110 End*/
    
    
    // Declaring the menu items for the Tools Menu
    public CoeusMenuItem mnuItmSearchSponser;
    
    // Declaring the menu items for the View Menu
    public CoeusMenuItem mnuItmDetails;
    public javax.swing.JCheckBoxMenuItem mnuItmPanel;
       
      public  CoeusMenu mnuEdit,mnuView,mnuFile,mnuTools;
      private char functionType;
      /*
     * to indicate horizondal seperator in menu items
     */
    private final String SEPERATOR="seperator";
    /** Creates a new instance of MaintainSponsorHierarchyBaseWindow */
    public MaintainSponsorHierarchyBaseWindow(String title, CoeusAppletMDIForm mdiForm) {
        super(title, mdiForm);
        this.mdiForm = mdiForm;
        initComponents();
    }
    
    private void initComponents() {
        setFrameToolBar(getSponsorHierarchyBaseWindowToolBar());
        prepareMenus();
    }
    
    private JToolBar getSponsorHierarchyBaseWindowToolBar(){
         JToolBar toolBar = new JToolBar();
        
        btnCreateNewGroup = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.NEW_ICON)),
        null, "Insert new row");
        
        btnChangeGroupName = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.EDIT_ICON)),
        null, "Modify current row");
        
        btnDelete = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.DELETE_ICON)),
        null, "Delete");
        
        btnMoveUp = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource("images/moveup.gif")),
        null, "Move Group Up");
        
        btnMoveDown = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource("images/movedown.gif")),
        null, "Move Group Down");
        
        btnSearchSponser = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.SEARCH_ICON)),
        null, "Search");
        
        btnDetails = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.DETAILS_ICON)),
        null, "Details");
        
        btnSave = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.SAVE_ICON)),
        null, "Save");
        
        btnPanel = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource("images/sponsorlist.gif")),
        null,"Display Sponsor Panel");
        btnPanel.setSelected(true);
        btnPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.LOWERED));
        
        btnClose = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.CLOSE_ICON)),
        null, "Close");
        
        toolBar.add(btnCreateNewGroup);
        toolBar.add(btnChangeGroupName);
        toolBar.add(btnDelete);
        toolBar.add(btnMoveUp);
        toolBar.add(btnMoveDown);
        toolBar.add(btnSearchSponser);
        toolBar.addSeparator();
        toolBar.add(btnDetails);
        toolBar.add(btnPanel);
        toolBar.add(btnSave);
        toolBar.addSeparator();
        toolBar.add(btnClose);
        
        return toolBar;
    }
    
    public void prepareMenus(){
        if(mnuFile== null){
         // Holds the File Menu details
             Vector vecFileMenu = new Vector();
             
              mnuItmInbox = new CoeusMenuItem("Inbox", null, true, true);
              mnuItmInbox.setMnemonic('I');
              
              mnuItmClose = new CoeusMenuItem("Close", null, true, true);
              mnuItmClose.setMnemonic('C');

              mnuItmSave = new CoeusMenuItem("Save", null, true, true);
              mnuItmSave.setMnemonic('S');
              mnuItmSave.setAccelerator(javax.swing.KeyStroke.getKeyStroke(KeyEvent.VK_S,KeyEvent.CTRL_MASK));

              mnuItmChangePassword = new CoeusMenuItem("Change Password", null, true, true);
              mnuItmChangePassword.setMnemonic('h');
              
             //Added for Case#3682 - Enhancements related to Delegations - Start             
             mnuItmDelegations= new CoeusMenuItem("Delegations...", null, true, true);
             mnuItmDelegations.setMnemonic('g');
             //Added for Case#3682 - Enhancements related to Delegations - End               
              
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
              //Added for Case#3682 - Enhancements related to Delegations - End              
              vecFileMenu.add(mnuItmPreferences);
              vecFileMenu.add(SEPERATOR);
              vecFileMenu.add(mnuItmExit);
              mnuFile = new CoeusMenu("File", null, vecFileMenu, true, true);
              mnuFile.setMnemonic('F');
          }
          if(mnuEdit== null){
         // Holds the Edit Menu details
             Vector vecEditMenu = new Vector();
             
             
              mnuItmCreateNewGroup = new CoeusMenuItem("Create New Group", null, true, true);
              mnuItmCreateNewGroup.setMnemonic('N');
              
              mnuItmChangeGroupName = new CoeusMenuItem("Change Group Name", null, true, true);
              mnuItmChangeGroupName.setMnemonic('C');

              mnuItmDelete = new CoeusMenuItem("Delete", null, true, true);
              mnuItmDelete.setMnemonic('D');

              mnuItmMoveUp = new CoeusMenuItem("Move Up", null, true, true);
              mnuItmMoveUp.setMnemonic('U');
              mnuItmMoveUp.setAccelerator(javax.swing.KeyStroke.getKeyStroke(KeyEvent.VK_U,KeyEvent.CTRL_MASK));

              mnuItmMoveDown = new CoeusMenuItem("Move Down", null, true, true);
              mnuItmMoveDown.setMnemonic('D');
              mnuItmMoveDown.setAccelerator(javax.swing.KeyStroke.getKeyStroke(KeyEvent.VK_D,KeyEvent.CTRL_MASK));
              
              //Addded for Case#2445 - proposal development print forms linked to indiv sponsor, should link to sponsor hierarchy - Start
              mnuItmSponsorForm = new CoeusMenuItem("Sponsor Form", null, true, true);
              //Case#2445 - End
              
              vecEditMenu.add(mnuItmCreateNewGroup);
              vecEditMenu.add(mnuItmChangeGroupName);
              vecEditMenu.add(mnuItmDelete);
              vecEditMenu.add(SEPERATOR);
              vecEditMenu.add(mnuItmMoveUp);
              vecEditMenu.add(mnuItmMoveDown);
              //Addded for Case#2445 - proposal development print forms linked to indiv sponsor, should link to sponsor hierarchy - Start
              vecEditMenu.add(SEPERATOR);
              vecEditMenu.add(mnuItmSponsorForm);
              //Case#2445 - End
              mnuEdit = new CoeusMenu("Edit", null, vecEditMenu, true, true);
              mnuEdit.setMnemonic('E');
          }
          if(mnuView == null){
              // Holds the View Menu details
              Vector vecViewMenu = new Vector();
              mnuItmDetails = new CoeusMenuItem("Details", null, true, true);
              mnuItmDetails.setMnemonic('D');

              mnuItmPanel = new javax.swing.JCheckBoxMenuItem("Panel", null, true);
              mnuItmPanel.setMnemonic('P');

              vecViewMenu.add(mnuItmDetails);
              vecViewMenu.add(SEPERATOR);
              vecViewMenu.add(mnuItmPanel);
              mnuView = new CoeusMenu("View", null, vecViewMenu, true, true);
              mnuView.setMnemonic('V');
          }
        if(mnuTools == null) {
            // Holds the Tools Menu details
            Vector vecToolsMenu = new Vector();
            mnuItmSearchSponser = new CoeusMenuItem("Search",null,true,true);
            mnuItmSearchSponser.setMnemonic('S');
            vecToolsMenu.add(mnuItmSearchSponser);
            mnuTools = new CoeusMenu("Tools",null,vecToolsMenu,true,true);
            mnuTools.setMnemonic('T');
        }
    }
    private void loadMenus() {
        mdiForm.getCoeusMenuBar().remove(0);
        mdiForm.getCoeusMenuBar().add(mnuFile, 0);
        mdiForm.getCoeusMenuBar().add(mnuEdit,1);
        mdiForm.getCoeusMenuBar().add(mnuView,2);
        mdiForm.getCoeusMenuBar().add(mnuTools,7);
        mdiForm.getCoeusMenuBar().validate();
    }
    
    private void unloadMenus() {
        mdiForm.getCoeusMenuBar().remove(mnuFile);
        mdiForm.getCoeusMenuBar().remove(mnuEdit);
        mdiForm.getCoeusMenuBar().remove(mnuView);
        mdiForm.getCoeusMenuBar().remove(mnuTools);
        mdiForm.getCoeusMenuBar().add(new CoeusFileMenu(mdiForm).getMenu(), 0);
        mdiForm.getCoeusMenuBar().validate();
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
    
    public void saveActiveSheet() {
    }
    
    public void saveAsActiveSheet() {
    }
    
}
