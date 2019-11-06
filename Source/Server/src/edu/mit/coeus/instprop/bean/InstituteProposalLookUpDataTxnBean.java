/*
 * @(#)InstituteProposalLookUpDataTxnBean.java 1.0 01/19/04 11:50 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */


package edu.mit.coeus.instprop.bean;

import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.utils.dbengine.DBEngineConstants;
import edu.mit.coeus.utils.dbengine.TransactionMonitor;

import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.exception.CoeusException;
import java.util.Vector;
import java.util.HashMap;
import java.util.Hashtable;
import java.sql.Date;
import java.sql.Timestamp;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ComboBoxBean;
/**
 * This class provides the methods for performing all look up procedures executions for
 * Institute Proposal functionality. 
 * All methods use <code>DBEngineImpl</code> singleton instance for
 * database interaction.
 *
 * @version 1.0 on January 17, 2004, 11:50 AM
 * @author  Prasanna Kumar K
 */
public class InstituteProposalLookUpDataTxnBean {
    
    // Instance of a dbEngine
    private DBEngineImpl dbEngine;
    
    /** Creates a new instance of AwardTxnBean */
    public InstituteProposalLookUpDataTxnBean() {
        dbEngine = new DBEngineImpl();
    }     
    
    /**
     *  This method used populates combo box with Institute Proposal Status item
     *  <li>To fetch the data, it uses the procedure DW_GET_PROPOSAL_STATUS.
     *
     *  @return CoeusVector map of all Institute Proposal Status with Proposal
     *  item types code as key and proposal item type description as value.
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getProposalStatus() throws CoeusException,DBException{
        Vector result = null;
        CoeusVector proposalStatus = null;
        Vector param= new Vector();
        HashMap hasProposalStatus = null;
        if(dbEngine != null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
                "call DW_GET_PROPOSAL_STATUS( <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int typeCount = result.size();
        if (typeCount > 0){
            proposalStatus = new CoeusVector();
            for(int rowIndex=0; rowIndex<typeCount; rowIndex++){
                hasProposalStatus = (HashMap)result.elementAt(rowIndex);
                proposalStatus.addElement(new ComboBoxBean(
                        hasProposalStatus.get(
                        "PROPOSAL_STATUS_CODE").toString(),
                        hasProposalStatus.get("DESCRIPTION").toString()));
            }
        }
        return proposalStatus;
    }
    
    /**
     *  This method used populates combo box with Proposal types item 
     *  from OSP$PROPOSAL_TYPE.
     *  <li>To fetch the data, it uses the procedure GET_PROPOSAL_TYPE_LIST_DW.
     *
     *  @return CoeusVector map of all Proposal types
     *
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getProposalTypes() throws CoeusException,DBException{
        Vector result = null;
        CoeusVector proposalTypes = null;
        Vector param= new Vector();
        HashMap hasProposalTypes = null;
        if(dbEngine != null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
                "call GET_PROPOSAL_TYPE_LIST_DW( <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int typeCount = result.size();
        if (typeCount > 0){
            proposalTypes = new CoeusVector();
            for(int rowIndex=0; rowIndex<typeCount; rowIndex++){
                hasProposalTypes = (HashMap)result.elementAt(rowIndex);
                proposalTypes.addElement(new ComboBoxBean(
                        hasProposalTypes.get(
                        "PROPOSAL_TYPE_CODE").toString(),
                        hasProposalTypes.get("DESCRIPTION").toString()));
            }
        }
        return proposalTypes;
    }
    
    /**
     *  This method used populates combo box with Proposal types item 
     *  from OSP$PROPOSAL_TYPE.
     *  <li>To fetch the data, it uses the procedure DW_GET_IDC_RATE_TYPE.
     *
     *  @return CoeusVector map of all Proposal types
     *
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getIDCRateType() throws CoeusException,DBException{
        Vector result = null;
        CoeusVector idcRateType = null;
        Vector param= new Vector();
        HashMap hshIdcRateTypes = null;
        if(dbEngine != null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
                "call DW_GET_IDC_RATE_TYPE( <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int typeCount = result.size();
        if (typeCount > 0){
            idcRateType = new CoeusVector();
            for(int rowIndex=0; rowIndex<typeCount; rowIndex++){
                hshIdcRateTypes = (HashMap)result.elementAt(rowIndex);
                idcRateType.addElement(new ComboBoxBean(
                        hshIdcRateTypes.get(
                        "IDC_RATE_TYPE_CODE").toString(),
                        hshIdcRateTypes.get("DESCRIPTION").toString()));
            }
        }
        return idcRateType;
    }    
    
    /**
     *  This method used populates combo box with Cost Sharing types item 
     *  from OSP$COST_SHARING_TYPE.
     *  <li>To fetch the data, it uses the procedure DW_GET_COST_SHARING_TYPE.
     *
     *  @return CoeusVector map of all Cost Sharing types
     *
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getCostSharingType() throws CoeusException,DBException{
        Vector result = null;
        CoeusVector cvTypes = null;
        Vector param= new Vector();
        HashMap hshTypes = null;
        if(dbEngine != null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
                "call DW_GET_COST_SHARING_TYPE( <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int typeCount = result.size();
        if (typeCount > 0){
            cvTypes = new CoeusVector();
            for(int rowIndex=0; rowIndex<typeCount; rowIndex++){
                hshTypes = (HashMap)result.elementAt(rowIndex);
                cvTypes.addElement(new ComboBoxBean(
                        hshTypes.get(
                        "COST_SHARING_TYPE_CODE").toString(),
                        hshTypes.get("DESCRIPTION").toString()));
            }
        }
        return cvTypes;
    }    
    
    /**
     *  This method used to populate combo box with Cost Sharing types item 
     *  from OSP$IP_REVIEW_REQ_TYPE
     *  <li>To fetch the data, it uses the procedure DW_GET_IP_REVIEW_REQ_TYPE.
     *
     *  @return CoeusVector map of all Review Requirments types
     *
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getIPReviewRequirementType() throws CoeusException,DBException{
        Vector result = null;
        CoeusVector cvTypes = null;
        Vector param= new Vector();
        HashMap hshTypes = null;
        if(dbEngine != null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
                "call DW_GET_IP_REVIEW_REQ_TYPE( <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int typeCount = result.size();
        if (typeCount > 0){
            cvTypes = new CoeusVector();
            for(int rowIndex=0; rowIndex<typeCount; rowIndex++){
                hshTypes = (HashMap)result.elementAt(rowIndex);
                cvTypes.addElement(new ComboBoxBean(
                        hshTypes.get(
                        "IP_REVIEW_REQ_TYPE_CODE").toString(),
                        hshTypes.get("DESCRIPTION").toString()));
            }
        }
        return cvTypes;
    }    
    
     /**
     *  This method is used to populate combo box with Cost Sharing types item 
     *  from OSP$IP_REVIEW_RESULT_TYPE
     *  <li>To fetch the data, it uses the procedure DW_GET_IP_REVIEW_RESULT_TYPE.
     *
     *  @return CoeusVector map of all Review Result types
     *
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getIPReviewResultType() throws CoeusException,DBException{
        Vector result = null;
        CoeusVector cvTypes = null;
        Vector param= new Vector();
        HashMap hshTypes = null;
        if(dbEngine != null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
                "call DW_GET_IP_REVIEW_RESULT_TYPE( <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int typeCount = result.size();
        if (typeCount > 0){
            cvTypes = new CoeusVector();
            for(int rowIndex=0; rowIndex<typeCount; rowIndex++){
                hshTypes = (HashMap)result.elementAt(rowIndex);
                cvTypes.addElement(new ComboBoxBean(
                        hshTypes.get(
                        "IP_REVIEW_RESULT_TYPE_CODE").toString(),
                        hshTypes.get("DESCRIPTION").toString()));
            }
        }
        return cvTypes;
    }    
    
    /**
     *  This method is used to populate combo box with Cost Sharing types item 
     *  from OSP$IP_REVIEW_ACTIVITY_TYPE
     *  <li>To fetch the data, it uses the procedure DW_GET_IP_REVIEW_ACTIVITY_TYPE.
     *
     *  @return CoeusVector map of all Review Activity types
     *
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getIPReviewActivityType() throws CoeusException,DBException{
        Vector result = null;
        CoeusVector cvTypes = null;
        Vector param= new Vector();
        HashMap hshTypes = null;
        if(dbEngine != null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
                "call DW_GET_IP_REVIEW_ACTIVITY_TYPE( <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int typeCount = result.size();
        if (typeCount > 0){
            cvTypes = new CoeusVector();
            for(int rowIndex=0; rowIndex<typeCount; rowIndex++){
                hshTypes = (HashMap)result.elementAt(rowIndex);
                cvTypes.addElement(new ComboBoxBean(
                        hshTypes.get(
                        "IP_REVIEW_ACTIVITY_TYPE_CODE").toString(),
                        hshTypes.get("DESCRIPTION").toString()));
            }
        }
        return cvTypes;
    }        
    
    /**
     *  This method is used to populate combo box with Proposal Log Status item 
     *  <li>To fetch the data, it uses the procedure GET_PROPOSAL_LOG_STATUS.
     *
     *  @return CoeusVector of ComboBoxBeans
     *
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getProposalLogStatus() throws CoeusException,DBException{
        Vector result = null;
        CoeusVector cvTypes = null;
        Vector param= new Vector();
        HashMap hshTypes = null;
        if(dbEngine != null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
                "call GET_PROPOSAL_LOG_STATUS( <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int typeCount = result.size();
        if (typeCount > 0){
            cvTypes = new CoeusVector();
            for(int rowIndex=0; rowIndex<typeCount; rowIndex++){
                hshTypes = (HashMap)result.elementAt(rowIndex);
                cvTypes.addElement(new ComboBoxBean(
                        hshTypes.get("CODE").toString(),
                        hshTypes.get("DESCRIPTION").toString()));
            }
        }
        return cvTypes;
    }      

}