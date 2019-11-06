/*
 * @(#)InstituteProposalMaintenanceServlet.java 1.0 April 26, 2004 12:55 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/* PMD check performed, and commented unused imports and
 * variables on 13-JULY-2011 by Bharati
 */


package edu.mit.coeus.servlet;

import edu.mit.coeus.award.bean.AwardLookUpDataTxnBean;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.bean.*;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.KeyConstants;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.instprop.bean.*;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentTxnBean;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.award.bean.AwardTxnBean;
import edu.mit.coeus.unit.bean.UnitDataTxnBean;
import edu.mit.coeus.unit.bean.UnitDetailFormBean;
import edu.mit.coeus.irb.bean.ProtocolDataTxnBean;
import edu.mit.coeus.award.bean.AwardFundingProposalBean;
//import edu.mit.coeus.propdev.bean.ProposalKeyPersonFormBean;
import edu.mit.coeus.utils.ParameterUtils;
import edu.mit.coeus.utils.locking.LockingBean;
import edu.mit.coeus.utils.locking.LockingException;
import edu.ucsd.coeus.personalization.Personalization;
import edu.vanderbilt.coeus.instprop.bean.ProposalApprovedSubcontractBean;

import javax.servlet.http.*;
import javax.servlet.*;
import java.io.*;
import java.util.Vector;
//import java.util.HashMap;
import java.util.Hashtable;
import edu.mit.coeus.utils.ModuleConstants;
/** This servlet is used for Institute Proposal Maintenance. 
 * All Institute Proposal related server calls are implemented here.
 *
 * @author Prasanna Kumar K.
 * @version :1.0 April 26, 2004 12:55 PM
 *
 */

public class InstituteProposalMaintenanceServlet extends CoeusBaseServlet implements TypeConstants{
    
//    private UtilFactory UtilFactory = new UtilFactory();
    
    private static final char GET_INSTITUTE_PROPOSAL_DATA = 'A';
    private static final char SAVE_INSTITUTE_PROPOSAL = 'B';
    private static final char GET_MASTER_DATA_FOR_NEW_MODE = 'C';
    private static final char VALIDATE_BEFORE_SAVE = 'D';
    private static final char GET_PROPOSAL_IP_REVIEW = 'E';
    private static final char MERGE_PROPOSAL_LOG = 'F';
    private static final char GET_NEW_PROPOSAL_LOG = 'G';
    private static final char GET_PROPOSAL_LOG = 'H';
    private static final char SAVE_PROPOSAL_LOG = 'I';
    private static final char CHECK_PROPOSAL_LOG_RIGHTS = 'J';
    private static final char CHECK_TEMP_LOG_EXIST = 'K';
    private static final char MERGE_AND_SAVE_PROPOSAL_LOG = 'L';
    private static final char UPDATE_IP_REVIEW = 'M';
    private static final char CHECK_INST_PROP_RIGHTS = 'N';
    private static final char RELEASE_LOCK = 'O';
    private static final char CHECK_NEGO_RIGHT = 'P'; 
    private static final char VALIDATE_PROP_NUM = 'V'; 
    private static final char GET_PROPOSAL_TITLE= 'T'; 
    
    //Case 2106 Start
    private static final char GET_INV_CREDIT_SPLIT_DATA = 'U';
    private static final char SAVE_INV_CREDIT_SPLIT_DATA = 'v';
    //Case 2106 End
    //Case #2136 start
    private static final char GET_PROP_UNIT_ADMIN_TYPE_DATA = 'Q';
    private static final char SAVE_PROP_UNIT_ADMIN_TYPE_DATA = 'W';
    //Code added for Case#3388 - Implementing authorization check at department level
    private static final char CAN_VIEW_INST_PROPOSAL = 'w';
    //Case #2136 start
    //Rights
    private static final String CREATE_PROPOSAL_LOG = "CREATE_PROPOSAL_LOG";
    private static final String CREATE_TEMPORARY_LOG = "CREATE_TEMPORARY_LOG";
    private static final String CREATE_INST_PROPOSAL = "CREATE_INST_PROPOSAL";
    private static final String MODIFY_INST_PROPOSAL = "MODIFY_INST_PROPOSAL";
    private static final String VIEW_INST_PROPOSAL = "VIEW_INST_PROPOSAL";
    private static final String MODIFY_PROPOSAL_LOG = "MODIFY_PROPOSAL_LOG";
    private static final String VIEW_PROPOSAL_LOG = "VIEW_PROPOSAL_LOG";    
    private static final String MAINTAIN_PROPOSAL_IP_REVIEW = "MAINTAIN_PROPOSAL_IP_REVIEW";
	private static final String VIEW_NEGOTIATIONS = "VIEW_NEGOTIATIONS";
	private static final String MODIFY_NEGOTIATIONS = "MODIFY_NEGOTIATIONS";
    //Code added for Case#3388 - Implementing authorization check at department level
    private static final String VIEW_INT_PROPOSAL_AT_UNIT = "VIEW_INT_PROPOSAL_AT_UNIT";
    //Institute Proposal Modes
    private static final char NEW_MODE = 'N';
    private static final char MODIFY_MODE = 'M';
    private static final char NEW_ENTRY_MODE = 'E';
    private static final char DISPLAY_MODE = 'D';
    //Added for Case#3587 - Multi Campus enchanment - Start
    private static final char USER_HAS_RIGHT_IN_UNIT_LEVEL = 'R';
    private static final String LEAD_UNIT_NUMBER = "LEAD_UNIT_NUMBER";
    private static final String RIGHT_ID = "RIGHT_ID";
    private static final String EMPTY_STRING = "";
    //Case#3587 - End
    private static final int PROPOSAL_FUNDED = 2;
    //Added for COEUSQA-1525 : Attachments for Institute Proposal - Start
    private static final char GET_ATTACHMENT_RIGHTS = 'a';
    private static final String PROPOSAL_NUMBER = "PROPOSAL_NUMBER";
    private static final String LEAD_UNIT = "LEAD_UNIT";
    private static final int CAN_USER_MAINTAIN_ATTACHMENT = 0;
    private static final int CAN_USER_VIEW_ATTACHMENT = 1;
    private static final String VIEW_INST_PROPOSAL_DOC = "VIEW_INST_PROPOSAL_DOC";
    private static final String MAINTAIN_INST_PROPOSAL_DOC = "MAINTAIN_INST_PROPOSAL_DOC";
    private static final char GET_ATTACHMENT_DATAS ='t';
    private static final char UPDATE_ATTACHMENT = 'u';
    private static final char RELEASE_ATTACHMENT_LOCK = 'r';
    //COEUSQA-1525 : End
    
    // JM 4-12-2012 added procedure to get IP centers data
    private static final char GET_IP_CENTERS = 'j';
    // JM 05-01-2013 added procedure to get IP subcontracts data
    private static final char GET_SUBCONTRACTS = 'k';
    private static final char UPDATE_SUBCONTRACTS = 'l';
    
    /**  This method is used for applets.
     *  Post the information into server using object serialization.
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws ServletException if any ServletException
     * @throws IOException if an y IOException
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        
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
            
            // keep all the beans into vector
//            Vector dataObjects = new Vector();
            
            
            char functionType = requester.getFunctionType();
            InstituteProposalBean instituteProposalBean = null;
            InstituteProposalTxnBean instituteProposalTxnBean = new InstituteProposalTxnBean();
            String proposalNumber="";
//            int sequenceNumber;
        //Coeus Enhancement case #1799 start    
            if(functionType == GET_PROPOSAL_TITLE){
             String  id = requester.getId();
             String title = instituteProposalTxnBean.getProposalTitle(id);
             responder.setDataObject(title);          
            }
            
            if(functionType == VALIDATE_PROP_NUM){
                 String  id = requester.getId();
                 int exist = instituteProposalTxnBean.validateProposalNumber(id);
                 responder.setDataObject(new Integer(exist));          
             }
            
        //Coeus Enhancement case #1799 end    
            
            /* Get Institute Proposal*/
            if(functionType == GET_INSTITUTE_PROPOSAL_DATA){
                Hashtable hshGetInstPropData = (Hashtable)requester.getDataObject();
                instituteProposalBean = (InstituteProposalBean)hshGetInstPropData.get(InstituteProposalBean.class);
                
                Hashtable institutePropData = null;
                //If Modify Mode
                if(instituteProposalBean.getMode() == MODIFY_MODE){
                    //Get Proposal Lock
                    // Commented by Shivakumar for locking enhancement
//                    boolean isAvailable = instituteProposalTxnBean.getInstituteProposalLock(instituteProposalBean.getProposalNumber());
                    // Code added by Shivakumar -BEGIN1
                    LockingBean lockingBean = instituteProposalTxnBean.getInstituteProposalLock(instituteProposalBean.getProposalNumber(),loggedinUser,unitNumber);
                    boolean isAvailable = lockingBean.isGotLock();
                    // Code added by Shivakumar -END1
                    if(isAvailable){
                        try{
                            institutePropData = getInstituteProposalData(instituteProposalBean.getProposalNumber(), instituteProposalBean.getMode());
                            instituteProposalTxnBean.transactionCommit();
                            responder.setLockingBean(lockingBean);
                        }catch(DBException dbEx){
                            // commented for using UtilFactory.log instead of printStackTrace
                            UtilFactory.log(dbEx.getMessage(),dbEx,"InstituteProposalMaintenanceServlet", "doPost");
//                            //dbEx.printStackTrace();
                            instituteProposalTxnBean.transactionRollback();
                            throw dbEx;
                        }finally{
                            instituteProposalTxnBean.endConnection();
                        }    
                    }
                    responder.setDataObject(institutePropData);
                    responder.setMessage(null);
                    responder.setResponseStatus(true);                    
                }else if(instituteProposalBean.getMode() == DISPLAY_MODE){
                    institutePropData = getInstituteProposalData(instituteProposalBean.getProposalNumber(), instituteProposalBean.getMode());
                    responder.setDataObject(institutePropData);
                    responder.setMessage(null);
                    responder.setResponseStatus(true);                        
                }else if(instituteProposalBean.getMode() == NEW_ENTRY_MODE){
                    //Get Proposal Lock
                    // Commented by Shivakumar for locking enhancement
//                    boolean isAvailable = instituteProposalTxnBean.getInstituteProposalLock(instituteProposalBean.getProposalNumber());
                    LockingBean lockingBean = instituteProposalTxnBean.getLock(instituteProposalBean.getProposalNumber(), loggedinUser, unitNumber);                    
                    boolean isAvailable = lockingBean.isGotLock();   
                    if(isAvailable){
                        try{
                            instituteProposalTxnBean.transactionCommit();      
                            institutePropData = getInstituteProposalData(instituteProposalBean.getProposalNumber(), instituteProposalBean.getMode());                    
                            responder.setLockingBean(lockingBean);
                            responder.setDataObject(institutePropData);
                            responder.setMessage(null);
                            responder.setResponseStatus(true);                        
                        }catch(DBException dbEx){
                            instituteProposalTxnBean.transactionRollback();
                            CoeusMessageResourcesBean coeusMessageResourcesBean
                                =new CoeusMessageResourcesBean();
                            String msg = lockingBean.getUserID()+" "+coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1000")+" "+instituteProposalBean.getProposalNumber(); 
                            //String msg = "Sorry, the user "+lockingBean.getUserID()+" is using proposal number  "+instituteProposalBean.getProposalNumber();
                            responder.setMessage(msg);
                            responder.setResponseStatus(false);
                        }finally{
                            instituteProposalTxnBean.endConnection();
                        }    
                    }else{
                        instituteProposalTxnBean.transactionRollback();
                        CoeusMessageResourcesBean coeusMessageResourcesBean
                                =new CoeusMessageResourcesBean();
                        String msg = lockingBean.getUserID()+" "+coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1000")+" "+instituteProposalBean.getProposalNumber();                         
                        //String msg = "Sorry, the user "+lockingBean.getUserID()+" is using proposal number  "+instituteProposalBean.getProposalNumber();
                        responder.setMessage(msg);
                        responder.setResponseStatus(false);                        
                    }
                }else if(instituteProposalBean.getMode() == NEW_MODE){
//                    LockingBean lockingBean = instituteProposalTxnBean.getInstituteProposalLock(instituteProposalBean.getProposalNumber(),loggedinUser,unitNumber);                    
                    //LockingBean lockingBean = instituteProposalTxnBean.getInstituteProposalLock(instituteProposalBean.getProposalNumber(), loggedinUser, unitNumber); 
                    LockingBean lockingBean = instituteProposalTxnBean.getLock(instituteProposalBean.getProposalNumber(), loggedinUser, unitNumber);                    
                    boolean isAvailable = lockingBean.isGotLock();   
                    if(isAvailable){
                        try{
                            instituteProposalTxnBean.transactionCommit();                    
                            institutePropData = getInstituteProposalDataForNewMode(instituteProposalBean.getProposalNumber());
                            responder.setLockingBean(lockingBean);                               
                            responder.setDataObject(institutePropData);
                            responder.setMessage(null);
                            responder.setResponseStatus(true);    
                        }catch(DBException dbEx){
                            instituteProposalTxnBean.transactionRollback();
                            CoeusMessageResourcesBean coeusMessageResourcesBean
                                =new CoeusMessageResourcesBean();
                            String msg = lockingBean.getUserID()+" "+coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1000")+" "+instituteProposalBean.getProposalNumber(); 
                            //String msg = "Sorry, the user "+lockingBean.getUserID()+" is using proposal number  "+instituteProposalBean.getProposalNumber();
                            responder.setMessage(msg);
                            responder.setResponseStatus(false);
                            throw dbEx;
                        }finally{
                            instituteProposalTxnBean.endConnection();
                        }    
                    }else{
                        instituteProposalTxnBean.transactionRollback();
                        CoeusMessageResourcesBean coeusMessageResourcesBean
                                =new CoeusMessageResourcesBean();
                        String msg = lockingBean.getUserID()+" "+coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1000")+" "+instituteProposalBean.getProposalNumber(); 
                        //String msg = "Sorry, the user "+lockingBean.getUserID()+" is using proposal number  "+instituteProposalBean.getProposalNumber();
                        responder.setMessage(msg);
                        responder.setResponseStatus(false);                        
                    }    
                }
            // JM 05-01-2013 added for IP subcontracts
	        }else if(functionType == GET_SUBCONTRACTS){
	        	proposalNumber = (String)requester.getDataObject();
	        	CoeusVector cvSubcontracts = instituteProposalTxnBean.getProposalSubcontracts(proposalNumber);
	            responder.setDataObject(cvSubcontracts);
	            responder.setResponseStatus(true);
	        }else if(functionType == UPDATE_SUBCONTRACTS){
	        	ProposalApprovedSubcontractBean bean = (ProposalApprovedSubcontractBean)requester.getDataObject();
	        	boolean success = instituteProposalTxnBean.updateProposalSubcontracts(bean);
	            responder.setResponseStatus(true);
            // END  
            // JM 04-12-2012 added for IP centers
	        }else if(functionType == GET_IP_CENTERS){
	        	proposalNumber = (String)requester.getDataObject();
	        	CoeusVector cvCenters = instituteProposalTxnBean.getProposalCenters(proposalNumber);
	            responder.setDataObject(cvCenters);
	            responder.setResponseStatus(true);
	        // END  
            }else if(functionType == SAVE_INSTITUTE_PROPOSAL){
                Hashtable institutePropData = (Hashtable)requester.getDataObject();
                instituteProposalBean = (InstituteProposalBean)((CoeusVector)institutePropData.get(InstituteProposalBean.class)).elementAt(0);
                InstituteProposalUpdateTxnBean instituteProposalUpdateTxnBean = new InstituteProposalUpdateTxnBean(loggedinUser);
                //Save the data       
                // For bug fixing while releasing lock - BEGIN
                if((instituteProposalBean.getAcType() != null) && (instituteProposalBean.getAcType().equals("I"))){
                    //Case Id #2002 - start
                    instituteProposalUpdateTxnBean.setUnitNumber(unitNumber);
                    //Case Id #2002 - End
                    boolean success = instituteProposalUpdateTxnBean.addUpdInstituteProposal(institutePropData);
                }else{
                    boolean lockCheck = instituteProposalTxnBean.lockCheck(instituteProposalBean.getProposalNumber(), loggedinUser);
                    if(!lockCheck){
                        //Added for the Coeus Enhancement case:#1799 start step:1
                        instituteProposalUpdateTxnBean.setUnitNumber(unitNumber);
                        //End Coeus Enhancement case:#1799 step:1
                        boolean success = instituteProposalUpdateTxnBean.addUpdInstituteProposal(institutePropData);
                    }else{
                        CoeusMessageResourcesBean coeusMessageResourcesBean
                                =new CoeusMessageResourcesBean();
                        //String msg = "Sorry,  the lock for proposal number "+instituteProposalBean.getProposalNumber()+"  has been deleted by DB Administrator ";
                        String msg = coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1006")+" "+instituteProposalBean.getProposalNumber()+" "+coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1002");
                        throw new LockingException(msg);
                    }    
                }    
                // For bug fixing while releasing lock - END
                // Commented by Shivakumar for locking bug fixing
//                boolean success = instituteProposalUpdateTxnBean.addUpdInstituteProposal(institutePropData);
                //Check whether to Release the Lock 
                boolean releaseLock = ((Boolean)institutePropData.get(CoeusConstants.IS_RELEASE_LOCK)) == null 
                        ? false : ((Boolean)institutePropData.get(CoeusConstants.IS_RELEASE_LOCK)).booleanValue();
                if(releaseLock){
                    //If release Lock
                    // Commented by Shivakumar locking enhancement
//                    instituteProposalTxnBean.releaseEdit(instituteProposalBean.getProposalNumber());
                    // Code added by Shivakumar -BEGIN
//                    instituteProposalTxnBean.releaseEdit(instituteProposalBean.getProposalNumber(),loggedinUser);
                    // Calling releaseLock method to fix the bug in locking system
                    LockingBean lockingBean = instituteProposalTxnBean.releaseLock(instituteProposalBean.getProposalNumber(),loggedinUser);
                    responder.setLockingBean(lockingBean);
                    // Code added by Shivakumar -END
                    responder.setMessage(null);
                    responder.setResponseStatus(true);
                }else{                    
                    //If retain Lock, get data after save
                    // Code  added by Shivakumar for locking enhancement - BEGIN 1    
//                    LockingBean lockingBean =  instituteProposalTxnBean.getLock(instituteProposalBean.getProposalNumber(),loggedinUser,unitNumber);                
//                    boolean isAvailable = lockingBean.isGotLock();    
//                    instituteProposalTxnBean.transactionCommit();                
//                    if(isAvailable){                     
//                         responder.setLockingBean(lockingBean);
//                    } 
                    // Code  added by Shivakumar for locking enhancement - END 1 
//                    institutePropData = getInstituteProposalData(instituteProposalBean.getProposalNumber(), instituteProposalBean.getMode());
                    institutePropData = getInstituteProposalData(instituteProposalBean.getProposalNumber(), MODIFY_MODE);
                    responder.setDataObject(institutePropData);
                    responder.setMessage(null);
                    responder.setResponseStatus(true);
                }
            }else if(functionType == VALIDATE_BEFORE_SAVE){
                instituteProposalBean = (InstituteProposalBean)requester.getDataObject();
                AwardTxnBean awardTxnBean = new AwardTxnBean();
                int validAwardNumber = 0;
                int validAccountNumber = 0;
                if(instituteProposalBean.getCurrentAwardNumber()!=null && !instituteProposalBean.getCurrentAwardNumber().equals("")){
                    validAwardNumber = awardTxnBean.validateAwardNumber(instituteProposalBean.getCurrentAwardNumber());                    
                }
                //Modified for Case#2402 :use a parameter to set the length of the account number throughout app - Start
                //Account validation is deom only if the proposal status is funded
//                if(instituteProposalBean.getCurrentAccountNumber()!=null && !instituteProposalBean.getCurrentAccountNumber().equals("")){
                if(instituteProposalBean.getStatusCode() == PROPOSAL_FUNDED && instituteProposalBean.getCurrentAccountNumber()!=null && !instituteProposalBean.getCurrentAccountNumber().equals("")){//Case#2402 - End
                    validAccountNumber = awardTxnBean.checkValidAccountNumber(instituteProposalBean.getCurrentAccountNumber());
                }
                //Check all validations here before saving
                CoeusVector validationResult = new CoeusVector();
                if(validAwardNumber < 0){
                    responder.setMessage(null);
                    responder.setResponseStatus(false);
                    validationResult.addElement(new Boolean(false));
                    if(validAccountNumber < 0){
                        validationResult.addElement(new Boolean(false));
                    }else{
                        validationResult.addElement(new Boolean(true));
                    }
                    responder.setDataObject(validationResult);
                //Modified for Case#2402 :use a parameter to set the length of the account number throughout app - Start                    
//                }else if(validAccountNumber < 0){                    
                }else if(instituteProposalBean.getStatusCode() == 2 && validAccountNumber < 0){//Case#2402 - End

                    responder.setMessage(null);
                    responder.setResponseStatus(false);
                    validationResult.addElement(new Boolean(true));
                    validationResult.addElement(new Boolean(false));
                    responder.setDataObject(validationResult);                    
                }else{
                    responder.setResponseStatus(true);
                    responder.setDataObject(null);
                    responder.setMessage(null);
                }
            }else if(functionType == GET_PROPOSAL_IP_REVIEW){
                proposalNumber = (String)requester.getDataObject();
                Hashtable institutePropData = getProposalIPReviewData(proposalNumber);
                responder.setResponseStatus(true);
                responder.setDataObject(institutePropData);
                responder.setMessage(null);
            }else if(functionType == MERGE_PROPOSAL_LOG){
                CoeusVector cvDataObjects = (CoeusVector)requester.getDataObjects();
                String tempLogNumber = (String)cvDataObjects.elementAt(0);
                proposalNumber = (String)cvDataObjects.elementAt(1);
                InstituteProposalUpdateTxnBean instituteProposalUpdateTxnBean = new InstituteProposalUpdateTxnBean(loggedinUser);
                int success = instituteProposalUpdateTxnBean.mergeLog(tempLogNumber, proposalNumber);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }else if(functionType == GET_NEW_PROPOSAL_LOG){
                Hashtable institutePropData = getNewProposalLogData();
                responder.setDataObject(institutePropData);
                responder.setResponseStatus(true);
                responder.setMessage(null);                
            }else if(functionType == GET_PROPOSAL_LOG){
                proposalNumber = (String)requester.getDataObject();
                Hashtable institutePropData = getProposalLogData(proposalNumber);
                responder.setDataObject(institutePropData);
                responder.setResponseStatus(true);
                responder.setMessage(null);                
            }else if(functionType == SAVE_PROPOSAL_LOG){
                InstituteProposalLogBean instituteProposalLogBean = (InstituteProposalLogBean)requester.getDataObject();
                InstituteProposalUpdateTxnBean instituteProposalUpdateTxnBean = new InstituteProposalUpdateTxnBean(loggedinUser);
                boolean success = instituteProposalUpdateTxnBean.updateProposalLog(instituteProposalLogBean);
                instituteProposalLogBean = instituteProposalTxnBean.getInstituteProposalLog(instituteProposalLogBean.getProposalNumber());
                responder.setDataObject(instituteProposalLogBean);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }else if(functionType == CHECK_PROPOSAL_LOG_RIGHTS){
                UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
                CoeusVector cvAuthRights = new CoeusVector();     
                //Check all rights for Proposal Log
                //Modified for Case#3587 - multicampus enhancement  - Start                
//                boolean hasRight = userMaintDataTxnBean.getUserHasOSPRight(loggedinUser, CREATE_PROPOSAL_LOG);
//                cvAuthRights.addElement(new Boolean(hasRight));
//                hasRight = userMaintDataTxnBean.getUserHasOSPRight(loggedinUser, CREATE_TEMPORARY_LOG);
//                cvAuthRights.addElement(new Boolean(hasRight));
//                hasRight = userMaintDataTxnBean.getUserHasOSPRight(loggedinUser, CREATE_INST_PROPOSAL);
//                cvAuthRights.addElement(new Boolean(hasRight));
//                hasRight = userMaintDataTxnBean.getUserHasOSPRight(loggedinUser, MODIFY_INST_PROPOSAL);
//                cvAuthRights.addElement(new Boolean(hasRight));
//                hasRight = userMaintDataTxnBean.getUserHasOSPRight(loggedinUser, MODIFY_PROPOSAL_LOG);
//                cvAuthRights.addElement(new Boolean(hasRight));
                boolean hasRight = userMaintDataTxnBean.getUserHasRightInAnyUnit(loggedinUser, CREATE_PROPOSAL_LOG);
                cvAuthRights.addElement(new Boolean(hasRight));
                hasRight = userMaintDataTxnBean.getUserHasRightInAnyUnit(loggedinUser, CREATE_TEMPORARY_LOG);
                cvAuthRights.addElement(new Boolean(hasRight));
                hasRight = userMaintDataTxnBean.getUserHasRightInAnyUnit(loggedinUser, CREATE_INST_PROPOSAL);
                cvAuthRights.addElement(new Boolean(hasRight));
                hasRight = userMaintDataTxnBean.getUserHasRightInAnyUnit(loggedinUser, MODIFY_INST_PROPOSAL);
                cvAuthRights.addElement(new Boolean(hasRight));
                hasRight = userMaintDataTxnBean.getUserHasRightInAnyUnit(loggedinUser, MODIFY_PROPOSAL_LOG);
                cvAuthRights.addElement(new Boolean(hasRight));
                //Case#3587 - End
                
                hasRight = userMaintDataTxnBean.getUserHasRightInAnyUnit(loggedinUser, VIEW_PROPOSAL_LOG);
                cvAuthRights.addElement(new Boolean(hasRight));
                
                responder.setDataObject(cvAuthRights);
                responder.setResponseStatus(true);
                responder.setMessage(null);                                
            }else if(functionType == GET_MASTER_DATA_FOR_NEW_MODE){
                CoeusVector cvInstPropData = getMasterDataForNewMode();
                
                responder.setDataObject(cvInstPropData);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }else if(functionType == CHECK_TEMP_LOG_EXIST){
                InstituteProposalUpdateTxnBean instituteProposalUpdateTxnBean = new InstituteProposalUpdateTxnBean(loggedinUser);
                InstituteProposalLogBean instituteProposalLogBean = (InstituteProposalLogBean)requester.getDataObject();
                boolean updated = false;
                CoeusVector cvData = new CoeusVector();
                if(instituteProposalLogBean.getAcType()!=null){
                    if(instituteProposalLogBean.getAcType().equalsIgnoreCase("I")){
                        if(instituteProposalLogBean.getLogType()!='T'){
                            int count = instituteProposalTxnBean.checkTempLogs(instituteProposalLogBean.getPrincipleInvestigatorId());
                            CoeusVector cvTempLogList = new CoeusVector();
                            //If there are Temp Logs get all logs and send it to client
                            if(count > 0){
                                cvTempLogList = instituteProposalTxnBean.getInstituteProposalTempLog(instituteProposalLogBean.getPrincipleInvestigatorId());
                                cvData.addElement(new Boolean(true));
                                cvData.addElement(cvTempLogList);
                            }else{
                                updated = instituteProposalUpdateTxnBean.updateProposalLog(instituteProposalLogBean);
                                instituteProposalLogBean = instituteProposalTxnBean.getInstituteProposalLog(instituteProposalLogBean.getProposalNumber());
                                cvData.addElement(new Boolean(false));
                                cvData.addElement(instituteProposalLogBean);
                            }
                        }else{
                            //If Temp Log Insert
                            updated = instituteProposalUpdateTxnBean.updateProposalLog(instituteProposalLogBean);
                            instituteProposalLogBean = instituteProposalTxnBean.getInstituteProposalLog(instituteProposalLogBean.getProposalNumber());
                            cvData.addElement(new Boolean(false));
                            cvData.addElement(instituteProposalLogBean);                            
                        }
                    }else if(instituteProposalLogBean.getAcType().equalsIgnoreCase("U")){
                        updated = instituteProposalUpdateTxnBean.updateProposalLog(instituteProposalLogBean);
                        instituteProposalLogBean = instituteProposalTxnBean.getInstituteProposalLog(instituteProposalLogBean.getProposalNumber());
                        cvData.addElement(new Boolean(false));
                        cvData.addElement(instituteProposalLogBean);                        
                    }
                    responder.setDataObject(cvData);
                    responder.setResponseStatus(true);
                    responder.setMessage(null);
                }
            }else if(functionType == MERGE_AND_SAVE_PROPOSAL_LOG){
                InstituteProposalUpdateTxnBean instituteProposalUpdateTxnBean = new InstituteProposalUpdateTxnBean(loggedinUser);
                CoeusVector cvLogData = (CoeusVector)requester.getDataObject();
                String tempLogNumber = (String)cvLogData.elementAt(0);
                InstituteProposalLogBean instituteProposalLogBean = (InstituteProposalLogBean)cvLogData.elementAt(1);
                boolean success = instituteProposalUpdateTxnBean.saveAndMergeProposalLog(instituteProposalLogBean, tempLogNumber);
                instituteProposalLogBean = instituteProposalTxnBean.getInstituteProposalLog(instituteProposalLogBean.getProposalNumber());
                responder.setDataObject(instituteProposalLogBean);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }else if(functionType == UPDATE_IP_REVIEW){
                Hashtable ipReviewData = (Hashtable)requester.getDataObject();
                InstituteProposalUpdateTxnBean instituteProposalUpdateTxnBean = new InstituteProposalUpdateTxnBean(loggedinUser);
                boolean success = instituteProposalUpdateTxnBean.updateIPReview(ipReviewData);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }else if(functionType == CHECK_INST_PROP_RIGHTS){
                
                CoeusVector cvAuthRights = new CoeusVector();
                UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
                
                //Modified for Case#3587 - Multi Campus enchancement - Start
//                boolean hasRight = userMaintDataTxnBean.getUserHasOSPRight(loggedinUser, CREATE_INST_PROPOSAL);
                  boolean hasRight = userMaintDataTxnBean.getUserHasRightInAnyUnit(loggedinUser, CREATE_INST_PROPOSAL);
                cvAuthRights.addElement(new Boolean(hasRight));
//                hasRight = userMaintDataTxnBean.getUserHasOSPRight(loggedinUser, MODIFY_INST_PROPOSAL);
                  hasRight = userMaintDataTxnBean.getUserHasRightInAnyUnit(loggedinUser, MODIFY_INST_PROPOSAL);
                cvAuthRights.addElement(new Boolean(hasRight));
                
                //Commented for Case#3587 - End
                hasRight = userMaintDataTxnBean.getUserHasRightInAnyUnit(loggedinUser, VIEW_INST_PROPOSAL);
                cvAuthRights.addElement(new Boolean(hasRight));
                //Modified for Case#3587 - Multi Campus enchancement - Start
//                hasRight = userMaintDataTxnBean.getUserHasOSPRight(loggedinUser, MAINTAIN_PROPOSAL_IP_REVIEW);
                hasRight = userMaintDataTxnBean.getUserHasRightInAnyUnit(loggedinUser, MAINTAIN_PROPOSAL_IP_REVIEW);
                //Case#3587 - End
                
                cvAuthRights.addElement(new Boolean(hasRight));
              
                
                
                responder.setDataObject(cvAuthRights);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }else if(functionType == RELEASE_LOCK){
                proposalNumber = (String)requester.getDataObject();
                // Commented by Shivakumar for locking enhancement
//                instituteProposalTxnBean.releaseEdit(proposalNumber);
                // Code added by Shivakumar -BEGIN
//                instituteProposalTxnBean.releaseEdit(proposalNumber,loggedinUser);
                // Calling the releaseLock method to fix the bug in locking system
                LockingBean lockingBean = instituteProposalTxnBean.releaseLock(proposalNumber,loggedinUser);
                responder.setLockingBean(lockingBean);
                // Code added by Shivakumar -END
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }else if(functionType==CHECK_NEGO_RIGHT){
                proposalNumber = (String)requester.getDataObject();
                UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
                boolean hasViewRight = false;
                boolean hasNegoRight = true;
                boolean mode = false;
                // 3587: Multi Campus Enahncements - Start
//                hasNegoRight = userMaintDataTxnBean.getUserHasOSPRight(loggedinUser,MODIFY_NEGOTIATIONS);
                instituteProposalTxnBean = new InstituteProposalTxnBean();
                String leadunit = instituteProposalTxnBean.getLeadUnitForInstProposal(proposalNumber);
                hasNegoRight = userMaintDataTxnBean.getUserHasRight(loggedinUser, MODIFY_NEGOTIATIONS, leadunit);
                // 3587: Multi Campus Enahncements - End
                //Modified for COEUSDEV-191 : Cannot view negotiation in a unit inspite of having rights - Start
//                hasViewRight = userMaintDataTxnBean.getUserHasRight(loggedinUser, VIEW_NEGOTIATIONS, unitNumber);
                hasViewRight = userMaintDataTxnBean.getUserHasRight(loggedinUser, VIEW_NEGOTIATIONS, leadunit);
                //COEUSDEV-191 : END
                String piUserId = instituteProposalTxnBean.getPIUserID(proposalNumber);
                if (piUserId != null && !("").equals(piUserId)) {
                    if(loggedinUser.equals(piUserId)) {
                        mode = false;
                    }else{
                        mode = true;
                    }
                }
                CoeusVector vecRights = new CoeusVector();
                vecRights.addElement(new Boolean(hasNegoRight));
                vecRights.addElement(new Boolean(hasViewRight));
                vecRights.addElement(new Boolean(mode));
                responder.setDataObject(vecRights);
                responder.setResponseStatus(true);
                responder.setMessage(null);
			}
            
            //case 2106 Start
            else if(functionType == GET_INV_CREDIT_SPLIT_DATA){
                CoeusVector cvData = new CoeusVector();
                String proposalNo = (String)requester.getDataObject();
                
                instituteProposalTxnBean = new InstituteProposalTxnBean();
                ProposalDevelopmentTxnBean proposalDevelopmentTxnBean = new ProposalDevelopmentTxnBean();
                
                CoeusVector cvInvCreditTypes = proposalDevelopmentTxnBean.getInvCreditTypes();
                cvData.add(cvInvCreditTypes == null ? new CoeusVector() : cvInvCreditTypes);
                
                CoeusVector cvPerCreditSplit = instituteProposalTxnBean.getInstPropPerCreditSplit(proposalNo);
                cvData.add(cvPerCreditSplit == null ? new CoeusVector() : cvPerCreditSplit);
                
                
                CoeusVector cvUnitCreditSplit = instituteProposalTxnBean.getInstPropUnitCreditSplit(proposalNo);
                cvData.add(cvUnitCreditSplit == null ? new CoeusVector() : cvUnitCreditSplit);
                
                responder.setDataObject(cvData);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }else if(functionType == SAVE_INV_CREDIT_SPLIT_DATA){
                CoeusVector cvData = (CoeusVector)requester.getDataObject();
                
                InstituteProposalUpdateTxnBean ipUpdateTxnBean = new InstituteProposalUpdateTxnBean(loggedinUser);
                ipUpdateTxnBean.addUpdCreditSplit(cvData);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }
            //case 2106 End
            //case #2136 Start
            else if(functionType == GET_PROP_UNIT_ADMIN_TYPE_DATA){
                CoeusVector cvData = new CoeusVector();
                String proposalNo = (String)requester.getDataObject();
                
                instituteProposalTxnBean = new InstituteProposalTxnBean();
                
                UnitDataTxnBean unitTxn = new UnitDataTxnBean(loggedinUser);
                CoeusVector cvAdminTypeCode = unitTxn.getAdminTypeCode();
                cvData.add(cvAdminTypeCode == null ? new CoeusVector() : cvAdminTypeCode);
                
                CoeusVector cvUnitAdmin = instituteProposalTxnBean.getInstPropUnitAdmin(proposalNo);
                cvData.add(cvUnitAdmin == null ? new CoeusVector() : cvUnitAdmin);
                
                responder.setDataObject(cvData);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }else if(functionType == SAVE_PROP_UNIT_ADMIN_TYPE_DATA){
                Vector vecData = (Vector)requester.getDataObjects();
                InstituteProposalUpdateTxnBean ipUpdateTxnBean = new InstituteProposalUpdateTxnBean(loggedinUser);
                ipUpdateTxnBean.addUpdInstPropAdmin(vecData);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }
            
           //case #2136 Enhancement end
            //Code added for Case#3388 - Implementing authorization check at department level - starts
            //check to view the institute proposal
            else if(functionType == CAN_VIEW_INST_PROPOSAL){
                String instProposalNumber = (String) requester.getDataObject();
                UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
                //Modified for Case#3587 - multicampus enhancement  - Start
//                boolean hasRight = userMaintDataTxnBean.getUserHasOSPRight(loggedinUser, MODIFY_INST_PROPOSAL);
                String leadUnitNo = instituteProposalTxnBean.getLeadUnitForInstProposal(instProposalNumber);
                 boolean hasRight = userMaintDataTxnBean.getUserHasRight(loggedinUser, MODIFY_INST_PROPOSAL,leadUnitNo);
                //Case#3587 - End
                if(!hasRight){
                    hasRight = userMaintDataTxnBean.getUserHasRightInAnyUnit(loggedinUser, VIEW_INST_PROPOSAL);
                    if(!hasRight){
                        instituteProposalTxnBean = new InstituteProposalTxnBean();
//                        String leadUnitNo = instituteProposalTxnBean.getLeadUnitForInstProposal(instProposalNumber);
                        hasRight = userMaintDataTxnBean.getUserHasRight(loggedinUser, VIEW_INT_PROPOSAL_AT_UNIT, leadUnitNo);
                    }
                }
                responder.setDataObject(new Boolean(hasRight));
                responder.setResponseStatus(true);
                responder.setMessage(null);                
            }
            //Added for Case#3587 - multicampus enhancement  - Start
            else if(functionType == USER_HAS_RIGHT_IN_UNIT_LEVEL){
                Hashtable htRightCheck = (Hashtable) requester.getDataObject();
                String leadUnitNumber = EMPTY_STRING;
                String rightId = EMPTY_STRING;
                if(htRightCheck != null && htRightCheck.size() > 0){
                    leadUnitNumber = (String)htRightCheck.get(LEAD_UNIT_NUMBER);
                    rightId = (String)htRightCheck.get(RIGHT_ID);
                }
                UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
                boolean hasRight = userMaintDataTxnBean.getUserHasRight(loggedinUser,rightId,leadUnitNumber);
                responder.setDataObject(new Boolean(hasRight));
                responder.setResponseStatus(true);
                responder.setMessage(null);                
            }//Case#3587 - End
            //Code added for Case#3388 - Implementing authorization check at department level - ends

            //Added for COEUSQA-1525 : Attachments for Institute Proposal - Start
            else if(functionType == GET_ATTACHMENT_RIGHTS){
                proposalNumber = (String) requester.getDataObject();
                String leadUnitNumber = instituteProposalTxnBean.getLeadUnitForInstProposal(proposalNumber);
                UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
                boolean canUserViewAttachment = false;
                //Check user can maintain(Creating new attachment, modify the attachment) the proposal attachment
                boolean canUserMaintainAttachment =  userMaintDataTxnBean.getUserHasRight(
                        loggedinUser, MAINTAIN_INST_PROPOSAL_DOC,leadUnitNumber);
                if(!canUserMaintainAttachment){
                      canUserViewAttachment = userMaintDataTxnBean.getUserHasRight(loggedinUser, VIEW_INST_PROPOSAL_DOC, leadUnitNumber);
                }
                //Gets the attachment lock when user can maintain the attachment
                if(canUserMaintainAttachment){
                    instituteProposalTxnBean.getAttachmentLock(proposalNumber,loggedinUser,unitNumber);
                }
                Hashtable htAttacmentRights = new Hashtable();
                htAttacmentRights.put(CAN_USER_MAINTAIN_ATTACHMENT,new Boolean(canUserMaintainAttachment));
                htAttacmentRights.put(CAN_USER_VIEW_ATTACHMENT,new Boolean(canUserViewAttachment));
                responder.setDataObject(htAttacmentRights);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }else if(functionType == GET_ATTACHMENT_DATAS){
                Vector vecProposalDetail = requester.getDataObjects();
                proposalNumber = (String)vecProposalDetail.get(0);
                int sequenceNumber = ((Integer)vecProposalDetail.get(1)).intValue();
                
                Vector vecAttachmentDetails = new Vector();
                vecAttachmentDetails.add(0,instituteProposalTxnBean.getProposalAttachmentTypes());
                vecAttachmentDetails.add(1,instituteProposalTxnBean.getProposalAttachments(proposalNumber,sequenceNumber));
                responder.setDataObjects(vecAttachmentDetails);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }else if(functionType == UPDATE_ATTACHMENT){
                InstituteProposalAttachmentBean attachmentBean = (InstituteProposalAttachmentBean)requester.getDataObject();
                // COEUSQA - 1525 - Start
                int sequenceNumber = -1;
                // While Deleting a attachment Proposal current sequence is passed to fetch the attachment.
                // For upadte and insert actual acttachment sequence number is passed.
                if(TypeConstants.DELETE_RECORD.equals(attachmentBean.getAcType())){
                    sequenceNumber = ((Integer)requester.getDataObjects().get(0)).intValue();
                }else{
                    // the actual sequence number is set in the attachmentBean in case of the deleted sequence
                    sequenceNumber = attachmentBean.getSequenceNumber();
                }
                // COEUSQA - 1525 - End
                boolean lockCheck = instituteProposalTxnBean.attachmentLockCheck(
                        attachmentBean.getProposalNumber(), loggedinUser);
                if(!lockCheck){
                    InstituteProposalUpdateTxnBean updateTxnBean = new InstituteProposalUpdateTxnBean(loggedinUser);
                    updateTxnBean.addUpdProposalAttachment(attachmentBean);
                    Vector vecAttachmentDetails = new Vector();
                    vecAttachmentDetails.add(0,instituteProposalTxnBean.getProposalAttachmentTypes());
                    // COEUSQA - 1525 - Start
                    vecAttachmentDetails.add(1,
                            instituteProposalTxnBean.getProposalAttachments(
                            attachmentBean.getProposalNumber(),sequenceNumber));
                    // COEUSQA - 1525 - End
                    responder.setDataObjects(vecAttachmentDetails);
                    responder.setResponseStatus(true);
                    responder.setMessage(null);
                }else{
                    CoeusMessageResourcesBean coeusMessageResourcesBean
                            =new CoeusMessageResourcesBean();
                    String msg = coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1006")+" "+
                            attachmentBean.getProposalNumber()+" "+
                            coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1002");
                    throw new LockingException(msg);
                }
               
            }else if(functionType == RELEASE_ATTACHMENT_LOCK){
                proposalNumber =  (String)requester.getDataObject();
                instituteProposalTxnBean.releaseAttachmentLock(proposalNumber, loggedinUser);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }
            //COEUSQA-1525 : End
            //UCSD - rdias coeus personalization Begin
            //Intercept the responderbean for including form security. info
            setFormAccessXML(requester,responder);            
            //UCSD - rdias coeus personalization End            
        }catch( LockingException lockEx ) {
               // commented for using UtilFactory.log instead of printStackTrace
               //UtilFactory.log(lockEx.getMessage(),lockEx,"InstituteProposalMaintenanceServlet", "doPost");
//               //lockEx.printStackTrace();
               LockingBean lockingBean = lockEx.getLockingBean();
               String errMsg = lockEx.getErrorMessage();        
               CoeusMessageResourcesBean coeusMessageResourcesBean
                    =new CoeusMessageResourcesBean();
                errMsg= coeusMessageResourcesBean.parseMessageKey(errMsg);            
                responder.setException(lockEx);
                responder.setResponseStatus(false);            
                responder.setMessage(errMsg);               
                UtilFactory.log( errMsg, lockEx,
                "InstituteProposalMaintenanceServlet", "doPost");
        }catch( CoeusException coeusEx ) {
            // commented for using UtilFactory.log instead of printStackTrace
               //UtilFactory.log(coeusEx.getMessage(),coeusEx,"InstituteProposalMaintenanceServlet", "doPost");
//            coeusEx.printStackTrace();
//            int index=0;
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
            responder.setException(coeusEx);
            responder.setResponseStatus(false);
            
            responder.setMessage(errMsg);
            UtilFactory.log( errMsg, coeusEx,
            "InstituteProposalMaintenanceServlet", "doPost");
            
        }catch( DBException dbEx ) {
            // commented for using UtilFactory.log instead of printStackTrace
               //UtilFactory.log(dbEx.getMessage(),dbEx,"InstituteProposalMaintenanceServlet", "doPost");
//            dbEx.printStackTrace();
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
            responder.setException(dbEx);
            responder.setResponseStatus(false);
            
            responder.setMessage(errMsg);
            UtilFactory.log( errMsg, dbEx,
            "InstituteProposalMaintenanceServlet", "doPost");
            
        }catch(Exception e) {
            // commented for using UtilFactory.log instead of printStackTrace
               //UtilFactory.log(e.getMessage(),e,"InstituteProposalMaintenanceServlet", "doPost");
//            e.printStackTrace();
            responder.setException(e);
            responder.setResponseStatus(false);
            responder.setMessage(e.getMessage());
            UtilFactory.log( e.getMessage(), e,
            "InstituteProposalMaintenanceServlet", "doPost");
        //Case 3193 - START
        }catch(Throwable throwable){
            Exception ex = new Exception(throwable);
            responder.setException(ex);
            responder.setResponseStatus(false);
            responder.setMessage(ex.getMessage());
            UtilFactory.log( throwable.getMessage(), throwable, "InstituteProposalMaintenanceServlet", "doPost");
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
                "InstituteProposalMaintenanceServlet", "doPost");
            }
        }
    }
    
    /**
     *  This method is used to get all Institute Proposal Master Details
     *
     * @return Hashtable containing Master data
     *
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */    
    private Hashtable getInstituteProposalMasterData() 
            throws CoeusException, DBException {
        Hashtable instituteProposalData = new Hashtable();
        InstituteProposalLookUpDataTxnBean instituteProposalLookUpDataTxnBean = new InstituteProposalLookUpDataTxnBean();
        ProposalDevelopmentTxnBean proposalDevelopmentTxnBean = new ProposalDevelopmentTxnBean();
        CoeusVector coeusVector = null;
        Vector vctInstitutePropData = null;        
        
        //Institute Proposal Status
        coeusVector = instituteProposalLookUpDataTxnBean.getProposalStatus();        
        if(coeusVector==null){
            coeusVector = new CoeusVector();
        }
        instituteProposalData.put(KeyConstants.INSTITUTE_PROPOSAL_STATUS, coeusVector);
        
        //NSF Codes
        coeusVector = new CoeusVector();
        vctInstitutePropData = proposalDevelopmentTxnBean.getProposalNSFCodes();
        if(vctInstitutePropData==null){
            coeusVector = new CoeusVector();
        }else{
            coeusVector.addAll(vctInstitutePropData);
        }
        instituteProposalData.put(KeyConstants.NSF_CODE, coeusVector);
        
        //Activity Type
        coeusVector = new CoeusVector();
        vctInstitutePropData = proposalDevelopmentTxnBean.getProposalActivityTypes();
        if(vctInstitutePropData==null){
            coeusVector = new CoeusVector();
        }else{
            coeusVector.addAll(vctInstitutePropData);
        }
        instituteProposalData.put(KeyConstants.ACTIVITY_TYPE, coeusVector);
        
        //Proposal Type
        coeusVector = new CoeusVector();
        coeusVector = instituteProposalLookUpDataTxnBean.getProposalTypes();
        if(coeusVector==null){
            coeusVector = new CoeusVector();
        }
        instituteProposalData.put(KeyConstants.PROPOSAL_TYPE, coeusVector);
        
        //Notice of Oppurtunity
        coeusVector = new CoeusVector();
        vctInstitutePropData = proposalDevelopmentTxnBean.getProposalNoticeOpp();
        if(vctInstitutePropData==null){
            coeusVector = new CoeusVector();
        }else{
            coeusVector.addAll(vctInstitutePropData);
        }
        instituteProposalData.put(KeyConstants.NOTICE_OF_OPPORTUNITY, coeusVector);

       //IDC Rate Types
        coeusVector = new CoeusVector();
        coeusVector = instituteProposalLookUpDataTxnBean.getIDCRateType();
        if(coeusVector==null){
            coeusVector = new CoeusVector();
        }
        instituteProposalData.put(KeyConstants.IDC_RATE_TYPES, coeusVector);
        
        //Cost Sharing Types
        coeusVector = new CoeusVector();
        coeusVector = instituteProposalLookUpDataTxnBean.getCostSharingType();
        if(coeusVector==null){
            coeusVector = new CoeusVector();
        }
        instituteProposalData.put(KeyConstants.COST_SHARING_TYPES, coeusVector);
        
        //IP Review Requirement Type
        coeusVector = new CoeusVector();
        coeusVector = instituteProposalLookUpDataTxnBean.getIPReviewRequirementType();
        if(coeusVector==null){
            coeusVector = new CoeusVector();
        }
        instituteProposalData.put(KeyConstants.IP_REVIEW_REQUIREMENT_TYPE, coeusVector);
        
        //IP Review Result Type
        coeusVector = new CoeusVector();
        coeusVector = instituteProposalLookUpDataTxnBean.getIPReviewResultType();
        if(coeusVector==null){
            coeusVector = new CoeusVector();
        }
        instituteProposalData.put(KeyConstants.IP_REVIEW_RESULT_TYPE, coeusVector);        
        
        //IP Review Activity Type
        coeusVector = new CoeusVector();
        coeusVector = instituteProposalLookUpDataTxnBean.getIPReviewActivityType();
        if(coeusVector==null){
            coeusVector = new CoeusVector();
        }
        instituteProposalData.put(KeyConstants.IP_REVIEW_ACTIVITY_TYPE, coeusVector);                
        
        //Special Review Type
        coeusVector = new CoeusVector();
        ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean();
        // COEUSQA-2320 Show in Lite for Special Review in Code table
		// vctInstitutePropData = protocolDataTxnBean.getSpecialReviewCode();
        vctInstitutePropData = protocolDataTxnBean.getSpecialReviewTypesForModule(ModuleConstants.PROPOSAL_INSTITUTE_MODULE_CODE);
        if(vctInstitutePropData!=null){
            coeusVector.addAll(vctInstitutePropData);
        }
        instituteProposalData.put(KeyConstants.SPECIAL_REVIEW_TYPE, coeusVector);
        
        //Special Review Approval Type
        coeusVector = new CoeusVector();        
        vctInstitutePropData = protocolDataTxnBean.getReviewApprovalType();
        if(vctInstitutePropData!=null){
            coeusVector.addAll(vctInstitutePropData);
        }
        instituteProposalData.put(KeyConstants.SPECIAL_REVIEW_APPROVAL_TYPE, coeusVector);
        
        //Special Review List
        coeusVector = new CoeusVector();
        vctInstitutePropData = protocolDataTxnBean.getValidSpecialReviewList();
        if(vctInstitutePropData!=null){
            coeusVector.addAll(vctInstitutePropData);
        }
        instituteProposalData.put(SRApprovalInfoBean.class, coeusVector);        
        
        // Added for Case 2162  - adding Award Type - Start
        AwardLookUpDataTxnBean AwardTxnBean = new AwardLookUpDataTxnBean();
        CoeusVector cvAwrdType = (CoeusVector)AwardTxnBean.getAwardType();
        edu.mit.coeus.utils.ComboBoxBean comboBoxBean = new edu.mit.coeus.utils.ComboBoxBean();
        comboBoxBean.setCode("");
        comboBoxBean.setDescription("");
        cvAwrdType.add(0, comboBoxBean);
        instituteProposalData.put(KeyConstants.AWARD_TYPE, cvAwrdType);
        // Added for Case 2162  - adding Award Type - Start
        return instituteProposalData;
    }
    
    /**
     *  This method is used to get Institute Proposal Details in Modify/Display Mode
     *
     * @return Hashtable containing Institute Proposal Details
     * @param proposalNumber String
     * @param mode char
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    private Hashtable getInstituteProposalData(String proposalNumber, char mode) 
        throws CoeusException, DBException {
        
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        
        //Get all Master data required
        Hashtable instituteProposalData = getInstituteProposalMasterData();        
        CoeusVector cvInstPropData = null;
        
        InstituteProposalTxnBean instituteProposalTxnBean = new InstituteProposalTxnBean();
        //CoeusVector coeusVector = null;
        InstituteProposalBean instituteProposalBean = null;
        
        //Get Institute Proposal Details        
        instituteProposalBean = instituteProposalTxnBean.getInstituteProposalDetails(proposalNumber);
        
        //If NEW_ENTRY increment sequence number by 1
        if(mode==NEW_ENTRY_MODE){
            instituteProposalBean.setSequenceNumber(instituteProposalBean.getSequenceNumber()+1);
            instituteProposalBean.setAcType("I");
            /* Added by Shivakumar for bug fixing, bug id 1351 */
            instituteProposalBean.setMode(mode);
            // End Shivakumar
        }
        
        cvInstPropData = new CoeusVector();
        cvInstPropData.addElement(instituteProposalBean);
        instituteProposalData.put(InstituteProposalBean.class, cvInstPropData);        
        
        //Get Funding Awards
        cvInstPropData = null;
        cvInstPropData = instituteProposalTxnBean.getAwardsFundingForProposal(instituteProposalBean.getProposalNumber());
        if(cvInstPropData==null){
            cvInstPropData = new CoeusVector();
        }
        instituteProposalData.put(AwardFundingProposalBean.class, cvInstPropData);
                
        //Get IDC Rated
        cvInstPropData = null;
        if(instituteProposalBean.getIdcRateIndicator()!=null && instituteProposalBean.getIdcRateIndicator().charAt(0)=='P'){
            cvInstPropData = instituteProposalTxnBean.getInstituteProposalIDCRate(proposalNumber);
        }
        if(cvInstPropData==null){
            cvInstPropData = new CoeusVector();
        }
        instituteProposalData.put(InstituteProposalIDCRateBean.class, cvInstPropData);
        
        //Get Science Code
        cvInstPropData = null;
        if(instituteProposalBean.getScienceCodeIndicator()!=null && instituteProposalBean.getScienceCodeIndicator().charAt(0)=='P'){
            cvInstPropData = instituteProposalTxnBean.getInstituteProposalScienceCode(proposalNumber);
        }
        if(cvInstPropData==null){
            cvInstPropData = new CoeusVector();
        }
        instituteProposalData.put(InstituteProposalScienceCodeBean.class, cvInstPropData);        
        
        //Get Cost Sharing data
        cvInstPropData = null;
        if(instituteProposalBean.getCostSharingIndicator()!=null && instituteProposalBean.getCostSharingIndicator().charAt(0)=='P'){
            cvInstPropData = instituteProposalTxnBean.getInstituteProposalCostSharing(proposalNumber);
        }
        if(cvInstPropData==null){
            cvInstPropData = new CoeusVector();
        }
        instituteProposalData.put(InstituteProposalCostSharingBean.class, cvInstPropData);
        
        //Get IP Review Activity
        cvInstPropData = null;
        if(instituteProposalBean.getIpReviewActivityIndicator()!=null && instituteProposalBean.getIpReviewActivityIndicator().charAt(0)=='P'){
            cvInstPropData = instituteProposalTxnBean.getInstituteProposalIPReviewActivity(proposalNumber);
        }
        if(cvInstPropData==null){
            cvInstPropData = new CoeusVector();
        }
        instituteProposalData.put(InstituteProposalIPReviewActivityBean.class, cvInstPropData);        
        
        //Get Investigator
        cvInstPropData = null;
        //cvInstPropData = instituteProposalTxnBean.getInstituteProposalInvestigators(proposalNumber);        
        cvInstPropData = instituteProposalBean.getInvestigators();
        if(cvInstPropData==null){
            cvInstPropData = new CoeusVector();
        }
        instituteProposalData.put(InstituteProposalInvestigatorBean.class, cvInstPropData);        
        
        // 3823: Key Person Records Needed in Inst Proposal and Award - Start
        cvInstPropData = null;
        // This sequence number is used for fetching key person data. DO NOT USE this tmpSequnceNumber getting or updating
        // any other data.
        int tmpSequnceNumber = instituteProposalBean.getSequenceNumber();
        if(mode==NEW_ENTRY_MODE){
            tmpSequnceNumber --;
        }
        cvInstPropData = instituteProposalTxnBean.getInstituteProposalKeyPersons(proposalNumber, tmpSequnceNumber);
        if(cvInstPropData==null){
            cvInstPropData = new CoeusVector();
        }
        instituteProposalData.put(InstituteProposalKeyPersonBean.class, cvInstPropData);
        // 3823: Key Person Records Needed in Inst Proposal and Award - End

        //Get Special Review Data
        cvInstPropData = null;
        if(instituteProposalBean.getSpecialReviewIndicator()!=null && instituteProposalBean.getSpecialReviewIndicator().charAt(0)=='P'){            
            cvInstPropData = instituteProposalTxnBean.getInstituteProposalSpecialReview(proposalNumber);        
        }
        if(cvInstPropData==null){
            cvInstPropData = new CoeusVector();
        }
        instituteProposalData.put(InstituteProposalSpecialReviewBean.class, cvInstPropData);                
        
        //Get Custome Elements Data
        cvInstPropData = null;
        //if(instituteProposalBean.getSpecialReviewIndicator()!=null && instituteProposalBean.getSpecialReviewIndicator().charAt(0)=='P'){            
        cvInstPropData = instituteProposalTxnBean.getCustomData(proposalNumber);
        //}
        if(cvInstPropData==null){
            cvInstPropData = new CoeusVector();
        }
        instituteProposalData.put(InstituteProposalCustomDataBean.class, cvInstPropData);
        
        //Get all Comment Codes required
        CoeusVector cvParameters = new CoeusVector();
        //holds all comments for all tabs
        CoeusVector cvAllComments = new CoeusVector();
        
        //PROPOSAL_COMMENT_CODE
        CoeusParameterBean coeusParameterBean = new CoeusParameterBean();
        coeusParameterBean.setParameterName(CoeusConstants.PROPOSAL_COMMENT_CODE);
        coeusParameterBean.setParameterValue(coeusFunctions.getParameterValue(coeusParameterBean.getParameterName()));        
        //Get Comments for this comment code
        if(coeusParameterBean.getParameterValue() != null && !coeusParameterBean.getParameterValue().equalsIgnoreCase("")){
            cvParameters.addElement(coeusParameterBean);
            cvInstPropData = new CoeusVector();
            cvInstPropData = instituteProposalTxnBean.getInstituteProposalComments(proposalNumber, Integer.parseInt(coeusParameterBean.getParameterValue()));
            if(cvInstPropData!=null){
                cvAllComments.addAll(cvInstPropData);
            }
        }
        
        //INDIRECT_COST_COMMENT_CODE - IDC
        coeusParameterBean = new CoeusParameterBean();
        coeusParameterBean.setParameterName(CoeusConstants.INDIRECT_COST_COMMENT_CODE);
        coeusParameterBean.setParameterValue(coeusFunctions.getParameterValue(coeusParameterBean.getParameterName()));        
        //Get Comments for this comment code
        if(coeusParameterBean.getParameterValue() != null && !coeusParameterBean.getParameterValue().equalsIgnoreCase("")){
            cvParameters.addElement(coeusParameterBean);
            cvInstPropData = new CoeusVector();
            cvInstPropData = instituteProposalTxnBean.getInstituteProposalComments(proposalNumber, Integer.parseInt(coeusParameterBean.getParameterValue()));
            if(cvInstPropData!=null){
                cvAllComments.addAll(cvInstPropData);
            }
        }
        
        //PROPOSAL_SUMMARY_COMMENT_CODE
        coeusParameterBean = new CoeusParameterBean();
        coeusParameterBean.setParameterName(CoeusConstants.PROPOSAL_SUMMARY_COMMENT_CODE);
        coeusParameterBean.setParameterValue(coeusFunctions.getParameterValue(coeusParameterBean.getParameterName()));
        //Get Comments for this comment code
        if(coeusParameterBean.getParameterValue() != null && !coeusParameterBean.getParameterValue().equalsIgnoreCase("")){
            cvParameters.addElement(coeusParameterBean);
            cvInstPropData = new CoeusVector();
            cvInstPropData = instituteProposalTxnBean.getInstituteProposalComments(proposalNumber, Integer.parseInt(coeusParameterBean.getParameterValue()));
            if(cvInstPropData!=null){
                cvAllComments.addAll(cvInstPropData);
            }
        }
        
        //COST_SHARING_COMMENT_CODE
        coeusParameterBean = new CoeusParameterBean();
        coeusParameterBean.setParameterName(CoeusConstants.COST_SHARING_COMMENT_CODE);
        coeusParameterBean.setParameterValue(coeusFunctions.getParameterValue(coeusParameterBean.getParameterName()));
        //Get Comments for this comment code
        if(coeusParameterBean.getParameterValue() != null && !coeusParameterBean.getParameterValue().equalsIgnoreCase("")){
            cvParameters.addElement(coeusParameterBean);
            cvInstPropData = new CoeusVector();
            cvInstPropData = instituteProposalTxnBean.getInstituteProposalComments(proposalNumber, Integer.parseInt(coeusParameterBean.getParameterValue()));
            if(cvInstPropData!=null){
                cvAllComments.addAll(cvInstPropData);
            }
        }
        
        //PROPOSAL_IP_REVIEW_COMMENT_CODE
        coeusParameterBean = new CoeusParameterBean();
        coeusParameterBean.setParameterName(CoeusConstants.PROPOSAL_IP_REVIEW_COMMENT_CODE);
        coeusParameterBean.setParameterValue(coeusFunctions.getParameterValue(coeusParameterBean.getParameterName()));
        //Get Comments for this comment code
        if(coeusParameterBean.getParameterValue() != null && !coeusParameterBean.getParameterValue().equalsIgnoreCase("")){
            cvParameters.addElement(coeusParameterBean);
            cvInstPropData = new CoeusVector();
            cvInstPropData = instituteProposalTxnBean.getInstituteProposalComments(proposalNumber, Integer.parseInt(coeusParameterBean.getParameterValue()));
            if(cvInstPropData!=null){
                cvAllComments.addAll(cvInstPropData);
            }
        }
        
        //PROPOSAL_IP_REVIEWER_COMMENT_CODE
        coeusParameterBean = new CoeusParameterBean();
        coeusParameterBean.setParameterName(CoeusConstants.PROPOSAL_IP_REVIEWER_COMMENT_CODE);
        coeusParameterBean.setParameterValue(coeusFunctions.getParameterValue(coeusParameterBean.getParameterName()));
        //Get Comments for this comment code
        if(coeusParameterBean.getParameterValue() != null && !coeusParameterBean.getParameterValue().equalsIgnoreCase("")){
            cvParameters.addElement(coeusParameterBean);
            cvInstPropData = new CoeusVector();
            cvInstPropData = instituteProposalTxnBean.getInstituteProposalComments(proposalNumber, Integer.parseInt(coeusParameterBean.getParameterValue()));
            if(cvInstPropData!=null){
                cvAllComments.addAll(cvInstPropData);
            }
        }
        //Added for Coeus Enhancement Case #1799 - start step:2
        coeusParameterBean = new CoeusParameterBean();
        coeusParameterBean.setParameterName(CoeusConstants.SPL_REV_TYPE_CODE_HUMAN);
        coeusParameterBean.setParameterValue(coeusFunctions.getParameterValue(coeusParameterBean.getParameterName()));
        if(coeusParameterBean.getParameterValue() != null && !coeusParameterBean.getParameterValue().equalsIgnoreCase("")){
            cvParameters.addElement(coeusParameterBean);
        }
        
        coeusParameterBean = new CoeusParameterBean();
        coeusParameterBean.setParameterName(CoeusConstants.ENABLE_PROTOCOL_TO_AWARD_LINK);
        coeusParameterBean.setParameterValue(coeusFunctions.getParameterValue(coeusParameterBean.getParameterName()));
        if(coeusParameterBean.getParameterValue() != null && !coeusParameterBean.getParameterValue().equalsIgnoreCase("")){
            cvParameters.addElement(coeusParameterBean);
        }
        
        coeusParameterBean = new CoeusParameterBean();
        coeusParameterBean.setParameterName(CoeusConstants.ENABLE_PROTOCOL_TO_PROPOSAL_LINK);
        coeusParameterBean.setParameterValue(coeusFunctions.getParameterValue(coeusParameterBean.getParameterName()));
        if(coeusParameterBean.getParameterValue() != null && !coeusParameterBean.getParameterValue().equalsIgnoreCase("")){
            cvParameters.addElement(coeusParameterBean);
        }
        
        coeusParameterBean = new CoeusParameterBean();
        coeusParameterBean.setParameterName(CoeusConstants.LINKED_TO_IRB_CODE);
        coeusParameterBean.setParameterValue(coeusFunctions.getParameterValue(coeusParameterBean.getParameterName()));
        if(coeusParameterBean.getParameterValue() != null && !coeusParameterBean.getParameterValue().equalsIgnoreCase("")){
            cvParameters.addElement(coeusParameterBean);
        }
        
        //Coeus Enhancement Case #1799 - end step:2
        
        //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - start
        //get IACUC_SPL_REV_TYPE_CODE parameter
        coeusParameterBean = new CoeusParameterBean();
        coeusParameterBean.setParameterName(CoeusConstants.IACUC_SPL_REV_TYPE_CODE);
        coeusParameterBean.setParameterValue(coeusFunctions.getParameterValue(coeusParameterBean.getParameterName()));
        if(coeusParameterBean.getParameterValue() != null && !coeusParameterBean.getParameterValue().equalsIgnoreCase("")){
            cvParameters.addElement(coeusParameterBean);
        }
        
        //get ENABLE_IACUC_PROTOCOL_TO_AWARD_LINK parameter
        coeusParameterBean = new CoeusParameterBean();
        coeusParameterBean.setParameterName(CoeusConstants.ENABLE_IACUC_PROTOCOL_TO_AWARD_LINK);
        coeusParameterBean.setParameterValue(coeusFunctions.getParameterValue(coeusParameterBean.getParameterName()));
        if(coeusParameterBean.getParameterValue() != null && !coeusParameterBean.getParameterValue().equalsIgnoreCase("")){
            cvParameters.addElement(coeusParameterBean);
        }
        
        //get ENABLE_IACUC_PROTOCOL_TO_PROPOSAL_LINK parameter
        coeusParameterBean = new CoeusParameterBean();
        coeusParameterBean.setParameterName(CoeusConstants.ENABLE_IACUC_PROTOCOL_TO_PROPOSAL_LINK);
        coeusParameterBean.setParameterValue(coeusFunctions.getParameterValue(coeusParameterBean.getParameterName()));
        if(coeusParameterBean.getParameterValue() != null && !coeusParameterBean.getParameterValue().equalsIgnoreCase("")){
            cvParameters.addElement(coeusParameterBean);
        }
        
        //get LINKED_TO_IACUC_CODE parameter
        coeusParameterBean = new CoeusParameterBean();
        coeusParameterBean.setParameterName(CoeusConstants.LINKED_TO_IACUC_CODE);
        coeusParameterBean.setParameterValue(coeusFunctions.getParameterValue(coeusParameterBean.getParameterName()));
        if(coeusParameterBean.getParameterValue() != null && !coeusParameterBean.getParameterValue().equalsIgnoreCase("")){
            cvParameters.addElement(coeusParameterBean);
        }
        //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - end
        
        //Added for Case#2402 - use a parameter to set the length of the account number throughout app - Start
        //To get the MAX_ACCOUNT_NUMBER_LIMIT parameter details
        coeusParameterBean = new CoeusParameterBean();
        coeusParameterBean.setParameterName(CoeusConstants.MAX_ACCOUNT_NUMBER_LENGTH);
        coeusParameterBean.setParameterValue(ParameterUtils.getMaxAccountNumberLength());
        cvParameters.addElement(coeusParameterBean);
        //Case#2402 - End
        
        //Add all comments to hashtable
        instituteProposalData.put(InstituteProposalCommentsBean.class, cvAllComments);
        //Add all comment code to hashtable
        instituteProposalData.put(CoeusParameterBean.class, cvParameters);
        
        return instituteProposalData;
    }
    
    /**
     *  This method is used to get Institute Proposal Details in New Mode
     *
     * @return Hashtable containing Institute Proposal Details
     * @param proposalNumber String
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    private Hashtable getInstituteProposalDataForNewMode(String proposalNumber) 
        throws CoeusException, DBException {
        
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        
         //Get all Master data required
        Hashtable instituteProposalData = getInstituteProposalMasterData();        
        InstituteProposalTxnBean instituteProposalTxnBean = new InstituteProposalTxnBean();
        InstituteProposalLogBean instituteProposalLogBean = instituteProposalTxnBean.getInstituteProposalLog(proposalNumber);
        UnitDataTxnBean unitDataTxnBean = new UnitDataTxnBean();
        
        CoeusVector cvInstPropData = new CoeusVector();
        cvInstPropData.addElement(instituteProposalLogBean);
        instituteProposalData.put(InstituteProposalLogBean.class, cvInstPropData);
        
        InstituteProposalBean instituteProposalBean = new InstituteProposalBean();
        //Added for the case # COEUSQA-1690 - Copy Notes and Deadlines from Proposal Log into IP -start
        // In order to copy the comments from Proposal Log to Institute proposal, set the values in InstituteProposalCommentsBean
        InstituteProposalCommentsBean instituteProposalCommentsBean  = new InstituteProposalCommentsBean();
        instituteProposalCommentsBean.setComments(instituteProposalLogBean.getComments() == null ? "":instituteProposalLogBean.getComments());
        instituteProposalCommentsBean.setProposalNumber(instituteProposalLogBean.getProposalNumber());
        instituteProposalCommentsBean.setSequenceNumber(1);
        
        cvInstPropData = new CoeusVector();
        cvInstPropData.addElement(instituteProposalCommentsBean);
        instituteProposalData.put(InstituteProposalCommentsBean.class, cvInstPropData);    
        
        instituteProposalBean.setDeadLineDate(instituteProposalLogBean.getDeadlineDate());
        //Added for the case # COEUSQA-1690 - Copy Notes and Deadlines from Proposal Log into IP -end
        instituteProposalBean.setProposalNumber(instituteProposalLogBean.getProposalNumber());        
        instituteProposalBean.setSequenceNumber(1);
        instituteProposalBean.setAcType("I");
        instituteProposalBean.setStatusCode(1);
        instituteProposalBean.setProposalTypeCode(instituteProposalLogBean.getProposalTypeCode());
        instituteProposalBean.setTitle(instituteProposalLogBean.getTitle());
        instituteProposalBean.setSponsorCode(instituteProposalLogBean.getSponsorCode());
        instituteProposalBean.setSponsorName(instituteProposalLogBean.getSponsorName());
        // Added for Case 2162  - adding Award Type - Start 
        instituteProposalBean.setAwardTypeCode(0);
        instituteProposalBean.setAwardTypeDesc("");
        // Added for Case 2162  - adding Award Type - End
        /* Added by Shivakumar bug fixing, bug id 1351 */
        instituteProposalBean.setMode(NEW_MODE);
        // End Shivakumar
        // Added for COEUSQA-1471_show institute proposal for merged proposal logs_start
        instituteProposalBean.setMergedProposalData(instituteProposalTxnBean.getMergedDataForProposal(instituteProposalLogBean.getProposalNumber()));
        // Added for COEUSQA-1471_show institute proposal for merged proposal logs_end
        cvInstPropData = new CoeusVector();
        cvInstPropData.addElement(instituteProposalBean);
        instituteProposalData.put(InstituteProposalBean.class, cvInstPropData);       
        
        //Get Investigator and Lead Unit from ProposalLogBean and send it to client
        cvInstPropData = new CoeusVector();
        InstituteProposalInvestigatorBean instituteProposalInvestigatorBean = new InstituteProposalInvestigatorBean();
        instituteProposalInvestigatorBean.setProposalNumber(proposalNumber);
        instituteProposalInvestigatorBean.setSequenceNumber(1);
        instituteProposalInvestigatorBean.setPersonId(instituteProposalLogBean.getPrincipleInvestigatorId());
        instituteProposalInvestigatorBean.setPersonName(instituteProposalLogBean.getPrincipleInvestigatorName());
        instituteProposalInvestigatorBean.setNonMITPersonFlag(instituteProposalLogBean.isNonMITPersonFlag());
        instituteProposalInvestigatorBean.setPrincipalInvestigatorFlag(true);
        instituteProposalInvestigatorBean.setFacultyFlag(instituteProposalLogBean.isFacultyFlag());
        instituteProposalInvestigatorBean.setAcType("I");
        
        //Get Lead Unit Details
        UnitDetailFormBean unitDetailFormBean = unitDataTxnBean.getUnitDetails(instituteProposalLogBean.getLeadUnit());
        InstituteProposalUnitBean instituteProposalUnitBean = new InstituteProposalUnitBean();
        instituteProposalUnitBean.setProposalNumber(proposalNumber);
        instituteProposalUnitBean.setSequenceNumber(1);
        instituteProposalUnitBean.setPersonId(instituteProposalLogBean.getPrincipleInvestigatorId());
        instituteProposalUnitBean.setUnitNumber(unitDetailFormBean.getUnitNumber());
        instituteProposalUnitBean.setUnitName(unitDetailFormBean.getUnitName());
        instituteProposalUnitBean.setOspAdministratorName(unitDetailFormBean.getOspAdminName());
        instituteProposalUnitBean.setOspAdminPersonId(unitDetailFormBean.getOspAdminId());
        instituteProposalUnitBean.setLeadUnitFlag(true);
        instituteProposalUnitBean.setAcType("I");
        
        CoeusVector cvLeadUnit = new CoeusVector();
        cvLeadUnit.add(instituteProposalUnitBean);
        instituteProposalInvestigatorBean.setInvestigatorUnits(cvLeadUnit);        
        cvInstPropData.addElement(instituteProposalInvestigatorBean);
        
        instituteProposalData.put(InstituteProposalInvestigatorBean.class, cvInstPropData);

       

        //Get Custome Elements Data
        cvInstPropData = null;
        //if(instituteProposalBean.getSpecialReviewIndicator()!=null && instituteProposalBean.getSpecialReviewIndicator().charAt(0)=='P'){            
        cvInstPropData = instituteProposalTxnBean.getCustomData(proposalNumber);
        //}
        if(cvInstPropData==null){
            cvInstPropData = new CoeusVector();
        }
        instituteProposalData.put(InstituteProposalCustomDataBean.class, cvInstPropData);
        
        cvInstPropData = new CoeusVector();
        //PROPOSAL_COMMENT_CODE
        CoeusParameterBean coeusParameterBean = new CoeusParameterBean();
        coeusParameterBean.setParameterName(CoeusConstants.PROPOSAL_COMMENT_CODE);
        coeusParameterBean.setParameterValue(coeusFunctions.getParameterValue(coeusParameterBean.getParameterName()));
        if(coeusParameterBean.getParameterValue() != null && !coeusParameterBean.getParameterValue().equalsIgnoreCase("")){
            cvInstPropData.addElement(coeusParameterBean);
        }
        
        //INDIRECT_COST_COMMENT_CODE - IDC
        coeusParameterBean = new CoeusParameterBean();
        coeusParameterBean.setParameterName(CoeusConstants.INDIRECT_COST_COMMENT_CODE);
        coeusParameterBean.setParameterValue(coeusFunctions.getParameterValue(coeusParameterBean.getParameterName()));
        if(coeusParameterBean.getParameterValue() != null && !coeusParameterBean.getParameterValue().equalsIgnoreCase("")){
            cvInstPropData.addElement(coeusParameterBean);
        }
        
        //PROPOSAL_SUMMARY_COMMENT_CODE
        coeusParameterBean = new CoeusParameterBean();
        coeusParameterBean.setParameterName(CoeusConstants.PROPOSAL_SUMMARY_COMMENT_CODE);
        coeusParameterBean.setParameterValue(coeusFunctions.getParameterValue(coeusParameterBean.getParameterName()));
        if(coeusParameterBean.getParameterValue() != null && !coeusParameterBean.getParameterValue().equalsIgnoreCase("")){
            cvInstPropData.addElement(coeusParameterBean);
        }
        
        //COST_SHARING_COMMENT_CODE
        coeusParameterBean = new CoeusParameterBean();
        coeusParameterBean.setParameterName(CoeusConstants.COST_SHARING_COMMENT_CODE);
        coeusParameterBean.setParameterValue(coeusFunctions.getParameterValue(coeusParameterBean.getParameterName()));
        if(coeusParameterBean.getParameterValue() != null && !coeusParameterBean.getParameterValue().equalsIgnoreCase("")){
            cvInstPropData.addElement(coeusParameterBean);
        }
        
        //PROPOSAL_IP_REVIEW_COMMENT_CODE
        coeusParameterBean = new CoeusParameterBean();
        coeusParameterBean.setParameterName(CoeusConstants.PROPOSAL_IP_REVIEW_COMMENT_CODE);
        coeusParameterBean.setParameterValue(coeusFunctions.getParameterValue(coeusParameterBean.getParameterName()));
        if(coeusParameterBean.getParameterValue() != null && !coeusParameterBean.getParameterValue().equalsIgnoreCase("")){
            cvInstPropData.addElement(coeusParameterBean);
        }
        
        //PROPOSAL_IP_REVIEWER_COMMENT_CODE
        coeusParameterBean = new CoeusParameterBean();
        coeusParameterBean.setParameterName(CoeusConstants.PROPOSAL_IP_REVIEWER_COMMENT_CODE);
        coeusParameterBean.setParameterValue(coeusFunctions.getParameterValue(coeusParameterBean.getParameterName()));
        if(coeusParameterBean.getParameterValue() != null && !coeusParameterBean.getParameterValue().equalsIgnoreCase("")){
            cvInstPropData.addElement(coeusParameterBean);
        }
        
        //Added for Case#2402 - use a parameter to set the length of the account number throughout app - Start
        //TO get MAX_ACCOUNT_NUMBER_LIMIT parameter details
        coeusParameterBean = new CoeusParameterBean();
        coeusParameterBean.setParameterName(CoeusConstants.MAX_ACCOUNT_NUMBER_LENGTH);
        coeusParameterBean.setParameterValue(ParameterUtils.getMaxAccountNumberLength());
        cvInstPropData.addElement(coeusParameterBean);
        //Case#2402 - End
        
        //Add all comment code to hashtable
        instituteProposalData.put(CoeusParameterBean.class, cvInstPropData);        
        
        return instituteProposalData;
    }
    
    /**
     *  This method is used to get Proposal IP Review Data
     *
     * @return Hashtable containing Proposal IP Review Data
     * @param proposalNumber String
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */    
    private Hashtable getProposalIPReviewData(String proposalNumber)
     throws CoeusException, DBException {
        InstituteProposalTxnBean instituteProposalTxnBean = new InstituteProposalTxnBean();
                InstituteProposalLookUpDataTxnBean instituteProposalLookUpDataTxnBean = new InstituteProposalLookUpDataTxnBean();
        CoeusFunctions coeusFunctions = new CoeusFunctions();

        CoeusVector cvCommentCodes = new CoeusVector();
        CoeusVector cvInstituteData = null;

        Hashtable institutePropData = new Hashtable();
        //Get Proposal Dev Details
        InstituteProposalBean instituteProposalBean = instituteProposalTxnBean.getInstituteProposalDetails(proposalNumber);
        cvInstituteData = new CoeusVector();
        cvInstituteData.add(instituteProposalBean);
        institutePropData.put(InstituteProposalBean.class, cvInstituteData);
        
        //Get Proposal IP Review Details
        InstituteProposalIPReviewBean instituteProposalIPReviewBean = instituteProposalTxnBean.getInstituteProposalIPReview(proposalNumber);
        cvInstituteData = new CoeusVector();
        cvInstituteData.add(instituteProposalIPReviewBean);
        institutePropData.put(InstituteProposalIPReviewBean.class, cvInstituteData);        

        //Get IP Review Activity data
        cvInstituteData = null;
        if(instituteProposalBean.getIpReviewActivityIndicator()!=null && instituteProposalBean.getIpReviewActivityIndicator().charAt(0)=='P'){
            cvInstituteData = instituteProposalTxnBean.getInstituteProposalIPReviewActivity(proposalNumber);
        }
        if(cvInstituteData==null){
            cvInstituteData = new CoeusVector();
        }
        institutePropData.put(InstituteProposalIPReviewActivityBean.class, cvInstituteData);
        
        //IP Review Requirement Type
        cvInstituteData = new CoeusVector();
        cvInstituteData = instituteProposalLookUpDataTxnBean.getIPReviewRequirementType();
        if(cvInstituteData==null){
            cvInstituteData = new CoeusVector();
        }
        institutePropData.put(KeyConstants.IP_REVIEW_REQUIREMENT_TYPE, cvInstituteData);
        
        //IP Review Result Type
        cvInstituteData = new CoeusVector();
        cvInstituteData = instituteProposalLookUpDataTxnBean.getIPReviewResultType();
        if(cvInstituteData==null){
            cvInstituteData = new CoeusVector();
        }
        institutePropData.put(KeyConstants.IP_REVIEW_RESULT_TYPE, cvInstituteData);
        
        //IP Review Activity Type
        cvInstituteData = new CoeusVector();
        cvInstituteData = instituteProposalLookUpDataTxnBean.getIPReviewActivityType();
        if(cvInstituteData==null){
            cvInstituteData = new CoeusVector();
        }
        institutePropData.put(KeyConstants.IP_REVIEW_ACTIVITY_TYPE, cvInstituteData);
        
        //Get IP Review Comments
        CoeusParameterBean coeusParameterBean = new CoeusParameterBean();
        CoeusVector cvAllComments = new CoeusVector();
        cvInstituteData = null;
        coeusParameterBean.setParameterName(CoeusConstants.PROPOSAL_IP_REVIEW_COMMENT_CODE);
        coeusParameterBean.setParameterValue(coeusFunctions.getParameterValue(coeusParameterBean.getParameterName()));                        
        if(coeusParameterBean.getParameterValue()!=null && !coeusParameterBean.getParameterValue().equals("")){
            cvCommentCodes.addElement(coeusParameterBean);
            cvInstituteData = instituteProposalTxnBean.getInstituteProposalComments(proposalNumber, Integer.parseInt(coeusParameterBean.getParameterValue()));
        }        
        if(cvInstituteData!=null){
            cvAllComments.addAll(cvInstituteData);
        }

        //Get IP Reviewer Comments
        coeusParameterBean = new CoeusParameterBean();
        cvInstituteData = null;
        coeusParameterBean.setParameterName(CoeusConstants.PROPOSAL_IP_REVIEWER_COMMENT_CODE);
        coeusParameterBean.setParameterValue(coeusFunctions.getParameterValue(coeusParameterBean.getParameterName()));
        if(coeusParameterBean.getParameterValue()!=null && !coeusParameterBean.getParameterValue().equals("")){
            cvCommentCodes.addElement(coeusParameterBean);
            cvInstituteData = instituteProposalTxnBean.getInstituteProposalComments(proposalNumber, Integer.parseInt(coeusParameterBean.getParameterValue()));
        }                
        if(cvInstituteData!=null){
            cvAllComments.addAll(cvInstituteData);
        }
        //Add all comments to hashtable
        institutePropData.put(InstituteProposalCommentsBean.class, cvAllComments);
        
        //Add all comment code to hashtable
        institutePropData.put(CoeusParameterBean.class, cvCommentCodes);
        
        return institutePropData;
    }
    
    /**
     *  This method is used to get Proposal Log Data
     *
     * @return Hashtable containing Proposal Log Data
     * @param proposalNumber String
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */    
    private Hashtable getProposalLogData(String proposalNumber)
     throws CoeusException, DBException {
        Hashtable proposalLogData = new Hashtable();
        CoeusVector coeusVector = new CoeusVector();
        InstituteProposalTxnBean instituteProposalTxnBean = new InstituteProposalTxnBean();
        InstituteProposalLookUpDataTxnBean instituteProposalLookUpDataTxnBean  = new InstituteProposalLookUpDataTxnBean();
        
        InstituteProposalLogBean instituteProposalLogBean = instituteProposalTxnBean.getInstituteProposalLog(proposalNumber);
        coeusVector.addElement(instituteProposalLogBean);
        proposalLogData.put(InstituteProposalLogBean.class, coeusVector);        
        
        //Proposal Type
        coeusVector = new CoeusVector();
        coeusVector = instituteProposalLookUpDataTxnBean.getProposalTypes();
        if(coeusVector==null){
            coeusVector = new CoeusVector();
        }
        proposalLogData.put(KeyConstants.PROPOSAL_TYPE, coeusVector);        

        //Institute Proposal Log Status
        coeusVector = instituteProposalLookUpDataTxnBean.getProposalLogStatus();        
        if(coeusVector==null){
            coeusVector = new CoeusVector();
        }
        proposalLogData.put(KeyConstants.INSTITUTE_PROPOSAL_STATUS, coeusVector);        
        
        return proposalLogData;
    }
    
    /**
     *  This method is used to get Proposal Log Data
     *
     * @return Hashtable containing Proposal Log Data
     * @param proposalNumber String
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */    
    private Hashtable getNewProposalLogData()
     throws CoeusException, DBException {
         
        InstituteProposalLookUpDataTxnBean instituteProposalLookUpDataTxnBean  = new InstituteProposalLookUpDataTxnBean();
        Hashtable proposalLogData = new Hashtable();
        CoeusVector coeusVector = new CoeusVector();
        
        //Proposal Type
        coeusVector = instituteProposalLookUpDataTxnBean.getProposalTypes();
        if(coeusVector==null){
            coeusVector = new CoeusVector();
        }
        proposalLogData.put(KeyConstants.PROPOSAL_TYPE, coeusVector);        

        //Institute Proposal Status
        coeusVector = instituteProposalLookUpDataTxnBean.getProposalLogStatus();        
        if(coeusVector==null){
            coeusVector = new CoeusVector();
        }
        proposalLogData.put(KeyConstants.INSTITUTE_PROPOSAL_STATUS, coeusVector);        
        
        return proposalLogData;
    }
    
    /**
     *  This method is used to get Institute Proposal Master Data for 
     *
     * @return Hashtable containing Proposal Log Data
     * @param proposalNumber String
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */    
    private CoeusVector getMasterDataForNewMode()
     throws CoeusException, DBException {
        
         CoeusFunctions coeusFunctions = new CoeusFunctions();
         CoeusVector cvInstPropData = new CoeusVector();
         
        //PROPOSAL_COMMENT_CODE
        CoeusParameterBean coeusParameterBean = new CoeusParameterBean();
        coeusParameterBean.setParameterName(CoeusConstants.PROPOSAL_COMMENT_CODE);
        coeusParameterBean.setParameterValue(coeusFunctions.getParameterValue(coeusParameterBean.getParameterName()));
        if(coeusParameterBean.getParameterValue() != null && !coeusParameterBean.getParameterValue().equalsIgnoreCase("")){
            cvInstPropData.addElement(coeusParameterBean);
        }
        
        //INDIRECT_COST_COMMENT_CODE - IDC
        coeusParameterBean = new CoeusParameterBean();
        coeusParameterBean.setParameterName(CoeusConstants.INDIRECT_COST_COMMENT_CODE);
        coeusParameterBean.setParameterValue(coeusFunctions.getParameterValue(coeusParameterBean.getParameterName()));
        if(coeusParameterBean.getParameterValue() != null && !coeusParameterBean.getParameterValue().equalsIgnoreCase("")){
            cvInstPropData.addElement(coeusParameterBean);
        }
        
        //PROPOSAL_SUMMARY_COMMENT_CODE
        coeusParameterBean = new CoeusParameterBean();
        coeusParameterBean.setParameterName(CoeusConstants.PROPOSAL_SUMMARY_COMMENT_CODE);
        coeusParameterBean.setParameterValue(coeusFunctions.getParameterValue(coeusParameterBean.getParameterName()));
        if(coeusParameterBean.getParameterValue() != null && !coeusParameterBean.getParameterValue().equalsIgnoreCase("")){
            cvInstPropData.addElement(coeusParameterBean);
        }
        
        //COST_SHARING_COMMENT_CODE
        coeusParameterBean = new CoeusParameterBean();
        coeusParameterBean.setParameterName(CoeusConstants.COST_SHARING_COMMENT_CODE);
        coeusParameterBean.setParameterValue(coeusFunctions.getParameterValue(coeusParameterBean.getParameterName()));
        if(coeusParameterBean.getParameterValue() != null && !coeusParameterBean.getParameterValue().equalsIgnoreCase("")){
            cvInstPropData.addElement(coeusParameterBean);
        }
        
        //PROPOSAL_IP_REVIEW_COMMENT_CODE
        coeusParameterBean = new CoeusParameterBean();
        coeusParameterBean.setParameterName(CoeusConstants.PROPOSAL_IP_REVIEW_COMMENT_CODE);
        coeusParameterBean.setParameterValue(coeusFunctions.getParameterValue(coeusParameterBean.getParameterName()));
        if(coeusParameterBean.getParameterValue() != null && !coeusParameterBean.getParameterValue().equalsIgnoreCase("")){
            cvInstPropData.addElement(coeusParameterBean);
        }
        
        //PROPOSAL_IP_REVIEWER_COMMENT_CODE
        coeusParameterBean = new CoeusParameterBean();
        coeusParameterBean.setParameterName(CoeusConstants.PROPOSAL_IP_REVIEWER_COMMENT_CODE);
        coeusParameterBean.setParameterValue(coeusFunctions.getParameterValue(coeusParameterBean.getParameterName()));
        if(coeusParameterBean.getParameterValue() != null && !coeusParameterBean.getParameterValue().equalsIgnoreCase("")){
            cvInstPropData.addElement(coeusParameterBean);
        }
        
        //Added for Coeus Enhancement Case #1799 - start step:2
        coeusParameterBean = new CoeusParameterBean();
        coeusParameterBean.setParameterName(CoeusConstants.SPL_REV_TYPE_CODE_HUMAN);
        coeusParameterBean.setParameterValue(coeusFunctions.getParameterValue(coeusParameterBean.getParameterName()));
        if(coeusParameterBean.getParameterValue() != null && !coeusParameterBean.getParameterValue().equalsIgnoreCase("")){
            cvInstPropData.addElement(coeusParameterBean);
        }
        
        coeusParameterBean = new CoeusParameterBean();
        coeusParameterBean.setParameterName(CoeusConstants.ENABLE_PROTOCOL_TO_AWARD_LINK);
        coeusParameterBean.setParameterValue(coeusFunctions.getParameterValue(coeusParameterBean.getParameterName()));
        if(coeusParameterBean.getParameterValue() != null && !coeusParameterBean.getParameterValue().equalsIgnoreCase("")){
            cvInstPropData.addElement(coeusParameterBean);
        }
        
        coeusParameterBean = new CoeusParameterBean();
        coeusParameterBean.setParameterName(CoeusConstants.ENABLE_PROTOCOL_TO_PROPOSAL_LINK);
        coeusParameterBean.setParameterValue(coeusFunctions.getParameterValue(coeusParameterBean.getParameterName()));
        if(coeusParameterBean.getParameterValue() != null && !coeusParameterBean.getParameterValue().equalsIgnoreCase("")){
            cvInstPropData.addElement(coeusParameterBean);
        }
        
        
        coeusParameterBean = new CoeusParameterBean();
        coeusParameterBean.setParameterName(CoeusConstants.LINKED_TO_IRB_CODE);
        coeusParameterBean.setParameterValue(coeusFunctions.getParameterValue(coeusParameterBean.getParameterName()));
        if(coeusParameterBean.getParameterValue() != null && !coeusParameterBean.getParameterValue().equalsIgnoreCase("")){
            cvInstPropData.addElement(coeusParameterBean);
        }
        
        //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - start
        //get IACUC_SPL_REV_TYPE_CODE parameter
        coeusParameterBean = new CoeusParameterBean();
        coeusParameterBean.setParameterName(CoeusConstants.IACUC_SPL_REV_TYPE_CODE);
        coeusParameterBean.setParameterValue(coeusFunctions.getParameterValue(coeusParameterBean.getParameterName()));
        if(coeusParameterBean.getParameterValue() != null && !coeusParameterBean.getParameterValue().equalsIgnoreCase("")){
            cvInstPropData.addElement(coeusParameterBean);
        }
        
        //get ENABLE_IACUC_PROTOCOL_TO_AWARD_LINK parameter
        coeusParameterBean = new CoeusParameterBean();
        coeusParameterBean.setParameterName(CoeusConstants.ENABLE_IACUC_PROTOCOL_TO_AWARD_LINK);
        coeusParameterBean.setParameterValue(coeusFunctions.getParameterValue(coeusParameterBean.getParameterName()));
        if(coeusParameterBean.getParameterValue() != null && !coeusParameterBean.getParameterValue().equalsIgnoreCase("")){
            cvInstPropData.addElement(coeusParameterBean);
        }
        
        //get ENABLE_IACUC_PROTOCOL_TO_PROPOSAL_LINK parameter
        coeusParameterBean = new CoeusParameterBean();
        coeusParameterBean.setParameterName(CoeusConstants.ENABLE_IACUC_PROTOCOL_TO_PROPOSAL_LINK);
        coeusParameterBean.setParameterValue(coeusFunctions.getParameterValue(coeusParameterBean.getParameterName()));
        if(coeusParameterBean.getParameterValue() != null && !coeusParameterBean.getParameterValue().equalsIgnoreCase("")){
            cvInstPropData.addElement(coeusParameterBean);
        }
        
        //get LINKED_TO_IACUC_CODE parameter
        coeusParameterBean = new CoeusParameterBean();
        coeusParameterBean.setParameterName(CoeusConstants.LINKED_TO_IACUC_CODE);
        coeusParameterBean.setParameterValue(coeusFunctions.getParameterValue(coeusParameterBean.getParameterName()));
        if(coeusParameterBean.getParameterValue() != null && !coeusParameterBean.getParameterValue().equalsIgnoreCase("")){
            cvInstPropData.addElement(coeusParameterBean);
        }
        //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB -  end
        
        //Added for Case#2402 - use a parameter to set the length of the account number throughout app - Start
        //TO get MAX_ACCOUNT_NUMBER_LIMIT parameter details
        coeusParameterBean = new CoeusParameterBean();
        coeusParameterBean.setParameterName(CoeusConstants.MAX_ACCOUNT_NUMBER_LENGTH);
        coeusParameterBean.setParameterValue(ParameterUtils.getMaxAccountNumberLength());
        cvInstPropData.addElement(coeusParameterBean);
        //Case#2402 - End
       
        
        return cvInstPropData;
    }   
    //UCSD - rdias coeus personalization
    //Intercept the responderbean for including personalization. info
    private void setFormAccessXML(RequesterBean requester,ResponderBean responder) {
        try{
        Personalization.getInstance().setProposalAccessXML(responder,requester);
        }catch (Exception exception) {
         UtilFactory.log(exception.getMessage(),exception,"Servlet","accessXML");
        }
    }    
    
}
