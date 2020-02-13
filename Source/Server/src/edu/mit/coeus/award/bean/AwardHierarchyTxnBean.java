/*
 * @(#)AwardHierarchyTxnBean.java 1.0 May 26, 2004, 3:33 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.award.bean;

import edu.mit.coeus.utils.KeyConstants;
import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.utils.dbengine.ProcReqParameter;
import edu.mit.coeus.utils.dbengine.DBEngineConstants;
import edu.mit.coeus.utils.dbengine.TransactionMonitor;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.exception.CoeusException;

import edu.mit.coeus.utils.CoeusVector;
import java.util.Map;

import java.util.Vector;
import java.util.Hashtable;
import java.util.HashMap;
import java.sql.Timestamp;
import java.sql.Date;
import java.sql.SQLException;

import edu.mit.coeus.utils.query.*;
import java.sql.Connection;
import edu.mit.coeus.bean.SequenceLogic;
import edu.mit.coeus.bean.IndicatorLogic;
import edu.mit.coeus.bean.CoeusBean;
/**
 * This class provides the methods for performing Award Hierarchy functionality using
 * stored procedures.
 *
 * All methods use <code>DBEngineImpl</code> instance for the
 * database interaction.
 *
 * @version 1.0 May 26, 2004, 3:33 PM
 * @author  Prasanna Kumar K
 */

public class AwardHierarchyTxnBean {
    // instance of a dbEngine
    private DBEngineImpl dbEngine;
    // holds the dataset name
    private static final String DSN = "Coeus";
    
    // holds the userId for the logged in user
    private String userId;    
    
    private Timestamp dbTimestamp;
    
    private char functionType;
    
    /** Creates a new instance of BudgetUpdateTxnBean */
    public AwardHierarchyTxnBean() {
    }
    
    /**
     * Creates new BudgetUpdateTxnBean and initializes userId.
     * @param userId String which the Loggedin userid
     */
    public AwardHierarchyTxnBean(String userId) {
        this.userId = userId;
        dbEngine = new DBEngineImpl();
    }
    
    /**  Method used to Copy Award from the Source Award to Target Award
     *
     * @return ProcReqParameter
     *  
     * @param sourceAwardNumber String
     * @param targetAwardNumber String
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available. 
     */
    public ProcReqParameter copyAward(String sourceAwardNumber,
        String targetAwardNumber)  throws CoeusException, DBException{
        
        Vector param = new Vector();
        Vector result = new Vector();
        
        int isSuccess = 0;
        
        param.addElement(new Parameter("SOURCE_AWARD_NUMBER",
            DBEngineConstants.TYPE_STRING,
            sourceAwardNumber));
        param.addElement(new Parameter("TARGET_AWARD_NUMBER",
            DBEngineConstants.TYPE_STRING,
            targetAwardNumber)); 
        param.addElement(new Parameter("USER_ID",
            DBEngineConstants.TYPE_STRING,
            userId)); 
        
        StringBuffer sql = new StringBuffer(
            "{  <<OUT INTEGER IS_SUCCESS>> = call FN_COPY_AWARD(");
        sql.append(" <<SOURCE_AWARD_NUMBER>> , ");
        sql.append(" <<TARGET_AWARD_NUMBER>> , ");
        sql.append(" <<USER_ID>> ) } ");
        
        ProcReqParameter procReqParameter  = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sql.toString());                

        return procReqParameter;        
    } 
    
    /** Method used to Generate Award Report Required
     *
     * @return ProcReqParameter
     *  
     * @param mitAwardNumber String
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available. 
     */
    public ProcReqParameter generateAwardReportRequired(String mitAwardNumber, String userId)  throws CoeusException, DBException{
        
        Vector param = new Vector();
        Vector result = new Vector();
        
        int isSuccess = 0;
        
        param.addElement(new Parameter("MIT_AWARD_NUMBER",
            DBEngineConstants.TYPE_STRING,
            mitAwardNumber));
        //Added for Case 2269: Money & End Dates Tab/Panel in Awards Module - start
        //Parameter added for the award reporting requirement enhancement 2268
        param.addElement(new Parameter("USER_ID",
            DBEngineConstants.TYPE_STRING,
            userId));
        //Added for Case 2269: Money & End Dates Tab/Panel in Awards Module - end
        StringBuffer sql = new StringBuffer(
            "{  <<OUT INTEGER REP_REQUIRED>> = call FN_GENERATE_AWARD_REP_REQ(");
        sql.append(" <<MIT_AWARD_NUMBER>>, <<USER_ID>> ) } ");
        
        ProcReqParameter procReqParameter  = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sql.toString());                

        return procReqParameter;        
    }    
    
    /** Method used to Insert Sap Feed Details
     *
     * @return ProcReqParameter
     *
     * @param mitAwardNumber String
     * @param sequenceNumber int
     * @param feedType String
     * @param transactionId int
	 * @param userId String -- Added by Jobin for Bug Fix : 1184(inserting the update user as the logged in user)
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available. */
    public ProcReqParameter insertSapFeedDetails(String mitAwardNumber, int sequenceNumber, String feedType, String transactionId,String userId )  throws CoeusException, DBException{
        
        Vector param = new Vector();
        Vector result = new Vector();
        
        int isSuccess = 0;
        
        param.addElement(new Parameter("MIT_AWARD_NUMBER",
            DBEngineConstants.TYPE_STRING,
            mitAwardNumber));        
        param.addElement(new Parameter("SEQUENCE_NUMBER",
            DBEngineConstants.TYPE_INT,
            ""+sequenceNumber));        
        param.addElement(new Parameter("FEED_TYPE",
            DBEngineConstants.TYPE_STRING,
            feedType)); 
        param.addElement(new Parameter("TRANSACTION_ID",
            DBEngineConstants.TYPE_STRING,
            transactionId)); 
        param.addElement(new Parameter("UPDATE_USER",
            DBEngineConstants.TYPE_STRING,
            userId)); 
        
        StringBuffer sql = new StringBuffer(
            "{  <<OUT INTEGER IS_SUCCESS>> = call FN_INSERT_SAP_FEED_DETAILS_NEW(");
        sql.append(" <<MIT_AWARD_NUMBER>> , ");
        sql.append(" <<SEQUENCE_NUMBER>> , ");
        sql.append(" <<FEED_TYPE>> , ");
        sql.append(" <<TRANSACTION_ID>> , <<UPDATE_USER>>) } ");
        
        ProcReqParameter procReqParameter  = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sql.toString());                

        return procReqParameter;        
    }        
    
    // Added with COEUSDEV-563:Award Sync to Parent is not triggering SAP feed for child accounts its touching-Start
    /** Method used to Insert Sap Feed Details for all child awards in the hierarchy on performing a Sync Action.
     * @param mitAwardNumber String
     * @param sequenceNumber int
     * @return ProcReqParameter
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available. */
    public ProcReqParameter insertSapFeedDetailsForChildAwards(String mitAwardNumber, int sequenceNumber )  throws CoeusException, DBException{
        
        Vector param = new Vector();
        Vector result = new Vector();
        
        int isSuccess = 0;
        
        param.addElement(new Parameter("MIT_AWARD_NUMBER",
                DBEngineConstants.TYPE_STRING,
                mitAwardNumber));        
        param.addElement(new Parameter("SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,
                new Integer(sequenceNumber)));
        param.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING,
                userId));
        
        StringBuffer sql = new StringBuffer(
                "{  <<OUT INTEGER IS_SUCCESS>> = call FN_INSERT_SAP_FEED_FOR_SYNC(");
        sql.append(" <<MIT_AWARD_NUMBER>> , ");
        sql.append(" <<SEQUENCE_NUMBER>> , ");
        sql.append("  <<UPDATE_USER>>) }");
        
        ProcReqParameter procReqParameter  = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sql.toString());                

        return procReqParameter;        
    }
    //COEUSDEV-563:Award Sync to Parent is not triggering SAP feed for child accounts its touching - End
    
    /** Method used to copy Award functionality
     *
     * @return String new Parent AwardNumber
     * @param awardCopyBean AwardCopyBean
	 * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available. */
    public String copyAward(AwardCopyBean awardCopyBean) throws CoeusException,DBException{
        String targetAwardNumber = "";
        if(awardCopyBean!=null){
            if(awardCopyBean.getCopyAsNewOrChild().equalsIgnoreCase("N")){
				//-- Added userId field by Jobin for Bug Fix :1184
                targetAwardNumber = copyAwardAsNew(awardCopyBean.getSourceAwardNumber(), awardCopyBean.isCopyDescendents(), awardCopyBean.getDescendents(),userId);
            }else{
				//-- Added userId field by Jobin for Bug Fix :1184
                targetAwardNumber = copyAwardAsChild(awardCopyBean.getSourceAwardNumber(), awardCopyBean.getTargetAwardNumber(), awardCopyBean.isCopyDescendents(), awardCopyBean.getDescendents(),userId);
            }            
        }
        return targetAwardNumber;
    }
    
    /**
     * Method used to copy Award details to a new Award. It creates a new Award for the given Source Award Number
     *
     * @param sourceAwardNumber Source Award Number
     * @param isCopyDescendents boolean indicates whether descendents should be copied    
     * @param descendents CoeusVector containing descendents
     * @param userId String -- Added userId field by Jobin for Bug Fix :1184
     * @return String new Award number
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public String copyAwardAsNew(String sourceAwardNumber, boolean isCopyDescendents, CoeusVector descendents,String userId) throws CoeusException,DBException{
        boolean success = false;
        Connection conn = null;
        String targetAwardNumber = "";
        try{
            Vector vctResult = null;
            
            Vector procedures = new Vector(3,2);
            int sequenceNumber = 1;

            AwardTxnBean awardTxnBean = new AwardTxnBean();
            //Gete next Award Number
            targetAwardNumber = awardTxnBean.getNextAwardNumber();
            //Get Sap Feed Details
            CoeusVector sapFeedDetails = awardTxnBean.getSapFeedDetails(targetAwardNumber);
            //Transaction Id
            String transactionId = null;

            dbTimestamp = (new CoeusFunctions()).getDBTimestamp();

            if(dbEngine==null){
                throw new CoeusException("db_exceptionCode.1000");
            }else{
                CoeusVector sapFeedFiltered = null;
                String feedType = "";
                
                //Start Txn
                conn = dbEngine.beginTxn();        

                //Copy Award
                procedures = new Vector(3,2);
                procedures.addElement(copyAward(sourceAwardNumber, targetAwardNumber));
                procedures.addElement(generateAwardReportRequired(targetAwardNumber, userId));
                procedures.addElement(getCurrentTransactionId());

                vctResult = dbEngine.executeStoreProcs(procedures, conn);

                procedures = new Vector(3,2);

                //Get Current Transaction Id
                if(vctResult!=null){
                    Vector vctTransaction = (Vector)vctResult.elementAt(2);
                    HashMap transactionMap = (HashMap)vctTransaction.elementAt(0);
                    transactionId = (String)transactionMap.get("TRANSACTION_ID");
                }

                if(sapFeedDetails == null || sapFeedDetails.size() == 0){
                    feedType = "N";
                    
                    //Bug Fix:1185 Start
                    procedures.addElement(insertSapFeedDetails(targetAwardNumber, sequenceNumber, feedType, transactionId,userId));
                    //Bug Fix:1185 End
                    
                }else{
                    Equals seqIdEq = new Equals("sequenceNumber", new Integer(sequenceNumber));
                    Equals statusEq = new Equals("feedStatus","P");
                    Equals transIdEq = new Equals("transactionId",transactionId);
                    And sapFeedFirstAnd = new And(seqIdEq, statusEq);
                    And sapFeedSecAnd = new And(sapFeedFirstAnd, transIdEq);

                    sapFeedFiltered = sapFeedDetails.filter(sapFeedSecAnd);
                    if(sapFeedFiltered.size() ==0){
                        Equals statusPEq = new Equals("feedStatus","P");
                        Equals statusFEq = new Equals("feedStatus","F");                
                        Equals feedTypeEq = new Equals("feedType","N");
                        Or statusOr = new Or(statusPEq, statusFEq);
                        sapFeedFirstAnd = new And(statusOr, feedTypeEq);
                        sapFeedFiltered = sapFeedDetails.filter(sapFeedFirstAnd);
                        if(sapFeedFiltered.size() == 0){
                            feedType = "N";
                        }else{
                            feedType = "C";
                        }
                        //Update SAP Feed details -- Added userId field by Jobin for Bug Fix :1184
                        procedures.addElement(insertSapFeedDetails(targetAwardNumber, sequenceNumber, feedType, transactionId,userId));
                    }
                }
                //Update Award Hierarchy Details
                
                AwardHierarchyBean awardHierarchyBean = new AwardHierarchyBean();
                awardHierarchyBean.setParentMitAwardNumber("000000-000");
                awardHierarchyBean.setMitAwardNumber(targetAwardNumber);
                awardHierarchyBean.setRootMitAwardNumber(targetAwardNumber);
                awardHierarchyBean.setAcType("I");
                procedures.addElement(addUpdAwardHierarchy(awardHierarchyBean, dbTimestamp));

                dbEngine.executeStoreProcs(procedures, conn);
                
                if(isCopyDescendents){
					//-- Added userId field by Jobin for Bug Fix :1184
                    copyAllDescendents(targetAwardNumber, targetAwardNumber, descendents, descendents, new Vector(), conn, userId);
                }
                //Commit Txn
                dbEngine.commit(conn);
                success = true;
            }
        }finally{
            dbEngine.endTxn(conn);
        }
        return targetAwardNumber;
    }
    
    /**
     * Method used to copy given Award details as a child Award.
     * It creates a copy and marks it as child of the given target Award
     *
     * @param sourceAwardNumber Source Award Number
     * @param targetAwardNumber Target Award Number where the given Source should be copied as child
     * @param isCopyDescendents boolean indicates whether descendents should be copied    
     * @param descendents CoeusVector containing descendents
     * @param userId String -- Added by Jobin for Bug Fix : 1184
     * @return String new Award number
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public String copyAwardAsChild(String sourceAwardNumber, String targetAwardNumber, boolean isCopyDescendents, CoeusVector descendents,String userId)  throws CoeusException,DBException{
        boolean success = false;
        Connection conn = null;
        String nextAwardNumber = "";
        
        dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
        
        try{
            Vector vctResult = null;
            
            Vector procedures = new Vector(3,2);
            int sequenceNumber = 1;

            AwardTxnBean awardTxnBean = new AwardTxnBean();
            //Get Root Award for the given Target Award number
            String rootAwardNumber = awardTxnBean.getRootAward(targetAwardNumber);            
            
            CoeusVector awardHierarchy = awardTxnBean.getAwardHierarchy(rootAwardNumber);
            if(awardHierarchy != null && awardHierarchy.size() > 0){
                awardHierarchy.sort("mitAwardNumber");
                //Get Last available Award Number
                AwardHierarchyBean awardHierarchyBean = (AwardHierarchyBean)awardHierarchy.elementAt(awardHierarchy.size()-1);
                nextAwardNumber = awardHierarchyBean.getMitAwardNumber();
                //Get next Award Number Node for the last Award Number
                nextAwardNumber = awardTxnBean.getNextAwardNode(nextAwardNumber);
            
                //Get Sap Feed Details
                CoeusVector sapFeedDetails = awardTxnBean.getSapFeedDetails(nextAwardNumber);
                //Transaction Id
                String transactionId = null;
				
				dbTimestamp = (new CoeusFunctions()).getDBTimestamp();

                if(dbEngine==null){
                    throw new CoeusException("db_exceptionCode.1000");
                }else{
                    CoeusVector sapFeedFiltered = null;
                    String feedType = "";

                    //Start Txn
                    conn = dbEngine.beginTxn();        

                    //Copy Award
                    procedures = new Vector(3,2);
                    procedures.addElement(copyAward(sourceAwardNumber, nextAwardNumber));
                    procedures.addElement(generateAwardReportRequired(nextAwardNumber, userId));
                    procedures.addElement(getCurrentTransactionId());

                    vctResult = dbEngine.executeStoreProcs(procedures, conn);

                    procedures = new Vector(3,2);

                    //Get Current Transaction Id
                    if(vctResult!=null){
                        Vector vctTransaction = (Vector)vctResult.elementAt(2);
                        HashMap transactionMap = (HashMap)vctTransaction.elementAt(0);
                        transactionId = (String)transactionMap.get("TRANSACTION_ID");
                    }

                    if(sapFeedDetails == null || sapFeedDetails.size() == 0){
                        feedType = "N";
						//-- Added userId field by Jobin for Bug Fix :1184
                        procedures.addElement(insertSapFeedDetails(nextAwardNumber, sequenceNumber, feedType, transactionId,userId));
                    }else{
                        Equals seqIdEq = new Equals("sequenceNumber", new Integer(sequenceNumber));
                        Equals statusEq = new Equals("feedStatus","P");
                        Equals transIdEq = new Equals("transactionId",transactionId);
                        And sapFeedFirstAnd = new And(seqIdEq, statusEq);
                        And sapFeedSecAnd = new And(sapFeedFirstAnd, transIdEq);

                        sapFeedFiltered = sapFeedDetails.filter(sapFeedSecAnd);
                        if(sapFeedFiltered.size() ==0){
                            Equals statusPEq = new Equals("feedStatus","P");
                            Equals statusFEq = new Equals("feedStatus","F");                
                            Equals feedTypeEq = new Equals("feedType","N");
                            Or statusOr = new Or(statusPEq, statusFEq);
                            sapFeedFirstAnd = new And(statusOr, feedTypeEq);
                            sapFeedFiltered = sapFeedDetails.filter(sapFeedFirstAnd);
                            if(sapFeedFiltered.size() == 0){
                                feedType = "N";
                            }else{
                                feedType = "C";
                            }
                            //Update SAP Feed details -- Added userId field by Jobin for Bug Fix :1184
                            procedures.addElement(insertSapFeedDetails(nextAwardNumber, sequenceNumber, feedType, transactionId,userId));
                        }
                    }
                    //Update Award Hierarchy Details
                    
                    awardHierarchyBean = new AwardHierarchyBean();
                    awardHierarchyBean.setParentMitAwardNumber(targetAwardNumber);
                    awardHierarchyBean.setMitAwardNumber(nextAwardNumber);
                    awardHierarchyBean.setRootMitAwardNumber(rootAwardNumber);
                    awardHierarchyBean.setAcType("I");
                    procedures.addElement(addUpdAwardHierarchy(awardHierarchyBean, dbTimestamp));

                    dbEngine.executeStoreProcs(procedures, conn);

                    //Copy Descendents if selected
                    if(isCopyDescendents){
						//-- Added userId field by Jobin for Bug Fix :1184
                        copyAllDescendents(nextAwardNumber, rootAwardNumber, descendents, descendents, new Vector(), conn, userId);
                    }                
                    //Commit Txn
                    dbEngine.commit(conn);
                    success = true;
                }
            }
        }finally{
            dbEngine.endTxn(conn);
        }
        //Return the new copied Award Number.
        //This is the parent for all the Descendents also
        return nextAwardNumber;
    }
    
    /** Method used to copy all the descendents of a Award.
     * This method is called recursively while copying descendents
     *
     * @param childDescendents CoeusVector
     * @param parentAwardNumber Parent Award Number of the descendents
     * @param rootAwardNumber Root Award Number of the descendents
     * @param conn Connection object. This is passed as all updates should go in one Txn.
     * @param userId -- Added userId field by Jobin for Bug Fix :1184
     * @return String new Award number
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available. */
    public String copyAllDescendents(String parentAwardNumber, String rootAwardNumber, CoeusVector childDescendents, CoeusVector baseDescendents, Vector copiedDescendents, Connection conn,String userId)  throws CoeusException,DBException{

        Vector vctResult = null;

        Vector procedures = new Vector(3,2);
        //int sequenceNumber = 1;            
        AwardTxnBean awardTxnBean = new AwardTxnBean();
        
        dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
        
        //Get next Award Number Node for the last Award Number
        String nextAwardNumber = parentAwardNumber;
        String sourceAwardNumber = "";
        
        CoeusVector sapFeedDetails = null;
        //Transaction Id
        String transactionId = null;
        CoeusVector sapFeedFiltered = null;
        CoeusVector descendents = null;
        String feedType = "";
        AwardHierarchyBean awardHierarchyBean = new AwardHierarchyBean();
                
        for(int nodeCount = 0; nodeCount < childDescendents.size(); nodeCount++){
            awardHierarchyBean = (AwardHierarchyBean)childDescendents.elementAt(nodeCount);
            //Check If this award Number is already copied as Descendent of any other Award
            if(!copiedDescendents.contains(awardHierarchyBean)){
                //Get Next Award Number
                nextAwardNumber = awardTxnBean.getNextAwardNode(nextAwardNumber);                                    
                sourceAwardNumber = awardHierarchyBean.getMitAwardNumber();

                sapFeedFiltered = null;
                feedType = "";
                //Get Sap Feed Details
                sapFeedDetails = awardTxnBean.getSapFeedDetails(nextAwardNumber);

                //Copy Award
                procedures = new Vector(3,2);
                procedures.addElement(copyAward(sourceAwardNumber, nextAwardNumber));
                procedures.addElement(generateAwardReportRequired(nextAwardNumber, userId));
                procedures.addElement(getCurrentTransactionId());

                vctResult = dbEngine.executeStoreProcs(procedures, conn);

                procedures = new Vector(3,2);

                //Get Current Transaction Id
                if(vctResult!=null){
                    Vector vctTransaction = (Vector)vctResult.elementAt(2);
                    HashMap transactionMap = (HashMap)vctTransaction.elementAt(0);
                    transactionId = (String)transactionMap.get("TRANSACTION_ID");
                }

                if(sapFeedDetails == null || sapFeedDetails.size() == 0){
                    feedType = "N";
                    //Update SAP Feed details -- Added userId field by Jobin for Bug Fix :1184
                    procedures.addElement(insertSapFeedDetails(nextAwardNumber, 1, feedType, transactionId,userId));
                }else{
                    //Sequence Number is 1
                    Equals seqIdEq = new Equals("sequenceNumber", new Integer(1));
                    Equals statusEq = new Equals("feedStatus","P");
                    Equals transIdEq = new Equals("transactionId",transactionId);
                    And sapFeedFirstAnd = new And(seqIdEq, statusEq);
                    And sapFeedSecAnd = new And(sapFeedFirstAnd, transIdEq);

                    sapFeedFiltered = sapFeedDetails.filter(sapFeedSecAnd);
                    if(sapFeedFiltered.size() ==0){
                        Equals statusPEq = new Equals("feedStatus","P");
                        Equals statusFEq = new Equals("feedStatus","F");                
                        Equals feedTypeEq = new Equals("feedType","N");
                        Or statusOr = new Or(statusPEq, statusFEq);
                        sapFeedFirstAnd = new And(statusOr, feedTypeEq);
                        sapFeedFiltered = sapFeedDetails.filter(sapFeedFirstAnd);
                        if(sapFeedFiltered.size() == 0){
                            feedType = "N";
                        }else{
                            feedType = "C";
                        }
                        //Update SAP Feed details -- Added userId field by Jobin for Bug Fix :1184
                        procedures.addElement(insertSapFeedDetails(nextAwardNumber, 1, feedType, transactionId, userId));
                    }
                }
                //Update Award Hierarchy Details
               
                awardHierarchyBean = new AwardHierarchyBean();
                awardHierarchyBean.setParentMitAwardNumber(parentAwardNumber);
                awardHierarchyBean.setMitAwardNumber(nextAwardNumber);
                awardHierarchyBean.setRootMitAwardNumber(rootAwardNumber);
                awardHierarchyBean.setAcType("I");
                procedures.addElement(addUpdAwardHierarchy(awardHierarchyBean, dbTimestamp));

                dbEngine.executeStoreProcs(procedures, conn);

                Equals parentAwardEq = new Equals("parentMitAwardNumber", sourceAwardNumber);

                descendents = baseDescendents.filter(parentAwardEq);
                
                if(descendents.size() != 0){
					//-- Added userId field by Jobin for Bug Fix :1184
                    nextAwardNumber = copyAllDescendents(nextAwardNumber, rootAwardNumber, descendents, baseDescendents, copiedDescendents, conn, userId);
                    descendents = baseDescendents.filter(parentAwardEq);
                    copiedDescendents.addAll(descendents);
                }
            }
        }
        return nextAwardNumber;
    }
    
    /**  This method used to get Current Award Transaction Id
     *  <li>To fetch the data, it uses the function FN_GET_CURRENT_AWARD_TRANS_ID.
     * @return int count for the award number
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available. */
    public ProcReqParameter getCurrentTransactionId() throws CoeusException, DBException {
        String transactionId = "";
        Vector param= new Vector();
        Vector result = new Vector();

        /* calling stored function */
        StringBuffer sql = new StringBuffer(
            "{  <<OUT INTEGER TRANSACTION_ID>> = call FN_GET_CURRENT_AWARD_TRANS_ID() }");
        
        ProcReqParameter procReqParameter  = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sql.toString());                

        return procReqParameter; 
    }
    
    /** Method used to update/insert/delete all the details of Award Hierarchy
     * <li>To fetch the data, it uses DW_UPD_AWARD_HIERARCHY procedure.
     *
     * @return boolean this holds true for successfull insert/modify or
     * false if fails.
     * @param awardHierarchyBean AwardHierarchyBean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available. */
    public ProcReqParameter addUpdAwardHierarchy( AwardHierarchyBean
                awardHierarchyBean, Timestamp timestamp)  throws CoeusException,DBException{
        //boolean success = false;
        //Vector procedures = new Vector(5,3);

        Vector param = new Vector();
        //dbTimestamp = (new CoeusFunctions()).getDBTimestamp();

        dbTimestamp = timestamp;
        
        param.addElement(new Parameter("ROOT_MIT_AWARD_NUMBER",
                    DBEngineConstants.TYPE_STRING,
                            awardHierarchyBean.getRootMitAwardNumber()));        
        param.addElement(new Parameter("MIT_AWARD_NUMBER",
                    DBEngineConstants.TYPE_STRING,
                            awardHierarchyBean.getMitAwardNumber()));
        param.addElement(new Parameter("PARENT_AWARD_NUMBER",
                    DBEngineConstants.TYPE_STRING,
                            awardHierarchyBean.getParentMitAwardNumber()));        
        param.addElement(new Parameter("UPDATE_TIMESTAMP",
                    DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        param.addElement(new Parameter("UPDATE_USER",
                    DBEngineConstants.TYPE_STRING, userId));
        param.addElement(new Parameter("AW_MIT_AWARD_NUMBER",
                    DBEngineConstants.TYPE_STRING,
                            awardHierarchyBean.getMitAwardNumber()));
        param.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                    DBEngineConstants.TYPE_TIMESTAMP,
                                awardHierarchyBean.getUpdateTimestamp()));
        param.addElement(new Parameter("AC_TYPE",
                    DBEngineConstants.TYPE_STRING,
                            awardHierarchyBean.getAcType()));

        StringBuffer sql = new StringBuffer(
                                        "call DW_UPD_AWARD_HIERARCHY(");
        sql.append(" <<ROOT_MIT_AWARD_NUMBER>> , ");
        sql.append(" <<MIT_AWARD_NUMBER>> , ");        
        sql.append(" <<PARENT_AWARD_NUMBER>> , ");
        sql.append(" <<UPDATE_TIMESTAMP>> , ");
        sql.append(" <<UPDATE_USER>> , ");        
        sql.append(" <<AW_MIT_AWARD_NUMBER>> , ");
        sql.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sql.append(" <<AC_TYPE>> )");

        ProcReqParameter procReqParameter  = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sql.toString());

        return procReqParameter;
    }    
    
    //Main method for testing the bean
    public static void main(String args[]) {
        try{
			
            AwardTxnBean awardTxnBean = new AwardTxnBean();
            AwardUpdateTxnBean awardUpdateTxnBean = new AwardUpdateTxnBean("COEUS");
            
            AwardHeaderBean awardHeaderBean = null;
            CoeusVector awardData = awardTxnBean.getAwardApprovedEquipment("000005-053");
            AwardApprovedEquipmentBean awardApprovedEquipmentBean = (AwardApprovedEquipmentBean)awardData.elementAt(0);
            awardApprovedEquipmentBean.setAcType("U");
            //boolean success = awardUpdateTxnBean.addUpdAwardApprovedEquipment(awardApprovedEquipmentBean);
            
            //System.out.println("Update : "+success);

        }catch(Exception e){
            e.printStackTrace();
        }
    }
    //New methods added with case 2796: Sync to parent- Start
    /** This method is used to SYNC the data of Parent to child
     * The beans which requires syncing will have the syncRequired flag set to true.
     *
     * @param awardData - the HashTable containing all the award Information
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector syncAward(Hashtable awardData) throws CoeusException, DBException {
        
        boolean success = false;
        Vector procedures = new Vector(3,2);
        CoeusVector recordsToSync;
        Vector result = null;
        AwardBean awardBean = (AwardBean)awardData.get(AwardBean.class);
        boolean syncSAPFeedForChildAwards = false;
        
        //Sync AwardDetailsBean fields - status and sponsor
        if(awardBean.getAwardDetailsBean().isSyncRequired()){
            Map param = awardBean.getAwardDetailsBean().getParameter();
            //Sync Award Sponsor code first and then status.
            if(param.containsKey(KeyConstants.SPONSOR_CODE)){
                procedures.add(syncAwardSponsor(awardBean));
            }
            if(param.containsKey(KeyConstants.AWARD_STATUS)){
                procedures.add(syncAwardStatus(awardBean));
            }
            // Added for COEUSDEV-563:Award Sync to Parent is not triggering SAP feed for child accounts its touching
            if(param.containsKey(KeyConstants.ADD_SAP_FEED_FOR_CHILD_AWARDS)){
                if("Y".equalsIgnoreCase((String)param.get(KeyConstants.ADD_SAP_FEED_FOR_CHILD_AWARDS))){
                    syncSAPFeedForChildAwards = true;
                }
            }
            // COEUSDEV-563:End
        }
        //Sync award comments
        recordsToSync = getRecordsMarkedForSync(awardBean.getAwardComments());
        AwardCommentsBean awardCommentsBean = null;
        
        for(int i=0;i<recordsToSync.size();i++){
            awardCommentsBean = (AwardCommentsBean)recordsToSync.get(i);
            procedures.add(syncAwardComments(awardCommentsBean));
        }
        
        //Sync award  Terms
        String termsKey[] = {KeyConstants.EQUIPMENT_APPROVAL_TERMS, KeyConstants.INVENTION_TERMS,
        KeyConstants.PRIOR_APPROVAL_TERMS, KeyConstants.PROPERTY_TERMS, KeyConstants.PUBLICATION_TERMS,
        KeyConstants.REFERENCED_DOCUMENT_TERMS, KeyConstants.RIGHTS_IN_DATA_TERMS,
        KeyConstants.SUBCONTRACT_APPROVAL_TERMS, KeyConstants.TRAVEL_RESTRICTION_TERMS};
        
        for(int index = 0; index < termsKey.length; index++) {
            recordsToSync = getRecordsMarkedForSync((CoeusVector)awardData.get(termsKey[index]));
            AwardTermsBean awardTermsBean = null;
            for(int i = 0; i < recordsToSync.size() ; i++){
                awardTermsBean = (AwardTermsBean)recordsToSync.elementAt(i);
                procedures.add(syncAwardTerms(awardTermsBean,index));
            }
        }
        
        //Sync Award Contacts
        recordsToSync = getRecordsMarkedForSync((CoeusVector)awardData.get(AwardContactBean.class));
        AwardContactBean awardContactBean = null;
        for(int i = 0; i < recordsToSync.size() ; i++){
            awardContactBean = (AwardContactBean)recordsToSync.elementAt(i);
            procedures.add(syncAwardContacts(awardContactBean));
        }
        
        //Sync Award Investigators
        recordsToSync = getRecordsMarkedForSync(awardBean.getAwardInvestigators());
        AwardInvestigatorsBean invBean = null;
        for(int i = 0; i < recordsToSync.size() ; i++){
            invBean = (AwardInvestigatorsBean)recordsToSync.get(i);
            procedures.add(syncAwardInvestigators(invBean));
            // Added for COEUSDEV-563:Award Sync to Parent is not triggering SAP feed for child accounts its touching
            Map syncInfo = invBean.getParameter();
            if(syncInfo.containsKey(KeyConstants.ADD_SAP_FEED_FOR_CHILD_AWARDS)){
                if("Y".equalsIgnoreCase((String)syncInfo.get(KeyConstants.ADD_SAP_FEED_FOR_CHILD_AWARDS))){
                    syncSAPFeedForChildAwards = true;
                }
            }
            // COEUSDEV-563:End
        }
        
        //Sync Reports
        recordsToSync = getRecordsMarkedForSync((CoeusVector)awardData.get(AwardReportTermsBean.class));
        AwardReportTermsBean awardReportTermsBean = null;
        for(int i = 0; i < recordsToSync.size() ; i++){
            awardReportTermsBean = (AwardReportTermsBean)recordsToSync.get(i);
            procedures.add(syncAwardReports(awardReportTermsBean));
        }
        // Added for COEUSDEV-563:Award Sync to Parent is not triggering SAP feed for child accounts its touching
        if(syncSAPFeedForChildAwards){
            procedures.add(insertSapFeedDetailsForChildAwards(awardBean.getMitAwardNumber(), awardBean.getSequenceNumber()));
        }
        // COEUSDEV-563:End
        return procedures;
    }
    
    private CoeusVector getRecordsMarkedForSync(CoeusVector cvRecords){
        
        Equals syncReqd = new Equals("syncRequired",true);
        NotEquals acTypeNotNull = new NotEquals("acType",null);
        And filterCondition = new And(syncReqd,acTypeNotNull);
        
        CoeusVector recordsToSync = new CoeusVector();
        if(cvRecords!=null && !cvRecords.isEmpty()){
            recordsToSync = cvRecords.filter(filterCondition);
            recordsToSync.sort("acType",false);//to get insert procedures first
        }
        return recordsToSync;
    }
    
    /** This method is used to SYNC the status of parent award to children.
     * If the parent status is changed from one status to another,
     * the same update is done on the child award
     * <li>To SYNC the data, it uses pkg_sync_award.sync_award_status
     *
     * @param awardBean - the AwardBean which holds the award info
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    private ProcReqParameter syncAwardStatus(AwardBean awardBean) throws CoeusException, DBException {
        
        Vector param= new Vector();
        
        //Sync award status
        param.add(new Parameter("AS_MIT_AWARD_NUMBER",
                DBEngineConstants.TYPE_STRING,awardBean.getMitAwardNumber()));
        param.add(new Parameter("AS_SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,new Integer(awardBean.getSequenceNumber())));
        param.add(new Parameter("AS_AWARD_STATUS",
                DBEngineConstants.TYPE_INT,new Integer(awardBean.getStatusCode())));
        param.add(new Parameter("AS_UPDATE_USER",
                DBEngineConstants.TYPE_STRING,userId));
        param.add(new Parameter("AS_TARGET",
                DBEngineConstants.TYPE_STRING,awardBean.getAwardDetailsBean().getSyncTarget()));
        //COEUSDEV:253-Add FABE and CS checkboxes tgo sync to children window in award Sync
        Map syncInfo = awardBean.getAwardDetailsBean().getParameter();
        param.add(new Parameter("AS_INCLUDE_FABE",
                DBEngineConstants.TYPE_STRING,syncInfo.get(KeyConstants.SYNC_FABE_ACCOUNTS)));
        param.add(new Parameter("AS_INCLUDE_CS",
                DBEngineConstants.TYPE_STRING,syncInfo.get(KeyConstants.SYNC_CS_ACCOUNTS)));
        //COEUSDEV:253 End
        StringBuffer sql = new StringBuffer("{ <<OUT INTEGER SYNC_FLAG>> = ");
        sql.append(" call pkg_sync_award.sync_award_status( ");
        sql.append(" <<AS_MIT_AWARD_NUMBER>> , ");
        sql.append(" <<AS_SEQUENCE_NUMBER>> , ");
        sql.append(" <<AS_AWARD_STATUS>> , ");
        sql.append(" <<AS_UPDATE_USER>> , ");
        sql.append(" <<AS_TARGET>> , ");
        //COEUSDEV:253-Add FABE and CS checkboxes tgo sync to children window in award Sync
        sql.append(" <<AS_INCLUDE_FABE>> , ");
        sql.append(" <<AS_INCLUDE_CS>> ");
        //COEUSDEV:253 End
        sql.append(" ) }");
        
        ProcReqParameter procReqParameter  = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sql.toString());
        return procReqParameter;
    }
    
    /** This method is used to SYNC the sponsor code of parent award to children.
     * If the prime sponsor of parent is changed from one code to another,
     * the same update is done on the child award
     * <li>To SYNC the data, it uses pkg_sync_award.sync_award_sponsor
     *
     * @param awardBean - the AwardBean which holds the award info
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    private ProcReqParameter syncAwardSponsor(AwardBean awardBean) throws CoeusException, DBException {
        
        Vector param= new Vector();
        
        //Sync award sponsor
        param.add(new Parameter("AS_MIT_AWARD_NUMBER",
                DBEngineConstants.TYPE_STRING,awardBean.getMitAwardNumber()));
        param.add(new Parameter("AS_SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,new Integer(awardBean.getSequenceNumber())));
        param.add(new Parameter("AS_SPONSOR_CODE",
                DBEngineConstants.TYPE_STRING, awardBean.getSponsorCode()));
        param.add(new Parameter("AS_UPDATE_USER",
                DBEngineConstants.TYPE_STRING,userId));
        param.add(new Parameter("AS_TARGET",
                DBEngineConstants.TYPE_STRING,awardBean.getAwardDetailsBean().getSyncTarget()));
        //COEUSDEV:253-Add FABE and CS checkboxes tgo sync to children window in award Sync
        Map syncInfo = awardBean.getAwardDetailsBean().getParameter();
        param.add(new Parameter("AS_INCLUDE_FABE",
                DBEngineConstants.TYPE_STRING,syncInfo.get(KeyConstants.SYNC_FABE_ACCOUNTS)));
        param.add(new Parameter("AS_INCLUDE_CS",
                DBEngineConstants.TYPE_STRING,syncInfo.get(KeyConstants.SYNC_CS_ACCOUNTS)));
        //COEUSDEV:253 End
        
        StringBuffer sql = new StringBuffer("{ <<OUT INTEGER SYNC_FLAG>> = ");
        sql.append(" call pkg_sync_award.sync_award_sponsor( ");
        sql.append(" <<AS_MIT_AWARD_NUMBER>> , ");
        sql.append(" <<AS_SEQUENCE_NUMBER>> , ");
        sql.append(" <<AS_SPONSOR_CODE>> , ");
        sql.append(" <<AS_UPDATE_USER>> , ");
        sql.append(" <<AS_TARGET>> , ");
        //COEUSDEV:253-Add FABE and CS checkboxes tgo sync to children window in award Sync
        sql.append(" <<AS_INCLUDE_FABE>> , ");
        sql.append(" <<AS_INCLUDE_CS>> ");
        //COEUSDEV:253 End
        sql.append(" ) }");
        
        ProcReqParameter procReqParameter  = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sql.toString());
        return procReqParameter;
    }
    
    
    /** This method is used to SYNC the comments of the parent Award to children.
     * <li>To SYNC the data, it uses pkg_sync_award.sync_award_comments
     *
     * @param commentsBean - the AwardCommentsBean which holds the comments info
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    private ProcReqParameter syncAwardComments(AwardCommentsBean commentsBean) throws CoeusException, DBException {
        
        Vector param= new Vector();
        
        //Sync award comments
        param.add(new Parameter("AS_MIT_AWARD_NUMBER",
                DBEngineConstants.TYPE_STRING,commentsBean.getMitAwardNumber()));
        param.add(new Parameter("AS_SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,new Integer(commentsBean.getSequenceNumber())));
        param.add(new Parameter("AS_AWARD_COMMENT_CODE",
                DBEngineConstants.TYPE_INT,new Integer(commentsBean.getCommentCode())));
        param.add(new Parameter("AS_UPDATE_USER",
                DBEngineConstants.TYPE_STRING,userId));
        param.add(new Parameter("AS_TARGET",
                DBEngineConstants.TYPE_STRING,commentsBean.getSyncTarget()));
        //COEUSDEV:253-Add FABE and CS checkboxes tgo sync to children window in award Sync
        Map syncInfo = commentsBean.getParameter();
        param.add(new Parameter("AS_INCLUDE_FABE",
                DBEngineConstants.TYPE_STRING,syncInfo.get(KeyConstants.SYNC_FABE_ACCOUNTS)));
        param.add(new Parameter("AS_INCLUDE_CS",
                DBEngineConstants.TYPE_STRING,syncInfo.get(KeyConstants.SYNC_CS_ACCOUNTS)));
        //COEUSDEV:253 End
        
        StringBuffer sql = new StringBuffer("{ <<OUT INTEGER SYNC_FLAG>> = ");
        sql.append(" call pkg_sync_award.sync_award_comments( ");
        sql.append(" <<AS_MIT_AWARD_NUMBER>> , ");
        sql.append(" <<AS_SEQUENCE_NUMBER>> , ");
        sql.append(" <<AS_AWARD_COMMENT_CODE>> , ");
        sql.append(" <<AS_UPDATE_USER>> , ");
        sql.append(" <<AS_TARGET>> , ");
        //COEUSDEV:253-Add FABE and CS checkboxes tgo sync to children window in award Sync
        sql.append(" <<AS_INCLUDE_FABE>> , ");
        sql.append(" <<AS_INCLUDE_CS>> ");
        //COEUSDEV:253 End
        sql.append(" ) }");
        
        ProcReqParameter procReqParameter  = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sql.toString());
        return procReqParameter;
    }
    
    /** This method is used to SYNC the award terms.
     * Each terms uses seperate functions to sync the data.
     * @param termsBean - the AwardTermsBean which holds the terms info
     * @param termIndex - integer representation of term type.
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    private ProcReqParameter syncAwardTerms(AwardTermsBean termsBean,int termIndex) throws CoeusException, DBException {
        
        Vector param= new Vector();
        StringBuffer sql = new StringBuffer();
        String functionName = null;
        
        String functionNames[] = {"sync_award_equipment_terms" , "sync_award_invention_terms","sync_awd_prior_approval_terms",
        "sync_award_property_terms","sync_award_publication_terms","sync_award_document_terms",
        "sync_award_rights_in_data","sync_awd_subcontr_appr_terms","sync_awd_travel_terms"};
        
        if( termIndex < functionNames.length ){
            
            functionName = functionNames[termIndex];
            param.add(new Parameter("AS_MIT_AWARD_NUMBER",
                    DBEngineConstants.TYPE_STRING,termsBean.getMitAwardNumber()));
            param.add(new Parameter("AS_SEQUENCE_NUMBER",
                    DBEngineConstants.TYPE_INT,new Integer(termsBean.getSequenceNumber())));
            param.add(new Parameter("AS_TERM_CODE",
                    DBEngineConstants.TYPE_INT,new Integer(termsBean.getTermsCode())));
            param.add(new Parameter("AS_UPDATE_USER",
                    DBEngineConstants.TYPE_STRING,userId));
            param.add(new Parameter("AS_AC_TYPE",
                    DBEngineConstants.TYPE_STRING,termsBean.getAcType()));
            param.add(new Parameter("AS_TARGET",
                    DBEngineConstants.TYPE_STRING,termsBean.getSyncTarget()));
            //COEUSDEV:253-Add FABE and CS checkboxes tgo sync to children window in award Sync
            Map syncInfo = termsBean.getParameter();
            param.add(new Parameter("AS_INCLUDE_FABE",
                    DBEngineConstants.TYPE_STRING,syncInfo.get(KeyConstants.SYNC_FABE_ACCOUNTS)));
            param.add(new Parameter("AS_INCLUDE_CS",
                    DBEngineConstants.TYPE_STRING,syncInfo.get(KeyConstants.SYNC_CS_ACCOUNTS)));
            //COEUSDEV:253 End

            sql.append("{ <<OUT INTEGER SYNC_FLAG>> = ");
            sql.append(" call pkg_sync_award.");
            sql.append(functionName);
            sql.append(" ( <<AS_MIT_AWARD_NUMBER>> , ");
            sql.append(" <<AS_SEQUENCE_NUMBER>> , ");
            sql.append(" <<AS_TERM_CODE>> , ");
            sql.append(" <<AS_UPDATE_USER>> , ");
            sql.append(" <<AS_AC_TYPE>> , ");
            sql.append(" <<AS_TARGET>> , ");
            //COEUSDEV:253-Add FABE and CS checkboxes tgo sync to children window in award Sync
            sql.append(" <<AS_INCLUDE_FABE>> , ");
            sql.append(" <<AS_INCLUDE_CS>> ");
            //COEUSDEV:253 End
            sql.append(" ) }");
        }
        
        ProcReqParameter procReqParameter  = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sql.toString());
        return procReqParameter;
        
    }
    /** This method is used to SYNC the award Contacts
     * <li>To SYNC the data, it uses pkg_sync_award.sync_award_contacts
     *
     * @param commentsBean - the AwardCommentsBean which holds the comments info
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    private ProcReqParameter syncAwardContacts(AwardContactBean awardContactBean) throws CoeusException, DBException {
        
        Vector param= new Vector();
        
        //Sync award contacts
        param.add(new Parameter("AS_MIT_AWARD_NUMBER",
                DBEngineConstants.TYPE_STRING,awardContactBean.getMitAwardNumber()));
        param.add(new Parameter("AS_SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,new Integer(awardContactBean.getSequenceNumber())));
        param.add(new Parameter("AS_CONTACT_TYPE_CODE",
                DBEngineConstants.TYPE_INT,new Integer(awardContactBean.getContactTypeCode())));
         param.add(new Parameter("AW_CONTACT_TYPE_CODE",
                DBEngineConstants.TYPE_INT,new Integer(awardContactBean.getAw_ContactTypeCode())));
         param.add(new Parameter("AS_ROLODEX_ID",
                DBEngineConstants.TYPE_INT,new Integer(awardContactBean.getRolodexId())));
         param.add(new Parameter("AW_ROLODEX_ID",
                DBEngineConstants.TYPE_INT,new Integer(awardContactBean.getAw_RolodexId())));
        param.add(new Parameter("AS_UPDATE_USER",
                DBEngineConstants.TYPE_STRING,userId));
        param.add(new Parameter("AS_AC_TYPE",
                DBEngineConstants.TYPE_STRING,awardContactBean.getAcType()));
        param.add(new Parameter("AS_TARGET",
                DBEngineConstants.TYPE_STRING,awardContactBean.getSyncTarget()));
        //COEUSDEV:253-Add FABE and CS checkboxes tgo sync to children window in award Sync
        Map syncInfo = awardContactBean.getParameter();
        param.add(new Parameter("AS_INCLUDE_FABE",
                DBEngineConstants.TYPE_STRING,syncInfo.get(KeyConstants.SYNC_FABE_ACCOUNTS)));
        param.add(new Parameter("AS_INCLUDE_CS",
                DBEngineConstants.TYPE_STRING,syncInfo.get(KeyConstants.SYNC_CS_ACCOUNTS)));
        //COEUSDEV:253 End
        
        StringBuffer sql = new StringBuffer("{ <<OUT INTEGER SYNC_FLAG>> = ");
        sql.append(" call pkg_sync_award.sync_award_contacts( ");
        sql.append(" <<AS_MIT_AWARD_NUMBER>> , ");
        sql.append(" <<AS_SEQUENCE_NUMBER>> , ");
        sql.append(" <<AS_CONTACT_TYPE_CODE>> , ");
        sql.append(" <<AW_CONTACT_TYPE_CODE>> , ");
        sql.append(" <<AS_ROLODEX_ID>> , ");
        sql.append(" <<AW_ROLODEX_ID>> , ");
        sql.append(" <<AS_UPDATE_USER>> , ");
        sql.append(" <<AS_AC_TYPE>> , ");
        sql.append(" <<AS_TARGET>> , ");
        //COEUSDEV:253-Add FABE and CS checkboxes tgo sync to children window in award Sync
        sql.append(" <<AS_INCLUDE_FABE>> , ");
        sql.append(" <<AS_INCLUDE_CS>> ");
        //COEUSDEV:253 End
        sql.append(" ) }");
        
        ProcReqParameter procReqParameter  = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sql.toString());
        return procReqParameter;
    }
    
    /** This method is used to SYNC the award Investigators
     * <li>To SYNC the data, it uses pkg_sync_award.sync_awd_investigators
     *
     * @param investigatorBean - the AwardInvestigatorsBean which holds the investigator info
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    private ProcReqParameter syncAwardInvestigators(AwardInvestigatorsBean investigatorBean) throws CoeusException, DBException {
        
        Vector param= new Vector();
        
        //Sync award contacts
        param.add(new Parameter("AS_MIT_AWARD_NUMBER",
                DBEngineConstants.TYPE_STRING,investigatorBean.getMitAwardNumber()));
        param.add(new Parameter("AS_SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,new Integer(investigatorBean.getSequenceNumber())));
        param.add(new Parameter("AS_PERSON_ID",
                DBEngineConstants.TYPE_STRING,investigatorBean.getPersonId()));
        param.add(new Parameter("AW_PERSON_ID",
                DBEngineConstants.TYPE_STRING,investigatorBean.getAw_PersonId()));
        param.add(new Parameter("AS_UPDATE_USER",
                DBEngineConstants.TYPE_STRING,userId));
        param.add(new Parameter("AS_AC_TYPE",
                DBEngineConstants.TYPE_STRING,investigatorBean.getAcType()));
        param.add(new Parameter("AS_TARGET",
                DBEngineConstants.TYPE_STRING,investigatorBean.getSyncTarget()));
        //COEUSDEV:253-Add FABE and CS checkboxes tgo sync to children window in award Sync
        Map syncInfo = investigatorBean.getParameter();
        param.add(new Parameter("AS_INCLUDE_FABE",
                DBEngineConstants.TYPE_STRING,syncInfo.get(KeyConstants.SYNC_FABE_ACCOUNTS)));
        param.add(new Parameter("AS_INCLUDE_CS",
                DBEngineConstants.TYPE_STRING,syncInfo.get(KeyConstants.SYNC_CS_ACCOUNTS)));
        //COEUSDEV:253 End
        
        StringBuffer sql = new StringBuffer("{ <<OUT INTEGER SYNC_FLAG>> = ");
        sql.append(" call pkg_sync_award.sync_awd_investigators( ");
        sql.append(" <<AS_MIT_AWARD_NUMBER>> , ");
        sql.append(" <<AS_SEQUENCE_NUMBER>> , ");
        sql.append(" <<AS_PERSON_ID>> , ");
        sql.append(" <<AW_PERSON_ID>> , ");
        sql.append(" <<AS_UPDATE_USER>> , ");
        sql.append(" <<AS_AC_TYPE>> , ");
        sql.append(" <<AS_TARGET>> , ");
        //COEUSDEV:253-Add FABE and CS checkboxes tgo sync to children window in award Sync
        sql.append(" <<AS_INCLUDE_FABE>> , ");
        sql.append(" <<AS_INCLUDE_CS>> ");
        //COEUSDEV:253 End
        sql.append(" ) }");
        
        ProcReqParameter procReqParameter  = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sql.toString());
        return procReqParameter;
    }
    
    /** This method is used to SYNC the award reports
     * <li>To SYNC the data, it uses pkg_sync_award.sync_awd_reports
     *
     * @param awardReportTermsBean - the AwardReportTermsBean which holds the reports info
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    private ProcReqParameter syncAwardReports(AwardReportTermsBean awardReportTermsBean) throws CoeusException, DBException {
        
        Vector param= new Vector();
        //Sync award reports
        param.add(new Parameter("AS_MIT_AWARD_NUMBER",
                DBEngineConstants.TYPE_STRING,awardReportTermsBean.getMitAwardNumber()));
        param.add(new Parameter("AS_SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,new Integer(awardReportTermsBean.getSequenceNumber())));
        param.add(new Parameter("AS_REPORT_CLASS_CODE",
                DBEngineConstants.TYPE_INT,new Integer(awardReportTermsBean.getReportClassCode())));
        param.add(new Parameter("AS_REPORT_CODE",
                DBEngineConstants.TYPE_INT,new Integer(awardReportTermsBean.getReportCode())));
         param.add(new Parameter("AS_FREQUENCY_CODE",
                DBEngineConstants.TYPE_INT,new Integer(awardReportTermsBean.getFrequencyCode())));
        param.add(new Parameter("AS_FREQUENCY_BASE_CODE",
                DBEngineConstants.TYPE_INT,new Integer(awardReportTermsBean.getFrequencyBaseCode())));
         param.add(new Parameter("AS_OSP_DISTR_CODE",
                DBEngineConstants.TYPE_INT,new Integer(awardReportTermsBean.getOspDistributionCode())));
        param.add(new Parameter("AS_CONTACT_TYPE_CODE",
                DBEngineConstants.TYPE_INT,new Integer(awardReportTermsBean.getContactTypeCode())));
         param.add(new Parameter("AS_ROLODEX_ID",
                DBEngineConstants.TYPE_INT,new Integer(awardReportTermsBean.getRolodexId())));
         //Added for COEUSQA-2408 : CLONE -Award Sync - MOdify and sync reports not working correctly - Start
         param.add(new Parameter("AS_DUE_DATE",
                 DBEngineConstants.TYPE_DATE,awardReportTermsBean.getDueDate()));
         param.add(new Parameter("AS_NUMBER_OF_COPIES",
                 DBEngineConstants.TYPE_INT,new Integer(awardReportTermsBean.getNumberOfCopies())));
         param.add(new Parameter("AW_REPORT_CLASS_CODE",
                DBEngineConstants.TYPE_INT,new Integer(awardReportTermsBean.getAw_ReportClassCode())));
         param.add(new Parameter("AW_REPORT_CODE",
                DBEngineConstants.TYPE_INT,new Integer(awardReportTermsBean.getAw_ReportCode())));
         param.add(new Parameter("AW_FREQUENCY_CODE",
                DBEngineConstants.TYPE_INT,new Integer(awardReportTermsBean.getAw_FrequencyCode())));
         param.add(new Parameter("AW_FREQUENCY_BASE_CODE",
                DBEngineConstants.TYPE_INT,new Integer(awardReportTermsBean.getAw_FrequencyBaseCode())));
         param.add(new Parameter("AW_OSP_DISTR_CODE",
                DBEngineConstants.TYPE_INT,new Integer(awardReportTermsBean.getAw_OspDistributionCode())));
         //COEUSQA-2408 : End
        param.add(new Parameter("AW_CONTACT_TYPE_CODE",
                DBEngineConstants.TYPE_INT,new Integer(awardReportTermsBean.getAw_ContactTypeCode())));
         param.add(new Parameter("AW_ROLODEX_ID",
                DBEngineConstants.TYPE_INT,new Integer(awardReportTermsBean.getAw_RolodexId())));
         
         param.add(new Parameter("AW_DUE_DATE",
                 DBEngineConstants.TYPE_DATE,awardReportTermsBean.getAw_DueDate()));
         
        param.add(new Parameter("AS_UPDATE_USER",
                DBEngineConstants.TYPE_STRING,userId));
        param.add(new Parameter("AS_AC_TYPE",
                DBEngineConstants.TYPE_STRING,awardReportTermsBean.getAcType()));
        param.add(new Parameter("AS_TARGET",
                DBEngineConstants.TYPE_STRING,awardReportTermsBean.getSyncTarget()));
        //COEUSDEV:253-Add FABE and CS checkboxes tgo sync to children window in award Sync
        Map syncInfo = awardReportTermsBean.getParameter();
        param.add(new Parameter("AS_INCLUDE_FABE",
                DBEngineConstants.TYPE_STRING,syncInfo.get(KeyConstants.SYNC_FABE_ACCOUNTS)));
        param.add(new Parameter("AS_INCLUDE_CS",
                DBEngineConstants.TYPE_STRING,syncInfo.get(KeyConstants.SYNC_CS_ACCOUNTS)));
        //COEUSDEV:253 End
        
        StringBuffer sql = new StringBuffer("{ <<OUT INTEGER SYNC_FLAG>> = ");
        sql.append(" call pkg_sync_award.sync_awd_reports( ");
        sql.append(" <<AS_MIT_AWARD_NUMBER>> , ");
        sql.append(" <<AS_SEQUENCE_NUMBER>> , ");
        sql.append(" <<AS_REPORT_CLASS_CODE>> , ");
        sql.append(" <<AS_REPORT_CODE>> , ");
        sql.append(" <<AS_FREQUENCY_CODE>> , ");
        sql.append(" <<AS_FREQUENCY_BASE_CODE>> , ");
        sql.append(" <<AS_OSP_DISTR_CODE>> , ");
        sql.append(" <<AS_CONTACT_TYPE_CODE>> , ");
        sql.append(" <<AS_ROLODEX_ID>> , ");
        //Added for COEUSQA-2408 : CLONE -Award Sync - MOdify and sync reports not working correctly - Start
        sql.append(" <<AS_DUE_DATE>> , ");
        sql.append(" <<AS_NUMBER_OF_COPIES>> , ");
        sql.append(" <<AW_REPORT_CLASS_CODE>> , ");
        sql.append(" <<AW_REPORT_CODE>> , ");
        sql.append(" <<AW_FREQUENCY_CODE>> , ");
        sql.append(" <<AW_FREQUENCY_BASE_CODE>> , ");
        sql.append(" <<AW_OSP_DISTR_CODE>> , ");
        //COEUSQA-2408 : End

        sql.append(" <<AW_CONTACT_TYPE_CODE>> , ");
        sql.append(" <<AW_ROLODEX_ID>> , ");      
        sql.append(" <<AW_DUE_DATE>> , ");      
        sql.append(" <<AS_UPDATE_USER>> , ");
        sql.append(" <<AS_AC_TYPE>> , ");
        sql.append(" <<AS_TARGET>> , ");
        //COEUSDEV:253-Add FABE and CS checkboxes tgo sync to children window in award Sync
        sql.append(" <<AS_INCLUDE_FABE>> , ");
        sql.append(" <<AS_INCLUDE_CS>> ");
        //COEUSDEV:253 End
        sql.append(" ) }");
        
        ProcReqParameter procReqParameter  = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sql.toString());
        return procReqParameter;
    }
    //2796: Sync to parent End
}