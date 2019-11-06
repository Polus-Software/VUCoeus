
/*
 * CostSharingDistributionAction.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * Created on April 9, 2007, 12:13 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.wmc.coeuslite.budget.action;



import edu.mit.coeus.bean.CoeusParameterBean;
import edu.mit.coeus.utils.DateUtils;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeuslite.utils.CoeusDynaBeansList;
import edu.wmc.coeuslite.budget.bean.ProposalBudgetHeaderBean;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ParameterUtils;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import edu.mit.coeuslite.utils.CoeusLiteConstants;
import edu.mit.coeuslite.utils.CoeusliteMenuItems;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.validator.DynaValidatorForm;


/**
 *
 * @author divyasusendran
 */
public class CostSharingDistributionAction extends BudgetBaseAction{
    private static final String SUCCESS = "success";
    private static final String COST_DISTRIBUTION_DYNA_LIST = "costDistributionDynaList";
    private static final String PROPOSAL_BUDGET_HEADER_BEAN = "ProposalBudgetHeaderBean";
    private static final String DELETED_FORMS = "deletedCSDForms";
    private static final String BUDGET_PERIOD_DATA = "CSDBudgetPeriodData";
    private static final String GET_CSD_PERIOD_DATA = "getCSDBudgetPeriodData";
    private static final String COST_SHARING_AMOUNT = "costSharingAmount";
    private static final String PROPOSAL_NUMBER = "proposalNumber";
    private static final String VERSION_NUMBER = "versionNumber";
    private static final String ACTYPE = "acType";
    private static final String SOURCE_ACCOUNT = "sourceAccount";
    private static final String FISCAL_YEAR = "costSharingFiscalYear";
    private static final String UPDATE_TIMESTAMP = "updateTimeStamp";
    private static final String UPDATE_USER = "updateUser";
    private static final String COST_SHARING_PERCENT = "costSharingPercentage";
    private static final String STR_COST_SHARING_PERCENT = "strCostSharingPercentage";
    private static final String STR_COST_SHARING_AMOUNT = "strCostSharingAmount";
    private static final String AW_PROPOSAL_NUMBER = "aw_ProposalNumber";
    private static final String AW_VERSION_NUMBER = "aw_VersionNumber";
    private static final String AW_FISCAL_YEAR = "aw_FiscalYear";
    private static final String AW_SOURCE_ACCOUNT = "aw_SourceAccount";
    private static final String AW_UPDATE_TIMESTAMP = "aw_UpdateTimeStamp";
    private static final String USER = "user";
    private String accountNumberMaxLength = "";
    /** Creates a new instance of CostSharingDistributionAction */
    public CostSharingDistributionAction() {
    }
    
    /**
     * Method to perform action
     * @param actionMapping instance of ActionMapping
     * @param actionForm instance of ActionForm
     * @param request instance of Request
     * @param response instance of Response
     * @throws Exception if exception occur
     * @return instance of ActionForward
     */
    public ActionForward performExecute(ActionMapping actionMapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        CoeusDynaBeansList costDistributionDynaList = (CoeusDynaBeansList)actionForm;
        ActionForward actionForward = getCostSharingDistributionData(costDistributionDynaList,actionMapping,request);
        return actionForward;
    }
    
    /**
     * This method will identify which request is comes from which path and
     * navigates to the respective ActionForward
     * @return ActionForward object
     * @param costDistributionDynaList
     * @param actionMapping
     * @throws Exception
     */
    private ActionForward getCostSharingDistributionData(CoeusDynaBeansList costDistributionDynaList
            ,ActionMapping actionMapping,HttpServletRequest request)throws Exception{
        String navigator = SUCCESS;
        HttpSession session = request.getSession();
        //Added for Case#2402- use a parameter to set the length of the account number throughout app - Start
        //To get MAX_ACCOUNT_NUMBER_LENGTH paramter details
        accountNumberMaxLength = ParameterUtils.getMaxAccountNumberLength();
        session.setAttribute("AccountNumberMaxLength",accountNumberMaxLength);
        //Case#2402 - End
            if(actionMapping.getPath().equals("/getCostSharingDistribution")){
                navigator = getCostSharingDistributionDetailsforVersion(costDistributionDynaList,request);
            } else if(actionMapping.getPath().equals("/addCostSharingDistribution")){
                costDistributionDynaList = (CoeusDynaBeansList)request.getSession().getAttribute(COST_DISTRIBUTION_DYNA_LIST);
                navigator = addCostSharingDistribution( costDistributionDynaList,request);
                request.setAttribute("dataModified", "modified");
            } else if(actionMapping.getPath().equals("/deleteCostSharingDistribution")){
                costDistributionDynaList = (CoeusDynaBeansList)request.getSession().getAttribute(COST_DISTRIBUTION_DYNA_LIST);
                navigator = deleteCostSharingDistribution( costDistributionDynaList,request);
                request.setAttribute("dataModified", "modified");
            } else {
                UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute(USER+session.getId());  
                edu.mit.coeuslite.utils.LockBean lockBean = getLockingBean(userInfoBean, (String)session.getAttribute(CoeusLiteConstants.PROPOSAL_NUMBER+session.getId()), request);
                boolean isLockExists = isLockExists(lockBean, lockBean.getModuleKey());
                edu.mit.coeuslite.utils.LockBean lockData = getLockedData(CoeusLiteConstants.BUDGET_LOCK_STR+lockBean.getModuleNumber(), request);
                if(!isLockExists && lockBean.getSessionId().equals(lockData.getSessionId())) {
                    
                    if(actionMapping.getPath().equals("/saveCostSharingDistribution")){
                        costDistributionDynaList = (CoeusDynaBeansList)request.getSession().getAttribute(COST_DISTRIBUTION_DYNA_LIST);
                        navigator = saveCostSharingDistribution( costDistributionDynaList,request);
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
        mapMenuList.put("menuCode",CoeusliteMenuItems.COST_SHARING_DISTRIBUTION_CODE);
        setSelectedMenuList(request, mapMenuList);
        return actionMapping.findForward(navigator);
    }
    
    /**
     * Method to perform delete action
     * @param costDistributionDynaList instance of CoeusDynaBeansList
     * @param request instance of HttpServletRequest
     * @throws Exception if exception occur
     * @return instance of String
     */
    private String deleteCostSharingDistribution( CoeusDynaBeansList costDistributionDynaList,
            HttpServletRequest request)throws Exception{
        String navigator = EMPTY_STRING;
        HttpSession session = request.getSession();
        Timestamp dbTimestamp = prepareTimeStamp();
        ProposalBudgetHeaderBean headerBean = (ProposalBudgetHeaderBean)session.getAttribute(PROPOSAL_BUDGET_HEADER_BEAN);
        String proposalNumber = headerBean.getProposalNumber();
        int versionNumber = headerBean.getVersionNumber();
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute(USER+session.getId());
        String userId = userInfoBean.getUserId();
        int rowCount = Integer.parseInt(request.getParameter("removeCSDRowCount"));
        // get the vector of deleted forms from session
        Vector vecDeleted = (Vector)session.getAttribute(DELETED_FORMS);
        // if no forms are deleted, create a new vector for grouping deleted forms
        if(vecDeleted == null){
             vecDeleted =  new Vector();
        }
        List lstDeleteData = (List)costDistributionDynaList.getList();
        // form to be removed from the dyna beans list
        DynaValidatorForm dynaDeleteForm = (DynaValidatorForm)lstDeleteData.get(rowCount);
        // this is done for rows which are added and deleted without saving i.e for
        // forms which are not from the database
        if(dynaDeleteForm.get(ACTYPE).equals(TypeConstants.INSERT_RECORD)){
            dynaDeleteForm.set(AW_UPDATE_TIMESTAMP,dynaDeleteForm.get(UPDATE_TIMESTAMP));
        }
        dynaDeleteForm.set(ACTYPE,TypeConstants.DELETE_RECORD);
        dynaDeleteForm.set(AW_PROPOSAL_NUMBER,proposalNumber);
        dynaDeleteForm.set(AW_VERSION_NUMBER,new Integer(versionNumber));
        dynaDeleteForm.set(AW_FISCAL_YEAR,dynaDeleteForm.get(FISCAL_YEAR));
        dynaDeleteForm.set(AW_SOURCE_ACCOUNT,dynaDeleteForm.get(SOURCE_ACCOUNT));
        
        String updateTime = (String)dynaDeleteForm.get(UPDATE_TIMESTAMP);
        dynaDeleteForm.set(AW_UPDATE_TIMESTAMP,updateTime);
        dynaDeleteForm.set(UPDATE_TIMESTAMP,dbTimestamp.toString());
        dynaDeleteForm.set(UPDATE_USER,userId);
        vecDeleted.add(dynaDeleteForm);
        lstDeleteData.remove(dynaDeleteForm);
        costDistributionDynaList.setList(lstDeleteData);
        session.setAttribute(COST_DISTRIBUTION_DYNA_LIST, costDistributionDynaList);
        session.setAttribute(DELETED_FORMS,vecDeleted);
        navigator = SUCCESS;
        return navigator;
    }
    
    /**
     * Method to perform save action
     * @param costDistributionDynaList instance of CoeusDynaBeansList
     * @param request instance of Request
     * @throws Exception if exception occur
     * @return instance of String
     */
    private String saveCostSharingDistribution(CoeusDynaBeansList costDistributionDynaList,
            HttpServletRequest request) throws Exception{
        String navigator = SUCCESS;
        HttpSession session = request.getSession();
        boolean isYearValid = true;
        WebTxnBean webTxnBean = new WebTxnBean();
        Timestamp dbTimestamp = prepareTimeStamp();
        ActionMessages actionMessages = null;
        ProposalBudgetHeaderBean headerBean = (ProposalBudgetHeaderBean)session.getAttribute(PROPOSAL_BUDGET_HEADER_BEAN);
        String proposalNumber = headerBean.getProposalNumber();
        int versionNumber = headerBean.getVersionNumber();
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute(USER+session.getId());
        String userId = userInfoBean.getUserId();
        HashMap hmPeriodDetails = new HashMap();
        hmPeriodDetails.put(PROPOSAL_NUMBER,proposalNumber);
        hmPeriodDetails.put(VERSION_NUMBER,new Integer(versionNumber));
        Hashtable htGetPeriodDetails = (Hashtable)webTxnBean.getResults(request,GET_CSD_PERIOD_DATA,hmPeriodDetails);
        List vecBudgetPeriodBean =  (Vector)htGetPeriodDetails.get(GET_CSD_PERIOD_DATA);
        session.setAttribute(BUDGET_PERIOD_DATA,vecBudgetPeriodBean);
        double totCostAmt = getTotalCostSharing(vecBudgetPeriodBean);
        List lstModified = (List)costDistributionDynaList.getList();
       
        // Allow to save only if
        //1)the total cost sharing amount from the period data is equal to the 
        //      sum of all the cost sharing amounts entered in the form else fire the validation
        //2)if there are no rows i.e all rows are deleted then dont fire the validation
        int listSize = lstModified.size();
        if(listSize == 0){
            // deleting the forms removed from the coeusdynabeanslist
            Vector vecDeleted = (Vector)session.getAttribute(DELETED_FORMS);
            if(vecDeleted != null && vecDeleted.size()>0){
                for(int count =0; count <vecDeleted.size();count++){
                    DynaValidatorForm dynaDelSaveForm = (DynaValidatorForm)vecDeleted.get(count);
                    if(dynaDelSaveForm.get(ACTYPE).equals(TypeConstants.DELETE_RECORD)){
                        String strCostSharingAmt = (String)dynaDelSaveForm.get(STR_COST_SHARING_AMOUNT);
                        double dCostSharingAmount = formatStringToDouble(strCostSharingAmt);
                        dynaDelSaveForm.set(COST_SHARING_AMOUNT,new Double(dCostSharingAmount));
                        
                        String costSharingPercent = (String)dynaDelSaveForm.get(STR_COST_SHARING_PERCENT);
                        Double dCostSharingPercent = new Double(formatStringToDouble(costSharingPercent));
                        
                        DecimalFormat deciFormat = new DecimalFormat("0.00");
                        String costSharingPercentage = deciFormat.format(dCostSharingPercent) ;
                        
                        dynaDelSaveForm.set(COST_SHARING_PERCENT,dCostSharingPercent);
                        dynaDelSaveForm.set(STR_COST_SHARING_PERCENT,costSharingPercentage);
                    }
                    webTxnBean.getResults(request,"addUpdPropCostSharing",dynaDelSaveForm);
                }
            }
            
        }
        else{
            
            //Checking whether source account and fiscal year are empty,
        //if fiscal year is not empty then checking
        // whether it is a valid year between 1900 to 2099
        for(int sizeIndex = 0; sizeIndex < lstModified.size(); sizeIndex++){
            DynaValidatorForm dynaSetForm = (DynaValidatorForm)lstModified.get(sizeIndex);
            if((dynaSetForm.get(STR_COST_SHARING_AMOUNT).toString().trim())!= null){
                    String costSharingAmt = (dynaSetForm.get(STR_COST_SHARING_AMOUNT).toString().trim()).replaceAll("[a-z,A-Z,~,@,#,^,$,@,#,(,),!,`,%, ,&,*,/]","");
                    Object result = GenericTypeValidator.formatDouble(costSharingAmt);
                    if(result == null){
                        actionMessages = new ActionMessages();
                        actionMessages.add("costSharingDistribution.InvalidCostSharingAmount",
                                new ActionMessage("costSharingDistribution.InvalidCostSharingAmount",new Integer(sizeIndex+1)));
                        saveMessages(request, actionMessages);
                        return navigator;
                    }else{
//                        Modified for Case 3659 - When distributing Cost Sharing amount in LITE - Amounts Rounds to the nearest dollar for the distribution amount -Start
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
//                        dynaSetForm.set(COST_SHARING_AMOUNT,new Double(strTempRate));
                        
                        dynaSetForm.set(COST_SHARING_AMOUNT,new Double(costSharingAmt));
//                        Modified for Case 3659 - When distributing Cost Sharing amount in LITE - Amounts Rounds to the nearest dollar for the distribution amount -End
                        dynaSetForm.set(STR_COST_SHARING_AMOUNT,
                                java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US).format(dynaSetForm.get(COST_SHARING_AMOUNT)));
                    }
                }
            if((dynaSetForm.get(SOURCE_ACCOUNT).toString().trim()).equals(EMPTY_STRING) || dynaSetForm.get(SOURCE_ACCOUNT)==null ){
                actionMessages = new ActionMessages();
                actionMessages.add("costSharingDistribution.error.SourceAccountEmpty",
                        new ActionMessage("costSharingDistribution.error.SourceAccountEmpty",new Integer(sizeIndex+1)));
                saveMessages(request, actionMessages);
                return navigator;
            }else{
                   String srcAccount = (dynaSetForm.get(SOURCE_ACCOUNT).toString().trim()).replaceAll("[~,@,#,^,$,@,#,(,),!,`,%, ,&,*,/]","");
                   dynaSetForm.set(SOURCE_ACCOUNT,srcAccount);
            }
            if((dynaSetForm.get(FISCAL_YEAR).toString().trim()).equals(EMPTY_STRING) || dynaSetForm.get(FISCAL_YEAR)==null){
                actionMessages = new ActionMessages();
                actionMessages.add("costSharingDistribution.error.FiscalYearEmpty",
                        new ActionMessage("costSharingDistribution.error.FiscalYearEmpty",new Integer(sizeIndex+1)));
                saveMessages(request, actionMessages);
                return navigator;
            }else if(dynaSetForm.get(FISCAL_YEAR)!=null && ((String)dynaSetForm.get(FISCAL_YEAR)).indexOf('.')>0) {
                actionMessages = new ActionMessages();
                actionMessages.add("costSharingDistribution.error.InvalidFiscalYear",
                new ActionMessage("costSharingDistribution.error.InvalidFiscalYear",new Integer(sizeIndex+1)));
                saveMessages(request, actionMessages);
                return navigator;
            }else{
                 String fiscalYear = (dynaSetForm.get(FISCAL_YEAR).toString().trim()).replaceAll("[a-z,A-Z,~,@,#,^,$,@,#,(,),!,`,%, ,&,*,/]","");
                 Object result = GenericTypeValidator.formatDouble(fiscalYear);
                 if(result == null){
                    actionMessages = new ActionMessages();
                    actionMessages.add("costSharingDistribution.error.InvalidFiscalYear",
                            new ActionMessage("costSharingDistribution.error.InvalidFiscalYear",new Integer(sizeIndex+1)));
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
                        actionMessages.add("costSharingDistribution.error.InvalidFiscalYear",
                                new ActionMessage("costSharingDistribution.error.InvalidFiscalYear",new Integer(sizeIndex+1)));
                        saveMessages(request, actionMessages);
                        return navigator;
                    }else{
                        dynaSetForm.set(FISCAL_YEAR,strTempFiscal.trim());
                    }
                }
            }
            if((dynaSetForm.get("strCostSharingPercentage").toString().trim())!= null){
                String costPercent = (dynaSetForm.get("strCostSharingPercentage").toString().trim()).replaceAll("[a-z,A-Z,~,@,#,^,$,@,#,(,),!,`,%, ,&,*,/]","");
                Object result = GenericTypeValidator.formatDouble(costPercent);
                if(result == null){
                    actionMessages = new ActionMessages();
                    actionMessages.add("costSharingDistribution.CostPercent.mask",
                            new ActionMessage("costSharingDistribution.CostPercent.mask",new Integer(sizeIndex+1)));
                    saveMessages(request, actionMessages);
                    return navigator;
                }else{
                    int pos = result.toString().indexOf(".");
                    String strTempPercent = "";
                    pos = result.toString().indexOf(".");
                    if(pos!=-1){
                        strTempPercent =  result.toString().substring(0,pos);
                    }else{
                        strTempPercent = result.toString();
                    }
                    if(new Double(strTempPercent).doubleValue() > 100.00 ){
                        actionMessages = new ActionMessages();
                        actionMessages.add("costSharingDistribution.CostPercent.range",
                                new ActionMessage("costSharingDistribution.CostPercent.range",new Integer(sizeIndex+1)));
                        saveMessages(request, actionMessages);
                        return navigator;
                        
                    }else{
                        dynaSetForm.set("costSharingPercentage",new Double(strTempPercent));
                        dynaSetForm.set(STR_COST_SHARING_PERCENT,strTempPercent);
                    }
                }
            }
        }
        // checking whether the source account and fiscal year are not duplicated
        for(int index=0;index < lstModified.size();index++){
            DynaValidatorForm dynaIndexForm = (DynaValidatorForm)lstModified.get(index);
            for(int count = index+1 ; count <lstModified.size(); count++){
                DynaValidatorForm dynaCountForm = (DynaValidatorForm)lstModified.get(count);
                if(dynaCountForm.get(FISCAL_YEAR)!= null && dynaIndexForm.get(FISCAL_YEAR)!=null &&
                        dynaCountForm.get(SOURCE_ACCOUNT)!= null && dynaIndexForm.get(SOURCE_ACCOUNT)!=null){
                    if((dynaCountForm.get(FISCAL_YEAR).equals(dynaIndexForm.get(FISCAL_YEAR))
                    &&(dynaCountForm.get(SOURCE_ACCOUNT).equals(dynaIndexForm.get(SOURCE_ACCOUNT))))){
                        actionMessages = new ActionMessages();
                        actionMessages.add("costSharingDistribution.error.RepeatedFiscalYearSourceAccount",
                                new ActionMessage("costSharingDistribution.error.RepeatedFiscalYearSourceAccount",dynaCountForm.get(FISCAL_YEAR),dynaCountForm.get(SOURCE_ACCOUNT)));
                        saveMessages(request, actionMessages);
                        return navigator;
                        
                    }
                }
                
            }
        }
        double totlistCostAmt = getTotalCostSharing(lstModified);
        if(totCostAmt == totlistCostAmt){
           
            // deleting the forms removed from the coeusdynabeanslist
            Vector vecDeleted = (Vector)session.getAttribute(DELETED_FORMS);
            if(vecDeleted != null && vecDeleted.size()>0){
                for(int count =0; count <vecDeleted.size();count++){
                    DynaValidatorForm dynaDelSaveForm = (DynaValidatorForm)vecDeleted.get(count);
                    if(dynaDelSaveForm.get(ACTYPE).equals(TypeConstants.DELETE_RECORD)){
                        String strCostSharingAmt = (String)dynaDelSaveForm.get(STR_COST_SHARING_AMOUNT);
                        double dCostSharingAmount = formatStringToDouble(strCostSharingAmt);
                        dynaDelSaveForm.set(COST_SHARING_AMOUNT,new Double(dCostSharingAmount));
                        
                        String costSharingPercent = (String)dynaDelSaveForm.get(STR_COST_SHARING_PERCENT);
                        Double dCostSharingPercent = new Double(formatStringToDouble(costSharingPercent));
                        
                        DecimalFormat deciFormat = new DecimalFormat("0.00");
                        String costSharingPercentage = deciFormat.format(dCostSharingPercent) ;
                        
                        dynaDelSaveForm.set(COST_SHARING_PERCENT,dCostSharingPercent);
                        dynaDelSaveForm.set(STR_COST_SHARING_PERCENT,costSharingPercentage);
                    }
                    webTxnBean.getResults(request,"addUpdPropCostSharing",dynaDelSaveForm);
                }
            }
            
            
            for(int index=0; index < listSize ;index++){
                DynaValidatorForm dynaSaveForm = (DynaValidatorForm)lstModified.get(index);
                if(dynaSaveForm.get(ACTYPE).equals(TypeConstants.UPDATE_RECORD)){
                    // if source account or fiscal year not present(fiscal year shld be validated bfore storing)...raise a warning
                    String strCostSharingAmt = (String)dynaSaveForm.get(STR_COST_SHARING_AMOUNT);
                    double dCostSharingAmount = formatStringToDouble(strCostSharingAmt);
                    dynaSaveForm.set(COST_SHARING_AMOUNT,new Double(dCostSharingAmount));
                    
                    String costSharingPercent = (String)dynaSaveForm.get(STR_COST_SHARING_PERCENT);
                    Double dCostSharingPercent = new Double(formatStringToDouble(costSharingPercent));
                    
                    DecimalFormat deciFormat = new DecimalFormat("0.00");
                    String costSharingPercentage = deciFormat.format(dCostSharingPercent) ;
                    
                    dynaSaveForm.set(COST_SHARING_PERCENT,dCostSharingPercent);
                    dynaSaveForm.set(STR_COST_SHARING_PERCENT,costSharingPercentage);
                    
                    dynaSaveForm.set(AW_PROPOSAL_NUMBER,dynaSaveForm.get(PROPOSAL_NUMBER));
                    dynaSaveForm.set(AW_VERSION_NUMBER,new Integer(dynaSaveForm.get(VERSION_NUMBER).toString()));
                    
                    String updTimeStamp = (String)dynaSaveForm.get(UPDATE_TIMESTAMP);
                    dynaSaveForm.set(AW_UPDATE_TIMESTAMP,updTimeStamp);
                    dynaSaveForm.set(UPDATE_TIMESTAMP,dbTimestamp.toString());
                }
                if(dynaSaveForm.get(ACTYPE).equals(TypeConstants.INSERT_RECORD)){
                    String strCostSharingAmt = (String)dynaSaveForm.get(STR_COST_SHARING_AMOUNT);
                    double dCostSharingAmount = formatStringToDouble(strCostSharingAmt);
                    dynaSaveForm.set(COST_SHARING_AMOUNT,new Double(dCostSharingAmount));
                    
                    String costSharingPercent = (String)dynaSaveForm.get(STR_COST_SHARING_PERCENT);
                    Double dCostSharingPercent = new Double(formatStringToDouble(costSharingPercent));
                    
                    DecimalFormat deciFormat = new DecimalFormat("0.00");
                    String costSharingPercentage = deciFormat.format(dCostSharingPercent) ;
                    
                    dynaSaveForm.set(COST_SHARING_PERCENT,dCostSharingPercent);
                    dynaSaveForm.set(STR_COST_SHARING_PERCENT,costSharingPercentage);
                    
                }
                webTxnBean.getResults(request,"addUpdPropCostSharing",dynaSaveForm);
            }
            
            
            
        }else{
            
                actionMessages = new ActionMessages();
            actionMessages.add("costSharingDistribution.error.TotalsNotEqual",
                    new ActionMessage("costSharingDistribution.error.TotalsNotEqual",
                    java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US).format(new Double(totCostAmt))));
            saveMessages(request, actionMessages);
            return navigator;
            
        }    
            
        }
        session.removeAttribute(DELETED_FORMS);
        HashMap hmdetails = new HashMap();
        List lstCostSharing = getCostSharing(request,hmdetails, proposalNumber, versionNumber);
        // formatting the cost sharing amount and cost sharing percentage before displaying
        for(int index =0 ; index < lstCostSharing.size();index++){
                DynaValidatorForm dynaForm = (DynaValidatorForm)lstCostSharing.get(index);
                dynaForm.set(ACTYPE,TypeConstants.UPDATE_RECORD);
                dynaForm.set(UPDATE_USER,userId);
                Double costSharingAmt = (Double)dynaForm.get(COST_SHARING_AMOUNT);
                String strCostSharingAmount = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US).format(costSharingAmt);
                dynaForm.set(STR_COST_SHARING_AMOUNT,strCostSharingAmount);
                
                dynaForm.set(AW_PROPOSAL_NUMBER,dynaForm.get(PROPOSAL_NUMBER));
                dynaForm.set(AW_VERSION_NUMBER,new Integer(dynaForm.get(VERSION_NUMBER).toString()));
                
                String srcAccount = (String)dynaForm.get(SOURCE_ACCOUNT);
                dynaForm.set(AW_SOURCE_ACCOUNT,srcAccount);
                String fiscalYr = ((String)dynaForm.get(FISCAL_YEAR)).trim();
                dynaForm.set(AW_FISCAL_YEAR,fiscalYr);                
                dynaForm.set(FISCAL_YEAR,fiscalYr);                
                DecimalFormat deciFormat = new DecimalFormat("0.00");
                String strCostSharingPercentage = deciFormat.format((Double)dynaForm.get(COST_SHARING_PERCENT)) ;
                dynaForm.set(STR_COST_SHARING_PERCENT,strCostSharingPercentage);
            }
        costDistributionDynaList.setList(lstCostSharing);
        session.setAttribute(COST_DISTRIBUTION_DYNA_LIST, costDistributionDynaList);
        navigator = SUCCESS;
        return navigator;
    }
    
    
    /**
     * Method to perform adding distribution action
     * @param costDistributionDynaList instance of CoeusDynaBeansList
     * @param request instance of Request
     * @throws Exception if exception occur
     * @return instance of String
     */
    private String addCostSharingDistribution(CoeusDynaBeansList costDistributionDynaList,
            HttpServletRequest request) throws Exception{
        String navigator = EMPTY_STRING;
        HttpSession session = request.getSession();
        ProposalBudgetHeaderBean headerBean = (ProposalBudgetHeaderBean)session.getAttribute(PROPOSAL_BUDGET_HEADER_BEAN);
        String proposalNumber = headerBean.getProposalNumber();
        int versionNumber = headerBean.getVersionNumber();
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute(USER+session.getId());
        String userId = userInfoBean.getUserId();
        Timestamp dbTimestamp = prepareTimeStamp();
        if(costDistributionDynaList!=null){
            List lstDistributionData = null;
            DynaActionForm dynaFormData = costDistributionDynaList.getDynaForm(request,"costSharingDistribution");
            DynaBean dynaNewBean = ((DynaBean)dynaFormData).getDynaClass().newInstance();
            dynaNewBean.set(PROPOSAL_NUMBER,proposalNumber);
            dynaNewBean.set(VERSION_NUMBER,new Integer(versionNumber));
            dynaNewBean.set(UPDATE_TIMESTAMP,dbTimestamp.toString());
            dynaNewBean.set(UPDATE_USER,userId);
            dynaNewBean.set(ACTYPE,TypeConstants.INSERT_RECORD);
            dynaNewBean.set(COST_SHARING_PERCENT,new Double(0));
            dynaNewBean.set(STR_COST_SHARING_PERCENT,"0.0");
            dynaNewBean.set(FISCAL_YEAR,"");
            dynaNewBean.set(COST_SHARING_AMOUNT,new Double(0));
            dynaNewBean.set(STR_COST_SHARING_AMOUNT,"$0.00");
            dynaNewBean.set(SOURCE_ACCOUNT,"");
            if(costDistributionDynaList.getList()!=null){
                lstDistributionData = costDistributionDynaList.getList();
            } else {
                lstDistributionData = new ArrayList();
            }
            lstDistributionData.add(dynaNewBean);
            costDistributionDynaList.setList(lstDistributionData);
            session.setAttribute(COST_DISTRIBUTION_DYNA_LIST, costDistributionDynaList);
        }
        navigator = SUCCESS;
        return navigator;
    }
    
    
    
    /**
     * Method to perform getting cost sharing distribution action
     * @param costDistributionDynaList instance of CoeusDynaBeansList
     * @param request instance of Request
     * @throws Exception if exception occur
     * @return instance of String
     */
    private String getCostSharingDistributionDetailsforVersion(CoeusDynaBeansList costDistributionDynaList,
            HttpServletRequest request) throws Exception{
        
        String navigator = EMPTY_STRING;
        HttpSession session = request.getSession();
        // removing from session
        session.removeAttribute(COST_DISTRIBUTION_DYNA_LIST);
        session.removeAttribute("NoCostDistribution");
        session.removeAttribute(BUDGET_PERIOD_DATA);
        session.removeAttribute(DELETED_FORMS);
        
        Timestamp dbTimestamp = prepareTimeStamp();
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute(USER+session.getId());
        String userId = userInfoBean.getUserId();
        ProposalBudgetHeaderBean headerBean = (ProposalBudgetHeaderBean)session.getAttribute(PROPOSAL_BUDGET_HEADER_BEAN);
        String proposalNumber = headerBean.getProposalNumber();
        int versionNumber = headerBean.getVersionNumber();

        boolean noValue = false;
        session.setAttribute("NoCostDistribution",new Boolean(noValue));
        Vector vecCostSharing = null;
        WebTxnBean  webTxnBean = new WebTxnBean();
        // getting budget period data for the given proposal and version number
        HashMap hmPeriodDetails = new HashMap();
        hmPeriodDetails.put(PROPOSAL_NUMBER,proposalNumber);
        hmPeriodDetails.put(VERSION_NUMBER,new Integer(versionNumber));
        Hashtable htGetPeriodDetails = (Hashtable)webTxnBean.getResults(request,GET_CSD_PERIOD_DATA,hmPeriodDetails);
        List vecBudgetPeriodBean =  (Vector)htGetPeriodDetails.get(GET_CSD_PERIOD_DATA);
        if(vecBudgetPeriodBean != null && vecBudgetPeriodBean.size()>0){
            session.setAttribute(BUDGET_PERIOD_DATA,vecBudgetPeriodBean);
        }
        // getting the cost sharing distribution data  from the procedure
        // if the vector fromthe procedure is empty, then prepare the data from the
        //vector containing  budget period data
        HashMap hmdetails = new HashMap();
        vecCostSharing = getCostSharing(request,hmdetails, proposalNumber, versionNumber);
        if(vecCostSharing != null && vecCostSharing.size()>0){
            for(int index =0 ; index < vecCostSharing.size();index++){
                DynaValidatorForm dynaForm = (DynaValidatorForm)vecCostSharing.get(index);
                dynaForm.set(ACTYPE,TypeConstants.UPDATE_RECORD);
                dynaForm.set(UPDATE_USER,userId);
                Double costSharingAmt = (Double)dynaForm.get(COST_SHARING_AMOUNT);
                String strCostSharingAmount = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US).format(costSharingAmt);
                dynaForm.set(STR_COST_SHARING_AMOUNT,strCostSharingAmount);
                dynaForm.set(AW_PROPOSAL_NUMBER,dynaForm.get(PROPOSAL_NUMBER));
                dynaForm.set(AW_VERSION_NUMBER,new Integer(dynaForm.get(VERSION_NUMBER).toString()));
                String srcAccount = (String)dynaForm.get(SOURCE_ACCOUNT);
                //Added for Case#2402- use a parameter to set the length of the account number throughout app - Start
                int accountNumMaxLength = Integer.parseInt(accountNumberMaxLength);
                 if(srcAccount.length() > accountNumMaxLength){
                    srcAccount = srcAccount.substring(0,accountNumMaxLength);
                }
                dynaForm.set(SOURCE_ACCOUNT,srcAccount);
                //Case#2402 - End
                dynaForm.set(AW_SOURCE_ACCOUNT,srcAccount);
                String fiscalYr = ((String)dynaForm.get(FISCAL_YEAR)).trim();
                dynaForm.set(AW_FISCAL_YEAR,fiscalYr);                
                dynaForm.set(FISCAL_YEAR,fiscalYr);                
                DecimalFormat deciFormat = new DecimalFormat("0.00");
                String strCostSharingPercentage = deciFormat.format((Double)dynaForm.get(COST_SHARING_PERCENT)) ;
                dynaForm.set(STR_COST_SHARING_PERCENT,strCostSharingPercentage);
                
            }
            costDistributionDynaList.setList(vecCostSharing);
            session.setAttribute(COST_DISTRIBUTION_DYNA_LIST,costDistributionDynaList);
        } else{
            
            // if the cost sharing amount from all periods is zero
            // it means there is no cost sharing distribution and a message is fired

            double totCostAmt = 0.0;
            totCostAmt = getTotalCostSharing(vecBudgetPeriodBean);
            if(totCostAmt > 0.0){
                vecCostSharing = getProposalCostSharingBean( vecBudgetPeriodBean);
                if(vecCostSharing != null && vecCostSharing.size()>0){
                    for(int index =0 ; index < vecCostSharing.size();index++){
                        DynaValidatorForm dynaForm = (DynaValidatorForm)vecCostSharing.get(index);
                        dynaForm.set(UPDATE_TIMESTAMP,dbTimestamp.toString());
                        dynaForm.set(UPDATE_USER,userId);
                        Double costSharingAmt = (Double)dynaForm.get(COST_SHARING_AMOUNT);
                        String strCostSharingAmount = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US).format(costSharingAmt);
                        dynaForm.set(STR_COST_SHARING_AMOUNT,strCostSharingAmount);
                        DecimalFormat deciFormat = new DecimalFormat("0.00");
                        String strCostSharingPercentage = deciFormat.format((Double)dynaForm.get(COST_SHARING_PERCENT)) ;
                        dynaForm.set(STR_COST_SHARING_PERCENT,strCostSharingPercentage);
                    }
                    costDistributionDynaList.setList(vecCostSharing);
                    session.setAttribute(COST_DISTRIBUTION_DYNA_LIST,costDistributionDynaList);
                }
            } else{
                noValue = true;
                session.setAttribute("NoCostDistribution",new Boolean(noValue));
            }
            
        }
        navigator = SUCCESS;
        return navigator;
    }
    
    /**
     * Method to prepare a data for the form from BudgetPeriod data
     * @param vecBudgetPeriodBean instance of List containing Budget Period Data
     * @throws Exception if exception occur
     * @return instance of Vector
     */
    public Vector getProposalCostSharingBean(List vecBudgetPeriodBean) throws Exception {
        
        Vector vecBeans = new Vector();
        for(int index=0;index < vecBudgetPeriodBean.size(); index++) {
            DynaValidatorForm dynaPeriodForm = (DynaValidatorForm)vecBudgetPeriodBean.get(index);
            Timestamp dbTimestamp = prepareTimeStamp();
            dynaPeriodForm.set(ACTYPE,TypeConstants.INSERT_RECORD);
            dynaPeriodForm.set(COST_SHARING_PERCENT,new Double(0));
            String strFiscalYear = getFiscalYear((Date)dynaPeriodForm.get("startDate")+"");
            dynaPeriodForm.set(FISCAL_YEAR,strFiscalYear);
            dynaPeriodForm.set(SOURCE_ACCOUNT,"");
            dynaPeriodForm.set(UPDATE_TIMESTAMP,dbTimestamp.toString());
            vecBeans.add(dynaPeriodForm);
        }
        return vecBeans;
    }
    
    /**
     * Method which returns the Fiscal year fromthe given date
     * @param datesql instance of String having sql date
     * @throws Exception if exception occur
     * @return instance of String containing Fiscal year
     */
    public String getFiscalYear(String datesql) throws Exception {
        try {
            String fiscalYear = null;
            DateUtils dateUtils = new DateUtils();
            
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
            String strDatesimple = dateUtils.formatDate(datesql, "MM/dd/yyyy");
            Date date =  simpleDateFormat.parse(strDatesimple);
            
            GregorianCalendar gregorianCalendar = new GregorianCalendar();
            gregorianCalendar.setTime(date);
            
            int month = gregorianCalendar.get(Calendar.MONTH)+1;
            int year = gregorianCalendar.get(Calendar.YEAR);
            
            String formatDate = dateUtils.formatDate("1998-06-30", "MM/dd/yyyy");
            Date utilFormatDate = simpleDateFormat.parse(formatDate);
            gregorianCalendar.setTime(utilFormatDate);
            
            int monthofFormatDate = gregorianCalendar.get(Calendar.MONTH)+1;
            
            
            if(month > monthofFormatDate ) {
                year = year+1;
                fiscalYear = year+"" ;
            } else
                fiscalYear = year+"" ;
            
            return fiscalYear;
        }catch(Exception e) {
            e.getMessage();
        }
        return null;
    }
    
    /**
     * Method to get total cost sharing amount for all periods from BudgetPeriod data
     * @param actionMapping instance of List having Budget period Data
     * @return instance of Double containing totalCostSharing Amount
     */
    private double getTotalCostSharing(List vecBudgetPeriodBean) throws Exception{
        double totCostAmt = 0.00;
        for(int index = 0 ; index < vecBudgetPeriodBean.size(); index++){
            DynaValidatorForm dynaCostAmt = (DynaValidatorForm)vecBudgetPeriodBean.get(index);
            if(dynaCostAmt.get(COST_SHARING_AMOUNT) !=null){
                totCostAmt = totCostAmt + ((Double)dynaCostAmt.get(COST_SHARING_AMOUNT)).doubleValue();
                //Rounding off the UnderRecoveryAmount for two decimal places.
                totCostAmt = Math.round(totCostAmt * 100.00) / 100.00;
            }
        }
        return totCostAmt;
    }
    
    /** this code has been taken as it is from coeus Premium application code
     * checks if fiscalYear starts is between 1900 and 2099 as per PB validation
     * @param fiscalYear String fiscalYear to validate
     * @return boolean if true a valid fiscal year entry
     */
    private boolean isFiscalYearValid(String fiscalYear) throws Exception {
        
        boolean isValidFiscalYear = false;
       // modified for COEUSQA-1426: Ability to enter data besides YYYY  start
       // now fiscal year will accpet values from 1-9999
       //previously fiscal year was accepting only YYYY format
            if(fiscalYear.length() > 0 && fiscalYear.length() <=4) {
               isValidFiscalYear = true;
            }
            else {
	       isValidFiscalYear = false;	
            }
         // to check whether fiscal year is '0'
           if(Integer.parseInt(fiscalYear)==0)
                isValidFiscalYear = false;
        
        // modified for COEUSQA-1426: Ability to enter data besides YYYY End
        return isValidFiscalYear;
    }
}
