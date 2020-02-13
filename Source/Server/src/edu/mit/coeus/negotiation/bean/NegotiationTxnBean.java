/*
 * NegotiationTxnBean.java
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * Created on July 14, 2004, 12:15 PM
 */
 /*
  * PMD check performed, and commented unused imports and variables on 11-APR-2011
  * by Maharaja Palanichamy
  */
package edu.mit.coeus.negotiation.bean;

import edu.mit.coeus.instprop.bean.InstituteProposalLogBean;
import edu.mit.coeus.instprop.bean.InstituteProposalTxnBean;
//import edu.mit.coeus.utils.KeyConstants;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.TransactionMonitor;
import edu.mit.coeus.utils.dbengine.DBEngineConstants;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.dbengine.ProcReqParameter;
import edu.mit.coeus.departmental.bean.*;
import edu.mit.coeus.customelements.bean.CustomElementsInfoBean;
import edu.mit.coeus.utils.mail.MailProperties;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.utils.locking.LockingBean;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;

import java.util.Vector;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.TreeSet;
import java.util.Comparator;
import java.sql.Timestamp;
import java.sql.Date;
import java.sql.Connection;

/**
 * This class contains implementation of all get procedures used in Negotiation.
 * @author  jobinelias
 */
public class NegotiationTxnBean {
	
	// Instance of a dbEngine
    private DBEngineImpl dbEngine;
    private static final String rowLockStr = "osp$Negotiation_";
    //COEUSQA-119 : View negotiations in lite - Start
    private static final String LAST_UPDATE_TIMESTAMP = "UPDATE_TIMESTAMP";
    private static final String LAST_UPDATE_USER = "UPDATE_USER";
    //COEUSQA-119 : End  
    
    
    // Creating instance of Connection for locking enhancement
    private Connection conn = null;
	
	// Instance of Transaction Monitor
	private TransactionMonitor transactionMonitor;
	
	// holds the dataset name
    private static final String DSN = "Coeus"; 
	
	private Timestamp dbTimestamp;
	
	// holds the userId for the logged in user
    private String userId;   

    // holds the message for negotiation mail
    private String message;
	
	/** Creates a new instance of NegotiationTxnBean */
	public NegotiationTxnBean() {
		dbEngine = new DBEngineImpl();
		transactionMonitor =  TransactionMonitor.getInstance();
	}
	
	public NegotiationTxnBean(String userId) {
		//this();
        this.userId = userId;
		dbEngine = new DBEngineImpl();
		transactionMonitor =  TransactionMonitor.getInstance();
        
    }
	
	 /**This method used to get the user id
      * <li>To fetch the data, it uses the function FN_GET_PI_USER_ID.
      *
      * @return String
      * @param proposalNumber String
      * @exception DBException if any error during database transaction.
      * @exception CoeusException if the instance of dbEngine is not available. 
      **/
     public String getUserId(String proposalNumber) throws CoeusException, DBException{
        String userId = null;
        Vector param= new Vector();
        Vector result = new Vector();
        param.add(new Parameter("PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING, ""+proposalNumber));
        
        /* calling stored function */
        if(dbEngine != null) {
            result = dbEngine.executeFunctions("Coeus",
            "{ <<OUT STRING USER_ID>> = "
            +" call FN_GET_PI_USER_ID(<<PROPOSAL_NUMBER>>) }", param);
        } else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            userId = (String)rowParameter.get("USER_ID");
        }
        return userId;
    } 
	 
	/**This method used to get the PERSON NAME with the person id
      * <li>To fetch the data, it uses the function FN_GET_PERSON_NAME.
      *
      * @return String
      * @param personId String
      * @exception DBException if any error during database transaction.
      * @exception CoeusException if the instance of dbEngine is not available. 
      **/
     public String getPersonName(String personId) throws CoeusException, DBException{
        String personName = null;
        Vector param= new Vector();
        Vector result = new Vector();
        param.add(new Parameter("PERSON_ID",
            DBEngineConstants.TYPE_STRING, ""+personId));
        
        /* calling stored function */
        if(dbEngine != null) {
            result = dbEngine.executeFunctions("Coeus",
            "{ <<OUT STRING GET_PERSON_NAME>> = "
            +" call FN_GET_PERSON_NAME(<<PERSON_ID>>) }", param);
        } else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            personName = (String)rowParameter.get("GET_PERSON_NAME");
        }
        return personName;
    } 
	
	 /**This method used to get the sponsor NAME with the SPONSOR id
      * <li>To fetch the data, it uses the function GET_SPONSOR_NAME.
      *
      * @return String
      * @param personId String
      * @exception DBException if any error during database transaction.
      * @exception CoeusException if the instance of dbEngine is not available. 
      **/
     public String getSponsorName(String sponsorId) throws CoeusException, DBException{
        String sponsorName = null;
        Vector param= new Vector();
        Vector result = new Vector();
        param.add(new Parameter("SPONSOR_ID",
            DBEngineConstants.TYPE_STRING, ""+sponsorId));
        
        /* calling stored function */
        if(dbEngine != null) {
            result = dbEngine.executeFunctions("Coeus",
            "{ <<OUT STRING SPONSOR_NAME>> = "
            +" call GET_SPONSOR_NAME(<<SPONSOR_ID>>) }", param);
        } else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            sponsorName = (String)rowParameter.get("SPONSOR_NAME");
        }
        return sponsorName;
    }
	 
	 /**This method used to get the UNIT NAME with the UNIT NUMBER
      * <li>To fetch the data, it uses the function FN_GET_UNIT_NAME.
      *
      * @return String unitName
      * @param leadUnitNumber String
      * @exception DBException if any error during database transaction.
      * @exception CoeusException if the instance of dbEngine is not available. 
      **/
     public String getUnitName(String leadUnitNumber) throws CoeusException, DBException{
        String unitName = null;
        Vector param= new Vector();
        Vector result = new Vector();
        param.add(new Parameter("UNIT_NUMBER",
            DBEngineConstants.TYPE_STRING, ""+leadUnitNumber));
        
        /* calling stored function */
        if(dbEngine != null) {
            result = dbEngine.executeFunctions("Coeus",
            "{ <<OUT STRING UNIT_NAME>> = "
            +" call FN_GET_UNIT_NAME(<<UNIT_NUMBER>>) }", param);
        } else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            unitName = (String)rowParameter.get("UNIT_NAME");
        }
        return unitName;
    }
	 
	 /**This method used to get the USER NAME with the USER ID
      * <li>To fetch the data, it uses the function FN_GET_USER_NAME.
      *
      * @return String userName
      * @param userId String
      * @exception DBException if any error during database transaction.
      * @exception CoeusException if the instance of dbEngine is not available. 
      **/
     public String getUserName(String userId) throws CoeusException, DBException{
        String userName = null;
        Vector param= new Vector();
        Vector result = new Vector();
        param.add(new Parameter("USER_ID",
            DBEngineConstants.TYPE_STRING, ""+userId));
        
        /* calling stored function */
        if(dbEngine != null) {
            result = dbEngine.executeFunctions("Coeus",
            "{ <<OUT STRING GET_USER_NAME>> = "
            +" call FN_GET_USER_NAME(<<USER_ID>>) }", param);
        } else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            userName = (String)rowParameter.get("GET_USER_NAME");
        }
        return userName;
    }
	 
	 /**This method used to get the UNIT NAME with the UNIT NUMBER
      * <li>To fetch the data, it uses the function FN_GET_UNITNAME.
      *
      * @return int negCount
      * @param proposalNumber String
      * @exception DBException if any error during database transaction.
      * @exception CoeusException if the instance of dbEngine is not available. 
      **/
     public int getNegotiationCount(String proposalNumber) throws CoeusException, DBException{
        int negCount = 0;
        Vector param= new Vector();
        Vector result = new Vector();
        param.add(new Parameter("PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING, ""+proposalNumber));
        
        /* calling stored function */
        if(dbEngine != null) {
            result = dbEngine.executeFunctions("Coeus",
            "{ <<OUT INTEGER GET_NEG_COUNT>> = "
            +" call fn_get_negotiation_count(<<PROPOSAL_NUMBER>>) }", param);
        } else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            negCount = Integer.parseInt(rowParameter.get("GET_NEG_COUNT").toString());
        }
        return negCount;
    }
	 /** This method get lock for given Negotiation number
      * This method locks the given Negotiation number
      *
      * @return boolean lockSuccess
      * @param negotiationNumber String
      * @exception DBException if any error during database transaction.
      * @exception CoeusException if the instance of dbEngine is not available.
      */     
     public boolean getNegotiatorLock(String negotiationNumber)
        throws CoeusException, DBException{
        boolean lockSuccess = false;
        String lockId = rowLockStr+negotiationNumber;
        if(transactionMonitor.canEdit(lockId)){
            lockSuccess = true;
        }else{
            //throw new CoeusException("exceptionCode.999999");
			 
            throw new CoeusException("Negotiation "+negotiationNumber +" is being modified by another user");
        }
        return lockSuccess;
     }
     
     // Code added by Shivakumar for locking enhancement - BEGIN     
     public LockingBean getNegotiatorLock(String negotiationNumber,String loggedinUser,String unitNumber)
        throws CoeusException, DBException{
        boolean lockSuccess = false;
        String lockId = rowLockStr+negotiationNumber;
//        if(dbEngine!=null){
//                  conn = dbEngine.beginTxn();
//        }else{
//            throw new CoeusException("db_exceptionCode.1000");
//        } 
              LockingBean lockingBean = new LockingBean();
              lockingBean = transactionMonitor.canEdit(lockId,loggedinUser,unitNumber);
              return lockingBean;
     }
     
     // Method to commit transaction
        public void transactionCommit() throws DBException{            
            dbEngine.commit(conn);
        }    
        
        // Method to rollback transaction
        public void transactionRollback() throws DBException{            
            dbEngine.rollback(conn);
        }    
        
        public void endConnection() throws DBException{
            dbEngine.endTxn(conn);
        }    
     // Code added by Shivakumar for locking enhancement - END
    /**
     *  The method used to release the lock of a particular Award
     *  @param rowId the id for lock to be released
     */
     public void releaseEdit(String negotiationNumber)
        throws CoeusException, DBException{
        String lockId = rowLockStr+negotiationNumber;
        transactionMonitor.releaseEdit(lockId);
     }
     // Code added by Shivakumar for locking enhancement - BEGIN
//     public void releaseEdit(String negotiationNumber,String loggedinUser)
//        throws CoeusException, DBException{
//        String lockId = rowLockStr+negotiationNumber;
//        transactionMonitor.releaseEdit(lockId,loggedinUser);
//     }
     // Calling releaseLock method to fix the bug in locking
     public LockingBean releaseLock(String negotiationNumber,String loggedinUser)
        throws CoeusException, DBException{
        String lockId = rowLockStr+negotiationNumber;
        LockingBean lockingBean = transactionMonitor.releaseLock(lockId,loggedinUser);
        return lockingBean;
     }
     
     // lockCheck method created for bug fixing
     public boolean lockCheck(String negotiationNumber, String loggedinUser)
        throws CoeusException, DBException{        
        String rowId = this.rowLockStr+negotiationNumber;
        boolean lockCheck = transactionMonitor.lockAvailabilityCheck(rowId,loggedinUser);
        return lockCheck;
     }
     // Code added by Shivakumar for locking enhancement - END
	 
	 /** Method used to get the Negotiation Activities for the given Negotiation Number.
     * <li>To fetch the data, it uses DW_GET_NEGOTIATION_ACTIVITIES.
     *
     * @return CoeusVector collection of negotiationActivitiesBean
     * @param negotiationNumber is used to get NegotiationActivitiesBean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
     //start case 1735 
//    public CoeusVector getNegotiationActivities(String negotiationNumber)
     //Modified for COEUSDEV-294 : Error adding activity to a negotiation - Start
//     public CoeusVector getNegotiationActivities(String negotiationNumber, String userID)
    //end case 1735 
       public CoeusVector getNegotiationActivities(String negotiationNumber, String userID, String leadUnit)
       //COEUSDEV-294 : End
       			throws CoeusException, DBException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap row = null;
        NegotiationActivitiesBean negotiationActivitiesBean = null;
        String fileName;
        NegotiationAttachmentBean attachmentBean = null;
        param.addElement(new Parameter("NEGOTIATION_NUMBER",
        DBEngineConstants.TYPE_STRING, negotiationNumber));
        //start case 1735 
        param.addElement(new Parameter("USER_ID","String",userID));
        //Added for  COEUSDEV-294 : Error adding activity to a negotiation - Start
        param.addElement(new Parameter("UNIT_NUMBER","String",leadUnit));
        //COEUSDEV-294 : End
//        if(dbEngine!=null){
//            result = dbEngine.executeRequest("Coeus",
//            "call GET_NEGOTIATION_ACTIVITIES ( <<NEGOTIATION_NUMBER>>,<<OUT RESULTSET rset>> )",
//            "Coeus", param);
        //Modified for  COEUSDEV-294 : Error adding activity to a negotiation - Start
//         if(dbEngine!=null){
//            result = dbEngine.executeRequest("Coeus",
//            "call GET_NEGOTIATION_ACTIVITIES ( <<NEGOTIATION_NUMBER>>, <<USER_ID>>,<<OUT RESULTSET rset>> )",
//            "Coeus", param);
        //end case 1735        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call GET_NEGOTIATION_ACTIVITIES ( <<NEGOTIATION_NUMBER>>, <<USER_ID>>, <<UNIT_NUMBER>>,<<OUT RESULTSET rset>> )",
            "Coeus", param);
        //COEUSDEV-294 : End
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
		
        CoeusVector negotiationActivitiesList = new CoeusVector();
        for(int index = 0; index < listSize; index++) {
            negotiationActivitiesBean = new NegotiationActivitiesBean();
                row = (HashMap)result.elementAt(index);
            negotiationActivitiesBean.setNegotiationNumber((String)row.get("NEGOTIATION_NUMBER"));
            negotiationActivitiesBean.setActivityNumber(
                row.get("ACTIVITY_NUMBER") == null ? 0 : Integer.parseInt(row.get("ACTIVITY_NUMBER").toString()));
            negotiationActivitiesBean.setDescription((String)row.get("DESCRIPTION"));
            negotiationActivitiesBean.setDocumentFileAddress((String)row.get("DOC_FILE_ADDRESS"));
            negotiationActivitiesBean.setNegotiationActTypeCode(
                row.get("NEGOTIATION_ACT_TYPE_CODE") == null ? 0 : Integer.parseInt(row.get("NEGOTIATION_ACT_TYPE_CODE").toString()));
			negotiationActivitiesBean.setActivityTypeDescription((String)row.get("ACT_TYPE_DESCRIPTION"));
			String boolValue = (String)row.get("RESTRICTED_VIEW");
			
			boolean isRestricted = false;
			if (boolValue.equals("Y")) {
				isRestricted = true;
			} else if (boolValue.equals("N")) {
				isRestricted = false;
			}
            negotiationActivitiesBean.setRestrictedView(isRestricted);      
			
			negotiationActivitiesBean.setCreateDate(
                row.get("CREATE_DATE") == null ?
                null : new Date(((Timestamp) row.get("CREATE_DATE")).getTime()));
			negotiationActivitiesBean.setActivityDate(
                row.get("ACTIVITY_DATE") == null ?
                null : new Date(((Timestamp) row.get("ACTIVITY_DATE")).getTime()));
				
			negotiationActivitiesBean.setFollowUpDate(
			row.get("FOLLOWUP_DATE") == null ?
                null : new Date(((Timestamp) row.get("FOLLOWUP_DATE")).getTime()));
            negotiationActivitiesBean.setUpdateTimestamp((java.sql.Timestamp)row.get("UPDATE_TIMESTAMP"));
			String updatedUser = (String)row.get("UPDATE_USER");
            negotiationActivitiesBean.setUpdateUser(updatedUser);   
			negotiationActivitiesBean.setLastUpdatedBy(getUserName(updatedUser));
             //Added with case 2806 - Upload attachments to negotiation - Start
//            negotiationActivitiesBean.setFileName((String)row.get("FILE_NAME"));
//            negotiationActivitiesBean.setAttachmentPresent((String)row.get("FILE_NAME") == null ? false : true);
            //Added with Case 4007: Icon based on Attachment Type
            fileName = (String)row.get("FILE_NAME");
            if(fileName!=null){
                attachmentBean = new NegotiationAttachmentBean();
                attachmentBean.setFileName(fileName);
                attachmentBean.setMimeType((String)row.get("MIME_TYPE"));
                negotiationActivitiesBean.setAttachmentBean(attachmentBean);
                negotiationActivitiesBean.setAttachmentPresent(true);
            }else{
                negotiationActivitiesBean.setAttachmentPresent(false);
            }
            //4007 End
            //Added with case 2806 - Upload attachments to negotiation - End  
            negotiationActivitiesList.addElement(negotiationActivitiesBean);
        }
        return negotiationActivitiesList;
    }
	
	/** Method used to get the custom columns of the module for the given module code.
     * <li>To fetch the data, it uses DW_GET_CUST_COLUMNS_FOR_MODULE
     *
     * @return CoeusVector collection of negotiationCustomElementsBean
     * @param moduleCode is used to get negotiationCustomElementsBean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getCustomColumnsForModule(String moduleCode)
			throws CoeusException, DBException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap row = null;
		
		NegotiationCustomElementsBean negotiationCustomElementsBean = null;
        
        param.addElement(new Parameter("MODULE_CODE",
        DBEngineConstants.TYPE_STRING, moduleCode));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call DW_GET_CUST_COLUMNS_FOR_MODULE ( <<MODULE_CODE>>, <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        CoeusVector customColumList = new CoeusVector();
        for(int index = 0; index < listSize; index++) {
            negotiationCustomElementsBean = new NegotiationCustomElementsBean();
                row = (HashMap)result.elementAt(index);
            negotiationCustomElementsBean.setNegotiationNumber((String)row.get("NEGOTIATION_NUMBER"));
            negotiationCustomElementsBean.setColumnName((String)row.get("COLUMN_NAME"));
            negotiationCustomElementsBean.setColumnValue((String)row.get("COLUMN_VALUE"));
			negotiationCustomElementsBean.setUpdateTimestamp((java.sql.Timestamp)row.get("UPDATE_TIMESTAMP"));
            negotiationCustomElementsBean.setUpdateUser((String)(row.get("UPDATE_USER")));
			String boolValue = (String)row.get("HAS_LOOKUP");
			boolean isRestricted = false;
			if (boolValue.equals("Y")) {
				isRestricted = true;
			} else if (boolValue.equals("N")) {
				isRestricted = false;
			}
			boolean isRequired = false;
			String boolRequired = (String)row.get("IS_REQUIRED");
			if (boolRequired.equals("Y")) {
				isRequired = true;
			} else if (boolRequired.equals("N")) {
				isRequired = false;
			}
            negotiationCustomElementsBean.setRequired(isRequired); 
			negotiationCustomElementsBean.setColumnLabel((String)row.get("COLUMN_LABEL"));            
            negotiationCustomElementsBean.setDataType((String)row.get("DATA_TYPE")); 
            negotiationCustomElementsBean.setDataLength(
			row.get("DATA_LENGTH") == null ? 0 : Integer.parseInt(row.get("DATA_LENGTH").toString()));
			negotiationCustomElementsBean.setDefaultValue((String)row.get("DEFAULT_VALUE"));   
			negotiationCustomElementsBean.setLookUpWindow((String)row.get("LOOKUP_WINDOW"));   
			negotiationCustomElementsBean.setLookUpArgument((String)row.get("LOOKUP_ARGUMENT"));
			customColumList.addElement(negotiationCustomElementsBean);
            
        }
        return customColumList;
    }
	
	/** Method used to get the negotiation info the given NEGOTIATION_NUMBER
     * <li>To fetch the data, it uses GET_NEGOTIATION_INFO procedure
     *
     * @return NegotiationInfoBean negotiationInfoBean
     * @param negotiationNumber is used to get negotiationInfoBean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public NegotiationInfoBean getNegotiationInfo(String negotiationNumber)
			throws CoeusException, DBException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap row = null;
        NegotiationInfoBean negotiationInfoBean = null;
        
        param.addElement(new Parameter("NEGOTIATION_NUMBER",
        DBEngineConstants.TYPE_STRING, negotiationNumber));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call GET_NEGOTIATION_INFO ( <<NEGOTIATION_NUMBER>>, <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        if(listSize > 0 ) {
            negotiationInfoBean = new NegotiationInfoBean();
            row = (HashMap)result.elementAt(0);
            negotiationInfoBean.setNegotiationNumber((String)row.get("NEGOTIATION_NUMBER"));
			String negotiatorId = (String)row.get("NEGOTIATOR_ID");
            negotiationInfoBean.setNegotiatorId(negotiatorId);
			negotiationInfoBean.setNegotiatorName(getPersonName(negotiatorId));
			negotiationInfoBean.setStartDate(
                row.get("START_DATE") == null ?
                null : new Date(((Timestamp) row.get("START_DATE")).getTime()));
             //Added for case 4185 - New field Closed Date - Start
             negotiationInfoBean.setClosedDate(
                row.get("CLOSED_DATE") == null ?
                null : new Date(((Timestamp) row.get("CLOSED_DATE")).getTime()));
             //Added for case 4185 - New field Closed Date - End
			negotiationInfoBean.setUpdateTimestamp((java.sql.Timestamp)row.get("UPDATE_TIMESTAMP"));
            negotiationInfoBean.setUpdateUser((String)(row.get("UPDATE_USER")));
			
            negotiationInfoBean.setStatusCode(
			row.get("NEGOTIATION_STATUS_CODE") == null ? 0 : Integer.parseInt(row.get("NEGOTIATION_STATUS_CODE").toString()));
			negotiationInfoBean.setDocFileAddress((String)row.get("DOC_FILE_ADDRESS"));   
			negotiationInfoBean.setNegotiatorId((String)row.get("NEGOTIATOR_ID"));   
			negotiationInfoBean.setStatusDescription((String)row.get("DESCRIPTION"));
           //case 3590 start
           
           //COEUSDEV - 733 Create a new notification for negotiation module - Start
            negotiationInfoBean.setAwStatusCode(
                    row.get("NEGOTIATION_STATUS_CODE") == null ? 0 : Integer.parseInt(row.get("NEGOTIATION_STATUS_CODE").toString()));
           //COEUSDEV - 733 Create a new notification for negotiation module - End         
            
           negotiationInfoBean.setNegotiationAgreeTypeCode(
		row.get("NEGOTIATION_AGREE_TYPE_CODE") == null ? 0 : Integer.parseInt(row.get("NEGOTIATION_AGREE_TYPE_CODE").toString()));
           negotiationInfoBean.setProposedStartDate(
                row.get("PROPOSED_START_DATE") == null ? null : new Date(((Timestamp) row.get("PROPOSED_START_DATE")).getTime()));
           negotiationInfoBean.setPrimeSponsorCode((String)row.get("PRIME_SPONSOR_CODE"));  
           negotiationInfoBean.setPrimeSponsorName((String)row.get("SPONSOR_NAME")); 
           negotiationInfoBean.setNegotiationAgreeTypeDescription((String)row.get("NEGO_AGREE_TYPE_DESCRIPTION"));                 
           //case 3590 end             
			            
        }
        return negotiationInfoBean;
    }
	
	/** Method used to get the negotiation info the given NEGOTIATION_NUMBER
     * <li>To fetch the data, it uses GET_NEGOTIATION_INFO procedure
     *
     * @return NegotiationHeaderBean negotiationHeaderBean
     * @param negotiationNumber is used to get negotiationHeaderBean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public NegotiationHeaderBean getNegotiationHeader(String negotiationNumber)
			throws CoeusException, DBException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap row = null;
        NegotiationHeaderBean negotiationHeaderBean = null;        
        
        param.addElement(new Parameter("NEGOTIATION_NUMBER",
        DBEngineConstants.TYPE_STRING, negotiationNumber));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call GET_NEGOTIATION_HEADER ( <<NEGOTIATION_NUMBER>>, <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        if (listSize > 0) {
            negotiationHeaderBean = new NegotiationHeaderBean();
            row = (HashMap)result.elementAt(0);
            negotiationHeaderBean.setNegotiationNumber((String)row.get("NEGOTIATION_NUMBER"));
            negotiationHeaderBean.setPiID((String)row.get("PI_ID"));
            negotiationHeaderBean.setPiName((String)row.get("PI_NAME"));
			String sponsorCode = (String)row.get("SPONSOR_CODE");
			negotiationHeaderBean.setSponsorCode(sponsorCode);
                        
                        /**Bug Fix Case Id 1864 Start
                         */
                        if(sponsorCode == null){
                            sponsorCode = "";
                        }
                        if(sponsorCode.trim().equals("") ){
                            negotiationHeaderBean.setSponsorName("");
                        }else{
                            negotiationHeaderBean.setSponsorName(getSponsorName(sponsorCode));
                        }
                        //negotiationHeaderBean.setSponsorName(getSponsorName(sponsorCode));
                        /**Bug Fix Case Id 1864 End
                         */
                        
                        //case 3961 start
                        sponsorCode = (String)row.get("PRIME_SPONSOR_CODE");
			negotiationHeaderBean.setPrimeSponsorCode(sponsorCode);
                        
                        if(sponsorCode == null){
                            sponsorCode = "";
                        }
                        if(sponsorCode.trim().equals("") ){
                            negotiationHeaderBean.setPrimeSponsorName("");
                        }else{
                            negotiationHeaderBean.setPrimeSponsorName(getSponsorName(sponsorCode));
                        }
                        //case 3961 end 
			String initialContractId = (String)row.get("INITIAL_CONTRACT_ADMIN");
			negotiationHeaderBean.setInitialContractAdmin(getPersonName(initialContractId));
                        negotiationHeaderBean.setUpdateTimestamp((java.sql.Timestamp)row.get("UPDATE_TIMESTAMP"));
                        negotiationHeaderBean.setUpdateUser((String)(row.get("UPDATE_USER")));
                        
                        //COEUSQA-119 : View negotiations in lite - Start                        
                        HashMap hmNegotiationUpdateDetails = getNegotiationUpdateDetails(negotiationNumber);
                        if(hmNegotiationUpdateDetails != null && hmNegotiationUpdateDetails.size() > 0){
                            negotiationHeaderBean.setLastUpdateTimestamp((Timestamp) hmNegotiationUpdateDetails.get(LAST_UPDATE_TIMESTAMP));
                            negotiationHeaderBean.setLastUpdateUser((String) hmNegotiationUpdateDetails.get(LAST_UPDATE_USER));
                        }
                        //COEUSQA-119 : End
			
            negotiationHeaderBean.setProposalTypeCode(
			row.get("PROPOSAL_TYPE_CODE") == null ? 0 : Integer.parseInt(row.get("PROPOSAL_TYPE_CODE").toString()));
			negotiationHeaderBean.setProposalNumber((String)row.get("PROPOSAL_NUMBER"));   
			negotiationHeaderBean.setTitle((String)row.get("TITLE")); 
			String leadUnitNumber = (String)row.get("LEAD_UNIT");
			negotiationHeaderBean.setLeadUnit(leadUnitNumber);
			//getting the lead unit number and by passing that will get the unit name.
			negotiationHeaderBean.setUnitName(getUnitName(leadUnitNumber));
			//have to clarify
			negotiationHeaderBean.setProposalTypeDescription((String)row.get("DESCRIPTION"));
			
        }
        return negotiationHeaderBean;
    }
    
     //COEUSQA-119 : View negotiations in lite - Start
    /** Method used to get the Negotiation Update User and Timestamp
     *  details
     *
     */
    public HashMap getNegotiationUpdateDetails(String negotiationNumber)
    throws CoeusException, DBException {
        Vector resultDetails = new Vector(3,2);
        Vector param= new Vector();
        HashMap rowDetails = null;
        HashMap hmResult = null;
        param.addElement(new Parameter("NEGOTIATION_NUMBER",
                DBEngineConstants.TYPE_STRING, negotiationNumber));
        if(dbEngine!=null){
            resultDetails = dbEngine.executeRequest("Coeus",
                    "call GET_NEGOTIATION_UPDATE_DETAILS( <<NEGOTIATION_NUMBER>>, <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSizeDetails = resultDetails.size();
        if (listSizeDetails > 0) {
            rowDetails = (HashMap)resultDetails.elementAt(0);
            hmResult = new HashMap();
            hmResult.put(LAST_UPDATE_TIMESTAMP, (java.sql.Timestamp)rowDetails.get("UPDATE_TIMESTAMP"));
            hmResult.put(LAST_UPDATE_USER, rowDetails.get("UPDATE_USER"));
        }
        return hmResult;
    }
    //COEUSQA-119 : End
	
	/** Method used to get the negotiation Data the given NEGOTIATION_NUMBER
     *
     * @return NegotiationBean negotiationBean
     * @param negotiationNumber is used to get negotiationHeaderBean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
  /*  public NegotiationBean getNegotiationData(String negotiationNumber)
			throws CoeusException, DBException {
        NegotiationBean negotiationBean = new NegotiationBean();
		
		NegotiationHeaderBean negotiationHeaderBean = getNegotiationHeader(negotiationNumber);
		NegotiationInfoBean negotiationInfoBean = getNegotiationInfo(negotiationNumber);
		negotiationBean.setNegotiationNumber(negotiationNumber);
		negotiationBean.setNegotiationHeaderBean(negotiationHeaderBean);
		negotiationBean.setNegotiationInfoBean(negotiationInfoBean);
		negotiationBean.setCvNegotiationActivities(getNegotiationActivities(negotiationNumber));
		negotiationBean.setCvNegotiationCustomElements(getNegotiationCustomData(negotiationNumber));
		
        return negotiationBean;
    }*/
	
	/** Method used to get the vector of combobox bean about the negotiation act types.
     * <li>To fetch the data, it uses DW_GET_NEGOTIATION_ACT_TYPES procedure
     *
     * @return CoeusVector collection of combobox bean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getNegotiationActTypes()
			throws CoeusException, DBException {
        Vector result = new Vector(3,2);
        
        HashMap row = null;
        ComboBoxBean comboBoxBean = null;
        Vector param= new Vector();
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call DW_GET_NEGOTIATION_ACT_TYPES ( <<OUT RESULTSET rset>> )","Coeus",param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        CoeusVector negotiationActTypesList = new CoeusVector();
        for(int index = 0; index < listSize; index++) {
            comboBoxBean = new ComboBoxBean();
            row = (HashMap)result.elementAt(index);
            comboBoxBean.setCode(row.get("NEGOTIATION_ACT_TYPE_CODE").toString());
            comboBoxBean.setDescription((String)row.get("DESCRIPTION"));
            negotiationActTypesList.addElement(comboBoxBean);
            
        }
        return negotiationActTypesList;
    }
	
	/** Method used to get the vector of combobox bean about the proposal types.
     * <li>To fetch the data, it uses GET_PROPOSAL_TYPE_LIST_DW procedure
     *
     * @return CoeusVector collection of combobox bean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getProposalTypeList()
			throws CoeusException, DBException {
        Vector result = new Vector(3,2);
        
        HashMap row = null;
        ComboBoxBean comboBoxBean = null;
        Vector param= new Vector();
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call GET_PROPOSAL_TYPE_LIST_DW ( <<OUT RESULTSET rset>> )","Coeus",param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        CoeusVector proposalTypesList = new CoeusVector();
        for(int index = 0; index < listSize; index++) {
            comboBoxBean = new ComboBoxBean();
            row = (HashMap)result.elementAt(index);
            comboBoxBean.setCode(row.get("PROPOSAL_TYPE_CODE").toString());
            comboBoxBean.setDescription((String)row.get("DESCRIPTION"));
            proposalTypesList.addElement(comboBoxBean);
            
        }
        return proposalTypesList;
    }
	/** Method used to get the vector of combobox bean about the negotiation status.
     * <li>To fetch the data, it uses DW_GET_NEGOTIATION_STATUS procedure
     *
     * @return CoeusVector collection of combobox bean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getNegotiationStatusList()
			throws CoeusException, DBException {
        Vector result = new Vector(3,2);
        
        HashMap row = null;
        ComboBoxBean comboBoxBean = null;
        Vector param= new Vector();
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call DW_GET_NEGOTIATION_STATUS ( << OUT RESULTSET rset >>)","Coeus",param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        CoeusVector negotiationStatusList = new CoeusVector();
        for(int index = 0; index < listSize; index++) {
            comboBoxBean = new ComboBoxBean();
            row = (HashMap)result.elementAt(index);
            comboBoxBean.setCode(row.get("NEGOTIATION_STATUS_CODE").toString());
			
            comboBoxBean.setDescription((String)row.get("DESCRIPTION"));
            negotiationStatusList.addElement(comboBoxBean);
            
        }
        return negotiationStatusList;
    }
	/** Method used to get the Negotiation custom data for the given Negotiation Number.
     * <li>To fetch the data, it uses GET_NEGOT_CUST_DATA.
     *
     * @return CoeusVector collection of negotiationCustomElementsBean
     * @param negotiationNumber is used to get negotiationCustomElementsBean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getNegotiationCustomData(String negotiationNumber)
			throws CoeusException, DBException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap row = null;
        NegotiationCustomElementsBean negotiationCustomElementsBean = null;
        
        param.addElement(new Parameter("NEGOTIATION_NUMBER",
        DBEngineConstants.TYPE_STRING, negotiationNumber));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call GET_NEGOT_CUST_DATA ( <<NEGOTIATION_NUMBER>>, <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        CoeusVector negotiationCustomList = new CoeusVector();
        for(int index = 0; index < listSize; index++) {
            negotiationCustomElementsBean = new NegotiationCustomElementsBean();
                row = (HashMap)result.elementAt(index);
            negotiationCustomElementsBean.setNegotiationNumber((String)row.get("NEGOTIATION_NUMBER"));
            negotiationCustomElementsBean.setColumnName((String)row.get("COLUMN_NAME"));
            negotiationCustomElementsBean.setColumnValue((String)row.get("COLUMN_VALUE"));
			negotiationCustomElementsBean.setUpdateTimestamp((java.sql.Timestamp)row.get("UPDATE_TIMESTAMP"));
            negotiationCustomElementsBean.setUpdateUser((String)(row.get("UPDATE_USER")));
			String boolValue = (String)row.get("HAS_LOOKUP");
			boolean isRestricted = false;
			if (boolValue.equals("Y")) {
				isRestricted = true;
			} else if (boolValue.equals("N")) {
				isRestricted = false;
			}
            negotiationCustomElementsBean.setHasLookUp(isRestricted);                
            negotiationCustomElementsBean.setColumnLabel((String)row.get("COLUMN_LABEL"));            
            negotiationCustomElementsBean.setDataType((String)row.get("DATA_TYPE")); 
            negotiationCustomElementsBean.setDataLength(
			row.get("DATA_LENGTH") == null ? 0 : Integer.parseInt(row.get("DATA_LENGTH").toString()));
			negotiationCustomElementsBean.setDefaultValue((String)row.get("DEFAULT_VALUE"));   
			negotiationCustomElementsBean.setLookUpWindow((String)row.get("LOOKUP_WINDOW"));   
			negotiationCustomElementsBean.setLookUpArgument((String)row.get("LOOKUP_ARGUMENT"));   
            negotiationCustomList.addElement(negotiationCustomElementsBean);
        }
        return negotiationCustomList;
    }
	
	/**
     * Method used to get Award others details from OSP$AWARD_CUSTOM_DATA
     * for a given Mit Award Number and others details for the loggedin user module.
     * <li>It uses GET_AWARD_CUSTOM_DATA to fetch others data for given Award
     * number and DW_GET_CUST_COLUMNS_FOR_MODULE to get the custom data for the given module.
     * <li>FN_AWARD_HAS_CUSTOM_DATA is used to check whether custom data is
     * available for the given proposal number
     * <li> fn_module_has_custom_data is used to check whether custom data is
     * available for the given module number.
     *
     * <li> get_parameter_value  is used to get the module number for the proposal
     * module by passing the code COEUS_MODULE_DEV_PROPOSAL.
     *
     * @param proposalnumber this is given as input parameter for the
     * procedure to execute.
     * @return Vector collections of ProposalCustomElementsInfoBean for others tab
     * @exception DBException if the instance of a dbEngine is null.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getCustomData(String negotiationNumber)
	throws CoeusException, DBException {
		Vector param= new Vector();
		Vector negotiationOthers = null;
		Vector moduleOthers = null;
		HashMap modId = null;
		Vector result = new Vector();
		TreeSet othersData = new TreeSet(new BeanComparator());
		CoeusVector cvCustomData = new CoeusVector();
		int customModCount = 0;
		String moduleId = "";
		
		param.add(new Parameter("PARAM_NAME",
		DBEngineConstants.TYPE_STRING,"COEUS_MODULE_NEGOTIATION"));
		result = dbEngine.executeFunctions("Coeus",
		"{<<OUT STRING MOD_NAME>>=call get_parameter_value ( "
		+ " << PARAM_NAME >>)}", param);
		if(!result.isEmpty()){
			modId = (HashMap)result.elementAt(0);
			moduleId = modId.get("MOD_NAME").toString();
		}
		
		// check whether any custom data is present for proposal module
		param.removeAllElements();
		param.add(new Parameter("MOD_NAME",
		DBEngineConstants.TYPE_STRING,moduleId));
		result = dbEngine.executeFunctions("Coeus",
		"{<<OUT INTEGER COUNT>>=call fn_module_has_custom_data ( "
		+ " << MOD_NAME >>)}", param);
		if(!result.isEmpty()){
			HashMap modCount = (HashMap)result.elementAt(0);
			customModCount = Integer.parseInt(modCount.get("COUNT").toString());
		}
		
		// custom data is present for the Award module. so get the
		// Award custom data and module custom data and send unique
		// set of custom data from both.
		
		//get proposal custom data
		negotiationOthers = getNegotiationCustomData(negotiationNumber);
		othersData.addAll(negotiationOthers);
		
		if( customModCount > 0 ) {
			// get module custom data
			DepartmentPersonTxnBean departmentPersonTxnBean = new DepartmentPersonTxnBean();
			moduleOthers = departmentPersonTxnBean.getPersonColumnModule(moduleId);
			moduleOthers = setAcTypeAsNew(moduleOthers, negotiationNumber);
			othersData.addAll(moduleOthers);
		}
		//Set required flag based on CustomElementsUsage data
		if(negotiationOthers != null){
			CoeusVector cvModuleOthers = null;
			if(moduleOthers!=null){
				cvModuleOthers = new CoeusVector();
				cvModuleOthers.addAll(moduleOthers);
			}
			CustomElementsInfoBean customElementsInfoBean = null;
			CoeusVector cvFilteredData = null;
			for(int row = 0; row < negotiationOthers.size(); row++){
				customElementsInfoBean = (CustomElementsInfoBean)negotiationOthers.elementAt(row);
				if(cvModuleOthers == null){
					customElementsInfoBean.setRequired(false);
				}else{
					cvFilteredData = cvModuleOthers.filter(new Equals("columnName", customElementsInfoBean.getColumnName()));
					if(cvFilteredData==null || cvFilteredData.size()==0){
						customElementsInfoBean.setRequired(false);
					}else{
						customElementsInfoBean.setRequired(((CustomElementsInfoBean)cvFilteredData.elementAt(0)).isRequired());
					}
				}
			}
			cvCustomData.addAll(new Vector(othersData));
			cvCustomData.sort("columnLabel", true, true);
			return cvCustomData;
		}else{
			// there is no custom data available for the proposal module.
			return null;
		}
	}
	
	/**
     * Method used to get Award others details from OSP$AWARD_CUSTOM_DATA
     * for a given Mit Award Number and others details for the loggedin user module.
     * <li>It uses GET_AWARD_CUSTOM_DATA to fetch others data for given Award
     * number and DW_GET_CUST_COLUMNS_FOR_MODULE to get the custom data for the given module.
     * <li>FN_AWARD_HAS_CUSTOM_DATA is used to check whether custom data is
     * available for the given proposal number
     * <li> fn_module_has_custom_data is used to check whether custom data is
     * available for the given module number.
     *
     * <li> get_parameter_value  is used to get the module number for the proposal
     * module by passing the code COEUS_MODULE_DEV_PROPOSAL.
     *
     * @param proposalnumber this is given as input parameter for the
     * procedure to execute.
     * @return Vector collections of ProposalCustomElementsInfoBean for others tab
     * @exception DBException if the instance of a dbEngine is null.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getCustomDataForNewMode(String negotiationNumber)
	throws CoeusException, DBException {
		Vector param= new Vector();
		Vector negotiationOthers = null;
		Vector moduleOthers = null;
		HashMap modId = null;
		Vector result = new Vector();
		TreeSet othersData = new TreeSet(new BeanComparator());
		CoeusVector cvCustomData = new CoeusVector();
		int customModCount = 0;
		String moduleId = "";
		
		param.add(new Parameter("PARAM_NAME",
		DBEngineConstants.TYPE_STRING,"COEUS_MODULE_NEGOTIATION"));
		result = dbEngine.executeFunctions("Coeus",
		"{<<OUT STRING MOD_NAME>>=call get_parameter_value ( "
		+ " << PARAM_NAME >>)}", param);
		if(!result.isEmpty()) {
			modId = (HashMap)result.elementAt(0);
			moduleId = modId.get("MOD_NAME").toString();
		}
		
		// check whether any custom data is present for proposal module
		param.removeAllElements();
		param.add(new Parameter("MOD_NAME",
		DBEngineConstants.TYPE_STRING,moduleId));
		result = dbEngine.executeFunctions("Coeus",
		"{<<OUT INTEGER COUNT>>=call fn_module_has_custom_data ( "
		+ " << MOD_NAME >>)}", param);
		if(!result.isEmpty()) {
			HashMap modCount = (HashMap)result.elementAt(0);
			customModCount = Integer.parseInt(modCount.get("COUNT").toString());
		}
		
		// custom data is present for the Award module. so get the
		// Award custom data and module custom data and send unique
		// set of custom data from both.
		
		if( customModCount > 0 ) {
			// get module custom data
			DepartmentPersonTxnBean departmentPersonTxnBean = new DepartmentPersonTxnBean();
			moduleOthers = departmentPersonTxnBean.getPersonColumnModule(moduleId);
			moduleOthers = setAcTypeAsNew(moduleOthers, negotiationNumber);
			othersData.addAll(moduleOthers);
			cvCustomData.addAll(new Vector(othersData));
			return cvCustomData;
		} else{
			// there is no custom data available for the Negotiation module.
			return null;
		}
	}
	
	/**
     * Class used to compare two CustomElementsInfoBean
     */
    class BeanComparator implements Comparator{
        public int compare( Object bean1, Object bean2 ){
            if( (bean1 != null ) && ( bean2 != null ) ){
                if( ( bean1 instanceof CustomElementsInfoBean )
                        && ( bean2 instanceof CustomElementsInfoBean ) ) {
                    CustomElementsInfoBean firstBean, secondBean;
                    firstBean = ( CustomElementsInfoBean ) bean1;
                    secondBean = ( CustomElementsInfoBean ) bean2;
                    return firstBean.getColumnName().compareToIgnoreCase(
                        secondBean.getColumnName());
                }
            }
           return 0;
        }
    }  
	
	/**
     * sets AcType 'I' for the records copied from the module cost elements
     * to the proposal cost elements.
     * @param modOthers vector contain CustomElementsInfoBean
     */
    private Vector setAcTypeAsNew(Vector modOthers, String negotiationNumber){
        if(modOthers != null && modOthers.size() > 0 ){
            int modCount = modOthers.size();
                CustomElementsInfoBean customBean;
            NegotiationCustomElementsBean negotiationCustomElementsBean;
            for ( int modIndex = 0; modIndex < modCount; modIndex++ ) {
                customBean = (CustomElementsInfoBean)modOthers.elementAt(modIndex);
                customBean.setAcType("I");
                negotiationCustomElementsBean = new NegotiationCustomElementsBean(customBean);
                negotiationCustomElementsBean.setNegotiationNumber(negotiationNumber);
                modOthers.set(modIndex,negotiationCustomElementsBean);
            }
        }
        return modOthers;
    }
	
	/**  This method used get PI User Name for the given Proposal number
     *  <li>To fetch the data, it uses the function fn_get_pi_full_name.
     *
     * @return String User ID for the Proposal number
     * @param proposalNumber String
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available. */
     public String getPIUserName(String proposalNumber)
                            throws CoeusException, DBException {
        String userName = null;
        Vector param= new Vector();
        Vector result = new Vector();
        param.add(new Parameter("PROPOSAL_NUMBER",
                                DBEngineConstants.TYPE_STRING,proposalNumber));
        /* calling stored function */
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
            "{ <<OUT STRING USER_NAME>> = "
            +" call fn_get_pi_full_name(<< PROPOSAL_NUMBER >> ) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
           HashMap rowParameter = (HashMap)result.elementAt(0);
           Object value = rowParameter.get("USER_NAME");
           if(value!=null){ 
            userName = value.toString();
           }else{
               userName = null;
           }
        }
        return userName;
    }    
	
	// For the updation of the fields the methods starts from here
	
	/**  Method used to update/insert Negotiation Activities
     *  <li>To fetch the data, it uses DW_UPD_NEG_ACTIVITIES procedure.
     *
     * @return boolean true for successful insert/modify
     *
     * @param negotiationActivitiesBean NegotiationActivitiesBean
     * @exception DBException if the instance of a dbEngine is null.
     * @exception CoeusException if the DB instance is not available. */
    public ProcReqParameter addUpdatedNegActivities(NegotiationActivitiesBean negotiationActivitiesBean)  
        throws CoeusException ,DBException {
        Vector param = new Vector();
		dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
		// getting the updated values and add to a vector
        param.addElement(new Parameter("NEGOTIATION_NUMBER",
             DBEngineConstants.TYPE_STRING, negotiationActivitiesBean.getNegotiationNumber()));
        param.addElement(new Parameter("ACTIVITY_NUMBER",
            DBEngineConstants.TYPE_INT, ""+negotiationActivitiesBean.getActivityNumber()));
        param.addElement(new Parameter("DESCRIPTION",
            DBEngineConstants.TYPE_STRING,negotiationActivitiesBean.getDescription()));
		
        param.addElement(new Parameter("NEGOTIATION_ACT_TYPE_CODE",
            DBEngineConstants.TYPE_INT, ""+negotiationActivitiesBean.getNegotiationActTypeCode()));
		
        param.addElement(new Parameter("DOC_FILE_ADDRESS",
             DBEngineConstants.TYPE_STRING, negotiationActivitiesBean.getDocumentFileAddress()));
        param.addElement(new Parameter("FOLLOWUP_DATE",
            DBEngineConstants.TYPE_DATE,negotiationActivitiesBean.getFollowUpDate()));
        param.addElement(new Parameter("CREATE_DATE",
            DBEngineConstants.TYPE_DATE, negotiationActivitiesBean.getCreateDate()));
        param.addElement(new Parameter("ACTIVITY_DATE",
            DBEngineConstants.TYPE_DATE, negotiationActivitiesBean.getActivityDate()));
		param.addElement(new Parameter("RESTRICTED_VIEW",
            DBEngineConstants.TYPE_STRING, negotiationActivitiesBean.isRestrictedView() ? "Y" : "N")); 
		 param.addElement(new Parameter("UPDATE_USER",
            DBEngineConstants.TYPE_STRING, userId));	
		param.addElement(new Parameter("UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
		param.addElement(new Parameter("AW_NEGOTIATION_NUMBER",
             DBEngineConstants.TYPE_STRING, negotiationActivitiesBean.getNegotiationNumber()));
       param.addElement(new Parameter("AW_ACTIVITY_NUMBER",
            DBEngineConstants.TYPE_INT, ""+negotiationActivitiesBean.getActivityNumber()));
		
		param.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP,negotiationActivitiesBean.getUpdateTimestamp()));
			
        param.addElement(new Parameter("AC_TYPE",
            DBEngineConstants.TYPE_STRING, negotiationActivitiesBean.getAcType()));

        StringBuffer sql = new StringBuffer("call DW_UPD_NEG_ACTIVITIES(");
        sql.append(" <<NEGOTIATION_NUMBER>>, ");
        sql.append(" <<ACTIVITY_NUMBER>>, ");
        sql.append(" <<DESCRIPTION>>, ");
        sql.append(" <<NEGOTIATION_ACT_TYPE_CODE>>, ");
        sql.append(" <<DOC_FILE_ADDRESS>>, ");
        sql.append(" <<FOLLOWUP_DATE>>, ");
        sql.append(" <<CREATE_DATE>>, ");
        sql.append(" <<ACTIVITY_DATE>>, ");
		sql.append(" <<RESTRICTED_VIEW>>, ");
		sql.append(" <<UPDATE_USER>>, ");
        sql.append(" <<UPDATE_TIMESTAMP>>, ");
        sql.append(" <<AW_NEGOTIATION_NUMBER>>, ");
        sql.append(" <<AW_ACTIVITY_NUMBER>>, ");
		sql.append(" <<AW_UPDATE_TIMESTAMP>>, ");
        sql.append(" <<AC_TYPE>> )");

        ProcReqParameter procReqParameter = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sql.toString());        

        return procReqParameter;
    } 
	
	/**  Method used to update/insert Negotiation custom data
     *  <li>To fetch the data, it uses DW_UPDATE_NEGOT_CUSTOM_DATA procedure.
     *
     * @return boolean true for successful insert/modify
     *
     * @param NegotiationCustomElementsBean negotiationCustomElementsBean
     * @exception DBException if the instance of a dbEngine is null.
     * @exception CoeusException if the DB instance is not available. */
    public ProcReqParameter addUpdatedNegCustomData(NegotiationCustomElementsBean negotiationCustomElementsBean)  
        throws CoeusException ,DBException{
        Vector param = new Vector();
		dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
		// getting the updated values and add to a vector
        param.addElement(new Parameter("NEGOTIATION_NUMBER",
             DBEngineConstants.TYPE_STRING, negotiationCustomElementsBean.getNegotiationNumber()));
        param.addElement(new Parameter("COLUMN_NAME",
            DBEngineConstants.TYPE_STRING,negotiationCustomElementsBean.getColumnName()));
        param.addElement(new Parameter("COLUMN_VALUE",
            DBEngineConstants.TYPE_STRING, negotiationCustomElementsBean.getColumnValue()));
		
		param.addElement(new Parameter("UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
		
        param.addElement(new Parameter("UPDATE_USER",
            DBEngineConstants.TYPE_STRING, userId));
		param.addElement(new Parameter("AW_NEGOTIATION_NUMBER",
             DBEngineConstants.TYPE_STRING, negotiationCustomElementsBean.getNegotiationNumber()));
		param.addElement(new Parameter("AW_COLUMN_NAME",
            DBEngineConstants.TYPE_STRING,negotiationCustomElementsBean.getColumnName()));
		param.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP,negotiationCustomElementsBean.getUpdateTimestamp()));
		
        param.addElement(new Parameter("AC_TYPE",
            DBEngineConstants.TYPE_STRING, negotiationCustomElementsBean.getAcType()));

        StringBuffer sql = new StringBuffer("call DW_UPDATE_NEGOT_CUSTOM_DATA(");
        sql.append(" <<NEGOTIATION_NUMBER>>, ");
        sql.append(" <<COLUMN_NAME>>, ");
        sql.append(" <<COLUMN_VALUE>>, ");
        sql.append(" <<UPDATE_TIMESTAMP>>, ");
        sql.append(" <<UPDATE_USER>>, ");
		sql.append(" <<AW_NEGOTIATION_NUMBER>>, ");
		sql.append(" <<AW_COLUMN_NAME>>, ");
		sql.append(" <<AW_UPDATE_TIMESTAMP>>, ");
        sql.append(" <<AC_TYPE>> )");

        ProcReqParameter procReqParameter = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sql.toString());        

        return procReqParameter;
    }
	
	/**  Method used to update/insert Negotiation custom data
     *  <li>To fetch the data, it uses UPD_NEGOTIATION procedure.
     *
     * @return boolean true for successful insert/modify
     *
     * @param NegotiationCustomElementsBean negotiationCustomElementsBean
     * @exception DBException if the instance of a dbEngine is null.
     * @exception CoeusException if the DB instance is not available. */
    public ProcReqParameter addUpdatedNegotiation(NegotiationInfoBean negotiationInfoBean)  
        throws CoeusException ,DBException{
        Vector param = new Vector();
		dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
		// getting the updated values and add to a vector
        param.addElement(new Parameter("NEGOTIATION_NUMBER",
             DBEngineConstants.TYPE_STRING,negotiationInfoBean.getNegotiationNumber()));
        param.addElement(new Parameter("NEGOTIATOR_ID",
            DBEngineConstants.TYPE_STRING,negotiationInfoBean.getNegotiatorId()));
        param.addElement(new Parameter("DOC_FILE_ADDRESS",
            DBEngineConstants.TYPE_STRING,negotiationInfoBean.getDocFileAddress()));
		param.addElement(new Parameter("START_DATE",
            DBEngineConstants.TYPE_DATE,negotiationInfoBean.getStartDate()));
         //Added for case 4185 - new field closed Date -Start
        param.addElement(new Parameter("CLOSED_DATE",
            DBEngineConstants.TYPE_DATE,negotiationInfoBean.getClosedDate()));	
         //Added for case 4185 - new field closed Date -End        
		param.addElement(new Parameter("NEGOTIATION_STATUS_CODE",
            DBEngineConstants.TYPE_INT, ""+negotiationInfoBean.getStatusCode()));
		param.addElement(new Parameter("UPDATE_USER",
            DBEngineConstants.TYPE_STRING, userId));
		param.addElement(new Parameter("UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
            //case 3590 start
            param.addElement(new Parameter("NEGOTIATION_AGREE_TYPE_CODE",
                DBEngineConstants.TYPE_INT, ""+negotiationInfoBean.getNegotiationAgreeTypeCode()));
            param.addElement(new Parameter("PRIME_SPONSOR_CODE",
                DBEngineConstants.TYPE_STRING,negotiationInfoBean.getPrimeSponsorCode()));
            param.addElement(new Parameter("PROPOSED_START_DATE",
                DBEngineConstants.TYPE_DATE,negotiationInfoBean.getProposedStartDate()));
            //case 3590 end
		param.addElement(new Parameter("AW_NEGOTIATION_NUMBER",
             DBEngineConstants.TYPE_STRING,negotiationInfoBean.getNegotiationNumber()));
		param.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP,negotiationInfoBean.getUpdateTimestamp()));      		
        param.addElement(new Parameter("AC_TYPE",
            DBEngineConstants.TYPE_STRING, negotiationInfoBean.getAcType()));
		
        StringBuffer sql = new StringBuffer("call UPD_NEGOTIATION(");
        sql.append(" <<NEGOTIATION_NUMBER>>, ");
        sql.append(" <<NEGOTIATOR_ID>>, ");
        sql.append(" <<DOC_FILE_ADDRESS>>, ");
        sql.append(" <<START_DATE>>, ");
        sql.append(" <<CLOSED_DATE>>, ");//case#4185
        sql.append(" <<NEGOTIATION_STATUS_CODE>>, ");
        sql.append(" <<UPDATE_USER>>, ");
        sql.append(" <<UPDATE_TIMESTAMP>>, ");
        //case 3590 start
        sql.append(" <<NEGOTIATION_AGREE_TYPE_CODE>>, ");
        sql.append(" <<PRIME_SPONSOR_CODE>>, ");
        sql.append(" <<PROPOSED_START_DATE>>, ");
        //case 3590 end
        sql.append(" <<AW_NEGOTIATION_NUMBER>>, ");
		sql.append(" <<AW_UPDATE_TIMESTAMP>>, ");
        sql.append(" <<AC_TYPE>> )");

        ProcReqParameter procReqParameter = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sql.toString());        

        return procReqParameter;
    }
	
	/** Method used to update/insert Negotiation details
     *
     * @return boolean true for successful insert/modify
     * @param negotiationData Hashtable
     * @exception DBException if the instance of a dbEngine is null.
     * @exception CoeusException if the DB instance is not available.
     */
    
    public boolean updateNegotiation(Hashtable negotiationData) throws CoeusException ,DBException {
		boolean success = false;
		Vector procedures = new Vector();
                Vector vctInsertProcedures = new Vector();//2806
		Vector param = new Vector();
		if (negotiationData != null) {
			
			// updating negotiation info bean
			CoeusVector cvNegInfo = (CoeusVector)negotiationData.get(NegotiationInfoBean.class);
			if (cvNegInfo != null && cvNegInfo.size() > 0) {
				for (int index = 0; index < cvNegInfo.size(); index++) {
					NegotiationInfoBean negotiationInfoBean = (NegotiationInfoBean)cvNegInfo.get(index);
                                        if(negotiationInfoBean != null && negotiationInfoBean.getAcType() != null){
                                            procedures.addElement(addUpdatedNegotiation(negotiationInfoBean));
                                        }
				}
			}
			
			// updating negotiation activities bean
			CoeusVector cvNegActivities = (CoeusVector)negotiationData.get(NegotiationActivitiesBean.class);
			if (cvNegActivities != null && cvNegActivities.size() > 0) {
				for (int index = 0; index < cvNegActivities.size(); index++) { 
					NegotiationActivitiesBean negotiationActivitiesBean = (NegotiationActivitiesBean)cvNegActivities.get(index);
                                        if(negotiationActivitiesBean != null && negotiationActivitiesBean.getAcType() != null){
                                            procedures.addElement(addUpdatedNegActivities(negotiationActivitiesBean));
                                            // Added with case 2806 - Upload attachments to negotiation - Start
                                            NegotiationAttachmentBean attachmentBean = negotiationActivitiesBean.getAttachmentBean();
                                            if(attachmentBean!=null && attachmentBean.getAcType()!=null){
                                                attachmentBean.setNegotiationNumber(negotiationActivitiesBean.getNegotiationNumber());
                                                attachmentBean.setActivityNumber(negotiationActivitiesBean.getActivityNumber());
                                                if(attachmentBean.getAcType().equals(TypeConstants.INSERT_RECORD)){
                                                    vctInsertProcedures.addElement(addNegotiationDocument(attachmentBean));
                                                }else if(attachmentBean.getAcType().equals(TypeConstants.UPDATE_RECORD)){
                                                    attachmentBean.setAcType(TypeConstants.DELETE_RECORD);
                                                    procedures.addElement(addUpdNegAttachments(attachmentBean));
                                                    attachmentBean.setAcType(TypeConstants.INSERT_RECORD);
                                                    vctInsertProcedures.addElement(addNegotiationDocument(attachmentBean));
                                                }else if(attachmentBean.getAcType().equals(TypeConstants.DELETE_RECORD)){
                                                    procedures.addElement(addUpdNegAttachments(attachmentBean));
                                                }
                                            }
                                            // Added with case 2806 - Upload attachments to negotiation - End
                                        }
				}
			}
			
			// updating custom elements bean
			
			CoeusVector cvNegCustomElements = (CoeusVector)negotiationData.get(NegotiationCustomElementsBean.class);
			if (cvNegCustomElements != null && cvNegCustomElements.size() > 0) {
				for (int index = 0; index < cvNegCustomElements.size(); index++) {
					NegotiationCustomElementsBean negotiationCustomElementsBean = (NegotiationCustomElementsBean)cvNegCustomElements.get(index);
                                        if(negotiationCustomElementsBean != null && negotiationCustomElementsBean.getAcType() != null){
                                            procedures.addElement(addUpdatedNegCustomData(negotiationCustomElementsBean));
                                        }
				}
			}
			
                        //case 3590 start
                        // updating negotiation location bean
			CoeusVector cvNegLocaton = (CoeusVector)negotiationData.get(NegotiationLocationBean.class);
			if (cvNegLocaton != null && cvNegLocaton.size() > 0) {
				for (int index = 0; index < cvNegLocaton.size(); index++) { 
					NegotiationLocationBean negotiationLocationBean = (NegotiationLocationBean)cvNegLocaton.get(index);
                                        if(negotiationLocationBean != null && negotiationLocationBean.getAcType() != null){
                                            procedures.addElement(addUpdatedNegLocation(negotiationLocationBean));
                                        }
				}
			}
                        //case 3590 end
			if(dbEngine != null) {
                            //Added with case 2806 - Upload attachments to negotiation - Start
//				dbEngine.executeStoreProcs(procedures);
                            java.sql.Connection conn = null;
                            try{
                                //Begin a new Transaction
                                conn = dbEngine.beginTxn();
                                //Update other data
                                if(procedures!=null && procedures.size() > 0){
                                    dbEngine.executeStoreProcs(procedures, conn);
                                }
                                //Update PDF Data
                                if(vctInsertProcedures!=null && vctInsertProcedures.size() > 0){
                                    dbEngine.batchSQLUpdate(vctInsertProcedures, conn);
                                }
                                //End Txn
                                dbEngine.endTxn(conn);
                            }catch(Exception ex){
                                dbEngine.rollback(conn);
                                throw new DBException(ex.getMessage());
                            }
                            success = true;
                            //Added with case 2806 - Upload attachments to negotiation - End
                        } else {
				throw new CoeusException("db_exceptionCode.1000");
			}
			success = true;
		}
		
		return success;
	}
    
    
    //case 3590 start
    public CoeusVector getNegotiationAgreementTypeList()
			throws CoeusException, DBException {
        Vector result = new Vector(3,2);
        
        HashMap row = null;
        ComboBoxBean comboBoxBean = null;
        Vector param= new Vector();
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call get_negotiation_agreement_type ( << OUT RESULTSET rset >>)","Coeus",param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        CoeusVector negotiationAgreementTypeList = new CoeusVector();
        for(int index = 0; index < listSize; index++) {
            comboBoxBean = new ComboBoxBean();
            row = (HashMap)result.elementAt(index);
            comboBoxBean.setCode(row.get("NEGOTIATION_AGREE_TYPE_CODE").toString());
			
            comboBoxBean.setDescription((String)row.get("DESCRIPTION"));
            negotiationAgreementTypeList.addElement(comboBoxBean);
            
        }
        return negotiationAgreementTypeList;
    }
    
    public CoeusVector getNegotiationLocationTypeList()
			throws CoeusException, DBException {
        Vector result = new Vector(3,2);
        
        HashMap row = null;
        ComboBoxBean comboBoxBean = null;
        Vector param= new Vector();
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call get_negotiation_location_type ( << OUT RESULTSET rset >>)","Coeus",param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        CoeusVector negotiationLocationTypeList = new CoeusVector();
        for(int index = 0; index < listSize; index++) {
            comboBoxBean = new ComboBoxBean();
            row = (HashMap)result.elementAt(index);
            comboBoxBean.setCode(row.get("NEGOTIATION_LOCATION_TYPE_CODE").toString());
			
            comboBoxBean.setDescription((String)row.get("DESCRIPTION"));
            negotiationLocationTypeList.addElement(comboBoxBean);
            
        }
        return negotiationLocationTypeList;
    }
    
    //** Method used to get the Negotiation Location for the given Negotiation Number.    
     public CoeusVector getNegotiationLocation(String negotiationNumber)
			throws CoeusException, DBException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap hmRow = null;
        NegotiationLocationBean negotiationLocationBean = null;
        
        param.addElement(new Parameter("NEGOTIATION_NUMBER",
        DBEngineConstants.TYPE_STRING, negotiationNumber));       
       
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call get_negotiation_location_last ( <<NEGOTIATION_NUMBER>>,<<OUT RESULTSET rset>> )",
            "Coeus", param);            
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
		
        CoeusVector negotiationLocationList = new CoeusVector();
        for(int index = 0; index < listSize; index++) {
            negotiationLocationBean = new NegotiationLocationBean();
                hmRow = (HashMap)result.elementAt(index);
            negotiationLocationBean.setNegotiationNumber((String)hmRow.get("NEGOTIATION_NUMBER"));
            negotiationLocationBean.SetLocationNumber(
                hmRow.get("LOCATION_NUMBER") == null ? 0 : Integer.parseInt(hmRow.get("LOCATION_NUMBER").toString()));
            negotiationLocationBean.SetNegotiationLocationTypeCode(
                hmRow.get("NEGOTIATION_LOCATION_TYPE_CODE") == null ? 0 : Integer.parseInt(hmRow.get("NEGOTIATION_LOCATION_TYPE_CODE").toString()));
            negotiationLocationBean.setNegotiationLocationTypeDes((String)hmRow.get("LOCATION_TYPE_DES"));
            negotiationLocationBean.setEffectiveDate(
                hmRow.get("EFFECTIVE_DATE") == null ?
                    null : new Date(((Timestamp) hmRow.get("EFFECTIVE_DATE")).getTime())); 
            negotiationLocationBean.setUpdateTimestamp((java.sql.Timestamp)hmRow.get("UPDATE_TIMESTAMP"));			
            negotiationLocationBean.setUpdateUser((String)hmRow.get("UPDATE_USER"));   
            negotiationLocationList.addElement(negotiationLocationBean);
        }
        return negotiationLocationList;
    }
     
         
    //** Method used to get the Negotiation Location for the given Negotiation Number.    
     public CoeusVector getNegotiationLocationHistory(String negotiationNumber)
			throws CoeusException, DBException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap hmRow = null;
        NegotiationLocationBean negotiationLocationBean = null;
        
        param.addElement(new Parameter("NEGOTIATION_NUMBER",
        DBEngineConstants.TYPE_STRING, negotiationNumber));       
       
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call get_negotiation_location ( <<NEGOTIATION_NUMBER>>,<<OUT RESULTSET rset>> )",
            "Coeus", param);            
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
		
        CoeusVector negotiationLocationList = new CoeusVector();
        for(int index = 0; index < listSize; index++) {
            negotiationLocationBean = new NegotiationLocationBean();
                hmRow = (HashMap)result.elementAt(index);
            negotiationLocationBean.setNegotiationNumber((String)hmRow.get("NEGOTIATION_NUMBER"));
            negotiationLocationBean.SetLocationNumber(
                hmRow.get("LOCATION_NUMBER") == null ? 0 : Integer.parseInt(hmRow.get("LOCATION_NUMBER").toString()));
            negotiationLocationBean.SetNegotiationLocationTypeCode(
                hmRow.get("NEGOTIATION_LOCATION_TYPE_CODE") == null ? 0 : Integer.parseInt(hmRow.get("NEGOTIATION_LOCATION_TYPE_CODE").toString()));
            negotiationLocationBean.setNegotiationLocationTypeDes((String)hmRow.get("LOCATION_TYPE_DES"));
            negotiationLocationBean.setEffectiveDate(
                hmRow.get("EFFECTIVE_DATE") == null ?
                    null : new Date(((Timestamp) hmRow.get("EFFECTIVE_DATE")).getTime())); 
            negotiationLocationBean.setUpdateTimestamp((java.sql.Timestamp)hmRow.get("UPDATE_TIMESTAMP"));			
            negotiationLocationBean.setUpdateUser((String)hmRow.get("UPDATE_USER"));   
            negotiationLocationList.addElement(negotiationLocationBean);
        }
        return negotiationLocationList;
    }
     
//     *  Method used to update/insert Negotiation Location
//     *  <li>To fetch the data, it uses UPD_NEG_LOCATION procedure.
//     *
//     * @return boolean true for successful insert/modify
    
    public ProcReqParameter addUpdatedNegLocation(NegotiationLocationBean negotiationLocationBean)  
        throws CoeusException ,DBException {
        Vector param = new Vector();
		dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
		// getting the updated values and add to a vector
        param.addElement(new Parameter("NEGOTIATION_NUMBER",
             DBEngineConstants.TYPE_STRING, negotiationLocationBean.getNegotiationNumber()));
        param.addElement(new Parameter("LOCATION_NUMBER",
            DBEngineConstants.TYPE_INT, ""+negotiationLocationBean.getLocationNumber()));        
        param.addElement(new Parameter("NEGOTIATION_LOCATION_TYPE_CODE",
            DBEngineConstants.TYPE_INT, ""+negotiationLocationBean.getNegotiationLocationTypeCode()));	
        param.addElement(new Parameter("EFFECTIVE_DATE",
            DBEngineConstants.TYPE_DATE,negotiationLocationBean.getEffectiveDate()));
       param.addElement(new Parameter("UPDATE_USER",
            DBEngineConstants.TYPE_STRING, userId));	
       param.addElement(new Parameter("UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
       param.addElement(new Parameter("AW_NEGOTIATION_NUMBER",
             DBEngineConstants.TYPE_STRING, negotiationLocationBean.getNegotiationNumber()));
       param.addElement(new Parameter("AW_LOCATION_NUMBER",
            DBEngineConstants.TYPE_INT, ""+negotiationLocationBean.getLocationNumber()));		
       param.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP,negotiationLocationBean.getUpdateTimestamp()));
       param.addElement(new Parameter("AC_TYPE",
            DBEngineConstants.TYPE_STRING, negotiationLocationBean.getAcType()));

        StringBuffer sql = new StringBuffer("call upd_negotiation_location(");
        sql.append(" <<NEGOTIATION_NUMBER>>, ");
        sql.append(" <<LOCATION_NUMBER>>, ");
        sql.append(" <<NEGOTIATION_LOCATION_TYPE_CODE>>, ");
        sql.append(" <<EFFECTIVE_DATE>>, ");
	sql.append(" <<UPDATE_USER>>, ");
        sql.append(" <<UPDATE_TIMESTAMP>>, ");
        sql.append(" <<AW_NEGOTIATION_NUMBER>>, ");
        sql.append(" <<AW_LOCATION_NUMBER>>, ");
	sql.append(" <<AW_UPDATE_TIMESTAMP>>, ");
        sql.append(" <<AC_TYPE>> )");

        ProcReqParameter procReqParameter = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sql.toString());        

        return procReqParameter;
    } 
    //case 3590 end 
    //case 3961 start
     public int getInstituteProposalCount(String proposalNumber) throws CoeusException, DBException{
        int propCount = 0;
        Vector param= new Vector();
        Vector result = new Vector();
        param.add(new Parameter("PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING, ""+proposalNumber));
        
        /* calling stored function */
        if(dbEngine != null) {
            result = dbEngine.executeFunctions("Coeus",
            "{ <<OUT INTEGER GET_INST_PROP_COUNT>> = "
            +" call fn_get_inst_prop_count(<<PROPOSAL_NUMBER>>) }", param);
        } else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            propCount = Integer.parseInt(rowParameter.get("GET_INST_PROP_COUNT").toString());
        }
        return propCount;
    }
    //case 3961 end 
	
	public static void main(String args[]){
        try{
            NegotiationTxnBean negotiationTxnBean
            = new NegotiationTxnBean();
            NegotiationActivitiesBean negotiationActivitiesBean = new NegotiationActivitiesBean();
//			negotiationActivitiesBean.setNegotiationNumber("T0000002");
//			negotiationTxnBean.getNegotiationActivities("T0000002");
//			negotiationTxnBean.getNegotiationHeader("T0000002");
//			negotiationTxnBean.getNegotiationInfo("T0000002");
//			negotiationTxnBean.getNegotiationStatusList();
//			negotiationTxnBean.getNegotiationActTypes();
//			negotiationTxnBean.getProposalTypeList();
//			negotiationTxnBean.getNegotiationCustomData("T0000002");
//			negotiationTxnBean.getCustomColumnsForModule("5");
			//Hashtable negotiationData = new Hashtable();
			
			//boolean success = negotiationTxnBean.updateNegotiation(negotiationData);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
//    New method added with case 4185 - Negotiation Location History must be editable - Start
         /**
         *  The method which updates location history.
         *  This method in turn calls upd_negotiation_location.
         *
         *  @param cvLocData - The negotiation locations vector.
         *  @return boolean true/false whether the update is success is not.
         *
         *  @exception DBException if any error during database transaction.
         *  @exception CoeusException if the instance of dbEngine is not available.
         */
        public boolean updateLocationHistory(CoeusVector cvLocData) throws CoeusException ,DBException {
            
            boolean success     = false;
            Vector procedures   = new Vector();
            NegotiationLocationBean negotiationLocationBean = null;
            
            if (cvLocData != null && cvLocData.size() > 0) {
                for (int index = 0; index < cvLocData.size(); index++) {
                    negotiationLocationBean = (NegotiationLocationBean)cvLocData.get(index);
                    if(negotiationLocationBean != null && negotiationLocationBean.getAcType() != null){
                        procedures.addElement(addUpdatedNegLocation(negotiationLocationBean));
                    }
                }
            }
            if(dbEngine != null) {
                dbEngine.executeStoreProcs(procedures);
            } else {
                throw new CoeusException("db_exceptionCode.1000");
            }
            success = true;
            return success;
        }
//        New method added with case 4185 - Negotiation Location History must be editable - End
        
//        New methods added with case 2806 - Upload attachments to negotiation - Start
        
        /**
         *  The method which calls the procedure update_negotiation_attachment.
         *  This procedure performs deletion from osp$negotiation_documents
         *
         *  @param negotiationAttachmentBean NegotiationAttachmentBean
         *  @return procReqParameter ProcReqParameter
         *
         *  @exception DBException if any error during database transaction.
         *  @exception CoeusException if the instance of dbEngine is not available.
         */
        public ProcReqParameter addUpdNegAttachments(NegotiationAttachmentBean negotiationAttachmentBean)
        throws CoeusException ,DBException {
            Vector param = new Vector();
            dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
            param.addElement(new Parameter("NEGOTIATION_NUMBER",
                    DBEngineConstants.TYPE_STRING, negotiationAttachmentBean.getNegotiationNumber()));
            param.addElement(new Parameter("ACTIVITY_NUMBER",
                    DBEngineConstants.TYPE_INT, ""+negotiationAttachmentBean.getActivityNumber()));
            param.addElement(new Parameter("AC_TYPE",
                    DBEngineConstants.TYPE_STRING, negotiationAttachmentBean.getAcType()));
            
            StringBuffer sql = new StringBuffer("call update_negotiation_attachment(");
            sql.append(" <<NEGOTIATION_NUMBER>>, ");
            sql.append(" <<ACTIVITY_NUMBER>>, ");
            sql.append(" <<AC_TYPE>> )");
            
            ProcReqParameter procReqParameter = new ProcReqParameter();
            procReqParameter.setDSN(DSN);
            procReqParameter.setParameterInfo(param);
            procReqParameter.setSqlCommand(sql.toString());
            
            return procReqParameter;
        }
        /**
         *  The method used to get the insert query for the Negotiation attachment blob.
         *
         *  @param negotiationAttachmentBean NegotiationAttachmentBean
         *  @return procReqParameter ProcReqParameter
         *
         *  @exception DBException if any error during database transaction.
         *  @exception CoeusException if the instance of dbEngine is not available.
         */
        public ProcReqParameter addNegotiationDocument(NegotiationAttachmentBean negotiationAttachmentBean)
        throws CoeusException, DBException {
           
            Vector param = new Vector();
            boolean isUpdated = false;
            
            byte[] fileData = null;
            ProcReqParameter procReqParameter = null;
            
            if(negotiationAttachmentBean!=null && negotiationAttachmentBean.getAcType()!=null &&
                    negotiationAttachmentBean.getAcType().equalsIgnoreCase("I")){
                fileData = negotiationAttachmentBean.getFileBytes();
                if(fileData!=null){
                    String statement = "";
                    dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
                    param.addElement(new Parameter("NEGOTIATION_NUMBER",
                            DBEngineConstants.TYPE_STRING, negotiationAttachmentBean.getNegotiationNumber()));
                    param.addElement(new Parameter("ACTIVITY_NUMBER",
                            DBEngineConstants.TYPE_INT, ""+negotiationAttachmentBean.getActivityNumber()));
                    param.addElement(new Parameter("FILE_NAME",
                            DBEngineConstants.TYPE_STRING, negotiationAttachmentBean.getFileName()));
                    param.addElement(new Parameter("DOCUMENT",
                            DBEngineConstants.TYPE_BLOB, negotiationAttachmentBean.getFileBytes()));
                    param.addElement(new Parameter("MIME_TYPE",
                            DBEngineConstants.TYPE_STRING, negotiationAttachmentBean.getMimeType()));
                    param.addElement(new Parameter("UPDATE_USER",
                            DBEngineConstants.TYPE_STRING, userId));
                    param.addElement(new Parameter("UPDATE_TIMESTAMP",
                            DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
                    
                    statement = "INSERT INTO OSP$NEGOTIATION_DOCUMENTS ( NEGOTIATION_NUMBER, ACTIVITY_NUMBER, FILE_NAME, DOCUMENT, MIME_TYPE ,UPDATE_TIMESTAMP, UPDATE_USER) "+
                            " VALUES(<<NEGOTIATION_NUMBER>>, <<ACTIVITY_NUMBER>>, <<FILE_NAME>>, <<DOCUMENT>>,<<MIME_TYPE>> , <<UPDATE_TIMESTAMP>>, <<UPDATE_USER>>)";
                    
                    procReqParameter = new ProcReqParameter();
                    procReqParameter.setDSN(DSN);
                    procReqParameter.setParameterInfo(param);
                    procReqParameter.setSqlCommand(statement);
                }
            }
            return procReqParameter;
        }
        /**
         *  The method used to fetch the Negotiation attachment blob from DB.
         *
         *  @param negotiationAttachmentBean NegotiationAttachmentBean
         *  @return negotiationAttachmentBean NegotiationAttachmentBean
         *
         *  @exception DBException if any error during database transaction.
         *  @exception CoeusException if the instance of dbEngine is not available.
         */
        public NegotiationAttachmentBean getNegotiationDocument(NegotiationAttachmentBean negotiationAttachmentBean)
        throws CoeusException, DBException{
            Vector result = new Vector(3, 2);
            Vector param = new Vector();
            String selectQuery = "";
            byte[] fileData = null;
            
            param.addElement(new Parameter("NEGOTIATION_NUMBER",
                    DBEngineConstants.TYPE_STRING, negotiationAttachmentBean.getNegotiationNumber()));
            param.addElement(new Parameter("ACTIVITY_NUMBER",
                    DBEngineConstants.TYPE_INT, ""+negotiationAttachmentBean.getActivityNumber()));
            
            selectQuery = "SELECT DOCUMENT,FILE_NAME,UPDATE_USER,UPDATE_TIMESTAMP FROM OSP$NEGOTIATION_DOCUMENTS "+
                    "WHERE NEGOTIATION_NUMBER =  <<NEGOTIATION_NUMBER>> AND  ACTIVITY_NUMBER =  <<ACTIVITY_NUMBER>>";
            
            HashMap resultRow = null;
            if(dbEngine!=null){
                result = dbEngine.executeRequest("Coeus", selectQuery, "Coeus",param);
                if( !result.isEmpty() ){
                    resultRow = (HashMap)result.get(0);
                    
                    negotiationAttachmentBean.setFileBytes(convert((ByteArrayOutputStream)resultRow.get("DOCUMENT")));
                    negotiationAttachmentBean.setFileName((String)resultRow.get("FILE_NAME"));
                    negotiationAttachmentBean.setUpdateUser((String)resultRow.get("UPDATE_USER"));
                    negotiationAttachmentBean.setUpdateTimestamp((Timestamp)resultRow.get("UPDATE_TIMESTAMP"));
                    return negotiationAttachmentBean;
                }
            }else {
                throw new CoeusException("db_exceptionCode.1000");
            }
            return null;
        }
        /**
         *  The method used to convert ByteArrayOutputStream to byte array.
         *
         *  @param baos ByteArrayOutputStream
         *  @return byteArray byte[]
         */
        private byte[] convert(ByteArrayOutputStream baos){
            byte[] byteArray = null;
            try {
                byteArray = baos.toByteArray();
            }finally{
                try {
                    baos.close();
                }catch (IOException ioex){}
            }
            return byteArray;
        }
//        New methods added with case 2806 - Upload attachments to negotiation - End
        // 3587: Multi Campus Enahncements - Start
        public String fetchNegotiationLeadUnit(String negotiationNumber) throws CoeusException, DBException{
            String leadUnit = "";
            InstituteProposalTxnBean instituteProposalTxnBean = new InstituteProposalTxnBean();
            int validProposal = instituteProposalTxnBean.validateProposalNumber(negotiationNumber);
            if(validProposal == 1){
                leadUnit = instituteProposalTxnBean.getLeadUnitForInstProposal(negotiationNumber);
            } else {
                InstituteProposalLogBean instituteProposalLogBean =
                        instituteProposalTxnBean.getInstituteProposalLog(negotiationNumber);
                if(instituteProposalLogBean != null){
                    leadUnit = instituteProposalLogBean.getLeadUnit();
                }
                
            }
            return leadUnit;
        }
        // 3587: Multi Campus Enahncements - End
        
    //Added for COEUSDEV-294 : Error adding activity to a negotiation - Start  
    /**
     * Method to get max activity number for a negotiation
     * This method calls the DB Function FN_GET_MAX_NEGOT_ACTIVITIY_NUM.
     * @param String negotiationNumber
     * @return maxNegotiationActivityNumber
     * @throws DBException
     * @throws CoeusException
     */
    public int getMaxNegotiationActivityNumber(String negotiationNumber) throws DBException, CoeusException{
        int maxNegotiationActivityNumber = -1;
        HashMap row = null;
        Vector result = new Vector();
        
        Vector param= new Vector();
        
        param.add(new Parameter("NEGOTIATION_NUMBER",
                DBEngineConstants.TYPE_STRING,negotiationNumber)) ;

        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{<<OUT INTEGER MAX_NEGOTIATION_ACTIVITY_NUMBER>> = call FN_GET_MAX_NEG_ACTIVITIY_NUM( "
                    + " << NEGOTIATION_NUMBER >>)}", param) ;
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(result != null && !result.isEmpty()){
            row = (HashMap)result.elementAt(0);
            maxNegotiationActivityNumber = Integer.parseInt(row.get("MAX_NEGOTIATION_ACTIVITY_NUMBER").toString());
        }       
        return maxNegotiationActivityNumber;
    }
    //COEUSDEV-268 : End
    
    //COEUSDEV - 733 Create a new notification for negotiation module - Start
    /* 
     */
    /**
     * This function fetches all relevant negotiation information into a hashmap for a negotiation
     * @param String negotiationNumber
     * @return cvNegotiationDetails
     * @throws DBException
     * @throws CoeusException     
     */
    public CoeusVector getNegotiationDetailsForMail(String negotiationNumber) throws CoeusException, DBException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap hmReturn = new HashMap();
        NegotiationHeaderBean negotiationHeaderBean;
        NegotiationInfoBean negotiationInfoBean;
        CoeusVector cvNegotiationDetails = new CoeusVector();
        String negotiationMessage = "";
        try {
            //getting the negotiation header bean
            negotiationHeaderBean = this.getNegotiationHeader(negotiationNumber);
            //getting the negotiation info bean
            negotiationInfoBean = this.getNegotiationInfo(negotiationNumber);
            String leadUnit = negotiationHeaderBean.getLeadUnit();
            //getting the negotiation activities
            CoeusVector cvNegotiationActivities = this.getNegotiationActivities(negotiationNumber, negotiationHeaderBean.getUserId(), leadUnit);
            //The below is commented to avoid the fetching of activity message details. Can be used in future for getting the activity details
            //negotiationMessage = getActivityMessage(cvNegotiationActivities);
            //putting into the hash table
            hmReturn.put("NEGOTIATION_NUMBER", negotiationInfoBean.getNegotiationNumber());
            hmReturn.put("NEGOTIATOR_ID", negotiationInfoBean.getNegotiatorName());
            hmReturn.put("STATUS_DESCRIPTION", negotiationInfoBean.getStatusDescription());
            hmReturn.put("PROPOSED_START_DATE", negotiationInfoBean.getProposedStartDate());
            hmReturn.put("START_DATE", negotiationInfoBean.getStartDate());
            hmReturn.put("CLOSED_DATE", negotiationInfoBean.getClosedDate());
            hmReturn.put("TITLE", negotiationHeaderBean.getTitle());
            hmReturn.put("PI_NAME", negotiationHeaderBean.getPiName());
            hmReturn.put("LEAD_UNIT", negotiationHeaderBean.getLeadUnit());            
            hmReturn.put("SPONSOR_CODE", negotiationHeaderBean.getSponsorCode());
            hmReturn.put("SPONSOR_NAME", negotiationHeaderBean.getSponsorName());
            cvNegotiationDetails.add(hmReturn);
            cvNegotiationDetails.add(negotiationMessage);
        } catch (CoeusException ex) {
            throw ex;
        } catch (DBException dbEx) {
            dbEx.printStackTrace();
            this.transactionRollback();
            throw dbEx;
        } finally{
            this.endConnection();
        }       
        return cvNegotiationDetails;
    }
    
    /**
     *  This method used get the paramater values for the other tab of negotiation
     *  screen  from osp$parameter.
     *  <li>To fetch the data, it uses the function GET_PARAMETER_VALUE.
     *
     *  @return String parameter value
     *  @param String parammeter for which the values will returned
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public String getParameterValues(String parameter)
    throws CoeusException, DBException {
        String value = null;
        Vector param= new Vector();
        Vector result = new Vector();
        param.add(new Parameter("PARAMATER",DBEngineConstants.TYPE_STRING,parameter));
        /* calling stored function */
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{ <<OUT STRING VALUE>> = "
                    +" call GET_PARAMETER_VALUE(<< PARAMATER >> ) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            if(rowParameter.get("VALUE") != null){
                value = rowParameter.get("VALUE").toString();
            }
        }
        return value;
    }
    
    /**
     *  This method used get the activity message of negotiation
     *  @parameter CoeusVector cvNegotiationActivities
     *  @return String negotiation activity details.
     */
    public String getActivityMessage(CoeusVector cvNegotiationActivities){
        Format formatter = new SimpleDateFormat("dd-MMM-yyyy");
        String strValue ="";
        String negotiationMessage ="";
        StringBuffer message = new StringBuffer();
        message = message.append("\n");
        message = message.append(MailProperties.getProperty("negotiationNotification.activity.activityDetails")+":");
        message = message.append("\n");
        //setting the negotiation activities in the string
        for(Object negotiationObject : cvNegotiationActivities){
            NegotiationActivitiesBean negotiationActivitiesBean = (NegotiationActivitiesBean)negotiationObject;
            message = message.append("\n");
            message = message.append("------------------------------------------------------------------\n");
            message = message.append(MailProperties.getProperty("negotiationNotification.activity.activityNumber")+":\t");
            message = message.append(""+negotiationActivitiesBean.getActivityNumber()+"\n");
            message = message.append(MailProperties.getProperty("negotiationNotification.activity.activityType")+":  \t");
            message = message.append(negotiationActivitiesBean.getActivityTypeDescription()+"\n");
            message = message.append(MailProperties.getProperty("negotiationNotification.activity.activityDescription")+":    \t");
            message = message.append(negotiationActivitiesBean.getDescription()+"\n");
            message = message.append(MailProperties.getProperty("negotiationNotification.activity.activityDate")+":  \t");
            strValue = formatter.format(negotiationActivitiesBean.getActivityDate());
            message = message.append(strValue+"\n");
            message = message.append(MailProperties.getProperty("negotiationNotification.activity.createdDate")+":   \t");
            strValue = formatter.format(negotiationActivitiesBean.getCreateDate());
            message = message.append(strValue+"\n");
            message = message.append(MailProperties.getProperty("negotiationNotification.activity.followUpDate")+":  \t");
            strValue = formatter.format(negotiationActivitiesBean.getFollowUpDate());
            message = message.append(strValue+"\n");
            message = message.append(MailProperties.getProperty("negotiationNotification.activity.lastUpdatedBy")+":\t");
            message = message.append(negotiationActivitiesBean.getLastUpdatedBy()+"\n");
        }
        negotiationMessage = new String(message);
        return negotiationMessage;
    }
    //COEUSDEV - 733 Create a new notification for negotiation module - End
    
}
