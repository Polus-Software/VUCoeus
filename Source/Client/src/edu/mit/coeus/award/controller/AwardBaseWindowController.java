/*
 * AwardBaseWindowController.java
 *
 * Created on March 18, 2004, 3:47 PM
 */
/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.award.controller;

/**
 *
 * @author  sharathk
 * PMD check performed and removed unused variables and imports
 * on 30 Apr 09 by keerthyjayaraj
 */
import edu.mit.coeus.award.AwardConstants;
import edu.mit.coeus.instprop.bean.InstituteProposalInvestigatorBean;
import edu.mit.coeus.mail.controller.ActionValidityChecking;
import edu.mit.coeus.propdev.bean.InvestigatorCreditSplitBean;
import edu.mit.coeus.utils.customelements.CustomElementsForm;
//import edu.mit.coeus.user.gui.UserDelegationForm;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.text.*;
import java.beans.*;
import javax.swing.*;
import javax.swing.event.*;

import edu.mit.coeus.award.bean.*;
import edu.mit.coeus.award.gui.*;
import edu.mit.coeus.bean.KeyPersonUnitBean;
import edu.mit.coeus.gui.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.brokers.*;
import edu.mit.coeus.exception.*;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.utils.investigator.*;
import edu.mit.coeus.search.gui.FundingProposalSearch;
import edu.mit.coeus.gui.event.*;
import edu.mit.coeus.subcontract.bean.SubContractFundingSourceBean;
import edu.mit.coeus.propdev.gui.MedusaDetailForm;
import edu.mit.coeus.propdev.gui.ProposalNotepadForm;
import edu.mit.coeus.propdev.bean.ProposalAwardHierarchyLinkBean;
import edu.mit.coeus.bean.ValidRatesBean;
import edu.mit.coeus.propdev.gui.InboxDetailForm;
import edu.mit.coeus.routing.gui.RoutingForm;
import edu.mit.coeus.user.gui.UserPreferencesForm;
import edu.mit.coeus.utils.investigator.invCreditSplit.InvCreditSplitObject;
import edu.mit.coeus.utils.investigator.invCreditSplit.InvestigatorCreditSplitController;
import edu.mit.coeus.utils.investigator.invUnitAdminType.InvestigatorUnitAdminTypeController;
import edu.ucsd.coeus.personalization.controller.AbstractController;

// JM 5-22-2013 centers
import edu.vanderbilt.coeus.award.controller.AwardCentersController;
// JM END

//JM 9-26-2014 award restrictions
import edu.vanderbilt.coeus.award.controller.AwardRestrictionsController;
//JM END

public class AwardBaseWindowController extends AwardController implements VetoableChangeListener,
ActionListener, BeanUpdatedListener, ChangeListener{
    
    private AwardBaseWindow awardBaseWindow;
    //private AwardBaseBean awardBaseBean;
    
    private JTabbedPane tbdPnAward;
    
    private Controller controller[];
    
    private MedusaDetailForm medusaDetailform;
    
    private AwardDetailController awardDetailController;
    
    private OtherHeaderController otherHeaderController;
    
    private MoneyAndEndDatesController moneyAndEndDatesController;
    
    private AwardReportsController awardReportsController;
    
    private AwardTermsController awardTermsController;
    
    //For the Coeus Enhancement case:#1799 start step:1
    private AwardSpecialReviewController awardSpecialReviewController; 
    //End Coeus Enhancement case:#1799 step:1
    
    private AwardInvestigatorController awardInvestigatorController;
    
    // 3823: Key Person Records Needed in Inst Proposal and Award
    private AwardKeyPersonController awardKeyPersonController;    
    
    private SubContractController subContractController;
    
    private AwardCommentsController awardCommentsController;
    
    private AwardContactsController awardContactsController;
    
    private AwardOtherController awardOtherController;
    
    // Added for case# 2800 - Award  Attachments - Start
    private AwardDocumentController awardDocumentController;
    // Added for case# 2800 - Award  Attachments - End
    
    // JM 11-21-2011 added Award Centers tab
    private AwardCentersController awardCentersController;
    // END
    
    // JM 9-26-2014 added Award Restrictions tab
    private AwardRestrictionsController awardRestrictionsController;
    // END
    
    // JM 6-21-2012 static variable to hold HOLD status
    private static final int HOLD_STATUS = 7;
    private static final String HOLD_STRING = "Hold";
    // JM END
    
    //Dialogs
    private ApprForeignTripController apprForeignTripController ;
    private AwardCloseoutController awardCloseoutController;
    
    private CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();
    
    private CoeusMessageResources coeusMessageResources;
    
    private QueryEngine queryEngine;
    
    private AwardBean awardBean;
    
    private String titleStart;
    private String title;
    
    private String proposalNumber;
     
    //for print modification in money and end dates
    private String transactionId = null;   
    
    private boolean openedFromEdiTransaction;
    
    private boolean canDisplay;
    
    private int selectedTabIndex;
    private boolean displayOtherHeader;
    
    private boolean closed = false;
    
    private boolean maintainReporting;
    
    /*Bug Fix:1410 - to fire the terms tab in the display mode and display the data*/
    private boolean nextPreviousAction;
    
    /**
     *holds the count of saves done for an award once opened.
     *used to set AC types for award amount info when save clicked for the
     *first time after opening award.
     */
    private int saveCount, saveMoneyAndEndDatesCount;
    
    private static final String FUNC_NOT_IMPL = "Functionality not implemented";
    
    /**
     * Holds Investigator Sequence Numbers from Server so as to modify again when its sent back to server.
     */
//    private int invgtrSeqNoFrmSrv;
    
    private static final String AWARD_DETAIL = "Award Detail";
    private static final String OTHER_HEADER = "Other Header";
    private static final String MONEY_END_DATES = "Money and End Dates";
    private static final String AWARD_CONTACTS = "Contacts";
    private static final String AWARD_REPORTS = "Reports";
    private static final String AWARD_TERMS = "Terms";
    
    //For the Coeus Enhancement case:#1799 start step:2
    private static final String AWARD_SPECIAL_REVIEW = "Special Review";
    //End Coeus Enhancement case:#1799 step:2
    
    private static final String INVESTIGATOR = "Investigator";    
    // 3823: Key Person Records Needed in Inst Proposal and Award
    private static final String KEY_PERSON = "Key Person";    
    private static final String COMMENTS = "Comments";
    private static final String SUB_CONTRACT = "Subcontracts";
    private static final String OTHER = "Other";
    
    // JM 11-21-2011 added for Centers
    private static final String CENTERS = "Center Numbers";
    
    // JM 9-26-2014 added for Restrictions
    private static final String RESTRICTIONS = "Restrictions";
    
    // Added for case# 2800 - Award  Attachments - Start
    private static final String AWARD_DOCUMENTS = "Attachments";
    // Added for case# 2800 - Award  Attachments - End
    
    private static final int AWARD_DETAIL_TAB_INDEX = 0;
    private static final int OTHER_HEADER_TAB_INDEX = 1;
    private static final int MONEY_END_DATES_TAB_INDEX = 2;
    private static final int AWARD_CONTACTS_TAB_INDEX = 3;
    private static final int AWARD_REPORTS_TAB_INDEX = 4;
    private static final int AWARD_TERMS_TAB_INDEX = 5;
    
    //For the Coeus Enhancement case:#1799 start step:3 
    private static final int SPECIAL_REVIEW_INDEX = 6;
    private static final int INVESTIGATOR_TAB_INDEX = 7;
    // 3823: Key Person Records Needed in Inst Proposal and Award - Start
    // Added a new Tab 'Key Person' at the 8th index
//    private static final int SUB_CONTRACT_TAB_INDEX = 9;
//    private static final int COMMENTS_TAB_INDEX = 8;
//    // Modified and Added for case# 2800 - Award  Attachments - Start
//    private static final int AWARD_DOCUMENT_TAB_INDEX = 10;
//    private static final int OTHER_TAB_INDEX = 11;
//    // Modified and Added for case# 2800 - Award  Attachments - End
//    //End Coeus Enhancement case:#1799 step:3
    
    // JM 11-22-2011 tabs reordered
    // JM 9-26-2014 restrictions tab added
    private static final int COMMENTS_TAB_INDEX = 8;
    private static final int SUB_CONTRACT_TAB_INDEX = 9;
    private static final int AWARD_DOCUMENT_TAB_INDEX = 10;
    private static final int RESTRICTIONS_TAB_INDEX = 11;
    private static final int CENTERS_TAB_INDEX = 12;    
    private static final int OTHER_TAB_INDEX = 13;
    private static final int TOTAL_NUMBER_OF_AWARD_TABS = 13;
    // JM END
    // 3823: Key Person Records Needed in Inst Proposal and Award - End
    
    //constants for funding proposal - START
    private static final String PROPOSAL_NUMBER = "PROPOSAL_NUMBER";
    private static final String SEQUENCE_NUMBER = "SEQUENCE_NUMBER";
    private static final String TOTAL_DIRECT_COST_TOTAL = "TOTAL_DIRECT_COST_TOTAL";
    private static final String TOTAL_INDIRECT_COST_TOTAL = "TOTAL_INDIRECT_COST_TOTAL";
    private static final String REQUESTED_START_DATE_TOTAL = "REQUESTED_START_DATE_TOTAL";
    private static final String REQUESTED_END_DATE_TOTAL = "REQUESTED_END_DATE_TOTAL";
    private static final String STATUS_CODE = "STATUS_CODE";
    private static final String PROPOSAL_TYPE_DESC = "PROPOSAL_TYPE_DESC";
    //constants for funding proposal - END
    
    private static final String PROPOSAL_SEARCH = "proposalSearch";
    
    private static final char SAVE_AWARD = 'G';
    private static final char GET_AWARD_DETAILS = 'C';
    
    private static final String SERVLET = "/AwardMaintenanceServlet";
    
    /** Do you want to save changes ? */
    private static final String SAVE_CHANGES = "award_exceptionCode.1004";
    
    /** Please select award type first */
    private static final String SELECT_AWARD_TYPE_FIRST = "award_exceptionCode.1005";
    
    private static final String MEDUSA_SAVE_CONFIRMATION = "awardMedusa_exceptionCode.1551";
    
    private static final String NOTEPAD_SAVE_CONFIRMATION = "awardNotepad_exceptionCode.1552";
    
    private static final String OPEN_CS_2_ENTER_DEST_ACCOUNT = "You must open the Cost Sharing window to enter destination account before saving the current award.";
    
    private static final String OPEN_IDC_2_ENTER_START_DATE = "You must open the Indirect Cost window to enter Start Date before saving the current award.";
    
    private static final String FINC_INTST_NOT_COMP_HOLD = " has financial interest \n disclosures associated with it which are not complete. \n" +
    "This Award's status should be set to Hold. \n Do you want to set the award on hold?";
    
    //Award Budget Enhancment Start 1
//    private static final String AWARD_BUDGET_SAVE_CONFIRMATION = "awardBudgetSummary_exceptionCode.2016";
    private static final String ENTER_OBL_EFF_DATE = "awardBudgetSummary_exceptionCode.2032";
    private static final String ENTER_OBL_EXP_DATE = "awardBudgetSummary_exceptionCode.2033";
//    private static final String ENTER_ACCOUNT_NO = "awardBudgetSummary_exceptionCode.2034";
    //Award Budget Enhancment End 1
    
    // Case Id 1822 Award F&A Enhancement Start1
    private static final String BEGIN_DATE_FOR_AWARD ="beginDate_exceptionCode.2003";
    private static final String BEGIN_DATE_NOT_LATER_THAN_EXPIRATION_DATE="beginDateLater_exceptionCode.2004";
    private static final String ANTICIPATED_TOTAL_ZERO ="anticipatedTotalZero_exceptionCode.2005";
//    private static final String AWARD_SAVE_BEFORE_FNA ="awardSave_exceptionCode.2006";
    // Case Id 1822 Award F&A Enhancement End 1
   
     //AWARD ROUTING ENHANCEMENT STARTS
    private boolean routing=false;
    private static final String SUBMISSION_SERVLET="/AwardSubmissionServlet";
    private static final char CHECK_USER_HAS_DOC_ROUTE_RIGHT='F';
    //AWARD ROUTING ENHANCEMENT ENDS
    
    
    /** The Award should be saved before launching Award Closeout.\n Do you want to save now?
     */
//    private static final String AWARD_CLOSEOUT = "awardCloseout_exceptionCode.1561";
    
//    private static final String MONEY_AND_END_DATES_REPORT_SAVE_CONFIRMATION ="awardMoneyAndEndDates_exceptionCode.1174";
    private static final String NO_TRANSACTION_ID = "awardMoneyAndEndDates_exceptionCode.1175";    
    
    private DateUtils dateUtils = new DateUtils();
    
    private SimpleDateFormat simpleDateFormat;
    
    private static final String SIMPLE_DATE_FORMAT = "MM/dd/yyyy";
    
    private static final String DATE_SEPARATERS = "-:/.,|";

    private ChangePassword changePassword;
    
    private UserPreferencesForm userPreferencesForm;
    //Added for Case#3682 - Enhancements related to Delegations - Start
//    private UserDelegationForm userDelegationForm;
    //Added for Case#3682 - Enhancements related to Delegations - End

    //Bug Fix:Performance Issue (Out of memory) Start 1 
    private Container awardBaseContainer;
    private JPanel basePanel;
    private JScrollPane scrPnOtherHeader = null;
    private JScrollPane scrPnSubContract = null;
    private JScrollPane scrPnComments = null;
    
    //Added for the Coeus Enhancement case:#1799 start step:4 
    private JScrollPane scrPnSpecialReview = null;
    //End Coeus Enhancement case:#1799  step:4 
    
    private JScrollPane scrPnInvestigator = null;
    // 3823: Key Person Records Needed in Inst Proposal and Award
    private JScrollPane scrPnKeyPerson = null;
    private JScrollPane scrPnTerms = null;
    private JScrollPane scrPnContacts = null;
    private JScrollPane scrPnOther = null;
    
    private JScrollPane scrPnDetails = null;
    private JScrollPane scrPnReport = null;
    private JScrollPane scrPnMoneyEndDates = null;
    
    // Added for case# 2800 - Award  Attachments - Start
    private JScrollPane scrPnAwardDocuments = null;
    // Added for case# 2800 - Award  Attachments - End
    
    //Bug Fix:Performance Issue (Out of memory) End 1
    
    // JM 9-26-2014 added for Restrictions
    private JScrollPane scrPnAwardRestrictions = null;
    
    // JM 11-21-2011 added for Centers
    private JScrollPane scrPnAwardCenters = null;
    
    //Added for the Coeus Enhancement case:#1885
    private static final char GET_LOCK_FOR_AWARD = 'z';
    //Case :#3149 � Tabbing between fields does not work on others tabs - Start
    private int count = 1;
    //Case :#3149 - End
    
    //rdias UCSD Begin - Reference to UCSD's personalization engine
    AbstractController persnref = null;
    //rdias UCSD End - Reference to UCSD's personalization engine
    
    private static final String MSGKEY_SAVE_AWARD_SEQUENCE = "award_exceptionCode.1012";//COEUSDEV-215
    
    //Added for COEUSDEV-416 : Award Sync to Children - Display proper error message when not syncing because the award is not saved - Start
    private static final String ADD_REPORT_AND_SYNC = "awardReportSync.addReport_exceptionCode.1000";
    private static final String MODIFY_REPORT_AND_SYNC = "awardReportSync.modifyReport_exceptionCode.1001";
    //Added for COEUSDEV-416 : Award Sync to Children - Display proper error message when not syncing because the award is not saved - End
    
    //COEUSQA-1477 Dates in Search Results - Start
    private static final String DATE_FORMAT_DELIMITER = "/";
    private static final String DATE_FORMAT_USER_DELIMITER = "-"; 
    private static final String DATE_FORMAT_YEAR_DELIMITER = "y";
    private static final String DATE_FORMAT_MONTH_DELIMITER = "m";
    private static final String DATE_FORMAT_DATE_DELIMITER = "d";
    //COEUSQA-1477 Dates in Search Results - End
    // Added for COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split Infrastructure project - Start
    private boolean isCreditSplitSaveRequired;
    // Added for COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split Infrastructure project - End

    public AwardBaseWindowController(String title, char functionType, AwardBaseBean awardBaseBean) {
        this(title, functionType, awardBaseBean, null);
    }
    
    /** Creates a new instance of AwardBaseWindowController
     * @param title title.
     * @param functionType functiontype
     * @param awardBaseBean award base bean.
     * @param awardHierarchyBean award Hierarchy bean.
     */
    public AwardBaseWindowController(String title, final char functionType,final AwardBaseBean bean, final AwardHierarchyBean awardHierarchyBean) {
        super(bean);
        //routing=isRouted;
        simpleDateFormat = new SimpleDateFormat(SIMPLE_DATE_FORMAT);
        if(bean != null) {
            this.awardBaseBean = bean;
        }
        //rdias UCSD Begin  - Set reference to UCSD's personalization engine
        persnref = AbstractController.getPersonalizationControllerRef();
        //rdias UCSD End - Set reference to UCSD's personalization engine
        this.titleStart = title;
        setFunctionType(functionType);
        
        queryEngine = QueryEngine.getInstance();
        
        awardBaseWindow = new AwardBaseWindow(title, mdiForm);
        awardBaseWindow.setAwardBaseBean(awardBaseBean);
        tbdPnAward = new CoeusTabbedPane(CoeusTabbedPane.CTRL_T);
        
        //Runnable fetchData = new Runnable() {
        
        //    public void run() {
        blockEvents(true);
        if(!fetchData(getFunctionType(), awardHierarchyBean)) {
            canDisplay = false;
            return ;
        }else {
            //Get Data From server and initialize components.
            CoeusVector cvCustomData;
            try{
                cvCustomData = queryEngine.getDetails(queryKey, AwardCustomDataBean.class);
                // 3823: Key Person Records Needed in Inst Proposal and Award - Start
//                //For the Coeus Enhancement case:#1799 start step:5
//                //int controllers = 9;
//                //                int controllers = 10;
//                int controllers = 11;// Modified for case# 2800 - Award  Attachments - End
//                //End Coeus Enhancement case:#1799 End step:5
                int tabCount = TOTAL_NUMBER_OF_AWARD_TABS;
                if(cvCustomData != null && cvCustomData.size() > 0) {
                    //display other tab
//                    controllers = controllers + 1;
                    tabCount++;
                }
//                controller = new Controller[controllers];
                controller =  new Controller[tabCount];
                // 3823: Key Person Records Needed in Inst Proposal and Award - End
                canDisplay  = true;
                if(awardDetailController == null) {
                    loadTab(AWARD_DETAIL_TAB_INDEX);
                }
                registerComponents();
                awardDetailController.prepareQueryKey(awardBaseBean);
                awardDetailController.initComponents();
                awardDetailController.registerComponents();
                awardDetailController.setFunctionType(getFunctionType());
                awardDetailController.setFormData(awardBaseBean);
                //rdias - UCSD's coeus personalization - Begin
                persnref.customize_module(this.getControlledUI(),this.getControlledUI(), this,
                		CoeusGuiConstants.AWARD_FRAME_TITLE); //This will overwrite menus and toolbar for this module
                persnref.customize_Form(awardDetailController.getControlledUI(),
                		CoeusGuiConstants.AWARD_FRAME_TITLE);//Overwrite comp on detail form
                //rdias - UCSD's coeus personalization - End
            }catch (CoeusException coeusException) {
                coeusException.printStackTrace();
                canDisplay = false;
            }
        }
        blockEvents(false);
        //    }//End Run
        //}; //End fetch Data Thread
        
        if(functionType == NEW_AWARD){
            //Don't run as thread as mit award number has to be got from server.
            //coz in MDI put frame we use mit award number as reference. which
            //will have to be modified later if we use threads.
            //fetchData.run();
        }else {
            //System.out.println(SwingUtilities.isEventDispatchThread());
            //SwingUtilities.invokeLater(fetchData);
            //COEUSDEV-948 - Remove copy of Special Reviews from copy of Parent - Start
            if(functionType == NEW_CHILD_COPIED) {
                CoeusVector cvSpecialReview = null;
                try {
                    cvSpecialReview = queryEngine.getDetails(queryKey, AwardSpecialReviewBean.class);
                } catch (CoeusException ce) {
                    ce.printStackTrace();
                }
                AwardSpecialReviewBean awardSpecialReviewBean;
                if(cvSpecialReview != null && !cvSpecialReview.isEmpty()) {
                    for(Object specialReview : cvSpecialReview){
                        awardSpecialReviewBean = (AwardSpecialReviewBean)specialReview;
                        queryEngine.removeData(queryKey,awardSpecialReviewBean);
                    }
                }
            }
            //COEUSDEV-948 - End            
        }
        
        //awardBaseWindow.setTitle(title);
        coeusMessageResources = CoeusMessageResources.getInstance();
        
        //registerComponents();
        initComponents();
        
    }
    
    private boolean fetchData(char functionType, AwardHierarchyBean awardHierarchyBean) {
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setFunctionType(GET_AWARD_DETAILS);
        //if(functionType == CORRECT_AWARD) {
        //    requesterBean.setDataObject(awardBaseBean.getMitAwardNumber());
        //}
        
        AwardBean awardBeanToServer = new AwardBean();
        if(awardBaseBean != null) {
            awardBeanToServer.setMitAwardNumber(awardBaseBean.getMitAwardNumber());
            awardBeanToServer.setSequenceNumber(awardBaseBean.getSequenceNumber());
        }
        
        //System.out.println("Mode : "+functionType);
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
            return false;
        }
        
        //rdias - UCSD's coeus personalization - Begin
        persnref.setFormSecurity(responderBean.getPersnXMLObject());
        if(functionType != DISPLAY_MODE && persnref != null && awardBaseBean != null) {
        	boolean allowaccess = persnref.formSecurity("award",awardBaseBean.getMitAwardNumber());
        	if (!allowaccess) {
            	CoeusOptionPane.showErrorDialog("Coeus Form Security: Edit disabled");
            	return false;
            }
        }
        //rdias - UCSD's coeus personalization - End        
        boolean bcheck=responderBean.isSuccessfulResponse();
        if(responderBean.isSuccessfulResponse()) {
            Hashtable awardData = (Hashtable)responderBean.getDataObject();
            //prepare query key
            awardBean = (AwardBean)awardData.get(AwardBean.class);
            this.awardBaseBean = (AwardBaseBean)awardBean;
            if(functionType == NEW_AWARD || functionType == NEW_CHILD) {
                this.awardBaseBean.setSequenceNumber(1);
            //Added with case 2796: Sync To Parent
            }else if(functionType == NEW_CHILD_COPIED){
                this.awardBaseBean.setParent(false);
            }
            //2796: Sync To Parent : End
            queryKey = this.awardBaseBean.getMitAwardNumber() + this.awardBaseBean.getSequenceNumber();
            
            //Set title.
            title = titleStart + this.awardBaseBean.getMitAwardNumber() +": Sequence : "+this.awardBaseBean.getSequenceNumber();
            this.title = title;
            if(awardBaseWindow != null) {
                awardBaseWindow.setTitle(title);
            }
            extractAwardToQueryEngine(awardData,false);
        }else {
            //Server Error
            CoeusOptionPane.showErrorDialog(responderBean.getMessage());
            return false;
        }
        
        return true;
    }
    
    /** extracts award data and stores to QueryEngine.
     * @param awardData award data to be saves to query engine.
     */
    private void extractAwardToQueryEngine(Hashtable awardData,boolean afterSave) {
        //Bug Fix:Performance Issue (Out of memory) Start 2
        if(afterSave){
            queryEngine.removeDataCollection(queryKey);
        }
        //Bug Fix:Performance Issue (Out of memory) End 2
        
        awardBean = (AwardBean)awardData.get(AwardBean.class);
        
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
        /*if(getFunctionType() == NEW_AWARD) {
            awardHeaderBean = new AwardHeaderBean();
            awardHeaderBean.setMitAwardNumber(awardBaseBean.getMitAwardNumber());
            awardHeaderBean.setSequenceNumber(awardBaseBean.getSequenceNumber());
         
            CoeusVector cvAwardHeader = new CoeusVector();
            cvAwardHeader.add(awardHeaderBean);
            awardData.put(AwardHeaderBean.class ,cvAwardHeader);
        }*/
        
        CoeusVector cvAwardInvestigators = awardBean.getAwardInvestigators();
        if(cvAwardInvestigators != null && cvAwardInvestigators.size() > 0) {
            awardData.put(AwardInvestigatorsBean.class, cvAwardInvestigators);
        }
        
        
        CoeusVector cvAwardAmountInfo = awardBean.getAwardAmountInfo();
        if(cvAwardAmountInfo != null && cvAwardAmountInfo.size() > 0) {
            //Bug Fix:1507 Start 1
            //Added by Geo as an extension
            //We need to check this in all the modes. So commenting the mode checking code
            
//            if(getFunctionType() == TypeConstants.MODIFY_MODE ){
                if(saveCount == 0)
                {
                   cvAwardAmountInfo = setChangeToZero(cvAwardAmountInfo);
                }
//            }
            //Bug Fix:1507 End 1
            awardData.put(AwardAmountInfoBean.class, cvAwardAmountInfo);
        }
        
        CoeusVector cvAwardComments = awardBean.getAwardComments();
        if(cvAwardComments != null && cvAwardComments.size() > 0) {
            awardData.put(AwardCommentsBean.class, cvAwardComments);
        }
        
        /*CoeusVector cvAwardCustomElements = awardBean.getAwardCustomElements();
        if(cvAwardCustomElements != null && cvAwardCustomElements.size() > 0) {
            awardData.put(AwardCustomDataBean.class, cvAwardCustomElements);
        }*/
        
        CoeusVector cvAwardApprovedSubcontracts = awardBean.getAwardApprovedSubcontracts();
        if(cvAwardApprovedSubcontracts != null && cvAwardApprovedSubcontracts.size() > 0) {
            awardData.put(AwardApprovedSubcontractBean.class, cvAwardApprovedSubcontracts);
        }
        
        CoeusVector cvSubcontractFundingSource = awardBean.getSubcontractFundingSource();
        if(cvSubcontractFundingSource != null && cvSubcontractFundingSource.size() > 0) {
            awardData.put(SubContractFundingSourceBean.class, cvSubcontractFundingSource);
        }
        
        //Get the AwardHierarchy Data and put it into hashtable
        if (getFunctionType() == NEW_AWARD || getFunctionType() == NEW_CHILD ||
        getFunctionType() == NEW_CHILD_COPIED) {
            AwardHierarchyBean awardHierarchyBean = (AwardHierarchyBean)
            awardData.get(AwardHierarchyBean.class);
            if (awardHierarchyBean != null) {
                CoeusVector cvAwardHierarchy = new CoeusVector();
                cvAwardHierarchy.add(awardHierarchyBean);
                awardData.put(AwardHierarchyBean.class, cvAwardHierarchy);
            }
        }
        
        //if in new award mode. the sequence number of custum elements would be -1
        //so set the sequence number before sending to query engine.
        //Bug Fix:Ajay:added getFunctionType = NEW_CHILD , this was missed.
        if(getFunctionType() == NEW_AWARD || getFunctionType() == NEW_ENTRY || getFunctionType() == CORRECT_AWARD
            || getFunctionType() == NEW_CHILD) {
            CoeusVector cvAwardCustomData = (CoeusVector)awardData.get(AwardCustomDataBean.class);
            if(cvAwardCustomData != null && cvAwardCustomData.size() > 0) {
                AwardCustomDataBean awardCustomDataBean;
                for(int index = 0; index < cvAwardCustomData.size(); index++) {
                    awardCustomDataBean = (AwardCustomDataBean)cvAwardCustomData.get(index);
                    awardCustomDataBean.setSequenceNumber(awardBean.getSequenceNumber());
                    if (afterSave) { //for bug fix for save required #1334
                        awardCustomDataBean.setAcType(null);
                    }
                }//end for
            }//end if  cvAwardCustomData.size() > 0
        }//end if NEW_AWARD
        
          // Added by Bijosh/chandra - to avoid save conformation in NEW_CHILD_COPIED mode
        if(getFunctionType() == NEW_CHILD_COPIED) {
            CoeusVector cvAwardCustomData = (CoeusVector)awardData.get(AwardCustomDataBean.class);
            if(cvAwardCustomData != null && cvAwardCustomData.size() > 0) {
                AwardCustomDataBean awardCustomDataBean;
                for(int index = 0; index < cvAwardCustomData.size(); index++) {
                    awardCustomDataBean = (AwardCustomDataBean)cvAwardCustomData.get(index);
                    //awardCustomDataBean.setSequenceNumber(awardBean.getSequenceNumber());
                    if (afterSave) { //for bug fix for save required #1334
                        awardCustomDataBean.setAcType(null);
                    }
                }//end for
            }//end if  cvAwardCustomData.size() > 0
        }//end if NEW_CHILD_COPIED
        
        // Added for case# 2800 - Award  Attachments - Start
        CoeusVector cvAwardDocuments = (CoeusVector)awardData.get(AwardDocumentBean.class);
        if(cvAwardDocuments != null && cvAwardDocuments.size()>0){
            awardData.put(AwardDocumentBean.class, cvAwardDocuments);
        }else{
            cvAwardDocuments = new CoeusVector();
            awardData.put(AwardDocumentBean.class, cvAwardDocuments);
        }
        // Added for case# 2800 - Award  Attachments - End        
        // Added for COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split Infrastructure project - Start
        CoeusVector cvInvCreditTypes = (CoeusVector)awardData.get(CoeusConstants.INVESTIGATOR_CREDIT_TYPES_KEY);
        if(cvInvCreditTypes != null && !cvInvCreditTypes.isEmpty()){
            awardData.put(CoeusConstants.INVESTIGATOR_CREDIT_TYPES_KEY, cvInvCreditTypes);
        }else{
            cvInvCreditTypes = new CoeusVector();
            awardData.put(CoeusConstants.INVESTIGATOR_CREDIT_TYPES_KEY, cvInvCreditTypes);
        }
        
        CoeusVector cvInvCrediSplit = (CoeusVector)awardData.get(CoeusConstants.INVESTIGATOR_CREDIT_SPLIT_KEY);
        if(cvInvCrediSplit != null && !cvInvCrediSplit.isEmpty()){
            awardData.put(CoeusConstants.INVESTIGATOR_CREDIT_SPLIT_KEY, cvInvCrediSplit);
        }else{
            cvInvCrediSplit = new CoeusVector();
            awardData.put(CoeusConstants.INVESTIGATOR_CREDIT_SPLIT_KEY, cvInvCrediSplit);
        }
        
        
        CoeusVector cvInvUnitCreditSplit = (CoeusVector)awardData.get(CoeusConstants.INVESTIGATOR_CREDIT_SPLIT_KEY);
        if(cvInvUnitCreditSplit != null && !cvInvUnitCreditSplit.isEmpty()){
            awardData.put(CoeusConstants.INVESTIGATOR_CREDIT_SPLIT_KEY, cvInvUnitCreditSplit);
        }else{
            cvInvUnitCreditSplit = new CoeusVector();
            awardData.put(CoeusConstants.INVESTIGATOR_CREDIT_SPLIT_KEY, cvInvUnitCreditSplit);
        }

        // Added for COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split Infrastructure project - End
        queryEngine.addDataCollection(queryKey,awardData);
        
        //Since Investigators encapsulates Units managing A/C types has to be
        //done explicitly. so set all Units beans to Query engine. and then update(integrate)
        //before sending to server(Save Award).
        extractInvestigatorUnits();
        //COEUSQA 2111 STARTS
        edu.mit.coeus.award.bean.AwardDocumentRouteBean awardDocumentRouteBean= awardBean.getLatestAwardDocumentRouteBean();
        if((awardDocumentRouteBean!=null)&&(awardDocumentRouteBean.getRoutingDocumentNumber()>0)){routing=true;}
        //COEUSQA 2111 ENDS
    }
    
    public void prepareQueryKey(AwardBaseBean awardBaseBean) {
        //remove data from old query key before preparing new one.
        queryEngine.removeDataCollection(queryKey);
        super.prepareQueryKey(awardBaseBean);
    }
    
    /** instantiates instance variables and tabpages. */
    private void initComponents() {
        
        awardBaseWindow.setFunctionType(getFunctionType());
        
        //If in display Mode disable Save
        if(getFunctionType() == DISPLAY_MODE) {
            awardBaseWindow.btnSave.setEnabled(false);
            awardBaseWindow.mnuItmSave.setEnabled(false);
            awardBaseWindow.btnSync.setEnabled(false);//2796
            awardBaseWindow.mnuItmSync.setEnabled(false);//2796
             //AWARD ROUTING ENHANCEMENT STARTS
            //awardBaseWindow.mnuItmApprovalMap.setEnabled(false);
            awardBaseWindow.mnuItmApproveReject.setEnabled(routing);
           // awardBaseWindow.mnuItmShowRouting.setEnabled(false);
           // awardBaseWindow.mnuItmSubmitApproval.setEnabled(false);
            //AWARD ROUTING ENHANCEMENT ENDS
        }else {
            awardBaseWindow.btnPrevious.setEnabled(false);
            awardBaseWindow.btnNext.setEnabled(false);
            //Bug Fix : 1057 - START
            awardBaseWindow.mnuItmPrevious.setEnabled(false);
            awardBaseWindow.mnuItmNext.setEnabled(false);
            //Bug Fix : 1057 - END
            //Case 2796:Sync To Parent
            if(!awardBaseBean.isParent()){
                awardBaseWindow.btnSync.setEnabled(false);
                awardBaseWindow.mnuItmSync.setEnabled(false);
            }
            //2796 End
            //AWARD ROUTING ENHANCEMENT STARTS
           // awardBaseWindow.mnuItmApprovalMap.setEnabled(true);
            awardBaseWindow.mnuItmApproveReject.setEnabled(routing);
          //  awardBaseWindow.mnuItmShowRouting.setEnabled(true);
           // awardBaseWindow.mnuItmSubmitApproval.setEnabled(true);
            //AWARD ROUTING ENHANCEMENT ENDS
        }
        //Bug Fix:Performance Issue (Out of memory) Start 3
        //Commented and made as instance variable
        //JPanel basePanel = new JPanel();
        basePanel = new JPanel();
        //Bug Fix:Performance Issue (Out of memory) End 3
        
        basePanel.setLayout(new BorderLayout());
        
        if(awardDetailController == null){
            loadTab(AWARD_DETAIL_TAB_INDEX);
        }
        if(awardReportsController == null) {
            loadTab(AWARD_REPORTS_TAB_INDEX);
        }
        //create Money and End Dates to listen to account number modified
        //and award status modified
        if(moneyAndEndDatesController == null) {
            loadTab(MONEY_END_DATES_TAB_INDEX);
        }
        
        //otherHeaderController = new OtherHeaderController(awardBaseBean, getFunctionType());
        
        //awardInvestigatorController = new AwardInvestigatorController(awardBaseBean, queryKey);
        //awardInvestigatorController.setFunctionType(getFunctionType());
        
        //subContractController = new SubContractController(awardBaseBean, getFunctionType());
        
        //awardCommentsController = new AwardCommentsController(mdiForm, awardBaseBean);
        //awardCommentsController.setFunctionType(getFunctionType());
        
        //awardTermsController = new AwardTermsController(awardBaseBean, getFunctionType());
        
        //awardContactsController = new AwardContactsController(awardBaseBean, mdiForm, getFunctionType());
        
        //Bug Fix:Performance Issue (Out of memory) Start 4
        //Commented and made Instance variable
        //Container awardBaseContainer = awardBaseWindow.getContentPane();
        awardBaseContainer = awardBaseWindow.getContentPane();
        
        //initially it was tbdPnAward.addTab(AWARD_DETAIL,new JScrollPane(awardDetailController.getControlledUI()));
        scrPnDetails = new JScrollPane(awardDetailController.getControlledUI());
        // JM 4-10-2012 allow mouse wheel to scroll JScrollPane
        scrPnDetails.setWheelScrollingEnabled(true);
        scrPnDetails.addMouseWheelListener( new MouseWheelListener( )
        {
            public void mouseWheelMoved( MouseWheelEvent e ) {
            }
        });

        tbdPnAward.addTab(AWARD_DETAIL, scrPnDetails); //new JScrollPane(awardDetailController.getControlledUI()));
//        tbdPnAward.addTab(AWARD_DETAIL, null); //new JScrollPane(awardDetailController.getControlledUI()));
        
        tbdPnAward.addTab(OTHER_HEADER, null);//otherHeaderController.getControlledUI());
        
        //initially it was tbdPnAward.addTab(MONEY_END_DATES,new JScrollPane(moneyAndEndDatesController.getControlledUI()));
        scrPnMoneyEndDates = new JScrollPane(moneyAndEndDatesController.getControlledUI());
        tbdPnAward.addTab(MONEY_END_DATES, scrPnMoneyEndDates);//new JScrollPane(moneyAndEndDatesController.getControlledUI()));
        
        tbdPnAward.addTab(AWARD_CONTACTS, null);//awardContactsController.getControlledUI());
        
        
        //initially it was tbdPnAward.addTab(MONEY_END_DATES,new JScrollPane(awardReportsController.getControlledUI()));
        scrPnReport = new JScrollPane(awardReportsController.getControlledUI());
        tbdPnAward.addTab(AWARD_REPORTS,scrPnReport); //new JScrollPane(awardReportsController.getControlledUI()));
        
        //Bug Fix:Performance Issue (Out of memory) End 4
        
        tbdPnAward.addTab(AWARD_TERMS, null);
        
        //For the Coeus Enhancement case:#1799 start step:6 
        tbdPnAward.addTab(AWARD_SPECIAL_REVIEW,null);
        //End Coeus Enhancement case:#1799 step:6
        
        tbdPnAward.addTab(INVESTIGATOR, null);//awardInvestigatorController.getControlledUI());
        
        // 3823: Key Person Records Needed in Inst Proposal and Award
// JM 6-7-2011 removed per 4.4.2; do not want KEY PERSON Tab
//        tbdPnAward.addTab(KEY_PERSON, null);
        
        tbdPnAward.addTab(COMMENTS, null);//awardCommentsController.getControlledUI());
        tbdPnAward.addTab(SUB_CONTRACT, null);//subContractController.getControlledUI());
        
        // Added for case# 2800 - Award  Attachments - Start
        tbdPnAward.addTab(AWARD_DOCUMENTS,null);
        // Added for case# 2800 - Award  Attachments - End
        
        // JM 9-26-2014 added for Restrictions
        tbdPnAward.addTab(RESTRICTIONS,null);
        
        // JM 11-21-2011 added for Centers
        tbdPnAward.addTab(CENTERS,null);

        CoeusVector cvCustomData;
        try{
            cvCustomData = queryEngine.getDetails(queryKey, AwardCustomDataBean.class);
            
            if(cvCustomData != null && cvCustomData.size() > 0) {
                //display others tab
                tbdPnAward.addTab(OTHER, null);
            }
        }catch (CoeusException coeusException) {
            coeusException.printStackTrace();
        }
        
        //if in new mode load all tabs
       //Case :#3149 � Tabbing between fields does not work on others tabs - Start
       // Modified to initialize the components in Tab
       // if(getFunctionType() != CORRECT_AWARD && getFunctionType() != DISPLAY_MODE) {
        //Case :#3149 - End
            for(int index = 1; index < controller.length; index++) {
                loadTab(index);
                //Bug Fix:Performance Issue (Out of memory) Start  5
                
                //Wrote a new Method for loading the tabs
                //tbdPnAward.setComponentAt(index, controller[index].getControlledUI());
                
                setTabComponent(index);
                
                //Bug Fix:Performance Issue (Out of memory) End 5
            }
        //Case :#3149 Tabbing between fields does not work on others tabs - Start
       // }
       //Case :#3149 - End
        basePanel.add(tbdPnAward);
        awardBaseContainer.add(basePanel);
        //rdias - UCSD's coeus personalization - Begin
        persnref.customize_tabs(tbdPnAward, CoeusGuiConstants.AWARD_FRAME_TITLE,
        		awardBean.getMitAwardNumber(),getFunctionType());
        //rdias - UCSD's coeus personalization - End

    }
    
    public boolean isDisplayed() {
        return canDisplay;
    }
    
    /** displays the internal frame which is controlled by this. */
    public void display() {
        if(!canDisplay) return ;
        
        try{
            char formFunctionType;
            
            if(getFunctionType() != DISPLAY_MODE) {
                formFunctionType = CORRECT_AWARD;
            }else {
                formFunctionType  = DISPLAY_MODE;
            }
            mdiForm.putFrame(CoeusGuiConstants.AWARD_BASE_WINDOW, awardBaseBean.getMitAwardNumber(), formFunctionType, awardBaseWindow);
            mdiForm.getDeskTopPane().add(awardBaseWindow);
            awardBaseWindow.setSelected(true);
            awardBaseWindow.setVisible(true);
            
            //Select proposal window should not be opened if the new award is being
            //generated from an EDI transaction.(checking for EDI Transaction not done since
            //this window is not opened from EDI.
            // JM 9-26-2013 change for proposal search pop-up for NEW_ENTRY
            if((getFunctionType() == NEW_AWARD || getFunctionType() == NEW_ENTRY) 
            		&& !openedFromEdiTransaction) {
            //    if(getFunctionType() == NEW_AWARD && !openedFromEdiTransaction) {
                showProposalSearch();
            }
           //Added for Case#3893 - Java 1.5 issues - Start
            if(getFunctionType() != DISPLAY_MODE) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        awardDetailController.getAwardDetailForm().cmbStatus.requestFocus();
                    }
                });
            }
            //Case#3893 - End
        }catch (PropertyVetoException propertyVetoException) {
            propertyVetoException.printStackTrace();
        }
    }
    
    public void formatFields() {
    }
    
    /** returns the Component controlled.
     * @return controlled UI
     */
    public Component getControlledUI() {
        return awardBaseWindow;
    }
    
    public Object getFormData() {
        return null;
    }
    
    /** registers the components with event listeners. */
    public void registerComponents() {
        awardBaseWindow.addVetoableChangeListener(this);
        
        //adding listeners to toolbar buttons
        awardBaseWindow.btnNext.addActionListener(this);
        awardBaseWindow.btnPrevious.addActionListener(this);
        awardBaseWindow.btnPrintAwdNotice.addActionListener(this);
        awardBaseWindow.btnNotepad.addActionListener(this);
        awardBaseWindow.btnRepReq.addActionListener(this);
        awardBaseWindow.btnMedusa.addActionListener(this);
        awardBaseWindow.btnSave.addActionListener(this);
        awardBaseWindow.btnClose.addActionListener(this);
        
        //adding listeners to File menu
        awardBaseWindow.mnuItmInbox.addActionListener(this);
        //Case 2110 Start
        awardBaseWindow.mnuItmCurrentLocks.addActionListener(this);
        //Case 2110 End
        
        //Commented since we are not using it in Coeus 4.0
        //awardBaseWindow.mnuItmPrintSetup.addActionListener(this);
        
        awardBaseWindow.mnuItmNext.addActionListener(this);
        awardBaseWindow.mnuItmPrevious.addActionListener(this);
        awardBaseWindow.mnuItmPrntAwdNotice.addActionListener(this);
        awardBaseWindow.mnuItmPrntDeltaRep.addActionListener(this);
        awardBaseWindow.mnuItmClose.addActionListener(this);
        awardBaseWindow.mnuItmSave.addActionListener(this);
        awardBaseWindow.mnuItmChangePassword.addActionListener(this);
        //Added for Case#3682 - Enhancements related to Delegations - Start
//        awardBaseWindow.mnuItmDelegations.addActionListener(this);
        //Added for Case#3682 - Enhancements related to Delegations - End
        awardBaseWindow.mnuItmPreferences.addActionListener(this);
        awardBaseWindow.mnuItmExit.addActionListener(this);
        
        //adding listeners to Details Menu
        awardBaseWindow.mnuItmCostSharing.addActionListener(this);
        awardBaseWindow.mnuItmIndirectCost.addActionListener(this);
        awardBaseWindow.mnuItmPaymntSch.addActionListener(this);
        awardBaseWindow.mnuItmScienceCode.addActionListener(this);
        awardBaseWindow.mnuItmSplRate.addActionListener(this);
        //For the Coeus Enhancement case:#1799 start step:7
        //awardBaseWindow.mnuItmSplReview.addActionListener(this);
        //End Coeus Enhancement case:#1799 step:7
        awardBaseWindow.mnuItmSpnsrFndngTrans.addActionListener(this);
        awardBaseWindow.mnuItmApprEq.addActionListener(this);
        awardBaseWindow.mnuItmApprFornTrips.addActionListener(this);
        awardBaseWindow.mnuItmFundProps.addActionListener(this);
        awardBaseWindow.mnuItmAwdClsOut.addActionListener(this);
        awardBaseWindow.mnuItmNotepad.addActionListener(this);
        awardBaseWindow.mnuItmRepReq.addActionListener(this);
        awardBaseWindow.mnuItmMedusa.addActionListener(this);
        //Added for Case#2214 email enhancement
        //awardBaseWindow.btnSendEmail.addActionListener(this); // JM 1-14-2015
        
        // JM 1-14-2015 check for permissions; only enable button if user has notification right - MC only
		awardBaseWindow.btnSendEmail.setEnabled(false);
        edu.vanderbilt.coeus.utils.UserPermissions perm = 
        		new edu.vanderbilt.coeus.utils.UserPermissions(mdiForm.getUserId());
        try {
			boolean hasRight = perm.hasRight("SEND_AWARD_NOTIFICATIONS");
			if (hasRight) {
				awardBaseWindow.btnSendEmail.setEnabled(true);
		        awardBaseWindow.btnSendEmail.addActionListener(this);
			}
			
		} catch (CoeusClientException e) {
			System.out.println("Could not get notification senders list");
		}
        // JM END        
        
        awardBaseWindow.btnSync.addActionListener(this);
        awardBaseWindow.mnuItmSync.addActionListener(this);
        awardBaseWindow.mnuItmApproveReject.addActionListener(this);
        //for award disclosure status
//adding menu item award disclosure status-- starts
         awardBaseWindow.mnuItmDisclosureStatus.addActionListener(this);
//adding menu item award disclosure status-- ends
        //COEUSQA 2111 STARTS 
         awardBaseWindow.mnuItmRoutPrntAwdNotice.addActionListener(this);
         awardBaseWindow.mnuItmRoutPrntDeltaRep.addActionListener(this);
         if((CORRECT_AWARD == getFunctionType() || TypeConstants.DISPLAY_MODE == getFunctionType())
         && CheckUserHasRouteDocRight()){
             awardBaseWindow.mnuItmRoutPrntAwdNotice.setEnabled(true);
             awardBaseWindow.mnuItmRoutPrntDeltaRep.setEnabled(true);
         }else{
             awardBaseWindow.mnuItmRoutPrntAwdNotice.setEnabled(false);
             awardBaseWindow.mnuItmRoutPrntDeltaRep.setEnabled(false);
         }
         
         
         awardBaseWindow.mnuItmRoutingHistory.addActionListener(this);
        //COEUSQA 2111 ENDS
         
        //Listen to Award Detail bean modified
        //to check if Award Type is selected.so as to allow Other header to be selcted.
        //listen to bean updated only if not in display mode.
        if(getFunctionType() != DISPLAY_MODE) {
            addBeanUpdatedListener(this, AwardHeaderBean.class);
        }
        //Bug Fix : 1048 - START
        //Will always have to listen to this event.
        // since Medusa can be opened in Display  Mode also.
        addBeanUpdatedListener(this, AwardAmountInfoBean.class);
        //Bug Fix : 1048 - END
        
        tbdPnAward.addChangeListener(this);
    }
    
    /** saves all the tab page data to the query engine. */
    public void saveFormData() {
        try{
            //Save Form Data of all Tab Pages.
        /*awardDetailController.saveFormData();
        otherHeaderController.saveFormData();
        awardInvestigatorController.saveFormData();
        subContractController.saveFormData();
        awardCommentsController.saveFormData();
        //awardTermsController.saveFormData();
        awardContactsController.saveFormData();
         */
            // 4154: Problems in IRB Linking - Start
            if(getFunctionType()==NEW_AWARD){
                awardSpecialReviewController.saveRequired = true;
            }
            // 4154: Problems in IRB Linking - End
            for(int index = 0; index < controller.length; index++) {
                if(controller[index] != null) {
                    controller[index].saveFormData();
                }
            }
        }catch (CoeusException coeusException) {
            coeusException.printStackTrace();
            CoeusOptionPane.showErrorDialog(coeusException.getMessage());
        }
    }
    
    //this is just to stop investigator  from saving before close.
    private void saveFormDataBeforeClose() {
        try{
        /*awardDetailController.saveFormData();
        otherHeaderController.saveFormData();
        subContractController.saveFormData();
        awardCommentsController.saveFormData();
        //awardTermsController.saveFormData();
        awardContactsController.saveFormData();
         */
            for(int index = 0; index < controller.length; index++) {
                if(controller[index] != null && !controller[index].getClass().equals(AwardInvestigatorController.class)){
                    controller[index].saveFormData();
                }
            }
        }catch (CoeusException coeusException) {
            coeusException.printStackTrace();
            CoeusOptionPane.showErrorDialog(coeusException.getMessage());
        }
    }
    
    private boolean postSaveCheck(boolean beforeClose)throws CoeusException, CoeusUIException {
        boolean statusChange = false;
        CoeusVector cvData = queryEngine.getDetails(queryKey, AwardDetailsBean.class);
        AwardDetailsBean awardDetailsBean = (AwardDetailsBean)cvData.get(0);
		// JM 6-21-2012 changed HOLD status code to 7 since 6 is Award Ended here at VU
        //if(awardDetailsBean.getStatusCode() != 6) {//6 = HOLD
        if(awardDetailsBean.getStatusCode() != HOLD_STATUS) {//7 = HOLD AT VU
        // JM END
            //Pre Save
            RequesterBean preSaveRequesterBean = new RequesterBean();
            Hashtable hashtable = new Hashtable();
            CoeusVector cvAwardDetails = queryEngine.getDetails(queryKey, AwardDetailsBean.class);
            CoeusVector cvFundingProps = queryEngine.getDetails(queryKey, AwardFundingProposalBean.class);
            hashtable.put(AwardBaseBean.class, cvAwardDetails);
            hashtable.put(AwardFundingProposalBean.class, cvFundingProps);
            preSaveRequesterBean.setDataObject(hashtable);
            preSaveRequesterBean.setFunctionType('V');
            
            AppletServletCommunicator preSaveAppletServletCommunicator = new AppletServletCommunicator(CoeusGuiConstants.CONNECTION_URL + SERVLET, preSaveRequesterBean);
            preSaveAppletServletCommunicator.send();
            ResponderBean preSaveResponderBean = preSaveAppletServletCommunicator.getResponse();
            if(preSaveResponderBean.isSuccessfulResponse()){
                // Modified for COEUSDEV-946 : Add additional Hold Prompts to Award save and present one prompt listing all Hold reasons; Modify Hold Notice to list all Hold reasons - Start
//                boolean finInstComplete = ((Boolean)preSaveResponderBean.getDataObject()).booleanValue();
//                if(!finInstComplete) {
//                    //display msg to set the award to hold.
//                    int select = CoeusOptionPane.showQuestionDialog(coeusMessageResources.parseMessageKey("The award "+awardBaseBean.getMitAwardNumber() +
//                            FINC_INTST_NOT_COMP_HOLD),
//                            CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_YES);
//                    
//                    if(select == CoeusOptionPane.SELECTION_YES) {
//                        awardDetailsBean.setStatusCode(6);//6 = Status code as HOLD
//                        queryEngine.update(queryKey, awardDetailsBean);
//                        //saveAward(beforeClose);
//                        updateAwardStatus(beforeClose, true);
//                        statusChange = true;
//                    }else {
//                        //change for fix release lock bug
////                        //Don't Display Status update dialog.
////                        if(beforeClose) {
////                            releaseLock();
////                        }
//                        //change for fix release lock bug
//                    }
//                }
                // Award COI Disclosure validation are moved to pkg_award_validation, this package can be configure to add any kind of validation
                // without making any  client or server changes.
                // All the validations are displayed in a single pop-up window. User can select Yes to change the award status to 'Hold'
                /* JM 10-4-2013 using enhanced validations below
                Vector vecvalidations = (Vector)preSaveResponderBean.getDataObjects();
                if(vecvalidations != null && !vecvalidations.isEmpty()){
                    AwardValidationController awardValidationController = new AwardValidationController(mdiForm,awardDetailsBean.getMitAwardNumber());
                    awardValidationController.setFormData(vecvalidations);
                    awardValidationController.display();
                    if(awardValidationController.canChangeStatusToHold()){
                    		// JM 6-21-2012 changed HOLD status code to 7 since 6 is Award Ended at VU
                            awardDetailsBean.setStatusCode(HOLD_STATUS);
                            // JM END
                            queryEngine.update(queryKey, awardDetailsBean);
                            updateAwardStatus(beforeClose, true);
                            statusChange = true;
                    }
                }*/
                // JM 10-4-2013 enhanced validations, now with added awesomeness!
            	// TODO
            	HashMap valid = performValidations(awardDetailsBean, beforeClose, statusChange);
            	awardDetailsBean = (AwardDetailsBean) valid.get("awardDetailsBean");
            	statusChange = (Boolean) valid.get("statusChange");
                // JM END
            }else {
                //server error
                CoeusOptionPane.showErrorDialog(preSaveResponderBean.getMessage());
                throw new CoeusException(SERVER_ERROR);
            }
        }//end if statuscode = HOLD
        return statusChange;
    }
    
    // JM 10-7-2013 moved validations to new method so can be called from new menu item
    // TODO
    private HashMap performValidations(AwardDetailsBean awardDetailsBean,boolean beforeClose,boolean statusChange) throws CoeusException {
    	HashMap retValid = new HashMap();
    	edu.vanderbilt.coeus.award.controller.AwardValidationController enhancedValid = 
    		new edu.vanderbilt.coeus.award.controller.AwardValidationController(awardBaseBean.getMitAwardNumber());
        Vector vecValidations = (Vector) enhancedValid.performAwardValidations();
        if (enhancedValid.hasValidationErrors() || enhancedValid.hasValidationAlerts()) {
        	// Get the errors and the alerts
            AwardValidationController awardValidationController = new AwardValidationController(mdiForm,awardDetailsBean.getMitAwardNumber());
			awardValidationController.setFormData(vecValidations);
            awardValidationController.display();
            if (enhancedValid.hasValidationErrors()) {
                if (awardValidationController.canChangeStatusToHold()) {
                        awardDetailsBean.setStatusCode(HOLD_STATUS);
                        queryEngine.update(queryKey, awardDetailsBean);
                        updateAwardStatus(beforeClose, true);
                        statusChange = true;
                }
            }
        }
        retValid.put("awardDetailsBean", awardDetailsBean);
        retValid.put("statusChange", statusChange);
        return retValid;
    }
    // JM END
    
    private void updateAwardStatus(boolean beforeClose, boolean statusChange)throws CoeusException {
        CoeusVector cvData = queryEngine.getDetails(queryKey, AwardDetailsBean.class);
        AwardDetailsBean awardDetailsBean = (AwardDetailsBean)cvData.get(0);
		// JM 6-21-2012 changed HOLD status code to 7 since 6 is Award Ended at VU
        awardDetailsBean.setStatusCode(HOLD_STATUS);
        // JM END
        queryEngine.update(queryKey, awardDetailsBean);
        
        Hashtable data = new Hashtable();
        //change for fix release lock bug
        if (beforeClose){
        //change for fix release lock bug
        data.put(CoeusConstants.IS_RELEASE_LOCK, new Boolean(beforeClose));
        if(getFunctionType() == NEW_AWARD){
            data.put(CoeusConstants.AWARD, new Boolean(false));
        }
        }
        data.put(AwardDetailsBean.class, awardDetailsBean);
        //change for fix release lock bug
        data.put("STATUSCHANGE", new Boolean(statusChange));
        //change for fix release lock bug
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setDataObject(data);
        requesterBean.setFunctionType('c');
        
        AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator(CoeusGuiConstants.CONNECTION_URL + SERVLET, requesterBean);
        appletServletCommunicator.send();
        ResponderBean responderBean = appletServletCommunicator.getResponse();
        if(responderBean.isSuccessfulResponse()){
            //Success
//            BeanEvent beanEvent = new BeanEvent();
//            beanEvent.setBean(awardDetailsBean);
//            beanEvent.setSource(this);
//            fireBeanUpdated(beanEvent);
        }else {
            //server error
            CoeusOptionPane.showErrorDialog(responderBean.getMessage());
            throw new CoeusException(SERVER_ERROR);
        }
        
    }
    
    /** sends award data to server to be saved.
     * @throws CoeusUIException if any exception occurs.
     */
    private void saveAward(boolean beforeClose)throws CoeusUIException{

        //Case :#3149 � Tabbing between fields does not work on others tabs - Start
        int row = 0 ;
        int column =0;
        JTable customTable = null;
        CustomElementsForm customElementsForm = null;
        if(awardOtherController != null && getFunctionType() != DISPLAY_MODE){
            customElementsForm = awardOtherController.getCustomElementsForm();
            customElementsForm.setSaveAction(true);
        }
       //Case :#3149 - End

        //Save Data for the Other header tab since its reqd for Award Type validation.
        if(tbdPnAward.getSelectedIndex() == OTHER_HEADER_TAB_INDEX) {
            otherHeaderController.saveFormData();
        }
        
        //special case. will have to refactor later.
        if(awardContactsController != null) {
            awardContactsController.saveFormData();
        }
        
        try{
            if(!validate()) {
                throw new CoeusUIException();
            }
        }catch (CoeusUIException coeusUIException) {
            //Case :#3149 � Tabbing between fields does not work on others tabs - Start
             if(awardOtherController != null && getFunctionType() != DISPLAY_MODE){
                 customElementsForm = awardOtherController.getCustomElementsForm();
                 customTable = customElementsForm.getTable();
                 if(customElementsForm.getOtherTabMandatory()){
                     boolean[] lookUpAvailable = customElementsForm.getLookUpAvailable();
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
                 customTable.setEnabled(true);
             }
            //Case :#3149 - End
            throw coeusUIException;
       }
        
        saveFormData();
        //2796 - Sync to parent
//        if(getFunctionType()== CORRECT_AWARD && syncObj!=null){
//            setSyncFlags();
//        }
        //2796 : End
        CoeusVector cvData;
        AwardDetailsBean awardDetailsBean;
        
        try{
            
            cvData = queryEngine.getDetails(queryKey, AwardDetailsBean.class);
            awardDetailsBean = (AwardDetailsBean)cvData.get(0);
//            if(awardDetailsBean.getStatusCode() != 6) {//6 = HOLD
//                //Pre Save
//                RequesterBean preSaveRequesterBean = new RequesterBean();
//                Hashtable hashtable = new Hashtable();
//                CoeusVector cvAwardDetails = queryEngine.getDetails(queryKey, AwardDetailsBean.class);
//                CoeusVector cvFundingProps = queryEngine.getDetails(queryKey, AwardFundingProposalBean.class);
//                hashtable.put(AwardBaseBean.class, cvAwardDetails);
//                hashtable.put(AwardFundingProposalBean.class, cvFundingProps);
//                preSaveRequesterBean.setDataObject(hashtable);
//                preSaveRequesterBean.setFunctionType('V');
//                
//                AppletServletCommunicator preSaveAppletServletCommunicator = new AppletServletCommunicator(CoeusGuiConstants.CONNECTION_URL + SERVLET, preSaveRequesterBean);
//                preSaveAppletServletCommunicator.send();
//                ResponderBean preSaveResponderBean = preSaveAppletServletCommunicator.getResponse();
//                if(preSaveResponderBean.isSuccessfulResponse()){
//                    boolean finInstComplete = ((Boolean)preSaveResponderBean.getDataObject()).booleanValue();
//                    if(!finInstComplete) {
//                        //display msg to set the award to hold.
//                        int select = CoeusOptionPane.showQuestionDialog(coeusMessageResources.parseMessageKey("The award "+awardBaseBean.getMitAwardNumber() +
//                        FINC_INTST_NOT_COMP_HOLD),
//                        CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_YES);
//                        
//                        if(select == CoeusOptionPane.SELECTION_YES) {
//                            awardDetailsBean.setStatusCode(6);//6 = Status code as HOLD
//                        }
//                    }
//                }else {
//                    //server error
//                    CoeusOptionPane.showErrorDialog(preSaveResponderBean.getMessage());
//                    throw new CoeusException(SERVER_ERROR);
//                }
//            }//end if statuscode = 6
            
            //Extract Units from Investigators and set to Investigator.
            //integrateInvestigatorUnits();
            //changed as now Investigator is handling Investigator persons and Units separately.
            //investigator units are integrated before adding to Award Bean.
            
            Hashtable data = new Hashtable();
            
            AwardHeaderBean awardHeaderBean;
            
            
            //AwardBean awardBean = new AwardBean();
            awardBean.setMitAwardNumber(awardBaseBean.getMitAwardNumber());
            awardBean.setSequenceNumber(awardBaseBean.getSequenceNumber());
            //awardBean.setUpdateTimestamp(this.awardBean.getUpdateTimestamp());
            
            //Don't filter to get only modified beans as all beans are required to
            //set sequience number and indicators.
            //cvData = queryEngine.getDetails(queryKey, AwardDetailsBean.class);
            //awardDetailsBean = (AwardDetailsBean)cvData.get(0);
            
            cvData = queryEngine.getDetails(queryKey, AwardHeaderBean.class);
            awardHeaderBean = (AwardHeaderBean)cvData.get(0);
            //if in new award mode and proposal selected from proposal search.
            //proposal number would've been set.
            if(getFunctionType() == NEW_AWARD && proposalNumber != null) {
                awardHeaderBean.setProposalNumber(proposalNumber);
            }
            
            //investigator
            Hashtable htInvestigatorData = new Hashtable();
            cvData = queryEngine.getDetails(queryKey, AwardUnitBean.class);
            htInvestigatorData.put(AwardUnitBean.class, cvData);
            
            cvData = queryEngine.getDetails(queryKey, AwardInvestigatorsBean.class);
            htInvestigatorData.put(AwardInvestigatorsBean.class, cvData);
            integrateInvestigatorUnits(htInvestigatorData);
            
            if(cvData != null && cvData.size() > 0) {
                awardBean.setAwardInvestigators(cvData);
            }
            // Added for COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split Infrastructure project - Start
            cvData = queryEngine.getDetails(queryKey, CoeusConstants.INVESTIGATOR_CREDIT_SPLIT_KEY);
            data.put(CoeusConstants.INVESTIGATOR_CREDIT_SPLIT_KEY, cvData == null ? new CoeusVector() : cvData);
            cvData = queryEngine.getDetails(queryKey, CoeusConstants.INVESTIGATOR_UNIT_CREDIT_SPLIT_KEY);
            data.put(CoeusConstants.INVESTIGATOR_UNIT_CREDIT_SPLIT_KEY, cvData == null ? new CoeusVector() : cvData);
            // Added for COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split Infrastructure project - End
            
            // Modified for COEUSDEV-1152_LOG_ Award creating duplicated transaction IDs - Start
            // When Transaction type is selected and money and end dates screen 
            // is saving for first time in modify and new entry mode, then amount fo details are insert to new seqeunce
//            cvData = queryEngine.getDetails(queryKey, AwardAmountInfoBean.class);
//            if(cvData != null && cvData.size() > 0) {
//                if((saveCount == 0 && getFunctionType() == TypeConstants.MODIFY_MODE)
//                //Bug Fix : 1112 - START
//                //See document CoeusMigration\support\communication\mails\Coeus 4 Bug Status.doc
//                //for detailed description
//                || getFunctionType() == NEW_ENTRY)
//                    //Bug Fix : 1112 - END
//                {
//                    //saving first time. so award amount has to be inserted with new
//                    //amount sequence number and transaction id.
//                    AwardAmountInfoBean awardAmountInfoBean;
//                    for(int index = 0; index < cvData.size(); index++){
//                        awardAmountInfoBean = (AwardAmountInfoBean)cvData.get(index);
//                        if(awardAmountInfoBean.getAcType() != null) {
//                            //modified
//                            awardAmountInfoBean.setAcType(TypeConstants.INSERT_RECORD);
//                        }
//                    }//End for
//                }
//                awardBean.setAwardAmountInfo(cvData);
//            }
            if(moneyAndEndDatesController.isTransactionTypeChanged()){
                cvData = queryEngine.getDetails(queryKey, AwardAmountInfoBean.class);
                if(cvData != null && cvData.size() > 0) {
                    if((saveMoneyAndEndDatesCount == 0 && getFunctionType() == TypeConstants.MODIFY_MODE)
                    //Bug Fix : 1112 - START
                    //See document CoeusMigration\support\communication\mails\Coeus 4 Bug Status.doc
                    //for detailed description
                    || getFunctionType() == NEW_ENTRY || getFunctionType() == NEW_AWARD)
                        //Bug Fix : 1112 - END
                    {
                        //saving first time. so award amount has to be inserted with new
                        //amount sequence number and transaction id.
                        AwardAmountInfoBean awardAmountInfoBean;
                        for(int index = 0; index < cvData.size(); index++){
                            awardAmountInfoBean = (AwardAmountInfoBean)cvData.get(index);
                            if(awardAmountInfoBean.getAcType() != null) {
                                //modified
                                awardAmountInfoBean.setAcType(TypeConstants.INSERT_RECORD);
                            }
                        }//End for
                        saveMoneyAndEndDatesCount++;
                    }
                    awardBean.setAwardAmountInfo(cvData);
                }
            }
            // Modified for COEUSDEV-1152_LOG_ Award creating duplicated transaction IDs - End
            cvData = queryEngine.getDetails(queryKey, AwardCommentsBean.class);
            if(cvData != null && cvData.size() > 0) {
                awardBean.setAwardComments(cvData);
            }
            
            /*cvData = queryEngine.getDetails(queryKey, AwardCustomDataBean.class);
            if(cvData != null && cvData.size() > 0) {
                awardBean.setAwardCustomElements(cvData);
            }*/
            
            NotEquals neqEmpty = new NotEquals("subcontractName", EMPTY);
            NotEquals neqNull = new NotEquals("subcontractName", null);
            And neqEmptyAndneqNull = new And(neqEmpty, neqNull);
            cvData = queryEngine.executeQuery(queryKey, AwardApprovedSubcontractBean.class, neqEmptyAndneqNull);
            
            if(cvData != null && cvData.size() > 0) {
                cvData.sort("acType");
                awardBean.setAwardApprovedSubcontracts(cvData);
            }
            
            cvData = queryEngine.getDetails(queryKey, SubContractFundingSourceBean.class);
            if(cvData != null && cvData.size() > 0) {
                awardBean.setSubcontractFundingSource(cvData);
            }
            
            if(getFunctionType() == CORRECT_AWARD) {
                awardBean.setAcType(TypeConstants.UPDATE_RECORD);
            }else {
                awardBean.setAcType(TypeConstants.INSERT_RECORD);
            }
            
            awardBean.setAwardDetailsBean(awardDetailsBean);
            
            awardBean.setAwardHeaderBean(awardHeaderBean);
            
            //Bug Fix:1403 Start
            if(getFunctionType() == NEW_CHILD_COPIED){
                awardBean.setMode(NEW_CHILD_COPIED);
            }
            //Bug Fix:1403 End
            data.put(AwardBean.class, awardBean);
            
            cvData = queryEngine.getDetails(queryKey, AwardContactDetailsBean.class);
            data.put(AwardContactBean.class, cvData);
            
            cvData = queryEngine.getDetails(queryKey, AwardApprovedEquipmentBean.class);
            data.put(AwardApprovedEquipmentBean.class, cvData);
            
            cvData = queryEngine.getDetails(queryKey, AwardPaymentScheduleBean.class);
            data.put(AwardPaymentScheduleBean.class, cvData);
            
            cvData = queryEngine.getDetails(queryKey, AwardCostSharingBean.class);
            data.put(AwardCostSharingBean.class, cvData);
            
            cvData = queryEngine.getDetails(queryKey, AwardApprovedForeignTripBean.class);
            data.put(AwardApprovedForeignTripBean.class, cvData);
            
            cvData = queryEngine.getDetails(queryKey, AwardScienceCodeBean.class);
            data.put(AwardScienceCodeBean.class, cvData);
            
            cvData = queryEngine.getDetails(queryKey, AwardTransferingSponsorBean.class);
            cvData.sort("acType", true);
            data.put(AwardTransferingSponsorBean.class, cvData);
            
            cvData = queryEngine.getDetails(queryKey, AwardFundingProposalBean.class);
            data.put(AwardFundingProposalBean.class, cvData);
            
            cvData = queryEngine.getDetails(queryKey, AwardBudgetBean.class);
            data.put(AwardBudgetBean.class, cvData);
            
            cvData = queryEngine.getDetails(queryKey, AwardCloseOutBean.class);
            data.put(AwardCloseOutBean.class, cvData);
            
            cvData = queryEngine.getDetails(queryKey, AwardReportTermsBean.class);
            data.put(AwardReportTermsBean.class, cvData);
            
            cvData = queryEngine.getDetails(queryKey, AwardIDCRateBean.class);
            data.put(AwardIDCRateBean.class, cvData);
            
            cvData = queryEngine.getDetails(queryKey, AwardSpecialReviewBean.class);
            data.put(AwardSpecialReviewBean.class, cvData);
            //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - start
            CoeusVector cvSpRvData = queryEngine.getDetails(queryKey, AwardSpecialReviewBean.class);
            awardSpecialReviewController.setVecAwardSpRvData(cvSpRvData);
            //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - end
            // 3823: Key Person Records Needed in Inst Proposal and Award - Start
            cvData = queryEngine.getDetails(queryKey, AwardKeyPersonBean.class);
            data.put(AwardKeyPersonBean.class, cvData);
            // 3823: Key Person Records Needed in Inst Proposal and Award - End


            ////for the unit details save
            cvData = queryEngine.getDetails(queryKey,KeyPersonUnitBean.class);
            data.put(KeyPersonUnitBean.class, cvData);
            ////for the unit details save ends


            
            //Award Terms uses differnt keys to store different Terms - START
            //Constants are :
            //"Equipment Approval",
            //"Invention",
            //"Other Approval/Notification Requirement",
            //"Property",
            //"Publication",
            //"Referenced Documents",
            //"Rights In Data",
            //"Subcontract Approval",
            //"Travel"
            //Keys for these display values are in KeyConstants
            
            NotEquals neMitNull = new NotEquals("mitAwardNumber", null);
            
            cvData = queryEngine.getDetails(queryKey, KeyConstants.EQUIPMENT_APPROVAL_TERMS);
            if(cvData != null && cvData.size() > 0) {
                cvData = cvData.filter(neMitNull);
                cvData.sort("acType", true);
            }
            data.put(KeyConstants.EQUIPMENT_APPROVAL_TERMS, cvData);
            
            cvData = queryEngine.getDetails(queryKey, KeyConstants.INVENTION_TERMS);
            if(cvData != null && cvData.size() > 0) {
                cvData = cvData.filter(neMitNull);
                cvData.sort("acType", true);
            }
            data.put(KeyConstants.INVENTION_TERMS, cvData);
            
            cvData = queryEngine.getDetails(queryKey, KeyConstants.PRIOR_APPROVAL_TERMS);
            if(cvData != null && cvData.size() > 0) {
                cvData = cvData.filter(neMitNull);
                cvData.sort("acType", true);
            }
            data.put(KeyConstants.PRIOR_APPROVAL_TERMS, cvData);
            
            cvData = queryEngine.getDetails(queryKey, KeyConstants.PROPERTY_TERMS);
            if(cvData != null && cvData.size() > 0) {
                cvData = cvData.filter(neMitNull);
                cvData.sort("acType", true);
            }
            data.put(KeyConstants.PROPERTY_TERMS, cvData);
            
            cvData = queryEngine.getDetails(queryKey, KeyConstants.PUBLICATION_TERMS);
            if(cvData != null && cvData.size() > 0) {
                cvData = cvData.filter(neMitNull);
                cvData.sort("acType", true);
            }
            data.put(KeyConstants.PUBLICATION_TERMS, cvData);
            
            cvData = queryEngine.getDetails(queryKey, KeyConstants.REFERENCED_DOCUMENT_TERMS);
            if(cvData != null && cvData.size() > 0) {
                cvData = cvData.filter(neMitNull);
                cvData.sort("acType", true);
            }
            data.put(KeyConstants.REFERENCED_DOCUMENT_TERMS, cvData);
            
            cvData = queryEngine.getDetails(queryKey, KeyConstants.RIGHTS_IN_DATA_TERMS);
            if(cvData != null && cvData.size() > 0) {
                cvData = cvData.filter(neMitNull);
                cvData.sort("acType", true);
            }
            data.put(KeyConstants.RIGHTS_IN_DATA_TERMS, cvData);
            
            cvData = queryEngine.getDetails(queryKey, KeyConstants.SUBCONTRACT_APPROVAL_TERMS);
            if(cvData != null && cvData.size() > 0) {
                cvData = cvData.filter(neMitNull);
                cvData.sort("acType", true);
            }
            data.put(KeyConstants.SUBCONTRACT_APPROVAL_TERMS, cvData);
            
            cvData = queryEngine.getDetails(queryKey, KeyConstants.TRAVEL_RESTRICTION_TERMS);
            if(cvData != null && cvData.size() > 0) {
                cvData = cvData.filter(neMitNull);
                cvData.sort("acType", true);
            }
            data.put(KeyConstants.TRAVEL_RESTRICTION_TERMS, cvData);
            
            //Award Terms - END
            
            //Set the AwardHierarchy Data into hashtable
            if (getFunctionType() == NEW_AWARD || getFunctionType() == NEW_CHILD ||
            getFunctionType() == NEW_CHILD_COPIED) {
                cvData = queryEngine.getDetails(queryKey, AwardHierarchyBean.class);
                if (cvData != null && cvData.size() > 0) {
                    AwardHierarchyBean awardHierarchyBean =
                    (AwardHierarchyBean) cvData.get(0);
                    data.put(AwardHierarchyBean.class, awardHierarchyBean);
                }
            }
            
            //Others
            cvData = queryEngine.getDetails(queryKey, AwardCustomDataBean.class);
            data.put(AwardCustomDataBean.class, cvData);
            // Added for COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split Infrastructure project - Start
            if(getFunctionType() == NEW_AWARD || getFunctionType() == NEW_CHILD || getFunctionType() == NEW_CHILD_COPIED){
                 CoeusVector cvPerCreditSplit = queryEngine.getDetails(queryKey, CoeusConstants.INVESTIGATOR_CREDIT_SPLIT_KEY);
                 if(cvPerCreditSplit != null && !cvPerCreditSplit.isEmpty()){
                     cvPerCreditSplit.setUpdate(
                             InvestigatorCreditSplitBean.class, "moduleNumber",String.class,awardBean.getMitAwardNumber(), null );
                     cvPerCreditSplit.setUpdate(
                             InvestigatorCreditSplitBean.class, "sequenceNo", DataType.getClass(DataType.INT)
                             ,awardBean.getSequenceNumber(), null );
                     
                     data.put(CoeusConstants.INVESTIGATOR_CREDIT_SPLIT_KEY,cvPerCreditSplit);
                 }
                 CoeusVector cvPerUnitCreditSplit = queryEngine.getDetails(queryKey, CoeusConstants.INVESTIGATOR_UNIT_CREDIT_SPLIT_KEY);
                 if(cvPerUnitCreditSplit != null && !cvPerUnitCreditSplit.isEmpty()){
                     cvPerUnitCreditSplit.setUpdate(
                             InvestigatorCreditSplitBean.class, "moduleNumber",String.class,awardBean.getMitAwardNumber(), null );
                     cvPerUnitCreditSplit.setUpdate(
                             InvestigatorCreditSplitBean.class, "sequenceNo", DataType.getClass(DataType.INT)
                             ,awardBean.getSequenceNumber(), null );
                     data.put(CoeusConstants.INVESTIGATOR_UNIT_CREDIT_SPLIT_KEY,cvPerUnitCreditSplit);
                 }
            }
            // Added for COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split Infrastructure project - End
            //Case 1822 Award FNA Start
            /**This sets sequence number and amount sequence number for Award FNA New Entry
             * to awardAmountFNABean**/
//            if(getFunctionType() == NEW_ENTRY){
//                CoeusVector cvAmtInfo = awardBean.getAwardAmountInfo();
//                if(cvAmtInfo!=null && cvAmtInfo.size()>0){
//                    AwardAmountInfoBean awardAmountInfoBean = (AwardAmountInfoBean)cvAmtInfo.get(0);
//                    cvData = queryEngine.getDetails(queryKey, AwardAmountFNABean.class);
//                    if(cvData !=null && cvData.size()>0){
//                        for(int index = 0 ; index < cvData.size(); index++ ){
//                            AwardAmountFNABean awardAmountFNABean = 
//                                            (AwardAmountFNABean)cvData.get(index);
//                            awardAmountFNABean.setMitAwardNumber(awardAmountInfoBean.getMitAwardNumber());
//                            awardAmountFNABean.setSequenceNumber(awardAmountInfoBean.getSequenceNumber());
//                            
//                            //For new entry the AmountSequenceNumber will alaways be 0
//                            if(awardAmountInfoBean.getAmountSequenceNumber() == 0){
//                                awardAmountFNABean.setAmountSequenceNumber(1);
//                            }else{
//                                awardAmountFNABean.setAmountSequenceNumber(awardAmountInfoBean.getAmountSequenceNumber());
//                            }
//                            awardAmountFNABean.setAcType(TypeConstants.INSERT_RECORD);
//                        }//End of for
//                    }//End of if
//                    
//                    if(cvData == null){
//                        cvData = new CoeusVector();
//                    }
//                    data.put(AwardAmountFNABean.class, cvData);
//                    awardAmountInfoBean = null;
//                }
//            }
//            //Case 1822 Award FNA End
            RequesterBean requesterBean = new RequesterBean();
            requesterBean.setFunctionType(SAVE_AWARD);
            requesterBean.setDataObject(data);

            //if award saving first time set boolean value = true. else false
            //for SAP feed entry
            AwardTransactionInfoBean awardTransactionInfoBean;
            if(saveCount == 0) {
                awardTransactionInfoBean = new
                AwardTransactionInfoBean();
                awardTransactionInfoBean.setFirstTimeSave(true);
            }else {
                //update transaction info bean
                awardTransactionInfoBean = (AwardTransactionInfoBean)
                queryEngine.getDetails(queryKey, AwardTransactionInfoBean.class).get(0);
                
                awardTransactionInfoBean.setFirstTimeSave(false);
            }
            data.put(AwardTransactionInfoBean.class, awardTransactionInfoBean);
            
            AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator(CoeusGuiConstants.CONNECTION_URL + SERVLET, requesterBean);
            appletServletCommunicator.send();
            ResponderBean responderBean = appletServletCommunicator.getResponse();
            
            if(responderBean == null) {
                //Could not contact server.
                CoeusOptionPane.showErrorDialog(COULD_NOT_CONTACT_SERVER);
                throw new CoeusUIException();
            }
            
            if(responderBean.isSuccessfulResponse()) {
                boolean statusChange = postSaveCheck(beforeClose);

                if(beforeClose) {
                    //change for fix release lock bug
                    //make sure to release the lock 
                    if (!statusChange) updateAwardStatus(beforeClose, false);
                    //change for fix release lock bug
                    return ;
                }
                saveCount = saveCount + 1;
                if(controller[MONEY_END_DATES_TAB_INDEX] == null) {
                    loadTab(MONEY_END_DATES_TAB_INDEX);
                    
                    //Bug Fix:Performance Issue (Out of memory) Start 6
                    scrPnMoneyEndDates.remove(moneyAndEndDatesController.getControlledUI());
                    scrPnMoneyEndDates = null;
                    scrPnMoneyEndDates = new JScrollPane(controller[MONEY_END_DATES_TAB_INDEX].getControlledUI());
                    tbdPnAward.setComponentAt(MONEY_END_DATES_TAB_INDEX, scrPnMoneyEndDates /*new JScrollPane(controller[MONEY_END_DATES_TAB_INDEX].getControlledUI())*/);
                    //Bug Fix:Performance Issue (Out of memory) End 6
                    
                }
                ((MoneyAndEndDatesController)controller[MONEY_END_DATES_TAB_INDEX]).setSaveCount(saveCount);                
                
                //Bug Fix:1711 Start 1
                //Hashtable awardData = (Hashtable)responderBean.getDataObject();
                 CoeusVector cvRepReqData = null;
                Hashtable awardData = new Hashtable();
                Vector vecAwdDat = (Vector)responderBean.getDataObjects();
                if(vecAwdDat != null){
                    awardData = (Hashtable)vecAwdDat.get(0);
                    //repReqCount = Integer.parseInt( ((Integer)vecAwdDat.get(1)).toString() );
                    cvRepReqData = (CoeusVector)vecAwdDat.get(1);
                }
                //Bug Fix:1711 End 1
                
                if(saveCount == 1 && awardData.containsKey(AwardTransactionInfoBean.class)) {
                    CoeusVector cvAwardData = new CoeusVector();
                    cvAwardData = (CoeusVector)awardData.get(AwardTransactionInfoBean.class);
                    awardTransactionInfoBean = (AwardTransactionInfoBean)cvAwardData.get(0);
                    transactionId = awardTransactionInfoBean.getTransactionId();                
                }
                if(statusChange) {
                    AwardBean awdBean = (AwardBean)awardData.get(AwardBean.class);
                    CoeusVector coeusVecStatus = (CoeusVector)awardData.get( KeyConstants.AWARD_STATUS);
            		// JM 6-21-2012 changed HOLD status code to 7 since 6 is Award Ended at VU
                    Equals equalsHold = new Equals("code", Integer.toString(HOLD_STATUS));
                    // JM END
                    coeusVecStatus = coeusVecStatus.filter(equalsHold);
                    AwardDetailsBean awdDetailsBean = awdBean.getAwardDetailsBean();
                    ComboBoxBean comboBoxBean = (ComboBoxBean)coeusVecStatus.get(0);
            		// JM 6-21-2012 changed HOLD status code to 7 since 6 is Award Ended at VU
                    awdDetailsBean.setStatusCode(HOLD_STATUS);
                    // JM END
                    awdDetailsBean.setStatusDescription(comboBoxBean.getDescription());
                }
                //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - start
                Vector vecData = awardSpecialReviewController.getVecAwardSpRvData();
                ActionValidityChecking checkValid = new ActionValidityChecking();
                //Award Mail Notification for insertion and deletion of special review
                if(vecData != null){
                    for(int i =0 ; i < vecData.size();i++){
                        AwardSpecialReviewBean awardSpRevBean =(AwardSpecialReviewBean)vecData.get(i);
                        if(awardSpRevBean.getSpecialReviewCode() == 2){
                            if(awardSpRevBean.getAcType() != null  && awardSpRevBean.getAcType().equals("I")){
                                synchronized(checkValid){
                                    checkValid.sendMail(ModuleConstants.AWARD_MODULE_CODE,MailActions.AWARD_SPECIAL_REVIEW_INSERTED_FOR_IACUC,awardSpRevBean.getMitAwardNumber(),awardSpRevBean.getSequenceNumber());
                                }
                            }else if(awardSpRevBean.getAcType() != null  && awardSpRevBean.getAcType().equals("D")){
                                
                                synchronized(checkValid){
                                    checkValid.sendMail(ModuleConstants.AWARD_MODULE_CODE,MailActions.AWARD_SPECIAL_REVIEW_DELETED_FOR_IACUC,awardSpRevBean.getMitAwardNumber(),awardSpRevBean.getProtoSequenceNumber());
                                }
                            }
                        }
                    }
                }
                //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - end
                extractAwardToQueryEngine(awardData,true);
                refresh();
                // Added for COEUSQA-2111 : Award module basic routing - Start
                if(CORRECT_AWARD == getFunctionType() && CheckUserHasRouteDocRight()){
                    awardBaseWindow.mnuItmRoutPrntAwdNotice.setEnabled(true);
                    awardBaseWindow.mnuItmRoutPrntDeltaRep.setEnabled(true);
                }else{
                    awardBaseWindow.mnuItmRoutPrntAwdNotice.setEnabled(false);
                    awardBaseWindow.mnuItmRoutPrntDeltaRep.setEnabled(false);
                }
                // Added for COEUSQA-2111 : Award module basic routing - End

                //Bug Fix:1711 Start 2
                if(cvRepReqData != null && cvRepReqData.size() >0){
                    showReviewRepReq(cvRepReqData);
                }
                //Bug Fix:1711 End 2
                
            }else {
                //Server Error
                CoeusOptionPane.showErrorDialog(responderBean.getMessage());
                throw new CoeusUIException();
            }
             //Case :#3149 � Tabbing between fields does not work on others tabs - Start
            if(tbdPnAward.getSelectedIndex() == OTHER_TAB_INDEX && awardOtherController != null && getFunctionType() != DISPLAY_MODE) {
                customElementsForm = awardOtherController.getCustomElementsForm();
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
                customTable.setEnabled(true);
           }
           //Case :#3149 - End
            // Commented for COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split Infrastructure project - Start
            //COEUSQA:1676 - Award Credit Split - Start
            //Get the Investigator details while synching Proposal to Award
//            AwardBean awardBean = (AwardBean)data.get(AwardBean.class);
//            CoeusVector cvAwardInvestigators = awardBean.getAwardInvestigators();
//            AwardInvestigatorsBean awardInvestigatorsBean = null;
//            CoeusVector cvInvestigators = new CoeusVector();
//            if(cvAwardInvestigators!= null && cvAwardInvestigators.size() > 0){
//                 for(Object cvawardInvestigators : cvAwardInvestigators){
//                    awardInvestigatorsBean = (AwardInvestigatorsBean)cvawardInvestigators;
//                    if(awardInvestigatorsBean!=null && awardInvestigatorsBean.getAcType()!=null){
//                        cvInvestigators.add(awardInvestigatorsBean);
//                    }
//                }
//            }
//            CoeusVector cvProposals  = (CoeusVector) data.get(AwardFundingProposalBean.class);
//            if(cvInvestigators!=null && cvInvestigators.size()>0) {
//                InvestigatorCreditSplitController invCreditSplitController =
//                        new InvestigatorCreditSplitController(getFunctionType());
//                invCreditSplitController.setFormData(awardInvestigatorController.getCreditSplitData());
//                //Call the validate() of InvestigatorCreditSplitController to show the warning message if investigator or units credit split doesn't matches to 100           
//                invCreditSplitController.validate();
//            }
            //COEUSQA:1676 - End
            // Commented for COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split Infrastructure project - End
        }catch (CoeusException coeusException) {
            coeusException.printStackTrace();
        }
     
    }
    
    /** refreshes all tab pages. */
    public void refresh() {
        //refresh all tab pages. set mode as update since
        //after refreshing the data to will sent as update
        //next time saved.
        
        setFunctionType(CORRECT_AWARD);
        
        /*awardDetailController.setFunctionType(CORRECT_AWARD);
        awardDetailController.setRefreshRequired(true);
        awardDetailController.refresh();
         
        subContractController.setFunctionType(CORRECT_AWARD);
        subContractController.setRefreshRequired(true);
        subContractController.refresh();
         
        otherHeaderController.setFunctionType(CORRECT_AWARD);
        otherHeaderController.setRefreshRequired(true);
        otherHeaderController.refresh();
         
        awardInvestigatorController.setFunctionType(CORRECT_AWARD);
        awardInvestigatorController.setRefreshRequired(true);
        awardInvestigatorController.refresh();
         
        awardCommentsController.setFunctionType(CORRECT_AWARD);
        awardCommentsController.setRefreshRequired(true);
        awardCommentsController.refresh();
         
        awardContactsController.setFunctionType(CORRECT_AWARD);
        awardContactsController.setRefreshRequired(true);
        awardContactsController.refresh();
         */
        
        try{
            Controller tabController;
            for(int index = 0; index < controller.length; index++) {
                tabController = controller[index];
                if(tabController != null) {
                    tabController.setFunctionType(CORRECT_AWARD);
                    tabController.setRefreshRequired(true);
                    tabController.refresh();
                }
            }
        }catch (CoeusException coeusException) {
            coeusException.printStackTrace();
            CoeusOptionPane.showErrorDialog(coeusException.getMessage());
        }
        
    }
    
    /** sets the form data.
     * @param data form data
     */
    public void setFormData(Object data) {
        if(!(data instanceof AwardBaseBean)) {
            return ;
        }
        
        awardBaseBean = (AwardBaseBean)data;
        
        try{
            
            
            blockEvents(true);
            if(!fetchData(getFunctionType(), null)) {
                canDisplay = false;
                return ;
            }else {
                canDisplay  = true;
                
                AwardController awardController;
                int selectedIndex = tbdPnAward.getSelectedIndex();
                
                for(int index = 0; index < controller.length; index++) {
                    if(controller[index] == null) {
                        continue;
                    }
                    
                    //Bug Fix: 1410 Start 1 Added by chandra to refresh the values 27-Jan-05
                    //Commented for Bug Fix 1410

//                    awardController = (AwardController)controller[index];
//                    awardController.prepareQueryKey(awardBaseBean);
//                    awardController.setFormData(awardBaseBean);
//                    awardController.setRefreshRequired(true);
//                    if(index == selectedIndex) {
//                        awardController.refresh();
//                    }
                    //Added for the case# COEUDEV-301 -Awd List>Scrolling thru List in Display mode-start
                    //Added for the Key Person
                    if(controller[index] instanceof AwardKeyPersonController){
                        awardKeyPersonController.setAwardBaseBean(awardBaseBean);
                        awardKeyPersonController.setQueryKey(getQueryKey());
                        awardKeyPersonController.setFormData();
                        awardKeyPersonController.setRefreshRequired(true);
                        awardKeyPersonController.refresh();
                    }            
                    //Added for the case# COEUDEV-301 -Awd List>Scrolling thru List in Display mode-End                    
                    if(controller[index] instanceof AwardController){
                        awardController = (AwardController)controller[index];
                        awardController.prepareQueryKey(awardBaseBean);
                        awardController.setFormData(awardBaseBean);
                        awardController.setRefreshRequired(true);
                        //Added for Case 2269: Money & End Dates Tab/Panel in Awards Module - start
                        if(awardController instanceof MoneyAndEndDatesController){
                            ((MoneyAndEndDatesController)awardController).setSelectedAwardBaseBean(awardBaseBean);
                        }
                        //Added for Case 2269: Money & End Dates Tab/Panel in Awards Module - end
                        if(index == selectedIndex) {
                            awardController.refresh();
                        }
                    }else{
                        //Added for Brown's enhancement
                        awardInvestigatorController.setAwardBaseBean(awardBaseBean);
                        //Added for Brown's enhancement
                        awardInvestigatorController.setQueryKey(getQueryKey());
                        awardInvestigatorController.setFormData();
                        awardInvestigatorController.setRefreshRequired(true);
                        awardInvestigatorController.refresh();
                        
                        // Case# 2878:Special Reviews do not appear until Award is saved - Start
                        //Null checking code added
                        if(awardSpecialReviewController != null){
                            awardSpecialReviewController.setQueryKey(getQueryKey());
                            awardSpecialReviewController.setFormData(awardBaseBean);
                            awardSpecialReviewController.setRefreshRequired(true);
                            awardSpecialReviewController.refresh();
                        }
                        // Case# 2878:Special Reviews do not appear until Award is saved - End
                    }
                    //Bug Fix: 1410 End 1
                   
                }
            }
            
        }catch (CoeusException coeusException) {
            coeusException.printStackTrace();
            CoeusOptionPane.showErrorDialog(coeusException.getMessage());
        }finally {
            blockEvents(false);
        }
    }
    
    
    protected void clearOldInstance() {
        mdiForm.removeFrame(CoeusGuiConstants.AWARD_BASE_WINDOW, awardBaseBean.getMitAwardNumber());
        edu.mit.coeus.gui.menu.CoeusWindowMenu windowMenu = mdiForm.getWindowMenu();
        if( windowMenu != null ){
            windowMenu = windowMenu.removeMenuItem( title );
        }
        
    }
    
    protected void updateNewInstance() {
        char formFunctionType;
        if(getFunctionType() != DISPLAY_MODE) {
            formFunctionType = CORRECT_AWARD;
        }else {
            formFunctionType  = DISPLAY_MODE;
        }
        mdiForm.putFrame(CoeusGuiConstants.AWARD_BASE_WINDOW, awardBaseBean.getMitAwardNumber(), formFunctionType, awardBaseWindow);
        //update to handle new window menu item to the existing Window Menu.
        edu.mit.coeus.gui.menu.CoeusWindowMenu windowMenu = mdiForm.getWindowMenu();
        if( windowMenu != null ){
            windowMenu = windowMenu.addNewMenuItem( title, awardBaseWindow );
            windowMenu.updateCheckBoxMenuItemState( title, true );
        }
        mdiForm.refreshWindowMenu(windowMenu);
    }
    
    /** validates all tabpages
     * returns false if validation in any one of the tab page validation fails.
     * else returns true.
     * @throws CoeusUIException if exception occurs.
     * @return false if validation in any one of the tab page validation fails.
     * else returns true.
     */
    public boolean validate() throws CoeusUIException {
        //Validate All Tab Pages
        boolean valid = false;
        /*if(awardDetailController.validate() == false){
            tbdPnAward.setSelectedIndex(AWARD_DETAIL_TAB_INDEX);
            valid = false;
        }else if(otherHeaderController.validate() == false) {
            tbdPnAward.setSelectedIndex(OTHER_HEADER_TAB_INDEX);
            valid = false;
        }else if(awardInvestigatorController.validate() == false) {
            tbdPnAward.setSelectedIndex(INVESTIGATOR_TAB_INDEX);
            tbdPnAward.getSelectedComponent().requestFocus();
            valid = false;
        }else if(subContractController.validate() == false) {
            tbdPnAward.setSelectedIndex(SUB_CONTRACT_TAB_INDEX);
            valid = false;
        }else if(awardCommentsController.validate() == false) {
            tbdPnAward.setSelectedIndex(COMMENTS_TAB_INDEX);
            valid = false;
        //}else if(awardTermsController.validate() == false){
         //   tbdPnAward.setSelectedIndex(AWARD_TERMS_TAB_INDEX);
         //   valid = false;
        }else if(awardContactsController.validate() == false) {
            tbdPnAward.setSelectedIndex(AWARD_CONTACTS_TAB_INDEX);
            valid = false;
        }else {
             valid = true;
        }*/
        
        valid = true;
        for(int index = 0; index < controller.length; index++) {
            if(controller[index] != null && !controller[index].validate()) {
                tbdPnAward.setSelectedIndex(index);
                valid = false;
                break;
            }
        }
        
        //Select tab only for Investigator. since shotcut keys don't work for investigator
        //if tab is not selected. if this is done for other tabs the field which is
        //invalid won't receive focus.
        if(!valid && tbdPnAward.getSelectedIndex() == INVESTIGATOR_TAB_INDEX) {
            //set focus to the selected tab. so that keyboard shortcuts still works for the tab.
            EventQueue.invokeLater(new Runnable() {
                public void run() {
                    try{
                        tbdPnAward.getSelectedComponent().requestFocusInWindow();
                    }catch (NullPointerException nullPointerException) {
                        //Will occur if award is tried to closed with modifications
                        //and click yes for save required. since the form is closed after
                        //saving the award data. thread will try to select the tab which
                        //is no more valid.
                    }
                }
            });
        }//End if Investigator tab selection.
        
        //no need to do other validations if any of the above validations have failed.
        if(!valid) {
            return valid;
        }
        
        try{
            //check if cost sharing destination acount is null.if so display error msg and return.
            //this can happen when getting data from inst prop in funding prop window.
            Equals eqCostSharingDesc = new Equals("destinationAccount", null);
            CoeusVector cvCostSharing = queryEngine.executeQuery(queryKey, AwardCostSharingBean.class, eqCostSharingDesc);
            if(cvCostSharing != null && cvCostSharing.size() > 0) {
                //cost sharing with no destination account exists. display error msg.
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(OPEN_CS_2_ENTER_DEST_ACCOUNT));
                return false;
            }
            
            //check if Indirect Cost start date is null.if so display error msg and return.
            //this can happen when getting data from inst prop in funding prop window.
            Equals eqIDCStartDate = new Equals("startDate", null);
            CoeusVector cvIDCRate = queryEngine.executeQuery(queryKey, AwardIDCRateBean.class, eqIDCStartDate);
            if(cvIDCRate != null && cvIDCRate.size() > 0) {
                //Bug Fix:1577 Start
                for(int index = 0; index<cvIDCRate.size(); index++){
                    AwardIDCRateBean awardIDCRateBean =(AwardIDCRateBean)cvIDCRate.get(index);
                    if(awardIDCRateBean.getAcType() == null || !awardIDCRateBean.getAcType().equals(TypeConstants.DELETE_RECORD)){
                        //Indirect Cost with no start date exists. display error msg.
                        CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(OPEN_IDC_2_ENTER_START_DATE));
                        return false;
                    }
                }
                //Bug Fix:1577 End
            }
            
        }catch (CoeusException coeusException) {
            coeusException.printStackTrace();
        }
        // Added for COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split Infrastructure project - Start
        InvestigatorCreditSplitController invCreditSplitController =  new InvestigatorCreditSplitController(getFunctionType());
        invCreditSplitController.setFormData(awardInvestigatorController.getCreditSplitData());
        if(!invCreditSplitController.validate()){
            tbdPnAward.setSelectedIndex(INVESTIGATOR_TAB_INDEX);
            valid = false;
        }
        // Added for COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split Infrastructure project - End
        return valid;
    }
    
    /** listens to window closing event.
     * @param propertyChangeEvent propertyChangeEvent
     * @throws PropertyVetoException PropertyVetoException
     */
    public void vetoableChange(PropertyChangeEvent propertyChangeEvent) throws PropertyVetoException {
        if(closed) return ;
        boolean changed = ((Boolean) propertyChangeEvent.getNewValue()).booleanValue();
        if(propertyChangeEvent.getPropertyName().equals(JInternalFrame.IS_CLOSED_PROPERTY) && changed) {
            close();
        }
    }
    
    /** closes the Base Window and removes the reference from MDIForm.
     * @throws PropertyVetoException PropertyVetoException
     */
    public void close() throws PropertyVetoException{
        if(getFunctionType() != DISPLAY_MODE) {
            //saveFormData();
            //for investigator bug fix.
            saveFormDataBeforeClose();
            
            //Check if data modified and display save confirmation.
            if(isSaveRequired() || (awardInvestigatorController != null && awardInvestigatorController.isDataChanged())) {
                int selection = CoeusOptionPane.showQuestionDialog(coeusMessageResources.parseMessageKey(SAVE_CHANGES), CoeusOptionPane.OPTION_YES_NO_CANCEL, CoeusOptionPane.DEFAULT_YES);
                //selection = JOptionPane.showConfirmDialog(mdiForm, coeusMessageResources.parseMessageKey(SAVE_CHANGES));
                if(selection == CoeusOptionPane.SELECTION_YES) {
                    try{
                        saveAward(true);
                    }catch (CoeusUIException coeusUIException) {
                        //Validation Failed
                        throw new PropertyVetoException(EMPTY, null);
                    }
                }else if(selection == CoeusOptionPane.SELECTION_CANCEL) {
                    //Case :#3149 � Tabbing between fields does not work on others tabs - Start
                    if(tbdPnAward.getSelectedIndex() == OTHER_TAB_INDEX && awardOtherController != null && getFunctionType() != DISPLAY_MODE) {
                        CustomElementsForm customElementsForm = awardOtherController.getCustomElementsForm();
                        boolean[] lookUpAvailable = customElementsForm.getLookUpAvailable();
                        JTable customTable = customElementsForm.getTable();
                        int row = customElementsForm.getRow();
                        int column = customElementsForm.getColumn();
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
                    }
                    //Case :#3149 - End
                    throw new PropertyVetoException(EMPTY, null);
                }else if(selection == CoeusOptionPane.SELECTION_NO && getFunctionType() != NEW_AWARD) {
                    if(!releaseLock()) {
                        //Could not release lock.
                        
                        //throw new PropertyVetoException(EMPTY, null);
                        
                        /*No need to throw exception as this will not let the user to
                         close the window.
                         */
                    }
                }
            }else {
                if(!releaseLock()) {
                    //Could not release lock.
                    
                    //throw new PropertyVetoException(EMPTY, null);
                    
                    /*No need to throw exception as this will not let the user to
                     close the window.
                     */
                }
            }
        }//end if display mode
        mdiForm.removeFrame(CoeusGuiConstants.AWARD_BASE_WINDOW, awardBaseBean.getMitAwardNumber());
        queryEngine.removeDataCollection(queryKey);
        //awardBaseWindow.dispose();
        closed = true;
        //Select next Internal Frame.
        awardBaseWindow.doDefaultCloseAction();
        cleanUp();
        
        //Bug Fix:Performance Issue (Out of memory) Start 7
        System.gc();
        //Bug Fix:Performance Issue (Out of memory) End 7
    }
    
    /** displays proposal search window as modal dialog. */
    private void showProposalSearch() {
        try{
            FundingProposalSearch fundingProposalSearch = new FundingProposalSearch(mdiForm, PROPOSAL_SEARCH, FundingProposalSearch.TWO_TABS);
            fundingProposalSearch.showSearchWindow();
            
            if(fundingProposalSearch.getSelectedRow() == null || fundingProposalSearch.getSelectedRow().isEmpty()) {
                return ;
            }
            
            int selectedRow = fundingProposalSearch.getSearchResTable().getSelectedRow();
            if(selectedRow == -1)return ;
            
            proposalNumber = fundingProposalSearch.getSearchResTable().getValueAt(selectedRow, 0).toString();
            if(fundingProposalSearch.isAwardStatusToBeMadeHold()) {
                //set award status to hold.
                AwardDetailsBean awardDetailsBean = new AwardDetailsBean();
        		// JM 6-21-2012 changed HOLD status code to 7 since 6 is Award Ended at VU
                awardDetailsBean.setStatusCode(HOLD_STATUS);
                // JM END
                BeanEvent beanEvent = new BeanEvent();
                beanEvent.setBean(awardDetailsBean);
                beanEvent.setSource(this);
                fireBeanUpdated(beanEvent);
            }
            //System.out.println(proposalNumber);
            RequesterBean requesterBean = new RequesterBean();
            ResponderBean responderBean = new ResponderBean();
            requesterBean.setFunctionType('D');
            CoeusVector cvProp = new CoeusVector();
            
            AwardFundingProposalBean awardFundingProposalBean = new AwardFundingProposalBean();
            awardFundingProposalBean.setMitAwardNumber(awardBaseBean.getMitAwardNumber());
            awardFundingProposalBean.setProposalNumber(proposalNumber);
            // Added COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split Infrastructure project - Start
//            if(isIPHasNewInvestigator(proposalNumber)){
//                MessageFormat formatter = new MessageFormat("");
//                String message = formatter.format(
//                        coeusMessageResources.parseMessageKey("awardFundingInvSync_exceptionCode.1000"),"'",proposalNumber,"'");
//                int option = CoeusOptionPane.showQuestionDialog(message,
//                        CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_YES);
//                switch( option ){
//                    case JOptionPane.YES_OPTION:
                        awardFundingProposalBean.setCanSyncIPInvToAward(true);
                        awardFundingProposalBean.setCanSyncIPCreditToAward(true);
//                }
//            }else{
//                awardFundingProposalBean.setCanSyncIPCreditToAward(true);
//            }
            // Added COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split Infrastructure project - End
            cvProp.add(awardFundingProposalBean);
            
            requesterBean.setDataObject(cvProp);
            AppletServletCommunicator comm= new AppletServletCommunicator(CoeusGuiConstants.CONNECTION_URL + SERVLET,
            requesterBean);
            comm.setRequest(requesterBean);
            comm.send();
            responderBean = comm.getResponse();
            Hashtable htData = null;
            if(responderBean.isSuccessfulResponse()) {
                htData = (Hashtable)responderBean.getDataObject();
            }
            if(htData != null) {
                // Modified for COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split Infrastructure project - Start
//                updateAwardDetailsFromProposal(htData, getFunctionType());
                updateAwardDetailsFromProposal(htData, getFunctionType(),false);
                // Modified for COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split Infrastructure project - End
                //set this proposal to funding proposal
                HashMap hmSelectedProp = fundingProposalSearch.getSelectedRow();
                String propNumber, propSeqNum, propTypeDesc;
                double directCost, indirectCost;
                java.sql.Date startDate, endDate;
                int propStatusCode;
                
                // JM 5-23-2013 subcontracts from IP into display?
            	subContractController.subContractTableModel.fireTableRowsInserted(
            			subContractController.subContractTableModel.getRowCount()+1,
            			subContractController.subContractTableModel.getRowCount()+1);
                // JM END
                
                propNumber = (Utils.convertNull(hmSelectedProp.get(
                PROPOSAL_NUMBER))).trim();
                
                propSeqNum = (Utils.convertNull(hmSelectedProp.get(
                SEQUENCE_NUMBER))).trim();
                
                if (hmSelectedProp.get(TOTAL_DIRECT_COST_TOTAL)!=null) {
                    directCost = Double.parseDouble(hmSelectedProp.get(
                    TOTAL_DIRECT_COST_TOTAL).toString());
                } else {
                    directCost = 0.0;
                }
                if (hmSelectedProp.get(TOTAL_INDIRECT_COST_TOTAL) == null) {
                    indirectCost = 0.0;
                } else {
                    indirectCost = Double.parseDouble(hmSelectedProp.get(
                    TOTAL_INDIRECT_COST_TOTAL).toString());
                }
                if (hmSelectedProp.get(REQUESTED_START_DATE_TOTAL)==null) {
                    startDate = null;
                } else {
                    String strDate = hmSelectedProp.get(REQUESTED_START_DATE_TOTAL).toString();
                    //COEUSQA-1477 Dates in Search Results - Start
                    strDate = formatDateForSearchResults(strDate);
                    //java.util.Date date = (java.util.Date)simpleDateFormat.parse(dateUtils.restoreDate(strDate,DATE_SEPARATERS));
                    java.util.Date date = (java.util.Date)simpleDateFormat.parse(strDate);
                    //COEUSQA-1477 Dates in Search Results - End
                    startDate = new java.sql.Date(date.getTime());
                    //                    startDate = new java.sql.Date(((Timestamp)hmSelectedProp.get(
                    //                    REQUESTED_START_DATE_TOTAL)).getTime());
                }
                if (hmSelectedProp.get(REQUESTED_END_DATE_TOTAL)==null) {
                    endDate = null;
                }else {
                    String endDt = hmSelectedProp.get(REQUESTED_END_DATE_TOTAL).toString();
                    //COEUSQA-1477 Dates in Search Results - Start
                    endDt = formatDateForSearchResults(endDt);
                    //java.util.Date date = (java.util.Date)simpleDateFormat.parse(dateUtils.restoreDate(endDt,DATE_SEPARATERS));
                    java.util.Date date = (java.util.Date)simpleDateFormat.parse(endDt);
                    //COEUSQA-1477 Dates in Search Results - End
                    
                    endDate = new java.sql.Date(date.getTime());
                    //endDate = new java.sql.Date(((Timestamp)hmSelectedProp.get(REQUESTED_END_DATE_TOTAL)).getTime());
                }
                propStatusCode =  hmSelectedProp.get(STATUS_CODE) == null ? 0 :
                    Integer.parseInt(hmSelectedProp.get(STATUS_CODE).toString());
                    propTypeDesc = Utils.convertNull(hmSelectedProp.get(
                    PROPOSAL_TYPE_DESC));
                    
                    // create a new bean and assign values to it
                    AwardFundingProposalBean newBean = new AwardFundingProposalBean();
                    newBean.setMitAwardNumber(awardBaseBean.getMitAwardNumber());
                    newBean.setProposalNumber(propNumber);
                    //since this is done in new award and only one proposal can be selected
                    newBean.setRowId(1);
                    newBean.setSequenceNumber(awardBaseBean.getSequenceNumber());
                    newBean.setProposalSequenceNumber(Integer.parseInt(propSeqNum));
                    newBean.setProposalTypeDescription(propTypeDesc);
                    newBean.setTotalDirectCostTotal(directCost);
                    newBean.setTotalInDirectCostTotal(indirectCost);
                    newBean.setRequestStartDateTotal(startDate);
                    newBean.setRequestEndDateTotal(endDate);
                    newBean.setProposalStatusCode(propStatusCode);
                    newBean.setAcType(TypeConstants.INSERT_RECORD);
                    queryEngine.insert(queryKey, newBean);
                    
            }
        }catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    
    /**this will be called when nothing to modify and Award is closed or
     *user chooses to close award without saving changes
     */
    private boolean releaseLock() {
        //release lock
        RequesterBean requesterBean = new RequesterBean();
        if(getFunctionType() == NEW_CHILD || getFunctionType() == NEW_CHILD_COPIED) {
            String mitAwardNumber = awardBaseBean.getMitAwardNumber();
            String last = mitAwardNumber.substring(6);
            mitAwardNumber = mitAwardNumber.replaceAll(last, "-001");
            requesterBean.setDataObject(mitAwardNumber);
        }else {
            requesterBean.setDataObject(awardBaseBean.getMitAwardNumber());
        }
        
        requesterBean.setFunctionType('S');//S = To Release Lock.
        AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator(CoeusGuiConstants.CONNECTION_URL + SERVLET, requesterBean);
        appletServletCommunicator.send();
        ResponderBean responderBean = appletServletCommunicator.getResponse();
        
        //        if(responderBean == null) {
        //            //Could not contact server.
        //            CoeusOptionPane.showErrorDialog(COULD_NOT_CONTACT_SERVER);
        //            return false;
        //        }
        
        //        if(!responderBean.isSuccessfulResponse()) {
        //            CoeusOptionPane.showErrorDialog(responderBean.getMessage());
        //            return false;
        //        }//End if Not successful response
        return true;
    }
    
    /** listens to action events.
     * @param actionEvent actionEvent
     */
    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        
        //Bug Fix : Hour glass implementation - Step 1 - START
        try{
            blockEvents(true);
            //mdiForm.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            //Bug Fix : Hour glass implementation - Step 1 - END
            
            if(source.equals(awardBaseWindow.btnSave) || source.equals(awardBaseWindow.mnuItmSave)) {
                try{
                    saveAward(false);
                    
                }catch (CoeusUIException coeusUIException) {
                    //Validation Failed
                }
            }else if(source.equals(awardBaseWindow.btnNext) || source.equals(awardBaseWindow.mnuItmNext)) {
                displayNextAward();
                /*Bug Fix:1410 - to fire the terms tab in the display mode and display the data*/
                nextPreviousAction = true;
                displayOtherHeader = true;
            }else if(source.equals(awardBaseWindow.btnPrevious) || source.equals(awardBaseWindow.mnuItmPrevious)) {
                displayPreviousAward();
                /*Bug Fix:1410 - to fire the terms tab in the display mode and display the data*/
                nextPreviousAction = true;
                displayOtherHeader = true;
            }else if(source.equals(awardBaseWindow.btnMedusa) ||source.equals(awardBaseWindow.mnuItmMedusa)) {
                showMedusa();
            }else if(source.equals(awardBaseWindow.btnNotepad) || source.equals(awardBaseWindow.mnuItmNotepad)) {
                showNotepad();
            }else if(source.equals(awardBaseWindow.btnClose) || source.equals(awardBaseWindow.mnuItmClose)) {
                try{
                    close();
                }catch (PropertyVetoException propertyVetoException) {
                    //Don't do anything. this exception is thrown to stop window from closing.
                }
            }else if(source.equals(awardBaseWindow.mnuItmCostSharing)) {
                try{
                    showCostSharing();
                }catch (CoeusClientException ex){
                    CoeusOptionPane.showDialog(ex);
                    return ;
                }
            }else if(source.equals(awardBaseWindow.mnuItmSplRate)) {
                showSpecialRate();
            }else if(source.equals(awardBaseWindow.mnuItmPaymntSch)) {
                showPaymentSchedule();
            }else if(source.equals(awardBaseWindow.mnuItmApprEq)) {
                showApprovedEquipments();
            }else if(source.equals(awardBaseWindow.mnuItmIndirectCost)) {
                showIndirectCost();
            }else if(source.equals(awardBaseWindow.mnuItmApprFornTrips)) {
                showApprForeignTrip();
            }else if(source.equals(awardBaseWindow.mnuItmScienceCode)) {
                showScienceCode();
            }else if(source.equals(awardBaseWindow.mnuItmSpnsrFndngTrans)) {
                showSponsorFundingTransferred();
            }else if(source.equals(awardBaseWindow.mnuItmAwdClsOut)){
                if(getFunctionType()==NEW_AWARD){
                    moneyAndEndDatesController.saveFormData();
                }
                showAwardCloseout();
            }else if(source.equals(awardBaseWindow.mnuItmFundProps)) {
                // case 2399
            	 if (isSaveRequired()) {
            		saveFormData(); // JM 9-5-2013 only call if save is required
            	}
            	// JM END
                showFundingProps();
              //For the Coeus Enhancement case:#1799 step:8  
//            }else if(source.equals(awardBaseWindow.mnuItmSplReview)) {
//                showSpecialReview();
              //End Coeus Enhancement case:#1799 step:8  
            }else if(source.equals(awardBaseWindow.mnuItmRepReq) || source.equals(awardBaseWindow.btnRepReq)) { //Bug fix :1161
                showReportingRequirements();
            }else if(source.equals(awardBaseWindow.mnuItmInbox)) {
                showInboxDetails();
            } else if (source.equals(awardBaseWindow.mnuItmPrntAwdNotice) || source.equals(awardBaseWindow.btnPrintAwdNotice) ) {
                try {
                    if (isSaveRequired()) {
                        saveAward(false);
                    }
                    showAwardNotice();
                } catch(Exception e) {
                    if(!e.getMessage().equals("null.")) {
                    CoeusOptionPane.showErrorDialog(e.getMessage());
                    return ;
                    }
                }
            } else if (source.equals(awardBaseWindow.mnuItmPrntDeltaRep)) {
                try{
                    if(getFunctionType()!= TypeConstants.DISPLAY_MODE){
                        saveAward(false);
                    }
                    showDeltaReport();
                }catch(Exception e){
                   if(!e.getMessage().equals("null.")) {
                    CoeusOptionPane.showErrorDialog(e.getMessage());
                    return;
                    }
               }
            } else if (source.equals(awardBaseWindow.mnuItmChangePassword)) {
                showChangePassword();
            //Added for Case#3682 - Enhancements related to Delegations - Start
//            }else if(source.equals(awardBaseWindow.mnuItmPreferences)) {
//                displayUserDelegation();
            //Added for Case#3682 - Enhancements related to Delegations - End
            }else if(source.equals(awardBaseWindow.mnuItmPreferences)) {
                showPreference();
            } else if (source.equals(awardBaseWindow.mnuItmExit)) {
                exitApplication();
            }//Case 2110 Start
            else if(source.equals(awardBaseWindow.mnuItmCurrentLocks)){
                showLocksForm();
            }//Case 2110 End            
            
            //Added for Case#2214 Email enhancement - start
            else if(source.equals(awardBaseWindow.btnSendEmail)) {
                // JM 6-12-2014 repurposing for new notification system
            	//ActionValidityChecking checkValid = new ActionValidityChecking();
                //synchronized(checkValid) {
                //    //COEUSDEV-75:Rework email engine so the email body is picked up from one place
                //    checkValid.sendMail(ModuleConstants.AWARD_MODULE_CODE,0, awardBaseBean.getMitAwardNumber(), awardBaseBean.getSequenceNumber());
                //}
            	//
                String ecatHome = CoeusServerProperties.getProperty("ECAT_HOME_PAGE");
            	java.net.URI uri = new java.net.URI(ecatHome + "notifications/awards/can_send_award_notice/" + awardBaseBean.getMitAwardNumber() + "/uid=" + mdiForm.getUserId());
                Desktop dt = Desktop.getDesktop();
            	dt.browse(uri);
            	// JM END
            }
            //Case 2106 Start 1
            else if(source.equals(
            ((InvestigatorForm)awardInvestigatorController.getForm()).btnCreditSplit)){
                performCreditSplit();
            }
            //Case 2106 End 1
            //Added for Case#2136 enhancement start 1
            else if(source.equals(
            ((InvestigatorForm)awardInvestigatorController.getForm()).btnAdminType)){
                performAdminType();
            }
            //Added for Case#2136 enhancement end 1
            else if(source.equals(awardBaseWindow.btnSync) || source.equals(awardBaseWindow.mnuItmSync) ){
                awardDetailController.showSyncOptions();
            }
                             //AWARD ROUTING ENHANCEMENT STARTS
//            else if(source.equals(awardBaseWindow.mnuItmApprovalMap)){
//                showApprovalMap();
//            }
            else if(source.equals(awardBaseWindow.mnuItmApproveReject))
            {
                //The award is currently in routing is the assumption
                edu.mit.coeus.award.bean.AwardDocumentRouteBean adrb= awardBean.getLatestAwardDocumentRouteBean();
                RoutingForm routingForm = new RoutingForm(
                                    adrb,
                                    ModuleConstants.AWARD_MODULE_CODE,awardBean.getMitAwardNumber(),
                                    adrb.getRoutingDocumentNumber(), awardBean.getLeadUnitNumber(),false);
                routingForm.display();
        
            }

            else if(source.equals(awardBaseWindow.mnuItmDisclosureStatus)) {
               // awardBaseWindow.mnuItmDisclosureStatus.setEanable(true);
                showDisclosureStatus();                  
            }
            //COEUSQA 2111 STARTS
            else if(source.equals(awardBaseWindow.mnuItmRoutPrntAwdNotice)){
                RouteAwardDocumentMenuAction();
            }
            else if(source.equals(awardBaseWindow.mnuItmRoutPrntDeltaRep)){
                RouteDeltaDocumentMenuAction();
            }
            else if(source.equals(awardBaseWindow.mnuItmRoutingHistory)){
                RoutingDocumentForm routingDocumentForm=new RoutingDocumentForm(awardBean);
                routingDocumentForm.display();
                
            }
            //COEUSQA 2111 ENDS
            else {CoeusOptionPane.showInfoDialog(FUNC_NOT_IMPL);}            
            
        }
        //Case 2110 Start Current Locks
        catch(CoeusException coeusException){
           coeusException.printStackTrace();
           CoeusOptionPane.showErrorDialog(coeusException.getMessage());
        }catch(Exception exception){
            exception.printStackTrace();
            CoeusOptionPane.showErrorDialog(exception.getMessage());
        }//Case 2110 End Current Locks 
        
         //Bug Fix : Hour glass implementation - Step 2 - START
        finally {
            blockEvents(false);
            //mdiForm.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
        //Bug Fix : Hour glass implementation - Step 2 - END
        
    }
    
    //Added for Case#3682 - Enhancements related to Delegations- Start
    /*
     * Display Delegations window
     */
//    private void displayUserDelegation() {
//        userDelegationForm = new UserDelegationForm(mdiForm,true);
//        userDelegationForm.display();
//    }
//    //Added for Case#3682 - Enhancements related to Delegations - End
    
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
    
    //Case 2110 Start To get the current locks of the user
    private void showLocksForm() throws edu.mit.coeus.exception.CoeusException{
        CurrentLockForm currentLockForm = new CurrentLockForm(mdiForm,true);
        currentLockForm.display();
    }
    //Case 2110 End
    
    //Case 2106 2
    private void performCreditSplit(){
        // Commented for COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split Infrastructure project - Start
//        if(isSaveRequired() && getFunctionType() != TypeConstants.DISPLAY_MODE){
//            try {
//                // Case# 2836:Please save award message opening Cr Splt or F&A - Start
////            CoeusOptionPane.showInfoDialog(
////                coeusMessageResources.parseMessageKey("awardDetail_exceptionCode.1066"));
////            return ;
//                saveAward(false);
//            } catch (CoeusUIException ex) {
////                ex.printStackTrace();
//                return;
//            }
//            // Case# 2836:Please save award message opening Cr Splt or F&A - End
//        }
        // Commented for COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split Infrastructure project - End
        InvestigatorCreditSplitController invCreditSplitController = 
                        new InvestigatorCreditSplitController(getFunctionType());
        invCreditSplitController.setFormData(awardInvestigatorController.getCreditSplitData());
        invCreditSplitController.display();
        // Added for COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split Infrastructure project - Start
        isCreditSplitSaveRequired = invCreditSplitController.isSaveRequired();
        // Added for COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split Infrastructure project - End
        invCreditSplitController.cleanUp();
    }
    //Case 2106 2
    //Added for Case#2136 enhancement start 2
    private void performAdminType(){
        if(isSaveRequired() && getFunctionType() != TypeConstants.DISPLAY_MODE){
            CoeusOptionPane.showInfoDialog(
                coeusMessageResources.parseMessageKey("awardDetail_adminType_exceptionCode.1000"));
            return ;
        }
        
       String propNumber = null;
       try{
           CoeusVector cvData = queryEngine.getDetails(queryKey, AwardHeaderBean.class);
           if(cvData != null && !cvData.isEmpty()) {
               AwardHeaderBean  awardHeaderBean = (AwardHeaderBean)cvData.get(0);
               propNumber = awardHeaderBean.getProposalNumber();
               cvData = null;
               awardHeaderBean = null;
           }
           InvestigatorUnitAdminTypeController adminType
           = new InvestigatorUnitAdminTypeController(getFunctionType(),
           (InvCreditSplitObject)awardInvestigatorController.getCreditSplitData());
           adminType.setAwardProposalNumber(propNumber);
           adminType.setFormBaseBean(awardBaseBean);
           adminType.setFormData(awardInvestigatorController.getLeadUnitNo());
           adminType.display();
           awardInvestigatorController.fetchAdministratorData();
           adminType.cleanUp();
       }catch(CoeusException ce){
           ce.printStackTrace();
           CoeusOptionPane.showErrorDialog(ce.getMessage());
       }
    } 
    //Added for Case#2136 enhancement end 2
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
    
    // show the award close out screen
    private void showAwardCloseout(){
       /* AwardReportTermsBean awardReportTermsBean = null;
        try{
            CoeusVector cvFinalDate = queryEngine.executeQuery(queryKey, AwardReportTermsBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
            if(cvFinalDate!= null && cvFinalDate.size() > 0){
                for(int index=0; index < cvFinalDate.size(); index++){
                    awardReportTermsBean = (AwardReportTermsBean)cvFinalDate.get(index);
                }
            }
        
            if(getFunctionType() == NEW_AWARD){
                if(awardReportTermsBean!= null){
                    if(awardReportTermsBean.isFinalReport()){
                           int confirm = CoeusOptionPane.showQuestionDialog(
                                coeusMessageResources.parseMessageKey(AWARD_CLOSEOUT),
                                CoeusOptionPane.OPTION_YES_NO,
                                CoeusOptionPane.DEFAULT_YES);
                        switch(confirm){
                            case JOptionPane.YES_OPTION:
                                try{
                                    saveAward(false);
                                }catch (Exception exception){
                                    //either validation error occured or could not save Award.
                                    //should not show medusa window.
                                    return ;
                                }
                                awardCloseoutController = new AwardCloseoutController(awardBaseBean, getFunctionType());
                                awardCloseoutController.cleanUp();
                                return ;
                                //break;
                            case JOptionPane.NO_OPTION:
                                return ;
                                //break;
                        }
                    }
                }else{
                    //not a finla report. open award closeout without asking.
                    awardCloseoutController = new AwardCloseoutController(awardBaseBean, getFunctionType());
                    awardCloseoutController.cleanUp();
                    return ;
                }
        
            }
        */
        awardCloseoutController = new AwardCloseoutController(awardBaseBean, getFunctionType());
        awardCloseoutController.cleanUp();
        //  }catch (CoeusException coeusException) {
        //     coeusException.printStackTrace();
        // }
    }
    
    /** Displays the Proposal Notepad screen */
    private void showNotepad(){
        if ( isSaveRequired() ){
            int confirm = CoeusOptionPane.showQuestionDialog(
            coeusMessageResources.parseMessageKey(NOTEPAD_SAVE_CONFIRMATION),
            CoeusOptionPane.OPTION_YES_NO,
            CoeusOptionPane.DEFAULT_YES);
            switch(confirm){
                case JOptionPane.YES_OPTION:
                    try{
                        if( validate() ){
                            saveAward(false);
                        }
                    }catch (Exception exception){
                        exception.printStackTrace();
                    }
                    showNotepadWindow();
                    break;
                case JOptionPane.NO_OPTION:
                    break;
            }
        }else{
            showNotepadWindow();
        }
    }
    
    /** Supporting method to display the Proposal Notepad screen */
    private void showNotepadWindow(){
        //Check if Notepad is already opened
        CoeusInternalFrame frame = mdiForm.getFrame(CoeusGuiConstants.NOTEPAD_FRAME_TITLE);
        if(frame == null){
            ProposalAwardHierarchyLinkBean linkBean = new ProposalAwardHierarchyLinkBean();
            linkBean.setAwardNumber(awardBaseBean.getMitAwardNumber());
            linkBean.setBaseType(CoeusConstants.AWARD);
            
            ProposalNotepadForm proposalNotepadForm = new ProposalNotepadForm(linkBean, mdiForm);
            proposalNotepadForm.display();
        }else{
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
            "proposal_Notepad_exceptionCode.7116"));
        }
    }
    
    //Bug fix:Start 1048
    /** displays medusa screen. */
    private void showMedusa() {
        showMedusa(null);
    }
    
    
    private void  showMedusaWidnow(){
        showMedusaWidnow(null);
    }
    
    
    /** displays medusa screen. */
    private void showMedusa(String awardnumber) {
        if ( isSaveRequired() ){
            int confirm = CoeusOptionPane.showQuestionDialog(
            coeusMessageResources.parseMessageKey(MEDUSA_SAVE_CONFIRMATION),
            CoeusOptionPane.OPTION_YES_NO,
            CoeusOptionPane.DEFAULT_YES);
            switch(confirm){
                case JOptionPane.YES_OPTION:
                    try{
                        saveAward(false);
                    }catch (Exception exception){
                        //either validation error occured or could not save Award.
                        //should not show medusa window.
                        return ;
                    }
                    showMedusaWidnow(awardnumber);
                    break;
                case JOptionPane.NO_OPTION:
                    break;
            }
        }else{
            showMedusaWidnow(awardnumber);
        }
    }
    
    private void  showMedusaWidnow(String awardNumber){
        try{
            ProposalAwardHierarchyLinkBean linkBean = new ProposalAwardHierarchyLinkBean();
            linkBean.setBaseType(CoeusConstants.AWARD);
            if(awardNumber == null) {
                linkBean.setAwardNumber(awardBaseBean.getMitAwardNumber());
            }else {
                linkBean.setAwardNumber(awardNumber);
            }
            
            if( ( medusaDetailform = (MedusaDetailForm)mdiForm.getFrame(
            CoeusGuiConstants.MEDUSA_BASE_FRAME_TITLE))!= null ){
                if( medusaDetailform.isIcon() ){
                    medusaDetailform.setIcon(false);
                }
                linkBean.setBaseType(CoeusConstants.AWARD);
                
                if(awardNumber == null) {
                    linkBean.setAwardNumber(awardBaseBean.getMitAwardNumber());
                    medusaDetailform.setSelectedNodeId(awardBaseBean.getMitAwardNumber());
                }else {
                    linkBean.setAwardNumber(awardNumber);
                    medusaDetailform.setSelectedNodeId(awardNumber);
                }
                
                medusaDetailform.setSelected( true );
                return;
            }
            medusaDetailform = new MedusaDetailForm(mdiForm,linkBean);
            medusaDetailform.setVisible(true);
        }catch(Exception exception){
            exception.printStackTrace();
        }
    }
    //Bug Fix:End 1048
    
    private void showCostSharing() throws CoeusClientException{
        AwardCostSharingController awardCostSharingController = new AwardCostSharingController(awardBaseBean, mdiForm, getFunctionType());
        awardCostSharingController.display();
        awardCostSharingController.cleanUp();
    }
    
    private void showSpecialRate() {
        SpecialRateController specialRateController = new SpecialRateController(awardBaseBean, getFunctionType());
        specialRateController.display();
        specialRateController.cleanUp();
    }
    
    private void showPaymentSchedule() {
        AwardPaymentScheduleController awardPaymentScheduleController = new AwardPaymentScheduleController(awardBaseBean, mdiForm, getFunctionType());
        awardPaymentScheduleController.display();
        awardPaymentScheduleController.cleanUp();
    }
    
    private void showApprovedEquipments() {
        ApprEquipmentController apprEquipmentController = new ApprEquipmentController(awardBaseBean, getFunctionType());
        apprEquipmentController.display();
        apprEquipmentController.cleanUp();
    }
    
    private void showIndirectCost() {
        //check if comments tab in selected if selected update to query engine
        //so that IDC rates comments are in sync
        int selectedTabIndex = tbdPnAward.getSelectedIndex();
        if(selectedTabIndex == COMMENTS_TAB_INDEX) {
            awardCommentsController.saveFormData();
        }
        
        AwardIndirectCostController awardIndirectCostController = new AwardIndirectCostController(awardBaseBean, getFunctionType());
        awardIndirectCostController.display();
        awardIndirectCostController.cleanUp();
    }
    
    private void showApprForeignTrip() {
        if(apprForeignTripController == null) {
            apprForeignTripController = new ApprForeignTripController(awardBaseBean, getFunctionType());
        }else {
            apprForeignTripController.setFormData(awardBaseBean);
        }
        apprForeignTripController.display();
    }
    
    private void showScienceCode() {
        AwardScienceCodeController awardScienceCodeController = new AwardScienceCodeController(awardBaseBean, getFunctionType());
        awardScienceCodeController.display();
        awardScienceCodeController.cleanUp();
    }
    
    private void showSponsorFundingTransferred() {
        AwardSponsorFundingController awardSponsorFundingController = new AwardSponsorFundingController(awardBaseBean, getFunctionType());
        awardSponsorFundingController.display();
        awardSponsorFundingController.cleanUp();
    }
    
    private void showFundingProps() {
        FundingProposalsController fundingProposalsController = new FundingProposalsController(awardBaseBean, getFunctionType());
        fundingProposalsController.display();
        fundingProposalsController.cleanUp();
    }
    
    /* For the Coeus Enhancement case:#1799 start Step:9
    private void showSpecialReview() {
        AwardSpecialReviewController awardSpecialReviewController = new AwardSpecialReviewController(awardBaseBean, getFunctionType());
        awardSpecialReviewController.display();
        awardSpecialReviewController.cleanUp();
    } End Coeus Enhancement case:#1799 step:9*/
    
    private void showReportingRequirements() {
        String awardNumber = awardBaseBean.getMitAwardNumber();
        
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
            }
            else {
                //check if any reporting Requirement Window is opened and >= 3
                //Disp msg Cannot open more than 3 Award Reporting Requirement windows.
                //Please close atleast one Award Reporting Requirement window.
                boolean repReqWinOpen = isRepReqWindowOpen(awardNumber, TypeConstants.DISPLAY_MODE, true, true);
                if(repReqWinOpen) {
                    return ;
                }
                //else Check if this rep req is opened then bring it to front
                
                if(getFunctionType() == DISPLAY_MODE && canMaintainReporting()) {
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
                }//End if CORRECT_AWARD
                
                //if Modify Mode then check if Award sheet is opened in Modes M, N, E,C,P.
                //Award sheet for +s_mit_award_number + is open in modify mode.
                //Cannot open reporting requirements window in modify mode when
                //an award is being modified.\n Do you want to open reporting
                //requirements window for the selected award in display mode
                if(mode == TypeConstants.MODIFY_MODE) {
                    if(isAwardWindowOpen(awardNumber, CORRECT_AWARD, false)) {
                        //An Award is opened in Modify Mode
                        AwardBaseWindow awardBaseWindow = (AwardBaseWindow)mdiForm.getEditingFrame(CoeusGuiConstants.AWARD_BASE_WINDOW);
                        if(awardBaseWindow == null) {
                            //No Award is being modified
                        }else {
                            
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
                        }//End if else Award Modified (award = null)
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
        ReportingReqBaseWindowController reportingReqBaseWindowController;
        reportingReqBaseWindowController = new ReportingReqBaseWindowController(awardBean, mode);
        reportingReqBaseWindowController.display();
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
    
    /**
     * This method is used to display the Award Notice window
     */
    private void showAwardNotice() {
        PrintNoticeForm printNoticeForm = null;
        printNoticeForm = new PrintNoticeForm();
        printNoticeForm.setAward(awardBean);
        printNoticeForm.display();
    }
    /**
     * This method is used to display the Delta report window
     */
    private void showDeltaReport() {
        PrintDeltaReportForm printDeltaReportForm = new PrintDeltaReportForm();
        printDeltaReportForm.setAward(awardBean);
        printDeltaReportForm.display();
    }
    
   /** print Budget Hierarchy in the monyAndEndDates screen */
    private void showBudgetHierarchyReport(){
        // 4499: Unable to print the Money and End date in Display Mode - Start
//         if ( isSaveRequired() ){
//            try{
//                saveAward(false);
//                //Added for Award Print Issue-java.sql.SQL.Exception-start
//                moneyAndEndDatesController.printBudgetHierarchy();
//                //Added for Award Print Issue-java.sql.SQL.Exception-end
//         }catch (Exception exception){
//                exception.printStackTrace();
//            }
//         }
        
        if ( isSaveRequired() ){
            try {
                saveAward(false);
            } catch (CoeusUIException ex) {
                ex.printStackTrace();
                return;
                
                
            }
        }
        try {
            moneyAndEndDatesController.printBudgetHierarchy();
        } catch (CoeusException ex) {
            ex.printStackTrace();
        }
        // 4499: Unable to print the Money and End date in Display Mode - End
//         try{
//
//                moneyAndEndDatesController.printBudgetHierarchy();
//
//         }catch (Exception exception){
//                exception.printStackTrace();
//                CoeusOptionPane.showErrorDialog(exception.getMessage());
//        }
//        if ( isSaveRequired() ){
//            int confirm = CoeusOptionPane.showQuestionDialog(
//            coeusMessageResources.parseMessageKey(MONEY_AND_END_DATES_REPORT_SAVE_CONFIRMATION), 
//            CoeusOptionPane.OPTION_YES_NO,
//            CoeusOptionPane.DEFAULT_YES);
//            switch(confirm){
//                case JOptionPane.YES_OPTION:
//                    try{                       
//                        saveAward(false);
//                        moneyAndEndDatesController.printBudgetHierarchy();
//                    }catch (Exception exception){
//                        exception.printStackTrace();
//                    }
//                    
//                    break;
//                case JOptionPane.NO_OPTION:
//                    break;
//            }
//        }else{
//            try{
//
//                moneyAndEndDatesController.printBudgetHierarchy();
//
//            }catch (Exception exception){
//                exception.printStackTrace();
//                CoeusOptionPane.showErrorDialog(exception.getMessage());
//            }
//        }
    }
    
    /** print Budget modification at a new transactionId in the monyAndEndDates screen */
    private void showCurrentBudgetModificatoinReport(AwardAmountInfoBean awardAmountInfoBean){
       
        if ( isSaveRequired() ){
//            int confirm = CoeusOptionPane.showQuestionDialog(
//            coeusMessageResources.parseMessageKey(MONEY_AND_END_DATES_REPORT_SAVE_CONFIRMATION), 
//            CoeusOptionPane.OPTION_YES_NO,
//            CoeusOptionPane.DEFAULT_YES);
//            switch(confirm){
//                case JOptionPane.YES_OPTION:
                    try{                       
                        saveAward(false);
                        moneyAndEndDatesController.printCurrentBudgetModification(awardAmountInfoBean.getMitAwardNumber(), transactionId);
                    }catch (Exception exception){
                        exception.printStackTrace();
                    }                    
//                    break;
//                case JOptionPane.NO_OPTION:
//                    break;
//            }
        }else{
            try{
                if ( transactionId != null){
                    moneyAndEndDatesController.printCurrentBudgetModification(awardAmountInfoBean.getMitAwardNumber(), transactionId);
                }else{
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(NO_TRANSACTION_ID));
                }

            }catch (Exception exception){
                exception.printStackTrace();
                CoeusOptionPane.showErrorDialog(exception.getMessage());
            }
        }
    }
    
    
    //Award Budget Enhancment Start 2
    private void showDetailAwardBudget(AwardAmountInfoBean awardAmountInfoBean , Hashtable userRights){
         if ( isSaveRequired() && getFunctionType() != TypeConstants.DISPLAY_MODE){
             try {
               // Case# 3105:Need to save award before opening budget eventhough no changes have been made- Start
//             CoeusOptionPane.showInfoDialog(
//                    coeusMessageResources.parseMessageKey(AWARD_BUDGET_SAVE_CONFIRMATION));
                 saveAward(false);
                 // 4031: Unknown error while scrolling the awards list -Start
                 // awardAmountInfoBean passed by fireBeanUpdated does NOT have
                 // updated data. Getting the updated awardAmountInfoBean from
                 // quesryEngine
                 CoeusVector cvAmtDetails = queryEngine.executeQuery(queryKey,AwardAmountInfoBean.class,CoeusVector.FILTER_ACTIVE_BEANS);
                 if(cvAmtDetails != null && cvAmtDetails.size() > 0){
                     awardAmountInfoBean  = (AwardAmountInfoBean)cvAmtDetails.get(0);
                 }
                 // 4031: Unknown error while scrolling the awards list -End
             } catch (CoeusUIException ex) {
//                 ex.printStackTrace();
                 return;
             }catch (CoeusException ex) {
                 ex.printStackTrace();
             }
         }
//         }else{
            // Case# 3105:Need to save award before opening budget eventhough no changes have been made- End
             if(awardAmountInfoBean.getCurrentFundEffectiveDate() == null ){
                 CoeusOptionPane.showInfoDialog(
                    coeusMessageResources.parseMessageKey(ENTER_OBL_EFF_DATE));
                 return ;
             }else if(awardAmountInfoBean.getObligationExpirationDate() == null){
                 CoeusOptionPane.showInfoDialog(
                    coeusMessageResources.parseMessageKey(ENTER_OBL_EXP_DATE));
                 return ;
             }
             //Commented for case #2100 start
//             else if(awardAmountInfoBean.getAccountNumber() == null || 
//                      awardAmountInfoBean.getAccountNumber().trim().equals(EMPTY)){
//                          
//                          CoeusOptionPane.showInfoDialog(
//                            coeusMessageResources.parseMessageKey(ENTER_ACCOUNT_NO) + " "+awardAmountInfoBean.getMitAwardNumber());
//                          return ;
//             }
             //Commented for case #2100 end
              try{
                    //AwardBudgetSummaryController controller = new AwardBudgetSummaryController(awardAmountInfoBean, getFunctionType());
                    
                    //Get the Unit level right from the hashtable and update to the base class
                    Hashtable unitLevelRight = (Hashtable)userRights.get(KeyConstants.AWARD_BUDGET_UNIT_LEVEL_RIGHT);
                    
                    //Get the OSP level right from the hashtable and update to the base class
                    Hashtable ospLevelRight = (Hashtable)userRights.get(KeyConstants.AWARD_BUDGET_OSP_LEVEL_RIGHT);
                    
                    //Case 2067 Start 1
                    char funcType = getFunctionType();
                    //Case 2067 End 1
                        
                    //Added for the Enhancement case:1885
                    if(getFunctionType() == TypeConstants.DISPLAY_MODE){
                        boolean isAwardBudgetViewer = ((Boolean)unitLevelRight.get(KeyConstants.VIEW_AWARD_BUDGET)).booleanValue();
                        boolean isAwardBudgetModifier = ((Boolean)unitLevelRight.get(KeyConstants.MODIFY_AWARD_BUDGET)).booleanValue();
                        boolean isAwardBudgetSubmitter = ((Boolean)unitLevelRight.get(KeyConstants.SUBMIT_AWARD_BUDGET)).booleanValue();
                       
                        //Modified with case 3587: MultiCampus Enhancement
//                        boolean isAwardBudgetCreator = ((Boolean)ospLevelRight.get(KeyConstants.CREATE_AWARD_BUDGET)).booleanValue();
//                        boolean isAnyAwardBudgetSubmitter = ((Boolean)ospLevelRight.get(KeyConstants.SUBMIT_ANY_AWARD_BUDGET)).booleanValue();
//                        boolean isAwardBudgetApprover = ((Boolean)ospLevelRight.get(KeyConstants.APPROVE_AWARD_BUDGET)).booleanValue();
//                        boolean isAwardBudgetAdmin = ((Boolean)ospLevelRight.get(KeyConstants.MAINTAIN_AWARD_BUDGET_ROUTING)).booleanValue();
//                        boolean isPostAwardBudget = ((Boolean)ospLevelRight.get(KeyConstants.POST_AWARD_BUDGET)).booleanValue();
//
                        
                        boolean isAwardBudgetCreator = ((Boolean)unitLevelRight.get(KeyConstants.CREATE_AWARD_BUDGET)).booleanValue();
                        boolean isAnyAwardBudgetSubmitter = ((Boolean)unitLevelRight.get(KeyConstants.SUBMIT_ANY_AWARD_BUDGET)).booleanValue();
                        boolean isAwardBudgetApprover = ((Boolean)unitLevelRight.get(KeyConstants.APPROVE_AWARD_BUDGET)).booleanValue();
                        boolean isAwardBudgetAdmin = ((Boolean)unitLevelRight.get(KeyConstants.MAINTAIN_AWARD_BUDGET_ROUTING)).booleanValue();
                        boolean isPostAwardBudget = ((Boolean)unitLevelRight.get(KeyConstants.POST_AWARD_BUDGET)).booleanValue();
                        //3587 End
                        
                        if( isAwardBudgetSubmitter || isAwardBudgetModifier || isAwardBudgetCreator || isAnyAwardBudgetSubmitter
                        || isAwardBudgetApprover || isAwardBudgetAdmin || isPostAwardBudget){
                            
                            //Case 2067 Start 2
                            //setFunctionType(TypeConstants.MODIFY_MODE);
                            
                            funcType = TypeConstants.MODIFY_MODE;
                            //Case 2067 End 2
                            
                            try{
                                checkAndGetLock(awardBaseBean.getMitAwardNumber(),CoeusGuiConstants.getMDIForm().getUnitNumber());
                            }catch(Exception exception){
                                int option = CoeusOptionPane.showQuestionDialog("The Award Number "+awardAmountInfoBean.getRootMitAwardNumber()+ " has been locked by an other user.Do you want to open the Award Budget in the Display Mode?", 
                                CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_YES);
                                if(option == CoeusOptionPane.SELECTION_YES) {
                                    
                                    //Case 2067 Start 3
                                    //setFunctionType(TypeConstants.DISPLAY_MODE);
                                    
                                    funcType = TypeConstants.DISPLAY_MODE;
                                    //Case 2067 End 3
                                    
                                }else if(option == CoeusOptionPane.SELECTION_NO){
                                    
                                    //Case 2067 Start 4
                                    //setFunctionType(TypeConstants.DISPLAY_MODE);
                                    
                                    funcType = TypeConstants.DISPLAY_MODE;
                                    //Case 2067 End 4
                                    
                                    return;
                                }
                                
                            }
                        }else{
                            //Case 2067 Start 5
                            //setFunctionType(getFunctionType());
                            
                            funcType = getFunctionType();
                            //Case 2067 End 5
                        }
                    }
                    //End Coeus Enhancement case:#1885
                    
                    //Case 2067 Start 6
                    //AwardBudgetSummaryController controller = new AwardBudgetSummaryController(awardAmountInfoBean, getFunctionType());
                    
                    AwardBudgetSummaryController controller = new AwardBudgetSummaryController(awardAmountInfoBean, funcType);
                    //Case 2067 End 6
                    
                    controller.setUnitLevelRight(unitLevelRight);
                    controller.setOspLevelRight(ospLevelRight);
                    controller.setFormData(null);
                    controller.display();
                    controller.cleanUp();
                    if( getFunctionType() == TypeConstants.DISPLAY_MODE){
                        if(funcType == TypeConstants.MODIFY_MODE){
                            releaseLock();
                        }
                    }
                    controller = null;
                }catch (CoeusException coeusException){
                    coeusException.printStackTrace();
                }
//        }
    }
    //Award Budget Enhancment End 2
    
    /** check the lock, when Proposal special Review is made editable in DISPLAY MODE.
     *Check the lock when user saves the data
     *@param proposalNumber, Unit number
     *@throws Exception
     *@ returns boolean
     *Case #1885  - start
     */
    private boolean checkAndGetLock(String mitAwardNumber, String unitNumber)
    throws Exception{
        boolean success = false;
        String connectTo = CoeusGuiConstants.CONNECTION_URL + CoeusGuiConstants.PROPOSAL_SERVLET;
        Vector data = new Vector();
        data.add(mitAwardNumber);
        data.add(unitNumber);
        RequesterBean request = new RequesterBean();
        request.setFunctionType( GET_LOCK_FOR_AWARD );
        request.setDataObjects( data );
        AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator(CoeusGuiConstants.CONNECTION_URL + SERVLET, request);
        appletServletCommunicator.send();
        ResponderBean response = appletServletCommunicator.getResponse();
        if( response.isSuccessfulResponse() ){
            success = ((Boolean)response.getDataObject()).booleanValue();
        }else{
            throw new Exception(response.getMessage());
        }
        return success;
    }
     
    /** Case Id 1822. Open the Award F&A
     */
    private void showFNA(AwardAmountInfoBean awardAmountInfoBean){
        
//        if(getFunctionType()==NEW_ENTRY){
//            CoeusOptionPane.showInfoDialog("Please save award before opening FNA");
//        }else{
       
        if ( isSaveRequired() ){
            try {
                // Case# 2836:Please save award message opening Cr Splt or F&A - Start
//                 CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(AWARD_SAVE_BEFORE_FNA));
                saveAward(false);
                // 4031: Unknown error while scrolling the awards list -Start
                // awardAmountInfoBean passed by fireBeanUpdated does NOT have 
                // updated data. Getting the updated awardAmountInfoBean from
                // quesryEngine
                CoeusVector cvAmtDetails = queryEngine.executeQuery(queryKey,AwardAmountInfoBean.class,CoeusVector.FILTER_ACTIVE_BEANS);
                if(cvAmtDetails != null && cvAmtDetails.size() > 0){
                    awardAmountInfoBean  = (AwardAmountInfoBean)cvAmtDetails.get(0);
                }
                // 4031: Unknown error while scrolling the awards list -End
            } catch (CoeusUIException ex) {
//                ex.printStackTrace();
                return;
            } catch (CoeusException ex) {
                ex.printStackTrace();
            }
        }
//        }else{
          // Case# 2836:Please save award message opening Cr Splt or F&A - End
          boolean currentAward = false;
            try{
                CoeusVector cvDetails = queryEngine.executeQuery(queryKey,AwardDetailsBean.class,CoeusVector.FILTER_ACTIVE_BEANS);
                AwardDetailsBean awardDetailsBean  = (AwardDetailsBean)cvDetails.get(0);
                
                if(awardAmountInfoBean.getMitAwardNumber().equals(awardBaseBean.getMitAwardNumber())){
                    currentAward =true;
                }
                
                java.sql.Date  beginDate = awardDetailsBean.getAwardEffectiveDate();
                if(validateFNA(awardAmountInfoBean,beginDate)){
                    AwardAmountFNAController awardAmountFNAController= new AwardAmountFNAController(awardAmountInfoBean,getFunctionType());
                    awardAmountFNAController.setBeginDate(beginDate);
                    awardAmountFNAController.setCurrentAward(currentAward);
                    awardAmountFNAController.setQueryKey(queryKey);
                    //Added for bug fixed for case#2370 start
                    AwardBean awdBean = (AwardBean)ObjectCloner.deepCopy(awardBean);
                    awdBean.setMitAwardNumber(awardBaseBean.getMitAwardNumber());
                    awdBean.setSequenceNumber(awardBaseBean.getSequenceNumber());
                    awardAmountFNAController.setAwardBean(awardBean);
                    //Added for bug fixed for case#2370 end
                    awardAmountFNAController.setFormData(awardAmountInfoBean);
                    awardAmountFNAController.postInitComponents();
                    awardAmountFNAController.display();
                    double fnaTotalAmount = awardAmountFNAController.getTotalCost();
                    awardAmountInfoBean.setTotalFNACost(fnaTotalAmount);
                    if(awardAmountFNAController.modified){
                        
                        Vector awardData = awardAmountFNAController.getAwardData();
                        if(awardData != null && !awardData.isEmpty()) {
                            try{
                                blockEvents(true);
                                //prepare query key
                                Hashtable awdData = (Hashtable)awardData.get(0);
                                CoeusVector cvRepReqData = (CoeusVector)awardData.get(1);
                                
                                awardBean = (AwardBean)awdData.get(AwardBean.class);
                                this.awardBaseBean = (AwardBaseBean)awardBean;
                                if(getFunctionType() == NEW_AWARD || getFunctionType() == NEW_CHILD) {
                                    this.awardBaseBean.setSequenceNumber(1);
                                }

                                queryKey = this.awardBaseBean.getMitAwardNumber() + this.awardBaseBean.getSequenceNumber();
                                if(saveCount == 1 && awdData.containsKey(AwardTransactionInfoBean.class)) {
                                    CoeusVector cvAwardData = new CoeusVector();
                                    cvAwardData = (CoeusVector)awdData.get(AwardTransactionInfoBean.class);
                                    AwardTransactionInfoBean awardTransactionInfoBean = (AwardTransactionInfoBean)cvAwardData.get(0);
                                    transactionId = awardTransactionInfoBean.getTransactionId();
                                }
                                extractAwardToQueryEngine(awdData, false);
                                refresh();
//                                if(cvRepReqData != null && cvRepReqData.size() >0){
//                                    showReviewRepReq(cvRepReqData);
//                                }
                            }finally{
                                 blockEvents(false);
                            }
                        }
                    }
                    //setSaveRequired(false);
                    awardAmountFNAController.cleanUp();
                    awardAmountFNAController = null;
                }
            }catch (CoeusException ce){
                ce.printStackTrace();
            }//End catch
            catch(Exception exp){
                CoeusOptionPane.showErrorDialog(exp.getMessage());
               exp.printStackTrace();
           }
      //  }
//        }//End else
    }//End showFNA
    
    /** Case Id 1822 - Validate the F&A window before it opens
     */
    private boolean validateFNA(AwardAmountInfoBean selectedBean,java.sql.Date beginDate) throws CoeusException{       
        if(selectedBean.getMitAwardNumber().equals(awardBaseBean.getMitAwardNumber())){
            if(beginDate==null){
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(BEGIN_DATE_FOR_AWARD) +" "+selectedBean.getMitAwardNumber());
                return false;
            }else if(beginDate.after(selectedBean.getFinalExpirationDate())){
                CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(BEGIN_DATE_NOT_LATER_THAN_EXPIRATION_DATE));
                return false;
                
            }else if(selectedBean.getAnticipatedTotalAmount() <= 0.00 ){
                CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(ANTICIPATED_TOTAL_ZERO));
                return false;
            }
           }
        else{
            if(selectedBean.getAwardEffectiveDate()==null){
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(BEGIN_DATE_FOR_AWARD) +" "+selectedBean.getMitAwardNumber());
                return false;
            }else if(selectedBean.getAwardEffectiveDate().after(selectedBean.getFinalExpirationDate())){
                CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(BEGIN_DATE_NOT_LATER_THAN_EXPIRATION_DATE));
                return false;
                
            }else if(selectedBean.getAnticipatedTotalAmount() <= 0.00 ){
                CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(ANTICIPATED_TOTAL_ZERO));
                return false;
            }
        }
        return true;
    }
    
    
    
    /** Checks if data in QueryEngine is modifed and needs to be saved.
     * returns false if nothing to save.
     * else returns true.
     * @return false if nothing to save.
     * else returns true.
     */
    public boolean isSaveRequired() {
        
        //Bug Fix : 1009 - Asks Save Required in Display Mode  - START
        if(getFunctionType() == DISPLAY_MODE) {
            return false;
        }
        //Bug Fix : 1009 - Asks Save Required in Display Mode  - END
        
        //Save Data for the currently selected Tab
        int selectedTabIndex = tbdPnAward.getSelectedIndex();
        /*switch (selectedTabIndex){
            case AWARD_DETAIL_TAB_INDEX:
                awardDetailController.saveFormData();
                break;
            case OTHER_HEADER_TAB_INDEX:
                otherHeaderController.saveFormData();
                break;
            case SUB_CONTRACT_TAB_INDEX:
                subContractController.saveFormData();
                break;
            case COMMENTS_TAB_INDEX:
                awardCommentsController.saveFormData();
                break;
            case INVESTIGATOR_TAB_INDEX:
                awardInvestigatorController.saveFormData();
                break;
            case AWARD_TERMS_TAB_INDEX:
                //awardTermsController.saveFormData();
                break;
            case AWARD_CONTACTS_TAB_INDEX:
                awardContactsController.saveFormData();
                break;
        }*/
        
        try{
            //Added For COEUSQA-2383 - Two Lead Unit appear for an award Scenario 2 & 3 Start
            if(selectedTabIndex != INVESTIGATOR_TAB_INDEX){
            //Added For COEUSQA-2383 - Two Lead Unit appear for an award Scenario 2 & 3 Start
                controller[selectedTabIndex].saveFormData();
            }
        }catch (CoeusException coeusException) {
            coeusException.printStackTrace();
            CoeusOptionPane.showErrorDialog(coeusException.getMessage());
            return true;
        }
        
        Enumeration enumeration =  queryEngine.getKeyEnumeration(queryKey);
        
        Equals eqInsert = new Equals("acType", TypeConstants.INSERT_RECORD);
        Equals eqUpdate = new Equals("acType", TypeConstants.UPDATE_RECORD);
        Equals eqDelete = new Equals("acType", TypeConstants.DELETE_RECORD);
        
        Or insertOrUpdate = new Or(eqInsert, eqUpdate);
        Or insertOrUpdateOrDelete = new Or(insertOrUpdate, eqDelete);
        
        Object key;
        CoeusVector data;
        boolean awardModified = false;
        try{
            while(enumeration.hasMoreElements()) {
                key = enumeration.nextElement();
                //case 2796: Sync to parent
                if(!(key instanceof Class) ||
                        key.equals(ValidRatesBean.class) ||
                        key.equals(AwardCustomDataBean.class)){
                    continue;
                }
                //2796 End
                data = queryEngine.executeQuery(queryKey, (Class)key, insertOrUpdateOrDelete);
                if(! awardModified) {
                    if(data != null && data.size() > 0) {
                        awardModified = true;
                        break;
                    }
                }
            }
            //Custom elements must call isDataChanged to check saverequired.
            //Done with case 2796 to avoid save confirmation even if data is not modified.
            if(awardOtherController != null && awardOtherController.isDataChanged()){
                awardModified = true;
            }
            //2796 End
            //save reqd check for Award Terms
            if(!awardModified) {
                String termsKey[] = {KeyConstants.EQUIPMENT_APPROVAL_TERMS, KeyConstants.INVENTION_TERMS,
                KeyConstants.PRIOR_APPROVAL_TERMS, KeyConstants.PROPERTY_TERMS, KeyConstants.PUBLICATION_TERMS,
                KeyConstants.REFERENCED_DOCUMENT_TERMS, KeyConstants.RIGHTS_IN_DATA_TERMS,
                KeyConstants.SUBCONTRACT_APPROVAL_TERMS, KeyConstants.TRAVEL_RESTRICTION_TERMS};
                for(int index = 0; index < termsKey.length; index++) {
                    data = queryEngine.executeQuery(queryKey, termsKey[index], insertOrUpdateOrDelete);
                    if(! awardModified) {
                        if(data != null && data.size() > 0) {
                            awardModified = true;
                            break;
                        }
                    }
                }//end for
            }//end if award not modified
            
        }catch (CoeusException coeusException) {
            coeusException.printStackTrace();
        // 4031: Unknown error while scrolling the awards list -Start
        }catch (Exception exception) {
            exception.printStackTrace();
            awardModified = true;
        }
        // 4031: Unknown error while scrolling the awards list - End
        // Added for COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split Infrastructure project - Start
        if(!awardModified){
            awardModified = isCreditSplitSaveRequired();
        }
        // Added for COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split Infrastructure project - End
        return awardModified;
    }
    
    /** cleans up instance variables.
     * i.e. sets instance variables to null.
     */
    public void cleanUp() {
        //Bug Fix:Performance Issue (Out of memory) Start 8
        awardBean = null;
        syncController = null;//2796
        //removing listeners
        removeBeanUpdatedListener(this, AwardHeaderBean.class);
        removeBeanUpdatedListener(this, AwardAmountInfoBean.class);
        
        scrPnMoneyEndDates.remove(controller[MONEY_END_DATES_TAB_INDEX].getControlledUI());
        scrPnMoneyEndDates = null;
        scrPnReport.remove(awardReportsController.getControlledUI());
        scrPnReport = null;
        
        scrPnDetails.remove(awardDetailController.getControlledUI());
        scrPnDetails = null;
        
        if(scrPnOtherHeader != null){
            scrPnOtherHeader.remove(otherHeaderController.getControlledUI());
            scrPnOtherHeader = null;
        }
   
        if(scrPnComments != null){
            scrPnComments.remove(awardCommentsController.getControlledUI());
            scrPnComments = null;
        }
        
        if(scrPnInvestigator != null){
            scrPnInvestigator.remove(awardInvestigatorController.getControlledUI());
            scrPnInvestigator = null;
        }
        // 3823: Key Person Records Needed in Inst Proposal and Award - Start
        if(scrPnKeyPerson != null){
            scrPnKeyPerson.remove(awardKeyPersonController.getControlledUI());
            scrPnKeyPerson = null;
        }
        // 3823: Key Person Records Needed in Inst Proposal and Award - End
        if(scrPnTerms != null){
            scrPnTerms.remove(awardTermsController.getControlledUI());
            scrPnTerms = null;
        }
        
        if(scrPnContacts != null){
            scrPnContacts.remove(awardContactsController.getControlledUI());
            scrPnContacts = null;
        }
        
        if(scrPnOther != null){
            scrPnOther.remove(awardOtherController.getControlledUI());
            scrPnOther = null;
        }
        
       if(subContractController != null){
           if(subContractController.getControlledUI() != null)
//               System.out.println("Sub Contract Not Null");
            scrPnSubContract.remove(subContractController.getControlledUI());
            scrPnSubContract = null;
        }
        
       // JM 9-26-2014 added Restrictions tab
       if(scrPnAwardRestrictions != null){
    	   scrPnAwardRestrictions.remove(awardRestrictionsController.getControlledUI());
    	   scrPnAwardRestrictions = null;
       }  
       
       // JM 11-21-2011 added Centers tab
       if(scrPnAwardCenters != null){
    	   scrPnAwardCenters.remove(awardCentersController.getControlledUI());
    	   scrPnAwardCenters = null;
       }      
        
        if(awardInvestigatorController!=null){
            awardInvestigatorController.cleanUp();
        }
        
//        if(otherHeaderController != null){
//           otherHeaderController.cleanUp();
//           otherHeaderController = null;
//        }
        
        for(int index = 0; index < controller.length ; index++) {
            if(controller[index] != null) {
                if(controller[index] instanceof AwardController) {
                    //Added since all other tabs was listening to this bean event
                    if(controller[index] instanceof OtherHeaderController){
                        continue ;
                    }
                    ((AwardController)controller[index]).cleanUp();
                }
                controller[index] = null;
                
            }
        }
        
        if(awardCommentsController != null){
//            System.out.println("awardCommentsController not null");
            awardCommentsController.cleanUp();
            awardCommentsController = null;
        }
        
        // Added for case# 2800 - Award  Attachments - Start
        if(scrPnAwardDocuments != null){
            scrPnAwardDocuments.remove(awardDocumentController.getControlledUI());
            scrPnAwardDocuments = null;
        }
        // Added for case# 2800 - Award  Attachments - End
            
        if(otherHeaderController != null){
           otherHeaderController.cleanUp();
           otherHeaderController = null;
        }
        
        
        
        awardInvestigatorController = null;
      
        medusaDetailform = null;
        otherHeaderController = null;
        awardDetailController = null;
        moneyAndEndDatesController = null;
        awardReportsController = null;
        awardTermsController = null;
        
        subContractController = null;
        awardCommentsController = null;
        awardContactsController = null;
        awardOtherController = null;
        apprForeignTripController = null;
        awardCloseoutController = null;
        
        awardBaseWindow.cleanUp();
        awardBaseWindow = null;
        
        // Added for case# 2800 - Award  Attachments - Start
        awardDocumentController = null;
        // Added for case# 2800 - Award  Attachments - End
        
        // 3823: Key Person Records Needed in Inst Proposal and Award
        awardKeyPersonController = null;
        
        if(controller[1] != null){
            ((OtherHeaderController)controller[1]).cleanUp();
        }
        
        controller[1] = null;
        
//        mdiForm.removeFrame(CoeusGuiConstants.AWARD_BASE_WINDOW, awardBaseBean.getMitAwardNumber());
        mdiForm = null;
        coeusMessageResources = null;
        
        //apprForeignTripController.cleanUp();
        basePanel.remove(tbdPnAward);
        
        tbdPnAward = null;
        awardBaseBean = null;
        awardBaseContainer.remove(basePanel);
        basePanel = null;
        awardBaseContainer = null;
        //Bug Fix:Performance Issue (Out of memory) End 8
    }
    
    private void displayNextAward() {
        BeanEvent beanEvent = new BeanEvent();
        beanEvent.setBean(new AwardBean());
        beanEvent.setSource(this);
        beanEvent.setMessageId(SHOW_NEXT_AWARD);
        fireBeanUpdated(beanEvent);
        
    }
    
    private void displayPreviousAward() {
        BeanEvent beanEvent = new BeanEvent();
        beanEvent.setBean(new AwardBean());
        beanEvent.setSource(this);
        beanEvent.setMessageId(SHOW_PREV_AWARD);
        fireBeanUpdated(beanEvent);
    }
    
    void setEnableNext(boolean enable) {
        awardBaseWindow.btnNext.setEnabled(enable);
    }
    
    void setEnablePrevious(boolean enable) {
        awardBaseWindow.btnPrevious.setEnabled(enable);
    }
    
    public void beanUpdated(BeanEvent beanEvent) {
        Controller source = beanEvent.getSource();
        if(source.getClass().equals(AwardDetailController.class)) {
            //update flag to display Other Header.
            AwardHeaderBean awardHeaderBean = (AwardHeaderBean)beanEvent.getBean();
            if(awardHeaderBean.getAwardTypeCode()!=0){
                displayOtherHeader = true;
            }else{
                displayOtherHeader = false;
            }
        }
        else if(source.getClass().equals(MoneyAndEndDatesController.class)) {
            if (beanEvent.getMessageId()== SHOW_MEDUSA){
                //show Medusa
                //Bug Fix: Start 1048
                AwardAmountInfoBean awardAmountInfoBean = (AwardAmountInfoBean)beanEvent.getBean();
                showMedusa(awardAmountInfoBean.getMitAwardNumber());
                //Bug Fix: End 1048
                
            }else if(beanEvent.getMessageId()== REPORT_BUDGET_HIERARCHY){
                showBudgetHierarchyReport();
            }else if (beanEvent.getMessageId()== REPORT_MODIFICATION){
                AwardAmountInfoBean awardAmountInfoBean = (AwardAmountInfoBean)beanEvent.getBean();
                showCurrentBudgetModificatoinReport(awardAmountInfoBean);
            }
            
            //Award Budget Enhancment Start 3
            else if(beanEvent.getMessageId()== SHOW_AWARD_BUDGET){
                AwardAmountInfoBean awardAmountInfoBean = (AwardAmountInfoBean)beanEvent.getBean();
                
                //Fix for locking bug Start
                //Hashtable userRights = (Hashtable)beanEvent.getObject();
                //showDetailAwardBudget(awardAmountInfoBean , userRights);
                CoeusVector cvData = (CoeusVector)beanEvent.getObject();
                
                Hashtable userRights = (Hashtable)cvData.get(0);
                String awardNumber = (String)cvData.get(1);
                
                if(awardNumber.equals(awardBaseBean.getMitAwardNumber())){
                    showDetailAwardBudget(awardAmountInfoBean , userRights);
                }
                //Fix for locking bug End 
            }
            //Award Budget Enhancment End 3
            
            // Case Id 1822 Start 
            else if(beanEvent.getMessageId()== SHOW_AWARD_FNA){
                 AwardAmountInfoBean awardAmountInfoBean = (AwardAmountInfoBean)beanEvent.getBean();
                 showFNA(awardAmountInfoBean);
            }
            // Case Id 1822 End
        }
    }
    
    public void stateChanged(ChangeEvent changeEvent) {
        //save selected tabs FormData before Calling Refresh.
        /*switch (selectedTabIndex) {
            case AWARD_DETAIL_TAB_INDEX:
                awardDetailController.saveFormData();
                break;
            case OTHER_HEADER_TAB_INDEX:
                if(displayOtherHeader) {
                    otherHeaderController.saveFormData();
                }
                break;
            case SUB_CONTRACT_TAB_INDEX:
                subContractController.saveFormData();
                break;
            case COMMENTS_TAB_INDEX:
                awardCommentsController.saveFormData();
                break;
            case INVESTIGATOR_TAB_INDEX:
                awardInvestigatorController.saveFormData();
            case AWARD_TERMS_TAB_INDEX:
                //awardTermsController.saveFormData();
            case AWARD_CONTACTS_TAB_INDEX:
                awardContactsController.saveFormData();
        }*/
        try{
            
            if(getFunctionType() != DISPLAY_MODE) {
                //save data for earlier selected tab
                if(selectedTabIndex == OTHER_HEADER_TAB_INDEX){
                    if(displayOtherHeader) {
                        controller[selectedTabIndex].saveFormData();
                    }
                //Fix for issue with unit updation for which user doesnt have right.
                //Added with COEUSDEV 253
                }else if(selectedTabIndex!= INVESTIGATOR_TAB_INDEX
                        && selectedTabIndex!= SPECIAL_REVIEW_INDEX){
                    controller[selectedTabIndex].saveFormData();
                }
                
            }
        }catch (CoeusException coeusException) {
            coeusException.printStackTrace();
            CoeusOptionPane.showErrorDialog(coeusException.getMessage());
            tbdPnAward.setSelectedIndex(selectedTabIndex);
        }
        //Refresh Data
        selectedTabIndex = tbdPnAward.getSelectedIndex();
        
        if(controller[selectedTabIndex] == null) {
            loadTab(selectedTabIndex);
            //Bug Fix:Performance Issue (Out of memory) Start 9
            //Wrote a new method for loading the tab
            
            //tbdPnAward.setComponentAt(selectedTabIndex, new JScrollPane(controller[selectedTabIndex].getControlledUI()));
            
            setTabComponent(selectedTabIndex);
            
            //Bug Fix:Performance Issue (Out of memory) End 9
        }
        
        //if in display mode no need to refresh just return
        /*Bug Fix:1410 - to fire the terms tab in the display mode and display the data*/
        if(getFunctionType() == DISPLAY_MODE && !nextPreviousAction ) {
        //if(getFunctionType() == DISPLAY_MODE) {
            return ;
        }
        
        switch (selectedTabIndex) {
            case AWARD_DETAIL_TAB_INDEX:
                awardDetailController.refresh();
                break;
            case OTHER_HEADER_TAB_INDEX:
                if(! displayOtherHeader) {
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(SELECT_AWARD_TYPE_FIRST));
                    tbdPnAward.setSelectedIndex(AWARD_DETAIL_TAB_INDEX);
// JM 6-21-2011 added logic to highlight award type field when this error is thrown  
                    awardDetailController.awardDetailForm.cmbAwardType.grabFocus();
// END
                }else {
                    otherHeaderController.refresh();
                }
                break;
            case MONEY_END_DATES_TAB_INDEX:
                
                moneyAndEndDatesController.refresh();
                break;
            case SUB_CONTRACT_TAB_INDEX:
                subContractController.refresh();
                break;
            case COMMENTS_TAB_INDEX:
                awardCommentsController.refresh();
                break;
            //For the Coeus Enhancement case:#1799 start step:10    
            case SPECIAL_REVIEW_INDEX:
                awardSpecialReviewController.refresh();
                break;
            //End Coeus Enhancement case:#1799 start step:10    
            case INVESTIGATOR_TAB_INDEX:
                awardInvestigatorController.refresh();
                break;
            // 3823: Key Person Records Needed in Inst Proposal and Award - Start
// JM 11-22-2011 key person tab not used
//            case KEY_PERSON_TAB_INDEX:
//                awardKeyPersonController.refresh();
//                break;
//            // 3823: Key Person Records Needed in Inst Proposal and Award - End  
// END JM
            case AWARD_TERMS_TAB_INDEX:
                awardTermsController.refresh();
                break;
            case AWARD_CONTACTS_TAB_INDEX:
                awardContactsController.refresh();
                break;
                
            // Added for case# 2800 - Award  Attachments - Start
            case AWARD_DOCUMENT_TAB_INDEX:
               awardDocumentController.refresh();
                break;
            // Added for case# 2800 - Award  Attachments - End
            case AWARD_REPORTS_TAB_INDEX:
                awardReportsController.refresh();
                break;
            //Case :#3149 � Tabbing between fields does not work on others tabs - Start
            case OTHER_TAB_INDEX:
                if(tbdPnAward.getSelectedIndex() == OTHER_TAB_INDEX && count != 0 && awardOtherController != null && getFunctionType() != DISPLAY_MODE){
                    awardOtherController.getCustomElementsForm().setTabFocus();
                    awardOtherController.getCustomElementsForm().setTableFocus();
                }
                break;
            //Case :#3149 - End
        }
    }
    
    private void loadTab(int index) {
        //lazy loading tab controller and data - START
        switch (index) {
            case AWARD_DETAIL_TAB_INDEX:
                //Bug Fix:Performance Issue (Out of memory) Start 10
                //Earlier no check - if instace is created or not.
                if(awardDetailController == null){
                    awardDetailController = new AwardDetailController(awardBaseBean, getFunctionType());
                    awardDetailController.setBaseController(this);//2796
                    controller[index] = awardDetailController;
                }
                //Bug Fix:Performance Issue (Out of memory) End 10
                
                break;
            case OTHER_HEADER_TAB_INDEX:
                otherHeaderController = new OtherHeaderController(awardBaseBean, getFunctionType());
                controller[index] = otherHeaderController;
                break;
            case MONEY_END_DATES_TAB_INDEX:
                
                //Bug Fix:Performance Issue (Out of memory) Start 11
                //Earlier no check - if instace is created or not. 
                if(moneyAndEndDatesController == null){
                    moneyAndEndDatesController = new MoneyAndEndDatesController(awardBaseBean, getFunctionType());
                    moneyAndEndDatesController.setFormData(awardBaseBean);
                    controller[index] = moneyAndEndDatesController;
                }
                //Bug Fix:Performance Issue (Out of memory) End 11
                
                break;
            case SUB_CONTRACT_TAB_INDEX:
                subContractController = new SubContractController(awardBaseBean, getFunctionType());
                controller[index] = subContractController;
                break;
            case AWARD_REPORTS_TAB_INDEX:
                
                //Bug Fix:Performance Issue (Out of memory) Start 12
                //Earlier no check - if instace is created or not.
                if(awardReportsController == null){
                    awardReportsController = new AwardReportsController(awardBaseBean,  getFunctionType());
                    awardReportsController.setBaseController(this);//2796
                    controller[index] = awardReportsController;
                }
                //Bug Fix:Performance Issue (Out of memory) End 12
                
                break;
            case COMMENTS_TAB_INDEX:
                awardCommentsController = new AwardCommentsController(awardBaseBean, getFunctionType());
                awardCommentsController.setBaseController(this);//2796
                controller[index] = awardCommentsController;
                break;
            //For the Coeus Enhancement case:#1799 start step:11    
            case SPECIAL_REVIEW_INDEX:
                awardSpecialReviewController = new AwardSpecialReviewController(awardBaseBean,getFunctionType());
                controller[index] = awardSpecialReviewController;
                break;
           //End Coeus Enhancement case:#1799 step:11     
            case INVESTIGATOR_TAB_INDEX:
                awardInvestigatorController = new AwardInvestigatorController(awardBaseBean, queryKey);
                awardInvestigatorController.setFunctionType(getFunctionType());
                awardInvestigatorController.setBaseController(this);//2796
                controller[index] = awardInvestigatorController;

                //Case 2106 Start 3
                ((InvestigatorForm)awardInvestigatorController.getForm()).btnCreditSplit.addActionListener(this);
                //Case 2106 End 3
                //Added for Case#2136 enhancement start 3
                ((InvestigatorForm)awardInvestigatorController.getForm()).btnAdminType.addActionListener(this);
                //Added for Case#2136 enhancement end 3
                break;
// JM 11-22-2011 key person tab not used
//            // 3823: Key Person Records Needed in Inst Proposal and Award - Start
//            case KEY_PERSON_TAB_INDEX:
//                awardKeyPersonController = new AwardKeyPersonController(awardBaseBean, queryKey, getFunctionType());
//                controller[index] = awardKeyPersonController;
//                awardKeyPersonController.setKeyPersonTabIndex(index);
//                break;
//            // 3823: Key Person Records Needed in Inst Proposal and Award - End
// END JM
            case AWARD_TERMS_TAB_INDEX:
                awardTermsController = new AwardTermsController(awardBaseBean, getFunctionType());
                awardTermsController.setBaseController(this);//2796
                controller[index] = awardTermsController;
                break;
            case AWARD_CONTACTS_TAB_INDEX:
                awardContactsController = new AwardContactsController(awardBaseBean, mdiForm, getFunctionType());
                awardContactsController.setFunctionType(getFunctionType());
                awardContactsController.setBaseController(this);//2796
                controller[index] = awardContactsController;
                break;
            
            // Added for case# 2800 - Award  Attachments - Start
            case AWARD_DOCUMENT_TAB_INDEX:     
                awardDocumentController = new AwardDocumentController(awardBaseBean, mdiForm, getFunctionType());
                controller[index] = awardDocumentController;
                break;
           // Added for case# 2800 - Award  Attachments - End
                
            // JM 9-26-2014 added Restrictions tab
            case RESTRICTIONS_TAB_INDEX:
                awardRestrictionsController = new AwardRestrictionsController(awardBaseBean, getFunctionType());
                controller[index] = awardRestrictionsController;
                break;
                
            // JM 11-22-2011 added Centers tab
            case CENTERS_TAB_INDEX:
                awardCentersController = new AwardCentersController(awardBaseBean);;
                controller[index] = awardCentersController;
                break;
                
            case OTHER_TAB_INDEX:
                awardOtherController = new AwardOtherController(awardBaseBean, getFunctionType());
                controller[index] = awardOtherController;
                //Case :#3149 � Tabbing between fields does not work on others tabs - Start
                if(getFunctionType() != DISPLAY_MODE){
                    if(tbdPnAward.getSelectedIndex() == OTHER_TAB_INDEX && count != 0 && awardOtherController != null){
                        // awardOtherController.getCustomElementsForm().setTabFocus();
                        awardOtherController.getCustomElementsForm().setTableFocus();
                    }
                    tbdPnAward.addMouseListener(new MouseAdapter() {
                        public void mouseClicked(MouseEvent e) {
                            if(tbdPnAward.getSelectedIndex() == OTHER_TAB_INDEX){
                                count = 1;
                                awardOtherController.getCustomElementsForm().setTableFocus();
                            }
                        }
                    });
                }
                // Case :#3149 - End
                break;
                

        }
        //lazy loading tab controller and data - END
    }

    //Bug Fix:Performance Issue (Out of memory) Start 13
    /*
     *Method for setting the tabs
     */
    private void setTabComponent(int selectedTabIndex){
        
        switch (selectedTabIndex) {
            case AWARD_DETAIL_TAB_INDEX:
                scrPnDetails.remove(awardDetailController.getControlledUI());
                scrPnDetails = null;
                scrPnDetails = new JScrollPane(awardDetailController.getControlledUI());
                tbdPnAward.setComponentAt(selectedTabIndex,scrPnDetails);
                //System.out.println("Selected Tab:AWARD_DETAIL_TAB_INDEX");
                break;
            case OTHER_HEADER_TAB_INDEX:
                scrPnOtherHeader = new JScrollPane(otherHeaderController.getControlledUI());;
                tbdPnAward.setComponentAt(selectedTabIndex,scrPnOtherHeader);
                //System.out.println("Selected Tab:OTHER_HEADER_TAB_INDEX");
                break;
            case MONEY_END_DATES_TAB_INDEX:
                scrPnMoneyEndDates.remove(moneyAndEndDatesController.getControlledUI());
                scrPnMoneyEndDates = null;
                scrPnMoneyEndDates = new JScrollPane(moneyAndEndDatesController.getControlledUI());
                tbdPnAward.setComponentAt(selectedTabIndex,scrPnMoneyEndDates);
                //System.out.println("Selected Tab:MONEY_END_DATES_TAB_INDEX");
                break;
            case SUB_CONTRACT_TAB_INDEX:
                scrPnSubContract = new JScrollPane(subContractController.getControlledUI());;
                tbdPnAward.setComponentAt(selectedTabIndex,scrPnSubContract);
                //System.out.println("Selected Tab:SUB_CONTRACT_TAB_INDEX");
                break;
            case AWARD_REPORTS_TAB_INDEX:
                scrPnReport.remove(awardReportsController.getControlledUI());
                scrPnReport = null;
                scrPnReport = new JScrollPane(awardReportsController.getControlledUI());
                tbdPnAward.setComponentAt(selectedTabIndex,scrPnReport);
                //System.out.println("Selected Tab:AWARD_REPORTS_TAB_INDEX");
                break;
            case COMMENTS_TAB_INDEX:
                scrPnComments = new JScrollPane(awardCommentsController.getControlledUI());;
                tbdPnAward.setComponentAt(selectedTabIndex,scrPnComments);
                //System.out.println("Selected Tab:COMMENTS_TAB_INDEX");
                break;
            //For the Coeus Enhancement case:#1799 start step:12       
            case SPECIAL_REVIEW_INDEX:
                 scrPnSpecialReview = new JScrollPane(awardSpecialReviewController.getControlledUI());;
                 tbdPnAward.setComponentAt(selectedTabIndex,scrPnSpecialReview);
                 break;
             //End Coeus Enhancement case:#1799 step:12       
            case INVESTIGATOR_TAB_INDEX:
                scrPnInvestigator = new JScrollPane(awardInvestigatorController.getControlledUI());;
                tbdPnAward.setComponentAt(selectedTabIndex,scrPnInvestigator);
                break;
// JM 11-22-2011 key person tab not used
//            // 3823: Key Person Records Needed in Inst Proposal and Award - Start    
//            case KEY_PERSON_TAB_INDEX:
//                JPanel pnlKeyPer = new JPanel(new FlowLayout(FlowLayout.LEFT));
//                pnlKeyPer.add(awardKeyPersonController.getControlledUI());
//                scrPnKeyPerson = new JScrollPane(pnlKeyPer);
//                tbdPnAward.setComponentAt(selectedTabIndex,scrPnKeyPerson);
//                break;
//            // 3823: Key Person Records Needed in Inst Proposal and Award - End    
// END JM
            case AWARD_TERMS_TAB_INDEX:
                scrPnTerms = new JScrollPane(awardTermsController.getControlledUI());;
                tbdPnAward.setComponentAt(selectedTabIndex,scrPnTerms);
                //System.out.println("Selected Tab:AWARD_TERMS_TAB_INDEX");
                break;
            case AWARD_CONTACTS_TAB_INDEX:
                scrPnContacts = new JScrollPane(awardContactsController.getControlledUI());;
                tbdPnAward.setComponentAt(selectedTabIndex,scrPnContacts);
                //System.out.println("Selected Tab:AWARD_CONTACTS_TAB_INDEX");
                break;
            
             // Added for case# 2800 - Award  Attachments - Start
            case AWARD_DOCUMENT_TAB_INDEX:
                scrPnAwardDocuments = new JScrollPane(awardDocumentController.getControlledUI());
                tbdPnAward.setComponentAt(selectedTabIndex,scrPnAwardDocuments);
                break;
            // Added for case# 2800 - Award  Attachments - End            
            // JM 9-26-2014 added Restrictions tab
            case RESTRICTIONS_TAB_INDEX:
                scrPnAwardRestrictions = new JScrollPane(awardRestrictionsController.getControlledUI());;
                tbdPnAward.setComponentAt(selectedTabIndex,scrPnAwardRestrictions);
                break;
            // JM 11-21-2011 added Centers tab
            case CENTERS_TAB_INDEX:
                scrPnAwardCenters = new JScrollPane(awardCentersController.getControlledUI());;
                tbdPnAward.setComponentAt(selectedTabIndex,scrPnAwardCenters);
                break;
            case OTHER_TAB_INDEX:
                scrPnOther = new JScrollPane(awardOtherController.getControlledUI());;
                tbdPnAward.setComponentAt(selectedTabIndex,scrPnOther);
                //System.out.println("Selected Tab:OTHER_TAB_INDEX");
                break;
        }
        //rdias - UCSD's coeus personalization - Overwrite comp in a tab or a form
        persnref.customize_Form(tbdPnAward.getComponentAt(selectedTabIndex),
        		CoeusGuiConstants.AWARD_FRAME_TITLE);
    }//End setTabComponent
    //Bug Fix:Performance Issue (Out of memory) End 13
    
    /**
     * extracts all investigator units into a vector and stores it in Query Engine.
     */
    private void extractInvestigatorUnits() {
        CoeusVector cvInvestigators, cvInvestigatorUnits, cvTempUnits;
        try{
            cvInvestigators = queryEngine.getDetails(queryKey, AwardInvestigatorsBean.class);
            if(cvInvestigators == null || cvInvestigators.size() == 0) return ;
            
            int size = cvInvestigators.size();
            cvInvestigatorUnits = new CoeusVector();
            AwardInvestigatorsBean awardInvestigatorsBean;
            
            //Extract the Sequnce number to instance variable.
//            awardInvestigatorsBean = (AwardInvestigatorsBean)cvInvestigators.get(0);
//            invgtrSeqNoFrmSrv = awardInvestigatorsBean.getSequenceNumber();
            
            for(int index = 0; index < size; index++) {
                awardInvestigatorsBean = (AwardInvestigatorsBean)cvInvestigators.get(index);
                cvTempUnits = awardInvestigatorsBean.getInvestigatorUnits();
                if(cvTempUnits != null && cvTempUnits.size() > 0) {
                    cvInvestigatorUnits.addAll(cvTempUnits);
                }
            }
            queryEngine.addCollection(queryKey, AwardUnitBean.class, cvInvestigatorUnits);
            
        }catch (CoeusException coeusException) {
            coeusException.printStackTrace();
        }
    }
    
    /**
     * Integrate all investigator units into a vector and stores it in Query Engine.
     */
    private void integrateInvestigatorUnits(Hashtable htProposalData) {
        CoeusVector cvInvestigators, cvUnits, cvTempUnits;
        cvInvestigators = (CoeusVector)htProposalData.get(AwardInvestigatorsBean.class);
        cvUnits = (CoeusVector)htProposalData.get(AwardUnitBean.class);
        
        if(cvInvestigators == null || cvInvestigators.size() == 0) return ;
        int size = cvInvestigators.size();
        AwardInvestigatorsBean investigatorBean;
        AwardUnitBean unitBean;
        for(int index = 0; index < size; index++) {
            investigatorBean = (AwardInvestigatorsBean)cvInvestigators.get(index);
            Equals eqPersonId = new Equals("personId", investigatorBean.getPersonId());
            cvTempUnits = cvUnits.filter(eqPersonId);
            investigatorBean.setInvestigatorUnits(cvTempUnits);
        }
    }
    /**
     * integrates Investigator Units to Investigators from Query Engine.
     */
    /*private void integrateInvestigatorUnits() {
        CoeusVector cvInvestigators, cvTempUnits;
        try{
            cvInvestigators = queryEngine.getDetails(queryKey, AwardInvestigatorsBean.class);
            if(cvInvestigators == null || cvInvestigators.size() == 0) return ;
     
            Equals eqUpdate = new Equals("acType", TypeConstants.UPDATE_RECORD);
     
            int size = cvInvestigators.size();
            AwardInvestigatorsBean awardInvestigatorsBean;
            AwardUnitBean awardUnitBean;
            for(int index = 0; index < size; index++) {
                awardInvestigatorsBean = (AwardInvestigatorsBean)cvInvestigators.get(index);
                cvTempUnits = awardInvestigatorsBean.getInvestigatorUnits();
                if(cvTempUnits != null && cvTempUnits.size() > 0) {
                    for(int count = 0; count < cvTempUnits.size(); count++) {
                        //set Investigator Units modifications to Query Engine.
                        awardUnitBean = (AwardUnitBean)cvTempUnits.get(count);
     
                        if(awardUnitBean != null) {
                            queryEngine.set(queryKey, awardUnitBean);
                        }
                    }
                }
                //if this investigator is updated(one of the case is Delete -> Insert)
                //Remove all units belonging to old investigator.
                //this can be achieved by setting unit actype = Delete where unit actype = null
                Equals eqPersonId = new Equals("personId", awardInvestigatorsBean.getPersonId());
                Equals eqNull = new Equals("acType", null);
                And personAndNull = new And(eqPersonId, eqNull);
                queryEngine.setUpdate(queryKey, AwardUnitBean.class, "acType", String.class, TypeConstants.DELETE_RECORD, personAndNull);
     
                //add units eith acType = null since set would only take care of
                //insert, update and delete operations and units for old investigators had to be
                //removed. so put these new units to query engine.
                cvTempUnits = cvTempUnits.filter(eqNull);
                for(int count = 0; count < cvTempUnits.size(); count++) {
                    awardUnitBean = (AwardUnitBean)cvTempUnits.get(count);
                    awardUnitBean.setAcType(TypeConstants.INSERT_RECORD);
                    queryEngine.insert(queryKey, awardUnitBean);
                }
     
                //extract Investigator units from Query Engine to Investigator.
                //Equals eqPersonId = new Equals("personId", awardInvestigatorsBean.getPersonId());
                Equals eqSequenceNo = new Equals("sequenceNumber", new Integer(awardInvestigatorsBean.getSequenceNumber()));
                And personAndSequence = new And(eqPersonId, eqSequenceNo);
     
                //beans with old sequence number and delete.
                if(awardInvestigatorsBean.getAcType() != null &&
                    awardInvestigatorsBean.getAcType().equals(TypeConstants.UPDATE_RECORD) &&
                    !(awardInvestigatorsBean.getAw_PersonId().equals(awardInvestigatorsBean.getPersonId()))
                    ) {
                    Equals eqOldSeqNo = new Equals("sequenceNumber", new Integer(invgtrSeqNoFrmSrv));
                    //Equals eqNull = new Equals("acType", null);
                    Equals eqAwPersonId = new Equals("aw_PersonId", awardInvestigatorsBean.getPersonId());
                    And oldSeqNoAndNull = new And(eqOldSeqNo, eqNull);
                    And oldSeqNoAndNullAndPersonId = new And(oldSeqNoAndNull, eqAwPersonId);
                    queryEngine.setUpdate(queryKey, AwardUnitBean.class, "acType", String.class, TypeConstants.DELETE_RECORD, oldSeqNoAndNullAndPersonId);
                    //queryEngine.removeData(queryKey, AwardUnitBean.class, oldSeqNoAndNullAndPersonId);
                }
     
                queryEngine.setUpdate(queryKey, AwardUnitBean.class, "sequenceNumber", DataType.getClass(DataType.INT), new Integer(invgtrSeqNoFrmSrv), eqUpdate);
     
                cvTempUnits = queryEngine.executeQuery(queryKey, AwardUnitBean.class, eqPersonId);
                awardInvestigatorsBean.setInvestigatorUnits(cvTempUnits);
                queryEngine.set(queryKey, awardInvestigatorsBean);
            }//End for - Investigators.
     
            //Set Sequence number to all Investigator and Unit Beans where AC Type = Update
            queryEngine.setUpdate(queryKey, AwardInvestigatorsBean.class, "sequenceNumber", DataType.getClass(DataType.INT), new Integer(invgtrSeqNoFrmSrv), eqUpdate);
     
     
        }catch (CoeusException coeusException) {
            coeusException.printStackTrace();
        }
    }*/
    
    /**
     * Getter for property openedFromEdiTransaction.
     * @return Value of property openedFromEdiTransaction.
     */
    public boolean isOpenedFromEdiTransaction() {
        return openedFromEdiTransaction;
    }
    
    /**
     * Setter for property openedFromEdiTransaction.
     * @param openedFromEdiTransaction New value of property openedFromEdiTransaction.
     */
    public void setOpenedFromEdiTransaction(boolean openedFromEdiTransaction) {
        this.openedFromEdiTransaction = openedFromEdiTransaction;
    }
    
    /**
     * Getter for property maintainReporting.
     * @return Value of property maintainReporting.
     */
    public boolean canMaintainReporting() {
        return maintainReporting;
    }
    
    /**
     * Setter for property maintainReporting.
     * @param maintainReporting New value of property maintainReporting.
     */
    public void setMaintainReporting(boolean maintainReporting) {
        this.maintainReporting = maintainReporting;
    }
    
    //Bug Fix:1507 Start 2
    private CoeusVector setChangeToZero(CoeusVector cvAwdAmountInfo){
        for(int index = 0;index < cvAwdAmountInfo.size();index++){
            AwardAmountInfoBean awardAmountInfoBean = (AwardAmountInfoBean)cvAwdAmountInfo.get(index);
            awardAmountInfoBean.setAnticipatedChange(0.0);
            awardAmountInfoBean.setObligatedChange(0.0);
               // #3857 --  set direct and indirect amount to 0   start  
            awardAmountInfoBean.setDirectAnticipatedChange(0.0);
            awardAmountInfoBean.setIndirectAnticipatedChange(0.0);
            awardAmountInfoBean.setDirectObligatedChange(0.0);
            awardAmountInfoBean.setIndirectObligatedAmount(0.0);
         //  #3857 --              
        }
        return cvAwdAmountInfo;
    }
    //Bug Fix:1507 End 2
    
    //Bug Fix:1711 Start 3
    private void showReviewRepReq(CoeusVector cvRepReqData){
        ReviewAwardRepReq reviewAwardRepReq = new ReviewAwardRepReq(awardBaseBean,getFunctionType());
        reviewAwardRepReq.setFormData(cvRepReqData);
        reviewAwardRepReq.display();
    }
    //Bug Fix:1711 Start 3
    
    // rdias UCSD - Coeus personalization impl
    public OtherHeaderController getAwCustomElementsController() {
        return otherHeaderController;
    }
   // rdias UCSD - Coeus personalization impl
    
    //Added with case 2796: Sync To Parent
    /*
     * This is called from all the controllers once sync action is performed.
     * @param datakey - is the key of module which fired sync action
     * This method does nothing but saves the award.
     */
    public void saveAndSyncAward(Object dataKey) {
        
        blockEvents(true);
        saveFormData();
        
        try {
            awardBean.setSyncRequired(true);
            saveAward(false);
            
        } catch (CoeusUIException ex){//reset all flags in case of exception
            
        }finally {
            awardBean.setSyncRequired(false);
            setSyncFlags(dataKey,false,new HashMap());
            refreshTabs();
            blockEvents(false);
        }
    }
  
    /* This method refreshes all tabs */
    private void refreshTabs() {
        try{
            Controller tabController;
            for(int index = 0; index < controller.length; index++) {
                tabController = controller[index];
                if(tabController != null) {
                    tabController.setRefreshRequired(true);
                    tabController.refresh();
                }
            }
        }catch (CoeusException coeusException) {
            coeusException.printStackTrace();
            CoeusOptionPane.showErrorDialog(coeusException.getMessage());
        }
    }
    
    //Modified for COEUSDEV-416 : Award Sync to Children - Display proper error message when not syncing because the award is not saved
//    public boolean validateBeforeSync(){
    public boolean validateBeforeSync(String syncType, char mode){
        //COEUSDEV-416 : End
        try {
            //COEUSDEV215: Errors in award Syncing
            //Added for COEUSDEV-416 : Award Sync to Children - Display proper error message when not syncing because the award is not saved
            if(TypeConstants.INSERT_RECORD.equals(awardBean.getAcType()) &&
                    AwardConstants.REPORTS_SYNC.equals(syncType) && AwardConstants.ADD_SYNC == mode){
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(ADD_REPORT_AND_SYNC));
                return false;
            }else if(TypeConstants.INSERT_RECORD.equals(awardBean.getAcType()) &&
                    AwardConstants.REPORTS_SYNC.equals(syncType) && AwardConstants.MODIFY_SYNC == mode){
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(MODIFY_REPORT_AND_SYNC));
                return false;
            }else
                //COEUSDEV-416 : End
                if(TypeConstants.INSERT_RECORD.equals(awardBean.getAcType())){
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(MSGKEY_SAVE_AWARD_SEQUENCE));
                return false;
                }
            for(int index = 0; index < controller.length; index++) {
                if(controller[index] != null && !(controller[index] instanceof AwardInvestigatorController)
                &&  !(controller[index] instanceof AwardContactsController)){
                    controller[index].saveFormData();
                }
            }
            //COEUSDEV 215 End
            return validate();
        } catch (CoeusException ex) {
            CoeusOptionPane.showErrorDialog(ex.getMessage());
            return false;
        } catch (Exception ex) {
            return false;
        }
    }
    //2796: End

    //Retreive Unit info For BIRT Right checks  - START
    private String getLeadUnit(){
        String leadUnit = null;
        CoeusVector cvUnit = null;
        try{
            cvUnit = queryEngine.executeQuery(queryKey,AwardUnitBean.class, new Equals("leadUnitFlag", true));
            if(cvUnit != null && cvUnit.size() > 0) {//Would be only One
                AwardUnitBean unitBean = (AwardUnitBean)cvUnit.get(0);
                leadUnit = unitBean.getUnitNumber();
            }
        }catch(CoeusException coeusException){
        }
        return leadUnit;
    }
    //Retreive Unit info For BIRT Right checks  - END
    
    //COEUSQA-1477 Dates in Search Results - Start
    /**
     * This method format the date which can be passed as a 
     * parameter to be Date() constructor.
     * @param value
     * @return returns dateValue which is formatted date value
     */
    public String formatDateForSearchResults(String value){
        String dateValue = "";
        String validDateFormat = "";
        String dateFormat = CoeusServerProperties.getProperty(CoeusPropertyKeys.SEARCH_DATE_FORMAT);
        if(dateFormat.length()>0){
            DateUtils dtUtils = new DateUtils();
            HashMap hmDateFormats = dtUtils.loadFormatsForSearchResults();
            if(hmDateFormats.get(dateFormat)!=null){
                validDateFormat = hmDateFormats.get(dateFormat).toString();
            }
            if(!(validDateFormat.length()>0)){
                //assign default date value
                dateFormat = CoeusConstants.ORACLE_DATE_YYYY_MM_DD_SLASH;
            }
        }else{
            dateFormat = CoeusConstants.ORACLE_DATE_YYYY_MM_DD_SLASH;
        }
        if(value!=null && value.length()>0){
            //to check if date contains user defined delimiter
            if(value.contains(DATE_FORMAT_USER_DELIMITER)){
                 //to replace the user defined delimiter to default date format
                 dateValue = value.replaceAll(DATE_FORMAT_USER_DELIMITER,DATE_FORMAT_DELIMITER);
            }else{
                dateValue = value;
            }
            if(dateValue.indexOf(DATE_FORMAT_DELIMITER)>0){
                String [] dateComponents = dateValue.split(DATE_FORMAT_DELIMITER);
                if((dateComponents[dateComponents.length-1]).length()<=4){
                    dateValue = fetchDateValuesForSearchResults(dateComponents, dateFormat);
                }else{
                    String date = (dateComponents[dateComponents.length-1]);
                    String patternValue = date.substring(0,date.indexOf(" "));
                    String time = date.substring((date.indexOf(patternValue)+patternValue.length()),date.length());
                    dateComponents[dateComponents.length-1] = patternValue;
                    date = fetchDateValuesForSearchResults(dateComponents, dateFormat);
                    dateValue = date+time;
                }
            }
        }
        return dateValue;
    }

    /**
     * This method format the date which can be passed as a 
     * parameter to be Date() constructor.
     * @param dateComponents
     * @param dateFormat
     * @return returns dateValue which is formatted date value
     */
    public String fetchDateValuesForSearchResults(String[] dateComponents, String dateFormat) {
        HashMap hmDateFormat = new HashMap(4);
        if(dateComponents!=null && dateFormat!=null){
            if(dateFormat.contains(DATE_FORMAT_USER_DELIMITER)){
                dateFormat = dateFormat.replaceAll(DATE_FORMAT_USER_DELIMITER,DATE_FORMAT_DELIMITER);
            }
            //to check whether the date contains "/"
            if(dateFormat.indexOf(DATE_FORMAT_DELIMITER)>0){
                String [] dateDefaultComponents = dateFormat.split(DATE_FORMAT_DELIMITER);
                Integer counter = new Integer(0);
                //to add to the collection object if data matches the repective delimiter
                for(String data:dateDefaultComponents){
                    //to remove "fm" from "Month"
                    if(data.contains("fm")){
                        data = data.replaceAll("fm","");
                    }
                    if(data.substring(0,1).equalsIgnoreCase(DATE_FORMAT_YEAR_DELIMITER)){
                        hmDateFormat.put("Year",counter);
                    }else if(data.substring(0,1).equalsIgnoreCase(DATE_FORMAT_MONTH_DELIMITER)){
                        hmDateFormat.put("Month",counter);
                    }else if(data.substring(0,1).equalsIgnoreCase(DATE_FORMAT_DATE_DELIMITER)){
                        hmDateFormat.put("Date",counter);
                    }
                    counter++;
                }
            }
        }
        //to format the date if it month is in Words
        Integer month=01;
        if(dateComponents[(Integer)hmDateFormat.get("Month")].length()>2){
            Enumeration monthNames =  DateUtils.getMonths().keys();
            while( monthNames.hasMoreElements() ){
                String monthName = (String) monthNames.nextElement();
                if( monthName.startsWith(dateComponents[(Integer)hmDateFormat.get("Month")]) ){
                    String monthNumber = (String) DateUtils.getMonths().get(monthName);
                    month = Integer.parseInt(monthNumber);
                    break;
                }
            }
        }else{
            month = (Integer)hmDateFormat.get("Month");
        }
        //formation of the date in the default date format
        String date = dateComponents[(Integer)hmDateFormat.get("Year")]+DATE_FORMAT_DELIMITER
                +month
                +DATE_FORMAT_DELIMITER+dateComponents[(Integer)hmDateFormat.get("Date")];
        return date;
    }
    //COEUSQA-1477 Dates in Search Results - End
    

//code for getting disclosure status of awards-- starts
    private void showDisclosureStatus() {
        HashMap hmParameter = new HashMap();
        int moduleCode = 1;
        String awardNumber =awardBaseBean.getMitAwardNumber();
        hmParameter.put("AWARD_NUMER", awardNumber);
        edu.mit.coeus.propdev.gui.DisclosureStatusForm  disclosureStatusForm=new
        edu.mit.coeus.propdev.gui.DisclosureStatusForm(awardNumber,moduleCode);
    }
//code for getting disclosure status of awards-- ends

//COEUSQA 2111 STARTS
    private void RouteAwardDocumentMenuAction(){
        ShowRouteAwardNotice();
        UpdateApproveRejection();
    }
    private void RouteDeltaDocumentMenuAction(){
        
        ShowRouteDeltaReport();
        UpdateApproveRejection();
    }
  private void ShowRouteAwardNotice() {
        RoutePrintNoticeForm routePrintNoticeForm =new RoutePrintNoticeForm();
        routePrintNoticeForm.setAward(awardBean);
        routePrintNoticeForm.display();
    }
    private void ShowRouteDeltaReport() {
        RouteDeltaReportForm routeDeltaReportForm = new RouteDeltaReportForm();
        routeDeltaReportForm.setAward(awardBean);
        routeDeltaReportForm.display();
    }
  private boolean CheckUserHasRouteDocRight(){
  boolean hasRight=false;
  RequesterBean requesterBean = new RequesterBean();
  requesterBean.setFunctionType(CHECK_USER_HAS_DOC_ROUTE_RIGHT);
  requesterBean.setDataObject(awardBean.getMitAwardNumber());
  String connectTo = CoeusGuiConstants.CONNECTION_URL
                + SUBMISSION_SERVLET;
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, requesterBean);
        comm.send();
  ResponderBean response = comm.getResponse();
  if(response.isSuccessfulResponse()){
  hasRight=(Boolean)response.getDataObject();
  
  }
  return hasRight;
  }
 //to update the menu items
 private void UpdateApproveRejection(){
     awardBaseWindow.mnuItmApproveReject.setEnabled(false);
     edu.mit.coeus.award.bean.AwardDocumentRouteBean awardDocumentRouteBean= awardBean.getLatestAwardDocumentRouteBean();
     if(awardDocumentRouteBean!=null){
         int docNumber=awardDocumentRouteBean.getRoutingDocumentNumber();
         if(docNumber>0){
             awardBaseWindow.mnuItmApproveReject.setEnabled(true);
         }
         }
 }
//COEUSQA 2111 ENDS
// Added for COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split Infrastructure project - Start
    /**
     * Method to get boolean value is credit split save required
     * @return isCreditSplitSaveRequired
     */
 public boolean isCreditSplitSaveRequired() {
     return isCreditSplitSaveRequired;
 }
 
    /**
     * Method to set boolean value is credit split save required
     * @param isCreditSplitSaveRequired 
     */
 public void setIsCreditSplitSaveRequired(boolean isCreditSplitSaveRequired) {
     this.isCreditSplitSaveRequired = isCreditSplitSaveRequired;
 }
 /**
  * Method to check checks if the proposal being linked has any investigators that are not in award already
  * @param propNumber
  * @return
  */
// private boolean isIPHasNewInvestigator(String propNumber) throws CoeusException, CoeusClientException {
//     RequesterBean requesterBean = new RequesterBean();
//     ResponderBean responderBean = new ResponderBean();
//     requesterBean.setFunctionType('!');
//     requesterBean.setDataObject(propNumber);
//     AppletServletCommunicator comm= new AppletServletCommunicator(CoeusGuiConstants.CONNECTION_URL + SERVLET,requesterBean);
//     comm.setRequest(requesterBean);
//     comm.send();
//     responderBean = comm.getResponse();
//     Hashtable htData = null;
//     if(responderBean.hasResponse()) {
//         CoeusVector cvIPInvestigator = (CoeusVector)responderBean.getDataObject();
//         CoeusVector cvAwardInvestigators = queryEngine.getDetails(queryKey,AwardInvestigatorsBean.class);
//         if((cvAwardInvestigators == null || cvAwardInvestigators.isEmpty())
//         && (cvIPInvestigator != null && !cvIPInvestigator.isEmpty())){
//             return true;
//         }
//         
//         if(cvIPInvestigator != null && !cvIPInvestigator.isEmpty()){
//             for(Object ipInvestigator : cvIPInvestigator){
//                 InstituteProposalInvestigatorBean ipInvestigatorDetails =
//                         (InstituteProposalInvestigatorBean)ipInvestigator;
//                 And andPerson = new And(new Equals("personId",ipInvestigatorDetails.getPersonId()),
//                         CoeusVector.FILTER_ACTIVE_BEANS);
//                 CoeusVector cvFilterAwardInv = cvAwardInvestigators.filter(andPerson);
//                 if(cvFilterAwardInv == null || cvFilterAwardInv.isEmpty()){
//                     return true;
//                 }
//             }
//         }
//     }else{
//         throw new CoeusClientException(responderBean.getMessage(),CoeusClientException.ERROR_MESSAGE);
//     }
//     
//     return false;
// }
 // Added for COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split Infrastructure project - End
}
