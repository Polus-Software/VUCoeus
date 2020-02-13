/*
 * @(#)ProtocolDataTxnBean.java 1.0 10/24/02 10:37 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/* PMD check performed, and commented unused imports and variables on 29-NOV-2010
 * by Md.Ehtesham Ansari
 */

package edu.mit.coeus.irb.bean;

import edu.mit.coeus.mail.MailHandler;
import edu.mit.coeus.mail.bean.MailMessageInfoBean;
import edu.mit.coeus.mail.bean.PersonRecipientBean;
import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.utils.dbengine.DBEngineConstants;
//import edu.mit.coeus.utils.dbengine.ProcReqParameter;
import edu.mit.coeus.utils.dbengine.TransactionMonitor;
import edu.mit.coeus.bean.*;
//import edu.mit.coeus.utils.UtilFactory;
//import edu.mit.coeus.utils.ComboBoxBean;
//import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.departmental.bean.DepartmentPersonTxnBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.customelements.bean.CustomElementsInfoBean;
//import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
//import edu.mit.coeus.utils.CoeusVector;
//import edu.mit.coeus.utils.TypeConstants;
//import edu.mit.coeus.utils.ModuleConstants;
import edu.mit.coeus.utils.locking.LockingBean;
import edu.mit.coeus.utils.mail.MailProperties;
import edu.mit.coeus.utils.mail.MailPropertyKeys;
//Coeus Enhancement case #1799 start
import edu.mit.coeus.utils.mail.SetMailAttributes;
//import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.utils.query.Equals;
//Coeus Enhancement case #1799 end
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;


import java.util.Vector;
import java.util.HashMap;
import java.sql.Timestamp;
import java.sql.Date;
import java.sql.Connection;
import java.util.TreeSet;
import java.util.Comparator;


/**
 * This class provides the methods for performing all "get" procedure executions
 * for a Protocol functionality. Various methods are used to fetch the data for
 * "ProtocolDetailsForm" from the Database.
 * All methods are used <code>DBEngineImpl</code> instance for the
 * database interaction.
 *
 * @version 1.0 October 24, 2002, 10:37 AM
 * @author  Mukundan C
 */

public class ProtocolDataTxnBean implements TypeConstants, ModuleConstants,MailPropertyKeys{
    
    // instance of a dbEngine
    private DBEngineImpl dbEngine;
    
    private TransactionMonitor transMon;
    // Added by Shivakumar for locking enhancement
    private Connection conn = null;
    // holds the userId for the logged in user
    private String userId;
    
    private static final String rowLockStr = "osp$Protocol_";
       private static final String routingLockStr = "osp$Protocol Routing_";
    private static final char NEW_AMMENDMENT = 'N';
    
    private static final char NEW_REVISION = 'R';

    
	/*Added for case#4278-In Comments on Action Details, indicate Renewal with Amendment for submissions created via New Am -start */
	private static final char NEW_AMMENDMENT_REVISION = 'L';
	/*Added for case#4278-In Comments on Action Details, indicate Renewal with Amendment for submissions created via New Am -end */

    private char functionType;
    //Added for case #1961 start 1
    private Vector vecCorresType;
    //Added for case #1961 end 1
    private static final int REVIEW_COMPLETE = 1;
    /** Creates a new instance of ProtocolDataTxnBean */
    public ProtocolDataTxnBean() {
        dbEngine = new DBEngineImpl();
        transMon = TransactionMonitor.getInstance();
    }
    
    
    public ProtocolDataTxnBean(String userId) {
        this.userId = userId;
        dbEngine = new DBEngineImpl();
        transMon = TransactionMonitor.getInstance();
    }
    /**
     *  This method used get max specail review number for protocol and sequence number
     *  <li>To fetch the data, it uses the function GET_SPECIAL_REVIEW_NUMBER.
     *
     *  @return int review number for the protocol number and sequence number
     *  @param String protocol number  and sequence number
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public int getNextSpecialReviewNumber(String protocolNumber,int seqNumber)
    throws CoeusException, DBException {
        int reviewNumber = 0;
        Vector param= new Vector();
        Vector result = new Vector();
        param.add(new Parameter("PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,protocolNumber));
        param.addElement(new Parameter("SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,new Integer(seqNumber).toString()));
        /* calling stored function */
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{ <<OUT INTEGER REVIEWNUMBER>> = "
                    +" call GET_SPECIAL_REVIEW_NUMBER(<< PROTOCOL_NUMBER >> ,<< SEQUENCE_NUMBER >> ) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            reviewNumber = Integer.parseInt(rowParameter.get("REVIEWNUMBER").toString());
        }
        return reviewNumber;
    }
    
//    /**
//     * This method populates the list box meant to retrieve the special review
//     * in the protocol detail screen.
//     * <li>To fetch the data, it uses the procedure DW_GET_SPECIAL_REVIEW.
//     *
//     * @return Vector map of all special review code as key
//     * and special review code description as value.
//     * @exception DBException if the instance of a dbEngine is null.
//     */
//    public Vector getSpecialReviewCode() throws DBException{
//        Vector result = new Vector(3,2);
//        Vector vecSpecialReview=null;
//        HashMap hasSpecialReview=null;
//        Vector param= new Vector();
//
//        if(dbEngine!=null){
//            result = dbEngine.executeRequest("Coeus",
//                    "call GET_SPECIAL_REVIEW ( <<OUT RESULTSET rset>> )",
//                    "Coeus", param);
//        }else{
//            throw new DBException("DB instance is not available");
//        }
//        int typesCount = result.size();
//        if (typesCount >0){
//            vecSpecialReview=new Vector();
//            for(int types=0;types<typesCount;types++){
//                hasSpecialReview = (HashMap)result.elementAt(types);
//                vecSpecialReview.addElement(new ComboBoxBean(
//                        hasSpecialReview.get("SPECIAL_REVIEW_CODE").toString(),
//                        hasSpecialReview.get("DESCRIPTION").toString()));
//            }
//        }
//        return vecSpecialReview;
//    }
    /**
     * This method populates the list box meant to retrieve the special review
     * in the all the modules where special review is used.
     * <li>To fetch the data, it uses the procedure GET_SPECIAL_REVIEW_FOR_MODULE.
     *
     * @return Vector map of all special review code as key
     * and special review code description as value.
     * @exception DBException if the instance of a dbEngine is null.
     */
    public Vector getSpecialReviewTypesForModule(int moduleCode) throws DBException{
        Vector result = new Vector(3,2);
        Vector vecSpecialReview= new Vector();
        HashMap hasSpecialReview=null;
        Vector param= new Vector();
        param.add(new Parameter("AS_MODULE_CODE",
                DBEngineConstants.TYPE_INT,moduleCode));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call GET_SPECIAL_REVIEW_FOR_MODULE (<<AS_MODULE_CODE>>, <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new DBException("DB instance is not available");
        }
        int typesCount = result.size();
        if (typesCount >0){
            vecSpecialReview=new Vector();
            for(int types=0;types<typesCount;types++){
                hasSpecialReview = (HashMap)result.elementAt(types);
                vecSpecialReview.addElement(new ComboBoxBean(
                        hasSpecialReview.get("SPECIAL_REVIEW_CODE").toString(),
                        hasSpecialReview.get("DESCRIPTION").toString()));
            }
        }
        return vecSpecialReview;
    }
    
    /**
     * This method populates the list box meant to retrieve the review approval
     * in the protocol detail screen.
     * <li>To fetch the data, it uses the procedure DW_GET_SP_REV_APPROVAL_TYPE.
     *
     * @return Vector map of all review approval type as key
     * and review approval type code description as value.
     * @exception DBException if the instance of a dbEngine is null.
     */
    public Vector getReviewApprovalType() throws DBException{
        Vector result = new Vector(3,2);
        Vector vecReviewApproval=null;
        HashMap hasReviewApproval=null;
        Vector param= new Vector();
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call GET_SP_REV_APPROVAL_TYPE ( <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new DBException("DB instance is not available");
        }
        int typesCount = result.size();
        if (typesCount >0){
            vecReviewApproval=new Vector();
            for(int types=0;types<typesCount;types++){
                hasReviewApproval = (HashMap)result.elementAt(types);
                vecReviewApproval.addElement(new ComboBoxBean(
                        hasReviewApproval.get("APPROVAL_TYPE_CODE").toString(),
                        hasReviewApproval.get("DESCRIPTION").toString()));
            }
        }
        return vecReviewApproval;
    }
    
    /**
     * This method populates the list box meant to retrieve the protocol Type
     * in the protocol detail screen.
     * <li>To fetch the data, it uses the procedure get_protocol_types.
     *
     * @return Vector map of all protocol type as key
     * and protocol type description as value.
     * @exception DBException if the instance of a dbEngine is null.
     */
    public Vector getProtocolTypes() throws DBException{
        Vector result = new Vector(3,2);
        Vector vecProtocolTypes=null;
        HashMap hasProtcolType=null;
        Vector param= new Vector();
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call get_protocol_types ( <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new DBException("DB instance is not available");
        }
        int typesCount = result.size();
        if (typesCount >0){
            vecProtocolTypes=new Vector();
            for(int types=0;types<typesCount;types++){
                hasProtcolType = (HashMap)result.elementAt(types);
                vecProtocolTypes.addElement(new ComboBoxBean(
                        hasProtcolType.get("PROTOCOL_TYPE_CODE").toString(),
                        hasProtcolType.get("DESCRIPTION").toString()));
            }
        }
        return vecProtocolTypes;
    }
    
    /**
     * This method populates the list box meant to retrieve the protocol Status
     * in the protocol detail screen.
     * <li>To fetch the data, it uses the procedure get_protocol_status.
     *
     * @return Vector map of all protocol status as key
     * and protocol status description as value.
     * @exception DBException if the instance of a dbEngine is null.
     */
    public Vector getProtocolStatus() throws DBException{
        Vector result = new Vector(3,2);
        Vector vecProtocolStatus = null;
        HashMap hasProtcolStatus = null;
        Vector param= new Vector();
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call get_protocol_status ( <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new DBException("DB instance is not available");
        }
        int statusCount = result.size();
        if (statusCount >0){
            vecProtocolStatus = new Vector();
            for(int status=0;status<statusCount;status++){
                hasProtcolStatus = (HashMap)result.elementAt(status);
                vecProtocolStatus.addElement(new ComboBoxBean(
                        hasProtcolStatus.get("PROTOCOL_STATUS_CODE").toString(),
                        hasProtcolStatus.get("DESCRIPTION").toString()));
            }
        }
        return vecProtocolStatus;
    }
    
    /**
     * This method populates the list box meant to retrieve the vulnerable Type
     * in the protocol location screen.
     * <li>To fetch the data, it uses the procedure get_vulnerable_sub_types.
     *
     * @return Vector map of all vulnerable type as key
     * and vulnerable type description as value.
     * @exception DBException if the instance of a dbEngine is null.
     */
    public Vector getVulnerableSubTypes() throws DBException{
        Vector result = new Vector(3,2);
        Vector vecVulTypes = null;
        HashMap hasVulTypes = null;
        Vector param= new Vector();
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call get_vulnerable_sub_types ( <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new DBException("DB instance is not available");
        }
        int vtypesCount =result.size();
        if (vtypesCount >0){
            vecVulTypes = new Vector();
            for(int types=0;types<vtypesCount;types++){
                hasVulTypes = (HashMap)result.elementAt(types);
                vecVulTypes.addElement(new ComboBoxBean(
                        hasVulTypes.get("VULNERABLE_SUBJECT_TYPE_CODE").toString(),
                        hasVulTypes.get("DESCRIPTION").toString()));
            }
        }
        return vecVulTypes;
    }
    
    /**
     * This method populates the notes for the protocol in a vector
     * this will be displayed in the ProtocolDetailsForm.
     * To fetch the data, it uses the procedure get_protocol_notes.
     *
     * @return Vector of ProtocolNotepadBeans
     * @exception DBException if the instance of dbEngine is null.
     */
    public Vector getProtocolNotes(String protocolNumber,int seqNumber)
    throws DBException{
        Vector result = new Vector(3,2);
        Vector vecNotes = null;
        HashMap protoNotesRow = null;
        ProtocolNotepadBean protocolNotepadBean = null;
        Vector param= new Vector();
        param.addElement(new Parameter("PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,protocolNumber));
        param.addElement(new Parameter("SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,new Integer(seqNumber).toString()));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call get_protocol_notes ( <<PROTOCOL_NUMBER>> , "
                    +" <<SEQUENCE_NUMBER>> , <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new DBException("DB instance is not available");
        }
        int notesCount =result.size();
        if (notesCount >0){
            vecNotes = new Vector();
            for(int rowIndex=0;rowIndex<notesCount;rowIndex++){
                protocolNotepadBean = new ProtocolNotepadBean();
                protoNotesRow = (HashMap) result.elementAt(rowIndex);
                protocolNotepadBean.setProtocolNumber(
                        (String)protoNotesRow.get("PROTOCOL_NUMBER"));
                protocolNotepadBean.setSequenceNumber(
                        Integer.parseInt(protoNotesRow.get(
                        "SEQUENCE_NUMBER") == null ? "0" : protoNotesRow.get(
                        "SEQUENCE_NUMBER").toString()));
                protocolNotepadBean.setEntryNumber(
                        Integer.parseInt(protoNotesRow.get(
                        "ENTRY_NUMBER") == null ? "0" : protoNotesRow.get(
                        "ENTRY_NUMBER").toString()));
                protocolNotepadBean.setComments(
                        (String)protoNotesRow.get("COMMENTS"));
                protocolNotepadBean.setRestrictedFlag(
                        protoNotesRow.get("RESTRICTED_VIEW") == null ? false
                        :(protoNotesRow.get("RESTRICTED_VIEW").toString()
                        .equalsIgnoreCase("y") ? true :false));
                protocolNotepadBean.setUpdateTimestamp(
                        (Timestamp)protoNotesRow.get("UPDATE_TIMESTAMP"));
                protocolNotepadBean.setUpdateUser(
                        (String)protoNotesRow.get("UPDATE_USER"));
                /*
                 * UserId to UserName Enhancement - Start
                 * Added new property to get username
                 */
                if(protoNotesRow.get("UPDATE_USER_NAME") != null) {
                    protocolNotepadBean.setUpdateUserName((String)protoNotesRow.get("UPDATE_USER_NAME"));
                } else {
                    protocolNotepadBean.setUpdateUserName((String)protoNotesRow.get("UPDATE_USER"));
                }
                //UserId to Username enhancement - End
                vecNotes.add(protocolNotepadBean);
            }
        }
        return vecNotes;
    }
    
    /**
     * This method populates the list box meant to retrieve the Correspondent
     * Type in the protocol location screen.
     * <li>To fetch the data, it uses the procedure get_correspondent_types.
     *
     * @return Vector map of all correspondent type as key
     * and correspondent type description as value.
     * @exception DBException if the instance of a dbEngine is null.
     */
    public Vector getCorrespondentTypes() throws DBException{
        Vector result = new Vector(3,2);
        Vector vecCorrsTypes = null;
        HashMap hasCorssTypes = null;
        Vector param= new Vector();
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call get_correspondent_types ( <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new DBException("DB instance is not available");
        }
        int ctypesCount = result.size();
        if (ctypesCount >0){
            vecCorrsTypes = new Vector();
            for(int types=0;types<ctypesCount;types++){
                hasCorssTypes = (HashMap)result.elementAt(types);
                vecCorrsTypes.addElement(new ComboBoxBean(
                        hasCorssTypes.get("CORRESPONDENT_TYPE_CODE").toString(),
                        hasCorssTypes.get("DESCRIPTION").toString()));
            }
        }
        return vecCorrsTypes;
    }
    
    /**
     * This method populates the list box meant to retrieve the Fundsources
     * Type in the protocol location screen.
     * <li>To fetch the data, it uses the procedure get_funding_source_types.
     *
     * @return Vector map of all Fundsources type as key
     * and Fundsources type description as value.
     * @exception DBException if the instance of a dbEngine is null.
     */
    public Vector getFundSourceTypes() throws DBException{
        Vector result = new Vector(3,2);
        Vector vecFundSourceTypes = null;
        HashMap hasFundSourceTypes = null;
        Vector param= new Vector();
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call get_funding_source_types ( <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new DBException("DB instance is not available");
        }
        int fundTypesCount = result.size();
        if (fundTypesCount >0){
            vecFundSourceTypes = new Vector();
            for(int fund=0;fund<fundTypesCount;fund++){
                hasFundSourceTypes = (HashMap)result.elementAt(fund);
                vecFundSourceTypes.addElement(new ComboBoxBean(
                        hasFundSourceTypes.get("FUNDING_SOURCE_TYPE_CODE").toString(),
                        hasFundSourceTypes.get("DESCRIPTION").toString()));
            }
        }
        return vecFundSourceTypes;
    }
    
    
    /**
     *  Overloaded method to implement rowlocking by using Transaction monitor
     *
     *  @param protocolNumber this is given as input parameter for the
     *  procedure to execute.
     *  @param functionType to check for modify
     *  @return ProtocolInfoBean  with prptocol details for protocolDetails screen.
     *  @exception Exception if the instance of a dbEngine is null.
     */
    public ProtocolInfoBean getProtocolInfo(String protocolNumber,
            char functionType) throws Exception{
        this.functionType = functionType;
        //Commented on 16/10/2003 - No need to validate here as it is done in Servlet - start
            /*if(functionType==NEW_AMMENDMENT){
                //Check whether new Amendment can be created for this protocol.
                if(!canAmendProtocol(protocolNumber)){
                    throw new CoeusException("protocolAmendRenew_exceptionCode.2600");
                }
            }else if( functionType==NEW_REVISION){
                //Check whether new Renewal can be created for this protocol.
                if(!canRenewProtocol(protocolNumber)){
                    throw new CoeusException("protocolAmendRenew_exceptionCode.2601");
                }
            } */
        //Commented on 16/10/2003 - end
        
        ProtocolInfoBean protocolBean=getProtocolInfo(protocolNumber);
        String rowId = rowLockStr+protocolBean.getProtocolNumber();
        
        // If Amendment or Revision lock the row
        if(functionType==NEW_AMMENDMENT){
            rowId = rowId + "A";
        }else if( functionType==NEW_REVISION){
            rowId = rowId + "R";
        }
        if(transMon.canEdit(rowId))
            return protocolBean;
        else
            throw new CoeusException("exceptionCode.999999");
    }
    
    // Code added by Shivakumar for locking enhancement - BEGIN
    
    public ProtocolInfoBean getProtocolInfo(String protocolNumber,
            char functionType, String loggedinUser, String unitNumber) throws Exception{
        this.functionType = functionType;
        //Commented on 16/10/2003 - No need to validate here as it is done in Servlet - start
            /*if(functionType==NEW_AMMENDMENT){
                //Check whether new Amendment can be created for this protocol.
                if(!canAmendProtocol(protocolNumber)){
                    throw new CoeusException("protocolAmendRenew_exceptionCode.2600");
                }
            }else if( functionType==NEW_REVISION){
                //Check whether new Renewal can be created for this protocol.
                if(!canRenewProtocol(protocolNumber)){
                    throw new CoeusException("protocolAmendRenew_exceptionCode.2601");
                }
            } */
        //Commented on 16/10/2003 - end
        
        ProtocolInfoBean protocolBean=getProtocolInfo(protocolNumber);
        
        String rowId = rowLockStr+protocolBean.getProtocolNumber();
        
        // If Amendment or Revision lock the row
        if(functionType==NEW_AMMENDMENT){
            protocolBean.setPendingAmmendRenewal(hasPendingAmendmentsRenewals(protocolNumber, "A"));
            rowId = rowId + "A";
		}
		// else if( functionType==NEW_REVISION)
		/*Added for case#4278- In Comments on Action Details, indicate Renewal with Amendment for submissions created via New Am -start*/
		else if (functionType == NEW_REVISION || functionType == NEW_AMMENDMENT_REVISION)
		{
			/*Added for case#4278- In Comments on Action Details, indicate Renewal with Amendment for submissions created via New Am -end*/
            protocolBean.setPendingAmmendRenewal(hasPendingAmendmentsRenewals(protocolNumber, "R"));
            rowId = rowId + "R";
        }
        if(dbEngine!=null){
            conn = dbEngine.beginTxn();
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        LockingBean lockingBean = new LockingBean();
        lockingBean = transMon.canEdit(rowId,loggedinUser,unitNumber,conn);
        boolean lockCheck = lockingBean.isGotLock();
        if(lockCheck)
            try{
                transactionCommit();
                return protocolBean;
            }catch(DBException dbEx){
//                dbEx.printStackTrace();
                 transactionRollback();
                throw dbEx;
            } finally {
                //closed the connection -- added by Jobin
                endConnection();
            } else
                throw new CoeusException("exceptionCode.999999");
    }
    
    // Method to commit transaction
    public void transactionCommit() throws DBException{
        dbEngine.commit(conn);
    }
    
    // Method to rollback transaction
    public void transactionRollback() throws DBException {
        dbEngine.rollback(conn);
    }
    /**
     * for closing the connection
     */
    public void endConnection() throws DBException {
        dbEngine.endTxn(conn);
    }
    
    public boolean lockCheck(String protocolNumber, String loggedinUser)
    throws CoeusException, DBException{
        ProtocolInfoBean protocolBean=getProtocolInfo(protocolNumber);
        String rowId = rowLockStr+protocolBean.getProtocolNumber();
        boolean lockCheck = transMon.lockAvailabilityCheck(rowId,loggedinUser);
        return lockCheck;
    }
    
    public LockingBean getLock(String protocolNumber,String loggedinUser,String unitNumber)
    throws CoeusException, DBException{
        dbEngine=new DBEngineImpl();
        ProtocolInfoBean protocolBean=getProtocolInfo(protocolNumber);
        String rowId = rowLockStr+protocolBean.getProtocolNumber();
        if(dbEngine!=null){
            conn = dbEngine.beginTxn();
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        LockingBean lockingBean = new LockingBean();
        lockingBean = transMon.newLock(rowId, loggedinUser, unitNumber, conn);
        return lockingBean;
    }
    
    // Code added by Shivakumar for locking enhancement - END
    
    /**
     * This method get the protocolnumber flag ,application date flag and
     * approval date flag for review code and approval type code
     * <li>To fetch the data, it uses the function get_valid_sp_rev_approval_info.
     *
     * @return SPApprovalInfoBean which contains protocolnumber flag ,
     * application date flag and approval date flag.
     * @exception DBException if the instance of a dbEngine is null.
     */
    public SRApprovalInfoBean getApprovalListFlag(int reviewCode,int approvalTypeCode)
    throws DBException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap spApprovalRow = null;
        SRApprovalInfoBean sRApprovalInfoBean = null;
        param.addElement(new Parameter("REVIEW_CODE",
                DBEngineConstants.TYPE_INT,new Integer(reviewCode).toString()));
        param.addElement(new Parameter("APPROVAL_TYPE_CODE",
                DBEngineConstants.TYPE_INT,new Integer(approvalTypeCode).toString()));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call get_valid_sp_rev_approval_info( << REVIEW_CODE >> , << APPROVAL_TYPE_CODE >> , "+
                    "<< OUT STRING PROTOCOL_FLAG >>,"+
                    "<< OUT STRING APPROVAL_DATE_FLAG >> , << OUT STRING APPLICATION_DATE_FLAG >> )",
                    "Coeus", param);
        }else{
            throw new DBException("DB instance is not available");
        }
        int listSize = result.size();
        if (listSize >0){
            sRApprovalInfoBean = new SRApprovalInfoBean();
            spApprovalRow = (HashMap)result.elementAt(0);
            sRApprovalInfoBean.setProtocolFlag((String)
            spApprovalRow.get("PROTOCOL_NUMBER_FLAG"));
            sRApprovalInfoBean.setApprovalDateFlag((String)
            spApprovalRow.get("APPROVAL_DATE_FLAG"));
            sRApprovalInfoBean.setApplicationDateFlag((String)
            spApprovalRow.get("APPLICATION_DATE_FLAG"));
        }
        return sRApprovalInfoBean;
    }
    
    /**
     * This method get the valid special review list
     * <li>To fetch the data, it uses the function GET_VALID_SP_REVIEW_LIST.
     *
     * @return SRApprovalInfoBean which contains review code ,approval type code,
     * protocolnumber flag , application date flag and approval date flag.
     * @exception DBException if the instance of a dbEngine is null.
     */
    public Vector getValidSpecialReviewList()
    throws DBException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap srApprovalRow = null;
        SRApprovalInfoBean sRApprovalInfoBean = null;
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call GET_VALID_SP_REVIEW_LIST( <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new DBException("DB instance is not available");
        }
        int listSize = result.size();
        Vector approvalList = null;
        if (listSize >0){
            approvalList = new Vector(3,2);
            for(int rowIndex=0;rowIndex<listSize;rowIndex++){
                sRApprovalInfoBean = new SRApprovalInfoBean();
                srApprovalRow = (HashMap)result.elementAt(rowIndex);
                sRApprovalInfoBean.setSpecialReviewCode(Integer.parseInt(
                        srApprovalRow.get("SPECIAL_REVIEW_CODE").toString()));
                sRApprovalInfoBean.setApprovalTypeCode(Integer.parseInt(
                        srApprovalRow.get("APPROVAL_TYPE_CODE").toString()));
                sRApprovalInfoBean.setProtocolFlag((String)
                srApprovalRow.get("PROTOCOL_NUMBER_FLAG"));
                sRApprovalInfoBean.setApprovalDateFlag((String)
                srApprovalRow.get("APPROVAL_DATE_FLAG"));
                sRApprovalInfoBean.setApplicationDateFlag((String)
                srApprovalRow.get("APPLICATION_DATE_FLAG"));
                
                approvalList.add(sRApprovalInfoBean);
            }
        }
        return approvalList;
    }
    
    
    
    /**
     * Method used to get protocol details from OSP$PROTOCOL and all sub tables
     * for a given protocolnumber.
     * <li>To fetch the data, it uses get_protocol_info procedure.
     *
     * @param protocolNumber this is given as input parameter for the
     * procedure to execute.
     * @return ProtocolInfoBean  with prptocol details for protocolDetails screen.
     * @exception DBException if the instance of a dbEngine is null.
     */
    public ProtocolInfoBean getProtocolInfo(String protocolNumber)
    throws CoeusException,DBException{
        ProtocolInfoBean protocolInfoBean = null;
        try{
            protocolInfoBean = getProtocolMaintenanceDetails( protocolNumber );
            if(protocolInfoBean != null ){
                //getting the protocol keyperson list and adding to protcolinfo bean
                protocolInfoBean.setKeyStudyPersonnel(getProtocolKeyPersonList(
                        protocolInfoBean.getProtocolNumber(),
                        protocolInfoBean.getSequenceNumber()));
                
                //getting the protocol location list and adding to protcolinfo bean
                protocolInfoBean.setLocationLists(getProtocolLocationList(
                        protocolInfoBean.getProtocolNumber(),
                        protocolInfoBean.getSequenceNumber()));
                //getting the protocol vulnerableSubject list and adding to
                //protcolinfo bean
                protocolInfoBean.setVulnerableSubjectLists(
                        getProtocolVulnerableSubList(protocolInfoBean.getProtocolNumber(),
                        protocolInfoBean.getSequenceNumber()));
                //getting the protocol investigators and adding to protcolinfo bean
                protocolInfoBean.setInvestigators(getProtocolInvestigators(
                        protocolInfoBean.getProtocolNumber(),
                        protocolInfoBean.getSequenceNumber()));
                //getting the protocol correspondents and adding to protcolinfo bean
                protocolInfoBean.setCorrespondants(getProtocolCorrespondents(
                        protocolInfoBean.getProtocolNumber(),
                        protocolInfoBean.getSequenceNumber()));
                //getting the protocol arearesearch and adding to protcolinfo bean
                protocolInfoBean.setAreaOfResearch(getProtocolResearchArea(
                        protocolInfoBean.getProtocolNumber(),
                        protocolInfoBean.getSequenceNumber()));
                //getting the protocol fundsources and adding to protcolinfo bean
                protocolInfoBean.setFundingSources(getProtocolFundingSources(
                        protocolInfoBean.getProtocolNumber(),
                        protocolInfoBean.getSequenceNumber()));
                //getting the protocol action and adding to protcolinfo bean only if
                // the request is not for NEW_AMENDMENT or NEW_RENEWAL
				//                if(functionType!=NEW_AMMENDMENT && functionType!=NEW_REVISION){
				//Added for case#4278-In Comments on Action Details, indicate Renewal with Amendment for submissions created via New Am -start
				if (functionType != NEW_AMMENDMENT && functionType != NEW_REVISION && functionType != NEW_AMMENDMENT_REVISION)
				{
					//Added for case#4278-In Comments on Action Details, indicate Renewal with Amendment for submissions created via New Am -end
                    protocolInfoBean.setActions(getProtocolActions(
                            protocolInfoBean.getProtocolNumber(),
                            protocolInfoBean.getSequenceNumber()));
                    //Added for COEUSDEV-328 : Notify IRB FYI submission only permits the Aggregator to upload one document - Start
                    protocolInfoBean.setActionsDocument(getProtocolActionsDocuments(
                            protocolInfoBean.getProtocolNumber(),
                            protocolInfoBean.getSequenceNumber()));
                    //COEUSDEV-328 : End
                    
                }
                //COEUSQA:3503 - Protocol Copying - Option to Copy Attachments and Questionnaires - Start
                protocolInfoBean.setAttachments(getUploadDocumentForProtocol(protocolInfoBean.getProtocolNumber()));
                protocolInfoBean.setOtherAttachments(getProtoOtherAttachments(protocolInfoBean.getProtocolNumber()));
                //COEUSQA:3503 - End
                
                // Implemented for Special Reviews in Protocol by Raghunath P.V.
                
                //getting the protocol special reviews and adding to protcolinfo bean
                protocolInfoBean.setSpecialReviews(getProtocolSpecialReview(
                        protocolInfoBean.getProtocolNumber(),
                        protocolInfoBean.getSequenceNumber()));
                //getting the data for user roles tree view
                protocolInfoBean.setUserRolesInfoBean(getProtocolUserRoles(
                        protocolInfoBean.getProtocolNumber(),
                        protocolInfoBean.getSequenceNumber()));
                //getting the notes data for the protocol
                protocolInfoBean.setVecNotepad(getProtocolNotes(
                        protocolInfoBean.getProtocolNumber(),
                        protocolInfoBean.getSequenceNumber()));
                //getting the References data for the protocol
                protocolInfoBean.setReferences(getProtocolReferences(
                        protocolInfoBean.getProtocolNumber(),
                        protocolInfoBean.getSequenceNumber()));
                //getting Custom Elements for the Protocol
                protocolInfoBean.setCustomElements(getOthersDetails(
                        protocolInfoBean.getProtocolNumber(),
                        protocolInfoBean.getSequenceNumber()));
                //getting Protocol Related Projects for the Protocol
                protocolInfoBean.setRelatedProjects(getProtocolRelatedProjects(
                        protocolInfoBean.getProtocolNumber(),
                        protocolInfoBean.getSequenceNumber()));
                //Get Amendments/Renewals for this protocol
				//                if(functionType!=NEW_AMMENDMENT && functionType!=NEW_REVISION){
				//Added for case#4278-In Comments on Action Details, indicate Renewal with Amendment for submissions created via New Am -start
				if (functionType != NEW_AMMENDMENT && functionType != NEW_REVISION && functionType != NEW_AMMENDMENT_REVISION)
				{
					//Added for case#4278-In Comments on Action Details, indicate Renewal with Amendment for submissions created via New Am -end
                    protocolInfoBean.setAmendmentRenewal(getAllAmendmentsRenewals(
                            protocolInfoBean.getProtocolNumber()));
                } else {
                    //Added for coeus4.3 enhancements - starts
                    //Get Editable Modules for amendments/Renewals
                    ProtocolAmendRenewalBean protocolAmendRenewalBean = new ProtocolAmendRenewalBean();
                    Vector vecAmendRenewData = new Vector();
                    protocolAmendRenewalBean.setVecModuleData(
                            getAmendRenewEditableData(protocolInfoBean.getProtocolNumber()));
                    // Added with CoeusQA2313: Completion of Questionnaire for Submission
                    protocolAmendRenewalBean.setVecSelectedOrigProtoQnr(
                            getAmendRenewQuestionnaires(protocolInfoBean.getProtocolNumber()));
                    // CoeusQA2313: Completion of Questionnaire for Submission - End
                    vecAmendRenewData.add(protocolAmendRenewalBean);
                    protocolInfoBean.setAmendmentRenewal(vecAmendRenewData);                    
                }
                //Get Editable Modules for amendments/Renewals
                protocolInfoBean.setAmendRenewEditableModules(getAmendRenewEditableModules());
                //Added for coeus4.3 enhancements - ends
            }
        }catch(Exception e){
//            e.printStackTrace();
            UtilFactory.log(e.getMessage(),e,"ProtocolDataTxnBean", "getProtocolInfo");
        }
        // returning protocolinfobean to client after getting from the database.
        return protocolInfoBean;
    }
    
    
    //prps start - jan 14 2003
    
    /**
     * Method used to get protocol details from OSP$PROTOCOL and all sub tables
     * based on protocolnumber  and sequencenumber
     *
     * @param protocolNumber
     * @param sequenceNumber
     * @return ProtocolInfoBean  with prptocol details for protocolDetails screen.
     * @exception DBException if the instance of a dbEngine is null.
     */
    public ProtocolInfoBean getProtocolInfo(String protocolNumber, int sequenceNumber)
    throws CoeusException,DBException{
        ProtocolInfoBean protocolInfoBean = null;
        try{
            protocolInfoBean = getProtocolMaintenanceDetails( protocolNumber, sequenceNumber );
            if(protocolInfoBean != null ){
                //getting the protocol keyperson list and adding to protcolinfo bean
                protocolInfoBean.setKeyStudyPersonnel(getProtocolKeyPersonList(
                        protocolInfoBean.getProtocolNumber(),
                        protocolInfoBean.getSequenceNumber()));
                
                //getting the protocol location list and adding to protcolinfo bean
                protocolInfoBean.setLocationLists(getProtocolLocationList(
                        protocolInfoBean.getProtocolNumber(),
                        protocolInfoBean.getSequenceNumber()));
                //getting the protocol vulnerableSubject list and adding to
                //protcolinfo bean
                protocolInfoBean.setVulnerableSubjectLists(
                        getProtocolVulnerableSubList(protocolInfoBean.getProtocolNumber(),
                        protocolInfoBean.getSequenceNumber()));
                //getting the protocol investigators and adding to protcolinfo bean
                protocolInfoBean.setInvestigators(getProtocolInvestigators(
                        protocolInfoBean.getProtocolNumber(),
                        protocolInfoBean.getSequenceNumber()));
                //getting the protocol correspondents and adding to protcolinfo bean
                protocolInfoBean.setCorrespondants(getProtocolCorrespondents(
                        protocolInfoBean.getProtocolNumber(),
                        protocolInfoBean.getSequenceNumber()));
                //getting the protocol arearesearch and adding to protcolinfo bean
                protocolInfoBean.setAreaOfResearch(getProtocolResearchArea(
                        protocolInfoBean.getProtocolNumber(),
                        protocolInfoBean.getSequenceNumber()));
                //getting the protocol fundsources and adding to protcolinfo bean
                protocolInfoBean.setFundingSources(getProtocolFundingSources(
                        protocolInfoBean.getProtocolNumber(),
                        protocolInfoBean.getSequenceNumber()));
                //getting the protocol action and adding to protcolinfo bean only if
                // the request is not for NEW_AMENDMENT or NEW_RENEWAL
                if(functionType!=NEW_AMMENDMENT && functionType!=NEW_REVISION){
                    protocolInfoBean.setActions(getProtocolActions(
                            protocolInfoBean.getProtocolNumber(),
                            protocolInfoBean.getSequenceNumber()));
                }
                //Added for COEUSDEV-328 : Notify IRB FYI submission only permits the Aggregator to upload one document - Start
                protocolInfoBean.setActionsDocument(getProtocolActionsDocuments(
                        protocolInfoBean.getProtocolNumber(),
                        protocolInfoBean.getSequenceNumber()));
                //COEUSDEV-328 : End
                
                // Implemented for Special Reviews in Protocol by Raghunath P.V.
                
                //getting the protocol special reviews and adding to protcolinfo bean
                protocolInfoBean.setSpecialReviews(getProtocolSpecialReview(
                        protocolInfoBean.getProtocolNumber(),
                        protocolInfoBean.getSequenceNumber()));
                //getting the data for user roles tree view
                protocolInfoBean.setUserRolesInfoBean(getProtocolUserRoles(
                        protocolInfoBean.getProtocolNumber(),
                        protocolInfoBean.getSequenceNumber()));
                //getting the notes data for the protocol
                protocolInfoBean.setVecNotepad(getProtocolNotes(
                        protocolInfoBean.getProtocolNumber(),
                        protocolInfoBean.getSequenceNumber()));
                //getting the References data for the protocol
                protocolInfoBean.setReferences(getProtocolReferences(
                        protocolInfoBean.getProtocolNumber(),
                        protocolInfoBean.getSequenceNumber()));
                //getting Custom Elements for the Protocol
                protocolInfoBean.setCustomElements(getOthersDetails(
                        protocolInfoBean.getProtocolNumber(),
                        protocolInfoBean.getSequenceNumber()));
                //getting Protocol Related Projects for the Protocol
                protocolInfoBean.setRelatedProjects(getProtocolRelatedProjects(
                        protocolInfoBean.getProtocolNumber(),
                        protocolInfoBean.getSequenceNumber()));
                //Get Amendments/Renewals for this protocol
                if(functionType!=NEW_AMMENDMENT && functionType!=NEW_REVISION){
                    protocolInfoBean.setAmendmentRenewal(getAllAmendmentsRenewals(
                            protocolInfoBean.getProtocolNumber()));
                }
            }
        }catch(Exception e){
//            e.printStackTrace();
            UtilFactory.log(e.getMessage(),e,"ProtocolDataTxnBean", "getProtocolInfo");
        }
        // returning protocolinfobean to client after getting from the database.
        return protocolInfoBean;
    }
    
    //prps end - jan 14 2003
    //Coeus enhancement 32.2 added by shiji - step 1 : start
    public boolean getTrainingFlag(String personId) throws CoeusException,DBException{
        boolean success = false ;
        // get the new seq id and do the updation
        Vector param= new Vector();
        
        HashMap row = null;
        Vector result = new Vector();
        
        param= new Vector();
        param.add(new Parameter("PERSON_ID",
                DBEngineConstants.TYPE_STRING, personId )) ;
        
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{<<OUT INTEGER SUCCESS>> = call fn_get_inv_training_status( "
                    + " << PERSON_ID >> )}", param) ;
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        
        if(!result.isEmpty()){
            row = (HashMap)result.elementAt(0);
            success = Integer.parseInt(row.get("SUCCESS").toString()) == 1 ? true : false;
        }
        return success ;
        
    }
    //Coeus enhancement 32.2 - step 2 : end
    
    /**
     * Method used to get protocol details from OSP$PROTOCOL for a given
     * protocolnumber.
     * <li>To fetch the data, it uses get_protocol_info procedure.
     *
     * @param protocolNumber this is given as input parameter for the
     * procedure to execute.
     * @return ProtocolInfoBean  with prptocol details for protocolDetails screen.
     * @exception DBException if the instance of a dbEngine is null.
     */
    
    public ProtocolInfoBean getProtocolMaintenanceDetails(String protocolNumber )
    throws CoeusException, DBException {
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap infoProtocolRow = null;
        ProtocolInfoBean protocolInfoBean = null;
        param.addElement(new Parameter("PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,protocolNumber));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call get_protocol_info( <<PROTOCOL_NUMBER>> , "
                    +" <<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new DBException("DB instance is not available");
        }
        if(!result.isEmpty()){
            protocolInfoBean = new ProtocolInfoBean();
            infoProtocolRow = (HashMap)result.elementAt(0);
            protocolInfoBean.setProtocolNumber((String)
            infoProtocolRow.get("PROTOCOL_NUMBER"));
            protocolInfoBean.setSequenceNumber(Integer.parseInt(
                    infoProtocolRow.get("SEQUENCE_NUMBER") == null ? "0"
                    :infoProtocolRow.get("SEQUENCE_NUMBER").toString()));
            protocolInfoBean.setProtocolTypeCode(Integer.parseInt(
                    infoProtocolRow.get("PROTOCOL_TYPE_CODE").toString() == null ? "0"
                    :infoProtocolRow.get("PROTOCOL_TYPE_CODE").toString()));
            protocolInfoBean.setProtocolTypeDesc((String)
            infoProtocolRow.get("PROTOCOL_TYPE_DESCRIPTION"));
            protocolInfoBean.setProtocolStatusCode(Integer.parseInt(
                    infoProtocolRow.get("PROTOCOL_STATUS_CODE").toString() == null ? "0"
                    :infoProtocolRow.get("PROTOCOL_STATUS_CODE").toString()));
            protocolInfoBean.setAw_ProtocolStatusCode(Integer.parseInt(
                    infoProtocolRow.get("PROTOCOL_STATUS_CODE").toString() == null ? "0"
                    :infoProtocolRow.get("PROTOCOL_STATUS_CODE").toString()));
            protocolInfoBean.setProtocolStatusDesc((String)
            infoProtocolRow.get("PROTOCOL_STATUS_DESCRIPTION"));
            protocolInfoBean.setTitle((String)
            infoProtocolRow.get("TITLE"));
            protocolInfoBean.setDescription((String)
            infoProtocolRow.get("DESCRIPTION"));
            protocolInfoBean.setSpecialReviewIndicator((String)
            infoProtocolRow.get("SPECIAL_REVIEW_INDICATOR"));
            protocolInfoBean.setVulnerableSubjectIndicator((String)
            infoProtocolRow.get("VULNERABLE_SUBJECT_INDICATOR"));
            protocolInfoBean.setApplicationDate(
                    infoProtocolRow.get("APPLICATION_DATE") == null ? null
                    :new Date( ((Timestamp) infoProtocolRow.get(
                    "APPLICATION_DATE")).getTime()) );
            protocolInfoBean.setApprovalDate(
                    infoProtocolRow.get("APPROVAL_DATE") == null ? null
                    :new Date( ((Timestamp) infoProtocolRow.get(
                    "APPROVAL_DATE")).getTime()) );
            protocolInfoBean.setExpirationDate(
                    infoProtocolRow.get("EXPIRATION_DATE") == null ? null
                    :new Date( ((Timestamp) infoProtocolRow.get(
                    "EXPIRATION_DATE")).getTime()) );
            //Last Approval Date Start
            protocolInfoBean.setLastApprovalDate(
                    infoProtocolRow.get("LAST_APPROVAL_DATE") == null ? null
                    :new Date( ((Timestamp) infoProtocolRow.get(
                    "LAST_APPROVAL_DATE")).getTime()) );
            //Last approval Date End
            protocolInfoBean.setFDAApplicationNumber((String)
            infoProtocolRow.get("FDA_APPLICATION_NUMBER"));
            protocolInfoBean.setRefNum_1( (String)
            infoProtocolRow.get("REFERENCE_NUMBER_1"));
            protocolInfoBean.setRefNum_2( (String)
            infoProtocolRow.get("REFERENCE_NUMBER_2"));
            protocolInfoBean.setKeyStudyIndicator((String)
            infoProtocolRow.get("KEY_STUDY_PERSON_INDICATOR"));
            protocolInfoBean.setFundingSourceIndicator((String)
            infoProtocolRow.get("FUNDING_SOURCE_INDICATOR"));
            protocolInfoBean.setCorrespondenceIndicator((String)
            infoProtocolRow.get("CORRESPONDENT_INDICATOR"));
            protocolInfoBean.setReferenceIndicator((String)
            infoProtocolRow.get("REFERENCE_INDICATOR"));
            protocolInfoBean.setProjectsIndicator((String)
            infoProtocolRow.get("RELATED_PROJECTS_INDICATOR"));
            protocolInfoBean.setIsBillableFlag(infoProtocolRow.get(
                    "IS_BILLABLE") == null ? false :
                        (infoProtocolRow.get("IS_BILLABLE").toString()
                        .equalsIgnoreCase("y") ? true :false));
            protocolInfoBean.setCreateTimestamp(
                    (Timestamp)infoProtocolRow.get("CREATE_TIMESTAMP"));
            protocolInfoBean.setCreateUser((String)
            infoProtocolRow.get("CREATE_USER"));
            protocolInfoBean.setUpdateTimestamp(
                    (Timestamp)infoProtocolRow.get("UPDATE_TIMESTAMP"));
            protocolInfoBean.setUpdateUser((String)
            infoProtocolRow.get("UPDATE_USER"));
        }
        return protocolInfoBean;
    }
    
    
    //prps start - jan 14 2003
    
    /**
     * Method used to get protocol details from OSP$PROTOCOL a given
     * for a particular sequence of a protocol
     * <li>To fetch the data, it uses get_proto_info_for_seq procedure.
     *
     * @param protocolNumber
     * @param sequenceNumber
     * @return ProtocolInfoBean  with protocol details for protocolDetails screen.
     * @exception DBException if the instance of a dbEngine is null.
     */
    
    public ProtocolInfoBean getProtocolMaintenanceDetails(String protocolNumber , int sequenceNumber)
    throws CoeusException, DBException {
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap infoProtocolRow = null;
        ProtocolInfoBean protocolInfoBean = null;
        param.addElement(new Parameter("AW_PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,protocolNumber));
        param.addElement(new Parameter("AW_SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT, String.valueOf(sequenceNumber)));
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call get_proto_info_for_seq( << AW_PROTOCOL_NUMBER >> , << AW_SEQUENCE_NUMBER >> , "
                    +" <<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new DBException("DB instance is not available");
        }
        if(!result.isEmpty()){
            protocolInfoBean = new ProtocolInfoBean();
            infoProtocolRow = (HashMap)result.elementAt(0);
            protocolInfoBean.setProtocolNumber((String)
            infoProtocolRow.get("PROTOCOL_NUMBER"));
            protocolInfoBean.setSequenceNumber(Integer.parseInt(
                    infoProtocolRow.get("SEQUENCE_NUMBER") == null ? "0"
                    :infoProtocolRow.get("SEQUENCE_NUMBER").toString()));
            protocolInfoBean.setProtocolTypeCode(Integer.parseInt(
                    infoProtocolRow.get("PROTOCOL_TYPE_CODE").toString() == null ? "0"
                    :infoProtocolRow.get("PROTOCOL_TYPE_CODE").toString()));
            protocolInfoBean.setProtocolTypeDesc((String)
            infoProtocolRow.get("PROTOCOL_TYPE_DESCRIPTION"));
            protocolInfoBean.setProtocolStatusCode(Integer.parseInt(
                    infoProtocolRow.get("PROTOCOL_STATUS_CODE").toString() == null ? "0"
                    :infoProtocolRow.get("PROTOCOL_STATUS_CODE").toString()));
            protocolInfoBean.setAw_ProtocolStatusCode(Integer.parseInt(
                    infoProtocolRow.get("PROTOCOL_STATUS_CODE").toString() == null ? "0"
                    :infoProtocolRow.get("PROTOCOL_STATUS_CODE").toString()));
            protocolInfoBean.setProtocolStatusDesc((String)
            infoProtocolRow.get("PROTOCOL_STATUS_DESCRIPTION"));
            protocolInfoBean.setTitle((String)
            infoProtocolRow.get("TITLE"));
            protocolInfoBean.setDescription((String)
            infoProtocolRow.get("DESCRIPTION"));
            protocolInfoBean.setSpecialReviewIndicator((String)
            infoProtocolRow.get("SPECIAL_REVIEW_INDICATOR"));
            protocolInfoBean.setVulnerableSubjectIndicator((String)
            infoProtocolRow.get("VULNERABLE_SUBJECT_INDICATOR"));
            protocolInfoBean.setApplicationDate(
                    infoProtocolRow.get("APPLICATION_DATE") == null ? null
                    :new Date( ((Timestamp) infoProtocolRow.get(
                    "APPLICATION_DATE")).getTime()) );
            protocolInfoBean.setApprovalDate(
                    infoProtocolRow.get("APPROVAL_DATE") == null ? null
                    :new Date( ((Timestamp) infoProtocolRow.get(
                    "APPROVAL_DATE")).getTime()) );
            protocolInfoBean.setExpirationDate(
                    infoProtocolRow.get("EXPIRATION_DATE") == null ? null
                    :new Date( ((Timestamp) infoProtocolRow.get(
                    "EXPIRATION_DATE")).getTime()) );
            protocolInfoBean.setFDAApplicationNumber((String)
            infoProtocolRow.get("FDA_APPLICATION_NUMBER"));
            protocolInfoBean.setRefNum_1( (String)
            infoProtocolRow.get("REFERENCE_NUMBER_1"));
            protocolInfoBean.setRefNum_2( (String)
            infoProtocolRow.get("REFERENCE_NUMBER_2"));
            protocolInfoBean.setKeyStudyIndicator((String)
            infoProtocolRow.get("KEY_STUDY_PERSON_INDICATOR"));
            protocolInfoBean.setFundingSourceIndicator((String)
            infoProtocolRow.get("FUNDING_SOURCE_INDICATOR"));
            protocolInfoBean.setCorrespondenceIndicator((String)
            infoProtocolRow.get("CORRESPONDENT_INDICATOR"));
            protocolInfoBean.setReferenceIndicator((String)
            infoProtocolRow.get("REFERENCE_INDICATOR"));
            protocolInfoBean.setProjectsIndicator((String)
            infoProtocolRow.get("RELATED_PROJECTS_INDICATOR"));
            protocolInfoBean.setIsBillableFlag(infoProtocolRow.get(
                    "IS_BILLABLE") == null ? false :
                        (infoProtocolRow.get("IS_BILLABLE").toString()
                        .equalsIgnoreCase("y") ? true :false));
            //Added for Case#3087 - In Premium - Review History Add the following elements - Start
            protocolInfoBean.setLastApprovalDate(
                    infoProtocolRow.get("LAST_APPROVAL_DATE") == null ? null
                    :new Date( ((Timestamp) infoProtocolRow.get(
                    "LAST_APPROVAL_DATE")).getTime()) );
            //Added for Case#3087 - In Premium - Review History Add the following elements - End
            protocolInfoBean.setCreateTimestamp(
                    (Timestamp)infoProtocolRow.get("CREATE_TIMESTAMP"));
            protocolInfoBean.setCreateUser((String)
            infoProtocolRow.get("CREATE_USER"));
            protocolInfoBean.setUpdateTimestamp(
                    (Timestamp)infoProtocolRow.get("UPDATE_TIMESTAMP"));
            protocolInfoBean.setUpdateUser((String)
            infoProtocolRow.get("UPDATE_USER"));
        }
        return protocolInfoBean;
    }
    
    //prps end - jan 14 2003
    
    /**
     * Method used to get the Protocol Summary details including last submission
     * details if available.
     *
     * @param protocolNumber String representing the Protocol Number
     * @returns Vector which contains ProtocolInfoBean and ProtocolSubmissionInfoBean
     * as elements.
     */
    public ProtocolInfoBean getProtocolSummaryDetails( String protocolNumber )
    throws CoeusException, DBException {
//        Vector summaryDetails = null;
        ProtocolInfoBean infoBean = getProtocolMaintenanceDetails( protocolNumber );
        if( infoBean != null ) {
//            summaryDetails = new Vector();
            //getting the protocol investigators and adding to protcolinfo bean
            infoBean.setInvestigators(getProtocolInvestigators(
                    infoBean.getProtocolNumber(),infoBean.getSequenceNumber()));
            //getting the protocol arearesearch and adding to protcolinfo bean
            infoBean.setAreaOfResearch(getProtocolResearchArea(
                    infoBean.getProtocolNumber(),infoBean.getSequenceNumber()));
            //getting the protocol keyperson list and adding to protcolinfo bean
            infoBean.setKeyStudyPersonnel(getProtocolKeyPersonList(
                    infoBean.getProtocolNumber(),infoBean.getSequenceNumber()));
            //getting Notes info and set it to protcolinfo bean
            infoBean.setVecNotepad(getProtocolNotes(
                    infoBean.getProtocolNumber(),infoBean.getSequenceNumber()));
            
            /*ProtocolSubmissionTxnBean submissionTxnBean = new ProtocolSubmissionTxnBean();
            ProtocolSubmissionInfoBean submissionBean =
                    submissionTxnBean.getProtocolSubmissionDetails( protocolNumber );
            summaryDetails.addElement( infoBean );
            if( submissionBean != null ) {
                summaryDetails.addElement( submissionBean );
            }*/
        }
        //return summaryDetails;
        return infoBean;
    }
    
    /**
     * Method used to get protocolkeyperson list from OSP$PROTOCOL_KEY_PERSONS
     * for a given protocolnumber and seqnumber.
     * <li>To fetch the data, it uses get_proto_key_person_list procedure.
     *
     * @param protocolNumber get list of protocolKeypersonList for this id
     * @param seqNumber input to the procedure
     * @return Vector map of protocolkeyperson list data is set of
     * ProtocolKeyPersonnelBean.
     * @exception DBException if the instance of a dbEngine is null.
     */
    public Vector getProtocolKeyPersonList(String protocolNumber,int seqNumber)
    throws DBException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap keyPersonListRow = null;
        ProtocolKeyPersonnelBean protocolKeyPersonnelBean = null;
        param.addElement(new Parameter("PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,protocolNumber));
        param.addElement(new Parameter("SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,new Integer(seqNumber).toString()));
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call get_proto_key_person_list( <<PROTOCOL_NUMBER>> , "
                    +" <<SEQUENCE_NUMBER>> , <<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new DBException("DB instance is not available");
        }
        int listSize = result.size();
        Vector keyList = null;
        if (listSize >0){
            keyList = new Vector(3,2);
            for(int rowIndex=0;rowIndex<listSize;rowIndex++){
                protocolKeyPersonnelBean = new ProtocolKeyPersonnelBean();
                keyPersonListRow = (HashMap)result.elementAt(rowIndex);
                protocolKeyPersonnelBean.setProtocolNumber(
                        (String)keyPersonListRow.get("PROTOCOL_NUMBER"));
                protocolKeyPersonnelBean.setSequenceNumber(
                        Integer.parseInt(keyPersonListRow.get(
                        "SEQUENCE_NUMBER") == null ? "0" :keyPersonListRow.get(
                        "SEQUENCE_NUMBER").toString()));
                protocolKeyPersonnelBean.setPersonId((String)
                keyPersonListRow.get("PERSON_ID"));
                protocolKeyPersonnelBean.setPersonName((String)
                keyPersonListRow.get("PERSON_NAME"));
                protocolKeyPersonnelBean.setPersonRole((String)
                keyPersonListRow.get("PERSON_ROLE"));
                protocolKeyPersonnelBean.setNonEmployeeFlag(keyPersonListRow.get(
                        "NON_EMPLOYEE_FLAG") == null ? false :
                            (keyPersonListRow.get("NON_EMPLOYEE_FLAG").toString()
                            .equalsIgnoreCase("y") ? true :false));
                
                /*protocolKeyPersonnelBean.setFacultyFlag(keyPersonListRow.get(
                        "FACULTY_FLAG") == null ? false :
                            (keyPersonListRow.get( "FACULTY_FLAG").toString()
                                    .equalsIgnoreCase("y") ? true :false));*/
                
                protocolKeyPersonnelBean.setAffiliationTypeCode(Integer.parseInt(keyPersonListRow.get("AFFILIATION_TYPE_CODE").toString()));
                protocolKeyPersonnelBean.setAffiliationTypeDescription((String)keyPersonListRow.get("DESCRIPTION"));
                protocolKeyPersonnelBean.setUpdateTimestamp(
                        (Timestamp)keyPersonListRow.get("UPDATE_TIMESTAMP"));
                protocolKeyPersonnelBean.setUpdateUser( (String)
                keyPersonListRow.get("UPDATE_USER"));
                
                keyList.add(protocolKeyPersonnelBean);
            }
        }
        return keyList;
    }
    
    /**
     * Method used to get protocollocation list from OSP$PROTOCOL_LOCATION for
     * a given protocolnumber and seqnumber.
     * <li>To fetch the data, it uses get_proto_location_list procedure.
     *
     * @param protocolNumber get list of protocolLoctionListt for this id
     * @param seqNumber input to the procedure
     * @return Vector map of protocollocation list data is set of
     * ProtocolLocationListBean.
     * @exception DBException if the instance of a dbEngine is null.
     */
    public Vector getProtocolLocationList(String protocolNumber,int seqNumber)
    throws DBException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap locationProtocolListRow = null;
        ProtocolLocationListBean protocolLocationListBean = null;
        param.addElement(new Parameter("PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,protocolNumber));
        param.addElement(new Parameter("SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,new Integer(seqNumber).toString()));
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call get_proto_location_list( <<PROTOCOL_NUMBER>> , "
                    +" <<SEQUENCE_NUMBER>> , <<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new DBException("DB instance is not available");
        }
        int listSize = result.size();
        StringBuffer strBffr ;
        Vector locationList = null;
        if(listSize >0){
            locationList = new Vector(3,2);
            for(int rowIndex=0;rowIndex<listSize;rowIndex++){
                strBffr = new StringBuffer();
                protocolLocationListBean = new ProtocolLocationListBean();
                locationProtocolListRow = (HashMap)result.elementAt(rowIndex);
                protocolLocationListBean.setProtocolNumber(
                        (String)locationProtocolListRow.get(
                        "PROTOCOL_NUMBER"));
                protocolLocationListBean.setSequenceNumber(
                        Integer.parseInt(locationProtocolListRow.get(
                        "SEQUENCE_NUMBER")== null ? "0" :
                            locationProtocolListRow.get("SEQUENCE_NUMBER").toString()));
                
                protocolLocationListBean.setOrganizationId((String)
                locationProtocolListRow.get("ORGANIZATION_ID"));
                protocolLocationListBean.setOrganizationName((String)
                locationProtocolListRow.get("ORGANIZATION_NAME"));
                protocolLocationListBean.setRolodexId(Integer.parseInt(
                        locationProtocolListRow.get("ROLODEX_ID") == null
                        ? "0" : locationProtocolListRow.get("ROLODEX_ID").toString()));
                strBffr.append((String)
                locationProtocolListRow.get("ORGANIZATION") == null
                        ? "" :locationProtocolListRow.get("ORGANIZATION")
                        .toString()+ "$#");
                strBffr.append((String)
                locationProtocolListRow.get("ADDRESS_LINE_1") == null
                        ? "" :locationProtocolListRow.get("ADDRESS_LINE_1")
                        .toString()+ "$#");
                strBffr.append((String)
                locationProtocolListRow.get("ADDRESS_LINE_2") == null
                        ? "" :locationProtocolListRow.get("ADDRESS_LINE_2")
                        .toString()+ "$#");
                strBffr.append((String)
                locationProtocolListRow.get("ADDRESS_LINE_3") == null
                        ? "" :locationProtocolListRow.get("ADDRESS_LINE_3")
                        .toString()+ "$#");
                strBffr.append((String)
                locationProtocolListRow.get("CITY") == null
                        ? "" :locationProtocolListRow.get("CITY").toString()+ "$#");
                strBffr.append((String)
                locationProtocolListRow.get("COUNTY") == null
                        ? "" :locationProtocolListRow.get("COUNTY").toString()+ "$#");
                strBffr.append((String)
                locationProtocolListRow.get("STATE") == null
                        ? "" :locationProtocolListRow.get("STATE").toString()+ " - ");
                strBffr.append((String)
                locationProtocolListRow.get("POSTAL_CODE") == null
                        ? "" :locationProtocolListRow.get("POSTAL_CODE")
                        .toString()+ "$#");
                strBffr.append((String)
                locationProtocolListRow.get("COUNTRY_CODE") == null
                        ? "" :locationProtocolListRow.get("COUNTRY_CODE").toString());
                protocolLocationListBean.setAddress( (String) strBffr.toString());
                
                protocolLocationListBean.setOrganizationTypeId(Integer.parseInt(
                        locationProtocolListRow.get("PROTOCOL_ORG_TYPE_CODE") == null ? "0" :
                            locationProtocolListRow.get("PROTOCOL_ORG_TYPE_CODE").toString()));
                
                protocolLocationListBean.setOrganizationTypeName((String)
                locationProtocolListRow.get("DESCRIPTION"));
                
                protocolLocationListBean.setUpdateTimestamp(
                        (Timestamp)locationProtocolListRow.get("UPDATE_TIMESTAMP"));
                protocolLocationListBean.setUpdateUser((String)
                locationProtocolListRow.get("UPDATE_USER"));
                
                locationList.add(protocolLocationListBean);
            }
        }
        return locationList;
    }
    
    /**
     * Method used to get protocolvulnerable list from
     * OSP$PROTOCOL_VULNERABLE_SUB for a given protocolnumber and seqnumber.
     * <li>To fetch the data, it uses get_proto_vulnerable_sub_list.
     *
     * @param protocolNumber get list of protocolVulnerableList for this id
     * @param seqNumber input to the procedure
     * @return Vector map of protocolvulnerable list data is set of
     * ProtocolVulnerableSubListsBean.
     * @exception DBException if the instance of a dbEngine is null.
     */
    public Vector getProtocolVulnerableSubList(String protocolNumber,
            int seqNumber) throws DBException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap vulnerableProtocolListRow = null;
        ProtocolVulnerableSubListsBean protocolVulnerableSubListsBean = null;
        param.addElement(new Parameter("PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,protocolNumber));
        param.addElement(new Parameter("SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,new Integer(seqNumber).toString()));
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call get_proto_vulnerable_sub_list( <<PROTOCOL_NUMBER>> , "
                    +" <<SEQUENCE_NUMBER>> , <<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new DBException("DB instance is not available");
        }
        int listSize = result.size();
        Vector vulnerableList = null;
        if(listSize>0){
            vulnerableList = new Vector(3,2);
            for(int rowIndex=0;rowIndex<listSize;rowIndex++){
                protocolVulnerableSubListsBean =
                        new ProtocolVulnerableSubListsBean();
                vulnerableProtocolListRow = (HashMap)result.elementAt(
                        rowIndex);
                protocolVulnerableSubListsBean.setProtocolNumber(
                        (String)vulnerableProtocolListRow.get(
                        "PROTOCOL_NUMBER"));
                protocolVulnerableSubListsBean.setSequenceNumber(
                        Integer.parseInt(vulnerableProtocolListRow.get(
                        "SEQUENCE_NUMBER") == null ? "0" :
                            vulnerableProtocolListRow.get(
                        "SEQUENCE_NUMBER").toString()));
                protocolVulnerableSubListsBean.setVulnerableSubjectTypeCode(
                        Integer.parseInt(vulnerableProtocolListRow.get(
                        "VULNERABLE_SUBJECT_TYPE_CODE") == null ? "0" :
                            vulnerableProtocolListRow.get(
                        "VULNERABLE_SUBJECT_TYPE_CODE").toString()));
                protocolVulnerableSubListsBean.setVulnerableSubjectTypeDesc(
                        (String)vulnerableProtocolListRow.get("DESCRIPTION"));
                
                //Protocol Enhancement - Saving null in db Start
                /*protocolVulnerableSubListsBean.setSubjectCount(
                    Integer.parseInt(vulnerableProtocolListRow.get(
                    "SUBJECT_COUNT") == null ? "0" :vulnerableProtocolListRow.get(
                        "SUBJECT_COUNT").toString()));*/
                protocolVulnerableSubListsBean.setSubjectCount(
                        vulnerableProtocolListRow.get("SUBJECT_COUNT") == null ?
                            null :new Integer( vulnerableProtocolListRow.get("SUBJECT_COUNT").toString() ) );
                
                //Protocol Enhancement - Saving null in db End
                protocolVulnerableSubListsBean.setUpdateTimestamp(
                        (Timestamp)vulnerableProtocolListRow.get("UPDATE_TIMESTAMP"));
                protocolVulnerableSubListsBean.setUpdateUser(
                        (String)vulnerableProtocolListRow.get("UPDATE_USER"));
                
                vulnerableList.add(protocolVulnerableSubListsBean);
            }
        }
        return vulnerableList;
    }
    
    /**
     * Method used to get protocolinvestigators from  OSP$PROTOCOL_INVESTIGATORS
     * for a given protocolnumber and seqnumber.
     * <li>To fetch the data, it uses get_proto_investigators.
     *
     * @param protocolNumber get list of protocolInvestigatorList for this id
     * @param seqNumber input to the procedure
     * @return Vector map of protocolinvestigator list data is set of
     * ProtocolInvestigatorsBean.
     * @exception DBException if the instance of a dbEngine is null.
     */
    public Vector getProtocolInvestigators(String protocolNumber,int seqNumber)
    throws DBException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap investigatorProtocolRow = null;
        ProtocolInvestigatorsBean protocolInvestigatorsBean = null;
        param.addElement(new Parameter("PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,protocolNumber));
        param.addElement(new Parameter("SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,new Integer(seqNumber).toString()));
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call get_proto_investigators( <<PROTOCOL_NUMBER>> , "
                    +" <<SEQUENCE_NUMBER>> , <<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new DBException("DB instance is not available");
        }
        int listSize = result.size();
        Vector investigatorList = null;
        if(listSize>0){
            investigatorList = new Vector(3,2);
            for(int rowIndex=0;rowIndex<listSize;rowIndex++){
                protocolInvestigatorsBean = new ProtocolInvestigatorsBean();
                investigatorProtocolRow = (HashMap)result.elementAt(rowIndex);
                protocolInvestigatorsBean.setProtocolNumber(
                        (String)investigatorProtocolRow.get("PROTOCOL_NUMBER"));
                protocolInvestigatorsBean.setSequenceNumber(
                        Integer.parseInt(investigatorProtocolRow.get(
                        "SEQUENCE_NUMBER") == null ? "0" :
                            investigatorProtocolRow.get(
                        "SEQUENCE_NUMBER").toString()));
                protocolInvestigatorsBean.setPersonId((String)
                investigatorProtocolRow.get("PERSON_ID"));
                protocolInvestigatorsBean.setPersonName((String)
                investigatorProtocolRow.get("PERSON_NAME"));
                protocolInvestigatorsBean.setNonEmployeeFlag(
                        investigatorProtocolRow.get(
                        "NON_EMPLOYEE_FLAG") == null ? false
                        :(investigatorProtocolRow.get("NON_EMPLOYEE_FLAG"
                        ).toString().equalsIgnoreCase("y") ? true :false));
                protocolInvestigatorsBean.setPrincipalInvestigatorFlag(
                        investigatorProtocolRow.get(
                        "PRINCIPAL_INVESTIGATOR_FLAG") == null ? false
                        :(investigatorProtocolRow.get("PRINCIPAL_INVESTIGATOR_FLAG"
                        ).toString().equalsIgnoreCase("y") ? true :false));
                
                /*protocolInvestigatorsBean.setFacultyFlag(
                    investigatorProtocolRow.get(
                    "FACULTY_FLAG") == null ? false
                    :(investigatorProtocolRow.get("FACULTY_FLAG"
                    ).toString().equalsIgnoreCase("y") ? true :false));*/
                
                protocolInvestigatorsBean.setUpdateTimestamp(
                        (Timestamp)investigatorProtocolRow.get("UPDATE_TIMESTAMP"));
                protocolInvestigatorsBean.setUpdateUser((String)
                investigatorProtocolRow.get("UPDATE_USER"));
                protocolInvestigatorsBean.setAffiliationTypeCode(Integer.parseInt(investigatorProtocolRow.get("AFFILIATION_TYPE_CODE").toString()));
                protocolInvestigatorsBean.setAffiliationTypeDescription((String)investigatorProtocolRow.get("DESCRIPTION"));
                
                protocolInvestigatorsBean.setInvestigatorUnits(
                        getProtocolInvestigatorsUnits(
                        protocolInvestigatorsBean.getProtocolNumber(),
                        protocolInvestigatorsBean.getSequenceNumber(),
                        protocolInvestigatorsBean.getPersonId()));
                
                investigatorList.add(protocolInvestigatorsBean);
            }
        }
        return investigatorList;
    }
    
    /**
     * Method used to get protocolinvestigators from  OSP$PROTOCOL_UNITS
     * for a given protocolnumber , seqnumber and personID.
     * <li>To fetch the data, it uses get_proto_investigator_units.
     *
     * @param protocolNumber get list of protocolInvestigatorUnitList for this id
     * @param seqNumber input to the procedure
     * @param personID input to the procedure
     * @return Vector map of protocolinvestigatorUnits data is set of
     * ProtocolInvestigatorUnitsBean.
     * @exception DBException if the instance of a dbEngine is null.
     */
    public Vector getProtocolInvestigatorsUnits(String protocolNumber,
            int seqNumber,String personID) throws DBException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap invesUnitsProtocolRow = null;
        ProtocolInvestigatorUnitsBean protocolInvestigatorUnitsBean = null;
        param.addElement(new Parameter("PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,protocolNumber));
        param.addElement(new Parameter("SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,new Integer(seqNumber).toString()));
        param.addElement(new Parameter("PERSON_ID",
                DBEngineConstants.TYPE_STRING,personID));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call get_proto_investigator_units ( <<PROTOCOL_NUMBER>> , "
                    +" <<SEQUENCE_NUMBER>> , <<PERSON_ID>> , <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new DBException("DB instance is not available");
        }
        int listSize = result.size();
        Vector investigatorUnitList =null;
        if(listSize>0){
            investigatorUnitList = new Vector(3,2);
            for(int rowIndex=0;rowIndex<listSize;rowIndex++){
                protocolInvestigatorUnitsBean =
                        new ProtocolInvestigatorUnitsBean();
                invesUnitsProtocolRow = (HashMap)result.elementAt(rowIndex);
                protocolInvestigatorUnitsBean.setProtocolNumber(
                        (String)invesUnitsProtocolRow.get("PROTOCOL_NUMBER"));
                protocolInvestigatorUnitsBean.setSequenceNumber(
                        Integer.parseInt(invesUnitsProtocolRow.get(
                        "SEQUENCE_NUMBER") == null ? "0" :
                            invesUnitsProtocolRow.get(
                        "SEQUENCE_NUMBER").toString()));
                protocolInvestigatorUnitsBean.setUnitNumber(
                        (String)invesUnitsProtocolRow.get("UNIT_NUMBER"));
                protocolInvestigatorUnitsBean.setUnitName(
                        (String)invesUnitsProtocolRow.get("UNIT_NAME"));
                protocolInvestigatorUnitsBean.setPersonName(
                        (String)invesUnitsProtocolRow.get("OSP_ADMINISTRATOR_NAME"));
                protocolInvestigatorUnitsBean.setLeadUnitFlag(
                        invesUnitsProtocolRow.get(
                        "LEAD_UNIT_FLAG") == null ? false
                        :(invesUnitsProtocolRow.get("LEAD_UNIT_FLAG"
                        ).toString().equalsIgnoreCase("y")
                        ? true :false));
                protocolInvestigatorUnitsBean.setPersonId(
                        (String)invesUnitsProtocolRow.get("PERSON_ID"));
                protocolInvestigatorUnitsBean.setUpdateTimestamp(
                        (Timestamp)invesUnitsProtocolRow.get("UPDATE_TIMESTAMP"));
                protocolInvestigatorUnitsBean.setUpdateUser(
                        (String)invesUnitsProtocolRow.get("UPDATE_USER"));
                
                investigatorUnitList.add(protocolInvestigatorUnitsBean);
            }
        }
        return investigatorUnitList;
    }
    
    /**
     * Method used to get protocolcorrespondents from
     * OSP$PROTOCOL_CORRESPONDENTS for a given protocolnumber and seqnumber.
     * <li>To fetch the data, it uses get_proto_correspondents.
     *
     * @param protocolNumber get list of protocolCorrespondentList for this id
     * @param seqNumber input to the procedure
     * @return Vector map of protocolcorrespondent data is set of
     * ProtocolCorrespondentsBean.
     * @exception DBException if the instance of a dbEngine is null.
     */
    public Vector getProtocolCorrespondents(String protocolNumber,int seqNumber)
    throws DBException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap correspProtocolRow = null;
        ProtocolCorrespondentsBean protocolCorrespondentsBean = null;
        param.addElement(new Parameter("PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,protocolNumber));
        param.addElement(new Parameter("SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,new Integer(seqNumber).toString()));
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call get_proto_correspondents ( <<PROTOCOL_NUMBER>> , "
                    +" <<SEQUENCE_NUMBER>> , <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new DBException("DB instance is not available");
        }
        int listSize = result.size();
        Vector correspList = null;
        if(listSize>0){
            correspList = new Vector(3,2);
            for(int rowIndex=0;rowIndex<listSize;rowIndex++){
                protocolCorrespondentsBean = new ProtocolCorrespondentsBean();
                correspProtocolRow = (HashMap)result.elementAt(rowIndex);
                protocolCorrespondentsBean.setProtocolNumber(
                        (String)correspProtocolRow.get("PROTOCOL_NUMBER"));
                protocolCorrespondentsBean.setSequenceNumber(
                        Integer.parseInt(correspProtocolRow.get(
                        "SEQUENCE_NUMBER") == null ? "0" :
                            correspProtocolRow.get(
                        "SEQUENCE_NUMBER").toString()));
                protocolCorrespondentsBean.setCorrespondentTypeCode(
                        Integer.parseInt(correspProtocolRow.get(
                        "CORRESPONDENT_TYPE_CODE") == null ? "0" :
                            correspProtocolRow.get(
                        "CORRESPONDENT_TYPE_CODE").toString()));
                
                protocolCorrespondentsBean.setAwCorrespondentTypeCode(
                        Integer.parseInt(correspProtocolRow.get(
                        "CORRESPONDENT_TYPE_CODE") == null ? "0" :
                            correspProtocolRow.get(
                        "CORRESPONDENT_TYPE_CODE").toString()));
                protocolCorrespondentsBean.setCorrespondentTypeDesc(
                        (String)correspProtocolRow.get("DESCRIPTION"));
                protocolCorrespondentsBean.setPersonId(
                        (String)correspProtocolRow.get("PERSON_ID"));
                // Added to fix the updation issue - Start
                protocolCorrespondentsBean.setAwPersonId(
                        (String)correspProtocolRow.get("PERSON_ID"));
                // Added to fix the updation issue - Start
                protocolCorrespondentsBean.setPersonName(
                        (String)correspProtocolRow.get("PERSON_NAME"));
                protocolCorrespondentsBean.setNonEmployeeFlag(
                        correspProtocolRow.get(
                        "NON_EMPLOYEE_FLAG") == null ? false
                        :(correspProtocolRow.get("NON_EMPLOYEE_FLAG").toString()
                        .equalsIgnoreCase("y") ? true :false));
                protocolCorrespondentsBean.setComments(
                        (String)correspProtocolRow.get("COMMENTS"));
                protocolCorrespondentsBean.setUpdateTimestamp(
                        (Timestamp)correspProtocolRow.get("UPDATE_TIMESTAMP"));
                protocolCorrespondentsBean.setUpdateUser(
                        (String)correspProtocolRow.get("UPDATE_USER"));
                
                correspList.add(protocolCorrespondentsBean);
            }
        }
        return correspList;
    }
    
    /**
     * Method used to get protocolresearchArea from OSP$PROTOCOL_RESEARCH_AREAS
     * for a given protocolnumber and seqnumber.
     * <li>To fetch the data, it uses get_proto_research_areas.
     *
     * @param protocolNumber get list of protocolresearchAreaList for this id
     * @param seqNumber input to the procedure
     * @return Vector map of protocolresearcharea data is set of
     * ProtocolReasearchAreasBean.
     * @exception DBException if the instance of a dbEngine is null.
     */
    public Vector getProtocolResearchArea(String protocolNumber,int seqNumber)
    throws DBException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap researchAreaProtocolRow = null;
        ProtocolReasearchAreasBean protocolReasearchAreasBean = null;
        param.addElement(new Parameter("PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,protocolNumber));
        param.addElement(new Parameter("SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,new Integer(seqNumber).toString()));
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call get_proto_research_areas ( <<PROTOCOL_NUMBER>> , "
                    +" <<SEQUENCE_NUMBER>> , <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new DBException("DB instance is not available");
        }
        int listSize = result.size();
        Vector researchList = null;
        if(listSize>0){
            researchList = new Vector(3,2);
            for(int rowIndex=0;rowIndex<listSize;rowIndex++){
                protocolReasearchAreasBean = new ProtocolReasearchAreasBean();
                researchAreaProtocolRow = (HashMap)result.elementAt(rowIndex);
                protocolReasearchAreasBean.setProtocolNumber(
                        (String)researchAreaProtocolRow.get("PROTOCOL_NUMBER"));
                protocolReasearchAreasBean.setSequenceNumber(
                        Integer.parseInt(researchAreaProtocolRow.get(
                        "SEQUENCE_NUMBER") == null ? "0" :
                            researchAreaProtocolRow.get(
                        "SEQUENCE_NUMBER").toString()));
                protocolReasearchAreasBean.setResearchAreaCode(
                        (String)researchAreaProtocolRow.get("RESEARCH_AREA_CODE"));
                protocolReasearchAreasBean.setResearchAreaDescription(
                        (String)researchAreaProtocolRow.get("DESCRIPTION"));
                protocolReasearchAreasBean.setUpdateTimestamp(
                        (Timestamp)researchAreaProtocolRow.get("UPDATE_TIMESTAMP"));
                protocolReasearchAreasBean.setUpdateUser(
                        (String)researchAreaProtocolRow.get("UPDATE_USER"));
                
                researchList.add(protocolReasearchAreasBean);
            }
        }
        return researchList;
    }
    
    /**
     * Method used to get protocolFundingSourcefrom OSP$PROTOCOL_FUNDING_SOURCES
     * for a given protocolnumber and seqnumber.
     * <li>To fetch the data, it uses get_proto_funding_sources.
     *
     * @param protocolNumber get list of protocolFundingList for this id
     * @param seqNumber input to the procedure
     * @return Vector map of protocolfundsources data is set of
     * ProtocolFundingSourceBean.
     * @exception DBException if the instance of a dbEngine is null.
     */
    public Vector getProtocolFundingSources(String protocolNumber,int seqNumber)
    throws DBException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap fundSourcesProtocolRow = null;
        ProtocolFundingSourceBean protocolFundingSourceBean = null;
        param.addElement(new Parameter("PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,protocolNumber));
        param.addElement(new Parameter("SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,new Integer(seqNumber).toString()));
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call get_proto_funding_sources ( <<PROTOCOL_NUMBER>> , "
                    +" <<SEQUENCE_NUMBER>> , <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new DBException("DB instance is not available");
        }
        int listSize = result.size();
        Vector fundList = new Vector(3,2);
        for(int rowIndex=0;rowIndex<listSize;rowIndex++){
            protocolFundingSourceBean = new ProtocolFundingSourceBean();
            fundSourcesProtocolRow = (HashMap)result.elementAt(rowIndex);
            protocolFundingSourceBean.setProtocolNumber((String)
            fundSourcesProtocolRow.get("PROTOCOL_NUMBER"));
            protocolFundingSourceBean.setSequenceNumber(Integer.parseInt(
                    fundSourcesProtocolRow.get("SEQUENCE_NUMBER") == null ? "0"
                    : fundSourcesProtocolRow.get("SEQUENCE_NUMBER").toString()));
            protocolFundingSourceBean.setFundingSourceTypeCode(
                    Integer.parseInt(fundSourcesProtocolRow.get(
                    "FUNDING_SOURCE_TYPE_CODE") == null ? "0"
                    :fundSourcesProtocolRow.get(
                    "FUNDING_SOURCE_TYPE_CODE").toString()));
            // Implemented by Raghunath P.V. for populating awFundingSourceTypeCode Parameter.
            
            protocolFundingSourceBean.setAwFundingSourceTypeCode(
                    Integer.parseInt(fundSourcesProtocolRow.get(
                    "FUNDING_SOURCE_TYPE_CODE") == null ? "0"
                    :fundSourcesProtocolRow.get(
                    "FUNDING_SOURCE_TYPE_CODE").toString()));
            
            protocolFundingSourceBean.setFundingSourceTypeDesc(
                    (String)fundSourcesProtocolRow.get("DESCRIPTION"));
            protocolFundingSourceBean.setFundingSource((String)
            fundSourcesProtocolRow.get("FUNDING_SOURCE"));
            protocolFundingSourceBean.setUpdateTimestamp(
                    (Timestamp)fundSourcesProtocolRow.get("UPDATE_TIMESTAMP"));
            protocolFundingSourceBean.setUpdateUser((String)
            fundSourcesProtocolRow.get("UPDATE_USER"));
            
            fundList.add(protocolFundingSourceBean);
        }
        return fundList;
    }
    
    /**
     * Method used to get protocol actions from OSP$PROTOCOL_ACTIONS
     * for a given protocol number and seqnumber.
     * <li>To fetch the data, it uses get_proto_actions.
     *
     * @param protocolNumber get list of protocolActionsList for this id
     * @param seqNumber input to the procedure
     * @return Vector map of protocolactions data is set of
     * ProtocolActionsBean.
     * @exception DBException if the instance of a dbEngine is null.
     */
    public Vector getProtocolActions(String protocolNumber,int seqNumber)
    throws DBException,CoeusException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap actionsProtocolRow = null;
        ProtocolActionsBean protocolActionsBean = null;
        //Added for case #1961 start 2
//        ProtoCorrespTypeTxnBean correspTxnBean = new ProtoCorrespTypeTxnBean();
        //        commented by Geo for the fix for wayne
//        Vector correspTypes = correspTxnBean.getProtoCorrespTemplates();
        //Added for case #1961 end 2
        param.addElement(new Parameter("PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,protocolNumber));
        param.addElement(new Parameter("SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,new Integer(seqNumber).toString()));
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call get_proto_actions ( <<PROTOCOL_NUMBER>> , "
                    +" <<SEQUENCE_NUMBER>> , <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new DBException("DB instance is not available");
        }
        int listSize = result.size();
        Vector actionList = null;
        if(listSize>0){
            actionList = new Vector(3,2);
            for(int rowIndex=0;rowIndex<listSize;rowIndex++){
                protocolActionsBean = new ProtocolActionsBean();
                actionsProtocolRow = (HashMap)result.elementAt(rowIndex);
                protocolActionsBean.setProtocolNumber((String)
                actionsProtocolRow.get("PROTOCOL_NUMBER"));
                protocolActionsBean.setSequenceNumber(Integer.parseInt(
                        actionsProtocolRow.get("SEQUENCE_NUMBER") == null ? "0"
                        : actionsProtocolRow.get("SEQUENCE_NUMBER").toString()));
                protocolActionsBean.setActionId(Integer.parseInt(
                        actionsProtocolRow.get("ACTION_ID") == null ? "0"
                        : actionsProtocolRow.get("ACTION_ID").toString()));
                protocolActionsBean.setActionTypeCode(Integer.parseInt(
                        actionsProtocolRow.get("PROTOCOL_ACTION_TYPE_CODE")
                        == null ? "0" : actionsProtocolRow.get(
                        "PROTOCOL_ACTION_TYPE_CODE").toString()));
                protocolActionsBean.setActionTypeDescription(
                        (String)actionsProtocolRow.get("DESCRIPTION"));
                protocolActionsBean.setScheduleId((String)
                actionsProtocolRow.get("SCHEDULE_ID"));
                
                protocolActionsBean.setSubmissionNumber(Integer.parseInt(
                        actionsProtocolRow.get("SUBMISSION_NUMBER") == null ? "0"
                        : actionsProtocolRow.get("SUBMISSION_NUMBER").toString()));
                
                protocolActionsBean.setComments((String)
                actionsProtocolRow.get("COMMENTS"));
                protocolActionsBean.setUpdateTimestamp(
                        (Timestamp)actionsProtocolRow.get("UPDATE_TIMESTAMP"));
                protocolActionsBean.setUpdateUser((String)
                actionsProtocolRow.get("UPDATE_USER"));
                //Commented for COEUSDEV-328 :  Notify IRB FYI submission only permits the Aggregator to upload one document - Start
                //Added for case#3046 - Notify IRB attachments - start
//                String isDocPresent = (String)actionsProtocolRow.get("IS_DOC_PRESENT");
//                if(isDocPresent.equals("Yes")){
//                    //#Case 3855 -- Start Added to set the file name for the protocolTransaction bean to get file name in ProtocolOtherAttachmentsForm
//                    protocolActionsBean.setFileName((String) actionsProtocolRow.get("FILE_NAME"));
//                    protocolActionsBean.setMimeType((String) actionsProtocolRow.get("MIME_TYPE"));//4007
//                    //#Case 3855 -- end
//                    protocolActionsBean.setIsDocumentExists(true);
//                }else{
//                    protocolActionsBean.setIsDocumentExists(false);
//                }          
                //COEUSDEV-328:End
                //Added for case#3046 - Notify IRB attachments - end                
                //Check whether this action has Correspondence - start
                protocolActionsBean.setIsCorrespondenceExists(
                        actionHasCorrespondence(protocolNumber, protocolActionsBean.getSequenceNumber(), protocolActionsBean.getActionId()));
                //Added for case #1961 start 3
                
                //Need to fix properly... by Geo on 01/04/2007
                //To fix wayne state issue, set this flag always true...
//                protocolActionsBean.setIsCorresTemplateExists(hasTemplates(protocolActionsBean,correspTypes));
                protocolActionsBean.setIsCorresTemplateExists(true);
                //End fix
                
                protocolActionsBean.setProtoCorresType(getVecCorresType());
                //Added for case #1961 end 3
                //Added by Jobin for the updation of the Action Date
                protocolActionsBean.setActionDate(
                        actionsProtocolRow.get("ACTION_DATE") == null ? null
                        :new Date( ((Timestamp) actionsProtocolRow.get(
                        "ACTION_DATE")).getTime()) );
                //Added for COEUSDEV-86 : questionnaire for Submission - Start
               /* String isAnsweredQnrPresent = (String)actionsProtocolRow.get("IS_ANSWERED_QNR_PRESENT");
                if(isAnsweredQnrPresent.equals("Yes")){
                    protocolActionsBean.setAnsweredQnrPresent(true);
                }else{
                    protocolActionsBean.setAnsweredQnrPresent(false);
                }*/
                //Added for COEUSDEV-86 : questionnaire for Submission - End
                //Check whether this action has Correspondence - end
                actionList.add(protocolActionsBean);
            }
        }
        return actionList;
    }
    //Added for case #1961 start 4
//    /** This method is used to check template is exists or not for specified protocol number and corres type
//     */
//    private boolean hasTemplates(ProtocolActionsBean protocolActionsBean,Vector correspTypes){
//        int success = 0 ;
//        boolean hasTemplate = false;
//        try {
//            // get the new seq id and do the updation
//            ProtoCorrespTypeTxnBean correspTxnBean = new ProtoCorrespTypeTxnBean();
//            Vector corresTypeCode = correspTxnBean.getValidProtoActionCorrespTypes(protocolActionsBean);
//            setVecCorresType(corresTypeCode);
//            //Vector correspTypes = correspTxnBean.getProtoCorrespTemplates();
//            Vector corresTemp = null;
//            if(corresTypeCode!=null && corresTypeCode.size() > 0){
//                corresTemp = new Vector();
//                for(int index =0 ; index < corresTypeCode.size();index++){
//                    CorrespondenceTypeFormBean correpTypeFormBean =
//                            (CorrespondenceTypeFormBean)corresTypeCode.get(index);
//                    if(correspTypes!=null&&correspTypes.size() >0){
//                        for(int ind =0;ind < correspTypes.size();ind++){
//                            CorrespondenceTypeFormBean correpTypeBean =
//                                    (CorrespondenceTypeFormBean)correspTypes.get(ind);
//                            if(correpTypeFormBean.getProtoCorrespTypeCode() == correpTypeBean.getProtoCorrespTypeCode()){
//                                corresTemp.addElement(correpTypeBean);
//                            }
//                        }
//                    }
//                    
//                }
//            }
//            if(corresTemp!=null&&corresTemp.size() > 0){
//                
//                for(int index = 0; index < corresTemp.size();index++){
//                    CorrespondenceTypeFormBean correpTypeFormBean =
//                            (CorrespondenceTypeFormBean)corresTemp.get(index);
//                    if(correpTypeFormBean.getFileBytes()!= null&&correpTypeFormBean.getFileBytes().length >0 ){
//                        hasTemplate = true;
//                    }
//                }
//            }else{
//                hasTemplate = false;
//            }
//            return hasTemplate;
//        } catch(Exception ex) {
//            ex.printStackTrace() ;
//        }
//        
//        return hasTemplate ;
//    }
    //Added for case #1961 start 4
    /**
     * Method used to get protocol actions from OSP$PROTOCOL_ACTIONS
     * for a given protocol number and seqnumber.
     * <li>To fetch the data, it uses get_proto_actions.
     *
     * @param protocolNumber get list of protocolActionsList for this id
     * @param seqNumber input to the procedure
     * @return Vector map of protocolactions data is set of
     * ProtocolActionsBean.
     * @exception DBException if the instance of a dbEngine is null.
     */
    public Vector getProtocolActionsWithSubmission(String protocolNumber,int seqNumber,int submissionNumber)
    throws DBException,CoeusException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap actionsProtocolRow = null;
        //Added for case #1961 start
//        ProtoCorrespTypeTxnBean correspTxnBean = new ProtoCorrespTypeTxnBean();
//        commented by Geo for the fix for wayne
//        Vector correspTypes = correspTxnBean.getProtoCorrespTemplates();
        //Added for case #1961 end
        ProtocolActionsBean protocolActionsBean = null;
        param.addElement(new Parameter("PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,protocolNumber));
        param.addElement(new Parameter("SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,""+seqNumber));
        param.addElement(new Parameter("SUBMISSION_NUMBER",
                DBEngineConstants.TYPE_INT,new Integer(submissionNumber).toString()));
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call GET_PROTO_ACTIONS_SUBMISSION ( <<PROTOCOL_NUMBER>> , "
                    +" <<SEQUENCE_NUMBER>> ,<<SUBMISSION_NUMBER>>, <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new DBException("DB instance is not available");
        }
        int listSize = result.size();
        Vector actionList = null;
        if(listSize>0){
            actionList = new Vector(3,2);
            for(int rowIndex=0;rowIndex<listSize;rowIndex++){
                protocolActionsBean = new ProtocolActionsBean();
                actionsProtocolRow = (HashMap)result.elementAt(rowIndex);
                protocolActionsBean.setProtocolNumber((String)
                actionsProtocolRow.get("PROTOCOL_NUMBER"));
                protocolActionsBean.setSequenceNumber(Integer.parseInt(
                        actionsProtocolRow.get("SEQUENCE_NUMBER") == null ? "0"
                        : actionsProtocolRow.get("SEQUENCE_NUMBER").toString()));
                protocolActionsBean.setActionId(Integer.parseInt(
                        actionsProtocolRow.get("ACTION_ID") == null ? "0"
                        : actionsProtocolRow.get("ACTION_ID").toString()));
                protocolActionsBean.setActionTypeCode(Integer.parseInt(
                        actionsProtocolRow.get("PROTOCOL_ACTION_TYPE_CODE")
                        == null ? "0" : actionsProtocolRow.get(
                        "PROTOCOL_ACTION_TYPE_CODE").toString()));
                protocolActionsBean.setActionTypeDescription(
                        (String)actionsProtocolRow.get("DESCRIPTION"));
                protocolActionsBean.setScheduleId((String)
                actionsProtocolRow.get("SCHEDULE_ID"));
                
                protocolActionsBean.setSubmissionNumber(Integer.parseInt(
                        actionsProtocolRow.get("SUBMISSION_NUMBER") == null ? "0"
                        : actionsProtocolRow.get("SUBMISSION_NUMBER").toString()));
                
                protocolActionsBean.setComments((String)
                actionsProtocolRow.get("COMMENTS"));
                protocolActionsBean.setUpdateTimestamp(
                        (Timestamp)actionsProtocolRow.get("UPDATE_TIMESTAMP"));
                protocolActionsBean.setUpdateUser((String)
                actionsProtocolRow.get("UPDATE_USER"));
                //Check whether this action has Correspondence - start
                protocolActionsBean.setIsCorrespondenceExists(
                        actionHasCorrespondence(protocolNumber, protocolActionsBean.getSequenceNumber(), protocolActionsBean.getActionId()));
                //Added for case #1961 start 0
                //Commenting by Geo to fix wayne state issue
                //need to fix this correctly
//                protocolActionsBean.setIsCorresTemplateExists(hasTemplates(protocolActionsBean,correspTypes));
                protocolActionsBean.setIsCorresTemplateExists(true);
                //end fix
                protocolActionsBean.setProtoCorresType(getVecCorresType());
                //Added for case #1961 end 0
                //Added by Jobin for the updation of the Action Date
                protocolActionsBean.setActionDate(
                        actionsProtocolRow.get("ACTION_DATE") == null ? null
                        :new Date( ((Timestamp) actionsProtocolRow.get(
                        "ACTION_DATE")).getTime()) );
                
                //Check whether this action has Correspondence - end
                //Added for case#3046 - Notify IRB attachments - start
                String isDocPresent = (String)actionsProtocolRow.get("IS_DOC_PRESENT");
                if(isDocPresent.equals("Yes")){
                    protocolActionsBean.setIsDocumentExists(true);
                }else{
                    protocolActionsBean.setIsDocumentExists(false);
                }                
                //Added for case#3046 - Notify IRB attachments - end                
                
                actionList.add(protocolActionsBean);
            }
        }
        return actionList;
    }
    
    /**
     * Method used to get protocol actions from OSP$PROTOCOL_SPECIAL_REVIEW
     * for a given protocol number and seqnumber.
     * <li>To fetch the data, it uses GET_PROTO_SPECIAL_REVIEW_LIST.
     *
     * @param protocolNumber get list of protocolActionsList for this id
     * @param seqNumber input to the procedure
     * @return Vector map of protocolSpecialReviewFormBean data is set of
     * ProtocolSpecialReviewFormBean.
     * @exception DBException if the instance of a dbEngine is null.
     */
    public Vector getProtocolSpecialReview(String protocolNumber, int sequenceNumber)
    throws DBException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap reviewProtocolRow = null;
        ProtocolSpecialReviewFormBean protocolSpecialReviewFormBean = null;
        param.addElement(new Parameter("PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,protocolNumber));
        
        param.addElement(new Parameter("SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_STRING,new Integer(sequenceNumber).toString()));
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call GET_PROTO_SPECIAL_REVIEW_LIST ( <<PROTOCOL_NUMBER>> , <<SEQUENCE_NUMBER>>, "
                    +" <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new DBException("DB instance is not available");
        }
        int listSize = result.size();
        Vector actionList = null;
        if(listSize>0){
            actionList = new Vector(3,2);
            for(int rowIndex=0;rowIndex<listSize;rowIndex++){
                protocolSpecialReviewFormBean = new ProtocolSpecialReviewFormBean();
                reviewProtocolRow = (HashMap)result.elementAt(rowIndex);
                protocolSpecialReviewFormBean.setProtocolNumber((String)
                reviewProtocolRow.get("PROTOCOL_NUMBER"));
                protocolSpecialReviewFormBean.setSequenceNumber(Integer.parseInt(
                        reviewProtocolRow.get("SEQUENCE_NUMBER") == null ? "0"
                        : reviewProtocolRow.get("SEQUENCE_NUMBER").toString()));
                protocolSpecialReviewFormBean.setSpecialReviewNumber(Integer.parseInt(
                        reviewProtocolRow.get("SPECIAL_REVIEW_NUMBER") == null ? "0"
                        : reviewProtocolRow.get("SPECIAL_REVIEW_NUMBER").toString()));
                protocolSpecialReviewFormBean.setSpecialReviewCode(Integer.parseInt(
                        reviewProtocolRow.get("SPECIAL_REVIEW_CODE") == null ? "0"
                        : reviewProtocolRow.get("SPECIAL_REVIEW_CODE").toString()));
                protocolSpecialReviewFormBean.setSpecialReviewDescription((String)
                reviewProtocolRow.get("REVIEWDESC"));
                protocolSpecialReviewFormBean.setProtocolSPRevNumber((String)
                reviewProtocolRow.get("SP_REV_PROTOCOL_NUMBER"));
                protocolSpecialReviewFormBean.setApprovalCode(Integer.parseInt(
                        reviewProtocolRow.get("APPROVAL_TYPE_CODE") == null ? "0"
                        : reviewProtocolRow.get("APPROVAL_TYPE_CODE").toString()));
                protocolSpecialReviewFormBean.setApprovalDescription((String)
                reviewProtocolRow.get("APPROVALDESC"));
                protocolSpecialReviewFormBean.setApplicationDate(
                        reviewProtocolRow.get("APPLICATION_DATE") == null ? null
                        :new Date( ((Timestamp) reviewProtocolRow.get(
                        "APPLICATION_DATE")).getTime()) );
                protocolSpecialReviewFormBean.setApprovalDate(
                        reviewProtocolRow.get("APPROVAL_DATE") == null ? null
                        :new Date( ((Timestamp) reviewProtocolRow.get(
                        "APPROVAL_DATE")).getTime()) );
                protocolSpecialReviewFormBean.setComments((String)
                reviewProtocolRow.get("COMMENTS"));
                protocolSpecialReviewFormBean.setUpdateTimestamp(
                        (Timestamp)reviewProtocolRow.get("UPDATE_TIMESTAMP"));
                protocolSpecialReviewFormBean.setUpdateUser((String)
                reviewProtocolRow.get("UPDATE_USER"));
                
                actionList.add(protocolSpecialReviewFormBean);
            }
        }
        return actionList;
    }
    
    /**
     * Method used to get protocol actions from OSP$PROTOCOL_USER_ROLES
     * for a given protocol number and seqnumber.
     * <li>To fetch the data, it uses GET_ROLES_FOR_PROTOCOL.
     *
     * @param protocolNumber get list of RoleInfoBean for this id
     * @param seqNumber input to the procedure
     * @return RoleInfoBean
     * @exception DBException if the instance of a dbEngine is null.
     */
    public Vector getProtocolRoles()throws DBException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap roleRow = null;
        RoleInfoBean roleInfoBean = null;
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call GET_PROTOCOL_ROLES ( <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new DBException("DB instance is not available");
        }
        int listSize = result.size();
        Vector actionList = null;
        if(listSize>0){
            actionList = new Vector(3,2);
            for(int rowIndex=0;rowIndex<listSize;rowIndex++){
                roleInfoBean = new RoleInfoBean();
                roleRow = (HashMap)result.elementAt(rowIndex);
                roleInfoBean.setRoleId(Integer.parseInt(
                        roleRow.get("ROLE_ID") == null ? "0"
                        : roleRow.get("ROLE_ID").toString()));
                roleInfoBean.setRoleName((String)
                roleRow.get("ROLE_NAME"));
                roleInfoBean.setRoleDesc((String)
                roleRow.get("DESCRIPTION"));
                roleInfoBean.setRoleType(
                        roleRow.get("ROLE_TYPE").toString().charAt(0));
                roleInfoBean.setUnitNumber((String)
                roleRow.get("OWNED_BY_UNIT"));
                roleInfoBean.setDescend(roleRow.get(
                        "DESCEND_FLAG") == null ? false :
                            (roleRow.get("DESCEND_FLAG").toString()
                            .equalsIgnoreCase("y") ? true :false));
                roleInfoBean.setStatus(
                        roleRow.get("STATUS_FLAG").toString().charAt(0));
                
                actionList.add(roleInfoBean);
            }
        }
        return actionList;
    }
    
    /**
     * Method used to get user id for combination of protocol number,seqnumber and roleid
     * from protocol actions from OSP$PROTOCOL_USER_ROLES and OSP$USER
     * <li>To fetch the data, it uses get_users_for_protocol_role.
     *
     * @param protocolNumber ,seqnumber and roleid
     * @return Vector collections of user id
     * @exception DBException if the instance of a dbEngine is null.
     */
    public Vector getProtocolUser(String protocolNumber,int seqNumber,int roleId)
    throws DBException{
        Vector result = new Vector(3,2);
        Vector vecProtocolUser = new Vector();
        HashMap hasProtcolUser=null;
        Vector param= new Vector();
//        String userId ="";
        param.addElement(new Parameter("PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,protocolNumber));
        param.addElement(new Parameter("SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,new Integer(seqNumber).toString()));
        param.addElement(new Parameter("ROLE_ID",
                DBEngineConstants.TYPE_INT,new Integer(roleId).toString()));
        
        ProtocolRoleInfoBean roleInfoBean = null;
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call get_users_for_protocol_role ( <<PROTOCOL_NUMBER>> , "
                    +" <<SEQUENCE_NUMBER>> ,<<ROLE_ID>>, <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new DBException("DB instance is not available");
        }
        int typesCount = result.size();
        if (typesCount >0){
            //vecProtocolUser = new Vector();
            for(int types=0;types<typesCount;types++){
                hasProtcolUser = (HashMap)result.elementAt(types);
                //userId = hasProtcolUser.get("USER_ID").toString();
                //vecProtocolUser.add(userId);
                
                // COEUSDEV-273: Protocol roles update error - new se & save  
//                roleInfoBean = new RoleInfoBean();
                roleInfoBean = new ProtocolRoleInfoBean();
                roleInfoBean.setProtocolNumber((String)hasProtcolUser.get("PROTOCOL_NUMBER"));
                roleInfoBean.setSequenceNumber(Integer.parseInt(hasProtcolUser.get("SEQUENCE_NUMBER").toString()));
                roleInfoBean.setUserId((String)hasProtcolUser.get("USER_ID"));
                roleInfoBean.setUpdateUser((String)hasProtcolUser.get("UPDATE_USER"));
                roleInfoBean.setUpdateTimestamp((Timestamp)hasProtcolUser.get("UPDATE_TIMESTAMP"));
                vecProtocolUser.add(roleInfoBean);
            }
        }
        return vecProtocolUser;
    }
    
    /**
     * Method used to get user id for combination of unit number and roleid
     * from protocol actions from OSP$USER and OSP$USER_ROLES
     * <li>To fetch the data, it uses get_user_for_roles_unit.
     *
     * @param protocolNumber and roleid
     * @return Vector collections of user id
     * @exception DBException if the instance of a dbEngine is null.
     */
    public Vector getUserForRoleUnit(String unitNumber,int roleId)
    throws DBException{
        Vector result = new Vector(3,2);
        Vector vecProtocolUser = null;
        HashMap hasProtcolUser=null;
        Vector param= new Vector();
        String userId ="";
        param.addElement(new Parameter("UNIT_NUMBER",
                DBEngineConstants.TYPE_STRING,unitNumber));
        param.addElement(new Parameter("ROLE_ID",
                DBEngineConstants.TYPE_INT,new Integer(roleId).toString()));
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call get_user_for_roles_unit ( <<UNIT_NUMBER>> , "
                    +" <<ROLE_ID>>, <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new DBException("DB instance is not available");
        }
        int typesCount = result.size();
        if (typesCount >0){
            vecProtocolUser = new Vector();
            for(int types=0;types<typesCount;types++){
                hasProtcolUser = (HashMap)result.elementAt(types);
                userId = hasProtcolUser.get("USER_ID").toString();
                vecProtocolUser.add(userId);
            }
        }
        return vecProtocolUser;
    }
    
    /**
     * Method used to get tree structure for the user for roles combination
     *
     * @param protocolNumber and seqnumber
     * @return Vector collections of UserRolesInfoBean in turn it is collection
     * of roleinfobean and userRolesInfoBean
     * @exception DBException if the instance of a dbEngine is null.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getProtocolUserRoles(String protocolNumber,int seqNumber)
    throws CoeusException,DBException{
        UserDetailsBean userDetailsBean = new UserDetailsBean();
        Vector vecRolesBean = new Vector();
        vecRolesBean = getProtocolRoles();
        // COEUSDEV-273: Protocol roles update error - new sequence�- Start
        Vector vecAllProtocolUsers =  getUsersForAllProtocolRoles(protocolNumber, seqNumber);
        Vector roles = new Vector();
        RoleInfoBean userRoleBean = null;
//        if ((vecRolesBean != null) && (vecRolesBean.size() >0)){
//            int totalRoles = vecRolesBean.size();
//            for(int index1=0;index1<totalRoles;index1++){
//                RoleInfoBean roleInfoBean =
//                        (RoleInfoBean)vecRolesBean.elementAt(index1);
//                int roleId = roleInfoBean.getRoleId();
//                UserRolesInfoBean userRolesInfoBean = new UserRolesInfoBean();
//                userRolesInfoBean.setIsRole(true);
//                userRolesInfoBean.setRoleBean(roleInfoBean);
//                Vector vecUserId = getProtocolUser(protocolNumber,seqNumber,roleId);
//                if ((vecUserId != null) && (vecUserId.size() >0)){
//                    userRolesInfoBean.setHasChildren(true);
//                    int innerlength = vecUserId.size();
//                    Vector users = new Vector();
//                    for(int index2=0;index2<innerlength;index2++){
//                        //String userId =
//                        //(String)vecUserId.elementAt(index2);
//                        userRoleBean = (RoleInfoBean)vecUserId.elementAt(index2);
//                        String userId = userRoleBean.getUserId();
//                        UserInfoBean userInfoBean =
//                                userDetailsBean.getUserInfo(userId);
//                        UserRolesInfoBean userRolesInfoBean2 = new UserRolesInfoBean();
//                        userRolesInfoBean2.setUserBean(userInfoBean);
//                        userRolesInfoBean2.setIsRole(false);
//                        userRolesInfoBean2.setHasChildren(false);
//                        userRolesInfoBean2.setUpdateUser(userRoleBean.getUpdateUser());
//                        userRolesInfoBean2.setUpdateTimestamp(userRoleBean.getUpdateTimestamp());
//                        users.addElement(userRolesInfoBean2);
//                        
//                    }//end inner for loop
//                    userRolesInfoBean.setUsers(users);
//                }//end of inner  if loop
//                roles.addElement(userRolesInfoBean);
//            }//end of outer for loop
//            
//        }//end of outer if loop
        
        
        if ((vecRolesBean != null) && (vecRolesBean.size() >0)){
            int totalRoles = vecRolesBean.size();
            int totalProtocolUsers = 0;
            if(vecAllProtocolUsers != null && !vecAllProtocolUsers.isEmpty()){
                totalProtocolUsers = vecAllProtocolUsers.size();
            }
            UserRolesInfoBean userRolesInfoBean = null;
            for(int index1=0;index1<totalRoles;index1++){
                RoleInfoBean roleInfoBean = (RoleInfoBean)vecRolesBean.elementAt(index1);
                int roleId = roleInfoBean.getRoleId();
                Vector users = new Vector();
                userRolesInfoBean = new UserRolesInfoBean();
                userRolesInfoBean.setRoleBean(roleInfoBean); 
                userRolesInfoBean.setIsRole(true);
                for(int index2 =0; index2 < totalProtocolUsers; index2++){
                    ProtocolRoleInfoBean protocolRoleInfoBean = (ProtocolRoleInfoBean) vecAllProtocolUsers.get(index2);

                    if(protocolRoleInfoBean.getRoleId() == roleId){
                        
                        userRolesInfoBean.setHasChildren(true);
                        String userId = protocolRoleInfoBean.getUserId();
                        UserInfoBean userInfoBean =
                                userDetailsBean.getUserInfo(userId);
                        ProtocolUserRoleInfoBean protocolUserRoleInfoBean = new ProtocolUserRoleInfoBean();
                        protocolUserRoleInfoBean.setUserBean(userInfoBean);
                        protocolUserRoleInfoBean.setIsRole(false);
                        protocolUserRoleInfoBean.setHasChildren(false);
                        protocolUserRoleInfoBean.setUpdateUser(protocolRoleInfoBean.getUpdateUser());
                        protocolUserRoleInfoBean.setUpdateTimestamp(protocolRoleInfoBean.getUpdateTimestamp());
                        protocolUserRoleInfoBean.setProtocolNumber(protocolRoleInfoBean.getProtocolNumber());
                        protocolUserRoleInfoBean.setSequenceNumber(protocolRoleInfoBean.getSequenceNumber());
                        
                        users.addElement(protocolUserRoleInfoBean);   
                    }
                }
                userRolesInfoBean.setUsers(users);
                roles.addElement(userRolesInfoBean);
            }
        }
        // COEUSDEV-273: Protocol roles update error - new sequence�- End
        return roles;
    }
    
    /**
     * Method used to get tree structure for the user for roles combination
     *
     * @param protocolNumber and seqnumber
     * @return Vector collections of UserRolesInfoBean in turn it is collection
     * of roleinfobean and userRolesInfoBean
     * @exception DBException if the instance of a dbEngine is null.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getDefaultUserRolesForUnit(String unitNumber,String userId)
    throws CoeusException,DBException{
        UserDetailsBean userDetailsBean = new UserDetailsBean();
        Vector vecRolesBean = new Vector();
        vecRolesBean = getProtocolRoles();
        Vector roles = new Vector();
        if ((vecRolesBean != null) && (vecRolesBean.size() >0)){
            int outerlength = vecRolesBean.size();
            for(int index1=0;index1<outerlength;index1++){
                RoleInfoBean roleInfoBean =
                        (RoleInfoBean)vecRolesBean.elementAt(index1);
                int roleId = roleInfoBean.getRoleId();
                UserRolesInfoBean userRolesInfoBean = new UserRolesInfoBean();
                userRolesInfoBean.setIsRole(true);
                userRolesInfoBean.setRoleBean(roleInfoBean);
                Vector users = new Vector();
                if (roleId == CoeusConstants.PROTOCOL_COORDINATOR_ID){
                    userRolesInfoBean.setHasChildren(true);
                    UserInfoBean userInfoBean =userDetailsBean.getUserInfo(userId);
                    UserRolesInfoBean userRolesInfoBean2 = new UserRolesInfoBean();
                    userRolesInfoBean2.setUserBean(userInfoBean);
                    userRolesInfoBean2.setIsRole(false);
                    userRolesInfoBean2.setHasChildren(false);
                    users.addElement(userRolesInfoBean2);
                    //case 664 - prps start Apr 13 2004
                    // After adding the current user as aggregator
                    // Add other aggregators (if any) from the unit
                    
                    Vector vecUserId = getUserForRoleUnit(unitNumber,roleId);
                    if ((vecUserId != null) && (vecUserId.size() >0)){
                        int innerlength = vecUserId.size();
                        for(int index2=0;index2<innerlength;index2++) {
                            String userID = (String)vecUserId.elementAt(index2);
                            // since the current user is already added as aggregator,
                            // if he is part of the unit as aggregator then do not
                            // add him again.
                            if (!userID.equals(userId)) {
                                userInfoBean = userDetailsBean.getUserInfo(userID);
                                userRolesInfoBean2 = new UserRolesInfoBean();
                                userRolesInfoBean2.setUserBean(userInfoBean);
                                userRolesInfoBean2.setIsRole(false);
                                userRolesInfoBean2.setHasChildren(false);
                                users.addElement(userRolesInfoBean2);
                            }
                        }//end inner for loop
                        //userRolesInfoBean.setUsers(users);
                    }//end of inner  if loop
                    
                    
                    //case 664 - prps end Apr 13 2004
                    
                    
                }else{
                    Vector vecUserId = getUserForRoleUnit(unitNumber,roleId);
                    if ((vecUserId != null) && (vecUserId.size() >0)){
                        userRolesInfoBean.setHasChildren(true);
                        int innerlength = vecUserId.size();
                        //Vector users = new Vector();
                        for(int index2=0;index2<innerlength;index2++){
                            String userID =
                                    (String)vecUserId.elementAt(index2);
                            UserInfoBean userInfoBean = userDetailsBean.getUserInfo(userID);
                            UserRolesInfoBean userRolesInfoBean2 = new UserRolesInfoBean();
                            userRolesInfoBean2.setUserBean(userInfoBean);
                            userRolesInfoBean2.setIsRole(false);
                            userRolesInfoBean2.setHasChildren(false);
                            users.addElement(userRolesInfoBean2);
                            
                        }//end inner for loop
                        //userRolesInfoBean.setUsers(users);
                    }//end of inner  if loop
                }
                userRolesInfoBean.setUsers(users);
                roles.addElement(userRolesInfoBean);
            }//end of outer for loop
            
        }//end of outer if loop
        return roles;
    }
    
    /**
     * Method used to get tree structure for the user for roles combination it is
     * overridden method
     *
     * @param userId
     * @return Vector collections of UserRolesInfoBean in turn it is collection
     * of roleinfobean and userRolesInfoBean
     * @exception DBException if the instance of a dbEngine is null.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getProtocolUserRoles(String userId)
    throws CoeusException,DBException{
        UserDetailsBean userDetailsBean = new UserDetailsBean();
        Vector vecRolesBean = new Vector();
        vecRolesBean = getProtocolRoles();
        Vector roles = new Vector();
        if ((vecRolesBean != null) && (vecRolesBean.size() >0)){
            int outerlength = vecRolesBean.size();
            for(int index1=0;index1<outerlength;index1++){
                RoleInfoBean roleInfoBean =
                        (RoleInfoBean)vecRolesBean.elementAt(index1);
                int roleId = roleInfoBean.getRoleId();
                UserRolesInfoBean userRolesInfoBean = new UserRolesInfoBean();
                userRolesInfoBean.setIsRole(true);
                userRolesInfoBean.setRoleBean(roleInfoBean);
                if (roleId == CoeusConstants.PROTOCOL_COORDINATOR_ID){
                    userRolesInfoBean.setHasChildren(true);
                    Vector users = new Vector();
                    UserInfoBean userInfoBean =userDetailsBean.getUserInfo(userId);
                    UserRolesInfoBean userRolesInfoBean2 = new UserRolesInfoBean();
                    userRolesInfoBean2.setUserBean(userInfoBean);
                    userRolesInfoBean2.setIsRole(false);
                    userRolesInfoBean2.setHasChildren(false);
                    users.addElement(userRolesInfoBean2);
                    userRolesInfoBean.setUsers(users);
                }//end of if loop
                roles.addElement(userRolesInfoBean);
            }//end of outer for loop
            
        }//end of outer if loop
        return roles;
    }
    
    
    // This method will check if the protocol cab be opened for modification
    public int performEditValidation(String protocolId) {
        int success = 0 ;
        try {
            // get the new seq id and do the updation
            Vector param= new Vector();
            
            HashMap nextNumRow = null;
            Vector result = new Vector();
            
//            System.out.println("** Start Server side validation for edit ") ;
            param= new Vector();
            param.add(new Parameter("AS_PROTOCOL_NUMBER",
                    DBEngineConstants.TYPE_STRING, protocolId )) ;
            
            if(dbEngine!=null){
                result = dbEngine.executeFunctions("Coeus",
                        "{<<OUT INTEGER SUCCESS>> = call FN_CAN_MODIFY_PROTOCOL( "
                        + " << AS_PROTOCOL_NUMBER >> )}", param) ;
            }else{
                throw new CoeusException("db_exceptionCode.1000") ;
            }
            if(!result.isEmpty()){
                nextNumRow = (HashMap)result.elementAt(0);
                success = Integer.parseInt(nextNumRow.get("SUCCESS").toString());
//                System.out.println("** Server side validation for edit returned " + success + "**") ;
                
            }
            
        } catch(Exception ex) {
//            ex.printStackTrace() ;
            UtilFactory.log(ex.getMessage(),ex,"ProtocolDataTxnBean", "performEditValidation");
        }
        
        return success ;
    }
    
    
    /**
     * Method used to get user id for combination of unit number and roleid
     * from protocol actions from OSP$USER and OSP$USER_ROLES
     * <li>To fetch the data, it uses get_user_for_roles_unit.
     *
     * @param protocolNumber and roleid
     * @return Vector collections of user id
     * @exception DBException if the instance of a dbEngine is null.
     */
    public Vector getAffiliationTypes()
    throws DBException{
        Vector result = new Vector(3,2);
        Vector vecAffiliationTypes = new Vector();
        HashMap hasAffiliationType=null;
        Vector param= new Vector();
//        String userId ="";
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call GET_AFFILIATION_TYPES ( <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new DBException("DB instance is not available");
        }
        int typesCount = result.size();
        if (typesCount >0){
            for(int types=0;types<typesCount;types++){
                hasAffiliationType = (HashMap)result.elementAt(types);
                vecAffiliationTypes.addElement(new ComboBoxBean(
                        hasAffiliationType.get("AFFILIATION_TYPE_CODE").toString(),
                        hasAffiliationType.get("DESCRIPTION").toString()));
            }
        }
        return vecAffiliationTypes;
    }
    
    /**
     * Method used to get Protocol Organionzation Type Codes and Description.
     *
     * <li>To fetch the data, it uses GET_PROTOCOL_ORG_TYPES.
     *
     * @return Vector collections of ComboBoxBean
     * @exception DBException if the instance of a dbEngine is null.
     */
    public Vector getProtocolOrganizationTypes()
    throws DBException{
        Vector result = new Vector(3,2);
        Vector vecProtOrgTypes = new Vector();
        HashMap hasProtOrgTypes=null;
        Vector param= new Vector();
//        String userId ="";
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call GET_PROTOCOL_ORG_TYPES ( <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new DBException("DB instance is not available");
        }
        int typesCount = result.size();
        if (typesCount >0){
            for(int types=0;types<typesCount;types++){
                hasProtOrgTypes = (HashMap)result.elementAt(types);
                vecProtOrgTypes.addElement(new ComboBoxBean(
                        hasProtOrgTypes.get("PROTOCOL_ORG_TYPE_CODE").toString(),
                        hasProtOrgTypes.get("DESCRIPTION").toString()));
            }
        }
        return vecProtOrgTypes;
    }
    
    /**
     * Method used to get Protocol Reference Type Codes and Description.
     *
     * <li>To fetch the data, it uses GET_PROTOCOL_ORG_TYPES.
     *
     * @return Vector collections of ComboBoxBean
     * @exception DBException if the instance of a dbEngine is null.
     */
    public Vector getProtocolReferenceTypes()
    throws DBException{
        
        Vector result = new Vector(3,2);
        Vector vecProtRefTypes = new Vector();
        try{
            HashMap hasProtRefTypes=null;
            Vector param= new Vector();
//            String userId ="";
            
            if(dbEngine!=null){
                result = dbEngine.executeRequest("Coeus",
                        "call GET_PROTO_REFERENCE_TYPES ( <<OUT RESULTSET rset>> )",
                        "Coeus", param);
            }else{
                throw new DBException("DB instance is not available");
            }
            int typesCount = result.size();
            if (typesCount >0){
                for(int types=0;types<typesCount;types++){
                    hasProtRefTypes = (HashMap)result.elementAt(types);
                    vecProtRefTypes.addElement(new ComboBoxBean(
                            hasProtRefTypes.get("PROTOCOL_REFERENCE_TYPE_CODE").toString(),
                            hasProtRefTypes.get("DESCRIPTION").toString()));
                }
            }
        }catch(Exception e){
//            e.printStackTrace();
            UtilFactory.log(e.getMessage(),e,"ProtocolDataTxnBean", "getProtocolReferenceTypes");
        }
        return vecProtRefTypes;
    }
    
    /**
     *  This method used get max Protocol Reference Number for the given protocol and sequence number
     *  <li>To fetch the data, it uses the function FN_GET_PROTO_REFERENCE_NUMBER.
     *
     *  @return int review number for the protocol number and sequence number
     *  @param String protocol number  and sequence number
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public int getNextProtocolReferenceNumber(String protocolNumber,int seqNumber)
    throws CoeusException, DBException {
        int referenceNumber = 0;
        Vector param= new Vector();
        Vector result = new Vector();
        param.add(new Parameter("PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,protocolNumber));
        param.addElement(new Parameter("SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,new Integer(seqNumber).toString()));
        /* calling stored function */
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{ <<OUT INTEGER REFERENCE_NUMBER>> = "
                    +" call FN_GET_PROTO_REFERENCE_NUMBER(<< PROTOCOL_NUMBER >> ,<< SEQUENCE_NUMBER >> ) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            referenceNumber = Integer.parseInt(rowParameter.get("REFERENCE_NUMBER").toString());
        }
        return referenceNumber;
    }
    
    /**
     * Method used to get protocol actions from OSP$PROTOCOL_REFERENCES
     * for a given protocol number and seqnumber.
     * <li>To fetch the data, it uses GET_PROTO_REFERENCE_LIST.
     *
     * @param protocolNumber get list of protocolActionsList for this id
     * @param seqNumber input to the procedure
     * @return Vector map of protocolSpecialReviewFormBean data is set of
     * ProtocolSpecialReviewFormBean.
     * @exception DBException if the instance of a dbEngine is null.
     */
    public Vector getProtocolReferences(String protocolNumber, int sequenceNumber)
    throws DBException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap referenceRow = null;
        ProtocolReferencesBean protocolReferencesBean = null;
        param.addElement(new Parameter("PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,protocolNumber));
        
        param.addElement(new Parameter("SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_STRING,new Integer(sequenceNumber).toString()));
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call GET_PROTO_REFERENCE_LIST ( <<PROTOCOL_NUMBER>> , <<SEQUENCE_NUMBER>>, "
                    +" <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new DBException("DB instance is not available");
        }
        int listSize = result.size();
        Vector actionList = null;
        if(listSize>0){
            actionList = new Vector(3,2);
            for(int rowIndex=0;rowIndex<listSize;rowIndex++){
                protocolReferencesBean = new ProtocolReferencesBean();
                referenceRow = (HashMap)result.elementAt(rowIndex);
                protocolReferencesBean.setProtocolNumber((String)
                referenceRow.get("PROTOCOL_NUMBER"));
                protocolReferencesBean.setSequenceNumber(Integer.parseInt(
                        referenceRow.get("SEQUENCE_NUMBER") == null ? "0"
                        : referenceRow.get("SEQUENCE_NUMBER").toString()));
                protocolReferencesBean.setReferenceNumber(Integer.parseInt(
                        referenceRow.get("PROTOCOL_REFERENCE_NUMBER") == null ? "0"
                        : referenceRow.get("PROTOCOL_REFERENCE_NUMBER").toString()));
                protocolReferencesBean.setReferenceTypeCode(Integer.parseInt(
                        referenceRow.get("PROTOCOL_REFERENCE_TYPE_CODE") == null ? "0"
                        : referenceRow.get("PROTOCOL_REFERENCE_TYPE_CODE").toString()));
                protocolReferencesBean.setReferenceTypeDescription((String)
                referenceRow.get("DESCRIPTION"));
                protocolReferencesBean.setReferenceKey((String)
                referenceRow.get("REFERENCE_KEY"));
                protocolReferencesBean.setApplicationDate(
                        referenceRow.get("APPLICATION_DATE") == null ? null
                        :new Date( ((Timestamp) referenceRow.get(
                        "APPLICATION_DATE")).getTime()) );
                protocolReferencesBean.setApprovalDate(
                        referenceRow.get("APPROVAL_DATE") == null ? null
                        :new Date( ((Timestamp) referenceRow.get(
                        "APPROVAL_DATE")).getTime()) );
                protocolReferencesBean.setComments((String)
                referenceRow.get("COMMENTS"));
                protocolReferencesBean.setUpdateTimestamp(
                        (Timestamp)referenceRow.get("UPDATE_TIMESTAMP"));
                protocolReferencesBean.setUpdateUser((String)
                referenceRow.get("UPDATE_USER"));
                
                actionList.add(protocolReferencesBean);
            }
        }
        return actionList;
    }
    
    /**
     * Method used to get protocol others details from OSP$PROTOCOL_CUSTOM_DATA
     * for a given protocol number
     * <li>To fetch the data, it uses GET_PROTOCOL_CUSTOM_DATA procedure.
     *
     * @param protocolNumber this is given as input parameter for the
     * procedure to execute.
     * @return Vector collections of ProtocolCustomElementsInfoBean for others tab
     * @exception DBException if the instance of a dbEngine is null.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getProtocolOthersDetails(String protocolNumber, int sequenceNumber)
    throws CoeusException, DBException{
        Vector result = null;
        Vector param= new Vector();
        ProtocolCustomElementsInfoBean protocolCustomElementsInfoBean = null;
        HashMap othersRow = null;
        param.addElement(new Parameter("PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,protocolNumber));
        param.addElement(new Parameter("SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,new Integer(sequenceNumber).toString()));
        param.addElement(new Parameter("MODULE_CODE",
                DBEngineConstants.TYPE_INT,new Integer(ModuleConstants.PROTOCOL_MODULE_CODE).toString()));
        if(dbEngine !=null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
                    "call GET_PROTOCOL_CUSTOM_DATA( <<PROTOCOL_NUMBER>>, <<SEQUENCE_NUMBER>>, <<MODULE_CODE>>, "+
                    "<<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        Vector othersList = null;
        if (listSize > 0){
            othersList = new Vector(3,2);
            for(int rowIndex=0; rowIndex<listSize; rowIndex++){
                protocolCustomElementsInfoBean = new ProtocolCustomElementsInfoBean();
                othersRow = (HashMap)result.elementAt(rowIndex);
                protocolCustomElementsInfoBean.setProtocolNumber( (String)
                othersRow.get("PROTOCOL_NUMBER"));
                protocolCustomElementsInfoBean.setSequenceNumber(
                        Integer.parseInt(othersRow.get(
                        "SEQUENCE_NUMBER") == null ? "0" : othersRow.get(
                        "SEQUENCE_NUMBER").toString()));
                protocolCustomElementsInfoBean.setColumnName((String)
                othersRow.get("COLUMN_NAME"));
                protocolCustomElementsInfoBean.setColumnValue( (String)
                othersRow.get("COLUMN_VALUE"));
                protocolCustomElementsInfoBean.setColumnLabel( (String)
                othersRow.get("COLUMN_LABEL"));
                protocolCustomElementsInfoBean.setDataType( (String)
                othersRow.get("DATA_TYPE"));
                protocolCustomElementsInfoBean.setDataLength(
                        Integer.parseInt(othersRow.get("DATA_LENGTH") == null ? "0" :
                            othersRow.get("DATA_LENGTH").toString()));
                protocolCustomElementsInfoBean.setDefaultValue( (String)
                othersRow.get("DEFAULT_VALUE"));
                protocolCustomElementsInfoBean.setHasLookUp(
                        othersRow.get("HAS_LOOKUP") == null ? false :
                            (othersRow.get("HAS_LOOKUP").toString()
                            .equalsIgnoreCase("y") ? true :false));
                protocolCustomElementsInfoBean.setLookUpWindow( (String)
                othersRow.get("LOOKUP_WINDOW"));
                protocolCustomElementsInfoBean.setLookUpArgument( (String)
                othersRow.get("LOOKUP_ARGUMENT"));
                protocolCustomElementsInfoBean.setRequired(
                        othersRow.get("IS_REQUIRED") == null ? false :
                            (othersRow.get("IS_REQUIRED").toString()
                            .equalsIgnoreCase("y") ? true :false));
                protocolCustomElementsInfoBean.setDescription( (String)
                othersRow.get("DESCRIPTION"));
                protocolCustomElementsInfoBean.setUpdateTimestamp(
                        (Timestamp)othersRow.get("UPDATE_TIMESTAMP"));
                protocolCustomElementsInfoBean.setUpdateUser( (String)
                othersRow.get("UPDATE_USER"));
                othersList.add(protocolCustomElementsInfoBean);
            }
        }
        return othersList;
    }
    
    /** Method used to get protocol others details from OSP$PROTOCOL_CUSTOM_DATA
     * for a given protocol number and others details for the loggedin user module.
     * <li>It uses GET_PROTOCOL_CUSTOM_DATA to fetch others data for given protocol
     * number and DW_GET_CUST_COLUMNS_FOR_MODULE to get the custom data for the given module.
     * <li>FN_PROTOCOL_HAS_CUSTOM_DATA is used to check whether custom data is
     * available for the given protocol number
     * <li> fn_module_has_custom_data is used to check whether custom data is
     * available for the given module number.
     *
     * <li> get_parameter_value  is used to get the module number for the protocol
     * module by passing the code COEUS_MODULE_IRB
     *
     * @return Vector collections of ProtocolCustomElementsInfoBean for others tab
     * @param protocolNumber Protocol Number
     * @param sequenceNumber Sequence Number
     * @exception DBException if the instance of a dbEngine is null.
     * @exception CoeusException if the instance of dbEngine is not available. */
    public Vector getOthersDetails(String protocolNumber, int sequenceNumber)
    throws CoeusException, DBException{
        DepartmentPersonTxnBean departmentTxnBean = new DepartmentPersonTxnBean();
        Vector param= new Vector();
        //ProtocolCustomElementsInfoBean ProtocolCustomElementsInfoBean = null;
        //HashMap othersRow = null;
        Vector protocolOthers = null,moduleOthers = null;
        HashMap modId = null;
        Vector result = new Vector();
        TreeSet othersData = new TreeSet(new BeanComparator());
        int customPropCount = 0, customModCount = 0;
        String moduleId = "";
        param.add(new Parameter("PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,protocolNumber));
        param.add(new Parameter("SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,new Integer(sequenceNumber).toString()));
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{<<OUT INTEGER COUNT>>=call FN_PROTOCOL_HAS_CUSTOM_DATA ( "
                    + " << PROTOCOL_NUMBER >> , << SEQUENCE_NUMBER >>)}", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap propCount = (HashMap)result.elementAt(0);
            customPropCount = Integer.parseInt(propCount.get("COUNT").toString());
        }
        // get the module number for the protocol
        param.removeAllElements();
        param.add(new Parameter("PARAM_NAME",
                DBEngineConstants.TYPE_STRING,"COEUS_MODULE_IRB"));
        result = dbEngine.executeFunctions("Coeus",
                "{<<OUT STRING MOD_NAME>>=call get_parameter_value ( "
                + " << PARAM_NAME >>)}", param);
        if(!result.isEmpty()){
            modId = (HashMap)result.elementAt(0);
            moduleId = modId.get("MOD_NAME").toString();
        }
        // check whether any custom data is present for protocol module
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
        if ( ( customPropCount > 0 ) || ( customModCount > 0 )){
            // custom data is present for the protocol module. so get the
            // protocol custom data  and module custom data and send unique
            // set of custom data from both.
            if ( customPropCount > 0 ) {
                //get protocol custom data
                protocolOthers = getProtocolOthersDetails(protocolNumber,sequenceNumber);
                othersData.addAll(protocolOthers);
            }
            if( customModCount > 0 ) {
                // get module custom data
                
                moduleOthers = departmentTxnBean.getPersonColumnModule(moduleId);
                moduleOthers = setAcTypeAsNew(moduleOthers);
                othersData.addAll(moduleOthers);
            }/*else{
                // there is no custom data available for the protocol module.
                return null;
            }*/
            //Added for protocol custom data validations - start - 1
            //Set Required property for existing Protocol Custom data
            if(protocolOthers != null){
                CoeusVector cvModuleData = null;
                if(moduleOthers != null){
                    cvModuleData = new CoeusVector();
                    cvModuleData.addAll(moduleOthers);
                }
                CustomElementsInfoBean customElementsInfoBean = null;
                CoeusVector cvFilteredData = null;
                for(int row = 0; row < protocolOthers.size(); row++){
                    customElementsInfoBean = (CustomElementsInfoBean)protocolOthers.elementAt(row);
                    if(cvModuleData == null){
                        customElementsInfoBean.setRequired(false);
                    }else{
                        cvFilteredData = cvModuleData.filter(
                                new Equals("columnName", customElementsInfoBean.getColumnName()));
                        if(cvFilteredData==null || cvFilteredData.size()==0){
                            customElementsInfoBean.setRequired(false);
                        }else{
                            customElementsInfoBean.setRequired(
                                    ((CustomElementsInfoBean)cvFilteredData.elementAt(0)).isRequired());
                        }
                    }
                }
            }            
            //Added for protocol custom data validations - end - 1
            return new Vector(othersData);
        }else{
            // there is no custom data available for the protocol module.
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
     * to the protocol cost elements.
     * @param modOthers vector contain CustomElementsInfoBean
     */
    private Vector setAcTypeAsNew(Vector modOthers){
        if(modOthers != null && modOthers.size() > 0 ){
            int modCount = modOthers.size();
            CustomElementsInfoBean customBean;
            ProtocolCustomElementsInfoBean protocolCustomElementsInfoBean;
            for ( int modIndex = 0; modIndex < modCount; modIndex++ ) {
                customBean = (CustomElementsInfoBean)modOthers.elementAt(modIndex);
                customBean.setAcType(INSERT_RECORD);
                protocolCustomElementsInfoBean = new ProtocolCustomElementsInfoBean(customBean);
                modOthers.set(modIndex,protocolCustomElementsInfoBean);
            }
        }
        return modOthers;
    }
    
    /**  This method used get max Ammendment Version Number for the given protocol.
     *  <li>To fetch the data, it uses the function FN_GET_NEXT_AMENDMENT_VERSION.
     *
     * @return int version number for the protocol number
     * @param protocolNumber Protocol Number
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available. */
    public int getMaxAmendmentVersionNumber(String protocolNumber)
    throws CoeusException, DBException {
        int versionNumber = 0;
        Vector param= new Vector();
        Vector result = new Vector();
        param.add(new Parameter("PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,protocolNumber));
        /* calling stored function */
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{ <<OUT INTEGER VERSION_NUMBER>> = "
                    +" call FN_GET_NEXT_AMENDMENT_VERSION(<< PROTOCOL_NUMBER >> ) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            versionNumber = Integer.parseInt(rowParameter.get("VERSION_NUMBER").toString());
        }
        return versionNumber;
    }
    
    /**  This method used get max Revision Number for the given protocol
     *  <li>To fetch the data, it uses the function FN_GET_NEXT_AMENDMENT_VERSION.
     *
     * @return int revision number for the protocol number
     * @param protocolNumber Protocol Number
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available. */
    public int getMaxRevisionVersionNumber(String protocolNumber)
    throws CoeusException, DBException {
        int versionNumber = 0;
        Vector param= new Vector();
        Vector result = new Vector();
        param.add(new Parameter("PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,protocolNumber));
        /* calling stored function */
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{ <<OUT INTEGER VERSION_NUMBER>> = "
                    +" call FN_GET_NEXT_REVISION_VERSION(<< PROTOCOL_NUMBER >> ) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            versionNumber = Integer.parseInt(rowParameter.get("VERSION_NUMBER").toString());
        }
        return versionNumber;
    }
    
    /**
     * Method used to get all Amendments & Renewals for a given protocol
     * from OSP$PROTOCOL
     *
     * <li>To fetch the data, it uses GET_ALL_AMENDMENT_REVISION procedure.
     *
     * @param protocolNumber this is given as input parameter for the
     * procedure to execute.
     * @return Vector collections of ProtocolAmendRenewalBean
     * @exception DBException if the instance of a dbEngine is null.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getAllAmendmentsRenewals(String protocolNumber)
    throws CoeusException, DBException{
        Vector result = null;
        Vector param= new Vector();
        ProtocolAmendRenewalBean protocolAmendRenewalBean = null;
        HashMap protocolRow = null;
        param.addElement(new Parameter("PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,protocolNumber));
        if(dbEngine !=null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
                    "call GET_ALL_AMENDMENT_REVISION( <<PROTOCOL_NUMBER>>, "+
                    "<<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        Vector protocolList = null;
        if (listSize > 0){
            protocolList = new Vector(3,2);
            for(int rowIndex=0; rowIndex<listSize; rowIndex++){
                protocolAmendRenewalBean = new ProtocolAmendRenewalBean();
                protocolRow = (HashMap)result.elementAt(rowIndex);
                protocolAmendRenewalBean.setProtocolAmendRenewalNumber((String)
                protocolRow.get("PROTO_AMEND_REN_NUMBER"));
                protocolAmendRenewalBean.setProtocolNumber((String)
                protocolRow.get("PROTOCOL_NUMBER"));
                protocolAmendRenewalBean.setSequenceNumber(
                        Integer.parseInt(protocolRow.get(
                        "SEQUENCE_NUMBER") == null ? "0" : protocolRow.get(
                        "SEQUENCE_NUMBER").toString()));
                protocolAmendRenewalBean.setDateCreated(
                        protocolRow.get("DATE_CREATED") == null ? null
                        :new Date( ((Timestamp) protocolRow.get(
                        "DATE_CREATED")).getTime()));
                protocolAmendRenewalBean.setSummary((String)
                protocolRow.get("SUMMARY"));
                protocolAmendRenewalBean.setUpdateTimestamp(
                        (Timestamp)protocolRow.get("UPDATE_TIMESTAMP"));
                protocolAmendRenewalBean.setUpdateUser((String)
                protocolRow.get("UPDATE_USER"));
                protocolAmendRenewalBean.setProtocolStatus( Integer.parseInt(protocolRow.get(
                        "PROTOCOL_STATUS_CODE") == null ? "0" : protocolRow.get(
                        "PROTOCOL_STATUS_CODE").toString()));
                protocolAmendRenewalBean.setProtocolStatusDescription((String)
                protocolRow.get("DESCRIPTION"));
                // Added for coeus4.3 enhancement - starts
                // get the editable modules list for particular parent protocol number
                protocolAmendRenewalBean.setVecModuleData(
                        getAmendRenewEditableData((String) protocolRow.get("PROTO_AMEND_REN_NUMBER")));
                // Added for coeus4.3 enhancement - ends
                // Added with CoeusQA2313: Completion of Questionnaire for Submission
                protocolAmendRenewalBean.setVecSelectedOrigProtoQnr(
                        getAmendRenewQuestionnaires((String) protocolRow.get("PROTO_AMEND_REN_NUMBER")));
                // CoeusQA2313: Completion of Questionnaire for Submission - End
                protocolList.add(protocolAmendRenewalBean);
            }
        }
        return protocolList;
    }
    
    /** This method is used to check whether the given Protocol can be Amended.
     *  <li>To fetch the data, it uses FN_CAN_AMMEND_PROTOCOL function.
     * @param protocolNumber Protocol Number
     * @throws CoeusException Coeus Exception
     * @throws DBException Database exception
     * @return boolean
     */
    
    public boolean canAmendProtocol(String protocolNumber)
    throws CoeusException, DBException {
        boolean success = false ;
        // get the new seq id and do the updation
        Vector param= new Vector();
        
        HashMap row = null;
        Vector result = new Vector();
        
        param= new Vector();
        param.add(new Parameter("AS_PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING, protocolNumber )) ;
        
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{<<OUT INTEGER SUCCESS>> = call FN_CAN_AMMEND_PROTOCOL( "
                    + " << AS_PROTOCOL_NUMBER >> )}", param) ;
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        
        if(!result.isEmpty()){
            row = (HashMap)result.elementAt(0);
            success = Integer.parseInt(row.get("SUCCESS").toString()) == 1 ? true : false;
        }
        return success ;
    }
    
    /*
     * action A for amendment and R for renewal
     */
    public boolean hasPendingAmendmentsRenewals(String protocolNumber, String action)
    throws CoeusException, DBException {
        boolean success = false ;
        // get the new seq id and do the updation
        Vector param= new Vector();
        
        HashMap row = null;
        Vector result = new Vector();
        
        param= new Vector();
        param.add(new Parameter("AS_PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING, protocolNumber )) ;
        param.add(new Parameter("AS_ACTION",
                DBEngineConstants.TYPE_STRING, action )) ;
        
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{<<OUT INTEGER SUCCESS>> = call FN_CHECK_IS_PEND_AMM_REN_PROTO( "
                    + " << AS_PROTOCOL_NUMBER >> , << AS_ACTION >> )}", param) ;
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        
        if(!result.isEmpty()){
            row = (HashMap)result.elementAt(0);
            success = Integer.parseInt(row.get("SUCCESS").toString()) >= 1 ? true : false;
        }
        return success ;
    }
    
    /** This method is used to check whether the given Protocol can be Renewed.
     *  <li>To fetch the data, it uses FN_CAN_RENEW_PROTOCOL function.
     * @param protocolNumber Protocol Number
     * @return boolean indicating whether the given Protocol can be Renewed
     * @throws CoeusException Coeus Exception
     * @throws DBException Database Exception
     */
    public boolean canRenewProtocol(String protocolNumber)
    throws CoeusException, DBException {
        boolean success = false ;
        // get the new seq id and do the updation
        Vector param= new Vector();
        
        HashMap row = null;
        Vector result = new Vector();
        
        param= new Vector();
        param.add(new Parameter("AS_PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING, protocolNumber )) ;
        
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{<<OUT INTEGER SUCCESS>> = call FN_CAN_RENEW_PROTOCOL( "
                    + " << AS_PROTOCOL_NUMBER >> )}", param) ;
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        
        if(!result.isEmpty()){
            row = (HashMap)result.elementAt(0);
            success = Integer.parseInt(row.get("SUCCESS").toString()) == 1 ? true : false;
        }
        return success ;
    }
    
    /**
     * This method populates the Coeus Modules in Protocol Related Projects Screens.
     * <li>To fetch the data, it uses the procedure GET_MODULE_FOR_PROTO_PROJECTS.
     *
     * @return Vector containing combobox bean of Module codes
     * @exception DBException if the instance of a dbEngine is null.
     */
    public Vector getProtoProjectRelatedModules() throws DBException{
        Vector result = new Vector(3,2);
        Vector vecModules=null;
        HashMap hasModules=null;
        Vector param= new Vector();
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call GET_MODULE_FOR_PROTO_PROJECTS ( <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new DBException("DB instance is not available");
        }
        int typesCount = result.size();
        if (typesCount >0){
            vecModules=new Vector();
            for(int types=0;types<typesCount;types++){
                hasModules = (HashMap)result.elementAt(types);
                vecModules.addElement(new ComboBoxBean(
                        hasModules.get("MODULE_CODE").toString(),
                        hasModules.get("DESCRIPTION").toString()));
            }
        }
        return vecModules;
    }
    
    /** Method used to get Related Projects from
     * OSP$PROTOCOL_RELATED_PROJECTS for a given protocolnumber and seqnumber.
     * <li>To fetch the data, it uses GET_PROTO_RELATED_PROJECTS.
     *
     * @return Vector map of relatedProjects data is set of ProtocolRelatedProjectsBean.
     * @param protocolNumber get list of relatedProjects for this id
     * @param seqNumber input to the procedure
     * @throws CoeusException Coeus Exception
     * @exception DBException if the instance of a dbEngine is null. */
    public Vector getProtocolRelatedProjects(String protocolNumber, int seqNumber)
    throws DBException, CoeusException{
//        System.out.println("Inside getProtocolRelatedProjects()");
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap projectsRow = null;
        ProtocolRelatedProjectsBean protocolRelatedProjectsBean = null;
        param.addElement(new Parameter("PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING, protocolNumber));
        param.addElement(new Parameter("SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT, new Integer(seqNumber).toString()));
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call GET_PROTO_RELATED_PROJECTS ( <<PROTOCOL_NUMBER>> , "
                    +" <<SEQUENCE_NUMBER>> , <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000") ;
        }
//        System.out.println("Size in getProtocolRelatedProjects() : "+result.size());
        int listSize = result.size();
        Vector projectsList = null;
        if(listSize>0){
            projectsList = new Vector(3,2);
            for(int rowIndex=0;rowIndex<listSize;rowIndex++){
                protocolRelatedProjectsBean = new ProtocolRelatedProjectsBean();
                projectsRow = (HashMap)result.elementAt(rowIndex);
                protocolRelatedProjectsBean.setProtocolNumber(
                        (String)projectsRow.get("PROTOCOL_NUMBER"));
                protocolRelatedProjectsBean.setSequenceNumber(
                        Integer.parseInt(projectsRow.get(
                        "SEQUENCE_NUMBER") == null ? "0" :
                            projectsRow.get(
                        "SEQUENCE_NUMBER").toString()));
                protocolRelatedProjectsBean.setProjectNumber(
                        projectsRow.get("PROJECT_NUMBER").toString());
                
                protocolRelatedProjectsBean.setModuleCode(
                        Integer.parseInt(projectsRow.get(
                        "MODULE_CODE") == null ? "0" :
                            projectsRow.get(
                        "MODULE_CODE").toString()));
                protocolRelatedProjectsBean.setTitle(
                        getProjectTitle(protocolRelatedProjectsBean.getModuleCode(),
                        protocolRelatedProjectsBean.getProjectNumber()));
                protocolRelatedProjectsBean.setDescription(
                        (String)projectsRow.get("DESCRIPTION"));
                protocolRelatedProjectsBean.setUpdateTimestamp(
                        (Timestamp)projectsRow.get("UPDATE_TIMESTAMP"));
                protocolRelatedProjectsBean.setUpdateUser(
                        (String)projectsRow.get("UPDATE_USER"));
                protocolRelatedProjectsBean.setAwModuleCode(
                        protocolRelatedProjectsBean.getModuleCode());
                protocolRelatedProjectsBean.setAwProjectNumber(
                        protocolRelatedProjectsBean.getProjectNumber());
                projectsList.add(protocolRelatedProjectsBean);
            }
        }
        return projectsList;
    }
    
    /**
     * Method used to get Title of Projects from Awards, Intitute Proposal
     * or Development Proposal based on module code
     * <li>To fetch the data, it uses FN_GET_RELATED_PROJECT_TITLE.
     *
     * @param moduleCode module code
     * @param projectNumber Project Number
     * @return String Title
     * @exception DBException if the instance of a dbEngine is null.
     * @exception CoeusException Coeus Exception
     */
    public String getProjectTitle(int moduleCode, String projectNumber)
    throws DBException, CoeusException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap projectsRow = null;
        
        param.addElement(new Parameter("MODULE_CODE",
                DBEngineConstants.TYPE_INT, new Integer(moduleCode).toString()));
        param.addElement(new Parameter("PROJECT_NUMBER",
                DBEngineConstants.TYPE_STRING, projectNumber));
        
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{<<OUT STRING TITLE>> = call FN_GET_RELATED_PROJECT_TITLE( "
                    + " << MODULE_CODE >>, << PROJECT_NUMBER >>)}", param) ;
        }else{
            throw new CoeusException("db_exceptionCode.1000") ;
        }
        
//        int listSize = result.size();
//        Vector projectsList = null;
        String title = "";
//        System.out.println("Title in side function before :"+title);
        if(!result.isEmpty()){
            projectsRow = (HashMap)result.elementAt(0);
            title = (String)projectsRow.get("TITLE");
        }
        if(title==null){
            title="";
        }
        return title;
    }
    
    /**
     *  The method used to fetch the specific correspondence report which is stored in the database
     *  and write to the Server hard drive. And return the filename with the relative path.
     *  the path.
     */
    public synchronized byte[] getSpecificCorrespondencePDF(String protocolNumber, int protoCorrespTypeCode,
            int actionId ) throws CoeusException, DBException {
        Vector result = new Vector(3, 2);
        Vector agendaParameter = new Vector();
//        String newAgendaID = null ;
        agendaParameter.addElement(new Parameter("PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                protocolNumber));
        agendaParameter.addElement(new Parameter("PROTO_CORRESP_TYPE_CODE",
                DBEngineConstants.TYPE_INT,
                ""+protoCorrespTypeCode));
        agendaParameter.addElement(new Parameter("ACTION_ID",
                DBEngineConstants.TYPE_INT,
                ""+actionId));
        
        String selectQuery = "SELECT CORRESPONDENCE FROM OSP$PROTOCOL_CORRESPONDENCE " +
                "WHERE PROTOCOL_NUMBER =  <<PROTOCOL_NUMBER>> " +
                " AND PROTO_CORRESP_TYPE_CODE =  <<PROTO_CORRESP_TYPE_CODE>> " +
                " AND ACTION_ID =  <<ACTION_ID>> " ;
        
        HashMap resultRow = null;
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus", selectQuery, "Coeus",agendaParameter);
            //System.out.println("result=>"+result);
            if( !result.isEmpty() ){
                resultRow = (HashMap)result.get(0);
                java.io.ByteArrayOutputStream correspReport =
                        (java.io.ByteArrayOutputStream)resultRow.get("CORRESPONDENCE");
                return correspReport.toByteArray();
            }
        }else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        
        return null;
    }
    
    /**
     * Method used to get list of all correspondeces generated for the given protocol number and action
     * <li>To fetch the data, it uses FN_GET_RELATED_PROJECT_TITLE.
     *
     * @param moduleCode module code
     * @param projectNumber Project Number
     * @return String Title
     * @exception DBException if the instance of a dbEngine is null.
     * @exception CoeusException Coeus Exception
     */
    public Vector getAllCorrespondenceDocuments(String protocolNumber, int sequenceNumber, int actionId)
    throws DBException, CoeusException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap correspondenceRow = null;
        
        param.addElement(new Parameter("PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING, protocolNumber));
        param.addElement(new Parameter("SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,
                ""+sequenceNumber ));
        param.addElement(new Parameter("ACTION_ID",
                DBEngineConstants.TYPE_INT,
                ""+actionId ));
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call GET_PROTO_CORRESPONDENCE_LIST ( <<PROTOCOL_NUMBER>> , "
                    +" <<SEQUENCE_NUMBER>> ,<< ACTION_ID >> , <<OUT RESULTSET rset>> )",
                    "Coeus", param);
            
        }else{
            throw new CoeusException("db_exceptionCode.1000") ;
        }
        
        int listSize = result.size();
        Vector correspondenceList = null;
        
        ProtoCorrespRecipientsBean protoCorrespRecipientsBean = null;
        if(listSize>0){
            correspondenceList = new Vector(3,2);
            for(int rowIndex=0;rowIndex<listSize;rowIndex++){
                protoCorrespRecipientsBean = new ProtoCorrespRecipientsBean();
                correspondenceRow = (HashMap)result.elementAt(rowIndex);
                protoCorrespRecipientsBean.setProtocolNumber(
                        (String)correspondenceRow.get("PROTOCOL_NUMBER"));
                protoCorrespRecipientsBean.setProtoCorrespTypeCode(
                        Integer.parseInt(correspondenceRow.get("PROTO_CORRESP_TYPE_CODE").toString()));
                protoCorrespRecipientsBean.setActionId(
                        Integer.parseInt(correspondenceRow.get("ACTION_ID").toString()));
                protoCorrespRecipientsBean.setProtoCorrespDescription(
                        correspondenceRow.get("DESCRIPTION").toString());
                protoCorrespRecipientsBean.setUpdateTimestamp(
                        (Timestamp)correspondenceRow.get("UPDATE_TIMESTAMP"));
                protoCorrespRecipientsBean.setUpdateUser(
                        (String)correspondenceRow.get("UPDATE_USER"));
                // Code added for 4.3 Enhancement - starts
                protoCorrespRecipientsBean.setSequenceNumber(
                        Integer.parseInt(correspondenceRow.get(
                        "SEQUENCE_NUMBER") == null ? "0" : correspondenceRow.get(
                        "SEQUENCE_NUMBER").toString()));
                protoCorrespRecipientsBean.setFinalFlag(
                        (correspondenceRow.get("FINAL_FLAG")==null)?false :
                            (correspondenceRow.get("FINAL_FLAG").equals("Y"))?true : false);
                // Code added for 4.3 Enhancement - ends
                correspondenceList.add(protoCorrespRecipientsBean);
            }
        }
        return correspondenceList;
    }
    
    
    public boolean actionHasCorrespondence(String protocolNumber, int sequenceNumber, int actionId) {
        int success = 0 ;
        boolean hasCorrespondence = false;
        try {
            // get the new seq id and do the updation
            Vector param= new Vector();
            
            HashMap nextNumRow = null;
            Vector result = new Vector();
            
            param= new Vector();
            param.add(new Parameter("AW_PROTOCOL_NUMBER",
                    DBEngineConstants.TYPE_STRING, protocolNumber )) ;
            param.add(new Parameter("AW_SEQUENCE_NUMBER",
                    DBEngineConstants.TYPE_INT, ""+sequenceNumber )) ;
            param.add(new Parameter("AW_ACTION_ID",
                    DBEngineConstants.TYPE_INT, ""+actionId )) ;
            
            if(dbEngine!=null){
                result = dbEngine.executeFunctions("Coeus",
                        "{<<OUT INTEGER SUCCESS>> = call FN_ACTION_HAS_CORRESPONDENCE( "
                        + " << AW_PROTOCOL_NUMBER >>, << AW_SEQUENCE_NUMBER >>, << AW_ACTION_ID >> )}", param) ;
            }else{
                throw new CoeusException("db_exceptionCode.1000") ;
            }
            if(!result.isEmpty()){
                nextNumRow = (HashMap)result.elementAt(0);
                success = Integer.parseInt(nextNumRow.get("SUCCESS").toString());
                if(success == 1){
                    hasCorrespondence = true;
                }else{
                    hasCorrespondence = false;
                }
            }
            
        } catch(Exception ex) {
//            ex.printStackTrace() ;
            UtilFactory.log(ex.getMessage(),ex,"ProtocolDataTxnBean", "actionHasCorrespondence");
        }
        
        return hasCorrespondence ;
    }
    
    /**
     * This method populates the notes for the protocol in a vector
     * this will be displayed in the ProtocolDetailsForm.
     * To fetch the data, it uses the procedure get_protocol_notes.
     *
     * @return Vector of ProtocolNotepadBeans
     * @exception DBException if the instance of dbEngine is null.
     */
    public Vector getProtocolNotRestrictedNotes(String protocolNumber,int seqNumber)
    throws DBException, CoeusException{
        Vector result = new Vector(3,2);
        Vector vecNotes = null;
        HashMap protoNotesRow = null;
        ProtocolNotepadBean protocolNotepadBean = null;
        Vector param= new Vector();
        param.addElement(new Parameter("PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,protocolNumber));
        param.addElement(new Parameter("SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,new Integer(seqNumber).toString()));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call GET_PROTO_NOT_RESTRICTED_NOTES ( <<PROTOCOL_NUMBER>> , "
                    +" <<SEQUENCE_NUMBER>> , <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int notesCount =result.size();
        if (notesCount >0){
            vecNotes = new Vector();
            for(int rowIndex=0;rowIndex<notesCount;rowIndex++){
                protocolNotepadBean = new ProtocolNotepadBean();
                protoNotesRow = (HashMap) result.elementAt(rowIndex);
                protocolNotepadBean.setProtocolNumber(
                        (String)protoNotesRow.get("PROTOCOL_NUMBER"));
                protocolNotepadBean.setSequenceNumber(
                        Integer.parseInt(protoNotesRow.get(
                        "SEQUENCE_NUMBER") == null ? "0" : protoNotesRow.get(
                        "SEQUENCE_NUMBER").toString()));
                protocolNotepadBean.setEntryNumber(
                        Integer.parseInt(protoNotesRow.get(
                        "ENTRY_NUMBER") == null ? "0" : protoNotesRow.get(
                        "ENTRY_NUMBER").toString()));
                protocolNotepadBean.setComments(
                        (String)protoNotesRow.get("COMMENTS"));
                protocolNotepadBean.setRestrictedFlag(
                        protoNotesRow.get("RESTRICTED_VIEW") == null ? false
                        :(protoNotesRow.get("RESTRICTED_VIEW").toString()
                        .equalsIgnoreCase("y") ? true :false));
                protocolNotepadBean.setUpdateTimestamp(
                        (Timestamp)protoNotesRow.get("UPDATE_TIMESTAMP"));
                protocolNotepadBean.setUpdateUser(
                        (String)protoNotesRow.get("UPDATE_USER"));
                //Added for COEUSDEV-165 : PI User unable to view notes that are saved in Lite - Start
                if(protoNotesRow.get("UPDATE_USER_NAME") != null) {
                    protocolNotepadBean.setUpdateUserName((String)protoNotesRow.get("UPDATE_USER_NAME"));
                } else {
                    protocolNotepadBean.setUpdateUserName((String)protoNotesRow.get("UPDATE_USER"));
                }
                //COEUSDEV-165 : END
                    
                vecNotes.add(protocolNotepadBean);
            }
        }
        return vecNotes;
    }
    
    /**
     *  This method used get max Protocol Notes Entry Number for the given protocol number
     *  <li>To fetch the data, it uses the function FN_GET_MAX_NOTES_ENTRY_NUMBER.
     *
     *  @return int max entry number for the protocol number
     *  @param String protocol number
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public int getMaxProtocolNotesEntryNumber(String protocolNumber)
    throws CoeusException, DBException {
        int entryNumber = 0;
        Vector param= new Vector();
        Vector result = new Vector();
        param.add(new Parameter("PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,protocolNumber));
        /* calling stored function */
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{ <<OUT INTEGER ENTRY_NUMBER>> = "
                    +" call FN_GET_MAX_NOTES_ENTRY_NUMBER(<< PROTOCOL_NUMBER >> ) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            entryNumber = Integer.parseInt(rowParameter.get("ENTRY_NUMBER").toString());
        }
        return entryNumber;
    }
    
    
    
    public Vector getProtocolUnitsMaxSeqNumber(String protocolNumber)
    throws CoeusException, DBException {
        Vector param= new Vector();
        Vector result = new Vector();
//        HashMap hashUnits = new HashMap() ;
        param.add(new Parameter("PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,protocolNumber));
        /* calling stored function */
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call GET_PROTO_UNITS_MAX_SEQ_NUM ( <<PROTOCOL_NUMBER>> , "
                    +" <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        
        return result ;
    }
    
    
    //Bug Fix:1444 Start
    /**
     * This method populates the list box meant to retrieve the Correspondent
     * Type in the protocol location screen.
     * <li>To fetch the data, it uses the procedure GET_CORRESP_TYPES_FOR_PROTO.
     *
     * @return Vector map of all correspondent type as key
     * and correspondent type description as value.
     * @exception DBException if the instance of a dbEngine is null.
     */
    public Vector getCorrespondentTypesForProtocol() throws DBException{
        Vector result = new Vector(3,2);
        Vector vecCorrsTypes = null;
        HashMap hasCorssTypes = null;
        Vector param= new Vector();
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call GET_CORRESP_TYPES_FOR_PROTO ( <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new DBException("DB instance is not available");
        }
        int ctypesCount = result.size();
        if (ctypesCount >0){
            vecCorrsTypes = new Vector();
            for(int types=0;types<ctypesCount;types++){
                hasCorssTypes = (HashMap)result.elementAt(types);
                vecCorrsTypes.addElement(new ComboBoxBean(
                        hasCorssTypes.get("CORRESPONDENT_TYPE_CODE").toString(),
                        hasCorssTypes.get("DESCRIPTION").toString()));
            }
        }
        return vecCorrsTypes;
    }
    //Bug Fix:1444 end
    
    /**
     * This method retrieves the Uploaded Documents
     * Used in IRB WEB Module.
     * <li>To fetch the data, it uses the procedure GET_PROTOCOL_DOCUMENT_LIST.
     *
     * @return Vector map of all correspondent type as key
     * and correspondent type description as value.
     * @exception DBException if the instance of a dbEngine is null.
     */
    public Vector getUploadDocumentForProtocol(String protocolNumber) throws DBException{
        Vector result = new Vector(3,2);
        Vector vecUploadDoc = null;
        HashMap hmUploadDoc = null;
        Vector param= new Vector();
        param.addElement(new Parameter("PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,protocolNumber));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call GET_PROTOCOL_DOCUMENT_LIST ( <<PROTOCOL_NUMBER>> , <<OUT RESULTSET rset>> )","Coeus", param);
        }else{
            throw new DBException("DB instance is not available");
        }
        int ctypesCount = result.size();
        if (ctypesCount >0){
            vecUploadDoc = new Vector();
            for(int types=0;types<ctypesCount;types++){
                hmUploadDoc = (HashMap)result.elementAt(types);
                UploadDocumentBean uploadDocumentBean = new UploadDocumentBean();
                
                uploadDocumentBean.setProtocolNumber((String)hmUploadDoc.get("PROTOCOL_NUMBER"));
                uploadDocumentBean.setSequenceNumber(Integer.parseInt(hmUploadDoc.get("SEQUENCE_NUMBER").toString()));
                //Commented for Protocol Upload Document enhancement start 1
                // uploadDocumentBean.setUploadId(Integer.parseInt(hmUploadDoc.get("DOC_ID").toString()));
                //Commented for Protocol Upload Document enhancement end 1
                //Added for Protocol Upload Document enhancement start 2
                uploadDocumentBean.setVersionNumber(Integer.parseInt(hmUploadDoc.get("VERSION_NUMBER").toString()));
                //Added for Protocol Upload Document enhancement end 2
                uploadDocumentBean.setDocCode(Integer.parseInt(hmUploadDoc.get("DOCUMENT_TYPE_CODE").toString()));
                uploadDocumentBean.setDocType((String)hmUploadDoc.get("DOC_TYPE_DESCRIPTION"));
                uploadDocumentBean.setDescription(hmUploadDoc.get("DESCRIPTION") == null ? "" : (String)hmUploadDoc.get("DESCRIPTION"));
                uploadDocumentBean.setFileName((String)hmUploadDoc.get("FILE_NAME"));
                uploadDocumentBean.setUpdateUser((String)hmUploadDoc.get("UPDATE_USER"));
                //uploadDocumentBean.setDocument(((ByteArrayOutputStream)hmUploadDoc.get("DOCUMENT")).toByteArray());
                if(hmUploadDoc.get("DOCUMENT")!=null){
                    uploadDocumentBean.setDocument(((ByteArrayOutputStream)hmUploadDoc.get("DOCUMENT")).toByteArray());
                }else{
                    uploadDocumentBean.setDocument(null);
                }
                uploadDocumentBean.setDocumentId(Integer.parseInt(hmUploadDoc.get("DOCUMENT_ID").toString()));
                uploadDocumentBean.setAw_documentId(Integer.parseInt(hmUploadDoc.get("DOCUMENT_ID").toString()));
                /*
                 * UserId to UserName Enhancement - Start
                 * Added new property to get username
                 */
                if(hmUploadDoc.get("UPDATE_USER_NAME") != null) {
                    uploadDocumentBean.setUpdateUserName((String)hmUploadDoc.get("UPDATE_USER_NAME"));
                } else {
                    uploadDocumentBean.setUpdateUserName((String)hmUploadDoc.get("UPDATE_USER"));
                }
                //UserId to UserName Enhancement - End
                uploadDocumentBean.setUpdateTimestamp((Timestamp)hmUploadDoc.get("UPDATE_TIMESTAMP"));
                
                //                try{
                //                    ByteArrayOutputStream byteArrayOutputStream;
                //                    byteArrayOutputStream =(ByteArrayOutputStream)hmUploadDoc.get("DOCUMENT");
                //                    byte data[] = byteArrayOutputStream.toByteArray();
                //                    byteArrayOutputStream.close();
                //                    uploadDocumentBean.setDocument(data);
                //                }catch (Exception ex){
                //                    ex.printStackTrace();
                //                }
                uploadDocumentBean.setAw_protocolNumber((String)hmUploadDoc.get("PROTOCOL_NUMBER"));
                uploadDocumentBean.setAw_sequenceNumber(Integer.parseInt(hmUploadDoc.get("SEQUENCE_NUMBER").toString()));
                //Commented for Protocol Upload Document enhancement start 3
                //uploadDocumentBean.setAw_uploadID(Integer.parseInt(hmUploadDoc.get("DOC_ID").toString()));
                //Commented for Protocol Upload Document enhancement end 3
                //Added for Protocol Upload Document enhancement start 4
                uploadDocumentBean.setAw_versionNumber(Integer.parseInt(hmUploadDoc.get("VERSION_NUMBER").toString()));
                uploadDocumentBean.setAw_docCode(Integer.parseInt(hmUploadDoc.get("DOCUMENT_TYPE_CODE").toString()));
                uploadDocumentBean.setStatusCode(Integer.parseInt(hmUploadDoc.get("DOCUMENT_STATUS_CODE").toString()));
                uploadDocumentBean.setAw_StatusCode(Integer.parseInt(hmUploadDoc.get("DOCUMENT_STATUS_CODE").toString()));
                uploadDocumentBean.setStatusDescription(hmUploadDoc.get("DOC_STATUS_DESCRIPTION").toString());
                //Added for Protocol Upload Document enhancement end 4
                vecUploadDoc.addElement(uploadDocumentBean);
            }
        }
        return vecUploadDoc;
    }
    //Modified for Protocol Upload document Enhancement start 5
    /**
     * This method retrieves the Uploaded Document for a givien version number
     * Used in IRB WEB Module.
     * <li>To fetch the data, it uses the procedure GET_PROTOCOL_DOC_FOR_VERSION.
     *
     * @return bean conataining data
     * @exception DBException if the instance of a dbEngine is null.
     */
    //Modified for Case#3036 - Didn't recognize document file type in Protocol attachments -Start
    /*public UploadDocumentBean getUploadDocumentForVersionNumber(String protocolNumber,
            int seqNumber,int docType, int versionNumber) throws DBException{*/
    public UploadDocumentBean getUploadDocumentForVersionNumber(String protocolNumber,
            int seqNumber,int docType, int versionNumber,int docuId) throws DBException{
   //Modified for Case#3036 - Didn't recognize document file type in Protocol attachments - End
        Vector result = new Vector(3,2);
//        Vector vecUploadDoc = null;
        HashMap hmUploadDoc = null;
        Vector param= new Vector();
        param.addElement(new Parameter("PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,protocolNumber));
        param.addElement(new Parameter("SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT, ""+seqNumber));
        //Modified for Case#3036 - Didn't recognize document file type in Protocol attachments -Start
        param.addElement(new Parameter("DOCUMENT_TYPE_CODE",
                DBEngineConstants.TYPE_INT, ""+docType));
        
        param.addElement(new Parameter("VERSION_NUMBER",
                DBEngineConstants.TYPE_INT, ""+versionNumber));        
        
        param.addElement(new Parameter("DOCUMENT_ID",
                DBEngineConstants.TYPE_INT, ""+docuId));
        if(dbEngine!=null){
            /*result = dbEngine.executeRequest("Coeus",
                    "call GET_PROTOCOL_DOC_FOR_VERSION ( <<PROTOCOL_NUMBER>> , "
                    +" <<SEQUENCE_NUMBER>> ,<<AV_DOCUMENT_TYPE_CODE>>, <<AV_VERSION_NUMBER>>" +
                    ",<<OUT RESULTSET rset>> )","Coeus", param);*/
            String Statement ="SELECT * FROM OSP$PROTOCOL_DOCUMENTS WHERE  OSP$PROTOCOL_DOCUMENTS.PROTOCOL_NUMBER = <<PROTOCOL_NUMBER>> " +
                               "AND OSP$PROTOCOL_DOCUMENTS.SEQUENCE_NUMBER = <<SEQUENCE_NUMBER>> AND OSP$PROTOCOL_DOCUMENTS.DOCUMENT_TYPE_CODE = <<DOCUMENT_TYPE_CODE>> " +
                                "AND OSP$PROTOCOL_DOCUMENTS.VERSION_NUMBER = <<VERSION_NUMBER>> AND OSP$PROTOCOL_DOCUMENTS.DOCUMENT_ID = <<DOCUMENT_ID>> ";
            result = dbEngine.executeRequest("COEUS", Statement, "COEUS",param);
            //Modified for Case#3036 - Didn't recognize document file type in Protocol attachments -End
        }else{
            throw new DBException("DB instance is not available");
        }
        int ctypesCount = result.size();
        UploadDocumentBean uploadDocumentBean = null;
        if (ctypesCount >0){
            hmUploadDoc = (HashMap)result.elementAt(0);
            uploadDocumentBean = new UploadDocumentBean();
            
            uploadDocumentBean.setProtocolNumber((String)hmUploadDoc.get("PROTOCOL_NUMBER"));
            uploadDocumentBean.setSequenceNumber(Integer.parseInt(hmUploadDoc.get("SEQUENCE_NUMBER").toString()));
            uploadDocumentBean.setVersionNumber(Integer.parseInt(hmUploadDoc.get("VERSION_NUMBER").toString()));
            uploadDocumentBean.setDocCode(Integer.parseInt(hmUploadDoc.get("DOCUMENT_TYPE_CODE").toString()));
            uploadDocumentBean.setDocType((String)hmUploadDoc.get("DOC_TYPE_DESCRIPTION"));
            uploadDocumentBean.setDescription((String)hmUploadDoc.get("DESCRIPTION"));
            uploadDocumentBean.setFileName((String)hmUploadDoc.get("FILE_NAME"));
            uploadDocumentBean.setUpdateUser((String)hmUploadDoc.get("UPDATE_USER"));
            uploadDocumentBean.setUpdateTimestamp((Timestamp)hmUploadDoc.get("UPDATE_TIMESTAMP"));
            
            try{
                ByteArrayOutputStream byteArrayOutputStream;
                byteArrayOutputStream =(ByteArrayOutputStream)hmUploadDoc.get("DOCUMENT");
                byte data[] = byteArrayOutputStream.toByteArray();
                byteArrayOutputStream.close();
                uploadDocumentBean.setDocument(data);
            }catch (Exception ex){
//                ex.printStackTrace();
                UtilFactory.log(ex.getMessage(),ex,"ProtocolDataTxnBean", "getUploadDocumentForVersionNumber");
            }
        }
        return uploadDocumentBean;
    }
    /**
     * This method retrieves the Uploaded Documents for Protocol for all sequence number.
     * <li>To fetch the data, it uses the procedure GET_PROTOCOL_DOCUMENTS_HISTORY.
     * @param protocol Number to fetch data
     * @param documentId for which the history is fetched
     * @return Vector containg the history of the selected document
     * @exception DBException if the instance of a dbEngine is null.
     */
    public Vector getProtocolHistoryData(String protocolNumber, String documentId) throws DBException {
        Vector result = new Vector(3,2);
        Vector vecParentData = null;
        HashMap hmUploadDoc = null;
        Vector param= new Vector();
        param.addElement(new Parameter("AV_PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,protocolNumber));
        // Modified Coeus 4.3 enhancement- start
        /*To show the history for a particular document the documentId is also
        passed while fetching the documents*/
        param.addElement(new Parameter("AV_DOCUMENT_ID",
                DBEngineConstants.TYPE_INT,documentId));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call GET_PROTOCOL_DOCUMENTS_HISTORY ( <<AV_PROTOCOL_NUMBER>> ,<<AV_DOCUMENT_ID>>, "
                    +" <<OUT RESULTSET rset>> )","Coeus", param);
            // Coeus 4.3 enhancement- end
        }else{
            throw new DBException("DB instance is not available");
        }
        int ctypesCount = result.size();
        if (ctypesCount >0){
            vecParentData = new Vector();
            for(int types = 0; types < ctypesCount; types ++){
                hmUploadDoc = (HashMap)result.elementAt(types);
                UploadDocumentBean uploadDocumentBean = new UploadDocumentBean();
                
                uploadDocumentBean.setProtocolNumber((String)hmUploadDoc.get("PROTOCOL_NUMBER"));
                uploadDocumentBean.setSequenceNumber(Integer.parseInt(hmUploadDoc.get("SEQUENCE_NUMBER").toString()));
                uploadDocumentBean.setVersionNumber(Integer.parseInt(hmUploadDoc.get("VERSION_NUMBER").toString()));
                uploadDocumentBean.setDocCode(Integer.parseInt(hmUploadDoc.get("DOCUMENT_TYPE_CODE").toString()));
                uploadDocumentBean.setDocType((String)hmUploadDoc.get("PROTO_TYPE_DESC"));
                uploadDocumentBean.setDescription(hmUploadDoc.get("DESCRIPTION") == null ? "" : (String)hmUploadDoc.get("DESCRIPTION"));
                uploadDocumentBean.setFileName((String)hmUploadDoc.get("FILE_NAME"));
                uploadDocumentBean.setUpdateUser((String)hmUploadDoc.get("UPDATE_USER"));
                //Added for Coeus4.3 Enhancement- Starts
                //Introduced document id to identify a document
                uploadDocumentBean.setDocumentId(Integer.parseInt(hmUploadDoc.get("DOCUMENT_ID").toString()));
                uploadDocumentBean.setAw_documentId(Integer.parseInt(hmUploadDoc.get("DOCUMENT_ID").toString()));
                //Added for Coeus4.3 Enhancement- Ends
                
                /*
                 * UserId to UserName Enhancement - Start
                 * Added new property to get username
                 */
                if(hmUploadDoc.get("UPDATE_USER_NAME") != null) {
                    uploadDocumentBean.setUpdateUserName((String)hmUploadDoc.get("UPDATE_USER_NAME"));
                } else {
                    uploadDocumentBean.setUpdateUserName((String)hmUploadDoc.get("UPDATE_USER"));
                }
                //Userid to Username enhancement - End
                uploadDocumentBean.setUpdateTimestamp((Timestamp)hmUploadDoc.get("UPDATE_TIMESTAMP"));
                uploadDocumentBean.setAw_protocolNumber((String)hmUploadDoc.get("PROTOCOL_NUMBER"));
                uploadDocumentBean.setAw_sequenceNumber(Integer.parseInt(hmUploadDoc.get("SEQUENCE_NUMBER").toString()));
                uploadDocumentBean.setAw_versionNumber(Integer.parseInt(hmUploadDoc.get("VERSION_NUMBER").toString()));
                uploadDocumentBean.setAw_docCode(Integer.parseInt(hmUploadDoc.get("DOCUMENT_TYPE_CODE").toString()));
                uploadDocumentBean.setStatusCode(Integer.parseInt(hmUploadDoc.get("DOCUMENT_STATUS_CODE").toString()));
                uploadDocumentBean.setAw_StatusCode(Integer.parseInt(hmUploadDoc.get("DOCUMENT_STATUS_CODE").toString()));
                uploadDocumentBean.setStatusDescription((String)hmUploadDoc.get("PROTO_STATUS_DESC"));
                vecParentData.addElement(uploadDocumentBean);
            }
        }
        return vecParentData;
    }
    //Modified for Protocol Upload document Enhancement end 5
    /**
     * This method populates the combobox in Upload Document Page of IRB-WEB
     * Type in the protocol location screen.
     * <li>To fetch the data, it uses the procedure GET_PROTOCOL_DOCUMENT_TYPE.
     *
     * @return CoeusVector map of all Doc Code as key
     * and Doc Type description as value.
     * @exception DBException if the instance of a dbEngine is null.
     */
    public CoeusVector getProtocolDocumetTypes() throws DBException{
        Vector result = new Vector(3,2);
        CoeusVector vecDocTypes = null;
        HashMap hmDocTypes = null;
        Vector param= new Vector();
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call GET_PROTOCOL_DOCUMENT_TYPE ( <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new DBException("DB instance is not available");
        }
        int ctypesCount = result.size();
        if (ctypesCount >0){
            vecDocTypes = new CoeusVector();
            for(int types=0;types<ctypesCount;types++){
                hmDocTypes = (HashMap)result.elementAt(types);
                //Modified for case 3552 - IRB attachments - start
                if(hmDocTypes.get("DOCUMENT_TYPE_GROUP").toString().equals("P")){
                    vecDocTypes.addElement(new ComboBoxBean(
                            hmDocTypes.get("DOCUMENT_TYPE_CODE").toString(),
                            hmDocTypes.get("DESCRIPTION").toString()));
                }
                //Modified for case 3552 - IRB attachments - end
            }
        }
        return vecDocTypes;
    }
    
    //Protocol Enhancment Case #1790 Start - Action Comments Editable(Rights Checking)
    /**
     *  This method used get Lead Unit of the Protocol for the given protocol and sequence number
     *  <li>To fetch the data, it uses the function FN_GET_LEAD_UNIT_FOR_PROTOCOL.
     *
     *  @return int review number for the protocol number and sequence number
     *  @param String protocol number  and sequence number
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public String getLeadUnitForProtocol(String protocolNumber,int seqNumber)
    throws CoeusException, DBException {
        String leadUnit = null;
        Vector param= new Vector();
        Vector result = new Vector();
        param.add(new Parameter("PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,protocolNumber));
        param.addElement(new Parameter("SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,new Integer(seqNumber).toString()));
        /* calling stored function */
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{ <<OUT STRING UNIT_NUMBER>> = "
                    +" call FN_GET_LEAD_UNIT_FOR_PROTOCOL(<< PROTOCOL_NUMBER >> ,<< SEQUENCE_NUMBER >> ) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            leadUnit = rowParameter.get("UNIT_NUMBER").toString();
        }
        return leadUnit;
    }
    //Protocol Enhancment Case #1790 End - Action Comments Editable(Rights Checking)
    
    //CoeusEnhancement case #1799 start
    /**
     * This method returns the email Id for the funding source recipients
     * To fetch the data, it uses the procedure GET_NOTIF_RECIP_FOR_PROTO_LINK
     *
     * @return Vector of String
     * @exception DBException if the instance of dbEngine is null.
     */
    public Vector getRecipients(String id,int moduleId)
    throws DBException{
        Vector result = new Vector(3,2);
        Vector vecNotes = null;
        HashMap protoNotesRow = null;
        //  ProtocolNotepadBean protocolNotepadBean = null;
        Vector param= new Vector();
        param.addElement(new Parameter("AV_PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,id));
        param.addElement(new Parameter("AV_MODULE_CODE",
                DBEngineConstants.TYPE_INT,moduleId));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call GET_NOTIF_RECIP_FOR_PROTO_LINK  ( <<AV_PROTOCOL_NUMBER>> ,<<AV_MODULE_CODE>>,<<OUT RESULTSET DETAILS>>)",
                    "Coeus", param);
        }else{
            throw new DBException("DB instance is not available");
        }
        int notesCount =result.size();
        if (notesCount >0){
            vecNotes = new Vector();
            //COEUSDEV-75:Rework email engine so the email body is picked up from one place
            PersonRecipientBean personInfoBean = null;
            for(int rowIndex=0;rowIndex<notesCount;rowIndex++){
                protoNotesRow = (HashMap) result.elementAt(rowIndex);
                personInfoBean = new PersonRecipientBean();
                personInfoBean.setEmailId((String)protoNotesRow.get("EMAIL_ADDRESS"));
                personInfoBean.setUserId((String)protoNotesRow.get("USER_NAME"));
                vecNotes.add(personInfoBean);
            }
            //COEUSDEV-75:End
        }
        return vecNotes;
    }
    
    
    
    // Main method for testing the methods of protocol development
   /*public static void main(String args[]) throws DBException,CoeusException{
        ProtocolDataTxnBean txn = new ProtocolDataTxnBean();
        try{
    
            boolean success = txn.actionHasCorrespondence("0000002000",2,6);
            System.out.println("Has correspondence : "+success);
    */
            /*if(vctTest != null){
                ProtocolReferencesBean protocolReferencesBean;
                protocolReferencesBean = (ProtocolReferencesBean) vctTest.elementAt(0);
                System.out.println("Protocol No : "+protocolReferencesBean.getProtocolNumber());
                System.out.println("Reference Key : "+protocolReferencesBean.getReferenceKey());
                System.out.println("Application date : "+protocolReferencesBean.getApplicationDate());
             
                System.out.println("size : "+vctTest.size());
            }else{
                System.out.println("Is null");
            }*/
    
    
           /* Vector listBean = txn.getSpecialReviewCode();
            int comboLength = listBean.size();
            for(int comboIndex=0;comboIndex<comboLength;comboIndex++){
                ComboBoxBean comBean
                    = (ComboBoxBean)listBean.elementAt(comboIndex);
                System.out.println("code >>>> "
                    +comBean.getCode());
                System.out.println("description >>>> "
                    +comBean.getDescription());
            }*/
    
            /*Vector listBean = txn.getReviewApprovalType();
            int comboLength = listBean.size();
            for(int comboIndex=0;comboIndex<comboLength;comboIndex++){
                ComboBoxBean comBean
                    = (ComboBoxBean)listBean.elementAt(comboIndex);
                System.out.println("code >>>> "
                    +comBean.getCode());
                System.out.println("description >>>> "
                    +comBean.getDescription());
            }*/
    
           /* Vector listBean = txn.getProtocolSpecialReview("0000000928",1);
            int comboLength = listBean.size();
            for(int comboIndex=0;comboIndex<comboLength;comboIndex++){
                roleInfoBean depBean
                    = (ProtocolSpecialReviewFormBean)listBean.elementAt(comboIndex);
                System.out.println("proposal number >>>> "
                    +depBean.getProtocolNumber());
                System.out.println("sequence number >>>> "
                    +depBean.getSequenceNumber());
                System.out.println("special review number >>>> "
                    +depBean.getSpecialReviewNumber());
                System.out.println("speccial review code >>>> "
                    +depBean.getSpecialReviewCode());
                System.out.println("approval code >>>> "
                    +depBean.getApprovalCode());
                System.out.println("sp rev protocol number >>>> "
                    +depBean.getProtocolSPRevNumber());
            }*/
    
           /*SPApprovalInfoBean approvalInfoBean = new SPApprovalInfoBean();
           approvalInfoBean = txn.getApprovalListFlag(1,1) ;
                System.out.println("protocol flag >>>> "
                    +approvalInfoBean.getProtocolFlag());
                System.out.println("approval flag >>>> "
                    +approvalInfoBean.getApprovalDateFlag());
                System.out.println("application flag >>>> "
                    +approvalInfoBean.getApplicationDateFlag());
            System.out.println("Testing");*/
       /*
        }catch(Exception e){
            e.printStackTrace();
            System.out.println(e);
        }
    }*/
    
    /* the method send the email to the given EMail Id
     *  @param String toID - email ID
     *
     */
//  Commented with COEUSDEV-75:Rework email engine so the email body is picked up from one place
//    /** This method is used to send mail. Returns true if the mail is sent successfully.
//     * @return boolean
//     * @param String mailIds
//     * @param Vector attachmentFilePath
//     *
//     **/
//    public boolean sendEmailNotification(String fundingSourceId,String module_code,String message) throws Exception{
//        
//        boolean isSend = false ;
//        String strEnabled ;
//        DepartmentPersonTxnBean departmentPersonTxnBean = new DepartmentPersonTxnBean();
//        //        UtilFactory UtilFactory = new UtilFactory();
//        CoeusMessageResourcesBean coeusMessageResourcesBean = new CoeusMessageResourcesBean();
//        
//        Vector toID = getRecipients(fundingSourceId,module_code);
//        
//        //  protocolDataTxnBean.sendEmailNotification(toID);
//        String recipients= "";
//        if(toID != null) {
//            for(int i=0;i<toID.size();i++) {
//                PersonInfoBean personBean = (PersonInfoBean)toID.get(i);
//                if(personBean != null && personBean.getEmail()!=null && personBean.getEmail()!= ""){
//                    if(recipients!=""){
//                        recipients = recipients + "," +personBean.getEmail();
//                    }else {
//                        recipients = personBean.getEmail();
//                    }
//                }
//            }
//        }
//        if (recipients == "")
//            return false;
//        //start
//        //Check if mail service is enabled
//        strEnabled = departmentPersonTxnBean.getParameterValues("CMS_ENABLED");
//        if(!strEnabled.equalsIgnoreCase("1")){
//            return false;
//        }
//        
//        //To get email Id of logged in User
//        CoeusFunctions coeusFunctions = new CoeusFunctions();
//        UserInfoBean userInfoBean;
//        
//        ReadJavaMailProperties readJavaMailProperties = new ReadJavaMailProperties();
//        SetMailAttributes setMailAttributes = new SetMailAttributes();
//        String strMode = departmentPersonTxnBean.getParameterValues("CMS_MODE");
//        String strToAddress;
//        String strFromAddress = departmentPersonTxnBean.getParameterValues("CMS_SENDER_ID");
//        if(strMode.equalsIgnoreCase("T")){
//            strToAddress = departmentPersonTxnBean.getParameterValues("CMS_TEST_MAIL_RECEIVE_ID");
//            setMailAttributes.setTo(strToAddress);
//            setMailAttributes.setFrom(strFromAddress);
//        }else{
//            setMailAttributes.setTo(recipients);
//            
//            //end
//            //To get email Id of logged in User
//            
//            String strEmailId = "";
//            userInfoBean = (UserInfoBean) coeusFunctions.getUserDetails(userId);
//            if(userInfoBean != null && userInfoBean.getEmailId()!=null){
//                strEmailId = userInfoBean.getEmailId();
//            }
//            
//            if(strEmailId==null || strEmailId.equals("")){
//                strEmailId = coeusMessageResourcesBean.parseMessageKey("mailDefaultFromAddress_exceptionCode.7502");
//            }
//            // if(!strMode.equalsIgnoreCase("T"))
//            setMailAttributes.setFrom(strEmailId);
//            
//        }
//        setMailAttributes.setSubject(coeusMessageResourcesBean.parseMessageKey("mailSubjectIRB_exceptionCode.7504"));
//        // String mailMsg = null;
//        
//        setMailAttributes.setMessage(message);
//        
//        setMailAttributes.setSend("Y");
//        
//        SendJavaMail sendJavaMail = new SendJavaMail(setMailAttributes,readJavaMailProperties);
//        
//        isSend = sendJavaMail.sendMessage();
//        if(isSend){
//            UtilFactory.log("The message : \n"+message +"\n has been sent to the following recipients : "+setMailAttributes.getTo()+
//                    "\n", null, "MailServlet","sendMail");
//        }
//        return isSend;
//    }
    //  Added with COEUSDEV-75:Rework email engine so the email body is picked up from one place
        
    /** This method is used to send mail notification for funding source actions
     * @return boolean
     * @param int moduleCode - The module on which action is performed
     * @param int actionCode - The action being performed - special review inserted/deleted
     * @param String moduleItemKey - The module item key
     * @param int moduleItemKeySequence - The module Item key sequence.
     * @param String fundingSrcId - The funding source being added.
     * @param int fundingSrcModuleCode - The funding source module code.
     * @return boolean true if mail sent successfully.
     *
     **/
    public boolean sendEmailNotification(int moduleCode,int actionCode, String moduleItemKey, int moduleItemKeySequence,String fundingSrcId,int fundingSrcModuleCode) throws Exception{
        
        boolean isSent = false ;
        Vector toID = getRecipients(fundingSrcId,fundingSrcModuleCode);
        if(toID != null) {
            MailHandler mailHandler = new MailHandler();
            CoeusMessageResourcesBean coeusMessageResourcesBean = new CoeusMessageResourcesBean();
            // COEUSQA-2105: No notification for some IRB actions 
//            String moduleNotification =IRB_NOTIFICATION;
//            if(moduleCode==AWARD_MODULE_CODE){
//                moduleNotification = AWARD_NOTIFICATION;
//            }else if(moduleCode==PROPOSAL_INSTITUTE_MODULE_CODE){
//                moduleNotification = PROPOSAL_INST_NOTIFICATION;
//            }
//            String subject = MailProperties.getProperty(moduleNotification+DOT+actionCode+DOT+SUBJECT);
//            String message = MailProperties.getProperty(moduleNotification+DOT+actionCode+DOT+BODY);
//            MailMessageInfoBean messageBean = new MailMessageInfoBean();
            MailMessageInfoBean messageBean = mailHandler.getNotification(moduleCode,actionCode,moduleItemKey,moduleItemKeySequence);
            if( messageBean != null && messageBean.isActive()){
                //COEUSQA-2105: No notification for some IRB actions 
//                messageBean.setSubject(subject);
//                messageBean.setMessage(message);
                messageBean.setPersonRecipientList(toID);
//                isSent = mailHandler.sendSystemGeneratedMail(moduleCode,actionCode,moduleItemKey,moduleItemKeySequence,messageBean);
                // Modified for COEUSQA-3030 : Special review inserted notification not working - Start
//                isSent = mailHandler.sendMail(actionCode,actionCode,moduleItemKey,moduleItemKeySequence,messageBean);                
                isSent = mailHandler.sendMail(moduleCode,actionCode,moduleItemKey,moduleItemKeySequence,messageBean);
                // Modified for COEUSQA-3030 : Special review inserted notification not working - End

            } else {
                UtilFactory.log("Did not send mail for the action "+actionCode+ " for the module "+moduleCode+" for the module item key " +moduleItemKey);
            }
        }
        return isSent;
    }
    
    //Case 1787 Start
    //Lock the protocol in Display Mode
    // 2930: Auto-delete Current Locks based on new parameter
    // Changed the Return Type of the method
//    public void lockProtocol(
//            String protocolNumber,String loggedinUser, String unitNumber) throws Exception{
        
     public LockingBean lockProtocol(
            String protocolNumber,String loggedinUser, String unitNumber) throws Exception{
        String rowId = rowLockStr+protocolNumber;
        if(dbEngine!=null){
            conn = dbEngine.beginTxn();
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        LockingBean lockingBean = new LockingBean();
        lockingBean = transMon.canEdit(rowId,loggedinUser,unitNumber,conn);
        boolean lockCheck = lockingBean.isGotLock();
        if(lockCheck)
            try{
                transactionCommit();
                // 2930: Auto-delete Current Locks based on new parameter
                return lockingBean;
            }catch(DBException dbEx){
//                dbEx.printStackTrace();
                transactionRollback();
                throw dbEx;
            } finally {
                //closed the connection -- added by Jobin
                endConnection();
            } else
                throw new CoeusException("exceptionCode.999999");
    }

    // for protocol Routing

          public LockingBean lockProtocolRouting(
            String protocolNumber,String loggedinUser, String unitNumber) throws Exception{
        String rowId = routingLockStr+protocolNumber;
        if(dbEngine!=null){
            conn = dbEngine.beginTxn();
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        LockingBean lockingBean = new LockingBean();
        lockingBean = transMon.canEdit(rowId,loggedinUser,unitNumber,conn);
        boolean lockCheck = lockingBean.isGotLock();
        if(lockCheck)
            try{
                transactionCommit();
                // 2930: Auto-delete Current Locks based on new parameter
                return lockingBean;
            }catch(DBException dbEx){
//                dbEx.printStackTrace();
                transactionRollback();
                throw dbEx;
            } finally {
                //closed the connection -- added by Jobin
                endConnection();
            } else
                throw new CoeusException("exceptionCode.999999");
    }




    //Added for case #1961 start 4
    /**
     * Getter for property vecCorresType.
     * @return Value of property vecCorresType.
     */
    public java.util.Vector getVecCorresType() {
        return vecCorresType;
    }
    
    /**
     * Setter for property vecCorresType.
     * @param vecCorresType New value of property vecCorresType.
     */
    public void setVecCorresType(java.util.Vector vecCorresType) {
        this.vecCorresType = vecCorresType;
    }
    
    /*
     * Coeus4.3 Enhancement - Start
     * More flexibility to assigning access view
     * This method is added to get the units for a particular protocol
     */
    
    public Vector getUnitsForProtocolLocations(String protoNum) throws DBException {
        Vector result = new Vector();
        Vector param= new Vector();
        Vector vecUnits = null;
        HashMap hmUnitsList = null;
        param.addElement(new Parameter("AS_PROTOCOL_NUMBER", DBEngineConstants.TYPE_STRING, protoNum));
         if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call GET_UNITS_FOR_PROTO_LOCATIONS  ( <<AS_PROTOCOL_NUMBER>> ,<<OUT RESULTSET DETAILS>>)",
            "Coeus", param);
        }else{
            throw new DBException("DB instance is not available");
        }
        
        if(result != null && result.size() > 0 ) {
            vecUnits = new Vector();
            for(int index = 0; index < result.size(); index++) {
                hmUnitsList = (HashMap)result.get(index);
                vecUnits.addElement(hmUnitsList.get("UNIT_NUMBER"));
            }
        }
        return vecUnits;
    }
    //Coeus4.3 Enhancement Protocol Right Checking - End
    /**
     * Coeus 4.3 enhancement
     * Checks whether a particular document can be ammended or not
     * @returns boolean true if ammendable else false
     */
    public boolean isDocumentAmendable(String protocolNumber, int documentId){
        int ammendable = 0 ;
        try {
            Vector param= new Vector();
            
            HashMap rowMap = null;
            Vector result = new Vector();
            
            param= new Vector();
            param.add(new Parameter("AS_PROTOCOL_NUMBER",
                    DBEngineConstants.TYPE_STRING, protocolNumber )) ;
            param.add(new Parameter("AS_DOCUMENT_ID",
                    DBEngineConstants.TYPE_INT, Integer.toString(documentId))) ;
            if(dbEngine!=null){
                result = dbEngine.executeFunctions("Coeus",
                        "{<<OUT INTEGER AMENDABLE>> = call FN_CAN_AMEND_PROTOCOL_DOCUMENT( "
                        + " << AS_PROTOCOL_NUMBER >>, << AS_DOCUMENT_ID >> )}", param) ;
            }else{
                throw new CoeusException("db_exceptionCode.1000") ;
            }
            if(!result.isEmpty()){
                rowMap = (HashMap)result.elementAt(0);
                ammendable = Integer.parseInt(rowMap.get("AMENDABLE").toString());
            }
            
        } catch(Exception ex) {
//            ex.printStackTrace() ;
            UtilFactory.log(ex.getMessage(),ex,"ProtocolDataTxnBean", "actionHasCorrespondence");
      }
        
        return ammendable==0? true : false ;
    }
    
    /**
     * Added for coeus4.3 enhancement 2
     * To get the editable modules list for parent protocol number
     * @param amendRenewalProtocol 
     * @throws edu.mit.coeus.exception.CoeusException 
     * @throws edu.mit.coeus.utils.dbengine.DBException 
     * @return Vector
     */
    public Vector getAmendRenewEditableData(String amendRenewalProtocol) 
        throws CoeusException, DBException {
        // To get the editable modules list for particular Amendment/Renewal protocol.
        Vector param= new Vector();
        Vector moduleResult = null;
        param.addElement(new Parameter("PROTO_AMEND_RENEWAL_NUMBER",
                DBEngineConstants.TYPE_STRING, amendRenewalProtocol));
        if(dbEngine !=null){
            moduleResult = new Vector(3,2);
            moduleResult = dbEngine.executeRequest("Coeus",
                    "call GET_PROTO_AMEND_RENEW_MODULES( <<PROTO_AMEND_RENEWAL_NUMBER>>, "+
                    "<<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        //HashMap hmProtocolModule = null;
        Vector vecModuleData =  new Vector();//to avoid null pointers
        if (moduleResult!=null && moduleResult.size() > 0 && amendRenewalProtocol != null){
            HashMap moduleRow = null;
            ProtocolModuleBean protocolModuleBean = null;
            //hmProtocolModule = new HashMap();
//            vecModuleData = new Vector();
            for(int count=0 ; count < moduleResult.size() ; count++){
                moduleRow = (HashMap)moduleResult.elementAt(count);
                protocolModuleBean = new ProtocolModuleBean();
                protocolModuleBean.setProtocolNumber((String)
                moduleRow.get("PROTOCOL_NUMBER"));
                protocolModuleBean.setProtocolAmendRenewalNumber((String)
                moduleRow.get("PROTO_AMEND_RENEWAL_NUMBER"));
                protocolModuleBean.setProtocolModuleCode((String)
                moduleRow.get("PROTOCOL_MODULE_CODE"));
                protocolModuleBean.setUpdateTimestamp(
                        (Timestamp)moduleRow.get("UPDATE_TIMESTAMP"));
                protocolModuleBean.setUpdateUser((String)
                moduleRow.get("UPDATE_USER"));
                vecModuleData.add(protocolModuleBean);
                //hmProtocolModule.put(moduleRow.get("PROTOCOL_MODULE_CODE"), amendRenewalProtocol);
            }
        }
        return vecModuleData;
    }
    
    /**
     * Added for CoeusQA2313: Completion of Questionnaire for Submission
     * To get the selected questionnaires from the parent protocol for this amendment/renewal
     * @param amendRenewalProtocol
     * @throws edu.mit.coeus.exception.CoeusException
     * @throws edu.mit.coeus.utils.dbengine.DBException
     * @return Vector
     */
    public Vector getAmendRenewQuestionnaires(String amendRenewalProtocol)
    throws CoeusException, DBException {
        Vector param= new Vector();
        Vector moduleResult = null;
        param.addElement(new Parameter("MODULE_CODE",
                DBEngineConstants.TYPE_INT, ModuleConstants.PROTOCOL_MODULE_CODE));
        param.addElement(new Parameter("PROTO_AMEND_RENEWAL_NUMBER",
                DBEngineConstants.TYPE_STRING, amendRenewalProtocol));
        if(dbEngine !=null){
            moduleResult = new Vector(3,2);
            moduleResult = dbEngine.executeRequest("Coeus",
                    "call GET_AMEND_RENEW_QUESTIONNAIRE( <<MODULE_CODE>>, <<PROTO_AMEND_RENEWAL_NUMBER>>, "+
                    "<<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        Vector vecModuleData =  new Vector();//to avoid null pointers
        if (moduleResult!=null && moduleResult.size() > 0 && amendRenewalProtocol != null){
            HashMap moduleRow = null;
            ProtocolModuleBean protocolModuleBean = null;
            //hmProtocolModule = new HashMap();
            for(int count=0 ; count < moduleResult.size() ; count++){
                moduleRow = (HashMap)moduleResult.elementAt(count);
                protocolModuleBean = new ProtocolModuleBean();
                protocolModuleBean.setProtocolNumber((String)
                moduleRow.get("PROTOCOL_NUMBER"));
                protocolModuleBean.setProtocolAmendRenewalNumber((String)
                moduleRow.get("PROTO_AMEND_RENEWAL_NUMBER"));
                protocolModuleBean.setProtocolModuleCode(
                moduleRow.get("QUESTIONNAIRE_ID").toString());
                protocolModuleBean.setUpdateTimestamp(
                        (Timestamp)moduleRow.get("UPDATE_TIMESTAMP"));
                protocolModuleBean.setUpdateUser((String)
                moduleRow.get("UPDATE_USER"));
                vecModuleData.add(protocolModuleBean);
            }
        }
        return vecModuleData;
    }
    
    /**
     * Added for coeus4.3 enhancement
     * To get the editable modules list for whole protocol
     * @throws edu.mit.coeus.utils.dbengine.DBException 
     * @return Vector
     */
    public Vector getAmendRenewEditableModules() throws DBException{
        Vector result = new Vector(3,2);
        Vector vecModules = null;
        HashMap hmDocTypes = null;
        Vector param= new Vector();
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call GET_PROTOCOL_MODULES ( <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new DBException("DB instance is not available");
        }
        int ctypesCount = result.size();
        if (result!=null && result.size() >0){
            vecModules = new Vector();
            HashMap hmModule = new HashMap();
            for(int types=0;types<ctypesCount;types++){
                hmDocTypes = (HashMap)result.elementAt(types);
                hmModule.put(hmDocTypes.get("PROTOCOL_MODULE_CODE"),
                    hmDocTypes.get("DESCRIPTION"));
            }
            vecModules.add(hmModule);
        }
        return vecModules;
    }
    
    //Added for case #1961 end 4
    //Case 1787 End
    
  /*  public void sendEmailNotification(Vector toID)
    {
   
     String toString="";
     if( toID != null && toID.size()>0)
     {
     for(int i=0;i<toID.size();i++)
     {
   
      toString+=toID.get(i);
      toString+=";";
   
   
     }
     try{
     SetMailAttributes mailAttributes = new SetMailAttributes();
     //System.out.println(toString);
    // mailAttributes.setTo(toID.elementAt(0).toString());
     mailAttributes.setTo("suhanak@nous.soft.net");
     mailAttributes.setSubject("fehueh");
     //mailAttributes.setFrom("suhanak@nous.soft.net");
     UserMaintDataTxnBean userMaint = new UserMaintDataTxnBean();
     String fromId = userMaint.getUserEMail(userId);
     mailAttributes.setFrom("surekhan@nous.soft.net");
     //mailAttributes.setFrom(fromId);
     mailAttributes.setMessage("Test");
     mailAttributes.setSend("Y");
     ReadJavaMailProperties readJavaMailProperties = new ReadJavaMailProperties();
     SendJavaMail sendJavaMail = new SendJavaMail(mailAttributes,readJavaMailProperties);
   
     sendJavaMail.sendMessage();
     }
     catch(CoeusException ce)
     {
      ce.toString();
   
     }
     catch(Exception e)
     {
      e.toString();
   
     }
     }
   
   
    }*/
    //CoeusEnhancement case #1799 start
    
    
  /*  public static void main(String[] args){
   
        ProtocolDataTxnBean pot= new ProtocolDataTxnBean();
    try{
   
   
    pot.sendEmailNotification("0402000299","7","fde");
    }
    catch(Exception e)
    {}
    }*/
    
    /*Added for Case# 3018 -create ability to delete pending studies - Start*/
    /** 
     * To check if the protocol can be deleted for the given protocol number
     * Function FN_CAN_DELETE_PROTOCOL is used to check if protocol can be deleted
     * @param String protocolNumber
     * @throws edu.mit.coeus.exception.CoeusException 
     * @throws edu.mit.coeus.utils.dbengine.DBException 
     * @return int canDelete
     * canDelete = 0 - protocol is in one of the 3 required status and is not linked to any module
     * canDelete = 1 - protocol is linked
     * canDelete = 2 - protocol not in either 'Pending in Progress','Amendment in Progress' or 'Renewal in Progress' 
     */
    public int checkCanDeleteProtocol(String protocolNumber)
    throws CoeusException, DBException {
        int canDelete = 0;
        Vector param= new Vector();
        Vector result = new Vector();
        param.add(new Parameter("PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,protocolNumber));
        /* calling stored function */
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{ <<OUT INTEGER ll_Ret>> = "
                    +" call FN_CAN_DELETE_PROTOCOL(<< PROTOCOL_NUMBER >>) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            canDelete = Integer.parseInt(rowParameter.get("ll_Ret").toString());
        }
        return canDelete;
    }
    /*Added for Case# 3018 -create ability to delete pending studies - End*/
    
    /**
     * Added for Case#3070 - Ability to modify funding source
     * To get the Protocol Link details for the given protocol number
     * @param protocolNumber 
     * @throws edu.mit.coeus.utils.dbengine.DBException 
     * @return Vector
     */
    public Vector getProtocolLinksData(String protocolNumber) throws DBException{
        Vector result = new Vector(3,2);
        Vector vecProtocolLinks= new Vector();
        HashMap hasSpecialReview=null;
        Vector param= new Vector();
        param.addElement(new Parameter("PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,protocolNumber));
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call GET_IRB_LINKS_FOR_PROTOCOL( <<PROTOCOL_NUMBER>> , "
                    +" <<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new DBException("DB instance is not available");
        }
        if (result != null){
            for(int types=0; types< result.size(); types++){
                hasSpecialReview = (HashMap)result.elementAt(types);
                ProtocolLinkBean protocolLinkBean = new ProtocolLinkBean();
                protocolLinkBean.setProtocolNumber(hasSpecialReview.get("PROTOCOL_NUMBER").toString());
                protocolLinkBean.setSequenceNumber(((BigDecimal)hasSpecialReview.get("SEQUENCE_NUMBER")).intValue());
                protocolLinkBean.setModuleCode(((BigDecimal)hasSpecialReview.get("MODULE_CODE")).intValue());
                protocolLinkBean.setModuleItemKey(hasSpecialReview.get("MODULE_ITEM_KEY").toString());
                protocolLinkBean.setModuleItemSeqNumber(((BigDecimal)hasSpecialReview.get("MODULE_ITEM_SEQUENCE_NUMBER")).intValue());//4398
                protocolLinkBean.setActionType(hasSpecialReview.get("ACTION_TYPE").toString());
                vecProtocolLinks.addElement(protocolLinkBean);
            }
        }
        return vecProtocolLinks;
    }   
   //Added for case 2176 - Risk Level Category - start
    /**
     * Gets all the risk levels from the database
     */
    public Vector getRiskLevels() throws CoeusException, DBException{
        Vector result = new Vector(3,2);
        Vector vecRiskLevels = null;
        Vector param= new Vector();
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call GET_RISK_LEVELS ( <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new DBException("DB instance is not available");
        }
        
        int rowCount = result.size();
        if (result!=null && result.size() >0){
            vecRiskLevels = new Vector();
            ComboBoxBean comboBoxBean = null;
            HashMap hmRow = null;
            for(int row = 0; row <rowCount; row++){
                hmRow = (HashMap)result.elementAt(row);
                comboBoxBean = new ComboBoxBean();
                comboBoxBean.setCode(hmRow.get("RISK_LEVEL_CODE").toString());
                comboBoxBean.setDescription((String)hmRow.get("DESCRIPTION"));
                vecRiskLevels.add(comboBoxBean);
            }
        }
        return vecRiskLevels;
    }
    
    /**
     * Gets all the risk levels for the given protocol number
     *
     * @param protocolNumber
     */
    public CoeusVector getProtocolRiskLevels(String protocolNumber) throws CoeusException, DBException{
        Vector result = new Vector(3,2);
        CoeusVector cvProtocolRiskLevels = null;
        Vector param= new Vector();
        
         param.add(new Parameter("PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,protocolNumber));
         
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call GET_PROTOCOL_RISK_LEVELS ( <<PROTOCOL_NUMBER>>, <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new DBException("DB instance is not available");
        }
         
        int rowCount = result.size();
        if (result!=null && result.size() >0){
            cvProtocolRiskLevels = new CoeusVector();
            ProtocolRiskLevelBean protocolRiskLevelBean = null;
            HashMap hmRow = null;
            for(int row = 0; row <rowCount; row++){
                hmRow = (HashMap)result.elementAt(row);
                protocolRiskLevelBean = new ProtocolRiskLevelBean();
                protocolRiskLevelBean.setProtocolNumber((String)hmRow.get("PROTOCOL_NUMBER"));
                protocolRiskLevelBean.setSequenceNumber(
                        Integer.parseInt(hmRow.get("SEQUENCE_NUMBER") == null ?
                            "0" : hmRow.get("SEQUENCE_NUMBER").toString()));
                protocolRiskLevelBean.setComments((String)hmRow.get("COMMENTS"));
                protocolRiskLevelBean.setDateAssigned(hmRow.get("DATE_ASSIGNED") == null ?
                    null:new Date( ((Timestamp)hmRow.get("DATE_ASSIGNED")).getTime()) );
                protocolRiskLevelBean.setDateUpdated(hmRow.get("DATE_UPDATED") == null ?
                    null:new Date( ((Timestamp)hmRow.get("DATE_UPDATED")).getTime()));
                protocolRiskLevelBean.setRiskLevelCode(hmRow.get("RISK_LEVEL_CODE").toString());
                protocolRiskLevelBean.setStatus((String)hmRow.get("STATUS"));
                protocolRiskLevelBean.setUpdateUser((String)hmRow.get("UPDATE_USER"));
                protocolRiskLevelBean.setUpdateTimestamp((Timestamp)hmRow.get("UPDATE_TIMESTAMP"));
                cvProtocolRiskLevels.add(protocolRiskLevelBean);
            }
        }
        return cvProtocolRiskLevels;
    }
    //Added for case 2176 - Risk Level Category - end
    
    /**
     * Check the module item key is alread added for that module
     * @param protocolNumber 
     * @param moduleCode 
     * @param moduleItemKey 
     * @throws edu.mit.coeus.exception.CoeusException 
     * @throws edu.mit.coeus.utils.dbengine.DBException 
     * @return boolean cas add
     */
    public boolean canAddProtocolLinks(String protocolNumber, String moduleCode,
            String moduleItemKey) throws CoeusException,DBException{
        boolean success = false ;
        Vector param= new Vector();
        
        HashMap row = null;
        Vector result = new Vector();
        
        param= new Vector();
        param.add(new Parameter("PROTOCOL_NUMBER", DBEngineConstants.TYPE_STRING, protocolNumber)) ;
        param.add(new Parameter("MODULE_CODE", DBEngineConstants.TYPE_STRING, moduleCode)) ;
        param.add(new Parameter("MODULE_ITEM_KEY", DBEngineConstants.TYPE_STRING, moduleItemKey)) ;
        
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{<<OUT INTEGER SUCCESS>> = call FN_CAN_ADD_PROTOCOL_LINKS( "
                    + " << PROTOCOL_NUMBER >>, << MODULE_CODE >>, << MODULE_ITEM_KEY >> )}", param) ;
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        
        if(!result.isEmpty()){
            row = (HashMap)result.elementAt(0);
            success = Integer.parseInt(row.get("SUCCESS").toString()) == 1 ? true : false;
        }
        return success ;
    }    
    
    //Added for case 3552 - IRB Attachments - start
    /**
     * Get all other attachments for the given protocol
     *
     * @param protocolNumber
     * @return CoeusVector
     */
    public CoeusVector getProtoOtherAttachments(String protocolNumber) 
    throws CoeusException, DBException{
        CoeusVector cvOtherAttachments = new CoeusVector();
        Vector param = new Vector();
        Vector result = null;
        
        param.addElement(new Parameter("PROTOCOL_NUMBER", DBEngineConstants.TYPE_STRING, protocolNumber));
        if(dbEngine != null){
        result = dbEngine.executeRequest("Coeus",
                "call GET_PROTOCOL_OTHER_DOCUMENTS(<<PROTOCOL_NUMBER>>, <<OUT RESULTSET rset>> ) ",
                "Coeus", param);
        }
        if(result != null && result.size() > 0){
            HashMap hmRow = null;
            UploadDocumentBean uploadDocumentBean = null;
            for(int i = 0; i<result.size(); i++){
                hmRow = (HashMap)result.get(i);
                uploadDocumentBean = new UploadDocumentBean();
                uploadDocumentBean.setProtocolNumber((String)hmRow.get("PROTOCOL_NUMBER"));
                uploadDocumentBean.setSequenceNumber(Integer.parseInt(hmRow.get("SEQUENCE_NUMBER").toString()));
                uploadDocumentBean.setDocumentId(Integer.parseInt(hmRow.get("DOCUMENT_ID").toString()));
                uploadDocumentBean.setDescription((String)hmRow.get("DESCRIPTION"));
                uploadDocumentBean.setDocCode(Integer.parseInt(hmRow.get("DOCUMENT_TYPE_CODE").toString()));
                uploadDocumentBean.setDocType(hmRow.get("DOCUMENT_TYPE_DESCRIPTION").toString());
                uploadDocumentBean.setFileName((String)hmRow.get("FILE_NAME"));
                uploadDocumentBean.setUpdateTimestamp((Timestamp)hmRow.get("UPDATE_TIMESTAMP"));
                uploadDocumentBean.setUpdateUser((String)hmRow.get("UPDATE_USER"));
                uploadDocumentBean.setUpdateUserName((String)hmRow.get("UPDATE_USER_NAME"));
                cvOtherAttachments.add(uploadDocumentBean);
            }
        }
        return cvOtherAttachments;
    }
    
    /**
     * Gets the other document for the given protocol number and documentId
     *
     * @param protocolNumber
     * @param documentId
     * @return UploadDocumentBean
     */
    public UploadDocumentBean getProtoOtherAttachment(String protocolNumber, int documentId) 
    throws CoeusException, DBException{
        Vector param = new Vector();
        Vector result = null;
        UploadDocumentBean uploadDocumentBean = null;
        
        param.addElement(new Parameter("PROTOCOL_NUMBER", DBEngineConstants.TYPE_STRING, protocolNumber));
        param.addElement(new Parameter("DOCUMENT_ID", DBEngineConstants.TYPE_INT,
                Integer.toString(documentId)));
        
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT PROTOCOL_NUMBER, SEQUENCE_NUMBER, DOCUMENT_ID, ");
        sql.append(" DOCUMENT_TYPE_CODE, DOCUMENT, DESCRIPTION, FILE_NAME, ");
        sql.append(" UPDATE_TIMESTAMP, UPDATE_USER ");
        sql.append(" FROM OSP$PROTOCOL_OTHER_DOCUMENTS");
        sql.append(" WHERE PROTOCOL_NUMBER = <<PROTOCOL_NUMBER>> ");
        sql.append(" AND DOCUMENT_ID = <<DOCUMENT_ID>> ");
        if(dbEngine != null){
            result = dbEngine.executeRequest("Coeus", sql.toString(), "Coeus",param);
        }
        if(result != null && result.size() > 0){
            HashMap hmRow = null;
            hmRow = (HashMap)result.get(0);
            uploadDocumentBean = new UploadDocumentBean();
            uploadDocumentBean.setProtocolNumber((String)hmRow.get("PROTOCOL_NUMBER"));
            uploadDocumentBean.setSequenceNumber(Integer.parseInt(hmRow.get("SEQUENCE_NUMBER").toString()));
            uploadDocumentBean.setDocumentId(Integer.parseInt(hmRow.get("DOCUMENT_ID").toString()));
            uploadDocumentBean.setDescription((String)hmRow.get("DESCRIPTION"));
            uploadDocumentBean.setFileName((String)hmRow.get("FILE_NAME"));
            uploadDocumentBean.setDocument(convert((ByteArrayOutputStream)hmRow.get("DOCUMENT")));
            uploadDocumentBean.setUpdateTimestamp((Timestamp)hmRow.get("UPDATE_TIMESTAMP"));
            uploadDocumentBean.setUpdateUser((String)hmRow.get("UPDATE_USER"));
        }
        return uploadDocumentBean;
    }
    
    /**
     * Gets the next document id for the protocol other attachments given the 
     * protocol number
     *
     * @param protocolNumber
     * @return int - next document id
     */
    public int getNextProtOtherDocumentId(String protocolNumber)
    throws CoeusException,DBException{
        Vector param= new Vector();
        int documentId = 0;
        HashMap row = null;
        Vector result = new Vector();
        
        param= new Vector();
        param.add(new Parameter("PROTOCOL_NUMBER", DBEngineConstants.TYPE_STRING, protocolNumber)) ;
        
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{<<OUT INTEGER DOCUMENT_ID>> = call FN_GET_PROT_OTHER_DOCUMENT_ID( "
                    + " << PROTOCOL_NUMBER >> )}", param) ;
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        
        if(!result.isEmpty()){
            row = (HashMap)result.elementAt(0);
            documentId = Integer.parseInt(row.get("DOCUMENT_ID").toString());
        }
        return documentId ;
    }  
    /**
     * Gets the other document types for protocol other attachments
     *
     * @return CoeusVector
     */
     public CoeusVector getProtocolOtherDocumetTypes() throws DBException{
        Vector result = new Vector(3,2);
        CoeusVector vecDocTypes = null;
        HashMap hmDocTypes = null;
        Vector param= new Vector();
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call GET_PROTOCOL_DOCUMENT_TYPE ( <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new DBException("DB instance is not available");
        }
        int ctypesCount = result.size();
        if (ctypesCount >0){
            vecDocTypes = new CoeusVector();
            for(int types=0;types<ctypesCount;types++){
                hmDocTypes = (HashMap)result.elementAt(types);
                if(hmDocTypes.get("DOCUMENT_TYPE_GROUP").toString().equals("O")){
                    vecDocTypes.addElement(new ComboBoxBean(
                            hmDocTypes.get("DOCUMENT_TYPE_CODE").toString(),
                            hmDocTypes.get("DESCRIPTION").toString()));
                }
            }
        }
        return vecDocTypes;
    }
     
     //Added for Case# 3087 - In Premium - Review History Add the following elements - Start
     /*
      * Get protocol document records for the give protocol number 
      * and sequences less than or equal to the given sequence
      * @param protocolNumber
      * @param sequenceNumber
      * @return Vector containing all the protocol documents details
      * @throws DBException
      */
     public Vector getProtocolDocuments(String protocolNumber,int sequenceNumber)
     throws DBException{
         
         Vector result = new Vector(3,2);
         Vector vecUploadDoc = null;
         HashMap hmUploadDoc = null;
         Vector param= new Vector();
         param.addElement(new Parameter("PROTOCOL_NUMBER",
                 DBEngineConstants.TYPE_STRING,protocolNumber));
         param.addElement(new Parameter("SEQUENCE_NUMBER",
                 DBEngineConstants.TYPE_INTEGER,new Integer(sequenceNumber)));
         
         if(dbEngine!=null){
             result = dbEngine.executeRequest("Coeus",
                     "call get_document_list ( <<PROTOCOL_NUMBER>>,<<SEQUENCE_NUMBER>> , <<OUT RESULTSET rset>> )","Coeus", param);
         }else{
             throw new DBException("DB instance is not available");
         }
         int ctypesCount = result.size();
         if (ctypesCount >0){
             vecUploadDoc = new Vector();
             for(int types=0;types<ctypesCount;types++){
                 hmUploadDoc = (HashMap)result.elementAt(types);
                 UploadDocumentBean uploadDocumentBean = new UploadDocumentBean();
                 
                 uploadDocumentBean.setProtocolNumber((String)hmUploadDoc.get("PROTOCOL_NUMBER"));
                 uploadDocumentBean.setSequenceNumber(Integer.parseInt(hmUploadDoc.get("SEQUENCE_NUMBER").toString()));
                 uploadDocumentBean.setVersionNumber(Integer.parseInt(hmUploadDoc.get("VERSION_NUMBER").toString()));
                 uploadDocumentBean.setDocCode(Integer.parseInt(hmUploadDoc.get("DOCUMENT_TYPE_CODE").toString()));
                 uploadDocumentBean.setDocType((String)hmUploadDoc.get("DOC_TYPE_DESCRIPTION"));
                 uploadDocumentBean.setDescription(hmUploadDoc.get("DESCRIPTION") == null ? "" : (String)hmUploadDoc.get("DESCRIPTION"));
                 uploadDocumentBean.setFileName((String)hmUploadDoc.get("FILE_NAME"));
                 uploadDocumentBean.setUpdateUser((String)hmUploadDoc.get("UPDATE_USER"));
                 uploadDocumentBean.setDocumentId(Integer.parseInt(hmUploadDoc.get("DOCUMENT_ID").toString()));
                 if(hmUploadDoc.get("UPDATE_USER_NAME") != null) {
                     uploadDocumentBean.setUpdateUserName((String)hmUploadDoc.get("UPDATE_USER_NAME"));
                 } else {
                     uploadDocumentBean.setUpdateUserName((String)hmUploadDoc.get("UPDATE_USER"));
                 }
                 uploadDocumentBean.setUpdateTimestamp((Timestamp)hmUploadDoc.get("UPDATE_TIMESTAMP"));
                 uploadDocumentBean.setStatusCode(Integer.parseInt(hmUploadDoc.get("DOCUMENT_STATUS_CODE").toString()));
                 uploadDocumentBean.setStatusDescription(hmUploadDoc.get("DOC_STATUS_DESCRIPTION").toString());
                 vecUploadDoc.addElement(uploadDocumentBean);
             }
         }
         return vecUploadDoc;
     }
     //Added for Case# 3087 - In Premium - Review History Add the following elements - End
     
    /**
     * Converts ByteArrayOutputStream to array of bytes
     *
     * @param ByteArrayOutputStream
     * @return byte[]
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
    //Added for case 3552 - IRB Attachments - end
   // 2930: Auto-delete Current Locks based on new parameter - Start
    public void setFunctionType(char functionType) {
        this.functionType = functionType;
    }
    // 2930: Auto-delete Current Locks based on new parameter - End
    
        //Protocol Report Watermarking - START
    //JIRA COEUSQA-2350 - START
    public String getProtocolWatermarkText(String protocolNumber, int seqNum, int docType, int version, int docId) throws  DBException{
        String strValue = ""; //COEUSQA-3227
        Vector param= new Vector();
        HashMap hmNextUploadId = null;
        Vector result = new Vector();
        
        param= new Vector();
        
        param.add(new Parameter("PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING, protocolNumber )) ;
        param.add(new Parameter("SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT, seqNum )) ;
        param.add(new Parameter("DOC_TYPE",
                DBEngineConstants.TYPE_INT, docType )) ;
        param.add(new Parameter("VERSION",
                DBEngineConstants.TYPE_INT, version )) ;
        param.add(new Parameter("DOC_ID",
                DBEngineConstants.TYPE_INT, docId )) ;

        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{ <<OUT STRING REPORT_VALUE>> = call FN_GET_PROTOCOL_WATERMARK_TEXT ("
                    +"<< PROTOCOL_NUMBER >>, <<SEQUENCE_NUMBER>>, <<DOC_TYPE>>, <<VERSION>>, <<DOC_ID>> )}",param);
        }else{
            throw new DBException("DB instance is not available");
        }
        if(!result.isEmpty()){
            hmNextUploadId = (HashMap)result.elementAt(0);
            if(hmNextUploadId.get("REPORT_VALUE") != null){//COEUSQA-3227
                strValue = hmNextUploadId.get("REPORT_VALUE").toString();
            }//COEUSQA-3227
        }
        return strValue;
    }
    //JIRA COEUSQA-2350 - END
    //Protocol Report Watermarking - END

    //Added with case  4277:Now that there is New Amendment/Renewal, do not allow changes in an Renewal.
    /**
     * Method to find the protocol type - whether the protocol is parent/amendment/renewal/renewal with amendment
     *@param protocolNumber can be the original protocol number /AmendmentRenewalNumber
     *@return 0 if parent protocol, 1 if amendment, 2 if renewal and 3 if renewal with amendment.
     *
     */
    public int isProtocolRenewalAmendment(String protocolNumber) throws CoeusException,DBException{
       
        int protocolType = 0;
        HashMap row = null;
        Vector result = new Vector();
        
        Vector param= new Vector();
        param.add(new Parameter("PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING, protocolNumber )) ;
        
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{<<OUT INTEGER PROTO_TYPE>> = call fn_is_protocol_renew_amendment( "
                    + " << PROTOCOL_NUMBER >> )}", param) ;
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        
        if(!result.isEmpty()){
            row = (HashMap)result.elementAt(0);
            protocolType = Integer.parseInt(row.get("PROTO_TYPE").toString());
        }
        return protocolType ;
        
    }
    //4277 End

//    New method added with case 4398 - Funding source added directly is lost when an amendment is approved. - Start
    /**
     * Checks whether a particular module is selected in any amendment/renewal for the protocol
     *
     * @param protocolNumber - the original protocol number or amend-renew-number
     * @param moduleCode - the protocol module to be checked.
     *
     * @return boolean true if module is selected in amendment/renewal or false if not selected.
     */
    public boolean isModuleAddedinAmendmentRenewal(String protocolNumber,String moduleCode) throws CoeusException, DBException{
        
        boolean available = false;
        Vector vctAmendRenewals = getAmendRenewEditableData(protocolNumber);
        if(vctAmendRenewals!=null && !vctAmendRenewals.isEmpty()){
            ProtocolModuleBean protocolModuleBean = null;
            for(int i=0; i<vctAmendRenewals.size(); i++ ){
                protocolModuleBean = (ProtocolModuleBean)vctAmendRenewals.elementAt(i);
                if(moduleCode.equals(protocolModuleBean.getProtocolModuleCode())){
                    available = true;
                    break;
                }
            }
        }
        return available;
    }
//    case 4398 - end
    // COEUSDEV-161: Changes to IRB reviewer comments functionality - Access peermissions - Start
    /**
     * This method is used to check if the user is an Investigator of the Protocol.
     * This method calls the DB Function FN_IS_USER_PROTO_INVESTIGATOR.
     * @param String protocolNumber - Protocol Number 
     * @param int sequenceNumber - Sequence Number of the Protocol 
     * @param String userId - user id of the person 
     * @return boolean true if the user is an investigator of the protocol
     * @throws DBException 
     * @throws CoeusException 
     */
    public boolean isUserProtocolInvestigator(String protocolNumber, int sequenceNumber, String userId) throws DBException, CoeusException{
        boolean isInvestigator = false;
        
        HashMap row = null;
        Vector result = new Vector();
        
        Vector param= new Vector();
        param.add(new Parameter("PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING, protocolNumber )) ;
        
        param.add(new Parameter("SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT, sequenceNumber )) ;
        
        param.add(new Parameter("USER_ID",
                DBEngineConstants.TYPE_STRING, userId )) ;
        
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{<<OUT INTEGER IS_INV>> = call FN_IS_USER_PROTO_INVESTIGATOR( "
                    + " << PROTOCOL_NUMBER >> , << SEQUENCE_NUMBER >> , << USER_ID >> )}", param) ;
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        
        if(!result.isEmpty()){
            row = (HashMap)result.elementAt(0);
            int value = Integer.parseInt(row.get("IS_INV").toString());
            if(value > 0){
                isInvestigator = true;
            } else {
                isInvestigator = false;
            }
        }       
        return isInvestigator;
    }
    
    /**
     * This method is used to check if the user is Protocol Reviewer.
     * This method calls the DB Function FN_IS_USER_PROTOCOL_REVIEWER.
     * @param String userId - user id of the person
     * @return boolean true if the user is a Protocol Reviewer.
     * @throws DBException
     * @throws CoeusException
     */
      //Modified for COEUSDEV-303 : Review View menu items are not enabled if the user has reviewer role - start
//    public boolean isUserProtocolReviewer(String userId, String unitNumber) throws DBException, CoeusException{
    public boolean isUserProtocolReviewer(String userId) throws DBException, CoeusException{//COEUSDEV-303 : End
        boolean isReviewer = false;
        
        HashMap row = null;
        Vector result = new Vector();
        
        Vector param= new Vector();
        param.add(new Parameter("USER_ID",
                DBEngineConstants.TYPE_STRING, userId )) ;
        //Commented for COEUSDEV-303 : Review View menu items are not enabled if the user has reviewer role - start
//        param.add(new Parameter("UNIT_NUMBER",
//                DBEngineConstants.TYPE_STRING, unitNumber)) ;
//      //COEUSDEV-303 : END
                
        if(dbEngine!=null){
            //Modified for COEUSDEV-303 : Review View menu items are not enabled if the user has reviewer role - start
//            result = dbEngine.executeFunctions("Coeus",
//                    "{<<OUT INTEGER IS_REVIEWER>> = call FN_IS_USER_PROTOCOL_REVIEWER( "
//                    + " << USER_ID >> , << UNIT_NUMBER >> )}", param) ;
            result = dbEngine.executeFunctions("Coeus",
                    "{<<OUT INTEGER IS_REVIEWER>> = call FN_IS_USER_PROTOCOL_REVIEWER( "
                    + " << USER_ID >> )}", param) ;
            //COEUSDEV-303 : End
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        
        if(!result.isEmpty()){
            row = (HashMap)result.elementAt(0);
            int value = Integer.parseInt(row.get("IS_REVIEWER").toString());
            if(value > 0){
                isReviewer = true;
            } else {
                isReviewer = false;
            }
        }       
        return isReviewer;
    }
    // COEUSDEV-161: Changes to IRB reviewer comments functionality - Access peermissions - End
    
     //Added for COEUSDEV-237 : Investigator cannot see review comments - Start
     /**
     * This method is used to check if the user is present in protocol committee.
     * This method calls the DB Function FN_IS_PERSON_COMM_ACTIVE_MEM.
     * @param String userId - user id of the person
     * @param String committeeId - committeId to which protocol is submitted
     * @return boolean true if the user is a member in committe to which protocol is submitted .
     * @throws DBException
     * @throws CoeusException
     */
    public boolean isPersonActiveCommitteeMember(String personId, String committeeId) throws DBException, CoeusException{
        boolean isProtoCommMember = false;
        
        HashMap row = null;
        Vector result = new Vector();
        
        Vector param= new Vector();
        
        param.add(new Parameter("COMMITTEE_ID",
                DBEngineConstants.TYPE_STRING, committeeId)) ;
        
        param.add(new Parameter("PERSON_ID",
                DBEngineConstants.TYPE_STRING, personId )) ;
        
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{<<OUT INTEGER IS_PROT_COMM_MEMBER>> = call FN_IS_PERSON_COMM_ACTIVE_MEM( "
                    + " << COMMITTEE_ID >> , << PERSON_ID >> )}", param) ;
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        
        if(!result.isEmpty()){
            row = (HashMap)result.elementAt(0);
            int value = Integer.parseInt(row.get("IS_PROT_COMM_MEMBER").toString());
            if(value > 0){
                isProtoCommMember = true;
            } else {
                isProtoCommMember = false;
            }
        }       
        return isProtoCommMember;
    }
    
    
    /**
     * This method is used to check whether protocol reviewer review complete flag is set
     * This method calls the DB Function FN_IS_REVIEWER_REVIEW_COMPLETE.
     * @param String protocolNumber
     * @param String sequenceNumber
     * @param String submissionNumber
     * @param String personId
     * @return boolean true if the user is a member in committe to which protocol is submitted .
     * @throws DBException
     * @throws CoeusException
     */
    public boolean isProtocolReviwerReviewComplete(String protocolNumber,int sequenceNumber,int submissionNumber,String personId) throws DBException, CoeusException{
        boolean isProtocolReviwerReviewComplete = false;
        
        HashMap row = null;
        Vector result = new Vector();
        
        Vector param= new Vector();
        
        param.add(new Parameter("PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,protocolNumber)) ;

        
        param.addElement(new Parameter("SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,new Integer(sequenceNumber).toString()));
        
        param.addElement(new Parameter("SUBMISSION_NUMBER",
                DBEngineConstants.TYPE_INT,new Integer(submissionNumber).toString()));
        
        param.add(new Parameter("PERSON_ID",
                DBEngineConstants.TYPE_STRING, personId )) ;
        
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{<<OUT INTEGER IS_REVIEWER_REVIEW_COMPLETE>> = call FN_IS_REVIEWER_REVIEW_COMPLETE( "
                    + " << PROTOCOL_NUMBER >> ,<< SEQUENCE_NUMBER >>,<< SUBMISSION_NUMBER >>, << PERSON_ID >> )}", param) ;
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        
        if(!result.isEmpty()){
            row = (HashMap)result.elementAt(0);
            int reviewComplete = Integer.parseInt(row.get("IS_REVIEWER_REVIEW_COMPLETE").toString());
            
            if(REVIEW_COMPLETE == reviewComplete ){
                isProtocolReviwerReviewComplete = true;
            } else {
                isProtocolReviwerReviewComplete = false;
            }
        }       
        return isProtocolReviwerReviewComplete;
    }
    // COEUSDEV-237: End
    
    // COEUSDEV-273: Protocol roles update error - new sequence�- Start
    /**
     * This method is used to fetch all the protocol users for a Protocol.
     *
     */
    private Vector getUsersForAllProtocolRoles(String protocolNumber, int sequenceNumber) throws DBException {
        Vector vecUsersForAllRoles = new Vector();
        Vector result = new Vector(3,2);
        ProtocolRoleInfoBean protocolUserRoleInfoBean = null;
        HashMap row = new HashMap();
        Vector param= new Vector();
        param.addElement(new Parameter("PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,protocolNumber));
        param.addElement(new Parameter("SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INTEGER,new Integer(sequenceNumber)));
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call GET_USERS_FOR_ALL_PROTO_ROLES ( <<PROTOCOL_NUMBER>>,<<SEQUENCE_NUMBER>> , <<OUT RESULTSET rset>> )","Coeus", param);
        }else{
            throw new DBException("DB instance is not available");
        }
        int totalUsers = result.size();
        if (totalUsers >0){
            
            for(int index = 0; index < totalUsers; index++){
                
                row = (HashMap)result.elementAt(index);
                protocolUserRoleInfoBean  = new ProtocolRoleInfoBean();
                protocolUserRoleInfoBean.setProtocolNumber((String)row.get("PROTOCOL_NUMBER"));
                protocolUserRoleInfoBean.setSequenceNumber(Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));
                protocolUserRoleInfoBean.setUserId((String)row.get("USER_ID"));
                protocolUserRoleInfoBean.setUpdateUser((String)row.get("UPDATE_USER"));
                protocolUserRoleInfoBean.setUpdateTimestamp((Timestamp)row.get("UPDATE_TIMESTAMP"));
                protocolUserRoleInfoBean.setRoleId(Integer.parseInt(
                        row.get("ROLE_ID") == null ? "0"
                        : row.get("ROLE_ID").toString()));
                protocolUserRoleInfoBean.setRoleName((String)row.get("ROLE_NAME"));
                protocolUserRoleInfoBean.setRoleType(row.get("ROLE_TYPE").toString().charAt(0));
                protocolUserRoleInfoBean.setUnitNumber((String)row.get("UNIT_NUMBER"));
                
                vecUsersForAllRoles.addElement(protocolUserRoleInfoBean);
            }
        }
        return vecUsersForAllRoles;
    }
     // COEUSDEV-273: Protocol roles update error - new sequence�- Start - End
    
    //Added for COEUSDEV-328 : Notify IRB FYI submission only permits the Aggregator to upload one document - Start
    /**
     * Method used to get protocol actions document from OSP$PROTOCOL_SUBMISSION_DOC
     * for a given protocol number and seqnumber.
     * <li>To fetch the data, it uses get_proto_actions_documents.
     * @param protocolNumber
     * @param sequenceNumber
     * @return Vector 
     * @exception DBException if the instance of a dbEngine is null.
     */
    public Vector getProtocolActionsDocuments(String protocolNumber,int sequenceNumber)
    throws DBException,CoeusException{
        Vector result = new Vector();
        Vector param= new Vector();
        HashMap documentActionRow = null;
        ProtocolActionsBean protocolActionsBean = null;
        ProtocolActionDocumentBean protocolActionDocumentBean = null;
        param.addElement(new Parameter("PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,protocolNumber));
        param.addElement(new Parameter("SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,new Integer(sequenceNumber).toString()));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call get_proto_actions_documents ( <<PROTOCOL_NUMBER>> , "
                    +" <<SEQUENCE_NUMBER>> , <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new DBException("DB instance is not available");
        }
        int listSize = result.size();
        Vector actionDocumentList = null;
        if(listSize>0){
            actionDocumentList = new Vector();
            for(int rowIndex=0;rowIndex<listSize;rowIndex++){
                protocolActionsBean = new ProtocolActionsBean();
                protocolActionDocumentBean = new ProtocolActionDocumentBean();
                documentActionRow = (HashMap)result.elementAt(rowIndex);
                protocolActionsBean.setProtocolNumber((String)documentActionRow.get("PROTOCOL_NUMBER"));
                protocolActionsBean.setSequenceNumber(Integer.parseInt(documentActionRow.get(
                        "SEQUENCE_NUMBER") == null ? "0" : documentActionRow.get(
                        "SEQUENCE_NUMBER").toString()));
                protocolActionsBean.setSubmissionNumber(Integer.parseInt(documentActionRow.get(
                        "SUBMISSION_NUMBER") == null ? "0" : documentActionRow.get(
                        "SUBMISSION_NUMBER").toString()));
                
                protocolActionsBean.setUpdateTimestamp((Timestamp)documentActionRow.get("UPDATE_TIMESTAMP"));
                protocolActionsBean.setActionDate(
                        documentActionRow.get("ACTION_DATE") == null ? null
                        :new Date( ((Timestamp) documentActionRow.get(
                        "ACTION_DATE")).getTime()) );
                protocolActionsBean.setActionTypeCode(Integer.parseInt(
                        documentActionRow.get("PROTOCOL_ACTION_TYPE_CODE")
                        == null ? "0" : documentActionRow.get(
                        "PROTOCOL_ACTION_TYPE_CODE").toString()));
                 protocolActionsBean.setActionTypeDescription(
                        (String)documentActionRow.get("ACTION_DESCRIPTION"));
                 protocolActionDocumentBean.setFileName((String)documentActionRow.get("FILE_NAME"));
                 protocolActionDocumentBean.setDocumentId(Integer.parseInt(documentActionRow.get(
                         "DOCUMENT_ID") == null ? "0" : documentActionRow.get(
                         "DOCUMENT_ID").toString()));
                 protocolActionDocumentBean.setDescription(
                         documentActionRow.get("DESCRIPTION") == null ? ""
                         :(String)documentActionRow.get("DESCRIPTION"));
                 protocolActionDocumentBean.setMimeType((String)documentActionRow.get("MIME_TYPE"));
                protocolActionsBean.setProtocolActionDocumentBean(protocolActionDocumentBean);
                //Added for the case#COEUSQA-2353Need to a better way to handle large numbers of protocol attachments-start
                protocolActionsBean.setProtocolActionDocumentDescription(
                        documentActionRow.get("DESCRIPTION") == null ? ""
                         :(String)documentActionRow.get("DESCRIPTION"));
                //Added for the case#COEUSQA-2353Need to a better way to handle large numbers of protocol attachments-end
                actionDocumentList.add(protocolActionsBean);
            }
        }
        return actionDocumentList;
    }
    //COEUSDEV-328 : End
    //COEUSDEV-75:Rework email engine so the email body is picked up from one place
    /* This function fetches all relevant protocol information into a hashmap
     *Calls get_protocol_details_for_mail
     */
    public HashMap getProtocolDetailsForMail(String protocolNumber) throws DBException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap hmReturn = new HashMap();
        param.addElement(new Parameter("AW_PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,protocolNumber));
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call get_protocol_details_for_mail( << AW_PROTOCOL_NUMBER >> , "
                    +" <<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new DBException("DB instance is not available");
        }
        if(!result.isEmpty()){
            hmReturn = (HashMap)result.elementAt(0);
        }
        return hmReturn;
    }
    //Added for COEUSDEV-86 : Questionnaire for a Submission - Start
    /*
     * Method to check lock exists for the protocol
     * @param protocolNumber
     * @param loggedinUser
     * @return lockExists
     */
    public boolean isProtocolLockExists(String protocolNumber,String loggedinUser)
    throws CoeusException, DBException{
        String rowId = rowLockStr+protocolNumber;
        boolean isProtoLockExists = transMon.lockAvailabilityCheck(rowId,loggedinUser);
        return isProtoLockExists;
    }
        public boolean isProtocolRoutingLockExists(String protocolNumber,String loggedinUser)
     throws CoeusException, DBException{
        String rowId = routingLockStr+protocolNumber;
        boolean isProtoLockExists = transMon.lockAvailabilityCheck(rowId,loggedinUser);
        return isProtoLockExists;
    }
    /*
     * Method to get amendment editalb emodules
     * @param protoAmendRenewNumber
     * @return vcEditableModules
     */
    public Vector getProtoAmendRenewEditableModules(String protoAmendRenewNumber) throws DBException{
        Vector result = new Vector();
        Vector vcEditableModules = new Vector();
        Vector param= new Vector();
        param.addElement(new Parameter("AV_PROTO_AMEND_RENEWAL_NUMBER",
                DBEngineConstants.TYPE_STRING,protoAmendRenewNumber));
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call GET_EDIT_PROTO_AMEND_RENEW_MOD( << AV_PROTO_AMEND_RENEWAL_NUMBER >> , "
                    +" <<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new DBException("DB instance is not available");
        }
        if(!result.isEmpty()){
            int listSize = result.size();
            Vector actionDocumentList = null;
            if(listSize>0){
                actionDocumentList = new Vector();
                HashMap editableModules =  null;
                for(int rowIndex=0;rowIndex<listSize;rowIndex++){
                    editableModules = (HashMap)result.elementAt(rowIndex);
                    vcEditableModules.add(editableModules.get("PROTOCOL_MODULE_CODE"));
                }
            }
        }
        return vcEditableModules;
    }
    
      /*
       * Get protocol document records for the give protocol number
       * and sequences less than or equal to the given sequence
       * @param protocolNumber
       * @param sequenceNumber
       * @return Vector containing all the protocol documents details
       * @throws DBException
       */
    public Vector getProtocolDocumentsForTheSeqeuence(String protocolNumber,int sequenceNumber)
    throws DBException{
        
        Vector result = new Vector(3,2);
        Vector vecUploadDoc = null;
        HashMap hmUploadDoc = null;
        Vector param= new Vector();
        param.addElement(new Parameter("PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,protocolNumber));
        param.addElement(new Parameter("SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INTEGER,new Integer(sequenceNumber)));
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call GET_PROTOCOL_DOCUMENTS ( <<PROTOCOL_NUMBER>>,<<SEQUENCE_NUMBER>> , <<OUT RESULTSET rset>> )","Coeus", param);
        }else{
            throw new DBException("DB instance is not available");
        }
        int ctypesCount = result.size();
        if (ctypesCount >0){
            vecUploadDoc = new Vector();
            for(int types=0;types<ctypesCount;types++){
                hmUploadDoc = (HashMap)result.elementAt(types);
                UploadDocumentBean uploadDocumentBean = new UploadDocumentBean();
                
                uploadDocumentBean.setProtocolNumber((String)hmUploadDoc.get("PROTOCOL_NUMBER"));
                uploadDocumentBean.setSequenceNumber(Integer.parseInt(hmUploadDoc.get("SEQUENCE_NUMBER").toString()));
                uploadDocumentBean.setVersionNumber(Integer.parseInt(hmUploadDoc.get("VERSION_NUMBER").toString()));
                uploadDocumentBean.setDocCode(Integer.parseInt(hmUploadDoc.get("DOCUMENT_TYPE_CODE").toString()));
                uploadDocumentBean.setDocType((String)hmUploadDoc.get("DOC_TYPE_DESCRIPTION"));
                uploadDocumentBean.setDescription(hmUploadDoc.get("DESCRIPTION") == null ? "" : (String)hmUploadDoc.get("DESCRIPTION"));
                uploadDocumentBean.setFileName((String)hmUploadDoc.get("FILE_NAME"));
                uploadDocumentBean.setUpdateUser((String)hmUploadDoc.get("UPDATE_USER"));
                uploadDocumentBean.setDocumentId(Integer.parseInt(hmUploadDoc.get("DOCUMENT_ID").toString()));
                if(hmUploadDoc.get("UPDATE_USER_NAME") != null) {
                    uploadDocumentBean.setUpdateUserName((String)hmUploadDoc.get("UPDATE_USER_NAME"));
                } else {
                    uploadDocumentBean.setUpdateUserName((String)hmUploadDoc.get("UPDATE_USER"));
                }
                uploadDocumentBean.setUpdateTimestamp((Timestamp)hmUploadDoc.get("UPDATE_TIMESTAMP"));
                uploadDocumentBean.setStatusCode(Integer.parseInt(hmUploadDoc.get("DOCUMENT_STATUS_CODE").toString()));
                uploadDocumentBean.setStatusDescription(hmUploadDoc.get("DOC_STATUS_DESCRIPTION").toString());
                vecUploadDoc.addElement(uploadDocumentBean);
            }
        }
        return vecUploadDoc;
    }
     
    //Added for COEUSDEV-86 : Questionnaire for a Submission - End
    
     //Added for-COEUSQA-2879 IRB - Correspondents do not auto-populate for protocols created in Premium-Start
    /**
     *  This method used get the Default organization correspondence             
     * @param locationLists - Vector List of arganization ID
     * @return cvIRBCorrForOrg - Vector List of default organization correspondence
     */
    public CoeusVector getIRBCorrespForOrg(Vector organizationID) 
    throws CoeusException, DBException{
        Vector result = new Vector();
        CoeusVector cvIRBCorrForOrg = new CoeusVector();
        HashMap hmIRBOrgCorr=null;        
        if(organizationID !=null && organizationID.size()>0){ 
        for(int index=0;index<organizationID.size();index++){
        Vector param= new Vector();
        param.addElement(new Parameter("AV_ORGANIZATION_ID",
                DBEngineConstants.TYPE_STRING,organizationID.get(index).toString()));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call GET_ORG_CORRESPONDENCE( <<AV_ORGANIZATION_ID>>, "+
                    "<<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new DBException("DB instance is not available");
        }  
        
        int typesCount = result.size();
        if (typesCount >0){
            for(int types=0;types<typesCount;types++){ 
                ProtocolCorrespondentsBean protocolCorrespondentsBean = new ProtocolCorrespondentsBean();
                hmIRBOrgCorr = (HashMap)result.elementAt(types);
                protocolCorrespondentsBean.setPersonId((String) hmIRBOrgCorr.get("PERSON_ID"));
                protocolCorrespondentsBean.setPersonName((String) hmIRBOrgCorr.get("PERSON_NAME"));
                protocolCorrespondentsBean.setNonEmployeeFlag(hmIRBOrgCorr.get("NON_EMPLOYEE_FLAG") == null ? false:
                    (hmIRBOrgCorr.get("NON_EMPLOYEE_FLAG").toString().equalsIgnoreCase("Y") ? true :false));                                                
                protocolCorrespondentsBean.setCorrespondentTypeCode(hmIRBOrgCorr.get("CORRESPONDENT_TYPE_CODE") == null ? 0:
                    Integer.parseInt(hmIRBOrgCorr.get("CORRESPONDENT_TYPE_CODE").toString()));
                protocolCorrespondentsBean.setComments((String) hmIRBOrgCorr.get("COMMENTS")); 
                cvIRBCorrForOrg.add(protocolCorrespondentsBean);
            }
        }
        }
        }                
      return cvIRBCorrForOrg;
    }     
    
    /**
     *  This method used get the Default Unit correspondence        
     * 
     * @param unitLists - Vector List of Unit number
     * @return cvIRBCorrForUnit - Vector List of default Unit correspondence
     */
    public CoeusVector getIRBCorrespForUnit(Vector unitLists) 
    throws CoeusException, DBException{
        Vector result = new Vector();
        CoeusVector cvIRBCorrForUnit = new CoeusVector();
        HashMap hmIRBOrgCorr=null;       
        if(unitLists !=null && unitLists.size()>0){ 
        for(int index=0;index<unitLists.size();index++){
        Vector param= new Vector();
        param.addElement(new Parameter("AV_UNIT_NUMBER",
                DBEngineConstants.TYPE_STRING,unitLists.get(index).toString()));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call GET_UNIT_CORRESPONDENCE( <<AV_UNIT_NUMBER>>, "+
                    "<<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new DBException("DB instance is not available");
        }  
        
        int typesCount = result.size();
        if (typesCount >0){
            for(int types=0;types<typesCount;types++){ 
                ProtocolCorrespondentsBean protocolCorrespondentsBean = new ProtocolCorrespondentsBean();
                hmIRBOrgCorr = (HashMap)result.elementAt(types);
                protocolCorrespondentsBean.setPersonId((String) hmIRBOrgCorr.get("PERSON_ID"));
                protocolCorrespondentsBean.setPersonName((String) hmIRBOrgCorr.get("PERSON_NAME"));
                protocolCorrespondentsBean.setNonEmployeeFlag(hmIRBOrgCorr.get("NON_EMPLOYEE_FLAG") == null ? false:
                    (hmIRBOrgCorr.get("NON_EMPLOYEE_FLAG").toString().equalsIgnoreCase("Y") ? true :false));                                                
                protocolCorrespondentsBean.setCorrespondentTypeCode(hmIRBOrgCorr.get("CORRESPONDENT_TYPE_CODE") == null ? 0:
                    Integer.parseInt(hmIRBOrgCorr.get("CORRESPONDENT_TYPE_CODE").toString()));
                protocolCorrespondentsBean.setComments((String) hmIRBOrgCorr.get("COMMENTS")); 
                cvIRBCorrForUnit.add(protocolCorrespondentsBean);
            }
        }
        }
        }                
      return cvIRBCorrForUnit;
    }     
    
    /**
     *  This method used check is correspondence exists     
     *  @param protocolCorrespondentsBean - Protocol correspondence bean  
     *  @return success - int count of added correspondnece
     */
    public int isIRBProtocolCorrExist(ProtocolCorrespondentsBean protocolCorrespondentsBean) throws CoeusException,DBException{
        int correspopndenceCount = 0 ;         
        Vector param= new Vector();        
        HashMap row = null;
        Vector result = new Vector();        
        param= new Vector();
        param.add(new Parameter("AS_PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING, protocolCorrespondentsBean.getProtocolNumber()));         
        param.add(new Parameter("AS_SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,protocolCorrespondentsBean.getSequenceNumber()));
        param.add(new Parameter("AS_CORRESPONDENT_TYPE_CODE",
                DBEngineConstants.TYPE_INT, protocolCorrespondentsBean.getCorrespondentTypeCode()));
        param.add(new Parameter("AS_PERSON_ID",
                DBEngineConstants.TYPE_STRING, protocolCorrespondentsBean.getPersonId()));        
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{ <<OUT INTEGER SUCCESS>> = "
                    +" call FN_CHECK_PROTO_CORESP_EXIST(<< AS_PROTOCOL_NUMBER >> ,<< AS_SEQUENCE_NUMBER >> ,<<AS_CORRESPONDENT_TYPE_CODE>>, <<AS_PERSON_ID>> ) }", param);             
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        
        if(!result.isEmpty()){
            row = (HashMap)result.elementAt(0);
            correspopndenceCount = Integer.parseInt(row.get("SUCCESS").toString());
        }
        return correspopndenceCount ;
        
    }
    
   //Added for-COEUSQA-2879 IRB - Correspondents do not auto-populate for protocols created in Premium-End
//end class
    
      /**
     * Added for COEUSQAQA-2431: Amendment attachment view error - start
     * To get the selected document for amending if it is deleted 
     * @param protocolNumber
     * @param sequenceNumber
     * @param documentId
     * @throws edu.mit.coeus.exception.CoeusException
     * @throws edu.mit.coeus.utils.dbengine.DBException
     * @return Boolean value
     */
    public boolean isProtocolDocumentAmend(String protocolNumber, int sequenceNumber ,int documentId)
    throws CoeusException, DBException {
        Vector param= new Vector();
        Vector moduleResult = null;
        HashMap hmAmendedDocument = null;
        boolean isAmendable = false;
        int documentStatusCode = 0;
        param.addElement(new Parameter("AV_PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING, protocolNumber));
        param.addElement(new Parameter("AV_SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT, sequenceNumber));
        param.addElement(new Parameter("AV_DOCUMENT_ID",
                DBEngineConstants.TYPE_INT, documentId));
        if(dbEngine !=null){
            moduleResult = new Vector(3,2);
            moduleResult = dbEngine.executeRequest("Coeus",
                    "call GET_AMEND_PROTOCOL_DOCUMENT( <<AV_PROTOCOL_NUMBER>>, <<AV_SEQUENCE_NUMBER>>, <<AV_DOCUMENT_ID>>,"+
                    "<<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        Vector vecModuleData =  new Vector();//to avoid null pointers
        if (moduleResult!=null && moduleResult.size() > 0){            
           hmAmendedDocument  = (HashMap) moduleResult.get(0);
           documentStatusCode = Integer.parseInt(hmAmendedDocument.get("DOCUMENT_STATUS_CODE").toString());           
        }
        if(documentStatusCode == 0 || documentStatusCode == 3){
             isAmendable = true;  
           }
        return isAmendable;
    }
    //COEUSQA-2431: Amendment attachment view error - end

//code added for getting COI disclosure details to displaying DisclosureStatusForm - starts
 public Vector getDisclosureStatusDetails(String protocolNumber)throws CoeusException, DBException{
        Vector protocolPersonsVector= new Vector();
        Vector result = null;
        Vector param= new Vector();
        HashMap hmRow = null;
        param.addElement(new Parameter("PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,protocolNumber));
        if(dbEngine !=null)
        {
                    result = dbEngine.executeRequest("Coeus",
                    "call GET_PROTOCOL_DISCLOSURE_STATUS( <<PROTOCOL_NUMBER>> , "+
                    "<<OUT RESULTSET rset>> )", "Coeus", param);
        }else
        {
            throw new CoeusException("db_exceptionCode.1000");
        }
        int protocolPersonsCount = result.size();
        if(protocolPersonsCount>0)
        {
            protocolPersonsVector=new Vector();
            for(int rowIndex=0;rowIndex<protocolPersonsCount;rowIndex++)
            {
                hmRow = (HashMap)result.elementAt(rowIndex);
                Vector data= new Vector();
                data.add((String)hmRow.get("FULL_NAME"));
//                data.add((String)hmRow.get("UNIT_NAME"));
                data.add((String)hmRow.get("ROLE"));
                if(hmRow.get("DESCRIPTION") !=null)
                {
                    data.add((String)hmRow.get("DESCRIPTION"));
                }
                else
                {
                    data.add("Not Disclosed");
                }
                data.add((String)hmRow.get("TITLE"));
                protocolPersonsVector.add(data);

            }
        }
     return protocolPersonsVector;
    }
 //code added for getting COI disclosure details to displaying DisclosureStatusForm - ends
}

