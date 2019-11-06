/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/*
 * InstituteProposalBaseWindowController.java
 *
 * Created on April 23, 2004, 5:05 PM
 */

/* PMD check performed, and commented unused imports and variables on 25-SEPTEMBER-2007
 * by Nandkumar S N
 */

package edu.mit.coeus.instprop.controller;

import edu.mit.coeus.exception.*;
import edu.mit.coeus.search.gui.CoeusSearch;
import edu.mit.coeus.user.gui.UserDelegationForm;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.gui.*;
import edu.mit.coeus.propdev.gui.InboxDetailForm;
import edu.mit.coeus.utils.saveas.*;
import edu.mit.coeus.brokers.*;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.instprop.gui.*;
import edu.mit.coeus.instprop.bean.*;
import edu.mit.coeus.propdev.gui.MedusaDetailForm;
import edu.mit.coeus.propdev.gui.ProposalNotepadForm;
import edu.mit.coeus.propdev.bean.ProposalAwardHierarchyLinkBean;
//import edu.mit.coeus.utils.CoeusGuiConstants;
//import edu.mit.coeus.utils.BaseWindowObservable;
//import edu.mit.coeus.instprop.bean.InstituteProposalBean;
//import edu.mit.coeus.utils.query.*;
//import edu.mit.coeus.instprop.bean.InstituteProposalInvestigatorBean;
//import edu.mit.coeus.instprop.bean.InstituteProposalUnitBean;
//import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.negotiation.bean.NegotiationInfoBean;
//import edu.mit.coeus.utils.saveas.SaveAsDialog;

import java.beans.*;
//import javax.swing.JComponent;
import javax.swing.*;
import java.awt.*;
import javax.swing.event.*;
import java.awt.event.*;
//import java.util.HashMap;
import java.util.Hashtable;
import java.util.Observer;
import java.util.Vector;
import javax.swing.table.*;

//import edu.mit.coeus.utils.ChangePassword;
import edu.mit.coeus.user.gui.UserPreferencesForm;
//import edu.mit.coeus.utils.SortForm;
//import javax.swing.table.TableModel;



/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * 
* @author chandru
*/
public class InstituteProposalListController extends InstituteProposalController implements MouseListener, ActionListener, VetoableChangeListener, Observer{
    /** holds Award List instance to be controlled. */
    private InstituteProposalListForm instituteProposalListForm;  
    
    /** Coeus Serach instance to search Awards. */
    private CoeusSearch coeusSearch,proposalLogSearch;
    
    private Hashtable searchData;
    
    private static final int PROPOSAL_COLUMN = 0;
    
    private boolean closed = false;
    
    /** Holds CoeusMessageResources instance used for reading message Properties.
     */
    private CoeusMessageResources coeusMessageResources = CoeusMessageResources.getInstance();
    
    private QueryEngine queryEngine = QueryEngine.getInstance();;
    
    private CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();
    private InstituteProposalBaseWindowController instituteProposalBaseWindowController ;
    
    private JTable tblResultsTable;
    private IPReviewDialogController reviewController;
    private static final char CHECK_RIGHT = 'N'; 
    private static final char CHECK_NEGO_RIGHT = 'P';
    private static final String GET_SERVLET = "/InstituteProposalMaintenanceServlet";
    private static final String connect = CoeusGuiConstants.CONNECTION_URL + GET_SERVLET;
    
    private static final String INSTITUTE_PROPOSAL_SEARCH = "PROPOSALSEARCH";
    private static final String INSTITUTE_PROPOSAL_LOG_SEARCH = "PROPOSALLOGSEARCH";
    private static final String LOG_PROPOSAL_NUMBER = "PROPOSAL_NUMBER";
    private int selectedRow; 
    private int baseTableRow;
    
    private static final int PROP_NO_COLUMN = 0;
    private static final int TYPE_COLUMN = 1;
    private static final int ACTIVITY_COLUMN = 2;
    private static final int STATUS_COLUMN = 3;
    private static final int LEAD_UNIT_COLUMN = 4;
    private static final int LEAD_UNIT_NAME_COLUMN = 5;
    private static final int ACCOUNT_COLUMN = 6;
    private static final int TITLE_COLUMN = 7;
    private static final int SPONSOR_CODE_COLUMN = 8;
    private static final int SPONSOR_NAME_COLUMN = 9;
    private static final int PRIME_SPONSOR = 10;
    private static final int PI_COLUMN = 11;
    
    private boolean iPReviewRight = false;
    private boolean isViewProposal = false;
    private boolean negoMode = false;
    
    private String newProposalNumber=EMPTY_STRING;
    private ChangePassword changePassword;
    private UserPreferencesForm userPreferencesForm;
    //Added for Case#3682 - Enhancements related to Delegations - Start
    private UserDelegationForm userDelegationForm;
    //Added for Case#3682 - Enhancements related to Delegations - End
    
    // added by Nadh for sorting proposals Starts - 18-01-2005
    //holds sorted columns and its states
    private Vector vecSortedData; 
    
    private static final int OK_CLICKED = 0; 
    
    //holds column index
    private int oldCol = -1;
    
    //holds column status
    private int status=MultipleTableColumnSorter.NOT_SORTED;
    
    //end 18-01-2005

    //Code added for Case#3388 - Implementing authorization check at department level
    private static final char CAN_VIEW_INST_PROPOSAL = 'w';

    //Added for case#3243
    private static final String SIMPLE_DATE_FORMAT = "yyyy/MM/dd";      
    //Added for Case#3587 - Multi Campus enchanment - Start
    private static final String LEAD_UNIT_NUMBER = "LEAD_UNIT_NUMBER";
    private static final String EMPTY_STRING = "";
    private static final String MODIFY_INST_PROPOSAL = "MODIFY_INST_PROPOSAL";
    private static final String MAINTAIN_PROPOSAL_IP_REVIEW = "MAINTAIN_PROPOSAL_IP_REVIEW";
    private static final String CREATE_INST_PROPOSAL = "CREATE_INST_PROPOSAL";
    private static final String CANNOT_MODIFY_IP = "instPropModify_exceptionCode.1502";
    private static final String CANNOT_CREATE_NEW_ENTRY = "instPropNewEntry_exceptionCode.1503";
    private static final String CANNOT_REVIEW_IP = "instPropIPReview_exceptionCode.1357";
    private static final String CANNOT_OPEN_MEDUSA = "instPropMedusa_exceptionCode.1600";
    //Case#3587 - End
    // COEUSDEV-185: Proposal Development and Subawards are viewable from medusa without appropriate role- Start
    private static final String VIEW_INT_PROPOSAL_AT_UNIT = "VIEW_INT_PROPOSAL_AT_UNIT";
    private static final String VIEW_INST_PROPOSAL = "VIEW_INST_PROPOSAL";
    // COEUSDEV-185: Proposal Development and Subawards are viewable from medusa without appropriate role- End
    //Added for COEUSQA-1525 : Attachments for Institute Proposal - Start
    private static final char GET_ATTACHMENT_RIGHTS = 'a';
    private boolean userCanMaintainAttachment;
    private boolean userCanViewAttachment;
    private static final String CANNOT_OPEN_ATTACHMENT = "instPropAttachment_exceptionCode.1600";
    private static final String SELECT_ROW_TO_OPEN_ATTACHMENT ="instPropAttachment_exceptionCode.1601";
    //COEUSQA-1525 : End
    /** Creates a new instance of InstituteProposalBaseWindowController */
    public InstituteProposalListController() {
        initComponents();
        registerComponents();
    }
    
     private void initComponents() {
        try{
            instituteProposalListForm = new 
            InstituteProposalListForm("Institutional Proposal List", mdiForm);
           
            coeusSearch = new CoeusSearch(mdiForm, INSTITUTE_PROPOSAL_SEARCH , 0);
            JTable tblResults = coeusSearch.getEmptyResTable();
            instituteProposalListForm.initComponents(tblResults);
            /** contact server and get the rightCheckings and then 
             *enable and disable the menu items and tool bar buttons.
             */
            CoeusVector cvRightChecking = checkIsRightAvailable();
            if(cvRightChecking!= null && cvRightChecking.size() > 0){
                enableDisableMenuItems(cvRightChecking);
            }
        }catch (CoeusClientException exception) {
            CoeusOptionPane.showDialog(exception);
            exception.printStackTrace();
        }catch (Exception exception){
            CoeusOptionPane.showErrorDialog(exception.getMessage());
            exception.printStackTrace();
        }
    }
     
     /** displays Award Search Window. */
    private void showInstituteProposalSearch() {
        try {
            //Case 2451 start
            vecSortedData = new Vector();
            //Case 2451 end
             searchData = new Hashtable();
            coeusSearch.showSearchWindow();
            tblResultsTable = coeusSearch.getSearchResTable();
            searchData = coeusSearch.getSearchResults();
            instituteProposalListForm.displayResults(tblResultsTable);
            
            //adding listener for table.
            if(tblResultsTable != null) {
                instituteProposalListForm.tblResults.addMouseListener(this);
                instituteProposalListForm.tblResults.getTableHeader().addMouseListener(this);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
     /** Displays Award List. */
    public void display() {
        try{
            mdiForm.putFrame(CoeusGuiConstants.INSTITUTE_PROPOSAL_FRAME_TITLE, instituteProposalListForm);
            mdiForm.getDeskTopPane().add(instituteProposalListForm);
            instituteProposalListForm.setSelected(true);
            instituteProposalListForm.setVisible(true);
            showInstituteProposalSearch();
        }catch (PropertyVetoException propertyVetoException) {
            propertyVetoException.printStackTrace();
        }
    }
    
    public void formatFields() {
    }
    
    public java.awt.Component getControlledUI() {
        return instituteProposalListForm;
    }
    
    public Object getFormData() {
        return instituteProposalListForm;
    }
    
    public void registerComponents() {
        instituteProposalListForm.addVetoableChangeListener(this);
        
        // Setting listener for the Tool bar buttons
        instituteProposalListForm.btnClose.addActionListener(this);
        instituteProposalListForm.btnCorrectProposal.addActionListener(this);
        instituteProposalListForm.btnDisplayProposal.addActionListener(this);
        instituteProposalListForm.btnIpReview.addActionListener(this);
        instituteProposalListForm.btnMedusa.addActionListener(this);
        instituteProposalListForm.btnNegotiation.addActionListener(this);
        instituteProposalListForm.btnNewEntryProposal.addActionListener(this);
        instituteProposalListForm.btnNewProposal.addActionListener(this);
        instituteProposalListForm.btnNotepad.addActionListener(this);
        //Added for COEUSQA-1525 : Attachments for Institute Proposal - Start
        instituteProposalListForm.btnAttachments.addActionListener(this);
        //COEUSQA-1525 : End
        instituteProposalListForm.btnSaveas.addActionListener(this);
        instituteProposalListForm.btnSearchProposal.addActionListener(this);
        instituteProposalListForm.btnSortProposal.addActionListener(this);
        
        //Setting the listener for the menu items
        
        instituteProposalListForm.mnuItmChangePassword.addActionListener(this);
        instituteProposalListForm.mnuItmClose.addActionListener(this);
        instituteProposalListForm.mnuItmCorrectProposal.addActionListener(this);
        instituteProposalListForm.mnuItmDisplayProposal.addActionListener(this);
        instituteProposalListForm.mnuItmExit.addActionListener(this);
        instituteProposalListForm.mnuItmInbox.addActionListener(this);
        instituteProposalListForm.mnuItmIPReview.addActionListener(this);
        instituteProposalListForm.mnuItmMedusa.addActionListener(this);
        instituteProposalListForm.mnuItmNegotiation.addActionListener(this);
        instituteProposalListForm.mnuItmNewEntry.addActionListener(this);
        instituteProposalListForm.mnuItmNewProposal.addActionListener(this);
        instituteProposalListForm.mnuItmNotepad.addActionListener(this);
        //Added for COEUSQA-1525 : Attachments for Institute Proposal - Start
        instituteProposalListForm.mnuItmAttachments.addActionListener(this);
        //COEUSQA-1525 : End
        //Added for Case#3682 - Enhancements related to Delegations - Start
        instituteProposalListForm.mnuItmDelegations.addActionListener(this);
        //Added for Case#3682 - Enhancements related to Delegations - End
        instituteProposalListForm.mnuItmPreferences.addActionListener(this);
        
        //Commented since we are not using it in Coeus 4.0
        //instituteProposalListForm.mnuItmPrintSetUp.addActionListener(this);
        
        instituteProposalListForm.mnuItmSaveas.addActionListener(this);
        instituteProposalListForm.mnuItmSortProposal.addActionListener(this);
        instituteProposalListForm.mnuItmSearch.addActionListener(this);
        //Case 2110 Start
        instituteProposalListForm.mnuItmCurrentLocks.addActionListener(this);
        //Case 2110 End
        //Bug fixed for case #2123 start 1
        instituteProposalListForm.tblResults.addMouseListener(this);
        instituteProposalListForm.tblResults.getTableHeader().addMouseListener(this);
        //Bug fixed for case #2123 end 1
    }
    
    public void saveFormData() {
    }
    
    public void setFormData(Object data) {
    }
    
    public boolean validate() throws CoeusUIException {
        return false;
    }
    
    public void actionPerformed(ActionEvent actionEvent){
        try{
            Object source = actionEvent.getSource();
            if(source.equals(instituteProposalListForm.mnuItmCorrectProposal) ||
                source.equals(instituteProposalListForm.btnCorrectProposal)){
                    blockEvents(true);
                    //Modified for Case#3587 - multicampus enhancement  - Start
//                    correctInstituteProposal();
                    selectedRow = instituteProposalListForm.tblResults.getSelectedRow();
                    String leadUnitNumber = EMPTY_STRING;
                    if(selectedRow > -1){
                        leadUnitNumber = getSelectedValue(selectedRow,"UNIT_NUMBER");
                        boolean canModify = checkUserHasRightInLeadUnit(leadUnitNumber,MODIFY_INST_PROPOSAL);
                        
                        if(canModify){
                            correctInstituteProposal();
                        }else{
                            CoeusOptionPane.showWarningDialog(coeusMessageResources.parseMessageKey(
                                    CANNOT_MODIFY_IP));
                        }
                    }
                    //Case#3587 - End
                    blockEvents(false);
            }else if(source.equals(instituteProposalListForm.mnuItmDisplayProposal) ||
                source.equals(instituteProposalListForm.btnDisplayProposal)){
                displayInstituteProposal();
            }else if(source.equals(instituteProposalListForm.btnSearchProposal)
                || source.equals(instituteProposalListForm.mnuItmSearch)){
                showInstituteProposalSearch();
            }else if(source.equals(instituteProposalListForm.mnuItmNewEntry) ||
                source.equals(instituteProposalListForm.btnNewEntryProposal)){
                blockEvents(true);    
                //Modified for Case#3587 - multicampus enhancement  - Start
//                    newEntryProposal();
                selectedRow = instituteProposalListForm.tblResults.getSelectedRow();
                String leadUnitNumber = EMPTY_STRING;
                if(selectedRow > -1){
                    leadUnitNumber = getSelectedValue(selectedRow,"UNIT_NUMBER");
                    
                    boolean canModify = checkUserHasRightInLeadUnit(leadUnitNumber,MODIFY_INST_PROPOSAL);
                    
                    if(canModify){
                        newEntryProposal();
                    }else{
                        CoeusOptionPane.showWarningDialog(coeusMessageResources.parseMessageKey(
                                    CANNOT_CREATE_NEW_ENTRY));
                    }
                }
                //Case#3587 - End
                blockEvents(false);
            }else if(source.equals(instituteProposalListForm.btnClose) ||
                source.equals(instituteProposalListForm.mnuItmClose)){
                    close();
            }else if(source.equals(instituteProposalListForm.btnNewProposal) ||
                source.equals(instituteProposalListForm.mnuItmNewProposal)){
                    blockEvents(true);
                    newInstituteProposal();
                    blockEvents(false);
            }else if(source.equals(instituteProposalListForm.btnIpReview) ||
                    source.equals(instituteProposalListForm.mnuItmIPReview)){
                //Modified for Case#3587 - multicampus enhancement  - Start
//                    showIPReview();
                selectedRow = instituteProposalListForm.tblResults.getSelectedRow();
                 String leadUnitNumber = EMPTY_STRING;
                 if(selectedRow > -1){
                     leadUnitNumber = getSelectedValue(selectedRow,"UNIT_NUMBER");
                     boolean canModify = checkUserHasRightInLeadUnit(leadUnitNumber,MAINTAIN_PROPOSAL_IP_REVIEW);
                     
                     if(canModify){
                         showIPReview();
                     }else{
                          CoeusOptionPane.showWarningDialog(coeusMessageResources.parseMessageKey(
                                    CANNOT_REVIEW_IP));
                          
                     }
                 }
                //Case#3587 - End
            }else if(source.equals(instituteProposalListForm.mnuItmMedusa) || 
                source.equals(instituteProposalListForm.btnMedusa)){
                //Modified for Case#3587 - multicampus enhancement  - Start
//                showMedusaWindow();
                selectedRow = instituteProposalListForm.tblResults.getSelectedRow();
                String leadUnitNumber = EMPTY_STRING;
                if(selectedRow > -1){
                    leadUnitNumber  = getSelectedValue(selectedRow,"UNIT_NUMBER");
                    // COEUSDEV-185: Proposal Development and Subawards are viewable from medusa without appropriate role- Start
//                    boolean hasRight = isViewProposal;
                    boolean hasRight = checkUserHasRightInLeadUnit(leadUnitNumber, VIEW_INST_PROPOSAL);
                    if(!hasRight){
                        hasRight = checkUserHasRightInLeadUnit(leadUnitNumber, VIEW_INT_PROPOSAL_AT_UNIT);
                    }
                    // COEUSDEV-185: Proposal Development and Subawards are viewable from medusa without appropriate role- End
                    if(!hasRight){
                        hasRight = checkUserHasRightInLeadUnit(leadUnitNumber,MODIFY_INST_PROPOSAL );
                    }
                    if(!hasRight){
                        hasRight = checkUserHasRightInLeadUnit(leadUnitNumber,CREATE_INST_PROPOSAL);
                    }

                    if(hasRight){
                        showMedusaWindow();
                    }else{
                        
                        CoeusOptionPane.showWarningDialog(coeusMessageResources.parseMessageKey(
                                    CANNOT_OPEN_MEDUSA));
                    }
                }
                //Case#3587 - End
            }else if(source.equals(instituteProposalListForm.mnuItmNotepad) || 
                source.equals(instituteProposalListForm.btnNotepad)) {
                showNotepad();
            }else if(source.equals(instituteProposalListForm.btnSaveas)||
                source.equals(instituteProposalListForm.mnuItmSaveas)){
                    saveProposalList();
            }else if(source.equals(instituteProposalListForm.mnuItmInbox)){
                blockEvents(true);
                showInboxDetails();
                blockEvents(false);
            }else if(source.equals(instituteProposalListForm.mnuItmExit)){
                exitApplication();
            }else if(source.equals(instituteProposalListForm.mnuItmNegotiation) ||
               source.equals(instituteProposalListForm.btnNegotiation)){
                showNegotiation();
            }else if(source.equals(instituteProposalListForm.mnuItmChangePassword)){
                showChangePassword();
             //Added for Case#3682 - Enhancements related to Delegations - Start
             }else if(source.equals(instituteProposalListForm.mnuItmDelegations)){
                displayUserDelegation();
             //Added for Case#3682 - Enhancements related to Delegations - End
            }else if(source.equals(instituteProposalListForm.mnuItmPreferences)){
                showPreference();
            }else if(source.equals(instituteProposalListForm.mnuItmSortProposal) || 
                source.equals(instituteProposalListForm.btnSortProposal)) {
                showSort();
            }//Case 2110 Start
            else if(source.equals(instituteProposalListForm.mnuItmCurrentLocks)){
                showLocksForm();
            }//Case 2110 End
            //Added for COEUSQA-1525 : Attachments for Institute Proposal - Start
            else if(source.equals(instituteProposalListForm.mnuItmAttachments) ||
                    source.equals(instituteProposalListForm.btnAttachments) ){
                int selectedRow = instituteProposalListForm.tblResults.getSelectedRow();
                if(selectedRow < 0){
                    CoeusOptionPane.showWarningDialog(coeusMessageResources.parseMessageKey(
                            SELECT_ROW_TO_OPEN_ATTACHMENT));
                }else{
                    showProposalAttachment();
                }
            }
            //COEUSQA-1525 : End
        }catch (CoeusClientException coeusClientException){
            CoeusOptionPane.showDialog(coeusClientException);
        }catch (CoeusException coeusException){
            CoeusOptionPane.showErrorDialog(coeusException.getMessage());
        }catch (Exception ex){
            CoeusOptionPane.showErrorDialog(ex.getMessage());
        }
    }
    
    // added by Nadh to implement sorting proposals start - 18-01-2004
    /*
     * this method shows the sort window
     * return void
     */
    private void showSort() {
        if(vecSortedData==null) {
            vecSortedData = new Vector();
        }
        SortForm sortForm = new SortForm(tblResultsTable,vecSortedData);
        Vector sortedData = sortForm.display();
        vecSortedData = (Vector)sortedData.get(1);
        if(((Integer)sortedData.get(0)).intValue() == OK_CLICKED)
            coeusSearch.sortByColumns(tblResultsTable,vecSortedData);
        else
            return;
    }//end Nadh
    
    
    //Added for Case#3682 - Enhancements related to Delegations - Start
    /*
     * Displays Delegations window
     */
    private void displayUserDelegation() {
        userDelegationForm = new UserDelegationForm(mdiForm,true);
        userDelegationForm.display();
    }
    //Added for Case#3682 - Enhancements related to Delegations -   End
    
    // Added by surekha to implement the User Preference details
    private void showPreference(){
        if(userPreferencesForm == null) {
            userPreferencesForm = new UserPreferencesForm(mdiForm,true);
        }
        userPreferencesForm.loadUserPreferences(mdiForm.getUserId());
        userPreferencesForm.setUserName(mdiForm.getUserName());
        userPreferencesForm.display();
    }// End surekha
    
    
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
    
    
    private void showNegotiation() throws CoeusClientException, Exception{
        int selRow = instituteProposalListForm.tblResults.getSelectedRow();
        if(selRow!=-1){
            String proposalNumber = getSelectedValue(selRow, "PROPOSAL_NUMBER");
            if(enableDisableNegotiation()){
                if(negoMode){
                    NegotiationInfoBean bean = new NegotiationInfoBean();
                    bean.setNegotiationNumber(proposalNumber);
                    edu.mit.coeus.negotiation.controller.NegotiationBaseWindowController controller=
                    new edu.mit.coeus.negotiation.controller.NegotiationBaseWindowController(CoeusGuiConstants.INSTITUTE_PROPOSAL_LIST,'M',bean);
                    controller.display();
                }else{
                    NegotiationInfoBean bean = new NegotiationInfoBean();
                    bean.setNegotiationNumber(proposalNumber);
                    edu.mit.coeus.negotiation.controller.NegotiationBaseWindowController controller=
                    new edu.mit.coeus.negotiation.controller.NegotiationBaseWindowController(CoeusGuiConstants.INSTITUTE_PROPOSAL_LIST,'D',bean);
                    controller.display();
                }
            }
        }
    }
    
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
    
     /** To save the proposal log list
     */
    private void saveProposalList(){
        selectedRow = instituteProposalListForm.tblResults.getSelectedRow();
        //Code modified for PT ID#2382 - Save As functionality
        if( selectedRow == -1 ){
            //return;
        }
        //SaveAsDialog saveAsDialog = new SaveAsDialog(tblResultsTable);searchData
        SaveAsDialog saveAsDialog = new SaveAsDialog(instituteProposalListForm.tblResults);
    }
    
    /** To display Medusa screen
     */
    private void showMedusaWindow(){
        try{
            ProposalAwardHierarchyLinkBean linkBean;
            MedusaDetailForm medusaDetailform;
            selectedRow = instituteProposalListForm.tblResults.getSelectedRow();
            if( selectedRow >= 0 ){
                String proposalNumber = getSelectedValue(selectedRow, LOG_PROPOSAL_NUMBER);
                linkBean = new ProposalAwardHierarchyLinkBean();
                linkBean.setInstituteProposalNumber(proposalNumber);
                linkBean.setBaseType(CoeusConstants.INST_PROP);
                if( ( medusaDetailform = (MedusaDetailForm)mdiForm.getFrame(
                CoeusGuiConstants.MEDUSA_BASE_FRAME_TITLE))!= null ){
                    if( medusaDetailform.isIcon() ){
                        medusaDetailform.setIcon(false);
                    }
                    medusaDetailform.setSelectedNodeId(proposalNumber);
                    medusaDetailform.setSelected( true );
                    return;
                }
                medusaDetailform = new MedusaDetailForm(mdiForm, linkBean);
                medusaDetailform.setVisible(true);
            }
            
        }catch(Exception exception){
            exception.printStackTrace();
        }
    }
    
    /** To display the notepad window if not already open */
    private void showNotepad() throws Exception{
        //Check if Notepad is already opened
        CoeusInternalFrame frame = mdiForm.getFrame(CoeusGuiConstants.NOTEPAD_FRAME_TITLE);
        if(frame == null){
            selectedRow = instituteProposalListForm.tblResults.getSelectedRow();
            if( selectedRow == -1 ){
                return ;
            }
            String proposalNumber = getSelectedValue(selectedRow, LOG_PROPOSAL_NUMBER);
            ProposalAwardHierarchyLinkBean linkBean = new ProposalAwardHierarchyLinkBean();
            linkBean.setInstituteProposalNumber(proposalNumber);
            linkBean.setBaseType(CoeusConstants.INST_PROP);

            ProposalNotepadForm proposalNotepadForm = new ProposalNotepadForm(linkBean, mdiForm);
            proposalNotepadForm.display();

        }else{
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
            "proposal_Notepad_exceptionCode.7116"));
        }
    }
    
    private void showIPReview() throws Exception{
        selectedRow = instituteProposalListForm.tblResults.getSelectedRow();
        InstituteProposalBean baseBean = new InstituteProposalBean();
        if(selectedRow == -1)return ;
        String proposalNumber = getSelectedValue(selectedRow, LOG_PROPOSAL_NUMBER);
        
        // try to get the requested frame which is already opened 
            CoeusInternalFrame frame = mdiForm.getFrame(
                    CoeusGuiConstants.CORRECT_INSTITUTE_PROPOSAL_BASE_WINDOW,proposalNumber);
            if (frame != null){
                try{
                    frame.setSelected(true);
                    frame.setVisible(true);
                    return ;
                }catch (PropertyVetoException propertyVetoException) {
                    
                }
            }
        baseBean.setProposalNumber(proposalNumber);
        //baseBean.setSequenceNumber(
        setIPReviewRight(isIPReviewRight());
        reviewController = new IPReviewDialogController((InstituteProposalBaseBean)baseBean,getFunctionType());
    }
    
    private void newInstituteProposal() throws CoeusClientException,CoeusException{
        try {
            
            InstituteProposalBean newInstituteProposalBean = new InstituteProposalBean();
            
            newInstituteProposalBean.setProposalNumber("");
            newInstituteProposalBean.setMode(NEW_INST_PROPOSAL);
            
             InstituteProposalBaseWindowController instituteProposalBaseWindowController =
                    new InstituteProposalBaseWindowController("Institute Proposal  ", NEW_INST_PROPOSAL, (InstituteProposalBaseBean)newInstituteProposalBean);             
              if(isProposalWindowOpen(instituteProposalBaseWindowController.getProposalNumber(), CORRECT_INST_PROPOSAL)) {
                 return ;
                }
             instituteProposalBaseWindowController.setIpRight(isIPReviewRight());
             String proposalNumberUnitNumber[] = instituteProposalBaseWindowController.displayProposal();
             if(!instituteProposalBaseWindowController.isWindowOpen()){
                 instituteProposalBaseWindowController.setNegotiationRightData(checkNegoRightInNewMode(proposalNumberUnitNumber[0]));
                 String unitNumber = proposalNumberUnitNumber[1];
                 if(unitNumber!= null && !(unitNumber.trim().equals(EMPTY_STRING))){
                    instituteProposalBaseWindowController.setUnitNumber(unitNumber);
                 }
                 instituteProposalBaseWindowController.registerObserver(this);
                // instituteProposalBaseWindowController.setFormData((InstituteProposalBaseBean)newInstituteProposalBean);
                 baseTableRow = instituteProposalListForm.tblResults.getSelectedRow();
             }else{
                 return ;
             }
//        } catch (Exception e) {
//            e.printStackTrace();
        }catch (CoeusClientException coeusClientException){
            CoeusOptionPane.showDialog(coeusClientException);
        }catch (CoeusException coeusException){
            CoeusOptionPane.showErrorDialog(coeusException.getMessage());
        }
    }
    
     /** Get the proposal number based on the search index column
     */
    private String getSelectedValue(int row, String colValue) 
        throws CoeusClientException,CoeusException{
        int column = getColumnIndexValue(coeusSearch, colValue);
        return (String)tblResultsTable.getValueAt(row, column);
    }
    
    /** Get the column index for the selected row, Get the data based on the 
     *Display bean data
     */
    private int getColumnIndexValue( CoeusSearch coeusSearch, String columnName) 
    throws CoeusClientException,CoeusException{
        int columnIndex = -1;
        try{
            SearchColumnIndex searchIndex = new SearchColumnIndex();
            columnIndex = searchIndex.getSearchColumnIndex(coeusSearch, columnName);
            searchIndex = null;
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return columnIndex;
    }
        
    
    private void correctInstituteProposal() throws CoeusClientException,CoeusException{
        selectedRow = instituteProposalListForm.tblResults.getSelectedRow();
        if(selectedRow == -1)return ;
        
         String proposalNumber = getSelectedValue(selectedRow,"PROPOSAL_NUMBER");
         //case Id #  2002 - Changed the column index - start
         String unitNumber = getSelectedValue(selectedRow,"UNIT_NUMBER");
         //case Id #  2002 - End
        // For Locking mechanism
        if(isProposalWindowOpen(proposalNumber, CORRECT_INST_PROPOSAL)) {
          return ;
        }
        //InstituteProposalBaseBean instituteProposalBaseBean= new InstituteProposalBaseBean();
        InstituteProposalBean instituteProposalBean = new InstituteProposalBean();
        instituteProposalBean.setProposalNumber(proposalNumber);
        
        instituteProposalBean.setMode(CORRECT_INST_PROPOSAL);
         InstituteProposalBaseWindowController instituteProposalBaseWindowController = 
            new InstituteProposalBaseWindowController("Correct Institute Proposal  ", CORRECT_INST_PROPOSAL, (InstituteProposalBaseBean)instituteProposalBean);
        instituteProposalBaseWindowController.registerObserver(this);
        
        instituteProposalBaseWindowController.setIpRight(isIPReviewRight());
        instituteProposalBaseWindowController.setNegotiationRightData(checkNegoRight(selectedRow,proposalNumber));
         instituteProposalBaseWindowController.setUnitNumber(unitNumber);
         
        baseTableRow = instituteProposalListForm.tblResults.getSelectedRow();
        String propUnitNumber[] = instituteProposalBaseWindowController.displayProposal();
    }
   
    private void newEntryProposal() throws CoeusClientException,CoeusException{
        selectedRow = instituteProposalListForm.tblResults.getSelectedRow();
        if(selectedRow == -1)return ;
        
         String proposalNumber = getSelectedValue(selectedRow,"PROPOSAL_NUMBER");
         //case Id #  2002 - Changed the column index - start
         String unitNumber = getSelectedValue(selectedRow,"UNIT_NUMBER");
         //case Id #  2002 - End
        if(isProposalWindowOpen(proposalNumber, CORRECT_INST_PROPOSAL)) {
          return ;
        }
         InstituteProposalBean instituteProposalBean= new InstituteProposalBean();
         instituteProposalBean.setProposalNumber(proposalNumber);
         instituteProposalBean.setMode(NEW_ENTRY_INST_PROPOSAL);
         
         InstituteProposalBaseWindowController instituteProposalBaseWindowController =
                 new InstituteProposalBaseWindowController("Institute Proposal New Entry  ", NEW_ENTRY_INST_PROPOSAL, (InstituteProposalBaseBean)instituteProposalBean);
         instituteProposalBaseWindowController.registerObserver(this);
         instituteProposalBaseWindowController.setIpRight(isIPReviewRight());
         instituteProposalBaseWindowController.setNegotiationRightData(checkNegoRight(selectedRow,proposalNumber));
         instituteProposalBaseWindowController.setUnitNumber(unitNumber);
         baseTableRow = instituteProposalListForm.tblResults.getSelectedRow();
         String proposalUnitNumber[] = instituteProposalBaseWindowController.displayProposal();

    }
    
    private void displayInstituteProposal() throws CoeusClientException,CoeusException{
        selectedRow = instituteProposalListForm.tblResults.getSelectedRow();
        if(selectedRow == -1)return ;
        String proposalNumber = getSelectedValue(selectedRow,"PROPOSAL_NUMBER");
         //case Id #  2002 - Changed the column index - start
         String unitNumber = getSelectedValue(selectedRow,"UNIT_NUMBER");
         //case Id #  2002 - End
         //Code added for Case#3388 - Implementing authorization check at department level - starts
         //Checking view rights before opening the institute proposal.
        if(!canViewProposal(proposalNumber)){
                            CoeusOptionPane.showErrorDialog(
                                coeusMessageResources.parseMessageKey("proposal_BaseWin_exceptionCode.7104"));
            return;
        }
        //Code added for Case#3388 - Implementing authorization check at department level - ends
        if(isProposalWindowOpen(proposalNumber, DISPLAY_PROPOSAL)) {
          return ;
        }
        
        InstituteProposalBean instituteProposalBean= new InstituteProposalBean();
        instituteProposalBean.setProposalNumber(proposalNumber);
        instituteProposalBean.setMode(DISPLAY_PROPOSAL);
        mdiForm.setCursor(new Cursor(java.awt.Cursor.WAIT_CURSOR));
         instituteProposalBaseWindowController = 
            new InstituteProposalBaseWindowController("Display Institute Proposal ", DISPLAY_PROPOSAL, (InstituteProposalBaseBean)instituteProposalBean);
         instituteProposalBaseWindowController.setNegotiationRightData(checkNegoRight(selectedRow,proposalNumber));
        instituteProposalBaseWindowController.setUnitNumber(unitNumber);
        instituteProposalBaseWindowController.registerObserver(this);
        String proposalUnitNumber[] = instituteProposalBaseWindowController.displayProposal();
        mdiForm.setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR ));
       // instituteProposalBaseWindowController.showNextProposal(tblResultsTable);
    }
    
    public void vetoableChange(PropertyChangeEvent propertyChangeEvent) throws PropertyVetoException {
         if(closed) return ;
        boolean changed = ((Boolean) propertyChangeEvent.getNewValue()).booleanValue();
        if(propertyChangeEvent.getPropertyName().equals(JInternalFrame.IS_CLOSED_PROPERTY) && changed) {
            close();
        }
    }
    
     /** closes this window. */
    private void close() {
        
        mdiForm.removeFrame(CoeusGuiConstants.INSTITUTE_PROPOSAL_FRAME_TITLE);
        closed = true;
        instituteProposalListForm.doDefaultCloseAction();
        clean();

    }
    // To clean the instance variables
    private void clean(){
       searchData = null;
       tblResultsTable = null;
       instituteProposalListForm.tblResults.removeMouseListener(this);
       instituteProposalListForm.tblResults.getTableHeader().removeMouseListener(this);
    }
    
    public void mouseClicked(MouseEvent mouseEvent) {
        try{
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
            }//end Nadh
            if(mouseEvent.getSource() instanceof JTable) {
                int clickCount = mouseEvent.getClickCount();
                if(clickCount != 2) return ;
                // If there is no right to view the proposal in display mode then don't open
                // the window in display mode.
                //Code commented for Case#3388 - Implementing authorization check at department level
                //Condition commented that the view right checking is moved in to the displayInstituteProposal() block
                //if(isViewProposal){
                    
                    //Bug Fix:1062 -- Hourglass implemntation START
                    mdiForm.setCursor(new Cursor(java.awt.Cursor.WAIT_CURSOR));
                    displayInstituteProposal();
                    mdiForm.setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));
                    //Bug Fix:1062 -- Hourglass implemntation END
                //}
            }
        }catch (CoeusClientException coeusClientException){
            CoeusOptionPane.showDialog(coeusClientException);
        }catch (CoeusException coeusException){
            CoeusOptionPane.showErrorDialog(coeusException.getMessage());
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
    
    /** Contact the server to get the Right details. If right present return a 
     *vector which contains 4 elelments of boolean object
     */
    private  CoeusVector checkIsRightAvailable() throws CoeusClientException{
        CoeusVector cvData = null;
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setFunctionType(CHECK_RIGHT);
        AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator(connect, requesterBean);
        appletServletCommunicator.send();
        ResponderBean responderBean = appletServletCommunicator.getResponse();
        if(responderBean==null){
            return null;
        }
        if(responderBean.isSuccessfulResponse()) {
            cvData = (CoeusVector)responderBean.getDataObject();
        }else {
            throw new CoeusClientException(responderBean.getMessage(),CoeusClientException.ERROR_MESSAGE);
            //CoeusOptionPane.showErrorDialog(responderBean.getMessage());
           // return null;
        }
        return cvData;
    }
    
    // Check for the negotiation right. This call will be made while opening the 
    // windows in all modes.
//    private CoeusVector checkNegoRight(){
//            int selRow = tblResultsTable.getSelectedRow();
//            CoeusVector cvData = null;
//            if(selRow!=-1){
//                String proposalNumber = (String)tblResultsTable.getValueAt(selRow, 0);
//                RequesterBean requesterBean = new RequesterBean();
//                requesterBean.setFunctionType(CHECK_NEGO_RIGHT);
//                requesterBean.setDataObject(proposalNumber);
//                AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator(connect, requesterBean);
//                appletServletCommunicator.send();
//                ResponderBean responderBean = appletServletCommunicator.getResponse();
//                if(responderBean.isSuccessfulResponse()) {
//                    cvData = (CoeusVector)responderBean.getDataObject();
//
//                }else {
//                    CoeusOptionPane.showErrorDialog(responderBean.getMessage());
//                    return null;
//                }
//        }
//        return cvData;
//    }

    // This is an overloaded method which will accept proposal number and 
    // selected row of the table.Added by chandra to avoid the server call twise.
    // @returns vector of rights.
    // Check for the negotiation right. This call will be made while opening the 
    // windows in all modes.
    private CoeusVector checkNegoRight(int selRow,String proposalNumber) throws CoeusClientException{
            CoeusVector cvData = null;
                RequesterBean requesterBean = new RequesterBean();
                requesterBean.setFunctionType(CHECK_NEGO_RIGHT);
                requesterBean.setDataObject(proposalNumber);
                AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator(connect, requesterBean);
                appletServletCommunicator.send();
                ResponderBean responderBean = appletServletCommunicator.getResponse();
                if(responderBean==null){
                    return null;
                }
                if(responderBean.isSuccessfulResponse()) {
                    cvData = (CoeusVector)responderBean.getDataObject();
                }else {
                    throw new CoeusClientException(responderBean.getMessage(),CoeusClientException.ERROR_MESSAGE);
//                    CoeusOptionPane.showErrorDialog(responderBean.getMessage());
//                    return null;
                }
        return cvData;
    }
    
    // Check for the negotiation right in New Mode. This call will be made while opening the 
    // windows in all modes.
    private CoeusVector checkNegoRightInNewMode(String proposalNumber) throws CoeusClientException{
            CoeusVector cvData = null;
            RequesterBean requesterBean = new RequesterBean();
            requesterBean.setFunctionType(CHECK_NEGO_RIGHT);
            requesterBean.setDataObject(proposalNumber);
            AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator(connect, requesterBean);
            appletServletCommunicator.send();
            ResponderBean responderBean = appletServletCommunicator.getResponse();
            if(responderBean==null){
                return null;
            }
            if(responderBean.isSuccessfulResponse()) {
                cvData = (CoeusVector)responderBean.getDataObject();
            }else {
                throw new CoeusClientException(responderBean.getMessage(),CoeusClientException.ERROR_MESSAGE);
            }
        return cvData;
    }
    
    
    
    
    /** Get the vector of boolean Objects. Depending upon the right, enable and 
     *disable the menu items and tool bar buttons
     */
    private void enableDisableMenuItems(CoeusVector rightsData){
        boolean isCreateProposal = ((Boolean)rightsData.elementAt(0)).booleanValue();
        boolean isModifyProposal = ((Boolean)rightsData.elementAt(1)).booleanValue();
        isViewProposal = ((Boolean)rightsData.elementAt(2)).booleanValue();
        iPReviewRight  = ((Boolean)rightsData.elementAt(3)).booleanValue();
        
        if(!isCreateProposal){
            instituteProposalListForm.mnuItmNewProposal.setEnabled(false);
            instituteProposalListForm.btnNewProposal.setEnabled(false);
        }
        if(!isModifyProposal){
            instituteProposalListForm.mnuItmNewProposal.setEnabled(false);
            instituteProposalListForm.mnuItmNewEntry.setEnabled(false);
            instituteProposalListForm.mnuItmCorrectProposal.setEnabled(false);
            
            instituteProposalListForm.btnNewProposal.setEnabled(false);
            instituteProposalListForm.btnNewEntryProposal.setEnabled(false);
            instituteProposalListForm.btnCorrectProposal.setEnabled(false);
        }
        
        //Code commented for Case#3388 - Implementing authorization check at department level - starts
//        if(!isViewProposal){
//            instituteProposalListForm.mnuItmDisplayProposal.setEnabled(false);
//            instituteProposalListForm.btnDisplayProposal.setEnabled(false);
//        }
        //Code commented for Case#3388 - Implementing authorization check at department level - ends
        if(!iPReviewRight){
            instituteProposalListForm.mnuItmIPReview.setEnabled(false);
            instituteProposalListForm.btnIpReview.setEnabled(false);
        }
    }
    
     /** Build the new proposal data based on the search column index
     *@param columnIndex, proposalDevelopmentFormBean
     */
    private Vector buildNewProposalData(SearchColumnIndex searchColumnIndex,
    InstituteProposalInvestigatorBean invBean,InstituteProposalUnitBean unitBean,InstituteProposalBean bean) {
        Vector data  = new Vector();
        DateUtils dtUtils = new DateUtils();
        
        try{
            String reqStartDateInitial = EMPTY_STRING;
            String reqEndDateInitial = EMPTY_STRING;
            String reqstartTotal = EMPTY_STRING;
            String reqEndTotal = EMPTY_STRING;
            
            if(bean.getRequestStartDateInitial()!= null){
                reqStartDateInitial = dtUtils.formatDate(
                bean.getRequestStartDateInitial().toString(),"dd-MMM-yyyy");
            }
            if(bean.getRequestEndDateInitial()!= null){
                reqEndDateInitial = dtUtils.formatDate(
                bean.getRequestEndDateInitial().toString(),"dd-MMM-yyyy");
            }
            if(bean.getRequestStartDateTotal()!= null){
                reqstartTotal = dtUtils.formatDate(
                bean.getRequestStartDateTotal().toString(),"dd-MMM-yyyy");
            }
            if(bean.getRequestEndDateTotal()!= null){
                reqEndTotal = dtUtils.formatDate(
                bean.getRequestEndDateTotal().toString(),"dd-MMM-yyyy");
            }
            java.util.WeakHashMap mapData = new java.util.WeakHashMap();
            
            mapData.put(new Integer(searchColumnIndex.getSearchColumnIndex(
            coeusSearch,"PROPOSAL_NUMBER")), bean.getProposalNumber());
            
            //Added for COEUSQA-1525 : Attachments for Institute Proposal - Start
            mapData.put(new Integer(searchColumnIndex.getSearchColumnIndex(
            coeusSearch,"SEQUENCE_NUMBER")),bean.getSequenceNumber()+CoeusGuiConstants.EMPTY_STRING);
            //COEUSQA-1525 : end
            
            mapData.put(new Integer(searchColumnIndex.getSearchColumnIndex(
            coeusSearch,"PROPOSAL_TYPE_DESC")),bean.getProposalTypeDescription());
            mapData.put(new Integer(searchColumnIndex.getSearchColumnIndex(
            coeusSearch,"PROPOSAL_ACTIVITY_DESC")),bean.getProposalActivityTypeDescription());
            mapData.put(new Integer(searchColumnIndex.getSearchColumnIndex(
            coeusSearch,"PROPOSAL_STATUS_DESC")),bean.getStatusDescription());
            mapData.put(new Integer(searchColumnIndex.getSearchColumnIndex(
            coeusSearch,"UNIT_NUMBER")),unitBean.getUnitNumber());
            mapData.put(new Integer(searchColumnIndex.getSearchColumnIndex(
            coeusSearch,"UNIT_NAME")),unitBean.getUnitName());
            mapData.put(new Integer(searchColumnIndex.getSearchColumnIndex(
            coeusSearch,"CURRENT_ACCOUNT_NUMBER")),bean.getCurrentAccountNumber());
            mapData.put(new Integer(searchColumnIndex.getSearchColumnIndex(
            coeusSearch,"TITLE")),bean.getTitle());
            mapData.put(new Integer(searchColumnIndex.getSearchColumnIndex(
            coeusSearch,"SPONSOR_CODE")),bean.getSponsorCode());
            mapData.put(new Integer(searchColumnIndex.getSearchColumnIndex(
            coeusSearch,"SPONSOR_NAME")),bean.getSponsorName());
            mapData.put(new Integer(searchColumnIndex.getSearchColumnIndex(
            coeusSearch,"PRIME_SPONSOR_CODE")),bean.getPrimeSponsorCode());
            mapData.put(new Integer(searchColumnIndex.getSearchColumnIndex(
            coeusSearch,"PERSON_NAME")),invBean.getPersonName());
            mapData.put(new Integer(searchColumnIndex.getSearchColumnIndex(
            coeusSearch,"REQUESTED_START_DATE_INITIAL")),reqStartDateInitial);
            mapData.put(new Integer(searchColumnIndex.getSearchColumnIndex(
            coeusSearch,"REQUESTED_END_DATE_INITIAL")),reqEndDateInitial);
            mapData.put(new Integer(searchColumnIndex.getSearchColumnIndex(
            coeusSearch,"REQUESTED_START_DATE_TOTAL")),reqstartTotal);
            mapData.put(new Integer(searchColumnIndex.getSearchColumnIndex(
            coeusSearch,"REQUESTED_END_DATE_TOTAL")),reqEndTotal);
            mapData.put(new Integer(searchColumnIndex.getSearchColumnIndex(
            coeusSearch,"TOTAL_DIRECT_COST_INITIAL")),new Double(bean.getTotalDirectCostInitial()));
            mapData.put(new Integer(searchColumnIndex.getSearchColumnIndex(
            coeusSearch,"TOTAL_INDIRECT_COST_INITIAL")),new Double(bean.getTotalInDirectCostTotal()));
            mapData.put(new Integer(searchColumnIndex.getSearchColumnIndex(
            coeusSearch,"TOTAL_DIRECT_COST_TOTAL")),new Double(bean.getTotalDirectCostTotal()));
            mapData.put(new Integer(searchColumnIndex.getSearchColumnIndex(
            coeusSearch,"TOTAL_INDIRECT_COST_TOTAL")),new Double(bean.getTotalInDirectCostTotal()));
            
            
            java.util.ArrayList keySet = new java.util.ArrayList(mapData.keySet());
            java.util.Collections.sort(keySet);
            java.util.Iterator iterator = keySet.iterator();
            while(iterator.hasNext()){
                data.add(mapData.get(iterator.next()));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return data;
    }
    
    public void update(java.util.Observable observable, Object arg) {
        try{
            DateUtils dtUtils = new DateUtils();
            String reqStartDateInitial = EMPTY_STRING;
            String reqEndDateInitial = EMPTY_STRING;
            String reqstartTotal = EMPTY_STRING;
            String reqEndTotal = EMPTY_STRING;
            
            edu.mit.coeus.utils.SearchColumnIndex searchColumnIndex = new edu.mit.coeus.utils.SearchColumnIndex();
            InstituteProposalInvestigatorBean invBean = null;
            InstituteProposalBean bean = null;
            InstituteProposalUnitBean unitBean = null;
            // If the values in Institute proposal bean is changed, then notify to the
            // List Controller
            
            //Code added for PT ID#3243
            String dateFormat = CoeusServerProperties.getProperty(CoeusPropertyKeys.SEARCH_DATE_FORMAT, SIMPLE_DATE_FORMAT);            
            if (arg instanceof InstituteProposalBean ){
                bean = (InstituteProposalBean)arg;
                CoeusVector cvInv = bean.getInvestigators();
                Equals invEqls = new Equals("principalInvestigatorFlag", true);
                if(cvInv!= null && cvInv.size() > 0){
                    cvInv = cvInv.filter(invEqls);
                    invBean = (InstituteProposalInvestigatorBean)cvInv.elementAt(0);
                }
                CoeusVector cvUnit = invBean.getInvestigatorUnits();
                invEqls  = new Equals("leadUnitFlag", true);
                if(cvUnit!= null && cvUnit .size() > 0){
                    cvUnit = cvUnit.filter(invEqls);
                    unitBean = (InstituteProposalUnitBean)cvUnit.elementAt(0);
                }
                
                if( ((BaseWindowObservable)observable).getFunctionType()== NEW_INST_PROPOSAL){
                    Vector vecTableRow = buildNewProposalData(searchColumnIndex,invBean,unitBean,bean);
                    int lastRow = instituteProposalListForm.tblResults.getRowCount();
                    vecTableRow.addElement(""+lastRow);
                    ((DefaultTableModel)instituteProposalListForm.tblResults.getModel()).insertRow(lastRow, vecTableRow);
                    if( lastRow == 0 ) {
                        instituteProposalListForm.tblResults.setRowSelectionInterval(0,0);
                    }
                    //Bug fixed for case #2123 start 2
                    if(tblResultsTable == null){
                        tblResultsTable = instituteProposalListForm.tblResults;
                    }
                    //Bug fixed for case #2123 end 2
                    baseTableRow = lastRow;
                  
                //Code commented for PT ID#3243, setting of new values back to list window made mandatory even in ADD mode
                }//else{
                    
                    
                    //Code modified for PT ID#3243 - start
                    if(bean.getRequestStartDateInitial()!= null){
                        //COEUSQA-1477 Dates in Search Results - Start
                        reqStartDateInitial = dtUtils.parseDateForSearchResults(bean.getRequestStartDateInitial().toString(), dateFormat);
                        //reqStartDateInitial = dtUtils.formatDate(
                        //    bean.getRequestStartDateInitial().toString(), dateFormat);
                        //COEUSQA-1477 Dates in Search Results - End
                    }
                    
                    if(bean.getRequestEndDateInitial()!= null){
                        //COEUSQA-1477 Dates in Search Results - Start
                        reqEndDateInitial = dtUtils.parseDateForSearchResults(bean.getRequestEndDateInitial().toString(), dateFormat);
                        //reqEndDateInitial = dtUtils.formatDate(
                        //bean.getRequestEndDateInitial().toString(), dateFormat);
                        //COEUSQA-1477 Dates in Search Results - End
                    }
                    
                    if(bean.getRequestStartDateTotal()!= null){
                        //COEUSQA-1477 Dates in Search Results - Start
                        reqstartTotal = dtUtils.parseDateForSearchResults(bean.getRequestStartDateTotal().toString(), dateFormat);
                        //reqstartTotal = dtUtils.formatDate(
                        //bean.getRequestStartDateTotal().toString(), dateFormat);
                        //COEUSQA-1477 Dates in Search Results - End
                    }
                    
                    if(bean.getRequestEndDateTotal()!= null){
                        //COEUSQA-1477 Dates in Search Results - Start
                        reqEndTotal = dtUtils.parseDateForSearchResults(bean.getRequestEndDateTotal().toString(), dateFormat);
                        //reqEndTotal = dtUtils.formatDate(
                        //bean.getRequestEndDateTotal().toString(), dateFormat);
                        //COEUSQA-1477 Dates in Search Results - End
                    }
                    //Code modified for PT ID#3243 - end
                    
                    ((TableModel)instituteProposalListForm.tblResults.getModel()).setValueAt(
                    bean.getProposalNumber(), baseTableRow,  searchColumnIndex.getSearchColumnIndex(coeusSearch,"PROPOSAL_NUMBER"));
                    
                    //Added for COEUSQA-1525 : Attachments for Institute Proposal - Start
                    // JM 11-2-2012 commented out this call as it was erroring out and keeping the screen from refreshing
                    //((TableModel)instituteProposalListForm.tblResults.getModel()).setValueAt(
                    //bean.getSequenceNumber()+CoeusGuiConstants.EMPTY_STRING, baseTableRow,  searchColumnIndex.getSearchColumnIndex(coeusSearch,"SEQUENCE_NUMBER"));
                    //COEUSQA-1525 : End
                    
                    ((TableModel)instituteProposalListForm.tblResults.getModel()).setValueAt(
                    bean.getProposalTypeDescription(), baseTableRow, searchColumnIndex.getSearchColumnIndex(coeusSearch,"PROPOSAL_TYPE_DESC"));
                    
                    ((TableModel)instituteProposalListForm.tblResults.getModel()).setValueAt(
                    bean.getProposalActivityTypeDescription(), baseTableRow, searchColumnIndex.getSearchColumnIndex(coeusSearch,"PROPOSAL_ACTIVITY_DESC"));
                    
                    ((TableModel)instituteProposalListForm.tblResults.getModel()).setValueAt(
                    bean.getStatusDescription(), baseTableRow, searchColumnIndex.getSearchColumnIndex(coeusSearch,"PROPOSAL_STATUS_DESC"));
                    ((TableModel)instituteProposalListForm.tblResults.getModel()).setValueAt(
                    unitBean.getUnitNumber(), baseTableRow, searchColumnIndex.getSearchColumnIndex(coeusSearch,"UNIT_NUMBER"));
                    ((TableModel)instituteProposalListForm.tblResults.getModel()).setValueAt(
                    unitBean.getUnitName(), baseTableRow, searchColumnIndex.getSearchColumnIndex(coeusSearch,"UNIT_NAME"));
                    
                    ((TableModel)instituteProposalListForm.tblResults.getModel()).setValueAt(
                    bean.getCurrentAccountNumber(), baseTableRow, searchColumnIndex.getSearchColumnIndex(coeusSearch,"CURRENT_ACCOUNT_NUMBER"));
                    
                    ((TableModel)instituteProposalListForm.tblResults.getModel()).setValueAt(
                    bean.getTitle(), baseTableRow, searchColumnIndex.getSearchColumnIndex(coeusSearch,"TITLE"));
                    
                    ((TableModel)instituteProposalListForm.tblResults.getModel()).setValueAt(
                    bean.getSponsorCode(), baseTableRow, searchColumnIndex.getSearchColumnIndex(coeusSearch,"SPONSOR_CODE"));
                    
                    ((TableModel)instituteProposalListForm.tblResults.getModel()).setValueAt(
                    bean.getSponsorName(), baseTableRow, searchColumnIndex.getSearchColumnIndex(coeusSearch,"SPONSOR_NAME"));
                    
                    ((TableModel)instituteProposalListForm.tblResults.getModel()).setValueAt(
                    bean.getPrimeSponsorCode(), baseTableRow, searchColumnIndex.getSearchColumnIndex(coeusSearch,"PRIME_SPONSOR_CODE"));
                    
                    ((TableModel)instituteProposalListForm.tblResults.getModel()).setValueAt(
                    invBean.getPersonName(), baseTableRow, searchColumnIndex.getSearchColumnIndex(coeusSearch,"PERSON_NAME"));
                    
                    ((TableModel)instituteProposalListForm.tblResults.getModel()).setValueAt(
                    reqStartDateInitial, baseTableRow, searchColumnIndex.getSearchColumnIndex(coeusSearch,"REQUESTED_START_DATE_INITIAL"));
                    
                    ((TableModel)instituteProposalListForm.tblResults.getModel()).setValueAt(
                    reqEndDateInitial, baseTableRow, searchColumnIndex.getSearchColumnIndex(coeusSearch,"REQUESTED_END_DATE_INITIAL"));
                    
                    ((TableModel)instituteProposalListForm.tblResults.getModel()).setValueAt(
                    reqstartTotal, baseTableRow, searchColumnIndex.getSearchColumnIndex(coeusSearch,"REQUESTED_START_DATE_TOTAL"));
                    
                    ((TableModel)instituteProposalListForm.tblResults.getModel()).setValueAt(
                    reqEndTotal, baseTableRow, searchColumnIndex.getSearchColumnIndex(coeusSearch,"REQUESTED_END_DATE_TOTAL"));
                    
                    ((TableModel)instituteProposalListForm.tblResults.getModel()).setValueAt(
                    new Double(bean.getTotalDirectCostInitial()), baseTableRow, searchColumnIndex.getSearchColumnIndex(coeusSearch,"TOTAL_DIRECT_COST_INITIAL"));
                    ((TableModel)instituteProposalListForm.tblResults.getModel()).setValueAt(
                    new Double(bean.getTotalInDirectCostInitial()), baseTableRow, searchColumnIndex.getSearchColumnIndex(coeusSearch,"TOTAL_INDIRECT_COST_INITIAL"));
                    ((TableModel)instituteProposalListForm.tblResults.getModel()).setValueAt(
                    new Double(bean.getTotalDirectCostTotal()), baseTableRow, searchColumnIndex.getSearchColumnIndex(coeusSearch,"TOTAL_DIRECT_COST_TOTAL"));
                    ((TableModel)instituteProposalListForm.tblResults.getModel()).setValueAt(
                    new Double(bean.getTotalInDirectCostTotal()), baseTableRow, searchColumnIndex.getSearchColumnIndex(coeusSearch,"TOTAL_INDIRECT_COST_TOTAL"));
                    
                    int selRow = instituteProposalListForm.tblResults.getSelectedRow();
                    if(  selRow != -1 ) {
                        baseTableRow = instituteProposalListForm.tblResults.getSelectedRow();
                    }
                    
                //}
                
            }else if(arg.equals("PREVIOUS_PROPOSAL")){
                int currentSelRow = instituteProposalListForm.tblResults.getSelectedRow();
                //Code added for Case#3388 - Implementing authorization check at department level - starts
                //Iterate to previous proposal numbers in the list till the user have rights to view the particular proposal                
                boolean isValid = true;
                while(isValid){
                    if(currentSelRow==0){
                        CoeusOptionPane.showInfoDialog("There are no more proposals to display ");
                        return ;
                    }
                    String propoNumber = getSelectedValue(currentSelRow-1, LOG_PROPOSAL_NUMBER);
                    if(canViewProposal(propoNumber)){
                        break;
                    }
                    currentSelRow--;
                }
                //Code added for Case#3388 - Implementing authorization check at department level - ends
                InstituteProposalBean newInstituteProposalBean = new InstituteProposalBean();
                String propoNumber = getSelectedValue(currentSelRow-1, LOG_PROPOSAL_NUMBER);
                newInstituteProposalBean.setProposalNumber(propoNumber);
                newInstituteProposalBean.setMode(DISPLAY_PROPOSAL);
                instituteProposalListForm.tblResults.setRowSelectionInterval(currentSelRow-1, currentSelRow-1);
                instituteProposalBaseWindowController.clearOldInstance();
                instituteProposalBaseWindowController.setMode(DISPLAY_PROPOSAL);
                instituteProposalBaseWindowController.setFormData((InstituteProposalBaseBean)newInstituteProposalBean);
                instituteProposalBaseWindowController.performNavigation(propoNumber);
                
            }else if(arg.equals("NEXT_PROPOSAL")){
                int rowCount= instituteProposalListForm.tblResults.getRowCount();
                int currentSelRow = instituteProposalListForm.tblResults.getSelectedRow();
                //Code added for Case#3388 - Implementing authorization check at department level - starts
                //Iterate to next proposal numbers in the list till the user have rights to view the particular proposal                
                boolean isValid = true;
                while(isValid){
                    if(currentSelRow==rowCount-1){
                        CoeusOptionPane.showInfoDialog("There are no more proposals to display ");
                        return ;
                    }
                    String propoNumber = getSelectedValue(currentSelRow+1, LOG_PROPOSAL_NUMBER);
                    if(canViewProposal(propoNumber)){
                        break;
                    }
                    currentSelRow++;
                }
                //Code added for Case#3388 - Implementing authorization check at department level - ends
                InstituteProposalBean newInstituteProposalBean = new InstituteProposalBean();
                String propoNumber = getSelectedValue(currentSelRow+1, LOG_PROPOSAL_NUMBER);
                newInstituteProposalBean.setProposalNumber(propoNumber);
                newInstituteProposalBean.setMode(DISPLAY_PROPOSAL);
                instituteProposalListForm.tblResults.setRowSelectionInterval(currentSelRow+1, currentSelRow+1);
                instituteProposalBaseWindowController.setProposalNumber(propoNumber);
                instituteProposalBaseWindowController.setMode(DISPLAY_PROPOSAL);
                instituteProposalBaseWindowController.clearOldInstance();
                instituteProposalBaseWindowController.setFormData((InstituteProposalBaseBean)newInstituteProposalBean);
                instituteProposalBaseWindowController.performNavigation(propoNumber);
            }
        }catch (CoeusException coeusException){
            CoeusOptionPane.showErrorDialog(coeusException.getMessage());
        }catch (CoeusClientException coeusClientException){
            CoeusOptionPane.showErrorDialog(coeusClientException.getMessage());
        }
    }
    
    /** Getter for property iPReviewRight.
     * @return Value of property iPReviewRight.
     *
     */
    public boolean isIPReviewRight() {
        return iPReviewRight;
    }
    
    /** Setter for property iPReviewRight.
     * @param iPReviewRight New value of property iPReviewRight.
     *
     */
    public void setIPReviewRight(boolean iPReviewRight) {
        this.iPReviewRight = iPReviewRight;
    }
    
    /** Getter for property newProposalNumber.
     * @return Value of property newProposalNumber.
     *
     */
    public java.lang.String getNewProposalNumber() {
        return newProposalNumber;
    }
    
    /** Setter for property newProposalNumber.
     * @param newProposalNumber New value of property newProposalNumber.
     *
     */
    public void setNewProposalNumber(java.lang.String newProposalNumber) {
        this.newProposalNumber = newProposalNumber;
    }
    
    // Check for the negotiation right. If rights are not there then throw the 
    // message.
    // return @boolean for the message
    private boolean enableDisableNegotiation() throws CoeusClientException, Exception{
        selectedRow = instituteProposalListForm.tblResults.getSelectedRow();
        if(selectedRow == -1)return false;
        String proposalNumber = getSelectedValue(selectedRow, LOG_PROPOSAL_NUMBER);
        CoeusVector rightData = checkNegoRight(selectedRow,proposalNumber);
        boolean hasNegoModifyRight  = ((Boolean)rightData.elementAt(0)).booleanValue();
        boolean hasNegoViewData = ((Boolean)rightData.elementAt(1)).booleanValue();
        boolean checkPi = ((Boolean)rightData.elementAt(2)).booleanValue();
        boolean isRight = false;
      
        if(hasNegoModifyRight){
            negoMode=true;
            isRight = true;
        }else{
            if(hasNegoViewData){
                negoMode = false;
                isRight = true;
            }else{
                if(checkPi){
                    negoMode = false;
                    isRight = true;
                }else{
                 CoeusOptionPane.showInfoDialog(
                    coeusMessageResources.parseMessageKey(
                    "You do not have the right to view negotiations for this proposal")); 
                     isRight = false;
                }
            }
        }
        return isRight;
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
    
    /**
     * Code added for Case#3388 - Implementing authorization check at department level
     * To check the user is having rights to view this institute proposal
     * @param institute proposal number
     * @throws CoeusClientException
     * @return boolean
     */    
    private boolean canViewProposal(String instProposalNumber){
        boolean canView = false;
        RequesterBean request = new RequesterBean();
        request.setFunctionType(CAN_VIEW_INST_PROPOSAL);
        request.setDataObject(instProposalNumber);
        AppletServletCommunicator comm = new AppletServletCommunicator(connect, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response.isSuccessfulResponse()){
            canView = ((Boolean)response.getDataObject()).booleanValue();
        }
        return canView;
    }      
    
    //Added for COEUSQA-1525 : Attachments for Institute Proposal - Start
    /*
     * Gets the proposal attachments rights from server and sets it 
     * @param proposalNumber - String
     */
    public void fetchProposalAttachmentRights(String proposalNumber) throws Exception {
        boolean canUserMaintainAttachment = false;
        RequesterBean request = new RequesterBean();
        request.setFunctionType(GET_ATTACHMENT_RIGHTS);
        request.setDataObject(proposalNumber);
        AppletServletCommunicator comm = new AppletServletCommunicator(connect, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response.isSuccessfulResponse()) {
            Hashtable htAttacmentRights = (Hashtable)response.getDataObject();
            setUserCanMaintainAttachment(((Boolean)htAttacmentRights.get(Integer.valueOf(0))).booleanValue());
            setUserCanViewAttachment(((Boolean)htAttacmentRights.get(Integer.valueOf(1))).booleanValue());
        }else{
            throw new Exception(response.getMessage());
        }
    }
    
    /*
     * Method to check user can maintain attachment
     * @return userCanMaintainAttachment
     */
    private boolean isUserCanMaintainAttachment() {
        return userCanMaintainAttachment;
    }
    
    /*
     * Method to set whether user can maintain attachment
     * @param userCanMaintainAttachment
     */
    private void setUserCanMaintainAttachment(boolean userCanMaintainAttachment) {
        this.userCanMaintainAttachment = userCanMaintainAttachment;
    }
    
     /*
     * Method to check user view maintain attachment
     * @return userCanViewAttachment
     */   
    private boolean isUserCanViewAttachment() {
        return userCanViewAttachment;
    }
    
    /*
     * Method to set whether user can view attachment
     * @param userCanViewAttachment
     */
    private void setUserCanViewAttachment(boolean userCanViewAttachment) {
        this.userCanViewAttachment = userCanViewAttachment;
    }
    
    /*
     * Method to show institute proposal attachment window
     */
    public void showProposalAttachment()throws CoeusClientException,CoeusException, Exception{
        selectedRow = instituteProposalListForm.tblResults.getSelectedRow();
        String proposalNumber = getSelectedValue(selectedRow, "PROPOSAL_NUMBER");
        int sequenceNumber = Integer.parseInt(getSelectedValue(selectedRow,"SEQUENCE_NUMBER"));
        fetchProposalAttachmentRights(proposalNumber);
        if(isUserCanMaintainAttachment()){
            showAttachment(proposalNumber,sequenceNumber,TypeConstants.MODIFY_MODE);
        }else if(isUserCanViewAttachment()){
            showAttachment(proposalNumber,sequenceNumber,TypeConstants.DISPLAY_MODE);
        }else{
            CoeusOptionPane.showWarningDialog(coeusMessageResources.parseMessageKey(
                    CANNOT_OPEN_ATTACHMENT));
        }
    }
    
    public void showAttachment(String proposalNumber, int sequenceNumber,char functionType){
        try{
            mdiForm.checkDuplicate(CoeusGuiConstants.INSTITUTE_PROPOSAL_ATTACHMENT_FRAME_TITLE,
                    proposalNumber, functionType );
           
        }catch(Exception e){
            // Exception occured.  Record may be already opened in requested mode
            //   or if the requested mode is edit mode and application is already
            //   editing any other record.
            if(e.getMessage().length() > 0 ) {
                CoeusOptionPane.showInfoDialog(e.getMessage());
            }
            // try to get the requested frame which is already opened
            CoeusInternalFrame frame = mdiForm.getFrame(
                    CoeusGuiConstants.INSTITUTE_PROPOSAL_ATTACHMENT_FRAME_TITLE,proposalNumber);
            if(frame == null){
                // if no frame opened for the requested record then the
                //   requested mode is edit mode. So get the frame of the
                //   editing record.
                frame = mdiForm.getEditingFrame(
                        CoeusGuiConstants.PROPOSAL_NARRATIVE_FRAME_TITLE );
            }
            if (frame != null){
                try{
                    frame.setSelected(true);
                    frame.setVisible(true);
                }catch(Exception ex) {
                    ex.printStackTrace();
                }
            }
            return;
        }
        InstituteProposalAttachmentForm proposalAttachmentForm =
                new InstituteProposalAttachmentForm(mdiForm,proposalNumber, sequenceNumber,functionType);
        proposalAttachmentForm.showAttachmentForm();
    }
    //COEUSQA-1525 : End
}

