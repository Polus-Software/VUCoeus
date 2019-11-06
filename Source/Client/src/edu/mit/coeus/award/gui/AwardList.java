/*
 * AwardBaseWindow.java
 *
 * Created on March 15, 2004, 1:51 PM
 */

package edu.mit.coeus.award.gui;

/**
 *
 * @author  sharathk
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;

import edu.mit.coeus.gui.*;
import edu.mit.coeus.gui.menu.*;
import edu.mit.coeus.gui.toolbar.CoeusToolBarButton;
import edu.mit.coeus.utils.*;

public class AwardList extends CoeusInternalFrame{
    
    public JTable tblResults;
    private JScrollPane scrPnResults;
    
    private CoeusAppletMDIForm mdiForm;
    
    /*
     * to indicate horizondal seperator in menu items
     */
    private final String SEPERATOR="seperator";
    
    public CoeusToolBarButton btnCreateNewAward, btnCorrectAward, btnDisplayAward, btnAwardNewEntry,
    btnAwardHierarchy, btnNotepad, btnReportingRequirements, btnMedusa,
    btnSortAwards, btnAwardSummary, btnSearch, btnSaveAs, btnClose;
    
    //File Menu Items
    public CoeusMenuItem mnuItmInbox, mnuItmSaveAs,/*mnuItmPrintSetup,*/  mnuItmSort,
    mnuItmSummary, mnuItmClose, mnuItmChangePassword,
            /*Added for Case#3682 - Enhancements related to Delegations - Start*/
            mnuItmDelegations,
            /*Added for Case#3682 - Enhancements related to Delegations - End*/
    mnuItmPreferences,
    mnuItmExit,/*Case 2110 Start*/mnuItmCurrentLocks/*Case 2110 End*/;
    
    //Tools Menu Items
    public CoeusMenuItem mnuItmSearch;
    
    //Edit Menu Items
    public CoeusMenuItem mnuItmNewAward, mnuItmCorrectAward, mnuItmDisplayAward, 
    mnuItmNewEntry, mnuItmAwardHierarchy, mnuItmNotepad, mnuItmReportingReq, 
    mnuItmGenReportingReq, mnuItmMedusa;
    
    private CoeusMenu mnuFile, mnuEdit, mnuTools;
    
    /** Creates a new instance of AwardBaseWindow */
    public AwardList(String title, CoeusAppletMDIForm mdiForm) {
        super(title, mdiForm);
        this.mdiForm = mdiForm;
    }
    
    public void initComponents(JTable tblEmptyResults) {
        
        setFrameToolBar(getAwardToolBar());
        prepareMenus();
        
        tblResults = tblEmptyResults;
        
        scrPnResults = new JScrollPane(tblResults);
        
        //Bug Fix : background color should be white not gray - START
        scrPnResults.getViewport().setBackground(Color.white);
        //Bug Fix : background color should be white not gray - END
        
        getContentPane().add(scrPnResults);
        // Bug Fix - To avoid setting the table column sizes. 
        // The sizes are set in CoeusSearch.XML - Start
      //  setColumnSizes();
        //End
    }
    
    // Bug Fix - To avoid setting the table column sizes. 
     // The sizes are set in CoeusSearch.XML - Start
//    private void setColumnSizes() {
//        int colSize[] = {150, 100, 150, 100, 100, 75, 200, 100, 200, 200, 200};
//        for(int columnIndex = 0; columnIndex < colSize.length; columnIndex++) {
//            tblResults.getColumnModel().getColumn(columnIndex).setPreferredWidth(colSize[columnIndex]);
//            tblResults.getColumnModel().getColumn(columnIndex).setMinWidth(colSize[columnIndex]);
//        }
//    }// end
    
    private JToolBar getAwardToolBar() {
        JToolBar awardToolBar = new JToolBar();
        
        btnCreateNewAward = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.ADD_ICON)),
        null, "Create New Award");
        
        btnCorrectAward = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.EDIT_ICON)),
        null, "Correct Award");
        
        btnDisplayAward = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.DISPLAY_ICON)),
        null, "Display Award");
        
        btnAwardNewEntry = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.AWARD_NEW_ENTRY_ICON)),
        null, "Award New Entry");
        
        btnAwardHierarchy = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.AWARD_HIERARCHY)),
        null, "Award Hierarchy");
        
        btnNotepad = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.NOTEPAD_ICON)),
        null, "Notepad");
        
        btnReportingRequirements = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.REPORTING_REQUIREMENTS_ICON)),
        null, "Reporting Requirements");
        
        btnMedusa = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.MEDUSA_ICON)),
        null, "Medusa");
        
        btnSortAwards = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.SORT_ICON)),
        null, "Sort Awards");
        
        btnAwardSummary = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.ENABLED_SUMMARY_ICON)),
        null, "Award Summary");
        
        btnSearch = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.SEARCH_ICON)),
        null, "Search for Award");
        
        btnSaveAs = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.SAVEAS_ICON)),
        null, "Save As");
        
        btnClose = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.CLOSE_ICON)),
        null, "Close");
        
        awardToolBar.add(btnCreateNewAward);
        awardToolBar.add(btnCorrectAward);
        awardToolBar.add(btnDisplayAward);
        awardToolBar.add(btnAwardNewEntry);
        awardToolBar.add(btnAwardHierarchy);
        awardToolBar.add(btnNotepad);
        awardToolBar.add(btnReportingRequirements);
        awardToolBar.addSeparator();
        awardToolBar.add(btnMedusa);
        awardToolBar.addSeparator();
        awardToolBar.add(btnSortAwards);
        awardToolBar.add(btnAwardSummary);
        awardToolBar.add(btnSearch);
        awardToolBar.add(btnSaveAs);
        awardToolBar.addSeparator();
        awardToolBar.add(btnClose);
        
        return awardToolBar;
    }
    
    private void prepareMenus() {
        //build File Menu
        Vector vecFile = new Vector();
        
        mnuItmInbox = new CoeusMenuItem("Inbox", null, true, true);
        mnuItmInbox.setMnemonic('I');
        mnuItmSaveAs = new CoeusMenuItem("SaveAs", null, true, true);
        
        //Commented since we are not using it in Coeus 4.0
        //mnuItmPrintSetup = new CoeusMenuItem("PrintSetup", null, true, true);
        //mnuItmPrintSetup.setMnemonic('u');
        
        mnuItmSort = new CoeusMenuItem("Sort", null, true, true);
        mnuItmSort.setMnemonic('o');
        
        mnuItmSummary = new CoeusMenuItem("Summary", null, true, true);
        mnuItmSummary.setMnemonic('s');
        
        mnuItmClose = new CoeusMenuItem("Close", null, true, true);
        mnuItmClose.setMnemonic('c');
        
        mnuItmChangePassword = new CoeusMenuItem("Change Password", null, true, true);
        mnuItmChangePassword.setMnemonic('h');
        
        //Added for Case#3682 - Enhancements related to Delegations - Start 
        mnuItmDelegations = new CoeusMenuItem("Delegations", null, true, true);
        mnuItmDelegations.setMnemonic('D');
        //Added for Case#3682 - Enhancements related to Delegations - End
        
        mnuItmPreferences = new CoeusMenuItem("Preferences", null, true, true);
        mnuItmPreferences.setMnemonic('p');
        
        mnuItmExit = new CoeusMenuItem("Exit", null, true, true);
        mnuItmExit.setMnemonic('x');
        
        //Case 2110 Start
        mnuItmCurrentLocks  = new CoeusMenuItem("Current Locks",null,true,true);
        mnuItmCurrentLocks.setMnemonic('L');
        //Case 2110 End
        
        vecFile.add(mnuItmInbox);
        vecFile.add(SEPERATOR);
        vecFile.add(mnuItmSaveAs);
        vecFile.add(SEPERATOR);
        
        //Commented since we are not using it in Coeus 4.0
        //vecFile.add(mnuItmPrintSetup);
        //vecFile.add(SEPERATOR);
        
        vecFile.add(mnuItmSort);
        vecFile.add(mnuItmSummary);
        vecFile.add(mnuItmClose);
        vecFile.add(mnuItmChangePassword);
        //Case 2110 Start
        vecFile.add(SEPERATOR);
        vecFile.add(mnuItmCurrentLocks);
        vecFile.add(SEPERATOR);
        //Case 2110 End
        //Added for Case#3682 - Enhancements related to Delegations - start
        vecFile.add(mnuItmDelegations);
        vecFile.add(SEPERATOR);
        //Added for Case#3682 - Enhancements related to Delegations - End
        vecFile.add(mnuItmPreferences);
        vecFile.add(SEPERATOR);
        vecFile.add(mnuItmExit);
        
        //build Edit Menu
        Vector vecEdit = new Vector();
        
        mnuItmNewAward = new CoeusMenuItem("New Award", null, true, true);
        mnuItmNewAward.setMnemonic('N');
        
        mnuItmCorrectAward = new CoeusMenuItem("Correct Award", null, true, true);
        mnuItmCorrectAward.setMnemonic('C');
        
        mnuItmDisplayAward = new CoeusMenuItem("Display Award", null, true, true);
        mnuItmDisplayAward.setMnemonic('i');
        
        mnuItmNewEntry = new CoeusMenuItem("New Entry", null, true, true);
        mnuItmNewEntry.setMnemonic('e');
        
        mnuItmAwardHierarchy = new CoeusMenuItem("Award Hierarchy", null, true, true);
        mnuItmAwardHierarchy.setMnemonic('H');
        
        mnuItmNotepad = new CoeusMenuItem("Notepad", null, true, true);
        mnuItmNotepad.setMnemonic('t');
        
        mnuItmReportingReq = new CoeusMenuItem("Reporting Requirements", null, true, true);
        mnuItmReportingReq.setMnemonic('R');
        
        mnuItmGenReportingReq = new CoeusMenuItem("Generate Reporting Requirements", null, true, true);
        mnuItmGenReportingReq.setMnemonic('G');
        
        mnuItmMedusa = new CoeusMenuItem("Medusa", null, true, true);
        mnuItmMedusa.setMnemonic('M');        
        mnuItmMedusa.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, ActionEvent.CTRL_MASK));
        
        vecEdit.add(mnuItmNewAward);
        vecEdit.add(mnuItmCorrectAward);
        vecEdit.add(mnuItmDisplayAward);
        vecEdit.add(mnuItmNewEntry);
        vecEdit.add(SEPERATOR);
        vecEdit.add(mnuItmAwardHierarchy);
        vecEdit.add(mnuItmNotepad);
        vecEdit.add(SEPERATOR);
        vecEdit.add(mnuItmReportingReq);
        vecEdit.add(mnuItmGenReportingReq);
        vecEdit.add(SEPERATOR);
        vecEdit.add(mnuItmMedusa);
        
        //Build tools Menu.
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
    
    private void loadMenus() {
        
        mdiForm.getCoeusMenuBar().remove(0);
        mdiForm.getCoeusMenuBar().add(mnuFile, 0);
        mdiForm.getCoeusMenuBar().add(mnuEdit, 1);
        mdiForm.getCoeusMenuBar().add(mnuTools, 6);
        //Setting up Edit Menu Items
        
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
    
    public void saveActiveSheet() {
    }
    
    public void saveAsActiveSheet() {
    }
    
    public void mouseClicked(MouseEvent mouseEvent) {
        int clickCount = mouseEvent.getClickCount();
        if(clickCount != 2) return ;
        //Click Count == 2 Display Award in Display Mode.
        CoeusOptionPane.showInfoDialog("Functionality not implemented : will display selected award in display Mode");
    }
    
    public void mouseEntered(MouseEvent mouseEvent) {
    }
    
    public void mouseExited(MouseEvent mouseEvent) {
    }
    
    public void mousePressed(MouseEvent mouseEvent) {
    }
    
    public void mouseReleased(MouseEvent mouseEvent) {
    }
    
    public void internalFrameActivated(InternalFrameEvent e) {
        super.internalFrameActivated(e);
        loadMenus();
    }
    
    
    public void internalFrameDeactivated(InternalFrameEvent e) {
        unloadMenus();
        super.internalFrameDeactivated(e);
        
    }
    
}
