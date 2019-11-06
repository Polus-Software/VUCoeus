/*
 * ArraBaseAction.java
 *
 * Created on August 11, 2009, 10:45 AM
 *
 */

package edu.mit.coeuslite.arra.action;

import edu.mit.coeus.arra.ArraAuthorization;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.CoeusProperties;
import edu.mit.coeus.utils.CoeusPropertyKeys;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.xml.bean.arra.ContractReport;
import edu.mit.coeus.utils.xml.bean.arra.GrantLoanReport;
import edu.mit.coeus.xml.generator.ArraStream;
import edu.mit.coeus.xml.generator.CoeusXMLGenrator;
import edu.mit.coeuslite.arra.bean.ArraAwardHeaderBean;
import edu.mit.coeuslite.utils.CoeusBaseAction;
import edu.mit.coeuslite.utils.CoeusLiteConstants;
import edu.mit.coeuslite.utils.CoeusliteMenuItems;
import edu.mit.coeuslite.utils.LockBean;
import edu.mit.coeuslite.utils.bean.MenuBean;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import edu.wmc.coeuslite.budget.bean.ReadXMLData;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventLocator;
import javax.xml.bind.Validator;
import javax.xml.bind.helpers.DefaultValidationEventHandler;
import org.apache.struts.validator.DynaValidatorForm;
import org.w3c.dom.Document;

/**
 *
 * @author keerthyjayaraj
 */
public abstract class ArraBaseAction extends CoeusBaseAction{
    
    private static final String XML_MENU_PATH = "/edu/mit/coeuslite/arra/xml/ArraMenu.xml";
    private static final String XML_SUB_MENU_PATH="/edu/mit/coeuslite/arra/xml/ArraSubMenu.xml";
    private static final String ARRA_SUB_HEADER = "arraSubHeader";
    private static final String ARRA_MENU_ITEMS = "arraMenuItems";
    //Essential Session Params
    protected static final String ARRA_REPORT_NUMBER = "arraReportNo";
    protected static final String ARRA_REPORT_AWARD_NUMBER = "arraReportAwardNo";
    protected static final String ARRA_REPORT_VERSION = "arraReportVersion";

    // JM 10-18-2012 updated locking string to read metrics
    //protected static final String ARRA_LOCK_STRING = "osp$Arra_";
    protected static final String ARRA_LOCK_STRING = "osp$Metrics_";
    // JM END
    
    // Added with COEUSDEV-624/COEUSDEV-603:ARRA Award Type Issues
    public static final String CONTRACT_AWARD_TYPE = "Federally Awarded Contract";
    public static final String GRANT_AWARD_TYPE = "Grant";
    // COEUSDEV-624/COEUSDEV-603:End
    /** Creates a new instance of ArraListAction */
    public ArraBaseAction() {
    }
   
    protected void getArraMenus(HttpServletRequest request,String mitAwardNo) throws Exception{
        HttpSession session = request.getSession();
        Vector vctArraMenuItems  = null;
        ReadXMLData readXMLData = new ReadXMLData();
        ArraAwardHeaderBean arraAwardHeaderBean = (ArraAwardHeaderBean) session.getAttribute("arraAwardHeaderBean");
        
        // Arra Phase 2 changes
        int reportNumber = arraAwardHeaderBean.getArraReportNumber();
        int versionNumber = arraAwardHeaderBean.getVersionNumber();
        
        ArraAuthorization auth = new ArraAuthorization();
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        String mode = (String)session.getAttribute(CoeusLiteConstants.MODE_DETAILS+session.getId());
//        vctArraMenuItems = (Vector) session.getAttribute(ARRA_MENU_ITEMS);
//        if (vctArraMenuItems == null || vctArraMenuItems.size()==0) {
            vctArraMenuItems = readXMLData.readXMLDataForMenu(XML_MENU_PATH);
            if(vctArraMenuItems!=null && !vctArraMenuItems.isEmpty()){
                MenuBean menuBean;
                for(int i=0;i<vctArraMenuItems.size();i++){
                    menuBean = (MenuBean)vctArraMenuItems.get(i);
                    if(CoeusliteMenuItems.ARRA_SUBMIT_MENU_CODE.equals(menuBean.getMenuId())){                       
                        if(CoeusLiteConstants.DISPLAY_MODE.equalsIgnoreCase(mode) || "S".equalsIgnoreCase(arraAwardHeaderBean.getComplete())){
                            menuBean.setVisible(false);
                            // Arra Phase 2 changes
//                        }else if(auth.canMarkReportComplete(userInfoBean.getUserId(),userInfoBean.getPersonId(),mitAwardNo)){
                        }else if(auth.canMarkReportComplete(userInfoBean.getUserId(),mitAwardNo, versionNumber, reportNumber)){
                        }else{
                            menuBean.setVisible(false);
                        }                       
                    }
                     //The Mark Incomplete menu displays, only when arra report is completes and if the
                     // user has maintain arra right
                    else if(CoeusliteMenuItems.ARRA_MARK_INCOMPLETE_MENU_CODE.equals(menuBean.getMenuId())){
                        if(CoeusLiteConstants.MODIFY_MODE.equalsIgnoreCase(mode)){
                            // Arra Phase 2 chaanges
//                            if(auth.canMarkReportInComplete(userInfoBean.getUserId(),mitAwardNo) && "S".equalsIgnoreCase(arraAwardHeaderBean.getComplete())){
                            if(auth.canMarkReportInComplete(userInfoBean.getUserId(),mitAwardNo, versionNumber, reportNumber) && 
                                    ("S".equalsIgnoreCase(arraAwardHeaderBean.getComplete()) || "Y".equalsIgnoreCase(arraAwardHeaderBean.getComplete()))){
                                menuBean.setVisible(true);
                            }else{
                                menuBean.setVisible(false);
                            }
                        }else if(CoeusLiteConstants.DISPLAY_MODE.equalsIgnoreCase(mode)){
                            // Arra Phase 2 chaanges
//                            if("S".equalsIgnoreCase(arraAwardHeaderBean.getComplete()) && auth.canMarkReportInComplete(userInfoBean.getUserId(),mitAwardNo)){
                            if(auth.canMarkReportInComplete(userInfoBean.getUserId(),mitAwardNo, versionNumber, reportNumber)  &&
                                    ("S".equalsIgnoreCase(arraAwardHeaderBean.getComplete()) || "Y".equalsIgnoreCase(arraAwardHeaderBean.getComplete()))){
                                menuBean.setVisible(true);
                            }else{
                                menuBean.setVisible(false);
                            }
                        }else{
                            menuBean.setVisible(false);
                        }
                    }
                    //Mark the Arra Status as Submitted
                    else if(CoeusliteMenuItems.ARRA_SUBMIT_COMPLETED_MENU_CODE.equals(menuBean.getMenuId())){
                        if(auth.canMarkReportSubmit(userInfoBean.getUserId(),mitAwardNo, versionNumber, reportNumber) && 
                                CoeusLiteConstants.DISPLAY_MODE.equalsIgnoreCase(mode) && auth.canMarkReportComplete(userInfoBean.getUserId(), mitAwardNo, versionNumber, reportNumber) && "Y".equalsIgnoreCase(arraAwardHeaderBean.getComplete())){
                            menuBean.setVisible(true);
                        }else{
                            menuBean.setVisible(false);
                        }
                    }
                }
            }
            session.setAttribute(ARRA_MENU_ITEMS, vctArraMenuItems);
//        }
        Vector vecArraSubMenuHeader = null;
        vecArraSubMenuHeader = (Vector)session.getAttribute(ARRA_SUB_HEADER);
        if(vecArraSubMenuHeader == null || vecArraSubMenuHeader.size()==0){
            vecArraSubMenuHeader = readXMLData.readXMLDataForSubHeader(XML_SUB_MENU_PATH);
            session.setAttribute(ARRA_SUB_HEADER,vecArraSubMenuHeader);
        }
    }
 
    
    protected void setSelectedStatusMenu(String menuCode, HttpSession session){
        Vector menuItemsVector  = null;
        menuItemsVector=(Vector)session.getAttribute(ARRA_MENU_ITEMS);
        Vector modifiedVector = new Vector();
        for (int index=0; index<menuItemsVector.size();index++) {
            MenuBean menuBean = (MenuBean)menuItemsVector.get(index);
            String menuId = menuBean.getMenuId();
            if (menuId.equals(menuCode)) {
                menuBean.setSelected(true);
            } else {
                menuBean.setSelected(false);
            }
            modifiedVector.add(menuBean);
        }
        session.setAttribute(ARRA_MENU_ITEMS, modifiedVector);
    }
    
    protected void getArraHeader(String arraReportNumber , String mitAwardNumber, HttpServletRequest request)throws Exception{
        
        HttpSession session = request.getSession();
        try {
            if(mitAwardNumber!= null && !mitAwardNumber.equals(EMPTY_STRING)){
                WebTxnBean webTxnBean = new WebTxnBean();
                ArraAwardHeaderBean headerBean = null;
                Hashtable htArraAwdData = new Hashtable();
                htArraAwdData.put("arraReportNumber", new Integer(arraReportNumber) );
                htArraAwdData.put("mitAwardNumber", mitAwardNumber );
                htArraAwdData = (Hashtable)webTxnBean.getResults(request,"getArraAwardHeaderData",htArraAwdData);
                Vector vecArraHeader = (Vector)htArraAwdData.get("getArraAwardHeaderData");
                if(vecArraHeader!=null && vecArraHeader.size()>0) {
                    headerBean = (ArraAwardHeaderBean)vecArraHeader.get(0);
                    // boolean arraReportCompleted = headerBean.isComplete();
                    String arraReportCompleted = headerBean.getComplete();
                    if("Y".equalsIgnoreCase(arraReportCompleted)){
                        session.setAttribute(CoeusLiteConstants.MODE_DETAILS+session.getId(),CoeusLiteConstants.DISPLAY_MODE);
                    }
                    session.setAttribute("arraAwardHeaderBean",headerBean);
                    session.setAttribute(ARRA_REPORT_NUMBER,arraReportNumber);
                    session.setAttribute(ARRA_REPORT_AWARD_NUMBER,mitAwardNumber);
                    session.setAttribute(ARRA_REPORT_VERSION,new Integer(headerBean.getVersionNumber()));
                }
            }else{
                session.removeAttribute("arraAwardHeaderBean");
                session.removeAttribute(ARRA_REPORT_NUMBER);
                session.removeAttribute(ARRA_REPORT_AWARD_NUMBER);
                session.removeAttribute(ARRA_REPORT_VERSION);
            }
        } catch(Exception dbEx){
              UtilFactory.log("FROM COEUS LITE : "+dbEx.getMessage(),dbEx,"ArraBaseAction","getArraHeader");

        }

    }
    
    /*To get the highly compensated individuals */
    protected Vector getHighlyCompensatedIndividuals( HttpServletRequest request,String organisationId) throws Exception{
        Vector vctDetails = null;
        try {
            if(organisationId!=null && !EMPTY_STRING.equals(organisationId.trim())){
                WebTxnBean webTxnBean = new WebTxnBean();
                Hashtable htOrgData = new Hashtable();
                htOrgData.put("organisationId", organisationId );
                htOrgData = (Hashtable)webTxnBean.getResults(request,"getArraHighlyCompensated",htOrgData);
                vctDetails =  (Vector)htOrgData.get("getArraHighlyCompensated");
            }
        }catch(Exception dbEx){
              UtilFactory.log("FROM COEUS LITE : "+dbEx.getMessage(),dbEx,"ArraBaseAction","getHighlyCompensatedIndividuals");

        }
        return vctDetails;
    }
    
     /* To get the arra award vendor details from db*/
    protected Vector getVendors(String arraReportNumber , String mitAwardNumber, String subContractCode, HttpServletRequest request) throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        HashMap hmArraData = new HashMap();
        Hashtable htArraData = new Hashtable();
        try {
            hmArraData.put("arraReportNumber", new Integer(arraReportNumber) );
            hmArraData.put("mitAwardNumber", mitAwardNumber );
            hmArraData.put("subContractCode", subContractCode );
            htArraData = (Hashtable)webTxnBean.getResults(request, "getArraAwardVendors",hmArraData);
        } catch(Exception dbEx){
              UtilFactory.log("FROM COEUS LITE : "+dbEx.getMessage(),dbEx,"ArraBaseAction","getVendors");

        }
        return  (Vector)htArraData.get("getArraAwardVendors");
       
    }
    
    /* To get the rolodex details from db*/
    protected Vector getRolodexDetails(HttpServletRequest request,HashMap hmData) throws Exception {
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htData = new Hashtable();
        try {
         htData = (Hashtable)webTxnBean.getResults(request, "getRolodex_details", hmData);
        }catch(Exception dbEx){
              UtilFactory.log("FROM COEUS LITE : "+dbEx.getMessage(),dbEx,"ArraBaseAction","getRolodexDetails");

        }

        return (Vector)htData.get("getRolodex_details");
    }
    
    
    
    
    /**
     * Get the concatenated address from the DynaValidatorForm
     * @param organizationForm
     * @return String
     */
    protected String getCompleteAddress(DynaValidatorForm organizationForm){
        String tempContact = EMPTY_STRING;
        String contact =  EMPTY_STRING;
        
//        tempContact = (String)organizationForm.get("lastName");
//        if(tempContact != null && !tempContact.equals(EMPTY_STRING)){ contact = contact+tempContact;}
//        tempContact = (String)organizationForm.get("suffix");
//        if(tempContact != null && !tempContact.equals(EMPTY_STRING)){ contact = contact+" "+tempContact;}
//        tempContact = (String)organizationForm.get("prefix");
//        if(tempContact != null && !tempContact.equals(EMPTY_STRING)){ contact = contact+" "+tempContact;}
//        tempContact = (String)organizationForm.get("firstName");
//        if(tempContact != null && !tempContact.equals(EMPTY_STRING)){ contact = contact+" "+tempContact;}
//        tempContact = (String)organizationForm.get("middleName");
//        if(tempContact != null && !tempContact.equals(EMPTY_STRING)){ contact = contact+" "+tempContact+" ";}
        tempContact = (String)organizationForm.get("organization");
        if(tempContact != null && !tempContact.equals(EMPTY_STRING)){ contact = contact+"\n"+tempContact;}
        tempContact = (String)organizationForm.get("addressLine1");
        if(tempContact != null && !tempContact.equals(EMPTY_STRING)){ contact = contact+"\n"+tempContact;}
        tempContact = (String)organizationForm.get("addressLine2");
        if(tempContact != null && !tempContact.equals(EMPTY_STRING)){ contact = contact+"\n"+tempContact;}
        tempContact = (String)organizationForm.get("addressLine3");
        if(tempContact != null && !tempContact.equals(EMPTY_STRING)){ contact = contact+"\n"+tempContact;}
        tempContact = (String)organizationForm.get("city");
        if(tempContact != null && !tempContact.equals(EMPTY_STRING)){ contact = contact+"\n"+tempContact;}
        tempContact = (String)organizationForm.get("county");
        if(tempContact != null && !tempContact.equals(EMPTY_STRING)){ contact = contact+"\n"+tempContact;}
        tempContact = (String)organizationForm.get("state");
        if(tempContact != null && !tempContact.equals(EMPTY_STRING)){ contact = contact+"\n"+tempContact;}
        tempContact = (String)organizationForm.get("postalCode");
        if(tempContact != null && !tempContact.equals(EMPTY_STRING)){ contact = contact+"\n"+tempContact;}
        tempContact = (String)organizationForm.get("countryCode");
        if(tempContact != null && !tempContact.equals(EMPTY_STRING)){ contact = contact+"\n"+tempContact;}
        return contact;
    }
    
      //to prepare the update timestamp.
     public Timestamp prepareTimeStamp() throws Exception{
        Timestamp dbTimestamp = null;
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        dbTimestamp = coeusFunctions.getDBTimestamp();
        return dbTimestamp;
    }

     protected double formatStringToDouble(String strValue) throws Exception{
        double value = 0 ;
        if(strValue!=null && !strValue.equals(EMPTY_STRING)){
            String regExp = "[$,/,@,#,$,%,^,&,*,(,),-,_,+,%,!]";
            String replacement = strValue.replaceAll(regExp,EMPTY_STRING);
            if(replacement != null && !replacement.equals(EMPTY_STRING)){
            value = Double.parseDouble(replacement);
            }
        }
        return value;
    }
     
     
     protected LockBean getArraLockingBean(UserInfoBean userInfoBean, String arraAwardNumber, int arraReportNumber, HttpServletRequest request) throws Exception{
         
         LockBean lockBean = new LockBean();
         lockBean.setLockId(ARRA_LOCK_STRING + arraAwardNumber + "_" + arraReportNumber);
         
         lockBean.setMode(CoeusLiteConstants.MODIFY_MODE);
         lockBean.setModuleKey(CoeusLiteConstants.ARRA_MODULE);
         lockBean.setModuleNumber(arraAwardNumber+"_"+arraReportNumber);
         lockBean.setModuleUnitNumber(userInfoBean.getUnitNumber());
         lockBean.setUnitNumber(UNIT_NUMBER);
         lockBean.setUserId(userInfoBean.getUserId());
         lockBean.setUserName(userInfoBean.getUserName());
         lockBean.setSessionId(request.getSession().getId());
         return lockBean;
         
     }
     
     /**
      * This method is used for locking the Arra record. if the record cannot be locked, the method will return false.
      */
     protected boolean prepareLock(String mitAwardNumber, int reportNumber, HttpServletRequest request) throws Exception{
         
         HttpSession session = request.getSession();
         UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
         boolean recordLocked = true;
         
         LockBean lockBean = null;
//         LockBean sessionLockBean = (LockBean)session.getAttribute(CoeusLiteConstants.LOCK_BEAN+session.getId());
         
         lockBean = getArraLockingBean(userInfoBean, mitAwardNumber, reportNumber,request);
         LockBean dbLockBean = getLockedData(ARRA_LOCK_STRING +lockBean.getModuleNumber(), request);
         // Check if this record can be locked. 
         boolean lockAvailable = isLockExists(lockBean, lockBean.getModuleKey());
         boolean userAlreadyLocked = false; // This is to check if this ARRA record was already  locked by the logged in user from the same session.
         if(!lockAvailable) {
         // If this record cannot be locked, i.e, Lock already existes in the database    
             if(dbLockBean!=null && lockBean != null && lockBean.getSessionId().equals(dbLockBean.getSessionId())) {
             // Check if this record was locked from the same session.   
                 userAlreadyLocked = true;
             }
         }
         
         if(lockAvailable && !lockBean.getMode().equals(CoeusLiteConstants.DISPLAY_MODE)){
         // If this record can be locked.
             lockModule(lockBean,request);
             session.setAttribute(CoeusLiteConstants.LOCK_BEAN+session.getId(), lockBean);
             session.setAttribute(CoeusLiteConstants.MODE_DETAILS+session.getId(),CoeusLiteConstants.MODIFY_MODE);
         }else if(!userAlreadyLocked){
         // If the record cannot be locked and  the record was locked by another session    
             if(!lockBean.getMode().equals(CoeusLiteConstants.DISPLAY_MODE)){
             // If the record is not in Display mode    
                 showLockingMessage(lockBean.getModuleNumber(), request);
                 recordLocked = false;
             }
              
             session.setAttribute(CoeusLiteConstants.MODE_DETAILS+session.getId(),CoeusLiteConstants.DISPLAY_MODE);
         }
         
         return recordLocked;
      }
     
     /**
      * This methos is used for setting the Arra locking error message ('Arra record cannot be locked') in the Action Messages
      *
      */
     protected void showLockingMessage(String moduleNumber, HttpServletRequest request) throws Exception{
         HttpSession session = request.getSession();
         String lockId = ARRA_LOCK_STRING+moduleNumber;
//         WebTxnBean webTxnBean = new WebTxnBean();
          //Added for the case# COEUSQA-1697-View Restriction of User in Open Record-start
         UserInfoBean userInfoBean = (UserInfoBean)request.getSession().getAttribute("user"+request.getSession().getId());
         String loggedInUserId = userInfoBean.getUserId();
         CoeusFunctions coeusFunctions = new CoeusFunctions();
         //Added for the case# COEUSQA-1697-View Restriction of User in Open Record-end
         LockBean dbLockBean = getLockedData(lockId,request);
         if(dbLockBean!= null){
             dbLockBean.setModuleKey(CoeusLiteConstants.ARRA_MODULE);
             dbLockBean.setModuleNumber(moduleNumber);
             String lockUserId = dbLockBean.getUserId();
             UserMaintDataTxnBean userTxnBean = new UserMaintDataTxnBean();
             String lockUserName = userTxnBean.getUserName(lockUserId);
             String lockExists = "arra.alreadyLocked";
             ActionMessages messages = new ActionMessages();
             //Added for the case# COEUSQA-1697-View Restriction of User in Open Record-start
             String displayLockName = coeusFunctions.getParameterValue(CoeusConstants.DISPLAY_LOCKNAME_ARRA);
             if(displayLockName != null && "Y".equalsIgnoreCase(displayLockName.trim()) || lockUserId.equalsIgnoreCase(loggedInUserId)){
                 lockUserName=lockUserName;
             }else{
                 lockUserName = CoeusConstants.lockedUsername;
             }
             //Added for the case# COEUSQA-1697-View Restriction of User in Open Record-end
             messages.add("alreadyLocked", new ActionMessage(lockExists,lockUserName));
             saveMessages(request, messages);
             session.removeAttribute(CoeusLiteConstants.LOCK_BEAN+session.getId());
         }
     }
      
     protected void lockArraReportForEdit(String mitAwardNo, int reportNo, HttpServletRequest request) throws Exception {
         
         HttpSession session = request.getSession();
//         UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
         ArraAwardHeaderBean headerBean = (ArraAwardHeaderBean) session.getAttribute("arraAwardHeaderBean");
        // boolean arraReportCompleted = false;
         String arraReportCompleted = EMPTY_STRING;
         boolean finalReportFlag = false;
         if(headerBean != null){
             arraReportCompleted = headerBean.getComplete();
             finalReportFlag  = headerBean.isFinalReportFlag();
         }
        // if(arraReportCompleted){
         if("Y".equalsIgnoreCase(arraReportCompleted) || "S".equalsIgnoreCase(arraReportCompleted)){
            session.setAttribute(CoeusLiteConstants.MODE_DETAILS+session.getId(),CoeusLiteConstants.DISPLAY_MODE);
         } else {
             prepareLock(mitAwardNo, reportNo, request);
         }
     }
     
     protected File printGrantLoanReport(String arraReportNumber , String mitAwardNumber, HttpServletRequest request) throws Exception {
        
        
        ArraStream arraStream = new ArraStream();
        Hashtable htData = new Hashtable();
        htData.put("ARRA_REPORT_NUMBER", arraReportNumber);
        htData.put("MIT_AWARD_NUMBER", mitAwardNumber);
        htData.put("REPORT_TYPE","GRANT_LOAN_REPORT");
        String reportName = "ARRA_";
        
        //get the report path  from config
        String reportFolder = CoeusProperties.getProperty(CoeusPropertyKeys.REPORT_GENERATED_PATH,"Reports");
        String reportPath = getServlet().getServletContext().getRealPath("/")+reportFolder+"/";
        
        //Put the generated xml file in the reports folder
        GrantLoanReport grantLoanReport = (GrantLoanReport)arraStream.getObjectStream(htData);
        JAXBContext jaxbContext = JAXBContext.newInstance("edu.mit.coeus.utils.xml.bean.arra");
        
        //The following code validate the xml against the schema
        try{
            Validator v = jaxbContext.createValidator();
            v.setEventHandler(new DefaultValidationEventHandler(){public boolean handleEvent(ValidationEvent ve) {
                if (ve.getSeverity()==ve.FATAL_ERROR ||
                        ve .getSeverity()==ve.ERROR){
                    ValidationEventLocator  locator = ve.getLocator();
                    //print message from valdation event
                    UtilFactory.log("Error Thrown at:   " + locator.getObject().getClass().getName()
                    +"\nError Message:     " + ve.getMessage());
                }
                return true;
            }
            });
            v.validate(grantLoanReport);
        }catch(Exception ex){
             UtilFactory.log("FROM COEUS LITE : "+ex.getMessage(),ex,"ArraBaseAction","printGrantLoanReport");
        }
        
        //Remove the time stamp from the report for the date
        String packageName = "edu.mit.coeus.utils.xml.bean.arra";
        CoeusXMLGenrator xmlGenerator = new CoeusXMLGenrator();
        Document grantLoanReportDocument = xmlGenerator.marshelObject(grantLoanReport,packageName,true);
        String grantLoanReportXml = arraStream.replaceAll(grantLoanReportDocument);

        //Marshal the data
//        Marshaller marshaller = jaxbContext.createMarshaller();
//        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, new Boolean(true));
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        try{
//            marshaller.marshal(grantLoanReport, byteArrayOutputStream);
//        }catch(Exception ex){
//            request.setAttribute("Exception",ex);
//            throw ex;
//        }
        FileOutputStream fos = null;
        File xmlFile = null;
        try{
            //SimpleDateFormat dateFormat= new SimpleDateFormat("$MMddyyyy-hhmmss$");
            //Date reportDate = Calendar.getInstance().getTime();
            //String reportFullName = reportName+dateFormat.format(reportDate)+".xml";
            String reportFullName = reportName+grantLoanReport.getGrantLoanReportHeader().getAwardIdNumber();
        
            reportFullName = reportFullName.replaceAll("[/\\:*?\"<>|#]", "_");
            if(grantLoanReport.getGrantLoanSubRecipientReport().isEmpty()){
                reportFullName = reportFullName +".xml";
            }else {
                reportFullName = reportFullName +"_S.xml";
            }
            xmlFile = new File(reportPath,reportFullName);
            fos = new FileOutputStream(xmlFile);
            fos.write(grantLoanReportXml.getBytes());
            //byteArrayOutputStream.writeTo(fos);
        }catch(IOException ex){
            UtilFactory.log("FROM COEUS LITE : "+ex.getMessage(),ex,"ArraStream","printGrantLoanReport");
        }finally{
            try{
                fos.flush();
                fos.close();
            }catch(IOException ioEx){
                //Do nothing
            }
        }
        String url="/"+reportFolder + "/" + xmlFile.getName();
//        String templateURL = url;
         
        return xmlFile;
        
    }

     protected File printContractReport(String arraReportNumber , 
             String mitAwardNumber, HttpServletRequest request) throws Exception {
        
        ArraStream arraStream = new ArraStream();
        Hashtable htData = new Hashtable();
        htData.put("ARRA_REPORT_NUMBER", arraReportNumber);
        htData.put("MIT_AWARD_NUMBER", mitAwardNumber);
        htData.put("REPORT_TYPE","CONTRACT_REPORT");
        String reportName = "ARRA_";
        
        //get the report path  from config
        String reportFolder = CoeusProperties.getProperty(CoeusPropertyKeys.REPORT_GENERATED_PATH,"Reports");
        String reportPath = getServlet().getServletContext().getRealPath("/")+reportFolder+"/";
        
        //Put the generated xml file in the reports folder
        ContractReport contractReport = (ContractReport)arraStream.getObjectStream(htData);
        JAXBContext jaxbContext = JAXBContext.newInstance("edu.mit.coeus.utils.xml.bean.arra");
        
        //The following code validate the xml against the schema
        try{
            Validator v = jaxbContext.createValidator();
            v.setEventHandler(new DefaultValidationEventHandler(){public boolean handleEvent(ValidationEvent ve) {
                if (ve.getSeverity()==ve.FATAL_ERROR ||
                        ve .getSeverity()==ve.ERROR){
                    ValidationEventLocator  locator = ve.getLocator();
                    //print message from valdation event
                    UtilFactory.log("Error Thrown at:   " + locator.getObject().getClass().getName()
                    +"\nError Message:     " + ve.getMessage());
                }
                return true;
            }
            });
            v.validate(contractReport);
        }catch(Exception ex){
            UtilFactory.log("FROM COEUS LITE : "+ex.getMessage(),ex,"ArraBaseAction","printContractReport");
        }
        
        //Remove the time stamp from the report for the date
        String packageName = "edu.mit.coeus.utils.xml.bean.arra";
        CoeusXMLGenrator xmlGenerator = new CoeusXMLGenrator();
        Document contractReportDocument = xmlGenerator.marshelObject(contractReport,packageName,true);
        String contractReportXml = arraStream.replaceAll(contractReportDocument);

 
        FileOutputStream fos = null;
        File xmlFile = null;
        try{
   
            String reportFullName = reportName+contractReport.getContractReportHeader().getAwardIdNumber();
        
            reportFullName = reportFullName.replaceAll("[/\\:*?\"<>|#]", "_");
            if(contractReport.getSubRecipientReport().isEmpty()){
                reportFullName = reportFullName +".xml";
            }else {
                reportFullName = reportFullName +"_S.xml";
            }
            xmlFile = new File(reportPath,reportFullName);
            fos = new FileOutputStream(xmlFile);
            fos.write(contractReportXml.getBytes());
        }catch(IOException ex){
            UtilFactory.log("FROM COEUS LITE : "+ex.getMessage(),ex,"ArraStream","printContractReport");
        }finally{
            try{
                fos.flush();
                fos.close();
            }catch(IOException ioEx){
                //Do nothing
            }
        }
        String url="/"+reportFolder + "/" + xmlFile.getName();
//        String templateURL = url;
         
        return xmlFile;
        
    }
}
