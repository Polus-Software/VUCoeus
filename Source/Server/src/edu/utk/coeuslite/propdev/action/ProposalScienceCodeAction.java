/*
 * ProposalScienceCodeAction.java
 *
 * Created on January 25, 2011, 11:30 AM
 *
 ** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * PMD check performed, and commented unused imports and variables on 31-JAN-2011
 * by Maharaja Palanichamy
 */

package edu.utk.coeuslite.propdev.action;

import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeuslite.utils.CoeusLiteConstants;
import edu.mit.coeuslite.utils.CoeusliteMenuItems;
import edu.mit.coeuslite.utils.LockBean;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;
import java.util.Map;

/**
 *
 * @author  Maharaja Palanichamy
 */
public class ProposalScienceCodeAction extends ProposalBaseAction {
    private static final String PROPOSAL_NUMBER = "proposalNumber";    
    private static final String UPD_TIME_STAMP = "updateTimeStamp";
    private static final String UPD_USER = "updateUser";    
    private static final String ACTYPE = "acType";
    private static final String SUCCESS = "success";
    private static final String SAVE_PROPOSAL_SCIENCE_CODE = "/saveProposalScienceCode";
    private static final String DELETE_PROPOSAL_SCIENCE_CODE = "/deleteProposalScienceCode";
    private static final String UPDATE_PROPOSAL_SCIENCE_CODE = "updateProposalScienceCode";
    private static final String GET_PROPOSAL_SCIENCE_CODE = "getProposalScienceCode";
    
    /**
     * Creates a new instance of ProposalScienceCodeAction
     */
    public ProposalScienceCodeAction() {
    }
    
    public ActionForward performExecute(ActionMapping actionMapping, ActionForm actionForm,
    HttpServletRequest request, HttpServletResponse response) throws Exception {
           String navigator ="";
           navigator = performScienceCodeAction(actionMapping, actionForm, request);
           Map mapMenuList = new HashMap();
           mapMenuList.put("menuItems",CoeusliteMenuItems.PROPOSAL_MENU_ITEMS);
           mapMenuList.put("menuCode",CoeusliteMenuItems.SCIENCE_CODE_MENU_CODE);
           setSelectedMenuList(request, mapMenuList);           
           readSavedStatus(request);
           return actionMapping.findForward(navigator);           
    } 
        
    /**
     * This method is to check whether the entered science code is valid or not
     * @param scienceCode
     * @throws Exception
     * @return
     */
    private boolean validateScienceCodeData(DynaValidatorForm dynaForm, HttpServletRequest request) throws Exception{
        boolean validate = true;
        if("".equals(dynaForm.get("code").toString().trim())){
            ActionMessages messages = new ActionMessages();
            messages.add("invalidScienceCode", new ActionMessage("proposal.scienceCode.error.mandatory"));
            saveMessages(request, messages);
            validate = false;
        }        
        return validate;
    }
                    
   /**
     * Method to identify the action and direct it to the respective handler method.
     * @param actionMapping
     * @param actionForm
     * @param request
     * @exception Exception
     */
    private String performScienceCodeAction(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request)throws Exception {
        String navigator = SUCCESS;
        HttpSession session = request.getSession();
        String proposalNumber = (String)session.getAttribute(PROPOSAL_NUMBER+request.getSession().getId());    
        DynaValidatorForm dynaForm = (DynaValidatorForm)actionForm;
        if(actionMapping.getPath().equalsIgnoreCase(SAVE_PROPOSAL_SCIENCE_CODE)){
            navigator = saveProposalScienceCode(dynaForm, request, proposalNumber);
        }else if(actionMapping.getPath().equalsIgnoreCase(DELETE_PROPOSAL_SCIENCE_CODE)){
            navigator = removeProposalScienceCode( dynaForm, request, proposalNumber);
        }else{
            navigator = getProposalScienceCode(dynaForm, request, proposalNumber);            
        }
        return navigator;
    }
    
    /**
     * Method to save the science code     
     * @param actionForm
     * @param request
     * @param proposalNumber
     * @exception Exception
     */
    private String saveProposalScienceCode(DynaValidatorForm dynaForm,
            HttpServletRequest request,
            String proposalNumber) throws Exception{
        String navigator =SUCCESS;
        // Check if lock exists or not
        HttpSession session = request.getSession();
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        LockBean lockBean = getLockingBean(userInfoBean, (String)session.getAttribute(PROPOSAL_NUMBER+session.getId()), request);
        boolean isLockExists = isLockExists(lockBean, lockBean.getModuleKey());
        LockBean lockData = getLockedData(CoeusLiteConstants.PROP_DEV_LOCK_STR+lockBean.getModuleNumber(), request);
        if(!isLockExists && lockBean.getSessionId().equals(lockData.getSessionId())) {
            try {
                boolean validData = validateScienceCodeData(dynaForm, request);
                if(!validData ){
                    return "invalidScienceCode";
                }
                dynaForm.set(PROPOSAL_NUMBER, proposalNumber);
                Timestamp dbTimestamp = prepareTimeStamp();
                dynaForm.set(UPD_TIME_STAMP,dbTimestamp.toString());
                dynaForm.set(UPD_USER, userInfoBean.getUserId());
                dynaForm.set(ACTYPE, TypeConstants.INSERT_RECORD);
                WebTxnBean webTxnBean = new WebTxnBean();
                Hashtable htPropoSaveData = (Hashtable)webTxnBean.getResults(request,UPDATE_PROPOSAL_SCIENCE_CODE, dynaForm);
                
                Hashtable htPropoAfterSaveData = (Hashtable)webTxnBean.getResults(request,GET_PROPOSAL_SCIENCE_CODE, dynaForm);
                Vector vcscienceCode=(Vector)htPropoAfterSaveData.get(GET_PROPOSAL_SCIENCE_CODE);
                
                if(vcscienceCode == null || vcscienceCode.isEmpty()){
                    vcscienceCode = new Vector();
                }
                session.setAttribute("proposalScienceCodes",vcscienceCode);
            } catch (Exception ex) {
                UtilFactory.log(ex.getMessage());
            }
        }else{
            String errMsg = "release_lock_for";
            ActionMessages messages = new ActionMessages();
            messages.add("errMsg", new ActionMessage(errMsg,lockBean.getModuleKey(),lockBean.getModuleNumber()));
            saveMessages(request, messages);
        }
        return navigator;
    }
    
    /**
     * Method to delete the science code     
     * @param actionForm
     * @param request
     * @param proposalNumber
     * @exception Exception
     */
    private String removeProposalScienceCode(DynaValidatorForm dynaForm,
            HttpServletRequest request,
            String proposalNumber) throws Exception{
        String navigator =SUCCESS;
        HttpSession session = request.getSession();
        try {
            dynaForm.set(ACTYPE,TypeConstants.DELETE_RECORD);    
            dynaForm.set("awProposalNumber",proposalNumber);        
            WebTxnBean webTxnBean = new WebTxnBean();
            Hashtable htPropoSaveData = (Hashtable)webTxnBean.getResults(request,UPDATE_PROPOSAL_SCIENCE_CODE, dynaForm);
            
            Hashtable htPropoAfterRemoveData = (Hashtable)webTxnBean.getResults(request,GET_PROPOSAL_SCIENCE_CODE, dynaForm);
            Vector vcscienceCode=(Vector)htPropoAfterRemoveData.get(GET_PROPOSAL_SCIENCE_CODE);
            
            if(vcscienceCode == null || vcscienceCode.isEmpty()){
                vcscienceCode = new Vector();
            }
            session.setAttribute("proposalScienceCodes",vcscienceCode);
        } catch (Exception ex) {
            UtilFactory.log(ex.getMessage());
        }
        
        return navigator;
    }
   
    /**
     * Method to get and list all the science codes
     * @param actionForm
     * @param request
     * @param proposalNumber
     * @exception Exception
     */
    private String getProposalScienceCode(DynaValidatorForm dynaForm,
            HttpServletRequest request,
            String proposalNumber) throws Exception{
        String navigator =SUCCESS;
        HttpSession session = request.getSession();
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        try {            
            dynaForm.set(PROPOSAL_NUMBER, proposalNumber);
            Timestamp dbTimestamp = prepareTimeStamp();
            dynaForm.set(UPD_TIME_STAMP,dbTimestamp.toString());
            dynaForm.set(UPD_USER, userInfoBean.getUserId());
            WebTxnBean webTxnBean = new WebTxnBean();
            Hashtable htPropoScienceCodeData = (Hashtable)webTxnBean.getResults(request,GET_PROPOSAL_SCIENCE_CODE, dynaForm);
            Vector vcscienceCode=(Vector)htPropoScienceCodeData.get(GET_PROPOSAL_SCIENCE_CODE);
            
            if(vcscienceCode == null || vcscienceCode.isEmpty()){
               vcscienceCode = new Vector();
            }
             session.setAttribute("proposalScienceCodes",vcscienceCode);
        } catch (Exception ex) {
            UtilFactory.log(ex.getMessage());
        }
        
        return navigator;
    }  
}  