/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.propdev.gui;

import edu.mit.coeus.propdev.bean.ProposalYNQBean;
import edu.mit.coeus.routing.gui.RoutingForm;
import edu.mit.coeus.utils.question.bean.QuestionListBean;
import java.awt.Frame;
import javax.swing.JOptionPane;
import edu.mit.coeus.propdev.gui.ProposalRoutingForm;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentFormBean;
import edu.mit.coeus.gui.CoeusMessageResources;
import java.util.Vector;
//import edu.mit.coeus.propdev.bean.ProposalLeadUnitFormBean;
//import edu.mit.coeus.propdev.bean.ProposalInvestigatorFormBean;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.utils.*;

/**
 * ShowRouting.java
 *
 * @author  ranjeeva
 * Created on January 20, 2004, 3:22 PM
 */
public class ShowRouting {
    
    private Frame parent;
    private boolean modal;
    // Following Details are obtained from ProposalDetailForm
    /** ProposalDevelopmentFormBean instance to get Details of Proposal */
    private ProposalDevelopmentFormBean proposalDevelopmentFormBean;
    
    private ProposalDetailForm proposalDetailForm;
    
    /** ProposalDetailAdminForm instance to call validate methods for checking Proposal Entry*/
    private ProposalDetailAdminForm proposalDetailAdminForm;
    /** ProposalInvestigatorForm instance to call validate methods for checking Investigator Entry*/
    private ProposalInvestigatorForm proposalInvestigator;
    /** JTabbedPane instance to select the Tab in Proposal Entry*/
    private CoeusTabbedPane tbdPnProposal;
    
    // Following Details are obtained from ProposalDetailForm
    
    
    private char functionType;
//    private Vector vecProposalInvestigator;
//    private final char SAVE_RETAIN_LOCK = 'S';
    
    /** Holds CoeusMessageResources instance used for reading message Properties. */
    private CoeusMessageResources coeusMessageResources = CoeusMessageResources.getInstance();
    private static final char GET_PROP_ROUTING_DATA = 'P';
//    private static final char GET_PROP_ROUTING_DATA_FOR_APPROVE = 'Z';
    
    //Messsage Constants for all the Error Messages
//    private static final String ENTER_TITLE = "proposalSubmitValidation_exceptionCode.1130"; //"Enter the Title for the Proposal";
//    private static final String ENTER_SPONSOR = "proposalSubmitValidation_exceptionCode.1131"; //"Please enter a sponsor";
    private static final String ENTER_INVESTIGATOR = "proposalSubmitValidation_exceptionCode.1132"; //"Please enter a principal Investigator";
    private static final String PROPOSAL_IN_PROGRESS ="showRouting_exceptionCode.1133";// "Proposal Approval is in progress. Choose the 'Approve' menu item to view the proposal routing.";
//    private static final String SELECT_LEAD_UNIT =  "proposalSubmitValidation_exceptionCode.1134"; //"Please Select one Unit as the Lead Unit";
    private static final String NARRATIVE_INCOMPLETE = "proposalSubmitValidation_exceptionCode.1135"; //"The narrative is incomplete. Please update the status";
    private static final String BUDGET_INCOMPLETE = "proposalSubmitValidation_exceptionCode.1136"; //"The budget is incomplete. Please update the status";
    private static final String YESNO_QUESTION_INCOMPLETE = "proposalSubmitValidation_exceptionCode.1137"; //"Please answer the Yes/No Questions";
    private static final String ACTIVITY_TYPE_CHANGED = "showRouting_exceptionCode.1134";
    private static final char CHECK_BUDGET_VALIDATION = 'b';
    private static final String START_DATE_BUDGET_CHANGED = "showRouting_exceptionCode.1135"; //Start date of the budget should be between start and end dates of the proposal.Make changes to Proposal or Budget dates so that they are on sync
    private static final String END_DATE_BUDGET_CHANGED = "showRouting_exceptionCode.1136";
    
    String connectTo = CoeusGuiConstants.CONNECTION_URL + CoeusGuiConstants.PROPOSAL_ACTION_SERVLET;
    private boolean parentProposal;
    private static final char GET_ROUTING_DATA = 'P';
    private static final String NO_ROUTING_MAP = "showRouting_exceptionCode.1138"; //"No routing maps have been defined for this proposal";
    //Messsage Constants for all the Error Messages
    //Code added for Case#2785 - Protocol Routing
    private boolean errMsgDisplayed;
    
    //Added for COEUSQA-2400 : Proposal dev - routing window does not open when proposal is in Post Submission rejection staus
    private int PROPOSAL_IN_PROGRESS_STATUS = 1;
    private int PROPOSAL_IS_REJECTED_STATUS = 3;
    //COEUSQA-1433 - Allow Recall from Routing - Start
    private int PROPOSAL_IS_RECALLED_STATUS = 8;
    //COEUSQA-1433 - Allow Recall from Routing - End
    private int PROPOSAL_IN_POST_SUBMISSION_REJECTION_STATUS = 7;
    //Added for COEUSQA-2400 : Proposal dev - routing window does not open when proposal is in Post Submission rejection staus
    
    /** Creates a new instance of ShowRouting */
    public ShowRouting() {
        
    }
    
    /** Creates a new instance of ShowRouting
     * @param parent Component
     * @param parameters Vector containing all the necessary parameters required to bring the form
     * details
     * @param modal Model window when true
     * @throws Exception Exception to Base Class for prompting for details
     */
    
    public ShowRouting(Frame parent,Vector parameters, boolean modal) throws Exception {
        
        this.parent = parent;
        this.modal = modal;
        for(int index =0;index < parameters.size(); index++) {
            if(parameters.get(index).getClass() == ProposalDevelopmentFormBean.class) {
                this.proposalDevelopmentFormBean = (ProposalDevelopmentFormBean) parameters.get(index);
            }
            if(parameters.get(index).getClass() == ProposalDetailForm.class) {
                this.proposalDetailForm = (ProposalDetailForm) parameters.get(index);
            }
            if(parameters.get(index).getClass() == ProposalDetailAdminForm.class) {
                this.proposalDetailAdminForm = (ProposalDetailAdminForm) parameters.get(index);
            }
            
            if(parameters.get(index).getClass() == CoeusTabbedPane.class) {
                this.tbdPnProposal= (CoeusTabbedPane) parameters.get(index);
            }
            if(parameters.get(index).getClass() == String.class) {
                this.functionType =  ((String) parameters.get(index)).charAt(0);
            }
            
            if(parameters.get(index).getClass() == ProposalInvestigatorForm.class) {
                this.proposalInvestigator =  ((ProposalInvestigatorForm) parameters.get(index));
            }
            
        }
        
        //showRoutingWindow();
    }
    
    /** Setter method for setting ProposalDevelopmentFormBean
     * @param proposalDevelopmentFormBean ProposalDevelopmentFormBean instanace
     */
    public void setProposalDevelopmentFormBean(ProposalDevelopmentFormBean proposalDevelopmentFormBean) {
        this.proposalDevelopmentFormBean = proposalDevelopmentFormBean;
    }
    
    /** Getter method for getting ProposalDevelopmentFormBean
     * @return ProposalDevelopmentFormBean
     */
    public ProposalDevelopmentFormBean getProposalDevelopmentFormBean() {
        return proposalDevelopmentFormBean;
    }
    
    /** Setter method for setting ProposalDetailAdminForm
     * @param proposalDetailAdminForm ProposalDetailAdminForm inatance
     */
    
    public void setProposalDetailAdminForm(ProposalDetailAdminForm proposalDetailAdminForm) {
        this.proposalDetailAdminForm = proposalDetailAdminForm;
    }
    
    /** Getter method for getting ProposalDetailAdminForm
     * @return ProposalDetailAdminForm
     */
    
    public ProposalDetailAdminForm getProposalDetailAdminForm() {
        return proposalDetailAdminForm;
    }
    
    /** showRoutingWindow Constructor throwing Exception to ProposalDetailForm
     * @throws Exception Exception
     */
    public void showRoutingWindow() throws Exception {
        
         //Modified for COEUSQA-2400 : Proposal dev - routing window does not open when proposal is in Post Submission rejection staus
//        if( proposalDevelopmentFormBean.getCreationStatusCode() == 1
//                || proposalDevelopmentFormBean.getCreationStatusCode() == 3){
        //COEUSQA-1433 - Allow Recall from Routing - End
         //if( proposalDevelopmentFormBean.getCreationStatusCode() == PROPOSAL_IN_PROGRESS_STATUS  ||
         //       proposalDevelopmentFormBean.getCreationStatusCode() == PROPOSAL_IS_REJECTED_STATUS ){
         if( proposalDevelopmentFormBean.getCreationStatusCode() == PROPOSAL_IN_PROGRESS_STATUS  ||
                proposalDevelopmentFormBean.getCreationStatusCode() == PROPOSAL_IS_RECALLED_STATUS ||
                proposalDevelopmentFormBean.getCreationStatusCode() == PROPOSAL_IS_REJECTED_STATUS ){
             //COEUSQA-1433 - Allow Recall from Routing - End
             /*
                ||Commented earlier modification for
                Case#COEUSQA-2400 : Proposal dev - routing window does not open when proposal is in Post Submission rejection status
                as the user wanted 'Approval/Rejection' menu to be enabled and 'Show routing' menu to be disabled, hence commented 
                this status checking
                proposalDevelopmentFormBean.getCreationStatusCode() == PROPOSAL_IN_POST_SUBMISSION_REJECTION_STATUS){*/
        //COEUSQA-2400 : End
//Commented for case #2064 start
            //            if( functionType != TypeConstants.DISPLAY_MODE ){
//                if(validateProposalEntry()) {
//                    ProposalRoutingForm proposalRoutingForm = new ProposalRoutingForm(parent,proposalDevelopmentFormBean,GET_PROP_ROUTING_DATA,true);
//                    proposalRoutingForm.display();
//                }
//            }else
//            {
            //Commented for case 2785 Routing enhancements - start
//                ProposalRoutingForm proposalRoutingForm = new ProposalRoutingForm(parent,proposalDevelopmentFormBean,GET_PROP_ROUTING_DATA,true);
//                proposalRoutingForm.display();
            //Commented for case 2785 Routing enhancements - end
//            }
//Commented for case #2064 end
           //Added for case 2785 Routing enhancements - start
            boolean mapsFound = buildMapsForRouting(3, 
                    proposalDevelopmentFormBean.getProposalNumber(),
                    proposalDevelopmentFormBean.getOwnedBy(), true, "D" );
            if(mapsFound){
                RoutingForm proposalRoutingForm = new RoutingForm(
                                    proposalDevelopmentFormBean,
                                    3, proposalDevelopmentFormBean.getProposalNumber(),
                                    0, proposalDevelopmentFormBean.getOwnedBy(),false);
                            proposalRoutingForm.setShowRouting(true);
                            proposalRoutingForm.display();
            }else if(!errMsgDisplayed){
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(NO_ROUTING_MAP));
                errMsgDisplayed = false;                
            }
             //Added for case 2785 Routing enhancements - end
        } else {
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(PROPOSAL_IN_PROGRESS));
        }
        
    }
    
    /** This method is called in ProposalDetailForm after creating an instance of this form
     * It does the validation check on the Proposal Entry before opening the Show Routing Screen
     * @throws Exception Exception
     * @return true if validation success
     */
    //Modified for the case#COEUSQA-1679-Modification for Final Document Indicator-start
    //The isHierarchy is passed to find whether the parent is in hierarchy or not.
    //public boolean validateProposalEntry() throws Exception {
    public boolean validateProposalEntry(boolean isHierarchy) throws Exception {
        
        //Check for  Title and Sponsor entry is done in ProposalDetailAdminForm
        
        if(proposalDetailAdminForm.validateOtherDetails() == false) {
            tbdPnProposal.setSelectedIndex(0);
            return false;
        }
        
        // validating the data entered in ProposalDetailAdminForm
        if(!proposalDetailAdminForm.validateData()) {
            return false;
        }
        
        //Checking Investigator Entry if entered validated else prompt message
        if(proposalInvestigator.getFormData() != null && proposalInvestigator.getFormData().size() > 0) {
            proposalInvestigator.validateData();
        }else {
            tbdPnProposal.setSelectedIndex(3);
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(ENTER_INVESTIGATOR));
            return false;
        }
        //Added for the case # COEUSQA-1679-Modification for Final Document Indicator-start
        //Checking Narrative is Entered and Complete
        // Added by chandra for the bug fix #974 - start 29th June 2004
        //Added for the case # COEUSQA-1679-Modification for Final Document Indicator-end 
        if(isHierarchy){           
            if(proposalDevelopmentFormBean.getNarrativeStatus()!= null ){
                // Added by chandra for the bug fix #974 - end 29th June 2004
                if(proposalDevelopmentFormBean.getNarrativeStatus().trim().equalsIgnoreCase("I")) {
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(NARRATIVE_INCOMPLETE));
                    return false;
                }
            }
        //Added for the case # COEUSQA-1679-Modification for Final Document Indicator-start    
        }
        //Added for the case # COEUSQA-1679-Modification for Final Document Indicator-end
        //Checking Budget is Entered and Complete
        // Added by chandra for the bug fix #974 - start 29th June 2004
        if(proposalDevelopmentFormBean.getBudgetStatus()!= null){
            // Added by chandra for the bug fix #974 - End 29th June 2004.
            if(proposalDevelopmentFormBean.getBudgetStatus().trim().equalsIgnoreCase("I")) {
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(BUDGET_INCOMPLETE));
                return false;
            }
        }
        
        //Checking Any Question and If there Check it is Answered by looking at the Vector of Answer
        Vector vecPropYNQuestionList = proposalDevelopmentFormBean.getPropYNQuestionList();
        if(vecPropYNQuestionList != null && vecPropYNQuestionList.size() > 0) {
            Vector vecPropYNQAnswerList = proposalDevelopmentFormBean.getPropYNQAnswerList();
            //code modified for coeus4.3 YNQ enhancement 
//            if(!(vecPropYNQAnswerList != null && vecPropYNQAnswerList.size() > 0)) {
            // Modified for COEUSQA-2702 : YNQ blocker - Start
//            if(vecPropYNQAnswerList == null 
//                    || vecPropYNQAnswerList.size() != vecPropYNQuestionList.size()) {
//                tbdPnProposal.setSelectedIndex(0);
//                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(YESNO_QUESTION_INCOMPLETE));
//                return false;
//            }
//            tbdPnProposal.setSelectedIndex(0);
//            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(YESNO_QUESTION_INCOMPLETE));
//            return false;
            boolean canFireValidation = false;
            
            // JM 11-4-2011 added catch to not require YN questions for Pre-Application proposal type  
            if (proposalDevelopmentFormBean.getProposalTypeCode() != 2) {
            if(vecPropYNQAnswerList == null){
                canFireValidation = true;
                
            }else{
                for(Object questionBean : vecPropYNQuestionList){
                    QuestionListBean questionUsageBean = (QuestionListBean)questionBean;
                    String questionUsageId = questionUsageBean.getQuestionId();
                    boolean isQuestionExistInAnswerList = false;
                    for(Object answeredBean : vecPropYNQAnswerList){
                        ProposalYNQBean answeredQuestionBean = (ProposalYNQBean)answeredBean;
                        String answeredQuestionId = answeredQuestionBean.getQuestionId();
                        if(questionUsageId.equalsIgnoreCase(answeredQuestionId)){
                            if(answeredQuestionBean.getAnswer() == null ||
                                    CoeusGuiConstants.EMPTY_STRING.equals(answeredQuestionBean.getAnswer())){
                                canFireValidation = true;
                                break;
                            }
                            isQuestionExistInAnswerList = true;
                        }
                        
                    }
                    if(!isQuestionExistInAnswerList){
                        canFireValidation = true;
                        break;
                    }
                }
            }
            }
            // JM END
            if(canFireValidation){
                
                tbdPnProposal.setSelectedIndex(0);
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(YESNO_QUESTION_INCOMPLETE));
                return false;
            }
            // Modified for COEUSQA-2702 : YNQ blocker - End
            
        }
        
        //checks Budget Validation
        //Case 2545
        if(!isParentProposal()){
            if(!checkBudgetValidation()){
                return false;
            }
        }
        return true;
    }
    
    /**
     * Checks the Budget Validation by passing ProposalDevelopmentFormBean
     * and retriving thtrr flags if false then Message is poped up
     * First flag if start date is not with in Budget dates
     * Second flag if end date is not with in Budget dates
     * third flag indicated Activity Type code change
     */
    public boolean checkBudgetValidation() {
        
        SubmitForApprove submitForApprove = new SubmitForApprove();
        //CHECK_BUDGET_VALIDATION
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setFunctionType(CHECK_BUDGET_VALIDATION);
        
        System.out.println(proposalDevelopmentFormBean.getProposalNumber());
        System.out.println(proposalDevelopmentFormBean.getRequestStartDateInitial());
        System.out.println(proposalDevelopmentFormBean.getRequestStartDateInitial());
        
        requesterBean.setDataObject(proposalDevelopmentFormBean);
        ResponderBean  responderBean = submitForApprove.getDataFromServer(connectTo,requesterBean);
        Vector budgetCheckFlag = responderBean.getDataObjects();
        if(budgetCheckFlag != null && budgetCheckFlag.size() > 0) {
            
            
            if(((Boolean) budgetCheckFlag.get(2)).booleanValue()){
                int option = CoeusOptionPane.showQuestionDialog(coeusMessageResources.parseMessageKey(ACTIVITY_TYPE_CHANGED),CoeusOptionPane.OPTION_YES_NO,CoeusOptionPane.DEFAULT_YES);
                switch(option) {
                    case JOptionPane.YES_OPTION :
                        return true;
                    case JOptionPane.NO_OPTION:
                        return false;
                }
            }
            
            if(((Boolean) budgetCheckFlag.get(0)).booleanValue()){
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(START_DATE_BUDGET_CHANGED));
                return false;
            }
            if(((Boolean) budgetCheckFlag.get(1)).booleanValue()){
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(END_DATE_BUDGET_CHANGED));
                return false;
            }
            
            
        }
         return true;
    }
    
    /**
     * Getter for property parentProposal.
     * @return Value of property parentProposal.
     */
    public boolean isParentProposal() {
        return parentProposal;
    }    
    
    /**
     * Setter for property parentProposal.
     * @param parentProposal New value of property parentProposal.
     */
    public void setParentProposal(boolean parentProposal) {
        this.parentProposal = parentProposal;
    }    
     /** Method to get all the beans from the server */
    private boolean buildMapsForRouting(int moduleCode, String proposalNumber, String unitNumber,
            boolean buildMapRequired, String buildMapsOption ){
        boolean mapsFound = false;
        
        try {
            Vector vecRequestParameters = new Vector();
            vecRequestParameters.add(0, String.valueOf(moduleCode));
            vecRequestParameters.add(1, proposalNumber);
            vecRequestParameters.add(2, "0");
            vecRequestParameters.add(3, unitNumber);
            vecRequestParameters.add(4, new Boolean(buildMapRequired));
            vecRequestParameters.add(5, buildMapsOption);
            if(proposalDevelopmentFormBean.getRoutingBean()!=null){
                vecRequestParameters.add(6, new Integer(proposalDevelopmentFormBean.getRoutingBean().getApprovalSequence()));
            }else{
                vecRequestParameters.add(6, null);
            }
            RequesterBean requester = new RequesterBean();
            requester.setFunctionType(GET_ROUTING_DATA); //GET_PROP_ROUTING_DATA
            requester.setDataObjects(vecRequestParameters);
            String CONNECTION_STRING = CoeusGuiConstants.CONNECTION_URL + "/RoutingServlet";
            AppletServletCommunicator comm = new AppletServletCommunicator(CONNECTION_STRING, requester);
            comm.send();
            ResponderBean response = comm.getResponse();
            Vector vecData = new Vector();
            
            if ( response != null ){
                if( response.isSuccessfulResponse() ){
                    vecData = response.getDataObjects();
                     try {
                         if(vecData != null && vecData .size() > 0) {
                            int buildMapID = 0;
                            if(vecData.get(0).getClass() == Integer.class) {
                                buildMapID = ((Integer) vecData.get(0)).intValue();
                                if(buildMapID > 0){
                                    mapsFound = true;
                                }
                            }
                         }
                    }catch(Exception exp) {
                        exp.printStackTrace();
                    }
                //Code added for case 2785 Routing enhancements - start
                } else if(response.getException() != null && response.getMessage() != null 
                        && response.getMessage().length() > 0){
                    CoeusOptionPane.showErrorDialog(response.getMessage());
                    errMsgDisplayed = true;
                }
                //Code added for case 2785 Routing enhancements - ends
            }
            
        }catch(Exception exp) {
            exp.printStackTrace();
        }
        return mapsFound;
    }
    
}
