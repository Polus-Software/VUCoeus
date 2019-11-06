/*
 * LockingServlet.java
 *
 * Created on July 20, 2004, 4:14 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.servlet;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.bean.*;
import edu.mit.coeus.irb.bean.ProtocolDataTxnBean;
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
import edu.mit.coeus.utils.dbengine.TransactionMonitor;
import edu.mit.coeus.utils.locking.LockingBean;
import edu.mit.coeus.utils.locking.LockingTxnBean;
import edu.mit.coeus.utils.locking.LockingException;
import edu.mit.coeuslite.utils.CoeusLiteConstants;

import javax.servlet.http.*;
import javax.servlet.*;
import java.io.*;
import java.util.Vector;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Timer;
import java.sql.Date;
import java.util.Calendar;

/** This servlet is used for Award Maintenance. 
 * All Award related server calls are implemented here.
 *
 * @author Shivakumarr M J.
 * @version :1.0 July 20, 2004 4:16 PM
 *
 */
public class LockingServlet extends CoeusBaseServlet implements TypeConstants{
    
    // Functions
    private static final char GET_LOCK = 'A';
    private static final char UPDATE_AWARD_LOCK = 'B';
    private static final char RELEASE_LOCK = 'C';
    private static final char GET_UPDATE_TIMESTAMP = 'D';
    private static final char UPDATE_AWARD_LOCK_BULK = 'E';
    private static final char GET_ALL_LOCKIDS = 'F';
    private static final char DELETE_LOCK_ID = 'G';
    private static final String LOCK_STRING = "OSP$";
    //COEUSQA-1433 - Allow Recall from Routing - Start
    private static final char PROTOCOL_RECALL_LOCK_CHECK = 'H';
    //COEUSQA-1433 - Allow Recall from Routing - End
    private static final char UNLOCK_ROUTING = 'J';
    
            
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
            Vector dataObjects = new Vector();
            
            char functionType = requester.getFunctionType();
            AwardBean awardBean = null;
            AwardTxnBean awardTxnBean = new AwardTxnBean();
            AwardLookUpDataTxnBean awardLookUpDataTxnBean = new AwardLookUpDataTxnBean();
            String awardNumber="";
            int sequenceNumber;
            
            if(functionType == GET_LOCK){
                Hashtable hshLockData = new Hashtable();
//                String awardNo = requester.getDataObject().toString();
                //edu.mit.coeus.award.bean.AwardBaseBean bean = (AwardBaseBean)requester.getDataObject();
                CoeusVector cvData = (CoeusVector)requester.getDataObject();
                awardBean = (AwardBean)cvData.elementAt(0);
                String awardNo  = awardBean.getMitAwardNumber();                
//                LockingBean bean = (LockingBean)requester.getDataObject();
                LockingTxnBean lockingTxnBean = new LockingTxnBean();
                LockingBean lockingBean = new LockingBean();
                lockingBean.setModuleName("Award");
                lockingBean.setKey1(awardNo);
                lockingBean.setKey2("");
                lockingBean.setUpdateUser(loggedinUser);
                lockingBean.setUnitNumber(unitNumber);                
                LockingBean lockingBeanData = lockingTxnBean.getAwardLock(lockingBean);                
                hshLockData.put(LockingBean.class, lockingBeanData);
                responder.setDataObject(hshLockData);
                responder.setMessage(null);
                responder.setResponseStatus(true);
                  
            }    
            else if(functionType == UPDATE_AWARD_LOCK){                
                CoeusVector cvData = (CoeusVector)requester.getDataObject();
                awardBean = (AwardBean)cvData.elementAt(0);
                String awardNo  = awardBean.getMitAwardNumber();                
                LockingBean bean = new LockingBean();
                LockingTxnBean lockingTxnBean = new LockingTxnBean();
                bean.setModuleName("Award");
                bean.setKey1(awardNo);
                bean.setKey2("");
                bean.setUserID("COEUS");
                int updateNumber = lockingTxnBean.updAwardLock(bean);                
                if(updateNumber == 1){
                    responder.setDataObject(new Boolean(true));     
                    responder.setMessage(null);
                } 
                else{
                    responder.setDataObject(new Boolean(false));     
                    responder.setMessage(null);
                }
                responder.setResponseStatus(true);  
            }    
            else if(functionType == RELEASE_LOCK){                
                CoeusVector cvData = (CoeusVector)requester.getDataObject();
                awardBean = (AwardBean)cvData.elementAt(0);
                String awardNo  = awardBean.getMitAwardNumber();
                LockingBean bean = new LockingBean();
                LockingTxnBean lockingTxnBean = new LockingTxnBean();
                bean.setModuleName("Award");
                bean.setKey1(awardNo);
                bean.setKey2("");                
                int lockId = lockingTxnBean.releaseAwardLock(bean);
                if(lockId == 1){
                    responder.setDataObject(new Boolean(true));     
                    responder.setMessage(null);
                } 
                else{
                    responder.setDataObject(new Boolean(false));     
                    responder.setMessage(null);
                }                
                responder.setResponseStatus(true);  
            }else if(functionType == GET_UPDATE_TIMESTAMP){         
                Hashtable htTimestamp = new Hashtable();
                CoeusVector cvData = (CoeusVector)requester.getDataObject();
                awardBean = (AwardBean)cvData.elementAt(0);
                String awardNo  = awardBean.getMitAwardNumber();                
                LockingBean bean = new LockingBean();
                LockingTxnBean lockingTxnBean = new LockingTxnBean();
                String updLockId = "Award"+awardNo;                
                bean.setLockID(updLockId);
                LockingBean lockTimestampData = lockingTxnBean.getUpdateTimestamp(bean);                                
                htTimestamp.put(LockingBean.class,lockTimestampData);
                responder.setDataObject(htTimestamp);
                responder.setMessage(null);
                responder.setResponseStatus(true);                
            }else if(functionType == UPDATE_AWARD_LOCK_BULK){                                
                Hashtable hshGetLockData = (Hashtable)requester.getDataObject();
                LockingTxnBean lockingTxnBean = new LockingTxnBean();                
                Hashtable htUpdLock = lockingTxnBean.updAwardLockBulk(hshGetLockData);                                
                responder.setDataObject(htUpdLock);     
                responder.setMessage(null);
                responder.setResponseStatus(true);                  
            }else if(functionType == GET_ALL_LOCKIDS){                                                
                    LockingTxnBean lockingTxnBean = new LockingTxnBean();                
                    CoeusVector cvLockIds = lockingTxnBean.getAllLockIds();                
                    String msg = "";
                    if(((Boolean)cvLockIds.get(cvLockIds.size()-1)).booleanValue()) {
                        CoeusMessageResourcesBean coeusMessageResourcesBean
                                    = new CoeusMessageResourcesBean();
                        msg= coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1020");
                    }
                    cvLockIds.removeElementAt(cvLockIds.size()-1);
                    responder.setDataObjects(cvLockIds);     
                    responder.setMessage(msg);
                    responder.setResponseStatus(true);                  
            }else if(functionType == DELETE_LOCK_ID){
                    CoeusVector data = (CoeusVector)requester.getDataObjects();
                    String moduleName = (String)data.elementAt(0);
                    String item = (String)data.elementAt(1);
                    String lockId = LOCK_STRING+moduleName.trim()+"_"+item;
                    LockingTxnBean lockingTxnBean = new LockingTxnBean();                
                    LockingBean lockDelete = lockingTxnBean.deleteLockId(lockId, loggedinUser);
                    boolean deleteStatus = false;
                    if(lockDelete.getAcType().equals("D")){
                        deleteStatus = true;
                    }else{
                        deleteStatus = false;
                    }                        
                    responder.setLockingBean(lockDelete);
                    responder.setMessage(null);
                    responder.setResponseStatus(deleteStatus);                  
            }
            //COEUSQA-1433 - Allow Recall from Routing - Start
            else if(functionType == PROTOCOL_RECALL_LOCK_CHECK){
                // Lock the Protocol and Send the boolean value to the Client Side
                boolean lockCheck = false;
                String protocolNumber = (String)requester.getId();
                LockingBean lockingBean = new LockingBean();
                ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean();
                //Commented and added for COEUSDEV-1075 : Locking messages inconsistency between Lite and Premium - start
                //lockingBean = protocolDataTxnBean.lockProtocol(protocolNumber, loggedinUser, unitNumber);
                lockingBean = protocolDataTxnBean.lockProtocolRouting(protocolNumber, loggedinUser, unitNumber);
                //Commented and added for COEUSDEV-1075 : Locking messages inconsistency between Lite and Premium - end
                if(lockingBean != null){
                    lockCheck = true;
                }
                protocolDataTxnBean.transactionCommit();
                responder.setDataObject(lockCheck);
            }
            //COEUSQA-1433 - Allow Recall from Routing - End
            
            else if(functionType == UNLOCK_ROUTING){
                
                Vector vecModuleDetails = requester.getDataObjects();
                LockingBean lockingBean = null;
                if(vecModuleDetails != null && !vecModuleDetails.isEmpty()){
                    int moduleCode = Integer.parseInt((String)vecModuleDetails.get(0));
                    String moduleItemKey = (String)vecModuleDetails.get(1);
                    
                    String lockStr = "";
                    if(moduleCode == ModuleConstants.PROPOSAL_DEV_MODULE_CODE){
                        lockStr = CoeusLiteConstants.PROP_ROUTING_LOCK_STR+moduleItemKey;
                    }else if(moduleCode == ModuleConstants.PROTOCOL_MODULE_CODE){
                        lockStr = CoeusLiteConstants.PROTO_ROUTING_LOCK_STR+moduleItemKey;
                    }else if(moduleCode == ModuleConstants.IACUC_MODULE_CODE){
                        lockStr = CoeusLiteConstants.IACUC_PROTO_ROUTING_LOCK_STR+moduleItemKey;
                    }
                    
                    LockingTxnBean lockingTxnBean = new LockingTxnBean();
                    String unitnumber = lockingTxnBean.getLockData(lockStr, loggedinUser);
                    // Unit number "00000000" is set for protocol locked from coeus lite.
                    if(unitnumber!=null && !unitnumber.equals("00000000")) {
                        TransactionMonitor transactionMonitor = new TransactionMonitor(userBean.getUserId());
                         boolean lockCheck = transactionMonitor.lockAvailabilityCheck(lockStr,userBean.getUserId());
                         if(!lockCheck){
                            lockingBean = transactionMonitor.releaseLock(lockStr,userBean.getUserId());
                         }
                        
                    }
                    
                }
                responder.setDataObject(lockingBean);
                responder.setMessage(null);
                responder.setResponseStatus(true) ;
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
                "BudgetMaintenanceServlet", "perform");
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
            "LockingServlet", "doPost");
            
        }catch(Exception e) {
            //e.printStackTrace();
            responder.setResponseStatus(false);
            responder.setException(e);
            responder.setMessage(e.getMessage());
            UtilFactory.log( e.getMessage(), e,
            "LockingServlet", "doPost");
        //Case 3193 - START
        }catch(Throwable throwable){
            Exception ex = new Exception(throwable);
            responder.setException(ex);
            responder.setResponseStatus(false);
            responder.setMessage(ex.getMessage());
            UtilFactory.log( throwable.getMessage(), throwable, "LockingServlet", "doPost");
        //Case 3193 - END
        } finally {
            try{
                
                outputToApplet = new ObjectOutputStream(response.getOutputStream());
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
                "LockingServlet", "doPost");
            }
        }   
        
        
    }   

    
}
