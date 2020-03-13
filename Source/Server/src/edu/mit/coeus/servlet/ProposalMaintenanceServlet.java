
/*
 * ProposalMaintenanceServlet.java
 *
 * Created on March 13, 2003, 12:27 PM
 */

/* PMD check performed, and commented unused imports and
 * variables on 08-FEB-2012 by Bharati
 */
package edu.mit.coeus.servlet;

import edu.mit.coeus.award.bean.AwardLookUpDataTxnBean;
import edu.mit.coeus.questionnaire.bean.QuestionnaireTxnBean;
import edu.mit.coeus.s2s.bean.S2SSubmissionDataTxnBean;
import edu.mit.coeus.utils.MailActions;
import edu.mit.coeus.propdev.ProposalAuthorization;
import edu.mit.coeus.routing.bean.RoutingBean;
import edu.mit.coeus.routing.bean.RoutingDetailsBean;
import edu.mit.coeus.routing.bean.RoutingMapBean;
import edu.mit.coeus.routing.bean.RoutingTxnBean;
import edu.mit.coeus.routing.bean.RoutingUpdateTxnBean;
//import java.text.MessageFormat;
import javax.servlet.*;
import javax.servlet.http.*;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.bean.*;

import edu.mit.coeus.propdev.bean.*;
//import edu.mit.coeus.irb.bean.PersonInfoTxnBean;

import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.organization.bean.*;
import edu.mit.coeus.rolodexmaint.bean.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.departmental.bean.DepartmentPersonTxnBean;
import edu.mit.coeus.sponsormaint.bean.SponsorMaintenanceDataTxnBean;
import edu.mit.coeus.customelements.bean.CustomElementsInfoBean;
import edu.mit.coeus.departmental.bean.DepartmentPersonFormBean;
import edu.mit.coeus.irb.bean.ProtocolDataTxnBean;
import edu.mit.coeus.irb.bean.ProtocolInfoBean;
//import edu.mit.coeus.irb.bean.ProtocolInvestigatorsBean;
//import edu.mit.coeus.irb.bean.ProtocolSpecialReviewFormBean;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
import edu.mit.coeus.utils.locking.LockingBean;
import edu.mit.coeus.utils.locking.LockingException;
import edu.mit.coeus.utils.locking.LockingTxnBean;

import java.io.*;
//import java.text.DecimalFormat;
import java.util.*;
import edu.mit.coeus.utils.ModuleConstants;
//import edu.mit.coeus.utils.dbengine.ProcReqParameter;
//import java.text.SimpleDateFormat;

/**
 * This servlet is used to handle the proposal requests.
 * @author  subramanya
 * @version
 */
public class ProposalMaintenanceServlet extends CoeusBaseServlet implements TypeConstants {

    //holds the until Facotry instance
    //    private UtilFactory UtilFactory = new UtilFactory();
    private static final char SAVE_PROPOSAL_PERSON_BIOGRAPHY_DETAILS = 'w';
    private static final char SAVE_PROPOSAL_DEGREE_DETAILS = 'a';
    private static final char GET_PROPOSAL_PERSON_BIOGRAPHY_DATA = 'B';
    private static final char SAVE_PROP_ADMIN_CERTIFY_DETAILS = 'C';
    private static final char GET_PROPOSAL_ADMIN_DETAILS = 'E';
    private static final char GET_NARRATIVE_USERS_FOR_PROPOSAL = 'F';
    private static final char GET_ROLES_FOR_USER = 'G';
    private static final char SET_PROPOSAL_USER_ROLES = 'H';
    private static final char GET_USERS_FOR_PROP_ROLES = 'I';
    private static final char GET_PROPOSAL_ABSTRACTS = 'J';
    private static final char GET_PROPOSAL_DEGREE_DETAILS_FOR_PERSON = 'K';
    private static final char UPDATE_NARRATIVE_DETAILS = 'L';
    private static final char SAVE_PROPOSAL_ABSTRACTS = 'j';
    private static final char GET_NARRATIVE_DETAILS = 'N';
    //Added for case #1856 start 1
    private static final char GET_MODIFY_NARRATIVE_DATA = 'e';
    //Added for case #1856 end 1
    //Added by Shiji for right checking - step1 :start
    private static final char GET_NARRATIVE = 'c';
    //right checking - step1 : end
    //Added by shiji for Case id:2016 step 1 - start
    private static final char SEND_NOTIFICATION = 'd';
    //Case id:2016 step 1 - end
    private static final char GET_IS_OK_COPY_BUDGET = 'O';
    private static final char GET_PROPOSAL_VIEWERS = 'P';
    private static final char GET_COPY_BUDGET_NARR_ALLOWED = 'Q'; 
    private static final char SAVE_RELEASE_LOCK = 'R';
    private static final char SAVE_RETAIN_LOCK = 'S';
    private static final char SAVE_QUESTION_ANSWERS = 's';
    private static final char QUESTION_ANSWER = 'T';
    private static final char USER_HAS_RIGHT = 'U';
    private static final char COPY_PROPOSAL_DETAILS = 'V';
    private static final char GET_PROPOSAL_PERSON_DETAILS = 'W';
    private static final char PROP_TO_PROT_SAVE = '8';
    private static final char SAVE_PROPOSAL_PERSON_RECORD = 'X';
    private static final char GET_EDITABLE_COLUMN_NAMES = 'Y';
    private static final char ROW_UNLOCK_MODE = 'Z';
//    private static final char GET_NARRATIVE_PDF = 'k';
//    private static final char GET_NARRATIVE_SOURCE = 'l';
    /*Added for Case# COEUSQA-1675 -ability to delete Proposal Development proposals - Start*/
    private static final char DELETE_PROPOSAL = 'i';
    private static final char GET_DELETE_RIGHTS = 'l';
    /*Added for Case# COEUSQA-1675 -ability to delete Proposal Development proposals - End*/
    private static final char UPDATE_NARRATIVE_PDF = 'm';
    private static final char UPDATE_NARRATIVE_SOURCE = 'n';
    private static final char GET_PROPOSAL_STATUS = 'o';
    private static final char CHECK_ROLE_HAS_NARRATIVE_RIGHT = 'p';
    //Added by Shiji for right checking - step2 :start
    private static final char CHECK_USER_HAS_RIGHT = 'b';
    //right checking - step2 :end
    private static final char GET_PRINTING_DATA = 'g';
    //Case #1602 Start 1
    private static final char GET_PROP_PERSON_DETAILS_FOR_INV_KEYSTUDY = 'q';
    //Case #1769 - start
    private static final char CHECK_LOCK_FOR_PROP_SPL_REVIEW = 'r';
    // To get the person document type code
    private static final char GET_PERSON_DOC_CODE = 'z';
    //Case #1769 - End
    //
    //Case #1602 End 1
    /*For the CoeusEnhancement case:1776, module with the narrative type code equal
     *to the Paramater NARRATIVE_TYPE_CODE in the parameter table shouldn't save
     *without the module title start step:1*/
    private static final char GET_ADD_NARRATIVE_DATA = 'x';
    private static final char VALIDATE_PROP_NUM = 'v';
    private static final char GET_PROPOSAL_TITLE = 't';
    /* End CoeusEnhancement case:1776 step:1*/
    //Added for Case#3577 - Replace Personnel Attachments while Proposal Development is Approval in Progress - Start
    private static final char GET_PROP_PERSONNEL_RIGHTS = 'f';
    //Added for Case#3577 - Replace Personnel Attachments while Proposal Development is Approval in Progress - End
    //Bug Fix by Geo on 14-Jul-2005
    //Should not assign class variables in Servlet
    //    String loggedinUser;
    //End Fix
    //Rights
    private static final String SUBMIT_PROPOSAL = "SUBMIT_PROPOSAL";
    private static final String APPROVE_PROPOSAL = "APPROVE_PROPOSAL";
    private static final String VIEW_ANY_PROPOSAL = "VIEW_ANY_PROPOSAL";
    private static final String VIEW_RIGHT  = "VIEW_PROPOSAL";    
    
    //Added by Shiji for right checking - step3 :start
    private static final String MODIFY_ANY_PROPOSAL = "MODIFY_ANY_PROPOSAL";
    //right checking - step3 :end
    private static final String VIEW_NARRATIVE = "VIEW_NARRATIVE";
    private static final String MODIFY_NARRATIVE = "MODIFY_NARRATIVE";
    // Added for the Coeus enhancement case:#1767
    private static final String ALTER_PROPOSAL_DATA = "ALTER_PROPOSAL_DATA";
    // End Coeus enhancement case:#1767
    //Role
    private static final int APPROVER_ROLE_ID = 101;
    //Modified for 3183 - Proposal Hierarchy changes - start
    //'u' is already used for CAN_ADD_NARRATIVE function type
    //Added for case#2420 - Upload of files on Edit Module Details Window - start
    //private static final char GET_BLOB_DATA_FOR_NARRATIVE = 'u';
    private static final char GET_BLOB_DATA_FOR_NARRATIVE = 'y';
    //Added for case#2420 - Upload of files on Edit Module Details Window - end
    //Modified for 3183 - Proposal Hierarchy changes - start
    private static final char CAN_ADD_NARRATIVE = 'u';
    //Case#4572 - Writing notepad entries and sending notifications irrespective of proposal status - Start
    private static final int APPROVEL_IN_PROGRESS = 2;
    //Case#4572 - End
    // COEUSDEV-185	Proposal Development and Subawards are viewable from medusa without appropriate role.
    private static final char CAN_VIEW_DEV_PROPOSAL = '0';
    //Added for COEUSQA-1579 : For Hierarchy Proposal, Approval in Progress, cannot sync after narratives updated on child proposal - Start
    private static final char PROPOSAL_CREATION_STATUS_CODE = 'h';
    //COEUSQA-1579 : End
    // Added for COEUSQA-2778-Submited Prop to Sponsor-Narrative Incomplete - Start
    private static final char GET_NARRATIVE_STATUS = '1';
    // Added for COEUSQA-2778-Submited Prop to Sponsor-Narrative Incomplete - End
    //COEUSQA-1433 - Allow Recall from Routing - Start
    private static final char PROPOSAL_RECALL_LOCK_CHECK = '2';
    //COEUSQA-1433 - Allow Recall from Routing - End
    private static final char PROPOSAL_CREATION_STATUS_DETAILS = '3';
    
    private static final char CHECK_IS_PHS_HS_CT_FORM = '4';
    
    // JM 11-21-2012 get proposal details only
    private static final char GET_PROPOSAL_DETAILS_ONLY = '4';
    // JM END
    
    /** Initializes the servlet.
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

    }

    /** Destroys the servlet.
     */
    public void destroy() {
    }

    /** Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {
        // the request object from applet
        RequesterBean requester = null;
        // the response object to applet
        ResponderBean responder = new ResponderBean();

        // open object input/output streams
        ObjectInputStream inputFromApplet = null;
        ObjectOutputStream outputToApplet = null;
//        UserInfoBean userBean;
        ProposalDevelopmentTxnBean proposalDataTxnBean;
        ProposalDevelopmentUpdateTxnBean proposalUpdTxnBean;
        ProposalDevelopmentFormBean proposalDevelopmentFormBean = null;
//        RoleRightInfoBean roleRightInfoBean = null;
//        RolodexDetailsBean rolodexDetailsBean;
        ProtocolDataTxnBean protocolDataTxnBean;
        //For the Coeus Enhancement case:#1799 start step:1
        CoeusVector cvParameters = new CoeusVector();
        //End Coeus Enhancement case:#1799 start step:1

        String proposalNumber;
        char functionType;

        try {
            // get an input stream
            inputFromApplet = new ObjectInputStream(request.getInputStream());
            // read the serialized request object from applet
            requester = (RequesterBean) inputFromApplet.readObject();
            isValidRequest(requester);
            // get the user
            String loggedinUser = requester.getUserName();

            proposalDataTxnBean = new ProposalDevelopmentTxnBean();
            ProposalDevelopmentUpdateTxnBean proposalUpdateTxnBean = new ProposalDevelopmentUpdateTxnBean(loggedinUser);
            // keep all the beans into vector
            Vector dataObjects = new Vector();
            functionType = requester.getFunctionType();
            proposalNumber = (requester.getId() == null ? ""
                    : (String) requester.getId());
            if (functionType == GET_PROPOSAL_TITLE) {
                String id = requester.getId();
                String title = proposalUpdateTxnBean.getProposalTitle(id);
                responder.setDataObject(title);
            } else if (functionType == VALIDATE_PROP_NUM) {
                String id = requester.getId();
                int exist = proposalUpdateTxnBean.validateProposalNumber(id);
                responder.setDataObject(new Integer(exist));
            } else if (functionType == GET_PRINTING_DATA) {
                ProposalDevelopmentFormBean proposalBean =
                        (ProposalDevelopmentFormBean) requester.getDataObject();
//                    Hashtable hshSponsorData  = new Hashtable();
                ProposalDevelopmentTxnBean propDevTxnBean = new ProposalDevelopmentTxnBean();
                Hashtable packagePageData =
                        propDevTxnBean.getProposalPrintForms(requester.getUserName(), proposalBean.getProposalNumber(),
                        proposalBean.getSponsorCode(), proposalBean.getPrimeSponsorCode());
                responder.setDataObject(packagePageData);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            } else if (functionType == GET_PROPOSAL_VIEWERS) {
                Vector dataObj = (Vector) requester.getDataObject();
                proposalNumber = (String) dataObj.elementAt(0);
                int roleId = Integer.parseInt((String) dataObj.elementAt(1));
                dataObjects =
                        proposalDataTxnBean.getUsersForProposalRole(proposalNumber, roleId);
            } else if (functionType == GET_ROLES_FOR_USER) {
                Vector roles = proposalDataTxnBean.getRoleAndRoleName(loggedinUser);
                dataObjects.addElement(roles);
                // COEUSDEV-185	Proposal Development and Subawards are viewable from medusa without appropriate role - Start
            } else if (functionType == CAN_VIEW_DEV_PROPOSAL) {
                proposalNumber = (String) requester.getDataObject();
                ProposalAuthorization ProposalAuthorization = new ProposalAuthorization();
                boolean canViewDevProp = ProposalAuthorization.canViewProposal(loggedinUser, proposalNumber);
                responder.setDataObject(new Boolean(canViewDevProp));
                responder.setResponseStatus(true);
                responder.setMessage(null);
            // Uncommented for COEUSQA-3438 : user unable to view PD proposals through medusa when they have PD rights 
            }else if(functionType == SAVE_QUESTION_ANSWERS ) {
                Vector proposalYNQBean = (Vector) requester.getDataObject();
                if (proposalYNQBean == null) {
                    return;
                }
                proposalUpdTxnBean = new ProposalDevelopmentUpdateTxnBean(loggedinUser);
                for (int index = 0; index < proposalYNQBean.size(); index++) {
                    ////System.out.println("is the update good ?. " + proposalUpdTxnBean.addUpdDeleteProposalYNQ((ProposalYNQBean)proposalYNQBean.elementAt(index)));
                }
            // JM 11-21-2012 get proposal details only
            } else if (functionType == GET_PROPOSAL_DETAILS_ONLY) {
                proposalNumber = (String) requester.getId();
                proposalDataTxnBean = new ProposalDevelopmentTxnBean();
                proposalDevelopmentFormBean = proposalDataTxnBean.getProposalDevelopmentDetails(proposalNumber);
                dataObjects.addElement(proposalDevelopmentFormBean);
                responder.setResponseStatus(true);
                responder.setDataObjects(dataObjects);
            // JM END
            } else if (functionType == GET_PROPOSAL_ABSTRACTS) {
                proposalNumber = (String) requester.getId();
                ProposalDevelopmentTxnBean proposalDevTxnBean = new ProposalDevelopmentTxnBean();
                Vector vAbstractTypes = proposalDevTxnBean.getAbstractTypes();
                ////System.out.println("vAbstractTypes = "+vAbstractTypes);
                Vector vProposalAbstract = proposalDevTxnBean.getProposalAbstract(proposalNumber);
                ////System.out.println("vProposalAbstract = "+vProposalAbstract);
                dataObjects.addElement(vAbstractTypes);
                ////System.out.println("added vAbstractTypes to dataObjects");
                dataObjects.addElement(vProposalAbstract);
                ////System.out.println("added vProposalAbstract to dataObjects");
            } else if (functionType == SAVE_PROPOSAL_ABSTRACTS) {
                proposalNumber = (String) requester.getId();
                ////System.out.println("Entering the SAVE_PROPOSAL_ABSTRACTS.");
                Vector vProposalAbstract = (Vector) requester.getDataObjects();
                if (vProposalAbstract == null) {
                    ////System.out.println("Error in sending the vProposalAbstract Vector.");
                    ////System.out.println("vProposalAbstract == null.");
                    return;
                }
                proposalUpdTxnBean = new ProposalDevelopmentUpdateTxnBean(loggedinUser);
                for (int index = 0; index < vProposalAbstract.size(); index++) {
                    proposalUpdTxnBean.addUpdDeleteProposalAbstract((ProposalAbstractFormBean) vProposalAbstract.elementAt(index));
                }
                //ProposalDevelopmentTxnBean proposalDevTxnBean = new ProposalDevelopmentTxnBean();
                //Vector vAbstractTypes = proposalDevTxnBean.getAbstractTypes();
                //Vector vProposalAbstract = proposalDevTxnBean.getProposalAbstract(proposalNumber);
                //dataObjects.addElement(vAbstractTypes);
                //dataObjects.addElement(vProposalAbstract);
            }


             else if(functionType == PROP_TO_PROT_SAVE){
                 boolean success = false;
                 //char functionType= '8';
                 String userId = requester.getUserName();
                 ProtocolInfoBean protocolData = (ProtocolInfoBean)requester.getDataObject();
                    proposalUpdTxnBean = new ProposalDevelopmentUpdateTxnBean(loggedinUser);
                    protocolData.setCreateUser(userId);
                    String propnum=(String)requester.getId();
                    Vector propdetails = proposalUpdTxnBean.addStubProtocolInfo(protocolData,functionType,userId);
                    proposalDataTxnBean.transactionCommit();
                    success=(Boolean)propdetails.get(1);
                    String protocolStubNumber=(String)propdetails.get(0);
                    if(!success){
                    responder.setResponseStatus(false);
                    responder.setMessage("Proposal To Protocol Save Failed..");
                }else{
                    responder.setResponseStatus(true);
                    responder.setDataObject(protocolStubNumber);
                }
                   
            }

            else if (functionType == GET_PROPOSAL_PERSON_DETAILS) {
                ////System.out.println("In side GET_PROPOSAL_PERSON_DETAILS");
                ////System.out.println("***********************************");

                Vector vecInputData = (Vector) requester.getDataObject();
                String proposalId = (String) vecInputData.elementAt(0);
                String personId = (String) vecInputData.elementAt(1);

                ////System.out.println("proposalId is "+proposalId);
                ////System.out.println("personId is "+personId);

                Vector vecProposalPersonOtherDetails = null;
                String moduleCode = null;
                String homeUnit = null;
                ProposalPersonFormBean proposalPersonFormBean = null;

                boolean userCanMaintainUnitPersonInfo = false;
                boolean userCanMaintainPersonInfo = false;
                String userUnitNumber = null;

                Vector vecCustColumnsForModuleVector = null;

                DepartmentPersonTxnBean departmentPersonTxnBean = new DepartmentPersonTxnBean();
                SponsorMaintenanceDataTxnBean sponsorMaintenanceDataTxnBean = new SponsorMaintenanceDataTxnBean();

                //PersonInfoTxnBean personInfoTxnBean = new PersonInfoTxnBean();
                //personId = personInfoTxnBean.getPersonID(personName);

                TreeSet othersData = new TreeSet(new CustomElementsComparator());
                ////System.out.println("personId is "+personId);
                if (proposalId != null && personId != null) {
                    // Commented for COEUSQA-2293 - Citizenship Info field (other/custom data) should be editable in Person details on a proposal - Start
                    //For other tab MODUIY_PROPOSAL is checked as the other tab in proposal person window
//                        homeUnit = departmentPersonTxnBean.getHomeUnit(personId);
//                        if(homeUnit != null){
//                            userCanMaintainUnitPersonInfo =
//                            sponsorMaintenanceDataTxnBean.isUserHasRight(loggedinUser, homeUnit, "MAINTAIN_PERSON_INFO");
//                        }

//                        Vector userInformation = (Vector)departmentPersonTxnBean.getUserInfoForPerson(personId);
//                        String id = (String)userInformation.elementAt(0);
//                        userUnitNumber = (String)userInformation.elementAt(1);
                    ////System.out.println("userId is "+id);
                    ////System.out.println("userUnitNumber is "+userUnitNumber);
//                        if(userUnitNumber != null){
//                            userCanMaintainPersonInfo =
//                            sponsorMaintenanceDataTxnBean.isUserHasRight(loggedinUser, userUnitNumber, "MAINTAIN_PERSON_INFO");
//                        }
                    //COEUSQA-2293 :End
                    proposalPersonFormBean = proposalDataTxnBean.getProposalPersonDetails(proposalId, personId);

                    vecProposalPersonOtherDetails = proposalDataTxnBean.getProposalOthersDetails(proposalId, personId);
                    if (vecProposalPersonOtherDetails != null) {
                        othersData.addAll(vecProposalPersonOtherDetails);
                    }

                    moduleCode = departmentPersonTxnBean.getParameterValues("COEUS_MODULE_PERSON");
                    //                    //System.out.println("moduleCode is "+moduleCode);
                    if (moduleCode != null && moduleCode.trim().length() > 0) {
                        vecCustColumnsForModuleVector = (Vector) departmentPersonTxnBean.getPersonColumnModule(moduleCode);
                    }
                    if (vecCustColumnsForModuleVector != null) {
                        int size = vecCustColumnsForModuleVector.size();
                        if (size > 0) {
                            vecCustColumnsForModuleVector = setAcTypeAsNew(vecCustColumnsForModuleVector);
                            othersData.addAll(vecCustColumnsForModuleVector);
                        }
                    }
                    ////System.out.println("TimeStamp is "+proposalPersonFormBean.getUpdateTimestamp());
                    ////System.out.println("Update User is "+proposalPersonFormBean.getUpdateUser());
                    ////System.out.println("userCanMaintainUnitPersonInfo is "+userCanMaintainUnitPersonInfo);
                    ////System.out.println("userCanMaintainPersonInfo is "+userCanMaintainPersonInfo);
                    Vector vecData = new Vector(othersData);
                    ////System.out.println("GetData Values");
                    printBean(vecData);
                    dataObjects.addElement(proposalPersonFormBean);
                    dataObjects.addElement(vecData);
                    //Commented for COEUSQA-2293 - Citizenship Info field (other/custom data) should be editable in Person details on a proposal - Start
//                        dataObjects.addElement(new Boolean(userCanMaintainUnitPersonInfo));
//                        dataObjects.addElement(new Boolean(userCanMaintainPersonInfo));
                    //COEUSQA-2293 : End
                    //responder.setId(personId);
                    responder.setDataObject(dataObjects);

                }
                ////System.out.println("**********************************************");
            } //Case #1602 Start 2
            else if (functionType == GET_PROP_PERSON_DETAILS_FOR_INV_KEYSTUDY) {
                Vector vecInputData = (Vector) requester.getDataObject();
                String proposalId = (String) vecInputData.elementAt(0);
                String personId = (String) vecInputData.elementAt(1);

                //Vector vecProposalPersonOtherDetails = null;
                String moduleCode = null;
//                    String homeUnit = null;
                ProposalPersonFormBean proposalPersonFormBean = null;

//                    boolean userCanMaintainUnitPersonInfo = false;
//                    boolean userCanMaintainPersonInfo = false;
//                    String userUnitNumber = null;

                Vector vecCustColumnsForModuleVector = null;
                Vector departmentOthersFormBeanVector = new Vector();

                DepartmentPersonTxnBean departmentPersonTxnBean = new DepartmentPersonTxnBean();
                if (proposalId != null && personId != null) {
                    //homeUnit = departmentPersonTxnBean.getHomeUnit(personId);
                    //                    if(homeUnit != null){
                    //                        userCanMaintainUnitPersonInfo =
                    //                            sponsorMaintenanceDataTxnBean.isUserHasRight(loggedinUser, homeUnit, "MAINTAIN_PERSON_INFO");
                    //                    }

                    //                    Vector userInformation = (Vector)departmentPersonTxnBean.getUserInfoForPerson(personId);
                    //                    String id = (String)userInformation.elementAt(0);
                    //                    userUnitNumber = (String)userInformation.elementAt(1);

                    //                    if(userUnitNumber != null){
                    //                        userCanMaintainPersonInfo =
                    //                            sponsorMaintenanceDataTxnBean.isUserHasRight(loggedinUser, userUnitNumber, "MAINTAIN_PERSON_INFO");
                    //                    }

                    proposalPersonFormBean = proposalDataTxnBean.getProposalPersonDetails(proposalId, personId);
                    if (proposalPersonFormBean == null) {
                        DepartmentPersonFormBean departmentPersonFormBean = departmentPersonTxnBean.getPersonDetails(personId);
                        proposalPersonFormBean = new ProposalPersonFormBean(departmentPersonFormBean);
                    }
                    departmentOthersFormBeanVector = (Vector) departmentPersonTxnBean.getPersonOthersDetails(personId);
                    CoeusVector training = departmentPersonTxnBean.getTraining();
                    CoeusVector personTraining = departmentPersonTxnBean.getDepartmentPersonTraining(personId);


                    moduleCode = departmentPersonTxnBean.getParameterValues("COEUS_MODULE_PERSON");

                    if (moduleCode != null && moduleCode.trim().length() > 0) {
                        vecCustColumnsForModuleVector = (Vector) departmentPersonTxnBean.getPersonColumnModule(moduleCode);
                    }


                    dataObjects.addElement(proposalPersonFormBean);
                    dataObjects.addElement(vecCustColumnsForModuleVector);
                    dataObjects.addElement(departmentOthersFormBeanVector);
                    dataObjects.addElement(training);
                    dataObjects.addElement(personTraining);
                    responder.setDataObject(dataObjects);
                }

            } //Case #1602 End 2
            else if (functionType == GET_PROPOSAL_DEGREE_DETAILS_FOR_PERSON) {
                DepartmentPersonTxnBean departmentPersonTxnBean = new DepartmentPersonTxnBean();
                Vector reqData = (Vector) requester.getDataObject();
                String proposalId = null;
                String personId = null;
                if (reqData != null) {
                    proposalId = (String) reqData.elementAt(0);
                    personId = (String) reqData.elementAt(1);
                }
                ////System.out.println("proposalId is "+proposalId);
                ////System.out.println("personId is "+personId);
                Vector vecProposalPersonDegree = null;
                if (proposalId != null && personId != null) {
                    ProposalPersonTxnBean proposalPersonTxnBean = new ProposalPersonTxnBean();
                    vecProposalPersonDegree = proposalPersonTxnBean.getProposalPersonDegree(proposalId, personId);
                }
                Vector vecDegreeTypeCodes = (Vector) departmentPersonTxnBean.getDegreeTypeCodeDescription();
                Vector vecSchoolCodes = (Vector) departmentPersonTxnBean.getSchoolCodeDescription();

                dataObjects.addElement(vecDegreeTypeCodes);
                dataObjects.addElement(vecSchoolCodes);
                dataObjects.addElement(vecProposalPersonDegree);

                responder.setDataObject(dataObjects);

            } else if (functionType == GET_EDITABLE_COLUMN_NAMES) {

                Vector vecEditableColumns = null;
                vecEditableColumns = proposalDataTxnBean.getPersonEditableColumns();
                responder.setDataObject(vecEditableColumns);
            } else if (functionType == GET_COPY_BUDGET_NARR_ALLOWED) {
                proposalNumber = (String) requester.getId();
                proposalDataTxnBean = new ProposalDevelopmentTxnBean();
                //Modified for case#2903 - Modify all dev proposals should allow you to copy proposal - start
                //If the user is having MODIFY_ANY_PROPOSAL then no need to check the Budget and Narrative rights
                String unitNumber = (String) requester.getDataObject();
                UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
                boolean rightExist = userMaintDataTxnBean.getUserHasRight(loggedinUser, "MODIFY_ANY_PROPOSAL", unitNumber);
                String strHasBudget = "NO";
                String strHasNarrative = "NO";

                if (proposalDataTxnBean.isPropBudgetCopyAllowed(proposalNumber, loggedinUser, rightExist)) {
                    strHasBudget = "YES";
                }
                if (proposalDataTxnBean.isPropNarrativeCopyAllowed(proposalNumber, loggedinUser, rightExist)) {
                    strHasNarrative = "YES";

                }
                // COEUSQA-2321: Copy Questionnaires for Proposal Development records - Start
                String strHasQnr = "NO";
                QuestionnaireTxnBean questionnaireTxnBean = new QuestionnaireTxnBean();
                if (questionnaireTxnBean.checkAnyQuestionIsAnsweredInModule(3, proposalNumber, 0, "0")) {
                    strHasQnr = "YES";
                }
                // COEUSQA-2321: Copy Questionnaires for Proposal Development records - End
                //Modified for case#2903 - Modify all dev proposals should allow you to copy proposal - end
                
                //Added for COEUSQA-3509 : Add warning message to Copy proposal window - start
                //If proposal is grant gov. proposal then set ggproposal value as yes
                S2SSubmissionDataTxnBean s2SSubmissionDataTxnBean = new S2SSubmissionDataTxnBean();
                String strGGProposal = "NO";
                if(s2SSubmissionDataTxnBean.isS2SCandidate(proposalNumber)){
                    strGGProposal = "YES";
                }
                //Added for COEUSQA-3509 : Add warning message to Copy proposal window - end
                dataObjects.addElement(strHasBudget);
                dataObjects.addElement(strHasNarrative);
                dataObjects.addElement(strHasQnr);
                dataObjects.addElement(strGGProposal);
                responder.setResponseStatus(true);
                responder.setDataObjects(dataObjects);
            } else if (functionType == GET_IS_OK_COPY_BUDGET) {
                Vector dataObjs = (Vector) requester.getDataObjects();
                proposalNumber = (String) dataObjs.elementAt(0);
                String unitNumber = (String) dataObjs.elementAt(1);
                proposalDataTxnBean = new ProposalDevelopmentTxnBean();
//                    String strIsOk= "NO";
                int intIsOk = proposalDataTxnBean.getIsOkToCopyProposalBudget(proposalNumber, unitNumber);

                dataObjects.addElement(new Integer(intIsOk));
                responder.setResponseStatus(true);
                responder.setDataObjects(dataObjects);
            } /*Added for Case# COEUSQA-1675-ability to delete Proposal Development proposals - Start*/ else if (functionType == GET_DELETE_RIGHTS) {
                proposalNumber = requester.getId();
                Vector dataObjs = (Vector) requester.getDataObjects();
                UserMaintDataTxnBean txnData = new UserMaintDataTxnBean();
                boolean isAuthorised = txnData.getUserHasProposalRight(loggedinUser, proposalNumber, "DELETE_PROPOSAL");
                String unitNumber = (String) dataObjs.elementAt(0);
                //If no rights check at Unit level right
                if (!isAuthorised) {
                    if (unitNumber != null && unitNumber.length() > 0) {
                        isAuthorised = txnData.getUserHasRight(loggedinUser, "DELETE_ANY_PROPOSAL", unitNumber);
                    }
                }
//Added for COEUSQA-2548_Delete Proposal action issue with unlinked hierarchy proposal requested from same Proposal List window_Start
                proposalDataTxnBean = new ProposalDevelopmentTxnBean();
                String isInHierarchy = proposalDataTxnBean.checkIsProposalInHierarchy(proposalNumber);
                dataObjects.add(0, new Boolean(isAuthorised));
                dataObjects.add(1, isInHierarchy);
                responder.setDataObjects(dataObjects);
//              responder.setDataObject(new Boolean(isAuthorised));
//Added for COEUSQA-2548_Delete Proposal action issue with unlinked hierarchy proposal requested from same Proposal List window_End
                responder.setResponseStatus(true);

            } else if (functionType == DELETE_PROPOSAL) {
                proposalNumber = requester.getId();
                Vector dataObjs = (Vector) requester.getDataObjects();
                String unitNumber = (String) dataObjs.elementAt(0);
                UserMaintDataTxnBean txnData = new UserMaintDataTxnBean();
                int proposalDeleted = -1;
                boolean deleteSucessful = false;
                String errMsg = null;
                int canDelete = proposalDataTxnBean.checkCanDeleteProposal(proposalNumber);
                if (canDelete == 0) {
                    //canDelete= 0 - proposal is in pending in progress status's
                    //now check for lock, if lock is available then delete the proposal
                    boolean lockCheck = proposalDataTxnBean.lockCheck(requester.getId(), loggedinUser);
                    if (lockCheck) {
                        //get the lock of the proposal and  delete the proposal
                        LockingBean lockingBean = proposalDataTxnBean.getLock(proposalNumber, loggedinUser, unitNumber);
                        if (lockingBean != null && lockingBean.isGotLock()) {
                            proposalDataTxnBean.transactionCommit();
                            proposalUpdTxnBean = new ProposalDevelopmentUpdateTxnBean(loggedinUser);
                            proposalDeleted = proposalUpdTxnBean.deletePendingProposal(proposalNumber);
                            lockingBean = proposalUpdTxnBean.releaseLock(proposalNumber, loggedinUser);
                            responder.setLockingBean(lockingBean);
                        }
                        if (proposalDeleted == 0) {
                            deleteSucessful = true;
                        } else {
                            responder.setDataObject(new Boolean(false));
                            UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
                            String userName = userMaintDataTxnBean.getUserName(lockingBean.getUserID());
                            errMsg = userName + " is using proposal " + proposalNumber;
                            responder.setMessage(errMsg);
                            responder.setResponseStatus(true);
                        }
                        responder.setDataObject(new Boolean(deleteSucessful));
                        responder.setResponseStatus(true);
                    } else {
                        // The proposal is being used by another user
                        responder.setDataObject(new Boolean(false));
                        UserInfoBean userBean = (UserInfoBean) new UserDetailsBean().getUserInfo(requester.getUserName());
                        errMsg = userBean.getUserName() + " is using proposal " + proposalNumber;
                        responder.setMessage(errMsg);
                        responder.setResponseStatus(true);
                    }
                } else if (canDelete == 2 || canDelete == 5) {
                    //canDelete == 2 -  proposal is not one of the status's required
                    //(Pending in Progress, send an error msg
                    responder.setDataObject(new Boolean(false));
                    errMsg = "deleteProposal_exceptionCode.100" + canDelete;
                    responder.setMessage(errMsg);
                    responder.setResponseStatus(true);
                }
            } /*Added for Case# COEUSQA-1675 -ability to delete Proposal Development proposals - End*/ else if (functionType == COPY_PROPOSAL_DETAILS) {
                Vector dataObjs = (Vector) requester.getDataObjects();
                proposalNumber = (String) dataObjs.elementAt(0);
                String unitNumber = (String) dataObjs.elementAt(1);
                char budgetFlag = ((Character) dataObjs.elementAt(2)).charValue();
                char narrativeFlag = ((Character) dataObjs.elementAt(3)).charValue();
                // COEUSQA-2321: Copy Questionnaires for Proposal Development records
                char questionnaireFlag = ((Character) dataObjs.elementAt(4)).charValue();
                proposalDataTxnBean = new ProposalDevelopmentTxnBean();
                String targetProposalNumber = proposalDataTxnBean.getNextProposalNumber();
                proposalUpdTxnBean = new ProposalDevelopmentUpdateTxnBean(loggedinUser);
                // COEUSQA-2321: Copy Questionnaires for Proposal Development records
//                    int intCopyFlag = proposalUpdTxnBean.copyProposal(proposalNumber, targetProposalNumber, budgetFlag, narrativeFlag, unitNumber, loggedinUser);
                int intCopyFlag = proposalUpdTxnBean.copyProposal(proposalNumber, targetProposalNumber, budgetFlag,
                        narrativeFlag, questionnaireFlag, unitNumber, loggedinUser);


                proposalDevelopmentFormBean = proposalDataTxnBean.getProposalDevelopmentDetails(targetProposalNumber);

                dataObjects.addElement(new Integer(intCopyFlag));
                dataObjects.addElement(proposalDevelopmentFormBean);

                responder.setResponseStatus(true);
                responder.setDataObjects(dataObjects);
            } else if (functionType == GET_PROPOSAL_ADMIN_DETAILS) {
                ////System.out.println("Inside ProposalMaintenanceServlet");
                ////System.out.println("Before getting DataObject");
                proposalNumber = (String) requester.getId();
                ////System.out.println("ProposalNumber Inside Servlet = "+proposalNumber);
                //Checking if logged in user has OSP rights
                UserDetailsBean usrDetBean = new UserDetailsBean();
                int hasRightIntFlag = usrDetBean.getUserHasAnyOSPRights(loggedinUser);
                String hasRightStrFlag = "";
                if (hasRightIntFlag == 1) {
                    hasRightStrFlag = "YES";
                } else {
                    hasRightStrFlag = "NO";
                }
                ////System.out.println("hasRightStrFlag = "+hasRightStrFlag);
                ProposalDevelopmentTxnBean propDevTxnBean = new ProposalDevelopmentTxnBean();
                ProposalAdminFormBean admDetails = propDevTxnBean.getProposalAdminDetails(proposalNumber);
                ////System.out.println("admDetails object = "+admDetails);
                Vector certifyDetails = propDevTxnBean.getProposalCertifyDetails(proposalNumber);
                ////System.out.println("certifyDetails Vector = "+certifyDetails);
                dataObjects.addElement(hasRightStrFlag);
                dataObjects.addElement(admDetails);
                dataObjects.addElement(certifyDetails);
            } else if (functionType == SAVE_PROP_ADMIN_CERTIFY_DETAILS) {
                ////System.out.println("Inside ProposalMaintenanceServlet");
                ////System.out.println("Before getting DataObject");
                Vector vCertify = (Vector) requester.getDataObject();
                ////System.out.println("vCertify Vector Inside Servlet = "+vCertify);
                if ((vCertify != null) || ((vCertify.size()) > 0)) {
                    try {
                        int dataSize = vCertify.size();
                        ProposalCertificationFormBean proposalCertificationFormBean = null;
                        for (int indx = 0; indx < dataSize; indx++) {
                            proposalCertificationFormBean =
                                    (ProposalCertificationFormBean) vCertify.elementAt(indx);
                            if (proposalCertificationFormBean != null) {
                                ProposalDevelopmentUpdateTxnBean propDevUpdTxnBean =
                                        new ProposalDevelopmentUpdateTxnBean(loggedinUser);
                                propDevUpdTxnBean.updDeleteProposalCertify(proposalCertificationFormBean);
                            }
                        }
                    } catch (Exception e) {
                        // commented for using UtilFactory.log instead of printStackTrace
                        UtilFactory.log(e.getMessage(), e, "ProposalMaintenanceServlet", "doPost");
//                            e.printStackTrace();
                    }
                }
            } else if (functionType == SAVE_PROPOSAL_PERSON_RECORD) {
                ////System.out.println("In Side SAVE_PROPOSAL_PERSON_RECORD");
                Vector vecDataVector = (Vector) requester.getDataObject();
                ProposalPersonFormBean pBean = (ProposalPersonFormBean) vecDataVector.elementAt(0);

                ////System.out.println("personId is "+pBean.getPersonId());
                ////System.out.println("proposalId is "+pBean.getProposalNumber());
                ////System.out.println("Actype is "+pBean.getAcType());
                ////System.out.println("TimeStamp is "+pBean.getUpdateTimestamp());
                ////System.out.println("Update User is "+pBean.getUpdateUser());

                Vector vecPersonCustomElements = (Vector) vecDataVector.elementAt(1);
                ////System.out.println("Changed Values At Server");
                ////System.out.println("************************");
                //printBean(vecPersonCustomElements);
                try {
                    //ProposalDevelopmentUpdateTxnBean propDevUpdTxnBean =
                    //                new ProposalDevelopmentUpdateTxnBean(loggedinUser);
                    ProposalPersonTxnBean proposalPersonTxnBean = new ProposalPersonTxnBean(loggedinUser);
                    boolean success = proposalPersonTxnBean.addDelPropPerson(pBean, vecPersonCustomElements);
                    ////System.out.println("Success is "+success);

                    // COEUSDEV-199: Proposal personnel upload changes do not trigger out of sync flag in hierarchies - Start
                    ProposalHierarchyTxnBean hierarchyTxnBean = new ProposalHierarchyTxnBean(loggedinUser);
                    if (pBean != null) {
                        hierarchyTxnBean.updateSyncFlag(pBean.getProposalNumber(), "P");
                    }
                    // COEUSDEV-199: Proposal personnel upload changes do not trigger out of sync flag in hierarchies - End
                } catch (Exception e) {
                    // commented for using UtilFactory.log instead of printStackTrace
                    UtilFactory.log(e.getMessage(), e, "ProposalMaintenanceServlet", "doPost");
//                        e.printStackTrace();
                }
            } else if (functionType == SAVE_PROPOSAL_DEGREE_DETAILS) {

                ////System.out.println("In Side SAVE_PROPOSAL_DEGREE_DETAILS");
                Vector vecDegreeDataVector = (Vector) requester.getDataObject();
                //printBean(vecPersonCustomElements);
                try {
                    //ProposalDevelopmentUpdateTxnBean propDevUpdTxnBean =
                    //                new ProposalDevelopmentUpdateTxnBean(loggedinUser);
                    ProposalPersonTxnBean proposalPersonTxnBean = new ProposalPersonTxnBean(loggedinUser);
                    //printDegreeBeans(vecDegreeDataVector);
                    boolean success = proposalPersonTxnBean.addUpdProposalPersonDegree(vecDegreeDataVector);
                    ////System.out.println("Success is "+success);
                    // COEUSDEV-199: Proposal personnel upload changes do not trigger out of sync flag in hierarchies - Start
                    if (vecDegreeDataVector != null && vecDegreeDataVector.size() > 0) {
                        ProposalPerDegreeFormBean proposalPerDegreeFormBean = (ProposalPerDegreeFormBean) vecDegreeDataVector.elementAt(0);
                        ProposalHierarchyTxnBean hierarchyTxnBean = new ProposalHierarchyTxnBean(loggedinUser);
                        if (proposalPerDegreeFormBean != null) {
                            hierarchyTxnBean.updateSyncFlag(proposalPerDegreeFormBean.getProposalNumber(), "P");
                        }
                    }
                    // COEUSDEV-199: Proposal personnel upload changes do not trigger out of sync flag in hierarchies - End
                } catch (Exception e) {
                    // commented for using UtilFactory.log instead of printStackTrace
                    UtilFactory.log(e.getMessage(), e, "ProposalMaintenanceServlet", "doPost");
//                        e.printStackTrace();
                }
            } else if (functionType == SAVE_PROPOSAL_PERSON_BIOGRAPHY_DETAILS) {

                ////System.out.println("In Side SAVE_PROPOSAL_PERSON_BIOGRAPHY_DETAILS");

                Vector vecDataVector = (Vector) requester.getDataObject();
                Vector vecBiodata = new Vector();
                vecBiodata = (Vector) vecDataVector.elementAt(0);
                Hashtable htPersonBiography = (Hashtable) vecDataVector.elementAt(1);
                String proposalId = (String) vecDataVector.elementAt(2);
                if (htPersonBiography != null) {
                    Enumeration enumu = htPersonBiography.keys();
                    while (enumu.hasMoreElements()) {
                        String personId = (String) enumu.nextElement();
                        Vector vecBioForPerson = (Vector) htPersonBiography.get(personId);
                        if (vecBioForPerson != null && !vecBioForPerson.isEmpty()) {
                            setBiographyNumbers(vecBioForPerson, proposalId, personId);
                            for (int index = 0; index < vecBioForPerson.size(); index++) {
                                vecBiodata.addElement(vecBioForPerson.elementAt(index));
                            }
                        }
                    }
                }

                //ProposalDevelopmentUpdateTxnBean propDevUpdTxnBean =
                //                new ProposalDevelopmentUpdateTxnBean(loggedinUser);
                ProposalPersonTxnBean proposalPersonTxnBean = new ProposalPersonTxnBean(loggedinUser);


                //Case #1777 Start
                ProposalDevelopmentUpdateTxnBean propDevUpdTxnBean =
                        new ProposalDevelopmentUpdateTxnBean(loggedinUser);
                Hashtable htPropPersonData = (Hashtable) vecDataVector.get(3);
                //Added for the Coeus Enhancemnt case:#1767
                int proposalStatusCode = ((Integer) vecDataVector.get(4)).intValue();
                String unitNumber = (String) vecDataVector.get(5);
                boolean alterProposalPerson = false;
                alterProposalPerson = ((Boolean) vecDataVector.get(6)).booleanValue();
                //COEUSQA-1433 - Allow Recall from Routing - Start
                //if (proposalStatusCode == 2 || proposalStatusCode == 4 && alterProposalPerson) {
                if (proposalStatusCode == CoeusConstants.PROPOSAL_APPROVAL_IN_PROGRESS_STATUS_CODE 
                        || proposalStatusCode == CoeusConstants.PROPOSAL_APPROVED_STATUS_CODE && alterProposalPerson) {
                //COEUSQA-1433 - Allow Recall from Routing - End
                    try {
                        LockingBean lockingBean = proposalDataTxnBean.getLock(proposalId, loggedinUser, unitNumber);
                        boolean getLock = lockingBean.isGotLock();
                        if (!getLock) {
                            throw new CoeusException();
                        }
                    } catch (DBException dbEx) {
                        // commented for using UtilFactory.log instead of printStackTrace
                        UtilFactory.log(dbEx.getMessage(), dbEx, "ProposalMaintenanceServlet", "doPost");
//                            dbEx.printStackTrace();
                        proposalDataTxnBean.transactionRollback();
                        throw dbEx;
                    } finally {
                        proposalDataTxnBean.endConnection();
                    }
                }
                //End for the Coeus Enhancemnt case:#1767
                Enumeration enumer = htPropPersonData.keys();
                while (enumer.hasMoreElements()) {
                    String key = (String) enumer.nextElement();
                    ProposalPersonFormBean proposalPersonFormBean =
                            (ProposalPersonFormBean) htPropPersonData.get(key);
                    propDevUpdTxnBean.updPropPerson(proposalPersonFormBean);
                }
                //Case #1777 End

                boolean success = proposalPersonTxnBean.addUpdPersonBiography(vecBiodata);
                dataObjects = new Vector();
                dataObjects = proposalPersonTxnBean.getAllPersonDetails(proposalId);
                //Code added for case#3183 - Proposal Hierarchy enhancement - starts
                if (dataObjects != null && dataObjects.size() > 0) {
                    for (int index = 0; index < dataObjects.size(); index++) {
                        ProposalPersonFormBean personBean =
                                (ProposalPersonFormBean) dataObjects.get(index);
                        personBean.setEditable(proposalPersonTxnBean.isPersonDataEditable(
                                personBean.getProposalNumber(), personBean.getPersonId()));
                    }
                }
                //Code added for case#3183 - Proposal Hierarchy enhancement - ends
                //Added for the Coeus Enhancemnt case:#1767
                if (alterProposalPerson) {
                    proposalUpdTxnBean = new ProposalDevelopmentUpdateTxnBean(loggedinUser);
                    proposalUpdTxnBean.releaseLock(proposalId, loggedinUser);
                }
                //End for the Coeus Enhancemnt case:#1767
                // COEUSDEV-199: Proposal personnel upload changes do not trigger out of sync flag in hierarchies - Start
                ProposalHierarchyTxnBean hierarchyTxnBean = new ProposalHierarchyTxnBean(loggedinUser);
                hierarchyTxnBean.updateSyncFlag(proposalId, "P");
                // COEUSDEV-199: Proposal personnel upload changes do not trigger out of sync flag in hierarchies - End
                responder.setResponseStatus(true);
                responder.setDataObjects(dataObjects);
            } else if (functionType == GET_PROPOSAL_ADMIN_DETAILS) {
                ////System.out.println("Inside ProposalMaintenanceServlet");
                ////System.out.println("Before getting DataObject");
                proposalNumber = (String) requester.getId();
                ////System.out.println("ProposalNumber Inside Servlet = "+proposalNumber);
                //Checking if logged in user has OSP rights
                UserDetailsBean usrDetBean = new UserDetailsBean();
                int hasRightIntFlag = usrDetBean.getUserHasAnyOSPRights(loggedinUser);
                String hasRightStrFlag = "";
                if (hasRightIntFlag == 1) {
                    hasRightStrFlag = "YES";
                } else {
                    hasRightStrFlag = "NO";
                }
                ////System.out.println("hasRightStrFlag = "+hasRightStrFlag);
                ProposalDevelopmentTxnBean propDevTxnBean = new ProposalDevelopmentTxnBean();
                ProposalAdminFormBean admDetails = propDevTxnBean.getProposalAdminDetails(proposalNumber);
                ////System.out.println("admDetails object = "+admDetails);
                Vector certifyDetails = propDevTxnBean.getProposalCertifyDetails(proposalNumber);
                ////System.out.println("certifyDetails Vector = "+certifyDetails);
                dataObjects.addElement(hasRightStrFlag);
                dataObjects.addElement(admDetails);
                dataObjects.addElement(certifyDetails);
            } else if (functionType == GET_PROPOSAL_PERSON_BIOGRAPHY_DATA) {
                ////System.out.println("Here in GET_PROPOSAL_PERSON_BIOGRAPHY_DATA");
                proposalNumber = (String) requester.getId();
                //JIRA COEUSDEV-548 - START
                String unitNumber = (String) requester.getDataObject();
                //JIRA COEUSDEV-548 - END
                Vector vecPropPersonBioDetails = null;
                if (proposalNumber != null) {
                    ProposalPersonTxnBean proposalPersonTxnBean = new ProposalPersonTxnBean();
                    vecPropPersonBioDetails = proposalPersonTxnBean.getAllPersonDetails(proposalNumber);
                    //Code added for case#3183 - Proposal Hierarchy enhancement - starts
                    if (vecPropPersonBioDetails != null && vecPropPersonBioDetails.size() > 0) {
                        for (int index = 0; index < vecPropPersonBioDetails.size(); index++) {
                            ProposalPersonFormBean personBean =
                                    (ProposalPersonFormBean) vecPropPersonBioDetails.get(index);
                            personBean.setEditable(proposalPersonTxnBean.isPersonDataEditable(
                                    personBean.getProposalNumber(), personBean.getPersonId()));
                        }
                    }
                    //Code added for case#3183 - Proposal Hierarchy enhancement - ends
                }
                //JIRA COEUSDEV-548 - START
                UserMaintDataTxnBean dataTxnBean = new UserMaintDataTxnBean();
                int userHasRight = 0;//No rights
                boolean hasRight = dataTxnBean.getUserHasRight(loggedinUser, "MODIFY_ANY_PROPOSAL", unitNumber);
                if (hasRight) {
                    userHasRight = 1;
                } else {
                    userHasRight = proposalDataTxnBean.getUserHasPropRightCount(loggedinUser, proposalNumber, "MODIFY_PROPOSAL");
                }
                //JIRA COEUSDEV-548 - END
                ////System.out.println("userHasRight value is "+userHasRight);
                ////System.out.println("BIOGRAPHY DETAILS FROM DATABASE");
                //printBiographyDetails(vecPropPersonBioDetails);
                dataObjects.addElement(vecPropPersonBioDetails);

                dataObjects.addElement(new Integer(userHasRight));
                //Code added for Proposal Hierarchy case#3183 - starts
                ProposalNarrativeTxnBean proposalNarrativeTxnBean = new ProposalNarrativeTxnBean();
                Vector vecPropHierLink = proposalNarrativeTxnBean.getPropHierLinkData(proposalNumber);
                dataObjects.addElement(vecPropHierLink);
                //Code added for Proposal Hierarchy case#3183 - ends
                responder.setDataObject(dataObjects);

            } else if (functionType == SAVE_RETAIN_LOCK) {
                proposalUpdTxnBean = new ProposalDevelopmentUpdateTxnBean(loggedinUser);

                proposalDevelopmentFormBean = (ProposalDevelopmentFormBean) requester.getDataObject();
                boolean success = false;
                // for locking bug fixing - BEGIN
                if ((proposalDevelopmentFormBean.getAcType() != null) && (proposalDevelopmentFormBean.getAcType().equals("I"))) {
                    success = proposalUpdTxnBean.addUpdProposalDevelopment(proposalDevelopmentFormBean);
                    // 2930: Auto-delete Current Locks based on new parameter - Start
                    // Lock the Proposal if it is newly created
                    LockingBean lockingBean = proposalDataTxnBean.getLock(proposalNumber, loggedinUser, proposalNumber);
                    responder.setLockingBean(lockingBean);
                    // 2930: Auto-delete Current Locks based on new parameter - End
                } else {
                    boolean lockCheck = proposalDataTxnBean.lockCheck(proposalNumber, loggedinUser);
                    if (!lockCheck) {
                        success = proposalUpdTxnBean.addUpdProposalDevelopment(proposalDevelopmentFormBean);
                    } else {
                        //String msg = "Sorry,  the lock for proposal number "+proposalNumber+"  has been deleted by DB Administrator ";
                        CoeusMessageResourcesBean coeusMessageResourcesBean = new CoeusMessageResourcesBean();
                        String msg = coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1006") + " " + proposalNumber + " " + coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1002");
                        throw new LockingException(msg);
                    }
                }
                // for locking bug fixing - END
                //                if ( proposalUpdTxnBean.addUpdProposalDevelopment(
                //                        proposalDevelopmentFormBean) ){
                if (success) {
                    // 2930: Auto-delete Current Locks based on new parameter - Start
//                        // Locking new proposal created.
//                        LockingBean lockingBean =  proposalDataTxnBean.getLock(proposalNumber, loggedinUser, proposalNumber);
//                        boolean getLock = lockingBean.isGotLock();
                    // 2930: Auto-delete Current Locks based on new parameter - End

                    //Proposal Hierarchy Enhancment Start
                    //*gnprh commented for Proposal Hierarchy
                    ProposalHierarchyTxnBean propHierarchyTxnBean =
                            new ProposalHierarchyTxnBean(loggedinUser);
                    int count = propHierarchyTxnBean.updateSyncFlag(proposalNumber, "P");
                    //Proposal Hierarchy Enhancment End

                    proposalDevelopmentFormBean =
                            proposalDataTxnBean.getProposalDevelopmentDetails(proposalNumber);
                    dataObjects.addElement(proposalDevelopmentFormBean);
                    //Added for the Coeus Enhancement case:1799 start step:1
                    protocolDataTxnBean = new ProtocolDataTxnBean();
                    // COEUSQA-2320 Show in Lite for Special Review in Code table
                    // dataObjects.addElement(protocolDataTxnBean.getSpecialReviewCode());
                    dataObjects.addElement(protocolDataTxnBean.getSpecialReviewTypesForModule(ModuleConstants.PROPOSAL_DEV_MODULE_CODE));
                    dataObjects.addElement(protocolDataTxnBean.getReviewApprovalType());
                    dataObjects.addElement(protocolDataTxnBean.getValidSpecialReviewList());
                    //End Coeus Enhancement case:#1799
                } else {

                    responder.setResponseStatus(false);
                    responder.setMessage(
                            "Proposal Maintainance details save failed");
                }

            } else if (functionType == SAVE_RELEASE_LOCK) {
                proposalUpdTxnBean = new ProposalDevelopmentUpdateTxnBean(loggedinUser);

                proposalDevelopmentFormBean = (ProposalDevelopmentFormBean) requester.getDataObject();
                boolean success = false;
                // for locking bug fixing - BEGIN
                if ((proposalDevelopmentFormBean.getAcType() != null) && (proposalDevelopmentFormBean.getAcType().equals("I"))) {
                    success = proposalUpdTxnBean.addUpdProposalDevelopment(proposalDevelopmentFormBean);
                } else {
                    boolean lockCheck = proposalDataTxnBean.lockCheck(proposalNumber, loggedinUser);
                    if (!lockCheck) {
                        success = proposalUpdTxnBean.addUpdProposalDevelopment(proposalDevelopmentFormBean);
                    } else {
                        //String msg = "Sorry,  the lock for proposal number "+proposalNumber+"  has been deleted by DB Administrator ";
                        CoeusMessageResourcesBean coeusMessageResourcesBean = new CoeusMessageResourcesBean();
                        String msg = coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1006") + " " + proposalNumber + " " + coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1002");
                        throw new LockingException(msg);
                    }
                }
                // for locking bug fixing - END
                // For locking bug fixing
                //                if ( proposalUpdTxnBean.addUpdProposalDevelopment(
                //                        proposalDevelopmentFormBean) ){
                if (success) {
                    // write code to release lock
                    //Code commented by Shivakumar for locking enhancement
                    //                    proposalUpdTxnBean.releaseEdit(proposalNumber);
                    // Code added by Shivakumar -BEGIN1
                    // Commented on request of Ravi 12 Aug 2004
                    //                    proposalUpdTxnBean.releaseEdit(proposalNumber,loggedinUser);
                    // Calling releaseLock method for fixing the bug
                    LockingBean lockingBean = proposalUpdTxnBean.releaseLock(proposalNumber, loggedinUser);
                    responder.setLockingBean(lockingBean);
                    // Code added by Shivakumar -END1

                    //Proposal Hierarchy Enhancment Start
                    //*gnprh commented for Proposal Hierarchy
                    ProposalHierarchyTxnBean propHierarchyTxnBean =
                            new ProposalHierarchyTxnBean(loggedinUser);
                    int count = propHierarchyTxnBean.updateSyncFlag(proposalNumber, "P");
                    //Proposal Hierarchy Enhancment End

                    //Pass the data back to client - start
                    proposalDevelopmentFormBean =
                            proposalDataTxnBean.getProposalDevelopmentDetails(proposalNumber);
                    dataObjects.addElement(proposalDevelopmentFormBean);
                    //Added for the Coeus Enhancement case:#1799 start step:2
                    protocolDataTxnBean = new ProtocolDataTxnBean();
                    // COEUSQA-2320 Show in Lite for Special Review in Code table.doc
                    //dataObjects.addElement(protocolDataTxnBean.getSpecialReviewCode());
                    dataObjects.addElement(protocolDataTxnBean.getSpecialReviewTypesForModule(ModuleConstants.PROPOSAL_DEV_MODULE_CODE));
                    dataObjects.addElement(protocolDataTxnBean.getReviewApprovalType());
                    dataObjects.addElement(protocolDataTxnBean.getValidSpecialReviewList());
                    //End Coeus Enhancement case:#1799 step:2
                    //Pass the data back to client - end
                    responder.setResponseStatus(true);
                    //responder.setDataObject("updateLock connection released");
                } else {

                    responder.setResponseStatus(false);
                    responder.setMessage(
                            "Proposal Maintainance details save failed");
                }

            } else if (functionType == ROW_UNLOCK_MODE) {
                proposalNumber = requester.getDataObject().toString().trim();
                proposalUpdTxnBean = new ProposalDevelopmentUpdateTxnBean(loggedinUser);
                //Code commented by Shivakumar for locking enhancement
                //                proposalUpdTxnBean.releaseEdit(proposalNumber);
                // Code added by Shivakumar -BEGIN1
                //                proposalUpdTxnBean.releaseEdit(proposalNumber,loggedinUser);
                // Calling releaseLock method for fixing bug in locking
                LockingTxnBean lockingTxnBean = new LockingTxnBean();
                String unitnumber = lockingTxnBean.getLockData("osp$Development Proposal_" + proposalNumber, loggedinUser);
                // Unit number "00000000" is set for proposal locked from coeus lite.
                if (unitnumber != null && !unitnumber.equals("00000000")) {
                    LockingBean lockingBean = proposalUpdTxnBean.releaseLock(proposalNumber, loggedinUser);
                    if (lockingBean != null) {
                        responder.setLockingBean(lockingBean);
                    }
                }
                // Code added by Shivakumar -END1
                responder.setResponseStatus(true);
                responder.setDataObject("updateLock connection released");
            } else if (functionType == SET_PROPOSAL_USER_ROLES) {
                /*Vector userParams = requester.getDataObjects();
                //System.out.println("saving proposal viewers"+userParams);
                if( userParams != null ){

                int vecSize = userParams.size();
                ProposalRolesFormBean roleFormBean=null;
                for( int indx = 0 ; indx < vecSize; indx++) {
                roleFormBean = (ProposalRolesFormBean) userParams.elementAt( indx );
                //System.out.println("SAVING"+roleFormBean);;
                if(roleFormBean != null){
                //System.out.println("saving ..id"+roleFormBean.getUserId());
                roleFormBean.setUpdateTimestamp((new CoeusFunctions()).getDBTimestamp());
                ProposalDevelopmentUpdateTxnBean updateTxnBean = new ProposalDevelopmentUpdateTxnBean(loggedinUser);
                updateTxnBean.addDeleteProposalRolesForUser(roleFormBean);
                }
                }
                updateNarrativeUsers(userParams);
                }*/
                Vector userParams = requester.getDataObjects();
                proposalUpdTxnBean = new ProposalDevelopmentUpdateTxnBean(loggedinUser);
                boolean blnflag = proposalUpdTxnBean.addDeleteProposalViewers(userParams, proposalNumber);
                dataObjects.add(new Boolean(blnflag));
                responder.setResponseStatus(true);
            } else if (functionType == QUESTION_ANSWER) {
                ////System.out.println("Inside the QUESTION and ANSWER.");
                //inputFromApplet =
                //        new ObjectInputStream(request.getInputStream() );
                ////System.out.println("Before reading the input");
                //requester = (RequesterBean) inputFromApplet.readObject();
                ////System.out.println("After reading the input");
                proposalDataTxnBean = new ProposalDevelopmentTxnBean();
                /*
                Vector roleIDs = proposalDataTxnBean.getProposalRightRole(100);

                if (roleIDs == null) {
                //System.out.println("The roleIDS is null");
                }
                Vector userRole = proposalDataTxnBean.getProposalRightForUser(requester.getId(),loggedinUser);
                if (userRole == null) {
                //System.out.println("The userRole is null");
                }
                 */
                Vector questionList = proposalDataTxnBean.getYesNoQuestionList("P",proposalNumber);
                Vector answerList = proposalDataTxnBean.getProposalYNQ(proposalNumber);
                //                        //System.out.println("ProposalMaintenanceServlet answerList is Null ? " + answerList);
                // Commented and it should taken care at the client side by
                // QuestionForm
                        /*
                ProposalYNQBean[] yqnArray = null;
                if (answerList == null) {
                answerList = new Vector();
                //System.out.println("The AnswerList is null.");
                yqnArray = new ProposalYNQBean[questionList.size()];
                for (int i = 0; i < questionList.size(); i++) {
                yqnArray[i] = new ProposalYNQBean();
                yqnArray[i].setQuestionId(((edu.mit.coeus.utils.question.bean.QuestionListBean)questionList.elementAt( i )).getQuestionId());
                yqnArray[i].setProposalNumber(requester.getId());
                yqnArray[i].setAcType("I");
                //orgQuestionList[i].setOrgId(requester.getId());
                //setting default answer.
                yqnArray[i].setAnswer("");
                answerList.addElement(yqnArray[i]);
                }
                }
                 */
                Hashtable explanation = new Hashtable();
                String questionID = null;

                for (int index = 0; index < answerList.size(); index++) {
                    ////System.out.println("Inside the loop " + index);
                    questionID = ((ProposalYNQBean) answerList.elementAt(index)).getQuestionId();
                    ////System.out.println("questionID = " + questionID);
                    Vector vExp = proposalDataTxnBean.getProposalYNQExplantion(questionID);
                    if (vExp != null) {
                        explanation.put(questionID, vExp);
                    }
                }

                //dataObjects.addElement(roleIDs);
                //dataObjects.addElement(userRole);
                dataObjects.addElement(questionList);
                dataObjects.addElement(answerList);
                dataObjects.addElement(explanation);
                responder.setDataObjects(dataObjects);
                responder.setResponseStatus(true);
            } else if (functionType == GET_USERS_FOR_PROP_ROLES) {
                inputFromApplet =
                        new ObjectInputStream(request.getInputStream());
                requester = (RequesterBean) inputFromApplet.readObject();
                proposalDataTxnBean = new ProposalDevelopmentTxnBean();
                Vector dataObj = (Vector) requester.getDataObject();
                dataObjects = new Vector();
                proposalNumber = (String) dataObj.elementAt(0);
                int roleId = Integer.parseInt((String) dataObj.elementAt(1));
                ////System.out.println("values from client:"+proposalNumber);
                ////System.out.println("roleId:"+roleId);
                dataObjects = proposalDataTxnBean.getUsersForProposalRole(proposalNumber, roleId);
                ////System.out.println("dataObjects from server:"+dataObjects);
            } else if (functionType == GET_NARRATIVE_DETAILS) {
                Character mode = (Character) requester.getDataObject();
                UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
                //Check OSP Right VIEW_ANY_PROPOSAL
                boolean hasRight = userMaintDataTxnBean.getUserHasOSPRight(loggedinUser, VIEW_ANY_PROPOSAL);
                //If not present check VIEW_NARRATIVE
                if (!hasRight) {
                    hasRight = userMaintDataTxnBean.getUserHasProposalRight(loggedinUser, proposalNumber, VIEW_NARRATIVE);
                    //If not present check MODIFY_NARRATIVE
                    if (!hasRight) {
                        hasRight = userMaintDataTxnBean.getUserHasProposalRight(loggedinUser, proposalNumber, MODIFY_NARRATIVE);
                    }
                }
                //If User has atleast one of the above rights
                if (hasRight) {

                    //Check whether Proposal can be modified
                    ProposalActionTxnBean proposalActionTxnBean = new ProposalActionTxnBean();
                    int canModify = proposalActionTxnBean.canModifyDevelopmentProposal(proposalNumber);
                    if (canModify > 0) {
                        //If can Modify, check whether user has MODIFY_NARRATIVE right
                        hasRight = userMaintDataTxnBean.getUserHasProposalRight(loggedinUser, proposalNumber, MODIFY_NARRATIVE);
                    } else {
                        hasRight = false;
                    }
                    ProposalNarrativeTxnBean proposalNarrativeTxnBean = new ProposalNarrativeTxnBean();
                    Vector narratives = proposalNarrativeTxnBean.getProposalNarrativeDetails(proposalNumber);

                    /*added for the coeus narrative enhancements case : 1624 start step:1*/
                    Vector narrativeTypes = proposalNarrativeTxnBean.getProposalNarrativeTypes();
                    /*end step1 */

                    /*Added for the Coeus Enhancement case:1776 ,to get the parameter value from the parameter table start step:2*/
                    CoeusFunctions coeusFunctions = new CoeusFunctions();
                    String narrativeTypeCode = coeusFunctions.getParameterValue("NARRATIVE_TYPE_OTHER_CODE");
                    /*End Coeus Enhancement case:1776 step:2*/

                    dataObjects = new Vector();
                    dataObjects.addElement(new Boolean(hasRight));
                    dataObjects.addElement(narratives);

                    /*added for the coeus narrative enhancements case : 1624 start step:2*/
                    dataObjects.addElement(narrativeTypes);
                    /*end step2*/

                    /* Added for the CoeusEnhancement case:1776 step:3*/
                    dataObjects.addElement(narrativeTypeCode);
                    /*End CoeusEnhancement case:1776 step:3*/

                    responder.setDataObjects(dataObjects);
                    //Try to get Narrative Lock
                    //Budget Data is required to display in Display mode
                    //even if Budget is locked by some other User
                    //added to lock the narrative only if the functiontype is modify
                    // IF condition modified by Shiva 01/09/2004
                    //                         if( !hasRight&&mode.charValue() != 'D'){
                    if (mode.charValue() != 'D') {
                        // Code commented by Shivakumar for locking enhancement - BEGIN
                        //                            proposalNarrativeTxnBean.getNarrativeLock(proposalNumber);
                        // Code commented by Shivakumar for locking enhancement - END
                        // Code added by Shivakumar for locking enhancement - BEGIN
                        try {
                            // 2930: Auto-delete Current Locks based on new parameter - Start
                            // Get the Lock for the Narrative and send the Locking Bean back to the client side
//                                proposalNarrativeTxnBean.getNarrativeLock(proposalNumber,loggedinUser,proposalNumber);
//                                proposalNarrativeTxnBean.transactionCommit();
                            LockingBean lockingBean = proposalNarrativeTxnBean.getNarrativeLock(proposalNumber, loggedinUser, proposalNumber);
                            proposalNarrativeTxnBean.transactionCommit();
                            responder.setLockingBean(lockingBean);
                            // 2930: Auto-delete Current Locks based on new parameter - End
                        } catch (DBException dbEx) {
                            // commented for using UtilFactory.log instead of printStackTrace
                            UtilFactory.log(dbEx.getMessage(), dbEx, "ProposalMaintenanceServlet", "doPost");
//                                dbEx.printStackTrace();
                            proposalNarrativeTxnBean.transactionRollback();
                            throw dbEx;
                        } finally {
                            proposalNarrativeTxnBean.endConnection();
                        }
                        // Code added by Shivakumar for locking enhancement - END
                    }
                } else {
                    //Message
                    throw new CoeusException("narrativeAuthorization_exceptionCode.4100");
                }
                /*Added for the Coeus Enhancement case:1776 step:4*/
                //Modified for bug id 1860 : start
                //Added by Shiji for right checking - step4 :start
            } else if (functionType == GET_NARRATIVE) {
                Character mode = (Character) requester.getDataObject();
                Vector data = (Vector) requester.getDataObjects();
                ProposalDevelopmentFormBean proposalDevtFormBean =
                        proposalDataTxnBean.getProposalDevelopmentDetails(proposalNumber);
                int statusCode = proposalDevtFormBean.getCreationStatusCode();
                boolean isNarrOpenedFromBaseWindow = ((Boolean) data.get(0)).booleanValue();
                UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
                String unitNumber = (String) data.get(1);
                String topLevelUnit = userMaintDataTxnBean.getCampusForDept(unitNumber);
                boolean hasRight = false;
                //Code commented and modified for case#3316 - Bug fix - starts
//                    boolean hasViewRightOnly=false;
//                    boolean hasAlterRight = false;
//                    boolean isModifyNarr = false;
                //If Narrative is opened from Base window
//                    ProposalDevelopmentTxnBean proposalDevelopmentTxnBean = new ProposalDevelopmentTxnBean();
                // Check if user has any OSP Right
                // Case 1856
//                    hasRight = userMaintDataTxnBean.getUserHasAnyOSPRight(loggedinUser);
//                    if(hasRight) {
//                            if(statusCode != 1 && statusCode != 3) {
//                                hasViewRightOnly = true;
//                            }
//                        }
//                        //Check if user has MODIFY_ANY_PROPOSAL right at lead unit
//                        hasRight=userMaintDataTxnBean.getUserHasRight(loggedinUser,MODIFY_ANY_PROPOSAL,unitNumber);
//                        if(hasRight) {
//                            if(statusCode != 1 && statusCode != 3) {
//                                hasViewRightOnly = true;
//                            }
//                        }
//                        //If not check if user has MODIFY_NARRATIVE right
//                        else if(!hasRight){
//                                hasRight = userMaintDataTxnBean.getUserHasProposalRight(loggedinUser, proposalNumber, MODIFY_NARRATIVE);
//                                if(hasRight) {
//                                    if(statusCode != 1 && statusCode != 3) {
//                                        hasViewRightOnly = true;
//                                    }if(statusCode == 2 || statusCode == 4){
//                                        hasAlterRight = true;
//                                        isModifyNarr =true;
//                                    }
//                                }
//                                  //If not present check if user has VIEW_ANY_PROPOSAL right at lead unit level
//                                else if(!hasRight) {
//                                        hasRight=userMaintDataTxnBean.getUserHasRight(loggedinUser,VIEW_ANY_PROPOSAL,unitNumber);
//                                        if(hasRight) {
//                                            hasViewRightOnly=true;
//                                        }
//                                        //If not check if user has VIEW_NARRATIVE right
//                                        else if(!hasRight) {
//                                            hasRight = userMaintDataTxnBean.getUserHasProposalRight(loggedinUser, proposalNumber, VIEW_NARRATIVE);
//                                            if(hasRight) {
//                                                hasViewRightOnly=true;
//                                            }
//                                            //IF user has any OSP right, and the proposal status is (2:Approval In Progress,4: Approved,5: Submitted, 
//                                            //6. Post-Submission Approval or 7. Post-Submission Rejection). 
//                                            else if(!hasRight) {
//                                                if(statusCode == 2 || statusCode== 4 || statusCode == 5 || statusCode == 6 || statusCode == 7) {
//                                                    hasRight =  userMaintDataTxnBean.getUserHasAnyOSPRight(loggedinUser);
//                                                    if(hasRight) {
//                                                        hasViewRightOnly=true;
//                                                    }
//                                                }
//                                            }
//                                        }
//                                    }
//                                }
//                        if(!isNarrOpenedFromBaseWindow) {
//                            if( mode.charValue() == 'D') {
//                                hasViewRightOnly=true;
//                            }
//                        }
                // Added for the Coeus enhancement case:#1767

//                     if(statusCode == 2 || statusCode == 4){
//                         hasAlterRight = userMaintDataTxnBean.getUserHasOSPRight(loggedinUser, ALTER_PROPOSAL_DATA);
//                         if(!hasAlterRight && isModifyNarr) {
//                             hasAlterRight = true; 
//                         }
//                     }
                HashMap hmNarrativeRights = getNarrativeRights(loggedinUser, unitNumber,
                        proposalNumber, statusCode);
                boolean canModifyNarrative = ((Boolean) hmNarrativeRights.get("canModifyNarrative")).booleanValue();
                boolean canViewNarrative = ((Boolean) hmNarrativeRights.get("canViewNarrative")).booleanValue();
                // COEUSDEV-319: Premium - Change menu label in Protocol window - Protocol Actions --> Approve - Start
                boolean hasPropRightToModify = ((Boolean) hmNarrativeRights.get("hasProposalRightToModify")).booleanValue();
                boolean hasOSPDeptRightToModify = ((Boolean) hmNarrativeRights.get("hasOSPDeptRightToModify")).booleanValue();
                // COEUSDEV-319: Premium - Change menu label in Protocol window - Protocol Actions --> Approve - End
                //End Coeus Enhancement case:#1767
                //COEUSQA-3964 Start
                if(!canViewNarrative){
                    ProposalDevelopmentTxnBean proposalDevelopmentTxnBean = new ProposalDevelopmentTxnBean();
                    canViewNarrative = proposalDevelopmentTxnBean.IsUserPI(proposalNumber, loggedinUser, ModuleConstants.PROPOSAL_DEV_MODULE_CODE);
                }
                //COEUSQA-3964 End
                //If User has atleast one of the above rights
                //if(hasRight || hasAlterRight){
                if (canModifyNarrative || canViewNarrative) {
//                        if((!hasViewRightOnly && !hasAlterRight) || (!hasViewRightOnly && hasAlterRight && isModifyNarr)) {
//                            //Check whether Proposal can be modified
//                            ProposalActionTxnBean proposalActionTxnBean = new ProposalActionTxnBean();
//                            int canModify = proposalActionTxnBean.canModifyDevelopmentProposal(proposalNumber);
//                            if(canModify <= 0 ){
//                                hasViewRightOnly=true; 
//                            }
//                        }
                    //Code commented and modified for case#3316 - Bug fix - ends
                    ProposalNarrativeTxnBean proposalNarrativeTxnBean = new ProposalNarrativeTxnBean();
                    Vector narratives = proposalNarrativeTxnBean.getProposalNarrativeDetails(proposalNumber);

                    /*added for the coeus narrative enhancements case : 1624 start step:1*/
                    Vector narrativeTypes = proposalNarrativeTxnBean.getProposalNarrativeTypes();
                    /*end step1 */

                    /*Added for the Coeus Enhancement case:1776 ,to get the parameter value from the parameter table start step:2*/
                    CoeusFunctions coeusFunctions = new CoeusFunctions();
                    String narrativeTypeCode = coeusFunctions.getParameterValue("NARRATIVE_TYPE_OTHER_CODE");
                    /*End Coeus Enhancement case:1776 step:2*/
                    //Added for case# 3183 ProposalHierarchy - start
                    ProposalHierarchyTxnBean txn = new ProposalHierarchyTxnBean();
                    HashMap hmProposalData = txn.getParentProposalData(proposalNumber);
                    Vector vecPropHierLink = proposalNarrativeTxnBean.getPropHierLinkData(proposalNumber);
                    //Added for case# 3183 ProposalHierarchy - end
                    dataObjects = new Vector();
                    dataObjects.addElement(new Boolean(hasRight));
                    dataObjects.addElement(narratives);

                    /*added for the coeus narrative enhancements case : 1624 start step:2*/
                    dataObjects.addElement(narrativeTypes);
                    /*end step2*/

                    /* Added for the CoeusEnhancement case:1776 step:3*/
                    dataObjects.addElement(narrativeTypeCode);
                    /*End CoeusEnhancement case:1776 step:3*/
                    //Code commented and modified for case#3316 - Bug fix
                    //dataObjects.addElement(new Boolean(hasViewRightOnly));
                    dataObjects.addElement(new Boolean(canViewNarrative));
                    //Added for the Coeus Enhancement case:#1767
                    //Code commented and modified for case#3316 - Bug fix
                    //dataObjects.addElement(new Boolean(hasAlterRight));
                    dataObjects.addElement(new Boolean(canModifyNarrative));
                    //End Coeus Enhancement case:#1767
                    //Added for case# 3183 ProposalHierarchy - start
                    dataObjects.addElement(hmProposalData);

                    dataObjects.addElement(vecPropHierLink);
                    //Added for case# 3183 ProposalHierarchy - end
                    // COEUSDEV-319: Premium - Change menu label in Protocol window - Protocol Actions --> Approve - Start
                    dataObjects.addElement(hasOSPDeptRightToModify);
                    dataObjects.addElement(hasPropRightToModify);
                    // COEUSDEV-319: Premium - Change menu label in Protocol window - Protocol Actions --> Approve - End
                    responder.setDataObjects(dataObjects);
                    //Try to get Narrative Lock
                    //Budget Data is required to display in Display mode
                    //even if Budget is locked by some other User
                    //added to lock the narrative only if the functiontype is modify
                    // IF condition modified by Shiva 01/09/2004
                    //                         if( !hasRight&&mode.charValue() != 'D'){
                    //Code commented and modified for case#3316 - Bug fix
                    //if( mode.charValue() != 'D' && !hasAlterRight && !hasViewRightOnly){
                    //Modified for Proposal Narrative Locking Issue case id 3316 - start
                    if (canModifyNarrative) {
                        //Modified for Proposal Narrative Locking Issue case id 3316 - end
                        // Code commented by Shivakumar for locking enhancement - BEGIN
                        //                            proposalNarrativeTxnBean.getNarrativeLock(proposalNumber);
                        // Code commented by Shivakumar for locking enhancement - END
                        // Code added by Shivakumar for locking enhancement - BEGIN
                        try {
                            // 2930: Auto-delete Current Locks based on new parameter - Start
                            // Get the Lock for the Narrative and send the Locking Bean Back to client side
//                                proposalNarrativeTxnBean.getNarrativeLock(proposalNumber,loggedinUser,proposalNumber);
//                                proposalNarrativeTxnBean.transactionCommit();
                            LockingBean lockingBean = proposalNarrativeTxnBean.getNarrativeLock(proposalNumber, loggedinUser, proposalNumber);
                            proposalNarrativeTxnBean.transactionCommit();
                            responder.setLockingBean(lockingBean);
                            // 2930: Auto-delete Current Locks based on new parameter - End
                        } catch (DBException dbEx) {
                            // commented for using UtilFactory.log instead of printStackTrace
                            UtilFactory.log(dbEx.getMessage(), dbEx, "ProposalMaintenanceServlet", "doPost");
//                                dbEx.printStackTrace();
                            proposalNarrativeTxnBean.transactionRollback();
                            throw dbEx;
                        } finally {
                            proposalNarrativeTxnBean.endConnection();
                        }
                        // Code added by Shivakumar for locking enhancement - END
                    }
                } else {
                    //Message
                    throw new CoeusException("narrativeAuthorization_exceptionCode.4100");
                }
                /*Added for the Coeus Enhancement case:1776 step:4*/
            }//right checking - step4 :end
            //bug id 1860 : end
            //Added by shiji for Case id:2016 step2 - start
            else if (functionType == SEND_NOTIFICATION) {
                CoeusVector cvApprovalMaps = null;
                CoeusVector cvApprovers = new CoeusVector();
                //Added for case#2999
                CoeusVector cvRoutingDetails = new CoeusVector();
                ProposalActionTxnBean proposalActionTxnBean = new ProposalActionTxnBean();
//                    String proposalNo = requester.getId();
                String message = (String) requester.getDataObject();

                String fromUser = requester.getUserName();
                //Gets the approval maps for the proposal
                //Commented for case#2999 - Multiple emails generated when a document is uploaded - start
//                    cvApprovalMaps = (CoeusVector)proposalActionTxnBean.getProposalApprovalMaps(proposalNumber);
//                    if(cvApprovalMaps != null && cvApprovalMaps.size() != 0) {
//                        for(int i=0;i< cvApprovalMaps.size();i++) {
//                            ProposalApprovalMapBean proposalApprovalMapBean=(ProposalApprovalMapBean)cvApprovalMaps.get(i);
//                            if(proposalApprovalMapBean.getProposalApprovals() != null)
//                                cvApprovers.addAll(proposalApprovalMapBean.getProposalApprovals());//Gets the approvers for each approval map and adds to a vector
//                        }
//                    }
                //Added for Case#4572 - Writing notepad entries and sending notifications irrespective of proposal status - Start
                //Mails are send only to approvers when proposal status is approvel in progress
                if (proposalDevelopmentFormBean == null) {
                    proposalDevelopmentFormBean = proposalDataTxnBean.getProposalDevelopmentDetails(proposalNumber);

                }
                if (proposalDevelopmentFormBean != null && proposalDevelopmentFormBean.getCreationStatusCode() == APPROVEL_IN_PROGRESS) {//Case#4572 - End
                    //Commented for case#2999 - Multiple emails generated when a document is uploaded - end
                    //Added for case#2999 - Multiple emails generated when a document is uploaded - start
                    RoutingTxnBean routingTxnBean = new RoutingTxnBean();
                    RoutingBean routingBean = new RoutingBean();
                    routingBean = routingTxnBean.getRoutingHeader("3", proposalNumber, "0", 0);
                    if (routingBean != null) {
                        cvApprovalMaps = (CoeusVector) routingTxnBean.getRoutingMaps(routingBean.getRoutingNumber());
                    }

                    if (cvApprovalMaps != null && cvApprovalMaps.size() > 0) {
                        for (int index = 0; index < cvApprovalMaps.size(); index++) {
                            RoutingMapBean routingMapBean = (RoutingMapBean) cvApprovalMaps.get(index);
                            cvRoutingDetails.add(routingMapBean.getRoutingMapDetails());
                        }
                    }
                    if (cvRoutingDetails != null && cvRoutingDetails.size() > 0) {
                        for (int index = 0; index < cvRoutingDetails.size(); index++) {
                            CoeusVector cvRoutingDetail = (CoeusVector) cvRoutingDetails.get(index);
                            if (cvRoutingDetail != null && cvRoutingDetail.size() > 0) {
                                for (int index1 = 0; index1 < cvRoutingDetail.size(); index1++) {
                                    RoutingDetailsBean routingDetailsBean = (RoutingDetailsBean) cvRoutingDetail.get(index1);
                                    cvApprovers.add(routingDetailsBean);
                                }
                            }
                        }
                    }
                    //Added for case#2999 - Multiple emails generated when a document is uploaded - end
                    boolean success = sendNotification(cvApprovers, message, proposalNumber, fromUser);
                    responder.setResponseStatus(success);
                    responder.setMessage(null);
                } else {
                    responder.setResponseStatus(false);
                    responder.setMessage(null);
                }
            } //Case id:2016 step 2 - end
            else if (functionType == GET_ADD_NARRATIVE_DATA) {
                dataObjects = new Vector();
                //addition for case 2333
                String proposalNo = requester.getId();
                //end addition case 2333
                ProposalNarrativeTxnBean proposalNarrativeTxnBean = new ProposalNarrativeTxnBean();

                /*added for the coeus narrative enhancements case : 1624 start step:1*/
                //start change case 2333
                //Vector narrativeTypes = proposalNarrativeTxnBean.getProposalNarrativeTypes();
                Vector narrativeTypes = proposalNarrativeTxnBean.getFilteredProposalNarrativeTypes(proposalNo);
                //end change case 2333
                    /*end step1 */

                //Added for the Coeus Enhancement
                CoeusFunctions coeusFunctions = new CoeusFunctions();
                String narrativeTypeCode = coeusFunctions.getParameterValue("NARRATIVE_TYPE_CODE");

                /*added for the coeus narrative enhancements case : 1624 start step:2*/
                dataObjects.addElement(narrativeTypes);
                /*end step2*/
                dataObjects.addElement(narrativeTypeCode);
                responder.setDataObjects(dataObjects);

            }/*End CoeusEnhancement case:1776 step:4*/ else if (functionType == GET_NARRATIVE_USERS_FOR_PROPOSAL) {

                Vector narrativeUsers = proposalDataTxnBean.getUsersForProposalRight(proposalNumber);
                dataObjects.addElement(narrativeUsers);

            } else if (functionType == UPDATE_NARRATIVE_DETAILS) {

                proposalUpdTxnBean = new ProposalDevelopmentUpdateTxnBean(loggedinUser);
                Vector narrativeData = (Vector) requester.getDataObjects();

                //Modified for Case# 3316 - starts
                //Before saving check whether the user has the narrative lock
                ProposalNarrativeTxnBean proposalNarrativeTxnBean = new ProposalNarrativeTxnBean(loggedinUser);
                ProposalNarrativeFormBean formBean = (ProposalNarrativeFormBean) narrativeData.get(0);
                String proposalId = formBean.getProposalNumber();
                proposalNarrativeTxnBean = new ProposalNarrativeTxnBean(loggedinUser);
                boolean isAvailable = proposalNarrativeTxnBean.hasNarrativeLock(proposalId);
                if (isAvailable) {
                    // Added for the Coeus enhancement case:#1767
                    ProposalNarrativeFormBean formbean = null;
                    Vector vecNarrData = new Vector();
                    for (int index = 0; index < narrativeData.size(); index++) {
                        if (narrativeData.elementAt(index) instanceof ProposalNarrativeFormBean) {
                            formbean = (ProposalNarrativeFormBean) narrativeData.elementAt(index);
                            vecNarrData.add(formbean);
                        }
                    }
                    Vector alterProposalData = (Vector) narrativeData.elementAt(narrativeData.size() - 1);
                    //ProposalNarrativeTxnBean proposalNarrativeTxnBean = new ProposalNarrativeTxnBean();
                    // Commented for Case 3316 - OSP/Departemental User able to modify the narrative modules -Start
//                        String unitNumber = null;
//                        boolean canAlterPropData = false;
//                        if(alterProposalData != null && alterProposalData.size()>0) {
//                            unitNumber = (String)alterProposalData.get(0);
//                            canAlterPropData = ((Boolean)alterProposalData.get(1)).booleanValue();
//                        if(canAlterPropData){
//                            try{
//                                proposalNarrativeTxnBean.getNarrativeLock(proposalNumber,loggedinUser,unitNumber);
//                                proposalNarrativeTxnBean.transactionCommit();
//                            }catch(DBException dbEx){
//                                    // commented for using UtilFactory.log instead of printStackTrace
//                                    UtilFactory.log(dbEx.getMessage(),dbEx,"ProposalMaintenanceServlet", "doPost");
//    //                            dbEx.printStackTrace();
//                                proposalNarrativeTxnBean.transactionRollback();
//                                throw dbEx;
//                            }finally{
//                                proposalNarrativeTxnBean.endConnection();
//                            }
//                        }
//                        }
                    // End Coeus enhancement case:#1767
                    // Commented for Case 3316 - OSP/Departemental User able to modify the narrative modules -End
                    if (proposalUpdTxnBean.addUpdDeleteProposalNarrativeDetails(vecNarrData)) {
                        ProposalHierarchyTxnBean hierarchyTxnBean = new ProposalHierarchyTxnBean(loggedinUser);
                        hierarchyTxnBean.updateSyncFlag(proposalNumber, "P");
                        //ProposalNarrativeTxnBean proposalNarrativeTxnBean = new ProposalNarrativeTxnBean();
                        narrativeData = new Vector();
                        Vector narratives = proposalNarrativeTxnBean.getProposalNarrativeDetails(proposalNumber);
                        /*added for the coeus narrative enhancements case :1624 start step 3*/
                        Vector narrativeTypes = proposalNarrativeTxnBean.getProposalNarrativeTypes();
                        /*end step3*/

                        /*Added for the Coeus Enhancement case:1776 to get the narrativetype code value even after updating step:5*/
                        CoeusFunctions coeusFunctions = new CoeusFunctions();
                        String narrativeTypeCode = coeusFunctions.getParameterValue("NARRATIVE_TYPE_CODE");
                        /*End CoeusEnhancement case:1776 step:5*/

                        responder.setResponseStatus(true);
                        dataObjects.addElement(narratives);

                        /*added for the coeus narrative enhancements case :1624 start step 4*/
                        dataObjects.addElement(narrativeTypes);
                        /*end step4*/

                        /*Added for the Coeus Enhancement case:1776 step:6*/
                        dataObjects.addElement(narrativeTypeCode);
                        /*End CoeusEnhancement case:1776 step:6*/
                        // Commented for Case 3316 - OSP/Departemental User able to modify the narrative modules -Start
                        //Added for the Coeus Enhancemnt case:#1767
//                            if(canAlterPropData){
//                                proposalNarrativeTxnBean.releaseLock(proposalNumber,loggedinUser);
//                            }
                        //eND for the Coeus Enhancemnt case:#1767
                        // Commented for Case 3316 - OSP/Departemental User able to modify the narrative modules -End
                    } else {
                        responder.setResponseStatus(false);
                        responder.setId("-1");
                    }
                    //Modified for Case# 3316 - ends
//                                        }
                    //                }else if(functionType == GET_NARRATIVE_PDF){
                    //                    //Added for case #1860 start 1
                    //                    Vector data = requester.getDataObjects();
                    //                    //Added for case #1860 end 1
                    //                    //Commented for case #1860 start 2
                    //                    //ProposalNarrativePDFSourceBean proposalNarrativePDFSourceBean = (ProposalNarrativePDFSourceBean)requester.getDataObject();
                    //                    //commented for case #1860 end 2
                    //                    //Modified for case #1860 start 3
                    //                    ProposalNarrativePDFSourceBean proposalNarrativePDFSourceBean = (ProposalNarrativePDFSourceBean)data.get(0);
                    //                    //Modified for case #1860 end 3
                    //                    //Check for Rights
                    //                    ProposalNarrativeTxnBean proposalNarrativeTxnBean = new ProposalNarrativeTxnBean();
                    //                    boolean hasRight = proposalNarrativeTxnBean.canViewNarrativeModule(loggedinUser, proposalNarrativePDFSourceBean.getProposalNumber() , proposalNarrativePDFSourceBean.getModuleNumber());
                    //
                    //                    UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
                    //                    //Commented for case #1860 start 4
                    //                    //If no rights check for OSP right
                    ////                    if(!hasRight){
                    ////                        hasRight = userMaintDataTxnBean.getUserHasOSPRight(loggedinUser, VIEW_ANY_PROPOSAL);
                    ////                    }
                    //                    //Commented for case #1860 end 4
                    //                    //Added for case #1860 start 5
                    //                    String unitNumber = (String)data.get(1);
                    //                    ProposalDevelopmentFormBean proposalDevtFormBean =
                    //                        proposalDataTxnBean.getProposalDevelopmentDetails(proposalNumber );
                    //                    int statusCode = proposalDevtFormBean.getCreationStatusCode();
                    //                    if(!hasRight){
                    //                        hasRight=userMaintDataTxnBean.getUserHasRight(loggedinUser,MODIFY_ANY_PROPOSAL,unitNumber);
                    //                        //If not check if user has MODIFY_NARRATIVE right
                    //                        if(!hasRight){
                    //                            hasRight = userMaintDataTxnBean.getUserHasProposalRight(loggedinUser, proposalNumber, MODIFY_NARRATIVE);
                    //                            //If not present check if user has VIEW_ANY_PROPOSAL right at lead unit level
                    //                            if(!hasRight) {
                    //                                hasRight=userMaintDataTxnBean.getUserHasRight(loggedinUser,VIEW_ANY_PROPOSAL,unitNumber);
                    //                                //If not check if user has VIEW_NARRATIVE right
                    //                                if(!hasRight) {
                    //                                    hasRight = userMaintDataTxnBean.getUserHasProposalRight(loggedinUser, proposalNumber, VIEW_NARRATIVE);
                    //                                    //IF user has any OSP right, and the proposal status is (2:Approval In Progress,4: Approved,5: Submitted,
                    //                                    //6. Post-Submission Approval or 7. Post-Submission Rejection).
                    //                                    if(!hasRight) {
                    //                                        if(statusCode == 2 || statusCode== 4 || statusCode == 5 || statusCode == 6 || statusCode == 7) {
                    //                                            hasRight =  userMaintDataTxnBean.getUserHasAnyOSPRight(loggedinUser);
                    //                                        }
                    //                                    }
                    //                                }
                    //                            }
                    //                        }
                    //                    }
                    //                    ///Added for case #1860 end 5
                    //                    dataObjects = new Vector();
                    //                    if(hasRight){
                    //                        //byte[] fileData = proposalDataTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                    //                        proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                    //                        //if(fileData!=null){
                    //                        // To upload any file. Case #1779- start
                    //                        if(proposalNarrativePDFSourceBean!=null && proposalNarrativePDFSourceBean.getFileBytes() != null){
                    //                            String fileName=proposalNarrativePDFSourceBean.getFileName();
                    //                            String extension="";
                    //                            if(fileName != null && !fileName.trim().equals("")){
                    //                                int index = fileName.lastIndexOf('.');
                    //                                if(index != -1 && index != fileName.length()){
                    //                                    extension = fileName.substring(index+1,fileName.length());
                    //                                }
                    //                            }
                    //                            // To upload any file Case #1779- end
                    //                            byte[] fileData = proposalNarrativePDFSourceBean.getFileBytes();
                    //                            //Create the file in Server
                    //                            CoeusConstants.SERVER_HOME_PATH = this.getServletContext().getRealPath("/");
                    //                            //                            InputStream is = getClass().getResourceAsStream("/coeus.properties");
                    //                            //                            Properties coeusProps = new Properties();
                    //                            //                            coeusProps.load(is);
                    //                            String reportPath = CoeusProperties.getProperty(CoeusPropertyKeys.REPORT_GENERATED_PATH); //get path (to generate PDF) from config
                    //
                    //                            String filePath = CoeusConstants.SERVER_HOME_PATH+File.separator+reportPath;
                    //                            File reportDir = new File(filePath);
                    //                            if(!reportDir.exists()){
                    //                                reportDir.mkdirs();
                    //                            }
                    //                            SimpleDateFormat dateFormat= new SimpleDateFormat("MMddyyyy-hhmmss");
                    //                            //Added for viewing the files if there is no extension,bug fix prior to Enhancement case:1779 start
                    //                            if(extension.equals(""))
                    //                                extension = "pdf";
                    //                            //End Bug Fix
                    //
                    //                            //Modified to upload any file-start
                    //                            //File reportFile = new File(reportDir + File.separator + "NarrativePDF"+dateFormat.format(new Date())+"."+extension);
                    //                            File reportFile = new File(reportDir + File.separator + "Narrative"+dateFormat.format(new Date())+"."+extension);
                    //                            //Modified to upload any file-end
                    //                            //Added on 9th Feb 2004 - start
                    //                            reportFile.deleteOnExit();
                    //                            //Added on 9th Feb 2004 - end
                    //                            FileOutputStream fos = new FileOutputStream(reportFile);
                    //                            fos.write( fileData,0,fileData.length );
                    //                            fos.close();
                    //                            //vctPath.addElement(  );
                    //                            dataObjects.addElement(new Boolean(hasRight));
                    //                            dataObjects.addElement("/"+reportPath+"/"+reportFile.getName());
                    //                        }else{
                    //                            dataObjects.addElement(new Boolean(hasRight));
                    //                            dataObjects.addElement(null);
                    //                        }
                    //                    }else{
                    //                        dataObjects.addElement(new Boolean(hasRight));
                    //                        dataObjects.addElement(null);
                    //                    }
                    //                    responder.setDataObjects(dataObjects);
                    //                    responder.setResponseStatus( true );
                    //                }else if(functionType == GET_NARRATIVE_SOURCE){
                    //                    //Added for case #1860 start 6
                    //                    Vector data = requester.getDataObjects();
                    //                    //Added for case #1860 end 6
                    //                    //Commented for case #1860 start 7
                    //                    //ProposalNarrativePDFSourceBean proposalNarrativePDFSourceBean = (ProposalNarrativePDFSourceBean)requester.getDataObject();
                    //                    //Commented for case #1860 end 7
                    //                    //Added for case #1860 start 8
                    //                    ProposalNarrativePDFSourceBean proposalNarrativePDFSourceBean = (ProposalNarrativePDFSourceBean)data.get(0);
                    //                    //Added for case #1860 end 8
                    //                    //Check for Rights
                    //                    ProposalNarrativeTxnBean proposalNarrativeTxnBean = new ProposalNarrativeTxnBean();
                    //                    boolean hasRight = proposalNarrativeTxnBean.canViewNarrativeModule(loggedinUser, proposalNarrativePDFSourceBean.getProposalNumber() , proposalNarrativePDFSourceBean.getModuleNumber());
                    //
                    //                    UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
                    //                    //Commented for case #1860 start 9
                    //                    //If no rights check for OSP right
                    ////                    if(!hasRight){
                    ////                        hasRight = userMaintDataTxnBean.getUserHasOSPRight(loggedinUser, VIEW_ANY_PROPOSAL);
                    ////                    }
                    //                    //Commented for case #1860 end 9
                    //                    //Added for case #1860 start 10
                    //                    String unitNumber = (String)data.get(1);
                    //                    ProposalDevelopmentFormBean proposalDevtFormBean =
                    //                        proposalDataTxnBean.getProposalDevelopmentDetails(proposalNumber );
                    //                    int statusCode = proposalDevtFormBean.getCreationStatusCode();
                    //                    if(!hasRight){
                    //                        hasRight=userMaintDataTxnBean.getUserHasRight(loggedinUser,MODIFY_ANY_PROPOSAL,unitNumber);
                    //                        //If not check if user has MODIFY_NARRATIVE right
                    //                        if(!hasRight){
                    //                            hasRight = userMaintDataTxnBean.getUserHasProposalRight(loggedinUser, proposalNumber, MODIFY_NARRATIVE);
                    //                            //If not present check if user has VIEW_ANY_PROPOSAL right at lead unit level
                    //                            if(!hasRight) {
                    //                                hasRight=userMaintDataTxnBean.getUserHasRight(loggedinUser,VIEW_ANY_PROPOSAL,unitNumber);
                    //                                //If not check if user has VIEW_NARRATIVE right
                    //                                if(!hasRight) {
                    //                                    hasRight = userMaintDataTxnBean.getUserHasProposalRight(loggedinUser, proposalNumber, VIEW_NARRATIVE);
                    //                                    //IF user has any OSP right, and the proposal status is (2:Approval In Progress,4: Approved,5: Submitted,
                    //                                    //6. Post-Submission Approval or 7. Post-Submission Rejection).
                    //                                    if(!hasRight) {
                    //                                        if(statusCode == 2 || statusCode== 4 || statusCode == 5 || statusCode == 6 || statusCode == 7) {
                    //                                            hasRight =  userMaintDataTxnBean.getUserHasAnyOSPRight(loggedinUser);
                    //                                        }
                    //                                    }
                    //                                }
                    //                            }
                    //                        }
                    //                    }
                    //                    //Added for case #1860 end 10
                    //                    dataObjects = new Vector();
                    //                    if(hasRight){
                    //                        //byte[] fileData = proposalDataTxnBean.getNarrativeSource(proposalNarrativePDFSourceBean);
                    //                        proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativeSource(proposalNarrativePDFSourceBean);
                    //
                    //                        if(proposalNarrativePDFSourceBean!=null && proposalNarrativePDFSourceBean.getFileBytes() != null){
                    //                            byte[] fileData = proposalNarrativePDFSourceBean.getFileBytes();
                    //                            //Create the file in Server
                    //                            CoeusConstants.SERVER_HOME_PATH = this.getServletContext().getRealPath("/");
                    //                            InputStream is = getClass().getResourceAsStream("/coeus.properties");
                    //                            Properties coeusProps = new Properties();
                    //                            coeusProps.load(is);
                    //                            String reportPath = coeusProps.getProperty("REPORT_GENERATED_PATH"); //get path (to generate PDF) from config
                    //
                    //                            String filePath = CoeusConstants.SERVER_HOME_PATH+File.separator+reportPath;
                    //                            File reportDir = new File(filePath);
                    //                            if(!reportDir.exists()){
                    //                                reportDir.mkdirs();
                    //                            }
                    //                            SimpleDateFormat dateFormat= new SimpleDateFormat("MMddyyyy-hhmmss");
                    //                            File reportFile = new File(reportDir + File.separator + "NarrativeSource"+dateFormat.format(new Date())+".doc");
                    //                            //Added on 9th Feb 2004 - start
                    //                            reportFile.deleteOnExit();
                    //                            //Added on 9th Feb 2004 - end
                    //                            FileOutputStream fos = new FileOutputStream(reportFile);
                    //                            fos.write( fileData,0,fileData.length );
                    //                            fos.close();
                    //                            //vctPath.addElement(  );
                    //                            dataObjects.addElement(new Boolean(hasRight));
                    //                            dataObjects.addElement("/"+reportPath+"/"+reportFile.getName());
                    //                        }else{
                    //                            dataObjects.addElement(new Boolean(hasRight));
                    //                            dataObjects.addElement(null);
                    //                        }
                    //                    }else{
                    //                        dataObjects.addElement(new Boolean(hasRight));
                    //                        dataObjects.addElement(null);
                    //                    }
                    //                    responder.setDataObjects(dataObjects);
                    //                    responder.setResponseStatus( true );
                } else {
                    responder.setResponseStatus(false);
                    responder.setId("-1");
                }
                //Code modified for case#2420 - Upload of files on Edit Module Details Window - end
            } else if (functionType == UPDATE_NARRATIVE_PDF) {
                //Code modified for case#2420 - Upload of files on Edit Module Details Window - start
                ProposalNarrativePDFSourceBean proposalNarrativePDFSourceBean = (ProposalNarrativePDFSourceBean) requester.getDataObject();
                String proposalId = proposalNarrativePDFSourceBean.getProposalNumber();
                ProposalNarrativeTxnBean proposalNarrativeTxnBean = new ProposalNarrativeTxnBean(loggedinUser);
                boolean isAvailable = proposalNarrativeTxnBean.hasNarrativeLock(proposalId);
                if (isAvailable) {
                    boolean isUpdate = proposalNarrativeTxnBean.addUpdProposalNarrativePDF(proposalNarrativePDFSourceBean);
                    if (isUpdate) {
                        //Added for case# 3183 - Proposal Hierarchy - start
                        ProposalHierarchyTxnBean hierarchyTxnBean = new ProposalHierarchyTxnBean(loggedinUser);
                        hierarchyTxnBean.updateSyncFlag(proposalNarrativePDFSourceBean.getProposalNumber(), "P");
                        proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                        proposalNarrativePDFSourceBean.setFileBytes(null);
                        //Added for case# 3183 - Proposal Hierarchy - end
                        responder.setDataObject(new Boolean(isUpdate));
                        responder.setResponseStatus(true);
                        responder.setId("1");
                    } else {
                        responder.setResponseStatus(false);
                        responder.setId("0");
                    }
                    //Added for case# 3183 - Proposal Hierarchy - start
                    dataObjects.add(proposalNarrativePDFSourceBean);
                    //Added for case# 3183 - Proposal Hierarchy - end
                } else {
                    responder.setResponseStatus(false);
                    responder.setId("-1");
                }
                //Code modified for case#2420 - Upload of files on Edit Module Details Window - end
            } else if (functionType == GET_PERSON_DOC_CODE) {
                ProposalPersonTxnBean proposalPersonTxnBean = new ProposalPersonTxnBean();
                dataObjects = proposalPersonTxnBean.getPersonDocumentTypeCodes();
            } //Commented for case 3685 - Removed source tables from database - start
            //                else if(functionType == UPDATE_NARRATIVE_SOURCE){
            //                    ProposalNarrativePDFSourceBean proposalNarrativePDFSourceBean = (ProposalNarrativePDFSourceBean)requester.getDataObject();
            //                    //proposalUpdTxnBean = new ProposalDevelopmentUpdateTxnBean( loggedinUser );
            //                    ProposalNarrativeTxnBean proposalNarrativeTxnBean = new ProposalNarrativeTxnBean(loggedinUser);
            //                    boolean isUpdate = proposalNarrativeTxnBean.addUpdProposalNarrativeSource(proposalNarrativePDFSourceBean);
            //                    if(isUpdate){
            //                        ProposalHierarchyTxnBean hierarchyTxnBean = new ProposalHierarchyTxnBean(loggedinUser);
            //                        hierarchyTxnBean.updateSyncFlag(proposalNarrativePDFSourceBean.getProposalNumber(), "P");
            //                    }
            //                    responder.setDataObject(new Boolean(isUpdate));
            //                    responder.setResponseStatus(true);
            //                    responder.setMessage(null);
            //                }
            //Commented for case 3685 - Removed source tables from database - end
            else if (functionType == GET_PROPOSAL_STATUS) {
                dataObjects = proposalDataTxnBean.getProposalStatus();
                responder.setDataObjects(dataObjects);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            } else if (functionType == CHECK_ROLE_HAS_NARRATIVE_RIGHT) {
                int roleId = ((Integer) requester.getDataObject()).intValue();
                ProposalNarrativeTxnBean proposalNarrativeTxnBean = new ProposalNarrativeTxnBean();
                boolean blnRoleHasNarrRight = proposalNarrativeTxnBean.getRoleHasNarrativeRight(roleId);
                responder.setDataObject(new Boolean(blnRoleHasNarrRight));
                responder.setResponseStatus(true);
                responder.setMessage(null);
            } //Modified for bug id 1856 step 1 :start
            //Added by Shiji for right checking - step5 :start
            else if (functionType == CHECK_USER_HAS_RIGHT) {
                Vector vecParams = requester.getDataObjects();
                UserMaintDataTxnBean dataTxnBean = new UserMaintDataTxnBean();
                String userId = null;
                String unitNumber = null;
                String rightId = null;

                if (vecParams != null) {
                    userId = (String) vecParams.elementAt(0);
                    unitNumber = (String) vecParams.elementAt(1);
                    rightId = (String) vecParams.elementAt(2);
                }

                boolean userHasRight = dataTxnBean.getUserHasRight(userId, rightId, unitNumber);
                responder.setDataObject(new Boolean(userHasRight));
                responder.setResponseStatus(true);
            }//right checking - step5 :end
            //bug id 1856 step 1 : end
            //Modified for bug id 1856 step 2: start
            else if (functionType == USER_HAS_RIGHT) {

                Vector vecParams = requester.getDataObjects();

                String userId = null;
                String propNumber = null;
                String rightId = null;

                if (vecParams != null) {
                    userId = (String) vecParams.elementAt(0);
                    propNumber = (String) vecParams.elementAt(1);
                    rightId = (String) vecParams.elementAt(2);
                }

                UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
                boolean userHasRight = userMaintDataTxnBean.getUserHasProposalRight(userId, propNumber, rightId);
                 //COEUSQA-3964 Start
                if( rightId != null && ( rightId.equalsIgnoreCase(VIEW_RIGHT) || rightId.equalsIgnoreCase(VIEW_ANY_PROPOSAL))){
                    
                    if(!userHasRight){
                        ProposalDevelopmentTxnBean proposalDevelopmentTxnBean = new ProposalDevelopmentTxnBean();
                        userHasRight = proposalDevelopmentTxnBean.IsUserPI(propNumber, userId, ModuleConstants.PROPOSAL_DEV_MODULE_CODE);
                    }
                    
                }
                 //COEUSQA-3964 End
                   
                responder.setDataObject(new Boolean(userHasRight));
                responder.setResponseStatus(true);
            }//bug id 1856 step 2 : end
            else if (functionType == CHECK_LOCK_FOR_PROP_SPL_REVIEW) {//Case #1769  - start
                proposalDataTxnBean = new ProposalDevelopmentTxnBean();
                Vector data = requester.getDataObjects();
                String propNumber = (String) data.get(0);
                String unitNumber = (String) data.get(1);
                LockingBean lockingBean = proposalDataTxnBean.checkAndLock(propNumber, loggedinUser, unitNumber);
                boolean isAvailable = lockingBean.isGotLock();
                if (isAvailable) {
                    responder.setDataObject(new Boolean(isAvailable));
                    responder.setResponseStatus(true);
                    responder.setMessage(null);
                } else {
                    proposalDataTxnBean.transactionRollback();
                    CoeusMessageResourcesBean coeusMessageResourcesBean = new CoeusMessageResourcesBean();
                    UserMaintDataTxnBean userTxnBean = new UserMaintDataTxnBean();
                    UserInfoBean userInfoBean = userTxnBean.getUser(lockingBean.getUserID());
                    String msg = userInfoBean.getUserName() + " " + coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1000") + " Development Proposal " + propNumber + "  ";
                    throw new LockingException(msg);
                }//Case #1769  - End
                //Code added for Case# 3183 - Proposal Hierarchy - starts
            } else if (functionType == CAN_ADD_NARRATIVE) {
                ProposalNarrativeTxnBean proposalNarrativeTxnBean = new ProposalNarrativeTxnBean();
                ProposalNarrativeFormBean formBean = (ProposalNarrativeFormBean) requester.getDataObject();
                boolean canAdd = proposalNarrativeTxnBean.canAddNarrativeType(formBean);
                responder.setDataObject(new Boolean(canAdd));
                responder.setResponseStatus(true);
            } //Added for COEUSQA-1579 : For Hierarchy Proposal, Approval in Progress, cannot sync after narratives updated on child proposal - Start
            else if (functionType == PROPOSAL_CREATION_STATUS_CODE) {
                proposalNumber = (String) requester.getDataObject();
                ProposalDevelopmentTxnBean proposalDevelopmentTxnBean = new ProposalDevelopmentTxnBean();
                int proposalStatusCode = proposalDevelopmentTxnBean.getProposalStatusCode(proposalNumber);
                responder.setDataObject(new Integer(proposalStatusCode));
                responder.setResponseStatus(true);
            } //COEUSQA-1579 : End
             else if (functionType == CHECK_IS_PHS_HS_CT_FORM) {
                proposalNumber = (String) requester.getDataObject();
                ProposalDevelopmentTxnBean proposalDevelopmentTxnBean = new ProposalDevelopmentTxnBean();
                boolean caninclude = proposalDevelopmentTxnBean.isPHSHumanSubjectCTFormIncluded(proposalNumber);                
                responder.setDataObject(new Boolean(caninclude));
                responder.setResponseStatus(true);               
            }           
            
            else if (functionType == PROPOSAL_CREATION_STATUS_DETAILS) {
                proposalNumber = (String) requester.getDataObject();
                ProposalDevelopmentTxnBean proposalDevelopmentTxnBean = new ProposalDevelopmentTxnBean();
                ComboBoxBean protocolStatusDetail = proposalDevelopmentTxnBean.getProposalStatusDetails(proposalNumber);
                responder.setDataObject(protocolStatusDetail);
                responder.setResponseStatus(true);
            }
            // Added for COEUSQA-2778 : Submited Prop to Sponsor-Narrative Incomplete - Start
            else if (functionType == GET_NARRATIVE_STATUS) {
                ProposalDevelopmentTxnBean proposalDevelopmentTxnBean = new ProposalDevelopmentTxnBean();
                responder.setDataObject(proposalDevelopmentTxnBean.getNarrativeStatus(proposalNumber));
                responder.setResponseStatus(true);
            } // Added forCOEUSQA- 2778 : Submited Prop to Sponsor-Narrative Incomplete - End
            //Code added for Case# 3183 - Proposal Hierarchy - ends
            else {
                ProposalHierarchyBean proposalHierarchyBean = null;
                HashMap data = null;
                Vector vecRoleRightInfoBean = new Vector();
                if (functionType == DISPLAY_MODE) {
                    proposalDevelopmentFormBean =
                            proposalDataTxnBean.getProposalDevelopmentDetails(
                            proposalNumber);
                    vecRoleRightInfoBean = proposalDataTxnBean.getProposalRightForUser(proposalNumber, loggedinUser);
                    // Added by chandra - start
                    //* gnprh commented for Proposal Hierarchy
                    ProposalHierarchyTxnBean txn = new ProposalHierarchyTxnBean();
                    data = txn.getParentProposalData(proposalNumber);
                    String rootProposal = (String) data.get("PARENT_PROPOSAL");
                    if (rootProposal != null) {
                        proposalHierarchyBean = txn.getHierarchyData(rootProposal);
                    }

                    // End

                } else if (functionType == MODIFY_MODE) {
                    // Commented by Shivakumar for locking enhancement
                    //                            proposalDevelopmentFormBean =
                    //                            proposalDataTxnBean.getProposalDevelopmentDetails( proposalNumber, functionType );
                    //
                    // 2930: Auto-delete Current Locks based on new parameter - Start
                    // Get the lock for the proposal, and send the Locking Bean back to client side
//                        // Code added by Shivakumar -BEGIN
//                        proposalDevelopmentFormBean =
//                        proposalDataTxnBean.getProposalDevelopmentDetails( proposalNumber,loggedinUser,proposalNumber,functionType );
                    // Code added by Shivakumar -END
                    String unitNumber = (String) requester.getDataObject();
                    proposalDevelopmentFormBean = proposalDataTxnBean.getProposalDevelopmentDetails(proposalNumber);
                    LockingBean lockingBean = proposalDataTxnBean.lockDevProposal(proposalNumber, loggedinUser, unitNumber);
                    responder.setLockingBean(lockingBean);
                    // 2930: Auto-delete Current Locks based on new parameter - End
                    vecRoleRightInfoBean = proposalDataTxnBean.getProposalRightForUser(proposalNumber, loggedinUser);

                    // Added by chandra - start
                    //gnprh commented for Proposal Hierarchy
                    ProposalHierarchyTxnBean txn = new ProposalHierarchyTxnBean();
                    data = txn.getParentProposalData(proposalNumber);
                    String rootProposal = (String) data.get("PARENT_PROPOSAL");
                    if (rootProposal != null) {
                        proposalHierarchyBean = txn.getHierarchyData(rootProposal);
                    }
                    // Added by chandra - End

                } else if (functionType == ADD_MODE) {
                    String unitNumber = (String) requester.getDataObject();
                    proposalDevelopmentFormBean =
                            proposalDataTxnBean.getNewProposalDetails(
                            unitNumber, loggedinUser);
                    //Assign the new Proposal Number - start
                    proposalNumber = proposalDevelopmentFormBean.getProposalNumber();
                    //Assign the new Proposal Number - end
                    vecRoleRightInfoBean = proposalDataTxnBean.getProposalRightRole(CoeusConstants.PROPOSAL_ROLE_ID);
                    proposalDevelopmentFormBean.setCreateUser(loggedinUser);
                    //Set Creation Status as 1 - In Progress - start
                    //COEUSQA-1433 - Allow Recall from Routing - Start
                    //proposalDevelopmentFormBean.setCreationStatusCode(1);
                    proposalDevelopmentFormBean.setCreationStatusCode(CoeusConstants.PROPOSAL_IN_PROGRESS_STATUS_CODE);
                    //COEUSQA-1433 - Allow Recall from Routing - End
                    proposalDevelopmentFormBean.setCreationStatusDescription("In Progress");

                    // Added by chandra - start
                    //  gnprh commnted for Proposal Hierarchy
                    ProposalHierarchyTxnBean txn = new ProposalHierarchyTxnBean();
                    data = txn.getParentProposalData(proposalNumber);
                    String rootProposal = (String) data.get("PARENT_PROPOSAL");
                    if (rootProposal != null) {
                        proposalHierarchyBean = txn.getHierarchyData(rootProposal);
                    }
//                      // End

                    //Set Creation Status as 1 - In Progress - end
                }//Added for case #1856 start 2
                else if (functionType == GET_MODIFY_NARRATIVE_DATA) {
                    boolean hasRight = false;
                    String unitNumber = (String) requester.getDataObject();
                    UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
                    ProposalDevelopmentFormBean proposalDevtFormBean =
                            proposalDataTxnBean.getProposalDevelopmentDetails(proposalNumber);
                    int statusCode = proposalDevtFormBean.getCreationStatusCode();
                    //Modified for case# 3316 - start
                    //Check for the rights if the status code is 1,2,3,4,8
                    //COEUSQA-1433 - Allow Recall from Routing - Start
                    //if (statusCode == 1 || statusCode == 3 || statusCode == 2 || statusCode == 4) {
                    if (statusCode == CoeusConstants.PROPOSAL_IN_PROGRESS_STATUS_CODE || statusCode == CoeusConstants.PROPOSAL_REJECTED_STATUS_CODE
                            || statusCode == CoeusConstants.PROPOSAL_APPROVAL_IN_PROGRESS_STATUS_CODE || statusCode == CoeusConstants.PROPOSAL_APPROVED_STATUS_CODE 
                            || statusCode == CoeusConstants.PROPOSAL_RECALLED_STATUS_CODE) {
                    //COEUSQA-1433 - Allow Recall from Routing - End
                        //Modified for case# 3316 - end
                        hasRight = userMaintDataTxnBean.getUserHasRight(loggedinUser, MODIFY_ANY_PROPOSAL, unitNumber);
                        //If not check if user has MODIFY_NARRATIVE right
                        if (!hasRight) {
                            hasRight = userMaintDataTxnBean.getUserHasProposalRight(loggedinUser, proposalNumber, MODIFY_NARRATIVE);
                        }
                    }
                    responder.setDataObject(new Boolean(hasRight));
                    responder.setResponseStatus(true);
                } //Added for case#2420 - Upload of files on Edit Module Details Window - start
                else if (functionType == GET_BLOB_DATA_FOR_NARRATIVE) {
                    ProposalNarrativePDFSourceBean proposalNarrativePDFSourceBean = (ProposalNarrativePDFSourceBean) requester.getDataObject();
                    ProposalNarrativeTxnBean proposalNarrativeTxnBean = new ProposalNarrativeTxnBean();
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                    responder.setDataObject(proposalNarrativePDFSourceBean);
                    responder.setResponseStatus(true);
                    return;
                } //Added for case#2420 - Upload of files on Edit Module Details Window - end
                //Added for Case#3577 - Replace Personnel Attachments while Proposal Development is Approval in Progress - Start
                // Use this function type to get all the rights related to proposal
                else if (functionType == GET_PROP_PERSONNEL_RIGHTS) {
                    Vector vecRequestedData = (Vector) requester.getDataObject();

                    boolean canModifyAnyProp = false;
                    boolean canModifyProp = false;
                    boolean canAlterProp = false;

                    if (vecRequestedData == null) {
                        return;
                    } else {
                        UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();

                        String propNumber = (String) vecRequestedData.get(0);
                        String userId = (String) vecRequestedData.get(1);
                        String unitNumber = (String) vecRequestedData.get(2);

                        canModifyAnyProp = userMaintDataTxnBean.getUserHasRight(userId, (String) vecRequestedData.get(3), unitNumber);
                        //COEUSQA-2417:File refresh during routing not working for Premium Proposal Personnel documents and not working in Lite
                        canModifyProp = userMaintDataTxnBean.getUserHasProposalRight(userId, propNumber, (String) vecRequestedData.get(4));
                        //Modified with case 3587:Multicampus Enhancement
//                            canAlterProp = userMaintDataTxnBean.getUserHasOSPRight(userId, (String)vecRequestedData.get(5));
                        canAlterProp = userMaintDataTxnBean.getUserHasRight(userId, (String) vecRequestedData.get(5), unitNumber);
                        //3587 : End
                    }
                    Map hmProposalRights = new HashMap();
                    hmProposalRights.put("MODIFY_ANY_PROPOSAL", new Boolean(canModifyAnyProp));
                    hmProposalRights.put("MODIFY_PROPOSAL", new Boolean(canModifyProp));
                    hmProposalRights.put("ALTER_PROPOSAL_DATA", new Boolean(canAlterProp));
                    responder.setDataObject(hmProposalRights);
                    responder.setResponseStatus(true);
                    return;
                }
                //COEUSQA-1433 - Allow Recall from Routing - Start
                else if ( functionType == PROPOSAL_RECALL_LOCK_CHECK ) {
                    // Lock the Protocol and Send the boolean value to the Client Side
                    boolean locCheck = false;
                    proposalNumber = (String) requester.getId();
                    String unitNumber =(String) requester.getDataObject();
                    LockingBean lockingBean = new LockingBean();
                    //Adding the null check
                    if(unitNumber==null || (unitNumber!=null && unitNumber.length()>0)){
                        unitNumber = "000001";
                    }
                    //Commented and added for COEUSDEV-1075 : Locking messages inconsistency between Lite and Premium - start
                    //lockingBean =  proposalDataTxnBean.lockDevProposal(proposalNumber, loggedinUser, unitNumber);
                    lockingBean =  proposalDataTxnBean.lockProposalRouting(proposalNumber, loggedinUser, unitNumber);
                    //Commented and added for COEUSDEV-1075 : Locking messages inconsistency between Lite and Premium - end
                    if(lockingBean != null){
                        locCheck = true;
                    }
                    
                    responder.setDataObject(new Boolean(locCheck));
                }
                //COEUSQA-1433 - Allow Recall from Routing - End
                //Added for Case#3577 - Replace Personnel Attachments while Proposal Development is Approval in Progress - End
                //Added for case #1856 end 2
                // 0 element to hold proposal details
                dataObjects.addElement(proposalDevelopmentFormBean);
                // 1 element holds the proposal type look up
                dataObjects.addElement(proposalDataTxnBean.getProposalTypes());
                // 2 element holds the NSF code look up
                dataObjects.addElement(proposalDataTxnBean.getProposalNSFCodes());
                // 3 element holds the Activity type look up
                dataObjects.addElement(proposalDataTxnBean.getProposalActivityTypes());
                // 4 element holds the notice opp for the look up
                dataObjects.addElement(proposalDataTxnBean.getProposalNoticeOpp());
                //5 element to holds the RoleRightInfoBean
                dataObjects.addElement(vecRoleRightInfoBean);
                protocolDataTxnBean = new ProtocolDataTxnBean();
                //6 - set special review codes
                // COEUSQA-2320 Show in Lite for Special Review in Code table
                // dataObjects.addElement(protocolDataTxnBean.getSpecialReviewCode());
                dataObjects.addElement(protocolDataTxnBean.getSpecialReviewTypesForModule(ModuleConstants.PROPOSAL_DEV_MODULE_CODE));
                // Added By Raghunath P.V. for getting the Special Review Codes
                //7 - set approval codes
                dataObjects.addElement(protocolDataTxnBean.getReviewApprovalType());
                //8 - setapproval roles.. For validating the Application date, Approval date and protocol number in Special Review
                dataObjects.addElement(protocolDataTxnBean.getValidSpecialReviewList());
                UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
                boolean hasRight = userMaintDataTxnBean.getUserHasProposalRight(loggedinUser, proposalNumber, SUBMIT_PROPOSAL);
                //9 - set whether SUBMIT_PROPOSAL right exists for this User
                dataObjects.addElement(new Boolean(hasRight));
                hasRight = false;
                if (functionType != ADD_MODE) {
                    ProposalActionTxnBean proposalActionTxnBean = new ProposalActionTxnBean();
                    if (proposalActionTxnBean.checkPersonCanSubmit(loggedinUser, proposalNumber) > 0) {
                        hasRight = true;
                    } else {
                        hasRight = false;
                    }
                }
                //10 - set whether user can submit this Proposal
                dataObjects.addElement(new Boolean(hasRight));

                hasRight = false;
                if (proposalDataTxnBean.getUserHasProposalRole(loggedinUser, proposalNumber, APPROVER_ROLE_ID) > 0) {
                    hasRight = true;
                } else {
                    if (userMaintDataTxnBean.getUserHasOSPRight(loggedinUser, APPROVE_PROPOSAL)) {
                        hasRight = true;
                    }
                }
                //11 - set whether user can Approve this Proposal
                dataObjects.addElement(new Boolean(hasRight));
                //12 - set Proposal Status
                dataObjects.addElement(proposalDataTxnBean.getProposalStatus());
                //*gnprh - commented for Proposal Hierarchy
                //13 - Get the proposal hierarchy related details
                dataObjects.addElement(data);
                // 14 - Get the Proposal Hierarchy Tree data
                dataObjects.addElement(proposalHierarchyBean);


                //For the Coeus enhancement case:1799 start step:2
                CoeusParameterBean coeusParameterBean = null;
                CoeusFunctions coeusFunctions = new CoeusFunctions();
                coeusParameterBean = new CoeusParameterBean();
                coeusParameterBean.setParameterName(CoeusConstants.SPL_REV_TYPE_CODE_HUMAN);
                coeusParameterBean.setParameterValue(coeusFunctions.getParameterValue(coeusParameterBean.getParameterName()));
                if (coeusParameterBean.getParameterValue() != null && !coeusParameterBean.getParameterValue().equalsIgnoreCase("")) {
                    cvParameters.addElement(coeusParameterBean);
                }

                coeusParameterBean = new CoeusParameterBean();
                coeusParameterBean.setParameterName(CoeusConstants.ENABLE_PROTOCOL_TO_DEV_PROPOSAL_LINK);
                coeusParameterBean.setParameterValue(coeusFunctions.getParameterValue(coeusParameterBean.getParameterName()));
                if (coeusParameterBean.getParameterValue() != null && !coeusParameterBean.getParameterValue().equalsIgnoreCase("")) {
                    cvParameters.addElement(coeusParameterBean);
                }


                coeusParameterBean = new CoeusParameterBean();
                coeusParameterBean.setParameterName(CoeusConstants.LINKED_TO_IRB_CODE);
                coeusParameterBean.setParameterValue(coeusFunctions.getParameterValue(coeusParameterBean.getParameterName()));
                if (coeusParameterBean.getParameterValue() != null && !coeusParameterBean.getParameterValue().equalsIgnoreCase("")) {
                    cvParameters.addElement(coeusParameterBean);
                }
                //Added for Case#2402 - use a parameter to set the length of the account number throughout app - Start
                //To get MAX_ACCOUNT_NUMBER_LENGTH parameter details
                coeusParameterBean = new CoeusParameterBean();
                coeusParameterBean.setParameterName(CoeusConstants.MAX_ACCOUNT_NUMBER_LENGTH);
                coeusParameterBean.setParameterValue(ParameterUtils.getMaxAccountNumberLength());
                cvParameters.addElement(coeusParameterBean);
                //Case#2402 - End

                //Added for the COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - start
                //get the  IACUC_SPL_REV_TYPE_CODE parameter details
                coeusParameterBean = new CoeusParameterBean();
                coeusParameterBean.setParameterName(CoeusConstants.IACUC_SPL_REV_TYPE_CODE);
                coeusParameterBean.setParameterValue(coeusFunctions.getParameterValue(coeusParameterBean.getParameterName()));
                if (coeusParameterBean.getParameterValue() != null && !coeusParameterBean.getParameterValue().equalsIgnoreCase("")) {
                    cvParameters.addElement(coeusParameterBean);
                }
                
                //get the  ENABLE_IACUC_TO_DEV_PROPOSAL_LINK parameter details
                coeusParameterBean = new CoeusParameterBean();
                coeusParameterBean.setParameterName(CoeusConstants.ENABLE_IACUC_TO_DEV_PROPOSAL_LINK);
                coeusParameterBean.setParameterValue(coeusFunctions.getParameterValue(coeusParameterBean.getParameterName()));
                if (coeusParameterBean.getParameterValue() != null && !coeusParameterBean.getParameterValue().equalsIgnoreCase("")) {
                    cvParameters.addElement(coeusParameterBean);
                }
                
                //get the  LINKED_TO_IACUC_CODE  parameter details
                coeusParameterBean = new CoeusParameterBean();
                coeusParameterBean.setParameterName(CoeusConstants.LINKED_TO_IACUC_CODE);
                coeusParameterBean.setParameterValue(coeusFunctions.getParameterValue(coeusParameterBean.getParameterName()));
                if (coeusParameterBean.getParameterValue() != null && !coeusParameterBean.getParameterValue().equalsIgnoreCase("")) {
                    cvParameters.addElement(coeusParameterBean);
                }
                //Added for the COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - end
                
                // 15 - Get the required parameter values from the parameter table
                dataObjects.addElement(cvParameters);
                //End Coeus Enhancement case:#1799 step:2

                // Added for case # 2162 - adding Award Type - Start
                AwardLookUpDataTxnBean AwardTxnBean = new AwardLookUpDataTxnBean();
                CoeusVector cvAwrdType = (CoeusVector) AwardTxnBean.getAwardType();
                edu.mit.coeus.utils.ComboBoxBean comboBoxBean = new edu.mit.coeus.utils.ComboBoxBean();
                comboBoxBean.setCode("");
                comboBoxBean.setDescription("");
                cvAwrdType.add(0, comboBoxBean);
                dataObjects.addElement(cvAwrdType);
                // Added for case #2162  - adding Award Type - End
                //Added for case 2406 - Organizations and Locations - start
                //17 - Propoal Location Types
                dataObjects.addElement(proposalDataTxnBean.getLocationTypes());
                //Added for case 2406 - Organizations and Locations - end
            }

            responder.setDataObjects(dataObjects);
            responder.setResponseStatus(true);
            //Commented for the case # COEUSQA- 1675- ability to delete proposal development proposal-start
            //responder.setMessage(null);
            //Commented for the case # COEUSQA- 1675- ability to delete proposal development proposal-end

        } catch (LockingException lockEx) {
            // commented for using UtilFactory.log instead of printStackTrace
            //UtilFactory.log(lockEx.getMessage(),lockEx,"ProposalMaintenanceServlet", "doPost");
//            lockEx.printStackTrace();
//            LockingBean lockingBean = lockEx.getLockingBean();
            String errMsg = lockEx.getErrorMessage();
            CoeusMessageResourcesBean coeusMessageResourcesBean = new CoeusMessageResourcesBean();
            errMsg = coeusMessageResourcesBean.parseMessageKey(errMsg);
            responder.setException(lockEx);
            responder.setResponseStatus(false);
            responder.setMessage(errMsg);
            //Added for Proposal Narrative Locking Issue case id 3316 - start
            responder.setLocked(true);
            //Added for Proposal Narrative Locking Issue case id 3316 - end
            UtilFactory.log(errMsg, lockEx, "ProposalMaintenanceServlet",
                    "perform");
        } catch (CoeusException coeusEx) {
//            int index=0;
            String errMsg;
            if (coeusEx.getErrorId() == 999999) {
                errMsg = "dbEngine_intlErr_exceptionCode.1028";
                responder.setLocked(true);
            } else {
                errMsg = coeusEx.getMessage();
            }
            CoeusMessageResourcesBean coeusMessageResourcesBean = new CoeusMessageResourcesBean();
            errMsg = coeusMessageResourcesBean.parseMessageKey(errMsg);

            responder.setResponseStatus(false);

            //print the error message at client side
            responder.setException(coeusEx);
            responder.setMessage(errMsg);
            UtilFactory.log(errMsg, coeusEx, "ProposalMaintenanceServlet",
                    "perform");

        } catch (DBException dbEx) {

            //Added by Ajay: Start 31-08-2004
            // commented for using UtilFactory.log instead of printStackTrace
            //UtilFactory.log(dbEx.getMessage(),dbEx,"ProposalMaintenanceServlet", "doPost");
//            dbEx.printStackTrace();
            //Added by Ajay: End 31-08-2004

//            int index=0;
            String errMsg = dbEx.getUserMessage();
            if (dbEx.getErrorId() == 20102) {
                errMsg = "dbEngine_intlErr_exceptionCode.1028";
            }
            if (errMsg.equals("db_exceptionCode.1111")) {
                responder.setCloseRequired(true);
            }
            CoeusMessageResourcesBean coeusMessageResourcesBean = new CoeusMessageResourcesBean();
            errMsg = coeusMessageResourcesBean.parseMessageKey(errMsg);

            responder.setResponseStatus(false);

            // Do not send any DBException to Client side - Start
            //print the error message at client side
//            responder.setException(dbEx);
            responder.setException(new CoeusException(errMsg));
            // Do not send any DBException to Client side - End
            responder.setMessage(errMsg);
            UtilFactory.log(errMsg, dbEx,
                    "ProposalMaintenanceServlet", "perform");

        } catch (Exception e) {
            // commented for using UtilFactory.log instead of printStackTrace
            //UtilFactory.log(e.getMessage(),e,"ProposalMaintenanceServlet", "doPost");
//            e.printStackTrace();
            //print the error message at server side
            responder.setResponseStatus(false);
            //print the error message at client side
            responder.setException(e);
            responder.setMessage(e.getMessage());
            UtilFactory.log(e.getMessage(), e,
                    "ProposalMaintenanceServlet", "perform");

            //Case 3193 - START
        } catch (Throwable throwable) {
            Exception ex = new Exception(throwable);
            responder.setException(ex);
            responder.setResponseStatus(false);
            responder.setMessage(ex.getMessage());
            UtilFactory.log(throwable.getMessage(), throwable, "ProposalMaintenanceServlet", "doPost");
            //Case 3193 - END

        } finally {
            try {
                // send the object to applet
                outputToApplet = new ObjectOutputStream(response.getOutputStream());
                outputToApplet.writeObject(responder);
                // close the streams
                if (inputFromApplet != null) {
                    inputFromApplet.close();
                }
                if (outputToApplet != null) {
                    outputToApplet.flush();
                    outputToApplet.close();
                }
            } catch (IOException ioe) {
                UtilFactory.log(ioe.getMessage(), ioe,
                        "ProposalMaintenanceServlet", "perform");
            }
        }

    }

    /** Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {
    }

    /**
     * sets AcType 'I' for the records copied from the module cost elements
     * to the proposal cost elements.
     * @param modOthers vector contain CustomElementsInfoBean
     */
    private Vector setAcTypeAsNew(Vector modOthers) {
        if (modOthers != null && modOthers.size() > 0) {
            int modCount = modOthers.size();
            CustomElementsInfoBean customBean = null;
            CustomElementsInfoBean customElementsInfoBean;
            for (int modIndex = 0; modIndex < modCount; modIndex++) {
                customBean = (CustomElementsInfoBean) modOthers.elementAt(modIndex);
                customBean.setAcType(INSERT_RECORD);
                //proposalCustomElementsInfoBean = new ProposalCustomElementsInfoBean(customBean);
                modOthers.set(modIndex, customBean);
            }
        }
        return modOthers;
    }

    //Added by shiji for Case id:2016 step3 - start
    /*To send notifications to all users who have already approved the proposal when a change in made to a Narrative module*/
    private boolean sendNotification(CoeusVector cvApprovers, String message, String proposalNumber, String fromUser) throws CoeusException, DBException {
        Vector vecInbox = new Vector();
        Vector vecNotePad = new Vector();
//        Vector vecNotification = new Vector();
        MessageBean messageBean = new MessageBean();
        CoeusVector cvApprove = new CoeusVector();
        //Case#4572 - Writing notepad entries and sending notifications irrespective of proposal status
        CoeusVector cvPropApprovedApprovers = new CoeusVector();
        //Case#4572 - End
        for (int index = 0; index < cvApprovers.size(); index++) {
            InboxBean inboxBean = new InboxBean();
            //Commented/Added for case#2999 - Multiple emails generated when a document is uploaded - start
//            ProposalApprovalBean proposalApprovalBean = (ProposalApprovalBean)cvApprovers.get(index);            
//            //Approval status 'A' stands for 'Approved' and Approval status 'O' stands for 'Approved by other' 
//            if(proposalApprovalBean.getApprovalStatus().equals("A") || proposalApprovalBean.getApprovalStatus().equals("O")
//            || proposalApprovalBean.getApprovalStatus().equals("B")) {
            RoutingDetailsBean routingDetailsBean = (RoutingDetailsBean) cvApprovers.get(index);
            if (routingDetailsBean.getApprovalStatus().equals("A")
                    || routingDetailsBean.getApprovalStatus().equals("O")
                    || routingDetailsBean.getApprovalStatus().equals("B")) {
//                String toUser = proposalApprovalBean.getUserId();
                String toUser = routingDetailsBean.getUserId();
                //Commented/Added for case#2999 - Multiple emails generated when a document is uploaded - end
                //String toUser = CoeusGuiConstants.getMDIForm().getUserName();
                if (!cvApprove.contains(toUser)) {
                    //Case#4572 - Writing notepad entries and sending notifications irrespective of proposal status
                    cvPropApprovedApprovers.add(routingDetailsBean);
                    //Case#4572 - End
                    messageBean.setAcType("I");
                    messageBean.setMessage(message);
                    inboxBean.setMessageBean(messageBean);
                    inboxBean.setAcType("I");
                    inboxBean.setOpenedFlag('Y');
                    inboxBean.setFromUser(fromUser);
                    inboxBean.setProposalNumber(proposalNumber);
                    /* Case :1828 Start */
                    inboxBean.setModuleCode(3);
                    /* Case :1828 End */
                    inboxBean.setToUser(toUser);
                    inboxBean.setSubjectType('N');
                    vecInbox.add(inboxBean);
                    cvApprove.add(toUser);
                }
            }
        }
        NotepadBean notepadBean = new NotepadBean();
        notepadBean.setAcType("I");
        notepadBean.setComments(message);
        notepadBean.setProposalAwardNumber(proposalNumber);
        //Case#3577 - Replace Personnel Attachments while Proposal Development is Approval in Progress - Start
        notepadBean.setUpdateUser(fromUser);
        //Case#3577 - Replace Personnel Attachments while Proposal Development is Approval in Progress - End
        vecNotePad.add(notepadBean);

        ProposalActionUpdateTxnBean proposalActionUpdateTxnBean = new ProposalActionUpdateTxnBean();

        boolean isSuccess = proposalActionUpdateTxnBean.sendNotificationForNarrative(vecInbox, vecNotePad);
        //Case#3577 - Replace Personnel Attachments while Proposal Development is Approval in Progress - Start
        // Sending email after updation
        if (isSuccess) {
            //Modified for COEUSDEV-340 : Email that is generated when a narrative is changes does not have proposal and narrative details - Start
            //Getting the subject from coeus message property file
//            String subject = "Notification";
            //Commented with COEUSDEV-75:Rework email engine so the email body is picked up from one place
//            CoeusMessageResourcesBean coeusMessageResourcesBean = new CoeusMessageResourcesBean();
//            MessageFormat formatter = new MessageFormat("");
//            String subject = formatter.format(coeusMessageResourcesBean.parseMessageKey("proposal_nar_mail_sub_exceptionCode.2000"),proposalNumber);
            //COEUSDEV:75 End
            //COEUSDEV-340 : End
            RoutingUpdateTxnBean routingUpdateTxnBean = new RoutingUpdateTxnBean(fromUser);
            //Modified for Case#4572 - Writing notepad entries and sending notifications irrespective of proposal status
            //Mail is send only to the proposal approved approvers
            //routingUpdateTxnBean.sendMailToApprovers(cvApprovers,message,subject);
            String messageBody = "";
            if (cvPropApprovedApprovers != null && cvPropApprovedApprovers.size() > 0) {
                RoutingDetailsBean routingDetailsBean = (RoutingDetailsBean) cvPropApprovedApprovers.get(0);
                //Added for Case#4393 - make sure all Emails sent from Coeus are set thru the new JavaMail email engine  - Start
                RoutingTxnBean routingTxnBean = new RoutingTxnBean();

                //Modified for COEUSDEV-340 : Email that is generated when a narrative is changes does not have proposal and narrative details - Start
                //To append Narrative type and module description in the message
//                 messageBody = message+routingTxnBean.getMailBodyContent(
//                         3,proposalNumber,-1,
//                         routingDetailsBean.getRoutingNumber(),
//                         routingDetailsBean.getMapNumber(),
//                         routingDetailsBean.getStopNumber(),
//                         routingDetailsBean.getLevelNumber(),
//                         routingDetailsBean.getApproverNumber(),
//                         "","");
                String msgBody = routingTxnBean.getMailBodyContent(
                        3, proposalNumber, -1,
                        routingDetailsBean.getRoutingNumber(),
                        routingDetailsBean.getMapNumber(),
                        routingDetailsBean.getStopNumber(),
                        routingDetailsBean.getLevelNumber(),
                        routingDetailsBean.getApproverNumber(),
                        "", "");
                msgBody = msgBody.substring(8, msgBody.length());
                messageBody = message + msgBody;
                //COEUSDEV-340 : End
                //Case#4393 - End
            }
            //COEUSDEV-75:Rework email engine so the email body is picked up from one place
//            routingUpdateTxnBean.sendMailToApprovers(cvPropApprovedApprovers,messageBody,subject);
            routingUpdateTxnBean.sendMailToApprovers(ModuleConstants.PROPOSAL_DEV_MODULE_CODE, MailActions.NARRATIVE_CHANGE,
                    proposalNumber, -1, messageBody, cvPropApprovedApprovers);
            //COEUSDEV:75 End
            //Case#4572 - End
        }
        //Case#3577 - Replace Personnel Attachments while Proposal Development is Approval in Progress - End
        return isSuccess;
    }
    //Case id:2016 step3 - end

    /**
     *  This method is used for applets.
     *  Post the information into server using object serialization.
     */
    private void setBiographyNumbers(Vector biographyData, String propId, String personId) {

        int maxBioNumber = 0;
        ProposalBiographyFormBean bioBean = null;
        int size = biographyData.size();

        maxBioNumber = getMaxPersonBiographyNumber(propId, personId);
        //System.out.println("maxBioNumber for Proposal "+propId +" and Person "+personId + "is : "+maxBioNumber );

        for (int index = 0; index < size; index++) {
            bioBean = (ProposalBiographyFormBean) biographyData.elementAt(index);
            if (bioBean.getAcType() != null) {
                if (bioBean.getAcType().equalsIgnoreCase(INSERT_RECORD)) {
                    maxBioNumber = maxBioNumber + 1;
                    bioBean.setBioNumber(maxBioNumber);
                }
            }
        }
    }

    private int getMaxPersonBiographyNumber(String proposalId, String personId) {
        int maxNo = 0;
        try {
            ProposalDevelopmentTxnBean proposalDevelopmentTxnBean = new ProposalDevelopmentTxnBean();
            maxNo = proposalDevelopmentTxnBean.getPersonBiographyNumber(proposalId, personId);
        } catch (Exception e) {
            // commented for using UtilFactory.log instead of printStackTrace
            UtilFactory.log(e.getMessage(), e, "ProposalMaintenanceServlet", "getMaxPersonBiographyNumber");
//            e.printStackTrace();
        }
        return maxNo;
    }

    private void printBiographyDetails(Vector vecBiographyDataVector) {
        if (vecBiographyDataVector != null) {
            int bioSize = vecBiographyDataVector.size();
            for (int index = 0; index < bioSize; index++) {

                ProposalBiographyFormBean bioBean = (ProposalBiographyFormBean) vecBiographyDataVector.get(index);

                if (bioBean != null) {
                    //System.out.println("~~~~~~~~~~~~~");
                    //System.out.println("personId is "+bioBean.getPersonId());
                    //System.out.println("biNumber is "+bioBean.getBioNumber());
                    //System.out.println("Bio Description is "+bioBean.getDescription());
                    //System.out.println("Actype is "+bioBean.getAcType());
                    //System.out.println("TimeStamp is ");
                    //System.out.println("UPDATEUSER is ");
                    //System.out.println("~~~~~~~~~~~~~");
                }
            }
        }

    }

    /**
     * Returns a short description of the servlet.
     * @return String servlet name.
     */
    public String getServletInfo() {
        return "Proposal Maintenance Servlet";
    }

    private void printDegreeBeans(Vector vecDegreeDataVector) {
        ////System.out.println("Degree Details are :");
        ////System.out.println("********************");
        if (vecDegreeDataVector != null) {
            int size = vecDegreeDataVector.size();
            for (int index = 0; index < size; index++) {
                ProposalPerDegreeFormBean cBean = (ProposalPerDegreeFormBean) vecDegreeDataVector.elementAt(index);
                if (cBean != null) {
                    //System.out.println("ProposalNumber is "+cBean.getProposalNumber());
                    //System.out.println("PersonId is "+cBean.getPersonId());
                    //System.out.println("ActYpe is "+cBean.getAcType());
                    //System.out.println("Degree Code is "+cBean.getDegreeCode());
                    //System.out.println("Degree is "+cBean.getDegree());
                    //System.out.println("Graduationdate Code is "+cBean.getGraduationDate());
                    //System.out.println("UpdateTimeStamp is "+cBean.getUpdateTimestamp());
                    //System.out.println("Update User is "+cBean.getUpdateUser());
                } else {
                    //System.out.println("cBean is NULL");
                }
            }
        }
    }

    private void printBean(Vector vecCustColumnsForModuleVector) {
        if (vecCustColumnsForModuleVector != null) {
            int size = vecCustColumnsForModuleVector.size();
            for (int index = 0; index < size; index++) {
                CustomElementsInfoBean cBean = (CustomElementsInfoBean) vecCustColumnsForModuleVector.elementAt(index);
                if (cBean != null) {
                    //System.out.println("ActYpe is "+cBean.getAcType());
                    //System.out.println("getColumnName number is "+cBean.getColumnName());
                    //System.out.println("getUpdateTimestamp is "+cBean.getUpdateTimestamp());
                    //System.out.println("Update User is "+cBean.getUpdateUser());
                } else {
                    //System.out.println("cBean is NULL");
                }
            }
        }
    }
    /*
     * 1. create a new procedure GET_NARR_USERS_ALL_MOD which should group by module
     * 2. create a new method getProposalNarrativeModules(String proposalNumber) which is
     * like getProposalNarrative(String proposalNumber)
     * 3. create the xls document in vss/GNPRD/3.Database/StoreProcs_Phase II
     *   to maintain the new procedures list
     * 4. update the PB analysis document
     * 5. unit testing
     */

    private void updateNarrativeUsers(Vector userParams, String loggedinUser)
            throws CoeusException, DBException {

        final char NARRATIVE_ACCESS_TYPE = 'R';

        String proposalNumber = null;
        String userId = null;
        int vecSize = userParams.size();
        ProposalRolesFormBean roleFormBean = null;
        for (int indx = 0; indx < vecSize; indx++) {
            roleFormBean = (ProposalRolesFormBean) userParams.elementAt(indx);
            ////System.out.println("SAVING"+roleFormBean);;
            if (roleFormBean != null) {
                ////System.out.println("saving ..id"+roleFormBean.getUserId());
                proposalNumber = roleFormBean.getProposalNumber();
                userId = roleFormBean.getUserId();
            }
            break;
        }
        // get the narrative users
//        ProposalDevelopmentTxnBean proposalDevelopmentTxnBean = new ProposalDevelopmentTxnBean();
        ProposalNarrativeTxnBean proposalNarrativeTxnBean = new ProposalNarrativeTxnBean(loggedinUser);
        Vector narrativeModules = proposalNarrativeTxnBean.getProposalNarrativeModules(proposalNumber);
        // loop thru the narrative users for module
        ProposalNarrativeModuleUsersFormBean proposalNarrativeModuleUsersFormBean = null;
        if (narrativeModules == null) {
            ////System.out.println("module vector is null ");
            vecSize = 0;
        } else {
            vecSize = narrativeModules.size();
            ////System.out.println("narrative modules vector size "+vecSize);
        }

        for (int indx = 0; indx < vecSize; indx++) {
            proposalNarrativeModuleUsersFormBean = new ProposalNarrativeModuleUsersFormBean();
            Integer moduleIntNumber = (Integer) narrativeModules.elementAt(indx);
            ////System.out.println("module number "+moduleIntNumber);
            int moduleNumber = moduleIntNumber.intValue();
            proposalNarrativeModuleUsersFormBean.setProposalNumber(proposalNumber);
            proposalNarrativeModuleUsersFormBean.setModuleNumber(moduleNumber);
            proposalNarrativeModuleUsersFormBean.setAcType(INSERT_RECORD);
            proposalNarrativeModuleUsersFormBean.setAccessType(NARRATIVE_ACCESS_TYPE);
            proposalNarrativeModuleUsersFormBean.setUpdateTimestamp((new CoeusFunctions()).getDBTimestamp());
            proposalNarrativeModuleUsersFormBean.setUpdateUser(loggedinUser);
            proposalNarrativeModuleUsersFormBean.setUserId(userId);
            //ProposalDevelopmentUpdateTxnBean updateTxnBean = new ProposalDevelopmentUpdateTxnBean(loggedinUser);
            proposalNarrativeTxnBean.addDeleteProposalNarrative(proposalNarrativeModuleUsersFormBean);
        }

    }

    /**
     * To check the narrative rights
     * @param loggedinUser 
     * @param unitNumber 
     * @param proposalNumber 
     * @param statusCode 
     * @throws edu.mit.coeus.exception.CoeusException 
     * @throws edu.mit.coeus.utils.dbengine.DBException 
     * @throws org.okip.service.shared.api.Exception 
     * @return HashMap which has modify and view narrative rights flag
     */
    private HashMap getNarrativeRights(String loggedinUser, String unitNumber, String proposalNumber,
            int statusCode) throws CoeusException, DBException, org.okip.service.shared.api.Exception {

        UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
        HashMap hmNarrativeRights = new HashMap();
        boolean canModifyNarrative = false;
        boolean canViewNarrative = false;
        boolean modifyAnyProposal = userMaintDataTxnBean.getUserHasRight(loggedinUser, MODIFY_ANY_PROPOSAL, unitNumber);
        boolean modifyNarrative = userMaintDataTxnBean.getUserHasProposalRight(loggedinUser, proposalNumber, MODIFY_NARRATIVE);

        //MODIFY RIGHTS CHECKING for NARRATIVE - Start

        //If the user is having Modify Any proposal rights
        //then check for the proposal status
        if (modifyAnyProposal) {
            //If the status code is 1 (In Progress) or 3(Rejected) or 2 (Approval In Progress) or 4(Approved) or 8(Recalled)
            //set modify narrative right to true.
            //COEUSQA-1433 - Allow Recall from Routing - Start
            /*if (statusCode == 1 || statusCode == 3
                    || statusCode == 2 || statusCode == 4) {*/
            if (statusCode == CoeusConstants.PROPOSAL_IN_PROGRESS_STATUS_CODE || statusCode == CoeusConstants.PROPOSAL_REJECTED_STATUS_CODE
                    || statusCode == CoeusConstants.PROPOSAL_APPROVAL_IN_PROGRESS_STATUS_CODE || statusCode == CoeusConstants.PROPOSAL_APPROVED_STATUS_CODE
                    || statusCode == CoeusConstants.PROPOSAL_RECALLED_STATUS_CODE) {
            //COEUSQA-1433 - Allow Recall from Routing - End
                canModifyNarrative = true;
            }
        }
        // COEUSDEV-319: Premium - Change menu label in Protocol window - Protocol Actions --> Approve - Start
        //If the status code is 2 (Approval In Progress) or 4(Approved)
        //and has alter proposal right set modify narrative right to true.
        //COEUSQA-1433 - Allow Recall from Routing - Start
        //if (!canModifyNarrative && (statusCode == 2 || statusCode == 4)) {
        if (!canModifyNarrative && (statusCode == CoeusConstants.PROPOSAL_APPROVAL_IN_PROGRESS_STATUS_CODE
                || statusCode == CoeusConstants.PROPOSAL_APPROVED_STATUS_CODE)) {
        //COEUSQA-1433 - Allow Recall from Routing - End
            //Modified with case 3587:MultiCampus Enhancement
//            boolean hasAlterRight = userMaintDataTxnBean.getUserHasOSPRight(loggedinUser, ALTER_PROPOSAL_DATA);
            boolean hasAlterRight = userMaintDataTxnBean.getUserHasRight(loggedinUser, ALTER_PROPOSAL_DATA, unitNumber);
            //3587 End
            if (hasAlterRight) {
                canModifyNarrative = true;
            }
        }
        hmNarrativeRights.put("hasOSPDeptRightToModify", new Boolean(canModifyNarrative));
        hmNarrativeRights.put("hasProposalRightToModify", new Boolean(modifyNarrative));
        // COEUSDEV-319: Premium - Change menu label in Protocol window - Protocol Actions --> Approve- End
        //If the user is having Modify Narrative right
        //then check for the proposal status
        if (!canModifyNarrative && modifyNarrative) {
            //If the status code is 1 (In Progress) or 3(Rejected) or 2 (Approval In Progress) or 4(Approved) or 8(Recalled)
            //set modify narrative right to true.
            //COEUSQA-1433 - Allow Recall from Routing - Start
            /*if (statusCode == 1 || statusCode == 3
                    || statusCode == 2 || statusCode == 4) {*/
            if (statusCode == CoeusConstants.PROPOSAL_IN_PROGRESS_STATUS_CODE || statusCode == CoeusConstants.PROPOSAL_REJECTED_STATUS_CODE
                    || statusCode == CoeusConstants.PROPOSAL_APPROVAL_IN_PROGRESS_STATUS_CODE || statusCode == CoeusConstants.PROPOSAL_APPROVED_STATUS_CODE
                    || statusCode == CoeusConstants.PROPOSAL_RECALLED_STATUS_CODE) {
            //COEUSQA-1433 - Allow Recall from Routing - End
                canModifyNarrative = true;
            }
        }

        //MODIFY RIGHTS CHECKING for NARRATIVE - End
        //VIEW RIGHTS CHECKING for NARRATIVE - Start

        //If the user is not having modify narrative then check for view narrative rights
        if (canModifyNarrative) {
            canViewNarrative = true;
        }

        if (!canViewNarrative) {
            boolean viewAnyProposal = userMaintDataTxnBean.getUserHasRight(loggedinUser, VIEW_ANY_PROPOSAL, unitNumber);
            boolean viewNarrative = userMaintDataTxnBean.getUserHasProposalRight(loggedinUser, proposalNumber, VIEW_NARRATIVE);

            //If the user is having Modify Any proposal rights
            //then check for the proposal status            
            if (modifyAnyProposal) {
                //If the status code is not equal to 1 (In Progress) and 3(Rejected) and 2 (Approval In Progress) and 4(Approved) and 8(Recalled)
                //set view narrative right to true.
                //COEUSQA-1433 - Allow Recall from Routing - Start
                /*if (statusCode == 1 || statusCode == 3
                    || statusCode == 2 || statusCode == 4) {*/
                if (statusCode == CoeusConstants.PROPOSAL_IN_PROGRESS_STATUS_CODE || statusCode == CoeusConstants.PROPOSAL_REJECTED_STATUS_CODE
                    || statusCode == CoeusConstants.PROPOSAL_APPROVAL_IN_PROGRESS_STATUS_CODE || statusCode == CoeusConstants.PROPOSAL_APPROVED_STATUS_CODE
                    || statusCode == CoeusConstants.PROPOSAL_RECALLED_STATUS_CODE) {
                //COEUSQA-1433 - Allow Recall from Routing - End
                    canViewNarrative = true;
                }
            }

            //If the user is having Modify Narrative right
            //then check for the proposal status
            if (!canViewNarrative && modifyNarrative) {
                //If the status code is not equal to 1 (In Progress) and 3(Rejected) and 2 (Approval In Progress) and 4(Approved) and 8(Recalled)
                //set view narrative right to true.
                //COEUSQA-1433 - Allow Recall from Routing - Start
                /*if (statusCode == 1 || statusCode == 3
                    || statusCode == 2 || statusCode == 4) {*/
                if (statusCode == CoeusConstants.PROPOSAL_IN_PROGRESS_STATUS_CODE || statusCode == CoeusConstants.PROPOSAL_REJECTED_STATUS_CODE
                    || statusCode == CoeusConstants.PROPOSAL_APPROVAL_IN_PROGRESS_STATUS_CODE || statusCode == CoeusConstants.PROPOSAL_APPROVED_STATUS_CODE
                    || statusCode == CoeusConstants.PROPOSAL_RECALLED_STATUS_CODE) {
                //COEUSQA-1433 - Allow Recall from Routing - End
                    canViewNarrative = true;
                }
            }

            //If the user is having View Any proposal rights
            //then set view narrative right to true.
            if (!canViewNarrative && viewAnyProposal) {
                canViewNarrative = true;
            }

            //If the user is having View Narrative right
            //then set view narrative right to true.
            if (!canViewNarrative && viewNarrative) {
                canViewNarrative = true;
            }

            if (!canViewNarrative) {
                //If the user is having any OSP right and 
                //the status is not equal to 1 (In Progress) and 3(Rejected) and 8(Recalled)
                //then set view narrative right to true.                
                boolean userHasAnyOspRights = userMaintDataTxnBean.getUserHasAnyOSPRight(loggedinUser);
                //COEUSQA-1433 - Allow Recall from Routing - Start
                //if (userHasAnyOspRights && statusCode != 1 && statusCode != 3) {
                if (userHasAnyOspRights && statusCode != CoeusConstants.PROPOSAL_IN_PROGRESS_STATUS_CODE && statusCode != CoeusConstants.PROPOSAL_REJECTED_STATUS_CODE
                        && statusCode != CoeusConstants.PROPOSAL_RECALLED_STATUS_CODE) {
                //COEUSQA-1433 - Allow Recall from Routing - End
                    canViewNarrative = true;
                }
            }
        }
        //MODIFY RIGHTS CHECKING for NARRATIVE - Start
        //setting modify and view narrative flag.
        hmNarrativeRights.put("canModifyNarrative", new Boolean(canModifyNarrative));
        hmNarrativeRights.put("canViewNarrative", new Boolean(canViewNarrative));
        return hmNarrativeRights;
    }
 }

