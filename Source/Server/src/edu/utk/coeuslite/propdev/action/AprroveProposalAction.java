/*
 * AprroveProposalAction.java
 *
 * Created on August 1, 2006, 5:33 PM
 */

package edu.utk.coeuslite.propdev.action;

import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.propdev.bean.ProposalApprovalMapBean;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import edu.utk.coeuslite.propdev.bean.ApprovalRouteDisplayBean;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.DynaBean;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.apache.struts.validator.DynaValidatorForm;

/**
 *
 * @author  vinayks
 */
public class AprroveProposalAction extends ProposalBaseAction {
    private static final String PROPOSAL_MESSAGES = "ProposalMessages";
    private static final String BY_PASS = "ByPass";
    private static final String APPROVE = "approve";
    private static final String REJECT = "Reject";
    private static final String ACTION_MODE = "actionMode";
    private static final String SUCCESS ="success";
    private static final String PROPOSAL_NUMBER = "proposalNumber" ;
    private static final String MAP_ID = "mapId" ;
    private static final String APPROVE_SUCCESS ="APPROVE_SUCCESS";
    private static final String APPROVE_ACTION = "A";
    private static final String REJECT_ACTION = "R";
    private static final String BYPASSS_ACTION = "B";
    private static final String APPROVE_ALL = "1";
    private static final String APPROVE_NOT_ALL = "0";
    private static final String WAITING_FOR_APPROVAL = "W" ;
    private static final String PRIMARY_APPROVER_FLAG = "Y";    
    private static final String CREATION_STATUS_CODE = "creationStatusCode" ;
    private static final String BYPASS_COMMENTS = "approve_proposal_bypass_comments" ;
    
    private static final String GET_ROUTING_ACTIONS = "/getProposalActions" ;
    private static final String APPROVE_PROPOSAL = "/approveProposal" ;
    //Statement IDs
    private static final String APPROVE_PROPOSAL_ALL = "/approveProposalAll";
    private static final String UPDATE_APPROVAL_COMMENTS = "updatePropApprComments";
    private static final String GET_PROP_APPROVAL_FOR_STATUS = "getPropApprovalForStatus" ;
    private static final String APPROVE_PROPOSAL_ACTION ="approveProposal";
    private static final String UPDATE_PROPOSAL_STATUS = "updateProposalStatus";
    private static final String GET_PROP_APPROVAL_MAPS = "getProposalApprovalMaps" ;
    private static final String GET_PROP_APPROVAL_STATUS = "getProposalApprovalStatus" ;
    
    /** Creates a new instance of AprroveProposalAction */
    public AprroveProposalAction() {
    }
    
    public ActionForward performExecute(ActionMapping actionMapping, ActionForm actionForm,
    HttpServletRequest request, HttpServletResponse response) throws Exception {
        DynaValidatorForm dynaApproveProposalForm = (DynaValidatorForm)actionForm ;
        HttpSession session = request.getSession() ;
        String actionMode = EMPTY_STRING ;
        String navigator = SUCCESS ;
        System.out.println("PROPOSAL NUMBER"  +session.getAttribute("proposalNumber"+session.getId()));
        
        if(actionMapping.getPath().equals(GET_ROUTING_ACTIONS)){
            actionMode = request.getParameter(ACTION_MODE);
            if(actionMode!=null && !actionMode.equals(EMPTY_STRING)){
                MessageResources messages = MessageResources.getMessageResources(PROPOSAL_MESSAGES);
                String byPassComments = messages.getMessage(BYPASS_COMMENTS);
                if(actionMode.equalsIgnoreCase(BY_PASS)){
                    dynaApproveProposalForm.set("comments" , byPassComments) ;
                }
                String beanName = request.getParameter("beanName");
                ApprovalRouteDisplayBean byPassBean =
                (ApprovalRouteDisplayBean)session.getAttribute(beanName+session.getId());
                session.setAttribute("byPassApproval"+session.getId(), byPassBean);
                dynaApproveProposalForm.set(ACTION_MODE ,actionMode.trim());
            }
        }else if(actionMapping.getPath().equals(APPROVE_PROPOSAL)){
            actionMode = (String)dynaApproveProposalForm.get(ACTION_MODE);
            if(actionMode!=null && !actionMode.equals(EMPTY_STRING)){
                if(actionMode.equalsIgnoreCase(APPROVE)){
                    Vector vecPropApprovalStatus = getPropApprovalForStatus(request);
                     if(vecPropApprovalStatus!=null && vecPropApprovalStatus.size()>0){
                         request.setAttribute("approveAll", "approveAll");
                         navigator = "approveAll";
                     }else{
                         dynaApproveProposalForm.set("approveAll" , "1");
                         performApproveAction(request, dynaApproveProposalForm, response);
                     }
                }else if(actionMode.equalsIgnoreCase(REJECT)){
                    performRejectAction(request, dynaApproveProposalForm, response);
                }else if(actionMode.equalsIgnoreCase(BY_PASS)){
                    performByPassAction(request, dynaApproveProposalForm, response);
                }
            }
        } else if (actionMapping.getPath().equals(APPROVE_PROPOSAL_ALL)){
            performApproveAction(request, dynaApproveProposalForm, response);
        }
        return  actionMapping.findForward(navigator);
    }
    
    
    /**This method performs Approve action
     * @throws Exception
     */
    public void performApproveAction(HttpServletRequest request, 
        DynaValidatorForm dynaApproveProposalForm, HttpServletResponse response)throws Exception{
        Vector vecPropApproval = performApproveProposalAction(request);
        Vector vecFilteredApprove = prepareApproveBean(vecPropApproval, request);
      // Vector vecPropApprovalStatus = getPropApprovalForStatus();
       updateApproveProposal(vecFilteredApprove ,APPROVE_ACTION ,
            (String) dynaApproveProposalForm.get("approveAll"), dynaApproveProposalForm, request, response);
    }
    
    /**
     * This method is to perform the reject action
     * @throws Exception
     */
    public void performRejectAction(HttpServletRequest request, 
        DynaValidatorForm dynaApproveProposalForm, HttpServletResponse response)throws Exception{
        //Vector vecPropApprovalStatus = getPropApprovalForStatus();
        Vector vecPropApproval = performApproveProposalAction(request);
        Vector vecFilteredApprove = prepareApproveBean(vecPropApproval, request);
        dynaApproveProposalForm.set("approveAll" , "1");
        updateApproveProposal(vecFilteredApprove ,REJECT_ACTION , 
            (String) dynaApproveProposalForm.get("approveAll"), dynaApproveProposalForm, request, response);
    }
    
    /**
     * This method id to perform the bypass action
     * @throws Exception
     */
    public void performByPassAction(HttpServletRequest request, 
        DynaValidatorForm dynaApproveProposalForm, HttpServletResponse response)throws Exception{
        Vector vecByPassProposal = new Vector();
        HttpSession session = request.getSession();
        ApprovalRouteDisplayBean byPassBean =
            (ApprovalRouteDisplayBean)session.getAttribute("byPassApproval"+session.getId());
        //BeanUtilsBean beanUtilsBean = new BeanUtilsBean();
        // beanUtilsBean.copyProperties(dynaApproveProposalForm , byPassBean);
        dynaApproveProposalForm.set("proposalNumber",byPassBean.getProposalNumber());
        dynaApproveProposalForm.set("mapId",byPassBean.getMapId());
        dynaApproveProposalForm.set("levelNumber",byPassBean.getLevelNumber());
        dynaApproveProposalForm.set("updateTimeStamp",byPassBean.getUpdateTimeStamp());
        dynaApproveProposalForm.set("stopNumber",byPassBean.getStopNumber());
        dynaApproveProposalForm.set("userId",byPassBean.getUserId());
        dynaApproveProposalForm.set("avUpdateTimestamp",prepareTimeStamp().toString());
        vecByPassProposal.addElement(dynaApproveProposalForm);
        
        updateApproveProposal(vecByPassProposal ,BYPASSS_ACTION, APPROVE_NOT_ALL, 
                        dynaApproveProposalForm, request, response);
    }
    
    /**
     *This method gets the proposal approval for Approval status TO BE SUBMITTED
     * @throws Exception
     * @return
     */
    public Vector getPropApprovalForStatus(HttpServletRequest request) throws Exception{
        HashMap hmPropApproval = new HashMap();
        HttpSession session = request.getSession();
        WebTxnBean webTxnBean = new WebTxnBean();
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        String userId = userInfoBean.getUserId();
        hmPropApproval.put(PROPOSAL_NUMBER , (String)session.getAttribute(PROPOSAL_NUMBER+session.getId()));
        hmPropApproval.put("userId",userId.toUpperCase());
        hmPropApproval.put("approvalStatus","T");
        hmPropApproval.put("primaryApprover",PRIMARY_APPROVER_FLAG);
        Hashtable htPropApproval =
        (Hashtable)webTxnBean.getResults(request, GET_PROP_APPROVAL_FOR_STATUS , hmPropApproval);
        Vector vecPropApproval = (Vector)htPropApproval.get(GET_PROP_APPROVAL_FOR_STATUS);
        return vecPropApproval;
    }
    
    
    /**Thhis method updates the comments and approval status in
     * the table osp$eps_prop_Approval
     * @param vecApproveProposal
     * @param action
     * @param approveAll
     * @throws Exception
     */
    
    public void updateApproveProposal(Vector vecApproveProposal, String action , String approveAll, 
        DynaValidatorForm dynaApproveProposalForm, HttpServletRequest request,
        HttpServletResponse response)throws Exception{
            HttpSession session = request.getSession();
        WebTxnBean webTxnBean = new WebTxnBean();
        if(vecApproveProposal!=null && vecApproveProposal.size() > 0){
            for(int index = 0 ;index < vecApproveProposal.size() ; index++){
                DynaValidatorForm dynaPropApprStatus =
                (DynaValidatorForm)vecApproveProposal.get(index);
                dynaPropApprStatus.set("comments" ,dynaApproveProposalForm.get("comments"));
                dynaPropApprStatus.set("avUpdateTimestamp",prepareTimeStamp().toString());
                
                //server call to update the comments
                webTxnBean.getResults(request , UPDATE_APPROVAL_COMMENTS , dynaPropApprStatus);
                
                dynaPropApprStatus.set("action" , action);
                dynaPropApprStatus.set("approveAll",approveAll );
                
                //server call to update the APPROVE_STATUS
                Hashtable htApproveProposal =
                    (Hashtable)webTxnBean.getResults(request , APPROVE_PROPOSAL_ACTION , dynaPropApprStatus);
                
                HashMap hmApproveProposal = (HashMap)htApproveProposal.get(APPROVE_PROPOSAL_ACTION);
                String approveSuccess = (String)hmApproveProposal.get(APPROVE_SUCCESS);
                updateProposalStatus(approveSuccess, request, response);
                String proposalNumber = (String)session.getAttribute(PROPOSAL_NUMBER+session.getId());
                // Update child proposal status to the DB
                updateChildStatus(request,proposalNumber);
            }
            
        }
    }
        
        
    /**This method updates the proposal status in osp$eps_proposal
     * @param approvalSuccess
     * @throws Exception
     */
    public void updateProposalStatus(String approvalSuccess, 
        HttpServletRequest request, HttpServletResponse response) throws Exception{
        int  approvalValue = Integer.parseInt(approvalSuccess);
        HttpSession session = request.getSession();
        WebTxnBean webTxnBean = new WebTxnBean();        
        HashMap hmProposalStatus = new HashMap();
        hmProposalStatus.put(PROPOSAL_NUMBER, session.getAttribute(PROPOSAL_NUMBER+session.getId()));
        if(approvalValue == 3 ){//Proposal has been approved at last stop.  User does not have submit right
            hmProposalStatus.put(CREATION_STATUS_CODE, new Integer(4));
            webTxnBean.getResults(request , UPDATE_PROPOSAL_STATUS , hmProposalStatus);
        }
        else if(approvalValue == 2 || approvalValue == 5) {
            //update status to approved, and show message to submit through coeus application
            hmProposalStatus.put(CREATION_STATUS_CODE, new Integer(4));
            webTxnBean.getResults(request , UPDATE_PROPOSAL_STATUS , hmProposalStatus);
            // Perform submit to Sponsor action
            
            String url = "/submitSponsor.do?statusCode="+approvalValue;
            RequestDispatcher dispatcher = request.getRequestDispatcher(url);
            dispatcher.forward(request, response);
            
        }
        else if(approvalValue == 4) {
            //update status to submitted.
            hmProposalStatus.put(CREATION_STATUS_CODE, new Integer(5));
            webTxnBean.getResults(request , UPDATE_PROPOSAL_STATUS , hmProposalStatus);
        }
    }
     /**
     *
     * @throws Exception
     * @return
     */
    public Vector performApproveProposalAction(HttpServletRequest request)throws Exception{
        HttpSession session = request.getSession();
        String proposalNumber = (String)session.getAttribute(PROPOSAL_NUMBER+session.getId());
        Vector vecPropApprovalMaps = getProposalApprovalMaps(proposalNumber, request);
        HashMap hmData = new HashMap();
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        String userId = userInfoBean.getUserId();
        Vector vecPropApprovalStatus = null ;
        Vector vecMap = null ;
        if(vecPropApprovalMaps!=null && vecPropApprovalMaps.size() > 0){
            vecMap = new Vector();
            for(int index = 0 ; index < vecPropApprovalMaps.size() ; index ++){
                DynaBean dynaPropApprovalMaps = (DynaBean)vecPropApprovalMaps.get(index);
                hmData.put(PROPOSAL_NUMBER , dynaPropApprovalMaps.get(PROPOSAL_NUMBER) );
                hmData.put("userId" , userId );
                hmData.put(MAP_ID , dynaPropApprovalMaps.get(MAP_ID) );
                vecPropApprovalStatus = getProposalApprovalStatus(hmData, request);
                vecMap.add(vecPropApprovalStatus);
            }
        }
        return vecMap ;
    }
    
    
    /**This method is to get the Approval Maps for the particular proposalNumber
     * @param proposalNumber
     * @throws Exception
     * @return Vector of dynaBeans
     */
    public Vector getProposalApprovalMaps(String proposalNumber, HttpServletRequest request) throws Exception{
        HashMap hmData = new HashMap();
        hmData.put(PROPOSAL_NUMBER,proposalNumber);
        WebTxnBean webTxnBean = new WebTxnBean();         
        Hashtable htPropApprovalMaps =
        (Hashtable)webTxnBean.getResults(request,GET_PROP_APPROVAL_MAPS,hmData);
        Vector vecPropApprovalMaps =
        (Vector)htPropApprovalMaps.get(GET_PROP_APPROVAL_MAPS);
        return vecPropApprovalMaps ;
    }
    
    /**
     * This method is to get the Proposal Approval Status
     * @param hmPropStatus
     * @throws Exception
     * @return Vector of dynaBeans
     */
    public Vector getProposalApprovalStatus(HashMap hmPropStatus, HttpServletRequest request)throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();         
        Hashtable htPropApprovalStatus =
        (Hashtable)webTxnBean.getResults(request,GET_PROP_APPROVAL_STATUS,hmPropStatus);
        Vector vecPropApprovalStatus =
        (Vector)htPropApprovalStatus.get(GET_PROP_APPROVAL_STATUS);
        return vecPropApprovalStatus ;
    }
    
    private Vector prepareApproveBean(Vector vecApproveProposal, HttpServletRequest request)throws Exception{
        CoeusVector cvCustomBeanData = null ;
        HttpSession session = request.getSession();
        String dynaUser = null;
        String dynaStatus = null;
        String userId = ((UserInfoBean)session.getAttribute("user"+session.getId())).getUserId();
        Vector vecApproveFilterd= new Vector();
        if(vecApproveProposal!=null && vecApproveProposal.size() > 0){
            for(int index = 0 ;index < vecApproveProposal.size();index++ ){
                Vector vecApproveMap = (Vector)vecApproveProposal.get(index);
                if(vecApproveMap != null && vecApproveMap.size() > 0){
                    for(int aIndex = 0; aIndex < vecApproveMap.size(); aIndex ++){
                        DynaValidatorForm dynaForm = (DynaValidatorForm)vecApproveMap.get(aIndex);
                        dynaUser = (String)dynaForm.get("userId");
                        dynaStatus = (String)dynaForm.get("approvalStatus");
                        if(userId.equalsIgnoreCase(dynaUser) && WAITING_FOR_APPROVAL.equalsIgnoreCase(dynaStatus)){
                            vecApproveFilterd.add(dynaForm);
                        }
                    }
                }
                
            }
        }
        return vecApproveFilterd;
    }
    
    
}

