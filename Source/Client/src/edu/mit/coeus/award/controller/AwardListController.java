/*
 * AwardBaseWindowController.java
 *
 * Created on March 16, 2004, 11:43 AM
 */
/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * @author  jobinelias
 */

/*
 * PMD check performed, and commented unused imports and variables on 24-AUG-2011
 * by Bharati
 */

package edu.mit.coeus.award.controller;

/**
 *
 * @author  sharathk
 */

import edu.mit.coeus.user.gui.UserDelegationForm;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.beans.*;

import edu.mit.coeus.award.gui.*;
import edu.mit.coeus.award.bean.*;
import edu.mit.coeus.exception.*;
import edu.mit.coeus.search.gui.CoeusSearch;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.gui.*;
import edu.mit.coeus.propdev.gui.InboxDetailForm;
import edu.mit.coeus.utils.saveas.*;
import edu.mit.coeus.brokers.*;
import edu.mit.coeus.bean.AuthorizationBean;
import edu.mit.coeus.utils.query.AuthorizationOperator;
//import edu.mit.coeus.subcontract.bean.SubContractFundingSourceBean;
import edu.mit.coeus.propdev.bean.ProposalAwardHierarchyLinkBean;
import edu.mit.coeus.propdev.gui.MedusaDetailForm;
//import edu.mit.coeus.propdev.gui.ProposalNotepad;
import edu.mit.coeus.propdev.gui.ProposalNotepadForm;
//import edu.mit.coeus.propdev.gui.ProposalAwardHierarchyForm;
import edu.mit.coeus.gui.event.*;
import edu.mit.coeus.bean.BaseBean;
import edu.mit.coeus.search.bean.DisplayBean;
import edu.mit.coeus.utils.ChangePassword;
import edu.mit.coeus.user.gui.UserPreferencesForm;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

// JM 12-19-2012 properties for column indexes
import edu.vanderbilt.coeus.gui.SearchResultColumnIndex;
// JM END

/** Controller for Award List. */
public class AwardListController extends AwardController implements ActionListener,
VetoableChangeListener, MouseListener, BeanUpdatedListener{
    
    /** holds Award List instance to be controlled. */
    private AwardList awardList;
    
    /** Coeus Serach instance to search Awards. */
    private CoeusSearch coeusSearch;
    
    /** Holds CoeusMessageResources instance used for reading message Properties.
     */
    private CoeusMessageResources coeusMessageResources = CoeusMessageResources.getInstance();
    
    //private QueryEngine queryEngine = QueryEngine.getInstance();;
    
    private CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();
    
    private JTable tblResultsTable;
    
    private static final String AWARD_SEARCH = "awardSearch";
    
    /** An award sheet is already open for modification. */
    //private static final String AWARD_SHEET_ALREADY_OPEN = "award_exceptionCode.1006";
    
    //private static final char SAVE_AWARD = 'G';
    //private static final char GET_AWARD_DETAILS = 'C';
    
    //Moved To Award Controller
    //private static final char HAS_REPORTING_REQUIREMENT = 'E';
    //private static final char GENERATE_REPORTING_REQUIREMENTS = 'B';
    //private static final char IS_AWARD_LOCKED = 'I';
    
    private static final String SERVLET = "/AwardMaintenanceServlet";
    
    private static final String AUTH_SERVLET = "/AuthorizationServlet";
    
    //Moved to Award Controller
    //private static final String REP_REQ_SERVLET = "/AwardReportReqMaintenanceServlet";
    
    private static int AWARD_COLUMN = -1;
    
    private static final String FUNC_NOT_IMPL = "Functionality not implemented";
    
    private static final String NO_MORE_AWARDS_TO_DISPLAY = "There are no more awards to display";
    
    // Modified for case# 2800 - Award Upload Attachments - Start
    private boolean createAward, modifyAward, viewAward, maintainReporting, modifySubcontract , viewSubcontract, viewDocuments,maintainDocuments;
    // Added for case# 2800 - Award Upload Attachments - End
    
    private ChangePassword changePassword;
    
    private UserPreferencesForm userPreferencesForm;
    //Added for Case#3682 - Enhancements related to Delegations - Start
    private UserDelegationForm userDelegationForm;
    //Added for Case#3682 - Enhancements related to Delegations - End
    //holds sorted columns and its states
    private Vector vecSortedData;
    
    private static final int OK_CLICKED = 0;
    
    //holds column index
    private int oldCol = -1;
    
    //COEUSQA-3273 : Inactive Cost Element in use Error Alert required for Proposal and Award Budgets - start
    private static final String AWARD_BUDGET_SERVLET = "/AwardBudgetMaintainanceServlet";
    private static final String connectTo = CoeusGuiConstants.CONNECTION_URL +
                                                        AWARD_BUDGET_SERVLET;
    private static final char GET_INACTIVE_COST_ELEMENT_DETAILS = 'k';
    private static final String BUDGET_HAVE_INACTIVE_COST_ELEMENTS = "budgetSelect_exceptionCode.1061";
    //COEUSQA-3273 : Inactive Cost Element in use Error Alert required for Proposal and Award Budgets - end
    
    //holds column status
    private int status=MultipleTableColumnSorter.NOT_SORTED;
    
    // JM 12-19-2012 variable for unit number in Award List; will replace hard-coded value
    // since we have reordered our fields
    private static int UNIT_NUMBER_COL;
    // JM END
    
    /**
     *Reporting Requirement information is not available for the award 
     */
    //private static final String REP_REQ_NOT_AVAILABLE = "repRequirements_exceptionCode.1054";
    // 3587 Multi Campus Enhancements
    private static final char CHECK_USER_HAS_MODIFY_RIGHT = '$';
    /** Creates a new instance of AwardBaseWindowController */
    public AwardListController() {
        initComponents();
        authorizationCheck();
        registerComponents();
        
        // JM 12-19-2012 properties for column indexes
        UNIT_NUMBER_COL = SearchResultColumnIndex.getSearchResultColumnIndex("awardList_leadUnitNumber");
        // JM END
        
    }
    
    private void initComponents() {
        try{
            awardList = new AwardList("Award List", mdiForm);
            coeusSearch = new CoeusSearch(mdiForm, AWARD_SEARCH, 0);
            JTable tblResults = coeusSearch.getEmptyResTable();
            awardList.initComponents(tblResults);
        }catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    
    private void authorizationCheck() {
        RequesterBean requester;
        ResponderBean responder;
        
        requester = new RequesterBean();
        Hashtable authorizations = new Hashtable();
        
        AuthorizationBean authorizationBean;
        AuthorizationOperator authorizationOperator;
        
        String CREATE_AWARD, MODIFY_AWARD, VIEW_AWARD, MAINTAIN_REPORTING ,MODIFY_SUBCONTRACT , VIEW_SUBCONTRACT;
        CREATE_AWARD = "CREATE_AWARD";
        MODIFY_AWARD = "MODIFY_AWARD";
        VIEW_AWARD = "VIEW_AWARD";
        MAINTAIN_REPORTING = "MAINTAIN_REPORTING";
        MODIFY_SUBCONTRACT = "MODIFY_SUBCONTRACT";
        VIEW_SUBCONTRACT = "VIEW_SUBCONTRACT";
        // Added for case# 2800 - Award Upload Attachments - Start
        String VIEW_AWARD_DOCUMENTS = "VIEW_AWARD_DOCUMENTS";
        String MAINTAIN_AWARD_DOCUMENTS = "MAINTAIN_AWARD_DOCUMENTS";
        // Added for case# 2800 - Award Upload Attachments - End
        
        // Determine whether user has right to create an award
        authorizationBean = new AuthorizationBean();
        authorizationBean.setFunction(CREATE_AWARD);
        // 3587: Multi Campus Enahncements - Start
//        authorizationBean.setFunctionType("OSP");
        authorizationBean.setFunctionType("RIGHT_ID");
        // 3587: Multi Campus Enahncements - End
        authorizationBean.setPerson(CoeusGuiConstants.getMDIForm().getUserId());
        authorizationOperator = new AuthorizationOperator(authorizationBean);
        authorizations.put(CREATE_AWARD, authorizationOperator);
        
        // Determine whether user has right to modify an award
        authorizationBean = new AuthorizationBean();
        authorizationBean.setFunction(MODIFY_AWARD);
        // 3587: Multi Campus Enahncements - Start
//        authorizationBean.setFunctionType("OSP");
        authorizationBean.setFunctionType("RIGHT_ID");
        // 3587: Multi Campus Enahncements - End
        authorizationBean.setPerson(CoeusGuiConstants.getMDIForm().getUserId());
        authorizationOperator = new AuthorizationOperator(authorizationBean);
        authorizations.put(MODIFY_AWARD, authorizationOperator);
        
        // Determine whether user has right to display an Award
        authorizationBean = new AuthorizationBean();
        authorizationBean.setFunction(VIEW_AWARD);
        authorizationBean.setFunctionType("RIGHT_ID");
        authorizationBean.setPerson(CoeusGuiConstants.getMDIForm().getUserId());
        authorizationOperator = new AuthorizationOperator(authorizationBean);
        authorizations.put(VIEW_AWARD, authorizationOperator);
        
        authorizationBean = new AuthorizationBean();
        authorizationBean.setFunction(MAINTAIN_REPORTING);
        // 3587: multi Campus Enahncements - Start
//        authorizationBean.setFunctionType("OSP");
        authorizationBean.setFunctionType("RIGHT_ID");
        // 3587: multi Campus Enahncements - End
        authorizationBean.setPerson(CoeusGuiConstants.getMDIForm().getUserId());
        authorizationOperator = new AuthorizationOperator(authorizationBean);
        authorizations.put(MAINTAIN_REPORTING, authorizationOperator);
        
        // Determine whether user has right to modify an subcontract
        authorizationBean = new AuthorizationBean();
        authorizationBean.setFunction(MODIFY_SUBCONTRACT);
        // 3587: Multi Campus Enahncements - Start
//        authorizationBean.setFunctionType("OSP");
        authorizationBean.setFunctionType("RIGHT_ID");
        // 3587: Multi Campus Enahncements - End
        authorizationBean.setPerson(CoeusGuiConstants.getMDIForm().getUserId());
        authorizationOperator = new AuthorizationOperator(authorizationBean);
        authorizations.put(MODIFY_SUBCONTRACT, authorizationOperator);
        
        // Determine whether user has right to display an subcontract
        authorizationBean = new AuthorizationBean();
        authorizationBean.setFunction(VIEW_SUBCONTRACT);
        authorizationBean.setFunctionType("RIGHT_ID");
        authorizationBean.setPerson(CoeusGuiConstants.getMDIForm().getUserId());
        authorizationOperator = new AuthorizationOperator(authorizationBean);
        authorizations.put(VIEW_SUBCONTRACT, authorizationOperator);
        
        // Added for case# 2800 - Award Upload Attachments - Start
        authorizationBean = new AuthorizationBean();
        authorizationBean.setFunction(MAINTAIN_AWARD_DOCUMENTS);
        authorizationBean.setFunctionType("RIGHT_ID");
        authorizationBean.setPerson(CoeusGuiConstants.getMDIForm().getUserId());
        authorizationOperator = new AuthorizationOperator(authorizationBean);
        authorizations.put(MAINTAIN_AWARD_DOCUMENTS, authorizationOperator);
        
        authorizationBean = new AuthorizationBean();
        authorizationBean.setFunction(VIEW_AWARD_DOCUMENTS);
        authorizationBean.setFunctionType("RIGHT_ID");
        authorizationBean.setPerson(CoeusGuiConstants.getMDIForm().getUserId());
        authorizationOperator = new AuthorizationOperator(authorizationBean);
        authorizations.put(VIEW_AWARD_DOCUMENTS, authorizationOperator);
                
        // Added for case# 2800 - Award Upload Attachments - End
        
        requester.setAuthorizationOperators(authorizations);
        requester.setIsAuthorizationRequired(true);
        
        AppletServletCommunicator comm = new AppletServletCommunicator(CoeusGuiConstants.CONNECTION_URL + AUTH_SERVLET, requester);
        
        comm.send();
        responder = comm.getResponse();
        if(responder.isSuccessfulResponse()){
            authorizations = responder.getAuthorizationOperators();
        }else{
            CoeusOptionPane.showInfoDialog(responder.getMessage());
        }
        
        createAward = ((Boolean)authorizations.get(CREATE_AWARD)).booleanValue();
        modifyAward = ((Boolean)authorizations.get(MODIFY_AWARD)).booleanValue();
        viewAward = ((Boolean)authorizations.get(VIEW_AWARD)).booleanValue();
        maintainReporting = ((Boolean)authorizations.get(MAINTAIN_REPORTING)).booleanValue();
        modifySubcontract = ((Boolean)authorizations.get(MODIFY_SUBCONTRACT)).booleanValue();
        viewSubcontract = ((Boolean)authorizations.get(VIEW_SUBCONTRACT)).booleanValue();
        // Added for case# 2800 - Award Upload Attachments - Start
        viewDocuments = ((Boolean)authorizations.get(VIEW_AWARD_DOCUMENTS)).booleanValue();
        maintainDocuments = ((Boolean)authorizations.get(MAINTAIN_AWARD_DOCUMENTS)).booleanValue();
        // Added for case# 2800 - Award Upload Attachments - End
        /*
        System.out.println("CREATE_AWARD" + createAward);
        System.out.println("MODIFY_AWARD" + modifyAward);
        System.out.println("VIEW_AWARD" + viewAward);
        System.out.println("MAINTAIN_REPORTING" + maintainReporting);
        */
        
        awardList.mnuItmNewAward.setEnabled(createAward);
        awardList.btnCreateNewAward.setEnabled(createAward);
//        awardList.btnAwardNewEntry.setEnabled(createAward);
        
        awardList.mnuItmCorrectAward.setEnabled(modifyAward);
        awardList.btnCorrectAward.setEnabled(modifyAward);
        
        //Code commented for Case#3388 - Implementing authorization check at department level
//        awardList.mnuItmDisplayAward.setEnabled(modifyAward);
        
//        awardList.btnCreateNewAward.setEnabled(modifyAward);
        awardList.mnuItmNewEntry.setEnabled(modifyAward);
        awardList.btnAwardNewEntry.setEnabled(modifyAward);
        
        //Code commented for Case#3388 - Implementing authorization check at department level
//        awardList.mnuItmDisplayAward.setEnabled(viewAward);
//        awardList.btnDisplayAward.setEnabled(viewAward);
        
        awardList.mnuItmGenReportingReq.setEnabled(maintainReporting);
        
        
    }
    
    public boolean viewSubcontract(){
        authorizationCheck();
        if(modifySubcontract || viewSubcontract){
            return true;
        }else{
            return false;
        }
    }
    
    // Added for case# 2800 - Award Upload Attachments - Start
    public Hashtable getAwardDocumentRights(){
        Hashtable htAwardDocRights = new Hashtable();
        authorizationCheck();
        htAwardDocRights.put("AwardModify",new Boolean(modifyAward));
        htAwardDocRights.put("AwardView",new Boolean(viewAward));
        htAwardDocRights.put("AwardMaintainDoc",new Boolean(maintainDocuments));
        htAwardDocRights.put("AwardViewDoc",new Boolean(viewDocuments));
        return htAwardDocRights;
    }
    // Added for case# 2800 - Award Upload Attachments - End
    
    /** displays Award Search Window. */
    private void showAwardSearch() {
        try {
            //Case 2451 start
            vecSortedData = new Vector();
            //Case 2451 end
            coeusSearch.showSearchWindow();
            tblResultsTable = coeusSearch.getSearchResTable();
            
            //Bug Fix - MIT_AWARD_NUMBER position START
            if(AWARD_COLUMN == -1) {
                Hashtable htSelRow = coeusSearch.getSearchResults();
                
                //Bug Fix if ESC Pressed it'll be null.
                if(htSelRow == null) return ;
                
                Vector vecDisplay = (Vector)htSelRow.get("displaylabels");
                DisplayBean displayBean;
                for(int index = 0; index < vecDisplay.size(); index++){
                    displayBean = (DisplayBean)vecDisplay.get(index);
                    if(displayBean.getName() != null && displayBean.getName().equals("MIT_AWARD_NUMBER")) {
                        AWARD_COLUMN = index;
                    }
                }
            }//Award Column -1 not initialized
            //Bug Fix - MIT_AWARD_NUMBER position END
            
            awardList.displayResults(tblResultsTable);
            
            //adding listener for table.
            if(tblResultsTable != null) {
                awardList.tblResults.addMouseListener(this);
                awardList.tblResults.getTableHeader().addMouseListener(this);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /** Displays Award List. */
    public void display() {
        try{
            mdiForm.putFrame(CoeusGuiConstants.AWARD_FRAME_TITLE, awardList);
            mdiForm.getDeskTopPane().add(awardList);
            awardList.setSelected(true);
            awardList.setVisible(true);
            showAwardSearch();
        }catch (PropertyVetoException propertyVetoException) {
            propertyVetoException.printStackTrace();
        }
    }
    
    public void formatFields() {
    }
    
    /** returns the component being controlled.
     * @return component being controlled.
 */
    public Component getControlledUI() {
        return awardList;
    }
    
    public Object getFormData() {
        return null;
    }
    
    /** registers Components with listeners. */
    public void registerComponents() {
        awardList.addVetoableChangeListener(this);
        
        //Adding Listeners for ToolBar Buttons
        awardList.btnCreateNewAward.addActionListener(this);
        awardList.btnCorrectAward.addActionListener(this);
        awardList.btnDisplayAward.addActionListener(this);
        awardList.btnAwardNewEntry.addActionListener(this);
        awardList.btnAwardHierarchy.addActionListener(this);
        awardList.btnNotepad.addActionListener(this);
        awardList.btnReportingRequirements.addActionListener(this);
        awardList.btnMedusa.addActionListener(this);
        awardList.btnSortAwards.addActionListener(this);
        awardList.btnAwardSummary.addActionListener(this);
        awardList.btnSearch.addActionListener(this);
        awardList.btnSaveAs.addActionListener(this);
        awardList.btnClose.addActionListener(this);
        awardList.mnuItmMedusa.addActionListener(this);
        
        //Adding Listeners for File Menu
        awardList.mnuItmInbox.addActionListener(this);
        awardList.mnuItmSaveAs.addActionListener(this);
        
        //Commented since we are not using it in Coeus 4.0
        //awardList.mnuItmPrintSetup.addActionListener(this);   
        
        awardList.mnuItmSort.addActionListener(this);
        awardList.mnuItmSummary.addActionListener(this);
        awardList.mnuItmClose.addActionListener(this);
        awardList.mnuItmChangePassword.addActionListener(this);
        //Added for Case#3682 - Enhancements related to Delegations - Start
        awardList.mnuItmDelegations.addActionListener(this);
        //Added for Case#3682 - Enhancements related to Delegations - End
        awardList.mnuItmPreferences.addActionListener(this);
        awardList.mnuItmExit.addActionListener(this);
        //Case 2110 Start
        awardList.mnuItmCurrentLocks.addActionListener(this);
        //Case 2110 End
        
        //Adding Listeners for Edit Menu
        awardList.mnuItmNewAward.addActionListener(this);
        awardList.mnuItmCorrectAward.addActionListener(this);
        awardList.mnuItmDisplayAward.addActionListener(this);
        awardList.mnuItmNewEntry.addActionListener(this);
        awardList.mnuItmAwardHierarchy.addActionListener(this);
        awardList.mnuItmNotepad.addActionListener(this);
        awardList.mnuItmReportingReq.addActionListener(this);
        awardList.mnuItmGenReportingReq.addActionListener(this);
        
        //Adding Listeners for Tools Menu
        awardList.mnuItmSearch.addActionListener(this);
        
        //Mouse listener for tbl results is moved to showAwardSearch.
        //since every time it gets a new instance of table
        
        addBeanUpdatedListener(this, AwardBean.class);
        
    }
    
    public void saveFormData() {
    }
    
    public void setFormData(Object data) {
    }
    
    public boolean validate() throws CoeusUIException {
        return false;
    }
    
    /** listens to action performed events.
     * @param actionEvent ActionEvent object.
     */
    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
                
        //Bug Fix : Hour glass implementation - Step 1 - START
        try{
        blockEvents(true);
        mdiForm.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        //Bug Fix : Hour glass implementation - Step 1 - END  
        
        if(source.equals(awardList.mnuItmInbox)) {
            showInboxDetails();
        }else if(source.equals(awardList.mnuItmClose) || source.equals(awardList.btnClose)) {
            close();
        }else if(source.equals(awardList.mnuItmSearch) || source.equals(awardList.btnSearch)) {
            showAwardSearch();
        }else if(source.equals(awardList.mnuItmAwardHierarchy) || source.equals(awardList.btnAwardHierarchy)) {
            showAwardHierarchy();
        }else if(source.equals(awardList.mnuItmNewAward) || source.equals(awardList.btnCreateNewAward)) {
            showNewAward();
        }else if(source.equals(awardList.mnuItmCorrectAward) || source.equals(awardList.btnCorrectAward)) {
            correctAward();
        }else if(source.equals(awardList.mnuItmNewEntry) || source.equals(awardList.btnAwardNewEntry)) {
            newEntry();
        }else if(source.equals(awardList.mnuItmDisplayAward) || source.equals(awardList.btnDisplayAward)) {
            displayAward();
        }else if(source.equals(awardList.mnuItmSaveAs) || source.equals(awardList.btnSaveAs)) {
            saveAwardList();
        }else if(source.equals(awardList.mnuItmSummary) || source.equals(awardList.btnAwardSummary)) {
            showAwardSummary();
        }else if(source.equals(awardList.mnuItmMedusa) || source.equals(awardList.btnMedusa)){
            showMedusaWindow();
        }else if(source.equals(awardList.mnuItmNotepad) || source.equals(awardList.btnNotepad)) {
            showNotepad();
        }else if(source.equals(awardList.mnuItmReportingReq) || source.equals(awardList.btnReportingRequirements)) {
            showReportingRequirements();
        }else if(source.equals(awardList.mnuItmGenReportingReq)) {
            generateReportingRequirements();
        }else if(source.equals(awardList.mnuItmChangePassword)) {
            showChangePassword();
        }else if(source.equals(awardList.mnuItmPreferences)) {
            showPreference();
        //Added for Case#3682 - Enhancements related to Delegations - Start
        }else if(source.equals(awardList.mnuItmDelegations)) {
            displayUserDelegation();
        //Added for Case#3682 - Enhancements related to Delegations - End
        }else if(source.equals(awardList.mnuItmSort) || source.equals(awardList.btnSortAwards)) {
            showSort();
            // start of bug fix id 1651
        }else if(source.equals(awardList.mnuItmExit)) {
            exitApplication();
        }//end of bug fix id 1651
        //Case 2110 Start
        else if(source.equals(awardList.mnuItmCurrentLocks)){
            showLocksForm();
        }//Case 2110 End
        else {
            CoeusOptionPane.showInfoDialog(FUNC_NOT_IMPL);
        }    
        
        }//Case 2110 Start
        catch(CoeusException coeusException){
            coeusException.printStackTrace();
            CoeusOptionPane.showErrorDialog(coeusException.getMessage());
        }catch(Exception exception){
            exception.printStackTrace();
            CoeusOptionPane.showErrorDialog(exception.getMessage());
        }//Case 2110 End
        
        //Bug Fix : Hour glass implementation - Step 2 - START
        finally {
            blockEvents(false);
            mdiForm.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
        //Bug Fix : Hour glass implementation - Step 2 - END
        
    }
    
    //Added by shiji for bug fix id 1651
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
    }//End bug fix id 1651
    
    //Added for Case#3682 - Enhancements related to Delegations - Start
    /*
     * Display Delegations window
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
        SortForm sortForm = new SortForm(awardList.tblResults,vecSortedData);
        Vector sortedData = sortForm.display();
        vecSortedData = (Vector)sortedData.get(1);
        if(((Integer)sortedData.get(0)).intValue() == OK_CLICKED)
            coeusSearch.sortByColumns(awardList.tblResults,vecSortedData);
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
    
    //Case 2110 Start To get the current locks of the user
    private void showLocksForm() throws edu.mit.coeus.exception.CoeusException{
        CurrentLockForm currentLockForm = new CurrentLockForm(mdiForm,true);
        currentLockForm.display();
    }    
    //Case 2110 End
    
    /** To display the notepad window if not already open */
    private void showNotepad(){
        //Check if Notepad is already opened
        CoeusInternalFrame frame = mdiForm.getFrame(CoeusGuiConstants.NOTEPAD_FRAME_TITLE);
        if(frame == null){
            int selectedRow = awardList.tblResults.getSelectedRow();
            if( selectedRow == -1 ) return ;
            String awardNumber = awardList.tblResults.getValueAt(selectedRow, AWARD_COLUMN).toString();

            ProposalAwardHierarchyLinkBean linkBean = new ProposalAwardHierarchyLinkBean();
            linkBean.setAwardNumber(awardNumber);
            linkBean.setBaseType(CoeusConstants.AWARD);

            ProposalNotepadForm proposalNotepadForm = new ProposalNotepadForm(linkBean, mdiForm);
            proposalNotepadForm.display();

        }else{
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
            "proposal_Notepad_exceptionCode.7116"));
        }
    }
    
    /** To View Award Summary */
    private void showAwardSummary(){
        int selectedRow = awardList.tblResults.getSelectedRow();
        if( selectedRow == -1 ) return ;
        AwardSummaryController awardSummaryController = new AwardSummaryController(awardList.tblResults);
        awardSummaryController.display();
    }
    
    /** Display the Award Hierarchy window */
    private void showAwardHierarchy(){
        int selectedRow = awardList.tblResults.getSelectedRow();
        if( selectedRow == -1 ) return ;
        String mitAwardNumber = awardList.tblResults.getValueAt(selectedRow, AWARD_COLUMN).toString();
        AwardHierarchyController awardHierarchyController = new AwardHierarchyController(mitAwardNumber);
        awardHierarchyController.display();
    }
    
    /** Display New Award */
    private void showNewAward() {
        AwardBaseWindow awardBaseWindow = null;
        
        if(isAwardWindowOpen(EMPTY, CORRECT_AWARD)) {
          return ;
        }
        
        //System.out.println(NEW_AWARD);
       // AwardBaseWindowController awardBaseWindowController = new AwardBaseWindowController("New Award : ", NEW_AWARD, null,false);
        AwardBaseWindowController awardBaseWindowController = new AwardBaseWindowController("New Award : ", NEW_AWARD, null);
        awardBaseWindowController.setMaintainReporting(maintainReporting);
        awardBaseWindowController.display();
    }
    
    private void correctAward() {
        int selectedRow = awardList.tblResults.getSelectedRow();
        if(selectedRow == -1)return ;
        
        String awardNumber = awardList.tblResults.getValueAt(selectedRow, AWARD_COLUMN).toString();
        
        if(isAwardWindowOpen(awardNumber, CORRECT_AWARD)) {
          return ;
        }
        // 3587: Multi Campus Enahncements - Start
        boolean hasModifyRight = checkUserHasModifyRight(awardNumber);
        if(!hasModifyRight){
            CoeusOptionPane.showErrorDialog("You do not have the right to modify this Award. ");
            return ;
        }
        // 3587: Multi Campus Enahncements- End
        AwardBean awardBean = new AwardBean();
        awardBean.setMitAwardNumber(awardNumber);
        
        AwardBaseWindowController awardBaseWindowController=new AwardBaseWindowController("Correct Award : ", CORRECT_AWARD , awardBean);
        awardBaseWindowController.setMaintainReporting(maintainReporting);
        awardBaseWindowController.display();
    }
    
    private void newEntry() {
        int selectedRow = awardList.tblResults.getSelectedRow();
        if(selectedRow == -1)return ;
        
        String awardNumber = awardList.tblResults.getValueAt(selectedRow, AWARD_COLUMN).toString();
        
        if(isAwardWindowOpen(awardNumber, CORRECT_AWARD)) {
            return ;
        }
        // 3587: Multi Campus Enahncements - Start
        boolean hasModifyRight = checkUserHasModifyRight(awardNumber);
        if(!hasModifyRight){
            CoeusOptionPane.showErrorDialog("You do not have the right to modify this Award. ");
            return ;
        }
        // 3587: Multi Campus Enahncements- End
        //Added for COEUSQA-3273 : Inactive Cost Element in use Error Alert required for Proposal and Award Budgets - start
        // get the budget data for award
        // If award budget holds inactive cost elements then it returns true
        boolean allow_copy = isInactiveCostCEPresent(awardNumber);
        int selection;
        if(allow_copy){
            selection = CoeusOptionPane.showQuestionDialog(coeusMessageResources.parseMessageKey(BUDGET_HAVE_INACTIVE_COST_ELEMENTS),
                    CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_YES);
            //if user selects yes then allow to copy the award budget
            if(selection ==CoeusOptionPane.SELECTION_YES){
                allow_copy = true;
            } else if(selection ==CoeusOptionPane.SELECTION_NO){
                allow_copy = false;
            }
        }else{
            allow_copy = true;
        }
        //if allow_copy returns true then allow to create new entry for award
        if(allow_copy){
            AwardBean awardBean = new AwardBean();
            awardBean.setMitAwardNumber(awardNumber);
            
            AwardBaseWindowController awardBaseWindowController = new AwardBaseWindowController("Award New Entry : ", NEW_ENTRY, awardBean);
            awardBaseWindowController.setMaintainReporting(maintainReporting);
            awardBaseWindowController.display();
        }
        //Added for COEUSQA-3273 : Inactive Cost Element in use Error Alert required for Proposal and Award Budgets - end
    }
    
    private void displayAward() {
        int selectedRow = awardList.tblResults.getSelectedRow();
        if(selectedRow == -1)return ;
        
        String awardNumber = awardList.tblResults.getValueAt(selectedRow, AWARD_COLUMN).toString();
        
        if(isAwardWindowOpen(awardNumber, DISPLAY_MODE)) {
          return ;
        } 
        //Added for COEUSQA-2521 - User who has both view depts awards (or view award) and view award budget roles can't view award budget. - start
        //Taking the previous 'View Award' role util invoking this method.
        //For Loading the latest Roles Assigned to the User. 
        authorizationCheck();
        //Added for COEUSQA-2521 - User who has both view depts awards (or view award) and view award budget roles can't view award budget. - end 
        //Code added for Case#3388 - Implementing authorization check at department level - starts
        //Check the user is having rights to view this award
        if(!viewAward && !modifyAward){
        	// JM 12-19-2012 change unit number col index to static variable rather than hard-coded to 2
            String unitNumber = awardList.tblResults.getValueAt(selectedRow, UNIT_NUMBER_COL).toString();
            // JM END
            boolean hasRight = checkRightsInUnitLevel(unitNumber);
            if(!hasRight){
                CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey("repRequirements_exceptionCode.1056"));
                return;
            }
        }
        //Code added for Case#3388 - Implementing authorization check at department level - ends
        AwardBean awardBean = new AwardBean();
        awardBean.setMitAwardNumber(awardNumber);
        mdiForm.setCursor(new Cursor(java.awt.Cursor.WAIT_CURSOR));
        AwardBaseWindowController awardBaseWindowController = new AwardBaseWindowController("Display Award : ", DISPLAY_MODE , awardBean);
        mdiForm.setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));        
        awardBaseWindowController.setMaintainReporting(maintainReporting);
        awardBaseWindowController.display();        
    }
    
    /** displays inbox details. */
    private void showInboxDetails() {
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
    
    /** Displays Reporting Requirement details **/
    private void showReportingRequirements() {
        ReportingReqBaseWindowController reportingReqBaseWindowController =null;
        int selectedRow = awardList.tblResults.getSelectedRow();
        if(selectedRow == -1)return ;
        String awardNumber = awardList.tblResults.getValueAt(selectedRow, AWARD_COLUMN).toString();
        //Code added for Case#3388 - Implementing authorization check at department level - starts
        //Check the user is having rights to view this award        
        //Added with Case 3587: Multicampus Enhancement
        String unitNumber = awardList.tblResults.getValueAt(selectedRow, 2).toString();
        if(!checkUserHasReportingRights(unitNumber)){
            CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey("repRequirements_exceptionCode.1058"));
            return;
        }
        //3587 End
        if(!viewAward && !modifyAward){
//            String unitNumber = awardList.tblResults.getValueAt(selectedRow, 2).toString();
            boolean hasRight = checkRightsInUnitLevel(unitNumber);
            if(!hasRight){
                CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey("repRequirements_exceptionCode.1056"));
                return;
            }
        }
        //Code added for Case#3388 - Implementing authorization check at department level - ends          
        char mode = TypeConstants.DISPLAY_MODE;
        
        //Check if reporting requirements is available
        RequesterBean requester = new RequesterBean();
        ResponderBean responder;
        
        requester.setDataObject(awardNumber);
        requester.setFunctionType(HAS_REPORTING_REQUIREMENT);
        AppletServletCommunicator comm = new AppletServletCommunicator(CoeusGuiConstants.CONNECTION_URL + REP_REQ_SERVLET, requester);
        
        comm.send();
        responder = comm.getResponse();
        if(responder.isSuccessfulResponse()){
            boolean hasRepReq = ((Boolean)responder.getDataObject()).booleanValue();
            if(!hasRepReq) {
                //Doesn't have Reporting Requirements
                CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(REP_REQ_NOT_AVAILABLE) + awardNumber);
                return ;
            }else {
                //check if any reporting Requirement Window is opened and >= 3
                //Disp msg Cannot open more than 3 Award Reporting Requirement windows. 
                //Please close atleast one Award Reporting Requirement window.
                boolean repReqWinOpen = isRepReqWindowOpen(awardNumber, TypeConstants.DISPLAY_MODE, true, true);
                if(repReqWinOpen) {
                    return ;
                }
                //else Check if this rep req is opened then bring it to front
                
                //check if any reporting Requirements is already opened in Modify Mode.
                //Disp msg Only one Reporting Requirements window can be open in modify mode. 
                //Reporting Requirements window for award "+Award + " is already open in modify mode.
                //Do you want to open Reporting Requirements for the selected award in display mode."
                repReqWinOpen = isRepReqWindowOpen(awardNumber, TypeConstants.MODIFY_MODE, false, true);
                if(repReqWinOpen) {
                    int selected = CoeusOptionPane.showQuestionDialog("Only one Reporting Requirements window can be open in modify mode."+ 
                    "Reporting Requirements window for award " + awardNumber + " is already open in modify mode."+
                    "Do you want to open Reporting Requirements for the selected award in display mode.",
                    CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_YES);
                    if(selected == CoeusOptionPane.SELECTION_NO) {
                        return ;
                    }else {
                        mode = TypeConstants.DISPLAY_MODE;
                    }
                }else {
                    // No reporting Requirements is opened in Modify Mode
                    mode = TypeConstants.MODIFY_MODE;
                }
                
                //if Modify Mode then check if Award sheet is opened in Modes M, N, E,C,P. 
                //Award sheet for +s_mit_award_number + is open in modify mode.
                //Cannot open reporting requirements window in modify mode when
                //an award is being modified.\n Do you want to open reporting 
                //requirements window for the selected award in display mode
                if(mode == TypeConstants.MODIFY_MODE) {
                    if(isAwardWindowOpen(awardNumber, CORRECT_AWARD, false)) {
                        //An Award is opened in Modify Mode
                        AwardBaseWindow awardBaseWindow = (AwardBaseWindow)mdiForm.getEditingFrame(CoeusGuiConstants.AWARD_BASE_WINDOW);
                        //Bug Fix:1672 Start
                        //awardNumber = awardBaseWindow.getAwardBaseBean().getMitAwardNumber();
                        String awdNumber = awardBaseWindow.getAwardBaseBean().getMitAwardNumber();
                        //Bug Fix:1672 End
                        
                        int selection = CoeusOptionPane.showQuestionDialog("Award sheet for " + awdNumber + " is open in modify mode."
                        +"Cannot open reporting requirements window in modify mode when "
                        +"an award is being modified.\n Do you want to open reporting "
                        +"requirements window for the selected award in display mode.",
                        CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_YES);
                        if(selection == CoeusOptionPane.SELECTION_NO) {
                            return ;
                        }else {
                            mode = TypeConstants.DISPLAY_MODE;
                        }//End if else selection
                    }
                }
                
                //if award is locked then display Yes No Dialog
                //"Do you want to open reporting requirements for the selected award in display mode.
                if(mode == TypeConstants.MODIFY_MODE) {
                    requester.setFunctionType(IS_AWARD_LOCKED);
                    requester.setDataObject(awardNumber);
                    comm.send();
                    responder = comm.getResponse();
                    if(responder.isSuccessfulResponse()){
                        boolean awardLocked = ((Boolean)responder.getDataObject()).booleanValue();
                        if(awardLocked) {
                            CoeusOptionPane.showInfoDialog(responder.getMessage());
                            int selection = CoeusOptionPane.showQuestionDialog("Do you want to open reporting requirements for the selected award in display mode.",
                            CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_YES);
                            if(selection == CoeusOptionPane.SELECTION_NO) {
                                return ;
                            }else {
                                mode = TypeConstants.DISPLAY_MODE;
                            }
                        }
                    }else {
                        //Server Error
                        CoeusOptionPane.showInfoDialog(responder.getMessage());
                        return ;
                    }
                }//End if Modify mode
                
            }
        }else{
            CoeusOptionPane.showInfoDialog(responder.getMessage());
            return ;
        }
        
        AwardBean awardBean = new AwardBean();
        awardBean.setMitAwardNumber(awardNumber);
        reportingReqBaseWindowController = new ReportingReqBaseWindowController(awardBean, mode);
        reportingReqBaseWindowController.display();
    }
    
    private void generateReportingRequirements() {
        int selectedRow = awardList.tblResults.getSelectedRow();
        if(selectedRow == -1)return ;
        String awardNumber = awardList.tblResults.getValueAt(selectedRow, AWARD_COLUMN).toString();
        
        if(awardNumber.endsWith("001")) {
            CoeusOptionPane.showErrorDialog("Reporting requirements cannot be manually generated for root awards.");
            return ;
        }
        //Code added for Case#3388 - Implementing authorization check at department level - starts
        //Check the user is having rights to view this award     
        //Added with Case 3587: Multicampus Enhancement
        String unitNumber = awardList.tblResults.getValueAt(selectedRow, 2).toString();
        if(!checkUserHasReportingRights(unitNumber)){
            CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey("repRequirements_exceptionCode.1057"));
            return;
        }
        //3587 End
        if(!viewAward && !modifyAward){
//            String unitNumber = awardList.tblResults.getValueAt(selectedRow, 2).toString();
            boolean hasRight = checkRightsInUnitLevel(unitNumber);
            if(!hasRight){
                CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey("repRequirements_exceptionCode.1056"));
                return;
            }
        }
        //Code added for Case#3388 - Implementing authorization check at department level - ends        
        if(isAwardWindowOpen(awardNumber, CORRECT_AWARD, false)) {
            CoeusOptionPane.showErrorDialog("An award sheet is already open for modification."+
            "Please close the award that is being modified before regenerating reporting requirements");
            return ;
        }//end if award window open
        
        boolean repReqWinOpen = isRepReqWindowOpen(awardNumber, TypeConstants.MODIFY_MODE, false, false);
        if(repReqWinOpen) {
            int selection = CoeusOptionPane.showQuestionDialog("Reporting Requirements for the award "
            + awardNumber + " is being modified. Please close the reporting requirements window"
            +" before generating reporting requirements for another award. "
            +"\n Do you want to switch to the reporting requirements window now.",
            CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_YES);
            if(selection == CoeusOptionPane.SELECTION_NO) {
                return ;
            }else {
                //Select the rep req window
                CoeusInternalFrame frame = mdiForm.getFrame(
                    CoeusGuiConstants.REPORTING_REQ_BASE_WINDOW, awardNumber);
                if(frame != null) {
                    try{
                        frame.setSelected(true);
                        frame.setVisible(true);
                    }catch (PropertyVetoException propertyVetoException) {
                        propertyVetoException.printStackTrace();
                    }
                }
                return ;
            }
        }//end if rep req window open
        
        RequesterBean requester = new RequesterBean();
        ResponderBean responder;
        
        requester.setDataObject(awardNumber);
        requester.setFunctionType(HAS_REPORTING_REQUIREMENT);
        AppletServletCommunicator comm = new AppletServletCommunicator(CoeusGuiConstants.CONNECTION_URL + REP_REQ_SERVLET, requester);
        
        comm.send();
        responder = comm.getResponse();
        if(responder.isSuccessfulResponse()){
            boolean hasRepReq = ((Boolean)responder.getDataObject()).booleanValue();
            if(!hasRepReq) {
                //Doesn't have Reporting Requirements
                int selection = CoeusOptionPane.showQuestionDialog("Are you sure you want to generate"+
                " reporting requirements for the award - "+ awardNumber,
                CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_YES);
                if(selection == CoeusOptionPane.SELECTION_NO) {
                    return ;
                }else {
                    //Generate Reporting requirements
                    requester.setFunctionType(GENERATE_REPORTING_REQUIREMENTS);
                    comm.send();
                    responder = comm.getResponse();
                    if(!responder.isSuccessfulResponse()) {
                        CoeusOptionPane.showErrorDialog(responder.getMessage());
                        return ;
                    }
                }
                return ;
            }else {
                //Has Reporting Requirements
                CoeusOptionPane.showInfoDialog("Reporting requirements already exist for the award - "
                + awardNumber);
                return ;
            }
        }else{
            CoeusOptionPane.showInfoDialog(responder.getMessage());
            return ;
        }
        
    }
    
    private boolean closed = false;
    
    /** closes this window. */
    private void close() {
        mdiForm.removeFrame(CoeusGuiConstants.AWARD_FRAME_TITLE);
        //awardList.dispose();
        closed = true;
        //select next Internal Frame.
        awardList.doDefaultCloseAction();
        cleanUp();
    }
    
    public void vetoableChange(PropertyChangeEvent propertyChangeEvent) throws PropertyVetoException {
        if(closed) return ;
        
        boolean changed = ((Boolean) propertyChangeEvent.getNewValue()).booleanValue();
        if(propertyChangeEvent.getPropertyName().equals(JInternalFrame.IS_CLOSED_PROPERTY) && changed) {
            close();
        }
    }
    
    public void mouseClicked(MouseEvent mouseEvent) {
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
            if(mouseEvent.getClickCount() != 2) return ;
            //Double Clicked on Table. open Award in display Mode.
            
            //BUG FIX -- Bug Id:1062  Hour glassimplementation.. START
            mdiForm.setCursor(new Cursor(java.awt.Cursor.WAIT_CURSOR));
            displayAward();
            mdiForm.setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));
            //BUG FIX -- Bug Id:1062  Hour glassimplementation.. END
        }
    }
    
    public void mouseEntered(MouseEvent mouseEvent) {
    }
    
    public void mouseExited(MouseEvent mouseEvent) {
    }
    
    public void mousePressed(MouseEvent mouseEvent) {
    }
    
    public void mouseReleased(MouseEvent mouseEvent) {
    }
    
    private void saveAwardList() {
        //SaveAsDialog saveAsDialog = new SaveAsDialog(tblResultsTable);
        SaveAsDialog saveAsDialog = new SaveAsDialog(awardList.tblResults);
    }
    
    private void  showMedusaWindow(){
        try{
            ProposalAwardHierarchyLinkBean linkBean;
            MedusaDetailForm medusaDetailform;
            //CoeusInternalFrame frame = mdiForm.getFrame(CoeusGuiConstants.MEDUSA_BASE_FRAME_TITLE);
            int selectedRow = awardList.tblResults.getSelectedRow();
            if( selectedRow >= 0 ){
                String awardNumber = awardList.tblResults.getValueAt(selectedRow, AWARD_COLUMN).toString();
                linkBean = new ProposalAwardHierarchyLinkBean();
                linkBean.setAwardNumber(awardNumber);
                linkBean.setBaseType(CoeusConstants.AWARD);
                if( ( medusaDetailform = (MedusaDetailForm)mdiForm.getFrame(
                CoeusGuiConstants.MEDUSA_BASE_FRAME_TITLE))!= null ){
                    if( medusaDetailform.isIcon() ){
                        medusaDetailform.setIcon(false);
                    }
                    medusaDetailform.setSelectedNodeId(awardNumber);
                    medusaDetailform.setSelected( true );
                    return;
                }
                medusaDetailform = new MedusaDetailForm(mdiForm,linkBean);
                medusaDetailform.setVisible(true);
            }else{
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
                "protoBaseWin_exceptionCode.1052"));
            }
            
        }catch(Exception exception){
            exception.printStackTrace();
        }
    }
    
    public void beanUpdated(BeanEvent beanEvent) {
        Controller source = beanEvent.getSource();
        BaseBean baseBean = beanEvent.getBean();
        if(source.getClass().equals(AwardBaseWindowController.class) && baseBean.getClass().equals(AwardBean.class)) {
            //pre / next clicked
            AwardBaseWindowController awardBaseWindowController = (AwardBaseWindowController)source;
            if(beanEvent.getMessageId() == SHOW_NEXT_AWARD) {
                awardBaseWindowController.clearOldInstance();
                showNextAward(awardBaseWindowController);
                awardBaseWindowController.updateNewInstance();
            }else if(beanEvent.getMessageId() == SHOW_PREV_AWARD) {
                awardBaseWindowController.clearOldInstance();
                showPreviousAward(awardBaseWindowController);
                awardBaseWindowController.updateNewInstance();
            }
        }
    }
    
    private void showNextAward(AwardBaseWindowController awardBaseWindowController) {
        //Code modified for Case#3388 - Implementing authorization check at department level - starts
        //Iterate to next award numbers in the list till the user have rights to view the particular award
        boolean isValid = true;
        String awardNumber = null;
        int selectedRow = awardList.tblResults.getSelectedRow();
        while(isValid){
            if(selectedRow == -1)return ;

            if(awardList.tblResults.getRowCount() == 1 || selectedRow == awardList.tblResults.getRowCount() - 1) {
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
                NO_MORE_AWARDS_TO_DISPLAY));
                return ;
            }
            /*
            if((awardList.tblResults.getRowCount() - selectedRow) == 3) {
                //will be @ the last row. disable next
                awardBaseWindowController.setEnableNext(false);
            }else {
                //not @ the last row. enable next
                awardBaseWindowController.setEnableNext(true);
            }
            */
            selectedRow = selectedRow + 1;
            awardNumber = awardList.tblResults.getValueAt(selectedRow, AWARD_COLUMN).toString();
            awardList.tblResults.setRowSelectionInterval(selectedRow, selectedRow);
            if(!viewAward && !modifyAward){
            	// JM 12-19-2012 change unit number col index to static variable rather than hard-coded to 2
                String unitNumber = awardList.tblResults.getValueAt(selectedRow, UNIT_NUMBER_COL).toString();
                // JM END
                boolean hasRight = checkRightsInUnitLevel(unitNumber);
                if(hasRight){
                    awardList.tblResults.setRowSelectionInterval(selectedRow, selectedRow);
                    break;
                }
            } else {
                awardList.tblResults.setRowSelectionInterval(selectedRow, selectedRow);
                break;
            }
        }
        //Code modified for Case#3388 - Implementing authorization check at department level - ends
        AwardBean awardBean = new AwardBean();
        awardBean.setMitAwardNumber(awardNumber);
        
        awardBaseWindowController.setFormData(awardBean);
        
    }
    
    private void showPreviousAward(AwardBaseWindowController awardBaseWindowController) {
        //Code modified for Case#3388 - Implementing authorization check at department level - starts
        //Iterate to previous award numbers in the list till the user have rights to view the particular award
        boolean isValid = true;
        String awardNumber = null;
        int selectedRow = awardList.tblResults.getSelectedRow();
        while(isValid){
            if(selectedRow == -1)return ;

            if(awardList.tblResults.getRowCount() == 1 || selectedRow == 0) {
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
                NO_MORE_AWARDS_TO_DISPLAY));
                return ;
            }
            /*
            if(selectedRow == 1) {
                //@ the First row. disable Previous
                awardBaseWindowController.setEnablePrevious(false);
            }else {
                //not @ the first row. enable Previous
                awardBaseWindowController.setEnablePrevious(true);
            }
            */
            selectedRow = selectedRow - 1;
            awardNumber = awardList.tblResults.getValueAt(selectedRow, AWARD_COLUMN).toString();
            //awardList.tblResults.setRowSelectionInterval(selectedRow, selectedRow);
            if(!viewAward && !modifyAward){
            	// JM 12-19-2012 change unit number col index to static variable rather than hard-coded to 2
                String unitNumber = awardList.tblResults.getValueAt(selectedRow, UNIT_NUMBER_COL).toString();
                // JM END
                boolean hasRight = checkRightsInUnitLevel(unitNumber);
                if(hasRight){
                    awardList.tblResults.setRowSelectionInterval(selectedRow, selectedRow);
                    break;
                }
            } else {
                awardList.tblResults.setRowSelectionInterval(selectedRow, selectedRow);
                break;
            }            
        }
        //Code modified for Case#3388 - Implementing authorization check at department level - ends
        AwardBean awardBean = new AwardBean();
        awardBean.setMitAwardNumber(awardNumber);
        
        awardBaseWindowController.setFormData(awardBean);
    }
    
    public void cleanUp() {
        removeBeanUpdatedListener(this, AwardBean.class);
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
    
    /*
    private Hashtable getAwardData(char functionType, AwardBaseBean awardBaseBean, AwardHierarchyBean awardHierarchyBean) {
        //Get Data From server.
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setFunctionType(GET_AWARD_DETAILS);
                
        AwardBean awardBeanToServer = new AwardBean();
        if(awardBaseBean != null) {
            awardBeanToServer.setMitAwardNumber(awardBaseBean.getMitAwardNumber());
            awardBeanToServer.setSequenceNumber(awardBaseBean.getSequenceNumber());
        }
        
        System.out.println("Mode : "+functionType);
        awardBeanToServer.setMode(functionType);
        
        Hashtable dataToServer = new Hashtable();
        dataToServer.put(AwardBean.class, awardBeanToServer);
        
        if(awardHierarchyBean != null) {
            dataToServer.put(AwardHierarchyBean.class, awardHierarchyBean);
        }
        
        requesterBean.setDataObject(dataToServer);
        
        AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator(CoeusGuiConstants.CONNECTION_URL + SERVLET, requesterBean);
        appletServletCommunicator.setRequest(requesterBean);
        appletServletCommunicator.send();
        ResponderBean responderBean = appletServletCommunicator.getResponse();
        
        if(responderBean == null) {
            //Could not contact server.
            CoeusOptionPane.showErrorDialog(COULD_NOT_CONTACT_SERVER);
            return null;
        }
        
        if(responderBean.isSuccessfulResponse()) {
            Hashtable awardData = (Hashtable)responderBean.getDataObject();
            //prepare query key
            AwardBean awardBean = (AwardBean)awardData.get(AwardBean.class);
            this.awardBaseBean = (AwardBaseBean)awardBean;
            if(functionType == NEW_AWARD || functionType == NEW_CHILD) {
                this.awardBaseBean.setSequenceNumber(1);
            }
            
            queryKey = this.awardBaseBean.getMitAwardNumber() + this.awardBaseBean.getSequenceNumber();

            return awardData;

        }else {
            //Server Error
            CoeusOptionPane.showErrorDialog(SERVER_ERROR);
            return null;
        }
    }
    
     private void extractAwardToQueryEngine(Hashtable awardData) {
        
        AwardBean awardBean = (AwardBean)awardData.get(AwardBean.class);
        
        awardData.remove(AwardBean.class);
        
        AwardDetailsBean awardDetailsBean = awardBean.getAwardDetailsBean();
            if(awardDetailsBean != null) {
                awardDetailsBean.setUpdateTimestamp(awardBean.getUpdateTimestamp());
                awardDetailsBean.setUpdateUser(awardBean.getUpdateUser());
                CoeusVector cvAwardDetails = new CoeusVector();
                cvAwardDetails.add(awardDetailsBean);
                awardData.put(AwardDetailsBean.class, cvAwardDetails);
            }
            
            AwardHeaderBean awardHeaderBean = awardBean.getAwardHeaderBean();
            if(awardHeaderBean != null) {
                CoeusVector cvAwardHeader = new CoeusVector();
                //set Mit Award Number and Sequence number
                awardHeaderBean.setMitAwardNumber(awardBean.getMitAwardNumber());
                awardHeaderBean.setSequenceNumber(awardBean.getSequenceNumber());
                
                cvAwardHeader.add(awardHeaderBean);
                awardData.put(AwardHeaderBean.class ,cvAwardHeader);
            }
            
            CoeusVector cvAwardInvestigators = awardBean.getAwardInvestigators();
            if(cvAwardInvestigators != null && cvAwardInvestigators.size() > 0) {
                awardData.put(AwardInvestigatorsBean.class, cvAwardInvestigators);
            }
            
            CoeusVector cvAwardAmountInfo = awardBean.getAwardAmountInfo();
            if(cvAwardAmountInfo != null && cvAwardAmountInfo.size() > 0) {
                awardData.put(AwardAmountInfoBean.class, cvAwardAmountInfo);
            }
            
            CoeusVector cvAwardComments = awardBean.getAwardComments();
            if(cvAwardComments != null && cvAwardComments.size() > 0) {
                awardData.put(AwardCommentsBean.class, cvAwardComments);
            }
            
            CoeusVector cvAwardCustomElements = awardBean.getAwardCustomElements();
            if(cvAwardCustomElements != null && cvAwardCustomElements.size() > 0) {
                awardData.put(AwardCustomDataBean.class, cvAwardCustomElements);
            }
            
            CoeusVector cvAwardApprovedSubcontracts = awardBean.getAwardApprovedSubcontracts();
            if(cvAwardApprovedSubcontracts != null && cvAwardApprovedSubcontracts.size() > 0) {
                awardData.put(AwardApprovedSubcontractBean.class, cvAwardApprovedSubcontracts);
            }
            
            CoeusVector cvSubcontractFundingSource = awardBean.getSubcontractFundingSource();
            if(cvSubcontractFundingSource != null && cvSubcontractFundingSource.size() > 0) {
                awardData.put(SubContractFundingSourceBean.class, cvSubcontractFundingSource);
            }
            
            queryEngine.addDataCollection(queryKey,awardData);
    }
    */
    
    /**
     * Code added for Case#3388 - Implementing authorization check at department level
     * Check the user is having rights to view this award
     * @param unitNumber
     * @return boolean
     */    
    public boolean checkRightsInUnitLevel(String unitNumber){
        String VIEW_AWARDS_AT_UNIT = "VIEW_AWARDS_AT_UNIT";
        boolean viewAwardInUnit = false;
        Hashtable authorizations = new Hashtable();
        AuthorizationBean authorizationBean = new AuthorizationBean();
        authorizationBean.setFunction(VIEW_AWARDS_AT_UNIT);
        authorizationBean.setFunctionType("RIGHT");
        authorizationBean.setPerson(CoeusGuiConstants.getMDIForm().getUserId());
        authorizationBean.setQualifier(unitNumber);
        authorizationBean.setQualifierType("UNIT");
        authorizations.put(VIEW_AWARDS_AT_UNIT, new AuthorizationOperator(authorizationBean));

        RequesterBean requester = new RequesterBean();        
        requester.setAuthorizationOperators(authorizations);
        requester.setIsAuthorizationRequired(true);
        
        AppletServletCommunicator comm = new AppletServletCommunicator(CoeusGuiConstants.CONNECTION_URL + AUTH_SERVLET, requester);
        
        comm.send();
        ResponderBean responder = comm.getResponse();
        if(responder.isSuccessfulResponse()){
            authorizations = responder.getAuthorizationOperators();
            viewAwardInUnit = ((Boolean)authorizations.get(VIEW_AWARDS_AT_UNIT)).booleanValue();
        }else{
            CoeusOptionPane.showInfoDialog(responder.getMessage());
        }
        return viewAwardInUnit;
    }
    
    // 3587: Multi Campus Enahncements - Start
    private boolean checkUserHasModifyRight(String awardNumber) {
        boolean modifyRight = false;
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setFunctionType(CHECK_USER_HAS_MODIFY_RIGHT);
        requesterBean.setDataObject(awardNumber);
        
        AppletServletCommunicator appletServletCommunicator = new
                AppletServletCommunicator(CoeusGuiConstants.CONNECTION_URL + SERVLET, requesterBean);
        appletServletCommunicator.send();
        ResponderBean responderBean = appletServletCommunicator.getResponse();
        
        if(responderBean != null) {
            if(responderBean.isSuccessfulResponse()) {
                Boolean right = (Boolean) responderBean.getDataObject();
                modifyRight = right.booleanValue();
            }
        }
        return modifyRight;
    }
    
    private boolean checkUserHasReportingRights(String unitNumber){
        String MAINTAIN_REPORTING = "MAINTAIN_REPORTING";
        boolean reportingRight = false;
        Hashtable authorizations = new Hashtable();
        AuthorizationBean authorizationBean = new AuthorizationBean();
        authorizationBean.setFunction(MAINTAIN_REPORTING);
        authorizationBean.setFunctionType("RIGHT");
        authorizationBean.setPerson(CoeusGuiConstants.getMDIForm().getUserId());
        authorizationBean.setQualifier(unitNumber);
        authorizationBean.setQualifierType("UNIT");
        authorizations.put(MAINTAIN_REPORTING, new AuthorizationOperator(authorizationBean));

        RequesterBean requester = new RequesterBean();        
        requester.setAuthorizationOperators(authorizations);
        requester.setIsAuthorizationRequired(true);
        
        AppletServletCommunicator comm = new AppletServletCommunicator(CoeusGuiConstants.CONNECTION_URL + AUTH_SERVLET, requester);
        
        comm.send();
        ResponderBean responder = comm.getResponse();
        if(responder.isSuccessfulResponse()){
            authorizations = responder.getAuthorizationOperators();
            reportingRight = ((Boolean)authorizations.get(MAINTAIN_REPORTING)).booleanValue();
        }else{
            CoeusOptionPane.showInfoDialog(responder.getMessage());
        }
        return reportingRight;
    }
    // 3587: Multi Campus Enahncements - End
    
    //Added for COEUSQA-3273 : Inactive Cost Element in use Error Alert required for Proposal and Award Budgets - start
  
    /**
     * This method fetches the award budget details with cost element status
     * @param awardNumber
     * @return boolean value
     */
    private boolean isInactiveCostCEPresent(String awardNumber){
        CoeusVector cvCopyData=null;
        RequesterBean request = new RequesterBean();
        boolean inACtive = false;
        ResponderBean response = null;
        request.setId(awardNumber);
        request.setFunctionType(GET_INACTIVE_COST_ELEMENT_DETAILS);
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
        comm.send();
        response = comm.getResponse();
        if(response.isSuccessfulResponse()){
            inACtive =((Boolean) response.getDataObject()).booleanValue();
        }
        return inACtive;
    }
    //Added for COEUSQA-3273 : Inactive Cost Element in use Error Alert required for Proposal and Award Budgets - end
}
