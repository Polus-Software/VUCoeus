/*
 * ProposalCreditSplit.java
 *
 * Created on November 29, 2006, 10:32 AM
 */

package edu.utk.coeuslite.propdev.action;

import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeuslite.utils.CoeusLiteConstants;
import edu.mit.coeuslite.utils.CoeusliteMenuItems;
import edu.mit.coeuslite.utils.LockBean;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.sql.Timestamp;
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
import org.apache.struts.validator.DynaValidatorForm;

/**
 *
 * @author  noorula
 */
public class ProposalCreditSplitAction extends ProposalBaseAction {
    
    private static final String SUCCESS = "success";
    private static final String SAVE_CREDIT_SPLIT = "/saveCreditSplitInfo";
    private static final String INSERT_VALUE = "insertValue";
    private static final String AC_TYPE = "acType";
    private static final String ZERO = "0.00";
    private static final String INV_CREDIT_TYPE_CODE = "invCreditTypeCode";
    private static final String DESCRIPTION = "description";
    private static final String PERSON_ID = "personId";
    private static final String SEPERATOR = "-";
    private static final String PERSON_UNIT_DETAILS = "vecPersonUnitDetails";
    private static final String UNIT_NUMBER = "unitNumber";
    private static final String INV_CREDIT_TYPE = "vecInvCreditType";
    private static final String UPDATE_TIME_STAMP = "updateTimestamp";
    /** Creates a new instance of ProposalCreditSplit */
    public ProposalCreditSplitAction() {
    }
    
    public ActionForward performExecute(ActionMapping actionMapping, ActionForm actionForm, 
        HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());        
        if(actionMapping.getPath().equals(SAVE_CREDIT_SPLIT)){
            LockBean lockBean = getLockingBean(userInfoBean, (String)session.getAttribute("proposalNumber"+session.getId()), request);
            boolean isLockExists = isLockExists(lockBean, lockBean.getModuleKey());
            LockBean lockData = getLockedData(CoeusLiteConstants.PROP_DEV_LOCK_STR+lockBean.getModuleNumber(), request);
            if(!isLockExists && lockBean.getSessionId().equals(lockData.getSessionId())) {
                // Code modified for bug fix case# 2818
                ActionForward actionForward = saveCreditSplitDetails(request, actionMapping);
            }else{
                String errMsg = "release_lock_for";
                ActionMessages messages = new ActionMessages();
                messages.add("errMsg", new ActionMessage(errMsg,lockBean.getModuleKey(),lockBean.getModuleNumber()));
                saveMessages(request, messages);
            }
        }
        getCreditSplitDetails(request);
        Map mapMenuList = new HashMap();
        mapMenuList.put("menuItems",CoeusliteMenuItems.PROPOSAL_MENU_ITEMS);
        mapMenuList.put("menuCode",CoeusliteMenuItems.CREDIT_SPLIT); 
        setSelectedMenuList(request, mapMenuList);
        readSavedStatus(request);         
        return actionMapping.findForward("success");
    }
    
    /**
     * To get the credit split details from the data base.
     * @param request
     * @throws Exception
     * @return String
     */    
    private String getCreditSplitDetails(HttpServletRequest request) throws Exception {
        String navigator = SUCCESS;
        HttpSession session = request.getSession();
        WebTxnBean webTxnBean = new WebTxnBean();
        HashMap hmCreditsplit = new HashMap();
        hmCreditsplit.put("proposalNumber", session.getAttribute("proposalNumber"+session.getId()));
        Hashtable htCreditSplit = (Hashtable) webTxnBean.getResults(request, "getMoreCreditSplitDetails" , hmCreditsplit);
        // To get credit type datas.
        Vector vecInvCreditType = (Vector) htCreditSplit.get("getInvCreditTypeData");
        // To get investigators credit type values.
        Vector vecPropCreditSplit = (Vector) htCreditSplit.get("getPropCreditSplit");
        // To get units credit type values.
        Vector vecPropUnitCreditSplit = (Vector) htCreditSplit.get("getPropUnitCreditSplit");
        // To get investigators details.
//        Vector vecPersonCreditSplit = (Vector) htCreditSplit.get("getPersonCreditSplit");
        Vector vecPersonUnitDetails = (Vector) htCreditSplit.get("getPersonCreditSplit");        
        // To get units details.
        Vector vecUnitCreditSplit = (Vector) htCreditSplit.get("getUnitCreditSplit");
        // Code commented for bug fix case# 2818 - starts
//        Vector vecPersonUnitDetails = new Vector();
//        if(vecPersonCreditSplit!=null && vecPersonCreditSplit.size()>0) {
//            for(int index = 0 ; index < vecPersonCreditSplit.size() ; index++) {
//                DynaValidatorForm dynaForm = (DynaValidatorForm) vecPersonCreditSplit.get(index);
//                if(dynaForm!=null){
//                    if(vecUnitCreditSplit!=null && vecUnitCreditSplit.size()>0) {
//                        for(int count = 0 ; count < vecUnitCreditSplit.size() ; count++) {
//                            DynaValidatorForm unitDynaForm = (DynaValidatorForm) vecUnitCreditSplit.get(count);
//                            if(unitDynaForm!=null && dynaForm.get(PERSON_ID).equals(unitDynaForm.get(PERSON_ID))){
//                                dynaForm.set(UNIT_NUMBER, unitDynaForm.get(UNIT_NUMBER));
//                                dynaForm.set("unitName", unitDynaForm.get("unitName"));
//                                break;
//                            }
//                        }
//                    }
//                    vecPersonUnitDetails.add(dynaForm);
//                }
//            }
//        }
        // Code commented for bug fix case# 2818 - ends
        // Setting all the credit split values to the session.
        session.setAttribute(INV_CREDIT_TYPE, vecInvCreditType);
        session.setAttribute("vecPropCreditSplit", vecPropCreditSplit);
        session.setAttribute("vecPropUnitCreditSplit", vecPropUnitCreditSplit);
        session.setAttribute("vecUnitCreditSplit", vecUnitCreditSplit);
        session.setAttribute(PERSON_UNIT_DETAILS, vecPersonUnitDetails);
        return navigator;
    }
    
    /**
     * To save the credit split investigators data and units data to the data base.
     * @param request
     * @throws Exception
     */    
    private ActionForward saveCreditSplitDetails(HttpServletRequest request,
            ActionMapping actionMapping) throws Exception {
        HttpSession session = request.getSession();
        WebTxnBean webTxnBean = new WebTxnBean();
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        Vector vecInvCreditType = (Vector) session.getAttribute(INV_CREDIT_TYPE);
        Vector vecPersonUnitDetails = (Vector) session.getAttribute(PERSON_UNIT_DETAILS);
        Vector vecPropUnitCreditSplit = (Vector) session.getAttribute("vecPropUnitCreditSplit");
        Vector vecUnitCreditSplit = (Vector) session.getAttribute("vecUnitCreditSplit");
        Timestamp dbTimestamp = prepareTimeStamp();                    
                    
        // Validation for Units addToZero.
        if(validateUnitDetails(request) && validatePersonDetails(request)) {
            // Saving datas to the DB.
            for(int index=0 ; index < vecPersonUnitDetails.size() ; index++){
                DynaValidatorForm dynaForm = (DynaValidatorForm) vecPersonUnitDetails.get(index);
                if(dynaForm!=null){
                    for(int count=0 ; count < vecInvCreditType.size() ; count++){
                        DynaValidatorForm dynaFormCredit = (DynaValidatorForm) vecInvCreditType.get(count);
                        if(dynaFormCredit!=null){
                            String credit = request.getParameter(dynaForm.get(PERSON_ID).toString()+SEPERATOR+
                                                                dynaFormCredit.get(DESCRIPTION).toString());
                            //dynaForm.set("proposalNumber", session.getAttribute("propsalNumber"+session.getId()));
                            dynaForm.set(UPDATE_TIME_STAMP, dbTimestamp.toString());
                            dynaForm.set("updateUser", userInfoBean.getUserId());
                            dynaForm.set(INV_CREDIT_TYPE_CODE, dynaFormCredit.get(INV_CREDIT_TYPE_CODE));
                            if(credit!=null && (credit.equals(ZERO) || credit.equals("0"))){
                                credit = null;
                            }
                            dynaForm.set("credit", credit);
                            HashMap hmValueDetails = new HashMap();
                            hmValueDetails.put("value", credit);
                            hmValueDetails.put(DESCRIPTION, dynaFormCredit.get(DESCRIPTION));
                            hmValueDetails.put("personName", dynaForm.get("personName"));
                            hmValueDetails.put("name", "investigator");
                            // Code modified for bug fix case# 2818 - starts
                            if(validNumber(request, hmValueDetails)){
                                dynaForm = checkPersonExists(request, dynaForm);
                                // to save the investigators credit split values to the DB
                                webTxnBean.getResults(request, "updatePersonCreditSplit", dynaForm);

    //                            credit = request.getParameter(dynaForm.get(UNIT_NUMBER).toString()+
    //                                SEPERATOR+dynaForm.get(PERSON_ID).toString()+"-unitTotal-"+dynaFormCredit.get(DESCRIPTION).toString());
                            } else {
                                return actionMapping.findForward("success");
                            }
                            int unitCount = 0;
                            if(vecUnitCreditSplit!=null && vecUnitCreditSplit.size()>0){
                                for(int unit=0 ; unit < vecUnitCreditSplit.size() ; unit++){
                                    DynaValidatorForm unitForm = (DynaValidatorForm) vecUnitCreditSplit.get(unit);
                                    boolean isPresent = false;
                                    if(vecPropUnitCreditSplit!=null && vecPropUnitCreditSplit.size()>0){
                                        for(int unitIndex=0 ; unitIndex < vecPropUnitCreditSplit.size() ; unitIndex++){
                                            DynaValidatorForm dynaUnitForm = (DynaValidatorForm) vecPropUnitCreditSplit.get(unitIndex);
                                            if(dynaUnitForm!=null && dynaUnitForm.get("personId").equals(dynaForm.get("personId")) &&
                                                dynaUnitForm.get("personId").equals(unitForm.get("personId")) &&
                                                dynaUnitForm.get(INV_CREDIT_TYPE_CODE).equals(dynaFormCredit.get(INV_CREDIT_TYPE_CODE)) &&
                                                unitForm.get("unitNumber").equals(dynaUnitForm.get("unitNumber"))){
                                                credit = request.getParameter(dynaForm.get(PERSON_ID).toString()+SEPERATOR+dynaFormCredit.get(DESCRIPTION).toString()+unitCount);                        
                                                unitCount++;
                                                if(credit!=null && (credit.equals(ZERO) || credit.equals("0"))){
                                                    credit = null;
                                                }                        
                                                dynaForm.set("credit", credit);
                                                hmValueDetails = new HashMap();
                                                hmValueDetails.put("value", credit);
                                                hmValueDetails.put(DESCRIPTION, dynaFormCredit.get(DESCRIPTION));
                                                hmValueDetails.put("personName", dynaForm.get("personName"));
                                                hmValueDetails.put("name", "unit");
                                                if(validNumber(request, hmValueDetails)){
                                                    dynaForm.set("unitNumber", unitForm.get("unitNumber"));
                                                    dynaForm.set(INV_CREDIT_TYPE_CODE, dynaUnitForm.get(INV_CREDIT_TYPE_CODE));
                                                    dynaForm = checkUnitExists(request, dynaForm);
                                                    dynaForm.set("awUpdateTimestamp", dynaUnitForm.get(UPDATE_TIME_STAMP));
                                                    // to update the existing units credit split values to the new values.
                                                    webTxnBean.getResults(request, "updateUnitCreditSplit", dynaForm);
                                                    isPresent = true;
                                                } else {
                                                    return actionMapping.findForward("success");
                                                }
                                            }
                                        }
                                    }
                                    if(!isPresent && unitForm.get("personId").equals(dynaForm.get("personId"))){
                                        credit = request.getParameter(dynaForm.get(PERSON_ID).toString()+SEPERATOR+dynaFormCredit.get(DESCRIPTION).toString()+unitCount);                        
                                        unitCount++;
                                        if(credit!=null && (credit.equals(ZERO) || credit.equals("0"))){
                                            credit = null;
                                        }                        
                                        dynaForm.set("credit", credit);
                                        hmValueDetails = new HashMap();
                                        hmValueDetails.put("value", credit);
                                        hmValueDetails.put(DESCRIPTION, dynaFormCredit.get(DESCRIPTION));
                                        hmValueDetails.put("personName", dynaForm.get("personName"));
                                        hmValueDetails.put("name", "unit");
                                        if(validNumber(request, hmValueDetails)){
                                            dynaForm.set("unitNumber", unitForm.get("unitNumber"));
                                            dynaForm.set(INV_CREDIT_TYPE_CODE, dynaFormCredit.get(INV_CREDIT_TYPE_CODE));
                                            dynaForm = checkUnitExists(request, dynaForm);
                                            // to update the existing units credit split values to the new values.
                                            webTxnBean.getResults(request, "updateUnitCreditSplit", dynaForm);
                                        } else {
                                            return actionMapping.findForward("success");
                                        }
                                    }
                                }
                            }
                            // Code modified for bug fix case# 2818 - ends
                            // Code commented for bug fix case# 2818 - starts
//                            credit = request.getParameter(dynaForm.get(PERSON_ID).toString()+"-unitTotal-"+dynaFormCredit.get(DESCRIPTION).toString());                        
//                            
//                            if(credit!=null && (credit.equals(ZERO) || credit.equals("0"))){
//                                credit = null;
//                            }                        
//                            dynaForm.set("credit", credit);
//                            dynaForm = checkUnitExists(request, dynaForm);
//                            webTxnBean.getResults(request, "updateUnitCreditSplit", dynaForm); 
                            // Code commented for bug fix case# 2818 - ends
                        }
                    }                
                }
            }
        }
        return actionMapping.findForward("success");
    }
    
    /**
     * To check whether the person is already added to the DB or not.
     * @param request
     * @param dynaForm
     * @return DynaValidatorForm
     */    
    private DynaValidatorForm checkPersonExists(HttpServletRequest request, DynaValidatorForm dynaForm){
        boolean isPresent = false;
        HttpSession session = request.getSession();
        Vector vecPropCreditSplit = (Vector) session.getAttribute("vecPropCreditSplit");
        if(vecPropCreditSplit!=null && vecPropCreditSplit.size()>0){
            for(int index=0 ; index < vecPropCreditSplit.size() ; index++){
                DynaValidatorForm form = (DynaValidatorForm) vecPropCreditSplit.get(index);
                if(dynaForm.get(PERSON_ID).equals(form.get(PERSON_ID)) && 
                    dynaForm.get(INV_CREDIT_TYPE_CODE).equals(form.get(INV_CREDIT_TYPE_CODE))) {
                    dynaForm.set("awUpdateTimestamp", form.get(UPDATE_TIME_STAMP));
                    isPresent = true;
                    break;
                }
            }
        }
        if(isPresent){
            dynaForm.set(AC_TYPE, TypeConstants.UPDATE_RECORD);
        } else {
            dynaForm.set(AC_TYPE, TypeConstants.INSERT_RECORD);
        }        
        return dynaForm;
    }
    
    /**
     * To check whether the unit is already added to the DB or not.
     * @param request
     * @param dynaForm
     * @return DynaValidatorForm
     */    
    private DynaValidatorForm checkUnitExists(HttpServletRequest request, DynaValidatorForm dynaForm){
        boolean isPresent = false;
        HttpSession session = request.getSession();
        Vector vecPropUnitCreditSplit = (Vector) session.getAttribute("vecPropUnitCreditSplit");
        if(vecPropUnitCreditSplit!=null && vecPropUnitCreditSplit.size()>0){
            for(int index=0 ; index < vecPropUnitCreditSplit.size() ; index++){
                DynaValidatorForm form = (DynaValidatorForm) vecPropUnitCreditSplit.get(index);
                if(dynaForm.get(PERSON_ID).equals(form.get(PERSON_ID)) && 
                    dynaForm.get(UNIT_NUMBER).equals(form.get(UNIT_NUMBER)) &&
                    dynaForm.get(INV_CREDIT_TYPE_CODE).equals(form.get(INV_CREDIT_TYPE_CODE))) {
                    dynaForm.set("awUpdateTimestamp", form.get(UPDATE_TIME_STAMP));
                    isPresent = true;
                    break;
                }
            }
        }
        if(isPresent){
            dynaForm.set(AC_TYPE, TypeConstants.UPDATE_RECORD);
        } else {
            dynaForm.set(AC_TYPE, TypeConstants.INSERT_RECORD);
        }        
        return dynaForm;
    }
    
    /**
     * To check whether the credit split units is added to zero.
     * @param request
     * @return boolean
     */    
    private boolean validateUnitDetails(HttpServletRequest request){
        HttpSession session = request.getSession();
        boolean isValid = true;
        ActionMessages actionMessages;
        Vector vecInvCreditType = (Vector) session.getAttribute(INV_CREDIT_TYPE);
        Vector vecPersonUnitDetails = (Vector) session.getAttribute(PERSON_UNIT_DETAILS);
        for(int index=0 ; index< vecPersonUnitDetails.size() ; index++){
            DynaValidatorForm dynaForm = (DynaValidatorForm) vecPersonUnitDetails.get(index);
            if(dynaForm!=null){
                for(int count=0 ; count < vecInvCreditType.size() ; count++){
                    DynaValidatorForm dynaFormCredit = (DynaValidatorForm) vecInvCreditType.get(count);
                    // Code commented for bug fix case# 2818 - starts
//                    String value = request.getParameter(dynaForm.get(UNIT_NUMBER).toString()+
//                        SEPERATOR+dynaForm.get(PERSON_ID).toString()+SEPERATOR+dynaFormCredit.get(DESCRIPTION).toString());
                    // Code commented for bug fix case# 2818 - ends
                    // Code added for bug fix case# 2818 - starts
                    String value = request.getParameter(dynaForm.get(PERSON_ID).toString()+SEPERATOR+
                        "unitTotal"+SEPERATOR+dynaFormCredit.get(DESCRIPTION).toString());                    
                    // Code added for bug fix case# 2818 - ends
                    HashMap hmValueDetails = new HashMap();
                    hmValueDetails.put("value", value);
                    hmValueDetails.put(DESCRIPTION, dynaFormCredit.get(DESCRIPTION));
                    hmValueDetails.put("personName", dynaForm.get("personName"));
                    hmValueDetails.put("name", "unit");
                    if(validNumber(request, hmValueDetails)){
                        if(dynaFormCredit!=null && dynaFormCredit.get("addsToHundred").equals("Y")){
                            if((new Float(value).floatValue()) != 100.0){
                                actionMessages = new ActionMessages();
                                actionMessages.add("unitNeedToFill",
                                    new ActionMessage("error.unitCreditSplit.required", dynaForm.get("personName"), dynaFormCredit.get(DESCRIPTION)));
                                saveMessages(request, actionMessages);
                                request.setAttribute(INSERT_VALUE, INSERT_VALUE);
                                isValid = false;
                                return isValid;                             
                            }
                        }
                    } else {
                        isValid = false;
                        return isValid;                          
                    }
                }
            }
        }
        return isValid;
    }
    
    /**
     * To check whether the credit split investigators is added to zero.
     * @param request
     * @return boolean
     */    
    private boolean validatePersonDetails(HttpServletRequest request){
        HttpSession session = request.getSession();
        boolean isValid = true;
        ActionMessages actionMessages;
        Vector vecInvCreditType = (Vector) session.getAttribute(INV_CREDIT_TYPE);
        Vector vecPersonUnitDetails = (Vector) session.getAttribute(PERSON_UNIT_DETAILS);        
        for(int index=0 ; index< vecPersonUnitDetails.size() ; index++){
            DynaValidatorForm dynaForm = (DynaValidatorForm) vecPersonUnitDetails.get(index);
            if(dynaForm!=null){        
                for(int count=0 ; count < vecInvCreditType.size() ; count++){
                    DynaValidatorForm dynaFormCredit = (DynaValidatorForm) vecInvCreditType.get(count);
//                    String personValue = request.getParameter(dynaForm.get(PERSON_ID)+SEPERATOR+dynaFormCredit.get(DESCRIPTION).toString());
                    String value = request.getParameter("personTotal-"+dynaFormCredit.get(DESCRIPTION).toString());
                    HashMap hmValueDetails = new HashMap();
                    hmValueDetails.put("value", value);
                    hmValueDetails.put(DESCRIPTION, dynaFormCredit.get(DESCRIPTION));
                    hmValueDetails.put("personName", dynaForm.get("personName"));
                    hmValueDetails.put("name", "investigator");
                    if(validNumber(request, hmValueDetails)){
                        if(dynaFormCredit!=null && dynaFormCredit.get("addsToHundred").equals("Y") && index == vecPersonUnitDetails.size()-1){
                            if((new Float(value).floatValue()) != 100.0){
                                actionMessages = new ActionMessages();
                                actionMessages.add("investigatorNeedToFill",
                                    new ActionMessage("error.investigatorCreditSplit.required", dynaFormCredit.get(DESCRIPTION)));
                                saveMessages(request, actionMessages);
                                request.setAttribute(INSERT_VALUE, INSERT_VALUE);
                                isValid = false;
                                return isValid;                             
                            }
                        }
                    } else {
                        isValid = false;
                        return isValid;                    
                    }
                }
            }
        }
        return isValid;
    }
    
    /**
     * To check whether the entered value is a valid number or not.
     * @param request
     * @param hmValueDetails
     * @return boolean
     */    
    private boolean validNumber(HttpServletRequest request, HashMap hmValueDetails){
        boolean isValid = true;
        String inputValue = (String)hmValueDetails.get("value");
        String creditName = (String)hmValueDetails.get(DESCRIPTION);
        String personName = (String)hmValueDetails.get("personName");
        String name = (String)hmValueDetails.get("name");
        ActionMessages actionMessages;
        try{
            float value = 0.0f;
            if(inputValue == null || inputValue.equals(EMPTY_STRING)){
                inputValue = ZERO;
            }
            value = new Float(inputValue).floatValue();
            if(value<0 || value > 999.99){
                actionMessages = new ActionMessages();
                actionMessages.add("numberLimitExceeds",
                    new ActionMessage("error.numberLimitExceeds.required", creditName, personName, name));
                saveMessages(request, actionMessages);
                request.setAttribute(INSERT_VALUE, INSERT_VALUE);                
                return !isValid;
            }
        } catch (NumberFormatException e){
            actionMessages = new ActionMessages();
            actionMessages.add("notValidNumber",
                new ActionMessage("error.notValidNumber.required", creditName, name));
            saveMessages(request, actionMessages);
            request.setAttribute(INSERT_VALUE, INSERT_VALUE);
            return !isValid;
        }
        return isValid;
    }
}
