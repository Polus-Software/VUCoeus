/*
 * @(#)ProposalDetailForm.java 1.0 13/03/03
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
/*
 * PMD check performed, and commented unused imports and variables on 19-APR-2011
 * by Maharaja Palanichamy
 */

package edu.mit.coeus.propdev.gui;

import edu.mit.coeus.gui.*;
import edu.mit.coeus.mail.controller.ActionValidityChecking;
//import edu.mit.coeus.mailaction.bean.MailActionInfoBean;
import edu.mit.coeus.propdev.bean.*;
import edu.mit.coeus.gui.toolbar.CoeusToolBarButton;
import edu.mit.coeus.gui.menu.CoeusMenuItem;
import edu.mit.coeus.gui.menu.CoeusMenu;
import edu.mit.coeus.gui.menu.CoeusFileMenu;
import edu.mit.coeus.routing.gui.RoutingForm;
//import edu.mit.coeus.routing.gui.RoutingValidationChecksForm;
import edu.mit.coeus.user.gui.UserDelegationForm;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.utils.tree.UserRoleNodeRenderer;
import edu.mit.coeus.user.gui.UserPreferencesForm;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.bean.*;
//import edu.mit.coeus.organization.bean.OrganizationAddressFormBean;
//import edu.mit.coeus.rolodexmaint.bean.RolodexDetailsBean;
import edu.mit.coeus.budget.controller.SelectBudgetController;
//import edu.mit.coeus.propdev.gui.MedusaDetailForm;
//import edu.mit.coeus.businessrules.bean.BusinessRulesBean;
//import edu.mit.coeus.propdev.bean.ProposalAwardHierarchyLinkBean;

import edu.mit.coeus.s2s.bean.ProcessGrantsSubmission;
import edu.mit.coeus.s2s.bean.S2SHeader;
import edu.mit.coeus.s2s.bean.SubmissionDetailInfoBean;
import java.text.MessageFormat;


import javax.swing.*;
import javax.swing.event.*;
//import javax.swing.table.DefaultTableModel;
import java.beans.*;

import java.awt.*;
import java.awt.event.*;

import java.util.*;
//import edu.mit.coeus.utils.BaseWindowObservable;
import edu.mit.coeus.exception.*;
import edu.mit.coeus.instprop.gui.InvestigaorListForm;
import edu.mit.coeus.irb.bean.ProtocolFundingSourceBean;
import edu.mit.coeus.irb.bean.ProtocolInfoBean;
import static edu.mit.coeus.propdev.gui.ProposalInvestigatorForm.connect;
import edu.mit.coeus.utils.query.*;
//import edu.mit.coeus.propdev.gui.PrintProposal;
import edu.mit.coeus.s2s.bean.OpportunityInfoBean;
import edu.mit.coeus.search.gui.CoeusSearch;
import edu.mit.coeus.questionnaire.bean.QuestionnaireAnswerHeaderBean;
//import edu.mit.coeus.questionnaire.controller.QuestionAnswersController;
import edu.mit.coeus.questionnaire.controller.QuestionnaireAnswersBaseController;
import edu.mit.coeus.questionnaire.gui.QuestionnaireAnswersBaseForm;
import edu.mit.coeus.s2s.controller.S2SErrorFormController;
import edu.mit.coeus.s2s.controller.UserAttachedS2SFormsController;
import edu.mit.coeus.s2s.validator.S2SValidationException;
import edu.mit.coeus.search.gui.ProposalSearch;
//import edu.mit.coeus.propdev.gui.ProposalPrintForm;
//import edu.mit.coeus.s2s.bean.ProcessGrantsSubmission;
//import edu.mit.coeus.s2s.bean.S2SSubmissionStatusBean;
//import edu.mit.coeus.s2s.bean.S2SXMLInfoBean;
//import edu.mit.coeus.s2s.validator.S2SValidationException;
//import edu.mit.coeus.s2s.validator.UniqueSchemaNotFoundException;

//import edu.mit.coeus.utils.ChangePassword;
//import edu.mit.coeus.propdev.gui.SpecialReviewForm;
import edu.ucsd.coeus.personalization.controller.AbstractController;
import java.io.IOException;

/* 4-23-2015 added for custom functionality */
import edu.mit.coeus.utils.locking.LockingBean;
import edu.vanderbilt.coeus.gui.CoeusHelpGidget;
import edu.vanderbilt.coeus.utils.CustomFunctions;
/* JM END */

/**
 * The class is used to maintain the Proposal details.
 * This will be invoked from the <CODE>ProposalBaseWindow</CODE> in three different modes,
 * Add, Modify and Display. The enabled status of all the components will change
 * according to the mode of operation.
 *
 * @author  subramanya
 * Created on March 13, 2003, 10:49 AM
 * @updated Subramanya
 * Description : Updated Functionality for Previous/Next button in View Mode.
 */
public class ProposalDetailForm extends CoeusInternalFrame
implements ActionListener,TypeConstants, Observer, ChangeListener{

    /** Added by chandra
     */
    static final String connect = CoeusGuiConstants.CONNECTION_URL +
    "/ProposalActionServlet";
    private SpecialReviewForm specialReviewForm;
    private static final String HIERARCHY_SERVLET = "/ProposalHierarchyServlet";
    private static final String S2S_SERVLET = CoeusGuiConstants.CONNECTION_URL + "/S2SServlet";
    // check for the establishing proposal hierarchy relationship
    private static final char CAN_BUILD_HIERARCHY = 'D';
    // Establish create proposal hierarchy action
    private static final char CREATE_PARENT_PROPOSAL = 'E';
    // For joining to the link
    private static final char JOIN_PROPOSAL = 'H';
    private static final char SYNC_ALL_PROPOSAL = 'I';
    //private static final char CHECK_LOCK_FOR_SYNC_ALL = 'K';
    private static final char REMOVE_PROP_FROM_HIERARCHY = 'O';
    private static final char REFRESH_HIERARCHY = 'P';

    private static final String CREATE_ACTION = "create";
    private static final String JOIN_ACTION = "join";
    private boolean parentProposal;
    private boolean hierarchy;
    private String parentPropNo = "";
    private JScrollPane scrPnHierarchy = null;
    private boolean parameterExist;
    private String instPropNumber = "";
    private static final String generate = "G";
    private static final String submissionType = "P";
    private static final String submissionStatus = "S";
    private static final char FEED_INST_NUMBER = 'U';
    /* End Chandra
     */
    private static final String MAINTAIN_PROPOSAL_ACCESS = "MAINTAIN_PROPOSAL_ACCESS";
    private final char SAVE_RETAIN_LOCK = 'S';

    private final char SAVE_RELEASE_LOCK = 'R';

    private final char ROW_UNLOCK_MODE = 'Z';
    private final char MAINTAIN_PERSON_CERTIFICATION ='x';
    public static String PROPOSAL_ID;
    private final char NOTIFY_PROPOSAL_PERSONS ='w';
    private final String USER_HAS_PROPOSAL_ROLE ="USER_HAS_PROP_ROLE";

//    private final String USER_HAS_OSP_RIGHT ="USER_HAS_OSP_RIGHT";

    private final String APPROVE_PROPOSAL_RIGHT ="APPROVE_PROPOSAL";
    //Added for Case#3587 - multicampus enhancement  - Start
    private final String USER_HAS_DEPARTMENTAL_RIGHT = "USER_HAS_DEPARTMENTAL_RIGHT";
    //Case#3587 - End


    private final int PROPOSAL_ROLE = 101;

    // private CoeusVector cvTempData;

    //holds the proposal ID.
    public String proposalID;

//    private int viewOpenWindowCount = 0;

    //Code Added by Vyjayanthi on 27/01/2004 - Start
    private boolean hasSubmitForApprovalRt = false;
    private boolean hasSubmitToSponsorRt = false;
    private boolean hasAddViewerRt = false;
    //Code Added by Vyjayanthi on 27/01/2004 - End

    //==Added for Different functiontype for getting data from server
//    private static final char GET_PROP_ROUTING_DATA = 'P';
//    private static final char GET_PROP_ROUTING_DATA_FOR_APPROVE = 'Z';
    //==Added for Different functiontype for getting data from server

    //Added during PMD check on 24/09/2007 -Start
//    private static final String PROPOSAL_DETAIL_FORM = "ProposalDetailForm";
    //Added during PMD check on 24/09/2007 - End
    private CoeusMenuItem narrative,budget,/*specialReview*/yesNoQuestion,propRoles,
    abstracts, propPersonnel, notepad, dataOverride, medusa, create, join, remove,approve,
    submitApproval, submitSponsor,submitGrantsGov, showRouting,addViewer, approvalMap,/* notify,*/
    certifications,notification,disclosurestatus, validationChecks, nextProposal, previousProposal, print,
    inbox /*,printSetup*/ ,close,save,changePass,pref,mnuItemExit,
    /*Added for Case#3682 - Enhancements related to Delegations - Start*/
    delegations,
    /*Added for Case#3682 - Enhancements related to Delegations - End*/
    /*Added new Menu Item for current Lock start1 */mnuItemCurrentLocks,
    /*Added by tarique for sync menu item */mnuItmSyncAll, mnuItmQuestions,
    /*Added for case #2371*/mnuItmCurPendReport;//modified for bug fix 1651

    /*Added for COEUSQA-4066*/
    private CoeusMenuItem userAttachedS2SForms;
    
    private CoeusMenuItem humnSubS2SForms;

    private CoeusMenu localFileMenu, editMenu,actionsMenu;
    private CoeusToolBarButton btnNextProposal,btnPrevProposal,btnApproveProposal,
    btnSubmitProposal,btnApprovalMap, /*btnProposalNotification,*/
    btnProposalNarrative, btnBudget, /*btnSpecialReview,*/ btnYesNoQues, btnRoles,
    btnAbstracts, btnPersons, btnNotepad, btnMedusa, btnPrint, btnSave,btnSyncAll, btnClose;
    //Added for the case#COEUSQA-1679-Modification for Final Document Indicator-start
//    private edu.mit.coeus.routing.gui.RoutingApprovalForm routingApprovalForm;

    /* JM 5-17-2016 new button to display proposal number which this proposal was copied from;
     * 		5-25-2016 questionnaire, validations, and GG buttons */
    private CoeusToolBarButton btnCopiedFromPropNum, btnQuestionnaire, btnValidations, btnGG;
    /* JM END */
    
    Vector proposalStatus = new Vector();
    Vector proposalTypes = new Vector();
    Vector proposalNSFCodes = new Vector() ;
    Vector proposalActivityTypes = new Vector() ;
    Vector proposalNoticeOpp = new Vector() ;
    Vector investigatorQusestion = new Vector();
    Vector proposalRoleRights = new Vector();
    Vector propDefaultUsers = new Vector();
    Vector propUserRoles = new Vector();
    Hashtable qstExplanation = new Hashtable();
    // Added for Case 2162  - adding Award Type - Start
    Vector vecAwardTypes = new Vector();
    // Added for Case 2162  - adding Award Type - End
    //Added for case 2406 - Organizations and Locations - start
    Vector vecLocationTypes;
    //Added for case 2406 - Organizations and Locations - end
    HashMap hierarchyMap = new HashMap();
    ProposalHierarchyBean proposalHierarchyBean = null;
    ProposalHierarchyContoller hierarchyController = null;

    Vector vecPropYNQQuestionList = new Vector();
    Vector vecPropYNQAnswerList;
    Hashtable htPropYNQExplanation = new Hashtable();

    Vector propPersons;

    Vector propSpecialReviewCodes = new Vector();
    Vector propReviewApprovalTypes = new Vector();
    Vector propValidSpecialReviewList = new Vector();
    //For the Coeus Enhancements case:#1799 start step:1
    CoeusVector cvParameters = new CoeusVector();
    //End Coeus Enhancement case:#1799 step:1

    Vector vecSpecialReviewData;
    Vector vecSplRevReplicateData;

    private Vector tempUserRoles;

    private Vector userRoles = new Vector();

    //holds the modifiable flag
    private boolean modifiable = true;

    //hold the function type for this window.
    private char functionType;

    //holds the reference for mdi form
    private CoeusAppletMDIForm mdiForm = null;


    //holds the proposal detail component
    //made public for COEUSQA-2749
    public ProposalDetailAdminForm proposalDetail = null;

    //holds the proposal organization component
    private ProposalOrganizationAdminForm proposalOrganization = null;
    private ProtocolInfoBean protocolBean = null;
    private  DateUtils dtUtils;

    //holds the proposal organization address form bean
//    private OrganizationAddressFormBean organizationAddressFormBean;

    //holds the performing organization info bean
//    private OrganizationAddressFormBean performingOrganizationInfo;

    //holds the Rolodex bean which contains the details corresponding to the
    //contact address ID in the OrganizationAddressFormBean
//    private RolodexDetailsBean rolodexDetailsBean;

    //holds the proposal mailing info component
    private ProposalMailingInfoAdminForm proposalMailingInfo = null;

    //holds the proposal investigator component
    private ProposalInvestigatorForm proposalInvestigator = null;
    private static final char GET_APP_HOME_URL = 'o';  
    // holds the medusa details
    private MedusaDetailForm medusaDetailform = null;

    // Holdds the Sponsor window
//    private SubmitToSponsor submitToSponsor=null;

    //holds the proposal key person component
    //private ProposalKeyStudyPersonnelAdminForm proposalKeyPerson = null;

    //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - Start
    //holds the proposal key person bean
    private Vector allKeyStudyPersonnel;
    //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - End

    //change for the add unit section with the updated util package.
    private ProposalKeyPersonController proposalKeyPersonController;


    //change for the add unit section with the updated util package ends.....



    //Added for the Coeus Enhancement case:#1823 start step:1 ,holds special review component
    private ProposalSpecialReviewForm proposalSpecialReview = null;
    //End Coeus Enhancement case:#1823 step:1

    //holds the proposal science code component
    private ProposalScienceCodeAdminForm proposalScienceCode = null;

    //holds the proposal other component
    private ProposalOtherElementsAdminForm proposalOther = null;

    private ProposalYesNoQuestionsForm proposalYesNoQuestionsForm = null;

    //    private UserRolesMaintenance rolesForm;
    private ProposalUserRoleMaintainance rolesForm;
    //holds CoeusMessageResources instance used for reading message Properties.
    private CoeusMessageResources coeusMessageResources;

    //holds the proposal detail form bean
    private ProposalDevelopmentFormBean proposalDevelopmentFormBean = null;

    //holds the Tabbed pane of proposal screens
    private CoeusTabbedPane tbdPnProposal = null;

    //holds the save required flag
    private boolean saveRequired = false;

    private JTable tblProposal;
    /*
     * to indicate horizondal seperator in menu items
     */
    private final String SEPERATOR="seperator";

    private Vector keyStudyPersonnel,investigators,specialReviewData/*For the Coeus Enhancement case:#1823 step:2*/,scienceCodeData,otherData,keyStudyPersonnelUnits;

    private boolean propTab, propOrgTab, propMailTab, propInvTab, propKeyTab,propSpecialRevTab,//Added for the Coeus Enhancement case:#1823 step:3
    propScienceTab,propOthersTab,propRoleSaveReq; //propYNQ;
    private String unitNumber, unitName;

    //holds the reference of the value in Proposal Base Window
    //    private Integer displaySheetCount;

    //Stores the RIGHT ID which is required for checking user rights to modify narrative
    private final String MODIFY_NARRATIVE_RIGHT="MODIFY_NARRATIVE";

//    private static final int IN_PROGRESS_STATUS_CODE = 1;

//    private static final int REJECTED_STATUS_CODE = 3;

    //Stores the RIGHT ID which is required for checking user rights to modify proposal
    private final String MODIFY_PROPOSAL_RIGHT="MODIFY_PROPOSAL";
    //Added by Shiji for right checking - step 1 : start
    private final String MODIFY_ANY_PROPOSAL_RIGHT = "MODIFY_ANY_PROPOSAL";
    //right checking - step 1 : end

    //Stores the RIGHT ID which is required for checking user rights to modify budget
    private final String MODIFY_BUDGET_RIGHT="MODIFY_BUDGET";
 private static final char GET_PARAMETER_VALUE='y';


    private CoeusFileMenu fileMenu;

    private String PIName="";

    private Hashtable statusDescription;

    private CoeusDlgWindow dlgRoles;
    Vector existingUserRoles = new Vector(10,10) ;
    ProposalNarrativeForm narrativeForm;
    ProposalPersonnelForm proposalPersonnelForm;
    private BaseWindowObservable observable = new BaseWindowObservable();

    //holds an instance of the QueryEngine
//    private QueryEngine queryEngine;    //Added by Vyjayanthi

    private boolean narrativeStatusUpdated;
    private char narrativeStatus;

    //Added for Narrative User Rights - start
    private CoeusVector narrativeUserRights;
    //Added for Narrative User Rights - end

    //boolean used when user wants to discard the changes made and to close the screen.
    private boolean discardData;

    private ChangePassword changePassword;

    private UserPreferencesForm userPreferencesForm ;
    //Added for Case#3682 - Enhancements related to Delegations - Start
    private UserDelegationForm userDelegationForm;
    //Added for Case#3682 - Enhancements related to Delegations - End
    // Case 1769 -start
    private static final String ALTER_PROPOSAL_DATA = "ALTER_PROPOSAL_DATA";
    private static final char CHECK_SPECIAL_REVIEW_RIGHT =  'c';
    private static final char GET_LOCK_FOR_SPECIAL_REVIEW = 'r';
    //Update the parent proposal status
    private static final char UPDATE_CHILD_STATUS = 'z';
    private boolean isSpecialReviewRight;
    private boolean checkLock;
    private boolean rightExist;
    private boolean  isSpecialReviewLocked;
    private int propsoalStatusCode;
    // Get the search refrence for the Next and Pervious action
    private ProposalSearch baseSearch;

    //case #1769 - End
    //Added for case id : 3183 - start
    private static final char CAN_LINK_TO_HIERARCHY = 'R';
    //Added for case id : 3183 - end
    //Proposal Hierarchy Messages
    private static final String SYNC_MESSAGE = "propHierarchy_exceptionCode.1001";
    private static final String SYNC_ERROR = "propHierarchy_exceptionCode.1002";
    private static final String SPONSOR_NOT_SAME = "propHierarchy_exceptionCode.1003";
    //private static final String LINKING_ABORT = "propHierarchy_exceptionCode.1004";
    private static final String CANNOT_JOIN_ITSELF = "propHierarchy_exceptionCode.1004";
    private static final String JOIN_ERROR = "propHierarchy_exceptionCode.1005";
    private static final String CREATE_ERROR = "propHierarchy_exceptionCode.1006";
    private static final String ALREADY_LINKED = "propHierarchy_exceptionCode.1007";
    private static final String STATUS_VALIDATION = "propHierarchy_exceptionCode.1008";
    private static final String NO_BUDGET = "propHierarchy_exceptionCode.1009";
    private static final String INVESTIGATOR_VALIDATION = "propHierarchy_exceptionCode.1010";
    private static final String BUDGET_VER_VALIDATION = "propHierarchy_exceptionCode.1011";
    private static final String SELECT_PROP_NOT_IN_HIERARCHY = "propHierarchy_exceptionCode.1012";
    private static final String SELECT_PARENT_PROP_TO_JOIN = "propHierarchy_exceptionCode.1013";
    private static final String REMOVE_FROM_HIERARCHY_MESSAGE = "propHierarchy_exceptionCode.1014";
    private static final String NO_RIGHT_TO_JOIN = "propHierarchy_exceptionCode.1015";
    //Added for case#3183 - Proposal hierarchy
    private static final String BUDGET_STATUS_COMPLETE_LINK = "propHierarchy_exceptionCode.1021";
    private static final String BUDGET_STATUS_COMPLETE_REMOVE = "propHierarchy_exceptionCode.1022";
    //Added for the case # COEUSQA-1679-Modification for Final Document Indicator-start
    private static final String NARRATIVE_INCOMPLETE = "proposalSubmitValidation_exceptionCode.1140"; //"The narrative is incomplete. Please update the status";
    //Added for case#3230 - Display of Proposal Role Form based on rights
    private static final char USER_HAS_RIGHT = 'U';
    private static final char CHECK_USER_HAS_RIGHT = 'b';
    //Added for Case#2214 Email enhancement
    //Added with case 2158: Budget Validations : Start
    private static final char CHECK_BUDGET_VERSION = 'y';
    private static final char VALIDATION_ERROR   = 'E';
    private static final char VALIDATION_WARNING = 'W';
    private static final char GRANTSGOV_ERROR    = 'G';
    private static final String ERRKEY_SELECT_FINAL_BUDGET = "validationChecks_exceptionCode.1905";
    private static final String CONFIRM_SYNCING = "validationChecks_exceptionCode.1906";
    //2158 End
    private CoeusToolBarButton sendEmail;

   //Case :#3149 Tabbing between fields does not work on others tabs - Start
    private int row = 0 ;
    private int column =0;
    private JTable customTable = null;
    private int count = 1;
    //Case :#3149 - End
    //Added for case 2406 - Organization and Location - start
    public static int ORGANIZATION_TAB = 1;
    private CoeusVector cvSites;
    //Added for case 2406 - Organization and Location - end
    //JIRA COEUSDEV-61 - START - 1
    private OpportunityInfoBean opportunityInfoBeanForGG;
    //JIRA COEUSDEV-61 - END - 1
    // 4272: Maintain History of Questionnaires
private static final char CHECK_QUESTIONNAIRE_COMPLETED = 'b';

    /** Creates a new instance of ProposalDetailForm */
    //COEUSDEV-197:Budget validation rules are NOT applied when a proposal is linked to a hierarchy and creating new hierarchy
    private static final String CONFIRM_LINK = "validationChecks_exceptionCode.1907";
    private static final String CONFIRM_CREATE_PARENT = "validationChecks_exceptionCode.1908";
    private static final char HIERARCHY_VALIDATION_CHECKS = 'S';
    //COEUSDEV-197:End
    //Added for COEUSQA-2400 : Proposal dev - routing window does not open when proposal is in Post Submission rejection staus
    private static final int PROPOSAL_IN_PROGRESS_STATUS = 1;
    private static final int PROPOSAL_IS_REJECTED_STATUS = 3;

    private static final char PROPOSAL_CREATION_STATUS_DETAILS = '3';
    private static final int PROPOSAL_IN_POST_SUBMISSION_REJECTION_STATUS = 7;
    //COEUSQA-2400 : End
    // Added for COEUSQA-2778-Submited Prop to Sponsor-Narrative Incomplete - Start
    private static final char GET_NARRATIVE_STATUS = '1';
    private static final String PROPOSAL_NARRATIVE_STATUS = "I";
    // Added for COEUSQA-2778-Submited Prop to Sponsor-Narrative Incomplete - End

    //COEUSQA-1433 - Allow Recall from Routing - Start
    private final String PROPOSAL_SERVLET = "/ProposalMaintenanceServlet";
    private static final int PROPOSAL_IN_RECALLED_STATUS = 8;
    private static final char PROPOSAL_RECALL_LOCK_CHECK = '2';
    //COEUSQA-1433 - Allow Recall from Routing - Start
    
    //Added for COEUSDEV-1075 : Locking messages inconsistency between Lite and Premium - start
    private static final String GET_PARAMETER_VALUE_ROUTING = "GET_PARAMETER_VALUE";
    String propID;
    //Added for COEUSDEV-1075 : Locking messages inconsistency between Lite and Premium - end
    //ppc certify flag for key person starts
    private static final String ENABLE_PROP_PERSON_SELF_CERTIFY="ENABLE_PROP_PERSON_SELF_CERTIFY";
    //ppc certify flag for key person ends
	
	private static final char CHECK_IS_PHS_HS_CT_FORM = '4';

    // JM 4-15-2015 added APPROVAL_IN_PROGRESS and APPROVED status variables
    private static final int APPROVAL_IN_PROGRESS = 2;
    // JM END

    public ProposalDetailForm( char fnType, String propID,
    CoeusAppletMDIForm mdiForm ) {
        super( CoeusGuiConstants.PROPOSAL_DETAILS_FRAME_TITLE
        + ( (propID != null && propID.length() > 0  && fnType != COPY_MODE)
        ? " - " + propID : "" ), mdiForm );
        this.functionType = fnType;
        this.mdiForm = mdiForm;
        this.proposalID = propID;
        SpecialReviewForm.removeInstance();
        this.setFrameToolBar(getToolBar()); //Need to be Implemented
        this.setToolsMenu(null);
        this.setFrameMenu(getEditMenu());
        setFrameIcon(mdiForm.getCoeusIcon());
        hackToGainFocus();
        fileMenu = mdiForm.getFileMenu();
    }

    private void hackToGainFocus() {
        JFrame frame = new JFrame();
        frame.setLocation(-200,100);
        frame.setSize( 100, 100 );
        frame.show();
        frame.dispose();
        //this.dispatchEvent(new InternalFrameEvent(this,InternalFrameEvent.INTERNAL_FRAME_ACTIVATED));
    }

    //Added by Vyjayanthi
    /** Enable/Disable the menu items or buttons based appropriate conditions */
    private void enableDisableMenuItems(){
        if( proposalDevelopmentFormBean == null ){
            return ;
        }

        //Check for Approve menu item
        //Code modified for Case#3612 - Parallel Routing and Show Routing implementation
        //If the status is Rejected then disable the Approve menu item
//        if( proposalDevelopmentFormBean.getCreationStatusCode() == 1 ){
        //Modified for COEUSQA-2400 : Proposal dev - routing window does not open when proposal is in Post Submission rejection staus
//        if( proposalDevelopmentFormBean.getCreationStatusCode() == 1
//                || proposalDevelopmentFormBean.getCreationStatusCode() == 3){
        //COEUSQA-1433 - Allow Recall from Routing - Start
        /*if( proposalDevelopmentFormBean.getCreationStatusCode() == PROPOSAL_IN_PROGRESS_STATUS  ||
                proposalDevelopmentFormBean.getCreationStatusCode() == PROPOSAL_IS_REJECTED_STATUS){*/
          if( proposalDevelopmentFormBean.getCreationStatusCode() == PROPOSAL_IN_PROGRESS_STATUS  ||
                proposalDevelopmentFormBean.getCreationStatusCode() == PROPOSAL_IS_REJECTED_STATUS ||
                proposalDevelopmentFormBean.getCreationStatusCode() == PROPOSAL_IN_RECALLED_STATUS){
        //COEUSQA-1433 - Allow Recall from Routing - End
                /*||Commented earlier modification for
                Case#COEUSQA-2400 : Proposal dev - routing window does not open when proposal is in Post Submission rejection status
                as the user wanted 'Approval/Rejection' menu to be enabled and 'Show routing' menu to be disabled, hence commented
                this status checking
                proposalDevelopmentFormBean.getCreationStatusCode() == PROPOSAL_IN_POST_SUBMISSION_REJECTION_STATUS){*/
        //COEUSQA-2400 : End
            approve.setEnabled(false);
            btnApproveProposal.setEnabled(false);
        }else{
            approve.setEnabled(true);
            btnApproveProposal.setEnabled(true);
        }
        //Added for Case#COEUSQA-2400 : Proposal dev - routing window does not open when proposal is in Post Submission rejection status_Start
        if(proposalDevelopmentFormBean.getCreationStatusCode() == PROPOSAL_IN_POST_SUBMISSION_REJECTION_STATUS){
            showRouting.setEnabled(false);
        }
        //Added for Case#COEUSQA-2400 : Proposal dev - routing window does not open when proposal is in Post Submission rejection status_End

        //Check for Submit for Approval menu item
        if( functionType == TypeConstants.DISPLAY_MODE ){
            submitApproval.setEnabled(false);
            btnSubmitProposal.setEnabled(false);
        }else{
            //Check for right
           // Code Added for Proposal Hierarchy case#3183 - Start
           // Enable Submit for Approval button and Menu, if it is Parent proposal in Hierarchy
            if( !hasSubmitForApprovalRt && !isParentProposal()){
                submitApproval.setEnabled(false);
                btnSubmitProposal.setEnabled(false);
            }else{
                submitApproval.setEnabled(true);
                btnSubmitProposal.setEnabled(true);
            }  // Code Added for Proposal Hierarchy case#3183 - End
            //Enable the menu in Add mode
            if( functionType == TypeConstants.ADD_MODE ){
                hasSubmitForApprovalRt = true;
                submitApproval.setEnabled(true);
                btnSubmitProposal.setEnabled(true);
            }
        }

        //Check for Submit To Sponsor menu item
        if( hasSubmitToSponsorRt ){
            submitSponsor.setEnabled(true);
        }else{
            submitSponsor.setEnabled(false);
        }

        //Check for Add Viewer
        //Modified for COEUSQA-2400 : Proposal dev - routing window does not open when proposal is in Post Submission rejection staus
//        if( proposalDevelopmentFormBean.getCreationStatusCode() == 1
//                || proposalDevelopmentFormBean.getCreationStatusCode() == 3){
        //COEUSQA-1433 - Allow Recall from Routing - Start
        /*if( proposalDevelopmentFormBean.getCreationStatusCode() == PROPOSAL_IN_PROGRESS_STATUS  ||
                proposalDevelopmentFormBean.getCreationStatusCode() == PROPOSAL_IS_REJECTED_STATUS ||
                proposalDevelopmentFormBean.getCreationStatusCode() == PROPOSAL_IN_POST_SUBMISSION_REJECTION_STATUS){*/
          if(proposalDevelopmentFormBean.getCreationStatusCode() == PROPOSAL_IN_PROGRESS_STATUS  ||
                proposalDevelopmentFormBean.getCreationStatusCode() == PROPOSAL_IS_REJECTED_STATUS ||
                proposalDevelopmentFormBean.getCreationStatusCode() == PROPOSAL_IN_RECALLED_STATUS ||
                proposalDevelopmentFormBean.getCreationStatusCode() == PROPOSAL_IN_POST_SUBMISSION_REJECTION_STATUS){
            //COEUSQA-1433 - Allow Recall from Routing - End
            //COEUSQA-2400 : End
            addViewer.setEnabled(false);

        }else{
            //Check if user has proposal role
            if( !hasAddViewerRt ){
                //Check if user has OSP right
                boolean hasOSPRight = false;
                if( !hasOSPRight ){
                    addViewer.setEnabled(false);
                }else{
                    addViewer.setEnabled(true);
                }
            }else{
                addViewer.setEnabled(true);
            }
            //Added for show routing
            showRouting.setEnabled(false);
            //Added for show routing
        }

        //Check for DataOverride
//        if( proposalDevelopmentFormBean.getCreationStatusCode() != 2 &&
//        proposalDevelopmentFormBean.getCreationStatusCode() != 4){
//            dataOverride.setEnabled(false);
//        }else{
//            dataOverride.setEnabled(true);
//        }
                   try
        {
           boolean enableCertification=fetchParameterValue();
           if(enableCertification){

                    certifications.setEnabled(false);
                }else{
                    certifications.setEnabled(true);
                }
        }catch(Exception e){}




             try
        {
             boolean enablenotifications=fetchParameterValue();
                  {
                      if(enablenotifications){
                notification.setEnabled(false);
           boolean enableNotification=getNotifyProposalPersonRight();

           if(enableNotification!=true){
                    notification.setVisible(true);
                    notification.setEnabled(false);
                }else{
               notification.setVisible(true);
                  notification.setEnabled(true);
                }
 //Added for COEUSDEV-736:
 //This code is added to disable Send Notification  button if the proposal is a child proposal.
        if(hierarchyMap!= null)
        {
            boolean inHierarchy = ((Boolean)hierarchyMap.get("IN_HIERARCHY")).booleanValue();
            boolean isParent = false;
            if(inHierarchy) {
                isParent = ((Boolean)hierarchyMap.get("IS_PARENT")).booleanValue();
                if(!isParent){notification.setEnabled(false);}
            }
        }
//Added for COEUSDEV-736: to disable Send Notification  button end.
        }

                  }

             }catch(Exception e){}

        if( proposalDevelopmentFormBean.getCreationStatusCode() == 1 ){
            dataOverride.setEnabled(false);
        }else{
            dataOverride.setEnabled(true);
        }
        // Check for the Proposal Hierarchy related details
     //*   gnprh - Commented Temporarly - start
      if(hierarchyMap!= null){
            boolean inHierarchy = ((Boolean)hierarchyMap.get("IN_HIERARCHY")).booleanValue();
            boolean isParent = false;
            if(inHierarchy) {
                isParent = ((Boolean)hierarchyMap.get("IS_PARENT")).booleanValue();
                if(isParent){
                    create.setVisible(false);
                    remove.setVisible(false);
                    join.setVisible(true);
                    btnSyncAll.setVisible(true);
                    mnuItmSyncAll.setVisible(true);
                    //Modified by tarique for disable item in display mode and
                    //new mode
                    if(functionType == TypeConstants.DISPLAY_MODE){
                        join.setVisible(false);

                    }
                    //Modified by tarique End here
                    join.setText("Link Child Proposal");
                }else{
                    //Disbale the hierarchy related menu Items
                    create.setVisible(false);
                    join.setVisible(false);
                    remove.setVisible(true);
                    //btnSyncAll.setVisible(false);
                    //Modified by tarique start here
                    btnSyncAll.setVisible(true);
                    btnSyncAll.setToolTipText("Sync To Parent");
                    btnSyncAll.setIcon(new ImageIcon(getClass().getClassLoader().
                                getResource(CoeusGuiConstants.SYNC_ICON)));
                    mnuItmSyncAll.setVisible(true);
                    mnuItmSyncAll.setText("Sync To Parent");
                    mnuItmSyncAll.setMnemonic('o');
                    //Modified by tarique End here

                    join.setText("Link To Hierarchy");

                    // disable the action menu items
                    approve.setEnabled(false);
                    submitSponsor.setEnabled(false);
                    submitApproval.setEnabled(false);
                    validationChecks.setEnabled(false);
                    showRouting.setEnabled(false);
                    approvalMap.setEnabled(false);

                }
            }else{
                remove.setVisible(false);
                btnSyncAll.setVisible(false);
                mnuItmSyncAll.setVisible(false);
            }
            //Added by Tarique for disable items in display mode and add mode
            if(functionType == TypeConstants.ADD_MODE || functionType ==
                                                    TypeConstants.DISPLAY_MODE){
                create.setVisible(false);
                join.setVisible(false);
                remove.setVisible(false);
                btnSyncAll.setVisible(false);
                mnuItmSyncAll.setVisible(false);
            }
            //Added by tarique End here

        }
        //Added by tarique start
        setPropHierarchyStatus();
      //*End

        if(isHierarchy() && !isParentProposal()){
            btnSubmitProposal.setEnabled(false);
            btnApproveProposal.setEnabled(false);
            btnApprovalMap.setEnabled(false);
            approve.setEnabled(false);
            submitApproval.setEnabled(false);
            submitSponsor.setEnabled(false);
            submitGrantsGov.setEnabled(false);
            validationChecks.setEnabled(false);
        }

    }

    /**
     * constructs File menu
     *
     * @return CoeusMenu ProposalDetails File menu
     */
    private CoeusMenu getFileMenu() {
        if( localFileMenu == null ) {
            Vector fileChildren = new Vector();

            print = new CoeusMenuItem("Print", null, true, true);
            print.setMnemonic('P');
            print.addActionListener(this);

            nextProposal = new CoeusMenuItem("Next", null, true, true);
            nextProposal.setMnemonic('N');
            nextProposal.addActionListener(this);

            previousProposal = new CoeusMenuItem("Previous", null, true, true);
            previousProposal.setMnemonic('r');
            previousProposal.addActionListener(this);

            inbox = new CoeusMenuItem("Inbox",null,true,true);
            inbox.setMnemonic('I');
            inbox.addActionListener(this);

            close = new CoeusMenuItem("Close",null,true,true);
            close.setMnemonic('C');
            close.addActionListener(this);

            save = new CoeusMenuItem("Save",null,true,true);
            save.setMnemonic('S');
            save.addActionListener(this);
            save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,KeyEvent.CTRL_MASK));


            //Commented since we are not using it in Coeus 4.0
            /*
            printSetup = new CoeusMenuItem("Print Setup..",null,true,true);
            printSetup.setMnemonic('P');
            printSetup.addActionListener(this);
             */
            //Added for Case #2371 start 1
            mnuItmCurPendReport = new CoeusMenuItem("Current And Pending Report...",null,true,true);
            mnuItmCurPendReport.setMnemonic('d');
            mnuItmCurPendReport.addActionListener(this);
            //Added for case #2371 end 1
            changePass = new CoeusMenuItem("Change Password",null,true,true);
            changePass.setMnemonic('h');
            changePass.addActionListener(this);

            //Added for Case#3682 - Enhancements related to Delegations - Start
            delegations = new CoeusMenuItem("Delegations",null,true,true);
            delegations.setMnemonic('D');
            delegations.addActionListener(this);
            //Added for Case#3682 - Enhancements related to Delegations - End

            pref = new CoeusMenuItem("Preferences",null,true,true);
            pref.setMnemonic('R');
            pref.addActionListener(this);

            //For Case 2110 Start2
            mnuItemCurrentLocks =  new CoeusMenuItem("Current Locks",null,true,true);
            mnuItemCurrentLocks.setMnemonic('L');
            mnuItemCurrentLocks.addActionListener(this);
            //For Case 2110 End2

            // Added for bug fix 1651 : start
            mnuItemExit = new CoeusMenuItem("Exit",null,true,true);
            mnuItemExit.setMnemonic('x');
            mnuItemExit.addActionListener(this);
            // End of Bug fix 1651

            fileChildren.add(inbox);
            fileChildren.add(print);

            //Commented since we are not using it in Coeus 4.0
            //fileChildren.add(printSetup);

            fileChildren.add(nextProposal);
            fileChildren.add(previousProposal);
            //Added for Case #2371 start 2
            fileChildren.add(SEPERATOR);
            fileChildren.add(mnuItmCurPendReport);
            //Added for Case #2371 end 2
            fileChildren.add(SEPERATOR);
            fileChildren.add(close);
            fileChildren.add(SEPERATOR);
            fileChildren.add(save);
            fileChildren.add(SEPERATOR);
            fileChildren.add(changePass);
            //For Case 2110 Start3
            fileChildren.add(SEPERATOR);
            fileChildren.add(mnuItemCurrentLocks);
            fileChildren.add(SEPERATOR);
            //For Case 2110 End3
            //Added for Case#3682 - Enhancements related to Delegations - Start
            fileChildren.add(delegations);
            fileChildren.add(SEPERATOR);
            //Added for Case#3682 - Enhancements related to Delegations - End
            fileChildren.add(pref);
            fileChildren.add(mnuItemExit);//bug fix 1651

            //            if( functionType == DISPLAY_MODE ) {
            //                nextProposal.setEnabled( true );
            //                previousProposal.setEnabled( true );
            //                save.setEnabled( false );
            //            }else {
            //                nextProposal.setEnabled( false );
            //                previousProposal.setEnabled( false );
            //                save.setEnabled( true );
            //            }
           //Case Id
            if( functionType == DISPLAY_MODE ) {
                if(isSpecialReviewRight){
                    save.setEnabled( true);
                    nextProposal.setEnabled( false );
                    previousProposal.setEnabled( false );
                }else{
                    save.setEnabled( false);
                    nextProposal.setEnabled( true );
                    previousProposal.setEnabled( true );
                }
            }else{
                nextProposal.setEnabled( false );
                previousProposal.setEnabled( false );
                save.setEnabled( true );
            }
            localFileMenu = new CoeusMenu("File", null, fileChildren, true, true);
            localFileMenu.setMnemonic('F');
        }
        return localFileMenu;

    }

    /**
     * constructs edit menu with sub menu items like Narrative, Budget, Medusa etc..
     *
     * @return CoeusMenu ProposalDetails edit menu
     */
    private CoeusMenu getEditMenu() {
        if( editMenu == null ) {
            Vector fileChildren = new Vector();

            narrative = new CoeusMenuItem("Narrative...", null, true, true);
            narrative.setMnemonic('N');
            narrative.addActionListener(this);

            budget = new CoeusMenuItem("Budget...", null, true, true);
            budget.setMnemonic('B');
            budget.addActionListener(this);

            userAttachedS2SForms = new CoeusMenuItem("User Attached S2S Forms...", null, true, true);
//            userAttachedS2SForms.setMnemonic('S');
            userAttachedS2SForms.addActionListener(this);
            
            //Human Subjects 
            humnSubS2SForms = new CoeusMenuItem("Human Subjects Forms...", null, true, true);
            humnSubS2SForms.addActionListener(this);
            //Human Subjects 

            //Commented for the Coeus Enhancement case:#1823 start step:4
//            specialReview = new CoeusMenuItem("Special Review...", null, true, true);
//            specialReview.setMnemonic('S');
//            specialReview.addActionListener(this);
            //End Coeus Enhancement case:#1823 step:4

            yesNoQuestion = new CoeusMenuItem("Yes No Questions...", null, true, true);
            yesNoQuestion.setMnemonic('Y');
            yesNoQuestion.addActionListener(this);

            propRoles = new CoeusMenuItem("Proposal Roles...", null, true, true);
            propRoles.setMnemonic('P');
            propRoles.addActionListener(this);
             //added for questionnaire start 1
            mnuItmQuestions = new CoeusMenuItem("Questionnaire...", null, true, true);
            mnuItmQuestions.setMnemonic('Q');
            mnuItmQuestions.addActionListener(this);
            //added for questionnaire end 1
            abstracts = new CoeusMenuItem("Abstracts...", null, true, true);
            abstracts.setMnemonic('A');
            abstracts.addActionListener(this);

            propPersonnel = new CoeusMenuItem("Proposal Personnel...", null, true, true);
            propPersonnel.setMnemonic('e');
            propPersonnel.addActionListener(this);

            notepad = new CoeusMenuItem("Notepad...", null, true, true);
            notepad.setMnemonic('t');
            notepad.addActionListener(this);

            dataOverride = new CoeusMenuItem("Data Override...", null, true, true);
            dataOverride.setMnemonic('v');
            dataOverride.addActionListener(this);


            medusa = new CoeusMenuItem("Medusa", null, true, true);
            medusa.setMnemonic('M');
            medusa.addActionListener(this);
            medusa.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M,KeyEvent.CTRL_MASK));

            create = new CoeusMenuItem("Create Hierarchy", null, true, true);
            create.setMnemonic('C');
            create.addActionListener(this);

            join = new CoeusMenuItem("Link To Hierarchy", null, true, true);
            join.setMnemonic('k');
            join.addActionListener(this);

            //remove = new CoeusMenuItem("Remove Hierarchy", null, true, true);
            //Modified by Tarique for changing menu item name
            remove = new CoeusMenuItem("Remove from Hierarchy", null, true, true);
            //Modified by tarique End here
            remove.setMnemonic('R');
            remove.addActionListener(this);
            //Added by tarique for syncing start
            mnuItmSyncAll = new CoeusMenuItem("Sync All", null, true, true);
            mnuItmSyncAll.setMnemonic('L');
            mnuItmSyncAll.addActionListener(this);
            //Added by tarique for syncing end

            fileChildren.add(narrative);
            fileChildren.add(budget);
             
            // JM 1-14-2015 check for permissions; only show menu option if user has right
            edu.vanderbilt.coeus.utils.UserPermissions perm = 
        		new edu.vanderbilt.coeus.utils.UserPermissions(mdiForm.getUserId());
            try {
            	boolean hasRight = perm.hasRight("UPLOAD_USER_S2S_FORMS");
            	if (hasRight) {
            fileChildren.add(userAttachedS2SForms);
            fileChildren.add(humnSubS2SForms);
            	}
				
			} catch (CoeusClientException e) {
				System.out.println("Could not get user permissions for S2S form menu item");
			}
	        // JM END 

            fileChildren.add(SEPERATOR);

            //Commented for the Coeus Enhancement case:#1823 start step:5
//            fileChildren.add(specialReview);
            //End Coeus Enhancement case:#1823 step:5
            fileChildren.add(yesNoQuestion);
            //Added for question start
            fileChildren.add(mnuItmQuestions);
            //Added for question end
            fileChildren.add(propRoles);
            fileChildren.add(abstracts);
            fileChildren.add(propPersonnel);
            fileChildren.add(notepad);
            fileChildren.add(SEPERATOR);
            fileChildren.add(dataOverride);
            fileChildren.add(SEPERATOR);
            fileChildren.add(medusa);
            fileChildren.add(SEPERATOR);
           // gnprh - start
            fileChildren.add(create);
            fileChildren.add(join);
            fileChildren.add(remove);
            fileChildren.add(mnuItmSyncAll);
            // End
            editMenu = new CoeusMenu("Edit", null, fileChildren, true, true);
            editMenu.setMnemonic('E');
        }
        return editMenu;
    }



    /**
     * constructs action menu with sub menu items
     *
     * @return CoeusMenu ProposalDetails Action menu
     */
    private CoeusMenu getActionMenu() {

        if( actionsMenu == null ) {
            Vector fileChildren = new Vector();

            // COEUSDEV-319: Premium - Change menu label in Protocol window - Protocol Actions --> Approve
//            approve = new CoeusMenuItem("Approve...", null, true, true);
            approve = new CoeusMenuItem("Approval/Rejection...", null, true, true);
            approve.setMnemonic('A');
            approve.addActionListener(this);

            submitApproval = new CoeusMenuItem("Submit for Approval...", null, true, true);
            submitApproval.setMnemonic('S');
            submitApproval.addActionListener(this);

            submitSponsor = new CoeusMenuItem("Submit to Sponsor", null, true, true);
            submitSponsor.setMnemonic('b');
            submitSponsor.addActionListener(this);

            submitGrantsGov = new CoeusMenuItem(CoeusGuiConstants.GRANTS_GOV, null, true, true); // JM 5-25-2016 static title
            submitGrantsGov.setMnemonic('g');
            submitGrantsGov.addActionListener(this);
            //            submitGrantsGov.setEnabled(false);


            validationChecks = new CoeusMenuItem(CoeusGuiConstants.VALIDATIONS, null, true, true); // JM 5-25-2016 static title
            validationChecks.setMnemonic('V');
            validationChecks.addActionListener(this);

            showRouting = new CoeusMenuItem("Show Routing", null, true, true);
            showRouting.setMnemonic('R');
            showRouting.addActionListener(this);

            addViewer = new CoeusMenuItem("Add Viewer", null, true, true);
            addViewer.setMnemonic('V');
            addViewer.addActionListener(this);

            approvalMap = new CoeusMenuItem("Select Approval Map...", null, true, true);
            approvalMap.setMnemonic('M');
            approvalMap.addActionListener(this);

            //Commented for COEUSQA-2342 : remove the old notification icon and menu item from proposal development window - Start
//            notify = new CoeusMenuItem("Notify...", null, true, true);
//            notify.setMnemonic('N');
//            notify.addActionListener(this);
            //COEUSQA-2342 : End
            certifications = new CoeusMenuItem("Print Certifications...", null, true, true);
            certifications.setMnemonic('P');
            certifications.addActionListener(this);

            //            anish new edit of notification STARTS
             notification = new CoeusMenuItem("Send Notification", null, true, true);
             notification.setMnemonic('N');
             notification.addActionListener(this);

//             NOTIFICATION  ENDS
//for adding new menu item disclosure status for coi starts
             disclosurestatus = new CoeusMenuItem("COI Disclosure Status...", null, true, true);
             disclosurestatus.setMnemonic('Z');
             disclosurestatus.addActionListener(this);
//for adding new menu item disclosure status for coi ends

            fileChildren.add(approve);
            fileChildren.add(SEPERATOR);
            fileChildren.add(submitApproval);
            fileChildren.add(submitSponsor);
            fileChildren.add(SEPERATOR);
            fileChildren.add(submitGrantsGov);
            fileChildren.add(validationChecks);
            fileChildren.add(showRouting);
            fileChildren.add(SEPERATOR);
            fileChildren.add(addViewer);
            fileChildren.add(approvalMap);
            //Commented for COEUSQA-2342 : remove the old notification icon and menu item from proposal development window - Start
//            fileChildren.add(notify);
            //COEUSQA-2342 : End

            // notification START
            try
            {
            parameterExist=fetchParameterValue();
            if(parameterExist){
            	/* JM 5-25-2011 remove menu selection for Print Certifications per 4.4.2; 4-2-2015 re-enabling for testing */
                fileChildren.add(SEPERATOR);



                fileChildren.add(certifications);


                /* JM END */
                fileChildren.add(notification);
                notification.setVisible(true);
                notification.setEnabled(false);

                rightExist=getNotifyProposalPersonRight();
                if(rightExist){
                	fileChildren.add(notification);
                }
                else{
                	fileChildren.add(notification);
                	notification.setVisible(true);
                	notification.setEnabled(false);
               }
//Added for COEUSDEV-736:
//This code is added to disable Send Notification  button if the proposal is a child proposal.
                if(hierarchyMap!= null){
		            boolean inHierarchy = ((Boolean)hierarchyMap.get("IN_HIERARCHY")).booleanValue();
		            boolean isParent = false;
		            if(inHierarchy) {
		                isParent = ((Boolean)hierarchyMap.get("IS_PARENT")).booleanValue();
		                if(!isParent){notification.setEnabled(false);}


		            }
	            }
//Added for COEUSDEV-736: to disable Send Notification button end.
            }
            else
            {   fileChildren.add(notification);
                notification.setVisible(false);
            }
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
            //Notification ENDS

            fileChildren.add(disclosurestatus);
            actionsMenu = new CoeusMenu("Action", null, fileChildren, true, true);
            actionsMenu.setMnemonic('A');
        }
        return actionsMenu;

    }

    /**
     * This method is used to create the tool bar for Save, navigate proposals
     * Close buttons etc.
     *
     * @returns JToolBar ProposalDetails Toolbar
     */
    private JToolBar getToolBar() {
        JToolBar toolbar = new JToolBar();

        /*---Commented below code and added same without getClassLoader()---*/

        btnNextProposal = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.ERIGHT_ARROW_ICON)),
        null, "Next Proposal");

        btnNextProposal.setDisabledIcon(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.DRIGHT_ARROW_ICON)));

        btnPrevProposal = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.ELEFT_ARROW_ICON)),
        null, "Previous Proposal");

        btnPrevProposal.setDisabledIcon(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.DLEFT_ARROW_ICON)));

        btnApproveProposal = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.EAPPROVE_ICON)),
        null, "Approve Proposal");

        btnApproveProposal.setDisabledIcon(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.DAPPROVE_ICON)));

        btnSubmitProposal = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.ESUBMIT_APPROVAL_ICON)),
        null, "Submit Proposal for approval");

        btnSubmitProposal.setDisabledIcon(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.DSUBMIT_APPROVAL_ICON)));

        btnApprovalMap = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.MAP_ICON)),
        null, "Select Approval Map");

        //Commented for COEUSQA-2342 : remove the old notification icon and menu item from proposal development window - Start
//        btnProposalNotification = new CoeusToolBarButton(new ImageIcon(
//        getClass().getClassLoader().getResource(CoeusGuiConstants.NOTIFY_ICON)),
//        null, "Send Proposal Notification");
        //COEUSQA-2342 : End

        btnProposalNarrative = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.NARRATIVE_ICON)),
        null, "Proposal Narrative");

        /* CASE #1167 Comment Begin */
        /*btnBudget = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.AWARDS_ICON)),
        null, "Proposal Budget");*/
        /* CASE #1167 Comment End */

        /* CASE #1167 Begin */
        btnBudget = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.BUDGET_ICON)),
        null, "Proposal Budget");
        /* CASE #1167 End */

        //Commented for the Coeus Enhancement case:#1823 start step:6
//        btnSpecialReview = new CoeusToolBarButton(new ImageIcon(
//        getClass().getClassLoader().getResource(CoeusGuiConstants.SPECIAL_REVIEW_ICON)),
//        null, "Special Review");
        //End Coeus Enhancement case:#1823 step:6

        btnYesNoQues = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.YES_NO_ICON)),
        null, "Yes No Questions..");

        btnRoles = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.USER_ROLES_ICON)),
        null, "Proposal Roles Maintenance");

        btnAbstracts = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.ABSTRACT_ICON)),
        null, "Proposal Abstracts");

        btnPersons = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.PERSONS_ICON)),
        null, "Proposal Persons");

        btnNotepad = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.NOTEPAD_ICON)),
        null, "Notepad");

        btnMedusa = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.MEDUSA_ICON)),
        null, "Medusa");
        
        /* JM 5-17-2016 button to display copied from num */
        btnCopiedFromPropNum = new CoeusToolBarButton(new ImageIcon(
                getClass().getClassLoader().getResource(CoeusGuiConstants.COPY_ICON)),
                null, "Copied From Proposal Number");
        /* JM END */
        
        /* JM 5-25-2016 buttons for questionnaire, validations, GG */
        btnQuestionnaire = new CoeusToolBarButton(new ImageIcon(
                getClass().getClassLoader().getResource(CoeusGuiConstants.QUESTION_PROP_HIE_ICON)),
                null, CoeusGuiConstants.PROPOSAL_QUESTIONNAIRE);
        
        btnValidations = new CoeusToolBarButton(new ImageIcon(
                getClass().getClassLoader().getResource(CoeusGuiConstants.CATEGORY_ICON)),
                null, CoeusGuiConstants.VALIDATIONS);
        
        btnGG = new CoeusToolBarButton(new ImageIcon(
                getClass().getClassLoader().getResource(CoeusGuiConstants.PROTOCOL_SUBMISSION_BASE_ICON)),
                null, CoeusGuiConstants.GRANTS_GOV);
        /* JM END */

        btnPrint = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.PRINT_ICON)),
        null, "Print");

        btnSave = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.SAVE_ICON)),
        null, "Save");

        btnSave.setDisabledIcon(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.DSAVE_ICON)));

        btnSyncAll = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.SYNC_ICON)),
        null, "Sync All");

        btnClose = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.CLOSE_ICON)),
        null, "Close");

        sendEmail = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.EMAIL_ICON)), null, "Send Mail Notification");

        /**
         * Implementation of Previous / Next Tool bar Button Functionality.
         *
         * Updated Subramanya dated April 23 2003.
         */
        if( this.functionType != CoeusGuiConstants.DISPLAY_MODE ){
            btnNextProposal.setEnabled(false);
            btnPrevProposal.setEnabled(false);
        }
        btnApproveProposal.addActionListener(this);
        btnSubmitProposal.addActionListener(this);
        btnApprovalMap.addActionListener(this);
        //Commented for COEUSQA-2342 : remove the old notification icon and menu item from proposal development window - Start
//        btnProposalNotification.addActionListener(this);
        //COEUSQA-2342 : End
        btnProposalNarrative.addActionListener(this);
        btnBudget.addActionListener(this);
        //Commented for the Coeus Enhancement case:#1823 start step:7
//        btnSpecialReview.addActionListener(this);
        //End Coeus Enhancement case:#1823 step:7
        btnYesNoQues.addActionListener(this);
        btnRoles.addActionListener(this);
        btnAbstracts.addActionListener(this);
        btnPersons.addActionListener(this);
        btnNotepad.addActionListener(this);
        btnMedusa.addActionListener(this);
        btnPrint.addActionListener(this);
        btnSave.addActionListener(this);
        btnSyncAll.addActionListener(this);
        btnClose.addActionListener(this);
        sendEmail.addActionListener(this);

        toolbar.add(btnNextProposal);
        toolbar.add(btnPrevProposal);
        toolbar.add(btnApproveProposal);
        toolbar.add(btnSubmitProposal);
        toolbar.add(btnApprovalMap);
        //Commented for COEUSQA-2342 : remove the old notification icon and menu item from proposal development window - Start
//        toolbar.add(btnProposalNotification);
        //COEUSQA-2342 : End
        /* JM 5-25-2016 validations and GG buttons */
        toolbar.addSeparator();
        btnValidations.addActionListener(this);
        toolbar.add(btnValidations);
        btnGG.addActionListener(this);
        toolbar.add(btnGG);
        /* JM END */
        
        toolbar.addSeparator();
        btnCopiedFromPropNum.addActionListener(this);
        toolbar.add(btnProposalNarrative);
        toolbar.add(btnBudget);
        /* JM 5-25-2016 questionnaire button */
        btnQuestionnaire.addActionListener(this);
        toolbar.add(btnQuestionnaire); 
        /* JM END */
        //Commented for the Coeus Enhancement case:#1823 start step:8
//        toolbar.add(btnSpecialReview);
        //End Coeus Enhancement case:#1823 step:8
        toolbar.add(btnYesNoQues);
        toolbar.add(btnRoles);
        toolbar.add(btnAbstracts);
        toolbar.add(btnPersons);
        toolbar.addSeparator();
        toolbar.add(btnNotepad);
        toolbar.addSeparator();
        toolbar.add(btnMedusa);
        toolbar.addSeparator();
        /* JM 5-17-2016 copied from proposal button */
        btnCopiedFromPropNum.addActionListener(this);
        toolbar.add(btnCopiedFromPropNum);
        /* JM END */
        toolbar.add(btnPrint);
        toolbar.add(btnSave);
        toolbar.addSeparator();
       // gnprh - start
        toolbar.add(btnSyncAll);
        //* End

        toolbar.addSeparator();
        toolbar.add(sendEmail);
        toolbar.addSeparator();
        toolbar.add(btnClose);



        toolbar.setFloatable(false);
        if(functionType == DISPLAY_MODE){
            btnSave.setEnabled(false);
            btnApproveProposal.setEnabled( false );
            btnSubmitProposal.setEnabled( false );
        }
        return toolbar;
    }

    public void registerObserver( Observer observer ) {
        observable.addObserver( observer );
    }
    public void unRegisterObserver( Observer observer ) {
        observable.deleteObserver( observer );
    }

//    /** generate xml - added by ele for proposal nih printing
//     */
//
//    private void proposalXMLGenerator() throws Exception{
//
//        String connURL = CoeusGuiConstants.CONNECTION_URL +
//        "/ProposalXMLGeneratorServlet";
//
//
//        // connect to the database and get the formData
//        RequesterBean request = new RequesterBean();
//        request.setId( proposalID );
//
//        AppletServletCommunicator comm = new AppletServletCommunicator(
//        connURL, request );
//        comm.send();
//
//        ResponderBean response = comm.getResponse();
//        if ( response.isSuccessfulResponse() ) {
//            String pdfURL = response.getDataObject().toString() ;
//            AppletContext coeusContxt = mdiForm.getCoeusAppletContext();
//
//            if (coeusContxt != null) {//applet - obsolete
//                coeusContxt.showDocument( new URL(
//                CoeusGuiConstants.CONNECTION_URL + pdfURL ), "_blank" );
//            }
//            else {//webstart
//                javax.jnlp.BasicService bs = (javax.jnlp.BasicService)javax.jnlp.ServiceManager.lookup("javax.jnlp.BasicService");
//                bs.showDocument(new URL(CoeusGuiConstants.CONNECTION_URL + pdfURL ));
//                try {
//                    if ((response.getId().equalsIgnoreCase("Yes")) || (response.getId().equalsIgnoreCase("Y")) ) {
//                        String debugXmlURL = pdfURL.substring(0,pdfURL.indexOf(".pdf"))  + ".xml" ;
//                        bs.showDocument(new URL(CoeusGuiConstants.CONNECTION_URL + debugXmlURL ));
//                    }
//                }
//                catch(Exception xmlExp) {
//                    xmlExp.printStackTrace() ;
//                }
//            }
//
//        }
//        else {
//            CoeusOptionPane.showErrorDialog(response.getMessage()) ;
//        }
//    }





    /**
     *  This
     */
    public void actionPerformed( ActionEvent ae ) {
        Object source = ae.getSource();

        this.dtUtils = new DateUtils();
        //this.dtFormat = new java.text.SimpleDateFormat("MM/dd/yyyy");


        Component comp = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
        if( comp != null ) {
            InputVerifier  inputVer = ((JComponent)comp).getInputVerifier();
            if( inputVer != null && !inputVer.shouldYieldFocus((JComponent)comp)){
                return;
            }
        }
        try{
             mdiForm.setCursor(new java.awt.Cursor(Cursor.WAIT_CURSOR));

             if(source.equals(specialReviewForm.btnStartProtocol)){
                // CoeusOptionPane.showInfoDialog("new proto Saving.....");
                 char funType='8';

                 String newProtocolNumber= savenewProtocol(funType);
                if(newProtocolNumber!=null){
                    int selRow= specialReviewForm.tblSpecialReview.getSelectedRow();
                    ProtocolInfoBean newProtocolBean=getProtocolDetails(newProtocolNumber);
                    if(newProtocolBean!=null){
                        //* *  * THIS METHOD FOR setting the specialreview datas SAVING......(STREAM PROTOCOL).
                         specialReviewForm.setProtocolCreated(newProtocolBean,newProtocolNumber);
                    }































                    specialReviewForm.btnStartProtocol.setEnabled(false);

                    }

                }
            else if( source.equals( btnClose ) || source.equals( close ) ){
                closeProposalDetailForm();



            }else if ( source.equals( btnSave ) || source.equals( save ) ){
                //Case :#3149 ? Tabbing between fields does not work on others tabs - Start
                if(getFunctionType() != DISPLAY_MODE && proposalOther != null){
                    if(tbdPnProposal.getSelectedIndex() == 7){
                        customTable = proposalOther.getTable();
                        row = proposalOther.getRow();
                        column = proposalOther.getColumn();
                        count = 0;
                    }
                }
                //Case :#3149 - End
                saveProposalDetails( SAVE_RETAIN_LOCK );

                //Proposal Hierarchy Enhancment Start
                // gnprh - commneted for Proposal Hierarchy
                refreshPropHierarchyTab();
                 //* End
                 //Proposal Hierarchy Enhancment End

            }else if( source.equals( btnNextProposal ) || source.equals( nextProposal )){
                performNextPreviousProposalAction( true );
            }else if( source.equals(btnPrevProposal) || source.equals( previousProposal)){
                performNextPreviousProposalAction( false );
                // Implemented by Raghunath P.V. For ProposalPersonnel
            }else if( source.equals( btnPersons ) || (source.equals( propPersonnel )) ){
                saveProposalDetails( SAVE_RETAIN_LOCK );
                showProposalPersonnel();
            }else if( source.equals( btnYesNoQues ) || (source.equals( yesNoQuestion )) ){
                showYesNoQuestionsForm();
            }else if( source.equals( btnRoles ) || source.equals( propRoles ) ){
                showUserRoles();
            }else if( (source.equals( btnAbstracts )) || (source.equals(abstracts))){
                saveProposalDetails( SAVE_RETAIN_LOCK );
                performAbstractDisplay();
             //Commented for the Coeus Enhancement case:#1823 start step:9
//            }else if( (source.equals( specialReview )) || (source.equals( btnSpecialReview))){
//                showSpecialReview();
             //End Coeus Enhancement case:#1823 step:9
            }else if( (source.equals( narrative ) ) || ( source.equals( btnProposalNarrative) ) ) {
                showNarrative(functionType);
            }else if(source.equals( btnBudget ) || source.equals( budget )) {
                saveProposalDetails( SAVE_RETAIN_LOCK );    // Added by Vyjayanthi
                showBudget();                               // Added by Vyjayanthi
            //Commented for COEUSQA-2342 : remove the old notification icon and menu item from proposal development window - Start
//            }else if( (source.equals( btnProposalNotification) ) || ( source.equals( notify) ) ) { // Added by Amit
//                showNotification();                                                                                             // Added by Amit
            //COEUSQA-2342 : End
            }else if(source.equals(userAttachedS2SForms)){
                showUserAttachmentsS2SForms(proposalID);
            }else if(source.equals(humnSubS2SForms)){
                showHumnSubjtForms(proposalID);
            }else if(source.equals(dataOverride)){                                                                  // Added by Amit
                showDataOverride();                                                                                         // Added by Amit
            }else if(source.equals(addViewer)){                                                                     // Added by Amit
                showProposalViewer(proposalID);                                                                   // Added by Amit
            }else if(source.equals(btnMedusa) || source.equals(medusa)){
                showMedusa();
            }else if( (source.equals( approvalMap) ) || ( source.equals( btnApprovalMap) ) ) {
                showApprovalMap();
            }else if( source.equals( showRouting) ) {
                ComboBoxBean proposalStatusDetails = getProposalStatusDetails(proposalID);
                int proposalStatusCode = 0;
                String proposalStatusDesc = CoeusGuiConstants.EMPTY_STRING;
                if(proposalStatusDetails != null){
                    proposalStatusCode = Integer.parseInt(proposalStatusDetails.getCode());
                    proposalStatusDesc = proposalStatusDetails.getDescription();
                }
                if(proposalStatusCode == PROPOSAL_IN_PROGRESS_STATUS
                        || proposalStatusCode == PROPOSAL_IS_REJECTED_STATUS
                        || proposalStatusCode == PROPOSAL_IN_RECALLED_STATUS){
                    showRouting();
                }else{
                    MessageFormat formatter = new MessageFormat("");
                    String message = formatter.format(coeusMessageResources.parseMessageKey("showRouting_exceptionCode.1144"),"'",proposalStatusDesc,"'");
                    CoeusOptionPane.showWarningDialog(message);
                }
            }else if( source.equals( submitApproval) || ( source.equals( btnSubmitProposal))  ) {
                // saveProposalDetails( SAVE_RETAIN_LOCK );
                showSubmitForApproval();
            } 
            /* JM 5-25-2016 added to trigger by button as well */
            else if (source.equals(validationChecks) || source.equals(btnValidations)) { 
            	char errType = doValidation(true);
                if(errType==0){
                    CoeusOptionPane.showInfoDialog(
                        coeusMessageResources.parseMessageKey("validationChecks_exceptionCode.1904"));
                }
            }
            /* JM END */
            else if( source.equals(btnNotepad) || source.equals(notepad) ){
                showNotepad();
            }else if( source.equals(btnApproveProposal) || source.equals(approve) ){
                showProposalRouting();
            }else if(source.equals(submitSponsor)){
                submitSponsor();
            }
             /* JM 5-25-2016 added to trigger by button as well */
            else if (source.equals(submitGrantsGov) || source.equals(btnGG) ){
                submitGrantsGov();
                updateChildStatus(proposalID);
            }
            /* JM END */
            else if( source.equals(inbox) ) {
                showInboxDetails();
            }else if( source.equals(changePass) ) {
                showChangePassword();
            }else if(source.equals(pref)){
                showPreference();
            }
            //Added for Case#3682 - Enhancements related to Delegations - Start
            else if(source.equals(delegations)){
                displayUserDelegation();
            }
            //Added for Case#3682 - Enhancements related to Delegations - End
            else if (source.equals(print) || source.equals(btnPrint))  {          //added by ele for nih prop printing
// JM 7-25-2011 check performing organization and throw error if missing
            	cvSites = proposalOrganization.getFormData();
            	ProposalSiteBean proposalSiteBean = (ProposalSiteBean) cvSites.get(1);
            	
                if (proposalSiteBean.getOrganizationId() == null) {
                	CoeusOptionPane.showErrorDialog("Performing organization must be defined.");
                } else {
// END                	
                //case 1686 begin
                if(isSaveRequired()) {
                    String msg = coeusMessageResources.parseMessageKey(
                    "saveConfirmCode.1002");
                    int selection = CoeusOptionPane.showQuestionDialog(msg, CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_YES);
                    if(selection == CoeusOptionPane.SELECTION_YES) {
                        saveProposalDetails( SAVE_RETAIN_LOCK );
                    }else if(selection == CoeusOptionPane.SELECTION_NO) {
                        return;
                    }
                }

                //case 1686 end
                /* when the File menu print is clicked showPrintForm
                 * method will be called
                 *  Added by Geo
                 */
                    /*
                     *  Begin block
                     */
                showPrintForm();
                    /*
                     *  End block
                     */


                //                     proposalXMLGenerator() ;
                } // JM
            }else if (source.equals(certifications)){
                //Added for bug fix #1829 Start
                if(isSaveRequired()) {
                    String msg = coeusMessageResources.parseMessageKey(
                    "saveConfirmCode.1002");
                    int selection = CoeusOptionPane.showQuestionDialog(msg, CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_YES);
                    if(selection == CoeusOptionPane.SELECTION_YES) {
                        saveProposalDetails( SAVE_RETAIN_LOCK );
                    }else if(selection == CoeusOptionPane.SELECTION_NO) {
                        return;
                    }
                }//Bug fix End #1829
                showPrintCertifications();
                //start of bug fix id 1651
            }else if(source.equals(notification)){
                notification.setEnabled(true);





                showPersonNotification();

            }
            else if(source.equals(disclosurestatus)){
                disclosurestatus.setEnabled(true);





                showdisclosurestatus();

            }
            else if(source.equals(mnuItemExit)) {
                exitApplication();//end of bug fix id 1651
            }else if(source.equals(create)){
                saveProposalDetails(SAVE_RETAIN_LOCK);
                createHierarchy();
            }else if(source.equals(join)){
                saveProposalDetails(SAVE_RETAIN_LOCK);
                joinHierarchy();
            }else if(source.equals(btnSyncAll) || source.equals(mnuItmSyncAll)){
                String message = coeusMessageResources.parseMessageKey(SYNC_MESSAGE);
                int answer = CoeusOptionPane.showQuestionDialog(message,
                        CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_NO);
                if (answer == JOptionPane.YES_OPTION) {
                    saveProposalDetails(SAVE_RETAIN_LOCK);
                    //if(isSyncRequired()){
                        syncAllProposals();
                    //}
                }
            }else if(source.equals(remove)){
                removeProposalFromHierarchy();
            }//For Case 2110 Start4
            else if(source.equals(mnuItemCurrentLocks)){
                showLocksForm();
            }//For Case 2110 End 4
            //Added for Case #2371 start 3
            else if(source.equals(mnuItmCurPendReport)){
                saveProposalDetails(SAVE_RETAIN_LOCK);
                showCurrentAndPendingDetails();
            }
            //Added for Case #2371 end 3
             //Added for questionnaire start 2
             /* JM 5-25-2016 added to trigger by button as well */
            else if(source.equals(mnuItmQuestions) || source.equals(btnQuestionnaire)){
                saveProposalDetails(SAVE_RETAIN_LOCK);
                showQuestionsWindow();
            }
             /* JM END */
            //Added for questionnaire end 2
            //Added for Case#2214 Email enhancement - start
            //Invoking the mail interface from SendMailNotification command button from tool bar.
            else if(source.equals(sendEmail)) {
                    ActionValidityChecking checkValid = new ActionValidityChecking();
                    synchronized(checkValid) {
                        //COEUSDEV-75:Rework email engine so the email body is picked up from one place
                        checkValid.sendMail(ModuleConstants.PROPOSAL_DEV_MODULE_CODE, 0, proposalID, 0);
                    }
            }
            //Added for Case#2214 Email enhancement - end

            
             /* JM 5-17-2016 show copied from proposal number */
            else if (source.equals(btnCopiedFromPropNum)) {
            	showCopiedFromPropNum();
            }
            /* JM END */
             
            /* JM 5-25-2016 questionnaire, validations, and GG buttons */
            else if (source.equals(btnQuestionnaire)) {
            	showCopiedFromPropNum();
            }
            else if (source.equals(btnValidations)) {
            	showCopiedFromPropNum();
            }
            else if (source.equals(btnGG)) {
            	showCopiedFromPropNum();
            }
            /* JM END */
            
            else {
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
                "funcNotImpl_exceptionCode.1100"));
            }
        }catch (PropertyVetoException propertyVetoException) {
            //Its Becoz of closing. so no need to print.
        }catch (CoeusUIException coeusUIException) {
            coeusUIException.printStackTrace();
            CoeusOptionPane.showDialog(coeusUIException);
            //Case Id 1769
             if(functionType==DISPLAY_MODE){
                    if(isSpecialReviewRight){
                        if(isSpecialReviewLocked){
                            releaseUpdateLock();
                        }
                    }
                }
            tbdPnProposal.setSelectedIndex(coeusUIException.getTabIndex());
            //Case :#3149 ? Tabbing between fields does not work on others tabs - Start
            if(proposalOther != null){
                if(proposalOther.getOtherTabMandatory() && getFunctionType() != DISPLAY_MODE){
                    boolean[] lookUpAvailable = proposalOther.getLookUpAvailable();
                    customTable = proposalOther.getTable();
                    row = proposalOther.getMandatoryRow();
                    column = proposalOther.getMandatoryColumn();
                    count = 0;
                    if(lookUpAvailable[row]){
                        customTable.editCellAt(row,column+1);
                        customTable.setRowSelectionInterval(row,row);
                        customTable.setColumnSelectionInterval(column+1,column+1);

                    }else{
                        customTable.editCellAt(row,column);
                        customTable.setRowSelectionInterval(row,row);
                        customTable.setColumnSelectionInterval(column,column);
                    }
                    customTable.setEnabled(true);
                }
            }
            //Case :#3149 - End


        }catch(CoeusException ex){
            //Case Id 1769
            if(functionType==DISPLAY_MODE){
                    if(isSpecialReviewRight){
                        if(isSpecialReviewLocked){
                            releaseUpdateLock();
                        }
                    }
                }
            ex.printStackTrace();
            // added by manoj to display action messages as information messages
            CoeusOptionPane.showDialog(new CoeusClientException(ex));
        }catch ( Exception e) {
            e.printStackTrace();
            //Case Id 1769
            if(functionType==DISPLAY_MODE){
                    if(isSpecialReviewRight){
                        if(isSpecialReviewLocked){
                            releaseUpdateLock();
                        }
                    }
                }

            if(!( e.getMessage().equals(
            coeusMessageResources.parseMessageKey(
            "protoDetFrm_exceptionCode.1130")) )){
                CoeusOptionPane.showInfoDialog(e.getMessage());
            }
        }finally{
            mdiForm.setCursor(new java.awt.Cursor(Cursor.DEFAULT_CURSOR));
        }
    }


    private boolean isSyncRequired() throws CoeusException{
        CoeusVector cvProposalBudget  = proposalHierarchyBean.getProposalData();
        CoeusVector cvBudgetVersion = null;
        boolean syncRequired = false;
        ProposalBudgetBean proposalBudgetBean = null;
        boolean isProposalSync = false;
        boolean isBudgetSync  = false;
        if(cvProposalBudget!= null && cvProposalBudget .size() > 0){
            for(int index = 0; index < cvProposalBudget .size() ; index++){
                proposalBudgetBean = (ProposalBudgetBean)cvProposalBudget.get(index);
                cvBudgetVersion = proposalBudgetBean.getBudgetVersions();
                isBudgetSync = isBudgetSynced(cvBudgetVersion);
                isProposalSync = proposalBudgetBean.isProposalSynced();
                if(isBudgetSync && isProposalSync){
                    continue;
                }else{
                    syncRequired = true;
                    return syncRequired;
                }
            }
        }
        return syncRequired;
    }

    private boolean isBudgetSynced(CoeusVector cvBudgetVersionsData) throws CoeusException{
        ProposalBudgetVersionBean proposalBudgetVersionBean = null;
        boolean isBudgetSync = false;
        if(cvBudgetVersionsData!= null && cvBudgetVersionsData.size()> 0){
            for(int index = 0; index <cvBudgetVersionsData.size() ; index++){
                proposalBudgetVersionBean = (ProposalBudgetVersionBean)cvBudgetVersionsData.get(index);
                isBudgetSync = proposalBudgetVersionBean.isBudgetSynced();
                 if(isBudgetSync){
                    continue;
                }else{
                    isBudgetSync = false;
                    return isBudgetSync;
                }
            }
        }
        return isBudgetSync;
    }

    private void syncAllProposals() throws CoeusException,Exception{
        /*if(!isSyncRequired()){
            CoeusOptionPane.showInfoDialog("The Proposal is already sync state");
            return ;
        }*/
        int syncFlag = 0;
        /*String message = coeusMessageResources.parseMessageKey(SYNC_MESSAGE);;
        int answer = CoeusOptionPane.showQuestionDialog(message,
        CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_NO);
        if (answer == JOptionPane.YES_OPTION) {*/
            // save the proposal before sync
            //saveProposalDetails( SAVE_RETAIN_LOCK );
        //Modified with case 2158: Budget Validations
//        if(!isParentProposal()){
            //Added with COEUSDEV-198:Sync,link and create proposal hierarchy should apply budget validation rules ONLY
//            char errType = doValidation(false);//hard stop for no final version selected required.
            char errType = validateForHierarchy(proposalID,isParentProposal());
            //COEUSDEV-198 End
            if(errType == VALIDATION_ERROR){
                return;
            }else if(errType == VALIDATION_WARNING){
                int option = CoeusOptionPane.showQuestionDialog(
                        coeusMessageResources.parseMessageKey(CONFIRM_SYNCING),
                        CoeusOptionPane.OPTION_YES_NO,CoeusOptionPane.DEFAULT_NO);
                if(option == JOptionPane.NO_OPTION){
                    return;
                }
            }
//        }//2158 End
            this.setCursor(new java.awt.Cursor(Cursor.WAIT_CURSOR));
            Vector syncData =  connectToSyncAllChild();
            if(syncData!= null && syncData.size()>0) {
                syncFlag = Integer.parseInt(syncData.elementAt(0).toString());
                this.proposalDevelopmentFormBean = (ProposalDevelopmentFormBean) syncData.elementAt(1);
                // Get the Propsoal Hierarchy data
                hierarchyMap = (HashMap)syncData.elementAt(2);
                // Get the Proposal Hierarchy Tree data
                proposalHierarchyBean = (ProposalHierarchyBean)syncData.elementAt(3);
                updateData();

                /** Check if the selected proposal is in Hierarchy. If it is in Hierarchy
                 *then check for the parent and then set the values
                 */
                if(hierarchyMap!= null){
                    boolean inHierarchy = ((Boolean)hierarchyMap.get("IN_HIERARCHY")).booleanValue();
                    boolean isParent = false;
                    if(inHierarchy) {
                        setHierarchy(inHierarchy);
                        setParentPropNo((String)hierarchyMap.get("PARENT_PROPOSAL"));
                        isParent = ((Boolean)hierarchyMap.get("IS_PARENT")).booleanValue();
                        if(isParent){
                            setParentProposal(isParent);
                        }
                    }
                }


                hierarchyController.setProposalHierarchyBean(proposalHierarchyBean);
                hierarchyController.setFormData(unitNumber);
            }
            this.setCursor(new java.awt.Cursor(Cursor.DEFAULT_CURSOR));
            if(syncFlag < 0) {
                CoeusOptionPane.showErrorDialog(
                    coeusMessageResources.parseMessageKey(SYNC_ERROR));
                return;
            }else if(syncFlag == 100){
                CoeusOptionPane.showInfoDialog(
                    coeusMessageResources.parseMessageKey(SPONSOR_NOT_SAME));
            }
        //}
    }


    private Vector connectToSyncAllChild() throws CoeusException,Exception{
        String connectTo = CoeusGuiConstants.CONNECTION_URL + HIERARCHY_SERVLET;
        RequesterBean request = new RequesterBean();
        Vector data = null;

        CoeusVector cvDataToserver = new CoeusVector();

        cvDataToserver.add(getParentPropNo());// Parent proposal Number
        cvDataToserver.add(proposalID);

        cvDataToserver.add(new Boolean(isParentProposal()));
        request.setDataObject(cvDataToserver);
        request.setFunctionType(SYNC_ALL_PROPOSAL);
        AppletServletCommunicator comm
        = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response!=null){
            if (response.isSuccessfulResponse()){
                data = (Vector)response.getDataObjects();
            }
            else {
                throw new CoeusException(response.getMessage());
            }
        }
        return data;
    }

    /** This method will be triggered when Join to hierarchy action is performed
     *This will do the initial checking for the join action and then it will
     *join to the hierarchy
     */
    private void joinHierarchy() throws CoeusException,Exception{
        String proposalNumber = "";
        Vector data = null ;
        boolean isRoleExists = false;
        int code;

        /*Validate the current proposal*/
        data  = canBuildHierarchy(proposalID , JOIN_ACTION);
        isRoleExists = ((Boolean)data.get(0)).booleanValue();
        code = ((Integer)data.get(1)).intValue();

        if(!isRoleExists){
            CoeusOptionPane.showErrorDialog(
            coeusMessageResources.parseMessageKey(NO_RIGHT_TO_JOIN));
            return ;
        }

        if(!isParentProposal() && !validateHierarchy(code , proposalID)){
            return ;
        }
        //Added for case#3183 - Proposal hierarchy - starts
        //validate the proposal budget status while linking to hierarchy
        else if(isParentProposal() && code == 900){
            CoeusOptionPane.showInfoDialog(
            coeusMessageResources.parseMessageKey(BUDGET_STATUS_COMPLETE_LINK));
            return;
        }//End of if
        //Added for case#3183 - Proposal hierarchy - ends

        //Modified by tarique for show sub project and sub budget start
        ProposalHierarchyChildType proposalHierarchyChildType;

        proposalHierarchyChildType = new ProposalHierarchyChildType();
        int childTypeCode = proposalHierarchyChildType.display();
        if(!proposalHierarchyChildType.isOkPressed()){
            proposalHierarchyChildType.cleanUp();
            return;
        }
        proposalHierarchyChildType.cleanUp();
        //Modified by tarique End here

        proposalNumber  = getProposalIdBySearch();
        /** If the selected proposal number  is EMPTY
         */
        if(proposalNumber== null || ("".equals(proposalNumber))){
            //CoeusOptionPane.showInfoDialog("Please select a proposal Number");
            return ;
        }else if(proposalNumber.equals(proposalID)){
            CoeusOptionPane.showInfoDialog(
                coeusMessageResources.parseMessageKey(CANNOT_JOIN_ITSELF));
            return ;
        }else{
            //Added for case id 3183 - start
            if(!canLinkToHierarchy(proposalNumber)){
                return ;
            }
            //Added for case id 3183 - end
            data  = canBuildHierarchy(proposalNumber,JOIN_ACTION);
            isRoleExists = ((Boolean)data.get(0)).booleanValue();
            code = ((Integer)data.get(1)).intValue();
             if(!isRoleExists){
                CoeusOptionPane.showErrorDialog(
                coeusMessageResources.parseMessageKey(NO_RIGHT_TO_JOIN));
                return ;
            }
            /*Validate the searched proposal*/
            if(!isParentProposal() && !validateHierarchy(code , true ,proposalNumber , true)){
                return ;
            }else if(isParentProposal() && !validateHierarchy(code , true, proposalNumber)){
                return ;
            }
        }//End of inner else
        this.setCursor(new java.awt.Cursor(Cursor.WAIT_CURSOR));
        joinToParentProposal(proposalNumber , childTypeCode);
        this.setCursor(new java.awt.Cursor(Cursor.DEFAULT_CURSOR));
        //Added for Proposal Hierarchy Icon by Tarique start
        setPropHierarchyStatus();
        //Added for Proposal Hierarchy Icon by Tarique end
    }

    /** This will join to the parent proposal by making a server call.
     *It will pass the corersponding selected proposal number from search and
     *then it will join to the hierarchy
     *@ param selected proposal number
     */
    private void joinToParentProposal(String proposalNumber , int childTypeCode) throws CoeusException,Exception{
        CoeusVector data = linkToParent(proposalNumber , childTypeCode);
        int joinFlag = 0 ;
        if(data!= null && data.size()>0) {
            joinFlag = Integer.parseInt(data.elementAt(0).toString());
            this.proposalDevelopmentFormBean = (ProposalDevelopmentFormBean) data.elementAt(1);

            // Code Added for Proposal Hierarchy case# 3183 - start
            // Get the Propsoal Hierarchy data
            hierarchyMap = (HashMap) data.elementAt(2);
            if(hierarchyMap !=null && hierarchyMap.size() > 0){
                String parentPropNo =  hierarchyMap.get("PARENT_PROPOSAL").toString();
                if(proposalID !=null && parentPropNo !=null && parentPropNo.equals(proposalID)){
                    hierarchyMap.put("IS_PARENT", new Boolean(true));
                }
            }
             // Code Added for Proposal Hierarchy case# 3183 - End
            // Get the Proposal Hierarchy Tree data
            proposalHierarchyBean = (ProposalHierarchyBean)data.elementAt(3);

            /** Check if the selected proposal is in Hierarchy. If it is in Hierarchy
             *then check for the parent and then set the values
             */
            if(hierarchyMap!= null){
                boolean inHierarchy = ((Boolean)hierarchyMap.get("IN_HIERARCHY")).booleanValue();
                boolean isParent = false;
                if(inHierarchy) {
                    setHierarchy(inHierarchy);
                    setParentPropNo((String)hierarchyMap.get("PARENT_PROPOSAL"));
                    isParent = ((Boolean)hierarchyMap.get("IS_PARENT")).booleanValue();
                    if(isParent){
                        setParentProposal(isParent);
                        updateData();
                        //Added for Showing icon after joining by tarique start here
                        create.setVisible(false);
                        // Code Modified for Proposal Hierarchy case# 3183 - Start
                         remove.setVisible(false);
//                        remove.setVisible(true);
                        // Code Modified for Proposal Hierarchy case# 3183 - End
                        join.setVisible(true);
                        btnSyncAll.setVisible(true);
                        mnuItmSyncAll.setVisible(true);

                    }else{
                        create.setVisible(false);
                        join.setVisible(false);
                        remove.setVisible(true);
                        btnSyncAll.setVisible(true);
                        btnSyncAll.setToolTipText("Sync To Parent");
                        btnSyncAll.setIcon(new ImageIcon(getClass().getClassLoader().
                        getResource(CoeusGuiConstants.SYNC_ICON)));
                        mnuItmSyncAll.setVisible(true);
                        mnuItmSyncAll.setText("Sync To Parent");
                        mnuItmSyncAll.setMnemonic('o');
                    }//Added for Showing icon after joining by tarique end here
                }
            }

            if(hierarchy){
                if(hierarchyController == null){
                    hierarchyController = new ProposalHierarchyContoller(proposalHierarchyBean);
                    hierarchyController.setFormData(unitNumber);
                    scrPnHierarchy = new JScrollPane();
                    scrPnHierarchy.setViewportView( hierarchyController.getControlledUI() );
                    tbdPnProposal.addTab("Proposal Hierarchy", scrPnHierarchy);
                }else if(isParentProposal()){
                    hierarchyController.setProposalHierarchyBean(proposalHierarchyBean);
                    hierarchyController.setFormData(unitNumber);
                }
            }
        }

        if(joinFlag< 0) {
            CoeusOptionPane.showErrorDialog(
            coeusMessageResources.parseMessageKey(JOIN_ERROR));
            return;
        }else if(joinFlag == 100){
            CoeusOptionPane.showInfoDialog(
                coeusMessageResources.parseMessageKey(SPONSOR_NOT_SAME));
            return ;
        }
    }
    /** Get the search DevProposalSearch window(2 tab) and get the selected proposal
     *number from the search window.This is called when join action is performed
     *@ returns proposal number
     */
    private String getProposalIdBySearch() throws CoeusException,Exception{
        String proposalNumber  = "";
        CoeusSearch coeusSearch = new CoeusSearch(mdiForm, "PROPOSALDEVSEARCHNOROLES", 1);
        coeusSearch.showSearchWindow();
        HashMap proposalMap = coeusSearch.getSelectedRow();
        if(proposalMap!= null){
            proposalNumber = (String)proposalMap.get("PROPOSAL_NUMBER");
        }
        return proposalNumber;

    }
    /** This method will communicate with the server and gets the updated data for the
     *existing propsoal number
     */
    private CoeusVector linkToParent(String childProposalNumber , int childTypeCode) throws CoeusException,Exception{
        String connectTo = CoeusGuiConstants.CONNECTION_URL + HIERARCHY_SERVLET;
        RequesterBean request = new RequesterBean();
        CoeusVector cvLinkData = null;
        Vector vecProcParams = new Vector();
        String parentProposalNumber = "";
        String childPropNumber = "";

        if(isParentProposal()){
            parentProposalNumber = proposalID;
            childPropNumber = childProposalNumber;
        }else{
            parentProposalNumber = childProposalNumber;
            childPropNumber = proposalID;
        }
        //Added with COEUSDEV-197:Budget validation rules are NOT applied when a proposal is linked to a hierarchy and creating new hierarchy
        char errType = validateForHierarchy(childPropNumber,false);
        if(errType == VALIDATION_ERROR){
            return null;
        }else if(errType == VALIDATION_WARNING){
            int   option = CoeusOptionPane.showQuestionDialog(
                    coeusMessageResources.parseMessageKey(CONFIRM_LINK),
                    CoeusOptionPane.OPTION_YES_NO,CoeusOptionPane.DEFAULT_NO);
            if(option == JOptionPane.NO_OPTION){
                return null;
            }
        }
        //COEUSDEV-197:End
        vecProcParams.add(0,childPropNumber);
        vecProcParams.add(1,parentProposalNumber);// Parent proposal Number
        vecProcParams.add(2,new Integer(childTypeCode));
        //Added for case id:3183 - start
        vecProcParams.add(3,new Boolean(isParentProposal()));
        //Added for case id:3183 - end
        request.setDataObjects(vecProcParams);
        request.setFunctionType(JOIN_PROPOSAL);
        AppletServletCommunicator comm
        = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response!=null){
            if (response.isSuccessfulResponse()){
                cvLinkData = (CoeusVector)response.getDataObjects();
                CoeusOptionPane.showInfoDialog("Proposal "+childPropNumber+" has been linked to Proposal "+" "+
                parentProposalNumber);
            }else {
                throw new CoeusException(response.getMessage());
            }
        }

        return cvLinkData;
    }
    /** This method will be triggered when the create Hierarchy action is performed
     *It will do the initial validations and then create the hierarchy
     */
    private void createHierarchy() throws CoeusException,Exception{

        Vector data = canBuildHierarchy(proposalID,CREATE_ACTION);
        boolean isRightExists =((Boolean)data.get(0)).booleanValue();
        int code = ((Integer)data.get(1)).intValue();

         if(!isRightExists){
            CoeusOptionPane.showErrorDialog(
            coeusMessageResources.parseMessageKey(NO_RIGHT_TO_JOIN));
            return ;
        }

        if(validateHierarchy(code , proposalID)){
            //Added with COEUSDEV-197:Budget validation rules are NOT applied when a proposal is linked to a hierarchy and creating new hierarchy
            char errType = validateForHierarchy(proposalID,false);
            if(errType == VALIDATION_ERROR){
                return;
            }else if(errType == VALIDATION_WARNING){
                int option = CoeusOptionPane.showQuestionDialog(
                        coeusMessageResources.parseMessageKey(CONFIRM_CREATE_PARENT),
                        CoeusOptionPane.OPTION_YES_NO,CoeusOptionPane.DEFAULT_NO);
                if(option == JOptionPane.NO_OPTION){
                    return;
                }
            }
            //COEUSDEV-197:End
            createParentProposal();
            setHierarchy(true);
            setParentProposal(false);
            enableDisableMenuItems();
        }
    }
    /** This method will check whether the selected propsoal is capable to
     *create/Join with the hierarchy.
     *It will communicate with the server and carries out the code and right details
     *required for joining and createing action
     *@returns vector contains the code(as Exception codes) and right(Checks for
     *the Proposal aggregator role)
     */
    private Vector canBuildHierarchy(String proposalID,String actionType) throws CoeusException{
        Vector data = null;
        String connectTo = CoeusGuiConstants.CONNECTION_URL +HIERARCHY_SERVLET;
        RequesterBean request = new RequesterBean();
        request.setFunctionType(CAN_BUILD_HIERARCHY);
        request.setDataObject(proposalID);
        request.setId(actionType);

        AppletServletCommunicator comm
        = new AppletServletCommunicator( connectTo, request );
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response == null) {
            response = new ResponderBean();
            response.setResponseStatus(false);
        }
        if (response.isSuccessfulResponse()) {
            data = response.getDataObjects();
        }else{
            throw new CoeusException(response.getMessage());
        }
        return data;
    }// End canBuildHierarchy

    /** If the necessary validations are passed it will be creating the
     *hierarchy. It will communicate with the server to create a hierarchy
     *It will create a new proposal number as a parent and updates the parent
     */
    private void createParentProposal() throws CoeusException,Exception{

        Vector data  = createAndUpdateParent();
        int createFlag = 0 ;
        if(data!= null && data.size()>0) {
            createFlag = Integer.parseInt(data.elementAt(0).toString());
            //Code commented and added for case#3183 - Proposal Hierarchy - starts
            //this.proposalDevelopmentFormBean = (ProposalDevelopmentFormBean) data.elementAt(1);
            ProposalDevelopmentFormBean bean = (ProposalDevelopmentFormBean) data.elementAt(1);
//            CoeusOptionPane.showInfoDialog("Parent Proposal "+
//            proposalDevelopmentFormBean.getProposalNumber() +" has been created. ");
            CoeusOptionPane.showInfoDialog("Parent Proposal "+
            bean.getProposalNumber() +" has been created. ");
            //Code commented and added for case#3183 - Proposal Hierarchy - ends
            //observable.setFunctionType( functionType );
            //observable.notifyObservers( this.proposalDevelopmentFormBean );
        }
        if(createFlag< 0) {
            CoeusOptionPane.showErrorDialog(
                coeusMessageResources.parseMessageKey(CREATE_ERROR));
            return;
        }
    }
    /** This method will communicate with the server to create a parent proposal number
     *and update the parent.
     *@return vector contains ProposalDevelopmentFormBean  and the int flag specifies
     *sucessfull updation and creation of the parent proposa number
     */
    private Vector createAndUpdateParent() throws CoeusException{
        String connectTo = CoeusGuiConstants.CONNECTION_URL + HIERARCHY_SERVLET;
        RequesterBean request = new RequesterBean();
        Vector data = null;
        Vector vecProcParams = new Vector();
        vecProcParams.addElement(proposalID);
        vecProcParams.addElement(proposalDevelopmentFormBean.getOwnedBy());
        request.setDataObjects(vecProcParams);
        request.setFunctionType(CREATE_PARENT_PROPOSAL);
        AppletServletCommunicator comm
        = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response!=null){
            if (response.isSuccessfulResponse()){
                data = (Vector)response.getDataObjects();
            }
            else {
                throw new CoeusException(response.getMessage());
            }
        }
        return data;
    }
    //Removes the proposal from hierarchy
    private void removeProposalFromHierarchy() throws CoeusException{

        Vector data  = canBuildHierarchy(proposalID , JOIN_ACTION);
        boolean isRoleExists = ((Boolean)data.get(0)).booleanValue();

        if(!isRoleExists){
            CoeusOptionPane.showErrorDialog(
            coeusMessageResources.parseMessageKey(NO_RIGHT_TO_JOIN));
            return ;
        }
        //Added for case#3183 - Proposal hierarchy
        //validate the proposal budget status while removing from hierarchy
        data  = canBuildHierarchy(parentPropNo , JOIN_ACTION);
        int code = ((Integer)data.get(1)).intValue();
        if(code == 900){
            CoeusOptionPane.showInfoDialog(
            coeusMessageResources.parseMessageKey(BUDGET_STATUS_COMPLETE_REMOVE));
            return ;
        }
        //Added for case#3183 - Proposal hierarchy - ends
        int answer = CoeusOptionPane.showQuestionDialog(
        coeusMessageResources.parseMessageKey(REMOVE_FROM_HIERARCHY_MESSAGE),
        CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_NO);
        if (answer == JOptionPane.YES_OPTION) {
            String connectTo = CoeusGuiConstants.CONNECTION_URL + HIERARCHY_SERVLET;
            RequesterBean request = new RequesterBean();
            CoeusVector cvData = new CoeusVector();

























            cvData.addElement(getParentPropNo()); //Parent proposal no
            cvData.addElement(proposalID);        //Child proposal no
            request.setDataObject(cvData);

            request.setFunctionType(REMOVE_PROP_FROM_HIERARCHY);
            AppletServletCommunicator comm
                                = new AppletServletCommunicator(connectTo, request);
            comm.send();
            ResponderBean response = comm.getResponse();
            if (response!=null){
                if (response.isSuccessfulResponse()){
                    tbdPnProposal.remove(scrPnHierarchy);
                    //scrPnHierarchy.remove(hierarchyController.getControlledUI());
                    hierarchyController.cleanUp();
                    hierarchyMap = null;
                    hierarchyController = null;
                    scrPnHierarchy = null;
                    //Added by tarique for showing menu items and icon start
                    setHierarchy(false);
                    btnSubmitProposal.setEnabled(true);
                    submitApproval.setEnabled(true);
                    btnApproveProposal.setEnabled(true);
                    approve.setEnabled(true);
                    submitSponsor.setEnabled(true);
                    showRouting.setEnabled(true);
                    validationChecks.setEnabled(true);
                    approvalMap.setEnabled(true);
                    btnApprovalMap.setEnabled(true);
                    create.setVisible(true);
                    join.setVisible(true);
                    // Code Modified for Proposal Hierarchy case# 3183 - Start
                    hasSubmitForApprovalRt = true;
                    // Code Modified for Proposal Hierarchy case# 3183 - End
                    enableDisableMenuItems();
                    //Added by tarique for showing menu items and icon end
                }else {
                    throw new CoeusException(response.getMessage());
                }
            }
        }
    }

    // Addded for case 2110 Start 5 To get Current Locks for loggedinUser
     private void showLocksForm() throws edu.mit.coeus.exception.CoeusException{
        CurrentLockForm currentLockForm = new CurrentLockForm(mdiForm,true);
        currentLockForm.display();
    }
    //Added for case 2110 End 5

    private void refreshPropHierarchyTab() throws CoeusException{
        if(!hierarchy){
            return ;
        }

        String connectTo = CoeusGuiConstants.CONNECTION_URL + HIERARCHY_SERVLET;
        RequesterBean request = new RequesterBean();
        CoeusVector cvData = null;

        request.setDataObject(proposalID);
        request.setFunctionType(REFRESH_HIERARCHY);
        AppletServletCommunicator comm =
                            new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response!=null){
            if (response.isSuccessfulResponse()){
                cvData = (CoeusVector)response.getDataObjects();
            }else {
                throw new CoeusException(response.getMessage());
            }
        }

        if(cvData == null){
            return ;
        }
        // Get the Propsoal Hierarchy data
        hierarchyMap = (HashMap)cvData.elementAt(0);
        // Get the Proposal Hierarchy Tree data
        proposalHierarchyBean = (ProposalHierarchyBean)cvData.elementAt(1);

        /** Check if the selected proposal is in Hierarchy. If it is in Hierarchy
         *then check for the parent and then set the values
         */
        if(hierarchyMap!= null){
            boolean inHierarchy = ((Boolean)hierarchyMap.get("IN_HIERARCHY")).booleanValue();
            boolean isParent = false;
            if(inHierarchy) {
                setHierarchy(inHierarchy);
                setParentPropNo((String)hierarchyMap.get("PARENT_PROPOSAL"));
                isParent = ((Boolean)hierarchyMap.get("IS_PARENT")).booleanValue();
                if(isParent){
                    setParentProposal(isParent);
                }
            }
        }

        hierarchyController.setProposalHierarchyBean(proposalHierarchyBean);
        hierarchyController.setFormData(unitNumber);

    }//End refreshPropHierarchyTab


    private boolean validateHierarchy(int code , boolean isJoin , String propNo){
        return validateHierarchy(code , isJoin , propNo , false);
    }

    private boolean validateHierarchy(int code , String propNo){
        return validateHierarchy(code , false , propNo , false);
    }

    /*
     */
    private boolean validateHierarchy(int code , boolean isJoin , String propNo , boolean searchedProposal){
        String message = "";

        if(code==100){          //Check if the proposal already in the hierarchy
            message = "Proposal " + propNo + " " + coeusMessageResources.parseMessageKey(ALREADY_LINKED);
            CoeusOptionPane.showInfoDialog(message);
            return false;
        }else if(code==200){    //Check for the proposal status. If the proposal status is In Progress Or Rejected then only establish the hierarchy
            message = "Proposal " + propNo + " " + coeusMessageResources.parseMessageKey(STATUS_VALIDATION);
            CoeusOptionPane.showInfoDialog(message);
            return false;
        }else if(code==300){    //Check whether the proposal has budget or Not
            message = "Proposal " + propNo + " " + coeusMessageResources.parseMessageKey(NO_BUDGET);
            CoeusOptionPane.showInfoDialog(message);
            return false;
        }else if(code ==400){   //Check for Investigator
            message = "Proposal " + propNo + " " + coeusMessageResources.parseMessageKey(INVESTIGATOR_VALIDATION);
            CoeusOptionPane.showInfoDialog(message);
            return false;
        }else if(code == 700 && !searchedProposal){  //Check for budget version whether its final
            message = "Budget for Proposal " + propNo + " " + coeusMessageResources.parseMessageKey(BUDGET_VER_VALIDATION);
            int answer = CoeusOptionPane.showQuestionDialog(message,
                    CoeusOptionPane.OPTION_OK_CANCEL, CoeusOptionPane.DEFAULT_NO);
            if (answer == JOptionPane.CANCEL_OPTION) {
                return false;
            }
        }else if(isJoin){
            /** Check if the present proposal is parent Or not. If parent do the necessary validation
             *else if the present proposal is not in the hierarchy and not a parene then do the necessary
             *validation
             */
            if(isParentProposal()){
                /** If the selected prposal number from search window presents in the hiearchy and
                 *the parent proposal number is trying to join then it has to select a propsoal
                 *which is not there in the hierarchy
                 */
                if(code==500){
                    CoeusOptionPane.showInfoDialog(
                        coeusMessageResources.parseMessageKey(SELECT_PROP_NOT_IN_HIERARCHY));
                    return false;
                }
                //Added for case#3183 - Proposal hierarchy - starts
                //validate the proposal budget status while linking to hierarchy
                else if(code==900){
                    CoeusOptionPane.showInfoDialog(
                        coeusMessageResources.parseMessageKey(BUDGET_STATUS_COMPLETE_LINK));
                    return false;
                }
                //Added for case#3183 - Proposal hierarchy - ends
            }else{
                //if(code==600){
                if(code==600 || code==700){
                    // which is not in the hierarchy
                    CoeusOptionPane.showInfoDialog(
                    coeusMessageResources.parseMessageKey(SELECT_PARENT_PROP_TO_JOIN));
                    return false;
                }
                //Added for case#3183 - Proposal hierarchy - starts
                //validate the proposal budget status while linking to hierarchy
                else if(code==900 && searchedProposal){
                    CoeusOptionPane.showInfoDialog(
                        coeusMessageResources.parseMessageKey(BUDGET_STATUS_COMPLETE_LINK));
                    return false;
                }
                //Added for case#3183 - Proposal hierarchy - ends
            }
        }

        return true;
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

    // Added by Nadh to implement the change password
    private void showChangePassword(){
        if(changePassword == null) {
            changePassword = new ChangePassword();
        }
        changePassword.display();
    }// End Nadh

    /**
     *  Added a method to show the Printing form from the File menu.
     *  This private method will get called from the actionPerformed method
     * by checking the menu item as 'Print' of File meanu
     *
     * Added by Geo on 03-Sep-2004
     */
    //    private void showPrintForm(){
    //        ProposalPrintForm printForm = new ProposalPrintForm(proposalDevelopmentFormBean);
    //        printForm.setFormData();
    //        printForm.display();
    //    }
    private void showPrintForm(){
        PrintProposal printForm = new PrintProposal(proposalDevelopmentFormBean,mdiForm);
        printForm.setFormData();
        //Commented for Case#2445 - proposal development print forms linked to indiv sponsor, should link to sponsor hierarchy
        //Method call is moved to setFormData() in PrintProposal
//        printForm.display();
        //Case#2445 - End
    }

    /**
     *  This method shows the Print Certification window from the Action menu.
     *  This private method will get called from the actionPerformed method
     *  by checking the menu item as 'Print Certifications..' of Action meanu.
     *
     **/
    private void showPrintCertifications(){
        Vector veInvestigators = proposalDevelopmentFormBean.getInvestigators();
        if ( veInvestigators != null && veInvestigators.size() > 0 ){
            PrintCertifications printCertifications =
            //start case 2358
            new PrintCertifications(proposalDevelopmentFormBean);
//            new PrintCertifications(proposalDevelopmentFormBean.getTitle(),
//            proposalDevelopmentFormBean.getSponsorName(),proposalDevelopmentFormBean.getSponsorCode(), proposalDevelopmentFormBean.getPrimeSponsorCode(),veInvestigators);
            //end case 2358
        }
    }

    /**
     *This method returns a vector of broken rules beans if any else it returns null
     */
    private Vector getBrokenRulesData(boolean validateFinal) throws Exception {

        int budgetVersion = checkBudgetFinalVersion(validateFinal);
        Vector inputVector= new Vector();
        Vector dataObjects = null;
        String leadUnitNumber = proposalDevelopmentFormBean.getOwnedBy();
        //Code commented and modified for Case#2785 - Routing Enhancements - starts
//        inputVector.add(proposalID);
//        inputVector.add(leadUnitNumber);
//        String connectTo = CoeusGuiConstants.CONNECTION_URL +
//        CoeusGuiConstants.PROPOSAL_ACTION_SERVLET;
        //Modified with case 2158:Budget Validations
        inputVector.add(new Integer(ModuleConstants.PROPOSAL_DEV_MODULE_CODE));//module code
        inputVector.add(proposalID);//module item key
        inputVector.add(new Integer(budgetVersion));//budgetversion passed as moduleitemkeysequence
        inputVector.add(leadUnitNumber);//unit number
        inputVector.add(new Integer(1));//approval sequence
        //2158 End
        String connectTo = CoeusGuiConstants.CONNECTION_URL + "/RoutingServlet";

        RequesterBean request = new RequesterBean();
        request.setFunctionType('N');
        request.setDataObjects(inputVector);
        AppletServletCommunicator comm
                = new AppletServletCommunicator( connectTo, request );
        //Code commented and modified for Case#2785 - Routing Enhancements - ends
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response == null) {
            response = new ResponderBean();
            response.setResponseStatus(false);

            // response.setMessage(coeusMessageResources.parseMessageKey(
            // "server_exceptionCode.1000"));
        }
        if (response.isSuccessfulResponse()) {
            dataObjects = response.getDataObjects();
        }

        return dataObjects;
    }

    /**
     * This method is invoked when the user clicks ADD PROPOSAL VIEWER in the Edit menu
     * of Proposal module
     */
    private void showProposalViewer(String proposalNumber){
//        int selectedRow = tblProposal.getSelectedRow();
        if (proposalNumber == null) {
            log(coeusMessageResources.parseMessageKey(
            "protoBaseWin_exceptionCode.1052"));
        } else {
            try{
                //proposalDevelopmentFormBean = (ProposalDevelopmentFormBean)proposalStatus.get(0);
                int code = proposalDevelopmentFormBean.getCreationStatusCode();
                // Get the description from the data object
//                String statusDescription = (String)tblProposal.getValueAt(selectedRow, 2);
//                String statusDescription = proposalDevelopmentFormBean.getCreationStatusDescription();

                boolean valid = performValidation(proposalNumber);
                //if(statusDescription.equalsIgnoreCase("In Progress")){
                if(code==1){
                    log("Cannot perform this function on a proposal in progress");
                    //}else if(statusDescription.equalsIgnoreCase("Rejected")){
                }else if(code==3){
                    log("Cannot perform this function on a rejected proposal");
                }else{
                    //System.out.println("Value of valid is "+valid);
                    if(!valid){

                        log("You do not have a right to add proposal viewer");
                    }else{
                        // Get the details from the bean rather than the referenced table.
                        String sponsorCode = proposalDevelopmentFormBean.getSponsorCode();
                        String sponsorName = proposalDevelopmentFormBean.getSponsorName();
                        String sponsorCodeName = sponsorCode + " : " + sponsorName;
                        if (sponsorCode.equals(null)) {
                            sponsorCodeName = "";
                        }
                        ProposalViewersForm proposalViewform =
                        new ProposalViewersForm(proposalNumber,
                        CoeusGuiConstants.PROPOSAL_VIEWER_ROLE_ID,sponsorCodeName);
                    }
                }
            }catch( Exception err ){
                err.printStackTrace();
                CoeusOptionPane.showInfoDialog(err.getMessage());
            }
        }
    }
    
    /* JM 5-17-2016 show copied from proposal number */
    private void showCopiedFromPropNum() {
    	String oldProposalNumber = proposalDevelopmentFormBean.getCopiedFromPropNum();
    	edu.vanderbilt.coeus.propdev.CopiedFromPropNum copy = 
    			new edu.vanderbilt.coeus.propdev.CopiedFromPropNum(oldProposalNumber);
    	copy.popupFrame();
    }
    /* JM END */

    private boolean performValidation(String proposalNumber){

        Vector rightData = null;
        boolean hasRights = false;
        String connectTo = CoeusGuiConstants.CONNECTION_URL + "/coeusFunctionsServlet";
        RequesterBean request = new RequesterBean();
        Vector vecFnParams = new Vector();

        Vector vecGetParams = new Vector();
        vecGetParams.addElement(USER_HAS_PROPOSAL_ROLE);
        //Modified for Case#3587 - multicampus enhancement  - Start
//        vecGetParams.addElement(USER_HAS_OSP_RIGHT);
        vecGetParams.addElement(USER_HAS_DEPARTMENTAL_RIGHT);
        //Case#3587 - End

        vecFnParams.addElement(mdiForm.getUserName());
        vecFnParams.addElement(new Integer(PROPOSAL_ROLE));
        vecFnParams.addElement(proposalNumber);
        vecFnParams.addElement(APPROVE_PROPOSAL_RIGHT);

        Vector vecDataToServer = new Vector();
        vecDataToServer.addElement(vecGetParams);
        vecDataToServer.addElement(vecFnParams);

        request.setDataObjects(vecDataToServer);

        request.setDataObject("GET_RIGHTS_FOR_PROPOSAL");
        AppletServletCommunicator comm
        = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();

        if (response!=null){
            if (response.isSuccessfulResponse()){
                rightData = (Vector)response.getDataObject();
            }
        }

        if(rightData != null){
            int userHasRole = ((Integer)rightData.elementAt(0)).intValue();
            int userHasOSPRight = ((Integer)rightData.elementAt(1)).intValue();

            //System.out.println("userHasRole is "+userHasRole);
            //System.out.println("userHasOSPRight is "+userHasOSPRight);

            if(userHasRole == 0 && userHasOSPRight == 0){
                hasRights = false;
            }else{
                hasRights = true;
            }
        }
        return hasRights;
    }

    /**
     * display alert message
     *
     * @param mesg the message to be displayed
     */
    private void log(String mesg) {
        CoeusOptionPane.showErrorDialog(mesg);
    }

    //raghuSV to do validation checks
    //Method modified with case 2158 : Budget Validations
    //Added param validateFinal to set whether a hard stop is required if no final budget is selected.
    //Performs validation and returns an error Type:E for error, W for warning, G gor grantsGov Error
    private char doValidation(boolean validateFinal) throws Exception{

        char errType = 0;
        Vector vecBrokenRules=null;
        if(this.functionType != CoeusGuiConstants.DISPLAY_MODE ){
            saveProposalDetails( SAVE_RETAIN_LOCK );
        }
        vecBrokenRules = getBrokenRulesData(validateFinal);
        //Code commented and modified for Case#2785 - Routing Enhancements - starts
        if (vecBrokenRules != null && vecBrokenRules.size() > 0){
            //ProposalValidationChecksForm valChecks = new ProposalValidationChecksForm(mdiForm,vecBrokenRules,proposalID );
            edu.mit.coeus.routing.gui.RoutingValidationChecksForm valChecks =
                    new edu.mit.coeus.routing.gui.RoutingValidationChecksForm(mdiForm,vecBrokenRules,3, proposalID );
            valChecks.display();
            if(valChecks.isError()){
                errType = VALIDATION_ERROR;
            }else{
                errType = VALIDATION_WARNING;
            }

            //Code commented and modified for Case#2785 - Routing Enhancements - ends
        }else if(proposalDevelopmentFormBean.isS2sOppSelected() && !validateGrantsGov()){
            errType = GRANTSGOV_ERROR;
//            String msg=coeusMessageResources.parseMessageKey("validationChecks_exceptionCode.1904");
//            CoeusOptionPane.showInfoDialog(msg);
            //Code added for Case#3611 - Validation checks not working - starts
//        } else {
//            CoeusOptionPane.showInfoDialog(
//                    coeusMessageResources.parseMessageKey("validationChecks_exceptionCode.1904"));
            //Code added for Case#3611 - Validation checks not working - ends
        }
        return errType;
    }
    private boolean validateGrantsGov(){
            S2SHeader headerParam = new S2SHeader();
            ProcessGrantsSubmission s2sValidation = new ProcessGrantsSubmission(headerParam);
            headerParam.setSubmissionTitle(proposalDevelopmentFormBean.getProposalNumber());
            headerParam.setOpportunityId(proposalDevelopmentFormBean.getProgramAnnouncementNumber());
            headerParam.setCfdaNumber(proposalDevelopmentFormBean.getCfdaNumber());
            headerParam.setAgency(proposalDevelopmentFormBean.getSponsorCode()+" : "+proposalDevelopmentFormBean.getSponsorName());
            HashMap streamParam = new HashMap();
            streamParam.put("PROPOSAL_NUMBER", proposalDevelopmentFormBean.getProposalNumber());
            headerParam.setStreamParams(streamParam);
            try{
                s2sValidation.validateApplication(null);
            }catch (S2SValidationException  s2sException){
                S2SErrorFormController errContlr = new S2SErrorFormController();
//                if(s2sException.getOppSchemaUrl()==null || s2sException.getOppSchemaUrl().equals("")){
//                    s2sException.setOppSchemaUrl(this.getOpportunityInfoBean().getSchemaUrl());
//                }
//                if(s2sException.getOppInstrUrl()==null || s2sException.getOppInstrUrl().equals("")){
//                    s2sException.setOppInstrUrl(this.getOpportunityInfoBean().getInstructionUrl());
//                }
                errContlr.setFormData(headerParam,s2sException);
                errContlr.display();
                return false;
//            edu.mit.coeus.utils.CoeusOptionPane.showErrorDialog(s2sException.getHtmlOutput());
            }catch (Exception  ex){
                ex.printStackTrace();
                //CoeusOptionPane.showErrorDialog(this, ex.getMessage()); // JM
                CoeusOptionPane.showErrorDialog(this, "Unexpected Grants.gov validation error. Unable to route proposal. Please contact Coeus Help."); //JM
                return false; // JM 7-26-2013 return false if it throws an error!
            }
            return true;
    }
    private static final String s2sServlet = CoeusGuiConstants.CONNECTION_URL+
    "/S2SServlet";

    private void submitGrantsGov() throws CoeusException{
        if(this.functionType != CoeusGuiConstants.DISPLAY_MODE ){
            try{
                saveProposalDetails( SAVE_RETAIN_LOCK );
            }catch(Exception ex){
                ex.printStackTrace();
                throw new CoeusException(ex.getMessage());
            }
        }
        HashMap params = new HashMap();
        params.put("PROPOSAL_NUMBER", proposalDevelopmentFormBean.getProposalNumber());
        //        params.put("SPONSOR_CODE", proposalDevelopmentFormBean.getSponsorCode());
        //        params.put("SPONSOR_NAME", proposalDevelopmentFormBean.getSponsorName());
        S2SHeader headerParam = new S2SHeader();
        headerParam.setSubmissionTitle(proposalDevelopmentFormBean.getProposalNumber());
        if(proposalDevelopmentFormBean.getCfdaNumber()!=null){
            StringBuffer tempCfdaNum = new StringBuffer(proposalDevelopmentFormBean.getCfdaNumber());
            int charIndex = tempCfdaNum.indexOf(".");
            if(charIndex==-1){
                tempCfdaNum.insert(2,'.');
            }
            headerParam.setCfdaNumber(tempCfdaNum.toString());
        }
        //coeus-675 start
        // JM 6-22-2015 old code works; new code breaks! fixing it
//        headerParam.setOpportunityId(proposalDevelopmentFormBean.getProgramAnnouncementNumber());
        if (proposalDevelopmentFormBean.getProgramAnnouncementNumber() != null &&
        		proposalDevelopmentFormBean.getProgramAnnouncementNumber().length() > 0) {
        	headerParam.setOpportunityId(proposalDevelopmentFormBean.getProgramAnnouncementNumber().toUpperCase().trim().replaceAll(" ", "")); // JM
        }
        // JM END
        //coeus-7675 end
        headerParam.setAgency(proposalDevelopmentFormBean.getSponsorCode()+" : "+
        proposalDevelopmentFormBean.getSponsorName());

        headerParam.setStreamParams(params);
        try{
            ProcessGrantsSubmission grantSubmission = new ProcessGrantsSubmission(headerParam);
            grantSubmission.setPropDevBean(proposalDevelopmentFormBean);
            registerObserver( this );
            grantSubmission.setObservable(observable);
            grantSubmission.setFunctionType(getFunctionType());
            grantSubmission.showS2SSubmissionForm();
        }finally{
            unRegisterObserver(this);
        }
    }
    public void submitSponsor() throws Exception{
        try{
//            String propNumber = "";
            String proposalType = proposalDevelopmentFormBean.getProposalTypeDesc();
            int proposalTypeCode = proposalDevelopmentFormBean.getProposalTypeCode();
            
            /* JM 3-4-2016 we don't want to pop this up for resubmissions */
            /*
            if(proposalTypeCode==6){
           	
                SubmitToSponsor submitToSponsor = new SubmitToSponsor(mdiForm,true,proposalID);




                if(!isHierarchy()&& !validateForSubmitToSponsor() ){

                    return;
                }

                submitToSponsor.setHierarchy(isHierarchy());
                submitToSponsor.setParent(isParentProposal());
                submitToSponsor.setProposalDevelopmentFormBean(proposalDevelopmentFormBean);
                submitToSponsor.setFormMode('S');   //Added by Vyjayanthi to set the form mode

                submitSponsor.setEnabled(submitToSponsor.display());
                return ;

            }else */
            /* JM END */	
            if(proposalDevelopmentFormBean.isS2sOppSelected()) {
                //Grants Gov candidate and submission type = Change Corrected
                S2SHeader headerParam = new S2SHeader();
                headerParam.setSubmissionTitle(proposalDevelopmentFormBean.getProposalNumber());
                if(proposalDevelopmentFormBean.getCfdaNumber()!=null){
                    StringBuffer tempCfdaNum = new StringBuffer(proposalDevelopmentFormBean.getCfdaNumber());
                    int charIndex = tempCfdaNum.indexOf(".");
                    if(charIndex==-1){
                        tempCfdaNum.insert(2,'.');
                    }
                    headerParam.setCfdaNumber(tempCfdaNum.toString());
                }
                headerParam.setOpportunityId(proposalDevelopmentFormBean.getProgramAnnouncementNumber());
                headerParam.setAgency(proposalDevelopmentFormBean.getSponsorCode()+" : "+ proposalDevelopmentFormBean.getSponsorName());
                HashMap params = new HashMap();
                params.put("PROPOSAL_NUMBER", proposalDevelopmentFormBean.getProposalNumber());
                headerParam.setStreamParams(params);

                RequesterBean request = new RequesterBean();
                request.setDataObject(headerParam);
                request.setFunctionType(S2SConstants.GET_DATA);
                AppletServletCommunicator comm = new AppletServletCommunicator();
                comm.setConnectTo(S2S_SERVLET);
                comm.setRequest(request);
                comm.send();
                ResponderBean response = comm.getResponse();
                if(response.isSuccessfulResponse()){
                    Object object[] = (Object[])response.getDataObject();
                    OpportunityInfoBean opportunityInfoBean = (OpportunityInfoBean)object[0];
                    int submissionTypeCode = opportunityInfoBean.getSubmissionTypeCode();
                    if(submissionTypeCode == 3) {//Change Corrected Submission
                        SubmitToSponsor submitToSponsor = new SubmitToSponsor(mdiForm,true,proposalID);
//			//Case : 2920 - Change corrected prop dev - Inst prop confirmation window
//                        submitToSponsor.setDevProposalText(coeusMessageResources.parseMessageKey("proposalSubmitToSponsor_exceptionCode.1126"));
                        submitToSponsor.setHierarchy(isHierarchy());
                        submitToSponsor.setParent(isParentProposal());
                        submitToSponsor.setProposalDevelopmentFormBean(proposalDevelopmentFormBean);
                        submitToSponsor.setFormMode('S');   //Added by Vyjayanthi to set the form mode
                        submitSponsor.setEnabled(submitToSponsor.display());
                        return ;
                    }
                }else{
                    Exception ex = response.getException();
                    if(ex==null){
                        throw new CoeusException(response.getMessage());
                    }else {
                        throw new CoeusException(ex.getMessage());
                    }
                }

            }//else{

                //propNumber = feedInstitutePropNumber(proposalID,instPropNumber,generate,submissionType,submissionStatus);
            //Added for the case # COEUSQA-1679-Modification for Final Document Indicator-start
            // CHeck Proposal is in hierachy and Narrative staus is complete
            if(!isHierarchy() && !validateForSubmitToSponsor()){
                return;
            } else{
                //Added for the case # COEUSQA-1679-Modification for Final Document Indicator-end
                feedInstitutePropNumber(proposalID,instPropNumber,generate,submissionType,submissionStatus);
                // CoeusOptionPane.showInfoDialog("Institute Proposal  "+propNumber +"  has been generated");
                submitSponsor.setEnabled(false);
                // Update the creattion status code
                updateChildStatus(proposalID);
                submitGrantsGov();
                //Added for the case # COEUSQA-1679-Modification for Final Document Indicator-start
            }
            //Added for the case # COEUSQA-1679-Modification for Final Document Indicator-end
            //}
        }catch(CoeusException coeusClientException){
            CoeusOptionPane.showErrorDialog(coeusClientException.getMessage());
            coeusClientException.printStackTrace();
        }catch(CoeusClientException coeusClientException){
            CoeusOptionPane.showDialog(coeusClientException);
            coeusClientException.printStackTrace();
        }
    }

    private void  feedInstitutePropNumber(String proposalID, String instPropNumber,
    String generate,String submissionType,String submissionStatus) throws CoeusClientException{

        Vector dataObjects = new Vector(3,2);
        String instProp="";
        RequesterBean request = new RequesterBean();

        dataObjects.addElement(proposalID);
        dataObjects.addElement(instPropNumber);
        dataObjects.addElement(generate);
        dataObjects.addElement(submissionType);
        dataObjects.addElement(submissionStatus);

        request.setFunctionType(FEED_INST_NUMBER);
        request.setDataObjects(dataObjects);
        AppletServletCommunicator comm = new AppletServletCommunicator(connect, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response.isSuccessfulResponse()){
//            System.out.println("SuccessFull Response");
            Vector vecDetails = response.getDataObjects();
            instProp = (String) vecDetails.get(0);
            if(instProp!=null || !instProp.equals("")){
                CoeusOptionPane.showInfoDialog("Institute Proposal  "+instProp +"  has been generated");
            }
        }else {
//            System.out.println("Error while loading Details...");
            throw new CoeusClientException(response.getMessage());
        }
    }


    // Added by chandra
    private void showMedusa() {
        if ( isSaveRequired() ){
            int confirm = CoeusOptionPane.showQuestionDialog(
            coeusMessageResources.parseMessageKey("medusaSaveConfirm_exceptionCode.1120"),
            CoeusOptionPane.OPTION_YES_NO,
            CoeusOptionPane.DEFAULT_YES);
            switch(confirm){
                case JOptionPane.YES_OPTION:
                    try{
                        saveProposalDetails( SAVE_RETAIN_LOCK );
                    }catch (Exception exception){
                        exception.printStackTrace();
                    }
                    showMedusaWidnow();
                    break;
                case JOptionPane.NO_OPTION:
                    break;
            }
        }else{
            showMedusaWidnow();
        }
    }

    // Added by Chandra
    private void  showMedusaWidnow(){
        try{
            ProposalAwardHierarchyLinkBean linkBean = new ProposalAwardHierarchyLinkBean();
            linkBean.setBaseType(CoeusConstants.DEV_PROP);
            linkBean.setDevelopmentProposalNumber(proposalID);
            if( ( medusaDetailform = (MedusaDetailForm)mdiForm.getFrame(
            CoeusGuiConstants.MEDUSA_BASE_FRAME_TITLE))!= null ){
                if( medusaDetailform.isIcon() ){
                    medusaDetailform.setIcon(false);
                }
                linkBean.setBaseType(CoeusConstants.DEV_PROP);
                linkBean.setDevelopmentProposalNumber(proposalID);
                medusaDetailform.setSelectedNodeId(proposalID);
                medusaDetailform.setSelected( true );
                return;
            }
            medusaDetailform = new MedusaDetailForm(mdiForm,linkBean);
            medusaDetailform.setVisible(true);
        }catch(Exception exception){
            exception.printStackTrace();
        }
    }

    // START======Added by Ranjeev

    /**
     * Method For Submit For Submit for Approval.
     * It does all validation checks on the Porposal and EDI calls
     * It shows the Broken Rules if any before Submitting for Approval
     */
    private void showSubmitForApproval() throws Exception {

        //called to update the proposalDevelopmentFormBean with changed value
        // in Start Date End Date and Activity Code
        proposalDetail.getFormData();

        // calls the Show Routing method to Do proposal submit Validation
        Vector parameters = new Vector();
        parameters.add(proposalDetail);
        parameters.add(this);
        parameters.add(proposalDevelopmentFormBean);
        parameters.add(tbdPnProposal);
        parameters.add(functionType+"");
        parameters.add(proposalInvestigator);

        ShowRouting showRouting = new ShowRouting(mdiForm,parameters,true);
        // Set the parent proposal data
        if(isHierarchy() && isParentProposal()){
            showRouting.setParentProposal(true);
            // COEUSDEV-172:Should NOT allow parent proposal to submit if any of the chidlren are not in sync - Start
            if(isSyncRequired()){
                CoeusOptionPane.showWarningDialog(coeusMessageResources.parseMessageKey("proposalSubmitValidation_exceptionCode.1138"));
                return;
            }
            // COEUSDEV-172:Should NOT allow parent proposal to submit if any of the chidlren are not in sync - End
        }else{
            showRouting.setParentProposal(false);
        }
        // Added for COEUSQA-2702 : YNQ blocker - Start
        try {
            // Modified for COEUSQA-2881 : Last Update User and Timestamp changes - Start
//            saveRequired = true;
//            saveProposalDetails( SAVE_RETAIN_LOCK );
//            showRouting.setProposalDevelopmentFormBean(proposalDevelopmentFormBean);
            if(functionType == TypeConstants.MODIFY_MODE){
                saveRequired = true;
                saveProposalDetails( SAVE_RETAIN_LOCK );
                showRouting.setProposalDevelopmentFormBean(proposalDevelopmentFormBean);
            }
            // Modified for COEUSQA-2881 : Last Update User and Timestamp changes - End
        }catch(Exception exp) {
            CoeusOptionPane.showInfoDialog(exp.getMessage());
            tbdPnProposal.setSelectedIndex(6);
            return ;
        }
        // Added for COEUSQA-2702 : YNQ blocker - End
        //Modified for the case#coeusqs-1679 -Modification for Final Document Indicator-start
        //if(showRouting.validateProposalEntry() ) {
        //The Validation message should through only when the propsal is not in heirarchy, so passes the boolean varible, to find
        // proposal is in hierarchy or not
        if(showRouting.validateProposalEntry(isHierarchy())) {
            SubmitForApprove submitForApprove = new SubmitForApprove(proposalDevelopmentFormBean);

            //proposalInvestigator.getInvestigatorAnswers
            Vector tempQuestList = proposalDevelopmentFormBean.getInvestigatorQuestions();
            //Added for COEUSDEV-346 : PI Certification Question can't be saved in Coeus Lite. - Start
            //To check whether all the investigaotrs are certified
            // call Proposal Save Operation before proposal validations
//            if(tempQuestList==null||tempQuestList.isEmpty()||
//            submitForApprove.isAllInvestigatorCertified(proposalInvestigator)) {
              // call Proposal Save Operation before EDI validations
//                try {
//                    saveRequired = true;
//                    saveProposalDetails( SAVE_RETAIN_LOCK );
//                }catch(Exception exp) {
//                    // Added by chandra - If the custom elements are null. then
//                    // throw the message and select the respective tab - start 8th July 2004
//                    CoeusOptionPane.showInfoDialog(exp.getMessage());
//                    tbdPnProposal.setSelectedIndex(6);
//                    return ;
//                    //exp.printStackTrace();
//                    // End Chandra - 8th July 2004
//                }
            // Commented for COEUSQA-2702 : YNQ blocker - Start
//             try {
//                 saveRequired = true;
//                 saveProposalDetails( SAVE_RETAIN_LOCK );
//             }catch(Exception exp) {
//                 CoeusOptionPane.showInfoDialog(exp.getMessage());
//                 tbdPnProposal.setSelectedIndex(6);
//                 return ;
//             }
            // Commented for COEUSQA-2702 : YNQ blocker - End
            if(tempQuestList==null||tempQuestList.isEmpty()||
            submitForApprove.isAllInvestigatorCertified(proposalDevelopmentFormBean.getProposalNumber())) {
            //COEUSDEV-346 : End

                // COEUSDEV-202: Error about mandatory questionnaire - Does not identify the questionnaire - -Start
//                // 4272: Maintain History of Questionnaires - Start
//                int mandatoryQnrCompleted = validateQuestionnaireCompleted();
//                if(mandatoryQnrCompleted == 1){
//                    CoeusOptionPane.showWarningDialog(coeusMessageResources.parseMessageKey("questionnaire_exceptionCode.1020"));
//                    return;
//                } else if(mandatoryQnrCompleted == 2){
//                    CoeusOptionPane.showWarningDialog(coeusMessageResources.parseMessageKey("questionnaire_exceptionCode.1021"));
//                    return;
//                //COEUSDEV-143 - The Questionnaires are being required for ALL submissions - even if they are marked NOT mandatory.
//                }else if(mandatoryQnrCompleted == 3){
//                    CoeusOptionPane.showWarningDialog(coeusMessageResources.parseMessageKey("questionnaire_exceptionCode.1028"));
//                    return;
//                }
//                //COEUSDEV-143 End
//                // 4272: Maintain History of Questionnaires - End

                Vector vecUnfilledQnr = validateQuestionnaireCompleted();

                if(vecUnfilledQnr != null && vecUnfilledQnr.size() > 0){
                    String mandatoryQnr = (String) vecUnfilledQnr.get(0);
                    String incompleteQnr = (String) vecUnfilledQnr.get(1);
                    String newVersionQnr = (String) vecUnfilledQnr.get(2);

                    boolean mandatoryQnrPresent = false;
                    boolean incompleteQnrPresent = false;
                    boolean newVersionQnrPresent = false;

                    if(mandatoryQnr != null && !"".equals(mandatoryQnr.trim())){
                        mandatoryQnrPresent = true;
                    }

                    if(incompleteQnr != null && !"".equals(incompleteQnr.trim())){
                        incompleteQnrPresent = true;
                    }

                    if(newVersionQnr != null && !"".equals(newVersionQnr.trim())){
                        newVersionQnrPresent = true;
                    }

                    String validationMessage = "";

                    if(mandatoryQnrPresent){
                        validationMessage = coeusMessageResources.parseMessageKey("questionnaire_exceptionCode.1020");

                        StringTokenizer stokenMessage = new StringTokenizer(mandatoryQnr,"~");
                        while (stokenMessage.hasMoreTokens()){
                            String message = stokenMessage.nextToken();
                            validationMessage = validationMessage + "\n \t" + message;
                        }

                    }

                    if(incompleteQnrPresent){
                        if(mandatoryQnrPresent){
                            validationMessage = validationMessage + "\n \n";
                        }
                        validationMessage = validationMessage + coeusMessageResources.parseMessageKey("questionnaire_exceptionCode.1028") ;

                        StringTokenizer stokenMessage = new StringTokenizer(incompleteQnr,"~");
                        while (stokenMessage.hasMoreTokens()){
                            String message = stokenMessage.nextToken();
                            validationMessage = validationMessage + "\n \t" + message;
                        }

                    }

                    if(newVersionQnrPresent){
                        if(incompleteQnrPresent || mandatoryQnrPresent){
                            validationMessage = validationMessage + "\n \n";
                        }
                        validationMessage = validationMessage  + coeusMessageResources.parseMessageKey("questionnaire_exceptionCode.1021") ;

                        StringTokenizer stokenMessage = new StringTokenizer(newVersionQnr,"~");
                        while (stokenMessage.hasMoreTokens()){
                            String message = stokenMessage.nextToken();
                            validationMessage = validationMessage + "\n \t" + message;
                        }
                    }

                    if(mandatoryQnrPresent || incompleteQnrPresent || newVersionQnrPresent){
                        CoeusOptionPane.showWarningDialog(validationMessage);
                        return;
                    }

                }
                // COEUSDEV-202: Error about mandatory questionnaire - Does not identify the questionnaire - End

                //calling EDI Validation method that returns true if passed
                if(submitForApprove.callEDIValidations()) {

                    Vector vecBrokenRules = getBrokenRulesData(true);
                    if(vecBrokenRules != null && vecBrokenRules.size()>0){
                        //Code commented and modified for Case#2785 - Routing Enhancements - starts
//                        ProposalValidationChecksForm valChecks = new ProposalValidationChecksForm(mdiForm,vecBrokenRules,proposalID );
//                        valChecks.setVisible(true);
                            edu.mit.coeus.routing.gui.RoutingValidationChecksForm valChecks =
                                    new edu.mit.coeus.routing.gui.RoutingValidationChecksForm(mdiForm,vecBrokenRules,3, proposalID );
                            valChecks.display();
                        //Code commented and modified for Case#2785 - Routing Enhancements - ends
                        //Confirm Message Window before Submitting for Approval if Validation rules failed
                            if(valChecks.isError() || !submitForApprove.confirmSubmitForApproval()){
                                return;
                            }
                    }else {
                        CoeusOptionPane.showInfoDialog(
                        coeusMessageResources.parseMessageKey("validationChecks_exceptionCode.1904"));
                    }

                    if((proposalDevelopmentFormBean.isS2sOppSelected() && validateGrantsGov() && submitForApprove.doSubmitApproveAction())
                        ||(!proposalDevelopmentFormBean.isS2sOppSelected()&&submitForApprove.doSubmitApproveAction())){
                        // Commented by chandra. bug fix 981. Send the bean from the submitApproveForm ,
                        // not the instance level bean. - start 5th July 2004
                        //Code commented and modified for Case#2785 - Routing Enhancements - starts
                        //ProposalRoutingForm proposalRoutingForm = new ProposalRoutingForm(mdiForm,proposalDevelopmentFormBean,GET_PROP_ROUTING_DATA_FOR_APPROVE,true);
//                        ProposalRoutingForm proposalRoutingForm = new ProposalRoutingForm(mdiForm,submitForApprove.getProposalDevelopmentFormBean(),GET_PROP_ROUTING_DATA_FOR_APPROVE,true);
                        //changed the signature of constructor
                        //RoutingForm proposalRoutingForm = new RoutingForm(submitForApprove.getProposalDevelopmentFormBean(), 3, proposalID, 0,  false);
                        RoutingForm proposalRoutingForm = new RoutingForm(
                                submitForApprove.getProposalDevelopmentFormBean(),
                                3, proposalID, 0, submitForApprove.getProposalDevelopmentFormBean().getOwnedBy(), false);
                        //Code commented and modified for Case#2785 - Routing Enhancements - ends
                        if(isHierarchy() && isParentProposal()){
                           proposalRoutingForm.setParentProposal(true);
                       }
                        // End 5th July 2004
                        //Added for the case# COEUSQA-1679- Modification for Final Document Indicator-start
                        //set the Hierarchy value in routingform, inorder to find, whether the proposal is in heirarch or not
                        proposalRoutingForm.setHierarchy(isHierarchy());
                        //Added for the case# COEUSQA-1679- Modification for Final Document Indicator-end
                        //COEUSQA-1433 - Allow Recall from Routing - Start
                        // Modified for COEUSQA-3816 : Lite - Proposal routing - Locking issues - Start
                        // Routing lock is done in RoutingApprovalForm
//                        if(proposalRoutingForm.isEnabled() && functionType == DISPLAY_MODE){
//                            boolean isProtocolLocked = lockProposalRecall();
//                            if(isProtocolLocked){
//                                proposalRoutingForm.display();


//                            }
//                        }
                        proposalRoutingForm.display();
                        // Modified for COEUSQA-3816 : Lite - Proposal routing - Locking issues - End
                        //COEUSQA-1433 - Allow Recall from Routing - End
                        if(proposalRoutingForm.isMapsNotFound()){
                            return;
                        }
                        proposalDevelopmentFormBean = (ProposalDevelopmentFormBean) proposalRoutingForm.getModuleBean();
                        observable.setFunctionType( functionType );
                        proposalDevelopmentFormBean.setStatusCode(2);
                        observable.notifyObservers( proposalDevelopmentFormBean );
                        // added to update all the tabs with the latest data which came from server
                        updateData();
                        // Update the
                        if(isHierarchy() && isParentProposal()){
                            updateChildStatus(proposalDevelopmentFormBean.getProposalNumber());
                        }
                        closeProposalDetailForm();
                        //specialReviewForm.clearSpecialReviewData();
                    }

                }

            }else
                tbdPnProposal.setSelectedIndex(3);
        }
    }
    /** Update the child proposal's creation status code, if the parent proposal
     *performed actions like Submit, Approve, Reject, PostSubmission
     */
    private boolean updateChildStatus(String proposalNumber) throws CoeusException{
         boolean success = false;
//        Vector data = new Vector();
        RequesterBean request = new RequesterBean();
        request.setFunctionType( UPDATE_CHILD_STATUS );
        request.setDataObject( proposalNumber );
        AppletServletCommunicator comm = new AppletServletCommunicator(
        connect, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if( response.isSuccessfulResponse() ){
            success = ((Boolean)response.getDataObject()).booleanValue();
        }else{
            throw new CoeusException(response.getMessage());
        }
        return success;
    }

    private void showRouting()  throws Exception {

        /** Called to update the proposalDevelopmentFormBean with changed value
         * in Start Date End Date and Activity Code
         */
        if(this.functionType != CoeusGuiConstants.DISPLAY_MODE ){
            saveProposalDetails( SAVE_RETAIN_LOCK );
        }
        proposalDetail.getFormData();

        Vector parameters = new Vector();
        parameters.add(proposalDetail);
        parameters.add(this);
        parameters.add(proposalDevelopmentFormBean);
        parameters.add(tbdPnProposal);
        parameters.add(functionType+"");
        parameters.add(proposalInvestigator);
        ShowRouting showRouting = new ShowRouting(mdiForm,parameters,true);
        //Commented for case #2064 start
        //saves the Proposal
       // if( functionType == TypeConstants.DISPLAY_MODE ){
            showRouting.showRoutingWindow();
      //  }
//        else{
//            if(showRouting.validateProposalEntry()) {
//                if(!(functionType == TypeConstants.DISPLAY_MODE) ){
//                    saveProposalDetails( SAVE_RETAIN_LOCK );
//                    ProposalRoutingForm proposalRoutingForm = new ProposalRoutingForm(
//                    mdiForm, proposalDevelopmentFormBean,
//                    GET_PROP_ROUTING_DATA, true);
//                    proposalRoutingForm.display();
//                }
//            }
//        }
        //Commented for case #2064 end
    }

    /**
     * Method For Displaying Approval Map on Menu Initialtion
     * It passes the Proposal Number unit Number functionType and the Beans as parameters
     * call <code>SelectApprovalMapsForm</code> to Display Approval Maps
     */
    private void showApprovalMap() {
        //Commented for Coeus 4.3 Routing enhancement -PT ID:2785 - start
//        Vector parameters = new Vector();
//        parameters.add(proposalID);
//        parameters.add(unitNumber);
//        parameters.add(functionType+"");
//        parameters.add(proposalDevelopmentFormBean);
//        parameters.add(proposalDetail);
//          SelectApprovalMapsForm selectApprovalMapsForm = new SelectApprovalMapsForm(parameters);
        //Commented for Coeus 4.3 Routing enhancement -PT ID:2785 - end

        //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - start
        HashMap hmParameter = new HashMap();
        hmParameter.put("MODULE_ITEM_KEY", proposalID);
        hmParameter.put("UNIT_NUMBER", proposalDevelopmentFormBean.getOwnedBy());
        hmParameter.put("FUNCTION_TYPE", functionType+"");
        hmParameter.put("MODULE_BEAN", proposalDevelopmentFormBean);
        edu.mit.coeus.routing.gui.SelectRoutingMapsForm selectRoutingMapsForm =
                new edu.mit.coeus.routing.gui.SelectRoutingMapsForm(hmParameter, 3);
        proposalDetail.setSaveRequired(selectRoutingMapsForm.isSaveRequired());
        //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - end
    }


    private void showPersonNotification() {
        HashMap hmParameter = new HashMap();
        String ProposalNum= proposalDevelopmentFormBean.getProposalNumber();
        hmParameter.put("PROP_NUMBER",proposalDevelopmentFormBean.getProposalNumber());
       edu.mit.coeus.propdev.gui.PersonNotificationForm  personNotificationForm=new
               edu.mit.coeus.propdev.gui.PersonNotificationForm(ProposalNum);
        //HashMap hmParameter = new HashMap();
        //hmParameter.put("MODULE_ITEM_KEY", proposalID);
       // hmParameter.put("UNIT_NUMBER", proposalDevelopmentFormBean.getOwnedBy());
       // hmParameter.put("FUNCTION_TYPE", functionType+"");
       // hmParameter.put("MODULE_BEAN", proposalDevelopmentFormBean);
       // edu.mit.coeus.routing.gui.SelectRoutingMapsForm selectRoutingMapsForm =
        //        new edu.mit.coeus.routing.gui.SelectRoutingMapsForm(hmParameter, 3);
    }

    // END=====Added by Ranjeev

//code added for displaying DisclosureStatusForm to show the proposal disclosure status- starts
 private void showdisclosurestatus() {
      HashMap hmParameter = new HashMap();
      int ModuleCode=3;
        String ProposalNum= proposalDevelopmentFormBean.getProposalNumber();
        hmParameter.put("PROP_NUMBER",proposalDevelopmentFormBean.getProposalNumber());
        edu.mit.coeus.propdev.gui.DisclosureStatusForm  disclosureStatusForm=new
               edu.mit.coeus.propdev.gui.DisclosureStatusForm(ProposalNum,ModuleCode);
         }
 //code added for displaying DisclosureStatusForm to show the proposal disclosure status -ends


    /** Method to display the proposal routing screen to perform approve action */
    //Commented and modified for Case#3775 - Manually selected map disappears
    //private void showProposalRouting(){
    private void showProposalRouting()throws Exception{
        //Code commented and modified for Case#2785 - Routing Enhancements - starts
//        ProposalRoutingForm proposalRoutingForm = new ProposalRoutingForm(
//        mdiForm, proposalDevelopmentFormBean,GET_PROP_ROUTING_DATA_FOR_APPROVE,true);
//        Changed the signature of constructor
//        edu.mit.coeus.routing.gui.RoutingForm proposalRoutingForm = new edu.mit.coeus.routing.gui.RoutingForm(proposalDevelopmentFormBean,
//                 3, proposalID, 0,  false);
       // COEUSQA-2400_CLONE_Proposal dev-routing window does not open when proposal is in Post Submission rejection status- Start
//        edu.mit.coeus.routing.gui.RoutingForm proposalRoutingForm =
//                new edu.mit.coeus.routing.gui.RoutingForm(proposalDevelopmentFormBean,
//                3, proposalID, 0, proposalDevelopmentFormBean.getOwnedBy(), false);
        edu.mit.coeus.routing.gui.RoutingForm proposalRoutingForm = null;
        if(proposalDevelopmentFormBean.getCreationStatusCode() == PROPOSAL_IN_POST_SUBMISSION_REJECTION_STATUS){
                proposalRoutingForm =
                new edu.mit.coeus.routing.gui.RoutingForm(proposalDevelopmentFormBean,
                3, proposalID, 0, proposalDevelopmentFormBean.getOwnedBy(), true);
        }else{
             proposalRoutingForm =
                new edu.mit.coeus.routing.gui.RoutingForm(proposalDevelopmentFormBean,
                3, proposalID, 0, proposalDevelopmentFormBean.getOwnedBy(), false);
        }
        //COEUSQA-2400_CLONE_Proposal dev-routing window does not open when proposal is in Post Submission rejection status-End
        //Code commented and modified for Case#2785 - Routing Enhancements - ends
        //Added for the case# COEUSQA-1679- Modification for Final Document Indicator-start
        //set the Hierarchy value in routingform, inorder to find, whether the proposal is in heirarch or not
        proposalRoutingForm.setHierarchy(isHierarchy());
        //Added for the case# COEUSQA-1679- Modification for Final Document Indicator-end
        proposalRoutingForm.setParentProposal(isParentProposal());
        proposalRoutingForm.registerObserver( this );
        if( hasSubmitToSponsorRt ){
            //COEUSQA-1433 - Allow Recall from Routing - Start

            // Modified for COEUSQA-3816 : Lite - Proposal routing - Locking issues - Start
            // Routing lock is done in RoutingApprovalForm
//            if(proposalRoutingForm.isEnabled() && functionType == DISPLAY_MODE){
//                boolean isProtocolLocked = lockProposalRecall();
//                if(isProtocolLocked){
//                    submitSponsor.setEnabled(proposalRoutingForm.display());


//                }
//            }
            submitSponsor.setEnabled(proposalRoutingForm.display());
            // Modified for COEUSQA-3816 : Lite - Proposal routing - Locking issues - End
            //COEUSQA-1433 - Allow Recall from Routing - End
        }else{
            //COEUSQA-1433 - Allow Recall from Routing - Start
            // Modified for COEUSQA-3816 : Lite - Proposal routing - Locking issues - Start
            // Routing lock is done in RoutingApprovalForm
//            if(proposalRoutingForm.isEnabled() && functionType == DISPLAY_MODE){
//                boolean isProtocolLocked = lockProposalRecall();
//                if(isProtocolLocked){
//                    proposalRoutingForm.display();


//                }
//            }
            proposalRoutingForm.display();
            // Modified for COEUSQA-3816 : Lite - Proposal routing - Locking issues - End            
            //COEUSQA-1433 - Allow Recall from Routing - End
        }
        //Added for Case#3775 - Manually selected map disappears - starts
        if(proposalRoutingForm.isCloseRequired()){
            proposalDevelopmentFormBean = (ProposalDevelopmentFormBean) proposalRoutingForm.getModuleBean();
            proposalDevelopmentFormBean.setCreationStatusCode(3);
            observable.notifyObservers(proposalDevelopmentFormBean);
            // Update the Status of the Children as the same as that of the Parent proposal
            if(isHierarchy() && isParentProposal()){
                updateChildStatus(proposalDevelopmentFormBean.getProposalNumber());
            }

            closeProposalDetailForm();
        }
        //Added for Case#3775 - Manually selected map disappears - ends
    }

    /** Displays the Proposal Notepad screen */
    private void showNotepad(){
        if ( isSaveRequired() ){
            int confirm = CoeusOptionPane.showQuestionDialog(
            coeusMessageResources.parseMessageKey("proposal_Notepad_exceptionCode.7117"),
            CoeusOptionPane.OPTION_YES_NO_CANCEL,
            CoeusOptionPane.DEFAULT_YES);
            switch(confirm){
                case JOptionPane.YES_OPTION:
                    try{
                        saveProposalDetails( SAVE_RETAIN_LOCK );
                    }catch (Exception exception){
                        exception.printStackTrace();
                    }
                    showNotepadWindow();
                    break;
                case JOptionPane.NO_OPTION:
                case JOptionPane.CANCEL_OPTION:
                    break;
            }
        }else{
            showNotepadWindow();
        }
    }

    //Modified for bug id 1856 step 1 : start
    /** Supporting method to display the Proposal Notepad screen */
    private void showNotepadWindow(){
        //Check if Notepad is already opened
        CoeusInternalFrame frame = mdiForm.getFrame(CoeusGuiConstants.NOTEPAD_FRAME_TITLE);
        if(frame == null){
            ProposalAwardHierarchyLinkBean linkBean = new ProposalAwardHierarchyLinkBean();
            linkBean.setDevelopmentProposalNumber(proposalID);
            linkBean.setBaseType(CoeusConstants.DEV_PROP);
            String unitNumber = proposalDevelopmentFormBean.getOwnedBy();
            ProposalNotepadForm proposalNotepadForm = new ProposalNotepadForm(linkBean, mdiForm);
            proposalNotepadForm.setProposalUnitNumber(unitNumber);
            proposalNotepadForm.postInitComponents();
            proposalNotepadForm.display();
        }else{
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
            "proposal_Notepad_exceptionCode.7116"));
        }
    }
    //bug id 1856 step 1: end

    // Added by Vyjayanthi
    // Budget Code - Start
    /** This method displays the select budget screen */
    private void showBudget(){
        if( proposalID != null ){
            /** If budget window is not open display the select budget screen
             * otherwise display message that the window is already open */
            /*
            if( !isBudgetWindowOpen() ) {
                SelectBudgetController selectBudgetController = new SelectBudgetController();
                selectBudgetController.setFunctionType(functionType);
                selectBudgetController.setProposalId(proposalID);
                selectBudgetController.setFormData(proposalDetail);
                selectBudgetController.display();
            }else{
                CoeusOptionPane.showInfoDialog(
                    coeusMessageResources.parseMessageKey(
                    "budget_common_exceptionCode.1005"));
            }
             */
            SelectBudgetController selectBudgetController = new SelectBudgetController();
            selectBudgetController.setFunctionType(functionType);
            // Added by chandra -start
            selectBudgetController.setParentProposal(isParentProposal());
            selectBudgetController.setHierarchy(isHierarchy());
            selectBudgetController.setProposalHierarchyBean(proposalHierarchyBean);
            selectBudgetController.setParentPropNo(getParentPropNo());
            // Added by chandra End
            selectBudgetController.setProposalId(proposalID);
            selectBudgetController.setUnitNumber(proposalDevelopmentFormBean.getOwnedBy());
            //Added by shiji for Right checking - step 2 : start
            selectBudgetController.setIsBudgetOpenedFromPropList(false);
            //Right checking - step 2 : end
            try{
                selectBudgetController.setFormData();
                selectBudgetController.display();
            }catch (CoeusClientException coeusClientException){
                if( !coeusClientException.getMessageKey().equals("") ){
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
                    coeusClientException.getMessage()));
                }
            }
        }
    }

    /*
     * Added for COEUSQA-4066
     * Method to display the UserAttachedS2SForm window
     */
    private void showUserAttachmentsS2SForms(String proposalNumber){
        UserAttachedS2SFormsController userAttachedS2SFormController = new UserAttachedS2SFormsController(proposalNumber, getFunctionType());
        userAttachedS2SFormController.display();
    }

    //Commented for COEUSQA-2342 : remove the old notification icon and menu item from proposal development window - Start
    //Notification Start
    // this method displays Notify Window.
//    private void showNotification(){
//        if(proposalID != null){
//            ProposalNotify proposalNotify = new ProposalNotify(proposalDevelopmentFormBean);
//            proposalNotify.showNotify();
//        }
//        /* else{
//        CoeusOptionPane.showInfoDialog(
//                    coeusMessageResources.parseMessageKey(
//                    "budget_common_exceptionCode.1005"));
//        }
//         */
//    }
    // Notification End
    //COEUSQA-2342 : End

    //Data Override start
    //this method displays Data Over ride window.
    private void showDataOverride() throws Exception{
        if(proposalID != null){
            int stCode = proposalDevelopmentFormBean==null?-1:proposalDevelopmentFormBean.getCreationStatusCode();
            ProposalDataOverride proposalDataOverride = new ProposalDataOverride(proposalID);
            proposalDataOverride.display(stCode!=2&&stCode!=4);

//            proposalDevelopmentFormBean = proposalDataOverride.getProposalDevelopmentFormBean();
            if(proposalDataOverride.getProposalDevelopmentFormBean()!=null){
                proposalDevelopmentFormBean = proposalDataOverride.getProposalDevelopmentFormBean();
                updateData();
            }

        }
        /* else{
            CoeusOptionPane.showInfoDialog(
                    coeusMessageResources.parseMessageKey(
                    "budget_common_exceptionCode.1005"));
        }
         */

    }
    //data override finished

    /** Method to check if the budget window for the selected proposal is open
     * @return budgetWindowOpen if budget window is open, false otherwise */
    /*
    private boolean isBudgetWindowOpen(){
        String key, propNo;
        CoeusVector cvData = new CoeusVector();
        boolean budgetWindowOpen = false;
        queryEngine = QueryEngine.getInstance();
        Enumeration enum = queryEngine.getKeys();
        BudgetInfoBean budgetInfoBean = new BudgetInfoBean();

        while(enum.hasMoreElements()){
            key = (String)enum.nextElement();
            try{
                cvData = queryEngine.getDetails(key, BudgetInfoBean.class);
                if( cvData != null || cvData.size() > 0){
                    budgetInfoBean = (BudgetInfoBean)cvData.get(0);
                    propNo = budgetInfoBean.getProposalNumber();
                    if( propNo.equals(proposalID) ){
                        budgetWindowOpen = true;
                        break;
                    }
                }
            }catch (CoeusException coeusException){
                coeusException.getMessage();
            }
        }
        return budgetWindowOpen;
    }
    // Budget Code - End
     */

    private void performAbstractDisplay(){
        //System.out.println("Now inside performAbstractDisplay");
        //System.out.println("ProposalID = " + proposalID);
        ProposalAbstractForm proposalAbstractForm =
        new ProposalAbstractForm(functionType,proposalID, mdiForm);
        //proposalAbstractForm.setProposalNumber(proposalID);
        String strSponsorCodeDesc = "";
        if(ProposalDetailAdminForm.SPONSOR_CODE != null && !ProposalDetailAdminForm.SPONSOR_CODE.equals("")) {
            strSponsorCodeDesc = ProposalDetailAdminForm.SPONSOR_CODE + " : " +ProposalDetailAdminForm.SPONSOR_DESCRIPTION;
        }
        proposalAbstractForm.setSponsorCodeName(strSponsorCodeDesc);
        proposalAbstractForm.showDialogForm();
    }

    /*Commented for the Coeus Enhancement case:#1823 start step:10
    private void showSpecialReview() throws Exception{
        boolean specialReviewIsSave = false;
        vecSpecialReviewData = new Vector();
        vecSpecialReviewData = proposalDevelopmentFormBean.getPropSpecialReviewFormBean();

        ProposalSpecialReviewFormBean proposalSpecialReviewFormBean;

        try {
            vecSplRevReplicateData = (Vector)ObjectCloner.deepCopy(vecSpecialReviewData);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }

        ProposalSpecialReviewForm proposalSpecialReviewForm = new ProposalSpecialReviewForm(proposalID, vecSpecialReviewData, functionType);
        proposalSpecialReviewForm.setApprovalTypes(this.propReviewApprovalTypes);
        proposalSpecialReviewForm.setSpecialReviewTypeCodes(this.propSpecialReviewCodes);
        proposalSpecialReviewForm.setVecReplicateData(vecSplRevReplicateData);
        proposalSpecialReviewForm.setValidateVector(this.propValidSpecialReviewList);
        //For the Coeus Enhancement case:#1799 start step:2
        proposalSpecialReviewForm.setCvParameters(this.cvParameters);
        //End Coeus Enhancement case:#1799  step:2
        proposalSpecialReviewForm.setFormData();
        proposalSpecialReviewForm.showProposalSpecialForm();

        if(proposalSpecialReviewForm.isSaveRequired()){
            if(saveRequired != true){
                saveRequired = proposalSpecialReviewForm.isSaveRequired();
            }
            vecSpecialReviewData = proposalSpecialReviewForm.getFormData();

            if(vecSpecialReviewData != null){
                int intSize = vecSpecialReviewData.size();
                SpecialReviewFormBean specialReviewFormBean;
                //ProposalSpecialReviewFormBean proposalSpecialReviewFormBean ;
                for(int intRow =0; intRow < intSize ; intRow++){

                    specialReviewFormBean = (SpecialReviewFormBean) vecSpecialReviewData.elementAt(intRow);

                    proposalSpecialReviewFormBean = new ProposalSpecialReviewFormBean(specialReviewFormBean);
                    proposalSpecialReviewFormBean.setProposalNumber(proposalID);
                    vecSpecialReviewData.setElementAt(proposalSpecialReviewFormBean,intRow);
                }
            }
            proposalDevelopmentFormBean.setPropSpecialReviewFormBean(vecSpecialReviewData);
        }
        else{
            vecSpecialReviewData = vecSplRevReplicateData;
            proposalDevelopmentFormBean.setPropSpecialReviewFormBean(vecSpecialReviewData);
        }
    }End Coeus Enhancement case:#1823 step:10*/

    private void showYesNoQuestionsForm(){
        proposalYesNoQuestionsForm =
        new ProposalYesNoQuestionsForm(proposalID, functionType);
        // Added for COEUSQA-2702 : YNQ blocker - Start
        //JIRA COEUSQA-2882 - START - 1
        if(getFunctionType() != DISPLAY_MODE){
        //JIRA COEUSQA-2882 - END - 1
        try {
            // Modified for COEUSQA-2881 : Last Update User and Timestamp changes - Start
//            saveRequired = true;
//            saveProposalDetails( SAVE_RETAIN_LOCK );
            if(functionType == TypeConstants.MODIFY_MODE){
                saveRequired = true;
                saveProposalDetails( SAVE_RETAIN_LOCK );
            }
            // Modified for COEUSQA-2881 : Last Update User and Timestamp changes - End
        }catch(Exception exp) {
            CoeusOptionPane.showInfoDialog(exp.getMessage());
            tbdPnProposal.setSelectedIndex(0);
            return ;
        }
        //JIRA COEUSQA-2882 - START - 2
        }
        //JIRA COEUSQA-2882 - END - 2
        // Added for COEUSQA-2702 : YNQ blocker - End
        String strSponsorCodeDesc = "";
        
        //Commeneted and added for COEUSQA-3390 : sponsor name is null in proposal roles screen - start  !ProposalDetailAdminForm.SPONSOR_DESCRIPTION.equals("")
        //if(ProposalDetailAdminForm.SPONSOR_CODE != null && !ProposalDetailAdminForm.SPONSOR_CODE.equals("")) {
        if(ProposalDetailAdminForm.SPONSOR_CODE != null && !CoeusGuiConstants.EMPTY_STRING.equals(ProposalDetailAdminForm.SPONSOR_DESCRIPTION)) {
            strSponsorCodeDesc = ProposalDetailAdminForm.SPONSOR_CODE + " : " +ProposalDetailAdminForm.SPONSOR_DESCRIPTION;
        }
        //Commeneted and added for COEUSQA-3390 : sponsor name is null in proposal roles screen - end
        
        proposalYesNoQuestionsForm.setSponsorName(strSponsorCodeDesc);
        //System.out.println("Proposal Detail Form vecPropYNQAnswerList is null ? " + vecPropYNQAnswerList);
        // Modified for COEUSQA-2702 : YNQ blocker - Start
//        proposalYesNoQuestionsForm.setVecAnswerList(vecPropYNQAnswerList);
//        proposalYesNoQuestionsForm.setVecQuestionList(vecPropYNQQuestionList);
//        proposalYesNoQuestionsForm.setExplanation(htPropYNQExplanation);
        proposalYesNoQuestionsForm.setVecAnswerList(proposalDevelopmentFormBean.getPropYNQAnswerList());
        proposalYesNoQuestionsForm.setVecQuestionList(proposalDevelopmentFormBean.getPropYNQuestionList());
        proposalYesNoQuestionsForm.setExplanation(proposalDevelopmentFormBean.getPropYNQExplanationList());
        // Modified for COEUSQA-2702 : YNQ blocker - End
        //Bug Fix 1565: Start
        proposalYesNoQuestionsForm.setMoreExplanation(proposalDevelopmentFormBean.getMoreExplanation());
        //Bug Fix 1565: End

        proposalYesNoQuestionsForm.showProposalQuestionForm();

        if(proposalYesNoQuestionsForm.isSaveRequired()){
            if(saveRequired != true){
                saveRequired = proposalYesNoQuestionsForm.isSaveRequired();
            }
        }

        //Bug Fix: 1665 Start
        if(proposalYesNoQuestionsForm.isInactiveQuesPresent()){
            saveRequired = true;
        }
        //Bug Fix: 1665 End

        vecPropYNQAnswerList = proposalYesNoQuestionsForm.getVecProposalQuestionTable();
        printYNQData(vecPropYNQAnswerList);
        proposalDevelopmentFormBean.setPropYNQAnswerList(vecPropYNQAnswerList);

    }

    private void printYNQData(Vector vecPropYNQAnswerList){
        if(vecPropYNQAnswerList != null){
            ProposalYNQBean proposalYNQBean = null;
            for(int index = 0; index < vecPropYNQAnswerList.size(); index++){
                proposalYNQBean = (ProposalYNQBean)vecPropYNQAnswerList.elementAt(index);
                if(proposalYNQBean != null){
                    //System.out.println("proposalYNQBean.getProposalNumber() "+proposalYNQBean.getProposalNumber());
                    //System.out.println("proposalYNQBean.getExplanation() "+proposalYNQBean.getExplanation());
                    //System.out.println("proposalYNQBean.getReviewDate() "+proposalYNQBean.getReviewDate());
                    //System.out.println("proposalYNQBean.getQuestionId() "+proposalYNQBean.getQuestionId());
                    //System.out.println("proposalYNQBean.getAnswer() "+proposalYNQBean.getAnswer());
                    //System.out.println("proposalYNQBean.getUpdateTimeStamp() "+proposalYNQBean.getUpdateTimeStamp());
                    //System.out.println("proposalYNQBean.getUpdateUser() "+proposalYNQBean.getUpdateUser());
                    //System.out.println("proposalYNQBean.getAcType() "+proposalYNQBean.getAcType());
                }
            }
        }
    }

    private void showUserRoles() throws Exception{
        Vector tempRoles = proposalDevelopmentFormBean.getUserRolesInfoBean();
        propUserRoles.clear();
        if( tempRoles != null ) {
            int size = tempRoles.size();
            for( int indx = 0; indx < size; indx++ ) {
                UserRolesInfoBean tempBean = new UserRolesInfoBean();
                UserRolesInfoBean existBean = (UserRolesInfoBean) tempRoles.elementAt(indx );
                tempBean.setHasChildren( existBean.hasChildren() );
                tempBean.setIsRole( existBean.isRole() );
                tempBean.setRoleBean( existBean.getRoleBean() );
                tempBean.setUserBean( existBean.getUserBean() );
                tempBean.setUsers( existBean.getUsers() );
                propUserRoles.addElement( tempBean );
            }
        }

        if ( functionType == DISPLAY_MODE ) {
            userRoles.removeAllElements();
            userRoles.addElement( propDefaultUsers );
            userRoles.addElement( propUserRoles );
        }else {

            userRoles.removeAllElements();
            if ( propDefaultUsers != null &&  propDefaultUsers.size() > 0 ) {
                // table data for unit number
                userRoles.addElement( propDefaultUsers );
            }else{
                // empty users data to specify that there are no users for the
                // given unit number
                userRoles.addElement( new Vector() );
            }

            if( tempUserRoles != null ) {
                // unsaved tree data
                userRoles.addElement( tempUserRoles );
            }else {
                // data which came from database
                userRoles.addElement(  new Vector(propUserRoles) );
            }
        }
        CoeusVector cvRoleRight=null;
        //Modified for case #1856 start
        if( hasModifyProposalRights() || userHasRight(MAINTAIN_PROPOSAL_ACCESS)){
            //Modified for case #1856 end
            // rolesForm = new ProposalUserRoleMaintainance(proposalID, userRoles, functionType);
            Hashtable returnData;
            CoeusVector cvNarrativeRights = null;
            CoeusVector cvNarrativeModules = null;
            Vector data = new Vector();
            data.addElement(proposalID);
            RequesterBean request = new RequesterBean();
            request.setDataObject("GET_ALL_PROPOSAL_ROLE_RIGHTS");
            request.setDataObjects(data);
            AppletServletCommunicator comm = new AppletServletCommunicator( CoeusGuiConstants.CONNECTION_URL + CoeusGuiConstants.FUNCTION_SERVLET, request );
            comm.send();
            ResponderBean res = comm.getResponse();
            if(res.isSuccessfulResponse()){
                //cvRoleRight = (CoeusVector)res.getDataObject();
                // Added by chandra to fix Narrative bug. - 03-sept-2004
                // Check the Narrative rights before opening proposal roles.
                returnData = (Hashtable)res.getDataObject();
                cvRoleRight = (CoeusVector)returnData.get( KeyConstants.ROLE_RIGHTS);
                cvNarrativeRights = (CoeusVector)returnData.get( KeyConstants.NARRATIVE_RIGHTS);
                cvNarrativeModules = (CoeusVector)returnData.get( KeyConstants.NARRATIVE_MODULE);
                proposalDevelopmentFormBean.setNarrativeUserRights(cvNarrativeRights);
                proposalDevelopmentFormBean.setNarrativeModules(cvNarrativeModules);
                // end chandra - 03-sept-2004

            }else{
                return;
            }
            //Added/Modified for case#3230 - Display of Proposal Role Form based on rights - start
            char proposalRoleFormFunctionType = functionType;
            if(functionType != CoeusGuiConstants.DISPLAY_MODE){
                if(canModifyProposalRole()){
                    proposalRoleFormFunctionType = CoeusGuiConstants.MODIFY_MODE;
                }else{
                    proposalRoleFormFunctionType = CoeusGuiConstants.DISPLAY_MODE;
                }
            }
            if(narrativeUserRights!=null){
                rolesForm = new ProposalUserRoleMaintainance(proposalID, userRoles, proposalRoleFormFunctionType, narrativeUserRights, proposalDevelopmentFormBean.getNarrativeModules(),cvRoleRight);
            }else{
                rolesForm = new ProposalUserRoleMaintainance(proposalID, userRoles, proposalRoleFormFunctionType, proposalDevelopmentFormBean.getNarrativeUserRights(), proposalDevelopmentFormBean.getNarrativeModules(),cvRoleRight);
            }
            //Added/Modified for case#3230 - Display of Proposal Role Form based on rights - end
        }else{
            rolesForm = new ProposalUserRoleMaintainance(proposalID, userRoles,
            CoeusGuiConstants.DISPLAY_MODE, proposalDevelopmentFormBean.getNarrativeUserRights(), proposalDevelopmentFormBean.getNarrativeModules(),cvRoleRight);
        }

        rolesForm.setAdminRoleId( CoeusGuiConstants.PROPOSAL_APPROVER_ID );
        rolesForm.setMandatoryRoleId( CoeusGuiConstants.PROPOSAL_AGGREGATOR_ID );
        rolesForm.setShowSponsor( true );
        rolesForm.setModuleLabelName( "Proposal Number:" );

        UserRoleNodeRenderer nodeRenderer = new UserRoleNodeRenderer();
        /* ---Commented below code and added same without getClassLoader()---*/
        nodeRenderer.setActiveRoleIcon( new ImageIcon( getClass().getClassLoader().getResource(
        CoeusGuiConstants.PROP_ACTIVE_ROLE_ICON ) ) );
        nodeRenderer.setInactiveRoleIcon( new ImageIcon( getClass().getClassLoader().getResource(
        CoeusGuiConstants.PROP_INACTIVE_ROLE_ICON ) ) );

        //        nodeRenderer.setActiveRoleIcon( new ImageIcon( getClass().getResource(
        //            CoeusGuiConstants.PROP_ACTIVE_ROLE_ICON ) ) );
        //        nodeRenderer.setInactiveRoleIcon( new ImageIcon( getClass().getResource(
        //            CoeusGuiConstants.PROP_INACTIVE_ROLE_ICON ) ) );

        if( ProposalDetailAdminForm.SPONSOR_CODE != null ){
            rolesForm.setSponsorName( ProposalDetailAdminForm.SPONSOR_CODE +" : "+
            ProposalDetailAdminForm.SPONSOR_DESCRIPTION );
        }
        dlgRoles = new CoeusDlgWindow(mdiForm, "Proposal Roles", true);

        dlgRoles.getContentPane().add( rolesForm.showUserRolesMaintenance( dlgRoles ) );
        dlgRoles.pack();
        rolesForm.getRolesTree().setCellRenderer( nodeRenderer );

        // displaying the window in center of the screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        //dlgGenerate.setSize(new Dimension(500,200));
        Dimension dlgSize = dlgRoles.getSize();
        dlgRoles.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        dlgRoles.setResizable(false);
        dlgRoles.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dlgRoles.addKeyListener(new KeyAdapter(){
            public void keyReleased(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
                    if(!CoeusOptionPane.isPropagating()){
                        validateData();
                    }else{
                        CoeusOptionPane.setPropagating( false );
                    }

                }
            }
        });
        dlgRoles.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent we){
                validateData();
            }
        });

        dlgRoles.show();
        if( functionType != DISPLAY_MODE ) {
            if ( rolesForm.isSaveRequired() ) {
                //CoeusVector cvTempData;
                //CoeusVector cvFilteredData;
                //System.out.println("before updating tempUserRoles existingUserRoles:");
                getUserRoleInfo(proposalDevelopmentFormBean.getUserRolesInfoBean());
                tempUserRoles = rolesForm.getUserRolesData();
                //System.out.println("after updating tempUserRoles existingUserRoles:");
                getUserRoleInfo(proposalDevelopmentFormBean.getUserRolesInfoBean());
                //Narrative User Rights
                narrativeUserRights = rolesForm.getNarrativeUsers();
                if(narrativeUserRights!=null){
                    narrativeUserRights = narrativeUserRights.filter(new NotEquals("acType", null));
                }
            }else{
                if( tempUserRoles == null ){
                    tempUserRoles = ( Vector ) userRoles.elementAt( 1 );
                }else{
                    rolesForm.setSaveRequired(true);
                }
            }
        }
    }


    private boolean userHasRight(String right ) {
        if( proposalRoleRights != null && proposalRoleRights.size() > 0 ) {
            int size = proposalRoleRights.size();
            for( int indx = 0; indx < size ; indx++ ) {
                RoleRightInfoBean rightInfoBean =
                (RoleRightInfoBean)proposalRoleRights.get(indx);
                if( right.equals(rightInfoBean.getRightId())){
                    return true;
                }
            }
        }
        return false;
    }
    private void validateData(){
        if ( functionType != DISPLAY_MODE &&  rolesForm.isDataChanged()  ) {
            String msg = coeusMessageResources.parseMessageKey(
            "saveConfirmCode.1002");

            int confirm = CoeusOptionPane.showQuestionDialog(msg,
            CoeusOptionPane.OPTION_YES_NO_CANCEL,
            CoeusOptionPane.DEFAULT_YES);
            switch(confirm){
                case ( JOptionPane.NO_OPTION ) :
                    rolesForm.setSaveRequired( false );
                    dlgRoles.dispose();
                    break;
                case ( JOptionPane.YES_OPTION ) :
                    rolesForm.setSaveRequired( true );
                    dlgRoles.dispose();
                    break;
                case ( JOptionPane.CANCEL_OPTION ) :
                    dlgRoles.setVisible( true );
                    //return;
                    break;
            }

        }else{
            dlgRoles.dispose();
        }
    }

    private  Hashtable getUserRoleInfo( Vector userRoleDetails ) {
        int rolesSize = userRoleDetails.size();
        Hashtable extractedUsers = new Hashtable();
        ProposalRolesFormBean userRole;
        UserRolesInfoBean bean,childBean;
        RoleInfoBean roleBean;
        UserInfoBean userBean;
        Vector children;
        int childCount;
        for(int index = 0 ; index < rolesSize ; index++ ) {
            bean = (UserRolesInfoBean) userRoleDetails.elementAt( index );
            if( bean.isRole() ) {
                roleBean = bean.getRoleBean();
                if( bean.hasChildren() ){
                    children = bean.getUsers();
                    childCount = children.size();
                    for(int childIndex = 0; childIndex < childCount ; childIndex++ ) {
                        childBean = (UserRolesInfoBean) children.elementAt(childIndex);
                        userBean = childBean.getUserBean();
                        userRole = new ProposalRolesFormBean();
                        userRole.setProposalNumber(proposalID);
                        userRole.setUserId(userBean.getUserId());
                        userRole.setRoleId(roleBean.getRoleId());
                        String key = userRole.getRoleId() + userRole.getUserId();
                        extractedUsers.put( key , userRole );
                        //System.out.println("key:"+key+": userid:"+userRole.getUserId());
                    }
                }
            }
        }
        return extractedUsers;
    }

    private Vector checkModifications( Hashtable htExistingUsers, Hashtable htTempUsers ) {
        Set newSet = htTempUsers.keySet();
        if(!newSet.isEmpty()){
            Iterator iterator = newSet.iterator();
            while(iterator.hasNext()){
                String key = (String)iterator.next();
                if( !htExistingUsers.containsKey( key ) ){
                    ProposalRolesFormBean newRoleBean
                    = ( ProposalRolesFormBean )htTempUsers.get( key );
                    newRoleBean.setAcType( "I" );
                    //System.out.println("inserted record:"+newRoleBean.getRoleId()+":"+newRoleBean.getUserId());
                    htExistingUsers.put(key, newRoleBean );
                }
            }
        }
        Set existingSet = htExistingUsers.keySet();
        if(!existingSet.isEmpty()){
            Iterator iterator = existingSet.iterator();
            while(iterator.hasNext()){
                String key = (String)iterator.next();
                if( !htTempUsers.containsKey( key ) ){
                    ProposalRolesFormBean newRoleBean
                    = ( ProposalRolesFormBean )htExistingUsers.get( key );
                    newRoleBean.setAcType( "D" );
                    //System.out.println("deleted record:"+newRoleBean.getRoleId()+":"+newRoleBean.getUserId());
                    htExistingUsers.put(key, newRoleBean );
                }
            }
        }

        return new Vector( htExistingUsers.values() );
    }


    //Modified for bug id 1856 step 2 : start
    /**
     * This method display the Proposal Personnels
     */
    private void showProposalPersonnel() throws Exception{
        boolean isPropPerson = false;
        if( proposalID != null ){
            if( propPersons != null && propPersons.size() > 0 ) {

                // try to get the requested frame which is already opened
                CoeusInternalFrame frame = mdiForm.getFrame(
                CoeusGuiConstants.PROP_PERSON_BIOGRAPHY_FRAME_TITLE,proposalID);
                if(frame == null){
                    String title = "Proposal Personnel";
                    proposalPersonnelForm = new ProposalPersonnelForm(title, mdiForm);
                    if(propsoalStatusCode == 2 || propsoalStatusCode == 4 ){
                        if(isSpecialReviewRight){
                            isPropPerson = true;
                        }
                    }
                    //Code added for Proposal Hierarchy case#3183
                    proposalPersonnelForm.setHierarchy(isHierarchy());
                    proposalPersonnelForm.setParentProposal(isParentProposal());
                    proposalPersonnelForm.setFunctionType(functionType);
                    proposalPersonnelForm.setAlterProposalRight(isPropPerson);
                    proposalPersonnelForm.setProposalStatusCode(propsoalStatusCode);
                    proposalPersonnelForm.setProposalId(proposalID);
                    //JIRA COEUSDEV-548 - START
                    proposalPersonnelForm.setUnitNumber(unitNumber);
                    //JIRA COEUSDEV-548 - END
                    proposalPersonnelForm.showWindow();
                }else{
                    frame.setSelected(true);
                    frame.setVisible(true);
                }

            }else{
                throw new Exception(coeusMessageResources.parseMessageKey("proposal_DtlForm_exceptionCode.7113"));
            }
        }
    }
   // bug id 1856 step 2 : end

    private void showNarrative(char fType) throws Exception{
        if( proposalID != null ){
            saveProposalDetails( SAVE_RETAIN_LOCK );

    		// JM 4-10-2015 check if narratives are locked
            if (proposalDevelopmentFormBean.getCreationStatusCode() == APPROVAL_IN_PROGRESS) {
	    		CustomFunctions fn = new CustomFunctions();
	    		LockingBean lockingBean = (LockingBean) fn.getNarrativeLock(proposalID);
	    		String lockingUser = "";
	    		
	    		boolean locked = false;
	    		if (lockingBean.getUpdateUser() != null) {
	    			locked = true;
	    			lockingUser = lockingBean.getUpdateUser();
	    		}
	    		
	    		if (locked) {
	    			if (lockingBean.getUpdateUserName() != null) {
	    				lockingUser = lockingBean.getUpdateUserName();
	    			}
	    			String lockMessage = "Narratives are currently in use and locked by " + lockingUser + "  ";
	    			CoeusOptionPane.showErrorDialog(lockMessage);
	    		}
            }
    		// JM END

            try{
                mdiForm.checkDuplicate(CoeusGuiConstants.PROPOSAL_NARRATIVE_FRAME_TITLE,
                proposalID, fType );
            }catch(Exception e){
                // Exception occured.  Record may be already opened in requested mode
                //   or if the requested mode is edit mode and application is already
                //   editing any other record.
                if(e.getMessage().length() > 0 ) {
                    CoeusOptionPane.showInfoDialog(e.getMessage());
                }
                // try to get the requested frame which is already opened
                CoeusInternalFrame frame = mdiForm.getFrame(
                CoeusGuiConstants.PROPOSAL_NARRATIVE_FRAME_TITLE,proposalID);
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

            try{
               narrativeForm = new ProposalNarrativeForm( proposalID, fType, mdiForm );
               narrativeForm.setHierarchy(isHierarchy());
               narrativeForm.setParent(isParentProposal());

                //Added for the Coeus Enhancement case:1767 step:2
                narrativeForm.setProposalStatusCode(propsoalStatusCode);
                //End Coeus Enhancement case:1767 step:2
                //Added by shiji for Right checking - step 3 : start
                narrativeForm.setIsNarrativeOpenedFromPropList(false);
                narrativeForm.setUnitNumber(proposalDevelopmentFormBean.getOwnedBy());
                //Right checking - step 3 : end
                narrativeForm.showNarrativeForm();
            }catch ( Exception e) {
                String msg = e.getMessage();
                if( msg != null && msg.length() > 0 && !( msg.equals(coeusMessageResources.parseMessageKey(
                "protoDetFrm_exceptionCode.1130")) )){
                    CoeusOptionPane.showInfoDialog(e.getMessage());
                }
            }
        }
    }    /**
     * Implementation of Previous / Next Tool bar Button Functionality.
     *
     * Updated Subramanya dated April 23 2003.
     */
    private void closeProposalDetailForm() throws Exception{
        // Added by Vyjayanthi
        // Budget Code - Start
        /* To check if the budget window for the selected proposal is open */
        //Commented to allow closing of proposal detail form even if budget is open
        /*
        if( isBudgetWindowOpen() ) {
            CoeusOptionPane.showInfoDialog(
                coeusMessageResources.parseMessageKey(
                "budget_common_exceptionCode.1008"));
             throw new PropertyVetoException(
                        coeusMessageResources.parseMessageKey(
                        "protoDetFrm_exceptionCode.1130"),null);
        }
        // Budget Code - End
         */
//        CoeusInternalFrame frame;
   //     if( checkChildWindows() ){
            if ( ! isSaveRequired() ) {
                mdiForm.removeFrame(CoeusGuiConstants.PROPOSAL_DETAILS_FRAME_TITLE,proposalID);
                releaseUpdateLock();
                //                reduceDisplaySheetCount();
                //                this.dispose();
                this.doDefaultCloseAction();
                //                frame = mdiForm.getFrame(
                //                CoeusGuiConstants.PROPOSAL_BASE_FRAME_TITLE);
                //                if(frame != null){
                //                    frame.setSelected(true);
                //                    frame.setVisible(true);
                //                }

                //Proposal Hierarchy Enhancment Start
                if(hierarchy){
                    hierarchyController.cleanUp();
                    hierarchyMap = null;
                    hierarchyController = null;
                    scrPnHierarchy = null;
                }
                //Proposal Hierarchy Enhancment End
            }else{
                String msg = coeusMessageResources.parseMessageKey(
                "saveConfirmCode.1002");
                int confirm = CoeusOptionPane.showQuestionDialog(msg,
                CoeusOptionPane.OPTION_YES_NO_CANCEL,
                CoeusOptionPane.DEFAULT_YES);

                switch(confirm){
                    case(JOptionPane.YES_OPTION):
                        saveProposalDetails( SAVE_RELEASE_LOCK );
                        mdiForm.removeFrame(CoeusGuiConstants.PROPOSAL_DETAILS_FRAME_TITLE,proposalID);
                              //  specialReviewForm.clearSpecialReviewData();
                              //  proposalSpecialReview.setVecData(null);
                       // proposalDevelopmentFormBean.setPropSpecialReviewFormBean(null);
                        this.doDefaultCloseAction();

                        //Proposal Hierarchy Enhancment Start
                        if(hierarchy){
                            hierarchyController.cleanUp();
                            hierarchyMap = null;
                            hierarchyController = null;
                            scrPnHierarchy = null;
                        }
                        //Proposal Hierarchy Enhancment End

                        break;
                    case(JOptionPane.NO_OPTION):
                        mdiForm.removeFrame(CoeusGuiConstants.PROPOSAL_DETAILS_FRAME_TITLE,proposalID);
                        saveRequired = false;
                        //set a flag to close the internal frame without checking for save required
                        // as user wishes to discard the changes.
                        discardData = true;
                        proposalDetail.setSaveRequired( false );
                        proposalOrganization.setSaveRequired( false );
                        proposalMailingInfo.setSaveRequired(  false );
                        proposalInvestigator.setSaveRequired( false );
                        proposalKeyPersonController.setSaveRequired( false );
                        proposalScienceCode.setSaveRequired( false );
                        proposalSpecialReview.setSaveRequired(false);
                        if ( proposalOther != null ) {
                            proposalOther.setSaveRequired( false );
                        }
                        if( rolesForm != null ) {
                            rolesForm.setSaveRequired(false);
                        }
                        releaseUpdateLock();

                        //Proposal Hierarchy Enhancment Start
                        if(hierarchy){
                            hierarchyController.cleanUp();
                            hierarchyMap = null;
                            hierarchyController = null;
                            scrPnHierarchy = null;
                        }
                        //Proposal Hierarchy Enhancment End

                        this.doDefaultCloseAction();
                        break;

                    case ( JOptionPane.CANCEL_OPTION ):
                        //Case :#3149 ? Tabbing between fields does not work on others tabs - Start

                        if(tbdPnProposal.getSelectedIndex() == 7 && proposalOther != null && getFunctionType() != DISPLAY_MODE){
                            proposalOther.setSaveAction(true);
                            boolean[] lookUpAvailable = proposalOther.getLookUpAvailable();
                            customTable = proposalOther.getTable();
                            row = proposalOther.getRow();
                            column = proposalOther.getColumn();
                            count = 0;
                            if(lookUpAvailable[row]){

                                customTable.editCellAt(row,column);
                                customTable.setRowSelectionInterval(row,row);
                                customTable.setColumnSelectionInterval(column,column);

                            }else{
                                customTable.editCellAt(row,column);
                                customTable.setRowSelectionInterval(row,row);
                                customTable.setColumnSelectionInterval(column,column);
                            }
                            customTable.setEnabled(true);
                        }  //Case :#3149 - End
                    case ( JOptionPane.CLOSED_OPTION ):
                        throw new PropertyVetoException(
                        coeusMessageResources.parseMessageKey(
                        "protoDetFrm_exceptionCode.1130"),null);
                }
            }
            //editting for new special review data

        //}
    }

    private void releaseUpdateLock(){
        try{
            //connect to server and release the lock
            String rowId = null;
            //Case #1769  - start
            char funType = MODIFY_MODE;
            if(functionType==DISPLAY_MODE){
                if(checkLock){
                    funType = MODIFY_MODE;
                    checkLock = false;
                }else{
                    funType = DISPLAY_MODE;
                }
            }// Case #1769 - End
            //if ( functionType == MODIFY_MODE ) {
            if ( funType == MODIFY_MODE ) {
                rowId = proposalID;
                RequesterBean requester = new RequesterBean();
                requester.setDataObject(rowId);
                requester.setFunctionType( ROW_UNLOCK_MODE );
                String connectTo =CoeusGuiConstants.CONNECTION_URL +
                CoeusGuiConstants.PROPOSAL_SERVLET;
                AppletServletCommunicator comm = new AppletServletCommunicator(connectTo,requester);
                comm.send();
                ResponderBean res = comm.getResponse();
                if (res != null && !res.isSuccessfulResponse()){
                    CoeusOptionPane.showErrorDialog(res.getMessage());
                    return;
                }
            }
        }catch(Exception e){
            CoeusOptionPane.showErrorDialog(e.getMessage());
        }
    }


    /**
     * This method is used to refreshWindowFrame
     */
    private void refreshProposalWindow( String oldProposalID )throws Exception{

        /*mdiForm.removeFrame(CoeusGuiConstants.PROPOSAL_DETAILS_FRAME_TITLE,
                                                                 oldProposalID);
        mdiForm.putFrame( CoeusGuiConstants.PROPOSAL_DETAILS_FRAME_TITLE,
                          this.proposalID, this.functionType, this );
        mdiForm.getDeskTopPane().add(this);
        setVisible(true);
        setSelected(true);

        //update to handle new window menu item to the existing Window Menu.
        CoeusWindowMenu windowMenu = desktop.getWindowMenu();
        if( windowMenu != null ){
            windowMenu = windowMenu.removeMenuItem( this.getName() );
            windowMenu = windowMenu.addNewMenuItem( this.getName(), this );
            windowMenu.updateCheckBoxMenuItemState( this.getName(), true );
        }
        desktop.refreshMenu(menus);
        desktop.refreshWindowMenu(windowMenu);

        if( windowMenu != null ){
        }


         */
    }

    /**
     * This method is used to perform next proposal record view action.
     */
    private void performNextPreviousProposalAction( boolean isNext ){

        int selectedProposalRow = 0;
        String oldProposalID = this.proposalID;
        boolean extreme = false;
        try{
           // if( checkChildWindows() ){
                selectedProposalRow = tblProposal.getSelectedRow();
                if( isNext ){
                    if ( selectedProposalRow < tblProposal.getRowCount()-1 ){
                        ++selectedProposalRow;
                    }else{
                        extreme = true;
                    }
                }else{
                    if ( selectedProposalRow > 0 ){
                        --selectedProposalRow;
                    }else{
                        extreme = true;
                    }
                }
                if( extreme ){
                    CoeusOptionPane.showInfoDialog(
                    coeusMessageResources.parseMessageKey("proposal_Navigation_exceptionCode.6404"));
                    return;
                }
                try{
                    String newProposalId = getSelectedProposalNumber(selectedProposalRow,"PROPOSAL_NUMBER");
//                    String newProposalId = tblProposal.getValueAt(selectedProposalRow,0).toString().trim();
                    CoeusInternalFrame frame = mdiForm.getFrame(CoeusGuiConstants.PROPOSAL_DETAILS_FRAME_TITLE,newProposalId);
                    if( frame != null ){
                        tblProposal.addRowSelectionInterval( selectedProposalRow, selectedProposalRow );
                        frame.setSelected(true);
                        frame.setVisible(true);
                        return;
                    }
                    if( isNext ){
                        btnPrevProposal.setEnabled(true);
                        if( selectedProposalRow  == tblProposal.getRowCount()-1 ){
                            btnNextProposal.setEnabled(false);
                        }
                    }else{
                        btnNextProposal.setEnabled(true);
                        if( tblProposal.getSelectedRow() == 0 ){
                            btnPrevProposal.setEnabled(false);
                        }
                    }
                    tblProposal.addRowSelectionInterval( selectedProposalRow, selectedProposalRow );
                    mdiForm.removeFrame(CoeusGuiConstants.PROPOSAL_DETAILS_FRAME_TITLE,oldProposalID);
                    edu.mit.coeus.gui.menu.CoeusWindowMenu windowMenu = mdiForm.getWindowMenu();
                    if( windowMenu != null ){
                        windowMenu = windowMenu.removeMenuItem( this.getTitle() );
                    }
                    this.proposalID = getSelectedProposalNumber(selectedProposalRow, "PROPOSAL_NUMBER");
//                    this.proposalID = tblProposal.getValueAt(selectedProposalRow,0).toString().trim();
                    refreshDialogForm(proposalID);
                    mdiForm.refreshWindowMenu(windowMenu);
                    // Added by chandra .
                    // Bug fixing Id #987 - start 5th July 2004
                    enableDisableMenuItems();
                    // Bug fixing Id #987 - End 5th July 2004

                    refreshProposalWindow(oldProposalID);
                }catch( Exception ex) {
                    CoeusOptionPane.showInfoDialog( ex.getMessage() );
                }
          //  }
        }catch( Exception showErr ){
            //showErr.printStackTrace();
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
            "proposal_DtlForm_exceptionCode.7112"));
        }
    }



    /** This method is used to create the tabbedpane along with the components
     * which are used in <CODE>ProposalDetailsForm</CODE>.
     *
     */
    public JTabbedPane createForm() throws Exception{
        if(functionType==DISPLAY_MODE ){
            isSpecialReviewRight= getSpecialReviewRight(proposalID);//3587
        }
        setValues(getProposalDetails( proposalID ));
        tbdPnProposal = new CoeusTabbedPane();


        //Do Validations before opening Proposal in Modify mode
        if (functionType == MODIFY_MODE) {
            validateModifyProposal();
        }
        // create NameAddress tab
        FlowLayout layout = new FlowLayout(FlowLayout.LEFT);
        JPanel pnlDetail = new JPanel(layout);

        proposalDetail = new ProposalDetailAdminForm(
        functionType, proposalDevelopmentFormBean);
        //Set the ComboValues
        proposalDetail.setProposalTypes(proposalTypes);
        proposalDetail.setProposalStatus(proposalStatus);
        proposalDetail.setNSFCodes(proposalNSFCodes);
        proposalDetail.setActivityCodes(proposalActivityTypes);
        // Added for Case 2162  - adding Award Type - Start
        proposalDetail.setAwardTypes(vecAwardTypes);
        // Added for Case 2162  - adding Award Type - End
        proposalDetail.setNoticeOfOpportunities(proposalNoticeOpp);
        pnlDetail.add(proposalDetail.showProposalDetailAdminForm(mdiForm));
        JScrollPane jscrPn = new JScrollPane();
        jscrPn.setViewportView(pnlDetail);

        JPanel pnlOrg = new JPanel(layout);
        if( proposalDevelopmentFormBean != null ){
            proposalID = proposalDevelopmentFormBean.getProposalNumber();
            //need to be implemented to pull the data from all 7 tabs
            //investigator, mailinginfo, other, organization, key person etc.
            if ( functionType != ADD_MODE ) {
                //Added for the Coeus Enhancement case:#1823 start step:11
                specialReviewData = proposalDevelopmentFormBean.getPropSpecialReviewFormBean();
                //End Coeus Enhancement case:#1823 step:11
                //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - Start
                //keyStudyPersonnel = proposalDevelopmentFormBean.getKeyStudyPersonnel();
                allKeyStudyPersonnel = keyStudyPersonnel = proposalDevelopmentFormBean.getKeyStudyPersonnel();
                //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - End
                scienceCodeData = proposalDevelopmentFormBean.getScienceCode();
                investigators = proposalDevelopmentFormBean.getInvestigators();
                vecPropYNQAnswerList = proposalDevelopmentFormBean.getPropYNQAnswerList();

            }
            vecPropYNQQuestionList = proposalDevelopmentFormBean.getPropYNQuestionList();
            htPropYNQExplanation = proposalDevelopmentFormBean.getPropYNQExplanationList();
            otherData = proposalDevelopmentFormBean.getOthers();
            investigatorQusestion =
            proposalDevelopmentFormBean.getInvestigatorQuestions();
            qstExplanation = proposalDevelopmentFormBean.getMoreExplanation();
        }

        proposalOrganization = new ProposalOrganizationAdminForm(
        functionType, proposalDevelopmentFormBean);
        //Commented for case 2406 - Organization and Location - start
//        proposalOrganization.setOrgAddressInfo(
//        proposalDevelopmentFormBean.getOrganizationAddressFormBean());
//        proposalOrganization.setRolodexDetails(
//        proposalDevelopmentFormBean.getRolodexDetailsBean());
//        proposalOrganization.setPerformingOrganizationInfo(
//        proposalDevelopmentFormBean.getPerOrganizationAddressFormBean());
        //Commented for case 2406 - Organization and Location - end
        //Added for case 2406 - Organization and Location - start
        proposalOrganization.setLocationTypes(vecLocationTypes);
        //Added for case 2406 - Organization and Location - end

        pnlOrg.add( proposalOrganization.showOrganizationAdminForm(mdiForm) );
        JScrollPane jscrPnOrg = new JScrollPane();
        jscrPnOrg.setViewportView( pnlOrg );

        JPanel pnlMInfo = new JPanel(layout);
        proposalMailingInfo = new ProposalMailingInfoAdminForm(
        functionType, proposalDevelopmentFormBean);

        //Modified for Case#2402- use a parameter to set the length of the account number throughout app - Start
        //To get the paramater value of ACCOUNT_NUMBER_LIMIT in ProposalMailingInfoAdminForm
        //for setting size to mail account number text box.
//        pnlMInfo.add( proposalMailingInfo.showMailingInfoAdminForm(mdiForm) );
        pnlMInfo.add( proposalMailingInfo.showMailingInfoAdminForm(mdiForm, cvParameters) );
        //Case#2402 - End
        JScrollPane jscrPnMInfo = new JScrollPane();
        jscrPnMInfo.setViewportView( pnlMInfo );

        JPanel pnlInv = new JPanel(layout);

        proposalInvestigator = new ProposalInvestigatorForm( functionType,
        investigators );
        // Provide Certify Validate Vector to Investigator Form
        proposalInvestigator.setRoleRights( proposalRoleRights );
        proposalInvestigator.setProposalId( proposalID );
        proposalInvestigator.setDetailForm( this );
        proposalInvestigator.setParent(isParentProposal());

        proposalInvestigator.setProposalUnitNumber( proposalDevelopmentFormBean.getOwnedBy() );
        proposalInvestigator.setProposalUnitName( proposalDevelopmentFormBean.getOwnedByDesc() );
        proposalInvestigator.setInvestigatorQuestions( investigatorQusestion );
        proposalInvestigator.setMoreExplanations( qstExplanation );
        proposalInvestigator.setSponsorCode(proposalDevelopmentFormBean.getSponsorCode());
        proposalInvestigator.setSponsorName( proposalDevelopmentFormBean.getSponsorName() );
        //Modified by tarique for checking valiation for investigator for proposal hierarchy
        //pnlInv.add( proposalInvestigator.showProposalInvestigatorsForm( mdiForm) );
       // gnprh Commneted for Proposal Hierarchy start
        if(hierarchyMap!= null){
            boolean inHierarchy = ((Boolean)hierarchyMap.get("IN_HIERARCHY")).booleanValue();
            if(inHierarchy){
                pnlInv.add(proposalInvestigator.showPropHieInvestigatorForm(mdiForm , inHierarchy));
            }else{
                pnlInv.add( proposalInvestigator.showProposalInvestigatorsForm( mdiForm) );
            }
        }else{
            pnlInv.add( proposalInvestigator.showProposalInvestigatorsForm( mdiForm) );
        }//* End
	//pnlInv.add( proposalInvestigator.showProposalInvestigatorsForm( mdiForm) );
        //end modified by tarique
        JScrollPane jscrPnInv = new JScrollPane();
        jscrPnInv.setViewportView( pnlInv );

       // JPanel pnlKeyPerson = new JPanel(layout);
        //new code
         JPanel pnlKeyPerson = new JPanel(new FlowLayout(FlowLayout.LEFT));
        //ppc certify flag change starts         
         proposalDevelopmentFormBean.setKeyPersonCertifyParam(getParameterValueForPPC());         

         proposalKeyPersonController=new ProposalKeyPersonController(proposalID,keyStudyPersonnel,functionType,
                 proposalDevelopmentFormBean.isKeyPersonCertifyParam());
        //ppc certify flag change ends
        //proposalKeyPersonController.setFormData();
        proposalKeyPersonController.setRefreshRequired(true);
        proposalKeyPersonController.refresh();

                pnlKeyPerson.add(proposalKeyPersonController.getControlledUI());
               JScrollPane jscrPnKeyPerson = new JScrollPane();
               jscrPnKeyPerson.setViewportView(pnlKeyPerson);
             // jscrPnKeyPerson.setViewportView( pnlKeyPerson );

        //old code
        //proposalKeyPerson = new ProposalKeyStudyPersonnelAdminForm(functionType,keyStudyPersonnel,proposalID);
       // proposalKeyPerson.setParentProposal(isParentProposal());
       // proposalKeyPerson.setDetailForm( this );

//        pnlKeyPerson.add( proposalKeyPerson.showProposalKeyPersonForm(mdiForm));
//        JScrollPane jscrPnKeyPerson = new JScrollPane();
//        jscrPnKeyPerson.setViewportView( pnlKeyPerson );

        //Added for the Coeus Enhancement case:#1823 start step:12
        JPanel pnlSpecialReview = new JPanel(layout);
        //Case #1769 - start
        char funType='D';
        propsoalStatusCode = proposalDevelopmentFormBean.getCreationStatusCode();
        if(functionType==DISPLAY_MODE && (propsoalStatusCode == 2 || propsoalStatusCode == 4)){
        if(isSpecialReviewRight){
                funType = MODIFY_MODE;
                btnSave.setEnabled(isSpecialReviewRight);
            }else{
                funType = DISPLAY_MODE;
                btnSave.setEnabled(isSpecialReviewRight);
            }
        }else{
           funType = functionType ;
        }// Case #1769 - End

       //For the Coeus Enhancement case:#1799
        //proposalSpecialReview = new ProposalSpecialReviewForm(proposalID,specialReviewData,functionType);
        proposalSpecialReview = new ProposalSpecialReviewForm(proposalID,specialReviewData,funType);
       // specialReviewForm = new SpecialReviewForm(funType, specialReviewData, "PROPOSAL");
        specialReviewForm= SpecialReviewForm.getInstance(funType, specialReviewData, "PROPOSAL");
        specialReviewForm.setUnitNumber(unitNumber);
        proposalSpecialReview.setApprovalTypes(this.propReviewApprovalTypes);
        proposalSpecialReview.setSpecialReviewTypeCodes(this.propSpecialReviewCodes);
        proposalSpecialReview.setVecReplicateData(vecSplRevReplicateData);
        proposalSpecialReview.setValidateVector(this.propValidSpecialReviewList);
        //For the Coeus Enhancement case:#1799 start step:2
        proposalSpecialReview.setCvParameters(this.cvParameters);
        //End Coeus Enhancement case:#1799  step:2
        //Added for COEUSQA-2984 : Statuses in special review - start
        proposalSpecialReview.setCreationStatusCode(propsoalStatusCode);
        //Added for COEUSQA-2984 : Statuses in special review - end
        proposalSpecialReview.setFormData();
        pnlSpecialReview.add(proposalSpecialReview.showProposalSpecialForm());
        JScrollPane jscrPnSpecialReview = new JScrollPane();
        jscrPnSpecialReview.setViewportView( pnlSpecialReview );
        //End Coeus Enhancement case:#1823 start step:12

        JPanel pnlScienceCode = new JPanel(layout);
        proposalScienceCode = new ProposalScienceCodeAdminForm(functionType,scienceCodeData,proposalID);
        //Added for Case#3404-Outstanding proposal hierarchy changes - Start
         if(hierarchyMap!= null){
            proposalScienceCode.setParent(((Boolean)hierarchyMap.get("IS_PARENT")).booleanValue());
        }
        //Added for Case#3404-Outstanding proposal hierarchy changes - End
        pnlScienceCode.add( proposalScienceCode.showProposalScienceCodeForm(mdiForm) );
        JScrollPane jscrPnScienceCode = new JScrollPane();
        jscrPnScienceCode.setViewportView( pnlScienceCode );

        // set the tab into tab pane

        tbdPnProposal.setFont( CoeusFontFactory.getNormalFont() );
        tbdPnProposal.addTab("Proposal", jscrPn );
        tbdPnProposal.addTab("Organization", jscrPnOrg );
        tbdPnProposal.addTab("Mailing Info", jscrPnMInfo );
        tbdPnProposal.addTab("Investigator", jscrPnInv );
        tbdPnProposal.addTab("Key Person", jscrPnKeyPerson );
        //Added for the Coeus Enhancement case:#1823 start step:13
        tbdPnProposal.addTab("Special Review",jscrPnSpecialReview );
        //End Coeus Enhancement case:#1823 start step:13
        tbdPnProposal.addTab("Science Code", jscrPnScienceCode );

        if(otherData != null){
            JPanel pnlOther = new JPanel(layout);
            proposalOther = new ProposalOtherElementsAdminForm(functionType,otherData,proposalID);
            pnlOther.add( proposalOther );
            JScrollPane jscrPnOther = new JScrollPane();
            jscrPnOther.setViewportView( proposalOther );

            tbdPnProposal.addTab("Other", jscrPnOther );
        }
        tbdPnProposal.setSelectedIndex(0);



        //        if(inHierarchy){
        if(hierarchy){
            if(hierarchyController == null){
                hierarchyController= new ProposalHierarchyContoller(proposalHierarchyBean);
                hierarchyController.setFormData(unitNumber);
                scrPnHierarchy = new JScrollPane();
                scrPnHierarchy.setViewportView( hierarchyController.getControlledUI() );
                tbdPnProposal.addTab("Proposal Hierarchy", scrPnHierarchy);

            }
        }


         /* This  will catch the tab change event in the tabbed pane.
          * If the user selects any other tab from the CommitteeTab without
          * saving the committee information then it will prompt the user
          * to save the committee information before shifting to the other tab.
          */
        tbdPnProposal.addChangeListener(this);
        //rdias - Set reference to UCSD's personalization engine
//        customizeProposalDev();
        //rdias - UCSD's coeus personalization - End
        if( specialReviewForm.btnStartProtocol.getActionListeners().length<=0)
         specialReviewForm.btnStartProtocol.addActionListener(this);
        return tbdPnProposal;
    }
    public void stateChanged(ChangeEvent ce){
        JTabbedPane pn = (JTabbedPane)ce.getSource();
        int selectedTab = pn.getSelectedIndex();
        try {
            switch ( selectedTab ) {
                case 0 :
                    proposalDetail.setDefaultFocusForComponent();
                    break;
                case 1 :
                    proposalOrganization.setDefaultFocusForComponent();
                    break;
                case 2 :
                    proposalMailingInfo.setDefaultFocusForComponent();
                    break;
                case 3 :
                    proposalInvestigator.setDefaultFocusForComponent();
                    break;
                case 4:
//                    proposalKeyPersonController.g
//                    proposalKeyPersonController..setDefaultFocusForComponent();
                    break;
                    //Added for the Coeus Enhancement case:#1823 start step:14
                case 5:
                    proposalSpecialReview.setDefaultFocusInComponent();
                    break;
                    //End Coeus Enhancement case:#1823 step:14
                case 6:
                    proposalScienceCode.setDefaultFocusForComponent();
                    break;
                case 7:
                    //for Other
                    //Case :#3149 ? Tabbing between fields does not work on others tabs - Start
                    if(getFunctionType() != DISPLAY_MODE && proposalOther != null){
                        if(tbdPnProposal.getSelectedIndex() == 7 && count != 0 && proposalOther != null){
                            proposalOther.setTabFocus();
                            proposalOther.setTableFocus();
                            count++;
                        }

                        tbdPnProposal.addMouseListener(new MouseAdapter() {
                            public void mousePressed(MouseEvent e) {
                                count = 1;
                                if(tbdPnProposal.getSelectedIndex() == 7){
                                    proposalOther.setTableFocus();
                                }
                            }
                        });
                    }
                //Case :#3149 - End

                    break;
            }
        }catch(Exception e) {
            tbdPnProposal.setSelectedIndex(0);
            CoeusOptionPane.showErrorDialog(e.getMessage());
        }
    }


    /** This method will check for the right ALTER_PROPOSAL_DATA.
     *If the user has this right, then Proposal Special review
     *can be editable in DISPLAY mode also. Make server
     *call to get the right status
     *Case #1769
     *Method params modified with case 3587: Multicampus enhancement
     */
    private boolean getSpecialReviewRight(String proposalID)throws Exception{
        boolean hasSpecialReviewRight = false;
        RequesterBean  requesterBean = new RequesterBean();
        requesterBean.setFunctionType( CHECK_SPECIAL_REVIEW_RIGHT );
        requesterBean.setId(proposalID);
        requesterBean.setDataObject( ALTER_PROPOSAL_DATA );
        AppletServletCommunicator comm = new AppletServletCommunicator(
        connect, requesterBean);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response!= null){
            if(response.isSuccessfulResponse()){
                hasSpecialReviewRight = ((Boolean)response.getDataObject()).booleanValue();
            }else {
                throw new Exception(response.getMessage());
            }
        }
        return hasSpecialReviewRight;
    }// End Case #1769
    //Added For PPC
     private boolean getMaintainPersonCertificationRight()throws Exception{
        String ProposalNum= proposalDevelopmentFormBean.getProposalNumber();
        boolean hasMaintainPersonCertificationRight = false;
        RequesterBean  requesterBean = new RequesterBean();
        requesterBean.setFunctionType( MAINTAIN_PERSON_CERTIFICATION );
        requesterBean.setId(ProposalNum);
        requesterBean.setDataObject((String)mdiForm.getUserId() );
        AppletServletCommunicator comm = new AppletServletCommunicator(
        connect, requesterBean);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response!= null){
            if(response.isSuccessfulResponse()){
                hasMaintainPersonCertificationRight = ((Boolean)response.getDataObject()).booleanValue();
            }else {
                throw new Exception(response.getMessage());
            }
        }
        return hasMaintainPersonCertificationRight;
    }
     //End
    /**
     * initialize the proposal components.
     * Creates a panel and add the components.
     */
    private JComponent createProposalPanel() throws Exception{
        JPanel pnlForm = new JPanel();
        pnlForm.setLayout(new BorderLayout());

        pnlForm.add( createForm(), BorderLayout.CENTER );
        pnlForm.setSize( 650, 300 );
        pnlForm.setLocation(200, 200);
        coeusMessageResources = CoeusMessageResources.getInstance();
        return pnlForm;
    }


    /**
     * Used for saving the current activesheet  when clicked Save from File menu.
     */
    public void saveActiveSheet(){
        try{
            saveProposalDetails(SAVE_RETAIN_LOCK);
        }catch (PropertyVetoException propertyVetoException) {
            //Its Becoz of closing. so no need to print.
        }catch (CoeusUIException coeusUIException) {
            CoeusOptionPane.showDialog(coeusUIException);
            tbdPnProposal.setSelectedIndex(coeusUIException.getTabIndex());
        }catch(CoeusException ex){
            // added by manoj to display action messages as information messages
            CoeusOptionPane.showDialog(new CoeusClientException(ex));
        }

        catch ( Exception e) {
            e.printStackTrace();
            if(!( e.getMessage().equals(coeusMessageResources.parseMessageKey(
            "protoDetFrm_exceptionCode.1130")) )){
                CoeusOptionPane.showInfoDialog(e.getMessage());
            }
        }

    }

    /** Sets the reference to the table used in <CODE>ProposalBaseWindow</CODE>.
     *
     * @param protocolsheet JTable reference of the table used in
     * <CODE>ProposalBaseWindow</CODE>.
     */
    public void setProposalSheet(JTable proposalSheet){
        this.tblProposal = proposalSheet;
        /**
         * Implementation of Previous / Next Tool bar Button Functionality.
         *
         * Updated Subramanya dated April 23 2003.
         */
        if( this.functionType == CoeusGuiConstants.DISPLAY_MODE ){
            btnNextProposal.addActionListener(this);
            btnPrevProposal.addActionListener(this);
            int selRow = tblProposal.getSelectedRow();
            if( selRow == tblProposal.getRowCount()-1 ){
                btnNextProposal.setEnabled(false);
            }
            if( selRow == 0 ){
                btnPrevProposal.setEnabled(false);
            }
        }
    }

    /**
     * This Method is used to Refresh the Data in the Proposal Development Tabs
     * According to Show Next and Previous Button.
     *
     */
    /** This method is used to show the <CODE>ProtocolDetailsForm</CODE>.
     */
    public void showDialogForm() throws Exception{

        getContentPane().add( createProposalPanel() );

        getStatusDescription();
         /* This will catch the window closing event and
          * checks any data is modified.If any changes are done by
          * the user the system will ask for confirmation of Saving the info.
          */
        this.addVetoableChangeListener(new VetoableChangeListener(){
            public void vetoableChange(PropertyChangeEvent pce)
            throws PropertyVetoException {
                if (pce.getPropertyName().equals(
                JInternalFrame.IS_CLOSED_PROPERTY) ) {
                    boolean changed = ((Boolean) pce.getNewValue()).booleanValue();
                    //Fix for releaseing the lock when user cancels the close request
                    //Added by Geo on 10-Jun-2005
                    //BEGIN
                    if(changed)
                        if( !discardData && isSaveRequired() ) {
                            //Commented for the fix mentioned above
                            //                    if( !discardData && changed && isSaveRequired() ) {
                            //END
                            try {
                                closeProposalDetailForm();
                              //  specialReviewForm.clearSpecialReviewData();
                              //  proposalSpecialReview.setVecData(null);
                             //   proposalDevelopmentFormBean.setPropSpecialReviewFormBean(null);
                            }catch ( CoeusUIException cue ){
                                tbdPnProposal.setSelectedIndex(cue.getTabIndex());
                                CoeusOptionPane.showDialog(cue);
                                throw new PropertyVetoException(
                                coeusMessageResources.parseMessageKey(
                                "protoDetFrm_exceptionCode.1130"),null);
                            }catch ( Exception e) {
                                //e.printStackTrace();
                                if(!( e.getMessage().equals(
                                coeusMessageResources.parseMessageKey(
                                "protoDetFrm_exceptionCode.1130")) )){
                                    CoeusOptionPane.showInfoDialog(e.getMessage());
                                }
                                throw new PropertyVetoException(
                                coeusMessageResources.parseMessageKey(
                                "protoDetFrm_exceptionCode.1130"),null);
                            }
                        }else{
                            //Commented to allow closing of proposal detail form even if budget is open
                        /*
                        if( isBudgetWindowOpen() ) {
                            CoeusOptionPane.showInfoDialog(
                                coeusMessageResources.parseMessageKey(
                                "budget_common_exceptionCode.1008"));
                             throw new PropertyVetoException(
                                        coeusMessageResources.parseMessageKey(
                                        "protoDetFrm_exceptionCode.1130"),null);
                        }
                         */
                            try{
                               // if( checkChildWindows() ){
                                    releaseUpdateLock();
//                                 if(functionType != TypeConstants.DISPLAY_MODE && specialReviewForm!=null)
//                              //  specialReviewForm.clearSpecialReviewData();
                                    mdiForm.removeFrame(CoeusGuiConstants.PROPOSAL_DETAILS_FRAME_TITLE,proposalID);
                                //}
                            }catch ( Exception e) {
                                //e.printStackTrace();
                                if(!( e.getMessage().equals(
                                coeusMessageResources.parseMessageKey(
                                "protoDetFrm_exceptionCode.1130")) )){
                                    CoeusOptionPane.showInfoDialog(e.getMessage());
                                }
                                throw new PropertyVetoException(
                                coeusMessageResources.parseMessageKey(
                                "protoDetFrm_exceptionCode.1130"),null);
                            }
                        }
                }
            }
        });

        setTitle( CoeusGuiConstants.PROPOSAL_DETAILS_FRAME_TITLE + " - " + proposalID );

        mdiForm.putFrame( CoeusGuiConstants.PROPOSAL_DETAILS_FRAME_TITLE,
        this.proposalID, this.functionType, this );
        mdiForm.getDeskTopPane().add(this);
        setVisible(true);
        setSelected(true);
        enableDisableMenuItems();   //Added by Vyjayanthi

        if( functionType != DISPLAY_MODE ) {
            tbdPnProposal.setSelectedIndex(0);
            proposalDetail.setFocusToType();
        }

    }


    /**
     * This Method is used to Refresh the Proposal Detail Form Data set
     * with out reconstructing the visual components.
     * @param proposalID holds the proposal ID.
     */
    private void refreshDialogForm( String proposalID ) throws Exception{
        setValues(getProposalDetails( proposalID ));

        if( proposalDevelopmentFormBean != null ) {

            setTitle( CoeusGuiConstants.PROPOSAL_DETAILS_FRAME_TITLE + " - " + proposalID );
            mdiForm.putFrame( CoeusGuiConstants.PROPOSAL_DETAILS_FRAME_TITLE,
            proposalID, this.functionType, this );

            proposalDetail.setValues( proposalDevelopmentFormBean );
            proposalOrganization.setValues( proposalDevelopmentFormBean );
            proposalMailingInfo.setValues( proposalDevelopmentFormBean );
            //proposalOrganization.setFormData();
            //proposalMailingInfo.setFormData();
            // write similar code to investigator also.
            proposalInvestigator.setInvestigatorData(
            proposalDevelopmentFormBean.getInvestigators() );
           // proposalKeyPersonController=new ProposalKeyPersonController(proposalID,keyStudyPersonnel,functionType);
            proposalKeyPersonController.convertVectorToBean((CoeusVector)proposalDevelopmentFormBean.getKeyStudyPersonnel());
            proposalKeyPersonController.setFormData(
            proposalDevelopmentFormBean.getKeyStudyPersonnel() );

            proposalScienceCode.setProposalScienceCodeData(
            proposalDevelopmentFormBean.getScienceCode() );


            if ( proposalOther != null ) {
                proposalOther.setPersonColumnValues( proposalDevelopmentFormBean.getOthers() );
            }
        }


        //Enable-Disabling of view back/next button
        int selectedProposalRow = tblProposal.getSelectedRow();
        if( selectedProposalRow == 0 ){
            btnPrevProposal.setEnabled(false);
        }else if( selectedProposalRow  == tblProposal.getRowCount()-1 ){
            btnNextProposal.setEnabled(false);
        }
        //update to handle new window menu item to the existing Window Menu.
        edu.mit.coeus.gui.menu.CoeusWindowMenu windowMenu = mdiForm.getWindowMenu();
        if( windowMenu != null ){
            windowMenu = windowMenu.addNewMenuItem( this.getTitle(), this );
            windowMenu.updateCheckBoxMenuItemState( this.getTitle(), true );
        }

    }

    /**
     * This method is used to connect to the server and gets the Proposal details
     * for specific proposal Id.
     *
     * @param proposalId String represent the Proposal ID.
     * @return Vector contain the Proposal details.
     */
    private Vector getProposalDetails( String proposalId ) throws Exception {
        char funType = 'D';
        Vector dataObjects = null;
        String connectTo = CoeusGuiConstants.CONNECTION_URL +
        CoeusGuiConstants.PROPOSAL_SERVLET ;

        // connect to the database and get the formData for the given organization id
        RequesterBean request = new RequesterBean();
        if( functionType == CoeusGuiConstants.AMEND_MODE ){
            request.setFunctionType( CoeusGuiConstants.AMEND_MODE );
        }else{
            request.setFunctionType( functionType );
        }
        request.setId( proposalId );
        // 2930: Auto-delete Current Locks based on new parameter - Start
        // Send the Unit Number to the serverside If ProposalDetailForm is opening in
        // Edit Mode.
//        if( functionType == ADD_MODE ) {
//            request.setDataObject( unitNumber );
//        }
         if( functionType == ADD_MODE || functionType == MODIFY_MODE) {
            request.setDataObject( unitNumber );
        }
        // 2930: Auto-delete Current Locks based on new parameter - End
        request.setUserName(mdiForm.getUserName());
        AppletServletCommunicator comm
        = new AppletServletCommunicator( connectTo, request );
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response == null) {
            response = new ResponderBean();
            response.setResponseStatus(false);
            response.setMessage(coeusMessageResources.parseMessageKey(
            "server_exceptionCode.1000"));
        }
        if (response.isSuccessfulResponse()) {
            dataObjects = response.getDataObjects();
            //Code Added by Vyjayanthi on 27/01/2004 to check for rights - Start
            hasSubmitForApprovalRt = ((Boolean)dataObjects.get(9)).booleanValue();
            hasSubmitToSponsorRt = ((Boolean)dataObjects.get(10)).booleanValue();
            hasAddViewerRt = ((Boolean)dataObjects.get(11)).booleanValue();
            //Code Added by Vyjayanthi on 27/01/2004 to check for rights - End
        } else {
            if (response.isLocked()) {
            /* the row is being locked by some other user
             */
                setModifiable(false);
                //Modified for case# 3439 to include the locking message - start
                //throw new Exception("proposal_row_clocked_exceptionCode.777777");
                coeusMessageResources = CoeusMessageResources.getInstance();
                String msg = "";
                if(response.getMessage()!=null){
                    msg = msg + response.getMessage().trim() + ". ";
                }
                msg =  msg + coeusMessageResources.parseMessageKey(
                    "proposal_row_clocked_exceptionCode.777777");
                throw new Exception(msg);
                //Modified for case# 3439 to include the locking message - end

            }else {
                throw new Exception(response.getMessage());
            }
        }
        return dataObjects;
    }


    /**
     * set the bean values. The data are from the server after
     *
     * @param dataObjects vector contain the Protocol maintenance data
     * 0th element contains Protocol Status details
     * 1st element contain Protocol Correspondent Type details
     * 2nd element contain Fund Source types list
     * 3rd element contain available Vulnerable subjects
     * 4th element contain selected questions list
     * 5th element contain Protocol Details
     */
    private void setValues(Vector dataObjects) {
        // get the bean objects from the vector
        if (dataObjects != null) {
            proposalDevelopmentFormBean = (ProposalDevelopmentFormBean)
            dataObjects.elementAt(0);
            propPersons = proposalDevelopmentFormBean.getPropPersons();
            propDefaultUsers = proposalDevelopmentFormBean.getUserInfo();
            if( propPersons != null ) {
                int perSize = propPersons.size();
                ProposalPersonFormBean oldPersonBean;
                for( int perIndx = 0 ; perIndx < perSize ; perIndx++ ) {
                    oldPersonBean = ( ProposalPersonFormBean ) propPersons.elementAt( perIndx );
                }
            }

            //COEUSQA:3446 - Disable YNQ icon & menu-path in Proposal Development if all Proposal YNQ's are marked Inactive - Start
            if(proposalDevelopmentFormBean.getPropYNQuestionList() == null || proposalDevelopmentFormBean.getPropYNQuestionList().isEmpty()) {
                yesNoQuestion.setEnabled(false);
                btnYesNoQues.setEnabled(false);
            }
            //COEUSQA:3446 - End
            //if( functionType != DISPLAY_MODE ) {
            //proposal type look up
            proposalTypes = (Vector) dataObjects.elementAt(1);
            //proposal NSF code look up
            proposalNSFCodes = (Vector) dataObjects.elementAt(2);
            //proposl activity code look up
            proposalActivityTypes = (Vector) dataObjects.elementAt(3);
            //proposal notice opp look up
            proposalNoticeOpp = (Vector) dataObjects.elementAt(4);
            // get the role rights
            proposalRoleRights = (Vector) dataObjects.elementAt(5);
            //Added for fixing bug id : 1856 :start
            if(proposalRoleRights == null) {
               proposalRoleRights= new Vector();
            }
            //bug id : 1856 :end
            // get the Special Review codes
            propSpecialReviewCodes = (Vector) dataObjects.elementAt(6);
            //COEUSQA-2320_Show in Lite for Special Review in Code table_Start
            propSpecialReviewCodes = propSpecialReviewCodes == null ? new Vector():propSpecialReviewCodes;
            //COEUSQA-2320_Show in Lite for Special Review in Code table_End
            // get the Approval Type codes
            propReviewApprovalTypes = (Vector) dataObjects.elementAt(7);
            // get the Special Review List
            propValidSpecialReviewList = (Vector) dataObjects.elementAt(8);
            //COEUSQA-2320_Show in Lite for Special Review in Code table_Start
            propValidSpecialReviewList = propValidSpecialReviewList == null ? new Vector():propValidSpecialReviewList;
            //COEUSQA-2320_Show in Lite for Special Review in Code table_End
            //proposal status look up
            proposalStatus = (Vector) dataObjects.elementAt(12);
            // Get the Propsoal Hierarchy data

            // gnprh - start
            hierarchyMap = (HashMap) dataObjects.elementAt(13);
            // Get the Proposal Hierarchy Tree data
            proposalHierarchyBean = (ProposalHierarchyBean)dataObjects.elementAt(14);

            //For the Coeus Enhancement case:#1799 start step:3
            /* gnprh
             *Currently in the servelt the index of this is 15.
             *By commneting the proposal hierarchy, the index will be 13
             *Need to change the index to 15 while delivering the propsoal
             *hierarchy */
            cvParameters = (CoeusVector)dataObjects.elementAt(15);
            //cvParameters = (CoeusVector)dataObjects.elementAt(13);
            //End Coeus Enhancement case:#1799 start step:3

            //Added for Case # 2162 - Adding Award Type - Start
            vecAwardTypes = (CoeusVector)dataObjects.elementAt(16);
            //Added for Case # 2162 - Adding Award Type  - End

            //Added for Case 2406 - Organizations and Location - start
            vecLocationTypes = (Vector)dataObjects.elementAt(17);
            //Added for Case 2406 - Organizations and Location - end

            //}
            if ( functionType == ADD_MODE ) {

                proposalDevelopmentFormBean.setOwnedBy( unitNumber );
                proposalDevelopmentFormBean.setOwnedByDesc( unitName );
            }
            /** Check if the selected proposal is in Hierarchy. If it is in Hierarchy
             *then check for the parent and then set the values
             */
            //* gnprh
            if(hierarchyMap!= null){
                boolean inHierarchy = ((Boolean)hierarchyMap.get("IN_HIERARCHY")).booleanValue();
                boolean isParent = false;
                if(inHierarchy) {
                    setHierarchy(inHierarchy);
                    setParentPropNo((String)hierarchyMap.get("PARENT_PROPOSAL"));
                    isParent = ((Boolean)hierarchyMap.get("IS_PARENT")).booleanValue();
                    if(isParent){
                        setParentProposal(isParent);
                    }
                }
            }//*Commnetd for the Proposal Hierarchy

        }
    }

    /** This method is used to set the <CODE>modifiable</CODE> flag.
     *
     * @param modifiable boolean value true if the selected record can be
     * modified else false.
     */
    public void setModifiable(boolean modifiable){
        this.modifiable = modifiable;
    }

    /** This method is used to check whether the selected record can be modified
     * or not.
     *
     * @return true if the selected record can be modified else false.
     */
    public boolean isModifiable(){
        return this.modifiable;
    }

    /** This method is used to get the functionType.
     * This will be invoked from <CODE>ProposalBaseWindow</CODE>.
     * @return functionType character which specifies the functionType.
     */
    public char getFunctionType(){
        return this.functionType;
    }

    /** This method is used to get the form opened mode.
     * This will be called from <CODE>ProposalBaseWindow</CODE>.
     * @param functionType character which specifies the form opened mode.
     */
    public void setFunctionType(char functionType){
        this.functionType = functionType;
    }

    /** The method used to get the save status of the <CODE>ProposalDetailForm</CODE>.
     * @return save boolean true if there are any unsaved modifications,
     * else false.
     */
    protected boolean isSaveRequired() {
        /* check all the falgs in Proposal detail, Proposal Organization,
         * Proposal MailingInfo, Proposal Investigator, Proposal KeyPerson,
         * Proposal ScienceCode, Proposal Other to check
         * whether any data is changed by the user or
         * not to set the save required flag in this ProtocolDetails form.
         *
         */
        //Case #1769 - start
        char funType = 'M';
        if(functionType==DISPLAY_MODE){
            propSpecialRevTab = proposalSpecialReview.isSaveRequired();
            if(isSpecialReviewRight && propSpecialRevTab ){
                funType = MODIFY_MODE;
            }else{
                funType = DISPLAY_MODE;
            }
        }//#1769 - End

        try{
            //case #1769 - start
            //if( functionType != DISPLAY_MODE /*&& !saveRequired*/ ) {
            if( funType != DISPLAY_MODE ) {//#1769 - End
                proposalDetail.getFormData();
                //Modified for case 2406 - Organization and Location - start
                cvSites = proposalOrganization.getFormData();
                //Modified for case 2406 - Organization and Location - end
                proposalMailingInfo.getFormData();

                // need to implement for investigators
                investigators = proposalInvestigator.getFormData();
                if( proposalOther != null ) {
                    propOthersTab = proposalOther.isSaveRequired();
                }

                propTab = proposalDetail.isSaveRequired();
                propOrgTab = proposalOrganization.isSaveRequired();
                propMailTab = proposalMailingInfo.isSaveRequired();
                propInvTab = proposalInvestigator.isSaveRequired();
                //Added for the Coeus Enhancement case:#1823 start step:15
                propSpecialRevTab = proposalSpecialReview.isSaveRequired();
                //End Coeus Enhancement case:#1823 step:15
                propKeyTab = proposalKeyPersonController.isDataChanged();
                propScienceTab = proposalScienceCode.isSaveRequired();
                // UnCommented for COEUSQA-2881 : Last Update User and Timestamp changes - Start
                boolean propYNQ = false;
                if(proposalYesNoQuestionsForm != null){
                    propYNQ = proposalYesNoQuestionsForm.isSaveRequired();
                }
                // UnCommented for COEUSQA-2881 : Last Update User and Timestamp changes - End
                if(rolesForm != null ) {
                    propRoleSaveReq = rolesForm.isSaveRequired();
                }
                // Modified for COEUSQA-2881 : Last Update User and Timestamp changes - Start
//                if ( propTab || propOrgTab || propMailTab || propInvTab
//                        || propKeyTab || propSpecialRevTab|| propScienceTab || propOthersTab
//                        || propRoleSaveReq ){ //|| propYNQ) {
//                    saveRequired = true;
//                }
                if ( propTab || propOrgTab || propMailTab || propInvTab
                || propKeyTab || propSpecialRevTab|| propScienceTab || propOthersTab
                || propRoleSaveReq || propYNQ){
                    saveRequired = true;
                }else{
                    saveRequired = false;
                }
                // Modified for COEUSQA-2881 : Last Update User and Timestamp changes - End
                //                System.out.println("1tab:"+propTab);
                //                System.out.println("org:"+propOrgTab);
                //                System.out.println("mail:"+propMailTab);
                //                System.out.println("inv:"+propInvTab);
                //                System.out.println("key:"+propKeyTab);
                //                System.out.println("sciencecode:"+propScienceTab);
                //                System.out.println("other:"+propOthersTab);
                //                System.out.println("roles:"+propRoleSaveReq);
                //                System.out.println("propYNQ:"+propYNQ);
            }
            //    return saveRequired;

        }catch(Exception e){
            e.printStackTrace();
            saveRequired = true;
        }
        return saveRequired;
    }
    /** check the lock, when Proposal special Review is made editable in DISPLAY MODE.
     *Check the lock when user saves the data
     *@param proposalNumber, Unit number
     *@throws Exception
     *@ returns boolean
     *Case #1769  - start
     */
    private boolean checkAndGetLock(String proposalNumber, String unitNumber)
    throws Exception{
        boolean success = false;
        String connectTo = CoeusGuiConstants.CONNECTION_URL + CoeusGuiConstants.PROPOSAL_SERVLET;
        Vector data = new Vector();
        data.add(proposalNumber);
        data.add(unitNumber);
        RequesterBean request = new RequesterBean();
        request.setFunctionType( GET_LOCK_FOR_SPECIAL_REVIEW );
        request.setDataObjects( data );
        AppletServletCommunicator comm = new AppletServletCommunicator(
        connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if( response.isSuccessfulResponse() ){
            success = ((Boolean)response.getDataObject()).booleanValue();
        }else{
            throw new Exception(response.getMessage());
        }
        return success;
    }
/**
 *
 * @param SaveType
 * @return
 * @throws Exception
 * Function For Create New Protocol
 * Created Shibu
 */
      protected String savenewProtocol(char SaveType) throws Exception{
          //for validation Checking..
          String   newProtocolNumber=null;
            if( !proposalDetail.validateData() ){
                tbdPnProposal.setSelectedIndex( 0 );
            }else if ( !proposalInvestigator.validateData() ) {
                tbdPnProposal.setSelectedIndex( 3 );
            }else{


             String connectTo = CoeusGuiConstants.CONNECTION_URL
                + CoeusGuiConstants.PROPOSAL_SERVLET;
                /* connect to the database and save the protocol details         */
                /// char SaveType='8';   /* for proposal to protocol  saveType*/
                protocolBean=new ProtocolInfoBean();
                Vector investigators_vec = proposalInvestigator.getFormData();
                protocolBean.setInvestigators(investigators_vec);




                //  setting the new special review vector...
                 Vector specialReviewData_vec = proposalSpecialReview.getProposalSpecialReviewDataProtocol();
                 protocolBean.setSpecialReviews(specialReviewData_vec);

//

//                  vecSpecialReviewData = specialReviewForm.getSpecialReviewData();
//                  protocolBean.setSpecialReviews(vecSpecialReviewData);


                proposalDetail.getFormData();
                String stitle=proposalDevelopmentFormBean.getTitle();
                String protocolDescription="Protocol Created From Proposal:"+proposalID;
                Vector cvdata=proposalOrganization.getFormData();
                protocolBean.setLocationLists(cvdata);
                 Vector newUserRoles = null;

//                    if ( rolesForm == null ){
//                      //  newUserRoles = proposalDevelopmentFormBean.getUserRolesInfoBean();
//                       // newUserRoles = proposalDevelopmentFormBean.getUserInfo();
//
//                    }else {
//                        if( tempUserRoles != null ) {
//                            newUserRoles = tempUserRoles;
//                        }
//                    }
//                   Hashtable htNewUsers = getUserRoleInfo( newUserRoles );
//                   Vector userRolesForDB  = checkModifications(
//                    new Hashtable(), htNewUsers );
                   // protocolBean.setUserRoles(userRolesForDB);
                   protocolBean.setUserRoles(null);
                protocolBean.setProtocolNumber(instPropNumber);
                protocolBean.setSequenceNumber(0);
                protocolBean.setAcType("I");
                protocolBean.setTitle(stitle) ;
                protocolBean.setDescription(protocolDescription);
                protocolBean.setProtocolTypeCode(1);
                protocolBean.setProtocolStatusCode(100);
                protocolBean.setFundingSourceIndicator("P1");
                protocolBean.setSpecialReviewIndicator("P1");
                protocolBean.setIsBillableFlag(false);
                protocolBean.setVulnerableSubjectIndicator("N0");
                protocolBean.setKeyStudyIndicator("N0");
                protocolBean.setCorrespondenceIndicator("N0");
                protocolBean.setReferenceIndicatorStatus("N0");
                protocolBean.setReferenceIndicator("N0");
                protocolBean.setProjectsIndicator(null);

                Vector cvFund=new Vector() ;
                String sponsorcode="1";
                String sponsorName="SponsorName";
                if( ProposalDetailAdminForm.SPONSOR_CODE != null ){
                    sponsorcode= ProposalDetailAdminForm.SPONSOR_CODE;
                    sponsorName= ProposalDetailAdminForm.SPONSOR_CODE +" : "+ ProposalDetailAdminForm.SPONSOR_DESCRIPTION  ;

                    ProtocolFundingSourceBean protocolFundingSourceBean = new ProtocolFundingSourceBean();
                    protocolFundingSourceBean.setFundingSource(sponsorcode);
                    protocolFundingSourceBean.setFundingSourceName(sponsorName);
                    protocolFundingSourceBean.setFundingSourceTypeCode(1);
                    protocolFundingSourceBean.setAcType("I");
                    cvFund.add(protocolFundingSourceBean);
                    protocolBean.setFundingSources(cvFund);
                 }
                

                RequesterBean request = new RequesterBean();
                request.setFunctionType( SaveType );
                request.setId( proposalID );
                request.setDataObject( protocolBean );
                AppletServletCommunicator comm = new AppletServletCommunicator(
                connectTo, request);
                comm.send();
                ResponderBean response = comm.getResponse();
                 if (response.isSuccessfulResponse()){
                     newProtocolNumber=(String)response.getDataObject();
                     CoeusOptionPane.showInfoDialog("New stub protocol created successfully");
                 }else{
                     CoeusOptionPane.showInfoDialog("New stub protocol creation failed");
                 }

                 return newProtocolNumber;
              }
          //}
           return newProtocolNumber;
      }


      /**
       * for new protocol linked to protocols
       * @param protocolNumber
       * @return
       * @throws CoeusException
       */
    private ProtocolInfoBean getProtocolDetails(String protocolNumber)throws CoeusException{
        ProtocolInfoBean infoBean = new ProtocolInfoBean();
        if (protocolNumber != null && protocolNumber.trim().length() > 0 && !protocolNumber.equals("")) {
            String connectTo = CoeusGuiConstants.CONNECTION_URL + "/protocolMntServlet";
            RequesterBean requester = new RequesterBean();
            requester.setFunctionType('f');
            requester.setDataObject(protocolNumber);
            AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, requester);
            comm.send();
            ResponderBean response = comm.getResponse();
            if(!response.isSuccessfulResponse()){
                throw new CoeusException(response.getMessage(), 1);
            }else{
                infoBean = (ProtocolInfoBean)response.getDataObject();
            }
        }
        return infoBean;
    }
    
    // JM 4-4-2013 check to make sure person is not on proposal twice
    private boolean checkPersonDuplicates(ProposalDevelopmentFormBean formBean, Vector keyPersons) {
        int matches = 0;
        boolean duplicatesFound = false;
        Vector investigators = (Vector) formBean.getInvestigators();
        String selPersonId, checkPersonId;        
        if (investigators != null) {
        	ProposalInvestigatorFormBean bean1;
        	ProposalKeyPersonFormBean bean2;
            for( int perRow = 0 ; perRow < investigators.size(); perRow++ ) {
            	matches = 0;
            	bean1 = (ProposalInvestigatorFormBean) investigators.elementAt(perRow);
                selPersonId = (String) bean1.getPersonId();
                if (keyPersons != null) {
	                for(int checkRow = 0 ; checkRow < keyPersons.size(); checkRow++ ){
	                	bean2 = (ProposalKeyPersonFormBean) keyPersons.elementAt(checkRow);
	                	checkPersonId = (String) bean2.getPersonId();
	                    if (selPersonId.equals(checkPersonId)) {
	                    	duplicatesFound = true;
	                    }
	                }
                }
            }
        }
        return duplicatesFound;
    }
    // JM END
    
    private void saveProposalDetails( char saveType ) throws Exception{

        if ( isSaveRequired() ) {
            /** check for the lock, if lock exists show the error message
             *else lock the current proposal number for the
             *propsoal special review updation
             *Case #1769  - start
             */
            if(functionType==DISPLAY_MODE){
                String unitNo = "";
                if(isSpecialReviewRight){
                    if(unitNumber==null || unitNumber.trim().equals("")){
                        unitNo = this.proposalDevelopmentFormBean.getOwnedBy();
                    }else{
                        unitNo = unitNumber;
                    }
                    isSpecialReviewLocked = checkAndGetLock(proposalID,unitNo);
                    checkLock = true;
                }
            }//Case #1769  - End

            if( !proposalDetail.validateData() ){
                tbdPnProposal.setSelectedIndex( 0 );
            }else if ( !proposalOrganization.validateData() ) {
                tbdPnProposal.setSelectedIndex( 1 );
            }else if ( !proposalInvestigator.validateData() ) {
                tbdPnProposal.setSelectedIndex( 3 );
            }else if ( !proposalKeyPersonController.validate()) {
                tbdPnProposal.setSelectedIndex( 4 );
            //Added for the Coeus Enhancement case:#1823 start step:16
            }else if(!proposalSpecialReview.validateData()){
                tbdPnProposal.setSelectedIndex( 5 );
            //End Coeus Enhancement case:#1823 step:16
            }else if ( !proposalScienceCode.validateData() ) {
                tbdPnProposal.setSelectedIndex( 6 );//modified for the Coeus Enhancement case:#1823 start step:17
            }else if ( proposalOther != null && !proposalOther.validateData() ) {
                tbdPnProposal.setSelectedIndex( 7 );//modified for the Coeus Enhancement case:#1823 start step:18
            }else if(!proposalMailingInfo.validateData()){// Case 2463
                tbdPnProposal.setSelectedIndex( 2 );
            }else{
                proposalKeyPersonController.saveFormData();
                keyStudyPersonnel = proposalKeyPersonController.getFormkpData();
                //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - Start
                if(keyStudyPersonnel != null && keyStudyPersonnel.size()>0){
                    allKeyStudyPersonnel = addKeyPersonData(keyStudyPersonnel, allKeyStudyPersonnel);
                }
                //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - End
                ProposalKeyPersonFormBean tmpKeyPerson;
                //for adding the proposal person table with the keyperson data.
                if(keyStudyPersonnel!=null)
                {
                    for(int i=0; i<keyStudyPersonnel.size();i++)
                    {
                        tmpKeyPerson=(ProposalKeyPersonFormBean)keyStudyPersonnel.get(i);
                        if(tmpKeyPerson!=null)
                        {if(tmpKeyPerson.getAcType()!=null){
                            if( (tmpKeyPerson.getAcType()).compareTo(DELETE_RECORD)!=0)
                         { this.addPropPerson(tmpKeyPerson.getPersonId(),!((KeyPersonBean)tmpKeyPerson).isPerOrRol());}
                         else
                         {this.deletePropPerson(tmpKeyPerson.getPersonId(), true);}
                         } }//end of inner if loop
                    }//end of the for loop
                }//end of if condition



                keyStudyPersonnelUnits=proposalKeyPersonController.getFormUnitData();
                scienceCodeData = proposalScienceCode.getScienceCodeAdminData();
                //Added for the Coeus Enhancement case:#1823 start step:19
                specialReviewData = proposalSpecialReview.getProposalSpecialReviewData();
                //End Coeus Enhancement case:#1823 start step:19
                //if(proposalYesNoQuestionsForm != null){
                //  vecPropYNQAnswerList = proposalYesNoQuestionsForm.getVecProposalQuestionTable();
                //}

                if( proposalOther != null ) {
                    otherData = proposalOther.getOtherColumnElementData();
                }
                //Added for case 2406 - Organization and Location - start
                proposalDevelopmentFormBean.setSites(cvSites);
                //Added for case 2406 - Organization and Location - end
                proposalDevelopmentFormBean.setPropPersons( propPersons );
                proposalDevelopmentFormBean.setInvestigators( investigators );
                proposalDevelopmentFormBean.setKeyStudyPersonnel( keyStudyPersonnel );

                proposalDevelopmentFormBean.setKeyStudyPersonnelUnits(keyStudyPersonnelUnits);
                //Added for the Coeus Enhancement case:#1823 start step:20
                proposalDevelopmentFormBean.setPropSpecialReviewFormBean(specialReviewData);
                //End Coeus Enhancement case:#1823 start step:20
                proposalDevelopmentFormBean.setScienceCode( scienceCodeData );
                proposalDevelopmentFormBean.setOthers( otherData );

                if(vecPropYNQAnswerList!= null && vecPropYNQAnswerList.size()>0){
                    proposalDevelopmentFormBean.setPropYNQAnswerList(vecPropYNQAnswerList);
                }
                if ( functionType == ADD_MODE ) {
                    // adding default users of the protocol's lead unit
                    // to the OSP$PROTOCOL_USER_ROLES table

                    Vector newUserRoles = null;
                    if ( rolesForm == null ){
                        newUserRoles = proposalDevelopmentFormBean.getUserRolesInfoBean();
                    }else {
                        if( tempUserRoles != null ) {
                            newUserRoles = tempUserRoles;
                        }
                    }
                    //System.out.println("in add mode.... user and roles ");
                    Hashtable htNewUsers = getUserRoleInfo( newUserRoles );
                    //System.out.println("in add mode getting acTypes");
                    Vector userRolesForDB  = checkModifications(
                    new Hashtable(), htNewUsers );
                    proposalDevelopmentFormBean.setPropRolesFormBean( userRolesForDB );
                    proposalDevelopmentFormBean.setAcType( INSERT_RECORD );

                }else {             // if ( rolesForm != null && rolesForm.isSaveRequired() ) {

                    Hashtable htExistingUsers = new Hashtable();
                    Hashtable htTempUsers = new Hashtable();
                    //System.out.println("in modify mode....");
                    existingUserRoles = proposalDevelopmentFormBean.getUserRolesInfoBean();
                    if( existingUserRoles != null ){
                        //System.out.println("existingUserRoles:");
                        htExistingUsers = getUserRoleInfo( existingUserRoles );
                    }
                    if ( tempUserRoles != null ) {
                        //System.out.println("tempUserRoles:");
                        htTempUsers = getUserRoleInfo( tempUserRoles );
                        //System.out.println("in modify mode getting acTypes");
                        Vector userRolesForDB  = checkModifications(
                        htExistingUsers, htTempUsers );
                        proposalDevelopmentFormBean.setPropRolesFormBean( userRolesForDB );
                    }
                    //Added for Narrative User rights - start
                    if(narrativeUserRights != null && narrativeUserRights.size() >0){
                        proposalDevelopmentFormBean.setNarrativeUserRights(narrativeUserRights);
                    }
                    //Added for Narrative User rights - end
                    if( narrativeStatusUpdated ) {
                        proposalDevelopmentFormBean.setNarrativeStatus(""+narrativeStatus);
                    }
                    proposalDevelopmentFormBean.setAcType( UPDATE_RECORD );
                    /*if ( functionType == MODIFY_MODE ) {
                        proposalDevelopmentFormBean.setAcType( UPDATE_RECORD );
                    }*/
                }
                /*if ( functionType == ADD_MODE ) {
                    proposalDevelopmentFormBean.setAcType( INSERT_RECORD );
                }else{
                    proposalDevelopmentFormBean.setAcType( UPDATE_RECORD );
                }*/
                String connectTo = CoeusGuiConstants.CONNECTION_URL
                + CoeusGuiConstants.PROPOSAL_SERVLET;
                /* connect to the database and save the proposal details
                 */
                RequesterBean request = new RequesterBean();
                request.setFunctionType( saveType );
                request.setId( proposalID );
                request.setDataObject( proposalDevelopmentFormBean );
                AppletServletCommunicator comm = new AppletServletCommunicator(
                connectTo, request);
                comm.send();
                ResponderBean response = comm.getResponse();
                if( response.isSuccessfulResponse() ){
                    //System.out.println("saved successfully...");
                    //                    updateRow();
                    //                    if( saveType == SAVE_RETAIN_LOCK ) {
                    Vector dataObject = new Vector();

                    dataObject = ( Vector ) response.getDataObjects();
                    if( dataObject != null && dataObject.size() > 0 ) {
                        proposalDevelopmentFormBean =
                        ( ProposalDevelopmentFormBean )dataObject.elementAt( 0 );
                        // get the Special Review codes
                        propSpecialReviewCodes = (Vector) dataObject.elementAt(1);
                        // get the Approval Type codes
                        propReviewApprovalTypes = (Vector) dataObject.elementAt(2);
                        // get the Special Review List
                        propValidSpecialReviewList = (Vector) dataObject.elementAt(3);
                        //proposal status look up

                        propPersons = proposalDevelopmentFormBean.getPropPersons();
                        if( propPersons != null ) {
                            int perSize = propPersons.size();
                            ProposalPersonFormBean oldPersonBean;
                            for( int perIndx = 0 ; perIndx < perSize ; perIndx++ ) {
                                oldPersonBean = ( ProposalPersonFormBean ) propPersons.elementAt( perIndx );
                            }
                        }
                        observable.setFunctionType( functionType );
                        observable.notifyObservers( proposalDevelopmentFormBean );
//                        System.out.println("calling update data....");
                        updateData();
                    }
                    //                    }
                    saveRequired = false;
                }else{
                	// JM 4-10-2013 don't want to drop lock on error
                    //releaseUpdateLock(); // JM
                    saveRequired = false;
                    // JM 4-5-2013 check for duplicate persons
                    if (checkPersonDuplicates(proposalDevelopmentFormBean,allKeyStudyPersonnel)) {
                    	throw new CoeusException("Proposal person cannot be both investigator and key person");
                    }
                    else {
                    // JM END
                    throw new CoeusException(response.getMessage());
                    } // JM
                    //System.out.println("error....");
                    //                    CoeusOptionPane.showInfoDialog( response.getMessage() );
                }

                if(functionType==DISPLAY_MODE){
                    if(isSpecialReviewRight){
                        if(isSpecialReviewLocked){
                            releaseUpdateLock();
                        }
                    }
                }

            }

        }
       //Case :#3149 ? Tabbing between fields does not work on others tabs - Start
        if(tbdPnProposal.getSelectedIndex() == 7 && getFunctionType() != DISPLAY_MODE && proposalOther != null){
            proposalOther.setSaveAction(true);
            boolean[] lookUpAvailable = proposalOther.getLookUpAvailable();
            customTable = proposalOther.getTable();
            row = proposalOther.getRow();
            column = proposalOther.getColumn();
            count = 0;
            if(lookUpAvailable[row]){

                customTable.editCellAt(row,column);
                customTable.setRowSelectionInterval(row,row);
                customTable.setColumnSelectionInterval(column,column);

            }else{
                customTable.editCellAt(row,column);
                customTable.setRowSelectionInterval(row,row);
                customTable.setColumnSelectionInterval(column,column);
            }
            customTable.setEnabled(true);
        }  //Case :#3149 - End
    }
    //    private void updateRow(){
    //        String deadlineDate="";
    //        DateUtils dtUtils = new DateUtils();
    //        if(proposalDevelopmentFormBean.getDeadLineDate() != null){
    //            deadlineDate = dtUtils.formatDate(
    //            proposalDevelopmentFormBean.getDeadLineDate().toString(),"dd-MMM-yyyy");
    //        }
    //        //System.out.println("getting pi details");
    //        getPIDetails( proposalDevelopmentFormBean.getInvestigators() );
    //        if( functionType == MODIFY_MODE ) {
    //            //System.out.println("updating basewindow....");
    //            int rowNum = tblProposal.getSelectedRow();
    //
    //            ((DefaultTableModel)tblProposal.getModel()).setValueAt(
    //            proposalDevelopmentFormBean.getProposalNumber(),rowNum,0);
    //            ((DefaultTableModel)tblProposal.getModel()).setValueAt(
    //            proposalDevelopmentFormBean.getProposalTypeDesc(),rowNum,1);
    //
    //            ((DefaultTableModel)tblProposal.getModel()).setValueAt(
    //            statusDescription.get(""+proposalDevelopmentFormBean.getStatusCode()),
    //            rowNum,2);
    //            ((DefaultTableModel)tblProposal.getModel()).setValueAt(
    //            proposalDevelopmentFormBean.getTitle(),rowNum,3);
    //
    //            ((DefaultTableModel)tblProposal.getModel()).setValueAt(
    //            proposalDevelopmentFormBean.getOwnedBy(),rowNum,4);
    //            ((DefaultTableModel)tblProposal.getModel()).setValueAt(
    //            proposalDevelopmentFormBean.getOwnedByDesc(),rowNum,5);
    //            ((DefaultTableModel)tblProposal.getModel()).setValueAt(PIName,rowNum,6);
    //
    //            //get the PI and lead unit name/number
    //            ((DefaultTableModel)tblProposal.getModel()).setValueAt(deadlineDate,rowNum,7);
    //
    //            ((DefaultTableModel)tblProposal.getModel()).setValueAt(
    //            proposalDevelopmentFormBean.getSponsorCode(),rowNum,8);
    //            ((DefaultTableModel)tblProposal.getModel()).setValueAt(
    //            proposalDevelopmentFormBean.getSponsorName(),rowNum,9);
    //            tblProposal.setRowSelectionInterval(rowNum, rowNum );
    //        }else if( functionType == ADD_MODE ) {
    //            Vector newRowData = new Vector();
    //            newRowData.addElement( proposalDevelopmentFormBean.getProposalNumber() );
    //            newRowData.addElement( proposalDevelopmentFormBean.getProposalTypeDesc() );
    //            newRowData.addElement( statusDescription.get(""+proposalDevelopmentFormBean.getStatusCode()) );
    //            newRowData.addElement( proposalDevelopmentFormBean.getTitle() );
    //            newRowData.addElement( proposalDevelopmentFormBean.getOwnedBy() );
    //            newRowData.addElement( proposalDevelopmentFormBean.getOwnedByDesc() );
    //            newRowData.addElement( PIName );
    //            newRowData.addElement( deadlineDate );
    //            newRowData.addElement( proposalDevelopmentFormBean.getSponsorCode() );
    //            newRowData.addElement( proposalDevelopmentFormBean.getSponsorName() );
    //            int lastRow = tblProposal.getRowCount() - 1 ;
    //            //((DefaultTableModel)tblProposal.getModel()).insertRow(0,newRowData);
    //            //((DefaultTableModel)tblProposal.getModel()).addRow(newRowData);
    //            if( lastRow >= 0 ) {
    //                ((DefaultTableModel)tblProposal.getModel()).insertRow(lastRow,newRowData);
    //                tblProposal.setRowSelectionInterval(lastRow+1,lastRow+1);
    //            }
    //
    //        }
    //    }


    /** This method is used to get the Primary Investigator details from the
     * collection of Investigators of this Proposal, which will be used to
     * populate the details in the <CODE>ProposalBaseWindow</CODE>.
     *
     * @param investigators Collection of <CODE>ProposalInvestigatorFormBean</CODE>s of a
     * Proposal.
     */
    public void getPIDetails(Vector investigators ){
        int count = ( investigators == null ? 0 : investigators.size() );

        for (int i=0; i<count;i++) {
            ProposalInvestigatorFormBean investigatorBean
            = (ProposalInvestigatorFormBean) investigators.elementAt(i);
            if (investigatorBean.isPrincipleInvestigatorFlag()) {
                PIName = investigatorBean.getPersonName();
                break;
            }
        }
    }

    private void updateData() {
        saveRequired = false;
        narrativeStatusUpdated = false;
        if( functionType != DISPLAY_MODE ) {
            functionType = MODIFY_MODE;
        }
        if( proposalDevelopmentFormBean != null ) {
            if( functionType != DISPLAY_MODE ) {
                proposalDetail.setFunctionType( MODIFY_MODE );
                proposalOrganization.setFunctionType( MODIFY_MODE );
                proposalMailingInfo.setFunctionType( MODIFY_MODE );
                proposalInvestigator.setFunctionType( MODIFY_MODE );
                if(isParentProposal()){
                    proposalKeyPersonController.setFunctionType( DISPLAY_MODE );
                }else{
                    proposalKeyPersonController.setFunctionType( MODIFY_MODE );
                    proposalKeyPersonController.setSaveRequired(false);
                    proposalKeyPersonController.setDataChanged(false);
                }
                proposalScienceCode.setFunctionType( MODIFY_MODE );
            }
            vecPropYNQQuestionList = proposalDevelopmentFormBean.getPropYNQuestionList();
            htPropYNQExplanation = proposalDevelopmentFormBean.getPropYNQExplanationList();
            vecPropYNQAnswerList = proposalDevelopmentFormBean.getPropYNQAnswerList();
            //System.out.println("in updateData....");
            propPersons = proposalDevelopmentFormBean.getPropPersons();
            proposalDetail.setValues( proposalDevelopmentFormBean );

            proposalDetail.setSaveRequired( false );

            proposalOrganization.setValues( proposalDevelopmentFormBean );
            proposalOrganization.setSaveRequired( false );

            proposalMailingInfo.setValues(proposalDevelopmentFormBean);
            proposalMailingInfo.setSaveRequired(  false );

            // write similar code to investigator also.
            proposalInvestigator.setInvestigatorData(
            proposalDevelopmentFormBean.getInvestigators() );
            proposalInvestigator.setSaveRequired( false );
//proposalKeyPersonController=new ProposalKeyPersonController(proposalID,keyStudyPersonnel,functionType);
            CoeusVector tmp=(CoeusVector)proposalDevelopmentFormBean.getKeyStudyPersonnel();
            proposalKeyPersonController.convertVectorToBean((tmp));
            proposalKeyPersonController.setFormData();
            proposalKeyPersonController.setSaveRequired( false );
            proposalKeyPersonController.setDataChanged(false);

            proposalScienceCode.setProposalScienceCodeData(
            proposalDevelopmentFormBean.getScienceCode() );
            proposalScienceCode.setSaveRequired( false );
            //Added for the  Coeus Enhancement case:#1823 start step:21
            specialReviewData = proposalDevelopmentFormBean.getPropSpecialReviewFormBean();
            proposalSpecialReview.setVecData(specialReviewData);
            proposalSpecialReview.setApprovalTypes(this.propReviewApprovalTypes);
            proposalSpecialReview.setSpecialReviewTypeCodes(this.propSpecialReviewCodes);
            proposalSpecialReview.setVecReplicateData(vecSplRevReplicateData);
            proposalSpecialReview.setValidateVector(this.propValidSpecialReviewList);
            /*
             *Set delete vector to empty vector instead of null vector
             * Fix for bugid #2435
             * Geo on 09-27-2006
             * BEGIN
             */
//            proposalSpecialReview.setVecDeletedSpecialreviewCodes(null);
            proposalSpecialReview.setVecDeletedSpecialreviewCodes(new Vector());
            /*
             * END #2435
             */
            proposalSpecialReview.setCvParameters(this.cvParameters);
            proposalSpecialReview.setFormData();
            proposalSpecialReview.setSaveRequired(false);
            ////End Coeus Enhancement case:#1823 start step:21
            if(proposalYesNoQuestionsForm != null){
                proposalYesNoQuestionsForm.setVecAnswerList(proposalDevelopmentFormBean.getPropYNQAnswerList());
                //proposalYesNoQuestionsForm.setVecAnswerList(vecPropYNQAnswerList);
                proposalYesNoQuestionsForm.setVecQuestionList(vecPropYNQQuestionList);
                proposalYesNoQuestionsForm.setExplanation(htPropYNQExplanation);
                // Added for COEUSQA-2881 : Last Update User and Timestamp changes - Start
                proposalYesNoQuestionsForm.setSaveRequired(false);
                // Added for COEUSQA-2881 : Last Update User and Timestamp changes - End
            }

            if ( proposalOther != null ) {

                proposalOther.setPersonColumnValues( proposalDevelopmentFormBean.getOthers() );
                if( functionType != DISPLAY_MODE ) {
                    proposalOther.setFunctionType( MODIFY_MODE );
                }
                proposalOther.setSaveRequired( false );
            }
            if( rolesForm != null ) {
                rolesForm.setSaveRequired( false );
            }
            tempUserRoles = null;
            narrativeUserRights = null;
            //Added by tarique for proposal hierarchy
            create.setVisible(true);
            join.setVisible(true);
            remove.setVisible(true);
            enableDisableMenuItems();
            //Added by tarique end here
            //propUserRoles = proposalDevelopmentFormBean.getUserRolesInfoBean();

        }
    }

    /** Getter for property unitName.
     * @return Value of property unitName.
     */
    public java.lang.String getUnitName() {
        return unitName;
    }

    /** Setter for property unitName.
     * @param unitName New value of property unitName.
     */
    public void setUnitName(java.lang.String unitName) {
        this.unitName = unitName;
    }

    /** Getter for property unitNumber.
     * @return Value of property unitNumber.
     */
    public java.lang.String getUnitNumber() {
        return unitNumber;
    }

    /** Setter for property unitNumber.
     * @param unitNumber New value of property unitNumber.
     */
    public void setUnitNumber(java.lang.String unitNumber) {
        this.unitNumber = unitNumber;
    }



    /**
     * This method is used to add custom tool bar and menu bar to the application
     *  when the internal frame is activated.
     * @param e  InternalFrameEvent which delegates the event. */
    public void internalFrameActivated(InternalFrameEvent e) {
        super.internalFrameActivated( e );
        // recreate the menu bar
        mdiForm.getCoeusMenuBar().add(getActionMenu(),2);
        mdiForm.getCoeusMenuBar().remove( 0 );
        mdiForm.getCoeusMenuBar().add( getFileMenu(), 0 );
        mdiForm.getCoeusMenuBar().validate();
        //        enableDisableMenuItems();   //Added by Vyjayanthi

    }

    /**
     * This method is used to remove Action menu when the internal frame is
     * closed.
     * @param e  InternalFrameEvent which delegates the event. */
    /*public void internalFrameClosed(InternalFrameEvent e) {
        super.internalFrameClosed( e );
        mdiForm.getCoeusMenuBar().remove( 0 );
        mdiForm.getCoeusMenuBar().add( fileMenu.getMenu(), 0);
        mdiForm.getCoeusMenuBar().validate();
        mdiForm.getCoeusMenuBar().revalidate();
    }*/

    /**
     * This method is used to remove the Action menu when the internal frame is
     * deactivated.
     * @param e  InternalFrameEvent which delegates the event.
     */
    public void internalFrameDeactivated(InternalFrameEvent e) {
        mdiForm.getCoeusMenuBar().remove( 2 );
        super.internalFrameDeactivated( e );
        mdiForm.getCoeusMenuBar().remove( 0 );
        mdiForm.getCoeusMenuBar().add( fileMenu.getMenu(), 0 );
        mdiForm.getCoeusMenuBar().validate();
        mdiForm.getCoeusMenuBar().revalidate();
    }

    //    /** Setter for property displaySheetCount.
    //     * @param displaySheetCount New value of property displaySheetCount.
    //     */
    //    public void reduceDisplaySheetCount() {
    //        if (functionType == 'D') {
    //            ProposalBaseWindow.displaySheetCount--;
    //        }
    //    }

    /**
     * This method is invoked when the user clicks Modify in the Edit menu
     * of Proposal module
     *
     * @return boolean which indicates whether the validation was success or failure.
     */
    private boolean validateModifyProposal() throws Exception {
         boolean modifyProposal = false;
        int statusCode;
        try {
            statusCode = proposalDevelopmentFormBean.getCreationStatusCode();

            switch (statusCode) {
                case CoeusGuiConstants.PROPOSAL_APPROVAL_IN_PROGRESS:
                    throw new Exception("proposal_DtlForm_exceptionCode.7105");
                    //break;
                case CoeusGuiConstants.PROPOSAL_APPROVED:
                    throw new Exception("proposal_DtlForm_exceptionCode.7106");
                    //break;
                case CoeusGuiConstants.PROPOSAL_SUBMITTED:
                    throw new Exception("proposal_DtlForm_exceptionCode.7107");
                case CoeusGuiConstants.PROPOSAL_POST_SUB_APPROVAL:
                    throw new Exception("proposal_DtlForm_exceptionCode.7114");
                case CoeusGuiConstants.PROPOSAL_POST_SUB_REJECTION:
                    throw new Exception("proposal_DtlForm_exceptionCode.7115");
            }

            if (hasModifyProposalRights()) {
                modifyProposal = true;
            } else {
                throw new Exception("proposal_DtlForm_exceptionCode.7108");
            }
        } catch( Exception e ) {
            releaseUpdateLock();
            throw new Exception( e.getMessage() );
        }
        return modifyProposal;
    }


    //Modified for bug id 1856 step 3 : start
    /**
     * This method is invoked when the user clicks Modify in the Edit menu
     * of Proposal module. Checks whether the User has rights to Modify Proposal
     *
     * @return boolean. true indicates has rights and false indicates no rights.
     */
    //Modified by shiji for Right checking - step 4 : start

    private boolean hasModifyProposalRights(){

        boolean hasRights = false;
        String connectTo = "";
        //Coeus enhancement Case #1828 :step 1 : start
        if(unitNumber == null) {
            unitNumber = proposalDevelopmentFormBean.getOwnedBy();
        }
        //Coeus enhancement Case #1828 :step 1 : end

        //check if user has MODIFY_ANY_PROPOSAL right at the lead unit of the Proposal.
        Vector vecFnParams = new Vector();
        RequesterBean request = new RequesterBean();
        AppletServletCommunicator comm
        = new AppletServletCommunicator(connectTo, request);
        ResponderBean response = null;
        vecFnParams.addElement(mdiForm.getUserName());
        vecFnParams.addElement(unitNumber);
        vecFnParams.addElement(MODIFY_ANY_PROPOSAL_RIGHT);
            connectTo = CoeusGuiConstants.CONNECTION_URL + "/ProposalMaintenanceServlet";
            request.setDataObjects(vecFnParams);
            request.setFunctionType('b');
            comm.setConnectTo(connectTo);
            comm.setRequest(request);
            comm.send();
            response = comm.getResponse();
            if (response!=null){
                if (response.isSuccessfulResponse()){
                    hasRights = ((Boolean) response.getDataObject()).booleanValue();
                }
            }
        //Check if the User has MODIFY_PROPOSAL right for the particular proposal.
        if(!hasRights) {
            connectTo = CoeusGuiConstants.CONNECTION_URL + "/ProposalMaintenanceServlet";
            vecFnParams.setElementAt(proposalID,1);
            vecFnParams.setElementAt(MODIFY_PROPOSAL_RIGHT,2);
            request.setDataObjects(vecFnParams);
            request.setFunctionType('U');
            comm.setConnectTo(connectTo);
            comm.setRequest(request);
            comm.send();
            response = comm.getResponse();

            if (response!=null){
                if (response.isSuccessfulResponse()){
                    hasRights  = ((Boolean) response.getDataObject()).booleanValue();
//                    int right = ((Integer) response.getDataObject()).intValue();
//                    if (right == 1) {
//                        hasRights = true;
//                    }
                }
            }
        }
        // check if the User has MODIFY_NARRATIVE for this particular proposal
        if (!hasRights) {
            vecFnParams.setElementAt(MODIFY_NARRATIVE_RIGHT, 2);
            request.setDataObjects(vecFnParams);
            comm.setRequest(request);
            comm.send();
            response = comm.getResponse();

            if (response!=null){
                if (response.isSuccessfulResponse()){
                    hasRights  = ((Boolean) response.getDataObject()).booleanValue();
//                    int right = ((Integer) response.getDataObject()).intValue();
//                    if (right == 1) {
//                        hasRights = true;
//                    }
                }
            }
        }
        // check if the User has MODIFY_BUDGET for this particular proposal
        if (!hasRights) {

            vecFnParams.setElementAt(MODIFY_BUDGET_RIGHT, 2);
            request.setDataObjects(vecFnParams);
            comm.setRequest(request);
            comm.send();
            response = comm.getResponse();

            if (response!=null){
                if (response.isSuccessfulResponse()){
                    hasRights  = ((Boolean) response.getDataObject()).booleanValue();
//                    int right = ((Integer) response.getDataObject()).intValue();
//                    if (right == 1) {
//                        hasRights = true;
//                    }
                }
            }
        }

        return hasRights;
    }

    //bug id 1856 step 3 : end

   /* private boolean hasModifyProposalRights(){

        boolean hasRights = false;
        //Coeus enhancement Case #1828 :step 1 : start
        if(unitNumber == null) {
            unitNumber = proposalDevelopmentFormBean.getOwnedBy();
        }
        //Coeus enhancement Case #1828 :step 1 : end
        //String proposalNumber;
        Vector vecFnParams = new Vector();
        String topLevelUnit=null;
        String connectTo = CoeusGuiConstants.CONNECTION_URL + "/coeusFunctionsServlet";
        RequesterBean request = new RequesterBean();
        request.setId(unitNumber);
        //request.setUserName(mdiForm.getUserName());
        request.setDataObject("FN_GET_CAMPUS_FOR_DEPT");
        AppletServletCommunicator comm
        = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();

        if (response!=null){
            if (response.isSuccessfulResponse()){
                topLevelUnit = (String)response.getDataObject();
            }
        }

        if(!topLevelUnit.equals(null)) {
            connectTo = CoeusGuiConstants.CONNECTION_URL + "/ProposalMaintenanceServlet";
            vecFnParams.addElement(mdiForm.getUserName());
            vecFnParams.addElement(topLevelUnit);
            vecFnParams.addElement(MODIFY_ANY_PROPOSAL_RIGHT);
            request.setDataObjects(vecFnParams);
            request.setFunctionType('b');
            comm.setConnectTo(connectTo);
            comm.setRequest(request);
            comm.send();
            response = comm.getResponse();

            if (response!=null){
                if (response.isSuccessfulResponse()){
                    hasRights = ((Boolean) response.getDataObject()).booleanValue();
                }
            }
        }
        if(!hasRights) {
            connectTo = CoeusGuiConstants.CONNECTION_URL + "/ProposalMaintenanceServlet";
            vecFnParams.setElementAt(unitNumber, 1);
            request.setDataObjects(vecFnParams);
            comm.setConnectTo(connectTo);
            comm.setRequest(request);
            comm.send();
            response = comm.getResponse();
            if (response!=null){
                if (response.isSuccessfulResponse()){
                    hasRights = ((Boolean) response.getDataObject()).booleanValue();
                }
            }
        }
        if(!hasRights) {
            connectTo = CoeusGuiConstants.CONNECTION_URL + "/ProposalMaintenanceServlet";
            // RequesterBean request = new RequesterBean();
            vecFnParams.setElementAt(proposalID,1);
            vecFnParams.setElementAt(MODIFY_PROPOSAL_RIGHT,2);
            request.setDataObjects(vecFnParams);
            request.setFunctionType('U');
            //AppletServletCommunicator comm
            // = new AppletServletCommunicator(connectTo, request);
            comm.setConnectTo(connectTo);
            comm.setRequest(request);
            comm.send();
            response = comm.getResponse();

            if (response!=null){
                if (response.isSuccessfulResponse()){
                    int right = ((Integer) response.getDataObject()).intValue();
                    if (right == 1) {
                        hasRights = true;
                    }
                }
            }
        }
        if (!hasRights) {

            vecFnParams.setElementAt(MODIFY_NARRATIVE_RIGHT, 2);
            request.setDataObjects(vecFnParams);
            comm.setRequest(request);
            comm.send();
            response = comm.getResponse();

            if (response!=null){
                if (response.isSuccessfulResponse()){
                    int right = ((Integer) response.getDataObject()).intValue();
                    if (right == 1) {
                        hasRights = true;
                    }
                }
            }
        }

        if (!hasRights) {

            vecFnParams.setElementAt(MODIFY_BUDGET_RIGHT, 2);
            request.setDataObjects(vecFnParams);
            comm.setRequest(request);
            comm.send();
            response = comm.getResponse();

            if (response!=null){
                if (response.isSuccessfulResponse()){
                    int right = ((Integer) response.getDataObject()).intValue();
                    if (right == 1) {
                        hasRights = true;
                    }
                }
            }
        }

        return hasRights;
    }*/
    //right checking - step 4 :end

    /** This methods used get the description for the creationStatusCode
     * in this method hashtable is created to store the description for
     * status code in PB this have been done in data window
     */
    private void getStatusDescription(){
        statusDescription = new Hashtable();
        statusDescription.put("1","In Progress");
        statusDescription.put("2","Approval In Progress");
        statusDescription.put("3","Rejected");
        statusDescription.put("4","Approved");
        statusDescription.put("5","Submitted");
    }

    /**
     * This method is used to send the selected person in investigator or key study
     * personnel to add to OSP$EPS_PROP_PERSON table.
     *
     * @param personId String representing either Person Id / Rolodex Id.
     * @param person boolean value which specifies whether the given id belongs
     * to person / rolodex.
     */
    public void addPropPerson( String personId , boolean person ) {
        boolean found = false;
        if( propPersons == null ) {
            propPersons = new Vector();
        }

        if( propPersons.size() > 0 ) {
            int perSize = propPersons.size();
            ProposalPersonFormBean oldPersonBean;
            for( int perIndx = 0 ; perIndx < perSize ; perIndx++ ) {
                oldPersonBean = ( ProposalPersonFormBean ) propPersons.elementAt( perIndx );
                if( oldPersonBean.getPersonId().equals( personId ) ) {
                    String acType = oldPersonBean.getAcType();
                    //Bug fix for case #2071 by tarique start 1
                    if(person){
                        oldPersonBean.setPerson(person);
                    }
                    //Bug fix for case #2071 by tarique end 1
                    // dont set any acType if the existing record is deleted and
                    // added again before saving.
                    if( acType != null && acType.equals( DELETE_RECORD ) ) {
                        oldPersonBean.setAcType( null );
                        oldPersonBean.setPerson(person);
                        propPersons.set( perIndx, oldPersonBean );
                    }
                    found = true;
                    break;
                }
            }
        }
        if( !found ) {
            ProposalPersonFormBean personBean = new ProposalPersonFormBean();
            personBean.setProposalNumber( proposalID );
            personBean.setPersonId( personId );
            personBean.setPerson( person );
            personBean.setAcType( INSERT_RECORD );
            propPersons.addElement( personBean );
        }
    }

    /**
     * Method used to delete the selected person id from OSP$EPS_PROP_PERSON table
     * if it is not used in both PropoalInvestigatorForm and ProposalKeyStudyPersonnelForm
     *
     * @param personId String representing person/ rolodex id which is to be deleted.
     * @param fromInv boolean which specifies from which screen (investigator/ keystudy personnel)
     * the delete action is invoked , so that to check for existence in other screen.
     *
     */

    public void deletePropPerson( String personId, boolean fromInv ) {
        if( propPersons == null ) {
            propPersons = new Vector();
        }
        //first check whether the person to be deleted is not there both in
        // investigator form and key study personnel form. if present dont delete
        if( fromInv ) {
//            if( proposalKeyPersonController..isPersonPresent( personId ) ){
//                return;
//            }
//        }else {
            if( proposalInvestigator.isPersonPresent( personId ) ){
                return;
            }

        }
        if( propPersons.size() > 0 ) {
            int perSize = propPersons.size();
            ProposalPersonFormBean oldPersonBean;
            for( int perIndx = 0 ; perIndx < perSize ; perIndx++ ) {
                oldPersonBean = ( ProposalPersonFormBean ) propPersons.elementAt( perIndx );
                if( oldPersonBean.getPersonId().equals( personId ) ) {
                    String acType = oldPersonBean.getAcType();
                    if( acType != null && acType.equals( INSERT_RECORD ) ) {
                        // if the person to be deleted is newly added one , dont
                        // send to server
                        propPersons.removeElementAt( perIndx );
                    }else {
                        oldPersonBean.setAcType( DELETE_RECORD );
                        propPersons.set( perIndx, oldPersonBean );
                    }
                    break;
                }
            }
        }

    }

    /**
     * Method used to check whether the selected person to certify is a valid
     * person in OSP$EPS_PROP_PERSON table.
     *
     * @param personId String representing the person /rolodex id to verify.
     * @return boolean true if the given person id is valid else false.
     */
    public boolean isProposalPerson( String personId ) {
        if ( propPersons == null ) {
            return false;
        }
        if( propPersons.size() > 0 ) {
            int perSize = propPersons.size();
            ProposalPersonFormBean oldPersonBean;
            for( int perIndx = 0 ; perIndx < perSize ; perIndx++ ) {
                oldPersonBean = ( ProposalPersonFormBean ) propPersons.elementAt( perIndx );
                if( oldPersonBean.getPersonId().equals( personId ) ) {
                    return true;
                }
            }
        }
        return false;
    }

    /** Getter for property propPersons.
     * @return Value of property propPersons.
     */
    public java.util.Vector getPropPersons() {
        return propPersons;
    }

    /** Setter for property propPersons.
     * @param propPersons New value of property propPersons.
     */
    public void setPropPersons(java.util.Vector propPersons) {
        this.propPersons = propPersons;
    }

// commented this method during PMD check as it is an unused private method
//    private boolean checkChildWindows() throws Exception{
//        CoeusInternalFrame frame;
//        frame = mdiForm.getFrame(CoeusGuiConstants.PROPOSAL_NARRATIVE_FRAME_TITLE,proposalID);
//        if( mdiForm.getFrame(CoeusGuiConstants.PROPOSAL_NARRATIVE_FRAME_TITLE,proposalID) != null ) {
//            throw new Exception(coeusMessageResources.parseMessageKey(
//            "proposal_DtlForm_exceptionCode.7109"));
//        }else if( mdiForm.getFrame(CoeusGuiConstants.PROPOSAL_ABSTRACTS_FRAME_TITLE,proposalID) != null ) {
//            throw new Exception(coeusMessageResources.parseMessageKey(
//            "proposal_DtlForm_exceptionCode.7110"));
//        }else if( mdiForm.getFrame(CoeusGuiConstants.PROP_PERSON_BIOGRAPHY_FRAME_TITLE,proposalID) != null ) {
//            throw new Exception(coeusMessageResources.parseMessageKey(
//            "proposal_DtlForm_exceptionCode.7111"));
//        }
//        return true;
//    }

    public void saveAsActiveSheet() {
    }

    public void setNarrativeStatus(char newNarrativeStatus){
        this.narrativeStatusUpdated = true;
        this.narrativeStatus = newNarrativeStatus;
        this.saveRequired = true;
    }

    public void update(Observable o, Object arg) {
        if ( arg != null ){
            if( arg instanceof ProposalDevelopmentFormBean ) {
                proposalDevelopmentFormBean = ( ProposalDevelopmentFormBean )arg;
                observable.setFunctionType( functionType );
                observable.notifyObservers( proposalDevelopmentFormBean );
                updateData();
            }if(arg instanceof OpportunityInfoBean){
                OpportunityInfoBean oppBean = (OpportunityInfoBean)arg;
                proposalDevelopmentFormBean.setCfdaNumber(oppBean.getCfdaNumber());
                proposalDevelopmentFormBean.setProgramAnnouncementNumber(oppBean.getOpportunityId());
                proposalDevelopmentFormBean.setProgramAnnouncementTitle(oppBean.getOpportunityTitle());
                proposalDevelopmentFormBean.setS2sOppSelected(oppBean.getOpportunityId()!=null);
		proposalDetail.setValues( proposalDevelopmentFormBean );
                proposalDetail.setSaveRequired(false);
                this.saveRequired = false;
            }if(arg instanceof SubmissionDetailInfoBean){
                SubmissionDetailInfoBean subBean = (SubmissionDetailInfoBean)arg;
                proposalDevelopmentFormBean.setSponsorProposalNumber(subBean.getAgencyTrackingNumber());
		proposalDetail.setValues( proposalDevelopmentFormBean );
                proposalDetail.setSaveRequired(false);
                this.saveRequired = false;
            }
        }
    }

    /** Getter for property narrativeUserRights.
     * @return Value of property narrativeUserRights.
     *
     */
    public edu.mit.coeus.utils.CoeusVector getNarrativeUserRights() {
        return narrativeUserRights;
    }

    /** Setter for property narrativeUserRights.
     * @param narrativeUserRights New value of property narrativeUserRights.
     *
     */
    public void setNarrativeUserRights(edu.mit.coeus.utils.CoeusVector narrativeUserRights) {
        this.narrativeUserRights = narrativeUserRights;
    }

    private void showInboxDetails(){
        InboxDetailForm inboxDtlForm = null;
        try{
            if( ( inboxDtlForm = (InboxDetailForm)mdiForm.getFrame("Inbox" ))!= null ){
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

    // Added by chandra. To fix the bug #872, #976.
    // If the narrative and budget status are updated when the parent window
    // is opened then update the values immediately. else update the data
    //to the data base.
    // 30th June 2004

    public void setNarrativeStatusCode(char status){
        if(status=='C'){
            proposalDetail.updateNarrativeStatus('C');
        }else if(status=='I'){
            proposalDetail.updateNarrativeStatus('I');
        }else if(status=='N'){
            proposalDetail.updateNarrativeStatus('N');
        }
    }

    public void setBudgetStatusCode(String status){
        if(status.equals("C")){
            proposalDetail.updateBudgetStatus("C");
        }else if(status.equals("I")){
            proposalDetail.updateBudgetStatus("I");
        }else if(status.equals("N")){
            proposalDetail.updateBudgetStatus("N");
        }
    }
    /** Method to set Proposal Hiearchy Icon as well as Parent Proposal Icon
        Added By Tarique start
     */
    public void setPropHierarchyStatus(){
        if(isHierarchy()){
            proposalDetail.lblPropHiearchyIcon.setIcon(null);
            if(isParentProposal()){
                proposalDetail.lblParentIcon.setIcon(
                     new ImageIcon(getClass().getClassLoader()
                        .getResource(CoeusGuiConstants.PARENT_PROP_HIE_ICON)));
            }else{
                proposalDetail.lblParentIcon.setIcon(
                     new ImageIcon(getClass().getClassLoader()
                        .getResource(CoeusGuiConstants.CHILD_PROP_HIE_ICON)));
                btnSubmitProposal.setEnabled(false);
                submitApproval.setEnabled(false);
                btnApproveProposal.setEnabled(false);
                approve.setEnabled(false);
                submitSponsor.setEnabled(false);
                showRouting.setEnabled(false);
                validationChecks.setEnabled(false);
                approvalMap.setEnabled(false);
                submitGrantsGov.setEnabled(false);
                btnApprovalMap.setEnabled(false);
            }
        }else{
            proposalDetail.lblParentIcon.setIcon(null);
            proposalDetail.lblPropHiearchyIcon.setIcon(
                new ImageIcon(getClass().getClassLoader()
                    .getResource(CoeusGuiConstants.NONE_ICON)));
            remove.setVisible(false);
            btnSyncAll.setVisible(false);
            mnuItmSyncAll.setVisible(false);
            //Added for case id:3183 - start
            if(proposalInvestigator!=null){
		proposalInvestigator.setInHierarchy(false);
            }
            //Added for case id:3183 - end
        }
    }
    //Added by tarique end here
    /**
     * Getter for property parentProposal.
     * @return Value of property parentProposal.
     */
    public boolean isParentProposal() {
        return parentProposal;
        //return false;
    }

    /**
     * Setter for property parentProposal.
     * @param parentProposal New value of property parentProposal.
     */
    public void setParentProposal(boolean parentProposal) {
        this.parentProposal = parentProposal;
    }

    /**
     * Getter for property hierarchy.
     * @return Value of property hierarchy.
     */
    public boolean isHierarchy() {
        return hierarchy;
        //return false;
    }

    /**
     * Setter for property hierarchy.
     * @param hierarchy New value of property hierarchy.
     */
    public void setHierarchy(boolean hierarchy) {
        this.hierarchy = hierarchy;
    }

    /**
     * Getter for property parentPropNo.
     * @return Value of property parentPropNo.
     */
    public java.lang.String getParentPropNo() {
        return parentPropNo;
    }

    /**
     * Setter for property parentPropNo.
     * @param parentPropNo New value of property parentPropNo.
     */
    public void setParentPropNo(java.lang.String parentPropNo) {
        this.parentPropNo = parentPropNo;
    }
    /** Added for case #2371 to show the current and pending details start 4
     */
    private void showCurrentAndPendingDetails() throws CoeusException{
        InvestigaorListForm investigaorListForm = new InvestigaorListForm(mdiForm, null);
        investigaorListForm.setOpenFromPropDev(true);
        Vector vecInvData = proposalInvestigator.getFormData();
        Vector vecKeyData = new Vector();
        //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - Start
        //To check whether the data for key person is existing
        if(allKeyStudyPersonnel != null && allKeyStudyPersonnel.size()>0){
            vecKeyData = allKeyStudyPersonnel;
        }
        if(vecKeyData != null && vecKeyData.size() > 0){
            vecInvData.addAll(vecKeyData);
        }
        Vector vecAllData = removeDuplicateData(vecInvData);
        Object [] bothInvestigatorAndKeyPerson = (Object [])vecAllData.get(1);
        Vector vecCurrentData = (Vector)vecAllData.get(0);
        String personName = CoeusGuiConstants.EMPTY_STRING;
        ProposalInvestigatorFormBean bean = null;
        ProposalKeyPersonFormBean keyPersonDetails = null;
        int counter = bothInvestigatorAndKeyPerson.length;
        for(Object currentData : vecCurrentData){
            if(currentData instanceof ProposalInvestigatorFormBean){
                bean = (ProposalInvestigatorFormBean)currentData;
                personName = bean.getPersonName();
            }else if(currentData instanceof ProposalKeyPersonFormBean){
                keyPersonDetails = (ProposalKeyPersonFormBean)currentData;
                personName = keyPersonDetails.getPersonName();
            }
            for(int start=0; start<counter;start++){
                if(bothInvestigatorAndKeyPerson[start].equals(personName)){
                    if(bean!=null){
                      bean.setBothPIAndKeyPerson(true);
                    }else if(keyPersonDetails!=null){
                        keyPersonDetails.setBothPIAndKeyPerson(true);
                    }
                }
            }
            bean = null;
            keyPersonDetails = null;
        }

        vecInvData = vecCurrentData;
        //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - End
        if(vecInvData == null){
            vecInvData = new Vector();
        }
        investigaorListForm.setFormData(vecInvData);
        investigaorListForm.display();

    }
    //Added for case #2371 to show the current and pending details end 4
     /** Added for questionnaire start 3
     */
    private void showQuestionsWindow() throws CoeusException, PropertyVetoException{
        String title = CoeusGuiConstants.PROPOSAL_QUESTIONNAIRE;
        QuestionnaireAnswersBaseForm listWindow = null;
        if( ( listWindow = (QuestionnaireAnswersBaseForm)mdiForm.getFrame(
         title))!= null ){
             if( listWindow.isIcon() ){
                 listWindow.setIcon(false);
             }
             listWindow.setSelected( true );
             return;
         }
        QuestionnaireAnswerHeaderBean questionnaireModuleObject = new QuestionnaireAnswerHeaderBean();
        questionnaireModuleObject.setModuleItemCode(3);
        questionnaireModuleObject.setModuleItemKey(proposalID);
        questionnaireModuleObject.setModuleItemDescription(CoeusGuiConstants.PROPOSAL_MODULE);
        questionnaireModuleObject.setModuleSubItemCode(0);
        questionnaireModuleObject.setModuleSubItemKey("0");//case 2158
        questionnaireModuleObject.setModuleSubItemDescription(CoeusGuiConstants.PROPOSAL_MODULE);
        QuestionnaireAnswersBaseController questionnaireAnswersBaseController
              = new QuestionnaireAnswersBaseController(title, mdiForm);
        questionnaireAnswersBaseController.setFormData(questionnaireModuleObject);
        questionnaireAnswersBaseController.setFunctionType(getFunctionType());

        CoeusVector cvQuestionnaireData
            = (CoeusVector)questionnaireAnswersBaseController.getFormData();
        if(cvQuestionnaireData == null || cvQuestionnaireData.isEmpty()) {
            cvQuestionnaireData = null;
            CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey("questions_exceptionCode.1007"));
            questionnaireAnswersBaseController.cleanUp();
            return;
        }
        questionnaireAnswersBaseController.setTabData();
        questionnaireAnswersBaseController.display();
        questionnaireModuleObject = null;
        questionnaireAnswersBaseController = null;

    }

    /**
     * Getter for property baseSearch.
     * @return Value of property baseSearch.
     */
    public edu.mit.coeus.search.gui.ProposalSearch getBaseSearch() {
        return baseSearch;
    }

    /**
     * Setter for property baseSearch.
     * @param baseSearch New value of property baseSearch.
     */
    public void setBaseSearch(edu.mit.coeus.search.gui.ProposalSearch baseSearch) {
        this.baseSearch = baseSearch;
    }

     /** Get the proposal number based on the search index column
     */
    private String getSelectedProposalNumber(int row, String colValue) throws Exception{
        int column = getColumnIndexValue(getBaseSearch(), colValue);
        return (String)tblProposal.getValueAt(row, column);
    }

    /** Get the column index for the selected row, Get the data based on the
     *Display bean data. This will be required for previous and next action
     */
    private int getColumnIndexValue( ProposalSearch searchWindow, String columnName) {
        int columnIndex = -1;
        try{
            SearchColumnIndex searchIndex = new SearchColumnIndex();
            columnIndex = searchIndex.getSearchColumnIndex(searchWindow, columnName);
            searchIndex = null;
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return columnIndex;

    }

    //Added for questionnaire end 3
// Added by chandra - End 30th June 2004
     //Added for case id 3183 - start
    public boolean canLinkToHierarchy(String parentProposalNumber)
    throws CoeusException{
        boolean canLinkToHierarchy = false;
        String connectTo = CoeusGuiConstants.CONNECTION_URL +HIERARCHY_SERVLET;
        Vector serverDataObjects = new Vector();

        serverDataObjects.add(proposalID);
        serverDataObjects.add(parentProposalNumber);
        RequesterBean request = new RequesterBean();
        request.setFunctionType(CAN_LINK_TO_HIERARCHY);
        request.setDataObjects(serverDataObjects);

        AppletServletCommunicator comm
        = new AppletServletCommunicator( connectTo, request );
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response == null) {
            response = new ResponderBean();
            response.setResponseStatus(false);
        }
        if (response.isSuccessfulResponse()) {
            serverDataObjects = response.getDataObjects();
            if(serverDataObjects!=null){
                canLinkToHierarchy = ((Boolean)serverDataObjects.get(0)).booleanValue();
                if(!canLinkToHierarchy){
                    String narrativeDescription = (String)serverDataObjects.get(1);
                    String errorMessage = coeusMessageResources.parseMessageKey("propHierarchy_exceptionCode.1019")+
                    " "+ narrativeDescription+" " + coeusMessageResources.parseMessageKey("propHierarchy_exceptionCode.1020") ;
                    int selection = CoeusOptionPane.showQuestionDialog(errorMessage, CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_YES);
                    if(selection == CoeusOptionPane.SELECTION_YES) {
                        canLinkToHierarchy = true;
                    }else if(selection == CoeusOptionPane.SELECTION_NO) {
                        canLinkToHierarchy = false;
                    }
                }
            }
        }else{
            throw new CoeusException(response.getMessage());
        }
        return canLinkToHierarchy;
    }
    //Added for case id 3183 - end
    //Added for case#3230 - Display of Proposal Role Form based on rights - start
    /**
     * This method checks whether the Proposal Role Form can be opended in edit mode
     * when the function type is 'M'. It checks two rights viz, MAINTAIN_PROPOSAL_ACCESS
     * and MODIFY_ANY_PROPOSAL. The first right is checked at Proposal level (ie User level),
     * if its not available, then the second right is checked at unit level.
     * If either of the rights is available, then the form is shown in edit mode
     * @return boolean canModifyProposalRole
     */
    private boolean canModifyProposalRole(){
        boolean canModifyProposalRole = false;
        String connectTo = "";
        if(unitNumber == null) {
            unitNumber = proposalDevelopmentFormBean.getOwnedBy();
        }
        Vector vecFnParams = new Vector();
        RequesterBean request = new RequesterBean();
        AppletServletCommunicator comm
        = new AppletServletCommunicator(connectTo, request);
        ResponderBean response = null;

        //The first right is checked at Proposal level (ie User level),
        vecFnParams.addElement(mdiForm.getUserName());
        vecFnParams.addElement(proposalDevelopmentFormBean.getProposalNumber());
        vecFnParams.addElement(MAINTAIN_PROPOSAL_ACCESS);
        connectTo = CoeusGuiConstants.CONNECTION_URL + "/ProposalMaintenanceServlet";
        request.setDataObjects(vecFnParams);
        request.setFunctionType(USER_HAS_RIGHT);
        comm.setConnectTo(connectTo);
        comm.setRequest(request);
        comm.send();
        response = comm.getResponse();
        if (response != null){
            if (response.isSuccessfulResponse()){
                canModifyProposalRole = ((Boolean)response.getDataObject()).booleanValue();
            }
        }

        //If its not available, then the second right is checked at unit level.
        if(!canModifyProposalRole){
            vecFnParams.removeAllElements();
            vecFnParams.addElement(mdiForm.getUserName());
            vecFnParams.addElement(unitNumber);
            vecFnParams.addElement(MODIFY_ANY_PROPOSAL_RIGHT);
            connectTo = CoeusGuiConstants.CONNECTION_URL + "/ProposalMaintenanceServlet";
            request.setDataObjects(vecFnParams);
            request.setFunctionType(CHECK_USER_HAS_RIGHT);
            comm.setConnectTo(connectTo);
            comm.setRequest(request);
            comm.send();
            response = comm.getResponse();
            if (response != null){
                if (response.isSuccessfulResponse()){
                    canModifyProposalRole = ((Boolean)response.getDataObject()).booleanValue();
                }
            }
        }
        return canModifyProposalRole;
    }
    //Added for case#3230 - Display of Proposal Role Form based on rights - end

    //JIRA COEUSDEV-61 - START - 2
    /**
     * @return the opportunityInfoBeanForGG
     */
    public OpportunityInfoBean getOpportunityInfoBeanForGG() {
        return opportunityInfoBeanForGG;
    }

    /**
     * @param opportunityInfoBeanForGG the opportunityInfoBeanForGG to set
     */
    public void setOpportunityInfoBeanForGG(OpportunityInfoBean opportunityInfoBeanForGG) {
        this.opportunityInfoBeanForGG = opportunityInfoBeanForGG;
        proposalDevelopmentFormBean.setCfdaNumber(opportunityInfoBeanForGG.getCfdaNumber());
        proposalDevelopmentFormBean.setProgramAnnouncementNumber(opportunityInfoBeanForGG.getOpportunityId());
        proposalDetail.txtCfdaNo.setText(opportunityInfoBeanForGG.getCfdaNumber());
        proposalDetail.txtProgramNo.setText(opportunityInfoBeanForGG.getOpportunityId());
        proposalDevelopmentFormBean.setOpportunityInfoBean(opportunityInfoBeanForGG);
    }
    //JIRA COEUSDEV-61 - END - 2

    //Method to fetch the final version of the proposal to perform budget validations
    // Returns 1000 if there is no budget, 1001 if there is no final version selected.
    //Added with case 2158
    private int checkBudgetFinalVersion(boolean validateFinal) throws CoeusException{

        int finalVersion = 0;
        RequesterBean request = new RequesterBean();
        request.setFunctionType(CHECK_BUDGET_VERSION);
        request.setDataObject(proposalID);
        String connectTo = CoeusGuiConstants.CONNECTION_URL + "/BudgetMaintenanceServlet";
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        int errCode = 0;
        if (response != null){
            if (response.isSuccessfulResponse()){
                errCode = ((Integer) response.getDataObject()).intValue();
            }
        }
        //if no hardstop for final validation is required, budget version will go as 0
        //Then while evaluating the rule, max version of budget will be taken for validation.
        if(errCode==1001 ){
            if(validateFinal){
                throw new CoeusException(coeusMessageResources.parseMessageKey(ERRKEY_SELECT_FINAL_BUDGET));
            }
        }else if(errCode==1000){
            CoeusOptionPane.showWarningDialog("Proposal " + proposalID + " " + coeusMessageResources.parseMessageKey(NO_BUDGET));
        }else{
            finalVersion = errCode;
        }
        return finalVersion;
    }
    //2158 End
   // 4272: Maintain History of Questionnaires - Start
//    private int validateQuestionnaireCompleted() {
    private Vector validateQuestionnaireCompleted() {
//        int success = 0;
        Vector vecUnfilledQnr = new Vector();
        RequesterBean request = new RequesterBean();
        request.setFunctionType(CHECK_QUESTIONNAIRE_COMPLETED);
        CoeusVector proposalData = new CoeusVector();
        proposalData.add(0, proposalID);
        proposalData.add(1, new Integer(3));
        proposalData.add(2, "0");
        proposalData.add(3, new Integer(0));
        request.setDataObjects(proposalData);
        String connectTo = CoeusGuiConstants.CONNECTION_URL + "/questionnaireServlet";
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();

        if (response != null){
            if (response.isSuccessfulResponse()){
//                success = ((Integer) response.getDataObject()).intValue();
                vecUnfilledQnr = (Vector) response.getDataObject();
            }
        }
//        return success;
        return vecUnfilledQnr;
    }
    // 4272: Maintain History of Questionnaires - End

    //COEUSDEV-198:Sync, link and create proposal hierarchy should apply budget validation rules ONLY
    private char validateForHierarchy(String proposalNumber,boolean isParent) throws Exception{

        char errType = 0;
        if(this.functionType != CoeusGuiConstants.DISPLAY_MODE ){
            saveProposalDetails( SAVE_RETAIN_LOCK );
        }
        Vector inputVector= new Vector();
        String leadUnitNumber = proposalDevelopmentFormBean.getOwnedBy();
        inputVector.add(proposalNumber);//module item key
        inputVector.add(leadUnitNumber);//unit number
        inputVector.add(new Boolean(isParent));
        String connectTo = CoeusGuiConstants.CONNECTION_URL + HIERARCHY_SERVLET;

        RequesterBean request = new RequesterBean();
        request.setFunctionType(HIERARCHY_VALIDATION_CHECKS);
        request.setDataObjects(inputVector);
        AppletServletCommunicator comm = new AppletServletCommunicator( connectTo, request );
        comm.send();
        ResponderBean response = comm.getResponse();

        if (response!=null && response.isSuccessfulResponse()) {
            HashMap hmResult = (HashMap)response.getDataObject();
            if (hmResult != null && !hmResult.isEmpty()){
                edu.mit.coeus.routing.gui.RoutingValidationChecksForm valChecks =
                        new edu.mit.coeus.routing.gui.RoutingValidationChecksForm(mdiForm,hmResult,3, proposalNumber );
                valChecks.display();
                if(valChecks.isError()){
                    errType = VALIDATION_ERROR;
                }else{
                    errType = VALIDATION_WARNING;
                }
            }
        }
        return errType;
    }
    //COEUSDEV-198 End
    //Added for the case # COEUSQA-1679-Modification for Final Document Indicator-start
    /** This method is used to validate before submitting the proposal to sponsor
     * It does the validation check on the Proposal Entry before submitting to the sponsor
     * @throws Exception Exception
     * @return true if validation success
     */
    public boolean validateForSubmitToSponsor() throws Exception {
        // Modified for COEUSQA-2778-Submited Prop to Sponsor-Narrative Incomplete - Start
//        if(proposalDevelopmentFormBean.getNarrativeStatus()!= null ){
//            if(proposalDevelopmentFormBean.getNarrativeStatus().trim().equalsIgnoreCase("I")) {
//                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(NARRATIVE_INCOMPLETE));
//                return false;
//            }
//        }
        String narrativeStatus = CoeusGuiConstants.EMPTY_STRING;
        RequesterBean request = new RequesterBean();
        String connectTo = CoeusGuiConstants.CONNECTION_URL + "/ProposalMaintenanceServlet";
        ResponderBean response = null;
        request.setId(proposalDevelopmentFormBean.getProposalNumber());
        request.setFunctionType(GET_NARRATIVE_STATUS);
        AppletServletCommunicator comm
                = new AppletServletCommunicator(connectTo, request);
        comm.setRequest(request);
        comm.send();
        response = comm.getResponse();
        if (response!=null){
            if (response.isSuccessfulResponse()){
                narrativeStatus = ((String)response.getDataObject());
            }
        }
        if(PROPOSAL_NARRATIVE_STATUS.equals(narrativeStatus)) {
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(NARRATIVE_INCOMPLETE));
            return false;
        }
        // Modified for COEUSQA-2778-Submited Prop to Sponsor-Narrative Incomplete - end
        return true;
    }
     private boolean fetchParameterValue()throws Exception{
        boolean parameterFlag = false;
        RequesterBean  requesterB = new RequesterBean();
        requesterB.setFunctionType(GET_PARAMETER_VALUE);
       // requesterBean.setId("ENABLE_PROP_PERSON_SELF_CERTIFY");
         requesterB.setParameterValue("ENABLE_PROP_PERSON_SELF_CERTIFY");
      //  requesterB.setDataObject("ENABLE_PROP_PERSON_SELF_CERTIFY");
         AppletServletCommunicator comm = new AppletServletCommunicator(
         connect, requesterB);
         comm.send();
           ResponderBean response = comm.getResponse();
         if(response!= null){
            if(response.isSuccessfulResponse()){
                parameterFlag = ((Boolean)response.getParameterValue()).booleanValue();
            }else {
                throw new Exception(response.getMessage());
            }
        }
         return parameterFlag ;
    }
   private boolean getNotifyProposalPersonRight()throws Exception{
        String ProposalNum= proposalDevelopmentFormBean.getProposalNumber();
        boolean hasNotifyProposalPersonRight = false;
        RequesterBean  requesterBean = new RequesterBean();
        requesterBean.setFunctionType(NOTIFY_PROPOSAL_PERSONS);
        requesterBean.setId(ProposalNum);
        requesterBean.setDataObject((String)mdiForm.getUserId() );
        AppletServletCommunicator comm = new AppletServletCommunicator(
        connect, requesterBean);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response!= null){
            if(response.isSuccessfulResponse()){
                hasNotifyProposalPersonRight = ((Boolean)response.getDataObject()).booleanValue();
            }else {
                throw new Exception(response.getMessage());
            }
        }
        return hasNotifyProposalPersonRight;
    }
    //Added for the case # COEUSQA-1679-Modification for Final Document Indicator-end

    // rdias UCSD - Coeus personalization impl
    public void customizeProposalDev() {
    	AbstractController persnref = AbstractController.getPersonalizationControllerRef();
    	persnref.customize_tabs(tbdPnProposal,
    			CoeusGuiConstants.PROPOSAL_BASE_FRAME_TITLE,proposalDevelopmentFormBean.getProposalNumber(),
    			functionType);
    	persnref.customize_module(this,this,this,CoeusGuiConstants.PROPOSAL_BASE_FRAME_TITLE);
    	persnref.customize_Form(proposalDetail,CoeusGuiConstants.PROPOSAL_BASE_FRAME_TITLE);
    }
   // rdias UCSD - Coeus personalization impl

   //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - Start
    /** This method is used to remove the duplicate data before displaying the person names for selection
     * @param Vector vecInvData
     * @return Vector after duplicate data is removed
     */
    private Vector removeDuplicateData(Vector vecInvData){
        HashSet hsBothInvestigatorAndKeyPerson = new HashSet();
        Vector cvAllData = new Vector();
        for(int mainIndex=0;mainIndex < vecInvData.size();mainIndex++){
            Object mainInvesKeyPersonObject = vecInvData.get(mainIndex);
            String mainPersonName = CoeusGuiConstants.EMPTY_STRING;
            boolean principalInvestigator = false;
            int counter = 0;
            //To check whether the bean is investigator or key person
            if(mainInvesKeyPersonObject instanceof ProposalInvestigatorFormBean){
                ProposalInvestigatorFormBean bean = (ProposalInvestigatorFormBean)vecInvData.get(mainIndex);
                mainPersonName = bean.getPersonName();
                principalInvestigator = bean.isPrincipleInvestigatorFlag();
            }else if(mainInvesKeyPersonObject instanceof ProposalKeyPersonFormBean){
                ProposalKeyPersonFormBean keyPersonDetails = (ProposalKeyPersonFormBean)vecInvData.get(mainIndex);
                mainPersonName = keyPersonDetails.getPersonName();
                principalInvestigator = false;
            }
            //To loop through the objects in the vector
            for(int index=0;index < vecInvData.size();index++){
                Object invesKeyPersonObject = vecInvData.get(index);
                String personName = CoeusGuiConstants.EMPTY_STRING;
                String keyPersonName = CoeusGuiConstants.EMPTY_STRING;
                //To check whether the bean is investigator or key person
                if(invesKeyPersonObject instanceof ProposalInvestigatorFormBean){
                    ProposalInvestigatorFormBean bean = (ProposalInvestigatorFormBean)vecInvData.get(index);
                    personName = bean.getPersonName();
                }else if(invesKeyPersonObject instanceof ProposalKeyPersonFormBean){
                    ProposalKeyPersonFormBean keyPersonDetails = (ProposalKeyPersonFormBean)vecInvData.get(index);
                    keyPersonName = personName = keyPersonDetails.getPersonName();
                }
                //To check whether the person is principal investigator
                if(mainPersonName!= CoeusGuiConstants.EMPTY_STRING && mainPersonName.equals(personName) && !principalInvestigator){
                    //If the person is not a investigator increment the counter
                    counter++;
                    //Add the person to the Set to remove the duplicate entry
                    hsBothInvestigatorAndKeyPerson.add(mainPersonName);
                }else if(mainPersonName!= CoeusGuiConstants.EMPTY_STRING && mainPersonName.equals(personName) && principalInvestigator){
                   //If the person is investigator increment the counter
                   counter++;
                   //Add the person to the Set to remove the duplicate entry
                   if(keyPersonName!= CoeusGuiConstants.EMPTY_STRING && personName.equals(keyPersonName)){
                        hsBothInvestigatorAndKeyPerson.add(mainPersonName);
                    }
                   //If the person is added to the list remove the entry from the vector
                   if(counter > 1){
                       vecInvData.removeElementAt(index);
                       counter--;
                   }
               }
            }
            //If the person is added to the list remove the entry from the vector
            if(counter > 1){
                vecInvData.removeElementAt(mainIndex);
            }
        }
        //Fetch the unique person values from Set to Object array
        Object bothInvesAndKeyPer [] = hsBothInvestigatorAndKeyPerson.toArray();
        cvAllData.add(vecInvData);
        cvAllData.add(bothInvesAndKeyPer);
        return cvAllData;
    }

    /** This method is used to add the key person data before displaying the person names for selection
     * @param Vector vecKeyPersonData
     * @param Vector vecAllKeyPersonData
     * @return Vector after adding data
     */
    public Vector addKeyPersonData(Vector vecKeyPersonData, Vector vecAllKeyPersonData){
        if(vecAllKeyPersonData == null){
            vecAllKeyPersonData = new Vector();
        }
        for(Object dataBean : vecKeyPersonData){
            vecAllKeyPersonData.add(dataBean);
        }
        return vecAllKeyPersonData;
    }
    //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - End


    //COEUSQA-1433 - Allow Recall from Routing - Start
      /*The method check if the protocol is locked by an other user
       * Returns lockAvailable
       */
    public boolean lockProposalRecall() {
        boolean lockAvailable = false;
        boolean userRestrictedView = false;
        propID = proposalDevelopmentFormBean.getProposalNumber();
        if(functionType == DISPLAY_MODE) {
            String connectTo = CoeusGuiConstants.CONNECTION_URL + PROPOSAL_SERVLET;
            RequesterBean request = new RequesterBean();
            request.setFunctionType(PROPOSAL_RECALL_LOCK_CHECK);
            request.setId((String)proposalDevelopmentFormBean.getProposalNumber());
            AppletServletCommunicator comm = new AppletServletCommunicator(
                    connectTo, request);
            comm.send();
            ResponderBean response = comm.getResponse();
            if(response==null){
                CoeusOptionPane.showErrorDialog("Could not contact server");
            } else {
                if(response.getDataObject() == null){
                    lockAvailable = false;
                }else{
                    lockAvailable =((Boolean)response.getDataObject()).booleanValue();
                }
                
                //Commented and Added for COEUSDEV-1075 : Locking messages inconsistency between Lite and Premium - start
                /*if(!lockAvailable){
                    CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey("protocolDetForm_exceptionCode.1019"));
                }*/
                String displayingLockName = getParameterValue();
                
                if(displayingLockName != null && "Y".equalsIgnoreCase(displayingLockName.trim())){
                    userRestrictedView = true;
                }
                //If parameter DISPLAY_LOCKNAME_FOR_ROUTING is set as 'S' then need to provide the locked user name
                if(userRestrictedView && response.getMessage()!= null){
                    if(!lockAvailable){
                        String message = response.getMessage();
                        CoeusOptionPane.showErrorDialog(message);
                    }
                }
                //If parameter DISPLAY_LOCKNAME_FOR_ROUTING is set as 'N' then need to hide the locked user name
                if(!userRestrictedView){
                    if(!lockAvailable){
                        CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey("locking_exceptionCode.1021")+" "+"Proposal Routing "+ propID);
                    }
                }
                //Added for COEUSDEV-1075 : Locking messages inconsistency between Lite and Premium - end
            }
        }
        return lockAvailable;
    }
    //COEUSQA-1433 - Allow Recall from Routing - Start

    /*
     * Method to get the proposal status code
     * @param proposalNumber
     * @return proposalStatusCode
     */
    private ComboBoxBean getProposalStatusDetails(String proposalNumber)throws Exception{
        ComboBoxBean proposalStatusDetails = null;
        String connectTo = CoeusGuiConstants.CONNECTION_URL +PROPOSAL_SERVLET;
        RequesterBean request = new RequesterBean();
        request.setFunctionType(PROPOSAL_CREATION_STATUS_DETAILS);
        request.setDataObject(proposalNumber);
        AppletServletCommunicator comm
                = new AppletServletCommunicator( connectTo, request );
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response == null) {
            response = new ResponderBean();
            response.setResponseStatus(false);
        }
        if (response.isSuccessfulResponse()) {
            proposalStatusDetails = (ComboBoxBean)response.getDataObject();
        }else{
            throw new CoeusException(response.getMessage());
        }

        return proposalStatusDetails;
    }
    
    //Added for COEUSDEV-1075 : Locking messages inconsistency between Lite and Premium - start
    /**
     * Method to get the value for the parameter  DISPLAY_LOCKNAME_FOR_ROUTING
     * @return value
     */
    public String getParameterValue(){
        final String connectTo = CoeusGuiConstants.CONNECTION_URL+ "/coeusFunctionsServlet";
        final String PARAMETER = "DISPLAY_LOCKNAME_FOR_ROUTING";
        String value = CoeusGuiConstants.EMPTY_STRING;
        RequesterBean requester = new RequesterBean();
        ResponderBean responder = null;
        requester.setDataObject(GET_PARAMETER_VALUE_ROUTING);
        Vector vecParameter = new Vector();
        vecParameter.add(PARAMETER);
        requester.setDataObjects(vecParameter);
        AppletServletCommunicator comm
                = new AppletServletCommunicator(connectTo, requester);
        comm.send();
        responder = comm.getResponse();
        if(responder.isSuccessfulResponse()){
            value =(String) responder.getDataObject();
        }
        return value;
    }
//Added for COEUSDEV-1075 : Locking messages inconsistency between Lite and Premium - end
 public boolean getParameterValueForPPC(){
        final String connectTo = CoeusGuiConstants.CONNECTION_URL+ "/coeusFunctionsServlet";
        final String PARAMETER = ENABLE_PROP_PERSON_SELF_CERTIFY;
        boolean returnValue = false;
        Integer value;
        RequesterBean requester = new RequesterBean();
        ResponderBean responder = null;
        requester.setDataObject(GET_PARAMETER_VALUE_ROUTING);
        Vector vecParameter = new Vector();
        vecParameter.add(PARAMETER);
        requester.setDataObjects(vecParameter);
        AppletServletCommunicator comm
                = new AppletServletCommunicator(connectTo, requester);
        comm.send();
        responder = comm.getResponse();
        if(responder.isSuccessfulResponse()){
            value =Integer.parseInt(responder.getDataObject().toString());
            if(value == 1){
                returnValue = true;
            }
        }
        return returnValue;
    }   

    private void showHumnSubjtForms(String proposalNumber) throws IOException, Exception{
    String personId =(String)mdiForm.getUserId();
    String ProposalNum= proposalNumber;
    String urlSend=null;
    String baseUrl = getAppHomeUrl();
    StringBuffer url =null;
                            url=new StringBuffer();
                            url.append(baseUrl);
                            url.append("getPHSHumanSubjectForm.do?proposalNumber=");
                            url.append(ProposalNum+"&id_person="+personId);
                            urlSend=url.toString();
                       
        try{
            URLOpener.openUrl(urlSend);
            }catch(Exception ex){
                CoeusOptionPane.showErrorDialog(ex.getMessage());
            }
    }
    
    private String getAppHomeUrl() throws Exception{     
        RequesterBean  requesterBean = new RequesterBean();
        requesterBean.setFunctionType(GET_APP_HOME_URL);      
        String appHomeUrl = null;
        AppletServletCommunicator comm = new AppletServletCommunicator(
        connect, requesterBean);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response!= null){
            if(response.isSuccessfulResponse()){
                appHomeUrl = (String)response.getParameterValue();
            }else {
                throw new Exception(response.getMessage());
            }
        }
        return appHomeUrl;
    }
	 private boolean isPHSHumanSubjectCTFormIncluded(String proposalNumber)throws Exception{
        boolean returnValue = false;
        String connectTo = CoeusGuiConstants.CONNECTION_URL +PROPOSAL_SERVLET;
        RequesterBean request = new RequesterBean();
        request.setFunctionType(CHECK_IS_PHS_HS_CT_FORM);
        request.setDataObject(proposalNumber);
        AppletServletCommunicator comm
                = new AppletServletCommunicator( connectTo, request );
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response == null) {
            response = new ResponderBean();
            response.setResponseStatus(false);
        }        
        if (response.isSuccessfulResponse()) {
           returnValue  =  ((Boolean)response.getDataObject()).booleanValue();                    
        }else{
            throw new CoeusException(response.getMessage());
        }

        return returnValue;
    } 
	public void EnableDisableHumanSubject(){
     try{ 
      if(isPHSHumanSubjectCTFormIncluded(proposalID)){
          humnSubS2SForms.setVisible(true);
      }else{
          humnSubS2SForms.setVisible(false);
      }
     }catch(Exception e){}
      
  }
}
