/*
 * TransactionMonitor.java
 *
 * Created on December 5, 2002, 10:47 PM
 */

/* 
 * PMD check performed, and commented unused imports and variables on 20th Jan 2009
 * by Sreenath
 */

package edu.mit.coeus.utils.dbengine;

import edu.mit.coeus.utils.locking.LockingBean;
import edu.mit.coeus.utils.locking.LockingException;
import edu.mit.coeus.utils.CoeusFunctions;
//import edu.mit.coeus.utils.locking.LockingBean;
import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.utils.dbengine.DBEngineConstants;
//import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.CoeusVector;
//import edu.mit.coeus.utils.dbengine.ProcReqParameter;
import edu.mit.coeus.bean.CoeusMessageResourcesBean;

import java.util.Vector;
import java.util.HashMap;
//import java.util.Date;
//import java.util.Set;
//import java.util.Iterator;
//import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Timestamp;


import java.util.Hashtable;
//import java.util.Timer;
//import java.util.TimerTask;
//import java.util.Properties;
//import java.util.Enumeration;
/**
 *
 * @author  geo
 */
public class TransactionMonitor {
    
    
    // holds the dataset name
//    private static final String DSN = "Coeus";    
    private String userId;
    // Instance of a dbEngine
    private DBEngineImpl dbEngine;
    private Connection conn;
    private Hashtable lockedRows;
    private static TransactionMonitor transactionMonitor;
    /** Creates a new instance of TransactionMonitor */
    // Modified by Shivakumar 04 Aug. 2004
    public TransactionMonitor() {
        lockedRows = new Hashtable();
        dbEngine = new DBEngineImpl();
        //initializeTimer();
    }
    
    public TransactionMonitor(String userId) {
        this.userId = userId;
        dbEngine = new DBEngineImpl();        
    }
    
    public static TransactionMonitor getInstance(){
        if(transactionMonitor==null){
            transactionMonitor= new TransactionMonitor();            
        }
        return transactionMonitor;        
    }
    
    public boolean canDelete(String rowId){
        return (lockedRows.get(rowId)==null);
    }
    
    public boolean canEdit(String rowId){
        Long curTime = new Long(System.currentTimeMillis());
        //System.out.println("Can edit "+rowId);
        if(lockedRows.get(rowId)==null){
            //System.out.println("Lets lock it "+rowId);
            this.lockedRows.put(rowId,curTime);
            return true;
        }else{
            //System.out.println("It is locked "+rowId);
            return false;
        }
    }
    
    public void releaseEdit(String rowId){
        if(rowId!=null)
        {
            //System.out.println("Let me release it "+rowId);
            this.lockedRows.remove(rowId);
            return;
        }
        //System.out.println("Stray rowId "+rowId);
    }    

    Hashtable getLockedRows(){
        return lockedRows;
    }
    
    // Added by Shivakumar - 04 Aug 2004 - begin 
    
    public LockingBean canEdit(String rowId,String loggedinUser,String unitNumber,Connection conn)
            throws CoeusException, DBException,LockingException{
        LockingBean lockingBean = new LockingBean();
        dbEngine=new DBEngineImpl();
        Vector result=null;
        Vector param=new Vector();
        HashMap invRow=null;
        param.addElement(new Parameter("LOCK_ID",
           DBEngineConstants.TYPE_STRING, rowId));        
        param.addElement(new Parameter("USER_ID",
           DBEngineConstants.TYPE_STRING, loggedinUser.toUpperCase()));        
        param.addElement(new Parameter("UNIT_NUMBER",
           DBEngineConstants.TYPE_STRING, unitNumber));
        param.addElement(new Parameter("LOCK_SESSION_ID",
           DBEngineConstants.TYPE_STRING, ""));
        if(dbEngine !=null){            
            result=new Vector(3,2);
            
            result = dbEngine.executeRequest("Coeus",
            "call PKG_LOCK.NEW_GET_LOCK ( <<LOCK_ID>>, <<USER_ID>>,<<UNIT_NUMBER>>, <<LOCK_SESSION_ID>>,<<OUT RESULTSET rset>> )",
            "Coeus", param,conn);  
        }else{
            throw new CoeusException("db_exceptionCode.1000");   
        }     
        int listSize=result.size();
//        CoeusVector invList=null;
        lockingBean = null;
        if(listSize>0){
            for(int index = 0; index < listSize; index++){
//                invList=new CoeusVector();
                invRow=(HashMap)result.elementAt(index); 
                lockingBean = new LockingBean();
                lockingBean.setUserID((String)
                    invRow.get("USER_ID"));
//                String lockID = invRow.get("LOCK_ID").toString();
                lockingBean.setLockID((String)invRow.get("LOCK_ID").toString());
                lockingBean.setUnitNumber((String)
                    invRow.get("UNIT_NUMBER"));
                lockingBean.setUserID((String)
                    invRow.get("UPDATE_USER"));                
                lockingBean.setUpdateTimestamp((Timestamp)
                    invRow.get("UPDATE_TIMESTAMP"));
                lockingBean.setCreateTimestamp((Timestamp)
                    invRow.get("CREATE_TIMESTAMP"));
                lockingBean.setGotLock(invRow.get("GOT_LOCK") == null ? false:(invRow.get("GOT_LOCK").toString().equals("1") ? true : false));
            }          
        }        
        boolean gotLock = lockingBean.isGotLock();
        String userId = lockingBean.getUserID();
//        String lockId = lockingBean.getLockID();
        String acType = "I";
        lockingBean.setAcType(acType);
            if(!(gotLock)){
                String module = lockingBean.getLockID();
                StringBuffer moduleName = new StringBuffer(module);
                moduleName.delete(0, 4);
                Vector vcParam = new Vector();
                vcParam.addElement(new Parameter("USER_ID",
                    DBEngineConstants.TYPE_STRING, lockingBean.getUserID()));
                /* Calling procedure DW_GET_USER to get USER_NAME
                 * which has to be displayed at Client side
                 */
                if(dbEngine !=null){            
                    result=new Vector(3,2);
                    //Commented for displaying username for user id enhancement - starts
//                    result = dbEngine.executeRequest("Coeus",
//                    "call DW_GET_USER ( <<USER_ID>>, <<OUT RESULTSET rset>> )",
//                    "Coeus", vcParam);
                    //Commented for displaying username for user id enhancement - ends
                    //Added for displaying username for user id enhancement - starts
                    /* Calling function FN_GET_USER_NAME to get USER_NAME
                     * which has to be displayed at Client side
                     */
                    result = dbEngine.executeFunctions("Coeus",
                    "{<<OUT STRING USER_NAME>>=call FN_GET_USER_NAME ( "
                            + " << USER_ID >>)}", vcParam);
                    //Added for displaying username for user id enhancement - ends
                }else{
                    throw new CoeusException("db_exceptionCode.1000");   
                }
                //Commented for displaying username for user id enhancement - starts
//                int vcSize=result.size();
//                String userName = "";
//                if(vcSize > 0){
//                    for(int index = 0; index < vcSize; index++){
//                        HashMap hmUserData=(HashMap)result.elementAt(index);
//
//                        userName = (String)hmUserData.get("USER_NAME");
//                    }
//                }
                //Commented for displaying username for user id enhancement - ends
                //Added for displaying username for user id enhancement - starts
                String userName = userId;
                if(!result.isEmpty()){                
                        HashMap hmUserData=(HashMap)result.elementAt(0);
                        userName = (String)hmUserData.get("USER_NAME");
                        userName = (userName==null || userName.equals(""))? userId :userName ;
                }
                //Added for displaying username for user id enhancement - ends
//                StringBuffer strBuffModuleName = new StringBuffer(module);
                String strModName = new String();
                String strNumber = new String();
                strModName = moduleName.substring(0,moduleName.indexOf("_"));
                strNumber = module.substring(module.indexOf("_")+1);
                String msg = "";
                CoeusMessageResourcesBean coeusMessageResourcesBean
                    =new CoeusMessageResourcesBean();
                if(userName == ""){                    
                    //msg = "Some other application is using "+strModName+" "+strNumber; 
                    /* Getting message for coeusMessage.properties file
                     */
                    msg = coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1001")+" "+strModName+" "+strNumber+" "; 
                }else{                    
                    //msg = userName +" is using "+strModName+" "+strNumber; 
                    /* Getting message for coeusMessage.properties file
                     */
                    msg = userName+" "+coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1000")+" "+strModName+" "+strNumber+" "; 
                }
                /* Rollback the connection if the user fails to get 
                 * lock
                 */                  
                dbEngine.rollback(conn);
                throw new LockingException(msg);
            }
        return lockingBean;
    }
    
    
        public LockingBean canEdit(String rowId,String loggedinUser,String unitNumber,
            Connection conn, String sessionId)
            throws CoeusException, DBException,LockingException{
        LockingBean lockingBean = new LockingBean();
        dbEngine=new DBEngineImpl();
        Vector result=null;
        Vector param=new Vector();
        HashMap invRow=null;
        param.addElement(new Parameter("LOCK_ID",
           DBEngineConstants.TYPE_STRING, rowId));        
        param.addElement(new Parameter("USER_ID",
           DBEngineConstants.TYPE_STRING, loggedinUser.toUpperCase()));        
        param.addElement(new Parameter("UNIT_NUMBER",
           DBEngineConstants.TYPE_STRING, unitNumber));
        param.addElement(new Parameter("LOCK_SESSION_ID",
           DBEngineConstants.TYPE_STRING, sessionId));
        if(dbEngine !=null){            
            result=new Vector(3,2);
            
            result = dbEngine.executeRequest("Coeus",
            "call PKG_LOCK.NEW_GET_LOCK ( <<LOCK_ID>>, <<USER_ID>>,<<UNIT_NUMBER>>, <<LOCK_SESSION_ID>>,<<OUT RESULTSET rset>> )",
            "Coeus", param,conn);  
        }else{
            throw new CoeusException("db_exceptionCode.1000");   
        }     
        int listSize=result.size();
//        CoeusVector invList=null;
        lockingBean = null;
        if(listSize>0){
            for(int index = 0; index < listSize; index++){
//                invList=new CoeusVector();
                invRow=(HashMap)result.elementAt(index); 
                lockingBean = new LockingBean();
                lockingBean.setUserID((String)
                    invRow.get("USER_ID"));
//                String lockID = invRow.get("LOCK_ID").toString();
                lockingBean.setLockID((String)invRow.get("LOCK_ID").toString());
                lockingBean.setUnitNumber((String)
                    invRow.get("UNIT_NUMBER"));
                lockingBean.setUserID((String)
                    invRow.get("UPDATE_USER"));                
                lockingBean.setUpdateTimestamp((Timestamp)
                    invRow.get("UPDATE_TIMESTAMP"));
                lockingBean.setCreateTimestamp((Timestamp)
                    invRow.get("CREATE_TIMESTAMP"));
                lockingBean.setGotLock(invRow.get("GOT_LOCK") == null ? false:(invRow.get("GOT_LOCK").toString().equals("1") ? true : false));
            }          
        }        
        boolean gotLock = lockingBean.isGotLock();
        String userId = lockingBean.getUserID();
//        String lockId = lockingBean.getLockID();
        String acType = "I";
        lockingBean.setAcType(acType);
            if(!(gotLock)){
                String module = lockingBean.getLockID();
                StringBuffer moduleName = new StringBuffer(module);
                moduleName.delete(0, 4);
                Vector vcParam = new Vector();
                vcParam.addElement(new Parameter("USER_ID",
                    DBEngineConstants.TYPE_STRING, lockingBean.getUserID()));
                /* Calling procedure DW_GET_USER to get USER_NAME
                 * which has to be displayed at Client side
                 */
                if(dbEngine !=null){            
                    result=new Vector(3,2);
                    //Commented for displaying username for user id enhancement - starts
//                    result = dbEngine.executeRequest("Coeus",
//                    "call DW_GET_USER ( <<USER_ID>>, <<OUT RESULTSET rset>> )",
//                    "Coeus", vcParam);
                    //Commented for displaying username for user id enhancement - ends
                    //Added for displaying username for user id enhancement - starts
                    /* Calling function FN_GET_USER_NAME to get USER_NAME
                     * which has to be displayed at Client side
                     */
                    result = dbEngine.executeFunctions("Coeus",
                    "{<<OUT STRING USER_NAME>>=call FN_GET_USER_NAME ( "
                            + " << USER_ID >>)}", vcParam);
                    //Added for displaying username for user id enhancement - ends
                }else{
                    throw new CoeusException("db_exceptionCode.1000");   
                }
                //Commented for displaying username for user id enhancement - starts
//                int vcSize=result.size();
//                String userName = "";
//                if(vcSize > 0){
//                    for(int index = 0; index < vcSize; index++){
//                        HashMap hmUserData=(HashMap)result.elementAt(index);
//
//                        userName = (String)hmUserData.get("USER_NAME");
//                    }
//                }
                //Commented for displaying username for user id enhancement - ends
                //Added for displaying username for user id enhancement - starts
                String userName = userId;
                if(!result.isEmpty()){                
                        HashMap hmUserData=(HashMap)result.elementAt(0);
                        userName = (String)hmUserData.get("USER_NAME");
                        userName = (userName==null || userName.equals(""))? userId :userName ;
                }
                //Added for displaying username for user id enhancement - ends
//                StringBuffer strBuffModuleName = new StringBuffer(module);
                String strModName = new String();
                String strNumber = new String();
                strModName = moduleName.substring(0,moduleName.indexOf("_"));
                strNumber = module.substring(module.indexOf("_")+1);
                String msg = "";
                CoeusMessageResourcesBean coeusMessageResourcesBean
                    =new CoeusMessageResourcesBean();
                if(userName == ""){                    
                    //msg = "Some other application is using "+strModName+" "+strNumber; 
                    /* Getting message for coeusMessage.properties file
                     */
                    msg = coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1001")+" "+strModName+" "+strNumber+" "; 
                }else{                    
                    //msg = userName +" is using "+strModName+" "+strNumber; 
                    /* Getting message for coeusMessage.properties file
                     */
                    msg = userName+" "+coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1000")+" "+strModName+" "+strNumber+" "; 
                }
                /* Rollback the connection if the user fails to get 
                 * lock
                 */                  
                dbEngine.rollback(conn);
                throw new LockingException(msg);
            }
        return lockingBean;
    }

    
    // This method will be used only when new Award, proposal numbers are created.
    // New locking method - BEGIN 
    
    public LockingBean newLock(String rowId,String loggedinUser,String unitNumber,Connection conn)
            throws CoeusException, DBException,LockingException{
        LockingBean lockingBean = new LockingBean();
        dbEngine=new DBEngineImpl();
        Vector result=null;
        Vector param=new Vector();
        HashMap invRow=null;
        param.addElement(new Parameter("LOCK_ID",
           DBEngineConstants.TYPE_STRING, rowId));        
        param.addElement(new Parameter("USER_ID",
           DBEngineConstants.TYPE_STRING, loggedinUser.toUpperCase()));        
        param.addElement(new Parameter("UNIT_NUMBER",
           DBEngineConstants.TYPE_STRING, unitNumber));
         param.addElement(new Parameter("LOCK_SESSION_ID",
           DBEngineConstants.TYPE_STRING, ""));
        if(dbEngine !=null){            
            result=new Vector(3,2);
            
            result = dbEngine.executeRequest("Coeus",
            "call PKG_LOCK.NEW_GET_LOCK ( <<LOCK_ID>>, <<USER_ID>>,<<UNIT_NUMBER>>, <<LOCK_SESSION_ID>>,<<OUT RESULTSET rset>> )",
            "Coeus", param,conn);  
        }else{
            throw new CoeusException("db_exceptionCode.1000");   
        }     
        int listSize=result.size();
//        CoeusVector invList=null;
        lockingBean = null;
        if(listSize>0){
            for(int index = 0; index < listSize; index++){
//                invList=new CoeusVector();
                invRow=(HashMap)result.elementAt(index); 
                lockingBean = new LockingBean();
                lockingBean.setUserID((String)
                    invRow.get("USER_ID"));
//                String lockID = invRow.get("LOCK_ID").toString();
                lockingBean.setLockID((String)invRow.get("LOCK_ID").toString());
                lockingBean.setUnitNumber((String)
                    invRow.get("UNIT_NUMBER"));
                lockingBean.setUserID((String)
                    invRow.get("UPDATE_USER"));                
                lockingBean.setUpdateTimestamp((Timestamp)
                    invRow.get("UPDATE_TIMESTAMP"));
                lockingBean.setCreateTimestamp((Timestamp)
                    invRow.get("CREATE_TIMESTAMP"));
                lockingBean.setGotLock(invRow.get("GOT_LOCK") == null ? false:(invRow.get("GOT_LOCK").toString().equals("1") ? true : false));
                // 2930: Auto-delete Current Locks based on new parameter
                lockingBean.setAcType("I");
            }          
        }        
//        boolean gotLock = lockingBean.isGotLock();
//        String userId = lockingBean.getUserID();
//        String lockId = lockingBean.getLockID();        
////        if(!(rowId.equalsIgnoreCase(lockId) && loggedinUser.equalsIgnoreCase(userId))){
//            if(!(gotLock)){
//                String module = lockingBean.getLockID();
//                StringBuffer moduleName = new StringBuffer(module);
//                moduleName.delete(0, 4);
//                String msg = "Sorry,  the user "+lockingBean.getUserID()+"  is using  "+moduleName;
//                throw new LockingException(msg);
//            }
////        }            
        return lockingBean;
    }   
     
    
    
    
   /* The following method is used to for locking any data,it does not 
     accept any Connection object for executing procedures
    */
    
    public LockingBean canEdit(String rowId,String loggedinUser,String unitNumber)
            throws CoeusException, DBException,LockingException{
        LockingBean lockingBean = new LockingBean();
        dbEngine=new DBEngineImpl();
        Vector result=null;
        Vector param=new Vector();
        HashMap invRow=null;
        param.addElement(new Parameter("LOCK_ID",
           DBEngineConstants.TYPE_STRING, rowId));        
        param.addElement(new Parameter("USER_ID",
           DBEngineConstants.TYPE_STRING, loggedinUser.toUpperCase()));        
        param.addElement(new Parameter("UNIT_NUMBER",
           DBEngineConstants.TYPE_STRING, unitNumber));
        param.addElement(new Parameter("LOCK_SESSION_ID",
           DBEngineConstants.TYPE_STRING, ""));
        if(dbEngine !=null){            
            result=new Vector(3,2);
            
            result = dbEngine.executeRequest("Coeus",
            "call PKG_LOCK.NEW_GET_LOCK ( <<LOCK_ID>>, <<USER_ID>>,<<UNIT_NUMBER>>, <<LOCK_SESSION_ID>>,<<OUT RESULTSET rset>> )",
            "Coeus", param);  
        }else{
            throw new CoeusException("db_exceptionCode.1000");   
        }     
        int listSize=result.size();
//        CoeusVector invList=null;
        lockingBean = null;
        if(listSize>0){
            for(int index = 0; index < listSize; index++){
//                invList=new CoeusVector();
                invRow=(HashMap)result.elementAt(index); 
                lockingBean = new LockingBean();
                lockingBean.setUserID((String)
                    invRow.get("USER_ID"));
//                String lockID = invRow.get("LOCK_ID").toString();
                lockingBean.setLockID((String)invRow.get("LOCK_ID").toString());
                lockingBean.setUnitNumber((String)
                    invRow.get("UNIT_NUMBER"));
                lockingBean.setUserID((String)
                    invRow.get("UPDATE_USER"));                
                lockingBean.setUpdateTimestamp((Timestamp)
                    invRow.get("UPDATE_TIMESTAMP"));
                lockingBean.setCreateTimestamp((Timestamp)
                    invRow.get("CREATE_TIMESTAMP"));
                lockingBean.setGotLock(invRow.get("GOT_LOCK") == null ? false:(invRow.get("GOT_LOCK").toString().equals("1") ? true : false));
            }          
        }        
        boolean gotLock = lockingBean.isGotLock();
        String userId = lockingBean.getUserID();
//        String lockId = lockingBean.getLockID();
        String acType = "I";
        lockingBean.setAcType(acType);
            if(!(gotLock)){
                String module = lockingBean.getLockID();
                StringBuffer moduleName = new StringBuffer(module);
                moduleName.delete(0, 4);
                Vector vcParam = new Vector();
                vcParam.addElement(new Parameter("USER_ID",
                    DBEngineConstants.TYPE_STRING, lockingBean.getUserID()));
                /* Calling procedure DW_GET_USER to get USER_NAME
                 * which has to be displayed at Client side
                 */
                if(dbEngine !=null){            
                    result=new Vector(3,2);
                    //Commented for displaying username for user id enhancement - starts
//                    result = dbEngine.executeRequest("Coeus",
//                    "call DW_GET_USER ( <<USER_ID>>, <<OUT RESULTSET rset>> )",
//                    "Coeus", vcParam);
                    //Commented for displaying username for user id enhancement - ends
                    //Added for displaying username for user id enhancement - starts
                    /* Calling function FN_GET_USER_NAME to get USER_NAME
                     * which has to be displayed at Client side
                     */
                    result = dbEngine.executeFunctions("Coeus",
                    "{<<OUT STRING USER_NAME>>=call FN_GET_USER_NAME ( "
                            + " << USER_ID >>)}", vcParam);
                    //Added for displaying username for user id enhancement - ends
                }else{
                    throw new CoeusException("db_exceptionCode.1000");   
                }
                //Commented for displaying username for user id enhancement - starts
//                int vcSize=result.size();
//                String userName = "";
//                if(vcSize > 0){
//                    for(int index = 0; index < vcSize; index++){
//                        HashMap hmUserData=(HashMap)result.elementAt(index);
//
//                        userName = (String)hmUserData.get("USER_NAME");
//                    }
//                }
                //Commented for displaying username for user id enhancement - ends
                //Added for displaying username for user id enhancement - starts
                String userName = userId;
                if(!result.isEmpty()){                
                        HashMap hmUserData=(HashMap)result.elementAt(0);
                        userName = (String)hmUserData.get("USER_NAME");
                        userName = (userName==null || userName.equals(""))? userId :userName ;
                }
                //Added for displaying username for user id enhancement - ends
//                StringBuffer strBuffModuleName = new StringBuffer(module);
                String strModName = new String();
                String strNumber = new String();
                strModName = moduleName.substring(0,moduleName.indexOf("_"));
                strNumber = module.substring(module.indexOf("_")+1);

                
                strModName.toString();
                strNumber.toString();                

                String msg = "";
                CoeusMessageResourcesBean coeusMessageResourcesBean
                    =new CoeusMessageResourcesBean();
                if(userName == ""){                    
                    //msg = "Some other application is using "+strModName+" "+strNumber; 
                    /* Getting message for coeusMessage.properties file
                     */
                    msg = coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1001")+" "+strModName+" "+strNumber; 
                }else{                    
                    //msg = userName +" is using "+strModName+" "+strNumber; 
                    /* Getting message for coeusMessage.properties file
                     */
                    msg = userName+" "+coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1000")+" "+strModName+" "+strNumber; 
                }
                /* Rollback the connection if the user fails to get 
                 * lock
                 */                  
                dbEngine.rollback(conn);
                throw new LockingException(msg);
            }
        return lockingBean;
    }
    
    
    
    // New locking method - END
    public void releaseEdit(String rowId, String loggedinUser)
               throws CoeusException, DBException{
        
        dbEngine=new DBEngineImpl();
        Vector result=null;
        Vector param=new Vector();
        HashMap invRow=null;
//        int number=0;
        param.addElement(new Parameter("LOCK_ID",
           DBEngineConstants.TYPE_STRING, rowId));        
        param.addElement(new Parameter("UPDATE_USER",
           DBEngineConstants.TYPE_STRING, loggedinUser.toUpperCase()));        
        if(dbEngine !=null){
            result=new Vector(3,2);            
            result=dbEngine.executeFunctions("Coeus",
             "{ <<OUT INTEGER NUMBER>> = call PKG_LOCK.FN_NEW_RELEASE_LOCK( <<LOCK_ID>>,<<UPDATE_USER>> ) } ", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");   
        }     
        
        int listSize=result.size();
        if(listSize>0){
            invRow=(HashMap)result.elementAt(0); 
//            number = Integer.parseInt(invRow.get("NUMBER").toString());
        }    
           // return number;
        }  
    
    // New releaseEdit method written for bug fixing -- BEGIN
    
    public LockingBean releaseLock(String rowId, String loggedinUser)
               throws CoeusException, DBException{
        
        dbEngine=new DBEngineImpl();
        Vector result=null;
        Vector param=new Vector();
        HashMap invRow=null;
//        int number=0;
        // Getting DB timestamp
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        Timestamp dbTimestamp = coeusFunctions.getDBTimestamp();
        param.addElement(new Parameter("LOCK_ID",
           DBEngineConstants.TYPE_STRING, rowId));        
        param.addElement(new Parameter("UPDATE_USER",
           DBEngineConstants.TYPE_STRING, loggedinUser.toUpperCase())); 
        // Code added for bug fixing while releasing lock
        LockingBean lockingBean = new LockingBean();
        boolean lockCheck = lockAvailabilityCheck(rowId,loggedinUser);
        if(!lockCheck){
            if(dbEngine !=null){
                result=new Vector(3,2);            
                result=dbEngine.executeFunctions("Coeus",
                 "{ <<OUT INTEGER NUMBER>> = call PKG_LOCK.FN_NEW_RELEASE_LOCK( <<LOCK_ID>>,<<UPDATE_USER>> ) } ", param);
            }else{
                throw new CoeusException("db_exceptionCode.1000");   
            }     

            int listSize=result.size();
            if(listSize>0){
                invRow=(HashMap)result.elementAt(0); 
//                number = Integer.parseInt(invRow.get("NUMBER").toString());
            }                                
               lockingBean.setLockID(rowId);
               String acType = "D";
               lockingBean.setAcType(acType);
               lockingBean.setUpdateTimestamp(dbTimestamp);
               boolean success = true;
               lockingBean.setGotLock(success);
        }else{ 
               String sbLockId = new String(rowId);
               lockingBean.setModuleName(sbLockId.substring(4,sbLockId.indexOf("_")));
               lockingBean.setLockID(sbLockId.substring(sbLockId.indexOf("_")+1));
               String acType = "E";
               lockingBean.setAcType(acType);
               lockingBean.setUpdateTimestamp(dbTimestamp);
               boolean success = true;
               lockingBean.setGotLock(success);               
               
//                String msg = "Sorry,  the lockId  "+rowId+"  has been deleted by Administrator ";
//                throw new LockingException(msg);
        }    
           return lockingBean;
        }
    
    // New releaseEdit method written for bug fixing -- END
    
    
       public boolean lockAvailabilityCheck(String rowId, String loggedinUser)
       throws CoeusException, DBException{
           boolean success = false;
           try {
               dbEngine=new DBEngineImpl();
               Vector result=null;
               Vector param=new Vector();
               HashMap invRow=null;
               int number=0;
               param.addElement(new Parameter("LOCK_ID",
               DBEngineConstants.TYPE_STRING, rowId));
               param.addElement(new Parameter("UPDATE_USER",
               DBEngineConstants.TYPE_STRING, loggedinUser.toUpperCase()));
               if(dbEngine !=null){
                   result=new Vector(3,2);
                   result=dbEngine.executeFunctions("Coeus",
                   "{ <<OUT INTEGER NUMBER>> = call PKG_LOCK.FN_LOCK_AVAILABILITY_CHECK( <<LOCK_ID>>, <<UPDATE_USER>> ) } ", param);
               }else{
                   throw new CoeusException("db_exceptionCode.1000");
               }
               int listSize=result.size();
               if(listSize>0){
                   invRow=(HashMap)result.elementAt(0);
                   number = Integer.parseInt(invRow.get("NUMBER").toString());
               }
               if(number == 1){
                   success = true;
               }else{
                   success = false;
               }
           }catch (DBException dbException) {
               throw new CoeusException(dbException.getMessage());
           }finally{
               if(dbEngine!= null){
                   dbEngine.endTxn(conn);
               }
           }
           return success;
       }
       
    // Added by Shivakumar -end    
    
    
//    private void initializeTimer() {
//        Timer releaseTimer = new Timer();
//        int waitInterval = DBEngineConstants.getLockInterval();
//        releaseTimer.scheduleAtFixedRate(new PollTask(this,waitInterval), 20, 60*1000 );
//    }
//    
//    /**
//     * Inner class to execute task(sending GACK meesage to mq server)  repetietively
//     */
//    class PollTask extends TimerTask {
//        private long currentTime;
//        private int waitInterval=15;
//        private TransactionMonitor transMonitor;
//        public PollTask(TransactionMonitor transMonitor,int waitInterval){
//            this.waitInterval = waitInterval;
//            this.transMonitor = transMonitor;
//        }
//        public void run() {
//            try{
//                long currentTime = System.currentTimeMillis();
//                Hashtable locks = transMonitor.getLockedRows();
//                Enumeration allKeys = locks.keys();
//                while (allKeys.hasMoreElements()) {
//                    String refId = (String)allKeys.nextElement();
//                    Long lockedTime = (Long)locks.get(refId);
////                    System.out.println("locked time=>"+lockedTime);
//                    long waitingTime = waitInterval * 60 *1000;
//  //                  System.out.println("wait time=>"+waitingTime);
//                    if ((currentTime - lockedTime.longValue()) > waitingTime) {
//                        transMonitor.releaseEdit(refId);
//                    }
//                }
//            }catch(Exception reminderEx){
//                reminderEx.printStackTrace();
//            }
//        }
//    }
    
       public boolean isLockAvailable(String rowId)
       throws CoeusException, DBException{
           boolean success = false;
           try {
               dbEngine=new DBEngineImpl();
               Vector result=null;
               Vector param=new Vector();
               HashMap invRow=null;
               int number=0;
               param.addElement(new Parameter("LOCK_ID",
               DBEngineConstants.TYPE_STRING, rowId));
               
               if(dbEngine !=null){
                   result=new Vector(3,2);
                   result=dbEngine.executeFunctions("Coeus",
                   "{ <<OUT INTEGER NUMBER>> = call PKG_LOCK.FN_IS_LOCK_AVAILABLE( <<LOCK_ID>> ) } ", param);
               }else{
                   throw new CoeusException("db_exceptionCode.1000");
               }
               int listSize=result.size();
               if(listSize>0){
                   invRow=(HashMap)result.elementAt(0);
                   number = Integer.parseInt(invRow.get("NUMBER").toString());
               }
               if(number == 1){
                   success = true;
               }else{
                   success = false;
               }
           }catch (DBException dbException) {
               throw new CoeusException(dbException.getMessage());
           }finally{
               if(dbEngine!= null){
                   dbEngine.endTxn(conn);
               }
           }
               return success;
           }
       
//    public static void main(String args[]){
//        try{
//            TransactionMonitor transactionMonitor = new TransactionMonitor();            
//        }catch(Exception ex){
//            ex.printStackTrace();
//        }
//    }
    
}
