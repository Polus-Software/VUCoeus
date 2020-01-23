/*
 * UnderRecoveryAction.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * Created on May 4, 2007, 1:54 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */



package edu.wmc.coeuslite.budget.action;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.validator.GenericTypeValidator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import edu.mit.coeuslite.utils.CoeusDynaBeansList;
import edu.wmc.coeuslite.budget.bean.ProposalBudgetHeaderBean;
import edu.mit.coeuslite.utils.CoeusliteMenuItems;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import edu.mit.coeus.budget.bean.BudgetInfoBean;
import edu.mit.coeus.utils.ParameterUtils;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.validator.DynaValidatorForm;
import edu.mit.coeus.utils.TypeConstants;
import java.sql.Timestamp;
import edu.mit.coeuslite.utils.CoeusLiteConstants;
import edu.mit.coeuslite.utils.ComboBoxBean;
import edu.mit.coeuslite.utils.LockBean;
/**
 *
 * @author divyasusendran
 */
public class UnderRecoveryAction  extends BudgetBaseAction{
    /** Creates a new instance of UnderRecoveryAction */
    private static final String GET_UNDER_RECOVERY_DISTRIBUTION = "/getUnderRecoveryDistribution";
    private static final String ADD_UNDER_RECOVERY = "/addUnderRecoveryDistribution";
    private static final String DELETE_UNDER_RECOVERY = "/deleteUnderRecoveryDistribution";
    private static final String SAVE_UNDER_RECOVERY = "/saveUnderRecoveryDistribution";
    private static final String BUDGET_PERIOD_DATA = "URBudgetPeriodData";
    private static final String NO_UNDER_RECOVERY_DISTRIBUTION = "noUnderRecoveryDistribution";
    private static final String PROPOSAL_NUMBER = "proposalNumber";
    private static final String VERSION_NUMBER = "versionNumber";
    private static final String ADD_UPD_UR = "addUpdPropUnderRecovery";
    private static final String UNDERRECOVERYDYNALIST = "underRecoveryDynaList";
    private static final String VERSION_NUMBER_WMC = "versionNumber_wmc";
    private static final String PROPOSAL_NUMBER_WMC = "proposalNumber_wmc";
    private static final String UPDATE_USER_WMC = "updateUser_wmc";
    private static final String ON_OFF_CAMPUS_WMC = "onOffCampusFlag_wmc";
    private static final String APPLICABLE_RATES_WMC = "applicableRate_wmc";
    private static final String STR_APPLICABLE_RATES_WMC = "strApplicableRate_wmc";
    private static final String FISCAL_YEAR_WMC = "fiscalYear_wmc";
    private static final String SOURCE_ACCOUNT_WMC = "sourceAccount_wmc";
    private static final String UNDER_RECOVER_AMT_WMC = "underRecoveryAmt_wmc";
    private static final String STR_UNDER_RECOVERY_AMT_WMC = "strUnderRecoveryAmt_wmc";
    private static final String AW_UPDATE_TIMESTAMP_WMC = "awUpdateTimestamp_wmc";
    private static final String AW_PROPOSAL_NUMBER_WMC = "awProposalNumber_wmc";
    private static final String AW_VERSION_NUMBER_WMC  = "awVersionNumber_wmc";
    private static final String AW_APPLICABLE_RATE_WMC = "awApplicableRate_wmc";
    private static final String AW_FISCAL_YEAR = "awFiscalYear_wmc";
    private static final String AW_ON_OFF_CAMPUS = "awOnOffCampusFlag_wmc";
    private static final String AW_SOURCE_ACCOUNT_WMC = "awSourceAccount_wmc";
    private static final String UPDATE_TIMESTAMP_WMC = "updateTimestamp_wmc";
    private static final String ACTYPE = "acType";
    private static final String USER = "user";
    private static final String SUCCESS = "success";
    private static final String DELETED_UR_FORMS = "deletedURForms";
    private static final String GET_BUDGET_PERIOD_DATA = "getCSDBudgetPeriodData";
    private static final String INVALID_FISCAL_YEAR_ERR_MSG = "underRecoveryDistribution.error.InvalidFiscalYear";
    private String accountNumberMaxLength;
    public UnderRecoveryAction() {
    }
    
    public ActionForward performExecute(ActionMapping actionMapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        CoeusDynaBeansList underRecoveryDynaList = (CoeusDynaBeansList)actionForm;
        ActionForward actionForward = getUnderRecoveryData(actionMapping,request,underRecoveryDynaList);
        return actionForward;
    }
    
    private ActionForward getUnderRecoveryData(ActionMapping actionMapping,
            HttpServletRequest request, CoeusDynaBeansList underRecoveryDynaList)throws Exception {
        String navigator = SUCCESS;
        HttpSession session = request.getSession();
        //Added for Case#2402- use a parameter to set the length of the account number throughout app - Start
        //To get MAX_ACCOUNT_NUMBER_LENGTH paramter details
        accountNumberMaxLength = ParameterUtils.getMaxAccountNumberLength();
        session.setAttribute("AccountNumberMaxLength",accountNumberMaxLength);
        //Case#2402 - End
        if(actionMapping.getPath().equals(GET_UNDER_RECOVERY_DISTRIBUTION)){
            navigator = getUnderRecoveryForVersion(request,underRecoveryDynaList);
        }else if(actionMapping.getPath().equals(ADD_UNDER_RECOVERY)){
            navigator = addUnderRecovery(request,underRecoveryDynaList);
            request.setAttribute("dataModified", "modified");
        }else if(actionMapping.getPath().equals(DELETE_UNDER_RECOVERY)){
            navigator = deleteUnderRecoveryDistribution(underRecoveryDynaList,request);
            request.setAttribute("dataModified", "modified");
        }else{
            UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute(USER+session.getId());
            LockBean lockBean = getLockingBean(userInfoBean, (String)session.getAttribute(CoeusLiteConstants.PROPOSAL_NUMBER+session.getId()), request);
            boolean isLockExists = isLockExists(lockBean, lockBean.getModuleKey());
            LockBean lockData = getLockedData(CoeusLiteConstants.BUDGET_LOCK_STR+lockBean.getModuleNumber(), request);
            if(!isLockExists && lockBean.getSessionId().equals(lockData.getSessionId())) {
                
                if(actionMapping.getPath().equals(SAVE_UNDER_RECOVERY)){
                    navigator = saveUnderRecoveryDistribution(underRecoveryDynaList,request);
                }
                
            } else {
                String errMsg = "release_lock_for";
                ActionMessages messages = new ActionMessages();
                messages.add("errMsg", new ActionMessage(errMsg,lockBean.getModuleKey(),lockBean.getModuleNumber()));
                saveMessages(request, messages);
            }
        }
        Map mapMenuList = new HashMap();
        mapMenuList.put("menuItems",CoeusliteMenuItems.BUDGET_MENU_ITEMS);
        mapMenuList.put("menuCode",CoeusliteMenuItems.UNDER_RECOVERY_CODE);
        setSelectedMenuList(request, mapMenuList);
        return actionMapping.findForward(navigator);
    }
    
    
    
    
    /**
     * Method to perform save action
     * @param underRecoveryDynaList instance of CoeusDynaBeansList
     * @param request instance of Request
     * @throws Exception if exception occur
     * @return instance of String which contains "success"
     */
    private String saveUnderRecoveryDistribution(CoeusDynaBeansList underRecoveryDynaList,
            HttpServletRequest request) throws Exception{
        String navigator = SUCCESS;
        HttpSession session = request.getSession();
        boolean isYearValid = true;
        WebTxnBean webTxnBean = new WebTxnBean();
        Timestamp dbTimestamp = prepareTimeStamp();
        ActionMessages actionMessages = null;
        ProposalBudgetHeaderBean headerBean = (ProposalBudgetHeaderBean)session.getAttribute("ProposalBudgetHeaderBean");
        String proposalNumber = headerBean.getProposalNumber();
        int versionNumber = headerBean.getVersionNumber();
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute(USER+session.getId());
        String userId = userInfoBean.getUserId();
        HashMap hmPeriodDetails = new HashMap();
        hmPeriodDetails.put(PROPOSAL_NUMBER,proposalNumber);
        hmPeriodDetails.put(VERSION_NUMBER,new Integer(versionNumber));
        Hashtable htGetPeriodDetails = (Hashtable)webTxnBean.getResults(request,GET_BUDGET_PERIOD_DATA,hmPeriodDetails);
        List vecBudgetPeriodBean =  (Vector)htGetPeriodDetails.get(GET_BUDGET_PERIOD_DATA);
        session.setAttribute(BUDGET_PERIOD_DATA,vecBudgetPeriodBean);
        double totUnderRecAmt = getTotalUnderRecAmt(vecBudgetPeriodBean);
        List lstModified = (List)underRecoveryDynaList.getList();
        
        // Allow to save only if
        //1)the total under recovery amount from the period data is equal to the
        //      sum of all the under recovery amounts entered in the form else fire the validation
        //2)if there are no rows i.e all rows are deleted then dont fire the validation
        int listSize = lstModified.size();
        if(lstModified != null){
        if(listSize == 0){
            // deleting the forms removed from the coeusdynabeanslist
            Vector vecDeleted = (Vector)session.getAttribute(DELETED_UR_FORMS);
            if(vecDeleted != null && vecDeleted.size()>0){
                for(int count =0; count <vecDeleted.size();count++){
                    DynaValidatorForm dynaDelSaveForm = (DynaValidatorForm)vecDeleted.get(count);
                    if(dynaDelSaveForm.get(ACTYPE).equals(TypeConstants.DELETE_RECORD)){
                        formatFields(dynaDelSaveForm);
                        String updateTime = (String)dynaDelSaveForm.get(UPDATE_TIMESTAMP_WMC);
                        dynaDelSaveForm.set(AW_UPDATE_TIMESTAMP_WMC,updateTime);
                        dynaDelSaveForm.set(UPDATE_TIMESTAMP_WMC,dbTimestamp.toString());
                    }
                    webTxnBean.getResults(request,ADD_UPD_UR,dynaDelSaveForm);
                }
            }
            
        } else{
            //validating the fields rates ,source account and fiscal year ,
            //if fiscal year is not empty then checking
            // whether it is a valid year between 1900 to 2099
            for(int sizeIndex = 0; sizeIndex < lstModified.size(); sizeIndex++){
                DynaValidatorForm dynaSetForm = (DynaValidatorForm)lstModified.get(sizeIndex);
                
                if((dynaSetForm.get(STR_UNDER_RECOVERY_AMT_WMC).toString().trim())!= null){
                    String underRecoverAmt = (dynaSetForm.get(STR_UNDER_RECOVERY_AMT_WMC).toString().trim()).replaceAll("[a-z,A-Z,~,@,#,^,$,@,#,(,),!,`,%, ,&,*,/]","");
                    Object result = GenericTypeValidator.formatDouble(underRecoverAmt);
                    if(result == null){
                        actionMessages = new ActionMessages();
                        actionMessages.add("underRecoveryDistribution.InvalidUnderRecoveryAmount",
                                new ActionMessage("underRecoveryDistribution.InvalidUnderRecoveryAmount",new Integer(sizeIndex+1)));
                        saveMessages(request, actionMessages);
                        return navigator;
                    }else{
// Modified for Case 3659 - When distributing Underrecovery amount in LITE - Amounts Rounds to the nearest dollar for the distribution amount -Start	
             
//                        DecimalFormat decFormat = new DecimalFormat("0.00");
//                        String strTempForamtted = decFormat.format(result);
//                        int pos = strTempForamtted.indexOf(".");
//                        String strTempRate = "";
//                        pos = strTempForamtted.indexOf(".");
//                        if(pos!=-1){
//                            strTempRate =  strTempForamtted.substring(0,pos);
//                        }else{
//                            strTempRate = strTempForamtted;
//                        }
//                        dynaSetForm.set(UNDER_RECOVER_AMT_WMC,new Double(strTempRate));
                        dynaSetForm.set(UNDER_RECOVER_AMT_WMC,new Double(underRecoverAmt));
// Modified for Case 3659 - When distributing Underrecovery amount in LITE - Amounts Rounds to the nearest dollar for the distribution amount	-End
                        dynaSetForm.set(STR_UNDER_RECOVERY_AMT_WMC,
                                java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US).format(dynaSetForm.get(UNDER_RECOVER_AMT_WMC)));
                    }
                }
                if((dynaSetForm.get(SOURCE_ACCOUNT_WMC).toString().trim()).equals(EMPTY_STRING) || dynaSetForm.get(SOURCE_ACCOUNT_WMC)==null ){
                    actionMessages = new ActionMessages();
                    actionMessages.add("underRecoveryDistribution.error.SourceAccountEmpty",
                            new ActionMessage("underRecoveryDistribution.error.SourceAccountEmpty",new Integer(sizeIndex+1)));
                    saveMessages(request, actionMessages);
                    return navigator;
                }else{
                    String srcAccount = (dynaSetForm.get(SOURCE_ACCOUNT_WMC).toString().trim()).replaceAll("[~,@,#,^,$,@,#,(,),!,`,%, ,&,*,/]","");
                    dynaSetForm.set(SOURCE_ACCOUNT_WMC,srcAccount);
                }
                if((dynaSetForm.get(FISCAL_YEAR_WMC).toString().trim()).equals(EMPTY_STRING) || dynaSetForm.get(FISCAL_YEAR_WMC)==null ){
                    actionMessages = new ActionMessages();
                    actionMessages.add("underRecoveryDistribution.error.FiscalYearEmpty",
                            new ActionMessage("underRecoveryDistribution.error.FiscalYearEmpty",new Integer(sizeIndex+1)));
                    saveMessages(request, actionMessages);
                    return navigator;
                }else{
                    String fiscalYear = (dynaSetForm.get(FISCAL_YEAR_WMC).toString().trim()).replaceAll("[a-z,A-Z,~,@,#,^,$,@,#,(,),!,`,%, ,&,*,/]","");
                    Object result = GenericTypeValidator.formatDouble(fiscalYear);
                    if(result == null){
                        actionMessages = new ActionMessages();
                        actionMessages.add(INVALID_FISCAL_YEAR_ERR_MSG,
                                new ActionMessage(INVALID_FISCAL_YEAR_ERR_MSG,new Integer(sizeIndex+1)));
                        saveMessages(request, actionMessages);
                        return navigator;
                    }else{
                        
                        int pos = result.toString().indexOf(".");
                        String strTempFiscal = "";
                        pos = result.toString().indexOf(".");
                        if(pos!=-1){
                            strTempFiscal =  result.toString().substring(0,pos);
                        }else{
                            strTempFiscal = result.toString();
                        }
                        isYearValid = isFiscalYearValid(strTempFiscal);
                        if(!isYearValid){
                            actionMessages = new ActionMessages();
                            actionMessages.add(INVALID_FISCAL_YEAR_ERR_MSG,
                                    new ActionMessage(INVALID_FISCAL_YEAR_ERR_MSG,new Integer(sizeIndex+1)));
                            saveMessages(request, actionMessages);
                            return navigator;
                        }else{
                            dynaSetForm.set(FISCAL_YEAR_WMC,strTempFiscal);
                        }
                    }
                }
                if((dynaSetForm.get(STR_APPLICABLE_RATES_WMC).toString().trim())!= null){
                    String applicableRate = (dynaSetForm.get(STR_APPLICABLE_RATES_WMC).toString().trim()).replaceAll("[a-z,A-Z,~,@,#,^,$,@,#,(,),!,`,%, ,&,*,/]","");
                    Object result = GenericTypeValidator.formatDouble(applicableRate);
                    if(result == null){
                        actionMessages = new ActionMessages();
                        actionMessages.add("underRecoveryDistribution.ApplicableRates.mask",
                                new ActionMessage("underRecoveryDistribution.ApplicableRates.mask",new Integer(sizeIndex+1)));
                        saveMessages(request, actionMessages);
                        return navigator;
                    }else{
                        int pos = result.toString().indexOf(".");
                        String strTempRate = "";
                        pos = result.toString().indexOf(".");
                        if(pos!=-1){
                            strTempRate =  result.toString().substring(0,pos);
                        }else{
                            strTempRate = result.toString();
                        }
                        if(new Double(strTempRate).doubleValue() > 999.99 ){
                            actionMessages = new ActionMessages();
                            actionMessages.add("underRecoveryDistribution.ApplicableRates.range",
                                    new ActionMessage("underRecoveryDistribution.ApplicableRates.range",new Integer(sizeIndex+1)));
                            saveMessages(request, actionMessages);
                            return navigator;
                            
                        }else{
                            dynaSetForm.set(APPLICABLE_RATES_WMC,new Double(result.toString()));
                            dynaSetForm.set(STR_APPLICABLE_RATES_WMC,result.toString());
                        }
                    }
                }
            }
            // checking whether the rate , on off campus flag source account and fiscal year are not duplicated
            for(int index=0;index < lstModified.size();index++){
                DynaValidatorForm dynaIndexForm = (DynaValidatorForm)lstModified.get(index);
                for(int count = index+1 ; count <lstModified.size(); count++){
                    DynaValidatorForm dynaCountForm = (DynaValidatorForm)lstModified.get(count);
                    if(dynaCountForm.get(FISCAL_YEAR_WMC)!= null && dynaIndexForm.get(FISCAL_YEAR_WMC)!=null &&
                            dynaCountForm.get(SOURCE_ACCOUNT_WMC)!= null && dynaIndexForm.get(SOURCE_ACCOUNT_WMC)!=null &&
                            dynaCountForm.get(APPLICABLE_RATES_WMC)!= null && dynaIndexForm.get(APPLICABLE_RATES_WMC)!=null &&
                            dynaCountForm.get(ON_OFF_CAMPUS_WMC)!= null && dynaIndexForm.get(ON_OFF_CAMPUS_WMC)!=null){
                        if((dynaCountForm.get(FISCAL_YEAR_WMC).equals(dynaIndexForm.get(FISCAL_YEAR_WMC)))
                        &&(dynaCountForm.get(SOURCE_ACCOUNT_WMC).equals(dynaIndexForm.get(SOURCE_ACCOUNT_WMC)))
                        &&(dynaCountForm.get(APPLICABLE_RATES_WMC).equals(dynaIndexForm.get(APPLICABLE_RATES_WMC)))
                        && (dynaCountForm.get(ON_OFF_CAMPUS_WMC).equals(dynaIndexForm.get(ON_OFF_CAMPUS_WMC)))){
                            actionMessages = new ActionMessages();
                            actionMessages.add("underRecoveryDistribution.error.RepeatedRow",
                                    new ActionMessage("underRecoveryDistribution.error.RepeatedRow",new Integer(index+1)));
                            saveMessages(request, actionMessages);
                            return navigator;
                            
                        }
                    }
                    
                }
            }
            double totlistUnderAmt = 0.00;
            if(lstModified != null && lstModified.size() > 0){
                for(int index = 0 ; index < lstModified.size(); index++){
                    DynaValidatorForm dynaPeriod =(DynaValidatorForm)lstModified.get(index);
                    totlistUnderAmt = totlistUnderAmt + ((Double)dynaPeriod.get(UNDER_RECOVER_AMT_WMC)).doubleValue();
                    //COEUSDEV-931 - User can't distribute Under-recovery - Start
                    //Rounding off the UnderRecoveryAmount for two decimal places
                    totlistUnderAmt = Math.round(totlistUnderAmt * 100.00) / 100.00;
                    //COEUSDEV-931 - End

                }
            }
            if(totUnderRecAmt == totlistUnderAmt){
                // deleting the forms removed from the coeusdynabeanslist
                Vector vecDeleted = (Vector)session.getAttribute(DELETED_UR_FORMS);
                if(vecDeleted != null && vecDeleted.size()>0){
                    for(int count =0; count <vecDeleted.size();count++){
                        DynaValidatorForm dynaDelSaveForm = (DynaValidatorForm)vecDeleted.get(count);
                        if(dynaDelSaveForm.get(ACTYPE).equals(TypeConstants.DELETE_RECORD)){
                            formatFields(dynaDelSaveForm);
                            String updateTime = (String)dynaDelSaveForm.get(UPDATE_TIMESTAMP_WMC);
                            dynaDelSaveForm.set(AW_UPDATE_TIMESTAMP_WMC,updateTime);
                            dynaDelSaveForm.set(UPDATE_TIMESTAMP_WMC,dbTimestamp.toString());
                        }
                        webTxnBean.getResults(request,ADD_UPD_UR,dynaDelSaveForm);
                    }
                }
                for(int index=0; index < listSize ;index++){
                    DynaValidatorForm dynaSaveForm = (DynaValidatorForm)lstModified.get(index);
                    if(dynaSaveForm.get(ACTYPE).equals(TypeConstants.UPDATE_RECORD)){
                        formatFields(dynaSaveForm);
                        String updTimeStamp = (String)dynaSaveForm.get(UPDATE_TIMESTAMP_WMC);
                        dynaSaveForm.set(AW_UPDATE_TIMESTAMP_WMC,updTimeStamp);
                        dynaSaveForm.set(UPDATE_TIMESTAMP_WMC,dbTimestamp.toString());
                    }
                    if(dynaSaveForm.get(ACTYPE).equals(TypeConstants.INSERT_RECORD)){
                        formatFields(dynaSaveForm);
                        dynaSaveForm.set(UPDATE_TIMESTAMP_WMC,dbTimestamp.toString());
                    }
                    webTxnBean.getResults(request,ADD_UPD_UR,dynaSaveForm);
                }
            }else{
                actionMessages = new ActionMessages();
                actionMessages.add("underRecoveryDistribution.error.TotalsNotEqual",
                        new ActionMessage("underRecoveryDistribution.error.TotalsNotEqual",
                        java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US).format(new Double(totUnderRecAmt))));
                saveMessages(request, actionMessages);
                return navigator;
                
            }
        }
    }
        session.removeAttribute(DELETED_UR_FORMS);
        List lstUnderRecovery = getUnderRecovery(request,proposalNumber,versionNumber);
        for(int index =0 ; index < lstUnderRecovery.size();index++){
            DynaValidatorForm dynaUnderRecForm = (DynaValidatorForm)lstUnderRecovery.get(index);
            dynaUnderRecForm.set(ACTYPE,TypeConstants.UPDATE_RECORD);
            dynaUnderRecForm.set(UPDATE_USER_WMC,userId);
            dynaUnderRecForm.set(AW_PROPOSAL_NUMBER_WMC,dynaUnderRecForm.get(PROPOSAL_NUMBER_WMC));
            dynaUnderRecForm.set(AW_VERSION_NUMBER_WMC,dynaUnderRecForm.get(VERSION_NUMBER_WMC));
            double applicableRate =((Double)dynaUnderRecForm.get(APPLICABLE_RATES_WMC)).doubleValue();
            dynaUnderRecForm.set(AW_APPLICABLE_RATE_WMC,new Double(applicableRate));
            formatFields(dynaUnderRecForm);
            String fiscalYr  = (String)dynaUnderRecForm.get(FISCAL_YEAR_WMC);
            dynaUnderRecForm.set(AW_FISCAL_YEAR,fiscalYr);
            String onOffCamp = (String)dynaUnderRecForm.get(ON_OFF_CAMPUS_WMC);
            dynaUnderRecForm.set(AW_ON_OFF_CAMPUS,onOffCamp);
            String srcAccount = (String)dynaUnderRecForm.get(SOURCE_ACCOUNT_WMC);
            dynaUnderRecForm.set(AW_SOURCE_ACCOUNT_WMC,srcAccount);
        }
        underRecoveryDynaList.setList(lstUnderRecovery);
        session.setAttribute(UNDERRECOVERYDYNALIST, underRecoveryDynaList);
        return navigator;
    }
    
    
    
    
    /**
     * Method to perform delete action
     * @param underRecoveryDynaList instance of CoeusDynaBeansList
     * @param request instance of HttpServletRequest
     * @throws Exception if exception occur
     * @return instance of String which contains "success"
     */
    private String deleteUnderRecoveryDistribution( CoeusDynaBeansList underRecoveryDynaList,
            HttpServletRequest request)throws Exception{
        String navigator = SUCCESS;
        HttpSession session = request.getSession();
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute(USER+session.getId());
        String userId = userInfoBean.getUserId();
        int rowCount = Integer.parseInt(request.getParameter("removeUrRowCount"));
        // get the vector of deleted forms from session
        Vector vecDeleted = (Vector)session.getAttribute(DELETED_UR_FORMS);
        // if no forms are deleted, create a new vector for grouping deleted forms
        if(vecDeleted == null){
            vecDeleted =  new Vector();
        }
        List lstDeleteData = (List)underRecoveryDynaList.getList();
        // form to be removed from the dyna beans list
        DynaValidatorForm dynaDeleteForm = null;
        if(lstDeleteData != null){
        dynaDeleteForm = (DynaValidatorForm)lstDeleteData.get(rowCount);
        }
        // this is done for rows which are added and deleted without saving i.e for
        // forms which are not from the database
        if(dynaDeleteForm.get(ACTYPE).equals(TypeConstants.INSERT_RECORD)){
            dynaDeleteForm.set(AW_UPDATE_TIMESTAMP_WMC,dynaDeleteForm.get(UPDATE_TIMESTAMP_WMC));
        }
        dynaDeleteForm.set(ACTYPE,TypeConstants.DELETE_RECORD);
        dynaDeleteForm.set(UPDATE_USER_WMC,userId);
        vecDeleted.add(dynaDeleteForm);
        lstDeleteData.remove(dynaDeleteForm);
        underRecoveryDynaList.setList(lstDeleteData);
        session.setAttribute(UNDERRECOVERYDYNALIST, underRecoveryDynaList);
        session.setAttribute(DELETED_UR_FORMS,vecDeleted);
        return navigator;
    }
    
    
    /**
     * Method to perform adding distribution action
     * @param underRecoveryDynaList instance of CoeusDynaBeansList
     * @param request instance of Request
     * @throws Exception if exception occur
     * @return instance of String which contains "success"
     */
    private String addUnderRecovery(HttpServletRequest request,
            CoeusDynaBeansList underRecoveryDynaList) throws Exception{
        String navigator = SUCCESS;
        HttpSession session = request.getSession();
        ProposalBudgetHeaderBean headerBean = (ProposalBudgetHeaderBean)session.getAttribute("ProposalBudgetHeaderBean");
        String proposalNumber = headerBean.getProposalNumber();
        int versionNumber = headerBean.getVersionNumber();
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute(USER+session.getId());
        String userId = userInfoBean.getUserId();
        Timestamp dbTimestamp = prepareTimeStamp();
        if(underRecoveryDynaList!=null){
            List lstDistributionData = null;
            DynaActionForm dynaFormData = underRecoveryDynaList.getDynaForm(request,"underRecoveryDistribution");
            DynaBean dynaNewBean = ((DynaBean)dynaFormData).getDynaClass().newInstance();
            dynaNewBean.set(PROPOSAL_NUMBER_WMC,proposalNumber);
            dynaNewBean.set(VERSION_NUMBER_WMC,new Integer(versionNumber));
            dynaNewBean.set(UPDATE_TIMESTAMP_WMC,dbTimestamp.toString());
            dynaNewBean.set(UPDATE_USER_WMC,userId);
            dynaNewBean.set(ACTYPE,TypeConstants.INSERT_RECORD);
            dynaNewBean.set(APPLICABLE_RATES_WMC,new Double(0));
            dynaNewBean.set(STR_APPLICABLE_RATES_WMC,"0.0");
            dynaNewBean.set(ON_OFF_CAMPUS_WMC,"N");
            dynaNewBean.set(FISCAL_YEAR_WMC,"");
            dynaNewBean.set(UNDER_RECOVER_AMT_WMC,new Double(0));
            dynaNewBean.set(STR_UNDER_RECOVERY_AMT_WMC,"$0.00");
            dynaNewBean.set(SOURCE_ACCOUNT_WMC,"");
            if(underRecoveryDynaList.getList()!=null){
                lstDistributionData = underRecoveryDynaList.getList();
            } else {
                lstDistributionData = new ArrayList();
            }
            lstDistributionData.add(dynaNewBean);
            underRecoveryDynaList.setList(lstDistributionData);
            session.setAttribute(UNDERRECOVERYDYNALIST, underRecoveryDynaList);
        }
        return navigator;
    }
    
    
    
    /**
     * Method to perform getting cost sharing distribution action
     * @param underRecoveryDynaList instance of CoeusDynaBeansList
     * @param request instance of Request
     * @throws Exception if exception occur
     * @return navigator instance of String which contains "success"
     */
    private String  getUnderRecoveryForVersion(HttpServletRequest request,
            CoeusDynaBeansList underRecoveryDynaList) throws Exception{
        String navigator = SUCCESS;
        HttpSession session = request.getSession();
        session.removeAttribute(UNDERRECOVERYDYNALIST);
        session.removeAttribute("UROnOffCampus");
        session.removeAttribute(NO_UNDER_RECOVERY_DISTRIBUTION);
        session.removeAttribute(BUDGET_PERIOD_DATA);
        session.removeAttribute(DELETED_UR_FORMS);
        ProposalBudgetHeaderBean budgetHeader = (ProposalBudgetHeaderBean)session.getAttribute("ProposalBudgetHeaderBean");
        String proposalNumber = budgetHeader.getProposalNumber();
        int versionNumber = budgetHeader.getVersionNumber();
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute(USER+session.getId());
        String userId = userInfoBean.getUserId();
        Vector vecOnOff = setOnOffValues();
        WebTxnBean webTxnBean = new WebTxnBean();
        boolean noURDistr = false;
        session.setAttribute(NO_UNDER_RECOVERY_DISTRIBUTION,new Boolean(noURDistr));
        double totalUnderRec = 0.00;
        HashMap hmPeriodDetails = new HashMap();
        hmPeriodDetails.put(PROPOSAL_NUMBER,proposalNumber);
        hmPeriodDetails.put(VERSION_NUMBER,new Integer(versionNumber));
        Hashtable htGetPeriodDetails = (Hashtable)webTxnBean.getResults(request,GET_BUDGET_PERIOD_DATA,hmPeriodDetails);
        List vecBudgetPeriodBean =  (Vector)htGetPeriodDetails.get(GET_BUDGET_PERIOD_DATA);
        if(vecBudgetPeriodBean != null && vecBudgetPeriodBean.size()>0){
            session.setAttribute(BUDGET_PERIOD_DATA,vecBudgetPeriodBean);
        }
        totalUnderRec= getTotalUnderRecAmt(vecBudgetPeriodBean);
        session.setAttribute("UROnOffCampus",vecOnOff);
        List lstUnderRecData = getUnderRecovery(request,proposalNumber,versionNumber);
        if(lstUnderRecData != null && lstUnderRecData.size()>0){
            for(int index = 0 ; index < lstUnderRecData.size() ; index++){
                DynaValidatorForm dynaUnderRecForm = (DynaValidatorForm)lstUnderRecData.get(index);
                dynaUnderRecForm.set(ACTYPE,TypeConstants.UPDATE_RECORD);
                dynaUnderRecForm.set(UPDATE_USER_WMC,userId);
                dynaUnderRecForm.set(AW_PROPOSAL_NUMBER_WMC,dynaUnderRecForm.get(PROPOSAL_NUMBER_WMC));
                dynaUnderRecForm.set(AW_VERSION_NUMBER_WMC,dynaUnderRecForm.get(VERSION_NUMBER_WMC));
                double applicableRate =((Double)dynaUnderRecForm.get(APPLICABLE_RATES_WMC)).doubleValue();
                dynaUnderRecForm.set(AW_APPLICABLE_RATE_WMC,new Double(applicableRate));
                formatFields(dynaUnderRecForm);
                String fiscalYr  = (String)dynaUnderRecForm.get(FISCAL_YEAR_WMC);
                dynaUnderRecForm.set(AW_FISCAL_YEAR,fiscalYr);
                String onOffCamp = (String)dynaUnderRecForm.get(ON_OFF_CAMPUS_WMC);
                dynaUnderRecForm.set(AW_ON_OFF_CAMPUS,onOffCamp);
                
                String srcAccount = (String)dynaUnderRecForm.get(SOURCE_ACCOUNT_WMC);
                //Added for Case#2402- use a parameter to set the length of the account number throughout app - Start
                int accountNumMaxLength = Integer.parseInt(accountNumberMaxLength);
                 if(srcAccount.length() > accountNumMaxLength){
                    srcAccount = srcAccount.substring(0,accountNumMaxLength);
                }
                dynaUnderRecForm.set(SOURCE_ACCOUNT_WMC,srcAccount);
                //Case#2402 - End
                dynaUnderRecForm.set(AW_SOURCE_ACCOUNT_WMC,srcAccount);
            }
            underRecoveryDynaList.setList(lstUnderRecData);
            session.setAttribute(UNDERRECOVERYDYNALIST,underRecoveryDynaList);
        }else{
            if(totalUnderRec > 0.00){
                // if no data is retrieved fromthe procedure, then data is prepared with the proposal rates 
                //for the given proposal number and version number. From the list of rates obatined, based on the
                // OHRateClass code and OHRateType Code the list is filtered and teh data is dispalyed.
                List lstPropRates = getProposalRates(request,proposalNumber,versionNumber);
                BudgetInfoBean budgetInfoBean = (BudgetInfoBean)session.getAttribute("budgetInfoBean");
                Vector vecUnderRecRates = new Vector();
                if(lstPropRates != null && lstPropRates.size()>0){
                    for(int index =0 ; index < lstPropRates.size(); index++){
                        DynaValidatorForm dynaRateForm = (DynaValidatorForm)lstPropRates.get(index);
                        if(Integer.parseInt(dynaRateForm.get("rateClassCode_wmc").toString())== budgetInfoBean.getOhRateClassCode()
                        && Integer.parseInt(dynaRateForm.get("rateTypeCode_wmc").toString())==budgetInfoBean.getOhRateTypeCode()){
                            // Added for COEUSDEV-743 : Unable to delete IDC rates - Start
                            String onOffCamplusFlag = (String)dynaRateForm.get("onOffCampusFlag_wmc");
                            // In Proposal rates 'N' meant for ON Campus and 'F' meant for OFF Campus, 
                            // where else for Under Recovery 'Y' meant for On Campus and 'N' for Off Campus
                            if("N".equals(onOffCamplusFlag)){
                                dynaRateForm.set("onOffCampusFlag_wmc","Y");
                            }else{
                                dynaRateForm.set("onOffCampusFlag_wmc","N");
                            }
                            // Added for COEUSDEV-743 : Unable to delete IDC rates - End
                            vecUnderRecRates.add(dynaRateForm);
                        }
                    }
                }
                // the data for the rows to be displayed is prepared from the below method
                List lstUnderRec = prepareUnderRecovery(request,vecUnderRecRates,budgetInfoBean);
                underRecoveryDynaList.setList(lstUnderRec);
                session.setAttribute(UNDERRECOVERYDYNALIST,underRecoveryDynaList);
            }else{
                noURDistr = true;
                session.setAttribute(NO_UNDER_RECOVERY_DISTRIBUTION,new Boolean(noURDistr));
            }
        }
        return navigator;
    }
    
    /**
     * Method for getting proposal rates from the procedure
     * @param proposal Number
     * @param versionNumber
     * @param request instance of Request
     * @throws Exception if exception occur
     * @return vecUnderRec instance of List containing the proposal rates
     */
    private List getProposalRates(HttpServletRequest request,String proposalNumber,int versionNumber) throws Exception{
        HashMap hmRecoveryDet = new HashMap();
        WebTxnBean webTxnBean = new WebTxnBean();
        hmRecoveryDet.put(PROPOSAL_NUMBER,proposalNumber);
        hmRecoveryDet.put(VERSION_NUMBER,new Integer(versionNumber));
        Hashtable htGetDetails = (Hashtable)webTxnBean.getResults(request,"getProposalRateData",hmRecoveryDet);
        Vector vecUnderRec  = (Vector)htGetDetails.get("getProposalRateData");
        return  vecUnderRec =  vecUnderRec !=null && vecUnderRec.size() >0 ? vecUnderRec : new Vector();
    }
    
    /**
     * Method for preparing under recovery distribution from the proposal Rates vector and budget info bean
     * @param vecUnderRecRates instance of Vector constains all the proposal rates for the given proposal and version number
     * @param budgetInfoBean instance of BudgetInfoBean
     * @param request instance of Request
     * @throws Exception if exception occur
     * @return vecUnderRecData instance of List containing the UR distribution for the form
     */
    private List prepareUnderRecovery(HttpServletRequest request,
            Vector vecUnderRecRates,BudgetInfoBean budgetInfoBean) throws Exception{
        Vector vecUnderRecData = new Vector();
        UserInfoBean userInfoBean = (UserInfoBean)request.getSession().getAttribute(USER+request.getSession().getId());
        String userId = userInfoBean.getUserId();
        for(int index = 0 ; index < vecUnderRecRates.size(); index++){
            DynaValidatorForm dynaPrepareForm = (DynaValidatorForm)vecUnderRecRates.get(index);
            dynaPrepareForm.set(PROPOSAL_NUMBER_WMC,budgetInfoBean.getProposalNumber());
            dynaPrepareForm.set(VERSION_NUMBER_WMC,new Integer(budgetInfoBean.getVersionNumber()));
            dynaPrepareForm.set(UNDER_RECOVER_AMT_WMC,new Double(0));
            dynaPrepareForm.set(SOURCE_ACCOUNT_WMC,EMPTY_STRING);
            dynaPrepareForm.set(UPDATE_USER_WMC,userId);
            dynaPrepareForm.set(ACTYPE,TypeConstants.INSERT_RECORD);
            formatFields(dynaPrepareForm);
            vecUnderRecData.add(dynaPrepareForm);
        }
        return vecUnderRecData;
    }
    
    /**
     * Method for formatting the Applicable rate field and Under recovery amt field
     * @param dynaUnderRecForm instance of DynaValidatorForm
     * @throws Exception if exception occur
     */
    private void formatFields(DynaValidatorForm dynaUnderRecForm) throws Exception{
        DecimalFormat deciFormat = new DecimalFormat("0.00");
        Double underRec = (Double)dynaUnderRecForm.get(UNDER_RECOVER_AMT_WMC); 
        String strUnderRecAmt = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US).format(new Double(deciFormat.format(underRec)).doubleValue());
        dynaUnderRecForm.set(STR_UNDER_RECOVERY_AMT_WMC,strUnderRecAmt);
        String strApplicRate = deciFormat.format((Double)dynaUnderRecForm.get(APPLICABLE_RATES_WMC)) ;
        dynaUnderRecForm.set(STR_APPLICABLE_RATES_WMC,strApplicRate);
    }
    
    /**
     * Method for On - Off campus values to the comboBoxbean
     * @param dynaUnderRecForm instance of DynaValidatorForm
     * @throws Exception if exception occur
     * @return vecOnOffCampus instance of Vector containing the comboBoxbean
     */
    private Vector setOnOffValues() throws Exception {
        Vector vecOnOffCampus = new Vector();
        ComboBoxBean onBean = new ComboBoxBean();
        onBean.setCode("Y");
        onBean.setDescription("On");
        vecOnOffCampus.add(onBean);
        ComboBoxBean ofBean = new ComboBoxBean();
        ofBean.setCode("N");
        ofBean.setDescription("Off");
        vecOnOffCampus.add(ofBean);
        return vecOnOffCampus;
    }
    
    /**
     * Method for getting the total underrecovery amt
     * @param vecBudgetPeriodBean instance of List
     * @throws Exception if exception occur
     * @return totalUnderRec instance of double containing the total under recovery amt
     */
    private double getTotalUnderRecAmt(List vecBudgetPeriodBean)throws Exception{
        double totalUnderRec = 0.0;
        if(vecBudgetPeriodBean != null && vecBudgetPeriodBean.size() > 0){
            for(int index = 0 ; index < vecBudgetPeriodBean.size(); index++){
                DynaValidatorForm dynaPeriod =(DynaValidatorForm)vecBudgetPeriodBean.get(index);
                totalUnderRec = totalUnderRec + ((Double)dynaPeriod.get("underRecoveryAmount")).doubleValue();
                //COEUSDEV-931 - User can't distribute Under-recovery - Start
                //Rounding off the UnderRecoveryAmount for two decimal places
                totalUnderRec = Math.round(totalUnderRec * 100.00) / 100.00;
                //COEUSDEV-931 - End

            }
        }
        return totalUnderRec;
    }
    
    /** this code has been taken as it is from coeus Premium application code
     * checks if fiscalYear starts is between 1900 and 2099 as per PB validation
     * @param fiscalYear String fiscalYear to validate
     * @return boolean if true a valid fiscal year entry
     */
    private boolean isFiscalYearValid(String fiscalYear) throws Exception {
        
        boolean isValidFiscalYear = false;
        if(fiscalYear.length() > 0 && fiscalYear.length() == 4) {
            int fiscalYearValue = Integer.parseInt(fiscalYear.substring(0,2));
            if(fiscalYearValue >= 19 && fiscalYearValue <= 20) {
                isValidFiscalYear = true;
            }
        }else
            isValidFiscalYear = false;
        return isValidFiscalYear;
    }
    
}
