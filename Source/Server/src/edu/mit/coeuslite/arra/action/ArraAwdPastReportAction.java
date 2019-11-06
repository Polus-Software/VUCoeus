/*
 * ArraAwdPastReportAction.java
 *
 * Created on November 24, 2009, 6:41 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
/* PMD check performed, and commented unused imports and variables on 27-NOV-2010
 * by Keerthy Jayaraj
 */
package edu.mit.coeuslite.arra.action;


import edu.mit.coeus.arra.ArraAuthorization;
import edu.mit.coeus.arra.bean.ArraReportTxnBean;
import edu.mit.coeuslite.arra.bean.ArraAwardHeaderBean;
import edu.mit.coeuslite.utils.CoeusDynaFormList;
import edu.mit.coeuslite.utils.CoeusliteMenuItems;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

/**
 *
 * @author suganyadevipv
 */
public class ArraAwdPastReportAction extends ArraBaseAction{
    private static final String GET_PAST_ARRA_REPORTS = "/getPastReports";
    private static final String GET_PAST_ARRA_DETAILS = "/getArraDetailsForVersion";
    private static final String HIGHLY_COMP_ORG_ID = "000001";    
    private static final String DYNA_BEAN_LIST = "subcontractDynaBeansListForVersion";
    private static final String ARRA_REPORT_NUMBER = "arraReportNumber";
    private static final String MIT_AWARD_NUMBER = "mitAwardNumber";
    private static final String VERSION_NUMBER = "versionNumber";
    private static final String SUBCONTRACT_CODE = "subContractCode";
    
    /** Creates a new instance of ArraAwdPastReportAction */
    public ArraAwdPastReportAction() {
    }
    public ActionForward performExecute(ActionMapping actionMapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        String navigator = EMPTY_STRING;
        DynaValidatorForm dynaForm = (DynaValidatorForm)actionForm;
        if(actionMapping.getPath().equals(GET_PAST_ARRA_REPORTS)){
            String mitAwardNo = (String)session.getAttribute(ARRA_REPORT_AWARD_NUMBER);
            navigator = getPastArraAward(mitAwardNo,request);
        }else if(actionMapping.getPath().equals(GET_PAST_ARRA_DETAILS)){
            String reportNo =  request.getParameter("arraReportNo");
            String mitAwardNo = request.getParameter("arraReportAwardNo");
            String versionNumber = request.getParameter("arraVersionNumber");
//            ArraAuthorization arraAuthorization = new ArraAuthorization();
//            ArraReportTxnBean arraReprotTxnBean = new  ArraReportTxnBean();
            getArraHeaderForVersion(reportNo , mitAwardNo, versionNumber, request);
            navigator = fetchArraAwardDetails(reportNo,mitAwardNo,versionNumber,request,dynaForm);
        }
        
        setSelectedStatusMenu(CoeusliteMenuItems.ARRA_PAST_REPORT_MENU_CODE,session);
        return actionMapping.findForward(navigator);
    }
    
    /* To get the past arra award details from db*/
    /**This method is used to fetch arra past Reports, whose status is submitted
     *@param reportNumber - The Arra report Number.
     *@param mitAwardNumber - The mit award number.
     *@param versionNumber - The version Number
     */
    private String getPastArraAward(String mitAwardNumber, HttpServletRequest request) throws Exception{
        String navigator = "success";
        WebTxnBean webTxnBean = new WebTxnBean();
        HashMap hmArraData = new HashMap();
        hmArraData.put("mitAwardNumber", mitAwardNumber);
        Hashtable htArraDetails = (Hashtable)webTxnBean.getResults(request, "getPastArraAwardDetails",hmArraData);
        Vector vecArraDetails =  (Vector)htArraDetails.get("getPastArraAwardDetails");
        request.setAttribute("pastArraAwardDetails",vecArraDetails);
        return navigator;
    }
    /* To get the arra award details from db*/
     /**This method is used to get arra Award details for version
     *@param reportNumber - The Arra report Number.
     *@param mitAwardNumber - The mit award number.
     *@param versionNumber - The version Number
     *@return Vector which contains all the Award details for 
     * particular version and award number.
     */
    private Vector getArraAwardDetailsForVersion(String arraReportNumber , String mitAwardNumber, String versionNumber,HttpServletRequest request) throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        HashMap hmArraData = new HashMap();
        hmArraData.put(ARRA_REPORT_NUMBER, new Integer(arraReportNumber) );
        hmArraData.put(MIT_AWARD_NUMBER, mitAwardNumber );
        hmArraData.put(VERSION_NUMBER, new Integer(versionNumber));
        Hashtable htArraDetails = (Hashtable)webTxnBean.getResults(request, "getArraAwardDetailsForVersion",hmArraData);
        Vector vecArraDetails =  (Vector)htArraDetails.get("getArraAwardDetailsForVersion");
        return vecArraDetails;
    }
    /**This method is used to fetch arra Award details for version
     *@param reportNumber - The Arra report Number.
     *@param mitAwardNumber - The mit award number.
     *@param versionNumber - The version Number
     *@return Vector which contains all the Award details for 
     * particular version and award number.
     */
    private String fetchArraAwardDetails(String arraReportNumber , String mitAwardNumber,String versionNumber, HttpServletRequest request,DynaValidatorForm dynaForm) throws Exception{
        String navigator = "success";
//        HttpSession session = request.getSession();
        Vector vecArraDetails = getArraAwardDetailsForVersion(arraReportNumber,mitAwardNumber,versionNumber,request);
        if(vecArraDetails!=null && vecArraDetails.size() >0 ){
            
            HashMap hmData = null;
            DynaValidatorForm arraAwardPastReport = (DynaValidatorForm)vecArraDetails.get(0);
            // Added with COEUSDEV-624/COEUSDEV-603:ARRA Award Type Issues
            String awardType = (String)arraAwardPastReport.get("awardType");
            if(GRANT_AWARD_TYPE.equalsIgnoreCase(awardType)){
                awardType = GRANT_AWARD_TYPE;
            }else{
                awardType = CONTRACT_AWARD_TYPE;
            }
            arraAwardPastReport.set("awardType",awardType);
            // COEUSDEV-624/COEUSDEV-603:End
            //Fetch infra structure contact details
            if(arraAwardPastReport.get("infraContactId") != null){
                hmData = new HashMap();
                hmData.put("rolodexId",arraAwardPastReport.get("infraContactId"));
                Vector vecRolodex = (Vector)getRolodexDetails(request,hmData);
                if(vecRolodex != null && vecRolodex.size() >0 ){
                    DynaValidatorForm dynaAddress = (DynaValidatorForm)vecRolodex.get(0);
                    String address = getCompleteAddress(dynaAddress);
                    arraAwardPastReport.set("infraContactAddress",address);
                }
            }
            //Fetch primary place of performance details
            Object primPlaceOfPerf = arraAwardPastReport.get("primPlaceOfPerfId");
            if(primPlaceOfPerf != null){
                hmData = new HashMap();
                hmData.put("rolodexId",primPlaceOfPerf);
                Vector vecRolodex = (Vector)getRolodexDetails(request,hmData);
                if(vecRolodex != null && vecRolodex.size() >0 ){
                    DynaValidatorForm dynaAddress = (DynaValidatorForm)vecRolodex.get(0);
                    String address = getCompleteAddress(dynaAddress);
                    arraAwardPastReport.set("primPlaceOfPerfAddress",address);
                }
            }
            
            Vector vctHighlyComp = getHighlyCompensatedIndividuals(request,HIGHLY_COMP_ORG_ID);
            request.setAttribute("arraHighlyCompensatedForVersion",vctHighlyComp);
            Vector vctJobsCreated = getJobsCreated(arraReportNumber,mitAwardNumber,versionNumber,request);
            request.setAttribute("arraAwardJobsCreatedForVersion",vctJobsCreated);
            request.setAttribute("arraAwardPastReport",arraAwardPastReport);
            Vector vctVendors = fetchArraVendorsForVersion(arraReportNumber,mitAwardNumber,null,versionNumber,request);                    
            request.setAttribute("arraAwardVendorsForVersion",vctVendors);
            Vector vecSubcontracts = getArraAwardSubcontractsForVersion(arraReportNumber,mitAwardNumber,versionNumber,request);
            request.setAttribute("arraAwardSubcontractsForVersion",vecSubcontracts);
            CoeusDynaFormList list = new CoeusDynaFormList();
//            Vector subcontractDetailsVector = new Vector();
            HashMap subcontractDetailsMap = new HashMap();
            if(vecSubcontracts != null && vecSubcontracts.size()>0){
                for(int i=0;i<vecSubcontracts.size();i++){
                    DynaValidatorForm subcontractForm = (DynaValidatorForm) vecSubcontracts.get(i);
                    String subcontractCode = (String)subcontractForm.get("subcontractCode");
                    list = getArraAwardSubcontractDetailsForVersion(arraReportNumber ,mitAwardNumber,versionNumber,subcontractCode,request);
                    subcontractDetailsMap.put(subcontractCode,list);
                }
            }
            request.setAttribute(DYNA_BEAN_LIST,subcontractDetailsMap);

        }
        
        return navigator;
    }
    /*To get the highly compensated individuals */
     /**This method is used to fetch arra HighlyCompensatedIndividuals details for version
     *@param reportNumber - The Arra report Number.
     *@param mitAwardNumber - The mit award number.
     *@param versionNumber - The version Number
     *@return Vector which contains all the HighlyCompensatedIndividuals details for 
     * particular version and award number.
     */
    protected Vector getHighlyCompensatedIndividuals( HttpServletRequest request,String organisationId) throws Exception{
        Vector vctDetails = null;
        if(organisationId!=null && !EMPTY_STRING.equals(organisationId.trim())){
            WebTxnBean webTxnBean = new WebTxnBean();
            Hashtable htOrgData = new Hashtable();
            htOrgData.put("organisationId", organisationId );
            htOrgData = (Hashtable)webTxnBean.getResults(request,"getArraHighlyCompensated",htOrgData);
            vctDetails =  (Vector)htOrgData.get("getArraHighlyCompensated");
        }
        return vctDetails;
    }
    
    /* To get the arra award vendor details from db*/
    /**This method is used to fetch arra Vendors for version
     *@param reportNumber - The Arra report Number.
     *@param mitAwardNumber - The mit award number.
     *@param versionNumber - The version Number
     *@return Vector which contains all the Vendor details for 
     * particular version and award number.
     */
    protected Vector getVendors(String arraReportNumber , String mitAwardNumber, String subContractCode,String versionNumber, HttpServletRequest request) throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        HashMap hmArraData = new HashMap();
        hmArraData.put(ARRA_REPORT_NUMBER, new Integer(arraReportNumber) );
        hmArraData.put(MIT_AWARD_NUMBER, mitAwardNumber );
        hmArraData.put(VERSION_NUMBER, new Integer(versionNumber));
        hmArraData.put(SUBCONTRACT_CODE, subContractCode );
        Hashtable htArraData = (Hashtable)webTxnBean.getResults(request, "getArraAwardVendorsForVersion",hmArraData);
        return  (Vector)htArraData.get("getArraAwardVendorsForVersion");
        
    }
    /**This method is used to fetch Jobs Created for arra
     *@param reportNumber - The Arra report Number.
     *@param mitAwardNumber - The mit award number.
     *@param versionNumber - The version Number
     *@return Vector which contains jobs created details for 
     * particular version and award number.
     */
    private Vector getJobsCreated(String arraReportNumber, String mitAwardNumber,String versionNumber, HttpServletRequest request) throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htJobs = new Hashtable();
        htJobs.put(ARRA_REPORT_NUMBER, new Integer(arraReportNumber));
        htJobs.put(MIT_AWARD_NUMBER, mitAwardNumber );
        htJobs.put(VERSION_NUMBER, new Integer(versionNumber));
        htJobs = (Hashtable)webTxnBean.getResults(request,"getArraAwardJobsForVersion",htJobs);
        return (Vector)htJobs.get("getArraAwardJobsForVersion");
    }
    /**This method is used to fetch arra Vendor details for version
     *@param reportNumber - The Arra report Number.
     *@param mitAwardNumber - The mit award number.
     *@param versionNumber - The version Number
     *@return Vector which contains all the Vendor details for 
     * particular version and award number.
     */
    private Vector fetchArraVendorsForVersion(String arraReportNumber, String mitAwardNumber, String subContractCode, String versionNumber, HttpServletRequest request) throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        HashMap hmArraData = new HashMap();
        hmArraData.put(ARRA_REPORT_NUMBER, new Integer(arraReportNumber) );
        hmArraData.put(MIT_AWARD_NUMBER, mitAwardNumber );
        hmArraData.put(VERSION_NUMBER, new Integer(versionNumber));
        hmArraData.put(SUBCONTRACT_CODE, subContractCode );
        Hashtable htArraData = (Hashtable)webTxnBean.getResults(request, "getArraAwardVendorsForVersion",hmArraData);
        return  (Vector)htArraData.get("getArraAwardVendorsForVersion");
    }
    /**This method is used to fetch arra subcontract details for version
     *@param reportNumber - The Arra report Number.
     *@param mitAwardNumber - The mit award number.
     *@param versionNumber - The version Number
     *@return Vector which contains all the subcontract details for 
     * particular version and award number.
     */
    private Vector getArraAwardSubcontractsForVersion(String arraReportNumber , String mitAwardNumber,String versionNumber, HttpServletRequest request) throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        HashMap hmArraData = new HashMap();
        hmArraData.put(ARRA_REPORT_NUMBER, new Integer(arraReportNumber) );
        hmArraData.put(MIT_AWARD_NUMBER, mitAwardNumber );
        hmArraData.put(VERSION_NUMBER, new Integer(versionNumber));
        Hashtable htArraDetails = (Hashtable)webTxnBean.getResults(request, "getArraAwardSubcontractsForVersion",hmArraData);
        return (Vector)htArraDetails.get("getArraAwardSubcontractsForVersion");
    }
     /* To get the arra award subcontract details from db*/
     /**This method is used to fetch arra subcontract details for version
     *@param reportNumber - The Arra report Number.
     *@param mitAwardNumber - The mit award number.
     *@param versionNumber - The version Number
     *@return CoeusDynaFormList which contains all the subcontract details for 
     * particular version and award number.
     */
    private CoeusDynaFormList getArraAwardSubcontractDetailsForVersion(String arraReportNumber , String mitAwardNumber,String versionNumber, String subContractCode, HttpServletRequest request) throws Exception{
        
        Vector vctSubcontractDetails = null;
        Vector vctVendors = null;
        Vector vctHighlyComp = null;        
        String subcontractorID = null;
        CoeusDynaFormList coeusDynaFormList = new CoeusDynaFormList();
//        HttpSession session = request.getSession();
        WebTxnBean webTxnBean = new WebTxnBean();
        HashMap hmArraData = new HashMap();
        hmArraData.put(ARRA_REPORT_NUMBER, new Integer(arraReportNumber) );
        hmArraData.put(MIT_AWARD_NUMBER, mitAwardNumber );
        hmArraData.put(VERSION_NUMBER, new Integer(versionNumber));        
        hmArraData.put(SUBCONTRACT_CODE, subContractCode );
        Hashtable htArraDetails = (Hashtable)webTxnBean.getResults(request, "getArraAwardSubcontractDetailsForVersion",hmArraData);
        vctSubcontractDetails =  (Vector)htArraDetails.get("getArraAwardSubcontractDetailsForVersion");
        if(vctSubcontractDetails!=null && vctSubcontractDetails.size() >0 ){
            DynaValidatorForm subContractDetForm = (DynaValidatorForm)vctSubcontractDetails.get(0);
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
        vctVendors = fetchArraVendorsForVersion(arraReportNumber,mitAwardNumber,subContractCode,versionNumber,request);
        vctVendors = (vctVendors==null)?new Vector():vctVendors;
        vctHighlyComp = (vctHighlyComp==null)?new Vector():vctHighlyComp;
        //Set All values
        coeusDynaFormList.setInfoList(vctSubcontractDetails);
        coeusDynaFormList.setList(vctVendors);
        coeusDynaFormList.setBeanList(vctHighlyComp);     
        return coeusDynaFormList;
        
    }
    /*To get Arra award header details for particular version*/
    /**This method is used to fetch arra header details for version by using GET_ARRA_HEADER_FOR_VERSION
     *@param reportNumber - The Arra report Number.
     *@param mitAwardNumber - The mit award number.
     *@param versionNumber - The version Number
     */
    protected void getArraHeaderForVersion(String arraReportNumber , String mitAwardNumber, 
            String versionNumber, HttpServletRequest request)throws Exception{
//        HttpSession session = request.getSession();
        WebTxnBean webTxnBean = new WebTxnBean();
        ArraAwardHeaderBean headerBean = null;
        Hashtable htArraAwdData = new Hashtable();
        htArraAwdData.put(ARRA_REPORT_NUMBER, new Integer(arraReportNumber) );
        htArraAwdData.put(MIT_AWARD_NUMBER, mitAwardNumber );
        htArraAwdData.put(VERSION_NUMBER, new Integer(versionNumber));
        htArraAwdData = (Hashtable)webTxnBean.getResults(request,"getArraAwardHeaderDataForVersion",htArraAwdData);
        Vector vecArraHeader = (Vector)htArraAwdData.get("getArraAwardHeaderDataForVersion");
        if(vecArraHeader!=null && vecArraHeader.size()>0) {
            headerBean = (ArraAwardHeaderBean)vecArraHeader.get(0);
            request.setAttribute("arraAwardHeaderBeanForVersion",headerBean);
        }
    }
    

}
