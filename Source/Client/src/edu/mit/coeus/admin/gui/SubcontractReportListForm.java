/*
 * SubcontractReportListForm.java
 *
 * Created on January 3, 2005, 4:21 PM
 */

package edu.mit.coeus.admin.gui;


import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusInternalFrame;
import edu.mit.coeus.gui.menu.CoeusFileMenu;
import edu.mit.coeus.gui.menu.CoeusMenu;
import edu.mit.coeus.gui.menu.CoeusMenuItem;
import edu.mit.coeus.gui.toolbar.CoeusToolBarButton;
import edu.mit.coeus.utils.CoeusGuiConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.event.InternalFrameEvent;
/**
 *
 * @author  chandrashekara
 **
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

public class SubcontractReportListForm  extends CoeusInternalFrame{
    private CoeusAppletMDIForm mdiForm;
    
    public  CoeusToolBarButton btnValidationChecks,btnGoals,btnSelectAll,
        btnDeselectAll,btnClose;
    
    public CoeusToolBarButton btnSort;//Added by nadh - 19/01/2005 for Sort ToolBar Button
    
    // Menu items for the File Menu
    public  CoeusMenuItem mnuItmInbox,mnuItmClose,
    mnuItmChangePassword, 
            /*Added for Case#3682 - Enhancements related to Delegations - Start*/
            mnuItmDelegations,
            /*Added for Case#3682 - Enhancements related to Delegations - End*/
    mnuItmPreferences,mnuItmExit,mnuItmSort,
    /*Case 2110 Start*/mnuItmCurrentLocks/*Case 2110 End*/; 
    // Enter menu items for the Edit Menu
    public CoeusMenuItem mnuItmSelectAll, mnuItmDeselectAll;
    // Enter menu items for the Action Menu
    public CoeusMenuItem mnuItmValidationChecks, mnuItmPrint,mnuItmEnterGoals;
    
    public CoeusMenuItem mnuItm294,mnuItm295;
    
    public  CoeusMenu mnuFile,mnuEdit,mnuAction,mnuPrint;
    
    /*
     * to indicate horizondal seperator in menu items
     */
    private final String SEPERATOR = "seperator";
    public JTable tblReports;
    public JScrollPane scrPnReports;
    
    /** Creates a new instance of SubcontractReportListForm */
    public SubcontractReportListForm(String title,CoeusAppletMDIForm mdiForm) {
        super(title, mdiForm);
        this.mdiForm = mdiForm;
        initComponents();
    }
    
    private  void initComponents() {
        setFrameToolBar(getSubcontractBaseWindowToolBar());
        prepareMenus();
        tblReports = new JTable();
        scrPnReports = new JScrollPane(tblReports);
        scrPnReports.getViewport().setBackground(Color.white);
        getContentPane().add(scrPnReports);
        scrPnReports.setViewportView(tblReports); 
        //scrPnReports.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    }
     
    
    private JToolBar getSubcontractBaseWindowToolBar(){
        JToolBar toolBar = new JToolBar();
        
        btnValidationChecks = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.SUB_VALIDATION_ICON)),
        null, "Validation checks");
        
        btnGoals = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.SUB_GOALS_ICON)),
        null, "Enter Goals");
        
        btnSelectAll = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.SUB_SELECT_ALL_ICON)),
        null, "Select All");
        
        btnDeselectAll = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.SUB_DESELECT_ALL_ICON)),
        null, "Deselect All");
        
        btnClose = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.CLOSE_ICON)),
        null, "Close");
        
        //Added by nadh - 19/01/2005 for Sort ToolBar Button - Start
        btnSort = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.SORT_ICON)),null,
        "Sort Subcontracting Reports");
        //Added by Nadh - End
        
        toolBar.add(btnValidationChecks);
        toolBar.add(btnGoals);
        toolBar.addSeparator();
        toolBar.add(btnSelectAll);
        toolBar.add(btnDeselectAll);
        toolBar.add(btnSort);//Added by nadh - 19/01/2005 for Sort ToolBar Button
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
            
            mnuItmClose= new CoeusMenuItem("Close", null, true, true);
            mnuItmClose.setMnemonic('C');
            
//            mnuItmSave = new CoeusMenuItem("Save", null, true, true);
//            mnuItmSave.setMnemonic('S');
//            mnuItmSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,KeyEvent.CTRL_MASK));
            
//            mnuItmPrintSetup= new CoeusMenuItem("Print Setup...", null, true, true);
//            mnuItmPrintSetup.setMnemonic('u');
            
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
            
            mnuItmSort = new CoeusMenuItem("Sort...", null, true,true);
            mnuItmSort.setMnemonic('o');
            
            //Case 2110 Start
            mnuItmCurrentLocks = new CoeusMenuItem("Current Locks",null,true,true);
            mnuItmCurrentLocks.setMnemonic('L');            
            //Case 2110 End
            
            vecFileMenu.add(mnuItmInbox);
            vecFileMenu.add(SEPERATOR);
            vecFileMenu.add(mnuItmSort);
            vecFileMenu.add(SEPERATOR);
            vecFileMenu.add(mnuItmClose);
            vecFileMenu.add(SEPERATOR);
            vecFileMenu.add(mnuItmChangePassword);
            //Case 2110 Start
            vecFileMenu.add(SEPERATOR);
            vecFileMenu.add(mnuItmCurrentLocks);
            vecFileMenu.add(SEPERATOR);
            //Case 2110 end
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
            // holds the data for the Edit Menu
            Vector vecEditMenu = new Vector();
            // Build the Edit Menu...
            mnuItmSelectAll = new CoeusMenuItem("Select All", null, true, true);
            mnuItmSelectAll.setMnemonic('l');
            mnuItmSelectAll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,KeyEvent.CTRL_MASK));
            
            mnuItmDeselectAll = new CoeusMenuItem("Deselect All", null, true, true);
            mnuItmDeselectAll.setMnemonic('D');
            
            vecEditMenu.add(mnuItmSelectAll);
            vecEditMenu.add(SEPERATOR);
            vecEditMenu.add(mnuItmDeselectAll);
            
            
            mnuEdit = new CoeusMenu("Edit", null, vecEditMenu, true, true);
            mnuEdit.setMnemonic('E');
        }
        
            if (mnuAction == null) {
            // holds the data for the Edit Menu
            Vector vecAction = new Vector();
            // Build the Edit Menu...
            mnuItmValidationChecks = new CoeusMenuItem("Validation checks", null, true, true);
            mnuItmValidationChecks.setMnemonic('V');
            Vector vecPrintData = new Vector();
            // create sub menu items for the Print
            mnuItm294 = new CoeusMenuItem("294", null, true, true);
            mnuItm295 = new CoeusMenuItem("295", null, true, true);
            vecPrintData.add(mnuItm294);
            vecPrintData.add(mnuItm295);
            
            mnuPrint = new CoeusMenu("Print", null, vecPrintData, true, true);
            mnuPrint.setMnemonic('P');
            mnuItmEnterGoals = new CoeusMenuItem("Enter Goals", null, true, true);
            mnuItmEnterGoals.setMnemonic('E');
            
            vecAction.add(mnuItmValidationChecks);
            vecAction.add(mnuItmPrint);
            vecAction.add(mnuItmEnterGoals);
            vecAction.add(mnuPrint);
            
            
            mnuAction = new CoeusMenu("Action", null, vecAction, true, true);
            mnuAction.setMnemonic('A');
        }
    }
    
    private void loadMenus() {
        mdiForm.getCoeusMenuBar().remove(0);
        mdiForm.getCoeusMenuBar().add(mnuFile, 0);
        mdiForm.getCoeusMenuBar().add(mnuEdit, 1);
        mdiForm.getCoeusMenuBar().add(mnuAction, 2);
        revalidate();
    }
    
    private void unloadMenus() {
        mdiForm.getCoeusMenuBar().remove(mnuFile);
        mdiForm.getCoeusMenuBar().remove(mnuEdit);
        mdiForm.getCoeusMenuBar().remove(mnuAction);
        mdiForm.getCoeusMenuBar().add(new CoeusFileMenu(mdiForm).getMenu(), 0);
        revalidate();
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
