/*
 * @(#)ProposalNarrativeTxnBean.java 1.0 03/10/03 9:50 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/* PMD check performed, and commented unused imports and
 * variables on 19th Jan 2009 by Sreenath
 */

package edu.mit.coeus.propdev.bean;

import java.io.*;
import java.util.Vector;
import java.util.HashMap;
//import java.util.Hashtable;
//import java.util.Comparator;
//import java.util.TreeSet;
import java.sql.Timestamp;
//import java.sql.Date;
import java.sql.Connection;

import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.utils.dbengine.ProcReqParameter;
import edu.mit.coeus.utils.dbengine.DBEngineConstants;
import edu.mit.coeus.utils.dbengine.TransactionMonitor;
import edu.mit.coeus.rolodexmaint.bean.*;
//import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.locking.LockingBean;

/**
 * This class provides the methods for performing all procedure executions for
 * Proposal Narrative module. Various methods are used to fetch/update Proposal Narrative
 * details.
 * All methods are used <code>DBEngineImpl</code> singleton instance for the
 * database interaction.
 *
 * @version 1.0 on March 24, 2004, 9:50 AM
 * @author  Prasanna Kumar K
 */

public class ProposalNarrativeTxnBean implements TypeConstants{
    // Instance of a dbEngine
    private DBEngineImpl dbEngine;
    // holds the dataset name
    private static final String DSN = "Coeus";
    // holds the userId for the logged in user
    private String userId;
    private TransactionMonitor transMon;
    private Timestamp dbTimestamp;
    // Instance of Connection
    Connection conn = null;
    
    private static final String rowLockStr = "osp$Narrative_";
    
    /** Creates a new instance of ProposalNarrativeTxnBean */
    public ProposalNarrativeTxnBean(String userId){
        this.userId = userId;
        dbEngine = new DBEngineImpl();
        transMon = TransactionMonitor.getInstance();
    }
    
    /** Creates a new instance of ProposalNarrativeTxnBean */
    public ProposalNarrativeTxnBean(){
        dbEngine = new DBEngineImpl();
        transMon = TransactionMonitor.getInstance();
    }
    
    /**
     *  This method used check whether User can View/Modify Narrative Module for the given Proposal number and Module Number
     *  <li>To fetch the data, it uses the function FN_GET_MAX_DP_NOTES_ENT_NUM.
     *
     *  @return int max entry number for the Proposal number
     *  @param String Proposal number
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public boolean canViewNarrativeModule(String userId, String proposalNumber, int moduleNumber)
    throws CoeusException, DBException {
        int hasRight = 0;
        Vector param= new Vector();
        Vector result = new Vector();
        
        param.add(new Parameter("USER_ID",
        DBEngineConstants.TYPE_STRING, userId));
        param.add(new Parameter("PROPOSAL_NUMBER",
        DBEngineConstants.TYPE_STRING, proposalNumber));
        param.add(new Parameter("MODULE_NUMBER",
        DBEngineConstants.TYPE_INT, ""+moduleNumber));
        
        /* calling stored function */
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
            "{ <<OUT INTEGER HAS_RIGHT>> = "
            +" call FN_USER_CAN_VIEW_NARR_MODULE( << USER_ID >>, << PROPOSAL_NUMBER >>, << MODULE_NUMBER >> ) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            hasRight = Integer.parseInt(rowParameter.get("HAS_RIGHT").toString());
        }
        if(hasRight == 1){
            return true;
        }else{
            return false;
        }
    }
    
    //Added with COEUSDEV 308: Application not checking module level rights for a user in Narrative
    /**
     *  This method used to get the access type for a user in a given narrative module
     *  <li>To fetch the data, it uses the function fn_get_narr_user_right_for_mod.
     *
     *  @return userRight - the char representing the access type.
     *  @param String proposalNumber - the Proposal number
     *  @param int moduleNumber - the the narrative module number
     *  @param String userId - the user id
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public char getNarrativeUserRightforModule(String proposalNumber, int moduleNumber,String userId)
    throws CoeusException, DBException {
        char userRight = '\0';
        Vector param= new Vector();
        Vector result = new Vector();
        
        param.add(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING, proposalNumber));
        param.add(new Parameter("MODULE_NUMBER",
                DBEngineConstants.TYPE_INT, new Integer(moduleNumber)));
        param.add(new Parameter("USER_ID",
                DBEngineConstants.TYPE_STRING, userId));
        
        /* calling stored function */
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{ <<OUT STRING USER_RIGHT>> = "
                    +" call fn_get_narr_user_right_for_mod( << PROPOSAL_NUMBER >>, << MODULE_NUMBER >>, << USER_ID >>) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            userRight = rowParameter.get("USER_RIGHT").toString().charAt(0);
        }
        return userRight;
    }
    //COEUSDEV 308: End
    
    
    /**
     *  This method used check whether the given Role has Narrative Rights
     *  <li>To fetch the data, it uses the function FN_ROLE_HAS_RIGHT.
     *
     *  @return boolean indicating whether the given role has Narrative Right
     *  @param int roleId
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public boolean getRoleHasNarrativeRight(int roleId)
    throws CoeusException, DBException {
        int hasRight = 0;
        Vector param= new Vector();
        Vector result = new Vector();
        
        param.add(new Parameter("ROLE_ID",
        DBEngineConstants.TYPE_INT, ""+roleId));
        
        /* calling stored function */
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
            "{ <<OUT INTEGER HAS_RIGHT>> = "
            +" call FN_ROLE_HAS_RIGHT( << ROLE_ID >> ) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            hasRight = Integer.parseInt(rowParameter.get("HAS_RIGHT").toString());
        }
        if(hasRight == 1){
            return true;
        }else{
            return false;
        }
    }
    
    //added for case 2945
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
    /**
     *  The method used to fetch the Narrative PDF's from DB.
     *  Template will be stored in the database and written to the Server hard drive.
     *
     *  @param proposalNarrativePDFSourceBean ProposalNarrativePDFSourceBean
     *  @return byte[] PDF Data
     *
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public ProposalNarrativePDFSourceBean getNarrativePDF(ProposalNarrativePDFSourceBean proposalNarrativePDFSourceBean)
    throws CoeusException, DBException{
        Vector result = new Vector(3, 2);
        Vector parameter = new Vector();
        String selectQuery = "";
        byte[] fileData = null;
        
        parameter.addElement(new Parameter("PROPOSAL_NUMBER",
        DBEngineConstants.TYPE_STRING, proposalNarrativePDFSourceBean.getProposalNumber()));
        parameter.addElement(new Parameter("MODULE_NUMBER",
        DBEngineConstants.TYPE_INT, ""+proposalNarrativePDFSourceBean.getModuleNumber()));
        
        //Modified for case id 3183 - start
//        selectQuery = "SELECT NARRATIVE_PDF,FILE_NAME,UPDATE_USER,UPDATE_TIMESTAMP FROM OSP$NARRATIVE_PDF "+
//        "WHERE PROPOSAL_NUMBER =  <<PROPOSAL_NUMBER>> AND  MODULE_NUMBER =  <<MODULE_NUMBER>>";
        selectQuery = "SELECT NARRATIVE_PDF,FILE_NAME,MIME_TYPE,UPDATE_USER,UPDATE_TIMESTAMP," +
        " FN_GET_USER_NAME(UPDATE_USER) AS UPDATE_USER_NAME FROM OSP$NARRATIVE_PDF "+
        "WHERE PROPOSAL_NUMBER =  <<PROPOSAL_NUMBER>> AND  MODULE_NUMBER =  <<MODULE_NUMBER>>";
        //Modified for case id 3183 - end
        HashMap resultRow = null;
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus", selectQuery, "Coeus",parameter);
            if( !result.isEmpty() ){
                resultRow = (HashMap)result.get(0);
              // proposalNarrativePDFSourceBean.setFileBytes((byte[])resultRow.get("NARRATIVE_PDF"));
                
                proposalNarrativePDFSourceBean.setFileBytes(convert((ByteArrayOutputStream)resultRow.get("NARRATIVE_PDF")));
                proposalNarrativePDFSourceBean.setFileName((String)resultRow.get("FILE_NAME"));
                proposalNarrativePDFSourceBean.setMimeType((String)resultRow.get("MIME_TYPE"));//4007
                proposalNarrativePDFSourceBean.setUpdateUser((String)resultRow.get("UPDATE_USER"));
                proposalNarrativePDFSourceBean.setUpdateTimestamp((Timestamp)resultRow.get("UPDATE_TIMESTAMP"));
                //Added for case id 3183 - start
                proposalNarrativePDFSourceBean.setUpdateUserName((String)resultRow.get("UPDATE_USER_NAME"));
                //Added for case id 3183 - end
                return proposalNarrativePDFSourceBean;
            }
        }else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        return null;
    }
    
    /**
     *  The method used to fetch the Narrative PDF's from DB.
     *  Template will be stored in the database and written to the Server hard drive.
     *
     *  @param proposalNarrativePDFSourceBean ProposalNarrativePDFSourceBean
     *  @return byte[] PDF Data
     *
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public ProposalNarrativePDFSourceBean getNarrativePDF(ProposalNarrativePDFSourceBean proposalNarrativePDFSourceBean, java.sql.Connection conn)
    throws CoeusException, DBException{
        Vector result = new Vector(3, 2);
        Vector parameter = new Vector();
        String selectQuery = "";
        byte[] fileData = null;
        
        parameter.addElement(new Parameter("PROPOSAL_NUMBER",
        DBEngineConstants.TYPE_STRING, proposalNarrativePDFSourceBean.getProposalNumber()));
        parameter.addElement(new Parameter("MODULE_NUMBER",
        DBEngineConstants.TYPE_INT, ""+proposalNarrativePDFSourceBean.getModuleNumber()));
        
        selectQuery = "SELECT NARRATIVE_PDF,FILE_NAME,UPDATE_USER,UPDATE_TIMESTAMP FROM OSP$NARRATIVE_PDF "+
        "WHERE PROPOSAL_NUMBER =  <<PROPOSAL_NUMBER>> AND  MODULE_NUMBER =  <<MODULE_NUMBER>>";
        
        HashMap resultRow = null;
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus", selectQuery, "Coeus",parameter, conn);
            if( !result.isEmpty() ){
                resultRow = (HashMap)result.get(0);
          
                proposalNarrativePDFSourceBean.setFileBytes(convert ((ByteArrayOutputStream) resultRow.get("NARRATIVE_PDF") ) );
             //   proposalNarrativePDFSourceBean.setFileBytes((byte[])resultRow.get("NARRATIVE_PDF"));
                proposalNarrativePDFSourceBean.setFileName((String)resultRow.get("FILE_NAME"));
                proposalNarrativePDFSourceBean.setUpdateUser((String)resultRow.get("UPDATE_USER"));
                proposalNarrativePDFSourceBean.setUpdateTimestamp((Timestamp)resultRow.get("UPDATE_TIMESTAMP"));
                return proposalNarrativePDFSourceBean;
            }
        }else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        return null;
    }
    
    /**
     *  The method used to fetch the Narrative Source from DB.
     *  Template will be stored in the database and written to the Server hard drive.
     *
     *  @param proposalNarrativePDFSourceBean ProposalNarrativePDFSourceBean
     *  @return byte[] PDF Data
     *
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public ProposalNarrativePDFSourceBean getNarrativeSource(ProposalNarrativePDFSourceBean proposalNarrativePDFSourceBean)
    throws CoeusException, DBException{
        Vector result = new Vector(3, 2);
        Vector parameter = new Vector();
        String selectQuery = "";
        byte[] fileData = null;
        
        parameter.addElement(new Parameter("PROPOSAL_NUMBER",
        DBEngineConstants.TYPE_STRING, proposalNarrativePDFSourceBean.getProposalNumber()));
        parameter.addElement(new Parameter("MODULE_NUMBER",
        DBEngineConstants.TYPE_INT, ""+proposalNarrativePDFSourceBean.getModuleNumber()));
        
        selectQuery = "SELECT NARRATIVE_SOURCE, FILE_NAME, INPUT_TYPE, PLATFORM_TYPE, UPDATE_USER, UPDATE_TIMESTAMP FROM OSP$NARRATIVE_SOURCE "+
        "WHERE PROPOSAL_NUMBER =  <<PROPOSAL_NUMBER>> AND  MODULE_NUMBER =  <<MODULE_NUMBER>>";
        
        HashMap resultRow = null;
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus", selectQuery, "Coeus",parameter);
            if( !result.isEmpty() ){
                resultRow = (HashMap)result.get(0);                
           
                proposalNarrativePDFSourceBean.setFileBytes(convert((ByteArrayOutputStream)resultRow.get("NARRATIVE_SOURCE")));
            //  proposalNarrativePDFSourceBean.setFileBytes((byte[])resultRow.get("NARRATIVE_SOURCE"));
                proposalNarrativePDFSourceBean.setFileName((String)resultRow.get("FILE_NAME"));
                proposalNarrativePDFSourceBean.setInputType((String)resultRow.get("INPUT_TYPE"));
                proposalNarrativePDFSourceBean.setPlatFormType((String)resultRow.get("PLATFORM_TYPE"));
                proposalNarrativePDFSourceBean.setUpdateUser((String)resultRow.get("UPDATE_USER"));
                proposalNarrativePDFSourceBean.setUpdateTimestamp((Timestamp)resultRow.get("UPDATE_TIMESTAMP"));
                return proposalNarrativePDFSourceBean;
            }
        }else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        return null;
    }
    
    /**
     *  The method used to fetch the Narrative Source from DB.
     *  Template will be stored in the database and written to the Server hard drive.
     *
     *  @param proposalNarrativePDFSourceBean ProposalNarrativePDFSourceBean
     *  @return byte[] PDF Data
     *
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public ProposalNarrativePDFSourceBean getNarrativeSource(ProposalNarrativePDFSourceBean proposalNarrativePDFSourceBean, java.sql.Connection conn)
    throws CoeusException, DBException{
        Vector result = new Vector(3, 2);
        Vector parameter = new Vector();
        String selectQuery = "";
        byte[] fileData = null;
        
        parameter.addElement(new Parameter("PROPOSAL_NUMBER",
        DBEngineConstants.TYPE_STRING, proposalNarrativePDFSourceBean.getProposalNumber()));
        parameter.addElement(new Parameter("MODULE_NUMBER",
        DBEngineConstants.TYPE_INT, ""+proposalNarrativePDFSourceBean.getModuleNumber()));
        
        selectQuery = "SELECT NARRATIVE_SOURCE, FILE_NAME, INPUT_TYPE, PLATFORM_TYPE, UPDATE_USER, UPDATE_TIMESTAMP FROM OSP$NARRATIVE_SOURCE "+
        "WHERE PROPOSAL_NUMBER =  <<PROPOSAL_NUMBER>> AND  MODULE_NUMBER =  <<MODULE_NUMBER>>";
        
        HashMap resultRow = null;
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus", selectQuery, "Coeus",parameter, conn);
            if( !result.isEmpty() ){
                resultRow = (HashMap)result.get(0);
                proposalNarrativePDFSourceBean.setFileBytes(convert((ByteArrayOutputStream)resultRow.get("NARRATIVE_SOURCE")));
          //    proposalNarrativePDFSourceBean.setFileBytes((byte[])resultRow.get("NARRATIVE_SOURCE"));
                proposalNarrativePDFSourceBean.setFileName((String)resultRow.get("FILE_NAME"));
                proposalNarrativePDFSourceBean.setInputType((String)resultRow.get("INPUT_TYPE"));
                proposalNarrativePDFSourceBean.setPlatFormType((String)resultRow.get("PLATFORM_TYPE"));
                proposalNarrativePDFSourceBean.setUpdateUser((String)resultRow.get("UPDATE_USER"));
                proposalNarrativePDFSourceBean.setUpdateTimestamp((Timestamp)resultRow.get("UPDATE_TIMESTAMP"));
                return proposalNarrativePDFSourceBean;
            }
        }else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        return null;
    }
    
    /**
     * Gets all the proposal narrative Details for proposal number . The
     * return Vector(Collection) from OSP$NARRATIVE_USER_RIGHTS table.
     * <li>To fetch the data, it uses the procedure DW_GET_NARR_USERS_ALL_MOD.
     *
     * @param String proposal Number to get proposal narrative details.
     * @return ProposalNarrativeModuleUsersFormBean attributes of the proposal.
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    //Modified on 19 Feb 2004 as CoeusVector is very handy and this method is not used anywhere
    //public Vector getProposalNarrative(String proposalNumber)
    public CoeusVector getProposalNarrative(String proposalNumber)
    throws CoeusException, DBException{
        Vector result = null;
        Vector param= new Vector();
        ProposalNarrativeModuleUsersFormBean proposalNarrativeModuleUsersFormBean = null;
        HashMap narrativeRow = null;
        param.addElement(new Parameter("PROPOSAL_NUMBER",
        DBEngineConstants.TYPE_STRING,proposalNumber));
        
        if(dbEngine !=null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
            "call DW_GET_NARR_USERS_ALL_MOD(<<PROPOSAL_NUMBER>> , "+
            "<<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        CoeusVector narrativeList = null;
        if (listSize > 0){
            narrativeList = new CoeusVector();
            for(int rowIndex=0; rowIndex<listSize; rowIndex++){
                proposalNarrativeModuleUsersFormBean = new ProposalNarrativeModuleUsersFormBean();
                narrativeRow = (HashMap)result.elementAt(rowIndex);
                
                proposalNarrativeModuleUsersFormBean.setProposalNumber( (String)
                narrativeRow.get("PROPOSAL_NUMBER"));
                proposalNarrativeModuleUsersFormBean.setUserId( (String)
                narrativeRow.get("USER_ID"));
                proposalNarrativeModuleUsersFormBean.setModuleNumber(
                Integer.parseInt(narrativeRow.get("MODULE_NUMBER").toString()));
                proposalNarrativeModuleUsersFormBean.setAccessType(
                narrativeRow.get("ACCESS_TYPE").toString().charAt(0));
                proposalNarrativeModuleUsersFormBean.setUpdateTimestamp(
                (Timestamp)narrativeRow.get("UPDATE_TIMESTAMP"));
                proposalNarrativeModuleUsersFormBean.setUpdateUser( (String)
                narrativeRow.get("UPDATE_USER"));
                
                narrativeList.add(proposalNarrativeModuleUsersFormBean);
            }
        }
        return narrativeList;
    }
    
    /** This method gets unique Modules for the given the Proposal Number . The
     * return is a Vector(Collection) from OSP$NARRATIVE_USER_RIGHTS table.
     * <li>To fetch the data, it uses the procedure GET_NARR_USERS_UNIQ_MOD.
     *
     * @return Vector of Module Names.
     * @param proposalNumber is given as input parameter for the procedure.
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    //Modified on 19 Feb 2004 as CoeusVector is very handy
    //public Vector getProposalNarrativeModules(String proposalNumber)
    public CoeusVector getProposalNarrativeModules(String proposalNumber)
    throws CoeusException, DBException{
        Vector result = null;
        Vector param= new Vector();
        HashMap moduleNumberRow = null;
        param.addElement(new Parameter("PROPOSAL_NUMBER",
        DBEngineConstants.TYPE_STRING,proposalNumber));
        
        if(dbEngine !=null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
            "call GET_NARR_USERS_UNIQ_MOD(<<PROPOSAL_NUMBER>> , "+
            "<<OUT RESULTSET rset>> )", "Coeus", param);
            
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        CoeusVector moduleNumberList = null;
        if (listSize > 0){
            moduleNumberList = new CoeusVector();
            for(int rowIndex=0; rowIndex<listSize; rowIndex++){
                moduleNumberRow = (HashMap)result.elementAt(rowIndex);
                moduleNumberList.add(Integer.valueOf(moduleNumberRow.get("MODULE_NUMBER").toString()));
            }
        }
        return moduleNumberList;
    }
    
    /**
     * Gets all the proposal narrative details for given proposal number.
     * This method locks the Narrative Records for the given Proposal Number
     *
     * @param proposalNumber is used as an input parameter to the procedure.
     * @param functionType is used to identify the Mode
     *
     * @return Vector of Proposal Narratives
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
   /*public Vector getProposalNarrativeDetails(String proposalNumber, char functionType)
            throws CoeusException, DBException{
    
        Vector vctNarrativeDetails = getProposalNarrativeDetails(proposalNumber);
        String rowId = rowLockStr+proposalNumber;
        if ( functionType == MODIFY_MODE ) {
            if(transMon.canEdit(rowId)){
                return vctNarrativeDetails;
            }
            else{
                throw new CoeusException("exceptionCode.999999");
            }
        }
        return vctNarrativeDetails;
    }*/
    
    /**
     * This method locks the Narrative Records for the given Proposal Number
     *
     * @param proposalNumber is used as an input parameter to the procedure.
     *
     * @return boolean indicating whether locking was Success
     * @exception CoeusException if lock was not successfull
     */
    public boolean getNarrativeLock(String proposalNumber)
    throws CoeusException{
        String rowId = rowLockStr+proposalNumber;
        boolean lockSuccess = false;
        if(transMon.canEdit(rowId)){
            lockSuccess = true;
        }else{
            throw new CoeusException("exceptionCode.999999");
        }
        return lockSuccess;
    }

    // 2930: Auto-delete Current Locks based on new parameter - Start
    // Changed the return type of the method
//    // Code added by Shivakumar for locking enhancement - BEGIN
//    public boolean getNarrativeLock(String proposalNumber, String loggedinUser, String unitNumber)
//    throws CoeusException,DBException{
//        String rowId = rowLockStr+proposalNumber;
//        boolean lockSuccess = false;
//        if(dbEngine!=null){
//            conn = dbEngine.beginTxn();
//        }else{
//            throw new CoeusException("db_exceptionCode.1000");
//        }
//        LockingBean lockingeBean  = transMon.canEdit(rowId,loggedinUser, unitNumber,conn);
//        boolean lockCheck = lockingeBean.isGotLock();
//        if(lockCheck){
//            lockSuccess = true;
//        }else{
//            throw new CoeusException("exceptionCode.999999");
//        }
//        return lockSuccess;
//    }
    
    /**
     * This method gets the Lock for the Narrative.
     *
     * @param proposalNumber
     * @param loggedinUser
     * @param unitNumber
     * @return LockingBean which contains inofrmation about Locking
     * @exception CoeusException if Locking is not succesful
     * @exception DBException if any error during database transaction
     */
    
    public LockingBean getNarrativeLock(String proposalNumber, String loggedinUser, String unitNumber)
    throws CoeusException,DBException{
        String rowId = rowLockStr+proposalNumber;
        
        if(dbEngine!=null){
            conn = dbEngine.beginTxn();
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        LockingBean lockingeBean  = transMon.canEdit(rowId,loggedinUser, unitNumber,conn);
        return lockingeBean;
    }
    // 2930: Auto-delete Current Locks based on new parameter - End
    
    // Method to commit transaction
    public void transactionCommit() throws DBException{
        dbEngine.commit(conn);
    }
    
    // Method to rollback transaction
    public void transactionRollback() throws DBException{
        dbEngine.rollback(conn);
    }
    // Method to close the connection
    public void endConnection() throws DBException{
        dbEngine.endTxn(conn);
    }
    // Code added by Shivakumar for locking enhancement - END
    
    /**
     * Gets all the proposal narrative details for given proposal number. The
     * return Vector(Collection) from OSP$NARRATIVE_USER_RIGHTS table.
     * <li>To fetch the data, it uses the procedure GET_PROP_NARR.
     *
     * @param proposalNumber is used as an input parameter to the procedure.
     * @return Vector of ProposalNarrativeModuleUsersFormBeans.
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getProposalNarrativeDetails(String proposalNumber)
    throws CoeusException, DBException{
        Vector result = null;
        Vector param= new Vector();
        ProposalNarrativeFormBean proposalNarrativeFormBean = null;
        HashMap narrativeRow = null;
        param.addElement(new Parameter("PROPOSAL_NUMBER",
        DBEngineConstants.TYPE_STRING,proposalNumber));
        
        if(dbEngine !=null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
            "call GET_PROP_NARR(<<PROPOSAL_NUMBER>> , "+
            "<<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        Vector narrativeList = null;
        if (listSize > 0){
            narrativeList = new Vector(3,2);
            for(int rowIndex=0; rowIndex<listSize; rowIndex++){
                proposalNarrativeFormBean = new ProposalNarrativeFormBean();
                narrativeRow = (HashMap)result.elementAt(rowIndex);
                proposalNarrativeFormBean.setProposalNumber( (String)
                narrativeRow.get("PROPOSAL_NUMBER"));
                proposalNarrativeFormBean.setModuleNumber(Integer.parseInt(narrativeRow.get("MODULE_NUMBER").toString()));
                proposalNarrativeFormBean.setModuleSequenceNumber(Integer.parseInt(narrativeRow.get("MODULE_SEQUENCE_NUMBER").toString()));
                proposalNarrativeFormBean.setModuleTitle( (String)
                narrativeRow.get("MODULE_TITLE"));
                proposalNarrativeFormBean.setModuleStatusCode(
                narrativeRow.get("MODULE_STATUS_CODE").toString().charAt(0));
                proposalNarrativeFormBean.setContactName( (String)
                narrativeRow.get("CONTACT_NAME"));
                proposalNarrativeFormBean.setPhoneNumber( (String)
                narrativeRow.get("PHONE_NUMBER"));
                proposalNarrativeFormBean.setEmailAddress( (String)
                narrativeRow.get("EMAIL_ADDRESS"));
                proposalNarrativeFormBean.setComments( (String)
                narrativeRow.get("COMMENTS"));
                /*added for the Coeus Enhancement case:1624 start step:1*/
                String code = narrativeRow.get("NARRATIVE_TYPE_CODE") == null ? "0" : narrativeRow.get("NARRATIVE_TYPE_CODE").toString();
                proposalNarrativeFormBean.setNarrativeTypeCode(Integer.parseInt(code));
                /*end step1*/
                proposalNarrativeFormBean.setUpdateUser( (String)
                narrativeRow.get("UPDATE_USER"));
                proposalNarrativeFormBean.setUpdateTimestamp( (Timestamp)
                narrativeRow.get("UPDATE_TIMESTAMP"));
                proposalNarrativeFormBean.setHasSourceModuleNumber(narrativeRow.get("SOURCE_MODULE_NUMBER")
                == null ? false : true);
                proposalNarrativeFormBean.setHasPDFModuleNumber(narrativeRow.get("PDF_MODULE_NUMBER")
                == null ? false : true);
                // Added for Proposal Hierarchy Enhancement Case# 3183 - Start
                ProposalNarrativePDFSourceBean propNarrativePDFSourceBean = new 
                    ProposalNarrativePDFSourceBean();
                propNarrativePDFSourceBean.setUpdateTimestamp( (Timestamp)
                narrativeRow.get("PDF_UPDATE_TIMESTAMP"));
                propNarrativePDFSourceBean.setUpdateUserName((String)
                narrativeRow.get("PDF_UPDATE_USER"));
                proposalNarrativeFormBean.setPropNarrativePDFSourceBean(propNarrativePDFSourceBean);
                // Added for Proposal Hierarchy Enhancement Case# 3183 - End
                proposalNarrativeFormBean.setPropModuleUsers(getPropNarrativeModuleUsers(proposalNarrativeFormBean.getProposalNumber(),
                proposalNarrativeFormBean.getModuleNumber()));
                //Added for case#2420 - Upload of files on Edit Module Details Window - start
                if(narrativeRow.get("FILE_NAME") != null){
                    proposalNarrativeFormBean.setFileName(narrativeRow.get("FILE_NAME").toString());                
                }
                //Added for case#2420 - Upload of files on Edit Module Details Window - end                
                narrativeList.add(proposalNarrativeFormBean);
            }
        }
        return narrativeList;
    }
    
    /**
     * Added for the coeus enhancements - Narrative Types case 1624 step2
     * Gets all the proposal narrative types . The
     * return Vector(Collection) from OSP_NARRATIVE_TYPES table.
     * <li>To fetch the data, it uses the procedure GET_NARRATIVE_TYPE.
     * @return Vector of ProposalNarrativeTypeBean.
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getProposalNarrativeTypes( ) throws DBException{
        Vector result = new Vector(3,2);
        Vector vecProposalNarrativeTypes=null;
        ProposalNarrativeTypeBean  proposalNarrativeTypeBean = null;
       
        HashMap hmpProposalNarrativeTypes=null;
        Vector param= new Vector();
               
        if(dbEngine!=null){
            
            result = dbEngine.executeRequest("Coeus",
            "call GET_NARRATIVE_TYPE ( <<OUT RESULTSET rset>> )", 
            "Coeus", param);
        }else{
            throw new DBException("DB instance is not available");
        }
        int typesCount = result.size();
        
        if (typesCount >0){
            vecProposalNarrativeTypes=new Vector(3,2);
            for(int types=0;types<typesCount;types++){
                proposalNarrativeTypeBean = new ProposalNarrativeTypeBean();
                hmpProposalNarrativeTypes = (HashMap)result.elementAt(types);
                proposalNarrativeTypeBean.setNarrativeTypeCode(Integer.parseInt(hmpProposalNarrativeTypes.get("NARRATIVE_TYPE_CODE").toString()));
                proposalNarrativeTypeBean.setDescription((String)hmpProposalNarrativeTypes.get("DESCRIPTION"));
                proposalNarrativeTypeBean.setSystemGenerated((String)hmpProposalNarrativeTypes.get("SYSTEM_GENERATED"));
                proposalNarrativeTypeBean.setAllowMultiple((String)hmpProposalNarrativeTypes.get("ALLOW_MULTIPLE"));
                proposalNarrativeTypeBean.setUpdateTimestamp((Timestamp)hmpProposalNarrativeTypes.get("UPDATE_TIMESTAMP"));
                proposalNarrativeTypeBean.setUpdateUser((String)hmpProposalNarrativeTypes.get("UPDATE_USER"));
                vecProposalNarrativeTypes.add(proposalNarrativeTypeBean);
            }
        }
        return vecProposalNarrativeTypes;
    }
    
     /**
     * Added for the case 2333 - to filter narrative types depending on proposal forms 
     * return Vector(Collection) from OSP_NARRATIVE_TYPES table.
     * <li>To fetch the data, it uses the procedure GET_NARRATIVE_LIST.
     * @param proposalNumber is used an input parameter to the procedure
     * @return Vector of ProposalNarrativeTypeBean.
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getFilteredProposalNarrativeTypes(String proposalNumber ) throws DBException{
        Vector result = new Vector(3,2);
        Vector vecProposalNarrativeTypes=null;
        ProposalNarrativeTypeBean  proposalNarrativeTypeBean = null;
       
        HashMap hmpProposalNarrativeTypes=null;
        Vector param= new Vector();
     
        param.addElement(new Parameter("PROPOSAL_NUMBER",
        DBEngineConstants.TYPE_STRING,proposalNumber));
      
        if(dbEngine!=null){
            
            result = dbEngine.executeRequest("Coeus",
           "call GET_NARRATIVE_LIST (<<PROPOSAL_NUMBER>>, <<OUT RESULTSET rset>> )", 
            "Coeus", param);
        }else{
            throw new DBException("DB instance is not available");
        }
        int typesCount = result.size();
        
        if (typesCount >0){
            vecProposalNarrativeTypes=new Vector(3,2);
            for(int types=0;types<typesCount;types++){
                proposalNarrativeTypeBean = new ProposalNarrativeTypeBean();
                hmpProposalNarrativeTypes = (HashMap)result.elementAt(types);
                proposalNarrativeTypeBean.setNarrativeTypeCode(Integer.parseInt(hmpProposalNarrativeTypes.get("NARRATIVE_TYPE_CODE").toString()));
                proposalNarrativeTypeBean.setDescription((String)hmpProposalNarrativeTypes.get("DESCRIPTION"));
                proposalNarrativeTypeBean.setSystemGenerated((String)hmpProposalNarrativeTypes.get("SYSTEM_GENERATED"));
                proposalNarrativeTypeBean.setAllowMultiple((String)hmpProposalNarrativeTypes.get("ALLOW_MULTIPLE"));
                proposalNarrativeTypeBean.setUpdateTimestamp((Timestamp)hmpProposalNarrativeTypes.get("UPDATE_TIMESTAMP"));
                proposalNarrativeTypeBean.setUpdateUser((String)hmpProposalNarrativeTypes.get("UPDATE_USER"));
                vecProposalNarrativeTypes.add(proposalNarrativeTypeBean);
            }
        }
        return vecProposalNarrativeTypes;
    }
    
    
    /**
     *  The method used to release the lock of a particular Narrative
     *  @param rowId the id for lock to be released
     */
    public void releaseEdit(String rowId){
        transMon.releaseEdit(this.rowLockStr+rowId);
    }
    
    // Code added by Shivakumar for locking enhancement - BEGIN
    public void releaseEdit(String rowId, String userId) throws
    CoeusException,DBException{
        transMon.releaseEdit(this.rowLockStr+rowId,userId);
    }
    // Code added by Shivakumar for locking enhancement - END
    public LockingBean releaseLock(String rowId, String userId) throws
    CoeusException,DBException{
        LockingBean lockingBean = transMon.releaseLock(this.rowLockStr+rowId,userId);
        return lockingBean;
    }
    // Calling releaseLock method to fix the bug in locking
    
    /**
     * Gets all the proposal narrative user details for given proposal number and module number. The
     * return Vector(Collection) from OSP$NARRATIVE_USER_RIGHTS table.
     * <li>To fetch the data, it uses the procedure DW_GET_NARR_MODULEUSERS.
     *
     * @param proposalNumber is used an input parameter to the procedure.
     * @param moduleNumber is used an input parameter to the procedure.
     * @return Vector of ProposalNarrativeModuleUsersFormBean.
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getPropNarrativeModuleUsers(String proposalNumber, int moduleNumber)
    throws CoeusException, DBException{
        Vector result = null;
        Vector param= new Vector();
        ProposalNarrativeModuleUsersFormBean proposalNarrativeModuleUsersFormBean = null;
        HashMap narrativeRow = null;
        param.addElement(new Parameter("PROPOSAL_NUMBER",
        DBEngineConstants.TYPE_STRING,proposalNumber));
        param.addElement(new Parameter("MODULE_NUMBER",
        DBEngineConstants.TYPE_INT,""+moduleNumber));
        if(dbEngine !=null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
            "call DW_GET_NARR_MODULEUSERS(<<PROPOSAL_NUMBER>> , <<MODULE_NUMBER>> , "+
            "<<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        Vector narrativeList = null;
        if (listSize > 0){
            narrativeList = new Vector(3,2);
            for(int rowIndex=0; rowIndex<listSize; rowIndex++){
                proposalNarrativeModuleUsersFormBean = new ProposalNarrativeModuleUsersFormBean();
                narrativeRow = (HashMap)result.elementAt(rowIndex);
                
                proposalNarrativeModuleUsersFormBean.setProposalNumber( (String)
                narrativeRow.get("PROPOSAL_NUMBER"));
                proposalNarrativeModuleUsersFormBean.setModuleNumber(
                Integer.parseInt(narrativeRow.get("MODULE_NUMBER").toString()));
                proposalNarrativeModuleUsersFormBean.setUserId( (String)
                narrativeRow.get("USER_ID"));
                proposalNarrativeModuleUsersFormBean.setModuleNumber(
                Integer.parseInt(narrativeRow.get("MODULE_NUMBER").toString()));
                proposalNarrativeModuleUsersFormBean.setAccessType(
                narrativeRow.get("ACCESS_TYPE").toString().charAt(0));
                proposalNarrativeModuleUsersFormBean.setUpdateTimestamp(
                (Timestamp)narrativeRow.get("UPDATE_TIMESTAMP"));
                proposalNarrativeModuleUsersFormBean.setUpdateUser( (String)
                narrativeRow.get("UPDATE_USER"));
                
                proposalNarrativeModuleUsersFormBean.setUserName( (String)
                narrativeRow.get("USER_NAME").toString());
                
                narrativeList.add(proposalNarrativeModuleUsersFormBean);
            }
        }
        return narrativeList;
    }
    
    /** This method is used to get whether the given proposal number has any Narrative.
     * <li>To fetch the data, it uses the function FN_NARRATIVE_EXISTS.
     *
     * @return int count for the proposal number.
     * @param proposalNumber is given as the input parameter to the function.
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public int getProposalHasNarrative(String proposalNumber)
    throws CoeusException, DBException {
        int count = 0;
        Vector param= new Vector();
        Vector result = new Vector();
        param.add(new Parameter("PROPOSAL_NUMBER",
        DBEngineConstants.TYPE_STRING,proposalNumber));
        /* calling stored function */
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
            "{ <<OUT INTEGER COUNT>> = "
            +" call FN_NARRATIVE_EXISTS(<< PROPOSAL_NUMBER >> ) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            count = Integer.parseInt(rowParameter.get("COUNT").toString());
        }
        return count;
    }
    
    /** This method is used to check whether the given Proposal Number and User Id has rights to Narrative Module.
     * <li>To fetch the data, it uses the function FN_USER_HAS_NARR_MOD_RIGHT.
     *
     * @return int count for the proposal number.
     * @param userId is given as the first input parameter to the function.
     * @param proposalNumber is given as the second input parameter to the function.
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public int getUserHasNarrativeModRights(String userId, String proposalNumber)
    throws CoeusException, DBException {
        int count = 0;
        Vector param= new Vector();
        Vector result = new Vector();
        param.add(new Parameter("USER_ID",
        DBEngineConstants.TYPE_STRING,userId));
        param.add(new Parameter("PROPOSAL_NUMBER",
        DBEngineConstants.TYPE_STRING,proposalNumber));
        /* calling stored function */
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
            "{ <<OUT INTEGER COUNT>> = "
            +" call FN_USER_HAS_NARR_MOD_RIGHT(<< USER_ID >> , << PROPOSAL_NUMBER >> ) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            count = Integer.parseInt(rowParameter.get("COUNT").toString());
        }
        return count;
    }
    
    /**  Method used to update/Delete proposal narrative details of a Proposal screen.
     *  <li>To fetch the data, it uses DW_UPDATE_NARR_USER_RIGHTS procedure.
     *
     * @return boolean true for successful insert/delete
     * @param proposalNarrativeModuleUsersFormBean contains Proposal Narrative details
     * @exception DBException if the instance of a dbEngine is null.
     * @exception CoeusException if the DB instance is not available. */
    public boolean addDeleteProposalNarrative( ProposalNarrativeModuleUsersFormBean
    proposalNarrativeModuleUsersFormBean)  throws CoeusException ,DBException{
        boolean success = false;
        Vector procedures = new Vector(5,3);
        Vector paramNarrative= new Vector();
        Timestamp dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
        //proposalDevelopmentUpdateTxnBean = new ProposalDevelopmentUpdateTxnBean(userId);
        
        paramNarrative.addElement(new Parameter("PROPOSAL_NUMBER",
        DBEngineConstants.TYPE_STRING,
        proposalNarrativeModuleUsersFormBean.getProposalNumber()));
        paramNarrative.addElement(new Parameter("USER_ID",
        DBEngineConstants.TYPE_STRING,
        proposalNarrativeModuleUsersFormBean.getUserId()));
        paramNarrative.addElement(new Parameter("MODULE_NUMBER",
        DBEngineConstants.TYPE_INT,
        ""+proposalNarrativeModuleUsersFormBean.getModuleNumber()) );
        paramNarrative.addElement(new Parameter("ACCESS_TYPE",
        DBEngineConstants.TYPE_STRING,
        new Character(proposalNarrativeModuleUsersFormBean.getAccessType()).toString()));
        paramNarrative.addElement(new Parameter("UPDATE_USER",
        DBEngineConstants.TYPE_STRING, userId));
        paramNarrative.addElement(new Parameter("UPDATE_TIMESTAMP",
        DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        paramNarrative.addElement(new Parameter("AW_UPDATE_USER",
        DBEngineConstants.TYPE_STRING,
        proposalNarrativeModuleUsersFormBean.getUpdateUser()));
        paramNarrative.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
        DBEngineConstants.TYPE_TIMESTAMP,
        proposalNarrativeModuleUsersFormBean.getUpdateTimestamp()));
        paramNarrative.addElement(new Parameter("AC_TYPE",
        DBEngineConstants.TYPE_STRING,
        proposalNarrativeModuleUsersFormBean.getAcType()));
        
        StringBuffer sqlNarrative = new StringBuffer(
        "call DW_UPDATE_NARR_USER_RIGHTS(");
        sqlNarrative.append(" <<PROPOSAL_NUMBER>> , ");
        sqlNarrative.append(" <<MODULE_NUMBER>> , ");
        sqlNarrative.append(" <<USER_ID>> , ");
        sqlNarrative.append(" <<ACCESS_TYPE>> , ");
        sqlNarrative.append(" <<UPDATE_USER>> , ");
        sqlNarrative.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlNarrative.append(" <<AW_UPDATE_USER>> , ");
        sqlNarrative.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sqlNarrative.append(" <<AC_TYPE>> )");
        
        ProcReqParameter procNarrative  = new ProcReqParameter();
        procNarrative.setDSN(DSN);
        procNarrative.setParameterInfo(paramNarrative);
        procNarrative.setSqlCommand(sqlNarrative.toString());
        
        procedures.add(procNarrative);
        
        if(dbEngine!=null){
            dbEngine.executeStoreProcs(procedures);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        success = true;
        return success;
    }
    
    /**
     * This method is used to insert Proposal Narrative PDF Doc into database.
     * @param proposalNarrativePDFSourceBean ProposalNarrativePDFSourceBean
     * @param fileData byte data of the New PDF Template
     *
     * @return boolean true if the insertion is success, else false.
     * @exception CoeusException raised if dbEngine is Null.
     * @exception DBException raised during db transactional error.
     */
    public ProcReqParameter updateProposalNarrativePDF(ProposalNarrativePDFSourceBean proposalNarrativePDFSourceBean,Timestamp timestamp)
    throws CoeusException, DBException {
//        Vector result = new Vector(3, 2);
        Vector parameter = new Vector();
//        boolean isUpdated = false;
       
        byte[] fileData = null;
        ProcReqParameter procNarrative = null;
        
        if(proposalNarrativePDFSourceBean!=null && proposalNarrativePDFSourceBean.getAcType()!=null &&
        !proposalNarrativePDFSourceBean.getAcType().equalsIgnoreCase("D")){
            fileData = proposalNarrativePDFSourceBean.getFileBytes();
            if(fileData!=null){
                String statement = "";
                if(proposalNarrativePDFSourceBean.getAcType().equalsIgnoreCase("I")){
                    parameter.addElement(new Parameter("PROPOSAL_NUMBER",
                    DBEngineConstants.TYPE_STRING, proposalNarrativePDFSourceBean.getProposalNumber()));
                    parameter.addElement(new Parameter("MODULE_NUMBER",
                    DBEngineConstants.TYPE_INT, ""+proposalNarrativePDFSourceBean.getModuleNumber()));
                    parameter.addElement(new Parameter("FILE_NAME",
                    DBEngineConstants.TYPE_STRING, proposalNarrativePDFSourceBean.getFileName()));
                    //Added with case 4007:Attachment based on mime type - Start
                    parameter.addElement(new Parameter("MIME_TYPE",
                    DBEngineConstants.TYPE_STRING, proposalNarrativePDFSourceBean.getMimeType()));
                    //4007 End
                    parameter.addElement(new Parameter("UPDATE_TIMESTAMP",
                    DBEngineConstants.TYPE_TIMESTAMP, timestamp==null?dbTimestamp:timestamp));
                    parameter.addElement(new Parameter("UPDATE_USER",
                    DBEngineConstants.TYPE_STRING, userId));
                    parameter.addElement(new Parameter("NARRATIVE_PDF",
                    DBEngineConstants.TYPE_BLOB, proposalNarrativePDFSourceBean.getFileBytes()));
                    
                    statement = "INSERT INTO OSP$NARRATIVE_PDF (PROPOSAL_NUMBER, MODULE_NUMBER, FILE_NAME, MIME_TYPE , UPDATE_TIMESTAMP, UPDATE_USER, NARRATIVE_PDF) "+
                    " VALUES( <<PROPOSAL_NUMBER>>, <<MODULE_NUMBER>>, <<FILE_NAME>>, <<MIME_TYPE>> ,<<UPDATE_TIMESTAMP>>, <<UPDATE_USER>>, <<NARRATIVE_PDF>>)";
                    
                    //                    String selectQry = "UPDATE OSP$NARRATIVE_PDF SET NARRATIVE_PDF = ? " +
                    //                    " WHERE UPDATE_TIMESTAMP =  ?";
                    
                    procNarrative = new ProcReqParameter();
                    procNarrative.setDSN(DSN);
                    procNarrative.setParameterInfo(parameter);
                    procNarrative.setSqlCommand(statement);
                    
                    //                    if(dbEngine!=null) {
                    //                        boolean status = dbEngine.insertPdfBlob("Coeus", statement,
                    //                        parameter,selectQry,fileData,dbTimestamp);
                    //
                    //                        isUpdated = status ;
                    //                    }
                    //                    else {
                    //                        throw new CoeusException("db_exceptionCode.1000");
                    //                    }
                }
            }
        }
        return procNarrative;
    }
    
    /**
     *  Over loaded method to use timestamp parameter if any
     */
    public ProcReqParameter updateProposalNarrativePDF(ProposalNarrativePDFSourceBean proposalNarrativePDFSourceBean)
    throws CoeusException, DBException {
        return updateProposalNarrativePDF(proposalNarrativePDFSourceBean,null);
    }
    
    /**
     * This method is used to insert Proposal Narrative Source Word Doc into database.
     * @param proposalNarrativePDFSourceBean ProposalNarrativePDFSourceBean
     * @param fileData byte data of the New PDF Template
     *
     * @return boolean true if the insertion is success, else false.
     * @exception CoeusException raised if dbEngine is Null.
     * @exception DBException raised during db transactional error.
     */
    public ProcReqParameter updateProposalNarrativeSource(ProposalNarrativePDFSourceBean proposalNarrativePDFSourceBean)
    throws CoeusException, DBException {
//        Vector result = new Vector(3, 2);
        Vector parameter = new Vector();
//        boolean isUpdated = false;
       
        byte[] fileData = null;
        ProcReqParameter procNarrative = null;
        
        if(proposalNarrativePDFSourceBean!=null && proposalNarrativePDFSourceBean.getAcType()!=null &&
        !proposalNarrativePDFSourceBean.getAcType().equalsIgnoreCase("D")){
            fileData = proposalNarrativePDFSourceBean.getFileBytes();
            if(fileData!=null){
                String statement = "";
                if(proposalNarrativePDFSourceBean.getAcType().equalsIgnoreCase("I")){
                    parameter.addElement(new Parameter("PROPOSAL_NUMBER",
                    DBEngineConstants.TYPE_STRING, proposalNarrativePDFSourceBean.getProposalNumber()));
                    parameter.addElement(new Parameter("MODULE_NUMBER",
                    DBEngineConstants.TYPE_INT, ""+proposalNarrativePDFSourceBean.getModuleNumber()));
                    parameter.addElement(new Parameter("FILE_NAME",
                    DBEngineConstants.TYPE_STRING, proposalNarrativePDFSourceBean.getFileName()));
                    parameter.addElement(new Parameter("INPUT_TYPE",
                    DBEngineConstants.TYPE_STRING, proposalNarrativePDFSourceBean.getInputType()));
                    parameter.addElement(new Parameter("PLATFORM_TYPE",
                    DBEngineConstants.TYPE_STRING, proposalNarrativePDFSourceBean.getPlatFormType()));
                    parameter.addElement(new Parameter("UPDATE_TIMESTAMP",
                    DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));
                    parameter.addElement(new Parameter("UPDATE_USER",
                    DBEngineConstants.TYPE_STRING, userId));
                    parameter.addElement(new Parameter("NARRATIVE_SOURCE",
                    DBEngineConstants.TYPE_BLOB, proposalNarrativePDFSourceBean.getFileBytes()));
                    
                    statement = "INSERT INTO OSP$NARRATIVE_SOURCE (PROPOSAL_NUMBER, MODULE_NUMBER, FILE_NAME, INPUT_TYPE, PLATFORM_TYPE, UPDATE_TIMESTAMP, UPDATE_USER, NARRATIVE_SOURCE ) "+
                    " VALUES( <<PROPOSAL_NUMBER>>, <<MODULE_NUMBER>>, <<FILE_NAME>>, <<INPUT_TYPE>>, <<PLATFORM_TYPE>>, <<UPDATE_TIMESTAMP>>, <<UPDATE_USER>>, <<NARRATIVE_SOURCE>>)";
                    
                    //                    String selectQry = "UPDATE OSP$NARRATIVE_SOURCE SET NARRATIVE_SOURCE = ? " +
                    //                    " WHERE UPDATE_TIMESTAMP =  ?";
                    
                    procNarrative = new ProcReqParameter();
                    procNarrative.setDSN(DSN);
                    procNarrative.setParameterInfo(parameter);
                    procNarrative.setSqlCommand(statement);
                    
                    //                    if(dbEngine!=null) {
                    //                        boolean status = dbEngine.insertPdfBlob("Coeus", statement,
                    //                        parameter,selectQry,fileData,dbTimestamp);
                    //
                    //                        isUpdated = status ;
                    //                    }
                    //                    else {
                    //                        throw new CoeusException("db_exceptionCode.1000");
                    //                    }
                }
            }
        }
        return procNarrative;
    }
    
    /**
     *  Method used to update/delete all Proposal Narrative PDFs.
     *  To update the data, it uses DW_UPD_PROP_APPR_COMMENTS procedure.
     *
     *  @param vctPropMaps Vector of ProposalApprovalMapBean
     *  @return boolean true if updated successfully else false
     *
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public boolean addUpdProposalNarrativePDF(ProposalNarrativePDFSourceBean proposalNarrativePDFSourceBean)
    throws CoeusException, DBException{
        
        Vector param = new Vector();
        Vector procedures = new Vector(5,3);
        Vector vctInsertProcedures = new Vector(3,2);
        boolean isUpdate = false;
        dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
        if(proposalNarrativePDFSourceBean!=null && proposalNarrativePDFSourceBean.getAcType()!=null){
            if(proposalNarrativePDFSourceBean.getAcType().equalsIgnoreCase("I")){
                vctInsertProcedures.add(updateProposalNarrativePDF(proposalNarrativePDFSourceBean));
            }else if(proposalNarrativePDFSourceBean.getAcType().equalsIgnoreCase("U")){
                //Get the data to be Updated
                
                byte[] fileData = proposalNarrativePDFSourceBean.getFileBytes();
                //Added for Bug Fix,updating the filename to the database start 
                String fileName = proposalNarrativePDFSourceBean.getFileName();
                //End updating the filename to the database
                String mimeType = proposalNarrativePDFSourceBean.getMimeType();//4007
                //Get data from server
                //ProposalDevelopmentTxnBean proposalDevelopmentTxnBean = new ProposalDevelopmentTxnBean();
                ProposalNarrativeTxnBean proposalNarrativeTxnBean = new ProposalNarrativeTxnBean();
                proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                
                //If Update delete and Insert
                proposalNarrativePDFSourceBean.setAcType("D");
                
                param = new Vector();
                param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING, proposalNarrativePDFSourceBean.getProposalNumber()));
                param.addElement(new Parameter("MODULE_NUMBER",
                DBEngineConstants.TYPE_INT, ""+proposalNarrativePDFSourceBean.getModuleNumber()));
                param.addElement(new Parameter("FILE_NAME",
                DBEngineConstants.TYPE_STRING, proposalNarrativePDFSourceBean.getFileName()));
                param.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING, proposalNarrativePDFSourceBean.getUpdateUser()));
                param.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING, proposalNarrativePDFSourceBean.getAcType()));
                StringBuffer sql = new StringBuffer(
                "call DW_UPDATE_PROPOSALNARRPDF(");
                sql.append(" <<PROPOSAL_NUMBER>> , ");
                sql.append(" <<MODULE_NUMBER>> , ");
                sql.append(" <<FILE_NAME>> , ");
                sql.append(" <<UPDATE_USER>> , ");
                sql.append(" <<AC_TYPE>> )");
                
                ProcReqParameter procReqParameter  = new ProcReqParameter();
                procReqParameter.setDSN(DSN);
                procReqParameter.setParameterInfo(param);
                procReqParameter.setSqlCommand(sql.toString());
                
                procedures.add(procReqParameter);
                
                //               if(dbEngine!=null){
                //                    dbEngine.executeStoreProcs(procedures);
                //                    isUpdate = true;
                //                }else{
                //                    throw new CoeusException("db_exceptionCode.1000");
                //                }
                //Now insert
                proposalNarrativePDFSourceBean.setAcType("I");
                proposalNarrativePDFSourceBean.setFileBytes(fileData);
                //Added for Bug Fix,updating the filename to the database start 
                proposalNarrativePDFSourceBean.setFileName(fileName);
                //End updating the filename to the database
                proposalNarrativePDFSourceBean.setMimeType(mimeType);//Case 4007
                //isUpdate = updateProposalNarrativePDF(proposalNarrativePDFSourceBean);
                vctInsertProcedures.add(updateProposalNarrativePDF(proposalNarrativePDFSourceBean));
            }
            if(dbEngine!=null){
                java.sql.Connection conn = null;
                try{
                    //Begin a new Transaction
                    conn = dbEngine.beginTxn();
                    //Update other data
                    if(procedures!=null && procedures.size() > 0){
                        dbEngine.executeStoreProcs(procedures, conn);
                    }
                    //Update Bio Source PDF Data
                    if(vctInsertProcedures!=null && vctInsertProcedures.size() > 0){
                        dbEngine.batchSQLUpdate(vctInsertProcedures, conn);
                    }
                    //End Txn
                    dbEngine.endTxn(conn);
                }catch(Exception ex){
                    dbEngine.rollback(conn);
                    throw new DBException(ex);
                }
                isUpdate = true;
            }else{
                throw new CoeusException("db_exceptionCode.1000");
            }
        }
        return isUpdate;
    }
    
    /**
     *  Method used to update/delete all Proposal Narrative Word Docs.
     *  To update the data, it uses DW_UPD_PROP_APPR_COMMENTS procedure.
     *
     *  @param vctPropMaps Vector of ProposalApprovalMapBean
     *  @return boolean true if updated successfully else false
     *
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public boolean addUpdProposalNarrativeSource(ProposalNarrativePDFSourceBean proposalNarrativePDFSourceBean)
    throws CoeusException, DBException{
        
        Vector param = new Vector();
        Vector procedures = new Vector(5,3);
        Vector vctInsertProcedures = new Vector(3,2);
        boolean isUpdate = false;
        dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
        if(proposalNarrativePDFSourceBean!=null && proposalNarrativePDFSourceBean.getAcType()!=null){
            if(proposalNarrativePDFSourceBean.getAcType().equalsIgnoreCase("I")){
                vctInsertProcedures.add(updateProposalNarrativeSource(proposalNarrativePDFSourceBean));
            }else if(proposalNarrativePDFSourceBean.getAcType().equalsIgnoreCase("U")){
                
             
                byte[] fileData = proposalNarrativePDFSourceBean.getFileBytes();
                //Get data from server
                //ProposalDevelopmentTxnBean proposalDevelopmentTxnBean = new ProposalDevelopmentTxnBean();
                ProposalNarrativeTxnBean proposalNarrativeTxnBean = new ProposalNarrativeTxnBean();
                proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativeSource(proposalNarrativePDFSourceBean);
                
                //If Update delete and Insert
                proposalNarrativePDFSourceBean.setAcType("D");
                
                param = new Vector();
                param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING, proposalNarrativePDFSourceBean.getProposalNumber()));
                param.addElement(new Parameter("MODULE_NUMBER",
                DBEngineConstants.TYPE_INT, ""+proposalNarrativePDFSourceBean.getModuleNumber()));
                param.addElement(new Parameter("FILE_NAME",
                DBEngineConstants.TYPE_STRING, proposalNarrativePDFSourceBean.getFileName()));
                param.addElement(new Parameter("INPUT_TYPE",
                DBEngineConstants.TYPE_STRING, proposalNarrativePDFSourceBean.getInputType()));
                param.addElement(new Parameter("PLATFORM_TYPE",
                DBEngineConstants.TYPE_STRING, proposalNarrativePDFSourceBean.getPlatFormType()));
                param.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING, proposalNarrativePDFSourceBean.getUpdateUser()));
                param.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING, proposalNarrativePDFSourceBean.getAcType()));
                StringBuffer sql = new StringBuffer(
                "call DW_UPDATE_PROPOSALNARRSRC(");
                sql.append(" <<PROPOSAL_NUMBER>> , ");
                sql.append(" <<MODULE_NUMBER>> , ");
                sql.append(" <<FILE_NAME>> , ");
                sql.append(" <<INPUT_TYPE>> , ");
                sql.append(" <<PLATFORM_TYPE>> , ");
                sql.append(" <<UPDATE_USER>> , ");
                sql.append(" <<AC_TYPE>> )");
                
                ProcReqParameter procReqParameter  = new ProcReqParameter();
                procReqParameter.setDSN(DSN);
                procReqParameter.setParameterInfo(param);
                procReqParameter.setSqlCommand(sql.toString());
                
                procedures.add(procReqParameter);
                
                proposalNarrativePDFSourceBean.setAcType("I");
                proposalNarrativePDFSourceBean.setFileBytes(fileData);
                //isUpdate = updateProposalNarrativeSource(proposalNarrativePDFSourceBean);
                vctInsertProcedures.add(updateProposalNarrativeSource(proposalNarrativePDFSourceBean));
            }
            if(dbEngine!=null){
                java.sql.Connection conn = null;
                try{
                    //Begin a new Transaction
                    conn = dbEngine.beginTxn();
                    //Update other data
                    if(procedures!=null && procedures.size() > 0){
                        dbEngine.executeStoreProcs(procedures, conn);
                    }
                    //Update Bio Source PDF Data
                    if(vctInsertProcedures!=null && vctInsertProcedures.size() > 0){
                        dbEngine.batchSQLUpdate(vctInsertProcedures, conn);
                    }
                    //End Txn
                    dbEngine.endTxn(conn);
                }catch(Exception ex){
                    dbEngine.rollback(conn);
                    throw new DBException(ex);
                }
                isUpdate = true;
            }else{
                throw new CoeusException("db_exceptionCode.1000");
            }
        }
        return isUpdate;
    }
    
    /**
     * Gets all the proposal narrative PDF details for given proposal number. The
     * return Vector(Collection) from OSP$NARRATIVE_PDF table.
     * <li>To fetch the data, it uses the procedure GET_PROP_NARR_PDF_FOR_PROP.
     *
     * @param proposalNumber is used an input parameter to the procedure.
     *
     * @return Vector of ProposalNarrativeModuleUsersFormBean.
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getPropNarrativePDFForProposal(String proposalNumber)
    throws CoeusException, DBException{
        Vector result = null;
        Vector param= new Vector();
        ProposalNarrativePDFSourceBean proposalNarrativePDFSourceBean = null;
        HashMap narrativeRow = null;
        param.addElement(new Parameter("PROPOSAL_NUMBER",
        DBEngineConstants.TYPE_STRING,proposalNumber));
        if(dbEngine !=null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
            "call GET_PROP_NARR_PDF_FOR_PROP(<<PROPOSAL_NUMBER>>, "+
            "<<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        Vector narrativeList = null;
        if (listSize > 0){
            narrativeList = new Vector(3,2);
            for(int rowIndex=0; rowIndex<listSize; rowIndex++){
                proposalNarrativePDFSourceBean = new ProposalNarrativePDFSourceBean();
                narrativeRow = (HashMap)result.elementAt(rowIndex);
                
                proposalNarrativePDFSourceBean.setProposalNumber( (String)
                narrativeRow.get("PROPOSAL_NUMBER"));
                proposalNarrativePDFSourceBean.setModuleNumber(
                Integer.parseInt(narrativeRow.get("MODULE_NUMBER").toString()));
                proposalNarrativePDFSourceBean.setFileName((String)
                narrativeRow.get("FILE_NAME"));
                proposalNarrativePDFSourceBean.setUpdateTimestamp(
                (Timestamp)narrativeRow.get("UPDATE_TIMESTAMP"));
                proposalNarrativePDFSourceBean.setUpdateUser( (String)
                narrativeRow.get("UPDATE_USER"));
                
                narrativeList.add(proposalNarrativePDFSourceBean);
            }
        }
        return narrativeList;
    }
    
    /**
     * Gets all the proposal narrative PDF details for given proposal number. The
     * return Vector(Collection) from OSP$NARRATIVE_PDF table.
     * <li>To fetch the data, it uses the procedure GET_PROP_NARR_PDF_FOR_PROP.
     *
     * @param proposalNumber is used an input parameter to the procedure.
     *
     * @return Vector of ProposalNarrativeModuleUsersFormBean.
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getPropNarrativePDFForProposal(String proposalNumber, java.sql.Connection conn)
    throws CoeusException, DBException{
        Vector result = null;
        Vector param= new Vector();
        ProposalNarrativePDFSourceBean proposalNarrativePDFSourceBean = null;
        HashMap narrativeRow = null;
        param.addElement(new Parameter("PROPOSAL_NUMBER",
        DBEngineConstants.TYPE_STRING,proposalNumber));
        if(dbEngine !=null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
            "call GET_PROP_NARR_PDF_FOR_PROP(<<PROPOSAL_NUMBER>>, "+
            "<<OUT RESULTSET rset>> )", "Coeus", param, conn);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        Vector narrativeList = null;
        if (listSize > 0){
            narrativeList = new Vector(3,2);
            for(int rowIndex=0; rowIndex<listSize; rowIndex++){
                proposalNarrativePDFSourceBean = new ProposalNarrativePDFSourceBean();
                narrativeRow = (HashMap)result.elementAt(rowIndex);
                
                proposalNarrativePDFSourceBean.setProposalNumber( (String)
                narrativeRow.get("PROPOSAL_NUMBER"));
                proposalNarrativePDFSourceBean.setModuleNumber(
                Integer.parseInt(narrativeRow.get("MODULE_NUMBER").toString()));
                proposalNarrativePDFSourceBean.setFileName((String)
                narrativeRow.get("FILE_NAME"));
                proposalNarrativePDFSourceBean.setUpdateTimestamp(
                (Timestamp)narrativeRow.get("UPDATE_TIMESTAMP"));
                proposalNarrativePDFSourceBean.setUpdateUser( (String)
                narrativeRow.get("UPDATE_USER"));
                
                narrativeList.add(proposalNarrativePDFSourceBean);
            }
        }
        return narrativeList;
    }
    
    /**
     * Gets all the proposal narrative PDF details for given proposal number. The
     * return Vector(Collection) from OSP$NARRATIVE_PDF table.
     * <li>To fetch the data, it uses the procedure GET_PROP_NARR_PDF_FOR_PROP.
     *
     * @param proposalNumber is used an input parameter to the procedure.
     *
     * @return Vector of ProposalNarrativeModuleUsersFormBean.
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getPropNarrativeSourceForProposal(String proposalNumber)
    throws CoeusException, DBException{
        Vector result = null;
        Vector param= new Vector();
        ProposalNarrativePDFSourceBean proposalNarrativePDFSourceBean = null;
        HashMap narrativeRow = null;
        param.addElement(new Parameter("PROPOSAL_NUMBER",
        DBEngineConstants.TYPE_STRING,proposalNumber));
        if(dbEngine !=null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
            "call GET_PROP_NARR_SOURCE_FOR_PROP(<<PROPOSAL_NUMBER>>, "+
            "<<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        Vector narrativeList = null;
        if (listSize > 0){
            narrativeList = new Vector(3,2);
            for(int rowIndex=0; rowIndex<listSize; rowIndex++){
                proposalNarrativePDFSourceBean = new ProposalNarrativePDFSourceBean();
                narrativeRow = (HashMap)result.elementAt(rowIndex);
                
                proposalNarrativePDFSourceBean.setProposalNumber( (String)
                narrativeRow.get("PROPOSAL_NUMBER"));
                proposalNarrativePDFSourceBean.setModuleNumber(
                Integer.parseInt(narrativeRow.get("MODULE_NUMBER").toString()));
                proposalNarrativePDFSourceBean.setFileName((String)
                narrativeRow.get("FILE_NAME"));
                proposalNarrativePDFSourceBean.setInputType((String)
                narrativeRow.get("INPUT_TYPE"));
                proposalNarrativePDFSourceBean.setPlatFormType((String)
                narrativeRow.get("PLATFORM_TYPE"));
                proposalNarrativePDFSourceBean.setUpdateTimestamp(
                (Timestamp)narrativeRow.get("UPDATE_TIMESTAMP"));
                proposalNarrativePDFSourceBean.setUpdateUser( (String)
                narrativeRow.get("UPDATE_USER"));
                
                narrativeList.add(proposalNarrativePDFSourceBean);
            }
        }
        return narrativeList;
    }
    
    /**
     * Gets all the proposal narrative PDF details for given proposal number. The
     * return Vector(Collection) from OSP$NARRATIVE_PDF table.
     * <li>To fetch the data, it uses the procedure GET_PROP_NARR_PDF_FOR_PROP.
     *
     * @param proposalNumber is used an input parameter to the procedure.
     *
     * @return Vector of ProposalNarrativeModuleUsersFormBean.
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getPropNarrativeSourceForProposal(String proposalNumber, java.sql.Connection conn)
    throws CoeusException, DBException{
        Vector result = null;
        Vector param= new Vector();
        ProposalNarrativePDFSourceBean proposalNarrativePDFSourceBean = null;
        HashMap narrativeRow = null;
        param.addElement(new Parameter("PROPOSAL_NUMBER",
        DBEngineConstants.TYPE_STRING,proposalNumber));
        if(dbEngine !=null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
            "call GET_PROP_NARR_SOURCE_FOR_PROP(<<PROPOSAL_NUMBER>>, "+
            "<<OUT RESULTSET rset>> )", "Coeus", param, conn);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        Vector narrativeList = null;
        if (listSize > 0){
            narrativeList = new Vector(3,2);
            for(int rowIndex=0; rowIndex<listSize; rowIndex++){
                proposalNarrativePDFSourceBean = new ProposalNarrativePDFSourceBean();
                narrativeRow = (HashMap)result.elementAt(rowIndex);
                
                proposalNarrativePDFSourceBean.setProposalNumber( (String)
                narrativeRow.get("PROPOSAL_NUMBER"));
                proposalNarrativePDFSourceBean.setModuleNumber(
                Integer.parseInt(narrativeRow.get("MODULE_NUMBER").toString()));
                proposalNarrativePDFSourceBean.setFileName((String)
                narrativeRow.get("FILE_NAME"));
                proposalNarrativePDFSourceBean.setInputType((String)
                narrativeRow.get("INPUT_TYPE"));
                proposalNarrativePDFSourceBean.setPlatFormType((String)
                narrativeRow.get("PLATFORM_TYPE"));
                proposalNarrativePDFSourceBean.setUpdateTimestamp(
                (Timestamp)narrativeRow.get("UPDATE_TIMESTAMP"));
                proposalNarrativePDFSourceBean.setUpdateUser( (String)
                narrativeRow.get("UPDATE_USER"));
                
                narrativeList.add(proposalNarrativePDFSourceBean);
            }
        }
        return narrativeList;
    }
    
    /** This method is used to copy only Blob data of all Proposal Person Biography PDF.
     *
     * @return boolean indicating whether the Updation was success
     * @param proposalPersonBioPDFBean ProposalPersonBioPDFBean
     *
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public ProcReqParameter copyNarrativePDFBlobs(ProposalNarrativePDFSourceBean proposalNarrativePDFSourceBean)
    throws CoeusException, DBException {
//        Vector result = new Vector(3, 2);
        Vector parameter = new Vector();
//        boolean isUpdated = false;
//        Vector procedures = new Vector();
        
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        dbTimestamp = coeusFunctions.getDBTimestamp();
        
        parameter.addElement(new Parameter("PROPOSAL_NUMBER",
        DBEngineConstants.TYPE_STRING, proposalNarrativePDFSourceBean.getProposalNumber()));
        parameter.addElement(new Parameter("MODULE_NUMBER",
        DBEngineConstants.TYPE_INT, ""+proposalNarrativePDFSourceBean.getModuleNumber()));
        //Modified for Case#3183 - Proposal hierarchy - starts        
//        parameter.addElement(new Parameter("UPDATE_TIMESTAMP",
//        DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));
        parameter.addElement(new Parameter("UPDATE_TIMESTAMP",
        DBEngineConstants.TYPE_TIMESTAMP, proposalNarrativePDFSourceBean.getUpdateTimestamp()));        
        //Modified for Case#3183 - Proposal hierarchy - ends
        parameter.addElement(new Parameter("NARRATIVE_PDF",
        DBEngineConstants.TYPE_BLOB, proposalNarrativePDFSourceBean.getFileBytes()));
        
        String statement = "UPDATE OSP$NARRATIVE_PDF SET NARRATIVE_PDF = <<NARRATIVE_PDF>>, UPDATE_TIMESTAMP = <<UPDATE_TIMESTAMP>> "+
        " WHERE PROPOSAL_NUMBER = <<PROPOSAL_NUMBER>> AND MODULE_NUMBER = <<MODULE_NUMBER>>";
        
        //        String selectQry = "UPDATE OSP$NARRATIVE_PDF SET NARRATIVE_PDF = ? " +
        //        " WHERE UPDATE_TIMESTAMP =  ?";
        
        ProcReqParameter procReqParameter  = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(parameter);
        procReqParameter.setSqlCommand(statement);
        
        //        if(dbEngine!=null) {
        //            boolean status = dbEngine.insertPdfBlob("Coeus", statement,
        //            parameter, selectQry, proposalNarrativePDFSourceBean.getFileBytes(),dbTimestamp);
        //
        //            isUpdated = status ;
        //        }else{
        //            throw new CoeusException("db_exceptionCode.1000");
        //        }
        //        return isUpdated;
        return procReqParameter;
    }
    
    /** This method is used to copy only Blob data of all Proposal Person Biography Source.
     *
     * @return boolean indicating whether the Updation was success
     * @param proposalPersonBioSourceBean ProposalPersonBioSourceBean
     *
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public ProcReqParameter copyNarrativeSourceBlobs(ProposalNarrativePDFSourceBean proposalNarrativePDFSourceBean)
    throws CoeusException, DBException {
//        Vector result = new Vector(3, 2);
        Vector parameter = new Vector();
//        boolean isUpdated = false;
//        Vector procedures = new Vector();
        
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        dbTimestamp = coeusFunctions.getDBTimestamp();
        
        parameter.addElement(new Parameter("PROPOSAL_NUMBER",
        DBEngineConstants.TYPE_STRING, proposalNarrativePDFSourceBean.getProposalNumber()));
        parameter.addElement(new Parameter("MODULE_NUMBER",
        DBEngineConstants.TYPE_INT, ""+proposalNarrativePDFSourceBean.getModuleNumber()));
        //Modified for Case#3183 - Proposal hierarchy - starts
//        parameter.addElement(new Parameter("UPDATE_TIMESTAMP",
//        DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));
        parameter.addElement(new Parameter("UPDATE_TIMESTAMP",
        DBEngineConstants.TYPE_TIMESTAMP, proposalNarrativePDFSourceBean.getUpdateTimestamp()));
        //Modified for Case#3183 - Proposal hierarchy - ends
        parameter.addElement(new Parameter("NARRATIVE_SOURCE",
        DBEngineConstants.TYPE_BLOB, proposalNarrativePDFSourceBean.getFileBytes()));
        
        String statement = "UPDATE OSP$NARRATIVE_SOURCE SET NARRATIVE_SOURCE = <<NARRATIVE_SOURCE>>, UPDATE_TIMESTAMP = <<UPDATE_TIMESTAMP>> "+
        " WHERE PROPOSAL_NUMBER = <<PROPOSAL_NUMBER>> AND MODULE_NUMBER = <<MODULE_NUMBER>>";
        
        //        String selectQry = "UPDATE OSP$NARRATIVE_SOURCE SET NARRATIVE_SOURCE = ? " +
        //        " WHERE UPDATE_TIMESTAMP =  ?";
        
        ProcReqParameter procReqParameter  = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(parameter);
        procReqParameter.setSqlCommand(statement);
        
        //        if(dbEngine!=null) {
        //            boolean status = dbEngine.insertPdfBlob("Coeus", statement,
        //            parameter,selectQry,proposalNarrativePDFSourceBean.getFileBytes(),dbTimestamp);
        //
        //            isUpdated = status ;
        //        }else{
        //            throw new CoeusException("db_exceptionCode.1000");
        //        }
        //        return isUpdated;
        return procReqParameter;
    }
    
    /**
     *  Method used to Update Sponsor Ownership
     *  To update the data, it uses fn_build_maps function.
     *
     *  @param proposalNumber Proposal Number
     *  @param narrativeStatus char
     *  @return boolean success
     *
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public boolean updateProposalNarrativeStatus(String proposalNumber, char narrativeStatus)
    throws CoeusException, DBException {
        boolean success = false;
        Vector param= new Vector();
//        Vector result = new Vector();
        Vector procedures = new Vector(5,3);
        
        param.add(new Parameter("PROPOSAL_NUMBER",
        DBEngineConstants.TYPE_STRING, proposalNumber));
        param.add(new Parameter("STATUS",
        DBEngineConstants.TYPE_STRING, ""+narrativeStatus));
        
        StringBuffer sql = new StringBuffer(
        "{ <<OUT INTEGER IS_UPDATE>> = "
        +" call FN_UPD_PROPOSAL_NARR_STATUS(");
        sql.append(" <<PROPOSAL_NUMBER>> , ");
        sql.append(" <<STATUS>> ) }");
        
        ProcReqParameter procReqParameter  = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sql.toString());
        
        procedures.add(procReqParameter);
        
        if(dbEngine!=null){
            dbEngine.executeStoreProcs(procedures);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        success = true;
        return success;
    }
    
    
    
    /**
     *  Method used to Add Narrative attachments for Coeuslite
     *  To update the data, it uses sql insert query
     *  @param ProposalNarrativePDFSourceBean proposalNarrativePDFSourceBean    
     *  @return boolean success
     *
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public boolean addUpdProposalPDF(ProposalNarrativePDFSourceBean proposalNarrativePDFSourceBean)
    throws CoeusException, DBException {
//        Vector result = new Vector(3, 2);
        Vector parameter = new Vector();
        boolean isUpdated = false;
        byte[] fileData = null;
        ProcReqParameter procNarrative = null;
        Vector vctInsertProcedures = new Vector();
        if(proposalNarrativePDFSourceBean!=null && proposalNarrativePDFSourceBean.getAcType()!=null &&
        !proposalNarrativePDFSourceBean.getAcType().equalsIgnoreCase("D")){
            fileData = proposalNarrativePDFSourceBean.getFileBytes();
            if(fileData!=null){
                String statement = "";
                if(proposalNarrativePDFSourceBean.getAcType().equalsIgnoreCase("I")){
                    parameter.addElement(new Parameter("PROPOSAL_NUMBER",
                    DBEngineConstants.TYPE_STRING, proposalNarrativePDFSourceBean.getProposalNumber()));
                    parameter.addElement(new Parameter("MODULE_NUMBER",
                    DBEngineConstants.TYPE_INT, ""+proposalNarrativePDFSourceBean.getModuleNumber()));
                    parameter.addElement(new Parameter("FILE_NAME",
                    DBEngineConstants.TYPE_STRING, proposalNarrativePDFSourceBean.getFileName()));
                    //Mime Type added with case 4007 : Icon based on mime type - Start
                    parameter.addElement(new Parameter("MIME_TYPE",
                    DBEngineConstants.TYPE_STRING, proposalNarrativePDFSourceBean.getMimeType()));
                    //4007 End
                    parameter.addElement(new Parameter("UPDATE_TIMESTAMP",
                    DBEngineConstants.TYPE_TIMESTAMP, proposalNarrativePDFSourceBean.getUpdateTimestamp()));
                    parameter.addElement(new Parameter("UPDATE_USER",
                    DBEngineConstants.TYPE_STRING, userId));
                    parameter.addElement(new Parameter("NARRATIVE_PDF",
                    DBEngineConstants.TYPE_BLOB, proposalNarrativePDFSourceBean.getFileBytes()));
                    
                    statement = "INSERT INTO OSP$NARRATIVE_PDF (PROPOSAL_NUMBER, MODULE_NUMBER, FILE_NAME,MIME_TYPE, UPDATE_TIMESTAMP, UPDATE_USER, NARRATIVE_PDF) "+
                    " VALUES( <<PROPOSAL_NUMBER>>, <<MODULE_NUMBER>>, <<FILE_NAME>>,<<MIME_TYPE>>, <<UPDATE_TIMESTAMP>>, <<UPDATE_USER>>, <<NARRATIVE_PDF>>)";                    
                
                    procNarrative = new ProcReqParameter();
                    procNarrative.setDSN(DSN);
                    procNarrative.setParameterInfo(parameter);
                    procNarrative.setSqlCommand(statement);
                    vctInsertProcedures.add(procNarrative);
                }
            }            
        }
         if(dbEngine!=null){
                java.sql.Connection conn = null;
                try{
                    //Begin a new Transaction
                    conn = dbEngine.beginTxn();
                    
                    //Update PDF Data
                    if(vctInsertProcedures!=null && vctInsertProcedures.size() > 0){
                        dbEngine.batchSQLUpdate(vctInsertProcedures, conn);
                    }
                    //End Txn
                    dbEngine.endTxn(conn);
                }catch(Exception ex){
                    dbEngine.rollback(conn);
                    throw new DBException(ex);
                }
                isUpdated = true;
            }else{
                throw new CoeusException("db_exceptionCode.1000");
            }
        return isUpdated;
    }
    
    public boolean uploadNarrativeBlob(ProposalNarrativePDFSourceBean proposalNarrativePDFSourceBean)
    throws CoeusException, DBException {
        Vector parameter = new Vector();
        boolean isUpdated = false;
//        Vector procedures = new Vector();
        Vector vctInsertProcedures = new Vector();
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        dbTimestamp = coeusFunctions.getDBTimestamp();
        
        
        parameter.addElement(new Parameter("PROPOSAL_NUMBER",
        DBEngineConstants.TYPE_STRING, proposalNarrativePDFSourceBean.getProposalNumber()));
        parameter.addElement(new Parameter("MODULE_NUMBER",
        DBEngineConstants.TYPE_INT, ""+proposalNarrativePDFSourceBean.getModuleNumber()));
        parameter.addElement(new Parameter("FILE_NAME",
        DBEngineConstants.TYPE_STRING, proposalNarrativePDFSourceBean.getFileName()));
        //Mime Type added with case 4007 : Icon based on mime type - Start
        parameter.addElement(new Parameter("MIME_TYPE",
                DBEngineConstants.TYPE_STRING, proposalNarrativePDFSourceBean.getMimeType()));
        //4007 End
        parameter.addElement(new Parameter("UPDATE_TIMESTAMP",
        DBEngineConstants.TYPE_TIMESTAMP, proposalNarrativePDFSourceBean.getUpdateTimestamp()));
        parameter.addElement(new Parameter("UPDATE_USER",
        DBEngineConstants.TYPE_STRING, userId));
        parameter.addElement(new Parameter("NARRATIVE_PDF",
        DBEngineConstants.TYPE_BLOB, proposalNarrativePDFSourceBean.getFileBytes()));
        
        String statement = "UPDATE OSP$NARRATIVE_PDF SET NARRATIVE_PDF = <<NARRATIVE_PDF>>, FILE_NAME = <<FILE_NAME>>, "+ 
            " MIME_TYPE = <<MIME_TYPE>>, UPDATE_TIMESTAMP = <<UPDATE_TIMESTAMP>>, UPDATE_USER = <<UPDATE_USER>> "+
             "WHERE PROPOSAL_NUMBER = <<PROPOSAL_NUMBER>> AND MODULE_NUMBER = <<MODULE_NUMBER>>";
        
        //        String selectQry = "UPDATE OSP$NARRATIVE_PDF SET NARRATIVE_PDF = ? " +
        //        " WHERE UPDATE_TIMESTAMP =  ?";
        
        ProcReqParameter procReqParameter  = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(parameter);
        procReqParameter.setSqlCommand(statement);
        vctInsertProcedures.add(procReqParameter);
        
         if(dbEngine!=null){
                java.sql.Connection conn = null;
                try{
                    //Begin a new Transaction
                    conn = dbEngine.beginTxn();
                    
                    //Update PDF Data
                    if(vctInsertProcedures!=null && vctInsertProcedures.size() > 0){
                        dbEngine.batchSQLUpdate(vctInsertProcedures, conn);
                    }
                    //End Txn
                    dbEngine.endTxn(conn);
                }catch(Exception ex){
                    dbEngine.rollback(conn);
                    throw new DBException(ex);
                }
                isUpdated = true;
            }else{
                throw new CoeusException("db_exceptionCode.1000");
            }
        return isUpdated;
    }
    
    //Added for case#2420 - Upload of files on Edit Module Details Window - start
    /**
     * This method checks whether lock for a given narrative exists or not
     * @param proposalNumber String
     * @exception Exception
     * @return boolean isLockPresent
     */
    public boolean hasNarrativeLock(String proposalNumber) throws Exception{
        String rowId = rowLockStr+proposalNumber;
        boolean isLockPresent = false;
        if(!transMon.isLockAvailable(rowId)){
            isLockPresent = true;
        }else{
            isLockPresent = false;
        }
        return isLockPresent;
    }  
    //Added for case#2420 - Upload of files on Edit Module Details Window - end
    /**
     *   Added for Proposal Hierarchy Enhancement Case# 3183 - Start  
     *   Method used to get the Proposal Hierarchy Link data    
     *  @param  proposalNumber   
     *  @return CoeusVector 
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getPropHierLinkData(String proposalNumber)
    throws CoeusException, DBException{
        Vector result = null;
        Vector param= new Vector();
        ProposalHierarchyLinkBean proposalHierarchyLinkBean = null;
        HashMap hmHierLinkRow = null;
        param.addElement(new Parameter("PROPOSAL_NUMBER",
        DBEngineConstants.TYPE_STRING,proposalNumber));
        
        if(dbEngine !=null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
            "call GET_EPS_PROP_HIER_ATT_LINKS(<<PROPOSAL_NUMBER>> , "+
            "<<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        CoeusVector cvHierLinkList = null;
        if (listSize > 0){
            cvHierLinkList = new CoeusVector();
            for(int rowIndex=0; rowIndex<listSize; rowIndex++){
                proposalHierarchyLinkBean = new ProposalHierarchyLinkBean();
                hmHierLinkRow = (HashMap)result.elementAt(rowIndex);
                
                proposalHierarchyLinkBean.setParentProposalNumber( (String)
                hmHierLinkRow.get("PARENT_PROPOSAL_NUMBER"));
                
                proposalHierarchyLinkBean.setParentModuleNumber(
                Integer.parseInt(hmHierLinkRow.get("PARENT_MODULE_NUMBER").toString()));

                proposalHierarchyLinkBean.setChildProposalNumber( (String)
                hmHierLinkRow.get("CHILD_PROPOSAL_NUMBER"));
                
                proposalHierarchyLinkBean.setChildModuleNumber(
                Integer.parseInt(hmHierLinkRow.get("CHILD_MODULE_NUMBER").toString()));
                
                proposalHierarchyLinkBean.setDocumentTypeCode(
                Integer.parseInt(hmHierLinkRow.get("DOCUMENT_TYPE_CODE").toString()));
                
                proposalHierarchyLinkBean.setLinkType((String)
                hmHierLinkRow.get("LINK_TYPE"));
                
                proposalHierarchyLinkBean.setPersonId((String)
                hmHierLinkRow.get("PERSON_ID"));                 
                
                proposalHierarchyLinkBean.setUpdateTimestamp(
                (Timestamp)hmHierLinkRow.get("UPDATE_TIMESTAMP"));
                
                proposalHierarchyLinkBean.setUpdateUser( (String)
                hmHierLinkRow.get("UPDATE_USER"));
                
                cvHierLinkList.add(proposalHierarchyLinkBean);
            }
        }
        return cvHierLinkList;
    }  //Added for Proposal Hierarchy Enhancement Case# 3183 - Start
    
    public boolean canAddNarrativeType(ProposalNarrativeFormBean formBean)
    throws CoeusException, DBException {
        int hasRight = 0;
        Vector param= new Vector();
        Vector result = new Vector();
        
        param.add(new Parameter("PROPOSAL_NUMBER",
        DBEngineConstants.TYPE_STRING, formBean.getProposalNumber()));
        param.add(new Parameter("NARRATIVE_TYPE_CODE",
        DBEngineConstants.TYPE_INT, ""+formBean.getNarrativeTypeCode()));
        param.add(new Parameter("PROPOSAL_TYPE",
        DBEngineConstants.TYPE_STRING, formBean.isParentProposal()? "P" : "C"));
        
        /* calling stored function */
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
            "{ <<OUT INTEGER CAN_ADD>> = "
            +" call FN_CAN_ADD_NARR_OF_NARR_TYPE( << PROPOSAL_NUMBER >>, " 
            +"<< NARRATIVE_TYPE_CODE >>, << PROPOSAL_TYPE >> ) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            hasRight = Integer.parseInt(rowParameter.get("CAN_ADD").toString());
        }
        if(hasRight == 1){
            return true;
        }else{
            return false;
        }
    }
    
    //Added for COEUSQA-2697-Cannot upload attach. in Lite to a Placeholder Nar. Type created in Premium-start
     /**
     *  This method is to check whether a narrative exists for a given proposal and module number
     *  <li>To check the existence, it uses the function FN_IS_NARR_MODULE_EXIST</li>
     *  @param String proposalNumber - The proposal number
     *  @param int moduleNumber - The narrative module number
     *  @return boolean true if narrative exists, false if narrative does not exists.
     */
    public boolean isNarrativeExistsForModule(String proposalNumber,int moduleNumber)
    throws CoeusException, DBException {         
        boolean isNarrativeExistsForModule = false;
        Vector param= new Vector();
        Vector result = new Vector();
        param.add(new Parameter("PROPOSAL_NUMBER",
        DBEngineConstants.TYPE_STRING, proposalNumber));
        param.add(new Parameter("MODULE_NUMBER",
        DBEngineConstants.TYPE_INT, ""+moduleNumber));
        
        /* calling stored function */
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
            "{ <<OUT INTEGER PDF_COUNT>> = "
            +" call FN_IS_NARR_MODULE_EXIST(<< PROPOSAL_NUMBER >>, << MODULE_NUMBER >> ) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            int pdfCount = Integer.parseInt(rowParameter.get("PDF_COUNT").toString());
            if(pdfCount > 0){
                isNarrativeExistsForModule = true;
            }            
        }
        return isNarrativeExistsForModule;
    }
    //Added for COEUSQA-2697-Cannot upload attach. in Lite to a Placeholder Nar. Type created in Premium-end 
        
}//end of class
