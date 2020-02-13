/*
 * @(#)AwardMaintenanceServlet.java 1.0 3/11/03 8:11 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.servlet;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.bean.*;
import edu.mit.coeus.utils.ModuleConstants;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.moduleparameters.bean.ParameterBean;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.KeyConstants;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.award.bean.*;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentTxnBean;
import edu.mit.coeus.departmental.bean.DepartmentPersonTxnBean;
import edu.mit.coeus.instprop.bean.InstituteProposalLookUpDataTxnBean;
import edu.mit.coeus.irb.bean.ProtocolDataTxnBean;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.utils.query.And;
import edu.mit.coeus.utils.DateUtils;
import edu.mit.coeus.utils.ObjectCloner;
import edu.mit.coeus.utils.query.NotEquals;
import edu.mit.coeus.budget.bean.BudgetDataTxnBean;
import edu.mit.coeus.budget.bean.CostElementsBean;
import edu.mit.coeus.instprop.bean.*;
import edu.mit.coeus.irb.bean.PersonInfoTxnBean;
import edu.mit.coeus.irb.bean.PersonInfoFormBean;
import edu.mit.coeus.utils.locking.LockingException;
import edu.mit.coeus.utils.locking.LockingBean;

import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
import edu.mit.coeus.utils.ComboBoxBean;

import javax.servlet.http.*;
import javax.servlet.*;
import java.io.*;
import java.util.Vector;
import java.util.HashMap;
import java.util.Hashtable;
import java.sql.Date;
import java.util.Calendar;


/**This servlet is used for Award Report Requirements Maintenance. 
 * All Award Report Requirements related server calls are implemented here.
 *
 * @author  Shivakumar M J
 * Created on July 19, 2004, 10:52 AM
 */
public class AwardReportReqMaintenanceServlet extends CoeusBaseServlet implements TypeConstants{
    
    
//    Function codes used in AwardMaintenanceServlet
//    private static final char GET_VALID_REPORT_FREQUENCY_REP_REQ = 'W';
//    private static final char GENERATE_AWARD_REPORT_REQUIREMENT = 'X';
//    private static final char GET_AWARD_CONTACT = 'Y';
//    private static final char GET_AWARD_REPORTING_DETAILS = 'Z';
//    private static final char HAS_AWARD_REPORTING_REQUIREMENTS = 'E';
    
    private static final char GET_VALID_REPORT_FREQUENCY_REP_REQ = 'A';
    private static final char GENERATE_AWARD_REPORT_REQUIREMENT = 'B';
    private static final char GET_AWARD_CONTACT = 'C';
    private static final char GET_AWARD_REPORTING_DETAILS = 'D';
    private static final char HAS_AWARD_REPORTING_REQUIREMENTS = 'E';
    private static final char UPDATE_AWARD_REPORT = 'F';
    private static final char GET_PERSON_INFO_NAME = 'G';
    private static final char GET_AWARD_REPORTING_DETAILS_FOR_UPDATE = 'H';
    private static final char GET_AWARD_STATUS = 'I';
    private static final char UPDATE_AWARD_REPORT_AND_RELEASE_LOCK = 'J';
    private static final char RELEASE_LOCK = 'K';
	
	private static final char GET_REPORT_RIGHTS = 'L';
     /**
     * This method handles all the POST requests from the Client
     *
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws ServletException if any ServletException
     * @throws IOException if any IOException
     */    
    
    public void doPost(HttpServletRequest request,
    HttpServletResponse response)
    throws ServletException, IOException {
        
//        UtilFactory UtilFactory = new UtilFactory();
        
        // the request object from applet
        RequesterBean requester = null;
        // the response object to applet
        ResponderBean responder = new ResponderBean();
        
        // open object input/output streams
        ObjectInputStream inputFromApplet = null;
        ObjectOutputStream outputToApplet = null;
        
        String loggedinUser ="";
        String unitNumber = "";
        try{
            
             // get an input stream
            inputFromApplet = new ObjectInputStream(request.getInputStream());
            // read the serialized request object from applet
            requester = (RequesterBean) inputFromApplet.readObject();
            isValidRequest(requester);
            loggedinUser = requester.getUserName();
            
            // get the user
            UserInfoBean userBean = (UserInfoBean)new
                UserDetailsBean().getUserInfo(requester.getUserName());
            
            unitNumber = userBean.getUnitNumber();
            
            // keep all the beans into vector
            Vector dataObjects = new Vector();
            
            char functionType = requester.getFunctionType();
            AwardBean awardBean = null;
            AwardTxnBean awardTxnBean = new AwardTxnBean();
            AwardLookUpDataTxnBean awardLookUpDataTxnBean = new AwardLookUpDataTxnBean();
            String awardNumber="";
            int sequenceNumber;
            
            if(functionType == GET_VALID_REPORT_FREQUENCY_REP_REQ){
//                Commented by Shivakumar M J on 16 July 2004 as this Status data
//                is sent in function type 'Z'
//                int reportClassCode = ((Integer)requester.getDataObject()).intValue();
//                Hashtable hshReport = new Hashtable();
//                //Get Report for report class
//                CoeusVector awardData = awardLookUpDataTxnBean.getValidReportForReportClass(reportClassCode);
//                if(awardData==null){
//                    awardData = new CoeusVector();
//                }
//                hshReport.put(ReportBean.class, awardData);
//                
//                //Get Report Class, Report and Frequency mapping for given Report Class Code
//                awardData = new CoeusVector();
//                awardData = awardLookUpDataTxnBean.getValidReportClassReportFrequency(reportClassCode);
//                if(awardData==null){
//                    awardData = new CoeusVector();
//                }
//                hshReport.put(ValidReportClassReportFrequencyBean.class, awardData);
//                
//                //Get all Valid Frequency base
//                awardData = new CoeusVector();
//                awardData = awardLookUpDataTxnBean.getAllValidFrequencyBase();
//                if(awardData==null){
//                    awardData = new CoeusVector();
//                }
//                hshReport.put(FrequencyBaseBean.class, awardData);
//                
//                //Get OSP Distribution
//                awardData = new CoeusVector();
//                awardData = awardLookUpDataTxnBean.getDistribution();                
//                if(awardData==null){
//                    awardData = new CoeusVector();
//                }
//                hshReport.put(KeyConstants.DISTRIBUTION, awardData);
//                
//                //Get Report Status
//                
//                awardData = new CoeusVector();
//                awardData = awardLookUpDataTxnBean.getAwardReportStatus();                
//                if(awardData==null){
//                    awardData = new CoeusVector();
//                }
//                hshReport.put(KeyConstants.AWARD_REPORT_STATUS, awardData);
//                
//                responder.setDataObject(hshReport);
//                responder.setMessage(null);
//                responder.setResponseStatus(true);                
                
            }    
            
            else if(functionType == GENERATE_AWARD_REPORT_REQUIREMENT){

                String reqAwardNo = requester.getDataObject().toString();
//                Hashtable hshReport = new Hashtable();
                //Get Award Report Requirements checking code
                int awardRepReqCode = awardTxnBean.awardHasRepRequirement(reqAwardNo);
                if(awardRepReqCode < 0){
                    AwardAddRepReqTxnBean awardAddRepReqTxnBean = new AwardAddRepReqTxnBean(loggedinUser);
                    int generateAwardRepReq = awardAddRepReqTxnBean.generateAwardRepRequirement(reqAwardNo);
                    responder.setMessage(null);
                    responder.setResponseStatus(true);
                }
                else{
                    responder.setMessage(null);
                    responder.setResponseStatus(false);
                }    
               
                
//                responder.setDataObject(awardContactsData);
//                responder.setMessage(null);
                responder.setResponseStatus(true);                                
            }   
            
            else if(functionType == GET_AWARD_CONTACT){
//                Commented by Shivakumar M J on 16 July 2004 as this functionality
//                has been emebedded in GET_AWARD_REPORTING_DETAILS
//                String awardNo = requester.getDataObject().toString();
//                Hashtable hshReport = new Hashtable();
//                //Get Award contact
//                CoeusVector awardContactsData = awardTxnBean.getAwardContacts(awardNo);
//                if(awardContactsData==null){                    
//                    awardContactsData = new CoeusVector();
//                }                              
//                
//                responder.setDataObject(awardContactsData);
//                responder.setMessage(null);
//                responder.setResponseStatus(true);                                
            }    
            else if(functionType == GET_AWARD_REPORTING_DETAILS){
                
                Hashtable hshReport = new Hashtable();
                //modified by sharath - START
                //String mitAwardNumber = requester.getDataObject().toString();
                AwardReportReqBean bean = (AwardReportReqBean)requester.getDataObject();
                String mitAwardNumber = bean.getMitAwardNumber();
                //modified by sharath - END
                
                
                    //Get Award report contact requirements
                    AwardAddRepReqTxnBean awardAddRepReqTxnBean = new AwardAddRepReqTxnBean();
                    AwardReportReqBean awardReportReqBean = new AwardReportReqBean();
                    awardReportReqBean.setMitAwardNumber(mitAwardNumber);
                    CoeusVector awardData = awardAddRepReqTxnBean.getAwardRepReq(awardReportReqBean);

                    if(awardData==null){                    
                        awardData = new CoeusVector();
                    }                
                    hshReport.put(AwardReportReqBean.class, awardData);
                    //Get Report Status
                    awardData = awardAddRepReqTxnBean.getReportStatus();

                    if(awardData==null){                    
                        awardData = new CoeusVector();
                    }                
                    hshReport.put(KeyConstants.AWARD_REPORT_STATUS, awardData);

                    //Get Person info
    //                UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
    //                String personId = Frequester.getDataObject().toString();
    //                Vector vctGetPersonInfo = userMaintDataTxnBean.getPersonInfo(personId);
    //                CoeusVector cvPersonInfo = new CoeusVector();
    //                PersonInfoBean personInfoBean = new PersonInfoBean();
    //                personInfoBean.setPriorName(vctGetPersonInfo.get(0).toString());
    //                personInfoBean.setDirTitle(vctGetPersonInfo.get(1).toString());
    //                personInfoBean.setFacFlag(vctGetPersonInfo.get(2).toString());
    //                personInfoBean.setHomeUnit(vctGetPersonInfo.get(3).toString());
    //                cvPersonInfo.add(personInfoBean);
    //                hshReport.put(PersonInfoBean.class, cvPersonInfo);

                    //Get Rep class in Award Rep
                    CoeusVector awardReportClassData = awardAddRepReqTxnBean.getRepClassInAwardRep(mitAwardNumber);
                    if(awardReportClassData==null){                    
                        awardReportClassData = new CoeusVector();
                    }
                    hshReport.put(ComboBoxBean.class, awardReportClassData);

                    //Get Award Contacts data
                    CoeusVector awardContactsData = awardTxnBean.getAwardContacts(mitAwardNumber);
                    if(awardContactsData==null){                    
                        awardContactsData = new CoeusVector();
                    }                                
                    hshReport.put(AwardContactDetailsBean.class, awardContactsData);
                    // Modified for COEUSQA-1412 Subcontract Module changes - Start    
//                    CoeusVector awardContactTypeData = awardLookUpDataTxnBean.getContactTypes();
                    CoeusVector awardContactTypeData = awardLookUpDataTxnBean.getContactTypesForModule(ModuleConstants.AWARD_MODULE_CODE);
                    // Modified for COEUSQA-1412 Subcontract Module changes - End
                    if(awardContactTypeData==null){                    
                        awardContactTypeData = new CoeusVector();
                    }                                
                    hshReport.put(KeyConstants.AWARD_CONTACT_TYPE, awardContactTypeData);

                    responder.setDataObject(hshReport);
                    responder.setMessage(null);
                    responder.setResponseStatus(true);                
            }    
            
             else if(functionType == HAS_AWARD_REPORTING_REQUIREMENTS){
                String awardNo = requester.getDataObject().toString();
                //Get Award Report Requirements checking code
                int awardRepReqCode = awardTxnBean.awardHasRepRequirement(awardNo);
                boolean hasAwardRepReq;
                if(awardRepReqCode > 0){
                    responder.setDataObject(new Boolean(true));
                }
                else{
                    responder.setDataObject(new Boolean(false));
                }    
                //responder.setDataObject(new Boolean(hasAwardRepReq));
                responder.setMessage(null);
                responder.setResponseStatus(true);                                
            }                
            else if(functionType == UPDATE_AWARD_REPORT){
                Hashtable hshReport = new Hashtable();
                Hashtable awardReportData = (Hashtable)requester.getDataObject();
                AwardReportReqBean awardReportReqBean = new AwardReportReqBean();     
                CoeusVector cvAwardReportData = (CoeusVector)awardReportData.get(AwardReportReqBean.class);
                if (cvAwardReportData!=null && cvAwardReportData.size()>0) {
                  for(int index=0; index < cvAwardReportData.size(); index++){
                      awardReportReqBean = (AwardReportReqBean)cvAwardReportData.elementAt(index);
                      if(awardReportReqBean.getAcType()==null) {
                          continue;
                      }
                      AwardAddRepReqTxnBean awardAddRepReqTxnBean = new AwardAddRepReqTxnBean(loggedinUser);
                      boolean success = awardAddRepReqTxnBean.updateAwardRepReq(awardReportReqBean);                      
                      CoeusVector awardData = awardAddRepReqTxnBean.getAwardRepReq(awardReportReqBean);
                      hshReport.put(AwardReportReqBean.class, awardData);
                      responder.setDataObject(hshReport);
                      responder.setResponseStatus(true);
                  }    
                }                
            }    
            else if(functionType == GET_PERSON_INFO_NAME){
                String fullName = requester.getDataObject().toString();
//                PersonInfoTxnBean personInfoTxnBean = new PersonInfoTxnBean();
                AwardAddRepReqTxnBean awardAddRepReqTxnBean = new AwardAddRepReqTxnBean();
                PersonInfoFormBean personInfoFormBean = new PersonInfoFormBean();
                personInfoFormBean = awardAddRepReqTxnBean.getPersonInfoForName(fullName);
                responder.setDataObject(personInfoFormBean);
                responder.setMessage(null);
                responder.setResponseStatus(true);                                                
            }    
            else if(functionType == GET_AWARD_REPORTING_DETAILS_FOR_UPDATE){                
                Hashtable hshReport = new Hashtable();
                //modified by sharath - START
                //String mitAwardNumber = requester.getDataObject().toString();
                AwardReportReqBean bean = (AwardReportReqBean)requester.getDataObject();
                String mitAwardNumber = bean.getMitAwardNumber();
                //modified by sharath - END
                AwardAddRepReqTxnBean awardAddRepReqTxnBean = new AwardAddRepReqTxnBean();
//                AwardTxnBean awardTxnBean = new AwardTxnBean();
                // Commented by Shivakumar as call to getAwardLock method has to be modified.
//                boolean isAvailable = awardTxnBean.getAwardLock(mitAwardNumber);
                // Added by Shivakumar -BEGIN
                //LockingBean lockingBean =  awardTxnBean.getAwardLock(mitAwardNumber);  
                LockingBean lockingBean =  awardTxnBean.getAwardLock(mitAwardNumber,loggedinUser,unitNumber);
                boolean isAvailable = lockingBean.isGotLock();
                // Added by Shivakumar -END                
                if(isAvailable){
                    // Try/catch block to check that locking and retrieving data process are executed successfully.
                    try{
                        //Get Award report contact requirements
    //                    AwardAddRepReqTxnBean awardAddRepReqTxnBean = new AwardAddRepReqTxnBean();
                        AwardReportReqBean awardReportReqBean = new AwardReportReqBean();
                        awardReportReqBean.setMitAwardNumber(mitAwardNumber);
                        CoeusVector awardData = awardAddRepReqTxnBean.getAwardRepReq(awardReportReqBean);

                        if(awardData==null){                    
                            awardData = new CoeusVector();
                        }                
                        hshReport.put(AwardReportReqBean.class, awardData);
                        //Get Report Status
                        awardData = awardAddRepReqTxnBean.getReportStatus();

                        if(awardData==null){                    
                            awardData = new CoeusVector();
                        }                
                        hshReport.put(KeyConstants.AWARD_REPORT_STATUS, awardData);

                        //Get Person info
        //                UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
        //                String personId = Frequester.getDataObject().toString();
        //                Vector vctGetPersonInfo = userMaintDataTxnBean.getPersonInfo(personId);
        //                CoeusVector cvPersonInfo = new CoeusVector();
        //                PersonInfoBean personInfoBean = new PersonInfoBean();
        //                personInfoBean.setPriorName(vctGetPersonInfo.get(0).toString());
        //                personInfoBean.setDirTitle(vctGetPersonInfo.get(1).toString());
        //                personInfoBean.setFacFlag(vctGetPersonInfo.get(2).toString());
        //                personInfoBean.setHomeUnit(vctGetPersonInfo.get(3).toString());
        //                cvPersonInfo.add(personInfoBean);
        //                hshReport.put(PersonInfoBean.class, cvPersonInfo);

                        //Get Rep class in Award Rep
                        CoeusVector awardReportClassData = awardAddRepReqTxnBean.getRepClassInAwardRep(mitAwardNumber);
                        if(awardReportClassData==null){                    
                            awardReportClassData = new CoeusVector();
                        }
                        hshReport.put(ComboBoxBean.class, awardReportClassData);

                        //Get Award Contacts data
                        CoeusVector awardContactsData = awardTxnBean.getAwardContacts(mitAwardNumber);
                        if(awardContactsData==null){                    
                            awardContactsData = new CoeusVector();
                        }                                
                        hshReport.put(AwardContactDetailsBean.class, awardContactsData);

                        // Modified for COEUSQA-1412 Subcontract Module changes - Start
//                    CoeusVector awardContactTypeData = awardLookUpDataTxnBean.getContactTypes();
                        CoeusVector awardContactTypeData = awardLookUpDataTxnBean.getContactTypesForModule(ModuleConstants.AWARD_MODULE_CODE);
                        // Modified for COEUSQA-1412 Subcontract Module changes - End

                        if(awardContactTypeData==null){                    
                            awardContactTypeData = new CoeusVector();
                        }                                
                        hshReport.put(KeyConstants.AWARD_CONTACT_TYPE, awardContactTypeData);
                        awardTxnBean.transactionCommit();
                        responder.setLockingBean(lockingBean);
                    }catch(DBException dbEx){
                            dbEx.printStackTrace();
                            awardTxnBean.transactionRollback();
                            throw dbEx;
                    }finally{
                        awardTxnBean.endConnection();
                    }    
                    responder.setDataObject(hshReport);
                    responder.setMessage(null);
                    responder.setResponseStatus(true);
                    
                }/*   
                else{                    
                    CoeusMessageResourcesBean coeusMessageResourcesBean
                            =new CoeusMessageResourcesBean();
                    String msg = coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1003")+" "+mitAwardNumber+" "+coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1002");
                    responder.setMessage(msg);
                    responder.setResponseStatus(false);
                }  */  
            }
            else if(functionType == GET_AWARD_STATUS){
                String mitAwardNumber = requester.getDataObject().toString();
                AwardAddRepReqTxnBean awardAddRepReqTxnBean = new AwardAddRepReqTxnBean();
                // Commented by Shivakumar as call to getAwardLock method has to be modified.
//                boolean isAvailable = awardAddRepReqTxnBean.getAwardLock(mitAwardNumber);
                // Added by Shivakumar -BEGIN
                //LockingBean lockingBean =  awardTxnBean.getAwardLock(mitAwardNumber);
                LockingBean lockingBean =  awardTxnBean.getAwardLock(mitAwardNumber,loggedinUser,unitNumber);
                awardTxnBean.transactionCommit();
                boolean isAvailable = lockingBean.isGotLock();
                // Added by Shivakumar -END  
                if(isAvailable){                    
                    responder.setDataObject(new Boolean(false));     
                    responder.setMessage(null);
                }
                else{
                    responder.setDataObject(new Boolean(true));
                    responder.setMessage("Award "+mitAwardNumber +" is being used by another user");
                }                    
                responder.setResponseStatus(true);
            }
            
            else if(functionType == UPDATE_AWARD_REPORT_AND_RELEASE_LOCK){
                Hashtable hshReport = new Hashtable();
                Hashtable awardReportData = (Hashtable)requester.getDataObject();
                AwardReportReqBean awardReportReqBean = new AwardReportReqBean();                
                CoeusVector cvAwardReportData = (CoeusVector)awardReportData.get(AwardReportReqBean.class);
                if (cvAwardReportData!=null && cvAwardReportData.size()>0) {
                  for(int index=0; index < cvAwardReportData.size(); index++){
                      awardReportReqBean = (AwardReportReqBean)cvAwardReportData.elementAt(index);
                      if(awardReportReqBean.getAcType()==null) {
                          continue;
                      }
                      AwardAddRepReqTxnBean awardAddRepReqTxnBean = new AwardAddRepReqTxnBean(loggedinUser);
                      boolean success = awardAddRepReqTxnBean.updateAwardRepReq(awardReportReqBean);                      
//                      responder.setResponseStatus(true);
                  }    
                }
//                awardReportReqBean = (AwardReportReqBean)awardReportData.get(AwardReportReqBean.class);                        
                boolean releaseLock = ((Boolean)awardReportData.get(CoeusConstants.IS_RELEASE_LOCK)) == null 
                        ? false : ((Boolean)awardReportData.get(CoeusConstants.IS_RELEASE_LOCK)).booleanValue();                        
                      

                    //Release the lock                     
                    String awardReportNumber = awardReportReqBean.getMitAwardNumber();
                    //awardTxnBean.releaseEdit(awardReportNumber,loggedinUser);
                    // Calling new releaseLock method for bug fixing - BEGIN
                    LockingBean lockingBean = awardTxnBean.releaseLock(awardReportNumber,loggedinUser);
                    responder.setLockingBean(lockingBean);
                    // Calling new releaseLock method for bug fixing - END
                    responder.setMessage(null);
                    responder.setResponseStatus(true);                    
            }    
            else if(functionType == RELEASE_LOCK){
                String releaseAwardNo = requester.getDataObject().toString();
                //awardTxnBean.releaseEdit(releaseAwardNo,loggedinUser);
                // Calling new releaseLock method for bug fixing - BEGIN
                LockingBean lockingBean = awardTxnBean.releaseLock(releaseAwardNo,loggedinUser);
                responder.setLockingBean(lockingBean);
                // Calling new releaseLock method for bug fixing - END
                responder.setMessage(null);
                responder.setResponseStatus(true);                    
            } else if (functionType == GET_REPORT_RIGHTS) {
                                //Code added for Case#3388 - Implementing authorization check at department level
                                String awardNo = (String)requester.getDataObject();
				UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
				HashMap rightsMap = new HashMap();
                                // 3587 Multi Campus Enhancements - Start
                                String leadUnit = awardTxnBean.getLeadUnitForAward(awardNo);
//				boolean hasCreateRight = userMaintDataTxnBean.getUserHasOSPRight(loggedinUser, "CREATE_AWARD");
                                boolean hasCreateRight = userMaintDataTxnBean.getUserHasRight(loggedinUser, "CREATE_AWARD", leadUnit);
				rightsMap.put(KeyConstants.CREATE_RIGHTS, new Boolean(hasCreateRight));
//				boolean hasModifyRight = userMaintDataTxnBean.getUserHasOSPRight(loggedinUser, "MODIFY_AWARD");
                                boolean hasModifyRight = userMaintDataTxnBean.getUserHasRight(loggedinUser, "MODIFY_AWARD", leadUnit);
                                // 3587 Multi Campus Enhancements - End
				rightsMap.put(KeyConstants.MODIFY_RIGHTS, new Boolean(hasModifyRight));
				boolean hasUnitRight = userMaintDataTxnBean.getUserHasRightInAnyUnit(loggedinUser, "VIEW_AWARD");
                                // 3587: Multi Campus Enhancement
                                String leadUnitNum = awardTxnBean.getLeadUnitForAward(awardNo);
                                //Code added for Case#3388 - Implementing authorization check at department level - starts
                                if(!hasUnitRight){
                                    if(awardNo != null){
                                        // 3587: Multi Campus Enhancement
//                                        String leadUnitNum = awardTxnBean.getLeadUnitForAward(awardNo);
                                        hasUnitRight = userMaintDataTxnBean.getUserHasRight(loggedinUser, "VIEW_AWARDS_AT_UNIT", leadUnitNum);
                                    }
                                }
                                //Code added for Case#3388 - Implementing authorization check at department level - ends
				rightsMap.put(KeyConstants.VIEW_RIGHTS, new Boolean(hasUnitRight));
                                // 3587: Multi Campus Enhancement - Start
//				boolean hasMaintainRight = userMaintDataTxnBean.getUserHasOSPRight(loggedinUser, "MAINTAIN_REPORTING");
                                boolean hasMaintainRight = userMaintDataTxnBean.getUserHasRight(loggedinUser, "MAINTAIN_REPORTING", leadUnitNum);
                                // // 3587: Multi Campus Enhancement - End
				rightsMap.put(KeyConstants.MAINTAIN_RIGHTS, new Boolean(hasMaintainRight));
				responder.setDataObject(rightsMap);
				responder.setMessage(null);
				responder.setResponseStatus(true);
			}    
        }catch( LockingException lockEx ) {
               //lockEx.printStackTrace();
               LockingBean lockingBean = lockEx.getLockingBean();
               String errMsg = lockEx.getErrorMessage();        
               CoeusMessageResourcesBean coeusMessageResourcesBean
                    =new CoeusMessageResourcesBean();
                errMsg= coeusMessageResourcesBean.parseMessageKey(errMsg);            
                responder.setResponseStatus(false);    
                responder.setException(lockEx);
                responder.setMessage(errMsg);               
                UtilFactory.log( errMsg, lockEx,
                "AwardReportReqMaintenanceServlet", "doPost");
        }catch( CoeusException coeusEx ) {
            //coeusEx.printStackTrace();
            int index=0;
            String errMsg;
            if(coeusEx.getErrorId()==999999){
                errMsg = "dbEngine_intlErr_exceptionCode.1028";
                responder.setLocked(true);
            }else{
                errMsg = coeusEx.getMessage();
            }
            CoeusMessageResourcesBean coeusMessageResourcesBean
            =new CoeusMessageResourcesBean();
            errMsg= coeusMessageResourcesBean.parseMessageKey(errMsg);            
            responder.setResponseStatus(false);
            responder.setException(coeusEx);
            responder.setMessage(errMsg);
            UtilFactory.log( errMsg, coeusEx,
            "AwardReportReqMaintenanceServlet", "doPost");
        }catch( DBException dbEx ) {
            //dbEx.printStackTrace();
            int index=0;
            String errMsg = dbEx.getUserMessage();
            if (dbEx.getErrorId() == 20102 ) {
                errMsg = "dbEngine_intlErr_exceptionCode.1028";
            }
            if (errMsg.equals("db_exceptionCode.1111")) {
                responder.setCloseRequired(true);
            }
            CoeusMessageResourcesBean coeusMessageResourcesBean
            = new CoeusMessageResourcesBean();
            errMsg= coeusMessageResourcesBean.parseMessageKey(errMsg);
            
            responder.setResponseStatus(false);
            responder.setException(dbEx);
            responder.setMessage(errMsg);
            UtilFactory.log( errMsg, dbEx,
            "AwardReportReqMaintenanceServlet", "doPost");
            
        }catch(Exception e) {
            //e.printStackTrace();
            responder.setResponseStatus(false);
            responder.setException(e);
            responder.setMessage(e.getMessage());
            UtilFactory.log( e.getMessage(), e,
            "AwardReportReqMaintenanceServlet", "doPost");
        //Case 3193 - START
        }catch(Throwable throwable){
            Exception ex = new Exception(throwable);
            responder.setException(ex);
            responder.setResponseStatus(false);
            responder.setMessage(ex.getMessage());
            UtilFactory.log( throwable.getMessage(), throwable, "AwardReportReqMaintenanceServlet", "doPost");
        //Case 3193 - END
        } finally {
            try{
                
                outputToApplet
                = new ObjectOutputStream(response.getOutputStream());
                outputToApplet.writeObject(responder);
                // close the streams
                if (inputFromApplet!=null){
                    inputFromApplet.close();
                }
                if (outputToApplet!=null){
                    outputToApplet.flush();
                    outputToApplet.close();
                }
            }catch (IOException ioe){
                UtilFactory.log( ioe.getMessage(), ioe,
                "AwardReportReqMaintenanceServlet", "doPost");
            }
        }
    }    
        
    
    
}
