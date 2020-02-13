/*
 * @(#)CoeusDataTxnBean.java 1.0 08/19/03 1:15 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.bean;

import java.sql.SQLException;
import java.util.Vector;
import java.util.HashMap;

import edu.mit.coeus.utils.dbengine.*;
import edu.mit.coeus.exception.CoeusException;

/**
 * This class contains methods implementing common procedures used in more than one module.
 * Various methods are used to fetch the data from the Database.
 *
 * @version 1.0 on August 19, 2003, 1:15 PM
 * @author  Prasann Kumar
 */

public class CoeusDataTxnBean {
    
    // Instance of a dbEngine
    private DBEngineImpl dbEngine;
    
    /** Creates a new instance of CoeusDataTxnBean */
    public CoeusDataTxnBean() {
        dbEngine = new DBEngineImpl();
    }
    
    
    /**
     * Method used to check whether the given Development Proposal Number is valid or not.
     * <li>To fetch the data, it uses FN_IS_VALID_DEV_PROP_NUM procedure.
     *
     * @param proposalNumber Development Proposal Number
     * @return boolean indicating whether it is valid or not.
     * @exception DBException if the instance of a dbEngine is null.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public boolean isValidDevProposalNumber(String proposalNumber)
    throws CoeusException, DBException{
        int count = 0;
        boolean isValid = false;
        Vector param= new Vector();
        Vector result = new Vector();
        param.add(new Parameter("PROPOSAL_NUMBER",
        DBEngineConstants.TYPE_STRING,proposalNumber));
        /* calling stored function */
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
            "{ <<OUT INTEGER COUNT>> = "
            +" call FN_IS_VALID_DEV_PROP_NUM(<< PROPOSAL_NUMBER >> ) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            isValid = Integer.parseInt(rowParameter.get("COUNT").toString()) > 0 ? true : false;
        }
        return isValid;
    }
    
    /**
     * Method used to check whether the given Institute Proposal Number is valid or not.
     * <li>To fetch the data, it uses FN_IS_VALID_INST_PROP_NUM procedure.
     *
     * @param proposalNumber Institute Proposal Number
     * @return boolean indicating whether it is valid or not.
     * @exception DBException if the instance of a dbEngine is null.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public boolean isValidInstProposalNumber(String proposalNumber)
    throws CoeusException, DBException{
        int count = 0;
        boolean isValid = false;
        Vector param= new Vector();
        Vector result = new Vector();
        param.add(new Parameter("PROPOSAL_NUMBER",
        DBEngineConstants.TYPE_STRING,proposalNumber));
        /* calling stored function */
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
            "{ <<OUT INTEGER COUNT>> = "
            +" call FN_IS_VALID_INST_PROP_NUM(<< PROPOSAL_NUMBER >> ) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            isValid = Integer.parseInt(rowParameter.get("COUNT").toString()) > 0 ? true : false;
        }
        return isValid;
    }
    
    /**  For the Coeus Enhancement case:#1799  start 
     * This method used valid the protocol number enter by the user
     *  if it is valid it will return 1 else -1
     *  <li>To fetch the data, it uses the function fn_is_valid_protocol_number.
     *
     * @return int count for the protocol number
     * @param awardNumber protocol Number
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available. 
     **/
    public boolean validateProtocolNumber(String protocolNumber)
    throws CoeusException, DBException {
        int count = 0;
        boolean isValid = false;
        Vector param= new Vector();
        Vector result = new Vector();
        param.add(new Parameter("PROTOCOL_NUMBER",
        DBEngineConstants.TYPE_STRING,protocolNumber));
        /* calling stored function */
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
            "{ <<OUT INTEGER COUNT>> = "
            +" call fn_is_valid_protocol_number(<< PROTOCOL_NUMBER >> ) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            isValid = Integer.parseInt(rowParameter.get("COUNT").toString()) > 0 ? true : false;
        }
        return isValid;
    }
    //End Coeus Enhancement case:#1799 
    // COEUSQA-1724: IACUC module -Start
    public boolean validateIacucProtocolNumber(String protocolNumber)
    throws CoeusException, DBException {
        int count = 0;
        boolean isValid = false;
        Vector param= new Vector();
        Vector result = new Vector();
        param.add(new Parameter("PROTOCOL_NUMBER",
        DBEngineConstants.TYPE_STRING,protocolNumber));
        /* calling stored function */
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
            "{ <<OUT INTEGER COUNT>> = "
            +" call fn_is_valid_ac_protocol_number(<< PROTOCOL_NUMBER >> ) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            isValid = Integer.parseInt(rowParameter.get("COUNT").toString()) > 0 ? true : false;
        }
        return isValid;
    }
    // COEUSQA-1724: IACUC module - End
}
