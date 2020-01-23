/*
 * ArraAwardDetailsAction.java
 *
 * Created on August 11, 2009, 10:45 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
/* PMD check performed, and commented unused imports and variables on 27-NOV-2010
 * by Keerthy Jayaraj
 */
package edu.mit.coeuslite.arra.action;

import edu.mit.coeus.arra.ArraAuthorization;
import edu.mit.coeus.arra.bean.ArraReportTxnBean;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeuslite.utils.CoeusLiteConstants;
import edu.mit.coeuslite.utils.CoeusliteMenuItems;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeuslite.utils.DateUtils;
import edu.mit.coeuslite.utils.LockBean;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;
import org.apache.commons.beanutils.BeanUtilsBean;
/**
 *
 * @author keerthyjayaraj
 */
public class ArraAwardDetailsAction extends ArraBaseAction{
    
    private static final String GET_AWARD_DETAILS = "/getArraDetails";
//    private static final String COPY_AWARD_DETAILS = "/copyArraDetails";
    private static final String FETCH_AWARD_DETAILS = "/fetchArraDetails";
    private static final String SAVE_AWARD_DETAILS = "/saveArraAwardDetails";
    private static final String HIGHLY_COMP_ORG_ID = "000001";
    private static final String ARGUMENT_VALUE_LIST ="getArgValueList";
    private static final String PROJECT_STATUS ="arraAwardProjectStatus";
    private static final String ACTIVITY_TYPE ="arraAwardActivityTypes";
    private static final String AC_TYPE = "acType";
    private static final String AW_UPDATE_TIMESTAMP = "awUpdateTimestamp";
    private static final String UPDATE_TIMESTAMP = "updateTimestamp";
    
    
    private static final String COLUMN_PROPERTIES = "columnProperties";
    private static final String ARRA_REPORT_AWARDS_TABLE_NAME = "OSP$ARRA_REPORT_AWARDS";
    private static final String EDIT_COLUMN_PROPERTIES_RIGHT = "canEditAllRight";
    
    private static final String DATE_SEPARATERS = ":/.,|-";
    private static final String SIMPLE_DATE_FORMAT = "MM/dd/yyyy";
    
    /** Creates a new instance of ArraAwardDetailsAction */
    public ArraAwardDetailsAction() {
    }
    
    public ActionForward performExecute(ActionMapping actionMapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        HttpSession session = request.getSession();
        String navigator = EMPTY_STRING;
        DynaValidatorForm dynaForm = (DynaValidatorForm)actionForm;
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        boolean canOpenArraDetails = true;
        //This is called on initial page load
        if(actionMapping.getPath().equals(GET_AWARD_DETAILS)){
            String reportNo =  request.getParameter("arraReportNo");
            String mitAwardNo = request.getParameter("arraReportAwardNo");
            String versionNumber = request.getParameter("arraVersionNumber");
            ArraAuthorization arraAuthorization = new ArraAuthorization();
            ArraReportTxnBean arraReprotTxnBean = new  ArraReportTxnBean();
           
            int intReportNumber = 0;
            int intVersionNumber = 0;
            try{
                if(versionNumber != null){
                    intVersionNumber = new Integer(versionNumber).intValue();
                }
                if(reportNo != null){
                    intReportNumber = new Integer(reportNo).intValue();
                }
            } catch(Exception ex){
                ex.printStackTrace();
            }
            // Arra Phase 2 changes
//            if(arraAuthorization.canModifyArraReportDetails(userInfoBean.getUserId(), userInfoBean.getPersonId(), mitAwardNo)){ //checks for Modify Rights
            if(arraAuthorization.canModifyArraReportDetails(userInfoBean.getUserId(), userInfoBean.getPersonId(), mitAwardNo, intVersionNumber, intReportNumber)){ //checks for Modify Rights    
                //Check for editing Field rights
                // Arra Phase 2 changes
//                boolean canEditAll = arraAuthorization.checkUserHasMaintainArraRight(userInfoBean.getUserId(),mitAwardNo); 
                boolean canEditAll = arraAuthorization.canModifyAllArraReportFields(userInfoBean.getUserId(),mitAwardNo, intVersionNumber, intReportNumber); 
                HashMap columnProperties = arraReprotTxnBean.checkArraColumnProperties(ARRA_REPORT_AWARDS_TABLE_NAME);
                session.setAttribute(COLUMN_PROPERTIES,columnProperties);
                session.setAttribute(EDIT_COLUMN_PROPERTIES_RIGHT,new Boolean(canEditAll));
                getArraHeader(reportNo,mitAwardNo,request);
                fetchAwardInfo(request);
                lockArraReportForEdit(mitAwardNo, new Integer(reportNo).intValue(), request);
                getArraMenus(request,mitAwardNo);
                navigator = fetchArraAwardDetails(reportNo,mitAwardNo,request,dynaForm);
                // Arra Phase 2 changes
//            }else if(arraAuthorization.canViewArraReportDetails(userInfoBean.getUserId(), mitAwardNo)){ //Checks for View Rights
            }else if(arraAuthorization.canViewArraReportDetails(userInfoBean.getUserId(), mitAwardNo, intVersionNumber, intReportNumber)){ 
                session.setAttribute(CoeusLiteConstants.MODE_DETAILS+session.getId(),CoeusLiteConstants.DISPLAY_MODE);
                session.setAttribute(EDIT_COLUMN_PROPERTIES_RIGHT,new Boolean(false));
                getArraHeader(reportNo,mitAwardNo,request);
                fetchAwardInfo(request);
                getArraMenus(request,mitAwardNo);
                navigator = fetchArraAwardDetails(reportNo,mitAwardNo,request,dynaForm);
            }else {
                canOpenArraDetails = false;
                ActionMessages messages = new ActionMessages();
                messages.add("noRight", new ActionMessage("arraList.error.noRight"));
                saveMessages(request, messages);
                navigator = "noRight" ;
            }
            //This is called for roloading the report details
        }else if(actionMapping.getPath().equals(FETCH_AWARD_DETAILS)){
            String reportNo =  (String)session.getAttribute(ARRA_REPORT_NUMBER);
            String mitAwardNo = (String)session.getAttribute(ARRA_REPORT_AWARD_NUMBER);
            getArraHeader(reportNo,mitAwardNo,request);
            getArraMenus(request,mitAwardNo);
//            // Try to lock the Arr Report.
//            lockArraReportForEdit(mitAwardNo, new Integer(reportNo).intValue(), request);
            navigator = fetchArraAwardDetails(reportNo,mitAwardNo,request,dynaForm);
            //This is called for save action.
        }else  if(actionMapping.getPath().equals(SAVE_AWARD_DETAILS)){
            navigator = saveArraAwardDetails(request,dynaForm);
        }        
        if(canOpenArraDetails) {
            setSelectedStatusMenu(CoeusliteMenuItems.ARRA_DETAILS_MENU_CODE,session);
        }
        // Commented with COEUSDEV-624/COEUSDEV-603: Arra award type Issues
//        WebTxnBean webTxnBean = new WebTxnBean();
//         Hashtable htAwardTypes = (Hashtable)webTxnBean.getResults(request , "getPropAwardTypes" , null );
//         Vector vecAwrdType =(Vector)htAwardTypes.get("getPropAwardTypes");
         
         // Modified for COEUSDEV-537_My ARRA - Add two new columns to satisfy contracts_Start
         // Modified for ARRA modifications_Filtering Award Types(only Grant and Contract)
//         request.setAttribute("awardTypes",vecAwrdType);  
//         Vector vecTypes = new Vector();
//         if(vecAwrdType != null && vecAwrdType.size() > 0){
//             ComboBoxBean cmbBean = null;
//             for(Object obj : vecAwrdType ){
//                 cmbBean = (ComboBoxBean)obj;
//                 if("Grant".equalsIgnoreCase(cmbBean.getDescription()) ||
//                         "Contract".equalsIgnoreCase(cmbBean.getDescription())){
//                     vecTypes.add(cmbBean);
//                 }
//             }             
//         }
//         request.setAttribute("awardTypes",vecTypes);       
         // Modified for COEUSDEV-537_My ARRA - Add two new columns to satisfy contracts_End
         // COEUSDEV-624/COEUSDEV-603: Arra award type Issues
        return actionMapping.findForward(navigator);
    }
    
    private String fetchArraAwardDetails(String arraReportNumber , String mitAwardNumber, HttpServletRequest request,DynaValidatorForm dynaValidatorForm) throws Exception{
        String navigator = "success";
        Vector vecArraDetails = getArraAwardDetails(arraReportNumber,mitAwardNumber,request);
        if(vecArraDetails!=null && vecArraDetails.size() >0 ){
            
            HashMap hmData = null;
            DynaValidatorForm arraDetailsForm = (DynaValidatorForm)vecArraDetails.get(0);
            String awardType = (String)arraDetailsForm.get("awardType");
//            String reportType = "contractReport";
            // Added with COEUSDEV-624/COEUSDEV-603: Arra award type Issues
            if(GRANT_AWARD_TYPE.equalsIgnoreCase(awardType)){
                awardType = GRANT_AWARD_TYPE;
            }else{
                awardType = CONTRACT_AWARD_TYPE;
            }
            // COEUSDEV-624/COEUSDEV-603:End
            arraDetailsForm.set("awardType",awardType);
            request.getSession().setAttribute("awardType",awardType);
            //Fetch infra structure contact details
            if(arraDetailsForm.get("infraContactId") != null){
                hmData = new HashMap();
                hmData.put("rolodexId",arraDetailsForm.get("infraContactId"));
                Vector vecRolodex = (Vector)getRolodexDetails(request,hmData);
                if(vecRolodex != null && vecRolodex.size() >0 ){
                    DynaValidatorForm dynaAddress = (DynaValidatorForm)vecRolodex.get(0);
                    String address = getCompleteAddress(dynaAddress);
                    arraDetailsForm.set("infraContactAddress",address);
                }
            }
            //Fetch primary place of performance details
            Object primPlaceOfPerf = arraDetailsForm.get("primPlaceOfPerfId");
            if(primPlaceOfPerf != null){
                hmData = new HashMap();
                hmData.put("rolodexId",primPlaceOfPerf);
                Vector vecRolodex = (Vector)getRolodexDetails(request,hmData);
                if(vecRolodex != null && vecRolodex.size() >0 ){
                    DynaValidatorForm dynaAddress = (DynaValidatorForm)vecRolodex.get(0);
                    String address = getCompleteAddress(dynaAddress);
                    arraDetailsForm.set("primPlaceOfPerfAddress",address);
                }
            }
            //Find total no of jobs
//            Double noOfAwdJobs = new Double((String)arraDetailsForm.get("noOfJobs")==null?"0":(String)arraDetailsForm.get("noOfJobs"));
//            Double noOfSubJobs = new Double((String)arraDetailsForm.get("jobsAtSubs"));
//            double total = 0;
//            if(noOfAwdJobs!=null){
//                total = total + noOfAwdJobs.doubleValue();
//            }
//            if(noOfSubJobs!=null){
//                total = total + noOfSubJobs.doubleValue();
//            }
//            total=Math.round(total*Math.pow(10,2))/Math.pow(10,2);
//            arraDetailsForm.set("totalJobs",String.valueOf(total));
            edu.mit.coeuslite.utils.DateUtils dateUtils = new DateUtils();
            String tempAwardDate = (String)arraDetailsForm.get("awardDate");
            
            if(tempAwardDate != null && !tempAwardDate.equals(EMPTY_STRING)){
                tempAwardDate = dateUtils.formatDate(tempAwardDate,SIMPLE_DATE_FORMAT);
                arraDetailsForm.set("awardDate",tempAwardDate.toString());
            }
            String finalFlag =EMPTY_STRING;
            if(arraDetailsForm.get("finalFlag").toString().trim().equalsIgnoreCase("Y")){
                finalFlag = "on";
            }else{
                finalFlag = "off";
            }
            arraDetailsForm.set("finalFlag",finalFlag);
            //Fetch highly compensated individuals
            HttpSession session = request.getSession();
            Vector vctHighlyComp = getHighlyCompensatedIndividuals(request,HIGHLY_COMP_ORG_ID);
            session.setAttribute("arraHighlyCompensated",vctHighlyComp);
            Vector vctJobsCreated = getJobsCreated(arraReportNumber,mitAwardNumber,request);
            session.setAttribute("arraAwardJobsCreated",vctJobsCreated);
            BeanUtilsBean copyBean = new BeanUtilsBean();
            copyBean.copyProperties(dynaValidatorForm,arraDetailsForm);
        }
        
        return navigator;
    }
    
    /*To save the arra award details. */
    private String saveArraAwardDetails(HttpServletRequest request,DynaValidatorForm dynaValidatorForm) throws Exception{
        String navigator = "success";
        WebTxnBean webTxnBean = new WebTxnBean();
        double dblValue;
        String strValue;
        int intValue;
        
        HttpSession session = request.getSession();
        String reportNumber = (String)session.getAttribute(ARRA_REPORT_NUMBER);
        UserInfoBean userInfoBean = (UserInfoBean) session.getAttribute("user"+session.getId());
        LockBean lockBean = getArraLockingBean(userInfoBean, (String)session.getAttribute(ARRA_REPORT_AWARD_NUMBER), 
               new Integer(reportNumber).intValue(),request);
        // Check if the lock for this recird already exists in the DB
        boolean isLockExists = isLockExists(lockBean, lockBean.getModuleKey());
        LockBean lockData = getLockedData(ARRA_LOCK_STRING+lockBean.getModuleNumber(), request);
        if(isLockExists || !lockBean.getSessionId().equals(lockData.getSessionId())) {
        // If the Report was locked from a different session.    
            String errMsg = "release_lock_for";
            String lockedArraAwardNumber = lockBean.getModuleNumber().substring(0,10);
            ActionMessages messages = new ActionMessages();
            messages.add("errMsg", new ActionMessage(errMsg,lockBean.getModuleKey(),lockedArraAwardNumber));
            saveMessages(request, messages);
        }else{
            //set finalReportFolag value
            String finalFlag = (String) dynaValidatorForm.get("finalFlag");
            if("on".equalsIgnoreCase(finalFlag)){
                dynaValidatorForm.set("finalFlag", "Y");
            }else{
                dynaValidatorForm.set("finalFlag", "N");
            }
            //validate Amount
            strValue  = (String)dynaValidatorForm.get("strTotalInfraExpenditure");
            if(strValue!=null){
                try{
                    dblValue = formatStringToDouble(strValue);
                }catch(NumberFormatException ne){
                    dblValue = 0;
                }
                dynaValidatorForm.set("totalInfraExpenditure", new Double(dblValue));
            }
            
            //validate jobs
            strValue  = (String)dynaValidatorForm.get("noOfJobs");
            if(strValue!=null){
                try{
                    dblValue = formatStringToDouble(strValue);
                }catch(NumberFormatException ne){
                    dblValue = 0;
                }
                dynaValidatorForm.set("noOfJobs", String.valueOf(dblValue));
            }
            //validate awardAmount
            strValue  = (String)dynaValidatorForm.get("strAwardAmount");
            if(strValue!=null){
                try{
                    dblValue = formatStringToDouble(strValue);
                }catch(NumberFormatException ne){
                    dblValue = 0;
                }
                dynaValidatorForm.set("awardAmount", new Double(dblValue));
            }
            //validate totalExpenditure
            strValue  = (String)dynaValidatorForm.get("strTotalExpenditure");
            if(strValue!=null){
                try{
                    dblValue = formatStringToDouble(strValue);
                }catch(NumberFormatException ne){
                    dblValue = 0;
                }
                dynaValidatorForm.set("totalExpenditure",  new Double(dblValue));
            }
              //validate totalFederalInvoiced
            strValue  = (String)dynaValidatorForm.get("strTotalFederalInvoiced");
            if(strValue!=null){
                try{
                    dblValue = formatStringToDouble(strValue);
                }catch(NumberFormatException ne){
                    dblValue = 0;
                }
                dynaValidatorForm.set("totalFederalInvoiced",  new Double(dblValue));
            }
            //validate indSubAwardAmount
            strValue  = (String)dynaValidatorForm.get("strIndSubAwardAmount");
            if(strValue!=null){
                try{
                    dblValue = formatStringToDouble(strValue);
                }catch(NumberFormatException ne){
                    dblValue = 0;
                }
                dynaValidatorForm.set("indSubAwardAmount",  new Double(dblValue));
            }
            //validate vendorLess25KAmount
            strValue  = (String)dynaValidatorForm.get("strVendorLess25KAmount");
            if(strValue!=null){
                try{
                    dblValue = formatStringToDouble(strValue);
                }catch(NumberFormatException ne){
                    dblValue = 0;
                }
                dynaValidatorForm.set("vendorLess25KAmount",  new Double(dblValue));
            }
            //validate subAwdLess25KAmount
            strValue  = (String)dynaValidatorForm.get("strSubAwdLess25KAmount");
            if(strValue!=null){
                try{
                    dblValue = formatStringToDouble(strValue);
                }catch(NumberFormatException ne){
                    dblValue = 0;
                }
                dynaValidatorForm.set("subAwdLess25KAmount",  new Double(dblValue));
            }
            //validate indSubAwards
            strValue  = (String)dynaValidatorForm.get("strIndSubAwards");
            if(strValue!=null){
                try{
                    intValue = Integer.parseInt(strValue);
                }catch(NumberFormatException ne){
                    intValue = 0;
                }
                dynaValidatorForm.set("indSubAwards",  new Integer(intValue));
            }
            //validate vendorLess25K
            strValue  = (String)dynaValidatorForm.get("strVendorLess25K");
            if(strValue!=null){
                try{
                    intValue = Integer.parseInt(strValue);
                }catch(NumberFormatException ne){
                    intValue = 0;
                }
                dynaValidatorForm.set("vendorLess25K",  new Integer(intValue));
            }
            //validate SubAwdLess25K
            strValue  = (String)dynaValidatorForm.get("strSubAwdLess25K");
            if(strValue!=null){
                try{
                    intValue = Integer.parseInt(strValue);
                }catch(NumberFormatException ne){
                    intValue = 0;
                }
                dynaValidatorForm.set("subAwdLess25K",  new Integer(intValue));
            }
            //validate award Date
            DateUtils dateUtils = new DateUtils();
            String tempAwardDate = (String)dynaValidatorForm.get("awardDate");
            
            if(tempAwardDate != null && !tempAwardDate.equals(EMPTY_STRING)){
                tempAwardDate = dateUtils.formatDate(tempAwardDate,DATE_SEPARATERS,SIMPLE_DATE_FORMAT);
                dynaValidatorForm.set("awardDate",tempAwardDate.toString());
            }
            
            dynaValidatorForm.set(AC_TYPE,TypeConstants.UPDATE_RECORD);
            dynaValidatorForm.set(AW_UPDATE_TIMESTAMP,dynaValidatorForm.get(UPDATE_TIMESTAMP));
            Timestamp dbTimestamp = prepareTimeStamp();
            dynaValidatorForm.set(UPDATE_TIMESTAMP,dbTimestamp.toString());
            webTxnBean.getResults(request,"updArraAwardDetails",dynaValidatorForm);
        }
        return navigator;
    }
    
    /* To get the arra award details from db*/
    private Vector getArraAwardDetails(String arraReportNumber , String mitAwardNumber, HttpServletRequest request) throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        HashMap hmArraData = new HashMap();
        hmArraData.put("arraReportNumber", new Integer(arraReportNumber) );
        hmArraData.put("mitAwardNumber", mitAwardNumber );
        Hashtable htArraDetails = (Hashtable)webTxnBean.getResults(request, "getArraAwardDetails",hmArraData);
        Vector vecArraDetails =  (Vector)htArraDetails.get("getArraAwardDetails");
        return vecArraDetails;
    }
    
    /*To fetch all the code table values.*/
    protected void fetchAwardInfo(HttpServletRequest request)throws Exception{
        
        HttpSession session = request.getSession();
        WebTxnBean webTxnBean = new WebTxnBean();
        HashMap hmlookupArgument = new HashMap();
        hmlookupArgument.put("argumentName","ArraProjectStatus");
        Hashtable htArraAwdData = (Hashtable)webTxnBean.getResults(request,ARGUMENT_VALUE_LIST,hmlookupArgument);
        Vector vecPrjStatus = (Vector)htArraAwdData.get(ARGUMENT_VALUE_LIST);
        session.setAttribute(PROJECT_STATUS,vecPrjStatus);
        
        hmlookupArgument = new HashMap();
        hmlookupArgument.put("argumentName","Arra_Activity_Type");
        htArraAwdData = (Hashtable)webTxnBean.getResults(request,ARGUMENT_VALUE_LIST,hmlookupArgument);
        vecPrjStatus = (Vector)htArraAwdData.get(ARGUMENT_VALUE_LIST);
        if(vecPrjStatus == null){
            vecPrjStatus = new Vector();
        }
        session.setAttribute(ACTIVITY_TYPE,vecPrjStatus);
        // Added with COEUSDEV-624/COEUSDEV-603: Arra award type Issues
        Vector vecAwdTypes = new Vector();
        vecAwdTypes.add(new ComboBoxBean(CONTRACT_AWARD_TYPE,CONTRACT_AWARD_TYPE));
        vecAwdTypes.add(new ComboBoxBean(GRANT_AWARD_TYPE,GRANT_AWARD_TYPE));
        session.setAttribute("awardTypes",vecAwdTypes);
        // COEUSDEV-624/COEUSDEV-603: End
    }

    private Vector getJobsCreated(String arraReportNumber, String mitAwardNumber, HttpServletRequest request) throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htJobs = new Hashtable();
        htJobs.put("arraReportNumber", new Integer(arraReportNumber) );
        htJobs.put("mitAwardNumber", mitAwardNumber );
        htJobs = (Hashtable)webTxnBean.getResults(request,"getArraAwardJobs",htJobs);
        return (Vector)htJobs.get("getArraAwardJobs");
    }
    
    
}
