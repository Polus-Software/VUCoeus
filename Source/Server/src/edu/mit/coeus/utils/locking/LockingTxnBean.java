/*
 * LockingTxnBean.java
 *
 * Created on July 22, 2004, 2:26 PM
 */

/* PMD check performed, and commented unused imports and variables on 19th Jan 2009
 * by Sreenath
 */

package edu.mit.coeus.utils.locking;

/**
 *
 * @author  shivakumarmj
 */
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.locking.LockingBean;
import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.utils.dbengine.DBEngineConstants;
import edu.mit.coeus.utils.dbengine.TransactionMonitor;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.CoeusVector;
//import edu.mit.coeus.utils.ComboBoxBean;
//import edu.mit.coeus.bean.FrequencyBean;
////import edu.mit.coeus.subcontract.bean.SubContractTxnBean;
//import edu.mit.coeus.bean.CommentTypeBean;
//import edu.mit.coeus.utils.query.Equals;
//import edu.mit.coeus.utils.query.And;
//import edu.mit.coeus.departmental.bean.DepartmentPersonTxnBean;
//import edu.mit.coeus.utils.dbengine.ProcReqParameter;
import java.util.Vector;
import java.util.HashMap;
import java.util.Hashtable;
//import java.util.Date;
import java.util.Set;
import java.util.Iterator;
//import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * This class contains implementation of Locking related Procedures
 * 
 * @author  Prasanna Kumar
 * @version :1.0 June 29, 2004 12:22 PM
 */
public class LockingTxnBean {
    
    // holds the dataset name
    private static final String DSN = "Coeus";    
    // Instance of a dbEngine
    private DBEngineImpl dbEngine;
    
//    private Hashtable htTest;
    
    //private DBEngineImpl dbEngine1;
    
    private Timestamp dbTimestamp;
    
    // holds the userId for the logged in user
    private String userId;
    
    /** Creates a new instance of LockingTxnBean */
    public LockingTxnBean() {
        dbEngine = new DBEngineImpl();
//        htTest = new Hashtable();
    }
    
    /**
     * Creates new LockingTxnBean and initializes userId.
     * @param userId String which the Loggedin userid
     */
    public LockingTxnBean(String userId) {
        this.userId = userId;
        dbEngine = new DBEngineImpl();        
    }
    /** The following method is used to get Award lock. 
     * This method takes the required parameters from LockingBean        
     * @param lockingBean of LockingBean
     * @throws CoeusException s an Exception class, which is used to represnt any exception comes
     * in COEUS web module
     * @throws DBException is an Exception class, which is used to handle exceptions in dbEngine package 
     * during SQL Command execution
     * @return type is lockingBean
     */    
     public LockingBean getAwardLock(LockingBean lockingBean)
            throws CoeusException ,DBException{
        
        dbEngine=new DBEngineImpl();
        Vector result=null;
        Vector param=new Vector();
        HashMap invRow=null;
        param.addElement(new Parameter("MODULE_NAME",
           DBEngineConstants.TYPE_STRING, lockingBean.getModuleName()));
        param.addElement(new Parameter("KEY1",
           DBEngineConstants.TYPE_STRING, lockingBean.getKey1()));        
        param.addElement(new Parameter("KEY2",
           DBEngineConstants.TYPE_STRING, lockingBean.getKey2()));                
        param.addElement(new Parameter("USER_ID",
           DBEngineConstants.TYPE_STRING, lockingBean.getUserID()));
        param.addElement(new Parameter("UNIT_NUMBER",
           DBEngineConstants.TYPE_STRING, lockingBean.getUnitNumber()));
        
        if(dbEngine !=null){
            result=new Vector(3,2);
            result=dbEngine.executeRequest("Coeus",
              "call PKG_LOCK.GET_LOCK( <<MODULE_NAME>>, <<KEY1>>, <<KEY2>>, <<USER_ID>>, <<UNIT_NUMBER>>, "+
              "<<OUT RESULTSET rset>>)", "Coeus", param);
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
//                int returnCode = invRow.get("GOT_LOCK") == null ? 0 : Integer.parseInt(invRow.get("GOT_LOCK").toString());

                lockingBean.setGotLock(invRow.get("GOT_LOCK") == null ? false:(invRow.get("GOT_LOCK").toString().equals("1") ? true : false));
//                lockingBean.setGotLock(
//                          invRow.get("GOT_LOCK") == null ? 0 : Integer.parseInt(invRow.get("GOT_LOCK").toString()));           
            }          
        }    
        return lockingBean;
    }          
     
     /**
      * The following method is used update the existing Award locks present in the 
      * OSP$LOCK table.
      * @param lockingBean of LockingBean
      * @throws CoeusException is an Exception class, which is used to represnt any exception comes
      * in COEUS web module
      * @throws DBException is an Exception class, which is used to handle exceptions in dbEngine package 
      * during SQL Command execution.
      * @return type is integer variable number
      */     
     public int updAwardLock(LockingBean lockingBean)
            throws CoeusException ,DBException{
        
        dbEngine=new DBEngineImpl();
        Vector result=null;
        Vector param=new Vector();
        HashMap invRow=null;
        int number=0;
        param.addElement(new Parameter("MODULE_NAME",
           DBEngineConstants.TYPE_STRING, lockingBean.getModuleName()));
        param.addElement(new Parameter("KEY1",
           DBEngineConstants.TYPE_STRING, lockingBean.getKey1()));        
        param.addElement(new Parameter("KEY2",
           DBEngineConstants.TYPE_STRING, lockingBean.getKey2()));                
        param.addElement(new Parameter("USER_ID",
           DBEngineConstants.TYPE_STRING, lockingBean.getUserID()));     
              
        if(dbEngine !=null){
            result=new Vector(3,2);
            result=dbEngine.executeFunctions("Coeus",
             "{ <<OUT INTEGER NUMBER>> = call PKG_LOCK.FN_UPD_LOCK( <<MODULE_NAME>>,"
             +" <<KEY1>>, <<KEY2>>, <<USER_ID>>) } ", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");   
        }     
        
        int listSize=result.size();
        if(listSize>0){
            invRow=(HashMap)result.elementAt(0); 
            number = Integer.parseInt(invRow.get("NUMBER").toString());
        }    
            return number;
        }  
     
     /* 
      */ 
     
     /**
      * The following method is used to release the Award locks 
      * @param lockingBean of LockingBean
      * @throws CoeusException s an Exception class, which is used to represnt 
      * any exception comes in COEUS web module
      * @throws DBException is an Exception class, which is used to handle 
      * exceptions in dbEngine package during SQL Command execution
      * @return type is integer number
      */     
     public int releaseAwardLock(LockingBean lockingBean)
            throws CoeusException ,DBException{
        
        dbEngine=new DBEngineImpl();
        Vector result=null;
        Vector param=new Vector();
        HashMap invRow=null;
        int number=0;
        param.addElement(new Parameter("MODULE_NAME",
           DBEngineConstants.TYPE_STRING, lockingBean.getModuleName()));
        param.addElement(new Parameter("KEY1",
           DBEngineConstants.TYPE_STRING, lockingBean.getKey1()));        
        param.addElement(new Parameter("KEY2",
           DBEngineConstants.TYPE_STRING, lockingBean.getKey2()));                     
              
        if(dbEngine !=null){
            result=new Vector(3,2);            
            result=dbEngine.executeFunctions("Coeus",
             "{ <<OUT INTEGER NUMBER>> = call PKG_LOCK.FN_RELEASE_LOCK( <<MODULE_NAME>>,"
             +" <<KEY1>>, <<KEY2>>) } ", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");   
        }     
        
        int listSize=result.size();
        if(listSize>0){
            invRow=(HashMap)result.elementAt(0); 
            number = Integer.parseInt(invRow.get("NUMBER").toString());
        }    
            return number;
        }  
     
     /**
      * @param lockingBean is throws input
      * @throws CoeusException s an Exception class, which is used to represnt 
      * any exception comes in COEUS web module
      * @throws DBException is an Exception class, which is used to handle 
      * exceptions in dbEngine package during SQL Command execution
      * @return type is lockingBean
      */     
     public LockingBean getUpdateTimestamp(LockingBean lockingBean)
            throws CoeusException,DBException{
        
        dbEngine=new DBEngineImpl();
        Vector result=null;
        Vector param=new Vector();
        HashMap invRow=null;
        param.addElement(new Parameter("LOCK_ID",
           DBEngineConstants.TYPE_STRING, lockingBean.getLockID()));
        if(dbEngine !=null){
            result=new Vector(3,2);
            result=dbEngine.executeRequest("Coeus",
              "call GET_AWARD_LOCK_TIMESTAMP(<<LOCK_ID>>,<<OUT RESULTSET rset>>)", "Coeus", param);
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
                lockingBean.setLockID((String)
                    invRow.get("LOCK_ID"));
                lockingBean.setUpdateTimestamp((Timestamp)
                    invRow.get("UPDATE_TIMESTAMP"));
            }    
        }
        return lockingBean;
        
        
     }    
     
     
     /**
      * @param htLockIds is the input
      * @throws CoeusException s an Exception class, which is used to represnt 
      * any exception comes in COEUS web module
      * @throws DBException is an Exception class, which is used to handle 
      * exceptions in dbEngine package during SQL Command execution
      * @return type is hashtable
      */     
     public Hashtable updAwardLockBulk(Hashtable htLockIds)
            throws CoeusException, DBException{
        
        dbEngine=new DBEngineImpl();
        Vector result=null;
        Vector param=new Vector();
        HashMap invRow=null;
        Hashtable htUpdLockIds = new Hashtable();
//        LockingBean lockingBean = new LockingBean(); 
        int number=0;
//        int htSize = htLockIds.size();        
//        java.util.Enumeration enuHtLockIds = htLockIds.keys();                
//        String strKey=null;
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        dbTimestamp = coeusFunctions.getDBTimestamp();          
        
        String keyString = null;
        Set htSet = htLockIds.keySet();
        Iterator htItr = htSet.iterator();
        while(htItr.hasNext()) {
            keyString = (String)htItr.next();            
            param.clear();
            param.addElement(new Parameter("LOCK_ID",
                DBEngineConstants.TYPE_STRING,keyString));   
            param.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));   
            try{
                if(dbEngine !=null){
                    result=new Vector();            
                    result=dbEngine.executeFunctions("Coeus","{ <<OUT INTEGER NUMBER>> = call PKG_LOCK.FN_NEW_UPD_LOCK( <<LOCK_ID>>,<<UPDATE_TIMESTAMP>>) } ", param);

                }else{
                    throw new CoeusException("db_exceptionCode.1000");
                }

                int listSize=result.size();
                if(listSize>0){
                    invRow=(HashMap)result.elementAt(0); 
                    number = Integer.parseInt(invRow.get("NUMBER").toString());                    
                    if(number == -1){
                        if(htUpdLockIds != null) {            
                          String code = "UPDATION_FAILED";
                          htUpdLockIds.put(keyString,code);    
                        }  
                    }
                    else{
                        if(htUpdLockIds != null) {            
                           htUpdLockIds.put(keyString,dbTimestamp);    
                        } 
                    }    
                }  
            }catch(Exception ex){
//                ex.printStackTrace();
                UtilFactory.log(ex.getMessage(),ex,"LockingTxnBean", "updAwardLockBulk");
                if(htUpdLockIds != null) {            
                      String code = "UPDATION_FAILED";
                      htUpdLockIds.put(keyString,code);    
                }  
            }    
        }   
        return htUpdLockIds;
     }
     
     /**
      * @throws CoeusException s an Exception class, which is used to represnt 
      * any exception comes in COEUS web module
      * @throws DBException is an Exception class, which is used to handle 
      * exceptions in dbEngine package during SQL Command execution
      * @return type is the input
      */     
     public CoeusVector getAllLockIds() throws CoeusException, DBException {         
         Vector result=null;
         boolean incompatibleLocks = false;
         Vector param=new Vector();
         HashMap hmLockIds = null;
         CoeusVector cvLockIds = new CoeusVector();
         LockingBean lockingBean = null;
         if(dbEngine !=null){
            result=new Vector(3,2);
            result=dbEngine.executeRequest("Coeus",
              "call PKG_LOCK.GET_LOCK_IDS( <<OUT RESULTSET rset>> )", "Coeus", param);
         }else{
            throw new CoeusException("db_exceptionCode.1000");   
         }
         int recSize = result.size();
         if(recSize > 0){
             //Commented bcoz, if there is no locks, null pointer exception was throwing
//            cvLockIds = new CoeusVector();
            for(int count=0;count<recSize;count++){
                lockingBean = new LockingBean();
                hmLockIds = (HashMap)result.elementAt(count);
                String lockID = (String)hmLockIds.get("LOCK_ID");
                String sbLockId = new String(lockID);
                int index = sbLockId.indexOf("_");
                if(index != -1) { 
                    lockingBean.setModuleName(sbLockId.substring(4,index));
                    lockingBean.setLockID(sbLockId.substring(index+1));
                } else {
                    incompatibleLocks = true;
                    continue;
                }
                lockingBean.setCreateTimestamp((Timestamp)hmLockIds.get("CREATE_TIMESTAMP"));                
                lockingBean.setUserID((String)hmLockIds.get("UPDATE_USER")); 
                /*
                 * UserId to UserName Enhancement - Start
                 * Added new property for userid to username enhancement.
                 */
                if(hmLockIds.get("USERNAME") != null) {
                    lockingBean.setUpdateUserName((String)hmLockIds.get("USERNAME"));
                } else {
                    lockingBean.setUpdateUserName((String)hmLockIds.get("UPDATE_USER")); 
                }
                //UserId to UserName Enhancement - End
                lockingBean.setUpdateTimestamp((Timestamp)hmLockIds.get("UPDATE_TIMESTAMP"));
                cvLockIds.addElement(lockingBean);
            }
         }
         cvLockIds.addElement(new Boolean(incompatibleLocks));
         return cvLockIds;         
     } 
     
     /**
      * @param lockId is the inout
      * @throws CoeusException s an Exception class, which is used to represnt 
      * any exception comes in COEUS web module
      * @throws DBException is an Exception class, which is used to handle 
      * exceptions in dbEngine package during SQL Command execution
      * @return type is integer
      */     
     public LockingBean deleteLockId(String lockId, String userId) throws CoeusException, DBException {
         Vector result=null;
         Vector param=new Vector();
         HashMap hmLockIds = null;
         LockingBean lockingBean = null;
//         boolean status = false;
         int number = 0;
         param.addElement(new Parameter("LOCK_ID",
                DBEngineConstants.TYPE_STRING,lockId)); 
         param.addElement(new Parameter("USER_ID",
                DBEngineConstants.TYPE_STRING,userId)); 
         if(dbEngine !=null){
            result=new Vector(3,2);            
            result=dbEngine.executeFunctions("Coeus",
             "{ <<OUT INTEGER NUMBER>> = call PKG_LOCK.FN_DELETE_LOCK( <<LOCK_ID>> , <<USER_ID>> ) } ", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");   
        }
        int listSize=result.size();
        if(listSize>0){
            hmLockIds = (HashMap)result.elementAt(0); 
            number = Integer.parseInt(hmLockIds.get("NUMBER").toString());
        } 
        lockingBean = new LockingBean();
        if(number == 1){            
            lockingBean.setLockID(lockId);
            String acType = "D";
            lockingBean.setAcType(acType);
//            status = true;
        }else{
            lockingBean.setLockID(lockId);
            String acType = "E";
            lockingBean.setAcType(acType);
//            status = false;
        }    
        return lockingBean;
     }    
    public static void main(String args[]){
        try{
            LockingTxnBean lockingTxnBean=new LockingTxnBean();
            CoeusVector cvLockIds = lockingTxnBean.getAllLockIds();
//            System.out.println("Vector size "+cvLockIds.size());
        }catch(Exception ex){
//            ex.printStackTrace();
            UtilFactory.log(ex.getMessage(),ex,"LockingTxnBean", "deleteLockId");
        }    
    }
    
    /**
     * Method to get Locked id details
     * @param rowId
     * @param userId
     * @throws CoeusException
     * @throws DBException
     * @return unit number
     */    
    public String getLockData(String rowId, String userId)throws 
            CoeusException, DBException{
        String unitNumber = "";
        dbEngine=new DBEngineImpl();
        HashMap invRow=null;
        Vector result=null;
        Vector param=new Vector();
        param.addElement(new Parameter("AV_LOCK_ID",
           DBEngineConstants.TYPE_STRING, rowId));
        TransactionMonitor transMon = new TransactionMonitor();
        boolean lockCheck = transMon.lockAvailabilityCheck(rowId, userId);
        if(!lockCheck){
            if(dbEngine !=null){
                result=new Vector(3,2);            
                result = dbEngine.executeRequest("Coeus", "call PKG_LOCK.GET_LOCK_DETAILS (  <<AV_LOCK_ID>> , <<OUT RESULTSET rset>> )   ", 
                        "Coeus", param);
            }else{
                throw new CoeusException("db_exceptionCode.1000");   
            }     

            int listSize=result.size();
            if(listSize>0){
                invRow=(HashMap)result.elementAt(0);
                unitNumber =(String) invRow.get("UNIT_NUMBER");
            }                                
        }
        return unitNumber;
    }    
    
    /**
     * Method to Delete all the Locks from the OSP$LOCK Table
     * 
     * 
     * @return boolean true if all the locks are lockDeleted succesfully
     * @throws CoeusException
     * @throws DBException
     */
    public boolean deleteAllLocks() throws DBException, CoeusException{
        Vector result=null;
        Vector param=new Vector();
        boolean lockDeleted = false;
        if(dbEngine !=null){
            result=dbEngine.executeFunctions("Coeus",
                    "{ <<OUT INTEGER NUMBER>> = call FN_DELETE_ALL_LOCKS } ", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(result != null && result.size() > 0){
            int success = -1;
            HashMap hmResult = (HashMap)result.elementAt(0);
            success = Integer.parseInt(hmResult.get("NUMBER").toString());
            
            if(success == 1){
                lockDeleted =  true;
            }
        }
        return lockDeleted;
    }
    
    /**
     * Method to Delete all the Expired Locks from the OSP$LOCK Table
     * 
     * 
     * @param deleteInterval the Expiry Time of the Locks (in minutes)
     * @return boolean true if all the expierd locks are lockDeleted succesfully
     * @throws CoeusException
     * @throws DBException
     */
    public boolean deleteExpiredLocks(long deleteInterval) throws DBException, CoeusException{
        Vector result=null;
        Vector param=new Vector();
        boolean lockDeleted = false;
        param.addElement(new Parameter("AV_DELETE_INTERVAL",
                DBEngineConstants.TYPE_INT, String.valueOf(deleteInterval)));
        if(dbEngine !=null){
            result=dbEngine.executeFunctions("Coeus",
                "{ <<OUT INTEGER NUMBER>> = call FN_DELETE_EXPIRED_LOCKS( <<AV_DELETE_INTERVAL>> ) } ", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(result != null && result.size() > 0){
            int success = -1;
            HashMap hmResult = (HashMap)result.elementAt(0);
            success = Integer.parseInt(hmResult.get("NUMBER").toString());
            if(success == 1){
                lockDeleted =  true;
            }
        }
        return lockDeleted;
    }
}


