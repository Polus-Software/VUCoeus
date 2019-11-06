/*
 * @(#)ProtocolDetailsForm.java 1.0 10/17/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/* PMD check performed, and commented unused imports and variables on 16-FEB-2011
 * by Bharati
 */
package edu.mit.coeus.irb.gui;

import edu.mit.coeus.irb.controller.ProtocolHistoryController;
import edu.mit.coeus.irb.controller.ProtocolMailController;
import edu.mit.coeus.organization.bean.OrganizationAddressFormBean;
import edu.mit.coeus.propdev.bean.ProposalAwardHierarchyLinkBean;
import edu.mit.coeus.propdev.gui.MedusaDetailForm;
import edu.mit.coeus.rolodexmaint.bean.RolodexDetailsBean;
import edu.mit.coeus.routing.bean.RoutingBean;
import edu.mit.coeus.routing.gui.RoutingForm;
import java.text.MessageFormat;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.util.*;
import java.text.DecimalFormat;
//import java.io.File;
//import javax.swing.table.DefaultTableModel;

import edu.mit.coeus.utils.*;
import edu.mit.coeus.utils.customelements.*;
import edu.mit.coeus.utils.roles.*;
import edu.mit.coeus.irb.bean.*;
//import edu.mit.coeus.bean.RoleBean;
import edu.mit.coeus.gui.*;
import edu.mit.coeus.gui.toolbar.*;
import edu.mit.coeus.gui.menu.*;
import edu.mit.coeus.bean.*;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.customelements.bean.CustomElementsInfoBean;
//import edu.mit.coeus.utils.refno.ReferenceNumberForm;
import edu.mit.coeus.exception.*;
import edu.mit.coeus.propdev.gui.InboxDetailForm;

//Added by Vyjayanthi
//Start
//import edu.mit.coeus.irb.gui.ProcessAction;
//import edu.mit.coeus.irb.gui.ProtocolActionsInterface;
import edu.mit.coeus.questionnaire.bean.QuestionnaireAnswerHeaderBean;
import edu.mit.coeus.questionnaire.controller.QuestionnaireAnswersBaseController;
import edu.mit.coeus.questionnaire.gui.QuestionnaireAnswersBaseForm;
import edu.mit.coeus.routing.gui.RoutingValidationChecksForm;
import edu.mit.coeus.user.gui.UserDelegationForm;
import edu.mit.coeus.user.gui.UserPreferencesForm;

//End

/** This class constructs the <CODE>ProtocolDetailForm</CODE> which will be used to create
 * a protocol.
 *
 * @version :1.0 October 17, 2002, 1:35 PM
 * @author Phaneendra Kumar B
 */
public class ProtocolDetailForm extends CoeusInternalFrame
implements ActionListener, ProtocolActionsInterface, Observer { //ProtocolActionsInterface added by Vyjayanthi
    
    private JPanel pnlForm;
    
    private javax.swing.JScrollPane jscrPn;
    FlowLayout layout;
    JPanel pnlDetail;
    JPanel pnlInv;
    JPanel pnlKey;
    
    // Menu items for protocol
    private CoeusMenuItem protocolSubmit,protocolRoles,submissionDetails,
    references, protocolSummary,
    //Added for the case# COEUSQA-1403 Implement validation based on rules in protocols-start
            protoValidationChecks,
    //Added for the case# COEUSQA-1403 Implement validation based on rules in protocols-End            
            sendMail;//modified for Coeus Enhancement case:#1787step 1 //relatedProjects; //prps added submissionDetails
    //Added sendMail menu item for Coeus4.3 Enhancement case:#2214 Email Notification.
    
    //prps start dec 22 2003
    private CoeusMenuItem printAdhoc ;
    //prps start dec 22 2003
    
    //Commented for Case 3552 - IRB Attachments - start
    //Added for Protocol Upload Document start 1
    //private CoeusMenuItem mnuItmUploadDoc;
    //Added for Protocol Upload Document end 1
    //Commented for Case 3552 - IRB Attachments - start
    
    //Added for Coeus 4.3 enhancement PT ID:2210 - View Protocol History start
    private CoeusMenuItem protocolHistory;
    //Added for Coeus 4.3 enhancement PT ID:2210 - View Protocol History end
    //Added for case 2785 - Routing enhancement -start
    private CoeusMenuItem mnuItmApprove;
    private CoeusMenuItem mnuItmShowRouting;
    //Added for case 2785 - Routing enhancement -end
    //Added by Vyjayanthi
    //Start
    // Modified for COEUSQA-2692_Allow an investigator to abandon an IRB protocol that has never been approved_start
    private CoeusMenuItem protocolAbandon,protocolWithdraw, protocolRequestToTerminate, protocolRequestToClose,
    protocolRequestSuspension, protocolClosed, protocolTerminated, protocolSuspend,protocolSuspendByDSMB,
    protocolExpire, requestCloseEnrollment,closeEnrollment,notification;
    // Modified for COEUSQA-2692_Allow an investigator to abandon an IRB protocol that has never been approved_end
    
    //Added for performing request and non request actions - start - 1
    private CoeusMenuItem protocolRequestToReopen, protocolRequestToDA, protocolNotify, protocolReopen, protocolDA;
    //Added for performing request and non request actions - end - 1    
    
    private ProcessAction processAction;
    //End
    
    //Added by Jobin for Undo last Action - start
    private CoeusMenuItem undoLastAction; //end
       //Added for Disclosure Status menu item - start
    private CoeusMenuItem disclosurestatus; //end
    //Added questionnaire - start 1
    private CoeusMenuItem mnuItmQuestionnaire; 
    //Added questionnaire - end 1
    ProtocolMaintenanceForm protocolMaintenance;
    ProtocolInvestigatorsForm protocolInv;
    ProtocolKeyStudyPersonnelForm protocolKey;
    ProtocolCorrespondenceForm protocolCorr;
    ProtocolAORForm protocolArea;
    ProtocolFundingSourceForm protocolFund;
    ProtocolActionsForm protocolAction;
    ProtocolSubmissionForm submissionForm;
    ProtocolSubmissionDisplayForm submissionDisplayForm;
    ProtocolVulnerableSubjectsForm protocolSubjects;
    ProtocolSpecialReviewForm protocolSpecialReviewForm;
    ProtocolNotepadForm protocolNotepadForm;
    UserRolesMaintenance rolesForm;
    CustomElementsForm customElementsForm;
    //ReferenceNumberForm referenceNumberForm;
    ProtocolReferenceNumberForm protocolReferenceNumberForm;
    // added by Manoj 20/08/03 10.45
    //modified for Coeus Enhancement case:#1787 step 2
    // ProtocolRelatedProjects protocolRelatedProjects;
    
    // holds Amendments and Renewals details
    private ProtocolAmendmentRenewalForm amendRenewalForm;
    //Added for case 2176 - Risk Level Category - start
    private ProtocolRiskLevelCategoryForm protocolRiskLevelCategoryForm ;
    //Added for case 2176 - Risk Level Category - end
    //Added for case 3552 - IRB attachments - start
    private ProtocolAttachmentsForm protocolAttachmentsForm;
    //Added for case 3552 - IRB attachments - end
    private int sequenceId;
//    private CoeusDlgWindow coeusDialog;
    private CoeusDlgWindow dlgRoles;
    private CoeusAppletMDIForm mdiForm;
    
    private char functionType;
    private final String PROTOCOL_SERVLET = "/protocolMntServlet";
//    private final String PROTOCOL_SUB_SERVLET = "/protocolSubSrvlt";
    //prps start
    private final String SUBMISSION_DETAILS_SERVLET = "/SubmissionDetailsServlet" ;
    //prps end
    private final char GET_TREE_DATA_FOR_UNIT = 'T';
    private final char GET_USER_ROLES_FOR_UNIT ='7';
    private String protocolId;
    
    private ProtocolInfoBean protocolInfo;
    //private Vector vecProtoRelated = null;
//    private final String PROTOCOL_FRAME_NAME="PROTOCOL";
    
    private final char ADD_MODE = 'A';
    private final char MODIFY_MODE = 'M';
    private final char DISPLAY_MODE = 'D';
    
    //holds the copy type falg
    private static final char COPY_MODE = 'E';
    //COEUSQA-2602-Remove checkboxes from Renewal Summary screen
    private final char CHECK_IS_RENEWAL = 'K';
    
//    private static final char TAB_WINDOW = 'T';
//    private static final char RESPONSE_WINDOW = 'R';
    
    //Added by Vyjayanthi - 30/07/2004 for IRB Enhancement
    /** Holds the mode in which the protocol is opened
     * Included for Administrative Corrections mode
     */
    private String mode;
    private char screenMode;
    
    private boolean isRoleDataChanged;
    private boolean isLock = true;
//    private Vector newExistingData;
    private Vector updData=null;
    private Vector serverExistingDetails;
    private Vector serverCopyOfExistingUserRoles;
    
    //Added by Vyjayanthi - 02/08/2004 for IRB Enhancement
    /** Holds the entire Protocol data from the server to populate the screen
     * This is set in Administrative Correction mode while checking if
     * user has rights to perform Administrative Correction on a protocol
     */
    private Vector vecProtocolData;
    
    //public static String PROTOCOL_NUMBER;
    //Modified for case 3552 - IRB attachments - start
    public CoeusToolBarButton btnSaveProtocol;
    //Modified for case 3552 - IRB attachments - end
    private CoeusToolBarButton btnCloseProtocol;
    private CoeusToolBarButton btnProtocolRoles;
    private CoeusToolBarButton btnSummary;
    //Case#3091 - IRB - generate a protocol summary pdf  - Start
    private CoeusToolBarButton btnPrintSummary;
    //Case#3091 - End
    // protocol types
    private Vector protocolTypes;
    // protocol Status
    private Vector protocolStatus;
    // Protocol Correspondet Types information
    private Vector protocolCorrespondentTypes;
    // Protocol Fund source Types information
    private Vector protocolFundsourceTypes;
    private Vector protocolVulnerableSubjects;
    // Added By Raghunath P.V. For implementing reference numbers
    private Vector referenceNumberTypes;
    // holds the parameter values to the PROTOCOL module
    //private Vector parameters;// Commented to use CoeusLabel instead Module Parameter XML file Start :02-Sep-2005
    // Added By Raghunath P.V. For implementing reference numbers
    //private Vector referenceParameters;// Commented to use CoeusLabel instead Module Parameter XML file Start :02-Sep-2005
    // Added By Raghunath P.V. For implementing Special reviews
    private Vector protocolSpecialReviewCodes;
    private Vector protocolApprovalCodes;
    private Vector protocolSpecialReviewData;
//    private Vector protocolReferenceNumbersData;
    private Vector vecSRValidateData;
    
    // added by Senthil nathan
    private Vector protocolVecNotes;
    
    private Vector protInvestigators = null;
    private Vector protKeyStudyPersonnel = null;
    private Vector protCorrespondents = null;
    
    private Vector protReferenceData = null;
    private Vector protReplicateRefData = null;
    
    private Vector protAOR = null;
    private Vector protFundingSource = null;
    private Vector protActions = null;
    private Vector protCustomElements;
    private Vector selectedSubjects;
    //Coeus Enhancement case:#1787 step 3 start
   /* private Vector protRelProjects;
    // added by manoj to hold protocol related projects data before saving
    
    private Vector vecProtoRelProj = null;
    private Vector vecProtoRelProjTemp = null;
    */
    //Coeus Enhancement case:#1787 step 3 end
    private boolean saveRequired = false;
    JTabbedPane tbdPnProtocol;
    private boolean isTimedOut;
    
    private boolean modifiable = true;
    private int enableProposaltoProtocolLink;
    //Case #1787
    private boolean lockExist;
    
    private boolean closed;
    private boolean cnclClose;
    //modified for Coeus Enhancement case:#1787 step 4 
    // added by manoj to hold save required satus of protocol related projects
   /// private boolean isProtoRelSaveReq = false;
    private boolean isProtoRefSaveReq = false;
    
    //Added for COEUSQA-3144 : IRB and IACUC - Add warning to Undo Last Action that review comments will be lost - start
    private final static char UNDO_LAST_ACTION_CONFIRMATION = 'u';
    private static final String DIALOG_TYPE_POP_UP_FOR_ACTION = "ConfirmationMessage"; 
    //Added for COEUSQA-3144 - end    
    
    //holds CoeusMessageResources instance used for reading message Properties.
    private CoeusMessageResources coeusMessageResources;
    
    //public String PIName,leadUnitNumber,leadUnitName;
    private String referenceId;
    
    private Vector tempUserRoles;
    
    private Vector exisitingUserRoles;
    private Vector copyOfExistingUserRoles;
    private Vector userRoles = new Vector();
    private Vector userAndRoleDetails = new Vector();
    private static final String MODIFIED = "M";
    private static final String NOT_MODIFIED = "NM";
    private static final String NOT_PRESENT = "NP";
    
    private String newLeadUnit = "",oldLeadUnit = "";
    private Vector usersForUnit = new Vector();
    private Vector treeData = null;
    private Vector orgTypes = new Vector();
    CoeusVector cvParameters = new CoeusVector();
    //Added for case#4278-In Comments on Action Details, indicate Renewal with Amendment for submissions created via New Am -start
   // private boolean isNewAmendRenew=false;
    //Added for case#4278-In Comments on Action Details, indicate Renewal with Amendment for submissions created via New Am -end
    //Added for Case#2214 Email enhancement
    private CoeusToolBarButton sendEmail;
    //Coeus Enhancement case #1797 start
    //Added for the case# COEUSQA-1403 Implement validation based on rules in protocols-start
    private static final String CONFIRM_SUBMIT_APPROVAL = "submitforProtocolApproval_exceptionCode.1000"; //"Do you still want to submit the protocol?";
    //Added for the case# COEUSQA-1403 Implement validation based on rules in protocols-end
    private static final Vector submitEnabledProtocolStatusCodes = new Vector(9,1);
      static
      { 
        submitEnabledProtocolStatusCodes.add(new Integer(100));
        submitEnabledProtocolStatusCodes.add(new Integer(102));
        submitEnabledProtocolStatusCodes.add(new Integer(103));
        submitEnabledProtocolStatusCodes.add(new Integer(104));
        submitEnabledProtocolStatusCodes.add(new Integer(105));
        submitEnabledProtocolStatusCodes.add(new Integer(106));
//        submitEnabledProtocolStatusCodes.add(new Integer(107));
        submitEnabledProtocolStatusCodes.add(new Integer(304));
        submitEnabledProtocolStatusCodes.add(new Integer(309));
      }
   
    //Coeus Enhancement case #1797 end
    //This vector holds the ComboBoxBean that in turn contains the Affiliation data
    private Vector vecAffiliation;
    
    // 0 - for Normal Protocol Details
    // 1 - for Protocol Amendment
    // 2 - for Protocol Revison
    private int requestedModule = CoeusGuiConstants.PROTOCOL_DETAIL_CODE;
    private String referenceTitle;
    /*
     * to indicate horizondal seperator in menu items
     */
    private final String SEPERATOR="seperator";
    private final String versionFormatter = "000";
    private DecimalFormat decimalFormat = new DecimalFormat( versionFormatter );
    
    private String versionNumber="000";
    private boolean isFromBaseWindow;
//    private boolean isAllProjectsDelete = false;
    
    private final char MAX_AMEND_VER = 'F';
    
    private final char MAX_REVISION_VER = 'H';
    private final char SAVE_NEW_REVISION = 'r';
    private final char SAVE_NEW_AMENDMENT = 'n';
    private char saveType = 'S'; // normal protocol save code
    private char saveProposal = '8';
    private char userRole='0';
    private final int AMENDMENT_STATUS_CODE = 105;
    private final int REVISION_STATUS_CODE = 106;
    
    //holds the selected row of base table whose details are shown in this form
//    private int baseTableRow;
    
    private BaseWindowObservable observable;
    private Vector tempReferences = null;
    
//    private boolean notSaved = true;
    
    //    private boolean canModifyRolesInDisplayMode = true;
    //    private boolean canModifyNotesInDisplayMode = true;
    
    private int recordLockedStatus = -1;
    private LockObservable lockObservable = new LockObservable();
    private int authorizedToEditRoles = -1;
    private EscapeKeyListener escapeKeyListener = new EscapeKeyListener("escPressed");
    
//    private boolean lockSchedule = true;
//    private String msgPrompt = "" ;
    private String strTitle = "";
//    private String strDefault = "";
//    private ScheduleActionInputForm inputForm ;
//    private String userInput = null;
//    private Vector reviewComments = null;
    
    //Protocol Enhancement -  Administrative Correction Start 1 
    private CoeusDlgWindow dlgAdminCorrComments;
    private AdministrativeCorrectionComments adminCorrComments;
    
    private static final String WINDOW_TITLE = "Administrative Correction Comments";
    //Added for Protocol Upload Documents start 2
    //private static final String PROTOCOL_SAVE_INFO = "instPropBase_exceptionCode.1066";
    
    //Added for Protocol Upload Documents end 2
    private static final int WIDTH = 425;
    private static final int HEIGHT = 280;
    //Protocol Enhancement -  Administrative Correction End 1 
    
    //Case 1787 Start 17
    boolean canModifyInDisplay = false;
    //Case 1787 End 17
    
    //Case 2026 Start
   boolean canFundingSrcModifyInDisplay = false;
    //Case 2026 End
   // Code added for coeus4.3 enhancements
   private JPanel pnlAmendments;
   private Vector vecModuleData;
   private char originalFunctionType = 'A';
   private ProtocolInfoBean originalProtocolInfo;
   //Added for case 2785 - Routing enhancement - start
   private final int SUBMIT_TO_COMMITTEE = 100 ;
   private final int REJECTED_SUBMISSION = 214;
   private final int ROUTING_IN_PROGRESS = 213;
   //private final int PENDING = 102;
   //private static final String PROTOCOL_SUBMISSION_SERVLET = CoeusGuiConstants.CONNECTION_URL+ "/protocolSubSrvlt";
   //private final char UPD_PROT_SUBMISSION_STATUS = 'H';
   private final char SUBMIT_FOR_APPROVE = 'G';
   private final String NO_PROTOCOL_ROUTING_MAPS = "routing_showRouting_exceptionCode.1001";
   //Added for case#3089 - Special Review Tab made editable in display mode - start
   private final char HAS_ADMINISTRATIVE_CORRECTION_RIGHT = '1';
   private boolean canModifySpRevInDisplay = false;   
   //Added for case#3089 - Special Review Tab made editable in display mode - end
   //Code added for Case#2785 - Protocol Routing
   private boolean errMsgDisplayed;
   
   //Added for case 2785 - Routing enhancement - end
   //Added for case 3552 - IRB Attachments - start
   private int ATTACHMENTS_TAB = 8;
   private int SPECIAL_REVIEW_TAB = 9;
   private int RISK_LEVEL_TAB = 10;
   private int AMENDMEND_SUMMARY_TAB = 12;
   private int OTHERS_TAB = 13;
   
   private boolean saveSuccessful = false;
   private boolean admistrativeRightPresent = false;
   private boolean checkedAdministrativeCorrection = false;
   //Added for case 3552 - IRB Attachments - end
   //Case :#3149 Tabbing between fields does not work on Others tabs - Start
   private int row = 0 ;
   private int column =0;
   private JTable customTable = null;
   private int count = 1;
   boolean isotherTabSelected = false;
   //Case :#3149 - End
   //private static final char GET_NEW_ROUTING_NUMBER = 'T';
   
   //Added for Case#4275 - upload attachments until in agenda - Start
   private static final char HAS_MODIFY_ATTACHMENTS_RIGHTS = '5';
   private static final int SUBMITTED_TO_IRB = 101;
   private boolean checkedAttachmentRights = false;
   private boolean canModifyAttachment = false;
   private static final String UPLOAD_ATTACHMENTS = "008";
   //Case#4275 - End
   private boolean isOtherTabDisabled =  true;
   
   //Case#3091 - IRB - generate a protocol summary pdf - Start 
   private CoeusMenuItem mnuItmInbox,mnuItmClose,mnuItmSave,mnuItmSaveAs,mnuItmPrintSummary, mnuItmChangePassword
           ,mnuItmPreferences,mnuItmExit,mnuItmCurrentLock,mnuItmDelegations;
   private CoeusMenu mnuFile;
   //CAse#3091 - End
   // 4272: Maintain History of Questionnaires
    private static final char CHECK_QUESTIONNAIRE_COMPLETED = 'b';
   // COEUSQA-2105: No notification for some IRB actions
    //Added for COEUSQA-2249 : Routing history is lost after an amendment is routed and approved - Start
    private static final char GET_ROUTING_SEQUENCE_HISTORY = 'H';
    private static final int MODULE_CODE_INDEX = 0;
    private static final int MODULE_ITEM_KEY_INDEX  = 1;
    private static final String PROTOCOL_MODULE = "7";
    private static final String SAVE_CONFIRMATION_BEFOR_OPEN_ROUTING = "protoRoutingSaveConfirmation_exceptionCode.1000";
    //COEUSQA-2249 : End
    private boolean adminCorrectionMailSent;
    // Modified for COEUSQA-2692_Allow an investigator to abandon an IRB protocol that has never been approved - Start
    private static final int ABANDONED_STATUS_CODE = 313;
    // Modified for COEUSQA-2692_Allow an investigator to abandon an IRB protocol that has never been approved - End
    // Added for COEUSQA-2556 : When a protocol is in routing it should not have a status of Submitted to IRB - Start
    public static final int ROUTING_IN_PROGRESS_STATUS = 107;
    private static final char GET_PROTOCOL_DETAILS = 'f';
    // Added for COEUSQA-2556 : When a protocol is in routing it should not have a status of Submitted to IRB - End
    // Added with CoeusQA2313: Completion of Questionnaire for Submission
    private static final String QUESTIONNAIRE_SERVLET = "/questionnaireServlet";
    private static final char GET_QUESTIONS_MODE = 'I';
    private static final char COPY_QUESTIONNAIRE_FOR_REVISIONS = 'e';
    // CoeusQA2313: Completion of Questionnaire for Submission - End
    
    //COEUSQA-1433 - Allow Recall from Routing - Start
    private static final char PROTOCOL_RECALL_LOCK_CHECK = 'H';
    private final int RECALLED_SUBMISSION = 215;
    private final String LOCKING_SERVLET = "/LockingServlet";
    //COEUSQA-1433 - Allow Recall from Routing - End
    //COEUSQA:2653 - Add Protocols to Medusa - Start
     private CoeusToolBarButton btnMedusa;
    //COEUSQA:2653 - End
    //COEUSQA 1457 STARTS
    private static final char PROTOCOL_SEND_NOTIFICATION = ':';
    //COEUSQA 1457 ENDS
    //Added for COEUSDEV-1075 : Locking messages inconsistency between Lite and Premium - start
    private static final String GET_PARAMETER_VALUE = "GET_PARAMETER_VALUE";
    //Added for COEUSDEV-1075 : Locking messages inconsistency between Lite and Premium - end
    // Added for Enable multicampus for default organization in protocols - Start
    private static final String FUNCTION_SERVLET = "/coeusFunctionsServlet";
    // Added for Enable multicampus for default organization in protocols - End
    
    //COEUSQA:3503 - Protocol Copying - Option to Copy Attachments and Questionnaires - Start
    private static boolean isCopyQnr;
    private static boolean isCopyAttachments;
    private static boolean isCopyOtherAttach;
    private Vector protAttachments = null;
    private Vector protOtherAttachments = null;
    //COEUSQA:3503 - End
    
    /** Constructor which constructs the <CODE>ProtocolDetailForm</CODE> with the
     * specified parent component and fetches the protocol details for the
     * given protocol number.
     *
     * @param functionType character which specifies the form opened mode.
     * @param protocolId String representing the Protocol for which the details
     * has to be fetched.
     * @param mdiForm reference to the <CODE>CoeusAppletMDIForm</CODE>.
     * @throws Exception if unable to fetch the details from the database.
     */
    
    public ProtocolDetailForm(char functionType,String protocolId,
    CoeusAppletMDIForm mdiForm) throws Exception {
        super(CoeusGuiConstants.PROTOCOL_FRAME_TITLE + ( (protocolId != null &&
        protocolId.length() > 0 && functionType != COPY_MODE) ? " - "
        + protocolId :"" ), mdiForm);
        this.mdiForm = mdiForm;
        this.protocolId = protocolId;
        //ProtocolDetailForm.PROTOCOL_NUMBER = protocolId;
        this.functionType = functionType;
        screenMode = functionType;
        this.referenceId = protocolId;
        this.setFrameToolBar(getProtocolToolBar());
        this.setToolsMenu(null);
        //Case#3091 - IRB - generate a protocol summary pdf  - Start
        prepareFilemenu();
        //Case#3091 - End
        this.setFrameMenu(getProtocolEditMenu());
        //prps start
        this.setActionsMenu(getProtocolActionsMenu()) ;
        this.coeusMessageResources = CoeusMessageResources.getInstance();
        
        //prps end
        /*Vector data = getProtocolDetails(protocolId);
        if (data != null) {
            setValues(data);
        }*/
        setFrameIcon(mdiForm.getCoeusIcon());
        //initComponents();
        hackToGainFocus();
        
        observable = new BaseWindowObservable();
  
    }
    
    //COEUSQA:3503 - Protocol Copying - Option to Copy Attachments and Questionnaires - Start
    /**
     * Constructor which constructs the <CODE>ProtocolDetailForm</CODE> with the
     * specified parent component and fetches the protocol details for the
     * given protocol number with isCopyQnr, isCopyAttach, isCopyOtherAttach
     * 
     * @param functionType 
     * @param protocolId 
     * @param mdiForm 
     * @param isCopyQnr 
     * @param isCopyAttachments 
     * @param isCopyOtherAttach 
     * @throws java.lang.Exception 
     */
    public ProtocolDetailForm(char functionType,String protocolId,CoeusAppletMDIForm mdiForm, boolean isCopyQnr, 
                              boolean isCopyAttachments, boolean isCopyOtherAttach) throws Exception {
        super(CoeusGuiConstants.PROTOCOL_FRAME_TITLE + ( (protocolId != null &&
        protocolId.length() > 0 && functionType != COPY_MODE) ? " - "
        + protocolId :"" ), mdiForm);
        this.mdiForm = mdiForm;
        this.protocolId = protocolId;
        this.functionType = functionType;
        screenMode = functionType;
        this.referenceId = protocolId;
        this.setFrameToolBar(getProtocolToolBar());
        this.setToolsMenu(null);        
        prepareFilemenu();        
        this.setFrameMenu(getProtocolEditMenu());        
        this.setActionsMenu(getProtocolActionsMenu()) ;
        this.coeusMessageResources = CoeusMessageResources.getInstance();      
        setFrameIcon(mdiForm.getCoeusIcon());        
        hackToGainFocus();        
        observable = new BaseWindowObservable();
        this.isCopyQnr = isCopyQnr;
        this.isCopyAttachments = isCopyAttachments;
        this.isCopyOtherAttach = isCopyOtherAttach;
    }
    //COEUSQA:3503 - End
   
    /**
     * Constructor which will be used to open the detail form for Amendments and
     * Renewals. This will split the protocol number specified to extract the
     * exact version of amendment/renewal.
     *
     * @param protocolNo String representing the protocol number followed by A/R to
     * represent amendment/renewal and followed by three digit version number
     * @param functionType character representing the form opened mode.
     * @param moduleNo integer representing the module in which the form is requested
     * to open. 0 - Normal Protocol , 1 - Protocol Amendment, 2 - Protocol Renewal
     */
    public ProtocolDetailForm(String protocolNo, char functionType, int moduleNo)
    throws Exception {
        this(functionType, protocolNo, CoeusGuiConstants.getMDIForm());
        
        if(protocolNo.length() == 14 ) {
            // as protocolNumber will in the format: 0000001765A005
            versionNumber = protocolNo.substring(11);
            
        }else{
            isFromBaseWindow = true;
        }
        screenMode = functionType;
        this.requestedModule = moduleNo;
        observable = new BaseWindowObservable();
    }

    /** Constructor which constructs the <CODE>ProtocolDetailForm</CODE> with the
     * specified parent component and fetches the protocol details for the
     * given protocol number.
     *
     * @param protocolId String representing the protocol for which the details
     * has to be fetched.
     * @param mdiForm reference to the <CODE>CoeusAppletMDIForm</CODE>.
     * @throws Exception if unable to fetch the details from the database.
     */
    public ProtocolDetailForm(String protocolId, CoeusAppletMDIForm mdiForm)
    throws Exception {
        super("Protocol Details" + ( (protocolId != null &&
        protocolId.length() > 0 ) ? " - " + protocolId :"" ), mdiForm);
        this.mdiForm = mdiForm;
        this.protocolId = protocolId;
        //ProtocolDetailForm.PROTOCOL_NUMBER = protocolId;
        this.referenceId = protocolId;
        this.functionType = DISPLAY_MODE;  //BY DEFAULT DISPLAY MODE
        //        setValues(getProtocolDetails(protocolId));
        //        initComponents();
        hackToGainFocus();
        screenMode = functionType;
        observable = new BaseWindowObservable();
    }
    
    //Added by Vyjayanthi - 30/07/2004 for IRB Enhancement
    /** Constructor which constructs the <CODE>ProtocolDetailForm</CODE> with the
     * specified parent component and fetches the protocol details for the
     * given protocol number.
     *
     * @param functionType character which specifies the form opened mode.
     * @param protocolId String representing the Protocol for which the details
     * has to be fetched.
     * @param mdiForm reference to the <CODE>CoeusAppletMDIForm</CODE>.
     * @param mode holds the mode in which the form is open, included for
     * IRB enhancement to open in Administrative Corrections mode
     * @throws Exception if unable to fetch the details from the database.
     */
    public ProtocolDetailForm(String mode, char functionType,String protocolId,
    CoeusAppletMDIForm mdiForm) throws Exception {
        super("Protocol Details" + ( (protocolId != null &&
        protocolId.length() > 0 && functionType != COPY_MODE) ? " - "
        + protocolId :"" ), mdiForm);
        this.mdiForm = mdiForm;
        this.protocolId = protocolId;
        this.functionType = functionType;
        screenMode = functionType;
        this.referenceId = protocolId;
        this.setFrameToolBar(getProtocolToolBar());
        this.setToolsMenu(null);
        //Case#3091 - IRB - generate a protocol summary pdf  - Start
        prepareFilemenu();
        //Case#3091 - End
        this.setFrameMenu(getProtocolEditMenu());
        this.setActionsMenu(getProtocolActionsMenu());
        this.mode = mode;
        if( mode != null &&
        mode.equalsIgnoreCase(CoeusGuiConstants.ADMINISTRATIVE_CORRECTIONS) ){
            disableActionMenuItems();
            //Code added for coeus4.3 Amendments/Renewal enhancements - starts
            if(protocolId != null && protocolId.length() > 10){
                if(protocolId.charAt(10) == 'A'){
                    this.requestedModule = CoeusGuiConstants.PROTOCOL_AMENDMENT_CODE;
                } else {
                    this.requestedModule = CoeusGuiConstants.PROTOCOL_RENEWAL_CODE;
                }
            }
            //Code added for coeus4.3 Amendments/Renewal enhancements - ends
        }
        this.coeusMessageResources = CoeusMessageResources.getInstance();
        setFrameIcon(mdiForm.getCoeusIcon());
        hackToGainFocus();
        observable = new BaseWindowObservable();
    }
    
    /* added by Geo to gain the focus */
    /* begin Block */
    
    private void hackToGainFocus() {
        JFrame frame = new JFrame();
        frame.setLocation(-200,100);
        frame.setSize( 100, 100 );
        frame.show();
        frame.dispose();
        this.dispatchEvent(new InternalFrameEvent(this,InternalFrameEvent.INTERNAL_FRAME_ACTIVATED));
    }
    /* End Block */
    
    
    /** The method is used to get the <CODE>ProtocolInfoBean</CODE> which contains all the
     * details of the Protocol. This will be called from <CODE>ProtocolBaseWindow</CODE>.
     *
     * @return <CODE>ProtocolInfoBean</CODE> with all the protocol details.
     */
    public ProtocolInfoBean getProtocolInfoBean(){
        return protocolInfo;
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
    
    private void registerLockObserver( Observer observer) {
        lockObservable.addObserver( observer );
    }
    
    
    //prps start jan 06 2004
    // This method will check if the protocol needs to be saved with a
    // new sequence number when opened in modify mode
    private void checkForNewSequenceNumber() {
        if (functionType == MODIFY_MODE) {
            if (protocolId != null) {
                String connectTo = CoeusGuiConstants.CONNECTION_URL+ PROTOCOL_SERVLET;
                char CHECK_NEW_SEQ_NUMBER = 'O' ;
                RequesterBean request = new RequesterBean();
                request.setFunctionType(CHECK_NEW_SEQ_NUMBER);
                request.setId(protocolId);
                AppletServletCommunicator comm
                = new AppletServletCommunicator(connectTo, request);
                comm.send();
                ResponderBean response = comm.getResponse();
                if (response == null) {
                    response = new ResponderBean();
                    response.setResponseStatus(false);
                    response.setMessage(coeusMessageResources.parseMessageKey(
                    "server_exceptionCode.1000"));
                }
                
                if (response.isSuccessfulResponse()) { // set the flag to save the record (even though protocol record is not edited)
                    this.saveRequired = true ;
                    protocolMaintenance.setSaveRequired(true) ;
                }
            }// end if protocolid = null
        }// end if functionType
        
    }
    //prps end jan 06 2004
    
    
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
    private void setValues(Vector dataObjects) throws Exception{
        // get the bean objects from the vector
        if (dataObjects != null) {
            if (functionType == DISPLAY_MODE ){
                //btnSaveProtocol.setEnabled(false);
                protocolInfo = (ProtocolInfoBean) dataObjects.elementAt(0);
                sequenceId = protocolInfo.getSequenceNumber();
                //parameters = (Vector) dataObjects.elementAt(1);// Commented to use CoeusLabel instead Module Parameter XML file Start :02-Sep-2005
                //referenceParameters = (Vector) dataObjects.elementAt(2);// Commented to use CoeusLabel instead Module Parameter XML file Start :02-Sep-2005
                //Coeus Enhancement case #1797 start
                   ///////////////////////////////
              //  if((dataObjects.elementAt(1))!= null)
                Boolean submitEnabled = (Boolean)dataObjects.elementAt(1);
                boolean isAuthorised = false;
                if(submitEnabled != null)               
                   isAuthorised =  submitEnabled.booleanValue();
                //Modified for case 2785 - Routing enhancement - start
                //If the protocol status is any of the submitted statuses enable 
                // approve and disable submit for review.
                 if(submitEnabledProtocolStatusCodes.contains(new Integer(protocolInfo.getProtocolStatusCode())) && isAuthorised == true){
                    protocolSubmit.setEnabled(true) ;
                    mnuItmApprove.setEnabled(false);
                 }else{
                    protocolSubmit.setEnabled(false) ;
                 }
                ProtocolSubmissionInfoBean protocolSubmissionInfoBean = 
                        getSubmissionInfoForSequence();
                if(protocolSubmissionInfoBean!=null &&
                        protocolSubmissionInfoBean.getSubmissionStatusCode() == ROUTING_IN_PROGRESS){
                        mnuItmApprove.setEnabled(true);
                } else if(isRoutingDetailsPresent()){
                    mnuItmApprove.setEnabled(true);
                }
                if(submitEnabledProtocolStatusCodes.contains(new Integer(protocolInfo.getProtocolStatusCode()))){
                    mnuItmShowRouting.setEnabled(true);
                }else{
                    mnuItmShowRouting.setEnabled(false);
                }
                //Modified for case 2785 - Routing enhancement - end
               // Vector dataObjects = null;
               /* String connectTo = CoeusGuiConstants.CONNECTION_URL+ PROTOCOL_SERVLET;
                RequesterBean request = new RequesterBean();
                request.setFunctionType('h');
                request.setId(protocolId);
                request.setUserName(mdiForm.getUserName());
                 AppletServletCommunicator comm
                    = new AppletServletCommunicator(connectTo, request);
                comm.send();
                ResponderBean response = comm.getResponse();
                if (response == null) {
                    response = new ResponderBean();
                    response.setResponseStatus(false);
                    response.setMessage(coeusMessageResources.parseMessageKey(
                    "server_exceptionCode.1000"));
                }
                if (response.getDataObject()!= null) {
                  Object dataObject = response.getDataObject();
                  
                  
                  Boolean isAuthorized =(Boolean)dataObject;                  
                 
                 
                    
                    
                    
                    
                } 
                    
                
               
                
                */
                
                //Coeus Enhancement case #1797 end
                //Case 1787 Start 18
                // Protocol Fund source Types information
                protocolFundsourceTypes = (Vector) dataObjects.elementAt(4);
                Boolean hasModifyright = (Boolean)dataObjects.elementAt(5);
                setCanModifyInDisplay(hasModifyright.booleanValue());
                //Case 1787 End 18
                
                 //Case 2026 Start
                if(dataObjects !=null && dataObjects.size()>6 ){
                Boolean hasFundingSrcModifyright = (Boolean)dataObjects.elementAt(6);
                setFundingSrcModifyInDisplay(hasFundingSrcModifyright.booleanValue());                
                }
                //Case 2026 End               
                
                //Added for case#3089 - Special Review Tab made editable in display mode - start          
                protocolSpecialReviewCodes = (Vector)dataObjects.elementAt(7);                               
                protocolApprovalCodes = (Vector)dataObjects.elementAt(8);                
                //Added for case#3089 - Special Review Tab made editable in display mode - end
                //Case 4590 : Changes in special review being wiped out after an amendment is approved - Protocol : Start
                Boolean canModifySpecialReview = (Boolean)dataObjects.elementAt(9);
                setCanModifySpRevInDisplay(canModifySpecialReview.booleanValue());
                cvParameters = (CoeusVector)dataObjects.lastElement();
                //4590 end
             ///////////////////////////////////
            }else {
                ProtocolSubmissionInfoBean protocolSubmissionInfoBean = 
                        getSubmissionInfoForSequence();
                if(protocolSubmissionInfoBean!=null &&
                        protocolSubmissionInfoBean.getSubmissionStatusCode() == ROUTING_IN_PROGRESS){
                        mnuItmApprove.setEnabled(true);
                }
                //Modif
                btnSaveProtocol.setEnabled(true);
                protocolTypes = (Vector) dataObjects.elementAt(0);
                // protocol Status
                protocolStatus = (Vector) dataObjects.elementAt(1);
                // Protocol Correspondent Types information
                protocolCorrespondentTypes  = (Vector) dataObjects.elementAt(2);
                // Protocol Fund source Types information
                protocolFundsourceTypes = (Vector) dataObjects.elementAt(3);
                
                // available vulnerable subjects information
                protocolVulnerableSubjects = (Vector) dataObjects.elementAt(4);
                
                // Protocol SpecialReviewCodes information
                protocolSpecialReviewCodes = (Vector) dataObjects.elementAt(5);
                
                // available ApprovalCodes subjects information
                protocolApprovalCodes = (Vector) dataObjects.elementAt(6);
                
                // Validating vector for special review
                vecSRValidateData = (Vector) dataObjects.elementAt(7);
                //COEUSQA-2320_Show in Lite for Special Review in Code table_Start
                vecSRValidateData = vecSRValidateData == null ? new Vector(): vecSRValidateData;
                //COEUSQA-2320_Show in Lite for Special Review in Code table_End
                
                // Affilaition List in ComboBoxBean list
                vecAffiliation = (Vector) dataObjects.elementAt(8);
                orgTypes = (Vector) dataObjects.elementAt(9);
                //parameters = (Vector) dataObjects.elementAt(10);// Commented to use CoeusLabel instead Module Parameter XML file Start :02-Sep-2005
                
                referenceNumberTypes = (Vector) dataObjects.elementAt(11);
                //referenceParameters = (Vector) dataObjects.elementAt(12);// Commented to use CoeusLabel instead Module Parameter XML file Start :02-Sep-2005
                if(functionType != ADD_MODE){
                    // protocol details
                    protocolInfo = (ProtocolInfoBean) dataObjects.elementAt(13);
                    sequenceId = protocolInfo.getSequenceNumber();
                    if( functionType == COPY_MODE ) {
                        userAndRoleDetails = ( Vector ) dataObjects.elementAt( 14 );
                    }
                }else{
                    userAndRoleDetails = ( Vector ) dataObjects.elementAt( 13 );
                    protCustomElements = ( Vector ) dataObjects.elementAt( 14 );
                }
              
                 cvParameters = (CoeusVector)dataObjects.lastElement();
//                if(functionType == COPY_MODE){
                if(functionType == COPY_MODE || functionType == 'P'){
                // When trying to Copy Protocol (function type 'P')
                    //referenceId = "";
                    //added by ravi to reset application date to todays date and
                    // to remove approval date and expiration date - START
                    //Modified for Case#3940 In premium the copied protocol with the original protocols approval date - start
                    String protoNumber = protocolInfo.getProtocolNumber();
                    protoNumber = protoNumber != null || !protoNumber.equals("") ? protoNumber : "";
                    //Modified for Case#3940 In premium the copied protocol with the original protocols approval date - end
                    protocolInfo.setApplicationDate(new java.sql.Date(
                    new java.util.Date().getTime()));
                    protocolInfo.setApprovalDate(null);
                    protocolInfo.setExpirationDate(null);
                    protocolInfo.setLastApprovalDate(null);
                    // added by ravi -
                    protocolInfo.setProtocolNumber(null);
                    //prps start - this will set the action tab to zero rows when a protocol is copied
//                    protocolInfo.setProtocolStatusDesc("Pending/Progress") ;
                    //Modified for Case#3940 In premium the copied protocol with the original protocols approval date - Start
//                    protocolInfo.setProtocolStatusDesc("Pending/In Progress");                    
//                    protocolInfo.setProtocolStatusCode(1) ;
                    if(!protoNumber.equals("") && protoNumber.length() > 10){
                        if(protoNumber.charAt(10) == 'A'){
                            protocolInfo.setProtocolStatusDesc("Amendment In Progress");                    
                            protocolInfo.setProtocolStatusCode(105) ;                            
                        }else if(protoNumber.charAt(10) == 'R'){
                            protocolInfo.setProtocolStatusDesc("Renewal In Progress");                    
                            protocolInfo.setProtocolStatusCode(106) ;
                        }
                    }else{
                        protocolInfo.setProtocolStatusDesc("Pending/In Progress");                    
//                        protocolInfo.setProtocolStatusCode(1) ;
                        protocolInfo.setProtocolStatusCode(100) ;
                    }
                    //Modified for Case#3940 In premium the copied protocol with the original protocols approval date - End
                    protocolInfo.setActions(null);
                    //prps end
                }else if( functionType == CoeusGuiConstants.NEW_AMENDMENT ) {
                    /*
                     *Added by Geo on 10-24-2006
                     *Check for any pending renewal or ammendment before creating new one
                     */
                    if(protocolInfo.isPendingAmmendRenewal()){
                        if(CoeusOptionPane.showQuestionDialog(
                            coeusMessageResources.parseMessageKey("protocol_amendment_exceptionCode.2001"),
                            CoeusOptionPane.OPTION_YES_NO,
                            CoeusOptionPane.DEFAULT_NO)==CoeusOptionPane.SELECTION_NO){
                                releaseUpdateLock();
                                throw new CoeusException(CoeusGuiConstants.DO_NOT_SHOW_FORM);
                        }
                    }
                    protocolInfo.setProtocolStatusCode(AMENDMENT_STATUS_CODE);
                    //Added for Case#3940 In premium the copied protocol with the original protocols approval date -Start
                    protocolInfo.setProtocolStatusDesc("Amendment in Progress");
                    //Added for Case#3940 In premium the copied protocol with the original protocols approval date - End
                }else if( functionType == CoeusGuiConstants.RENEWAL ) {
                    if(protocolInfo.isPendingAmmendRenewal()){
                        if(CoeusOptionPane.showQuestionDialog(
                            coeusMessageResources.parseMessageKey("protocol_amendment_exceptionCode.2001"),
                            CoeusOptionPane.OPTION_YES_NO,
                            CoeusOptionPane.DEFAULT_NO)==CoeusOptionPane.SELECTION_NO){
                                releaseUpdateLock();
                                throw new CoeusException(CoeusGuiConstants.DO_NOT_SHOW_FORM);
                        }
                    }
                    protocolInfo.setProtocolStatusCode(REVISION_STATUS_CODE);
                    //Added for Case#3940 In premium the copied protocol with the original protocols approval date -Start
                    protocolInfo.setProtocolStatusDesc("Renewal in Progress");    
                    //Added for Case#3940 In premium the copied protocol with the original protocols approval date - End
                }
                /*Added for case #4278-In Comments on Action Details, indicate Renewal with Amendment for submissions created via New Am -start*/
                else if( functionType == CoeusGuiConstants.RENEWAL_WITH_AMENDMENT ) {
                    if(protocolInfo.isPendingAmmendRenewal()){
                        if(CoeusOptionPane.showQuestionDialog(
                                coeusMessageResources.parseMessageKey("protocol_amendment_exceptionCode.2001"),
                                CoeusOptionPane.OPTION_YES_NO,
                                CoeusOptionPane.DEFAULT_NO)==CoeusOptionPane.SELECTION_NO){
                            releaseUpdateLock();
                            throw new CoeusException(CoeusGuiConstants.DO_NOT_SHOW_FORM);
                        }
                    }
                    protocolInfo.setProtocolStatusCode(REVISION_STATUS_CODE);
                    protocolInfo.setProtocolStatusDesc("Renewal with Amendment  in Progress");
                }
                /*Added for case #4278-In Comments on Action Details, indicate Renewal with Amendment for submissions created via New Am -end*/
                
                //Added for case 2785 - Routing enhancements - Show Routing - starts
                if(functionType == ADD_MODE ||
                        ( protocolInfo != null &&
                        submitEnabledProtocolStatusCodes.contains(new Integer(protocolInfo.getProtocolStatusCode())))){
                    mnuItmShowRouting.setEnabled(true);
                }else{
                    mnuItmShowRouting.setEnabled(false);
                }
                //Added for case 2785 - Routing enhancements - Show Routing - ends
            }
            //modified for Coeus Enhancement case:#1787 step 5 start
            // added to hold proto related projects by manoj
          /*  if(protocolInfo != null ){
                setRelatedProjectsData(protocolInfo.getRelatedProjects());
                vecProtoRelProjTemp = protocolInfo.getRelatedProjects();
            }
           */
            //modified for Coeus Enhancement case:#1787 step 3 ends
            
        }
    }
    /**
     * this method connect to the server and gets the Protocol details
     * for the specified id.
     *
     * @param protocolId Protocol Id
     * @return Vector contain the Protocol details.
     */
    private Vector getProtocolDetails(String protocolId) throws Exception {
        Vector dataObjects = null;
        String connectTo = CoeusGuiConstants.CONNECTION_URL+ PROTOCOL_SERVLET;
        RequesterBean request = new RequesterBean();
        request.setFunctionType(functionType);
        if(functionType == 'P'){
            request.setFunctionType('E');
        }
        request.setId(protocolId);
      
        AppletServletCommunicator comm
        = new AppletServletCommunicator(connectTo, request);
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
        } else {
            // added by manoj to display the protocol action & acthorization messages as information message
            if(response.getDataObject() != null ){
                Object obj = response.getDataObject();
                if(obj instanceof CoeusException){
                    throw (CoeusException)obj;
                }else{
                    throw new Exception(response.getMessage());
                }
            }else{
                if (response.isLocked()) {
            /* the row is being locked by some other user
             */
                    setModifiable(false);
                    //System.out.println("funcType:"+functionType);
                    if( functionType == MODIFY_MODE ) {
                        if( requestedModule == CoeusGuiConstants.PROTOCOL_DETAIL_CODE ) {
                            throw new Exception(
                            "protocol_row_clocked_exceptionCode.666666");
                        }else if( requestedModule == CoeusGuiConstants.PROTOCOL_AMENDMENT_CODE ) {
                            throw new Exception("amend_row_locked_exceptionCode.888888");
                        }else if( requestedModule == CoeusGuiConstants.PROTOCOL_RENEWAL_CODE ) {
                            throw new Exception("renewal_row_locked_exceptionCode.000000");
                        }
                    }else if( functionType == CoeusGuiConstants.NEW_AMENDMENT ) {
                        //System.out.println("entered new amend excep");
                        throw new Exception("new_amend_row_locked_exceptionCode.11111");
                    }else if( functionType == CoeusGuiConstants.RENEWAL ) {
                        //System.out.println("entered new revision excep");
                        throw new Exception("new_renewal_row_locked_exceptionCode.22222");
                    }
                    
                }else {
                    throw new Exception(response.getMessage());
                }
            }
        }
        return dataObjects;
    }
    
    /**
     * initialize the components.
     * Creates a panel and add the components.
     */
    JComponent createProtocolPanel() throws Exception{
        pnlForm = new JPanel();
        pnlForm.setLayout(new BorderLayout());
        pnlForm.add(createForm(), BorderLayout.CENTER);
        pnlForm.setSize( 650, 300 );
        pnlForm.setLocation(200, 200);
        return pnlForm;
    }
    
    private CoeusMenu getProtocolEditMenu() {
        CoeusMenu mnuProtocol = null;
        Vector fileChildren = new Vector();
        
        protocolRoles = new CoeusMenuItem("Protocol Roles", null, true, true);
        protocolRoles.setMnemonic('P');
        protocolRoles.addActionListener(this);
                
        //prps start
        submissionDetails = new CoeusMenuItem("View Submission Details", null, true, true);
        submissionDetails.setMnemonic('V');
        submissionDetails.addActionListener(this);
        if( functionType == COPY_MODE ){
            submissionDetails.setEnabled(false);
        }
        
        references = new CoeusMenuItem("References", null, true, true);
        references.setMnemonic('R');
        references.addActionListener(this);
        
        protocolSummary = new CoeusMenuItem("View Summary", null, true, true);
        protocolSummary.setMnemonic('S');
        protocolSummary.addActionListener(this);
        
        //Added for Coeus 4.3 enhancement PT ID:2210 - View Protocol History start
        protocolHistory = new CoeusMenuItem("View History", null, true, true);
        protocolHistory.setMnemonic('H');
        protocolHistory.addActionListener(this);
        //Added for Coeus 4.3 enhancement PT ID:2210 - View Protocol History end
        
        //Commented for Case 3552 - IRB Attachments - start
        //Added for Protocol Upload Documents start 3
        //Modified for Coeus 4.3 enhancement
//        mnuItmUploadDoc = new CoeusMenuItem("Attachments",null,true,true);
//        mnuItmUploadDoc.setMnemonic('A');
//        mnuItmUploadDoc.addActionListener(this);
        //Added for Protocol Upload Documents end 3
        //Commented for Case 3552 - IRB Attachments - end
        
        //Added questionnaire - start 2
        mnuItmQuestionnaire = new CoeusMenuItem("Questionnaire...",null,true,true);
        mnuItmQuestionnaire.setMnemonic('Q');
        mnuItmQuestionnaire.addActionListener(this);
        //Added questionnaire - end 2
        //modified for Coeus Enhancement case:#1787 step 5 start
        // Added to link awards and proposals by Manoj Kumar
        /*relatedProjects = new CoeusMenuItem("Related Projects", null, true, true);
        relatedProjects.setMnemonic('J');
        relatedProjects.addActionListener(this);
        //prps end
        */
        //modified for Coeus Enhancement case:#1787 step 5 ends
        //prps start dec 22 2003
        printAdhoc = new CoeusMenuItem("Generate Correspondence",null,true,true);
        printAdhoc.setMnemonic('C');
        printAdhoc.addActionListener(this);
        //prps end dec 22 2003
        
        //Added for Coeus4.3 Enhancement case:#2214 Email notitication
        sendMail = new CoeusMenuItem("Send Email",null,true,true);
        sendMail.setMnemonic('E');
        sendMail.addActionListener(this);
        
        
        fileChildren.add(protocolRoles);

        //prps start
        fileChildren.add(submissionDetails) ;
        //prps end
        fileChildren.add(references) ;
        fileChildren.add(protocolSummary);
        //Added for Coeus 4.3 enhancement PT ID:2210 - View Protocol History start
        fileChildren.add(protocolHistory);
        //Added for Coeus 4.3 enhancement PT ID:2210 - View Protocol History end
        fileChildren.add(SEPERATOR);
        //modified for Coeus Enhancement case:#1787 step 6 start
        /* Added By Manoj to display Related Projects */
       /* fileChildren.add(relatedProjects) ;
        fileChildren.add(SEPERATOR);
        **/
        //modified for Coeus Enhancement case:#1787 step 3 ends
        //prps start dec 22 2003
        fileChildren.add(printAdhoc) ;
        //prps end dec 22 2003
        
        //Commented for Case 3552 - IRB Attachments - start
        //Added for Protocol Upload Documents start 4
        //fileChildren.add(mnuItmUploadDoc) ;
        //Added for Protocol Upload Documents end 4
        //Commented for Case 3552 - IRB Attachments - end
        
        //Added for Protocol Upload Document start 3
        fileChildren.add(mnuItmQuestionnaire);
        //Added for Protocol Upload Document end 3
        //Added for Coeus4.3 Enhancement case:#2214 Email notitication
        fileChildren.add(sendMail);
                
        mnuProtocol = new CoeusMenu("Edit", null, fileChildren, true, true);
        mnuProtocol.setMnemonic('E');
        
        return mnuProtocol;
        
    }
    
    /**
     * Constructs Protocol Actions menu with  menu item Submit for Review.
     * To submit a protocol user has to click this sub menu.
     * @return CoeusMenu Protocol Actions menu which will be added next to File
     * menu.
     */
    private CoeusMenu getProtocolActionsMenu() {
        CoeusMenu mnuProtocol = null;
        Vector fileChildren = new Vector();
        
        //Added for Case 2785 - Routing enhancement - start
        // COEUSDEV-319: Premium - Change menu label in Protocol window - Protocol Actions --> Approve
//        mnuItmApprove = new CoeusMenuItem("Approve", null, true, true);
        mnuItmApprove = new CoeusMenuItem("Approval/Rejection", null, true, true);
        mnuItmApprove.setMnemonic('v');
        mnuItmApprove.addActionListener(this);
        mnuItmApprove.setEnabled(false);
        
        mnuItmShowRouting = new CoeusMenuItem("Show Routing", null, true, true);
        mnuItmShowRouting.setMnemonic('h');
        mnuItmShowRouting.addActionListener(this);
        mnuItmShowRouting.setEnabled(false);
        //Added for Case 2785 - Routing enhancement - end
        
        //Added for the case# COEUSQA-1403 Implement validation based on rules in protocols-start
        protoValidationChecks = new CoeusMenuItem("Validation Checks",null,true,true);
        protoValidationChecks.setMnemonic('i');
        protoValidationChecks.addActionListener(this);
        //Added for the case# COEUSQA-1403 Implement validation based on rules in protocols-end
        
        
        protocolSubmit = new CoeusMenuItem("Submit for Review", null, true, true);
        protocolSubmit.setMnemonic('S');
        protocolSubmit.addActionListener(this);
        
        //Added by Vyjayanthi
        //Start
        processAction = ProcessAction.getInstance();
        // Added for COEUSQA-2692_Allow an investigator to abandon an IRB protocol that has never been approved_start
        protocolAbandon = new CoeusMenuItem("Abandon Protocol", null, true, true);
        protocolAbandon.setMnemonic('A');
        protocolAbandon.addActionListener(this);
        protocolAbandon.setActionCommand( "" + ACTION_PROTOCOL_ABANDON);
        // Added for COEUSQA-2692_Allow an investigator to abandon an IRB protocol that has never been approved_end

        protocolWithdraw = new CoeusMenuItem("Withdraw Submission", null, true, true);
        protocolWithdraw.setMnemonic('W');
        protocolWithdraw.addActionListener(this);
        protocolWithdraw.setActionCommand( "" + ACTION_PROTOCOL_WITHDRAWN);
        
        protocolRequestToTerminate = new CoeusMenuItem("Request to Terminate", null, true, true);
        protocolRequestToTerminate.setMnemonic('T');
        protocolRequestToTerminate.addActionListener(this);
        protocolRequestToTerminate.setActionCommand( "" + ACTION_REQUEST_FOR_TERMINATION);
        
        protocolRequestSuspension = new CoeusMenuItem("Request for Suspension", null, true, true);
        protocolRequestSuspension.setMnemonic('U');
        protocolRequestSuspension.addActionListener(this);
        protocolRequestSuspension.setActionCommand( "" + ACTION_REQUEST_FOR_SUSPENSION);
        
        protocolRequestToClose = new CoeusMenuItem("Request to Close", null, true, true);
        protocolRequestToClose.setMnemonic('C');
        protocolRequestToClose.addActionListener(this);
        protocolRequestToClose.setActionCommand( "" + ACTION_REQUEST_TO_CLOSE);
        
        protocolTerminated = new CoeusMenuItem("Terminate", null, true, true);
        protocolTerminated.setMnemonic('E');
        protocolTerminated.addActionListener(this);
        protocolTerminated.setActionCommand( "" + ACTION_PROTOCOL_TERMINATED);
        
        protocolSuspend = new CoeusMenuItem("Suspend", null, true, true);
        protocolSuspend.setMnemonic('P');
        protocolSuspend.addActionListener(this);
        protocolSuspend.setActionCommand( "" + ACTION_PROTOCOL_SUSPEND);
        
        protocolSuspendByDSMB = new CoeusMenuItem("Suspend By DSMB", null, true, true);
        protocolSuspendByDSMB.setMnemonic('Y');
        protocolSuspendByDSMB.addActionListener(this);
        protocolSuspendByDSMB.setActionCommand( "" + ACTION_PROTOCOL_SUSPEND_BY_DSMB);

        protocolExpire = new CoeusMenuItem("Expire", null, true, true);
        protocolExpire.setMnemonic('X');
        protocolExpire.addActionListener(this);
        protocolExpire.setActionCommand( "" + ACTION_PROTOCOL_EXPIRE);
        
        protocolClosed = new CoeusMenuItem("Close", null, true, true);
        protocolClosed.setMnemonic('L');
        protocolClosed.addActionListener(this);
        protocolClosed.setActionCommand( "" + ACTION_PROTOCOL_CLOSED);
        
        requestCloseEnrollment = new CoeusMenuItem("Request to Close Enrollment", null, true, true);
        requestCloseEnrollment.setMnemonic('o');
        requestCloseEnrollment.addActionListener(this);
        requestCloseEnrollment.setActionCommand( "" + ACTION_REQUEST_TO_CLOSE_ENROLMENT);
        
        closeEnrollment = new CoeusMenuItem("Close for Enrollment", null, true, true);
        closeEnrollment.setMnemonic('m');
        closeEnrollment.addActionListener(this);
        closeEnrollment.setActionCommand( "" + ACTION_PROTOCOL_CLOSE_ENROLMENT);
        //End
        
        //Added by Jobin for the undo Last action - start
        undoLastAction = new CoeusMenuItem("Undo Last Action" ,null,true,true);
        undoLastAction.setMnemonic('A');
        undoLastAction.addActionListener(this);
        undoLastAction.setActionCommand("" + 410);
        //end

       //for adding new menu item disclosure status for coi starts
        disclosurestatus = new CoeusMenuItem("COI Disclosure Status" ,null,true,true);
        disclosurestatus.setMnemonic('Z');
        disclosurestatus.addActionListener(this);
        disclosurestatus.setActionCommand("" + 410);
        //for adding new menu item disclosure status for coi ends
        
        //Added for performing request and non request actions - start - 2        
        protocolRequestToReopen = new CoeusMenuItem("Request to Re-open Enrollment", null, true, true);
        protocolRequestToReopen.setMnemonic('R');
        protocolRequestToReopen.addActionListener(this);
        protocolRequestToReopen.setActionCommand( "" + ACTION_REQUEST_TO_REOPEN_ENROLLMENT);   
        
        protocolRequestToDA = new CoeusMenuItem("Request for Data Analysis", null, true, true);
        protocolRequestToDA.setMnemonic('D');
        protocolRequestToDA.addActionListener(this);
        protocolRequestToDA.setActionCommand( "" + ACTION_REQUEST_TO_DATA_ANALYSIS);         
        
        protocolNotify = new CoeusMenuItem("Notify IRB", null, true, true);
        protocolNotify.setMnemonic('N');
        protocolNotify.addActionListener(this);
        protocolNotify.setActionCommand( "" + ACTION_NOTIFY_IRB);         
        
        protocolReopen = new CoeusMenuItem("Re-open Enrollment", null, true, true);
        protocolReopen.setMnemonic('r');
        protocolReopen.addActionListener(this);
        protocolReopen.setActionCommand( "" + ACTION_REOPEN_ENROLLMENT);  
        
        protocolDA = new CoeusMenuItem("Data Analysis", null, true, true);
        protocolDA.setMnemonic('d');
        protocolDA.addActionListener(this);
        protocolDA.setActionCommand( "" + ACTION_DATA_ANALYSIS);                 
        //Added for performing request and non request actions - end - 2

//adding menu item Send Notification..start
        notification = new CoeusMenuItem("Send Notification" ,null,true,false);
        notification.setMnemonic('w');
        notification.addActionListener(this);
        notification.setActionCommand("" + 410);
//adding menu item Send Notification..ends
        //prps new code start
        // this code will disable the menu iten submit for Review when the protocol screen
        // is opened in display mode
        if (functionType == DISPLAY_MODE ) {
           protocolSubmit.setEnabled(false) ;
        }
        else {
            protocolSubmit.setEnabled(true) ;
        }
        
        //prps new code end
        //Modified for case 2785 - Routing enhancement - start
        fileChildren.add(mnuItmApprove);
        fileChildren.add(mnuItmShowRouting);
        //Added for the case# COEUSQA-1403 Implement validation based on rules in protocols-start
        fileChildren.add(protoValidationChecks);
        //Added for the case# COEUSQA-1403 Implement validation based on rules in protocols-end
        fileChildren.add(protocolSubmit);
        fileChildren.add(SEPERATOR);
        //Modified for case 2785 - Routing enhancement - end
        //Added by Vyjayanthi
        //Start
        // Added for COEUSQA-2692_Allow an investigator to abandon an IRB protocol that has never been approved_start
        fileChildren.add(protocolAbandon);
        // Added for COEUSQA-2692_Allow an investigator to abandon an IRB protocol that has never been approved_end        
        fileChildren.add(protocolWithdraw);
        // fileChildren.add(protocolRequestToTerminate); //prps commented it on feb 05 2004
        fileChildren.add(protocolRequestSuspension);
        fileChildren.add(protocolRequestToClose);
        fileChildren.add(requestCloseEnrollment);
        //Added for performing request and non request actions - start - 3
        fileChildren.add(protocolRequestToReopen);
        fileChildren.add(protocolRequestToDA);    
        //Added for performing request and non request actions - end - 3        
        fileChildren.add(SEPERATOR);
        fileChildren.add(protocolTerminated);
        fileChildren.add(protocolSuspend);
        fileChildren.add(protocolSuspendByDSMB);
        fileChildren.add(protocolExpire);
        fileChildren.add(protocolClosed);
        fileChildren.add(closeEnrollment);
        //Added for performing request and non request actions - start - 4
        fileChildren.add(protocolNotify);
        fileChildren.add(protocolReopen);
        fileChildren.add(protocolDA);
        //Added for performing request and non request actions - end - 4        
        fileChildren.add(undoLastAction);
        fileChildren.add(disclosurestatus);
        int sendNotifFlag=checkSendNotificationEnabled();
        notification.setEnabled(false);
        if(sendNotifFlag==1){
            fileChildren.add(notification);
        }
        else if(sendNotifFlag==2){
            fileChildren.add(notification);
            notification.setEnabled(true);
        }
        //End
        
        mnuProtocol = new CoeusMenu("Protocol Actions", null, fileChildren, true, true);
        mnuProtocol.setMnemonic('t');
        return mnuProtocol;
        
    }
    
    private JToolBar getProtocolToolBar() {
        JToolBar toolbar = new JToolBar();
        
        btnSaveProtocol = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.SAVE_ICON)),
        null,"Save");
        
        
        btnProtocolRoles = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.USER_ROLES_ICON)),
        null,"Protocol Roles");
        
        btnSummary = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.ENABLED_SUMMARY_ICON)),
        null, "Summary");
        
        btnSummary.setDisabledIcon(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.DISABLED_SUMMARY_ICON)));
        
        //Case#3091 - IRB - generate a protocol summary pdf - Start
        btnPrintSummary = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.PRINT_ICON)),
        null, "Print Protocol Notice");
        //Case#3091 - End
        
        btnCloseProtocol = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.CLOSE_ICON)),
        null,"Close IRB Protocol");
        
        btnMedusa = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.MEDUSA_ICON)),
        null,"Medusa");
        
        sendEmail = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.EMAIL_ICON)), null, "Send Mail Notification");
        
        btnSaveProtocol.setMnemonic('s');
        
        btnProtocolRoles.setMnemonic('r');
        
        btnSummary.setMnemonic('u');
        
        sendEmail.setMnemonic('e');
        
        btnCloseProtocol.setMnemonic('c');
        btnSaveProtocol.addActionListener(this);
        btnSummary.addActionListener( this );
        btnCloseProtocol.addActionListener(this);
        btnProtocolRoles.addActionListener(this);
        sendEmail.addActionListener(this);
        //Case#3091 - IRB - generate a protocol summary pdf - Start
        btnPrintSummary.addActionListener(this);
        //Case#3091 - End
        //COEUSQA:2653 - Add Protocols to Medusa - Start
        btnMedusa.addActionListener(this);
        //COEUSQA:2653 - End
        
        toolbar.add(btnProtocolRoles);
        toolbar.add(btnSaveProtocol);
        toolbar.add(btnSummary);
        toolbar.add(btnPrintSummary);//3091
        toolbar.add(sendEmail);        
        toolbar.add(btnMedusa);
        toolbar.addSeparator();
        toolbar.add(btnCloseProtocol);                
        
        
        toolbar.setFloatable(false);
        return toolbar;
    }
    
    //Added by Vyjayanthi - 30/07/2004 for IRB Enhancement
    /** Disables all Action menu items
     * Called in Administrative Correction mode
     */
    private void disableActionMenuItems(){
        //Added for case 2785 - Routing enahncement - start
        mnuItmApprove.setEnabled(false);
        mnuItmShowRouting.setEnabled(false);
        //Added for case 2785 - Routing enahncement - end
        protocolSubmit.setEnabled(false);
        // Added for COEUSQA-2692_Allow an investigator to abandon an IRB protocol that has never been approved_start
        protocolAbandon.setEnabled(false);
        // Added for COEUSQA-2692_Allow an investigator to abandon an IRB protocol that has never been approved_end
        protocolWithdraw.setEnabled(false);
        protocolRequestToClose.setEnabled(false);
        protocolRequestSuspension.setEnabled(false);
        protocolClosed.setEnabled(false);
        protocolTerminated.setEnabled(false);
        protocolSuspend.setEnabled(false);
        protocolSuspendByDSMB.setEnabled(false);
        protocolExpire.setEnabled(false);
        requestCloseEnrollment.setEnabled(false);
        closeEnrollment.setEnabled(false);
        //Added for performing request and non request actions - start - 5
        protocolRequestToReopen.setEnabled(false);
        protocolRequestToDA.setEnabled(false);
        protocolNotify.setEnabled(false);
        protocolReopen.setEnabled(false);
        protocolDA.setEnabled(false);        
        //Added for performing request and non request actions - end - 5        
    }
    
    /** This method is used to get the functionType.
     * This will be invoked from <CODE>ProtocolBaseWindow</CODE>.
     * @return functionType character which specifies the functionType.
     */
    public char getFunctionType(){
        return functionType;
    }
    
    /** This method is used to get the form opened mode.
     * This will be called from <CODE>ProtocolBaseWindow</CODE>.
     * @param functionType character which specifies the form opened mode.
     */
    public void setFunctionType(char functionType){
        this.functionType = functionType;
    }
    
    /** This method is used to show the <CODE>ProtocolDetailsForm</CODE>.
     */
    public void showDialogForm() throws Exception{
        if ( requestedModule != CoeusGuiConstants.PROTOCOL_DETAIL_CODE ){
            if( !isFromBaseWindow ) {
                referenceId = referenceId.substring(0,10);
                referenceId += " Version " + versionNumber;
                if( requestedModule == CoeusGuiConstants.PROTOCOL_AMENDMENT_CODE ) {
                    referenceTitle = CoeusGuiConstants.AMENDMENT_DETAILS_TITLE;
                }else{
                    referenceTitle = CoeusGuiConstants.RENEWAL_DETAILS_TITLE;
                }
                
            }else{
                if( requestedModule == CoeusGuiConstants.PROTOCOL_AMENDMENT_CODE ) {
                    referenceTitle = CoeusGuiConstants.NEW_AMENDMENT_TITLE;
                }
                /*Added for case#4278-In Comments on Action Details, indicate Renewal with Amendment for submissions created via New Am -start*/
                else if( requestedModule == CoeusGuiConstants.PROTOCOL_RENEWAL_CODE ){
                    //To check for the mode New Renewal
                    if( functionType == CoeusGuiConstants.RENEWAL){
                        referenceTitle = CoeusGuiConstants.RENEWAL_DETAILS_TITLE;
                    } else{
                        referenceTitle = CoeusGuiConstants.RENEWAL_WITH_AMENDMENT_TITLE;
                    }
                }
                /*Added for case#4278-In Comments on Action Details, indicate Renewal with Amendment for submissions created via New Am -end*/
            }
        }else{
            referenceTitle = CoeusGuiConstants.PROTOCOL_FRAME_TITLE;
            if( functionType == COPY_MODE || functionType == ADD_MODE || functionType == 'P') {
                referenceId = "";
            }
        }
        String hyphen = " - ";
        if( functionType == COPY_MODE || functionType == ADD_MODE || functionType == 'P') {
            hyphen = "";
        }
        setTitle(referenceTitle + hyphen +referenceId );
        
        //Bug Fix:1359 Start 1
        strTitle = referenceTitle + hyphen +referenceId;
        //Bug Fix:1359 End 1
        
        this.getContentPane().add(createProtocolPanel());
        
        if( requestedModule ==  CoeusGuiConstants.PROTOCOL_DETAIL_CODE ||
        (requestedModule != CoeusGuiConstants.PROTOCOL_DETAIL_CODE &&
        isFromBaseWindow ) ) {
            referenceId = protocolId;
        }
        /**
         * Updated For : REF ID 174 Change Request Sent on Feb' 14 2003.
         * Initial size of Protocol detail window is too small.
         * With the initial size, the user will not be able to see last two
         * buttons, Find Addr and Clear Addr unless they scroll down.
         * Can we make the screen little bigger so that these buttons are
         * visible by default
         *
         * Updated By Subramanya Feb' 17 2003.
         */
        //setSize( 798, 600 );
        
        /* This will catch the window closing event and
         * checks any data is modified.If any changes are done by
         * the user the system will ask for confirmation of Saving the info.
         */
        this.addVetoableChangeListener(new VetoableChangeListener(){
            public void vetoableChange(PropertyChangeEvent pce)
            throws PropertyVetoException {
                if (pce.getPropertyName().equals(
                JInternalFrame.IS_CLOSED_PROPERTY) && !isTimedOut) {
//                    CoeusInternalFrame frame = null;
                    boolean changed = (
                    (Boolean) pce.getNewValue()).booleanValue();
                    if (changed && !closed && isSaveRequired()) {
                        String msg = coeusMessageResources.parseMessageKey(
                        "saveConfirmCode.1002");
                        int confirm = CoeusOptionPane.showQuestionDialog(msg,
                        CoeusOptionPane.OPTION_YES_NO_CANCEL,
                        CoeusOptionPane.DEFAULT_YES);
                        
                        switch(confirm){
                            case(JOptionPane.YES_OPTION):
                                try{
                                    //Coeus4.3 email notification implementation - start
                                    char tempFunctionType = functionType;
                                    setProtocolInfo();
                                     //COEUSQA:3315 - IACUC Protocol Actions Blanking Completed Questionnaires - Start
                                    checkQuestionnaireRevision();
                                    //COEUSQA:3315 - End
                                    //Added for case 4350 - Protocol - Creating renewals action is not sending mails - Start
                                    if(tempFunctionType == ADD_MODE) {
                                        ProtocolMailController mailController = new ProtocolMailController();
                                        synchronized(mailController) {
                                            mailController.sendMail(ACTION_NEW_PROTOCOL, protocolInfo.getProtocolNumber(), protocolInfo.getSequenceNumber());
                                            //100 - Protocol Creation
                                        }
                                    }
                                    else if(tempFunctionType == COPY_MODE) {
                                        if(requestedModule == CoeusGuiConstants.PROTOCOL_AMENDMENT_CODE) {
                                            ProtocolMailController mailController = new ProtocolMailController();
                                            synchronized(mailController) {
                                                mailController.sendMail(ACTION_NEW_AMENDMENT, protocolInfo.getProtocolNumber(), protocolInfo.getSequenceNumber());
                                                //103 - Amendment Creation
                                            }
                                        }else if(requestedModule == CoeusGuiConstants.PROTOCOL_RENEWAL_CODE) {
                                            ProtocolMailController mailController = new ProtocolMailController();
                                            synchronized(mailController) {
                                                mailController.sendMail(ACTION_NEW_RENEWAL, protocolInfo.getProtocolNumber(), protocolInfo.getSequenceNumber());
                                                //102 - Renewal Creation
                                            }
                                        }else if(requestedModule == CoeusGuiConstants.PROTOCOL_AMENDMENT_WITH_RENEWAL_CODE) {
                                            ProtocolMailController mailController = new ProtocolMailController();
                                            synchronized(mailController) {
                                                mailController.sendMail(ACTION_NEW_RENEW_WITH_AMEND,protocolInfo.getProtocolNumber(), protocolInfo.getSequenceNumber());
                                                //117 - Renewal with Amendment Creation
                                            }
                                        }
                                    }
                                    //Added for case 4350 - Protocol - Creating renewals action is not sending mails - End
                                    //Coeus4.3 email notification implementation - end
                                    //Case 2026 Start
                                    // COEUSDEV-273: Protocol roles update error - new se & save  
                                    // Commented the below and added a new condtion, since the application was throwing 'Lock deleted by Admin ' message

//                                    if( !canFundingSrcModifyInDisplay){
//                                    releaseUpdateLock();
//                                    }
                                    //Case 2026 End
                                    //Added for case#3089 - Special Review Tab made editable in display mode - start
//                                    if( !canModifySpRevInDisplay){
//                                        releaseUpdateLock();
//                                    }
                                    if( !canModifySpRevInDisplay || !canFundingSrcModifyInDisplay ){
                                        releaseUpdateLock();
                                    }
                                    // COEUSDEV-273: Protocol roles update error - new se & save  - End

                                    //Added for case#3089 - Special Review Tab made editable in display mode - end
                                    //updated by Subramnya - Close Button fix
                                    /*frame = mdiForm.getFrame(
                                    CoeusGuiConstants.PROTOCOL_FRAME_TITLE,
                                        protocolId );*/
                                    mdiForm.removeFrame(referenceTitle, referenceId );
                                    
                                }catch (CoeusUIException coeusUIException) {
                                    cnclClose = true;
                                    CoeusOptionPane.showDialog(coeusUIException);
                                    tbdPnProtocol.setSelectedIndex(coeusUIException.getTabIndex());
                                    
                                    throw new PropertyVetoException(
                                    coeusMessageResources.parseMessageKey(
                                    "protoDetFrm_exceptionCode.1130"),null);
                                    
                                }catch(Exception ex){
                                    cnclClose = true;
                                    String exMsg = ex.getMessage();
                                    CoeusOptionPane.showWarningDialog(exMsg);
                                    throw new PropertyVetoException(
                                    coeusMessageResources.parseMessageKey(
                                    "protoDetFrm_exceptionCode.1130"),null);
                                }
                                break;
                            case(JOptionPane.NO_OPTION):
                                releaseUpdateLock();
                                saveRequired = false;
                                
                                //updated by Subramnya - Close Button fix
                                /*frame = mdiForm.getFrame(
                                CoeusGuiConstants.PROTOCOL_FRAME_TITLE );*/
                                mdiForm.removeFrame(referenceTitle, referenceId );
                                break;
                            case(JOptionPane.CANCEL_OPTION):
                            case(JOptionPane.CLOSED_OPTION):
                                cnclClose = true;
                                throw new PropertyVetoException(
                                coeusMessageResources.parseMessageKey(
                                "protoDetFrm_exceptionCode.1130"),null);
                        }
                    }else{
                        // Added by chandra to check whether cancel is clicked.
                        // the veto firing happening twise. Hence this condition.
                                          
                        //Case 1787 Start 19
                        //if(!cnclClose && functionType!=DISPLAY_MODE){
                        if(!cnclClose){
                            if(functionType == DISPLAY_MODE && canModifyInDisplay 
                                    // COEUSDEV-273: Protocol roles update error - new se & save  
                                    && isLock){
                                releaseUpdateLock();
                            }else if(functionType!=DISPLAY_MODE){
                                releaseUpdateLock();
                            }
                            //Case 1787 Start 19
                        }
                        /**
                         * Bug Fix for Selection of Protocol Detail/Protocol Base Window.
                         * Updated Subramanya Feb' 13 2003.
                         */
                    /*if(mdiForm.getFrame(
                        CoeusGuiConstants.PROTOCOL_FRAME_TITLE,
                        referenceId) == null){*/
                        if( ! cnclClose  ) {
                            mdiForm.removeFrame(referenceTitle, referenceId  );
                            cnclClose = false;
                            //isDataChanged = false;
                        }
                    }
                }
            }
        });
        if( functionType == ADD_MODE || functionType == COPY_MODE || functionType == 'P') {
            referenceId = "";
            btnSummary.setEnabled( false );
            protocolSummary.setEnabled(false);
        }else{
            btnSummary.setEnabled( true );
            protocolSummary.setEnabled(true);
        }
        //Added for Coeus 4.3 enhancement PT ID:2210 - View Protocol History start
        if( functionType == ADD_MODE || functionType == COPY_MODE || functionType == 'P' ||
                (protocolInfo!=null && protocolInfo.getSequenceNumber()==1)) {
            protocolHistory.setEnabled(false);
        }else{
            protocolHistory.setEnabled(true);
        }
        //Added for Coeus 4.3 enhancement PT ID:2210 - View Protocol History end
        if( requestedModule != CoeusGuiConstants.PROTOCOL_DETAIL_CODE ) {
            protocolMaintenance.setProtocolNumber(protocolId.substring(0,10));
        }
        mdiForm.putFrame(referenceTitle, referenceId, functionType, this);
        //System.out.println("in detail form functionType:"+functionType);
        mdiForm.getDeskTopPane().add(this);
        setVisible(true);
        setSelected(true);
        if( requestedModule == CoeusGuiConstants.PROTOCOL_DETAIL_CODE ){
            if( functionType != DISPLAY_MODE ) {
                tbdPnProtocol.setSelectedIndex(0);
                protocolMaintenance.setFocusToType();
            }
        }else {
            //Modified for case 3552 - IRB attachments - start
            //Modified for case 2176 - Risk Level Category - start
            tbdPnProtocol.setSelectedIndex(AMENDMEND_SUMMARY_TAB);
            //Modified for case 2176 - Risk Level Category - end
            //Modified for case 3552 - IRB attachments - end
            amendRenewalForm.requestInitialFocus();
        }

        //Protocol Enhancement -  Administrative Correction Start 2
        if(mode == CoeusGuiConstants.ADMINISTRATIVE_CORRECTIONS){
            displayAdminCorrComments();
        }
        //Protocol Enhancement -  Administrative Correction End 2
        
    }
    
    /** This method is used to get the panel in which the JTabbedPane is added.
     * @return JComponent reference to the panel which consists of all the
     * tabbedpane components.
     */
    public JComponent getProtocolDetailsPanel(){
        return pnlForm;
    }
  
    /** This method is used to create the tabbedpane along with the components
     * which are used in <CODE>ProtocolDetailsForm</CODE>.
     *
     * @return JTabbedPane with all the components of <CODE>ProtocolDetailsForm</CODE> added
     * to it.
     */
    public JTabbedPane createForm() throws Exception{
        
        //setValues(getProtocolDetails(PROTOCOL_NUMBER));
        
        //Modified by Vyjayanthi for IRB Enhancement - 02/08/2004 - Start
        //To set the protocol data that is already fetched in Administrative correction mode
        if( mode != null &&
        mode.equalsIgnoreCase(CoeusGuiConstants.ADMINISTRATIVE_CORRECTIONS) ){
            setValues(vecProtocolData);
        }else {
            //Contact server to get the protocol data
            setValues(getProtocolDetails(protocolId));
        }
        //Modified by Vyjayanthi for IRB Enhancement - 02/08/2004 - End
        
        tbdPnProtocol = new JTabbedPane();
        //Added for case 2176 - Risk Level Category - start
        tbdPnProtocol.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                //Modified for case 3552 - IRB attachments - start
                if(tbdPnProtocol.getSelectedIndex() == RISK_LEVEL_TAB){
                    if(protocolRiskLevelCategoryForm != null &&
                            protocolRiskLevelCategoryForm.getFunctionType() != 'P'){ 
                        if(!protocolRiskLevelCategoryForm.isDataFetched()){
                            protocolRiskLevelCategoryForm.setFormData();
                        }
                    }
                }else if(tbdPnProtocol.getSelectedIndex() == ATTACHMENTS_TAB){
                    if(protocolAttachmentsForm != null){
                        //If other attachments is in display mode, and the logged in user has
                        //administrative correction right set the function type as modify mode
                        if(protocolAttachmentsForm.getOtherFunctionType() == TypeConstants.DISPLAY_MODE){
                            //Modified for Case#4275  - upload attachments until in agenda - Start
//                            if(!checkedAdministrativeCorrection){
//                                hasAdministrativeCorrectionRight();
//                            }
//                            if(admistrativeRightPresent){
//                                protocolAttachmentsForm.setOtherFunctionType(TypeConstants.MODIFY_MODE);
//
//                            }
                            if(hasAdminCorrectionRight()){
                                protocolAttachmentsForm.setOtherFunctionType(TypeConstants.MODIFY_MODE);
                            }
                            //Case#4275 - end
                        }

                        //Added for Case#4275  - upload attachments until in agenda - Start
                        if(protocolAttachmentsForm.getFunctionType() == TypeConstants.DISPLAY_MODE){
                            if(hasModifyAttachmentRights()){
                                protocolAttachmentsForm.setFunctionType(TypeConstants.MODIFY_MODE);
                            }
                        }
                        //Case#4275 - End
                        protocolAttachmentsForm.setFormData();  
                    }
                }
                //Modified for case 3552 - IRB attachments - end
            }
        });
        //Added for case 2176 - Risk Level Category - end
//        if( functionType == CoeusGuiConstants.NEW_AMENDMENT ||
//        functionType == CoeusGuiConstants.RENEWAL ) {
        /*Added for case#4278 -In Comments on Action Details, indicate Renewal with Amendment for submissions created via New Am -start*/    
        //It checks for the New Mendment/Renewal
         if( functionType == CoeusGuiConstants.NEW_AMENDMENT ||
        functionType == CoeusGuiConstants.RENEWAL ||  functionType == CoeusGuiConstants.RENEWAL_WITH_AMENDMENT ) {
            functionType = COPY_MODE;
        }
        /*Added for case#4278 -In Comments on Action Details, indicate Renewal with Amendment for submissions created via New Am -end*/    
        //Added for coeus4.3 enhancement
        //To get all the editable modules list.
        HashMap hmModeDetails = getModeDetails();
        setOriginalProtocolInfo((ProtocolInfoBean) ObjectCloner.deepCopy(protocolInfo));
        //Code added for coeus4.3 Amendments/Renewal enhancements - starts
        if(functionType == 'P'){
            hmModeDetails.put("functionType", new Character(ADD_MODE));
            protocolInfo.setProtocolNumber("");
            //Commented for Case#3940 In premium the copied protocol with the original protocols approval date - Start
//            protocolInfo.setProtocolStatusCode(100);
            //Commented for Case#3940 In premium the copied protocol with the original protocols approval date - end
        }
        //Code added for coeus4.3 Amendments/Renewal enhancements - ends
        // create NameAddress tab
        layout = new FlowLayout(FlowLayout.LEFT);
        pnlDetail = new JPanel(layout);
        //code modified and added for coeus4.3 enhancements
        // To check whether the module is editable.    
        hmModeDetails.put("moduleId", IrbWindowConstants.GENERAL_INFO);
        protocolMaintenance
        = new ProtocolMaintenanceForm(getMode(hmModeDetails),protocolInfo);
        protocolMaintenance.setOriginalFunctionType(functionType);
        originalFunctionType = functionType;
//        protocolMaintenance
//        = new ProtocolMaintenanceForm(functionType,protocolInfo);
        //For Protocol locations
        //code modified and added for coeus4.3 enhancements
        // To check whether the module is editable.          
        hmModeDetails.put("moduleId", IrbWindowConstants.ORGANIZATION);
        protocolMaintenance.setLocationFunctionType(getMode(hmModeDetails));
        protocolMaintenance.setAvailableStatus(protocolStatus);
        protocolMaintenance.setAvailableTypes(protocolTypes);
        protocolMaintenance.setOrgTypes( orgTypes );
        //protocolMaintenance.setParameters( parameters );// Commented to use CoeusLabel instead Module Parameter XML file Start :02-Sep-2005
        //protocolMaintenance.setAvailableSubjects(protocolVulnerableSubjects);
        jscrPn = new javax.swing.JScrollPane();
        pnlDetail.add(protocolMaintenance.showProtocolMaintenanceForm(mdiForm));
        
        //Added by Vyjayanthi - 30/07/2004 for IRB Enhancement - Start
        /** Approval date and Expiration dates in a protocol should be non-editable
         * in modify mode. It must be editable when a protocol is opened in
         * modify mode for administrative corrections
         */
        if( mode != null &&
        mode.equalsIgnoreCase(CoeusGuiConstants.ADMINISTRATIVE_CORRECTIONS)) {
            protocolMaintenance.setMode(CoeusGuiConstants.ADMINISTRATIVE_CORRECTIONS);
            protocolMaintenance.enableApprovalExpirationDates(true);
            protocolMaintenance.setTraversalInAdminCorrection();
            // Case# 2562: Last Approval Date field  editable in Administrative Correction Mode 
            protocolMaintenance.enableLastApprovalDate(true);
        }else {
            protocolMaintenance.enableApprovalExpirationDates(false);
        }
        //Added by Vyjayanthi - 30/07/2004 for IRB Enhancement - End
        
        jscrPn.setViewportView(pnlDetail);
        if( functionType == COPY_MODE ){
            protocolMaintenance.setProtocolNumber("");
            protocolInfo.setSequenceNumber(0);
        }
        
        pnlInv = new JPanel(layout);
        if( functionType != ADD_MODE ){
            
            selectedSubjects = protocolInfo.getVulnerableSubjectLists();
            protInvestigators = protocolInfo.getInvestigators();
            protKeyStudyPersonnel = protocolInfo.getKeyStudyPersonnel();
            protCorrespondents = protocolInfo.getCorrespondetns();
            protAOR = protocolInfo.getAreaOfResearch();
            protFundingSource = protocolInfo.getFundingSources();
            protActions = protocolInfo.getActions();
            protocolSpecialReviewData = protocolInfo.getSpecialReviews();
            protocolVecNotes = protocolInfo.getVecNotepad();
            protCustomElements = protocolInfo.getCustomElements();
            protReferenceData = protocolInfo.getReferences();
            exisitingUserRoles = protocolInfo.getUserRolesInfoBean();
            //modified for Coeus Enhancement case:#1787 step 7 
           // protRelProjects = protocolInfo.getRelatedProjects();            
            
            if( exisitingUserRoles != null ) {
                copyOfExistingUserRoles = new Vector();
                UserRolesInfoBean usrBean,tmpBean;
                for( int indx = 0; indx < exisitingUserRoles.size(); indx++) {
                    tmpBean = ( UserRolesInfoBean )exisitingUserRoles.get(indx);
                    usrBean = new UserRolesInfoBean();
                    usrBean.setHasChildren( tmpBean.hasChildren() );
                    usrBean.setIsRole( tmpBean.isRole() );
                    usrBean.setRoleBean( tmpBean.getRoleBean() );
                    usrBean.setUserBean( tmpBean.getUserBean() );
                    usrBean.setUsers( tmpBean.getUsers() );
                    usrBean.setUpdateTimestamp(tmpBean.getUpdateTimestamp());
                    usrBean.setUpdateUser(tmpBean.getUpdateUser());
                    copyOfExistingUserRoles.add( usrBean );
                }
            }
            
        }
        //code modified and added for coeus4.3 enhancements
        // To check whether the module is editable.          
        hmModeDetails.put("moduleId", IrbWindowConstants.INVESTIGATOR);
//        protocolInv = new ProtocolInvestigatorsForm(functionType,
//        protInvestigators, vecAffiliation);
        protocolInv = new ProtocolInvestigatorsForm(getMode(hmModeDetails),
        protInvestigators, vecAffiliation);        
        pnlInv.add(protocolInv.showProtocolInvestigatorsForm(mdiForm));
        JScrollPane jscrPnInvestigator = new JScrollPane();
        jscrPnInvestigator.setViewportView( pnlInv );
        
        pnlKey = new JPanel(layout);
        //code modified and added for coeus4.3 enhancements
        // To check whether the module is editable.        
        hmModeDetails.put("moduleId", IrbWindowConstants.KEY_STUDY_PERSONS);
//        protocolKey = new ProtocolKeyStudyPersonnelForm(functionType,
//        protKeyStudyPersonnel, vecAffiliation);
        protocolKey = new ProtocolKeyStudyPersonnelForm(getMode(hmModeDetails),
        protKeyStudyPersonnel, vecAffiliation);
        pnlKey.add(protocolKey.showProtocolKeyStudyPersonnelForm(mdiForm));
        JScrollPane jscrPnKeyStudy = new JScrollPane();
        jscrPnKeyStudy.setViewportView( pnlKey );
        
        JPanel pnlCorr = new JPanel(layout);
        //code modified and added for coeus4.3 enhancements
        // To check whether the module is editable.        
        hmModeDetails.put("moduleId", IrbWindowConstants.CORRESPONDENTS);
//        protocolCorr = new ProtocolCorrespondenceForm(functionType,
//        protCorrespondents, protocolCorrespondentTypes );
        protocolCorr = new ProtocolCorrespondenceForm(getMode(hmModeDetails),
        protCorrespondents, protocolCorrespondentTypes);        
        protocolCorr.setParentSequenceId(sequenceId);
        pnlCorr.add(protocolCorr.showProtocolCorrespondentsForm(mdiForm));
        JScrollPane jscrPnCorrespondance = new JScrollPane();
        jscrPnCorrespondance.setViewportView( pnlCorr );
        
        JPanel pnlArea = new JPanel(layout);
        //code modified and added for coeus4.3 enhancements
        // To check whether the module is editable.            
        hmModeDetails.put("moduleId", IrbWindowConstants.AREA_OF_RESEARCH);
//        protocolArea = new ProtocolAORForm(functionType, protAOR);
        protocolArea = new ProtocolAORForm(getMode(hmModeDetails), protAOR);
        pnlArea.add(protocolArea.showProtocolAORForm(mdiForm));
        JScrollPane jscrPnAOR = new JScrollPane();
        jscrPnAOR.setViewportView( pnlArea );
        
        JPanel pnlFund = new JPanel(layout);
        
        //Case 1787 Start 20
        /*protocolFund = new ProtocolFundingSourceForm(functionType,
        protFundingSource);*/
         //Added for Case 2026 Start
        //code modified and added for coeus4.3 enhancements
        // If it is Amendment/Renewal protocol then open Funding source module in display mode 
        //Code modified for Case#3070 - Ability to change a funding source - starts
//        if(functionType != ADD_MODE && ((protocolInfo.getProtocolNumber()!=null && 
//                protocolInfo.getProtocolNumber().length()>10) || functionType == CoeusGuiConstants.AMEND_MODE)){
//            protocolFund = new ProtocolFundingSourceForm('D', new Vector());
//        }else if(functionType == TypeConstants.DISPLAY_MODE && (canModifyInDisplay || canFundingSrcModifyInDisplay ) ){
//            protocolFund = new ProtocolFundingSourceForm(MODIFY_MODE,protFundingSource);
//        }else{
//            protocolFund = new ProtocolFundingSourceForm(functionType,protFundingSource);
//        }
        hmModeDetails.put("moduleId", IrbWindowConstants.FUNDING_SOURCE);
        if(functionType == TypeConstants.DISPLAY_MODE && (protocolInfo.getProtocolNumber()!=null && 
                protocolInfo.getProtocolNumber().length()<=10) && (canModifyInDisplay || canFundingSrcModifyInDisplay) ){
            hmModeDetails.put("functionType", new Character(MODIFY_MODE));
            protocolFund = new ProtocolFundingSourceForm(getMode(hmModeDetails),protFundingSource,protocolInfo);
            hmModeDetails.put("functionType", new Character(functionType));
        } else{
            protocolFund = new ProtocolFundingSourceForm(getMode(hmModeDetails),protFundingSource,protocolInfo);
        }
        //Code modified for Case#3070 - Ability to change a funding source - ends
         //Case 2026  End
        //Case 1787 End 20        
        
        protocolFund.setAvailableFundingSourceTypes(protocolFundsourceTypes);
        
         for (int index=0;index<cvParameters.size();index++) {
            CoeusParameterBean coeusParameterBean=(CoeusParameterBean)cvParameters.elementAt(index);
            if(CoeusConstants.ENABLE_PROPOSAL_TO_PROTOCOL.equals(coeusParameterBean.getParameterName())){
                enableProposaltoProtocolLink = Integer.parseInt(coeusParameterBean.getParameterValue());
            }
            }
        protocolFund.setEnableProposalLink(enableProposaltoProtocolLink);
        pnlFund.add(protocolFund.showProtocolFundingSourceForm(mdiForm));
        JScrollPane jscrPnFunding = new JScrollPane();
        jscrPnFunding.setViewportView( pnlFund );
        
        JPanel pnlAction = new JPanel(layout);
        //Code added for coeus4.3 Amendments/Renewal enhancements - starts
        //while copying protocol no actions should be copied to the new protocol.
        if(functionType == 'P'){
            protocolAction = new ProtocolActionsForm(functionType,
            new Vector());            
        } else {
            protocolAction = new ProtocolActionsForm(functionType,
            protActions);
        }
        //Code added for coeus4.3 Amendments/Renewal enhancements - ends
        pnlAction.add(protocolAction.showProtocolActionsForm(mdiForm));
        
        
        JScrollPane jscrPnOtherActions = new JScrollPane();
        jscrPnOtherActions.setViewportView( pnlAction );
        //prps start
        javax.swing.border.TitledBorder border = new javax.swing.border.TitledBorder("Action Details");
        border.setTitleFont(CoeusFontFactory.getLabelFont()) ;
        jscrPnOtherActions.setBorder(border);
        //prps end
        
        JPanel pnlSubjects = new JPanel(layout);
        //code modified and added for coeus4.3 enhancements
        // To check whether the module is editable.            
        hmModeDetails.put("moduleId", IrbWindowConstants.SUBJECTS);
//        protocolSubjects = new ProtocolVulnerableSubjectsForm(functionType,
//        selectedSubjects);
        protocolSubjects = new ProtocolVulnerableSubjectsForm(getMode(hmModeDetails),
        selectedSubjects);        
        protocolSubjects.setAvailableSubjects(protocolVulnerableSubjects);
        pnlSubjects.add(protocolSubjects.showVulnerableSubjectsForm(mdiForm));
        JScrollPane jscrPnSubjects = new JScrollPane();
        jscrPnSubjects.setViewportView( pnlSubjects );
        
        
        // Added By Raghunath to implement protocol special review
        
        JPanel pnlSpecialReview = new JPanel(layout);
        //code modified and added for coeus4.3 enhancements
        // To check whether the module is editable.            
        hmModeDetails.put("moduleId", IrbWindowConstants.SPECIAL_REVIEW);
//        protocolSpecialReviewForm = new ProtocolSpecialReviewForm(functionType,
//        protocolSpecialReviewData);
        //Added/Modified for case#3089 - Special Review Tab made editable in display mode - start
        char spRevFunctionType = getMode(hmModeDetails);
        if(spRevFunctionType == 'D'){
            //Case 4590:Changes in special review being wiped out after an amendment is approved - Protocol
            setCanModifySpRevInDisplay(isCanModifySpRevInDisplay() && hasAdministrativeCorrectionRight());
            if(isCanModifySpRevInDisplay()){
                spRevFunctionType = CoeusGuiConstants.EDIT_MODE;
            }
            //case 4590 End
        }                
        protocolSpecialReviewForm = new ProtocolSpecialReviewForm(spRevFunctionType, protocolSpecialReviewData);
        //Added/Modified for case#3089 - Special Review Tab made editable in display mode - end
        protocolSpecialReviewForm.setApprovalTypes(protocolApprovalCodes);
        protocolSpecialReviewForm.setSpecialReviewTypeCodes(protocolSpecialReviewCodes);
        protocolSpecialReviewForm.setValidateVector(vecSRValidateData);
        pnlSpecialReview.add(protocolSpecialReviewForm.showProtocolSpecialReviewForm(mdiForm));
        JScrollPane jscrPnSpecialReviews = new JScrollPane();
        jscrPnSpecialReviews.setViewportView( pnlSpecialReview );
        
        // Added By Senthil to implement protocol Notepad
        
        JPanel pnlNotepad = new JPanel(layout);
        //code modified and added for coeus4.3 enhancements
        // To check whether the module is editable.            
//        hmModeDetails.put("moduleId", IrbWindowConstants.NOTES);
//        hmModeDetails.put("functionType", new Character('M'));
        if(functionType == 'P'){
            protocolNotepadForm = new ProtocolNotepadForm(new Vector(),functionType);
        } else {
            protocolNotepadForm = new ProtocolNotepadForm(protocolVecNotes,functionType);            
        }        
//        protocolNotepadForm = new ProtocolNotepadForm(protocolVecNotes,
//                    getMode(hmModeDetails));
        hmModeDetails.put("functionType", new Character(functionType));
        protocolNotepadForm.setProtocolNumber(protocolId);
        protocolNotepadForm.setSequenceNumber(sequenceId);
        registerLockObserver( protocolNotepadForm );
        protocolNotepadForm.registerLockObserver( this );
        // Added for COEUSQA-2692_Allow an investigator to abandon an IRB protocol that has never been approved_start
        int statusCode = 0;
        if(protocolInfo != null){
            statusCode = protocolInfo.getProtocolStatusCode();
        }
        pnlNotepad.add(protocolNotepadForm.showProtocolNotepadForm(mdiForm,statusCode));
        // Added for COEUSQA-2692_Allow an investigator to abandon an IRB protocol that has never been approved_end

        JScrollPane jscrPnNotepad = new JScrollPane();
        jscrPnNotepad.setViewportView( pnlNotepad );
        
        // Added By Raghunath to implement protocol referenceNumbers
        
        //        JPanel pnlReferenceNumbers = new JPanel(layout);
        //
        //        referenceNumberForm = new ReferenceNumberForm(functionType, protReferenceData);
        //
        //        referenceNumberForm.setReferenceTypes(referenceNumberTypes);
        //        referenceNumberForm.setVecColumnNames(referenceParameters);
        //        referenceNumberForm.preInitComponents(null, null);
        //        referenceNumberForm.setWindowType(TAB_WINDOW);
        //        pnlReferenceNumbers.add(referenceNumberForm.showReferenceNumbers(mdiForm));
        //        JScrollPane jscrPnReferenceNo = new JScrollPane();
        //        jscrPnReferenceNo.setViewportView( pnlReferenceNumbers );
        
        
        tbdPnProtocol.setFont(CoeusFontFactory.getNormalFont());
        
        //to show Amendment and renewal form
        Vector amendRenList  = null;
        //code added for coeus4.3 enhancement - starts
        //to get the editable modules
        Vector editableModules = null;
        if( protocolInfo != null ) {
            amendRenList = protocolInfo.getAmendmentRenewal();
            //System.out.println("amendRenList:"+amendRenList);
            editableModules = protocolInfo.getAmendRenewEditableModules();
        }
        //Case#4494 - In Protocol, Error on Other tab when custom elements are not defined  - Start
        //Others modules is removed from editable modules list, if there is no customelemts.
        if( protocolInfo != null ) {
            Vector amendCustomElements = protocolInfo.getCustomElements();
            //Modified for COEUSQA-2567	: IRB - Application throwing Array index out of range error while opening a renewal in edit mode - Start
//            if(amendCustomElements == null || (amendCustomElements.size()<1)){
            if((amendCustomElements == null || amendCustomElements.size()<1) && 
                    editableModules != null && editableModules.size() > 0){//COEUSQA-2567 : end
                HashMap hmEditableModules = (HashMap) editableModules.get(0);
                hmEditableModules.remove(IrbWindowConstants.OTHERS);
                editableModules.removeAllElements();
                editableModules.addElement(hmEditableModules);
            }
        }
        //Case#4494 - End
        //Modified with CoeusQA2313: Completion of Questionnaire for Submission
        // Get all questionnaires
        HashMap hmApplicableQuestionnaire = getApplicableQuestionnaires();
        //case 4277:Now that there is New Amendment/Renewal, do not allow changes in an Renewal.
        //COEUSQA-2602-Remove checkboxes from Renewal Summary screen
        if(screenMode == CoeusGuiConstants.RENEWAL || isRenewal(protocolId)){
            editableModules = new Vector();            
            hmApplicableQuestionnaire = new HashMap();
        }
        //4277 End
//        amendRenewalForm = new ProtocolAmendmentRenewalForm(functionType,
//        amendRenList,requestedModule);
        //sene the editable modules to the AmendmentRenewalSummary screen
        if(functionType == 'P'){
            amendRenewalForm = new ProtocolAmendmentRenewalForm(functionType,
            new Vector(),requestedModule,editableModules, hmApplicableQuestionnaire);
        } else {
            amendRenewalForm = new ProtocolAmendmentRenewalForm(functionType,
            amendRenList,requestedModule,editableModules, hmApplicableQuestionnaire);            
        }
        // CoeusQA2313: Completion of Questionnaire for Submission - End
       //code added for coeus4.3 enhancement - ends
        pnlAmendments = new JPanel(layout);
        pnlAmendments.add(amendRenewalForm.getAmendRenewalPanel());
        JScrollPane scrPnAmendments = new JScrollPane();
        scrPnAmendments.setViewportView( pnlAmendments );
        
        //Added for case 2176 - Risk Level Category - start
        JPanel pnlRiskLevelCategory = new JPanel(layout);
        protocolRiskLevelCategoryForm = 
                new ProtocolRiskLevelCategoryForm(functionType, protocolId);
        pnlRiskLevelCategory.add(protocolRiskLevelCategoryForm);
        JScrollPane scrPnRiskLevelCategory = new JScrollPane();
        scrPnRiskLevelCategory.setViewportView(pnlRiskLevelCategory);
        //Added for case 2176 - Risk Level Category - end
        
        //Added for case 3552 - IRB Attachments - start
        JPanel pnlAttachments = new JPanel(layout);
        hmModeDetails.put("functionType", new Character(functionType));
        hmModeDetails.put("moduleId", IrbWindowConstants.UPLOAD_DOCUMENTS);
        //Modified for Case#4275 - upload attachments until in agenda - Start
        //otherFunctionType is passed in ProtocolAttachmentsForm constructor
//        protocolAttachmentsForm = new ProtocolAttachmentsForm(this,protocolInfo,
//                getMode(hmModeDetails));
        char otherFunctionType = getMode(hmModeDetails);
        protocolAttachmentsForm = new ProtocolAttachmentsForm(this,protocolInfo,
                otherFunctionType,otherFunctionType);
        //Modified for Case#4275 - End
        pnlAttachments.add(protocolAttachmentsForm);
        JScrollPane scrPnAttachments = new JScrollPane();
        scrPnAttachments.setViewportView(pnlAttachments);
        //Added for case 3552 - IRB Attachments - end
        
        tbdPnProtocol.addTab("Protocol", jscrPn);
        tbdPnProtocol.addTab("Investigator", jscrPnInvestigator);
        //Modified for Case# 3232-Word "key" is still being carried in the study personnel tab
//        tbdPnProtocol.addTab("Key Study Personnel", jscrPnKeyStudy);
        tbdPnProtocol.addTab("Study Personnel", jscrPnKeyStudy);        
        tbdPnProtocol.addTab("Correspondents", jscrPnCorrespondance);
        tbdPnProtocol.addTab("Area of Research", jscrPnAOR);
        tbdPnProtocol.addTab("Funding Source", jscrPnFunding);
        tbdPnProtocol.addTab("Actions", jscrPnOtherActions);
        
        //Protocol Enhancements - Reading header from the prop file Start
        //tbdPnProtocol.addTab("Subjects", jscrPnSubjects);
        String title = coeusMessageResources.parseLabelKey("subjects_labelCode.1000");
        tbdPnProtocol.addTab(title, jscrPnSubjects);
        //Protocol Enhancements - Reading header from the prop file End
        
        //Added for case 3552 - IRB Attachments - start
        tbdPnProtocol.addTab("Attachments", scrPnAttachments );
        //Added for case 3552 - IRB Attachments - end
        tbdPnProtocol.addTab("Special Review", jscrPnSpecialReviews);
        //Added for case 2176 - Risk Level Category - start
        tbdPnProtocol.addTab("Risk Levels", scrPnRiskLevelCategory);
        //Added for case 2176 - Risk Level Category - end
        tbdPnProtocol.addTab("Notes", jscrPnNotepad);
        tbdPnProtocol.addTab(amendRenewalForm.getTitle(), scrPnAmendments );
        
        /* modified by ravi to show custom elements if either module or protocol
         * contains custom data */
//        if( protCustomElements != null && protCustomElements.size() > 0 ) {
            // set the tab into tab pane
            JPanel pnlCustomElements = new JPanel(layout);
            //code modified and added for coeus4.3 enhancements - starts
            // To check whether the module is editable.
//            if(functionType != ADD_MODE){
//                boolean isMandatory = isMandatoryFieldExist(protCustomElements);
//                if(isMandatory && ((protocolInfo.getProtocolNumber() != null &&
//                        protocolInfo.getProtocolNumber().length() > 10) || functionType == 'E')){
//                    hmModeDetails.put("moduleId", IrbWindowConstants.OTHERS);
////                    hmModeDetails.put("functionType", new Character('M'));
//                    char data = getMode(hmModeDetails);
//                    if(data == 'M'){
//                        AmendmentRenewalSummary amendmentRenewalSummary =
//                                (AmendmentRenewalSummary) pnlAmendments.getComponent(0);
//                        amendmentRenewalSummary.chkOthers.setSelected(true);
//                    }
//                    customElementsForm = new CustomElementsForm(data,  true, protCustomElements);
//                } else if(protocolInfo.getProtocolNumber() != null && protocolInfo.getProtocolNumber().length() > 10){
//                    customElementsForm = new CustomElementsForm('D',  true, protCustomElements);
//                } else {
//                    customElementsForm = new CustomElementsForm(functionType,  true, protCustomElements);
//                }
//            } else {
//                customElementsForm = new CustomElementsForm(functionType,  true, protCustomElements);
//            }
            hmModeDetails.put("moduleId", IrbWindowConstants.OTHERS);
            hmModeDetails.put("functionType", new Character(functionType));
            customElementsForm = new CustomElementsForm(getMode(hmModeDetails),  true, protCustomElements);
            //code modified and added for coeus4.3 enhancements - ends
            pnlCustomElements.add(customElementsForm);
            JScrollPane jscrPnCustomElements = new JScrollPane();
            jscrPnCustomElements.setViewportView( pnlCustomElements );
            //Case#4494 - In Protocol, Error on Other tab when custom elements are not defined  - Start
            //Others tab is added only if there is customlements exist
            //Other tab is ommmited, if there is no custom elements
            if(!customElementsForm.getOtherColumnElementData().isEmpty()){
                HashMap hmEditModules =(HashMap)hmModeDetails.get("hmEditModules");
                if(hmEditModules != null && hmEditModules.size() < 1 ){
                    customElementsForm.setFunctionType(DISPLAY_MODE);
                    customElementsForm.setPersonColumnValues(protCustomElements);
                    customElementsForm.setSaveRequired(false);
                }
                isOtherTabDisabled = false;
                tbdPnProtocol.addTab("Others", jscrPnCustomElements);
            }
            //Case#4494 - End
//        }
        //Code added for coeus4.3 Amendments/Renewal enhancements - starts
        if(functionType == 'P'){
                originalFunctionType = functionType;
                functionType = 'E';
        }
        if(protocolInfo != null){
            Vector vecAmendRenewData = protocolInfo.getAmendmentRenewal();
            if(vecAmendRenewData!=null && vecAmendRenewData.size() > 0){
                ProtocolAmendRenewalBean bean =(ProtocolAmendRenewalBean) vecAmendRenewData.get(0);
                setVecModuleData((Vector)
                ObjectCloner.deepCopy(bean.getVecModuleData()));
            }
        }
        //Code added for coeus4.3 Amendments/Renewal enhancements - ends
        tbdPnProtocol.setSelectedIndex(0);
        
         /* This  will catch the tab change event in the tabbed pane.
          * If the user selects any other tab from the CommitteeTab without
          * saving the committee information then it will prompt the user
          * to save the committee information before shifting to the other tab.
          */
        tbdPnProtocol.addChangeListener(new ChangeListener(){
            public void stateChanged(ChangeEvent ce){
                JTabbedPane pn = (JTabbedPane)ce.getSource();
                int selectedTab = pn.getSelectedIndex();
                try {
                    switch ( selectedTab ) {
                        case 0 :
                            protocolMaintenance.setDefaultFocusForComponent();
                            break;
                        case 1 :
                            protocolInv.setDefaultFocusForComponent();
                            break;
                        case 2 :
                            protocolKey.setDefaultFocusForComponent();
                            break;
                        case 3 :
                            protocolCorr.setDefaultFocusForComponent();
                            break;
                        case 4:
                            protocolArea.setDefaultFocusForComponent();
                            break;
                        case 5:
                            protocolFund.setDefaultFocusForComponent();
                            break;
                        case 6:
                            protocolAction.setDefaultFocusForComponent();
                            break;
                        case 7:
                            protocolSubjects.setDefaultFocusForComponent();
                            break;
                        case 8:
                            protocolSpecialReviewForm.setDefaultFocusForComponent();
                            break;
                        case 9:
                            protocolNotepadForm.setDefaultFocusForComponent();
                            break;
                        case 10:
                            amendRenewalForm.requestInitialFocus();
                            break;
                        //Case :#3149 Tabbing between fields does not work on Others tabs - Start
                        //case modified from 11 to 13 for other tab   
                        case 13:
                            char fun = functionType;
                            if(customElementsForm != null &&                                    
                                    customElementsForm.getTable().isEnabled()){
                                customElementsForm.setTabFocus();
                                if(tbdPnProtocol.getSelectedIndex() == 13 && count != 0){
                                    customElementsForm.setTableFocus();
                                    count++;
                                }
                                
                                tbdPnProtocol.addMouseListener(new MouseAdapter() {
                                    public void mouseClicked(MouseEvent e) {
                                        count = 1;
                                        if(tbdPnProtocol.getSelectedIndex() == 13 && customElementsForm != null && 
                                                customElementsForm.getTable().isEnabled()){
                                            customElementsForm.setTableFocus();
                                        }
                                    }
                                });
                            }
                            // Case :#3149 - End
                            break;
                            
                    }
                }catch(Exception e) {
                    tbdPnProtocol.setSelectedIndex(0);
                    CoeusOptionPane.showErrorDialog(e.getMessage());
                }
            }
        });
        
        //prps start jan 06 2003
        checkForNewSequenceNumber() ;
        //prps end jan 06 2003
        protocolFund.btnStartProposal.addActionListener(this);
        return tbdPnProtocol;
        
    }
    
    /** The method used to get the save status of the <CODE>ProtocolDetailsForm</CODE>.
     * @return save boolean true if there are any unsaved modifications,
     * else false.
     */
    protected boolean isSaveRequired() {
        /* check all the falgs in ProtocolMaintenaceForm and ProtocolKeyPersonnel
         * and ProtocolAreaOfResearch and ProtocolCorrespondents and
         * ProtocolFundingSource and ProtocolInvestigatorsform
         * whether any data is changed by the user or
         * not to set the save required flag in this ProtocolDetails form.
         *
         */
        try{
            
            
            /**
             * Fix for Save confirmation Dispaly Fucntion Type.
             * April 12 2003.
             * Updated by Subramanya
             */
            //protocolSpecialReviewForm.handleEditableComponents();
            if( functionType == CoeusGuiConstants.DISPLAY_MODE ){
                if( (rolesForm != null && rolesForm.isSaveRequired()) ||
                   protocolNotepadForm.isSaveRequired()
                
                //Case 1787 Start 21
                || protocolFund.isSaveRequired()
                //Case 1787 End 21
                //Added for case#3089 - Special Review Tab made editable in display mode - start
                || protocolSpecialReviewForm.isSaveRequired()
                //Added for case#3089 - Special Review Tab made editable in display mode - end
                
                ) {
                    saveRequired = true;
                }else{
                    saveRequired = false;
                }
            }else {
                protocolInfo = protocolMaintenance.getFormData();
                if ( (protocolMaintenance.isSaveRequired())
                || (protocolKey.isSaveRequired())
                || (protocolArea.isSaveRequired())
                || (protocolCorr.isSaveRequired())
                || (protocolFund.isSaveRequired())
                || (protocolInv.isSaveRequired())
                || (protocolSubjects.isSaveRequired())
                || (protocolSpecialReviewForm.isSaveRequired())
                || (protocolNotepadForm.isSaveRequired())
                //Code modified for Coeus4.3 enhancement to validate the custom elements 
                //data while saving.
                //|| (customElementsForm != null && customElementsForm.isSaveRequired())
                || (customElementsForm != null  && customElementsForm.getFunctionType() != 'D'
                    && customElementsForm.validateData() && customElementsForm.isSaveRequired())
                //modified for Coeus Enhancement case:#1787 step 7 start
               // || (protocolRelatedProjects != null && isProtoRelSaveReq )
                || ( amendRenewalForm.isSaveRequired() )
                || (rolesForm != null && rolesForm.isSaveRequired())
                
                //Protocol Enhancment -  Administrative Correction Start 3
                || (protocolAction.isSaveRequired())
                //Protocol Enhancment -  Administrative Correction End 3
                ) {
                    saveRequired =true;
                    
                }else if (  functionType == COPY_MODE ){
                    saveRequired = true;
                }
                //                System.out.println("maint:"+protocolMaintenance.isSaveRequired());
                //                System.out.println("inv:"+protocolInv.isSaveRequired());
                //                System.out.println("key:"+protocolKey.isSaveRequired());
                //                System.out.println("area:"+protocolArea.isSaveRequired());
                //                System.out.println("corr:"+protocolCorr.isSaveRequired());
                //                System.out.println("fund:"+protocolFund.isSaveRequired());
                //                System.out.println("subj:"+protocolSubjects.isSaveRequired());
                //                System.out.println("sr:"+protocolSpecialReviewForm.isSaveRequired());
                //                //System.out.println("custom:"+customElementsForm.isSaveRequired());
                //                System.out.println("notes:"+protocolNotepadForm.isSaveRequired());
                //                System.out.println("amend/renew:" + amendRenewalForm.isSaveRequired());
                //                if( rolesForm != null ) {
                //                    System.out.println("roles:"+rolesForm.isSaveRequired());
                //                }
            }
        } catch (CoeusUIException coeusUIException) {
            saveRequired = true;
        } catch(Exception e){
            // Changed And Handle It when
            //saveRequired = true;
            //String exMsg = e.getMessage();
            //CoeusOptionPane.showWarningDialog(exMsg);
        }
        return saveRequired;
    }
    
    private void setProtocolDataInDisplayMode(){
        if( protocolNotepadForm.isSaveRequired() ) {
            //             commented not to change seq number when notes/roles have been
            //             modified in display mode : case 982
            //            protocolInfo.getProtocolDetailChangeFlags().setIsNotesChanged(true);
            protocolInfo.setVecNotepad(protocolNotepadForm.getProtocolVecNotes());
        }
        Hashtable htExistingUsers = new Hashtable();
        Hashtable htTempUsers = new Hashtable();
        if( copyOfExistingUserRoles != null ){
            htExistingUsers = getUserRoleInfo( copyOfExistingUserRoles );
        }
        
        if( rolesForm != null && rolesForm.isSaveRequired() ) {
            if ( tempUserRoles != null ) {
                htTempUsers = getUserRoleInfo( tempUserRoles );
            }
            //             commented not to change seq number when notes/roles have been
            //             modified in display mode : case 982
            //            protocolInfo.getProtocolDetailChangeFlags().setIsRolesChanged(true);
        }else{
            Vector rolesFromServer = protocolInfo.getUserRolesInfoBean();
            if( rolesFromServer != null ) {
                htTempUsers = getUserRoleInfo( rolesFromServer );
            }
        }
        Vector userRolesForDB  = checkModifications(htExistingUsers, htTempUsers );
        protocolInfo.setUserRoles( userRolesForDB );
        
        protocolInfo.setAcType("U");
        Vector  protCorrespondents = protocolInfo.getCorrespondetns();
        if( protCorrespondents != null && protCorrespondents.size() > 0) {
            protocolInfo.setCorrespondenceIndicatorStatus(NOT_MODIFIED);
        }else{
            protocolInfo.setCorrespondenceIndicatorStatus(NOT_PRESENT);
        }
        
        Vector keyPersonnel = protocolInfo.getKeyStudyPersonnel();
        if( keyPersonnel != null && keyPersonnel.size() > 0) {
            protocolInfo.setKeyStudyIndicatorStatus(NOT_MODIFIED);
        }else{
            protocolInfo.setKeyStudyIndicatorStatus(NOT_PRESENT);
        }
        Vector protFundingSource = protocolInfo.getFundingSources();
        if( protFundingSource != null && protFundingSource.size() > 0) {
            protocolInfo.setFundingSourceIndicatorStatus(NOT_MODIFIED);
        }else{
            protocolInfo.setFundingSourceIndicatorStatus(NOT_PRESENT);
        }
        //modified for Coeus Enhancement case:#1787 step 8 start
       /* Vector protoRelProjects = protocolInfo.getRelatedProjects();
        if( protRelProjects != null && protRelProjects.size() > 0 ) {
            protocolInfo.setProjectsIndicatorStatus(NOT_MODIFIED);
        }else{
            protocolInfo.setProjectsIndicatorStatus(NOT_PRESENT);
        }
         */
        
        Vector selectedSubjects = protocolInfo.getVulnerableSubjectLists();
        if( selectedSubjects != null && selectedSubjects.size() > 0) {
            protocolInfo.setVulnerableDataStatus(NOT_MODIFIED);
        }else{
            protocolInfo.setVulnerableDataStatus(NOT_PRESENT);
        }
        
        Vector protocolSpecialReviewData = protocolInfo.getSpecialReviews();
        if( protocolSpecialReviewData != null && protocolSpecialReviewData.size() > 0) {
            protocolInfo.setSpecialReviewDataStatus(NOT_MODIFIED);
        }else{
            protocolInfo.setSpecialReviewDataStatus(NOT_PRESENT);
        }
        
        Vector protReferenceData = protocolInfo.getReferences();
        if( protReferenceData != null && protReferenceData.size() > 0) {
            protocolInfo.setReferenceIndicatorStatus(NOT_MODIFIED);
        }else{
            protocolInfo.setReferenceIndicatorStatus(NOT_PRESENT);
        }
        if( customElementsForm != null ) {
            Vector protocolCustomElements =
            constructProtCustElement( customElementsForm.getOtherColumnElementData() );
            //System.out.println("protocolCustElements for Save:"+ protocolCustomElements);
            if(customElementsForm.isSaveRequired()){
                //             commented not to change seq number when notes/roles have been
                //             modified in display mode : case 982
                //                protocolInfo.getProtocolDetailChangeFlags().setIsCustomDataChanged(true);
                protocolInfo.setCustomElements(protocolCustomElements);
            }else{
                protocolInfo.setCustomElements(null);
            }
        }
        //Added for case#3089 - Special Review Tab made editable in display mode - start
        if(protocolFund.isSaveRequired() || protocolSpecialReviewForm.isSaveRequired()){
            //Added for Case#4312 - Non-IRB admin has ability to edit Funding Source outside of an amendment - Start
            //If the protocol is opened in Display mode and FundingSource or the SpecialReview Tab is enabled
            //then lock the protocol and set recorLockedStatus to 1.
            if((protocolFund.isEnabled() || protocolSpecialReviewForm.isEnabled()) && functionType == DISPLAY_MODE){
                boolean isProtocolLocked = lockProtocol();
                if(isProtocolLocked){
                    recordLockedStatus = CoeusGuiConstants.LOCK_SUCCESSFUL;
                }
            }
            //Case#4312 - End
            lockExist = canLockProtocol();
        }else{
            lockExist = false;
            return;
        }
        //Added for case#3089 - Special Review Tab made editable in display mode - end
       //Case 1787 Start 22
        Vector vecFundingSourceToServer = protocolFund.getFundingSourceData();
        protocolInfo.setIsAllFundingSourcesDelete( setFundingSourceAllDeletes( vecFundingSourceToServer ) );
        if(protocolFund.isSaveRequired()){
            protocolInfo.getProtocolDetailChangeFlags().setIsFundingSourceChanged(true);
            //protocolInfo.setFundingSources(fundingSrc);
            protocolInfo.setFundingSources(vecFundingSourceToServer);
            protocolInfo.setFundingSourceIndicatorStatus(MODIFIED);                
        }else{
            if( protFundingSource != null
                    && protFundingSource.size() > 0) {
                protocolInfo.setFundingSourceIndicatorStatus(NOT_MODIFIED);
            }else{
                protocolInfo.setFundingSourceIndicatorStatus(NOT_PRESENT);
            }
            protocolInfo.setFundingSources(null);
        }
        //Case 1787 End 22  
        //Added for case#3089 - Special Review Tab made editable in display mode - start
        Vector vecSpecialReviewToServer = null;
        vecSpecialReviewToServer = protocolSpecialReviewForm.getProtocolSpecialReviewData();
        if(protocolSpecialReviewForm.isSaveRequired()){
            protocolInfo.getProtocolDetailChangeFlags().setIsSpecialRevChanged(true);
            protocolInfo.setSpecialReviews(vecSpecialReviewToServer);
            protocolInfo.setSpecialReviewIndicator(MODIFIED);
        }else{
            if(vecSpecialReviewToServer != null && vecSpecialReviewToServer.size() > 0){
                protocolInfo.setSpecialReviewIndicator(NOT_MODIFIED);
            }else{
                protocolInfo.setSpecialReviewIndicator(NOT_PRESENT);
            }
            protocolInfo.setSpecialReviews(null);
        }
        //Added for case#3089 - Special Review Tab made editable in display mode - end
    }
 private Vector getUserRoleForUnit(String unitNum,String user ){

        RequesterBean requester = new RequesterBean();
        Vector treeForUnit = null;
        requester.setFunctionType(GET_USER_ROLES_FOR_UNIT);
        requester.setId( unitNum );
        requester.setDataObject(user);
        String connectTo =CoeusGuiConstants.CONNECTION_URL
        + PROTOCOL_SERVLET;
        AppletServletCommunicator comm
        = new AppletServletCommunicator(connectTo,requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        if ((response != null )
        && (!response.isSuccessfulResponse())){
            CoeusOptionPane.showErrorDialog(
            response.getMessage());
            return null;
        }else{
            treeForUnit = ( Vector ) response.getDataObject();
        }
        return treeForUnit;
 }
    protected String saveProposal()throws Exception{
        Vector vecSpecialReviewToServer = null;
        String proposalNumber=null;
        String unitNum=null;
        vecSpecialReviewToServer = protocolSpecialReviewForm.getProtocolSpecialReviewData();
        protocolInv.setInvestigatorOnCeateProtocl(true);
        ProtocolSpecialReviewFormBean rvFormBean = new ProtocolSpecialReviewFormBean();
        int splRevCode = 1;
        int apprvlTypeCode = 1;
        
        protocolInfo = protocolMaintenance.getFormData();
        
        String protocolNum = protocolInfo.getProtocolNumber();
        java.sql.Date applnDate = protocolInfo.getApplicationDate();
        java.sql.Date apprvlDate = protocolInfo.getApprovalDate();
        
           
        rvFormBean.setProtocolNumber(protocolNum);
        rvFormBean.setSpecialReviewCode(splRevCode);
        rvFormBean.setApprovalCode(apprvlTypeCode);
        rvFormBean.setApplicationDate(applnDate);
        rvFormBean.setApprovalDate(apprvlDate);
        rvFormBean.setProtocolSPRevNumber(protocolNum);

        if(vecSpecialReviewToServer == null) {
            vecSpecialReviewToServer = new Vector();
        }
        vecSpecialReviewToServer.add(rvFormBean);
        
        Vector investigators = protocolInv.getFormData();

        boolean saveCon=true;
        


        protocolInfo.setSequenceNumber(1);
        protocolInfo.setInvestigators(investigators);
        protocolInfo.setSpecialReviews(vecSpecialReviewToServer);

         String connect = CoeusGuiConstants.CONNECTION_URL + PROTOCOL_SERVLET;
        RequesterBean requester = new RequesterBean();
        requester.setFunctionType(userRole);
       // request.setId(protocolInfo.getProtocolNumber());
      //  requester.setDataObject(protocolInfo.getCreateUser());
        AppletServletCommunicator com = new AppletServletCommunicator(
        connect, requester);
        com.send();
          ResponderBean respons = com.getResponse();

        if (respons.isSuccessfulResponse()){
            unitNum=(String)respons.getDataObject();
        }
        Vector newUserRoles = null;
                if ( rolesForm == null ){
                   // newUserRoles = getTreeDataForUnit( unitNum );
                      newUserRoles = getUserRoleForUnit( unitNum,protocolInfo.getCreateUser() );

                }else {
                    if( tempUserRoles != null ) {
                        newUserRoles = tempUserRoles;
                    }
                }
                Hashtable htNewUsers = getUserRoleInfo( newUserRoles );

                Vector userRolesForDB  = checkModifications(
                new Hashtable(), htNewUsers );
         protocolInfo.setUserRoles( userRolesForDB );
           if(!protocolMaintenance.validateData()){
                tbdPnProtocol.setSelectedIndex(0);
            }else if(!protocolInv.validateData()){
                tbdPnProtocol.setSelectedIndex(1);
            }
          else{
        String connectTo = CoeusGuiConstants.CONNECTION_URL + PROTOCOL_SERVLET;
        RequesterBean request = new RequesterBean();
        request.setFunctionType(saveProposal);
       // request.setId(protocolInfo.getProtocolNumber());
        request.setDataObject(protocolInfo);
        AppletServletCommunicator comm = new AppletServletCommunicator(
        connectTo, request);
        comm.send();

        ResponderBean response = comm.getResponse();

        if (response.isSuccessfulResponse()){
            proposalNumber=(String)response.getDataObject();
            CoeusOptionPane.showInfoDialog("New stub proposal created successfully");

        }else{
              CoeusOptionPane.showInfoDialog("New stub proposal creation failed");
        }
            }

       protocolInfo.setInvestigators(null);
        return proposalNumber;
    }

    /** This method is used to save the Protocol information to the database.
     * @throws Exception if unable to save the protocol details to the database.
     */
    protected void setProtocolInfo() throws Exception{
        boolean isDeleted = false;
        Vector vecAmdnRenew = amendRenewalForm.getFormData();
        if(vecAmdnRenew != null && vecAmdnRenew.size() > 0){
            ProtocolAmendRenewalBean amendRenewBean = 
                    (ProtocolAmendRenewalBean) vecAmdnRenew.get(0);
            Vector vecModuleData = amendRenewBean.getVecModuleData();
            if(vecModuleData !=null && vecModuleData.size() > 0){
                for(int index = 0 ; index < vecModuleData.size() ; index++){
                    ProtocolModuleBean moduleBean = 
                            (ProtocolModuleBean) vecModuleData.get(index);
                    if(moduleBean.getAcType() != null &&
                            moduleBean.getAcType().equals("D") && 
                            moduleBean.getProtocolModuleCode().equals(IrbWindowConstants.OTHERS)){
                        isDeleted = true;
                    }
                }
            }
        }        
        if(functionType == DISPLAY_MODE ){
            //Modified for case#3089 - Special Review Tab made editable in display mode - start
            if(!protocolSpecialReviewForm.validateData()){
                //Modified for case 3552 - IRB attachments - start
                tbdPnProtocol.setSelectedIndex(SPECIAL_REVIEW_TAB);
                //Modified for case 3552 - IRB attachments - end
            }else{
                setProtocolDataInDisplayMode();
            }
            //Modified for case#3089 - Special Review Tab made editable in display mode - end
        }else if( functionType != DISPLAY_MODE){
            //protocolSpecialReviewForm.handleEditableComponents();
            // Added for Enable multicampus for default organization in protocols - Start
            boolean isDefaultLocation = false;
            if(protocolMaintenance.locationForm.isMulticampusOrg()){
                Vector vecInvDetails = protocolInv.getFormData();
                if(vecInvDetails != null && !vecInvDetails.isEmpty()){
                    for(Object invesDetails : vecInvDetails){
                        ProtocolInvestigatorsBean investigatorsBean = (ProtocolInvestigatorsBean)invesDetails;
                        if(investigatorsBean.isPrincipalInvestigatorFlag() &&
                                TypeConstants.INSERT_RECORD.equals(investigatorsBean.getAcType())){
                            isDefaultLocation = true;
                            break;
                        }
                    }
                }
            }
            if(isDefaultLocation){
                String connectTo = CoeusGuiConstants.CONNECTION_URL + FUNCTION_SERVLET;
                RequesterBean request = new RequesterBean();
                request.setId(protocolInv.getLeadUnitNumber());
                request.setDataObject("GET_ORGANIZATION_FOR_UNIT");
                AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
                comm.send();
                ResponderBean response = comm.getResponse();
                if(response.isSuccessfulResponse()){
                    String orgId = response.getId();
                    Vector vecOrgDetails = response.getDataObjects();
                    if(vecOrgDetails != null && !vecOrgDetails.isEmpty()){
                        OrganizationAddressFormBean organizationAddress = (OrganizationAddressFormBean)vecOrgDetails.get(0);
                        RolodexDetailsBean rolodexDetailsBean = (RolodexDetailsBean)vecOrgDetails.get(1);
                        boolean isSetDefaultLocation = true;
                        Vector vecLocations = protocolMaintenance.locationForm.getLocations();
                        if(vecLocations != null && !vecLocations.isEmpty()){
                            for(Object locationDetails : vecLocations){
                                ProtocolLocationListBean locationBean = (ProtocolLocationListBean)locationDetails;
                                String defaultOrgTypeCode = protocolMaintenance.locationForm.getDefaultOrganizationTypeCode();
                                int defaultOrgType = 0;
                                if(defaultOrgTypeCode != null && !"".equals(defaultOrgTypeCode)){
                                    defaultOrgType = Integer.parseInt(defaultOrgTypeCode);
                                }
                                if(orgId.equals(locationBean.getOrganizationId()) && 
                                        locationBean.getOrganizationTypeId() == defaultOrgType){
                                    isSetDefaultLocation = false;
                                    break;
                                }
                            }
                        }
                        if(isSetDefaultLocation){
                            protocolMaintenance.locationForm.setDefaultLocation(orgId,organizationAddress,rolodexDetailsBean);
                        }
                    }
                }
            }
            // Added for Enable multicampus for default organization in protocols - End
            
            if(!protocolMaintenance.validateData()){
                tbdPnProtocol.setSelectedIndex(0);
            }else if(!protocolInv.validateData()){
                tbdPnProtocol.setSelectedIndex(1);
            }else if(!protocolKey.validateFormData()){
                tbdPnProtocol.setSelectedIndex(2);
            }else if(!protocolCorr.validateData()){
                tbdPnProtocol.setSelectedIndex(3);
            }else if(!protocolArea.validateData()){
                tbdPnProtocol.setSelectedIndex(4);
            }else if(!protocolFund.validateData()){
                tbdPnProtocol.setSelectedIndex(5);
            }else if(!protocolSubjects.validateData()){
                tbdPnProtocol.setSelectedIndex(7);
            }else if(!protocolSpecialReviewForm.validateData()){
                //Modified for case 3552 - IRB attachments - start
                tbdPnProtocol.setSelectedIndex(SPECIAL_REVIEW_TAB);
                //Modified for case 3552 - IRB attachments - end
                //            }else if(!referenceNumberForm.validateData()){    //Implemented for ProtocolSpecialReview
                //                tbdPnProtocol.setSelectedIndex(10);
                //            }
            }else if( !amendRenewalForm.validateData()){
                //Modified for case 3552 - IRB attachments - start
                //Modified for case 2176 - Risk Level Category - start
                tbdPnProtocol.setSelectedIndex(AMENDMEND_SUMMARY_TAB);
                //Modified for case 2176 - Risk Level Category - end
                //Modified for case 3552 - IRB attachments - end
             //code modified for coeus4.3 enhancements
             // to disable the validation when the others page is in display mode.
//            }else if(customElementsForm != null && !customElementsForm.validateData()){
            }else if(customElementsForm != null && customElementsForm.getFunctionType() != 'D'
                    && customElementsForm.getFunctionType() != 'E' 
                    && !isDeleted && !customElementsForm.validateData()){
                //Modified for case 3552 - IRB attachments - start
                //Modified for case 2176 - Risk Level Category - start
                tbdPnProtocol.setSelectedIndex(OTHERS_TAB);
                //Modified for case 2176 - Risk Level Category - end
                //Modified for case 3552 - IRB attachments - end
            }else{
                /*Get all the tabs information for saving complete information*/
                if (functionType == ADD_MODE || functionType == MODIFY_MODE){
                    protocolInfo = protocolMaintenance.getFormData();
                    
                    if(protocolInfo != null){
                        /*if(!protocolMaintenance.isProtocolSubjectsModified()){
                            protocolInfo.setVulnerableSubjectLists(null);
                        }*/
                        //Maintenance modified
                        if(protocolMaintenance.isSaveRequired() && !protocolMaintenance.isProtocolLocationsModified()){
                            protocolInfo.getProtocolDetailChangeFlags().setIsProtocolMaintChanged(true);
                        }
                        
                        // Added Code for setting References Data
                        protReferenceData = constructProtocolRef(protReferenceData);
                        setReferenceStatusFlags();
                        
                        // Code references End
                        if(!protocolMaintenance.isProtocolLocationsModified()){
                            //Commented for COEUSQA-2879_LOG_IACUC and IRB_Correspondents do not auto-populate-Start
//                            protocolInfo.setLocationLists(null);
                            //Commented for COEUSQA-2879_LOG_IACUC and IRB_Correspondents do not auto-populate-End
                        }else{
                            protocolInfo.getProtocolDetailChangeFlags().setIsLocationChanged(true);
                        }
                        
                        Vector investigators = protocolInv.getFormData();
                        if(protocolInv.isSaveRequired()){
                            protocolInfo.getProtocolDetailChangeFlags().setIsInvestigatorChanged(true);
                            protocolInfo.setInvestigators(investigators);
                        }else{
                            //Commented for COEUSQA-2879_LOG_IACUC and IRB_Correspondents do not auto-populate-Start
//                            protocolInfo.setInvestigators(null);
                            //Commented for COEUSQA-2879_LOG_IACUC and IRB_Correspondents do not auto-populate-End
                        }
                        //Implemented by Raghunath P.V. for implementing Indicator Logic
                        Vector vecCorrespondanceToServer = protocolCorr.getFormData();
                        protocolInfo.setIsAllCorrespondentsDelete( setCorrespondanceIsAllDeletes( vecCorrespondanceToServer ) );
                        //commented to send all the data from client to generalize seq no. implementation in server
                        //Vector correspondands = filterCorrespondants(vecCorrespondanceToServer);
                        
                        if(protocolCorr.isSaveRequired()){
                            //protocolInfo.setCorrespondants(correspondands);
                            protocolInfo.setCorrespondants(vecCorrespondanceToServer);
                            protocolInfo.getProtocolDetailChangeFlags().setIsCorrespondentsChanged(true);
                            protocolInfo.setCorrespondenceIndicatorStatus(MODIFIED);
                        }else{
                            if( protCorrespondents != null
                            && protCorrespondents.size() > 0) {
                                protocolInfo.setCorrespondenceIndicatorStatus(NOT_MODIFIED);
                            }else{
                                protocolInfo.setCorrespondenceIndicatorStatus(NOT_PRESENT);
                            }
                            //Commented for COEUSQA-2879_LOG_IACUC and IRB_Correspondents do not auto-populate-Start
//                            protocolInfo.setCorrespondants(null);
                            //Commented for COEUSQA-2879_LOG_IACUC and IRB_Correspondents do not auto-populate-End
                        }
                        
                        Vector keyPersonnel
                        = protocolKey.getKeyStudyPersonnelData();
                        if(protocolKey.isSaveRequired()){
                            protocolInfo.getProtocolDetailChangeFlags().setIsKeyStudyChanged(true);
                            protocolInfo.setKeyStudyPersonnel(keyPersonnel);
                            protocolInfo.setKeyStudyIndicatorStatus(MODIFIED);
                        }else{
                            if( protKeyStudyPersonnel != null
                            && protKeyStudyPersonnel.size() > 0) {
                                protocolInfo.setKeyStudyIndicatorStatus(NOT_MODIFIED);
                            }else{
                                protocolInfo.setKeyStudyIndicatorStatus(NOT_PRESENT);
                            }
                            protocolInfo.setKeyStudyPersonnel(null);
                        }
                        
                        //Area of Research
                        Vector protocolAOR = protocolArea.getFormData();
                        //System.out.println("protocolAOR vector size is ......"+protocolAOR.size());
                        if(protocolArea.isSaveRequired()){
                            protocolInfo.getProtocolDetailChangeFlags().setIsAORChanged(true);
                            protocolInfo.setAreaOfResearch( protocolAOR );
                        }else{
                            protocolInfo.setAreaOfResearch(null);
                        }
                        
                        //Implemented by Raghunath P.V. for implementing Indicator Logic
                        Vector vecFundingSourceToServer = protocolFund.getFundingSourceData();
                        protocolInfo.setIsAllFundingSourcesDelete( setFundingSourceAllDeletes( vecFundingSourceToServer ) );
                        //commented to send all the data from client to generalize seq no. implementation in server
                        //Vector fundingSrc = filterFundingSources(vecFundingSourceToServer);
                      
                        if(protocolFund.isSaveRequired()){
                             
                            protocolInfo.getProtocolDetailChangeFlags().setIsFundingSourceChanged(true);
                            //protocolInfo.setFundingSources(fundingSrc);
                            protocolInfo.setFundingSources(vecFundingSourceToServer);
                            protocolInfo.setFundingSourceIndicatorStatus(MODIFIED);
                             
                        }else{
                            if( protFundingSource != null
                            && protFundingSource.size() > 0) {
                                protocolInfo.setFundingSourceIndicatorStatus(NOT_MODIFIED);
                            }else{
                                protocolInfo.setFundingSourceIndicatorStatus(NOT_PRESENT);
                            }
                            protocolInfo.setFundingSources(null);
                        }
                      //modified for Coeus Enhancement case:#1787 step 9 start
                       /* if(protocolRelatedProjects != null && isProtoRelSaveReq){
                            Vector data = getRelatedProjectsData();
                            if(data != null){
                                protocolInfo.setRelatedProjects(data);
                                protocolInfo.getProtocolDetailChangeFlags().setIsRelatedProjectsChanged(true);
                                protocolInfo.setProjectsIndicatorStatus(MODIFIED);
                                protocolInfo.setIsAllProjectsDelete(isAllProjectsDelete);
                            }
                        }else{
                            if( protRelProjects != null && protRelProjects.size() > 0 ) {
                                protocolInfo.setProjectsIndicatorStatus(NOT_MODIFIED);
                            }else{
                                protocolInfo.setProjectsIndicatorStatus(NOT_PRESENT);
                            }
                            protocolInfo.setRelatedProjects( null );
                        }*/
                       //modified for Coeus Enhancement case:#1787 step 9 ends
                        //Implemented by Raghunath P.V. for implementing Indicator Logic
                        Vector vecVulnerableToServer = protocolSubjects.getVulnerableSubjects();
                        protocolInfo.setIsAllVulnerablesDelete( setVulnerableSubIsAllDeletes( vecVulnerableToServer ) );
                        //commented to send all the data from client to generalize seq no. implementation in server
                        //Vector vulnerables = filterVulnerableSubjects(vecVulnerableToServer);
                        
                        if(protocolSubjects.isSaveRequired()){
                            protocolInfo.getProtocolDetailChangeFlags().setIsSubjectsChanged(true);
                            //protocolInfo.setVulnerableSubjectLists(vulnerables);
                            protocolInfo.setVulnerableSubjectLists(vecVulnerableToServer);
                            protocolInfo.setVulnerableDataStatus(MODIFIED);
                        }else{
                            if( selectedSubjects != null
                            && selectedSubjects.size() > 0) {
                                protocolInfo.setVulnerableDataStatus(NOT_MODIFIED);
                            }else{
                                protocolInfo.setVulnerableDataStatus(NOT_PRESENT);
                            }
                            protocolInfo.setVulnerableSubjectLists(null);
                        }
                        //Implemented by Raghunath P.V. for implementing Indicator Logic
                        Vector vecSpecialReviewsToServer = protocolSpecialReviewForm.getProtocolSpecialReviewData();
                        protocolInfo.setIsAllSpecialReviewsDeleted( setSpecialReviewsAllDeletes( vecSpecialReviewsToServer ) );
                        //commented to send all the data from client to generalize seq no. implementation in server
                        //Vector specialReview = filterSpecialReviews(vecSpecialReviewsToServer);
                        
                        if(protocolSpecialReviewForm.isSaveRequired()){
                            protocolInfo.getProtocolDetailChangeFlags().setIsSpecialRevChanged(true);
                            //protocolInfo.setSpecialReviews(specialReview);
                            protocolInfo.setSpecialReviews(vecSpecialReviewsToServer);
                            protocolInfo.setSpecialReviewDataStatus(MODIFIED);
                        }else{
                            if( protocolSpecialReviewData != null
                            && protocolSpecialReviewData.size() > 0) {
                                protocolInfo.setSpecialReviewDataStatus(NOT_MODIFIED);
                            }else{
                                protocolInfo.setSpecialReviewDataStatus(NOT_PRESENT);
                            }
                            protocolInfo.setSpecialReviews(null);
                        }
                        
                        //Deleted ProtReferences
                        
                        // Notepad logic implemented by Senthil K
                        Vector protoAddNotes = protocolNotepadForm.getProtocolVecNotes();
                        
                        if(protocolNotepadForm.isSaveRequired()){
                            protocolInfo.getProtocolDetailChangeFlags().setIsNotesChanged(true);
                            protocolInfo.setVecNotepad(protoAddNotes);
                        }else{
                            protocolInfo.setVecNotepad(null);
                        }
                        
                        //Protocol Enhancement -  Administrative Correction Start 4
                        //We are adding Administrative Correction as an action
                        //instead of a note
                        
                        //Added by Vyjayanthi - 30/07/2004 for IRB Enhancement - Start
                        //To add a new notepad entry in Admin Corrrection mode
                        /*if( mode != null &&
                        mode.equalsIgnoreCase(CoeusGuiConstants.ADMINISTRATIVE_CORRECTIONS) ){
                            if( protoAddNotes == null ){
                                protoAddNotes = new Vector();
                            }
                            int entryNumber = protoAddNotes.size() + 1;
                            ProtocolNotepadBean notepadBean = new ProtocolNotepadBean();
                            notepadBean.setAcType(TypeConstants.INSERT_RECORD);
                            notepadBean.setComments("Administrative Correction");
                            notepadBean.setEntryNumber(entryNumber);
                            notepadBean.setProtocolNumber(protocolId);
                            notepadBean.setRestrictedFlag(false);
                            notepadBean.setSequenceNumber(getSequenceId());
                            notepadBean.setUpdateUser(mdiForm.getUserName());
                            //Get Database Timestamp
                            java.sql.Timestamp dbTimeStamp = CoeusUtils.getDBTimeStamp();
                            notepadBean.setUpdateTimestamp(dbTimeStamp);
                            
                            protoAddNotes.addElement(notepadBean);
                            protocolInfo.setVecNotepad(protoAddNotes);
                        }*/
                        //Added by Vyjayanthi - 30/07/2004 for IRB Enhancement - End
                        
                        //Protocol Enhancement -  Administrative Correction End 4
                        
                        //Added code for others Tab
                        if( customElementsForm != null) {
                            Vector protocolCustomElements =
                            constructProtCustElement( customElementsForm.getOtherColumnElementData() );
                            //System.out.println("protocolCustElements for Save:"+ protocolCustomElements);
                            if(customElementsForm.isSaveRequired()  && !isDeleted){
                                protocolInfo.getProtocolDetailChangeFlags().setIsCustomDataChanged(true);
                                protocolInfo.setCustomElements(protocolCustomElements);
                            }else{
                                protocolInfo.setCustomElements(null);
                            }
                        }
                        Vector amendRenList = amendRenewalForm.getFormData();
                        if( amendRenewalForm.isSaveRequired() ) {
                            protocolInfo.setAmendmentRenewal( amendRenList );
                        }
                        
                        if(isRoleDataChanged){
                            if ( functionType == ADD_MODE ) {
                                // adding default users of the protocol's lead unit
                                // to the OSP$PROTOCOL_USER_ROLES table
                                Vector newUserRoles = null;
                                if ( rolesForm == null ){
                                    newUserRoles = getTreeDataForUnit( protocolInv.getLeadUnitNumber() );
                                }else {
                                    if( tempUserRoles != null ) {
                                        newUserRoles = tempUserRoles;
                                    }
                                }
                                Hashtable htNewUsers = getUserRoleInfo( newUserRoles );
                                
                                Vector userRolesForDB  = checkModifications(
                                new Hashtable(), htNewUsers );
                                protocolInfo.setUserRoles( userRolesForDB );
                                
                            }else {             // if ( rolesForm != null && rolesForm.isSaveRequired() ) {
                                Hashtable htExistingUsers = new Hashtable();
                                Hashtable htTempUsers = new Hashtable();
                                if( copyOfExistingUserRoles != null ){
                                    htExistingUsers = getUserRoleInfo( copyOfExistingUserRoles );
                                } 
                                if ( tempUserRoles != null ) {
                                    htTempUsers = getUserRoleInfo( tempUserRoles );
                                }else if( rolesForm == null || !rolesForm.isSaveRequired() ) {
                                    Vector rolesFromServer = protocolInfo.getUserRolesInfoBean();
                                    if( rolesFromServer != null ) {
                                        htTempUsers = getUserRoleInfo( rolesFromServer );
                                    }
                                }
                                
                                Vector userRolesForDB  = checkModifications(
                                htExistingUsers, htTempUsers );
                                protocolInfo.setUserRoles( userRolesForDB );
                                if ( functionType == MODIFY_MODE ) {
                                    protocolInfo.setAcType( "U" );
                                }
                            }
                        }
                        /*else if ( rolesForm == null || !rolesForm.isSaveRequired() ){
                                protocolInfo.setUserRoles(null);
                        }*/
                        
                    }
                }else if ( functionType == COPY_MODE ){
                    
                    protocolInfo = protocolMaintenance.getFormData();
                    if(protocolInfo != null){
                        //Setting The AC - Type to Insert All The Data For COPY
                        protocolInfo.setAcType("I");
                        
                        //Set all Details Change Flags to true
                        //protocolInfo.getProtocolDetailChangeFlags().setIsProtocolMaintChanged(true);
                        protocolInfo.getProtocolDetailChangeFlags().setIsLocationChanged(true);
                        protocolInfo.getProtocolDetailChangeFlags().setIsInvestigatorChanged(true);
                        protocolInfo.getProtocolDetailChangeFlags().setIsKeyStudyChanged(true);
                        protocolInfo.getProtocolDetailChangeFlags().setIsCorrespondentsChanged(true);
                        protocolInfo.getProtocolDetailChangeFlags().setIsAORChanged(true);
                        protocolInfo.getProtocolDetailChangeFlags().setIsFundingSourceChanged(true);
                        protocolInfo.getProtocolDetailChangeFlags().setIsSubjectsChanged(true);
                        protocolInfo.getProtocolDetailChangeFlags().setIsSpecialRevChanged(true);
                        protocolInfo.getProtocolDetailChangeFlags().setIsNotesChanged(true);
                        protocolInfo.getProtocolDetailChangeFlags().setIsCustomDataChanged(true);
                        protocolInfo.getProtocolDetailChangeFlags().setIsRolesChanged(true);
                        protocolInfo.getProtocolDetailChangeFlags().setIsReferencesChanged(true);
                        //modified for Coeus Enhancement case:#1787 step 10
                        //  protocolInfo.getProtocolDetailChangeFlags().setIsRelatedProjectsChanged(true);
                        Vector investigators = protocolInv.getFormData();
                        protocolInfo.setInvestigators(investigators);
                        //System.out.println("inv data:"+investigators);
                        Vector correspondands = protocolCorr.getFormData();
                        protocolInfo.setCorrespondants(correspondands);
                        //System.out.println("corr data:"+correspondands);
                        if( correspondands != null ) {
                            protocolInfo.setCorrespondenceIndicatorStatus(MODIFIED);
                        }else{
                            protocolInfo.setCorrespondenceIndicatorStatus(NOT_PRESENT);
                        }
                        
                        Vector keyPersonnel
                        = protocolKey.getKeyStudyPersonnelData();
                        protocolInfo.setKeyStudyPersonnel(keyPersonnel);
                        //System.out.println("key data:"+keyPersonnel);
                        if( keyPersonnel != null ) {
                            protocolInfo.setKeyStudyIndicatorStatus( MODIFIED );
                        }else{
                            protocolInfo.setKeyStudyIndicatorStatus( NOT_PRESENT );
                        }
                        
                        //Area of Research
                        Vector protocolAOR = protocolArea.getFormData();
                        protocolInfo.setAreaOfResearch( protocolAOR );
                        //System.out.println("AOR data:"+protocolAOR);
                        // Funding Source
                        Vector fundingSource
                        = protocolFund.getFundingSourceData();
                        protocolInfo.setFundingSources(fundingSource);
                        //System.out.println("fund data:"+fundingSource);
                        if( fundingSource != null ) {
                            protocolInfo.setFundingSourceIndicatorStatus( MODIFIED );
                        }else{
                            protocolInfo.setFundingSourceIndicatorStatus( NOT_PRESENT );
                            
                        }
                        Vector subjects = protocolSubjects.getVulnerableSubjects();
                        //                        protocolInfo.setVulnerableSubjectLists(subjects);
                        protocolInfo.setVulnerableSubjectLists(subjects);
                        //System.out.println("sub data:"+subjects);
                        if(subjects != null){
                            protocolInfo.setVulnerableDataStatus(MODIFIED);
                        }else{
                            protocolInfo.setVulnerableDataStatus(NOT_PRESENT);
                        }
                        // Implemented by Raghunath P.V. for implementing special review
                        //Vector reviews = protocolSpecialReviewForm.getProtocolSpecialReviewData();
                        //protocolInfo.setSpecialReviews(reviews);
                        Vector specialReviews = protocolSpecialReviewForm.getProtocolSpecialReviewData();
                        //System.out.println("sr data:"+specialReviews);
                        protocolInfo.setSpecialReviews(specialReviews);
                        if(specialReviews != null){
                            protocolInfo.setSpecialReviewDataStatus(MODIFIED);
                        }else{
                            protocolInfo.setSpecialReviewDataStatus(NOT_PRESENT);
                        }
                        
                        //                        Vector refNumbers = referenceNumberForm.getFormData();
                        //
                        //                            protocolInfo.setReferences(refNumbers);
                        //                        if(refNumbers != null){
                        //                            protocolInfo.setReferenceIndicatorStatus(MODIFIED);
                        //                            //System.out.println("sr modified in copy");
                        //                        }else{
                        //                            protocolInfo.setReferenceIndicatorStatus(NOT_PRESENT);
                        //                            //System.out.println("sr not present in copy");
                        //                        }
                        
                        //References
                        Vector refNumbers = null;
                        Vector tempRefNumbers = new Vector();
                        if(protocolReferenceNumberForm!=null){
                            if(isProtoRefSaveReq){
                                refNumbers = protocolReferenceNumberForm.getFormData();
                            }
                        }else{
                            refNumbers = protocolInfo.getReferences();
                        }
                        if(refNumbers!=null){
                            ReferencesBean referencesBean = null;
                            for(int indx = 0;indx < refNumbers.size();indx++){
                                referencesBean = (ReferencesBean)refNumbers.elementAt(indx);
                                if(referencesBean.getAcType()==null || referencesBean.getAcType()!="D"){
                                    referencesBean.setAcType("I");
                                    tempRefNumbers.addElement(referencesBean);
                                }
                            }
                            protocolInfo.setReferences(tempRefNumbers);
                            
                            if(tempRefNumbers!=null && tempRefNumbers.size() > 0){
                                protocolInfo.setReferenceIndicatorStatus(MODIFIED);
                            }else{
                                protocolInfo.setReferenceIndicatorStatus(NOT_PRESENT);
                            }
                        }
                        
                        //Custom Elements
                        if( customElementsForm != null && !isDeleted) {
                            Vector customElements = null;
                            Vector tempCustomElements = new Vector();
                            customElements = customElementsForm.getOtherColumnElementData();
                            if(customElements!=null){
                                CustomElementsInfoBean customElementsInfoBean = null;
                                for(int indx = 0;indx < customElements.size();indx++){
                                    customElementsInfoBean = (CustomElementsInfoBean)customElements.elementAt(indx);
                                    if(customElementsInfoBean.getAcType()==null || customElementsInfoBean.getAcType()=="U" || customElementsInfoBean.getAcType()==""){
                                        customElementsInfoBean.setAcType("I");
                                    }
                                    tempCustomElements.addElement(customElementsInfoBean);
                                }
                                protocolInfo.setCustomElements(tempCustomElements);
                            }
                        }
                        //modified for Coeus Enhancement case:#1787 step 11 start
                        // for related projects
                        
                       /* Vector relProj = null;
                        Vector tempRelProj = new Vector();
                        if(protocolRelatedProjects != null){
                            // Add Updated method
                            //                           relProj = protocolRelatedProjects.getFormData();
                            relProj = getRelatedProjectsData();
                        }else{
                            relProj = protocolInfo.getRelatedProjects();
                        }
                        if(relProj != null){
                            ProtocolRelatedProjectsBean relProjBean = null;
                            for(int indx = 0;indx < relProj.size(); indx++ ){
                                relProjBean = (ProtocolRelatedProjectsBean)relProj.elementAt(indx);
                                if(relProjBean.getAcType()==null || !relProjBean.getAcType().equals("D")){
                                    relProjBean.setAcType("I");
                                    tempRelProj.addElement(relProjBean);
                                }
                            }
                            protocolInfo.setRelatedProjects(tempRelProj);
                            
                            if(tempRelProj!=null && tempRelProj.size() > 0){
                                protocolInfo.setProjectsIndicatorStatus(MODIFIED);
                            }else{
                                protocolInfo.setProjectsIndicatorStatus(NOT_PRESENT);
                            }
                        } // end of block for related projects
                        */
                        //modified for Coeus Enhancement case:#1787 step 11 ends
                        
                        // Notepad logic implemented by Senthil K
                        Vector protoAddNotes = protocolNotepadForm.getProtocolVecNotes();
                        //System.out.println("notes data:"+protoAddNotes);
                        if( protoAddNotes != null) {
                            // setting acType to I explicitly in Copy mode.
                            ProtocolNotepadBean noteBean;
                            int count = protoAddNotes.size();
                            for( int indx = 0 ; indx < count; indx++ ) {
                                noteBean = ( ProtocolNotepadBean ) protoAddNotes.elementAt(indx);
                                noteBean.setAcType( "I" );
                                protoAddNotes.set( indx, noteBean );
                            }
                        }
                        protocolInfo.setVecNotepad(protoAddNotes);
                        Vector newUserRoles = null;
                        /* commented copying all the roles from the protocol to be
                         * copied. Only Protocol Co-ordinator will be assigned in server
                         * as it will go as "I" and with SAVE request. dt: 9/24/03
                         */
                         /* beging of Comment
                          
                          
                        if ( rolesForm == null || tempUserRoles == null ){
                            newUserRoles = protocolInfo.getUserRolesInfoBean();
                            //newUserRoles = getTreeDataForUnit( protocolInv.getLeadUnitNumber() );
                        }
                        end of Comment  */
                        /* take only those roles if user explictis assings */
                        if( tempUserRoles != null ) {
                            newUserRoles = tempUserRoles;
                            Hashtable htNewUsers = getUserRoleInfo( newUserRoles );
                            
                            Vector userRolesForDB  = checkModifications(
                            new Hashtable(), htNewUsers );
                            protocolInfo.setUserRoles( userRolesForDB );
                        }
                        
                        Vector amendRenList = amendRenewalForm.getFormData();
                        protocolInfo.setAmendmentRenewal( amendRenList );
                        
                        //COEUSQA:3503 - Protocol Copying - Option to Copy Attachments and Questionnaires - Start
                        protocolInfo.setCopyQnr(isCopyQnr);
                        protocolInfo.setCopyAttachments(isCopyAttachments);
                        protocolInfo.setCopyOtherAttachments(isCopyOtherAttach);
                        protocolInfo.setAwProtocolNumber(protocolId);
                        //COEUSQA:3503 - End
                    }
                }
                /*
                 * added by ravi to send save request as normal protocol save or
                 * save new amendment or save new renewal so that the db lock
                 * can be released properly.
                 */
                //System.out.println("requestedModule:"+requestedModule);
                //System.out.println("functionType:"+functionType);
                if( requestedModule == CoeusGuiConstants.PROTOCOL_AMENDMENT_CODE ) {
                    if( functionType == COPY_MODE ) {
                        int max_ver = getMaxAmendmentVersion(protocolId);
                        versionNumber = decimalFormat.format(max_ver+1);
                        protocolInfo.setProtocolNumber(protocolId+"A"+versionNumber);
                        saveType = SAVE_NEW_AMENDMENT; // code to specify the save request is for new amendment
                        //System.out.println("entered amend block");
                    }
                }else if( requestedModule == CoeusGuiConstants.PROTOCOL_RENEWAL_CODE ){
                    if( functionType == COPY_MODE ) {
                        int max_ver = getMaxRevisionVersion(protocolId);
                        versionNumber = decimalFormat.format(max_ver+1);
                        protocolInfo.setProtocolNumber(protocolId+"R"+versionNumber);
                        saveType = SAVE_NEW_REVISION; // code to specify the save request is for new revision.
                        //System.out.println("entered renewal block");
                    }
                }
            }
        }
        //System.out.println("userRoles before sending to server:"+protocolInfo.getUserRoles());        
        if( functionType != DISPLAY_MODE || lockExist)
        {
        setProtocolIndicators();
        String connectTo = CoeusGuiConstants.CONNECTION_URL + PROTOCOL_SERVLET;
        RequesterBean request = new RequesterBean();
        request.setFunctionType(saveType);
        request.setId(protocolInfo.getProtocolNumber());
        request.setDataObject(protocolInfo);
        AppletServletCommunicator comm = new AppletServletCommunicator(
        connectTo, request);
        comm.send();
        //Coeus Enhancement case #1797 start
        if(functionType == DISPLAY_MODE && lockExist){
            releaseUpdateLock();
            //Added for case#3089 - Special Review Tab made editable in display mode - start
            protocolSpecialReviewForm.setSaveRequired(false);
            setValues(getProtocolDetails(protocolId));
        }
        //Coeus Enhancement case #1797 end
        ResponderBean response = comm.getResponse();
        boolean isError = false;
        //code modified for coeus4.3 enhancement
//        if (response.isSuccessfulResponse()) {
        if (response.isSuccessfulResponse() ||
                (response.getId()!=null && response.getId().equals("MODULE"))) {
            saveRequired = false;
            Vector data = (Vector)response.getDataObjects();    
            protocolInfo = (ProtocolInfoBean)
            data.elementAt(0);
            
            //Protocol Enhancment -  Administrative Correction Start 5
            protocolAction.setSaveRequired(false);
            //Protocol Enhancment -  Administrative Correction End 5
            //code added for coeus4.3 enhancement
            //to show what all are the modules that are not saved in AmendRenewSummary page
            if((response.getId()!=null && response.getId().equals("MODULE"))){
                CoeusOptionPane.showWarningDialog(response.getMessage());
            }
            
              //Case 1787 Start 23
            if(screenMode == DISPLAY_MODE){
                protocolFund.setSaveRequired(false);
                saveRequired = false;
            }
            
           
            //Case 1787 End 23
        } else {
          
          CoeusOptionPane.showErrorDialog(response.getMessage());
          isError = true;
            if (response.isCloseRequired()) {
                mdiForm.removeFrame(referenceTitle, referenceId);
                isTimedOut = true;
                closed = true;
                this.doDefaultCloseAction();
                return;
            }else {
              //Code commented for coeus4.3 Amendments/Renewal enhancements
//               throw new Exception(response.getMessage());
            }
        }
        
        if( protocolInfo != null && !isError) {
            updateData();
            ProtocolFundingSourceForm.protocolInfo = protocolInfo;
        }else {
            
            throw new Exception(coeusMessageResources.parseMessageKey(
            "saveFail_exceptionCode.1102"));
        }
        } 
    }
     private  Hashtable getUserRoleInfoforStub( Vector userRoleDetails ) {
        int rolesSize = userRoleDetails.size();
        Hashtable extractedUsers = new Hashtable();
        ProtocolRolesFormBean userRole;
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
                        userRole = new ProtocolRolesFormBean();
                        userRole.setSequenceNumber(protocolInfo.getSequenceNumber());
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
    private  Hashtable getUserRoleInfo( Vector userRoleDetails ) {
        int rolesSize = userRoleDetails.size();
        Hashtable extractedUsers = new Hashtable();
        ProtocolRolesFormBean userRole;
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
                        userRole = new ProtocolRolesFormBean();
                        userRole.setSequenceNumber(protocolInfo.getSequenceNumber());
                        // COEUSDEV-273: Protocol roles update error - new sequence- Start
                        if(childBean instanceof ProtocolUserRoleInfoBean){
                            ProtocolUserRoleInfoBean protocolUserRoleInfoBean = (ProtocolUserRoleInfoBean)childBean;
                            userRole.setAwSequenceNumber(protocolUserRoleInfoBean.getSequenceNumber());
                        }
                        // COEUSDEV-273: Protocol roles update error - new sequence - End
                        userRole.setUserId(userBean.getUserId());
                        userRole.setRoleId(roleBean.getRoleId());
                        userRole.setUpdateTimestamp(childBean.getUpdateTimestamp());
                        userRole.setUpdateUser(childBean.getUpdateUser());
                        String key = userRole.getRoleId() + userRole.getUserId();
                        
                        
                        extractedUsers.put( key , userRole );
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
                //System.out.println("new id:"+key);
                if( !htExistingUsers.containsKey( key ) ){
                    ProtocolRolesFormBean newRoleBean
                    = ( ProtocolRolesFormBean )htTempUsers.get( key );
                    newRoleBean.setAcType( "I" );
                    protocolInfo.getProtocolDetailChangeFlags().setIsRolesChanged(true);
                    htExistingUsers.put(key, newRoleBean );
                 // COEUSDEV-273: Protocol roles update error - new se & save - Start    
                } else {
                    ProtocolRolesFormBean newRoleBean
                            = ( ProtocolRolesFormBean )htTempUsers.get( key );
                    if(newRoleBean.getAwSequenceNumber() != 0 && (newRoleBean.getAwSequenceNumber() < protocolInfo.getSequenceNumber())){
                        newRoleBean.setAcType( "I" );
                        protocolInfo.getProtocolDetailChangeFlags().setIsRolesChanged(true);
                        htExistingUsers.put(key, newRoleBean );
                    }
                }
               // COEUSDEV-273: Protocol roles update error - new se & save - End 
            }
        }
        Set existingSet = htExistingUsers.keySet();
        if(!existingSet.isEmpty()){ 
            Iterator iterator = existingSet.iterator();
            while(iterator.hasNext()){
                String key = (String)iterator.next();
                if( !htTempUsers.containsKey( key ) ){
                    ProtocolRolesFormBean newRoleBean
                    = ( ProtocolRolesFormBean )htExistingUsers.get( key );
                    newRoleBean.setAcType( "D" );
                    protocolInfo.getProtocolDetailChangeFlags().setIsRolesChanged(true);
                    htExistingUsers.put(key, newRoleBean );
                // COEUSDEV-273: Protocol roles update error - new se & save - Start    
                } else {
                    ProtocolRolesFormBean newRoleBean
                            = ( ProtocolRolesFormBean )htExistingUsers.get( key );
                    if(newRoleBean.getAwSequenceNumber() != 0 && (newRoleBean.getAwSequenceNumber() < protocolInfo.getSequenceNumber())){
                        newRoleBean.setAcType( "I" );
                        protocolInfo.getProtocolDetailChangeFlags().setIsRolesChanged(true);
                        htExistingUsers.put(key, newRoleBean );
                    }
                }
              // COEUSDEV-273: Protocol roles update error - new se & save - End  
            }
        }
        
        return new Vector( htExistingUsers.values() );
    }
    
    public void registerObserver( Observer observer ) {
        observable.addObserver( observer );
    }
    
    private void updateData() throws Exception{
        /** begin: fixed bug with id #147  */
       /*setStatusMessage(coeusMessageResources.parseMessageKey(
            "general_saveCode.2275"));*/
        /** end: fixed bug with id #147  */
        saveType = 'S'; // setting save type to normal update as the record will be locked with entire protocol no. after first save in amendment / renewal
        this.saveRequired = false;
        this.protocolId = protocolInfo.getProtocolNumber();
        this.sequenceId = protocolInfo.getSequenceNumber();
        if( functionType == DISPLAY_MODE ) {
            protocolMaintenance.setValues(protocolInfo);
            protocolVecNotes = protocolInfo.getVecNotepad();
            protocolNotepadForm.setProtocolVecNotes(protocolVecNotes);
            protocolNotepadForm.setSaveRequired(false);
            //Added GN443-Issue#108 for Modify and load special review and fundine source in display mode-start  
            protocolSpecialReviewData = protocolInfo.getSpecialReviews();
            if((protocolSpecialReviewData !=null) && protocolSpecialReviewData.size()>0){
            protocolSpecialReviewForm.setProtocolSpecialReviewData(protocolSpecialReviewData);
            protocolSpecialReviewForm.setSaveRequired(false);
            }
            protFundingSource = protocolInfo.getFundingSources(); 
            if((protFundingSource !=null) && (protFundingSource.size()>0)){
            protocolFund.setFundingSourceData(protFundingSource);
            protocolFund.setSaveRequired(false);               
            }
            //Added GN443-Issue#108 for Modify and load special review and fundine source in display mode-end
            if( rolesForm != null) {
                rolesForm.setSaveRequired(false);
            }
            tempUserRoles = null;
            userAndRoleDetails = null;
            exisitingUserRoles = protocolInfo.getUserRolesInfoBean();
            copyOfExistingUserRoles = (Vector) ObjectCloner.deepCopy(protocolInfo.getUserRolesInfoBean());
            if( requestedModule == CoeusGuiConstants.PROTOCOL_DETAIL_CODE ) {
                observable.setFunctionType(functionType);
                observable.notifyObservers(protocolInfo);
            }
            return;
        }
        btnSummary.setEnabled( true );
        //raghuSV : to enable submission details menu item
        //starts...
        submissionDetails.setEnabled(true);
        //ends
        //Added for case 3612 - Routing Issues -start
        //Enable the Show Routing menuitem if the status of protocol is Pending
       if(submitEnabledProtocolStatusCodes.contains(new Integer(protocolInfo.getProtocolStatusCode()))){
            mnuItmShowRouting.setEnabled(true);
        }else{
            mnuItmShowRouting.setEnabled(false);
        }
        //Added for case 3612 - Routing Issues -end
        // Commented for COEUSQA-3820 : Coeus 4.5.1: Protocol Routing Issue - Record Close Error 
//        if( requestedModule == CoeusGuiConstants.PROTOCOL_DETAIL_CODE ) {
            observable.setFunctionType(functionType);
            // Commented for COEUSQA-3852 : Unable to add person to amendment cleanly - Start
            // Added for COEUSQA-3820 : Coeus 4.5.1: Protocol Routing Issue - Record Close Error - Start
//            protocolInfo.setInvestigators(protocolInv.getFormData());
            // Added for COEUSQA-3820 : Coeus 4.5.1: Protocol Routing Issue - Record Close Error - End
            // Commented for COEUSQA-3852 : Unable to add person to amendment cleanly - Start
            observable.notifyObservers(protocolInfo);
            //updateRow();
            
            //Bug Fix:1359 Start 2
            //if(this.getTitle().indexOf('-') == -1 ){
            if(strTitle.indexOf('-') == -1){
                edu.mit.coeus.gui.menu.CoeusWindowMenu windowMenu = mdiForm.getWindowMenu();
                if( windowMenu != null ){
                    windowMenu = windowMenu.removeMenuItem( strTitle );
                }
                strTitle = getTitle() + " - " + protocolInfo.getProtocolNumber();
                setTitle(strTitle);
                windowMenu = windowMenu.addNewMenuItem( strTitle, this );
                windowMenu.updateCheckBoxMenuItemState( strTitle, true );
            }
            //Bug Fix:1359 End 2
//        }
        // To get the editable modules collection.
        HashMap hmModeDetails = getModeDetails();
        hmModeDetails.put("functionType", new Character(MODIFY_MODE));
        
        //code modified and added for coeus4.3 enhancements
        // To check whether the module is editable.         
        hmModeDetails.put("moduleId", IrbWindowConstants.GENERAL_INFO);
        functionType = MODIFY_MODE;
        screenMode = functionType;
        protocolMaintenance.setFunctionType(getMode(hmModeDetails));
        
        //For Protocol locations
        //code modified and added for coeus4.3 enhancements
        // To check whether the module is editable.         
        hmModeDetails.put("moduleId", IrbWindowConstants.ORGANIZATION);
        protocolMaintenance.setLocationFunctionType(getMode(hmModeDetails));
//        protocolMaintenance.setFunctionType(MODIFY_MODE);
        protocolMaintenance.setSaveRequired(false);
        protocolMaintenance.setValues(protocolInfo);
        if( requestedModule != CoeusGuiConstants.PROTOCOL_DETAIL_CODE ) {
            protocolMaintenance.setProtocolNumber(protocolId.substring(0,10));
        }
        
        //code modified and added for coeus4.3 enhancements
        // To check whether the module is editable.         
        hmModeDetails.put("moduleId", IrbWindowConstants.INVESTIGATOR);
//        protocolInv.setFunctionType(MODIFY_MODE);
        protocolInv.setFunctionType(getMode(hmModeDetails));
        protocolInv.setSaveRequired(false);
        protocolInv.setInvestigatorData(
        protocolInfo.getInvestigators());
        
        //code modified and added for coeus4.3 enhancements
        // To check whether the module is editable.         
        hmModeDetails.put("moduleId", IrbWindowConstants.CORRESPONDENTS);
//        protocolCorr.setFunctionType(MODIFY_MODE);
        protocolCorr.setFunctionType(getMode(hmModeDetails));    
        //Changes
        protCorrespondents = protocolInfo.getCorrespondetns();
        protocolCorr.setFormData(protCorrespondents);
        //protocolCorr.setFormData(protocolInfo.getCorrespondetns());
        protocolCorr.setSaveRequired(false);
        
        //code modified and added for coeus4.3 enhancements
        // To check whether the module is editable.         
        hmModeDetails.put("moduleId", IrbWindowConstants.KEY_STUDY_PERSONS);
//        protocolKey.setFunctionType(MODIFY_MODE);
        protocolKey.setFunctionType(getMode(hmModeDetails));     
        protKeyStudyPersonnel = protocolInfo.getKeyStudyPersonnel();
        //System.out.println("key data after saving:"+ protKeyStudyPersonnel);
        protocolKey.setKeyStudyPersonnelData(protKeyStudyPersonnel);
        protocolKey.formatFields();
        protocolKey.setSaveRequired(false);
        
        //code modified and added for coeus4.3 enhancements
        // To check whether the module is editable.         
        hmModeDetails.put("moduleId", IrbWindowConstants.FUNDING_SOURCE);
//        protocolFund.setFunctionType(MODIFY_MODE);
        protocolFund.setFunctionType(getMode(hmModeDetails));
        protFundingSource = protocolInfo.getFundingSources();
        //System.out.println("fund data after saving:"+protFundingSource);
        protocolFund.setFundingSourceData(protFundingSource);
        protocolFund.setSaveRequired(false);

        //code modified and added for coeus4.3 enhancements
        // To check whether the module is editable.         
        hmModeDetails.put("moduleId", IrbWindowConstants.AREA_OF_RESEARCH);
//        protocolArea.setFunctionType(MODIFY_MODE);
        protocolArea.setFunctionType(getMode(hmModeDetails));
        protocolArea.setFormData(protocolInfo.getAreaOfResearch());
        protocolArea.setSaveRequired(false);
        
        //code modified and added for coeus4.3 enhancements
        // To check whether the module is editable. 
        hmModeDetails.put("moduleId", IrbWindowConstants.SUBJECTS);
//        protocolSubjects.setFunctionType(MODIFY_MODE);
        protocolSubjects.setFunctionType(getMode(hmModeDetails));
        selectedSubjects = protocolInfo.getVulnerableSubjectLists();
        protocolSubjects.setValues(selectedSubjects);
        protocolSubjects.setSaveRequired(false);
        
        //code modified and added for coeus4.3 enhancements
        // To check whether the module is editable.         
        // Need To implement for Protocol Special Review Form
        hmModeDetails.put("moduleId", IrbWindowConstants.SPECIAL_REVIEW);        
//        protocolSpecialReviewForm.setFunctionType(MODIFY_MODE);
        protocolSpecialReviewForm.setFunctionType(getMode(hmModeDetails));
        protocolSpecialReviewData = protocolInfo.getSpecialReviews();
        protocolSpecialReviewForm.setProtocolSpecialReviewData(protocolSpecialReviewData);
        protocolSpecialReviewForm.setSaveRequired(false);
        
        //        referenceNumberForm.setFunctionType(MODIFY_MODE);
        protReferenceData = protocolInfo.getReferences();
        //        referenceNumberForm.setVecReferenceNumbersData( protReferenceData );
        //        referenceNumberForm.setSaveRequired(false);
        
        //code modified and added for coeus4.3 enhancements
        // To check whether the module is editable.         
//        hmModeDetails.put("moduleId", IrbWindowConstants.NOTES);
        protocolNotepadForm.setFunctionType(MODIFY_MODE);
//        protocolNotepadForm.setFunctionType(getMode(hmModeDetails));
        protocolVecNotes = protocolInfo.getVecNotepad();
        protocolNotepadForm.setProtocolVecNotes(protocolVecNotes);
        protocolNotepadForm.setSaveRequired(false);
        //To update the datas for the summary page.
        if(pnlAmendments!=null){
            if(requestedModule != CoeusGuiConstants.PROTOCOL_DETAIL_CODE){
                AmendmentRenewalSummary amendmentRenewalSummary = 
                    (AmendmentRenewalSummary) pnlAmendments.getComponent(0);
                // Modified for COEUSQA-3820 : Coeus 4.5.1: Protocol Routing Issue - Record Close Error - Start
//                amendmentRenewalSummary.setHmModuleData((HashMap)protocolInfo.getAmendRenewEditableModules().get(0));                
                Vector vecEditableModules = protocolInfo.getAmendRenewEditableModules();
                if(vecEditableModules != null && !vecEditableModules.isEmpty()){
                    amendmentRenewalSummary.setHmModuleData((HashMap)vecEditableModules.get(0));
                }
                // Modified for COEUSQA-3820 : Coeus 4.5.1: Protocol Routing Issue - Record Close Error - End
                amendmentRenewalSummary.setValues(protocolInfo.getAmendmentRenewal());
            }
        }
        //code modified and added for coeus4.3 enhancements
        // To check whether the module is editable.         
        hmModeDetails.put("moduleId", IrbWindowConstants.OTHERS);
        protCustomElements = protocolInfo.getCustomElements();
        if( customElementsForm != null ) {
            customElementsForm.setFunctionType(getMode(hmModeDetails));
            //System.out.println("setting custom data after saving:"+protCustomElements);
            customElementsForm.setPersonColumnValues(protCustomElements);
            customElementsForm.setSaveRequired(false);
        }
        
        if( protocolReferenceNumberForm != null){
            
            protocolReferenceNumberForm.setSaveRequired(false);
            isProtoRefSaveReq = false;
            protReplicateRefData = null;
            tempReferences = null;
        }
        
        if( rolesForm != null ){
            rolesForm.setSaveRequired( false );
        }
       //modified for Coeus Enhancement case:#1787 step 12 start 
       /* protRelProjects = protocolInfo.getRelatedProjects();
        setRelatedProjectsData(protocolInfo.getRelatedProjects());
        vecProtoRelProjTemp = protocolInfo.getRelatedProjects();
        
        if(protocolRelatedProjects != null){
            protocolRelatedProjects.setSaveRequired(false);
            isProtoRelSaveReq = false;
            
        }
        */
       //modified for Coeus Enhancement case:#1787 step 12 start 
        amendRenewalForm.setFunctionType( MODIFY_MODE );
        amendRenewalForm.setSaveRequired( false );
        //System.out.println("amend data after save:"+ protocolInfo.getAmendmentRenewal());
        amendRenewalForm.setValues( protocolInfo.getAmendmentRenewal() );
        //Added for coeus4.3 concurrent Amendments and Renewals  enhancements - starts
        Vector vecAmendRenewData = protocolInfo.getAmendmentRenewal();
        if(vecAmendRenewData!=null && vecAmendRenewData.size() > 0){
            ProtocolAmendRenewalBean bean =(ProtocolAmendRenewalBean) vecAmendRenewData.get(0);
            setVecModuleData((Vector)
            ObjectCloner.deepCopy(bean.getVecModuleData()));
        }
        originalFunctionType = 'M';
        setOriginalProtocolInfo((ProtocolInfoBean) ObjectCloner.deepCopy(protocolInfo));
        //Added for coeus4.3 concurrent Amendments and Renewals  enhancements - ends
        
        //Added for case 3552 - Protocol Attachments - start
        hmModeDetails.put("moduleId", IrbWindowConstants.UPLOAD_DOCUMENTS);
        char attachmentFunctionType = getMode(hmModeDetails);
        protocolAttachmentsForm.setFunctionType(attachmentFunctionType);
        protocolAttachmentsForm.setOtherFunctionType(attachmentFunctionType);
        protocolAttachmentsForm.setProtocolInfoBean(protocolInfo);
        protocolAttachmentsForm.formatFields();
        if(tbdPnProtocol.getSelectedIndex() == ATTACHMENTS_TAB){
            protocolAttachmentsForm.setFormData();
        }
        //Added for case 3552 - Protocol Attachments - end
        tempUserRoles = null;
        userAndRoleDetails = null;
        exisitingUserRoles = protocolInfo.getUserRolesInfoBean();
        copyOfExistingUserRoles = (Vector) ObjectCloner.deepCopy(protocolInfo.getUserRolesInfoBean());
        setSelected(true);
        setVisible(true);
        
    }
    
    /**
     * This method is used to show the alert message to the user with the
     * specified message.
     *
     * @param mesg String representing the message to be displayed to the user.
     */
    public void log(String mesg) {
        CoeusOptionPane.showErrorDialog(mesg);
    }
    
    
    private Vector getTreeDataForUnit( String unitNum ) {
        RequesterBean requester = new RequesterBean();
        Vector treeForUnit = null;
        requester.setFunctionType(GET_TREE_DATA_FOR_UNIT);
        requester.setId( unitNum );
        String connectTo =CoeusGuiConstants.CONNECTION_URL
        + PROTOCOL_SERVLET;
        AppletServletCommunicator comm
        = new AppletServletCommunicator(connectTo,requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        if ((response != null )
        && (!response.isSuccessfulResponse())){
            CoeusOptionPane.showErrorDialog(
            response.getMessage());
            return null;
        }else{
            treeForUnit = ( Vector ) response.getDataObject();
        }
        return treeForUnit;
    }
     private String getDevelopmentProposalInfo(String id){
        Vector vUnitsearchData=null;
        RequesterBean requester = new RequesterBean();
        requester.setFunctionType('t');
        requester.setId(id);
        String connectTo =CoeusGuiConstants.CONNECTION_URL  + "/ProposalMaintenanceServlet";
        AppletServletCommunicator comm
        = new AppletServletCommunicator(connectTo, requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        String proposalTitle ="";

            if (response!=null && response.getDataObject()!=null){

            proposalTitle = (String)response.getDataObject();

        }

        return proposalTitle;
    }
    /** Overridden method of <CODE>ActionListener</CODE>. All the  actions of
     * the menu and toolbar associated with the <CODE>ProtocolDetailsForm</CODE> will be invoked from
     * this method.
     * @param ae <CODE>ActionEvent</CODE>, a semantic event which indicates that a
     * component-defined action occured.
     */
    public void actionPerformed(ActionEvent ae){
        try {
             Object actSource = ae.getSource();
            if (actSource.equals(protocolFund.btnStartProposal)){

                String proposalNumber=saveProposal();
                if(proposalNumber!=null){
                int selRow= protocolFund.tblFundSoForm.getSelectedRow();
                String title = getDevelopmentProposalInfo(proposalNumber);
                                //stNm=(String)vecData.get(0);
                                protocolFund.tblFundSoForm.getModel().
                                setValueAt(proposalNumber,selRow,2);
                                protocolFund.tblFundSoForm.getModel().
                                setValueAt(title,selRow,3);
                                protocolFund.tblFundSoForm.getSelectionModel().
                                setLeadSelectionIndex(selRow);
                                protocolFund.tblFundSoForm.getSelectionModel().
                                setLeadSelectionIndex(selRow);
                                saveRequired = true;
                                if(protocolFund.tblFundSoForm.getCellEditor()!=null){
                                    protocolFund.tblFundSoForm.getCellEditor().
                                    cancelCellEditing();
                                }
                protocolFund.btnStartProposal.setEnabled(false);
                }
            }
//              protocolFund.btnStartProposal.addActionListener(new ActionListener()
//                             {
//                             public void actionPerformed(java.awt.event.ActionEvent evt) {
//
//
//
//                               }
//             });

//            String protocolNumber = null;
//            int seqNumber;
           
             protocolInfo = protocolMaintenance.getFormData();
            //Modified for Case#3091 - IRB - generate a protocol summary pdf  - Start
            //if (actSource.equals(btnSaveProtocol)){
            if (actSource.equals(btnSaveProtocol) || actSource.equals(mnuItmSave)) {//Case#3091 - End
                //Case :#3149 Tabbing between fields does not work on Others tabs - Start
                 if(getFunctionType() != DISPLAY_MODE &&  customElementsForm != null &&
                         customElementsForm.getTable().isEnabled()){
                     customElementsForm.setSaveAction(true);
                     if(tbdPnProtocol.getSelectedIndex() == OTHERS_TAB ) {
                         customTable = customElementsForm.getTable();
                         row = customElementsForm.getRow();
                         column = customElementsForm.getColumn();
                         count = 0;
                         isotherTabSelected = true;
                     }
                 }
                 //Case :#3149 - End
                if(isSaveRequired() || functionType == ADD_MODE){
                    //Coeus4.3 email notification implementation - start
                    char tempFunctionType = functionType;
                    setProtocolInfo();
                    //COEUSQA:3315 - IACUC Protocol Actions Blanking Completed Questionnaires - Start
                    checkQuestionnaireRevision();
                    //COEUSQA:3315 - End
                    // COEUSQA-2105: No notification for some IRB actions - Start
                    if(mode == CoeusGuiConstants.ADMINISTRATIVE_CORRECTIONS && !adminCorrectionMailSent){
                        
                        ProtocolMailController mailController = new ProtocolMailController();
                        synchronized(mailController) {
                            mailController.sendMail(ACTION_ADMINISTRATIVE_CORRECTION, protocolInfo.getProtocolNumber(),protocolInfo.getSequenceNumber());
                            
                        }
                        adminCorrectionMailSent = true;
                    }
                    // COEUSQA-2105: No notification for some IRB actions - End
                    //Added for case 4350 - Protocol - Creating renewals action is not sending mails - Start
                    if(tempFunctionType == ADD_MODE) {
                        ProtocolMailController mailController = new ProtocolMailController();
                        synchronized(mailController) {
                            mailController.sendMail(ACTION_NEW_PROTOCOL, protocolInfo.getProtocolNumber(), protocolInfo.getSequenceNumber());
                            //100 - Protocol Creation
                        }
                    }
                    else if(tempFunctionType == COPY_MODE) {
                        if(requestedModule == CoeusGuiConstants.PROTOCOL_AMENDMENT_CODE) {
                            ProtocolMailController mailController = new ProtocolMailController();
                            synchronized(mailController) {
                                mailController.sendMail(ACTION_NEW_AMENDMENT, protocolInfo.getProtocolNumber(),protocolInfo.getSequenceNumber());
                                //103 - Amendment Creation
                            }
                        }else if(requestedModule == CoeusGuiConstants.PROTOCOL_RENEWAL_CODE) {
                            ProtocolMailController mailController = new ProtocolMailController();
                            synchronized(mailController) {
                                mailController.sendMail(ACTION_NEW_RENEWAL, protocolInfo.getProtocolNumber(), protocolInfo.getSequenceNumber());
                                //102 - Renewal Creation
                            }
                        }else if(requestedModule == CoeusGuiConstants.PROTOCOL_AMENDMENT_WITH_RENEWAL_CODE) {
                            ProtocolMailController mailController = new ProtocolMailController();
                            synchronized(mailController) {
                                mailController.sendMail(ACTION_NEW_RENEW_WITH_AMEND, protocolInfo.getProtocolNumber(), protocolInfo.getSequenceNumber());
                                //117 - Renewal with Amendment Creation
                            }
                        }
                    }
                    //Added for case 4350 - Protocol - Creating renewals action is not sending mails - End
                    //Coeus4.3 email notification implementation - end
                    //Modified for case 3552 - IRB attachments - start
                    saveSuccessful = true;
                     //Case :#3149 Tabbing between fields does not work on Others tabs - Start
                    
                    if(tbdPnProtocol.getSelectedIndex() == OTHERS_TAB && getFunctionType() != DISPLAY_MODE && 
                            customElementsForm != null && customElementsForm.getTable().isEnabled()){
                        boolean[] lookUpAvailable = customElementsForm.getLookUpAvailable();
                        customTable = customElementsForm.getTable();
                        row = customElementsForm.getRow();
                        column = customElementsForm.getColumn();
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
                    }
                    
                    //Case :#3149 - End
                    //Modified for case 3552 - IRB attachments - end
                }
            //Modified for Case#3091 - IRB - generate a protocol summary pdf  - Start          
            //} else if (actSource.equals(btnCloseProtocol)) {
            } else if (actSource.equals(btnCloseProtocol) || actSource.equals(mnuItmClose)) {//Case#3091 - End
                String msg = coeusMessageResources.parseMessageKey(
                "saveConfirmCode.1002");
                /* before closing this window check for saveRequired flag and
                 * confirm the user for saving the information.
                 */
                if ( isSaveRequired()) {
                    int confirm = CoeusOptionPane.showQuestionDialog(msg,
                    CoeusOptionPane.OPTION_YES_NO_CANCEL,
                    CoeusOptionPane.DEFAULT_YES);
                    switch(confirm){
                        case(JOptionPane.YES_OPTION):
                            try{
                                /* save the ProtocolInfo and remove this frame
                                 * instance from the mdiForm.And get the
                                 * Protocol Base frame instance from the
                                 * mdiForm and set it as selected.
                                 */
                                //Coeus4.3 email notification implementation - start
                                char tempFunctionType = functionType;
                                setProtocolInfo();
                                //Added for case 4350 - Protocol - Creating renewals action is not sending mails - Start
                                if(tempFunctionType == ADD_MODE) {
                                    ProtocolMailController mailController = new ProtocolMailController();
                                    synchronized(mailController) {
                                        mailController.sendMail(ACTION_NEW_PROTOCOL, protocolInfo.getProtocolNumber(), protocolInfo.getSequenceNumber());
                                        //100 - Protocol Creation
                                    }
                                }
                               else if(tempFunctionType == COPY_MODE) {
                                    if(requestedModule == CoeusGuiConstants.PROTOCOL_AMENDMENT_CODE) {
                                        ProtocolMailController mailController = new ProtocolMailController();
                                        synchronized(mailController) {
                                            mailController.sendMail(ACTION_NEW_AMENDMENT, protocolInfo.getProtocolNumber(), protocolInfo.getSequenceNumber());
                                            //103 - Amendment Creation
                                        }
                                    }else if(requestedModule == CoeusGuiConstants.PROTOCOL_RENEWAL_CODE) {
                                        ProtocolMailController mailController = new ProtocolMailController();
                                        synchronized(mailController) {
                                            mailController.sendMail(ACTION_NEW_RENEWAL, protocolInfo.getProtocolNumber(), protocolInfo.getSequenceNumber());
                                            //102 - Renewal Creation
                                        }
                                    }else if(requestedModule == CoeusGuiConstants.PROTOCOL_AMENDMENT_WITH_RENEWAL_CODE) {
                                        ProtocolMailController mailController = new ProtocolMailController();
                                        synchronized(mailController) {
                                            mailController.sendMail(ACTION_NEW_RENEW_WITH_AMEND, protocolInfo.getProtocolNumber(),protocolInfo.getSequenceNumber());
                                            //117 - Renewal with Amendment Creation
                                        }
                                    }
                               }
                                //Added for case 4350 - Protocol - Creating renewals action is not sending mails - End
                                //Coeus4.3 email notification implementation - end
                                closed = true;
                                mdiForm.removeFrame(referenceTitle,referenceId);
                                this.doDefaultCloseAction();
                            }catch(Exception ex){
                                //Code commented for coeus4.3 enhancements
                                //To avoid multiple messages throwing, when lock is deleted
//                                String exMsg = ex.getMessage();
//                                CoeusOptionPane.showWarningDialog(exMsg);
//                                throw new Exception(
//                                coeusMessageResources.parseMessageKey(
//                                "protoDetFrm_exceptionCode.1130"));
                            }
                            break;
                        case(JOptionPane.NO_OPTION):
                            /* remove this frame
                             * instance from the mdiForm.And get the
                             * Protocol Base frame instance from the
                             * mdiForm and set it as selected.
                             */
                            //Modified*
                            saveRequired= false;
                            closed = true;
                            //Added*
                            protocolMaintenance.setSaveRequired(false);
                            protocolInv.setSaveRequired(false);
                            protocolCorr.setSaveRequired(false);
                            protocolKey.setSaveRequired(false);
                            protocolFund.setSaveRequired(false);
                            protocolArea.setSaveRequired(false);
                            if( rolesForm != null ){
                                rolesForm.setSaveRequired( false );
                            }
                            
                            protocolSubjects.setSaveRequired(false);
                            protocolSpecialReviewForm.setSaveRequired(false);
                            mdiForm.removeFrame(referenceTitle,referenceId);
                            //To be confirmed..*
                            protocolInfo = null;
                            this.doDefaultCloseAction();
                            break;
                        case(JOptionPane.CANCEL_OPTION):
                              //Case :#3149 Tabbing between fields does not work on Others tabs - Start
                            if(tbdPnProtocol.getSelectedIndex() == OTHERS_TAB && customElementsForm != null && 
                                                customElementsForm.getTable().isEnabled()){
                                boolean[] lookUpAvailable = customElementsForm.getLookUpAvailable();
                                customTable = customElementsForm.getTable();
                                row = customElementsForm.getRow();
                                column = customElementsForm.getColumn();
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
                            }
                            break;
                    }
                    
                }else {
                    //To be confirmed..
                    protocolInfo=null;
                    closed = true;
                    mdiForm.removeFrame(referenceTitle, referenceId);
                    this.doDefaultCloseAction();
                }
            }else if(actSource.equals(protocolSubmit)){
                //Coeus Enhancement case #1797
                
           //  if(functionType != DISPLAY_MODE || (submitEnabledProtocolStatusCodes.contains(new Integer(protocolInfo.getProtocolStatusCode())))){
                 // if(functionType != DISPLAY_MODE)
                  //has to be uncommented for locking in submit    
                  /* boolean lockAvailable = false;
                    if(functionType == DISPLAY_MODE)
                   {
                       String connectTo = CoeusGuiConstants.CONNECTION_URL + PROTOCOL_SERVLET;
                        RequesterBean request = new RequesterBean();
                        request.setFunctionType('p');
                        request.setId(protocolId);
                        AppletServletCommunicator comm = new AppletServletCommunicator(
                        connectTo, request);
                        comm.send();
                        ResponderBean response = comm.getResponse();
                        if(response==null){
                            CoeusOptionPane.showErrorDialog("Could not contact server");
                        }
                        else 
                        {  
                           lockAvailable =((Boolean)response.getDataObject()).booleanValue();
                           if(!lockAvailable)
                           
                                {
                                 CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("protocolDetForm_exceptionCode.1019"));

                                }
                           }
                        
                        }*/
                    
                   // }
                    //Code added for coeus4.3 Amendments/Renewal enhancements - starts                            //Added for coeus4.3 enhancement
                    //To get all the editable modules list.
                    HashMap hmModeDetails = getModeDetails();
                    // To check whether the module is editable.    
                    hmModeDetails.put("moduleId", IrbWindowConstants.OTHERS);
                    hmModeDetails.put("functionType", new Character('M'));
                    //Code added for coeus4.3 Amendments/Renewal enhancements - ends                    
                    if(( checkMandatoryData(protocolInfo) || isSaveRequired() || 
                            getMode(hmModeDetails) != 'D' && !customElementsForm.validateData())){
                        setProtocolInfo();
                    }
                     
                   lockExist = lockProtocol();
                       
                             
                   //has to be uncommented for locking in submit
                   if(functionType != DISPLAY_MODE || lockExist)
                   {
                    
                   
                   
                    
                    if((protocolId != null) && (protocolId.trim().length() > 0)){
                    // COEUSDEV-202: Error about mandatory questionnaire - Does not identify the questionnaire - Start
//                        // 4272: Maintain History of Questionnaires - Start
//                        int mandatoryQnrCompleted = validateQuestionnaireCompleted();
//                        if(mandatoryQnrCompleted == 1){
//                            CoeusOptionPane.showWarningDialog(coeusMessageResources.parseMessageKey("questionnaire_exceptionCode.1022"));
//                            return;
//                        } else if(mandatoryQnrCompleted == 2){
//                            CoeusOptionPane.showWarningDialog(coeusMessageResources.parseMessageKey("questionnaire_exceptionCode.1023"));
//                            return;
//                        }
                        // Added with CoeusQA2313: Completion of Questionnaire for Submission
                        checkQuestionnaireRevision();
                        // CoeusQA2313: Completion of Questionnaire for Submission - End
//                        // 4272: Maintain History of Questionnaires - End
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
                                validationMessage = coeusMessageResources.parseMessageKey("questionnaire_exceptionCode.1022");
                                
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
                                validationMessage = validationMessage + coeusMessageResources.parseMessageKey("questionnaire_exceptionCode.1029") ;
                                
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
                                validationMessage = validationMessage  + coeusMessageResources.parseMessageKey("questionnaire_exceptionCode.1023") ;
                                
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
                        
                        //                      commented by ravi on 22/10/03 as we are creating new
                        //                      submissions - START
                        //                        RequesterBean requester = new RequesterBean();
                        //                        requester.setFunctionType('P');
                        //                        requester.setId(protocolId);
                        //                        String connectTo =CoeusGuiConstants.CONNECTION_URL
                        //                        + PROTOCOL_SUB_SERVLET;
                        //                        AppletServletCommunicator comm
                        //                        = new AppletServletCommunicator(connectTo,requester);
                        //                        comm.send();
                        //                        ResponderBean response = comm.getResponse();
                        //                        if ((response != null )
                        //                        && (!response.isSuccessfulResponse())){
                        //                            CoeusOptionPane.showErrorDialog(
                        //                            response.getMessage());
                        //                            return;
                        //                        }else{
                        //                            boolean isMinutePresent
                        //                            = ((Boolean)response.getDataObject()
                        //                            ).booleanValue();
                        //                            if(!isMinutePresent){
                        //                  commenting - END
                        //Added for the case# COEUSQA-1403 Implement validation based on rules in protocols-start
                        Vector vecBrokenRules = getBrokenRulesData();
                        if(vecBrokenRules != null && vecBrokenRules.size()>0){                            
                            edu.mit.coeus.routing.gui.RoutingValidationChecksForm valChecks =
                                new edu.mit.coeus.routing.gui.RoutingValidationChecksForm(mdiForm,vecBrokenRules,7, protocolId );
                            valChecks.display();
                            //Confirm Message Window before Submitting for Approval if Validation rules failed
                            if(valChecks.isError() || !confirmSubmitForApproval()){
                                return;
                            }
                        }else {
                            CoeusOptionPane.showInfoDialog(
                                    coeusMessageResources.parseMessageKey("validationChecks_exceptionCode.1904"));
                        }                        
                        //Added for the case# COEUSQA-1403 Implement validation based on rules in protocols-end
                        submissionForm = new ProtocolSubmissionForm(
                        CoeusGuiConstants.getMDIForm(),
                        "Protocol Submission", true,protocolInfo);
                        //Added for case 2785 - Routing enahncement - start
                        //Set the lead unit of the protocol in the submission form
                        if(protocolInv!=null){
                            submissionForm.setLeadUnitNumber(protocolInv.getLeadUnitNumber());
                        }
                        //Added for case 2785 - Routing enahncement - end
                        Dimension screenSize
                        = Toolkit.getDefaultToolkit().getScreenSize();
                        Dimension dlgSize = submissionForm.getSize();
                        submissionForm.setLocation(
                        screenSize.width/2 - (dlgSize.width/2),
                        screenSize.height/2 - (dlgSize.height/2));
                        // Added by chandra. File IRB SystemTestingDl-01 Bug #36
                        submissionForm.setResizable(false);
                        // End Bug Fixing
                        submissionForm.setDefaultCloseOperation(
                        JDialog.DO_NOTHING_ON_CLOSE);
                        
                        submissionForm.addWindowListener(
                        new WindowAdapter(){
                            public void windowClosing(WindowEvent we){
                                saveSubmissionDetails();
                            }
                        });
                        submissionForm.addEscapeKeyListener(escapeKeyListener);
                        submissionForm.requestDefaultFocusForComponent();
                        submissionForm.show();
                        if( submissionForm.isDataSaved()) {
                            protocolInfo
                            = submissionForm.getProtocolDetails();
                            // Added for COEUSQA-3820 : Coeus 4.5.1: Protocol Routing Issue - Record Close Error - Start
                            if(107 == protocolInfo.getProtocolStatusCode() || // Routing In Progress
                                    101 == protocolInfo.getProtocolStatusCode()){ // Submitted to IRB
                                saveRequired = false;
                            }
                            // Added for COEUSQA-3820 : Coeus 4.5.1: Protocol Routing Issue - Record Close Error - End
                            //Modified for case 2785 - Routing enhancements - start
                            //if maps are present and the last approver has approved
                            //then proceed with other actions
                            //if any approver has rejected set the protocol status to Pending/In Progress
                            boolean proceedWithSubmissionActions = false;
                            if(submissionForm.isMapsFound() ){
                                if(submissionForm.getSubmissionStatusCode() == SUBMIT_TO_COMMITTEE){
                                    proceedWithSubmissionActions = true;
                                }
                                //COEUSQA-1433 - Allow Recall from Routing - Start
                                //else if(submissionForm.getSubmissionStatusCode() == REJECTED_SUBMISSION){
                                // Modified for COEUSQA-3748 : Protocols status is not updating correctly, when a protocols is rejected in routing - Start
//                                else if(submissionForm.getSubmissionStatusCode() == REJECTED_SUBMISSION || submissionForm.getSubmissionStatusCode() == RECALLED_SUBMISSION){
//                                    //COEUSQA-1433 - Allow Recall from Routing - End
//                                    protocolInfo.setProtocolStatusCode(100);
//                                    protocolInfo.setProtocolStatusDesc("Pending/In Progress");
//                                }
                                 protocolInfo = getProtocolinfo(protocolInfo.getProtocolNumber());
                                 protocolInfo.setInvestigators(protocolInv.getFormData());
                                // Modified for COEUSQA-3748 : Protocols status is not updating correctly, when a protocols is rejected in routing - End
                            }else{
                                proceedWithSubmissionActions = true;
                            }
                            // Added for COEUSQA-3820 : Coeus 4.5.1: Protocol Routing Issue - Record Close Error  - Start
                            functionType = TypeConstants.DISPLAY_MODE;
                            observable.setFunctionType(functionType);
                            observable.notifyObservers(protocolInfo);
                            // Added for COEUSQA-3820 : Coeus 4.5.1: Protocol Routing Issue - Record Close Error  - End
                             if(proceedWithSubmissionActions){
                                //prps start new code - May 1st 2003
                                // once the successful submit is done close the
                                // protocol screen
                                //If maps are present the info dialog will be shown when the last approver approves
                                 if(!submissionForm.isMapsFound() ){
                                    CoeusOptionPane.showInfoDialog("Protocol " + protocolId + " Submitted for Review") ;
                                 }
                                 //Added for case 4350 - Protocol - Creating renewals action is not sending mails - Start
                                 //Commented COEUSDEV-317: Notification not working correctly in IRB Module - Start
                                 //Mail is send from ProcessAction class
                                //Coeus4.3 email notification implementation - start
//                                ProtocolMailController mailController = new ProtocolMailController();
//                                synchronized(mailController) {
//                                    mailController.sendMail(String.valueOf(ACTION_SUBMIT_FOR_REVIEW), protocolInfo.getProtocolNumber(), protocolInfo.getSequenceNumber()+"");
//                                    //101 - Protocol Submission
//                                    //Added for case 4350 - Protocol - Creating renewals action is not sending mails - End
//                                }
                                //Coeus4.3 email notification implementation - end
                                //COEUSDEV-317 : End
                                 //For the IRb Enhancement -- Assigning to agenda as the follow up action to submitted to IRB start step:1
                                ProtocolActionsBean actionBean = new ProtocolActionsBean();
                                if((protocolId != null) && (protocolId.trim().length() > 0)) {
                                     actionBean.setProtocolNumber( protocolId );
                                     actionBean.setSequenceNumber( protocolInfo.getSequenceNumber() );
                                     actionBean.setActionTypeDescription( ((CoeusMenuItem)actSource).getText() );
                                     actionBean.setActionTypeCode(101);
                                     actionBean.setScheduleId(submissionForm.txtScheduleId.getText());
                                     actionBean.setCommitteeId(submissionForm.txtCommitteeID.getText());
                                     processAction(actionBean) ;
                                }//End IRB Ebhancement Step:1
                            }
                            //Modified for case 2785 - Routing enhancements - end
                                //Coeus Enhancement case #1797 start
                            if(functionType == DISPLAY_MODE)
                                       releaseUpdateLock();
                            //Coeus Enhancement case #1797 end
                            mdiForm.removeFrame(referenceTitle,referenceId);
                            this.doDefaultCloseAction();
                        //prps end new code
                        //modified for internal issue#126 : For Display mode IRB and IACUC Protocol lock
                        }else{
                            if(TypeConstants.DISPLAY_MODE == functionType){
                               releaseUpdateLock();
                            }
                        }
                        //                          commented by ravi - END
                        //                            }else{
                        //                                showSubmissionDisplayForm(protocolId);
                        //                            }
                        //                          commented by ravi - END
                    }
                    
                   //}
                   
                    
              }
            
            // else{
              //      showSubmissionDisplayForm(protocolId);
                    
               // }
            }
            //Added for case 2785 - Routing enhancement - start
            else if(actSource.equals(mnuItmApprove)){
                 RoutingForm routingForm = new RoutingForm(protocolInfo, 7, protocolId,  
                              protocolInfo.getSequenceNumber(), protocolInv.getLeadUnitNumber(), false);
                 //COEUSQA-1433 - Allow Recall from Routing - Start
                 // Modified for COEUSQA-3816 : Lite - Proposal routing - Locking issues - Start
                 // Routing lock is done in RoutingApprovalForm
//                 if(routingForm.isEnabled() && functionType == DISPLAY_MODE){
//                     boolean isProtocolLocked = lockProtocolRecall();
//                     if(isProtocolLocked){
//                         routingForm.display();
//                     }
//                 }                  
                 routingForm.display();
                 // Modified for COEUSQA-3816 : Lite - Proposal routing - Locking issues - End
                 //COEUSQA-1433 - Allow Recall from Routing - End
                 // Added for COEUSQA-2556 : When a protocol is in routing it should not have a status of Submitted to IRB - Start
                 // To update the protocol status in the protocol base window
                 
                 ProtocolInfoBean protocolInformation = getProtocolinfo(protocolId);
                 protocolInfo.setProtocolStatusCode(protocolInformation.getProtocolStatusCode());
                 protocolInfo.setProtocolStatusDesc(protocolInformation.getProtocolStatusDesc());
                 observable.setFunctionType(functionType);
                 observable.notifyObservers(protocolInfo);
                 // Added for COEUSQA-2556 : When a protocol is in routing it should not have a status of Submitted to IRB - End
                 ProtocolSubmissionInfoBean protocolSubmissionInfoBean = getSubmissionInfoForSequence();
                 if(routingForm.getSubmissionStatusCode() == SUBMIT_TO_COMMITTEE){
                     //Commented COEUSDEV-317: Notification not working correctly in IRB Module - Start
                     //Mail is send from ProcessAction class
                     //Added for case 4350 - Protocol - Creating renewals action is not sending mails - Start
//                     ProtocolMailController mailController = new ProtocolMailController();
//                     synchronized(mailController) {
//                        mailController.sendMail(String.valueOf(ACTION_SUBMIT_FOR_REVIEW), protocolInfo.getProtocolNumber(), String.valueOf(protocolInfo.getSequenceNumber()));
//                        //101 - Protocol Submission
//                        //Added for case 4350 - Protocol - Creating renewals action is not sending mails - End
//                    }
                     //COEUSDEV-317 : End
                     //For the IRb Enhancement -- Assigning to agenda as the follow up action to submitted to IRB start step:1
                     ProtocolActionsBean actionBean = new ProtocolActionsBean();
                     if((protocolId != null) && (protocolId.trim().length() > 0)) {
                         actionBean.setProtocolNumber( protocolId );
                         actionBean.setSequenceNumber( protocolInfo.getSequenceNumber() );
                         actionBean.setActionTypeDescription( ((CoeusMenuItem)actSource).getText() );
                         actionBean.setActionTypeCode(101);
                         if(protocolSubmissionInfoBean!=null){
                             actionBean.setScheduleId(protocolSubmissionInfoBean.getScheduleId());
                             actionBean.setCommitteeId(protocolSubmissionInfoBean.getCommitteeId());
                         }
                         processAction(actionBean) ;
                     }//End IRB Ebhancement Step:1
                     if(functionType == DISPLAY_MODE && lockExist){
                         releaseUpdateLock();
                     }
                     if( requestedModule == CoeusGuiConstants.PROTOCOL_DETAIL_CODE ) {
                         observable.setFunctionType(functionType);
                         observable.notifyObservers(protocolInfo);
                     }
                     //Coeus Enhancement case #1797 end
                     mdiForm.removeFrame(referenceTitle,referenceId);
                     this.doDefaultCloseAction();
                 }
                 //COEUSQA-1433 - Allow Recall from Routing - Start
                 //else if(routingForm.getSubmissionStatusCode() == REJECTED_SUBMISSION){
                 else if(routingForm.getSubmissionStatusCode() == REJECTED_SUBMISSION || routingForm.getSubmissionStatusCode() == RECALLED_SUBMISSION){
                 //COEUSQA-1433 - Allow Recall from Routing - End
                     // Commented for COEUSQA-3748 : Protocols status is not updating correctly, when a protocols is rejected in routing - Start
//                     protocolInfo.setProtocolStatusCode(100);
//                     protocolInfo.setProtocolStatusDesc("Pending/In Progress");
                     // Commented for COEUSQA-3748 : Protocols status is not updating correctly, when a protocols is rejected in routing - End
                     if(functionType == DISPLAY_MODE && lockExist){
                         releaseUpdateLock();
                     }
                     if( requestedModule == CoeusGuiConstants.PROTOCOL_DETAIL_CODE ) {
                         observable.setFunctionType(functionType);
                         observable.notifyObservers(protocolInfo);
                     }
                     //Coeus Enhancement case #1797 end
                     mdiForm.removeFrame(referenceTitle,referenceId);
                     this.doDefaultCloseAction();
                 }
                //Coeus Enhancement case #1797 start
            
        }else if(actSource.equals(mnuItmShowRouting)){
                 ProtocolInfoBean protocolDetails = getProtocolinfo(protocolId);
                 if(submitEnabledProtocolStatusCodes.contains(new Integer(protocolDetails.getProtocolStatusCode()))){
                     //Modified for COEUSQA-2249  : Routing history is lost after an amendment is routed and approved - Start
                     //save confirmation message is thrown when user selects show routing menu
                     //when New Amendment/Renewalor copied protocol before saving
                     if(functionType == 'E'){
                         CoeusOptionPane.showWarningDialog(coeusMessageResources.parseMessageKey(SAVE_CONFIRMATION_BEFOR_OPEN_ROUTING));
                     }else{
                         boolean mapsFound = doSubmitApproveAction();
                         if(mapsFound){
                             RoutingForm routingForm = new RoutingForm(protocolInfo, 7, protocolId,
                                     protocolInfo.getSequenceNumber(), protocolInv.getLeadUnitNumber(), false);
                             routingForm.setShowRouting(true);
                             routingForm.display();
                             //Code modified for Case#2785 - Protocol Routing
//            }else{
                         }else if(!errMsgDisplayed){
                             CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(NO_PROTOCOL_ROUTING_MAPS));
                         }
                         //Code added for Case#2785 - Protocol Routing
                         errMsgDisplayed = false;
                     }
                 }else{
                     MessageFormat formatter = new MessageFormat("");
                     String message = formatter.format(coeusMessageResources.parseMessageKey("showRouting_exceptionCode.1145"),"'",protocolDetails.getProtocolStatusDesc(),"'");
                     CoeusOptionPane.showWarningDialog(message);
                 }
        }
        //Added for case 2785 - Routing enhancement - end
            else if ( actSource.equals( protocolRoles ) ||
            actSource.equals( btnProtocolRoles ) ){
//                int seqNo = 0;
                dlgRoles = new CoeusDlgWindow(mdiForm,
                "Protocol Roles", true);
                newLeadUnit = protocolInv.getLeadUnitNumber();
                if( newLeadUnit.length() > 0 ) {
                    if ( !oldLeadUnit.equals( newLeadUnit )
                    || ( functionType == DISPLAY_MODE ) ){
                        oldLeadUnit = newLeadUnit;
                        RequesterBean requester = new RequesterBean();
                        requester.setDataObject("GET_USER_DETAILS");
                        requester.setId(newLeadUnit);
                        String connectTo = CoeusGuiConstants.CONNECTION_URL
                        + CoeusGuiConstants.FUNCTION_SERVLET;
                        AppletServletCommunicator comm
                        = new AppletServletCommunicator(connectTo,requester);
                        comm.send();
                        ResponderBean response = comm.getResponse();
                        if ((response != null )
                        && (!response.isSuccessfulResponse())){
                            CoeusOptionPane.showErrorDialog(
                            response.getMessage());
                            return;
                        }else{
                            usersForUnit = ( Vector ) response.getDataObject();
                        }
                        
                        treeData = getTreeDataForUnit( newLeadUnit );
                    }
                }else{
                    usersForUnit = new Vector();
                    treeData = null;
                }
                
                if ( functionType == DISPLAY_MODE ) {
                    try{
                        serverExistingDetails = getRolesInDisplayMode(protocolId,getSequenceId());
                    }catch (CoeusClientException coeusClientException){
                        CoeusOptionPane.showDialog(coeusClientException);
                    }
                    openInDisplayMode(serverExistingDetails);
                    userRoles.removeAllElements();
                    userRoles.addElement( usersForUnit );
                    userRoles.addElement(serverExistingDetails);
                }else {
                    userRoles.removeAllElements();
                    if ( usersForUnit != null &&  usersForUnit.size() > 0 ) {
                        // table data for unit number
                        userRoles.addElement( usersForUnit );
                    }else{
                        
                        // will execute only in add mode without lead unit number
                        if( userAndRoleDetails != null && userAndRoleDetails.size() > 1 ) {
                            userRoles.addElement( ( Vector )userAndRoleDetails.elementAt( 1 ) );
                        }else{
                            // empty users data to specify that there are no users for the
                            // given unit number
                            userRoles.addElement( new Vector() );
                        }
                    }
                    
                    if ( functionType == ADD_MODE || functionType == COPY_MODE ) {
                        if( treeData != null ) {
                            // tree data for unit number
                            userRoles.addElement( treeData );
                        }else {
                            // tree data with loggedin user information
                            userRoles.addElement( ( Vector )userAndRoleDetails.elementAt( 0 )  );
                        }
                    }else {
                        // Modify mode
                        if( tempUserRoles != null ) {
                            // unsaved tree data
                            userRoles.addElement( tempUserRoles );
                        }else {
                            // data which came from database
                            userRoles.addElement(  exisitingUserRoles );
                        }
                        
                        
                    }
                }
                String protNo = (functionType == COPY_MODE ) ? "" : protocolId ;
                
                // Modified for COEUSQA-2692 : Allow an investigator to abandon an IRB protocol that has never been approved - Start
//                if(authorizedToPerform('y')){
//                    if( functionType == DISPLAY_MODE ) {
//                        screenMode = MODIFY_MODE;
//                    }
//                }
                if(protocolInfo.getProtocolStatusCode() == ABANDONED_STATUS_CODE){
                    screenMode = DISPLAY_MODE;
                }else if(authorizedToPerform('y')){
                    if( functionType == DISPLAY_MODE ) {
                        screenMode = MODIFY_MODE;
                    }
                }
                // Modified for COEUSQA-2692 : Allow an investigator to abandon an IRB protocol that has never been approved - End
                if( functionType == DISPLAY_MODE  && authorizedToEditRoles == 1 ) {
                    if( recordLockedStatus == -1 ){
                        int option = CoeusOptionPane.DEFAULT_NO;
                        option = CoeusOptionPane.showQuestionDialog("Are you going to edit roles?",
                        CoeusOptionPane.OPTION_YES_NO,option);
                        if( option == CoeusOptionPane.SELECTION_YES ) {
                            if( canLockProtocol() ){
                                isLock=true;
                                screenMode = MODIFY_MODE;
                                //recordLockedStatus = 1;
                            }else{
                                isLock=false;
                                screenMode = DISPLAY_MODE;
                            }
                        }else if( option == CoeusOptionPane.SELECTION_NO ){
                            screenMode = DISPLAY_MODE;
                            
                        }
                    }else if ( recordLockedStatus == CoeusGuiConstants.LOCK_SUCCESSFUL ) {
                        screenMode = MODIFY_MODE;
                        //                            isLock=false;
                    }else if( recordLockedStatus == CoeusGuiConstants.LOCK_UNSUCCESSFUL ) {
                        CoeusOptionPane.showInfoDialog(
                        coeusMessageResources.parseMessageKey("protocolDetForm_exceptionCode.1019"));
                        screenMode = DISPLAY_MODE;
                        isLock=false;
                    }
                }
                // COEUSDEV-273: Protocol roles update error - new se & save  - Start
                // Application throwing 'lock deleted by admin' message, beacuse isLock is not set to false if the 
                // user cannot edit the roles
                if(authorizedToEditRoles == 0){
                    isLock = false;
                }
                // COEUSDEV-273: Protocol roles update error - new se & save  - End
                rolesForm = new UserRolesMaintenance(protNo,
                userRoles, screenMode);
                //commented setting AdminRoleId because authorized user should be able
                //to add/remove to Protocol Aggregator role.
                //rolesForm.setAdminRoleId( CoeusGuiConstants.PROTOCOL_COORDINATOR_ID );
                // Modified for COEUSQA-2824_IRB and IACUC protocol access permissions should not include approver_start
                rolesForm.setAdminRoleId( CoeusGuiConstants.IRB_PROTOCOL_APPROVER_ID );
                // Modified for COEUSQA-2824_IRB and IACUC protocol access permissions should not include approver_end
                rolesForm.setMandatoryRoleId( CoeusGuiConstants.PROTOCOL_COORDINATOR_ID );
                rolesForm.setShowSponsor( false );
                rolesForm.setModuleLabelName( "Protocol Number: " );
                rolesForm.registerLockObservable(this);
                dlgRoles.getContentPane().add( rolesForm.showUserRolesMaintenance( dlgRoles ) );
                dlgRoles.pack();
                /* displaying the window in center of the screen */
                Dimension screenSize = Toolkit.getDefaultToolkit().
                getScreenSize();
                //dlgGenerate.setSize(new Dimension(500,200));
                Dimension dlgSize = dlgRoles.getSize();
                dlgRoles.setLocation(screenSize.width/2 - (dlgSize.width/2),
                screenSize.height/2 - (dlgSize.height/2));
                dlgRoles.setResizable(false);
                dlgRoles.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
                //                    dlgRoles.getRootPane().getInputMap( JComponent.WHEN_IN_FOCUSED_WINDOW ).put(
                //                        KeyStroke.getKeyStroke( KeyEvent.VK_ESCAPE, 0 ),   "escPressed" );
                //                    dlgRoles.getRootPane().getActionMap().put("escPressed",
                //                        new AbstractAction( "escPressed" )   {
                //                            public void actionPerformed( ActionEvent actionEvent ){
                //                                // Code to be executed when 'Esc' is pressed should go here.
                //                                validateData();
                //                            }
                //                    } );
                dlgRoles.addEscapeKeyListener(escapeKeyListener);
                dlgRoles.addWindowListener(new WindowAdapter(){
                    public void windowClosing(WindowEvent we){
                        validateRolesData();
                        rolesForm.closeAndFire();
                    }
                });
                rolesForm.getRolesTree().requestFocusInWindow();
                //dlgRoles.setVisible(true);
                dlgRoles.show();
                boolean saveCon = rolesForm.isSaveRequired();
                if ( saveCon ) { 
                    tempUserRoles = rolesForm.getUserRolesData();
                    // COEUSDEV-273: Protocol roles update error - new sequence�
                     isRoleDataChanged = true;
                            /*if( userAndRoleDetails != null && userAndRoleDetails.size() > 0 ){
                                 userAndRoleDetails.setElementAt( ( Vector )tempUserRoles.clone() , 0 );
                            }*/
                }else{
                    if( tempUserRoles == null ){
                        tempUserRoles = ( Vector ) userRoles.elementAt( 1 );
                    }else{
                        rolesForm.setSaveRequired(true);
                    }
                }
                // check for he lock and then save the data.
                if(functionType==DISPLAY_MODE){
                    performRolesSaving(isLock,saveCon);
                    rolesForm.setSaveRequired(false);
                    isLock=false;
                }
                //  releaseUpdateLock();
                //                }
                //                }
            }//prps start
            else if (actSource.equals(submissionDetails)) {
                SubmissionDetailsBean detailsBean = new SubmissionDetailsBean() ;
                detailsBean.setProtocolNumber(protocolId) ;
                detailsBean.setScheduleId(null) ; //scheduleId = null from here
                detailsBean.setSequenceNumber(new Integer(sequenceId)) ; // seq num is also null
                //detailsBean = getSubmissionDetails(detailsBean) ; //Commented By sharath to change DataType for Submission Details Form
                //Vector vecDetails = detailsBean.getSubmissionDetails() ; //Commented By sharath to change DataType for Submission Details Form
                //Vector vecReviewers = detailsBean.getSelectedReviewers() ; //Commented By sharath to change DataType for Submission Details Form
                Vector vecDetails = getSubmissionDetails(detailsBean);
                Vector vecReviewers = null;
                //Bug Fix # 1840
                if (vecDetails.size() <= 0 || ((Vector)vecDetails.get(0)).size() == 0 ) {
                    //display appropriate msg
                    
                    CoeusOptionPane.showInfoDialog("Submission Details not available for this protocol") ;
                }
                else {
                    /* Case 646  - prahalad Mar 12 2004
                    added functiontype as a paramter instead of 'E'
                     */
                    
                    //Protocol Enhancement - Submission Details window editable Start
                    /*SubmissionDetailsForm frmSubmissionDetailsForm =
                    new SubmissionDetailsForm(mdiForm,
                    "Submission details for Protocol " + protocolId,protocolId,
                    true, vecDetails, vecReviewers, functionType, -1 ) ;*/
                    SubmissionDetailsForm frmSubmissionDetailsForm =
                    new SubmissionDetailsForm(mdiForm,
                    "Submission details for Protocol " + protocolId,protocolId,
                    true, vecDetails, vecReviewers, 'M', -1 ) ;
                    //Protocol Enhancement - Submission Details window editable End
                    //Added for case #1264 Start 2
//                    Vector entireActionsData = getActionsData(detailsBean);
//                    frmSubmissionDetailsForm.setVecEntireActionData(entireActionsData);
                    //Added for case #1264 End 2
                    frmSubmissionDetailsForm.setResizable(false);
                    frmSubmissionDetailsForm.showForm() ;
                    // COEUSQA-2105: No notification for some IRB actions - Start
//                    // 3283: Reviewer Notification Changes:Start
//                    if(frmSubmissionDetailsForm.isReviewerPersonsChanged()){
//                        ProtocolMailController mailController = new ProtocolMailController(true);
//                        synchronized(mailController) {
//                            mailController.sendMail(ACTION_REVIEWER_CHANGE, protocolId,sequenceId);
//                            //351 - Change in Reviewer
//                        }
//                    }
//                    // 3283 - End
                    // COEUSQA-2105: No notification for some IRB actions - End
                }
                
            }
            //Added for the case# COEUSQA-1403 Implement validation based on rules in protocols-start
             else if (actSource.equals(protoValidationChecks)) {
                 if(( checkMandatoryData(protocolInfo) || isSaveRequired() || !customElementsForm.validateData())){
                        setProtocolInfo();
                    }
                 Vector vecBroknRules = getBrokenRulesData();
                        if(vecBroknRules != null && vecBroknRules.size()>0){
                            RoutingValidationChecksForm valChecks =
                                new RoutingValidationChecksForm(mdiForm,vecBroknRules,7, protocolId );
                            valChecks.display();                            
                            
                        }else {
                            CoeusOptionPane.showInfoDialog(
                                    coeusMessageResources.parseMessageKey("validationChecks_exceptionCode.1904"));
                        }          
                 
             }
            //Added for the case# COEUSQA-1403 Implement validation based on rules in protocols-end
            else if( actSource.equals( references ) ) {
                showProtocolReferences();
            }else if( actSource.equals( protocolSummary ) ||
            actSource.equals( btnSummary ) ) {
                showSummaryDetails();
            }
            //Commented for Case 3552 - IRB Attachments - start
            //Added for Protocol Upload Documents start 5
//            else if(actSource.equals(mnuItmUploadDoc)){
//                showUploadDocumentWindow();
//            }
            //Added for Protocol Upload Documents end 5
            //Commented for Case 3552 - IRB Attachments - end
             //Added for Protocol Questionnaire start 4
            else if(actSource.equals(mnuItmQuestionnaire)){
                showQuestionnaireWindow();
            }
            //Added for Protocol Questionnaire end 4
           
            //modified for Coeus Enhancement case:#1787 step 13 start
           /* else if(actSource.equals( relatedProjects ) ){     // added by Manoj 12/08/03
                if(authorizedToPerform('z')){
                    showProtocolRelatedProjects();
                }
            }//prps start dec 22 2003
            */
            //modified for Coeus Enhancement case:#1787 step 13 ends
            
            else if (actSource.equals( printAdhoc )) {
                showAdhocReports() ;
            }
            //prps end dec 22 2003
            
            // Added by Jobin for the Undo Last Action - start
            else if (actSource.equals(undoLastAction)) {
                performUndoLastAction();
            }
            //Added for Coeus 4.3 enhancement PT ID:2210 - View Protocol History start
            else if(actSource.equals(protocolHistory)){
                showProtocolHistory();
            }
            //Added for Coeus 4.3 enhancement PT ID:2210 - View Protocol History end
            //Protocol Enhancement -  Administrative Correction Start 6 
            //Bug fix start
//            else if(adminCorrComments != null)
//            {
//             //Bug fix end
//                  if(actSource.equals(adminCorrComments.btnOK)){
//                   performOkAction();
//                }
//                else if(actSource.equals(adminCorrComments.btnCancel)){
//                    closed = true;
//                    mdiForm.removeFrame(referenceTitle,referenceId);
//                    this.doDefaultCloseAction();
//                    dlgAdminCorrComments.dispose();
//                }
//            }
            //Added for Coeus4.3 Enhancement case:#2214 Email notitication
            else if(actSource.equals(sendMail) || actSource.equals(sendEmail)) {
                 //Added for case 4350 - Protocol - Creating renewals action is not sending mails - Start
                    ProtocolMailController mailController = new ProtocolMailController();
                    synchronized(mailController) {
                        //Modified for case:4329:Clicking mail button for new Amendment/renewal throwing error.
//                        mailController.sendMail("0", "7", protocolInfo.getProtocolNumber(), protocolInfo.getSequenceNumber()+"");
                        mailController.sendMail(0,  protocolInfo.getProtocolNumber(), sequenceId);
                        //Added for case 4350 - Protocol - Creating renewals action is not sending mails - End
                    }
//                MailActionInfoBean mailInfoBean = new MailActionInfoBean();
//                mailInfoBean.setModuleCode("7");
//                mailInfoBean.setModuleItemKey(protocolInfo.getProtocolNumber());
//                mailInfoBean.setModuleSequence(new Integer(protocolInfo.getSequenceNumber()).toString());
//                MailController mailController = new MailController(new HashMap(), mailInfoBean, new CoeusVector());
//                synchronized(mailController) {
//                    mailController.display();
//                }
            }
            //Protocol Enhancement -  Administrative Correction End 6
            
            //Case#3091 - IRB - generate a protocol summary pdf  - Start
            else if (actSource.equals(btnPrintSummary) || actSource.equals(mnuItmPrintSummary)){
                try {
                    if(isSaveRequired()){
                        char tempFunctionType = functionType;
                        setProtocolInfo();
                        //Notification mail is send when a new protocol is saved or Amendment or renewal during print button or print menuitem is selected
                        if(tempFunctionType == ADD_MODE) {
                            ProtocolMailController mailController = new ProtocolMailController();
                            synchronized(mailController) {
                                mailController.sendMail(ACTION_NEW_PROTOCOL, protocolInfo.getProtocolNumber(),protocolInfo.getSequenceNumber());
                                //100 - Protocol Creation
                            }
                        } else if(tempFunctionType == COPY_MODE) {
                            if(requestedModule == CoeusGuiConstants.PROTOCOL_AMENDMENT_CODE) {
                                ProtocolMailController mailController = new ProtocolMailController();
                                synchronized(mailController) {
                                    mailController.sendMail(ACTION_NEW_AMENDMENT, protocolInfo.getProtocolNumber(),protocolInfo.getSequenceNumber());
                                    //103 - Amendment Creation
                                }
                            }else if(requestedModule == CoeusGuiConstants.PROTOCOL_RENEWAL_CODE) {
                                ProtocolMailController mailController = new ProtocolMailController();
                                synchronized(mailController) {
                                    mailController.sendMail(ACTION_NEW_RENEWAL, protocolInfo.getProtocolNumber(), protocolInfo.getSequenceNumber());
                                    //102 - Renewal Creation
                                }
                            }else if(requestedModule == CoeusGuiConstants.PROTOCOL_AMENDMENT_WITH_RENEWAL_CODE) {
                                ProtocolMailController mailController = new ProtocolMailController();
                                synchronized(mailController) {
                                    mailController.sendMail(ACTION_NEW_RENEW_WITH_AMEND, protocolInfo.getProtocolNumber(),protocolInfo.getSequenceNumber());
                                    //117 - Renewal with Amendment Creation
                                }
                            }
                        }
                    }
                    showProtocolPrintSummary();
                } catch(CoeusUIException coeusUIException) {
                    if(!coeusUIException.getMessage().equals("null.")) {
                        CoeusOptionPane.showDialog(coeusUIException);
                        return ;
                    }
                }
                
            }else if(actSource.equals(mnuItmChangePassword)){
                showChangePassword();
            }else if(actSource.equals(mnuItmPreferences)){
                showPreference();
            }else if(actSource.equals(mnuItmExit)){
                exitApplication();
            }else if(actSource.equals(mnuItmCurrentLock)){
                showLocksForm();
            }else if(actSource.equals(mnuItmDelegations)){
                showUserDelegation();
            }else if(actSource.equals(mnuItmInbox)){
                showInboxDetails();
            }
            //Added for Coeus 4.3 enhancement PT ID:2210 - View Protocol History end
            //Protocol Enhancement -  Administrative Correction Start 6
            //Bug fix start
            else if(adminCorrComments != null) {
                //Bug fix end
                if(actSource.equals(adminCorrComments.btnOK)){
                    performOkAction();
                } else if(actSource.equals(adminCorrComments.btnCancel)){
                    closed = true;
                    mdiForm.removeFrame(referenceTitle,referenceId);
                    this.doDefaultCloseAction();
                    dlgAdminCorrComments.dispose();
                }
            }
             //COEUSQA:2653 - Add Protocols to Medusa - Start
            else if(actSource.equals(btnMedusa)){
                 showMedusaWindow(protocolInfo.getProtocolNumber());                 
            }
            //COEUSQA:2653 - End
//adding new menu item Disclosure Status start
            else if(actSource.equals(disclosurestatus)){
                showdisclosurestatus();
            }
              //adding new menu item Disclosure Status end
//code added to send coi notification to protocol persons..start
               else if (actSource.equals(notification)) {
                    showPersonNotification();
            }
//code added to send coi notification to protocol persons..end
            //Case#3091 - End
            else{
                // prps end
                
                //Added by Vyjayanthi
                //Start
                
                ProtocolActionsBean actionBean = new ProtocolActionsBean();
//                ProtocolActionChangesBean actionChangesBean = null;
                // Modified for COEUSQA-2692_Allow an investigator to abandon an IRB protocol that has never been approved_start
                if ( actSource.equals( closeEnrollment ) ||
                actSource.equals( protocolTerminated ) ||
                actSource.equals( protocolSuspend ) ||
                actSource.equals( protocolSuspendByDSMB ) ||
                actSource.equals( protocolExpire ) ||
                actSource.equals( protocolClosed ) ||
                actSource.equals( protocolAbandon ) ||                        
                actSource.equals( protocolWithdraw ) ||
                actSource.equals( requestCloseEnrollment ) ||
                actSource.equals( protocolRequestToTerminate ) ||
                actSource.equals( protocolRequestToClose ) ||
                actSource.equals( protocolRequestSuspension ) ||
                //Added for performing request and non request actions - start - 6
                actSource.equals( protocolRequestToReopen ) ||        
                actSource.equals( protocolRequestToDA ) ||        
                actSource.equals( protocolNotify ) ||        
                actSource.equals( protocolReopen ) ||        
                actSource.equals( protocolDA )){
                //modified for COEUSQA-2692_Allow an investigator to abandon an IRB protocol that has never been approved_end
                //Added for performing request and non request actions - end - 6
                    if(functionType != DISPLAY_MODE && isSaveRequired()){
                        setProtocolInfo();
                    }
                    if((protocolId != null) && (protocolId.trim().length() > 0)) {
                        actionBean.setProtocolNumber( protocolId );
                        actionBean.setSequenceNumber( this.getSequenceId() );
                        actionBean.setActionTypeDescription( ((CoeusMenuItem)actSource).getText() );
                        actionBean.setActionTypeCode( Integer.parseInt(ae.getActionCommand()) );
                        processAction(actionBean) ;
                        //For the Enhancement ,removing the frame should be done after the entire action is performed step:5
                        mdiForm.removeFrame(referenceTitle,referenceId);
                        this.doDefaultCloseAction();
                        //End enhancement step:5
                   }
                    
                    //                    if(functionType != DISPLAY_MODE){
                    //                        if(isSaveRequired()){
                    //                            setProtocolInfo();
                    //                        }
                    //
                    //                        if((protocolId != null) && (protocolId.trim().length() > 0)) {
                    //                            actionBean.setProtocolNumber( protocolId );
                    //                            actionBean.setSequenceNumber( this.getSequenceId() );
                    //                            actionBean.setActionTypeDescription( ((CoeusMenuItem)actSource).getText() );
                    //                            actionBean.setActionTypeCode( Integer.parseInt(ae.getActionCommand()) );
                    //                            processAction(actionBean) ;
                    //                        }
                    //                    }else // if the protocol is saved then go ahead with the action
                    //                    {
                    //                        actionBean.setProtocolNumber( protocolId );
                    //                        actionBean.setSequenceNumber( this.getSequenceId() );
                    //                        actionBean.setActionTypeDescription( ((CoeusMenuItem)actSource).getText() );
                    //                        actionBean.setActionTypeCode( Integer.parseInt(ae.getActionCommand()) );
                    //                        //prps start dec 15 2003
                    //                        processAction(actionBean) ;
                    //                        //prps end dec 15 2003
                    //                    }
                    //                    //commenting end
                    
                    
                }
            }
        }catch (CoeusUIException coeusUIException) {
            CoeusOptionPane.showDialog(coeusUIException);
            tbdPnProtocol.setSelectedIndex(coeusUIException.getTabIndex());
            
            //Case :#3149 Tabbing between fields does not work on Others tabs - Start            
            if(customElementsForm != null && !customElementsForm.getOtherColumnElementData().isEmpty() &&
                    customElementsForm.getOtherTabMandatory() && getFunctionType() != DISPLAY_MODE &&
                     customElementsForm.getTable().isEnabled()){
                boolean[] lookUpAvailable = customElementsForm.getLookUpAvailable();
                customTable = customElementsForm.getTable();
                row = customElementsForm.getmandatoryRow();
                column = customElementsForm.getmandatoryColumn();
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
            }
            //Case :#3149 - End
            //Coeus Enhancement case #1797 start
             if(functionType == DISPLAY_MODE && lockExist)
                     releaseUpdateLock();
            //Coeus Enhancement case #1797 end
        }catch(CoeusException ex){
            // added by manoj to display action messages as information messages
            //Coeus Enhancement case #1797 start
             if(functionType == DISPLAY_MODE && lockExist)
                     releaseUpdateLock();
            //Coeus Enhancement case #1797 end
            CoeusOptionPane.showDialog(new CoeusClientException(ex));
        }catch(Exception e){
            //Coeus Enhancement case #1797 start
             if(functionType == DISPLAY_MODE && lockExist)
                     releaseUpdateLock();
            //Coeus Enhancement case #1797 end
            e.printStackTrace();
          /*  if(!( e.getMessage().equals(coeusMessageResources.parseMessageKey(
            "protoDetFrm_exceptionCode.1130")) )){
                CoeusOptionPane.showWarningDialog(e.getMessage());
            }else{
                //setSelected(true);
                setVisible(true);
            }*/
        }
          //Case :#3149 Tabbing between fields does not work on Others tabs - Start          
          if(tbdPnProtocol.getSelectedIndex() == OTHERS_TAB &&  customElementsForm != null && !customElementsForm.getOtherTabMandatory() && 
                getFunctionType() != DISPLAY_MODE &&
                customElementsForm.getTable().isEnabled()) {
                boolean[] lookUpAvailable = customElementsForm.getLookUpAvailable();
                customTable = customElementsForm.getTable();
                row = customElementsForm.getRow();
                column = customElementsForm.getColumn();
                if(lookUpAvailable[row]){
                    customTable.editCellAt(row,column);
                    customTable.setRowSelectionInterval(row,row);
                    customTable.setColumnSelectionInterval(column,column);
                    
                }else{
                    customTable.editCellAt(row,column);
                    customTable.setRowSelectionInterval(row,row);
                    customTable.setColumnSelectionInterval(column,column);
                }
            }
        //Case :#3149 - End
}
    //Added for case #1264 start 1
//    private Vector getActionsData(SubmissionDetailsBean detailsBean) throws CoeusException{
//        RequesterBean request = new RequesterBean();
//        ResponderBean response = null;
//        Vector actionData = null;
//        final String PROTOCOL_ACTION_SERVLET = "/protocolActionServlet";
//        String connectTo =CoeusGuiConstants.CONNECTION_URL
//                                    + PROTOCOL_ACTION_SERVLET ;
//        request.setId(detailsBean.getProtocolNumber());
//        request.setDataObject(detailsBean.getSequenceNumber());
//        request.setFunctionType('a');
//        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
//        comm.send();
//        response = comm.getResponse();
//        if(response!=null){
//            if(response.isSuccessfulResponse()){
//                actionData = (Vector)response.getDataObject();
//            }else{
//                throw new CoeusException(response.getMessage(),0);
//            }
//        }
//        return actionData;
//    }
    //Added for case #1264 end 1
    private Vector getRolesInDisplayMode(String protocolNumber,int sequenceNumber) throws CoeusClientException{
        Vector data = null;
        String connectTo = CoeusGuiConstants.CONNECTION_URL + PROTOCOL_SERVLET;
        RequesterBean request = new RequesterBean();
        request.setFunctionType('l');
        request.setId(protocolNumber);
        request.setDataObject(new Integer(sequenceNumber));
        AppletServletCommunicator comm = new AppletServletCommunicator(
        connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response==null){
            throw new CoeusClientException("Colud not contact server",CoeusClientException.ERROR_MESSAGE);
        }
        if(response.isSuccessfulResponse()){
            data = (Vector)response.getDataObjects();
            return data;
        }else{
            throw new CoeusClientException(response.getMessage(),CoeusClientException.ERROR_MESSAGE);
        }
    }
    
    
    private void openInDisplayMode(Vector serverExistingDetails){
        if( serverExistingDetails != null ) {
            serverCopyOfExistingUserRoles = new Vector();
            UserRolesInfoBean usrBean,tmpBean;
            for( int indx = 0; indx < serverExistingDetails.size(); indx++) {
                tmpBean = ( UserRolesInfoBean )serverExistingDetails.get(indx);
                usrBean = new UserRolesInfoBean();
                usrBean.setHasChildren( tmpBean.hasChildren() );
                usrBean.setIsRole( tmpBean.isRole() );
                usrBean.setRoleBean( tmpBean.getRoleBean() );
                usrBean.setUserBean( tmpBean.getUserBean() );
                usrBean.setUsers( tmpBean.getUsers() );
                usrBean.setUpdateTimestamp(tmpBean.getUpdateTimestamp());
                usrBean.setUpdateUser(tmpBean.getUpdateUser());
                serverCopyOfExistingUserRoles.add( usrBean );
            }
        }
    }
    
    
    private void performRolesSaving(boolean isLock, boolean saveConformation){
        if ( saveConformation) {
            isRoleDataChanged = true;
            if ( functionType == ADD_MODE ) {
                // adding default users of the protocol's lead unit
                // to the OSP$PROTOCOL_USER_ROLES table
                Vector newUserRoles = null;
                if ( rolesForm == null ){
                    newUserRoles = getTreeDataForUnit( protocolInv.getLeadUnitNumber() );
                }else {
                    if( tempUserRoles != null ) {
                        newUserRoles = tempUserRoles;
                    }
                }
                Hashtable htNewUsers = getUserRoleInfo( newUserRoles );
                
                Vector userRolesForDB  = checkModifications(
                new Hashtable(), htNewUsers );
                protocolInfo.setUserRoles( userRolesForDB );
                try{
                    saveData(userRolesForDB);
                }catch (Exception exception){
                    CoeusOptionPane.showErrorDialog(exception.getMessage());
                }
                releaseUpdateLock();
                saveConformation = false;
                rolesForm.setSaveRequired( false );
            }else {             // if ( rolesForm != null && rolesForm.isSaveRequired() ) {
                Hashtable htExistingUsers = new Hashtable();
                Hashtable htTempUsers = new Hashtable();
                if( serverCopyOfExistingUserRoles /*copyOfExistingUserRoles*/ != null ){
                    htExistingUsers = getUserRoleInfo( serverCopyOfExistingUserRoles );//copyOfExistingUserRoles );
                }
                if ( tempUserRoles != null ) {
                    htTempUsers = getUserRoleInfo( tempUserRoles );
                }else if( rolesForm == null || !rolesForm.isSaveRequired() ) {
                    Vector rolesFromServer = protocolInfo.getUserRolesInfoBean();
                    if( rolesFromServer != null ) {
                        htTempUsers = getUserRoleInfo( rolesFromServer );
                    }
                }
                
                Vector userRolesForDB  = checkModifications(htExistingUsers, htTempUsers );
                protocolInfo.setUserRoles( userRolesForDB );
                if ( functionType == MODIFY_MODE ) {
                    protocolInfo.setAcType( "U" );
                }
                try{
                    saveData(userRolesForDB);
                    releaseUpdateLock();
                    saveConformation = false;
                    rolesForm.setSaveRequired( false );
                }catch (Exception exception){
                    CoeusOptionPane.showErrorDialog(exception.getMessage());
                }
                saveConformation = false;
                rolesForm.setSaveRequired( false );
            }
        }else{
            if(isLock){
                releaseUpdateLock();
                isRoleDataChanged = false;
                saveConformation = false;
                rolesForm.setSaveRequired( false );
                isLock = false;
            }else{
                isRoleDataChanged = false;
                saveConformation = false;
                rolesForm.setSaveRequired( false );
                isLock = false;
            }
        }
    }
    
    private void saveData(Vector vecData) throws Exception{
        Vector data = null;
        String connectTo = CoeusGuiConstants.CONNECTION_URL + PROTOCOL_SERVLET;
        RequesterBean request = new RequesterBean();
        request.setFunctionType('t');
        request.setId(protocolInfo.getProtocolNumber());
        request.setDataObject(vecData);
        AppletServletCommunicator comm = new AppletServletCommunicator(
        connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response.isSuccessfulResponse()) {
            saveRequired = false;
            data = (Vector)response.getDataObjects();
        } else {
            CoeusOptionPane.showErrorDialog(response.getMessage());
            if (response.isCloseRequired()) {
                mdiForm.removeFrame(referenceTitle, referenceId);
                isTimedOut = true;
                closed = true;
                this.doDefaultCloseAction();
                return;
            }else {
                throw new Exception(response.getMessage());
            }
        }
        if( protocolInfo != null ) {
            try{
                updData = updateRolesData(data);
            }catch (Exception exception){
                CoeusOptionPane.showErrorDialog(exception.getMessage());
            }
        }else {
            throw new Exception(coeusMessageResources.parseMessageKey(
            "saveFail_exceptionCode.1102"));
        }
    }
    
    private Vector updateRolesData(Vector data) throws Exception {
        if( rolesForm != null ){
            rolesForm.setSaveRequired( false );
        }
//        newExistingData = new Vector();
        tempUserRoles = null;
        userAndRoleDetails = null;
        protocolInfo.setUserRolesInfoBean(data);
        exisitingUserRoles = protocolInfo.getUserRolesInfoBean();
        serverExistingDetails = protocolInfo.getUserRolesInfoBean();
        serverCopyOfExistingUserRoles =(Vector) ObjectCloner.deepCopy(protocolInfo.getUserRolesInfoBean());
        copyOfExistingUserRoles = (Vector) ObjectCloner.deepCopy(protocolInfo.getUserRolesInfoBean());
        recordLockedStatus=-1;
        return data;
    }
    
    
    
    //prps start dec 22 2003
    public void showAdhocReports() {
        try {
            if (protocolId.equals("")) {
                throw new Exception() ;
            }
            AdhocDetailsBean adhocDetailsBean = new AdhocDetailsBean() ;
            adhocDetailsBean.setProtocolNumber(protocolId) ;
            adhocDetailsBean.setSequenceNumber(sequenceId) ;
            //Added for the case COEUSDEV-220-Generate Correspondence
            //adhocDetailsBean.setCommitteeId(committeeId);
            //Added for the case COEUSDEV-220-Generate Correspondence-end
            adhocDetailsBean.setSubmissionNumber(0) ; //set this to zero
            adhocDetailsBean.setModule('P') ;
            AdhocReportsForm adhocReportsForm = new AdhocReportsForm(adhocDetailsBean) ;
            adhocReportsForm.showForm() ;
        }
        catch(Exception ex) {
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("correspType_exceptionCode.1015")) ;
        }
    }
    //prps end dec 22 2003
    
    
    //prps start dec 15 2003
    
    // This method will take care of processing actions having many followup actions
    // or actions having recursive followup actions
    // since updateActionStatus method needs to be called for every action. This
    // method cannot be a part of ProcessAction Object
     private void processAction(ProtocolActionsBean actionBean) throws Exception {
        ProtocolActionChangesBean protocolActionChangesBean ;
        //Added for COEUSDEV-86 : Questionnaire for a Submission - Start
        if(actionBean != null && functionType == MODIFY_MODE){
            actionBean.setProtocolEditable(true);
        }
        //Added for COEUSDEV-86 : Questionnaire for a Submission - End
        protocolActionChangesBean =  processAction.performOtherAction( actionBean ) ;
        
        if( protocolActionChangesBean != null ) {
            //For the IRB Enhancement ,Assigning to agenda as the follow up action to submitted to IRB
            //The Frame should be removed after the entire action is performed step:2
//            mdiForm.removeFrame(referenceTitle,referenceId);
//            this.doDefaultCloseAction();
            //End step:2
            observable.notifyObservers( protocolActionChangesBean );
            
            // update the actionBean
            actionBean.setProtocolNumber(protocolActionChangesBean.getProtocolNumber()) ;
            actionBean.setScheduleId(protocolActionChangesBean.getScheduleId()) ;
            actionBean.setSequenceNumber(protocolActionChangesBean.getSequenceNumber()) ;
            actionBean.setSubmissionNumber(protocolActionChangesBean.getSubmissionNumber()) ;
            
            Vector vecActionDetails = protocolActionChangesBean.getFollowupAction() ;
            if (vecActionDetails.size()>0) {
                Vector vecSubActions = (Vector)vecActionDetails.get(0) ;
                HashMap hashUserPrompt = (HashMap)vecActionDetails.get(1) ;
                HashMap hashUserPromptFlag = (HashMap)vecActionDetails.get(2) ;
                
                if (vecSubActions.size() > 0) // perform sub actions or followup actions.
                {  // for an action there cud be multiple followup actions
                    // or one followup action action might have another sub action (recursively)
                    for (int actionCount = 0; actionCount < vecSubActions.size(); actionCount++) {
                        int actionTypeCode = ((Integer)vecSubActions.get(actionCount)).intValue() ;
                        if (hashUserPromptFlag.get(new Integer(actionTypeCode)).toString().equalsIgnoreCase("Y") ) {
                            //For the IRB Enhancement ,Assigning to agenda as the follow up action to submitted to IRB step:3
                            String promptMessage = hashUserPrompt.get(new Integer(actionTypeCode)).toString() ;
                            int option = CoeusOptionPane.showQuestionDialog(promptMessage,
                            CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_YES);
                            System.out.println("Option :"+option);
                            if (option != CoeusOptionPane.SELECTION_NO ) { // prompt the user with msg obtained from database and if the choice is Yes continue follow up action
                                actionBean.setActionTypeCode(actionTypeCode) ;
                                try{
                                    processAction(actionBean) ;
                                }catch(CoeusException ex){
                                    // Added Try-Catch Block for - Display the Error Message if the User Cannot Assign to Agenda after
                                    // submitting the Protocol to IRB. After that close the Protocol Details Window
                                    CoeusOptionPane.showDialog(new CoeusClientException(ex));
                                }
                            }else{
                               observable.notifyObservers(protocolInfo);
                            }
                            //End IRB ENhancement step:3
                        }
                        else { // if the prompt is flag is set to N then user will not be prompted for the followup action
                            actionBean.setActionTypeCode(actionTypeCode) ;
                            processAction(actionBean) ;
                        }
                    }//end for
                    
                }// end if vec sub actions
            }// end if vecActionDetails
            
            
        }else{
            //For the IRB Enhancement ,Assigning to agenda as the follow up action to submitted to IRB step:4
            //if no acton is performed then just update the protocol info to the list table
            observable.notifyObservers(protocolInfo);
            //End IRB ENhancement step:4
        }
        ////        else
        ////        {
        ////            CoeusOptionPane.showErrorDialog("Action "  + actionBean.getActionTypeDescription() + " failed") ;
        ////        }
    }
    
    
    //prps end dec 15 2003

//code added for displaying DisclosureStatusForm to show the proposal disclosure status- starts
private void showdisclosurestatus() {
      HashMap hmParameter = new HashMap();
      int ModuleCode=7;
        String ProtocolNum= protocolInfo.getProtocolNumber();
        hmParameter.put("PROP_NUMBER",protocolInfo.getProtocolNumber());
       edu.mit.coeus.propdev.gui.DisclosureStatusForm  disclosureStatusForm=new
               edu.mit.coeus.propdev.gui.DisclosureStatusForm(ProtocolNum,ModuleCode);
}
//code added for displaying DisclosureStatusForm to show the proposal disclosure status- ends
    private boolean authorizedToPerform(char validate) throws CoeusException{
        if(functionType == ADD_MODE || functionType == COPY_MODE ){
            return true;
        }
        String connectTo = CoeusGuiConstants.CONNECTION_URL + PROTOCOL_SERVLET;
        RequesterBean request = new RequesterBean();
        request.setFunctionType(validate);
        request.setId(""+protocolId);
        AppletServletCommunicator comm = new AppletServletCommunicator(
        connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response.isSuccessfulResponse()){
            if( validate == 'y' ) {
                authorizedToEditRoles = 1;
            }
            return true;
        }else{
            if( validate == 'y' ) {
                authorizedToEditRoles = 0;
                if( functionType == DISPLAY_MODE ) {
                    return false;
                }
            }
            if(response.getDataObject() != null){
                throw (CoeusException)response.getDataObject();
            }else{
                throw new CoeusException(response.getMessage());
            }
        }
    }
    
    //modified for Coeus Enhancement case:#1787 step 14 start
    /*
     /** this method is used to show the protocol related projects for window
     */
   /* private void showProtocolRelatedProjects()throws Exception{
        String title = "Protocol Related Projects ";
        if(functionType != ADD_MODE){
            title = title+ " - "+ protocolId;
        }
        protocolRelatedProjects = new ProtocolRelatedProjects(mdiForm,title,true,
        vecProtoRelProjTemp,functionType);
        protocolRelatedProjects.showDialog();
        isProtoRelSaveReq = isProtoRelSaveReq | protocolRelatedProjects.isSaveRequired();
        if(protocolRelatedProjects.isSaveRequired()){
            vecProtoRelProjTemp = protocolRelatedProjects.getFormData();
        }
    }
    
    /** this method is used to show the protocol related projects for window
     *@param data this contains the vector of protocol related projects bean instances
     */
   /* private void setRelatedProjectsData(Vector data){
        vecProtoRelProj = null;
        if(data != null && data.size()>0){
            vecProtoRelProj = new Vector();
            for(int index = 0;index<data.size();index++){
                vecProtoRelProj.addElement(data.elementAt(index));
            }
        }
    }// end setRelatedProjectsData
    /** this method  returns the new protocol related projects for this protocol
     *@return new protocol related projects for this protocol as vector of beans
     */
    
   /* private Vector getRelatedProjectsData(){
        Vector newData = vecProtoRelProjTemp;
        if(vecProtoRelProj != null && vecProtoRelProj.size()>0 && (newData == null|| newData.size() <= 0 )){
            int size = vecProtoRelProj.size();
            for(int index = 0;index <size;index++){
                ProtocolRelatedProjectsBean beanData =
                (ProtocolRelatedProjectsBean)vecProtoRelProj.elementAt(index);
                //System.out.println(beanData.toString());
                beanData.setAcType("D");
                vecProtoRelProj.removeElementAt(index);
                vecProtoRelProj.add(index,beanData);
            }
            isAllProjectsDelete = true;
        }else if(vecProtoRelProj == null && newData != null && newData.size() > 0){
            int size = newData.size();
            vecProtoRelProj = new Vector();
            for(int index = 0;index<size;index++){
                ProtocolRelatedProjectsBean beanData =
                (ProtocolRelatedProjectsBean)newData.elementAt(index);
                beanData.setAcType("I");
                vecProtoRelProj.addElement(beanData);
            }
            isAllProjectsDelete = false;
        }else if(newData != null && vecProtoRelProj != null && newData.size()>0 && vecProtoRelProj.size()>0){
            isAllProjectsDelete = false;
            int oldSize = vecProtoRelProj.size();
            int newSize = newData.size();
            for(int newIndex = 0 ; newIndex < newSize; newIndex++){
                ProtocolRelatedProjectsBean newBeanData =
                (ProtocolRelatedProjectsBean)newData.elementAt(newIndex);
                int oldIndex = 0;
                for(;oldIndex < oldSize;oldIndex++){
                    ProtocolRelatedProjectsBean oldBeanData =
                    (ProtocolRelatedProjectsBean)vecProtoRelProj.elementAt(oldIndex);
                    if(oldBeanData.getModuleCode() == newBeanData.getModuleCode()){
                        if(oldBeanData.getProjectNumber().equals(newBeanData.getProjectNumber())){
                            break;
                        }
                    }
                }
                if(oldIndex == oldSize){
                    newBeanData.setAcType("I");
                    vecProtoRelProj.addElement(newBeanData);
                }
            }
            // for Deleted Records
            for(int oldIndex = 0 ; oldIndex < oldSize; oldIndex++){
                ProtocolRelatedProjectsBean oldBeanData =
                (ProtocolRelatedProjectsBean)vecProtoRelProj.elementAt(oldIndex);
                int newIndex = 0;
                for(;newIndex < newSize;newIndex++){
                    ProtocolRelatedProjectsBean newBeanData =
                    (ProtocolRelatedProjectsBean)newData.elementAt(newIndex);
                    if(newBeanData.getModuleCode() == oldBeanData.getModuleCode()){
                        if(newBeanData.getProjectNumber().equals(oldBeanData.getProjectNumber())){
                            break;
                        }
                    }
                }
                if(newIndex == newSize){
                    oldBeanData.setAcType("D");
                }
            }
        }
        return vecProtoRelProj;
    }// ends getrelatedprojects data
    */
    //modified for Coeus Enhancement case:#1787 step 14 ends
    //prps start
    // this method will get the submission details data from the servlet
    //private SubmissionDetailsBean getSubmissionDetails(SubmissionDetailsBean detailsBean) { // Commented By sharath to change DataType for Submission Details Form
    private Vector getSubmissionDetails(SubmissionDetailsBean detailsBean) {
        Vector vecDetails= new Vector() ;
        try {    // send request
            System.out.println("*** Sending ***") ;
            
            RequesterBean requester = new RequesterBean();
            requester.setFunctionType('G') ;
            requester.setDataObject(detailsBean) ;
            
            String connectTo =CoeusGuiConstants.CONNECTION_URL
            + SUBMISSION_DETAILS_SERVLET ;
            AppletServletCommunicator comm
            = new AppletServletCommunicator(connectTo,requester);
            comm.send();
            
            ResponderBean responder = comm.getResponse();
            if (responder.isSuccessfulResponse()) {
                //detailsBean = (SubmissionDetailsBean)responder.getDataObject() ; //Commented By sharath to change DataType for Submission Details Form
                vecDetails = (Vector)responder.getDataObject();
            }
            
        }
        catch(Exception ex) {
            System.out.println("  *** Error in getting submission details ***  ") ;
            ex.printStackTrace() ;
        }
        
        //return detailsBean ; //responder.getDataObject()
        return vecDetails;
    }
    
    //prps end
    
    
    
    
    private void validateRolesData(){
        // COEUSDEV-273: Protocol roles update error - new se & save 
//        if ( functionType != DISPLAY_MODE &&  rolesForm.isDataChanged()  ) {
         if ( rolesForm.isDataChanged()  ) {
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
    /**
     * This method is used to show the submission display form for the given
     * protocol number
     *
     * @param protocolId String representing protocol whose submission details
     * should be displayed.
     */
    private void showSubmissionDisplayForm(String protocolId) throws Exception {
        submissionDisplayForm
        = new ProtocolSubmissionDisplayForm(CoeusGuiConstants.getMDIForm(),
        "Protocol Submission", true, protocolId);
        Dimension screenSize
        = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = submissionDisplayForm.getSize();
        submissionDisplayForm.setLocation(
        screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        submissionDisplayForm.addKeyListener(new KeyAdapter(){
            public void keyReleased(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
                    submissionDisplayForm.dispose();
                }
            }
        });
        // Added by chandra - Bug Fixing. File:IRB-SystemTestingDL -01.xls Bug #18
        submissionDisplayForm.setResizable(false);
        submissionDisplayForm.setVisible(true);
        // End Bug Fixing.
    }
    
    /**
     * This method is used to ask the confirmation and save the submission details
     * before closing the dialog.
     */
    
    /* Case 652
        Cancelling submission - prahalad Mar 12 2004
     */
    private void saveSubmissionDetails(){
        int option = CoeusOptionPane.SELECTION_NO;
        option = CoeusOptionPane.showQuestionDialog(
        "Do you want to cancel the submission?",
        CoeusOptionPane.OPTION_YES_NO,
        CoeusOptionPane.DEFAULT_YES);
        if(option == CoeusOptionPane.SELECTION_YES) {
            submissionForm.dispose();
        }
        else if(option == CoeusOptionPane.SELECTION_NO) {
            submissionForm.setVisible(true);
        }
    }
    
    
    //udated for Row Locking. Subramanya
    private void releaseUpdateLock(){
        try{
            //connect to server and get org detail form
            String rowId = null;
//            ProtocolInfoBean protocolBean = getProtocolInfoBean();
            if ( screenMode == MODIFY_MODE||
               //Case 1787 Start 24
                 screenMode == DISPLAY_MODE ||
                 //Case 1787 End 24
            requestedModule != CoeusGuiConstants.PROTOCOL_DETAIL_CODE ||
            recordLockedStatus == CoeusGuiConstants.LOCK_SUCCESSFUL ) {
                
               
                    
                //rowId = protocolBean.getProtocolNumber();
                /* modified by ravi because protocolbean will be null if
                 * the user selects no option in save confirmation. so instead of
                 * getting protocol number from the bean take from the class
                 * variable which alread exists.
                 */
                
                rowId = protocolId;
                if( functionType != MODIFY_MODE && functionType != DISPLAY_MODE ) {
                    if( requestedModule == CoeusGuiConstants.PROTOCOL_AMENDMENT_CODE ) {
                        rowId+="A";
                    }else if( requestedModule == CoeusGuiConstants.PROTOCOL_RENEWAL_CODE ) {
                        rowId+="R";
                    }
                }
                if(screenMode == DISPLAY_MODE  && (canModifyInDisplay || canFundingSrcModifyInDisplay || canModifySpRevInDisplay) ) {
                    if(lockExist) {
                        //System.out.println("before releasing :"+rowId);
                        RequesterBean requester = new RequesterBean();
                        requester.setDataObject(rowId);
                        requester.setFunctionType('Z');
                        String connectTo =CoeusGuiConstants.CONNECTION_URL+PROTOCOL_SERVLET;
                        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo,requester);
                        comm.send();
                        ResponderBean res = comm.getResponse();
                        if (res != null && !res.isSuccessfulResponse()){
                            CoeusOptionPane.showErrorDialog(res.getMessage());
                            return;
                        }
                       lockExist = false;
                            
                        
                    }
                }
                else {
                    //System.out.println("before releasing :"+rowId);
                    RequesterBean requester = new RequesterBean();
                    requester.setDataObject(rowId);
                    requester.setFunctionType('Z');
                    String connectTo =CoeusGuiConstants.CONNECTION_URL+PROTOCOL_SERVLET;
                    AppletServletCommunicator comm = new AppletServletCommunicator(connectTo,requester);
                    comm.send();
                    ResponderBean res = comm.getResponse();
                    if (res != null && !res.isSuccessfulResponse()){
                        CoeusOptionPane.showErrorDialog(res.getMessage());
                        return;
                    }
                   
                }
            }
        }catch(Exception e){
            CoeusOptionPane.showErrorDialog(e.getMessage());
        }
    }
    
    private void showSummaryDetails() throws Exception{
        ProtocolSummaryForm summaryForm = new ProtocolSummaryForm(protocolId,false);
        summaryForm.showForm();
    }
    
    private void showProtocolReferences() throws Exception{
        
        //protReferenceData = protocolInfo.getReferences();
        if( tempReferences != null ) {
            protReferenceData = tempReferences;
        }else{
            protReferenceData = protocolInfo.getReferences();
        }
        try {
            protReplicateRefData = (Vector)ObjectCloner.deepCopy(protReferenceData);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        //code modified and added for coeus4.3 enhancements
        // To check whether the module is editable.         
        HashMap hmModeDetails = new HashMap();
        if(originalFunctionType == 'P'){
            hmModeDetails.put("functionType", new Character('A'));
        } else {
            hmModeDetails.put("functionType", new Character(functionType));
        }
        hmModeDetails.put("protocolNumber", protocolInfo.getProtocolNumber());
        hmModeDetails.put("hmEditModules", getFormatedModuleData(getVecModuleData()));
        hmModeDetails.put("moduleId", IrbWindowConstants.REFERENCE);
//        protocolReferenceNumberForm = new ProtocolReferenceNumberForm( protocolId, functionType, protReferenceData );
        protocolReferenceNumberForm = new ProtocolReferenceNumberForm( protocolId, getMode(hmModeDetails), protReferenceData );
        protocolReferenceNumberForm.setVecReferenceTypeCode(referenceNumberTypes);
        //protocolReferenceNumberForm.setVecColumnNames(referenceParameters);// Commented to use CoeusLabel instead Module Parameter XML file Start :02-Sep-2005
        protocolReferenceNumberForm.setFormData();
        protocolReferenceNumberForm.showProtocolReferenceForm();
        // added by manoj to fix the bug -- 18/09/2003
        isProtoRefSaveReq = isProtoRefSaveReq || protocolReferenceNumberForm.isSaveRequired() || functionType == COPY_MODE;
        if(isProtoRefSaveReq){
            if(saveRequired != true){
                saveRequired = isProtoRefSaveReq;
            }
            if(protocolReferenceNumberForm.isSaveRequired() || functionType == COPY_MODE){
                protReferenceData = protocolReferenceNumberForm.getFormData();
                protocolInfo.setReferences(protReferenceData);
                tempReferences = protReferenceData;
            }else{
                protocolInfo.setReferences(protReplicateRefData);
            }
            //            protReferenceData = constructProtocolRef(protReferenceData);
            //            setReferenceStatusFlags();
            //protReferenceData = constructProtocolRef(protReferenceData);
        }else{
            protocolInfo.setReferences(protReplicateRefData);
            //protReferenceData = protReplicateRefData;
            //            protReferenceData = protocolReferenceNumberForm.getFormData();
            //            protReferenceData = constructProtocolRef(protReferenceData);
            //            setReferenceStatusFlags();
            //          protocolInfo.setReferences(protReferenceData);
        }
        //protocolReferenceNumberForm = null;
    }
    
    private void setReferenceStatusFlags(){
        
        //Implemented by Raghunath P.V. for implementing Indicator Logic
        protocolInfo.setIsAllReferencesDelete( setRefIsAllDeletes( protReferenceData ) );
        /* commented to send all the data from client to generalize seq no. implementation in server
        Vector refNumbers = filterReferences(protReferenceData);*/
        
        if(protocolReferenceNumberForm != null && isProtoRefSaveReq){
            //protocolInfo.setReferences(refNumbers);
            protocolInfo.setReferences(protReferenceData);
            protocolInfo.getProtocolDetailChangeFlags().setIsReferencesChanged(true);
            protocolInfo.setReferenceIndicatorStatus(MODIFIED);
            
        }else{
            if( protReferenceData != null
            && protReferenceData.size() > 0) {
                protocolInfo.setReferenceIndicatorStatus(NOT_MODIFIED);
            }else{
                protocolInfo.setReferenceIndicatorStatus(NOT_PRESENT);
            }
            protocolInfo.setReferences(null);
            
        }
    }
    
    /** This method is called from Save Menu Item under File Menu.
     * Saves the changes made for all the tab pages in this screen.
     */
    public void saveActiveSheet() {
        try {
            if(isSaveRequired() || functionType == ADD_MODE){
                //Case :#3149 Tabbing between fields does not work on Others tabs - Start
                 if(tbdPnProtocol.getSelectedIndex() == OTHERS_TAB && customElementsForm != null && 
                        customElementsForm.getTable().isEnabled()) {
                     customTable = customElementsForm.getTable();
                     row = customElementsForm.getRow();
                     column = customElementsForm.getColumn();
                     isotherTabSelected = true;
                 }
                 //Case :#3149 - End
                //Coeus4.3 email notification implementation - start
                char tempFunctionType = functionType;
                setProtocolInfo();
                //Added for case 4350 - Protocol - Creating renewals action is not sending mails - Start
                if(tempFunctionType == ADD_MODE) {
                    ProtocolMailController mailController = new ProtocolMailController();
                    synchronized(mailController) {
                        mailController.sendMail(ACTION_NEW_PROTOCOL, protocolInfo.getProtocolNumber(),protocolInfo.getSequenceNumber());
                        //100 - ProtocolCreation
                    }
                }
                else if(tempFunctionType == COPY_MODE) {
                    if(requestedModule == CoeusGuiConstants.PROTOCOL_AMENDMENT_CODE) {
                        ProtocolMailController mailController = new ProtocolMailController();
                        synchronized(mailController) {
                            mailController.sendMail(ACTION_NEW_AMENDMENT, protocolInfo.getProtocolNumber(),protocolInfo.getSequenceNumber());
                            //103 - Amendment Creation
                        }
                    }else if(requestedModule == CoeusGuiConstants.PROTOCOL_RENEWAL_CODE) {
                        ProtocolMailController mailController = new ProtocolMailController();
                        synchronized(mailController) {
                            mailController.sendMail(ACTION_NEW_RENEWAL, protocolInfo.getProtocolNumber(), protocolInfo.getSequenceNumber());
                            //102 - Renewal Creation
                        }
                    }else if(requestedModule == CoeusGuiConstants.PROTOCOL_AMENDMENT_WITH_RENEWAL_CODE) {
                        ProtocolMailController mailController = new ProtocolMailController();
                        synchronized(mailController) {
                        mailController.sendMail(ACTION_NEW_RENEW_WITH_AMEND, protocolInfo.getProtocolNumber(), protocolInfo.getSequenceNumber());
                            //117 - Renewal with Amendment Creation
                        }
                    }
                }
                //Added for case 4350 - Protocol - Creating renewals action is not sending mails - End
            }
        } catch (CoeusUIException coeusUIException) {
            CoeusOptionPane.showDialog(coeusUIException);
            tbdPnProtocol.setSelectedIndex(coeusUIException.getTabIndex());
            //Case :#3149 Tabbing between fields does not work on Others tabs - Start
            if(!customElementsForm.getOtherColumnElementData().isEmpty() &&
                    customElementsForm.getOtherTabMandatory() && 
                    getFunctionType() != DISPLAY_MODE && 
                    customElementsForm.getTable().isEnabled()){
                boolean[] lookUpAvailable = customElementsForm.getLookUpAvailable();
                customTable = customElementsForm.getTable();
                row = customElementsForm.getmandatoryRow();
                column = customElementsForm.getmandatoryColumn();
                if(lookUpAvailable[row]){
                    customTable.editCellAt(row,column+1);
                    customTable.setRowSelectionInterval(row,row);
                    customTable.setColumnSelectionInterval(column+1,column+1);
                    
                }else{
                    customTable.editCellAt(row,column);
                    customTable.setRowSelectionInterval(row,row);
                    customTable.setColumnSelectionInterval(column,column);
                }
            }
            //Case :#3149 - End
        }catch(Exception e) {
            e.printStackTrace();
            CoeusOptionPane.showErrorDialog(e.getMessage());
        }
        //Case :#3149 Tabbing between fields does not work on Others tabs - Start
        if(tbdPnProtocol.getSelectedIndex() == OTHERS_TAB && getFunctionType() != DISPLAY_MODE && 
                customElementsForm != null && customElementsForm.getTable().isEnabled()) {
            boolean[] lookUpAvailable = customElementsForm.getLookUpAvailable();
            customTable = customElementsForm.getTable();
            row = customElementsForm.getRow();
            column = customElementsForm.getColumn();
            if(row == -1){
                row = 0;
            }
            if(column == -1){
                column = 0;
            }
            if(lookUpAvailable[row]){
                customTable.editCellAt(row,column);
                customTable.setRowSelectionInterval(row,row);
                customTable.setColumnSelectionInterval(column,column);
                
            }else{
                customTable.editCellAt(row,column);
                customTable.setRowSelectionInterval(row,row);
                customTable.setColumnSelectionInterval(column,column);
            }
        }
        //Case :#3149 - End
    }
    
    /** Getter for property sequenceId.
     * @return Value of property sequenceId.
     */
    public int getSequenceId() {
        return sequenceId;
    }
    
    /** Setter for property sequenceId.
     * @param sequenceId New value of property sequenceId.
     */
    public void setSequenceId(int sequenceId) {
        this.sequenceId = sequenceId;
    }
    
    /**
     * This method is used to filter the records which are not
     * modified i.e. beans with actype is null.
     * @returns Vector
     */
    /*commented to send all the data from client to generalize seq no. implementation in server
    private Vector filterCorrespondants(Vector correspondents){
     
       Vector vecCorrespondents = null;
       if ( correspondents != null ) {
            vecCorrespondents = new Vector();
            ProtocolCorrespondentsBean compBean = null;
            for( int indx = 0; indx < correspondents.size() ; indx ++ ){
                compBean = ( ProtocolCorrespondentsBean )correspondents.get(  indx );
                if(compBean.getAcType() == null || compBean.getAcType().equalsIgnoreCase("null") ){
                    if(compBean.getSequenceNumber() != sequenceId ){
                        compBean.setAcType("I");
                    }
                }
                if( compBean.getAcType() != null ){
                    vecCorrespondents.addElement( compBean );
                }
            }
       }
       return vecCorrespondents;
    }*/
    // This method is used to set the status of whether all the records in the
    //correspondent tab are deleted or not.
    private boolean setCorrespondanceIsAllDeletes(Vector correspondents){
        
        boolean isAllDeletes = true;
        if ( correspondents != null ) {
            ProtocolCorrespondentsBean compBean = null;
            for( int indx = 0; indx < correspondents.size() ; indx ++ ){
                compBean = ( ProtocolCorrespondentsBean )correspondents.get(  indx );
                String childAcType = compBean.getAcType();
                if( childAcType == null || !childAcType.equalsIgnoreCase("D") ){
                    isAllDeletes = false;
                    break;
                }
            }
        }
        return isAllDeletes;
    }
    
    // This method is used to set the status of whether all the records in the
    //protocol references tab are deleted or not.
    private boolean setRefIsAllDeletes(Vector refNum){
        
        boolean isAllDeletes = true;
        if ( refNum != null ) {
            ReferencesBean compBean = null;
            for( int indx = 0; indx < refNum.size() ; indx ++ ){
                compBean = ( ReferencesBean )refNum.get(  indx );
                String childAcType = compBean.getAcType();
                if( childAcType == null || !childAcType.equalsIgnoreCase("D") ){
                    isAllDeletes = false;
                    break;
                }
            }
        }
        return isAllDeletes;
    }
    
    private Vector constructProtocolRef(Vector vecRef){
        
        if(vecRef != null){
            int intSize = vecRef.size();
            ReferencesBean referencesBean;
            ProtocolReferencesBean protocolReferencesBean = null;
            for(int intRow =0; intRow < intSize ; intRow++){
                
                referencesBean = (ReferencesBean) vecRef.elementAt(intRow);
                protocolReferencesBean = new ProtocolReferencesBean(referencesBean);
                protocolReferencesBean.setProtocolNumber( protocolId );
                protocolReferencesBean.setSequenceNumber( sequenceId );
                vecRef.setElementAt(protocolReferencesBean,intRow);
            }
        }
        return vecRef;
    }
    
    private Vector constructProtCustElement(Vector vecCustElement){
        
        if(vecCustElement != null){
            int intSize = vecCustElement.size();
            CustomElementsInfoBean customElementsInfoBean;
            for(int intRow =0; intRow < intSize ; intRow++){
                customElementsInfoBean = (CustomElementsInfoBean) vecCustElement.elementAt(intRow);
                ProtocolCustomElementsInfoBean proCustBean = new ProtocolCustomElementsInfoBean(customElementsInfoBean);
                proCustBean.setProtocolNumber( protocolId );
                proCustBean.setSequenceNumber( sequenceId );
                vecCustElement.setElementAt(proCustBean,intRow);
            }
        }else{
            
        }
        return vecCustElement;
    }
    /**
     * This method is used to filter the records which are not
     * modified i.e. beans with actype is null.
     * @returns Vector
     */
    /*
    private Vector filterReferences(Vector refNum){
     
       Vector vecRefNum = null;
       if ( refNum != null ) {
            vecRefNum = new Vector();
            ProtocolReferencesBean compBean = null;
            for( int indx = 0; indx < refNum.size() ; indx ++ ){
                compBean = ( ProtocolReferencesBean )refNum.get(  indx );
                if( compBean.getAcType() == null ){
                    if(compBean.getSequenceNumber() != sequenceId ){
                        compBean.setAcType("I");
                    }
                }
                if( compBean.getAcType() != null ){
                    vecRefNum.addElement( compBean );
                }
            }
       }
       return vecRefNum;
    }*/
    
    /**
     * This method is used to filter the records which are not
     * modified i.e. beans with actype is null.
     * @returns Vector
     */
    /*private Vector filterVulnerableSubjects(Vector vulnerableSubjects){
     
       Vector vecVulnerableSubjects = null;
       //System.out.println("In filterVulnerableSubjects");
       if ( vulnerableSubjects != null ) {
           //System.out.println("vulnerableSubjects size is "+vulnerableSubjects.size());
            vecVulnerableSubjects = new Vector();
            ProtocolVulnerableSubListsBean compBean = null;
            for( int indx = 0; indx < vulnerableSubjects.size() ; indx ++ ){
                compBean = ( ProtocolVulnerableSubListsBean )vulnerableSubjects.get(  indx );
                if(compBean.getAcType() == null || compBean.getAcType().equalsIgnoreCase("null") ){
                    if(compBean.getSequenceNumber() != sequenceId ){
                        compBean.setAcType("I");
                    }
                }
                if( compBean.getAcType() != null ){
                    vecVulnerableSubjects.addElement( compBean );
                }
            }
       }
       return vecVulnerableSubjects;
    }*/
    
    // This method is used to set the status of whether all the records in the
    //correspondent tab are deleted or not.
    private boolean setVulnerableSubIsAllDeletes(Vector vulnerableSubjects){
        
        boolean isAllDeletes = true;
        if ( vulnerableSubjects != null ) {
            //System.out.println("vulnerableSubjects size is "+vulnerableSubjects.size());
            ProtocolVulnerableSubListsBean compBean = null;
            for( int indx = 0; indx < vulnerableSubjects.size() ; indx ++ ){
                compBean = ( ProtocolVulnerableSubListsBean )vulnerableSubjects.get(  indx );
                String childAcType = compBean.getAcType();
                if( childAcType == null || !childAcType.equalsIgnoreCase("D") ){
                    isAllDeletes = false;
                    break;
                }
            }
        }
        return isAllDeletes;
    }
    
    /**
     * This method is used to filter the records which are not
     * modified i.e. beans with actype is null.
     * @returns Vector
     */
    /*private Vector filterSpecialReviews(Vector specialReviews){
     
       Vector vecSpecialReviews = null;
       //System.out.println("In filterSpecialReviews");
       if ( specialReviews != null ) {
           int size = specialReviews.size();
           //System.out.println("specialReviews size is "+size);
            vecSpecialReviews = new Vector();
            ProtocolSpecialReviewFormBean compBean = null;
            for( int indx = 0; indx < specialReviews.size() ; indx ++ ){
                compBean = ( ProtocolSpecialReviewFormBean )specialReviews.get(  indx );
                if(compBean.getAcType() == null || compBean.getAcType().equalsIgnoreCase("null") ){
                    if(compBean.getSequenceNumber() != sequenceId ){
                        compBean.setAcType("I");
                    }
                }
                if( compBean.getAcType() != null ){
                    vecSpecialReviews.addElement( compBean );
                }
            }
       }
       return vecSpecialReviews;
    }*/
    
    // This method is used to set the status of whether all the records in the
    //correspondent tab are deleted or not.
    private boolean setSpecialReviewsAllDeletes(Vector specialReviews){
        
        boolean isAllDeletes = true;
        if ( specialReviews != null ) {
//            int size = specialReviews.size();
            //System.out.println("specialReviews size is "+size);
            ProtocolSpecialReviewFormBean compBean = null;
            for( int indx = 0; indx < specialReviews.size() ; indx ++ ){
                compBean = ( ProtocolSpecialReviewFormBean )specialReviews.get(  indx );
                String childAcType = compBean.getAcType();
                if( childAcType == null || !childAcType.equalsIgnoreCase("D") ){
                    isAllDeletes = false;
                    break;
                }
            }
        }
        return isAllDeletes;
    }
    
    /**
     * This method is used to filter the records which are not
     * modified i.e. beans with actype is null.
     * @returns Vector
     */
    /*private Vector filterFundingSources(Vector fundingSources){
     
       Vector vecFundingSources = null;
       //System.out.println("In filterSpecialReviews");
       if ( fundingSources != null ) {
           int size = fundingSources.size();
           //System.out.println("specialReviews size is "+size);
            vecFundingSources = new Vector();
            ProtocolFundingSourceBean compBean = null;
            for( int indx = 0; indx < fundingSources.size() ; indx ++ ){
                compBean = ( ProtocolFundingSourceBean )fundingSources.get(  indx );
                if(compBean.getAcType() == null || compBean.getAcType().equalsIgnoreCase("null") ){
                    if(compBean.getSequenceNumber() != sequenceId ){
                        compBean.setAcType("I");
                    }
                }
                if( compBean.getAcType() != null ){
                    vecFundingSources.addElement( compBean );
                }
            }
       }
       return vecFundingSources;
    }*/
    
    // This method is used to set the status of whether all the records in the
    //correspondent tab are deleted or not.
    private boolean setFundingSourceAllDeletes(Vector fundingSources){
        
        boolean isAllDeletes = true;
        if ( fundingSources != null ) {
//            int size = fundingSources.size();
            //System.out.println("specialReviews size is "+size);
            ProtocolFundingSourceBean compBean = null;
            for( int indx = 0; indx < fundingSources.size() ; indx ++ ){
                compBean = ( ProtocolFundingSourceBean )fundingSources.get(  indx );
                String childAcType = compBean.getAcType();
                if( childAcType == null || !childAcType.equalsIgnoreCase("D") ){
                    isAllDeletes = false;
                    break;
                }
            }
        }
        return isAllDeletes;
    }
    
    
    private int getMaxAmendmentVersion( String protocolNo ) throws Exception {
        
        RequesterBean requester = new RequesterBean();
        Vector dataObject = new Vector();
        requester.setFunctionType(MAX_AMEND_VER);
        requester.setId(protocolNo);
        String connectTo = CoeusGuiConstants.CONNECTION_URL+PROTOCOL_SERVLET;
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo,requester);
        comm.send();
        ResponderBean res = comm.getResponse();
        if( res.isSuccessfulResponse() ) {
            dataObject = (Vector) res.getDataObjects();
            if( dataObject != null && dataObject.size() > 0 ) {
                Integer maxVal = (Integer)dataObject.elementAt(0);
                return maxVal.intValue();
            }
        }else{
            throw new Exception(res.getMessage());
        }
        return 0;
    }
    
    private int getMaxRevisionVersion( String protocolNo ) throws Exception {
        
        RequesterBean requester = new RequesterBean();
        Vector dataObject = new Vector();
        requester.setFunctionType(MAX_REVISION_VER);
        requester.setId(protocolNo);
        String connectTo = CoeusGuiConstants.CONNECTION_URL+PROTOCOL_SERVLET;
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo,requester);
        comm.send();
        ResponderBean res = comm.getResponse();
        if( res.isSuccessfulResponse() ) {
            dataObject = (Vector) res.getDataObjects();
            if( dataObject != null && dataObject.size() > 0 ) {
                Integer maxVal = (Integer)dataObject.elementAt(0);
                return maxVal.intValue();
            }
        }else{
            throw new Exception(res.getMessage());
        }
        return 0;
    }
    
    public void update(Observable o, Object arg) {
        if( o instanceof LockObservable ) {
            recordLockedStatus = ((LockObservable)o).getLockStatus();
        }
    }
    
    private boolean canLockProtocol() {
        if( recordLockedStatus == -1 ) {
            try{
                if( RecordLocker.lock("PROTOCOL", protocolId ) ){
                    recordLockedStatus = CoeusGuiConstants.LOCK_SUCCESSFUL;
                    lockObservable.setLockStatus(CoeusGuiConstants.LOCK_SUCCESSFUL);
                    lockObservable.notifyObservers();
                    return true;
                }
            }catch(CoeusException coe) {
                recordLockedStatus = CoeusGuiConstants.LOCK_UNSUCCESSFUL;
                lockObservable.setLockStatus(CoeusGuiConstants.LOCK_UNSUCCESSFUL);
                lockObservable.notifyObservers();
                CoeusOptionPane.showDialog(new CoeusClientException(coe));
                return false;
            }
        }else if ( recordLockedStatus == CoeusGuiConstants.LOCK_SUCCESSFUL ) {
            return true;
        }
        return false;
    }
    // For the undo last action  - Added by Jobin - start
    /**
     * This method is to perform the undo last action.
     * making the server call and reload the screen with the protocol details
     * @throws CoeusException
     */
    private void performUndoLastAction() throws CoeusException {
        
        String connectTo = CoeusGuiConstants.CONNECTION_URL + "/protocolActionServlet";
        RequesterBean request = new RequesterBean();
        request.setFunctionType('U');
        request.setId(protocolId);
        AppletServletCommunicator comm = new AppletServletCommunicator(
        connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response.isSuccessfulResponse()) {
            protocolInfo = (ProtocolInfoBean)response.getDataObject();
            try {
                updateDataForLastActions(protocolInfo);
                // Added for COEUSQA-1432 : Can't undo expire action - Start
                // To update the Protocol list after perfoming the UNDO last action
                observable.notifyObservers(protocolInfo);
                // Added for COEUSQA-1432 : Can't undo expire action - End
            } catch (Exception e) {
                throw new CoeusException(e.getMessage());
            }
            CoeusOptionPane.showInfoDialog(
            coeusMessageResources.parseMessageKey("protoUndoAction_exceptionCode.1001")) ;
            mdiForm.removeFrame(referenceTitle,referenceId);
            this.doDefaultCloseAction();
        } else {
            /*
             *  Commented by Geo. The error message was showing two times
             */
            ////            CoeusOptionPane.showErrorDialog(response.getMessage());
            //            if (response.isCloseRequired()) {
            //                mdiForm.removeFrame(referenceTitle, referenceId);
            //                isTimedOut = true;
            //                closed = true;
            //                this.doDefaultCloseAction();
            //                return;
            //            }/* else {
            //                throw new CoeusException(response.getMessage());
            //            }
            //             */
            //Added for COEUSQA-3144 : IRB and IACUC - Add warning to Undo Last Action that review comments will be lost - start  
            // throw new CoeusException(response.getMessage());                     
            Object dataObject = response.getDataObject();
            if(dataObject != null && DIALOG_TYPE_POP_UP_FOR_ACTION.equals(dataObject)) {
                
                int option = CoeusOptionPane.showQuestionDialog(response.getMessage(),
                         CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_YES);
                 
               
                if(option == CoeusOptionPane.SELECTION_YES) { // Do Undo operation
                    request.setFunctionType(UNDO_LAST_ACTION_CONFIRMATION);
                    
                    comm.send();
                    response = comm.getResponse();
                    
                    if (response.isSuccessfulResponse()) {
                        protocolInfo = (ProtocolInfoBean)response.getDataObject();
                        try {
                            updateDataForLastActions(protocolInfo);
                        } catch (Exception e) {
                            throw new CoeusException(e.getMessage());
                        }
                        
                        CoeusOptionPane.showInfoDialog(
                                coeusMessageResources.parseMessageKey("protoUndoAction_exceptionCode.1001")) ;
                        mdiForm.removeFrame(referenceTitle,referenceId);
                        this.doDefaultCloseAction();
                    } else {
                        throw new CoeusException(response.getMessage());
                    }
                }
                
            }else{
                throw new CoeusException(response.getMessage());
            }
            
           //Added for COEUSQA-3144 : IRB and IACUC - Add warning to Undo Last Action that review comments will be lost - end       
        }
    }
    /**
     * Update the details for the undo last action details
     */
    private void updateDataForLastActions(ProtocolInfoBean protocolInfo) throws Exception{
        /** begin: fixed bug with id #147  */
       /*setStatusMessage(coeusMessageResources.parseMessageKey(
            "general_saveCode.2275"));*/
        /** end: fixed bug with id #147  */
        saveType = 'S'; // setting save type to normal update as the record will be locked with entire protocol no. after first save in amendment / renewal
        this.saveRequired = false;
        this.protocolId = protocolInfo.getProtocolNumber();
        this.sequenceId = protocolInfo.getSequenceNumber();
        if( functionType == DISPLAY_MODE ) {
            protocolMaintenance.setValues(protocolInfo);
            protocolVecNotes = protocolInfo.getVecNotepad();
            protocolNotepadForm.setProtocolVecNotes(protocolVecNotes);
            protocolNotepadForm.setSaveRequired(false);
            if( rolesForm != null) {
                rolesForm.setSaveRequired(false);
            }
            tempUserRoles = null;
            userAndRoleDetails = null;
            exisitingUserRoles = protocolInfo.getUserRolesInfoBean();
            copyOfExistingUserRoles = (Vector) ObjectCloner.deepCopy(protocolInfo.getUserRolesInfoBean());
            return;
        }
        btnSummary.setEnabled( true );
        //raghuSV : to enable submission details menu item
        //starts...
        submissionDetails.setEnabled(true);
        //ends
        
        if( requestedModule == CoeusGuiConstants.PROTOCOL_DETAIL_CODE ) {
            observable.setFunctionType(functionType);
            observable.notifyObservers(protocolInfo);
            //updateRow();
            if(this.getTitle().indexOf('-') == -1 ){
                setTitle(getTitle() + " - " + protocolInfo.getProtocolNumber());
            }
        }
        
        
        protReferenceData = protocolInfo.getReferences();
        
        if( protocolReferenceNumberForm != null){
            
            protocolReferenceNumberForm.setSaveRequired(false);
            //modified for Coeus Enhancement case:#1787 step 15
            isProtoRefSaveReq = false;
            protReplicateRefData = null;
            tempReferences = null;
        }
        
        if( rolesForm != null ){
            rolesForm.setSaveRequired( false );
        }
        //modified for Coeus Enhancement case:#1787 step 16 starts
        /*
        protRelProjects = protocolInfo.getRelatedProjects();
        setRelatedProjectsData(protocolInfo.getRelatedProjects());
        vecProtoRelProjTemp = protocolInfo.getRelatedProjects();
        
        if(protocolRelatedProjects != null){
            protocolRelatedProjects.setSaveRequired(false);
            isProtoRelSaveReq = false;
            
        }
        */
        tempUserRoles = null;
        userAndRoleDetails = null;
        exisitingUserRoles = protocolInfo.getUserRolesInfoBean();
        copyOfExistingUserRoles = (Vector) ObjectCloner.deepCopy(protocolInfo.getUserRolesInfoBean());
        setSelected(true);
        setVisible(true);
        
    }
    // Added by Jobin - End
    
    public void saveAsActiveSheet() {
    }
    
    //Added by Vyjayanthi - 02/08/2004 for IRB Enhancement
    /**
     * Setter for property vecProtocolData.
     * @param vecProtocolData New value of property vecProtocolData.
     */
    public void setProtocolData(java.util.Vector vecProtocolData) {
        this.vecProtocolData = vecProtocolData;
    }
    
    class EscapeKeyListener extends AbstractAction {
        public EscapeKeyListener(String name ) {
            super(name);
        }
        public EscapeKeyListener(){
            super();
        }
        public void actionPerformed(ActionEvent ae ) {
            Object source = ae.getSource();
            System.out.println("esc KeyPressed");
            System.out.println("source:"+source);
            if( submissionForm != null && source.equals( submissionForm.getRootPane() ) ) {
                System.out.println("in escKey of submissionForm");
                saveSubmissionDetails();
            }else if( dlgRoles != null && source.equals( dlgRoles.getRootPane() )){
                System.out.println("in escKey of roles");
                validateRolesData();
            }
        }
    }
    
    //Protocol Enhancement -  Administrative Correction Start 7
    private void displayAdminCorrComments(){
        dlgAdminCorrComments = new CoeusDlgWindow(mdiForm);
        adminCorrComments = new AdministrativeCorrectionComments();
        adminCorrComments.btnOK.addActionListener(this);
        adminCorrComments.btnCancel.addActionListener(this);
        
        
        dlgAdminCorrComments.setResizable(false);
        dlgAdminCorrComments.setModal(true);
        dlgAdminCorrComments.getContentPane().add(adminCorrComments);
        dlgAdminCorrComments.setTitle(WINDOW_TITLE);
        dlgAdminCorrComments.setFont(CoeusFontFactory.getLabelFont());
        dlgAdminCorrComments.setSize(WIDTH, HEIGHT);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgAdminCorrComments.getSize();
        dlgAdminCorrComments.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        
        java.awt.Component[] components = { adminCorrComments.txtArComments,
        adminCorrComments.btnOK, adminCorrComments.btnCancel
        };
        
        ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
        adminCorrComments.setFocusTraversalPolicy(traversePolicy);
        adminCorrComments.setFocusCycleRoot(true);
        
         dlgAdminCorrComments.addComponentListener(
            new ComponentAdapter(){
                public void componentShown(ComponentEvent e){
                    adminCorrComments.txtArComments.requestFocusInWindow();
                }
        });
        
        dlgAdminCorrComments.addEscapeKeyListener(new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                closed = true;
                mdiForm.removeFrame(referenceTitle,referenceId);
                doDefaultCloseAction();
                dlgAdminCorrComments.dispose();
            }
        });
       
        dlgAdminCorrComments.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgAdminCorrComments.addWindowListener(new WindowAdapter(){
             public void windowOpening(WindowEvent we){
                 //adminCorrComments.btnCancel.requestFocusInWindow();
             }
             public void windowClosing(WindowEvent we){
                 closed = true;
                 mdiForm.removeFrame(referenceTitle,referenceId);
                 doDefaultCloseAction();
                 dlgAdminCorrComments.dispose();
             }
        });
        
        dlgAdminCorrComments.setVisible (true);
        
    }
     
    private void performOkAction(){
        if(adminCorrComments.txtArComments.getText().trim().equals("")){
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
                "protoMntFrm_AdminCorrCode.1081"));
        }else{        
            ProtocolActionsBean newProtoActnBean = new ProtocolActionsBean();        
            String comments = adminCorrComments.txtArComments.getText().trim();
            
            //Specifies the Administrative correction
            newProtoActnBean.setActionId(113);
            
            newProtoActnBean.setComments(comments);
            newProtoActnBean.setProtocolNumber(protocolInfo.getProtocolNumber());
            newProtoActnBean.setAcType("I");
            if(protActions == null){
                protActions = new Vector();
            }
            protActions.addElement(newProtoActnBean);
            protocolInfo.setActions(protActions);
            dlgAdminCorrComments.dispose();
        }
    }
    //Protocol Enhancement -  Administrative Correction End 7
    
     //Case 1787 Start 25
    /**
     * Setter for property canModifyInDisplay.
     * @param canModifyInDisplay New value of property canModifyInDisplay.
     */
    public void setCanModifyInDisplay(boolean canModifyInDisplay) {
        this.canModifyInDisplay = canModifyInDisplay;
    }
    //Case 1787 End 25
    
    //Case 1787 
    /*The method check if the protocol is locked by an other user
     * and locks the protocol if lock is available
     * Returns <CODE>lockAvailable</CODE>
     */
    public boolean lockProtocol()
    {
    
              boolean lockAvailable = false;
                    if(functionType == DISPLAY_MODE)
                   {
                       String connectTo = CoeusGuiConstants.CONNECTION_URL + PROTOCOL_SERVLET;
                        RequesterBean request = new RequesterBean();
                        request.setFunctionType('p');
                        request.setId(protocolId);
                        AppletServletCommunicator comm = new AppletServletCommunicator(
                        connectTo, request);
                        comm.send();
                        ResponderBean response = comm.getResponse();
                        if(response==null){
                            CoeusOptionPane.showErrorDialog("Could not contact server");
                        }
                        else 
                        {  
                        
                           lockAvailable =((Boolean)response.getDataObject()).booleanValue();
                           if(!lockAvailable)
                           
                                {
                                CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey("protocolDetForm_exceptionCode.1019"));
                               //  throw new CoeusClientException(coeusMessageResources.parseMessageKey("protocolDetForm_exceptionCode.1019"),CoeusClientException.ERROR_MESSAGE);
                                }
                          
                       // lockExist = lockAvailable;
                        }
                    }
     return lockAvailable;
     
    }
    //Commented for case 3552 - IRB attachments - start
    //Added for Protocol Upload Documents start 6
//    private void showUploadDocumentWindow() throws Exception{
//        if(functionType == 'A' || functionType == 'N' || functionType == 'R'
//            || isSaveRequired()){
//            CoeusOptionPane.showInfoDialog(
//                coeusMessageResources.parseMessageKey(PROTOCOL_SAVE_INFO));
//            return;
//        }
//        try{
//            mdiForm.setCursor(new Cursor(java.awt.Cursor.WAIT_CURSOR));
//            //code modified and added for coeus4.3 enhancements
//            // To check whether the module is editable.             
//            HashMap hmModeDetails = new HashMap();
//            hmModeDetails.put("functionType", new Character(functionType));
//            hmModeDetails.put("protocolNumber", protocolInfo.getProtocolNumber());
//            hmModeDetails.put("hmEditModules", getFormatedModuleData(getVecModuleData()));
//            hmModeDetails.put("moduleId", IrbWindowConstants.UPLOAD_DOCUMENTS);
////            ProtocolUploadDocumentForm protocolUploadDocumentForm
////            = new ProtocolUploadDocumentForm(protocolInfo,functionType,strTitle);
//            ProtocolUploadDocumentForm protocolUploadDocumentForm
//            = new ProtocolUploadDocumentForm(protocolInfo,getMode(hmModeDetails));
//            if(protocolId != null){
//                protocolUploadDocumentForm.setWindowType("Protocol Upload");
//                if(( protocolId.indexOf('A') != -1 ) ||
//                ( protocolId.indexOf('R') != -1 )) {
//                    protocolUploadDocumentForm.setWindowType("Not Parent Protocol");
//                }
//            }
//            CoeusVector cvDocType 
//                = (CoeusVector)protocolUploadDocumentForm.getDocumentData('b',null,null);
//            if(cvDocType == null || cvDocType.size() == 0 ) {
//                CoeusOptionPane.showInfoDialog(
//                coeusMessageResources.parseMessageKey("protocolUpload_exceptionCode.1008"));
//                return;
//            }
//            protocolUploadDocumentForm.setFormData(cvDocType);
//            //protocolUploadDocumentForm.display();
//            protocolUploadDocumentForm.cleanup();
//            protocolUploadDocumentForm = null;
//        }catch (Exception e){
//            CoeusOptionPane.showErrorDialog(e.getMessage());
//            e.printStackTrace();
//        }finally{
//            mdiForm.setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));
//        }
//        
//    }
    //Added for Protocol Upload Documents end 6
    //Commented for case 3552 - IRB attachments - end
    //Added for Protocol Questionnaire start 5
    private void showQuestionnaireWindow() throws Exception{
        if(functionType != DISPLAY_MODE && (isSaveRequired() || functionType == ADD_MODE)){
           setProtocolInfo();
        }
        // Added with CoeusQA2313: Completion of Questionnaire for Submission
        checkQuestionnaireRevision();
        // CoeusQA2313: Completion of Questionnaire for Submission - End
        String title = CoeusGuiConstants.PROTOCOL_QUESTIONNAIRE; 
        QuestionnaireAnswersBaseForm listWindow = null;
        if( ( listWindow = (QuestionnaireAnswersBaseForm)mdiForm.getFrame(
         title))!= null ){
             if( listWindow.isIcon() ){
                 listWindow.setIcon(false);
             }
             listWindow.setSelected( true );
             return;
         }
        //Added for COEUSDEV-86 : Questionnaire for a Submission - Start
        int subModuleItemCode = setSubModuleItemCode();
        //COEUSDEV-86 : END
        // Moved code to getQuestionnaireHeaderInfo with CoeusQA2313: Completion of Questionnaire for Submission
        QuestionnaireAnswerHeaderBean questionnaireModuleObject = getQuestionnaireHeaderInfo();
        // CoeusQA2313: Completion of Questionnaire for Submission - End
        QuestionnaireAnswersBaseController questionnaireAnswersBaseController
              = new QuestionnaireAnswersBaseController(title, mdiForm);
        questionnaireAnswersBaseController.setFormData(questionnaireModuleObject);
        // Added with CoeusQA2313: Completion of Questionnaire for Submission
        // Set the selected original protocol questionnaire Data for amendment/renewal
        if(subModuleItemCode == 1){
                questionnaireAnswersBaseController.setAllSelectedOriginalProtocolQnr(
                        amendRenewalForm.getAllEditingOriginalProtocolQuestionnaires());
        }
        // CoeusQA2313: Completion of Questionnaire for Submission - End
        // Unable to submit Amendment Renewal, when there are mandatory questionnaires - Start
//        //code modified and added for coeus4.3 enhancements - starts
//        // To open this module in display mode while protocol is a Amendment or 
//        // Renewal protocol.
//        if((protocolInfo.getProtocolNumber()!=null && protocolInfo.getProtocolNumber().length()>10) ||
//                functionType == CoeusGuiConstants.AMEND_MODE){
//            questionnaireAnswersBaseController.setFunctionType('D');
//        } else {
//            questionnaireAnswersBaseController.setFunctionType(getFunctionType());
//        }
//        //code modified and added for coeus4.3 enhancements - ends
        questionnaireAnswersBaseController.setFunctionType(getFunctionType());
        // Unable to submit Amendment Renewal, when there are mandatory questionnaires - End
        CoeusVector cvQuestionnaireData 
            = (CoeusVector)questionnaireAnswersBaseController.getFormData();
        if(cvQuestionnaireData == null || cvQuestionnaireData.isEmpty()) {
            cvQuestionnaireData = null;
            CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey("questions_exceptionCode.1009"));
            questionnaireAnswersBaseController.cleanUp();
            return;
        }
        questionnaireAnswersBaseController.setTabData();
        questionnaireAnswersBaseController.display();
        questionnaireModuleObject = null;
        questionnaireAnswersBaseController = null;
    }
    //Added for Protocol Questionnaire end 5
       //Case 2026 Start 
    /**
     * Setter for property canFundingSrcModifyInDisplay.
     * @param canFundingSrcModifyInDisplay New value of property canFundingSrcModifyInDisplay.
     */
    public void setFundingSrcModifyInDisplay(boolean canFundingSrcModifyInDisplay) {
        this.canFundingSrcModifyInDisplay = canFundingSrcModifyInDisplay;
    }
    //Case 2026 End 
    
    /**
     * To get the modules data in modulecode as key and protocolNumber as value format
     * @param vecModuleData 
     * @return HashMap
     */
    public HashMap getFormatedModuleData(Vector vecModuleData){
        if(vecModuleData == null){
            if(protocolInfo != null && protocolInfo.getAmendmentRenewal() != null
                    && protocolInfo.getAmendmentRenewal().size()>0){
                vecModuleData = protocolInfo.getAmendmentRenewal();
                ProtocolAmendRenewalBean bean =(ProtocolAmendRenewalBean) vecModuleData.get(0);
                vecModuleData = bean.getVecModuleData();
            }
        }        
        HashMap hmModuleData = new HashMap();
        ProtocolModuleBean protocolModuleBean = null;
        if(vecModuleData!=null && vecModuleData.size() > 0){
            for(int index = 0 ; index < vecModuleData.size() ; index++){
                protocolModuleBean = (ProtocolModuleBean) vecModuleData.get(index);
                if(protocolModuleBean!=null){
                    hmModuleData.put(protocolModuleBean.getProtocolModuleCode(),
                            protocolModuleBean.getProtocolAmendRenewalNumber());
                }
            }
        }
        return hmModuleData;
    }
    
    /**
     * Get the mode details for the module that is editable or not for
     * the Amendment or Renewal protocol.
     * @param hmModeDetails 
     * @return char
     */
    public char getMode(HashMap hmModeDetails){
        char functionType = (hmModeDetails.get("functionType") == null) ? ' ' :
                        ((Character)hmModeDetails.get("functionType")).charValue();
        String protocolNumber = (String) hmModeDetails.get("protocolNumber");
        HashMap hmEditModules = (HashMap) hmModeDetails.get("hmEditModules");
        String moduleKey = (String) hmModeDetails.get("moduleId");
        
        if(functionType == CoeusGuiConstants.AMEND_MODE){
            return functionType;
        } else if(protocolNumber!=null && protocolNumber.length()>10 &&
                hmEditModules != null && 
                hmEditModules.containsKey(moduleKey) && 
                !hmEditModules.get(moduleKey).equals(protocolNumber)){
            functionType = 'D';
        } else if(protocolNumber!=null && protocolNumber.length()>10 &&
                hmEditModules != null &&
                !hmEditModules.containsKey(moduleKey)){
            functionType = 'D';
        }        
        return functionType;
    }
    
    /**
     * Check whether any mandatory fields added in the custom data
     * @param protCustomElements 
     * @return boolean
     */
    private boolean isMandatoryFieldExist(Vector protCustomElements){
        boolean isMandatory = false;
        if(protCustomElements!=null && protCustomElements.size() > 0){
            for(int index = 0 ; index < protCustomElements.size() ; index++){
                ProtocolCustomElementsInfoBean bean = (ProtocolCustomElementsInfoBean)
                    protCustomElements.get(index);
                if(bean.isRequired() && (bean.getColumnValue() == null || bean.getColumnValue().length() == 0)){
                    isMandatory =  true;
                    break;
                }
            }
        }
        return isMandatory;
    }
    
    /**
     * To get the data for cheking the mode details, and to dispaly the 
     * modules in appropriate mode
     * @return HashMap
     */
    private HashMap getModeDetails(){
        HashMap hmAmendRenew = null;
        HashMap hmModeDetails = new HashMap();
        hmModeDetails.put("functionType", new Character(functionType));
        //To get the editable modules list for Amendment/Renewal
        if((protocolInfo!=null && protocolInfo.getProtocolNumber() != null
                && protocolInfo.getProtocolNumber().length()>10) || 
                functionType == CoeusGuiConstants.AMEND_MODE){
            Vector vecAmendRenewData = protocolInfo.getAmendmentRenewal();
            if(vecAmendRenewData!=null && vecAmendRenewData.size() > 0){
                ProtocolAmendRenewalBean bean =(ProtocolAmendRenewalBean) vecAmendRenewData.get(0);
                hmAmendRenew = getFormatedModuleData(bean.getVecModuleData());
            }
            hmModeDetails.put("protocolNumber", protocolInfo.getProtocolNumber());
            hmModeDetails.put("hmEditModules", hmAmendRenew);
        }
        return hmModeDetails;
    }

    /**
     * To get the List of module data
     * @return Vector
     */
    public Vector getVecModuleData() {
        return vecModuleData;
    }

    /**
     * To set the List of module data 
     * @param vecModuleData 
     */
    public void setVecModuleData(Vector vecModuleData) {
        this.vecModuleData = vecModuleData;
    }

    public char getOriginalFunctionType() {
        return originalFunctionType;
    }

    public void setOriginalFunctionType(char originalFunctionType) {
        this.originalFunctionType = originalFunctionType;
    }

    public ProtocolInfoBean getOriginalProtocolInfo() {
        return originalProtocolInfo;
    }

    public void setOriginalProtocolInfo(ProtocolInfoBean originalProtocolInfo) {
        this.originalProtocolInfo = originalProtocolInfo;
    }

    /**
     * To set the protocol modules indicators
     **/
    private void setProtocolIndicators() {
        if(originalProtocolInfo != null){
            
//            int orgProtLength = (originalProtocolInfo.getProtocolNumber() == null) ? 0 :
//                originalProtocolInfo.getProtocolNumber().length();
//            int newProtLength = (protocolInfo.getProtocolNumber() == null) ? 0 :
//                protocolInfo.getProtocolNumber().length(); 
            int orgCount = 0;
            int newCount = 0;
            if(protocolKey.isSaveRequired() || originalFunctionType == 'E'){
                orgCount = (originalProtocolInfo.getKeyStudyPersonnel() == null) ? 0 :
                    originalProtocolInfo.getKeyStudyPersonnel().size();
                newCount = getModuleDataCount(protocolInfo.getKeyStudyPersonnel());
                protocolInfo.setKeyStudyIndicator(indicatorValue(orgCount, newCount));
            } else {
                protocolInfo.setKeyStudyIndicator(originalProtocolInfo.getKeyStudyIndicator());
            }
            
            if(protocolCorr.isSaveRequired() || originalFunctionType == 'E'){
                orgCount = (originalProtocolInfo.getCorrespondetns() == null) ? 0 :
                    originalProtocolInfo.getCorrespondetns().size();
                newCount = getModuleDataCount(protocolInfo.getCorrespondetns());
                protocolInfo.setCorrespondenceIndicator(indicatorValue(orgCount, newCount));
            } else {
                protocolInfo.setCorrespondenceIndicator(originalProtocolInfo.getCorrespondenceIndicator());
            }
            
            if(protocolFund.isSaveRequired() || originalFunctionType == 'E'){
                orgCount = (originalProtocolInfo.getFundingSources() == null) ? 0 :
                    originalProtocolInfo.getFundingSources().size();
                newCount = getModuleDataCount(protocolInfo.getFundingSources());
                protocolInfo.setFundingSourceIndicator(indicatorValue(orgCount, newCount));
            } else {
                protocolInfo.setFundingSourceIndicator(originalProtocolInfo.getFundingSourceIndicator());
            }
            
            if(protocolSubjects.isSaveRequired() || originalFunctionType == 'E'){
                orgCount = (originalProtocolInfo.getVulnerableSubjectLists() == null) ? 0 :
                    originalProtocolInfo.getVulnerableSubjectLists().size();
                newCount = getModuleDataCount(protocolInfo.getVulnerableSubjectLists());
                protocolInfo.setVulnerableSubjectIndicator(indicatorValue(orgCount, newCount));
            } else {
                protocolInfo.setVulnerableSubjectIndicator(originalProtocolInfo.getVulnerableSubjectIndicator());
            }
            
            if(protocolSpecialReviewForm.isSaveRequired() || originalFunctionType == 'E'){
                orgCount = (originalProtocolInfo.getSpecialReviews() == null) ? 0 :
                    originalProtocolInfo.getSpecialReviews().size();
                newCount = getModuleDataCount(protocolInfo.getSpecialReviews());
                protocolInfo.setSpecialReviewIndicator(indicatorValue(orgCount, newCount));
            } else {
                protocolInfo.setSpecialReviewIndicator(originalProtocolInfo.getSpecialReviewIndicator());
            }
            
            if((protocolReferenceNumberForm != null 
                    && protocolReferenceNumberForm.isSaveRequired())
                    || originalFunctionType == 'E'){
                orgCount = (originalProtocolInfo.getReferences() == null) ? 0 :
                    originalProtocolInfo.getReferences().size();
                newCount = getModuleDataCount(protocolInfo.getReferences());
                protocolInfo.setReferenceIndicator(indicatorValue(orgCount, newCount));
            } else {
                protocolInfo.setReferenceIndicator(originalProtocolInfo.getReferenceIndicator());
            }
        } 
        // New protocol
        else {
            int newCount = (protocolInfo.getKeyStudyPersonnel() == null) ? 0 :
                protocolInfo.getKeyStudyPersonnel().size();
            protocolInfo.setKeyStudyIndicator((newCount > 0) ? "P1" : "N0");
            
            newCount = (protocolInfo.getCorrespondetns() == null) ? 0 :
                protocolInfo.getCorrespondetns().size();
            protocolInfo.setCorrespondenceIndicator((newCount > 0) ? "P1" : "N0");
            
            newCount = (protocolInfo.getFundingSources() == null) ? 0 :
                protocolInfo.getFundingSources().size();
            protocolInfo.setFundingSourceIndicator((newCount > 0) ? "P1" : "N0");
            
            newCount = (protocolInfo.getVulnerableSubjectLists() == null) ? 0 :
                protocolInfo.getVulnerableSubjectLists().size();
            protocolInfo.setVulnerableSubjectIndicator((newCount > 0) ? "P1" : "N0");
            
            newCount = (protocolInfo.getSpecialReviews() == null) ? 0 :
                protocolInfo.getSpecialReviews().size();
            protocolInfo.setSpecialReviewIndicator((newCount > 0) ? "P1" : "N0");
            
            newCount = (protocolInfo.getReferences() == null) ? 0 :
                protocolInfo.getReferences().size();
            protocolInfo.setReferenceIndicator((newCount > 0) ? "P1" : "N0");
        }
    }
    
    /**
     * To get the appropriate indicator for given count of datas
     * @param oldCount original data count for module
     * @param newCount modified data count for module
     * @return String indicator
     */
    private String indicatorValue(int oldCount, int newCount){
        
        String indicator = "P1";
        
        // Normal Protocol
        if(oldCount == 0 && newCount == 0){
            indicator = "N0";
        } else if(oldCount == 0 && newCount > 0){
            indicator = "P1";
        } else if(oldCount > 0 && newCount == 0){
            indicator = "N1";
        } else if(oldCount > 0 && newCount > 0){
            indicator = "P1";
        }
        
        //Amendment and Renewal Protocol
        if(indicator == "P1" && originalFunctionType == 'E'){
            indicator = "P0";
        } else if(indicator == "N1" && originalFunctionType == 'E'){
            indicator = "N0";
        }
        
        return indicator;
    }
    
    /**
     * To get the count of datas to be modified for the particular module
     * @param vecData module datas
     * @return int number of records to modify
     */
    private int getModuleDataCount(Vector vecData){
        int validCount = 0;
        if(vecData != null && vecData.size() > 0){
            for(int index = 0 ; index < vecData.size() ; index++){
                IBaseDataBean dataBean = (IBaseDataBean) vecData.get(index);
                //Code modified for Case#3070 - Ability to change a funding source - starts
                if(dataBean != null && (dataBean.getAcType() == null 
                        || !dataBean.getAcType().equals(TypeConstants.DELETE_RECORD))){
                    validCount++;
                }
                //Code modified for Case#3070 - Ability to change a funding source - ends
            }
        }
        return validCount;
    }
    
    //Added for Coeus 4.3 enhancement PT ID:2210 - View Protocol History start
    /**
     * Shows the history of the protocol
     */
    public void showProtocolHistory(){
        if(protocolInfo!=null){
            if(protocolInfo.getSequenceNumber()!=1){
                ProtocolHistoryController protHistoryController = new ProtocolHistoryController(protocolInfo.getProtocolNumber());
                protHistoryController.display();
            }else{
                CoeusOptionPane.showWarningDialog(
                        coeusMessageResources.parseMessageKey("protocolHistoryExceptionCode.1001"));
            }
        }
        
    }
    //Added for Coeus 4.3 enhancement PT ID:2210 - View Protocol History end
    //Added for case 2785 - Routing enhancement - start
    /**
     * Get the submission information for the protocol from the database and filters
     * the result with current protocol id and sequence id 
     * 
     * @return ProtocolSubmissionInfoBean
     */
    public ProtocolSubmissionInfoBean getSubmissionInfoForSequence(){
        SubmissionDetailsBean detailsBean = new SubmissionDetailsBean() ;
        detailsBean.setProtocolNumber(protocolId) ;
        detailsBean.setScheduleId(null) ;
        detailsBean.setSequenceNumber(new Integer(sequenceId)) ; 
        Vector vecDetails = getSubmissionDetails(detailsBean);
        ProtocolSubmissionInfoBean protocolSubmissionInfoBean = null;
        boolean protSubmissionDetailsFound = false;
        int maxSubmissionNo = 0;
        ProtocolSubmissionInfoBean finalProtocolSubmissionInfoBean = null;
        if(vecDetails!=null && vecDetails.size()>0){
            Vector vecSubmissionDetails = (Vector)vecDetails.get(0);
            if(vecSubmissionDetails!=null){
                for(int i=0; i<vecSubmissionDetails.size(); i++){
                    protocolSubmissionInfoBean = (ProtocolSubmissionInfoBean)vecSubmissionDetails.get(i);
                    if(protocolSubmissionInfoBean.getProtocolNumber().equals(protocolId) &&
                        protocolSubmissionInfoBean.getSequenceNumber() == sequenceId){
                        if(protocolSubmissionInfoBean.getSubmissionNumber()>maxSubmissionNo){
                            maxSubmissionNo = protocolSubmissionInfoBean.getSubmissionNumber();
                            finalProtocolSubmissionInfoBean = protocolSubmissionInfoBean;
                        }
                         protSubmissionDetailsFound = true;
                        
                    }
                }
            }
        }
        if(protSubmissionDetailsFound){
            return finalProtocolSubmissionInfoBean;
        }else{
            return null;
        } 
    }
    /**
     * Builds the maps for the protocol. Gets the information whether maps are
     * available
     *
     * @returns boolean true if maps exist
     */
    public boolean doSubmitApproveAction() {
        try {
            
            Vector requestParameters = new Vector();
            //Passes Protocol Number, unit number,"S" submit for approve option
            requestParameters.add(protocolInfo.getProtocolNumber());
            requestParameters.add(new Integer(protocolInfo.getSequenceNumber()));
            requestParameters.add(protocolInv.getLeadUnitNumber());
            requestParameters.add("D");
            RequesterBean requesterBean = new RequesterBean();
            requesterBean.setFunctionType(SUBMIT_FOR_APPROVE);
            requesterBean.setDataObjects(requestParameters);
            String connectTo = CoeusGuiConstants.CONNECTION_URL
                    + "/protocolSubSrvlt";
            AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, requesterBean);
            comm.send();
            ResponderBean response = comm.getResponse();
            Vector submitApproveResultSetData = null;
            if(response.isSuccessfulResponse() && response.getDataObjects() != null){
                submitApproveResultSetData = response.getDataObjects();
                if(submitApproveResultSetData != null && submitApproveResultSetData.size() > 0) {
                    if(submitApproveResultSetData.get(0) != null) {
                            if(((Integer) submitApproveResultSetData.get(0)).intValue() > 0) {
                                return true;
                            } 
                    }
                }
            //Code added for Case#2785 - Protocol Routing - starts
            } else if(response.getException() != null && response.getMessage() != null
                    && response.getMessage().length() > 0){
                CoeusOptionPane.showErrorDialog(response.getMessage());
                errMsgDisplayed = true;
            //Code added for Case#2785 - Protocol Routing - ends
            }
        }catch(Exception expec) {
            expec.printStackTrace();
        }
          return false;
    }          
    //Added for case 2785 - Routing enhancement - end
    
    //Added for case#3089 - Special Review Tab made editable in display mode - start
    private boolean hasAdministrativeCorrectionRight(){
        boolean hasAminCorrRight = false;
        RequesterBean request = new RequesterBean();
        request.setFunctionType(HAS_ADMINISTRATIVE_CORRECTION_RIGHT);      
        request.setDataObject(protocolInv.getLeadUnitNumber());
        String connectTo = CoeusGuiConstants.CONNECTION_URL +PROTOCOL_SERVLET;        
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response.isSuccessfulResponse()){
            hasAminCorrRight = ((Boolean)response.getDataObject()).booleanValue();
        }
        //Added for case 3552 - IRB attachments - start
        checkedAdministrativeCorrection = true;
        admistrativeRightPresent = hasAminCorrRight;
        //Added for case 3552 - IRB attachments - end
        return hasAminCorrRight;
    }  
    
    //Added for case#4275 - upload attachments until in agenda - Start
     /**
     * To check Attachment can be done in Display mode for otherAttachments
     * @return admistrativeRightPresent
     */
    private boolean hasAdminCorrectionRight(){
        if(!checkedAdministrativeCorrection){
            admistrativeRightPresent = hasAdministrativeCorrectionRight();
        }
        return admistrativeRightPresent;
    }
    
    //Checks when protocol or Amendment or Renewal is opned in Display mode
    /**
     * To check Attachment can be done in Display mode for Attachments
     * @return attachmentRightPresent
     */
    private boolean hasModifyAttachRights(){
        boolean attachmentRightPresent = false;
        boolean canModifyAttachment = false;
        String isAttachmentMode = "";
        int protocolStatusCode = protocolInfo.getProtocolStatusCode();
        String protocolNumber = protocolInfo.getProtocolNumber();
        if(protocolNumber != null){
            
            //Check for protocol or Amendment or Renewal
            if(( protocolNumber.indexOf('A') > -1 ) ||
                    ( protocolNumber.indexOf('R') > -1 )) {
                HashMap hmEditModules = getModeDetails();
                HashMap checkedSummary = (HashMap)hmEditModules.get("hmEditModules");
                //If Amendement/Renewal then check whether the Attachment mode is selected
                //If selected then go for the attachment rights
                if(!checkedSummary.isEmpty()){
                    isAttachmentMode = (String)checkedSummary.get(UPLOAD_ATTACHMENTS);
                }
                if(isAttachmentMode != null && isAttachmentMode.equals(protocolNumber)){
                    canModifyAttachment = true;
                }
            }else{
                canModifyAttachment = true;
            }
        }
        //Checks if Protocol status is SUBMITTED_TO_IRB, then checks for rights to upload document in Display mode
        // Modified for COEUSQA-2556 : When a protocol is in routing it should not have a status of Submitted to IRB - Start
//        if(canModifyAttachment && (protocolStatusCode == SUBMITTED_TO_IRB)){
        if(canModifyAttachment && (protocolStatusCode == SUBMITTED_TO_IRB || protocolStatusCode == ROUTING_IN_PROGRESS_STATUS)){ // COEUSQA-2556 : End
            RequesterBean request = new RequesterBean();
            request.setId(protocolNumber);
            request.setDataObject(protocolInfo);
            request.setFunctionType(HAS_MODIFY_ATTACHMENTS_RIGHTS);
            String connectTo = CoeusGuiConstants.CONNECTION_URL +PROTOCOL_SERVLET;
            AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
            comm.send();
            ResponderBean response = comm.getResponse();
            if(response.isSuccessfulResponse()){
                attachmentRightPresent = ((Boolean)response.getDataObject()).booleanValue();
            }
        }
        
        checkedAttachmentRights = true;
        return attachmentRightPresent;
    }
    
    /**
     * To check Whether modfication rights is available in ProtocolOtherAttachments
     * @return canModifyAttachment
     */
    private boolean hasModifyAttachmentRights(){
         if(!checkedAttachmentRights){
            canModifyAttachment = hasModifyAttachRights();
        }
        return canModifyAttachment;
    }
    //Case#4275 - End
    
    public boolean isCanModifySpRevInDisplay() {
        return canModifySpRevInDisplay;
    }

    public void setCanModifySpRevInDisplay(boolean canModifySpRevInDisplay) {
        this.canModifySpRevInDisplay = canModifySpRevInDisplay;
    }           
    //Added for case#3089 - Special Review Tab made editable in display mode - end
    
    //Added for case 3552 - IRB attachments - start
    public boolean isSaveSuccessful(){
        return saveSuccessful;
    }
    public void setSaveSuccessful(boolean saveSuccessful){
        this.saveSuccessful = saveSuccessful;
    }
    //Added for case 3552 - IRB attachments - end
    
    private boolean isRoutingDetailsPresent() {
        Vector vecDetails= new Vector() ;
        boolean isPresent = false;
        try {
            if(protocolId == null || protocolId.equals("")){
                return isPresent;
            }
            RoutingBean routingBean = new RoutingBean();
            RequesterBean requester = new RequesterBean();
            //Modified for COEUSQA-2249 : Routing history is lost after an amendment is routed and approved - Start
            //Instead of checking the routing details for the current sequence, check is done for all the sequence
//            routingBean.setModuleCode(7);
//            routingBean.setModuleItemKey(protocolId);
//            routingBean.setModuleItemKeySequence(sequenceId);
//            requester.setFunctionType(GET_NEW_ROUTING_NUMBER) ;
             vecDetails.add(routingBean);
            Vector vecProtocolData = new Vector();
            vecProtocolData.add(MODULE_CODE_INDEX,PROTOCOL_MODULE);
            vecProtocolData.add(MODULE_ITEM_KEY_INDEX,protocolId);
            requester.setFunctionType(GET_ROUTING_SEQUENCE_HISTORY) ;
            requester.setDataObjects(vecProtocolData) ;
            //COEUSQA-2249 : End
            String connectTo =CoeusGuiConstants.CONNECTION_URL
                    + "/RoutingServlet" ;
            AppletServletCommunicator comm
                    = new AppletServletCommunicator(connectTo,requester);
            comm.send();
            ResponderBean responder = comm.getResponse();
            if (responder.isSuccessfulResponse()) {
                //Modified for COEUSQA-2249  : Routing history is lost after an amendment is routed and approved
//                vecDetails = (Vector)responder.getDataObjects();
//                routingBean = (RoutingBean) vecDetails.get(0);
//                if(routingBean != null && routingBean.getRoutingStartDate().length() > 0
//                        && routingBean.getModuleItemKeySequence() == sequenceId){
//                    isPresent = true;
//                }
                Hashtable hmApprovalHistory = (Hashtable)responder.getDataObject();
                if(hmApprovalHistory != null && hmApprovalHistory.size() > 0){
                    isPresent = true;
                }
                //COEUSQA-2249 : End
            }
        } catch(Exception ex) {
            System.out.println("  *** Error in getting submission details ***  ") ;
            ex.printStackTrace() ;
        }
        return isPresent;
    }    
    
    //Case#3091 - IRB - generate a protocol summary pdf  - Start
    /*
     * Method to build file menu with PrintSummary menu item
     */
    private void prepareFilemenu(){
        java.util.Vector fileChildren = new java.util.Vector();
        mnuItmInbox = new CoeusMenuItem("Inbox",null,true,true);
        mnuItmInbox.setMnemonic('I');
        mnuItmClose = new CoeusMenuItem("Close",null,true,true);
        mnuItmClose.setMnemonic('C');
        mnuItmSave = new CoeusMenuItem("Save",null,true,true);
        mnuItmSave.setMnemonic('S');
        mnuItmSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,KeyEvent.CTRL_MASK));
        mnuItmSaveAs = new CoeusMenuItem("Save As",null,true,true);
        mnuItmSaveAs.setMnemonic('A');
        
        mnuItmPrintSummary = new CoeusMenuItem("Print Summary",null,true,true);
        mnuItmPrintSummary.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,KeyEvent.CTRL_MASK));
        mnuItmPrintSummary.setMnemonic('P');
        
        mnuItmChangePassword = new CoeusMenuItem("Change Password",null,true,true);
        mnuItmChangePassword.setMnemonic('h');
        mnuItmPreferences = new CoeusMenuItem("Preferences",null,true,true);
        mnuItmPreferences.setMnemonic('R');
        
        mnuItmDelegations = new CoeusMenuItem("Delegations",null,true,true);
        mnuItmDelegations.setMnemonic('D');
        
        mnuItmExit = new CoeusMenuItem("Exit",null,true,true);
        mnuItmExit.setMnemonic('X');
        
        mnuItmCurrentLock = new CoeusMenuItem("Current Locks",null,true,true);
        mnuItmCurrentLock.setMnemonic('L');
        
        fileChildren.add(mnuItmInbox);
        fileChildren.add(SEPERATOR);
        fileChildren.add(mnuItmSave);
        fileChildren.add(mnuItmSaveAs);
        mnuItmSaveAs.setEnabled(false);
        fileChildren.add(SEPERATOR);
        fileChildren.add(mnuItmClose);
        fileChildren.add(SEPERATOR);
        fileChildren.add(mnuItmPrintSummary);
        fileChildren.add(SEPERATOR);
        fileChildren.add(mnuItmChangePassword);
        
        fileChildren.add(SEPERATOR);
        fileChildren.add(mnuItmCurrentLock);
        fileChildren.add(SEPERATOR);
        
        fileChildren.add(mnuItmDelegations);
        fileChildren.add(SEPERATOR);
        
        fileChildren.add(mnuItmPreferences);
        fileChildren.add(SEPERATOR);
        fileChildren.add(mnuItmExit);
        mnuItmPrintSummary.addActionListener(this);
        mnuItmInbox.addActionListener(this);
        mnuItmSave.addActionListener(this);
        mnuItmClose.addActionListener(this);
        mnuItmChangePassword.addActionListener(this);
        mnuItmCurrentLock.addActionListener(this);
        mnuItmDelegations.addActionListener(this);
        mnuItmPreferences.addActionListener(this);
        mnuItmExit.addActionListener(this);
        mnuFile = new CoeusMenu("File", null, fileChildren, true, true);
        mnuFile.setMnemonic('F');
    }
    
    /*
     * Method to show Print Summary window
     */
    private void showProtocolPrintSummary() {
        ProtocolPrintSummary printNoticeForm = new ProtocolPrintSummary(protocolInfo);
        printNoticeForm.display();
    }
    
    /*
     * Method to load the new file menu with printSummary menuitem
     */
    private void loadMenus() {
        mdiForm.getCoeusMenuBar().remove(0);
        mdiForm.getCoeusMenuBar().add(mnuFile, 0);
    }
    
    /*
     * Method to unload the CoeusAppletMDIForm default file menu
     */
    private void unloadMenus() {
        mdiForm.getCoeusMenuBar().remove(mnuFile);
        mdiForm.getCoeusMenuBar().add(new CoeusFileMenu(mdiForm).getMenu(), 0);
    }
    
    /**
     * This method is used to add custom menu bar to the application
     *  when the internal frame is activated.
     * @param e  InternalFrameEvent which delegates the event. 
     */   
    public void internalFrameActivated(InternalFrameEvent e) {
        super.internalFrameActivated(e);
        loadMenus();
        mdiForm.getCoeusMenuBar().validate();
    }
    
     /**
     * This method is used to remove the internal frame from the active windows
     * list in Window menu item.
     * @param e  InternalFrameEvent which delegates the event.
     */  
    public void internalFrameDeactivated(InternalFrameEvent e) {
        unloadMenus();
        super.internalFrameDeactivated(e);
        mdiForm.getCoeusMenuBar().revalidate();
    }
    
    /*
     * Displays inbox details.
     */
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
    
    /*
     * Displays ChangePassword window
     */
    private void showChangePassword(){
        ChangePassword changePassword = new ChangePassword();
        changePassword.display();
    }
    
    /*
     * Displays Preference window
     */
    private void showPreference(){
        UserPreferencesForm userPreferencesForm = new UserPreferencesForm(mdiForm,true);
        userPreferencesForm.loadUserPreferences(mdiForm.getUserId());
        userPreferencesForm.setUserName(mdiForm.getUserName());
        userPreferencesForm.display();
    }
    
    /*
     * Method used to close the application after confirmation.
     */
    private void exitApplication(){
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
    
    /*
     * Method used to display current locks
     */
    private void showLocksForm() throws edu.mit.coeus.exception.CoeusException{
        CurrentLockForm currentLockForm = new CurrentLockForm(mdiForm,true);
        currentLockForm.display();
    }
    
    /*
     * Method used to display Delegation window
     */
    private void showUserDelegation() {
        UserDelegationForm userDelegationForm = new UserDelegationForm(mdiForm,true);
        userDelegationForm.display();
    }
    //Case#3091 - end
    // COEUSDEV-202: Error about mandatory questionnaire - Does not identify the questionnaire - Start
    // 4272: Maintain History of Questionnaires - Start
//    private int validateQuestionnaireCompleted() {
    private Vector validateQuestionnaireCompleted() {
//        int success = 0;
        // COEUSDEV-202: Error about mandatory questionnaire - Does not identify the questionnaire - End
        //Added for COEUSDEV-86 : Questionnaire for a Submission - Start
        int subModuleItemCode = 0; //Normal Protocol
        if(protocolId != null && (protocolId.indexOf("A") > -1 || protocolId.indexOf("R") > -1)){
            subModuleItemCode = 1; // Amendment/Renewal
        }
        //COEUSDEV-86 : END
        Vector vecUnfilledQnr = new Vector();
        RequesterBean request = new RequesterBean();
        request.setFunctionType(CHECK_QUESTIONNAIRE_COMPLETED);
        CoeusVector proposalData = new CoeusVector();
        proposalData.add(0, protocolId);
        proposalData.add(1, new Integer(7));
        //Modified for COEUSDEV-86 : Questionnaire for a Submission - Start
//        proposalData.add(2,"0");
        proposalData.add(2, String.valueOf(subModuleItemCode));
        //COEUSDEV-86 : END
        proposalData.add(3, new Integer(sequenceId));
        request.setDataObjects(proposalData);
        String connectTo = CoeusGuiConstants.CONNECTION_URL + "/questionnaireServlet";
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        
        if (response != null){
            if (response.isSuccessfulResponse()){
                // COEUSDEV-202: Error about mandatory questionnaire - Does not identify the questionnaire
//                success = ((Integer) response.getDataObject()).intValue();
                vecUnfilledQnr = (Vector) response.getDataObject();
            }
        }
        // COEUSDEV-202: Error about mandatory questionnaire - Does not identify the questionnaire
//        return success;
        return vecUnfilledQnr;
    }
    // 4272: Maintain History of Questionnaires - End
    
    //Added for the case# COEUSQA-1403 Implement validation based on rules in protocols-start
    /**
     *This method returns a vector of broken rules beans if any else it returns null
     */
    private Vector getBrokenRulesData() throws Exception {
        
        //int budgetVersion = checkBudgetFinalVersion(validateFinal);
        Vector inputVector= new Vector();
        Vector dataObjects = null;
        String leadUnitNumber = protocolInv.getLeadUnitNumber();  
        inputVector.add(new Integer(ModuleConstants.PROTOCOL_MODULE_CODE));//module code
        inputVector.add(protocolId);//module item key
        inputVector.add(sequenceId);//budgetversion passed as moduleitemkeysequence
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
    public boolean confirmSubmitForApproval() {
        
        int option = CoeusOptionPane.showQuestionDialog(coeusMessageResources.parseMessageKey(CONFIRM_SUBMIT_APPROVAL),
                                                        CoeusOptionPane.OPTION_YES_NO,
                                                        CoeusOptionPane.DEFAULT_YES);     
        if(option == JOptionPane.YES_OPTION) {
            return true;
        }
        return false;
    }

    /*
     * Checking if Mandatory Data , Investigators and Area of Research present
     * @ param ProtocolInfoBean which contains master data
     * @ return boolean dataNotPresent true if Investigator or Area of Research is not found
     */
    private boolean checkMandatoryData(ProtocolInfoBean protocolInfo){
        boolean dataNotPresent = false;
        if(protocolInfo.getInvestigators() == null || protocolInfo.getAreaOfResearch() == null){
            dataNotPresent = true;
        }
        return dataNotPresent;
    }
    //Added for the case# COEUSQA-1403 Implement validation based on rules in protocols-end
    
    
    //COEUSQA-2602-Remove checkboxes from Renewal Summary screen - start
    
    /* Checking whether the protocol action type code is 'Renewal' or not.
     * @ param Sting protocolNumber
     * @ return boolean isRenewal, true if protocol is in 'Renewal' action type code
     */
    public boolean isRenewal(String protocolNumber) throws CoeusException{
        boolean isRenewal = false;
        
        String connectTo = CoeusGuiConstants.CONNECTION_URL+ PROTOCOL_SERVLET;
                RequesterBean requester = new RequesterBean();
        
        requester = new RequesterBean();
        requester.setFunctionType(CHECK_IS_RENEWAL);               
        requester.setDataObject(protocolNumber);
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo,requester);
        comm.send();    
        ResponderBean responder = comm.getResponse();
        responder = comm.getResponse();
        if(!responder.isSuccessfulResponse()){
            throw new CoeusException(responder.getMessage(),0);
        }else{
            isRenewal = ((Boolean)responder.getDataObject()).booleanValue();
        }
        return isRenewal;
    }
    
    //COEUSQA-2602-Remove checkboxes from Renewal Summary screen - start

    
    /**
     * This method will fetch the basic details for a protocol
     *
     * @param protocolId Protocol Id
     * @return protocolInfoBean  - ProtocolInfoBean
     */
    private ProtocolInfoBean getProtocolinfo(String protocolId) throws Exception {
        ProtocolInfoBean protocolInfoBean = null;
        String connectTo = CoeusGuiConstants.CONNECTION_URL+ PROTOCOL_SERVLET;
        RequesterBean request = new RequesterBean();
        request.setFunctionType(GET_PROTOCOL_DETAILS);
        request.setId(protocolId);
        request.setDataObject(protocolId);
        AppletServletCommunicator comm
                = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response == null) {
            response = new ResponderBean();
            response.setResponseStatus(false);
            response.setMessage(coeusMessageResources.parseMessageKey(
                    "server_exceptionCode.1000"));
        }
        if (response.isSuccessfulResponse()) {
            protocolInfoBean = (ProtocolInfoBean)response.getDataObject();
        }
        return protocolInfoBean;
    }
    
    // Added with CoeusQA2313: Completion of Questionnaire for Submission
    /* Method to fetch the applicable questionnaire data from the original protocol
     * @return HashMap of questionnaire id - questionnaire label
     *
     */
    private HashMap getApplicableQuestionnaires() throws CoeusException{
        
        
        HashMap hmQnrData = new HashMap();
        QuestionnaireAnswerHeaderBean questionnaireModuleObject = getQuestionnaireHeaderInfo();
//        questionnaireModuleObject.setModuleSubItemCode(0);
        RequesterBean request = new RequesterBean();
        ResponderBean response = null;
        request.setFunctionType(GET_QUESTIONS_MODE);
        CoeusVector cvQuestionnaireData = new CoeusVector();
        cvQuestionnaireData.add(questionnaireModuleObject);
        request.setDataObject(cvQuestionnaireData);
        String connectTo = CoeusGuiConstants.CONNECTION_URL + QUESTIONNAIRE_SERVLET;
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
        comm.send();
        response = comm.getResponse();
        if(response != null && response.hasResponse()){
            CoeusVector cvApplicableQuestionnaire = (CoeusVector)response.getDataObject();
            if(cvApplicableQuestionnaire!=null && !cvApplicableQuestionnaire.isEmpty()){
                for(Object obj: cvApplicableQuestionnaire){
                    questionnaireModuleObject = (QuestionnaireAnswerHeaderBean)obj;
                    if(questionnaireModuleObject.getApplicableSubmoduleCode() == 0 
                            && "Y".equals(questionnaireModuleObject.getQuestionnaireCompletionFlag())){
                        hmQnrData.put(String.valueOf(questionnaireModuleObject.getQuestionnaireId()),questionnaireModuleObject.getLabel());
                    }
                }
            }
        }
        return hmQnrData;
    }
    
    /**
     * Method to update qnr answers to new version
     *if protocol is under revision
     */
    private void checkQuestionnaireRevision() throws CoeusException{
        if(isQuestionnaireNeedsRevision()){
            updateQuestionnaireForRevision();
        }
    }
    
    /**
     * Method to check if a qnr needs a revision or not.
     */
    private boolean isQuestionnaireNeedsRevision() {
        
        int  statusCode = protocolInfo.getProtocolStatusCode();
        boolean canCopy = false;
        if(functionType != DISPLAY_MODE || lockExist){
            if(statusCode == 102 //SMR
                    || statusCode == 104// SRR
                    //COEUSQA:3315 - IACUC Protocol Actions Blanking Completed Questionnaires - Start
                    || statusCode == 103  // Deferred
                    || statusCode == 200  // Active - Open to Enrollment
                    || statusCode == 201  // Active - Closed to Enrollment
                    || statusCode == 202  // Active - Data Analysis Only
                    || statusCode == 203  // Exempt
                    || statusCode == 302  // Suspended by Investigator
                    || statusCode == 305  // Expired
                    || statusCode == 306  // Disapproved                   
                    || statusCode == 307  // Terminated by IRB
                    || statusCode == 308  // Suspended by IRB
                    || statusCode == 311  // Suspended by DSMB
                    || statusCode == 313  // Abandoned
                    //COEUSQA:3315 - End
                    || statusCode == 304) {// Withdrawn
                canCopy = true;
            }
        }
        return canCopy;
    }
    
    /**
     * Method to update qnr to new version if the protocol undergoes a revision
     */
    private void updateQuestionnaireForRevision() throws CoeusException{
        QuestionnaireAnswerHeaderBean questionnaireModuleObject = getQuestionnaireHeaderInfo();
        RequesterBean request = new RequesterBean();
        request.setDataObject(questionnaireModuleObject);
        request.setFunctionType(COPY_QUESTIONNAIRE_FOR_REVISIONS);
        String connectTo = CoeusGuiConstants.CONNECTION_URL + QUESTIONNAIRE_SERVLET;
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
        comm.send();
    }
    
    
    private QuestionnaireAnswerHeaderBean getQuestionnaireHeaderInfo(){
        QuestionnaireAnswerHeaderBean questionnaireModuleObject = new QuestionnaireAnswerHeaderBean();
        questionnaireModuleObject.setModuleItemCode(ModuleConstants.PROTOCOL_MODULE_CODE);
        questionnaireModuleObject.setModuleItemKey(protocolId);
        questionnaireModuleObject.setModuleItemDescription(CoeusGuiConstants.PROTOCOL_MODULE);
        questionnaireModuleObject.setModuleSubItemCode(setSubModuleItemCode());
        questionnaireModuleObject.setModuleSubItemKey(""+sequenceId);
        questionnaireModuleObject.setModuleSubItemDescription(CoeusGuiConstants.PROTOCOL_MODULE);
        return questionnaireModuleObject;
    }
    
    
    /*New method setSubModuleItemCode added to set the Protocol requested sub Module item code       
       * either its Original protocol request or Amendment/renewal protocol request
       *        
       */
      private int setSubModuleItemCode() {
        int subModuleItemCode = 0; //Normal Protocol
        if(protocolId != null 
                && (protocolId.indexOf("A") > -1 
                    || protocolId.indexOf("R") > -1)){
            subModuleItemCode = 1; // Amendment/Renewal
        }
        return subModuleItemCode;
      }
    // CoeusQA2313: Completion of Questionnaire for Submission -End
      
      //COEUSQA-1433 - Allow Recall from Routing - Start
      /*The method check if the protocol is locked by an other user
       * Returns lockAvailable
       */
      public boolean lockProtocolRecall() {
          boolean lockAvailable = false;
          boolean userRestrictedView = false;
          if(functionType == DISPLAY_MODE) {
              String connectTo = CoeusGuiConstants.CONNECTION_URL + LOCKING_SERVLET;
              RequesterBean request = new RequesterBean();
              request.setFunctionType(PROTOCOL_RECALL_LOCK_CHECK);
              request.setId(protocolId);
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
                /* if(!lockAvailable){
                      CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey("protocolDetForm_exceptionCode.1019"));
                  }*/
                  String displayingLockName = getParameterValue();
                  
                  if(displayingLockName != null && "Y".equalsIgnoreCase(displayingLockName.trim())){
                      userRestrictedView = true;
                  }
                  //If parameter DISPLAY_LOCKNAME_FOR_ROUTING is set as 'S' then need to provide the locked user name
                  if(userRestrictedView){
                      if(!lockAvailable && response.getMessage()!= null){
                          String message = response.getMessage();
                          CoeusOptionPane.showErrorDialog(message);
                      }
                  }
                  //If parameter DISPLAY_LOCKNAME_FOR_ROUTING is set as 'N' then need to hide the locked user name
                  if(!userRestrictedView){
                      if(!lockAvailable){
                          CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey("locking_exceptionCode.1021")+" "+"Protocol Routing "+ protocolId);
                      }
                  }
                  //Added for COEUSDEV-1075 : Locking messages inconsistency between Lite and Premium - end
              }
          }
          return lockAvailable;
      }
      //COEUSQA-1433 - Allow Recall from Routing - End
      
      //COEUSQA:2653 - Add Protocols to Medusa - Start
      /**
       * Method to open the Medusa screen for IRB protocol Number
       * @param protocolNumber
       */
      private void  showMedusaWindow(String protocolNumber){
          try{
              ProposalAwardHierarchyLinkBean linkBean;
              MedusaDetailForm medusaDetailform;
              
              linkBean = new ProposalAwardHierarchyLinkBean();
              linkBean.setIrbProtocolNumber(protocolNumber);
              linkBean.setBaseType(CoeusConstants.IRB_PROTOCOL);
              if( ( medusaDetailform = (MedusaDetailForm)mdiForm.getFrame(
                      CoeusGuiConstants.MEDUSA_BASE_FRAME_TITLE))!= null ){
                  if( medusaDetailform.isIcon() ){
                      medusaDetailform.setIcon(false);
                  }
                  medusaDetailform.setSelectedNodeId(protocolNumber);
                  medusaDetailform.setSelected( true );
                  return;
              }
              medusaDetailform = new MedusaDetailForm(mdiForm,linkBean);
              medusaDetailform.setVisible(true);
              
              
          }catch(Exception exception){
              exception.printStackTrace();
          }
      }
      //COEUSQA:2653 - End
      //code added to send coi notification to protocol persons..start
      private void showPersonNotification() {
          HashMap hmParameter = new HashMap();
          String protoNumber = protocolInfo.getProtocolNumber();
          int moduleCode=7;
          hmParameter.put("PROP_NUMBER",protoNumber);
          hmParameter.put("SEQUENCE_NUMBER",sequenceId);
          hmParameter.put("MODULE_CODE",moduleCode);
          edu.mit.coeus.iacuc.gui.ProtocolNotificationForm  personNotificationForm=new
                  edu.mit.coeus.iacuc.gui.ProtocolNotificationForm(protoNumber,sequenceId,moduleCode);
      }
      //code added to send coi notification to protocol persons..end
   
   private int checkSendNotificationEnabled() {
       int checkFlag=0;
       String connectTo = CoeusGuiConstants.CONNECTION_URL + PROTOCOL_SERVLET;
       RequesterBean request = new RequesterBean();
       request.setId(protocolId);
       request.setFunctionType(PROTOCOL_SEND_NOTIFICATION);
       AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
       comm.send();
       ResponderBean response = comm.getResponse();
       if(response==null){
           CoeusOptionPane.showErrorDialog("Could not contact server");
       } else{
           if(response.getDataObject()!= null){
               checkFlag = Integer.parseInt(response.getDataObject().toString());
               
           }
           
       }
       return checkFlag;
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
        requester.setDataObject(GET_PARAMETER_VALUE);
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
} 


