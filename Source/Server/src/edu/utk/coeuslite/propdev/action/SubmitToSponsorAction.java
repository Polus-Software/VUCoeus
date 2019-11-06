/*
 * SubmitToSponsorAction.java
 *
 * Created on August 29, 2006, 12:48 PM
 */

/* PMD check performed, and commented unused imports and
 * variables on 30-JULY-2011 by Bharati
 */

package edu.utk.coeuslite.propdev.action;

import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.instprop.bean.InstituteProposalTxnBean;
import edu.mit.coeus.irb.bean.ProtocolDataTxnBean;
import edu.mit.coeus.irb.bean.ProtocolInfoBean;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentUpdateTxnBean;
import edu.mit.coeus.routing.bean.RoutingUpdateTxnBean;
import edu.mit.coeus.s2s.bean.S2SSubmissionDataTxnBean;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ModuleConstants;
//import edu.mit.coeuslite.utils.CoeusLiteConstants;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;
//import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;

/**
 *
 * @author  chandrashekara
 */
public class SubmitToSponsorAction extends ProposalBaseAction{
    private static final String SUCCESS = "success";
    private static final String SUBMIT_SPONSOR = "/submitSponsor";
    private static final String FEED_INST_PROPOSAL = "/feedInstProposal";
    //Added For the Case # COEUSQA-1679-Modification for final flag indicator -start
    private static final String SUBMIT_SPONSOR_FAILED = "/cannotPerformSubmitSponsor";
    //Added For the Case # COEUSQA-1679-Modification for final flag indicator -end
    private static final String PROPOSAL_NUMBER = "proposalNumber";
    
    private static final String PROPOSAL_DETAILS_DATA = "getProposalSummaryDetails";
    private static final String FEED_INSTITUTE_PROPOSAL_NUMBER = "feedInstituteProposalNumber";
    private static final String GET_INST_PROP_NUMBER = "getInstPropNumber";
    private static final String INST_PROP_NUMBER_DATA = "instPropNumber";
    private static final String VALIDATE_INST_PROP = "validateInstProp";
    private static final String GENERATE_UPDATE_PROPOSAL = "generateUpdateProposal";
    private static final String FEED_ONLY_INST_PROP = "feedInstPropsoal";
    private static final String SET_PROPOSAL_STATUS = "updateProposalstatusCode";
    
    private static final int REVISION = 6;
//    private String GENERATE = "G";-- 2960
    private static final String SUBMISSION_TYPE = "P";
    private static final String SUBMISSION_STATUS = "S";
//    private String INST_PROP_NUMBER = "";-- 2960
    private static final String CREATION_STATUS_CODE = "statusCode";
    
    private static final String GENERATE_IDENTIFIER = "generate";
    private static final String SUBMISSION_TYPE_INDENTIFIER = "submissionType";
    private static final String SUBMISSION_STATUS_IDENTIFIER = "submissionStatus";
    private static final String INST_PROP_IDENTIFIER = "instProp";
    private static final String USER_ID = "userId";
    private static final String SUBMIT_TO_SPONSOR = "submitToSponsor";
    private static final String APPROVAL_STATUS_CODE = "ApproveStatusCode";
    
    //COEUSQA-2984 : Statuses in special review - start
    private static final String PROTOCOL_NUMBER = "spRevProtocolNumber";
    private static final String SPECIAL_REVIEW_NUMBER = "specialReviewNumber";
    private static final String PROPOSAL_SUBMITTED_STATUS = "5";
    private static final String ENABLE_PROTOCOL_TO_DEV_PROPOSAL ="1";
    //COEUSQA-2984 : Statuses in special review - end
    
    //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - start
    private static final String ENABLE_IACUC_TO_DEV_PROPOSAL = "1";
    //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - end
    
    //Messages
    private static final String DO_NOT_GENERATE = "doNotGenerate";
    private static final String INVALID_INST_PROP_NUM = "invalidInstPropNum";
    private static final String SELECT_OPTION_DATA = "selectOption";
    private static final String INSTITUTE_PROPOSAL_SCOPE_DATA = "InstituteProposal";
    private static final String SEQUENCE_SCOPE_DATA = "Sequence";
    
    private static final String VALUE_TWO = "2";
    private static final String VALUE_SIX  = "6";
    private static final String VALUE_FIVE = "5";
    /** Creates a new instance of SubmitToSponsorAction */
    public SubmitToSponsorAction() {
    }
    
    public ActionForward performExecute(ActionMapping actionMapping, ActionForm actionForm, 
       HttpServletRequest request, HttpServletResponse response) throws Exception {

           HttpSession session = request.getSession();
           DynaValidatorForm dynaForm = (DynaValidatorForm)actionForm;
           ActionForward actionForward = null;
           session.removeAttribute("INST_PROP_NUMBER");
           session.removeAttribute("GENERATE_INST_PROP_NUMBER");           
           String proposalNumber = (String)session.getAttribute(PROPOSAL_NUMBER+session.getId());
           //String approvalStatusCode = request.getParameter("statusCode");
           String approvalStatusCode = (String)session.getAttribute("statusCode");
           if(approvalStatusCode!= null && !approvalStatusCode.equals(EMPTY_STRING)){
            session.setAttribute(APPROVAL_STATUS_CODE,approvalStatusCode);
           }
           if(actionMapping.getPath().equals(SUBMIT_SPONSOR)){
               actionForward = submitToSponsor(dynaForm,actionMapping,proposalNumber,request);
               //COEUSQA-2984 : Statuses in special review - start
               //If status of the proposal is submitted and it is linked to human subjects then update the status of the protocol to the table OSP$EPS_PROP_SPECIAL_REVIEW
               WebTxnBean webTxnBean = new WebTxnBean();
               UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
               InstituteProposalTxnBean instituteProposalTxnBean = new InstituteProposalTxnBean();
               CoeusFunctions coeusFunctions = new CoeusFunctions();
               String protocolToInstPropLink = coeusFunctions.getParameterValue(CoeusConstants.ENABLE_PROTOCOL_TO_PROPOSAL_LINK);
               String protocolToDevPropLink = coeusFunctions.getParameterValue(CoeusConstants.ENABLE_PROTOCOL_TO_DEV_PROPOSAL_LINK);
               //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - start
               String iacucProtoToInstPropLink = coeusFunctions.getParameterValue(CoeusConstants.ENABLE_IACUC_PROTOCOL_TO_PROPOSAL_LINK);
               String iacucProtoToDevPropLink = coeusFunctions.getParameterValue(CoeusConstants.ENABLE_IACUC_TO_DEV_PROPOSAL_LINK);
               String specialRevCodeForIacuc = coeusFunctions.getParameterValue(CoeusConstants.IACUC_SPL_REV_TYPE_CODE);
               String linkedToIacucCode = coeusFunctions.getParameterValue(CoeusConstants.LINKED_TO_IACUC_CODE);
               
               String instProposal = (String)session.getAttribute("instPropoNumber");
               
               if(protocolToInstPropLink.equals("1")){
                   String specialReviewCode = coeusFunctions.getParameterValue(CoeusConstants.SPL_REV_TYPE_CODE_HUMAN);
                   String linkedToIrbCode = coeusFunctions.getParameterValue(CoeusConstants.LINKED_TO_IRB_CODE);
                   ProposalDevelopmentUpdateTxnBean proposalDevelopmentUpdateTxnBean = new ProposalDevelopmentUpdateTxnBean(userInfoBean.getUserId());
                   HashMap hmpSpRev = new HashMap();
                   hmpSpRev.put("proposalNumber",proposalNumber);
                   hmpSpRev.put("moduleCode", ModuleConstants.PROPOSAL_DEV_MODULE_CODE);
                   Hashtable hsSpRev = (Hashtable)webTxnBean.getResults(request, "getPropDevSpecialReview", hmpSpRev);
                   Vector vecData = (Vector)hsSpRev.get("getProposalSpecialReview");
                   HashMap protocolData = new HashMap();
                   ProtocolDataTxnBean protoTxnBean = new ProtocolDataTxnBean();
                   String specialReviewTypeCode = (String)session.getAttribute("specialReviewTypeCode");
                   edu.mit.coeus.iacuc.bean.ProtocolDataTxnBean iacucProtoTxnBean = new edu.mit.coeus.iacuc.bean.ProtocolDataTxnBean();
                   if(PROPOSAL_SUBMITTED_STATUS.equals(approvalStatusCode)){
                       if(vecData != null && !vecData.isEmpty()){
                           for(int index = 0 ; index <vecData.size() ; index++){
                               DynaValidatorForm specialRevForm = (DynaValidatorForm)vecData.get(index);
                               String protocolNumber = (String)specialRevForm.get("spRevProtocolNumber");
                               Integer specialReviewNumber = (Integer)specialRevForm.get("specialReviewNumber");
                               String spRvCode = (String)specialRevForm.get("specialReviewCode");
                               if(spRvCode.equals(specialReviewCode)){
                                   protocolData.put(PROTOCOL_NUMBER, protocolNumber);
                                   protocolData.put(SPECIAL_REVIEW_NUMBER, specialReviewNumber);
                                   protocolData.put(PROPOSAL_NUMBER, proposalNumber);
                                   ProtocolInfoBean protoInfoBean = (ProtocolInfoBean)protoTxnBean.getProtocolMaintenanceDetails(protocolNumber);
                                   int protocolStatusCode = protoInfoBean.getProtocolStatusCode();
                                   if(ENABLE_PROTOCOL_TO_DEV_PROPOSAL.equals(protocolToDevPropLink)){
                                       boolean updateTable = proposalDevelopmentUpdateTxnBean.updateProtocolStatus(proposalNumber,specialReviewNumber,protocolNumber,protocolStatusCode);
                                   }
                               }
                           }
                       }
                       //COEUSQA-2984 : Statuses in special review - end
                   }
                   CoeusVector cvInstPropData = new CoeusVector();
                   cvInstPropData.addElement(instituteProposalTxnBean.getInstituteProposalSpecialReview(instProposal));
                   cvInstPropData.addElement(specialReviewCode);
                   cvInstPropData.addElement(linkedToIrbCode);
                   proposalDevelopmentUpdateTxnBean = new ProposalDevelopmentUpdateTxnBean(userInfoBean.getUserId());
                   proposalDevelopmentUpdateTxnBean.performProtocolLinkFromProposalDev(cvInstPropData , userInfoBean.getUnitNumber());
               }
               if(iacucProtoToInstPropLink.equals("1")){
                   String specialReviewCode = coeusFunctions.getParameterValue(CoeusConstants.SPL_REV_TYPE_CODE_HUMAN);
                   String linkedToIrbCode = coeusFunctions.getParameterValue(CoeusConstants.LINKED_TO_IRB_CODE);
                   ProposalDevelopmentUpdateTxnBean proposalDevelopmentUpdateTxnBean = new ProposalDevelopmentUpdateTxnBean(userInfoBean.getUserId());
                   HashMap hmpSpRev = new HashMap();
                   hmpSpRev.put("proposalNumber",proposalNumber);
                   hmpSpRev.put("moduleCode", ModuleConstants.PROPOSAL_DEV_MODULE_CODE);
                   Hashtable hsSpRev = (Hashtable)webTxnBean.getResults(request, "getPropDevSpecialReview", hmpSpRev);
                   Vector vecData = (Vector)hsSpRev.get("getProposalSpecialReview");
                   HashMap protocolData = new HashMap();
                   ProtocolDataTxnBean protoTxnBean = new ProtocolDataTxnBean();
                   edu.mit.coeus.iacuc.bean.ProtocolDataTxnBean iacucProtoTxnBean = new edu.mit.coeus.iacuc.bean.ProtocolDataTxnBean();
                   //If status of the proposal is submitted and it is linked to Animal Usage(IACUC) and there is link between IACUC n Dev proposal
                   //then update the status of the Iacuc protocol to the table OSP$EPS_PROP_SPECIAL_REVIEW
                   if(PROPOSAL_SUBMITTED_STATUS.equals(approvalStatusCode)){
                       if(vecData != null && !vecData.isEmpty()){
                           for(int index = 0 ; index <vecData.size() ; index++){
                               DynaValidatorForm specialRevForm = (DynaValidatorForm)vecData.get(index);
                               String protocolNumber = (String)specialRevForm.get("spRevProtocolNumber");
                               Integer specialReviewNumber = (Integer)specialRevForm.get("specialReviewNumber");
                               String spRvCode = (String)specialRevForm.get("specialReviewCode");
                               if(spRvCode.equals(specialRevCodeForIacuc)){
                                   edu.mit.coeus.iacuc.bean.ProtocolInfoBean protoInfoBean = (edu.mit.coeus.iacuc.bean.ProtocolInfoBean)iacucProtoTxnBean.getProtocolMaintenanceDetails(protocolNumber);
                                   int protocolStatusCode = protoInfoBean.getProtocolStatusCode();
                                   if(ENABLE_IACUC_TO_DEV_PROPOSAL.equals(iacucProtoToDevPropLink)){
                                       boolean updateTable = proposalDevelopmentUpdateTxnBean.updateIacucProtocolStatus(proposalNumber,specialReviewNumber,protocolNumber,protocolStatusCode);
                                   }
                               }
                           }                         
                       }
                   }
                   CoeusVector cvInstPropData = new CoeusVector();
                   cvInstPropData.addElement(instituteProposalTxnBean.getInstituteProposalSpecialReview(instProposal));
                   cvInstPropData.addElement(specialRevCodeForIacuc);
                   cvInstPropData.addElement(linkedToIacucCode);
                   proposalDevelopmentUpdateTxnBean = new ProposalDevelopmentUpdateTxnBean(userInfoBean.getUserId());
                   proposalDevelopmentUpdateTxnBean.performIacucLinkFromProposalDev(cvInstPropData , userInfoBean.getUnitNumber());
               }//Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - end
           }else if(actionMapping.getPath().equals(FEED_INST_PROPOSAL)){
               actionForward  = feedAndValidateProposal(proposalNumber,dynaForm,
                       actionMapping,request);
           }
           //Added For the Case # COEUSQA-1679-Modification for final flag indicator -start
           else if(actionMapping.getPath().equals(SUBMIT_SPONSOR_FAILED)){
               actionForward  = submitToSponsorFailed(dynaForm,actionMapping,proposalNumber,request);
           }
           //Added For the Case # COEUSQA-1679-Modification for final flag indicator -end
                     
           return actionForward;
    }
    
    /** This method will feed the inst proposal and updates the status
     *@param DynaValidatorForm, ActionMapping
     *@returns ActionForward
     *@throws Exception
     */
    private ActionForward feedAndValidateProposal(String proposalNumber,DynaValidatorForm dynaForm,
        ActionMapping actionMapping, HttpServletRequest request) throws Exception{
        String instProposalNumber = (String)dynaForm.get(INST_PROP_NUMBER_DATA);
        boolean isValid = false;
        String GENERATE = "G";        
        ActionMessages actionMessages = new ActionMessages();
        ActionForward actionForward = null;
        if(instProposalNumber!= null && !instProposalNumber.equals(EMPTY_STRING)){
            isValid = validateProposalNumber(instProposalNumber, request);
        }
        String selctedItem = (String)dynaForm.get("generated");
        String notSelectedItem = (String)dynaForm.get("notGenerated");
        // If do not generate is selected
        if( notSelectedItem.equals("1")){
            // Show the message-----
            // return from here
            actionMessages.add("doNotGenerate", new ActionMessage(DO_NOT_GENERATE));
            actionMessages.add("doNotGenerate", new ActionMessage("doNotGenerate1"));
            actionMessages.add("doNotGenerate", new ActionMessage("doNotGenerate2"));
            saveMessages(request, actionMessages);
            request.setAttribute(PROPOSAL_NUMBER,proposalNumber);
            actionForward = actionMapping.findForward("messages");
            return actionForward;
            // If generate button is selected
        }else if( selctedItem.equals("2")){
            GENERATE = "G";
            request.getSession().setAttribute("GENERATE_INST_PROP_NUMBER",GENERATE );//2960             
        }else if(instProposalNumber != null ){
            if(!isValid){
                // Show the error message for invalid proposal number
                // return from here
                actionMessages.add("invalidInstProp", new ActionMessage(INVALID_INST_PROP_NUM));
                saveMessages(request, actionMessages);
                request.setAttribute(PROPOSAL_NUMBER,proposalNumber);
                actionForward = actionMapping.findForward("messages");
                return actionForward;
            }else{
                GENERATE = "N";
                request.getSession().setAttribute("GENERATE_INST_PROP_NUMBER",GENERATE );//2960
            }
        }else {
            // Select any option error message
            // return from here
            actionMessages = new ActionMessages();
            actionMessages.add("selectOption", new ActionMessage(SELECT_OPTION_DATA));
            saveMessages(request, actionMessages);
            request.setAttribute(PROPOSAL_NUMBER,proposalNumber);
            actionForward = actionMapping.findForward("messages");
            return actionForward;
        }
        
        actionForward = feedAndUpdateProposalStatus(proposalNumber,dynaForm,
                                actionMapping,instProposalNumber,request);
        return actionForward;
    }
    
   /** This method will validate the entered institute proposal number
    *@param institute proposal number
    *@returns boolean , says valid or not
    *@throws Exception 
    */
    private boolean validateProposalNumber(String instPropNumber,
        HttpServletRequest request) throws Exception{
        HashMap mapData = new HashMap();
        WebTxnBean webTxnBean = new WebTxnBean();
        mapData.put(INST_PROP_NUMBER_DATA, instPropNumber);
        boolean isValid = false;
        Hashtable htData = (Hashtable)webTxnBean.getResults(request, VALIDATE_INST_PROP, mapData);
        HashMap hmData = (HashMap)htData.get(VALIDATE_INST_PROP);
        String value = (String)hmData.get("isValid");
        int data = Integer.parseInt(value);
        if(data > 0){
            isValid = true;
        }
        return isValid;
    }
    
    /** This method will be invoked when the proposal Type code is != 6(REVISION)
     *It process the data and shows the generated proposal number
     */
    private ActionForward submitToSponsor(DynaValidatorForm dynaForm,
            ActionMapping actionMapping, String proposalNumber, HttpServletRequest request) throws Exception{
        HashMap mapData = new HashMap();
        WebTxnBean webTxnBean = new WebTxnBean();
        mapData.put(PROPOSAL_NUMBER, proposalNumber);
        Hashtable htProposalData = (Hashtable)webTxnBean.getResults(request, PROPOSAL_DETAILS_DATA, mapData);
        Vector dataObject = (Vector)htProposalData.get(PROPOSAL_DETAILS_DATA);
        dynaForm = (DynaValidatorForm)dataObject.get(0);
        String proposalTypeCode = (String)dynaForm.get("proposalType");
        int code = Integer.parseInt(proposalTypeCode);
        ActionForward actionForward = feedData(proposalNumber,code, actionMapping, request);
        return actionForward;
    }
    
    /** This method will feed the data to generate the inst proposal number
     *and set the post submission status
     *also calls a method to get the generated inst propsosal number for the
     *given development proposal number
     *@param propsoalNumber
     *@ throws Exception
     */
    private ActionForward feedData(String proposalNumber,  int code, 
        ActionMapping actionMapping, HttpServletRequest request) throws Exception{
        ActionForward actionForward = null;
        HttpSession session = request.getSession();
        if(code!= 6){
            UserInfoBean userInfoBean = (UserInfoBean)request.getSession().getAttribute("user"+request.getSession().getId());
            HashMap mapData = (HashMap)buildMapData(request,proposalNumber,userInfoBean);
            //2960
            String GENERATE = (String)request.getSession().getAttribute("GENERATE_INST_PROP_NUMBER");
            mapData.put(GENERATE_IDENTIFIER,"G");
            
            //Added for COEUSQA-3008 : Proposal Admin Details always displays Submission Type as Paper - start
            String propSubmissionType;
            S2SSubmissionDataTxnBean s2SSubmissionDataTxnBean = new S2SSubmissionDataTxnBean();
            //If proposal is S2S submission to Grants.gov(G,G proposal) then set submission type as "S"
            // "S" indicates for proposal is of type System to Sytem
            if(s2SSubmissionDataTxnBean.isS2SCandidate(proposalNumber)){
                propSubmissionType = "S";
                mapData.put(SUBMISSION_TYPE_INDENTIFIER,propSubmissionType);
            }
            //Added for COEUSQA-3008 : Proposal Admin Details always displays Submission Type as Paper - end
            
            // Feed the institute proposal and set the status as post submission
            Hashtable htTableData = feedInstitutePropsoal(mapData, request);
            //Code added for Case#2785 - Coeuslite Routing enhancement - starts
            RoutingUpdateTxnBean routingUpdateTxnBean = new RoutingUpdateTxnBean(userInfoBean.getUserId());
            routingUpdateTxnBean.sendMailToSponsor(proposalNumber);
            //Code added for Case#2785 - Coeuslite Routing enhancement - ends
            mapData.remove(INST_PROP_IDENTIFIER);
            mapData.remove(GENERATE_IDENTIFIER);
            mapData.remove(SUBMISSION_TYPE_INDENTIFIER);
            mapData.remove(SUBMISSION_STATUS_IDENTIFIER);
            // Get the feeded institute proposal number
            String instPropoNumber = getFeededInstProp(mapData, request);
            if(instPropoNumber != null){
                session.setAttribute("instPropoNumber", instPropoNumber);
            }
            request.setAttribute(INSTITUTE_PROPOSAL_SCOPE_DATA,instPropoNumber);
            request.setAttribute(SEQUENCE_SCOPE_DATA,GENERATE);
                        
            // COEUSDEV-146: Proposal Hierarchy - Child proposal status does not change when parent propsoal is submitted to sponsor
            updateChildStatus(request,proposalNumber);
            actionForward = actionMapping.findForward(SUCCESS);
        }else{
            // show the window to choose the existing proposal number
            actionForward = actionMapping.findForward(SUBMIT_TO_SPONSOR);
        }
        return actionForward;
    }
    
    /** This method will feed the institute proposal and set the post submission status 
     *The DB functions are FN_UPDATE_PROPOSAL_STATUS and FN_SET_POST_SUB_STATUS
     */
    private Hashtable feedInstitutePropsoal(HashMap mapData,
        HttpServletRequest request) throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htTableData = (Hashtable)webTxnBean.getResults(request, 
                    FEED_INSTITUTE_PROPOSAL_NUMBER,mapData);
        return htTableData;
    }
    
    /** This method will communicate with the data base to get the generated 
     *institute proposal number for given development proposal number
     *@param HashMap containing the development proposal
     *@returns the valid institute proposal number which is generated
     *@throws Exception
     */
    private String getFeededInstProp(HashMap mapData,
        HttpServletRequest request) throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();            
        Hashtable htTableData = (Hashtable)webTxnBean.getResults(request, GET_INST_PROP_NUMBER,mapData);
        HashMap hmData = (HashMap)htTableData.get(GET_INST_PROP_NUMBER);
        String instPropoNumber = (String)hmData.get(INST_PROP_NUMBER_DATA);
        return instPropoNumber;
    }
    
    /** This method will feed the institute proposal and changes the proposal status
     *It uses DB stored functions like FN_UPDATE_PROPOSAL_STATUS and FN_FEED_INST_PROP
     *@param DynaValidatorForm
     *@param ActionMapping 
     *@throws Exception
     */
    private ActionForward feedAndUpdateProposalStatus(String proposalNumber,DynaValidatorForm dynaForm, 
        ActionMapping actionMapping, String instProposalNumber, HttpServletRequest request) throws Exception{
            
         UserInfoBean userInfoBean =(UserInfoBean)request.getSession().getAttribute("user"+request.getSession().getId());
//         INST_PROP_NUMBER = instProposalNumber;--2960
         String INST_PROP_NUMBER = instProposalNumber;
         request.getSession().setAttribute("INST_PROP_NUMBER", INST_PROP_NUMBER);
         String GENERATE = (String)request.getSession().getAttribute("GENERATE_INST_PROP_NUMBER");
         //2960
         HashMap mapData = (HashMap)buildMapData(request,proposalNumber,userInfoBean);
         Hashtable htData = feedAndUpdateStatus(mapData,request);
         updateProposalStatus(proposalNumber,userInfoBean,request);
         mapData.remove(INST_PROP_IDENTIFIER);
         mapData.remove(GENERATE_IDENTIFIER);
         mapData.remove(SUBMISSION_TYPE_INDENTIFIER);
         mapData.remove(SUBMISSION_STATUS_IDENTIFIER);
         // Get the feeded institute proposal number
         String instPropoNumber = getFeededInstProp(mapData, request);
         request.setAttribute(INSTITUTE_PROPOSAL_SCOPE_DATA,instPropoNumber);
         request.setAttribute(SEQUENCE_SCOPE_DATA,GENERATE);
         ActionForward actionForward = actionMapping.findForward(SUCCESS);
         return actionForward;
         
    }
    /** This method will feeds and updates the data
     *@param HashMap contains the ncessary data
     *@returns Hashtable contains the result of executed data
     *@throws Exception
     */
    private Hashtable feedAndUpdateStatus(HashMap mapData,
        HttpServletRequest request) throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean(); 
        Hashtable htTableData = (Hashtable)webTxnBean.getResults(request, 
                    FEED_ONLY_INST_PROP,mapData);
        return htTableData;
    }
    
    private Hashtable updateProposalStatus(String proposalNumber, UserInfoBean userInfoBean,
        HttpServletRequest request) throws Exception{
        HashMap hmStatusData = (HashMap)buildStautsMap(proposalNumber,userInfoBean,request);
        WebTxnBean webTxnBean = new WebTxnBean(); 
        Hashtable htTableData = (Hashtable)webTxnBean.getResults(request,SET_PROPOSAL_STATUS,hmStatusData);
        return htTableData;
    }
    /** Build HashMap required for updating the proposal status
     *@param proposalNumber, UsreInfoBean
     *@throws Exception
     *@returns HashMap containing required data
     */
    private Map buildStautsMap(String proposalNumber, UserInfoBean userInfoBean,
        HttpServletRequest request) throws Exception{
        String code = EMPTY_STRING;
        HttpSession session = request.getSession();
        int codeValue = -1;
        String statusCode =  (String)session.getAttribute(APPROVAL_STATUS_CODE);
        if(statusCode!= null && !statusCode.equals(EMPTY_STRING)){
           if(statusCode.equals(VALUE_TWO)){
               code = VALUE_SIX;
           }else if(statusCode.equals(VALUE_FIVE)){
               code = VALUE_FIVE;
           }
           codeValue = Integer.parseInt(code);
        }
        HashMap mapData = new HashMap();
        mapData.put(PROPOSAL_NUMBER,proposalNumber);
        mapData.put(CREATION_STATUS_CODE,new Integer(codeValue));
        mapData.put(USER_ID,userInfoBean.getUserId());
        session.removeAttribute(APPROVAL_STATUS_CODE);
        return mapData;
    }
    
    
    
    /** Build the Map data to make server call
     */
    private Map buildMapData(HttpServletRequest request,String proposalNumber, UserInfoBean userInfoBean) throws Exception{
        HashMap mapData = new HashMap();
        mapData.put(PROPOSAL_NUMBER,proposalNumber);
        //2960
        String INST_PROP_NUMBER = (String)request.getSession().getAttribute("INST_PROP_NUMBER"); 
        mapData.put(INST_PROP_IDENTIFIER,INST_PROP_NUMBER);
        
        String GENERATE = (String)request.getSession().getAttribute("GENERATE_INST_PROP_NUMBER");
        mapData.put(GENERATE_IDENTIFIER,GENERATE);
        //2960
        mapData.put(SUBMISSION_TYPE_INDENTIFIER,SUBMISSION_TYPE);
        mapData.put(SUBMISSION_STATUS_IDENTIFIER,SUBMISSION_STATUS);
        mapData.put(USER_ID, userInfoBean.getUserId());
        return mapData;
    }
    //Added For the Case # COEUSQA-1679-Modification for final flag indicator -start
    private ActionForward submitToSponsorFailed(DynaValidatorForm dynaForm, ActionMapping actionMapping, String proposalNumber, HttpServletRequest request) {        
        ActionForward actionForward = actionMapping.findForward(SUCCESS);
        return actionForward;
    }
    //Added For the Case # COEUSQA-1679-Modification for final flag indicator -start
}
