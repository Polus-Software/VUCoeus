/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.instprop.gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.Vector;

import edu.mit.coeus.gui.*;
import edu.mit.coeus.gui.menu.*;
import edu.mit.coeus.gui.toolbar.CoeusToolBarButton;
import edu.mit.coeus.utils.*;

/**
 * ProposalLogBaseWindow.java
 * Created on May 7, 2004, 2:41 PM
 * @author  Vyjayanthi
 */
public class ProposalLogBaseWindow extends CoeusInternalFrame {
    
    /** Holds an instance of the table */
    public JTable tblResults;
    
    /** Holds an instance of the scrollpane */
    private JScrollPane scrPnResults;
    
    /** Holds an instance of the mdiForm */
    private CoeusAppletMDIForm mdiForm;
    
    /** Menu Items for the Proposal Log - File Menu */
    public CoeusMenuItem mnuItmInbox, mnuItmClose, mnuItmSaveAs,
    /*mnuItmPrintSetup,*/ mnuItmSort, mnuItmChangePassword,
    /*Added for Case#3682 - Enhancements related to Delegations - Start*/
    mnuItmDelegations,
    /*Added for Case#3682 - Enhancements related to Delegations - End*/
    mnuItmPreferences, mnuItmExit,
    /*Case 2110 Start*/mnuItmCurrentLocks/*Case 2110 End*/;
    
    /** Menu Items for the Proposal Log - Edit Menu */
    public CoeusMenuItem mnuItmNewLog, mnuItmTempLog, mnuItmModify,
    mnuItmDisplay, mnuItmMerge;
    
    public CoeusMenuItem mnuItmSearch;
    
    /** Holds the File menu and Edit menu */
    public CoeusMenu mnuFile, mnuEdit, mnuTools;
    
    /** ToolBar buttons for Proposal Log */
    public CoeusToolBarButton btnNewLog, btnTempLog, btnModify, btnDisplay,
    btnMerge, btnSortProposalLogs, btnSearch, btnSaveAs, btnClose;
    
    /** Horizontal seperator in menu items */
    private static final String SEPERATOR = "seperator";
    
    /**
     * Creates a new instance of ProposalLogBaseWindow
     * @param mdiForm holds the <CODE>CoeusAppletMDIForm</CODE> as parent.
     * @param title the Frame title
     */
    public ProposalLogBaseWindow(String title, CoeusAppletMDIForm mdiForm) {
        super(title, mdiForm);
        this.mdiForm = mdiForm;
    }
    
    /**
     * Initialize the menus and toolbar
     * @param tblEmptyResults Contains empty table
     */
    public void initComponents(JTable tblEmptyResults) {
        
        setFrameToolBar(getProposalLogToolBar());
        createMenus();
        
        tblResults = tblEmptyResults;
        
        scrPnResults = new JScrollPane(tblResults);
        
        scrPnResults.getViewport().setBackground(Color.white);

        getContentPane().add(scrPnResults);

    }
    
    /** To construct all the menus related to Proposal Log
     */
    private void createMenus(){
        Vector vecFile = new Vector();
        Vector vecEdit = new Vector();
        
        //Construct the File menu with sub menu Inbox, Close, Save, Print Setup,
        //Change Password, Preferences, Exit
        
        mnuItmInbox = new CoeusMenuItem("Inbox", null, true, true);
        mnuItmInbox.setMnemonic('I');        
        
        mnuItmClose = new CoeusMenuItem("Close", null, true, true);
        mnuItmClose.setMnemonic('C');
        
        mnuItmSaveAs = new CoeusMenuItem("Save As...", null, true, true);
        mnuItmSaveAs.setMnemonic('A');
        
        //Commented since we are not using it in Coeus 4.0
        //mnuItmPrintSetup = new CoeusMenuItem("Print Setup...", null, true, true);
        //mnuItmPrintSetup.setMnemonic('u');
        
        mnuItmSort = new CoeusMenuItem("Sort...", null, true, true);
        mnuItmSort.setMnemonic('o');
        
        mnuItmChangePassword = new CoeusMenuItem("Change Password", null, true, true);
        mnuItmChangePassword.setMnemonic('h');
                
        //Added for Case#3682 - Enhancements related to Delegations - Start             
        mnuItmDelegations= new CoeusMenuItem("Delegations...", null, true, true);
        mnuItmDelegations.setMnemonic('l');
        //Added for Case#3682 - Enhancements related to Delegations - End        
        
        mnuItmPreferences = new CoeusMenuItem("Preferences...", null, true, true);
        mnuItmPreferences.setMnemonic('P');
        
        mnuItmExit = new CoeusMenuItem("Exit", null, true, true);
        mnuItmExit.setMnemonic('x');
        
        //Case 2110 Start
        mnuItmCurrentLocks =  new CoeusMenuItem("Current Locks",null,true,true);
        mnuItmCurrentLocks.setMnemonic('L');
        //Case 2110 End
        
        vecFile.add(mnuItmInbox);
        vecFile.add(SEPERATOR);
        vecFile.add(mnuItmClose);
        vecFile.add(SEPERATOR);
        vecFile.add(mnuItmSaveAs);
        
        //Commented since we are not using it in Coeus 4.0
        //vecFile.add(mnuItmPrintSetup);
        //vecFile.add(SEPERATOR);
        
        vecFile.add(mnuItmSort);
        vecFile.add(mnuItmChangePassword);
        //Case 2110 Start
        vecFile.add(SEPERATOR);
        vecFile.add(mnuItmCurrentLocks);
        vecFile.add(SEPERATOR);
        //Case 2110 End
        //Added for Case#3682 - Enhancements related to Delegations - Start 
        vecFile.add(mnuItmDelegations);
        vecFile.add(SEPERATOR);
        //Added for Case#3682 - Enhancements related to Delegations - End
        vecFile.add(mnuItmPreferences);
        vecFile.add(SEPERATOR);
        vecFile.add(mnuItmExit);
        
        
        //Construct the Edit menu with sub menu Add, Notify, Medusa
        
        mnuItmNewLog = new CoeusMenuItem("New Log", null, true, true);
        mnuItmNewLog.setMnemonic('N');
        mnuItmNewLog.setEnabled(false);
        
        mnuItmTempLog = new CoeusMenuItem("Temp Log", null, true, true);
        mnuItmTempLog.setMnemonic('T');
        mnuItmTempLog.setEnabled(false);
        
        mnuItmModify = new CoeusMenuItem("Modify", null, true, true);
        mnuItmModify.setMnemonic('M');
        mnuItmModify.setEnabled(false);
        
        mnuItmDisplay = new CoeusMenuItem("Display", null, true, true);
        mnuItmDisplay.setMnemonic('D');
        mnuItmDisplay.setEnabled(false);
        
        mnuItmMerge = new CoeusMenuItem("Merge", null, true, true);
        mnuItmMerge.setMnemonic('g');
        mnuItmMerge.setEnabled(false);
        
        vecEdit.add(mnuItmNewLog);
        vecEdit.add(mnuItmModify);
        vecEdit.add(mnuItmDisplay);
        vecEdit.add(SEPERATOR);
        vecEdit.add(mnuItmTempLog);
        vecEdit.add(mnuItmMerge);
        
        //Construct the Tools Menu.
        mnuItmSearch = new CoeusMenuItem("Search", null,true, true);
        mnuItmSearch.setMnemonic('S');
        
        Vector vecTools = new Vector();
        vecTools.add(mnuItmSearch);

        mnuFile = new CoeusMenu("File", null, vecFile, true, true);
        mnuFile.setMnemonic('F');
        
        mnuEdit = new CoeusMenu("Edit", null, vecEdit, true, true);
        mnuEdit.setMnemonic('E');
        
        mnuTools = new CoeusMenu("Tools", null, vecTools, true, true);
        mnuTools.setMnemonic('T');
        
    }
    
    /** Construct all the menus related to Proposal Log
     */
    private void loadMenus() {
        mdiForm.getCoeusMenuBar().remove(0);
        mdiForm.getCoeusMenuBar().add(mnuFile, 0);
        mdiForm.getCoeusMenuBar().add(mnuEdit, 1);
        mdiForm.getCoeusMenuBar().add(mnuTools, 6);
        
        mdiForm.getCoeusMenuBar().validate();
    }
    
    /** Remove all the menus related to Proposal Log
     */
    private void unloadMenus() {
        mdiForm.getCoeusMenuBar().remove(mnuFile);
        mdiForm.getCoeusMenuBar().add(new CoeusFileMenu(mdiForm).getMenu(), 0);
        mdiForm.getCoeusMenuBar().remove(mnuEdit);
        mdiForm.getCoeusMenuBar().remove(mnuTools);
        
        mdiForm.getCoeusMenuBar().validate();
    }
    
    /** Constructs Proposal Log ToolBar 
     * @return toolBar the prepared toolBar
     */
    private JToolBar getProposalLogToolBar() {
        JToolBar toolBar = new JToolBar();
        
        btnNewLog = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.ADD_ICON)),
        null,"Create a New Proposal Log");
        btnNewLog.setEnabled(false);
        
        btnNewLog.setDisabledIcon(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.DADD_ICON)));
        
        btnTempLog = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.IP_TEMP_LOG_ICON)),
        null,"Create a new Temporary Log");
        btnTempLog.setEnabled(false);
        
        btnModify = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.EDIT_ICON)),
        null, "Correct Proposal");
        btnModify.setEnabled(false);

        btnModify.setDisabledIcon(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.DEDIT_ICON)));

        btnDisplay = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.DISPLAY_ICON)),
        null, "Display Proposal");
        btnDisplay.setEnabled(false);
        
        btnMerge = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.IP_MERGE_ICON)),
        null, "Merge Proposals");
        btnMerge.setEnabled(false);
                
        btnSortProposalLogs = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.SORT_ICON)),
        null, "Sort Proposal Logs");
        
        btnSearch = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.SEARCH_ICON)),
        null, "Search for Proposal Log");
        
        btnSaveAs = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.SAVEAS_ICON)),
        null, "Save As");
        
        btnClose = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.CLOSE_ICON)),
        null,"Close");
        
        toolBar.add(btnNewLog);
        toolBar.add(btnModify);
        toolBar.add(btnDisplay);
        toolBar.addSeparator();
        toolBar.add(btnTempLog);
        toolBar.add(btnMerge);
        toolBar.addSeparator();
        toolBar.add(btnSortProposalLogs);
        toolBar.add(btnSearch);
        toolBar.add(btnSaveAs);
        toolBar.addSeparator();
        toolBar.add(btnClose);
        
        return toolBar;
    }
    
    /** Add custom tool bar and menu bar to the application
     *  when the internal frame is activated.
     * @param e InternalFrameEvent which delegates the event.
     */
    public void internalFrameActivated(InternalFrameEvent e) {
        super.internalFrameActivated( e );
        loadMenus();
    }

    /** Remove custom tool bar and menu bar to the application
     *  when the internal frame is deactivated.
     * @param e InternalFrameEvent which delegates the event.
     */    
    public void internalFrameDeactivated(InternalFrameEvent e) {
        unloadMenus();
        super.internalFrameDeactivated(e);
    }
    
    /** Displays the results
     * @param results takes an instance of JTable
     */
    public void displayResults(JTable results) {
        if(results == null) return ;
        tblResults = results;
        scrPnResults.setViewportView(tblResults);
        this.revalidate();
    }
    
    /** Abstract method of CoeusInternalFrame
     */
    public void saveActiveSheet() {
    }
    
    /** Abstract method of CoeusInternalFrame
     */
    public void saveAsActiveSheet() {
    }
    
}
