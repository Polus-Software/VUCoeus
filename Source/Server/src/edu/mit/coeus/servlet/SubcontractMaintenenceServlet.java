/*
 * SubcontractMaintenenceServlet.java
 *
 * Created on September 4, 2004, 11:46 AM
 */

/* 
 * PMD check performed, and commented unused imports and variables on 20 Jan 2009
 * by Sreenath
 */

package edu.mit.coeus.servlet;

/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * @author  sharathk
 */

import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
import edu.mit.coeus.utils.ModuleConstants;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;

import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.bean.*;
import edu.mit.coeus.utils.locking.LockingBean;
import edu.mit.coeus.utils.locking.LockingException;
import edu.mit.coeus.utils.dbengine.DBException;
//import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.KeyConstants;
import edu.mit.coeus.subcontract.bean.*;
import edu.mit.coeus.award.bean.*;
//import edu.mit.coeus.award.report.ReportGenerator;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentTxnBean;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.ParameterUtils;
//import edu.mit.coeus.utils.CoeusConstants;

//import edu.mit.coeus.utils.CoeusProperties;
//import edu.mit.coeus.utils.CoeusPropertyKeys;
//import edu.mit.coeus.utils.xml.bean.subcontract.generator.SubcontractStream;
//import java.io.InputStream;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.text.SimpleDateFormat;

//import org.jfor.jfor.converter.Converter;
//import org.jfor.jfor.main.JForLogConfig;
//import org.xml.sax.InputSource;

public class SubcontractMaintenenceServlet extends CoeusBaseServlet{
    
    private static final char GET_SUNCONTRACT_DATA = 'A';
    private static final char GET_UNIT_NAME = 'B';
    private static final char GET_ORG_NAME = 'C';
    private static final char GET_NEW_SUBCONTRACT = 'N';
    private static final char GET_SUBCONTRACT_AWARD_INFO = 'D';
    private static final char SAVE_DATA = 'S';
    private static final char SAVE_DATA_AND_RELEASE_LOCK = 'R';
    private static final char RELEASE_LOCK = 'U';
    private static final char DISPLAY_MODE = 'K';
    private static final char GET_SEQUENCE_NUMBER = 'E';
    private static final char GET_RTF_FORM_LIST = 'J';
//    private static final char GET_RTF_FORM_VARIABLE_AND_PRINT = 'V';
    private static final char OSP_USER_RIGHT_CHECK_AND_SUB_REPORTS = 'O';
    private static final char GET_SUBCONTRACTING_GOALS_AMTS = 'G';
    private static final char UPDATE_SUBCONTRACTING_GOALS = 'H';
    private static final char SUB_VALIDATION_CHECKS = 'I';
    private static final char GET_TEMPLATE = 'Z';
   
//    private static final char RTF_MODE = 'R';
//    private static final char PDF_MODE = 'P';
    
    private static final char GET_FISCAL_PERIOD = 'F' ;
    
//    private static final String MAINTAIN_CODE_TABLES = "MAINTAIN_CODE_TABLES";
	private static final String MODIFY_SUBCONTRACTING_GOALS = "MODIFY_SUBCONTRACTING_GOALS";
	private static final String MODIFY_SUBCONTRACT = "MODIFY_SUBCONTRACT";
    //Code added for princeton enhancement case#2802
    //To get the Amount information details for the given subcontractId    
    private static final char GET_AMOUNT_INFO = 'a';
    private static final char GET_INVOICE_DETAILS = 'i';
    // 3587: Multi Campus Enahncements - Start
    private static final char CHECK_USER_HAS_CREATE_RIGHT = 'L';
    private static final char CHECK_USER_HAS_MODIFY_RIGHT = 'M';
    private static final String CREATE_SUBCONTRACT = "CREATE_SUBCONTRACT";
    private static final char CHECK_USER_HAS_GOAL_RIGHT = 'Q';
    // 3587: Multi Campus Enahncements - End
    // COEUSDEV-185	Proposal Development and Subawards are viewable from medusa without appropriate role - Start
    private static final char CHECK_CAN_VIEW_SUB_CONTRACT = 'j';
    private static final String VIEW_SUBCONTRACT = "VIEW_SUBCONTRACT";
    // COEUSDEV-185	Proposal Development and Subawards are viewable from medusa without appropriate role - End
    
    //COEUSQA: - Start
    private static final char GET_DOCUMENT_TYPE = 'b';
    private static final char ADD_UPD_DEL_SUBCONTRACT_ATTACH = 's';
    private static final char GET_SUBCONTRACT_UPLOAD_ATTACH = 'o';
    //COEUSQA: End
    // Added for COEUSQA-1412 Subcontract Module changes - Start
    private final char GET_SUBCONTRACT_TEMPLATE_TYPES = '1';
    private final char GET_ATTACHMENTS_FOR_GENERATE_AGREEMENT = '2';
    // Added for COEUSQA-1412 Subcontract Module changes - End
    //Added for COEUS-58- Start
    private static final char GET_DOCUMENT_RIGHTS = 'n';
    private static final String VIEW_SUBCONTRACT_DOCUMENTS = "VIEW_SUBCONTRACT_DOCUMENTS";
    //Added for COEUS-58- End

    /** Creates a new instance of SubcontractMaintenenceServlet */
    public SubcontractMaintenenceServlet() {
    }
    
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        
        // the request object from applet
        RequesterBean requester = null;
        // the response object to applet
        ResponderBean responder = new ResponderBean();
        
        // open object input/output streams
        ObjectInputStream inputFromApplet = null;
        ObjectOutputStream outputToApplet = null;
        
        String loggedinUser ="";
        String unitNumber = "";
        
        try {
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
            
            char functionType = requester.getFunctionType();
            //Added for Princeton enhancement case#2802
            //While sending e-mail invoice link to open in coeuslite to be send.
            String path = request.getRequestURL().toString();
            path = path.substring(0, path.lastIndexOf("/")+1);            
            if(functionType == GET_SUNCONTRACT_DATA || functionType==GET_NEW_SUBCONTRACT || functionType == GET_SEQUENCE_NUMBER ) {
                
                SubContractBaseBean subContractBaseBean = (SubContractBaseBean)requester.getDataObject();
                String subContractCode = subContractBaseBean.getSubContractCode();
                SubContractTxnBean subContractTxnBean = new SubContractTxnBean();
                AwardLookUpDataTxnBean awardLookUpDataTxnBean = new AwardLookUpDataTxnBean();
                SubContractBean subContractBean = new SubContractBean();
                if(functionType == GET_SUNCONTRACT_DATA || functionType == GET_SEQUENCE_NUMBER){
                    LockingBean lockingBean = subContractTxnBean.getSubContractLock(subContractCode, loggedinUser, unitNumber);
                    try{
                        boolean lockCheck = lockingBean.isGotLock();
                        if(lockCheck){
                            
                            //Data For Detail Tab
                            subContractBean = subContractTxnBean.getSubContract(subContractCode,functionType);
                            
                            CoeusVector cvSubContractStatus = subContractTxnBean.getSubContractStatus();
                            CoeusVector cvAwardType = awardLookUpDataTxnBean.getAwardType();
                            
                            //Data For Funding Source Tab
                            CoeusVector cvSubContractFundingSource = null;
                            if(subContractBean.getFundingSourceIndicator() != null && subContractBean.getFundingSourceIndicator().charAt(0) == 'P') {
                                cvSubContractFundingSource = subContractTxnBean.getSubContractAwards(subContractCode);
                            }
                            //Data for Amount Info Tab
                            CoeusVector cvSubContractAmtInfo = subContractTxnBean.getSubContractAmountInfo(subContractCode);
                            
                            //Data for AmountReleased Tab
                            CoeusVector cvSubContractAmtReleased = subContractTxnBean.getSubContractAmountReleased(subContractCode);
                            
                            // Data for the Subcontract closeout
                            CoeusVector cvCloseOut = null;
                            if(subContractBean.getCloseOutIndicator()!=null && subContractBean.getCloseOutIndicator().charAt(0)=='P'){
                                cvCloseOut = subContractTxnBean.getSubcontractCloseOut(subContractCode);
                            }
                            // Get data for the closeoutTypes
                            
                            CoeusVector cvCloseoutTypes = subContractTxnBean.getSubcontractCloseoutTypes();
                            //Data for Contacts
                            // Modified for COEUSQA-1412 Subcontract Module changes - Start
//                            CoeusVector cvContactTypes = awardLookUpDataTxnBean.getContactTypes();
                            CoeusVector cvContactTypes = awardLookUpDataTxnBean.getContactTypesForModule(ModuleConstants.SUBCONTRACTS_MODULE_CODE);
                            // Modified for COEUSQA-1412 Subcontract Module changes - End
                            
                            // Get the data for the Subcontract contacts details - Added by chandra
                            CoeusVector cvSubContacts = subContractTxnBean.getSubcontractContacts(subContractCode);
                            // Get the data for Subcontract Custom Data
                            CoeusVector cvSubContractCustomData = subContractTxnBean.getCustomData(subContractCode);

                            //Added for Case#2402 - use a parameter to set the length of the account number throughout app - Start
                            CoeusVector cvParameters = getParamaters();
                            //Case#2402 - End
                            // Addded for COEUSQA-1412 Subcontract Module changes - Change - Start
                            CoeusVector cvReports = subContractTxnBean.getSubcontractReports(subContractCode);
                            SubcontractTemplateInfoBean  subcontractTemplateInfoBean = subContractTxnBean.getSubcontractTemplateInfo(subContractCode);           
                            CoeusVector cvTemplateInfo = new CoeusVector();
                            if(subcontractTemplateInfoBean != null){
                                cvTemplateInfo.add(subcontractTemplateInfoBean);
                            }
                            // Addded for COEUSQA-1412 Subcontract Module changes - Change - End
                            //putting all the details to a hashtable to send to the client side
                            Hashtable htSubContractData = new Hashtable();
                            htSubContractData.put(SubContractBean.class, subContractBean);
                            htSubContractData.put(KeyConstants.SUBCONTRACT_STATUS, cvSubContractStatus);
                            htSubContractData.put(KeyConstants.AWARD_TYPE, cvAwardType);
                            htSubContractData.put(KeyConstants.CLOSEOUT_TYPES, cvCloseoutTypes);
                            // Addded for COEUSQA-1412 Subcontract Module changes - Change - Start
                            htSubContractData.put(KeyConstants.SUBCONTRACT_COST_TYPES, subContractTxnBean.getCostTypes());
                            htSubContractData.put(KeyConstants.SUBCONTRACT_REPORT_TYPES, subContractTxnBean.getReportTypes());
                            htSubContractData.put(KeyConstants.SUBCONTRACT_CONTACT_TYPES, 
                                    subContractTxnBean.getContactTypesForModule(ModuleConstants.SUBCONTRACTS_MODULE_CODE));
                            htSubContractData.put(KeyConstants.SUBCONTRACT_COPYRIGHT_TYPES, subContractTxnBean.getCopyRights());
                            htSubContractData.put(SubcontractReportBean.class, cvReports == null ? new CoeusVector() : cvReports);
                            htSubContractData.put(SubcontractTemplateInfoBean.class, cvTemplateInfo);
                            // Addded for COEUSQA-1412 Subcontract Module changes - Change - End
                            htSubContractData.put(SubContractFundingSourceBean.class, cvSubContractFundingSource == null ? new CoeusVector() : cvSubContractFundingSource);
                            htSubContractData.put(SubContractAmountInfoBean.class, cvSubContractAmtInfo == null ? new CoeusVector() : cvSubContractAmtInfo);
                            htSubContractData.put(SubcontractCloseoutBean.class, cvCloseOut == null ? new CoeusVector() : cvCloseOut);
                            htSubContractData.put(SubcontractContactDetailsBean.class, cvSubContacts == null ? new CoeusVector() : cvSubContacts);
                            
                            
                            htSubContractData.put(SubContractAmountReleased.class, cvSubContractAmtReleased == null ? new CoeusVector() : cvSubContractAmtReleased);
                            
                            htSubContractData.put(KeyConstants.AWARD_CONTACT_TYPE, cvContactTypes);
                            
                            htSubContractData.put(SubContractCustomDataBean.class,cvSubContractCustomData == null ? new CoeusVector() : cvSubContractCustomData);

                            //Added for Case#2402 - use a parameter to set the length of the account number throughout app - Start
                            htSubContractData.put(CoeusParameterBean.class, cvParameters == null ? new CoeusVector() : cvParameters);
                            //Case#2402 - End

                            subContractTxnBean.transactionCommit();
                            responder.setDataObject(htSubContractData);
                            responder.setLockingBean(lockingBean);
                            responder.setMessage(null);
                            responder.setResponseStatus(true);
                        }else{
                            subContractTxnBean.transactionRollback();
                            responder.setResponseStatus(false);
                        }
                    }catch(DBException dbEx){
//                        dbEx.printStackTrace();
                        subContractTxnBean.transactionRollback();
                        throw dbEx;
                    }finally{
                        subContractTxnBean.endConnection();
                    }
                }else{
                    String newSubcontractNumber = subContractTxnBean.getNewSubcontract();
                    subContractBean.setSubContractCode(newSubcontractNumber);
                    
                    //Data For Detail Tab
                    
                    //Data for Contacts
                    // Modified for COEUSQA-1412 Subcontract Module changes - Start
//                            CoeusVector cvContactTypes = awardLookUpDataTxnBean.getContactTypes();
                    CoeusVector cvContactTypes = awardLookUpDataTxnBean.getContactTypesForModule(ModuleConstants.SUBCONTRACTS_MODULE_CODE);
                    // Modified for COEUSQA-1412 Subcontract Module changes - End
                    
                    CoeusVector cvSubContractStatus = subContractTxnBean.getSubContractStatus();
                    CoeusVector cvAwardType = awardLookUpDataTxnBean.getAwardType();
                    
                    // Get data for the closeoutTypes
                    CoeusVector cvCloseoutTypes = subContractTxnBean.getSubcontractCloseoutTypes();
                    
                    // Get the data for Subcontract Custom Data
                    CoeusVector cvSubContractCustomData = subContractTxnBean.getCustomData(newSubcontractNumber);
                    //Added for Case#2402 - use a parameter to set the length of the account number throughout app - Start
                    CoeusVector cvParameters = getParamaters();
                    //Case#2402 - End
                    //putting all the details to a hashtable to send to the client side
                    Hashtable htSubContractData = new Hashtable();
                    htSubContractData.put(SubContractBean.class, subContractBean);
                    htSubContractData.put(KeyConstants.SUBCONTRACT_STATUS, cvSubContractStatus);
                    htSubContractData.put(KeyConstants.AWARD_TYPE, cvAwardType);
                    htSubContractData.put(KeyConstants.CLOSEOUT_TYPES, cvCloseoutTypes);
                    
                    htSubContractData.put(SubContractFundingSourceBean.class, new CoeusVector());
                    htSubContractData.put(SubContractAmountInfoBean.class, new CoeusVector());
                    htSubContractData.put(SubcontractCloseoutBean.class, new CoeusVector());
                    htSubContractData.put(SubcontractContactDetailsBean.class, new CoeusVector());
                    htSubContractData.put(SubContractCustomDataBean.class, cvSubContractCustomData == null ? new CoeusVector() : cvSubContractCustomData);
                    htSubContractData.put(KeyConstants.SUBCONTRACT_COST_TYPES, subContractTxnBean.getCostTypes());
                    htSubContractData.put(KeyConstants.SUBCONTRACT_REPORT_TYPES, subContractTxnBean.getReportTypes());
                    htSubContractData.put(KeyConstants.SUBCONTRACT_CONTACT_TYPES,
                            subContractTxnBean.getContactTypesForModule(ModuleConstants.SUBCONTRACTS_MODULE_CODE));
                    htSubContractData.put(KeyConstants.SUBCONTRACT_COPYRIGHT_TYPES, subContractTxnBean.getCopyRights());

                    htSubContractData.put(KeyConstants.AWARD_CONTACT_TYPE, cvContactTypes);
                    
                    htSubContractData.put(SubContractAmountReleased.class, new CoeusVector());
                    //Added for Case#2402 - use a parameter to set the length of the account number throughout app - Start
                    htSubContractData.put(CoeusParameterBean.class, cvParameters == null ? new CoeusVector() : cvParameters);
                    //Case#2402 - End
                    responder.setDataObject(htSubContractData);
                    responder.setMessage(null);
                    responder.setResponseStatus(true);
                    
                }
            }//End if get subcontract data
            
            if(functionType == DISPLAY_MODE) {
                SubContractBaseBean subContractBaseBean = (SubContractBaseBean)requester.getDataObject();
                String subContractCode = subContractBaseBean.getSubContractCode();
                
                SubContractTxnBean subContractTxnBean = new SubContractTxnBean();
                AwardLookUpDataTxnBean awardLookUpDataTxnBean = new AwardLookUpDataTxnBean();
                SubContractBean subContractBean = new SubContractBean();
                
                //Data For Detail Tab
                subContractBean = subContractTxnBean.getSubContract(subContractCode, GET_SUNCONTRACT_DATA);
                CoeusVector cvSubContractStatus = subContractTxnBean.getSubContractStatus();
                CoeusVector cvAwardType = awardLookUpDataTxnBean.getAwardType();
                
                //Data For Funding Source Tab
                CoeusVector cvSubContractFundingSource = null;
                if(subContractBean.getFundingSourceIndicator() != null && subContractBean.getFundingSourceIndicator().charAt(0) == 'P') {
                    cvSubContractFundingSource = subContractTxnBean.getSubContractAwards(subContractCode);
                }
                
                //Data for Amount Info Tab
                CoeusVector cvSubContractAmtInfo = subContractTxnBean.getSubContractAmountInfo(subContractCode);
                
                //Data for AmountReleased Tab
                CoeusVector cvSubContractAmtReleased = subContractTxnBean.getSubContractAmountReleased(subContractCode);
                
                // Data for the Subcontract closeout
                CoeusVector cvCloseOut = null;
                if(subContractBean.getCloseOutIndicator()!=null && subContractBean.getCloseOutIndicator().charAt(0)=='P'){
                    cvCloseOut = subContractTxnBean.getSubcontractCloseOut(subContractCode);
                }
                
                // Get data for the closeoutTypes
                CoeusVector cvCloseoutTypes = subContractTxnBean.getSubcontractCloseoutTypes();
                
                //Data for Contacts
                // Modified for COEUSQA-1412 Subcontract Module changes - Start
//                CoeusVector cvContactTypes = awardLookUpDataTxnBean.getContactTypes();
                CoeusVector cvContactTypes = awardLookUpDataTxnBean.getContactTypesForModule(ModuleConstants.SUBCONTRACTS_MODULE_CODE);
                // Modified for COEUSQA-1412 Subcontract Module changes - End

                
                // Get the data for the Subcontract contacts details - Added by chandra
                CoeusVector cvSubContacts = subContractTxnBean.getSubcontractContacts(subContractCode);
                // Get the data for Subcontract Custom Data
                CoeusVector cvSubContractCustomData = subContractTxnBean.getSubContractCustomData(subContractCode);
                
                //Added for Case#2402 - use a parameter to set the length of the account number throughout app - Start
                CoeusVector cvParameters = getParamaters();
                //Case#2402 - End
                
                // Addded for COEUSQA-1412 Subcontract Module changes - Change - Start
                CoeusVector cvReports = subContractTxnBean.getSubcontractReports(subContractCode);
                SubcontractTemplateInfoBean  subcontractTemplateInfoBean = subContractTxnBean.getSubcontractTemplateInfo(subContractCode);
                CoeusVector cvTemplateInfo = new CoeusVector();
                if(subcontractTemplateInfoBean != null){
                    cvTemplateInfo.add(subcontractTemplateInfoBean);
                }                
                // Addded for COEUSQA-1412 Subcontract Module changes - Change - End
                
                //putting all the details to a hashtable to send to the client side
                Hashtable htSubContractData = new Hashtable();
                htSubContractData.put(SubContractBean.class, subContractBean);
                htSubContractData.put(KeyConstants.SUBCONTRACT_STATUS, cvSubContractStatus);
                htSubContractData.put(KeyConstants.AWARD_TYPE, cvAwardType);
                htSubContractData.put(KeyConstants.CLOSEOUT_TYPES, cvCloseoutTypes);
                // Addded for COEUSQA-1412 Subcontract Module changes - Change - Start
                htSubContractData.put(KeyConstants.SUBCONTRACT_COST_TYPES, subContractTxnBean.getCostTypes());
                htSubContractData.put(KeyConstants.SUBCONTRACT_REPORT_TYPES, subContractTxnBean.getReportTypes());
                // Added for COEUSQA-1412 Subcontract Module changes - Start
                htSubContractData.put(KeyConstants.SUBCONTRACT_CONTACT_TYPES,
                        subContractTxnBean.getContactTypesForModule(ModuleConstants.SUBCONTRACTS_MODULE_CODE));
                // Added for COEUSQA-1412 Subcontract Module changes - End
                htSubContractData.put(KeyConstants.SUBCONTRACT_COPYRIGHT_TYPES, subContractTxnBean.getCopyRights());
                htSubContractData.put(SubcontractReportBean.class, cvReports == null ? new CoeusVector() : cvReports);
                htSubContractData.put(SubcontractTemplateInfoBean.class, cvTemplateInfo);
                // Addded for COEUSQA-1412 Subcontract Module changes - Change - End

                htSubContractData.put(SubContractFundingSourceBean.class, cvSubContractFundingSource == null ? new CoeusVector() : cvSubContractFundingSource);
                htSubContractData.put(SubContractAmountInfoBean.class, cvSubContractAmtInfo == null ? new CoeusVector() : cvSubContractAmtInfo);
                htSubContractData.put(SubcontractCloseoutBean.class, cvCloseOut == null ? new CoeusVector() : cvCloseOut);
                htSubContractData.put(SubcontractContactDetailsBean.class, cvSubContacts == null ? new CoeusVector() : cvSubContacts);
                
                
                htSubContractData.put(SubContractAmountReleased.class, cvSubContractAmtReleased == null ? new CoeusVector() : cvSubContractAmtReleased);
                
                htSubContractData.put(KeyConstants.AWARD_CONTACT_TYPE, cvContactTypes);
                
                htSubContractData.put(SubContractCustomDataBean.class,cvSubContractCustomData == null ? new CoeusVector() : cvSubContractCustomData);
				
                //Added for Case#2402 - use a parameter to set the length of the account number throughout app - Start
                htSubContractData.put(CoeusParameterBean.class, cvParameters == null ? new CoeusVector() : cvParameters);
                //Case#2402 - End

                subContractTxnBean.transactionCommit();
                responder.setDataObject(htSubContractData);
                responder.setMessage(null);
                responder.setResponseStatus(true);
            }
            else if(functionType == GET_UNIT_NAME) {
                SubContractTxnBean subContractTxnBean = new SubContractTxnBean();
                
                String unitNum = (String)requester.getDataObject();
                
                String unitName = subContractTxnBean.getUnitName(unitNum);
                
                responder.setDataObject(unitName);
                responder.setMessage(null);
                responder.setResponseStatus(true);
                
            }//End if get unit name
            else if(functionType == GET_ORG_NAME) {
                SubContractTxnBean subContractTxnBean = new SubContractTxnBean();
                
                String organizationId = (String)requester.getDataObject();
                
                String organizationName = subContractTxnBean.getOrganizationName(organizationId);
                
                responder.setDataObject(organizationName);
                responder.setMessage(null);
                responder.setResponseStatus(true);
            }//End if  get Organization name
            // To get the new Subcontract Code
            //            else if(functionType==GET_NEW_SUBCONTRACT){
            //                SubContractTxnBean subContractTxnBean = new SubContractTxnBean();
            //                Hashtable htSubContractData = new Hashtable();
            //                SubContractBean subContractBean = new SubContractBean();
            //                AwardLookUpDataTxnBean awardLookUpDataTxnBean = new AwardLookUpDataTxnBean();
            //
            //                String newSubcontractNumber = subContractTxnBean.getNewSubcontract();
            //                subContractBean.setSubContractCode(newSubcontractNumber);
            //
            //                CoeusVector cvSubContractStatus = subContractTxnBean.getSubContractStatus();
            //                CoeusVector cvAwardType = awardLookUpDataTxnBean.getAwardType();
            //                CoeusVector cvCloseoutTypes = subContractTxnBean.getSubcontractCloseoutTypes();
            //                //Data for Contacts
            //                CoeusVector cvContactTypes = awardLookUpDataTxnBean.getContactTypes();
            //
            //                htSubContractData.put(SubContractBean.class, subContractBean);
            //                htSubContractData.put(KeyConstants.SUBCONTRACT_STATUS, cvSubContractStatus);
            //                htSubContractData.put(KeyConstants.AWARD_TYPE, cvAwardType);
            //                htSubContractData.put(KeyConstants.CLOSEOUT_TYPES, cvCloseoutTypes);
            //                htSubContractData.put(KeyConstants.AWARD_CONTACT_TYPE, cvContactTypes);
            //
            //                responder.setDataObject(htSubContractData);
            //                responder.setMessage(null);
            //                responder.setResponseStatus(true);
            //            }
            else if (functionType == GET_SUBCONTRACT_AWARD_INFO) {
                String mitAwardNumber = (String)requester.getDataObject();
                SubContractTxnBean subContractTxnBean = new SubContractTxnBean();
                SubContractFundingSourceBean subContractFundingSourceBean =
                subContractTxnBean.getSubContractAwardInfo(mitAwardNumber);
                responder.setDataObject(subContractFundingSourceBean);
                responder.setMessage(null);
                responder.setResponseStatus(true);
            }  else if (functionType == SAVE_DATA) {
                Hashtable htData = (Hashtable)requester.getDataObject();
                SubContractTxnBean subContractTxnBean = new SubContractTxnBean(loggedinUser);
                SubContractBean subContractBeanLockCheck = (SubContractBean)htData.get(SubContractBean.class);
                
                boolean success = false;
                if(subContractBeanLockCheck != null && subContractBeanLockCheck.getAcType().equals("I")){
                    //modified for Princeton enhancement case#2802
                    //success = subContractTxnBean.updateSubContract(htData);
                    success = subContractTxnBean.updateSubContract(htData, path);
                    /* After saving the new SubContract code lock the subContract code
                     */
                    if(success){
                        LockingBean lockingBean = subContractTxnBean.getLockForNewSubContract(subContractBeanLockCheck.getSubContractCode(), loggedinUser, unitNumber);
                        try{
                            boolean lockCheck = lockingBean.isGotLock();
                            if(lockCheck){
                                subContractTxnBean.transactionCommit();
                                // 2930: Auto-delete Current Locks based on new parameter
                                // Send the LockingBean back to the Client Side
                                responder.setLockingBean(lockingBean);
                            }else{
                                subContractTxnBean.transactionRollback();
                            }
                        }catch(DBException dbEx){
//                            dbEx.printStackTrace();
                            UtilFactory.log(dbEx.getMessage(), dbEx, "SubContractMaintenanceServlet","doPost");
                            subContractTxnBean.transactionRollback();
                        }finally{
                            subContractTxnBean.endConnection();
                        }
                    }
                }else{
                    String subContCode = subContractBeanLockCheck.getSubContractCode();
                    boolean lockCheck = subContractTxnBean.lockCheck(subContCode, loggedinUser);
                    if(!lockCheck){
                        //modified for Princeton enhancement case#2802
                        //success = subContractTxnBean.updateSubContract(htData);
                        success = subContractTxnBean.updateSubContract(htData, path);
                    }else{
                        //String msg = "The lock for subcontract "+subContractBeanLockCheck.getSubContractCode()+"  has been deleted by Administrator ";
                        CoeusMessageResourcesBean coeusMessageResourcesBean
                        =new CoeusMessageResourcesBean();
                        String msg = coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1012")+" "+subContractBeanLockCheck.getSubContractCode()+" "+coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1002");
                        throw new LockingException(msg);
                    }
                }
                
                // Retrieving the data after updating
                SubContractBean subContractBean = (SubContractBean)htData.get(SubContractBean.class);
                String subContractCode = subContractBean.getSubContractCode();
                
                AwardLookUpDataTxnBean awardLookUpDataTxnBean = new AwardLookUpDataTxnBean();
                
                //Data For Detail Tab
                subContractBean = subContractTxnBean.getSubContract(subContractCode,GET_SUNCONTRACT_DATA);
                CoeusVector cvSubContractStatus = subContractTxnBean.getSubContractStatus();
                CoeusVector cvAwardType = awardLookUpDataTxnBean.getAwardType();
                
                //Data For Funding Source Tab
                CoeusVector cvSubContractFundingSource = null;
                if(subContractBean.getFundingSourceIndicator() != null && subContractBean.getFundingSourceIndicator().charAt(0) == 'P') {
                    cvSubContractFundingSource = subContractTxnBean.getSubContractAwards(subContractCode);
                }
                
                //Data for Amount Info Tab
                CoeusVector cvSubContractAmtInfo = subContractTxnBean.getSubContractAmountInfo(subContractCode);
                
                //Data for AmountReleased Tab
                CoeusVector cvSubContractAmtReleased = subContractTxnBean.getSubContractAmountReleased(subContractCode);
                
                // Data for the Subcontract closeout
                CoeusVector cvCloseOut = null;
                if(subContractBean.getCloseOutIndicator()!=null && subContractBean.getCloseOutIndicator().charAt(0)=='P'){
                    cvCloseOut = subContractTxnBean.getSubcontractCloseOut(subContractCode);
                }
                
                // Get data for the closeoutTypes
                CoeusVector cvCloseoutTypes = subContractTxnBean.getSubcontractCloseoutTypes();
                
                //Data for Contacts
                // Modified for COEUSQA-1412 Subcontract Module changes - Start
//                CoeusVector cvContactTypes = awardLookUpDataTxnBean.getContactTypes();
                CoeusVector cvContactTypes = awardLookUpDataTxnBean.getContactTypesForModule(ModuleConstants.SUBCONTRACTS_MODULE_CODE);
                // Modified for COEUSQA-1412 Subcontract Module changes - End
                
                // Get the data for the Subcontract contacts details - Added by chandra
                CoeusVector cvSubContacts = subContractTxnBean.getSubcontractContacts(subContractCode);
                // Get the data for Subcontract Custom Data
                CoeusVector cvSubContractCustomData = subContractTxnBean.getCustomData(subContractCode);
                // Addded for COEUSQA-1412 Subcontract Module changes - Change - Start
                CoeusVector cvReports = subContractTxnBean.getSubcontractReports(subContractCode);
                SubcontractTemplateInfoBean  subcontractTemplateInfoBean = subContractTxnBean.getSubcontractTemplateInfo(subContractCode);
                CoeusVector cvTemplateInfo = new CoeusVector();
                if(subcontractTemplateInfoBean != null){
                    cvTemplateInfo.add(subcontractTemplateInfoBean);
                }                
                // Addded for COEUSQA-1412 Subcontract Module changes - Change - End

                //putting all the details to a hashtable to send to the client side
                Hashtable htSubContractData = new Hashtable();
                htSubContractData.put(SubContractBean.class, subContractBean);
                htSubContractData.put(KeyConstants.SUBCONTRACT_STATUS, cvSubContractStatus);
                htSubContractData.put(KeyConstants.AWARD_TYPE, cvAwardType);
                htSubContractData.put(KeyConstants.CLOSEOUT_TYPES, cvCloseoutTypes);
                // Addded for COEUSQA-1412 Subcontract Module changes - Change - Start
                htSubContractData.put(KeyConstants.SUBCONTRACT_COST_TYPES, subContractTxnBean.getCostTypes());
                htSubContractData.put(KeyConstants.SUBCONTRACT_REPORT_TYPES, subContractTxnBean.getReportTypes());
                htSubContractData.put(KeyConstants.SUBCONTRACT_CONTACT_TYPES,
                        subContractTxnBean.getContactTypesForModule(ModuleConstants.SUBCONTRACTS_MODULE_CODE));
                htSubContractData.put(KeyConstants.SUBCONTRACT_COPYRIGHT_TYPES, subContractTxnBean.getCopyRights());
                htSubContractData.put(SubcontractReportBean.class, cvReports == null ? new CoeusVector() : cvReports);
                htSubContractData.put(SubcontractTemplateInfoBean.class, cvTemplateInfo);
                // Addded for COEUSQA-1412 Subcontract Module changes - Change - End

                htSubContractData.put(SubContractFundingSourceBean.class, cvSubContractFundingSource == null ? new CoeusVector() : cvSubContractFundingSource);
                htSubContractData.put(SubContractAmountInfoBean.class, cvSubContractAmtInfo == null ? new CoeusVector() : cvSubContractAmtInfo);
                htSubContractData.put(SubcontractCloseoutBean.class, cvCloseOut == null ? new CoeusVector() : cvCloseOut);
                htSubContractData.put(SubcontractContactDetailsBean.class, cvSubContacts == null ? new CoeusVector() : cvSubContacts);
                
                htSubContractData.put(SubContractAmountReleased.class, cvSubContractAmtReleased == null ? new CoeusVector() : cvSubContractAmtReleased);
                
                htSubContractData.put(KeyConstants.AWARD_CONTACT_TYPE, cvContactTypes);
                
                htSubContractData.put(SubContractCustomDataBean.class,cvSubContractCustomData == null ? new CoeusVector() : cvSubContractCustomData);
                responder.setDataObject(htSubContractData);
                
                responder.setMessage(null);
                responder.setResponseStatus(true);
            }else if( functionType == RELEASE_LOCK){
                String subContractCode  = requester.getDataObject().toString().trim();
                SubContractTxnBean subContractTxnBean = new SubContractTxnBean();
                LockingBean lockingBean = subContractTxnBean.releaseLock(subContractCode,loggedinUser);
                responder.setLockingBean(lockingBean);
                responder.setResponseStatus(true);
                responder.setDataObject("updateLock connection released");
            }else if( functionType == SAVE_DATA_AND_RELEASE_LOCK){
                Hashtable htData = (Hashtable)requester.getDataObject();
                SubContractTxnBean subContractTxnBean = new SubContractTxnBean(loggedinUser);
                boolean success = false;
                SubContractBean subContractLockCheck = (SubContractBean)htData.get(SubContractBean.class);
                if(subContractLockCheck != null && subContractLockCheck.getAcType().equals("I")){
                    //modified for Princeton enhancement case#2802
                    //success = subContractTxnBean.updateSubContract(htData);                    
                    success = subContractTxnBean.updateSubContract(htData, path);
                }else{
                    String subContCode = subContractLockCheck.getSubContractCode();
                    boolean lockCheck = subContractTxnBean.lockCheck(subContCode, loggedinUser);
                    if(!lockCheck){
                        //modified for Princeton enhancement case#2802
                        //success = subContractTxnBean.updateSubContract(htData);                        
                        success = subContractTxnBean.updateSubContract(htData, path);
                    }else{
                        CoeusMessageResourcesBean coeusMessageResourcesBean
                        =new CoeusMessageResourcesBean();
                        String msg = coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1012")+" "+subContractLockCheck.getSubContractCode()+" "+coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1002");
                        throw new LockingException(msg);
                    }
                }
                if(success){
                    SubContractBean subContractBeanLockCheck = (SubContractBean)htData.get(SubContractBean.class);
                    boolean lockAvailCheck = subContractTxnBean.lockCheck(subContractBeanLockCheck.getSubContractCode(), loggedinUser);
                    //if(subContractBeanLockCheck != null && (!subContractBeanLockCheck.getAcType().equals("I"))){
                    if(!lockAvailCheck){
                        LockingBean lockingBean = subContractTxnBean.releaseLock(subContractBeanLockCheck.getSubContractCode(),loggedinUser);
                        responder.setLockingBean(lockingBean);
                    }
                    CoeusVector cvSubContractAmtInfo = subContractTxnBean.getSubContractAmountInfo(subContractBeanLockCheck.getSubContractCode());
                    
                    //Data for AmountReleased Tab
                    CoeusVector cvSubContractAmtReleased = subContractTxnBean.getSubContractAmountReleased(subContractBeanLockCheck.getSubContractCode());
                    Hashtable htSubContractData = new Hashtable();
                    htSubContractData.put(SubContractBean.class, subContractBeanLockCheck);
                    htSubContractData.put(SubContractAmountReleased.class, cvSubContractAmtReleased == null ? new CoeusVector() : cvSubContractAmtReleased);
                    htSubContractData.put(SubContractAmountInfoBean.class, cvSubContractAmtInfo == null ? new CoeusVector() : cvSubContractAmtInfo);
                    responder.setDataObject(htSubContractData);
                    responder.setResponseStatus(true);
                }else{
                    responder.setResponseStatus(false);
                    responder.setDataObject("Saving the subContract data failed");
                }
            } else if (functionType == GET_RTF_FORM_LIST) {
                SubContractTxnBean txnBean = new SubContractTxnBean();
                CoeusVector cvRTFList = txnBean.getRTFFormList();
                responder.setDataObject(cvRTFList);
                responder.setMessage(null);
                responder.setResponseStatus(true);
//            } else if (functionType == GET_RTF_FORM_VARIABLE_AND_PRINT) {
//                CoeusVector cvDataObject = (CoeusVector)requester.getDataObject();
//                String formId = (String)cvDataObject.get(0);
//                String subContractCode = (String)cvDataObject.get(1);
//		Hashtable variableValues = (Hashtable)cvDataObject.get(2);
//                Character printMode = (Character)cvDataObject.get(3);
//                char priMode = printMode.charValue();
//                SubContractTxnBean txnBean = new SubContractTxnBean(loggedinUser);
//                String rtfName = "";
//                ByteArrayInputStream rtfTemplate = txnBean.getRTFTemplate(formId);
//                //CoeusVector cvRTFFormVariable = txnBean.getVariableForRTFForm();
//                
//                CoeusVector userValues = txnBean.getCurrentUserValues(subContractCode);
//                if (variableValues != null) {
//                    variableValues.put("USER_VALUES", userValues);
//                }
//                SubcontractStream stream = new SubcontractStream();
//                ByteArrayOutputStream outputStream = stream.getStreamData(variableValues);
//                rtfName = generateSubContractToRTF(outputStream, formId,subContractCode,rtfTemplate,priMode);
//                responder.setDataObject(rtfName);
//                responder.setMessage(null);
//                responder.setResponseStatus(true);
            }else if(functionType == OSP_USER_RIGHT_CHECK_AND_SUB_REPORTS ) {
				boolean isSubcontractgoals = false;
				boolean isModifySubcontract = false;
				Vector rightData = new Vector();
                SubContractTxnBean txnBean = new SubContractTxnBean(loggedinUser);
                ProposalDevelopmentTxnBean proptxn = new ProposalDevelopmentTxnBean();
                // 3587: Multi Campus Enahncements- Start
//                int hasSubcontractingGoals = proptxn.getUserHasOSPRight(loggedinUser,MODIFY_SUBCONTRACTING_GOALS);          
//		  int hasModifySubcontract = proptxn.getUserHasOSPRight(loggedinUser,MODIFY_SUBCONTRACT);
                UserMaintDataTxnBean userMaintenanceDataTxnBean = new UserMaintDataTxnBean();
                isModifySubcontract = userMaintenanceDataTxnBean.getUserHasRightInAnyUnit(loggedinUser,MODIFY_SUBCONTRACT);
                isSubcontractgoals  = userMaintenanceDataTxnBean.getUserHasRightInAnyUnit(loggedinUser,MODIFY_SUBCONTRACTING_GOALS);	
//				if(hasSubcontractingGoals==1){
//					isSubcontractgoals = true;
//				}else{
//					isSubcontractgoals = false;
//				}
                
//				if(hasModifySubcontract==1){
//					isModifySubcontract = true;
//				}else{
//					isModifySubcontract = false;
//				}
//                // 3587: Multi Campus Enahncements - End
		rightData.add(0, new Boolean(isSubcontractgoals));
		rightData.add(1, new Boolean(isModifySubcontract));
                CoeusVector cvSubcontactingReports = txnBean.getSubContractingReports();
                Hashtable htRepData = new Hashtable();
                htRepData.put(CoeusVector.class, cvSubcontactingReports==null ? new CoeusVector() : cvSubcontactingReports);
                //htRepData.put(Boolean.class, new Boolean(hasRight==1?true:false));
				htRepData.put(Vector.class, rightData);
                responder.setDataObject(htRepData);
                responder.setMessage(null);
                responder.setResponseStatus(true);
            }else if(functionType == GET_SUBCONTRACTING_GOALS_AMTS ) {
                SubContractTxnBean txnBean = new SubContractTxnBean(loggedinUser);
                SubcontactingBudgetBean subcontactingBudgetBean = txnBean.getSubcontractingBudgetGoals((String)requester.getDataObject());
                SubcontactExpenditureBean subcontactExpenditureBean = txnBean.getSubcontractExpenditure((String)requester.getDataObject());
                Hashtable htData = new Hashtable();
                //Added with case 3587: Multi campus enhancement
                UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
                AwardTxnBean awardTxnBean = new AwardTxnBean();
                String leadUnit = awardTxnBean.getLeadUnitForAward((String)requester.getDataObject());
                boolean goalRight = userMaintDataTxnBean.getUserHasRight(loggedinUser,MODIFY_SUBCONTRACTING_GOALS,leadUnit);
                htData.put(MODIFY_SUBCONTRACTING_GOALS,new Boolean(goalRight));
                //3587 End
                htData.put(SubcontactingBudgetBean.class, subcontactingBudgetBean==null ? new SubcontactingBudgetBean() : subcontactingBudgetBean);
                htData.put(SubcontactExpenditureBean.class, subcontactExpenditureBean==null ? new SubcontactExpenditureBean() : subcontactExpenditureBean);
                responder.setDataObject(htData);
                responder.setMessage(null);
                responder.setResponseStatus(true);
            }else if(functionType == UPDATE_SUBCONTRACTING_GOALS ) {
                SubContractTxnBean txnBean = new SubContractTxnBean(loggedinUser);
                boolean committed = txnBean.updateSubContractingGoals((SubcontactingBudgetBean)requester.getDataObject());
                responder.setMessage(null);
                responder.setResponseStatus(committed);
            }else if(functionType == SUB_VALIDATION_CHECKS ) {
                SubContractTxnBean txnBean = new SubContractTxnBean(loggedinUser);
                CoeusVector cvMessages = txnBean.SubValidationChecks();
                responder.setDataObject(cvMessages);
                responder.setMessage(null);
                responder.setResponseStatus(true);
            }else if(functionType == GET_TEMPLATE ){
                SubContractTxnBean txnBean = new SubContractTxnBean(loggedinUser);
                String formId = requester.getDataObject().toString();
                byte[] template = txnBean.getRTFArrayTemplate(formId);
                responder.setDataObject(template);
                responder.setMessage(null);
                responder.setResponseStatus(true);
            }else if (functionType == GET_FISCAL_PERIOD ){
                SubContractTxnBean txnBean = new SubContractTxnBean();
                CoeusVector cvMessages = txnBean.getFiscalPeriod();
                responder.setDataObject(cvMessages);
                responder.setMessage(null);
                responder.setResponseStatus(true);
            }
            //Code added for princeton enhancement case#2802 - starts
            //To get the Amount information details for the given subcontractId
            else if(functionType == GET_AMOUNT_INFO){
                SubContractTxnBean txnBean = new SubContractTxnBean();
                String subcontractId = requester.getDataObject().toString();
                CoeusVector cvAmountInfo = txnBean.getSubContractAmountInfo(subcontractId);
                responder.setDataObject(cvAmountInfo);
                responder.setMessage(null);
                responder.setResponseStatus(true);
            }
            //To get the Amount information details for the given subcontractId
            else if(functionType == GET_INVOICE_DETAILS){
                SubContractTxnBean txnBean = new SubContractTxnBean();
                SubContractAmountReleased subContractAmountReleased =(SubContractAmountReleased) requester.getDataObject();
                subContractAmountReleased = txnBean.getInvoiceDetails(subContractAmountReleased);
                responder.setDataObject(subContractAmountReleased);
                responder.setMessage(null);
                responder.setResponseStatus(true);
            }            
            //Code added for princeton enhancement case#2802 - ends
            // 3587: Multi Campus Enahncements - Start
            else if(functionType == CHECK_USER_HAS_CREATE_RIGHT){
                String reqHomeUnit = (String) requester.getDataObject();
                boolean createRight = false;
                UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
                if( reqHomeUnit != null){
                    createRight = userMaintDataTxnBean.getUserHasRight(loggedinUser,CREATE_SUBCONTRACT,
                            reqHomeUnit);
                }
                responder.setDataObject(new Boolean(createRight));
                responder.setResponseStatus(true);
            } else if(functionType == CHECK_USER_HAS_MODIFY_RIGHT){
                String subContractCode = (String)requester.getDataObject();
                boolean modifyRight = true;
                UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
                SubContractTxnBean subContractTxnBean = new SubContractTxnBean();
                SubContractBean subContractBean = subContractTxnBean.getSubContract(subContractCode);
                if( subContractBean != null && subContractBean.getRequisitionerUnit() != null
                        && !"".equals(subContractBean.getRequisitionerUnit())){
                    modifyRight = userMaintDataTxnBean.getUserHasRight(loggedinUser,MODIFY_SUBCONTRACT,
                            subContractBean.getRequisitionerUnit());
                }
                responder.setDataObject(new Boolean(modifyRight));
                responder.setResponseStatus(true);
            } else if(functionType == CHECK_USER_HAS_GOAL_RIGHT){
                String subContractCode = (String)requester.getDataObject();
                UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
                AwardTxnBean awardTxnBean = new AwardTxnBean();
                String leadUnit = awardTxnBean.getLeadUnitForAward((String)requester.getDataObject());
                boolean goalRight = userMaintDataTxnBean.getUserHasRight(loggedinUser,MODIFY_SUBCONTRACTING_GOALS,leadUnit);
                responder.setDataObject(new Boolean(goalRight));
                responder.setResponseStatus(true);
            }
            // 3587: Multi Campus Enahncements - End
            // COEUSDEV-185	Proposal Development and Subawards are viewable from medusa without appropriate role - Start
            else if(functionType == CHECK_CAN_VIEW_SUB_CONTRACT){
                boolean isViewable = false;
                String subContractCode = (String)requester.getDataObject();
                
                UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
                SubContractTxnBean subContractTxnBean = new SubContractTxnBean();
                SubContractBean subContractBean = subContractTxnBean.getSubContract(subContractCode);
                if( subContractBean != null && subContractBean.getRequisitionerUnit() != null
                        && !"".equals(subContractBean.getRequisitionerUnit())){
                    isViewable = userMaintDataTxnBean.getUserHasRight(loggedinUser,VIEW_SUBCONTRACT,
                            subContractBean.getRequisitionerUnit());
                    if (! isViewable){
                        isViewable = userMaintDataTxnBean.getUserHasRight(loggedinUser,MODIFY_SUBCONTRACT,
                                subContractBean.getRequisitionerUnit());
                        if (! isViewable){
                            isViewable = userMaintDataTxnBean.getUserHasRight(loggedinUser,CREATE_SUBCONTRACT,
                                    subContractBean.getRequisitionerUnit());
                        }
                    }
                } else {
                    isViewable = true;
                }
                responder.setDataObject(new Boolean(isViewable));
                responder.setResponseStatus(true);
            }
            // COEUSDEV-185	Proposal Development and Subawards are viewable from medusa without appropriate role - End
            //COEUSQA:1412 - Post-Award - Boiler plate forms for Subcontracts - Start
            else if(functionType == GET_DOCUMENT_TYPE){
                 SubContractTxnBean subContractTxnBean = new SubContractTxnBean();
                 CoeusVector cvAttachType = subContractTxnBean.getSubcontractAttachmentTypes();
                 responder.setDataObject(cvAttachType == null ? new CoeusVector()
                 :cvAttachType);
                 responder.setResponseStatus(true);
                 responder.setMessage(null);
            }
            
            else if(functionType == ADD_UPD_DEL_SUBCONTRACT_ATTACH){
                 Vector vecServerData = (Vector)requester.getDataObjects();
                 SubContractAttachmentBean subContractAttachmentBean = (SubContractAttachmentBean)vecServerData.get(0);
                 //SubContractTxnBean subContractTxnBean = new SubContractTxnBean();
                 //sheduleUpdateTxnBean = new ScheduleMaintenanceUpdateTxnBean(loggedinUser);
                 SubContractTxnBean subContractTxnBean = new SubContractTxnBean(loggedinUser);
                 
                 responder.setResponseStatus(false);
                 if(subContractAttachmentBean != null){
                     boolean isSuccess =
                             subContractTxnBean.addUpdSubcontractAttachment(subContractAttachmentBean);
                     if(isSuccess){
                         responder.setResponseStatus(true);
                     }
                     
                 }
                 responder.setMessage(null);
            } else if(functionType == GET_SUBCONTRACT_UPLOAD_ATTACH){
                 SubContractBean subContractBean = (SubContractBean)requester.getDataObject();
                 SubContractTxnBean subContractTxnBean = new SubContractTxnBean(loggedinUser);
                 CoeusVector vecUpldData = subContractTxnBean.getSubcontractAttachments(subContractBean.getSubContractCode(), subContractBean.getSequenceNumber());
                 responder.setDataObject(vecUpldData == null?new CoeusVector():vecUpldData);
                 responder.setResponseStatus(true);
                 responder.setMessage(null);
            } else if(functionType == GET_SUBCONTRACT_TEMPLATE_TYPES){
                SubContractTxnBean subContractTxnBean = new SubContractTxnBean(loggedinUser);
                responder.setDataObject(subContractTxnBean.getSubcontractTemplateTypes());
                responder.setResponseStatus(true);
                responder.setMessage(null);
            } else if(functionType == GET_ATTACHMENTS_FOR_GENERATE_AGREEMENT){
                SubContractBean subContractBean = (SubContractBean)requester.getDataObject();
                SubContractTxnBean subContractTxnBean = new SubContractTxnBean(loggedinUser);
                CoeusVector vecUpldData = subContractTxnBean.getSubcontractAttachments(subContractBean.getSubContractCode(), subContractBean.getSequenceNumber());
                CoeusFunctions coeusFunctions = new CoeusFunctions(loggedinUser);
                String paramValue = coeusFunctions.getParameterValue("SUBCONTRACT_GEN_AGRMT_ATT_TYPE_INCLUSION");
                HashMap hmAttachments = new HashMap();
                hmAttachments.put("SUBCONTRACT_FORM_ATTACHMENTS",subContractTxnBean.getRTFFormList());
                hmAttachments.put("SUBCONTRACT_ATTACHMENTS",(vecUpldData == null?new CoeusVector():vecUpldData));
                hmAttachments.put("SUBCONTRACT_GEN_AGRMT_ATT_TYPE_INCLUSION",paramValue);
                responder.setDataObject(hmAttachments);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }
            //COEUSQA:1412 - End
            //Added for COEUS-58- Start
             else if(functionType == GET_DOCUMENT_RIGHTS) {
                boolean canViewsubContractDocument = false;
                boolean canModifySubContract = false;
                String subContractCode = (String)requester.getDataObject();
                Hashtable htDocumentRights = new Hashtable();
                UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
                SubContractTxnBean subContractTxnBean = new SubContractTxnBean();
                SubContractBean subContractBean = subContractTxnBean.getSubContract(subContractCode);
                String viewerUnit = subContractBean.getRequisitionerUnit();
                if( viewerUnit == null || viewerUnit.isEmpty()) {
                    viewerUnit = unitNumber;
                }
                canViewsubContractDocument = userMaintDataTxnBean.getUserHasRight(loggedinUser, VIEW_SUBCONTRACT_DOCUMENTS, viewerUnit);
                canModifySubContract = userMaintDataTxnBean.getUserHasRight(loggedinUser,MODIFY_SUBCONTRACT,viewerUnit);
                htDocumentRights.put("canViewAttachment", new Boolean(canViewsubContractDocument));
                htDocumentRights.put("canModifySubContract", new Boolean(canModifySubContract));

                responder.setDataObject(htDocumentRights);
                responder.setMessage(null);
                responder.setResponseStatus(true);
            }
            //Added for COEUS-58- End
            
        }catch( LockingException lockEx ) {
            //lockEx.printStackTrace();
//            LockingBean lockingBean = lockEx.getLockingBean();
            String errMsg = lockEx.getErrorMessage();
            CoeusMessageResourcesBean coeusMessageResourcesBean
            =new CoeusMessageResourcesBean();
            errMsg= coeusMessageResourcesBean.parseMessageKey(errMsg);
            responder.setResponseStatus(false);
            responder.setMessage(errMsg);
            UtilFactory.log( errMsg, lockEx,
            "SubcontractMaintenenceServlet", "doPost");
        }
        catch( CoeusException coeusEx ) {
            //coeusEx.printStackTrace();
//            int index=0;
            String errMsg;
            if(coeusEx.getErrorId()==999999) {
                errMsg = "dbEngine_intlErr_exceptionCode.1028";
                responder.setLocked(true);
            }else{
                errMsg = coeusEx.getMessage();
            }
            CoeusMessageResourcesBean coeusMessageResourcesBean
            =new CoeusMessageResourcesBean();
            errMsg= coeusMessageResourcesBean.parseMessageKey(errMsg);
            
            responder.setResponseStatus(false);
            
            responder.setMessage(errMsg);
            UtilFactory.log( errMsg, coeusEx,
            "SubcontractMaintenenceServlet", "doPost");
            
        }catch( DBException dbEx ) {
            //dbEx.printStackTrace();
//            int index=0;
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
            
            responder.setMessage(errMsg);
            UtilFactory.log( errMsg, dbEx,
            "SubcontractMaintenenceServlet", "doPost");
            
        }catch(Exception e) {
            //e.printStackTrace();
            responder.setResponseStatus(false);
            responder.setMessage(e.getMessage());
            UtilFactory.log( e.getMessage(), e,
            "SubcontractMaintenenceServlet", "doPost");
        //Case 3193 - START
        }catch(Throwable throwable){
            Exception ex = new Exception(throwable);
            responder.setException(ex);
            responder.setResponseStatus(false);
            responder.setMessage(ex.getMessage());
            UtilFactory.log( throwable.getMessage(), throwable, "SubcontractMaintenenceServlet", "doPost");
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
                "SubcontractMaintenenceServlet", "doPost");
            }
        }
    }

    //Added for Case#2402 - use a parameter to set the length of the account number throughout app - Start
    /**
     * Method to get parameters from OSP$PARAMETER table
     * @return cvParameters
     */
    private CoeusVector getParamaters() throws DBException ,Exception{
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        CoeusVector cvParameters = new CoeusVector();
        CoeusParameterBean coeusParameterBean = new CoeusParameterBean();
        coeusParameterBean.setParameterName(CoeusConstants.MAX_ACCOUNT_NUMBER_LENGTH);
        coeusParameterBean.setParameterValue(ParameterUtils.getMaxAccountNumberLength());
        cvParameters.addElement(coeusParameterBean);
        return cvParameters;
    }
    //Case#2402 - End

    /*public String generateSubContractToRTF(ByteArrayOutputStream byteArrayOutputStream,String formId,String subContractCode,ByteArrayInputStream rtfTemplate,char printFormatType)
    throws Exception {
        // setting the path to store the XSL files
        CoeusConstants.SERVER_HOME_PATH = this.getServletContext().getRealPath("/");
        ReportGenerator reportGenerator = new ReportGenerator();
        ByteArrayInputStream xmlStream;
        InputStream rtfTemplateStream = rtfTemplate;
        InputStream xslStream;
        String report = new String(byteArrayOutputStream.toByteArray());
        //reading from the XSL
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(report.getBytes());
        xmlStream = byteArrayInputStream;
        xmlStream.close();
        xslStream =null;
        if (printFormatType == RTF_MODE) {
           byteArrayOutputStream = reportGenerator.convertXML2FO(xmlStream, rtfTemplateStream); 
           byteArrayOutputStream.close();
        } else {
            byteArrayOutputStream = reportGenerator.convertXML2PDF(xmlStream, rtfTemplateStream); 
            byteArrayOutputStream.close();
            byteArrayOutputStream =  reportGenerator.createPDF(byteArrayOutputStream);
        }
        String reportPath = CoeusProperties.getProperty(CoeusPropertyKeys.REPORT_GENERATED_PATH); //get path (to generate RTF) from config
        String filePath = CoeusConstants.SERVER_HOME_PATH+File.separator+reportPath;
        File reportDir = new File(filePath);
        if(!reportDir.exists()){
            reportDir.mkdirs();
        }
        SimpleDateFormat dateFormat= new SimpleDateFormat("MMddyyyy-hhmmss");
        if (printFormatType == PDF_MODE) {
            
            File file = new File(filePath, formId+dateFormat.format(new java.util.Date())+".pdf");
            FileOutputStream fos = new FileOutputStream(file);
            
            fos.write(byteArrayOutputStream.toByteArray());
            fos.close();
            byteArrayOutputStream.close();
            String pdfUrl = "";
            pdfUrl = "/"+reportPath+"/"+file.getName();
            return pdfUrl;
        }
        
        //Modified to avoid writing fo file to the disk
        
        //String foName = reportDir.getAbsolutePath()+ File.separatorChar+formId+".fo";
        String rtfName = formId+"_"+dateFormat.format(new java.util.Date())+".rtf";
        String rtfFullName = reportDir.getAbsolutePath()+ File.separatorChar+rtfName;
        String rtfPathName = File.separator+reportPath+File.separator+rtfName;
        
        String s = System.getProperty("user.dir");
        JForLogConfig.configure();
        byte[] resultBytes = byteArrayOutputStream.toByteArray();
        ByteArrayInputStream byteInputStream = new ByteArrayInputStream(resultBytes);
        
//      InputSource inputsource = new InputSource(fileToUrl(foName));
        InputSource inputsource = new InputSource((InputStream)byteInputStream);
        BufferedWriter bufferedwriter = new BufferedWriter(new FileWriter(rtfFullName));
        //setUserDir((new File(foName)).getCanonicalFile().getParent());
        //creating the RTF file from the FO  using jFor.
        new Converter(inputsource, bufferedwriter, Converter.createConverterOption());
        setUserDir(s);
	bufferedwriter.close();
        return rtfPathName;
    }*/
    
//    private static String fileToUrl(String s)
//    throws MalformedURLException {
//        return toURL(new File(s)).toString();
//    }
    
//    private static void setUserDir(String s) {
//        if(s == null) {
//            //m_logger.warn("relative image path is not supported");
//            return;
//        } else {
//            Properties properties = System.getProperties();
//            properties.put("user.dir", s);
//            System.setProperties(properties);
//            return;
//        }
//    }
    
//    private static URL toURL(File file)
//    throws MalformedURLException {
//        String s = file.getAbsolutePath();
//        if(File.separatorChar != '/'){
//            s = s.replace(File.separatorChar, '/');
//        }
//        if(!s.startsWith("/")){
//            s = "/" + s;
//        }
//        if(!s.endsWith("/") && file.isDirectory()){
//            s = s + "/";
//        }
//        return new URL("file", "", s);
//    }
}
