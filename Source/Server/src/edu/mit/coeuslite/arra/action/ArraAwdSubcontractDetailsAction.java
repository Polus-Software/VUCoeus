/*
 * ArraAwdSubcontractDetailsAction.java
 *
 * Created on August 11, 2009, 10:45 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeuslite.arra.action;

import edu.mit.coeus.arra.ArraAuthorization;
import edu.mit.coeus.arra.bean.ArraReportTxnBean;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeuslite.utils.CoeusliteMenuItems;
import edu.mit.coeuslite.utils.DateUtils;
import edu.mit.coeuslite.utils.LockBean;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
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
import edu.mit.coeuslite.utils.CoeusDynaFormList;
import org.apache.struts.action.DynaActionForm;
import org.apache.commons.beanutils.DynaBean;
/**
 *
 * @author keerthyjayaraj
 */
public class ArraAwdSubcontractDetailsAction extends ArraBaseAction{
    
    //actionmapping paths
    private static final String GET_SUBCONTRACT_DETAILS = "/getSubcontractDetails";
    private static final String SAVE_SUBCONTRACT_DETAILS = "/saveSubcontractDetails";
    private static final String ADD_VENDOR = "/addSubcontractVendor";
    private static final String REM_VENDOR = "/removeVendorDetails";
    private static final String REM_INDIVIDUAL = "/removeIndivdualDetails";
    private static final String ADD_INDIVIDUAL = "/addHighlyCompIndividual";
    //Session params
    private static final String SUBCONTRACT_CODE = "arraAwardSubcontractCode";
    private static final String SUBCONTRACTOR_ID = "arraAwardSubcontractorId";
    private static final String DYNA_BEAN_LIST = "subcontractDynaBeansList";
    private static final String REMOVED_VENDORS = "removedVendors";
    private static final String REMOVED_INDIVIDUALS = "removedIndividuals";
    private static final String VENDOR_UPDATED = "vendorUpdated";
    private static final String INDIVIDUAL_UPDATED = "indUpdated";
    //Transaction.xml params
    private static final String FETCH_SUBCONTRACT_DETAILS = "getArraAwardSubcontractDetails";
    private static final String UPDATE_SUBCONTRACT_DETAILS = "updArraAwardSubcontracts";
    private static final String UPDATE_SUBCONTRACT_VENDORS = "updateSubcontractVendor";
    private static final String UPDATE_SUBCONTRACT_INDIVIDUALS = "updateSubcontractIndividual";
    
    private static final String AC_TYPE = "acType";
    private static final String AW_UPDATE_TIMESTAMP = "awUpdateTimestamp";
    private static final String UPDATE_TIMESTAMP = "updateTimestamp";
     
    private static final String ARRA_REPORT_SUBCONTRACTS_COLUMN_PROPERTIES = "subcontractsColumnProperties";
    private static final String ARRA_REPORT_VENDORS_COLUMN_PROPERTIES = "vendorColumnProperties";
    private static final String ARRA_HIGHLY_COMPENSATED_COLUMN_PROPERTIES = "highlyCompensColumnProperties";
    private static final String ARRA_REPORT_SUBCONTRACTS_TABLE_NAME = "OSP$ARRA_REPORT_SUBCONTRACTS";
    private static final String ARRA_REPORT_VENDORS_TABLE_NAME = "OSP$ARRA_REPORT_VENDORS";
    private static final String ARRA_HIGHLY_COMPENSATED_TABLE_NAME = "OSP$ARRA_HIGHLY_COMPENSATED";
    
    private static final String EDIT_COLUMN_PROPERTIES_RIGHT = "canEditAllRight";
    private static final String DATE_SEPARATERS = ":/.,|-";
    private static final String SIMPLE_DATE_FORMAT = "MM/dd/yyyy";
    
    /**
     * Creates a new instance of ArraAwdSubcontractDetailsAction
     */
    public ArraAwdSubcontractDetailsAction() {
    }
    
    public ActionForward performExecute(ActionMapping actionMapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        CoeusDynaFormList coeusDynaFormList = (CoeusDynaFormList) actionForm;
        ActionForward actionForward = getSubContractDetails(actionMapping, request, coeusDynaFormList);
        setSelectedStatusMenu(CoeusliteMenuItems.ARRA_SUBCONTRACT_MENU_CODE,session);
        return actionForward;
        
    }
    
    
    /** This method will identify which request is comes from which path and
     *  navigates to the respective ActionForward
     *  @returns ActionForward object
     */
    private ActionForward getSubContractDetails(ActionMapping actionMapping, HttpServletRequest request ,CoeusDynaFormList coeusDynaFormList)throws Exception{
        
        String navigator = EMPTY_STRING;
        HttpSession session = request.getSession();
        ArraAuthorization arraAuthorization = new ArraAuthorization();
        ArraReportTxnBean arraReprotTxnBean = new  ArraReportTxnBean();
        if(actionMapping.getPath().equals(GET_SUBCONTRACT_DETAILS)){
            String reportNo =  (String)session.getAttribute(ARRA_REPORT_NUMBER);
            String mitAwardNo = (String)session.getAttribute(ARRA_REPORT_AWARD_NUMBER);
            String subContractCode = request.getParameter("subcontractCode");
            //Added for edit all functionality
            UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
              // Arra Phase 2 changes
//            boolean canEditAll = arraAuthorization.checkUserHasMaintainArraRight(userInfoBean.getUserId(),mitAwardNo); 
            Integer versionNumber = (Integer)session.getAttribute(ARRA_REPORT_VERSION);
            boolean canEditAll = arraAuthorization.canModifyAllArraReportFields(userInfoBean.getUserId(),mitAwardNo, versionNumber.intValue(), new Integer(reportNo).intValue());  
            session.setAttribute(EDIT_COLUMN_PROPERTIES_RIGHT,new Boolean(canEditAll));
            HashMap subcontColumnProperties = arraReprotTxnBean.checkArraColumnProperties(ARRA_REPORT_SUBCONTRACTS_TABLE_NAME);
            HashMap vendorColumnProperties = arraReprotTxnBean.checkArraColumnProperties( ARRA_REPORT_VENDORS_TABLE_NAME);
            HashMap highlyCompensColumnProperties = arraReprotTxnBean.checkArraColumnProperties(ARRA_HIGHLY_COMPENSATED_TABLE_NAME);            
            session.setAttribute(ARRA_REPORT_SUBCONTRACTS_COLUMN_PROPERTIES,subcontColumnProperties);
            session.setAttribute(ARRA_REPORT_VENDORS_COLUMN_PROPERTIES,vendorColumnProperties);
            session.setAttribute(ARRA_HIGHLY_COMPENSATED_COLUMN_PROPERTIES,highlyCompensColumnProperties);           
            navigator = getArraAwardSubcontractDetails(reportNo,mitAwardNo,subContractCode,request,coeusDynaFormList);
        }else if(actionMapping.getPath().equals(SAVE_SUBCONTRACT_DETAILS)){
             navigator = performSaveAllDetails(coeusDynaFormList, request);
        }else if(actionMapping.getPath().equals(ADD_VENDOR)){
            navigator = addSubcontractVendor(coeusDynaFormList, request);
        }else if(actionMapping.getPath().equals(ADD_INDIVIDUAL)){
            navigator = addSubcontractIndividual(coeusDynaFormList, request);
        }else if(actionMapping.getPath().equals(REM_INDIVIDUAL)){
            navigator = removeIndividual(coeusDynaFormList, request);
        }else if(actionMapping.getPath().equals(REM_VENDOR)){
            navigator = removeVendor(coeusDynaFormList, request);
        }
        
        ActionForward actionForward = actionMapping.findForward(navigator);
        return actionForward;
    }
    
    
    
    /* To get the arra award subcontract details from db*/
    private String getArraAwardSubcontractDetails(String arraReportNumber , String mitAwardNumber, String subContractCode, HttpServletRequest request,CoeusDynaFormList coeusDynaFormList) throws Exception{
        
        Vector vctSubcontractDetails = null;
        Vector vctVendors = null;
        Vector vctHighlyComp = null;
        String navigator = "success";
        String subcontractorID = null;
        HttpSession session = request.getSession();
        WebTxnBean webTxnBean = new WebTxnBean();
        HashMap hmArraData = new HashMap();
        hmArraData.put("arraReportNumber", new Integer(arraReportNumber) );
        hmArraData.put("mitAwardNumber", mitAwardNumber );
        hmArraData.put("subContractCode", subContractCode );
        Hashtable htArraDetails = (Hashtable)webTxnBean.getResults(request, FETCH_SUBCONTRACT_DETAILS,hmArraData);
        vctSubcontractDetails =  (Vector)htArraDetails.get(FETCH_SUBCONTRACT_DETAILS);
        if(vctSubcontractDetails!=null && vctSubcontractDetails.size() >0 ){
            DynaActionForm subContractDetForm = (DynaActionForm)vctSubcontractDetails.get(0);
            //Fetch primary place of performance details
            Object primPlaceOfPerf = subContractDetForm.get("primPlaceOfPerfId");
            if(primPlaceOfPerf != null){
                HashMap hmData = new HashMap();
                hmData.put("rolodexId",primPlaceOfPerf);
                Vector vecRolodex = (Vector)getRolodexDetails(request,hmData);
                if(vecRolodex != null && vecRolodex.size() >0 ){
                    DynaValidatorForm dynaAddress = (DynaValidatorForm)vecRolodex.get(0);
                    String address = getCompleteAddress(dynaAddress);
                    subContractDetForm.set("primPlaceOfPerfAddress",address);
                }
            }
            subcontractorID = (String)subContractDetForm.get("subcontractorID");
            //Fetch highly compensated individuals
            vctHighlyComp = getHighlyCompensatedIndividuals(request,subcontractorID);
        }
        //Fetch vendors
        vctVendors = getVendors(arraReportNumber,mitAwardNumber,subContractCode,request);
        vctVendors = (vctVendors==null)?new Vector():vctVendors;
        vctHighlyComp = (vctHighlyComp==null)?new Vector():vctHighlyComp;
        //Set All values
        coeusDynaFormList.setInfoList(vctSubcontractDetails);
        coeusDynaFormList.setList(vctVendors);
        coeusDynaFormList.setBeanList(vctHighlyComp);
        session.removeAttribute(REMOVED_VENDORS);
        session.removeAttribute(REMOVED_INDIVIDUALS);
        session.setAttribute(DYNA_BEAN_LIST,coeusDynaFormList);
        session.setAttribute(SUBCONTRACT_CODE,subContractCode);
        session.setAttribute(SUBCONTRACTOR_ID,subcontractorID);
        return navigator;
    }
    
    private String addSubcontractVendor(CoeusDynaFormList coeusDynaFormList, HttpServletRequest request) throws Exception{
        String navigator = "success";
        List arLstVendorData = coeusDynaFormList.getList();
        DynaActionForm dynaFormData = coeusDynaFormList.getDynaForm(request,"arraAwardVendorsForm");
        DynaBean dynaNewBean = ((DynaBean)dynaFormData).getDynaClass().newInstance();
        HttpSession session = request.getSession();
        String reportNo =  (String)session.getAttribute(ARRA_REPORT_NUMBER);
        String mitAwardNo = (String)session.getAttribute(ARRA_REPORT_AWARD_NUMBER);
        dynaNewBean.set("arraReportNumber",new Integer(reportNo));
        dynaNewBean.set("mitAwardNumber",mitAwardNo);
        dynaNewBean.set("versionNumber",session.getAttribute(ARRA_REPORT_VERSION));
        dynaNewBean.set("subContractCode",(String)session.getAttribute(SUBCONTRACT_CODE));
        dynaNewBean.set(AC_TYPE, TypeConstants.INSERT_RECORD);
        Timestamp prepareTimestamp = prepareTimeStamp();
        dynaNewBean.set(UPDATE_TIMESTAMP,prepareTimestamp.toString());
        if(arLstVendorData == null){
            arLstVendorData  = new Vector();
        }
        arLstVendorData.add(dynaNewBean);
        coeusDynaFormList.setList(arLstVendorData);
        session.setAttribute(DYNA_BEAN_LIST,coeusDynaFormList);
        request.setAttribute(VENDOR_UPDATED,"Y");
        return navigator;
    }
    
    private String addSubcontractIndividual(CoeusDynaFormList coeusDynaFormList, HttpServletRequest request) throws Exception{
        String navigator = "success";
        List arLstIndData = coeusDynaFormList.getBeanList();
        DynaActionForm dynaFormData = coeusDynaFormList.getDynaForm(request,"arraAwardHighlyCompBean");
        DynaBean dynaNewBean = ((DynaBean)dynaFormData).getDynaClass().newInstance();
        HttpSession session = request.getSession();
        dynaNewBean.set("organizationId",(String)session.getAttribute(SUBCONTRACTOR_ID));
        dynaNewBean.set(AC_TYPE, TypeConstants.INSERT_RECORD);
        Timestamp prepareTimestamp = prepareTimeStamp();
        dynaNewBean.set(UPDATE_TIMESTAMP,prepareTimestamp.toString());
        if(arLstIndData == null){
            arLstIndData  = new Vector();
        }
        arLstIndData.add(dynaNewBean);
        coeusDynaFormList.setBeanList(arLstIndData);
        session.setAttribute(DYNA_BEAN_LIST,coeusDynaFormList);
        request.setAttribute(INDIVIDUAL_UPDATED,"Y");
        return navigator;
    }
    
    private String removeVendor(CoeusDynaFormList coeusDynaFormList, HttpServletRequest request) throws Exception{
        String navigator = "success";
        HttpSession session = request.getSession();
        Vector vecRemovedVendors = (Vector) session.getAttribute(REMOVED_VENDORS);
        if (vecRemovedVendors == null){
            vecRemovedVendors= new Vector();
        }
        List arLstVendorData = coeusDynaFormList.getList();
        String vendorIndex = request.getParameter("vendorIndex");
        if(arLstVendorData !=null && vendorIndex !=null && !vendorIndex.equals(EMPTY_STRING)){
            int vendorRow = Integer.parseInt(vendorIndex);
            if( arLstVendorData.size() >vendorRow ){
                DynaActionForm dynaActionForm = (DynaActionForm) arLstVendorData.get(vendorRow);
                String acType = (String)dynaActionForm.get(AC_TYPE);
                if(!TypeConstants.INSERT_RECORD.equals(acType)){
                    dynaActionForm.set(AC_TYPE,TypeConstants.DELETE_RECORD);
                    vecRemovedVendors.addElement(dynaActionForm);
                }
                arLstVendorData.remove(vendorRow);
            }
        }
        coeusDynaFormList.setList(arLstVendorData);
        session.setAttribute(REMOVED_VENDORS, vecRemovedVendors);
        session.setAttribute(DYNA_BEAN_LIST,coeusDynaFormList);
        request.setAttribute(VENDOR_UPDATED,"Y");
        return navigator;
    }
    
    
    private String removeIndividual(CoeusDynaFormList coeusDynaFormList, HttpServletRequest request) throws Exception{
        String navigator = "success";
        HttpSession session = request.getSession();
                    
        Vector vecRemovedInd = (Vector) session.getAttribute(REMOVED_INDIVIDUALS);
        if (vecRemovedInd == null){
            vecRemovedInd= new Vector();
        }
         List arLstIndData = coeusDynaFormList.getBeanList();
        String indIndex = request.getParameter("indIndex");
        if(arLstIndData !=null && indIndex !=null && !indIndex.equals(EMPTY_STRING)){
            int indRow = Integer.parseInt(indIndex);
            if( arLstIndData.size() >indRow ){
                DynaActionForm dynaActionForm = (DynaActionForm) arLstIndData.get(indRow);
                String acType = (String)dynaActionForm.get(AC_TYPE);
                if(!TypeConstants.INSERT_RECORD.equals(acType)){
                    dynaActionForm.set(AC_TYPE,TypeConstants.DELETE_RECORD);
                    vecRemovedInd.addElement(dynaActionForm);
                }
                arLstIndData.remove(indRow);
            }
        }
        coeusDynaFormList.setBeanList(arLstIndData);
        session.setAttribute(REMOVED_INDIVIDUALS, vecRemovedInd);
        
        session.setAttribute(DYNA_BEAN_LIST,coeusDynaFormList);
        request.setAttribute(INDIVIDUAL_UPDATED,"Y");
        return navigator;
    }
    
    private String performSaveAllDetails(CoeusDynaFormList coeusDynaFormList, HttpServletRequest request) throws Exception {
        String navigator = "success";
        HttpSession session = request.getSession();
        String mitAwardNo = (String)session.getAttribute(ARRA_REPORT_AWARD_NUMBER);
        String reportNo = (String)session.getAttribute(ARRA_REPORT_NUMBER);
        UserInfoBean userInfoBean = (UserInfoBean) session.getAttribute("user"+session.getId());
        LockBean lockBean = getArraLockingBean(userInfoBean, mitAwardNo, new Integer(reportNo).intValue(),request);
        boolean moduleCanBeLocked = isLockExists(lockBean, lockBean.getModuleKey());
        LockBean lockData = getLockedData(ARRA_LOCK_STRING+lockBean.getModuleNumber(), request);
        if(moduleCanBeLocked || !lockBean.getSessionId().equals(lockData.getSessionId())) {
            String errMsg = "release_lock_for"; 
            String lockedArraAwardNumber = lockBean.getModuleNumber().substring(0,10);
            ActionMessages messages = new ActionMessages();
            messages.add("errMsg", new ActionMessage(errMsg,lockBean.getModuleKey(),lockedArraAwardNumber));
            saveMessages(request, messages);
        }else{
            saveSubcontractDetails(coeusDynaFormList,request);
            deleteVendors(request);
            saveVendors(coeusDynaFormList,request);
            deleteIndividuals(coeusDynaFormList,request);
            saveIndividuals(coeusDynaFormList,request);
            
            String subContractCode = (String)session.getAttribute(SUBCONTRACT_CODE);
            navigator = getArraAwardSubcontractDetails(reportNo,mitAwardNo,subContractCode,request,coeusDynaFormList);
        }
        return navigator;
    }
    
    private void saveSubcontractDetails(CoeusDynaFormList coeusDynaFormList, HttpServletRequest request) throws Exception{
        List lstSubDetails = coeusDynaFormList.getInfoList();
        String strValue;
        if(lstSubDetails!=null && !lstSubDetails.isEmpty()){
            WebTxnBean webTxnBean = new WebTxnBean();
            DynaActionForm subContractDetForm = (DynaActionForm)lstSubDetails.get(0);
            //validate jobs
            double dblValue;
            strValue  = (String)subContractDetForm.get("noOfJobs");
            if(strValue!=null){
                try{
                    // Remove ',' from the number of jobs
                    dblValue = formatStringToDouble(strValue);
                }catch(NumberFormatException ne){
                    dblValue = 0;
                }
                subContractDetForm.set("noOfJobs", String.valueOf(dblValue));
            }
            //validate Subaward Amount
            strValue  = (String)subContractDetForm.get("strSubAwardAmount");
            if(strValue!=null){
                try{
                    dblValue = formatStringToDouble(strValue);
                }catch(NumberFormatException ne){
                    dblValue = 0;
                }
                subContractDetForm.set("subAwardAmount", new Double(dblValue));
            }
            
            //validate subAwardAmtDispursed
            strValue  = (String)subContractDetForm.get("strSubAwardAmtDispursed");
            if(strValue!=null){
                try{
                    dblValue = formatStringToDouble(strValue);
                }catch(NumberFormatException ne){
                    dblValue = 0;
                }
                subContractDetForm.set("subAwardAmtDispursed", new Double(dblValue));
            }
            DateUtils dateUtils = new DateUtils();
            String tempSubAwardDate = ((String)subContractDetForm.get("subAwardDate")).trim();
            
            if(tempSubAwardDate != null && !tempSubAwardDate.equals(EMPTY_STRING)){
                tempSubAwardDate = dateUtils.formatDate(tempSubAwardDate,DATE_SEPARATERS,SIMPLE_DATE_FORMAT);
                subContractDetForm.set("subAwardDate",tempSubAwardDate.toString());
            }
            subContractDetForm.set(AC_TYPE,TypeConstants.UPDATE_RECORD);
            subContractDetForm.set(AW_UPDATE_TIMESTAMP,subContractDetForm.get(UPDATE_TIMESTAMP));
            Timestamp dbTimestamp = prepareTimeStamp();
            subContractDetForm.set(UPDATE_TIMESTAMP,dbTimestamp.toString());
            webTxnBean.getResults(request, UPDATE_SUBCONTRACT_DETAILS, subContractDetForm);
        }
    }
    
    private void deleteVendors( HttpServletRequest request) throws Exception{
        HttpSession session = request.getSession();
        Vector vecRemovedVendors = (Vector) session.getAttribute(REMOVED_VENDORS);
        if(vecRemovedVendors!=null && !vecRemovedVendors.isEmpty()){
            WebTxnBean webTxnBean = new WebTxnBean();
            DynaActionForm dynaForm;
            for(int i=0;i<vecRemovedVendors.size();i++){
                dynaForm = (DynaActionForm)vecRemovedVendors.get(i);
                if(TypeConstants.DELETE_RECORD.equals(dynaForm.get(AC_TYPE))){
                    webTxnBean.getResults(request, UPDATE_SUBCONTRACT_VENDORS , dynaForm);
                }
                vecRemovedVendors.remove(i--);
            }
        }
    }
    
    private void deleteIndividuals(CoeusDynaFormList coeusDynaFormList, HttpServletRequest request) throws Exception{
        HttpSession session = request.getSession();
        Vector vecRemovedInd = (Vector) session.getAttribute(REMOVED_INDIVIDUALS);
        if(vecRemovedInd!=null && !vecRemovedInd.isEmpty()){
            WebTxnBean webTxnBean = new WebTxnBean();
            DynaActionForm dynaForm;
            for(int i=0;i<vecRemovedInd.size();i++){
                dynaForm = (DynaActionForm)vecRemovedInd.get(i);
                 if(TypeConstants.DELETE_RECORD.equals(dynaForm.get(AC_TYPE))){
                    webTxnBean.getResults(request, UPDATE_SUBCONTRACT_INDIVIDUALS , dynaForm);
                }
                vecRemovedInd.remove(i--);
            }
        }
    }
    
    private void saveVendors(CoeusDynaFormList coeusDynaFormList, HttpServletRequest request) throws Exception{
        List lstVendors = coeusDynaFormList.getList();
        if(lstVendors!=null && !lstVendors.isEmpty()){
            WebTxnBean webTxnBean = new WebTxnBean();
            DynaActionForm dynaForm;
            double dynaAmount;
            String paymentAmt;
            
            for(int i=0;i<lstVendors.size();i++){
                dynaForm = (DynaActionForm)lstVendors.get(i);
                //validate Amount
                paymentAmt  = (String)dynaForm.get("strPaymentAmount");
                if(paymentAmt!=null){
                    try{
                        dynaAmount = formatStringToDouble(paymentAmt);
                    }catch(NumberFormatException ne){
                        dynaAmount = 0;
                    }
                    dynaForm.set("paymentAmount", new Double(dynaAmount));
                }
                //set acType
                if(dynaForm.get(AC_TYPE)==null || dynaForm.get(AC_TYPE).equals(EMPTY_STRING)) {
                    dynaForm.set(AC_TYPE,TypeConstants.UPDATE_RECORD);
                }
                //set timestamps
                dynaForm.set(AW_UPDATE_TIMESTAMP,dynaForm.get(UPDATE_TIMESTAMP));
                Timestamp dbTimestamp = prepareTimeStamp();
                dynaForm.set(UPDATE_TIMESTAMP,dbTimestamp.toString());
                webTxnBean.getResults(request, UPDATE_SUBCONTRACT_VENDORS, dynaForm);
            }
        }
    }
    
    private void saveIndividuals(CoeusDynaFormList coeusDynaFormList, HttpServletRequest request) throws Exception{
        List lstIndividuals = coeusDynaFormList.getBeanList();
        if(lstIndividuals!=null && !lstIndividuals.isEmpty()){
            WebTxnBean webTxnBean = new WebTxnBean();
            DynaActionForm dynaForm;
            double dynaCompensation;
            String compensation;
            
            for(int i=0;i<lstIndividuals.size();i++){
                dynaForm = (DynaActionForm)lstIndividuals.get(i);
                //validate Amount
                compensation  = (String)dynaForm.get("strCompensation");
                if(compensation!=null){
                    try{
                        dynaCompensation = formatStringToDouble(compensation);
                    }catch(NumberFormatException ne){
                        dynaCompensation = 0;
                    }
                    dynaForm.set("compensation", new Double(dynaCompensation));
                }
                if(dynaForm.get(AC_TYPE)==null || dynaForm.get(AC_TYPE).equals(EMPTY_STRING)) {
                    dynaForm.set(AC_TYPE,TypeConstants.UPDATE_RECORD);
                }
                dynaForm.set(AW_UPDATE_TIMESTAMP,dynaForm.get(UPDATE_TIMESTAMP));
                Timestamp dbTimestamp = prepareTimeStamp();
                dynaForm.set(UPDATE_TIMESTAMP,dbTimestamp.toString());
                webTxnBean.getResults(request, UPDATE_SUBCONTRACT_INDIVIDUALS , dynaForm);
            }
        }
    }
    
    
}
