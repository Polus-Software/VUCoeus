/*
 * ProposalGetSpRevAction.java
 *
 * Aug 9, 2006
 * UTK
 */

/* PMD check performed, and commented unused imports and
 * variables on 13-JULY-2011 by Bharati
 */

package edu.utk.coeuslite.propdev.action;

//import edu.mit.coeus.bean.PersonInfoBean;
//import edu.mit.coeus.bean.UserInfoBean;
//import edu.mit.coeus.exception.CoeusException;
//import edu.mit.coeuslite.utils.ComboBoxBean;
//import edu.mit.coeuslite.utils.bean.MenuBean;
//import edu.mit.coeus.utils.CoeusFunctions;
//import edu.mit.coeus.utils.TypeConstants;
//import edu.mit.coeus.utils.dbengine.DBEngineImpl;
//import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.bean.SpecialReviewFormBean;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentTxnBean;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeuslite.utils.CoeusLiteConstants;
import edu.mit.coeuslite.utils.CoeusliteMenuItems;
import edu.mit.coeuslite.utils.bean.WebTxnBean;

import edu.mit.coeuslite.utils.DateUtils;

//import java.io.IOException;
//import java.sql.Timestamp;
//import java.util.ArrayList;
//import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;
//import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
//import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
//import org.apache.struts.action.ActionMessage;
//import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.validator.DynaValidatorForm;
import edu.mit.coeus.utils.ModuleConstants;
import edu.mit.coeuslite.utils.LockBean;
import edu.mit.coeuslite.utils.SearchModuleBean;
import edu.utk.coeuslite.propdev.bean.EPSProposalHeaderBean;

public class ProposalGetSpRevAction extends ProposalBaseAction{
    private static final String PROPOSAL_SPECIAL_REVIEW = "getPropDevSpecialReview";
    private static final String SAVE_MENU = "P005";
    private static final String EMPTY_STRING ="";
    private static final String PROPOSAL_NUMBER="proposalNumber";
    private static final String GET_SPECIAL_REVIEW_CODE="getSpeciaReviewCode";
    private static final String GET_REVIEW_APPROVAL_TYPE = "getReviewApprovalType";
    private static final String GET_PROPOSAL_SPECIAL_REVIEW="getProposalSpecialReview";
    
    private static final String SPECIAL_REVIEW_CODE = "P018";
    
    //COEUSQA-2984 : Statuses in special review - start
    private static final int PROPOSAL_SUBMITTED_STATUS = 5;
    private static final String PROPOSAL_DETAILS_DATA = "getProposalSummaryDetails";
    private static final String PROTOCOL_NUMBER = "spRevProtocolNumber";
    private static final String GET_PROP_PROTOCOL_STATUS = "getPropProtocolStatus";
    //COEUSQA-2984 : Statuses in special review - end
    
    //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - start
    private static final String IACUC_PARAMETER_NAME = "ENABLE_IACUC_TO_DEV_PROPOSAL_LINK";
    //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - end
    
    private DateUtils dateUtils = new edu.mit.coeuslite.utils.DateUtils();
    //Added for case#2990 - Proposal Special Review Enhancement - start
    private static final String PARAMETER_NAME = "ENABLE_PROTOCOL_TO_DEV_PROPOSAL_LINK";
    private static final String GET_PARAMETER_VALUE = "getProtocolToDevPropLink";
    private static final String PROTOCOL_RIGHT = "CREATE_PROTOCOL";
    //Added for case#2990 - Proposal Special Review Enhancement - end
       
    /** Creates a new instance of ProposalGetSpRevAction */
    public ProposalGetSpRevAction() { 
    }
    
    public org.apache.struts.action.ActionForward performExecute(ActionMapping mapping, ActionForm form,
    HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        // set the selected status for the proposal menu
        Map mapMenuList = new HashMap();
        mapMenuList.put("menuItems", CoeusliteMenuItems.PROPOSAL_MENU_ITEMS);
        mapMenuList.put("menuCode", CoeusliteMenuItems.SPECIAL_REVIEW_CODE); 
        setSelectedMenuList(request, mapMenuList);
        
        WebTxnBean webTxnBean = new WebTxnBean();
        HttpSession session = request.getSession();
        
        HashMap hmpProposalData = new HashMap();
        
        ActionForward actionForward = null;
        String proposalNumber  = (String)session.getAttribute(CoeusLiteConstants.PROPOSAL_NUMBER+session.getId());       
        hmpProposalData.put(PROPOSAL_NUMBER,proposalNumber);  
        // COEUSQA-2320 Show in Lite for Special Review in Code table
        // Add module code to the map.
        hmpProposalData.put("moduleCode", ModuleConstants.PROPOSAL_DEV_MODULE_CODE);
        
        //Added for case#2990 - Proposal Special Review Enhancement - start
        String enableProtocolToDevPropLink = getParameterValue(request,"IRB" );
        if(enableProtocolToDevPropLink != null){
            session.setAttribute("enableProposalToProtocolLink", enableProtocolToDevPropLink);
        }
        //Added for case#2990 - Proposal Special Review Enhancement - end 
        
        //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - start
        //get the value for the ENABLE_IACUC_TO_DEV_PROPOSAL_LINK parameter
        String enableIacucProtocolToDevPropLink = getParameterValue(request,"IACUC" );
        if(enableIacucProtocolToDevPropLink != null){
            session.setAttribute("enableIacucProtocolToDevPropLink", enableIacucProtocolToDevPropLink);
        }
        //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - end
        
        // get specail review data
        actionForward = getSpecialReview(mapping,request,hmpProposalData);
         
        readSavedStatus(request);
        //Added for COEUSDEV-1144 : Unable to edit Special review comments RT #2022030 - Start
        editSpecialReview(form,request);
        //Added for COEUSDEV-1144 : Unable to edit Special review comments RT #2022030 - End
        int hasRight =checkProtocolRight(request);

        if(hasRight == 1) {
            request.setAttribute("hasRight", true);
        }else {
            request.setAttribute("hasRight", false);
        }
        //Added to hide special review section when proposal is open in edit mode in premium
        // This is done for FORMS-E PHS Human Subject 
        if(session.getAttribute("PHSHumanFromPremium"+proposalNumber+ session.getId())!= null){
            UserInfoBean userInfoBean = (UserInfoBean) request.getSession().getAttribute("user" + request.getSession().getId());
            boolean isValid = prepareLock(userInfoBean,proposalNumber,request);
            if(!isValid){
            session.setAttribute("mode"+session.getId(),"display");
            } 
        }
        return actionForward;
        
    }

    /*To get the SpecialReview data*/
    private ActionForward getSpecialReview(ActionMapping mapping,HttpServletRequest request,HashMap hmpProposalData) throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htSpecialReview = 
          (Hashtable)webTxnBean.getResults(request,PROPOSAL_SPECIAL_REVIEW,hmpProposalData);
        HttpSession session = request.getSession();
        // COEUSQA-2320 Show in Lite for Special Review in Code table - Start
        // Filter the special reivew entries based on show in lite flag.
        Vector vecSpecialReviewDetails = (Vector) htSpecialReview.get(GET_SPECIAL_REVIEW_CODE);
        Vector vecFilteredData = new Vector();
        for(int index=0; index<vecSpecialReviewDetails.size(); index++){
            DynaValidatorForm form = (DynaValidatorForm) vecSpecialReviewDetails.get(index);
            if("Y".equals(form.get("showInLite"))){
                vecFilteredData.add(form);
            }
        }
        // session.setAttribute("splReview", htSpecialReview.get(GET_SPECIAL_REVIEW_CODE));
        session.setAttribute("splReview", vecFilteredData);
        // COEUSQA-2320 Show in Lite for Special Review in Code table - End
        
        session.setAttribute("approval", htSpecialReview.get(GET_REVIEW_APPROVAL_TYPE));
        Vector htPropDevSpRev = (Vector)htSpecialReview.get(GET_PROPOSAL_SPECIAL_REVIEW);
        if(htPropDevSpRev!= null && htPropDevSpRev.size() >0){
            for(int i=0;i<htPropDevSpRev.size();i++){
                DynaValidatorForm dynaValidatorForm = (DynaValidatorForm)htPropDevSpRev.get(i);
                String applDate = (String)dynaValidatorForm.get("applicationDate");
                String approvalDate = (String)dynaValidatorForm.get("approvalDate");
                if(applDate != null){
                    String value = dateUtils.formatDate(applDate,edu.mit.coeuslite.utils.DateUtils.MM_DD_YYYY);
                    dynaValidatorForm.set("applicationDate",value);
                }else{
                    dynaValidatorForm.set("applicationDate","");
                }
                if(approvalDate != null){
                    String dateValue = dateUtils.formatDate(approvalDate,edu.mit.coeuslite.utils.DateUtils.MM_DD_YYYY);
                    dynaValidatorForm.set("approvalDate",dateValue);
                   
                }else{
                    dynaValidatorForm.set("approvalDate","");
                }
                //Added for case#2990 - Proposal Special Review Enhancement - start
                String specialReviewCode = (String)dynaValidatorForm.get("specialReviewCode");
                if(specialReviewCode.equals("1")){
                    String protocolNumber = (String)dynaValidatorForm.get("spRevProtocolNumber");
                    
                    //COEUSQA-2984 : Statuses in special review - start
                    UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
                    ProposalDevelopmentTxnBean proposalDevelopmentTxnBean = new ProposalDevelopmentTxnBean();
                    String proposalNumber  = (String)session.getAttribute(CoeusLiteConstants.PROPOSAL_NUMBER+session.getId());
                    HashMap mapData = new HashMap();
                    mapData.put(PROPOSAL_NUMBER, proposalNumber);
                    Hashtable htProposalData = (Hashtable)webTxnBean.getResults(request, PROPOSAL_DETAILS_DATA, mapData);
                    Vector dataObject = (Vector)htProposalData.get(PROPOSAL_DETAILS_DATA);
                    //Modified for COEUSQA-3377 : Link to IACUC malfunctioning in Premium Proposal Dev - start
                    //DynaValidatorForm specialRevForm = (DynaValidatorForm)dataObject.get(3);
                    DynaValidatorForm specialRevForm = (DynaValidatorForm)dataObject.get(0);
                    //Added for COEUSQA-3377 : Link to IACUC malfunctioning in Premium Proposal Dev - end
                    String creationStatusCode = (String)specialRevForm.get("creationStatusCode");
                    int creationStatusCodeNew = Integer.parseInt(creationStatusCode);
                    Integer specialReviewNumber = (Integer)dynaValidatorForm.get("specialReviewNumber");
                    String spRevProtocolNumber = protocolNumber;
                    if(spRevProtocolNumber != null && !spRevProtocolNumber.equals("")){
                        //If proposal is in submitted status then display the special review status from OSP$EPS_PROP_SPECIAL_REVIEW which is captured during
                        if(PROPOSAL_SUBMITTED_STATUS == creationStatusCodeNew){
                            SpecialReviewFormBean specialReviewFormBean = proposalDevelopmentTxnBean.getPropProtocolStatus(proposalNumber,spRevProtocolNumber,specialReviewNumber);
                            //specialReviewFormBean returns protocol status code & its description if it is captured 
                            //If protocol status code is not captured then it should display the current status 
                            if(specialReviewFormBean != null){
                                dynaValidatorForm.set("approvalDescription", specialReviewFormBean.getProtocolStatusDescription());
                            }else{
                                String protocolStatusDesc = getProtocolStatus(request, spRevProtocolNumber);
                                if(protocolStatusDesc != null && !protocolStatusDesc.equals("")){
                                    dynaValidatorForm.set("approvalDescription", protocolStatusDesc);
                                }
                            }
                        }else{
                            String protocolStatusDesc = getProtocolStatus(request, spRevProtocolNumber);
//                            if(protocolStatusDesc != null && !protocolStatusDesc.equals("")){
//                                dynaValidatorForm.set("approvalDescription", protocolStatusDesc);
//                            }
                        }
                    }
                    //COEUSQA-2984 : Statuses in special review - end
                }
                //Added for case#2990 - Proposal Special Review Enhancement - end
                //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - start
                else if(specialReviewCode.equals("2")){
                    String protocolNumber = (String)dynaValidatorForm.get("spRevProtocolNumber");
                    
                    UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
                    ProposalDevelopmentTxnBean proposalDevelopmentTxnBean = new ProposalDevelopmentTxnBean();
                    String proposalNumber  = (String)session.getAttribute(CoeusLiteConstants.PROPOSAL_NUMBER+session.getId());
                    HashMap mapData = new HashMap();
                    mapData.put(PROPOSAL_NUMBER, proposalNumber);
                    Hashtable htProposalData = (Hashtable)webTxnBean.getResults(request, PROPOSAL_DETAILS_DATA, mapData);
                    Vector dataObject = (Vector)htProposalData.get(PROPOSAL_DETAILS_DATA);
                    //Modified for COEUSQA-3377 : Link to IACUC malfunctioning in Premium Proposal Dev - start
                    //DynaValidatorForm specialRevForm = (DynaValidatorForm)dataObject.get(3);
                    DynaValidatorForm specialRevForm = (DynaValidatorForm)dataObject.get(0);
                    //Modified for COEUSQA-3377 : Link to IACUC malfunctioning in Premium Proposal Dev - end
                    String creationStatusCode = (String)specialRevForm.get("creationStatusCode");
                    int creationStatusCodeNew = Integer.parseInt(creationStatusCode);
                    Integer specialReviewNumber = (Integer)dynaValidatorForm.get("specialReviewNumber");
                    String spRevProtocolNumber = protocolNumber;
                    if(spRevProtocolNumber != null && !spRevProtocolNumber.equals("")){
                        //If proposal is in submitted status then display the special review status from OSP$EPS_PROP_SPECIAL_REVIEW which is captured during
                        if(PROPOSAL_SUBMITTED_STATUS == creationStatusCodeNew){
                            SpecialReviewFormBean specialReviewFormBean = proposalDevelopmentTxnBean.getPropIacucProtocolStatus(proposalNumber,spRevProtocolNumber,specialReviewNumber);
                            //specialReviewFormBean returns protocol status code & its description if it is captured 
                            //If protocol status code is not captured then it should display the current status 
                            if(specialReviewFormBean != null){
                                dynaValidatorForm.set("approvalDescription", specialReviewFormBean.getProtocolStatusDescription());
                            }else{
                                String protocolStatusDesc = getIacucProtocolStatus(request, spRevProtocolNumber);
                                if(protocolStatusDesc != null && !protocolStatusDesc.equals("")){
                                    dynaValidatorForm.set("approvalDescription", protocolStatusDesc);
                                }
                            }
                        }else{
                            String protocolStatusDesc = getIacucProtocolStatus(request, spRevProtocolNumber);
//                            if(protocolStatusDesc != null && !protocolStatusDesc.equals("")){
//                                dynaValidatorForm.set("approvalDescription", protocolStatusDesc);
//                            }
                        }
                    }
                }
                //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - end
            }
        }

        request.getSession().setAttribute("pdReviewList",htPropDevSpRev);
        return mapping.findForward("success");
    }
    
    //Modified for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - start
    //Added for case#2990 - Proposal Special Review Enhancement - start
    /**
     * This method gets the value for the parameter
     * ENABLE_PROTOCOL_TO_DEV_PROPOSAL_LINK from OSP$PARAMETER table
     * and sets its to session scope.
     * @param request HttpServletRequest
     * @throws Exception
     * @return flag String
     */
    private String getParameterValue(HttpServletRequest request,String reqFrom)throws Exception{
        Map hmParameter = new HashMap();
        WebTxnBean webTxnBean = new WebTxnBean();
        String flag = null;
        if("IRB".equals(reqFrom)){
            hmParameter.put("parameterName", PARAMETER_NAME);           
        }else{
            hmParameter.put("parameterName", IACUC_PARAMETER_NAME);            
        }
        Hashtable htParameterValue = (Hashtable)webTxnBean.getResults(request, GET_PARAMETER_VALUE, hmParameter);
        hmParameter = (HashMap)htParameterValue.get(GET_PARAMETER_VALUE);
        flag = (String)hmParameter.get("parameterValue");
        return flag; 
    }
    //Modified for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - end
    
    /**
     * This method gets the protocol status description for a given protocol number
     * @param request HttpServletRequest
     * @param protocolNumber String
     * @throws Exception
     * @return protocolStatusDesc String
     */    
    private String getProtocolStatus(HttpServletRequest request, String protocolNumber) throws Exception{                
        WebTxnBean webTxnBean = new WebTxnBean();
        HashMap hmProtocolDetails = new HashMap();
        hmProtocolDetails.put("protocolNumber", protocolNumber);
        Hashtable htProtocolDetails = (Hashtable)webTxnBean.getResults(request, "getProtocolInfo", hmProtocolDetails);
        Vector vecProtocolDetails = (Vector)htProtocolDetails.get("getProtocolInfo");
        DynaActionForm dynaForm = null;
        String protocolStatusDesc = "";
        if(vecProtocolDetails != null && vecProtocolDetails.size() > 0){
            dynaForm = (DynaActionForm)vecProtocolDetails.get(0);        
            protocolStatusDesc = (String)dynaForm.get("protocolStatusDesc");            
        }
        return protocolStatusDesc;
    }    
    //Added for case#2990 - Proposal Special Review Enhancement - end
    
    //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - start
    /**
     * This method gets the protocol status description for a given protocol number
     * @param request HttpServletRequest
     * @param protocolNumber String
     * @throws Exception
     * @return protocolStatusDesc String
     */
    private String getIacucProtocolStatus(HttpServletRequest request, String protocolNumber) throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        HashMap hmIacucProtocolDetails = new HashMap();
        hmIacucProtocolDetails.put("protocolNumber", protocolNumber);
        Hashtable htIacucProtocolDetails = (Hashtable)webTxnBean.getResults(request, "getIacucInfo", hmIacucProtocolDetails);
        Vector vecProtocolDetails = (Vector)htIacucProtocolDetails.get("getIacucInfo");
        DynaActionForm dynaForm = null;
        String protocolStatusDesc = "";
        if(vecProtocolDetails != null && vecProtocolDetails.size() > 0){
            dynaForm = (DynaActionForm)vecProtocolDetails.get(0);
            protocolStatusDesc = (String)dynaForm.get("protocolStatusDesc");
        }
        return protocolStatusDesc;
    }
    //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - end

    public void cleanUp() {
    }

     public int checkProtocolRight(HttpServletRequest request) throws Exception{
        HttpSession session = request.getSession();
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        String loggedinUser = userInfoBean.getUserId();

        EPSProposalHeaderBean proposalHeaderBean=(EPSProposalHeaderBean)session.getAttribute("epsProposalHeaderBean");
        WebTxnBean webTxnBean = new WebTxnBean();
        HashMap hmDetails = new HashMap();
        hmDetails.put("userId" , loggedinUser);
        hmDetails.put("unitNumber", proposalHeaderBean.getLeadUnitNumber());
        hmDetails.put("rightId" , PROTOCOL_RIGHT);
        int hasRight = 0;

        Hashtable htPersonRight =
        (Hashtable)webTxnBean.getResults(request, "isUserHasRight", hmDetails);
        HashMap hmPersonRight = (HashMap)htPersonRight.get("isUserHasRight");
        if(hmPersonRight !=null && hmPersonRight.size()>0){
            hasRight = Integer.parseInt(hmPersonRight.get("retValue").toString());

        }
        return hasRight;
    }
         /** This method will notify for the acquiring and releasing the Lock
     *based on the way the locks are opened.
     *It will check whether the proposal is opened through search or list
     *Based on the conditions it will acquire the lock and release the lock
     *If it locked then it will prepare the locking messages
     *@param UserInfoBean, ProposalNumber(Current)
     *@throws Exception
     *@returns boolean is locked or not
     */
    private boolean prepareLock(UserInfoBean userInfoBean, String proposalNumber, 
        HttpServletRequest request) throws Exception{
        boolean isSuccess = true;
        HttpSession session = request.getSession();
        WebTxnBean webTxnBean = new WebTxnBean();
        String mode = (String)session.getAttribute("mode"+session.getId());
        SearchModuleBean moduleBean = (SearchModuleBean)session.getAttribute(CoeusLiteConstants.PROPOSAL_SEARCH_ACTION+session.getId());
        LockBean lockBean = null;
        LockBean sessionLockBean = (LockBean)session.getAttribute(CoeusLiteConstants.LOCK_BEAN+session.getId());
    
        // If the action is from search window
        if(moduleBean!= null && !moduleBean.getOldModuleNumber().equals(EMPTY_STRING)){
            
                moduleBean.setMode(getMode(mode));
            // If the current Proposal number is in MODIFY MODE then lock it
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
                    LockBean serverDataBean = getLockedData(CoeusLiteConstants.PROP_DEV_LOCK_STR+lockBean.getModuleNumber(), request);
                    // If the current proposal is locked by other user, then show the message
                    // else lock it
                    if(!isLocked ){
                        /** Check if the same record is locked or not. If not 
                         *then only show the message else discard it
                         */
                        if(!isSessionRowLocked || !serverDataBean.getSessionId().equals(lockBean.getSessionId())){                          
                            isSuccess = false;
                        }// End if for lockeed record in the session
                    }else{// If the record is not locked then go ahead and lock it
                        lockModule(lockBean, request);
                        session.setAttribute(CoeusLiteConstants.RECORD_LOCKED+session.getId(), 
                            new Boolean(true));
                    }
                }
            
        }else{
            // Proposal opened from list
            lockBean = getLockingBean(userInfoBean, proposalNumber,request);
            boolean isLockExists = isLockExists(lockBean, lockBean.getModuleKey());
            LockBean serverDataBean = getLockedData(CoeusLiteConstants.PROP_DEV_LOCK_STR+lockBean.getModuleNumber(), request);
            if(isLockExists) {
                if(serverDataBean!=null && !lockBean.getSessionId().equals(serverDataBean.getSessionId())) {
                    isLockExists = false;
                }
            }

            if(isLockExists && !lockBean.getMode().equals(CoeusLiteConstants.DISPLAY_MODE)) {
                lockModule(lockBean, request);
                session.setAttribute(CoeusLiteConstants.RECORD_LOCKED+session.getId(), 
                            new Boolean(true));
            }else{
                if(sessionLockBean == null && !lockBean.getMode().equals(CoeusLiteConstants.DISPLAY_MODE)){                    
                    isSuccess = false;
                } else if(sessionLockBean!=null && serverDataBean!=null) {
                    if(!lockBean.getSessionId().equals(serverDataBean.getSessionId())) {                       
                        isSuccess = false;                        
                    }
                }
            }
        }
        session.removeAttribute(CoeusLiteConstants.PROPOSAL_SEARCH_ACTION+session.getId());
        return isSuccess;
        
    }
    //Added for COEUSDEV-1144 : Unable to edit Special review comments RT #2022030 - Start
   /**
    * Method called when special review is editted
    * @param form 
    * @param request 
    */
    private void editSpecialReview(ActionForm form,HttpServletRequest request){
        DynaValidatorForm dynaForm = (DynaValidatorForm) form;
        HttpSession session = request.getSession();
        String editMode = request.getParameter("editMode");
        if("true".equalsIgnoreCase(editMode)){
            Vector htPropDevSpRev = (Vector)session.getAttribute("pdReviewList");
            if(htPropDevSpRev != null && !htPropDevSpRev.isEmpty()){
                String specialReviewNumber = request.getParameter("specialReviewNumber");
                int splReviewNumber = 0;
                if(specialReviewNumber != null){
                    splReviewNumber = Integer.parseInt(specialReviewNumber);
                }
                for(Object specialReview : htPropDevSpRev){
                    DynaValidatorForm specialReviewForm = (DynaValidatorForm)specialReview;
                    if(splReviewNumber ==
                            Integer.parseInt(specialReviewForm.get("specialReviewNumber").toString())){
                        dynaForm.set("acType",TypeConstants.UPDATE_RECORD);
                        request.setAttribute("acType",TypeConstants.UPDATE_RECORD);
                        dynaForm.set("spRevProtocolNumber", specialReviewForm.get("spRevProtocolNumber"));
                        dynaForm.set("specialReviewCode", specialReviewForm.get("specialReviewCode"));
                        dynaForm.set("approvalCode", specialReviewForm.get("approvalCode"));
                        dynaForm.set("applicationDate", specialReviewForm.get("applicationDate"));
                        dynaForm.set("approvalDate", specialReviewForm.get("approvalDate"));
                        dynaForm.set("comments", specialReviewForm.get("comments"));
                        dynaForm.set("proposalNumber", specialReviewForm.get("proposalNumber"));
                        dynaForm.set("pdSpTimestamp", specialReviewForm.get("pdSpTimestamp"));
                        dynaForm.set("updateUser", specialReviewForm.get("updateUser"));
                        dynaForm.set("specialReviewDescription", specialReviewForm.get("specialReviewDescription"));
                        dynaForm.set("approvalDescription", specialReviewForm.get("approvalDescription"));
                        dynaForm.set("specialReviewNumber", specialReviewForm.get("specialReviewNumber"));
                        dynaForm.set("tempApprovalCode", specialReviewForm.get("approvalCode"));
                        dynaForm.set("tempApprovalText", specialReviewForm.get("approvalDescription"));
                        dynaForm.set("tempApprovalDate", specialReviewForm.get("applicationDate"));
                        dynaForm.set("tempApplicationDate",  specialReviewForm.get("applicationDate"));
                        
                        break;
                    }
                }
            }
            
        }
    }
   //Added for COEUSDEV-1144 : Unable to edit Special review comments RT #2022030 - End
   
    
}
