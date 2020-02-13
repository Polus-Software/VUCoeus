/*
 * ProtocolNotesAction.java
 *
 * Created on September 12, 2006, 11:50 AM
 */

package edu.mit.coeuslite.irb.action;

import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.irb.bean.ProtocolDataTxnBean;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeuslite.utils.CoeusLiteConstants;
import edu.mit.coeuslite.utils.CoeusliteMenuItems;
import edu.mit.coeuslite.utils.LockBean;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.validator.DynaValidatorForm;

/**
 *
 * @author  nandkumarsn
 */
public class ProtocolNotesAction extends ProtocolBaseAction{
    private static final String GET_PROTOCOL_NOTE = "/getProtocolNotes";
    private static final String SAVE_PROTOCOL_NOTE = "/saveProtocolNotes";
    private final String VIEW_RESTRICTED_PROTOCOL_NOTES = "VIEW_RESTRICTED_NOTES";
    private static final String EMPTY_STRING = "";
//    private HttpServletRequest request;
//    private HttpSession session ;
//    private ActionMapping actionMapping;
//    private ActionForward actionForward;
//    private WebTxnBean webTxnBean ;
    
    private final String ADD_PROTOCOL_NOTES = "ADD_PROTOCOL_NOTES";
    private final String ADD_ANY_PROTOCOL_NOTES = "ADD_ANY_PROTOCOL_NOTES";
    private final String MODIFY_PROTOCOL = "MODIFY_PROTOCOL";
    private final String GET_PROTOCOL_NOTES = "getProtocolNotes";
    private final String PROTOCOL_NOTES_DATA = "protocolNotesData";
    private final String GET_PROTO_NOT_RESTRICTED_NOTES = "getProtoNotRestrictedNotes";
    private final String PROTO_NOT_RESTRICTED_NOTES_DATA = "protoNotRestrictedNotesData";
    private final String GET_MAX_ENTRY_NUMBER = "getMaxEntryNumber";
    private final String MAX_ENTRY_NUMBER = "maxEntryNumber";
    private final String UPDATE_PROTOCOL_NOTES = "updateProtocolNotes";
    private final String PROTOCOL_NUMBER = "protocolNumber";
    private final String SEQUENCE_NUMBER = "sequenceNumber";
    private final String ADD_COMMENTS = "addComments";
    private final String ADD_RESTRICTED_VIEW = "addRestrictedView";
    private final String YES = "Y";
    private final String RELEASE_LOCK_FOR = "release_lock_for";
    
    
    
    /** Creates a new instance of ProtocolNotesAction */
    public ProtocolNotesAction() {
    }
    
    /**
     * This method performs the necessary actions by calling the
     * performProtocolNotesAction() method
     * @param actionMapping
     * @param actionForm
     * @param request
     * @param response
     * @throws Exception
     * @return actionForward
     */
    public ActionForward performExecute(ActionMapping actionMapping, ActionForm actionForm,
    HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        ActionForward actionForward = performProtocolNotesAction(actionForm,request, actionMapping);
        Map mapMenuList = new HashMap();
        mapMenuList.put("menuItems",CoeusliteMenuItems.PROTOCOL_MENU_ITEMS);
        mapMenuList.put("menuCode",CoeusliteMenuItems.PROTOCOL_NOTES_MENU);
        setSelectedMenuList(request, mapMenuList);
        readSavedStatus(request );
        return actionForward;
    }
    
    /**
     * This method will identify which request comes from which path and
     * navigates to the respective ActionForward
     * It also check for the lock before performing save action
     * @returns ActionForward object
     * @param actionForm
     * @throws Exception
     * @return
     */
    
    private ActionForward performProtocolNotesAction(ActionForm actionForm, HttpServletRequest request, 
        ActionMapping actionMapping) throws Exception{
        HttpSession session = request.getSession();
        String navigator = EMPTY_STRING;
        WebTxnBean webTxnBean = new WebTxnBean();
        edu.mit.coeus.bean.UserInfoBean userInfoBean = (edu.mit.coeus.bean.UserInfoBean)session.getAttribute("user"+session.getId());
        String protocolNumber  = (String)session.getAttribute(CoeusLiteConstants.PROTOCOL_NUMBER+session.getId());
        if(actionMapping.getPath().equals(SAVE_PROTOCOL_NOTE)){
            String functionType =  (String)session.getAttribute(CoeusLiteConstants.MODE_DETAILS+session.getId());           
            LockBean lockBean = getLockingBean(userInfoBean, 
                                (String)session.getAttribute(CoeusLiteConstants.PROTOCOL_NUMBER+session.getId()), request);
            boolean isLockExists = isLockExists(lockBean, lockBean.getModuleKey());   
            if(functionType!=null && !functionType.equals(EMPTY_STRING)){
                if(functionType.equalsIgnoreCase("D")){
                    if(isLockExists) {
                        lockProtocol(protocolNumber, request);
                        navigator = performSaveAction(actionForm,request);
                        releaseLock(lockBean, request);
                    }else{
//                        String errMsg = RELEASE_LOCK_FOR;
//                        ActionMessages messages = new ActionMessages();
//                        messages.add("errMsg", new ActionMessage(errMsg,lockBean.getModuleKey(),lockBean.getModuleNumber()));
//                        saveMessages(session, messages);
                        showLockingMessage(lockBean.getModuleNumber(), request);
                    }
                }else{
                    //Check if lock exists or not
                    LockBean lockData = getLockedData(CoeusLiteConstants.PROTOCOL_LOCK_STR+lockBean.getModuleNumber(), request);
                    if(!isLockExists && lockBean.getSessionId().equals(lockData.getSessionId())) {
                        navigator = performSaveAction(actionForm,request);
                    }else{
                        String errMsg = RELEASE_LOCK_FOR;
                        ActionMessages messages = new ActionMessages();
                        messages.add("errMsg", new ActionMessage(errMsg,lockBean.getModuleKey(),lockBean.getModuleNumber()));
                        saveMessages(session, messages);
//                        showLockingMessage(lockBean.getModuleNumber(), request);
                        
                    }
                }
            }
        }
        navigator = getProtocolNotesData(actionForm,request);
        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;   
    }
    
     /**
     * This method retrives protocol notes data corresponding 
     * to a selected protocol number and sequence number from the 
     * table OSP$PROTOCOL_NOTEPAD using the stored procedure
     * GET_PROTOCOL_NOTES
     * @param actionForm
     * @throws Exception
     * @return String to navigator
     */
    private String getProtocolNotesAllData(ActionForm form, HttpServletRequest request) throws Exception{        
        String protocolNumber = EMPTY_STRING;
        String sequenceNumber = EMPTY_STRING;
        HttpSession session = request.getSession();
        WebTxnBean webTxnBean = new WebTxnBean();
        DynaValidatorForm protocolNotesForm = (DynaValidatorForm)form;
        protocolNumber = (String)session.getAttribute(CoeusLiteConstants.PROTOCOL_NUMBER+session.getId());
        sequenceNumber = (String)session.getAttribute(CoeusLiteConstants.SEQUENCE_NUMBER+session.getId());
        Integer seqNum = new Integer(sequenceNumber);
        Map hmProtocolNotes = new HashMap();
        hmProtocolNotes.put(PROTOCOL_NUMBER, protocolNumber);
        hmProtocolNotes.put(SEQUENCE_NUMBER, seqNum);
        hmProtocolNotes =(Hashtable)webTxnBean.getResults(request,GET_PROTOCOL_NOTES, hmProtocolNotes);
        Vector vecProtocolNotesAllData = (Vector) hmProtocolNotes.get(GET_PROTOCOL_NOTES);
        //Modified for COEUSDEV-165 : PI User unable to view notes that are saved in Lite - Start
        //When user has 'VIEW_RESTRICTED_NOTES' right need to display all the notes
        //Added for Coeus4.3 Protocol Notes enhancement for filtering restricted notes - start
//        Vector vecProtocolNotesData = new Vector();
//        if(vecProtocolNotesAllData != null && vecProtocolNotesAllData.size() > 0){
//            for (int index = 0; index < vecProtocolNotesAllData.size(); index++){
//                DynaActionForm dynaForm = (DynaActionForm)vecProtocolNotesAllData.get(index);
//                String restrictedNote = (String)dynaForm.get("restrictedView");
//                if(restrictedNote.equals("N")){
//                    vecProtocolNotesData.add(dynaForm);                
//                }
//            }
//        }
        //Added for Coeus4.3 Protocol Notes enhancement for filtering restricted notes - end
        
//        session.setAttribute(PROTOCOL_NOTES_DATA, vecProtocolNotesData);
         session.setAttribute(PROTOCOL_NOTES_DATA, vecProtocolNotesAllData);
        //COEUSDEV-165 : END
        protocolNotesForm.set(ADD_COMMENTS,EMPTY_STRING);
        protocolNotesForm.set(ADD_RESTRICTED_VIEW,YES);
        return "success";
    }
    
    
    /**
     * This method retrives the not restricted protocol notes data corresponding 
     * to a selected protocol number and sequence number from the 
     * table OSP$PROTOCOL_NOTEPAD using the stored procedure
     * GET_PROTO_NOT_RESTRICTED_NOTES
     * @param actionForm
     * @throws Exception
     * @return String to navigator
     */
    private String getProtoNotRestrictedNotesData(ActionForm form, HttpServletRequest request) throws Exception{
        String protocolNumber = EMPTY_STRING;
        String sequenceNumber = EMPTY_STRING;
        HttpSession session = request.getSession();
        WebTxnBean webTxnBean = new WebTxnBean();
        DynaValidatorForm protocolNotesForm = (DynaValidatorForm)form;
        protocolNumber = (String)session.getAttribute(CoeusLiteConstants.PROTOCOL_NUMBER+session.getId());
        sequenceNumber = (String)session.getAttribute(CoeusLiteConstants.SEQUENCE_NUMBER+session.getId());
        Integer seqNum = new Integer(sequenceNumber);
        Map hmProtoNotRestrictedNotes = new HashMap();
        hmProtoNotRestrictedNotes.put(PROTOCOL_NUMBER, protocolNumber);
        hmProtoNotRestrictedNotes.put(SEQUENCE_NUMBER, seqNum);
        hmProtoNotRestrictedNotes =(Hashtable)webTxnBean.getResults(request,GET_PROTO_NOT_RESTRICTED_NOTES, hmProtoNotRestrictedNotes);
        Vector vecProtoNotRestrictedNotesData = (Vector) hmProtoNotRestrictedNotes.get(GET_PROTO_NOT_RESTRICTED_NOTES);
        //Modified for COEUSDEV-165 : PI User unable to view notes that are saved in Lite - Start
        //In cwProtocolNotes display iteration is done for protcolNotesData, so session attribute is set to protocolNotesData
//        session.setAttribute(PROTO_NOT_RESTRICTED_NOTES_DATA, vecProtoNotRestrictedNotesData);
        session.setAttribute(PROTOCOL_NOTES_DATA, vecProtoNotRestrictedNotesData);
        //COEUSDEV-165 : END
        protocolNotesForm.set(ADD_COMMENTS,EMPTY_STRING);
        protocolNotesForm.set(ADD_RESTRICTED_VIEW,YES);
        return "success";
    }
    
    /**
     * This method saves the newly added notes to the database table OSP$PROTOCOL_NOTEPAD
     * @throws Exception
     * @param actionForm
     * @param userInfoBean
     * @return String to navigator
     */
    private String performSaveAction(ActionForm form,HttpServletRequest request) throws Exception{
        String protocolNumber = EMPTY_STRING;
        String sequenceNumber = EMPTY_STRING;
        HttpSession session = request.getSession();
        edu.mit.coeus.bean.UserInfoBean userInfoBean = (edu.mit.coeus.bean.UserInfoBean)session.getAttribute("user"+session.getId());
        Integer seqNum = null;
        Integer entryNumber = null;
        WebTxnBean webTxnBean = new WebTxnBean();
        String navigator = EMPTY_STRING;
        DynaValidatorForm protocolNotesForm = (DynaValidatorForm)form;
        //Get user info
        String userId = userInfoBean.getUserId();
        //Get timestamp
        Timestamp dbTimestamp;
        dbTimestamp = prepareTimeStamp(protocolNotesForm);
        //Get protocolNumber and sequenceNumber
        protocolNumber = (String)session.getAttribute(CoeusLiteConstants.PROTOCOL_NUMBER+session.getId());
        sequenceNumber = (String)session.getAttribute(CoeusLiteConstants.SEQUENCE_NUMBER+session.getId());
        seqNum = new Integer(sequenceNumber);
        //Get Max Entry Number for a given protocol number
        entryNumber = getMaxEntryNumber(request);
        protocolNotesForm.set(PROTOCOL_NUMBER,protocolNumber);
        protocolNotesForm.set(SEQUENCE_NUMBER,seqNum);
        protocolNotesForm.set("entryNumber",entryNumber);
        protocolNotesForm.set("comments",protocolNotesForm.get(ADD_COMMENTS));
        protocolNotesForm.set("restrictedView",request.getParameter("restricted"));
        protocolNotesForm.set("updateUser",userId);
        protocolNotesForm.set("updateTimestamp",dbTimestamp.toString());
        protocolNotesForm.set("acType",TypeConstants.INSERT_RECORD);
        webTxnBean.getResults(request, UPDATE_PROTOCOL_NOTES, protocolNotesForm);
        navigator = "success";
        return navigator;
    }

    /**
     * This method gets the max entry number for a given protocol number
     * using the function FN_GET_MAX_NOTES_ENTRY_NUMBER
     * @throws Exception
     * @return Integer
     */
    private Integer getMaxEntryNumber(HttpServletRequest request) throws Exception{
        String protocolNumber = EMPTY_STRING;
        int newMaxEntryNumber = 0;
        String navigator = EMPTY_STRING;
        HttpSession session = request.getSession();
        WebTxnBean webTxnBean = new WebTxnBean();
        protocolNumber = (String)session.getAttribute(CoeusLiteConstants.PROTOCOL_NUMBER+session.getId());
        HashMap hmMaxEntryNumber = new HashMap();
        hmMaxEntryNumber.put(PROTOCOL_NUMBER, protocolNumber);
        //Get max entry number
        Hashtable htMaxEntryNumber =(Hashtable)webTxnBean.getResults(request,GET_MAX_ENTRY_NUMBER, hmMaxEntryNumber);
        hmMaxEntryNumber = (HashMap)htMaxEntryNumber.get(GET_MAX_ENTRY_NUMBER);
        if(hmMaxEntryNumber != null && hmMaxEntryNumber.size()>0) {
            String maxEntryNumber = (String)hmMaxEntryNumber.get(MAX_ENTRY_NUMBER);
            newMaxEntryNumber = Integer.parseInt(maxEntryNumber)+1;
        }
        return new Integer(newMaxEntryNumber);
    }
    
    /**
     *  Gets protocol notes data based on rights
     *  checks for VIEW_RESTRICTED_PROTOCOL_NOTES right
     *  and authorization right
     *  @param actionForm
     *  @param userInfoBean
     *  @throws Exception
     */
    private String getProtocolNotesData(ActionForm form,HttpServletRequest request) throws Exception{
        String navigator = EMPTY_STRING;
        boolean hasViewRestrictedNotes = false;
        HttpSession session = request.getSession();
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
        String userId = userInfoBean.getUserId();
        String unitNumber = userInfoBean.getUnitNumber();
        //Modified for COEUSDEV-165 : PI User unable to view notes that are saved in Lite - Start
        
//        hasViewRestrictedNotes = userMaintDataTxnBean.getUserHasRight(userId, VIEW_RESTRICTED_PROTOCOL_NOTES,unitNumber);
        //'VIEW_RESTRICTED_NOTES' right check is done for protocol lead unit
        String protocolNumber = (String)session.getAttribute(CoeusLiteConstants.PROTOCOL_NUMBER+session.getId());
        String sequenceNumber = (String)session.getAttribute(CoeusLiteConstants.SEQUENCE_NUMBER+session.getId());
        int protocolSequenceNumber = sequenceNumber == null ? 0 : Integer.parseInt(sequenceNumber);
        ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean();
        String protocolLeadUnit = protocolDataTxnBean.getLeadUnitForProtocol(protocolNumber,protocolSequenceNumber);
        hasViewRestrictedNotes = userMaintDataTxnBean.getUserHasRight(userId, VIEW_RESTRICTED_PROTOCOL_NOTES,protocolLeadUnit);
        //COEUSDEV-165 : END
        request.setAttribute("show", "show"); 
        if(!hasViewRestrictedNotes){
            //If User does not have VIEW_RESTRICTED_PROTOCOL_NOTES right
            //then get only Not Restricted notes
            navigator = getProtoNotRestrictedNotesData(form,request);
        }else{
            //If user has rights get all protocol notes
            navigator = getProtocolNotesAllData(form, request);
        }
        if(!isAuthorized(request)){
            session.setAttribute("showAdd",new Boolean(false));
        }else{
            session.setAttribute("showAdd",new Boolean(true));
        }
        return navigator;
    }
    
    public void cleanUp() {
    } 

    /**
     * This method checks whether the logged in user has rigths for adding
     * and modifying protocol notes
     * @return boolean
     * @throws Exception
     */
    
    private boolean isAuthorized(HttpServletRequest request) throws Exception{
        HttpSession session = request.getSession();
        UserMaintDataTxnBean txnData = new UserMaintDataTxnBean();
        UserInfoBean userInfoBean  = (UserInfoBean)session.getAttribute("user"+session.getId());
        String loggedinUser = userInfoBean.getUserId();
        String unitNumber = userInfoBean.getUnitNumber();
        String protocolNumber = (String)session.getAttribute(CoeusLiteConstants.PROTOCOL_NUMBER+session.getId());
        boolean isAuthorised = false;
        isAuthorised = txnData.getUserHasProtocolRight(loggedinUser, ADD_PROTOCOL_NOTES, protocolNumber);
        //If not authorised check MODIFY_PROTOCOL
        if(!isAuthorised){
            isAuthorised = txnData.getUserHasProtocolRight(loggedinUser, MODIFY_PROTOCOL, protocolNumber);
            //If not authorised check ADD_ANY_PROTOCOL_NOTES at unit level
            if(!isAuthorised){
                isAuthorised = txnData.getUserHasRight(loggedinUser, ADD_ANY_PROTOCOL_NOTES, unitNumber);
            }
        }
        return isAuthorised;
    }
    
    
    /**
     * This method locks a particular protcol 
     * @throws Exception
     */
//    private void lockProtocol(String protocolNumber, HttpServletRequest request) throws Exception {
//        HttpSession session = request.getSession();
//        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
//        LockBean lockBean = getLockingBean(userInfoBean, protocolNumber,request);
//        lockBean.setMode("M");
//        lockModule(lockBean,request);
//        session.setAttribute(CoeusLiteConstants.RECORD_LOCKED+session.getId(),new Boolean(true));       
//    }
}
