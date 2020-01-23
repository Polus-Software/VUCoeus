/*
 * ProtocolBaseAction.java
 *
 * Created on March 24, 2005, 10:23 AM
 */

/* PMD check performed, and commented unused imports and variables on 14-MARCH-2011
 * by Md.Ehtesham Ansari
 */
package edu.mit.coeuslite.iacuc.action;

import edu.mit.coeus.bean.CoeusMessageResourcesBean;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.iacuc.bean.CommitteeTxnBean;
import edu.mit.coeus.iacuc.bean.ProtocolAmendRenewalBean;
import edu.mit.coeus.iacuc.bean.ProtocolAuthorizationBean;
import edu.mit.coeus.iacuc.bean.ProtocolDataTxnBean;
import edu.mit.coeus.iacuc.bean.ProtocolInfoBean;
import edu.mit.coeus.iacuc.bean.ProtocolSubmissionInfoBean;
import edu.mit.coeus.iacuc.bean.ProtocolSubmissionTxnBean;
import edu.mit.coeus.iacuc.bean.ScheduleDetailsBean;
import edu.mit.coeus.iacuc.bean.ScheduleTxnBean;
import edu.mit.coeus.mapsrules.bean.BusinessRuleBean;
import edu.mit.coeus.mapsrules.bean.BusinessRuleConditionsBean;
import edu.mit.coeus.questionnaire.bean.ModuleDataBean;
import edu.mit.coeus.questionnaire.bean.QuestionnaireAnswerHeaderBean;
import edu.mit.coeus.questionnaire.bean.SubModuleDataBean;
import edu.mit.coeus.routing.bean.RoutingTxnBean;
import edu.mit.coeus.search.exception.CoeusSearchException;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.ModuleConstants;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.TransactionMonitor;
import edu.mit.coeuslite.iacuc.bean.ProtocolHeaderDetailsBean;
import edu.mit.coeuslite.iacuc.bean.ReadProtocolDetails;
import edu.mit.coeuslite.utils.CoeusBaseAction;
import edu.mit.coeuslite.utils.CoeusLiteConstants;
import edu.mit.coeuslite.utils.CoeusliteMenuItems;
import edu.mit.coeuslite.utils.LockBean;

import edu.mit.coeuslite.utils.SearchModuleBean;
import edu.mit.coeuslite.utils.WebUtilities;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import edu.mit.coeuslite.utils.bean.MenuBean;
import edu.wmc.coeuslite.budget.bean.ReadXMLData;



import java.util.Vector;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.DynaBean;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.validator.DynaValidatorForm;
import edu.mit.coeuslite.utils.IacucUtils;


/**
 *
 * @author  Chandrashekhar
 */
/** Specifies the Base Class for the Protocol Module.
 *Right checking, Session Handling, Exception handling process will be
 *executed here
 */
public abstract class ProtocolBaseAction extends CoeusBaseAction{
    public static final String EMPTY_STRING = "";   
    private final String MODIFY_IACUC_PROTOCOL = "MODIFY_IACUC_PROTOCOL";
    private final String MODIFY_ANY_IACUC_PROTOCOL = "MODIFY_ANY_IACUC_PROTOCOL";
    //Added for Case#3079- Change expire to no longer be a terminal status- Start    
    private final String DELETE_ANY_IACUC_PROTOCOL = "DELETE_ANY_IACUC_PROTOCOL";
    private final String DELETE_IACUC_PROTOCOL = "DELETE_IACUC_PROTOCOL";
    //Added for Case#3079- Change expire to no longer be a terminal status- End    
    // 4361: Rights checking issue in protocol in Lite - Start
    private final String VIEW_IACUC_PROTOCOL = "VIEW_IACUC_PROTOCOL";
    private final String VIEW_ANY_IACUC_PROTOCOL = "VIEW_ANY_IACUC_PROTOCOL";
    private final String CREATE_IACUC_PROTOCOL = "CREATE_IACUC_PROTOCOL";
    private final String GENERAL_PROTOCOL = "generalProtocol";    
    private final int PENDING_IN_PROGRESS = 100;
    // 4361: Rights checking issue in protocol in Lite - End
    
//    private String loggedinUser;
//    private String unitNumber;
//    public HttpServletRequest request;
//    private HttpServletResponse response;
//    private HttpSession session;
//    private static final String rowLockStr = "osp$IACUC Protocol_";
//    private WebTxnBean webTxnBean;
    
    private static final String MENU_ITEMS = "iacucmenuItemsVector";
    private static final String PROTOCOL_NUMBER = "protocolNumber";
    public static final String SUB_HEADER = "iacucsubHeaderVector";
    private static final String SUB_HEADER_PATH = "/edu/mit/coeuslite/iacuc/xml/IacucSubMenu.xml";
    // 3282: Reviewer View of Protocol materials
    private static final String REVIEWER_SUB_HEADER_PATH = "/edu/mit/coeuslite/iacuc/xml/IacucReviewerSubMenu.xml";
    private static final String PROTOCOL_MENU_PATH = "/edu/mit/coeuslite/iacuc/xml/IacucMenu.xml";
    private IacucUtils iacucUtils;
    //Code added for Case#2785 - Protocol Routing
    public static final String VIEW_ROUTING = "AV_VIEWROUTING";
    //Added for Case#4312 - Non-IRB admin has ability to edit Funding Source outside of an amendment  - Start
//    private static final String PERFORM_IACUC_ACTIONS_ON_PROTO = "PERFORM_IACUC_ACTIONS_ON_PROTO";
    //Case#4312 - End
//   Added with case 4398 - Funding source added directly is lost when an amendment is approved.
//    private static final String FUNDING_SRC_MODULE_CODE  = "AC005";
//    case 4398 - end
    //case 4590: Changes in special review being wiped out after an amendment is approved - Protocol 
//    private static final String SPL_RVW_MODULE_CODE      = "AC007";
    //4590 End

    private static final String PROTOCOL_REQUEST_ACTION_MENU_PATH = "/edu/mit/coeuslite/iacuc/xml/IacucRequestActionMenu.xml";
    protected static final String PROTOCOL_REQUEST_ACTION_MENU_ITEMS = "iacucActionMenuItems";
    private static final int RETURN_TO_PI_PROTOCOL_STATUS = 102;
    private static final int TABLED_PROTOCOL_STATUS = 103;
    private static final int MINOR_REVISION_REQUIRED_PROTOCOL_STATUS = 104;
    private static final int MAJOR_REVISION_REQUIRED_PROTOCOL_STATUS = 107;
    //Added for new sequence generate issue -Start
    private static final int WITHDRAWN = 105;
    private static final int ADMINISTRATIVELY_WITHDRAWN = 302;
    //Added for new sequence generate issue -End
    private static final int DISAPPROVED = 303;
    //COEUSQA:3315 - IACUC Protocol Actions Blanking Completed Questionnaires - Start
    private static final int IACUC_REVIEW_NOT_REQUIRED_STATUS = 300;
    //COEUSQA:3315 - End
    
    private static final String VALIDATION_DESC = "description";
    private static final String VALIDATION_RULE_CATEGORY = "ruleCategory";
    private static final String UNIT_NAME = "unitName";
    private static final String USER = "user";
    private static final String PROTOCOL_HEADER_BEAN="iacucHeaderBean";
    
    //Added for COEUSQA-2314 : IRB Admin should have ability to assign committee based on lead unit of the protocol - Start    
    private static final int RENEWAL_IN_PROGRESS = 106;
    private static final String MAINTAIN_PROTOCOL_SUBMISSIONS = "MAINTAIN_IACUC_PROTO_SUBMISION";
    //COEUSQA-2314 : End
    // Added for COEUSQA-2666: Complete Administrative Review functionality in IACUC
    private static final int ADMINISTRATIVELY_INCOMPLETE_STATUS = 203;
    // COEUSQA-2666: End
    // Added for COEUSQA-2692_Allow an investigator to abandon an IRB protocol that has never been approved _start
    private static final int ABANDONED_PROTOCOL = 309;
    // Added for COEUSQA-2692_Allow an investigator to abandon an IRB protocol that has never been approved _end
    // Added for COEUSQA-2556 : When a protocol is in routing it should not have a status of Submitted to IRB -Start
    private static final int ROUTING_IN_PROGRESS_STATUS = 108;
    // Added for COEUSQA-2556 : When a protocol is in routing it should not have a status of Submitted to IRB - End
    
    /** Creates a new instance of ProtocolBaseAction */
    public ProtocolBaseAction() {
    }
    
    /** It is a normal overridden method of Action class which specifies the path from
     *the abstratc metho perform()
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
    HttpServletResponse res){
//        super.execute(mapping, form, request, res);
        ActionForward forward = null;
        boolean isEdit = false;
        String newProtocol = EMPTY_STRING;
        String loggedinUser = EMPTY_STRING;
        String unitNumber = EMPTY_STRING;
        
        try{
            ActionForward fwd = authUser(mapping, request, form);
            if(fwd!=null){//user is not logged in
                return fwd;
            }
            //Code added for Case#2785 - Protocol Routing - starts
            HttpSession session = request.getSession();
            session.setAttribute("moduleCode"+session.getId(), ModuleConstants.IACUC_MODULE_CODE+"");
            //Code added for Case#2785 - Protocol Routing - ends
            saveMessages(request,null);
            readProtocolMenus(request);
            prepareLockRelease(request);
            UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute(USER+session.getId());
            /** Check for the session. Check for the valid session. If exception,
             *then send to the Logon screen
             */
            if(userInfoBean == null) {
                ActionForward actionForward = new ActionForward("/coeuslite/mit/iacuc/cwLogon.jsp");
                //start--2
                 request.setAttribute(CoeusLiteConstants.SESSION_EXPIRED, CoeusLiteConstants.INVALID_SESSION);
                //end--2
                ActionMessages actionMessages = new ActionMessages();
                String errMsg = "session_ended_exceptionCode";
                actionMessages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage(errMsg));
                saveMessages(request,actionMessages);
                return actionForward;
            }// End of session handling
            //start--3
            // 3282: Reviewer View of Protocol materials
            boolean isUSerProtocolReviewer = checkReviewerRight(request, userInfoBean);
            String  selected = request.getParameter(CoeusLiteConstants.SEARCH_ACTION);
            //end--3
            String protocolNumber = EMPTY_STRING;
            String seq = EMPTY_STRING;
            int sequenceNumber = 0;
            // 3282: Reviewer View of Protocol materials - Start
            String strSubmissionNumber = EMPTY_STRING;
            int submissionNumber = 0;
            // 3282: Reviewer View of Protocol materials - End
            String isEditable = request.getParameter("isEditable");
            //start--4
            if(isEditable != null && isEditable.equals("true")) {
                protocolNumber = request.getParameter(PROTOCOL_NUMBER);
                session.setAttribute(CoeusLiteConstants.MODE_DETAILS+session.getId(),CoeusLiteConstants.DISPLAY_MODE); 
            }
            //end--4
            if(selected== null && protocolNumber.equals(EMPTY_STRING)){
                //start--5

                protocolNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId());
                seq = (String)session.getAttribute(CoeusLiteConstants.IACUC_SEQUENCE_NUMBER+session.getId());
                //end--5
                
                if(seq!= null){
                    sequenceNumber = Integer.parseInt(seq);
                }
              
            }else if(selected!=null && selected.equals("SEARCH_WINDOW")) {
                protocolNumber = request.getParameter(PROTOCOL_NUMBER);
                seq = request.getParameter("sequenceNumber");
                String mode = EMPTY_STRING;

                //loadSaveFlagStatus(protocolNumber);

                SearchModuleBean moduleBean = new SearchModuleBean();
                
                String oldMode = (String)session.getAttribute(CoeusLiteConstants.MODE_DETAILS+session.getId());
                oldMode = getMode(oldMode);
                moduleBean.setOldMode(oldMode);
                moduleBean.setModuleKey(CoeusLiteConstants.PROTOCOL_MODULE);
                moduleBean.setModuleNumber(protocolNumber);
                mode = getMode(mode);
                moduleBean.setMode(mode);
                String oldProposalNumber = (String)session.getAttribute("PROTOCOL_NUMBER"+session.getId());
                if(oldProposalNumber!= null){
                    moduleBean.setOldModuleNumber(oldProposalNumber);
                }else{
                    moduleBean.setOldModuleNumber(EMPTY_STRING);
                }

//                moduleBean.setUnitNumber(unitNumber);
                session.setAttribute(CoeusLiteConstants.PROTOCOL_SEARCH_ACTION+session.getId(),moduleBean);
                
                
                if(seq!= null){
                    sequenceNumber = Integer.parseInt(seq);
                }
                //start--6
                
//                String oldProtocolNum = (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId());
                //end--6
            } else{
                protocolNumber = request.getParameter(PROTOCOL_NUMBER);
                seq = request.getParameter("sequenceNumber");
                //loadSaveFlagStatus(protocolNumber);
                if(seq!= null){
                    sequenceNumber = Integer.parseInt(seq);
                }
                //start--6
                
//                String oldProtocolNum = (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId());
                //end--6
                // 3282: Reviewer View of Protocol materials - Start
                strSubmissionNumber = request.getParameter("submissionNumber");
                if(strSubmissionNumber != null && !EMPTY_STRING.equals(strSubmissionNumber)){
                    submissionNumber = Integer.parseInt(strSubmissionNumber);
                }
                // 3282: Reviewer View of Protocol materials - End
            }

            if(userInfoBean!= null && userInfoBean.getUserId()!= null){
                loggedinUser = userInfoBean.getUserId();
                unitNumber = userInfoBean.getUnitNumber();
                if(protocolNumber!= null && selected!= null && sequenceNumber!= -1){
                    
//                    isEdit =  checkIsProtocolEditable(protocolNumber.trim(),userInfoBean);
//                    if(isEdit){
//                        //start--7
//                        
//                        session.setAttribute(CoeusLiteConstants.IACUC_MODE_DETAILS+session.getId(),CoeusLiteConstants.DISPLAY_MODE);
//                        //end--7
//                         //forward = mapping.findForward("canEdit");
//                    }else{
//                        //start--8
//                        
//                        session.setAttribute(CoeusLiteConstants.IACUC_MODE_DETAILS+session.getId(),CoeusLiteConstants.MODIFY_MODE);
//                        //end--8
//                        //forward = performExecute(mapping,form,req,res);
//                    }
                    
                    //start--9
//                    WebTxnBean webTxnBean = new WebTxnBean();
//                    HashMap hmProtocolNumber = new HashMap();
//                    hmProtocolNumber.put(PROTOCOL_NUMBER, protocolNumber);
//                    Hashtable htSeqNo = (Hashtable) webTxnBean.getResults(request, "generateIacucNewSequence", hmProtocolNumber);
//                    String seqNo = ((String)((HashMap)htSeqNo.get("generateIacucNewSequence")).get("SEQ_CODE"));
//                    if(seqNo!=null &&  seqNo.equals("1")){
//                        seq = new Integer((new Integer(seq).intValue()+1)).toString();
//                        session.setAttribute("newSeq"+session.getId(), "incremented");
//                    }
                    session.setAttribute(CoeusLiteConstants.IACUC_SEQUENCE_NUMBER+session.getId(),seq);
                    //end--9
                    if(submissionNumber > 0){
                        session.setAttribute("SUBMISSION_NUMBER"+session.getId(), new Integer(submissionNumber));
                    }
                    
                    // Validations while saving the protocol
                    String saveProtocol = request.getParameter("saveProtocol");
                    if(saveProtocol!= null){
                        doProtocolSaveValidation();// Do the vlaidations and fire the messages for the respective form
                    }
                }
                
                
                // Set the mode of the window as ADD mode.
                newProtocol = request.getParameter("NEW_PROTOCOL");
                if(newProtocol!= null){
                    protocolNumber = null;//"";
                    seq = null;
                    
                    //start--10
 
                    session.setAttribute(CoeusLiteConstants.IACUC_SEQUENCE_NUMBER+session.getId(),seq);
                    session.setAttribute(CoeusLiteConstants.MODE_DETAILS+session.getId(),CoeusLiteConstants.ADD_MODE);
                    //Added for the case#3282/3284 reviewer views and comments-start
                    session.removeAttribute("isFromReviewer");
                    //Added for the case#3282/3284 reviewer views and comments-end
                    //loadSaveFlagStatus(protocolNumber);
                }
                if(protocolNumber!= null){
                    LockBean lockBean = getLockingBeanIacucProtoRouting(userInfoBean,
                            (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId()), request);
                    releaseLock(lockBean, request);
                }

                session.setAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId(),protocolNumber);
                //end--10
            }
            /**If the error is true, then display the error
             *  messages and goto respective page and display the action error
             */
            if(isEdit){
                forward = mapping.findForward("canEdit");
            }else{
                readNavigationPath(request);
                secondLevelHeaderPath(request);
                //Added for coeus4.3 enhancement
                //For creating new Amendments and Renewal
                session.removeAttribute("iacucNewAmendRenew");
                //Code added for coeus4.3 concurrent Amendment/Renewal enhancement.
                //To set the menus for creating Amendment and Renewal protocol.
                if(request.getParameter("Menu_Id")!= null && 
                        request.getParameter("Menu_Id").equals("000")){
                    //Code modified for Case#2785 - Protocol Routing
//                    setMenuForAmendRenew(protocolNumber, request);
                    setMenuForAmendRenew(protocolNumber, ""+sequenceNumber, request);
                }
                forward = performExecute(mapping,form,request,res);
                protocolNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId());
                if(protocolNumber != null && !protocolNumber.equals("")){
                 Vector vecProtocolHeader = getProtocolHeader(protocolNumber,request);
                if(vecProtocolHeader!=null && vecProtocolHeader.size()>0){
                    ProtocolHeaderDetailsBean bean = (ProtocolHeaderDetailsBean) vecProtocolHeader.get(0);
                    //Code added for coeus4.3 enhancements - starts.
                    //If the protocol sequence number is incremented, 
                    //then the ProtocolHeaderDetailsBean sequence number should not be put it in session as sequenceNumber.
                    String newSeq = (String) session.getAttribute("iacucNewSeq"+session.getId());
                    if(newSeq==null){
                        session.setAttribute(CoeusLiteConstants.IACUC_SEQUENCE_NUMBER+session.getId(), bean.getSequenceNumber());
                    }
                    session.setAttribute(CoeusLiteConstants.IACUC_VERSION_NUMBER+session.getId(), bean.getVersionNumber());
                    //Code added for coeus4.3 enhancements - ends.
                    // Added for COEUSQA-2692_Allow an investigator to abandon an IRB protocol that has never been approved _start
                    if(bean.getProtocolStatusCode() == ABANDONED_PROTOCOL){
                        session.setAttribute(CoeusLiteConstants.MODE_DETAILS+session.getId(),CoeusLiteConstants.DISPLAY_MODE);
                        session.setAttribute("MODIFY_IACUC_PROTOCOL_FUNDING_SOURCE"+session.getId(), new Boolean(false));
                        session.setAttribute("MODIFY_IACUC_SPECIAL_RVW"+session.getId(), new Boolean(false));
                    }
                    // Added for COEUSQA-2692_Allow an investigator to abandon an IRB protocol that has never been approved _end
                    session.setAttribute("iacucHeaderBean", bean);
                }
            }
            }
        }catch(CoeusSearchException coeusSearchException){
            coeusSearchException.printStackTrace();
            forward =mapping.findForward("failure");
            request.setAttribute("Exception", coeusSearchException);
        }
        catch (Exception exception){
            exception.printStackTrace();
            request.setAttribute("Exception", exception);
            forward =mapping.findForward("failure");
//            LockBean lockBean = (LockBean)session.getAttribute(CoeusLiteConstants.LOCK_BEAN+session.getId());
//            if(lockBean!= null){
//                try{
//                    releaseLock(lockBean, request);
//                }catch (Exception lockException){
//                    lockException.printStackTrace();
//                    request.setAttribute("Exception", lockException);
//                    forward =mapping.findForward("failure");
//                }
//            }
        }
        return forward;
    }
    /** This abstract class will be overidden in each of the subclasses. This will be
     *functioning as a base execute() method for other action classes
     */
    public abstract ActionForward performExecute(ActionMapping mapping, ActionForm form, HttpServletRequest req,
    HttpServletResponse res) throws Exception;
    
    /** This method is used to Cleanup the instance variables or which not required
     * fields in the available Action class.
     */
    public abstract void cleanUp();
    
    
    /** Do the validations when "Save Protocol" action is performed
     *Check if the all necessary screens are saved. If not then throw
     *the messages to the user.
     */
    private void doProtocolSaveValidation() throws Exception{
        
    }
    
    /** check for the editable protocol, If the protocol is editable
     *it will return true else false. Depending upon the code, the
     *protocol editable status can be evaluated
     */
    
    protected boolean checkIsProtocolEditable(HttpServletRequest request,String protocolNumber) throws Exception{
        String errMsg = EMPTY_STRING;
        // 3282: Reviewer View of Protocol materials - Start
        // Changed the variable name from isEdit to isEditable
        boolean isEdit = false;
        boolean isEditable = false;
        // 3282: Reviewer View of Protocol materials - End
        HttpSession session = request.getSession();
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute(USER+session.getId());
        ActionMessages actionMessages = new ActionMessages();        
        String unitNumber = userInfoBean.getUnitNumber();
        String loggedinUser = userInfoBean.getUserId().toUpperCase();
        ProtocolDataTxnBean protocolDataTxnBean= new ProtocolDataTxnBean();
        CoeusMessageResourcesBean coeusMessageResourcesBean=new CoeusMessageResourcesBean();
        boolean isAmendRenewalApproved = false;
        
        String amendmentRenewalNumber;
        int amendmentRenewalSeqNumber;
        UserMaintDataTxnBean txnData = new UserMaintDataTxnBean(); 
        //Added for Case#4312 - Non-IRB admin has ability to edit Funding Source outside of an amendment  - Start
        boolean canModifyFundingSource = false;
        boolean canModifySpecialReview = false;
        boolean isAmendRenewal = false;
        //Case#4312 - End
        // COEUSDEV-161: Changes to IRB reviewer comments functionality - Access peermissions - Start
//        // 3282: Reviewer View of Protocol materials - Start
//        //boolean isUserProtocolReviewer = checkReviewerRight(request, userInfoBean);
//        boolean isFromReviewer=false; 
//       
//        
//        if(request.getParameter("notFromIsReviewer")!=null && request.getParameter("notFromIsReviewer").equalsIgnoreCase("true")){
//            session.removeAttribute("isFromReviewer");
//        }
//        if(request.getParameter("fromIsReviewer")!=null && request.getParameter("fromIsReviewer").equalsIgnoreCase("true")){
//            session.setAttribute("isFromReviewer",CoeusLiteConstants.YES);
//        }
//        if(session.getAttribute("isFromReviewer")!=null && session.getAttribute("isFromReviewer").equals(CoeusLiteConstants.YES)){
//            session.setAttribute("MODIFY_PROTOCOL_FUNDING_SOURCE"+session.getId(), new Boolean(false));
//            return false;
//        }
//
//       // 3282: Reviewer View of Protocol materials - End
      // COEUSDEV-161: Changes to IRB reviewer comments functionality - Access peermissions - End
       if(protocolNumber.length() > 10 && isIacucProtoAmendmentRenewal(protocolNumber.charAt(10))){
            isAmendRenewal = true;
            Vector vctAmendRenewals = protocolDataTxnBean.getAllAmendmentsRenewals(protocolNumber);
            if(vctAmendRenewals != null && vctAmendRenewals.size() > 0){
                ProtocolAmendRenewalBean protocolAmendRenewalBean = null;
                protocolAmendRenewalBean = (ProtocolAmendRenewalBean)vctAmendRenewals.elementAt(0);
                amendmentRenewalNumber = protocolAmendRenewalBean.getProtocolNumber();
                amendmentRenewalSeqNumber = protocolAmendRenewalBean.getSequenceNumber();
                if(amendmentRenewalNumber!=null){
                    isAmendRenewalApproved = true;
                }
            }
        }
        //If not amend/renewal approved
        if(!isAmendRenewalApproved){
           
            boolean isAuthorised = false;
            isAuthorised = txnData.getUserHasIACUCProtocolRight(loggedinUser, MODIFY_IACUC_PROTOCOL, protocolNumber);
            //If no rights check at Unit level right
            if(!isAuthorised){
                isAuthorised = txnData.getUserHasRight(loggedinUser, MODIFY_ANY_IACUC_PROTOCOL, unitNumber);
            }
//          if(!isAuthorised) {
//              isAuthorised = checkCommitteeRight(protocolNumber, userInfoBean.getPersonId());
//          }
            //Unit level right
            //Modified for Case#4312 - Non-IRB admin has ability to edit Funding Source outside of an amendment
//            int status =0;
            int status = protocolDataTxnBean.performEditValidation(protocolNumber);
            boolean  hasModify = false;
           
            if(isAuthorised){
                //Commented for Case#4312 - Non-IRB admin has ability to edit Funding Source outside of an amendment
//                status = protocolDataTxnBean.performEditValidation(protocolNumber);
                //Code added for coeus4.3 enhancements - starts.
                //Get the status of the protocol, the status is in edit mode, 
                //then check whether the protocol sequence number to be incremented or not.
                WebTxnBean webTxnBean = new WebTxnBean();
                HashMap hmProtocolNumber = new HashMap();
                hmProtocolNumber.put(PROTOCOL_NUMBER, protocolNumber);
                Hashtable htGeneralInfo =(Hashtable) webTxnBean.getResults(request, "getIacucInfo", hmProtocolNumber);
                Vector cvProtoData=(Vector)htGeneralInfo.get("getIacucInfo");
                if(cvProtoData!= null && cvProtoData.size() >0){
                    DynaValidatorForm dynaValidatorForm = (DynaValidatorForm)cvProtoData.get(0);
                    int code = Integer.parseInt(dynaValidatorForm.get("protocolStatusCode").toString());

                    if(code == RETURN_TO_PI_PROTOCOL_STATUS ||
                       code == WITHDRAWN ||
                       code == TABLED_PROTOCOL_STATUS || 
                       code == MINOR_REVISION_REQUIRED_PROTOCOL_STATUS ||
                       code == MAJOR_REVISION_REQUIRED_PROTOCOL_STATUS ||
                       code == ADMINISTRATIVELY_WITHDRAWN ||
                       code == DISAPPROVED ||
                       // Added for COEUSQA-2666: Complete Administrative Review functionality in IACUC - Start
                       code == ADMINISTRATIVELY_INCOMPLETE_STATUS){
                       // COEUSQA-2666: End
                        status = 1;
                        Hashtable htSeqNo = (Hashtable) webTxnBean.getResults(request, "generateIacucNewSequence", hmProtocolNumber);
                        String seqNo = ((String)((HashMap)htSeqNo.get("generateIacucNewSequence")).get("SEQ_CODE"));
                        if(seqNo!=null &&  seqNo.equals("1")){
                            String seq = (String) session.getAttribute(CoeusLiteConstants.IACUC_SEQUENCE_NUMBER+session.getId());
                            if(seq!=null){
                                seq = new Integer((new Integer(seq).intValue()+1)).toString();
                                session.setAttribute("iacucNewSeq"+session.getId(), "incremented");
                                session.setAttribute(CoeusLiteConstants.IACUC_SEQUENCE_NUMBER+session.getId(),seq);
                            }else{
                                session.removeAttribute("iacucNewSeq"+session.getId());
                            }
                        }
                    }else{
                        session.removeAttribute("iacucNewSeq"+session.getId());
                    }
                }
                //Code added for coeus4.3 enhancements - ends.
                if (status == 1){
                    hasModify = true;
                }
                else{
                    hasModify =  false;
//                    errMsg = "protocoledit_exceptionCode."+status;
//                    ActionMessage message = new ActionMessage(errMsg);
//                    actionMessages.add(ActionMessages.GLOBAL_MESSAGE,message);
//                    saveMessages(request,actionMessages);
//                    if( protocolNumber.indexOf('A') != -1 ) {
//                        errMsg = "amendedit_exceptionCode."+status;
//                        actionMessages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage(errMsg.trim()));
//                        saveMessages(request,actionMessages);
//                    }else if( protocolNumber.indexOf('R') != -1 ){
//                        errMsg = "renewaledit_exceptionCode."+status;
//                        actionMessages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage(errMsg));
//                        saveMessages(request,actionMessages);
//                    }
                     isEdit = true;
//                    isEditable = false;
                }
                 //Case 2026 Start
                if (status == 1 || status == 2501 || status == 2502 ){
                    hasModify = true;
                }
                //Case 2026 End
                
            }else{
                hasModify =  false;
//                errMsg = "protocolAuthorization_exceptionCode."+3003;
//                actionMessages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage(errMsg));
//                saveMessages(request,actionMessages);
                 isEdit = true;
//                isEditable = false;
            }
            
          //Case 2026 Start        
        /** Set the hasModify flag to the session. So that, in Funding source,evenb though
         *the window is opened in display mode, the funcding source should be able to
         *edit the data
         */
         //Added for Case#4312 - Non-IRB admin has ability to edit Funding Source outside of an amendment  - Start
         //Checks user has PERFORM_IACUC_ACTIONS_ON_PROTO rights to edit Funding source in protocol
            if(!isAmendRenewal){
                //Case 4590 : Changes in special review being wiped out after an amendment is approved - Protocol - Start
                boolean canModifyInDisplay      = false;
                String sequenceNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_SEQUENCE_NUMBER+session.getId());
                String protocolLeadUnit = protocolDataTxnBean.getLeadUnitForProtocol(protocolNumber,Integer.parseInt(sequenceNumber));
                ProtocolAuthorizationBean protocolAuthorizationBean = new ProtocolAuthorizationBean();
                canModifyInDisplay = protocolAuthorizationBean.hasIRBAdminRights(loggedinUser, protocolLeadUnit);
                //status = protocolDataTxnBean.performEditValidation(protocolNumber);
                //Status 1 - If protocol is in Editable mode
                //Status 2501 - If Protocol Status is Active-Open to Enrollment/Active - Closed to Enrollment/Active - Data Analysis Only
                //Status 2502 - If Protocol Status is in Closed Administratively for lack of response/Closed by Investigator
                if(canModifyInDisplay){
                    // Modified for COEUSQA-2692_Allow an investigator to abandon an IRB protocol that has never been approved_start
                    if ( status == 4004 || status == 4005 || status == 4006|| status == 4007|| status == 4008
                            || status == 4011){
                        canModifyInDisplay = false;
                    }
                    // Modified for COEUSQA-2692_Allow an investigator to abandon an IRB protocol that has never been approved_end
                    //step 3 : Check if the modules are already selected in amendment/renewal
                    if(canModifyInDisplay){
                        //Added with case 4398 - Funding source added directly is lost when an amendment is approved.
                        if(!protocolDataTxnBean.isModuleAddedinAmendmentRenewal(protocolNumber,CoeusliteMenuItems.IACUC_PROTOCOL_FUNDING_SOURCE_MENU)){
                            canModifyFundingSource = true;
                        }
                        //case 4398 - end
                        //Check if the user can modify special review in display mode
                        if(!protocolDataTxnBean.isModuleAddedinAmendmentRenewal(protocolNumber,CoeusliteMenuItems.IACUC_PROTOCOL_SPECIAL_REVIEW_MENU)){
                            canModifySpecialReview = true;
                        }
                    }
                    
                }
         }
        //session.setAttribute("MODIFY_PROTOCOL_FUNDING_SOURCE"+session.getId(), new Boolean(hasModify));
        session.setAttribute("MODIFY_IACUC_PROTOCOL_FUNDING_SOURCE"+session.getId(), new Boolean(canModifyFundingSource));
        session.setAttribute("MODIFY_IACUC_SPECIAL_RVW"+session.getId(), new Boolean(canModifySpecialReview));
        //Case 4590 End
        //Case#4312 - End
        //Case 2026 End
            
        }else{
            coeusMessageResourcesBean =new CoeusMessageResourcesBean();
            errMsg = "protocolAuthorization_exceptionCode."+3007;
            actionMessages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage(errMsg));
            saveMessages(request,actionMessages);
             isEdit = true;
//            isEditable = false;
        }
      
//        /** Check the lock if it is not in the Display mode. If lock present then set
//         * the mode as Display mode else it will be Modify mode
//         */
//        if(!isEditable ) {
//            boolean lock = isLockPresent(protocolNumber, userInfoBean.getUserName());
//            if(!lock){
//                //start--11
//                
//                session.setAttribute(CoeusLiteConstants.IACUC_MODE_DETAILS,CoeusLiteConstants.DISPLAY_MODE);
//                //end--11
//                
//                isEditable = true;
//            }
//        }
        isEditable = !isEdit;
        return isEditable;
    }
    
    /*
     * Coeus4.3 Enhacement - Start
     * Added for Protocol Right Checking
     */
    
//     private boolean checkCommitteeRight(String protocolNumber, String strPersonId) throws Exception {
//        boolean hasRight = false;
//        ProtocolDataTxnBean protocolTxnBean = new ProtocolDataTxnBean();
//        ProtocolInfoBean protocolData = null;
//        protocolData = protocolTxnBean.getIacucInfo(protocolNumber);
//        Vector vecOrgId = protocolTxnBean.getOrgId(protocolNumber, new Integer(protocolData.getSequenceNumber()).toString());
//        if(vecOrgId != null && vecOrgId.size() > 0 ) {
//            for(int index = 0; index < vecOrgId.size(); index++) {
//                String organizationId = (String)vecOrgId.get(index);
//                Vector vecCorrespList = new Vector();
//                vecCorrespList = protocolTxnBean.getCorrespList(organizationId);
//                if(vecCorrespList != null && vecCorrespList.size() >0) {
//                    if(vecCorrespList.contains(strPersonId)) {
//                        hasRight = true;
//                    }
//                }
//            }
//        }
//
//        if(hasRight == false) {
//            String committeeId = EMPTY_STRING;
//            String scheduleId = EMPTY_STRING;
//            Vector vecCommitteeList = protocolTxnBean.getCommSchId(protocolNumber);
//            if(vecCommitteeList != null && vecCommitteeList.size() > 0) {
//                for(int index = 0; index < vecCommitteeList.size(); index++) {
//                    Vector vecMemberList = new Vector();
//                    HashMap hmCommList = (HashMap)vecCommitteeList.get(index);
//                    committeeId = (String) hmCommList.get("committeeId");
//                    scheduleId = (String) hmCommList.get("scheduleID");
//                    vecMemberList = protocolTxnBean.getActiveMemList(committeeId, scheduleId);
//                    if(vecMemberList != null && vecMemberList.size() > 0) {
//                        if(vecMemberList.contains(strPersonId)) {
//                            hasRight = true;
//                        }
//                    }
//                }
//            }
//        }
//        return hasRight;
//}
//Coeus4.3 Enhancement Protocol Right Checking - End
         
    /** Check the lock while opening the protocol. If the lock exists, then
     *populate the error messages and sent to the client side.
     */
    public boolean isLockPresent(String protocolNumber, String userName, HttpServletRequest request) throws DBException, CoeusException{
        String errMsg = EMPTY_STRING;
        
        TransactionMonitor transMon = TransactionMonitor.getInstance();
        
//        String rowId = this.rowLockStr + protocolNumber;
        String rowId = CoeusLiteConstants.IACUC_PROTO_LOCK_STR + protocolNumber;
        boolean lockCheck = transMon.isLockAvailable(rowId);
        if(!lockCheck ){
            errMsg = "protocol_lock_exception.0001";
            ActionMessages messages = new ActionMessages();
            
            messages.add("userName", new ActionMessage(errMsg,userName,protocolNumber));
            saveMessages(request, messages);
        }else{
            lockCheck = true;
        }
        return lockCheck;
    }
    
    
    /** Check the lock while opening the protocol. If the lock exists for logged in user, then
     *populate the error messages and sent to the client side.
     */
    public boolean isLockPresent(String protocolNumber,
    String loggedinUser,String unitNumber, HttpServletRequest request) throws DBException, CoeusException{
        
        String errMsg = EMPTY_STRING;

        TransactionMonitor transMon = TransactionMonitor.getInstance();
        
           String rowId = CoeusLiteConstants.IACUC_PROTO_LOCK_STR  + protocolNumber;
            boolean lockCheck = transMon.lockAvailabilityCheck(rowId, loggedinUser);
            if(lockCheck ){
                errMsg = "protocol_lock_exception.0001";
                ActionMessages messages = new ActionMessages();                
                messages.add("userName", new ActionMessage(errMsg,loggedinUser,protocolNumber));
                saveMessages(request, messages);
            }
            return lockCheck;
        
    }
    
    
    public Timestamp prepareTimeStamp(DynaValidatorForm dynaValidatorForm) throws Exception{
        Timestamp dbTimestamp = null;
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        dbTimestamp = coeusFunctions.getDBTimestamp();
        return dbTimestamp;
    }
    
    public Timestamp prepareTimeStamp() throws Exception{
        Timestamp dbTimestamp = null;
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        dbTimestamp = coeusFunctions.getDBTimestamp();
        return dbTimestamp;
    }
    
//    private void loadSaveFlagStatus(String protocolNumber)
//    throws CoeusException,DBException {
//        Vector menuItemsVector = null;
//        if(protocolNumber!= null){
//            ProtocolTxnBean protocolTxnBean = new ProtocolTxnBean();
//            Vector vecResult = protocolTxnBean.getSaveStatus(protocolNumber);
//            if (vecResult != null && vecResult.size()>0) {
//                HashMap hmData = (HashMap)vecResult.get(0);
//                String generalInfo = hmData.get("GENERAL_INFO").toString();
//                String investigator = hmData.get("INVESTIGATOR").toString();
//                String keyStudyPerosnnel = hmData.get("KEY_STUDY_PERSONNEL").toString();
//                String areasOfResearch = hmData.get("AREAS_OF_RESEARCH").toString();
//                String fundingSource = hmData.get("FUNDING_SOURCE").toString();
//                String vulnerableSubjects = hmData.get("VULNERABLE_SUBJECTS").toString();
//                String specialReview = hmData.get("SPECIAL_REVIEW").toString();
//                String uploadDocuments = hmData.get("UPLOAD_DOCUMENTS").toString();
//                boolean isGeneralInfoSaved  = generalInfo.equals("Y") ? true:false;
//                boolean isInvestigatorSaved  = investigator.equals("Y") ? true:false;
//                boolean isKeyStudyPerosnnelSaved  = keyStudyPerosnnel.equals("Y") ? true:false;
//                boolean isAreasOfResearchSaved  = areasOfResearch.equals("Y") ? true:false;
//                boolean isFundingSourceSaved  = fundingSource.equals("Y") ? true:false;
//                boolean isVulnerableSubjectsSaved  = vulnerableSubjects.equals("Y") ? true:false;
//                boolean isSpecialReviewSaved  = specialReview.equals("Y") ? true:false;
//                boolean isUploadDocumentsSaved  = uploadDocuments.equals("Y") ? true:false;
//                menuItemsVector = (Vector)session.getAttribute(MENU_ITEMS);
//                if(menuItemsVector!= null && menuItemsVector.size() > 0){
//                for (int index = 0; index<menuItemsVector.size();index++) {
//                    MenuBean menuBean = (MenuBean) menuItemsVector.get(index);
//                    String menuId = menuBean.getMenuId();
//                    if (GENERAL_INFO_CODE.equals(menuId)) {
//                        menuBean.setSelected(isGeneralInfoSaved);
//                        menuBean.setDataSaved(isGeneralInfoSaved);
//                    } else if (INVESTIGATOR_CODE.equals(menuId)) {
//                        menuBean.setSelected(isInvestigatorSaved);
//                        menuBean.setDataSaved(isInvestigatorSaved);
//                    }  else if (KEY_STUDY_PERSON_CODE.equals(menuId)) {
//                        menuBean.setSelected(isKeyStudyPerosnnelSaved);
//                        menuBean.setDataSaved(isKeyStudyPerosnnelSaved);
//                    }  else if (AREAS_OF_RESEARCH_CODE.equals(menuId)) {
//                        menuBean.setSelected(isAreasOfResearchSaved);
//                        menuBean.setDataSaved(isAreasOfResearchSaved);
//                    }  else if (FUNDING_SOURCE_CODE.equals(menuId)) {
//                        menuBean.setSelected(isFundingSourceSaved);
//                        menuBean.setDataSaved(isFundingSourceSaved);
//                    }  else if (VULNERABLE_SUBJECTS_CODE.equals(menuId)) {
//                        menuBean.setSelected(isVulnerableSubjectsSaved);
//                        menuBean.setDataSaved(isVulnerableSubjectsSaved);
//                    }  else if (SPECIAL_REVIEW_CODE.equals(menuId)) {
//                        menuBean.setSelected(isSpecialReviewSaved);
//                        menuBean.setDataSaved(isSpecialReviewSaved);
//                    }  else if (UPLOAD_DOC_CODE.equals(menuId)) {
//                        menuBean.setSelected(isUploadDocumentsSaved);
//                        menuBean.setDataSaved(isUploadDocumentsSaved);
//                    }
//                }
//                session.setAttribute(MENU_ITEMS,menuItemsVector);
//            }
//                
//            }// This code has to be verified and tested...
//            else{
//             menuItemsVector = (Vector)session.getAttribute(MENU_ITEMS);
//             if(menuItemsVector!= null){
//                for (int index = 0; index<menuItemsVector.size();index++) {
//                    MenuBean menuBean = (MenuBean) menuItemsVector.get(index);
//                    String menuId = menuBean.getMenuId();
//                    if (GENERAL_INFO_CODE.equals(menuId)) {
//                        menuBean.setDataSaved(false);
//                    } else if (INVESTIGATOR_CODE.equals(menuId)) {
//                        menuBean.setDataSaved(false);
//                    }  else if (KEY_STUDY_PERSON_CODE.equals(menuId)) {
//                        menuBean.setDataSaved(false);
//                    }  else if (AREAS_OF_RESEARCH_CODE.equals(menuId)) {
//                        menuBean.setDataSaved(false);
//                    }  else if (FUNDING_SOURCE_CODE.equals(menuId)) {
//                        menuBean.setDataSaved(false);
//                    }  else if (VULNERABLE_SUBJECTS_CODE.equals(menuId)) {
//                        menuBean.setDataSaved(false);
//                    }  else if (SPECIAL_REVIEW_CODE.equals(menuId)) {
//                        menuBean.setDataSaved(false);
//                    }  else if (UPLOAD_DOC_CODE.equals(menuId)) {
//                        menuBean.setDataSaved(false);
//                    }
//                }
//        }
//                session.setAttribute(MENU_ITEMS,menuItemsVector);
//            }// End
//        }else{
//            // Added by chandra. set all the flags to false
//             menuItemsVector = (Vector)session.getAttribute(MENU_ITEMS);
//             if(menuItemsVector!= null){
//                for (int index = 0; index<menuItemsVector.size();index++) {
//                    MenuBean menuBean = (MenuBean) menuItemsVector.get(index);
//                    String menuId = menuBean.getMenuId();
//                    if (GENERAL_INFO_CODE.equals(menuId)) {
//                        menuBean.setDataSaved(false);
//                    } else if (INVESTIGATOR_CODE.equals(menuId)) {
//                        menuBean.setDataSaved(false);
//                    }  else if (KEY_STUDY_PERSON_CODE.equals(menuId)) {
//                        menuBean.setDataSaved(false);
//                    }  else if (AREAS_OF_RESEARCH_CODE.equals(menuId)) {
//                        menuBean.setDataSaved(false);
//                    }  else if (FUNDING_SOURCE_CODE.equals(menuId)) {
//                        menuBean.setDataSaved(false);
//                    }  else if (VULNERABLE_SUBJECTS_CODE.equals(menuId)) {
//                        menuBean.setDataSaved(false);
//                    }  else if (SPECIAL_REVIEW_CODE.equals(menuId)) {
//                        menuBean.setDataSaved(false);
//                    }  else if (UPLOAD_DOC_CODE.equals(menuId)) {
//                        menuBean.setDataSaved(false);
//                    }
//                }
//        }
//                session.setAttribute(MENU_ITEMS,menuItemsVector);
//        }
//        
//    }
    
//    private void releaseLock(String protocolNum, HttpServletRequest request) throws DBException, CoeusException {
      protected void releaseLock(String protocolNum, HttpServletRequest request) throws DBException, CoeusException {
        HttpSession session = request.getSession();
//        String rowId = "osp$IACUC Protocol_"+protocolNum;
        String rowId = CoeusLiteConstants.IACUC_PROTO_LOCK_STR+protocolNum;  
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute(USER+session.getId());
        TransactionMonitor transactionMonitor = TransactionMonitor.getInstance();
        boolean lockCheck = transactionMonitor.isLockAvailable(rowId);
        //start--12
        
//        String mode = (String)session.getAttribute(CoeusLiteConstants.MODE_DETAILS+session.getId());
        //end--12
        if(!lockCheck) {
            transactionMonitor.releaseEdit(rowId,userInfoBean.getUserId());
        }
    }
    
    protected void canLock(String mode,String protocolNum,String unitNum,String user) throws DBException,CoeusException{
        if(mode.equals("D") || mode.equals("A") || protocolNum == null) {
            return;
        }
        
        TransactionMonitor transactionMonitor = new TransactionMonitor();
        DBEngineImpl dBEngineImpl = new DBEngineImpl();
        Connection conn = null;
        try{
            conn = dBEngineImpl.beginTxn();
//            String rowId = "osp$IACUC Protocol_"+protocolNum;
            String rowId = CoeusLiteConstants.IACUC_PROTO_LOCK_STR+protocolNum;
            
            //If lock already exists/available for this user no need to insert
            boolean lockCheck = transactionMonitor.isLockAvailable(rowId);
            if(lockCheck) {
                transactionMonitor.canEdit(rowId, user, unitNum, conn);
                dBEngineImpl.endTxn(conn);
            }
        }catch(Exception ex) {
            ex.printStackTrace();
        }
        finally{
            dBEngineImpl.endTxn(conn);
        }
    }
    
    /**getting protocol header details
     *accepts the protocolNumber
     *returns Vector containing the details for the header
     **/
   public Vector getProtocolHeader(String protocolNumber, HttpServletRequest request) throws Exception{
        HashMap hmDetails= new HashMap();        
        hmDetails.put(PROTOCOL_NUMBER,protocolNumber);
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htProtocolHeaderDetails=(Hashtable)webTxnBean.getResults(request, "getIacucHeaderDetails",hmDetails );
        Vector vecProtocolHeader = (Vector)htProtocolHeaderDetails.get("getIacucHeaderDetails");
        return vecProtocolHeader;
    }
   //Added for Protocol Upload Documents Enhancements start
   /**
    *to get the latest Version of the document for a given document type
    *accepts Vector containing all the details of all documents 
    *
    */
   protected List getDocumentForVersion(Vector vecUploadData, HttpServletRequest request){
            HttpSession session = request.getSession();  
           Vector vecDocTypes=(Vector)session.getAttribute("DocTypes");
           vecDocTypes =(vecDocTypes != null)?vecDocTypes: new Vector();
           java.util.List vecUpload = null;
           edu.mit.coeus.utils.CoeusVector cvData = new edu.mit.coeus.utils.CoeusVector();
           cvData.addAll(vecUploadData == null ? new Vector() : vecUploadData);
           edu.mit.coeus.utils.CoeusVector cvFilterData = new edu.mit.coeus.utils.CoeusVector();
           edu.mit.coeus.utils.query.Equals eqDocCode;
           edu.mit.coeus.iacuc.bean.UploadDocumentBean uploadBean;
           vecUpload = new Vector();
           if(vecDocTypes != null && vecDocTypes.size() > 0 ){
               for(int index = 0; index < vecDocTypes.size() ; index ++) {
                   int documentCodeType
                   = Integer.parseInt(((edu.mit.coeus.utils.ComboBoxBean)vecDocTypes .get(index)).getCode());
                   eqDocCode = new edu.mit.coeus.utils.query.Equals("docCode",new Integer(documentCodeType));
                   cvFilterData = cvData.filter(eqDocCode);
                   if(cvFilterData.size()>0){
                       cvFilterData.sort("versionNumber" , false);
                       uploadBean = (edu.mit.coeus.iacuc.bean.UploadDocumentBean)cvFilterData.get(0);
                       vecUpload.add(uploadBean);
                       cvFilterData.removeAllElements();
                   }
               }
           }
           return (vecUpload == null ? new Vector() : vecUpload);
   }
   
  
     /** Read the save status for the given protocol number and sequence number
     *@throws Exception
     */
    protected void readSavedStatus(HttpServletRequest request) throws Exception{        
        Hashtable htReqData =null;
        HashMap hmMenuData = null;
        String search = request.getParameter("IACUC_TYPE");
        HttpSession session = request.getSession();
        String protocolNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId());
        // If protocol number not null and the action is based on some header don't perform read
        if(protocolNumber == null || search!= null){
            return ;
        }
        String seq = (String)session.getAttribute(CoeusLiteConstants.IACUC_SEQUENCE_NUMBER+session.getId());
        int sequeneceNumber = Integer.parseInt(seq);
        Vector menuData= (Vector)request.getSession().getAttribute(CoeusliteMenuItems.IACUC_MENU_ITEMS);
        WebTxnBean webTxnBean = new WebTxnBean();
        if(menuData!= null && menuData.size() > 0){
            htReqData = new Hashtable();
            hmMenuData =new HashMap();
            MenuBean dataBean = null;
            String menuId = EMPTY_STRING;
            String strValue = EMPTY_STRING;
            boolean isDynamic = false;
            HashMap hmReturnData= null;
//            HashMap hmQuestionnaire = new HashMap();
            for(int index = 0; index < menuData.size(); index++){
                dataBean = (MenuBean)menuData.get(index);
                 /**Checkk for the dynamically created menu's. For example
                 *Questionnaire Menu. the dynamicId specifies the dynamic menu ids
                 *generated. At present it gets the dynamic Id for the questionnaire
                 *Menu and makes server call to show the saved questionnaire menu
                 */
                if(dataBean.getDynamicId()!= null && !dataBean.getDynamicId().equals(EMPTY_STRING)){
                    menuId =dataBean.getDynamicId();
                    isDynamic = true;
                }else{
                    menuId = dataBean.getMenuId();
                    isDynamic = false;
                }
                hmMenuData.put(PROTOCOL_NUMBER, protocolNumber);
//                hmQuestionnaire.put("proposalNumber",protocolNumber);
//                hmQuestionnaire.put("menuId", menuId);
                //COEUSQA-3035-Indicator Logic Implementation for new IACUC Screens-Start
                //Modified for null check
                int currentSequence = Integer.parseInt(seq);
                if(session.getAttribute("oldSequenceNumber") != null){
                    currentSequence = Integer.parseInt(session.getAttribute("oldSequenceNumber").toString());                                
                }
                //hmMenuData.put("sequenceNumber", new Integer(sequeneceNumber));
                hmMenuData.put("sequenceNumber", new Integer(currentSequence));
                //COEUSQA-3035-Indicator Logic Implementation for new IACUC Screens-End
                hmMenuData.put("menuId", menuId);
                if(isDynamic){
                    // Added with CoeusQA2313: Completion of Questionnaire for Submission
                    HashMap hmQuestionnaire = new HashMap();
                    QuestionnaireAnswerHeaderBean headerBean = (QuestionnaireAnswerHeaderBean)dataBean.getUserObject();
                    hmQuestionnaire.put("moduleCode",headerBean.getModuleItemCode());
                    hmQuestionnaire.put("subModuleCode",new Integer(headerBean.getApplicableSubmoduleCode()));
                    hmQuestionnaire.put("menuId", menuId);
                    if("Y".equals(headerBean.getQuestionnaireCompletionFlag())){
                        hmQuestionnaire.put("moduleItemKey",headerBean.getApplicableModuleItemKey());
                        hmQuestionnaire.put("moduleItemKeySequence",headerBean.getApplicableModuleSubItemKey());
                    }else{
                        hmQuestionnaire.put("moduleItemKey",protocolNumber);
                        hmQuestionnaire.put("moduleItemKeySequence",sequeneceNumber);
                    }
                    // CoeusQA2313: Completion of Questionnaire for Submission - End
                    htReqData = (Hashtable)webTxnBean.getResults(request, "getIacucSavedQuestionnaireData", hmQuestionnaire);
                    hmReturnData = (HashMap)htReqData.get("getIacucSavedQuestionnaireData");
                }else{
                    htReqData = (Hashtable)webTxnBean.getResults(request, "getSavedIacucData", hmMenuData);
                    hmReturnData = (HashMap)htReqData.get("getSavedIacucData");
                }
                if(hmReturnData!=null) {
                    strValue = (String)hmReturnData.get("AV_SAVED_DATA");
                    int value = Integer.parseInt(strValue);
                    if(value == 1){
                        dataBean.setDataSaved(true);
                    }else if(value == 0){
                        dataBean.setDataSaved(false);
                    }
                }
            }
            //COEUSQA-3035-Indicator Logic Implementation for new IACUC Screens-Start             
            session.setAttribute("oldSequenceNumber",sequeneceNumber);
            //COEUSQA-3035-Indicator Logic Implementation for new IACUC Screens-End
            request.getSession().removeAttribute(MENU_ITEMS);
            request.getSession().setAttribute(MENU_ITEMS, menuData);
        }
    }
    // Commented for PMD Check
    /** it is helper method to print the data of saved menu list for debugging
     */
//    private void printData(Vector data) throws Exception{
//        String dataItem = "";
//        if(data!= null && data.size() > 0){
//            for(int index = 0; index < data.size(); index++){
//                MenuBean menuBean = (MenuBean)data.get(index);
//                if(menuBean.isDataSaved()){
//                    dataItem = "Saved";
//                }else{
//                    dataItem = " Not saved";
//                }
//            }
//        }
//    }
    
    protected void readNavigationPath(HttpServletRequest request) throws Exception{
            readNavigationPath(null,request);
    }
    protected void readNavigationPath(String subHeaderId, HttpServletRequest request) throws Exception{
        // 3282: Reviewer View of Protocol materials - Start
        String subHeaderPath = EMPTY_STRING;
        HttpSession session = request.getSession();
        UserInfoBean userInfoBean = (UserInfoBean) session.getAttribute(USER+session.getId());
        boolean isUserProtocolReviewer = checkReviewerRight(request, userInfoBean);
        if(isUserProtocolReviewer){
            subHeaderPath = REVIEWER_SUB_HEADER_PATH;
        } else {
            subHeaderPath = SUB_HEADER_PATH;
        }
        // 3282: Reviewer View of Protocol materials - End
        ReadProtocolDetails readProtocolDetails = new ReadProtocolDetails();
        Vector headerDetailsVec = (Vector)session.getAttribute(SUB_HEADER+session.getId());
        if (headerDetailsVec == null || headerDetailsVec.size()==0 ) {
            // 3282: Reviewer View of Protocol materials - Start
//                headerDetailsVec = readProtocolDetails.readXMLDataForSubHeader(SUB_HEADER_PATH );
            headerDetailsVec = readProtocolDetails.readXMLDataForSubHeader(subHeaderPath);
            // 3282: Reviewer View of Protocol materials - End
        }
//Commented for Protocol SubHeader 7/2/2007 - Start       
//        if(subHeaderId!=null)
//Commented for Protocol SubHeader 7/2/2007 - End               
            headerDetailsVec = readSelectedPath(subHeaderId,headerDetailsVec);
            session.setAttribute(SUB_HEADER+session.getId(),headerDetailsVec);
    }
   
    //Coeus 4.3 Enhancement: New Sub menu Pending PI Action  - Start
     protected void secondLevelHeaderPath(HttpServletRequest request) throws Exception{
            secondLevelHeaderPath(null,request);
    }
    protected void secondLevelHeaderPath(String subHeaderId, HttpServletRequest request) throws Exception{
        // 3282: Reviewer View of Protocol materials - Start
        String subHeaderPath = EMPTY_STRING;
        HttpSession session = request.getSession();
        UserInfoBean userInfoBean = (UserInfoBean) session.getAttribute(USER+session.getId());
        boolean isUserProtocolReviewer = checkReviewerRight(request, userInfoBean);
        if(isUserProtocolReviewer){
            subHeaderPath = REVIEWER_SUB_HEADER_PATH;
        } else {
            subHeaderPath = SUB_HEADER_PATH;
        }
        // 3282: Reviewer View of Protocol materials - End
        ReadProtocolDetails readProtocolDetails = new ReadProtocolDetails();
            Vector vecSecondHeader = (Vector)session.getAttribute(SUB_HEADER+session.getId());
            if(vecSecondHeader ==  null || vecSecondHeader.size() == 0){
                // 3282: Reviewer View of Protocol materials - Start
//                vecSecondHeader  = readProtocolDetails.readXMLDataForSubHeader("/edu/mit/coeuslite/irb/xml/ProtocolSubMenu.xml" );
                vecSecondHeader  = readProtocolDetails.readXMLDataForSubHeader(subHeaderPath);
                // 3282: Reviewer View of Protocol materials - End
            }
            vecSecondHeader = readSelectedPath(subHeaderId,vecSecondHeader);
            session.setAttribute("slSubHeaderVector"+session.getId(), vecSecondHeader);
    }
    //Coeus 4.3 Enhancement: New Sub menu Pending PI Action -End
    /** Read all the Protocol Menu's
     */
    public void readProtocolMenus(HttpServletRequest request) throws Exception{
        Vector menuItemsVector  = null;
        HttpSession session = request.getSession();
        ReadProtocolDetails readProtocolDetails = new ReadProtocolDetails();
        menuItemsVector = (Vector) session.getAttribute(MENU_ITEMS);
        HashMap hmData = null ;
        if (menuItemsVector == null || menuItemsVector.size()==0) {
            // COEUSDEV-86: Questionnaire for a Submission - Start
            String protocolNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId());
            protocolNumber = protocolNumber == null ? EMPTY_STRING:protocolNumber;
            String seqNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_SEQUENCE_NUMBER+session.getId());
            seqNumber = seqNumber == null ? EMPTY_STRING:seqNumber;
            hmData = new HashMap(); 
            menuItemsVector = readProtocolDetails.readXMLDataForMenu(PROTOCOL_MENU_PATH);
            hmData.put(ModuleDataBean.class , new Integer(ModuleConstants.IACUC_MODULE_CODE));
//            hmData.put(SubModuleDataBean.class , new Integer(0));
            int subModuleItemCode = 0;
            if(protocolNumber.length() > 10 && isIacucProtoAmendmentRenewal(protocolNumber.charAt(10))){
                subModuleItemCode = CoeusLiteConstants.IRB_SUB_MODULE_CODE_FOR_AMENDMENT_RENEWAL;
            } else {
                subModuleItemCode = 0;
            }
            hmData.put(SubModuleDataBean.class , new Integer(subModuleItemCode));
            
            hmData.put("link" , "/getProtocolQuestionnaire.do");
            hmData.put("actionFrom" ,"IACUC_PROTOCOL");
            //Modified for Case#3941 -Questionnaire answers missing after deleting the module - Start
//            String protocolNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId());
//            protocolNumber = protocolNumber == null ? EMPTY_STRING:protocolNumber;
//            String seqNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_SEQUENCE_NUMBER+session.getId());
//            seqNumber = seqNumber == null ? EMPTY_STRING:seqNumber;
            // COEUSDEV-86: Questionnaire for a Submission - End
            hmData.put("moduleItemKey",protocolNumber);
            hmData.put("moduleSubItemKey",seqNumber);
            //Modified for Case#3941 -Questionnaire answers missing after deleting the module - End
            menuItemsVector = getQuestionnaireMenuData(menuItemsVector ,  request , hmData );
            session.setAttribute(MENU_ITEMS, menuItemsVector);
        }else{
            session.setAttribute(MENU_ITEMS, menuItemsVector);
        }
    }
    
    /** Manufacture the LockBean based on the parameter passed by the specific module
     *say, Propsoal, Protocol, Budget etc.
     *@param UserInfoBean, Proposal number
     *@returns LockBean
     *@throws Exception
     */
    protected LockBean getLockingBean(UserInfoBean userInfoBean, String protocolNumber, HttpServletRequest request) throws Exception{
        LockBean lockBean = new LockBean();
        lockBean.setLockId(CoeusLiteConstants.IACUC_PROTO_LOCK_STR +protocolNumber);
        String mode = (String)request.getSession().getAttribute(CoeusLiteConstants.MODE_DETAILS+request.getSession().getId());
        mode = getMode(mode);
        lockBean.setMode(mode);
        lockBean.setModuleKey(CoeusLiteConstants.PROTOCOL_MODULE);
        lockBean.setModuleNumber(protocolNumber);
        lockBean.setModuleUnitNumber(userInfoBean.getUnitNumber());
        lockBean.setUnitNumber(UNIT_NUMBER);
        lockBean.setUserId(userInfoBean.getUserId());
        lockBean.setUserName(userInfoBean.getUserName());
        lockBean.setSessionId(request.getSession().getId());
        return lockBean;
    }
    
    protected String getMode(String mode) throws Exception{
        if(mode!= null && !mode.equals(EMPTY_STRING)){
            if(mode.equalsIgnoreCase(CoeusLiteConstants.DISPLAY_MODE)){
                mode = CoeusLiteConstants.DISPLAY_MODE;
            }
        }else{
            mode = CoeusLiteConstants.MODIFY_MODE;
        }
        return mode;
    }
    
    /** This method will notify for the acquiring and releasing the Lock
     *based on the way the locks are opened.
     *It will check whether the protocol is opened through search or list
     *Based on the conditions it will acquire the lock and release the lock
     *If it locked then it will prepare the locking messages
     *@param UserInfoBean, ProtocolNumber(Current)
     *@throws Exception
     *@returns boolean is locked or not
     */
    protected boolean prepareLock(String protocolNumber, HttpServletRequest request) throws Exception{
        HttpSession session = request.getSession();
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute(USER+session.getId());
        boolean isSuccess = true;
//        WebTxnBean webTxnBean = new WebTxnBean();
        String mode = (String)session.getAttribute(CoeusLiteConstants.MODE_DETAILS+session.getId());
        SearchModuleBean moduleBean = (SearchModuleBean)session.getAttribute(CoeusLiteConstants.PROTOCOL_SEARCH_ACTION+session.getId());
        LockBean lockBean = null;
        LockBean sessionLockBean = (LockBean)session.getAttribute(CoeusLiteConstants.LOCK_BEAN+session.getId());
    
        // If the action is from search window
        if(moduleBean!= null && !moduleBean.getOldModuleNumber().equals(EMPTY_STRING)){
            if(!moduleBean.getModuleNumber().equals(moduleBean.getOldModuleNumber())){
                // If the existing protocol number is not in DISPLAY MODE, release the lcok
                if(!moduleBean.getOldMode().equals(CoeusLiteConstants.DISPLAY_MODE)){
                    lockBean = getLockingBean(userInfoBean, moduleBean.getOldModuleNumber(), request);
                    LockBean serverDataBean = getLockedData(CoeusLiteConstants.IACUC_PROTO_LOCK_STR+lockBean.getModuleNumber(), request);
                    if(serverDataBean!=null && lockBean.getSessionId().equals(serverDataBean.getSessionId())) {
                        releaseLock(lockBean,request);
                        session.removeAttribute(CoeusLiteConstants.PROTOCOL_SEARCH_ACTION+session.getId());
                        session.setAttribute(CoeusLiteConstants.RECORD_LOCKED+session.getId(), 
                            new Boolean(false));
                    }
                }
            }
            moduleBean.setMode(getMode(mode));
            // If the current Protocol number is in MODIFY MODE then lock it
                if(!moduleBean.getMode().equals(CoeusLiteConstants.DISPLAY_MODE)){
                    lockBean = getLockingBean(userInfoBean,moduleBean.getModuleNumber(),request);
                    boolean isLocked = isLockExists(lockBean, lockBean.getModuleKey());
                    boolean isSessionRowLocked = false;
                    Object  isRowLocked = session.getAttribute(
                        CoeusLiteConstants.RECORD_LOCKED+session.getId());
                    if(isRowLocked!= null){
                        isSessionRowLocked = ((Boolean)isRowLocked).booleanValue();
                    }
                    /** Make server call and get the locked data. Check for the unit
                     *number. If the unit number!= 00000000 then assume that
                     *it is lokced by the coeus premium and show the message
                     */
                    LockBean serverDataBean = getLockedData(CoeusLiteConstants.IACUC_PROTO_LOCK_STR+lockBean.getModuleNumber(), request);
                    // If the current protocol is locked by other user, then show the message
                    // else lock it
                    if(!isLocked ){
                        /** Check if the same record is locked or not. If not 
                         *then only show the message else discard it
                         */
                        if(!isSessionRowLocked || (serverDataBean!= null && !serverDataBean.getSessionId().equals(lockBean.getSessionId()))){
                            showLockingMessage(lockBean.getModuleNumber(), request);
                            isSuccess = false;
                        }// End if for lockeed record in the session
                    }else{// If the record is not locked then go ahead and lock it
                        lockModule(lockBean,request);
                        session.setAttribute(CoeusLiteConstants.RECORD_LOCKED+session.getId(), 
                            new Boolean(true));
                    }
                }
            
        }else{
            // Protocol opened from list
            lockBean = getLockingBean(userInfoBean, protocolNumber,request);
	    LockBean serverDataBean = getLockedData(CoeusLiteConstants.IACUC_PROTO_LOCK_STR+lockBean.getModuleNumber(), request);
            boolean isLockExists = isLockExists(lockBean, lockBean.getModuleKey());
            if(isLockExists) {
                if(serverDataBean!=null && !lockBean.getSessionId().equals(serverDataBean.getSessionId())) {
                    isLockExists = false;
                }
            }
            /** check whether lock exists or not. If not and the mode of the 
             *protocol is not display then lock the protocol else show the message
             */
            if(isLockExists && !lockBean.getMode().equals(CoeusLiteConstants.DISPLAY_MODE)){
                lockModule(lockBean,request);
                session.setAttribute(CoeusLiteConstants.RECORD_LOCKED+session.getId(), 
                            new Boolean(true));
            }else{
                if(sessionLockBean == null && !lockBean.getMode().equals(CoeusLiteConstants.DISPLAY_MODE)){
                    showLockingMessage(lockBean.getModuleNumber(), request);
                    isSuccess = false;
                } else if(sessionLockBean!=null && serverDataBean!=null) {
                    if(!lockBean.getSessionId().equals(serverDataBean.getSessionId())) {
                        showLockingMessage(lockBean.getModuleNumber(),request);
                        isSuccess = false;                        
                    }
                }
            }
        }
        session.removeAttribute(CoeusLiteConstants.PROTOCOL_SEARCH_ACTION+session.getId());
        return isSuccess;
        
    }
    
    /** Prepare the Locking messages when other or same user locked
     *the same module number. Make server call to get the message for the
     *locked user
     *@param String moduleNumber
     *@throws Exception
     */
    protected void showLockingMessage(String moduleNumber, HttpServletRequest request) throws Exception{
        HttpSession session = request.getSession();
        String lockId = CoeusLiteConstants.IACUC_PROTO_LOCK_STR+moduleNumber;
//        WebTxnBean webTxnBean = new WebTxnBean();
        
        LockBean serverLockedBean = getLockedData(lockId,request);
        
        if(serverLockedBean!= null){
            serverLockedBean.setModuleKey(CoeusLiteConstants.PROTOCOL_MODULE);
            serverLockedBean.setModuleNumber(moduleNumber);
            //Added for the case# COEUSQA-1697-View Restriction of User in Open Record-start
            UserInfoBean userInfoBean = (UserInfoBean)request.getSession().getAttribute("user"+request.getSession().getId());  
            String loggedInUserId = userInfoBean.getUserId();
            //Added for the case# COEUSQA-1697-View Restriction of User in Open Record-end        
//            String acqLock = "acquired_lock";
//            ActionMessages messages = new ActionMessages();
//            messages.add("acqLock", new ActionMessage(acqLock,
//                serverLockedBean.getUserName(),serverLockedBean.getModuleKey(),
//                    serverLockedBean.getModuleNumber()));
            // Added for displaying user name - user id start
            //String lockUserId = serverLockedBean.getUserId();
            //UserMaintDataTxnBean userTxnBean = new UserMaintDataTxnBean();
            //String lockUserName = userTxnBean.getUserName(lockUserId);
            //Added for the case# COEUSQA-1697-View Restriction of User in Open Record-start
            String lockUserId = serverLockedBean.getUserId();          
            String lockUserName = EMPTY_STRING;
            String acqLock = "acquired_lock";
            ActionMessages messages = new ActionMessages();
            lockUserName =  viewRestrictionOfUser(loggedInUserId,lockUserId);    
            //Added for the case# COEUSQA-1697-View Restriction of User in Open Record-end
            messages.add("acqLock", new ActionMessage(acqLock,
                lockUserName,serverLockedBean.getModuleKey(),
                    serverLockedBean.getModuleNumber()));
            //End
            saveMessages(request, messages);
            session.removeAttribute(CoeusLiteConstants.LOCK_BEAN+session.getId());
        }
    }
    
//    /** Manufacture the LockBean based on the parameter passed by the specific module
//     *say, Propsoal, Protocol, Budget etc.
//     *@param UserInfoBean, Proposal number
//     *@returns LockBean
//     *@throws Exception
//     */
//    protected LockBean getInstProposalLockingBean(UserInfoBean userInfoBean, String proposalNumber, HttpServletRequest request) throws Exception{
//        LockBean lockBean = new LockBean();
//        lockBean.setLockId(CoeusLiteConstants.INST_PROP_LOCK_STR+proposalNumber);
//        String mode = (String)request.getSession().getAttribute("mode"+request.getSession().getId());
//        mode = getMode(mode);
//        lockBean.setMode(mode);
//        lockBean.setModuleKey(CoeusLiteConstants.INST_PROPOSAL_MODULE);
//        lockBean.setModuleNumber(proposalNumber);
//        lockBean.setModuleUnitNumber(userInfoBean.getUnitNumber());
//        lockBean.setUnitNumber(UNIT_NUMBER);
//        lockBean.setUserId(userInfoBean.getUserId());
//        lockBean.setUserName(userInfoBean.getUserName());
//        lockBean.setSessionId(request.getSession().getId());
//        return lockBean;
//    } 
    
//    /** Prepare the Locking messages when other or same user locked
//     *the same module number. Make server call to get the message for the
//     *locked user
//     *@param String moduleNumber
//     *@throws Exception
//     */
//    protected void showInstProposalLockingMessage(String moduleNumber, HttpServletRequest request) throws Exception{
//        HttpSession session = request.getSession();
//        String lockId = CoeusLiteConstants.INST_PROP_LOCK_STR+moduleNumber; 
//        WebTxnBean webTxnBean = new WebTxnBean();
//        
//        LockBean serverLockedBean = getLockedData(lockId,request);
//        if(serverLockedBean!= null){
//            serverLockedBean.setModuleKey(CoeusLiteConstants.INST_PROPOSAL_MODULE);
//            serverLockedBean.setModuleNumber(moduleNumber);
//            String acqLock = "acquired_lock";
//            ActionMessages messages = new ActionMessages();
//            messages.add("acqLock", new ActionMessage(acqLock,
//                serverLockedBean.getUserName(),serverLockedBean.getModuleKey(),
//                    serverLockedBean.getModuleNumber()));
//            saveMessages(request, messages);
//            session.removeAttribute(CoeusLiteConstants.LOCK_BEAN+session.getId());
//        }
//    }
     /** Manufacture the LockBean based on the parameter passed by the specific module
     *say, Propsoal, Protocol, Budget etc.
     *@param UserInfoBean, Proposal number
     *@returns LockBean
     *@throws Exception
     */
//    protected LockBean getAwardLockingBean(UserInfoBean userInfoBean, String awardNumber, HttpServletRequest request) throws Exception{
//        LockBean lockBean = new LockBean();
//        lockBean.setLockId(CoeusLiteConstants.AWARD_LOCK_STR+awardNumber);
//        String mode = (String)request.getSession().getAttribute("mode"+request.getSession().getId());
//        mode = getMode(mode);
//        lockBean.setMode(mode);
//        lockBean.setModuleKey(CoeusLiteConstants.AWARD_MODULE);
//        lockBean.setModuleNumber(awardNumber);
//        lockBean.setModuleUnitNumber(userInfoBean.getUnitNumber());
//        lockBean.setUnitNumber(UNIT_NUMBER);
//        lockBean.setUserId(userInfoBean.getUserId());
//        lockBean.setUserName(userInfoBean.getUserName());
//        lockBean.setSessionId(request.getSession().getId());
//        return lockBean;
//    }
    
//    /** Prepare the Locking messages when other or same user locked
//     *the same module number. Make server call to get the message for the
//     *locked user
//     *@param String moduleNumber
//     *@throws Exception
//     */
//    protected void showAwardLockingMessage(String moduleNumber, HttpServletRequest request) throws Exception{
//        HttpSession session = request.getSession();
//        String lockId = CoeusLiteConstants.AWARD_LOCK_STR+moduleNumber; 
//        WebTxnBean webTxnBean = new WebTxnBean();
//        
//        LockBean serverLockedBean = getLockedData(lockId,request);
//        if(serverLockedBean!= null){
//            serverLockedBean.setModuleKey(CoeusLiteConstants.AWARD_MODULE);
//            serverLockedBean.setModuleNumber(moduleNumber);
//            String acqLock = "acquired_lock";
//            ActionMessages messages = new ActionMessages();
//            messages.add("acqLock", new ActionMessage(acqLock,
//                serverLockedBean.getUserName(),serverLockedBean.getModuleKey(),
//                    serverLockedBean.getModuleNumber()));
//            saveMessages(request, messages);
//            session.removeAttribute(CoeusLiteConstants.LOCK_BEAN+session.getId());
//        }
//    }
//    
    /**
     * Added for Coeus4.3 Correspondents Page enhancement
     * This method updates the protocol correspondents table OSP$PROTOCOL_CORRESPONDENTS
     * @param inputData
     * @param request
     * @param dynaActionForm
     * @throws Exception
     */
    protected void updateCorrespondents(HashMap inputData, HttpServletRequest request, DynaActionForm dynaActionForm) throws Exception{
        
        HashMap hmCorrespondent = null;        
        Map hmProtoCorresp = null;
        Vector vecCorrespOrg = null;
        Timestamp dbTimestamp = null;        
        Boolean invFlag = (Boolean)inputData.get("investigatorFlag");
        DynaValidatorForm dynaValidatorForm = (DynaValidatorForm)dynaActionForm;
        Map hmCorrespOrg = new HashMap();
        WebTxnBean webTxnBean = new WebTxnBean();

        //For an organization or unit get all the correspondents
        if(!invFlag.booleanValue()){
            hmCorrespOrg.put("organizationId", inputData.get("orgOrUnitId"));
            //Modified for COEUSQA-2540 Need ability to separate Correspondent Types and Member roles in different modules-start
            hmCorrespOrg =(Hashtable)webTxnBean.getResults(request,"getIacucCorrespForOrg", hmCorrespOrg);
            vecCorrespOrg = (Vector) hmCorrespOrg.get("getIacucCorrespForOrg");
            //Modified for COEUSQA-2540 Need ability to separate Correspondent Types and Member roles in different modules-end
        }else{
            hmCorrespOrg.put("departmentNumber", inputData.get("orgOrUnitId"));
            //Modified for COEUSQA-2540 Need ability to separate Correspondent Types and Member roles in different modules-start
            hmCorrespOrg =(Hashtable)webTxnBean.getResults(request,"getIacucUnitLevelCorresp", hmCorrespOrg);
            vecCorrespOrg = (Vector) hmCorrespOrg.get("getIacucUnitLevelCorresp"); 
            //Modified for COEUSQA-2540 Need ability to separate Correspondent Types and Member roles in different modules-end
        }

        //Iterate thr' the correspondents        
        if(vecCorrespOrg != null && vecCorrespOrg.size() > 0){                            
            Hashtable htProtoCorresp = new Hashtable();
            for(int index = 0; index < vecCorrespOrg.size(); index++){	
                //Check for each correspondents whether it exists or not
                DynaActionForm dynaForm = (DynaActionForm)vecCorrespOrg.get(index);
                hmProtoCorresp = new HashMap();                    
                hmProtoCorresp.put(PROTOCOL_NUMBER, inputData.get(PROTOCOL_NUMBER));
                hmProtoCorresp.put("sequenceNumber", new Integer(inputData.get("sequenceNumber").toString()));
                hmProtoCorresp.put("correspondentType", dynaForm.get("correspondentType"));
                hmProtoCorresp.put("personId", dynaForm.get("personId"));
                htProtoCorresp = (Hashtable)webTxnBean.getResults(request,"checkIacucCorresp", hmProtoCorresp); 
                hmProtoCorresp = (HashMap)htProtoCorresp.get("checkIacucCorresp");
                String correspondentPresent = (String)hmProtoCorresp.get("ll_is_corresp_present");
                if(correspondentPresent.equals("0")){                                        
                    hmCorrespondent = new HashMap();                            
                    hmCorrespondent.put(PROTOCOL_NUMBER, inputData.get(PROTOCOL_NUMBER));
                    hmCorrespondent.put("sequenceNumber", inputData.get("sequenceNumber"));
                    hmCorrespondent.put("personId", dynaForm.get("personId"));
                    hmCorrespondent.put("personName", dynaForm.get("personName"));                            
                    hmCorrespondent.put("nonEmployeeFlag", dynaForm.get("nonEmployeeFlag"));
                    hmCorrespondent.put("correspondentType", dynaForm.get("correspondentType"));
                    hmCorrespondent.put("awCorrespondentType", dynaForm.get("correspondentType"));
                    hmCorrespondent.put("comments", dynaForm.get("comments"));
                    dbTimestamp = prepareTimeStamp(dynaValidatorForm);
                    hmCorrespondent.put("updateTimestamp", dbTimestamp.toString());
                    hmCorrespondent.put("acType", TypeConstants.INSERT_RECORD);                            
                    //Update Correspondents
                    webTxnBean.getResults(request, "updateIacucCorrespondentsData", hmCorrespondent);
                    hmCorrespondent = null;
                    htProtoCorresp.clear();
                }
                hmProtoCorresp.clear();
            }
            //setting the correspondents indicator to P1.
            boolean isAllDeleted = false;
            updateCorrespondentsIndicators(isAllDeleted,request,inputData);        
            readSavedStatus(request);                        
        }
    }
    
    /**
     * Added for Coeus4.3 Correspondents Page enhancement
     * To set the indicator flag for protocol correspondence to the database
     * @param isAllDeleted
     * @param request
     * @throws Exception
     */    
    private void updateCorrespondentsIndicators(boolean isAllDeleted, HttpServletRequest request, HashMap inputData) throws Exception{
        HttpSession session = request.getSession();
        Object data = session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_INDICATORS);
        HashMap hmIndicatorMap = (HashMap)data;
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute(USER+session.getId());
        WebTxnBean webTxnBean = new WebTxnBean();
        WebUtilities utils =  new WebUtilities();
        HashMap processedIndicator= (HashMap)utils.processIndicator(hmIndicatorMap, CoeusLiteConstants.CORRESPONDENT_INDICATOR, isAllDeleted);
        String processFundingIndicator = (String)processedIndicator.get(CoeusLiteConstants.CORRESPONDENT_INDICATOR);
        session.setAttribute(CoeusLiteConstants.IACUC_PROTOCOL_INDICATORS, processedIndicator);
        HashMap hashMap = new HashMap();
        hashMap.put(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER,inputData.get(PROTOCOL_NUMBER));
        hashMap.put(CoeusLiteConstants.IACUC_SEQUENCE_NUMBER,inputData.get("sequenceNumber"));
        hashMap.put(CoeusLiteConstants.FIELD,CoeusLiteConstants.CORRESPONDENT_INDICATOR_VALUE);
        hashMap.put(CoeusLiteConstants.INDICATOR,processFundingIndicator);
        Timestamp dbTimestamp = prepareTimeStamp();
        hashMap.put(CoeusLiteConstants.KEY_STUDY_TIMESTAMP,dbTimestamp.toString());
        hashMap.put(CoeusLiteConstants.USER,userInfoBean.getUserId());
        webTxnBean.getResults(request, "updateIacucIndicator", hashMap);
    }
    
    /**
     * Added for coeus4.3 enhancements
     * Check whether the module is editable(if it is Amendment/Renewal protocol)
     * @param request 
     * @param hmMenuList 
     * @throws java.lang.Exception 
     */
    protected void setSelectedMenuList(HttpServletRequest request, Map hmMenuList)throws Exception{
        // Call the super class setSelectedMenuList method to set the selected menu
        super.setSelectedMenuList(request, hmMenuList);
        // Check whether the module is editable(if it is Amendment/Renewal protocol).
        //Moved this to a seperate method with CoeusQA-2551
        checkEditableForAmendRenewal(request,hmMenuList);
    }    
    
    /**
     * Method to make a particular menu selected
     * @param request 
     * @param hmMenuList 
     * @throws java.lang.Exception 
     */
    protected void setSelectedMenu(HttpServletRequest request, Map hmMenuList)throws Exception{
        // Call the super class setSelectedMenuList method to set the selected menu
        super.setSelectedMenuList(request, hmMenuList);
    }
    
    /**
     * Method to check if a module is editable in amendment/renewal.
     * @param request 
     * @param hmMenuList 
     * @throws java.lang.Exception 
     */
    protected void checkEditableForAmendRenewal(HttpServletRequest request, Map hmMenuList)throws Exception{
        HttpSession session = request.getSession();
        String mode = (String) session.getAttribute(CoeusLiteConstants.MODE_DETAILS+
                session.getId());
        session.setAttribute("amendRenewPageMode"+session.getId(), CoeusLiteConstants.DISPLAY_MODE);
        if(mode!=null && !mode.equalsIgnoreCase(CoeusLiteConstants.DISPLAY_MODE)){
            String protocolNumber = (String)
            session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId());
            String menuCode = (String) hmMenuList.get("menuCode");
            HashMap hmProtoAmendRenewModules = (HashMap)
            session.getAttribute("amendRenewModules"+session.getId());
            hmProtoAmendRenewModules = (hmProtoAmendRenewModules == null) ? new HashMap() :
                hmProtoAmendRenewModules;
            if(hmProtoAmendRenewModules.containsKey(menuCode) &&
                    hmProtoAmendRenewModules.get(menuCode).toString().equals(protocolNumber)){
                session.setAttribute("amendRenewPageMode"+session.getId(), CoeusLiteConstants.MODIFY_MODE);
            }
        }
    }
    /**
     * Added for coeus4.3 enhancements
     * To get the IacucUtils new instance
     * @return IacucUtils
     */
    public IacucUtils getIacucUtils() {
        return new IacucUtils();
    }

    /**
     * Added for coeus4.3 enhancements
     * To set the ProtocolUtils instance
     * 
     * @param iacucUtils
     */
    public void setIacucUtils(IacucUtils iacucUtils) {
        this.iacucUtils = iacucUtils;
    }
    
    /**
     * Added for coeus4.3 enhancements
     * To set the menu details list for Amendments and Renewwal.
     * @param protocolNumber 
     * @param sequenceNumber 
     * @param request 
     * @throws java.lang.Exception 
     */
    protected void setMenuForAmendRenew(String protocolNumber,
            String sequenceNumber, HttpServletRequest request) throws Exception{
        Vector menuItemsVector  = null;
        //Added for Case#3079- Change expire to no longer be a terminal status- Start
        UserInfoBean userInfoBean = (UserInfoBean)request.getSession().getAttribute(USER+request.getSession().getId());        
        ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean();
        UserMaintDataTxnBean txnData = new UserMaintDataTxnBean(); 
        //Added for Case#3079- Change expire to no longer be a terminal status- End
        //Code added for Case#2785 - Protocol Routing - starts
        HashMap hmMap = getApprovalRights(request, ModuleConstants.IACUC_MODULE_CODE+"", protocolNumber, sequenceNumber);
        String avViewRouting =(String) hmMap.get(VIEW_ROUTING);
        //Code added for Case#2785 - Protocol Routing - ends
        HashMap hmData = new HashMap();
        //                ReadProtocolDetails readProtocolDetails = new ReadProtocolDetails();
        //                menuItemsVector = readProtocolDetails.readXMLDataForMenu("/edu/mit/coeuslite/irb/xml/ProtocolMenu.xml");
        ReadXMLData readXMLData = new ReadXMLData();
        menuItemsVector = readXMLData.readXMLDataForMenu("/edu/mit/coeuslite/iacuc/xml/IacucMenu.xml");
        hmData.put(ModuleDataBean.class , new Integer(ModuleConstants.IACUC_MODULE_CODE));
        // COEUSDEV-86: Questionnaire for a Submission - Start
//        hmData.put(SubModuleDataBean.class , new Integer(0));
        int subModuleItemCode = 0;
        if(protocolNumber.length() > 10 && isIacucProtoAmendmentRenewal(protocolNumber.charAt(10))){
            subModuleItemCode = CoeusLiteConstants.IRB_SUB_MODULE_CODE_FOR_AMENDMENT_RENEWAL;
        } else {
            subModuleItemCode = 0;
        }
        hmData.put(SubModuleDataBean.class , new Integer(subModuleItemCode));
        // COEUSDEV-86: Questionnaire for a Submission - End
        hmData.put("link" , "/getQuestionnaire.do");
        hmData.put("actionFrom" ,"IACUC_PROTOCOL");
        //Modified for Case#3941 -Questionnaire answers missing after deleting the module - Start        
            hmData.put("moduleItemKey",protocolNumber);
            hmData.put("moduleSubItemKey",sequenceNumber);
        //Modified for Case#3941 -Questionnaire answers missing after deleting the module - End
        menuItemsVector = getQuestionnaireMenuData(menuItemsVector,request ,hmData);
        //commented for making "New Amendment" "New Renewal" menu visible -start
        //session.setAttribute("menuItemsVector", menuItemsVector);
        //commented for making "New Amendment" "New Renewal" menu visible -end
        //Added for "New Amendment" "New Renewal" menus visible -start
        
        //Added for Case#3079- Change expire to no longer be a terminal status- Start 
        String leadUnitNum = "";
        if (!protocolNumber.equalsIgnoreCase("")) {
            Vector vecUnits = protocolDataTxnBean.getProtocolUnitsMaxSeqNumber(protocolNumber) ;
            for (int rowIdx=0 ; rowIdx< vecUnits.size() ; rowIdx++) {
                HashMap hashUnit = (HashMap)vecUnits.get(rowIdx) ;
                //Checks LEAD_UNIT_FLAG is Y, then the unitnumber is LeadUnitNumber
                if(!hashUnit.isEmpty() && hashUnit.get("LEAD_UNIT_FLAG").toString().equalsIgnoreCase("Y")){
                    leadUnitNum = hashUnit.get("UNIT_NUMBER").toString() ;
                }
            }// end for
        } // end if

         //Added this code for doing PI checking first, before doing Amendment right checking
         boolean isAuthorisedCreateRenew = false;
         boolean isAuthorisedCreateAmend = false;
         boolean isAuthorisedCreateAmendRenew = false;
         boolean isAuthorisedCreateContnReview = false;
         boolean isAuthorisedCreateContnReviewAmend = false;
         ProtocolAuthorizationBean authorizationBean = new ProtocolAuthorizationBean();
         isAuthorisedCreateAmend = authorizationBean.hasCreateAmenRenewRights(userInfoBean.getUserId(),protocolNumber);
         isAuthorisedCreateRenew = isAuthorisedCreateAmend;
         isAuthorisedCreateAmendRenew = isAuthorisedCreateRenew;
         isAuthorisedCreateContnReview = isAuthorisedCreateAmendRenew;
         isAuthorisedCreateContnReviewAmend = isAuthorisedCreateContnReview;
            
        //boolean isAuthorisedCreateRenew = false;
         if(!isAuthorisedCreateRenew) {
            isAuthorisedCreateRenew = txnData.getUserHasIACUCProtocolRight(userInfoBean.getUserId(), CoeusLiteConstants.ADD_RENEWAL, protocolNumber);
            //If no rights check at Unit level right
            if(!isAuthorisedCreateRenew){
                isAuthorisedCreateRenew = txnData.getUserHasRight(userInfoBean.getUserId(), CoeusLiteConstants.CREATE_ANY_IACUC_RENEWAL, leadUnitNum);
            }
         }
        //boolean isAuthorisedCreateAmend = false;
        // 4216: New Amendment or New Renewal sometimes isn't available in Lite - Start
//        isAuthorisedCreateRenew = txnData.getUserHasIACUCProtocolRight(userInfoBean.getUserId(), ADD_AMENDMENT, protocolNumber);
         if(!isAuthorisedCreateAmend) {
            isAuthorisedCreateAmend = txnData.getUserHasIACUCProtocolRight(userInfoBean.getUserId(), CoeusLiteConstants.ADD_AMENDMENT, protocolNumber);
            // 4216: New Amendment or New Renewal sometimes isn't available in Lite - End
            //If no rights check at Unit level right
            if(!isAuthorisedCreateAmend){
                isAuthorisedCreateAmend = txnData.getUserHasRight(userInfoBean.getUserId(), CoeusLiteConstants.CREATE_ANY_IACUC_AMENDMENT, leadUnitNum);
            }
         }
        /*COEUSQA-1724-Condition Added for new new Amendment Type - Start*/
           // boolean isAuthorisedCreateAmendRenew = false;
         if(!isAuthorisedCreateAmendRenew) {
            isAuthorisedCreateAmendRenew = txnData.getUserHasIACUCProtocolRight(userInfoBean.getUserId(), CoeusLiteConstants.CREATE_IACUC_RENEWAL_AMENDMENT, protocolNumber);
            // Protocol level rights check
            if(!isAuthorisedCreateAmendRenew){
                isAuthorisedCreateAmendRenew = txnData.getUserHasRight(userInfoBean.getUserId(), CoeusLiteConstants.CREATE_ANY_IACUC_REN_AMEN, leadUnitNum);
            //Unit level right       
            }  
         }
           // boolean isAuthorisedCreateContnReview = false;
         if(!isAuthorisedCreateContnReview) {
            isAuthorisedCreateContnReview = txnData.getUserHasIACUCProtocolRight(userInfoBean.getUserId(), CoeusLiteConstants.CREATE_IACUC_CONT_REVIEW, protocolNumber);
            // Protocol level rights check
            if(!isAuthorisedCreateContnReview){
                isAuthorisedCreateContnReview = txnData.getUserHasRight(userInfoBean.getUserId(), CoeusLiteConstants.CREATE_ANY_IACUC_CONT_REVIEW, leadUnitNum);
            //Unit level right
            }
         }
            //boolean isAuthorisedCreateContnReviewAmend = false;
         if(!isAuthorisedCreateContnReviewAmend) {
            isAuthorisedCreateContnReviewAmend = txnData.getUserHasIACUCProtocolRight(userInfoBean.getUserId(), CoeusLiteConstants.CREATE_IACUC_CONT_REV_AM, protocolNumber);
            // Protocol level rights check
            if(!isAuthorisedCreateContnReviewAmend){
                isAuthorisedCreateContnReviewAmend = txnData.getUserHasRight(userInfoBean.getUserId(), CoeusLiteConstants.CREATE_ANY_IACUC_CONT_REV_AM, leadUnitNum);
           //Unit level right
            }
         }
       /*COEUSQA-1724-Condition Added for new new Amendment Type - End*/
        //Added for Case#4369 -  PI to create amendment/renewal  - start
        //Checks if user doesn't have rights to create Amendment/Renewal and he is the PI for protocol
        // New Amendment and New Renewal link is enabled
        /*COEUSQA-1724-Condition Added for new new Amendment Type - Start*/

         //Commented this code for doing PI checking first, before doing Amendment right checking
//        if(!isAuthorisedCreateRenew 
//           || !isAuthorisedCreateAmend 
//           || !isAuthorisedCreateAmendRenew 
//           || !isAuthorisedCreateContnReview 
//           || !isAuthorisedCreateContnReviewAmend){
//            ProtocolAuthorizationBean protocolAuthorizationBean = new ProtocolAuthorizationBean();
//            isAuthorisedCreateAmend = protocolAuthorizationBean.hasCreateAmenRenewRights(userInfoBean.getUserId(),protocolNumber);
//            isAuthorisedCreateRenew = isAuthorisedCreateAmend;
//            isAuthorisedCreateAmendRenew = isAuthorisedCreateRenew;
//            isAuthorisedCreateContnReview = isAuthorisedCreateAmendRenew;
//            isAuthorisedCreateContnReviewAmend = isAuthorisedCreateContnReview;
//        }
        /*COEUSQA-1724-Condition Added for new new Amendment Type - End*/
        //Case#4369 - End
        //Added for Case#3079- Change expire to no longer be a terminal status- End
        //Added for Case# 3018 -create ability to delete pending studies - Start        
        boolean isAuthorisedToDelete = false;
        isAuthorisedToDelete = txnData.getUserHasIACUCProtocolRight(userInfoBean.getUserId(), DELETE_IACUC_PROTOCOL, protocolNumber);
        //If no rights check at Unit level right
        if(!isAuthorisedToDelete){
            isAuthorisedToDelete = txnData.getUserHasRight(userInfoBean.getUserId(), DELETE_ANY_IACUC_PROTOCOL, leadUnitNum);
        }
        int canDelete = checkCanDeleteProtocol(protocolNumber,request);
        //Added for Case# 3018 -create ability to delete pending studies - End        
        HashMap hmStatus =  new HashMap();
        hmStatus.put(PROTOCOL_NUMBER,protocolNumber);
        int protoStatusCode = 0;
        String protocolType = null;
        if(protocolNumber.length()>10){
        protocolType = protocolNumber.substring(10,11).toString();
        }else{
            protocolType = GENERAL_PROTOCOL;
        }
        //Vector vecStatus = new Vector();
        Vector vecProtocolHeader = (Vector)getProtocolHeader(protocolNumber, request);
        if(vecProtocolHeader != null && vecProtocolHeader.size() >0){
            ProtocolHeaderDetailsBean bean = (ProtocolHeaderDetailsBean)vecProtocolHeader.elementAt(0);
            protoStatusCode = bean.getProtocolStatusCode();
        }
        // 3282: Reviewer View of Protocol materials - Start
        boolean isUserProtocolReviewer = checkReviewerRight(request, userInfoBean);
        ProtocolAuthorizationBean protocolAuthorizationBean = new ProtocolAuthorizationBean();
//        boolean isUserIrbAdmin = protocolAuthorizationBean.hasIRBAdminRights(userInfoBean.getUserId(), leadUnitNum);
        //COEUSDEV-161: Changes to IRB reviewer comments functionality - Access peermissions - Start
//        if(isUserProtocolReviewer){
//            isAuthorisedCreateRenew  = false;
//            isAuthorisedCreateAmend = false;
//            isAuthorisedToDelete  = false;
//            
//        }
//        // 3282: Reviewer View of Protocol materials - End
        // COEUSDEV-161: Changes to IRB reviewer comments functionality - Access peermissions- End
        //Code Commented for Code refactoring.
//        if(protoStatusCode == 200 || protoStatusCode == 201 || protoStatusCode == 202 ||
//                protoStatusCode == 203 || protoStatusCode == 300 || protoStatusCode == 301||
//                protoStatusCode == 302 ||  protoStatusCode == 308 ||
//                //Added for Case#3079- Change expire to no longer be a terminal status- Start
//                    protoStatusCode == 305||
//                //Added for Case#3079- Change expire to no longer be a terminal status - End
//                //Modified for Case# 3018 -create ability to delete pending studies - Start
//                protoStatusCode == 100||protoStatusCode == 105 || protoStatusCode == 106 || protoStatusCode == 101){
                    //Modified for Case# 3018 -create ability to delete pending studies - End
            if(checkProtocolStatusCode(protoStatusCode)){
                
            for(int ind = 0 ; ind < menuItemsVector.size(); ind++){
                MenuBean menuBean = (MenuBean)menuItemsVector.get(ind);
                String menuCode = menuBean.getMenuId();
                //Commented and Modified for case# 3018 -create ability to delete pending studies - Start
                //Modified and Added for Case#3079- Change expire to no longer be a terminal status- Start
//                if(menuCode.equals("021") || menuCode.equals("022")){
//                    menuBean.setVisible(true);
//                }
                //if the status of the protocol is either Pending in Progress,Amendment in progress or Renewal in Progress
                // then enable the 'Delete Protocol' link
                if((protoStatusCode == PENDING_IN_PROGRESS) && (canDelete == 0 || canDelete == 1)){
                    /*if(menuCode.equals(CoeusliteMenuItems.PROTOCOL_DELETE_MENU)&&isAuthorisedToDelete){
                        menuBean.setVisible(true);
                    }*/
                    //Commented and Modified for Case# 3781_Rename Delete Protocol- Start
                    if(CoeusliteMenuItems.IACUC_PROTOCOL_DELETE_MENU.equals(menuCode) 
                            && isAuthorisedToDelete && protoStatusCode == PENDING_IN_PROGRESS
                            && GENERAL_PROTOCOL.equals(protocolType)){
                        menuBean.setVisible(true);
                    }else if(CoeusliteMenuItems.IACUC_PROTOCOL_AMENDMENT_DELETE_MENU.equals(menuCode)                                   
                                && isAuthorisedToDelete && protoStatusCode == PENDING_IN_PROGRESS  
                                && Character.toString(CoeusConstants.IACUC_AMENDMENT).equals(protocolType)){
                        menuBean.setVisible(true);
                    }else if(CoeusliteMenuItems.IACUC_PROTOCOL_RENEWAL_DELETE_MENU.equals(menuCode)                                   
                                && isAuthorisedToDelete && protoStatusCode == PENDING_IN_PROGRESS  
                                && Character.toString(CoeusConstants.IACUC_RENEWAL).equals(protocolType)){
                        menuBean.setVisible(true);
                    }else if(CoeusliteMenuItems.IACUC_PROTOCOL_RENEWAL_AMENDMENT_DELETE_MENU.equals(menuCode)                                  
                                && isAuthorisedToDelete && protoStatusCode == PENDING_IN_PROGRESS  
                                && Character.toString(CoeusConstants.IACUC_RENEWAL_WITH_AMENDMENT).equals(protocolType)){
                        menuBean.setVisible(true);
                    }
                    /*COEUSQA-1724-Condition Added for new new Amendment Type - Start*/
                    else if(CoeusliteMenuItems.IACUC_PROTOCOL_CONTN_CONTG_REVIEW_DELETE_MENU.equals(menuCode)                                  
                                && isAuthorisedToDelete && protoStatusCode == PENDING_IN_PROGRESS 
                                && Character.toString(CoeusConstants.IACUC_CONTINUATION_CONTINUING_REVIEW).equals(protocolType)){
                        menuBean.setVisible(true);
                    }else if(CoeusliteMenuItems.IACUC_PROTOCOL_CONTN_CONTG_REVIEW_AMEND_DELETE_MENU.equals(menuCode)                                  
                                && isAuthorisedToDelete && protoStatusCode == PENDING_IN_PROGRESS 
                                && Character.toString(CoeusConstants.IACUC_CONTI_CONTINUING_REVIEW_WITH_AMEND).equals(protocolType)){
                        menuBean.setVisible(true);
                    }
                    /*COEUSQA-1724-Condition Added for new new Amendment Type - End*/
                    else if(CoeusliteMenuItems.IACUC_PROTOCOL_AMENDMENT_SUMMARY_MENU.equals(menuCode)
                                && protoStatusCode == PENDING_IN_PROGRESS 
                                && Character.toString(CoeusConstants.IACUC_AMENDMENT).equals(protocolType)){
                        menuBean.setVisible(true);
                    }else if(CoeusliteMenuItems.IACUC_PROTOCOL_RENEWAL_SUMMARY_MENU.equals(menuCode) 
                                && protoStatusCode == PENDING_IN_PROGRESS 
                                && Character.toString(CoeusConstants.IACUC_RENEWAL).equals(protocolType)){
                        menuBean.setVisible(true);
                    }else if(CoeusliteMenuItems.IACUC_PROTOCOL_RENEWAL_AMENDMENT_SUMMARY_MENU.equals(menuCode) 
                                && protoStatusCode == PENDING_IN_PROGRESS 
                                && Character.toString(CoeusConstants.IACUC_RENEWAL_WITH_AMENDMENT).equals(protocolType)){
                        menuBean.setVisible(true);
                    }
                    /*COEUSQA-1724-Condition Added for new new Amendment Type - Start*/
                    else if(CoeusliteMenuItems.IACUC_PROTOCOL_CONTN_CONTG_REVIEW_SUMMARY_MENU.equals(menuCode) 
                                && protoStatusCode == PENDING_IN_PROGRESS 
                                && Character.toString(CoeusConstants.IACUC_CONTINUATION_CONTINUING_REVIEW).equals(protocolType)){
                        menuBean.setVisible(true);
                    }else if(CoeusliteMenuItems.IACUC_PROTOCOL_CONTN_CONTG_REVIEW_AMEND_SUMMARY_MENU.equals(menuCode) 
                                && protoStatusCode == PENDING_IN_PROGRESS 
                                && Character.toString(CoeusConstants.IACUC_CONTI_CONTINUING_REVIEW_WITH_AMEND).equals(protocolType)){
                        menuBean.setVisible(true);
                    }  
                    /*COEUSQA-1724-Condition Added for new new Amendment Type - End*/
                    
                    
                    //Commented and Modified for Case# 3781_Rename Delete Protocol- End
                }else if(protoStatusCode == 200 || protoStatusCode == 201 || protoStatusCode == 304 ||
                        protoStatusCode == 305 || protoStatusCode == 306 || protoStatusCode == 308){
                    
//                    if((menuCode.equals("021") && isAuthorisedCreateAmend) || (menuCode.equals("022")&&isAuthorisedCreateRenew)){
//                        menuBean.setVisible(true);
//                    }
                    // 3828: Create a menu item on the left hand side of IRB Lite view that states New Renewal-Amendment - Start
                    // COEUSQA-1724_IACUC Continuation Continuing -Start
                    if((CoeusliteMenuItems.IACUC_PROTOCOL_NEW_AMENDMENT_MENU.equals(menuCode) && isAuthorisedCreateAmend) 
                    || (CoeusliteMenuItems.IACUC_PROTOCOL_NEW_RENEWAL_MENU.equals(menuCode) && isAuthorisedCreateRenew)
                    || (CoeusliteMenuItems.IACUC_PROTOCOL_NEW_RENEW_AMEND_MENU.equals(menuCode) && isAuthorisedCreateAmendRenew)
                    || (CoeusliteMenuItems.IACUC_PROTOCOL_NEW_CONTN_CONTG_REVIEW_MENU.equals(menuCode) && isAuthorisedCreateContnReview)
                    || (CoeusliteMenuItems.IACUC_PROTOCOL_NEW_CONTN_CONTG_REVIEW_AMEND_MENU.equals(menuCode) && isAuthorisedCreateContnReviewAmend)){
                        menuBean.setVisible(true);
                    } 
                    // COEUSQA-1724_IACUC Continuation Continuing -End
                    // 3828: Create a menu item on the left hand side of IRB Lite view that states New Renewal-Amendment - End
                // if the status is Expired and the logged in user has rights to create renewal, then enable only Renewal link    
                }
                /*else if(protoStatusCode == 305 && isAuthorisedCreateRenew){
                    // 3828: Create a menu item on the left hand side of IRB Lite view that states New Renewal-Amendment - Start
                    // Display the “New Renewal/Amendment"(Menu id 29).
//                    if(menuCode.equals("022")){
//                        menuBean.setVisible(true);
//                    }
                    if(menuCode.equals("AC022") || menuCode.equals("AC029")){
                        menuBean.setVisible(true);
                    }                    
                    // 3828: Create a menu item on the left hand side of IRB Lite view that states New Renewal-Amendment - End
                }*/
               //Modified and Added for Case#3079- Change expire to no longer be a terminal status- End 
                // 4395: Can't view rejection comments in Lite - Start
//                //Code added for Case#2785 - Protocol Routing - starts
//                if(menuCode.equals(CoeusliteMenuItems.APPROVAL_ROUTING_PROTOCOL)
//                    && (avViewRouting!= null && avViewRouting.equals(CoeusliteMenuItems.ROUTING_STATUS))) {
//                    menuBean.setVisible(false);
//                }
//                //Code added for Case#2785 - Protocol Routing - ends
                // If the avViewRouting(Number of Entries for the Protocol in Routing Table) is not equal to Zero, Display the
                // 'Approval Routing' Menu Item.
                if(CoeusliteMenuItems.IACUC_PROTOCOL_APPROVAL_ROUTING_MENU.equals(menuCode)){
                    if (! CoeusliteMenuItems.ROUTING_STATUS.equals(avViewRouting)) {
                        menuBean.setVisible(true);
                    }
                }
               // 4395: Can't view rejection comments in Lite - End
                
                // 4361: Rights checking issue in protocol in Lite - Start
                // Do Proper Right Checking before Displaying Copy protocol Menu
                if(CoeusliteMenuItems.IACUC_COPY_PROTOCOL_MENU.equals(menuCode)){
                    boolean canCopyProtocol = false;
                    String loggedInUser = userInfoBean.getUserId();
                    if (txnData.getUserHasIACUCProtocolRight(loggedInUser, MODIFY_IACUC_PROTOCOL, protocolNumber)
                    ||  txnData.getUserHasIACUCProtocolRight(loggedInUser, VIEW_IACUC_PROTOCOL, protocolNumber)) {
                        canCopyProtocol = txnData.getUserHasRightInAnyUnit(userInfoBean.getUserId(), CREATE_IACUC_PROTOCOL);
                    } else if (txnData.getUserHasRight(loggedInUser, MODIFY_ANY_IACUC_PROTOCOL, leadUnitNum)
                    || txnData.getUserHasRight(loggedInUser, VIEW_ANY_IACUC_PROTOCOL, leadUnitNum)) {
                        canCopyProtocol = txnData.getUserHasRightInAnyUnit(loggedInUser, CREATE_IACUC_PROTOCOL);
                    }
                    if(canCopyProtocol){
                        menuBean.setVisible(true);
                    }else{
                        menuBean.setVisible(false);
                    }    
                }
                // 4361: Rights checking issue in protocol in Lite - End
                
              //Commented and Modified for case# 3018 -create ability to delete pending studies - Start
              // COEUSDEV-161: Changes to IRB reviewer comments functionality - Access peermissions - Start  
                // 3282: Reviewer View of Protocol materials - Start
//                if(isUserProtocolReviewer) {
//                    
//                    //Added for the case#3282/3282 reviewer views.comments-start
//                    //if(CoeusliteMenuItems.REVIEW_COMMENTS.equals(menuCode)){
//                    if(CoeusliteMenuItems.REVIEW_COMMENTS.equals(menuCode)){
//                        if(protoStatusCode == 100){
//                            menuBean.setVisible(false);
//                        }else{
//                            menuBean.setVisible(true);
//                        }
//                    }
//                     //Added for the case#3282/3282 reviewer views.comments-end
//                    
//                }
//                else if(CoeusliteMenuItems.REVIEW_COMMENTS.equals(menuCode) && protoStatusCode == 100){
//                    menuBean.setVisible(false);
//                }
               
                
                // 3282: Reviewer View of Protocol materials - End  
            }          
        // 3282: Reviewer View of Protocol materials - Start
        }
//        else if(protoStatusCode == 304){
//            // Protocol Status Code is 'Withdrawn'
//            for(int index = 0 ; index < menuItemsVector.size(); index++){
//                MenuBean menuBean = (MenuBean)menuItemsVector.get(index);
//                String menuCode = menuBean.getMenuId();
//                if(isUserProtocolReviewer) {
//                    if(CoeusliteMenuItems.COPY_PROTOCOL_MNEU.equals(menuCode) || CoeusliteMenuItems.PROTOCOL_ACTION_MENU.equals(menuCode) ){
//                        menuBean.setVisible(false);
//                    }
//                    
//                    if(CoeusliteMenuItems.REVIEW_COMMENTS.equals(menuCode)){
//                            menuBean.setVisible(true);
//                      
//                    }
//                }
//            }
//        }
//        // 3282: Reviewer View of Protocol materials - End
        //Modified for COEUSDEV-237 : Investigator cannot see review comments - Start
        //If user is a investigator 'REVIEW COMMENTS' is enabled
//        if(isUserProtocolReviewer || isUserIrbAdmin)
//        boolean isUserProtoInvestigator = false;
        int seqNumber = -1;
        if(sequenceNumber != null){
            seqNumber = Integer.parseInt(sequenceNumber);
        }
//        boolean canViewProtocol = false;
//        String loggedInUser = userInfoBean.getUserId();
        //Review comments menu will enabled when Logged-in user has basic view right on the protocol itself
//        isUserProtoInvestigator = protocolDataTxnBean.isUserProtocolInvestigator(protocolNumber,seqNumber,loggedInUser);
//        if(isUserProtocolReviewer || isUserIrbAdmin || isUserProtoInvestigator) {//COEUSDEV-237: END
            for(int index = 0 ; index < menuItemsVector.size(); index++){
                MenuBean menuBean = (MenuBean)menuItemsVector.get(index);
                String menuCode = menuBean.getMenuId();
                if(CoeusliteMenuItems.IACUC_PROTOCOL_REVIEW_COMMENTS_MENU.equals(menuCode)){
                    menuBean.setVisible(true);
                    
                }
            }
//        }
        // COEUSDEV-161: Changes to IRB reviewer comments functionality - Access peermissions - End
        request.getSession().setAttribute(MENU_ITEMS, menuItemsVector);
    }
    
    //Added for Case# 3018 -create ability to delete pending studies - Start
    /**
     * This method is used to check if a protocol can be deleted or not
     * if canDelete = 0, delete protocol(i.e protocol is not linked to another module
     * and is in either of the following 3 status' Pending in Progress,Renewal in Progress, Amendment in Progress)
     * @param request
     * @param protocolNumber
     * @return int canDelete
     * @throws Exception
     */
    protected int checkCanDeleteProtocol(String protocolNumber,HttpServletRequest request) throws Exception{
        int canDelete = -1;
        WebTxnBean webTxnBean = new WebTxnBean();
        HashMap hmProtocolNumber = new HashMap();
        hmProtocolNumber.put(PROTOCOL_NUMBER, protocolNumber);
        Hashtable htCanDelete =(Hashtable) webTxnBean.getResults(request, "canDeleteIacuc", hmProtocolNumber);
        canDelete = Integer.parseInt(((HashMap)htCanDelete.get("canDeleteIacuc")).get("ll_Ret").toString());
        return canDelete;
    }
    //Added for Case# 3018 -create ability to delete pending studies - End      
    // 3282: Reviewer View of Protocol materials =- Start
    /**
     * Method to check if the logged in user is a Protocol reviewer. Method uses 
     * FN_IS_USER_PROTOCOL_REVIEWER to get the information.
     *
     */
    //Modified for COEUSDEV-303 :  Review View menu items are not enabled if the user has reviewer role - Start
//    protected boolean isUserProtocolReviewer(HttpServletRequest request,String userId, String unitNumber) throws Exception{
    protected boolean isUserProtocolReviewer(HttpServletRequest request,String userId) throws Exception{//COEUSDEV-303
        int result;
        WebTxnBean webTxnBean = new WebTxnBean();
        HashMap hmPersonDetails = new HashMap();
        hmPersonDetails.put("personId", userId);
        //Commented for COEUSDEV-303 :  Review View menu items are not enabled if the user has reviewer role - Start
        //'FN_IS_USER_PROTOCOL_REVIEWER' uses only one parameter
//        hmPersonDetails.put("unitNumber", unitNumber);
        //COEUSDEV-303 : End
        Hashtable htIsReviewer =(Hashtable) webTxnBean.getResults(request, "isUserIacucReviewer", hmPersonDetails);
        result = Integer.parseInt(((HashMap)htIsReviewer.get("isUserIacucReviewer")).get("isReviewer").toString());
        if(result == 1){
            return true;
        }
        return false;
    }
    /**
     * Method first checks in the Session,if the logged in user is a Protocol Reviewer or not. 
     * If there is no information in the session, method will check if the user is a Protocol Reviewer
     * or not and saves the information in session.
     *
     */
    protected boolean checkReviewerRight(HttpServletRequest request, UserInfoBean userInfoBean) throws Exception{
        HttpSession session = request.getSession();
        String hasReviewerRight = (String) session.getAttribute(CoeusLiteConstants.USER_IS_IACUC_REVIEWER+session.getId());
        boolean isUserProtocolReviewer = false;
        
        if(CoeusLiteConstants.YES.equalsIgnoreCase(hasReviewerRight)){
            isUserProtocolReviewer = true;
        } else if(CoeusLiteConstants.NO.equalsIgnoreCase(hasReviewerRight)){
            isUserProtocolReviewer = false;
        } else {
            //Modified for COEUSDEV-303 :  Review View menu items are not enabled if the user has reviewer role - Start
//            isUserProtocolReviewer = isUserProtocolReviewer(request, userInfoBean.getUserId(), userInfoBean.getUnitNumber());
            isUserProtocolReviewer = isUserProtocolReviewer(request, userInfoBean.getUserId());
            //COEUSDEV-303 : End
            if(isUserProtocolReviewer){
                session.setAttribute(CoeusLiteConstants.USER_IS_IACUC_REVIEWER+session.getId(),CoeusLiteConstants.YES);
            }else{
                session.setAttribute(CoeusLiteConstants.USER_IS_IACUC_REVIEWER+session.getId(), CoeusLiteConstants.NO);
            }
        }
        return isUserProtocolReviewer;
    }
    // 3282: Reviewer View of Protocol materials - End
    
    // 4361: Rights checking issue in protocol in Lite - Start
    /**
     * Checks if the User can view Protocol
     * @param HttpServletRequest request
     * @param String protocolNumber
     * @return boolean canViewProtocol
     * @throws Exception
     */
    protected boolean checkCanViewProtocol(HttpServletRequest request, String protocolNumber) throws Exception{
        boolean canViewProtocol = false;
        HttpSession session = request.getSession();
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute(USER+session.getId());
        String loggedinUser = userInfoBean.getUserId();
        UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
        ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean();
        // Check if user has Modify Protocol Right at the Protocol Level
        canViewProtocol = userMaintDataTxnBean.getUserHasIACUCProtocolRight(loggedinUser, MODIFY_IACUC_PROTOCOL, protocolNumber);
        if(!canViewProtocol){
            // Check if user has View Protocol Right at the Protocol Level
            canViewProtocol = userMaintDataTxnBean.getUserHasIACUCProtocolRight(loggedinUser, VIEW_IACUC_PROTOCOL, protocolNumber);
            if(!canViewProtocol){
                // Get all the Units of the protocol
                Vector vecUnits = protocolDataTxnBean.getProtocolUnitsMaxSeqNumber(protocolNumber) ;
                for (int rowIdx=0 ; rowIdx< vecUnits.size() ; rowIdx++) {
                    HashMap hashUnit = (HashMap)vecUnits.get(rowIdx) ;
                    String tempUnitNumber = hashUnit.get("UNIT_NUMBER").toString() ;
                    // Check user has Modify Protocol Right at the unit level
                    canViewProtocol = userMaintDataTxnBean.getUserHasRight(loggedinUser, MODIFY_ANY_IACUC_PROTOCOL, tempUnitNumber);
                    if(!canViewProtocol){
                        // Check user has View Protocol at the unit level
                        canViewProtocol = userMaintDataTxnBean.getUserHasRight(loggedinUser, VIEW_ANY_IACUC_PROTOCOL, tempUnitNumber);
                    }
                    if (canViewProtocol)
                        break ;
                }
                if(!canViewProtocol) {
                    // Get unit numbers of Organizations (Locations) associated with protocol (Except the Lead Unit)
                    Vector vecUnitsforLoc = protocolDataTxnBean.getUnitsForProtocolLocations(protocolNumber);
                    if(vecUnitsforLoc != null && vecUnitsforLoc.size() > 0) {
                        for(int index = 0; index < vecUnitsforLoc.size(); index++) {
                            String unitNumber = (String)vecUnitsforLoc.get(index);
                            // Check user has Modify Protocol Right
                            canViewProtocol = userMaintDataTxnBean.getUserHasRight(loggedinUser, MODIFY_IACUC_PROTOCOL, unitNumber);
                            if(!canViewProtocol){
                                // Check user has View Protocol Right
                                canViewProtocol = userMaintDataTxnBean.getUserHasRight(loggedinUser, VIEW_IACUC_PROTOCOL, unitNumber);
                                if(!canViewProtocol) {
                                    // Check user has Modify Protocol Right at the unit level
                                    canViewProtocol = userMaintDataTxnBean.getUserHasRight(loggedinUser,MODIFY_ANY_IACUC_PROTOCOL , unitNumber);
                                    if(!canViewProtocol){
                                        // Check user has View Protocol Right at the unit level
                                        canViewProtocol = userMaintDataTxnBean.getUserHasRight(loggedinUser, VIEW_ANY_IACUC_PROTOCOL, unitNumber);
                                    }
                                }
                            }
                            
                            if(canViewProtocol) {
                                break;
                            }
                            
                        }
                    }
                }
                if(!canViewProtocol) {
                    // Check if the logged in user is the PI of the protocol
                    boolean isUserPi =  checkIsUserPI(request, protocolNumber, loggedinUser);
                    if(isUserPi){
                        canViewProtocol = true;
                    }
                }
                //Added for COEUSQA-2314 : IRB Admin should have ability to assign committee based on lead unit of the protocol - Start
                if(!canViewProtocol){
                    ProtocolInfoBean protocolInfoBean = protocolDataTxnBean.getProtocolMaintenanceDetails(protocolNumber);
                    if(protocolInfoBean != null && !checkStatusInProgress(protocolInfoBean.getProtocolStatusCode())){
                        ProtocolSubmissionTxnBean protocolSubmissionTxnBean = new ProtocolSubmissionTxnBean();
                        ProtocolSubmissionInfoBean submissionInformation =
                                protocolSubmissionTxnBean.getProtocolSubmissionDetails(protocolNumber);
                        String departmentNumber = "";
                        if(submissionInformation != null && submissionInformation.getCommitteeId() != null &&
                                !submissionInformation.getCommitteeId().equals(EMPTY_STRING)){
                            CommitteeTxnBean committeeTxnBean  = new CommitteeTxnBean(loggedinUser);
                            edu.mit.coeus.irb.bean.CommitteeMaintenanceFormBean beanHomeUnit =
                                    committeeTxnBean.getCommitteeDetails(submissionInformation.getCommitteeId()) ;
                            departmentNumber = beanHomeUnit.getUnitNumber() ;
                        } else  if (submissionInformation.getScheduleId()!= null
                                && !submissionInformation.getScheduleId().equals(EMPTY_STRING)) {
                            ScheduleTxnBean scheduleTxnBeanUnit = new ScheduleTxnBean(loggedinUser);
                            ScheduleDetailsBean beanHomeUnit
                                    = scheduleTxnBeanUnit.getScheduleDetails(submissionInformation.getScheduleId()) ;
                            departmentNumber = beanHomeUnit.getHomeUnitNumber() ;
                        } else {
                            departmentNumber = protocolDataTxnBean.getLeadUnitForProtocol(
                                    submissionInformation.getProtocolNumber(), submissionInformation.getSequenceNumber());
                        }
                        canViewProtocol = userMaintDataTxnBean.getUserHasRight(loggedinUser, MAINTAIN_PROTOCOL_SUBMISSIONS, departmentNumber);
                    }
                }
                //COEUSQA-2314 : End
            }
        }
        return canViewProtocol;
    }
    
    /**
     * Checks if the User is the PI for a Protocol
     * @param HttpServletRequest request
     * @param String protocolNumber
     * @param String loggedinUser
     * @return boolean isUSerPi
     * @throws Exception
     */
    private boolean checkIsUserPI(HttpServletRequest request, String protocolNumber, String loggedinUser) throws Exception {
        boolean isUserPi =  false;
        int result = 0;
        WebTxnBean webTxnBean = new WebTxnBean();
        HashMap hmProtocolDetails = new HashMap();
        hmProtocolDetails.put("moduleItemKey", protocolNumber);
        hmProtocolDetails.put("userId", loggedinUser);
        hmProtocolDetails.put("moduleCode", ModuleConstants.IACUC_MODULE_CODE+"");
        Hashtable htIsUserPi =(Hashtable) webTxnBean.getResults(request, "isIacucUserPI", hmProtocolDetails);
        result  = Integer.parseInt(((HashMap)htIsUserPi.get("isIacucUserPI")).get("ll_count").toString());
        if(result > 0){
            isUserPi = true;
        }
        return isUserPi;
    }
    // 4361: Rights checking issue in protocol in Lite - End
    
     /**
     * This method locks a particular protcol
     * @throws Exception
     */
    protected void lockProtocol(String protocolNumber, HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute(USER+session.getId());
        LockBean lockBean = getLockingBean(userInfoBean, protocolNumber,request);
        lockBean.setMode("M");
        lockModule(lockBean,request);
        session.setAttribute(CoeusLiteConstants.RECORD_LOCKED+session.getId(),new Boolean(true));
    }
    
    //Added for COEUSDEV-237 : Investigator cannot see review comments - Start
   /*
    * Method to get reviewers for the protocol
    * @param request
    * @param protocolNumber
    * @param sequenceNumber
    * @param submissionNumber
    * @return vecReviewers
    * @throws Exception
    */
    protected Vector getReviewers(HttpServletRequest request,String protocolNumber,int sequenceNumber,int submissionNumber)throws Exception{
        HashMap hmReviewers = new HashMap();
        hmReviewers.put(PROTOCOL_NUMBER,protocolNumber);
        hmReviewers.put("sequenceNumber",new Integer(sequenceNumber));
        hmReviewers.put("submissionNumber",new Integer(submissionNumber));
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htReviewers = (Hashtable)webTxnBean.getResults(request,"getIacucSubmissionReviewers",hmReviewers);
        Vector vecReviewers = (Vector)htReviewers.get("getIacucSubmissionReviewers");
        return  vecReviewers!=null && vecReviewers.size()>0 ? vecReviewers: new Vector();
    }
    //COEUSDEV-237 : End
    
    
     protected void readProtocolRequestActionMenu(HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        ReadXMLData readXMLData = new ReadXMLData();
        Vector menuItemsVector = readXMLData.readXMLDataForMenu(PROTOCOL_REQUEST_ACTION_MENU_PATH);
        session.setAttribute(PROTOCOL_REQUEST_ACTION_MENU_ITEMS, menuItemsVector);
        

    }
    protected void resetProtocolRequestActionMenu(HttpServletRequest request, boolean removeQuestionnairemenuItems) throws Exception{
        
        HttpSession session = request.getSession();
        HashMap hmData = new HashMap();

        String protocolNumber = EMPTY_STRING;
        //String seq = EMPTY_STRING;
        String submissionNumber = "1";
//        int sequenceNumber = -1;

        protocolNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId());
        HashMap hmSubmissionData = (HashMap)session.getAttribute("IACUC_SUBMISSION_DETAILS");
        if(hmSubmissionData != null) {
            submissionNumber = (String)hmSubmissionData.get("submissionNumber");
        }
        
        protocolNumber = protocolNumber==null? EMPTY_STRING: protocolNumber;
        String tempProtocolNumber = protocolNumber + "T";
        hmData.put(ModuleDataBean.class , new Integer(ModuleConstants.IACUC_MODULE_CODE));

        hmData.put(SubModuleDataBean.class , new Integer(2));

        hmData.put("link" , "/getIacucSubmissionQuestionnaire.do");
        hmData.put("actionFrom" ,"IACUC_PROTOCOL_SUBMISSION");

        submissionNumber = submissionNumber == null ? "1" : submissionNumber;
        
        Integer intSubmissionNum = Integer.parseInt(submissionNumber) + 1;
        hmData.put("moduleItemKey",tempProtocolNumber);
        hmData.put("moduleSubItemKey",intSubmissionNum.toString());

        Vector menuItemsVector = new Vector();
        menuItemsVector = (Vector)session.getAttribute(PROTOCOL_REQUEST_ACTION_MENU_ITEMS);
        
        if(removeQuestionnairemenuItems){
            removeQuestionnaireMenuItemsFromMenu(request, "IACUC_PROTOCOL_SUBMISSION");
            
        } 
       
        menuItemsVector = getQuestionnaireMenuDataForSubmission(menuItemsVector,request ,hmData);
        session.setAttribute(PROTOCOL_REQUEST_ACTION_MENU_ITEMS, menuItemsVector);
    }
    
    protected boolean prepareLockForprotocolSubmission(String protocolNumber, HttpServletRequest request) throws Exception{
        
        HttpSession session = request.getSession();
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute(USER+session.getId());
        boolean recordLocked = true;
        
        LockBean lockBean = null;
//       LockBean sessionLockBean = (LockBean)session.getAttribute(CoeusLiteConstants.LOCK_BEAN+session.getId());
        
        lockBean = getLockingBean(userInfoBean, protocolNumber, request);
        LockBean dbLockBean = getLockedData(CoeusLiteConstants.IACUC_PROTO_LOCK_STR+lockBean.getModuleNumber(), request);
        
        // Check if this record can be locked.
        boolean lockAvailable = isLockExists(lockBean, lockBean.getModuleKey());
        boolean userAlreadyLocked = false; // This is to check if this ARRA record was already  locked by the logged in user from the same session.
        if(!lockAvailable) {
            // If this record cannot be locked, i.e, Lock already existes in the database
            if(dbLockBean!=null && lockBean != null && lockBean.getSessionId().equals(dbLockBean.getSessionId())) {
                // Check if this record was locked from the same session.
                userAlreadyLocked = true;
            }
        }
        
        if(lockAvailable){
            // If this record can be locked.
            lockModuleForProtocolSubmission(lockBean,request);
            session.setAttribute(CoeusLiteConstants.LOCK_BEAN+session.getId(), lockBean);
            
        }else if(!userAlreadyLocked){
           
                recordLocked = false;
           
            
            session.setAttribute(CoeusLiteConstants.MODE_DETAILS+session.getId(),CoeusLiteConstants.DISPLAY_MODE);
        }
        
        return recordLocked;
    }

    
    public void lockModuleForProtocolSubmission(LockBean lockBean, HttpServletRequest request) throws Exception{
        HttpSession session = request.getSession();
                
        edu.mit.coeus.utils.dbengine.DBEngineImpl dBEngineImpl =
                new edu.mit.coeus.utils.dbengine.DBEngineImpl();
        edu.mit.coeus.utils.dbengine.TransactionMonitor transactionMonitor =
                edu.mit.coeus.utils.dbengine.TransactionMonitor.getInstance();
        java.sql.Connection conn = null;
        try{
            conn = dBEngineImpl.beginTxn();
            //If lock already exists/available for this user no need to insert
            boolean lockCheck = transactionMonitor.isLockAvailable(lockBean.getLockId());
            if(lockCheck) {
                transactionMonitor.canEdit(lockBean.getLockId(), lockBean.getUserId(),
                        lockBean.getUnitNumber(), conn, lockBean.getSessionId());
                dBEngineImpl.endTxn(conn);
            }
        }catch(Exception ex) {
            throw new Exception(ex.getMessage());
        } finally{
            dBEngineImpl.endTxn(conn);
            session.setAttribute(CoeusLiteConstants.LOCK_BEAN+request.getSession().getId(), lockBean);
        }
    }  
    
    // COEUSQA-1724_ Implement validation based on rules in protocols_Start
    /*
       * To check the Validation for provided protocolNumber and sequenceNumber
       * @param protocolNumber
       * @param sequenceNumber
       * @param DynaActionForm to get the dynaData
       * @param HttpSession
       * @throws Exception
       * @return Vector. Contains the objects from DynaValidatorForm
       */
    public Vector checkValidationProtocol(String protocolNumber, int sequenceNumber,
            DynaActionForm dynaForm, HttpSession session) throws Exception{
        int subModuleCode = 0;
        int approvalSequence = 1;
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute(USER+session.getId());
        String userId               = userInfoBean.getUserId();
        ProtocolHeaderDetailsBean headerBean = (ProtocolHeaderDetailsBean)session.getAttribute(PROTOCOL_HEADER_BEAN);
        RoutingTxnBean routingTxnBean = new RoutingTxnBean();
        return getBusinessRulesData(routingTxnBean.validateForRouting(ModuleConstants.IACUC_MODULE_CODE,
                                                                      subModuleCode, 
                                                                      protocolNumber,
                                                                      sequenceNumber, 
                                                                      approvalSequence, 
                                                                      headerBean.getUnitNumber(), 
                                                                      userId), 
                                                                      dynaForm);
    }
    
    /*
     * To process the vector obtained from calling the PLSQL function with the DynaValidatorForm
     * @param vector of validated protocol
     * @param DynaActionForm
     * @throws Exception
     * @return Vector. Contains the objects from DynaValidatorForm
     */
    private Vector getBusinessRulesData(Vector vecValidationProtocol, DynaActionForm  dynaForm) throws Exception{
        Vector dynaVector = new Vector();
        Vector vecErrors = new Vector();
        if(vecValidationProtocol!= null && vecValidationProtocol.size() > 0){
            BusinessRuleBean businessRulesBean  = null;
            for(Object obj : vecValidationProtocol){
                businessRulesBean = (BusinessRuleBean)obj;
                Vector vecRulesConditions = businessRulesBean.getBusinessRuleConditions();
                if(vecRulesConditions != null && vecRulesConditions.size() > 0){
                    BusinessRuleConditionsBean conditionBean = null;
                    for(Object ruleCond :vecRulesConditions){
                        BeanUtilsBean copyBean = new BeanUtilsBean();
                        DynaBean dynaData = (DynaBean)dynaForm.getDynaClass().newInstance();
                        copyBean.copyProperties(dynaData,businessRulesBean);
                        conditionBean =
                                (BusinessRuleConditionsBean)ruleCond;
                        dynaData.set(VALIDATION_DESC, conditionBean.getUserMessage());
                        dynaData.set(VALIDATION_RULE_CATEGORY, businessRulesBean.getRuleCategory());
                        dynaData.set(UNIT_NAME, businessRulesBean.getUnitName());
                        if("E".equals(businessRulesBean.getRuleCategory())){
                            vecErrors.addElement(dynaData);
                        } else {
                            dynaVector.addElement(dynaData);
                        }
                    }
                } else {
                    BeanUtilsBean copyBean = new BeanUtilsBean();
                    DynaBean dynaData = (DynaBean)dynaForm.getDynaClass().newInstance();
                    copyBean.copyProperties(dynaData,businessRulesBean);
                    if("E".equals(businessRulesBean.getRuleCategory())){
                        vecErrors.addElement(dynaData);
                    } else {
                        dynaVector.addElement(dynaData);
                    }
                }
            }
        }
        vecErrors.addAll(dynaVector);
        return  vecErrors;
    }
    // COEUSQA-1724_ Implement validation based on rules in protocols_End
    
    // COEUSQA-1724_IACUC Continuation Continuing -Start
    /*This method is used to check the current protocolStatusCode is equal with these following code
     *@param int protoStatusCode     
     *@return boolean value
     */
    private boolean checkProtocolStatusCode(int protoStatusCode){
        boolean isStatusCodeTrueFalse = false;
        if(protoStatusCode == 200 || protoStatusCode == 201 || protoStatusCode == 202 
           || protoStatusCode == 203 || protoStatusCode == 300 || protoStatusCode == 301
           || protoStatusCode == 302 || protoStatusCode == 308 || protoStatusCode == 305
           || protoStatusCode == 100 || protoStatusCode == 105 || protoStatusCode == 106 
           || protoStatusCode == 101 || protoStatusCode == 304 || protoStatusCode == 306 || protoStatusCode == ROUTING_IN_PROGRESS_STATUS){
                isStatusCodeTrueFalse = true;
         }
        return isStatusCodeTrueFalse;
    }
    // COEUSQA-1724_IACUC Continuation Continuing -End
    
    //Added for COEUSQA-2314 : IRB Admin should have ability to assign committee based on lead unit of the protocol - Start
    /*
     *Check the Status of protocol
     *@param protocolStatusCode
     *@return boolean isValidStatus
     */
    private boolean checkStatusInProgress(int protocolStatusCode){
        boolean isValidStatus =  false;
        if(protocolStatusCode == PENDING_IN_PROGRESS){
            isValidStatus = true;
        }
        return isValidStatus;
    }
    //Added for COEUSQA-2314 : IRB Admin should have ability to assign committee based on lead unit of the protocol - End
    
    //Added for the case# COEUSQA-1697-View Restriction of User in Open Record-start
    /**
     * This method is to username of the locked user, if the DISPLAY_LOCKNAME_PROP_BUDGET
     * is set to 'Y' then current lock user naem will be displayed, else it will disply 
     * 'Another user'.
     * @param loggedInUserId
     * @param lockUserId
     * @return currenatLockUserName
     */
     protected String viewRestrictionOfUser(String loggedInUserId, String lockUserId) throws DBException, CoeusException {
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        UserMaintDataTxnBean userTxnBean = new UserMaintDataTxnBean();
        String currenatLockUserName = userTxnBean.getUserName(lockUserId);
        //DISPLAY_LOCKNAME_IRB is Taking from the parameter table
        String displayLockName = coeusFunctions.getParameterValue(CoeusConstants.DISPLAY_LOCKNAME_IACUC);
        // If the value for DISPLAY_LOCKNAME_PROP_BUDGET is set 'Y' or if loginned user has lock,ot will display the lock user name
            if(displayLockName != null && "Y".equalsIgnoreCase(displayLockName.trim()) || lockUserId.equalsIgnoreCase(loggedInUserId)){
                currenatLockUserName = currenatLockUserName;
            }else{
                currenatLockUserName = CoeusConstants.lockedUsername;
            }
        return currenatLockUserName;
    }
    //Added for the case# COEUSQA-1697-View Restriction of User in Open Record-end
    
  
    //  Added for sequence number updation logic begin	
     
     /**
      * This method identifies the protcol status in which new sequence number need to 
      * be generated. It will returns true for protocol status in which
      * new sequence number need to be generated.
      *
      * @param request HttpServletRequest
      * @return generateSequence boolean
      * @throws Exception
      */
    protected boolean isGenerateSequence(HttpServletRequest request) throws Exception{
        
        boolean generateSequence = false;
        HttpSession session = request.getSession();
        String protocolNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId());
        //Get protocol details from session.
        ProtocolHeaderDetailsBean bean = (ProtocolHeaderDetailsBean)request.getSession().getAttribute(PROTOCOL_HEADER_BEAN);
        if(bean == null){
            //Fetch the details from database.
            Vector vecProtocolHeader = getProtocolHeader(protocolNumber, request);
            if(vecProtocolHeader!=null && vecProtocolHeader.size()>0){
                bean = (ProtocolHeaderDetailsBean) vecProtocolHeader.get(0);
            }
        }
        if(bean!=null){
            int statusCode = bean.getProtocolStatusCode();
            if(statusCode == RETURN_TO_PI_PROTOCOL_STATUS ||
                    statusCode == WITHDRAWN ||
                    statusCode == TABLED_PROTOCOL_STATUS ||
                    statusCode == MINOR_REVISION_REQUIRED_PROTOCOL_STATUS ||
                    statusCode == MAJOR_REVISION_REQUIRED_PROTOCOL_STATUS ||
                    statusCode == ADMINISTRATIVELY_WITHDRAWN ||
                    statusCode == DISAPPROVED ||
                    statusCode == ADMINISTRATIVELY_INCOMPLETE_STATUS){
                generateSequence = true;
            }
        }
        return generateSequence;
    }
    
    /**
     *  This method will identifies the list of objects which have sequence number 
     *  different from protocol sequence number from the incoming parameter lstFormData
     *  and returns the list of objects.
     *  @param lstFormData List
     *  @param request HttpServletRequest
     *  @return lstUpdateRecords Vector
     */
    
    protected Vector processRecordSequence(List  lstFormData , HttpServletRequest request) throws Exception{
        
        Vector lstUpdateRecords = new Vector();
        boolean generateSequence = isGenerateSequence(request);
        
        if(generateSequence){
            //Get protocol sequence
            HttpSession session = request.getSession();
            String protocolSequence = (String)session.getAttribute(CoeusLiteConstants.IACUC_SEQUENCE_NUMBER+session.getId());
            //The protocol status is eligible for new sequence; update the records with the max sequence.
            if(lstFormData != null && !lstFormData.isEmpty()){
                for(Object obj : lstFormData){
                    DynaActionForm objForm = (DynaActionForm) obj;
                    Object recordSequence = objForm.get("sequenceNumber");
                    if(protocolSequence!=null && !protocolSequence.equals(String.valueOf(recordSequence))){
                        objForm.set("acType", TypeConstants.INSERT_RECORD);
                        if(recordSequence instanceof String){
                            objForm.set("sequenceNumber", String.valueOf(protocolSequence));
                        }else if(recordSequence instanceof Integer){
                            objForm.set("sequenceNumber", new Integer(protocolSequence));
                        }
                        lstUpdateRecords.add(objForm);
                    }

                }
          }
        }
        return lstUpdateRecords;
    }
    
    /*
     * This function will handle the appropriate sequence number updatio of existing records.
     * @param updTimeStampPropName - The name of update timestamp property
     * @param vctData - The entire data vector with acType set on the appropriate records
     * @param updateProcedure - The update procedure for the respective screens
     * @param request - The HttpServletRequest object.
     *
     */
    //Modified for COEUSQA-3064 Unable to save funding source type in Lite if it is other than base types supplied 
    protected boolean updateRecordsToNewSequence( List  formData ,
            String updTimeStampPropName, String updateUser, String updateProcedure,  HttpServletRequest request) throws Exception{
        
        WebTxnBean webTxnBean= new WebTxnBean();
        Vector records = processRecordSequence(formData,request);
        boolean isSequenceUpdated = false;
        
        if(records != null){
            for(Object obj : records){
                DynaActionForm objForm = (DynaActionForm) obj;
                String acType = (String)objForm.get("acType");

                if(acType != null && !"".equals(acType.trim()) ){
                    Timestamp dbTimestamp = prepareTimeStamp();
                    objForm.set(updTimeStampPropName,dbTimestamp.toString());
                    UserInfoBean userInfoBean = (UserInfoBean)request.getSession().getAttribute(USER+request.getSession().getId());
                    //Modified for COEUSQA-3064 Unable to save funding source type in Lite if it is other than base types supplied 
                    objForm.set(updateUser,userInfoBean.getUserId());
                    webTxnBean.getResults(request, updateProcedure, objForm);
                    isSequenceUpdated = true;
                }
            }
        }
        return isSequenceUpdated;
    }
    //  Added for sequence number updation logic end	
    
    // Added for CoeusQA2313: Completion of Questionnaire for Submission
    /** 
     * Method to update the questionnaire to a new version
     * if the protocol s undergoing a revision.
     */
    protected boolean checkQuestionnaireRevision(HttpServletRequest request) throws Exception{
        
        boolean updated = false;
        HttpSession session = request.getSession();
        String protocolNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId());
        //Get protocol details from session.
        ProtocolHeaderDetailsBean bean = (ProtocolHeaderDetailsBean)request.getSession().getAttribute(PROTOCOL_HEADER_BEAN);
        if(bean == null){
            //Fetch the details from database.
            Vector vecProtocolHeader = getProtocolHeader(protocolNumber, request);
            if(vecProtocolHeader!=null && vecProtocolHeader.size()>0){
                bean = (ProtocolHeaderDetailsBean) vecProtocolHeader.get(0);
            }
        }
        if(bean!=null){
            // Get user Info
            UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute(USER+session.getId());
            // Get protocol details
            int statusCode = bean.getProtocolStatusCode();
            String sequenceNumber = bean.getSequenceNumber();
            if(isQuestionnaireNeedsRevision(statusCode)){
                updated = updateQuestionnaireForRevision(protocolNumber, sequenceNumber, userInfoBean.getUserId());
            }
        }
        return updated;
    }
    
    /**
     * Method to check if a questionnaire needs a revision
     * @param protocol status code.
     */
    private boolean isQuestionnaireNeedsRevision(int statusCode) {
        boolean canCopy = false;
        
        if(statusCode == MINOR_REVISION_REQUIRED_PROTOCOL_STATUS //Minor Revisions
                || statusCode == MAJOR_REVISION_REQUIRED_PROTOCOL_STATUS// Major revisions
                || statusCode == WITHDRAWN // Withdrawn
                //COEUSQA:3315 - IACUC Protocol Actions Blanking Completed Questionnaires - Start
                || statusCode == RETURN_TO_PI_PROTOCOL_STATUS  // Return to PI
                || statusCode == TABLED_PROTOCOL_STATUS  // Tabled
                || statusCode == ADMINISTRATIVELY_INCOMPLETE_STATUS  //Administratively Incomplete
                || statusCode == IACUC_REVIEW_NOT_REQUIRED_STATUS  // IACUC review  not required
                || statusCode == DISAPPROVED  // Disapproved
                //COEUSQA:3315 - End
                || statusCode == ADMINISTRATIVELY_WITHDRAWN){// Admin Withdrawn
            canCopy = true;
        }
        return canCopy;
    }
    
    /**
     * Method to update questionnaire to new version
     * @param protocolNumber
     * @param sequenceNumber
     * @param loggedInUser
     */
    private boolean updateQuestionnaireForRevision(String protocolNumber,String sequenceNumber, String loggedInUser ) throws Exception{
        QuestionnaireAnswerHeaderBean questionnaireModuleObject = new QuestionnaireAnswerHeaderBean();
        questionnaireModuleObject.setModuleItemCode(ModuleConstants.IACUC_MODULE_CODE);
        questionnaireModuleObject.setModuleItemKey(protocolNumber);
        
        int subModuleItemCode = 0;
        if(protocolNumber.length() > 10 &&  isIacucProtoAmendmentRenewal(protocolNumber.charAt(10))){
            subModuleItemCode = CoeusLiteConstants.IRB_SUB_MODULE_CODE_FOR_AMENDMENT_RENEWAL;
        } else {
            subModuleItemCode = 0;
        }
        questionnaireModuleObject.setModuleSubItemCode(subModuleItemCode);
        questionnaireModuleObject.setModuleSubItemKey(sequenceNumber);
        edu.mit.coeus.questionnaire.bean.QuestionnaireUpdateTxnBean questionnaireUpdateTxnBean
                = new edu.mit.coeus.questionnaire.bean.QuestionnaireUpdateTxnBean(loggedInUser);
        
        boolean dataCopied = questionnaireUpdateTxnBean.copyQuestionnairesForRevisions(questionnaireModuleObject);
        return dataCopied;
    }
    
    // CoeusQA2313: Completion of Questionnaire for Submission - End
    
    /**
     * Method to Update key persons indicator
     * @param isAllDeleted
     * @throws Exception if exception occur
     */
    protected void updateIndicators(boolean isAllDeleted, HttpServletRequest request,String indicatorField, String subModuleIndicator) throws Exception{
        HttpSession session = request.getSession();
        WebTxnBean webTxnBean = new WebTxnBean();
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        Object data = session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_INDICATORS);
        HashMap hmIndicatorMap = (HashMap)data;
        WebUtilities utils =  new WebUtilities();
        HashMap processedIndicator= (HashMap)utils.processIndicator(hmIndicatorMap, subModuleIndicator, isAllDeleted);
        session.setAttribute(CoeusLiteConstants.IACUC_PROTOCOL_INDICATORS, processedIndicator);
        HashMap hashMap = new HashMap();
        hashMap.put(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER,session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId()));
        hashMap.put(CoeusLiteConstants.IACUC_SEQUENCE_NUMBER,session.getAttribute(CoeusLiteConstants.IACUC_SEQUENCE_NUMBER+session.getId()));
        hashMap.put(CoeusLiteConstants.FIELD,indicatorField);
        hashMap.put(CoeusLiteConstants.INDICATOR,(String)processedIndicator.get(subModuleIndicator));
        Timestamp dbTimestamp = prepareTimeStamp();
        hashMap.put(CoeusLiteConstants.KEY_STUDY_TIMESTAMP,dbTimestamp.toString());
        hashMap.put(CoeusLiteConstants.USER,userInfoBean.getUserId());
        webTxnBean.getResults(request, "updateIacucIndicator", hashMap);
    }
     //Added for inticator logic end
       
    //Added COEUSQA-3042 IACUC Renewal and Continuation Add attachment -start
    /*
     * This method is used to check the protocol is renewal or continuation protocol
     * @param String protocolNumber
     * @return Boolean isRenewalContinuation
     */
    protected boolean isProtocolRenewalContinuation(String protocolNumber){
        boolean isRenewalContinuation = false;
        if(protocolNumber.indexOf('R') != -1 || protocolNumber.indexOf('C') != -1){
            isRenewalContinuation = true;
        }
        return isRenewalContinuation;
    }
    //Added COEUSQA-3042 IACUC Renewal and Continuation Add attachment -start
    
    // Added for COEUSQA-3330 IACUC - Ability to Edit Species/Group after Protocol Submission  - Start
    /**
     * Method to check whether the protocol status is non editable or not
     * @param statusCode 
     * @return isProtocolNonEditableStatus
     */
    protected boolean isProtocolNonEditableStatus(int statusCode){
        boolean isProtocolNonEditableStatus = false;
        if(statusCode == 101 || statusCode == 108 || statusCode == 200  || statusCode == 201 ||
                statusCode == 202 || statusCode == 304  || statusCode == 305 || statusCode == 306  ||
                statusCode == 307 || statusCode == 308 ||  statusCode == 309 ){
            /*Submitted to IACUC, Routing In Progress,Active,Active - On Hold,Administratively Approved,
             Suspended,Deactivated,Administratively Deactivated, Terminated, Expired, Abandoned
             */
            isProtocolNonEditableStatus = true;
        }
        
        return isProtocolNonEditableStatus;
    }
    

    /**
     * Method to check protocol is Amemdment/Renewals
     * @param protocolNumber 
     * @return isProtocolAmendRenew
     */
    protected boolean checkProtocolIsAmendRenew(String protocolNumber) {
        boolean isProtocolAmendRenew = false;
        if(protocolNumber != null
                && (protocolNumber.indexOf(Character.toString(CoeusConstants.IACUC_AMENDMENT)) > -1
                || protocolNumber.indexOf(Character.toString(CoeusConstants.IACUC_RENEWAL)) > -1
                || protocolNumber.indexOf(Character.toString(CoeusConstants.IACUC_RENEWAL_WITH_AMENDMENT)) > -1
                || protocolNumber.indexOf(Character.toString(CoeusConstants.IACUC_CONTINUATION_CONTINUING_REVIEW)) > -1
                || protocolNumber.indexOf(Character.toString(CoeusConstants.IACUC_CONTI_CONTINUING_REVIEW_WITH_AMEND)) > -1)){
            isProtocolAmendRenew = true;
        }
        return isProtocolAmendRenew;
    }
    
    /**
     * Method to check logged-in user has admin right (PERFORM_IACUC_ACTIONS_ON_PROTO) in the protocol lead unit
     * @param session 
     * @throws java.lang.Exception 
     * @return isUserhasAdminRight
     */
    protected boolean checkUserHasAdminRight(HttpSession session ) throws Exception{
        boolean isUserhasAdminRight = false;
        ProtocolHeaderDetailsBean headerDetailsBean = (ProtocolHeaderDetailsBean)session.getAttribute("iacucHeaderBean");
        String protocolNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId());
        String sequenceNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_SEQUENCE_NUMBER+session.getId());
        ProtocolDataTxnBean protocolDataTxnBean  = new ProtocolDataTxnBean();
        String protocolLeadUnit = protocolDataTxnBean.getLeadUnitForProtocol(protocolNumber,Integer.parseInt(sequenceNumber));
        ProtocolAuthorizationBean protocolAuthorizationBean = new ProtocolAuthorizationBean();
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        isUserhasAdminRight = protocolAuthorizationBean.hasIRBAdminRights(userInfoBean.getUserId(), protocolLeadUnit);
        return isUserhasAdminRight;
    }
    // Added for COEUSQA-3330 IACUC - Ability to Edit Species/Group after Protocol Submission  - End
    
    protected LockBean getLockingBeanIacucProtoRouting(UserInfoBean userInfoBean, String protocolNumber, HttpServletRequest request) throws Exception{
        LockBean lockBean = new LockBean();
        lockBean.setLockId(CoeusLiteConstants.IACUC_PROTO_ROUTING_LOCK_STR +protocolNumber);
        String mode = (String)request.getSession().getAttribute(CoeusLiteConstants.MODE_DETAILS+request.getSession().getId());
        mode = getMode(mode);
        lockBean.setMode(mode);
        lockBean.setModuleKey(CoeusLiteConstants.IACUC_PROTOCOL_ROUTING_MODULE);
        lockBean.setModuleNumber(protocolNumber);
        lockBean.setModuleUnitNumber(userInfoBean.getUnitNumber());
        lockBean.setUnitNumber(UNIT_NUMBER);
        lockBean.setUserId(userInfoBean.getUserId());
        lockBean.setUserName(userInfoBean.getUserName());
        lockBean.setSessionId(request.getSession().getId());
        return lockBean;
    }
}

