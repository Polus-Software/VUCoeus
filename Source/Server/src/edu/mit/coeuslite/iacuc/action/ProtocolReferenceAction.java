/*
 * ProtocolReferenceAction.java
 *
 * Created on September 12, 2006, 11:06 AM
 */
/* PMD check performed, and commented unused imports and variables on 28-JULY-2010
 * by MD.Ehtesham Ansari
 */

package edu.mit.coeuslite.iacuc.action;

import edu.mit.coeus.bean.UserInfoBean;
import java.util.Vector;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import edu.mit.coeuslite.utils.CoeusLiteConstants;
import edu.mit.coeuslite.utils.CoeusliteMenuItems;
import edu.mit.coeuslite.utils.DateUtils;
import edu.mit.coeuslite.utils.LockBean;
import java.util.HashMap;
import java.util.Hashtable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;
import edu.mit.coeuslite.utils.WebUtilities;
import java.util.Map;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

/**
 *
 * @author  divyasusendran
 */
public class ProtocolReferenceAction extends ProtocolBaseAction {
    
//    private HttpServletRequest request;
//    private HttpServletResponse response;
//    private HttpSession session;
//    private WebTxnBean webTxnBean ;
    private static final String EMPTY_STRING = "";
    private static final String PROTOCOL_NUMBER = "protocolNumber";
    private static final String SEQUENCE_NUMBER = "sequenceNumber";
    private static final String INSERT = "I";
    private static final String DELETE = "D";
    private static final String DATE_SEPARATERS = ":/.,|-";
    private static final String SIMPLE_DATE_FORMAT = "MM/dd/yyyy";
    private static final String GET_REFERENCE_ACTION = "/getIacucReferenceAction";
    
    /** Creates a new instance of ProtocolReferenceAction */
    public ProtocolReferenceAction() {
    }
    
    public void cleanUp() {
    }
    
    /**
     *  Method to perform actions
     * @param actionMapping
     * @param actionForm
     * @param request
     * @param response
     * @throws Exception
     * @return
     */    
    public ActionForward performExecute(ActionMapping actionMapping, ActionForm actionForm,
    HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        HttpSession session = request.getSession();
        String protocolNumber =EMPTY_STRING;
        String sequenceNumber = EMPTY_STRING;
        DynaValidatorForm protoReferenceForm = (DynaValidatorForm) actionForm;
        Map mapMenuList = new HashMap();
        mapMenuList.put("menuItems",CoeusliteMenuItems.IACUC_MENU_ITEMS);
        mapMenuList.put("menuCode",CoeusliteMenuItems.IACUC_PROTOCOL_REFERENCE_MENU); 
        setSelectedMenuList(request, mapMenuList); 
        boolean isAllDeleted = false;
        if(session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId()) != null &&
        session.getAttribute(CoeusLiteConstants.IACUC_SEQUENCE_NUMBER+session.getId()) != null ){
            protocolNumber =(String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId());
            sequenceNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_SEQUENCE_NUMBER+session.getId());
        }
        protoReferenceForm.set(PROTOCOL_NUMBER ,protocolNumber);
        protoReferenceForm.set(SEQUENCE_NUMBER ,sequenceNumber);
        if(actionMapping.getPath().equals(GET_REFERENCE_ACTION)){
            getProtocolReferenceData(request);
        }else {
             // Check if lock exists or not
            UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
            LockBean lockBean = getLockingBean(userInfoBean, (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId()), request);
            boolean isLockExists = isLockExists(lockBean, lockBean.getModuleKey());
            LockBean lockData = getLockedData(CoeusLiteConstants.IACUC_PROTO_LOCK_STR+lockBean.getModuleNumber(),request);
            if(!isLockExists && lockBean.getSessionId().equals(lockData.getSessionId())) {            
                addUpdateProtocolReference(protoReferenceForm,request);
            } else {
                String errMsg = "release_lock_for";
                ActionMessages messages = new ActionMessages();                
                messages.add("errMsg", new ActionMessage(errMsg,lockBean.getModuleKey(),lockBean.getModuleNumber()));
                saveMessages(request, messages);
            }
        }
        Vector vecReferenceList = getProtocolReferenceList(protoReferenceForm, request);
        if((vecReferenceList==null || vecReferenceList.size()==0)) {
            isAllDeleted =true;
            updateIndicators(isAllDeleted,request);        
        }
        session.setAttribute("iacucReferenceList" , vecReferenceList );
        protoReferenceForm = resetFormValues(protoReferenceForm);
        readSavedStatus(request);
        return actionMapping.findForward("success");
    }
    
    
    private void getProtocolReferenceData(HttpServletRequest request) throws Exception{
        HttpSession session = request.getSession();
        Vector vecProtocolReferenceTypes = getProtoReferenceTypes(request);
        if(vecProtocolReferenceTypes != null && vecProtocolReferenceTypes.size() > 0){
            session.setAttribute("iacucReferenceTypes", vecProtocolReferenceTypes);
        }        
    }
    
    /**
     * This method is used to retrieve the list of  Reference types 
     * @throws Exception
     * @return Vector of reference type description along with
     *  reference description
     */
    private Vector getProtoReferenceTypes(HttpServletRequest request) throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        Vector vecReferenceTypes = new Vector();
        Hashtable htRefTypes = (Hashtable)webTxnBean.getResults(request,"iacucReferenceTypes",null);
        vecReferenceTypes = (Vector)htRefTypes.get("iacucReferenceTypes");
        vecReferenceTypes = (vecReferenceTypes != null ? vecReferenceTypes : new Vector());
        return vecReferenceTypes;
    }
    
   
    /**
     *  This method is used to insert and delete the references for
     *  a particular protocol number and setting the corresponding 
     *  indicator
     * @param protoReferenceForm
     * @throws Exception
     */    
    private void addUpdateProtocolReference(DynaValidatorForm protoReferenceForm, HttpServletRequest request) throws Exception{        
        int maxReferenceNo = 0;
        HttpSession session = request.getSession();
        WebTxnBean webTxnBean  = new WebTxnBean();
        String protocolNumber = (String)protoReferenceForm.get(PROTOCOL_NUMBER);
        String sequenceNumber = (String)protoReferenceForm.get(SEQUENCE_NUMBER);
        String acType = (String)protoReferenceForm.get("acType");
        String comments = (String)protoReferenceForm.get("comments");
        // date validation for Application and Approval Date - start
        DateUtils dateUtils = new DateUtils();
        String tempApplicationDate = ((String)protoReferenceForm.get("applicationDate")).trim();
        String tempApprovalDate = ((String)protoReferenceForm.get("approvalDate")).trim();
        String tempExpirationDate = ((String)protoReferenceForm.get("expirationDate")).trim();
        if(tempApplicationDate != null && !tempApplicationDate.equals(EMPTY_STRING)){
        tempApplicationDate = dateUtils.formatDate(tempApplicationDate,DATE_SEPARATERS,SIMPLE_DATE_FORMAT); 
        protoReferenceForm.set("applicationDate",tempApplicationDate);
        }
        if(tempApprovalDate != null && !tempApprovalDate.equals(EMPTY_STRING)){
        tempApprovalDate = dateUtils.formatDate(tempApprovalDate,DATE_SEPARATERS,SIMPLE_DATE_FORMAT);
        protoReferenceForm.set("approvalDate",tempApprovalDate);
        }
        //Added for COEUSQA-1724-IACIC -references expiration date implementation -start
        if(tempExpirationDate != null && !tempExpirationDate.equals(EMPTY_STRING)){
        tempExpirationDate = dateUtils.formatDate(tempExpirationDate,DATE_SEPARATERS,SIMPLE_DATE_FORMAT);
        protoReferenceForm.set("expirationDate",tempExpirationDate);
        }
        //Added for COEUSQA-1724-IACIC -references expiration date implementation -end
        //date validation for Application, Expiration date and Approval Date - end
        if(acType.equals(INSERT)){
            if((protocolNumber != null && !(protocolNumber.equals(EMPTY_STRING))) && 
                (sequenceNumber != null && !(sequenceNumber.equals(EMPTY_STRING)))){
                maxReferenceNo = getReferenceNumber(protocolNumber,sequenceNumber, request); 
            }
            protoReferenceForm.set("referenceNumber",new Integer(maxReferenceNo+1));
            protoReferenceForm.set("comments",comments.trim());
            protoReferenceForm.set("awProtocolNumber",protocolNumber);
            protoReferenceForm.set("awSequenceNumber",sequenceNumber);
            protoReferenceForm.set("awReferenceNumber",new Integer(maxReferenceNo));
            protoReferenceForm.set("updateTimestamp" , prepareTimeStamp().toString());
            protoReferenceForm.set("awUpdateTimestamp",prepareTimeStamp().toString());
        }
        else if(acType.equals(DELETE)){
          protoReferenceForm.set("referenceTypeCode" ,"0");
          protoReferenceForm.set("updateTimestamp" , prepareTimeStamp().toString());   
        }
        if(acType != null && !"".equals(acType)){
            webTxnBean.getResults(request,"updateIacucProtoReference",protoReferenceForm);
        }
        if(acType.equals(INSERT)){
            boolean isAllDeleted = false;
            updateIndicators(isAllDeleted, request);            
        }
    }
    
    /**
     *  This method is used  for getting the maximum reference number available
     *  for a particular protocol number and sequence
     * @param protocolNumber
     * @param sequenceNumber
     * @throws Exception
     * @return int protocolReferenceNumber
     */    
    private int getReferenceNumber(String protocolNumber, String sequenceNumber, HttpServletRequest request) throws Exception{
        HashMap hmReferenceNumber = new HashMap();
        WebTxnBean webTxnBean = new WebTxnBean();
        hmReferenceNumber.put(PROTOCOL_NUMBER, protocolNumber);
        hmReferenceNumber.put(SEQUENCE_NUMBER, sequenceNumber);
        Hashtable  htReferenceNumber = (Hashtable)webTxnBean.getResults(request, "iacucReferenceNumber", hmReferenceNumber);
        HashMap hmTemp = (HashMap)htReferenceNumber.get("iacucReferenceNumber");
        int referenceNum  = Integer.parseInt((String)hmTemp.get("PROTOCOL_REFERENCE_NUMBER"));
        return (referenceNum) ;
    }
    
    /**
     *Gets the list of References for the given protocol number
     * and sequence number
     * @param protoReferenceForm
     * @throws Exception
     * @return Vector containing all the references
     */    
    private Vector getProtocolReferenceList(DynaValidatorForm protoReferenceForm, HttpServletRequest request) throws Exception {        
        String protocolNumber = (String)protoReferenceForm.get(PROTOCOL_NUMBER);
        String sequenceNumber = (String)protoReferenceForm.get(SEQUENCE_NUMBER);
        HashMap hmReferenceList = new HashMap();        
        WebTxnBean webTxnBean = new WebTxnBean();
        hmReferenceList.put(PROTOCOL_NUMBER , protocolNumber );
        hmReferenceList.put(SEQUENCE_NUMBER , sequenceNumber );
        Hashtable htReferenceList = (Hashtable)webTxnBean.getResults(request, "getIacucReferenceList", hmReferenceList );
        Vector vecReferenceList =  (Vector)htReferenceList.get("getIacucReferenceList");
        return (vecReferenceList != null ? vecReferenceList : new Vector() );
    }
    
     /**
     * To set the indicator flag for protocol Reference to the database
     * @param isAllDeleted
     * @throws Exception
     */    
    private void updateIndicators(boolean isAllDeleted, HttpServletRequest request) throws Exception{
        HttpSession session = request.getSession();
        WebTxnBean webTxnBean = new WebTxnBean();
        Object data = session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_INDICATORS);
        HashMap hmIndicatorMap = (HashMap)data;
        WebUtilities utils =  new WebUtilities();
        HashMap processedIndicator= (HashMap)utils.processIndicator(hmIndicatorMap, CoeusLiteConstants.REFERENCE_INDICATOR, isAllDeleted);
        String processFundingIndicator = (String)processedIndicator.get(CoeusLiteConstants.REFERENCE_INDICATOR);
        session.setAttribute(CoeusLiteConstants.IACUC_PROTOCOL_INDICATORS, processedIndicator);
        HashMap hashMap = new HashMap();
        hashMap.put(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER,session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId()));
        hashMap.put(CoeusLiteConstants.IACUC_SEQUENCE_NUMBER,session.getAttribute(CoeusLiteConstants.IACUC_SEQUENCE_NUMBER+session.getId()));
        hashMap.put(CoeusLiteConstants.FIELD,CoeusLiteConstants.REFERENCE_INDICATOR_VALUE);
        hashMap.put(CoeusLiteConstants.INDICATOR,processFundingIndicator);        
        hashMap.put(CoeusLiteConstants.KEY_STUDY_TIMESTAMP, prepareTimeStamp().toString());
        hashMap.put(CoeusLiteConstants.USER,((UserInfoBean)session.getAttribute("user"+session.getId())).getUserId());
        webTxnBean.getResults(request, "updateIacucIndicator", hashMap);
    }
    
    
    /**
     * Resetting the Form values
     * @param protoReferenceForm
     * @throws Exception
     * @return DynaValidatorForm
     */    
    private DynaValidatorForm resetFormValues(DynaValidatorForm protoReferenceForm)throws Exception {
        protoReferenceForm.set("referenceTypeCode", EMPTY_STRING);
        protoReferenceForm.set("referenceKey", EMPTY_STRING);
        protoReferenceForm.set("applicationDate", EMPTY_STRING);
        protoReferenceForm.set("approvalDate", EMPTY_STRING);
        //Added for COEUSQA-1724-IACIC -references expiration date implementation - start
        protoReferenceForm.set("expirationDate", EMPTY_STRING);
        //Added for COEUSQA-1724-IACIC -references expiration date implementation - end
        protoReferenceForm.set("comments", EMPTY_STRING);
        return protoReferenceForm;
    }
    
}
