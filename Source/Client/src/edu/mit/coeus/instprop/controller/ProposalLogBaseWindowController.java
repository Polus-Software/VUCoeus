/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.instprop.controller;

import edu.mit.coeus.user.gui.UserDelegationForm;
import edu.mit.coeus.utils.CoeusPropertyKeys;
import edu.mit.coeus.utils.CoeusServerProperties;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Observer;
import java.util.Vector;

import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusClientException;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.exception.CoeusUIException;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.instprop.bean.InstituteProposalLogBean;
import edu.mit.coeus.instprop.gui.ProposalLogBaseWindow;
import edu.mit.coeus.propdev.gui.InboxDetailForm;
import edu.mit.coeus.search.gui.CoeusSearch;
import edu.mit.coeus.search.gui.InstituteProposalSearch;
import edu.mit.coeus.user.gui.UserPreferencesForm;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.BaseWindowObservable;
import edu.mit.coeus.utils.ChangePassword;
import edu.mit.coeus.utils.CoeusDateFormat;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.CurrentLockForm;
import edu.mit.coeus.utils.MultipleTableColumnSorter;
import edu.mit.coeus.utils.SortForm;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.Utils;
import edu.mit.coeus.utils.DateUtils;
import edu.mit.coeus.utils.saveas.SaveAsDialog;
import edu.ucsd.coeus.personalization.controller.AbstractController;

/**
 * ProposalLogBaseWindowController.java
 * Created on May 10, 2004, 10:42 AM
 * @author  Vyjayanthi
 */
public class ProposalLogBaseWindowController extends InstituteProposalController
implements ActionListener, VetoableChangeListener, Observer {
    
    /** Holds an instance of Proposal Log Base Window */
    private ProposalLogBaseWindow proposalLogBaseWindow;
    
    /** Holds an instance of <CODE>InstituteProposalLogDetailsController</CODE> */
    private InstituteProposalLogDetailsController instituteProposalLog;
    
    /** Holds an instance of the mdiForm */
    private CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();

    /** Holds an instance of Coeus Serach to find proposals */
    private CoeusSearch coeusSearch;
    
    /** Holds an instance of the results table */
    private JTable tblResultsTable;
    
    /** Holds the selectedRow in the proposal log list
     */
    private int selectedRow;
    
    /** Holds the selectedRow in the Proposal Log List
     */
    private int baseTableRow;
    
    /** Holds true if the internal frame is closed, false otherwise */
    private boolean closed;
    
    /** Holds an instance of the ProposalLogMouseAdapter */
    private ProposalLogMouseAdapter proposalLogMouseAdapter;
    
    /** Holds all the log status */
    private static Hashtable htProposalStatus;
    
    /** Holds an instance of <CODE>CoeusMessageResources</CODE> 
     * for reading message Properties
     */
    private CoeusMessageResources coeusMessageResources = CoeusMessageResources.getInstance();
    
    private static final int NUMBER_COLUMN = 0;
    private static final int TITLE_COLUMN = 1;
    private static final int PROP_TYPE_COLUMN = 2;
    private static final int PI_COLUMN = 3;
    private static final int UNIT_COLUMN = 4;
    private static final int UNIT_COLUMN_NAME = 5;
    private static final int SPONSOR_CODE_COLUMN = 6;
    private static final int SPONSOR_NAME_COLUMN = 7;
    private static final int STATUS_COLUMN = 8;
    private static final int COMMENTS_COLUMN = 9;
    //case 3466 start    
//    private static final int TIMESTAMP_COLUMN = 10;
//    private static final int UPDATE_USER_COLUMN = 11;
    private static final int DEADLINE_COLUMN = 10;
    private static final int CREATETIMESTAMP_COLUMN = 11;
    private static final int TIMESTAMP_COLUMN = 12;
    private static final int CREATE_USER_COLUMN = 13;
    private static final int UPDATE_USER_COLUMN = 14;    
    //case 3466 end
    private static final String TEMPORARY_STATUS = "Temporary";
    private static final String SUBMITTED_STATUS = "Submitted";
    private static final String PROPOSAL_SEARCH = "PROPOSALSEARCH";
    
    private static final char MERGE_PROPOSAL_LOG = 'F';
    private static final char CHECK_PROPOSAL_LOG_RIGHTS = 'J';
    private static final char TEMP_LOG = 'T';
    private static final String CONNECTION_STRING = 
    CoeusGuiConstants.CONNECTION_URL + "/InstituteProposalMaintenanceServlet";
    
    private static final String LOG_STATUS_SHOULD_BE_TEMPORARY = 
        "instPropLog_exceptionCode.1401";
    private static final String TEMPORARY_PROPOSALS_START_WITH_CHAR = 
        "instPropLog_exceptionCode.1402";
    private static final String CANNOT_MODIFY_SUBMITTED_PROPOSAL = 
        "instPropLog_exceptionCode.1403";
    
    private ChangePassword changePassword;
    
    private UserPreferencesForm userPreferencesForm;
    //Added for Case#3682 - Enhancements related to Delegations - Start
    private UserDelegationForm userDelegationForm;
    //Added for Case#3682 - Enhancements related to Delegations -End
    
    //holds sorted columns and its states
    private Vector vecSortedData;
    
    private static final int OK_CLICKED = 0;
    
    //holds column index
    private int oldCol = -1;
    
    //holds column status
    private int status=MultipleTableColumnSorter.NOT_SORTED;
    
    //Added for case#3243
    private static final String SIMPLE_DATE_FORMAT = "yyyy/MM/dd";        
    
    //Added for Case#3587 - multicampus enhancement  - Start 
    private static final String CANNOT_MERGE_PROPOSAL_LOG = "instPropLog_exceptionCode.1415";
    private static final String CANNOT_MODIFY_PROPOSAL_LOG = "instPropLog_exceptionCode.1414";
    private static final String MODIFY_PROPOSAL_LOG = "MODIFY_PROPOSAL_LOG";
    //Case#3587 - End
    
    /** Creates a new instance of ProposalLogBaseWindowController */
    public ProposalLogBaseWindowController() {
        initComponents();
        registerComponents();
    }
    
    /** To perform initialization of the form
     */
    private void initComponents(){
        JTable tblResults=null; 
        try{
            proposalLogBaseWindow = new ProposalLogBaseWindow(
            CoeusGuiConstants.PROPOSAL_LOG_FRAME_TITLE, mdiForm);
            
            coeusSearch = new CoeusSearch(mdiForm, "PROPOSALLOGSEARCH", CoeusSearch.NO_TAB);
            tblResults = coeusSearch.getEmptyResTable();            
            proposalLogBaseWindow.initComponents(tblResults);
             if(tblResults!= null){
                javax.swing.table.TableColumn clmName
                    = tblResults.getColumnModel().getColumn(
                        tblResults.getColumnCount()-1);
                clmName.setPreferredWidth(0);
                clmName.setMaxWidth(0);
                clmName.setMinWidth(0);
                clmName.setWidth(0);
            }
            enableMenuItems();
            // rdias - UCSD coeuspersonalization
            AbstractController persnref = AbstractController.getPersonalizationControllerRef();
            persnref.customize_module(proposalLogBaseWindow,
            		proposalLogBaseWindow,this, "GENERIC");
            // rdias - UCSD coeuspersonalization                        
        }catch (Exception exception) {
            exception.printStackTrace();
        }
        proposalLogMouseAdapter = new ProposalLogMouseAdapter();
        if(tblResults!= null){
            tblResults.addMouseListener(proposalLogMouseAdapter);
        }
    }
    
    /** To populate the htProposalStatus with log status
     */
    static {
        htProposalStatus = new Hashtable();
        htProposalStatus.put("S", "Submitted");
        htProposalStatus.put("V", "Void");
        htProposalStatus.put("P", "Pending");
        htProposalStatus.put("T", "Temporary");
        htProposalStatus.put("M", "Merged");
    }
    
    /** Displays Proposal Log Search Window.
     */
    private void showProposalLogSearch() {
        try {
            coeusSearch.showSearchWindow();
            tblResultsTable = coeusSearch.getSearchResTable();
            proposalLogBaseWindow.displayResults(tblResultsTable);
            if(tblResultsTable!= null){
                javax.swing.table.TableColumn clmName
                    = tblResultsTable.getColumnModel().getColumn(
                        tblResultsTable.getColumnCount()-1);
                clmName.setPreferredWidth(0);
                clmName.setMaxWidth(0);
                clmName.setMinWidth(0);
                clmName.setWidth(0);
            }
            
            //Add listener for table
            if(tblResultsTable != null) {
                proposalLogBaseWindow.tblResults.addMouseListener(proposalLogMouseAdapter);
                proposalLogBaseWindow.tblResults.getTableHeader().addMouseListener(proposalLogMouseAdapter);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /** Check for rights to enable the buttons and menu items
     * @throws CoeusClientException
     */
    private void enableMenuItems() throws CoeusClientException {
        RequesterBean requester = new RequesterBean();
        CoeusVector cvData = null;
        requester.setFunctionType(CHECK_PROPOSAL_LOG_RIGHTS);
        AppletServletCommunicator comm = new AppletServletCommunicator(
            CONNECTION_STRING, requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response.isSuccessfulResponse()){
            cvData = (CoeusVector)response.getDataObject();
        }else {
            throw new CoeusClientException(response.getMessage());
        }
        
        if( cvData != null && cvData.size() > 0 ){
            
            //Enable New Log, Display, Temp Log if user has right to create proposal log
          
            if( ((Boolean)cvData.get(0)).booleanValue() ){
                proposalLogBaseWindow.mnuItmNewLog.setEnabled(true);
                proposalLogBaseWindow.btnNewLog.setEnabled(true);
                
                proposalLogBaseWindow.mnuItmDisplay.setEnabled(true);
                proposalLogBaseWindow.btnDisplay.setEnabled(true);
                
                proposalLogBaseWindow.mnuItmTempLog.setEnabled(true);
                proposalLogBaseWindow.btnTempLog.setEnabled(true);
            }
            
            //Enable Temp Log if the user has right to create temporary log
            if( ((Boolean)cvData.get(1)).booleanValue() ){
                proposalLogBaseWindow.mnuItmTempLog.setEnabled(true);
                proposalLogBaseWindow.btnTempLog.setEnabled(true);
            }
            
            //Enable Merge if the user has rights to create or modify institute proposal
            if( ((Boolean)cvData.get(2)).booleanValue() ||
            ((Boolean)cvData.get(3)).booleanValue() ){
                proposalLogBaseWindow.mnuItmMerge.setEnabled(true);
                proposalLogBaseWindow.btnMerge.setEnabled(true);
            }
            
            //Enable Modify and Display if the user has right to modify proposal log
            if( ((Boolean)cvData.get(4)).booleanValue() ){
                proposalLogBaseWindow.mnuItmModify.setEnabled(true);
                proposalLogBaseWindow.btnModify.setEnabled(true);

                proposalLogBaseWindow.mnuItmDisplay.setEnabled(true);
                proposalLogBaseWindow.btnDisplay.setEnabled(true);
            }else if( ((Boolean)cvData.get(5)).booleanValue() ){
                //Enable Display if user has right to view proposal log in any unit
                proposalLogBaseWindow.mnuItmDisplay.setEnabled(true);
                proposalLogBaseWindow.btnDisplay.setEnabled(true);
            }
        }
    }
    
    /** Displays the Form which is being controlled.
     */
    public void display() {
        try{
            mdiForm.putFrame(CoeusGuiConstants.PROPOSAL_LOG_FRAME_TITLE, proposalLogBaseWindow);
            mdiForm.getDeskTopPane().add(proposalLogBaseWindow);
            proposalLogBaseWindow.setSelected(true);
            proposalLogBaseWindow.setVisible(true);
            //commented by ravi on 7/Jun/04 as client doesn't want to show the search
            //initially when the screen opens.
//            showProposalLogSearch();
        }catch (PropertyVetoException propertyVetoException) {
            propertyVetoException.printStackTrace();
        }
    }
    
    /** Perform field formatting.
     * enabling, disabling components depending on the different conditions
     */
    public void formatFields() {
    }
    
    /** An overridden method of the controller
     * @return  returns the controlled form component
     */
    public java.awt.Component getControlledUI() {
        return proposalLogBaseWindow;
    }
    
    /** Returns the form data
     * @return returns the form data
     */
    public Object getFormData() {
        return null;
    }
    
    /** This method is used to set the listeners to the components.
     */
    public void registerComponents() {
        proposalLogBaseWindow.addVetoableChangeListener(this);
        
        //Add Listeners for File Menu Items
        proposalLogBaseWindow.mnuItmInbox.addActionListener(this);
        proposalLogBaseWindow.mnuItmClose.addActionListener(this);
        proposalLogBaseWindow.mnuItmSaveAs.addActionListener(this);
        //Case 2110 Start
        proposalLogBaseWindow.mnuItmCurrentLocks.addActionListener(this);
        //Case 2110 End
        
        //Commented since we are not using it in Coeus 4.0
        //proposalLogBaseWindow.mnuItmPrintSetup.addActionListener(this);
        
        proposalLogBaseWindow.mnuItmSort.addActionListener(this);
        proposalLogBaseWindow.mnuItmChangePassword.addActionListener(this);
        //Added for Case#3682 - Enhancements related to Delegations -Start
        proposalLogBaseWindow.mnuItmDelegations.addActionListener(this);
        //Added for Case#3682 - Enhancements related to Delegations -End
        proposalLogBaseWindow.mnuItmPreferences.addActionListener(this);
        proposalLogBaseWindow.mnuItmExit.addActionListener(this);
        
        //Add Listeners for Edit Menu Items
        proposalLogBaseWindow.mnuItmNewLog.addActionListener(this);
        proposalLogBaseWindow.mnuItmTempLog.addActionListener(this);
        proposalLogBaseWindow.mnuItmModify.addActionListener(this);
        proposalLogBaseWindow.mnuItmDisplay.addActionListener(this);
        proposalLogBaseWindow.mnuItmMerge.addActionListener(this);
        
        //Add Listeners for Tools Menu Items
        proposalLogBaseWindow.mnuItmSearch.addActionListener(this);
        
        //Add Listeners for ToolBars
        proposalLogBaseWindow.btnNewLog.addActionListener(this);
        proposalLogBaseWindow.btnTempLog.addActionListener(this);
        proposalLogBaseWindow.btnModify.addActionListener(this);
        proposalLogBaseWindow.btnDisplay.addActionListener(this);
        proposalLogBaseWindow.btnMerge.addActionListener(this);
        proposalLogBaseWindow.btnSortProposalLogs.addActionListener(this);
        proposalLogBaseWindow.btnSearch.addActionListener(this);
        proposalLogBaseWindow.btnSaveAs.addActionListener(this);
        proposalLogBaseWindow.btnClose.addActionListener(this);
        
    }
    
    /** Saves the Form Data.
     */
    public void saveFormData() {
    }
    
    /** This method is used to set the form data specified in
     * <CODE> data </CODE>
     * @param data data to set to the form
     */
    public void setFormData(Object data) {
    }
    
    /** Validate the form data/Form and returns true if
     * validation is through else returns false.
     * @throws CoeusUIException if some exception occurs or some validation fails.
     * @return true if
     * validation is through else returns false.
     */
    public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
        return false;
    }
    
    /** Close the frame
     */
    private void close() {
        mdiForm.removeFrame(CoeusGuiConstants.PROPOSAL_LOG_FRAME_TITLE);
        closed = true;
        //Select next Internal Frame.
        proposalLogBaseWindow.doDefaultCloseAction();
        
    }
    
    /** This method triggers all actions based on the event occured
     * @param actionEvent takes the actionEvent
     */
    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        try{
            blockEvents(true);
            mdiForm.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            if( source.equals(proposalLogBaseWindow.mnuItmInbox ) ){
                showInboxDetails();
            }else if( source.equals(proposalLogBaseWindow.btnNewLog) || 
            source.equals(proposalLogBaseWindow.mnuItmNewLog) ){
                showProposalLog(TypeConstants.ADD_MODE);
            }else if( source.equals(proposalLogBaseWindow.btnModify) ||
                    source.equals(proposalLogBaseWindow.mnuItmModify) ){
                //Modified for Case#3587 - multicampus enhancement  - Start
//                showProposalLog(TypeConstants.MODIFY_MODE);
                selectedRow = proposalLogBaseWindow.tblResults.getSelectedRow();
                String leadUnitNumber = EMPTY_STRING;
                if(selectedRow > -1){
                    leadUnitNumber = (String)proposalLogBaseWindow.tblResults.getValueAt(selectedRow, UNIT_COLUMN);
                    boolean hasRight = checkUserHasRightInLeadUnit(leadUnitNumber,MODIFY_PROPOSAL_LOG);
                    if(hasRight){
                        showProposalLog(TypeConstants.MODIFY_MODE);     
                    }else{
                       CoeusOptionPane.showWarningDialog(coeusMessageResources.parseMessageKey(
                                    CANNOT_MODIFY_PROPOSAL_LOG));
                    }
                }
                
            }else if( source.equals(proposalLogBaseWindow.btnDisplay) || 
            source.equals(proposalLogBaseWindow.mnuItmDisplay) ){
                showProposalLog(TypeConstants.DISPLAY_MODE);
            }else if( source.equals(proposalLogBaseWindow.btnTempLog) || 
            source.equals(proposalLogBaseWindow.mnuItmTempLog) ){
                showProposalLog(TEMP_LOG);
            }else if( source.equals(proposalLogBaseWindow.btnMerge) || 
            source.equals(proposalLogBaseWindow.mnuItmMerge) ){
                //Modified for Case#3587 - multicampus enhancement  - Start
//                 performMerge();
                 selectedRow = proposalLogBaseWindow.tblResults.getSelectedRow();
                 String leadUnitNumber = EMPTY_STRING;
                 if(selectedRow > -1){
                     leadUnitNumber = (String)proposalLogBaseWindow.tblResults.getValueAt(selectedRow, UNIT_COLUMN);
                     boolean hasRight = checkUserHasRightInLeadUnit(leadUnitNumber,MODIFY_INST_PROPOSAL);
                     if(!hasRight){
                         hasRight = checkUserHasRightInLeadUnit(leadUnitNumber,CREATE_INST_PROPOSAL);
                     }
                     if(hasRight){
                         performMerge();
                     }else{
                       CoeusOptionPane.showWarningDialog(coeusMessageResources.parseMessageKey(
                                    CANNOT_MERGE_PROPOSAL_LOG));
                     }
                 }
                 //Case#3587 - End
                
            }else if( source.equals(proposalLogBaseWindow.btnSearch) || 
            source.equals(proposalLogBaseWindow.mnuItmSearch) ){
                showProposalLogSearch();
            }else if( source.equals(proposalLogBaseWindow.btnSaveAs) || 
            source.equals(proposalLogBaseWindow.mnuItmSaveAs) ){
                saveProposalLogList();
            }else if( source.equals(proposalLogBaseWindow.btnClose) || 
            source.equals(proposalLogBaseWindow.mnuItmClose) ){
                close();
            }else if( source.equals(proposalLogBaseWindow.mnuItmExit) ){
                exitApplication();
            }else if( source.equals(proposalLogBaseWindow.mnuItmChangePassword) ){
                showChangePassword();
            }else if(source.equals(proposalLogBaseWindow.mnuItmPreferences)){
                showPreference();
            //Added for Case#3682 - Enhancements related to Delegations -Start
             }else if(source.equals(proposalLogBaseWindow.mnuItmDelegations)){
                displayUserDelegation();
            //Added for Case#3682 - Enhancements related to Delegations -End
            }else if( source.equals(proposalLogBaseWindow.btnSortProposalLogs) || 
            source.equals(proposalLogBaseWindow.mnuItmSort) ){
                showSort();
            }//Case 2110 Start
            else if(source.equals(proposalLogBaseWindow.mnuItmCurrentLocks)){
                showLocksForm();
            }//Case 2110 End
            else{
                CoeusOptionPane.showInfoDialog(
                coeusMessageResources.parseMessageKey("funcNotImpl_exceptionCode.1100"));
            }
        }catch (CoeusException coeusException){
            coeusException.printStackTrace();
            CoeusOptionPane.showErrorDialog(coeusException.getMessage());
        }catch (Exception exception){
            exception.printStackTrace();
        }finally {
            blockEvents(false);
            mdiForm.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }
    
    //Added for Case#3682 - Enhancements related to Delegations -Start
    /*
     *Displays Delegations window
     */
    private void displayUserDelegation() {
        userDelegationForm = new UserDelegationForm(mdiForm,true);
        userDelegationForm.display();
    }
    //Added for Case#3682 - Enhancements related to Delegations - End
    
    // Added by surekha to implement the User Preference details
    private void showPreference(){
        if(userPreferencesForm == null) {
            userPreferencesForm = new UserPreferencesForm(mdiForm,true);
        }
        userPreferencesForm.loadUserPreferences(mdiForm.getUserId());
        userPreferencesForm.setUserName(mdiForm.getUserName());
        userPreferencesForm.display();
    }// End surekha
    
    // added by Nadh to implement sorting proposals start - 18-01-2004
    /*
     * this method shows the sort window
     * return void
     */
    private void showSort() {
        if(vecSortedData==null) {
            vecSortedData = new Vector();
        }
        SortForm sortForm = new SortForm(tblResultsTable==null?coeusSearch.getEmptyResTable():tblResultsTable,vecSortedData);
        Vector sortedData = sortForm.display();
        vecSortedData = (Vector)sortedData.get(1);
        if(((Integer)sortedData.get(0)).intValue() == OK_CLICKED)
            coeusSearch.sortByColumns(tblResultsTable==null?coeusSearch.getEmptyResTable():tblResultsTable,vecSortedData);
        else
            return;
    }// end Nadh
    
    // Added by Nadh to implement the change password
    private void showChangePassword(){
        if(changePassword == null) {
            changePassword = new ChangePassword();
        }
        changePassword.display();
    }// End Nadh
    
    //Case 2110 Start To get the Current Locks of the user
     private void showLocksForm() throws edu.mit.coeus.exception.CoeusException{
        CurrentLockForm currentLockForm = new CurrentLockForm(mdiForm,true);
        currentLockForm.display();
    }
    //Case 2110 End
    
    /**
     * Method used to close the application after confirmation.
     */
    public void exitApplication(){
        String message = coeusMessageResources.parseMessageKey(
                                    "toolBarFactory_exitConfirmCode.1149");
        int answer = CoeusOptionPane.showQuestionDialog(message,
                    CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_NO);
        if (answer == JOptionPane.YES_OPTION) {
            if( mdiForm.closeInternalFrames() ) {
                mdiForm.dispose();
            }
        }
    }
    
    /** Display the inbox details
     */
    private void showInboxDetails(){
        InboxDetailForm inboxDtlForm = null;
        try{
            if( ( inboxDtlForm = (InboxDetailForm)mdiForm.getFrame(
            "Inbox" ))!= null ){
                if( inboxDtlForm.isIcon() ){
                    inboxDtlForm.setIcon(false);
                }
                inboxDtlForm.setSelected( true );
                return;
            }
            inboxDtlForm = new InboxDetailForm(mdiForm);
            inboxDtlForm.setVisible(true);
        }catch(Exception exception){
            CoeusOptionPane.showInfoDialog(exception.getMessage());
        }
    }
    
    /** Show the Proposal log in the given mode
     * @param functionType the mode to open the Proposal Log
     */
    private void showProposalLog(char functionType){
        selectedRow = proposalLogBaseWindow.tblResults.getSelectedRow();
        if( (functionType != TypeConstants.ADD_MODE && functionType != TEMP_LOG) &&
        selectedRow == -1 ){
            return ;
        }
        if( functionType == TypeConstants.MODIFY_MODE ){
            String status = (String)proposalLogBaseWindow.tblResults.getValueAt(
                selectedRow, STATUS_COLUMN);
//            case 3568 start
//            if( status.equalsIgnoreCase(SUBMITTED_STATUS) ){
//                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
//                CANNOT_MODIFY_SUBMITTED_PROPOSAL));
//                return ;
//            
//            case 3568 end
        }
        String logNumber = EMPTY_STRING;
        if( functionType == TypeConstants.MODIFY_MODE || 
        functionType == TypeConstants.DISPLAY_MODE ){
            logNumber = (String)proposalLogBaseWindow.tblResults.getValueAt(
            selectedRow, NUMBER_COLUMN);
            //Modified for COEUSDEV-294 : Error adding activity to a negotiation - Start
            //When row count is 1, then the selected row is assigned as the basetable row
//            baseTableRow = Integer.parseInt((String)proposalLogBaseWindow.tblResults.getValueAt(
//            selectedRow, proposalLogBaseWindow.tblResults.getColumnCount()-1));
            if(selectedRow != -1 && proposalLogBaseWindow.tblResults.getRowCount() == 1 && selectedRow == 0){
                baseTableRow = selectedRow;
            }else if(  selectedRow != -1 ) {
                baseTableRow = Integer.parseInt((String)proposalLogBaseWindow.tblResults.getValueAt(
                        selectedRow, proposalLogBaseWindow.tblResults.getColumnCount()-1));
            }
            //COEUSDEV-294 : End
        }
            
        instituteProposalLog = new InstituteProposalLogDetailsController(logNumber, functionType);
        //Register the observer
        instituteProposalLog.registerObserver(this);
        // rdias UCSD - Coeus personalization impl
        AbstractController persnref = AbstractController.getPersonalizationControllerRef();
        persnref.customize_Form(instituteProposalLog.getControlledUI(), "GENERIC");
        // rdias UCSD
        instituteProposalLog.display();

    }
    
    /** To merge the proposal log with the selected institute proposal
     */
    private void performMerge() throws CoeusException{
        selectedRow = proposalLogBaseWindow.tblResults.getSelectedRow();
        if (selectedRow == -1 ) return ;
        String instPropNumber = null;
        String logNumber = (String)proposalLogBaseWindow.tblResults.getValueAt(
                selectedRow, NUMBER_COLUMN);
        String status = (String)proposalLogBaseWindow.tblResults.getValueAt(
                selectedRow, STATUS_COLUMN);
        
        //Proposal log status must be "Temporary"
        if( !status.equalsIgnoreCase(TEMPORARY_STATUS) ){
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
            LOG_STATUS_SHOULD_BE_TEMPORARY));
            return ;
        }
        
        //Proposal number should start with a character
        if( !(Character.isLetter( logNumber.charAt(NUMBER_COLUMN) )) ){
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
            TEMPORARY_PROPOSALS_START_WITH_CHAR));
            return ;
        }
        
        //Open the Institute Proposal Search screen
        try{
            InstituteProposalSearch instPropSearch = new InstituteProposalSearch(
                mdiForm, PROPOSAL_SEARCH, CoeusSearch.TWO_TABS);
            instPropSearch.showSearchWindow();
            HashMap hmSelectedProp = instPropSearch.getSelectedRow();
            if (hmSelectedProp != null && !hmSelectedProp.isEmpty() ) {
                instPropNumber = Utils.convertNull(hmSelectedProp.get(
                "PROPOSAL_NUMBER"));
            }else{
                return;
            }
        }catch (Exception exception){
            exception.printStackTrace();
        }
        
        //Merge the proposals
        RequesterBean requester = new RequesterBean();
        CoeusVector cvData = new CoeusVector();
        cvData.addElement(logNumber);
        cvData.addElement(instPropNumber);
        requester.setFunctionType(MERGE_PROPOSAL_LOG);
        
        requester.setDataObjects(cvData);
        AppletServletCommunicator comm = new AppletServletCommunicator(CONNECTION_STRING, requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response.isSuccessfulResponse()){
            proposalLogBaseWindow.tblResults.setValueAt((String)htProposalStatus.get("M"),
            selectedRow, STATUS_COLUMN);
            CoeusOptionPane.showInfoDialog("Temporary Proposal Log " + 
            logNumber + " merged with Institute proposal " + instPropNumber);
        }else {
            // Bug Fix #1920 -start
            throw new CoeusException(response.getMessage());
            //CoeusOptionPane.showInfoDialog(response.getMessage());
//            CoeusOptionPane.showInfoDialog("An error occurred in merging temporary log "+
//            logNumber + " with proposal " + instPropNumber);
            // Bug Fix #1920 - End
        }
    }
    
    /** To save the proposal log list
     */
    private void saveProposalLogList(){
        selectedRow = proposalLogBaseWindow.tblResults.getSelectedRow();
        //Code modified for PT ID#2382 - Save As functionality
        if( selectedRow == -1 ){
            //return;
        }
        SaveAsDialog saveAsDialog = new SaveAsDialog(proposalLogBaseWindow.tblResults);
    }
    
    /** This will catch the window closing event
     * @param propertyChangeEvent holds the propertyChangeEvent
     * @throws PropertyVetoException if exception occured
     */
    public void vetoableChange(java.beans.PropertyChangeEvent propertyChangeEvent) 
    throws java.beans.PropertyVetoException {
        
        if( closed ) return ;
        
        boolean changed = ((Boolean) propertyChangeEvent.getNewValue()).booleanValue();
        if(propertyChangeEvent.getPropertyName().equals(JInternalFrame.IS_CLOSED_PROPERTY) && changed) {
            close();
        }
    }
    
    /**
     * To reflect changes in the Proposal Log List
     * @param observable the observable object
     * @param arg the data required for updation
     */
    public void update(java.util.Observable observable, Object arg) {
        //Code Added for PT ID#3243 - start        
        String dateFormat = CoeusServerProperties.getProperty(CoeusPropertyKeys.SEARCH_DATE_FORMAT, SIMPLE_DATE_FORMAT); 
        //COEUSQA-1477 Dates in Search Results - Start
        String oldDateFormat = dateFormat;
        String parseDateFormat = "";
        if(!dateFormat.contains("HH")){
            parseDateFormat = dateFormat +" " +"HH:MI:SS AM";
            dateFormat = dateFormat +" " +"hh:mm:ss a";
        }else{
            parseDateFormat = oldDateFormat;
        }
        //COEUSQA-1477 Dates in Search Results - End
        //Code Added for PT ID#3243 - end
        //case 3466 start
        DateUtils dtUtils = new DateUtils();
        //case 3466 end
        if (arg instanceof InstituteProposalLogBean ){
            InstituteProposalLogBean logBean = (InstituteProposalLogBean)arg;
            String status = new Character(logBean.getLogStatus()).toString();
            String proposalStatus = (String)htProposalStatus.get(status);
            if( ((BaseWindowObservable)observable).getFunctionType() == TypeConstants.ADD_MODE ||
            ((BaseWindowObservable)observable).getFunctionType() == TEMP_LOG ){
                Vector vecTableRow = new Vector();
                vecTableRow.addElement(logBean.getProposalNumber());
                vecTableRow.addElement(logBean.getTitle());
                vecTableRow.addElement(logBean.getProposalTypeDescription());
                vecTableRow.addElement(logBean.getPrincipleInvestigatorName());
                vecTableRow.addElement(logBean.getLeadUnit());
                // Updating the row with the unitname
                vecTableRow.addElement(logBean.getUnitName());
                vecTableRow.addElement(logBean.getSponsorCode());
                vecTableRow.addElement(logBean.getSponsorName());
                
                vecTableRow.addElement(proposalStatus);
                vecTableRow.addElement(logBean.getComments());
                //case 3466 start
                if(logBean.getDeadlineDate() != null) {
                    //COEUSQA-1477 Dates in Search Results - Start
                    //vecTableRow.addElement(dtUtils.formatDate(logBean.getDeadlineDate().toString(),SIMPLE_DATE_FORMAT));
                    vecTableRow.addElement(dtUtils.parseDateForSearchResults(logBean.getDeadlineDate().toString(), oldDateFormat));
                    //COEUSQA-1477 Dates in Search Results - End
                }else {
                    vecTableRow.addElement(null);
                }
                //COEUSQA-1477 Dates in Search Results - Start
                //vecTableRow.addElement(CoeusDateFormat.format(logBean.getCreateTimestamp().toString(), dateFormat));
                vecTableRow.addElement(dtUtils.parseDateForSearchResults(logBean.getCreateTimestamp().toString(), oldDateFormat));
                //COEUSQA-1477 Dates in Search Results - End
                //case 3466 end
                //Code modified for PT ID#3243 - start 
                //COEUSQA-1477 Dates in Search Results - Start
                //vecTableRow.addElement(CoeusDateFormat.format(logBean.getUpdateTimestamp().toString(), dateFormat));
                vecTableRow.addElement(dtUtils.formatTimeForSearchResults(logBean.getUpdateTimestamp().toString(), parseDateFormat));
                //COEUSQA-1477 Dates in Search Results - End
                //Code modified for PT ID#3243 - end
               //case 3466 start
                vecTableRow.addElement(logBean.getCreateUser());
                //case 3466 end
                vecTableRow.addElement(logBean.getUpdateUser());
                int lastRow = proposalLogBaseWindow.tblResults.getRowCount();
                vecTableRow.addElement( EMPTY_STRING + lastRow);
                ((DefaultTableModel)proposalLogBaseWindow.tblResults.getModel()).insertRow(lastRow, vecTableRow);
                if( lastRow == 0 ) {
                    proposalLogBaseWindow.tblResults.setRowSelectionInterval(0,0);
                }
                baseTableRow = lastRow;
                proposalLogBaseWindow.tblResults.scrollRectToVisible(
                    proposalLogBaseWindow.tblResults.getCellRect(baseTableRow, NUMBER_COLUMN, true));
            }else{
                ((DefaultTableModel)proposalLogBaseWindow.tblResults.getModel()).setValueAt(
                    logBean.getTitle(), baseTableRow, TITLE_COLUMN);
                ((DefaultTableModel)proposalLogBaseWindow.tblResults.getModel()).setValueAt(
                    logBean.getProposalTypeDescription(), baseTableRow, PROP_TYPE_COLUMN);
                ((DefaultTableModel)proposalLogBaseWindow.tblResults.getModel()).setValueAt(
                    logBean.getPrincipleInvestigatorName(), baseTableRow, PI_COLUMN);
                ((DefaultTableModel)proposalLogBaseWindow.tblResults.getModel()).setValueAt(
                    logBean.getLeadUnit(), baseTableRow, UNIT_COLUMN);
                ((DefaultTableModel)proposalLogBaseWindow.tblResults.getModel()).setValueAt(
                    logBean.getUnitName(), baseTableRow, UNIT_COLUMN_NAME);
                ((DefaultTableModel)proposalLogBaseWindow.tblResults.getModel()).setValueAt(
                    logBean.getSponsorCode(), baseTableRow, SPONSOR_CODE_COLUMN);
                ((DefaultTableModel)proposalLogBaseWindow.tblResults.getModel()).setValueAt(
                    logBean.getSponsorName(), baseTableRow, SPONSOR_NAME_COLUMN);
                ((DefaultTableModel)proposalLogBaseWindow.tblResults.getModel()).setValueAt(
                    proposalStatus, baseTableRow, STATUS_COLUMN);
                ((DefaultTableModel)proposalLogBaseWindow.tblResults.getModel()).setValueAt(
                    logBean.getComments(), baseTableRow, COMMENTS_COLUMN);
                //case 3466 start
                if (logBean.getDeadlineDate() != null) {
                    //COEUSQA-1477 Dates in Search Results - Start
                    //((DefaultTableModel)proposalLogBaseWindow.tblResults.getModel()).setValueAt(
                    //dtUtils.formatDate(logBean.getDeadlineDate().toString(), SIMPLE_DATE_FORMAT), baseTableRow, DEADLINE_COLUMN);
                    ((DefaultTableModel)proposalLogBaseWindow.tblResults.getModel()).setValueAt(
                    dtUtils.parseDateForSearchResults(logBean.getDeadlineDate().toString(), oldDateFormat), baseTableRow, DEADLINE_COLUMN);
                    //COEUSQA-1477 Dates in Search Results - End
                } else{
                    ((DefaultTableModel)proposalLogBaseWindow.tblResults.getModel()).setValueAt(null,baseTableRow, DEADLINE_COLUMN);
                }
                //COEUSQA-1477 Dates in Search Results - Start
                //((DefaultTableModel)proposalLogBaseWindow.tblResults.getModel()).setValueAt(
                //    CoeusDateFormat.format(logBean.getCreateTimestamp().toString(), dateFormat), baseTableRow, CREATETIMESTAMP_COLUMN);
                ((DefaultTableModel)proposalLogBaseWindow.tblResults.getModel()).setValueAt(
                    dtUtils.parseDateForSearchResults(logBean.getCreateTimestamp().toString(), oldDateFormat), baseTableRow, CREATETIMESTAMP_COLUMN);
                //COEUSQA-1477 Dates in Search Results - End
                //case 3466 end
                //Code modified for PT ID#3242
                //COEUSQA-1477 Dates in Search Results - Start
                //((DefaultTableModel)proposalLogBaseWindow.tblResults.getModel()).setValueAt(
                //    CoeusDateFormat.format(logBean.getUpdateTimestamp().toString(), dateFormat), baseTableRow, TIMESTAMP_COLUMN);
                ((DefaultTableModel)proposalLogBaseWindow.tblResults.getModel()).setValueAt(
                    dtUtils.formatTimeForSearchResults(logBean.getUpdateTimestamp().toString(), parseDateFormat), baseTableRow, TIMESTAMP_COLUMN);
                //COEUSQA-1477 Dates in Search Results - End
                //case 3466 start
                ((DefaultTableModel)proposalLogBaseWindow.tblResults.getModel()).setValueAt(
                    logBean.getCreateUser(), baseTableRow, CREATE_USER_COLUMN);
                //case 3466 end
                ((DefaultTableModel)proposalLogBaseWindow.tblResults.getModel()).setValueAt(
                    logBean.getUpdateUser(), baseTableRow, UPDATE_USER_COLUMN);
                int selRow = proposalLogBaseWindow.tblResults.getSelectedRow();
                if(  selRow != -1 ) {
                    baseTableRow = Integer.parseInt((String)proposalLogBaseWindow.tblResults.getValueAt(
                    selRow,proposalLogBaseWindow.tblResults.getColumnCount()-1));
                }
            }            
        }
    }
    
    /**
     * Getter for property status.
     * @return Value of property status.
     */
    public int getStatus() {
        return status;
    }
    
    /**
     * Setter for property status.
     * @param status New value of property status.
     */
    public void setStatus(int status) {
        this.status = status;
    }
    
    //Inner Class Mouse Adapter - START
    class ProposalLogMouseAdapter extends MouseAdapter{
        public void mouseClicked(MouseEvent mouseEvent){
            
            //Added by Nadh to get the column header and its status Start 18-01-2005
            if(mouseEvent.getSource() instanceof JTableHeader) {
                JTableHeader tblHeader = (JTableHeader) mouseEvent.getSource();
                TableColumnModel columnModel = tblHeader.getColumnModel();
                int viewColumn = columnModel.getColumnIndexAtX(mouseEvent.getX());
                int column = columnModel.getColumn(viewColumn).getModelIndex();
                int sortStatus = getStatus();
                if(oldCol != column )
                    sortStatus = MultipleTableColumnSorter.NOT_SORTED;
                sortStatus = sortStatus + (mouseEvent.isShiftDown() ? -1 : 1);
                sortStatus = (sortStatus + 4) % 3 - 1;
                setStatus(sortStatus);
                oldCol = column;
                if(getStatus()==MultipleTableColumnSorter.ASCENDING || getStatus() == MultipleTableColumnSorter.DESCENDING) {
                    Vector newSortedData = new Vector();
                    newSortedData.addElement(tblResultsTable.getColumnName(column));
                    newSortedData.addElement(new Integer(column));
                    newSortedData.addElement(new Boolean(status == 1 ? true : false));
                    if(vecSortedData == null)
                        vecSortedData = new Vector();
                    vecSortedData.removeAllElements();
                    vecSortedData.addElement(newSortedData);
                }else {
                    vecSortedData = null;
                }
            }//End Nadh
            if(mouseEvent.getSource() instanceof JTable) {
                if( mouseEvent.getClickCount() == 2 &&
                proposalLogBaseWindow.mnuItmDisplay.isEnabled()){
                    //Display Proposal Log only if user has view rights
                    showProposalLog(TypeConstants.DISPLAY_MODE);
                }
            }
        }
    }
    //Inner Class Mouse Adapter - END
    
}
