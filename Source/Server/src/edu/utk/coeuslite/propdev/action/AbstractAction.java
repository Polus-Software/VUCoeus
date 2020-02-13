/*
 * AbstractAction.java
 *
 * Created on 01 August 2006, 12:54
 */

package edu.utk.coeuslite.propdev.action;

import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeuslite.utils.CoeusLiteConstants;
import edu.mit.coeuslite.utils.CoeusliteMenuItems;
import edu.mit.coeuslite.utils.LockBean;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.validator.DynaValidatorForm;

public class AbstractAction extends ProposalBaseAction {
    private static final String GET_ABSTRACT_DATA="/getAbstract";
    private static final String SAVE_ABSTRACT_DATA="/saveAbstract";
    private static final String PROPOSAL_NUMBER = "proposalNumber";
    private static final String PRESENT_TAB = "hideTab";
    private static final String NEXT_TAB = "showTab";
    private static final String ABSTRACT = "abstract";
    private static final String ABSTRACT_TYPE_CODE = "abstractTypeCode";
    private static final String AW_UPDATE_TIMESTAMP = "awUpdateTimestamp";
    private static final String SUCCESS = "success";
    
    /** Creates a new instance of AbstractAction */
    public AbstractAction() {
    }
    
    public ActionForward performExecute(ActionMapping actionMapping, ActionForm actionForm,
    HttpServletRequest request, HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession();
        DynaValidatorForm dynaForm = (DynaValidatorForm)actionForm;
        dynaForm.set(PROPOSAL_NUMBER,session.getAttribute(PROPOSAL_NUMBER+session.getId()));
        ActionForward actionForward = getAbstractData(actionMapping, request, dynaForm);
        Map mapMenuList = new HashMap();
        mapMenuList.put("menuItems",CoeusliteMenuItems.PROPOSAL_MENU_ITEMS);
        mapMenuList.put("menuCode",CoeusliteMenuItems.ABSTRACT_MENU_CODE); 
        setSelectedMenuList(request, mapMenuList);
        readSavedStatus(request);
        return actionForward;
    }
    
    /** This method will identify which request is comes from which path and
     *navigates to the respective ActionForward
     *@returns ActionForward object
     */
    private ActionForward getAbstractData(ActionMapping actionMapping,
        HttpServletRequest request, DynaValidatorForm dynaForm)throws Exception{
        HttpSession session = request.getSession();
        WebTxnBean webTxnBean = new WebTxnBean();
        ActionForward actionForward = null;
        String navigator = EMPTY_STRING;
        if(actionMapping.getPath().equals(GET_ABSTRACT_DATA)){
            navigator = getProposalAbstract(request, dynaForm);
            actionForward = actionMapping.findForward(navigator);
        } else if(actionMapping.getPath().equals(SAVE_ABSTRACT_DATA)){
            String mode=(String)session.getAttribute("mode"+session.getId());
            if(mode==null || !mode.equalsIgnoreCase("display")) {
                // Check if lock exists or not
                UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
                String proposalNumber = (String) session.getAttribute(PROPOSAL_NUMBER+session.getId());
                LockBean lockBean = getLockingBean(userInfoBean, (String)session.getAttribute(PROPOSAL_NUMBER+session.getId()), request);
                boolean isLockExists = isLockExists(lockBean, lockBean.getModuleKey());
                LockBean lockData = getLockedData(CoeusLiteConstants.PROP_DEV_LOCK_STR+lockBean.getModuleNumber(), request);
                if(!isLockExists && lockBean.getSessionId().equals(lockData.getSessionId())) {
                    saveProposalAbstract(request, dynaForm);
                    // Update the proposal hierarchy sync flag
                    updateProposalSyncFlags(request, proposalNumber);
                }else{
                    String errMsg = "release_lock_for";
                    ActionMessages messages = new ActionMessages();
                    messages.add("errMsg", new ActionMessage(errMsg,lockBean.getModuleKey(),lockBean.getModuleNumber()));
                    saveMessages(request, messages);
                }
            }
            navigator = getProposalAbstract(request, dynaForm);
            actionForward = actionMapping.findForward(navigator);
        }
        return actionForward;
    }
    
    /** This method is to get the Header details and abstract saved details from the
     *  database
     * @throws Exception
     * @return String to forward
     */    
    private String getProposalAbstract(HttpServletRequest request,
        DynaValidatorForm dynaForm)throws Exception {
        HttpSession session = request.getSession();
        WebTxnBean webTxnBean = new WebTxnBean();
        //Added for case#2349 - Update timestamp for the Abstracts Module - start
        UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();                  
        //Added for case#2349 - Update timestamp for the Abstracts Module - end
        String proposalNumber = (String) session.getAttribute(PROPOSAL_NUMBER+session.getId());
        dynaForm.set(ABSTRACT,EMPTY_STRING);
        dynaForm.set(AW_UPDATE_TIMESTAMP,EMPTY_STRING);
        String hideTab =(String) dynaForm.get(PRESENT_TAB);
        String showTab = EMPTY_STRING;
        HashMap hmAbstractInfo = new HashMap();
        hmAbstractInfo.put(PROPOSAL_NUMBER, proposalNumber);       
        Hashtable htAbstractInfo = (Hashtable) webTxnBean.getResults(request, "proposalAbstract", hmAbstractInfo);
        Vector vecAbstractTypes = (Vector) htAbstractInfo.get("getAbstractTypes");
        Vector vecProposalAbstracts = (Vector) htAbstractInfo.get("getProposalAbstracts");
        if(dynaForm!=null && (dynaForm.get(NEXT_TAB)==null || dynaForm.get(NEXT_TAB).equals(EMPTY_STRING))){
             if(vecAbstractTypes!=null && vecAbstractTypes.size()>0) {
                 DynaValidatorForm dynaValidatorForm = (DynaValidatorForm) vecAbstractTypes.get(0);
                 if(dynaValidatorForm!=null) {
                    request.setAttribute("display", dynaValidatorForm.get(ABSTRACT_TYPE_CODE));
                    showTab = (String)dynaValidatorForm.get(ABSTRACT_TYPE_CODE);
                 }
             }
        } else {
            request.setAttribute("display",dynaForm.get(NEXT_TAB));
            showTab = (String) dynaForm.get(NEXT_TAB);
        }
        if(vecProposalAbstracts!=null && vecProposalAbstracts.size()>0) {
                for (int index=0;index<vecProposalAbstracts.size();index++) {
                    DynaValidatorForm dynaValidatorForm = (DynaValidatorForm) vecProposalAbstracts.get(index);
                    //Added/Modified for case#2349 - Update timestamp for the Abstracts Module - start
                    String userTimeStamp = userMaintDataTxnBean.getUserName(dynaValidatorForm.get("updateUser").toString())
                                            +" at "                                  
                                            +getFormattedDate(dynaValidatorForm.get("updateTimestamp").toString());
                    dynaValidatorForm.set("updateUser", userTimeStamp);                    
                    if(dynaValidatorForm!=null && dynaValidatorForm.get(ABSTRACT_TYPE_CODE).toString().equals(showTab)) {
                        dynaForm.set(ABSTRACT,dynaValidatorForm.get(ABSTRACT));
                        dynaForm.set(AW_UPDATE_TIMESTAMP,dynaValidatorForm.get("updateTimestamp"));
                        dynaForm.set("awAbstract", dynaValidatorForm.get(ABSTRACT));
                    }
                    //Added/Modified for case#2349 - Update timestamp for the Abstracts Module - end
                }
        }
        session.setAttribute("vecAbstractTypes", vecAbstractTypes);
        session.setAttribute("vecProposalAbstracts", vecProposalAbstracts);
        return SUCCESS;
    }
    
    /** This method is to save the details of abstract to the database
     * @throws Exception
     */    
    private void saveProposalAbstract(HttpServletRequest request, DynaValidatorForm dynaForm)throws Exception {
        HttpSession session = request.getSession();
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        WebTxnBean webTxnBean = new WebTxnBean();
        String hideTab =(String) dynaForm.get(PRESENT_TAB);
        String flag = TypeConstants.INSERT_RECORD;
        if(dynaForm.get(ABSTRACT)!=null && !dynaForm.get(ABSTRACT).toString().trim().equals(EMPTY_STRING)) {
            if(saveRequired(request, dynaForm)){
                Timestamp dbTimestamp = prepareTimeStamp();
                Vector vecProposalAbstracts =(Vector) session.getAttribute("vecProposalAbstracts");
                if(vecProposalAbstracts!=null && vecProposalAbstracts.size()>0) {
                    for (int index=0;index<vecProposalAbstracts.size();index++) {
                        DynaValidatorForm dynaValidatorForm = (DynaValidatorForm) vecProposalAbstracts.get(index);
                        if(dynaValidatorForm!=null && dynaValidatorForm.get(ABSTRACT_TYPE_CODE).toString().equals(hideTab)) {
                            flag = TypeConstants.UPDATE_RECORD;
                        }
                    }
                }
                dynaForm.set("updateTimestamp",dbTimestamp.toString());
                dynaForm.set(ABSTRACT_TYPE_CODE,hideTab);
                dynaForm.set("updateUser",userInfoBean.getUserId());            

                if(flag.equals(TypeConstants.INSERT_RECORD)) {
                    webTxnBean.getResults(request, "proposalAbstractInsert", dynaForm);
                } else {
                    webTxnBean.getResults(request, "proposalAbstractUpdate", dynaForm);
                }
            }
        } else {
            deleteProposalAbstract(request, dynaForm);
        }
    }
    
    /** This method is to delete the details of abstract to the database
     * @throws Exception
     */    
    private void deleteProposalAbstract(HttpServletRequest request,
        DynaValidatorForm dynaForm)throws Exception {
        WebTxnBean webTxnBean = new WebTxnBean();
        if(dynaForm.get(AW_UPDATE_TIMESTAMP)!=null && !dynaForm.get(AW_UPDATE_TIMESTAMP).equals(EMPTY_STRING)) {
            String hideTab =(String) dynaForm.get(PRESENT_TAB);
            dynaForm.set("updateTimestamp",dynaForm.get(AW_UPDATE_TIMESTAMP));
            dynaForm.set(ABSTRACT_TYPE_CODE,hideTab);
            webTxnBean.getResults(request, "proposalAbstractDelete", dynaForm);
        }
    }
    
    //Added for case#2349 - Update timestamp for the Abstracts Module - start
    /**
     * This method formats date to dd-MMM-yyyy hh:mm:ss a format
     * @param strDate String
     * @return strDate String
     */
    private String getFormattedDate(String strDate){        
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
        return (strDate != null ? dateFormat.format(java.sql.Timestamp.valueOf(strDate)) : null);           
    }    
    
    /**
     * This method checks whether save is required when the user switches between tabs
     * @param request HttpServletRequest
     * @param dynaForm DynaValidatorForm
     * @return isSaveRequired boolean, indicating whether save is required or not
     */
    private boolean saveRequired(HttpServletRequest request, DynaValidatorForm dynaForm){
        boolean isSaveRequired = true;
        HttpSession session = request.getSession();
        String newAbstractDesc = (String)dynaForm.get(ABSTRACT);
        String abstractTypeCode = (String)dynaForm.get("hideTab");
        String oldAbstractDesc = "";
        Vector vecProposalAbstracts = (Vector)session.getAttribute("vecProposalAbstracts");
        if(vecProposalAbstracts != null && vecProposalAbstracts.size() > 0){
            for(int index = 0; index < vecProposalAbstracts.size(); index++){
                DynaActionForm dynaActionForm = (DynaActionForm)vecProposalAbstracts.get(index);
                if(dynaActionForm.get(ABSTRACT_TYPE_CODE).toString().equals(abstractTypeCode)) {
                    oldAbstractDesc = (String)dynaActionForm.get(ABSTRACT);
                    if(oldAbstractDesc.trim().equals(newAbstractDesc.trim())){
                        isSaveRequired = false;
                    }
                    break;
                }
            }            
        }        
        return isSaveRequired;
    }    
    //Added for case#2349 - Update timestamp for the Abstracts Module - end
}
